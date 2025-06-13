package okhttp3.internal.platform;

import android.annotation.SuppressLint;
import android.net.ssl.SSLSockets;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

@SuppressLint({"NewApi"})
public class Android10Platform extends AndroidPlatform {
  public Android10Platform(Class<?> paramClass) {
    super(paramClass, null, null, null, null, null);
  }
  
  @Nullable
  public static Platform buildIfSupported() {
    if (!Platform.isAndroid())
      return null; 
    try {
      if (AndroidPlatform.getSdkInt() >= 29)
        return new Android10Platform(Class.forName("com.android.org.conscrypt.SSLParametersImpl")); 
    } catch (ReflectiveOperationException reflectiveOperationException) {}
    return null;
  }
  
  private void enableSessionTickets(SSLSocket paramSSLSocket) {
    if (SSLSockets.isSupportedSocket(paramSSLSocket))
      SSLSockets.setUseSessionTickets(paramSSLSocket, true); 
  }
  
  @SuppressLint({"NewApi"})
  @IgnoreJRERequirement
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) throws IOException {
    try {
      enableSessionTickets(paramSSLSocket);
      SSLParameters sSLParameters = paramSSLSocket.getSSLParameters();
      sSLParameters.setApplicationProtocols(Platform.alpnProtocolNames(paramList).<String>toArray(new String[0]));
      paramSSLSocket.setSSLParameters(sSLParameters);
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new IOException("Android internal error", illegalArgumentException);
    } 
  }
  
  @Nullable
  @IgnoreJRERequirement
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    String str = paramSSLSocket.getApplicationProtocol();
    return (str == null || str.isEmpty()) ? null : str;
  }
}
