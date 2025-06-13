package net.lingala.zip4j.model;

import net.lingala.zip4j.headers.HeaderSignature;

public class EndOfCentralDirectoryRecord extends ZipHeader {
  private String comment = "";
  
  private int numberOfThisDisk;
  
  private int numberOfThisDiskStartOfCentralDir;
  
  private long offsetOfEndOfCentralDirectory;
  
  private long offsetOfStartOfCentralDirectory;
  
  private int sizeOfCentralDirectory;
  
  private int totalNumberOfEntriesInCentralDirectory;
  
  private int totalNumberOfEntriesInCentralDirectoryOnThisDisk;
  
  public EndOfCentralDirectoryRecord() {
    setSignature(HeaderSignature.END_OF_CENTRAL_DIRECTORY);
  }
  
  public String getComment() {
    return this.comment;
  }
  
  public int getNumberOfThisDisk() {
    return this.numberOfThisDisk;
  }
  
  public int getNumberOfThisDiskStartOfCentralDir() {
    return this.numberOfThisDiskStartOfCentralDir;
  }
  
  public long getOffsetOfEndOfCentralDirectory() {
    return this.offsetOfEndOfCentralDirectory;
  }
  
  public long getOffsetOfStartOfCentralDirectory() {
    return this.offsetOfStartOfCentralDirectory;
  }
  
  public int getSizeOfCentralDirectory() {
    return this.sizeOfCentralDirectory;
  }
  
  public int getTotalNumberOfEntriesInCentralDirectory() {
    return this.totalNumberOfEntriesInCentralDirectory;
  }
  
  public int getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() {
    return this.totalNumberOfEntriesInCentralDirectoryOnThisDisk;
  }
  
  public void setComment(String paramString) {
    if (paramString != null)
      this.comment = paramString; 
  }
  
  public void setNumberOfThisDisk(int paramInt) {
    this.numberOfThisDisk = paramInt;
  }
  
  public void setNumberOfThisDiskStartOfCentralDir(int paramInt) {
    this.numberOfThisDiskStartOfCentralDir = paramInt;
  }
  
  public void setOffsetOfEndOfCentralDirectory(long paramLong) {
    this.offsetOfEndOfCentralDirectory = paramLong;
  }
  
  public void setOffsetOfStartOfCentralDirectory(long paramLong) {
    this.offsetOfStartOfCentralDirectory = paramLong;
  }
  
  public void setSizeOfCentralDirectory(int paramInt) {
    this.sizeOfCentralDirectory = paramInt;
  }
  
  public void setTotalNumberOfEntriesInCentralDirectory(int paramInt) {
    this.totalNumberOfEntriesInCentralDirectory = paramInt;
  }
  
  public void setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(int paramInt) {
    this.totalNumberOfEntriesInCentralDirectoryOnThisDisk = paramInt;
  }
}
