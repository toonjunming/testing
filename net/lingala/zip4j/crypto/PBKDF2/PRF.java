package net.lingala.zip4j.crypto.PBKDF2;

public interface PRF {
  byte[] doFinal(byte[] paramArrayOfbyte);
  
  int getHLen();
  
  void init(byte[] paramArrayOfbyte);
}
