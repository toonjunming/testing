package okhttp3.internal.http2;

import I丨L.iI丨LLL1;
import okhttp3.internal.Util;

public final class Header {
  public static final iI丨LLL1 PSEUDO_PREFIX = iI丨LLL1.encodeUtf8(":");
  
  public static final iI丨LLL1 RESPONSE_STATUS = iI丨LLL1.encodeUtf8(":status");
  
  public static final String RESPONSE_STATUS_UTF8 = ":status";
  
  public static final iI丨LLL1 TARGET_AUTHORITY;
  
  public static final String TARGET_AUTHORITY_UTF8 = ":authority";
  
  public static final iI丨LLL1 TARGET_METHOD = iI丨LLL1.encodeUtf8(":method");
  
  public static final String TARGET_METHOD_UTF8 = ":method";
  
  public static final iI丨LLL1 TARGET_PATH = iI丨LLL1.encodeUtf8(":path");
  
  public static final String TARGET_PATH_UTF8 = ":path";
  
  public static final iI丨LLL1 TARGET_SCHEME = iI丨LLL1.encodeUtf8(":scheme");
  
  public static final String TARGET_SCHEME_UTF8 = ":scheme";
  
  public final int hpackSize;
  
  public final iI丨LLL1 name;
  
  public final iI丨LLL1 value;
  
  static {
    TARGET_AUTHORITY = iI丨LLL1.encodeUtf8(":authority");
  }
  
  public Header(iI丨LLL1 paramiI丨LLL11, iI丨LLL1 paramiI丨LLL12) {
    this.name = paramiI丨LLL11;
    this.value = paramiI丨LLL12;
    this.hpackSize = paramiI丨LLL11.size() + 32 + paramiI丨LLL12.size();
  }
  
  public Header(iI丨LLL1 paramiI丨LLL1, String paramString) {
    this(paramiI丨LLL1, iI丨LLL1.encodeUtf8(paramString));
  }
  
  public Header(String paramString1, String paramString2) {
    this(iI丨LLL1.encodeUtf8(paramString1), iI丨LLL1.encodeUtf8(paramString2));
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Header;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.name.equals(((Header)paramObject).name)) {
        bool1 = bool2;
        if (this.value.equals(((Header)paramObject).value))
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  public int hashCode() {
    return (527 + this.name.hashCode()) * 31 + this.value.hashCode();
  }
  
  public String toString() {
    return Util.format("%s: %s", new Object[] { this.name.utf8(), this.value.utf8() });
  }
}
