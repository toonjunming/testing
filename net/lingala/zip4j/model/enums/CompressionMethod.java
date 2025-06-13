package net.lingala.zip4j.model.enums;

import net.lingala.zip4j.exception.ZipException;

public enum CompressionMethod {
  AES_INTERNAL_ONLY, DEFLATE, STORE;
  
  private static final CompressionMethod[] $VALUES;
  
  private int code;
  
  static {
    CompressionMethod compressionMethod2 = new CompressionMethod("STORE", 0, 0);
    STORE = compressionMethod2;
    CompressionMethod compressionMethod1 = new CompressionMethod("DEFLATE", 1, 8);
    DEFLATE = compressionMethod1;
    CompressionMethod compressionMethod3 = new CompressionMethod("AES_INTERNAL_ONLY", 2, 99);
    AES_INTERNAL_ONLY = compressionMethod3;
    $VALUES = new CompressionMethod[] { compressionMethod2, compressionMethod1, compressionMethod3 };
  }
  
  CompressionMethod(int paramInt1) {
    this.code = paramInt1;
  }
  
  public static CompressionMethod getCompressionMethodFromCode(int paramInt) throws ZipException {
    for (CompressionMethod compressionMethod : values()) {
      if (compressionMethod.getCode() == paramInt)
        return compressionMethod; 
    } 
    throw new ZipException("Unknown compression method", ZipException.Type.UNKNOWN_COMPRESSION_METHOD);
  }
  
  public int getCode() {
    return this.code;
  }
}
