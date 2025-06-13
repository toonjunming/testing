package com.mysql.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

public class FailoverConnectionProxy extends MultiHostConnectionProxy {
  private static final int DEFAULT_PRIMARY_HOST_INDEX = 0;
  
  private static Class<?>[] INTERFACES_TO_PROXY;
  
  private static final String METHOD_COMMIT = "commit";
  
  private static final String METHOD_ROLLBACK = "rollback";
  
  private static final String METHOD_SET_AUTO_COMMIT = "setAutoCommit";
  
  private static final String METHOD_SET_READ_ONLY = "setReadOnly";
  
  private static final int NO_CONNECTION_INDEX = -1;
  
  private int currentHostIndex = -1;
  
  private boolean enableFallBackToPrimaryHost;
  
  private boolean explicitlyAutoCommit;
  
  private Boolean explicitlyReadOnly;
  
  private boolean failoverReadOnly;
  
  private long primaryHostFailTimeMillis;
  
  private int primaryHostIndex;
  
  private long queriesBeforeRetryPrimaryHost;
  
  private long queriesIssuedSinceFailover;
  
  private int retriesAllDown;
  
  private int secondsBeforeRetryPrimaryHost;
  
  static {
    if (Util.isJdbc4()) {
      try {
        INTERFACES_TO_PROXY = new Class[] { Class.forName("com.mysql.jdbc.JDBC4MySQLConnection") };
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      INTERFACES_TO_PROXY = new Class[] { MySQLConnection.class };
    } 
  }
  
  private FailoverConnectionProxy(List<String> paramList, Properties paramProperties) throws SQLException {
    super(paramList, paramProperties);
    boolean bool = false;
    this.primaryHostIndex = 0;
    this.explicitlyReadOnly = null;
    this.explicitlyAutoCommit = true;
    this.enableFallBackToPrimaryHost = true;
    this.primaryHostFailTimeMillis = 0L;
    this.queriesIssuedSinceFailover = 0L;
    ConnectionPropertiesImpl connectionPropertiesImpl = new ConnectionPropertiesImpl();
    connectionPropertiesImpl.initializeProperties(paramProperties);
    this.secondsBeforeRetryPrimaryHost = connectionPropertiesImpl.getSecondsBeforeRetryMaster();
    this.queriesBeforeRetryPrimaryHost = connectionPropertiesImpl.getQueriesBeforeRetryMaster();
    this.failoverReadOnly = connectionPropertiesImpl.getFailOverReadOnly();
    this.retriesAllDown = connectionPropertiesImpl.getRetriesAllDown();
    if (this.secondsBeforeRetryPrimaryHost > 0 || this.queriesBeforeRetryPrimaryHost > 0L)
      bool = true; 
    this.enableFallBackToPrimaryHost = bool;
    pickNewConnection();
    this.explicitlyAutoCommit = this.currentConnection.getAutoCommit();
  }
  
  private void connectTo(int paramInt) throws SQLException {
    Exception exception;
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/FailoverConnectionProxy}} */
    try {
      switchCurrentConnectionTo(paramInt, createConnectionForHostIndex(paramInt));
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/FailoverConnectionProxy}} */
      return;
    } catch (SQLException sQLException) {
      if (this.currentConnection != null) {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        this("Connection to ");
        if (isPrimaryHostIndex(paramInt)) {
          str = "primary";
        } else {
          str = "secondary";
        } 
        stringBuilder.append(str);
        stringBuilder.append(" host '");
        stringBuilder.append(this.hostList.get(paramInt));
        stringBuilder.append("' failed");
        this.currentConnection.getLog().logWarn(stringBuilder.toString(), sQLException);
      } 
      throw sQLException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/FailoverConnectionProxy}} */
    throw exception;
  }
  
  public static Connection createProxyInstance(List<String> paramList, Properties paramProperties) throws SQLException {
    FailoverConnectionProxy failoverConnectionProxy = new FailoverConnectionProxy(paramList, paramProperties);
    return (Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(), INTERFACES_TO_PROXY, failoverConnectionProxy);
  }
  
  private void failOver() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield currentHostIndex : I
    //   7: invokespecial failOver : (I)V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	13	finally
  }
  
  private void failOver(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentHostIndex : I
    //   6: istore_2
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: invokespecial nextHost : (IZ)I
    //   13: istore #8
    //   15: aconst_null
    //   16: astore #12
    //   18: iload_2
    //   19: iconst_m1
    //   20: if_icmpeq -> 43
    //   23: aload_0
    //   24: iload_2
    //   25: invokevirtual isPrimaryHostIndex : (I)Z
    //   28: istore #11
    //   30: iload #11
    //   32: ifeq -> 38
    //   35: goto -> 43
    //   38: iconst_0
    //   39: istore_1
    //   40: goto -> 45
    //   43: iconst_1
    //   44: istore_1
    //   45: iload #8
    //   47: istore_2
    //   48: iconst_0
    //   49: istore #5
    //   51: iconst_0
    //   52: istore #4
    //   54: iload_1
    //   55: ifne -> 81
    //   58: iload_1
    //   59: istore_3
    //   60: aload_0
    //   61: iload_2
    //   62: invokevirtual isPrimaryHostIndex : (I)Z
    //   65: ifeq -> 71
    //   68: goto -> 81
    //   71: iconst_0
    //   72: istore_1
    //   73: goto -> 83
    //   76: astore #12
    //   78: goto -> 123
    //   81: iconst_1
    //   82: istore_1
    //   83: iload_1
    //   84: istore_3
    //   85: aload_0
    //   86: iload_2
    //   87: invokespecial connectTo : (I)V
    //   90: iload_1
    //   91: ifeq -> 109
    //   94: iload_1
    //   95: istore_3
    //   96: aload_0
    //   97: invokevirtual connectedToSecondaryHost : ()Z
    //   100: ifeq -> 109
    //   103: iload_1
    //   104: istore_3
    //   105: aload_0
    //   106: invokespecial resetAutoFallBackCounters : ()V
    //   109: iconst_1
    //   110: istore #7
    //   112: aload #12
    //   114: astore #13
    //   116: iload #5
    //   118: istore #6
    //   120: goto -> 239
    //   123: aload_0
    //   124: aload #12
    //   126: invokevirtual shouldExceptionTriggerConnectionSwitch : (Ljava/lang/Throwable;)Z
    //   129: ifeq -> 278
    //   132: iload #5
    //   134: ifle -> 143
    //   137: iconst_1
    //   138: istore #11
    //   140: goto -> 146
    //   143: iconst_0
    //   144: istore #11
    //   146: aload_0
    //   147: iload_2
    //   148: iload #11
    //   150: invokespecial nextHost : (IZ)I
    //   153: istore #9
    //   155: iload #9
    //   157: iload #8
    //   159: if_icmpne -> 222
    //   162: aload_0
    //   163: iload_2
    //   164: iconst_1
    //   165: invokespecial nextHost : (IZ)I
    //   168: istore #10
    //   170: iload_3
    //   171: istore_1
    //   172: aload #12
    //   174: astore #13
    //   176: iload #10
    //   178: istore_2
    //   179: iload #5
    //   181: istore #6
    //   183: iload #4
    //   185: istore #7
    //   187: iload #9
    //   189: iload #10
    //   191: if_icmpne -> 239
    //   194: iload #5
    //   196: iconst_1
    //   197: iadd
    //   198: istore #6
    //   200: ldc2_w 250
    //   203: invokestatic sleep : (J)V
    //   206: iload_3
    //   207: istore_1
    //   208: aload #12
    //   210: astore #13
    //   212: iload #10
    //   214: istore_2
    //   215: iload #4
    //   217: istore #7
    //   219: goto -> 239
    //   222: iload #9
    //   224: istore_2
    //   225: iload #4
    //   227: istore #7
    //   229: iload #5
    //   231: istore #6
    //   233: aload #12
    //   235: astore #13
    //   237: iload_3
    //   238: istore_1
    //   239: aload_0
    //   240: getfield retriesAllDown : I
    //   243: istore_3
    //   244: iload #6
    //   246: iload_3
    //   247: if_icmpge -> 267
    //   250: aload #13
    //   252: astore #12
    //   254: iload #6
    //   256: istore #5
    //   258: iload #7
    //   260: istore #4
    //   262: iload #7
    //   264: ifeq -> 54
    //   267: iload #7
    //   269: ifeq -> 275
    //   272: aload_0
    //   273: monitorexit
    //   274: return
    //   275: aload #13
    //   277: athrow
    //   278: aload #12
    //   280: athrow
    //   281: astore #12
    //   283: aload_0
    //   284: monitorexit
    //   285: aload #12
    //   287: athrow
    //   288: astore #13
    //   290: iload_3
    //   291: istore_1
    //   292: aload #12
    //   294: astore #13
    //   296: iload #10
    //   298: istore_2
    //   299: iload #4
    //   301: istore #7
    //   303: goto -> 239
    // Exception table:
    //   from	to	target	type
    //   2	15	281	finally
    //   23	30	281	finally
    //   60	68	76	java/sql/SQLException
    //   60	68	281	finally
    //   85	90	76	java/sql/SQLException
    //   85	90	281	finally
    //   96	103	76	java/sql/SQLException
    //   96	103	281	finally
    //   105	109	76	java/sql/SQLException
    //   105	109	281	finally
    //   123	132	281	finally
    //   146	155	281	finally
    //   162	170	281	finally
    //   200	206	288	java/lang/InterruptedException
    //   200	206	281	finally
    //   239	244	281	finally
    //   275	278	281	finally
    //   278	281	281	finally
  }
  
  private int nextHost(int paramInt, boolean paramBoolean) {
    int i = (paramInt + 1) % this.hostList.size();
    paramInt = i;
    if (isPrimaryHostIndex(i)) {
      paramInt = i;
      if (isConnected()) {
        paramInt = i;
        if (!paramBoolean) {
          paramInt = i;
          if (this.enableFallBackToPrimaryHost) {
            paramInt = i;
            if (!readyToFallBackToPrimaryHost())
              paramInt = nextHost(i, paramBoolean); 
          } 
        } 
      } 
    } 
    return paramInt;
  }
  
  private boolean queriesBeforeRetryPrimaryHostIsMet() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield queriesBeforeRetryPrimaryHost : J
    //   6: lstore_2
    //   7: lload_2
    //   8: lconst_0
    //   9: lcmp
    //   10: ifle -> 31
    //   13: aload_0
    //   14: getfield queriesIssuedSinceFailover : J
    //   17: lstore #4
    //   19: lload #4
    //   21: lload_2
    //   22: lcmp
    //   23: iflt -> 31
    //   26: iconst_1
    //   27: istore_1
    //   28: goto -> 33
    //   31: iconst_0
    //   32: istore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: iload_1
    //   36: ireturn
    //   37: astore #6
    //   39: aload_0
    //   40: monitorexit
    //   41: aload #6
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	19	37	finally
  }
  
  private void resetAutoFallBackCounters() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokestatic currentTimeMillis : ()J
    //   6: putfield primaryHostFailTimeMillis : J
    //   9: aload_0
    //   10: lconst_0
    //   11: putfield queriesIssuedSinceFailover : J
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
  
  private boolean secondsBeforeRetryPrimaryHostIsMet() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield secondsBeforeRetryPrimaryHost : I
    //   6: ifle -> 35
    //   9: aload_0
    //   10: getfield primaryHostFailTimeMillis : J
    //   13: invokestatic secondsSinceMillis : (J)J
    //   16: lstore_2
    //   17: aload_0
    //   18: getfield secondsBeforeRetryPrimaryHost : I
    //   21: istore_1
    //   22: lload_2
    //   23: iload_1
    //   24: i2l
    //   25: lcmp
    //   26: iflt -> 35
    //   29: iconst_1
    //   30: istore #4
    //   32: goto -> 38
    //   35: iconst_0
    //   36: istore #4
    //   38: aload_0
    //   39: monitorexit
    //   40: iload #4
    //   42: ireturn
    //   43: astore #5
    //   45: aload_0
    //   46: monitorexit
    //   47: aload #5
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	43	finally
  }
  
  private void switchCurrentConnectionTo(int paramInt, MySQLConnection paramMySQLConnection) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual invalidateCurrentConnection : ()V
    //   6: aload_0
    //   7: iload_1
    //   8: invokevirtual isPrimaryHostIndex : (I)Z
    //   11: istore #4
    //   13: iconst_0
    //   14: istore_3
    //   15: iload #4
    //   17: ifeq -> 43
    //   20: aload_0
    //   21: getfield explicitlyReadOnly : Ljava/lang/Boolean;
    //   24: astore #5
    //   26: aload #5
    //   28: ifnonnull -> 34
    //   31: goto -> 94
    //   34: aload #5
    //   36: invokevirtual booleanValue : ()Z
    //   39: istore_3
    //   40: goto -> 94
    //   43: aload_0
    //   44: getfield failoverReadOnly : Z
    //   47: ifeq -> 55
    //   50: iconst_1
    //   51: istore_3
    //   52: goto -> 94
    //   55: aload_0
    //   56: getfield explicitlyReadOnly : Ljava/lang/Boolean;
    //   59: astore #5
    //   61: aload #5
    //   63: ifnull -> 75
    //   66: aload #5
    //   68: invokevirtual booleanValue : ()Z
    //   71: istore_3
    //   72: goto -> 94
    //   75: aload_0
    //   76: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   79: astore #5
    //   81: aload #5
    //   83: ifnull -> 94
    //   86: aload #5
    //   88: invokeinterface isReadOnly : ()Z
    //   93: istore_3
    //   94: aload_0
    //   95: aload_0
    //   96: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   99: aload_2
    //   100: iload_3
    //   101: invokevirtual syncSessionState : (Lcom/mysql/jdbc/Connection;Lcom/mysql/jdbc/Connection;Z)V
    //   104: aload_0
    //   105: aload_2
    //   106: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   109: aload_0
    //   110: iload_1
    //   111: putfield currentHostIndex : I
    //   114: aload_0
    //   115: monitorexit
    //   116: return
    //   117: astore_2
    //   118: aload_0
    //   119: monitorexit
    //   120: aload_2
    //   121: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	117	finally
    //   20	26	117	finally
    //   34	40	117	finally
    //   43	50	117	finally
    //   55	61	117	finally
    //   66	72	117	finally
    //   75	81	117	finally
    //   86	94	117	finally
    //   94	114	117	finally
  }
  
  public boolean connectedToPrimaryHost() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield currentHostIndex : I
    //   7: invokevirtual isPrimaryHostIndex : (I)Z
    //   10: istore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_1
    //   14: ireturn
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public boolean connectedToSecondaryHost() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentHostIndex : I
    //   6: istore_1
    //   7: iload_1
    //   8: iflt -> 26
    //   11: aload_0
    //   12: iload_1
    //   13: invokevirtual isPrimaryHostIndex : (I)Z
    //   16: istore_2
    //   17: iload_2
    //   18: ifne -> 26
    //   21: iconst_1
    //   22: istore_2
    //   23: goto -> 28
    //   26: iconst_0
    //   27: istore_2
    //   28: aload_0
    //   29: monitorexit
    //   30: iload_2
    //   31: ireturn
    //   32: astore_3
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_3
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	32	finally
    //   11	17	32	finally
  }
  
  public ConnectionImpl createConnectionForHostIndex(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield hostList : Ljava/util/List;
    //   7: iload_1
    //   8: invokeinterface get : (I)Ljava/lang/Object;
    //   13: checkcast java/lang/String
    //   16: invokevirtual createConnectionForHost : (Ljava/lang/String;)Lcom/mysql/jdbc/ConnectionImpl;
    //   19: astore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_2
    //   23: areturn
    //   24: astore_2
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_2
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public void doAbort(Executor paramExecutor) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   6: aload_1
    //   7: invokeinterface abort : (Ljava/util/concurrent/Executor;)V
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  public void doAbortInternal() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   6: invokeinterface abortInternal : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void doClose() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   6: invokeinterface close : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void fallBackToPrimaryIfAvailable() {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/FailoverConnectionProxy}} */
    ConnectionImpl connectionImpl = null;
    try {
      ConnectionImpl connectionImpl1 = createConnectionForHostIndex(this.primaryHostIndex);
      connectionImpl = connectionImpl1;
      switchCurrentConnectionTo(this.primaryHostIndex, connectionImpl1);
    } catch (SQLException sQLException) {
      if (connectionImpl != null)
        try {
          connectionImpl.close();
        } catch (SQLException sQLException1) {} 
      resetAutoFallBackCounters();
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/FailoverConnectionProxy}} */
  }
  
  public MultiHostConnectionProxy.JdbcInterfaceProxy getNewJdbcInterfaceProxy(Object paramObject) {
    return new FailoverJdbcInterfaceProxy(paramObject);
  }
  
  public void incrementQueriesIssuedSinceFailover() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield queriesIssuedSinceFailover : J
    //   7: lconst_1
    //   8: ladd
    //   9: putfield queriesIssuedSinceFailover : J
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  public Object invokeMore(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: invokevirtual getName : ()Ljava/lang/String;
    //   6: astore #6
    //   8: ldc 'setReadOnly'
    //   10: aload #6
    //   12: invokevirtual equals : (Ljava/lang/Object;)Z
    //   15: istore #4
    //   17: aconst_null
    //   18: astore_1
    //   19: iload #4
    //   21: ifeq -> 56
    //   24: aload_0
    //   25: aload_3
    //   26: iconst_0
    //   27: aaload
    //   28: checkcast java/lang/Boolean
    //   31: putfield explicitlyReadOnly : Ljava/lang/Boolean;
    //   34: aload_0
    //   35: getfield failoverReadOnly : Z
    //   38: ifeq -> 56
    //   41: aload_0
    //   42: invokevirtual connectedToSecondaryHost : ()Z
    //   45: istore #4
    //   47: iload #4
    //   49: ifeq -> 56
    //   52: aload_0
    //   53: monitorexit
    //   54: aconst_null
    //   55: areturn
    //   56: aload_0
    //   57: getfield isClosed : Z
    //   60: ifeq -> 165
    //   63: aload_0
    //   64: aload_2
    //   65: invokevirtual allowedOnClosedConnection : (Ljava/lang/reflect/Method;)Z
    //   68: ifne -> 165
    //   71: aload_0
    //   72: getfield autoReconnect : Z
    //   75: ifeq -> 107
    //   78: aload_0
    //   79: getfield closedExplicitly : Z
    //   82: ifne -> 107
    //   85: aload_0
    //   86: iconst_m1
    //   87: putfield currentHostIndex : I
    //   90: aload_0
    //   91: invokevirtual pickNewConnection : ()V
    //   94: aload_0
    //   95: iconst_0
    //   96: putfield isClosed : Z
    //   99: aload_0
    //   100: aconst_null
    //   101: putfield closedReason : Ljava/lang/String;
    //   104: goto -> 165
    //   107: ldc_w 'No operations allowed after connection closed.'
    //   110: astore_1
    //   111: aload_0
    //   112: getfield closedReason : Ljava/lang/String;
    //   115: ifnull -> 156
    //   118: new java/lang/StringBuilder
    //   121: astore_1
    //   122: aload_1
    //   123: invokespecial <init> : ()V
    //   126: aload_1
    //   127: ldc_w 'No operations allowed after connection closed.'
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload_1
    //   135: ldc_w '  '
    //   138: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_1
    //   143: aload_0
    //   144: getfield closedReason : Ljava/lang/String;
    //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload_1
    //   152: invokevirtual toString : ()Ljava/lang/String;
    //   155: astore_1
    //   156: aload_1
    //   157: ldc_w '08003'
    //   160: aconst_null
    //   161: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   164: athrow
    //   165: aload_2
    //   166: aload_0
    //   167: getfield thisAsConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   170: aload_3
    //   171: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   174: astore #5
    //   176: aload #5
    //   178: astore_1
    //   179: aload_0
    //   180: aload_2
    //   181: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   184: aload #5
    //   186: invokevirtual proxyIfReturnTypeIsJdbcInterface : (Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
    //   189: astore_2
    //   190: aload_2
    //   191: astore_1
    //   192: goto -> 201
    //   195: astore_2
    //   196: aload_0
    //   197: aload_2
    //   198: invokevirtual dealWithInvocationException : (Ljava/lang/reflect/InvocationTargetException;)V
    //   201: ldc 'setAutoCommit'
    //   203: aload #6
    //   205: invokevirtual equals : (Ljava/lang/Object;)Z
    //   208: ifeq -> 224
    //   211: aload_0
    //   212: aload_3
    //   213: iconst_0
    //   214: aaload
    //   215: checkcast java/lang/Boolean
    //   218: invokevirtual booleanValue : ()Z
    //   221: putfield explicitlyAutoCommit : Z
    //   224: aload_0
    //   225: getfield explicitlyAutoCommit : Z
    //   228: ifne -> 251
    //   231: ldc 'commit'
    //   233: aload #6
    //   235: invokevirtual equals : (Ljava/lang/Object;)Z
    //   238: ifne -> 251
    //   241: ldc 'rollback'
    //   243: aload #6
    //   245: invokevirtual equals : (Ljava/lang/Object;)Z
    //   248: ifeq -> 262
    //   251: aload_0
    //   252: invokevirtual readyToFallBackToPrimaryHost : ()Z
    //   255: ifeq -> 262
    //   258: aload_0
    //   259: invokevirtual fallBackToPrimaryIfAvailable : ()V
    //   262: aload_0
    //   263: monitorexit
    //   264: aload_1
    //   265: areturn
    //   266: astore_1
    //   267: aload_0
    //   268: monitorexit
    //   269: aload_1
    //   270: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	266	finally
    //   24	47	266	finally
    //   56	104	266	finally
    //   111	156	266	finally
    //   156	165	266	finally
    //   165	176	195	java/lang/reflect/InvocationTargetException
    //   165	176	266	finally
    //   179	190	195	java/lang/reflect/InvocationTargetException
    //   179	190	266	finally
    //   196	201	266	finally
    //   201	224	266	finally
    //   224	251	266	finally
    //   251	262	266	finally
  }
  
  public boolean isConnected() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentHostIndex : I
    //   6: istore_1
    //   7: iload_1
    //   8: iconst_m1
    //   9: if_icmpeq -> 17
    //   12: iconst_1
    //   13: istore_2
    //   14: goto -> 19
    //   17: iconst_0
    //   18: istore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: iload_2
    //   22: ireturn
    //   23: astore_3
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_3
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	23	finally
  }
  
  public boolean isMasterConnection() {
    return connectedToPrimaryHost();
  }
  
  public boolean isPrimaryHostIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield primaryHostIndex : I
    //   6: istore_2
    //   7: iload_1
    //   8: iload_2
    //   9: if_icmpne -> 17
    //   12: iconst_1
    //   13: istore_3
    //   14: goto -> 19
    //   17: iconst_0
    //   18: istore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: iload_3
    //   22: ireturn
    //   23: astore #4
    //   25: aload_0
    //   26: monitorexit
    //   27: aload #4
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	23	finally
  }
  
  public void pickNewConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isClosed : Z
    //   6: ifeq -> 21
    //   9: aload_0
    //   10: getfield closedExplicitly : Z
    //   13: istore_1
    //   14: iload_1
    //   15: ifeq -> 21
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: aload_0
    //   22: invokevirtual isConnected : ()Z
    //   25: ifeq -> 45
    //   28: aload_0
    //   29: invokevirtual readyToFallBackToPrimaryHost : ()Z
    //   32: ifeq -> 38
    //   35: goto -> 45
    //   38: aload_0
    //   39: invokespecial failOver : ()V
    //   42: goto -> 69
    //   45: aload_0
    //   46: aload_0
    //   47: getfield primaryHostIndex : I
    //   50: invokespecial connectTo : (I)V
    //   53: goto -> 69
    //   56: astore_2
    //   57: aload_0
    //   58: invokespecial resetAutoFallBackCounters : ()V
    //   61: aload_0
    //   62: aload_0
    //   63: getfield primaryHostIndex : I
    //   66: invokespecial failOver : (I)V
    //   69: aload_0
    //   70: monitorexit
    //   71: return
    //   72: astore_2
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_2
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	72	finally
    //   21	35	72	finally
    //   38	42	72	finally
    //   45	53	56	java/sql/SQLException
    //   45	53	72	finally
    //   57	69	72	finally
  }
  
  public boolean readyToFallBackToPrimaryHost() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield enableFallBackToPrimaryHost : Z
    //   6: ifeq -> 37
    //   9: aload_0
    //   10: invokevirtual connectedToSecondaryHost : ()Z
    //   13: ifeq -> 37
    //   16: aload_0
    //   17: invokespecial secondsBeforeRetryPrimaryHostIsMet : ()Z
    //   20: ifne -> 32
    //   23: aload_0
    //   24: invokespecial queriesBeforeRetryPrimaryHostIsMet : ()Z
    //   27: istore_1
    //   28: iload_1
    //   29: ifeq -> 37
    //   32: iconst_1
    //   33: istore_1
    //   34: goto -> 39
    //   37: iconst_0
    //   38: istore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: iload_1
    //   42: ireturn
    //   43: astore_2
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_2
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	43	finally
  }
  
  public boolean shouldExceptionTriggerConnectionSwitch(Throwable paramThrowable) {
    if (!(paramThrowable instanceof SQLException))
      return false; 
    String str = ((SQLException)paramThrowable).getSQLState();
    return (str != null && str.startsWith("08")) ? true : ((paramThrowable instanceof CommunicationsException));
  }
  
  public class FailoverJdbcInterfaceProxy extends MultiHostConnectionProxy.JdbcInterfaceProxy {
    public final FailoverConnectionProxy this$0;
    
    public FailoverJdbcInterfaceProxy(Object param1Object) {
      super(param1Object);
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      boolean bool = param1Method.getName().startsWith("execute");
      if (FailoverConnectionProxy.this.connectedToSecondaryHost() && bool)
        FailoverConnectionProxy.this.incrementQueriesIssuedSinceFailover(); 
      param1Object = super.invoke(param1Object, param1Method, param1ArrayOfObject);
      if (FailoverConnectionProxy.this.explicitlyAutoCommit && bool && FailoverConnectionProxy.this.readyToFallBackToPrimaryHost())
        FailoverConnectionProxy.this.fallBackToPrimaryIfAvailable(); 
      return param1Object;
    }
  }
}
