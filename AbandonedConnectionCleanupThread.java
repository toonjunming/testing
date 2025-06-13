package com.mysql.jdbc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AbandonedConnectionCleanupThread implements Runnable {
  private static final ExecutorService cleanupThreadExcecutorService;
  
  public static Thread threadRef;
  
  static {
    ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
          public Thread newThread(Runnable param1Runnable) {
            param1Runnable = new Thread(param1Runnable, "Abandoned connection cleanup thread");
            param1Runnable.setDaemon(true);
            param1Runnable.setContextClassLoader(AbandonedConnectionCleanupThread.class.getClassLoader());
            AbandonedConnectionCleanupThread.threadRef = (Thread)param1Runnable;
            return (Thread)param1Runnable;
          }
        });
    cleanupThreadExcecutorService = executorService;
    executorService.execute(new AbandonedConnectionCleanupThread());
  }
  
  private void checkContextClassLoaders() {
    try {
      threadRef.getContextClassLoader().getResource("");
    } finally {
      Exception exception = null;
    } 
  }
  
  public static void checkedShutdown() {
    shutdown(true);
  }
  
  private static boolean consistentClassLoaders() {
    boolean bool;
    ClassLoader classLoader2 = Thread.currentThread().getContextClassLoader();
    ClassLoader classLoader1 = threadRef.getContextClassLoader();
    if (classLoader2 != null && classLoader1 != null && classLoader2 == classLoader1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public static void shutdown() {
    checkedShutdown();
  }
  
  private static void shutdown(boolean paramBoolean) {
    if (paramBoolean && !consistentClassLoaders())
      return; 
    cleanupThreadExcecutorService.shutdownNow();
  }
  
  public static void uncheckedShutdown() {
    shutdown(false);
  }
  
  public void run() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial checkContextClassLoaders : ()V
    //   4: getstatic com/mysql/jdbc/NonRegisteringDriver.refQueue : Ljava/lang/ref/ReferenceQueue;
    //   7: ldc2_w 5000
    //   10: invokevirtual remove : (J)Ljava/lang/ref/Reference;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnull -> 0
    //   18: aload_2
    //   19: checkcast com/mysql/jdbc/NonRegisteringDriver$ConnectionPhantomReference
    //   22: invokevirtual cleanup : ()V
    //   25: getstatic com/mysql/jdbc/NonRegisteringDriver.connectionPhantomRefs : Ljava/util/concurrent/ConcurrentHashMap;
    //   28: aload_2
    //   29: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   32: pop
    //   33: goto -> 0
    //   36: astore_1
    //   37: getstatic com/mysql/jdbc/NonRegisteringDriver.connectionPhantomRefs : Ljava/util/concurrent/ConcurrentHashMap;
    //   40: aload_2
    //   41: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   44: pop
    //   45: aload_1
    //   46: athrow
    //   47: astore_1
    //   48: aconst_null
    //   49: putstatic com/mysql/jdbc/AbandonedConnectionCleanupThread.threadRef : Ljava/lang/Thread;
    //   52: return
    //   53: astore_1
    //   54: goto -> 0
    // Exception table:
    //   from	to	target	type
    //   0	14	47	java/lang/InterruptedException
    //   0	14	53	java/lang/Exception
    //   18	25	36	finally
    //   25	33	47	java/lang/InterruptedException
    //   25	33	53	java/lang/Exception
    //   37	47	47	java/lang/InterruptedException
    //   37	47	53	java/lang/Exception
  }
}
