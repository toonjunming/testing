package okhttp3.internal.connection;

import I丨L.I11L;
import I丨L.IL1Iii;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.platform.Platform;

public final class Transmitter {
  public static final boolean $assertionsDisabled = false;
  
  private final Call call;
  
  @Nullable
  private Object callStackTrace;
  
  private boolean canceled;
  
  private final OkHttpClient client;
  
  public RealConnection connection;
  
  private final RealConnectionPool connectionPool;
  
  private final EventListener eventListener;
  
  @Nullable
  private Exchange exchange;
  
  private ExchangeFinder exchangeFinder;
  
  private boolean exchangeRequestDone;
  
  private boolean exchangeResponseDone;
  
  private boolean noMoreExchanges;
  
  private Request request;
  
  private final IL1Iii timeout;
  
  private boolean timeoutEarlyExit;
  
  public Transmitter(OkHttpClient paramOkHttpClient, Call paramCall) {
    IL1Iii iL1Iii = new IL1Iii() {
        public final Transmitter this$0;
        
        public void timedOut() {
          Transmitter.this.cancel();
        }
      };
    this.timeout = iL1Iii;
    this.client = paramOkHttpClient;
    this.connectionPool = Internal.instance.realConnectionPool(paramOkHttpClient.connectionPool());
    this.call = paramCall;
    this.eventListener = paramOkHttpClient.eventListenerFactory().create(paramCall);
    iL1Iii.timeout(paramOkHttpClient.callTimeoutMillis(), TimeUnit.MILLISECONDS);
  }
  
  private Address createAddress(HttpUrl paramHttpUrl) {
    HostnameVerifier hostnameVerifier1;
    SSLSocketFactory sSLSocketFactory;
    HostnameVerifier hostnameVerifier2;
    if (paramHttpUrl.isHttps()) {
      sSLSocketFactory = this.client.sslSocketFactory();
      hostnameVerifier1 = this.client.hostnameVerifier();
      hostnameVerifier2 = (HostnameVerifier)this.client.certificatePinner();
    } else {
      sSLSocketFactory = null;
      hostnameVerifier1 = null;
      hostnameVerifier2 = hostnameVerifier1;
    } 
    return new Address(paramHttpUrl.host(), paramHttpUrl.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory, hostnameVerifier1, (CertificatePinner)hostnameVerifier2, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
  }
  
  @Nullable
  private IOException maybeReleaseConnection(@Nullable IOException paramIOException, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   4: astore #7
    //   6: aload #7
    //   8: monitorenter
    //   9: iload_2
    //   10: ifeq -> 35
    //   13: aload_0
    //   14: getfield exchange : Lokhttp3/internal/connection/Exchange;
    //   17: ifnonnull -> 23
    //   20: goto -> 35
    //   23: new java/lang/IllegalStateException
    //   26: astore_1
    //   27: aload_1
    //   28: ldc 'cannot release connection while it is in use'
    //   30: invokespecial <init> : (Ljava/lang/String;)V
    //   33: aload_1
    //   34: athrow
    //   35: aload_0
    //   36: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   39: astore #6
    //   41: aload #6
    //   43: ifnull -> 73
    //   46: aload_0
    //   47: getfield exchange : Lokhttp3/internal/connection/Exchange;
    //   50: ifnonnull -> 73
    //   53: iload_2
    //   54: ifne -> 64
    //   57: aload_0
    //   58: getfield noMoreExchanges : Z
    //   61: ifeq -> 73
    //   64: aload_0
    //   65: invokevirtual releaseConnectionNoEvents : ()Ljava/net/Socket;
    //   68: astore #5
    //   70: goto -> 76
    //   73: aconst_null
    //   74: astore #5
    //   76: aload_0
    //   77: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   80: ifnull -> 86
    //   83: aconst_null
    //   84: astore #6
    //   86: aload_0
    //   87: getfield noMoreExchanges : Z
    //   90: istore_2
    //   91: iconst_1
    //   92: istore #4
    //   94: iload_2
    //   95: ifeq -> 110
    //   98: aload_0
    //   99: getfield exchange : Lokhttp3/internal/connection/Exchange;
    //   102: ifnonnull -> 110
    //   105: iconst_1
    //   106: istore_3
    //   107: goto -> 112
    //   110: iconst_0
    //   111: istore_3
    //   112: aload #7
    //   114: monitorexit
    //   115: aload #5
    //   117: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   120: aload #6
    //   122: ifnull -> 138
    //   125: aload_0
    //   126: getfield eventListener : Lokhttp3/EventListener;
    //   129: aload_0
    //   130: getfield call : Lokhttp3/Call;
    //   133: aload #6
    //   135: invokevirtual connectionReleased : (Lokhttp3/Call;Lokhttp3/Connection;)V
    //   138: aload_1
    //   139: astore #5
    //   141: iload_3
    //   142: ifeq -> 195
    //   145: aload_1
    //   146: ifnull -> 155
    //   149: iload #4
    //   151: istore_3
    //   152: goto -> 157
    //   155: iconst_0
    //   156: istore_3
    //   157: aload_0
    //   158: aload_1
    //   159: invokespecial timeoutExit : (Ljava/io/IOException;)Ljava/io/IOException;
    //   162: astore #5
    //   164: iload_3
    //   165: ifeq -> 184
    //   168: aload_0
    //   169: getfield eventListener : Lokhttp3/EventListener;
    //   172: aload_0
    //   173: getfield call : Lokhttp3/Call;
    //   176: aload #5
    //   178: invokevirtual callFailed : (Lokhttp3/Call;Ljava/io/IOException;)V
    //   181: goto -> 195
    //   184: aload_0
    //   185: getfield eventListener : Lokhttp3/EventListener;
    //   188: aload_0
    //   189: getfield call : Lokhttp3/Call;
    //   192: invokevirtual callEnd : (Lokhttp3/Call;)V
    //   195: aload #5
    //   197: areturn
    //   198: astore_1
    //   199: aload #7
    //   201: monitorexit
    //   202: aload_1
    //   203: athrow
    // Exception table:
    //   from	to	target	type
    //   13	20	198	finally
    //   23	35	198	finally
    //   35	41	198	finally
    //   46	53	198	finally
    //   57	64	198	finally
    //   64	70	198	finally
    //   76	83	198	finally
    //   86	91	198	finally
    //   98	105	198	finally
    //   112	115	198	finally
    //   199	202	198	finally
  }
  
  @Nullable
  private IOException timeoutExit(@Nullable IOException paramIOException) {
    if (this.timeoutEarlyExit)
      return paramIOException; 
    if (!this.timeout.exit())
      return paramIOException; 
    InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
    if (paramIOException != null)
      interruptedIOException.initCause(paramIOException); 
    return interruptedIOException;
  }
  
  public void acquireConnectionNoEvents(RealConnection paramRealConnection) {
    if (this.connection == null) {
      this.connection = paramRealConnection;
      paramRealConnection.transmitters.add(new TransmitterReference(this, this.callStackTrace));
      return;
    } 
    throw new IllegalStateException();
  }
  
  public void callStart() {
    this.callStackTrace = Platform.get().getStackTraceForCloseable("response.body().close()");
    this.eventListener.callStart(this.call);
  }
  
  public boolean canRetry() {
    boolean bool;
    if (this.exchangeFinder.hasStreamFailure() && this.exchangeFinder.hasRouteToTry()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void cancel() {
    synchronized (this.connectionPool) {
      RealConnection realConnection;
      this.canceled = true;
      Exchange exchange = this.exchange;
      ExchangeFinder exchangeFinder = this.exchangeFinder;
      if (exchangeFinder != null && exchangeFinder.connectingConnection() != null) {
        realConnection = this.exchangeFinder.connectingConnection();
      } else {
        realConnection = this.connection;
      } 
      if (exchange != null) {
        exchange.cancel();
      } else if (realConnection != null) {
        realConnection.cancel();
      } 
      return;
    } 
  }
  
  public void exchangeDoneDueToException() {
    synchronized (this.connectionPool) {
      if (!this.noMoreExchanges) {
        this.exchange = null;
        return;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this();
      throw illegalStateException;
    } 
  }
  
  @Nullable
  public IOException exchangeMessageDone(Exchange paramExchange, boolean paramBoolean1, boolean paramBoolean2, @Nullable IOException paramIOException) {
    synchronized (this.connectionPool) {
      boolean bool1;
      Exchange exchange = this.exchange;
      if (paramExchange != exchange)
        return paramIOException; 
      boolean bool3 = true;
      if (paramBoolean1) {
        bool1 = this.exchangeRequestDone ^ true;
        this.exchangeRequestDone = true;
      } else {
        bool1 = false;
      } 
      boolean bool2 = bool1;
      if (paramBoolean2) {
        if (!this.exchangeResponseDone)
          bool1 = true; 
        this.exchangeResponseDone = true;
        bool2 = bool1;
      } 
      if (this.exchangeRequestDone && this.exchangeResponseDone && bool2) {
        RealConnection realConnection = exchange.connection();
        realConnection.successCount++;
        this.exchange = null;
        bool1 = bool3;
      } else {
        bool1 = false;
      } 
      IOException iOException = paramIOException;
      if (bool1)
        iOException = maybeReleaseConnection(paramIOException, false); 
      return iOException;
    } 
  }
  
  public boolean hasExchange() {
    synchronized (this.connectionPool) {
      boolean bool;
      if (this.exchange != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isCanceled() {
    synchronized (this.connectionPool) {
      return this.canceled;
    } 
  }
  
  public Exchange newExchange(Interceptor.Chain paramChain, boolean paramBoolean) {
    synchronized (this.connectionPool) {
      if (!this.noMoreExchanges) {
        if (this.exchange == null) {
          ExchangeCodec exchangeCodec = this.exchangeFinder.find(this.client, paramChain, paramBoolean);
          null = new Exchange(this, this.call, this.eventListener, this.exchangeFinder, exchangeCodec);
          synchronized (this.connectionPool) {
            this.exchange = null;
            this.exchangeRequestDone = false;
            this.exchangeResponseDone = false;
            return null;
          } 
        } 
        IllegalStateException illegalStateException1 = new IllegalStateException();
        this("cannot make a new request because the previous response is still open: please call response.close()");
        throw illegalStateException1;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this("released");
      throw illegalStateException;
    } 
  }
  
  @Nullable
  public IOException noMoreExchanges(@Nullable IOException paramIOException) {
    synchronized (this.connectionPool) {
      this.noMoreExchanges = true;
      return maybeReleaseConnection(paramIOException, false);
    } 
  }
  
  public void prepareToConnect(Request paramRequest) {
    Request request = this.request;
    if (request != null) {
      if (Util.sameConnection(request.url(), paramRequest.url()) && this.exchangeFinder.hasRouteToTry())
        return; 
      if (this.exchange == null) {
        if (this.exchangeFinder != null) {
          maybeReleaseConnection(null, true);
          this.exchangeFinder = null;
        } 
      } else {
        throw new IllegalStateException();
      } 
    } 
    this.request = paramRequest;
    this.exchangeFinder = new ExchangeFinder(this, this.connectionPool, createAddress(paramRequest.url()), this.call, this.eventListener);
  }
  
  @Nullable
  public Socket releaseConnectionNoEvents() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   6: getfield transmitters : Ljava/util/List;
    //   9: invokeinterface size : ()I
    //   14: istore_2
    //   15: iload_1
    //   16: iload_2
    //   17: if_icmpge -> 52
    //   20: aload_0
    //   21: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   24: getfield transmitters : Ljava/util/List;
    //   27: iload_1
    //   28: invokeinterface get : (I)Ljava/lang/Object;
    //   33: checkcast java/lang/ref/Reference
    //   36: invokevirtual get : ()Ljava/lang/Object;
    //   39: aload_0
    //   40: if_acmpne -> 46
    //   43: goto -> 54
    //   46: iinc #1, 1
    //   49: goto -> 15
    //   52: iconst_m1
    //   53: istore_1
    //   54: iload_1
    //   55: iconst_m1
    //   56: if_icmpeq -> 132
    //   59: aload_0
    //   60: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   63: astore #5
    //   65: aload #5
    //   67: getfield transmitters : Ljava/util/List;
    //   70: iload_1
    //   71: invokeinterface remove : (I)Ljava/lang/Object;
    //   76: pop
    //   77: aconst_null
    //   78: astore #4
    //   80: aload_0
    //   81: aconst_null
    //   82: putfield connection : Lokhttp3/internal/connection/RealConnection;
    //   85: aload #4
    //   87: astore_3
    //   88: aload #5
    //   90: getfield transmitters : Ljava/util/List;
    //   93: invokeinterface isEmpty : ()Z
    //   98: ifeq -> 130
    //   101: aload #5
    //   103: invokestatic nanoTime : ()J
    //   106: putfield idleAtNanos : J
    //   109: aload #4
    //   111: astore_3
    //   112: aload_0
    //   113: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   116: aload #5
    //   118: invokevirtual connectionBecameIdle : (Lokhttp3/internal/connection/RealConnection;)Z
    //   121: ifeq -> 130
    //   124: aload #5
    //   126: invokevirtual socket : ()Ljava/net/Socket;
    //   129: astore_3
    //   130: aload_3
    //   131: areturn
    //   132: new java/lang/IllegalStateException
    //   135: dup
    //   136: invokespecial <init> : ()V
    //   139: athrow
  }
  
  public I11L timeout() {
    return this.timeout;
  }
  
  public void timeoutEarlyExit() {
    if (!this.timeoutEarlyExit) {
      this.timeoutEarlyExit = true;
      this.timeout.exit();
      return;
    } 
    throw new IllegalStateException();
  }
  
  public void timeoutEnter() {
    this.timeout.enter();
  }
  
  public static final class TransmitterReference extends WeakReference<Transmitter> {
    public final Object callStackTrace;
    
    public TransmitterReference(Transmitter param1Transmitter, Object param1Object) {
      super(param1Transmitter);
      this.callStackTrace = param1Object;
    }
  }
}
