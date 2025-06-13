package okhttp3.internal.http2;

import I丨L.I1I;
import I丨L.I丨L;
import I丨L.iI丨LLL1;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;

public final class Http2Connection implements Closeable {
  public static final boolean $assertionsDisabled = false;
  
  public static final int AWAIT_PING = 3;
  
  public static final int DEGRADED_PING = 2;
  
  public static final long DEGRADED_PONG_TIMEOUT_NS = 1000000000L;
  
  public static final int INTERVAL_PING = 1;
  
  public static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
  
  private static final ExecutorService listenerExecutor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Http2Connection", true));
  
  private long awaitPingsSent;
  
  private long awaitPongsReceived;
  
  public long bytesLeftInWriteWindow;
  
  public final boolean client;
  
  public final String connectionName;
  
  public final Set<Integer> currentPushRequests;
  
  private long degradedPingsSent;
  
  private long degradedPongDeadlineNs;
  
  private long degradedPongsReceived;
  
  private long intervalPingsSent;
  
  private long intervalPongsReceived;
  
  public int lastGoodStreamId;
  
  public final Listener listener;
  
  public int nextStreamId;
  
  public Settings okHttpSettings;
  
  public final Settings peerSettings;
  
  private final ExecutorService pushExecutor;
  
  public final PushObserver pushObserver;
  
  public final ReaderRunnable readerRunnable;
  
  private boolean shutdown;
  
  public final Socket socket;
  
  public final Map<Integer, Http2Stream> streams;
  
  public long unacknowledgedBytesRead;
  
  public final Http2Writer writer;
  
  private final ScheduledExecutorService writerExecutor;
  
  public Http2Connection(Builder paramBuilder) {
    int i;
    this.streams = new LinkedHashMap<Integer, Http2Stream>();
    this.intervalPingsSent = 0L;
    this.intervalPongsReceived = 0L;
    this.degradedPingsSent = 0L;
    this.degradedPongsReceived = 0L;
    this.awaitPingsSent = 0L;
    this.awaitPongsReceived = 0L;
    this.degradedPongDeadlineNs = 0L;
    this.unacknowledgedBytesRead = 0L;
    this.okHttpSettings = new Settings();
    Settings settings = new Settings();
    this.peerSettings = settings;
    this.currentPushRequests = new LinkedHashSet<Integer>();
    this.pushObserver = paramBuilder.pushObserver;
    boolean bool = paramBuilder.client;
    this.client = bool;
    this.listener = paramBuilder.listener;
    if (bool) {
      i = 1;
    } else {
      i = 2;
    } 
    this.nextStreamId = i;
    if (bool)
      this.nextStreamId = i + 2; 
    if (bool)
      this.okHttpSettings.set(7, 16777216); 
    String str = paramBuilder.connectionName;
    this.connectionName = str;
    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", new Object[] { str }), false));
    this.writerExecutor = scheduledThreadPoolExecutor;
    if (paramBuilder.pingIntervalMillis != 0) {
      IntervalPingRunnable intervalPingRunnable = new IntervalPingRunnable();
      i = paramBuilder.pingIntervalMillis;
      scheduledThreadPoolExecutor.scheduleAtFixedRate(intervalPingRunnable, i, i, TimeUnit.MILLISECONDS);
    } 
    this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", new Object[] { str }), true));
    settings.set(7, 65535);
    settings.set(5, 16384);
    this.bytesLeftInWriteWindow = settings.getInitialWindowSize();
    this.socket = paramBuilder.socket;
    this.writer = new Http2Writer(paramBuilder.sink, bool);
    this.readerRunnable = new ReaderRunnable(new Http2Reader(paramBuilder.source, bool));
  }
  
  private void failConnection(@Nullable IOException paramIOException) {
    ErrorCode errorCode = ErrorCode.PROTOCOL_ERROR;
    close(errorCode, errorCode, paramIOException);
  }
  
  private Http2Stream newStream(int paramInt, List<Header> paramList, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: iload_3
    //   1: iconst_1
    //   2: ixor
    //   3: istore #6
    //   5: aload_0
    //   6: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   9: astore #7
    //   11: aload #7
    //   13: monitorenter
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield nextStreamId : I
    //   20: ldc_w 1073741823
    //   23: if_icmple -> 33
    //   26: aload_0
    //   27: getstatic okhttp3/internal/http2/ErrorCode.REFUSED_STREAM : Lokhttp3/internal/http2/ErrorCode;
    //   30: invokevirtual shutdown : (Lokhttp3/internal/http2/ErrorCode;)V
    //   33: aload_0
    //   34: getfield shutdown : Z
    //   37: ifne -> 201
    //   40: aload_0
    //   41: getfield nextStreamId : I
    //   44: istore #5
    //   46: aload_0
    //   47: iload #5
    //   49: iconst_2
    //   50: iadd
    //   51: putfield nextStreamId : I
    //   54: new okhttp3/internal/http2/Http2Stream
    //   57: astore #8
    //   59: aload #8
    //   61: iload #5
    //   63: aload_0
    //   64: iload #6
    //   66: iconst_0
    //   67: aconst_null
    //   68: invokespecial <init> : (ILokhttp3/internal/http2/Http2Connection;ZZLokhttp3/Headers;)V
    //   71: iload_3
    //   72: ifeq -> 103
    //   75: aload_0
    //   76: getfield bytesLeftInWriteWindow : J
    //   79: lconst_0
    //   80: lcmp
    //   81: ifeq -> 103
    //   84: aload #8
    //   86: getfield bytesLeftInWriteWindow : J
    //   89: lconst_0
    //   90: lcmp
    //   91: ifne -> 97
    //   94: goto -> 103
    //   97: iconst_0
    //   98: istore #4
    //   100: goto -> 106
    //   103: iconst_1
    //   104: istore #4
    //   106: aload #8
    //   108: invokevirtual isOpen : ()Z
    //   111: ifeq -> 131
    //   114: aload_0
    //   115: getfield streams : Ljava/util/Map;
    //   118: iload #5
    //   120: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   123: aload #8
    //   125: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   130: pop
    //   131: aload_0
    //   132: monitorexit
    //   133: iload_1
    //   134: ifne -> 152
    //   137: aload_0
    //   138: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   141: iload #6
    //   143: iload #5
    //   145: aload_2
    //   146: invokevirtual headers : (ZILjava/util/List;)V
    //   149: goto -> 170
    //   152: aload_0
    //   153: getfield client : Z
    //   156: ifne -> 188
    //   159: aload_0
    //   160: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   163: iload_1
    //   164: iload #5
    //   166: aload_2
    //   167: invokevirtual pushPromise : (IILjava/util/List;)V
    //   170: aload #7
    //   172: monitorexit
    //   173: iload #4
    //   175: ifeq -> 185
    //   178: aload_0
    //   179: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   182: invokevirtual flush : ()V
    //   185: aload #8
    //   187: areturn
    //   188: new java/lang/IllegalArgumentException
    //   191: astore_2
    //   192: aload_2
    //   193: ldc_w 'client streams shouldn't have associated stream IDs'
    //   196: invokespecial <init> : (Ljava/lang/String;)V
    //   199: aload_2
    //   200: athrow
    //   201: new okhttp3/internal/http2/ConnectionShutdownException
    //   204: astore_2
    //   205: aload_2
    //   206: invokespecial <init> : ()V
    //   209: aload_2
    //   210: athrow
    //   211: astore_2
    //   212: aload_0
    //   213: monitorexit
    //   214: aload_2
    //   215: athrow
    //   216: astore_2
    //   217: aload #7
    //   219: monitorexit
    //   220: aload_2
    //   221: athrow
    // Exception table:
    //   from	to	target	type
    //   14	16	216	finally
    //   16	33	211	finally
    //   33	71	211	finally
    //   75	94	211	finally
    //   106	131	211	finally
    //   131	133	211	finally
    //   137	149	216	finally
    //   152	170	216	finally
    //   170	173	216	finally
    //   188	201	216	finally
    //   201	211	211	finally
    //   212	214	211	finally
    //   214	216	216	finally
    //   217	220	216	finally
  }
  
  private void pushExecutorExecute(NamedRunnable paramNamedRunnable) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield shutdown : Z
    //   6: ifne -> 19
    //   9: aload_0
    //   10: getfield pushExecutor : Ljava/util/concurrent/ExecutorService;
    //   13: aload_1
    //   14: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public void awaitPong() throws InterruptedException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield awaitPongsReceived : J
    //   6: aload_0
    //   7: getfield awaitPingsSent : J
    //   10: lcmp
    //   11: ifge -> 21
    //   14: aload_0
    //   15: invokevirtual wait : ()V
    //   18: goto -> 2
    //   21: aload_0
    //   22: monitorexit
    //   23: return
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	24	finally
  }
  
  public void close() {
    close(ErrorCode.NO_ERROR, ErrorCode.CANCEL, null);
  }
  
  public void close(ErrorCode paramErrorCode1, ErrorCode paramErrorCode2, @Nullable IOException paramIOException) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual shutdown : (Lokhttp3/internal/http2/ErrorCode;)V
    //   5: aconst_null
    //   6: astore_1
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield streams : Ljava/util/Map;
    //   13: invokeinterface isEmpty : ()Z
    //   18: ifne -> 60
    //   21: aload_0
    //   22: getfield streams : Ljava/util/Map;
    //   25: invokeinterface values : ()Ljava/util/Collection;
    //   30: aload_0
    //   31: getfield streams : Ljava/util/Map;
    //   34: invokeinterface size : ()I
    //   39: anewarray okhttp3/internal/http2/Http2Stream
    //   42: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   47: checkcast [Lokhttp3/internal/http2/Http2Stream;
    //   50: astore_1
    //   51: aload_0
    //   52: getfield streams : Ljava/util/Map;
    //   55: invokeinterface clear : ()V
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: ifnull -> 99
    //   66: aload_1
    //   67: arraylength
    //   68: istore #5
    //   70: iconst_0
    //   71: istore #4
    //   73: iload #4
    //   75: iload #5
    //   77: if_icmpge -> 99
    //   80: aload_1
    //   81: iload #4
    //   83: aaload
    //   84: astore #6
    //   86: aload #6
    //   88: aload_2
    //   89: aload_3
    //   90: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Ljava/io/IOException;)V
    //   93: iinc #4, 1
    //   96: goto -> 73
    //   99: aload_0
    //   100: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   103: invokevirtual close : ()V
    //   106: aload_0
    //   107: getfield socket : Ljava/net/Socket;
    //   110: invokevirtual close : ()V
    //   113: aload_0
    //   114: getfield writerExecutor : Ljava/util/concurrent/ScheduledExecutorService;
    //   117: invokeinterface shutdown : ()V
    //   122: aload_0
    //   123: getfield pushExecutor : Ljava/util/concurrent/ExecutorService;
    //   126: invokeinterface shutdown : ()V
    //   131: return
    //   132: astore_1
    //   133: aload_0
    //   134: monitorexit
    //   135: aload_1
    //   136: athrow
    //   137: astore_1
    //   138: goto -> 5
    //   141: astore #6
    //   143: goto -> 93
    //   146: astore_1
    //   147: goto -> 106
    //   150: astore_1
    //   151: goto -> 113
    // Exception table:
    //   from	to	target	type
    //   0	5	137	java/io/IOException
    //   9	60	132	finally
    //   60	62	132	finally
    //   86	93	141	java/io/IOException
    //   99	106	146	java/io/IOException
    //   106	113	150	java/io/IOException
    //   133	135	132	finally
  }
  
  public void flush() throws IOException {
    this.writer.flush();
  }
  
  public Http2Stream getStream(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield streams : Ljava/util/Map;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast okhttp3/internal/http2/Http2Stream
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: areturn
    //   23: astore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_2
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	23	finally
  }
  
  public boolean isHealthy(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield shutdown : Z
    //   6: istore #5
    //   8: iload #5
    //   10: ifeq -> 17
    //   13: aload_0
    //   14: monitorexit
    //   15: iconst_0
    //   16: ireturn
    //   17: aload_0
    //   18: getfield degradedPongsReceived : J
    //   21: aload_0
    //   22: getfield degradedPingsSent : J
    //   25: lcmp
    //   26: ifge -> 44
    //   29: aload_0
    //   30: getfield degradedPongDeadlineNs : J
    //   33: lstore_3
    //   34: lload_1
    //   35: lload_3
    //   36: lcmp
    //   37: iflt -> 44
    //   40: aload_0
    //   41: monitorexit
    //   42: iconst_0
    //   43: ireturn
    //   44: aload_0
    //   45: monitorexit
    //   46: iconst_1
    //   47: ireturn
    //   48: astore #6
    //   50: aload_0
    //   51: monitorexit
    //   52: aload #6
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	48	finally
    //   17	34	48	finally
  }
  
  public int maxConcurrentStreams() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield peerSettings : Lokhttp3/internal/http2/Settings;
    //   6: ldc 2147483647
    //   8: invokevirtual getMaxConcurrentStreams : (I)I
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
  
  public Http2Stream newStream(List<Header> paramList, boolean paramBoolean) throws IOException {
    return newStream(0, paramList, paramBoolean);
  }
  
  public int openStreamCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield streams : Ljava/util/Map;
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
  
  public void pushDataLater(final int streamId, l丨Li1LL paraml丨Li1LL, final int byteCount, final boolean inFinished) throws IOException {
    final I1I buffer = new I1I();
    long l = byteCount;
    paraml丨Li1LL.llliI(l);
    paraml丨Li1LL.read(i1I, l);
    if (i1I.iI1i丨I() == l) {
      pushExecutorExecute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[] { this.connectionName, Integer.valueOf(streamId) }) {
            public final Http2Connection this$0;
            
            public final I1I val$buffer;
            
            public final int val$byteCount;
            
            public final boolean val$inFinished;
            
            public final int val$streamId;
            
            public void execute() {
              try {
                boolean bool = Http2Connection.this.pushObserver.onData(streamId, buffer, byteCount, inFinished);
                if (bool)
                  Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL); 
                if (bool || inFinished)
                  synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
                  }  
              } catch (IOException iOException) {}
            }
          });
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(i1I.iI1i丨I());
    stringBuilder.append(" != ");
    stringBuilder.append(byteCount);
    throw new IOException(stringBuilder.toString());
  }
  
  public void pushHeadersLater(int paramInt, List<Header> paramList, boolean paramBoolean) {
    try {
      NamedRunnable namedRunnable = new NamedRunnable() {
          public final Http2Connection this$0;
          
          public final boolean val$inFinished;
          
          public final List val$requestHeaders;
          
          public final int val$streamId;
          
          public void execute() {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   4: getfield pushObserver : Lokhttp3/internal/http2/PushObserver;
            //   7: aload_0
            //   8: getfield val$streamId : I
            //   11: aload_0
            //   12: getfield val$requestHeaders : Ljava/util/List;
            //   15: aload_0
            //   16: getfield val$inFinished : Z
            //   19: invokeinterface onHeaders : (ILjava/util/List;Z)Z
            //   24: istore_1
            //   25: iload_1
            //   26: ifeq -> 46
            //   29: aload_0
            //   30: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   33: getfield writer : Lokhttp3/internal/http2/Http2Writer;
            //   36: aload_0
            //   37: getfield val$streamId : I
            //   40: getstatic okhttp3/internal/http2/ErrorCode.CANCEL : Lokhttp3/internal/http2/ErrorCode;
            //   43: invokevirtual rstStream : (ILokhttp3/internal/http2/ErrorCode;)V
            //   46: iload_1
            //   47: ifne -> 57
            //   50: aload_0
            //   51: getfield val$inFinished : Z
            //   54: ifeq -> 94
            //   57: aload_0
            //   58: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   61: astore_2
            //   62: aload_2
            //   63: monitorenter
            //   64: aload_0
            //   65: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   68: getfield currentPushRequests : Ljava/util/Set;
            //   71: aload_0
            //   72: getfield val$streamId : I
            //   75: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   78: invokeinterface remove : (Ljava/lang/Object;)Z
            //   83: pop
            //   84: aload_2
            //   85: monitorexit
            //   86: goto -> 94
            //   89: astore_3
            //   90: aload_2
            //   91: monitorexit
            //   92: aload_3
            //   93: athrow
            //   94: return
            //   95: astore_2
            //   96: goto -> 94
            // Exception table:
            //   from	to	target	type
            //   29	46	95	java/io/IOException
            //   50	57	95	java/io/IOException
            //   57	64	95	java/io/IOException
            //   64	86	89	finally
            //   90	92	89	finally
            //   92	94	95	java/io/IOException
          }
        };
      super(this, "OkHttp %s Push Headers[%s]", new Object[] { this.connectionName, Integer.valueOf(paramInt) }, paramInt, paramList, paramBoolean);
      pushExecutorExecute(namedRunnable);
    } catch (RejectedExecutionException rejectedExecutionException) {}
  }
  
  public void pushRequestLater(int paramInt, List<Header> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentPushRequests : Ljava/util/Set;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface contains : (Ljava/lang/Object;)Z
    //   15: ifeq -> 29
    //   18: aload_0
    //   19: iload_1
    //   20: getstatic okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR : Lokhttp3/internal/http2/ErrorCode;
    //   23: invokevirtual writeSynResetLater : (ILokhttp3/internal/http2/ErrorCode;)V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_0
    //   30: getfield currentPushRequests : Ljava/util/Set;
    //   33: iload_1
    //   34: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   37: invokeinterface add : (Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_0
    //   44: monitorexit
    //   45: new okhttp3/internal/http2/Http2Connection$4
    //   48: astore_3
    //   49: aload_3
    //   50: aload_0
    //   51: ldc_w 'OkHttp %s Push Request[%s]'
    //   54: iconst_2
    //   55: anewarray java/lang/Object
    //   58: dup
    //   59: iconst_0
    //   60: aload_0
    //   61: getfield connectionName : Ljava/lang/String;
    //   64: aastore
    //   65: dup
    //   66: iconst_1
    //   67: iload_1
    //   68: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   71: aastore
    //   72: iload_1
    //   73: aload_2
    //   74: invokespecial <init> : (Lokhttp3/internal/http2/Http2Connection;Ljava/lang/String;[Ljava/lang/Object;ILjava/util/List;)V
    //   77: aload_0
    //   78: aload_3
    //   79: invokespecial pushExecutorExecute : (Lokhttp3/internal/NamedRunnable;)V
    //   82: return
    //   83: astore_2
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_2
    //   87: athrow
    //   88: astore_2
    //   89: goto -> 82
    // Exception table:
    //   from	to	target	type
    //   2	28	83	finally
    //   29	45	83	finally
    //   45	82	88	java/util/concurrent/RejectedExecutionException
    //   84	86	83	finally
  }
  
  public void pushResetLater(final int streamId, final ErrorCode errorCode) {
    pushExecutorExecute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[] { this.connectionName, Integer.valueOf(streamId) }) {
          public final Http2Connection this$0;
          
          public final ErrorCode val$errorCode;
          
          public final int val$streamId;
          
          public void execute() {
            Http2Connection.this.pushObserver.onReset(streamId, errorCode);
            synchronized (Http2Connection.this) {
              Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
              return;
            } 
          }
        });
  }
  
  public Http2Stream pushStream(int paramInt, List<Header> paramList, boolean paramBoolean) throws IOException {
    if (!this.client)
      return newStream(paramInt, paramList, paramBoolean); 
    throw new IllegalStateException("Client cannot push requests.");
  }
  
  public boolean pushedStream(int paramInt) {
    boolean bool = true;
    if (paramInt == 0 || (paramInt & 0x1) != 0)
      bool = false; 
    return bool;
  }
  
  public Http2Stream removeStream(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield streams : Ljava/util/Map;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast okhttp3/internal/http2/Http2Stream
    //   18: astore_2
    //   19: aload_0
    //   20: invokevirtual notifyAll : ()V
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_2
    //   26: areturn
    //   27: astore_2
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_2
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	27	finally
  }
  
  public void sendDegradedPingLater() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield degradedPongsReceived : J
    //   6: lstore_1
    //   7: aload_0
    //   8: getfield degradedPingsSent : J
    //   11: lstore_3
    //   12: lload_1
    //   13: lload_3
    //   14: lcmp
    //   15: ifge -> 21
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: aload_0
    //   22: lload_3
    //   23: lconst_1
    //   24: ladd
    //   25: putfield degradedPingsSent : J
    //   28: aload_0
    //   29: invokestatic nanoTime : ()J
    //   32: ldc2_w 1000000000
    //   35: ladd
    //   36: putfield degradedPongDeadlineNs : J
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_0
    //   42: getfield writerExecutor : Ljava/util/concurrent/ScheduledExecutorService;
    //   45: astore #6
    //   47: new okhttp3/internal/http2/Http2Connection$3
    //   50: astore #5
    //   52: aload #5
    //   54: aload_0
    //   55: ldc_w 'OkHttp %s ping'
    //   58: iconst_1
    //   59: anewarray java/lang/Object
    //   62: dup
    //   63: iconst_0
    //   64: aload_0
    //   65: getfield connectionName : Ljava/lang/String;
    //   68: aastore
    //   69: invokespecial <init> : (Lokhttp3/internal/http2/Http2Connection;Ljava/lang/String;[Ljava/lang/Object;)V
    //   72: aload #6
    //   74: aload #5
    //   76: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   81: return
    //   82: astore #5
    //   84: aload_0
    //   85: monitorexit
    //   86: aload #5
    //   88: athrow
    //   89: astore #5
    //   91: goto -> 81
    // Exception table:
    //   from	to	target	type
    //   2	12	82	finally
    //   18	20	82	finally
    //   21	41	82	finally
    //   41	81	89	java/util/concurrent/RejectedExecutionException
    //   84	86	82	finally
  }
  
  public void setSettings(Settings paramSettings) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield shutdown : Z
    //   13: ifne -> 37
    //   16: aload_0
    //   17: getfield okHttpSettings : Lokhttp3/internal/http2/Settings;
    //   20: aload_1
    //   21: invokevirtual merge : (Lokhttp3/internal/http2/Settings;)V
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_0
    //   27: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   30: aload_1
    //   31: invokevirtual settings : (Lokhttp3/internal/http2/Settings;)V
    //   34: aload_2
    //   35: monitorexit
    //   36: return
    //   37: new okhttp3/internal/http2/ConnectionShutdownException
    //   40: astore_1
    //   41: aload_1
    //   42: invokespecial <init> : ()V
    //   45: aload_1
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    //   52: astore_1
    //   53: aload_2
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   7	9	52	finally
    //   9	26	47	finally
    //   26	36	52	finally
    //   37	47	47	finally
    //   48	50	47	finally
    //   50	52	52	finally
    //   53	55	52	finally
  }
  
  public void shutdown(ErrorCode paramErrorCode) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield shutdown : Z
    //   13: ifeq -> 21
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_3
    //   19: monitorexit
    //   20: return
    //   21: aload_0
    //   22: iconst_1
    //   23: putfield shutdown : Z
    //   26: aload_0
    //   27: getfield lastGoodStreamId : I
    //   30: istore_2
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_0
    //   34: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   37: iload_2
    //   38: aload_1
    //   39: getstatic okhttp3/internal/Util.EMPTY_BYTE_ARRAY : [B
    //   42: invokevirtual goAway : (ILokhttp3/internal/http2/ErrorCode;[B)V
    //   45: aload_3
    //   46: monitorexit
    //   47: return
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_1
    //   52: athrow
    //   53: astore_1
    //   54: aload_3
    //   55: monitorexit
    //   56: aload_1
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   7	9	53	finally
    //   9	18	48	finally
    //   18	20	53	finally
    //   21	33	48	finally
    //   33	47	53	finally
    //   49	51	48	finally
    //   51	53	53	finally
    //   54	56	53	finally
  }
  
  public void start() throws IOException {
    start(true);
  }
  
  public void start(boolean paramBoolean) throws IOException {
    if (paramBoolean) {
      this.writer.connectionPreface();
      this.writer.settings(this.okHttpSettings);
      int i = this.okHttpSettings.getInitialWindowSize();
      if (i != 65535)
        this.writer.windowUpdate(0, (i - 65535)); 
    } 
    (new Thread(this.readerRunnable)).start();
  }
  
  public void updateConnectionFlowControl(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield unacknowledgedBytesRead : J
    //   6: lload_1
    //   7: ladd
    //   8: lstore_1
    //   9: aload_0
    //   10: lload_1
    //   11: putfield unacknowledgedBytesRead : J
    //   14: lload_1
    //   15: aload_0
    //   16: getfield okHttpSettings : Lokhttp3/internal/http2/Settings;
    //   19: invokevirtual getInitialWindowSize : ()I
    //   22: iconst_2
    //   23: idiv
    //   24: i2l
    //   25: lcmp
    //   26: iflt -> 43
    //   29: aload_0
    //   30: iconst_0
    //   31: aload_0
    //   32: getfield unacknowledgedBytesRead : J
    //   35: invokevirtual writeWindowUpdateLater : (IJ)V
    //   38: aload_0
    //   39: lconst_0
    //   40: putfield unacknowledgedBytesRead : J
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: astore_3
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_3
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	43	46	finally
  }
  
  public void writeData(int paramInt, boolean paramBoolean, I1I paramI1I, long paramLong) throws IOException {
    long l = paramLong;
    if (paramLong == 0L) {
      this.writer.data(paramBoolean, paramInt, paramI1I, 0);
      return;
    } 
    /* monitor enter ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
    while (l > 0L) {
      try {
        IOException iOException;
        int i;
        boolean bool;
        Http2Writer http2Writer;
        while (true) {
          paramLong = this.bytesLeftInWriteWindow;
          if (paramLong <= 0L) {
            if (this.streams.containsKey(Integer.valueOf(paramInt))) {
              wait();
              continue;
            } 
            iOException = new IOException();
            this("stream closed");
            throw iOException;
          } 
          i = Math.min((int)Math.min(l, paramLong), this.writer.maxDataLength());
          long l1 = this.bytesLeftInWriteWindow;
          paramLong = i;
          this.bytesLeftInWriteWindow = l1 - paramLong;
          /* monitor exit ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
          l -= paramLong;
          http2Writer = this.writer;
          if (paramBoolean) {
            if (l == 0L) {
              boolean bool1 = true;
              break;
            } 
            bool = false;
          } else {
            continue;
          } 
          http2Writer.data(bool, paramInt, (I1I)iOException, i);
        } 
        http2Writer.data(bool, paramInt, (I1I)iOException, i);
        continue;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        InterruptedIOException interruptedIOException = new InterruptedIOException();
        this();
        throw interruptedIOException;
      } finally {}
      /* monitor exit ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
      throw paramI1I;
    } 
  }
  
  public void writeHeaders(int paramInt, boolean paramBoolean, List<Header> paramList) throws IOException {
    this.writer.headers(paramBoolean, paramInt, paramList);
  }
  
  public void writePing() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield awaitPingsSent : J
    //   7: lconst_1
    //   8: ladd
    //   9: putfield awaitPingsSent : J
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_0
    //   15: iconst_0
    //   16: iconst_3
    //   17: ldc_w 1330343787
    //   20: invokevirtual writePing : (ZII)V
    //   23: return
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	24	finally
    //   25	27	24	finally
  }
  
  public void writePing(boolean paramBoolean, int paramInt1, int paramInt2) {
    try {
      this.writer.ping(paramBoolean, paramInt1, paramInt2);
    } catch (IOException iOException) {
      failConnection(iOException);
    } 
  }
  
  public void writePingAndAwaitPong() throws InterruptedException {
    writePing();
    awaitPong();
  }
  
  public void writeSynReset(int paramInt, ErrorCode paramErrorCode) throws IOException {
    this.writer.rstStream(paramInt, paramErrorCode);
  }
  
  public void writeSynResetLater(int paramInt, ErrorCode paramErrorCode) {
    try {
      ScheduledExecutorService scheduledExecutorService = this.writerExecutor;
      NamedRunnable namedRunnable = new NamedRunnable() {
          public final Http2Connection this$0;
          
          public final ErrorCode val$errorCode;
          
          public final int val$streamId;
          
          public void execute() {
            try {
              Http2Connection.this.writeSynReset(streamId, errorCode);
            } catch (IOException iOException) {
              Http2Connection.this.failConnection(iOException);
            } 
          }
        };
      super(this, "OkHttp %s stream %d", new Object[] { this.connectionName, Integer.valueOf(paramInt) }, paramInt, paramErrorCode);
      scheduledExecutorService.execute(namedRunnable);
    } catch (RejectedExecutionException rejectedExecutionException) {}
  }
  
  public void writeWindowUpdateLater(int paramInt, long paramLong) {
    try {
      ScheduledExecutorService scheduledExecutorService = this.writerExecutor;
      NamedRunnable namedRunnable = new NamedRunnable() {
          public final Http2Connection this$0;
          
          public final int val$streamId;
          
          public final long val$unacknowledgedBytesRead;
          
          public void execute() {
            try {
              Http2Connection.this.writer.windowUpdate(streamId, unacknowledgedBytesRead);
            } catch (IOException iOException) {
              Http2Connection.this.failConnection(iOException);
            } 
          }
        };
      super(this, "OkHttp Window Update %s stream %d", new Object[] { this.connectionName, Integer.valueOf(paramInt) }, paramInt, paramLong);
      scheduledExecutorService.execute(namedRunnable);
    } catch (RejectedExecutionException rejectedExecutionException) {}
  }
  
  public static class Builder {
    public boolean client;
    
    public String connectionName;
    
    public Http2Connection.Listener listener = Http2Connection.Listener.REFUSE_INCOMING_STREAMS;
    
    public int pingIntervalMillis;
    
    public PushObserver pushObserver = PushObserver.CANCEL;
    
    public I丨L sink;
    
    public Socket socket;
    
    public l丨Li1LL source;
    
    public Builder(boolean param1Boolean) {
      this.client = param1Boolean;
    }
    
    public Http2Connection build() {
      return new Http2Connection(this);
    }
    
    public Builder listener(Http2Connection.Listener param1Listener) {
      this.listener = param1Listener;
      return this;
    }
    
    public Builder pingIntervalMillis(int param1Int) {
      this.pingIntervalMillis = param1Int;
      return this;
    }
    
    public Builder pushObserver(PushObserver param1PushObserver) {
      this.pushObserver = param1PushObserver;
      return this;
    }
    
    public Builder socket(Socket param1Socket) throws IOException {
      String str;
      SocketAddress socketAddress = param1Socket.getRemoteSocketAddress();
      if (socketAddress instanceof InetSocketAddress) {
        str = ((InetSocketAddress)socketAddress).getHostName();
      } else {
        str = str.toString();
      } 
      return socket(param1Socket, str, lIi丨I.I丨L(lIi丨I.lIi丨I(param1Socket)), lIi丨I.I1I(lIi丨I.L丨1丨1丨I(param1Socket)));
    }
    
    public Builder socket(Socket param1Socket, String param1String, l丨Li1LL param1l丨Li1LL, I丨L param1I丨L) {
      this.socket = param1Socket;
      this.connectionName = param1String;
      this.source = param1l丨Li1LL;
      this.sink = param1I丨L;
      return this;
    }
  }
  
  public final class IntervalPingRunnable extends NamedRunnable {
    public final Http2Connection this$0;
    
    public IntervalPingRunnable() {
      super("OkHttp %s ping", new Object[] { this$0.connectionName });
    }
    
    public void execute() {
      synchronized (Http2Connection.this) {
        boolean bool;
        if (Http2Connection.this.intervalPongsReceived < Http2Connection.this.intervalPingsSent) {
          bool = true;
        } else {
          Http2Connection.access$208(Http2Connection.this);
          bool = false;
        } 
        if (bool) {
          Http2Connection.this.failConnection(null);
        } else {
          Http2Connection.this.writePing(false, 1, 0);
        } 
        return;
      } 
    }
  }
  
  public static abstract class Listener {
    public static final Listener REFUSE_INCOMING_STREAMS = new Listener() {
        public void onStream(Http2Stream param2Http2Stream) throws IOException {
          param2Http2Stream.close(ErrorCode.REFUSED_STREAM, null);
        }
      };
    
    public void onSettings(Http2Connection param1Http2Connection) {}
    
    public abstract void onStream(Http2Stream param1Http2Stream) throws IOException;
  }
  
  public class null extends Listener {
    public void onStream(Http2Stream param1Http2Stream) throws IOException {
      param1Http2Stream.close(ErrorCode.REFUSED_STREAM, null);
    }
  }
  
  public final class PingRunnable extends NamedRunnable {
    public final int payload1;
    
    public final int payload2;
    
    public final boolean reply;
    
    public final Http2Connection this$0;
    
    public PingRunnable(boolean param1Boolean, int param1Int1, int param1Int2) {
      super("OkHttp %s ping %08x%08x", new Object[] { this$0.connectionName, Integer.valueOf(param1Int1), Integer.valueOf(param1Int2) });
      this.reply = param1Boolean;
      this.payload1 = param1Int1;
      this.payload2 = param1Int2;
    }
    
    public void execute() {
      Http2Connection.this.writePing(this.reply, this.payload1, this.payload2);
    }
  }
  
  public class ReaderRunnable extends NamedRunnable implements Http2Reader.Handler {
    public final Http2Reader reader;
    
    public final Http2Connection this$0;
    
    public ReaderRunnable(Http2Reader param1Http2Reader) {
      super("OkHttp %s", new Object[] { this$0.connectionName });
      this.reader = param1Http2Reader;
    }
    
    public void ackSettings() {}
    
    public void alternateService(int param1Int1, String param1String1, iI丨LLL1 param1iI丨LLL1, String param1String2, int param1Int2, long param1Long) {}
    
    public void applyAndAckSettings(boolean param1Boolean, Settings param1Settings) {
      synchronized (Http2Connection.this.writer) {
        Http2Connection http2Connection;
        synchronized (Http2Connection.this) {
          Http2Stream[] arrayOfHttp2Stream;
          long l;
          int j = Http2Connection.this.peerSettings.getInitialWindowSize();
          if (param1Boolean)
            Http2Connection.this.peerSettings.clear(); 
          Http2Connection.this.peerSettings.merge(param1Settings);
          int i = Http2Connection.this.peerSettings.getInitialWindowSize();
          param1Settings = null;
          if (i != -1 && i != j) {
            long l1 = (i - j);
            l = l1;
            if (!Http2Connection.this.streams.isEmpty()) {
              arrayOfHttp2Stream = (Http2Stream[])Http2Connection.this.streams.values().toArray((Object[])new Http2Stream[Http2Connection.this.streams.size()]);
              l = l1;
            } 
          } else {
            l = 0L;
          } 
          try {
            http2Connection = Http2Connection.this;
            http2Connection.writer.applyAndAckSettings(http2Connection.peerSettings);
          } catch (IOException iOException) {
            Http2Connection.this.failConnection(iOException);
          } 
          if (arrayOfHttp2Stream != null) {
            j = arrayOfHttp2Stream.length;
            i = 0;
            while (i < j) {
              synchronized (arrayOfHttp2Stream[i]) {
                null.addBytesToWriteWindow(l);
                i++;
              } 
            } 
          } 
          Http2Connection.listenerExecutor.execute(new NamedRunnable("OkHttp %s settings", new Object[] { this.this$0.connectionName }) {
                public final Http2Connection.ReaderRunnable this$1;
                
                public void execute() {
                  Http2Connection http2Connection = Http2Connection.this;
                  http2Connection.listener.onSettings(http2Connection);
                }
              });
          return;
        } 
      } 
    }
    
    public void data(boolean param1Boolean, int param1Int1, l丨Li1LL param1l丨Li1LL, int param1Int2) throws IOException {
      Http2Connection http2Connection;
      if (Http2Connection.this.pushedStream(param1Int1)) {
        Http2Connection.this.pushDataLater(param1Int1, param1l丨Li1LL, param1Int2, param1Boolean);
        return;
      } 
      Http2Stream http2Stream = Http2Connection.this.getStream(param1Int1);
      if (http2Stream == null) {
        Http2Connection.this.writeSynResetLater(param1Int1, ErrorCode.PROTOCOL_ERROR);
        http2Connection = Http2Connection.this;
        long l = param1Int2;
        http2Connection.updateConnectionFlowControl(l);
        param1l丨Li1LL.skip(l);
        return;
      } 
      http2Connection.receiveData(param1l丨Li1LL, param1Int2);
      if (param1Boolean)
        http2Connection.receiveHeaders(Util.EMPTY_HEADERS, true); 
    }
    
    public void execute() {
      // Byte code:
      //   0: getstatic okhttp3/internal/http2/ErrorCode.INTERNAL_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   3: astore #4
      //   5: aconst_null
      //   6: astore_2
      //   7: aconst_null
      //   8: astore_1
      //   9: aload_0
      //   10: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   13: aload_0
      //   14: invokevirtual readConnectionPreface : (Lokhttp3/internal/http2/Http2Reader$Handler;)V
      //   17: aload_0
      //   18: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   21: iconst_0
      //   22: aload_0
      //   23: invokevirtual nextFrame : (ZLokhttp3/internal/http2/Http2Reader$Handler;)Z
      //   26: ifeq -> 32
      //   29: goto -> 17
      //   32: getstatic okhttp3/internal/http2/ErrorCode.NO_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   35: astore #5
      //   37: aload #5
      //   39: astore_2
      //   40: getstatic okhttp3/internal/http2/ErrorCode.CANCEL : Lokhttp3/internal/http2/ErrorCode;
      //   43: astore_3
      //   44: aload_0
      //   45: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   48: aload #5
      //   50: aload_3
      //   51: aconst_null
      //   52: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;Ljava/io/IOException;)V
      //   55: goto -> 98
      //   58: astore_3
      //   59: aload #5
      //   61: astore_2
      //   62: goto -> 79
      //   65: astore_1
      //   66: aload #4
      //   68: astore_3
      //   69: aload_1
      //   70: astore #5
      //   72: goto -> 112
      //   75: astore_3
      //   76: aload #4
      //   78: astore_2
      //   79: aload_3
      //   80: astore_1
      //   81: getstatic okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   84: astore #5
      //   86: aload_0
      //   87: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   90: aload #5
      //   92: aload #5
      //   94: aload_3
      //   95: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;Ljava/io/IOException;)V
      //   98: aload_0
      //   99: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   102: invokestatic closeQuietly : (Ljava/io/Closeable;)V
      //   105: return
      //   106: astore #5
      //   108: aload_2
      //   109: astore_3
      //   110: aload_1
      //   111: astore_2
      //   112: aload_0
      //   113: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   116: aload_3
      //   117: aload #4
      //   119: aload_2
      //   120: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;Ljava/io/IOException;)V
      //   123: aload_0
      //   124: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   127: invokestatic closeQuietly : (Ljava/io/Closeable;)V
      //   130: aload #5
      //   132: athrow
      // Exception table:
      //   from	to	target	type
      //   9	17	75	java/io/IOException
      //   9	17	65	finally
      //   17	29	75	java/io/IOException
      //   17	29	65	finally
      //   32	37	75	java/io/IOException
      //   32	37	65	finally
      //   40	44	58	java/io/IOException
      //   40	44	106	finally
      //   81	86	106	finally
    }
    
    public void goAway(int param1Int, ErrorCode param1ErrorCode, iI丨LLL1 param1iI丨LLL1) {
      Http2Connection http2Connection;
      Http2Stream http2Stream;
      param1iI丨LLL1.size();
      synchronized (Http2Connection.this) {
        Http2Stream[] arrayOfHttp2Stream = (Http2Stream[])Http2Connection.this.streams.values().toArray((Object[])new Http2Stream[Http2Connection.this.streams.size()]);
        Http2Connection.access$302(Http2Connection.this, true);
        int i = arrayOfHttp2Stream.length;
        for (byte b = 0; b < i; b++) {
          http2Stream = arrayOfHttp2Stream[b];
          if (http2Stream.getId() > param1Int && http2Stream.isLocallyInitiated()) {
            http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
            Http2Connection.this.removeStream(http2Stream.getId());
          } 
        } 
        return;
      } 
    }
    
    public void headers(boolean param1Boolean, int param1Int1, int param1Int2, List<Header> param1List) {
      if (Http2Connection.this.pushedStream(param1Int1)) {
        Http2Connection.this.pushHeadersLater(param1Int1, param1List, param1Boolean);
        return;
      } 
      synchronized (Http2Connection.this) {
        Http2Stream http2Stream1;
        NamedRunnable namedRunnable;
        Http2Stream http2Stream2 = Http2Connection.this.getStream(param1Int1);
        if (http2Stream2 == null) {
          if (Http2Connection.this.shutdown)
            return; 
          Http2Connection http2Connection2 = Http2Connection.this;
          if (param1Int1 <= http2Connection2.lastGoodStreamId)
            return; 
          if (param1Int1 % 2 == http2Connection2.nextStreamId % 2)
            return; 
          Headers headers = Util.toHeaders(param1List);
          http2Stream1 = new Http2Stream();
          this(param1Int1, Http2Connection.this, false, param1Boolean, headers);
          Http2Connection http2Connection1 = Http2Connection.this;
          http2Connection1.lastGoodStreamId = param1Int1;
          http2Connection1.streams.put(Integer.valueOf(param1Int1), http2Stream1);
          ExecutorService executorService = Http2Connection.listenerExecutor;
          namedRunnable = new NamedRunnable() {
              public final Http2Connection.ReaderRunnable this$1;
              
              public final Http2Stream val$newStream;
              
              public void execute() {
                try {
                  Http2Connection.this.listener.onStream(newStream);
                } catch (IOException iOException) {
                  Platform platform = Platform.get();
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("Http2Connection.Listener failure for ");
                  stringBuilder.append(Http2Connection.this.connectionName);
                  platform.log(4, stringBuilder.toString(), iOException);
                  try {
                    newStream.close(ErrorCode.PROTOCOL_ERROR, iOException);
                  } catch (IOException iOException1) {}
                } 
              }
            };
          super(this, "OkHttp %s stream %d", new Object[] { this.this$0.connectionName, Integer.valueOf(param1Int1) }, http2Stream1);
          executorService.execute(namedRunnable);
          return;
        } 
        namedRunnable.receiveHeaders(Util.toHeaders((List<Header>)http2Stream1), param1Boolean);
        return;
      } 
    }
    
    public void ping(boolean param1Boolean, int param1Int1, int param1Int2) {
      if (param1Boolean) {
        Http2Connection http2Connection = Http2Connection.this;
        /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/internal/http2/Http2Connection}, name=null} */
        if (param1Int1 == 1) {
          try {
            Http2Connection.access$108(Http2Connection.this);
          } finally {
            Exception exception;
          } 
        } else if (param1Int1 == 2) {
          Http2Connection.access$608(Http2Connection.this);
        } else if (param1Int1 == 3) {
          Http2Connection.access$708(Http2Connection.this);
          Http2Connection.this.notifyAll();
        } 
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/internal/http2/Http2Connection}, name=null} */
      } else {
        try {
          ScheduledExecutorService scheduledExecutorService = Http2Connection.this.writerExecutor;
          Http2Connection.PingRunnable pingRunnable = new Http2Connection.PingRunnable();
          this(true, param1Int1, param1Int2);
          scheduledExecutorService.execute(pingRunnable);
        } catch (RejectedExecutionException rejectedExecutionException) {}
      } 
    }
    
    public void priority(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {}
    
    public void pushPromise(int param1Int1, int param1Int2, List<Header> param1List) {
      Http2Connection.this.pushRequestLater(param1Int2, param1List);
    }
    
    public void rstStream(int param1Int, ErrorCode param1ErrorCode) {
      if (Http2Connection.this.pushedStream(param1Int)) {
        Http2Connection.this.pushResetLater(param1Int, param1ErrorCode);
        return;
      } 
      Http2Stream http2Stream = Http2Connection.this.removeStream(param1Int);
      if (http2Stream != null)
        http2Stream.receiveRstStream(param1ErrorCode); 
    }
    
    public void settings(boolean param1Boolean, Settings param1Settings) {
      try {
        ScheduledExecutorService scheduledExecutorService = Http2Connection.this.writerExecutor;
        NamedRunnable namedRunnable = new NamedRunnable() {
            public final Http2Connection.ReaderRunnable this$1;
            
            public final boolean val$clearPrevious;
            
            public final Settings val$settings;
            
            public void execute() {
              Http2Connection.ReaderRunnable.this.applyAndAckSettings(clearPrevious, settings);
            }
          };
        super(this, "OkHttp %s ACK Settings", new Object[] { this.this$0.connectionName }, param1Boolean, param1Settings);
        scheduledExecutorService.execute(namedRunnable);
      } catch (RejectedExecutionException rejectedExecutionException) {}
    }
    
    public void windowUpdate(int param1Int, long param1Long) {
      // Byte code:
      //   0: iload_1
      //   1: ifne -> 50
      //   4: aload_0
      //   5: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   8: astore #4
      //   10: aload #4
      //   12: monitorenter
      //   13: aload_0
      //   14: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   17: astore #5
      //   19: aload #5
      //   21: aload #5
      //   23: getfield bytesLeftInWriteWindow : J
      //   26: lload_2
      //   27: ladd
      //   28: putfield bytesLeftInWriteWindow : J
      //   31: aload #5
      //   33: invokevirtual notifyAll : ()V
      //   36: aload #4
      //   38: monitorexit
      //   39: goto -> 88
      //   42: astore #5
      //   44: aload #4
      //   46: monitorexit
      //   47: aload #5
      //   49: athrow
      //   50: aload_0
      //   51: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   54: iload_1
      //   55: invokevirtual getStream : (I)Lokhttp3/internal/http2/Http2Stream;
      //   58: astore #4
      //   60: aload #4
      //   62: ifnull -> 88
      //   65: aload #4
      //   67: monitorenter
      //   68: aload #4
      //   70: lload_2
      //   71: invokevirtual addBytesToWriteWindow : (J)V
      //   74: aload #4
      //   76: monitorexit
      //   77: goto -> 88
      //   80: astore #5
      //   82: aload #4
      //   84: monitorexit
      //   85: aload #5
      //   87: athrow
      //   88: return
      // Exception table:
      //   from	to	target	type
      //   13	39	42	finally
      //   44	47	42	finally
      //   68	77	80	finally
      //   82	85	80	finally
    }
  }
  
  public class null extends NamedRunnable {
    public final Http2Connection.ReaderRunnable this$1;
    
    public final Http2Stream val$newStream;
    
    public null(String param1String, Object[] param1ArrayOfObject) {
      super(param1String, param1ArrayOfObject);
    }
    
    public void execute() {
      try {
        Http2Connection.this.listener.onStream(newStream);
      } catch (IOException iOException) {
        Platform platform = Platform.get();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Http2Connection.Listener failure for ");
        stringBuilder.append(Http2Connection.this.connectionName);
        platform.log(4, stringBuilder.toString(), iOException);
        try {
          newStream.close(ErrorCode.PROTOCOL_ERROR, iOException);
        } catch (IOException iOException1) {}
      } 
    }
  }
  
  public class null extends NamedRunnable {
    public final Http2Connection.ReaderRunnable this$1;
    
    public final boolean val$clearPrevious;
    
    public final Settings val$settings;
    
    public null(String param1String, Object[] param1ArrayOfObject) {
      super(param1String, param1ArrayOfObject);
    }
    
    public void execute() {
      this.this$1.applyAndAckSettings(clearPrevious, settings);
    }
  }
  
  public class null extends NamedRunnable {
    public final Http2Connection.ReaderRunnable this$1;
    
    public null(String param1String, Object... param1VarArgs) {
      super(param1String, param1VarArgs);
    }
    
    public void execute() {
      Http2Connection http2Connection = Http2Connection.this;
      http2Connection.listener.onSettings(http2Connection);
    }
  }
}
