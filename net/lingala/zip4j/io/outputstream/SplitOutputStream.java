package net.lingala.zip4j.io.outputstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.util.RawIO;

public class SplitOutputStream extends OutputStream implements OutputStreamWithSplitZipSupport {
  private long bytesWrittenForThisPart;
  
  private int currSplitFileCounter;
  
  private RandomAccessFile raf;
  
  private RawIO rawIO = new RawIO();
  
  private long splitLength;
  
  private File zipFile;
  
  public SplitOutputStream(File paramFile) throws FileNotFoundException, ZipException {
    this(paramFile, -1L);
  }
  
  public SplitOutputStream(File paramFile, long paramLong) throws FileNotFoundException, ZipException {
    if (paramLong < 0L || paramLong >= 65536L) {
      this.raf = new RandomAccessFile(paramFile, RandomAccessFileMode.WRITE.getValue());
      this.splitLength = paramLong;
      this.zipFile = paramFile;
      this.currSplitFileCounter = 0;
      this.bytesWrittenForThisPart = 0L;
      return;
    } 
    throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
  }
  
  private boolean isBufferSizeFitForCurrSplitFile(int paramInt) {
    long l = this.splitLength;
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (l >= 65536L)
      if (this.bytesWrittenForThisPart + paramInt <= l) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  private boolean isHeaderData(byte[] paramArrayOfbyte) {
    int i = this.rawIO.readIntLittleEndian(paramArrayOfbyte);
    for (HeaderSignature headerSignature : HeaderSignature.values()) {
      if (headerSignature != HeaderSignature.SPLIT_ZIP && headerSignature.getValue() == i)
        return true; 
    } 
    return false;
  }
  
  private void startNextSplitFile() throws IOException {
    String str1;
    String str4 = FileUtils.getZipFileNameWithoutExtension(this.zipFile.getName());
    String str3 = this.zipFile.getAbsolutePath();
    if (this.zipFile.getParent() == null) {
      str1 = "";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.zipFile.getParent());
      stringBuilder.append(System.getProperty("file.separator"));
      str1 = stringBuilder.toString();
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(".z0");
    stringBuilder2.append(this.currSplitFileCounter + 1);
    String str2 = stringBuilder2.toString();
    if (this.currSplitFileCounter >= 9) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(".z");
      stringBuilder.append(this.currSplitFileCounter + 1);
      str2 = stringBuilder.toString();
    } 
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(str1);
    stringBuilder3.append(str4);
    stringBuilder3.append(str2);
    File file = new File(stringBuilder3.toString());
    this.raf.close();
    if (!file.exists()) {
      if (this.zipFile.renameTo(file)) {
        this.zipFile = new File(str3);
        this.raf = new RandomAccessFile(this.zipFile, RandomAccessFileMode.WRITE.getValue());
        this.currSplitFileCounter++;
        return;
      } 
      throw new IOException("cannot rename newly created split file");
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("split file: ");
    stringBuilder1.append(file.getName());
    stringBuilder1.append(" already exists in the current directory, cannot rename this file");
    throw new IOException(stringBuilder1.toString());
  }
  
  public boolean checkBufferSizeAndStartNextSplitFile(int paramInt) throws ZipException {
    if (paramInt >= 0) {
      if (!isBufferSizeFitForCurrSplitFile(paramInt))
        try {
          startNextSplitFile();
          this.bytesWrittenForThisPart = 0L;
          return true;
        } catch (IOException iOException) {
          throw new ZipException(iOException);
        }  
      return false;
    } 
    throw new ZipException("negative buffersize for checkBufferSizeAndStartNextSplitFile");
  }
  
  public void close() throws IOException {
    this.raf.close();
  }
  
  public int getCurrentSplitFileCounter() {
    return this.currSplitFileCounter;
  }
  
  public long getFilePointer() throws IOException {
    return this.raf.getFilePointer();
  }
  
  public long getSplitLength() {
    return this.splitLength;
  }
  
  public boolean isSplitZipFile() {
    boolean bool;
    if (this.splitLength != -1L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void seek(long paramLong) throws IOException {
    this.raf.seek(paramLong);
  }
  
  public int skipBytes(int paramInt) throws IOException {
    return this.raf.skipBytes(paramInt);
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 <= 0)
      return; 
    long l2 = this.splitLength;
    if (l2 == -1L) {
      this.raf.write(paramArrayOfbyte, paramInt1, paramInt2);
      this.bytesWrittenForThisPart += paramInt2;
      return;
    } 
    long l1 = this.bytesWrittenForThisPart;
    if (l1 >= l2) {
      startNextSplitFile();
      this.raf.write(paramArrayOfbyte, paramInt1, paramInt2);
      this.bytesWrittenForThisPart = paramInt2;
    } else {
      long l = paramInt2;
      if (l1 + l > l2) {
        if (isHeaderData(paramArrayOfbyte)) {
          startNextSplitFile();
          this.raf.write(paramArrayOfbyte, paramInt1, paramInt2);
          this.bytesWrittenForThisPart = l;
        } else {
          this.raf.write(paramArrayOfbyte, paramInt1, (int)(this.splitLength - this.bytesWrittenForThisPart));
          startNextSplitFile();
          RandomAccessFile randomAccessFile = this.raf;
          l2 = this.splitLength;
          l1 = this.bytesWrittenForThisPart;
          randomAccessFile.write(paramArrayOfbyte, paramInt1 + (int)(l2 - l1), (int)(l - l2 - l1));
          this.bytesWrittenForThisPart = l - this.splitLength - this.bytesWrittenForThisPart;
        } 
      } else {
        this.raf.write(paramArrayOfbyte, paramInt1, paramInt2);
        this.bytesWrittenForThisPart += l;
      } 
    } 
  }
}
