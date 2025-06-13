package okhttp3.internal.connection;

import I丨L.I11L;
import I丨L.I丨L;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http1.Http1ExchangeCodec;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2ExchangeCodec;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.http2.StreamResetException;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;

public final class RealConnection extends Http2Connection.Listener implements Connection {
  public static final boolean $assertionsDisabled = false;
  
  private static final int MAX_TUNNEL_ATTEMPTS = 21;
  
  private static final String NPE_THROW_WITH_NULL = "throw with null exception";
  
  private int allocationLimit = 1;
  
  public final RealConnectionPool connectionPool;
  
  private Handshake handshake;
  
  private Http2Connection http2Connection;
  
  public long idleAtNanos = Long.MAX_VALUE;
  
  public boolean noNewExchanges;
  
  private Protocol protocol;
  
  private Socket rawSocket;
  
  private int refusedStreamCount;
  
  private final Route route;
  
  public int routeFailureCount;
  
  private I丨L sink;
  
  private Socket socket;
  
  private l丨Li1LL source;
  
  public int successCount;
  
  public final List<Reference<Transmitter>> transmitters = new ArrayList<Reference<Transmitter>>();
  
  public RealConnection(RealConnectionPool paramRealConnectionPool, Route paramRoute) {
    this.connectionPool = paramRealConnectionPool;
    this.route = paramRoute;
  }
  
  private void connectSocket(int paramInt1, int paramInt2, Call paramCall, EventListener paramEventListener) throws IOException {
    Socket socket;
    Proxy proxy = this.route.proxy();
    Address address = this.route.address();
    if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP) {
      socket = address.socketFactory().createSocket();
    } else {
      socket = new Socket(proxy);
    } 
    this.rawSocket = socket;
    paramEventListener.connectStart(paramCall, this.route.socketAddress(), proxy);
    this.rawSocket.setSoTimeout(paramInt2);
    try {
      Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), paramInt1);
      try {
        this.source = lIi丨I.I丨L(lIi丨I.lIi丨I(this.rawSocket));
        this.sink = lIi丨I.I1I(lIi丨I.L丨1丨1丨I(this.rawSocket));
      } catch (NullPointerException nullPointerException) {
        if ("throw with null exception".equals(nullPointerException.getMessage()))
          throw new IOException(nullPointerException); 
      } 
      return;
    } catch (ConnectException connectException1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to connect to ");
      stringBuilder.append(this.route.socketAddress());
      ConnectException connectException2 = new ConnectException(stringBuilder.toString());
      connectException2.initCause(connectException1);
      throw connectException2;
    } 
  }
  
  private void connectTls(ConnectionSpecSelector paramConnectionSpecSelector) throws IOException {
    Address address = this.route.address();
    SSLSocketFactory sSLSocketFactory = address.sslSocketFactory();
    Handshake handshake = null;
    ConnectionSpec connectionSpec = null;
    StringBuilder stringBuilder = null;
    try {
      SSLSocket sSLSocket = (SSLSocket)sSLSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
      try {
        String str;
        Protocol protocol;
        StringBuilder stringBuilder2;
        connectionSpec = paramConnectionSpecSelector.configureSecureSocket(sSLSocket);
        if (connectionSpec.supportsTlsExtensions())
          Platform.get().configureTlsExtensions(sSLSocket, address.url().host(), address.protocols()); 
        sSLSocket.startHandshake();
        SSLSession sSLSession = sSLSocket.getSession();
        handshake = Handshake.get(sSLSession);
        if (!address.hostnameVerifier().verify(address.url().host(), sSLSession)) {
          List<Certificate> list = handshake.peerCertificates();
          boolean bool = list.isEmpty();
          if (!bool) {
            X509Certificate x509Certificate = (X509Certificate)list.get(0);
            SSLPeerUnverifiedException sSLPeerUnverifiedException1 = new SSLPeerUnverifiedException();
            stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("Hostname ");
            stringBuilder.append(address.url().host());
            stringBuilder.append(" not verified:\n    certificate: ");
            stringBuilder.append(CertificatePinner.pin(x509Certificate));
            stringBuilder.append("\n    DN: ");
            stringBuilder.append(x509Certificate.getSubjectDN().getName());
            stringBuilder.append("\n    subjectAltNames: ");
            stringBuilder.append(OkHostnameVerifier.allSubjectAltNames(x509Certificate));
            this(stringBuilder.toString());
            throw sSLPeerUnverifiedException1;
          } 
          SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException();
          stringBuilder2 = new StringBuilder();
          this();
          stringBuilder2.append("Hostname ");
          stringBuilder2.append(address.url().host());
          stringBuilder2.append(" not verified (no certificates)");
          this(stringBuilder2.toString());
          throw sSLPeerUnverifiedException;
        } 
        address.certificatePinner().check(address.url().host(), stringBuilder2.peerCertificates());
        StringBuilder stringBuilder1 = stringBuilder;
        if (connectionSpec.supportsTlsExtensions())
          str = Platform.get().getSelectedProtocol(sSLSocket); 
        this.socket = sSLSocket;
        this.source = lIi丨I.I丨L(lIi丨I.lIi丨I(sSLSocket));
        this.sink = lIi丨I.I1I(lIi丨I.L丨1丨1丨I(this.socket));
        this.handshake = (Handshake)stringBuilder2;
        if (str != null) {
          protocol = Protocol.get(str);
        } else {
          protocol = Protocol.HTTP_1_1;
        } 
        this.protocol = protocol;
        return;
      } catch (AssertionError assertionError1) {
        SSLSocket sSLSocket1 = sSLSocket;
      } finally {
        AssertionError assertionError1;
        paramConnectionSpecSelector = null;
      } 
    } catch (AssertionError assertionError) {
      ConnectionSpec connectionSpec1 = connectionSpec;
    } finally {}
    ConnectionSpecSelector connectionSpecSelector = paramConnectionSpecSelector;
    if (Util.isAndroidGetsocknameError(assertionError)) {
      connectionSpecSelector = paramConnectionSpecSelector;
      IOException iOException = new IOException();
      connectionSpecSelector = paramConnectionSpecSelector;
      this(assertionError);
      connectionSpecSelector = paramConnectionSpecSelector;
      throw iOException;
    } 
    connectionSpecSelector = paramConnectionSpecSelector;
    throw assertionError;
  }
  
  private void connectTunnel(int paramInt1, int paramInt2, int paramInt3, Call paramCall, EventListener paramEventListener) throws IOException {
    Request request = createTunnelRequest();
    HttpUrl httpUrl = request.url();
    for (byte b = 0; b < 21; b++) {
      connectSocket(paramInt1, paramInt2, paramCall, paramEventListener);
      request = createTunnel(paramInt2, paramInt3, request, httpUrl);
      if (request == null)
        break; 
      Util.closeQuietly(this.rawSocket);
      this.rawSocket = null;
      this.sink = null;
      this.source = null;
      paramEventListener.connectEnd(paramCall, this.route.socketAddress(), this.route.proxy(), null);
    } 
  }
  
  private Request createTunnel(int paramInt1, int paramInt2, Request paramRequest, HttpUrl paramHttpUrl) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CONNECT ");
    stringBuilder.append(Util.hostHeader(paramHttpUrl, true));
    stringBuilder.append(" HTTP/1.1");
    String str = stringBuilder.toString();
    while (true) {
      Http1ExchangeCodec http1ExchangeCodec = new Http1ExchangeCodec(null, null, this.source, this.sink);
      I11L i11L = this.source.timeout();
      long l = paramInt1;
      TimeUnit timeUnit = TimeUnit.MILLISECONDS;
      i11L.timeout(l, timeUnit);
      this.sink.timeout().timeout(paramInt2, timeUnit);
      http1ExchangeCodec.writeRequest(paramRequest.headers(), str);
      http1ExchangeCodec.finishRequest();
      Response response = http1ExchangeCodec.readResponseHeaders(false).request(paramRequest).build();
      http1ExchangeCodec.skipConnectBody(response);
      int i = response.code();
      if (i != 200) {
        if (i == 407) {
          paramRequest = this.route.address().proxyAuthenticator().authenticate(this.route, response);
          if (paramRequest != null) {
            if ("close".equalsIgnoreCase(response.header("Connection")))
              return paramRequest; 
            continue;
          } 
          throw new IOException("Failed to authenticate with proxy");
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Unexpected response code for CONNECT: ");
        stringBuilder1.append(response.code());
        throw new IOException(stringBuilder1.toString());
      } 
      if (this.source.Ilil().l丨Li1LL() && this.sink.IL1Iii().l丨Li1LL())
        return null; 
      throw new IOException("TLS tunnel buffered too many bytes!");
    } 
  }
  
  private Request createTunnelRequest() throws IOException {
    Request request1 = (new Request.Builder()).url(this.route.address().url()).method("CONNECT", null).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    Response response = (new Response.Builder()).request(request1).protocol(Protocol.HTTP_1_1).code(407).message("Preemptive Authenticate").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(-1L).header("Proxy-Authenticate", "OkHttp-Preemptive").build();
    Request request2 = this.route.address().proxyAuthenticator().authenticate(this.route, response);
    if (request2 != null)
      request1 = request2; 
    return request1;
  }
  
  private void establishProtocol(ConnectionSpecSelector paramConnectionSpecSelector, int paramInt, Call paramCall, EventListener paramEventListener) throws IOException {
    Protocol protocol;
    List<Protocol> list;
    if (this.route.address().sslSocketFactory() == null) {
      list = this.route.address().protocols();
      protocol = Protocol.H2_PRIOR_KNOWLEDGE;
      if (list.contains(protocol)) {
        this.socket = this.rawSocket;
        this.protocol = protocol;
        startHttp2(paramInt);
        return;
      } 
      this.socket = this.rawSocket;
      this.protocol = Protocol.HTTP_1_1;
      return;
    } 
    paramEventListener.secureConnectStart((Call)list);
    connectTls((ConnectionSpecSelector)protocol);
    paramEventListener.secureConnectEnd((Call)list, this.handshake);
    if (this.protocol == Protocol.HTTP_2)
      startHttp2(paramInt); 
  }
  
  private boolean routeMatchesAny(List<Route> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      Route route = paramList.get(b);
      if (route.proxy().type() == Proxy.Type.DIRECT && this.route.proxy().type() == Proxy.Type.DIRECT && this.route.socketAddress().equals(route.socketAddress()))
        return true; 
    } 
    return false;
  }
  
  private void startHttp2(int paramInt) throws IOException {
    this.socket.setSoTimeout(0);
    Http2Connection http2Connection = (new Http2Connection.Builder(true)).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).pingIntervalMillis(paramInt).build();
    this.http2Connection = http2Connection;
    http2Connection.start();
  }
  
  public static RealConnection testConnection(RealConnectionPool paramRealConnectionPool, Route paramRoute, Socket paramSocket, long paramLong) {
    RealConnection realConnection = new RealConnection(paramRealConnectionPool, paramRoute);
    realConnection.socket = paramSocket;
    realConnection.idleAtNanos = paramLong;
    return realConnection;
  }
  
  public void cancel() {
    Util.closeQuietly(this.rawSocket);
  }
  
  public void connect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, Call paramCall, EventListener paramEventListener) {
    // Byte code:
    //   0: aload_0
    //   1: getfield protocol : Lokhttp3/Protocol;
    //   4: ifnonnull -> 497
    //   7: aload_0
    //   8: getfield route : Lokhttp3/Route;
    //   11: invokevirtual address : ()Lokhttp3/Address;
    //   14: invokevirtual connectionSpecs : ()Ljava/util/List;
    //   17: astore #8
    //   19: new okhttp3/internal/connection/ConnectionSpecSelector
    //   22: dup
    //   23: aload #8
    //   25: invokespecial <init> : (Ljava/util/List;)V
    //   28: astore #10
    //   30: aload_0
    //   31: getfield route : Lokhttp3/Route;
    //   34: invokevirtual address : ()Lokhttp3/Address;
    //   37: invokevirtual sslSocketFactory : ()Ljavax/net/ssl/SSLSocketFactory;
    //   40: ifnonnull -> 158
    //   43: aload #8
    //   45: getstatic okhttp3/ConnectionSpec.CLEARTEXT : Lokhttp3/ConnectionSpec;
    //   48: invokeinterface contains : (Ljava/lang/Object;)Z
    //   53: ifeq -> 140
    //   56: aload_0
    //   57: getfield route : Lokhttp3/Route;
    //   60: invokevirtual address : ()Lokhttp3/Address;
    //   63: invokevirtual url : ()Lokhttp3/HttpUrl;
    //   66: invokevirtual host : ()Ljava/lang/String;
    //   69: astore #8
    //   71: invokestatic get : ()Lokhttp3/internal/platform/Platform;
    //   74: aload #8
    //   76: invokevirtual isCleartextTrafficPermitted : (Ljava/lang/String;)Z
    //   79: ifeq -> 85
    //   82: goto -> 179
    //   85: new java/lang/StringBuilder
    //   88: dup
    //   89: invokespecial <init> : ()V
    //   92: astore #6
    //   94: aload #6
    //   96: ldc_w 'CLEARTEXT communication to '
    //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: aload #6
    //   105: aload #8
    //   107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload #6
    //   113: ldc_w ' not permitted by network security policy'
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: new okhttp3/internal/connection/RouteException
    //   123: dup
    //   124: new java/net/UnknownServiceException
    //   127: dup
    //   128: aload #6
    //   130: invokevirtual toString : ()Ljava/lang/String;
    //   133: invokespecial <init> : (Ljava/lang/String;)V
    //   136: invokespecial <init> : (Ljava/io/IOException;)V
    //   139: athrow
    //   140: new okhttp3/internal/connection/RouteException
    //   143: dup
    //   144: new java/net/UnknownServiceException
    //   147: dup
    //   148: ldc_w 'CLEARTEXT communication not enabled for client'
    //   151: invokespecial <init> : (Ljava/lang/String;)V
    //   154: invokespecial <init> : (Ljava/io/IOException;)V
    //   157: athrow
    //   158: aload_0
    //   159: getfield route : Lokhttp3/Route;
    //   162: invokevirtual address : ()Lokhttp3/Address;
    //   165: invokevirtual protocols : ()Ljava/util/List;
    //   168: getstatic okhttp3/Protocol.H2_PRIOR_KNOWLEDGE : Lokhttp3/Protocol;
    //   171: invokeinterface contains : (Ljava/lang/Object;)Z
    //   176: ifne -> 479
    //   179: aconst_null
    //   180: astore #9
    //   182: aload_0
    //   183: getfield route : Lokhttp3/Route;
    //   186: invokevirtual requiresTunnel : ()Z
    //   189: ifeq -> 220
    //   192: aload_0
    //   193: iload_1
    //   194: iload_2
    //   195: iload_3
    //   196: aload #6
    //   198: aload #7
    //   200: invokespecial connectTunnel : (IIILokhttp3/Call;Lokhttp3/EventListener;)V
    //   203: aload_0
    //   204: getfield rawSocket : Ljava/net/Socket;
    //   207: astore #8
    //   209: aload #8
    //   211: ifnonnull -> 217
    //   214: goto -> 267
    //   217: goto -> 230
    //   220: aload_0
    //   221: iload_1
    //   222: iload_2
    //   223: aload #6
    //   225: aload #7
    //   227: invokespecial connectSocket : (IILokhttp3/Call;Lokhttp3/EventListener;)V
    //   230: aload_0
    //   231: aload #10
    //   233: iload #4
    //   235: aload #6
    //   237: aload #7
    //   239: invokespecial establishProtocol : (Lokhttp3/internal/connection/ConnectionSpecSelector;ILokhttp3/Call;Lokhttp3/EventListener;)V
    //   242: aload #7
    //   244: aload #6
    //   246: aload_0
    //   247: getfield route : Lokhttp3/Route;
    //   250: invokevirtual socketAddress : ()Ljava/net/InetSocketAddress;
    //   253: aload_0
    //   254: getfield route : Lokhttp3/Route;
    //   257: invokevirtual proxy : ()Ljava/net/Proxy;
    //   260: aload_0
    //   261: getfield protocol : Lokhttp3/Protocol;
    //   264: invokevirtual connectEnd : (Lokhttp3/Call;Ljava/net/InetSocketAddress;Ljava/net/Proxy;Lokhttp3/Protocol;)V
    //   267: aload_0
    //   268: getfield route : Lokhttp3/Route;
    //   271: invokevirtual requiresTunnel : ()Z
    //   274: ifeq -> 305
    //   277: aload_0
    //   278: getfield rawSocket : Ljava/net/Socket;
    //   281: ifnull -> 287
    //   284: goto -> 305
    //   287: new okhttp3/internal/connection/RouteException
    //   290: dup
    //   291: new java/net/ProtocolException
    //   294: dup
    //   295: ldc_w 'Too many tunnel connections attempted: 21'
    //   298: invokespecial <init> : (Ljava/lang/String;)V
    //   301: invokespecial <init> : (Ljava/io/IOException;)V
    //   304: athrow
    //   305: aload_0
    //   306: getfield http2Connection : Lokhttp3/internal/http2/Http2Connection;
    //   309: ifnull -> 346
    //   312: aload_0
    //   313: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   316: astore #6
    //   318: aload #6
    //   320: monitorenter
    //   321: aload_0
    //   322: aload_0
    //   323: getfield http2Connection : Lokhttp3/internal/http2/Http2Connection;
    //   326: invokevirtual maxConcurrentStreams : ()I
    //   329: putfield allocationLimit : I
    //   332: aload #6
    //   334: monitorexit
    //   335: goto -> 346
    //   338: astore #7
    //   340: aload #6
    //   342: monitorexit
    //   343: aload #7
    //   345: athrow
    //   346: return
    //   347: astore #8
    //   349: goto -> 359
    //   352: astore #8
    //   354: goto -> 359
    //   357: astore #8
    //   359: aload_0
    //   360: getfield socket : Ljava/net/Socket;
    //   363: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   366: aload_0
    //   367: getfield rawSocket : Ljava/net/Socket;
    //   370: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   373: aload_0
    //   374: aconst_null
    //   375: putfield socket : Ljava/net/Socket;
    //   378: aload_0
    //   379: aconst_null
    //   380: putfield rawSocket : Ljava/net/Socket;
    //   383: aload_0
    //   384: aconst_null
    //   385: putfield source : LI丨L/l丨Li1LL;
    //   388: aload_0
    //   389: aconst_null
    //   390: putfield sink : LI丨L/I丨L;
    //   393: aload_0
    //   394: aconst_null
    //   395: putfield handshake : Lokhttp3/Handshake;
    //   398: aload_0
    //   399: aconst_null
    //   400: putfield protocol : Lokhttp3/Protocol;
    //   403: aload_0
    //   404: aconst_null
    //   405: putfield http2Connection : Lokhttp3/internal/http2/Http2Connection;
    //   408: aload #7
    //   410: aload #6
    //   412: aload_0
    //   413: getfield route : Lokhttp3/Route;
    //   416: invokevirtual socketAddress : ()Ljava/net/InetSocketAddress;
    //   419: aload_0
    //   420: getfield route : Lokhttp3/Route;
    //   423: invokevirtual proxy : ()Ljava/net/Proxy;
    //   426: aconst_null
    //   427: aload #8
    //   429: invokevirtual connectFailed : (Lokhttp3/Call;Ljava/net/InetSocketAddress;Ljava/net/Proxy;Lokhttp3/Protocol;Ljava/io/IOException;)V
    //   432: aload #9
    //   434: ifnonnull -> 451
    //   437: new okhttp3/internal/connection/RouteException
    //   440: dup
    //   441: aload #8
    //   443: invokespecial <init> : (Ljava/io/IOException;)V
    //   446: astore #9
    //   448: goto -> 458
    //   451: aload #9
    //   453: aload #8
    //   455: invokevirtual addConnectException : (Ljava/io/IOException;)V
    //   458: iload #5
    //   460: ifeq -> 476
    //   463: aload #10
    //   465: aload #8
    //   467: invokevirtual connectionFailed : (Ljava/io/IOException;)Z
    //   470: ifeq -> 476
    //   473: goto -> 182
    //   476: aload #9
    //   478: athrow
    //   479: new okhttp3/internal/connection/RouteException
    //   482: dup
    //   483: new java/net/UnknownServiceException
    //   486: dup
    //   487: ldc_w 'H2_PRIOR_KNOWLEDGE cannot be used with HTTPS'
    //   490: invokespecial <init> : (Ljava/lang/String;)V
    //   493: invokespecial <init> : (Ljava/io/IOException;)V
    //   496: athrow
    //   497: new java/lang/IllegalStateException
    //   500: dup
    //   501: ldc_w 'already connected'
    //   504: invokespecial <init> : (Ljava/lang/String;)V
    //   507: athrow
    // Exception table:
    //   from	to	target	type
    //   182	209	357	java/io/IOException
    //   220	230	352	java/io/IOException
    //   230	267	347	java/io/IOException
    //   321	335	338	finally
    //   340	343	338	finally
  }
  
  public Handshake handshake() {
    return this.handshake;
  }
  
  public boolean isEligible(Address paramAddress, @Nullable List<Route> paramList) {
    if (this.transmitters.size() < this.allocationLimit && !this.noNewExchanges) {
      if (!Internal.instance.equalsNonHost(this.route.address(), paramAddress))
        return false; 
      if (paramAddress.url().host().equals(route().address().url().host()))
        return true; 
      if (this.http2Connection == null)
        return false; 
      if (paramList != null && routeMatchesAny(paramList)) {
        if (paramAddress.hostnameVerifier() != OkHostnameVerifier.INSTANCE)
          return false; 
        if (!supportsUrl(paramAddress.url()))
          return false; 
        try {
          paramAddress.certificatePinner().check(paramAddress.url().host(), handshake().peerCertificates());
          return true;
        } catch (SSLPeerUnverifiedException sSLPeerUnverifiedException) {}
      } 
    } 
    return false;
  }
  
  public boolean isHealthy(boolean paramBoolean) {
    if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown())
      return false; 
    Http2Connection http2Connection = this.http2Connection;
    if (http2Connection != null)
      return http2Connection.isHealthy(System.nanoTime()); 
    if (paramBoolean)
      try {
        int i = this.socket.getSoTimeout();
        try {
          this.socket.setSoTimeout(1);
          paramBoolean = this.source.l丨Li1LL();
          if (paramBoolean)
            return false; 
          return true;
        } finally {
          this.socket.setSoTimeout(i);
        } 
      } catch (SocketTimeoutException socketTimeoutException) {
      
      } catch (IOException iOException) {
        return false;
      }  
    return true;
  }
  
  public boolean isMultiplexed() {
    boolean bool;
    if (this.http2Connection != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public ExchangeCodec newCodec(OkHttpClient paramOkHttpClient, Interceptor.Chain paramChain) throws SocketException {
    if (this.http2Connection != null)
      return new Http2ExchangeCodec(paramOkHttpClient, this, paramChain, this.http2Connection); 
    this.socket.setSoTimeout(paramChain.readTimeoutMillis());
    I11L i11L = this.source.timeout();
    long l = paramChain.readTimeoutMillis();
    TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    i11L.timeout(l, timeUnit);
    this.sink.timeout().timeout(paramChain.writeTimeoutMillis(), timeUnit);
    return new Http1ExchangeCodec(paramOkHttpClient, this, this.source, this.sink);
  }
  
  public RealWebSocket.Streams newWebSocketStreams(final Exchange exchange) throws SocketException {
    this.socket.setSoTimeout(0);
    noNewExchanges();
    return new RealWebSocket.Streams(true, this.source, this.sink) {
        public final RealConnection this$0;
        
        public final Exchange val$exchange;
        
        public void close() throws IOException {
          exchange.bodyComplete(-1L, true, true, null);
        }
      };
  }
  
  public void noNewExchanges() {
    synchronized (this.connectionPool) {
      this.noNewExchanges = true;
      return;
    } 
  }
  
  public void onSettings(Http2Connection paramHttp2Connection) {
    synchronized (this.connectionPool) {
      this.allocationLimit = paramHttp2Connection.maxConcurrentStreams();
      return;
    } 
  }
  
  public void onStream(Http2Stream paramHttp2Stream) throws IOException {
    paramHttp2Stream.close(ErrorCode.REFUSED_STREAM, null);
  }
  
  public Protocol protocol() {
    return this.protocol;
  }
  
  public Route route() {
    return this.route;
  }
  
  public Socket socket() {
    return this.socket;
  }
  
  public boolean supportsUrl(HttpUrl paramHttpUrl) {
    int j = paramHttpUrl.port();
    int i = this.route.address().url().port();
    boolean bool = false;
    if (j != i)
      return false; 
    if (!paramHttpUrl.host().equals(this.route.address().url().host())) {
      boolean bool1 = bool;
      if (this.handshake != null) {
        bool1 = bool;
        if (OkHostnameVerifier.INSTANCE.verify(paramHttpUrl.host(), (X509Certificate)this.handshake.peerCertificates().get(0)))
          bool1 = true; 
      } 
      return bool1;
    } 
    return true;
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Connection{");
    stringBuilder.append(this.route.address().url().host());
    stringBuilder.append(":");
    stringBuilder.append(this.route.address().url().port());
    stringBuilder.append(", proxy=");
    stringBuilder.append(this.route.proxy());
    stringBuilder.append(" hostAddress=");
    stringBuilder.append(this.route.socketAddress());
    stringBuilder.append(" cipherSuite=");
    Handshake handshake = this.handshake;
    if (handshake != null) {
      CipherSuite cipherSuite = handshake.cipherSuite();
    } else {
      str = "none";
    } 
    stringBuilder.append(str);
    stringBuilder.append(" protocol=");
    stringBuilder.append(this.protocol);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void trackFailure(@Nullable IOException paramIOException) {
    synchronized (this.connectionPool) {
      ErrorCode errorCode;
      if (paramIOException instanceof StreamResetException) {
        errorCode = ((StreamResetException)paramIOException).errorCode;
        if (errorCode == ErrorCode.REFUSED_STREAM) {
          int i = this.refusedStreamCount + 1;
          this.refusedStreamCount = i;
          if (i > 1) {
            this.noNewExchanges = true;
            this.routeFailureCount++;
          } 
        } else if (errorCode != ErrorCode.CANCEL) {
          this.noNewExchanges = true;
          this.routeFailureCount++;
        } 
      } else if (!isMultiplexed() || errorCode instanceof okhttp3.internal.http2.ConnectionShutdownException) {
        this.noNewExchanges = true;
        if (this.successCount == 0) {
          if (errorCode != null)
            this.connectionPool.connectFailed(this.route, (IOException)errorCode); 
          this.routeFailureCount++;
        } 
      } 
      return;
    } 
  }
}
