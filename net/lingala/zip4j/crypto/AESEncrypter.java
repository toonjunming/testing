package net.lingala.zip4j.crypto;

import java.security.SecureRandom;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.enums.AesKeyStrength;

public class AESEncrypter implements Encrypter {
  private AESEngine aesEngine;
  
  private final byte[] counterBlock;
  
  private byte[] derivedPasswordVerifier;
  
  private boolean finished;
  
  private final byte[] iv;
  
  private int loopCount = 0;
  
  private MacBasedPRF mac;
  
  private int nonce = 1;
  
  private final SecureRandom random = new SecureRandom();
  
  private byte[] saltBytes;
  
  public AESEncrypter(char[] paramArrayOfchar, AesKeyStrength paramAesKeyStrength) throws ZipException {
    if (paramArrayOfchar != null && paramArrayOfchar.length != 0) {
      if (paramAesKeyStrength == AesKeyStrength.KEY_STRENGTH_128 || paramAesKeyStrength == AesKeyStrength.KEY_STRENGTH_256) {
        this.finished = false;
        this.counterBlock = new byte[16];
        this.iv = new byte[16];
        init(paramArrayOfchar, paramAesKeyStrength);
        return;
      } 
      throw new ZipException("Invalid AES key strength");
    } 
    throw new ZipException("input password is empty or null");
  }
  
  private byte[] generateSalt(int paramInt) throws ZipException {
    if (paramInt == 8 || paramInt == 16) {
      byte b;
      if (paramInt == 8) {
        b = 2;
      } else {
        b = 4;
      } 
      byte[] arrayOfByte = new byte[paramInt];
      for (paramInt = 0; paramInt < b; paramInt++) {
        int j = this.random.nextInt();
        int i = paramInt * 4;
        arrayOfByte[i] = (byte)(j >> 24);
        arrayOfByte[i + 1] = (byte)(j >> 16);
        arrayOfByte[i + 2] = (byte)(j >> 8);
        arrayOfByte[i + 3] = (byte)j;
      } 
      return arrayOfByte;
    } 
    throw new ZipException("invalid salt size, cannot generate salt");
  }
  
  private void init(char[] paramArrayOfchar, AesKeyStrength paramAesKeyStrength) throws ZipException {
    byte[] arrayOfByte2 = generateSalt(paramAesKeyStrength.getSaltLength());
    this.saltBytes = arrayOfByte2;
    byte[] arrayOfByte1 = AesCipherUtil.derivePasswordBasedKey(arrayOfByte2, paramArrayOfchar, paramAesKeyStrength);
    this.derivedPasswordVerifier = AesCipherUtil.derivePasswordVerifier(arrayOfByte1, paramAesKeyStrength);
    this.aesEngine = AesCipherUtil.getAESEngine(arrayOfByte1, paramAesKeyStrength);
    this.mac = AesCipherUtil.getMacBasedPRF(arrayOfByte1, paramAesKeyStrength);
  }
  
  public int encryptData(byte[] paramArrayOfbyte) throws ZipException {
    if (paramArrayOfbyte != null)
      return encryptData(paramArrayOfbyte, 0, paramArrayOfbyte.length); 
    throw new ZipException("input bytes are null, cannot perform AES encryption");
  }
  
  public int encryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException {
    if (!this.finished) {
      if (paramInt2 % 16 != 0)
        this.finished = true; 
      int i = paramInt1;
      while (true) {
        int j = paramInt1 + paramInt2;
        if (i < j) {
          int k = i + 16;
          if (k <= j) {
            j = 16;
          } else {
            j -= i;
          } 
          this.loopCount = j;
          AesCipherUtil.prepareBuffAESIVBytes(this.iv, this.nonce);
          this.aesEngine.processBlock(this.iv, this.counterBlock);
          j = 0;
          while (true) {
            int m = this.loopCount;
            if (j < m) {
              m = i + j;
              paramArrayOfbyte[m] = (byte)(paramArrayOfbyte[m] ^ this.counterBlock[j]);
              j++;
              continue;
            } 
            this.mac.update(paramArrayOfbyte, i, m);
            this.nonce++;
            i = k;
          } 
          break;
        } 
        return paramInt2;
      } 
    } 
    throw new ZipException("AES Encrypter is in finished state (A non 16 byte block has already been passed to encrypter)");
  }
  
  public byte[] getDerivedPasswordVerifier() {
    return this.derivedPasswordVerifier;
  }
  
  public byte[] getFinalMac() {
    byte[] arrayOfByte2 = this.mac.doFinal();
    byte[] arrayOfByte1 = new byte[10];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 10);
    return arrayOfByte1;
  }
  
  public byte[] getSaltBytes() {
    return this.saltBytes;
  }
}
