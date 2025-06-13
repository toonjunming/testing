package okhttp3.internal;

import I1I.I丨iL.IL1Iii;
import I1I.I丨iL.ILil;
import I丨L.I1I;
import I丨L.IL丨丨l;
import I丨L.iI丨LLL1;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.IDN;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;

public final class Util {
  public static final byte[] EMPTY_BYTE_ARRAY;
  
  public static final Headers EMPTY_HEADERS;
  
  public static final RequestBody EMPTY_REQUEST;
  
  public static final ResponseBody EMPTY_RESPONSE;
  
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  public static final Comparator<String> NATURAL_ORDER;
  
  private static final IL丨丨l UNICODE_BOMS;
  
  public static final TimeZone UTC;
  
  private static final Charset UTF_32BE;
  
  private static final Charset UTF_32LE;
  
  private static final Pattern VERIFY_AS_IP_ADDRESS;
  
  private static final Method addSuppressedExceptionMethod;
  
  static {
    EMPTY_HEADERS = Headers.of(new String[0]);
    Method method = null;
    EMPTY_RESPONSE = ResponseBody.create((MediaType)null, arrayOfByte);
    EMPTY_REQUEST = RequestBody.create((MediaType)null, arrayOfByte);
    UNICODE_BOMS = IL丨丨l.I丨L(new iI丨LLL1[] { iI丨LLL1.decodeHex("efbbbf"), iI丨LLL1.decodeHex("feff"), iI丨LLL1.decodeHex("fffe"), iI丨LLL1.decodeHex("0000ffff"), iI丨LLL1.decodeHex("ffff0000") });
    UTF_32BE = Charset.forName("UTF-32BE");
    UTF_32LE = Charset.forName("UTF-32LE");
    UTC = TimeZone.getTimeZone("GMT");
    NATURAL_ORDER = IL1Iii.IL1Iii;
    try {
      Method method1 = Throwable.class.getDeclaredMethod("addSuppressed", new Class[] { Throwable.class });
      method = method1;
    } catch (Exception exception) {}
    addSuppressedExceptionMethod = method;
    VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
  }
  
  public static void addSuppressedIfPossible(Throwable paramThrowable1, Throwable paramThrowable2) {
    Method method = addSuppressedExceptionMethod;
    if (method != null)
      try {
        method.invoke(paramThrowable1, new Object[] { paramThrowable2 });
      } catch (InvocationTargetException|IllegalAccessException invocationTargetException) {} 
  }
  
  public static Charset bomAwareCharset(l丨Li1LL paraml丨Li1LL, Charset paramCharset) throws IOException {
    int i = paraml丨Li1LL.L11丨丨丨1(UNICODE_BOMS);
    if (i != -1) {
      if (i != 0) {
        if (i != 1) {
          if (i != 2) {
            if (i != 3) {
              if (i == 4)
                return UTF_32LE; 
              throw new AssertionError();
            } 
            return UTF_32BE;
          } 
          return StandardCharsets.UTF_16LE;
        } 
        return StandardCharsets.UTF_16BE;
      } 
      return StandardCharsets.UTF_8;
    } 
    return paramCharset;
  }
  
  public static String canonicalizeHost(String paramString) {
    if (paramString.contains(":")) {
      InetAddress inetAddress;
      if (paramString.startsWith("[") && paramString.endsWith("]")) {
        inetAddress = decodeIpv6(paramString, 1, paramString.length() - 1);
      } else {
        inetAddress = decodeIpv6(paramString, 0, paramString.length());
      } 
      if (inetAddress == null)
        return null; 
      byte[] arrayOfByte = inetAddress.getAddress();
      if (arrayOfByte.length == 16)
        return inet6AddressToAscii(arrayOfByte); 
      if (arrayOfByte.length == 4)
        return inetAddress.getHostAddress(); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Invalid IPv6 address: '");
      stringBuilder.append(paramString);
      stringBuilder.append("'");
      throw new AssertionError(stringBuilder.toString());
    } 
    try {
      paramString = IDN.toASCII(paramString).toLowerCase(Locale.US);
      if (paramString.isEmpty())
        return null; 
      boolean bool = containsInvalidHostnameAsciiCodes(paramString);
      return bool ? null : paramString;
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    } 
  }
  
  public static int checkDuration(String paramString, long paramLong, TimeUnit paramTimeUnit) {
    int i = paramLong cmp 0L;
    if (i >= 0) {
      Objects.requireNonNull(paramTimeUnit, "unit == null");
      paramLong = paramTimeUnit.toMillis(paramLong);
      if (paramLong <= 2147483647L) {
        if (paramLong != 0L || i <= 0)
          return (int)paramLong; 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(paramString);
        stringBuilder2.append(" too small.");
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramString);
      stringBuilder1.append(" too large.");
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" < 0");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static void checkOffsetAndCount(long paramLong1, long paramLong2, long paramLong3) {
    if ((paramLong2 | paramLong3) >= 0L && paramLong2 <= paramLong1 && paramLong1 - paramLong2 >= paramLong3)
      return; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static void closeQuietly(ServerSocket paramServerSocket) {
    if (paramServerSocket != null)
      try {
        paramServerSocket.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static void closeQuietly(Socket paramSocket) {
    if (paramSocket != null)
      try {
        paramSocket.close();
      } catch (AssertionError assertionError) {
        if (!isAndroidGetsocknameError(assertionError))
          throw assertionError; 
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static String[] concat(String[] paramArrayOfString, String paramString) {
    int i = paramArrayOfString.length + 1;
    String[] arrayOfString = new String[i];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    arrayOfString[i - 1] = paramString;
    return arrayOfString;
  }
  
  private static boolean containsInvalidHostnameAsciiCodes(String paramString) {
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c <= '\037' || c >= '')
        return true; 
      if (" #%/:?@[\\]".indexOf(c) != -1)
        return true; 
    } 
    return false;
  }
  
  public static int decodeHexDigit(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9')
      return paramChar - 48; 
    byte b = 97;
    if (paramChar < 'a' || paramChar > 'f') {
      b = 65;
      if (paramChar < 'A' || paramChar > 'F')
        return -1; 
    } 
    return paramChar - b + 10;
  }
  
  private static boolean decodeIpv4Suffix(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) {
    int j = paramInt3;
    int i = paramInt1;
    while (i < paramInt2) {
      if (j == paramArrayOfbyte.length)
        return false; 
      paramInt1 = i;
      if (j != paramInt3) {
        if (paramString.charAt(i) != '.')
          return false; 
        paramInt1 = i + 1;
      } 
      i = paramInt1;
      int k = 0;
      while (i < paramInt2) {
        char c = paramString.charAt(i);
        if (c < '0' || c > '9')
          break; 
        if (!k && paramInt1 != i)
          return false; 
        k = k * 10 + c - 48;
        if (k > 255)
          return false; 
        i++;
      } 
      if (i - paramInt1 == 0)
        return false; 
      paramArrayOfbyte[j] = (byte)k;
      j++;
    } 
    return !(j != paramInt3 + 4);
  }
  
  @Nullable
  private static InetAddress decodeIpv6(String paramString, int paramInt1, int paramInt2) {
    int j;
    int n;
    byte[] arrayOfByte = new byte[16];
    int i = 0;
    int k = -1;
    int m = -1;
    while (true) {
      j = i;
      n = k;
      if (paramInt1 < paramInt2) {
        if (i == 16)
          return null; 
        n = paramInt1 + 2;
        if (n <= paramInt2 && paramString.regionMatches(paramInt1, "::", 0, 2)) {
          if (k != -1)
            return null; 
          j = i + 2;
          paramInt1 = j;
          if (n == paramInt2) {
            n = paramInt1;
            break;
          } 
          m = n;
          i = j;
          k = paramInt1;
          paramInt1 = m;
        } else {
          j = paramInt1;
          if (i != 0)
            if (paramString.regionMatches(paramInt1, ":", 0, 1)) {
              j = paramInt1 + 1;
            } else {
              if (paramString.regionMatches(paramInt1, ".", 0, 1)) {
                if (!decodeIpv4Suffix(paramString, m, paramInt2, arrayOfByte, i - 2))
                  return null; 
                j = i + 2;
                n = k;
                break;
              } 
              return null;
            }  
          paramInt1 = j;
        } 
        j = paramInt1;
        m = 0;
        while (j < paramInt2) {
          n = decodeHexDigit(paramString.charAt(j));
          if (n == -1)
            break; 
          m = (m << 4) + n;
          j++;
        } 
        n = j - paramInt1;
        if (n == 0 || n > 4)
          return null; 
        n = i + 1;
        arrayOfByte[i] = (byte)(m >>> 8 & 0xFF);
        i = n + 1;
        arrayOfByte[n] = (byte)(m & 0xFF);
        m = paramInt1;
        paramInt1 = j;
        continue;
      } 
      break;
    } 
    if (j != 16) {
      if (n == -1)
        return null; 
      paramInt1 = j - n;
      System.arraycopy(arrayOfByte, n, arrayOfByte, 16 - paramInt1, paramInt1);
      Arrays.fill(arrayOfByte, n, 16 - j + n, (byte)0);
    } 
    try {
      return InetAddress.getByAddress(arrayOfByte);
    } catch (UnknownHostException unknownHostException) {
      throw new AssertionError();
    } 
  }
  
  public static int delimiterOffset(String paramString, int paramInt1, int paramInt2, char paramChar) {
    while (paramInt1 < paramInt2) {
      if (paramString.charAt(paramInt1) == paramChar)
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  public static int delimiterOffset(String paramString1, int paramInt1, int paramInt2, String paramString2) {
    while (paramInt1 < paramInt2) {
      if (paramString2.indexOf(paramString1.charAt(paramInt1)) != -1)
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  public static boolean discard(丨lL param丨lL, int paramInt, TimeUnit paramTimeUnit) {
    try {
      return skipAll(param丨lL, paramInt, paramTimeUnit);
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public static String format(String paramString, Object... paramVarArgs) {
    return String.format(Locale.US, paramString, paramVarArgs);
  }
  
  public static String getSystemProperty(String paramString1, @Nullable String paramString2) {
    try {
      paramString1 = System.getProperty(paramString1);
      if (paramString1 != null)
        paramString2 = paramString1; 
    } catch (AccessControlException accessControlException) {}
    return paramString2;
  }
  
  public static String hostHeader(HttpUrl paramHttpUrl, boolean paramBoolean) {
    String str;
    if (paramHttpUrl.host().contains(":")) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("[");
      stringBuilder1.append(paramHttpUrl.host());
      stringBuilder1.append("]");
      str = stringBuilder1.toString();
    } else {
      str = paramHttpUrl.host();
    } 
    if (!paramBoolean) {
      String str1 = str;
      if (paramHttpUrl.port() != HttpUrl.defaultPort(paramHttpUrl.scheme())) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append(":");
        stringBuilder1.append(paramHttpUrl.port());
        return stringBuilder1.toString();
      } 
      return str1;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(":");
    stringBuilder.append(paramHttpUrl.port());
    return stringBuilder.toString();
  }
  
  public static <T> List<T> immutableList(List<T> paramList) {
    return Collections.unmodifiableList(new ArrayList<T>(paramList));
  }
  
  @SafeVarargs
  public static <T> List<T> immutableList(T... paramVarArgs) {
    return Collections.unmodifiableList(Arrays.asList((T[])paramVarArgs.clone()));
  }
  
  public static <K, V> Map<K, V> immutableMap(Map<K, V> paramMap) {
    if (paramMap.isEmpty()) {
      paramMap = Collections.emptyMap();
    } else {
      paramMap = Collections.unmodifiableMap(new LinkedHashMap<K, V>(paramMap));
    } 
    return paramMap;
  }
  
  public static int indexOf(Comparator<String> paramComparator, String[] paramArrayOfString, String paramString) {
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      if (paramComparator.compare(paramArrayOfString[b], paramString) == 0)
        return b; 
    } 
    return -1;
  }
  
  public static int indexOfControlOrNonAscii(String paramString) {
    int i = paramString.length();
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (c <= '\037' || c >= '')
        return b; 
    } 
    return -1;
  }
  
  private static String inet6AddressToAscii(byte[] paramArrayOfbyte) {
    boolean bool = false;
    int k = -1;
    int i = 0;
    int j;
    for (j = 0; i < paramArrayOfbyte.length; j = n) {
      int m;
      for (m = i; m < 16 && paramArrayOfbyte[m] == 0 && paramArrayOfbyte[m + 1] == 0; m += 2);
      int i2 = m - i;
      int i1 = k;
      int n = j;
      if (i2 > j) {
        i1 = k;
        n = j;
        if (i2 >= 4) {
          n = i2;
          i1 = i;
        } 
      } 
      i = m + 2;
      k = i1;
    } 
    I1I i1I = new I1I();
    for (i = bool; i < paramArrayOfbyte.length; i += 2) {
      if (i == k) {
        i1I.iI(58);
        int m = i + j;
        i = m;
        if (m == 16) {
          i1I.iI(58);
          i = m;
        } 
        continue;
      } 
      if (i > 0)
        i1I.iI(58); 
      i1I.I丨(((paramArrayOfbyte[i] & 0xFF) << 8 | paramArrayOfbyte[i + 1] & 0xFF));
    } 
    return i1I.iIi1();
  }
  
  public static String[] intersect(Comparator<? super String> paramComparator, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    ArrayList<String> arrayList = new ArrayList();
    int i = paramArrayOfString1.length;
    for (byte b = 0; b < i; b++) {
      String str = paramArrayOfString1[b];
      int j = paramArrayOfString2.length;
      for (byte b1 = 0; b1 < j; b1++) {
        if (paramComparator.compare(str, paramArrayOfString2[b1]) == 0) {
          arrayList.add(str);
          break;
        } 
      } 
    } 
    return arrayList.<String>toArray(new String[arrayList.size()]);
  }
  
  public static boolean isAndroidGetsocknameError(AssertionError paramAssertionError) {
    boolean bool;
    if (paramAssertionError.getCause() != null && paramAssertionError.getMessage() != null && paramAssertionError.getMessage().contains("getsockname failed")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean nonEmptyIntersection(Comparator<String> paramComparator, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    if (paramArrayOfString1 != null && paramArrayOfString2 != null && paramArrayOfString1.length != 0 && paramArrayOfString2.length != 0) {
      int i = paramArrayOfString1.length;
      for (byte b = 0; b < i; b++) {
        String str = paramArrayOfString1[b];
        int j = paramArrayOfString2.length;
        for (byte b1 = 0; b1 < j; b1++) {
          if (paramComparator.compare(str, paramArrayOfString2[b1]) == 0)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  public static X509TrustManager platformTrustManager() {
    try {
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init((KeyStore)null);
      TrustManager[] arrayOfTrustManager = trustManagerFactory.getTrustManagers();
      if (arrayOfTrustManager.length == 1 && arrayOfTrustManager[0] instanceof X509TrustManager)
        return (X509TrustManager)arrayOfTrustManager[0]; 
      IllegalStateException illegalStateException = new IllegalStateException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unexpected default trust managers:");
      stringBuilder.append(Arrays.toString((Object[])arrayOfTrustManager));
      this(stringBuilder.toString());
      throw illegalStateException;
    } catch (GeneralSecurityException generalSecurityException) {
      throw new AssertionError("No System TLS", generalSecurityException);
    } 
  }
  
  public static boolean sameConnection(HttpUrl paramHttpUrl1, HttpUrl paramHttpUrl2) {
    boolean bool;
    if (paramHttpUrl1.host().equals(paramHttpUrl2.host()) && paramHttpUrl1.port() == paramHttpUrl2.port() && paramHttpUrl1.scheme().equals(paramHttpUrl2.scheme())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean skipAll(丨lL param丨lL, int paramInt, TimeUnit paramTimeUnit) throws IOException {
    long l1;
    long l2 = System.nanoTime();
    if (param丨lL.timeout().hasDeadline()) {
      l1 = param丨lL.timeout().deadlineNanoTime() - l2;
    } else {
      l1 = Long.MAX_VALUE;
    } 
    param丨lL.timeout().deadlineNanoTime(Math.min(l1, paramTimeUnit.toNanos(paramInt)) + l2);
    try {
      I1I i1I = new I1I();
      this();
      while (param丨lL.read(i1I, 8192L) != -1L)
        i1I.丨丨LLlI1(); 
      return true;
    } catch (InterruptedIOException interruptedIOException) {
      return false;
    } finally {
      if (l1 == Long.MAX_VALUE) {
        param丨lL.timeout().clearDeadline();
      } else {
        param丨lL.timeout().deadlineNanoTime(l2 + l1);
      } 
    } 
  }
  
  public static int skipLeadingAsciiWhitespace(String paramString, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      char c = paramString.charAt(paramInt1);
      if (c != '\t' && c != '\n' && c != '\f' && c != '\r' && c != ' ')
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  public static int skipTrailingAsciiWhitespace(String paramString, int paramInt1, int paramInt2) {
    while (--paramInt2 >= paramInt1) {
      char c = paramString.charAt(paramInt2);
      if (c != '\t' && c != '\n' && c != '\f' && c != '\r' && c != ' ')
        return paramInt2 + 1; 
      paramInt2--;
    } 
    return paramInt1;
  }
  
  public static ThreadFactory threadFactory(String paramString, boolean paramBoolean) {
    return new ILil(paramString, paramBoolean);
  }
  
  public static List<Header> toHeaderBlock(Headers paramHeaders) {
    ArrayList<Header> arrayList = new ArrayList();
    for (byte b = 0; b < paramHeaders.size(); b++)
      arrayList.add(new Header(paramHeaders.name(b), paramHeaders.value(b))); 
    return arrayList;
  }
  
  public static Headers toHeaders(List<Header> paramList) {
    Headers.Builder builder = new Headers.Builder();
    for (Header header : paramList)
      Internal.instance.addLenient(builder, header.name.utf8(), header.value.utf8()); 
    return builder.build();
  }
  
  public static String trimSubstring(String paramString, int paramInt1, int paramInt2) {
    paramInt1 = skipLeadingAsciiWhitespace(paramString, paramInt1, paramInt2);
    return paramString.substring(paramInt1, skipTrailingAsciiWhitespace(paramString, paramInt1, paramInt2));
  }
  
  public static boolean verifyAsIpAddress(String paramString) {
    return VERIFY_AS_IP_ADDRESS.matcher(paramString).matches();
  }
  
  static {
    byte[] arrayOfByte = new byte[0];
    EMPTY_BYTE_ARRAY = arrayOfByte;
  }
}
