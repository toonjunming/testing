package com.mysql.jdbc.util;

public class Base64Decoder {
  private static byte[] decoderMap = new byte[] { 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 
      54, 55, 56, 57, 58, 59, 60, 61, -1, -1, 
      -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 
      5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
      25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 
      29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
      39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
      49, 50, 51, -1, -1, -1, -1, -1 };
  
  public static byte[] decode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    IntWrapper intWrapper = new IntWrapper(paramInt1);
    byte[] arrayOfByte2 = new byte[4];
    byte[] arrayOfByte1 = new byte[paramInt2 * 3 / 4];
    int i = intWrapper.value + paramInt2 - 1;
    paramInt1 = 0;
    while (intWrapper.value <= i) {
      arrayOfByte2[0] = decoderMap[getNextValidByte(paramArrayOfbyte, intWrapper, i)];
      arrayOfByte2[1] = decoderMap[getNextValidByte(paramArrayOfbyte, intWrapper, i)];
      arrayOfByte2[2] = decoderMap[getNextValidByte(paramArrayOfbyte, intWrapper, i)];
      arrayOfByte2[3] = decoderMap[getNextValidByte(paramArrayOfbyte, intWrapper, i)];
      paramInt2 = paramInt1;
      if (arrayOfByte2[1] != -2) {
        arrayOfByte1[paramInt1] = (byte)(arrayOfByte2[0] << 2 | arrayOfByte2[1] >>> 4);
        paramInt2 = paramInt1 + 1;
      } 
      int j = paramInt2;
      if (arrayOfByte2[2] != -2) {
        arrayOfByte1[paramInt2] = (byte)((arrayOfByte2[1] & 0xF) << 4 | arrayOfByte2[2] >>> 2);
        j = paramInt2 + 1;
      } 
      paramInt1 = j;
      if (arrayOfByte2[3] != -2) {
        arrayOfByte1[j] = (byte)((arrayOfByte2[2] & 0x3) << 6 | arrayOfByte2[3]);
        paramInt1 = j + 1;
      } 
    } 
    paramArrayOfbyte = new byte[paramInt1];
    System.arraycopy(arrayOfByte1, 0, paramArrayOfbyte, 0, paramInt1);
    return paramArrayOfbyte;
  }
  
  private static byte getNextValidByte(byte[] paramArrayOfbyte, IntWrapper paramIntWrapper, int paramInt) {
    while (true) {
      int i = paramIntWrapper.value;
      if (i <= paramInt) {
        if (paramArrayOfbyte[i] >= 0 && decoderMap[paramArrayOfbyte[i]] >= 0) {
          paramIntWrapper.value = i + 1;
          return paramArrayOfbyte[i];
        } 
        paramIntWrapper.value = i + 1;
        continue;
      } 
      return 61;
    } 
  }
  
  public static class IntWrapper {
    public int value;
    
    public IntWrapper(int param1Int) {
      this.value = param1Int;
    }
  }
}
