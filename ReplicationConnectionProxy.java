package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ReplicationConnectionProxy extends MultiHostConnectionProxy implements PingTarget {
  private static Class<?>[] INTERFACES_TO_PROXY;
  
  private static Constructor<?> JDBC_4_REPL_CONNECTION_CTOR;
  
  public boolean allowMasterDownConnections = false;
  
  public boolean allowSlaveDownConnections = false;
  
  public ReplicationConnectionGroup connectionGroup;
  
  private long connectionGroupID = -1L;
  
  private NonRegisteringDriver driver;
  
  public boolean enableJMX = false;
  
  public LoadBalancedConnection masterConnection;
  
  private List<String> masterHosts;
  
  private Properties masterProperties;
  
  public boolean readFromMasterWhenNoSlaves = false;
  
  public boolean readFromMasterWhenNoSlavesOriginal = false;
  
  public boolean readOnly = false;
  
  private List<String> slaveHosts;
  
  private Properties slaveProperties;
  
  public LoadBalancedConnection slavesConnection;
  
  private ReplicationConnection thisAsReplicationConnection = (ReplicationConnection)this.thisAsConnection;
  
  static {
    if (Util.isJdbc4()) {
      try {
        JDBC_4_REPL_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4ReplicationMySQLConnection").getConstructor(new Class[] { ReplicationConnectionProxy.class });
        INTERFACES_TO_PROXY = new Class[] { ReplicationConnection.class, Class.forName("com.mysql.jdbc.JDBC4MySQLConnection") };
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      INTERFACES_TO_PROXY = new Class[] { ReplicationConnection.class };
    } 
  }
  
  private ReplicationConnectionProxy(List<String> paramList1, Properties paramProperties1, List<String> paramList2, Properties paramProperties2) throws SQLException {
    String str = paramProperties1.getProperty("replicationEnableJMX", "false");
    try {
      this.enableJMX = Boolean.parseBoolean(str);
      str = paramProperties1.getProperty("allowMasterDownConnections", "false");
      try {
        this.allowMasterDownConnections = Boolean.parseBoolean(str);
        str = paramProperties1.getProperty("allowSlaveDownConnections", "false");
        try {
          this.allowSlaveDownConnections = Boolean.parseBoolean(str);
          str = paramProperties1.getProperty("readFromMasterWhenNoSlaves");
          try {
            this.readFromMasterWhenNoSlavesOriginal = Boolean.parseBoolean(str);
            str = paramProperties1.getProperty("replicationConnectionGroup", null);
            if (str != null) {
              this.connectionGroup = ReplicationConnectionGroupManager.getConnectionGroupInstance(str);
              if (this.enableJMX)
                ReplicationConnectionGroupManager.registerJmx(); 
              this.connectionGroupID = this.connectionGroup.registerReplicationConnection(this.thisAsReplicationConnection, paramList1, paramList2);
              this.slaveHosts = new ArrayList<String>(this.connectionGroup.getSlaveHosts());
              this.masterHosts = new ArrayList<String>(this.connectionGroup.getMasterHosts());
            } else {
              this.slaveHosts = new ArrayList<String>(paramList2);
              this.masterHosts = new ArrayList<String>(paramList1);
            } 
            this.driver = new NonRegisteringDriver();
            this.slaveProperties = paramProperties2;
            this.masterProperties = paramProperties1;
            resetReadFromMasterWhenNoSlaves();
            try {
              initializeSlavesConnection();
            } catch (SQLException sQLException1) {
              if (!this.allowSlaveDownConnections) {
                ReplicationConnectionGroup replicationConnectionGroup = this.connectionGroup;
                if (replicationConnectionGroup != null)
                  replicationConnectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
                throw sQLException1;
              } 
            } 
            try {
              this.currentConnection = initializeMasterConnection();
              paramList1 = null;
            } catch (SQLException sQLException) {}
            if (this.currentConnection == null) {
              if (this.allowMasterDownConnections) {
                LoadBalancedConnection loadBalancedConnection = this.slavesConnection;
                if (loadBalancedConnection != null) {
                  this.readOnly = true;
                  this.currentConnection = loadBalancedConnection;
                  return;
                } 
              } 
              ReplicationConnectionGroup replicationConnectionGroup = this.connectionGroup;
              if (replicationConnectionGroup != null)
                replicationConnectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
              if (sQLException != null)
                throw sQLException; 
              throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.initializationWithEmptyHostsLists"), "S1009", null);
            } 
            return;
          } catch (Exception exception) {
            throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForReadFromMasterWhenNoSlaves", new Object[] { str }), "S1009", null);
          } 
        } catch (Exception exception) {
          throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForAllowSlaveDownConnections", new Object[] { str }), "S1009", null);
        } 
      } catch (Exception exception) {
        throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForAllowMasterDownConnections", new Object[] { str }), "S1009", null);
      } 
    } catch (Exception exception) {
      throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForReplicationEnableJMX", new Object[] { str }), "S1009", null);
    } 
  }
  
  private String buildURL(List<String> paramList, Properties paramProperties) {
    StringBuilder stringBuilder = new StringBuilder("jdbc:mysql:loadbalance://");
    Iterator<String> iterator = paramList.iterator();
    for (boolean bool = true; iterator.hasNext(); bool = false) {
      String str1 = iterator.next();
      if (!bool)
        stringBuilder.append(','); 
      stringBuilder.append(str1);
    } 
    stringBuilder.append("/");
    String str = paramProperties.getProperty("DBNAME");
    if (str != null)
      stringBuilder.append(str); 
    return stringBuilder.toString();
  }
  
  private void checkConnectionCapabilityForMethod(Method paramMethod) throws Throwable {
    if (!this.masterHosts.isEmpty() || !this.slaveHosts.isEmpty() || ReplicationConnection.class.isAssignableFrom(paramMethod.getDeclaringClass()))
      return; 
    throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.noHostsInconsistentState"), "25000", 1000002, true, null);
  }
  
  public static ReplicationConnection createProxyInstance(List<String> paramList1, Properties paramProperties1, List<String> paramList2, Properties paramProperties2) throws SQLException {
    ReplicationConnectionProxy replicationConnectionProxy = new ReplicationConnectionProxy(paramList1, paramProperties1, paramList2, paramProperties2);
    return (ReplicationConnection)Proxy.newProxyInstance(ReplicationConnection.class.getClassLoader(), INTERFACES_TO_PROXY, replicationConnectionProxy);
  }
  
  private MySQLConnection initializeMasterConnection() throws SQLException {
    this.masterConnection = null;
    if (this.masterHosts.size() == 0)
      return null; 
    LoadBalancedConnection loadBalancedConnection = (LoadBalancedConnection)this.driver.connect(buildURL(this.masterHosts, this.masterProperties), this.masterProperties);
    loadBalancedConnection.setProxy(getProxy());
    this.masterConnection = loadBalancedConnection;
    return loadBalancedConnection;
  }
  
  private MySQLConnection initializeSlavesConnection() throws SQLException {
    this.slavesConnection = null;
    if (this.slaveHosts.size() == 0)
      return null; 
    LoadBalancedConnection loadBalancedConnection = (LoadBalancedConnection)this.driver.connect(buildURL(this.slaveHosts, this.slaveProperties), this.slaveProperties);
    loadBalancedConnection.setProxy(getProxy());
    loadBalancedConnection.setReadOnly(true);
    this.slavesConnection = loadBalancedConnection;
    return loadBalancedConnection;
  }
  
  private void resetReadFromMasterWhenNoSlaves() {
    boolean bool;
    if (this.slaveHosts.isEmpty() || this.readFromMasterWhenNoSlavesOriginal) {
      bool = true;
    } else {
      bool = false;
    } 
    this.readFromMasterWhenNoSlaves = bool;
  }
  
  private boolean switchToMasterConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 22
    //   11: aload_2
    //   12: invokeinterface isClosed : ()Z
    //   17: istore_1
    //   18: iload_1
    //   19: ifeq -> 35
    //   22: aload_0
    //   23: invokespecial initializeMasterConnection : ()Lcom/mysql/jdbc/MySQLConnection;
    //   26: astore_2
    //   27: aload_2
    //   28: ifnonnull -> 35
    //   31: aload_0
    //   32: monitorexit
    //   33: iconst_0
    //   34: ireturn
    //   35: aload_0
    //   36: invokevirtual isMasterConnection : ()Z
    //   39: ifne -> 69
    //   42: aload_0
    //   43: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   46: astore_2
    //   47: aload_2
    //   48: ifnull -> 69
    //   51: aload_0
    //   52: aload_0
    //   53: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   56: aload_2
    //   57: iconst_0
    //   58: invokevirtual syncSessionState : (Lcom/mysql/jdbc/Connection;Lcom/mysql/jdbc/Connection;Z)V
    //   61: aload_0
    //   62: aload_0
    //   63: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   66: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   69: aload_0
    //   70: monitorexit
    //   71: iconst_1
    //   72: ireturn
    //   73: astore_2
    //   74: aload_0
    //   75: aconst_null
    //   76: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   79: aload_2
    //   80: athrow
    //   81: astore_2
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_2
    //   85: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	81	finally
    //   11	18	81	finally
    //   22	27	73	java/sql/SQLException
    //   22	27	81	finally
    //   35	47	81	finally
    //   51	69	81	finally
    //   74	81	81	finally
  }
  
  private boolean switchToSlavesConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 22
    //   11: aload_2
    //   12: invokeinterface isClosed : ()Z
    //   17: istore_1
    //   18: iload_1
    //   19: ifeq -> 35
    //   22: aload_0
    //   23: invokespecial initializeSlavesConnection : ()Lcom/mysql/jdbc/MySQLConnection;
    //   26: astore_2
    //   27: aload_2
    //   28: ifnonnull -> 35
    //   31: aload_0
    //   32: monitorexit
    //   33: iconst_0
    //   34: ireturn
    //   35: aload_0
    //   36: invokevirtual isSlavesConnection : ()Z
    //   39: ifne -> 69
    //   42: aload_0
    //   43: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   46: astore_2
    //   47: aload_2
    //   48: ifnull -> 69
    //   51: aload_0
    //   52: aload_0
    //   53: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   56: aload_2
    //   57: iconst_1
    //   58: invokevirtual syncSessionState : (Lcom/mysql/jdbc/Connection;Lcom/mysql/jdbc/Connection;Z)V
    //   61: aload_0
    //   62: aload_0
    //   63: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   66: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   69: aload_0
    //   70: monitorexit
    //   71: iconst_1
    //   72: ireturn
    //   73: astore_2
    //   74: aload_0
    //   75: aconst_null
    //   76: putfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   79: aload_2
    //   80: athrow
    //   81: astore_2
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_2
    //   85: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	81	finally
    //   11	18	81	finally
    //   22	27	73	java/sql/SQLException
    //   22	27	81	finally
    //   35	47	81	finally
    //   51	69	81	finally
    //   74	81	81	finally
  }
  
  private boolean switchToSlavesConnectionIfNecessary() throws SQLException {
    return (this.currentConnection == null || (isMasterConnection() && (this.readOnly || (this.masterHosts.isEmpty() && this.currentConnection.isClosed()))) || (!isMasterConnection() && this.currentConnection.isClosed())) ? switchToSlavesConnection() : false;
  }
  
  public void addSlaveHost(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual isHostSlave : (Ljava/lang/String;)Z
    //   7: istore_2
    //   8: iload_2
    //   9: ifeq -> 15
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: aload_0
    //   16: getfield slaveHosts : Ljava/util/List;
    //   19: aload_1
    //   20: invokeinterface add : (Ljava/lang/Object;)Z
    //   25: pop
    //   26: aload_0
    //   27: invokespecial resetReadFromMasterWhenNoSlaves : ()V
    //   30: aload_0
    //   31: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   34: astore_3
    //   35: aload_3
    //   36: ifnonnull -> 52
    //   39: aload_0
    //   40: invokespecial initializeSlavesConnection : ()Lcom/mysql/jdbc/MySQLConnection;
    //   43: pop
    //   44: aload_0
    //   45: invokespecial switchToSlavesConnectionIfNecessary : ()Z
    //   48: pop
    //   49: goto -> 60
    //   52: aload_3
    //   53: aload_1
    //   54: invokeinterface addHost : (Ljava/lang/String;)Z
    //   59: pop
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	63	finally
    //   15	35	63	finally
    //   39	49	63	finally
    //   52	60	63	finally
  }
  
  public void doAbort(Executor paramExecutor) throws SQLException {
    this.masterConnection.abort(paramExecutor);
    this.slavesConnection.abort(paramExecutor);
    ReplicationConnectionGroup replicationConnectionGroup = this.connectionGroup;
    if (replicationConnectionGroup != null)
      replicationConnectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
  }
  
  public void doAbortInternal() throws SQLException {
    this.masterConnection.abortInternal();
    this.slavesConnection.abortInternal();
    ReplicationConnectionGroup replicationConnectionGroup = this.connectionGroup;
    if (replicationConnectionGroup != null)
      replicationConnectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
  }
  
  public void doClose() throws SQLException {
    LoadBalancedConnection loadBalancedConnection = this.masterConnection;
    if (loadBalancedConnection != null)
      loadBalancedConnection.close(); 
    loadBalancedConnection = this.slavesConnection;
    if (loadBalancedConnection != null)
      loadBalancedConnection.close(); 
    ReplicationConnectionGroup replicationConnectionGroup = this.connectionGroup;
    if (replicationConnectionGroup != null)
      replicationConnectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
  }
  
  public void doPing() throws SQLException {
    boolean bool = isMasterConnection();
    LoadBalancedConnection loadBalancedConnection1 = this.masterConnection;
    if (loadBalancedConnection1 != null) {
      try {
        loadBalancedConnection1.ping();
      } catch (SQLException sQLException) {}
    } else {
      initializeMasterConnection();
    } 
    loadBalancedConnection1 = null;
    LoadBalancedConnection loadBalancedConnection2 = this.slavesConnection;
    if (loadBalancedConnection2 != null) {
      try {
        loadBalancedConnection2.ping();
      } catch (SQLException sQLException) {}
    } else {
      try {
        initializeSlavesConnection();
        boolean bool1 = switchToSlavesConnectionIfNecessary();
        if (bool1)
          bool = false; 
      } catch (SQLException sQLException) {
        if (this.masterConnection == null || !this.readFromMasterWhenNoSlaves)
          throw sQLException; 
      } 
    } 
    loadBalancedConnection2 = null;
    if (bool && loadBalancedConnection1 != null) {
      LoadBalancedConnection loadBalancedConnection = this.slavesConnection;
      if (loadBalancedConnection != null && loadBalancedConnection2 == null) {
        this.masterConnection = null;
        this.currentConnection = loadBalancedConnection;
        this.readOnly = true;
      } 
      throw loadBalancedConnection1;
    } 
    if (!bool && (loadBalancedConnection2 != null || this.slavesConnection == null)) {
      LoadBalancedConnection loadBalancedConnection = this.masterConnection;
      if (loadBalancedConnection != null && this.readFromMasterWhenNoSlaves && loadBalancedConnection1 == null) {
        this.slavesConnection = null;
        this.currentConnection = loadBalancedConnection;
        this.readOnly = true;
        loadBalancedConnection.setReadOnly(true);
      } 
      if (loadBalancedConnection2 != null)
        throw loadBalancedConnection2; 
    } 
  }
  
  public long getConnectionGroupId() {
    return this.connectionGroupID;
  }
  
  public Connection getCurrentConnection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   6: astore_2
    //   7: aload_2
    //   8: astore_1
    //   9: aload_2
    //   10: ifnonnull -> 17
    //   13: invokestatic getNullLoadBalancedConnectionInstance : ()Lcom/mysql/jdbc/LoadBalancedConnection;
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: areturn
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	21	finally
    //   13	17	21	finally
  }
  
  public Connection getMasterConnection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public MySQLConnection getNewWrapperForThisAsConnection() throws SQLException {
    return (Util.isJdbc4() || JDBC_4_REPL_CONNECTION_CTOR != null) ? (MySQLConnection)Util.handleNewInstance(JDBC_4_REPL_CONNECTION_CTOR, new Object[] { this }, null) : new ReplicationMySQLConnection(this);
  }
  
  public Connection getSlavesConnection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Object invokeMore(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
    checkConnectionCapabilityForMethod(paramMethod);
    boolean bool = false;
    while (true) {
      try {
        paramObject = paramMethod.invoke(this.thisAsConnection, paramArrayOfObject);
        if (paramObject != null && paramObject instanceof Statement)
          ((Statement)paramObject).setPingTarget(this); 
        return paramObject;
      } catch (InvocationTargetException invocationTargetException) {
        boolean bool1;
        if (bool) {
          bool1 = false;
        } else {
          bool1 = bool;
          if (invocationTargetException.getCause() != null) {
            bool1 = bool;
            if (invocationTargetException.getCause() instanceof SQLException) {
              bool1 = bool;
              if (((SQLException)invocationTargetException.getCause()).getSQLState() == "25000") {
                bool1 = bool;
                if (((SQLException)invocationTargetException.getCause()).getErrorCode() == 1000001)
                  try {
                    setReadOnly(this.readOnly);
                    bool1 = true;
                  } catch (SQLException sQLException) {
                    bool1 = bool;
                  }  
              } 
            } 
          } 
        } 
        if (bool1) {
          bool = bool1;
          continue;
        } 
        throw invocationTargetException;
      } 
    } 
  }
  
  public boolean isHostMaster(String paramString) {
    if (paramString == null)
      return false; 
    Iterator<String> iterator = this.masterHosts.iterator();
    while (iterator.hasNext()) {
      if (((String)iterator.next()).equalsIgnoreCase(paramString))
        return true; 
    } 
    return false;
  }
  
  public boolean isHostSlave(String paramString) {
    if (paramString == null)
      return false; 
    Iterator<String> iterator = this.slaveHosts.iterator();
    while (iterator.hasNext()) {
      if (((String)iterator.next()).equalsIgnoreCase(paramString))
        return true; 
    } 
    return false;
  }
  
  public boolean isMasterConnection() {
    boolean bool;
    MySQLConnection mySQLConnection = this.currentConnection;
    if (mySQLConnection != null && mySQLConnection == this.masterConnection) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isReadOnly() throws SQLException {
    return (!isMasterConnection() || this.readOnly);
  }
  
  public boolean isSlavesConnection() {
    boolean bool;
    MySQLConnection mySQLConnection = this.currentConnection;
    if (mySQLConnection != null && mySQLConnection == this.slavesConnection) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void pickNewConnection() throws SQLException {}
  
  public void promoteSlaveToMaster(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield masterHosts : Ljava/util/List;
    //   6: aload_1
    //   7: invokeinterface add : (Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_0
    //   14: aload_1
    //   15: invokevirtual removeSlave : (Ljava/lang/String;)V
    //   18: aload_0
    //   19: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   22: astore_2
    //   23: aload_2
    //   24: ifnull -> 35
    //   27: aload_2
    //   28: aload_1
    //   29: invokeinterface addHost : (Ljava/lang/String;)Z
    //   34: pop
    //   35: aload_0
    //   36: getfield readOnly : Z
    //   39: ifne -> 54
    //   42: aload_0
    //   43: invokevirtual isMasterConnection : ()Z
    //   46: ifne -> 54
    //   49: aload_0
    //   50: invokespecial switchToMasterConnection : ()Z
    //   53: pop
    //   54: aload_0
    //   55: monitorexit
    //   56: return
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	57	finally
    //   27	35	57	finally
    //   35	54	57	finally
  }
  
  public void propagateProxyDown(MySQLConnection paramMySQLConnection) {
    LoadBalancedConnection loadBalancedConnection = this.masterConnection;
    if (loadBalancedConnection != null)
      loadBalancedConnection.setProxy(paramMySQLConnection); 
    loadBalancedConnection = this.slavesConnection;
    if (loadBalancedConnection != null)
      loadBalancedConnection.setProxy(paramMySQLConnection); 
  }
  
  public void removeMasterHost(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: iconst_1
    //   5: invokevirtual removeMasterHost : (Ljava/lang/String;Z)V
    //   8: aload_0
    //   9: monitorexit
    //   10: return
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	11	finally
  }
  
  public void removeMasterHost(String paramString, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: iload_2
    //   5: iconst_0
    //   6: invokevirtual removeMasterHost : (Ljava/lang/String;ZZ)V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public void removeMasterHost(String paramString, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_3
    //   3: ifeq -> 21
    //   6: aload_0
    //   7: getfield slaveHosts : Ljava/util/List;
    //   10: aload_1
    //   11: invokeinterface add : (Ljava/lang/Object;)Z
    //   16: pop
    //   17: aload_0
    //   18: invokespecial resetReadFromMasterWhenNoSlaves : ()V
    //   21: aload_0
    //   22: getfield masterHosts : Ljava/util/List;
    //   25: aload_1
    //   26: invokeinterface remove : (Ljava/lang/Object;)Z
    //   31: pop
    //   32: aload_0
    //   33: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   36: astore #4
    //   38: aload #4
    //   40: ifnull -> 117
    //   43: aload #4
    //   45: invokeinterface isClosed : ()Z
    //   50: ifeq -> 56
    //   53: goto -> 117
    //   56: iload_2
    //   57: ifeq -> 73
    //   60: aload_0
    //   61: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   64: aload_1
    //   65: invokeinterface removeHostWhenNotInUse : (Ljava/lang/String;)V
    //   70: goto -> 83
    //   73: aload_0
    //   74: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   77: aload_1
    //   78: invokeinterface removeHost : (Ljava/lang/String;)V
    //   83: aload_0
    //   84: getfield masterHosts : Ljava/util/List;
    //   87: invokeinterface isEmpty : ()Z
    //   92: ifeq -> 114
    //   95: aload_0
    //   96: getfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   99: invokeinterface close : ()V
    //   104: aload_0
    //   105: aconst_null
    //   106: putfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   109: aload_0
    //   110: invokespecial switchToSlavesConnectionIfNecessary : ()Z
    //   113: pop
    //   114: aload_0
    //   115: monitorexit
    //   116: return
    //   117: aload_0
    //   118: aconst_null
    //   119: putfield masterConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   122: aload_0
    //   123: monitorexit
    //   124: return
    //   125: astore_1
    //   126: aload_0
    //   127: monitorexit
    //   128: aload_1
    //   129: athrow
    // Exception table:
    //   from	to	target	type
    //   6	21	125	finally
    //   21	38	125	finally
    //   43	53	125	finally
    //   60	70	125	finally
    //   73	83	125	finally
    //   83	114	125	finally
    //   117	122	125	finally
  }
  
  public void removeSlave(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: iconst_1
    //   5: invokevirtual removeSlave : (Ljava/lang/String;Z)V
    //   8: aload_0
    //   9: monitorexit
    //   10: return
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	11	finally
  }
  
  public void removeSlave(String paramString, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield slaveHosts : Ljava/util/List;
    //   6: aload_1
    //   7: invokeinterface remove : (Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_0
    //   14: invokespecial resetReadFromMasterWhenNoSlaves : ()V
    //   17: aload_0
    //   18: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull -> 119
    //   26: aload_3
    //   27: invokeinterface isClosed : ()Z
    //   32: ifeq -> 38
    //   35: goto -> 119
    //   38: iload_2
    //   39: ifeq -> 55
    //   42: aload_0
    //   43: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   46: aload_1
    //   47: invokeinterface removeHostWhenNotInUse : (Ljava/lang/String;)V
    //   52: goto -> 65
    //   55: aload_0
    //   56: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   59: aload_1
    //   60: invokeinterface removeHost : (Ljava/lang/String;)V
    //   65: aload_0
    //   66: getfield slaveHosts : Ljava/util/List;
    //   69: invokeinterface isEmpty : ()Z
    //   74: ifeq -> 116
    //   77: aload_0
    //   78: getfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   81: invokeinterface close : ()V
    //   86: aload_0
    //   87: aconst_null
    //   88: putfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   91: aload_0
    //   92: invokespecial switchToMasterConnection : ()Z
    //   95: pop
    //   96: aload_0
    //   97: invokevirtual isMasterConnection : ()Z
    //   100: ifeq -> 116
    //   103: aload_0
    //   104: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   107: aload_0
    //   108: getfield readOnly : Z
    //   111: invokeinterface setReadOnly : (Z)V
    //   116: aload_0
    //   117: monitorexit
    //   118: return
    //   119: aload_0
    //   120: aconst_null
    //   121: putfield slavesConnection : Lcom/mysql/jdbc/LoadBalancedConnection;
    //   124: aload_0
    //   125: monitorexit
    //   126: return
    //   127: astore_1
    //   128: aload_0
    //   129: monitorexit
    //   130: aload_1
    //   131: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	127	finally
    //   26	35	127	finally
    //   42	52	127	finally
    //   55	65	127	finally
    //   65	116	127	finally
    //   119	124	127	finally
  }
  
  public void setReadOnly(boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: iconst_0
    //   5: istore_2
    //   6: aconst_null
    //   7: astore #5
    //   9: aconst_null
    //   10: astore #6
    //   12: iload_1
    //   13: ifeq -> 88
    //   16: aload_0
    //   17: invokevirtual isSlavesConnection : ()Z
    //   20: ifeq -> 37
    //   23: aload_0
    //   24: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   27: invokeinterface isClosed : ()Z
    //   32: istore_3
    //   33: iload_3
    //   34: ifeq -> 147
    //   37: aload_0
    //   38: invokespecial switchToSlavesConnection : ()Z
    //   41: istore_3
    //   42: aconst_null
    //   43: astore #4
    //   45: iload_3
    //   46: istore_2
    //   47: goto -> 52
    //   50: astore #4
    //   52: iload_2
    //   53: ifne -> 77
    //   56: aload_0
    //   57: getfield readFromMasterWhenNoSlaves : Z
    //   60: ifeq -> 77
    //   63: aload_0
    //   64: invokespecial switchToMasterConnection : ()Z
    //   67: ifeq -> 77
    //   70: aload #6
    //   72: astore #4
    //   74: goto -> 77
    //   77: aload #4
    //   79: ifnonnull -> 85
    //   82: goto -> 147
    //   85: aload #4
    //   87: athrow
    //   88: aload_0
    //   89: invokevirtual isMasterConnection : ()Z
    //   92: ifeq -> 109
    //   95: aload_0
    //   96: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   99: invokeinterface isClosed : ()Z
    //   104: istore_2
    //   105: iload_2
    //   106: ifeq -> 147
    //   109: aload_0
    //   110: invokespecial switchToMasterConnection : ()Z
    //   113: istore_2
    //   114: aconst_null
    //   115: astore #4
    //   117: goto -> 124
    //   120: astore #4
    //   122: iload_3
    //   123: istore_2
    //   124: iload_2
    //   125: ifne -> 142
    //   128: aload_0
    //   129: invokespecial switchToSlavesConnectionIfNecessary : ()Z
    //   132: ifeq -> 142
    //   135: aload #5
    //   137: astore #4
    //   139: goto -> 142
    //   142: aload #4
    //   144: ifnonnull -> 182
    //   147: aload_0
    //   148: iload_1
    //   149: putfield readOnly : Z
    //   152: aload_0
    //   153: getfield readFromMasterWhenNoSlaves : Z
    //   156: ifeq -> 179
    //   159: aload_0
    //   160: invokevirtual isMasterConnection : ()Z
    //   163: ifeq -> 179
    //   166: aload_0
    //   167: getfield currentConnection : Lcom/mysql/jdbc/MySQLConnection;
    //   170: aload_0
    //   171: getfield readOnly : Z
    //   174: invokeinterface setReadOnly : (Z)V
    //   179: aload_0
    //   180: monitorexit
    //   181: return
    //   182: aload #4
    //   184: athrow
    //   185: astore #4
    //   187: aload_0
    //   188: monitorexit
    //   189: aload #4
    //   191: athrow
    // Exception table:
    //   from	to	target	type
    //   16	33	185	finally
    //   37	42	50	java/sql/SQLException
    //   37	42	185	finally
    //   56	70	185	finally
    //   85	88	185	finally
    //   88	105	185	finally
    //   109	114	120	java/sql/SQLException
    //   109	114	185	finally
    //   128	135	185	finally
    //   147	179	185	finally
    //   182	185	185	finally
  }
  
  public boolean shouldExceptionTriggerConnectionSwitch(Throwable paramThrowable) {
    return false;
  }
  
  public void syncSessionState(Connection paramConnection1, Connection paramConnection2, boolean paramBoolean) throws SQLException {
    try {
      super.syncSessionState(paramConnection1, paramConnection2, paramBoolean);
    } catch (SQLException sQLException) {
      try {
        super.syncSessionState(paramConnection1, paramConnection2, paramBoolean);
      } catch (SQLException sQLException1) {}
    } 
  }
}
