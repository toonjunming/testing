package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie {
  private static final Pattern DAY_OF_MONTH_PATTERN;
  
  private static final Pattern MONTH_PATTERN;
  
  private static final Pattern TIME_PATTERN;
  
  private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
  
  private final String domain;
  
  private final long expiresAt;
  
  private final boolean hostOnly;
  
  private final boolean httpOnly;
  
  private final String name;
  
  private final String path;
  
  private final boolean persistent;
  
  private final boolean secure;
  
  private final String value;
  
  static {
    MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
  }
  
  private Cookie(String paramString1, String paramString2, long paramLong, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    this.name = paramString1;
    this.value = paramString2;
    this.expiresAt = paramLong;
    this.domain = paramString3;
    this.path = paramString4;
    this.secure = paramBoolean1;
    this.httpOnly = paramBoolean2;
    this.hostOnly = paramBoolean3;
    this.persistent = paramBoolean4;
  }
  
  public Cookie(Builder paramBuilder) {
    String str3 = paramBuilder.name;
    Objects.requireNonNull(str3, "builder.name == null");
    String str2 = paramBuilder.value;
    Objects.requireNonNull(str2, "builder.value == null");
    String str1 = paramBuilder.domain;
    Objects.requireNonNull(str1, "builder.domain == null");
    this.name = str3;
    this.value = str2;
    this.expiresAt = paramBuilder.expiresAt;
    this.domain = str1;
    this.path = paramBuilder.path;
    this.secure = paramBuilder.secure;
    this.httpOnly = paramBuilder.httpOnly;
    this.persistent = paramBuilder.persistent;
    this.hostOnly = paramBuilder.hostOnly;
  }
  
  private static int dateCharacterOffset(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    while (paramInt1 < paramInt2) {
      char c = paramString.charAt(paramInt1);
      if ((c < ' ' && c != '\t') || c >= '' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ':') {
        c = '\001';
      } else {
        c = Character.MIN_VALUE;
      } 
      if (c == (paramBoolean ^ true))
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  private static boolean domainMatch(String paramString1, String paramString2) {
    return paramString1.equals(paramString2) ? true : ((paramString1.endsWith(paramString2) && paramString1.charAt(paramString1.length() - paramString2.length() - 1) == '.' && !Util.verifyAsIpAddress(paramString1)));
  }
  
  @Nullable
  public static Cookie parse(long paramLong, HttpUrl paramHttpUrl, String paramString) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual length : ()I
    //   4: istore #5
    //   6: aload_3
    //   7: iconst_0
    //   8: iload #5
    //   10: bipush #59
    //   12: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   15: istore #6
    //   17: aload_3
    //   18: iconst_0
    //   19: iload #6
    //   21: bipush #61
    //   23: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   26: istore #4
    //   28: iload #4
    //   30: iload #6
    //   32: if_icmpne -> 37
    //   35: aconst_null
    //   36: areturn
    //   37: aload_3
    //   38: iconst_0
    //   39: iload #4
    //   41: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   44: astore #27
    //   46: aload #27
    //   48: invokevirtual isEmpty : ()Z
    //   51: ifne -> 725
    //   54: aload #27
    //   56: invokestatic indexOfControlOrNonAscii : (Ljava/lang/String;)I
    //   59: iconst_m1
    //   60: if_icmpeq -> 66
    //   63: goto -> 725
    //   66: aload_3
    //   67: iload #4
    //   69: iconst_1
    //   70: iadd
    //   71: iload #6
    //   73: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   76: astore #28
    //   78: aload #28
    //   80: invokestatic indexOfControlOrNonAscii : (Ljava/lang/String;)I
    //   83: iconst_m1
    //   84: if_icmpeq -> 89
    //   87: aconst_null
    //   88: areturn
    //   89: iload #6
    //   91: iconst_1
    //   92: iadd
    //   93: istore #4
    //   95: aconst_null
    //   96: astore #23
    //   98: aconst_null
    //   99: astore #24
    //   101: ldc2_w -1
    //   104: lstore #10
    //   106: ldc2_w 253402300799999
    //   109: lstore #8
    //   111: iconst_0
    //   112: istore #19
    //   114: iconst_0
    //   115: istore #18
    //   117: iconst_1
    //   118: istore #17
    //   120: iconst_0
    //   121: istore #16
    //   123: iload #4
    //   125: iload #5
    //   127: if_icmpge -> 497
    //   130: aload_3
    //   131: iload #4
    //   133: iload #5
    //   135: bipush #59
    //   137: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   140: istore #6
    //   142: aload_3
    //   143: iload #4
    //   145: iload #6
    //   147: bipush #61
    //   149: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   152: istore #7
    //   154: aload_3
    //   155: iload #4
    //   157: iload #7
    //   159: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   162: astore #29
    //   164: iload #7
    //   166: iload #6
    //   168: if_icmpge -> 186
    //   171: aload_3
    //   172: iload #7
    //   174: iconst_1
    //   175: iadd
    //   176: iload #6
    //   178: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   181: astore #25
    //   183: goto -> 190
    //   186: ldc ''
    //   188: astore #25
    //   190: aload #29
    //   192: ldc 'expires'
    //   194: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   197: ifeq -> 220
    //   200: aload #25
    //   202: iconst_0
    //   203: aload #25
    //   205: invokevirtual length : ()I
    //   208: invokestatic parseExpires : (Ljava/lang/String;II)J
    //   211: lstore #12
    //   213: lload #12
    //   215: lstore #8
    //   217: goto -> 241
    //   220: aload #29
    //   222: ldc 'max-age'
    //   224: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   227: ifeq -> 271
    //   230: aload #25
    //   232: invokestatic parseMaxAge : (Ljava/lang/String;)J
    //   235: lstore #12
    //   237: lload #12
    //   239: lstore #10
    //   241: iconst_1
    //   242: istore #22
    //   244: aload #23
    //   246: astore #25
    //   248: aload #24
    //   250: astore #26
    //   252: iload #19
    //   254: istore #20
    //   256: iload #17
    //   258: istore #21
    //   260: lload #10
    //   262: lstore #12
    //   264: lload #8
    //   266: lstore #14
    //   268: goto -> 460
    //   271: aload #29
    //   273: ldc 'domain'
    //   275: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   278: ifeq -> 314
    //   281: aload #25
    //   283: invokestatic parseDomain : (Ljava/lang/String;)Ljava/lang/String;
    //   286: astore #26
    //   288: iconst_0
    //   289: istore #21
    //   291: aload #23
    //   293: astore #25
    //   295: iload #19
    //   297: istore #20
    //   299: iload #16
    //   301: istore #22
    //   303: lload #10
    //   305: lstore #12
    //   307: lload #8
    //   309: lstore #14
    //   311: goto -> 460
    //   314: aload #29
    //   316: ldc 'path'
    //   318: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   321: ifeq -> 351
    //   324: aload #24
    //   326: astore #26
    //   328: iload #19
    //   330: istore #20
    //   332: iload #17
    //   334: istore #21
    //   336: iload #16
    //   338: istore #22
    //   340: lload #10
    //   342: lstore #12
    //   344: lload #8
    //   346: lstore #14
    //   348: goto -> 460
    //   351: aload #29
    //   353: ldc 'secure'
    //   355: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   358: ifeq -> 391
    //   361: iconst_1
    //   362: istore #20
    //   364: aload #23
    //   366: astore #25
    //   368: aload #24
    //   370: astore #26
    //   372: iload #17
    //   374: istore #21
    //   376: iload #16
    //   378: istore #22
    //   380: lload #10
    //   382: lstore #12
    //   384: lload #8
    //   386: lstore #14
    //   388: goto -> 460
    //   391: aload #23
    //   393: astore #25
    //   395: aload #24
    //   397: astore #26
    //   399: iload #19
    //   401: istore #20
    //   403: iload #17
    //   405: istore #21
    //   407: iload #16
    //   409: istore #22
    //   411: lload #10
    //   413: lstore #12
    //   415: lload #8
    //   417: lstore #14
    //   419: aload #29
    //   421: ldc 'httponly'
    //   423: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   426: ifeq -> 460
    //   429: iconst_1
    //   430: istore #18
    //   432: lload #8
    //   434: lstore #14
    //   436: lload #10
    //   438: lstore #12
    //   440: iload #16
    //   442: istore #22
    //   444: iload #17
    //   446: istore #21
    //   448: iload #19
    //   450: istore #20
    //   452: aload #24
    //   454: astore #26
    //   456: aload #23
    //   458: astore #25
    //   460: iload #6
    //   462: iconst_1
    //   463: iadd
    //   464: istore #4
    //   466: aload #25
    //   468: astore #23
    //   470: aload #26
    //   472: astore #24
    //   474: iload #20
    //   476: istore #19
    //   478: iload #21
    //   480: istore #17
    //   482: iload #22
    //   484: istore #16
    //   486: lload #12
    //   488: lstore #10
    //   490: lload #14
    //   492: lstore #8
    //   494: goto -> 123
    //   497: ldc2_w -9223372036854775808
    //   500: lstore #12
    //   502: lload #10
    //   504: ldc2_w -9223372036854775808
    //   507: lcmp
    //   508: ifne -> 517
    //   511: lload #12
    //   513: lstore_0
    //   514: goto -> 586
    //   517: lload #10
    //   519: ldc2_w -1
    //   522: lcmp
    //   523: ifeq -> 583
    //   526: lload #10
    //   528: ldc2_w 9223372036854775
    //   531: lcmp
    //   532: ifgt -> 546
    //   535: lload #10
    //   537: ldc2_w 1000
    //   540: lmul
    //   541: lstore #8
    //   543: goto -> 551
    //   546: ldc2_w 9223372036854775807
    //   549: lstore #8
    //   551: lload_0
    //   552: lload #8
    //   554: ladd
    //   555: lstore #8
    //   557: lload #8
    //   559: lload_0
    //   560: lcmp
    //   561: iflt -> 576
    //   564: lload #8
    //   566: lstore_0
    //   567: lload #8
    //   569: ldc2_w 253402300799999
    //   572: lcmp
    //   573: ifle -> 514
    //   576: ldc2_w 253402300799999
    //   579: lstore_0
    //   580: goto -> 586
    //   583: lload #8
    //   585: lstore_0
    //   586: aload_2
    //   587: invokevirtual host : ()Ljava/lang/String;
    //   590: astore #25
    //   592: aload #24
    //   594: ifnonnull -> 603
    //   597: aload #25
    //   599: astore_3
    //   600: goto -> 618
    //   603: aload #25
    //   605: aload #24
    //   607: invokestatic domainMatch : (Ljava/lang/String;Ljava/lang/String;)Z
    //   610: ifne -> 615
    //   613: aconst_null
    //   614: areturn
    //   615: aload #24
    //   617: astore_3
    //   618: aload #25
    //   620: invokevirtual length : ()I
    //   623: aload_3
    //   624: invokevirtual length : ()I
    //   627: if_icmpeq -> 642
    //   630: invokestatic get : ()Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;
    //   633: aload_3
    //   634: invokevirtual getEffectiveTldPlusOne : (Ljava/lang/String;)Ljava/lang/String;
    //   637: ifnonnull -> 642
    //   640: aconst_null
    //   641: areturn
    //   642: ldc '/'
    //   644: astore #24
    //   646: aload #23
    //   648: ifnull -> 670
    //   651: aload #23
    //   653: ldc '/'
    //   655: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   658: ifne -> 664
    //   661: goto -> 670
    //   664: aload #23
    //   666: astore_2
    //   667: goto -> 702
    //   670: aload_2
    //   671: invokevirtual encodedPath : ()Ljava/lang/String;
    //   674: astore #23
    //   676: aload #23
    //   678: bipush #47
    //   680: invokevirtual lastIndexOf : (I)I
    //   683: istore #4
    //   685: aload #24
    //   687: astore_2
    //   688: iload #4
    //   690: ifeq -> 702
    //   693: aload #23
    //   695: iconst_0
    //   696: iload #4
    //   698: invokevirtual substring : (II)Ljava/lang/String;
    //   701: astore_2
    //   702: new okhttp3/Cookie
    //   705: dup
    //   706: aload #27
    //   708: aload #28
    //   710: lload_0
    //   711: aload_3
    //   712: aload_2
    //   713: iload #19
    //   715: iload #18
    //   717: iload #17
    //   719: iload #16
    //   721: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V
    //   724: areturn
    //   725: aconst_null
    //   726: areturn
    //   727: astore #25
    //   729: aload #23
    //   731: astore #25
    //   733: aload #24
    //   735: astore #26
    //   737: iload #19
    //   739: istore #20
    //   741: iload #17
    //   743: istore #21
    //   745: iload #16
    //   747: istore #22
    //   749: lload #10
    //   751: lstore #12
    //   753: lload #8
    //   755: lstore #14
    //   757: goto -> 460
    // Exception table:
    //   from	to	target	type
    //   200	213	727	java/lang/IllegalArgumentException
    //   230	237	727	java/lang/NumberFormatException
    //   281	288	727	java/lang/IllegalArgumentException
  }
  
  @Nullable
  public static Cookie parse(HttpUrl paramHttpUrl, String paramString) {
    return parse(System.currentTimeMillis(), paramHttpUrl, paramString);
  }
  
  public static List<Cookie> parseAll(HttpUrl paramHttpUrl, Headers paramHeaders) {
    List<?> list;
    ArrayList<Cookie> arrayList;
    List<String> list1 = paramHeaders.values("Set-Cookie");
    int i = list1.size();
    paramHeaders = null;
    for (byte b = 0; b < i; b++) {
      Cookie cookie = parse(paramHttpUrl, list1.get(b));
      if (cookie != null) {
        ArrayList<Cookie> arrayList1;
        Headers headers = paramHeaders;
        if (paramHeaders == null)
          arrayList1 = new ArrayList(); 
        arrayList1.add(cookie);
        arrayList = arrayList1;
      } 
    } 
    if (arrayList != null) {
      list = Collections.unmodifiableList(arrayList);
    } else {
      list = Collections.emptyList();
    } 
    return (List)list;
  }
  
  private static String parseDomain(String paramString) {
    if (!paramString.endsWith(".")) {
      String str = paramString;
      if (paramString.startsWith("."))
        str = paramString.substring(1); 
      paramString = Util.canonicalizeHost(str);
      if (paramString != null)
        return paramString; 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  private static long parseExpires(String paramString, int paramInt1, int paramInt2) {
    Object object1;
    int j;
    Object object3;
    Object object4;
    Object object5;
    Object object6;
    int m = dateCharacterOffset(paramString, paramInt1, paramInt2, false);
    Matcher matcher = TIME_PATTERN.matcher(paramString);
    paramInt1 = -1;
    int k = -1;
    byte b4 = -1;
    byte b1 = -1;
    byte b3 = -1;
    byte b2 = -1;
    while (m < paramInt2) {
      int n = dateCharacterOffset(paramString, m + 1, paramInt2, true);
      matcher.region(m, n);
      if (k == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
        m = Integer.parseInt(matcher.group(1));
        int i1 = Integer.parseInt(matcher.group(2));
        int i2 = Integer.parseInt(matcher.group(3));
        Object object12 = object1;
        Object object13 = object6;
        Object object14 = object3;
        continue;
      } 
      if (object6 == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
        int i1 = Integer.parseInt(matcher.group(1));
        Object object12 = object1;
        m = k;
        Object object13 = object3;
        Object object14 = object5;
        Object object15 = object4;
        continue;
      } 
      if (object3 == -1) {
        Pattern pattern = MONTH_PATTERN;
        if (matcher.usePattern(pattern).matches()) {
          String str = matcher.group(1).toLowerCase(Locale.US);
          int i1 = pattern.pattern().indexOf(str) / 4;
          Object object12 = object1;
          m = k;
          Object object13 = object6;
          Object object14 = object5;
          Object object15 = object4;
          continue;
        } 
      } 
      Object object7 = object1;
      m = k;
      Object object8 = object6;
      Object object9 = object3;
      Object object10 = object5;
      Object object11 = object4;
      if (object1 == -1) {
        object7 = object1;
        m = k;
        object8 = object6;
        object9 = object3;
        object10 = object5;
        object11 = object4;
        if (matcher.usePattern(YEAR_PATTERN).matches()) {
          int i1 = Integer.parseInt(matcher.group(1));
          object11 = object4;
          object10 = object5;
          object9 = object3;
          object8 = object6;
          m = k;
        } 
      } 
      continue;
      n = dateCharacterOffset(paramString, SYNTHETIC_LOCAL_VARIABLE_14 + 1, paramInt2, false);
      object1 = SYNTHETIC_LOCAL_VARIABLE_9;
      k = m;
      object6 = SYNTHETIC_LOCAL_VARIABLE_10;
      object3 = SYNTHETIC_LOCAL_VARIABLE_11;
      object5 = SYNTHETIC_LOCAL_VARIABLE_12;
      object4 = SYNTHETIC_LOCAL_VARIABLE_13;
      m = n;
    } 
    Object object2 = object1;
    if (object1 >= 70) {
      object2 = object1;
      if (object1 <= 99)
        j = object1 + 1900; 
    } 
    int i = j;
    if (j >= 0) {
      i = j;
      if (j <= 69)
        i = j + 2000; 
    } 
    if (i >= 1601) {
      if (object3 != -1) {
        if (object6 >= true && object6 <= 31) {
          if (k >= 0 && k <= 23) {
            if (object5 >= null && object5 <= 59) {
              if (object4 >= null && object4 <= 59) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
                gregorianCalendar.setLenient(false);
                gregorianCalendar.set(1, i);
                gregorianCalendar.set(2, object3 - 1);
                gregorianCalendar.set(5, object6);
                gregorianCalendar.set(11, k);
                gregorianCalendar.set(12, object5);
                gregorianCalendar.set(13, object4);
                gregorianCalendar.set(14, 0);
                return gregorianCalendar.getTimeInMillis();
              } 
              throw new IllegalArgumentException();
            } 
            throw new IllegalArgumentException();
          } 
          throw new IllegalArgumentException();
        } 
        throw new IllegalArgumentException();
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  private static long parseMaxAge(String paramString) {
    long l = Long.MIN_VALUE;
    try {
      long l1 = Long.parseLong(paramString);
      if (l1 > 0L)
        l = l1; 
      return l;
    } catch (NumberFormatException numberFormatException) {
      if (paramString.matches("-?\\d+")) {
        if (!paramString.startsWith("-"))
          l = Long.MAX_VALUE; 
        return l;
      } 
      throw numberFormatException;
    } 
  }
  
  private static boolean pathMatch(HttpUrl paramHttpUrl, String paramString) {
    String str = paramHttpUrl.encodedPath();
    if (str.equals(paramString))
      return true; 
    if (str.startsWith(paramString)) {
      if (paramString.endsWith("/"))
        return true; 
      if (str.charAt(paramString.length()) == '/')
        return true; 
    } 
    return false;
  }
  
  public String domain() {
    return this.domain;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool = paramObject instanceof Cookie;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    bool = bool1;
    if (((Cookie)paramObject).name.equals(this.name)) {
      bool = bool1;
      if (((Cookie)paramObject).value.equals(this.value)) {
        bool = bool1;
        if (((Cookie)paramObject).domain.equals(this.domain)) {
          bool = bool1;
          if (((Cookie)paramObject).path.equals(this.path)) {
            bool = bool1;
            if (((Cookie)paramObject).expiresAt == this.expiresAt) {
              bool = bool1;
              if (((Cookie)paramObject).secure == this.secure) {
                bool = bool1;
                if (((Cookie)paramObject).httpOnly == this.httpOnly) {
                  bool = bool1;
                  if (((Cookie)paramObject).persistent == this.persistent) {
                    bool = bool1;
                    if (((Cookie)paramObject).hostOnly == this.hostOnly)
                      bool = true; 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool;
  }
  
  public long expiresAt() {
    return this.expiresAt;
  }
  
  public int hashCode() {
    int i = this.name.hashCode();
    int j = this.value.hashCode();
    int m = this.domain.hashCode();
    int k = this.path.hashCode();
    long l = this.expiresAt;
    return ((((((((527 + i) * 31 + j) * 31 + m) * 31 + k) * 31 + (int)(l ^ l >>> 32L)) * 31 + (this.secure ^ true)) * 31 + (this.httpOnly ^ true)) * 31 + (this.persistent ^ true)) * 31 + (this.hostOnly ^ true);
  }
  
  public boolean hostOnly() {
    return this.hostOnly;
  }
  
  public boolean httpOnly() {
    return this.httpOnly;
  }
  
  public boolean matches(HttpUrl paramHttpUrl) {
    boolean bool;
    if (this.hostOnly) {
      bool = paramHttpUrl.host().equals(this.domain);
    } else {
      bool = domainMatch(paramHttpUrl.host(), this.domain);
    } 
    return !bool ? false : (!pathMatch(paramHttpUrl, this.path) ? false : (!(this.secure && !paramHttpUrl.isHttps())));
  }
  
  public String name() {
    return this.name;
  }
  
  public String path() {
    return this.path;
  }
  
  public boolean persistent() {
    return this.persistent;
  }
  
  public boolean secure() {
    return this.secure;
  }
  
  public String toString() {
    return toString(false);
  }
  
  public String toString(boolean paramBoolean) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.name);
    stringBuilder.append('=');
    stringBuilder.append(this.value);
    if (this.persistent)
      if (this.expiresAt == Long.MIN_VALUE) {
        stringBuilder.append("; max-age=0");
      } else {
        stringBuilder.append("; expires=");
        stringBuilder.append(HttpDate.format(new Date(this.expiresAt)));
      }  
    if (!this.hostOnly) {
      stringBuilder.append("; domain=");
      if (paramBoolean)
        stringBuilder.append("."); 
      stringBuilder.append(this.domain);
    } 
    stringBuilder.append("; path=");
    stringBuilder.append(this.path);
    if (this.secure)
      stringBuilder.append("; secure"); 
    if (this.httpOnly)
      stringBuilder.append("; httponly"); 
    return stringBuilder.toString();
  }
  
  public String value() {
    return this.value;
  }
  
  public static final class Builder {
    @Nullable
    public String domain;
    
    public long expiresAt = 253402300799999L;
    
    public boolean hostOnly;
    
    public boolean httpOnly;
    
    @Nullable
    public String name;
    
    public String path = "/";
    
    public boolean persistent;
    
    public boolean secure;
    
    @Nullable
    public String value;
    
    private Builder domain(String param1String, boolean param1Boolean) {
      Objects.requireNonNull(param1String, "domain == null");
      String str = Util.canonicalizeHost(param1String);
      if (str != null) {
        this.domain = str;
        this.hostOnly = param1Boolean;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected domain: ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Cookie build() {
      return new Cookie(this);
    }
    
    public Builder domain(String param1String) {
      return domain(param1String, false);
    }
    
    public Builder expiresAt(long param1Long) {
      long l = param1Long;
      if (param1Long <= 0L)
        l = Long.MIN_VALUE; 
      param1Long = l;
      if (l > 253402300799999L)
        param1Long = 253402300799999L; 
      this.expiresAt = param1Long;
      this.persistent = true;
      return this;
    }
    
    public Builder hostOnlyDomain(String param1String) {
      return domain(param1String, true);
    }
    
    public Builder httpOnly() {
      this.httpOnly = true;
      return this;
    }
    
    public Builder name(String param1String) {
      Objects.requireNonNull(param1String, "name == null");
      if (param1String.trim().equals(param1String)) {
        this.name = param1String;
        return this;
      } 
      throw new IllegalArgumentException("name is not trimmed");
    }
    
    public Builder path(String param1String) {
      if (param1String.startsWith("/")) {
        this.path = param1String;
        return this;
      } 
      throw new IllegalArgumentException("path must start with '/'");
    }
    
    public Builder secure() {
      this.secure = true;
      return this;
    }
    
    public Builder value(String param1String) {
      Objects.requireNonNull(param1String, "value == null");
      if (param1String.trim().equals(param1String)) {
        this.value = param1String;
        return this;
      } 
      throw new IllegalArgumentException("value is not trimmed");
    }
  }
}
