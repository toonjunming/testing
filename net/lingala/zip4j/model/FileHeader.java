package net.lingala.zip4j.model;

import net.lingala.zip4j.headers.HeaderSignature;

public class FileHeader extends AbstractFileHeader {
  private int diskNumberStart;
  
  private byte[] externalFileAttributes;
  
  private String fileComment;
  
  private int fileCommentLength = 0;
  
  private byte[] internalFileAttributes;
  
  private long offsetLocalHeader;
  
  private int versionMadeBy;
  
  public FileHeader() {
    setSignature(HeaderSignature.CENTRAL_DIRECTORY);
  }
  
  public int getDiskNumberStart() {
    return this.diskNumberStart;
  }
  
  public byte[] getExternalFileAttributes() {
    return this.externalFileAttributes;
  }
  
  public String getFileComment() {
    return this.fileComment;
  }
  
  public int getFileCommentLength() {
    return this.fileCommentLength;
  }
  
  public byte[] getInternalFileAttributes() {
    return this.internalFileAttributes;
  }
  
  public long getOffsetLocalHeader() {
    return this.offsetLocalHeader;
  }
  
  public int getVersionMadeBy() {
    return this.versionMadeBy;
  }
  
  public void setDiskNumberStart(int paramInt) {
    this.diskNumberStart = paramInt;
  }
  
  public void setExternalFileAttributes(byte[] paramArrayOfbyte) {
    this.externalFileAttributes = paramArrayOfbyte;
  }
  
  public void setFileComment(String paramString) {
    this.fileComment = paramString;
  }
  
  public void setFileCommentLength(int paramInt) {
    this.fileCommentLength = paramInt;
  }
  
  public void setInternalFileAttributes(byte[] paramArrayOfbyte) {
    this.internalFileAttributes = paramArrayOfbyte;
  }
  
  public void setOffsetLocalHeader(long paramLong) {
    this.offsetLocalHeader = paramLong;
  }
  
  public void setVersionMadeBy(int paramInt) {
    this.versionMadeBy = paramInt;
  }
  
  public String toString() {
    return getFileName();
  }
}
