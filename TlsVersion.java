package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TlsVersion {
  SSL_3_0, TLS_1_0, TLS_1_1, TLS_1_2, TLS_1_3;
  
  private static final TlsVersion[] $VALUES;
  
  public final String javaName;
  
  static {
    TlsVersion tlsVersion4 = new TlsVersion("TLS_1_3", 0, "TLSv1.3");
    TLS_1_3 = tlsVersion4;
    TlsVersion tlsVersion5 = new TlsVersion("TLS_1_2", 1, "TLSv1.2");
    TLS_1_2 = tlsVersion5;
    TlsVersion tlsVersion3 = new TlsVersion("TLS_1_1", 2, "TLSv1.1");
    TLS_1_1 = tlsVersion3;
    TlsVersion tlsVersion2 = new TlsVersion("TLS_1_0", 3, "TLSv1");
    TLS_1_0 = tlsVersion2;
    TlsVersion tlsVersion1 = new TlsVersion("SSL_3_0", 4, "SSLv3");
    SSL_3_0 = tlsVersion1;
    $VALUES = new TlsVersion[] { tlsVersion4, tlsVersion5, tlsVersion3, tlsVersion2, tlsVersion1 };
  }
  
  TlsVersion(String paramString1) {
    this.javaName = paramString1;
  }
  
  public static TlsVersion forJavaName(String paramString) {
    StringBuilder stringBuilder;
    paramString.hashCode();
    int i = paramString.hashCode();
    byte b = -1;
    switch (i) {
      case 79923350:
        if (!paramString.equals("TLSv1"))
          break; 
        b = 4;
        break;
      case 79201641:
        if (!paramString.equals("SSLv3"))
          break; 
        b = 3;
        break;
      case -503070501:
        if (!paramString.equals("TLSv1.3"))
          break; 
        b = 2;
        break;
      case -503070502:
        if (!paramString.equals("TLSv1.2"))
          break; 
        b = 1;
        break;
      case -503070503:
        if (!paramString.equals("TLSv1.1"))
          break; 
        b = 0;
        break;
    } 
    switch (b) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected TLS version: ");
        stringBuilder.append(paramString);
        throw new IllegalArgumentException(stringBuilder.toString());
      case 4:
        return TLS_1_0;
      case 3:
        return SSL_3_0;
      case 2:
        return TLS_1_3;
      case 1:
        return TLS_1_2;
      case 0:
        break;
    } 
    return TLS_1_1;
  }
  
  public static List<TlsVersion> forJavaNames(String... paramVarArgs) {
    ArrayList<TlsVersion> arrayList = new ArrayList(paramVarArgs.length);
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      arrayList.add(forJavaName(paramVarArgs[b])); 
    return Collections.unmodifiableList(arrayList);
  }
  
  public String javaName() {
    return this.javaName;
  }
}
