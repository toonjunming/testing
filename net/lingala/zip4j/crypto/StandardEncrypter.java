package net.lingala.zip4j.crypto;

import java.security.SecureRandom;
import java.util.Objects;
import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;

public class StandardEncrypter implements Encrypter {
  private byte[] headerBytes;
  
  private final ZipCryptoEngine zipCryptoEngine = new ZipCryptoEngine();
  
  public StandardEncrypter(char[] paramArrayOfchar, long paramLong) throws ZipException {
    init(paramArrayOfchar, paramLong);
  }
  
  private void init(char[] paramArrayOfchar, long paramLong) throws ZipException {
    if (paramArrayOfchar != null && paramArrayOfchar.length > 0) {
      this.zipCryptoEngine.initKeys(paramArrayOfchar);
      this.headerBytes = generateRandomBytes();
      this.zipCryptoEngine.initKeys(paramArrayOfchar);
      byte[] arrayOfByte = this.headerBytes;
      arrayOfByte[11] = (byte)(int)(paramLong >>> 24L);
      arrayOfByte[10] = (byte)(int)(paramLong >>> 16L);
      encryptData(arrayOfByte);
      return;
    } 
    throw new ZipException("input password is null or empty, cannot initialize standard encrypter");
  }
  
  public byte encryptByte(byte paramByte) {
    byte b = (byte)(this.zipCryptoEngine.decryptByte() & 0xFF ^ paramByte);
    this.zipCryptoEngine.updateKeys(paramByte);
    return b;
  }
  
  public int encryptData(byte[] paramArrayOfbyte) throws ZipException {
    Objects.requireNonNull(paramArrayOfbyte);
    return encryptData(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int encryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException {
    if (paramInt2 >= 0) {
      for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
        paramArrayOfbyte[i] = encryptByte(paramArrayOfbyte[i]); 
      return paramInt2;
    } 
    throw new ZipException("invalid length specified to decrpyt data");
  }
  
  public byte[] generateRandomBytes() {
    byte[] arrayOfByte = new byte[12];
    SecureRandom secureRandom = new SecureRandom();
    for (byte b = 0; b < 12; b++)
      arrayOfByte[b] = encryptByte((byte)secureRandom.nextInt(256)); 
    return arrayOfByte;
  }
  
  public byte[] getHeaderBytes() {
    return this.headerBytes;
  }
}
