package com.mysql.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class StringUtils {
  private static final int BYTE_RANGE = 256;
  
  private static final char[] HEX_DIGITS;
  
  private static final int NON_COMMENTS_MYSQL_VERSION_REF_LENGTH = 5;
  
  public static final Set<SearchMode> SEARCH_MODE__ALL = Collections.unmodifiableSet(EnumSet.allOf(SearchMode.class));
  
  public static final Set<SearchMode> SEARCH_MODE__BSESC_COM_WS;
  
  public static final Set<SearchMode> SEARCH_MODE__BSESC_MRK_WS;
  
  public static final Set<SearchMode> SEARCH_MODE__COM_WS;
  
  public static final Set<SearchMode> SEARCH_MODE__MRK_COM_WS;
  
  public static final Set<SearchMode> SEARCH_MODE__MRK_WS;
  
  public static final Set<SearchMode> SEARCH_MODE__NONE;
  
  private static final String VALID_ID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@";
  
  public static final char WILDCARD_ESCAPE = '\\';
  
  public static final char WILDCARD_MANY = '%';
  
  public static final char WILDCARD_ONE = '_';
  
  private static final int WILD_COMPARE_CONTINUE_WITH_WILD = 1;
  
  private static final int WILD_COMPARE_MATCH = 0;
  
  private static final int WILD_COMPARE_NO_MATCH = -1;
  
  private static byte[] allBytes;
  
  private static char[] byteToChars;
  
  private static final ConcurrentHashMap<String, Charset> charsetsByAlias;
  
  private static final String platformEncoding;
  
  private static Method toPlainStringMethod;
  
  static {
    SearchMode searchMode5 = SearchMode.SKIP_BETWEEN_MARKERS;
    SearchMode searchMode2 = SearchMode.SKIP_BLOCK_COMMENTS;
    SearchMode searchMode1 = SearchMode.SKIP_LINE_COMMENTS;
    SearchMode searchMode3 = SearchMode.SKIP_WHITE_SPACE;
    SEARCH_MODE__MRK_COM_WS = Collections.unmodifiableSet(EnumSet.of(searchMode5, searchMode2, searchMode1, searchMode3));
    SearchMode searchMode4 = SearchMode.ALLOW_BACKSLASH_ESCAPE;
    SEARCH_MODE__BSESC_COM_WS = Collections.unmodifiableSet(EnumSet.of(searchMode4, searchMode2, searchMode1, searchMode3));
    SEARCH_MODE__BSESC_MRK_WS = Collections.unmodifiableSet(EnumSet.of(searchMode4, searchMode5, searchMode3));
    SEARCH_MODE__COM_WS = Collections.unmodifiableSet(EnumSet.of(searchMode2, searchMode1, searchMode3));
    SEARCH_MODE__MRK_WS = Collections.unmodifiableSet(EnumSet.of(searchMode5, searchMode3));
    SEARCH_MODE__NONE = Collections.unmodifiableSet(EnumSet.noneOf(SearchMode.class));
    allBytes = new byte[256];
    byteToChars = new char[256];
    charsetsByAlias = new ConcurrentHashMap<String, Charset>();
    platformEncoding = System.getProperty("file.encoding");
    byte b;
    for (b = -128; b <= Byte.MAX_VALUE; b++)
      allBytes[b + 128] = (byte)b; 
    String str = new String(allBytes, 0, 255);
    int i = str.length();
    for (b = 0; b < 255 && b < i; b++)
      byteToChars[b] = str.charAt(b); 
    try {
      toPlainStringMethod = BigDecimal.class.getMethod("toPlainString", new Class[0]);
    } catch (NoSuchMethodException noSuchMethodException) {}
    HEX_DIGITS = new char[] { 
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f' };
  }
  
  public static void appendAsHex(StringBuilder paramStringBuilder, int paramInt) {
    int j;
    if (paramInt == 0) {
      paramStringBuilder.append("0x0");
      return;
    } 
    int i = 32;
    boolean bool = false;
    paramStringBuilder.append("0x");
    do {
      j = i - 4;
      i = (byte)(paramInt >>> j & 0xF);
      if (bool) {
        paramStringBuilder.append(HEX_DIGITS[i]);
      } else if (i != 0) {
        paramStringBuilder.append(HEX_DIGITS[i]);
        bool = true;
      } 
      i = j;
    } while (j != 0);
  }
  
  public static void appendAsHex(StringBuilder paramStringBuilder, byte[] paramArrayOfbyte) {
    paramStringBuilder.append("0x");
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      byte b1 = paramArrayOfbyte[b];
      char[] arrayOfChar = HEX_DIGITS;
      paramStringBuilder.append(arrayOfChar[b1 >>> 4 & 0xF]);
      paramStringBuilder.append(arrayOfChar[b1 & 0xF]);
    } 
  }
  
  public static String consistentToString(BigDecimal paramBigDecimal) {
    if (paramBigDecimal == null)
      return null; 
    Method method = toPlainStringMethod;
    if (method != null)
      try {
        return (String)method.invoke(paramBigDecimal, null);
      } catch (InvocationTargetException|IllegalAccessException invocationTargetException) {} 
    return paramBigDecimal.toString();
  }
  
  public static String dumpAsHex(byte[] paramArrayOfbyte, int paramInt) {
    int j;
    StringBuilder stringBuilder = new StringBuilder(paramInt * 4);
    int k = paramInt / 8;
    int i = 0;
    byte b1 = 0;
    while (i < k && b1 < paramInt) {
      j = b1;
      byte b;
      for (b = 0; b < 8; b++) {
        String str2 = Integer.toHexString(paramArrayOfbyte[j] & 0xFF);
        String str1 = str2;
        if (str2.length() == 1) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("0");
          stringBuilder2.append(str2);
          str1 = stringBuilder2.toString();
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str1);
        stringBuilder1.append(" ");
        stringBuilder.append(stringBuilder1.toString());
        j++;
      } 
      stringBuilder.append("    ");
      for (b = 0; b < 8; b++) {
        j = paramArrayOfbyte[b1] & 0xFF;
        if (j > 32 && j < 127) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append((char)j);
          stringBuilder1.append(" ");
          stringBuilder.append(stringBuilder1.toString());
        } else {
          stringBuilder.append(". ");
        } 
        b1++;
      } 
      stringBuilder.append("\n");
      i++;
    } 
    byte b2 = b1;
    i = 0;
    while (true) {
      j = i;
      if (b2 < paramInt) {
        String str2 = Integer.toHexString(paramArrayOfbyte[b2] & 0xFF);
        String str1 = str2;
        if (str2.length() == 1) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("0");
          stringBuilder2.append(str2);
          str1 = stringBuilder2.toString();
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str1);
        stringBuilder1.append(" ");
        stringBuilder.append(stringBuilder1.toString());
        i++;
        b2++;
        continue;
      } 
      break;
    } 
    while (j < 8) {
      stringBuilder.append("   ");
      j++;
    } 
    stringBuilder.append("    ");
    while (b1 < paramInt) {
      i = paramArrayOfbyte[b1] & 0xFF;
      if (i > 32 && i < 127) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append((char)i);
        stringBuilder1.append(" ");
        stringBuilder.append(stringBuilder1.toString());
      } else {
        stringBuilder.append(". ");
      } 
      b1++;
    } 
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
  
  private static boolean endsWith(byte[] paramArrayOfbyte, String paramString) {
    for (byte b = 1; b <= paramString.length(); b++) {
      int i = paramArrayOfbyte.length;
      int j = paramString.length();
      if (paramArrayOfbyte[i - b] != paramString.charAt(j - b))
        return false; 
    } 
    return true;
  }
  
  public static byte[] escapeEasternUnicodeByteStream(byte[] paramArrayOfbyte, String paramString) {
    if (paramArrayOfbyte == null)
      return null; 
    int i = paramArrayOfbyte.length;
    byte b = 0;
    if (i == 0)
      return new byte[0]; 
    int j = paramArrayOfbyte.length;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(j);
    i = 0;
    while (true) {
      if (paramString.charAt(b) == '\\') {
        byteArrayOutputStream.write(paramArrayOfbyte[i]);
        i++;
      } else {
        int k = paramArrayOfbyte[i];
        int m = k;
        if (k < 0)
          m = k + 256; 
        byteArrayOutputStream.write(m);
        if (m >= 128) {
          int n = i;
          if (i < j - 1) {
            m = i + 1;
            n = paramArrayOfbyte[m];
            i = n;
            if (n < 0)
              i = n + 256; 
            byteArrayOutputStream.write(i);
            n = m;
            if (i == 92) {
              byteArrayOutputStream.write(i);
              n = m;
            } 
          } 
        } else {
          k = i;
          if (m == 92) {
            k = i;
            if (i < j - 1) {
              int n = i + 1;
              k = paramArrayOfbyte[n];
              m = k;
              if (k < 0)
                m = k + 256; 
              k = i;
              if (m == 98) {
                byteArrayOutputStream.write(92);
                byteArrayOutputStream.write(98);
                k = n;
              } 
            } 
          } 
        } 
        i = k + 1;
      } 
      if (i >= j)
        return byteArrayOutputStream.toByteArray(); 
      b++;
    } 
  }
  
  public static String escapeQuote(String paramString1, String paramString2) {
    if (paramString1 == null)
      return null; 
    paramString1 = toString(stripEnclosure(paramString1.getBytes(), paramString2, paramString2));
    int i = paramString1.indexOf(paramString2);
    String str = paramString1.substring(0, i);
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(str);
    stringBuilder2.append(paramString2);
    stringBuilder2.append(paramString2);
    str = stringBuilder2.toString();
    paramString1 = paramString1.substring(i + 1, paramString1.length());
    for (i = paramString1.indexOf(paramString2); i > -1; i = paramString1.indexOf(paramString2)) {
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(paramString1.substring(0, i));
      String str1 = stringBuilder2.toString();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(paramString2);
      stringBuilder.append(paramString2);
      str = stringBuilder.toString();
      paramString1 = paramString1.substring(i + 1, paramString1.length());
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(str);
    stringBuilder1.append(paramString1);
    return stringBuilder1.toString();
  }
  
  public static Charset findCharset(String paramString) throws UnsupportedEncodingException {
    try {
      ConcurrentHashMap<String, Charset> concurrentHashMap = charsetsByAlias;
      Charset charset2 = concurrentHashMap.get(paramString);
      Charset charset1 = charset2;
      if (charset2 == null) {
        charset1 = Charset.forName(paramString);
        charset2 = concurrentHashMap.putIfAbsent(paramString, charset1);
        if (charset2 != null)
          charset1 = charset2; 
      } 
      return charset1;
    } catch (UnsupportedCharsetException unsupportedCharsetException) {
      throw new UnsupportedEncodingException(paramString);
    } catch (IllegalCharsetNameException illegalCharsetNameException) {
      throw new UnsupportedEncodingException(paramString);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new UnsupportedEncodingException(paramString);
    } 
  }
  
  public static char firstAlphaCharUc(String paramString, int paramInt) {
    if (paramString == null)
      return Character.MIN_VALUE; 
    int i = paramString.length();
    while (paramInt < i) {
      char c = paramString.charAt(paramInt);
      if (Character.isLetter(c))
        return Character.toUpperCase(c); 
      paramInt++;
    } 
    return Character.MIN_VALUE;
  }
  
  public static char firstNonWsCharUc(String paramString) {
    return firstNonWsCharUc(paramString, 0);
  }
  
  public static char firstNonWsCharUc(String paramString, int paramInt) {
    if (paramString == null)
      return Character.MIN_VALUE; 
    int i = paramString.length();
    while (paramInt < i) {
      char c = paramString.charAt(paramInt);
      if (!Character.isWhitespace(c))
        return Character.toUpperCase(c); 
      paramInt++;
    } 
    return Character.MIN_VALUE;
  }
  
  public static String fixDecimalExponent(String paramString) {
    int j = paramString.indexOf('E');
    int i = j;
    if (j == -1)
      i = paramString.indexOf('e'); 
    String str = paramString;
    if (i != -1) {
      j = paramString.length();
      i++;
      str = paramString;
      if (j > i) {
        j = paramString.charAt(i);
        str = paramString;
        if (j != 45) {
          str = paramString;
          if (j != 43) {
            StringBuilder stringBuilder = new StringBuilder(paramString.length() + 1);
            stringBuilder.append(paramString.substring(0, i));
            stringBuilder.append('+');
            stringBuilder.append(paramString.substring(i, paramString.length()));
            str = stringBuilder.toString();
          } 
        } 
      } 
    } 
    return str;
  }
  
  public static byte[] getBytes(String paramString) {
    try {
      return getBytes(paramString, 0, paramString.length(), platformEncoding);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return null;
    } 
  }
  
  public static byte[] getBytes(String paramString, int paramInt1, int paramInt2) {
    try {
      return getBytes(paramString, paramInt1, paramInt2, platformEncoding);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return null;
    } 
  }
  
  public static byte[] getBytes(String paramString1, int paramInt1, int paramInt2, String paramString2) throws UnsupportedEncodingException {
    if (!Util.isJdbc4())
      return (paramInt1 != 0 || paramInt2 != paramString1.length()) ? paramString1.substring(paramInt1, paramInt2 + paramInt1).getBytes(paramString2) : paramString1.getBytes(paramString2); 
    ByteBuffer byteBuffer = findCharset(paramString2).encode(CharBuffer.wrap(paramString1.toCharArray(), paramInt1, paramInt2));
    paramInt1 = byteBuffer.limit();
    byte[] arrayOfByte = new byte[paramInt1];
    byteBuffer.get(arrayOfByte, 0, paramInt1);
    return arrayOfByte;
  }
  
  public static byte[] getBytes(String paramString1, SingleByteCharsetConverter paramSingleByteCharsetConverter, String paramString2, String paramString3, int paramInt1, int paramInt2, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    byte[] arrayOfByte;
    if (paramSingleByteCharsetConverter != null)
      try {
        return paramSingleByteCharsetConverter.toBytes(paramString1, paramInt1, paramInt2);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.5"));
        stringBuilder.append(paramString2);
        stringBuilder.append(Messages.getString("StringUtils.6"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    if (paramString2 == null) {
      arrayOfByte = getBytes((String)stringBuilder, paramInt1, paramInt2);
    } else {
      String str = arrayOfByte.substring(paramInt1, paramInt2 + paramInt1);
      arrayOfByte = getBytes(str, paramString2);
      if (!paramBoolean && CharsetMapping.requiresEscapeEasternUnicode(paramString2) && !paramString2.equalsIgnoreCase(paramString3))
        arrayOfByte = escapeEasternUnicodeByteStream(arrayOfByte, str); 
    } 
    return arrayOfByte;
  }
  
  public static byte[] getBytes(String paramString1, SingleByteCharsetConverter paramSingleByteCharsetConverter, String paramString2, String paramString3, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    byte[] arrayOfByte;
    if (paramSingleByteCharsetConverter != null)
      try {
        return paramSingleByteCharsetConverter.toBytes(paramString1);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.5"));
        stringBuilder.append(paramString2);
        stringBuilder.append(Messages.getString("StringUtils.6"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    if (paramString2 == null) {
      arrayOfByte = getBytes((String)stringBuilder);
    } else {
      byte[] arrayOfByte1 = getBytes((String)arrayOfByte, paramString2);
      if (!paramBoolean && CharsetMapping.requiresEscapeEasternUnicode(paramString2) && !paramString2.equalsIgnoreCase(paramString3)) {
        arrayOfByte = escapeEasternUnicodeByteStream(arrayOfByte1, (String)arrayOfByte);
      } else {
        arrayOfByte = arrayOfByte1;
      } 
    } 
    return arrayOfByte;
  }
  
  public static byte[] getBytes(String paramString1, String paramString2) throws UnsupportedEncodingException {
    return getBytes(paramString1, 0, paramString1.length(), paramString2);
  }
  
  public static final byte[] getBytes(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, boolean paramBoolean, MySQLConnection paramMySQLConnection, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    if (paramMySQLConnection != null)
      try {
        SingleByteCharsetConverter singleByteCharsetConverter1 = paramMySQLConnection.getCharsetConverter(paramString2);
        return getBytes(paramString1, singleByteCharsetConverter1, paramString2, paramString3, paramInt1, paramInt2, paramBoolean, paramExceptionInterceptor);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.5"));
        stringBuilder.append(paramString2);
        stringBuilder.append(Messages.getString("StringUtils.6"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    SingleByteCharsetConverter singleByteCharsetConverter = SingleByteCharsetConverter.getInstance(paramString2, null);
    return getBytes((String)stringBuilder, singleByteCharsetConverter, paramString2, paramString3, paramInt1, paramInt2, paramBoolean, paramExceptionInterceptor);
  }
  
  public static byte[] getBytes(String paramString1, String paramString2, String paramString3, boolean paramBoolean, MySQLConnection paramMySQLConnection, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    if (paramMySQLConnection != null)
      try {
        SingleByteCharsetConverter singleByteCharsetConverter1 = paramMySQLConnection.getCharsetConverter(paramString2);
        return getBytes(paramString1, singleByteCharsetConverter1, paramString2, paramString3, paramBoolean, paramExceptionInterceptor);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.5"));
        stringBuilder.append(paramString2);
        stringBuilder.append(Messages.getString("StringUtils.6"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    SingleByteCharsetConverter singleByteCharsetConverter = SingleByteCharsetConverter.getInstance(paramString2, null);
    return getBytes((String)stringBuilder, singleByteCharsetConverter, paramString2, paramString3, paramBoolean, paramExceptionInterceptor);
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar) {
    try {
      return getBytes(paramArrayOfchar, 0, paramArrayOfchar.length, platformEncoding);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return null;
    } 
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    try {
      return getBytes(paramArrayOfchar, paramInt1, paramInt2, platformEncoding);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return null;
    } 
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar, int paramInt1, int paramInt2, String paramString) throws UnsupportedEncodingException {
    ByteBuffer byteBuffer = findCharset(paramString).encode(CharBuffer.wrap(paramArrayOfchar, paramInt1, paramInt2));
    paramInt1 = byteBuffer.limit();
    byte[] arrayOfByte = new byte[paramInt1];
    byteBuffer.get(arrayOfByte, 0, paramInt1);
    return arrayOfByte;
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar, SingleByteCharsetConverter paramSingleByteCharsetConverter, String paramString1, String paramString2, int paramInt1, int paramInt2, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    byte[] arrayOfByte;
    if (paramSingleByteCharsetConverter != null)
      try {
        return paramSingleByteCharsetConverter.toBytes(paramArrayOfchar, paramInt1, paramInt2);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.0"));
        stringBuilder.append(paramString1);
        stringBuilder.append(Messages.getString("StringUtils.1"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    if (paramString1 == null) {
      arrayOfByte = getBytes((char[])stringBuilder, paramInt1, paramInt2);
    } else {
      byte[] arrayOfByte1 = getBytes((char[])arrayOfByte, paramInt1, paramInt2, paramString1);
      if (!paramBoolean && CharsetMapping.requiresEscapeEasternUnicode(paramString1) && !paramString1.equalsIgnoreCase(paramString2)) {
        paramString2 = new String();
        this((char[])arrayOfByte, paramInt1, paramInt2);
        arrayOfByte = escapeEasternUnicodeByteStream(arrayOfByte1, paramString2);
      } else {
        arrayOfByte = arrayOfByte1;
      } 
    } 
    return arrayOfByte;
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar, SingleByteCharsetConverter paramSingleByteCharsetConverter, String paramString1, String paramString2, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    byte[] arrayOfByte;
    if (paramSingleByteCharsetConverter != null)
      try {
        return paramSingleByteCharsetConverter.toBytes(paramArrayOfchar);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.0"));
        stringBuilder.append(paramString1);
        stringBuilder.append(Messages.getString("StringUtils.1"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    if (paramString1 == null) {
      arrayOfByte = getBytes((char[])stringBuilder);
    } else {
      byte[] arrayOfByte1 = getBytes((char[])arrayOfByte, paramString1);
      if (!paramBoolean && CharsetMapping.requiresEscapeEasternUnicode(paramString1) && !paramString1.equalsIgnoreCase(paramString2)) {
        paramString2 = new String();
        this((char[])arrayOfByte);
        arrayOfByte = escapeEasternUnicodeByteStream(arrayOfByte1, paramString2);
      } else {
        arrayOfByte = arrayOfByte1;
      } 
    } 
    return arrayOfByte;
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar, String paramString) throws UnsupportedEncodingException {
    return getBytes(paramArrayOfchar, 0, paramArrayOfchar.length, paramString);
  }
  
  public static byte[] getBytes(char[] paramArrayOfchar, String paramString1, String paramString2, boolean paramBoolean, MySQLConnection paramMySQLConnection, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    if (paramMySQLConnection != null)
      try {
        SingleByteCharsetConverter singleByteCharsetConverter1 = paramMySQLConnection.getCharsetConverter(paramString1);
        return getBytes(paramArrayOfchar, singleByteCharsetConverter1, paramString1, paramString2, paramBoolean, paramExceptionInterceptor);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.0"));
        stringBuilder.append(paramString1);
        stringBuilder.append(Messages.getString("StringUtils.1"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    SingleByteCharsetConverter singleByteCharsetConverter = SingleByteCharsetConverter.getInstance(paramString1, null);
    return getBytes((char[])stringBuilder, singleByteCharsetConverter, paramString1, paramString2, paramBoolean, paramExceptionInterceptor);
  }
  
  public static byte[] getBytesNullTerminated(String paramString1, String paramString2) throws UnsupportedEncodingException {
    ByteBuffer byteBuffer = findCharset(paramString2).encode(paramString1);
    int i = byteBuffer.limit();
    byte[] arrayOfByte = new byte[i + 1];
    byteBuffer.get(arrayOfByte, 0, i);
    arrayOfByte[i] = 0;
    return arrayOfByte;
  }
  
  public static byte[] getBytesWrapped(String paramString1, char paramChar1, char paramChar2, SingleByteCharsetConverter paramSingleByteCharsetConverter, String paramString2, String paramString3, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    StringBuilder stringBuilder;
    byte[] arrayOfByte;
    if (paramSingleByteCharsetConverter != null)
      try {
        return paramSingleByteCharsetConverter.toBytesWrapped(paramString1, paramChar1, paramChar2);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("StringUtils.10"));
        stringBuilder.append(paramString2);
        stringBuilder.append(Messages.getString("StringUtils.11"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      }  
    if (paramString2 == null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      this(stringBuilder.length() + 2);
      stringBuilder1.append(paramChar1);
      stringBuilder1.append((String)stringBuilder);
      stringBuilder1.append(paramChar2);
      arrayOfByte = getBytes(stringBuilder1.toString());
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      this(arrayOfByte.length() + 2);
      stringBuilder1.append(paramChar1);
      stringBuilder1.append((String)arrayOfByte);
      stringBuilder1.append(paramChar2);
      String str = stringBuilder1.toString();
      arrayOfByte = getBytes(str, paramString2);
      if (!paramBoolean && CharsetMapping.requiresEscapeEasternUnicode(paramString2) && !paramString2.equalsIgnoreCase(paramString3))
        arrayOfByte = escapeEasternUnicodeByteStream(arrayOfByte, str); 
    } 
    return arrayOfByte;
  }
  
  public static int getInt(byte[] paramArrayOfbyte) throws NumberFormatException {
    return getInt(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static int getInt(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws NumberFormatException {
    while (paramInt1 < paramInt2 && Character.isWhitespace((char)paramArrayOfbyte[paramInt1]))
      paramInt1++; 
    if (paramInt1 != paramInt2) {
      int i;
      boolean bool1;
      int j = (char)paramArrayOfbyte[paramInt1];
      int m = 0;
      if (j == 45) {
        paramInt1++;
        bool1 = true;
      } else {
        i = paramInt1;
        if ((char)paramArrayOfbyte[paramInt1] == '+')
          i = paramInt1 + 1; 
        bool1 = false;
        paramInt1 = i;
      } 
      byte b = 7;
      if (bool1)
        b = 8; 
      int k = paramInt1;
      boolean bool2 = false;
      j = m;
      while (k < paramInt2) {
        char c = (char)paramArrayOfbyte[k];
        if (Character.isDigit(c)) {
          m = c - 48;
        } else if (Character.isLetter(c)) {
          n = Character.toUpperCase(c) - 65 + 10;
        } else {
          break;
        } 
        int n = (char)n;
        if (n >= 10)
          break; 
        if (j > 214748364 || (j == 214748364 && n > b)) {
          bool2 = true;
        } else {
          i = j * 10 + n;
        } 
        k++;
      } 
      if (k != paramInt1) {
        if (!bool2) {
          paramInt1 = i;
          if (bool1)
            paramInt1 = -i; 
          return paramInt1;
        } 
        throw new NumberFormatException(toString(paramArrayOfbyte));
      } 
      throw new NumberFormatException(toString(paramArrayOfbyte));
    } 
    throw new NumberFormatException(toString(paramArrayOfbyte));
  }
  
  public static long getLong(byte[] paramArrayOfbyte) throws NumberFormatException {
    return getLong(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static long getLong(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws NumberFormatException {
    int i;
    for (i = paramInt1; i < paramInt2 && Character.isWhitespace((char)paramArrayOfbyte[i]); i++);
    if (i != paramInt2) {
      boolean bool;
      paramInt1 = (char)paramArrayOfbyte[i];
      int k = 0;
      if (paramInt1 == 45) {
        paramInt1 = i + 1;
        bool = true;
      } else {
        paramInt1 = i;
        if ((char)paramArrayOfbyte[i] == '+')
          paramInt1 = i + 1; 
        bool = false;
      } 
      long l4 = 10L;
      long l3 = Long.MAX_VALUE / l4;
      long l1 = (int)(Long.MAX_VALUE % l4);
      long l2 = l1;
      if (bool)
        l2 = l1 + 1L; 
      l1 = 0L;
      int j = paramInt1;
      i = k;
      while (j < paramInt2) {
        char c = (char)paramArrayOfbyte[j];
        if (Character.isDigit(c)) {
          k = c - 48;
        } else if (Character.isLetter(c)) {
          k = Character.toUpperCase(c) - 65 + 10;
        } else {
          break;
        } 
        k = (char)k;
        if (k >= 10)
          break; 
        int m = l1 cmp l3;
        if (m > 0 || (m == 0 && k > l2)) {
          i = 1;
        } else {
          l1 = l1 * l4 + k;
        } 
        j++;
      } 
      if (j != paramInt1) {
        if (i == 0) {
          l2 = l1;
          if (bool)
            l2 = -l1; 
          return l2;
        } 
        throw new NumberFormatException(toString(paramArrayOfbyte));
      } 
      throw new NumberFormatException(toString(paramArrayOfbyte));
    } 
    throw new NumberFormatException(toString(paramArrayOfbyte));
  }
  
  public static short getShort(byte[] paramArrayOfbyte) throws NumberFormatException {
    return getShort(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static short getShort(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws NumberFormatException {
    int i;
    for (i = paramInt1; i < paramInt2 && Character.isWhitespace((char)paramArrayOfbyte[i]); i++);
    if (i != paramInt2) {
      paramInt1 = (char)paramArrayOfbyte[i];
      short s1 = 0;
      if (paramInt1 == 45) {
        paramInt1 = i + 1;
        i = 1;
      } else {
        paramInt1 = i;
        if ((char)paramArrayOfbyte[i] == '+')
          paramInt1 = i + 1; 
        i = 0;
      } 
      short s4 = (short)3276;
      short s3 = (short)7;
      short s2 = s3;
      if (i != 0)
        s2 = (short)(s3 + 1); 
      int j = paramInt1;
      boolean bool = false;
      while (j < paramInt2) {
        char c = (char)paramArrayOfbyte[j];
        if (Character.isDigit(c)) {
          k = c - 48;
        } else if (Character.isLetter(c)) {
          k = Character.toUpperCase(c) - 65 + 10;
        } else {
          break;
        } 
        int k = (char)k;
        if (k >= 10)
          break; 
        if (s1 > s4 || (s1 == s4 && k > s2)) {
          bool = true;
        } else {
          s1 = (short)((short)(s1 * 10) + k);
        } 
        j++;
      } 
      if (j != paramInt1) {
        if (!bool) {
          short s = s1;
          if (i != 0)
            s = (short)-s1; 
          return s;
        } 
        throw new NumberFormatException(toString(paramArrayOfbyte));
      } 
      throw new NumberFormatException(toString(paramArrayOfbyte));
    } 
    throw new NumberFormatException(toString(paramArrayOfbyte));
  }
  
  public static int indexOf(byte[] paramArrayOfbyte, char paramChar) {
    if (paramArrayOfbyte == null)
      return -1; 
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      if (paramArrayOfbyte[b] == paramChar)
        return b; 
    } 
    return -1;
  }
  
  public static int indexOfIgnoreCase(int paramInt, String paramString1, String paramString2) {
    if (paramString1 != null && paramString2 != null) {
      int j = paramString1.length();
      int i = paramString2.length();
      j -= i;
      if (paramInt <= j && i != 0) {
        char c2 = Character.toUpperCase(paramString2.charAt(0));
        char c1 = Character.toLowerCase(paramString2.charAt(0));
        while (paramInt <= j) {
          i = paramInt;
          if (isCharAtPosNotEqualIgnoreCase(paramString1, paramInt, c2, c1))
            while (true) {
              i = ++paramInt;
              if (paramInt <= j) {
                i = paramInt;
                if (isCharAtPosNotEqualIgnoreCase(paramString1, paramInt, c2, c1))
                  continue; 
              } 
              break;
            }  
          if (i <= j && startsWithIgnoreCase(paramString1, i, paramString2))
            return i; 
          paramInt = i + 1;
        } 
      } 
    } 
    return -1;
  }
  
  public static int indexOfIgnoreCase(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Set<SearchMode> paramSet) {
    Set<SearchMode> set = paramSet;
    if (paramString1 != null && paramString2 != null) {
      int j = paramString1.length();
      int i = paramString2.length();
      j -= i;
      if (paramInt <= j && i != 0) {
        if (set.contains(SearchMode.SKIP_BETWEEN_MARKERS))
          if (paramString3 != null && paramString4 != null && paramString3.length() == paramString4.length()) {
            if (paramString5 != null) {
              char[] arrayOfChar = paramString5.toCharArray();
              int k = arrayOfChar.length;
              i = 0;
              while (i < k) {
                if (paramString3.indexOf(arrayOfChar[i]) != -1) {
                  i++;
                  continue;
                } 
                throw new IllegalArgumentException(Messages.getString("StringUtils.16", new String[] { paramString5, paramString3 }));
              } 
            } else {
              throw new IllegalArgumentException(Messages.getString("StringUtils.16", new String[] { paramString5, paramString3 }));
            } 
          } else {
            throw new IllegalArgumentException(Messages.getString("StringUtils.15", new String[] { paramString3, paramString4 }));
          }  
        char c2 = Character.toUpperCase(paramString2.charAt(0));
        char c1 = Character.toLowerCase(paramString2.charAt(0));
        Set<SearchMode> set1 = set;
        if (Character.isWhitespace(c1)) {
          SearchMode searchMode = SearchMode.SKIP_WHITE_SPACE;
          set1 = set;
          if (set.contains(searchMode)) {
            set1 = EnumSet.copyOf(paramSet);
            set1.remove(searchMode);
          } 
        } 
        while (paramInt <= j) {
          paramInt = indexOfNextChar(paramInt, j, paramString1, paramString3, paramString4, paramString5, set1);
          if (paramInt == -1)
            return -1; 
          if (isCharEqualIgnoreCase(paramString1.charAt(paramInt), c2, c1) && startsWithIgnoreCase(paramString1, paramInt, paramString2))
            return paramInt; 
          paramInt++;
        } 
      } 
    } 
    return -1;
  }
  
  public static int indexOfIgnoreCase(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, Set<SearchMode> paramSet) {
    return indexOfIgnoreCase(paramInt, paramString1, paramString2, paramString3, paramString4, "", paramSet);
  }
  
  public static int indexOfIgnoreCase(int paramInt, String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, Set<SearchMode> paramSet) {
    Set<SearchMode> set = paramSet;
    if (paramString1 != null && paramArrayOfString != null) {
      int m = paramString1.length();
      int k = paramArrayOfString.length;
      int j = 0;
      int i = 0;
      while (j < k) {
        i += paramArrayOfString[j].length();
        j++;
      } 
      if (i == 0)
        return -1; 
      int n = paramArrayOfString.length;
      if (n > 0) {
        j = n - 1;
      } else {
        j = 0;
      } 
      int i1 = m - i + j;
      if (paramInt > i1)
        return -1; 
      SearchMode searchMode = SearchMode.SKIP_BETWEEN_MARKERS;
      if (!set.contains(searchMode) || (paramString2 != null && paramString3 != null && paramString2.length() == paramString3.length())) {
        Set<SearchMode> set1 = set;
        if (Character.isWhitespace(paramArrayOfString[0].charAt(0))) {
          SearchMode searchMode1 = SearchMode.SKIP_WHITE_SPACE;
          set1 = set;
          if (set.contains(searchMode1)) {
            set1 = EnumSet.copyOf(paramSet);
            set1.remove(searchMode1);
          } 
        } 
        paramSet = EnumSet.of(SearchMode.SKIP_WHITE_SPACE);
        paramSet.addAll(set1);
        paramSet.remove(searchMode);
        while (paramInt <= i1) {
          i = indexOfIgnoreCase(paramInt, paramString1, paramArrayOfString[0], paramString2, paramString3, set1);
          if (i == -1 || i > i1)
            break; 
          paramInt = paramArrayOfString[0].length() + i;
          k = 0;
          j = 1;
          while (++k < n && j != 0) {
            int i2 = indexOfNextChar(paramInt, m - 1, paramString1, null, null, null, paramSet);
            if (paramInt == i2 || !startsWithIgnoreCase(paramString1, i2, paramArrayOfString[k])) {
              j = 0;
              continue;
            } 
            paramInt = i2 + paramArrayOfString[k].length();
          } 
          if (j != 0)
            return i; 
          paramInt = i + 1;
        } 
        return -1;
      } 
      throw new IllegalArgumentException(Messages.getString("StringUtils.15", new String[] { paramString2, paramString3 }));
    } 
    return -1;
  }
  
  public static int indexOfIgnoreCase(String paramString1, String paramString2) {
    return indexOfIgnoreCase(0, paramString1, paramString2);
  }
  
  private static int indexOfNextChar(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, Set<SearchMode> paramSet) {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull -> 6
    //   4: iconst_m1
    //   5: ireturn
    //   6: aload_2
    //   7: invokevirtual length : ()I
    //   10: istore #15
    //   12: iload_0
    //   13: iload #15
    //   15: if_icmplt -> 20
    //   18: iconst_m1
    //   19: ireturn
    //   20: aload_2
    //   21: iload_0
    //   22: invokevirtual charAt : (I)C
    //   25: istore #9
    //   27: iload_0
    //   28: iconst_1
    //   29: iadd
    //   30: istore #10
    //   32: iload #10
    //   34: iload #15
    //   36: if_icmpge -> 50
    //   39: aload_2
    //   40: iload #10
    //   42: invokevirtual charAt : (I)C
    //   45: istore #8
    //   47: goto -> 53
    //   50: iconst_0
    //   51: istore #8
    //   53: iload_0
    //   54: iload_1
    //   55: if_icmpgt -> 1222
    //   58: iload_0
    //   59: iconst_2
    //   60: iadd
    //   61: istore #10
    //   63: iload #10
    //   65: iload #15
    //   67: if_icmpge -> 81
    //   70: aload_2
    //   71: iload #10
    //   73: invokevirtual charAt : (I)C
    //   76: istore #7
    //   78: goto -> 84
    //   81: iconst_0
    //   82: istore #7
    //   84: aload #6
    //   86: getstatic com/mysql/jdbc/StringUtils$SearchMode.ALLOW_BACKSLASH_ESCAPE : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   89: invokeinterface contains : (Ljava/lang/Object;)Z
    //   94: ifeq -> 136
    //   97: iload #9
    //   99: bipush #92
    //   101: if_icmpne -> 136
    //   104: iinc #0, 1
    //   107: iload_0
    //   108: iconst_2
    //   109: iadd
    //   110: istore #10
    //   112: iload #10
    //   114: iload #15
    //   116: if_icmpge -> 130
    //   119: aload_2
    //   120: iload #10
    //   122: invokevirtual charAt : (I)C
    //   125: istore #8
    //   127: goto -> 133
    //   130: iconst_0
    //   131: istore #8
    //   133: goto -> 1210
    //   136: aload #6
    //   138: getstatic com/mysql/jdbc/StringUtils$SearchMode.SKIP_BETWEEN_MARKERS : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   141: invokeinterface contains : (Ljava/lang/Object;)Z
    //   146: ifeq -> 535
    //   149: aload_3
    //   150: iload #9
    //   152: invokevirtual indexOf : (I)I
    //   155: istore #10
    //   157: iload #10
    //   159: iconst_m1
    //   160: if_icmpeq -> 535
    //   163: aload #4
    //   165: iload #10
    //   167: invokevirtual charAt : (I)C
    //   170: istore #16
    //   172: aload #5
    //   174: iload #9
    //   176: invokevirtual indexOf : (I)I
    //   179: iconst_m1
    //   180: if_icmpeq -> 189
    //   183: iconst_1
    //   184: istore #12
    //   186: goto -> 192
    //   189: iconst_0
    //   190: istore #12
    //   192: iconst_0
    //   193: istore #13
    //   195: iinc #0, 1
    //   198: iload_0
    //   199: iload_1
    //   200: if_icmpgt -> 462
    //   203: aload_2
    //   204: iload_0
    //   205: invokevirtual charAt : (I)C
    //   208: istore #17
    //   210: iload #17
    //   212: iload #16
    //   214: if_icmpne -> 222
    //   217: iload #13
    //   219: ifeq -> 462
    //   222: iload #12
    //   224: ifne -> 368
    //   227: aload #5
    //   229: iload #17
    //   231: invokevirtual indexOf : (I)I
    //   234: iconst_m1
    //   235: if_icmpeq -> 368
    //   238: aload #4
    //   240: aload_3
    //   241: iload #17
    //   243: invokevirtual indexOf : (I)I
    //   246: invokevirtual charAt : (I)C
    //   249: istore #18
    //   251: iconst_0
    //   252: istore #14
    //   254: iinc #0, 1
    //   257: iload_0
    //   258: istore #10
    //   260: iload #13
    //   262: istore #11
    //   264: iload_0
    //   265: iload_1
    //   266: if_icmpgt -> 384
    //   269: aload_2
    //   270: iload_0
    //   271: invokevirtual charAt : (I)C
    //   274: istore #19
    //   276: iload #19
    //   278: iload #18
    //   280: if_icmpne -> 295
    //   283: iload_0
    //   284: istore #10
    //   286: iload #13
    //   288: istore #11
    //   290: iload #14
    //   292: ifeq -> 384
    //   295: iload #19
    //   297: iload #17
    //   299: if_icmpne -> 315
    //   302: iload #14
    //   304: iconst_1
    //   305: iadd
    //   306: istore #10
    //   308: iload #10
    //   310: istore #14
    //   312: goto -> 254
    //   315: iload #19
    //   317: iload #18
    //   319: if_icmpne -> 331
    //   322: iload #14
    //   324: iconst_1
    //   325: isub
    //   326: istore #10
    //   328: goto -> 308
    //   331: iload_0
    //   332: istore #10
    //   334: aload #6
    //   336: getstatic com/mysql/jdbc/StringUtils$SearchMode.ALLOW_BACKSLASH_ESCAPE : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   339: invokeinterface contains : (Ljava/lang/Object;)Z
    //   344: ifeq -> 362
    //   347: iload_0
    //   348: istore #10
    //   350: iload #19
    //   352: bipush #92
    //   354: if_icmpne -> 362
    //   357: iload_0
    //   358: iconst_1
    //   359: iadd
    //   360: istore #10
    //   362: iload #10
    //   364: istore_0
    //   365: goto -> 254
    //   368: iload #17
    //   370: iload #9
    //   372: if_icmpne -> 394
    //   375: iload #13
    //   377: iconst_1
    //   378: iadd
    //   379: istore #11
    //   381: iload_0
    //   382: istore #10
    //   384: iload #10
    //   386: istore_0
    //   387: iload #11
    //   389: istore #13
    //   391: goto -> 195
    //   394: iload #17
    //   396: iload #16
    //   398: if_icmpne -> 413
    //   401: iload #13
    //   403: iconst_1
    //   404: isub
    //   405: istore #11
    //   407: iload_0
    //   408: istore #10
    //   410: goto -> 384
    //   413: aload #6
    //   415: getstatic com/mysql/jdbc/StringUtils$SearchMode.ALLOW_BACKSLASH_ESCAPE : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   418: invokeinterface contains : (Ljava/lang/Object;)Z
    //   423: ifeq -> 452
    //   426: iload_0
    //   427: istore #10
    //   429: iload #13
    //   431: istore #11
    //   433: iload #17
    //   435: bipush #92
    //   437: if_icmpne -> 384
    //   440: iload_0
    //   441: iconst_1
    //   442: iadd
    //   443: istore #10
    //   445: iload #13
    //   447: istore #11
    //   449: goto -> 384
    //   452: iload_0
    //   453: istore #10
    //   455: iload #13
    //   457: istore #11
    //   459: goto -> 384
    //   462: iload_0
    //   463: iconst_1
    //   464: iadd
    //   465: istore #10
    //   467: iload #10
    //   469: iload #15
    //   471: if_icmpge -> 485
    //   474: aload_2
    //   475: iload #10
    //   477: invokevirtual charAt : (I)C
    //   480: istore #8
    //   482: goto -> 488
    //   485: iconst_0
    //   486: istore #8
    //   488: iload_0
    //   489: iconst_2
    //   490: iadd
    //   491: istore #11
    //   493: iload_0
    //   494: istore #10
    //   496: iload #8
    //   498: istore #7
    //   500: iload #11
    //   502: iload #15
    //   504: if_icmpge -> 526
    //   507: aload_2
    //   508: iload #11
    //   510: invokevirtual charAt : (I)C
    //   513: istore #9
    //   515: iload #8
    //   517: istore #7
    //   519: iload #9
    //   521: istore #8
    //   523: goto -> 532
    //   526: iconst_0
    //   527: istore #8
    //   529: iload #10
    //   531: istore_0
    //   532: goto -> 133
    //   535: getstatic com/mysql/jdbc/StringUtils$SearchMode.SKIP_BLOCK_COMMENTS : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   538: astore #20
    //   540: aload #6
    //   542: aload #20
    //   544: invokeinterface contains : (Ljava/lang/Object;)Z
    //   549: ifeq -> 769
    //   552: iload #9
    //   554: bipush #47
    //   556: if_icmpne -> 769
    //   559: iload #8
    //   561: bipush #42
    //   563: if_icmpne -> 769
    //   566: iload #7
    //   568: bipush #33
    //   570: if_icmpeq -> 643
    //   573: iinc #0, 1
    //   576: iload_0
    //   577: iconst_1
    //   578: iadd
    //   579: istore #10
    //   581: iload #10
    //   583: iload_1
    //   584: if_icmpgt -> 635
    //   587: iload #10
    //   589: istore_0
    //   590: aload_2
    //   591: iload #10
    //   593: invokevirtual charAt : (I)C
    //   596: bipush #42
    //   598: if_icmpne -> 576
    //   601: iload #10
    //   603: iconst_1
    //   604: iadd
    //   605: istore_0
    //   606: iload_0
    //   607: iload #15
    //   609: if_icmpge -> 621
    //   612: aload_2
    //   613: iload_0
    //   614: invokevirtual charAt : (I)C
    //   617: istore_0
    //   618: goto -> 623
    //   621: iconst_0
    //   622: istore_0
    //   623: iload_0
    //   624: bipush #47
    //   626: if_icmpeq -> 635
    //   629: iload #10
    //   631: istore_0
    //   632: goto -> 576
    //   635: iload #10
    //   637: iconst_1
    //   638: iadd
    //   639: istore_0
    //   640: goto -> 705
    //   643: iload_0
    //   644: iconst_1
    //   645: iadd
    //   646: iconst_1
    //   647: iadd
    //   648: istore #11
    //   650: iconst_1
    //   651: istore #10
    //   653: iload #10
    //   655: iconst_5
    //   656: if_icmpgt -> 691
    //   659: iload #11
    //   661: iload #10
    //   663: iadd
    //   664: istore_0
    //   665: iload_0
    //   666: iload #15
    //   668: if_icmpge -> 691
    //   671: aload_2
    //   672: iload_0
    //   673: invokevirtual charAt : (I)C
    //   676: invokestatic isDigit : (C)Z
    //   679: ifne -> 685
    //   682: goto -> 691
    //   685: iinc #10, 1
    //   688: goto -> 653
    //   691: iload #11
    //   693: istore_0
    //   694: iload #10
    //   696: iconst_5
    //   697: if_icmpne -> 705
    //   700: iload #11
    //   702: iconst_5
    //   703: iadd
    //   704: istore_0
    //   705: iload_0
    //   706: iconst_1
    //   707: iadd
    //   708: istore #10
    //   710: iload #10
    //   712: iload #15
    //   714: if_icmpge -> 728
    //   717: aload_2
    //   718: iload #10
    //   720: invokevirtual charAt : (I)C
    //   723: istore #8
    //   725: goto -> 731
    //   728: iconst_0
    //   729: istore #8
    //   731: iload_0
    //   732: iconst_2
    //   733: iadd
    //   734: istore #11
    //   736: iload_0
    //   737: istore #10
    //   739: iload #8
    //   741: istore #7
    //   743: iload #11
    //   745: iload #15
    //   747: if_icmpge -> 526
    //   750: aload_2
    //   751: iload #11
    //   753: invokevirtual charAt : (I)C
    //   756: istore #9
    //   758: iload #8
    //   760: istore #7
    //   762: iload #9
    //   764: istore #8
    //   766: goto -> 532
    //   769: aload #6
    //   771: aload #20
    //   773: invokeinterface contains : (Ljava/lang/Object;)Z
    //   778: ifeq -> 845
    //   781: iload #9
    //   783: bipush #42
    //   785: if_icmpne -> 845
    //   788: iload #8
    //   790: bipush #47
    //   792: if_icmpne -> 845
    //   795: iinc #0, 1
    //   798: iload_0
    //   799: iconst_2
    //   800: iadd
    //   801: istore #11
    //   803: iload_0
    //   804: istore #10
    //   806: iload #7
    //   808: istore #8
    //   810: iload #11
    //   812: iload #15
    //   814: if_icmpge -> 828
    //   817: aload_2
    //   818: iload #11
    //   820: invokevirtual charAt : (I)C
    //   823: istore #8
    //   825: goto -> 842
    //   828: iconst_0
    //   829: istore #9
    //   831: iload #8
    //   833: istore #7
    //   835: iload #9
    //   837: istore #8
    //   839: iload #10
    //   841: istore_0
    //   842: goto -> 133
    //   845: aload #6
    //   847: getstatic com/mysql/jdbc/StringUtils$SearchMode.SKIP_LINE_COMMENTS : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   850: invokeinterface contains : (Ljava/lang/Object;)Z
    //   855: ifeq -> 1171
    //   858: iload #9
    //   860: bipush #45
    //   862: if_icmpne -> 923
    //   865: iload #8
    //   867: bipush #45
    //   869: if_icmpne -> 923
    //   872: iload #7
    //   874: invokestatic isWhitespace : (C)Z
    //   877: ifne -> 917
    //   880: iload #7
    //   882: bipush #59
    //   884: if_icmpne -> 893
    //   887: iconst_1
    //   888: istore #10
    //   890: goto -> 896
    //   893: iconst_0
    //   894: istore #10
    //   896: iload #10
    //   898: istore #11
    //   900: iload #10
    //   902: ifne -> 937
    //   905: iload #10
    //   907: istore #11
    //   909: iload #7
    //   911: ifeq -> 937
    //   914: goto -> 926
    //   917: iconst_0
    //   918: istore #11
    //   920: goto -> 937
    //   923: iconst_0
    //   924: istore #10
    //   926: iload #9
    //   928: bipush #35
    //   930: if_icmpne -> 1171
    //   933: iload #10
    //   935: istore #11
    //   937: iload_0
    //   938: istore #10
    //   940: iload #9
    //   942: istore #12
    //   944: iload #11
    //   946: ifeq -> 1019
    //   949: iload_0
    //   950: iconst_1
    //   951: iadd
    //   952: iconst_1
    //   953: iadd
    //   954: istore_0
    //   955: iload_0
    //   956: iconst_1
    //   957: iadd
    //   958: istore #10
    //   960: iload #10
    //   962: iload #15
    //   964: if_icmpge -> 978
    //   967: aload_2
    //   968: iload #10
    //   970: invokevirtual charAt : (I)C
    //   973: istore #8
    //   975: goto -> 981
    //   978: iconst_0
    //   979: istore #8
    //   981: iload_0
    //   982: iconst_2
    //   983: iadd
    //   984: istore #11
    //   986: iload_0
    //   987: istore #10
    //   989: iload #8
    //   991: istore #7
    //   993: iload #11
    //   995: iload #15
    //   997: if_icmpge -> 526
    //   1000: aload_2
    //   1001: iload #11
    //   1003: invokevirtual charAt : (I)C
    //   1006: istore #9
    //   1008: iload #8
    //   1010: istore #7
    //   1012: iload #9
    //   1014: istore #8
    //   1016: goto -> 532
    //   1019: iinc #10, 1
    //   1022: iload #10
    //   1024: iload_1
    //   1025: if_icmpgt -> 1059
    //   1028: aload_2
    //   1029: iload #10
    //   1031: invokevirtual charAt : (I)C
    //   1034: istore_0
    //   1035: iload_0
    //   1036: istore #12
    //   1038: iload_0
    //   1039: bipush #10
    //   1041: if_icmpeq -> 1059
    //   1044: iload_0
    //   1045: istore #12
    //   1047: iload_0
    //   1048: bipush #13
    //   1050: if_icmpeq -> 1059
    //   1053: iload_0
    //   1054: istore #12
    //   1056: goto -> 1019
    //   1059: iload #10
    //   1061: iconst_1
    //   1062: iadd
    //   1063: istore #11
    //   1065: iload #11
    //   1067: iload #15
    //   1069: if_icmpge -> 1083
    //   1072: aload_2
    //   1073: iload #11
    //   1075: invokevirtual charAt : (I)C
    //   1078: istore #8
    //   1080: goto -> 1086
    //   1083: iconst_0
    //   1084: istore #8
    //   1086: iload #10
    //   1088: istore_0
    //   1089: iload #8
    //   1091: istore #7
    //   1093: iload #12
    //   1095: bipush #13
    //   1097: if_icmpne -> 1141
    //   1100: iload #10
    //   1102: istore_0
    //   1103: iload #8
    //   1105: istore #7
    //   1107: iload #8
    //   1109: bipush #10
    //   1111: if_icmpne -> 1141
    //   1114: iload #11
    //   1116: iconst_1
    //   1117: iadd
    //   1118: istore_0
    //   1119: iload_0
    //   1120: iload #15
    //   1122: if_icmpge -> 1135
    //   1125: aload_2
    //   1126: iload_0
    //   1127: invokevirtual charAt : (I)C
    //   1130: istore #7
    //   1132: goto -> 1138
    //   1135: iconst_0
    //   1136: istore #7
    //   1138: iload #11
    //   1140: istore_0
    //   1141: iload_0
    //   1142: iconst_2
    //   1143: iadd
    //   1144: istore #11
    //   1146: iload_0
    //   1147: istore #10
    //   1149: iload #7
    //   1151: istore #8
    //   1153: iload #11
    //   1155: iload #15
    //   1157: if_icmpge -> 828
    //   1160: aload_2
    //   1161: iload #11
    //   1163: invokevirtual charAt : (I)C
    //   1166: istore #8
    //   1168: goto -> 842
    //   1171: aload #6
    //   1173: getstatic com/mysql/jdbc/StringUtils$SearchMode.SKIP_WHITE_SPACE : Lcom/mysql/jdbc/StringUtils$SearchMode;
    //   1176: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1181: ifeq -> 1220
    //   1184: iload #9
    //   1186: invokestatic isWhitespace : (C)Z
    //   1189: ifne -> 1195
    //   1192: goto -> 1220
    //   1195: iload #7
    //   1197: istore #9
    //   1199: iload #8
    //   1201: istore #7
    //   1203: iload #9
    //   1205: istore #8
    //   1207: goto -> 133
    //   1210: iinc #0, 1
    //   1213: iload #7
    //   1215: istore #9
    //   1217: goto -> 53
    //   1220: iload_0
    //   1221: ireturn
    //   1222: iconst_m1
    //   1223: ireturn
  }
  
  public static int indexOfQuoteDoubleAware(String paramString1, String paramString2, int paramInt) {
    if (paramString1 == null || paramString2 == null || paramString2.length() == 0 || paramInt > paramString1.length())
      return -1; 
    int j = paramString1.length();
    boolean bool = true;
    int i = -1;
    while (bool) {
      i = paramString1.indexOf(paramString2, paramInt);
      if (i == -1 || i == j - 1 || !paramString1.startsWith(paramString2, i + 1)) {
        bool = false;
        continue;
      } 
      paramInt = i + 2;
    } 
    return i;
  }
  
  private static boolean isCharAtPosNotEqualIgnoreCase(String paramString, int paramInt, char paramChar1, char paramChar2) {
    boolean bool;
    if (Character.toLowerCase(paramString.charAt(paramInt)) != paramChar2 && Character.toUpperCase(paramString.charAt(paramInt)) != paramChar1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isCharEqualIgnoreCase(char paramChar1, char paramChar2, char paramChar3) {
    return (Character.toLowerCase(paramChar1) == paramChar3 || Character.toUpperCase(paramChar1) == paramChar2);
  }
  
  public static boolean isEmptyOrWhitespaceOnly(String paramString) {
    if (paramString != null && paramString.length() != 0) {
      int i = paramString.length();
      for (byte b = 0; b < i; b++) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
      } 
    } 
    return true;
  }
  
  public static boolean isNullOrEmpty(String paramString) {
    return (paramString == null || paramString.length() == 0);
  }
  
  public static boolean isStrictlyNumeric(CharSequence paramCharSequence) {
    if (paramCharSequence == null || paramCharSequence.length() == 0)
      return false; 
    for (byte b = 0; b < paramCharSequence.length(); b++) {
      if (!Character.isDigit(paramCharSequence.charAt(b)))
        return false; 
    } 
    return true;
  }
  
  public static final boolean isValidIdChar(char paramChar) {
    boolean bool;
    if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@".indexOf(paramChar) != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static int lastIndexOf(byte[] paramArrayOfbyte, char paramChar) {
    if (paramArrayOfbyte == null)
      return -1; 
    for (int i = paramArrayOfbyte.length - 1; i >= 0; i--) {
      if (paramArrayOfbyte[i] == paramChar)
        return i; 
    } 
    return -1;
  }
  
  public static String quoteIdentifier(String paramString1, String paramString2, boolean paramBoolean) {
    String str1;
    if (paramString1 == null)
      return null; 
    String str2 = paramString1.trim();
    int i = paramString2.length();
    paramString1 = str2;
    if (i != 0)
      if (" ".equals(paramString2)) {
        paramString1 = str2;
      } else {
        if (!paramBoolean && str2.startsWith(paramString2) && str2.endsWith(paramString2)) {
          paramString1 = str2.substring(i, str2.length() - i);
          int j = paramString1.indexOf(paramString2);
          while (j >= 0) {
            int k = j + i;
            int m = paramString1.indexOf(paramString2, k);
            if (m == k)
              j = paramString1.indexOf(paramString2, m + i); 
          } 
          if (j < 0)
            return str2; 
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(paramString2);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(paramString2);
        stringBuilder2.append(paramString2);
        stringBuilder1.append(str2.replaceAll(paramString2, stringBuilder2.toString()));
        stringBuilder1.append(paramString2);
        str1 = stringBuilder1.toString();
      }  
    return str1;
  }
  
  public static String quoteIdentifier(String paramString, boolean paramBoolean) {
    return quoteIdentifier(paramString, "`", paramBoolean);
  }
  
  public static byte[] s2b(String paramString, MySQLConnection paramMySQLConnection) throws SQLException {
    if (paramString == null)
      return null; 
    if (paramMySQLConnection != null && paramMySQLConnection.getUseUnicode())
      try {
        String str = paramMySQLConnection.getEncoding();
        if (str == null)
          return paramString.getBytes(); 
        SingleByteCharsetConverter singleByteCharsetConverter = paramMySQLConnection.getCharsetConverter(str);
        return (singleByteCharsetConverter != null) ? singleByteCharsetConverter.toBytes(paramString) : paramString.getBytes(str);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        return paramString.getBytes();
      }  
    return paramString.getBytes();
  }
  
  public static String sanitizeProcOrFuncName(String paramString) {
    return (paramString == null || paramString.equals("%")) ? null : paramString;
  }
  
  public static List<String> split(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean) {
    if (paramString1 == null)
      return new ArrayList<String>(); 
    if (paramString2 != null) {
      int i = 0;
      ArrayList<String> arrayList = new ArrayList();
      while (true) {
        int j = indexOfIgnoreCase(i, paramString1, paramString2, paramString3, paramString4, paramString5, SEARCH_MODE__MRK_COM_WS);
        if (j != -1) {
          String str2 = paramString1.substring(i, j);
          String str1 = str2;
          if (paramBoolean)
            str1 = str2.trim(); 
          arrayList.add(str1);
          i = j + 1;
          continue;
        } 
        if (i < paramString1.length()) {
          paramString2 = paramString1.substring(i);
          paramString1 = paramString2;
          if (paramBoolean)
            paramString1 = paramString2.trim(); 
          arrayList.add(paramString1);
        } 
        return arrayList;
      } 
    } 
    throw new IllegalArgumentException();
  }
  
  public static List<String> split(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) {
    return split(paramString1, paramString2, paramString3, paramString4, "", paramBoolean);
  }
  
  public static List<String> split(String paramString1, String paramString2, boolean paramBoolean) {
    if (paramString1 == null)
      return new ArrayList<String>(); 
    if (paramString2 != null) {
      StringTokenizer stringTokenizer = new StringTokenizer(paramString1, paramString2, false);
      ArrayList<String> arrayList = new ArrayList(stringTokenizer.countTokens());
      while (stringTokenizer.hasMoreTokens()) {
        paramString2 = stringTokenizer.nextToken();
        paramString1 = paramString2;
        if (paramBoolean)
          paramString1 = paramString2.trim(); 
        arrayList.add(paramString1);
      } 
      return arrayList;
    } 
    throw new IllegalArgumentException();
  }
  
  public static List<String> splitDBdotName(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
    int i;
    if (paramString1 == null || paramString1.equals("%"))
      return Collections.emptyList(); 
    if (" ".equals(paramString3)) {
      i = paramString1.indexOf(".");
    } else {
      Set<SearchMode> set;
      if (paramBoolean) {
        set = SEARCH_MODE__MRK_WS;
      } else {
        set = SEARCH_MODE__BSESC_MRK_WS;
      } 
      i = indexOfIgnoreCase(0, paramString1, ".", paramString3, paramString3, set);
    } 
    if (i != -1) {
      paramString2 = unQuoteIdentifier(paramString1.substring(0, i), paramString3);
      paramString1 = unQuoteIdentifier(paramString1.substring(i + 1), paramString3);
    } else {
      paramString1 = unQuoteIdentifier(paramString1, paramString3);
    } 
    return Arrays.asList(new String[] { paramString2, paramString1 });
  }
  
  private static boolean startsWith(byte[] paramArrayOfbyte, String paramString) {
    int i = paramString.length();
    if (paramArrayOfbyte.length < i)
      return false; 
    for (byte b = 0; b < i; b++) {
      if (paramArrayOfbyte[b] != paramString.charAt(b))
        return false; 
    } 
    return true;
  }
  
  public static boolean startsWithIgnoreCase(String paramString1, int paramInt, String paramString2) {
    return paramString1.regionMatches(true, paramInt, paramString2, 0, paramString2.length());
  }
  
  public static boolean startsWithIgnoreCase(String paramString1, String paramString2) {
    return startsWithIgnoreCase(paramString1, 0, paramString2);
  }
  
  public static boolean startsWithIgnoreCaseAndNonAlphaNumeric(String paramString1, String paramString2) {
    byte b = 0;
    boolean bool = false;
    if (paramString1 == null) {
      if (paramString2 == null)
        bool = true; 
      return bool;
    } 
    int i = paramString1.length();
    while (b < i && !Character.isLetterOrDigit(paramString1.charAt(b)))
      b++; 
    return startsWithIgnoreCase(paramString1, b, paramString2);
  }
  
  public static int startsWithIgnoreCaseAndWs(String paramString, String[] paramArrayOfString) {
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      if (startsWithIgnoreCaseAndWs(paramString, paramArrayOfString[b], 0))
        return b; 
    } 
    return -1;
  }
  
  public static boolean startsWithIgnoreCaseAndWs(String paramString1, String paramString2) {
    return startsWithIgnoreCaseAndWs(paramString1, paramString2, 0);
  }
  
  public static boolean startsWithIgnoreCaseAndWs(String paramString1, String paramString2, int paramInt) {
    if (paramString1 == null) {
      boolean bool;
      if (paramString2 == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    int i = paramString1.length();
    while (paramInt < i && Character.isWhitespace(paramString1.charAt(paramInt)))
      paramInt++; 
    return startsWithIgnoreCase(paramString1, paramInt, paramString2);
  }
  
  public static String stripComments(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (paramString1 == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder(paramString1.length());
    StringReader stringReader = new StringReader(paramString1);
    int j = -1;
    int i = 0;
    try {
      while (true) {
        int k = stringReader.read();
        if (k != -1) {
          int m;
          int n;
          int i1;
          if (j != -1 && k == paramString3.charAt(j)) {
            n = -1;
            i1 = 0;
          } else {
            m = paramString2.indexOf(k);
            n = j;
            i1 = i;
            if (m != -1) {
              n = j;
              i1 = i;
              if (!i) {
                i1 = k;
                n = m;
              } 
            } 
          } 
          if (i1 == 0 && k == 47 && (paramBoolean2 || paramBoolean1)) {
            k = stringReader.read();
            if (k == 42 && paramBoolean1) {
              k = 0;
              while (true) {
                int i2;
                while (true) {
                  i2 = stringReader.read();
                  break;
                } 
                if (i2 < 0) {
                  j = n;
                  i = i1;
                } 
              } 
            } 
            m = k;
            if (k == 47) {
              m = k;
              if (paramBoolean2)
                while (true) {
                  k = stringReader.read();
                  m = k;
                  if (k != 10) {
                    m = k;
                    if (k != 13) {
                      m = k;
                      if (k >= 0)
                        continue; 
                    } 
                  } 
                  break;
                }  
            } 
          } else if (i1 == 0 && k == 35 && paramBoolean3) {
            while (true) {
              k = stringReader.read();
              m = k;
              if (k != 10) {
                m = k;
                if (k != 13) {
                  m = k;
                  if (k >= 0)
                    continue; 
                } 
              } 
              break;
            } 
          } else {
            m = k;
            if (i1 == 0) {
              m = k;
              if (k == 45) {
                m = k;
                if (paramBoolean4) {
                  m = stringReader.read();
                  if (m == -1 || m != 45) {
                    stringBuilder.append('-');
                    j = n;
                    i = i1;
                    if (m != -1) {
                      stringBuilder.append((char)m);
                      j = n;
                      i = i1;
                    } 
                    continue;
                  } 
                  while (true) {
                    k = stringReader.read();
                    m = k;
                    if (k != 10) {
                      m = k;
                      if (k != 13) {
                        m = k;
                        if (k >= 0)
                          continue; 
                      } 
                    } 
                    break;
                  } 
                } 
              } 
            } 
          } 
          j = n;
          i = i1;
          if (m != -1) {
            stringBuilder.append((char)m);
            j = n;
            i = i1;
          } 
          continue;
        } 
        break;
      } 
    } catch (IOException iOException) {}
    return stringBuilder.toString();
  }
  
  public static byte[] stripEnclosure(byte[] paramArrayOfbyte, String paramString1, String paramString2) {
    if (paramArrayOfbyte.length >= paramString1.length() + paramString2.length() && startsWith(paramArrayOfbyte, paramString1) && endsWith(paramArrayOfbyte, paramString2)) {
      int i = paramString1.length();
      int j = paramString2.length();
      i = paramArrayOfbyte.length - i + j;
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(paramArrayOfbyte, paramString1.length(), arrayOfByte, 0, i);
      return arrayOfByte;
    } 
    return paramArrayOfbyte;
  }
  
  public static String toAsciiString(byte[] paramArrayOfbyte) {
    return toAsciiString(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static String toAsciiString(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    char[] arrayOfChar = new char[paramInt2];
    boolean bool = false;
    int i = paramInt1;
    for (paramInt1 = bool; paramInt1 < paramInt2; paramInt1++) {
      arrayOfChar[paramInt1] = (char)paramArrayOfbyte[i];
      i++;
    } 
    return new String(arrayOfChar);
  }
  
  public static String toString(byte[] paramArrayOfbyte) {
    try {
      return findCharset(platformEncoding).decode(ByteBuffer.wrap(paramArrayOfbyte)).toString();
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return null;
    } 
  }
  
  public static String toString(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      return findCharset(platformEncoding).decode(ByteBuffer.wrap(paramArrayOfbyte, paramInt1, paramInt2)).toString();
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return null;
    } 
  }
  
  public static String toString(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, String paramString) throws UnsupportedEncodingException {
    return findCharset(paramString).decode(ByteBuffer.wrap(paramArrayOfbyte, paramInt1, paramInt2)).toString();
  }
  
  public static String toString(byte[] paramArrayOfbyte, String paramString) throws UnsupportedEncodingException {
    return findCharset(paramString).decode(ByteBuffer.wrap(paramArrayOfbyte)).toString();
  }
  
  public static String unQuoteIdentifier(String paramString1, String paramString2) {
    if (paramString1 == null)
      return null; 
    String str = paramString1.trim();
    int i = paramString2.length();
    paramString1 = str;
    if (i != 0)
      if (" ".equals(paramString2)) {
        paramString1 = str;
      } else {
        paramString1 = str;
        if (str.startsWith(paramString2)) {
          paramString1 = str;
          if (str.endsWith(paramString2)) {
            paramString1 = str.substring(i, str.length() - i);
            int j = paramString1.indexOf(paramString2);
            while (j >= 0) {
              j += i;
              int k = paramString1.indexOf(paramString2, j);
              if (k == j) {
                j = paramString1.indexOf(paramString2, k + i);
                continue;
              } 
              return str;
            } 
            paramString1 = str.substring(i, str.length() - i);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(paramString2);
            stringBuilder.append(paramString2);
            paramString1 = paramString1.replaceAll(stringBuilder.toString(), paramString2);
          } 
        } 
      }  
    return paramString1;
  }
  
  public static boolean wildCompareIgnoreCase(String paramString1, String paramString2) {
    boolean bool;
    if (wildCompareInternal(paramString1, paramString2) == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static int wildCompareInternal(String paramString1, String paramString2) {
    int k;
    if (paramString1 == null || paramString2 == null)
      return -1; 
    boolean bool = paramString2.equals("%");
    boolean bool1 = false;
    boolean bool2 = false;
    if (bool)
      return 0; 
    int n = paramString2.length();
    int m = paramString1.length();
    int i = 0;
    int j = 0;
    byte b = -1;
    while (true) {
      k = j;
      if (i != n) {
        while (paramString2.charAt(i) != '%' && paramString2.charAt(i) != '_') {
          k = i;
          if (paramString2.charAt(i) == '\\') {
            int i2 = i + 1;
            k = i;
            if (i2 != n)
              k = i2; 
          } 
          if (j != m) {
            i = k + 1;
            char c = Character.toUpperCase(paramString2.charAt(k));
            k = j + 1;
            if (c == Character.toUpperCase(paramString1.charAt(j))) {
              if (i == n) {
                i = bool2;
                if (k != m)
                  i = 1; 
                return i;
              } 
              j = k;
              b = 1;
              continue;
            } 
          } 
          return 1;
        } 
        int i1 = i;
        k = j;
        if (paramString2.charAt(i) == '_') {
          k = j;
          i1 = i;
          while (true) {
            if (k == m)
              return b; 
            i = k + 1;
            j = i1 + 1;
            if (j < n) {
              i1 = j;
              k = i;
              if (paramString2.charAt(j) != '_')
                break; 
              continue;
            } 
            break;
          } 
          i1 = j;
          k = i;
          if (j == n) {
            k = i;
            break;
          } 
        } 
        i = i1;
        j = k;
        if (paramString2.charAt(i1) == '%') {
          for (i = i1 + 1; i != n; i++) {
            if (paramString2.charAt(i) != '%')
              if (paramString2.charAt(i) == '_') {
                if (k == m)
                  return -1; 
                k++;
              } else {
                break;
              }  
          } 
          if (i == n)
            return 0; 
          if (k == m)
            return -1; 
          char c = paramString2.charAt(i);
          if (c == '\\') {
            j = i + 1;
            if (j != n) {
              c = paramString2.charAt(j);
              i = j;
            } 
          } 
          while (true) {
            if (k != m && Character.toUpperCase(paramString1.charAt(k)) != Character.toUpperCase(c)) {
              k++;
              continue;
            } 
            j = k + 1;
            if (k == m)
              return -1; 
            k = wildCompareInternal(paramString1.substring(j), paramString2.substring(1 + i));
            if (k <= 0)
              return k; 
            if (j == m)
              return -1; 
            k = j;
          } 
        } 
        continue;
      } 
      break;
    } 
    i = bool1;
    if (k != m)
      i = 1; 
    return i;
  }
  
  public enum SearchMode {
    ALLOW_BACKSLASH_ESCAPE, SKIP_BETWEEN_MARKERS, SKIP_BLOCK_COMMENTS, SKIP_LINE_COMMENTS, SKIP_WHITE_SPACE;
    
    private static final SearchMode[] $VALUES;
    
    static {
      SearchMode searchMode1 = new SearchMode("ALLOW_BACKSLASH_ESCAPE", 0);
      ALLOW_BACKSLASH_ESCAPE = searchMode1;
      SearchMode searchMode5 = new SearchMode("SKIP_BETWEEN_MARKERS", 1);
      SKIP_BETWEEN_MARKERS = searchMode5;
      SearchMode searchMode2 = new SearchMode("SKIP_BLOCK_COMMENTS", 2);
      SKIP_BLOCK_COMMENTS = searchMode2;
      SearchMode searchMode3 = new SearchMode("SKIP_LINE_COMMENTS", 3);
      SKIP_LINE_COMMENTS = searchMode3;
      SearchMode searchMode4 = new SearchMode("SKIP_WHITE_SPACE", 4);
      SKIP_WHITE_SPACE = searchMode4;
      $VALUES = new SearchMode[] { searchMode1, searchMode5, searchMode2, searchMode3, searchMode4 };
    }
  }
}
