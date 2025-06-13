package net.lingala.zip4j.io.inputstream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class InflaterInputStream extends DecompressedInputStream {
  private byte[] buff;
  
  private Inflater inflater = new Inflater(true);
  
  private int len;
  
  private byte[] singleByteBuffer = new byte[1];
  
  public InflaterInputStream(CipherInputStream paramCipherInputStream, int paramInt) {
    super(paramCipherInputStream);
    this.buff = new byte[paramInt];
  }
  
  private void fill() throws IOException {
    byte[] arrayOfByte = this.buff;
    int i = super.read(arrayOfByte, 0, arrayOfByte.length);
    this.len = i;
    if (i != -1) {
      this.inflater.setInput(this.buff, 0, i);
      return;
    } 
    throw new EOFException("Unexpected end of input stream");
  }
  
  public void close() throws IOException {
    Inflater inflater = this.inflater;
    if (inflater != null)
      inflater.end(); 
    super.close();
  }
  
  public void endOfEntryReached(InputStream paramInputStream) throws IOException {
    Inflater inflater = this.inflater;
    if (inflater != null) {
      inflater.end();
      this.inflater = null;
    } 
    super.endOfEntryReached(paramInputStream);
  }
  
  public void pushBackInputStreamIfNecessary(PushbackInputStream paramPushbackInputStream) throws IOException {
    int i = this.inflater.getRemaining();
    if (i > 0)
      paramPushbackInputStream.unread(getLastReadRawDataCache(), this.len - i, i); 
  }
  
  public int read() throws IOException {
    return (read(this.singleByteBuffer) == -1) ? -1 : this.singleByteBuffer[0];
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    try {
      while (true) {
        int i = this.inflater.inflate(paramArrayOfbyte, paramInt1, paramInt2);
        if (i == 0) {
          if (!this.inflater.finished()) {
            if (this.inflater.needsDictionary())
              return -1; 
            if (this.inflater.needsInput())
              fill(); 
            continue;
          } 
          continue;
        } 
        return i;
      } 
    } catch (DataFormatException dataFormatException) {
      throw new IOException(dataFormatException);
    } 
  }
}
