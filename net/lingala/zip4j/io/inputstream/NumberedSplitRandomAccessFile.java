package net.lingala.zip4j.io.inputstream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;
import net.lingala.zip4j.util.FileUtils;

public class NumberedSplitRandomAccessFile extends RandomAccessFile {
  private File[] allSortedSplitFiles;
  
  private int currentOpenSplitFileCounter = 0;
  
  private RandomAccessFile randomAccessFile;
  
  private String rwMode;
  
  private byte[] singleByteBuffer = new byte[1];
  
  private long splitLength;
  
  public NumberedSplitRandomAccessFile(File paramFile, String paramString) throws IOException {
    this(paramFile, paramString, FileUtils.getAllSortedNumberedSplitFiles(paramFile));
  }
  
  public NumberedSplitRandomAccessFile(File paramFile, String paramString, File[] paramArrayOfFile) throws IOException {
    super(paramFile, paramString);
    close();
    if (!RandomAccessFileMode.WRITE.getValue().equals(paramString)) {
      assertAllSplitFilesExist(paramArrayOfFile);
      this.randomAccessFile = new RandomAccessFile(paramFile, paramString);
      this.allSortedSplitFiles = paramArrayOfFile;
      this.splitLength = paramFile.length();
      this.rwMode = paramString;
      return;
    } 
    throw new IllegalArgumentException("write mode is not allowed for NumberedSplitRandomAccessFile");
  }
  
  public NumberedSplitRandomAccessFile(String paramString1, String paramString2) throws IOException {
    this(new File(paramString1), paramString2);
  }
  
  private void assertAllSplitFilesExist(File[] paramArrayOfFile) throws IOException {
    int i = paramArrayOfFile.length;
    byte b2 = 1;
    byte b1 = 0;
    while (b1 < i) {
      String str = FileUtils.getFileExtension(paramArrayOfFile[b1]);
      try {
        if (b2 == Integer.parseInt(str)) {
          b2++;
          b1++;
          continue;
        } 
        IOException iOException = new IOException();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Split file number ");
        stringBuilder.append(b2);
        stringBuilder.append(" does not exist");
        this(stringBuilder.toString());
        throw iOException;
      } catch (NumberFormatException numberFormatException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Split file extension not in expected format. Found: ");
        stringBuilder.append(str);
        stringBuilder.append(" expected of format: .001, .002, etc");
        throw new IOException(stringBuilder.toString());
      } 
    } 
  }
  
  private void openRandomAccessFileForIndex(int paramInt) throws IOException {
    if (this.currentOpenSplitFileCounter == paramInt)
      return; 
    if (paramInt <= this.allSortedSplitFiles.length - 1) {
      RandomAccessFile randomAccessFile = this.randomAccessFile;
      if (randomAccessFile != null)
        randomAccessFile.close(); 
      this.randomAccessFile = new RandomAccessFile(this.allSortedSplitFiles[paramInt], this.rwMode);
      this.currentOpenSplitFileCounter = paramInt;
      return;
    } 
    throw new IOException("split counter greater than number of split files");
  }
  
  public long getFilePointer() throws IOException {
    return this.randomAccessFile.getFilePointer();
  }
  
  public long length() throws IOException {
    return this.randomAccessFile.length();
  }
  
  public void openLastSplitFileForReading() throws IOException {
    openRandomAccessFileForIndex(this.allSortedSplitFiles.length - 1);
  }
  
  public int read() throws IOException {
    return (read(this.singleByteBuffer) == -1) ? -1 : (this.singleByteBuffer[0] & 0xFF);
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    int i = this.randomAccessFile.read(paramArrayOfbyte, paramInt1, paramInt2);
    if (i == -1) {
      i = this.currentOpenSplitFileCounter;
      if (i == this.allSortedSplitFiles.length - 1)
        return -1; 
      openRandomAccessFileForIndex(i + 1);
      return read(paramArrayOfbyte, paramInt1, paramInt2);
    } 
    return i;
  }
  
  public void seek(long paramLong) throws IOException {
    int i = (int)(paramLong / this.splitLength);
    if (i != this.currentOpenSplitFileCounter)
      openRandomAccessFileForIndex(i); 
    this.randomAccessFile.seek(paramLong - i * this.splitLength);
  }
  
  public void seekInCurrentPart(long paramLong) throws IOException {
    this.randomAccessFile.seek(paramLong);
  }
  
  public void write(int paramInt) throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    throw new UnsupportedOperationException();
  }
}
