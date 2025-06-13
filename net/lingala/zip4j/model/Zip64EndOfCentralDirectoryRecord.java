package net.lingala.zip4j.model;

public class Zip64EndOfCentralDirectoryRecord extends ZipHeader {
  private byte[] extensibleDataSector;
  
  private int numberOfThisDisk;
  
  private int numberOfThisDiskStartOfCentralDirectory;
  
  private long offsetStartCentralDirectoryWRTStartDiskNumber = -1L;
  
  private long sizeOfCentralDirectory;
  
  private long sizeOfZip64EndCentralDirectoryRecord;
  
  private long totalNumberOfEntriesInCentralDirectory;
  
  private long totalNumberOfEntriesInCentralDirectoryOnThisDisk;
  
  private int versionMadeBy;
  
  private int versionNeededToExtract;
  
  public byte[] getExtensibleDataSector() {
    return this.extensibleDataSector;
  }
  
  public int getNumberOfThisDisk() {
    return this.numberOfThisDisk;
  }
  
  public int getNumberOfThisDiskStartOfCentralDirectory() {
    return this.numberOfThisDiskStartOfCentralDirectory;
  }
  
  public long getOffsetStartCentralDirectoryWRTStartDiskNumber() {
    return this.offsetStartCentralDirectoryWRTStartDiskNumber;
  }
  
  public long getSizeOfCentralDirectory() {
    return this.sizeOfCentralDirectory;
  }
  
  public long getSizeOfZip64EndCentralDirectoryRecord() {
    return this.sizeOfZip64EndCentralDirectoryRecord;
  }
  
  public long getTotalNumberOfEntriesInCentralDirectory() {
    return this.totalNumberOfEntriesInCentralDirectory;
  }
  
  public long getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() {
    return this.totalNumberOfEntriesInCentralDirectoryOnThisDisk;
  }
  
  public int getVersionMadeBy() {
    return this.versionMadeBy;
  }
  
  public int getVersionNeededToExtract() {
    return this.versionNeededToExtract;
  }
  
  public void setExtensibleDataSector(byte[] paramArrayOfbyte) {
    this.extensibleDataSector = paramArrayOfbyte;
  }
  
  public void setNumberOfThisDisk(int paramInt) {
    this.numberOfThisDisk = paramInt;
  }
  
  public void setNumberOfThisDiskStartOfCentralDirectory(int paramInt) {
    this.numberOfThisDiskStartOfCentralDirectory = paramInt;
  }
  
  public void setOffsetStartCentralDirectoryWRTStartDiskNumber(long paramLong) {
    this.offsetStartCentralDirectoryWRTStartDiskNumber = paramLong;
  }
  
  public void setSizeOfCentralDirectory(long paramLong) {
    this.sizeOfCentralDirectory = paramLong;
  }
  
  public void setSizeOfZip64EndCentralDirectoryRecord(long paramLong) {
    this.sizeOfZip64EndCentralDirectoryRecord = paramLong;
  }
  
  public void setTotalNumberOfEntriesInCentralDirectory(long paramLong) {
    this.totalNumberOfEntriesInCentralDirectory = paramLong;
  }
  
  public void setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(long paramLong) {
    this.totalNumberOfEntriesInCentralDirectoryOnThisDisk = paramLong;
  }
  
  public void setVersionMadeBy(int paramInt) {
    this.versionMadeBy = paramInt;
  }
  
  public void setVersionNeededToExtract(int paramInt) {
    this.versionNeededToExtract = paramInt;
  }
}
