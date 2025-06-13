package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.exception.ZipException;

public class CountingOutputStream extends OutputStream implements OutputStreamWithSplitZipSupport {
  private long numberOfBytesWritten = 0L;
  
  private OutputStream outputStream;
  
  public CountingOutputStream(OutputStream paramOutputStream) {
    this.outputStream = paramOutputStream;
  }
  
  public boolean checkBuffSizeAndStartNextSplitFile(int paramInt) throws ZipException {
    return !isSplitZipFile() ? false : ((SplitOutputStream)this.outputStream).checkBufferSizeAndStartNextSplitFile(paramInt);
  }
  
  public void close() throws IOException {
    this.outputStream.close();
  }
  
  public int getCurrentSplitFileCounter() {
    return isSplitZipFile() ? ((SplitOutputStream)this.outputStream).getCurrentSplitFileCounter() : 0;
  }
  
  public long getFilePointer() throws IOException {
    OutputStream outputStream = this.outputStream;
    return (outputStream instanceof SplitOutputStream) ? ((SplitOutputStream)outputStream).getFilePointer() : this.numberOfBytesWritten;
  }
  
  public long getNumberOfBytesWritten() throws IOException {
    OutputStream outputStream = this.outputStream;
    return (outputStream instanceof SplitOutputStream) ? ((SplitOutputStream)outputStream).getFilePointer() : this.numberOfBytesWritten;
  }
  
  public long getOffsetForNextEntry() throws IOException {
    OutputStream outputStream = this.outputStream;
    return (outputStream instanceof SplitOutputStream) ? ((SplitOutputStream)outputStream).getFilePointer() : this.numberOfBytesWritten;
  }
  
  public long getSplitLength() {
    return isSplitZipFile() ? ((SplitOutputStream)this.outputStream).getSplitLength() : 0L;
  }
  
  public boolean isSplitZipFile() {
    boolean bool;
    OutputStream outputStream = this.outputStream;
    if (outputStream instanceof SplitOutputStream && ((SplitOutputStream)outputStream).isSplitZipFile()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.outputStream.write(paramArrayOfbyte, paramInt1, paramInt2);
    this.numberOfBytesWritten += paramInt2;
  }
}
