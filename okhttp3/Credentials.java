package okhttp3;

import I丨L.iI丨LLL1;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Credentials {
  public static String basic(String paramString1, String paramString2) {
    return basic(paramString1, paramString2, StandardCharsets.ISO_8859_1);
  }
  
  public static String basic(String paramString1, String paramString2, Charset paramCharset) {
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString1);
    stringBuilder2.append(":");
    stringBuilder2.append(paramString2);
    paramString1 = iI丨LLL1.encodeString(stringBuilder2.toString(), paramCharset).base64();
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("Basic ");
    stringBuilder1.append(paramString1);
    return stringBuilder1.toString();
  }
}
