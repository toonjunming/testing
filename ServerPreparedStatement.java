package com.mysql.jdbc;

import com.mysql.jdbc.log.LogUtils;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.TimerTask;

public class ServerPreparedStatement extends PreparedStatement {
  public static final int BLOB_STREAM_READ_BUF_SIZE = 8192;
  
  private static final Constructor<?> JDBC_4_SPS_CTOR;
  
  private boolean canRewrite;
  
  private Calendar defaultTzCalendar;
  
  private boolean detectedLongParameterSwitch;
  
  private int fieldCount;
  
  private boolean hasCheckedRewrite;
  
  private boolean hasOnDuplicateKeyUpdate;
  
  private boolean invalid;
  
  private SQLException invalidationException;
  
  public boolean isCached;
  
  private int locationOfOnDuplicateKeyUpdate;
  
  private Buffer outByteBuffer;
  
  private BindValue[] parameterBindings;
  
  private Field[] parameterFields;
  
  private Field[] resultFields;
  
  private boolean sendTypesToServer;
  
  private boolean serverNeedsResetBeforeEachExecution;
  
  private long serverStatementId;
  
  private Calendar serverTzCalendar;
  
  private int stringTypeCode;
  
  private boolean useAutoSlowLog;
  
  static {
    if (Util.isJdbc4()) {
      try {
        String str;
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.JDBC42ServerPreparedStatement";
        } else {
          str = "com.mysql.jdbc.JDBC4ServerPreparedStatement";
        } 
        Class<?> clazz1 = Class.forName(str);
        Class<int> clazz = int.class;
        JDBC_4_SPS_CTOR = clazz1.getConstructor(new Class[] { MySQLConnection.class, String.class, String.class, clazz, clazz });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_SPS_CTOR = null;
    } 
  }
  
  public ServerPreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, int paramInt1, int paramInt2) throws SQLException {
    super(paramMySQLConnection, paramString2);
    String str;
    boolean bool;
    this.hasOnDuplicateKeyUpdate = false;
    this.detectedLongParameterSwitch = false;
    this.invalid = false;
    this.sendTypesToServer = false;
    this.stringTypeCode = 254;
    this.isCached = false;
    this.hasCheckedRewrite = false;
    this.canRewrite = false;
    this.locationOfOnDuplicateKeyUpdate = -2;
    checkNullOrEmptyQuery(paramString1);
    char c = StringUtils.firstAlphaCharUc(paramString1, StatementImpl.findStartOfStatement(paramString1));
    this.firstCharOfStmt = c;
    if (c == 'I' && containsOnDuplicateKeyInString(paramString1)) {
      bool = true;
    } else {
      bool = false;
    } 
    this.hasOnDuplicateKeyUpdate = bool;
    if (this.connection.versionMeetsMinimum(5, 0, 0)) {
      this.serverNeedsResetBeforeEachExecution = this.connection.versionMeetsMinimum(5, 0, 3) ^ true;
    } else {
      this.serverNeedsResetBeforeEachExecution = this.connection.versionMeetsMinimum(4, 1, 10) ^ true;
    } 
    this.useAutoSlowLog = this.connection.getAutoSlowLog();
    this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
    paramString2 = this.connection.getStatementComment();
    if (paramString2 == null) {
      str = paramString1;
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("/* ");
      stringBuilder.append(paramString2);
      stringBuilder.append(" */ ");
      stringBuilder.append(paramString1);
      str = stringBuilder.toString();
    } 
    this.originalSql = str;
    if (this.connection.versionMeetsMinimum(4, 1, 2)) {
      this.stringTypeCode = 253;
    } else {
      this.stringTypeCode = 254;
    } 
    try {
      serverPrepare(paramString1);
      setResultSetType(paramInt1);
      setResultSetConcurrency(paramInt2);
      this.parameterTypes = new int[this.parameterCount];
      return;
    } catch (SQLException sQLException) {
      realClose(false, true);
      throw sQLException;
    } catch (Exception exception) {
      realClose(false, true);
      SQLException sQLException = SQLError.createSQLException(exception.toString(), "S1000", getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void clearParametersInternal(boolean paramBoolean) throws SQLException {
    boolean bool;
    if (this.parameterBindings != null) {
      byte b = 0;
      boolean bool1 = false;
      while (true) {
        bool = bool1;
        if (b < this.parameterCount) {
          BindValue[] arrayOfBindValue = this.parameterBindings;
          bool = bool1;
          if (arrayOfBindValue[b] != null) {
            bool = bool1;
            if ((arrayOfBindValue[b]).isLongData)
              bool = true; 
          } 
          arrayOfBindValue[b].reset();
          b++;
          bool1 = bool;
          continue;
        } 
        break;
      } 
    } else {
      bool = false;
    } 
    if (paramBoolean && bool) {
      serverResetStatement();
      this.detectedLongParameterSwitch = false;
    } 
  }
  
  private void dumpCloseForTestcase() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this.connection.generateConnectionCommentBlock(stringBuilder);
      stringBuilder.append("DEALLOCATE PREPARE debug_stmt_");
      stringBuilder.append(this.statementId);
      stringBuilder.append(";\n");
      this.connection.dumpTestcaseQuery(stringBuilder.toString());
      return;
    } 
  }
  
  private void dumpExecuteForTestcase() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      boolean bool = false;
      byte b;
      for (b = 0; b < this.parameterCount; b++) {
        this.connection.generateConnectionCommentBlock(stringBuilder);
        stringBuilder.append("SET @debug_stmt_param");
        stringBuilder.append(this.statementId);
        stringBuilder.append("_");
        stringBuilder.append(b);
        stringBuilder.append("=");
        BindValue[] arrayOfBindValue = this.parameterBindings;
        if ((arrayOfBindValue[b]).isNull) {
          stringBuilder.append("NULL");
        } else {
          stringBuilder.append(arrayOfBindValue[b].toString(true));
        } 
        stringBuilder.append(";\n");
      } 
      this.connection.generateConnectionCommentBlock(stringBuilder);
      stringBuilder.append("EXECUTE debug_stmt_");
      stringBuilder.append(this.statementId);
      if (this.parameterCount > 0) {
        stringBuilder.append(" USING ");
        for (b = bool; b < this.parameterCount; b++) {
          if (b > 0)
            stringBuilder.append(", "); 
          stringBuilder.append("@debug_stmt_param");
          stringBuilder.append(this.statementId);
          stringBuilder.append("_");
          stringBuilder.append(b);
        } 
      } 
      stringBuilder.append(";\n");
      this.connection.dumpTestcaseQuery(stringBuilder.toString());
      return;
    } 
  }
  
  private void dumpPrepareForTestcase() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      StringBuilder stringBuilder = new StringBuilder();
      this(this.originalSql.length() + 64);
      this.connection.generateConnectionCommentBlock(stringBuilder);
      stringBuilder.append("PREPARE debug_stmt_");
      stringBuilder.append(this.statementId);
      stringBuilder.append(" FROM \"");
      stringBuilder.append(this.originalSql);
      stringBuilder.append("\";\n");
      this.connection.dumpTestcaseQuery(stringBuilder.toString());
      return;
    } 
  }
  
  private Calendar getDefaultTzCalendar() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.defaultTzCalendar == null) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        this(TimeZone.getDefault());
        this.defaultTzCalendar = gregorianCalendar;
      } 
      return this.defaultTzCalendar;
    } 
  }
  
  public static ServerPreparedStatement getInstance(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, int paramInt1, int paramInt2) throws SQLException {
    if (!Util.isJdbc4())
      return new ServerPreparedStatement(paramMySQLConnection, paramString1, paramString2, paramInt1, paramInt2); 
    try {
      return (ServerPreparedStatement)JDBC_4_SPS_CTOR.newInstance(new Object[] { paramMySQLConnection, paramString1, paramString2, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new SQLException(illegalArgumentException.toString(), "S1000");
    } catch (InstantiationException instantiationException) {
      throw new SQLException(instantiationException.toString(), "S1000");
    } catch (IllegalAccessException illegalAccessException) {
      throw new SQLException(illegalAccessException.toString(), "S1000");
    } catch (InvocationTargetException invocationTargetException) {
      Throwable throwable = invocationTargetException.getTargetException();
      if (throwable instanceof SQLException)
        throw (SQLException)throwable; 
      throw new SQLException(throwable.toString(), "S1000");
    } 
  }
  
  private Calendar getServerTzCalendar() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.serverTzCalendar == null) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        this(this.connection.getServerTimezoneTZ());
        this.serverTzCalendar = gregorianCalendar;
      } 
      return this.serverTzCalendar;
    } 
  }
  
  private ResultSetInternalMethods serverExecute(int paramInt, boolean paramBoolean, Field[] paramArrayOfField) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #28
    //   11: aload #28
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   18: invokeinterface getIO : ()Lcom/mysql/jdbc/MysqlIO;
    //   23: astore #29
    //   25: aload #29
    //   27: invokevirtual shouldIntercept : ()Z
    //   30: ifeq -> 57
    //   33: aload #29
    //   35: aload_0
    //   36: getfield originalSql : Ljava/lang/String;
    //   39: aload_0
    //   40: iconst_1
    //   41: invokevirtual invokeStatementInterceptorsPre : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   44: astore #21
    //   46: aload #21
    //   48: ifnull -> 57
    //   51: aload #28
    //   53: monitorexit
    //   54: aload #21
    //   56: areturn
    //   57: aload_0
    //   58: getfield detectedLongParameterSwitch : Z
    //   61: ifeq -> 207
    //   64: iconst_0
    //   65: istore #7
    //   67: iconst_0
    //   68: istore #6
    //   70: lconst_0
    //   71: lstore #10
    //   73: iload #7
    //   75: aload_0
    //   76: getfield parameterCount : I
    //   79: iconst_1
    //   80: isub
    //   81: if_icmpge -> 203
    //   84: aload_0
    //   85: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   88: astore #21
    //   90: iload #6
    //   92: istore #5
    //   94: lload #10
    //   96: lstore #8
    //   98: aload #21
    //   100: iload #7
    //   102: aaload
    //   103: getfield isLongData : Z
    //   106: ifeq -> 189
    //   109: iload #6
    //   111: ifeq -> 176
    //   114: lload #10
    //   116: aload #21
    //   118: iload #7
    //   120: aaload
    //   121: getfield boundBeforeExecutionNum : J
    //   124: lcmp
    //   125: ifne -> 131
    //   128: goto -> 176
    //   131: new java/lang/StringBuilder
    //   134: astore_3
    //   135: aload_3
    //   136: invokespecial <init> : ()V
    //   139: aload_3
    //   140: ldc_w 'ServerPreparedStatement.11'
    //   143: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_3
    //   151: ldc_w 'ServerPreparedStatement.12'
    //   154: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: aload_3
    //   162: invokevirtual toString : ()Ljava/lang/String;
    //   165: ldc_w 'S1C00'
    //   168: aload_0
    //   169: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   172: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   175: athrow
    //   176: aload #21
    //   178: iload #7
    //   180: aaload
    //   181: getfield boundBeforeExecutionNum : J
    //   184: lstore #8
    //   186: iconst_1
    //   187: istore #5
    //   189: iinc #7, 1
    //   192: iload #5
    //   194: istore #6
    //   196: lload #8
    //   198: lstore #10
    //   200: goto -> 73
    //   203: aload_0
    //   204: invokespecial serverResetStatement : ()V
    //   207: iconst_0
    //   208: istore #5
    //   210: iload #5
    //   212: aload_0
    //   213: getfield parameterCount : I
    //   216: if_icmpge -> 292
    //   219: aload_0
    //   220: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   223: iload #5
    //   225: aaload
    //   226: getfield isSet : Z
    //   229: ifeq -> 238
    //   232: iinc #5, 1
    //   235: goto -> 210
    //   238: new java/lang/StringBuilder
    //   241: astore_3
    //   242: aload_3
    //   243: invokespecial <init> : ()V
    //   246: aload_3
    //   247: ldc_w 'ServerPreparedStatement.13'
    //   250: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   253: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   256: pop
    //   257: aload_3
    //   258: iload #5
    //   260: iconst_1
    //   261: iadd
    //   262: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   265: pop
    //   266: aload_3
    //   267: ldc_w 'ServerPreparedStatement.14'
    //   270: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: pop
    //   277: aload_3
    //   278: invokevirtual toString : ()Ljava/lang/String;
    //   281: ldc_w 'S1009'
    //   284: aload_0
    //   285: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   288: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   291: athrow
    //   292: iconst_0
    //   293: istore #5
    //   295: iload #5
    //   297: aload_0
    //   298: getfield parameterCount : I
    //   301: if_icmpge -> 338
    //   304: aload_0
    //   305: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   308: astore #21
    //   310: aload #21
    //   312: iload #5
    //   314: aaload
    //   315: getfield isLongData : Z
    //   318: ifeq -> 332
    //   321: aload_0
    //   322: iload #5
    //   324: aload #21
    //   326: iload #5
    //   328: aaload
    //   329: invokespecial serverLongData : (ILcom/mysql/jdbc/ServerPreparedStatement$BindValue;)V
    //   332: iinc #5, 1
    //   335: goto -> 295
    //   338: aload_0
    //   339: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   342: invokeinterface getAutoGenerateTestcaseScript : ()Z
    //   347: ifeq -> 354
    //   350: aload_0
    //   351: invokespecial dumpExecuteForTestcase : ()V
    //   354: aload #29
    //   356: invokevirtual getSharedSendPacket : ()Lcom/mysql/jdbc/Buffer;
    //   359: astore #30
    //   361: aload #30
    //   363: invokevirtual clear : ()V
    //   366: aload #30
    //   368: bipush #23
    //   370: invokevirtual writeByte : (B)V
    //   373: aload #30
    //   375: aload_0
    //   376: getfield serverStatementId : J
    //   379: invokevirtual writeLong : (J)V
    //   382: aload_0
    //   383: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   386: iconst_4
    //   387: iconst_1
    //   388: iconst_2
    //   389: invokeinterface versionMeetsMinimum : (III)Z
    //   394: ifeq -> 425
    //   397: aload_0
    //   398: invokevirtual isCursorRequired : ()Z
    //   401: ifeq -> 413
    //   404: aload #30
    //   406: iconst_1
    //   407: invokevirtual writeByte : (B)V
    //   410: goto -> 419
    //   413: aload #30
    //   415: iconst_0
    //   416: invokevirtual writeByte : (B)V
    //   419: aload #30
    //   421: lconst_1
    //   422: invokevirtual writeLong : (J)V
    //   425: aload_0
    //   426: getfield parameterCount : I
    //   429: bipush #7
    //   431: iadd
    //   432: bipush #8
    //   434: idiv
    //   435: istore #7
    //   437: aload #30
    //   439: invokevirtual getPosition : ()I
    //   442: istore #6
    //   444: iconst_0
    //   445: istore #5
    //   447: iload #5
    //   449: iload #7
    //   451: if_icmpge -> 466
    //   454: aload #30
    //   456: iconst_0
    //   457: invokevirtual writeByte : (B)V
    //   460: iinc #5, 1
    //   463: goto -> 447
    //   466: iload #7
    //   468: newarray byte
    //   470: astore #21
    //   472: aload_0
    //   473: getfield sendTypesToServer : Z
    //   476: ifeq -> 485
    //   479: iconst_1
    //   480: istore #4
    //   482: goto -> 488
    //   485: iconst_0
    //   486: istore #4
    //   488: aload #30
    //   490: iload #4
    //   492: invokevirtual writeByte : (B)V
    //   495: aload_0
    //   496: getfield sendTypesToServer : Z
    //   499: ifeq -> 535
    //   502: iconst_0
    //   503: istore #5
    //   505: iload #5
    //   507: aload_0
    //   508: getfield parameterCount : I
    //   511: if_icmpge -> 535
    //   514: aload #30
    //   516: aload_0
    //   517: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   520: iload #5
    //   522: aaload
    //   523: getfield bufferType : I
    //   526: invokevirtual writeInt : (I)V
    //   529: iinc #5, 1
    //   532: goto -> 505
    //   535: iconst_0
    //   536: istore #5
    //   538: iload #5
    //   540: aload_0
    //   541: getfield parameterCount : I
    //   544: if_icmpge -> 623
    //   547: aload_0
    //   548: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   551: astore #22
    //   553: aload #22
    //   555: iload #5
    //   557: aaload
    //   558: getfield isLongData : Z
    //   561: ifne -> 617
    //   564: aload #22
    //   566: iload #5
    //   568: aaload
    //   569: getfield isNull : Z
    //   572: ifne -> 591
    //   575: aload_0
    //   576: aload #30
    //   578: aload #22
    //   580: iload #5
    //   582: aaload
    //   583: aload #29
    //   585: invokespecial storeBinding : (Lcom/mysql/jdbc/Buffer;Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;Lcom/mysql/jdbc/MysqlIO;)V
    //   588: goto -> 617
    //   591: iload #5
    //   593: bipush #8
    //   595: idiv
    //   596: istore #7
    //   598: aload #21
    //   600: iload #7
    //   602: aload #21
    //   604: iload #7
    //   606: baload
    //   607: iconst_1
    //   608: iload #5
    //   610: bipush #7
    //   612: iand
    //   613: ishl
    //   614: ior
    //   615: i2b
    //   616: bastore
    //   617: iinc #5, 1
    //   620: goto -> 538
    //   623: aload #30
    //   625: invokevirtual getPosition : ()I
    //   628: istore #5
    //   630: aload #30
    //   632: iload #6
    //   634: invokevirtual setPosition : (I)V
    //   637: aload #30
    //   639: aload #21
    //   641: invokevirtual writeBytesNoNull : ([B)V
    //   644: aload #30
    //   646: iload #5
    //   648: invokevirtual setPosition : (I)V
    //   651: aload_0
    //   652: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   655: invokeinterface getLogSlowQueries : ()Z
    //   660: istore #18
    //   662: aload_0
    //   663: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   666: invokeinterface getGatherPerformanceMetrics : ()Z
    //   671: istore #19
    //   673: aload_0
    //   674: getfield profileSQL : Z
    //   677: ifne -> 699
    //   680: iload #18
    //   682: ifne -> 699
    //   685: iload #19
    //   687: ifeq -> 693
    //   690: goto -> 699
    //   693: lconst_0
    //   694: lstore #8
    //   696: goto -> 706
    //   699: aload #29
    //   701: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   704: lstore #8
    //   706: aload_0
    //   707: invokevirtual resetCancelledState : ()V
    //   710: aconst_null
    //   711: astore #26
    //   713: aconst_null
    //   714: astore #27
    //   716: aconst_null
    //   717: astore #25
    //   719: ldc_w ''
    //   722: astore #24
    //   724: aload #26
    //   726: astore #23
    //   728: aload #27
    //   730: astore #22
    //   732: aload_0
    //   733: getfield profileSQL : Z
    //   736: ifne -> 749
    //   739: iload #18
    //   741: ifne -> 749
    //   744: iload #19
    //   746: ifeq -> 764
    //   749: aload #26
    //   751: astore #23
    //   753: aload #27
    //   755: astore #22
    //   757: aload_0
    //   758: iconst_1
    //   759: invokevirtual asSql : (Z)Ljava/lang/String;
    //   762: astore #24
    //   764: aload #26
    //   766: astore #23
    //   768: aload #27
    //   770: astore #22
    //   772: aload_0
    //   773: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   776: invokeinterface getEnableQueryTimeouts : ()Z
    //   781: ifeq -> 880
    //   784: aload #26
    //   786: astore #23
    //   788: aload #27
    //   790: astore #22
    //   792: aload_0
    //   793: getfield timeoutInMillis : I
    //   796: ifeq -> 880
    //   799: aload #26
    //   801: astore #23
    //   803: aload #27
    //   805: astore #22
    //   807: aload_0
    //   808: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   811: iconst_5
    //   812: iconst_0
    //   813: iconst_0
    //   814: invokeinterface versionMeetsMinimum : (III)Z
    //   819: ifeq -> 880
    //   822: aload #26
    //   824: astore #23
    //   826: aload #27
    //   828: astore #22
    //   830: new com/mysql/jdbc/StatementImpl$CancelTask
    //   833: astore #21
    //   835: aload #26
    //   837: astore #23
    //   839: aload #27
    //   841: astore #22
    //   843: aload #21
    //   845: aload_0
    //   846: aload_0
    //   847: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   850: aload_0
    //   851: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   854: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   859: aload #21
    //   861: aload_0
    //   862: getfield timeoutInMillis : I
    //   865: i2l
    //   866: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   869: goto -> 883
    //   872: astore_3
    //   873: goto -> 2343
    //   876: astore_3
    //   877: goto -> 2351
    //   880: aconst_null
    //   881: astore #21
    //   883: aload_0
    //   884: invokevirtual statementBegins : ()V
    //   887: aload #29
    //   889: bipush #23
    //   891: aconst_null
    //   892: aload #30
    //   894: iconst_0
    //   895: aconst_null
    //   896: iconst_0
    //   897: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   900: astore #26
    //   902: iload #18
    //   904: ifne -> 940
    //   907: iload #19
    //   909: ifne -> 940
    //   912: aload_0
    //   913: getfield profileSQL : Z
    //   916: istore #20
    //   918: iload #20
    //   920: ifeq -> 926
    //   923: goto -> 940
    //   926: lconst_0
    //   927: lstore #10
    //   929: goto -> 947
    //   932: astore_3
    //   933: goto -> 2343
    //   936: astore_3
    //   937: goto -> 2351
    //   940: aload #29
    //   942: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   945: lstore #10
    //   947: aload #21
    //   949: ifnull -> 993
    //   952: aload #21
    //   954: invokevirtual cancel : ()Z
    //   957: pop
    //   958: aload_0
    //   959: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   962: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   967: invokevirtual purge : ()I
    //   970: pop
    //   971: aload #21
    //   973: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   976: astore #22
    //   978: aload #22
    //   980: ifnonnull -> 990
    //   983: aload #25
    //   985: astore #21
    //   987: goto -> 993
    //   990: aload #22
    //   992: athrow
    //   993: aload #21
    //   995: astore #23
    //   997: aload #21
    //   999: astore #22
    //   1001: aload_0
    //   1002: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   1005: astore #25
    //   1007: aload #21
    //   1009: astore #23
    //   1011: aload #21
    //   1013: astore #22
    //   1015: aload #25
    //   1017: monitorenter
    //   1018: aload_0
    //   1019: getfield wasCancelled : Z
    //   1022: ifeq -> 1057
    //   1025: aload_0
    //   1026: getfield wasCancelledByTimeout : Z
    //   1029: ifeq -> 1043
    //   1032: new com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   1035: astore_3
    //   1036: aload_3
    //   1037: invokespecial <init> : ()V
    //   1040: goto -> 1051
    //   1043: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   1046: dup
    //   1047: invokespecial <init> : ()V
    //   1050: astore_3
    //   1051: aload_0
    //   1052: invokevirtual resetCancelledState : ()V
    //   1055: aload_3
    //   1056: athrow
    //   1057: aload #25
    //   1059: monitorexit
    //   1060: iload #18
    //   1062: ifne -> 1079
    //   1065: iload #19
    //   1067: ifeq -> 1073
    //   1070: goto -> 1079
    //   1073: iconst_0
    //   1074: istore #18
    //   1076: goto -> 1597
    //   1079: lload #10
    //   1081: lload #8
    //   1083: lsub
    //   1084: lstore #16
    //   1086: iload #18
    //   1088: ifeq -> 1179
    //   1091: aload #21
    //   1093: astore #23
    //   1095: aload #21
    //   1097: astore #22
    //   1099: aload_0
    //   1100: getfield useAutoSlowLog : Z
    //   1103: ifeq -> 1136
    //   1106: aload #21
    //   1108: astore #23
    //   1110: aload #21
    //   1112: astore #22
    //   1114: lload #16
    //   1116: aload_0
    //   1117: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1120: invokeinterface getSlowQueryThresholdMillis : ()I
    //   1125: i2l
    //   1126: lcmp
    //   1127: ifle -> 1179
    //   1130: iconst_1
    //   1131: istore #18
    //   1133: goto -> 1182
    //   1136: aload #21
    //   1138: astore #23
    //   1140: aload #21
    //   1142: astore #22
    //   1144: aload_0
    //   1145: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1148: lload #16
    //   1150: invokeinterface isAbonormallyLongQuery : (J)Z
    //   1155: istore #18
    //   1157: aload #21
    //   1159: astore #23
    //   1161: aload #21
    //   1163: astore #22
    //   1165: aload_0
    //   1166: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1169: lload #16
    //   1171: invokeinterface reportQueryTime : (J)V
    //   1176: goto -> 1182
    //   1179: iconst_0
    //   1180: istore #18
    //   1182: iload #18
    //   1184: ifeq -> 1573
    //   1187: aload #21
    //   1189: astore #23
    //   1191: aload #21
    //   1193: astore #22
    //   1195: new java/lang/StringBuilder
    //   1198: astore #32
    //   1200: aload #21
    //   1202: astore #23
    //   1204: aload #21
    //   1206: astore #22
    //   1208: aload #32
    //   1210: aload_0
    //   1211: getfield originalSql : Ljava/lang/String;
    //   1214: invokevirtual length : ()I
    //   1217: bipush #48
    //   1219: iadd
    //   1220: invokespecial <init> : (I)V
    //   1223: aload #21
    //   1225: astore #23
    //   1227: aload #21
    //   1229: astore #22
    //   1231: aload #32
    //   1233: ldc_w 'ServerPreparedStatement.15'
    //   1236: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1242: pop
    //   1243: aload #21
    //   1245: astore #23
    //   1247: aload #21
    //   1249: astore #22
    //   1251: aload #32
    //   1253: aload #29
    //   1255: invokevirtual getSlowQueryThreshold : ()J
    //   1258: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1261: pop
    //   1262: aload #21
    //   1264: astore #23
    //   1266: aload #21
    //   1268: astore #22
    //   1270: aload #32
    //   1272: ldc_w 'ServerPreparedStatement.15a'
    //   1275: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1281: pop
    //   1282: aload #21
    //   1284: astore #23
    //   1286: aload #21
    //   1288: astore #22
    //   1290: aload #32
    //   1292: lload #16
    //   1294: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1297: pop
    //   1298: aload #21
    //   1300: astore #23
    //   1302: aload #21
    //   1304: astore #22
    //   1306: aload #32
    //   1308: ldc_w 'ServerPreparedStatement.16'
    //   1311: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1314: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1317: pop
    //   1318: aload #21
    //   1320: astore #23
    //   1322: aload #21
    //   1324: astore #22
    //   1326: aload #32
    //   1328: ldc_w 'as prepared: '
    //   1331: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1334: pop
    //   1335: aload #21
    //   1337: astore #23
    //   1339: aload #21
    //   1341: astore #22
    //   1343: aload #32
    //   1345: aload_0
    //   1346: getfield originalSql : Ljava/lang/String;
    //   1349: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1352: pop
    //   1353: aload #21
    //   1355: astore #23
    //   1357: aload #21
    //   1359: astore #22
    //   1361: aload #32
    //   1363: ldc_w '\\n\\n with parameters bound:\\n\\n'
    //   1366: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1369: pop
    //   1370: aload #21
    //   1372: astore #23
    //   1374: aload #21
    //   1376: astore #22
    //   1378: aload #32
    //   1380: aload #24
    //   1382: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1385: pop
    //   1386: aload #21
    //   1388: astore #23
    //   1390: aload #21
    //   1392: astore #22
    //   1394: aload_0
    //   1395: getfield eventSink : Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1398: astore #25
    //   1400: aload #21
    //   1402: astore #23
    //   1404: aload #21
    //   1406: astore #22
    //   1408: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1411: astore #30
    //   1413: aload #21
    //   1415: astore #23
    //   1417: aload #21
    //   1419: astore #22
    //   1421: aload_0
    //   1422: getfield currentCatalog : Ljava/lang/String;
    //   1425: astore #33
    //   1427: aload #21
    //   1429: astore #23
    //   1431: aload #21
    //   1433: astore #22
    //   1435: aload_0
    //   1436: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1439: invokeinterface getId : ()J
    //   1444: lstore #12
    //   1446: aload #21
    //   1448: astore #23
    //   1450: aload #21
    //   1452: astore #22
    //   1454: aload_0
    //   1455: invokevirtual getId : ()I
    //   1458: istore #5
    //   1460: aload #21
    //   1462: astore #23
    //   1464: aload #21
    //   1466: astore #22
    //   1468: invokestatic currentTimeMillis : ()J
    //   1471: lstore #14
    //   1473: aload #21
    //   1475: astore #23
    //   1477: aload #21
    //   1479: astore #22
    //   1481: aload #29
    //   1483: invokevirtual getQueryTimingUnits : ()Ljava/lang/String;
    //   1486: astore #31
    //   1488: aload #21
    //   1490: astore #23
    //   1492: aload #21
    //   1494: astore #22
    //   1496: new java/lang/Throwable
    //   1499: astore #27
    //   1501: aload #21
    //   1503: astore #23
    //   1505: aload #21
    //   1507: astore #22
    //   1509: aload #27
    //   1511: invokespecial <init> : ()V
    //   1514: aload #21
    //   1516: astore #23
    //   1518: aload #21
    //   1520: astore #22
    //   1522: aload #30
    //   1524: bipush #6
    //   1526: ldc_w ''
    //   1529: aload #33
    //   1531: lload #12
    //   1533: iload #5
    //   1535: iconst_0
    //   1536: lload #14
    //   1538: lload #16
    //   1540: aload #31
    //   1542: aconst_null
    //   1543: aload #27
    //   1545: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1548: aload #32
    //   1550: invokevirtual toString : ()Ljava/lang/String;
    //   1553: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1556: aload #21
    //   1558: astore #23
    //   1560: aload #21
    //   1562: astore #22
    //   1564: aload #25
    //   1566: aload #30
    //   1568: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1573: iload #19
    //   1575: ifeq -> 1597
    //   1578: aload #21
    //   1580: astore #23
    //   1582: aload #21
    //   1584: astore #22
    //   1586: aload_0
    //   1587: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1590: lload #16
    //   1592: invokeinterface registerQueryExecutionTime : (J)V
    //   1597: aload #21
    //   1599: astore #23
    //   1601: aload #21
    //   1603: astore #22
    //   1605: aload_0
    //   1606: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1609: invokeinterface incrementNumberOfPreparedExecutes : ()V
    //   1614: aload #21
    //   1616: astore #23
    //   1618: aload #21
    //   1620: astore #22
    //   1622: aload_0
    //   1623: getfield profileSQL : Z
    //   1626: ifeq -> 1846
    //   1629: aload #21
    //   1631: astore #23
    //   1633: aload #21
    //   1635: astore #22
    //   1637: aload_0
    //   1638: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1641: invokestatic getInstance : (Lcom/mysql/jdbc/MySQLConnection;)Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1644: astore #31
    //   1646: aload #21
    //   1648: astore #23
    //   1650: aload #21
    //   1652: astore #22
    //   1654: aload_0
    //   1655: aload #31
    //   1657: putfield eventSink : Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1660: aload #21
    //   1662: astore #23
    //   1664: aload #21
    //   1666: astore #22
    //   1668: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1671: astore #32
    //   1673: aload #21
    //   1675: astore #23
    //   1677: aload #21
    //   1679: astore #22
    //   1681: aload_0
    //   1682: getfield currentCatalog : Ljava/lang/String;
    //   1685: astore #27
    //   1687: aload #21
    //   1689: astore #23
    //   1691: aload #21
    //   1693: astore #22
    //   1695: aload_0
    //   1696: getfield connectionId : J
    //   1699: lstore #14
    //   1701: aload #21
    //   1703: astore #23
    //   1705: aload #21
    //   1707: astore #22
    //   1709: aload_0
    //   1710: getfield statementId : I
    //   1713: istore #5
    //   1715: aload #21
    //   1717: astore #23
    //   1719: aload #21
    //   1721: astore #22
    //   1723: invokestatic currentTimeMillis : ()J
    //   1726: lstore #12
    //   1728: aload #21
    //   1730: astore #23
    //   1732: aload #21
    //   1734: astore #22
    //   1736: aload #29
    //   1738: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   1741: lstore #16
    //   1743: aload #21
    //   1745: astore #23
    //   1747: aload #21
    //   1749: astore #22
    //   1751: aload #29
    //   1753: invokevirtual getQueryTimingUnits : ()Ljava/lang/String;
    //   1756: astore #30
    //   1758: aload #21
    //   1760: astore #23
    //   1762: aload #21
    //   1764: astore #22
    //   1766: new java/lang/Throwable
    //   1769: astore #25
    //   1771: aload #21
    //   1773: astore #23
    //   1775: aload #21
    //   1777: astore #22
    //   1779: aload #25
    //   1781: invokespecial <init> : ()V
    //   1784: aload #21
    //   1786: astore #23
    //   1788: aload #21
    //   1790: astore #22
    //   1792: aload #32
    //   1794: iconst_4
    //   1795: ldc_w ''
    //   1798: aload #27
    //   1800: lload #14
    //   1802: iload #5
    //   1804: iconst_m1
    //   1805: lload #12
    //   1807: lload #16
    //   1809: lload #8
    //   1811: lsub
    //   1812: aload #30
    //   1814: aconst_null
    //   1815: aload #25
    //   1817: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1820: aload_0
    //   1821: aload #24
    //   1823: invokespecial truncateQueryToLog : (Ljava/lang/String;)Ljava/lang/String;
    //   1826: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1829: aload #21
    //   1831: astore #23
    //   1833: aload #21
    //   1835: astore #22
    //   1837: aload #31
    //   1839: aload #32
    //   1841: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1846: aload #21
    //   1848: astore #23
    //   1850: aload #21
    //   1852: astore #22
    //   1854: aload #29
    //   1856: aload_0
    //   1857: iload_1
    //   1858: aload_0
    //   1859: getfield resultSetType : I
    //   1862: aload_0
    //   1863: getfield resultSetConcurrency : I
    //   1866: iload_2
    //   1867: aload_0
    //   1868: getfield currentCatalog : Ljava/lang/String;
    //   1871: aload #26
    //   1873: iconst_1
    //   1874: aload_0
    //   1875: getfield fieldCount : I
    //   1878: i2l
    //   1879: aload_3
    //   1880: invokevirtual readAllResults : (Lcom/mysql/jdbc/StatementImpl;IIIZLjava/lang/String;Lcom/mysql/jdbc/Buffer;ZJ[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetImpl;
    //   1883: astore #26
    //   1885: aload #26
    //   1887: astore #25
    //   1889: aload #21
    //   1891: astore #23
    //   1893: aload #21
    //   1895: astore #22
    //   1897: aload #29
    //   1899: invokevirtual shouldIntercept : ()Z
    //   1902: ifeq -> 1939
    //   1905: aload #21
    //   1907: astore #23
    //   1909: aload #21
    //   1911: astore #22
    //   1913: aload #29
    //   1915: aload_0
    //   1916: getfield originalSql : Ljava/lang/String;
    //   1919: aload_0
    //   1920: aload #26
    //   1922: iconst_1
    //   1923: aconst_null
    //   1924: invokevirtual invokeStatementInterceptorsPost : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Lcom/mysql/jdbc/ResultSetInternalMethods;ZLjava/sql/SQLException;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1927: astore_3
    //   1928: aload #26
    //   1930: astore #25
    //   1932: aload_3
    //   1933: ifnull -> 1939
    //   1936: aload_3
    //   1937: astore #25
    //   1939: aload #21
    //   1941: astore #23
    //   1943: aload #21
    //   1945: astore #22
    //   1947: aload_0
    //   1948: getfield profileSQL : Z
    //   1951: ifeq -> 2149
    //   1954: aload #21
    //   1956: astore #23
    //   1958: aload #21
    //   1960: astore #22
    //   1962: aload #29
    //   1964: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   1967: lstore #8
    //   1969: aload #21
    //   1971: astore #23
    //   1973: aload #21
    //   1975: astore #22
    //   1977: aload_0
    //   1978: getfield eventSink : Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1981: astore #26
    //   1983: aload #21
    //   1985: astore #23
    //   1987: aload #21
    //   1989: astore #22
    //   1991: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1994: astore #27
    //   1996: aload #21
    //   1998: astore #23
    //   2000: aload #21
    //   2002: astore #22
    //   2004: aload_0
    //   2005: getfield currentCatalog : Ljava/lang/String;
    //   2008: astore #30
    //   2010: aload #21
    //   2012: astore #23
    //   2014: aload #21
    //   2016: astore #22
    //   2018: aload_0
    //   2019: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   2022: invokeinterface getId : ()J
    //   2027: lstore #12
    //   2029: aload #21
    //   2031: astore #23
    //   2033: aload #21
    //   2035: astore #22
    //   2037: aload_0
    //   2038: invokevirtual getId : ()I
    //   2041: istore_1
    //   2042: aload #21
    //   2044: astore #23
    //   2046: aload #21
    //   2048: astore #22
    //   2050: invokestatic currentTimeMillis : ()J
    //   2053: lstore #14
    //   2055: aload #21
    //   2057: astore #23
    //   2059: aload #21
    //   2061: astore #22
    //   2063: aload #29
    //   2065: invokevirtual getQueryTimingUnits : ()Ljava/lang/String;
    //   2068: astore #31
    //   2070: aload #21
    //   2072: astore #23
    //   2074: aload #21
    //   2076: astore #22
    //   2078: new java/lang/Throwable
    //   2081: astore_3
    //   2082: aload #21
    //   2084: astore #23
    //   2086: aload #21
    //   2088: astore #22
    //   2090: aload_3
    //   2091: invokespecial <init> : ()V
    //   2094: aload #21
    //   2096: astore #23
    //   2098: aload #21
    //   2100: astore #22
    //   2102: aload #27
    //   2104: iconst_5
    //   2105: ldc_w ''
    //   2108: aload #30
    //   2110: lload #12
    //   2112: iload_1
    //   2113: iconst_0
    //   2114: lload #14
    //   2116: lload #8
    //   2118: lload #10
    //   2120: lsub
    //   2121: aload #31
    //   2123: aconst_null
    //   2124: aload_3
    //   2125: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   2128: aconst_null
    //   2129: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   2132: aload #21
    //   2134: astore #23
    //   2136: aload #21
    //   2138: astore #22
    //   2140: aload #26
    //   2142: aload #27
    //   2144: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   2149: iload #18
    //   2151: ifeq -> 2194
    //   2154: aload #21
    //   2156: astore #23
    //   2158: aload #21
    //   2160: astore #22
    //   2162: aload_0
    //   2163: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   2166: invokeinterface getExplainSlowQueries : ()Z
    //   2171: ifeq -> 2194
    //   2174: aload #21
    //   2176: astore #23
    //   2178: aload #21
    //   2180: astore #22
    //   2182: aload #29
    //   2184: aload #24
    //   2186: invokestatic getBytes : (Ljava/lang/String;)[B
    //   2189: aload #24
    //   2191: invokevirtual explainSlowQuery : ([BLjava/lang/String;)V
    //   2194: iload_2
    //   2195: ifne -> 2225
    //   2198: aload #21
    //   2200: astore #23
    //   2202: aload #21
    //   2204: astore #22
    //   2206: aload_0
    //   2207: getfield serverNeedsResetBeforeEachExecution : Z
    //   2210: ifeq -> 2225
    //   2213: aload #21
    //   2215: astore #23
    //   2217: aload #21
    //   2219: astore #22
    //   2221: aload_0
    //   2222: invokespecial serverResetStatement : ()V
    //   2225: aload #21
    //   2227: astore_3
    //   2228: aload_0
    //   2229: iconst_0
    //   2230: putfield sendTypesToServer : Z
    //   2233: aload #21
    //   2235: astore_3
    //   2236: aload_0
    //   2237: aload #25
    //   2239: putfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   2242: aload #21
    //   2244: astore_3
    //   2245: aload #29
    //   2247: invokevirtual hadWarnings : ()Z
    //   2250: ifeq -> 2261
    //   2253: aload #21
    //   2255: astore_3
    //   2256: aload #29
    //   2258: invokevirtual scanForAndThrowDataTruncation : ()V
    //   2261: aload_0
    //   2262: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   2265: iconst_0
    //   2266: invokevirtual set : (Z)V
    //   2269: aload #21
    //   2271: ifnull -> 2293
    //   2274: aload #21
    //   2276: invokevirtual cancel : ()Z
    //   2279: pop
    //   2280: aload_0
    //   2281: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   2284: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   2289: invokevirtual purge : ()I
    //   2292: pop
    //   2293: aload #28
    //   2295: monitorexit
    //   2296: aload #25
    //   2298: areturn
    //   2299: astore #22
    //   2301: aload #25
    //   2303: monitorexit
    //   2304: aload #21
    //   2306: astore_3
    //   2307: aload #22
    //   2309: athrow
    //   2310: astore #22
    //   2312: goto -> 2354
    //   2315: astore #22
    //   2317: goto -> 2301
    //   2320: astore_3
    //   2321: goto -> 2330
    //   2324: astore #22
    //   2326: goto -> 2335
    //   2329: astore_3
    //   2330: goto -> 2397
    //   2333: astore #22
    //   2335: goto -> 2354
    //   2338: astore_3
    //   2339: aload #23
    //   2341: astore #21
    //   2343: goto -> 2397
    //   2346: astore_3
    //   2347: aload #22
    //   2349: astore #21
    //   2351: aload_3
    //   2352: astore #22
    //   2354: aload #21
    //   2356: astore_3
    //   2357: aload #29
    //   2359: invokevirtual shouldIntercept : ()Z
    //   2362: ifeq -> 2383
    //   2365: aload #21
    //   2367: astore_3
    //   2368: aload #29
    //   2370: aload_0
    //   2371: getfield originalSql : Ljava/lang/String;
    //   2374: aload_0
    //   2375: aconst_null
    //   2376: iconst_1
    //   2377: aload #22
    //   2379: invokevirtual invokeStatementInterceptorsPost : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Lcom/mysql/jdbc/ResultSetInternalMethods;ZLjava/sql/SQLException;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   2382: pop
    //   2383: aload #21
    //   2385: astore_3
    //   2386: aload #22
    //   2388: athrow
    //   2389: astore #22
    //   2391: aload_3
    //   2392: astore #21
    //   2394: aload #22
    //   2396: astore_3
    //   2397: aload_0
    //   2398: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   2401: iconst_0
    //   2402: invokevirtual set : (Z)V
    //   2405: aload #21
    //   2407: ifnull -> 2429
    //   2410: aload #21
    //   2412: invokevirtual cancel : ()Z
    //   2415: pop
    //   2416: aload_0
    //   2417: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   2420: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   2425: invokevirtual purge : ()I
    //   2428: pop
    //   2429: aload_3
    //   2430: athrow
    //   2431: astore_3
    //   2432: aload #28
    //   2434: monitorexit
    //   2435: aload_3
    //   2436: athrow
    // Exception table:
    //   from	to	target	type
    //   14	46	2431	finally
    //   51	54	2431	finally
    //   57	64	2431	finally
    //   73	90	2431	finally
    //   98	109	2431	finally
    //   114	128	2431	finally
    //   131	176	2431	finally
    //   176	186	2431	finally
    //   203	207	2431	finally
    //   210	232	2431	finally
    //   238	292	2431	finally
    //   295	332	2431	finally
    //   338	354	2431	finally
    //   354	410	2431	finally
    //   413	419	2431	finally
    //   419	425	2431	finally
    //   425	444	2431	finally
    //   454	460	2431	finally
    //   466	479	2431	finally
    //   488	502	2431	finally
    //   505	529	2431	finally
    //   538	588	2431	finally
    //   591	598	2431	finally
    //   623	680	2431	finally
    //   699	706	2431	finally
    //   706	710	2431	finally
    //   732	739	2346	java/sql/SQLException
    //   732	739	2338	finally
    //   757	764	2346	java/sql/SQLException
    //   757	764	2338	finally
    //   772	784	2346	java/sql/SQLException
    //   772	784	2338	finally
    //   792	799	2346	java/sql/SQLException
    //   792	799	2338	finally
    //   807	822	2346	java/sql/SQLException
    //   807	822	2338	finally
    //   830	835	2346	java/sql/SQLException
    //   830	835	2338	finally
    //   843	850	2346	java/sql/SQLException
    //   843	850	2338	finally
    //   850	869	876	java/sql/SQLException
    //   850	869	872	finally
    //   883	887	2333	java/sql/SQLException
    //   883	887	2329	finally
    //   887	902	2324	java/sql/SQLException
    //   887	902	2320	finally
    //   912	918	936	java/sql/SQLException
    //   912	918	932	finally
    //   940	947	2324	java/sql/SQLException
    //   940	947	2320	finally
    //   952	978	936	java/sql/SQLException
    //   952	978	932	finally
    //   990	993	936	java/sql/SQLException
    //   990	993	932	finally
    //   1001	1007	2346	java/sql/SQLException
    //   1001	1007	2338	finally
    //   1015	1018	2346	java/sql/SQLException
    //   1015	1018	2338	finally
    //   1018	1040	2299	finally
    //   1043	1051	2299	finally
    //   1051	1057	2299	finally
    //   1057	1060	2299	finally
    //   1099	1106	2346	java/sql/SQLException
    //   1099	1106	2338	finally
    //   1114	1130	2346	java/sql/SQLException
    //   1114	1130	2338	finally
    //   1144	1157	2346	java/sql/SQLException
    //   1144	1157	2338	finally
    //   1165	1176	2346	java/sql/SQLException
    //   1165	1176	2338	finally
    //   1195	1200	2346	java/sql/SQLException
    //   1195	1200	2338	finally
    //   1208	1223	2346	java/sql/SQLException
    //   1208	1223	2338	finally
    //   1231	1243	2346	java/sql/SQLException
    //   1231	1243	2338	finally
    //   1251	1262	2346	java/sql/SQLException
    //   1251	1262	2338	finally
    //   1270	1282	2346	java/sql/SQLException
    //   1270	1282	2338	finally
    //   1290	1298	2346	java/sql/SQLException
    //   1290	1298	2338	finally
    //   1306	1318	2346	java/sql/SQLException
    //   1306	1318	2338	finally
    //   1326	1335	2346	java/sql/SQLException
    //   1326	1335	2338	finally
    //   1343	1353	2346	java/sql/SQLException
    //   1343	1353	2338	finally
    //   1361	1370	2346	java/sql/SQLException
    //   1361	1370	2338	finally
    //   1378	1386	2346	java/sql/SQLException
    //   1378	1386	2338	finally
    //   1394	1400	2346	java/sql/SQLException
    //   1394	1400	2338	finally
    //   1408	1413	2346	java/sql/SQLException
    //   1408	1413	2338	finally
    //   1421	1427	2346	java/sql/SQLException
    //   1421	1427	2338	finally
    //   1435	1446	2346	java/sql/SQLException
    //   1435	1446	2338	finally
    //   1454	1460	2346	java/sql/SQLException
    //   1454	1460	2338	finally
    //   1468	1473	2346	java/sql/SQLException
    //   1468	1473	2338	finally
    //   1481	1488	2346	java/sql/SQLException
    //   1481	1488	2338	finally
    //   1496	1501	2346	java/sql/SQLException
    //   1496	1501	2338	finally
    //   1509	1514	2346	java/sql/SQLException
    //   1509	1514	2338	finally
    //   1522	1556	2346	java/sql/SQLException
    //   1522	1556	2338	finally
    //   1564	1573	2346	java/sql/SQLException
    //   1564	1573	2338	finally
    //   1586	1597	2346	java/sql/SQLException
    //   1586	1597	2338	finally
    //   1605	1614	2346	java/sql/SQLException
    //   1605	1614	2338	finally
    //   1622	1629	2346	java/sql/SQLException
    //   1622	1629	2338	finally
    //   1637	1646	2346	java/sql/SQLException
    //   1637	1646	2338	finally
    //   1654	1660	2346	java/sql/SQLException
    //   1654	1660	2338	finally
    //   1668	1673	2346	java/sql/SQLException
    //   1668	1673	2338	finally
    //   1681	1687	2346	java/sql/SQLException
    //   1681	1687	2338	finally
    //   1695	1701	2346	java/sql/SQLException
    //   1695	1701	2338	finally
    //   1709	1715	2346	java/sql/SQLException
    //   1709	1715	2338	finally
    //   1723	1728	2346	java/sql/SQLException
    //   1723	1728	2338	finally
    //   1736	1743	2346	java/sql/SQLException
    //   1736	1743	2338	finally
    //   1751	1758	2346	java/sql/SQLException
    //   1751	1758	2338	finally
    //   1766	1771	2346	java/sql/SQLException
    //   1766	1771	2338	finally
    //   1779	1784	2346	java/sql/SQLException
    //   1779	1784	2338	finally
    //   1792	1829	2346	java/sql/SQLException
    //   1792	1829	2338	finally
    //   1837	1846	2346	java/sql/SQLException
    //   1837	1846	2338	finally
    //   1854	1885	2346	java/sql/SQLException
    //   1854	1885	2338	finally
    //   1897	1905	2346	java/sql/SQLException
    //   1897	1905	2338	finally
    //   1913	1928	2346	java/sql/SQLException
    //   1913	1928	2338	finally
    //   1947	1954	2346	java/sql/SQLException
    //   1947	1954	2338	finally
    //   1962	1969	2346	java/sql/SQLException
    //   1962	1969	2338	finally
    //   1977	1983	2346	java/sql/SQLException
    //   1977	1983	2338	finally
    //   1991	1996	2346	java/sql/SQLException
    //   1991	1996	2338	finally
    //   2004	2010	2346	java/sql/SQLException
    //   2004	2010	2338	finally
    //   2018	2029	2346	java/sql/SQLException
    //   2018	2029	2338	finally
    //   2037	2042	2346	java/sql/SQLException
    //   2037	2042	2338	finally
    //   2050	2055	2346	java/sql/SQLException
    //   2050	2055	2338	finally
    //   2063	2070	2346	java/sql/SQLException
    //   2063	2070	2338	finally
    //   2078	2082	2346	java/sql/SQLException
    //   2078	2082	2338	finally
    //   2090	2094	2346	java/sql/SQLException
    //   2090	2094	2338	finally
    //   2102	2132	2346	java/sql/SQLException
    //   2102	2132	2338	finally
    //   2140	2149	2346	java/sql/SQLException
    //   2140	2149	2338	finally
    //   2162	2174	2346	java/sql/SQLException
    //   2162	2174	2338	finally
    //   2182	2194	2346	java/sql/SQLException
    //   2182	2194	2338	finally
    //   2206	2213	2346	java/sql/SQLException
    //   2206	2213	2338	finally
    //   2221	2225	2346	java/sql/SQLException
    //   2221	2225	2338	finally
    //   2228	2233	2310	java/sql/SQLException
    //   2228	2233	2389	finally
    //   2236	2242	2310	java/sql/SQLException
    //   2236	2242	2389	finally
    //   2245	2253	2310	java/sql/SQLException
    //   2245	2253	2389	finally
    //   2256	2261	2310	java/sql/SQLException
    //   2256	2261	2389	finally
    //   2261	2269	2431	finally
    //   2274	2293	2431	finally
    //   2293	2296	2431	finally
    //   2301	2304	2315	finally
    //   2307	2310	2310	java/sql/SQLException
    //   2307	2310	2389	finally
    //   2357	2365	2389	finally
    //   2368	2383	2389	finally
    //   2386	2389	2389	finally
    //   2397	2405	2431	finally
    //   2410	2429	2431	finally
    //   2429	2431	2431	finally
    //   2432	2435	2431	finally
  }
  
  private void serverLongData(int paramInt, BindValue paramBindValue) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      MysqlIO mysqlIO = this.connection.getIO();
      Buffer buffer = mysqlIO.getSharedSendPacket();
      Object object = paramBindValue.value;
      if (object instanceof byte[]) {
        buffer.clear();
        buffer.writeByte((byte)24);
        buffer.writeLong(this.serverStatementId);
        buffer.writeInt(paramInt);
        buffer.writeBytesNoNull((byte[])paramBindValue.value);
        mysqlIO.sendCommand(24, null, buffer, true, null, 0);
      } else if (object instanceof InputStream) {
        storeStream(mysqlIO, paramInt, buffer, (InputStream)object);
      } else if (object instanceof Blob) {
        storeStream(mysqlIO, paramInt, buffer, ((Blob)object).getBinaryStream());
      } else if (object instanceof Reader) {
        storeReader(mysqlIO, paramInt, buffer, (Reader)object);
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(Messages.getString("ServerPreparedStatement.18"));
        stringBuilder.append(object.getClass().getName());
        stringBuilder.append("'");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      } 
      return;
    } 
  }
  
  private void serverPrepare(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      MysqlIO mysqlIO = this.connection.getIO();
      if (this.connection.getAutoGenerateTestcaseScript())
        dumpPrepareForTestcase(); 
      long l = 0L;
      try {
        if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "LOAD DATA")) {
          this.isLoadDataQuery = true;
        } else {
          this.isLoadDataQuery = false;
        } 
        if (this.connection.getProfileSql())
          l = System.currentTimeMillis(); 
        String str = this.connection.getEncoding();
        if (this.isLoadDataQuery || !this.connection.getUseUnicode() || str == null)
          str = null; 
        Buffer buffer = mysqlIO.sendCommand(22, paramString, null, false, str, 0);
        if (this.connection.versionMeetsMinimum(4, 1, 1)) {
          buffer.setPosition(1);
        } else {
          buffer.setPosition(0);
        } 
        this.serverStatementId = buffer.readLong();
        this.fieldCount = buffer.readInt();
        int i = buffer.readInt();
        this.parameterCount = i;
        this.parameterBindings = new BindValue[i];
        for (i = 0; i < this.parameterCount; i++)
          this.parameterBindings[i] = new BindValue(); 
        this.connection.incrementNumberOfPrepares();
        if (this.profileSQL) {
          ProfilerEventHandler profilerEventHandler = this.eventSink;
          ProfilerEvent profilerEvent = new ProfilerEvent();
          String str1 = this.currentCatalog;
          long l3 = this.connectionId;
          i = this.statementId;
          long l1 = System.currentTimeMillis();
          long l2 = mysqlIO.getCurrentTimeNanosOrMillis();
          String str2 = mysqlIO.getQueryTimingUnits();
          Throwable throwable = new Throwable();
          this();
          this((byte)2, "", str1, l3, i, -1, l1, l2 - l, str2, null, LogUtils.findCallingClassAndMethod(throwable), truncateQueryToLog(paramString));
          profilerEventHandler.consumeEvent(profilerEvent);
        } 
        if (!mysqlIO.isEOFDeprecated()) {
          i = 1;
        } else {
          i = 0;
        } 
        if (this.parameterCount > 0 && this.connection.versionMeetsMinimum(4, 1, 2) && !mysqlIO.isVersion(5, 0, 0)) {
          this.parameterFields = new Field[this.parameterCount];
          for (byte b = 0; b < this.parameterCount; b++) {
            Buffer buffer1 = mysqlIO.readPacket();
            this.parameterFields[b] = mysqlIO.unpackField(buffer1, false);
          } 
          if (i != 0)
            mysqlIO.readPacket(); 
        } 
        int j = this.fieldCount;
        if (j > 0) {
          this.resultFields = new Field[j];
          for (j = 0; j < this.fieldCount; j++) {
            Buffer buffer1 = mysqlIO.readPacket();
            this.resultFields[j] = mysqlIO.unpackField(buffer1, false);
          } 
          if (i != 0)
            mysqlIO.readPacket(); 
        } 
        this.connection.getIO().clearInputStream();
        return;
      } catch (SQLException sQLException1) {
        SQLException sQLException2 = sQLException1;
        if (this.connection.getDumpQueriesOnException()) {
          StringBuilder stringBuilder = new StringBuilder();
          this(this.originalSql.length() + 32);
          stringBuilder.append("\n\nQuery being prepared when exception was thrown:\n\n");
          stringBuilder.append(this.originalSql);
          sQLException2 = ConnectionImpl.appendMessageToException(sQLException1, stringBuilder.toString(), getExceptionInterceptor());
        } 
        throw sQLException2;
      } finally {}
      this.connection.getIO().clearInputStream();
      throw paramString;
    } 
  }
  
  private void serverResetStatement() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      MysqlIO mysqlIO = this.connection.getIO();
      Buffer buffer = mysqlIO.getSharedSendPacket();
      buffer.clear();
      buffer.writeByte((byte)26);
      buffer.writeLong(this.serverStatementId);
      try {
        boolean bool;
        if (!this.connection.versionMeetsMinimum(4, 1, 2)) {
          bool = true;
        } else {
          bool = false;
        } 
        mysqlIO.sendCommand(26, null, buffer, bool, null, 0);
        mysqlIO.clearInputStream();
        return;
      } catch (SQLException sQLException) {
        throw sQLException;
      } catch (Exception exception) {
        SQLException sQLException = SQLError.createSQLException(exception.toString(), "S1000", getExceptionInterceptor());
        sQLException.initCause(exception);
        throw sQLException;
      } finally {}
      mysqlIO.clearInputStream();
      throw buffer;
    } 
  }
  
  private void setTimeInternal(int paramInt, Time paramTime, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    if (paramTime == null) {
      setNull(paramInt, 92);
    } else {
      BindValue bindValue = getBinding(paramInt, false);
      resetToType(bindValue, 11);
      if (!this.useLegacyDatetimeCode) {
        bindValue.value = paramTime;
        if (paramCalendar != null)
          bindValue.calendar = (Calendar)paramCalendar.clone(); 
      } else {
        Calendar calendar = getCalendarInstanceForSessionOrNew();
        bindValue.value = TimeUtil.changeTimezone(this.connection, calendar, paramCalendar, paramTime, paramTimeZone, this.connection.getServerTimezoneTZ(), paramBoolean);
      } 
    } 
  }
  
  private void setTimestampInternal(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    if (paramTimestamp == null) {
      setNull(paramInt, 93);
    } else {
      BindValue bindValue = getBinding(paramInt, false);
      resetToType(bindValue, 12);
      Timestamp timestamp = paramTimestamp;
      if (!this.sendFractionalSeconds)
        timestamp = TimeUtil.truncateFractionalSeconds(paramTimestamp); 
      if (!this.useLegacyDatetimeCode) {
        bindValue.value = timestamp;
      } else {
        if (this.connection.getUseJDBCCompliantTimezoneShift()) {
          calendar = this.connection.getUtcCalendar();
        } else {
          calendar = getCalendarInstanceForSessionOrNew();
        } 
        Calendar calendar = TimeUtil.setProlepticIfNeeded(calendar, paramCalendar);
        bindValue.value = TimeUtil.changeTimezone(this.connection, calendar, paramCalendar, timestamp, paramTimeZone, this.connection.getServerTimezoneTZ(), paramBoolean);
      } 
      if (paramCalendar != null)
        bindValue.calendar = (Calendar)paramCalendar.clone(); 
    } 
  }
  
  private void storeBinding(Buffer paramBuffer, BindValue paramBindValue, MysqlIO paramMysqlIO) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #5
    //   11: aload #5
    //   13: monitorenter
    //   14: aload_2
    //   15: getfield value : Ljava/lang/Object;
    //   18: astore #6
    //   20: aload_2
    //   21: getfield bufferType : I
    //   24: istore #4
    //   26: iload #4
    //   28: ifeq -> 279
    //   31: iload #4
    //   33: iconst_1
    //   34: if_icmpeq -> 265
    //   37: iload #4
    //   39: iconst_2
    //   40: if_icmpeq -> 247
    //   43: iload #4
    //   45: iconst_3
    //   46: if_icmpeq -> 228
    //   49: iload #4
    //   51: iconst_4
    //   52: if_icmpeq -> 211
    //   55: iload #4
    //   57: iconst_5
    //   58: if_icmpeq -> 193
    //   61: iload #4
    //   63: bipush #7
    //   65: if_icmpeq -> 172
    //   68: iload #4
    //   70: bipush #8
    //   72: if_icmpeq -> 154
    //   75: iload #4
    //   77: bipush #15
    //   79: if_icmpeq -> 279
    //   82: iload #4
    //   84: sipush #246
    //   87: if_icmpeq -> 279
    //   90: iload #4
    //   92: sipush #253
    //   95: if_icmpeq -> 279
    //   98: iload #4
    //   100: sipush #254
    //   103: if_icmpeq -> 279
    //   106: iload #4
    //   108: tableswitch default -> 136, 10 -> 172, 11 -> 140, 12 -> 172
    //   136: aload #5
    //   138: monitorexit
    //   139: return
    //   140: aload_0
    //   141: aload_1
    //   142: aload #6
    //   144: checkcast java/sql/Time
    //   147: invokespecial storeTime : (Lcom/mysql/jdbc/Buffer;Ljava/sql/Time;)V
    //   150: aload #5
    //   152: monitorexit
    //   153: return
    //   154: aload_1
    //   155: bipush #8
    //   157: invokevirtual ensureCapacity : (I)V
    //   160: aload_1
    //   161: aload_2
    //   162: getfield longBinding : J
    //   165: invokevirtual writeLongLong : (J)V
    //   168: aload #5
    //   170: monitorexit
    //   171: return
    //   172: aload_0
    //   173: aload_1
    //   174: aload #6
    //   176: checkcast java/util/Date
    //   179: aload_3
    //   180: iload #4
    //   182: aload_2
    //   183: getfield calendar : Ljava/util/Calendar;
    //   186: invokespecial storeDateTime : (Lcom/mysql/jdbc/Buffer;Ljava/util/Date;Lcom/mysql/jdbc/MysqlIO;ILjava/util/Calendar;)V
    //   189: aload #5
    //   191: monitorexit
    //   192: return
    //   193: aload_1
    //   194: bipush #8
    //   196: invokevirtual ensureCapacity : (I)V
    //   199: aload_1
    //   200: aload_2
    //   201: getfield doubleBinding : D
    //   204: invokevirtual writeDouble : (D)V
    //   207: aload #5
    //   209: monitorexit
    //   210: return
    //   211: aload_1
    //   212: iconst_4
    //   213: invokevirtual ensureCapacity : (I)V
    //   216: aload_1
    //   217: aload_2
    //   218: getfield floatBinding : F
    //   221: invokevirtual writeFloat : (F)V
    //   224: aload #5
    //   226: monitorexit
    //   227: return
    //   228: aload_1
    //   229: iconst_4
    //   230: invokevirtual ensureCapacity : (I)V
    //   233: aload_1
    //   234: aload_2
    //   235: getfield longBinding : J
    //   238: l2i
    //   239: i2l
    //   240: invokevirtual writeLong : (J)V
    //   243: aload #5
    //   245: monitorexit
    //   246: return
    //   247: aload_1
    //   248: iconst_2
    //   249: invokevirtual ensureCapacity : (I)V
    //   252: aload_1
    //   253: aload_2
    //   254: getfield longBinding : J
    //   257: l2i
    //   258: invokevirtual writeInt : (I)V
    //   261: aload #5
    //   263: monitorexit
    //   264: return
    //   265: aload_1
    //   266: aload_2
    //   267: getfield longBinding : J
    //   270: l2i
    //   271: i2b
    //   272: invokevirtual writeByte : (B)V
    //   275: aload #5
    //   277: monitorexit
    //   278: return
    //   279: aload #6
    //   281: instanceof [B
    //   284: ifeq -> 299
    //   287: aload_1
    //   288: aload #6
    //   290: checkcast [B
    //   293: invokevirtual writeLenBytes : ([B)V
    //   296: goto -> 360
    //   299: aload_0
    //   300: getfield isLoadDataQuery : Z
    //   303: ifne -> 348
    //   306: aload_1
    //   307: aload #6
    //   309: checkcast java/lang/String
    //   312: aload_0
    //   313: getfield charEncoding : Ljava/lang/String;
    //   316: aload_0
    //   317: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   320: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   325: aload_0
    //   326: getfield charConverter : Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   329: aload_0
    //   330: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   333: invokeinterface parserKnowsUnicode : ()Z
    //   338: aload_0
    //   339: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   342: invokevirtual writeLenString : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;ZLcom/mysql/jdbc/MySQLConnection;)V
    //   345: goto -> 360
    //   348: aload_1
    //   349: aload #6
    //   351: checkcast java/lang/String
    //   354: invokestatic getBytes : (Ljava/lang/String;)[B
    //   357: invokevirtual writeLenBytes : ([B)V
    //   360: aload #5
    //   362: monitorexit
    //   363: return
    //   364: astore_1
    //   365: goto -> 424
    //   368: astore_1
    //   369: new java/lang/StringBuilder
    //   372: astore_1
    //   373: aload_1
    //   374: invokespecial <init> : ()V
    //   377: aload_1
    //   378: ldc_w 'ServerPreparedStatement.22'
    //   381: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   387: pop
    //   388: aload_1
    //   389: aload_0
    //   390: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   393: invokeinterface getEncoding : ()Ljava/lang/String;
    //   398: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   401: pop
    //   402: aload_1
    //   403: ldc_w '''
    //   406: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   409: pop
    //   410: aload_1
    //   411: invokevirtual toString : ()Ljava/lang/String;
    //   414: ldc 'S1000'
    //   416: aload_0
    //   417: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   420: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   423: athrow
    //   424: aload #5
    //   426: monitorexit
    //   427: aload_1
    //   428: athrow
    // Exception table:
    //   from	to	target	type
    //   14	26	368	java/io/UnsupportedEncodingException
    //   14	26	364	finally
    //   136	139	364	finally
    //   140	150	368	java/io/UnsupportedEncodingException
    //   140	150	364	finally
    //   150	153	364	finally
    //   154	168	368	java/io/UnsupportedEncodingException
    //   154	168	364	finally
    //   168	171	364	finally
    //   172	189	368	java/io/UnsupportedEncodingException
    //   172	189	364	finally
    //   189	192	364	finally
    //   193	207	368	java/io/UnsupportedEncodingException
    //   193	207	364	finally
    //   207	210	364	finally
    //   211	224	368	java/io/UnsupportedEncodingException
    //   211	224	364	finally
    //   224	227	364	finally
    //   228	243	368	java/io/UnsupportedEncodingException
    //   228	243	364	finally
    //   243	246	364	finally
    //   247	261	368	java/io/UnsupportedEncodingException
    //   247	261	364	finally
    //   261	264	364	finally
    //   265	275	368	java/io/UnsupportedEncodingException
    //   265	275	364	finally
    //   275	278	364	finally
    //   279	296	368	java/io/UnsupportedEncodingException
    //   279	296	364	finally
    //   299	345	368	java/io/UnsupportedEncodingException
    //   299	345	364	finally
    //   348	360	368	java/io/UnsupportedEncodingException
    //   348	360	364	finally
    //   360	363	364	finally
    //   369	424	364	finally
    //   424	427	364	finally
  }
  
  private void storeDateTime(Buffer paramBuffer, Date paramDate, MysqlIO paramMysqlIO, int paramInt, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.connection.versionMeetsMinimum(4, 1, 3)) {
        storeDateTime413AndNewer(paramBuffer, paramDate, paramInt, paramCalendar);
      } else {
        storeDateTime412AndOlder(paramBuffer, paramDate, paramInt);
      } 
      return;
    } 
  }
  
  private void storeDateTime412AndOlder(Buffer paramBuffer, Date paramDate, int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Calendar calendar;
      if (!this.useLegacyDatetimeCode) {
        if (paramInt == 10) {
          calendar = getDefaultTzCalendar();
        } else {
          calendar = getServerTzCalendar();
        } 
      } else if (paramDate instanceof Timestamp && this.connection.getUseJDBCCompliantTimezoneShift()) {
        calendar = this.connection.getUtcCalendar();
      } else {
        calendar = getCalendarInstanceForSessionOrNew();
      } 
      Date date = calendar.getTime();
      try {
        paramBuffer.ensureCapacity(8);
        paramBuffer.writeByte((byte)7);
        calendar.setTime(paramDate);
        int i = calendar.get(1);
        int j = calendar.get(2);
        paramInt = calendar.get(5);
        paramBuffer.writeInt(i);
        paramBuffer.writeByte((byte)(j + 1));
        paramBuffer.writeByte((byte)paramInt);
        if (paramDate instanceof Date) {
          paramBuffer.writeByte((byte)0);
          paramBuffer.writeByte((byte)0);
          paramBuffer.writeByte((byte)0);
        } else {
          paramBuffer.writeByte((byte)calendar.get(11));
          paramBuffer.writeByte((byte)calendar.get(12));
          paramBuffer.writeByte((byte)calendar.get(13));
        } 
        return;
      } finally {
        calendar.setTime(date);
      } 
    } 
  }
  
  private void storeDateTime413AndNewer(Buffer paramBuffer, Date paramDate, int paramInt, Calendar paramCalendar) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #9
    //   11: aload #9
    //   13: monitorenter
    //   14: aload #4
    //   16: astore #8
    //   18: aload #4
    //   20: ifnonnull -> 97
    //   23: aload_0
    //   24: getfield useLegacyDatetimeCode : Z
    //   27: ifne -> 54
    //   30: iload_3
    //   31: bipush #10
    //   33: if_icmpne -> 45
    //   36: aload_0
    //   37: invokespecial getDefaultTzCalendar : ()Ljava/util/Calendar;
    //   40: astore #8
    //   42: goto -> 97
    //   45: aload_0
    //   46: invokespecial getServerTzCalendar : ()Ljava/util/Calendar;
    //   49: astore #8
    //   51: goto -> 97
    //   54: aload_2
    //   55: instanceof java/sql/Timestamp
    //   58: ifeq -> 87
    //   61: aload_0
    //   62: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   65: invokeinterface getUseJDBCCompliantTimezoneShift : ()Z
    //   70: ifeq -> 87
    //   73: aload_0
    //   74: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   77: invokeinterface getUtcCalendar : ()Ljava/util/Calendar;
    //   82: astore #4
    //   84: goto -> 93
    //   87: aload_0
    //   88: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   91: astore #4
    //   93: aload #4
    //   95: astore #8
    //   97: aload #8
    //   99: invokevirtual getTime : ()Ljava/util/Date;
    //   102: astore #4
    //   104: aload #8
    //   106: aload_2
    //   107: invokevirtual setTime : (Ljava/util/Date;)V
    //   110: aload_2
    //   111: instanceof java/sql/Date
    //   114: ifeq -> 141
    //   117: aload #8
    //   119: bipush #11
    //   121: iconst_0
    //   122: invokevirtual set : (II)V
    //   125: aload #8
    //   127: bipush #12
    //   129: iconst_0
    //   130: invokevirtual set : (II)V
    //   133: aload #8
    //   135: bipush #13
    //   137: iconst_0
    //   138: invokevirtual set : (II)V
    //   141: bipush #7
    //   143: istore #5
    //   145: aload_2
    //   146: instanceof java/sql/Timestamp
    //   149: ifeq -> 156
    //   152: bipush #11
    //   154: istore #5
    //   156: aload_1
    //   157: iload #5
    //   159: invokevirtual ensureCapacity : (I)V
    //   162: aload_1
    //   163: iload #5
    //   165: invokevirtual writeByte : (B)V
    //   168: aload #8
    //   170: iconst_1
    //   171: invokevirtual get : (I)I
    //   174: istore #6
    //   176: aload #8
    //   178: iconst_2
    //   179: invokevirtual get : (I)I
    //   182: istore #7
    //   184: aload #8
    //   186: iconst_5
    //   187: invokevirtual get : (I)I
    //   190: istore_3
    //   191: aload_1
    //   192: iload #6
    //   194: invokevirtual writeInt : (I)V
    //   197: aload_1
    //   198: iload #7
    //   200: iconst_1
    //   201: iadd
    //   202: i2b
    //   203: invokevirtual writeByte : (B)V
    //   206: aload_1
    //   207: iload_3
    //   208: i2b
    //   209: invokevirtual writeByte : (B)V
    //   212: aload_2
    //   213: instanceof java/sql/Date
    //   216: ifeq -> 237
    //   219: aload_1
    //   220: iconst_0
    //   221: invokevirtual writeByte : (B)V
    //   224: aload_1
    //   225: iconst_0
    //   226: invokevirtual writeByte : (B)V
    //   229: aload_1
    //   230: iconst_0
    //   231: invokevirtual writeByte : (B)V
    //   234: goto -> 273
    //   237: aload_1
    //   238: aload #8
    //   240: bipush #11
    //   242: invokevirtual get : (I)I
    //   245: i2b
    //   246: invokevirtual writeByte : (B)V
    //   249: aload_1
    //   250: aload #8
    //   252: bipush #12
    //   254: invokevirtual get : (I)I
    //   257: i2b
    //   258: invokevirtual writeByte : (B)V
    //   261: aload_1
    //   262: aload #8
    //   264: bipush #13
    //   266: invokevirtual get : (I)I
    //   269: i2b
    //   270: invokevirtual writeByte : (B)V
    //   273: iload #5
    //   275: bipush #11
    //   277: if_icmpne -> 296
    //   280: aload_1
    //   281: aload_2
    //   282: checkcast java/sql/Timestamp
    //   285: invokevirtual getNanos : ()I
    //   288: sipush #1000
    //   291: idiv
    //   292: i2l
    //   293: invokevirtual writeLong : (J)V
    //   296: aload #8
    //   298: aload #4
    //   300: invokevirtual setTime : (Ljava/util/Date;)V
    //   303: aload #9
    //   305: monitorexit
    //   306: return
    //   307: astore_1
    //   308: aload #8
    //   310: aload #4
    //   312: invokevirtual setTime : (Ljava/util/Date;)V
    //   315: aload_1
    //   316: athrow
    //   317: astore_1
    //   318: aload #9
    //   320: monitorexit
    //   321: aload_1
    //   322: athrow
    // Exception table:
    //   from	to	target	type
    //   23	30	317	finally
    //   36	42	317	finally
    //   45	51	317	finally
    //   54	84	317	finally
    //   87	93	317	finally
    //   97	104	317	finally
    //   104	141	307	finally
    //   145	152	307	finally
    //   156	234	307	finally
    //   237	273	307	finally
    //   280	296	307	finally
    //   296	306	317	finally
    //   308	317	317	finally
    //   318	321	317	finally
  }
  
  private void storeReader(MysqlIO paramMysqlIO, int paramInt, Buffer paramBuffer, Reader paramReader) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #14
    //   11: aload #14
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   18: invokeinterface getClobCharacterEncoding : ()Ljava/lang/String;
    //   23: astore #13
    //   25: aload #13
    //   27: astore #12
    //   29: aload #13
    //   31: ifnonnull -> 45
    //   34: aload_0
    //   35: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   38: invokeinterface getEncoding : ()Ljava/lang/String;
    //   43: astore #12
    //   45: iconst_2
    //   46: istore #6
    //   48: iload #6
    //   50: istore #5
    //   52: aload #12
    //   54: ifnull -> 100
    //   57: aload #12
    //   59: ldc_w 'UTF-16'
    //   62: invokevirtual equals : (Ljava/lang/Object;)Z
    //   65: ifne -> 97
    //   68: aload_0
    //   69: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   72: aload #12
    //   74: invokeinterface getMaxBytesPerChar : (Ljava/lang/String;)I
    //   79: istore #5
    //   81: iload #5
    //   83: iconst_1
    //   84: if_icmpne -> 94
    //   87: iload #6
    //   89: istore #5
    //   91: goto -> 100
    //   94: goto -> 100
    //   97: iconst_4
    //   98: istore #5
    //   100: sipush #8192
    //   103: iload #5
    //   105: idiv
    //   106: newarray char
    //   108: astore #13
    //   110: aload_0
    //   111: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   114: invokeinterface getBlobSendChunkSize : ()I
    //   119: istore #7
    //   121: aload_3
    //   122: invokevirtual clear : ()V
    //   125: aload_3
    //   126: bipush #24
    //   128: invokevirtual writeByte : (B)V
    //   131: aload_3
    //   132: aload_0
    //   133: getfield serverStatementId : J
    //   136: invokevirtual writeLong : (J)V
    //   139: aload_3
    //   140: iload_2
    //   141: invokevirtual writeInt : (I)V
    //   144: iconst_0
    //   145: istore #8
    //   147: iconst_0
    //   148: istore #9
    //   150: iconst_0
    //   151: istore #6
    //   153: iconst_0
    //   154: istore #5
    //   156: aload #4
    //   158: aload #13
    //   160: invokevirtual read : ([C)I
    //   163: istore #10
    //   165: iload #10
    //   167: iconst_m1
    //   168: if_icmpeq -> 302
    //   171: aload_0
    //   172: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   175: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   180: astore #15
    //   182: aload_0
    //   183: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   186: invokeinterface parserKnowsUnicode : ()Z
    //   191: istore #11
    //   193: aload_0
    //   194: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   197: astore #16
    //   199: aload #13
    //   201: aconst_null
    //   202: aload #12
    //   204: aload #15
    //   206: iconst_0
    //   207: iload #10
    //   209: iload #11
    //   211: aload #16
    //   213: invokestatic getBytes : ([CLcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;IIZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   216: astore #15
    //   218: aload_3
    //   219: aload #15
    //   221: iconst_0
    //   222: aload #15
    //   224: arraylength
    //   225: invokevirtual writeBytesNoNull : ([BII)V
    //   228: iload #5
    //   230: aload #15
    //   232: arraylength
    //   233: iadd
    //   234: istore #5
    //   236: aload #15
    //   238: arraylength
    //   239: iload #8
    //   241: iadd
    //   242: istore #8
    //   244: iload #5
    //   246: iload #7
    //   248: if_icmplt -> 296
    //   251: aload_1
    //   252: bipush #24
    //   254: aconst_null
    //   255: aload_3
    //   256: iconst_1
    //   257: aconst_null
    //   258: iconst_0
    //   259: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   262: pop
    //   263: aload_3
    //   264: invokevirtual clear : ()V
    //   267: aload_3
    //   268: bipush #24
    //   270: invokevirtual writeByte : (B)V
    //   273: aload_3
    //   274: aload_0
    //   275: getfield serverStatementId : J
    //   278: invokevirtual writeLong : (J)V
    //   281: aload_3
    //   282: iload_2
    //   283: invokevirtual writeInt : (I)V
    //   286: iload #8
    //   288: istore #6
    //   290: iconst_0
    //   291: istore #5
    //   293: goto -> 296
    //   296: iconst_1
    //   297: istore #9
    //   299: goto -> 156
    //   302: iload #8
    //   304: iload #6
    //   306: if_icmpeq -> 321
    //   309: aload_1
    //   310: bipush #24
    //   312: aconst_null
    //   313: aload_3
    //   314: iconst_1
    //   315: aconst_null
    //   316: iconst_0
    //   317: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   320: pop
    //   321: iload #9
    //   323: ifne -> 338
    //   326: aload_1
    //   327: bipush #24
    //   329: aconst_null
    //   330: aload_3
    //   331: iconst_1
    //   332: aconst_null
    //   333: iconst_0
    //   334: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   337: pop
    //   338: aload_0
    //   339: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   342: invokeinterface getAutoClosePStmtStreams : ()Z
    //   347: istore #11
    //   349: iload #11
    //   351: ifeq -> 364
    //   354: aload #4
    //   356: ifnull -> 364
    //   359: aload #4
    //   361: invokevirtual close : ()V
    //   364: aload #14
    //   366: monitorexit
    //   367: return
    //   368: astore_1
    //   369: goto -> 423
    //   372: astore_1
    //   373: new java/lang/StringBuilder
    //   376: astore_3
    //   377: aload_3
    //   378: invokespecial <init> : ()V
    //   381: aload_3
    //   382: ldc_w 'ServerPreparedStatement.24'
    //   385: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   388: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   391: pop
    //   392: aload_3
    //   393: aload_1
    //   394: invokevirtual toString : ()Ljava/lang/String;
    //   397: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: pop
    //   401: aload_3
    //   402: invokevirtual toString : ()Ljava/lang/String;
    //   405: ldc 'S1000'
    //   407: aload_0
    //   408: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   411: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   414: astore_3
    //   415: aload_3
    //   416: aload_1
    //   417: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   420: pop
    //   421: aload_3
    //   422: athrow
    //   423: aload_0
    //   424: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   427: invokeinterface getAutoClosePStmtStreams : ()Z
    //   432: istore #11
    //   434: iload #11
    //   436: ifeq -> 449
    //   439: aload #4
    //   441: ifnull -> 449
    //   444: aload #4
    //   446: invokevirtual close : ()V
    //   449: aload_1
    //   450: athrow
    //   451: astore_1
    //   452: aload #14
    //   454: monitorexit
    //   455: aload_1
    //   456: athrow
    //   457: astore_1
    //   458: goto -> 364
    //   461: astore_3
    //   462: goto -> 449
    // Exception table:
    //   from	to	target	type
    //   14	25	451	finally
    //   34	45	451	finally
    //   57	81	451	finally
    //   100	121	451	finally
    //   121	144	372	java/io/IOException
    //   121	144	368	finally
    //   156	165	372	java/io/IOException
    //   156	165	368	finally
    //   171	199	372	java/io/IOException
    //   171	199	368	finally
    //   199	244	372	java/io/IOException
    //   199	244	368	finally
    //   251	286	372	java/io/IOException
    //   251	286	368	finally
    //   309	321	372	java/io/IOException
    //   309	321	368	finally
    //   326	338	372	java/io/IOException
    //   326	338	368	finally
    //   338	349	451	finally
    //   359	364	457	java/io/IOException
    //   359	364	451	finally
    //   364	367	451	finally
    //   373	423	368	finally
    //   423	434	451	finally
    //   444	449	461	java/io/IOException
    //   444	449	451	finally
    //   449	451	451	finally
    //   452	455	451	finally
  }
  
  private void storeStream(MysqlIO paramMysqlIO, int paramInt, Buffer paramBuffer, InputStream paramInputStream) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #12
    //   11: aload #12
    //   13: monitorenter
    //   14: sipush #8192
    //   17: newarray byte
    //   19: astore #13
    //   21: aload_0
    //   22: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   25: invokeinterface getBlobSendChunkSize : ()I
    //   30: istore #9
    //   32: aload_3
    //   33: invokevirtual clear : ()V
    //   36: aload_3
    //   37: bipush #24
    //   39: invokevirtual writeByte : (B)V
    //   42: aload_3
    //   43: aload_0
    //   44: getfield serverStatementId : J
    //   47: invokevirtual writeLong : (J)V
    //   50: aload_3
    //   51: iload_2
    //   52: invokevirtual writeInt : (I)V
    //   55: iconst_0
    //   56: istore #5
    //   58: iconst_0
    //   59: istore #7
    //   61: iconst_0
    //   62: istore #6
    //   64: iconst_0
    //   65: istore #8
    //   67: aload #4
    //   69: aload #13
    //   71: invokevirtual read : ([B)I
    //   74: istore #10
    //   76: iload #10
    //   78: iconst_m1
    //   79: if_icmpeq -> 163
    //   82: iconst_1
    //   83: istore #8
    //   85: aload_3
    //   86: aload #13
    //   88: iconst_0
    //   89: iload #10
    //   91: invokevirtual writeBytesNoNull : ([BII)V
    //   94: iload #6
    //   96: iload #10
    //   98: iadd
    //   99: istore #6
    //   101: iload #5
    //   103: iload #10
    //   105: iadd
    //   106: istore #5
    //   108: iload #6
    //   110: iload #9
    //   112: if_icmplt -> 160
    //   115: aload_1
    //   116: bipush #24
    //   118: aconst_null
    //   119: aload_3
    //   120: iconst_1
    //   121: aconst_null
    //   122: iconst_0
    //   123: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   126: pop
    //   127: aload_3
    //   128: invokevirtual clear : ()V
    //   131: aload_3
    //   132: bipush #24
    //   134: invokevirtual writeByte : (B)V
    //   137: aload_3
    //   138: aload_0
    //   139: getfield serverStatementId : J
    //   142: invokevirtual writeLong : (J)V
    //   145: aload_3
    //   146: iload_2
    //   147: invokevirtual writeInt : (I)V
    //   150: iload #5
    //   152: istore #7
    //   154: iconst_0
    //   155: istore #6
    //   157: goto -> 67
    //   160: goto -> 67
    //   163: iload #5
    //   165: iload #7
    //   167: if_icmpeq -> 182
    //   170: aload_1
    //   171: bipush #24
    //   173: aconst_null
    //   174: aload_3
    //   175: iconst_1
    //   176: aconst_null
    //   177: iconst_0
    //   178: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   181: pop
    //   182: iload #8
    //   184: ifne -> 199
    //   187: aload_1
    //   188: bipush #24
    //   190: aconst_null
    //   191: aload_3
    //   192: iconst_1
    //   193: aconst_null
    //   194: iconst_0
    //   195: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   198: pop
    //   199: aload_0
    //   200: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   203: invokeinterface getAutoClosePStmtStreams : ()Z
    //   208: istore #11
    //   210: iload #11
    //   212: ifeq -> 225
    //   215: aload #4
    //   217: ifnull -> 225
    //   220: aload #4
    //   222: invokevirtual close : ()V
    //   225: aload #12
    //   227: monitorexit
    //   228: return
    //   229: astore_1
    //   230: goto -> 284
    //   233: astore_1
    //   234: new java/lang/StringBuilder
    //   237: astore_3
    //   238: aload_3
    //   239: invokespecial <init> : ()V
    //   242: aload_3
    //   243: ldc_w 'ServerPreparedStatement.25'
    //   246: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload_3
    //   254: aload_1
    //   255: invokevirtual toString : ()Ljava/lang/String;
    //   258: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload_3
    //   263: invokevirtual toString : ()Ljava/lang/String;
    //   266: ldc 'S1000'
    //   268: aload_0
    //   269: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   272: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   275: astore_3
    //   276: aload_3
    //   277: aload_1
    //   278: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   281: pop
    //   282: aload_3
    //   283: athrow
    //   284: aload_0
    //   285: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   288: invokeinterface getAutoClosePStmtStreams : ()Z
    //   293: istore #11
    //   295: iload #11
    //   297: ifeq -> 310
    //   300: aload #4
    //   302: ifnull -> 310
    //   305: aload #4
    //   307: invokevirtual close : ()V
    //   310: aload_1
    //   311: athrow
    //   312: astore_1
    //   313: aload #12
    //   315: monitorexit
    //   316: aload_1
    //   317: athrow
    //   318: astore_1
    //   319: goto -> 225
    //   322: astore_3
    //   323: goto -> 310
    // Exception table:
    //   from	to	target	type
    //   14	21	312	finally
    //   21	55	233	java/io/IOException
    //   21	55	229	finally
    //   67	76	233	java/io/IOException
    //   67	76	229	finally
    //   85	94	233	java/io/IOException
    //   85	94	229	finally
    //   115	150	233	java/io/IOException
    //   115	150	229	finally
    //   170	182	233	java/io/IOException
    //   170	182	229	finally
    //   187	199	233	java/io/IOException
    //   187	199	229	finally
    //   199	210	312	finally
    //   220	225	318	java/io/IOException
    //   220	225	312	finally
    //   225	228	312	finally
    //   234	284	229	finally
    //   284	295	312	finally
    //   305	310	322	java/io/IOException
    //   305	310	312	finally
    //   310	312	312	finally
    //   313	316	312	finally
  }
  
  private void storeTime(Buffer paramBuffer, Time paramTime) throws SQLException {
    paramBuffer.ensureCapacity(9);
    paramBuffer.writeByte((byte)8);
    paramBuffer.writeByte((byte)0);
    paramBuffer.writeLong(0L);
    synchronized (getCalendarInstanceForSessionOrNew()) {
      Date date = null.getTime();
      try {
        null.setTime(paramTime);
        paramBuffer.writeByte((byte)null.get(11));
        paramBuffer.writeByte((byte)null.get(12));
        paramBuffer.writeByte((byte)null.get(13));
        return;
      } finally {
        null.setTime(date);
      } 
    } 
  }
  
  private String truncateQueryToLog(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_1
    //   13: astore_2
    //   14: aload_1
    //   15: invokevirtual length : ()I
    //   18: aload_0
    //   19: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   22: invokeinterface getMaxQuerySizeToLog : ()I
    //   27: if_icmple -> 85
    //   30: new java/lang/StringBuilder
    //   33: astore_2
    //   34: aload_2
    //   35: aload_0
    //   36: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   39: invokeinterface getMaxQuerySizeToLog : ()I
    //   44: bipush #12
    //   46: iadd
    //   47: invokespecial <init> : (I)V
    //   50: aload_2
    //   51: aload_1
    //   52: iconst_0
    //   53: aload_0
    //   54: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   57: invokeinterface getMaxQuerySizeToLog : ()I
    //   62: invokevirtual substring : (II)Ljava/lang/String;
    //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload_2
    //   70: ldc_w 'MysqlIO.25'
    //   73: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_2
    //   81: invokevirtual toString : ()Ljava/lang/String;
    //   84: astore_2
    //   85: aload_3
    //   86: monitorexit
    //   87: aload_2
    //   88: areturn
    //   89: astore_1
    //   90: aload_3
    //   91: monitorexit
    //   92: aload_1
    //   93: athrow
    // Exception table:
    //   from	to	target	type
    //   14	85	89	finally
    //   85	87	89	finally
    //   90	92	89	finally
  }
  
  public void addBatch() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.batchedArgs == null) {
        ArrayList<Object> arrayList = new ArrayList();
        this();
        this.batchedArgs = arrayList;
      } 
      List<Object> list = this.batchedArgs;
      BatchedBindValues batchedBindValues = new BatchedBindValues();
      this(this.parameterBindings);
      list.add(batchedBindValues);
      return;
    } 
  }
  
  public String asSql(boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #8
    //   11: aload #8
    //   13: monitorenter
    //   14: aconst_null
    //   15: astore #6
    //   17: aload_0
    //   18: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   21: aload_0
    //   22: getfield originalSql : Ljava/lang/String;
    //   25: aload_0
    //   26: getfield currentCatalog : Ljava/lang/String;
    //   29: invokestatic getInstance : (Lcom/mysql/jdbc/MySQLConnection;Ljava/lang/String;Ljava/lang/String;)Lcom/mysql/jdbc/PreparedStatement;
    //   32: astore #7
    //   34: aload #7
    //   36: astore #6
    //   38: aload #7
    //   40: getfield parameterCount : I
    //   43: istore_3
    //   44: aload #7
    //   46: astore #6
    //   48: aload_0
    //   49: getfield parameterCount : I
    //   52: istore #4
    //   54: iconst_0
    //   55: istore_2
    //   56: iload_2
    //   57: iload_3
    //   58: if_icmpge -> 318
    //   61: iload_2
    //   62: iload #4
    //   64: if_icmpge -> 318
    //   67: aload #7
    //   69: astore #6
    //   71: aload_0
    //   72: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   75: astore #9
    //   77: aload #9
    //   79: iload_2
    //   80: aaload
    //   81: ifnull -> 312
    //   84: aload #7
    //   86: astore #6
    //   88: aload #9
    //   90: iload_2
    //   91: aaload
    //   92: getfield isNull : Z
    //   95: ifeq -> 114
    //   98: aload #7
    //   100: astore #6
    //   102: aload #7
    //   104: iload_2
    //   105: iconst_1
    //   106: iadd
    //   107: iconst_0
    //   108: invokevirtual setNull : (II)V
    //   111: goto -> 312
    //   114: aload #9
    //   116: iload_2
    //   117: aaload
    //   118: astore #10
    //   120: aload #7
    //   122: astore #6
    //   124: aload #10
    //   126: getfield bufferType : I
    //   129: istore #5
    //   131: iload #5
    //   133: iconst_1
    //   134: if_icmpeq -> 293
    //   137: iload #5
    //   139: iconst_2
    //   140: if_icmpeq -> 271
    //   143: iload #5
    //   145: iconst_3
    //   146: if_icmpeq -> 250
    //   149: iload #5
    //   151: iconst_4
    //   152: if_icmpeq -> 230
    //   155: iload #5
    //   157: iconst_5
    //   158: if_icmpeq -> 210
    //   161: iload #5
    //   163: bipush #8
    //   165: if_icmpeq -> 190
    //   168: aload #7
    //   170: astore #6
    //   172: aload #7
    //   174: iload_2
    //   175: iconst_1
    //   176: iadd
    //   177: aload #9
    //   179: iload_2
    //   180: aaload
    //   181: getfield value : Ljava/lang/Object;
    //   184: invokevirtual setObject : (ILjava/lang/Object;)V
    //   187: goto -> 312
    //   190: aload #7
    //   192: astore #6
    //   194: aload #7
    //   196: iload_2
    //   197: iconst_1
    //   198: iadd
    //   199: aload #10
    //   201: getfield longBinding : J
    //   204: invokevirtual setLong : (IJ)V
    //   207: goto -> 312
    //   210: aload #7
    //   212: astore #6
    //   214: aload #7
    //   216: iload_2
    //   217: iconst_1
    //   218: iadd
    //   219: aload #10
    //   221: getfield doubleBinding : D
    //   224: invokevirtual setDouble : (ID)V
    //   227: goto -> 312
    //   230: aload #7
    //   232: astore #6
    //   234: aload #7
    //   236: iload_2
    //   237: iconst_1
    //   238: iadd
    //   239: aload #10
    //   241: getfield floatBinding : F
    //   244: invokevirtual setFloat : (IF)V
    //   247: goto -> 312
    //   250: aload #7
    //   252: astore #6
    //   254: aload #7
    //   256: iload_2
    //   257: iconst_1
    //   258: iadd
    //   259: aload #10
    //   261: getfield longBinding : J
    //   264: l2i
    //   265: invokevirtual setInt : (II)V
    //   268: goto -> 312
    //   271: aload #7
    //   273: astore #6
    //   275: aload #7
    //   277: iload_2
    //   278: iconst_1
    //   279: iadd
    //   280: aload #10
    //   282: getfield longBinding : J
    //   285: l2i
    //   286: i2s
    //   287: invokevirtual setShort : (IS)V
    //   290: goto -> 312
    //   293: aload #7
    //   295: astore #6
    //   297: aload #7
    //   299: iload_2
    //   300: iconst_1
    //   301: iadd
    //   302: aload #10
    //   304: getfield longBinding : J
    //   307: l2i
    //   308: i2b
    //   309: invokevirtual setByte : (IB)V
    //   312: iinc #2, 1
    //   315: goto -> 56
    //   318: aload #7
    //   320: astore #6
    //   322: aload #7
    //   324: iload_1
    //   325: invokevirtual asSql : (Z)Ljava/lang/String;
    //   328: astore #9
    //   330: aload #7
    //   332: ifnull -> 340
    //   335: aload #7
    //   337: invokevirtual close : ()V
    //   340: aload #8
    //   342: monitorexit
    //   343: aload #9
    //   345: areturn
    //   346: astore #7
    //   348: aload #6
    //   350: ifnull -> 366
    //   353: aload #6
    //   355: invokevirtual close : ()V
    //   358: goto -> 366
    //   361: astore #6
    //   363: goto -> 369
    //   366: aload #7
    //   368: athrow
    //   369: aload #8
    //   371: monitorexit
    //   372: aload #6
    //   374: athrow
    //   375: astore #6
    //   377: goto -> 340
    //   380: astore #6
    //   382: goto -> 366
    // Exception table:
    //   from	to	target	type
    //   17	34	346	finally
    //   38	44	346	finally
    //   48	54	346	finally
    //   71	77	346	finally
    //   88	98	346	finally
    //   102	111	346	finally
    //   124	131	346	finally
    //   172	187	346	finally
    //   194	207	346	finally
    //   214	227	346	finally
    //   234	247	346	finally
    //   254	268	346	finally
    //   275	290	346	finally
    //   297	312	346	finally
    //   322	330	346	finally
    //   335	340	375	java/sql/SQLException
    //   335	340	361	finally
    //   340	343	361	finally
    //   353	358	380	java/sql/SQLException
    //   353	358	361	finally
    //   366	369	361	finally
    //   369	372	361	finally
  }
  
  public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.hasCheckedRewrite) {
        this.hasCheckedRewrite = true;
        this.canRewrite = PreparedStatement.canRewrite(this.originalSql, isOnDuplicateKeyUpdate(), getLocationOfOnDuplicateKeyUpdate(), 0);
        PreparedStatement.ParseInfo parseInfo = new PreparedStatement.ParseInfo();
        this(this.originalSql, this.connection, this.connection.getMetaData(), this.charEncoding, this.charConverter);
        this.parseInfo = parseInfo;
      } 
      return this.canRewrite;
    } 
  }
  
  public MySQLConnection checkClosed() throws SQLException {
    if (!this.invalid)
      return super.checkClosed(); 
    throw this.invalidationException;
  }
  
  public void clearParameters() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      clearParametersInternal(true);
      return;
    } 
  }
  
  public void close() throws SQLException {
    null = this.connection;
    if (null == null)
      return; 
    synchronized (null.getConnectionMutex()) {
      if (this.isCached && isPoolable() && !this.isClosed) {
        clearParameters();
        this.isClosed = true;
        this.connection.recachePreparedStatement(this);
        return;
      } 
      this.isClosed = false;
      realClose(true, true);
      return;
    } 
  }
  
  public long[] computeMaxParameterSetSizeAndBatchSize(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #14
    //   11: aload #14
    //   13: monitorenter
    //   14: ldc2_w 10
    //   17: lstore #8
    //   19: lconst_0
    //   20: lstore #6
    //   22: iconst_0
    //   23: istore_2
    //   24: iload_2
    //   25: iload_1
    //   26: if_icmpge -> 176
    //   29: aload_0
    //   30: getfield batchedArgs : Ljava/util/List;
    //   33: iload_2
    //   34: invokeinterface get : (I)Ljava/lang/Object;
    //   39: checkcast com/mysql/jdbc/ServerPreparedStatement$BatchedBindValues
    //   42: getfield batchedParameterValues : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   45: astore #15
    //   47: aload_0
    //   48: getfield parameterCount : I
    //   51: istore_3
    //   52: iload_3
    //   53: bipush #7
    //   55: iadd
    //   56: bipush #8
    //   58: idiv
    //   59: i2l
    //   60: lconst_0
    //   61: ladd
    //   62: iload_3
    //   63: iconst_2
    //   64: imul
    //   65: i2l
    //   66: ladd
    //   67: lstore #4
    //   69: iconst_0
    //   70: istore_3
    //   71: iload_3
    //   72: aload_0
    //   73: getfield parameterBindings : [Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   76: arraylength
    //   77: if_icmpge -> 143
    //   80: lload #4
    //   82: lstore #10
    //   84: aload #15
    //   86: iload_3
    //   87: aaload
    //   88: getfield isNull : Z
    //   91: ifne -> 133
    //   94: aload #15
    //   96: iload_3
    //   97: aaload
    //   98: invokevirtual getBoundLength : ()J
    //   101: lstore #12
    //   103: aload #15
    //   105: iload_3
    //   106: aaload
    //   107: getfield isLongData : Z
    //   110: ifeq -> 126
    //   113: lload #4
    //   115: lstore #10
    //   117: lload #12
    //   119: ldc2_w -1
    //   122: lcmp
    //   123: ifeq -> 133
    //   126: lload #4
    //   128: lload #12
    //   130: ladd
    //   131: lstore #10
    //   133: iinc #3, 1
    //   136: lload #10
    //   138: lstore #4
    //   140: goto -> 71
    //   143: lload #8
    //   145: lload #4
    //   147: ladd
    //   148: lstore #8
    //   150: lload #6
    //   152: lstore #10
    //   154: lload #4
    //   156: lload #6
    //   158: lcmp
    //   159: ifle -> 166
    //   162: lload #4
    //   164: lstore #10
    //   166: iinc #2, 1
    //   169: lload #10
    //   171: lstore #6
    //   173: goto -> 24
    //   176: aload #14
    //   178: monitorexit
    //   179: iconst_2
    //   180: newarray long
    //   182: dup
    //   183: iconst_0
    //   184: lload #6
    //   186: lastore
    //   187: dup
    //   188: iconst_1
    //   189: lload #8
    //   191: lastore
    //   192: areturn
    //   193: astore #15
    //   195: aload #14
    //   197: monitorexit
    //   198: aload #15
    //   200: athrow
    // Exception table:
    //   from	to	target	type
    //   29	69	193	finally
    //   71	80	193	finally
    //   84	113	193	finally
    //   176	193	193	finally
    //   195	198	193	finally
  }
  
  public boolean containsOnDuplicateKeyUpdateInSQL() {
    return this.hasOnDuplicateKeyUpdate;
  }
  
  public long[] executeBatchSerially(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      MySQLConnection mySQLConnection = this.connection;
      if (!mySQLConnection.isReadOnly()) {
        clearWarnings();
        BindValue[] arrayOfBindValue = this.parameterBindings;
        try {
          StatementImpl.CancelTask cancelTask1;
          long[] arrayOfLong;
          List<Object> list = this.batchedArgs;
          StatementImpl.CancelTask cancelTask2 = null;
          ArrayList<ResultSetRow> arrayList = null;
          Object object = null;
          if (list != null) {
            int i = list.size();
            long[] arrayOfLong1 = new long[i];
            if (this.retrieveGeneratedKeys) {
              arrayList = new ArrayList();
              this(i);
              this.batchedGeneratedKeys = arrayList;
            } 
            for (byte b = 0;; b++) {
              long l = -3L;
              if (b < i)
                continue; 
              try {
                TimerTask timerTask;
                if (mySQLConnection.getEnableQueryTimeouts() && paramInt != 0 && mySQLConnection.versionMeetsMinimum(5, 0, 0)) {
                  timerTask = new StatementImpl.CancelTask();
                  this(this, this);
                } else {
                  timerTask = null;
                  arrayList = null;
                  paramInt = 0;
                } 
                if (timerTask != null) {
                  timerTask.cancel();
                  mySQLConnection.getCancelTimer().purge();
                } 
                resetCancelledState();
                if (object == null) {
                  arrayOfLong = arrayOfLong1;
                  break;
                } 
                throw SQLError.createBatchUpdateException(object, arrayOfLong1, getExceptionInterceptor());
              } finally {
                cancelTask1 = cancelTask2;
                if (cancelTask1 != null) {
                  cancelTask1.cancel();
                  mySQLConnection.getCancelTimer().purge();
                } 
                resetCancelledState();
              } 
              arrayOfLong1[b] = -3L;
            } 
          } 
          if (cancelTask1 == null)
            arrayOfLong = new long[0]; 
          return arrayOfLong;
        } finally {
          this.parameterBindings = arrayOfBindValue;
          this.sendTypesToServer = true;
          clearBatch();
        } 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(Messages.getString("ServerPreparedStatement.2"));
      stringBuilder.append(Messages.getString("ServerPreparedStatement.3"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public ResultSetInternalMethods executeInternal(int paramInt, Buffer paramBuffer, boolean paramBoolean1, boolean paramBoolean2, Field[] paramArrayOfField, boolean paramBoolean3) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.numberOfExecutions++;
      try {
        return serverExecute(paramInt, paramBoolean1, paramArrayOfField);
      } catch (SQLException sQLException1) {
        if (this.connection.getEnablePacketDebug())
          this.connection.getIO().dumpPacketRingBuffer(); 
        SQLException sQLException2 = sQLException1;
        if (this.connection.getDumpQueriesOnException()) {
          String str = toString();
          StringBuilder stringBuilder = new StringBuilder();
          this(str.length() + 32);
          stringBuilder.append("\n\nQuery being executed when exception was thrown:\n");
          stringBuilder.append(str);
          stringBuilder.append("\n\n");
          sQLException2 = ConnectionImpl.appendMessageToException(sQLException1, stringBuilder.toString(), getExceptionInterceptor());
        } 
        throw sQLException2;
      } catch (Exception exception) {
        if (this.connection.getEnablePacketDebug())
          this.connection.getIO().dumpPacketRingBuffer(); 
        SQLException sQLException2 = SQLError.createSQLException(exception.toString(), "S1000", getExceptionInterceptor());
        SQLException sQLException1 = sQLException2;
        if (this.connection.getDumpQueriesOnException()) {
          String str = toString();
          StringBuilder stringBuilder = new StringBuilder();
          this(str.length() + 32);
          stringBuilder.append("\n\nQuery being executed when exception was thrown:\n");
          stringBuilder.append(str);
          stringBuilder.append("\n\n");
          sQLException1 = ConnectionImpl.appendMessageToException(sQLException2, stringBuilder.toString(), getExceptionInterceptor());
        } 
        sQLException1.initCause(exception);
        throw sQLException1;
      } 
    } 
  }
  
  public Buffer fillSendPacket() throws SQLException {
    return null;
  }
  
  public Buffer fillSendPacket(byte[][] paramArrayOfbyte, InputStream[] paramArrayOfInputStream, boolean[] paramArrayOfboolean, int[] paramArrayOfint) throws SQLException {
    return null;
  }
  
  public BindValue getBinding(int paramInt, boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      BindValue[] arrayOfBindValue = this.parameterBindings;
      if (arrayOfBindValue.length != 0) {
        if (--paramInt >= 0 && paramInt < arrayOfBindValue.length) {
          if (arrayOfBindValue[paramInt] == null) {
            BindValue bindValue = new BindValue();
            this();
            arrayOfBindValue[paramInt] = bindValue;
          } else if ((arrayOfBindValue[paramInt]).isLongData && !paramBoolean) {
            this.detectedLongParameterSwitch = true;
          } 
          return this.parameterBindings[paramInt];
        } 
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(Messages.getString("ServerPreparedStatement.9"));
        stringBuilder.append(paramInt + 1);
        stringBuilder.append(Messages.getString("ServerPreparedStatement.10"));
        stringBuilder.append(this.parameterBindings.length);
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      } 
      throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.8"), "S1009", getExceptionInterceptor());
    } 
  }
  
  public byte[] getBytes(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      BindValue bindValue = getBinding(paramInt, false);
      if (bindValue.isNull)
        return null; 
      if (!bindValue.isLongData) {
        if (this.outByteBuffer == null) {
          Buffer buffer = new Buffer();
          this(this.connection.getNetBufferLength());
          this.outByteBuffer = buffer;
        } 
        this.outByteBuffer.clear();
        paramInt = this.outByteBuffer.getPosition();
        storeBinding(this.outByteBuffer, bindValue, this.connection.getIO());
        int i = this.outByteBuffer.getPosition() - paramInt;
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(this.outByteBuffer.getByteBuffer(), paramInt, arrayOfByte, 0, i);
        return arrayOfByte;
      } 
      throw SQLError.createSQLFeatureNotSupportedException();
    } 
  }
  
  public int getLocationOfOnDuplicateKeyUpdate() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.locationOfOnDuplicateKeyUpdate == -2)
        this.locationOfOnDuplicateKeyUpdate = StatementImpl.getOnDuplicateKeyLocation(this.originalSql, this.connection.getDontCheckOnDuplicateKeyUpdateInSQL(), this.connection.getRewriteBatchedStatements(), this.connection.isNoBackslashEscapesSet()); 
      return this.locationOfOnDuplicateKeyUpdate;
    } 
  }
  
  public ResultSetMetaData getMetaData() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Field[] arrayOfField = this.resultFields;
      if (arrayOfField == null)
        return null; 
      ResultSetMetaData resultSetMetaData = new ResultSetMetaData();
      this(arrayOfField, this.connection.getUseOldAliasMetadataBehavior(), this.connection.getYearIsDateType(), getExceptionInterceptor());
      return resultSetMetaData;
    } 
  }
  
  public BindValue[] getParameterBindValues() {
    return this.parameterBindings;
  }
  
  public ParameterMetaData getParameterMetaData() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.parameterMetaData == null) {
        MysqlParameterMetadata mysqlParameterMetadata = new MysqlParameterMetadata();
        this(this.parameterFields, this.parameterCount, getExceptionInterceptor());
        this.parameterMetaData = mysqlParameterMetadata;
      } 
      return this.parameterMetaData;
    } 
  }
  
  public long getServerStatementId() {
    return this.serverStatementId;
  }
  
  public boolean isCursorRequired() throws SQLException {
    boolean bool;
    if (this.resultFields != null && this.connection.isCursorFetchEnabled() && getResultSetType() == 1003 && getResultSetConcurrency() == 1007 && getFetchSize() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isNull(int paramInt) {
    throw new IllegalArgumentException(Messages.getString("ServerPreparedStatement.7"));
  }
  
  public boolean isOnDuplicateKeyUpdate() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool;
      if (getLocationOfOnDuplicateKeyUpdate() != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public PreparedStatement prepareBatchedInsertSQL(MySQLConnection paramMySQLConnection, int paramInt) throws SQLException {
    Object object = checkClosed().getConnectionMutex();
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      PreparedStatement preparedStatement = ((Wrapper)paramMySQLConnection.prepareStatement(this.parseInfo.getSqlForBatch(paramInt), this.resultSetType, this.resultSetConcurrency)).<PreparedStatement>unwrap(PreparedStatement.class);
      preparedStatement.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      return preparedStatement;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      SQLException sQLException = SQLError.createSQLException("Unable to prepare batch statement", "S1000", getExceptionInterceptor());
      sQLException.initCause(unsupportedEncodingException);
      throw sQLException;
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw paramMySQLConnection;
  }
  
  public void rePrepare() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.invalidationException = null;
      try {
        serverPrepare(this.originalSql);
      } catch (SQLException sQLException) {
        this.invalidationException = sQLException;
      } catch (Exception exception) {
        SQLException sQLException = SQLError.createSQLException(exception.toString(), "S1000", getExceptionInterceptor());
        this.invalidationException = sQLException;
        sQLException.initCause(exception);
      } 
      if (this.invalidationException != null) {
        this.invalid = true;
        this.parameterBindings = null;
        this.parameterFields = null;
        this.resultFields = null;
        ResultSetInternalMethods resultSetInternalMethods = this.results;
        if (resultSetInternalMethods != null)
          try {
            resultSetInternalMethods.close();
          } catch (Exception exception) {} 
        resultSetInternalMethods = this.generatedKeysResults;
        if (resultSetInternalMethods != null)
          try {
            resultSetInternalMethods.close();
          } catch (Exception exception) {} 
        try {
          closeAllOpenResults();
        } catch (Exception exception) {}
        if (this.connection != null && !this.connection.getDontTrackOpenResources())
          this.connection.unregisterStatement(this); 
      } 
      return;
    } 
  }
  
  public void realClose(boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    null = this.connection;
    if (null == null)
      return; 
    synchronized (null.getConnectionMutex()) {
      if (this.connection != null) {
        if (this.connection.getAutoGenerateTestcaseScript())
          dumpCloseForTestcase(); 
        if (paramBoolean1 && !this.connection.isClosed()) {
          Object object = this.connection.getConnectionMutex();
          /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          try {
            MysqlIO mysqlIO = this.connection.getIO();
            Buffer buffer = mysqlIO.getSharedSendPacket();
            buffer.writeByte((byte)25);
            buffer.writeLong(this.serverStatementId);
            mysqlIO.sendCommand(25, null, buffer, true, null, 0);
            buffer = null;
          } catch (SQLException sQLException) {
          
          } finally {}
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        } else {
          null = null;
        } 
        if (this.isCached) {
          this.connection.decachePreparedStatement(this);
          this.isCached = false;
        } 
        super.realClose(paramBoolean1, paramBoolean2);
        clearParametersInternal(false);
        this.parameterBindings = null;
        this.parameterFields = null;
        this.resultFields = null;
        if (null != null)
          throw null; 
      } 
      return;
    } 
  }
  
  public void resetToType(BindValue paramBindValue, int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      paramBindValue.reset();
      if ((paramInt != 6 || paramBindValue.bufferType == 0) && paramBindValue.bufferType != paramInt) {
        this.sendTypesToServer = true;
        paramBindValue.bufferType = paramInt;
      } 
      paramBindValue.isSet = true;
      paramBindValue.boundBeforeExecutionNum = this.numberOfExecutions;
      return;
    } 
  }
  
  public void setArray(int paramInt, Array paramArray) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 28
    //   18: aload_0
    //   19: iload_1
    //   20: bipush #-2
    //   22: invokevirtual setNull : (II)V
    //   25: goto -> 87
    //   28: aload_0
    //   29: iload_1
    //   30: iconst_1
    //   31: invokevirtual getBinding : (IZ)Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   34: astore #5
    //   36: aload_0
    //   37: aload #5
    //   39: sipush #252
    //   42: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   45: aload #5
    //   47: aload_2
    //   48: putfield value : Ljava/lang/Object;
    //   51: aload #5
    //   53: iconst_1
    //   54: putfield isLongData : Z
    //   57: aload_0
    //   58: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   61: invokeinterface getUseStreamLengthsInPrepStmts : ()Z
    //   66: ifeq -> 79
    //   69: aload #5
    //   71: iload_3
    //   72: i2l
    //   73: putfield bindLength : J
    //   76: goto -> 87
    //   79: aload #5
    //   81: ldc2_w -1
    //   84: putfield bindLength : J
    //   87: aload #4
    //   89: monitorexit
    //   90: return
    //   91: astore_2
    //   92: aload #4
    //   94: monitorexit
    //   95: aload_2
    //   96: athrow
    // Exception table:
    //   from	to	target	type
    //   18	25	91	finally
    //   28	76	91	finally
    //   79	87	91	finally
    //   87	90	91	finally
    //   92	95	91	finally
  }
  
  public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_2
    //   13: ifnonnull -> 25
    //   16: aload_0
    //   17: iload_1
    //   18: iconst_3
    //   19: invokevirtual setNull : (II)V
    //   22: goto -> 82
    //   25: aload_0
    //   26: iload_1
    //   27: iconst_0
    //   28: invokevirtual getBinding : (IZ)Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   31: astore #4
    //   33: aload_0
    //   34: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   37: iconst_5
    //   38: iconst_0
    //   39: iconst_3
    //   40: invokeinterface versionMeetsMinimum : (III)Z
    //   45: ifeq -> 60
    //   48: aload_0
    //   49: aload #4
    //   51: sipush #246
    //   54: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   57: goto -> 70
    //   60: aload_0
    //   61: aload #4
    //   63: aload_0
    //   64: getfield stringTypeCode : I
    //   67: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   70: aload #4
    //   72: aload_2
    //   73: invokestatic consistentToString : (Ljava/math/BigDecimal;)Ljava/lang/String;
    //   76: invokestatic fixDecimalExponent : (Ljava/lang/String;)Ljava/lang/String;
    //   79: putfield value : Ljava/lang/Object;
    //   82: aload_3
    //   83: monitorexit
    //   84: return
    //   85: astore_2
    //   86: aload_3
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   16	22	85	finally
    //   25	57	85	finally
    //   60	70	85	finally
    //   70	82	85	finally
    //   82	84	85	finally
    //   86	88	85	finally
  }
  
  public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 28
    //   18: aload_0
    //   19: iload_1
    //   20: bipush #-2
    //   22: invokevirtual setNull : (II)V
    //   25: goto -> 87
    //   28: aload_0
    //   29: iload_1
    //   30: iconst_1
    //   31: invokevirtual getBinding : (IZ)Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   34: astore #5
    //   36: aload_0
    //   37: aload #5
    //   39: sipush #252
    //   42: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   45: aload #5
    //   47: aload_2
    //   48: putfield value : Ljava/lang/Object;
    //   51: aload #5
    //   53: iconst_1
    //   54: putfield isLongData : Z
    //   57: aload_0
    //   58: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   61: invokeinterface getUseStreamLengthsInPrepStmts : ()Z
    //   66: ifeq -> 79
    //   69: aload #5
    //   71: iload_3
    //   72: i2l
    //   73: putfield bindLength : J
    //   76: goto -> 87
    //   79: aload #5
    //   81: ldc2_w -1
    //   84: putfield bindLength : J
    //   87: aload #4
    //   89: monitorexit
    //   90: return
    //   91: astore_2
    //   92: aload #4
    //   94: monitorexit
    //   95: aload_2
    //   96: athrow
    // Exception table:
    //   from	to	target	type
    //   18	25	91	finally
    //   28	76	91	finally
    //   79	87	91	finally
    //   87	90	91	finally
    //   92	95	91	finally
  }
  
  public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_2
    //   13: ifnonnull -> 26
    //   16: aload_0
    //   17: iload_1
    //   18: bipush #-2
    //   20: invokevirtual setNull : (II)V
    //   23: goto -> 89
    //   26: aload_0
    //   27: iload_1
    //   28: iconst_1
    //   29: invokevirtual getBinding : (IZ)Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   32: astore #4
    //   34: aload_0
    //   35: aload #4
    //   37: sipush #252
    //   40: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   43: aload #4
    //   45: aload_2
    //   46: putfield value : Ljava/lang/Object;
    //   49: aload #4
    //   51: iconst_1
    //   52: putfield isLongData : Z
    //   55: aload_0
    //   56: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   59: invokeinterface getUseStreamLengthsInPrepStmts : ()Z
    //   64: ifeq -> 81
    //   67: aload #4
    //   69: aload_2
    //   70: invokeinterface length : ()J
    //   75: putfield bindLength : J
    //   78: goto -> 89
    //   81: aload #4
    //   83: ldc2_w -1
    //   86: putfield bindLength : J
    //   89: aload_3
    //   90: monitorexit
    //   91: return
    //   92: astore_2
    //   93: aload_3
    //   94: monitorexit
    //   95: aload_2
    //   96: athrow
    // Exception table:
    //   from	to	target	type
    //   16	23	92	finally
    //   26	78	92	finally
    //   81	89	92	finally
    //   89	91	92	finally
    //   93	95	92	finally
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
    setByte(paramInt, paramBoolean);
  }
  
  public void setByte(int paramInt, byte paramByte) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt, false);
    resetToType(bindValue, 1);
    bindValue.longBinding = paramByte;
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    checkClosed();
    if (paramArrayOfbyte == null) {
      setNull(paramInt, -2);
    } else {
      BindValue bindValue = getBinding(paramInt, false);
      resetToType(bindValue, 253);
      bindValue.value = paramArrayOfbyte;
    } 
  }
  
  public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 28
    //   18: aload_0
    //   19: iload_1
    //   20: bipush #-2
    //   22: invokevirtual setNull : (II)V
    //   25: goto -> 87
    //   28: aload_0
    //   29: iload_1
    //   30: iconst_1
    //   31: invokevirtual getBinding : (IZ)Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   34: astore #5
    //   36: aload_0
    //   37: aload #5
    //   39: sipush #252
    //   42: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   45: aload #5
    //   47: aload_2
    //   48: putfield value : Ljava/lang/Object;
    //   51: aload #5
    //   53: iconst_1
    //   54: putfield isLongData : Z
    //   57: aload_0
    //   58: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   61: invokeinterface getUseStreamLengthsInPrepStmts : ()Z
    //   66: ifeq -> 79
    //   69: aload #5
    //   71: iload_3
    //   72: i2l
    //   73: putfield bindLength : J
    //   76: goto -> 87
    //   79: aload #5
    //   81: ldc2_w -1
    //   84: putfield bindLength : J
    //   87: aload #4
    //   89: monitorexit
    //   90: return
    //   91: astore_2
    //   92: aload #4
    //   94: monitorexit
    //   95: aload_2
    //   96: athrow
    // Exception table:
    //   from	to	target	type
    //   18	25	91	finally
    //   28	76	91	finally
    //   79	87	91	finally
    //   87	90	91	finally
    //   92	95	91	finally
  }
  
  public void setClob(int paramInt, Clob paramClob) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_2
    //   13: ifnonnull -> 26
    //   16: aload_0
    //   17: iload_1
    //   18: bipush #-2
    //   20: invokevirtual setNull : (II)V
    //   23: goto -> 94
    //   26: aload_0
    //   27: iload_1
    //   28: iconst_1
    //   29: invokevirtual getBinding : (IZ)Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;
    //   32: astore #4
    //   34: aload_0
    //   35: aload #4
    //   37: sipush #252
    //   40: invokevirtual resetToType : (Lcom/mysql/jdbc/ServerPreparedStatement$BindValue;I)V
    //   43: aload #4
    //   45: aload_2
    //   46: invokeinterface getCharacterStream : ()Ljava/io/Reader;
    //   51: putfield value : Ljava/lang/Object;
    //   54: aload #4
    //   56: iconst_1
    //   57: putfield isLongData : Z
    //   60: aload_0
    //   61: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   64: invokeinterface getUseStreamLengthsInPrepStmts : ()Z
    //   69: ifeq -> 86
    //   72: aload #4
    //   74: aload_2
    //   75: invokeinterface length : ()J
    //   80: putfield bindLength : J
    //   83: goto -> 94
    //   86: aload #4
    //   88: ldc2_w -1
    //   91: putfield bindLength : J
    //   94: aload_3
    //   95: monitorexit
    //   96: return
    //   97: astore_2
    //   98: aload_3
    //   99: monitorexit
    //   100: aload_2
    //   101: athrow
    // Exception table:
    //   from	to	target	type
    //   16	23	97	finally
    //   26	83	97	finally
    //   86	94	97	finally
    //   94	96	97	finally
    //   98	100	97	finally
  }
  
  public void setClosed(boolean paramBoolean) {
    this.isClosed = paramBoolean;
  }
  
  public void setDate(int paramInt, Date paramDate) throws SQLException {
    setDate(paramInt, paramDate, null);
  }
  
  public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
    if (paramDate == null) {
      setNull(paramInt, 91);
    } else {
      BindValue bindValue = getBinding(paramInt, false);
      resetToType(bindValue, 10);
      bindValue.value = paramDate;
      if (paramCalendar != null)
        bindValue.calendar = (Calendar)paramCalendar.clone(); 
    } 
  }
  
  public void setDouble(int paramInt, double paramDouble) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.connection.getAllowNanAndInf() || (paramDouble != Double.POSITIVE_INFINITY && paramDouble != Double.NEGATIVE_INFINITY && !Double.isNaN(paramDouble))) {
        BindValue bindValue = getBinding(paramInt, false);
        resetToType(bindValue, 5);
        bindValue.doubleBinding = paramDouble;
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("'");
      stringBuilder.append(paramDouble);
      stringBuilder.append("' is not a valid numeric or approximate numeric value");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public void setFloat(int paramInt, float paramFloat) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt, false);
    resetToType(bindValue, 4);
    bindValue.floatBinding = paramFloat;
  }
  
  public void setInt(int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt1, false);
    resetToType(bindValue, 3);
    bindValue.longBinding = paramInt2;
  }
  
  public void setLong(int paramInt, long paramLong) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt, false);
    resetToType(bindValue, 8);
    bindValue.longBinding = paramLong;
  }
  
  public void setNull(int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt1, false);
    resetToType(bindValue, 6);
    bindValue.isNull = true;
  }
  
  public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt1, false);
    resetToType(bindValue, 6);
    bindValue.isNull = true;
  }
  
  public int setOneBatchedParameterSet(PreparedStatement paramPreparedStatement, int paramInt, Object paramObject) throws SQLException {
    paramObject = ((BatchedBindValues)paramObject).batchedParameterValues;
    for (byte b = 0; b < paramObject.length; b++) {
      StringBuilder stringBuilder;
      if (((BindValue)paramObject[b]).isNull) {
        int j = paramInt + 1;
        paramPreparedStatement.setNull(paramInt, 0);
        paramInt = j;
        continue;
      } 
      if (((BindValue)paramObject[b]).isLongData) {
        Object object1 = ((BindValue)paramObject[b]).value;
        if (object1 instanceof InputStream) {
          int j = paramInt + 1;
          paramPreparedStatement.setBinaryStream(paramInt, (InputStream)object1, (int)((BindValue)paramObject[b]).bindLength);
          paramInt = j;
        } else {
          int j = paramInt + 1;
          paramPreparedStatement.setCharacterStream(paramInt, (Reader)object1, (int)((BindValue)paramObject[b]).bindLength);
          paramInt = j;
        } 
        continue;
      } 
      int i = ((BindValue)paramObject[b]).bufferType;
      if (i != 0)
        if (i != 1) {
          if (i != 2) {
            if (i != 3) {
              if (i != 4) {
                if (i != 5) {
                  if (i != 7) {
                    if (i != 8) {
                      if (i != 15 && i != 246 && i != 253 && i != 254) {
                        switch (i) {
                          default:
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Unknown type when re-binding parameter into batched statement for parameter index ");
                            stringBuilder.append(paramInt);
                            throw new IllegalArgumentException(stringBuilder.toString());
                          case 11:
                            i = paramInt + 1;
                            stringBuilder.setTime(paramInt, (Time)((BindValue)paramObject[b]).value);
                            paramInt = i;
                            break;
                          case 10:
                            i = paramInt + 1;
                            stringBuilder.setDate(paramInt, (Date)((BindValue)paramObject[b]).value);
                            paramInt = i;
                            break;
                          case 12:
                            i = paramInt + 1;
                            stringBuilder.setTimestamp(paramInt, (Timestamp)((BindValue)paramObject[b]).value);
                            paramInt = i;
                            break;
                        } 
                        continue;
                      } 
                    } else {
                      i = paramInt + 1;
                      stringBuilder.setLong(paramInt, ((BindValue)paramObject[b]).longBinding);
                      paramInt = i;
                      continue;
                    } 
                  } else {
                  
                  } 
                } else {
                  i = paramInt + 1;
                  stringBuilder.setDouble(paramInt, ((BindValue)paramObject[b]).doubleBinding);
                  paramInt = i;
                  continue;
                } 
              } else {
                i = paramInt + 1;
                stringBuilder.setFloat(paramInt, ((BindValue)paramObject[b]).floatBinding);
                paramInt = i;
                continue;
              } 
            } else {
              i = paramInt + 1;
              stringBuilder.setInt(paramInt, (int)((BindValue)paramObject[b]).longBinding);
              paramInt = i;
              continue;
            } 
          } else {
            i = paramInt + 1;
            stringBuilder.setShort(paramInt, (short)(int)((BindValue)paramObject[b]).longBinding);
            paramInt = i;
            continue;
          } 
        } else {
          i = paramInt + 1;
          stringBuilder.setByte(paramInt, (byte)(int)((BindValue)paramObject[b]).longBinding);
          paramInt = i;
          continue;
        }  
      Object object = ((BindValue)paramObject[b]).value;
      if (object instanceof byte[]) {
        stringBuilder.setBytes(paramInt, (byte[])object);
      } else {
        stringBuilder.setString(paramInt, (String)object);
      } 
      if (stringBuilder instanceof ServerPreparedStatement)
        (((ServerPreparedStatement)stringBuilder).getBinding(paramInt, false)).bufferType = ((BindValue)paramObject[b]).bufferType; 
      paramInt++;
      continue;
    } 
    return paramInt;
  }
  
  public void setPoolable(boolean paramBoolean) throws SQLException {
    if (!paramBoolean)
      this.connection.decachePreparedStatement(this); 
    super.setPoolable(paramBoolean);
  }
  
  public void setRef(int paramInt, Ref paramRef) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void setShort(int paramInt, short paramShort) throws SQLException {
    checkClosed();
    BindValue bindValue = getBinding(paramInt, false);
    resetToType(bindValue, 2);
    bindValue.longBinding = paramShort;
  }
  
  public void setString(int paramInt, String paramString) throws SQLException {
    checkClosed();
    if (paramString == null) {
      setNull(paramInt, 1);
    } else {
      BindValue bindValue = getBinding(paramInt, false);
      resetToType(bindValue, this.stringTypeCode);
      bindValue.value = paramString;
    } 
  }
  
  public void setTime(int paramInt, Time paramTime) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setTimeInternal(paramInt, paramTime, null, this.connection.getDefaultTimeZone(), false);
      return;
    } 
  }
  
  public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setTimeInternal(paramInt, paramTime, paramCalendar, paramCalendar.getTimeZone(), true);
      return;
    } 
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setTimestampInternal(paramInt, paramTimestamp, null, this.connection.getDefaultTimeZone(), false);
      return;
    } 
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setTimestampInternal(paramInt, paramTimestamp, paramCalendar, paramCalendar.getTimeZone(), true);
      return;
    } 
  }
  
  public void setURL(int paramInt, URL paramURL) throws SQLException {
    checkClosed();
    setString(paramInt, paramURL.toString());
  }
  
  @Deprecated
  public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    checkClosed();
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("com.mysql.jdbc.ServerPreparedStatement[");
    stringBuilder.append(this.serverStatementId);
    stringBuilder.append("] - ");
    try {
      stringBuilder.append(asSql());
    } catch (SQLException sQLException) {
      stringBuilder.append(Messages.getString("ServerPreparedStatement.6"));
      stringBuilder.append(sQLException);
    } 
    return stringBuilder.toString();
  }
  
  public static class BatchedBindValues {
    public ServerPreparedStatement.BindValue[] batchedParameterValues;
    
    public BatchedBindValues(ServerPreparedStatement.BindValue[] param1ArrayOfBindValue) {
      int i = param1ArrayOfBindValue.length;
      this.batchedParameterValues = new ServerPreparedStatement.BindValue[i];
      for (byte b = 0; b < i; b++)
        this.batchedParameterValues[b] = new ServerPreparedStatement.BindValue(param1ArrayOfBindValue[b]); 
    }
  }
  
  public static class BindValue {
    public long bindLength;
    
    public long boundBeforeExecutionNum = 0L;
    
    public int bufferType;
    
    public Calendar calendar;
    
    public double doubleBinding;
    
    public float floatBinding;
    
    public boolean isLongData;
    
    public boolean isNull;
    
    public boolean isSet = false;
    
    public long longBinding;
    
    public Object value;
    
    public BindValue() {}
    
    public BindValue(BindValue param1BindValue) {
      this.value = param1BindValue.value;
      this.isSet = param1BindValue.isSet;
      this.isLongData = param1BindValue.isLongData;
      this.isNull = param1BindValue.isNull;
      this.bufferType = param1BindValue.bufferType;
      this.bindLength = param1BindValue.bindLength;
      this.longBinding = param1BindValue.longBinding;
      this.floatBinding = param1BindValue.floatBinding;
      this.doubleBinding = param1BindValue.doubleBinding;
      this.calendar = param1BindValue.calendar;
    }
    
    public long getBoundLength() {
      if (this.isNull)
        return 0L; 
      if (this.isLongData)
        return this.bindLength; 
      int i = this.bufferType;
      if (i != 0)
        if (i != 1) {
          if (i != 2) {
            long l2 = 4L;
            long l1 = l2;
            if (i != 3) {
              l1 = l2;
              if (i != 4) {
                l1 = 8L;
                if (i != 5) {
                  if (i != 7) {
                    if (i != 8) {
                      if (i != 15 && i != 246 && i != 253 && i != 254) {
                        switch (i) {
                          default:
                            return 0L;
                          case 11:
                            return 9L;
                          case 10:
                            return 7L;
                          case 12:
                            break;
                        } 
                        return 11L;
                      } 
                    } else {
                      return 8L;
                    } 
                  } else {
                    return 11L;
                  } 
                } else {
                  return l1;
                } 
              } else {
                return l1;
              } 
            } else {
              return l1;
            } 
          } else {
            return 2L;
          } 
        } else {
          return 1L;
        }  
      Object object = this.value;
      if (object instanceof byte[]) {
        i = ((byte[])object).length;
        return i;
      } 
      i = ((String)object).length();
      return i;
    }
    
    public void reset() {
      this.isNull = false;
      this.isSet = false;
      this.value = null;
      this.isLongData = false;
      this.longBinding = 0L;
      this.floatBinding = 0.0F;
      this.doubleBinding = 0.0D;
      this.calendar = null;
    }
    
    public String toString() {
      return toString(false);
    }
    
    public String toString(boolean param1Boolean) {
      if (this.isLongData)
        return "' STREAM DATA '"; 
      if (this.isNull)
        return "NULL"; 
      int i = this.bufferType;
      if (i != 1 && i != 2 && i != 3) {
        if (i != 4) {
          if (i != 5) {
            if (i != 7)
              if (i != 8) {
                if (i != 15 && i != 253 && i != 254) {
                  Object object;
                  switch (i) {
                    default:
                      object = this.value;
                      if (object instanceof byte[])
                        return "byte data"; 
                      if (param1Boolean) {
                        object = new StringBuilder();
                        object.append("'");
                        object.append(String.valueOf(this.value));
                        object.append("'");
                        return object.toString();
                      } 
                      return String.valueOf(object);
                    case 10:
                    case 11:
                    case 12:
                      break;
                  } 
                } 
              } else {
                return String.valueOf(this.longBinding);
              }  
            if (param1Boolean) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("'");
              stringBuilder.append(String.valueOf(this.value));
              stringBuilder.append("'");
              return stringBuilder.toString();
            } 
            return String.valueOf(this.value);
          } 
          return String.valueOf(this.doubleBinding);
        } 
        return String.valueOf(this.floatBinding);
      } 
      return String.valueOf(this.longBinding);
    }
  }
}
