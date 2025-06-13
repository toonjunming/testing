package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.crypto.Encrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

public abstract class CipherOutputStream<T extends Encrypter> extends OutputStream {
  private T encrypter;
  
  private ZipEntryOutputStream zipEntryOutputStream;
  
  public CipherOutputStream(ZipEntryOutputStream paramZipEntryOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException, ZipException {
    this.zipEntryOutputStream = paramZipEntryOutputStream;
    this.encrypter = initializeEncrypter(paramZipEntryOutputStream, paramZipParameters, paramArrayOfchar);
  }
  
  public void close() throws IOException {
    this.zipEntryOutputStream.close();
  }
  
  public void closeEntry() throws IOException {
    this.zipEntryOutputStream.closeEntry();
  }
  
  public T getEncrypter() {
    return this.encrypter;
  }
  
  public long getNumberOfBytesWrittenForThisEntry() {
    return this.zipEntryOutputStream.getNumberOfBytesWrittenForThisEntry();
  }
  
  public abstract T initializeEncrypter(OutputStream paramOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException, ZipException;
  
  public void write(int paramInt) throws IOException {
    this.zipEntryOutputStream.write(paramInt);
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    this.zipEntryOutputStream.write(paramArrayOfbyte);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.encrypter.encryptData(paramArrayOfbyte, paramInt1, paramInt2);
    this.zipEntryOutputStream.write(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public void writeHeaders(byte[] paramArrayOfbyte) throws IOException {
    this.zipEntryOutputStream.write(paramArrayOfbyte);
  }
}
