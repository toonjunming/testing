package okhttp3.internal.platform;

import I丨L.I1I;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import okhttp3.internal.tls.BasicTrustRootIndex;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;

public class Platform {
  public static final int INFO = 4;
  
  private static final Platform PLATFORM = findPlatform();
  
  public static final int WARN = 5;
  
  private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());
  
  public static List<String> alpnProtocolNames(List<Protocol> paramList) {
    ArrayList<String> arrayList = new ArrayList(paramList.size());
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      Protocol protocol = paramList.get(b);
      if (protocol != Protocol.HTTP_1_0)
        arrayList.add(protocol.toString()); 
    } 
    return arrayList;
  }
  
  public static byte[] concatLengthPrefixed(List<Protocol> paramList) {
    I1I i1I = new I1I();
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      Protocol protocol = paramList.get(b);
      if (protocol != Protocol.HTTP_1_0) {
        i1I.iI(protocol.toString().length());
        i1I.lL(protocol.toString());
      } 
    } 
    return i1I.I丨L();
  }
  
  private static Platform findAndroidPlatform() {
    Platform platform = Android10Platform.buildIfSupported();
    if (platform != null)
      return platform; 
    platform = AndroidPlatform.buildIfSupported();
    Objects.requireNonNull(platform, "No platform found on Android");
    return platform;
  }
  
  private static Platform findJvmPlatform() {
    if (isConscryptPreferred()) {
      ConscryptPlatform conscryptPlatform = ConscryptPlatform.buildIfSupported();
      if (conscryptPlatform != null)
        return conscryptPlatform; 
    } 
    Jdk9Platform jdk9Platform = Jdk9Platform.buildIfSupported();
    if (jdk9Platform != null)
      return jdk9Platform; 
    Platform platform = Jdk8WithJettyBootPlatform.buildIfSupported();
    return (platform != null) ? platform : new Platform();
  }
  
  private static Platform findPlatform() {
    return isAndroid() ? findAndroidPlatform() : findJvmPlatform();
  }
  
  public static Platform get() {
    return PLATFORM;
  }
  
  public static boolean isAndroid() {
    return "Dalvik".equals(System.getProperty("java.vm.name"));
  }
  
  public static boolean isConscryptPreferred() {
    return "conscrypt".equals(Util.getSystemProperty("okhttp.platform", null)) ? true : "Conscrypt".equals(Security.getProviders()[0].getName());
  }
  
  @Nullable
  public static <T> T readFieldOrNull(Object paramObject, Class<T> paramClass, String paramString) {
    Class<?> clazz = paramObject.getClass();
    while (clazz != Object.class) {
      try {
        Field field = clazz.getDeclaredField(paramString);
        field.setAccessible(true);
        null = field.get(paramObject);
        return !paramClass.isInstance(null) ? null : paramClass.cast(null);
      } catch (NoSuchFieldException noSuchFieldException) {
        clazz = clazz.getSuperclass();
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError();
      } 
    } 
    if (!paramString.equals("delegate")) {
      illegalAccessException = readFieldOrNull(illegalAccessException, Object.class, "delegate");
      if (illegalAccessException != null)
        return readFieldOrNull(illegalAccessException, paramClass, paramString); 
    } 
    return null;
  }
  
  public void afterHandshake(SSLSocket paramSSLSocket) {}
  
  public CertificateChainCleaner buildCertificateChainCleaner(SSLSocketFactory paramSSLSocketFactory) {
    X509TrustManager x509TrustManager = trustManager(paramSSLSocketFactory);
    if (x509TrustManager != null)
      return buildCertificateChainCleaner(x509TrustManager); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to extract the trust manager on ");
    stringBuilder.append(get());
    stringBuilder.append(", sslSocketFactory is ");
    stringBuilder.append(paramSSLSocketFactory.getClass());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager paramX509TrustManager) {
    return new BasicCertificateChainCleaner(buildTrustRootIndex(paramX509TrustManager));
  }
  
  public TrustRootIndex buildTrustRootIndex(X509TrustManager paramX509TrustManager) {
    return new BasicTrustRootIndex(paramX509TrustManager.getAcceptedIssuers());
  }
  
  public void configureSslSocketFactory(SSLSocketFactory paramSSLSocketFactory) {}
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, @Nullable String paramString, List<Protocol> paramList) throws IOException {}
  
  public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
    paramSocket.connect(paramInetSocketAddress, paramInt);
  }
  
  public String getPrefix() {
    return "OkHttp";
  }
  
  public SSLContext getSSLContext() {
    try {
      return SSLContext.getInstance("TLS");
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new IllegalStateException("No TLS provider", noSuchAlgorithmException);
    } 
  }
  
  @Nullable
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    return null;
  }
  
  @Nullable
  public Object getStackTraceForCloseable(String paramString) {
    return logger.isLoggable(Level.FINE) ? new Throwable(paramString) : null;
  }
  
  public boolean isCleartextTrafficPermitted(String paramString) {
    return true;
  }
  
  public void log(int paramInt, String paramString, @Nullable Throwable paramThrowable) {
    Level level;
    if (paramInt == 5) {
      level = Level.WARNING;
    } else {
      level = Level.INFO;
    } 
    logger.log(level, paramString, paramThrowable);
  }
  
  public void logCloseableLeak(String paramString, Object paramObject) {
    String str = paramString;
    if (paramObject == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(" To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);");
      str = stringBuilder.toString();
    } 
    log(5, str, (Throwable)paramObject);
  }
  
  public String toString() {
    return getClass().getSimpleName();
  }
  
  @Nullable
  public X509TrustManager trustManager(SSLSocketFactory paramSSLSocketFactory) {
    try {
      paramSSLSocketFactory = readFieldOrNull(paramSSLSocketFactory, Class.forName("sun.security.ssl.SSLContextImpl"), "context");
      return (paramSSLSocketFactory == null) ? null : readFieldOrNull(paramSSLSocketFactory, X509TrustManager.class, "trustManager");
    } catch (ClassNotFoundException classNotFoundException) {
      return null;
    } 
  }
}
