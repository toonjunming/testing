package net.lingala.zip4j.model;

public class ArchiveExtraDataRecord extends ZipHeader {
  private String extraFieldData;
  
  private int extraFieldLength;
  
  public String getExtraFieldData() {
    return this.extraFieldData;
  }
  
  public int getExtraFieldLength() {
    return this.extraFieldLength;
  }
  
  public void setExtraFieldData(String paramString) {
    this.extraFieldData = paramString;
  }
  
  public void setExtraFieldLength(int paramInt) {
    this.extraFieldLength = paramInt;
  }
}
