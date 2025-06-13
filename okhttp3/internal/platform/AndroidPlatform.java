package okhttp3.internal.platform;

import android.os.Build;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;

public class AndroidPlatform extends Platform {
  private static final int MAX_LOG_LENGTH = 4000;
  
  private final CloseGuard closeGuard = CloseGuard.get();
  
  private final Method getAlpnSelectedProtocol;
  
  private final Method setAlpnProtocols;
  
  private final Method setHostname;
  
  private final Method setUseSessionTickets;
  
  private final Class<?> sslParametersClass;
  
  private final Class<?> sslSocketClass;
  
  public AndroidPlatform(Class<?> paramClass1, Class<?> paramClass2, Method paramMethod1, Method paramMethod2, Method paramMethod3, Method paramMethod4) {
    this.sslParametersClass = paramClass1;
    this.sslSocketClass = paramClass2;
    this.setUseSessionTickets = paramMethod1;
    this.setHostname = paramMethod2;
    this.getAlpnSelectedProtocol = paramMethod3;
    this.setAlpnProtocols = paramMethod4;
  }
  
  private boolean api23IsCleartextTrafficPermitted(String paramString, Class<?> paramClass, Object paramObject) throws InvocationTargetException, IllegalAccessException {
    try {
      return ((Boolean)paramClass.getMethod("isCleartextTrafficPermitted", new Class[0]).invoke(paramObject, new Object[0])).booleanValue();
    } catch (NoSuchMethodException noSuchMethodException) {
      return super.isCleartextTrafficPermitted(paramString);
    } 
  }
  
  private boolean api24IsCleartextTrafficPermitted(String paramString, Class<?> paramClass, Object paramObject) throws InvocationTargetException, IllegalAccessException {
    try {
      return ((Boolean)paramClass.getMethod("isCleartextTrafficPermitted", new Class[] { String.class }).invoke(paramObject, new Object[] { paramString })).booleanValue();
    } catch (NoSuchMethodException noSuchMethodException) {
      return api23IsCleartextTrafficPermitted(paramString, paramClass, paramObject);
    } 
  }
  
  @Nullable
  public static Platform buildIfSupported() {
    if (!Platform.isAndroid())
      return null; 
    try {
      Class<?> clazz2 = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
      Class<?> clazz1 = Class.forName("com.android.org.conscrypt.OpenSSLSocketImpl");
      if (Build.VERSION.SDK_INT >= 21)
        try {
          return new AndroidPlatform(clazz2, clazz1, clazz1.getDeclaredMethod("setUseSessionTickets", new Class[] { boolean.class }), clazz1.getMethod("setHostname", new Class[] { String.class }), clazz1.getMethod("getAlpnSelectedProtocol", new Class[0]), clazz1.getMethod("setAlpnProtocols", new Class[] { byte[].class }));
        } catch (NoSuchMethodException noSuchMethodException) {} 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Expected Android API level 21+ but was ");
      stringBuilder.append(Build.VERSION.SDK_INT);
      throw new IllegalStateException(stringBuilder.toString());
    } catch (ClassNotFoundException classNotFoundException) {
      return null;
    } 
  }
  
  public static int getSdkInt() {
    try {
      return Build.VERSION.SDK_INT;
    } catch (NoClassDefFoundError noClassDefFoundError) {
      return 0;
    } 
  }
  
  public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager paramX509TrustManager) {
    try {
      Class<?> clazz = Class.forName("android.net.http.X509TrustManagerExtensions");
      return new AndroidCertificateChainCleaner(clazz.getConstructor(new Class[] { X509TrustManager.class }, ).newInstance(new Object[] { paramX509TrustManager }, ), clazz.getMethod("checkServerTrusted", new Class[] { X509Certificate[].class, String.class, String.class }));
    } catch (Exception exception) {
      return super.buildCertificateChainCleaner(paramX509TrustManager);
    } 
  }
  
  public TrustRootIndex buildTrustRootIndex(X509TrustManager paramX509TrustManager) {
    try {
      Method method = paramX509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", new Class[] { X509Certificate.class });
      method.setAccessible(true);
      return new CustomTrustRootIndex(paramX509TrustManager, method);
    } catch (NoSuchMethodException noSuchMethodException) {
      return super.buildTrustRootIndex(paramX509TrustManager);
    } 
  }
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) throws IOException {
    if (!this.sslSocketClass.isInstance(paramSSLSocket))
      return; 
    if (paramString != null) {
      try {
        this.setUseSessionTickets.invoke(paramSSLSocket, new Object[] { Boolean.TRUE });
        this.setHostname.invoke(paramSSLSocket, new Object[] { paramString });
        this.setAlpnProtocols.invoke(paramSSLSocket, new Object[] { Platform.concatLengthPrefixed(paramList) });
        return;
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {}
      throw new AssertionError(invocationTargetException);
    } 
    this.setAlpnProtocols.invoke(invocationTargetException, new Object[] { Platform.concatLengthPrefixed(paramList) });
  }
  
  public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
    try {
      paramSocket.connect(paramInetSocketAddress, paramInt);
      return;
    } catch (AssertionError assertionError) {
      if (Util.isAndroidGetsocknameError(assertionError))
        throw new IOException(assertionError); 
      throw assertionError;
    } catch (ClassCastException classCastException) {
      if (Build.VERSION.SDK_INT == 26)
        throw new IOException("Exception in connect", classCastException); 
      throw classCastException;
    } 
  }
  
  public SSLContext getSSLContext() {
    boolean bool = true;
    try {
      int i = Build.VERSION.SDK_INT;
      if (i < 16 || i >= 22)
        bool = false; 
    } catch (NoClassDefFoundError noClassDefFoundError) {}
    if (bool)
      try {
        return SSLContext.getInstance("TLSv1.2");
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {} 
    try {
      return SSLContext.getInstance("TLS");
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new IllegalStateException("No TLS provider", noSuchAlgorithmException);
    } 
  }
  
  @Nullable
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    boolean bool = this.sslSocketClass.isInstance(paramSSLSocket);
    SSLSocket sSLSocket = null;
    if (!bool)
      return null; 
    try {
      String str;
      byte[] arrayOfByte = (byte[])this.getAlpnSelectedProtocol.invoke(paramSSLSocket, new Object[0]);
      paramSSLSocket = sSLSocket;
      if (arrayOfByte != null)
        str = new String(arrayOfByte, StandardCharsets.UTF_8); 
      return str;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    throw new AssertionError(invocationTargetException);
  }
  
  @Nullable
  public Object getStackTraceForCloseable(String paramString) {
    return this.closeGuard.createAndOpen(paramString);
  }
  
  public boolean isCleartextTrafficPermitted(String paramString) {
    try {
      Class<?> clazz = Class.forName("android.security.NetworkSecurityPolicy");
      return api24IsCleartextTrafficPermitted(paramString, clazz, clazz.getMethod("getInstance", new Class[0]).invoke((Object)null, new Object[0]));
    } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {
      return super.isCleartextTrafficPermitted(paramString);
    } catch (IllegalAccessException illegalAccessException) {
      throw new AssertionError("unable to determine cleartext support", illegalAccessException);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new AssertionError("unable to determine cleartext support", illegalArgumentException);
    } catch (InvocationTargetException invocationTargetException) {
      throw new AssertionError("unable to determine cleartext support", invocationTargetException);
    } 
  }
  
  public void log(int paramInt, String paramString, @Nullable Throwable paramThrowable) {
    byte b = 5;
    if (paramInt != 5)
      b = 3; 
    String str = paramString;
    if (paramThrowable != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append('\n');
      stringBuilder.append(Log.getStackTraceString(paramThrowable));
      str = stringBuilder.toString();
    } 
    paramInt = 0;
    int i = str.length();
    label23: while (paramInt < i) {
      int j = str.indexOf('\n', paramInt);
      if (j == -1)
        j = i; 
      while (true) {
        int k = Math.min(j, paramInt + 4000);
        Log.println(b, "OkHttp", str.substring(paramInt, k));
        if (k >= j) {
          paramInt = k + 1;
          continue label23;
        } 
        paramInt = k;
      } 
    } 
  }
  
  public void logCloseableLeak(String paramString, Object paramObject) {
    if (!this.closeGuard.warnIfOpen(paramObject))
      log(5, paramString, null); 
  }
  
  @Nullable
  public X509TrustManager trustManager(SSLSocketFactory paramSSLSocketFactory) {
    Object object = Platform.readFieldOrNull(paramSSLSocketFactory, (Class)this.sslParametersClass, "sslParameters");
    classNotFoundException = (ClassNotFoundException)object;
    if (object == null)
      try {
        classNotFoundException = Platform.readFieldOrNull(paramSSLSocketFactory, (Class)Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, paramSSLSocketFactory.getClass().getClassLoader()), "sslParameters");
      } catch (ClassNotFoundException classNotFoundException) {
        return super.trustManager(paramSSLSocketFactory);
      }  
    X509TrustManager x509TrustManager = Platform.<X509TrustManager>readFieldOrNull(classNotFoundException, X509TrustManager.class, "x509TrustManager");
    return (x509TrustManager != null) ? x509TrustManager : Platform.<X509TrustManager>readFieldOrNull(classNotFoundException, X509TrustManager.class, "trustManager");
  }
  
  public static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
    private final Method checkServerTrusted;
    
    private final Object x509TrustManagerExtensions;
    
    public AndroidCertificateChainCleaner(Object param1Object, Method param1Method) {
      this.x509TrustManagerExtensions = param1Object;
      this.checkServerTrusted = param1Method;
    }
    
    public List<Certificate> clean(List<Certificate> param1List, String param1String) throws SSLPeerUnverifiedException {
      try {
        X509Certificate[] arrayOfX509Certificate = param1List.<X509Certificate>toArray(new X509Certificate[param1List.size()]);
        return (List)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, new Object[] { arrayOfX509Certificate, "RSA", param1String });
      } catch (InvocationTargetException invocationTargetException) {
        SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(invocationTargetException.getMessage());
        sSLPeerUnverifiedException.initCause(invocationTargetException);
        throw sSLPeerUnverifiedException;
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError(illegalAccessException);
      } 
    }
    
    public boolean equals(Object param1Object) {
      return param1Object instanceof AndroidCertificateChainCleaner;
    }
    
    public int hashCode() {
      return 0;
    }
  }
  
  public static final class CloseGuard {
    private final Method getMethod;
    
    private final Method openMethod;
    
    private final Method warnIfOpenMethod;
    
    public CloseGuard(Method param1Method1, Method param1Method2, Method param1Method3) {
      this.getMethod = param1Method1;
      this.openMethod = param1Method2;
      this.warnIfOpenMethod = param1Method3;
    }
    
    public static CloseGuard get() {
      Method method1;
      Method method2 = null;
      try {
        Class<?> clazz = Class.forName("dalvik.system.CloseGuard");
        Method method4 = clazz.getMethod("get", new Class[0]);
        method1 = clazz.getMethod("open", new Class[] { String.class });
        Method method3 = clazz.getMethod("warnIfOpen", new Class[0]);
        method2 = method4;
      } catch (Exception exception) {
        exception = null;
        method1 = null;
      } 
      return new CloseGuard(method2, method1, (Method)exception);
    }
    
    public Object createAndOpen(String param1String) {
      Method method = this.getMethod;
      if (method != null)
        try {
          Object object = method.invoke((Object)null, new Object[0]);
          this.openMethod.invoke(object, new Object[] { param1String });
          return object;
        } catch (Exception exception) {} 
      return null;
    }
    
    public boolean warnIfOpen(Object param1Object) {
      boolean bool2 = false;
      boolean bool1 = bool2;
      if (param1Object != null)
        try {
          this.warnIfOpenMethod.invoke(param1Object, new Object[0]);
          bool1 = true;
        } catch (Exception exception) {
          bool1 = bool2;
        }  
      return bool1;
    }
  }
  
  public static final class CustomTrustRootIndex implements TrustRootIndex {
    private final Method findByIssuerAndSignatureMethod;
    
    private final X509TrustManager trustManager;
    
    public CustomTrustRootIndex(X509TrustManager param1X509TrustManager, Method param1Method) {
      this.findByIssuerAndSignatureMethod = param1Method;
      this.trustManager = param1X509TrustManager;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof CustomTrustRootIndex))
        return false; 
      param1Object = param1Object;
      if (!this.trustManager.equals(((CustomTrustRootIndex)param1Object).trustManager) || !this.findByIssuerAndSignatureMethod.equals(((CustomTrustRootIndex)param1Object).findByIssuerAndSignatureMethod))
        bool = false; 
      return bool;
    }
    
    public X509Certificate findByIssuerAndSignature(X509Certificate param1X509Certificate) {
      X509Certificate x509Certificate1;
      X509Certificate x509Certificate2 = null;
      try {
        TrustAnchor trustAnchor = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke(this.trustManager, new Object[] { param1X509Certificate });
        param1X509Certificate = x509Certificate2;
        if (trustAnchor != null)
          param1X509Certificate = trustAnchor.getTrustedCert(); 
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError("unable to get issues and signature", illegalAccessException);
      } catch (InvocationTargetException invocationTargetException) {
        x509Certificate1 = x509Certificate2;
      } 
      return x509Certificate1;
    }
    
    public int hashCode() {
      return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
    }
  }
}
