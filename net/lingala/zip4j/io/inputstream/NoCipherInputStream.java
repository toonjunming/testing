package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import net.lingala.zip4j.crypto.Decrypter;
import net.lingala.zip4j.model.LocalFileHeader;

public class NoCipherInputStream extends CipherInputStream {
  public NoCipherInputStream(ZipEntryInputStream paramZipEntryInputStream, LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar, int paramInt) throws IOException {
    super(paramZipEntryInputStream, paramLocalFileHeader, paramArrayOfchar, paramInt);
  }
  
  public Decrypter initializeDecrypter(LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar) {
    return new NoDecrypter();
  }
  
  public static class NoDecrypter implements Decrypter {
    public int decryptData(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      return param1Int2;
    }
  }
}
