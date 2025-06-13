package net.lingala.zip4j.model;

public class DigitalSignature extends ZipHeader {
  private String signatureData;
  
  private int sizeOfData;
  
  public String getSignatureData() {
    return this.signatureData;
  }
  
  public int getSizeOfData() {
    return this.sizeOfData;
  }
  
  public void setSignatureData(String paramString) {
    this.signatureData = paramString;
  }
  
  public void setSizeOfData(int paramInt) {
    this.sizeOfData = paramInt;
  }
}
