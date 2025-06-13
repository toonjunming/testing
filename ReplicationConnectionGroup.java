package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ReplicationConnectionGroup {
  private long activeConnections = 0L;
  
  private long connections = 0L;
  
  private String groupName;
  
  private boolean isInitialized = false;
  
  private Set<String> masterHostList = new CopyOnWriteArraySet<String>();
  
  private HashMap<Long, ReplicationConnection> replicationConnections = new HashMap<Long, ReplicationConnection>();
  
  private Set<String> slaveHostList = new CopyOnWriteArraySet<String>();
  
  private long slavesAdded = 0L;
  
  private long slavesPromoted = 0L;
  
  private long slavesRemoved = 0L;
  
  public ReplicationConnectionGroup(String paramString) {
    this.groupName = paramString;
  }
  
  public void addSlaveHost(String paramString) throws SQLException {
    if (this.slaveHostList.add(paramString)) {
      this.slavesAdded++;
      Iterator<ReplicationConnection> iterator = this.replicationConnections.values().iterator();
      while (iterator.hasNext())
        ((ReplicationConnection)iterator.next()).addSlaveHost(paramString); 
    } 
  }
  
  public long getActiveConnectionCount() {
    return this.activeConnections;
  }
  
  public long getConnectionCount() {
    return this.connections;
  }
  
  public int getConnectionCountWithHostAsMaster(String paramString) {
    Iterator<ReplicationConnection> iterator = this.replicationConnections.values().iterator();
    byte b = 0;
    while (iterator.hasNext()) {
      if (((ReplicationConnection)iterator.next()).isHostMaster(paramString))
        b++; 
    } 
    return b;
  }
  
  public int getConnectionCountWithHostAsSlave(String paramString) {
    Iterator<ReplicationConnection> iterator = this.replicationConnections.values().iterator();
    byte b = 0;
    while (iterator.hasNext()) {
      if (((ReplicationConnection)iterator.next()).isHostSlave(paramString))
        b++; 
    } 
    return b;
  }
  
  public String getGroupName() {
    return this.groupName;
  }
  
  public Collection<String> getMasterHosts() {
    return this.masterHostList;
  }
  
  public long getNumberOfSlavePromotions() {
    return this.slavesPromoted;
  }
  
  public long getNumberOfSlavesAdded() {
    return this.slavesAdded;
  }
  
  public long getNumberOfSlavesRemoved() {
    return this.slavesRemoved;
  }
  
  public Collection<String> getSlaveHosts() {
    return this.slaveHostList;
  }
  
  public long getTotalConnectionCount() {
    return this.connections;
  }
  
  public void handleCloseConnection(ReplicationConnection paramReplicationConnection) {
    this.replicationConnections.remove(Long.valueOf(paramReplicationConnection.getConnectionGroupId()));
    this.activeConnections--;
  }
  
  public void promoteSlaveToMaster(String paramString) throws SQLException {
    if ((this.slaveHostList.remove(paramString) | this.masterHostList.add(paramString)) != 0) {
      this.slavesPromoted++;
      Iterator<ReplicationConnection> iterator = this.replicationConnections.values().iterator();
      while (iterator.hasNext())
        ((ReplicationConnection)iterator.next()).promoteSlaveToMaster(paramString); 
    } 
  }
  
  public long registerReplicationConnection(ReplicationConnection paramReplicationConnection, List<String> paramList1, List<String> paramList2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isInitialized : Z
    //   6: ifne -> 44
    //   9: aload_2
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield masterHostList : Ljava/util/Set;
    //   17: aload_2
    //   18: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   23: pop
    //   24: aload_3
    //   25: ifnull -> 39
    //   28: aload_0
    //   29: getfield slaveHostList : Ljava/util/Set;
    //   32: aload_3
    //   33: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   38: pop
    //   39: aload_0
    //   40: iconst_1
    //   41: putfield isInitialized : Z
    //   44: aload_0
    //   45: getfield connections : J
    //   48: lconst_1
    //   49: ladd
    //   50: lstore #4
    //   52: aload_0
    //   53: lload #4
    //   55: putfield connections : J
    //   58: aload_0
    //   59: getfield replicationConnections : Ljava/util/HashMap;
    //   62: lload #4
    //   64: invokestatic valueOf : (J)Ljava/lang/Long;
    //   67: aload_1
    //   68: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   71: pop
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_0
    //   75: aload_0
    //   76: getfield activeConnections : J
    //   79: lconst_1
    //   80: ladd
    //   81: putfield activeConnections : J
    //   84: lload #4
    //   86: lreturn
    //   87: astore_1
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_1
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	87	finally
    //   13	24	87	finally
    //   28	39	87	finally
    //   39	44	87	finally
    //   44	74	87	finally
    //   88	90	87	finally
  }
  
  public void removeMasterHost(String paramString) throws SQLException {
    removeMasterHost(paramString, true);
  }
  
  public void removeMasterHost(String paramString, boolean paramBoolean) throws SQLException {
    if (this.masterHostList.remove(paramString)) {
      Iterator<ReplicationConnection> iterator = this.replicationConnections.values().iterator();
      while (iterator.hasNext())
        ((ReplicationConnection)iterator.next()).removeMasterHost(paramString, paramBoolean); 
    } 
  }
  
  public void removeSlaveHost(String paramString, boolean paramBoolean) throws SQLException {
    if (this.slaveHostList.remove(paramString)) {
      this.slavesRemoved++;
      Iterator<ReplicationConnection> iterator = this.replicationConnections.values().iterator();
      while (iterator.hasNext())
        ((ReplicationConnection)iterator.next()).removeSlave(paramString, paramBoolean); 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ReplicationConnectionGroup[groupName=");
    stringBuilder.append(this.groupName);
    stringBuilder.append(",masterHostList=");
    stringBuilder.append(this.masterHostList);
    stringBuilder.append(",slaveHostList=");
    stringBuilder.append(this.slaveHostList);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}
