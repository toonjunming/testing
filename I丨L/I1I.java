package I丨L;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

public final class I1I implements l丨Li1LL, I丨L, Cloneable, ByteChannel {
  public static final byte[] I1I = new byte[] { 
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
      97, 98, 99, 100, 101, 102 };
  
  @Nullable
  public lI丨lii IL1Iii;
  
  public long ILil;
  
  public I1I I1(int paramInt) {
    lI丨lii lI丨lii1 = l丨丨i11(2);
    byte[] arrayOfByte = lI丨lii1.IL1Iii;
    int j = lI丨lii1.I1I;
    int i = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 8 & 0xFF);
    arrayOfByte[i] = (byte)(paramInt & 0xFF);
    lI丨lii1.I1I = i + 1;
    this.ILil += 2L;
    return this;
  }
  
  public l丨Li1LL I11L() {
    return lIi丨I.I丨L(new Lil(this));
  }
  
  public short I11li1() {
    return llliI.I丨L(readShort());
  }
  
  public I丨L I1I() {
    return this;
  }
  
  public I1I I1IILIIL(int paramInt) {
    if (paramInt < 128) {
      iI(paramInt);
    } else if (paramInt < 2048) {
      iI(paramInt >> 6 | 0xC0);
      iI(paramInt & 0x3F | 0x80);
    } else if (paramInt < 65536) {
      if (paramInt >= 55296 && paramInt <= 57343) {
        iI(63);
      } else {
        iI(paramInt >> 12 | 0xE0);
        iI(paramInt >> 6 & 0x3F | 0x80);
        iI(paramInt & 0x3F | 0x80);
      } 
    } else {
      if (paramInt <= 1114111) {
        iI(paramInt >> 18 | 0xF0);
        iI(paramInt >> 12 & 0x3F | 0x80);
        iI(paramInt >> 6 & 0x3F | 0x80);
        iI(paramInt & 0x3F | 0x80);
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected code point: ");
      stringBuilder.append(Integer.toHexString(paramInt));
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    return this;
  }
  
  public final iI丨LLL1 I1L丨11L() {
    long l = this.ILil;
    if (l <= 2147483647L)
      return ILL丨Ii((int)l); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size > Integer.MAX_VALUE: ");
    stringBuilder.append(this.ILil);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public String IIi丨丨I(long paramLong) throws EOFException {
    return LlIl丨(paramLong, llliI.IL1Iii);
  }
  
  public I1I IL1Iii() {
    return this;
  }
  
  public final iI丨LLL1 ILL丨Ii(int paramInt) {
    return (paramInt == 0) ? iI丨LLL1.EMPTY : new iIlLiL(this, paramInt);
  }
  
  public iI丨LLL1 ILil(long paramLong) throws EOFException {
    return new iI丨LLL1(lI丨lii(paramLong));
  }
  
  public I1I Ilil() {
    return this;
  }
  
  public I1I I丨(long paramLong) {
    if (paramLong == 0L) {
      iI(48);
      return this;
    } 
    int k = Long.numberOfTrailingZeros(Long.highestOneBit(paramLong)) / 4 + 1;
    lI丨lii lI丨lii1 = l丨丨i11(k);
    byte[] arrayOfByte = lI丨lii1.IL1Iii;
    int j = lI丨lii1.I1I;
    for (int i = j + k - 1; i >= j; i--) {
      arrayOfByte[i] = I1I[(int)(0xFL & paramLong)];
      paramLong >>>= 4L;
    } 
    lI丨lii1.I1I += k;
    this.ILil += k;
    return this;
  }
  
  public long I丨Ii(byte paramByte, long paramLong1, long paramLong2) {
    // Byte code:
    //   0: lconst_0
    //   1: lstore #12
    //   3: lload_2
    //   4: lconst_0
    //   5: lcmp
    //   6: iflt -> 294
    //   9: lload #4
    //   11: lload_2
    //   12: lcmp
    //   13: iflt -> 294
    //   16: aload_0
    //   17: getfield ILil : J
    //   20: lstore #8
    //   22: lload #4
    //   24: lload #8
    //   26: lcmp
    //   27: ifle -> 37
    //   30: lload #8
    //   32: lstore #10
    //   34: goto -> 41
    //   37: lload #4
    //   39: lstore #10
    //   41: lload_2
    //   42: lload #10
    //   44: lcmp
    //   45: ifne -> 52
    //   48: ldc2_w -1
    //   51: lreturn
    //   52: aload_0
    //   53: getfield IL1Iii : LI丨L/lI丨lii;
    //   56: astore #15
    //   58: aload #15
    //   60: ifnonnull -> 67
    //   63: ldc2_w -1
    //   66: lreturn
    //   67: lload #12
    //   69: lstore #4
    //   71: aload #15
    //   73: astore #14
    //   75: lload #8
    //   77: lload_2
    //   78: lsub
    //   79: lload_2
    //   80: lcmp
    //   81: ifge -> 130
    //   84: aload #15
    //   86: astore #14
    //   88: lload #8
    //   90: lstore #4
    //   92: aload #14
    //   94: astore #15
    //   96: lload #8
    //   98: lload_2
    //   99: lcmp
    //   100: ifle -> 172
    //   103: aload #14
    //   105: getfield iI丨LLL1 : LI丨L/lI丨lii;
    //   108: astore #14
    //   110: lload #8
    //   112: aload #14
    //   114: getfield I1I : I
    //   117: aload #14
    //   119: getfield ILil : I
    //   122: isub
    //   123: i2l
    //   124: lsub
    //   125: lstore #8
    //   127: goto -> 88
    //   130: aload #14
    //   132: getfield I1I : I
    //   135: aload #14
    //   137: getfield ILil : I
    //   140: isub
    //   141: i2l
    //   142: lload #4
    //   144: ladd
    //   145: lstore #8
    //   147: lload #8
    //   149: lload_2
    //   150: lcmp
    //   151: ifge -> 168
    //   154: aload #14
    //   156: getfield l丨Li1LL : LI丨L/lI丨lii;
    //   159: astore #14
    //   161: lload #8
    //   163: lstore #4
    //   165: goto -> 130
    //   168: aload #14
    //   170: astore #15
    //   172: lload #4
    //   174: lload #10
    //   176: lcmp
    //   177: ifge -> 290
    //   180: aload #15
    //   182: getfield IL1Iii : [B
    //   185: astore #14
    //   187: aload #15
    //   189: getfield I1I : I
    //   192: i2l
    //   193: aload #15
    //   195: getfield ILil : I
    //   198: i2l
    //   199: lload #10
    //   201: ladd
    //   202: lload #4
    //   204: lsub
    //   205: invokestatic min : (JJ)J
    //   208: l2i
    //   209: istore #7
    //   211: aload #15
    //   213: getfield ILil : I
    //   216: i2l
    //   217: lload_2
    //   218: ladd
    //   219: lload #4
    //   221: lsub
    //   222: l2i
    //   223: istore #6
    //   225: iload #6
    //   227: iload #7
    //   229: if_icmpge -> 260
    //   232: aload #14
    //   234: iload #6
    //   236: baload
    //   237: iload_1
    //   238: if_icmpne -> 254
    //   241: iload #6
    //   243: aload #15
    //   245: getfield ILil : I
    //   248: isub
    //   249: i2l
    //   250: lload #4
    //   252: ladd
    //   253: lreturn
    //   254: iinc #6, 1
    //   257: goto -> 225
    //   260: lload #4
    //   262: aload #15
    //   264: getfield I1I : I
    //   267: aload #15
    //   269: getfield ILil : I
    //   272: isub
    //   273: i2l
    //   274: ladd
    //   275: lstore #4
    //   277: aload #15
    //   279: getfield l丨Li1LL : LI丨L/lI丨lii;
    //   282: astore #15
    //   284: lload #4
    //   286: lstore_2
    //   287: goto -> 172
    //   290: ldc2_w -1
    //   293: lreturn
    //   294: new java/lang/IllegalArgumentException
    //   297: dup
    //   298: ldc 'size=%s fromIndex=%s toIndex=%s'
    //   300: iconst_3
    //   301: anewarray java/lang/Object
    //   304: dup
    //   305: iconst_0
    //   306: aload_0
    //   307: getfield ILil : J
    //   310: invokestatic valueOf : (J)Ljava/lang/Long;
    //   313: aastore
    //   314: dup
    //   315: iconst_1
    //   316: lload_2
    //   317: invokestatic valueOf : (J)Ljava/lang/Long;
    //   320: aastore
    //   321: dup
    //   322: iconst_2
    //   323: lload #4
    //   325: invokestatic valueOf : (J)Ljava/lang/Long;
    //   328: aastore
    //   329: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   332: invokespecial <init> : (Ljava/lang/String;)V
    //   335: athrow
  }
  
  public byte[] I丨L() {
    try {
      return lI丨lii(this.ILil);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public iI丨LLL1 L11丨() {
    return new iI丨LLL1(I丨L());
  }
  
  public int L11丨丨丨1(IL丨丨l paramIL丨丨l) {
    int i = LLL(paramIL丨丨l, false);
    if (i == -1)
      return -1; 
    long l = paramIL丨丨l.IL1Iii[i].size();
    try {
      skip(l);
      return i;
    } catch (EOFException eOFException) {
      throw new AssertionError();
    } 
  }
  
  public I1I L1iI1(String paramString, Charset paramCharset) {
    丨i1丨1i(paramString, 0, paramString.length(), paramCharset);
    return this;
  }
  
  public I丨L LI丨l(丨lL param丨lL, long paramLong) throws IOException {
    while (paramLong > 0L) {
      long l = param丨lL.read(this, paramLong);
      if (l != -1L) {
        paramLong -= l;
        continue;
      } 
      throw new EOFException();
    } 
    return this;
  }
  
  public long LI丨丨l丨l(iI丨LLL1 paramiI丨LLL1) {
    return il丨l丨(paramiI丨LLL1, 0L);
  }
  
  public InputStream LL1IL() {
    return new ILil(this);
  }
  
  public int LLL(IL丨丨l paramIL丨丨l, boolean paramBoolean) {
    lI丨lii lI丨lii2 = this.IL1Iii;
    if (lI丨lii2 == null)
      return paramBoolean ? -2 : paramIL丨丨l.indexOf(iI丨LLL1.EMPTY); 
    byte[] arrayOfByte = lI丨lii2.IL1Iii;
    int j = lI丨lii2.ILil;
    int i = lI丨lii2.I1I;
    int[] arrayOfInt = paramIL丨丨l.ILil;
    lI丨lii lI丨lii1 = lI丨lii2;
    int k = 0;
    int m = -1;
    label57: while (true) {
      int i1 = k + 1;
      int i2 = arrayOfInt[k];
      int n = i1 + 1;
      k = arrayOfInt[i1];
      if (k != -1)
        m = k; 
      if (lI丨lii1 == null)
        continue; 
      if (i2 < 0) {
        for (k = n;; k = i1) {
          int i3 = j + 1;
          j = arrayOfByte[j];
          i1 = k + 1;
          if ((j & 0xFF) != arrayOfInt[k])
            return m; 
          if (i1 == n + i2 * -1) {
            k = 1;
          } else {
            k = 0;
          } 
          if (i3 == i) {
            lI丨lii1 = lI丨lii1.l丨Li1LL;
            j = lI丨lii1.ILil;
            arrayOfByte = lI丨lii1.IL1Iii;
            i = lI丨lii1.I1I;
            if (lI丨lii1 == lI丨lii2) {
              if (k == 0)
                return paramBoolean ? -2 : m; 
              lI丨lii1 = null;
            } 
          } else {
            j = i3;
          } 
          if (k != 0) {
            n = arrayOfInt[i1];
            k = i;
            i = j;
            j = n;
            break;
          } 
        } 
        continue;
      } 
      k = j + 1;
      i1 = arrayOfByte[j];
      for (j = n;; j++) {
        if (j == n + i2)
          return m; 
        if ((i1 & 0xFF) == arrayOfInt[j]) {
          i1 = arrayOfInt[j + i2];
          if (k == i) {
            lI丨lii lI丨lii3 = lI丨lii1.l丨Li1LL;
            int i3 = lI丨lii3.ILil;
            byte[] arrayOfByte1 = lI丨lii3.IL1Iii;
            n = lI丨lii3.I1I;
            i = i3;
            arrayOfByte = arrayOfByte1;
            j = i1;
            k = n;
            lI丨lii1 = lI丨lii3;
            if (lI丨lii3 == lI丨lii2) {
              lI丨lii1 = null;
              i = i3;
              arrayOfByte = arrayOfByte1;
              j = i1;
              k = n;
            } 
          } else {
            n = k;
            k = i;
            j = i1;
            i = n;
          } 
          if (j >= 0)
            return j; 
          n = -j;
          j = i;
          i = k;
          k = n;
          continue label57;
        } 
      } 
      break;
    } 
  }
  
  public boolean Lil(long paramLong) {
    boolean bool;
    if (this.ILil >= paramLong) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String LlIl丨(long paramLong, Charset paramCharset) throws EOFException {
    llliI.ILil(this.ILil, 0L, paramLong);
    if (paramCharset != null) {
      if (paramLong <= 2147483647L) {
        if (paramLong == 0L)
          return ""; 
        lI丨lii lI丨lii1 = this.IL1Iii;
        if (lI丨lii1.ILil + paramLong > lI丨lii1.I1I)
          return new String(lI丨lii(paramLong), paramCharset); 
        String str = new String(lI丨lii1.IL1Iii, lI丨lii1.ILil, (int)paramLong, paramCharset);
        int i = (int)(lI丨lii1.ILil + paramLong);
        lI丨lii1.ILil = i;
        this.ILil -= paramLong;
        if (i == lI丨lii1.I1I) {
          this.IL1Iii = lI丨lii1.ILil();
          iIi1.IL1Iii(lI丨lii1);
        } 
        return str;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
      stringBuilder.append(paramLong);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("charset == null");
  }
  
  public String LlLI1() throws EOFException {
    return 丨il(Long.MAX_VALUE);
  }
  
  public I1I LlLiL丨L丨(long paramLong) {
    int j = paramLong cmp 0L;
    if (j == 0) {
      iI(48);
      return this;
    } 
    boolean bool = false;
    int i = 1;
    long l = paramLong;
    if (j < 0) {
      l = -paramLong;
      if (l < 0L) {
        lL("-9223372036854775808");
        return this;
      } 
      bool = true;
    } 
    if (l < 100000000L) {
      if (l < 10000L) {
        if (l < 100L) {
          if (l >= 10L)
            i = 2; 
        } else if (l < 1000L) {
          i = 3;
        } else {
          i = 4;
        } 
      } else if (l < 1000000L) {
        if (l < 100000L) {
          i = 5;
        } else {
          i = 6;
        } 
      } else if (l < 10000000L) {
        i = 7;
      } else {
        i = 8;
      } 
    } else if (l < 1000000000000L) {
      if (l < 10000000000L) {
        if (l < 1000000000L) {
          i = 9;
        } else {
          i = 10;
        } 
      } else if (l < 100000000000L) {
        i = 11;
      } else {
        i = 12;
      } 
    } else if (l < 1000000000000000L) {
      if (l < 10000000000000L) {
        i = 13;
      } else if (l < 100000000000000L) {
        i = 14;
      } else {
        i = 15;
      } 
    } else if (l < 100000000000000000L) {
      if (l < 10000000000000000L) {
        i = 16;
      } else {
        i = 17;
      } 
    } else if (l < 1000000000000000000L) {
      i = 18;
    } else {
      i = 19;
    } 
    j = i;
    if (bool)
      j = i + 1; 
    lI丨lii lI丨lii1 = l丨丨i11(j);
    byte[] arrayOfByte = lI丨lii1.IL1Iii;
    i = lI丨lii1.I1I + j;
    while (l != 0L) {
      int k = (int)(l % 10L);
      arrayOfByte[--i] = I1I[k];
      l /= 10L;
    } 
    if (bool)
      arrayOfByte[i - 1] = 45; 
    lI丨lii1.I1I += j;
    this.ILil += j;
    return this;
  }
  
  public String Ll丨1(Charset paramCharset) {
    try {
      return LlIl丨(this.ILil, paramCharset);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public I1I L丨1l() {
    return this;
  }
  
  public long L丨1丨1丨I() {
    long l2 = this.ILil;
    long l1 = 0L;
    if (l2 != 0L) {
      l2 = -7L;
      byte b1 = 0;
      boolean bool = false;
      for (byte b = 0;; b = b2) {
        byte b2;
        StringBuilder stringBuilder;
        lI丨lii lI丨lii1 = this.IL1Iii;
        byte[] arrayOfByte = lI丨lii1.IL1Iii;
        int i = lI丨lii1.ILil;
        int j = lI丨lii1.I1I;
        while (true) {
          b2 = b;
          if (i < j) {
            b2 = arrayOfByte[i];
            if (b2 >= 48 && b2 <= 57) {
              int m = 48 - b2;
              int k = l1 cmp -922337203685477580L;
              if (k < 0 || (k == 0 && m < l2)) {
                I1I i1I = new I1I();
                i1I.LlLiL丨L丨(l1);
                i1I.iI(b2);
                if (!bool)
                  i1I.readByte(); 
                stringBuilder = new StringBuilder();
                stringBuilder.append("Number too large: ");
                stringBuilder.append(i1I.iIi1());
                throw new NumberFormatException(stringBuilder.toString());
              } 
              l1 = l1 * 10L + m;
            } else if (b2 == 45 && !b1) {
              l2--;
              bool = true;
            } else {
              if (b1) {
                b2 = 1;
                break;
              } 
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Expected leading [0-9] or '-' character but was 0x");
              stringBuilder1.append(Integer.toHexString(b2));
              throw new NumberFormatException(stringBuilder1.toString());
            } 
            i++;
            b1++;
            continue;
          } 
          break;
        } 
        if (i == j) {
          this.IL1Iii = stringBuilder.ILil();
          iIi1.IL1Iii((lI丨lii)stringBuilder);
        } else {
          ((lI丨lii)stringBuilder).ILil = i;
        } 
        if (b2 != 0 || this.IL1Iii == null)
          break; 
      } 
      this.ILil -= b1;
      if (!bool)
        l1 = -l1; 
      return l1;
    } 
    throw new IllegalStateException("size == 0");
  }
  
  public long L丨lLLL(byte paramByte) {
    return I丨Ii(paramByte, 0L, Long.MAX_VALUE);
  }
  
  public void close() {}
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof I1I))
      return false; 
    paramObject = paramObject;
    long l2 = this.ILil;
    if (l2 != ((I1I)paramObject).ILil)
      return false; 
    long l1 = 0L;
    if (l2 == 0L)
      return true; 
    lI丨lii lI丨lii1 = this.IL1Iii;
    paramObject = ((I1I)paramObject).IL1Iii;
    int j = lI丨lii1.ILil;
    int i = ((lI丨lii)paramObject).ILil;
    while (l1 < this.ILil) {
      l2 = Math.min(lI丨lii1.I1I - j, ((lI丨lii)paramObject).I1I - i);
      int k = 0;
      while (k < l2) {
        if (lI丨lii1.IL1Iii[j] != ((lI丨lii)paramObject).IL1Iii[i])
          return false; 
        k++;
        j++;
        i++;
      } 
      lI丨lii lI丨lii2 = lI丨lii1;
      k = j;
      if (j == lI丨lii1.I1I) {
        lI丨lii2 = lI丨lii1.l丨Li1LL;
        k = lI丨lii2.ILil;
      } 
      int m = i;
      Object object = paramObject;
      if (i == ((lI丨lii)paramObject).I1I) {
        object = ((lI丨lii)paramObject).l丨Li1LL;
        m = ((lI丨lii)object).ILil;
      } 
      l1 += l2;
      lI丨lii1 = lI丨lii2;
      j = k;
      i = m;
      paramObject = object;
    } 
    return true;
  }
  
  public void flush() {}
  
  public int hashCode() {
    lI丨lii lI丨lii1 = this.IL1Iii;
    if (lI丨lii1 == null)
      return 0; 
    int i = 1;
    while (true) {
      int k = lI丨lii1.ILil;
      int m = lI丨lii1.I1I;
      int j = i;
      while (k < m) {
        j = j * 31 + lI丨lii1.IL1Iii[k];
        k++;
      } 
      lI丨lii lI丨lii2 = lI丨lii1.l丨Li1LL;
      lI丨lii1 = lI丨lii2;
      i = j;
      if (lI丨lii2 == this.IL1Iii)
        return j; 
    } 
  }
  
  public I1I i1(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      ili丨11(paramArrayOfbyte, 0, paramArrayOfbyte.length);
      return this;
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public I1I iI(int paramInt) {
    lI丨lii lI丨lii1 = l丨丨i11(1);
    byte[] arrayOfByte = lI丨lii1.IL1Iii;
    int i = lI丨lii1.I1I;
    lI丨lii1.I1I = i + 1;
    arrayOfByte[i] = (byte)paramInt;
    this.ILil++;
    return this;
  }
  
  public final long iI1i丨I() {
    return this.ILil;
  }
  
  public String iIi1() {
    try {
      return LlIl丨(this.ILil, llliI.IL1Iii);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public I1I iIilII1(iI丨LLL1 paramiI丨LLL1) {
    if (paramiI丨LLL1 != null) {
      paramiI丨LLL1.write(this);
      return this;
    } 
    throw new IllegalArgumentException("byteString == null");
  }
  
  public void iI丨LLL1(I1I paramI1I, long paramLong) throws EOFException {
    long l = this.ILil;
    if (l >= paramLong) {
      paramI1I.write(this, paramLong);
      return;
    } 
    paramI1I.write(this, l);
    throw new EOFException();
  }
  
  public I1I iI丨Li丨lI(long paramLong) {
    lI丨lii lI丨lii1 = l丨丨i11(8);
    byte[] arrayOfByte = lI丨lii1.IL1Iii;
    int j = lI丨lii1.I1I;
    int i = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 56L & 0xFFL);
    int k = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 48L & 0xFFL);
    j = k + 1;
    arrayOfByte[k] = (byte)(int)(paramLong >>> 40L & 0xFFL);
    i = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 32L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 24L & 0xFFL);
    i = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 16L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 8L & 0xFFL);
    arrayOfByte[j] = (byte)(int)(paramLong & 0xFFL);
    lI丨lii1.I1I = j + 1;
    this.ILil += 8L;
    return this;
  }
  
  public I1I iiIIi丨11(int paramInt) {
    lI丨lii lI丨lii1 = l丨丨i11(4);
    byte[] arrayOfByte = lI丨lii1.IL1Iii;
    int j = lI丨lii1.I1I;
    int i = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 24 & 0xFF);
    j = i + 1;
    arrayOfByte[i] = (byte)(paramInt >>> 16 & 0xFF);
    i = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 8 & 0xFF);
    arrayOfByte[i] = (byte)(paramInt & 0xFF);
    lI丨lii1.I1I = i + 1;
    this.ILil += 4L;
    return this;
  }
  
  public I1I ili丨11(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      long l2 = paramArrayOfbyte.length;
      long l1 = paramInt1;
      long l3 = paramInt2;
      llliI.ILil(l2, l1, l3);
      paramInt2 += paramInt1;
      while (paramInt1 < paramInt2) {
        lI丨lii lI丨lii1 = l丨丨i11(1);
        int i = Math.min(paramInt2 - paramInt1, 8192 - lI丨lii1.I1I);
        System.arraycopy(paramArrayOfbyte, paramInt1, lI丨lii1.IL1Iii, lI丨lii1.I1I, i);
        paramInt1 += i;
        lI丨lii1.I1I += i;
      } 
      this.ILil += l3;
      return this;
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public long il丨l丨(iI丨LLL1 paramiI丨LLL1, long paramLong) {
    long l = 0L;
    if (paramLong >= 0L) {
      byte[] arrayOfByte;
      lI丨lii lI丨lii2 = this.IL1Iii;
      if (lI丨lii2 == null)
        return -1L; 
      long l1 = this.ILil;
      lI丨lii lI丨lii1 = lI丨lii2;
      if (l1 - paramLong < paramLong) {
        while (true) {
          lI丨lii1 = lI丨lii2;
          l = l1;
          if (l1 > paramLong) {
            lI丨lii2 = lI丨lii2.iI丨LLL1;
            l1 -= (lI丨lii2.I1I - lI丨lii2.ILil);
            continue;
          } 
          break;
        } 
      } else {
        while (true) {
          l1 = (lI丨lii1.I1I - lI丨lii1.ILil) + l;
          if (l1 < paramLong) {
            lI丨lii1 = lI丨lii1.l丨Li1LL;
            l = l1;
            continue;
          } 
          break;
        } 
      } 
      if (paramiI丨LLL1.size() == 2) {
        byte b1 = paramiI丨LLL1.getByte(0);
        byte b2 = paramiI丨LLL1.getByte(1);
        while (l < this.ILil) {
          arrayOfByte = lI丨lii1.IL1Iii;
          int i = (int)(lI丨lii1.ILil + paramLong - l);
          int j = lI丨lii1.I1I;
          while (i < j) {
            byte b = arrayOfByte[i];
            if (b == b1 || b == b2) {
              int k = lI丨lii1.ILil;
              return (i - k) + l;
            } 
            i++;
          } 
          l += (lI丨lii1.I1I - lI丨lii1.ILil);
          lI丨lii1 = lI丨lii1.l丨Li1LL;
          paramLong = l;
        } 
      } else {
        arrayOfByte = arrayOfByte.internalArray();
        while (l < this.ILil) {
          byte[] arrayOfByte1 = lI丨lii1.IL1Iii;
          int i = (int)(lI丨lii1.ILil + paramLong - l);
          int j = lI丨lii1.I1I;
          while (i < j) {
            byte b = arrayOfByte1[i];
            int m = arrayOfByte.length;
            for (int k = 0; k < m; k++) {
              if (b == arrayOfByte[k]) {
                k = lI丨lii1.ILil;
                return (i - k) + l;
              } 
            } 
            i++;
          } 
          l += (lI丨lii1.I1I - lI丨lii1.ILil);
          lI丨lii1 = lI丨lii1.l丨Li1LL;
          paramLong = l;
        } 
      } 
      return -1L;
    } 
    throw new IllegalArgumentException("fromIndex < 0");
  }
  
  public boolean isOpen() {
    return true;
  }
  
  public final I1I l1IIi1丨(I1I paramI1I, long paramLong1, long paramLong2) {
    if (paramI1I != null) {
      long l1;
      long l2;
      lI丨lii lI丨lii2;
      llliI.ILil(this.ILil, paramLong1, paramLong2);
      if (paramLong2 == 0L)
        return this; 
      paramI1I.ILil += paramLong2;
      lI丨lii lI丨lii1 = this.IL1Iii;
      while (true) {
        int j = lI丨lii1.I1I;
        int i = lI丨lii1.ILil;
        lI丨lii2 = lI丨lii1;
        l1 = paramLong1;
        l2 = paramLong2;
        if (paramLong1 >= (j - i)) {
          paramLong1 -= (j - i);
          lI丨lii1 = lI丨lii1.l丨Li1LL;
          continue;
        } 
        break;
      } 
      while (l2 > 0L) {
        lI丨lii lI丨lii3 = lI丨lii2.I丨L();
        int i = (int)(lI丨lii3.ILil + l1);
        lI丨lii3.ILil = i;
        lI丨lii3.I1I = Math.min(i + (int)l2, lI丨lii3.I1I);
        lI丨lii1 = paramI1I.IL1Iii;
        if (lI丨lii1 == null) {
          lI丨lii3.iI丨LLL1 = lI丨lii3;
          lI丨lii3.l丨Li1LL = lI丨lii3;
          paramI1I.IL1Iii = lI丨lii3;
        } else {
          lI丨lii1.iI丨LLL1.I1I(lI丨lii3);
        } 
        l2 -= (lI丨lii3.I1I - lI丨lii3.ILil);
        lI丨lii2 = lI丨lii2.l丨Li1LL;
        l1 = 0L;
      } 
      return this;
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  public I1I l1Lll(String paramString, int paramInt1, int paramInt2) {
    if (paramString != null) {
      if (paramInt1 >= 0) {
        if (paramInt2 >= paramInt1) {
          if (paramInt2 <= paramString.length()) {
            while (paramInt1 < paramInt2) {
              char c = paramString.charAt(paramInt1);
              if (c < '') {
                lI丨lii lI丨lii1 = l丨丨i11(1);
                byte[] arrayOfByte = lI丨lii1.IL1Iii;
                int j = lI丨lii1.I1I - paramInt1;
                int k = Math.min(paramInt2, 8192 - j);
                int i = paramInt1 + 1;
                arrayOfByte[paramInt1 + j] = (byte)c;
                for (paramInt1 = i; paramInt1 < k; paramInt1++) {
                  i = paramString.charAt(paramInt1);
                  if (i >= 128)
                    break; 
                  arrayOfByte[paramInt1 + j] = (byte)i;
                } 
                i = lI丨lii1.I1I;
                j = j + paramInt1 - i;
                lI丨lii1.I1I = i + j;
                this.ILil += j;
                continue;
              } 
              if (c < 'ࠀ') {
                iI(c >> 6 | 0xC0);
                iI(c & 0x3F | 0x80);
              } else if (c < '?' || c > '?') {
                iI(c >> 12 | 0xE0);
                iI(c >> 6 & 0x3F | 0x80);
                iI(c & 0x3F | 0x80);
              } else {
                int j = paramInt1 + 1;
                if (j < paramInt2) {
                  i = paramString.charAt(j);
                } else {
                  i = 0;
                } 
                if (c > '?' || i < 56320 || i > 57343) {
                  iI(63);
                  paramInt1 = j;
                  continue;
                } 
                int i = ((c & 0xFFFF27FF) << 10 | 0xFFFF23FF & i) + 65536;
                iI(i >> 18 | 0xF0);
                iI(i >> 12 & 0x3F | 0x80);
                iI(i >> 6 & 0x3F | 0x80);
                iI(i & 0x3F | 0x80);
                paramInt1 += 2;
                continue;
              } 
              paramInt1++;
            } 
            return this;
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("endIndex > string.length: ");
          stringBuilder2.append(paramInt2);
          stringBuilder2.append(" > ");
          stringBuilder2.append(paramString.length());
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("endIndex < beginIndex: ");
        stringBuilder1.append(paramInt2);
        stringBuilder1.append(" < ");
        stringBuilder1.append(paramInt1);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("beginIndex < 0: ");
      stringBuilder.append(paramInt1);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("string == null");
  }
  
  public long lIi丨I(丨lL param丨lL) throws IOException {
    if (param丨lL != null) {
      long l = 0L;
      while (true) {
        long l1 = param丨lL.read(this, 8192L);
        if (l1 != -1L) {
          l += l1;
          continue;
        } 
        return l;
      } 
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public long lI丨II() {
    if (this.ILil != 0L) {
      int i;
      long l1;
      int j = 0;
      long l2 = 0L;
      byte b = 0;
      while (true) {
        byte b1;
        I1I i1I;
        lI丨lii lI丨lii1 = this.IL1Iii;
        byte[] arrayOfByte = lI丨lii1.IL1Iii;
        int k = lI丨lii1.ILil;
        int m = lI丨lii1.I1I;
        l1 = l2;
        i = j;
        while (true) {
          b1 = b;
          if (k < m) {
            b1 = arrayOfByte[k];
            if (b1 >= 48 && b1 <= 57) {
              j = b1 - 48;
            } else {
              if (b1 >= 97 && b1 <= 102) {
                j = b1 - 97;
              } else if (b1 >= 65 && b1 <= 70) {
                j = b1 - 65;
              } else {
                if (i != 0) {
                  b1 = 1;
                  break;
                } 
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("Expected leading [0-9a-fA-F] character but was 0x");
                stringBuilder1.append(Integer.toHexString(b1));
                throw new NumberFormatException(stringBuilder1.toString());
              } 
              j += 10;
            } 
            if ((0xF000000000000000L & l1) == 0L) {
              l1 = l1 << 4L | j;
              k++;
              i++;
              continue;
            } 
            i1I = new I1I();
            i1I.I丨(l1);
            i1I.iI(b1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Number too large: ");
            stringBuilder.append(i1I.iIi1());
            throw new NumberFormatException(stringBuilder.toString());
          } 
          break;
        } 
        if (k == m) {
          this.IL1Iii = i1I.ILil();
          iIi1.IL1Iii((lI丨lii)i1I);
        } else {
          ((lI丨lii)i1I).ILil = k;
        } 
        if (b1 == 0) {
          j = i;
          b = b1;
          l2 = l1;
          if (this.IL1Iii == null)
            break; 
          continue;
        } 
        break;
      } 
      this.ILil -= i;
      return l1;
    } 
    throw new IllegalStateException("size == 0");
  }
  
  public byte[] lI丨lii(long paramLong) throws EOFException {
    llliI.ILil(this.ILil, 0L, paramLong);
    if (paramLong <= 2147483647L) {
      byte[] arrayOfByte = new byte[(int)paramLong];
      readFully(arrayOfByte);
      return arrayOfByte;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public I1I lL(String paramString) {
    l1Lll(paramString, 0, paramString.length());
    return this;
  }
  
  public final long llI() {
    long l2 = this.ILil;
    if (l2 == 0L)
      return 0L; 
    lI丨lii lI丨lii1 = this.IL1Iii.iI丨LLL1;
    int i = lI丨lii1.I1I;
    long l1 = l2;
    if (i < 8192) {
      l1 = l2;
      if (lI丨lii1.Ilil)
        l1 = l2 - (i - lI丨lii1.ILil); 
    } 
    return l1;
  }
  
  public void llliI(long paramLong) throws EOFException {
    if (this.ILil >= paramLong)
      return; 
    throw new EOFException();
  }
  
  public int ll丨L1ii() {
    return llliI.I1I(readInt());
  }
  
  public boolean l丨Li1LL() {
    boolean bool;
    if (this.ILil == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String l丨liiI1(long paramLong) throws EOFException {
    if (paramLong > 0L) {
      long l = paramLong - 1L;
      if (丨iI丨丨LLl(l) == 13) {
        String str1 = IIi丨丨I(l);
        skip(2L);
        return str1;
      } 
    } 
    String str = IIi丨丨I(paramLong);
    skip(1L);
    return str;
  }
  
  public lI丨lii l丨丨i11(int paramInt) {
    lI丨lii lI丨lii2;
    if (paramInt >= 1 && paramInt <= 8192) {
      lI丨lii lI丨lii3 = this.IL1Iii;
      if (lI丨lii3 == null) {
        lI丨lii3 = iIi1.ILil();
        this.IL1Iii = lI丨lii3;
        lI丨lii3.iI丨LLL1 = lI丨lii3;
        lI丨lii3.l丨Li1LL = lI丨lii3;
        return lI丨lii3;
      } 
      lI丨lii2 = lI丨lii3.iI丨LLL1;
      if (lI丨lii2.I1I + paramInt <= 8192) {
        lI丨lii3 = lI丨lii2;
        if (!lI丨lii2.Ilil) {
          lI丨lii3 = iIi1.ILil();
          lI丨lii2.I1I(lI丨lii3);
          return lI丨lii3;
        } 
        return lI丨lii3;
      } 
    } else {
      throw new IllegalArgumentException();
    } 
    lI丨lii lI丨lii1 = iIi1.ILil();
    lI丨lii2.I1I(lI丨lii1);
    return lI丨lii1;
  }
  
  public int read(ByteBuffer paramByteBuffer) throws IOException {
    lI丨lii lI丨lii1 = this.IL1Iii;
    if (lI丨lii1 == null)
      return -1; 
    int j = Math.min(paramByteBuffer.remaining(), lI丨lii1.I1I - lI丨lii1.ILil);
    paramByteBuffer.put(lI丨lii1.IL1Iii, lI丨lii1.ILil, j);
    int i = lI丨lii1.ILil + j;
    lI丨lii1.ILil = i;
    this.ILil -= j;
    if (i == lI丨lii1.I1I) {
      this.IL1Iii = lI丨lii1.ILil();
      iIi1.IL1Iii(lI丨lii1);
    } 
    return j;
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    llliI.ILil(paramArrayOfbyte.length, paramInt1, paramInt2);
    lI丨lii lI丨lii1 = this.IL1Iii;
    if (lI丨lii1 == null)
      return -1; 
    paramInt2 = Math.min(paramInt2, lI丨lii1.I1I - lI丨lii1.ILil);
    System.arraycopy(lI丨lii1.IL1Iii, lI丨lii1.ILil, paramArrayOfbyte, paramInt1, paramInt2);
    paramInt1 = lI丨lii1.ILil + paramInt2;
    lI丨lii1.ILil = paramInt1;
    this.ILil -= paramInt2;
    if (paramInt1 == lI丨lii1.I1I) {
      this.IL1Iii = lI丨lii1.ILil();
      iIi1.IL1Iii(lI丨lii1);
    } 
    return paramInt2;
  }
  
  public long read(I1I paramI1I, long paramLong) {
    if (paramI1I != null) {
      if (paramLong >= 0L) {
        long l2 = this.ILil;
        if (l2 == 0L)
          return -1L; 
        long l1 = paramLong;
        if (paramLong > l2)
          l1 = l2; 
        paramI1I.write(this, l1);
        return l1;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(paramLong);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("sink == null");
  }
  
  public byte readByte() {
    long l = this.ILil;
    if (l != 0L) {
      lI丨lii lI丨lii1 = this.IL1Iii;
      int j = lI丨lii1.ILil;
      int i = lI丨lii1.I1I;
      byte[] arrayOfByte = lI丨lii1.IL1Iii;
      int k = j + 1;
      byte b = arrayOfByte[j];
      this.ILil = l - 1L;
      if (k == i) {
        this.IL1Iii = lI丨lii1.ILil();
        iIi1.IL1Iii(lI丨lii1);
      } else {
        lI丨lii1.ILil = k;
      } 
      return b;
    } 
    throw new IllegalStateException("size == 0");
  }
  
  public void readFully(byte[] paramArrayOfbyte) throws EOFException {
    int i = 0;
    while (i < paramArrayOfbyte.length) {
      int j = read(paramArrayOfbyte, i, paramArrayOfbyte.length - i);
      if (j != -1) {
        i += j;
        continue;
      } 
      throw new EOFException();
    } 
  }
  
  public int readInt() {
    long l = this.ILil;
    if (l >= 4L) {
      lI丨lii lI丨lii1 = this.IL1Iii;
      int j = lI丨lii1.ILil;
      int i = lI丨lii1.I1I;
      if (i - j < 4)
        return (readByte() & 0xFF) << 24 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF; 
      byte[] arrayOfByte = lI丨lii1.IL1Iii;
      int k = j + 1;
      j = arrayOfByte[j];
      int n = k + 1;
      k = arrayOfByte[k];
      int m = n + 1;
      n = arrayOfByte[n];
      int i1 = m + 1;
      m = arrayOfByte[m];
      this.ILil = l - 4L;
      if (i1 == i) {
        this.IL1Iii = lI丨lii1.ILil();
        iIi1.IL1Iii(lI丨lii1);
      } else {
        lI丨lii1.ILil = i1;
      } 
      return (j & 0xFF) << 24 | (k & 0xFF) << 16 | (n & 0xFF) << 8 | m & 0xFF;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size < 4: ");
    stringBuilder.append(this.ILil);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public long readLong() {
    long l = this.ILil;
    if (l >= 8L) {
      lI丨lii lI丨lii1 = this.IL1Iii;
      int j = lI丨lii1.ILil;
      int i = lI丨lii1.I1I;
      if (i - j < 8)
        return (readInt() & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL & readInt(); 
      byte[] arrayOfByte = lI丨lii1.IL1Iii;
      int k = j + 1;
      long l2 = arrayOfByte[j];
      j = k + 1;
      long l6 = arrayOfByte[k];
      k = j + 1;
      long l4 = arrayOfByte[j];
      int m = k + 1;
      long l3 = arrayOfByte[k];
      j = m + 1;
      long l7 = arrayOfByte[m];
      k = j + 1;
      long l1 = arrayOfByte[j];
      j = k + 1;
      long l5 = arrayOfByte[k];
      k = j + 1;
      long l8 = arrayOfByte[j];
      this.ILil = l - 8L;
      if (k == i) {
        this.IL1Iii = lI丨lii1.ILil();
        iIi1.IL1Iii(lI丨lii1);
      } else {
        lI丨lii1.ILil = k;
      } 
      return (l6 & 0xFFL) << 48L | (l2 & 0xFFL) << 56L | (l4 & 0xFFL) << 40L | (l3 & 0xFFL) << 32L | (l7 & 0xFFL) << 24L | (l1 & 0xFFL) << 16L | (l5 & 0xFFL) << 8L | l8 & 0xFFL;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size < 8: ");
    stringBuilder.append(this.ILil);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public short readShort() {
    long l = this.ILil;
    if (l >= 2L) {
      lI丨lii lI丨lii1 = this.IL1Iii;
      int j = lI丨lii1.ILil;
      int i = lI丨lii1.I1I;
      if (i - j < 2)
        return (short)((readByte() & 0xFF) << 8 | readByte() & 0xFF); 
      byte[] arrayOfByte = lI丨lii1.IL1Iii;
      int k = j + 1;
      j = arrayOfByte[j];
      int m = k + 1;
      k = arrayOfByte[k];
      this.ILil = l - 2L;
      if (m == i) {
        this.IL1Iii = lI丨lii1.ILil();
        iIi1.IL1Iii(lI丨lii1);
      } else {
        lI丨lii1.ILil = m;
      } 
      return (short)((j & 0xFF) << 8 | k & 0xFF);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size < 2: ");
    stringBuilder.append(this.ILil);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void skip(long paramLong) throws EOFException {
    while (paramLong > 0L) {
      lI丨lii lI丨lii1 = this.IL1Iii;
      if (lI丨lii1 != null) {
        int i = (int)Math.min(paramLong, (lI丨lii1.I1I - lI丨lii1.ILil));
        long l1 = this.ILil;
        long l2 = i;
        this.ILil = l1 - l2;
        l1 = paramLong - l2;
        lI丨lii1 = this.IL1Iii;
        i = lI丨lii1.ILil + i;
        lI丨lii1.ILil = i;
        paramLong = l1;
        if (i == lI丨lii1.I1I) {
          this.IL1Iii = lI丨lii1.ILil();
          iIi1.IL1Iii(lI丨lii1);
          paramLong = l1;
        } 
        continue;
      } 
      throw new EOFException();
    } 
  }
  
  public I11L timeout() {
    return I11L.NONE;
  }
  
  public String toString() {
    return I1L丨11L().toString();
  }
  
  public int write(ByteBuffer paramByteBuffer) throws IOException {
    if (paramByteBuffer != null) {
      int j = paramByteBuffer.remaining();
      int i = j;
      while (i > 0) {
        lI丨lii lI丨lii1 = l丨丨i11(1);
        int k = Math.min(i, 8192 - lI丨lii1.I1I);
        paramByteBuffer.get(lI丨lii1.IL1Iii, lI丨lii1.I1I, k);
        i -= k;
        lI丨lii1.I1I += k;
      } 
      this.ILil += j;
      return j;
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public void write(I1I paramI1I, long paramLong) {
    if (paramI1I != null) {
      if (paramI1I != this) {
        llliI.ILil(paramI1I.ILil, 0L, paramLong);
        while (paramLong > 0L) {
          lI丨lii lI丨lii2 = paramI1I.IL1Iii;
          if (paramLong < (lI丨lii2.I1I - lI丨lii2.ILil)) {
            lI丨lii lI丨lii3 = this.IL1Iii;
            if (lI丨lii3 != null) {
              lI丨lii3 = lI丨lii3.iI丨LLL1;
            } else {
              lI丨lii3 = null;
            } 
            if (lI丨lii3 != null && lI丨lii3.Ilil) {
              int i;
              long l1 = lI丨lii3.I1I;
              if (lI丨lii3.I丨L) {
                i = 0;
              } else {
                i = lI丨lii3.ILil;
              } 
              if (l1 + paramLong - i <= 8192L) {
                lI丨lii2.iI丨LLL1(lI丨lii3, (int)paramLong);
                paramI1I.ILil -= paramLong;
                this.ILil += paramLong;
                return;
              } 
            } 
            paramI1I.IL1Iii = lI丨lii2.Ilil((int)paramLong);
          } 
          lI丨lii2 = paramI1I.IL1Iii;
          long l = (lI丨lii2.I1I - lI丨lii2.ILil);
          paramI1I.IL1Iii = lI丨lii2.ILil();
          lI丨lii lI丨lii1 = this.IL1Iii;
          if (lI丨lii1 == null) {
            this.IL1Iii = lI丨lii2;
            lI丨lii2.iI丨LLL1 = lI丨lii2;
            lI丨lii2.l丨Li1LL = lI丨lii2;
          } else {
            lI丨lii1.iI丨LLL1.I1I(lI丨lii2);
            lI丨lii2.IL1Iii();
          } 
          paramI1I.ILil -= l;
          this.ILil += l;
          paramLong -= l;
        } 
        return;
      } 
      throw new IllegalArgumentException("source == this");
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public I1I 丨i1丨1i(String paramString, int paramInt1, int paramInt2, Charset paramCharset) {
    if (paramString != null) {
      if (paramInt1 >= 0) {
        if (paramInt2 >= paramInt1) {
          byte[] arrayOfByte;
          if (paramInt2 <= paramString.length()) {
            if (paramCharset != null) {
              if (paramCharset.equals(llliI.IL1Iii)) {
                l1Lll(paramString, paramInt1, paramInt2);
                return this;
              } 
              arrayOfByte = paramString.substring(paramInt1, paramInt2).getBytes(paramCharset);
              ili丨11(arrayOfByte, 0, arrayOfByte.length);
              return this;
            } 
            throw new IllegalArgumentException("charset == null");
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("endIndex > string.length: ");
          stringBuilder2.append(paramInt2);
          stringBuilder2.append(" > ");
          stringBuilder2.append(arrayOfByte.length());
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("endIndex < beginIndex: ");
        stringBuilder1.append(paramInt2);
        stringBuilder1.append(" < ");
        stringBuilder1.append(paramInt1);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("beginIndex < 0: ");
      stringBuilder.append(paramInt1);
      throw new IllegalAccessError(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("string == null");
  }
  
  public final byte 丨iI丨丨LLl(long paramLong) {
    llliI.ILil(this.ILil, paramLong, 1L);
    long l = this.ILil;
    if (l - paramLong > paramLong)
      for (lI丨lii lI丨lii2 = this.IL1Iii;; lI丨lii2 = lI丨lii2.l丨Li1LL) {
        int i = lI丨lii2.I1I;
        int j = lI丨lii2.ILil;
        l = (i - j);
        if (paramLong < l)
          return lI丨lii2.IL1Iii[j + (int)paramLong]; 
        paramLong -= l;
      }  
    paramLong -= l;
    lI丨lii lI丨lii1 = this.IL1Iii;
    while (true) {
      lI丨lii lI丨lii2 = lI丨lii1.iI丨LLL1;
      int i = lI丨lii2.I1I;
      int j = lI丨lii2.ILil;
      l = paramLong + (i - j);
      lI丨lii1 = lI丨lii2;
      paramLong = l;
      if (l >= 0L)
        return lI丨lii2.IL1Iii[j + (int)l]; 
    } 
  }
  
  public String 丨il(long paramLong) throws EOFException {
    if (paramLong >= 0L) {
      long l1 = Long.MAX_VALUE;
      if (paramLong != Long.MAX_VALUE)
        l1 = paramLong + 1L; 
      long l2 = I丨Ii((byte)10, 0L, l1);
      if (l2 != -1L)
        return l丨liiI1(l2); 
      if (l1 < iI1i丨I() && 丨iI丨丨LLl(l1 - 1L) == 13 && 丨iI丨丨LLl(l1) == 10)
        return l丨liiI1(l1); 
      I1I i1I = new I1I();
      l1IIi1丨(i1I, 0L, Math.min(32L, iI1i丨I()));
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\\n not found: limit=");
      stringBuilder1.append(Math.min(iI1i丨I(), paramLong));
      stringBuilder1.append(" content=");
      stringBuilder1.append(i1I.L11丨().hex());
      stringBuilder1.append('…');
      throw new EOFException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("limit < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public long 丨lL(I11li1 paramI11li1) throws IOException {
    long l = this.ILil;
    if (l > 0L)
      paramI11li1.write(this, l); 
    return l;
  }
  
  public I1I 丨丨() {
    I1I i1I = new I1I();
    if (this.ILil == 0L)
      return i1I; 
    lI丨lii lI丨lii1 = this.IL1Iii.I丨L();
    i1I.IL1Iii = lI丨lii1;
    lI丨lii1.iI丨LLL1 = lI丨lii1;
    lI丨lii1.l丨Li1LL = lI丨lii1;
    lI丨lii1 = this.IL1Iii;
    while (true) {
      lI丨lii1 = lI丨lii1.l丨Li1LL;
      if (lI丨lii1 != this.IL1Iii) {
        i1I.IL1Iii.iI丨LLL1.I1I(lI丨lii1.I丨L());
        continue;
      } 
      i1I.ILil = this.ILil;
      return i1I;
    } 
  }
  
  public final void 丨丨LLlI1() {
    try {
      skip(this.ILil);
      return;
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public OutputStream 丨丨丨1丨() {
    return new IL1Iii(this);
  }
  
  public final I1I 丨丨丨丨(I1I paramI1I) {
    if (paramI1I.IL1Iii == null) {
      paramI1I.IL1Iii = this;
      paramI1I.ILil = true;
      return paramI1I;
    } 
    throw new IllegalStateException("already attached to a buffer");
  }
  
  public static final class I1I implements Closeable {
    public lI丨lii I1I;
    
    public I1I IL1Iii;
    
    public boolean ILil;
    
    public byte[] Ilil;
    
    public long I丨L = -1L;
    
    public int iI丨LLL1 = -1;
    
    public int l丨Li1LL = -1;
    
    public void close() {
      if (this.IL1Iii != null) {
        this.IL1Iii = null;
        this.I1I = null;
        this.I丨L = -1L;
        this.Ilil = null;
        this.l丨Li1LL = -1;
        this.iI丨LLL1 = -1;
        return;
      } 
      throw new IllegalStateException("not attached to a buffer");
    }
    
    public final int 丨丨(long param1Long) {
      int i = param1Long cmp -1L;
      if (i >= 0) {
        I1I i1I = this.IL1Iii;
        long l = i1I.ILil;
        if (param1Long <= l) {
          long l1;
          if (i == 0 || param1Long == l) {
            this.I1I = null;
            this.I丨L = param1Long;
            this.Ilil = null;
            this.l丨Li1LL = -1;
            this.iI丨LLL1 = -1;
            return -1;
          } 
          long l2 = 0L;
          lI丨lii lI丨lii2 = i1I.IL1Iii;
          lI丨lii lI丨lii1 = this.I1I;
          if (lI丨lii1 != null) {
            l1 = this.I丨L - (this.l丨Li1LL - lI丨lii1.ILil);
            if (l1 > param1Long) {
              l = l1;
              lI丨lii lI丨lii3 = lI丨lii2;
              lI丨lii2 = lI丨lii1;
              l1 = l2;
              lI丨lii1 = lI丨lii3;
            } 
          } else {
            lI丨lii1 = lI丨lii2;
            l1 = l2;
          } 
          l2 = l;
          if (l - param1Long > param1Long - l1) {
            lI丨lii2 = lI丨lii1;
            while (true) {
              int k = lI丨lii2.I1I;
              i = lI丨lii2.ILil;
              l = l1;
              lI丨lii1 = lI丨lii2;
              if (param1Long >= (k - i) + l1) {
                l1 += (k - i);
                lI丨lii2 = lI丨lii2.l丨Li1LL;
                continue;
              } 
              break;
            } 
          } else {
            while (l2 > param1Long) {
              lI丨lii2 = lI丨lii2.iI丨LLL1;
              l2 -= (lI丨lii2.I1I - lI丨lii2.ILil);
            } 
            lI丨lii1 = lI丨lii2;
            l = l2;
          } 
          lI丨lii2 = lI丨lii1;
          if (this.ILil) {
            lI丨lii2 = lI丨lii1;
            if (lI丨lii1.I丨L) {
              lI丨lii2 = lI丨lii1.l丨Li1LL();
              I1I i1I1 = this.IL1Iii;
              if (i1I1.IL1Iii == lI丨lii1)
                i1I1.IL1Iii = lI丨lii2; 
              lI丨lii1.I1I(lI丨lii2);
              lI丨lii2.iI丨LLL1.ILil();
            } 
          } 
          this.I1I = lI丨lii2;
          this.I丨L = param1Long;
          this.Ilil = lI丨lii2.IL1Iii;
          int j = lI丨lii2.ILil + (int)(param1Long - l);
          this.l丨Li1LL = j;
          i = lI丨lii2.I1I;
          this.iI丨LLL1 = i;
          return i - j;
        } 
      } 
      throw new ArrayIndexOutOfBoundsException(String.format("offset=%s > size=%s", new Object[] { Long.valueOf(param1Long), Long.valueOf(this.IL1Iii.ILil) }));
    }
    
    public final int 丨丨LLlI1() {
      long l = this.I丨L;
      if (l != this.IL1Iii.ILil)
        return (l == -1L) ? 丨丨(0L) : 丨丨(l + (this.iI丨LLL1 - this.l丨Li1LL)); 
      throw new IllegalStateException();
    }
  }
  
  public class IL1Iii extends OutputStream {
    public final I1I IL1Iii;
    
    public IL1Iii(I1I this$0) {}
    
    public void close() {}
    
    public void flush() {}
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.IL1Iii);
      stringBuilder.append(".outputStream()");
      return stringBuilder.toString();
    }
    
    public void write(int param1Int) {
      this.IL1Iii.iI((byte)param1Int);
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      this.IL1Iii.ili丨11(param1ArrayOfbyte, param1Int1, param1Int2);
    }
  }
  
  public class ILil extends InputStream {
    public final I1I IL1Iii;
    
    public ILil(I1I this$0) {}
    
    public int available() {
      return (int)Math.min(this.IL1Iii.ILil, 2147483647L);
    }
    
    public void close() {}
    
    public int read() {
      I1I i1I = this.IL1Iii;
      return (i1I.ILil > 0L) ? (i1I.readByte() & 0xFF) : -1;
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      return this.IL1Iii.read(param1ArrayOfbyte, param1Int1, param1Int2);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.IL1Iii);
      stringBuilder.append(".inputStream()");
      return stringBuilder.toString();
    }
  }
}
