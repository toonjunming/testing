package net.lingala.zip4j.crypto.engine;

import net.lingala.zip4j.util.Zip4jUtil;

public class ZipCryptoEngine {
  private static final int[] CRC_TABLE = new int[256];
  
  private final int[] keys = new int[3];
  
  static {
    for (byte b = 0; b < 'Ā'; b++) {
      int i = b;
      for (byte b1 = 0; b1 < 8; b1++) {
        if ((i & 0x1) == 1) {
          i = i >>> 1 ^ 0xEDB88320;
        } else {
          i >>>= 1;
        } 
      } 
      CRC_TABLE[b] = i;
    } 
  }
  
  private int crc32(int paramInt, byte paramByte) {
    return CRC_TABLE[(paramInt ^ paramByte) & 0xFF] ^ paramInt >>> 8;
  }
  
  public byte decryptByte() {
    int i = this.keys[2] | 0x2;
    return (byte)(i * (i ^ 0x1) >>> 8);
  }
  
  public void initKeys(char[] paramArrayOfchar) {
    int[] arrayOfInt = this.keys;
    byte b = 0;
    arrayOfInt[0] = 305419896;
    arrayOfInt[1] = 591751049;
    arrayOfInt[2] = 878082192;
    byte[] arrayOfByte = Zip4jUtil.convertCharArrayToByteArray(paramArrayOfchar);
    int i = arrayOfByte.length;
    while (b < i) {
      updateKeys((byte)(arrayOfByte[b] & 0xFF));
      b++;
    } 
  }
  
  public void updateKeys(byte paramByte) {
    int[] arrayOfInt = this.keys;
    arrayOfInt[0] = crc32(arrayOfInt[0], paramByte);
    arrayOfInt = this.keys;
    arrayOfInt[1] = arrayOfInt[1] + (arrayOfInt[0] & 0xFF);
    arrayOfInt[1] = arrayOfInt[1] * 134775813 + 1;
    arrayOfInt[2] = crc32(arrayOfInt[2], (byte)(arrayOfInt[1] >> 24));
  }
}
