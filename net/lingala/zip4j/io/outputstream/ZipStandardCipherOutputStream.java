package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.crypto.Encrypter;
import net.lingala.zip4j.crypto.StandardEncrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipStandardCipherOutputStream extends CipherOutputStream<StandardEncrypter> {
  public ZipStandardCipherOutputStream(ZipEntryOutputStream paramZipEntryOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException {
    super(paramZipEntryOutputStream, paramZipParameters, paramArrayOfchar);
  }
  
  private long getEncryptionKey(ZipParameters paramZipParameters) {
    return paramZipParameters.isWriteExtendedLocalFileHeader() ? ((Zip4jUtil.epochToExtendedDosTime(paramZipParameters.getLastModifiedFileTime()) & 0xFFFFL) << 16L) : paramZipParameters.getEntryCRC();
  }
  
  public StandardEncrypter initializeEncrypter(OutputStream paramOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException {
    StandardEncrypter standardEncrypter = new StandardEncrypter(paramArrayOfchar, getEncryptionKey(paramZipParameters));
    writeHeaders(standardEncrypter.getHeaderBytes());
    return standardEncrypter;
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    super.write(paramArrayOfbyte, paramInt1, paramInt2);
  }
}
