package net.lingala.zip4j.model;

public class Zip64EndOfCentralDirectoryLocator extends ZipHeader {
  private int numberOfDiskStartOfZip64EndOfCentralDirectoryRecord;
  
  private long offsetZip64EndOfCentralDirectoryRecord;
  
  private int totalNumberOfDiscs;
  
  public int getNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord() {
    return this.numberOfDiskStartOfZip64EndOfCentralDirectoryRecord;
  }
  
  public long getOffsetZip64EndOfCentralDirectoryRecord() {
    return this.offsetZip64EndOfCentralDirectoryRecord;
  }
  
  public int getTotalNumberOfDiscs() {
    return this.totalNumberOfDiscs;
  }
  
  public void setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(int paramInt) {
    this.numberOfDiskStartOfZip64EndOfCentralDirectoryRecord = paramInt;
  }
  
  public void setOffsetZip64EndOfCentralDirectoryRecord(long paramLong) {
    this.offsetZip64EndOfCentralDirectoryRecord = paramLong;
  }
  
  public void setTotalNumberOfDiscs(int paramInt) {
    this.totalNumberOfDiscs = paramInt;
  }
}
