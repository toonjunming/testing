package net.lingala.zip4j.crypto.PBKDF2;

public class BinTools {
  public static final String hex = "0123456789ABCDEF";
  
  public static String bin2hex(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return ""; 
    StringBuffer stringBuffer = new StringBuffer(paramArrayOfbyte.length * 2);
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = (paramArrayOfbyte[b] + 256) % 256;
      stringBuffer.append("0123456789ABCDEF".charAt(i / 16 & 0xF));
      stringBuffer.append("0123456789ABCDEF".charAt(i % 16 & 0xF));
    } 
    return stringBuffer.toString();
  }
  
  public static int hex2bin(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9')
      return paramChar - 48; 
    byte b = 65;
    if (paramChar < 'A' || paramChar > 'F') {
      b = 97;
      if (paramChar < 'a' || paramChar > 'f') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Input string may only contain hex digits, but found '");
        stringBuilder.append(paramChar);
        stringBuilder.append("'");
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    return paramChar - b + 10;
  }
  
  public static byte[] hex2bin(String paramString) {
    String str;
    if (paramString == null) {
      str = "";
    } else {
      str = paramString;
      if (paramString.length() % 2 != 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0");
        stringBuilder.append(paramString);
        str = stringBuilder.toString();
      } 
    } 
    byte[] arrayOfByte = new byte[str.length() / 2];
    int i = 0;
    byte b = 0;
    while (i < str.length()) {
      int j = i + 1;
      char c2 = str.charAt(i);
      char c1 = str.charAt(j);
      arrayOfByte[b] = (byte)(hex2bin(c2) * 16 + hex2bin(c1));
      b++;
      i = j + 1;
    } 
    return arrayOfByte;
  }
}
