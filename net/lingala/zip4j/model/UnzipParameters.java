package net.lingala.zip4j.model;

public class UnzipParameters {
  private boolean extractSymbolicLinks = true;
  
  public boolean isExtractSymbolicLinks() {
    return this.extractSymbolicLinks;
  }
  
  public void setExtractSymbolicLinks(boolean paramBoolean) {
    this.extractSymbolicLinks = paramBoolean;
  }
}
