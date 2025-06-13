package net.lingala.zip4j.model;

import net.lingala.zip4j.headers.HeaderSignature;

public class LocalFileHeader extends AbstractFileHeader {
  private byte[] extraField;
  
  private long offsetStartOfData;
  
  private boolean writeCompressedSizeInZip64ExtraRecord;
  
  public LocalFileHeader() {
    setSignature(HeaderSignature.LOCAL_FILE_HEADER);
  }
  
  public byte[] getExtraField() {
    return this.extraField;
  }
  
  public long getOffsetStartOfData() {
    return this.offsetStartOfData;
  }
  
  public boolean isWriteCompressedSizeInZip64ExtraRecord() {
    return this.writeCompressedSizeInZip64ExtraRecord;
  }
  
  public void setExtraField(byte[] paramArrayOfbyte) {
    this.extraField = paramArrayOfbyte;
  }
  
  public void setOffsetStartOfData(long paramLong) {
    this.offsetStartOfData = paramLong;
  }
  
  public void setWriteCompressedSizeInZip64ExtraRecord(boolean paramBoolean) {
    this.writeCompressedSizeInZip64ExtraRecord = paramBoolean;
  }
}
