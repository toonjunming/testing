package okhttp3.internal.connection;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.ConnectionSpec;
import okhttp3.internal.Internal;

public final class ConnectionSpecSelector {
  private final List<ConnectionSpec> connectionSpecs;
  
  private boolean isFallback;
  
  private boolean isFallbackPossible;
  
  private int nextModeIndex = 0;
  
  public ConnectionSpecSelector(List<ConnectionSpec> paramList) {
    this.connectionSpecs = paramList;
  }
  
  private boolean isFallbackPossible(SSLSocket paramSSLSocket) {
    for (int i = this.nextModeIndex; i < this.connectionSpecs.size(); i++) {
      if (((ConnectionSpec)this.connectionSpecs.get(i)).isCompatible(paramSSLSocket))
        return true; 
    } 
    return false;
  }
  
  public ConnectionSpec configureSecureSocket(SSLSocket paramSSLSocket) throws IOException {
    ConnectionSpec connectionSpec;
    int i = this.nextModeIndex;
    int j = this.connectionSpecs.size();
    while (true) {
      if (i < j) {
        ConnectionSpec connectionSpec1 = this.connectionSpecs.get(i);
        if (connectionSpec1.isCompatible(paramSSLSocket)) {
          this.nextModeIndex = i + 1;
          break;
        } 
        i++;
        continue;
      } 
      connectionSpec = null;
      break;
    } 
    if (connectionSpec != null) {
      this.isFallbackPossible = isFallbackPossible(paramSSLSocket);
      Internal.instance.apply(connectionSpec, paramSSLSocket, this.isFallback);
      return connectionSpec;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to find acceptable protocols. isFallback=");
    stringBuilder.append(this.isFallback);
    stringBuilder.append(", modes=");
    stringBuilder.append(this.connectionSpecs);
    stringBuilder.append(", supported protocols=");
    stringBuilder.append(Arrays.toString((Object[])paramSSLSocket.getEnabledProtocols()));
    throw new UnknownServiceException(stringBuilder.toString());
  }
  
  public boolean connectionFailed(IOException paramIOException) {
    this.isFallback = true;
    return !this.isFallbackPossible ? false : ((paramIOException instanceof java.net.ProtocolException) ? false : ((paramIOException instanceof java.io.InterruptedIOException) ? false : ((paramIOException instanceof javax.net.ssl.SSLHandshakeException && paramIOException.getCause() instanceof java.security.cert.CertificateException) ? false : ((paramIOException instanceof javax.net.ssl.SSLPeerUnverifiedException) ? false : (paramIOException instanceof javax.net.ssl.SSLException)))));
  }
}
