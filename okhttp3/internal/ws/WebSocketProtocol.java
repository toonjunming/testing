package okhttp3.internal.ws;

import I丨L.I1I;
import I丨L.iI丨LLL1;

public final class WebSocketProtocol {
  public static final String ACCEPT_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  
  public static final int B0_FLAG_FIN = 128;
  
  public static final int B0_FLAG_RSV1 = 64;
  
  public static final int B0_FLAG_RSV2 = 32;
  
  public static final int B0_FLAG_RSV3 = 16;
  
  public static final int B0_MASK_OPCODE = 15;
  
  public static final int B1_FLAG_MASK = 128;
  
  public static final int B1_MASK_LENGTH = 127;
  
  public static final int CLOSE_CLIENT_GOING_AWAY = 1001;
  
  public static final long CLOSE_MESSAGE_MAX = 123L;
  
  public static final int CLOSE_NO_STATUS_CODE = 1005;
  
  public static final int OPCODE_BINARY = 2;
  
  public static final int OPCODE_CONTINUATION = 0;
  
  public static final int OPCODE_CONTROL_CLOSE = 8;
  
  public static final int OPCODE_CONTROL_PING = 9;
  
  public static final int OPCODE_CONTROL_PONG = 10;
  
  public static final int OPCODE_FLAG_CONTROL = 8;
  
  public static final int OPCODE_TEXT = 1;
  
  public static final long PAYLOAD_BYTE_MAX = 125L;
  
  public static final int PAYLOAD_LONG = 127;
  
  public static final int PAYLOAD_SHORT = 126;
  
  public static final long PAYLOAD_SHORT_MAX = 65535L;
  
  private WebSocketProtocol() {
    throw new AssertionError("No instances.");
  }
  
  public static String acceptHeader(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
    return iI丨LLL1.encodeUtf8(stringBuilder.toString()).sha1().base64();
  }
  
  public static String closeCodeExceptionMessage(int paramInt) {
    if (paramInt < 1000 || paramInt >= 5000) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Code must be in range [1000,5000): ");
      stringBuilder.append(paramInt);
      return stringBuilder.toString();
    } 
    if ((paramInt >= 1004 && paramInt <= 1006) || (paramInt >= 1012 && paramInt <= 2999)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Code ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" is reserved and may not be used.");
      return stringBuilder.toString();
    } 
    return null;
  }
  
  public static void toggleMask(I1I.I1I paramI1I, byte[] paramArrayOfbyte) {
    int j = paramArrayOfbyte.length;
    int i = 0;
    do {
      byte[] arrayOfByte = paramI1I.Ilil;
      int k = paramI1I.l丨Li1LL;
      int m = paramI1I.iI丨LLL1;
      while (k < m) {
        i %= j;
        arrayOfByte[k] = (byte)(arrayOfByte[k] ^ paramArrayOfbyte[i]);
        k++;
        i++;
      } 
    } while (paramI1I.丨丨LLlI1() != -1);
  }
  
  public static void validateCloseCode(int paramInt) {
    String str = closeCodeExceptionMessage(paramInt);
    if (str == null)
      return; 
    throw new IllegalArgumentException(str);
  }
}
