package com.mysql.jdbc.jmx;

import com.mysql.jdbc.ConnectionGroupManager;
import java.sql.SQLException;

public class LoadBalanceConnectionGroupManager implements LoadBalanceConnectionGroupManagerMBean {
  private boolean isJmxRegistered = false;
  
  public void addHost(String paramString1, String paramString2, boolean paramBoolean) {
    try {
      ConnectionGroupManager.addHost(paramString1, paramString2, paramBoolean);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public int getActiveHostCount(String paramString) {
    return ConnectionGroupManager.getActiveHostCount(paramString);
  }
  
  public String getActiveHostsList(String paramString) {
    return ConnectionGroupManager.getActiveHostLists(paramString);
  }
  
  public long getActiveLogicalConnectionCount(String paramString) {
    return ConnectionGroupManager.getActiveLogicalConnectionCount(paramString);
  }
  
  public long getActivePhysicalConnectionCount(String paramString) {
    return ConnectionGroupManager.getActivePhysicalConnectionCount(paramString);
  }
  
  public String getRegisteredConnectionGroups() {
    return ConnectionGroupManager.getRegisteredConnectionGroups();
  }
  
  public int getTotalHostCount(String paramString) {
    return ConnectionGroupManager.getTotalHostCount(paramString);
  }
  
  public long getTotalLogicalConnectionCount(String paramString) {
    return ConnectionGroupManager.getTotalLogicalConnectionCount(paramString);
  }
  
  public long getTotalPhysicalConnectionCount(String paramString) {
    return ConnectionGroupManager.getTotalPhysicalConnectionCount(paramString);
  }
  
  public long getTotalTransactionCount(String paramString) {
    return ConnectionGroupManager.getTotalTransactionCount(paramString);
  }
  
  public void registerJmx() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isJmxRegistered : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifeq -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: invokestatic getPlatformMBeanServer : ()Ljavax/management/MBeanServer;
    //   17: astore_2
    //   18: new javax/management/ObjectName
    //   21: astore_3
    //   22: aload_3
    //   23: ldc 'com.mysql.jdbc.jmx:type=LoadBalanceConnectionGroupManager'
    //   25: invokespecial <init> : (Ljava/lang/String;)V
    //   28: aload_2
    //   29: aload_0
    //   30: aload_3
    //   31: invokeinterface registerMBean : (Ljava/lang/Object;Ljavax/management/ObjectName;)Ljavax/management/ObjectInstance;
    //   36: pop
    //   37: aload_0
    //   38: iconst_1
    //   39: putfield isJmxRegistered : Z
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: astore_2
    //   46: ldc 'Unable to register load-balance management bean with JMX'
    //   48: aconst_null
    //   49: aload_2
    //   50: aconst_null
    //   51: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   54: athrow
    //   55: astore_2
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_2
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	55	finally
    //   14	18	55	finally
    //   18	42	45	java/lang/Exception
    //   18	42	55	finally
    //   46	55	55	finally
  }
  
  public void removeHost(String paramString1, String paramString2) throws SQLException {
    ConnectionGroupManager.removeHost(paramString1, paramString2);
  }
  
  public void stopNewConnectionsToHost(String paramString1, String paramString2) throws SQLException {
    ConnectionGroupManager.removeHost(paramString1, paramString2);
  }
}
