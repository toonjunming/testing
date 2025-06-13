package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;

public class StandardDecrypter implements Decrypter {
  private ZipCryptoEngine zipCryptoEngine = new ZipCryptoEngine();
  
  public StandardDecrypter(char[] paramArrayOfchar, long paramLong1, long paramLong2, byte[] paramArrayOfbyte) throws ZipException {
    init(paramArrayOfbyte, paramArrayOfchar, paramLong2, paramLong1);
  }
  
  private void init(byte[] paramArrayOfbyte, char[] paramArrayOfchar, long paramLong1, long paramLong2) throws ZipException {
    if (paramArrayOfchar != null && paramArrayOfchar.length > 0) {
      this.zipCryptoEngine.initKeys(paramArrayOfchar);
      int i = 0;
      byte b = paramArrayOfbyte[0];
      while (i < 12) {
        int j = i + 1;
        if (j == 12) {
          i = (byte)(this.zipCryptoEngine.decryptByte() ^ b);
          if (i != (byte)(int)(paramLong2 >> 24L) && i != (byte)(int)(paramLong1 >> 8L))
            throw new ZipException("Wrong password!", ZipException.Type.WRONG_PASSWORD); 
        } 
        ZipCryptoEngine zipCryptoEngine = this.zipCryptoEngine;
        zipCryptoEngine.updateKeys((byte)(zipCryptoEngine.decryptByte() ^ b));
        i = j;
        if (j != 12) {
          b = paramArrayOfbyte[j];
          i = j;
        } 
      } 
      return;
    } 
    throw new ZipException("Wrong password!", ZipException.Type.WRONG_PASSWORD);
  }
  
  public int decryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException {
    if (paramInt1 >= 0 && paramInt2 >= 0) {
      for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
        byte b3 = paramArrayOfbyte[i];
        byte b2 = this.zipCryptoEngine.decryptByte();
        ZipCryptoEngine zipCryptoEngine = this.zipCryptoEngine;
        byte b1 = (byte)((b3 & 0xFF ^ b2) & 0xFF);
        zipCryptoEngine.updateKeys(b1);
        paramArrayOfbyte[i] = b1;
      } 
      return paramInt2;
    } 
    throw new ZipException("one of the input parameters were null in standard decrypt data");
  }
}
