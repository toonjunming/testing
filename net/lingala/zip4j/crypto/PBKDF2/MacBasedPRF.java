package net.lingala.zip4j.crypto.PBKDF2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacBasedPRF implements PRF {
  private int hLen;
  
  private Mac mac;
  
  private String macAlgorithm;
  
  public MacBasedPRF(String paramString) {
    this.macAlgorithm = paramString;
    try {
      Mac mac = Mac.getInstance(paramString);
      this.mac = mac;
      this.hLen = mac.getMacLength();
      return;
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new RuntimeException(noSuchAlgorithmException);
    } 
  }
  
  public byte[] doFinal() {
    return this.mac.doFinal();
  }
  
  public byte[] doFinal(byte[] paramArrayOfbyte) {
    return this.mac.doFinal(paramArrayOfbyte);
  }
  
  public int getHLen() {
    return this.hLen;
  }
  
  public void init(byte[] paramArrayOfbyte) {
    try {
      Mac mac = this.mac;
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramArrayOfbyte, this.macAlgorithm);
      mac.init(secretKeySpec);
      return;
    } catch (InvalidKeyException invalidKeyException) {
      throw new RuntimeException(invalidKeyException);
    } 
  }
  
  public void update(byte[] paramArrayOfbyte) {
    try {
      this.mac.update(paramArrayOfbyte);
      return;
    } catch (IllegalStateException illegalStateException) {
      throw new RuntimeException(illegalStateException);
    } 
  }
  
  public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      this.mac.update(paramArrayOfbyte, paramInt1, paramInt2);
      return;
    } catch (IllegalStateException illegalStateException) {
      throw new RuntimeException(illegalStateException);
    } 
  }
}
