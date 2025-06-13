package okhttp3;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

public final class ConnectionSpec {
  private static final CipherSuite[] APPROVED_CIPHER_SUITES;
  
  public static final ConnectionSpec CLEARTEXT;
  
  public static final ConnectionSpec COMPATIBLE_TLS;
  
  public static final ConnectionSpec MODERN_TLS;
  
  private static final CipherSuite[] RESTRICTED_CIPHER_SUITES;
  
  public static final ConnectionSpec RESTRICTED_TLS;
  
  @Nullable
  public final String[] cipherSuites;
  
  public final boolean supportsTlsExtensions;
  
  public final boolean tls;
  
  @Nullable
  public final String[] tlsVersions;
  
  static {
    CipherSuite[] arrayOfCipherSuite2 = new CipherSuite[9];
    CipherSuite cipherSuite1 = CipherSuite.TLS_AES_128_GCM_SHA256;
    arrayOfCipherSuite2[0] = cipherSuite1;
    CipherSuite cipherSuite4 = CipherSuite.TLS_AES_256_GCM_SHA384;
    arrayOfCipherSuite2[1] = cipherSuite4;
    CipherSuite cipherSuite6 = CipherSuite.TLS_CHACHA20_POLY1305_SHA256;
    arrayOfCipherSuite2[2] = cipherSuite6;
    CipherSuite cipherSuite2 = CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256;
    arrayOfCipherSuite2[3] = cipherSuite2;
    CipherSuite cipherSuite8 = CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256;
    arrayOfCipherSuite2[4] = cipherSuite8;
    CipherSuite cipherSuite5 = CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384;
    arrayOfCipherSuite2[5] = cipherSuite5;
    CipherSuite cipherSuite9 = CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384;
    arrayOfCipherSuite2[6] = cipherSuite9;
    CipherSuite cipherSuite7 = CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256;
    arrayOfCipherSuite2[7] = cipherSuite7;
    CipherSuite cipherSuite3 = CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256;
    arrayOfCipherSuite2[8] = cipherSuite3;
    RESTRICTED_CIPHER_SUITES = arrayOfCipherSuite2;
    CipherSuite[] arrayOfCipherSuite1 = new CipherSuite[16];
    arrayOfCipherSuite1[0] = cipherSuite1;
    arrayOfCipherSuite1[1] = cipherSuite4;
    arrayOfCipherSuite1[2] = cipherSuite6;
    arrayOfCipherSuite1[3] = cipherSuite2;
    arrayOfCipherSuite1[4] = cipherSuite8;
    arrayOfCipherSuite1[5] = cipherSuite5;
    arrayOfCipherSuite1[6] = cipherSuite9;
    arrayOfCipherSuite1[7] = cipherSuite7;
    arrayOfCipherSuite1[8] = cipherSuite3;
    arrayOfCipherSuite1[9] = CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA;
    arrayOfCipherSuite1[10] = CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA;
    arrayOfCipherSuite1[11] = CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256;
    arrayOfCipherSuite1[12] = CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384;
    arrayOfCipherSuite1[13] = CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA;
    arrayOfCipherSuite1[14] = CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA;
    arrayOfCipherSuite1[15] = CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA;
    APPROVED_CIPHER_SUITES = arrayOfCipherSuite1;
    Builder builder = (new Builder(true)).cipherSuites(arrayOfCipherSuite2);
    TlsVersion tlsVersion1 = TlsVersion.TLS_1_3;
    TlsVersion tlsVersion2 = TlsVersion.TLS_1_2;
    RESTRICTED_TLS = builder.tlsVersions(new TlsVersion[] { tlsVersion1, tlsVersion2 }).supportsTlsExtensions(true).build();
    MODERN_TLS = (new Builder(true)).cipherSuites(arrayOfCipherSuite1).tlsVersions(new TlsVersion[] { tlsVersion1, tlsVersion2 }).supportsTlsExtensions(true).build();
    COMPATIBLE_TLS = (new Builder(true)).cipherSuites(arrayOfCipherSuite1).tlsVersions(new TlsVersion[] { tlsVersion1, tlsVersion2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0 }).supportsTlsExtensions(true).build();
    CLEARTEXT = (new Builder(false)).build();
  }
  
  public ConnectionSpec(Builder paramBuilder) {
    this.tls = paramBuilder.tls;
    this.cipherSuites = paramBuilder.cipherSuites;
    this.tlsVersions = paramBuilder.tlsVersions;
    this.supportsTlsExtensions = paramBuilder.supportsTlsExtensions;
  }
  
  private ConnectionSpec supportedSpec(SSLSocket paramSSLSocket, boolean paramBoolean) {
    String[] arrayOfString2;
    String[] arrayOfString3;
    if (this.cipherSuites != null) {
      arrayOfString2 = Util.intersect(CipherSuite.ORDER_BY_NAME, paramSSLSocket.getEnabledCipherSuites(), this.cipherSuites);
    } else {
      arrayOfString2 = paramSSLSocket.getEnabledCipherSuites();
    } 
    if (this.tlsVersions != null) {
      arrayOfString3 = Util.intersect(Util.NATURAL_ORDER, paramSSLSocket.getEnabledProtocols(), this.tlsVersions);
    } else {
      arrayOfString3 = paramSSLSocket.getEnabledProtocols();
    } 
    String[] arrayOfString4 = paramSSLSocket.getSupportedCipherSuites();
    int i = Util.indexOf(CipherSuite.ORDER_BY_NAME, arrayOfString4, "TLS_FALLBACK_SCSV");
    String[] arrayOfString1 = arrayOfString2;
    if (paramBoolean) {
      arrayOfString1 = arrayOfString2;
      if (i != -1)
        arrayOfString1 = Util.concat(arrayOfString2, arrayOfString4[i]); 
    } 
    return (new Builder(this)).cipherSuites(arrayOfString1).tlsVersions(arrayOfString3).build();
  }
  
  public void apply(SSLSocket paramSSLSocket, boolean paramBoolean) {
    ConnectionSpec connectionSpec = supportedSpec(paramSSLSocket, paramBoolean);
    String[] arrayOfString = connectionSpec.tlsVersions;
    if (arrayOfString != null)
      paramSSLSocket.setEnabledProtocols(arrayOfString); 
    arrayOfString = connectionSpec.cipherSuites;
    if (arrayOfString != null)
      paramSSLSocket.setEnabledCipherSuites(arrayOfString); 
  }
  
  @Nullable
  public List<CipherSuite> cipherSuites() {
    String[] arrayOfString = this.cipherSuites;
    if (arrayOfString != null) {
      List<CipherSuite> list = CipherSuite.forJavaNames(arrayOfString);
    } else {
      arrayOfString = null;
    } 
    return (List<CipherSuite>)arrayOfString;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    if (!(paramObject instanceof ConnectionSpec))
      return false; 
    if (paramObject == this)
      return true; 
    paramObject = paramObject;
    boolean bool = this.tls;
    if (bool != ((ConnectionSpec)paramObject).tls)
      return false; 
    if (bool) {
      if (!Arrays.equals((Object[])this.cipherSuites, (Object[])((ConnectionSpec)paramObject).cipherSuites))
        return false; 
      if (!Arrays.equals((Object[])this.tlsVersions, (Object[])((ConnectionSpec)paramObject).tlsVersions))
        return false; 
      if (this.supportsTlsExtensions != ((ConnectionSpec)paramObject).supportsTlsExtensions)
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    byte b;
    if (this.tls) {
      b = ((527 + Arrays.hashCode((Object[])this.cipherSuites)) * 31 + Arrays.hashCode((Object[])this.tlsVersions)) * 31 + (this.supportsTlsExtensions ^ true);
    } else {
      b = 17;
    } 
    return b;
  }
  
  public boolean isCompatible(SSLSocket paramSSLSocket) {
    if (!this.tls)
      return false; 
    String[] arrayOfString = this.tlsVersions;
    if (arrayOfString != null && !Util.nonEmptyIntersection(Util.NATURAL_ORDER, arrayOfString, paramSSLSocket.getEnabledProtocols()))
      return false; 
    arrayOfString = this.cipherSuites;
    return !(arrayOfString != null && !Util.nonEmptyIntersection(CipherSuite.ORDER_BY_NAME, arrayOfString, paramSSLSocket.getEnabledCipherSuites()));
  }
  
  public boolean isTls() {
    return this.tls;
  }
  
  public boolean supportsTlsExtensions() {
    return this.supportsTlsExtensions;
  }
  
  @Nullable
  public List<TlsVersion> tlsVersions() {
    String[] arrayOfString = this.tlsVersions;
    if (arrayOfString != null) {
      List<TlsVersion> list = TlsVersion.forJavaNames(arrayOfString);
    } else {
      arrayOfString = null;
    } 
    return (List<TlsVersion>)arrayOfString;
  }
  
  public String toString() {
    if (!this.tls)
      return "ConnectionSpec()"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ConnectionSpec(cipherSuites=");
    stringBuilder.append(Objects.toString(cipherSuites(), "[all enabled]"));
    stringBuilder.append(", tlsVersions=");
    stringBuilder.append(Objects.toString(tlsVersions(), "[all enabled]"));
    stringBuilder.append(", supportsTlsExtensions=");
    stringBuilder.append(this.supportsTlsExtensions);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public static final class Builder {
    @Nullable
    public String[] cipherSuites;
    
    public boolean supportsTlsExtensions;
    
    public boolean tls;
    
    @Nullable
    public String[] tlsVersions;
    
    public Builder(ConnectionSpec param1ConnectionSpec) {
      this.tls = param1ConnectionSpec.tls;
      this.cipherSuites = param1ConnectionSpec.cipherSuites;
      this.tlsVersions = param1ConnectionSpec.tlsVersions;
      this.supportsTlsExtensions = param1ConnectionSpec.supportsTlsExtensions;
    }
    
    public Builder(boolean param1Boolean) {
      this.tls = param1Boolean;
    }
    
    public Builder allEnabledCipherSuites() {
      if (this.tls) {
        this.cipherSuites = null;
        return this;
      } 
      throw new IllegalStateException("no cipher suites for cleartext connections");
    }
    
    public Builder allEnabledTlsVersions() {
      if (this.tls) {
        this.tlsVersions = null;
        return this;
      } 
      throw new IllegalStateException("no TLS versions for cleartext connections");
    }
    
    public ConnectionSpec build() {
      return new ConnectionSpec(this);
    }
    
    public Builder cipherSuites(String... param1VarArgs) {
      if (this.tls) {
        if (param1VarArgs.length != 0) {
          this.cipherSuites = (String[])param1VarArgs.clone();
          return this;
        } 
        throw new IllegalArgumentException("At least one cipher suite is required");
      } 
      throw new IllegalStateException("no cipher suites for cleartext connections");
    }
    
    public Builder cipherSuites(CipherSuite... param1VarArgs) {
      if (this.tls) {
        String[] arrayOfString = new String[param1VarArgs.length];
        for (byte b = 0; b < param1VarArgs.length; b++)
          arrayOfString[b] = (param1VarArgs[b]).javaName; 
        return cipherSuites(arrayOfString);
      } 
      throw new IllegalStateException("no cipher suites for cleartext connections");
    }
    
    public Builder supportsTlsExtensions(boolean param1Boolean) {
      if (this.tls) {
        this.supportsTlsExtensions = param1Boolean;
        return this;
      } 
      throw new IllegalStateException("no TLS extensions for cleartext connections");
    }
    
    public Builder tlsVersions(String... param1VarArgs) {
      if (this.tls) {
        if (param1VarArgs.length != 0) {
          this.tlsVersions = (String[])param1VarArgs.clone();
          return this;
        } 
        throw new IllegalArgumentException("At least one TLS version is required");
      } 
      throw new IllegalStateException("no TLS versions for cleartext connections");
    }
    
    public Builder tlsVersions(TlsVersion... param1VarArgs) {
      if (this.tls) {
        String[] arrayOfString = new String[param1VarArgs.length];
        for (byte b = 0; b < param1VarArgs.length; b++)
          arrayOfString[b] = (param1VarArgs[b]).javaName; 
        return tlsVersions(arrayOfString);
      } 
      throw new IllegalStateException("no TLS versions for cleartext connections");
    }
  }
}
