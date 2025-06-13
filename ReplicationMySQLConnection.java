package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ReplicationMySQLConnection extends MultiHostMySQLConnection implements ReplicationConnection {
  public ReplicationMySQLConnection(MultiHostConnectionProxy paramMultiHostConnectionProxy) {
    super(paramMultiHostConnectionProxy);
  }
  
  private Connection getValidatedMasterConnection() {
    null = (getThisAsProxy()).masterConnection;
    if (null != null) {
      try {
        boolean bool = null.isClosed();
        if (bool)
          return null; 
      } catch (SQLException sQLException) {
        return null;
      } 
      return (Connection)sQLException;
    } 
    return null;
  }
  
  private Connection getValidatedSlavesConnection() {
    null = (getThisAsProxy()).slavesConnection;
    if (null != null) {
      try {
        boolean bool = null.isClosed();
        if (bool)
          return null; 
      } catch (SQLException sQLException) {
        return null;
      } 
      return (Connection)sQLException;
    } 
    return null;
  }
  
  public void abort(Executor paramExecutor) throws SQLException {
    getThisAsProxy().doAbort(paramExecutor);
  }
  
  public void abortInternal() throws SQLException {
    getThisAsProxy().doAbortInternal();
  }
  
  public void addSlaveHost(String paramString) throws SQLException {
    getThisAsProxy().addSlaveHost(paramString);
  }
  
  public void changeUser(String paramString1, String paramString2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial getValidatedMasterConnection : ()Lcom/mysql/jdbc/Connection;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnull -> 19
    //   11: aload_3
    //   12: aload_1
    //   13: aload_2
    //   14: invokeinterface changeUser : (Ljava/lang/String;Ljava/lang/String;)V
    //   19: aload_0
    //   20: invokespecial getValidatedSlavesConnection : ()Lcom/mysql/jdbc/Connection;
    //   23: astore_3
    //   24: aload_3
    //   25: ifnull -> 36
    //   28: aload_3
    //   29: aload_1
    //   30: aload_2
    //   31: invokeinterface changeUser : (Ljava/lang/String;Ljava/lang/String;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	39	finally
    //   11	19	39	finally
    //   19	24	39	finally
    //   28	36	39	finally
  }
  
  public MySQLConnection getActiveMySQLConnection() {
    return (MySQLConnection)getCurrentConnection();
  }
  
  public boolean getAllowMasterDownConnections() {
    return (getThisAsProxy()).allowMasterDownConnections;
  }
  
  public long getConnectionGroupId() {
    return getThisAsProxy().getConnectionGroupId();
  }
  
  public Connection getCurrentConnection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getThisAsProxy : ()Lcom/mysql/jdbc/ReplicationConnectionProxy;
    //   6: invokevirtual getCurrentConnection : ()Lcom/mysql/jdbc/Connection;
    //   9: astore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: areturn
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	14	finally
  }
  
  public Connection getMasterConnection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getThisAsProxy : ()Lcom/mysql/jdbc/ReplicationConnectionProxy;
    //   6: invokevirtual getMasterConnection : ()Lcom/mysql/jdbc/Connection;
    //   9: astore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: areturn
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	14	finally
  }
  
  public Properties getProperties() {
    Properties properties = new Properties();
    Connection connection = getValidatedMasterConnection();
    if (connection != null)
      properties.putAll(connection.getProperties()); 
    connection = getValidatedSlavesConnection();
    if (connection != null)
      properties.putAll(connection.getProperties()); 
    return properties;
  }
  
  public boolean getReplicationEnableJMX() {
    return (getThisAsProxy()).enableJMX;
  }
  
  public Connection getSlavesConnection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getThisAsProxy : ()Lcom/mysql/jdbc/ReplicationConnectionProxy;
    //   6: invokevirtual getSlavesConnection : ()Lcom/mysql/jdbc/Connection;
    //   9: astore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: areturn
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	14	finally
  }
  
  public ReplicationConnectionProxy getThisAsProxy() {
    return (ReplicationConnectionProxy)super.getThisAsProxy();
  }
  
  public boolean hasSameProperties(Connection paramConnection) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial getValidatedMasterConnection : ()Lcom/mysql/jdbc/Connection;
    //   4: astore #4
    //   6: aload_0
    //   7: invokespecial getValidatedSlavesConnection : ()Lcom/mysql/jdbc/Connection;
    //   10: astore #5
    //   12: iconst_0
    //   13: istore_3
    //   14: aload #4
    //   16: ifnonnull -> 26
    //   19: aload #5
    //   21: ifnonnull -> 26
    //   24: iconst_0
    //   25: ireturn
    //   26: aload #4
    //   28: ifnull -> 44
    //   31: iload_3
    //   32: istore_2
    //   33: aload #4
    //   35: aload_1
    //   36: invokeinterface hasSameProperties : (Lcom/mysql/jdbc/Connection;)Z
    //   41: ifeq -> 64
    //   44: aload #5
    //   46: ifnull -> 62
    //   49: iload_3
    //   50: istore_2
    //   51: aload #5
    //   53: aload_1
    //   54: invokeinterface hasSameProperties : (Lcom/mysql/jdbc/Connection;)Z
    //   59: ifeq -> 64
    //   62: iconst_1
    //   63: istore_2
    //   64: iload_2
    //   65: ireturn
  }
  
  public boolean isHostMaster(String paramString) {
    return getThisAsProxy().isHostMaster(paramString);
  }
  
  public boolean isHostSlave(String paramString) {
    return getThisAsProxy().isHostSlave(paramString);
  }
  
  public boolean isReadOnly() throws SQLException {
    return getThisAsProxy().isReadOnly();
  }
  
  public void ping() throws SQLException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/ReplicationMySQLConnection}} */
    try {
      Connection connection = getValidatedMasterConnection();
      if (connection != null)
        connection.ping(); 
    } catch (SQLException sQLException) {
      boolean bool = isMasterConnection();
      if (bool)
        throw sQLException; 
    } finally {
      Exception exception;
    } 
    try {
      Connection connection = getValidatedSlavesConnection();
      if (connection != null)
        connection.ping(); 
    } catch (SQLException sQLException) {
      boolean bool = isMasterConnection();
      if (!bool)
        throw sQLException; 
    } 
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ReplicationMySQLConnection}} */
  }
  
  public void promoteSlaveToMaster(String paramString) throws SQLException {
    getThisAsProxy().promoteSlaveToMaster(paramString);
  }
  
  public void removeMasterHost(String paramString) throws SQLException {
    getThisAsProxy().removeMasterHost(paramString);
  }
  
  public void removeMasterHost(String paramString, boolean paramBoolean) throws SQLException {
    getThisAsProxy().removeMasterHost(paramString, paramBoolean);
  }
  
  public void removeSlave(String paramString) throws SQLException {
    getThisAsProxy().removeSlave(paramString);
  }
  
  public void removeSlave(String paramString, boolean paramBoolean) throws SQLException {
    getThisAsProxy().removeSlave(paramString, paramBoolean);
  }
  
  public void setAllowMasterDownConnections(boolean paramBoolean) {
    (getThisAsProxy()).allowMasterDownConnections = paramBoolean;
  }
  
  public void setProxy(MySQLConnection paramMySQLConnection) {
    getThisAsProxy().setProxy(paramMySQLConnection);
  }
  
  public void setReadOnly(boolean paramBoolean) throws SQLException {
    getThisAsProxy().setReadOnly(paramBoolean);
  }
  
  public void setReplicationEnableJMX(boolean paramBoolean) {
    (getThisAsProxy()).enableJMX = paramBoolean;
  }
  
  public void setStatementComment(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial getValidatedMasterConnection : ()Lcom/mysql/jdbc/Connection;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 18
    //   11: aload_2
    //   12: aload_1
    //   13: invokeinterface setStatementComment : (Ljava/lang/String;)V
    //   18: aload_0
    //   19: invokespecial getValidatedSlavesConnection : ()Lcom/mysql/jdbc/Connection;
    //   22: astore_2
    //   23: aload_2
    //   24: ifnull -> 34
    //   27: aload_2
    //   28: aload_1
    //   29: invokeinterface setStatementComment : (Ljava/lang/String;)V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   11	18	37	finally
    //   18	23	37	finally
    //   27	34	37	finally
  }
}
