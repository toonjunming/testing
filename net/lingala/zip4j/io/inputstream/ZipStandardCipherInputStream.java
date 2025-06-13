package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import net.lingala.zip4j.crypto.Decrypter;
import net.lingala.zip4j.crypto.StandardDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.LocalFileHeader;

public class ZipStandardCipherInputStream extends CipherInputStream<StandardDecrypter> {
  public ZipStandardCipherInputStream(ZipEntryInputStream paramZipEntryInputStream, LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar, int paramInt) throws IOException {
    super(paramZipEntryInputStream, paramLocalFileHeader, paramArrayOfchar, paramInt);
  }
  
  private byte[] getStandardDecrypterHeaderBytes() throws IOException {
    byte[] arrayOfByte = new byte[12];
    readRaw(arrayOfByte);
    return arrayOfByte;
  }
  
  public StandardDecrypter initializeDecrypter(LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar) throws IOException {
    return new StandardDecrypter(paramArrayOfchar, paramLocalFileHeader.getCrc(), paramLocalFileHeader.getLastModifiedTime(), getStandardDecrypterHeaderBytes());
  }
}
