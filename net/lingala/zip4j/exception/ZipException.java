package net.lingala.zip4j.exception;

import java.io.IOException;

public class ZipException extends IOException {
  private static final long serialVersionUID = 1L;
  
  private Type type = Type.UNKNOWN;
  
  public ZipException(Exception paramException) {
    super(paramException);
  }
  
  public ZipException(String paramString) {
    super(paramString);
  }
  
  public ZipException(String paramString, Exception paramException) {
    super(paramString, paramException);
  }
  
  public ZipException(String paramString, Throwable paramThrowable, Type paramType) {
    super(paramString, paramThrowable);
    this.type = paramType;
  }
  
  public ZipException(String paramString, Type paramType) {
    super(paramString);
    this.type = paramType;
  }
  
  public Type getType() {
    return this.type;
  }
  
  public enum Type {
    CHECKSUM_MISMATCH, FILE_NOT_FOUND, TASK_CANCELLED_EXCEPTION, UNKNOWN, UNKNOWN_COMPRESSION_METHOD, UNSUPPORTED_ENCRYPTION, WRONG_PASSWORD;
    
    private static final Type[] $VALUES;
    
    static {
      Type type3 = new Type("WRONG_PASSWORD", 0);
      WRONG_PASSWORD = type3;
      Type type1 = new Type("TASK_CANCELLED_EXCEPTION", 1);
      TASK_CANCELLED_EXCEPTION = type1;
      Type type5 = new Type("CHECKSUM_MISMATCH", 2);
      CHECKSUM_MISMATCH = type5;
      Type type4 = new Type("UNKNOWN_COMPRESSION_METHOD", 3);
      UNKNOWN_COMPRESSION_METHOD = type4;
      Type type6 = new Type("FILE_NOT_FOUND", 4);
      FILE_NOT_FOUND = type6;
      Type type2 = new Type("UNSUPPORTED_ENCRYPTION", 5);
      UNSUPPORTED_ENCRYPTION = type2;
      Type type7 = new Type("UNKNOWN", 6);
      UNKNOWN = type7;
      $VALUES = new Type[] { type3, type1, type5, type4, type6, type2, type7 };
    }
  }
}
