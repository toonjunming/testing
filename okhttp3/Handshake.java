package okhttp3;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class Handshake {
  private final CipherSuite cipherSuite;
  
  private final List<Certificate> localCertificates;
  
  private final List<Certificate> peerCertificates;
  
  private final TlsVersion tlsVersion;
  
  private Handshake(TlsVersion paramTlsVersion, CipherSuite paramCipherSuite, List<Certificate> paramList1, List<Certificate> paramList2) {
    this.tlsVersion = paramTlsVersion;
    this.cipherSuite = paramCipherSuite;
    this.peerCertificates = paramList1;
    this.localCertificates = paramList2;
  }
  
  public static Handshake get(SSLSession paramSSLSession) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface getCipherSuite : ()Ljava/lang/String;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 144
    //   11: ldc 'SSL_NULL_WITH_NULL_NULL'
    //   13: aload_1
    //   14: invokevirtual equals : (Ljava/lang/Object;)Z
    //   17: ifne -> 134
    //   20: aload_1
    //   21: invokestatic forJavaName : (Ljava/lang/String;)Lokhttp3/CipherSuite;
    //   24: astore_2
    //   25: aload_0
    //   26: invokeinterface getProtocol : ()Ljava/lang/String;
    //   31: astore_1
    //   32: aload_1
    //   33: ifnull -> 124
    //   36: ldc 'NONE'
    //   38: aload_1
    //   39: invokevirtual equals : (Ljava/lang/Object;)Z
    //   42: ifne -> 114
    //   45: aload_1
    //   46: invokestatic forJavaName : (Ljava/lang/String;)Lokhttp3/TlsVersion;
    //   49: astore_3
    //   50: aload_0
    //   51: invokeinterface getPeerCertificates : ()[Ljava/security/cert/Certificate;
    //   56: astore_1
    //   57: goto -> 63
    //   60: astore_1
    //   61: aconst_null
    //   62: astore_1
    //   63: aload_1
    //   64: ifnull -> 75
    //   67: aload_1
    //   68: invokestatic immutableList : ([Ljava/lang/Object;)Ljava/util/List;
    //   71: astore_1
    //   72: goto -> 79
    //   75: invokestatic emptyList : ()Ljava/util/List;
    //   78: astore_1
    //   79: aload_0
    //   80: invokeinterface getLocalCertificates : ()[Ljava/security/cert/Certificate;
    //   85: astore_0
    //   86: aload_0
    //   87: ifnull -> 98
    //   90: aload_0
    //   91: invokestatic immutableList : ([Ljava/lang/Object;)Ljava/util/List;
    //   94: astore_0
    //   95: goto -> 102
    //   98: invokestatic emptyList : ()Ljava/util/List;
    //   101: astore_0
    //   102: new okhttp3/Handshake
    //   105: dup
    //   106: aload_3
    //   107: aload_2
    //   108: aload_1
    //   109: aload_0
    //   110: invokespecial <init> : (Lokhttp3/TlsVersion;Lokhttp3/CipherSuite;Ljava/util/List;Ljava/util/List;)V
    //   113: areturn
    //   114: new java/io/IOException
    //   117: dup
    //   118: ldc 'tlsVersion == NONE'
    //   120: invokespecial <init> : (Ljava/lang/String;)V
    //   123: athrow
    //   124: new java/lang/IllegalStateException
    //   127: dup
    //   128: ldc 'tlsVersion == null'
    //   130: invokespecial <init> : (Ljava/lang/String;)V
    //   133: athrow
    //   134: new java/io/IOException
    //   137: dup
    //   138: ldc 'cipherSuite == SSL_NULL_WITH_NULL_NULL'
    //   140: invokespecial <init> : (Ljava/lang/String;)V
    //   143: athrow
    //   144: new java/lang/IllegalStateException
    //   147: dup
    //   148: ldc 'cipherSuite == null'
    //   150: invokespecial <init> : (Ljava/lang/String;)V
    //   153: athrow
    // Exception table:
    //   from	to	target	type
    //   50	57	60	javax/net/ssl/SSLPeerUnverifiedException
  }
  
  public static Handshake get(TlsVersion paramTlsVersion, CipherSuite paramCipherSuite, List<Certificate> paramList1, List<Certificate> paramList2) {
    Objects.requireNonNull(paramTlsVersion, "tlsVersion == null");
    Objects.requireNonNull(paramCipherSuite, "cipherSuite == null");
    return new Handshake(paramTlsVersion, paramCipherSuite, Util.immutableList(paramList1), Util.immutableList(paramList2));
  }
  
  private List<String> names(List<Certificate> paramList) {
    ArrayList<String> arrayList = new ArrayList();
    for (Certificate certificate : paramList) {
      if (certificate instanceof X509Certificate) {
        arrayList.add(String.valueOf(((X509Certificate)certificate).getSubjectDN()));
        continue;
      } 
      arrayList.add(certificate.getType());
    } 
    return arrayList;
  }
  
  public CipherSuite cipherSuite() {
    return this.cipherSuite;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool = paramObject instanceof Handshake;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    bool = bool1;
    if (this.tlsVersion.equals(((Handshake)paramObject).tlsVersion)) {
      bool = bool1;
      if (this.cipherSuite.equals(((Handshake)paramObject).cipherSuite)) {
        bool = bool1;
        if (this.peerCertificates.equals(((Handshake)paramObject).peerCertificates)) {
          bool = bool1;
          if (this.localCertificates.equals(((Handshake)paramObject).localCertificates))
            bool = true; 
        } 
      } 
    } 
    return bool;
  }
  
  public int hashCode() {
    return (((527 + this.tlsVersion.hashCode()) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
  }
  
  public List<Certificate> localCertificates() {
    return this.localCertificates;
  }
  
  @Nullable
  public Principal localPrincipal() {
    Principal principal;
    if (!this.localCertificates.isEmpty()) {
      principal = ((X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal();
    } else {
      principal = null;
    } 
    return principal;
  }
  
  public List<Certificate> peerCertificates() {
    return this.peerCertificates;
  }
  
  @Nullable
  public Principal peerPrincipal() {
    Principal principal;
    if (!this.peerCertificates.isEmpty()) {
      principal = ((X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal();
    } else {
      principal = null;
    } 
    return principal;
  }
  
  public TlsVersion tlsVersion() {
    return this.tlsVersion;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Handshake{tlsVersion=");
    stringBuilder.append(this.tlsVersion);
    stringBuilder.append(" cipherSuite=");
    stringBuilder.append(this.cipherSuite);
    stringBuilder.append(" peerCertificates=");
    stringBuilder.append(names(this.peerCertificates));
    stringBuilder.append(" localCertificates=");
    stringBuilder.append(names(this.localCertificates));
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}
