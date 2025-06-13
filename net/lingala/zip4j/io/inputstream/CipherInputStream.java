package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.crypto.Decrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

public abstract class CipherInputStream<T extends Decrypter> extends InputStream {
  private T decrypter;
  
  private byte[] lastReadRawDataCache;
  
  private LocalFileHeader localFileHeader;
  
  private byte[] singleByteBuffer = new byte[1];
  
  private ZipEntryInputStream zipEntryInputStream;
  
  public CipherInputStream(ZipEntryInputStream paramZipEntryInputStream, LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar, int paramInt) throws IOException {
    this.zipEntryInputStream = paramZipEntryInputStream;
    this.decrypter = initializeDecrypter(paramLocalFileHeader, paramArrayOfchar);
    this.localFileHeader = paramLocalFileHeader;
    if (Zip4jUtil.getCompressionMethod(paramLocalFileHeader).equals(CompressionMethod.DEFLATE))
      this.lastReadRawDataCache = new byte[paramInt]; 
  }
  
  private void cacheRawData(byte[] paramArrayOfbyte, int paramInt) {
    byte[] arrayOfByte = this.lastReadRawDataCache;
    if (arrayOfByte != null)
      System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, paramInt); 
  }
  
  public void close() throws IOException {
    this.zipEntryInputStream.close();
  }
  
  public void endOfEntryReached(InputStream paramInputStream) throws IOException {}
  
  public T getDecrypter() {
    return this.decrypter;
  }
  
  public byte[] getLastReadRawDataCache() {
    return this.lastReadRawDataCache;
  }
  
  public LocalFileHeader getLocalFileHeader() {
    return this.localFileHeader;
  }
  
  public long getNumberOfBytesReadForThisEntry() {
    return this.zipEntryInputStream.getNumberOfBytesRead();
  }
  
  public abstract T initializeDecrypter(LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar) throws IOException, ZipException;
  
  public int read() throws IOException {
    return (read(this.singleByteBuffer) == -1) ? -1 : (this.singleByteBuffer[0] & 0xFF);
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    paramInt2 = Zip4jUtil.readFully(this.zipEntryInputStream, paramArrayOfbyte, paramInt1, paramInt2);
    if (paramInt2 > 0) {
      cacheRawData(paramArrayOfbyte, paramInt2);
      this.decrypter.decryptData(paramArrayOfbyte, paramInt1, paramInt2);
    } 
    return paramInt2;
  }
  
  public int readRaw(byte[] paramArrayOfbyte) throws IOException {
    return this.zipEntryInputStream.readRawFully(paramArrayOfbyte);
  }
}
