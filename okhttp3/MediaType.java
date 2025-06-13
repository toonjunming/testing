package okhttp3;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public final class MediaType {
  private static final Pattern PARAMETER;
  
  private static final String QUOTED = "\"([^\"]*)\"";
  
  private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
  
  private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
  
  @Nullable
  private final String charset;
  
  private final String mediaType;
  
  private final String subtype;
  
  private final String type;
  
  static {
    PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
  }
  
  private MediaType(String paramString1, String paramString2, String paramString3, @Nullable String paramString4) {
    this.mediaType = paramString1;
    this.type = paramString2;
    this.subtype = paramString3;
    this.charset = paramString4;
  }
  
  public static MediaType get(String paramString) {
    Matcher matcher = TYPE_SUBTYPE.matcher(paramString);
    if (matcher.lookingAt()) {
      String str1;
      String str2 = matcher.group(1);
      Locale locale = Locale.US;
      String str3 = str2.toLowerCase(locale);
      String str4 = matcher.group(2).toLowerCase(locale);
      locale = null;
      Matcher matcher1 = PARAMETER.matcher(paramString);
      int i = matcher.end();
      while (i < paramString.length()) {
        matcher1.region(i, paramString.length());
        if (matcher1.lookingAt()) {
          String str;
          str2 = matcher1.group(1);
          Locale locale1 = locale;
          if (str2 != null)
            if (!str2.equalsIgnoreCase("charset")) {
              locale1 = locale;
            } else {
              str2 = matcher1.group(2);
              if (str2 != null) {
                str = str2;
                if (str2.startsWith("'")) {
                  str = str2;
                  if (str2.endsWith("'")) {
                    str = str2;
                    if (str2.length() > 2)
                      str = str2.substring(1, str2.length() - 1); 
                  } 
                } 
              } else {
                str = matcher1.group(3);
              } 
              if (locale != null && !str.equalsIgnoreCase((String)locale)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Multiple charsets defined: \"");
                stringBuilder2.append((String)locale);
                stringBuilder2.append("\" and: \"");
                stringBuilder2.append(str);
                stringBuilder2.append("\" for: \"");
                stringBuilder2.append(paramString);
                stringBuilder2.append('"');
                throw new IllegalArgumentException(stringBuilder2.toString());
              } 
            }  
          i = matcher1.end();
          str1 = str;
          continue;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Parameter is not formatted correctly: \"");
        stringBuilder1.append(paramString.substring(i));
        stringBuilder1.append("\" for: \"");
        stringBuilder1.append(paramString);
        stringBuilder1.append('"');
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      return new MediaType(paramString, str3, str4, str1);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No subtype found for: \"");
    stringBuilder.append(paramString);
    stringBuilder.append('"');
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @Nullable
  public static MediaType parse(String paramString) {
    try {
      return get(paramString);
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    } 
  }
  
  @Nullable
  public Charset charset() {
    return charset(null);
  }
  
  @Nullable
  public Charset charset(@Nullable Charset paramCharset) {
    Charset charset;
    try {
      String str = this.charset;
      charset = paramCharset;
      if (str != null)
        charset = Charset.forName(str); 
    } catch (IllegalArgumentException illegalArgumentException) {
      charset = paramCharset;
    } 
    return charset;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool;
    if (paramObject instanceof MediaType && ((MediaType)paramObject).mediaType.equals(this.mediaType)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int hashCode() {
    return this.mediaType.hashCode();
  }
  
  public String subtype() {
    return this.subtype;
  }
  
  public String toString() {
    return this.mediaType;
  }
  
  public String type() {
    return this.type;
  }
}
