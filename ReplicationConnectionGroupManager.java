package com.mysql.jdbc;

import com.mysql.jdbc.jmx.ReplicationGroupManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ReplicationConnectionGroupManager {
  private static HashMap<String, ReplicationConnectionGroup> GROUP_MAP = new HashMap<String, ReplicationConnectionGroup>();
  
  private static boolean hasRegisteredJmx;
  
  private static ReplicationGroupManager mbean = new ReplicationGroupManager();
  
  static {
    hasRegisteredJmx = false;
  }
  
  public static void addSlaveHost(String paramString1, String paramString2) throws SQLException {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    while (iterator.hasNext())
      ((ReplicationConnectionGroup)iterator.next()).addSlaveHost(paramString2); 
  }
  
  public static long getActiveConnectionCount(String paramString) {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    long l;
    for (l = 0L; iterator.hasNext(); l += ((ReplicationConnectionGroup)iterator.next()).getActiveConnectionCount());
    return l;
  }
  
  public static int getConnectionCountWithHostAsMaster(String paramString1, String paramString2) {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    int i;
    for (i = 0; iterator.hasNext(); i += ((ReplicationConnectionGroup)iterator.next()).getConnectionCountWithHostAsMaster(paramString2));
    return i;
  }
  
  public static int getConnectionCountWithHostAsSlave(String paramString1, String paramString2) {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    int i;
    for (i = 0; iterator.hasNext(); i += ((ReplicationConnectionGroup)iterator.next()).getConnectionCountWithHostAsSlave(paramString2));
    return i;
  }
  
  public static ReplicationConnectionGroup getConnectionGroup(String paramString) {
    return GROUP_MAP.get(paramString);
  }
  
  public static ReplicationConnectionGroup getConnectionGroupInstance(String paramString) {
    // Byte code:
    //   0: ldc com/mysql/jdbc/ReplicationConnectionGroupManager
    //   2: monitorenter
    //   3: getstatic com/mysql/jdbc/ReplicationConnectionGroupManager.GROUP_MAP : Ljava/util/HashMap;
    //   6: aload_0
    //   7: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   10: ifeq -> 29
    //   13: getstatic com/mysql/jdbc/ReplicationConnectionGroupManager.GROUP_MAP : Ljava/util/HashMap;
    //   16: aload_0
    //   17: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   20: checkcast com/mysql/jdbc/ReplicationConnectionGroup
    //   23: astore_0
    //   24: ldc com/mysql/jdbc/ReplicationConnectionGroupManager
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: new com/mysql/jdbc/ReplicationConnectionGroup
    //   32: astore_1
    //   33: aload_1
    //   34: aload_0
    //   35: invokespecial <init> : (Ljava/lang/String;)V
    //   38: getstatic com/mysql/jdbc/ReplicationConnectionGroupManager.GROUP_MAP : Ljava/util/HashMap;
    //   41: aload_0
    //   42: aload_1
    //   43: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   46: pop
    //   47: ldc com/mysql/jdbc/ReplicationConnectionGroupManager
    //   49: monitorexit
    //   50: aload_1
    //   51: areturn
    //   52: astore_0
    //   53: ldc com/mysql/jdbc/ReplicationConnectionGroupManager
    //   55: monitorexit
    //   56: aload_0
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   3	24	52	finally
    //   29	47	52	finally
  }
  
  public static Collection<ReplicationConnectionGroup> getGroupsMatching(String paramString) {
    HashSet<ReplicationConnectionGroup> hashSet1;
    if (paramString == null || paramString.equals("")) {
      hashSet1 = new HashSet();
      hashSet1.addAll(GROUP_MAP.values());
      return hashSet1;
    } 
    HashSet<ReplicationConnectionGroup> hashSet2 = new HashSet();
    ReplicationConnectionGroup replicationConnectionGroup = GROUP_MAP.get(hashSet1);
    if (replicationConnectionGroup != null)
      hashSet2.add(replicationConnectionGroup); 
    return hashSet2;
  }
  
  public static Collection<String> getMasterHosts(String paramString) {
    Collection<ReplicationConnectionGroup> collection = getGroupsMatching(paramString);
    ArrayList<String> arrayList = new ArrayList();
    Iterator<ReplicationConnectionGroup> iterator = collection.iterator();
    while (iterator.hasNext())
      arrayList.addAll(((ReplicationConnectionGroup)iterator.next()).getMasterHosts()); 
    return arrayList;
  }
  
  public static int getNumberOfMasterPromotion(String paramString) {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    int i;
    for (i = 0; iterator.hasNext(); i = (int)(i + replicationConnectionGroup.getNumberOfSlavePromotions()))
      ReplicationConnectionGroup replicationConnectionGroup = iterator.next(); 
    return i;
  }
  
  public static String getRegisteredReplicationConnectionGroups() {
    Collection<ReplicationConnectionGroup> collection = getGroupsMatching(null);
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<ReplicationConnectionGroup> iterator = collection.iterator();
    for (String str = ""; iterator.hasNext(); str = ",") {
      String str1 = ((ReplicationConnectionGroup)iterator.next()).getGroupName();
      stringBuilder.append(str);
      stringBuilder.append(str1);
    } 
    return stringBuilder.toString();
  }
  
  public static Collection<String> getSlaveHosts(String paramString) {
    Collection<ReplicationConnectionGroup> collection = getGroupsMatching(paramString);
    ArrayList<String> arrayList = new ArrayList();
    Iterator<ReplicationConnectionGroup> iterator = collection.iterator();
    while (iterator.hasNext())
      arrayList.addAll(((ReplicationConnectionGroup)iterator.next()).getSlaveHosts()); 
    return arrayList;
  }
  
  public static long getSlavePromotionCount(String paramString) throws SQLException {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    long l = 0L;
    while (iterator.hasNext()) {
      long l1 = ((ReplicationConnectionGroup)iterator.next()).getNumberOfSlavePromotions();
      if (l1 > l)
        l = l1; 
    } 
    return l;
  }
  
  public static long getTotalConnectionCount(String paramString) {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    long l;
    for (l = 0L; iterator.hasNext(); l += ((ReplicationConnectionGroup)iterator.next()).getTotalConnectionCount());
    return l;
  }
  
  public static void promoteSlaveToMaster(String paramString1, String paramString2) throws SQLException {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    while (iterator.hasNext())
      ((ReplicationConnectionGroup)iterator.next()).promoteSlaveToMaster(paramString2); 
  }
  
  public static void registerJmx() throws SQLException {
    if (hasRegisteredJmx)
      return; 
    mbean.registerJmx();
    hasRegisteredJmx = true;
  }
  
  public static void removeMasterHost(String paramString1, String paramString2) throws SQLException {
    removeMasterHost(paramString1, paramString2, true);
  }
  
  public static void removeMasterHost(String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    while (iterator.hasNext())
      ((ReplicationConnectionGroup)iterator.next()).removeMasterHost(paramString2, paramBoolean); 
  }
  
  public static void removeSlaveHost(String paramString1, String paramString2) throws SQLException {
    removeSlaveHost(paramString1, paramString2, true);
  }
  
  public static void removeSlaveHost(String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    Iterator<ReplicationConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    while (iterator.hasNext())
      ((ReplicationConnectionGroup)iterator.next()).removeSlaveHost(paramString2, paramBoolean); 
  }
}
