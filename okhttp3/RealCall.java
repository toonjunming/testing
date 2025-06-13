package okhttp3;

import I丨L.I11L;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.Transmitter;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;

public final class RealCall implements Call {
  public final OkHttpClient client;
  
  private boolean executed;
  
  public final boolean forWebSocket;
  
  public final Request originalRequest;
  
  private Transmitter transmitter;
  
  private RealCall(OkHttpClient paramOkHttpClient, Request paramRequest, boolean paramBoolean) {
    this.client = paramOkHttpClient;
    this.originalRequest = paramRequest;
    this.forWebSocket = paramBoolean;
  }
  
  public static RealCall newRealCall(OkHttpClient paramOkHttpClient, Request paramRequest, boolean paramBoolean) {
    RealCall realCall = new RealCall(paramOkHttpClient, paramRequest, paramBoolean);
    realCall.transmitter = new Transmitter(paramOkHttpClient, realCall);
    return realCall;
  }
  
  public void cancel() {
    this.transmitter.cancel();
  }
  
  public RealCall clone() {
    return newRealCall(this.client, this.originalRequest, this.forWebSocket);
  }
  
  public void enqueue(Callback paramCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifne -> 43
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield executed : Z
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_0
    //   17: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   20: invokevirtual callStart : ()V
    //   23: aload_0
    //   24: getfield client : Lokhttp3/OkHttpClient;
    //   27: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   30: new okhttp3/RealCall$AsyncCall
    //   33: dup
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial <init> : (Lokhttp3/RealCall;Lokhttp3/Callback;)V
    //   39: invokevirtual enqueue : (Lokhttp3/RealCall$AsyncCall;)V
    //   42: return
    //   43: new java/lang/IllegalStateException
    //   46: astore_1
    //   47: aload_1
    //   48: ldc 'Already Executed'
    //   50: invokespecial <init> : (Ljava/lang/String;)V
    //   53: aload_1
    //   54: athrow
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	55	finally
    //   43	55	55	finally
    //   56	58	55	finally
  }
  
  public Response execute() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifne -> 73
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield executed : Z
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_0
    //   17: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   20: invokevirtual timeoutEnter : ()V
    //   23: aload_0
    //   24: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   27: invokevirtual callStart : ()V
    //   30: aload_0
    //   31: getfield client : Lokhttp3/OkHttpClient;
    //   34: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   37: aload_0
    //   38: invokevirtual executed : (Lokhttp3/RealCall;)V
    //   41: aload_0
    //   42: invokevirtual getResponseWithInterceptorChain : ()Lokhttp3/Response;
    //   45: astore_1
    //   46: aload_0
    //   47: getfield client : Lokhttp3/OkHttpClient;
    //   50: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   53: aload_0
    //   54: invokevirtual finished : (Lokhttp3/RealCall;)V
    //   57: aload_1
    //   58: areturn
    //   59: astore_1
    //   60: aload_0
    //   61: getfield client : Lokhttp3/OkHttpClient;
    //   64: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   67: aload_0
    //   68: invokevirtual finished : (Lokhttp3/RealCall;)V
    //   71: aload_1
    //   72: athrow
    //   73: new java/lang/IllegalStateException
    //   76: astore_1
    //   77: aload_1
    //   78: ldc 'Already Executed'
    //   80: invokespecial <init> : (Ljava/lang/String;)V
    //   83: aload_1
    //   84: athrow
    //   85: astore_1
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_1
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	85	finally
    //   30	46	59	finally
    //   73	85	85	finally
    //   86	88	85	finally
  }
  
  public Response getResponseWithInterceptorChain() throws IOException {
    ArrayList<Interceptor> arrayList = new ArrayList();
    arrayList.addAll(this.client.interceptors());
    arrayList.add(new RetryAndFollowUpInterceptor(this.client));
    arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
    arrayList.add(new CacheInterceptor(this.client.internalCache()));
    arrayList.add(new ConnectInterceptor(this.client));
    if (!this.forWebSocket)
      arrayList.addAll(this.client.networkInterceptors()); 
    arrayList.add(new CallServerInterceptor(this.forWebSocket));
    RealInterceptorChain realInterceptorChain = new RealInterceptorChain(arrayList, this.transmitter, null, 0, this.originalRequest, this, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis());
    boolean bool = false;
    try {
      Response response = realInterceptorChain.proceed(this.originalRequest);
      boolean bool1 = this.transmitter.isCanceled();
      if (!bool1) {
        this.transmitter.noMoreExchanges(null);
        return response;
      } 
      Util.closeQuietly(response);
      IOException iOException = new IOException();
      this("Canceled");
      throw iOException;
    } catch (IOException iOException) {
      try {
        throw this.transmitter.noMoreExchanges(iOException);
      } finally {
        iOException = null;
      } 
    } finally {}
    if (!bool)
      this.transmitter.noMoreExchanges(null); 
    throw realInterceptorChain;
  }
  
  public boolean isCanceled() {
    return this.transmitter.isCanceled();
  }
  
  public boolean isExecuted() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
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
  
  public String redactedUrl() {
    return this.originalRequest.url().redact();
  }
  
  public Request request() {
    return this.originalRequest;
  }
  
  public I11L timeout() {
    return this.transmitter.timeout();
  }
  
  public String toLoggableString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    if (isCanceled()) {
      str = "canceled ";
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    if (this.forWebSocket) {
      str = "web socket";
    } else {
      str = "call";
    } 
    stringBuilder.append(str);
    stringBuilder.append(" to ");
    stringBuilder.append(redactedUrl());
    return stringBuilder.toString();
  }
  
  public final class AsyncCall extends NamedRunnable {
    public static final boolean $assertionsDisabled = false;
    
    private volatile AtomicInteger callsPerHost = new AtomicInteger(0);
    
    private final Callback responseCallback;
    
    public final RealCall this$0;
    
    public AsyncCall(Callback param1Callback) {
      super("OkHttp %s", new Object[] { this$0.redactedUrl() });
      this.responseCallback = param1Callback;
    }
    
    public AtomicInteger callsPerHost() {
      return this.callsPerHost;
    }
    
    public void execute() {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lokhttp3/RealCall;
      //   4: invokestatic access$000 : (Lokhttp3/RealCall;)Lokhttp3/internal/connection/Transmitter;
      //   7: invokevirtual timeoutEnter : ()V
      //   10: aload_0
      //   11: getfield this$0 : Lokhttp3/RealCall;
      //   14: invokevirtual getResponseWithInterceptorChain : ()Lokhttp3/Response;
      //   17: astore_3
      //   18: iconst_1
      //   19: istore_1
      //   20: iconst_1
      //   21: istore_2
      //   22: aload_0
      //   23: getfield responseCallback : Lokhttp3/Callback;
      //   26: aload_0
      //   27: getfield this$0 : Lokhttp3/RealCall;
      //   30: aload_3
      //   31: invokeinterface onResponse : (Lokhttp3/Call;Lokhttp3/Response;)V
      //   36: aload_0
      //   37: getfield this$0 : Lokhttp3/RealCall;
      //   40: getfield client : Lokhttp3/OkHttpClient;
      //   43: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
      //   46: aload_0
      //   47: invokevirtual finished : (Lokhttp3/RealCall$AsyncCall;)V
      //   50: goto -> 219
      //   53: astore_3
      //   54: iload_2
      //   55: istore_1
      //   56: goto -> 66
      //   59: astore_3
      //   60: goto -> 147
      //   63: astore_3
      //   64: iconst_0
      //   65: istore_1
      //   66: aload_0
      //   67: getfield this$0 : Lokhttp3/RealCall;
      //   70: invokevirtual cancel : ()V
      //   73: iload_1
      //   74: ifne -> 138
      //   77: new java/io/IOException
      //   80: astore #4
      //   82: new java/lang/StringBuilder
      //   85: astore #5
      //   87: aload #5
      //   89: invokespecial <init> : ()V
      //   92: aload #5
      //   94: ldc 'canceled due to '
      //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   99: pop
      //   100: aload #5
      //   102: aload_3
      //   103: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   106: pop
      //   107: aload #4
      //   109: aload #5
      //   111: invokevirtual toString : ()Ljava/lang/String;
      //   114: invokespecial <init> : (Ljava/lang/String;)V
      //   117: aload #4
      //   119: aload_3
      //   120: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
      //   123: aload_0
      //   124: getfield responseCallback : Lokhttp3/Callback;
      //   127: aload_0
      //   128: getfield this$0 : Lokhttp3/RealCall;
      //   131: aload #4
      //   133: invokeinterface onFailure : (Lokhttp3/Call;Ljava/io/IOException;)V
      //   138: aload_3
      //   139: athrow
      //   140: astore_3
      //   141: goto -> 220
      //   144: astore_3
      //   145: iconst_0
      //   146: istore_1
      //   147: iload_1
      //   148: ifeq -> 202
      //   151: invokestatic get : ()Lokhttp3/internal/platform/Platform;
      //   154: astore #5
      //   156: new java/lang/StringBuilder
      //   159: astore #4
      //   161: aload #4
      //   163: invokespecial <init> : ()V
      //   166: aload #4
      //   168: ldc 'Callback failure for '
      //   170: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   173: pop
      //   174: aload #4
      //   176: aload_0
      //   177: getfield this$0 : Lokhttp3/RealCall;
      //   180: invokevirtual toLoggableString : ()Ljava/lang/String;
      //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   186: pop
      //   187: aload #5
      //   189: iconst_4
      //   190: aload #4
      //   192: invokevirtual toString : ()Ljava/lang/String;
      //   195: aload_3
      //   196: invokevirtual log : (ILjava/lang/String;Ljava/lang/Throwable;)V
      //   199: goto -> 36
      //   202: aload_0
      //   203: getfield responseCallback : Lokhttp3/Callback;
      //   206: aload_0
      //   207: getfield this$0 : Lokhttp3/RealCall;
      //   210: aload_3
      //   211: invokeinterface onFailure : (Lokhttp3/Call;Ljava/io/IOException;)V
      //   216: goto -> 36
      //   219: return
      //   220: aload_0
      //   221: getfield this$0 : Lokhttp3/RealCall;
      //   224: getfield client : Lokhttp3/OkHttpClient;
      //   227: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
      //   230: aload_0
      //   231: invokevirtual finished : (Lokhttp3/RealCall$AsyncCall;)V
      //   234: aload_3
      //   235: athrow
      // Exception table:
      //   from	to	target	type
      //   10	18	144	java/io/IOException
      //   10	18	63	finally
      //   22	36	59	java/io/IOException
      //   22	36	53	finally
      //   66	73	140	finally
      //   77	138	140	finally
      //   138	140	140	finally
      //   151	199	140	finally
      //   202	216	140	finally
    }
    
    public void executeOn(ExecutorService param1ExecutorService) {
      try {
        param1ExecutorService.execute(this);
      } catch (RejectedExecutionException rejectedExecutionException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException();
        this("executor rejected");
        interruptedIOException.initCause(rejectedExecutionException);
        RealCall.this.transmitter.noMoreExchanges(interruptedIOException);
        this.responseCallback.onFailure(RealCall.this, interruptedIOException);
        RealCall.this.client.dispatcher().finished(this);
      } finally {}
    }
    
    public RealCall get() {
      return RealCall.this;
    }
    
    public String host() {
      return RealCall.this.originalRequest.url().host();
    }
    
    public Request request() {
      return RealCall.this.originalRequest;
    }
    
    public void reuseCallsPerHostFrom(AsyncCall param1AsyncCall) {
      this.callsPerHost = param1AsyncCall.callsPerHost;
    }
  }
}
