package okhttp3.internal.ws;

import I1I.I丨iL.I丨L.IL1Iii;
import I丨L.I丨L;
import I丨L.iI丨LLL1;
import I丨L.l丨Li1LL;
import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
  public static final boolean $assertionsDisabled = false;
  
  private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
  
  private static final long MAX_QUEUE_SIZE = 16777216L;
  
  private static final List<Protocol> ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
  
  private boolean awaitingPong;
  
  private Call call;
  
  private ScheduledFuture<?> cancelFuture;
  
  private boolean enqueuedClose;
  
  private ScheduledExecutorService executor;
  
  private boolean failed;
  
  private final String key;
  
  public final WebSocketListener listener;
  
  private final ArrayDeque<Object> messageAndCloseQueue;
  
  private final Request originalRequest;
  
  private final long pingIntervalMillis;
  
  private final ArrayDeque<iI丨LLL1> pongQueue;
  
  private long queueSize;
  
  private final Random random;
  
  private WebSocketReader reader;
  
  private int receivedCloseCode;
  
  private String receivedCloseReason;
  
  private int receivedPingCount;
  
  private int receivedPongCount;
  
  private int sentPingCount;
  
  private Streams streams;
  
  private WebSocketWriter writer;
  
  private final Runnable writerRunnable;
  
  public RealWebSocket(Request paramRequest, WebSocketListener paramWebSocketListener, Random paramRandom, long paramLong) {
    byte[] arrayOfByte;
    this.pongQueue = new ArrayDeque<iI丨LLL1>();
    this.messageAndCloseQueue = new ArrayDeque();
    this.receivedCloseCode = -1;
    if ("GET".equals(paramRequest.method())) {
      this.originalRequest = paramRequest;
      this.listener = paramWebSocketListener;
      this.random = paramRandom;
      this.pingIntervalMillis = paramLong;
      arrayOfByte = new byte[16];
      paramRandom.nextBytes(arrayOfByte);
      this.key = iI丨LLL1.of(arrayOfByte).base64();
      this.writerRunnable = new IL1Iii(this);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Request must be GET: ");
    stringBuilder.append(arrayOfByte.method());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private void runWriter() {
    ScheduledExecutorService scheduledExecutorService = this.executor;
    if (scheduledExecutorService != null)
      scheduledExecutorService.execute(this.writerRunnable); 
  }
  
  private boolean send(iI丨LLL1 paramiI丨LLL1, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifne -> 95
    //   9: aload_0
    //   10: getfield enqueuedClose : Z
    //   13: ifeq -> 19
    //   16: goto -> 95
    //   19: aload_0
    //   20: getfield queueSize : J
    //   23: aload_1
    //   24: invokevirtual size : ()I
    //   27: i2l
    //   28: ladd
    //   29: ldc2_w 16777216
    //   32: lcmp
    //   33: ifle -> 49
    //   36: aload_0
    //   37: sipush #1001
    //   40: aconst_null
    //   41: invokevirtual close : (ILjava/lang/String;)Z
    //   44: pop
    //   45: aload_0
    //   46: monitorexit
    //   47: iconst_0
    //   48: ireturn
    //   49: aload_0
    //   50: aload_0
    //   51: getfield queueSize : J
    //   54: aload_1
    //   55: invokevirtual size : ()I
    //   58: i2l
    //   59: ladd
    //   60: putfield queueSize : J
    //   63: aload_0
    //   64: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   67: astore_3
    //   68: new okhttp3/internal/ws/RealWebSocket$Message
    //   71: astore #4
    //   73: aload #4
    //   75: iload_2
    //   76: aload_1
    //   77: invokespecial <init> : (ILI丨L/iI丨LLL1;)V
    //   80: aload_3
    //   81: aload #4
    //   83: invokevirtual add : (Ljava/lang/Object;)Z
    //   86: pop
    //   87: aload_0
    //   88: invokespecial runWriter : ()V
    //   91: aload_0
    //   92: monitorexit
    //   93: iconst_1
    //   94: ireturn
    //   95: aload_0
    //   96: monitorexit
    //   97: iconst_0
    //   98: ireturn
    //   99: astore_1
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_1
    //   103: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	99	finally
    //   19	45	99	finally
    //   49	91	99	finally
  }
  
  public void awaitTermination(int paramInt, TimeUnit paramTimeUnit) throws InterruptedException {
    this.executor.awaitTermination(paramInt, paramTimeUnit);
  }
  
  public void cancel() {
    this.call.cancel();
  }
  
  public void checkUpgradeSuccess(Response paramResponse, @Nullable Exchange paramExchange) throws IOException {
    StringBuilder stringBuilder1;
    if (paramResponse.code() == 101) {
      String str = paramResponse.header("Connection");
      if ("Upgrade".equalsIgnoreCase(str)) {
        str = paramResponse.header("Upgrade");
        if ("websocket".equalsIgnoreCase(str)) {
          String str1 = paramResponse.header("Sec-WebSocket-Accept");
          StringBuilder stringBuilder4 = new StringBuilder();
          stringBuilder4.append(this.key);
          stringBuilder4.append("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
          str = iI丨LLL1.encodeUtf8(stringBuilder4.toString()).sha1().base64();
          if (str.equals(str1)) {
            if (paramExchange != null)
              return; 
            throw new ProtocolException("Web Socket exchange missing: bad interceptor?");
          } 
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append("Expected 'Sec-WebSocket-Accept' header value '");
          stringBuilder3.append(str);
          stringBuilder3.append("' but was '");
          stringBuilder3.append(str1);
          stringBuilder3.append("'");
          throw new ProtocolException(stringBuilder3.toString());
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected 'Upgrade' header value 'websocket' but was '");
        stringBuilder.append(str);
        stringBuilder.append("'");
        throw new ProtocolException(stringBuilder.toString());
      } 
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected 'Connection' header value 'Upgrade' but was '");
      stringBuilder1.append(str);
      stringBuilder1.append("'");
      throw new ProtocolException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Expected HTTP 101 response but was '");
    stringBuilder2.append(stringBuilder1.code());
    stringBuilder2.append(" ");
    stringBuilder2.append(stringBuilder1.message());
    stringBuilder2.append("'");
    throw new ProtocolException(stringBuilder2.toString());
  }
  
  public boolean close(int paramInt, String paramString) {
    return close(paramInt, paramString, 60000L);
  }
  
  public boolean close(int paramInt, String paramString, long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: invokestatic validateCloseCode : (I)V
    //   6: aconst_null
    //   7: astore #5
    //   9: aload_2
    //   10: ifnull -> 79
    //   13: aload_2
    //   14: invokestatic encodeUtf8 : (Ljava/lang/String;)LI丨L/iI丨LLL1;
    //   17: astore #5
    //   19: aload #5
    //   21: invokevirtual size : ()I
    //   24: i2l
    //   25: ldc2_w 123
    //   28: lcmp
    //   29: ifgt -> 35
    //   32: goto -> 79
    //   35: new java/lang/IllegalArgumentException
    //   38: astore #5
    //   40: new java/lang/StringBuilder
    //   43: astore #6
    //   45: aload #6
    //   47: invokespecial <init> : ()V
    //   50: aload #6
    //   52: ldc_w 'reason.size() > 123: '
    //   55: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: pop
    //   59: aload #6
    //   61: aload_2
    //   62: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: pop
    //   66: aload #5
    //   68: aload #6
    //   70: invokevirtual toString : ()Ljava/lang/String;
    //   73: invokespecial <init> : (Ljava/lang/String;)V
    //   76: aload #5
    //   78: athrow
    //   79: aload_0
    //   80: getfield failed : Z
    //   83: ifne -> 135
    //   86: aload_0
    //   87: getfield enqueuedClose : Z
    //   90: ifeq -> 96
    //   93: goto -> 135
    //   96: aload_0
    //   97: iconst_1
    //   98: putfield enqueuedClose : Z
    //   101: aload_0
    //   102: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   105: astore_2
    //   106: new okhttp3/internal/ws/RealWebSocket$Close
    //   109: astore #6
    //   111: aload #6
    //   113: iload_1
    //   114: aload #5
    //   116: lload_3
    //   117: invokespecial <init> : (ILI丨L/iI丨LLL1;J)V
    //   120: aload_2
    //   121: aload #6
    //   123: invokevirtual add : (Ljava/lang/Object;)Z
    //   126: pop
    //   127: aload_0
    //   128: invokespecial runWriter : ()V
    //   131: aload_0
    //   132: monitorexit
    //   133: iconst_1
    //   134: ireturn
    //   135: aload_0
    //   136: monitorexit
    //   137: iconst_0
    //   138: ireturn
    //   139: astore_2
    //   140: aload_0
    //   141: monitorexit
    //   142: aload_2
    //   143: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	139	finally
    //   13	32	139	finally
    //   35	79	139	finally
    //   79	93	139	finally
    //   96	131	139	finally
  }
  
  public void connect(OkHttpClient paramOkHttpClient) {
    OkHttpClient okHttpClient = paramOkHttpClient.newBuilder().eventListener(EventListener.NONE).protocols(ONLY_HTTP1).build();
    final Request request = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
    Call call = Internal.instance.newWebSocketCall(okHttpClient, request);
    this.call = call;
    call.enqueue(new Callback() {
          public final RealWebSocket this$0;
          
          public final Request val$request;
          
          public void onFailure(Call param1Call, IOException param1IOException) {
            RealWebSocket.this.failWebSocket(param1IOException, null);
          }
          
          public void onResponse(Call param1Call, Response param1Response) {
            String str;
            Exchange exchange = Internal.instance.exchange(param1Response);
            try {
              RealWebSocket.this.checkUpgradeSuccess(param1Response, exchange);
              RealWebSocket.Streams streams = exchange.newWebSocketStreams();
              try {
                StringBuilder stringBuilder = new StringBuilder();
                this();
                stringBuilder.append("OkHttp WebSocket ");
                stringBuilder.append(request.url().redact());
                str = stringBuilder.toString();
                RealWebSocket.this.initReaderAndWriter(str, streams);
                RealWebSocket realWebSocket = RealWebSocket.this;
                realWebSocket.listener.onOpen(realWebSocket, param1Response);
                RealWebSocket.this.loopReader();
              } catch (Exception exception) {
                RealWebSocket.this.failWebSocket(exception, null);
              } 
              return;
            } catch (IOException iOException) {
              if (str != null)
                str.webSocketUpgradeFailed(); 
              RealWebSocket.this.failWebSocket(iOException, param1Response);
              Util.closeQuietly(param1Response);
              return;
            } 
          }
        });
  }
  
  public void failWebSocket(Exception paramException, @Nullable Response paramResponse) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifeq -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield failed : Z
    //   17: aload_0
    //   18: getfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   21: astore_3
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   27: aload_0
    //   28: getfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   31: astore #4
    //   33: aload #4
    //   35: ifnull -> 47
    //   38: aload #4
    //   40: iconst_0
    //   41: invokeinterface cancel : (Z)Z
    //   46: pop
    //   47: aload_0
    //   48: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   51: astore #4
    //   53: aload #4
    //   55: ifnull -> 65
    //   58: aload #4
    //   60: invokeinterface shutdown : ()V
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_0
    //   68: getfield listener : Lokhttp3/WebSocketListener;
    //   71: aload_0
    //   72: aload_1
    //   73: aload_2
    //   74: invokevirtual onFailure : (Lokhttp3/WebSocket;Ljava/lang/Throwable;Lokhttp3/Response;)V
    //   77: aload_3
    //   78: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   81: return
    //   82: astore_1
    //   83: aload_3
    //   84: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   87: aload_1
    //   88: athrow
    //   89: astore_1
    //   90: aload_0
    //   91: monitorexit
    //   92: aload_1
    //   93: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	89	finally
    //   12	33	89	finally
    //   38	47	89	finally
    //   47	53	89	finally
    //   58	65	89	finally
    //   65	67	89	finally
    //   67	77	82	finally
    //   90	92	89	finally
  }
  
  public void initReaderAndWriter(String paramString, Streams paramStreams) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   7: new okhttp3/internal/ws/WebSocketWriter
    //   10: astore #5
    //   12: aload #5
    //   14: aload_2
    //   15: getfield client : Z
    //   18: aload_2
    //   19: getfield sink : LI丨L/I丨L;
    //   22: aload_0
    //   23: getfield random : Ljava/util/Random;
    //   26: invokespecial <init> : (ZLI丨L/I丨L;Ljava/util/Random;)V
    //   29: aload_0
    //   30: aload #5
    //   32: putfield writer : Lokhttp3/internal/ws/WebSocketWriter;
    //   35: new java/util/concurrent/ScheduledThreadPoolExecutor
    //   38: astore #5
    //   40: aload #5
    //   42: iconst_1
    //   43: aload_1
    //   44: iconst_0
    //   45: invokestatic threadFactory : (Ljava/lang/String;Z)Ljava/util/concurrent/ThreadFactory;
    //   48: invokespecial <init> : (ILjava/util/concurrent/ThreadFactory;)V
    //   51: aload_0
    //   52: aload #5
    //   54: putfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   57: aload_0
    //   58: getfield pingIntervalMillis : J
    //   61: lconst_0
    //   62: lcmp
    //   63: ifeq -> 94
    //   66: new okhttp3/internal/ws/RealWebSocket$PingRunnable
    //   69: astore_1
    //   70: aload_1
    //   71: aload_0
    //   72: invokespecial <init> : (Lokhttp3/internal/ws/RealWebSocket;)V
    //   75: aload_0
    //   76: getfield pingIntervalMillis : J
    //   79: lstore_3
    //   80: aload #5
    //   82: aload_1
    //   83: lload_3
    //   84: lload_3
    //   85: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   88: invokeinterface scheduleAtFixedRate : (Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   93: pop
    //   94: aload_0
    //   95: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   98: invokevirtual isEmpty : ()Z
    //   101: ifne -> 108
    //   104: aload_0
    //   105: invokespecial runWriter : ()V
    //   108: aload_0
    //   109: monitorexit
    //   110: aload_0
    //   111: new okhttp3/internal/ws/WebSocketReader
    //   114: dup
    //   115: aload_2
    //   116: getfield client : Z
    //   119: aload_2
    //   120: getfield source : LI丨L/l丨Li1LL;
    //   123: aload_0
    //   124: invokespecial <init> : (ZLI丨L/l丨Li1LL;Lokhttp3/internal/ws/WebSocketReader$FrameCallback;)V
    //   127: putfield reader : Lokhttp3/internal/ws/WebSocketReader;
    //   130: return
    //   131: astore_1
    //   132: aload_0
    //   133: monitorexit
    //   134: aload_1
    //   135: athrow
    // Exception table:
    //   from	to	target	type
    //   2	94	131	finally
    //   94	108	131	finally
    //   108	110	131	finally
    //   132	134	131	finally
  }
  
  public void loopReader() throws IOException {
    while (this.receivedCloseCode == -1)
      this.reader.processNextFrame(); 
  }
  
  public void onReadClose(int paramInt, String paramString) {
    // Byte code:
    //   0: iload_1
    //   1: iconst_m1
    //   2: if_icmpeq -> 154
    //   5: aload_0
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield receivedCloseCode : I
    //   11: iconst_m1
    //   12: if_icmpne -> 136
    //   15: aload_0
    //   16: iload_1
    //   17: putfield receivedCloseCode : I
    //   20: aload_0
    //   21: aload_2
    //   22: putfield receivedCloseReason : Ljava/lang/String;
    //   25: aload_0
    //   26: getfield enqueuedClose : Z
    //   29: istore_3
    //   30: aconst_null
    //   31: astore #5
    //   33: aload #5
    //   35: astore #4
    //   37: iload_3
    //   38: ifeq -> 95
    //   41: aload #5
    //   43: astore #4
    //   45: aload_0
    //   46: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   49: invokevirtual isEmpty : ()Z
    //   52: ifeq -> 95
    //   55: aload_0
    //   56: getfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   59: astore #4
    //   61: aload_0
    //   62: aconst_null
    //   63: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   66: aload_0
    //   67: getfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   70: astore #5
    //   72: aload #5
    //   74: ifnull -> 86
    //   77: aload #5
    //   79: iconst_0
    //   80: invokeinterface cancel : (Z)Z
    //   85: pop
    //   86: aload_0
    //   87: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   90: invokeinterface shutdown : ()V
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_0
    //   98: getfield listener : Lokhttp3/WebSocketListener;
    //   101: aload_0
    //   102: iload_1
    //   103: aload_2
    //   104: invokevirtual onClosing : (Lokhttp3/WebSocket;ILjava/lang/String;)V
    //   107: aload #4
    //   109: ifnull -> 122
    //   112: aload_0
    //   113: getfield listener : Lokhttp3/WebSocketListener;
    //   116: aload_0
    //   117: iload_1
    //   118: aload_2
    //   119: invokevirtual onClosed : (Lokhttp3/WebSocket;ILjava/lang/String;)V
    //   122: aload #4
    //   124: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   127: return
    //   128: astore_2
    //   129: aload #4
    //   131: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   134: aload_2
    //   135: athrow
    //   136: new java/lang/IllegalStateException
    //   139: astore_2
    //   140: aload_2
    //   141: ldc_w 'already closed'
    //   144: invokespecial <init> : (Ljava/lang/String;)V
    //   147: aload_2
    //   148: athrow
    //   149: astore_2
    //   150: aload_0
    //   151: monitorexit
    //   152: aload_2
    //   153: athrow
    //   154: new java/lang/IllegalArgumentException
    //   157: dup
    //   158: invokespecial <init> : ()V
    //   161: athrow
    // Exception table:
    //   from	to	target	type
    //   7	30	149	finally
    //   45	72	149	finally
    //   77	86	149	finally
    //   86	95	149	finally
    //   95	97	149	finally
    //   97	107	128	finally
    //   112	122	128	finally
    //   136	149	149	finally
    //   150	152	149	finally
  }
  
  public void onReadMessage(iI丨LLL1 paramiI丨LLL1) throws IOException {
    this.listener.onMessage(this, paramiI丨LLL1);
  }
  
  public void onReadMessage(String paramString) throws IOException {
    this.listener.onMessage(this, paramString);
  }
  
  public void onReadPing(iI丨LLL1 paramiI丨LLL1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifne -> 55
    //   9: aload_0
    //   10: getfield enqueuedClose : Z
    //   13: ifeq -> 29
    //   16: aload_0
    //   17: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   20: invokevirtual isEmpty : ()Z
    //   23: ifeq -> 29
    //   26: goto -> 55
    //   29: aload_0
    //   30: getfield pongQueue : Ljava/util/ArrayDeque;
    //   33: aload_1
    //   34: invokevirtual add : (Ljava/lang/Object;)Z
    //   37: pop
    //   38: aload_0
    //   39: invokespecial runWriter : ()V
    //   42: aload_0
    //   43: aload_0
    //   44: getfield receivedPingCount : I
    //   47: iconst_1
    //   48: iadd
    //   49: putfield receivedPingCount : I
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: aload_0
    //   56: monitorexit
    //   57: return
    //   58: astore_1
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_1
    //   62: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	58	finally
    //   29	52	58	finally
  }
  
  public void onReadPong(iI丨LLL1 paramiI丨LLL1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield receivedPongCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield receivedPongCount : I
    //   12: aload_0
    //   13: iconst_0
    //   14: putfield awaitingPong : Z
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public boolean pong(iI丨LLL1 paramiI丨LLL1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifne -> 46
    //   9: aload_0
    //   10: getfield enqueuedClose : Z
    //   13: ifeq -> 29
    //   16: aload_0
    //   17: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   20: invokevirtual isEmpty : ()Z
    //   23: ifeq -> 29
    //   26: goto -> 46
    //   29: aload_0
    //   30: getfield pongQueue : Ljava/util/ArrayDeque;
    //   33: aload_1
    //   34: invokevirtual add : (Ljava/lang/Object;)Z
    //   37: pop
    //   38: aload_0
    //   39: invokespecial runWriter : ()V
    //   42: aload_0
    //   43: monitorexit
    //   44: iconst_1
    //   45: ireturn
    //   46: aload_0
    //   47: monitorexit
    //   48: iconst_0
    //   49: ireturn
    //   50: astore_1
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_1
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	50	finally
    //   29	42	50	finally
  }
  
  public boolean processNextFrame() throws IOException {
    boolean bool = false;
    try {
      this.reader.processNextFrame();
      int i = this.receivedCloseCode;
      if (i == -1)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      failWebSocket(exception, null);
      return false;
    } 
  }
  
  public long queueSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield queueSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int receivedPingCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield receivedPingCount : I
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
  
  public int receivedPongCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield receivedPongCount : I
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
  
  public Request request() {
    return this.originalRequest;
  }
  
  public boolean send(iI丨LLL1 paramiI丨LLL1) {
    Objects.requireNonNull(paramiI丨LLL1, "bytes == null");
    return send(paramiI丨LLL1, 2);
  }
  
  public boolean send(String paramString) {
    Objects.requireNonNull(paramString, "text == null");
    return send(iI丨LLL1.encodeUtf8(paramString), 1);
  }
  
  public int sentPingCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sentPingCount : I
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
  
  public void tearDown() throws InterruptedException {
    ScheduledFuture<?> scheduledFuture = this.cancelFuture;
    if (scheduledFuture != null)
      scheduledFuture.cancel(false); 
    this.executor.shutdown();
    this.executor.awaitTermination(10L, TimeUnit.SECONDS);
  }
  
  public boolean writeOneFrame() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifeq -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_0
    //   14: getfield writer : Lokhttp3/internal/ws/WebSocketWriter;
    //   17: astore #6
    //   19: aload_0
    //   20: getfield pongQueue : Ljava/util/ArrayDeque;
    //   23: invokevirtual poll : ()Ljava/lang/Object;
    //   26: checkcast I丨L/iI丨LLL1
    //   29: astore #7
    //   31: iconst_m1
    //   32: istore_1
    //   33: aconst_null
    //   34: astore_3
    //   35: aload #7
    //   37: ifnonnull -> 161
    //   40: aload_0
    //   41: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   44: invokevirtual poll : ()Ljava/lang/Object;
    //   47: astore_2
    //   48: aload_2
    //   49: instanceof okhttp3/internal/ws/RealWebSocket$Close
    //   52: ifeq -> 139
    //   55: aload_0
    //   56: getfield receivedCloseCode : I
    //   59: istore_1
    //   60: aload_0
    //   61: getfield receivedCloseReason : Ljava/lang/String;
    //   64: astore #4
    //   66: iload_1
    //   67: iconst_m1
    //   68: if_icmpeq -> 99
    //   71: aload_0
    //   72: getfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   75: astore #5
    //   77: aload_0
    //   78: aconst_null
    //   79: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   82: aload_0
    //   83: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   86: invokeinterface shutdown : ()V
    //   91: aload_2
    //   92: astore_3
    //   93: aload #5
    //   95: astore_2
    //   96: goto -> 166
    //   99: aload_0
    //   100: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   103: astore #5
    //   105: new okhttp3/internal/ws/RealWebSocket$CancelRunnable
    //   108: astore_3
    //   109: aload_3
    //   110: aload_0
    //   111: invokespecial <init> : (Lokhttp3/internal/ws/RealWebSocket;)V
    //   114: aload_0
    //   115: aload #5
    //   117: aload_3
    //   118: aload_2
    //   119: checkcast okhttp3/internal/ws/RealWebSocket$Close
    //   122: getfield cancelAfterCloseMillis : J
    //   125: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   128: invokeinterface schedule : (Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   133: putfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   136: goto -> 150
    //   139: aload_2
    //   140: ifnonnull -> 147
    //   143: aload_0
    //   144: monitorexit
    //   145: iconst_0
    //   146: ireturn
    //   147: aconst_null
    //   148: astore #4
    //   150: aconst_null
    //   151: astore #5
    //   153: aload_2
    //   154: astore_3
    //   155: aload #5
    //   157: astore_2
    //   158: goto -> 166
    //   161: aconst_null
    //   162: astore_2
    //   163: aconst_null
    //   164: astore #4
    //   166: aload_0
    //   167: monitorexit
    //   168: aload #7
    //   170: ifnull -> 183
    //   173: aload #6
    //   175: aload #7
    //   177: invokevirtual writePong : (LI丨L/iI丨LLL1;)V
    //   180: goto -> 303
    //   183: aload_3
    //   184: instanceof okhttp3/internal/ws/RealWebSocket$Message
    //   187: ifeq -> 263
    //   190: aload_3
    //   191: checkcast okhttp3/internal/ws/RealWebSocket$Message
    //   194: getfield data : LI丨L/iI丨LLL1;
    //   197: astore #4
    //   199: aload #6
    //   201: aload_3
    //   202: checkcast okhttp3/internal/ws/RealWebSocket$Message
    //   205: getfield formatOpcode : I
    //   208: aload #4
    //   210: invokevirtual size : ()I
    //   213: i2l
    //   214: invokevirtual newMessageSink : (IJ)LI丨L/I11li1;
    //   217: invokestatic I1I : (LI丨L/I11li1;)LI丨L/I丨L;
    //   220: astore_3
    //   221: aload_3
    //   222: aload #4
    //   224: invokeinterface iIlLiL : (LI丨L/iI丨LLL1;)LI丨L/I丨L;
    //   229: pop
    //   230: aload_3
    //   231: invokeinterface close : ()V
    //   236: aload_0
    //   237: monitorenter
    //   238: aload_0
    //   239: aload_0
    //   240: getfield queueSize : J
    //   243: aload #4
    //   245: invokevirtual size : ()I
    //   248: i2l
    //   249: lsub
    //   250: putfield queueSize : J
    //   253: aload_0
    //   254: monitorexit
    //   255: goto -> 303
    //   258: astore_3
    //   259: aload_0
    //   260: monitorexit
    //   261: aload_3
    //   262: athrow
    //   263: aload_3
    //   264: instanceof okhttp3/internal/ws/RealWebSocket$Close
    //   267: ifeq -> 309
    //   270: aload_3
    //   271: checkcast okhttp3/internal/ws/RealWebSocket$Close
    //   274: astore_3
    //   275: aload #6
    //   277: aload_3
    //   278: getfield code : I
    //   281: aload_3
    //   282: getfield reason : LI丨L/iI丨LLL1;
    //   285: invokevirtual writeClose : (ILI丨L/iI丨LLL1;)V
    //   288: aload_2
    //   289: ifnull -> 303
    //   292: aload_0
    //   293: getfield listener : Lokhttp3/WebSocketListener;
    //   296: aload_0
    //   297: iload_1
    //   298: aload #4
    //   300: invokevirtual onClosed : (Lokhttp3/WebSocket;ILjava/lang/String;)V
    //   303: aload_2
    //   304: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   307: iconst_1
    //   308: ireturn
    //   309: new java/lang/AssertionError
    //   312: astore_3
    //   313: aload_3
    //   314: invokespecial <init> : ()V
    //   317: aload_3
    //   318: athrow
    //   319: astore_3
    //   320: aload_2
    //   321: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   324: aload_3
    //   325: athrow
    //   326: astore_2
    //   327: aload_0
    //   328: monitorexit
    //   329: aload_2
    //   330: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	326	finally
    //   13	31	326	finally
    //   40	66	326	finally
    //   71	91	326	finally
    //   99	136	326	finally
    //   143	145	326	finally
    //   166	168	326	finally
    //   173	180	319	finally
    //   183	238	319	finally
    //   238	255	258	finally
    //   259	261	258	finally
    //   261	263	319	finally
    //   263	288	319	finally
    //   292	303	319	finally
    //   309	319	319	finally
    //   327	329	326	finally
  }
  
  public void writePingFrame() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifeq -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: getfield writer : Lokhttp3/internal/ws/WebSocketWriter;
    //   16: astore_2
    //   17: aload_0
    //   18: getfield awaitingPong : Z
    //   21: ifeq -> 32
    //   24: aload_0
    //   25: getfield sentPingCount : I
    //   28: istore_1
    //   29: goto -> 34
    //   32: iconst_m1
    //   33: istore_1
    //   34: aload_0
    //   35: aload_0
    //   36: getfield sentPingCount : I
    //   39: iconst_1
    //   40: iadd
    //   41: putfield sentPingCount : I
    //   44: aload_0
    //   45: iconst_1
    //   46: putfield awaitingPong : Z
    //   49: aload_0
    //   50: monitorexit
    //   51: iload_1
    //   52: iconst_m1
    //   53: if_icmpeq -> 122
    //   56: new java/lang/StringBuilder
    //   59: dup
    //   60: invokespecial <init> : ()V
    //   63: astore_2
    //   64: aload_2
    //   65: ldc_w 'sent ping but didn't receive pong within '
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload_2
    //   73: aload_0
    //   74: getfield pingIntervalMillis : J
    //   77: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_2
    //   82: ldc_w 'ms (after '
    //   85: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: pop
    //   89: aload_2
    //   90: iload_1
    //   91: iconst_1
    //   92: isub
    //   93: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload_2
    //   98: ldc_w ' successful ping/pongs)'
    //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload_0
    //   106: new java/net/SocketTimeoutException
    //   109: dup
    //   110: aload_2
    //   111: invokevirtual toString : ()Ljava/lang/String;
    //   114: invokespecial <init> : (Ljava/lang/String;)V
    //   117: aconst_null
    //   118: invokevirtual failWebSocket : (Ljava/lang/Exception;Lokhttp3/Response;)V
    //   121: return
    //   122: aload_2
    //   123: getstatic I丨L/iI丨LLL1.EMPTY : LI丨L/iI丨LLL1;
    //   126: invokevirtual writePing : (LI丨L/iI丨LLL1;)V
    //   129: goto -> 139
    //   132: astore_2
    //   133: aload_0
    //   134: aload_2
    //   135: aconst_null
    //   136: invokevirtual failWebSocket : (Ljava/lang/Exception;Lokhttp3/Response;)V
    //   139: return
    //   140: astore_2
    //   141: aload_0
    //   142: monitorexit
    //   143: aload_2
    //   144: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	140	finally
    //   12	29	140	finally
    //   34	51	140	finally
    //   122	129	132	java/io/IOException
    //   141	143	140	finally
  }
  
  public final class CancelRunnable implements Runnable {
    public final RealWebSocket this$0;
    
    public void run() {
      RealWebSocket.this.cancel();
    }
  }
  
  public static final class Close {
    public final long cancelAfterCloseMillis;
    
    public final int code;
    
    public final iI丨LLL1 reason;
    
    public Close(int param1Int, iI丨LLL1 param1iI丨LLL1, long param1Long) {
      this.code = param1Int;
      this.reason = param1iI丨LLL1;
      this.cancelAfterCloseMillis = param1Long;
    }
  }
  
  public static final class Message {
    public final iI丨LLL1 data;
    
    public final int formatOpcode;
    
    public Message(int param1Int, iI丨LLL1 param1iI丨LLL1) {
      this.formatOpcode = param1Int;
      this.data = param1iI丨LLL1;
    }
  }
  
  public final class PingRunnable implements Runnable {
    public final RealWebSocket this$0;
    
    public void run() {
      RealWebSocket.this.writePingFrame();
    }
  }
  
  public static abstract class Streams implements Closeable {
    public final boolean client;
    
    public final I丨L sink;
    
    public final l丨Li1LL source;
    
    public Streams(boolean param1Boolean, l丨Li1LL param1l丨Li1LL, I丨L param1I丨L) {
      this.client = param1Boolean;
      this.source = param1l丨Li1LL;
      this.sink = param1I丨L;
    }
  }
}
