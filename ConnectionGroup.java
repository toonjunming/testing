package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConnectionGroup {
  private long activeConnections = 0L;
  
  private int activeHosts = 0;
  
  private Set<String> closedHosts = new HashSet<String>();
  
  private long closedProxyTotalPhysicalConnections = 0L;
  
  private long closedProxyTotalTransactions = 0L;
  
  private HashMap<Long, LoadBalancedConnectionProxy> connectionProxies = new HashMap<Long, LoadBalancedConnectionProxy>();
  
  private long connections = 0L;
  
  private String groupName;
  
  private Set<String> hostList = new HashSet<String>();
  
  private boolean isInitialized = false;
  
  public ConnectionGroup(String paramString) {
    this.groupName = paramString;
  }
  
  public void addHost(String paramString) {
    addHost(paramString, false);
  }
  
  public void addHost(String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hostList : Ljava/util/Set;
    //   6: aload_1
    //   7: invokeinterface add : (Ljava/lang/Object;)Z
    //   12: ifeq -> 25
    //   15: aload_0
    //   16: aload_0
    //   17: getfield activeHosts : I
    //   20: iconst_1
    //   21: iadd
    //   22: putfield activeHosts : I
    //   25: aload_0
    //   26: monitorexit
    //   27: iload_2
    //   28: ifne -> 32
    //   31: return
    //   32: new java/util/HashMap
    //   35: dup
    //   36: invokespecial <init> : ()V
    //   39: astore #4
    //   41: aload_0
    //   42: getfield connectionProxies : Ljava/util/HashMap;
    //   45: astore_3
    //   46: aload_3
    //   47: monitorenter
    //   48: aload #4
    //   50: aload_0
    //   51: getfield connectionProxies : Ljava/util/HashMap;
    //   54: invokeinterface putAll : (Ljava/util/Map;)V
    //   59: aload_3
    //   60: monitorexit
    //   61: aload #4
    //   63: invokeinterface values : ()Ljava/util/Collection;
    //   68: invokeinterface iterator : ()Ljava/util/Iterator;
    //   73: astore_3
    //   74: aload_3
    //   75: invokeinterface hasNext : ()Z
    //   80: ifeq -> 100
    //   83: aload_3
    //   84: invokeinterface next : ()Ljava/lang/Object;
    //   89: checkcast com/mysql/jdbc/LoadBalancedConnectionProxy
    //   92: aload_1
    //   93: invokevirtual addHost : (Ljava/lang/String;)Z
    //   96: pop
    //   97: goto -> 74
    //   100: return
    //   101: astore_1
    //   102: aload_3
    //   103: monitorexit
    //   104: aload_1
    //   105: athrow
    //   106: astore_1
    //   107: aload_0
    //   108: monitorexit
    //   109: aload_1
    //   110: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	106	finally
    //   25	27	106	finally
    //   48	61	101	finally
    //   102	104	101	finally
    //   107	109	106	finally
  }
  
  public void closeConnectionProxy(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy) {
    this.activeConnections--;
    this.connectionProxies.remove(Long.valueOf(paramLoadBalancedConnectionProxy.getConnectionGroupProxyID()));
    this.closedProxyTotalPhysicalConnections += paramLoadBalancedConnectionProxy.getTotalPhysicalConnectionCount();
    this.closedProxyTotalTransactions += paramLoadBalancedConnectionProxy.getTransactionCount();
  }
  
  public int getActiveHostCount() {
    return this.activeHosts;
  }
  
  public long getActiveLogicalConnectionCount() {
    return this.activeConnections;
  }
  
  public long getActivePhysicalConnectionCount() {
    HashMap<Long, LoadBalancedConnectionProxy> hashMap;
    Iterator<LoadBalancedConnectionProxy> iterator;
    null = new HashMap<Object, Object>();
    synchronized (this.connectionProxies) {
      null.putAll(this.connectionProxies);
      iterator = null.values().iterator();
      long l;
      for (l = 0L; iterator.hasNext(); l += ((LoadBalancedConnectionProxy)iterator.next()).getActivePhysicalConnectionCount());
      return l;
    } 
  }
  
  public Collection<String> getClosedHosts() {
    return this.closedHosts;
  }
  
  public String getGroupName() {
    return this.groupName;
  }
  
  public Collection<String> getInitialHosts() {
    return this.hostList;
  }
  
  public long getTotalLogicalConnectionCount() {
    return this.connections;
  }
  
  public long getTotalPhysicalConnectionCount() {
    HashMap<Long, LoadBalancedConnectionProxy> hashMap;
    Iterator<LoadBalancedConnectionProxy> iterator;
    long l = this.closedProxyTotalPhysicalConnections;
    null = new HashMap<Object, Object>();
    synchronized (this.connectionProxies) {
      null.putAll(this.connectionProxies);
      iterator = null.values().iterator();
      while (iterator.hasNext())
        l += ((LoadBalancedConnectionProxy)iterator.next()).getTotalPhysicalConnectionCount(); 
      return l;
    } 
  }
  
  public long getTotalTransactionCount() {
    HashMap<Long, LoadBalancedConnectionProxy> hashMap;
    Iterator<LoadBalancedConnectionProxy> iterator;
    long l = this.closedProxyTotalTransactions;
    null = new HashMap<Object, Object>();
    synchronized (this.connectionProxies) {
      null.putAll(this.connectionProxies);
      iterator = null.values().iterator();
      while (iterator.hasNext())
        l += ((LoadBalancedConnectionProxy)iterator.next()).getTransactionCount(); 
      return l;
    } 
  }
  
  public long registerConnectionProxy(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy, List<String> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isInitialized : Z
    //   6: ifne -> 35
    //   9: aload_0
    //   10: getfield hostList : Ljava/util/Set;
    //   13: aload_2
    //   14: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   19: pop
    //   20: aload_0
    //   21: iconst_1
    //   22: putfield isInitialized : Z
    //   25: aload_0
    //   26: aload_2
    //   27: invokeinterface size : ()I
    //   32: putfield activeHosts : I
    //   35: aload_0
    //   36: getfield connections : J
    //   39: lconst_1
    //   40: ladd
    //   41: lstore_3
    //   42: aload_0
    //   43: lload_3
    //   44: putfield connections : J
    //   47: aload_0
    //   48: getfield connectionProxies : Ljava/util/HashMap;
    //   51: lload_3
    //   52: invokestatic valueOf : (J)Ljava/lang/Long;
    //   55: aload_1
    //   56: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   59: pop
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_0
    //   63: aload_0
    //   64: getfield activeConnections : J
    //   67: lconst_1
    //   68: ladd
    //   69: putfield activeConnections : J
    //   72: lload_3
    //   73: lreturn
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   2	35	74	finally
    //   35	62	74	finally
    //   75	77	74	finally
  }
  
  public void removeHost(String paramString) throws SQLException {
    removeHost(paramString, false);
  }
  
  public void removeHost(String paramString, boolean paramBoolean) throws SQLException {
    removeHost(paramString, paramBoolean, true);
  }
  
  public void removeHost(String paramString, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield activeHosts : I
    //   6: iconst_1
    //   7: if_icmpeq -> 183
    //   10: aload_0
    //   11: getfield hostList : Ljava/util/Set;
    //   14: aload_1
    //   15: invokeinterface remove : (Ljava/lang/Object;)Z
    //   20: ifeq -> 148
    //   23: aload_0
    //   24: aload_0
    //   25: getfield activeHosts : I
    //   28: iconst_1
    //   29: isub
    //   30: putfield activeHosts : I
    //   33: iload_2
    //   34: ifeq -> 134
    //   37: new java/util/HashMap
    //   40: astore #5
    //   42: aload #5
    //   44: invokespecial <init> : ()V
    //   47: aload_0
    //   48: getfield connectionProxies : Ljava/util/HashMap;
    //   51: astore #4
    //   53: aload #4
    //   55: monitorenter
    //   56: aload #5
    //   58: aload_0
    //   59: getfield connectionProxies : Ljava/util/HashMap;
    //   62: invokeinterface putAll : (Ljava/util/Map;)V
    //   67: aload #4
    //   69: monitorexit
    //   70: aload #5
    //   72: invokeinterface values : ()Ljava/util/Collection;
    //   77: invokeinterface iterator : ()Ljava/util/Iterator;
    //   82: astore #5
    //   84: aload #5
    //   86: invokeinterface hasNext : ()Z
    //   91: ifeq -> 134
    //   94: aload #5
    //   96: invokeinterface next : ()Ljava/lang/Object;
    //   101: checkcast com/mysql/jdbc/LoadBalancedConnectionProxy
    //   104: astore #4
    //   106: iload_3
    //   107: ifeq -> 119
    //   110: aload #4
    //   112: aload_1
    //   113: invokevirtual removeHostWhenNotInUse : (Ljava/lang/String;)V
    //   116: goto -> 84
    //   119: aload #4
    //   121: aload_1
    //   122: invokevirtual removeHost : (Ljava/lang/String;)V
    //   125: goto -> 84
    //   128: astore_1
    //   129: aload #4
    //   131: monitorexit
    //   132: aload_1
    //   133: athrow
    //   134: aload_0
    //   135: getfield closedHosts : Ljava/util/Set;
    //   138: aload_1
    //   139: invokeinterface add : (Ljava/lang/Object;)Z
    //   144: pop
    //   145: aload_0
    //   146: monitorexit
    //   147: return
    //   148: new java/lang/StringBuilder
    //   151: astore #4
    //   153: aload #4
    //   155: invokespecial <init> : ()V
    //   158: aload #4
    //   160: ldc 'Host is not configured: '
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload #4
    //   168: aload_1
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload #4
    //   175: invokevirtual toString : ()Ljava/lang/String;
    //   178: aconst_null
    //   179: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   182: athrow
    //   183: ldc 'Cannot remove host, only one configured host active.'
    //   185: aconst_null
    //   186: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   189: athrow
    //   190: astore_1
    //   191: aload_0
    //   192: monitorexit
    //   193: aload_1
    //   194: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	190	finally
    //   37	56	190	finally
    //   56	70	128	finally
    //   70	84	190	finally
    //   84	106	190	finally
    //   110	116	190	finally
    //   119	125	190	finally
    //   129	132	128	finally
    //   132	134	190	finally
    //   134	145	190	finally
    //   148	183	190	finally
    //   183	190	190	finally
  }
}
