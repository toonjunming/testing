package com.mysql.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SingleByteCharsetConverter {
  static {
    CONVERTER_MAP = new HashMap<String, SingleByteCharsetConverter>();
    byte b2 = 0;
    EMPTY_BYTE_ARRAY = new byte[0];
    unknownCharsMap = new byte[65536];
    byte b = -128;
    while (true) {
      b1 = b2;
      if (b <= Byte.MAX_VALUE) {
        allBytes[b + 128] = (byte)b;
        b++;
        continue;
      } 
      break;
    } 
    while (true) {
      byte[] arrayOfByte = unknownCharsMap;
      if (b1 < arrayOfByte.length) {
        arrayOfByte[b1] = 63;
        b1++;
        continue;
      } 
      break;
    } 
  }
  
  private SingleByteCharsetConverter(String paramString) throws UnsupportedEncodingException {
    byte[] arrayOfByte1 = allBytes;
    byte b = 0;
    paramString = new String(arrayOfByte1, 0, 256, paramString);
    int i = paramString.length();
    arrayOfByte1 = unknownCharsMap;
    byte[] arrayOfByte2 = this.charToByteMap;
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
    while (b < 'Ā' && b < i) {
      char c = paramString.charAt(b);
      this.byteToChars[b] = c;
      this.charToByteMap[c] = allBytes[b];
      b++;
    } 
  }
  
  public static SingleByteCharsetConverter getInstance(String paramString, Connection paramConnection) throws UnsupportedEncodingException, SQLException {
    // Byte code:
    //   0: ldc com/mysql/jdbc/SingleByteCharsetConverter
    //   2: monitorenter
    //   3: getstatic com/mysql/jdbc/SingleByteCharsetConverter.CONVERTER_MAP : Ljava/util/Map;
    //   6: aload_0
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/mysql/jdbc/SingleByteCharsetConverter
    //   15: astore_2
    //   16: aload_2
    //   17: astore_1
    //   18: aload_2
    //   19: ifnonnull -> 27
    //   22: aload_0
    //   23: invokestatic initCharset : (Ljava/lang/String;)Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   26: astore_1
    //   27: ldc com/mysql/jdbc/SingleByteCharsetConverter
    //   29: monitorexit
    //   30: aload_1
    //   31: areturn
    //   32: astore_0
    //   33: ldc com/mysql/jdbc/SingleByteCharsetConverter
    //   35: monitorexit
    //   36: aload_0
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	32	finally
    //   22	27	32	finally
  }
  
  public static SingleByteCharsetConverter initCharset(String paramString) throws UnsupportedEncodingException, SQLException {
    try {
      boolean bool = CharsetMapping.isMultibyteCharset(paramString);
      if (bool)
        return null; 
      SingleByteCharsetConverter singleByteCharsetConverter = new SingleByteCharsetConverter(paramString);
      CONVERTER_MAP.put(paramString, singleByteCharsetConverter);
      return singleByteCharsetConverter;
    } catch (RuntimeException runtimeException) {
      SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
      sQLException.initCause(runtimeException);
      throw sQLException;
    } 
  }
  
  public static String toStringDefaultEncoding(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    return new String(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public final byte[] toBytes(String paramString) {
    if (paramString == null)
      return null; 
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i];
    for (byte b = 0; b < i; b++)
      arrayOfByte[b] = this.charToByteMap[paramString.charAt(b)]; 
    return arrayOfByte;
  }
  
  public final byte[] toBytes(String paramString, int paramInt1, int paramInt2) {
    if (paramString == null)
      return null; 
    if (paramInt2 == 0)
      return EMPTY_BYTE_ARRAY; 
    byte[] arrayOfByte = new byte[paramInt2];
    for (byte b = 0; b < paramInt2; b++) {
      char c = paramString.charAt(b + paramInt1);
      arrayOfByte[b] = this.charToByteMap[c];
    } 
    return arrayOfByte;
  }
  
  public final byte[] toBytes(char[] paramArrayOfchar) {
    if (paramArrayOfchar == null)
      return null; 
    int i = paramArrayOfchar.length;
    byte[] arrayOfByte = new byte[i];
    for (byte b = 0; b < i; b++)
      arrayOfByte[b] = this.charToByteMap[paramArrayOfchar[b]]; 
    return arrayOfByte;
  }
  
  public final byte[] toBytes(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    if (paramArrayOfchar == null)
      return null; 
    if (paramInt2 == 0)
      return EMPTY_BYTE_ARRAY; 
    byte[] arrayOfByte = new byte[paramInt2];
    for (byte b = 0; b < paramInt2; b++)
      arrayOfByte[b] = this.charToByteMap[paramArrayOfchar[b + paramInt1]]; 
    return arrayOfByte;
  }
  
  public final byte[] toBytesWrapped(String paramString, char paramChar1, char paramChar2) {
    if (paramString == null)
      return null; 
    int j = paramString.length();
    int i = j + 2;
    byte[] arrayOfByte = new byte[i];
    byte b = this.charToByteMap[paramChar1];
    paramChar1 = Character.MIN_VALUE;
    arrayOfByte[0] = b;
    while (paramChar1 < j) {
      int m = paramChar1 + 1;
      arrayOfByte[m] = this.charToByteMap[paramString.charAt(paramChar1)];
      int k = m;
    } 
    arrayOfByte[i - 1] = this.charToByteMap[paramChar2];
    return arrayOfByte;
  }
  
  public final byte[] toBytesWrapped(char[] paramArrayOfchar, char paramChar1, char paramChar2) {
    if (paramArrayOfchar == null)
      return null; 
    int i = paramArrayOfchar.length + 2;
    int j = paramArrayOfchar.length;
    byte[] arrayOfByte = new byte[i];
    byte b = this.charToByteMap[paramChar1];
    paramChar1 = Character.MIN_VALUE;
    arrayOfByte[0] = b;
    while (paramChar1 < j) {
      int m = paramChar1 + 1;
      arrayOfByte[m] = this.charToByteMap[paramArrayOfchar[paramChar1]];
      int k = m;
    } 
    arrayOfByte[i - 1] = this.charToByteMap[paramChar2];
    return arrayOfByte;
  }
  
  public final String toString(byte[] paramArrayOfbyte) {
    return toString(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public final String toString(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    char[] arrayOfChar = new char[paramInt2];
    for (byte b = 0; b < paramInt2; b++) {
      arrayOfChar[b] = this.byteToChars[paramArrayOfbyte[paramInt1] + 128];
      paramInt1++;
    } 
    return new String(arrayOfChar);
  }
  
  static {
    byte b1;
  }
  
  private static final int BYTE_RANGE = 256;
  
  private static final Map<String, SingleByteCharsetConverter> CONVERTER_MAP;
  
  private static final byte[] EMPTY_BYTE_ARRAY;
  
  private static byte[] allBytes = new byte[256];
  
  private static byte[] unknownCharsMap;
  
  private char[] byteToChars = new char[256];
  
  private byte[] charToByteMap = new byte[65536];
}
