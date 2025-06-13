package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;

public abstract class CompressedOutputStream extends OutputStream {
  private CipherOutputStream cipherOutputStream;
  
  public CompressedOutputStream(CipherOutputStream paramCipherOutputStream) {
    this.cipherOutputStream = paramCipherOutputStream;
  }
  
  public void close() throws IOException {
    this.cipherOutputStream.close();
  }
  
  public void closeEntry() throws IOException {
    this.cipherOutputStream.closeEntry();
  }
  
  public long getCompressedSize() {
    return this.cipherOutputStream.getNumberOfBytesWrittenForThisEntry();
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.cipherOutputStream.write(paramArrayOfbyte, paramInt1, paramInt2);
  }
}
