package okhttp3.internal.http2;

public enum ErrorCode {
  CANCEL, COMPRESSION_ERROR, CONNECT_ERROR, ENHANCE_YOUR_CALM, FLOW_CONTROL_ERROR, HTTP_1_1_REQUIRED, INADEQUATE_SECURITY, INTERNAL_ERROR, NO_ERROR, PROTOCOL_ERROR, REFUSED_STREAM;
  
  private static final ErrorCode[] $VALUES;
  
  public final int httpCode;
  
  static {
    ErrorCode errorCode11 = new ErrorCode("NO_ERROR", 0, 0);
    NO_ERROR = errorCode11;
    ErrorCode errorCode4 = new ErrorCode("PROTOCOL_ERROR", 1, 1);
    PROTOCOL_ERROR = errorCode4;
    ErrorCode errorCode6 = new ErrorCode("INTERNAL_ERROR", 2, 2);
    INTERNAL_ERROR = errorCode6;
    ErrorCode errorCode9 = new ErrorCode("FLOW_CONTROL_ERROR", 3, 3);
    FLOW_CONTROL_ERROR = errorCode9;
    ErrorCode errorCode10 = new ErrorCode("REFUSED_STREAM", 4, 7);
    REFUSED_STREAM = errorCode10;
    ErrorCode errorCode3 = new ErrorCode("CANCEL", 5, 8);
    CANCEL = errorCode3;
    ErrorCode errorCode7 = new ErrorCode("COMPRESSION_ERROR", 6, 9);
    COMPRESSION_ERROR = errorCode7;
    ErrorCode errorCode5 = new ErrorCode("CONNECT_ERROR", 7, 10);
    CONNECT_ERROR = errorCode5;
    ErrorCode errorCode8 = new ErrorCode("ENHANCE_YOUR_CALM", 8, 11);
    ENHANCE_YOUR_CALM = errorCode8;
    ErrorCode errorCode1 = new ErrorCode("INADEQUATE_SECURITY", 9, 12);
    INADEQUATE_SECURITY = errorCode1;
    ErrorCode errorCode2 = new ErrorCode("HTTP_1_1_REQUIRED", 10, 13);
    HTTP_1_1_REQUIRED = errorCode2;
    $VALUES = new ErrorCode[] { 
        errorCode11, errorCode4, errorCode6, errorCode9, errorCode10, errorCode3, errorCode7, errorCode5, errorCode8, errorCode1, 
        errorCode2 };
  }
  
  ErrorCode(int paramInt1) {
    this.httpCode = paramInt1;
  }
  
  public static ErrorCode fromHttp2(int paramInt) {
    for (ErrorCode errorCode : values()) {
      if (errorCode.httpCode == paramInt)
        return errorCode; 
    } 
    return null;
  }
}
