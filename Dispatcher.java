package okhttp3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nullable;

public final class Dispatcher {
  public static final boolean $assertionsDisabled = false;
  
  @Nullable
  private ExecutorService executorService;
  
  @Nullable
  private Runnable idleCallback;
  
  private int maxRequests = 64;
  
  private int maxRequestsPerHost = 5;
  
  private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<RealCall.AsyncCall>();
  
  private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<RealCall.AsyncCall>();
  
  private final Deque<RealCall> runningSyncCalls = new ArrayDeque<RealCall>();
  
  public Dispatcher() {}
  
  public Dispatcher(ExecutorService paramExecutorService) {
    this.executorService = paramExecutorService;
  }
  
  @Nullable
  private RealCall.AsyncCall findExistingCallWithHost(String paramString) {
    for (RealCall.AsyncCall asyncCall : this.runningAsyncCalls) {
      if (asyncCall.host().equals(paramString))
        return asyncCall; 
    } 
    for (RealCall.AsyncCall asyncCall : this.readyAsyncCalls) {
      if (asyncCall.host().equals(paramString))
        return asyncCall; 
    } 
    return null;
  }
  
  private <T> void finished(Deque<T> paramDeque, T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_2
    //   4: invokeinterface remove : (Ljava/lang/Object;)Z
    //   9: ifeq -> 37
    //   12: aload_0
    //   13: getfield idleCallback : Ljava/lang/Runnable;
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_0
    //   20: invokespecial promoteAndExecute : ()Z
    //   23: ifne -> 36
    //   26: aload_1
    //   27: ifnull -> 36
    //   30: aload_1
    //   31: invokeinterface run : ()V
    //   36: return
    //   37: new java/lang/AssertionError
    //   40: astore_1
    //   41: aload_1
    //   42: ldc 'Call wasn't in-flight!'
    //   44: invokespecial <init> : (Ljava/lang/Object;)V
    //   47: aload_1
    //   48: athrow
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	49	finally
    //   37	49	49	finally
    //   50	52	49	finally
  }
  
  private boolean promoteAndExecute() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #6
    //   9: aload_0
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield readyAsyncCalls : Ljava/util/Deque;
    //   15: invokeinterface iterator : ()Ljava/util/Iterator;
    //   20: astore #5
    //   22: aload #5
    //   24: invokeinterface hasNext : ()Z
    //   29: ifeq -> 122
    //   32: aload #5
    //   34: invokeinterface next : ()Ljava/lang/Object;
    //   39: checkcast okhttp3/RealCall$AsyncCall
    //   42: astore #4
    //   44: aload_0
    //   45: getfield runningAsyncCalls : Ljava/util/Deque;
    //   48: invokeinterface size : ()I
    //   53: aload_0
    //   54: getfield maxRequests : I
    //   57: if_icmplt -> 63
    //   60: goto -> 122
    //   63: aload #4
    //   65: invokevirtual callsPerHost : ()Ljava/util/concurrent/atomic/AtomicInteger;
    //   68: invokevirtual get : ()I
    //   71: aload_0
    //   72: getfield maxRequestsPerHost : I
    //   75: if_icmplt -> 81
    //   78: goto -> 22
    //   81: aload #5
    //   83: invokeinterface remove : ()V
    //   88: aload #4
    //   90: invokevirtual callsPerHost : ()Ljava/util/concurrent/atomic/AtomicInteger;
    //   93: invokevirtual incrementAndGet : ()I
    //   96: pop
    //   97: aload #6
    //   99: aload #4
    //   101: invokeinterface add : (Ljava/lang/Object;)Z
    //   106: pop
    //   107: aload_0
    //   108: getfield runningAsyncCalls : Ljava/util/Deque;
    //   111: aload #4
    //   113: invokeinterface add : (Ljava/lang/Object;)Z
    //   118: pop
    //   119: goto -> 22
    //   122: aload_0
    //   123: invokevirtual runningCallsCount : ()I
    //   126: istore_2
    //   127: iconst_0
    //   128: istore_1
    //   129: iload_2
    //   130: ifle -> 138
    //   133: iconst_1
    //   134: istore_3
    //   135: goto -> 140
    //   138: iconst_0
    //   139: istore_3
    //   140: aload_0
    //   141: monitorexit
    //   142: aload #6
    //   144: invokeinterface size : ()I
    //   149: istore_2
    //   150: iload_1
    //   151: iload_2
    //   152: if_icmpge -> 179
    //   155: aload #6
    //   157: iload_1
    //   158: invokeinterface get : (I)Ljava/lang/Object;
    //   163: checkcast okhttp3/RealCall$AsyncCall
    //   166: aload_0
    //   167: invokevirtual executorService : ()Ljava/util/concurrent/ExecutorService;
    //   170: invokevirtual executeOn : (Ljava/util/concurrent/ExecutorService;)V
    //   173: iinc #1, 1
    //   176: goto -> 150
    //   179: iload_3
    //   180: ireturn
    //   181: astore #4
    //   183: aload_0
    //   184: monitorexit
    //   185: aload #4
    //   187: athrow
    // Exception table:
    //   from	to	target	type
    //   11	22	181	finally
    //   22	60	181	finally
    //   63	78	181	finally
    //   81	119	181	finally
    //   122	127	181	finally
    //   140	142	181	finally
    //   183	185	181	finally
  }
  
  public void cancelAll() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readyAsyncCalls : Ljava/util/Deque;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_1
    //   12: aload_1
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 39
    //   21: aload_1
    //   22: invokeinterface next : ()Ljava/lang/Object;
    //   27: checkcast okhttp3/RealCall$AsyncCall
    //   30: invokevirtual get : ()Lokhttp3/RealCall;
    //   33: invokevirtual cancel : ()V
    //   36: goto -> 12
    //   39: aload_0
    //   40: getfield runningAsyncCalls : Ljava/util/Deque;
    //   43: invokeinterface iterator : ()Ljava/util/Iterator;
    //   48: astore_1
    //   49: aload_1
    //   50: invokeinterface hasNext : ()Z
    //   55: ifeq -> 76
    //   58: aload_1
    //   59: invokeinterface next : ()Ljava/lang/Object;
    //   64: checkcast okhttp3/RealCall$AsyncCall
    //   67: invokevirtual get : ()Lokhttp3/RealCall;
    //   70: invokevirtual cancel : ()V
    //   73: goto -> 49
    //   76: aload_0
    //   77: getfield runningSyncCalls : Ljava/util/Deque;
    //   80: invokeinterface iterator : ()Ljava/util/Iterator;
    //   85: astore_1
    //   86: aload_1
    //   87: invokeinterface hasNext : ()Z
    //   92: ifeq -> 110
    //   95: aload_1
    //   96: invokeinterface next : ()Ljava/lang/Object;
    //   101: checkcast okhttp3/RealCall
    //   104: invokevirtual cancel : ()V
    //   107: goto -> 86
    //   110: aload_0
    //   111: monitorexit
    //   112: return
    //   113: astore_1
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_1
    //   117: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	113	finally
    //   12	36	113	finally
    //   39	49	113	finally
    //   49	73	113	finally
    //   76	86	113	finally
    //   86	107	113	finally
  }
  
  public void enqueue(RealCall.AsyncCall paramAsyncCall) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readyAsyncCalls : Ljava/util/Deque;
    //   6: aload_1
    //   7: invokeinterface add : (Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_1
    //   14: invokevirtual get : ()Lokhttp3/RealCall;
    //   17: getfield forWebSocket : Z
    //   20: ifne -> 41
    //   23: aload_0
    //   24: aload_1
    //   25: invokevirtual host : ()Ljava/lang/String;
    //   28: invokespecial findExistingCallWithHost : (Ljava/lang/String;)Lokhttp3/RealCall$AsyncCall;
    //   31: astore_2
    //   32: aload_2
    //   33: ifnull -> 41
    //   36: aload_1
    //   37: aload_2
    //   38: invokevirtual reuseCallsPerHostFrom : (Lokhttp3/RealCall$AsyncCall;)V
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_0
    //   44: invokespecial promoteAndExecute : ()Z
    //   47: pop
    //   48: return
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	49	finally
    //   36	41	49	finally
    //   41	43	49	finally
    //   50	52	49	finally
  }
  
  public void executed(RealCall paramRealCall) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield runningSyncCalls : Ljava/util/Deque;
    //   6: aload_1
    //   7: invokeinterface add : (Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  public ExecutorService executorService() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   6: ifnonnull -> 48
    //   9: new java/util/concurrent/ThreadPoolExecutor
    //   12: astore_1
    //   13: getstatic java/util/concurrent/TimeUnit.SECONDS : Ljava/util/concurrent/TimeUnit;
    //   16: astore_2
    //   17: new java/util/concurrent/SynchronousQueue
    //   20: astore_3
    //   21: aload_3
    //   22: invokespecial <init> : ()V
    //   25: aload_1
    //   26: iconst_0
    //   27: ldc 2147483647
    //   29: ldc2_w 60
    //   32: aload_2
    //   33: aload_3
    //   34: ldc 'OkHttp Dispatcher'
    //   36: iconst_0
    //   37: invokestatic threadFactory : (Ljava/lang/String;Z)Ljava/util/concurrent/ThreadFactory;
    //   40: invokespecial <init> : (IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;)V
    //   43: aload_0
    //   44: aload_1
    //   45: putfield executorService : Ljava/util/concurrent/ExecutorService;
    //   48: aload_0
    //   49: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: areturn
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	48	57	finally
    //   48	53	57	finally
  }
  
  public void finished(RealCall.AsyncCall paramAsyncCall) {
    paramAsyncCall.callsPerHost().decrementAndGet();
    finished(this.runningAsyncCalls, paramAsyncCall);
  }
  
  public void finished(RealCall paramRealCall) {
    finished(this.runningSyncCalls, paramRealCall);
  }
  
  public int getMaxRequests() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxRequests : I
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
  
  public int getMaxRequestsPerHost() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxRequestsPerHost : I
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
  
  public List<Call> queuedCalls() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield readyAsyncCalls : Ljava/util/Deque;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_2
    //   20: aload_2
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 51
    //   29: aload_1
    //   30: aload_2
    //   31: invokeinterface next : ()Ljava/lang/Object;
    //   36: checkcast okhttp3/RealCall$AsyncCall
    //   39: invokevirtual get : ()Lokhttp3/RealCall;
    //   42: invokeinterface add : (Ljava/lang/Object;)Z
    //   47: pop
    //   48: goto -> 20
    //   51: aload_1
    //   52: invokestatic unmodifiableList : (Ljava/util/List;)Ljava/util/List;
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: areturn
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	60	finally
    //   20	48	60	finally
    //   51	56	60	finally
  }
  
  public int queuedCallsCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readyAsyncCalls : Ljava/util/Deque;
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
  
  public List<Call> runningCalls() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_2
    //   11: aload_0
    //   12: getfield runningSyncCalls : Ljava/util/Deque;
    //   15: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   20: pop
    //   21: aload_0
    //   22: getfield runningAsyncCalls : Ljava/util/Deque;
    //   25: invokeinterface iterator : ()Ljava/util/Iterator;
    //   30: astore_1
    //   31: aload_1
    //   32: invokeinterface hasNext : ()Z
    //   37: ifeq -> 62
    //   40: aload_2
    //   41: aload_1
    //   42: invokeinterface next : ()Ljava/lang/Object;
    //   47: checkcast okhttp3/RealCall$AsyncCall
    //   50: invokevirtual get : ()Lokhttp3/RealCall;
    //   53: invokeinterface add : (Ljava/lang/Object;)Z
    //   58: pop
    //   59: goto -> 31
    //   62: aload_2
    //   63: invokestatic unmodifiableList : (Ljava/util/List;)Ljava/util/List;
    //   66: astore_1
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_1
    //   70: areturn
    //   71: astore_1
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_1
    //   75: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	71	finally
    //   31	59	71	finally
    //   62	67	71	finally
  }
  
  public int runningCallsCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield runningAsyncCalls : Ljava/util/Deque;
    //   6: invokeinterface size : ()I
    //   11: istore_2
    //   12: aload_0
    //   13: getfield runningSyncCalls : Ljava/util/Deque;
    //   16: invokeinterface size : ()I
    //   21: istore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: iload_2
    //   25: iload_1
    //   26: iadd
    //   27: ireturn
    //   28: astore_3
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_3
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	28	finally
  }
  
  public void setIdleCallback(@Nullable Runnable paramRunnable) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield idleCallback : Ljava/lang/Runnable;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void setMaxRequests(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: iconst_1
    //   2: if_icmplt -> 25
    //   5: aload_0
    //   6: monitorenter
    //   7: aload_0
    //   8: iload_1
    //   9: putfield maxRequests : I
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_0
    //   15: invokespecial promoteAndExecute : ()Z
    //   18: pop
    //   19: return
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: athrow
    //   25: new java/lang/StringBuilder
    //   28: dup
    //   29: invokespecial <init> : ()V
    //   32: astore_2
    //   33: aload_2
    //   34: ldc 'max < 1: '
    //   36: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   39: pop
    //   40: aload_2
    //   41: iload_1
    //   42: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: new java/lang/IllegalArgumentException
    //   49: dup
    //   50: aload_2
    //   51: invokevirtual toString : ()Ljava/lang/String;
    //   54: invokespecial <init> : (Ljava/lang/String;)V
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   7	14	20	finally
    //   21	23	20	finally
  }
  
  public void setMaxRequestsPerHost(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: iconst_1
    //   2: if_icmplt -> 25
    //   5: aload_0
    //   6: monitorenter
    //   7: aload_0
    //   8: iload_1
    //   9: putfield maxRequestsPerHost : I
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_0
    //   15: invokespecial promoteAndExecute : ()Z
    //   18: pop
    //   19: return
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: athrow
    //   25: new java/lang/StringBuilder
    //   28: dup
    //   29: invokespecial <init> : ()V
    //   32: astore_2
    //   33: aload_2
    //   34: ldc 'max < 1: '
    //   36: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   39: pop
    //   40: aload_2
    //   41: iload_1
    //   42: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: new java/lang/IllegalArgumentException
    //   49: dup
    //   50: aload_2
    //   51: invokevirtual toString : ()Ljava/lang/String;
    //   54: invokespecial <init> : (Ljava/lang/String;)V
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   7	14	20	finally
    //   21	23	20	finally
  }
}
