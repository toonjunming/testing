package I丨L;

import java.io.UnsupportedEncodingException;

public final class ILil {
  public static final byte[] IL1Iii = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 43, 47 };
  
  public static final byte[] ILil = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 45, 95 };
  
  public static String I1I(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    byte[] arrayOfByte = new byte[(paramArrayOfbyte1.length + 2) / 3 * 4];
    int k = paramArrayOfbyte1.length - paramArrayOfbyte1.length % 3;
    int i = 0;
    int j = 0;
    while (i < k) {
      int m = j + 1;
      arrayOfByte[j] = paramArrayOfbyte2[(paramArrayOfbyte1[i] & 0xFF) >> 2];
      j = m + 1;
      byte b = paramArrayOfbyte1[i];
      int n = i + 1;
      arrayOfByte[m] = paramArrayOfbyte2[(b & 0x3) << 4 | (paramArrayOfbyte1[n] & 0xFF) >> 4];
      m = j + 1;
      b = paramArrayOfbyte1[n];
      n = i + 2;
      arrayOfByte[j] = paramArrayOfbyte2[(b & 0xF) << 2 | (paramArrayOfbyte1[n] & 0xFF) >> 6];
      j = m + 1;
      arrayOfByte[m] = paramArrayOfbyte2[paramArrayOfbyte1[n] & 0x3F];
      i += 3;
    } 
    i = paramArrayOfbyte1.length % 3;
    if (i != 1) {
      if (i == 2) {
        i = j + 1;
        arrayOfByte[j] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0xFF) >> 2];
        int m = i + 1;
        j = paramArrayOfbyte1[k];
        arrayOfByte[i] = paramArrayOfbyte2[(paramArrayOfbyte1[++k] & 0xFF) >> 4 | (j & 0x3) << 4];
        arrayOfByte[m] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0xF) << 2];
        arrayOfByte[m + 1] = 61;
      } 
    } else {
      i = j + 1;
      arrayOfByte[j] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0xFF) >> 2];
      j = i + 1;
      arrayOfByte[i] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0x3) << 4];
      arrayOfByte[j] = 61;
      arrayOfByte[j + 1] = 61;
    } 
    try {
      return new String(arrayOfByte, "US-ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new AssertionError(unsupportedEncodingException);
    } 
  }
  
  public static byte[] IL1Iii(String paramString) {
    int i;
    int k;
    for (k = paramString.length(); k > 0; k--) {
      i = paramString.charAt(k - 1);
      if (i != 61 && i != 10 && i != 13 && i != 32 && i != 9)
        break; 
    } 
    int i1 = (int)(k * 6L / 8L);
    byte[] arrayOfByte2 = new byte[i1];
    int m = 0;
    byte b = 0;
    int n = 0;
    int j;
    for (j = 0;; j = i) {
      byte b1;
      int i2;
      if (m < k) {
        char c = paramString.charAt(m);
        if (c >= 'A' && c <= 'Z') {
          i = c - 65;
        } else if (c >= 'a' && c <= 'z') {
          i = c - 71;
        } else if (c >= '0' && c <= '9') {
          i = c + 4;
        } else if (c == '+' || c == '-') {
          i = 62;
        } else if (c == '/' || c == '_') {
          i = 63;
        } else {
          byte b2 = b;
          int i3 = n;
          i = j;
          if (c != '\n') {
            b2 = b;
            i3 = n;
            i = j;
            if (c != '\r') {
              b2 = b;
              i3 = n;
              i = j;
              if (c != ' ')
                if (c == '\t') {
                  b2 = b;
                  i3 = n;
                  i = j;
                } else {
                  return null;
                }  
            } 
          } 
          m++;
          b = b2;
          n = i3;
          j = i;
        } 
        n = n << 6 | (byte)i;
        b1 = ++b;
        i2 = n;
        i = j;
        if (b % 4 == 0) {
          i = j + 1;
          arrayOfByte2[j] = (byte)(n >> 16);
          j = i + 1;
          arrayOfByte2[i] = (byte)(n >> 8);
          arrayOfByte2[j] = (byte)n;
          i = j + 1;
          i2 = n;
          b1 = b;
        } 
      } else {
        break;
      } 
      m++;
      b = b1;
      n = i2;
    } 
    k = b % 4;
    if (k == 1)
      return null; 
    if (k == 2) {
      arrayOfByte2[j] = (byte)(n << 12 >> 16);
      i = j + 1;
    } else {
      i = j;
      if (k == 3) {
        m = n << 6;
        k = j + 1;
        arrayOfByte2[j] = (byte)(m >> 16);
        i = k + 1;
        arrayOfByte2[k] = (byte)(m >> 8);
      } 
    } 
    if (i == i1)
      return arrayOfByte2; 
    byte[] arrayOfByte1 = new byte[i];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, i);
    return arrayOfByte1;
  }
  
  public static String ILil(byte[] paramArrayOfbyte) {
    return I1I(paramArrayOfbyte, IL1Iii);
  }
  
  public static String I丨L(byte[] paramArrayOfbyte) {
    return I1I(paramArrayOfbyte, ILil);
  }
}
