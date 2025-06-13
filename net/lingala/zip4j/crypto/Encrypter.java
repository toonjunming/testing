package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.ZipException;

public interface Encrypter {
  int encryptData(byte[] paramArrayOfbyte) throws ZipException;
  
  int encryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException;
}
