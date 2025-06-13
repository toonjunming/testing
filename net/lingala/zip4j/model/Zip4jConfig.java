package net.lingala.zip4j.model;

import java.nio.charset.Charset;

public class Zip4jConfig {
  private final int bufferSize;
  
  private final Charset charset;
  
  public Zip4jConfig(Charset paramCharset, int paramInt) {
    this.charset = paramCharset;
    this.bufferSize = paramInt;
  }
  
  public int getBufferSize() {
    return this.bufferSize;
  }
  
  public Charset getCharset() {
    return this.charset;
  }
}
