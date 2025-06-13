package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public abstract class DecompressedInputStream extends InputStream {
  private CipherInputStream cipherInputStream;
  
  public byte[] oneByteBuffer = new byte[1];
  
  public DecompressedInputStream(CipherInputStream paramCipherInputStream) {
    this.cipherInputStream = paramCipherInputStream;
  }
  
  public void close() throws IOException {
    this.cipherInputStream.close();
  }
  
  public void endOfEntryReached(InputStream paramInputStream) throws IOException {
    this.cipherInputStream.endOfEntryReached(paramInputStream);
  }
  
  public byte[] getLastReadRawDataCache() {
    return this.cipherInputStream.getLastReadRawDataCache();
  }
  
  public void pushBackInputStreamIfNecessary(PushbackInputStream paramPushbackInputStream) throws IOException {}
  
  public int read() throws IOException {
    return (read(this.oneByteBuffer) == -1) ? -1 : this.oneByteBuffer[0];
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    return this.cipherInputStream.read(paramArrayOfbyte, paramInt1, paramInt2);
  }
}
