package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.crypto.Encrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

public class NoCipherOutputStream extends CipherOutputStream<NoCipherOutputStream.NoEncrypter> {
  public NoCipherOutputStream(ZipEntryOutputStream paramZipEntryOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException, ZipException {
    super(paramZipEntryOutputStream, paramZipParameters, paramArrayOfchar);
  }
  
  public NoEncrypter initializeEncrypter(OutputStream paramOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) {
    return new NoEncrypter();
  }
  
  public static class NoEncrypter implements Encrypter {
    public int encryptData(byte[] param1ArrayOfbyte) {
      return encryptData(param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
    }
    
    public int encryptData(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      return param1Int2;
    }
  }
}
