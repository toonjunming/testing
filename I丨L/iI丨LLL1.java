package I丨L;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class iI丨LLL1 implements Serializable, Comparable<iI丨LLL1> {
  public static final iI丨LLL1 EMPTY;
  
  public static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'a', 'b', 'c', 'd', 'e', 'f' };
  
  private static final long serialVersionUID = 1L;
  
  public final byte[] data;
  
  public transient int hashCode;
  
  public transient String utf8;
  
  static {
    EMPTY = of(new byte[0]);
  }
  
  public iI丨LLL1(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
  }
  
  public static int IL1Iii(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9')
      return paramChar - 48; 
    byte b = 97;
    if (paramChar < 'a' || paramChar > 'f') {
      b = 65;
      if (paramChar < 'A' || paramChar > 'F') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected hex digit: ");
        stringBuilder.append(paramChar);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    return paramChar - b + 10;
  }
  
  public static int codePointIndexToCharIndex(String paramString, int paramInt) {
    int k = paramString.length();
    int j = 0;
    int i = 0;
    while (j < k) {
      if (i == paramInt)
        return j; 
      int m = paramString.codePointAt(j);
      if ((Character.isISOControl(m) && m != 10 && m != 13) || m == 65533)
        return -1; 
      i++;
      j += Character.charCount(m);
    } 
    return paramString.length();
  }
  
  @Nullable
  public static iI丨LLL1 decodeBase64(String paramString) {
    if (paramString != null) {
      byte[] arrayOfByte = ILil.IL1Iii(paramString);
      if (arrayOfByte != null) {
        iI丨LLL1 iI丨LLL11 = new iI丨LLL1(arrayOfByte);
      } else {
        arrayOfByte = null;
      } 
      return (iI丨LLL1)arrayOfByte;
    } 
    throw new IllegalArgumentException("base64 == null");
  }
  
  public static iI丨LLL1 decodeHex(String paramString) {
    if (paramString != null) {
      if (paramString.length() % 2 == 0) {
        int i = paramString.length() / 2;
        byte[] arrayOfByte = new byte[i];
        for (byte b = 0; b < i; b++) {
          int j = b * 2;
          arrayOfByte[b] = (byte)((IL1Iii(paramString.charAt(j)) << 4) + IL1Iii(paramString.charAt(j + 1)));
        } 
        return of(arrayOfByte);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected hex string: ");
      stringBuilder.append(paramString);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("hex == null");
  }
  
  public static iI丨LLL1 encodeString(String paramString, Charset paramCharset) {
    if (paramString != null) {
      if (paramCharset != null)
        return new iI丨LLL1(paramString.getBytes(paramCharset)); 
      throw new IllegalArgumentException("charset == null");
    } 
    throw new IllegalArgumentException("s == null");
  }
  
  public static iI丨LLL1 encodeUtf8(String paramString) {
    if (paramString != null) {
      iI丨LLL1 iI丨LLL11 = new iI丨LLL1(paramString.getBytes(llliI.IL1Iii));
      iI丨LLL11.utf8 = paramString;
      return iI丨LLL11;
    } 
    throw new IllegalArgumentException("s == null");
  }
  
  public static iI丨LLL1 of(ByteBuffer paramByteBuffer) {
    if (paramByteBuffer != null) {
      byte[] arrayOfByte = new byte[paramByteBuffer.remaining()];
      paramByteBuffer.get(arrayOfByte);
      return new iI丨LLL1(arrayOfByte);
    } 
    throw new IllegalArgumentException("data == null");
  }
  
  public static iI丨LLL1 of(byte... paramVarArgs) {
    if (paramVarArgs != null)
      return new iI丨LLL1((byte[])paramVarArgs.clone()); 
    throw new IllegalArgumentException("data == null");
  }
  
  public static iI丨LLL1 of(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      llliI.ILil(paramArrayOfbyte.length, paramInt1, paramInt2);
      byte[] arrayOfByte = new byte[paramInt2];
      System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
      return new iI丨LLL1(arrayOfByte);
    } 
    throw new IllegalArgumentException("data == null");
  }
  
  public static iI丨LLL1 read(InputStream paramInputStream, int paramInt) throws IOException {
    if (paramInputStream != null) {
      if (paramInt >= 0) {
        byte[] arrayOfByte = new byte[paramInt];
        int i = 0;
        while (i < paramInt) {
          int j = paramInputStream.read(arrayOfByte, i, paramInt - i);
          if (j != -1) {
            i += j;
            continue;
          } 
          throw new EOFException();
        } 
        return new iI丨LLL1(arrayOfByte);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(paramInt);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("in == null");
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
    iI丨LLL1 iI丨LLL11 = read(paramObjectInputStream, paramObjectInputStream.readInt());
    try {
      Field field = iI丨LLL1.class.getDeclaredField("data");
      field.setAccessible(true);
      field.set(this, iI丨LLL11.data);
      return;
    } catch (NoSuchFieldException noSuchFieldException) {
      throw new AssertionError();
    } catch (IllegalAccessException illegalAccessException) {
      throw new AssertionError();
    } 
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.writeInt(this.data.length);
    paramObjectOutputStream.write(this.data);
  }
  
  public final iI丨LLL1 I1I(String paramString, iI丨LLL1 paramiI丨LLL1) {
    try {
      Mac mac = Mac.getInstance(paramString);
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramiI丨LLL1.toByteArray(), paramString);
      mac.init(secretKeySpec);
      return of(mac.doFinal(this.data));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } catch (InvalidKeyException invalidKeyException) {
      throw new IllegalArgumentException(invalidKeyException);
    } 
  }
  
  public final iI丨LLL1 ILil(String paramString) {
    try {
      return of(MessageDigest.getInstance(paramString).digest(this.data));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } 
  }
  
  public ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
  }
  
  public String base64() {
    return ILil.ILil(this.data);
  }
  
  public String base64Url() {
    return ILil.I丨L(this.data);
  }
  
  public int compareTo(iI丨LLL1 paramiI丨LLL1) {
    int j = size();
    int i = paramiI丨LLL1.size();
    int k = Math.min(j, i);
    byte b = 0;
    while (true) {
      byte b1 = -1;
      if (b < k) {
        int m = getByte(b) & 0xFF;
        int n = paramiI丨LLL1.getByte(b) & 0xFF;
        if (m == n) {
          b++;
          continue;
        } 
        if (m >= n)
          b1 = 1; 
        return b1;
      } 
      if (j == i)
        return 0; 
      if (j >= i)
        b1 = 1; 
      return b1;
    } 
  }
  
  public final boolean endsWith(iI丨LLL1 paramiI丨LLL1) {
    return rangeEquals(size() - paramiI丨LLL1.size(), paramiI丨LLL1, 0, paramiI丨LLL1.size());
  }
  
  public final boolean endsWith(byte[] paramArrayOfbyte) {
    return rangeEquals(size() - paramArrayOfbyte.length, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public boolean equals(Object paramObject) {
    null = true;
    if (paramObject == this)
      return true; 
    if (paramObject instanceof iI丨LLL1) {
      iI丨LLL1 iI丨LLL11 = (iI丨LLL1)paramObject;
      int i = iI丨LLL11.size();
      paramObject = this.data;
      if (i == paramObject.length && iI丨LLL11.rangeEquals(0, (byte[])paramObject, 0, paramObject.length))
        return null; 
    } 
    return false;
  }
  
  public byte getByte(int paramInt) {
    return this.data[paramInt];
  }
  
  public int hashCode() {
    int i = this.hashCode;
    if (i == 0) {
      i = Arrays.hashCode(this.data);
      this.hashCode = i;
    } 
    return i;
  }
  
  public String hex() {
    byte[] arrayOfByte = this.data;
    char[] arrayOfChar = new char[arrayOfByte.length * 2];
    int j = arrayOfByte.length;
    byte b = 0;
    int i = 0;
    while (b < j) {
      byte b1 = arrayOfByte[b];
      int k = i + 1;
      char[] arrayOfChar1 = HEX_DIGITS;
      arrayOfChar[i] = arrayOfChar1[b1 >> 4 & 0xF];
      i = k + 1;
      arrayOfChar[k] = arrayOfChar1[b1 & 0xF];
      b++;
    } 
    return new String(arrayOfChar);
  }
  
  public iI丨LLL1 hmacSha1(iI丨LLL1 paramiI丨LLL1) {
    return I1I("HmacSHA1", paramiI丨LLL1);
  }
  
  public iI丨LLL1 hmacSha256(iI丨LLL1 paramiI丨LLL1) {
    return I1I("HmacSHA256", paramiI丨LLL1);
  }
  
  public iI丨LLL1 hmacSha512(iI丨LLL1 paramiI丨LLL1) {
    return I1I("HmacSHA512", paramiI丨LLL1);
  }
  
  public final int indexOf(iI丨LLL1 paramiI丨LLL1) {
    return indexOf(paramiI丨LLL1.internalArray(), 0);
  }
  
  public final int indexOf(iI丨LLL1 paramiI丨LLL1, int paramInt) {
    return indexOf(paramiI丨LLL1.internalArray(), paramInt);
  }
  
  public final int indexOf(byte[] paramArrayOfbyte) {
    return indexOf(paramArrayOfbyte, 0);
  }
  
  public int indexOf(byte[] paramArrayOfbyte, int paramInt) {
    paramInt = Math.max(paramInt, 0);
    int i = this.data.length;
    int j = paramArrayOfbyte.length;
    while (paramInt <= i - j) {
      if (llliI.IL1Iii(this.data, paramInt, paramArrayOfbyte, 0, paramArrayOfbyte.length))
        return paramInt; 
      paramInt++;
    } 
    return -1;
  }
  
  public byte[] internalArray() {
    return this.data;
  }
  
  public final int lastIndexOf(iI丨LLL1 paramiI丨LLL1) {
    return lastIndexOf(paramiI丨LLL1.internalArray(), size());
  }
  
  public final int lastIndexOf(iI丨LLL1 paramiI丨LLL1, int paramInt) {
    return lastIndexOf(paramiI丨LLL1.internalArray(), paramInt);
  }
  
  public final int lastIndexOf(byte[] paramArrayOfbyte) {
    return lastIndexOf(paramArrayOfbyte, size());
  }
  
  public int lastIndexOf(byte[] paramArrayOfbyte, int paramInt) {
    for (paramInt = Math.min(paramInt, this.data.length - paramArrayOfbyte.length); paramInt >= 0; paramInt--) {
      if (llliI.IL1Iii(this.data, paramInt, paramArrayOfbyte, 0, paramArrayOfbyte.length))
        return paramInt; 
    } 
    return -1;
  }
  
  public iI丨LLL1 md5() {
    return ILil("MD5");
  }
  
  public boolean rangeEquals(int paramInt1, iI丨LLL1 paramiI丨LLL1, int paramInt2, int paramInt3) {
    return paramiI丨LLL1.rangeEquals(paramInt2, this.data, paramInt1, paramInt3);
  }
  
  public boolean rangeEquals(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
    if (paramInt1 >= 0) {
      byte[] arrayOfByte = this.data;
      if (paramInt1 <= arrayOfByte.length - paramInt3 && paramInt2 >= 0 && paramInt2 <= paramArrayOfbyte.length - paramInt3 && llliI.IL1Iii(arrayOfByte, paramInt1, paramArrayOfbyte, paramInt2, paramInt3))
        return true; 
    } 
    return false;
  }
  
  public iI丨LLL1 sha1() {
    return ILil("SHA-1");
  }
  
  public iI丨LLL1 sha256() {
    return ILil("SHA-256");
  }
  
  public iI丨LLL1 sha512() {
    return ILil("SHA-512");
  }
  
  public int size() {
    return this.data.length;
  }
  
  public final boolean startsWith(iI丨LLL1 paramiI丨LLL1) {
    return rangeEquals(0, paramiI丨LLL1, 0, paramiI丨LLL1.size());
  }
  
  public final boolean startsWith(byte[] paramArrayOfbyte) {
    return rangeEquals(0, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public String string(Charset paramCharset) {
    if (paramCharset != null)
      return new String(this.data, paramCharset); 
    throw new IllegalArgumentException("charset == null");
  }
  
  public iI丨LLL1 substring(int paramInt) {
    return substring(paramInt, this.data.length);
  }
  
  public iI丨LLL1 substring(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      byte[] arrayOfByte = this.data;
      if (paramInt2 <= arrayOfByte.length) {
        int i = paramInt2 - paramInt1;
        if (i >= 0) {
          if (paramInt1 == 0 && paramInt2 == arrayOfByte.length)
            return this; 
          byte[] arrayOfByte1 = new byte[i];
          System.arraycopy(arrayOfByte, paramInt1, arrayOfByte1, 0, i);
          return new iI丨LLL1(arrayOfByte1);
        } 
        throw new IllegalArgumentException("endIndex < beginIndex");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("endIndex > length(");
      stringBuilder.append(this.data.length);
      stringBuilder.append(")");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("beginIndex < 0");
  }
  
  public iI丨LLL1 toAsciiLowercase() {
    int i = 0;
    while (true) {
      byte[] arrayOfByte = this.data;
      if (i < arrayOfByte.length) {
        byte b = arrayOfByte[i];
        if (b < 65 || b > 90) {
          i++;
          continue;
        } 
        arrayOfByte = (byte[])arrayOfByte.clone();
        int j = i + 1;
        arrayOfByte[i] = (byte)(b + 32);
        for (i = j; i < arrayOfByte.length; i++) {
          j = arrayOfByte[i];
          if (j >= 65 && j <= 90)
            arrayOfByte[i] = (byte)(j + 32); 
        } 
        return new iI丨LLL1(arrayOfByte);
      } 
      return this;
    } 
  }
  
  public iI丨LLL1 toAsciiUppercase() {
    int i = 0;
    while (true) {
      byte[] arrayOfByte = this.data;
      if (i < arrayOfByte.length) {
        byte b = arrayOfByte[i];
        if (b < 97 || b > 122) {
          i++;
          continue;
        } 
        arrayOfByte = (byte[])arrayOfByte.clone();
        int j = i + 1;
        arrayOfByte[i] = (byte)(b - 32);
        for (i = j; i < arrayOfByte.length; i++) {
          j = arrayOfByte[i];
          if (j >= 97 && j <= 122)
            arrayOfByte[i] = (byte)(j - 32); 
        } 
        return new iI丨LLL1(arrayOfByte);
      } 
      return this;
    } 
  }
  
  public byte[] toByteArray() {
    return (byte[])this.data.clone();
  }
  
  public String toString() {
    if (this.data.length == 0)
      return "[size=0]"; 
    String str2 = utf8();
    int i = codePointIndexToCharIndex(str2, 64);
    if (i == -1) {
      String str;
      if (this.data.length <= 64) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[hex=");
        stringBuilder.append(hex());
        stringBuilder.append("]");
        str = stringBuilder.toString();
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[size=");
        stringBuilder.append(this.data.length);
        stringBuilder.append(" hex=");
        stringBuilder.append(substring(0, 64).hex());
        stringBuilder.append("…]");
        str = stringBuilder.toString();
      } 
      return str;
    } 
    String str1 = str2.substring(0, i).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
    if (i < str2.length()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[size=");
      stringBuilder.append(this.data.length);
      stringBuilder.append(" text=");
      stringBuilder.append(str1);
      stringBuilder.append("…]");
      str1 = stringBuilder.toString();
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[text=");
      stringBuilder.append(str1);
      stringBuilder.append("]");
      str1 = stringBuilder.toString();
    } 
    return str1;
  }
  
  public String utf8() {
    String str = this.utf8;
    if (str == null) {
      str = new String(this.data, llliI.IL1Iii);
      this.utf8 = str;
    } 
    return str;
  }
  
  public void write(I1I paramI1I) {
    byte[] arrayOfByte = this.data;
    paramI1I.ili丨11(arrayOfByte, 0, arrayOfByte.length);
  }
  
  public void write(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream != null) {
      paramOutputStream.write(this.data);
      return;
    } 
    throw new IllegalArgumentException("out == null");
  }
}
