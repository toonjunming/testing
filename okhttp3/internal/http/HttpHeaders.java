package okhttp3.internal.http;

import I丨L.I1I;
import I丨L.iI丨LLL1;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public final class HttpHeaders {
  private static final iI丨LLL1 QUOTED_STRING_DELIMITERS = iI丨LLL1.encodeUtf8("\"\\");
  
  private static final iI丨LLL1 TOKEN_DELIMITERS = iI丨LLL1.encodeUtf8("\t ,=");
  
  public static long contentLength(Headers paramHeaders) {
    return stringToLong(paramHeaders.get("Content-Length"));
  }
  
  public static long contentLength(Response paramResponse) {
    return contentLength(paramResponse.headers());
  }
  
  public static boolean hasBody(Response paramResponse) {
    if (paramResponse.request().method().equals("HEAD"))
      return false; 
    int i = paramResponse.code();
    return ((i < 100 || i >= 200) && i != 204 && i != 304) ? true : ((contentLength(paramResponse) != -1L || "chunked".equalsIgnoreCase(paramResponse.header("Transfer-Encoding"))));
  }
  
  public static boolean hasVaryAll(Headers paramHeaders) {
    return varyFields(paramHeaders).contains("*");
  }
  
  public static boolean hasVaryAll(Response paramResponse) {
    return hasVaryAll(paramResponse.headers());
  }
  
  private static void parseChallengeHeader(List<Challenge> paramList, I1I paramI1I) {
    label51: while (true) {
      String str = null;
      while (true) {
        String str2 = str;
        if (str == null) {
          skipWhitespaceAndCommas(paramI1I);
          str = readToken(paramI1I);
          str2 = str;
          if (str == null)
            return; 
        } 
        boolean bool2 = skipWhitespaceAndCommas(paramI1I);
        String str1 = readToken(paramI1I);
        if (str1 == null) {
          if (!paramI1I.l丨Li1LL())
            return; 
          paramList.add(new Challenge(str2, Collections.emptyMap()));
          return;
        } 
        int i = skipAll(paramI1I, (byte)61);
        boolean bool1 = skipWhitespaceAndCommas(paramI1I);
        if (!bool2 && (bool1 || paramI1I.l丨Li1LL())) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append(repeat('=', i));
          paramList.add(new Challenge(str2, Collections.singletonMap(null, stringBuilder.toString())));
          continue label51;
        } 
        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
        i += skipAll(paramI1I, (byte)61);
        while (true) {
          str = str1;
          str1 = null;
        } 
        break;
      } 
      break;
    } 
  }
  
  public static List<Challenge> parseChallenges(Headers paramHeaders, String paramString) {
    ArrayList<Challenge> arrayList = new ArrayList();
    for (byte b = 0; b < paramHeaders.size(); b++) {
      if (paramString.equalsIgnoreCase(paramHeaders.name(b))) {
        I1I i1I = new I1I();
        i1I.lL(paramHeaders.value(b));
        parseChallengeHeader(arrayList, i1I);
      } 
    } 
    return arrayList;
  }
  
  public static int parseSeconds(String paramString, int paramInt) {
    try {
      long l = Long.parseLong(paramString);
      if (l > 2147483647L)
        return Integer.MAX_VALUE; 
      if (l < 0L)
        return 0; 
      paramInt = (int)l;
    } catch (NumberFormatException numberFormatException) {}
    return paramInt;
  }
  
  private static String readQuotedString(I1I paramI1I) {
    if (paramI1I.readByte() == 34) {
      I1I i1I = new I1I();
      while (true) {
        long l = paramI1I.LI丨丨l丨l(QUOTED_STRING_DELIMITERS);
        if (l == -1L)
          return null; 
        if (paramI1I.丨iI丨丨LLl(l) == 34) {
          i1I.write(paramI1I, l);
          paramI1I.readByte();
          return i1I.iIi1();
        } 
        if (paramI1I.iI1i丨I() == l + 1L)
          return null; 
        i1I.write(paramI1I, l);
        paramI1I.readByte();
        i1I.write(paramI1I, 1L);
      } 
    } 
    throw new IllegalArgumentException();
  }
  
  private static String readToken(I1I paramI1I) {
    try {
      long l2 = paramI1I.LI丨丨l丨l(TOKEN_DELIMITERS);
      long l1 = l2;
      if (l2 == -1L)
        l1 = paramI1I.iI1i丨I(); 
      if (l1 != 0L) {
        String str = paramI1I.IIi丨丨I(l1);
      } else {
        paramI1I = null;
      } 
      return (String)paramI1I;
    } catch (EOFException eOFException) {
      throw new AssertionError();
    } 
  }
  
  public static void receiveHeaders(CookieJar paramCookieJar, HttpUrl paramHttpUrl, Headers paramHeaders) {
    if (paramCookieJar == CookieJar.NO_COOKIES)
      return; 
    List<Cookie> list = Cookie.parseAll(paramHttpUrl, paramHeaders);
    if (list.isEmpty())
      return; 
    paramCookieJar.saveFromResponse(paramHttpUrl, list);
  }
  
  private static String repeat(char paramChar, int paramInt) {
    char[] arrayOfChar = new char[paramInt];
    Arrays.fill(arrayOfChar, paramChar);
    return new String(arrayOfChar);
  }
  
  private static int skipAll(I1I paramI1I, byte paramByte) {
    byte b = 0;
    while (!paramI1I.l丨Li1LL() && paramI1I.丨iI丨丨LLl(0L) == paramByte) {
      b++;
      paramI1I.readByte();
    } 
    return b;
  }
  
  public static int skipUntil(String paramString1, int paramInt, String paramString2) {
    while (paramInt < paramString1.length() && paramString2.indexOf(paramString1.charAt(paramInt)) == -1)
      paramInt++; 
    return paramInt;
  }
  
  public static int skipWhitespace(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (c != ' ' && c != '\t')
        break; 
      paramInt++;
    } 
    return paramInt;
  }
  
  private static boolean skipWhitespaceAndCommas(I1I paramI1I) {
    boolean bool = false;
    while (!paramI1I.l丨Li1LL()) {
      byte b = paramI1I.丨iI丨丨LLl(0L);
      if (b == 44) {
        paramI1I.readByte();
        bool = true;
        continue;
      } 
      if (b == 32 || b == 9)
        paramI1I.readByte(); 
    } 
    return bool;
  }
  
  private static long stringToLong(String paramString) {
    long l = -1L;
    if (paramString == null)
      return -1L; 
    try {
      long l1 = Long.parseLong(paramString);
      l = l1;
    } catch (NumberFormatException numberFormatException) {}
    return l;
  }
  
  public static Set<String> varyFields(Headers paramHeaders) {
    Set<?> set = Collections.emptySet();
    int i = paramHeaders.size();
    for (byte b = 0; b < i; b++) {
      if ("Vary".equalsIgnoreCase(paramHeaders.name(b))) {
        String str = paramHeaders.value(b);
        Set<?> set1 = set;
        if (set.isEmpty())
          set1 = new TreeSet(String.CASE_INSENSITIVE_ORDER); 
        String[] arrayOfString = str.split(",");
        int j = arrayOfString.length;
        byte b1 = 0;
        while (true) {
          set = set1;
          if (b1 < j) {
            set1.add(arrayOfString[b1].trim());
            b1++;
            continue;
          } 
          break;
        } 
      } 
    } 
    return (Set)set;
  }
  
  private static Set<String> varyFields(Response paramResponse) {
    return varyFields(paramResponse.headers());
  }
  
  public static Headers varyHeaders(Headers paramHeaders1, Headers paramHeaders2) {
    Set<String> set = varyFields(paramHeaders2);
    if (set.isEmpty())
      return Util.EMPTY_HEADERS; 
    Headers.Builder builder = new Headers.Builder();
    byte b = 0;
    int i = paramHeaders1.size();
    while (b < i) {
      String str = paramHeaders1.name(b);
      if (set.contains(str))
        builder.add(str, paramHeaders1.value(b)); 
      b++;
    } 
    return builder.build();
  }
  
  public static Headers varyHeaders(Response paramResponse) {
    return varyHeaders(paramResponse.networkResponse().request().headers(), paramResponse.headers());
  }
  
  public static boolean varyMatches(Response paramResponse, Headers paramHeaders, Request paramRequest) {
    for (String str : varyFields(paramResponse)) {
      if (!Objects.equals(paramHeaders.values(str), paramRequest.headers(str)))
        return false; 
    } 
    return true;
  }
}
