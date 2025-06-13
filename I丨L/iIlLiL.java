package I丨L;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public final class iIlLiL extends iI丨LLL1 {
  public final transient int[] directory;
  
  public final transient byte[][] segments;
  
  public iIlLiL(I1I paramI1I, int paramInt) {
    super(null);
    llliI.ILil(paramI1I.ILil, 0L, paramInt);
    lI丨lii lI丨lii2 = paramI1I.IL1Iii;
    int k = 0;
    int j = 0;
    int i = 0;
    while (j < paramInt) {
      int m = lI丨lii2.I1I;
      int n = lI丨lii2.ILil;
      if (m != n) {
        j += m - n;
        i++;
        lI丨lii2 = lI丨lii2.l丨Li1LL;
        continue;
      } 
      throw new AssertionError("s.limit == s.pos");
    } 
    this.segments = new byte[i][];
    this.directory = new int[i * 2];
    lI丨lii lI丨lii1 = paramI1I.IL1Iii;
    j = 0;
    i = k;
    while (i < paramInt) {
      byte[][] arrayOfByte = this.segments;
      arrayOfByte[j] = lI丨lii1.IL1Iii;
      k = lI丨lii1.I1I;
      int m = lI丨lii1.ILil;
      k = i + k - m;
      i = k;
      if (k > paramInt)
        i = paramInt; 
      int[] arrayOfInt = this.directory;
      arrayOfInt[j] = i;
      arrayOfInt[arrayOfByte.length + j] = m;
      lI丨lii1.I丨L = true;
      j++;
      lI丨lii1 = lI丨lii1.l丨Li1LL;
    } 
  }
  
  private Object writeReplace() {
    return Ilil();
  }
  
  public final iI丨LLL1 Ilil() {
    return new iI丨LLL1(toByteArray());
  }
  
  public final int I丨L(int paramInt) {
    paramInt = Arrays.binarySearch(this.directory, 0, this.segments.length, paramInt + 1);
    if (paramInt < 0)
      paramInt ^= 0xFFFFFFFF; 
    return paramInt;
  }
  
  public ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
  }
  
  public String base64() {
    return Ilil().base64();
  }
  
  public String base64Url() {
    return Ilil().base64Url();
  }
  
  public boolean equals(Object paramObject) {
    null = true;
    if (paramObject == this)
      return true; 
    if (paramObject instanceof iI丨LLL1) {
      paramObject = paramObject;
      if (paramObject.size() == size() && rangeEquals(0, (iI丨LLL1)paramObject, 0, size()))
        return null; 
    } 
    return false;
  }
  
  public byte getByte(int paramInt) {
    int i;
    llliI.ILil(this.directory[this.segments.length - 1], paramInt, 1L);
    int j = I丨L(paramInt);
    if (j == 0) {
      i = 0;
    } else {
      i = this.directory[j - 1];
    } 
    int[] arrayOfInt = this.directory;
    byte[][] arrayOfByte = this.segments;
    int k = arrayOfInt[arrayOfByte.length + j];
    return arrayOfByte[j][paramInt - i + k];
  }
  
  public int hashCode() {
    int i = this.hashCode;
    if (i != 0)
      return i; 
    int m = this.segments.length;
    byte b = 0;
    int j = 0;
    int k = 1;
    while (b < m) {
      byte[] arrayOfByte = this.segments[b];
      int[] arrayOfInt = this.directory;
      int i1 = arrayOfInt[m + b];
      int n = arrayOfInt[b];
      for (i = i1; i < n - j + i1; i++)
        k = k * 31 + arrayOfByte[i]; 
      b++;
      j = n;
    } 
    this.hashCode = k;
    return k;
  }
  
  public String hex() {
    return Ilil().hex();
  }
  
  public iI丨LLL1 hmacSha1(iI丨LLL1 paramiI丨LLL1) {
    return Ilil().hmacSha1(paramiI丨LLL1);
  }
  
  public iI丨LLL1 hmacSha256(iI丨LLL1 paramiI丨LLL1) {
    return Ilil().hmacSha256(paramiI丨LLL1);
  }
  
  public int indexOf(byte[] paramArrayOfbyte, int paramInt) {
    return Ilil().indexOf(paramArrayOfbyte, paramInt);
  }
  
  public byte[] internalArray() {
    return toByteArray();
  }
  
  public int lastIndexOf(byte[] paramArrayOfbyte, int paramInt) {
    return Ilil().lastIndexOf(paramArrayOfbyte, paramInt);
  }
  
  public iI丨LLL1 md5() {
    return Ilil().md5();
  }
  
  public boolean rangeEquals(int paramInt1, iI丨LLL1 paramiI丨LLL1, int paramInt2, int paramInt3) {
    if (paramInt1 < 0 || paramInt1 > size() - paramInt3)
      return false; 
    int j = I丨L(paramInt1);
    int i = paramInt1;
    for (paramInt1 = j; paramInt3 > 0; paramInt1++) {
      if (paramInt1 == 0) {
        j = 0;
      } else {
        j = this.directory[paramInt1 - 1];
      } 
      int k = Math.min(paramInt3, this.directory[paramInt1] - j + j - i);
      int[] arrayOfInt = this.directory;
      byte[][] arrayOfByte = this.segments;
      int m = arrayOfInt[arrayOfByte.length + paramInt1];
      if (!paramiI丨LLL1.rangeEquals(paramInt2, arrayOfByte[paramInt1], i - j + m, k))
        return false; 
      i += k;
      paramInt2 += k;
      paramInt3 -= k;
    } 
    return true;
  }
  
  public boolean rangeEquals(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
    if (paramInt1 < 0 || paramInt1 > size() - paramInt3 || paramInt2 < 0 || paramInt2 > paramArrayOfbyte.length - paramInt3)
      return false; 
    for (int i = I丨L(paramInt1); paramInt3 > 0; i++) {
      int j;
      if (i == 0) {
        j = 0;
      } else {
        j = this.directory[i - 1];
      } 
      int k = Math.min(paramInt3, this.directory[i] - j + j - paramInt1);
      int[] arrayOfInt = this.directory;
      byte[][] arrayOfByte = this.segments;
      int m = arrayOfInt[arrayOfByte.length + i];
      if (!llliI.IL1Iii(arrayOfByte[i], paramInt1 - j + m, paramArrayOfbyte, paramInt2, k))
        return false; 
      paramInt1 += k;
      paramInt2 += k;
      paramInt3 -= k;
    } 
    return true;
  }
  
  public iI丨LLL1 sha1() {
    return Ilil().sha1();
  }
  
  public iI丨LLL1 sha256() {
    return Ilil().sha256();
  }
  
  public int size() {
    return this.directory[this.segments.length - 1];
  }
  
  public String string(Charset paramCharset) {
    return Ilil().string(paramCharset);
  }
  
  public iI丨LLL1 substring(int paramInt) {
    return Ilil().substring(paramInt);
  }
  
  public iI丨LLL1 substring(int paramInt1, int paramInt2) {
    return Ilil().substring(paramInt1, paramInt2);
  }
  
  public iI丨LLL1 toAsciiLowercase() {
    return Ilil().toAsciiLowercase();
  }
  
  public iI丨LLL1 toAsciiUppercase() {
    return Ilil().toAsciiUppercase();
  }
  
  public byte[] toByteArray() {
    int[] arrayOfInt = this.directory;
    byte[][] arrayOfByte1 = this.segments;
    byte[] arrayOfByte = new byte[arrayOfInt[arrayOfByte1.length - 1]];
    int j = arrayOfByte1.length;
    byte b = 0;
    for (int i = 0; b < j; i = k) {
      int[] arrayOfInt1 = this.directory;
      int m = arrayOfInt1[j + b];
      int k = arrayOfInt1[b];
      System.arraycopy(this.segments[b], m, arrayOfByte, i, k - i);
      b++;
    } 
    return arrayOfByte;
  }
  
  public String toString() {
    return Ilil().toString();
  }
  
  public String utf8() {
    return Ilil().utf8();
  }
  
  public void write(I1I paramI1I) {
    int j = this.segments.length;
    byte b = 0;
    int i;
    for (i = 0; b < j; i = k) {
      int[] arrayOfInt = this.directory;
      int m = arrayOfInt[j + b];
      int k = arrayOfInt[b];
      lI丨lii lI丨lii2 = new lI丨lii(this.segments[b], m, m + k - i, true, false);
      lI丨lii lI丨lii1 = paramI1I.IL1Iii;
      if (lI丨lii1 == null) {
        lI丨lii2.iI丨LLL1 = lI丨lii2;
        lI丨lii2.l丨Li1LL = lI丨lii2;
        paramI1I.IL1Iii = lI丨lii2;
      } else {
        lI丨lii1.iI丨LLL1.I1I(lI丨lii2);
      } 
      b++;
    } 
    paramI1I.ILil += i;
  }
  
  public void write(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream != null) {
      int j = this.segments.length;
      byte b = 0;
      for (int i = 0; b < j; i = k) {
        int[] arrayOfInt = this.directory;
        int m = arrayOfInt[j + b];
        int k = arrayOfInt[b];
        paramOutputStream.write(this.segments[b], m, k - i);
        b++;
      } 
      return;
    } 
    throw new IllegalArgumentException("out == null");
  }
}
