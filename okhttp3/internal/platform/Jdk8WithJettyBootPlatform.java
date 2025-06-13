package okhttp3.internal.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;
import okhttp3.internal.Util;

public class Jdk8WithJettyBootPlatform extends Platform {
  private final Class<?> clientProviderClass;
  
  private final Method getMethod;
  
  private final Method putMethod;
  
  private final Method removeMethod;
  
  private final Class<?> serverProviderClass;
  
  public Jdk8WithJettyBootPlatform(Method paramMethod1, Method paramMethod2, Method paramMethod3, Class<?> paramClass1, Class<?> paramClass2) {
    this.putMethod = paramMethod1;
    this.getMethod = paramMethod2;
    this.removeMethod = paramMethod3;
    this.clientProviderClass = paramClass1;
    this.serverProviderClass = paramClass2;
  }
  
  public static Platform buildIfSupported() {
    try {
      Class<?> clazz1 = Class.forName("org.eclipse.jetty.alpn.ALPN", true, null);
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      stringBuilder1.append("org.eclipse.jetty.alpn.ALPN");
      stringBuilder1.append("$Provider");
      Class<?> clazz2 = Class.forName(stringBuilder1.toString(), true, null);
      StringBuilder stringBuilder2 = new StringBuilder();
      this();
      stringBuilder2.append("org.eclipse.jetty.alpn.ALPN");
      stringBuilder2.append("$ClientProvider");
      Class<?> clazz3 = Class.forName(stringBuilder2.toString(), true, null);
      StringBuilder stringBuilder3 = new StringBuilder();
      this();
      stringBuilder3.append("org.eclipse.jetty.alpn.ALPN");
      stringBuilder3.append("$ServerProvider");
      Class<?> clazz4 = Class.forName(stringBuilder3.toString(), true, null);
      return new Jdk8WithJettyBootPlatform(clazz1.getMethod("put", new Class[] { SSLSocket.class, clazz2 }), clazz1.getMethod("get", new Class[] { SSLSocket.class }), clazz1.getMethod("remove", new Class[] { SSLSocket.class }), clazz3, clazz4);
    } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {
      return null;
    } 
  }
  
  public void afterHandshake(SSLSocket paramSSLSocket) {
    try {
      this.removeMethod.invoke(null, new Object[] { paramSSLSocket });
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    throw new AssertionError("failed to remove ALPN", invocationTargetException);
  }
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) {
    List<String> list = Platform.alpnProtocolNames(paramList);
    try {
      ClassLoader classLoader = Platform.class.getClassLoader();
      Class<?> clazz1 = this.clientProviderClass;
      Class<?> clazz2 = this.serverProviderClass;
      AlpnProvider alpnProvider = new AlpnProvider();
      this(list);
      Object object = Proxy.newProxyInstance(classLoader, new Class[] { clazz1, clazz2 }, alpnProvider);
      this.putMethod.invoke(null, new Object[] { paramSSLSocket, object });
      return;
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
    throw new AssertionError("failed to set ALPN", illegalAccessException);
  }
  
  @Nullable
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    try {
      String str;
      Method method = this.getMethod;
      AlpnProvider alpnProvider2 = null;
      AlpnProvider alpnProvider1 = (AlpnProvider)Proxy.getInvocationHandler(method.invoke(null, new Object[] { paramSSLSocket }));
      boolean bool = alpnProvider1.unsupported;
      if (!bool && alpnProvider1.selected == null) {
        Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", null);
        return null;
      } 
      if (bool) {
        alpnProvider1 = alpnProvider2;
      } else {
        str = alpnProvider1.selected;
      } 
      return str;
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
    throw new AssertionError("failed to get ALPN selected protocol", illegalAccessException);
  }
  
  public static class AlpnProvider implements InvocationHandler {
    private final List<String> protocols;
    
    public String selected;
    
    public boolean unsupported;
    
    public AlpnProvider(List<String> param1List) {
      this.protocols = param1List;
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      List<String> list;
      String str = param1Method.getName();
      Class<?> clazz = param1Method.getReturnType();
      param1Object = param1ArrayOfObject;
      if (param1ArrayOfObject == null)
        param1Object = Util.EMPTY_STRING_ARRAY; 
      if (str.equals("supports") && boolean.class == clazz)
        return Boolean.TRUE; 
      if (str.equals("unsupported") && void.class == clazz) {
        this.unsupported = true;
        return null;
      } 
      if (str.equals("protocols") && param1Object.length == 0)
        return this.protocols; 
      if ((str.equals("selectProtocol") || str.equals("select")) && String.class == clazz && param1Object.length == 1 && param1Object[0] instanceof List) {
        list = (List)param1Object[0];
        int i = list.size();
        for (byte b = 0; b < i; b++) {
          param1Object = list.get(b);
          if (this.protocols.contains(param1Object)) {
            this.selected = (String)param1Object;
            return param1Object;
          } 
        } 
        param1Object = this.protocols.get(0);
        this.selected = (String)param1Object;
        return param1Object;
      } 
      if ((str.equals("protocolSelected") || str.equals("selected")) && param1Object.length == 1) {
        this.selected = (String)param1Object[0];
        return null;
      } 
      return list.invoke(this, (Object[])param1Object);
    }
  }
}
