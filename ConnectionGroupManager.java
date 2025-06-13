package com.mysql.jdbc;

import com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConnectionGroupManager {
  private static HashMap<String, ConnectionGroup> GROUP_MAP = new HashMap<String, ConnectionGroup>();
  
  private static boolean hasRegisteredJmx;
  
  private static LoadBalanceConnectionGroupManager mbean = new LoadBalanceConnectionGroupManager();
  
  static {
    hasRegisteredJmx = false;
  }
  
  public static void addHost(String paramString1, String paramString2, boolean paramBoolean) {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    while (iterator.hasNext())
      ((ConnectionGroup)iterator.next()).addHost(paramString2, paramBoolean); 
  }
  
  public static int getActiveHostCount(String paramString) {
    HashSet<String> hashSet = new HashSet();
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    while (iterator.hasNext())
      hashSet.addAll(((ConnectionGroup)iterator.next()).getInitialHosts()); 
    return hashSet.size();
  }
  
  public static String getActiveHostLists(String paramString) {
    Collection<ConnectionGroup> collection = getGroupsMatching(paramString);
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    Iterator<ConnectionGroup> iterator1 = collection.iterator();
    while (iterator1.hasNext()) {
      for (String str1 : ((ConnectionGroup)iterator1.next()).getInitialHosts()) {
        Integer integer = (Integer)hashMap.get(str1);
        if (integer == null) {
          integer = Integer.valueOf(1);
        } else {
          integer = Integer.valueOf(integer.intValue() + 1);
        } 
        hashMap.put(str1, integer);
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<String> iterator = hashMap.keySet().iterator();
    for (String str = ""; iterator.hasNext(); str = ",") {
      String str1 = iterator.next();
      stringBuilder.append(str);
      stringBuilder.append(str1);
      stringBuilder.append('(');
      stringBuilder.append(hashMap.get(str1));
      stringBuilder.append(')');
    } 
    return stringBuilder.toString();
  }
  
  public static long getActiveLogicalConnectionCount(String paramString) {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    int i;
    for (i = 0; iterator.hasNext(); i = (int)(i + connectionGroup.getActiveLogicalConnectionCount()))
      ConnectionGroup connectionGroup = iterator.next(); 
    return i;
  }
  
  public static long getActivePhysicalConnectionCount(String paramString) {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    int i;
    for (i = 0; iterator.hasNext(); i = (int)(i + connectionGroup.getActivePhysicalConnectionCount()))
      ConnectionGroup connectionGroup = iterator.next(); 
    return i;
  }
  
  public static ConnectionGroup getConnectionGroup(String paramString) {
    return GROUP_MAP.get(paramString);
  }
  
  public static ConnectionGroup getConnectionGroupInstance(String paramString) {
    // Byte code:
    //   0: ldc com/mysql/jdbc/ConnectionGroupManager
    //   2: monitorenter
    //   3: getstatic com/mysql/jdbc/ConnectionGroupManager.GROUP_MAP : Ljava/util/HashMap;
    //   6: aload_0
    //   7: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   10: ifeq -> 29
    //   13: getstatic com/mysql/jdbc/ConnectionGroupManager.GROUP_MAP : Ljava/util/HashMap;
    //   16: aload_0
    //   17: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   20: checkcast com/mysql/jdbc/ConnectionGroup
    //   23: astore_0
    //   24: ldc com/mysql/jdbc/ConnectionGroupManager
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: new com/mysql/jdbc/ConnectionGroup
    //   32: astore_1
    //   33: aload_1
    //   34: aload_0
    //   35: invokespecial <init> : (Ljava/lang/String;)V
    //   38: getstatic com/mysql/jdbc/ConnectionGroupManager.GROUP_MAP : Ljava/util/HashMap;
    //   41: aload_0
    //   42: aload_1
    //   43: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   46: pop
    //   47: ldc com/mysql/jdbc/ConnectionGroupManager
    //   49: monitorexit
    //   50: aload_1
    //   51: areturn
    //   52: astore_0
    //   53: ldc com/mysql/jdbc/ConnectionGroupManager
    //   55: monitorexit
    //   56: aload_0
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   3	24	52	finally
    //   29	47	52	finally
  }
  
  private static Collection<ConnectionGroup> getGroupsMatching(String paramString) {
    HashSet<ConnectionGroup> hashSet1;
    if (paramString == null || paramString.equals("")) {
      hashSet1 = new HashSet();
      hashSet1.addAll(GROUP_MAP.values());
      return hashSet1;
    } 
    HashSet<ConnectionGroup> hashSet2 = new HashSet();
    ConnectionGroup connectionGroup = GROUP_MAP.get(hashSet1);
    if (connectionGroup != null)
      hashSet2.add(connectionGroup); 
    return hashSet2;
  }
  
  public static String getRegisteredConnectionGroups() {
    Collection<ConnectionGroup> collection = getGroupsMatching(null);
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<ConnectionGroup> iterator = collection.iterator();
    for (String str = ""; iterator.hasNext(); str = ",") {
      String str1 = ((ConnectionGroup)iterator.next()).getGroupName();
      stringBuilder.append(str);
      stringBuilder.append(str1);
    } 
    return stringBuilder.toString();
  }
  
  public static int getTotalHostCount(String paramString) {
    Collection<ConnectionGroup> collection = getGroupsMatching(paramString);
    HashSet<String> hashSet = new HashSet();
    for (ConnectionGroup connectionGroup : collection) {
      hashSet.addAll(connectionGroup.getInitialHosts());
      hashSet.addAll(connectionGroup.getClosedHosts());
    } 
    return hashSet.size();
  }
  
  public static long getTotalLogicalConnectionCount(String paramString) {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    long l;
    for (l = 0L; iterator.hasNext(); l += ((ConnectionGroup)iterator.next()).getTotalLogicalConnectionCount());
    return l;
  }
  
  public static long getTotalPhysicalConnectionCount(String paramString) {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    long l;
    for (l = 0L; iterator.hasNext(); l += ((ConnectionGroup)iterator.next()).getTotalPhysicalConnectionCount());
    return l;
  }
  
  public static long getTotalTransactionCount(String paramString) {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString).iterator();
    long l;
    for (l = 0L; iterator.hasNext(); l += ((ConnectionGroup)iterator.next()).getTotalTransactionCount());
    return l;
  }
  
  public static void registerJmx() throws SQLException {
    if (hasRegisteredJmx)
      return; 
    mbean.registerJmx();
    hasRegisteredJmx = true;
  }
  
  public static void removeHost(String paramString1, String paramString2) throws SQLException {
    removeHost(paramString1, paramString2, false);
  }
  
  public static void removeHost(String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    Iterator<ConnectionGroup> iterator = getGroupsMatching(paramString1).iterator();
    while (iterator.hasNext())
      ((ConnectionGroup)iterator.next()).removeHost(paramString2, paramBoolean); 
  }
}
