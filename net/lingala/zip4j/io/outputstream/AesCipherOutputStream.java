package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.crypto.AESEncrypter;
import net.lingala.zip4j.crypto.Encrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

public class AesCipherOutputStream extends CipherOutputStream<AESEncrypter> {
  private byte[] pendingBuffer = new byte[16];
  
  private int pendingBufferLength = 0;
  
  public AesCipherOutputStream(ZipEntryOutputStream paramZipEntryOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException, ZipException {
    super(paramZipEntryOutputStream, paramZipParameters, paramArrayOfchar);
  }
  
  private void writeAesEncryptionHeaderData(AESEncrypter paramAESEncrypter) throws IOException {
    writeHeaders(paramAESEncrypter.getSaltBytes());
    writeHeaders(paramAESEncrypter.getDerivedPasswordVerifier());
  }
  
  public void closeEntry() throws IOException {
    int i = this.pendingBufferLength;
    if (i != 0) {
      super.write(this.pendingBuffer, 0, i);
      this.pendingBufferLength = 0;
    } 
    writeHeaders(((AESEncrypter)getEncrypter()).getFinalMac());
    super.closeEntry();
  }
  
  public AESEncrypter initializeEncrypter(OutputStream paramOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException, ZipException {
    AESEncrypter aESEncrypter = new AESEncrypter(paramArrayOfchar, paramZipParameters.getAesKeyStrength());
    writeAesEncryptionHeaderData(aESEncrypter);
    return aESEncrypter;
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    int i = this.pendingBufferLength;
    if (paramInt2 >= 16 - i) {
      System.arraycopy(paramArrayOfbyte, paramInt1, this.pendingBuffer, i, 16 - i);
      byte[] arrayOfByte = this.pendingBuffer;
      super.write(arrayOfByte, 0, arrayOfByte.length);
      i = 16 - this.pendingBufferLength;
      paramInt2 -= i;
      this.pendingBufferLength = 0;
      paramInt1 = paramInt2;
      if (paramInt2 != 0) {
        int j = paramInt2 % 16;
        paramInt1 = paramInt2;
        if (j != 0) {
          System.arraycopy(paramArrayOfbyte, paramInt2 + i - j, this.pendingBuffer, 0, j);
          this.pendingBufferLength = j;
          paramInt1 = paramInt2 - j;
        } 
      } 
      super.write(paramArrayOfbyte, i, paramInt1);
      return;
    } 
    System.arraycopy(paramArrayOfbyte, paramInt1, this.pendingBuffer, i, paramInt2);
    this.pendingBufferLength += paramInt2;
  }
}
