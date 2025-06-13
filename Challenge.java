package okhttp3;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

public final class Challenge {
  private final Map<String, String> authParams;
  
  private final String scheme;
  
  public Challenge(String paramString1, String paramString2) {
    Objects.requireNonNull(paramString1, "scheme == null");
    Objects.requireNonNull(paramString2, "realm == null");
    this.scheme = paramString1;
    this.authParams = Collections.singletonMap("realm", paramString2);
  }
  
  public Challenge(String paramString, Map<String, String> paramMap) {
    Objects.requireNonNull(paramString, "scheme == null");
    Objects.requireNonNull(paramMap, "authParams == null");
    this.scheme = paramString;
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      if (entry.getKey() == null) {
        paramString = null;
      } else {
        paramString = ((String)entry.getKey()).toLowerCase(Locale.US);
      } 
      linkedHashMap.put(paramString, entry.getValue());
    } 
    this.authParams = Collections.unmodifiableMap(linkedHashMap);
  }
  
  public Map<String, String> authParams() {
    return this.authParams;
  }
  
  public Charset charset() {
    String str = this.authParams.get("charset");
    if (str != null)
      try {
        return Charset.forName(str);
      } catch (Exception exception) {} 
    return StandardCharsets.ISO_8859_1;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    if (paramObject instanceof Challenge) {
      paramObject = paramObject;
      if (((Challenge)paramObject).scheme.equals(this.scheme) && ((Challenge)paramObject).authParams.equals(this.authParams))
        return true; 
    } 
    return false;
  }
  
  public int hashCode() {
    return (899 + this.scheme.hashCode()) * 31 + this.authParams.hashCode();
  }
  
  public String realm() {
    return this.authParams.get("realm");
  }
  
  public String scheme() {
    return this.scheme;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.scheme);
    stringBuilder.append(" authParams=");
    stringBuilder.append(this.authParams);
    return stringBuilder.toString();
  }
  
  public Challenge withCharset(Charset paramCharset) {
    Objects.requireNonNull(paramCharset, "charset == null");
    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>(this.authParams);
    linkedHashMap.put("charset", paramCharset.name());
    return new Challenge(this.scheme, linkedHashMap);
  }
}
