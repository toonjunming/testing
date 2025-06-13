package net.lingala.zip4j.model;

import java.util.ArrayList;
import java.util.List;

public class CentralDirectory {
  private DigitalSignature digitalSignature = new DigitalSignature();
  
  private List<FileHeader> fileHeaders = new ArrayList<FileHeader>();
  
  public DigitalSignature getDigitalSignature() {
    return this.digitalSignature;
  }
  
  public List<FileHeader> getFileHeaders() {
    return this.fileHeaders;
  }
  
  public void setDigitalSignature(DigitalSignature paramDigitalSignature) {
    this.digitalSignature = paramDigitalSignature;
  }
  
  public void setFileHeaders(List<FileHeader> paramList) {
    this.fileHeaders = paramList;
  }
}
