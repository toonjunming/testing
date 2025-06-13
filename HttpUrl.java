package okhttp3;

import I丨L.I1I;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;

public final class HttpUrl {
  public static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
  
  public static final String FRAGMENT_ENCODE_SET = "";
  
  public static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
  
  private static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  public static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
  
  public static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
  
  public static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
  
  public static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
  
  public static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
  
  public static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
  
  public static final String QUERY_ENCODE_SET = " \"'<>#";
  
  public static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
  
  @Nullable
  private final String fragment;
  
  public final String host;
  
  private final String password;
  
  private final List<String> pathSegments;
  
  public final int port;
  
  @Nullable
  private final List<String> queryNamesAndValues;
  
  public final String scheme;
  
  private final String url;
  
  private final String username;
  
  public HttpUrl(Builder paramBuilder) {
    String str1;
    this.scheme = paramBuilder.scheme;
    this.username = percentDecode(paramBuilder.encodedUsername, false);
    this.password = percentDecode(paramBuilder.encodedPassword, false);
    this.host = paramBuilder.host;
    this.port = paramBuilder.effectivePort();
    this.pathSegments = percentDecode(paramBuilder.encodedPathSegments, false);
    List<String> list = paramBuilder.encodedQueryNamesAndValues;
    List list1 = null;
    if (list != null) {
      list = percentDecode(list, true);
    } else {
      list = null;
    } 
    this.queryNamesAndValues = list;
    String str2 = paramBuilder.encodedFragment;
    list = list1;
    if (str2 != null)
      str1 = percentDecode(str2, false); 
    this.fragment = str1;
    this.url = paramBuilder.toString();
  }
  
  public static String canonicalize(String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, @Nullable Charset paramCharset) {
    for (int i = paramInt1; i < paramInt2; i += Character.charCount(j)) {
      int j = paramString1.codePointAt(i);
      if (j < 32 || j == 127 || (j >= 128 && paramBoolean4) || paramString2.indexOf(j) != -1 || (j == 37 && (!paramBoolean1 || (paramBoolean2 && !percentEncoded(paramString1, i, paramInt2)))) || (j == 43 && paramBoolean3)) {
        I1I i1I = new I1I();
        i1I.l1Lll(paramString1, paramInt1, i);
        canonicalize(i1I, paramString1, i, paramInt2, paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramCharset);
        return i1I.iIi1();
      } 
    } 
    return paramString1.substring(paramInt1, paramInt2);
  }
  
  public static String canonicalize(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    return canonicalize(paramString1, 0, paramString1.length(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, null);
  }
  
  public static String canonicalize(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, @Nullable Charset paramCharset) {
    return canonicalize(paramString1, 0, paramString1.length(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramCharset);
  }
  
  public static void canonicalize(I1I paramI1I, String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, @Nullable Charset paramCharset) {
    for (Object object = null; paramInt1 < paramInt2; object = SYNTHETIC_LOCAL_VARIABLE_14) {
      int i = paramString1.codePointAt(paramInt1);
      if (paramBoolean1) {
        Object object1 = object;
        if (i != 9) {
          object1 = object;
          if (i != 10) {
            object1 = object;
            if (i != 12) {
              if (i == 13) {
                object1 = object;
                continue;
              } 
            } else {
              continue;
            } 
          } else {
            continue;
          } 
        } else {
          continue;
        } 
      } 
      if (i == 43 && paramBoolean3) {
        String str;
        if (paramBoolean1) {
          str = "+";
        } else {
          str = "%2B";
        } 
        paramI1I.lL(str);
        Object object1 = object;
      } else if (i < 32 || i == 127 || (i >= 128 && paramBoolean4) || paramString2.indexOf(i) != -1 || (i == 37 && (!paramBoolean1 || (paramBoolean2 && !percentEncoded(paramString1, paramInt1, paramInt2))))) {
        Object object1 = object;
        if (object == null)
          object1 = new I1I(); 
        if (paramCharset == null || paramCharset.equals(StandardCharsets.UTF_8)) {
          object1.I1IILIIL(i);
        } else {
          object1.丨i1丨1i(paramString1, paramInt1, Character.charCount(i) + paramInt1, paramCharset);
        } 
        while (true) {
          Object object2 = object1;
          if (!object1.l丨Li1LL()) {
            int j = object1.readByte() & 0xFF;
            paramI1I.iI(37);
            object = HEX_DIGITS;
            paramI1I.iI(object[j >> 4 & 0xF]);
            paramI1I.iI(object[j & 0xF]);
            continue;
          } 
          break;
        } 
      } else {
        paramI1I.I1IILIIL(i);
        Object object1 = object;
      } 
      continue;
      paramInt1 += Character.charCount(SYNTHETIC_LOCAL_VARIABLE_11);
    } 
  }
  
  public static int defaultPort(String paramString) {
    return paramString.equals("http") ? 80 : (paramString.equals("https") ? 443 : -1);
  }
  
  public static HttpUrl get(String paramString) {
    return (new Builder()).parse(null, paramString).build();
  }
  
  @Nullable
  public static HttpUrl get(URI paramURI) {
    return parse(paramURI.toString());
  }
  
  @Nullable
  public static HttpUrl get(URL paramURL) {
    return parse(paramURL.toString());
  }
  
  public static void namesAndValuesToQueryString(StringBuilder paramStringBuilder, List<String> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b += 2) {
      String str2 = paramList.get(b);
      String str1 = paramList.get(b + 1);
      if (b > 0)
        paramStringBuilder.append('&'); 
      paramStringBuilder.append(str2);
      if (str1 != null) {
        paramStringBuilder.append('=');
        paramStringBuilder.append(str1);
      } 
    } 
  }
  
  @Nullable
  public static HttpUrl parse(String paramString) {
    try {
      return get(paramString);
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    } 
  }
  
  public static void pathSegmentsToString(StringBuilder paramStringBuilder, List<String> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      paramStringBuilder.append('/');
      paramStringBuilder.append(paramList.get(b));
    } 
  }
  
  public static String percentDecode(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    for (int i = paramInt1; i < paramInt2; i++) {
      char c = paramString.charAt(i);
      if (c == '%' || (c == '+' && paramBoolean)) {
        I1I i1I = new I1I();
        i1I.l1Lll(paramString, paramInt1, i);
        percentDecode(i1I, paramString, i, paramInt2, paramBoolean);
        return i1I.iIi1();
      } 
    } 
    return paramString.substring(paramInt1, paramInt2);
  }
  
  public static String percentDecode(String paramString, boolean paramBoolean) {
    return percentDecode(paramString, 0, paramString.length(), paramBoolean);
  }
  
  private List<String> percentDecode(List<String> paramList, boolean paramBoolean) {
    int i = paramList.size();
    ArrayList<String> arrayList = new ArrayList(i);
    for (byte b = 0; b < i; b++) {
      String str = paramList.get(b);
      if (str != null) {
        str = percentDecode(str, paramBoolean);
      } else {
        str = null;
      } 
      arrayList.add(str);
    } 
    return Collections.unmodifiableList(arrayList);
  }
  
  public static void percentDecode(I1I paramI1I, String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: iload_3
    //   2: if_icmpge -> 123
    //   5: aload_1
    //   6: iload_2
    //   7: invokevirtual codePointAt : (I)I
    //   10: istore #6
    //   12: iload #6
    //   14: bipush #37
    //   16: if_icmpne -> 83
    //   19: iload_2
    //   20: iconst_2
    //   21: iadd
    //   22: istore #5
    //   24: iload #5
    //   26: iload_3
    //   27: if_icmpge -> 83
    //   30: aload_1
    //   31: iload_2
    //   32: iconst_1
    //   33: iadd
    //   34: invokevirtual charAt : (I)C
    //   37: invokestatic decodeHexDigit : (C)I
    //   40: istore #7
    //   42: aload_1
    //   43: iload #5
    //   45: invokevirtual charAt : (I)C
    //   48: invokestatic decodeHexDigit : (C)I
    //   51: istore #8
    //   53: iload #7
    //   55: iconst_m1
    //   56: if_icmpeq -> 105
    //   59: iload #8
    //   61: iconst_m1
    //   62: if_icmpeq -> 105
    //   65: aload_0
    //   66: iload #7
    //   68: iconst_4
    //   69: ishl
    //   70: iload #8
    //   72: iadd
    //   73: invokevirtual iI : (I)LI丨L/I1I;
    //   76: pop
    //   77: iload #5
    //   79: istore_2
    //   80: goto -> 112
    //   83: iload #6
    //   85: bipush #43
    //   87: if_icmpne -> 105
    //   90: iload #4
    //   92: ifeq -> 105
    //   95: aload_0
    //   96: bipush #32
    //   98: invokevirtual iI : (I)LI丨L/I1I;
    //   101: pop
    //   102: goto -> 112
    //   105: aload_0
    //   106: iload #6
    //   108: invokevirtual I1IILIIL : (I)LI丨L/I1I;
    //   111: pop
    //   112: iload_2
    //   113: iload #6
    //   115: invokestatic charCount : (I)I
    //   118: iadd
    //   119: istore_2
    //   120: goto -> 0
    //   123: return
  }
  
  public static boolean percentEncoded(String paramString, int paramInt1, int paramInt2) {
    int i = paramInt1 + 2;
    boolean bool = true;
    if (i >= paramInt2 || paramString.charAt(paramInt1) != '%' || Util.decodeHexDigit(paramString.charAt(paramInt1 + 1)) == -1 || Util.decodeHexDigit(paramString.charAt(i)) == -1)
      bool = false; 
    return bool;
  }
  
  public static List<String> queryStringToNamesAndValues(String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    for (int i = 0; i <= paramString.length(); i = j + 1) {
      int k = paramString.indexOf('&', i);
      int j = k;
      if (k == -1)
        j = paramString.length(); 
      k = paramString.indexOf('=', i);
      if (k == -1 || k > j) {
        arrayList.add(paramString.substring(i, j));
        arrayList.add(null);
      } else {
        arrayList.add(paramString.substring(i, k));
        arrayList.add(paramString.substring(k + 1, j));
      } 
    } 
    return arrayList;
  }
  
  @Nullable
  public String encodedFragment() {
    if (this.fragment == null)
      return null; 
    int i = this.url.indexOf('#');
    return this.url.substring(i + 1);
  }
  
  public String encodedPassword() {
    if (this.password.isEmpty())
      return ""; 
    int j = this.url.indexOf(':', this.scheme.length() + 3);
    int i = this.url.indexOf('@');
    return this.url.substring(j + 1, i);
  }
  
  public String encodedPath() {
    int i = this.url.indexOf('/', this.scheme.length() + 3);
    String str = this.url;
    int j = Util.delimiterOffset(str, i, str.length(), "?#");
    return this.url.substring(i, j);
  }
  
  public List<String> encodedPathSegments() {
    int i = this.url.indexOf('/', this.scheme.length() + 3);
    String str = this.url;
    int j = Util.delimiterOffset(str, i, str.length(), "?#");
    ArrayList<String> arrayList = new ArrayList();
    while (i < j) {
      int k = i + 1;
      i = Util.delimiterOffset(this.url, k, j, '/');
      arrayList.add(this.url.substring(k, i));
    } 
    return arrayList;
  }
  
  @Nullable
  public String encodedQuery() {
    if (this.queryNamesAndValues == null)
      return null; 
    int i = this.url.indexOf('?') + 1;
    String str = this.url;
    int j = Util.delimiterOffset(str, i, str.length(), '#');
    return this.url.substring(i, j);
  }
  
  public String encodedUsername() {
    if (this.username.isEmpty())
      return ""; 
    int i = this.scheme.length() + 3;
    String str = this.url;
    int j = Util.delimiterOffset(str, i, str.length(), ":@");
    return this.url.substring(i, j);
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool;
    if (paramObject instanceof HttpUrl && ((HttpUrl)paramObject).url.equals(this.url)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Nullable
  public String fragment() {
    return this.fragment;
  }
  
  public int hashCode() {
    return this.url.hashCode();
  }
  
  public String host() {
    return this.host;
  }
  
  public boolean isHttps() {
    return this.scheme.equals("https");
  }
  
  public Builder newBuilder() {
    byte b;
    Builder builder = new Builder();
    builder.scheme = this.scheme;
    builder.encodedUsername = encodedUsername();
    builder.encodedPassword = encodedPassword();
    builder.host = this.host;
    if (this.port != defaultPort(this.scheme)) {
      b = this.port;
    } else {
      b = -1;
    } 
    builder.port = b;
    builder.encodedPathSegments.clear();
    builder.encodedPathSegments.addAll(encodedPathSegments());
    builder.encodedQuery(encodedQuery());
    builder.encodedFragment = encodedFragment();
    return builder;
  }
  
  @Nullable
  public Builder newBuilder(String paramString) {
    try {
      Builder builder = new Builder();
      this();
      return builder.parse(this, paramString);
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    } 
  }
  
  public String password() {
    return this.password;
  }
  
  public List<String> pathSegments() {
    return this.pathSegments;
  }
  
  public int pathSize() {
    return this.pathSegments.size();
  }
  
  public int port() {
    return this.port;
  }
  
  @Nullable
  public String query() {
    if (this.queryNamesAndValues == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    namesAndValuesToQueryString(stringBuilder, this.queryNamesAndValues);
    return stringBuilder.toString();
  }
  
  @Nullable
  public String queryParameter(String paramString) {
    List<String> list = this.queryNamesAndValues;
    if (list == null)
      return null; 
    byte b = 0;
    int i = list.size();
    while (b < i) {
      if (paramString.equals(this.queryNamesAndValues.get(b)))
        return this.queryNamesAndValues.get(b + 1); 
      b += 2;
    } 
    return null;
  }
  
  public String queryParameterName(int paramInt) {
    List<String> list = this.queryNamesAndValues;
    if (list != null)
      return list.get(paramInt * 2); 
    throw new IndexOutOfBoundsException();
  }
  
  public Set<String> queryParameterNames() {
    if (this.queryNamesAndValues == null)
      return Collections.emptySet(); 
    LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (b < i) {
      linkedHashSet.add(this.queryNamesAndValues.get(b));
      b += 2;
    } 
    return Collections.unmodifiableSet(linkedHashSet);
  }
  
  public String queryParameterValue(int paramInt) {
    List<String> list = this.queryNamesAndValues;
    if (list != null)
      return list.get(paramInt * 2 + 1); 
    throw new IndexOutOfBoundsException();
  }
  
  public List<String> queryParameterValues(String paramString) {
    if (this.queryNamesAndValues == null)
      return Collections.emptyList(); 
    ArrayList<String> arrayList = new ArrayList();
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (b < i) {
      if (paramString.equals(this.queryNamesAndValues.get(b)))
        arrayList.add(this.queryNamesAndValues.get(b + 1)); 
      b += 2;
    } 
    return Collections.unmodifiableList(arrayList);
  }
  
  public int querySize() {
    boolean bool;
    List<String> list = this.queryNamesAndValues;
    if (list != null) {
      bool = list.size() / 2;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String redact() {
    return newBuilder("/...").username("").password("").build().toString();
  }
  
  @Nullable
  public HttpUrl resolve(String paramString) {
    Builder builder = newBuilder(paramString);
    if (builder != null) {
      HttpUrl httpUrl = builder.build();
    } else {
      builder = null;
    } 
    return (HttpUrl)builder;
  }
  
  public String scheme() {
    return this.scheme;
  }
  
  public String toString() {
    return this.url;
  }
  
  @Nullable
  public String topPrivateDomain() {
    return Util.verifyAsIpAddress(this.host) ? null : PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
  }
  
  public URI uri() {
    String str = newBuilder().reencodeForUri().toString();
    try {
      return new URI(str);
    } catch (URISyntaxException uRISyntaxException) {
      try {
        return URI.create(str.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
      } catch (Exception exception) {
        throw new RuntimeException(uRISyntaxException);
      } 
    } 
  }
  
  public URL url() {
    try {
      return new URL(this.url);
    } catch (MalformedURLException malformedURLException) {
      throw new RuntimeException(malformedURLException);
    } 
  }
  
  public String username() {
    return this.username;
  }
  
  public static final class Builder {
    public static final String INVALID_HOST = "Invalid URL host";
    
    @Nullable
    public String encodedFragment;
    
    public String encodedPassword = "";
    
    public final List<String> encodedPathSegments;
    
    @Nullable
    public List<String> encodedQueryNamesAndValues;
    
    public String encodedUsername = "";
    
    @Nullable
    public String host;
    
    public int port = -1;
    
    @Nullable
    public String scheme;
    
    public Builder() {
      ArrayList<String> arrayList = new ArrayList();
      this.encodedPathSegments = arrayList;
      arrayList.add("");
    }
    
    private Builder addPathSegments(String param1String, boolean param1Boolean) {
      int i = 0;
      while (true) {
        boolean bool;
        int j = Util.delimiterOffset(param1String, i, param1String.length(), "/\\");
        if (j < param1String.length()) {
          bool = true;
        } else {
          bool = false;
        } 
        push(param1String, i, j, bool, param1Boolean);
        i = ++j;
        if (j > param1String.length())
          return this; 
      } 
    }
    
    @Nullable
    private static String canonicalizeHost(String param1String, int param1Int1, int param1Int2) {
      return Util.canonicalizeHost(HttpUrl.percentDecode(param1String, param1Int1, param1Int2, false));
    }
    
    private boolean isDot(String param1String) {
      return (param1String.equals(".") || param1String.equalsIgnoreCase("%2e"));
    }
    
    private boolean isDotDot(String param1String) {
      return (param1String.equals("..") || param1String.equalsIgnoreCase("%2e.") || param1String.equalsIgnoreCase(".%2e") || param1String.equalsIgnoreCase("%2e%2e"));
    }
    
    private static int parsePort(String param1String, int param1Int1, int param1Int2) {
      try {
        param1Int1 = Integer.parseInt(HttpUrl.canonicalize(param1String, param1Int1, param1Int2, "", false, false, false, true, null));
        if (param1Int1 > 0 && param1Int1 <= 65535)
          return param1Int1; 
      } catch (NumberFormatException numberFormatException) {}
      return -1;
    }
    
    private void pop() {
      List<String> list = this.encodedPathSegments;
      if (((String)list.remove(list.size() - 1)).isEmpty() && !this.encodedPathSegments.isEmpty()) {
        list = this.encodedPathSegments;
        list.set(list.size() - 1, "");
      } else {
        this.encodedPathSegments.add("");
      } 
    }
    
    private static int portColonOffset(String param1String, int param1Int1, int param1Int2) {
      while (param1Int1 < param1Int2) {
        char c = param1String.charAt(param1Int1);
        if (c != ':') {
          int i = param1Int1;
          if (c != '[') {
            i = param1Int1;
          } else {
            while (true) {
              param1Int1 = i + 1;
              i = param1Int1;
              if (param1Int1 < param1Int2) {
                i = param1Int1;
                if (param1String.charAt(param1Int1) == ']') {
                  i = param1Int1;
                  break;
                } 
                continue;
              } 
              break;
            } 
          } 
          param1Int1 = i + 1;
          continue;
        } 
        return param1Int1;
      } 
      return param1Int2;
    }
    
    private void push(String param1String, int param1Int1, int param1Int2, boolean param1Boolean1, boolean param1Boolean2) {
      param1String = HttpUrl.canonicalize(param1String, param1Int1, param1Int2, " \"<>^`{}|/\\?#", param1Boolean2, false, false, true, null);
      if (isDot(param1String))
        return; 
      if (isDotDot(param1String)) {
        pop();
        return;
      } 
      List<String> list = this.encodedPathSegments;
      if (((String)list.get(list.size() - 1)).isEmpty()) {
        list = this.encodedPathSegments;
        list.set(list.size() - 1, param1String);
      } else {
        this.encodedPathSegments.add(param1String);
      } 
      if (param1Boolean1)
        this.encodedPathSegments.add(""); 
    }
    
    private void removeAllCanonicalQueryParameters(String param1String) {
      for (int i = this.encodedQueryNamesAndValues.size() - 2; i >= 0; i -= 2) {
        if (param1String.equals(this.encodedQueryNamesAndValues.get(i))) {
          this.encodedQueryNamesAndValues.remove(i + 1);
          this.encodedQueryNamesAndValues.remove(i);
          if (this.encodedQueryNamesAndValues.isEmpty()) {
            this.encodedQueryNamesAndValues = null;
            return;
          } 
        } 
      } 
    }
    
    private void resolvePath(String param1String, int param1Int1, int param1Int2) {
      if (param1Int1 == param1Int2)
        return; 
      char c = param1String.charAt(param1Int1);
      if (c == '/' || c == '\\') {
        this.encodedPathSegments.clear();
        this.encodedPathSegments.add("");
      } else {
        List<String> list = this.encodedPathSegments;
        list.set(list.size() - 1, "");
        while (true) {
          if (param1Int1 < param1Int2) {
            boolean bool;
            int i = Util.delimiterOffset(param1String, param1Int1, param1Int2, "/\\");
            if (i < param1Int2) {
              bool = true;
            } else {
              bool = false;
            } 
            push(param1String, param1Int1, i, bool, true);
            param1Int1 = i;
            if (bool) {
              param1Int1 = i;
            } else {
              continue;
            } 
          } else {
            break;
          } 
          param1Int1++;
        } 
        return;
      } 
      param1Int1++;
      continue;
    }
    
    private static int schemeDelimiterOffset(String param1String, int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_2
      //   1: iload_1
      //   2: isub
      //   3: iconst_2
      //   4: if_icmpge -> 9
      //   7: iconst_m1
      //   8: ireturn
      //   9: aload_0
      //   10: iload_1
      //   11: invokevirtual charAt : (I)C
      //   14: istore #4
      //   16: iload #4
      //   18: bipush #97
      //   20: if_icmplt -> 32
      //   23: iload_1
      //   24: istore_3
      //   25: iload #4
      //   27: bipush #122
      //   29: if_icmple -> 51
      //   32: iload #4
      //   34: bipush #65
      //   36: if_icmplt -> 154
      //   39: iload_1
      //   40: istore_3
      //   41: iload #4
      //   43: bipush #90
      //   45: if_icmple -> 51
      //   48: goto -> 154
      //   51: iload_3
      //   52: iconst_1
      //   53: iadd
      //   54: istore_1
      //   55: iload_1
      //   56: iload_2
      //   57: if_icmpge -> 154
      //   60: aload_0
      //   61: iload_1
      //   62: invokevirtual charAt : (I)C
      //   65: istore #4
      //   67: iload #4
      //   69: bipush #97
      //   71: if_icmplt -> 83
      //   74: iload_1
      //   75: istore_3
      //   76: iload #4
      //   78: bipush #122
      //   80: if_icmple -> 51
      //   83: iload #4
      //   85: bipush #65
      //   87: if_icmplt -> 99
      //   90: iload_1
      //   91: istore_3
      //   92: iload #4
      //   94: bipush #90
      //   96: if_icmple -> 51
      //   99: iload #4
      //   101: bipush #48
      //   103: if_icmplt -> 115
      //   106: iload_1
      //   107: istore_3
      //   108: iload #4
      //   110: bipush #57
      //   112: if_icmple -> 51
      //   115: iload_1
      //   116: istore_3
      //   117: iload #4
      //   119: bipush #43
      //   121: if_icmpeq -> 51
      //   124: iload_1
      //   125: istore_3
      //   126: iload #4
      //   128: bipush #45
      //   130: if_icmpeq -> 51
      //   133: iload #4
      //   135: bipush #46
      //   137: if_icmpne -> 145
      //   140: iload_1
      //   141: istore_3
      //   142: goto -> 51
      //   145: iload #4
      //   147: bipush #58
      //   149: if_icmpne -> 154
      //   152: iload_1
      //   153: ireturn
      //   154: iconst_m1
      //   155: ireturn
    }
    
    private static int slashCount(String param1String, int param1Int1, int param1Int2) {
      byte b = 0;
      while (param1Int1 < param1Int2) {
        char c = param1String.charAt(param1Int1);
        if (c == '\\' || c == '/') {
          b++;
          param1Int1++;
        } 
      } 
      return b;
    }
    
    public Builder addEncodedPathSegment(String param1String) {
      Objects.requireNonNull(param1String, "encodedPathSegment == null");
      push(param1String, 0, param1String.length(), false, true);
      return this;
    }
    
    public Builder addEncodedPathSegments(String param1String) {
      Objects.requireNonNull(param1String, "encodedPathSegments == null");
      return addPathSegments(param1String, true);
    }
    
    public Builder addEncodedQueryParameter(String param1String1, @Nullable String param1String2) {
      Objects.requireNonNull(param1String1, "encodedName == null");
      if (this.encodedQueryNamesAndValues == null)
        this.encodedQueryNamesAndValues = new ArrayList<String>(); 
      this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(param1String1, " \"'<>#&=", true, false, true, true));
      List<String> list = this.encodedQueryNamesAndValues;
      if (param1String2 != null) {
        param1String1 = HttpUrl.canonicalize(param1String2, " \"'<>#&=", true, false, true, true);
      } else {
        param1String1 = null;
      } 
      list.add(param1String1);
      return this;
    }
    
    public Builder addPathSegment(String param1String) {
      Objects.requireNonNull(param1String, "pathSegment == null");
      push(param1String, 0, param1String.length(), false, false);
      return this;
    }
    
    public Builder addPathSegments(String param1String) {
      Objects.requireNonNull(param1String, "pathSegments == null");
      return addPathSegments(param1String, false);
    }
    
    public Builder addQueryParameter(String param1String1, @Nullable String param1String2) {
      Objects.requireNonNull(param1String1, "name == null");
      if (this.encodedQueryNamesAndValues == null)
        this.encodedQueryNamesAndValues = new ArrayList<String>(); 
      this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(param1String1, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true));
      List<String> list = this.encodedQueryNamesAndValues;
      if (param1String2 != null) {
        param1String1 = HttpUrl.canonicalize(param1String2, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true);
      } else {
        param1String1 = null;
      } 
      list.add(param1String1);
      return this;
    }
    
    public HttpUrl build() {
      if (this.scheme != null) {
        if (this.host != null)
          return new HttpUrl(this); 
        throw new IllegalStateException("host == null");
      } 
      throw new IllegalStateException("scheme == null");
    }
    
    public int effectivePort() {
      int i = this.port;
      if (i == -1)
        i = HttpUrl.defaultPort(this.scheme); 
      return i;
    }
    
    public Builder encodedFragment(@Nullable String param1String) {
      if (param1String != null) {
        param1String = HttpUrl.canonicalize(param1String, "", true, false, false, false);
      } else {
        param1String = null;
      } 
      this.encodedFragment = param1String;
      return this;
    }
    
    public Builder encodedPassword(String param1String) {
      Objects.requireNonNull(param1String, "encodedPassword == null");
      this.encodedPassword = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
      return this;
    }
    
    public Builder encodedPath(String param1String) {
      Objects.requireNonNull(param1String, "encodedPath == null");
      if (param1String.startsWith("/")) {
        resolvePath(param1String, 0, param1String.length());
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected encodedPath: ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder encodedQuery(@Nullable String param1String) {
      if (param1String != null) {
        List<String> list = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(param1String, " \"'<>#", true, false, true, true));
      } else {
        param1String = null;
      } 
      this.encodedQueryNamesAndValues = (List<String>)param1String;
      return this;
    }
    
    public Builder encodedUsername(String param1String) {
      Objects.requireNonNull(param1String, "encodedUsername == null");
      this.encodedUsername = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
      return this;
    }
    
    public Builder fragment(@Nullable String param1String) {
      if (param1String != null) {
        param1String = HttpUrl.canonicalize(param1String, "", false, false, false, false);
      } else {
        param1String = null;
      } 
      this.encodedFragment = param1String;
      return this;
    }
    
    public Builder host(String param1String) {
      Objects.requireNonNull(param1String, "host == null");
      String str = canonicalizeHost(param1String, 0, param1String.length());
      if (str != null) {
        this.host = str;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected host: ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder parse(@Nullable HttpUrl param1HttpUrl, String param1String) {
      // Byte code:
      //   0: aload_2
      //   1: iconst_0
      //   2: aload_2
      //   3: invokevirtual length : ()I
      //   6: invokestatic skipLeadingAsciiWhitespace : (Ljava/lang/String;II)I
      //   9: istore_3
      //   10: aload_2
      //   11: iload_3
      //   12: aload_2
      //   13: invokevirtual length : ()I
      //   16: invokestatic skipTrailingAsciiWhitespace : (Ljava/lang/String;II)I
      //   19: istore #8
      //   21: aload_2
      //   22: iload_3
      //   23: iload #8
      //   25: invokestatic schemeDelimiterOffset : (Ljava/lang/String;II)I
      //   28: istore #4
      //   30: iload #4
      //   32: iconst_m1
      //   33: if_icmpeq -> 139
      //   36: aload_2
      //   37: iconst_1
      //   38: iload_3
      //   39: ldc_w 'https:'
      //   42: iconst_0
      //   43: bipush #6
      //   45: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
      //   48: ifeq -> 64
      //   51: aload_0
      //   52: ldc_w 'https'
      //   55: putfield scheme : Ljava/lang/String;
      //   58: iinc #3, 6
      //   61: goto -> 151
      //   64: aload_2
      //   65: iconst_1
      //   66: iload_3
      //   67: ldc_w 'http:'
      //   70: iconst_0
      //   71: iconst_5
      //   72: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
      //   75: ifeq -> 91
      //   78: aload_0
      //   79: ldc_w 'http'
      //   82: putfield scheme : Ljava/lang/String;
      //   85: iinc #3, 5
      //   88: goto -> 151
      //   91: new java/lang/StringBuilder
      //   94: dup
      //   95: invokespecial <init> : ()V
      //   98: astore_1
      //   99: aload_1
      //   100: ldc_w 'Expected URL scheme 'http' or 'https' but was ''
      //   103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   106: pop
      //   107: aload_1
      //   108: aload_2
      //   109: iconst_0
      //   110: iload #4
      //   112: invokevirtual substring : (II)Ljava/lang/String;
      //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   118: pop
      //   119: aload_1
      //   120: ldc_w '''
      //   123: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   126: pop
      //   127: new java/lang/IllegalArgumentException
      //   130: dup
      //   131: aload_1
      //   132: invokevirtual toString : ()Ljava/lang/String;
      //   135: invokespecial <init> : (Ljava/lang/String;)V
      //   138: athrow
      //   139: aload_1
      //   140: ifnull -> 850
      //   143: aload_0
      //   144: aload_1
      //   145: getfield scheme : Ljava/lang/String;
      //   148: putfield scheme : Ljava/lang/String;
      //   151: aload_2
      //   152: iload_3
      //   153: iload #8
      //   155: invokestatic slashCount : (Ljava/lang/String;II)I
      //   158: istore #4
      //   160: iload #4
      //   162: iconst_2
      //   163: if_icmpge -> 276
      //   166: aload_1
      //   167: ifnull -> 276
      //   170: aload_1
      //   171: getfield scheme : Ljava/lang/String;
      //   174: aload_0
      //   175: getfield scheme : Ljava/lang/String;
      //   178: invokevirtual equals : (Ljava/lang/Object;)Z
      //   181: ifne -> 187
      //   184: goto -> 276
      //   187: aload_0
      //   188: aload_1
      //   189: invokevirtual encodedUsername : ()Ljava/lang/String;
      //   192: putfield encodedUsername : Ljava/lang/String;
      //   195: aload_0
      //   196: aload_1
      //   197: invokevirtual encodedPassword : ()Ljava/lang/String;
      //   200: putfield encodedPassword : Ljava/lang/String;
      //   203: aload_0
      //   204: aload_1
      //   205: getfield host : Ljava/lang/String;
      //   208: putfield host : Ljava/lang/String;
      //   211: aload_0
      //   212: aload_1
      //   213: getfield port : I
      //   216: putfield port : I
      //   219: aload_0
      //   220: getfield encodedPathSegments : Ljava/util/List;
      //   223: invokeinterface clear : ()V
      //   228: aload_0
      //   229: getfield encodedPathSegments : Ljava/util/List;
      //   232: aload_1
      //   233: invokevirtual encodedPathSegments : ()Ljava/util/List;
      //   236: invokeinterface addAll : (Ljava/util/Collection;)Z
      //   241: pop
      //   242: iload_3
      //   243: iload #8
      //   245: if_icmpeq -> 261
      //   248: iload_3
      //   249: istore #4
      //   251: aload_2
      //   252: iload_3
      //   253: invokevirtual charAt : (I)C
      //   256: bipush #35
      //   258: if_icmpne -> 684
      //   261: aload_0
      //   262: aload_1
      //   263: invokevirtual encodedQuery : ()Ljava/lang/String;
      //   266: invokevirtual encodedQuery : (Ljava/lang/String;)Lokhttp3/HttpUrl$Builder;
      //   269: pop
      //   270: iload_3
      //   271: istore #4
      //   273: goto -> 684
      //   276: iload_3
      //   277: iload #4
      //   279: iadd
      //   280: istore #5
      //   282: iconst_0
      //   283: istore_3
      //   284: iconst_0
      //   285: istore #4
      //   287: aload_2
      //   288: iload #5
      //   290: iload #8
      //   292: ldc_w '@/\?#'
      //   295: invokestatic delimiterOffset : (Ljava/lang/String;IILjava/lang/String;)I
      //   298: istore #7
      //   300: iload #7
      //   302: iload #8
      //   304: if_icmpeq -> 318
      //   307: aload_2
      //   308: iload #7
      //   310: invokevirtual charAt : (I)C
      //   313: istore #6
      //   315: goto -> 321
      //   318: iconst_m1
      //   319: istore #6
      //   321: iload #6
      //   323: iconst_m1
      //   324: if_icmpeq -> 546
      //   327: iload #6
      //   329: bipush #35
      //   331: if_icmpeq -> 546
      //   334: iload #6
      //   336: bipush #47
      //   338: if_icmpeq -> 546
      //   341: iload #6
      //   343: bipush #92
      //   345: if_icmpeq -> 546
      //   348: iload #6
      //   350: bipush #63
      //   352: if_icmpeq -> 546
      //   355: iload #6
      //   357: bipush #64
      //   359: if_icmpeq -> 365
      //   362: goto -> 543
      //   365: iload_3
      //   366: ifne -> 484
      //   369: aload_2
      //   370: iload #5
      //   372: iload #7
      //   374: bipush #58
      //   376: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
      //   379: istore #6
      //   381: aload_2
      //   382: iload #5
      //   384: iload #6
      //   386: ldc ' "':;<=>@[]^`{}|/\?#'
      //   388: iconst_1
      //   389: iconst_0
      //   390: iconst_0
      //   391: iconst_1
      //   392: aconst_null
      //   393: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZLjava/nio/charset/Charset;)Ljava/lang/String;
      //   396: astore #9
      //   398: aload #9
      //   400: astore_1
      //   401: iload #4
      //   403: ifeq -> 443
      //   406: new java/lang/StringBuilder
      //   409: dup
      //   410: invokespecial <init> : ()V
      //   413: astore_1
      //   414: aload_1
      //   415: aload_0
      //   416: getfield encodedUsername : Ljava/lang/String;
      //   419: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   422: pop
      //   423: aload_1
      //   424: ldc_w '%40'
      //   427: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   430: pop
      //   431: aload_1
      //   432: aload #9
      //   434: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   437: pop
      //   438: aload_1
      //   439: invokevirtual toString : ()Ljava/lang/String;
      //   442: astore_1
      //   443: aload_0
      //   444: aload_1
      //   445: putfield encodedUsername : Ljava/lang/String;
      //   448: iload #6
      //   450: iload #7
      //   452: if_icmpeq -> 478
      //   455: aload_0
      //   456: aload_2
      //   457: iload #6
      //   459: iconst_1
      //   460: iadd
      //   461: iload #7
      //   463: ldc ' "':;<=>@[]^`{}|/\?#'
      //   465: iconst_1
      //   466: iconst_0
      //   467: iconst_0
      //   468: iconst_1
      //   469: aconst_null
      //   470: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZLjava/nio/charset/Charset;)Ljava/lang/String;
      //   473: putfield encodedPassword : Ljava/lang/String;
      //   476: iconst_1
      //   477: istore_3
      //   478: iconst_1
      //   479: istore #4
      //   481: goto -> 537
      //   484: new java/lang/StringBuilder
      //   487: dup
      //   488: invokespecial <init> : ()V
      //   491: astore_1
      //   492: aload_1
      //   493: aload_0
      //   494: getfield encodedPassword : Ljava/lang/String;
      //   497: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   500: pop
      //   501: aload_1
      //   502: ldc_w '%40'
      //   505: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   508: pop
      //   509: aload_1
      //   510: aload_2
      //   511: iload #5
      //   513: iload #7
      //   515: ldc ' "':;<=>@[]^`{}|/\?#'
      //   517: iconst_1
      //   518: iconst_0
      //   519: iconst_0
      //   520: iconst_1
      //   521: aconst_null
      //   522: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZLjava/nio/charset/Charset;)Ljava/lang/String;
      //   525: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   528: pop
      //   529: aload_0
      //   530: aload_1
      //   531: invokevirtual toString : ()Ljava/lang/String;
      //   534: putfield encodedPassword : Ljava/lang/String;
      //   537: iload #7
      //   539: iconst_1
      //   540: iadd
      //   541: istore #5
      //   543: goto -> 287
      //   546: aload_2
      //   547: iload #5
      //   549: iload #7
      //   551: invokestatic portColonOffset : (Ljava/lang/String;II)I
      //   554: istore #4
      //   556: iload #4
      //   558: iconst_1
      //   559: iadd
      //   560: istore_3
      //   561: iload_3
      //   562: iload #7
      //   564: if_icmpge -> 650
      //   567: aload_0
      //   568: aload_2
      //   569: iload #5
      //   571: iload #4
      //   573: invokestatic canonicalizeHost : (Ljava/lang/String;II)Ljava/lang/String;
      //   576: putfield host : Ljava/lang/String;
      //   579: aload_2
      //   580: iload_3
      //   581: iload #7
      //   583: invokestatic parsePort : (Ljava/lang/String;II)I
      //   586: istore #6
      //   588: aload_0
      //   589: iload #6
      //   591: putfield port : I
      //   594: iload #6
      //   596: iconst_m1
      //   597: if_icmpeq -> 603
      //   600: goto -> 673
      //   603: new java/lang/StringBuilder
      //   606: dup
      //   607: invokespecial <init> : ()V
      //   610: astore_1
      //   611: aload_1
      //   612: ldc_w 'Invalid URL port: "'
      //   615: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   618: pop
      //   619: aload_1
      //   620: aload_2
      //   621: iload_3
      //   622: iload #7
      //   624: invokevirtual substring : (II)Ljava/lang/String;
      //   627: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   630: pop
      //   631: aload_1
      //   632: bipush #34
      //   634: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   637: pop
      //   638: new java/lang/IllegalArgumentException
      //   641: dup
      //   642: aload_1
      //   643: invokevirtual toString : ()Ljava/lang/String;
      //   646: invokespecial <init> : (Ljava/lang/String;)V
      //   649: athrow
      //   650: aload_0
      //   651: aload_2
      //   652: iload #5
      //   654: iload #4
      //   656: invokestatic canonicalizeHost : (Ljava/lang/String;II)Ljava/lang/String;
      //   659: putfield host : Ljava/lang/String;
      //   662: aload_0
      //   663: aload_0
      //   664: getfield scheme : Ljava/lang/String;
      //   667: invokestatic defaultPort : (Ljava/lang/String;)I
      //   670: putfield port : I
      //   673: aload_0
      //   674: getfield host : Ljava/lang/String;
      //   677: ifnull -> 802
      //   680: iload #7
      //   682: istore #4
      //   684: aload_2
      //   685: iload #4
      //   687: iload #8
      //   689: ldc_w '?#'
      //   692: invokestatic delimiterOffset : (Ljava/lang/String;IILjava/lang/String;)I
      //   695: istore #5
      //   697: aload_0
      //   698: aload_2
      //   699: iload #4
      //   701: iload #5
      //   703: invokespecial resolvePath : (Ljava/lang/String;II)V
      //   706: iload #5
      //   708: istore_3
      //   709: iload #5
      //   711: iload #8
      //   713: if_icmpge -> 764
      //   716: iload #5
      //   718: istore_3
      //   719: aload_2
      //   720: iload #5
      //   722: invokevirtual charAt : (I)C
      //   725: bipush #63
      //   727: if_icmpne -> 764
      //   730: aload_2
      //   731: iload #5
      //   733: iload #8
      //   735: bipush #35
      //   737: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
      //   740: istore_3
      //   741: aload_0
      //   742: aload_2
      //   743: iload #5
      //   745: iconst_1
      //   746: iadd
      //   747: iload_3
      //   748: ldc ' "'<>#'
      //   750: iconst_1
      //   751: iconst_0
      //   752: iconst_1
      //   753: iconst_1
      //   754: aconst_null
      //   755: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZLjava/nio/charset/Charset;)Ljava/lang/String;
      //   758: invokestatic queryStringToNamesAndValues : (Ljava/lang/String;)Ljava/util/List;
      //   761: putfield encodedQueryNamesAndValues : Ljava/util/List;
      //   764: iload_3
      //   765: iload #8
      //   767: if_icmpge -> 800
      //   770: aload_2
      //   771: iload_3
      //   772: invokevirtual charAt : (I)C
      //   775: bipush #35
      //   777: if_icmpne -> 800
      //   780: aload_0
      //   781: aload_2
      //   782: iconst_1
      //   783: iload_3
      //   784: iadd
      //   785: iload #8
      //   787: ldc ''
      //   789: iconst_1
      //   790: iconst_0
      //   791: iconst_0
      //   792: iconst_0
      //   793: aconst_null
      //   794: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZLjava/nio/charset/Charset;)Ljava/lang/String;
      //   797: putfield encodedFragment : Ljava/lang/String;
      //   800: aload_0
      //   801: areturn
      //   802: new java/lang/StringBuilder
      //   805: dup
      //   806: invokespecial <init> : ()V
      //   809: astore_1
      //   810: aload_1
      //   811: ldc_w 'Invalid URL host: "'
      //   814: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   817: pop
      //   818: aload_1
      //   819: aload_2
      //   820: iload #5
      //   822: iload #4
      //   824: invokevirtual substring : (II)Ljava/lang/String;
      //   827: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   830: pop
      //   831: aload_1
      //   832: bipush #34
      //   834: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   837: pop
      //   838: new java/lang/IllegalArgumentException
      //   841: dup
      //   842: aload_1
      //   843: invokevirtual toString : ()Ljava/lang/String;
      //   846: invokespecial <init> : (Ljava/lang/String;)V
      //   849: athrow
      //   850: new java/lang/IllegalArgumentException
      //   853: dup
      //   854: ldc_w 'Expected URL scheme 'http' or 'https' but no colon was found'
      //   857: invokespecial <init> : (Ljava/lang/String;)V
      //   860: athrow
    }
    
    public Builder password(String param1String) {
      Objects.requireNonNull(param1String, "password == null");
      this.encodedPassword = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
      return this;
    }
    
    public Builder port(int param1Int) {
      if (param1Int > 0 && param1Int <= 65535) {
        this.port = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected port: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder query(@Nullable String param1String) {
      if (param1String != null) {
        List<String> list = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(param1String, " \"'<>#", false, false, true, true));
      } else {
        param1String = null;
      } 
      this.encodedQueryNamesAndValues = (List<String>)param1String;
      return this;
    }
    
    public Builder reencodeForUri() {
      int i = this.encodedPathSegments.size();
      boolean bool = false;
      byte b;
      for (b = 0; b < i; b++) {
        String str1 = this.encodedPathSegments.get(b);
        this.encodedPathSegments.set(b, HttpUrl.canonicalize(str1, "[]", true, true, false, true));
      } 
      List<String> list = this.encodedQueryNamesAndValues;
      if (list != null) {
        i = list.size();
        for (b = bool; b < i; b++) {
          String str1 = this.encodedQueryNamesAndValues.get(b);
          if (str1 != null)
            this.encodedQueryNamesAndValues.set(b, HttpUrl.canonicalize(str1, "\\^`{|}", true, true, true, true)); 
        } 
      } 
      String str = this.encodedFragment;
      if (str != null)
        this.encodedFragment = HttpUrl.canonicalize(str, " \"#<>\\^`{|}", true, true, false, false); 
      return this;
    }
    
    public Builder removeAllEncodedQueryParameters(String param1String) {
      Objects.requireNonNull(param1String, "encodedName == null");
      if (this.encodedQueryNamesAndValues == null)
        return this; 
      removeAllCanonicalQueryParameters(HttpUrl.canonicalize(param1String, " \"'<>#&=", true, false, true, true));
      return this;
    }
    
    public Builder removeAllQueryParameters(String param1String) {
      Objects.requireNonNull(param1String, "name == null");
      if (this.encodedQueryNamesAndValues == null)
        return this; 
      removeAllCanonicalQueryParameters(HttpUrl.canonicalize(param1String, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true));
      return this;
    }
    
    public Builder removePathSegment(int param1Int) {
      this.encodedPathSegments.remove(param1Int);
      if (this.encodedPathSegments.isEmpty())
        this.encodedPathSegments.add(""); 
      return this;
    }
    
    public Builder scheme(String param1String) {
      Objects.requireNonNull(param1String, "scheme == null");
      if (param1String.equalsIgnoreCase("http")) {
        this.scheme = "http";
      } else {
        if (param1String.equalsIgnoreCase("https")) {
          this.scheme = "https";
          return this;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected scheme: ");
        stringBuilder.append(param1String);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      return this;
    }
    
    public Builder setEncodedPathSegment(int param1Int, String param1String) {
      Objects.requireNonNull(param1String, "encodedPathSegment == null");
      String str = HttpUrl.canonicalize(param1String, 0, param1String.length(), " \"<>^`{}|/\\?#", true, false, false, true, null);
      this.encodedPathSegments.set(param1Int, str);
      if (!isDot(str) && !isDotDot(str))
        return this; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected path segment: ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder setEncodedQueryParameter(String param1String1, @Nullable String param1String2) {
      removeAllEncodedQueryParameters(param1String1);
      addEncodedQueryParameter(param1String1, param1String2);
      return this;
    }
    
    public Builder setPathSegment(int param1Int, String param1String) {
      Objects.requireNonNull(param1String, "pathSegment == null");
      String str = HttpUrl.canonicalize(param1String, 0, param1String.length(), " \"<>^`{}|/\\?#", false, false, false, true, null);
      if (!isDot(str) && !isDotDot(str)) {
        this.encodedPathSegments.set(param1Int, str);
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected path segment: ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder setQueryParameter(String param1String1, @Nullable String param1String2) {
      removeAllQueryParameters(param1String1);
      addQueryParameter(param1String1, param1String2);
      return this;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      String str = this.scheme;
      if (str != null) {
        stringBuilder.append(str);
        stringBuilder.append("://");
      } else {
        stringBuilder.append("//");
      } 
      if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
        stringBuilder.append(this.encodedUsername);
        if (!this.encodedPassword.isEmpty()) {
          stringBuilder.append(':');
          stringBuilder.append(this.encodedPassword);
        } 
        stringBuilder.append('@');
      } 
      str = this.host;
      if (str != null)
        if (str.indexOf(':') != -1) {
          stringBuilder.append('[');
          stringBuilder.append(this.host);
          stringBuilder.append(']');
        } else {
          stringBuilder.append(this.host);
        }  
      if (this.port != -1 || this.scheme != null) {
        int i = effectivePort();
        str = this.scheme;
        if (str == null || i != HttpUrl.defaultPort(str)) {
          stringBuilder.append(':');
          stringBuilder.append(i);
        } 
      } 
      HttpUrl.pathSegmentsToString(stringBuilder, this.encodedPathSegments);
      if (this.encodedQueryNamesAndValues != null) {
        stringBuilder.append('?');
        HttpUrl.namesAndValuesToQueryString(stringBuilder, this.encodedQueryNamesAndValues);
      } 
      if (this.encodedFragment != null) {
        stringBuilder.append('#');
        stringBuilder.append(this.encodedFragment);
      } 
      return stringBuilder.toString();
    }
    
    public Builder username(String param1String) {
      Objects.requireNonNull(param1String, "username == null");
      this.encodedUsername = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
      return this;
    }
  }
}
