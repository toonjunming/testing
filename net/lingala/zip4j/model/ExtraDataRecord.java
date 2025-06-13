package net.lingala.zip4j.model;

public class ExtraDataRecord extends ZipHeader {
  private byte[] data;
  
  private long header;
  
  private int sizeOfData;
  
  public byte[] getData() {
    return this.data;
  }
  
  public long getHeader() {
    return this.header;
  }
  
  public int getSizeOfData() {
    return this.sizeOfData;
  }
  
  public void setData(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
  }
  
  public void setHeader(long paramLong) {
    this.header = paramLong;
  }
  
  public void setSizeOfData(int paramInt) {
    this.sizeOfData = paramInt;
  }
}
