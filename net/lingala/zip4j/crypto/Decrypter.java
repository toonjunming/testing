package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.ZipException;

public interface Decrypter {
  int decryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException;
}
