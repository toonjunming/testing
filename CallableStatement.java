package com.mysql.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CallableStatement extends PreparedStatement implements CallableStatement {
  public static final Constructor<?> JDBC_4_CSTMT_2_ARGS_CTOR;
  
  public static final Constructor<?> JDBC_4_CSTMT_4_ARGS_CTOR;
  
  private static final int NOT_OUTPUT_PARAMETER_INDICATOR = -2147483648;
  
  private static final String PARAMETER_NAMESPACE_PREFIX = "@com_mysql_jdbc_outparam_";
  
  private boolean callingStoredFunction = false;
  
  private ResultSetInternalMethods functionReturnValueResults;
  
  private boolean hasOutputParams = false;
  
  public boolean outputParamWasNull = false;
  
  private ResultSetInternalMethods outputParameterResults;
  
  public CallableStatementParamInfo paramInfo;
  
  private int[] parameterIndexToRsIndex;
  
  private int[] placeholderToParameterIndexMap;
  
  private CallableStatementParam returnValueParam;
  
  static {
    if (Util.isJdbc4()) {
      try {
        String str;
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.JDBC42CallableStatement";
        } else {
          str = "com.mysql.jdbc.JDBC4CallableStatement";
        } 
        JDBC_4_CSTMT_2_ARGS_CTOR = Class.forName(str).getConstructor(new Class[] { MySQLConnection.class, CallableStatementParamInfo.class });
        JDBC_4_CSTMT_4_ARGS_CTOR = Class.forName(str).getConstructor(new Class[] { MySQLConnection.class, String.class, String.class, boolean.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_CSTMT_4_ARGS_CTOR = null;
      JDBC_4_CSTMT_2_ARGS_CTOR = null;
    } 
  }
  
  public CallableStatement(MySQLConnection paramMySQLConnection, CallableStatementParamInfo paramCallableStatementParamInfo) throws SQLException {
    super(paramMySQLConnection, paramCallableStatementParamInfo.nativeSql, paramCallableStatementParamInfo.catalogInUse);
    this.paramInfo = paramCallableStatementParamInfo;
    boolean bool = paramCallableStatementParamInfo.isFunctionCall;
    this.callingStoredFunction = bool;
    if (bool)
      this.parameterCount++; 
    this.retrieveGeneratedKeys = true;
  }
  
  public CallableStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2);
    this.callingStoredFunction = paramBoolean;
    if (!paramBoolean) {
      if (!StringUtils.startsWithIgnoreCaseAndWs(paramString1, "CALL")) {
        fakeParameterTypes(false);
      } else {
        determineParameterTypes();
      } 
      generateParameterMap();
    } else {
      determineParameterTypes();
      generateParameterMap();
      this.parameterCount++;
    } 
    this.retrieveGeneratedKeys = true;
  }
  
  private CallableStatementParam checkIsOutputParam(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: iload_1
    //   15: istore_2
    //   16: aload_0
    //   17: getfield callingStoredFunction : Z
    //   20: ifeq -> 80
    //   23: iload_1
    //   24: iconst_1
    //   25: if_icmpne -> 76
    //   28: aload_0
    //   29: getfield returnValueParam : Lcom/mysql/jdbc/CallableStatement$CallableStatementParam;
    //   32: ifnonnull -> 64
    //   35: new com/mysql/jdbc/CallableStatement$CallableStatementParam
    //   38: astore #5
    //   40: aload #5
    //   42: ldc ''
    //   44: iconst_0
    //   45: iconst_0
    //   46: iconst_1
    //   47: bipush #12
    //   49: ldc 'VARCHAR'
    //   51: iconst_0
    //   52: iconst_0
    //   53: iconst_2
    //   54: iconst_5
    //   55: invokespecial <init> : (Ljava/lang/String;IZZILjava/lang/String;IISI)V
    //   58: aload_0
    //   59: aload #5
    //   61: putfield returnValueParam : Lcom/mysql/jdbc/CallableStatement$CallableStatementParam;
    //   64: aload_0
    //   65: getfield returnValueParam : Lcom/mysql/jdbc/CallableStatement$CallableStatementParam;
    //   68: astore #5
    //   70: aload #4
    //   72: monitorexit
    //   73: aload #5
    //   75: areturn
    //   76: iload_1
    //   77: iconst_1
    //   78: isub
    //   79: istore_2
    //   80: aload_0
    //   81: iload_2
    //   82: invokespecial checkParameterIndexBounds : (I)V
    //   85: iload_2
    //   86: iconst_1
    //   87: isub
    //   88: istore_3
    //   89: aload_0
    //   90: getfield placeholderToParameterIndexMap : [I
    //   93: astore #5
    //   95: iload_3
    //   96: istore_1
    //   97: aload #5
    //   99: ifnull -> 107
    //   102: aload #5
    //   104: iload_3
    //   105: iaload
    //   106: istore_1
    //   107: aload_0
    //   108: getfield paramInfo : Lcom/mysql/jdbc/CallableStatement$CallableStatementParamInfo;
    //   111: iload_1
    //   112: invokevirtual getParameter : (I)Lcom/mysql/jdbc/CallableStatement$CallableStatementParam;
    //   115: astore #5
    //   117: aload_0
    //   118: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   121: invokeinterface getNoAccessToProcedureBodies : ()Z
    //   126: ifeq -> 150
    //   129: aload #5
    //   131: iconst_1
    //   132: putfield isOut : Z
    //   135: aload #5
    //   137: iconst_1
    //   138: putfield isIn : Z
    //   141: aload #5
    //   143: iconst_2
    //   144: putfield inOutModifier : I
    //   147: goto -> 158
    //   150: aload #5
    //   152: getfield isOut : Z
    //   155: ifeq -> 169
    //   158: aload_0
    //   159: iconst_1
    //   160: putfield hasOutputParams : Z
    //   163: aload #4
    //   165: monitorexit
    //   166: aload #5
    //   168: areturn
    //   169: new java/lang/StringBuilder
    //   172: astore #5
    //   174: aload #5
    //   176: invokespecial <init> : ()V
    //   179: aload #5
    //   181: ldc 'CallableStatement.9'
    //   183: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: pop
    //   190: aload #5
    //   192: iload_2
    //   193: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   196: pop
    //   197: aload #5
    //   199: ldc 'CallableStatement.10'
    //   201: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   204: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: aload #5
    //   210: invokevirtual toString : ()Ljava/lang/String;
    //   213: ldc 'S1009'
    //   215: aload_0
    //   216: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   219: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   222: athrow
    //   223: astore #5
    //   225: aload #4
    //   227: monitorexit
    //   228: aload #5
    //   230: athrow
    // Exception table:
    //   from	to	target	type
    //   16	23	223	finally
    //   28	64	223	finally
    //   64	73	223	finally
    //   80	85	223	finally
    //   89	95	223	finally
    //   107	147	223	finally
    //   150	158	223	finally
    //   158	166	223	finally
    //   169	223	223	finally
    //   225	228	223	finally
  }
  
  private void checkParameterIndexBounds(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.paramInfo.checkBounds(paramInt);
      return;
    } 
  }
  
  private boolean checkReadOnlyProcedure() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #10
    //   11: aload #10
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   18: invokeinterface getNoAccessToProcedureBodies : ()Z
    //   23: ifeq -> 31
    //   26: aload #10
    //   28: monitorexit
    //   29: iconst_0
    //   30: ireturn
    //   31: aload_0
    //   32: getfield paramInfo : Lcom/mysql/jdbc/CallableStatement$CallableStatementParamInfo;
    //   35: astore_2
    //   36: aload_2
    //   37: getfield isReadOnlySafeChecked : Z
    //   40: ifeq -> 53
    //   43: aload_2
    //   44: getfield isReadOnlySafeProcedure : Z
    //   47: istore_1
    //   48: aload #10
    //   50: monitorexit
    //   51: iload_1
    //   52: ireturn
    //   53: aconst_null
    //   54: astore_3
    //   55: aconst_null
    //   56: astore #4
    //   58: aconst_null
    //   59: astore #7
    //   61: aconst_null
    //   62: astore #8
    //   64: aload_0
    //   65: invokespecial extractProcedureName : ()Ljava/lang/String;
    //   68: astore #9
    //   70: aload_0
    //   71: getfield currentCatalog : Ljava/lang/String;
    //   74: astore #5
    //   76: aload #9
    //   78: astore #6
    //   80: aload #9
    //   82: ldc '.'
    //   84: invokevirtual indexOf : (Ljava/lang/String;)I
    //   87: iconst_m1
    //   88: if_icmpeq -> 185
    //   91: aload #9
    //   93: iconst_0
    //   94: aload #9
    //   96: ldc '.'
    //   98: invokevirtual indexOf : (Ljava/lang/String;)I
    //   101: invokevirtual substring : (II)Ljava/lang/String;
    //   104: astore #5
    //   106: aload #5
    //   108: astore_2
    //   109: aload #5
    //   111: ldc_w '`'
    //   114: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
    //   117: ifeq -> 151
    //   120: aload #5
    //   122: astore_2
    //   123: aload #5
    //   125: invokevirtual trim : ()Ljava/lang/String;
    //   128: ldc_w '`'
    //   131: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   134: ifeq -> 151
    //   137: aload #5
    //   139: iconst_1
    //   140: aload #5
    //   142: invokevirtual length : ()I
    //   145: iconst_1
    //   146: isub
    //   147: invokevirtual substring : (II)Ljava/lang/String;
    //   150: astore_2
    //   151: aload #9
    //   153: aload #9
    //   155: ldc '.'
    //   157: invokevirtual indexOf : (Ljava/lang/String;)I
    //   160: iconst_1
    //   161: iadd
    //   162: invokevirtual substring : (I)Ljava/lang/String;
    //   165: invokestatic getBytes : (Ljava/lang/String;)[B
    //   168: ldc_w '`'
    //   171: ldc_w '`'
    //   174: invokestatic stripEnclosure : ([BLjava/lang/String;Ljava/lang/String;)[B
    //   177: invokestatic toString : ([B)Ljava/lang/String;
    //   180: astore #6
    //   182: aload_2
    //   183: astore #5
    //   185: aload_0
    //   186: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   189: ldc_w 'SELECT SQL_DATA_ACCESS FROM information_schema.routines WHERE routine_schema = ? AND routine_name = ?'
    //   192: invokeinterface prepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   197: astore_2
    //   198: aload #8
    //   200: astore_3
    //   201: aload #7
    //   203: astore #4
    //   205: aload_2
    //   206: iconst_0
    //   207: invokeinterface setMaxRows : (I)V
    //   212: aload #8
    //   214: astore_3
    //   215: aload #7
    //   217: astore #4
    //   219: aload_2
    //   220: iconst_0
    //   221: invokeinterface setFetchSize : (I)V
    //   226: aload #8
    //   228: astore_3
    //   229: aload #7
    //   231: astore #4
    //   233: aload_2
    //   234: iconst_1
    //   235: aload #5
    //   237: invokeinterface setString : (ILjava/lang/String;)V
    //   242: aload #8
    //   244: astore_3
    //   245: aload #7
    //   247: astore #4
    //   249: aload_2
    //   250: iconst_2
    //   251: aload #6
    //   253: invokeinterface setString : (ILjava/lang/String;)V
    //   258: aload #8
    //   260: astore_3
    //   261: aload #7
    //   263: astore #4
    //   265: aload_2
    //   266: invokeinterface executeQuery : ()Ljava/sql/ResultSet;
    //   271: astore #5
    //   273: aload #5
    //   275: astore_3
    //   276: aload #5
    //   278: astore #4
    //   280: aload #5
    //   282: invokeinterface next : ()Z
    //   287: ifeq -> 426
    //   290: aload #5
    //   292: astore_3
    //   293: aload #5
    //   295: astore #4
    //   297: aload #5
    //   299: iconst_1
    //   300: invokeinterface getString : (I)Ljava/lang/String;
    //   305: astore #6
    //   307: aload #5
    //   309: astore_3
    //   310: aload #5
    //   312: astore #4
    //   314: ldc_w 'READS SQL DATA'
    //   317: aload #6
    //   319: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   322: ifne -> 343
    //   325: aload #5
    //   327: astore_3
    //   328: aload #5
    //   330: astore #4
    //   332: ldc_w 'NO SQL'
    //   335: aload #6
    //   337: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   340: ifeq -> 426
    //   343: aload #5
    //   345: astore_3
    //   346: aload #5
    //   348: astore #4
    //   350: aload_0
    //   351: getfield paramInfo : Lcom/mysql/jdbc/CallableStatement$CallableStatementParamInfo;
    //   354: astore #6
    //   356: aload #5
    //   358: astore_3
    //   359: aload #5
    //   361: astore #4
    //   363: aload #6
    //   365: monitorenter
    //   366: aload_0
    //   367: getfield paramInfo : Lcom/mysql/jdbc/CallableStatement$CallableStatementParamInfo;
    //   370: astore_3
    //   371: aload_3
    //   372: iconst_1
    //   373: putfield isReadOnlySafeChecked : Z
    //   376: aload_3
    //   377: iconst_1
    //   378: putfield isReadOnlySafeProcedure : Z
    //   381: aload #6
    //   383: monitorexit
    //   384: aload #5
    //   386: ifnull -> 396
    //   389: aload #5
    //   391: invokeinterface close : ()V
    //   396: aload_2
    //   397: ifnull -> 406
    //   400: aload_2
    //   401: invokeinterface close : ()V
    //   406: aload #10
    //   408: monitorexit
    //   409: iconst_1
    //   410: ireturn
    //   411: astore #7
    //   413: aload #6
    //   415: monitorexit
    //   416: aload #5
    //   418: astore_3
    //   419: aload #5
    //   421: astore #4
    //   423: aload #7
    //   425: athrow
    //   426: aload #5
    //   428: ifnull -> 438
    //   431: aload #5
    //   433: invokeinterface close : ()V
    //   438: aload_2
    //   439: ifnull -> 505
    //   442: aload_2
    //   443: invokeinterface close : ()V
    //   448: goto -> 505
    //   451: astore #4
    //   453: goto -> 460
    //   456: astore #4
    //   458: aconst_null
    //   459: astore_2
    //   460: aload_3
    //   461: ifnull -> 470
    //   464: aload_3
    //   465: invokeinterface close : ()V
    //   470: aload_2
    //   471: ifnull -> 480
    //   474: aload_2
    //   475: invokeinterface close : ()V
    //   480: aload #4
    //   482: athrow
    //   483: astore_2
    //   484: aconst_null
    //   485: astore_2
    //   486: aload #4
    //   488: ifnull -> 498
    //   491: aload #4
    //   493: invokeinterface close : ()V
    //   498: aload_2
    //   499: ifnull -> 505
    //   502: goto -> 442
    //   505: aload_0
    //   506: getfield paramInfo : Lcom/mysql/jdbc/CallableStatement$CallableStatementParamInfo;
    //   509: astore_2
    //   510: aload_2
    //   511: iconst_0
    //   512: putfield isReadOnlySafeChecked : Z
    //   515: aload_2
    //   516: iconst_0
    //   517: putfield isReadOnlySafeProcedure : Z
    //   520: aload #10
    //   522: monitorexit
    //   523: iconst_0
    //   524: ireturn
    //   525: astore_2
    //   526: aload #10
    //   528: monitorexit
    //   529: aload_2
    //   530: athrow
    //   531: astore_3
    //   532: goto -> 486
    // Exception table:
    //   from	to	target	type
    //   14	29	525	finally
    //   31	51	525	finally
    //   64	76	483	java/sql/SQLException
    //   64	76	456	finally
    //   80	106	483	java/sql/SQLException
    //   80	106	456	finally
    //   109	120	483	java/sql/SQLException
    //   109	120	456	finally
    //   123	151	483	java/sql/SQLException
    //   123	151	456	finally
    //   151	182	483	java/sql/SQLException
    //   151	182	456	finally
    //   185	198	483	java/sql/SQLException
    //   185	198	456	finally
    //   205	212	531	java/sql/SQLException
    //   205	212	451	finally
    //   219	226	531	java/sql/SQLException
    //   219	226	451	finally
    //   233	242	531	java/sql/SQLException
    //   233	242	451	finally
    //   249	258	531	java/sql/SQLException
    //   249	258	451	finally
    //   265	273	531	java/sql/SQLException
    //   265	273	451	finally
    //   280	290	531	java/sql/SQLException
    //   280	290	451	finally
    //   297	307	531	java/sql/SQLException
    //   297	307	451	finally
    //   314	325	531	java/sql/SQLException
    //   314	325	451	finally
    //   332	343	531	java/sql/SQLException
    //   332	343	451	finally
    //   350	356	531	java/sql/SQLException
    //   350	356	451	finally
    //   363	366	531	java/sql/SQLException
    //   363	366	451	finally
    //   366	384	411	finally
    //   389	396	525	finally
    //   400	406	525	finally
    //   406	409	525	finally
    //   413	416	411	finally
    //   423	426	531	java/sql/SQLException
    //   423	426	451	finally
    //   431	438	525	finally
    //   442	448	525	finally
    //   464	470	525	finally
    //   474	480	525	finally
    //   480	483	525	finally
    //   491	498	525	finally
    //   505	523	525	finally
    //   526	529	525	finally
  }
  
  private void checkStreamability() throws SQLException {
    if (!this.hasOutputParams || !createStreamingResultSet())
      return; 
    throw SQLError.createSQLException(Messages.getString("CallableStatement.14"), "S1C00", getExceptionInterceptor());
  }
  
  private void convertGetProcedureColumnsToInternalDescriptors(ResultSet paramResultSet) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      CallableStatementParamInfo callableStatementParamInfo = new CallableStatementParamInfo();
      this(this, paramResultSet);
      this.paramInfo = callableStatementParamInfo;
      return;
    } 
  }
  
  private void determineParameterTypes() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #8
    //   11: aload #8
    //   13: monitorenter
    //   14: aconst_null
    //   15: astore #6
    //   17: aconst_null
    //   18: astore #7
    //   20: aload_0
    //   21: invokespecial extractProcedureName : ()Ljava/lang/String;
    //   24: astore #4
    //   26: ldc ''
    //   28: astore_3
    //   29: aload_0
    //   30: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   33: invokeinterface supportsQuotedIdentifiers : ()Z
    //   38: ifeq -> 63
    //   41: aload_0
    //   42: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   45: invokeinterface getMetaData : ()Ljava/sql/DatabaseMetaData;
    //   50: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
    //   55: astore #5
    //   57: aload #5
    //   59: astore_3
    //   60: goto -> 76
    //   63: ldc ''
    //   65: astore_3
    //   66: goto -> 76
    //   69: astore #5
    //   71: aload #5
    //   73: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   76: aload #4
    //   78: ldc ''
    //   80: aload_3
    //   81: aload_0
    //   82: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   85: invokeinterface isNoBackslashEscapesSet : ()Z
    //   90: invokestatic splitDBdotName : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
    //   93: astore #5
    //   95: ldc ''
    //   97: astore_3
    //   98: aload #5
    //   100: invokeinterface size : ()I
    //   105: istore_1
    //   106: iconst_0
    //   107: istore_2
    //   108: iload_1
    //   109: iconst_2
    //   110: if_icmpne -> 138
    //   113: aload #5
    //   115: iconst_0
    //   116: invokeinterface get : (I)Ljava/lang/Object;
    //   121: checkcast java/lang/String
    //   124: astore_3
    //   125: aload #5
    //   127: iconst_1
    //   128: invokeinterface get : (I)Ljava/lang/Object;
    //   133: checkcast java/lang/String
    //   136: astore #4
    //   138: aload_0
    //   139: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   142: invokeinterface getMetaData : ()Ljava/sql/DatabaseMetaData;
    //   147: astore #9
    //   149: aload_3
    //   150: invokevirtual length : ()I
    //   153: ifgt -> 161
    //   156: iconst_1
    //   157: istore_1
    //   158: goto -> 163
    //   161: iconst_0
    //   162: istore_1
    //   163: aload_3
    //   164: astore #5
    //   166: aload_0
    //   167: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   170: iconst_5
    //   171: iconst_0
    //   172: iconst_2
    //   173: invokeinterface versionMeetsMinimum : (III)Z
    //   178: ifeq -> 194
    //   181: aload_3
    //   182: astore #5
    //   184: iload_1
    //   185: ifeq -> 194
    //   188: aload_0
    //   189: getfield currentCatalog : Ljava/lang/String;
    //   192: astore #5
    //   194: aload #9
    //   196: aload #5
    //   198: aconst_null
    //   199: aload #4
    //   201: ldc_w '%'
    //   204: invokeinterface getProcedureColumns : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
    //   209: astore #4
    //   211: iload_2
    //   212: istore_1
    //   213: aload #4
    //   215: invokeinterface next : ()Z
    //   220: ifeq -> 243
    //   223: aload #4
    //   225: invokeinterface previous : ()Z
    //   230: pop
    //   231: iconst_1
    //   232: istore_1
    //   233: goto -> 243
    //   236: astore_3
    //   237: goto -> 297
    //   240: astore_3
    //   241: iload_2
    //   242: istore_1
    //   243: iload_1
    //   244: ifeq -> 256
    //   247: aload_0
    //   248: aload #4
    //   250: invokespecial convertGetProcedureColumnsToInternalDescriptors : (Ljava/sql/ResultSet;)V
    //   253: goto -> 261
    //   256: aload_0
    //   257: iconst_1
    //   258: invokespecial fakeParameterTypes : (Z)V
    //   261: aload #7
    //   263: astore_3
    //   264: aload #4
    //   266: ifnull -> 283
    //   269: aload #4
    //   271: invokeinterface close : ()V
    //   276: aload #7
    //   278: astore_3
    //   279: goto -> 283
    //   282: astore_3
    //   283: aload_3
    //   284: ifnonnull -> 291
    //   287: aload #8
    //   289: monitorexit
    //   290: return
    //   291: aload_3
    //   292: athrow
    //   293: astore_3
    //   294: aconst_null
    //   295: astore #4
    //   297: aload #6
    //   299: astore #5
    //   301: aload #4
    //   303: ifnull -> 326
    //   306: aload #4
    //   308: invokeinterface close : ()V
    //   313: aload #6
    //   315: astore #5
    //   317: goto -> 326
    //   320: astore_3
    //   321: goto -> 336
    //   324: astore #5
    //   326: aload #5
    //   328: ifnull -> 334
    //   331: aload #5
    //   333: athrow
    //   334: aload_3
    //   335: athrow
    //   336: aload #8
    //   338: monitorexit
    //   339: aload_3
    //   340: athrow
    // Exception table:
    //   from	to	target	type
    //   20	26	293	finally
    //   29	57	69	java/sql/SQLException
    //   29	57	293	finally
    //   71	76	293	finally
    //   76	95	293	finally
    //   98	106	293	finally
    //   113	138	293	finally
    //   138	156	293	finally
    //   166	181	293	finally
    //   188	194	293	finally
    //   194	211	293	finally
    //   213	231	240	java/lang/Exception
    //   213	231	236	finally
    //   247	253	236	finally
    //   256	261	236	finally
    //   269	276	282	java/sql/SQLException
    //   269	276	320	finally
    //   287	290	320	finally
    //   291	293	320	finally
    //   306	313	324	java/sql/SQLException
    //   306	313	320	finally
    //   331	334	320	finally
    //   334	336	320	finally
    //   336	339	320	finally
  }
  
  private String extractProcedureName() throws SQLException {
    byte b;
    String str = StringUtils.stripComments(this.originalSql, "`\"'", "`\"'", true, false, true, true);
    int i = StringUtils.indexOfIgnoreCase(str, "CALL ");
    if (i == -1) {
      i = StringUtils.indexOfIgnoreCase(str, "SELECT ");
      b = 7;
    } else {
      b = 5;
    } 
    if (i != -1) {
      StringBuilder stringBuilder = new StringBuilder();
      str = str.substring(i + b).trim();
      i = str.length();
      for (b = 0; b < i; b++) {
        char c = str.charAt(b);
        if (Character.isWhitespace(c) || c == '(' || c == '?')
          break; 
        stringBuilder.append(c);
      } 
      return stringBuilder.toString();
    } 
    throw SQLError.createSQLException(Messages.getString("CallableStatement.1"), "S1000", getExceptionInterceptor());
  }
  
  private void fakeParameterTypes(boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      String str;
      byte[] arrayOfByte;
      Field field3 = new Field();
      this("", "PROCEDURE_CAT", 1, 0);
      Field field11 = new Field();
      this("", "PROCEDURE_SCHEM", 1, 0);
      Field field7 = new Field();
      this("", "PROCEDURE_NAME", 1, 0);
      Field field6 = new Field();
      this("", "COLUMN_NAME", 1, 0);
      Field field12 = new Field();
      this("", "COLUMN_TYPE", 1, 0);
      Field field4 = new Field();
      this("", "DATA_TYPE", 5, 0);
      Field field1 = new Field();
      this("", "TYPE_NAME", 1, 0);
      Field field5 = new Field();
      this("", "PRECISION", 4, 0);
      Field field8 = new Field();
      this("", "LENGTH", 4, 0);
      Field field2 = new Field();
      this("", "SCALE", 5, 0);
      Field field9 = new Field();
      this("", "RADIX", 5, 0);
      Field field10 = new Field();
      this("", "NULLABLE", 5, 0);
      Field field13 = new Field();
      this("", "REMARKS", 1, 0);
      if (paramBoolean) {
        str = extractProcedureName();
      } else {
        str = null;
      } 
      if (str == null) {
        str = null;
      } else {
        try {
          byte[] arrayOfByte1 = StringUtils.getBytes(str, "UTF-8");
          arrayOfByte = arrayOfByte1;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          arrayOfByte = StringUtils.s2b((String)arrayOfByte, this.connection);
        } 
      } 
      ArrayList<ByteArrayRow> arrayList = new ArrayList();
      this();
      for (byte b = 0; b < this.parameterCount; b++) {
        byte[] arrayOfByte8 = StringUtils.s2b(String.valueOf(b), this.connection);
        byte[] arrayOfByte4 = StringUtils.s2b(String.valueOf(1), this.connection);
        byte[] arrayOfByte5 = StringUtils.s2b(String.valueOf(12), this.connection);
        byte[] arrayOfByte3 = StringUtils.s2b("VARCHAR", this.connection);
        byte[] arrayOfByte6 = StringUtils.s2b(Integer.toString(65535), this.connection);
        byte[] arrayOfByte7 = StringUtils.s2b(Integer.toString(65535), this.connection);
        byte[] arrayOfByte1 = StringUtils.s2b(Integer.toString(0), this.connection);
        byte[] arrayOfByte2 = StringUtils.s2b(Integer.toString(10), this.connection);
        byte[] arrayOfByte9 = StringUtils.s2b(Integer.toString(2), this.connection);
        ByteArrayRow byteArrayRow = new ByteArrayRow();
        ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
        this(new byte[][] { 
              null, null, arrayOfByte, arrayOfByte8, arrayOfByte4, arrayOfByte5, arrayOfByte3, arrayOfByte6, arrayOfByte7, arrayOfByte1, 
              arrayOfByte2, arrayOfByte9, null }, exceptionInterceptor);
        arrayList.add(byteArrayRow);
      } 
      MySQLConnection mySQLConnection = this.connection;
      convertGetProcedureColumnsToInternalDescriptors(DatabaseMetaData.buildResultSet(new Field[] { 
              field3, field11, field7, field6, field12, field4, field1, field5, field8, field2, 
              field9, field10, field13 }, (ArrayList)arrayList, mySQLConnection));
      return;
    } 
  }
  
  private void generateParameterMap() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      CallableStatementParamInfo callableStatementParamInfo = this.paramInfo;
      if (callableStatementParamInfo == null)
        return; 
      int j = callableStatementParamInfo.getParameterCount();
      boolean bool = this.callingStoredFunction;
      int i = j;
      if (bool)
        i = j - 1; 
      if (this.paramInfo != null) {
        j = this.parameterCount;
        if (j != i) {
          String str1;
          String str2;
          this.placeholderToParameterIndexMap = new int[j];
          if (bool) {
            str2 = this.originalSql;
            str1 = "SELECT";
          } else {
            str2 = this.originalSql;
            str1 = "CALL";
          } 
          i = StringUtils.indexOfIgnoreCase(str2, str1);
          if (i != -1) {
            j = this.originalSql.indexOf('(', i + 4);
            if (j != -1) {
              i = StringUtils.indexOfIgnoreCase(j, this.originalSql, ")", "'", "'", StringUtils.SEARCH_MODE__ALL);
              if (i != -1) {
                List<String> list = StringUtils.split(this.originalSql.substring(j + 1, i), ",", "'\"", "'\"", true);
                int k = list.size();
                j = 0;
                for (i = 0; j < k; i = m) {
                  int m = i;
                  if (((String)list.get(j)).equals("?")) {
                    this.placeholderToParameterIndexMap[i] = j;
                    m = i + 1;
                  } 
                  j++;
                } 
              } 
            } 
          } 
        } 
      } 
      return;
    } 
  }
  
  public static CallableStatement getInstance(MySQLConnection paramMySQLConnection, CallableStatementParamInfo paramCallableStatementParamInfo) throws SQLException {
    if (!Util.isJdbc4())
      return new CallableStatement(paramMySQLConnection, paramCallableStatementParamInfo); 
    Constructor<?> constructor = JDBC_4_CSTMT_2_ARGS_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (CallableStatement)Util.handleNewInstance(constructor, new Object[] { paramMySQLConnection, paramCallableStatementParamInfo }, exceptionInterceptor);
  }
  
  public static CallableStatement getInstance(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    if (!Util.isJdbc4())
      return new CallableStatement(paramMySQLConnection, paramString1, paramString2, paramBoolean); 
    Constructor<?> constructor = JDBC_4_CSTMT_4_ARGS_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (CallableStatement)Util.handleNewInstance(constructor, new Object[] { paramMySQLConnection, paramString1, paramString2, Boolean.valueOf(paramBoolean) }, exceptionInterceptor);
  }
  
  private boolean hasParametersView() throws SQLException {
    Exception exception;
    Object object = checkClosed().getConnectionMutex();
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      if (this.connection.versionMeetsMinimum(5, 5, 0)) {
        DatabaseMetaDataUsingInfoSchema databaseMetaDataUsingInfoSchema = new DatabaseMetaDataUsingInfoSchema();
        this(this.connection, this.connection.getCatalog());
        boolean bool = databaseMetaDataUsingInfoSchema.gethasParametersView();
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return bool;
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      return false;
    } catch (SQLException null) {
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      return false;
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw exception;
  }
  
  private static String mangleParameterName(String paramString) {
    if (paramString == null)
      return null; 
    int i = paramString.length();
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (i > 0) {
      bool1 = bool2;
      if (paramString.charAt(0) == '@')
        bool1 = true; 
    } 
    StringBuilder stringBuilder = new StringBuilder(25 + paramString.length());
    stringBuilder.append("@com_mysql_jdbc_outparam_");
    stringBuilder.append(paramString.substring(bool1));
    return stringBuilder.toString();
  }
  
  private void retrieveOutParams() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      int i = this.paramInfo.numberOfParameters();
      this.parameterIndexToRsIndex = new int[i];
      byte b;
      for (b = 0; b < i; b++)
        this.parameterIndexToRsIndex[b] = Integer.MIN_VALUE; 
      Statement statement = null;
      if (i > 0) {
        StringBuilder stringBuilder = new StringBuilder();
        this("SELECT ");
        Iterator<CallableStatementParam> iterator = this.paramInfo.iterator();
        boolean bool = false;
        b = 0;
        i = 1;
        while (iterator.hasNext()) {
          CallableStatementParam callableStatementParam = iterator.next();
          if (callableStatementParam.isOut) {
            this.parameterIndexToRsIndex[callableStatementParam.index] = b;
            if (callableStatementParam.paramName == null && hasParametersView()) {
              StringBuilder stringBuilder1 = new StringBuilder();
              this();
              stringBuilder1.append("nullnp");
              stringBuilder1.append(callableStatementParam.index);
              callableStatementParam.paramName = stringBuilder1.toString();
            } 
            String str = mangleParameterName(callableStatementParam.paramName);
            if (i == 0) {
              stringBuilder.append(",");
            } else {
              i = 0;
            } 
            if (!str.startsWith("@"))
              stringBuilder.append('@'); 
            stringBuilder.append(str);
            b++;
            bool = true;
          } 
        } 
        if (bool) {
          try {
            Statement statement1 = this.connection.createStatement();
            try {
              ResultSetInternalMethods resultSetInternalMethods = ((ResultSetInternalMethods)statement1.executeQuery(stringBuilder.toString())).copy();
              this.outputParameterResults = resultSetInternalMethods;
              if (!resultSetInternalMethods.next()) {
                this.outputParameterResults.close();
                this.outputParameterResults = null;
              } 
            } finally {
              stringBuilder = null;
            } 
          } finally {}
          if (statement != null)
            statement.close(); 
          throw stringBuilder;
        } 
        this.outputParameterResults = null;
      } else {
        this.outputParameterResults = null;
      } 
      return;
    } 
  }
  
  private void setInOutParamsOnServer() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      CallableStatementParamInfo callableStatementParamInfo = this.paramInfo;
      if (callableStatementParamInfo.numParameters > 0) {
        Iterator<CallableStatementParam> iterator = callableStatementParamInfo.iterator();
        while (iterator.hasNext()) {
          CallableStatementParam callableStatementParam = iterator.next();
          if (callableStatementParam.isOut && callableStatementParam.isIn) {
            byte[] arrayOfByte1;
            byte[] arrayOfByte2;
            if (callableStatementParam.paramName == null && hasParametersView()) {
              StringBuilder stringBuilder1 = new StringBuilder();
              this();
              stringBuilder1.append("nullnp");
              stringBuilder1.append(callableStatementParam.index);
              callableStatementParam.paramName = stringBuilder1.toString();
            } 
            String str = mangleParameterName(callableStatementParam.paramName);
            StringBuilder stringBuilder = new StringBuilder();
            this(str.length() + 4 + 1 + 1);
            stringBuilder.append("SET ");
            stringBuilder.append(str);
            stringBuilder.append("=?");
            str = null;
            try {
              PreparedStatement preparedStatement = ((Wrapper)this.connection.clientPrepareStatement(stringBuilder.toString())).<PreparedStatement>unwrap(PreparedStatement.class);
            } finally {
              iterator = null;
            } 
            if (arrayOfByte1 != null)
              arrayOfByte1.close(); 
            throw iterator;
          } 
        } 
      } 
      return;
    } 
  }
  
  private void setOutParams() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      CallableStatementParamInfo callableStatementParamInfo = this.paramInfo;
      if (callableStatementParamInfo.numParameters > 0) {
        Iterator<CallableStatementParam> iterator = callableStatementParamInfo.iterator();
        while (iterator.hasNext()) {
          CallableStatementParam callableStatementParam = iterator.next();
          if (!this.callingStoredFunction && callableStatementParam.isOut) {
            byte b;
            if (callableStatementParam.paramName == null && hasParametersView()) {
              StringBuilder stringBuilder = new StringBuilder();
              this();
              stringBuilder.append("nullnp");
              stringBuilder.append(callableStatementParam.index);
              callableStatementParam.paramName = stringBuilder.toString();
            } 
            String str = mangleParameterName(callableStatementParam.paramName);
            int[] arrayOfInt = this.placeholderToParameterIndexMap;
            boolean bool = true;
            if (arrayOfInt == null) {
              b = callableStatementParam.index + 1;
            } else {
              boolean bool1 = false;
              b = 0;
              while (true) {
                arrayOfInt = this.placeholderToParameterIndexMap;
                if (b < arrayOfInt.length) {
                  if (arrayOfInt[b] == callableStatementParam.index) {
                    b++;
                    break;
                  } 
                  b++;
                  continue;
                } 
                bool = false;
                b = bool1;
                break;
              } 
              if (!bool) {
                StringBuilder stringBuilder = new StringBuilder();
                this();
                stringBuilder.append(Messages.getString("CallableStatement.21"));
                stringBuilder.append(callableStatementParam.paramName);
                stringBuilder.append(Messages.getString("CallableStatement.22"));
                throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
              } 
            } 
            setBytesNoEscapeNoQuotes(b, StringUtils.getBytes(str, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
          } 
        } 
      } 
      return;
    } 
  }
  
  public void addBatch() throws SQLException {
    setOutParams();
    super.addBatch();
  }
  
  public boolean checkReadOnlySafeStatement() throws SQLException {
    return (super.checkReadOnlySafeStatement() || checkReadOnlyProcedure());
  }
  
  public void clearParameters() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      super.clearParameters();
      try {
        ResultSetInternalMethods resultSetInternalMethods = this.outputParameterResults;
        if (resultSetInternalMethods != null)
          resultSetInternalMethods.close(); 
        return;
      } finally {
        this.outputParameterResults = null;
      } 
    } 
  }
  
  public boolean execute() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      checkStreamability();
      setInOutParamsOnServer();
      setOutParams();
      boolean bool = super.execute();
      if (this.callingStoredFunction) {
        ResultSetInternalMethods resultSetInternalMethods = this.results;
        this.functionReturnValueResults = resultSetInternalMethods;
        resultSetInternalMethods.next();
        this.results = null;
      } 
      retrieveOutParams();
      if (!this.callingStoredFunction)
        return bool; 
      return false;
    } 
  }
  
  public int[] executeBatch() throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeBatch());
  }
  
  public long[] executeLargeBatch() throws SQLException {
    if (!this.hasOutputParams)
      return super.executeLargeBatch(); 
    throw SQLError.createSQLException("Can't call executeBatch() on CallableStatement with OUTPUT parameters", "S1009", getExceptionInterceptor());
  }
  
  public long executeLargeUpdate() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      checkStreamability();
      if (this.callingStoredFunction) {
        execute();
        return -1L;
      } 
      setInOutParamsOnServer();
      setOutParams();
      long l = super.executeLargeUpdate();
      retrieveOutParams();
      return l;
    } 
  }
  
  public ResultSet executeQuery() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      checkStreamability();
      setInOutParamsOnServer();
      setOutParams();
      ResultSet resultSet = super.executeQuery();
      retrieveOutParams();
      return resultSet;
    } 
  }
  
  public int executeUpdate() throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeUpdate());
  }
  
  public String fixParameterName(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_1
    //   13: ifnull -> 23
    //   16: aload_1
    //   17: invokevirtual length : ()I
    //   20: ifne -> 92
    //   23: aload_0
    //   24: invokespecial hasParametersView : ()Z
    //   27: ifne -> 92
    //   30: new java/lang/StringBuilder
    //   33: astore_2
    //   34: aload_2
    //   35: invokespecial <init> : ()V
    //   38: aload_2
    //   39: ldc_w 'CallableStatement.0'
    //   42: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: pop
    //   49: aload_2
    //   50: aload_1
    //   51: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: pop
    //   55: aload_2
    //   56: invokevirtual toString : ()Ljava/lang/String;
    //   59: ifnonnull -> 74
    //   62: ldc_w 'CallableStatement.15'
    //   65: astore_1
    //   66: aload_1
    //   67: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   70: astore_1
    //   71: goto -> 81
    //   74: ldc_w 'CallableStatement.16'
    //   77: astore_1
    //   78: goto -> 66
    //   81: aload_1
    //   82: ldc 'S1009'
    //   84: aload_0
    //   85: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   88: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   91: athrow
    //   92: aload_1
    //   93: astore_2
    //   94: aload_1
    //   95: ifnonnull -> 111
    //   98: aload_1
    //   99: astore_2
    //   100: aload_0
    //   101: invokespecial hasParametersView : ()Z
    //   104: ifeq -> 111
    //   107: ldc_w 'nullpn'
    //   110: astore_2
    //   111: aload_0
    //   112: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   115: invokeinterface getNoAccessToProcedureBodies : ()Z
    //   120: ifne -> 132
    //   123: aload_2
    //   124: invokestatic mangleParameterName : (Ljava/lang/String;)Ljava/lang/String;
    //   127: astore_1
    //   128: aload_3
    //   129: monitorexit
    //   130: aload_1
    //   131: areturn
    //   132: ldc_w 'No access to parameters by name when connection has been configured not to access procedure bodies'
    //   135: ldc 'S1009'
    //   137: aload_0
    //   138: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   141: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   144: athrow
    //   145: astore_1
    //   146: aload_3
    //   147: monitorexit
    //   148: aload_1
    //   149: athrow
    // Exception table:
    //   from	to	target	type
    //   16	23	145	finally
    //   23	62	145	finally
    //   66	71	145	finally
    //   81	92	145	finally
    //   100	107	145	finally
    //   111	130	145	finally
    //   132	145	145	finally
    //   146	148	145	finally
  }
  
  public Array getArray(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Array array = resultSetInternalMethods.getArray(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return array;
    } 
  }
  
  public Array getArray(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Array array = resultSetInternalMethods.getArray(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return array;
    } 
  }
  
  public BigDecimal getBigDecimal(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      BigDecimal bigDecimal = resultSetInternalMethods.getBigDecimal(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return bigDecimal;
    } 
  }
  
  @Deprecated
  public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt1);
      BigDecimal bigDecimal = resultSetInternalMethods.getBigDecimal(mapOutputParameterIndexToRsIndex(paramInt1), paramInt2);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return bigDecimal;
    } 
  }
  
  public BigDecimal getBigDecimal(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      BigDecimal bigDecimal = resultSetInternalMethods.getBigDecimal(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return bigDecimal;
    } 
  }
  
  public Blob getBlob(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Blob blob = resultSetInternalMethods.getBlob(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return blob;
    } 
  }
  
  public Blob getBlob(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Blob blob = resultSetInternalMethods.getBlob(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return blob;
    } 
  }
  
  public boolean getBoolean(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      boolean bool = resultSetInternalMethods.getBoolean(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return bool;
    } 
  }
  
  public boolean getBoolean(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      boolean bool = resultSetInternalMethods.getBoolean(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return bool;
    } 
  }
  
  public byte getByte(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      byte b = resultSetInternalMethods.getByte(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return b;
    } 
  }
  
  public byte getByte(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      byte b = resultSetInternalMethods.getByte(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return b;
    } 
  }
  
  public byte[] getBytes(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      byte[] arrayOfByte = resultSetInternalMethods.getBytes(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return arrayOfByte;
    } 
  }
  
  public byte[] getBytes(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      byte[] arrayOfByte = resultSetInternalMethods.getBytes(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return arrayOfByte;
    } 
  }
  
  public Clob getClob(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Clob clob = resultSetInternalMethods.getClob(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return clob;
    } 
  }
  
  public Clob getClob(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Clob clob = resultSetInternalMethods.getClob(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return clob;
    } 
  }
  
  public Date getDate(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Date date = resultSetInternalMethods.getDate(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return date;
    } 
  }
  
  public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Date date = resultSetInternalMethods.getDate(mapOutputParameterIndexToRsIndex(paramInt), paramCalendar);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return date;
    } 
  }
  
  public Date getDate(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Date date = resultSetInternalMethods.getDate(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return date;
    } 
  }
  
  public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Date date = resultSetInternalMethods.getDate(fixParameterName(paramString), paramCalendar);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return date;
    } 
  }
  
  public double getDouble(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      double d = resultSetInternalMethods.getDouble(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return d;
    } 
  }
  
  public double getDouble(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      double d = resultSetInternalMethods.getDouble(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return d;
    } 
  }
  
  public float getFloat(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      float f = resultSetInternalMethods.getFloat(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return f;
    } 
  }
  
  public float getFloat(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      float f = resultSetInternalMethods.getFloat(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return f;
    } 
  }
  
  public int getInt(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      paramInt = resultSetInternalMethods.getInt(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return paramInt;
    } 
  }
  
  public int getInt(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      int i = resultSetInternalMethods.getInt(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return i;
    } 
  }
  
  public long getLong(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      long l = resultSetInternalMethods.getLong(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return l;
    } 
  }
  
  public long getLong(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      long l = resultSetInternalMethods.getLong(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return l;
    } 
  }
  
  public int getNamedParamIndex(String paramString, boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.connection.getNoAccessToProcedureBodies()) {
        if (paramString != null && paramString.length() != 0) {
          CallableStatementParamInfo callableStatementParamInfo = this.paramInfo;
          if (callableStatementParamInfo != null) {
            CallableStatementParam callableStatementParam = callableStatementParamInfo.getParameter(paramString);
            if (callableStatementParam != null) {
              if (!paramBoolean || callableStatementParam.isOut) {
                if (this.placeholderToParameterIndexMap == null) {
                  int i = callableStatementParam.index;
                  return i + 1;
                } 
                byte b = 0;
                while (true) {
                  int[] arrayOfInt = this.placeholderToParameterIndexMap;
                  if (b < arrayOfInt.length) {
                    if (arrayOfInt[b] == callableStatementParam.index)
                      return b + 1; 
                    b++;
                    continue;
                  } 
                  StringBuilder stringBuilder2 = new StringBuilder();
                  this();
                  stringBuilder2.append("Can't find local placeholder mapping for parameter named \"");
                  stringBuilder2.append(paramString);
                  stringBuilder2.append("\".");
                  throw SQLError.createSQLException(stringBuilder2.toString(), "S1009", getExceptionInterceptor());
                } 
              } 
              StringBuilder stringBuilder1 = new StringBuilder();
              this();
              stringBuilder1.append(Messages.getString("CallableStatement.5"));
              stringBuilder1.append(paramString);
              stringBuilder1.append(Messages.getString("CallableStatement.6"));
              throw SQLError.createSQLException(stringBuilder1.toString(), "S1009", getExceptionInterceptor());
            } 
          } 
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append(Messages.getString("CallableStatement.3"));
          stringBuilder.append(paramString);
          stringBuilder.append(Messages.getString("CallableStatement.4"));
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
        } 
        throw SQLError.createSQLException(Messages.getString("CallableStatement.2"), "S1009", getExceptionInterceptor());
      } 
      throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies", "S1009", getExceptionInterceptor());
    } 
  }
  
  public Object getObject(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      CallableStatementParam callableStatementParam = checkIsOutputParam(paramInt);
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Object object = resultSetInternalMethods.getObjectStoredProc(mapOutputParameterIndexToRsIndex(paramInt), callableStatementParam.desiredJdbcType);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return object;
    } 
  }
  
  public <T> T getObject(int paramInt, Class<T> paramClass) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      paramClass = ((ResultSetImpl)resultSetInternalMethods).getObject(mapOutputParameterIndexToRsIndex(paramInt), (Class)paramClass);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return (T)paramClass;
    } 
  }
  
  public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Object object = resultSetInternalMethods.getObject(mapOutputParameterIndexToRsIndex(paramInt), paramMap);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return object;
    } 
  }
  
  public Object getObject(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Object object = resultSetInternalMethods.getObject(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return object;
    } 
  }
  
  public <T> T getObject(String paramString, Class<T> paramClass) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      paramString = ((ResultSetImpl)resultSetInternalMethods).getObject(fixParameterName(paramString), (Class)paramClass);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return (T)paramString;
    } 
  }
  
  public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Object object = resultSetInternalMethods.getObject(fixParameterName(paramString), paramMap);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return object;
    } 
  }
  
  public ResultSetInternalMethods getOutputParameters(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.outputParamWasNull = false;
      if (paramInt == 1 && this.callingStoredFunction && this.returnValueParam != null)
        return this.functionReturnValueResults; 
      ResultSetInternalMethods resultSetInternalMethods = this.outputParameterResults;
      if (resultSetInternalMethods == null) {
        if (this.paramInfo.numberOfParameters() == 0)
          throw SQLError.createSQLException(Messages.getString("CallableStatement.7"), "S1009", getExceptionInterceptor()); 
        throw SQLError.createSQLException(Messages.getString("CallableStatement.8"), "S1000", getExceptionInterceptor());
      } 
      return resultSetInternalMethods;
    } 
  }
  
  public int getParameterIndexOffset() {
    return this.callingStoredFunction ? -1 : super.getParameterIndexOffset();
  }
  
  public ParameterMetaData getParameterMetaData() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.placeholderToParameterIndexMap == null)
        return this.paramInfo; 
      CallableStatementParamInfo callableStatementParamInfo = new CallableStatementParamInfo();
      this(this, this.paramInfo);
      return callableStatementParamInfo;
    } 
  }
  
  public Ref getRef(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Ref ref = resultSetInternalMethods.getRef(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return ref;
    } 
  }
  
  public Ref getRef(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Ref ref = resultSetInternalMethods.getRef(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return ref;
    } 
  }
  
  public short getShort(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      short s = resultSetInternalMethods.getShort(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return s;
    } 
  }
  
  public short getShort(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      short s = resultSetInternalMethods.getShort(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return s;
    } 
  }
  
  public String getString(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      String str = resultSetInternalMethods.getString(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return str;
    } 
  }
  
  public String getString(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      paramString = resultSetInternalMethods.getString(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return paramString;
    } 
  }
  
  public Time getTime(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Time time = resultSetInternalMethods.getTime(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return time;
    } 
  }
  
  public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Time time = resultSetInternalMethods.getTime(mapOutputParameterIndexToRsIndex(paramInt), paramCalendar);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return time;
    } 
  }
  
  public Time getTime(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Time time = resultSetInternalMethods.getTime(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return time;
    } 
  }
  
  public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Time time = resultSetInternalMethods.getTime(fixParameterName(paramString), paramCalendar);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return time;
    } 
  }
  
  public Timestamp getTimestamp(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Timestamp timestamp = resultSetInternalMethods.getTimestamp(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return timestamp;
    } 
  }
  
  public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      Timestamp timestamp = resultSetInternalMethods.getTimestamp(mapOutputParameterIndexToRsIndex(paramInt), paramCalendar);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return timestamp;
    } 
  }
  
  public Timestamp getTimestamp(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Timestamp timestamp = resultSetInternalMethods.getTimestamp(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return timestamp;
    } 
  }
  
  public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      Timestamp timestamp = resultSetInternalMethods.getTimestamp(fixParameterName(paramString), paramCalendar);
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return timestamp;
    } 
  }
  
  public URL getURL(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
      URL uRL = resultSetInternalMethods.getURL(mapOutputParameterIndexToRsIndex(paramInt));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return uRL;
    } 
  }
  
  public URL getURL(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
      URL uRL = resultSetInternalMethods.getURL(fixParameterName(paramString));
      this.outputParamWasNull = resultSetInternalMethods.wasNull();
      return uRL;
    } 
  }
  
  public int mapOutputParameterIndexToRsIndex(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.returnValueParam != null && paramInt == 1)
        return 1; 
      checkParameterIndexBounds(paramInt);
      int j = paramInt - 1;
      int[] arrayOfInt = this.placeholderToParameterIndexMap;
      int i = j;
      if (arrayOfInt != null)
        i = arrayOfInt[j]; 
      i = this.parameterIndexToRsIndex[i];
      if (i != Integer.MIN_VALUE)
        return i + 1; 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(Messages.getString("CallableStatement.21"));
      stringBuilder.append(paramInt);
      stringBuilder.append(Messages.getString("CallableStatement.22"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public void registerOutParameter(int paramInt1, int paramInt2) throws SQLException {
    (checkIsOutputParam(paramInt1)).desiredJdbcType = paramInt2;
  }
  
  public void registerOutParameter(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    registerOutParameter(paramInt1, paramInt2);
  }
  
  public void registerOutParameter(int paramInt1, int paramInt2, String paramString) throws SQLException {
    checkIsOutputParam(paramInt1);
  }
  
  public void registerOutParameter(String paramString, int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      registerOutParameter(getNamedParamIndex(paramString, true), paramInt);
      return;
    } 
  }
  
  public void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException {
    registerOutParameter(getNamedParamIndex(paramString, true), paramInt1);
  }
  
  public void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException {
    registerOutParameter(getNamedParamIndex(paramString1, true), paramInt, paramString2);
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
    setAsciiStream(getNamedParamIndex(paramString, false), paramInputStream);
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    setAsciiStream(getNamedParamIndex(paramString, false), paramInputStream, paramInt);
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    setAsciiStream(getNamedParamIndex(paramString, false), paramInputStream, paramLong);
  }
  
  public void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
    setBigDecimal(getNamedParamIndex(paramString, false), paramBigDecimal);
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
    setBinaryStream(getNamedParamIndex(paramString, false), paramInputStream);
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    setBinaryStream(getNamedParamIndex(paramString, false), paramInputStream, paramInt);
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    setBinaryStream(getNamedParamIndex(paramString, false), paramInputStream, paramLong);
  }
  
  public void setBlob(String paramString, InputStream paramInputStream) throws SQLException {
    setBlob(getNamedParamIndex(paramString, false), paramInputStream);
  }
  
  public void setBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    setBlob(getNamedParamIndex(paramString, false), paramInputStream, paramLong);
  }
  
  public void setBlob(String paramString, Blob paramBlob) throws SQLException {
    setBlob(getNamedParamIndex(paramString, false), paramBlob);
  }
  
  public void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
    setBoolean(getNamedParamIndex(paramString, false), paramBoolean);
  }
  
  public void setByte(String paramString, byte paramByte) throws SQLException {
    setByte(getNamedParamIndex(paramString, false), paramByte);
  }
  
  public void setBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
    setBytes(getNamedParamIndex(paramString, false), paramArrayOfbyte);
  }
  
  public void setCharacterStream(String paramString, Reader paramReader) throws SQLException {
    setCharacterStream(getNamedParamIndex(paramString, false), paramReader);
  }
  
  public void setCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
    setCharacterStream(getNamedParamIndex(paramString, false), paramReader, paramInt);
  }
  
  public void setCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
    setCharacterStream(getNamedParamIndex(paramString, false), paramReader, paramLong);
  }
  
  public void setClob(String paramString, Reader paramReader) throws SQLException {
    setClob(getNamedParamIndex(paramString, false), paramReader);
  }
  
  public void setClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
    setClob(getNamedParamIndex(paramString, false), paramReader, paramLong);
  }
  
  public void setClob(String paramString, Clob paramClob) throws SQLException {
    setClob(getNamedParamIndex(paramString, false), paramClob);
  }
  
  public void setDate(String paramString, Date paramDate) throws SQLException {
    setDate(getNamedParamIndex(paramString, false), paramDate);
  }
  
  public void setDate(String paramString, Date paramDate, Calendar paramCalendar) throws SQLException {
    setDate(getNamedParamIndex(paramString, false), paramDate, paramCalendar);
  }
  
  public void setDouble(String paramString, double paramDouble) throws SQLException {
    setDouble(getNamedParamIndex(paramString, false), paramDouble);
  }
  
  public void setFloat(String paramString, float paramFloat) throws SQLException {
    setFloat(getNamedParamIndex(paramString, false), paramFloat);
  }
  
  public void setInt(String paramString, int paramInt) throws SQLException {
    setInt(getNamedParamIndex(paramString, false), paramInt);
  }
  
  public void setLong(String paramString, long paramLong) throws SQLException {
    setLong(getNamedParamIndex(paramString, false), paramLong);
  }
  
  public void setNCharacterStream(String paramString, Reader paramReader) throws SQLException {
    setNCharacterStream(getNamedParamIndex(paramString, false), paramReader);
  }
  
  public void setNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
    setNCharacterStream(getNamedParamIndex(paramString, false), paramReader, paramLong);
  }
  
  public void setNull(String paramString, int paramInt) throws SQLException {
    setNull(getNamedParamIndex(paramString, false), paramInt);
  }
  
  public void setNull(String paramString1, int paramInt, String paramString2) throws SQLException {
    setNull(getNamedParamIndex(paramString1, false), paramInt, paramString2);
  }
  
  public void setObject(String paramString, Object paramObject) throws SQLException {
    setObject(getNamedParamIndex(paramString, false), paramObject);
  }
  
  public void setObject(String paramString, Object paramObject, int paramInt) throws SQLException {
    setObject(getNamedParamIndex(paramString, false), paramObject, paramInt);
  }
  
  public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException {}
  
  public void setShort(String paramString, short paramShort) throws SQLException {
    setShort(getNamedParamIndex(paramString, false), paramShort);
  }
  
  public void setString(String paramString1, String paramString2) throws SQLException {
    setString(getNamedParamIndex(paramString1, false), paramString2);
  }
  
  public void setTime(String paramString, Time paramTime) throws SQLException {
    setTime(getNamedParamIndex(paramString, false), paramTime);
  }
  
  public void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException {
    setTime(getNamedParamIndex(paramString, false), paramTime, paramCalendar);
  }
  
  public void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
    setTimestamp(getNamedParamIndex(paramString, false), paramTimestamp);
  }
  
  public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
    setTimestamp(getNamedParamIndex(paramString, false), paramTimestamp, paramCalendar);
  }
  
  public void setURL(String paramString, URL paramURL) throws SQLException {
    setURL(getNamedParamIndex(paramString, false), paramURL);
  }
  
  public boolean wasNull() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.outputParamWasNull;
    } 
  }
  
  public static class CallableStatementParam {
    public int desiredJdbcType;
    
    public int inOutModifier;
    
    public int index;
    
    public boolean isIn;
    
    public boolean isOut;
    
    public int jdbcType;
    
    public short nullability;
    
    public String paramName;
    
    public int precision;
    
    public int scale;
    
    public String typeName;
    
    public CallableStatementParam(String param1String1, int param1Int1, boolean param1Boolean1, boolean param1Boolean2, int param1Int2, String param1String2, int param1Int3, int param1Int4, short param1Short, int param1Int5) {
      this.paramName = param1String1;
      this.isIn = param1Boolean1;
      this.isOut = param1Boolean2;
      this.index = param1Int1;
      this.jdbcType = param1Int2;
      this.typeName = param1String2;
      this.precision = param1Int3;
      this.scale = param1Int4;
      this.nullability = param1Short;
      this.inOutModifier = param1Int5;
    }
    
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  public class CallableStatementParamInfo implements ParameterMetaData {
    public String catalogInUse;
    
    public boolean isFunctionCall;
    
    public boolean isReadOnlySafeChecked;
    
    public boolean isReadOnlySafeProcedure;
    
    public String nativeSql;
    
    public int numParameters;
    
    public List<CallableStatement.CallableStatementParam> parameterList;
    
    public Map<String, CallableStatement.CallableStatementParam> parameterMap;
    
    public final CallableStatement this$0;
    
    public CallableStatementParamInfo(CallableStatementParamInfo param1CallableStatementParamInfo) {
      byte b = 0;
      this.isReadOnlySafeProcedure = false;
      this.isReadOnlySafeChecked = false;
      this.nativeSql = CallableStatement.this.originalSql;
      this.catalogInUse = CallableStatement.this.currentCatalog;
      this.isFunctionCall = param1CallableStatementParamInfo.isFunctionCall;
      int[] arrayOfInt = CallableStatement.this.placeholderToParameterIndexMap;
      int i = arrayOfInt.length;
      this.isReadOnlySafeProcedure = param1CallableStatementParamInfo.isReadOnlySafeProcedure;
      this.isReadOnlySafeChecked = param1CallableStatementParamInfo.isReadOnlySafeChecked;
      this.parameterList = new ArrayList<CallableStatement.CallableStatementParam>(param1CallableStatementParamInfo.numParameters);
      this.parameterMap = new HashMap<String, CallableStatement.CallableStatementParam>(param1CallableStatementParamInfo.numParameters);
      if (this.isFunctionCall)
        this.parameterList.add(param1CallableStatementParamInfo.parameterList.get(0)); 
      boolean bool = this.isFunctionCall;
      while (b < i) {
        if (arrayOfInt[b] != 0) {
          CallableStatement.CallableStatementParam callableStatementParam = param1CallableStatementParamInfo.parameterList.get(arrayOfInt[b] + bool);
          this.parameterList.add(callableStatementParam);
          this.parameterMap.put(callableStatementParam.paramName, callableStatementParam);
        } 
        b++;
      } 
      this.numParameters = this.parameterList.size();
    }
    
    public CallableStatementParamInfo(ResultSet param1ResultSet) throws SQLException {
      this.isReadOnlySafeProcedure = false;
      this.isReadOnlySafeChecked = false;
      boolean bool = param1ResultSet.last();
      this.nativeSql = CallableStatement.this.originalSql;
      this.catalogInUse = CallableStatement.this.currentCatalog;
      this.isFunctionCall = CallableStatement.this.callingStoredFunction;
      if (bool) {
        this.numParameters = param1ResultSet.getRow();
        this.parameterList = new ArrayList<CallableStatement.CallableStatementParam>(this.numParameters);
        this.parameterMap = new HashMap<String, CallableStatement.CallableStatementParam>(this.numParameters);
        param1ResultSet.beforeFirst();
        addParametersFromDBMD(param1ResultSet);
      } else {
        this.numParameters = 0;
      } 
      if (this.isFunctionCall)
        this.numParameters++; 
    }
    
    private void addParametersFromDBMD(ResultSet param1ResultSet) throws SQLException {
      // Byte code:
      //   0: iconst_0
      //   1: istore_3
      //   2: aload_1
      //   3: invokeinterface next : ()Z
      //   8: ifeq -> 210
      //   11: aload_1
      //   12: iconst_4
      //   13: invokeinterface getString : (I)Ljava/lang/String;
      //   18: astore #7
      //   20: aload_1
      //   21: iconst_5
      //   22: invokeinterface getInt : (I)I
      //   27: istore_2
      //   28: iload_2
      //   29: iconst_1
      //   30: if_icmpeq -> 63
      //   33: iload_2
      //   34: iconst_2
      //   35: if_icmpeq -> 58
      //   38: iload_2
      //   39: iconst_4
      //   40: if_icmpeq -> 53
      //   43: iload_2
      //   44: iconst_5
      //   45: if_icmpeq -> 53
      //   48: iconst_0
      //   49: istore_2
      //   50: goto -> 65
      //   53: iconst_4
      //   54: istore_2
      //   55: goto -> 65
      //   58: iconst_2
      //   59: istore_2
      //   60: goto -> 65
      //   63: iconst_1
      //   64: istore_2
      //   65: iload_3
      //   66: ifne -> 82
      //   69: aload_0
      //   70: getfield isFunctionCall : Z
      //   73: ifeq -> 82
      //   76: iconst_0
      //   77: istore #4
      //   79: goto -> 90
      //   82: iload_2
      //   83: iconst_2
      //   84: if_icmpne -> 96
      //   87: iconst_1
      //   88: istore #4
      //   90: iconst_1
      //   91: istore #5
      //   93: goto -> 121
      //   96: iload_2
      //   97: iconst_1
      //   98: if_icmpne -> 107
      //   101: iconst_1
      //   102: istore #4
      //   104: goto -> 118
      //   107: iload_2
      //   108: iconst_4
      //   109: if_icmpne -> 115
      //   112: goto -> 76
      //   115: iconst_0
      //   116: istore #4
      //   118: iconst_0
      //   119: istore #5
      //   121: new com/mysql/jdbc/CallableStatement$CallableStatementParam
      //   124: dup
      //   125: aload #7
      //   127: iload_3
      //   128: iload #4
      //   130: iload #5
      //   132: aload_1
      //   133: bipush #6
      //   135: invokeinterface getInt : (I)I
      //   140: aload_1
      //   141: bipush #7
      //   143: invokeinterface getString : (I)Ljava/lang/String;
      //   148: aload_1
      //   149: bipush #8
      //   151: invokeinterface getInt : (I)I
      //   156: aload_1
      //   157: bipush #10
      //   159: invokeinterface getInt : (I)I
      //   164: aload_1
      //   165: bipush #12
      //   167: invokeinterface getShort : (I)S
      //   172: iload_2
      //   173: invokespecial <init> : (Ljava/lang/String;IZZILjava/lang/String;IISI)V
      //   176: astore #6
      //   178: aload_0
      //   179: getfield parameterList : Ljava/util/List;
      //   182: aload #6
      //   184: invokeinterface add : (Ljava/lang/Object;)Z
      //   189: pop
      //   190: aload_0
      //   191: getfield parameterMap : Ljava/util/Map;
      //   194: aload #7
      //   196: aload #6
      //   198: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   203: pop
      //   204: iinc #3, 1
      //   207: goto -> 2
      //   210: return
    }
    
    public void checkBounds(int param1Int) throws SQLException {
      if (param1Int >= 0 && param1Int - 1 < this.numParameters)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("CallableStatement.11"));
      stringBuilder.append(param1Int);
      stringBuilder.append(Messages.getString("CallableStatement.12"));
      stringBuilder.append(this.numParameters);
      stringBuilder.append(Messages.getString("CallableStatement.13"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", CallableStatement.this.getExceptionInterceptor());
    }
    
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
    
    public CallableStatement.CallableStatementParam getParameter(int param1Int) {
      return this.parameterList.get(param1Int);
    }
    
    public CallableStatement.CallableStatementParam getParameter(String param1String) {
      return this.parameterMap.get(param1String);
    }
    
    public String getParameterClassName(int param1Int) throws SQLException {
      boolean bool1;
      boolean bool2;
      boolean bool3;
      String str = getParameterTypeName(param1Int);
      if (StringUtils.indexOfIgnoreCase(str, "BLOB") != -1 || StringUtils.indexOfIgnoreCase(str, "BINARY") != -1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (StringUtils.indexOfIgnoreCase(str, "UNSIGNED") != -1) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      if (StringUtils.startsWithIgnoreCase(str, "MEDIUMINT")) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      return ResultSetMetaData.getClassNameForJavaType(getParameterType(param1Int), bool3, bool1, bool2, false, CallableStatement.this.connection.getYearIsDateType());
    }
    
    public int getParameterCount() throws SQLException {
      List<CallableStatement.CallableStatementParam> list = this.parameterList;
      return (list == null) ? 0 : list.size();
    }
    
    public int getParameterMode(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return (getParameter(param1Int - 1)).inOutModifier;
    }
    
    public int getParameterType(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return (getParameter(param1Int - 1)).jdbcType;
    }
    
    public String getParameterTypeName(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return (getParameter(param1Int - 1)).typeName;
    }
    
    public int getPrecision(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return (getParameter(param1Int - 1)).precision;
    }
    
    public int getScale(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return (getParameter(param1Int - 1)).scale;
    }
    
    public int isNullable(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return (getParameter(param1Int - 1)).nullability;
    }
    
    public boolean isSigned(int param1Int) throws SQLException {
      checkBounds(param1Int);
      return false;
    }
    
    public boolean isWrapperFor(Class<?> param1Class) throws SQLException {
      CallableStatement.this.checkClosed();
      return param1Class.isInstance(this);
    }
    
    public Iterator<CallableStatement.CallableStatementParam> iterator() {
      return this.parameterList.iterator();
    }
    
    public int numberOfParameters() {
      return this.numParameters;
    }
    
    public <T> T unwrap(Class<T> param1Class) throws SQLException {
      try {
        return param1Class.cast(this);
      } catch (ClassCastException classCastException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to unwrap to ");
        stringBuilder.append(param1Class.toString());
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", CallableStatement.this.getExceptionInterceptor());
      } 
    }
  }
}
