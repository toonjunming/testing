package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;

public class ZipEntryInputStream extends InputStream {
  private static final int MAX_RAW_READ_FULLY_RETRY_ATTEMPTS = 15;
  
  private long compressedSize;
  
  private InputStream inputStream;
  
  private long numberOfBytesRead = 0L;
  
  private byte[] singleByteArray = new byte[1];
  
  public ZipEntryInputStream(InputStream paramInputStream, long paramLong) {
    this.inputStream = paramInputStream;
    this.compressedSize = paramLong;
  }
  
  private int readUntilBufferIsFull(byte[] paramArrayOfbyte, int paramInt) throws IOException {
    int k = paramArrayOfbyte.length - paramInt;
    int m = 0;
    int j = 0;
    int i = paramInt;
    paramInt = j;
    while (i < paramArrayOfbyte.length && m != -1 && paramInt < 15) {
      int n = m + this.inputStream.read(paramArrayOfbyte, i, k);
      m = k;
      j = i;
      if (n > 0) {
        j = i + n;
        m = k - n;
      } 
      paramInt++;
      k = m;
      m = n;
      i = j;
    } 
    return i;
  }
  
  public void close() throws IOException {
    this.inputStream.close();
  }
  
  public long getNumberOfBytesRead() {
    return this.numberOfBytesRead;
  }
  
  public int read() throws IOException {
    return (read(this.singleByteArray) == -1) ? -1 : this.singleByteArray[0];
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    long l = this.compressedSize;
    int i = paramInt2;
    if (l != -1L) {
      long l1 = this.numberOfBytesRead;
      if (l1 >= l)
        return -1; 
      i = paramInt2;
      if (paramInt2 > l - l1)
        i = (int)(l - l1); 
    } 
    paramInt1 = this.inputStream.read(paramArrayOfbyte, paramInt1, i);
    if (paramInt1 > 0)
      this.numberOfBytesRead += paramInt1; 
    return paramInt1;
  }
  
  public int readRawFully(byte[] paramArrayOfbyte) throws IOException {
    int j = this.inputStream.read(paramArrayOfbyte);
    int i = j;
    if (j != paramArrayOfbyte.length) {
      i = readUntilBufferIsFull(paramArrayOfbyte, j);
      if (i != paramArrayOfbyte.length)
        throw new IOException("Cannot read fully into byte buffer"); 
    } 
    return i;
  }
}
