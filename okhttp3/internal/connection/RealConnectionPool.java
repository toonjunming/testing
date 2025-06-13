package okhttp3.internal.connection;

import I1I.I丨iL.I1I.IL1Iii;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.Proxy;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.Address;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;

public final class RealConnectionPool {
  public static final boolean $assertionsDisabled = false;
  
  private static final Executor executor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
  
  private final Runnable cleanupRunnable = new IL1Iii(this);
  
  public boolean cleanupRunning;
  
  private final Deque<RealConnection> connections = new ArrayDeque<RealConnection>();
  
  private final long keepAliveDurationNs;
  
  private final int maxIdleConnections;
  
  public final RouteDatabase routeDatabase = new RouteDatabase();
  
  public RealConnectionPool(int paramInt, long paramLong, TimeUnit paramTimeUnit) {
    this.maxIdleConnections = paramInt;
    this.keepAliveDurationNs = paramTimeUnit.toNanos(paramLong);
    if (paramLong > 0L)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("keepAliveDuration <= 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private int pruneAndGetAllocationCount(RealConnection paramRealConnection, long paramLong) {
    List<Reference<Transmitter>> list = paramRealConnection.transmitters;
    byte b = 0;
    while (b < list.size()) {
      Reference reference = list.get(b);
      if (reference.get() != null) {
        b++;
        continue;
      } 
      reference = reference;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("A connection to ");
      stringBuilder.append(paramRealConnection.route().address().url());
      stringBuilder.append(" was leaked. Did you forget to close a response body?");
      String str = stringBuilder.toString();
      Platform.get().logCloseableLeak(str, ((Transmitter.TransmitterReference)reference).callStackTrace);
      list.remove(b);
      paramRealConnection.noNewExchanges = true;
      if (list.isEmpty()) {
        paramRealConnection.idleAtNanos = paramLong - this.keepAliveDurationNs;
        return 0;
      } 
    } 
    return list.size();
  }
  
  public long cleanup(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connections : Ljava/util/Deque;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore #12
    //   13: aconst_null
    //   14: astore #10
    //   16: ldc2_w -9223372036854775808
    //   19: lstore #6
    //   21: iconst_0
    //   22: istore #4
    //   24: iconst_0
    //   25: istore_3
    //   26: aload #12
    //   28: invokeinterface hasNext : ()Z
    //   33: ifeq -> 106
    //   36: aload #12
    //   38: invokeinterface next : ()Ljava/lang/Object;
    //   43: checkcast okhttp3/internal/connection/RealConnection
    //   46: astore #11
    //   48: aload_0
    //   49: aload #11
    //   51: lload_1
    //   52: invokespecial pruneAndGetAllocationCount : (Lokhttp3/internal/connection/RealConnection;J)I
    //   55: ifle -> 64
    //   58: iinc #3, 1
    //   61: goto -> 26
    //   64: iload #4
    //   66: iconst_1
    //   67: iadd
    //   68: istore #5
    //   70: lload_1
    //   71: aload #11
    //   73: getfield idleAtNanos : J
    //   76: lsub
    //   77: lstore #8
    //   79: iload #5
    //   81: istore #4
    //   83: lload #8
    //   85: lload #6
    //   87: lcmp
    //   88: ifle -> 26
    //   91: aload #11
    //   93: astore #10
    //   95: lload #8
    //   97: lstore #6
    //   99: iload #5
    //   101: istore #4
    //   103: goto -> 26
    //   106: aload_0
    //   107: getfield keepAliveDurationNs : J
    //   110: lstore_1
    //   111: lload #6
    //   113: lload_1
    //   114: lcmp
    //   115: ifge -> 161
    //   118: iload #4
    //   120: aload_0
    //   121: getfield maxIdleConnections : I
    //   124: if_icmple -> 130
    //   127: goto -> 161
    //   130: iload #4
    //   132: ifle -> 142
    //   135: aload_0
    //   136: monitorexit
    //   137: lload_1
    //   138: lload #6
    //   140: lsub
    //   141: lreturn
    //   142: iload_3
    //   143: ifle -> 150
    //   146: aload_0
    //   147: monitorexit
    //   148: lload_1
    //   149: lreturn
    //   150: aload_0
    //   151: iconst_0
    //   152: putfield cleanupRunning : Z
    //   155: aload_0
    //   156: monitorexit
    //   157: ldc2_w -1
    //   160: lreturn
    //   161: aload_0
    //   162: getfield connections : Ljava/util/Deque;
    //   165: aload #10
    //   167: invokeinterface remove : (Ljava/lang/Object;)Z
    //   172: pop
    //   173: aload_0
    //   174: monitorexit
    //   175: aload #10
    //   177: invokevirtual socket : ()Ljava/net/Socket;
    //   180: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   183: lconst_0
    //   184: lreturn
    //   185: astore #10
    //   187: aload_0
    //   188: monitorexit
    //   189: aload #10
    //   191: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	185	finally
    //   26	58	185	finally
    //   70	79	185	finally
    //   106	111	185	finally
    //   118	127	185	finally
    //   135	137	185	finally
    //   146	148	185	finally
    //   150	157	185	finally
    //   161	175	185	finally
    //   187	189	185	finally
  }
  
  public void connectFailed(Route paramRoute, IOException paramIOException) {
    if (paramRoute.proxy().type() != Proxy.Type.DIRECT) {
      Address address = paramRoute.address();
      address.proxySelector().connectFailed(address.url().uri(), paramRoute.proxy().address(), paramIOException);
    } 
    this.routeDatabase.failed(paramRoute);
  }
  
  public boolean connectionBecameIdle(RealConnection paramRealConnection) {
    if (paramRealConnection.noNewExchanges || this.maxIdleConnections == 0) {
      this.connections.remove(paramRealConnection);
      return true;
    } 
    notifyAll();
    return false;
  }
  
  public int connectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connections : Ljava/util/Deque;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public void evictAll() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield connections : Ljava/util/Deque;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_1
    //   20: aload_1
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 73
    //   29: aload_1
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast okhttp3/internal/connection/RealConnection
    //   38: astore_2
    //   39: aload_2
    //   40: getfield transmitters : Ljava/util/List;
    //   43: invokeinterface isEmpty : ()Z
    //   48: ifeq -> 20
    //   51: aload_2
    //   52: iconst_1
    //   53: putfield noNewExchanges : Z
    //   56: aload_3
    //   57: aload_2
    //   58: invokeinterface add : (Ljava/lang/Object;)Z
    //   63: pop
    //   64: aload_1
    //   65: invokeinterface remove : ()V
    //   70: goto -> 20
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_3
    //   76: invokeinterface iterator : ()Ljava/util/Iterator;
    //   81: astore_1
    //   82: aload_1
    //   83: invokeinterface hasNext : ()Z
    //   88: ifeq -> 109
    //   91: aload_1
    //   92: invokeinterface next : ()Ljava/lang/Object;
    //   97: checkcast okhttp3/internal/connection/RealConnection
    //   100: invokevirtual socket : ()Ljava/net/Socket;
    //   103: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   106: goto -> 82
    //   109: return
    //   110: astore_1
    //   111: aload_0
    //   112: monitorexit
    //   113: aload_1
    //   114: athrow
    // Exception table:
    //   from	to	target	type
    //   10	20	110	finally
    //   20	70	110	finally
    //   73	75	110	finally
    //   111	113	110	finally
  }
  
  public int idleConnectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_1
    //   4: aload_0
    //   5: getfield connections : Ljava/util/Deque;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_3
    //   14: aload_3
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 51
    //   23: aload_3
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: checkcast okhttp3/internal/connection/RealConnection
    //   32: getfield transmitters : Ljava/util/List;
    //   35: invokeinterface isEmpty : ()Z
    //   40: istore_2
    //   41: iload_2
    //   42: ifeq -> 14
    //   45: iinc #1, 1
    //   48: goto -> 14
    //   51: aload_0
    //   52: monitorexit
    //   53: iload_1
    //   54: ireturn
    //   55: astore_3
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_3
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   4	14	55	finally
    //   14	41	55	finally
  }
  
  public void put(RealConnection paramRealConnection) {
    if (!this.cleanupRunning) {
      this.cleanupRunning = true;
      executor.execute(this.cleanupRunnable);
    } 
    this.connections.add(paramRealConnection);
  }
  
  public boolean transmitterAcquirePooledConnection(Address paramAddress, Transmitter paramTransmitter, @Nullable List<Route> paramList, boolean paramBoolean) {
    for (RealConnection realConnection : this.connections) {
      if ((paramBoolean && !realConnection.isMultiplexed()) || !realConnection.isEligible(paramAddress, paramList))
        continue; 
      paramTransmitter.acquireConnectionNoEvents(realConnection);
      return true;
    } 
    return false;
  }
}
