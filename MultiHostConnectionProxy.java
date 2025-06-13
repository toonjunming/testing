package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

public abstract class MultiHostConnectionProxy implements InvocationHandler {
  private static Constructor<?> JDBC_4_MS_CONNECTION_CTOR;
  
  private static final String METHOD_ABORT = "abort";
  
  private static final String METHOD_ABORT_INTERNAL = "abortInternal";
  
  private static final String METHOD_CLOSE = "close";
  
  private static final String METHOD_EQUALS = "equals";
  
  private static final String METHOD_GET_AUTO_COMMIT = "getAutoCommit";
  
  private static final String METHOD_GET_CATALOG = "getCatalog";
  
  private static final String METHOD_GET_MULTI_HOST_SAFE_PROXY = "getMultiHostSafeProxy";
  
  private static final String METHOD_GET_SESSION_MAX_ROWS = "getSessionMaxRows";
  
  private static final String METHOD_GET_TRANSACTION_ISOLATION = "getTransactionIsolation";
  
  private static final String METHOD_HASH_CODE = "hashCode";
  
  private static final String METHOD_IS_CLOSED = "isClosed";
  
  public boolean autoReconnect = false;
  
  public boolean closedExplicitly = false;
  
  public String closedReason = null;
  
  public MySQLConnection currentConnection = null;
  
  public List<String> hostList;
  
  public boolean isClosed = false;
  
  public Throwable lastExceptionDealtWith = null;
  
  public Properties localProps;
  
  public MySQLConnection proxyConnection = null;
  
  public MySQLConnection thisAsConnection = null;
  
  static {
    if (Util.isJdbc4())
      try {
        JDBC_4_MS_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4MultiHostMySQLConnection").getConstructor(new Class[] { MultiHostConnectionProxy.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      }  
  }
  
  public MultiHostConnectionProxy() throws SQLException {
    this.thisAsConnection = getNewWrapperForThisAsConnection();
  }
  
  public MultiHostConnectionProxy(List<String> paramList, Properties paramProperties) throws SQLException {
    this();
    initializeHostsSpecs(paramList, paramProperties);
  }
  
  public boolean allowedOnClosedConnection(Method paramMethod) {
    String str = paramMethod.getName();
    return (str.equals("getAutoCommit") || str.equals("getCatalog") || str.equals("getTransactionIsolation") || str.equals("getSessionMaxRows"));
  }
  
  public ConnectionImpl createConnectionForHost(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield localProps : Ljava/util/Properties;
    //   6: invokevirtual clone : ()Ljava/lang/Object;
    //   9: checkcast java/util/Properties
    //   12: astore #4
    //   14: aload_1
    //   15: invokestatic parseHostPortPair : (Ljava/lang/String;)[Ljava/lang/String;
    //   18: astore_1
    //   19: aload_1
    //   20: iconst_0
    //   21: aaload
    //   22: astore #5
    //   24: aload_1
    //   25: iconst_1
    //   26: aaload
    //   27: astore_3
    //   28: aload #4
    //   30: ldc 'DBNAME'
    //   32: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   35: astore #6
    //   37: aload #5
    //   39: ifnull -> 186
    //   42: aload_3
    //   43: astore_1
    //   44: aload_3
    //   45: ifnonnull -> 51
    //   48: ldc '3306'
    //   50: astore_1
    //   51: aload #4
    //   53: ldc 'HOST'
    //   55: aload #5
    //   57: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   60: pop
    //   61: aload #4
    //   63: ldc 'PORT'
    //   65: aload_1
    //   66: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   69: pop
    //   70: aload #4
    //   72: ldc 'HOST.1'
    //   74: aload #5
    //   76: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   79: pop
    //   80: aload #4
    //   82: ldc 'PORT.1'
    //   84: aload_1
    //   85: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   88: pop
    //   89: aload #4
    //   91: ldc 'NUM_HOSTS'
    //   93: ldc '1'
    //   95: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   98: pop
    //   99: aload #4
    //   101: ldc 'roundRobinLoadBalance'
    //   103: ldc 'false'
    //   105: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   108: pop
    //   109: aload_1
    //   110: invokestatic parseInt : (Ljava/lang/String;)I
    //   113: istore_2
    //   114: new java/lang/StringBuilder
    //   117: astore_3
    //   118: aload_3
    //   119: invokespecial <init> : ()V
    //   122: aload_3
    //   123: ldc 'jdbc:mysql://'
    //   125: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_3
    //   130: aload #5
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_3
    //   137: ldc ':'
    //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload_3
    //   144: aload_1
    //   145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: aload_3
    //   150: ldc '/'
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload #5
    //   158: iload_2
    //   159: aload #4
    //   161: aload #6
    //   163: aload_3
    //   164: invokevirtual toString : ()Ljava/lang/String;
    //   167: invokestatic getInstance : (Ljava/lang/String;ILjava/util/Properties;Ljava/lang/String;Ljava/lang/String;)Lcom/mysql/jdbc/Connection;
    //   170: checkcast com/mysql/jdbc/ConnectionImpl
    //   173: astore_1
    //   174: aload_1
    //   175: aload_0
    //   176: invokevirtual getProxy : ()Lcom/mysql/jdbc/MySQLConnection;
    //   179: invokevirtual setProxy : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   182: aload_0
    //   183: monitorexit
    //   184: aload_1
    //   185: areturn
    //   186: new java/sql/SQLException
    //   189: astore_1
    //   190: aload_1
    //   191: ldc 'Could not find a hostname to start a connection to'
    //   193: invokespecial <init> : (Ljava/lang/String;)V
    //   196: aload_1
    //   197: athrow
    //   198: astore_1
    //   199: aload_0
    //   200: monitorexit
    //   201: aload_1
    //   202: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	198	finally
    //   28	37	198	finally
    //   51	182	198	finally
    //   186	198	198	finally
  }
  
  public void dealWithInvocationException(InvocationTargetException paramInvocationTargetException) throws SQLException, Throwable, InvocationTargetException {
    Throwable throwable = paramInvocationTargetException.getTargetException();
    if (throwable != null) {
      if (this.lastExceptionDealtWith != throwable && shouldExceptionTriggerConnectionSwitch(throwable)) {
        invalidateCurrentConnection();
        pickNewConnection();
        this.lastExceptionDealtWith = throwable;
      } 
      throw throwable;
    } 
    throw paramInvocationTargetException;
  }
  
  public abstract void doAbort(Executor paramExecutor) throws SQLException;
  
  public abstract void doAbortInternal() throws SQLException;
  
  public abstract void doClose() throws SQLException;
  
  public InvocationHandler getNewJdbcInterfaceProxy(Object paramObject) {
    return new JdbcInterfaceProxy(paramObject);
  }
  
  public MySQLConnection getNewWrapperForThisAsConnection() throws SQLException {
    return (Util.isJdbc4() || JDBC_4_MS_CONNECTION_CTOR != null) ? (MySQLConnection)Util.handleNewInstance(JDBC_4_MS_CONNECTION_CTOR, new Object[] { this }, null) : new MultiHostMySQLConnection(this);
  }
  
  public MySQLConnection getProxy() {
    MySQLConnection mySQLConnection = this.proxyConnection;
    if (mySQLConnection == null)
      mySQLConnection = this.thisAsConnection; 
    return mySQLConnection;
  }
  
  public int initializeHostsSpecs(List<String> paramList, Properties paramProperties) {
    boolean bool = "true".equalsIgnoreCase(paramProperties.getProperty("autoReconnect"));
    byte b = 0;
    if (bool || "true".equalsIgnoreCase(paramProperties.getProperty("autoReconnectForPools"))) {
      bool = true;
    } else {
      bool = false;
    } 
    this.autoReconnect = bool;
    this.hostList = paramList;
    int i = paramList.size();
    Properties properties = (Properties)paramProperties.clone();
    this.localProps = properties;
    properties.remove("HOST");
    this.localProps.remove("PORT");
    while (b < i) {
      paramProperties = this.localProps;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("HOST.");
      stringBuilder.append(++b);
      paramProperties.remove(stringBuilder.toString());
      paramProperties = this.localProps;
      stringBuilder = new StringBuilder();
      stringBuilder.append("PORT.");
      stringBuilder.append(b);
      paramProperties.remove(stringBuilder.toString());
    } 
    this.localProps.remove("NUM_HOSTS");
    return i;
  }
  
  public void invalidateConnection(MySQLConnection paramMySQLConnection) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 49
    //   6: aload_1
    //   7: invokeinterface isClosed : ()Z
    //   12: ifne -> 49
    //   15: aload_1
    //   16: invokeinterface getAutoCommit : ()Z
    //   21: ifne -> 29
    //   24: iconst_1
    //   25: istore_2
    //   26: goto -> 31
    //   29: iconst_0
    //   30: istore_2
    //   31: aload_1
    //   32: iconst_1
    //   33: iload_2
    //   34: iconst_1
    //   35: aconst_null
    //   36: invokeinterface realClose : (ZZZLjava/lang/Throwable;)V
    //   41: goto -> 49
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_1
    //   53: goto -> 49
    // Exception table:
    //   from	to	target	type
    //   6	24	52	java/sql/SQLException
    //   6	24	44	finally
    //   31	41	52	java/sql/SQLException
    //   31	41	44	finally
  }
  
  public void invalidateCurrentConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   7: invokevirtual invalidateConnection : (Lcom/mysql/jdbc/MySQLConnection;)V
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
  
  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: invokevirtual getName : ()Ljava/lang/String;
    //   6: astore #7
    //   8: ldc 'getMultiHostSafeProxy'
    //   10: aload #7
    //   12: invokevirtual equals : (Ljava/lang/Object;)Z
    //   15: ifeq -> 27
    //   18: aload_0
    //   19: getfield thisAsConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: areturn
    //   27: ldc 'equals'
    //   29: aload #7
    //   31: invokevirtual equals : (Ljava/lang/Object;)Z
    //   34: istore #6
    //   36: iconst_0
    //   37: istore #4
    //   39: iload #6
    //   41: ifeq -> 61
    //   44: aload_3
    //   45: iconst_0
    //   46: aaload
    //   47: aload_0
    //   48: invokevirtual equals : (Ljava/lang/Object;)Z
    //   51: istore #6
    //   53: aload_0
    //   54: monitorexit
    //   55: iload #6
    //   57: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   60: areturn
    //   61: ldc 'hashCode'
    //   63: aload #7
    //   65: invokevirtual equals : (Ljava/lang/Object;)Z
    //   68: ifeq -> 85
    //   71: aload_0
    //   72: invokevirtual hashCode : ()I
    //   75: istore #4
    //   77: aload_0
    //   78: monitorexit
    //   79: iload #4
    //   81: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   84: areturn
    //   85: ldc 'close'
    //   87: aload #7
    //   89: invokevirtual equals : (Ljava/lang/Object;)Z
    //   92: ifeq -> 120
    //   95: aload_0
    //   96: invokevirtual doClose : ()V
    //   99: aload_0
    //   100: iconst_1
    //   101: putfield isClosed : Z
    //   104: aload_0
    //   105: ldc_w 'Connection explicitly closed.'
    //   108: putfield closedReason : Ljava/lang/String;
    //   111: aload_0
    //   112: iconst_1
    //   113: putfield closedExplicitly : Z
    //   116: aload_0
    //   117: monitorexit
    //   118: aconst_null
    //   119: areturn
    //   120: ldc 'abortInternal'
    //   122: aload #7
    //   124: invokevirtual equals : (Ljava/lang/Object;)Z
    //   127: ifeq -> 159
    //   130: aload_0
    //   131: invokevirtual doAbortInternal : ()V
    //   134: aload_0
    //   135: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   138: invokeinterface abortInternal : ()V
    //   143: aload_0
    //   144: iconst_1
    //   145: putfield isClosed : Z
    //   148: aload_0
    //   149: ldc_w 'Connection explicitly closed.'
    //   152: putfield closedReason : Ljava/lang/String;
    //   155: aload_0
    //   156: monitorexit
    //   157: aconst_null
    //   158: areturn
    //   159: ldc 'abort'
    //   161: aload #7
    //   163: invokevirtual equals : (Ljava/lang/Object;)Z
    //   166: ifeq -> 201
    //   169: aload_3
    //   170: arraylength
    //   171: iconst_1
    //   172: if_icmpne -> 201
    //   175: aload_0
    //   176: aload_3
    //   177: iconst_0
    //   178: aaload
    //   179: checkcast java/util/concurrent/Executor
    //   182: invokevirtual doAbort : (Ljava/util/concurrent/Executor;)V
    //   185: aload_0
    //   186: iconst_1
    //   187: putfield isClosed : Z
    //   190: aload_0
    //   191: ldc_w 'Connection explicitly closed.'
    //   194: putfield closedReason : Ljava/lang/String;
    //   197: aload_0
    //   198: monitorexit
    //   199: aconst_null
    //   200: areturn
    //   201: ldc 'isClosed'
    //   203: aload #7
    //   205: invokevirtual equals : (Ljava/lang/Object;)Z
    //   208: ifeq -> 225
    //   211: aload_0
    //   212: getfield isClosed : Z
    //   215: istore #6
    //   217: aload_0
    //   218: monitorexit
    //   219: iload #6
    //   221: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   224: areturn
    //   225: aload_0
    //   226: aload_1
    //   227: aload_2
    //   228: aload_3
    //   229: invokevirtual invokeMore : (Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;
    //   232: astore_1
    //   233: aload_0
    //   234: monitorexit
    //   235: aload_1
    //   236: areturn
    //   237: astore_1
    //   238: aload_2
    //   239: invokevirtual getExceptionTypes : ()[Ljava/lang/Class;
    //   242: astore_2
    //   243: aload_2
    //   244: arraylength
    //   245: istore #5
    //   247: iload #4
    //   249: iload #5
    //   251: if_icmpge -> 276
    //   254: aload_2
    //   255: iload #4
    //   257: aaload
    //   258: aload_1
    //   259: invokevirtual getClass : ()Ljava/lang/Class;
    //   262: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   265: ifne -> 274
    //   268: iinc #4, 1
    //   271: goto -> 247
    //   274: aload_1
    //   275: athrow
    //   276: new java/lang/IllegalStateException
    //   279: astore_2
    //   280: aload_2
    //   281: aload_1
    //   282: invokevirtual getMessage : ()Ljava/lang/String;
    //   285: aload_1
    //   286: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   289: aload_2
    //   290: athrow
    //   291: astore_2
    //   292: aload_2
    //   293: astore_1
    //   294: aload_2
    //   295: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   298: ifnull -> 306
    //   301: aload_2
    //   302: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   305: astore_1
    //   306: aload_1
    //   307: athrow
    //   308: astore_1
    //   309: aload_0
    //   310: monitorexit
    //   311: aload_1
    //   312: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	308	finally
    //   27	36	308	finally
    //   44	53	308	finally
    //   61	77	308	finally
    //   85	116	308	finally
    //   120	155	308	finally
    //   159	197	308	finally
    //   201	217	308	finally
    //   225	233	291	java/lang/reflect/InvocationTargetException
    //   225	233	237	java/lang/Exception
    //   225	233	308	finally
    //   238	247	308	finally
    //   254	268	308	finally
    //   274	276	308	finally
    //   276	291	308	finally
    //   294	306	308	finally
    //   306	308	308	finally
  }
  
  public abstract Object invokeMore(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
  
  public abstract boolean isMasterConnection();
  
  public abstract void pickNewConnection() throws SQLException;
  
  public void propagateProxyDown(MySQLConnection paramMySQLConnection) {
    this.currentConnection.setProxy(paramMySQLConnection);
  }
  
  public Object proxyIfReturnTypeIsJdbcInterface(Class<?> paramClass, Object paramObject) {
    if (paramObject != null && Util.isJdbcInterface(paramClass)) {
      paramClass = paramObject.getClass();
      return Proxy.newProxyInstance(paramClass.getClassLoader(), Util.getImplementedInterfaces(paramClass), getNewJdbcInterfaceProxy(paramObject));
    } 
    return paramObject;
  }
  
  public final void setProxy(MySQLConnection paramMySQLConnection) {
    this.proxyConnection = paramMySQLConnection;
    propagateProxyDown(paramMySQLConnection);
  }
  
  public abstract boolean shouldExceptionTriggerConnectionSwitch(Throwable paramThrowable);
  
  public void syncSessionState(Connection paramConnection1, Connection paramConnection2) throws SQLException {
    if (paramConnection1 != null && paramConnection2 != null) {
      boolean bool1 = paramConnection1.getUseLocalSessionState();
      paramConnection1.setUseLocalSessionState(true);
      boolean bool2 = paramConnection1.isReadOnly();
      paramConnection1.setUseLocalSessionState(bool1);
      syncSessionState(paramConnection1, paramConnection2, bool2);
    } 
  }
  
  public void syncSessionState(Connection paramConnection1, Connection paramConnection2, boolean paramBoolean) throws SQLException {
    if (paramConnection2 != null)
      paramConnection2.setReadOnly(paramBoolean); 
    if (paramConnection1 != null && paramConnection2 != null) {
      paramBoolean = paramConnection1.getUseLocalSessionState();
      paramConnection1.setUseLocalSessionState(true);
      paramConnection2.setAutoCommit(paramConnection1.getAutoCommit());
      paramConnection2.setCatalog(paramConnection1.getCatalog());
      paramConnection2.setTransactionIsolation(paramConnection1.getTransactionIsolation());
      paramConnection2.setSessionMaxRows(paramConnection1.getSessionMaxRows());
      paramConnection1.setUseLocalSessionState(paramBoolean);
    } 
  }
  
  public class JdbcInterfaceProxy implements InvocationHandler {
    public Object invokeOn = null;
    
    public final MultiHostConnectionProxy this$0;
    
    public JdbcInterfaceProxy(Object param1Object) {
      this.invokeOn = param1Object;
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      if ("equals".equals(param1Method.getName()))
        return Boolean.valueOf(param1ArrayOfObject[0].equals(this)); 
      MultiHostConnectionProxy multiHostConnectionProxy = MultiHostConnectionProxy.this;
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{com/mysql/jdbc/MultiHostConnectionProxy}, name=null} */
      param1Object = null;
      try {
        Object object2 = param1Method.invoke(this.invokeOn, param1ArrayOfObject);
        param1Object = object2;
        Object object1 = MultiHostConnectionProxy.this.proxyIfReturnTypeIsJdbcInterface(param1Method.getReturnType(), object2);
        param1Object = object1;
      } catch (InvocationTargetException invocationTargetException) {
        MultiHostConnectionProxy.this.dealWithInvocationException(invocationTargetException);
      } finally {}
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{com/mysql/jdbc/MultiHostConnectionProxy}, name=null} */
      return param1Object;
    }
  }
}
