package com.mysql.jdbc;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
  private static int CACHING_SHA2_DIGEST_LENGTH = 32;
  
  private static final char PVERSION41_CHAR = '*';
  
  private static final int SHA1_HASH_SIZE = 20;
  
  private static int charVal(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9') {
      paramChar -= '0';
    } else {
      if (paramChar >= 'A' && paramChar <= 'Z') {
        paramChar -= 'A';
      } else {
        paramChar -= 'a';
      } 
      paramChar += '\n';
    } 
    return paramChar;
  }
  
  public static byte[] createKeyFromOldPassword(String paramString) throws NoSuchAlgorithmException {
    return getBinaryPassword(getSaltFromPassword(makeScrambledPassword(paramString)), false);
  }
  
  public static byte[] getBinaryPassword(int[] paramArrayOfint, boolean paramBoolean) throws NoSuchAlgorithmException {
    byte[] arrayOfByte = new byte[20];
    byte b1 = 0;
    if (paramBoolean) {
      byte b = 0;
      while (b1 < 4) {
        int i = paramArrayOfint[b1];
        byte b3 = 3;
        while (b3 >= 0) {
          arrayOfByte[b] = (byte)(i & 0xFF);
          i >>= 8;
          b3--;
          b++;
        } 
        b1++;
      } 
      return arrayOfByte;
    } 
    b1 = 0;
    byte b2 = 0;
    while (b1 < 2) {
      int i = paramArrayOfint[b1];
      for (byte b = 3; b >= 0; b--) {
        arrayOfByte[b + b2] = (byte)(i % 256);
        i >>= 8;
      } 
      b2 += 4;
      b1++;
    } 
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    messageDigest.update(arrayOfByte, 0, 8);
    return messageDigest.digest();
  }
  
  private static int[] getSaltFromPassword(String paramString) {
    int[] arrayOfInt = new int[6];
    if (paramString != null && paramString.length() != 0) {
      byte b1 = 0;
      if (paramString.charAt(0) == '*') {
        paramString = paramString.substring(1, 5);
        while (b1 < 4) {
          charVal(paramString.charAt(b1));
          b1++;
        } 
        return arrayOfInt;
      } 
      int i = paramString.length();
      byte b2 = 0;
      for (b1 = 0; b2 < i; b1++) {
        byte b = 0;
        int j = 0;
        while (b < 8) {
          j = (j << 4) + charVal(paramString.charAt(b2));
          b++;
          b2++;
        } 
        arrayOfInt[b1] = j;
      } 
    } 
    return arrayOfInt;
  }
  
  private static String longToHex(long paramLong) {
    String str = Long.toHexString(paramLong);
    int i = str.length();
    byte b = 0;
    if (i < 8) {
      StringBuilder stringBuilder = new StringBuilder();
      while (b < 8 - i) {
        stringBuilder.append("0");
        b++;
      } 
      stringBuilder.append(str);
      return stringBuilder.toString();
    } 
    return str.substring(0, 8);
  }
  
  public static String makeScrambledPassword(String paramString) throws NoSuchAlgorithmException {
    long[] arrayOfLong = Util.hashPre41Password(paramString);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(longToHex(arrayOfLong[0]));
    stringBuilder.append(longToHex(arrayOfLong[1]));
    return stringBuilder.toString();
  }
  
  public static byte[] passwordHashStage1(String paramString) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramString.length();
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (c != ' ' && c != '\t')
        stringBuilder.append(c); 
    } 
    return messageDigest.digest(StringUtils.getBytes(stringBuilder.toString()));
  }
  
  public static byte[] passwordHashStage2(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    messageDigest.update(paramArrayOfbyte2, 0, 4);
    messageDigest.update(paramArrayOfbyte1, 0, 20);
    return messageDigest.digest();
  }
  
  public static byte[] scramble411(String paramString1, String paramString2, String paramString3) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    if (paramString3 == null || paramString3.length() == 0) {
      arrayOfByte1 = StringUtils.getBytes(paramString1);
    } else {
      arrayOfByte1 = StringUtils.getBytes((String)arrayOfByte1, paramString3);
    } 
    byte[] arrayOfByte1 = messageDigest.digest(arrayOfByte1);
    messageDigest.reset();
    byte[] arrayOfByte3 = messageDigest.digest(arrayOfByte1);
    messageDigest.reset();
    messageDigest.update(StringUtils.getBytes(paramString2, "ASCII"));
    messageDigest.update(arrayOfByte3);
    byte[] arrayOfByte2 = messageDigest.digest();
    int i = arrayOfByte2.length;
    for (byte b = 0; b < i; b++)
      arrayOfByte2[b] = (byte)(arrayOfByte2[b] ^ arrayOfByte1[b]); 
    return arrayOfByte2;
  }
  
  public static byte[] scrambleCachingSha2(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws DigestException {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      int i = CACHING_SHA2_DIGEST_LENGTH;
      byte[] arrayOfByte2 = new byte[i];
      byte[] arrayOfByte3 = new byte[i];
      byte[] arrayOfByte1 = new byte[i];
      messageDigest.update(paramArrayOfbyte1, 0, paramArrayOfbyte1.length);
      messageDigest.digest(arrayOfByte2, 0, CACHING_SHA2_DIGEST_LENGTH);
      messageDigest.reset();
      messageDigest.update(arrayOfByte2, 0, i);
      messageDigest.digest(arrayOfByte3, 0, CACHING_SHA2_DIGEST_LENGTH);
      messageDigest.reset();
      messageDigest.update(arrayOfByte3, 0, i);
      messageDigest.update(paramArrayOfbyte2, 0, paramArrayOfbyte2.length);
      messageDigest.digest(arrayOfByte1, 0, CACHING_SHA2_DIGEST_LENGTH);
      i = CACHING_SHA2_DIGEST_LENGTH;
      paramArrayOfbyte1 = new byte[i];
      xorString(arrayOfByte2, paramArrayOfbyte1, arrayOfByte1, i);
      return paramArrayOfbyte1;
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionFailedException(noSuchAlgorithmException);
    } 
  }
  
  public static void xorString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt) {
    int i = paramArrayOfbyte3.length;
    for (byte b = 0; b < paramInt; b++)
      paramArrayOfbyte2[b] = (byte)(paramArrayOfbyte1[b] ^ paramArrayOfbyte3[b % i]); 
  }
}
