package com.mysql.jdbc.jmx;

import com.mysql.jdbc.ReplicationConnectionGroup;
import com.mysql.jdbc.ReplicationConnectionGroupManager;
import java.sql.SQLException;
import java.util.Iterator;

public class ReplicationGroupManager implements ReplicationGroupManagerMBean {
  private boolean isJmxRegistered = false;
  
  public void addSlaveHost(String paramString1, String paramString2) throws SQLException {
    ReplicationConnectionGroupManager.addSlaveHost(paramString1, paramString2);
  }
  
  public long getActiveLogicalConnectionCount(String paramString) {
    return ReplicationConnectionGroupManager.getActiveConnectionCount(paramString);
  }
  
  public int getActiveMasterHostCount(String paramString) {
    return ReplicationConnectionGroupManager.getMasterHosts(paramString).size();
  }
  
  public int getActiveSlaveHostCount(String paramString) {
    return ReplicationConnectionGroupManager.getSlaveHosts(paramString).size();
  }
  
  public String getMasterHostsList(String paramString) {
    StringBuilder stringBuilder = new StringBuilder("");
    Iterator<String> iterator = ReplicationConnectionGroupManager.getMasterHosts(paramString).iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      String str = iterator.next();
      if (bool)
        stringBuilder.append(","); 
      bool = true;
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  public String getRegisteredConnectionGroups() {
    StringBuilder stringBuilder = new StringBuilder("");
    Iterator<ReplicationConnectionGroup> iterator = ReplicationConnectionGroupManager.getGroupsMatching(null).iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      ReplicationConnectionGroup replicationConnectionGroup = iterator.next();
      if (bool)
        stringBuilder.append(","); 
      bool = true;
      stringBuilder.append(replicationConnectionGroup.getGroupName());
    } 
    return stringBuilder.toString();
  }
  
  public String getSlaveHostsList(String paramString) {
    StringBuilder stringBuilder = new StringBuilder("");
    Iterator<String> iterator = ReplicationConnectionGroupManager.getSlaveHosts(paramString).iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      String str = iterator.next();
      if (bool)
        stringBuilder.append(","); 
      bool = true;
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  public int getSlavePromotionCount(String paramString) {
    return ReplicationConnectionGroupManager.getNumberOfMasterPromotion(paramString);
  }
  
  public long getTotalLogicalConnectionCount(String paramString) {
    return ReplicationConnectionGroupManager.getTotalConnectionCount(paramString);
  }
  
  public void promoteSlaveToMaster(String paramString1, String paramString2) throws SQLException {
    ReplicationConnectionGroupManager.promoteSlaveToMaster(paramString1, paramString2);
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
    //   23: ldc 'com.mysql.jdbc.jmx:type=ReplicationGroupManager'
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
    //   46: ldc 'Unable to register replication host management bean with JMX'
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
  
  public void removeMasterHost(String paramString1, String paramString2) throws SQLException {
    ReplicationConnectionGroupManager.removeMasterHost(paramString1, paramString2);
  }
  
  public void removeSlaveHost(String paramString1, String paramString2) throws SQLException {
    ReplicationConnectionGroupManager.removeSlaveHost(paramString1, paramString2);
  }
}
