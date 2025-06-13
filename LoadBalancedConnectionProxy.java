package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

public class LoadBalancedConnectionProxy extends MultiHostConnectionProxy implements PingTarget {
  public static final String BLACKLIST_TIMEOUT_PROPERTY_KEY = "loadBalanceBlacklistTimeout";
  
  public static final String HOST_REMOVAL_GRACE_PERIOD_PROPERTY_KEY = "loadBalanceHostRemovalGracePeriod";
  
  private static Class<?>[] INTERFACES_TO_PROXY;
  
  private static Constructor<?> JDBC_4_LB_CONNECTION_CTOR;
  
  private static Map<String, Long> globalBlacklist = new HashMap<String, Long>();
  
  private static LoadBalancedConnection nullLBConnectionInstance = null;
  
  private int autoCommitSwapThreshold = 0;
  
  private BalanceStrategy balancer;
  
  private ConnectionGroup connectionGroup = null;
  
  private long connectionGroupProxyID = 0L;
  
  private Map<ConnectionImpl, String> connectionsToHostsMap;
  
  private LoadBalanceExceptionChecker exceptionChecker;
  
  private int globalBlacklistTimeout = 0;
  
  private int hostRemovalGracePeriod = 0;
  
  private Map<String, Integer> hostsToListIndexMap;
  
  private Set<String> hostsToRemove = new HashSet<String>();
  
  private boolean inTransaction = false;
  
  public Map<String, ConnectionImpl> liveConnections;
  
  private long[] responseTimes;
  
  private int retriesAllDown;
  
  private long totalPhysicalConnections = 0L;
  
  private long transactionCount = 0L;
  
  private long transactionStartTime = 0L;
  
  private LoadBalancedConnectionProxy(List<String> paramList, Properties paramProperties) throws SQLException {
    String str2 = paramProperties.getProperty("loadBalanceConnectionGroup", null);
    String str1 = paramProperties.getProperty("loadBalanceEnableJMX", "false");
    try {
      StringBuilder stringBuilder;
      boolean bool = Boolean.parseBoolean(str1);
      List<String> list = paramList;
      if (str2 != null) {
        this.connectionGroup = ConnectionGroupManager.getConnectionGroupInstance(str2);
        if (bool)
          ConnectionGroupManager.registerJmx(); 
        this.connectionGroupProxyID = this.connectionGroup.registerConnectionProxy(this, paramList);
        list = new ArrayList<String>(this.connectionGroup.getInitialHosts());
      } 
      int i = initializeHostsSpecs(list, paramProperties);
      this.liveConnections = new HashMap<String, ConnectionImpl>(i);
      this.hostsToListIndexMap = new HashMap<String, Integer>(i);
      for (byte b = 0; b < i; b++)
        this.hostsToListIndexMap.put(this.hostList.get(b), Integer.valueOf(b)); 
      this.connectionsToHostsMap = new HashMap<ConnectionImpl, String>(i);
      this.responseTimes = new long[i];
      String str = this.localProps.getProperty("retriesAllDown", "120");
      try {
        this.retriesAllDown = Integer.parseInt(str);
        str = this.localProps.getProperty("loadBalanceBlacklistTimeout", "0");
        try {
          this.globalBlacklistTimeout = Integer.parseInt(str);
          str = this.localProps.getProperty("loadBalanceHostRemovalGracePeriod", "15000");
          try {
            this.hostRemovalGracePeriod = Integer.parseInt(str);
            str = this.localProps.getProperty("loadBalanceStrategy", "random");
            if ("random".equals(str)) {
              this.balancer = (BalanceStrategy)Util.loadExtensions(null, paramProperties, RandomBalanceStrategy.class.getName(), "InvalidLoadBalanceStrategy", null).get(0);
            } else if ("bestResponseTime".equals(str)) {
              this.balancer = (BalanceStrategy)Util.loadExtensions(null, paramProperties, BestResponseTimeBalanceStrategy.class.getName(), "InvalidLoadBalanceStrategy", null).get(0);
            } else if ("serverAffinity".equals(str)) {
              this.balancer = (BalanceStrategy)Util.loadExtensions(null, paramProperties, ServerAffinityStrategy.class.getName(), "InvalidLoadBalanceStrategy", null).get(0);
            } else {
              this.balancer = (BalanceStrategy)Util.loadExtensions(null, paramProperties, str, "InvalidLoadBalanceStrategy", null).get(0);
            } 
            str = paramProperties.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
            try {
              this.autoCommitSwapThreshold = Integer.parseInt(str);
              str = paramProperties.getProperty("loadBalanceAutoCommitStatementRegex", "");
              if (!"".equals(str))
                try {
                  "".matches(str);
                } catch (Exception exception) {
                  throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceAutoCommitStatementRegex", new Object[] { str }), "S1009", null);
                }  
              if (this.autoCommitSwapThreshold > 0) {
                str1 = this.localProps.getProperty("statementInterceptors");
                if (str1 == null) {
                  this.localProps.setProperty("statementInterceptors", "com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
                } else if (str1.length() > 0) {
                  Properties properties = this.localProps;
                  stringBuilder = new StringBuilder();
                  stringBuilder.append(str1);
                  stringBuilder.append(",com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
                  properties.setProperty("statementInterceptors", stringBuilder.toString());
                } 
                exception.setProperty("statementInterceptors", this.localProps.getProperty("statementInterceptors"));
              } 
              this.balancer.init(null, (Properties)exception);
              this.exceptionChecker = (LoadBalanceExceptionChecker)Util.loadExtensions(null, (Properties)exception, this.localProps.getProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker"), "InvalidLoadBalanceExceptionChecker", null).get(0);
              pickNewConnection();
              return;
            } catch (NumberFormatException numberFormatException) {
              throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceAutoCommitStatementThreshold", new Object[] { stringBuilder }), "S1009", null);
            } 
          } catch (NumberFormatException numberFormatException) {
            throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceHostRemovalGracePeriod", new Object[] { stringBuilder }), "S1009", null);
          } 
        } catch (NumberFormatException numberFormatException) {
          throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceBlacklistTimeout", new Object[] { stringBuilder }), "S1009", null);
        } 
      } catch (NumberFormatException numberFormatException) {
        throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForRetriesAllDown", new Object[] { stringBuilder }), "S1009", null);
      } 
    } catch (Exception exception) {
      throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceEnableJMX", new Object[] { str1 }), "S1009", null);
    } 
  }
  
  private void closeAllConnections() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield liveConnections : Ljava/util/Map;
    //   6: invokeinterface values : ()Ljava/util/Collection;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_1
    //   17: aload_1
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 45
    //   26: aload_1
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: checkcast com/mysql/jdbc/ConnectionImpl
    //   35: astore_2
    //   36: aload_2
    //   37: invokeinterface close : ()V
    //   42: goto -> 17
    //   45: aload_0
    //   46: getfield isClosed : Z
    //   49: ifne -> 75
    //   52: aload_0
    //   53: getfield balancer : Lcom/mysql/jdbc/BalanceStrategy;
    //   56: invokeinterface destroy : ()V
    //   61: aload_0
    //   62: getfield connectionGroup : Lcom/mysql/jdbc/ConnectionGroup;
    //   65: astore_1
    //   66: aload_1
    //   67: ifnull -> 75
    //   70: aload_1
    //   71: aload_0
    //   72: invokevirtual closeConnectionProxy : (Lcom/mysql/jdbc/LoadBalancedConnectionProxy;)V
    //   75: aload_0
    //   76: getfield liveConnections : Ljava/util/Map;
    //   79: invokeinterface clear : ()V
    //   84: aload_0
    //   85: getfield connectionsToHostsMap : Ljava/util/Map;
    //   88: invokeinterface clear : ()V
    //   93: aload_0
    //   94: monitorexit
    //   95: return
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    //   101: astore_2
    //   102: goto -> 17
    // Exception table:
    //   from	to	target	type
    //   2	17	96	finally
    //   17	36	96	finally
    //   36	42	101	java/sql/SQLException
    //   36	42	96	finally
    //   45	66	96	finally
    //   70	75	96	finally
    //   75	93	96	finally
  }
  
  public static LoadBalancedConnection createProxyInstance(List<String> paramList, Properties paramProperties) throws SQLException {
    LoadBalancedConnectionProxy loadBalancedConnectionProxy = new LoadBalancedConnectionProxy(paramList, paramProperties);
    return (LoadBalancedConnection)Proxy.newProxyInstance(LoadBalancedConnection.class.getClassLoader(), INTERFACES_TO_PROXY, loadBalancedConnectionProxy);
  }
  
  public static LoadBalancedConnection getNullLoadBalancedConnectionInstance() {
    // Byte code:
    //   0: ldc com/mysql/jdbc/LoadBalancedConnectionProxy
    //   2: monitorenter
    //   3: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.nullLBConnectionInstance : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   6: ifnonnull -> 39
    //   9: ldc com/mysql/jdbc/LoadBalancedConnection
    //   11: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   14: astore_0
    //   15: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.INTERFACES_TO_PROXY : [Ljava/lang/Class;
    //   18: astore_1
    //   19: new com/mysql/jdbc/LoadBalancedConnectionProxy$NullLoadBalancedConnectionProxy
    //   22: astore_2
    //   23: aload_2
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: aload_1
    //   29: aload_2
    //   30: invokestatic newProxyInstance : (Ljava/lang/ClassLoader;[Ljava/lang/Class;Ljava/lang/reflect/InvocationHandler;)Ljava/lang/Object;
    //   33: checkcast com/mysql/jdbc/LoadBalancedConnection
    //   36: putstatic com/mysql/jdbc/LoadBalancedConnectionProxy.nullLBConnectionInstance : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   39: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.nullLBConnectionInstance : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   42: astore_0
    //   43: ldc com/mysql/jdbc/LoadBalancedConnectionProxy
    //   45: monitorexit
    //   46: aload_0
    //   47: areturn
    //   48: astore_0
    //   49: ldc com/mysql/jdbc/LoadBalancedConnectionProxy
    //   51: monitorexit
    //   52: aload_0
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   3	39	48	finally
    //   39	43	48	finally
  }
  
  public boolean addHost(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hostsToListIndexMap : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   12: istore_2
    //   13: iload_2
    //   14: ifeq -> 21
    //   17: aload_0
    //   18: monitorexit
    //   19: iconst_0
    //   20: ireturn
    //   21: aload_0
    //   22: getfield responseTimes : [J
    //   25: astore_3
    //   26: aload_3
    //   27: arraylength
    //   28: iconst_1
    //   29: iadd
    //   30: newarray long
    //   32: astore #4
    //   34: aload_3
    //   35: iconst_0
    //   36: aload #4
    //   38: iconst_0
    //   39: aload_3
    //   40: arraylength
    //   41: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   44: aload_0
    //   45: aload #4
    //   47: putfield responseTimes : [J
    //   50: aload_0
    //   51: getfield hostList : Ljava/util/List;
    //   54: aload_1
    //   55: invokeinterface contains : (Ljava/lang/Object;)Z
    //   60: ifne -> 74
    //   63: aload_0
    //   64: getfield hostList : Ljava/util/List;
    //   67: aload_1
    //   68: invokeinterface add : (Ljava/lang/Object;)Z
    //   73: pop
    //   74: aload_0
    //   75: getfield hostsToListIndexMap : Ljava/util/Map;
    //   78: aload_1
    //   79: aload_0
    //   80: getfield responseTimes : [J
    //   83: arraylength
    //   84: iconst_1
    //   85: isub
    //   86: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   89: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   94: pop
    //   95: aload_0
    //   96: getfield hostsToRemove : Ljava/util/Set;
    //   99: aload_1
    //   100: invokeinterface remove : (Ljava/lang/Object;)Z
    //   105: pop
    //   106: aload_0
    //   107: monitorexit
    //   108: iconst_1
    //   109: ireturn
    //   110: astore_1
    //   111: aload_0
    //   112: monitorexit
    //   113: aload_1
    //   114: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	110	finally
    //   21	74	110	finally
    //   74	106	110	finally
  }
  
  public void addToGlobalBlacklist(String paramString) {
    addToGlobalBlacklist(paramString, System.currentTimeMillis() + this.globalBlacklistTimeout);
  }
  
  public void addToGlobalBlacklist(String paramString, long paramLong) {
    if (isGlobalBlacklistEnabled())
      synchronized (globalBlacklist) {
        globalBlacklist.put(paramString, Long.valueOf(paramLong));
      }  
  }
  
  public ConnectionImpl createConnectionForHost(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial createConnectionForHost : (Ljava/lang/String;)Lcom/mysql/jdbc/ConnectionImpl;
    //   7: astore_2
    //   8: aload_0
    //   9: getfield liveConnections : Ljava/util/Map;
    //   12: aload_1
    //   13: aload_2
    //   14: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   19: pop
    //   20: aload_0
    //   21: getfield connectionsToHostsMap : Ljava/util/Map;
    //   24: aload_2
    //   25: aload_1
    //   26: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   31: pop
    //   32: aload_0
    //   33: aload_0
    //   34: getfield totalPhysicalConnections : J
    //   37: lconst_1
    //   38: ladd
    //   39: putfield totalPhysicalConnections : J
    //   42: aload_2
    //   43: invokevirtual getStatementInterceptorsInstances : ()Ljava/util/List;
    //   46: invokeinterface iterator : ()Ljava/util/Iterator;
    //   51: astore_1
    //   52: aload_1
    //   53: invokeinterface hasNext : ()Z
    //   58: ifeq -> 85
    //   61: aload_1
    //   62: invokeinterface next : ()Ljava/lang/Object;
    //   67: checkcast com/mysql/jdbc/StatementInterceptorV2
    //   70: astore_3
    //   71: aload_3
    //   72: instanceof com/mysql/jdbc/LoadBalancedAutoCommitInterceptor
    //   75: ifeq -> 52
    //   78: aload_3
    //   79: checkcast com/mysql/jdbc/LoadBalancedAutoCommitInterceptor
    //   82: invokevirtual resumeCounters : ()V
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_2
    //   88: areturn
    //   89: astore_1
    //   90: aload_0
    //   91: monitorexit
    //   92: aload_1
    //   93: athrow
    // Exception table:
    //   from	to	target	type
    //   2	52	89	finally
    //   52	85	89	finally
  }
  
  public void doAbort(Executor paramExecutor) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield liveConnections : Ljava/util/Map;
    //   6: invokeinterface values : ()Ljava/util/Collection;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_2
    //   17: aload_2
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 46
    //   26: aload_2
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: checkcast com/mysql/jdbc/ConnectionImpl
    //   35: astore_3
    //   36: aload_3
    //   37: aload_1
    //   38: invokeinterface abort : (Ljava/util/concurrent/Executor;)V
    //   43: goto -> 17
    //   46: aload_0
    //   47: getfield isClosed : Z
    //   50: ifne -> 76
    //   53: aload_0
    //   54: getfield balancer : Lcom/mysql/jdbc/BalanceStrategy;
    //   57: invokeinterface destroy : ()V
    //   62: aload_0
    //   63: getfield connectionGroup : Lcom/mysql/jdbc/ConnectionGroup;
    //   66: astore_1
    //   67: aload_1
    //   68: ifnull -> 76
    //   71: aload_1
    //   72: aload_0
    //   73: invokevirtual closeConnectionProxy : (Lcom/mysql/jdbc/LoadBalancedConnectionProxy;)V
    //   76: aload_0
    //   77: getfield liveConnections : Ljava/util/Map;
    //   80: invokeinterface clear : ()V
    //   85: aload_0
    //   86: getfield connectionsToHostsMap : Ljava/util/Map;
    //   89: invokeinterface clear : ()V
    //   94: aload_0
    //   95: monitorexit
    //   96: return
    //   97: astore_1
    //   98: aload_0
    //   99: monitorexit
    //   100: aload_1
    //   101: athrow
    //   102: astore_3
    //   103: goto -> 17
    // Exception table:
    //   from	to	target	type
    //   2	17	97	finally
    //   17	36	97	finally
    //   36	43	102	java/sql/SQLException
    //   36	43	97	finally
    //   46	67	97	finally
    //   71	76	97	finally
    //   76	94	97	finally
  }
  
  public void doAbortInternal() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield liveConnections : Ljava/util/Map;
    //   6: invokeinterface values : ()Ljava/util/Collection;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_1
    //   17: aload_1
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 45
    //   26: aload_1
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: checkcast com/mysql/jdbc/ConnectionImpl
    //   35: astore_2
    //   36: aload_2
    //   37: invokeinterface abortInternal : ()V
    //   42: goto -> 17
    //   45: aload_0
    //   46: getfield isClosed : Z
    //   49: ifne -> 75
    //   52: aload_0
    //   53: getfield balancer : Lcom/mysql/jdbc/BalanceStrategy;
    //   56: invokeinterface destroy : ()V
    //   61: aload_0
    //   62: getfield connectionGroup : Lcom/mysql/jdbc/ConnectionGroup;
    //   65: astore_1
    //   66: aload_1
    //   67: ifnull -> 75
    //   70: aload_1
    //   71: aload_0
    //   72: invokevirtual closeConnectionProxy : (Lcom/mysql/jdbc/LoadBalancedConnectionProxy;)V
    //   75: aload_0
    //   76: getfield liveConnections : Ljava/util/Map;
    //   79: invokeinterface clear : ()V
    //   84: aload_0
    //   85: getfield connectionsToHostsMap : Ljava/util/Map;
    //   88: invokeinterface clear : ()V
    //   93: aload_0
    //   94: monitorexit
    //   95: return
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    //   101: astore_2
    //   102: goto -> 17
    // Exception table:
    //   from	to	target	type
    //   2	17	96	finally
    //   17	36	96	finally
    //   36	42	101	java/sql/SQLException
    //   36	42	96	finally
    //   45	66	96	finally
    //   70	75	96	finally
    //   75	93	96	finally
  }
  
  public void doClose() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial closeAllConnections : ()V
    //   6: aload_0
    //   7: monitorexit
    //   8: return
    //   9: astore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	9	finally
  }
  
  public void doPing() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_3
    //   4: iconst_0
    //   5: istore_1
    //   6: aload_0
    //   7: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   10: invokeinterface getLoadBalancePingTimeout : ()I
    //   15: istore_2
    //   16: aload_0
    //   17: getfield hostList : Ljava/util/List;
    //   20: invokeinterface iterator : ()Ljava/util/Iterator;
    //   25: astore #6
    //   27: aload #6
    //   29: invokeinterface hasNext : ()Z
    //   34: ifeq -> 210
    //   37: aload #6
    //   39: invokeinterface next : ()Ljava/lang/Object;
    //   44: checkcast java/lang/String
    //   47: astore #4
    //   49: aload_0
    //   50: getfield liveConnections : Ljava/util/Map;
    //   53: aload #4
    //   55: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   60: checkcast com/mysql/jdbc/ConnectionImpl
    //   63: astore #7
    //   65: aload #7
    //   67: ifnonnull -> 73
    //   70: goto -> 27
    //   73: iload_2
    //   74: ifne -> 85
    //   77: aload #7
    //   79: invokevirtual ping : ()V
    //   82: goto -> 92
    //   85: aload #7
    //   87: iconst_1
    //   88: iload_2
    //   89: invokevirtual pingInternal : (ZI)V
    //   92: iconst_1
    //   93: istore_1
    //   94: goto -> 27
    //   97: astore #5
    //   99: aload #4
    //   101: aload_0
    //   102: getfield connectionsToHostsMap : Ljava/util/Map;
    //   105: aload_0
    //   106: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   109: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   114: invokevirtual equals : (Ljava/lang/Object;)Z
    //   117: ifne -> 191
    //   120: aload #5
    //   122: invokevirtual getMessage : ()Ljava/lang/String;
    //   125: ldc_w 'Connection.exceededConnectionLifetime'
    //   128: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   131: invokevirtual equals : (Ljava/lang/Object;)Z
    //   134: ifeq -> 147
    //   137: aload_3
    //   138: astore #4
    //   140: aload_3
    //   141: ifnonnull -> 164
    //   144: goto -> 160
    //   147: aload_0
    //   148: invokevirtual isGlobalBlacklistEnabled : ()Z
    //   151: ifeq -> 160
    //   154: aload_0
    //   155: aload #4
    //   157: invokevirtual addToGlobalBlacklist : (Ljava/lang/String;)V
    //   160: aload #5
    //   162: astore #4
    //   164: aload_0
    //   165: getfield liveConnections : Ljava/util/Map;
    //   168: aload_0
    //   169: getfield connectionsToHostsMap : Ljava/util/Map;
    //   172: aload #7
    //   174: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   179: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   184: pop
    //   185: aload #4
    //   187: astore_3
    //   188: goto -> 27
    //   191: aload_0
    //   192: invokespecial closeAllConnections : ()V
    //   195: aload_0
    //   196: iconst_1
    //   197: putfield isClosed : Z
    //   200: aload_0
    //   201: ldc_w 'Connection closed because ping of current connection failed.'
    //   204: putfield closedReason : Ljava/lang/String;
    //   207: aload #5
    //   209: athrow
    //   210: iload_1
    //   211: ifne -> 249
    //   214: aload_0
    //   215: invokespecial closeAllConnections : ()V
    //   218: aload_0
    //   219: iconst_1
    //   220: putfield isClosed : Z
    //   223: aload_0
    //   224: ldc_w 'Connection closed due to inability to ping any active connections.'
    //   227: putfield closedReason : Ljava/lang/String;
    //   230: aload_3
    //   231: ifnonnull -> 247
    //   234: aload_0
    //   235: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   238: checkcast com/mysql/jdbc/ConnectionImpl
    //   241: invokevirtual throwConnectionClosedException : ()V
    //   244: goto -> 249
    //   247: aload_3
    //   248: athrow
    //   249: aload_0
    //   250: monitorexit
    //   251: return
    //   252: astore_3
    //   253: aload_0
    //   254: monitorexit
    //   255: aload_3
    //   256: athrow
    // Exception table:
    //   from	to	target	type
    //   6	27	252	finally
    //   27	65	252	finally
    //   77	82	97	java/sql/SQLException
    //   77	82	252	finally
    //   85	92	97	java/sql/SQLException
    //   85	92	252	finally
    //   99	137	252	finally
    //   147	160	252	finally
    //   164	185	252	finally
    //   191	210	252	finally
    //   214	230	252	finally
    //   234	244	252	finally
    //   247	249	252	finally
  }
  
  public long getActivePhysicalConnectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield liveConnections : Ljava/util/Map;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: iload_1
    //   13: i2l
    //   14: lstore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: lload_2
    //   18: lreturn
    //   19: astore #4
    //   21: aload_0
    //   22: monitorexit
    //   23: aload #4
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	19	finally
  }
  
  public long getConnectionGroupProxyID() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connectionGroupProxyID : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public String getCurrentActiveHost() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 35
    //   11: aload_0
    //   12: getfield connectionsToHostsMap : Ljava/util/Map;
    //   15: aload_1
    //   16: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   21: astore_1
    //   22: aload_1
    //   23: ifnull -> 35
    //   26: aload_1
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: areturn
    //   35: aload_0
    //   36: monitorexit
    //   37: aconst_null
    //   38: areturn
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	39	finally
    //   11	22	39	finally
    //   26	31	39	finally
  }
  
  public long getCurrentTransactionDuration() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield inTransaction : Z
    //   6: ifeq -> 33
    //   9: aload_0
    //   10: getfield transactionStartTime : J
    //   13: lconst_0
    //   14: lcmp
    //   15: ifle -> 33
    //   18: invokestatic nanoTime : ()J
    //   21: lstore_3
    //   22: aload_0
    //   23: getfield transactionStartTime : J
    //   26: lstore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: lload_3
    //   30: lload_1
    //   31: lsub
    //   32: lreturn
    //   33: aload_0
    //   34: monitorexit
    //   35: lconst_0
    //   36: lreturn
    //   37: astore #5
    //   39: aload_0
    //   40: monitorexit
    //   41: aload #5
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	37	finally
  }
  
  public Map<String, Long> getGlobalBlacklist() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isGlobalBlacklistEnabled : ()Z
    //   6: ifne -> 92
    //   9: aload_0
    //   10: getfield hostsToRemove : Ljava/util/Set;
    //   13: invokeinterface isEmpty : ()Z
    //   18: ifeq -> 34
    //   21: new java/util/HashMap
    //   24: dup
    //   25: iconst_1
    //   26: invokespecial <init> : (I)V
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: areturn
    //   34: new java/util/HashMap
    //   37: astore_2
    //   38: aload_2
    //   39: invokespecial <init> : ()V
    //   42: aload_0
    //   43: getfield hostsToRemove : Ljava/util/Set;
    //   46: invokeinterface iterator : ()Ljava/util/Iterator;
    //   51: astore_1
    //   52: aload_1
    //   53: invokeinterface hasNext : ()Z
    //   58: ifeq -> 88
    //   61: aload_2
    //   62: aload_1
    //   63: invokeinterface next : ()Ljava/lang/Object;
    //   68: checkcast java/lang/String
    //   71: invokestatic currentTimeMillis : ()J
    //   74: ldc2_w 5000
    //   77: ladd
    //   78: invokestatic valueOf : (J)Ljava/lang/Long;
    //   81: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   84: pop
    //   85: goto -> 52
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_2
    //   91: areturn
    //   92: new java/util/HashMap
    //   95: astore_1
    //   96: aload_1
    //   97: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.globalBlacklist : Ljava/util/Map;
    //   100: invokeinterface size : ()I
    //   105: invokespecial <init> : (I)V
    //   108: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.globalBlacklist : Ljava/util/Map;
    //   111: astore_2
    //   112: aload_2
    //   113: monitorenter
    //   114: aload_1
    //   115: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.globalBlacklist : Ljava/util/Map;
    //   118: invokeinterface putAll : (Ljava/util/Map;)V
    //   123: aload_2
    //   124: monitorexit
    //   125: aload_1
    //   126: invokeinterface keySet : ()Ljava/util/Set;
    //   131: astore_3
    //   132: aload_3
    //   133: aload_0
    //   134: getfield hostList : Ljava/util/List;
    //   137: invokeinterface retainAll : (Ljava/util/Collection;)Z
    //   142: pop
    //   143: aload_3
    //   144: invokeinterface iterator : ()Ljava/util/Iterator;
    //   149: astore #4
    //   151: aload #4
    //   153: invokeinterface hasNext : ()Z
    //   158: ifeq -> 240
    //   161: aload #4
    //   163: invokeinterface next : ()Ljava/lang/Object;
    //   168: checkcast java/lang/String
    //   171: astore_2
    //   172: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.globalBlacklist : Ljava/util/Map;
    //   175: aload_2
    //   176: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   181: checkcast java/lang/Long
    //   184: astore #5
    //   186: aload #5
    //   188: ifnull -> 151
    //   191: aload #5
    //   193: invokevirtual longValue : ()J
    //   196: invokestatic currentTimeMillis : ()J
    //   199: lcmp
    //   200: ifge -> 151
    //   203: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.globalBlacklist : Ljava/util/Map;
    //   206: astore #5
    //   208: aload #5
    //   210: monitorenter
    //   211: getstatic com/mysql/jdbc/LoadBalancedConnectionProxy.globalBlacklist : Ljava/util/Map;
    //   214: aload_2
    //   215: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   220: pop
    //   221: aload #5
    //   223: monitorexit
    //   224: aload #4
    //   226: invokeinterface remove : ()V
    //   231: goto -> 151
    //   234: astore_1
    //   235: aload #5
    //   237: monitorexit
    //   238: aload_1
    //   239: athrow
    //   240: aload_3
    //   241: invokeinterface size : ()I
    //   246: aload_0
    //   247: getfield hostList : Ljava/util/List;
    //   250: invokeinterface size : ()I
    //   255: if_icmpne -> 271
    //   258: new java/util/HashMap
    //   261: dup
    //   262: iconst_1
    //   263: invokespecial <init> : (I)V
    //   266: astore_1
    //   267: aload_0
    //   268: monitorexit
    //   269: aload_1
    //   270: areturn
    //   271: aload_0
    //   272: monitorexit
    //   273: aload_1
    //   274: areturn
    //   275: astore_1
    //   276: aload_2
    //   277: monitorexit
    //   278: aload_1
    //   279: athrow
    //   280: astore_1
    //   281: aload_0
    //   282: monitorexit
    //   283: aload_1
    //   284: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	280	finally
    //   34	52	280	finally
    //   52	85	280	finally
    //   92	114	280	finally
    //   114	125	275	finally
    //   125	151	280	finally
    //   151	186	280	finally
    //   191	211	280	finally
    //   211	224	234	finally
    //   224	231	280	finally
    //   235	238	234	finally
    //   238	240	280	finally
    //   240	267	280	finally
    //   276	278	275	finally
    //   278	280	280	finally
  }
  
  public MySQLConnection getNewWrapperForThisAsConnection() throws SQLException {
    return (Util.isJdbc4() || JDBC_4_LB_CONNECTION_CTOR != null) ? (MySQLConnection)Util.handleNewInstance(JDBC_4_LB_CONNECTION_CTOR, new Object[] { this }, null) : new LoadBalancedMySQLConnection(this);
  }
  
  public long getTotalPhysicalConnectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield totalPhysicalConnections : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public long getTransactionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield transactionCount : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean inTransaction() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield inTransaction : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void invalidateConnection(MySQLConnection paramMySQLConnection) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial invalidateConnection : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   7: aload_0
    //   8: invokevirtual isGlobalBlacklistEnabled : ()Z
    //   11: ifeq -> 31
    //   14: aload_0
    //   15: aload_0
    //   16: getfield connectionsToHostsMap : Ljava/util/Map;
    //   19: aload_1
    //   20: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   25: checkcast java/lang/String
    //   28: invokevirtual addToGlobalBlacklist : (Ljava/lang/String;)V
    //   31: aload_0
    //   32: getfield liveConnections : Ljava/util/Map;
    //   35: aload_0
    //   36: getfield connectionsToHostsMap : Ljava/util/Map;
    //   39: aload_1
    //   40: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   45: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   50: pop
    //   51: aload_0
    //   52: getfield connectionsToHostsMap : Ljava/util/Map;
    //   55: aload_1
    //   56: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   61: astore_1
    //   62: aload_1
    //   63: ifnull -> 120
    //   66: aload_0
    //   67: getfield hostsToListIndexMap : Ljava/util/Map;
    //   70: aload_1
    //   71: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   76: ifeq -> 120
    //   79: aload_0
    //   80: getfield hostsToListIndexMap : Ljava/util/Map;
    //   83: aload_1
    //   84: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   89: checkcast java/lang/Integer
    //   92: invokevirtual intValue : ()I
    //   95: istore_2
    //   96: aload_0
    //   97: getfield responseTimes : [J
    //   100: astore_3
    //   101: aload_3
    //   102: monitorenter
    //   103: aload_0
    //   104: getfield responseTimes : [J
    //   107: iload_2
    //   108: lconst_0
    //   109: lastore
    //   110: aload_3
    //   111: monitorexit
    //   112: goto -> 120
    //   115: astore_1
    //   116: aload_3
    //   117: monitorexit
    //   118: aload_1
    //   119: athrow
    //   120: aload_0
    //   121: monitorexit
    //   122: return
    //   123: astore_1
    //   124: aload_0
    //   125: monitorexit
    //   126: aload_1
    //   127: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	123	finally
    //   31	62	123	finally
    //   66	103	123	finally
    //   103	112	115	finally
    //   116	118	115	finally
    //   118	120	123	finally
  }
  
  public Object invokeMore(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: invokevirtual getName : ()Ljava/lang/String;
    //   6: astore #6
    //   8: aload_0
    //   9: getfield isClosed : Z
    //   12: istore #5
    //   14: aconst_null
    //   15: astore_1
    //   16: iload #5
    //   18: ifeq -> 131
    //   21: aload_0
    //   22: aload_2
    //   23: invokevirtual allowedOnClosedConnection : (Ljava/lang/reflect/Method;)Z
    //   26: ifne -> 131
    //   29: aload_2
    //   30: invokevirtual getExceptionTypes : ()[Ljava/lang/Class;
    //   33: arraylength
    //   34: ifle -> 131
    //   37: aload_0
    //   38: getfield autoReconnect : Z
    //   41: ifeq -> 73
    //   44: aload_0
    //   45: getfield closedExplicitly : Z
    //   48: ifne -> 73
    //   51: aload_0
    //   52: aconst_null
    //   53: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   56: aload_0
    //   57: invokevirtual pickNewConnection : ()V
    //   60: aload_0
    //   61: iconst_0
    //   62: putfield isClosed : Z
    //   65: aload_0
    //   66: aconst_null
    //   67: putfield closedReason : Ljava/lang/String;
    //   70: goto -> 131
    //   73: ldc_w 'No operations allowed after connection closed.'
    //   76: astore_1
    //   77: aload_0
    //   78: getfield closedReason : Ljava/lang/String;
    //   81: ifnull -> 122
    //   84: new java/lang/StringBuilder
    //   87: astore_1
    //   88: aload_1
    //   89: invokespecial <init> : ()V
    //   92: aload_1
    //   93: ldc_w 'No operations allowed after connection closed.'
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_1
    //   101: ldc_w ' '
    //   104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: aload_1
    //   109: aload_0
    //   110: getfield closedReason : Ljava/lang/String;
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: aload_1
    //   118: invokevirtual toString : ()Ljava/lang/String;
    //   121: astore_1
    //   122: aload_1
    //   123: ldc_w '08003'
    //   126: aconst_null
    //   127: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   130: athrow
    //   131: aload_0
    //   132: getfield inTransaction : Z
    //   135: ifne -> 160
    //   138: aload_0
    //   139: iconst_1
    //   140: putfield inTransaction : Z
    //   143: aload_0
    //   144: invokestatic nanoTime : ()J
    //   147: putfield transactionStartTime : J
    //   150: aload_0
    //   151: aload_0
    //   152: getfield transactionCount : J
    //   155: lconst_1
    //   156: ladd
    //   157: putfield transactionCount : J
    //   160: aload_2
    //   161: aload_0
    //   162: getfield thisAsConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   165: aload_3
    //   166: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   169: astore_3
    //   170: aload_3
    //   171: astore_1
    //   172: aload_3
    //   173: ifnull -> 211
    //   176: aload_3
    //   177: astore_1
    //   178: aload_3
    //   179: instanceof com/mysql/jdbc/Statement
    //   182: ifeq -> 197
    //   185: aload_3
    //   186: astore_1
    //   187: aload_3
    //   188: checkcast com/mysql/jdbc/Statement
    //   191: aload_0
    //   192: invokeinterface setPingTarget : (Lcom/mysql/jdbc/PingTarget;)V
    //   197: aload_3
    //   198: astore_1
    //   199: aload_0
    //   200: aload_2
    //   201: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   204: aload_3
    //   205: invokevirtual proxyIfReturnTypeIsJdbcInterface : (Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
    //   208: astore_2
    //   209: aload_2
    //   210: astore_1
    //   211: ldc_w 'commit'
    //   214: aload #6
    //   216: invokevirtual equals : (Ljava/lang/Object;)Z
    //   219: ifne -> 235
    //   222: aload_1
    //   223: astore_2
    //   224: ldc_w 'rollback'
    //   227: aload #6
    //   229: invokevirtual equals : (Ljava/lang/Object;)Z
    //   232: ifeq -> 477
    //   235: aload_0
    //   236: iconst_0
    //   237: putfield inTransaction : Z
    //   240: aload_0
    //   241: getfield connectionsToHostsMap : Ljava/util/Map;
    //   244: aload_0
    //   245: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   248: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   253: checkcast java/lang/String
    //   256: astore_3
    //   257: aload_1
    //   258: astore_2
    //   259: aload_3
    //   260: ifnull -> 336
    //   263: aload_0
    //   264: getfield responseTimes : [J
    //   267: astore_2
    //   268: aload_2
    //   269: monitorenter
    //   270: aload_0
    //   271: getfield hostsToListIndexMap : Ljava/util/Map;
    //   274: aload_3
    //   275: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   280: checkcast java/lang/Integer
    //   283: astore #6
    //   285: aload #6
    //   287: ifnull -> 324
    //   290: aload #6
    //   292: invokevirtual intValue : ()I
    //   295: istore #4
    //   297: aload_0
    //   298: getfield responseTimes : [J
    //   301: astore_3
    //   302: iload #4
    //   304: aload_3
    //   305: arraylength
    //   306: if_icmpge -> 324
    //   309: aload_3
    //   310: aload #6
    //   312: invokevirtual intValue : ()I
    //   315: invokestatic nanoTime : ()J
    //   318: aload_0
    //   319: getfield transactionStartTime : J
    //   322: lsub
    //   323: lastore
    //   324: aload_2
    //   325: monitorexit
    //   326: aload_1
    //   327: astore_2
    //   328: goto -> 336
    //   331: astore_1
    //   332: aload_2
    //   333: monitorexit
    //   334: aload_1
    //   335: athrow
    //   336: aload_0
    //   337: invokevirtual pickNewConnection : ()V
    //   340: goto -> 477
    //   343: astore_1
    //   344: goto -> 481
    //   347: astore_2
    //   348: aload_0
    //   349: aload_2
    //   350: invokevirtual dealWithInvocationException : (Ljava/lang/reflect/InvocationTargetException;)V
    //   353: ldc_w 'commit'
    //   356: aload #6
    //   358: invokevirtual equals : (Ljava/lang/Object;)Z
    //   361: ifne -> 377
    //   364: aload_1
    //   365: astore_2
    //   366: ldc_w 'rollback'
    //   369: aload #6
    //   371: invokevirtual equals : (Ljava/lang/Object;)Z
    //   374: ifeq -> 477
    //   377: aload_0
    //   378: iconst_0
    //   379: putfield inTransaction : Z
    //   382: aload_0
    //   383: getfield connectionsToHostsMap : Ljava/util/Map;
    //   386: aload_0
    //   387: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   390: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   395: checkcast java/lang/String
    //   398: astore_3
    //   399: aload_1
    //   400: astore_2
    //   401: aload_3
    //   402: ifnull -> 336
    //   405: aload_0
    //   406: getfield responseTimes : [J
    //   409: astore_2
    //   410: aload_2
    //   411: monitorenter
    //   412: aload_0
    //   413: getfield hostsToListIndexMap : Ljava/util/Map;
    //   416: aload_3
    //   417: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   422: checkcast java/lang/Integer
    //   425: astore_3
    //   426: aload_3
    //   427: ifnull -> 465
    //   430: aload_3
    //   431: invokevirtual intValue : ()I
    //   434: istore #4
    //   436: aload_0
    //   437: getfield responseTimes : [J
    //   440: astore #6
    //   442: iload #4
    //   444: aload #6
    //   446: arraylength
    //   447: if_icmpge -> 465
    //   450: aload #6
    //   452: aload_3
    //   453: invokevirtual intValue : ()I
    //   456: invokestatic nanoTime : ()J
    //   459: aload_0
    //   460: getfield transactionStartTime : J
    //   463: lsub
    //   464: lastore
    //   465: aload_2
    //   466: monitorexit
    //   467: aload_1
    //   468: astore_2
    //   469: goto -> 336
    //   472: astore_1
    //   473: aload_2
    //   474: monitorexit
    //   475: aload_1
    //   476: athrow
    //   477: aload_0
    //   478: monitorexit
    //   479: aload_2
    //   480: areturn
    //   481: ldc_w 'commit'
    //   484: aload #6
    //   486: invokevirtual equals : (Ljava/lang/Object;)Z
    //   489: ifne -> 503
    //   492: ldc_w 'rollback'
    //   495: aload #6
    //   497: invokevirtual equals : (Ljava/lang/Object;)Z
    //   500: ifeq -> 604
    //   503: aload_0
    //   504: iconst_0
    //   505: putfield inTransaction : Z
    //   508: aload_0
    //   509: getfield connectionsToHostsMap : Ljava/util/Map;
    //   512: aload_0
    //   513: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   516: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   521: checkcast java/lang/String
    //   524: astore_3
    //   525: aload_3
    //   526: ifnull -> 600
    //   529: aload_0
    //   530: getfield responseTimes : [J
    //   533: astore_2
    //   534: aload_2
    //   535: monitorenter
    //   536: aload_0
    //   537: getfield hostsToListIndexMap : Ljava/util/Map;
    //   540: aload_3
    //   541: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   546: checkcast java/lang/Integer
    //   549: astore #6
    //   551: aload #6
    //   553: ifnull -> 590
    //   556: aload #6
    //   558: invokevirtual intValue : ()I
    //   561: istore #4
    //   563: aload_0
    //   564: getfield responseTimes : [J
    //   567: astore_3
    //   568: iload #4
    //   570: aload_3
    //   571: arraylength
    //   572: if_icmpge -> 590
    //   575: aload_3
    //   576: aload #6
    //   578: invokevirtual intValue : ()I
    //   581: invokestatic nanoTime : ()J
    //   584: aload_0
    //   585: getfield transactionStartTime : J
    //   588: lsub
    //   589: lastore
    //   590: aload_2
    //   591: monitorexit
    //   592: goto -> 600
    //   595: astore_1
    //   596: aload_2
    //   597: monitorexit
    //   598: aload_1
    //   599: athrow
    //   600: aload_0
    //   601: invokevirtual pickNewConnection : ()V
    //   604: aload_1
    //   605: athrow
    //   606: astore_1
    //   607: aload_0
    //   608: monitorexit
    //   609: aload_1
    //   610: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	606	finally
    //   21	70	606	finally
    //   77	122	606	finally
    //   122	131	606	finally
    //   131	160	606	finally
    //   160	170	347	java/lang/reflect/InvocationTargetException
    //   160	170	343	finally
    //   178	185	347	java/lang/reflect/InvocationTargetException
    //   178	185	343	finally
    //   187	197	347	java/lang/reflect/InvocationTargetException
    //   187	197	343	finally
    //   199	209	347	java/lang/reflect/InvocationTargetException
    //   199	209	343	finally
    //   211	222	606	finally
    //   224	235	606	finally
    //   235	257	606	finally
    //   263	270	606	finally
    //   270	285	331	finally
    //   290	324	331	finally
    //   324	326	331	finally
    //   332	334	331	finally
    //   334	336	606	finally
    //   336	340	606	finally
    //   348	353	343	finally
    //   353	364	606	finally
    //   366	377	606	finally
    //   377	399	606	finally
    //   405	412	606	finally
    //   412	426	472	finally
    //   430	465	472	finally
    //   465	467	472	finally
    //   473	475	472	finally
    //   475	477	606	finally
    //   481	503	606	finally
    //   503	525	606	finally
    //   529	536	606	finally
    //   536	551	595	finally
    //   556	590	595	finally
    //   590	592	595	finally
    //   596	598	595	finally
    //   598	600	606	finally
    //   600	604	606	finally
    //   604	606	606	finally
  }
  
  public boolean isGlobalBlacklistEnabled() {
    boolean bool;
    if (this.globalBlacklistTimeout > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isMasterConnection() {
    return true;
  }
  
  public void pickNewConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isClosed : Z
    //   6: ifeq -> 23
    //   9: aload_0
    //   10: getfield closedExplicitly : Z
    //   13: istore #4
    //   15: iload #4
    //   17: ifeq -> 23
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   27: astore #5
    //   29: aload #5
    //   31: ifnonnull -> 79
    //   34: aload_0
    //   35: aload_0
    //   36: getfield balancer : Lcom/mysql/jdbc/BalanceStrategy;
    //   39: aload_0
    //   40: aload_0
    //   41: getfield hostList : Ljava/util/List;
    //   44: invokestatic unmodifiableList : (Ljava/util/List;)Ljava/util/List;
    //   47: aload_0
    //   48: getfield liveConnections : Ljava/util/Map;
    //   51: invokestatic unmodifiableMap : (Ljava/util/Map;)Ljava/util/Map;
    //   54: aload_0
    //   55: getfield responseTimes : [J
    //   58: invokevirtual clone : ()Ljava/lang/Object;
    //   61: checkcast [J
    //   64: aload_0
    //   65: getfield retriesAllDown : I
    //   68: invokeinterface pickConnection : (Lcom/mysql/jdbc/LoadBalancedConnectionProxy;Ljava/util/List;Ljava/util/Map;[JI)Lcom/mysql/jdbc/ConnectionImpl;
    //   73: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   76: aload_0
    //   77: monitorexit
    //   78: return
    //   79: aload #5
    //   81: invokeinterface isClosed : ()Z
    //   86: ifeq -> 93
    //   89: aload_0
    //   90: invokevirtual invalidateCurrentConnection : ()V
    //   93: aload_0
    //   94: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   97: invokeinterface getLoadBalancePingTimeout : ()I
    //   102: istore_3
    //   103: aload_0
    //   104: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   107: invokeinterface getLoadBalanceValidateConnectionOnSwapServer : ()Z
    //   112: istore #4
    //   114: iconst_0
    //   115: istore_1
    //   116: aload_0
    //   117: getfield hostList : Ljava/util/List;
    //   120: invokeinterface size : ()I
    //   125: istore_2
    //   126: iload_1
    //   127: iload_2
    //   128: if_icmpge -> 272
    //   131: aconst_null
    //   132: astore #5
    //   134: aload_0
    //   135: getfield balancer : Lcom/mysql/jdbc/BalanceStrategy;
    //   138: aload_0
    //   139: aload_0
    //   140: getfield hostList : Ljava/util/List;
    //   143: invokestatic unmodifiableList : (Ljava/util/List;)Ljava/util/List;
    //   146: aload_0
    //   147: getfield liveConnections : Ljava/util/Map;
    //   150: invokestatic unmodifiableMap : (Ljava/util/Map;)Ljava/util/Map;
    //   153: aload_0
    //   154: getfield responseTimes : [J
    //   157: invokevirtual clone : ()Ljava/lang/Object;
    //   160: checkcast [J
    //   163: aload_0
    //   164: getfield retriesAllDown : I
    //   167: invokeinterface pickConnection : (Lcom/mysql/jdbc/LoadBalancedConnectionProxy;Ljava/util/List;Ljava/util/Map;[JI)Lcom/mysql/jdbc/ConnectionImpl;
    //   172: astore #6
    //   174: aload #6
    //   176: astore #5
    //   178: aload_0
    //   179: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   182: ifnull -> 231
    //   185: iload #4
    //   187: ifeq -> 217
    //   190: iload_3
    //   191: ifne -> 206
    //   194: aload #6
    //   196: astore #5
    //   198: aload #6
    //   200: invokevirtual ping : ()V
    //   203: goto -> 217
    //   206: aload #6
    //   208: astore #5
    //   210: aload #6
    //   212: iconst_1
    //   213: iload_3
    //   214: invokevirtual pingInternal : (ZI)V
    //   217: aload #6
    //   219: astore #5
    //   221: aload_0
    //   222: aload_0
    //   223: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   226: aload #6
    //   228: invokevirtual syncSessionState : (Lcom/mysql/jdbc/Connection;Lcom/mysql/jdbc/Connection;)V
    //   231: aload #6
    //   233: astore #5
    //   235: aload_0
    //   236: aload #6
    //   238: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   241: aload_0
    //   242: monitorexit
    //   243: return
    //   244: astore #6
    //   246: aload_0
    //   247: aload #6
    //   249: invokevirtual shouldExceptionTriggerConnectionSwitch : (Ljava/lang/Throwable;)Z
    //   252: ifeq -> 266
    //   255: aload #5
    //   257: ifnull -> 266
    //   260: aload_0
    //   261: aload #5
    //   263: invokevirtual invalidateConnection : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   266: iinc #1, 1
    //   269: goto -> 126
    //   272: aload_0
    //   273: iconst_1
    //   274: putfield isClosed : Z
    //   277: aload_0
    //   278: ldc_w 'Connection closed after inability to pick valid new connection during load-balance.'
    //   281: putfield closedReason : Ljava/lang/String;
    //   284: aload_0
    //   285: monitorexit
    //   286: return
    //   287: astore #5
    //   289: aload_0
    //   290: monitorexit
    //   291: aload #5
    //   293: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	287	finally
    //   23	29	287	finally
    //   34	76	287	finally
    //   79	93	287	finally
    //   93	114	287	finally
    //   116	126	287	finally
    //   134	174	244	java/sql/SQLException
    //   134	174	287	finally
    //   178	185	244	java/sql/SQLException
    //   178	185	287	finally
    //   198	203	244	java/sql/SQLException
    //   198	203	287	finally
    //   210	217	244	java/sql/SQLException
    //   210	217	287	finally
    //   221	231	244	java/sql/SQLException
    //   221	231	287	finally
    //   235	241	244	java/sql/SQLException
    //   235	241	287	finally
    //   246	255	287	finally
    //   260	266	287	finally
    //   272	284	287	finally
  }
  
  public void propagateProxyDown(MySQLConnection paramMySQLConnection) {
    Iterator<ConnectionImpl> iterator = this.liveConnections.values().iterator();
    while (iterator.hasNext())
      ((ConnectionImpl)iterator.next()).setProxy(paramMySQLConnection); 
  }
  
  public void removeHost(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connectionGroup : Lcom/mysql/jdbc/ConnectionGroup;
    //   6: astore #4
    //   8: aload #4
    //   10: ifnull -> 54
    //   13: aload #4
    //   15: invokevirtual getInitialHosts : ()Ljava/util/Collection;
    //   18: invokeinterface size : ()I
    //   23: iconst_1
    //   24: if_icmpne -> 54
    //   27: aload_0
    //   28: getfield connectionGroup : Lcom/mysql/jdbc/ConnectionGroup;
    //   31: invokevirtual getInitialHosts : ()Ljava/util/Collection;
    //   34: aload_1
    //   35: invokeinterface contains : (Ljava/lang/Object;)Z
    //   40: ifne -> 46
    //   43: goto -> 54
    //   46: ldc_w 'Cannot remove only configured host.'
    //   49: aconst_null
    //   50: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   53: athrow
    //   54: aload_0
    //   55: getfield hostsToRemove : Ljava/util/Set;
    //   58: aload_1
    //   59: invokeinterface add : (Ljava/lang/Object;)Z
    //   64: pop
    //   65: aload_0
    //   66: getfield connectionsToHostsMap : Ljava/util/Map;
    //   69: aload_0
    //   70: getfield liveConnections : Ljava/util/Map;
    //   73: aload_1
    //   74: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   79: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   84: pop
    //   85: aload_0
    //   86: getfield hostsToListIndexMap : Ljava/util/Map;
    //   89: aload_1
    //   90: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   95: ifnull -> 238
    //   98: aload_0
    //   99: getfield responseTimes : [J
    //   102: arraylength
    //   103: iconst_1
    //   104: isub
    //   105: newarray long
    //   107: astore #5
    //   109: iconst_0
    //   110: istore_2
    //   111: aload_0
    //   112: getfield hostList : Ljava/util/List;
    //   115: invokeinterface iterator : ()Ljava/util/Iterator;
    //   120: astore #8
    //   122: aload #8
    //   124: invokeinterface hasNext : ()Z
    //   129: ifeq -> 232
    //   132: aload #8
    //   134: invokeinterface next : ()Ljava/lang/Object;
    //   139: checkcast java/lang/String
    //   142: astore #7
    //   144: aload_0
    //   145: getfield hostsToRemove : Ljava/util/Set;
    //   148: aload #7
    //   150: invokeinterface contains : (Ljava/lang/Object;)Z
    //   155: ifne -> 122
    //   158: aload_0
    //   159: getfield hostsToListIndexMap : Ljava/util/Map;
    //   162: aload #7
    //   164: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   169: checkcast java/lang/Integer
    //   172: astore #4
    //   174: aload #4
    //   176: ifnull -> 210
    //   179: aload #4
    //   181: invokevirtual intValue : ()I
    //   184: istore_3
    //   185: aload_0
    //   186: getfield responseTimes : [J
    //   189: astore #6
    //   191: iload_3
    //   192: aload #6
    //   194: arraylength
    //   195: if_icmpge -> 210
    //   198: aload #5
    //   200: iload_2
    //   201: aload #6
    //   203: aload #4
    //   205: invokevirtual intValue : ()I
    //   208: laload
    //   209: lastore
    //   210: aload_0
    //   211: getfield hostsToListIndexMap : Ljava/util/Map;
    //   214: aload #7
    //   216: iload_2
    //   217: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   220: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   225: pop
    //   226: iinc #2, 1
    //   229: goto -> 122
    //   232: aload_0
    //   233: aload #5
    //   235: putfield responseTimes : [J
    //   238: aload_1
    //   239: aload_0
    //   240: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   243: invokeinterface getHostPortPair : ()Ljava/lang/String;
    //   248: invokevirtual equals : (Ljava/lang/Object;)Z
    //   251: ifeq -> 266
    //   254: aload_0
    //   255: aload_0
    //   256: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   259: invokevirtual invalidateConnection : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   262: aload_0
    //   263: invokevirtual pickNewConnection : ()V
    //   266: aload_0
    //   267: monitorexit
    //   268: return
    //   269: astore_1
    //   270: aload_0
    //   271: monitorexit
    //   272: aload_1
    //   273: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	269	finally
    //   13	43	269	finally
    //   46	54	269	finally
    //   54	109	269	finally
    //   111	122	269	finally
    //   122	174	269	finally
    //   179	210	269	finally
    //   210	226	269	finally
    //   232	238	269	finally
    //   238	266	269	finally
  }
  
  public void removeHostWhenNotInUse(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield hostRemovalGracePeriod : I
    //   4: istore_3
    //   5: iload_3
    //   6: ifgt -> 15
    //   9: aload_0
    //   10: aload_1
    //   11: invokevirtual removeHost : (Ljava/lang/String;)V
    //   14: return
    //   15: iload_3
    //   16: istore_2
    //   17: iload_3
    //   18: sipush #1000
    //   21: if_icmple -> 28
    //   24: sipush #1000
    //   27: istore_2
    //   28: aload_0
    //   29: monitorenter
    //   30: invokestatic currentTimeMillis : ()J
    //   33: lstore #6
    //   35: aload_0
    //   36: getfield hostRemovalGracePeriod : I
    //   39: i2l
    //   40: lstore #8
    //   42: iload_2
    //   43: i2l
    //   44: lstore #4
    //   46: aload_0
    //   47: aload_1
    //   48: lload #6
    //   50: lload #8
    //   52: ladd
    //   53: lload #4
    //   55: ladd
    //   56: invokevirtual addToGlobalBlacklist : (Ljava/lang/String;J)V
    //   59: invokestatic currentTimeMillis : ()J
    //   62: lstore #6
    //   64: invokestatic currentTimeMillis : ()J
    //   67: aload_0
    //   68: getfield hostRemovalGracePeriod : I
    //   71: i2l
    //   72: lload #6
    //   74: ladd
    //   75: lcmp
    //   76: ifge -> 122
    //   79: aload_0
    //   80: getfield hostsToRemove : Ljava/util/Set;
    //   83: aload_1
    //   84: invokeinterface add : (Ljava/lang/Object;)Z
    //   89: pop
    //   90: aload_1
    //   91: aload_0
    //   92: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   95: invokeinterface getHostPortPair : ()Ljava/lang/String;
    //   100: invokevirtual equals : (Ljava/lang/Object;)Z
    //   103: ifne -> 114
    //   106: aload_0
    //   107: aload_1
    //   108: invokevirtual removeHost : (Ljava/lang/String;)V
    //   111: aload_0
    //   112: monitorexit
    //   113: return
    //   114: lload #4
    //   116: invokestatic sleep : (J)V
    //   119: goto -> 64
    //   122: aload_0
    //   123: monitorexit
    //   124: aload_0
    //   125: aload_1
    //   126: invokevirtual removeHost : (Ljava/lang/String;)V
    //   129: return
    //   130: astore_1
    //   131: aload_0
    //   132: monitorexit
    //   133: aload_1
    //   134: athrow
    //   135: astore #10
    //   137: goto -> 64
    // Exception table:
    //   from	to	target	type
    //   30	42	130	finally
    //   46	64	130	finally
    //   64	113	130	finally
    //   114	119	135	java/lang/InterruptedException
    //   114	119	130	finally
    //   122	124	130	finally
    //   131	133	130	finally
  }
  
  public boolean shouldExceptionTriggerConnectionSwitch(Throwable paramThrowable) {
    boolean bool;
    if (paramThrowable instanceof SQLException && this.exceptionChecker.shouldExceptionTriggerFailover((SQLException)paramThrowable)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void syncSessionState(Connection paramConnection1, Connection paramConnection2, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: checkcast com/mysql/jdbc/MySQLConnection
    //   4: invokeinterface getStatementInterceptorsInstances : ()Ljava/util/List;
    //   9: invokeinterface iterator : ()Ljava/util/Iterator;
    //   14: astore #5
    //   16: aload #5
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 61
    //   26: aload #5
    //   28: invokeinterface next : ()Ljava/lang/Object;
    //   33: checkcast com/mysql/jdbc/StatementInterceptorV2
    //   36: astore #4
    //   38: aload #4
    //   40: instanceof com/mysql/jdbc/LoadBalancedAutoCommitInterceptor
    //   43: ifeq -> 16
    //   46: aload #4
    //   48: checkcast com/mysql/jdbc/LoadBalancedAutoCommitInterceptor
    //   51: astore #4
    //   53: aload #4
    //   55: invokevirtual pauseCounters : ()V
    //   58: goto -> 64
    //   61: aconst_null
    //   62: astore #4
    //   64: aload_0
    //   65: aload_1
    //   66: aload_2
    //   67: iload_3
    //   68: invokespecial syncSessionState : (Lcom/mysql/jdbc/Connection;Lcom/mysql/jdbc/Connection;Z)V
    //   71: aload #4
    //   73: ifnull -> 81
    //   76: aload #4
    //   78: invokevirtual resumeCounters : ()V
    //   81: return
  }
  
  static {
    if (Util.isJdbc4()) {
      try {
        JDBC_4_LB_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4LoadBalancedMySQLConnection").getConstructor(new Class[] { LoadBalancedConnectionProxy.class });
        INTERFACES_TO_PROXY = new Class[] { LoadBalancedConnection.class, Class.forName("com.mysql.jdbc.JDBC4MySQLConnection") };
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      INTERFACES_TO_PROXY = new Class[] { LoadBalancedConnection.class };
    } 
  }
  
  public static class NullLoadBalancedConnectionProxy implements InvocationHandler {
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      param1Object = SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.unusableConnection"), "25000", 1000001, true, (ExceptionInterceptor)null);
      Class[] arrayOfClass = param1Method.getExceptionTypes();
      int i = arrayOfClass.length;
      byte b = 0;
      while (b < i) {
        if (!arrayOfClass[b].isAssignableFrom(param1Object.getClass())) {
          b++;
          continue;
        } 
        throw param1Object;
      } 
      throw new IllegalStateException(param1Object.getMessage(), param1Object);
    }
  }
}
