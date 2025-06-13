package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealConnectionPool;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.proxy.NullProxySelector;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public class OkHttpClient implements Cloneable, Call.Factory, WebSocket.Factory {
  public static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS;
  
  public static final List<Protocol> DEFAULT_PROTOCOLS = Util.immutableList(new Protocol[] { Protocol.HTTP_2, Protocol.HTTP_1_1 });
  
  public final Authenticator authenticator;
  
  @Nullable
  public final Cache cache;
  
  public final int callTimeout;
  
  public final CertificateChainCleaner certificateChainCleaner;
  
  public final CertificatePinner certificatePinner;
  
  public final int connectTimeout;
  
  public final ConnectionPool connectionPool;
  
  public final List<ConnectionSpec> connectionSpecs;
  
  public final CookieJar cookieJar;
  
  public final Dispatcher dispatcher;
  
  public final Dns dns;
  
  public final EventListener.Factory eventListenerFactory;
  
  public final boolean followRedirects;
  
  public final boolean followSslRedirects;
  
  public final HostnameVerifier hostnameVerifier;
  
  public final List<Interceptor> interceptors;
  
  @Nullable
  public final InternalCache internalCache;
  
  public final List<Interceptor> networkInterceptors;
  
  public final int pingInterval;
  
  public final List<Protocol> protocols;
  
  @Nullable
  public final Proxy proxy;
  
  public final Authenticator proxyAuthenticator;
  
  public final ProxySelector proxySelector;
  
  public final int readTimeout;
  
  public final boolean retryOnConnectionFailure;
  
  public final SocketFactory socketFactory;
  
  public final SSLSocketFactory sslSocketFactory;
  
  public final int writeTimeout;
  
  static {
    DEFAULT_CONNECTION_SPECS = Util.immutableList(new ConnectionSpec[] { ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT });
    Internal.instance = new Internal() {
        public void addLenient(Headers.Builder param1Builder, String param1String) {
          param1Builder.addLenient(param1String);
        }
        
        public void addLenient(Headers.Builder param1Builder, String param1String1, String param1String2) {
          param1Builder.addLenient(param1String1, param1String2);
        }
        
        public void apply(ConnectionSpec param1ConnectionSpec, SSLSocket param1SSLSocket, boolean param1Boolean) {
          param1ConnectionSpec.apply(param1SSLSocket, param1Boolean);
        }
        
        public int code(Response.Builder param1Builder) {
          return param1Builder.code;
        }
        
        public boolean equalsNonHost(Address param1Address1, Address param1Address2) {
          return param1Address1.equalsNonHost(param1Address2);
        }
        
        @Nullable
        public Exchange exchange(Response param1Response) {
          return param1Response.exchange;
        }
        
        public void initExchange(Response.Builder param1Builder, Exchange param1Exchange) {
          param1Builder.initExchange(param1Exchange);
        }
        
        public Call newWebSocketCall(OkHttpClient param1OkHttpClient, Request param1Request) {
          return RealCall.newRealCall(param1OkHttpClient, param1Request, true);
        }
        
        public RealConnectionPool realConnectionPool(ConnectionPool param1ConnectionPool) {
          return param1ConnectionPool.delegate;
        }
      };
  }
  
  public OkHttpClient() {
    this(new Builder());
  }
  
  public OkHttpClient(Builder paramBuilder) {
    this.dispatcher = paramBuilder.dispatcher;
    this.proxy = paramBuilder.proxy;
    this.protocols = paramBuilder.protocols;
    List<ConnectionSpec> list = paramBuilder.connectionSpecs;
    this.connectionSpecs = list;
    this.interceptors = Util.immutableList(paramBuilder.interceptors);
    this.networkInterceptors = Util.immutableList(paramBuilder.networkInterceptors);
    this.eventListenerFactory = paramBuilder.eventListenerFactory;
    this.proxySelector = paramBuilder.proxySelector;
    this.cookieJar = paramBuilder.cookieJar;
    this.cache = paramBuilder.cache;
    this.internalCache = paramBuilder.internalCache;
    this.socketFactory = paramBuilder.socketFactory;
    Iterator<ConnectionSpec> iterator = list.iterator();
    label26: while (true) {
      boolean bool = false;
      while (iterator.hasNext()) {
        ConnectionSpec connectionSpec = iterator.next();
        if (bool || connectionSpec.isTls()) {
          bool = true;
          continue;
        } 
        continue label26;
      } 
      SSLSocketFactory sSLSocketFactory = paramBuilder.sslSocketFactory;
      if (sSLSocketFactory != null || !bool) {
        this.sslSocketFactory = sSLSocketFactory;
        this.certificateChainCleaner = paramBuilder.certificateChainCleaner;
      } else {
        X509TrustManager x509TrustManager = Util.platformTrustManager();
        this.sslSocketFactory = newSslSocketFactory(x509TrustManager);
        this.certificateChainCleaner = CertificateChainCleaner.get(x509TrustManager);
      } 
      if (this.sslSocketFactory != null)
        Platform.get().configureSslSocketFactory(this.sslSocketFactory); 
      this.hostnameVerifier = paramBuilder.hostnameVerifier;
      this.certificatePinner = paramBuilder.certificatePinner.withCertificateChainCleaner(this.certificateChainCleaner);
      this.proxyAuthenticator = paramBuilder.proxyAuthenticator;
      this.authenticator = paramBuilder.authenticator;
      this.connectionPool = paramBuilder.connectionPool;
      this.dns = paramBuilder.dns;
      this.followSslRedirects = paramBuilder.followSslRedirects;
      this.followRedirects = paramBuilder.followRedirects;
      this.retryOnConnectionFailure = paramBuilder.retryOnConnectionFailure;
      this.callTimeout = paramBuilder.callTimeout;
      this.connectTimeout = paramBuilder.connectTimeout;
      this.readTimeout = paramBuilder.readTimeout;
      this.writeTimeout = paramBuilder.writeTimeout;
      this.pingInterval = paramBuilder.pingInterval;
      if (!this.interceptors.contains(null)) {
        if (!this.networkInterceptors.contains(null))
          return; 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Null network interceptor: ");
        stringBuilder1.append(this.networkInterceptors);
        throw new IllegalStateException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Null interceptor: ");
      stringBuilder.append(this.interceptors);
      throw new IllegalStateException(stringBuilder.toString());
    } 
  }
  
  private static SSLSocketFactory newSslSocketFactory(X509TrustManager paramX509TrustManager) {
    try {
      SSLContext sSLContext = Platform.get().getSSLContext();
      sSLContext.init(null, new TrustManager[] { paramX509TrustManager }, null);
      return sSLContext.getSocketFactory();
    } catch (GeneralSecurityException generalSecurityException) {
      throw new AssertionError("No System TLS", generalSecurityException);
    } 
  }
  
  public Authenticator authenticator() {
    return this.authenticator;
  }
  
  @Nullable
  public Cache cache() {
    return this.cache;
  }
  
  public int callTimeoutMillis() {
    return this.callTimeout;
  }
  
  public CertificatePinner certificatePinner() {
    return this.certificatePinner;
  }
  
  public int connectTimeoutMillis() {
    return this.connectTimeout;
  }
  
  public ConnectionPool connectionPool() {
    return this.connectionPool;
  }
  
  public List<ConnectionSpec> connectionSpecs() {
    return this.connectionSpecs;
  }
  
  public CookieJar cookieJar() {
    return this.cookieJar;
  }
  
  public Dispatcher dispatcher() {
    return this.dispatcher;
  }
  
  public Dns dns() {
    return this.dns;
  }
  
  public EventListener.Factory eventListenerFactory() {
    return this.eventListenerFactory;
  }
  
  public boolean followRedirects() {
    return this.followRedirects;
  }
  
  public boolean followSslRedirects() {
    return this.followSslRedirects;
  }
  
  public HostnameVerifier hostnameVerifier() {
    return this.hostnameVerifier;
  }
  
  public List<Interceptor> interceptors() {
    return this.interceptors;
  }
  
  @Nullable
  public InternalCache internalCache() {
    InternalCache internalCache;
    Cache cache = this.cache;
    if (cache != null) {
      internalCache = cache.internalCache;
    } else {
      internalCache = this.internalCache;
    } 
    return internalCache;
  }
  
  public List<Interceptor> networkInterceptors() {
    return this.networkInterceptors;
  }
  
  public Builder newBuilder() {
    return new Builder(this);
  }
  
  public Call newCall(Request paramRequest) {
    return RealCall.newRealCall(this, paramRequest, false);
  }
  
  public WebSocket newWebSocket(Request paramRequest, WebSocketListener paramWebSocketListener) {
    RealWebSocket realWebSocket = new RealWebSocket(paramRequest, paramWebSocketListener, new Random(), this.pingInterval);
    realWebSocket.connect(this);
    return realWebSocket;
  }
  
  public int pingIntervalMillis() {
    return this.pingInterval;
  }
  
  public List<Protocol> protocols() {
    return this.protocols;
  }
  
  @Nullable
  public Proxy proxy() {
    return this.proxy;
  }
  
  public Authenticator proxyAuthenticator() {
    return this.proxyAuthenticator;
  }
  
  public ProxySelector proxySelector() {
    return this.proxySelector;
  }
  
  public int readTimeoutMillis() {
    return this.readTimeout;
  }
  
  public boolean retryOnConnectionFailure() {
    return this.retryOnConnectionFailure;
  }
  
  public SocketFactory socketFactory() {
    return this.socketFactory;
  }
  
  public SSLSocketFactory sslSocketFactory() {
    return this.sslSocketFactory;
  }
  
  public int writeTimeoutMillis() {
    return this.writeTimeout;
  }
  
  public static final class Builder {
    public Authenticator authenticator;
    
    @Nullable
    public Cache cache;
    
    public int callTimeout;
    
    @Nullable
    public CertificateChainCleaner certificateChainCleaner;
    
    public CertificatePinner certificatePinner;
    
    public int connectTimeout;
    
    public ConnectionPool connectionPool;
    
    public List<ConnectionSpec> connectionSpecs;
    
    public CookieJar cookieJar;
    
    public Dispatcher dispatcher;
    
    public Dns dns;
    
    public EventListener.Factory eventListenerFactory;
    
    public boolean followRedirects;
    
    public boolean followSslRedirects;
    
    public HostnameVerifier hostnameVerifier;
    
    public final List<Interceptor> interceptors;
    
    @Nullable
    public InternalCache internalCache;
    
    public final List<Interceptor> networkInterceptors;
    
    public int pingInterval;
    
    public List<Protocol> protocols;
    
    @Nullable
    public Proxy proxy;
    
    public Authenticator proxyAuthenticator;
    
    public ProxySelector proxySelector;
    
    public int readTimeout;
    
    public boolean retryOnConnectionFailure;
    
    public SocketFactory socketFactory;
    
    @Nullable
    public SSLSocketFactory sslSocketFactory;
    
    public int writeTimeout;
    
    public Builder() {
      this.interceptors = new ArrayList<Interceptor>();
      this.networkInterceptors = new ArrayList<Interceptor>();
      this.dispatcher = new Dispatcher();
      this.protocols = OkHttpClient.DEFAULT_PROTOCOLS;
      this.connectionSpecs = OkHttpClient.DEFAULT_CONNECTION_SPECS;
      this.eventListenerFactory = EventListener.factory(EventListener.NONE);
      ProxySelector proxySelector = ProxySelector.getDefault();
      this.proxySelector = proxySelector;
      if (proxySelector == null)
        this.proxySelector = new NullProxySelector(); 
      this.cookieJar = CookieJar.NO_COOKIES;
      this.socketFactory = SocketFactory.getDefault();
      this.hostnameVerifier = OkHostnameVerifier.INSTANCE;
      this.certificatePinner = CertificatePinner.DEFAULT;
      Authenticator authenticator = Authenticator.NONE;
      this.proxyAuthenticator = authenticator;
      this.authenticator = authenticator;
      this.connectionPool = new ConnectionPool();
      this.dns = Dns.SYSTEM;
      this.followSslRedirects = true;
      this.followRedirects = true;
      this.retryOnConnectionFailure = true;
      this.callTimeout = 0;
      this.connectTimeout = 10000;
      this.readTimeout = 10000;
      this.writeTimeout = 10000;
      this.pingInterval = 0;
    }
    
    public Builder(OkHttpClient param1OkHttpClient) {
      ArrayList<Interceptor> arrayList2 = new ArrayList();
      this.interceptors = arrayList2;
      ArrayList<Interceptor> arrayList1 = new ArrayList();
      this.networkInterceptors = arrayList1;
      this.dispatcher = param1OkHttpClient.dispatcher;
      this.proxy = param1OkHttpClient.proxy;
      this.protocols = param1OkHttpClient.protocols;
      this.connectionSpecs = param1OkHttpClient.connectionSpecs;
      arrayList2.addAll(param1OkHttpClient.interceptors);
      arrayList1.addAll(param1OkHttpClient.networkInterceptors);
      this.eventListenerFactory = param1OkHttpClient.eventListenerFactory;
      this.proxySelector = param1OkHttpClient.proxySelector;
      this.cookieJar = param1OkHttpClient.cookieJar;
      this.internalCache = param1OkHttpClient.internalCache;
      this.cache = param1OkHttpClient.cache;
      this.socketFactory = param1OkHttpClient.socketFactory;
      this.sslSocketFactory = param1OkHttpClient.sslSocketFactory;
      this.certificateChainCleaner = param1OkHttpClient.certificateChainCleaner;
      this.hostnameVerifier = param1OkHttpClient.hostnameVerifier;
      this.certificatePinner = param1OkHttpClient.certificatePinner;
      this.proxyAuthenticator = param1OkHttpClient.proxyAuthenticator;
      this.authenticator = param1OkHttpClient.authenticator;
      this.connectionPool = param1OkHttpClient.connectionPool;
      this.dns = param1OkHttpClient.dns;
      this.followSslRedirects = param1OkHttpClient.followSslRedirects;
      this.followRedirects = param1OkHttpClient.followRedirects;
      this.retryOnConnectionFailure = param1OkHttpClient.retryOnConnectionFailure;
      this.callTimeout = param1OkHttpClient.callTimeout;
      this.connectTimeout = param1OkHttpClient.connectTimeout;
      this.readTimeout = param1OkHttpClient.readTimeout;
      this.writeTimeout = param1OkHttpClient.writeTimeout;
      this.pingInterval = param1OkHttpClient.pingInterval;
    }
    
    public Builder addInterceptor(Interceptor param1Interceptor) {
      if (param1Interceptor != null) {
        this.interceptors.add(param1Interceptor);
        return this;
      } 
      throw new IllegalArgumentException("interceptor == null");
    }
    
    public Builder addNetworkInterceptor(Interceptor param1Interceptor) {
      if (param1Interceptor != null) {
        this.networkInterceptors.add(param1Interceptor);
        return this;
      } 
      throw new IllegalArgumentException("interceptor == null");
    }
    
    public Builder authenticator(Authenticator param1Authenticator) {
      Objects.requireNonNull(param1Authenticator, "authenticator == null");
      this.authenticator = param1Authenticator;
      return this;
    }
    
    public OkHttpClient build() {
      return new OkHttpClient(this);
    }
    
    public Builder cache(@Nullable Cache param1Cache) {
      this.cache = param1Cache;
      this.internalCache = null;
      return this;
    }
    
    public Builder callTimeout(long param1Long, TimeUnit param1TimeUnit) {
      this.callTimeout = Util.checkDuration("timeout", param1Long, param1TimeUnit);
      return this;
    }
    
    @IgnoreJRERequirement
    public Builder callTimeout(Duration param1Duration) {
      this.callTimeout = Util.checkDuration("timeout", param1Duration.toMillis(), TimeUnit.MILLISECONDS);
      return this;
    }
    
    public Builder certificatePinner(CertificatePinner param1CertificatePinner) {
      Objects.requireNonNull(param1CertificatePinner, "certificatePinner == null");
      this.certificatePinner = param1CertificatePinner;
      return this;
    }
    
    public Builder connectTimeout(long param1Long, TimeUnit param1TimeUnit) {
      this.connectTimeout = Util.checkDuration("timeout", param1Long, param1TimeUnit);
      return this;
    }
    
    @IgnoreJRERequirement
    public Builder connectTimeout(Duration param1Duration) {
      this.connectTimeout = Util.checkDuration("timeout", param1Duration.toMillis(), TimeUnit.MILLISECONDS);
      return this;
    }
    
    public Builder connectionPool(ConnectionPool param1ConnectionPool) {
      Objects.requireNonNull(param1ConnectionPool, "connectionPool == null");
      this.connectionPool = param1ConnectionPool;
      return this;
    }
    
    public Builder connectionSpecs(List<ConnectionSpec> param1List) {
      this.connectionSpecs = Util.immutableList(param1List);
      return this;
    }
    
    public Builder cookieJar(CookieJar param1CookieJar) {
      Objects.requireNonNull(param1CookieJar, "cookieJar == null");
      this.cookieJar = param1CookieJar;
      return this;
    }
    
    public Builder dispatcher(Dispatcher param1Dispatcher) {
      if (param1Dispatcher != null) {
        this.dispatcher = param1Dispatcher;
        return this;
      } 
      throw new IllegalArgumentException("dispatcher == null");
    }
    
    public Builder dns(Dns param1Dns) {
      Objects.requireNonNull(param1Dns, "dns == null");
      this.dns = param1Dns;
      return this;
    }
    
    public Builder eventListener(EventListener param1EventListener) {
      Objects.requireNonNull(param1EventListener, "eventListener == null");
      this.eventListenerFactory = EventListener.factory(param1EventListener);
      return this;
    }
    
    public Builder eventListenerFactory(EventListener.Factory param1Factory) {
      Objects.requireNonNull(param1Factory, "eventListenerFactory == null");
      this.eventListenerFactory = param1Factory;
      return this;
    }
    
    public Builder followRedirects(boolean param1Boolean) {
      this.followRedirects = param1Boolean;
      return this;
    }
    
    public Builder followSslRedirects(boolean param1Boolean) {
      this.followSslRedirects = param1Boolean;
      return this;
    }
    
    public Builder hostnameVerifier(HostnameVerifier param1HostnameVerifier) {
      Objects.requireNonNull(param1HostnameVerifier, "hostnameVerifier == null");
      this.hostnameVerifier = param1HostnameVerifier;
      return this;
    }
    
    public List<Interceptor> interceptors() {
      return this.interceptors;
    }
    
    public List<Interceptor> networkInterceptors() {
      return this.networkInterceptors;
    }
    
    public Builder pingInterval(long param1Long, TimeUnit param1TimeUnit) {
      this.pingInterval = Util.checkDuration("interval", param1Long, param1TimeUnit);
      return this;
    }
    
    @IgnoreJRERequirement
    public Builder pingInterval(Duration param1Duration) {
      this.pingInterval = Util.checkDuration("timeout", param1Duration.toMillis(), TimeUnit.MILLISECONDS);
      return this;
    }
    
    public Builder protocols(List<Protocol> param1List) {
      param1List = new ArrayList<Protocol>(param1List);
      Protocol protocol = Protocol.H2_PRIOR_KNOWLEDGE;
      if (param1List.contains(protocol) || param1List.contains(Protocol.HTTP_1_1)) {
        if (!param1List.contains(protocol) || param1List.size() <= 1) {
          if (!param1List.contains(Protocol.HTTP_1_0)) {
            if (!param1List.contains(null)) {
              param1List.remove(Protocol.SPDY_3);
              this.protocols = Collections.unmodifiableList(param1List);
              return this;
            } 
            throw new IllegalArgumentException("protocols must not contain null");
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("protocols must not contain http/1.0: ");
          stringBuilder2.append(param1List);
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("protocols containing h2_prior_knowledge cannot use other protocols: ");
        stringBuilder1.append(param1List);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("protocols must contain h2_prior_knowledge or http/1.1: ");
      stringBuilder.append(param1List);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder proxy(@Nullable Proxy param1Proxy) {
      this.proxy = param1Proxy;
      return this;
    }
    
    public Builder proxyAuthenticator(Authenticator param1Authenticator) {
      Objects.requireNonNull(param1Authenticator, "proxyAuthenticator == null");
      this.proxyAuthenticator = param1Authenticator;
      return this;
    }
    
    public Builder proxySelector(ProxySelector param1ProxySelector) {
      Objects.requireNonNull(param1ProxySelector, "proxySelector == null");
      this.proxySelector = param1ProxySelector;
      return this;
    }
    
    public Builder readTimeout(long param1Long, TimeUnit param1TimeUnit) {
      this.readTimeout = Util.checkDuration("timeout", param1Long, param1TimeUnit);
      return this;
    }
    
    @IgnoreJRERequirement
    public Builder readTimeout(Duration param1Duration) {
      this.readTimeout = Util.checkDuration("timeout", param1Duration.toMillis(), TimeUnit.MILLISECONDS);
      return this;
    }
    
    public Builder retryOnConnectionFailure(boolean param1Boolean) {
      this.retryOnConnectionFailure = param1Boolean;
      return this;
    }
    
    public Builder socketFactory(SocketFactory param1SocketFactory) {
      Objects.requireNonNull(param1SocketFactory, "socketFactory == null");
      if (!(param1SocketFactory instanceof SSLSocketFactory)) {
        this.socketFactory = param1SocketFactory;
        return this;
      } 
      throw new IllegalArgumentException("socketFactory instanceof SSLSocketFactory");
    }
    
    public Builder sslSocketFactory(SSLSocketFactory param1SSLSocketFactory) {
      Objects.requireNonNull(param1SSLSocketFactory, "sslSocketFactory == null");
      this.sslSocketFactory = param1SSLSocketFactory;
      this.certificateChainCleaner = Platform.get().buildCertificateChainCleaner(param1SSLSocketFactory);
      return this;
    }
    
    public Builder sslSocketFactory(SSLSocketFactory param1SSLSocketFactory, X509TrustManager param1X509TrustManager) {
      Objects.requireNonNull(param1SSLSocketFactory, "sslSocketFactory == null");
      Objects.requireNonNull(param1X509TrustManager, "trustManager == null");
      this.sslSocketFactory = param1SSLSocketFactory;
      this.certificateChainCleaner = CertificateChainCleaner.get(param1X509TrustManager);
      return this;
    }
    
    public Builder writeTimeout(long param1Long, TimeUnit param1TimeUnit) {
      this.writeTimeout = Util.checkDuration("timeout", param1Long, param1TimeUnit);
      return this;
    }
    
    @IgnoreJRERequirement
    public Builder writeTimeout(Duration param1Duration) {
      this.writeTimeout = Util.checkDuration("timeout", param1Duration.toMillis(), TimeUnit.MILLISECONDS);
      return this;
    }
  }
}
