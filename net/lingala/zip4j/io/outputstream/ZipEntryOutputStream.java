package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;

public class ZipEntryOutputStream extends OutputStream {
  private boolean entryClosed;
  
  private long numberOfBytesWrittenForThisEntry = 0L;
  
  private OutputStream outputStream;
  
  public ZipEntryOutputStream(OutputStream paramOutputStream) {
    this.outputStream = paramOutputStream;
    this.entryClosed = false;
  }
  
  public void close() throws IOException {}
  
  public void closeEntry() throws IOException {
    this.entryClosed = true;
  }
  
  public long getNumberOfBytesWrittenForThisEntry() {
    return this.numberOfBytesWrittenForThisEntry;
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (!this.entryClosed) {
      this.outputStream.write(paramArrayOfbyte, paramInt1, paramInt2);
      this.numberOfBytesWrittenForThisEntry += paramInt2;
      return;
    } 
    throw new IllegalStateException("ZipEntryOutputStream is closed");
  }
}
