package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

public class JDBC4ClientInfoProviderSP implements JDBC4ClientInfoProvider {
  public PreparedStatement getClientInfoBulkSp;
  
  public PreparedStatement getClientInfoSp;
  
  public PreparedStatement setClientInfoSp;
  
  public void destroy() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield setClientInfoSp : Ljava/sql/PreparedStatement;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 22
    //   11: aload_1
    //   12: invokeinterface close : ()V
    //   17: aload_0
    //   18: aconst_null
    //   19: putfield setClientInfoSp : Ljava/sql/PreparedStatement;
    //   22: aload_0
    //   23: getfield getClientInfoSp : Ljava/sql/PreparedStatement;
    //   26: astore_1
    //   27: aload_1
    //   28: ifnull -> 42
    //   31: aload_1
    //   32: invokeinterface close : ()V
    //   37: aload_0
    //   38: aconst_null
    //   39: putfield getClientInfoSp : Ljava/sql/PreparedStatement;
    //   42: aload_0
    //   43: getfield getClientInfoBulkSp : Ljava/sql/PreparedStatement;
    //   46: astore_1
    //   47: aload_1
    //   48: ifnull -> 62
    //   51: aload_1
    //   52: invokeinterface close : ()V
    //   57: aload_0
    //   58: aconst_null
    //   59: putfield getClientInfoBulkSp : Ljava/sql/PreparedStatement;
    //   62: aload_0
    //   63: monitorexit
    //   64: return
    //   65: astore_1
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_1
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	65	finally
    //   11	22	65	finally
    //   22	27	65	finally
    //   31	42	65	finally
    //   42	47	65	finally
    //   51	62	65	finally
  }
  
  public String getClientInfo(Connection paramConnection, String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_1
    //   4: aload_0
    //   5: getfield getClientInfoSp : Ljava/sql/PreparedStatement;
    //   8: iconst_1
    //   9: aload_2
    //   10: invokeinterface setString : (ILjava/lang/String;)V
    //   15: aload_0
    //   16: getfield getClientInfoSp : Ljava/sql/PreparedStatement;
    //   19: invokeinterface execute : ()Z
    //   24: pop
    //   25: aload_0
    //   26: getfield getClientInfoSp : Ljava/sql/PreparedStatement;
    //   29: invokeinterface getResultSet : ()Ljava/sql/ResultSet;
    //   34: astore_2
    //   35: aload_2
    //   36: invokeinterface next : ()Z
    //   41: ifeq -> 52
    //   44: aload_2
    //   45: iconst_1
    //   46: invokeinterface getString : (I)Ljava/lang/String;
    //   51: astore_1
    //   52: aload_2
    //   53: ifnull -> 62
    //   56: aload_2
    //   57: invokeinterface close : ()V
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_1
    //   65: areturn
    //   66: astore_1
    //   67: goto -> 73
    //   70: astore_1
    //   71: aconst_null
    //   72: astore_2
    //   73: aload_2
    //   74: ifnull -> 83
    //   77: aload_2
    //   78: invokeinterface close : ()V
    //   83: aload_1
    //   84: athrow
    //   85: astore_1
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_1
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   4	35	70	finally
    //   35	52	66	finally
    //   56	62	85	finally
    //   77	83	85	finally
    //   83	85	85	finally
  }
  
  public Properties getClientInfo(Connection paramConnection) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_2
    //   4: new java/util/Properties
    //   7: astore_3
    //   8: aload_3
    //   9: invokespecial <init> : ()V
    //   12: aload_2
    //   13: astore_1
    //   14: aload_0
    //   15: getfield getClientInfoBulkSp : Ljava/sql/PreparedStatement;
    //   18: invokeinterface execute : ()Z
    //   23: pop
    //   24: aload_2
    //   25: astore_1
    //   26: aload_0
    //   27: getfield getClientInfoBulkSp : Ljava/sql/PreparedStatement;
    //   30: invokeinterface getResultSet : ()Ljava/sql/ResultSet;
    //   35: astore_2
    //   36: aload_2
    //   37: astore_1
    //   38: aload_2
    //   39: invokeinterface next : ()Z
    //   44: ifeq -> 71
    //   47: aload_2
    //   48: astore_1
    //   49: aload_3
    //   50: aload_2
    //   51: iconst_1
    //   52: invokeinterface getString : (I)Ljava/lang/String;
    //   57: aload_2
    //   58: iconst_2
    //   59: invokeinterface getString : (I)Ljava/lang/String;
    //   64: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   67: pop
    //   68: goto -> 36
    //   71: aload_2
    //   72: ifnull -> 81
    //   75: aload_2
    //   76: invokeinterface close : ()V
    //   81: aload_0
    //   82: monitorexit
    //   83: aload_3
    //   84: areturn
    //   85: astore_2
    //   86: aload_1
    //   87: ifnull -> 96
    //   90: aload_1
    //   91: invokeinterface close : ()V
    //   96: aload_2
    //   97: athrow
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   4	12	98	finally
    //   14	24	85	finally
    //   26	36	85	finally
    //   38	47	85	finally
    //   49	68	85	finally
    //   75	81	98	finally
    //   90	96	98	finally
    //   96	98	98	finally
  }
  
  public void initialize(Connection paramConnection, Properties paramProperties) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokeinterface getMetaData : ()Ljava/sql/DatabaseMetaData;
    //   8: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
    //   13: astore #4
    //   15: aload_2
    //   16: ldc 'clientInfoSetSPName'
    //   18: ldc 'setClientInfo'
    //   20: invokevirtual getProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   23: astore #7
    //   25: aload_2
    //   26: ldc 'clientInfoGetSPName'
    //   28: ldc 'getClientInfo'
    //   30: invokevirtual getProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   33: astore #6
    //   35: aload_2
    //   36: ldc 'clientInfoGetBulkSPName'
    //   38: ldc 'getClientInfoBulk'
    //   40: invokevirtual getProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   43: astore #5
    //   45: aload_2
    //   46: ldc 'clientInfoCatalog'
    //   48: ldc ''
    //   50: invokevirtual getProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   53: astore_3
    //   54: aload_3
    //   55: astore_2
    //   56: ldc ''
    //   58: aload_3
    //   59: invokevirtual equals : (Ljava/lang/Object;)Z
    //   62: ifeq -> 72
    //   65: aload_1
    //   66: invokeinterface getCatalog : ()Ljava/lang/String;
    //   71: astore_2
    //   72: aload_1
    //   73: checkcast com/mysql/jdbc/Connection
    //   76: astore #8
    //   78: new java/lang/StringBuilder
    //   81: astore_3
    //   82: aload_3
    //   83: invokespecial <init> : ()V
    //   86: aload_3
    //   87: ldc 'CALL '
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: aload_3
    //   94: aload #4
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_3
    //   101: aload_2
    //   102: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload_3
    //   107: aload #4
    //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload_3
    //   114: ldc '.'
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: aload_3
    //   121: aload #4
    //   123: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   126: pop
    //   127: aload_3
    //   128: aload #7
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload_3
    //   135: aload #4
    //   137: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: pop
    //   141: aload_3
    //   142: ldc '(?, ?)'
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: aload_0
    //   149: aload #8
    //   151: aload_3
    //   152: invokevirtual toString : ()Ljava/lang/String;
    //   155: invokeinterface clientPrepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   160: putfield setClientInfoSp : Ljava/sql/PreparedStatement;
    //   163: aload_1
    //   164: checkcast com/mysql/jdbc/Connection
    //   167: astore #7
    //   169: new java/lang/StringBuilder
    //   172: astore_3
    //   173: aload_3
    //   174: invokespecial <init> : ()V
    //   177: aload_3
    //   178: ldc 'CALL'
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: pop
    //   184: aload_3
    //   185: aload #4
    //   187: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: pop
    //   191: aload_3
    //   192: aload_2
    //   193: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: pop
    //   197: aload_3
    //   198: aload #4
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_3
    //   205: ldc '.'
    //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload_3
    //   212: aload #4
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload_3
    //   219: aload #6
    //   221: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload_3
    //   226: aload #4
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload_3
    //   233: ldc '(?)'
    //   235: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: pop
    //   239: aload_0
    //   240: aload #7
    //   242: aload_3
    //   243: invokevirtual toString : ()Ljava/lang/String;
    //   246: invokeinterface clientPrepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   251: putfield getClientInfoSp : Ljava/sql/PreparedStatement;
    //   254: aload_1
    //   255: checkcast com/mysql/jdbc/Connection
    //   258: astore_3
    //   259: new java/lang/StringBuilder
    //   262: astore_1
    //   263: aload_1
    //   264: invokespecial <init> : ()V
    //   267: aload_1
    //   268: ldc 'CALL '
    //   270: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: pop
    //   274: aload_1
    //   275: aload #4
    //   277: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   280: pop
    //   281: aload_1
    //   282: aload_2
    //   283: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: pop
    //   287: aload_1
    //   288: aload #4
    //   290: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: pop
    //   294: aload_1
    //   295: ldc '.'
    //   297: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   300: pop
    //   301: aload_1
    //   302: aload #4
    //   304: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   307: pop
    //   308: aload_1
    //   309: aload #5
    //   311: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   314: pop
    //   315: aload_1
    //   316: aload #4
    //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: pop
    //   322: aload_1
    //   323: ldc '()'
    //   325: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: pop
    //   329: aload_0
    //   330: aload_3
    //   331: aload_1
    //   332: invokevirtual toString : ()Ljava/lang/String;
    //   335: invokeinterface clientPrepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   340: putfield getClientInfoBulkSp : Ljava/sql/PreparedStatement;
    //   343: aload_0
    //   344: monitorexit
    //   345: return
    //   346: astore_1
    //   347: aload_0
    //   348: monitorexit
    //   349: aload_1
    //   350: athrow
    // Exception table:
    //   from	to	target	type
    //   2	54	346	finally
    //   56	72	346	finally
    //   72	343	346	finally
  }
  
  public void setClientInfo(Connection paramConnection, String paramString1, String paramString2) throws SQLClientInfoException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/JDBC4ClientInfoProviderSP}} */
    try {
      this.setClientInfoSp.setString(1, paramString1);
      this.setClientInfoSp.setString(2, paramString2);
      this.setClientInfoSp.execute();
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/JDBC4ClientInfoProviderSP}} */
      return;
    } catch (SQLException sQLException) {
      SQLClientInfoException sQLClientInfoException = new SQLClientInfoException();
      this();
      sQLClientInfoException.initCause(sQLException);
      throw sQLClientInfoException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/JDBC4ClientInfoProviderSP}} */
    throw paramConnection;
  }
  
  public void setClientInfo(Connection paramConnection, Properties paramProperties) throws SQLClientInfoException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/JDBC4ClientInfoProviderSP}} */
    try {
      Enumeration<?> enumeration = paramProperties.propertyNames();
      while (enumeration.hasMoreElements()) {
        String str = (String)enumeration.nextElement();
        setClientInfo(paramConnection, str, paramProperties.getProperty(str));
      } 
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/JDBC4ClientInfoProviderSP}} */
      return;
    } catch (SQLException sQLException) {
      SQLClientInfoException sQLClientInfoException = new SQLClientInfoException();
      this();
      sQLClientInfoException.initCause(sQLException);
      throw sQLClientInfoException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/JDBC4ClientInfoProviderSP}} */
    throw paramConnection;
  }
}
