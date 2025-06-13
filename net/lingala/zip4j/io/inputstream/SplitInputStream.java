package net.lingala.zip4j.io.inputstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;

public abstract class SplitInputStream extends InputStream {
  private int currentSplitFileCounter = 0;
  
  private boolean isSplitZipArchive;
  
  public RandomAccessFile randomAccessFile;
  
  private byte[] singleByteArray = new byte[1];
  
  public File zipFile;
  
  public SplitInputStream(File paramFile, boolean paramBoolean, int paramInt) throws FileNotFoundException {
    this.randomAccessFile = new RandomAccessFile(paramFile, RandomAccessFileMode.READ.getValue());
    this.zipFile = paramFile;
    this.isSplitZipArchive = paramBoolean;
    if (paramBoolean)
      this.currentSplitFileCounter = paramInt; 
  }
  
  public void close() throws IOException {
    RandomAccessFile randomAccessFile = this.randomAccessFile;
    if (randomAccessFile != null)
      randomAccessFile.close(); 
  }
  
  public abstract File getNextSplitFile(int paramInt) throws IOException;
  
  public void openRandomAccessFileForIndex(int paramInt) throws IOException {
    File file = getNextSplitFile(paramInt);
    if (file.exists()) {
      this.randomAccessFile.close();
      this.randomAccessFile = new RandomAccessFile(file, RandomAccessFileMode.READ.getValue());
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("zip split file does not exist: ");
    stringBuilder.append(file);
    throw new FileNotFoundException(stringBuilder.toString());
  }
  
  public void prepareExtractionForFileHeader(FileHeader paramFileHeader) throws IOException {
    if (this.isSplitZipArchive && this.currentSplitFileCounter != paramFileHeader.getDiskNumberStart()) {
      openRandomAccessFileForIndex(paramFileHeader.getDiskNumberStart());
      this.currentSplitFileCounter = paramFileHeader.getDiskNumberStart();
    } 
    this.randomAccessFile.seek(paramFileHeader.getOffsetLocalHeader());
  }
  
  public int read() throws IOException {
    return (read(this.singleByteArray) == -1) ? -1 : this.singleByteArray[0];
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield randomAccessFile : Ljava/io/RandomAccessFile;
    //   4: aload_1
    //   5: iload_2
    //   6: iload_3
    //   7: invokevirtual read : ([BII)I
    //   10: istore #5
    //   12: iload #5
    //   14: iload_3
    //   15: if_icmpne -> 27
    //   18: iload #5
    //   20: istore_2
    //   21: iload #5
    //   23: iconst_m1
    //   24: if_icmpne -> 96
    //   27: iload #5
    //   29: istore_2
    //   30: aload_0
    //   31: getfield isSplitZipArchive : Z
    //   34: ifeq -> 96
    //   37: aload_0
    //   38: aload_0
    //   39: getfield currentSplitFileCounter : I
    //   42: iconst_1
    //   43: iadd
    //   44: invokevirtual openRandomAccessFileForIndex : (I)V
    //   47: aload_0
    //   48: aload_0
    //   49: getfield currentSplitFileCounter : I
    //   52: iconst_1
    //   53: iadd
    //   54: putfield currentSplitFileCounter : I
    //   57: iload #5
    //   59: istore #4
    //   61: iload #5
    //   63: ifge -> 69
    //   66: iconst_0
    //   67: istore #4
    //   69: aload_0
    //   70: getfield randomAccessFile : Ljava/io/RandomAccessFile;
    //   73: aload_1
    //   74: iload #4
    //   76: iload_3
    //   77: iload #4
    //   79: isub
    //   80: invokevirtual read : ([BII)I
    //   83: istore_3
    //   84: iload #4
    //   86: istore_2
    //   87: iload_3
    //   88: ifle -> 96
    //   91: iload #4
    //   93: iload_3
    //   94: iadd
    //   95: istore_2
    //   96: iload_2
    //   97: ireturn
  }
}
