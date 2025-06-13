package net.lingala.zip4j.model;

public class DataDescriptor extends ZipHeader {
  private long compressedSize;
  
  private long crc;
  
  private long uncompressedSize;
  
  public long getCompressedSize() {
    return this.compressedSize;
  }
  
  public long getCrc() {
    return this.crc;
  }
  
  public long getUncompressedSize() {
    return this.uncompressedSize;
  }
  
  public void setCompressedSize(long paramLong) {
    this.compressedSize = paramLong;
  }
  
  public void setCrc(long paramLong) {
    this.crc = paramLong;
  }
  
  public void setUncompressedSize(long paramLong) {
    this.uncompressedSize = paramLong;
  }
}
