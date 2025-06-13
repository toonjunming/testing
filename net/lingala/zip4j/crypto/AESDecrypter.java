package net.lingala.zip4j.crypto;

import java.util.Arrays;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.enums.AesKeyStrength;

public class AESDecrypter implements Decrypter {
  private AESEngine aesEngine;
  
  private byte[] counterBlock = new byte[16];
  
  private byte[] iv = new byte[16];
  
  private MacBasedPRF mac;
  
  private int nonce = 1;
  
  public AESDecrypter(AESExtraDataRecord paramAESExtraDataRecord, char[] paramArrayOfchar, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws ZipException {
    init(paramArrayOfbyte1, paramArrayOfbyte2, paramArrayOfchar, paramAESExtraDataRecord);
  }
  
  private void init(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, char[] paramArrayOfchar, AESExtraDataRecord paramAESExtraDataRecord) throws ZipException {
    if (paramArrayOfchar != null && paramArrayOfchar.length > 0) {
      AesKeyStrength aesKeyStrength = paramAESExtraDataRecord.getAesKeyStrength();
      paramArrayOfbyte1 = AesCipherUtil.derivePasswordBasedKey(paramArrayOfbyte1, paramArrayOfchar, aesKeyStrength);
      if (Arrays.equals(paramArrayOfbyte2, AesCipherUtil.derivePasswordVerifier(paramArrayOfbyte1, aesKeyStrength))) {
        this.aesEngine = AesCipherUtil.getAESEngine(paramArrayOfbyte1, aesKeyStrength);
        this.mac = AesCipherUtil.getMacBasedPRF(paramArrayOfbyte1, aesKeyStrength);
        return;
      } 
      throw new ZipException("Wrong Password", ZipException.Type.WRONG_PASSWORD);
    } 
    throw new ZipException("empty or null password provided for AES decryption");
  }
  
  public int decryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException {
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
        this.mac.update(paramArrayOfbyte, i, j);
        AesCipherUtil.prepareBuffAESIVBytes(this.iv, this.nonce);
        this.aesEngine.processBlock(this.iv, this.counterBlock);
        for (byte b = 0; b < j; b++) {
          int m = i + b;
          paramArrayOfbyte[m] = (byte)(paramArrayOfbyte[m] ^ this.counterBlock[b]);
        } 
        this.nonce++;
        i = k;
        continue;
      } 
      return paramInt2;
    } 
  }
  
  public byte[] getCalculatedAuthenticationBytes() {
    return this.mac.doFinal();
  }
}
