package net.lingala.zip4j.model;

public class Zip64ExtendedInfo extends ZipHeader {
  private long compressedSize = -1L;
  
  private int diskNumberStart = -1;
  
  private long offsetLocalHeader = -1L;
  
  private int size;
  
  private long uncompressedSize = -1L;
  
  public long getCompressedSize() {
    return this.compressedSize;
  }
  
  public int getDiskNumberStart() {
    return this.diskNumberStart;
  }
  
  public long getOffsetLocalHeader() {
    return this.offsetLocalHeader;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public long getUncompressedSize() {
    return this.uncompressedSize;
  }
  
  public void setCompressedSize(long paramLong) {
    this.compressedSize = paramLong;
  }
  
  public void setDiskNumberStart(int paramInt) {
    this.diskNumberStart = paramInt;
  }
  
  public void setOffsetLocalHeader(long paramLong) {
    this.offsetLocalHeader = paramLong;
  }
  
  public void setSize(int paramInt) {
    this.size = paramInt;
  }
  
  public void setUncompressedSize(long paramLong) {
    this.uncompressedSize = paramLong;
  }
}
