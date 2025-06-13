package I丨L;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

public final class ll丨L1ii implements l丨Li1LL {
  public boolean I1I;
  
  public final I1I IL1Iii = new I1I();
  
  public final 丨lL ILil;
  
  public ll丨L1ii(丨lL param丨lL) {
    Objects.requireNonNull(param丨lL, "source == null");
    this.ILil = param丨lL;
  }
  
  public l丨Li1LL I11L() {
    return lIi丨I.I丨L(new Lil(this));
  }
  
  public short I11li1() throws IOException {
    llliI(2L);
    return this.IL1Iii.I11li1();
  }
  
  public I1I IL1Iii() {
    return this.IL1Iii;
  }
  
  public iI丨LLL1 ILil(long paramLong) throws IOException {
    llliI(paramLong);
    return this.IL1Iii.ILil(paramLong);
  }
  
  public I1I Ilil() {
    return this.IL1Iii;
  }
  
  public byte[] I丨L() throws IOException {
    this.IL1Iii.lIi丨I(this.ILil);
    return this.IL1Iii.I丨L();
  }
  
  public int L11丨丨丨1(IL丨丨l paramIL丨丨l) throws IOException {
    if (!this.I1I)
      while (true) {
        int i = this.IL1Iii.LLL(paramIL丨丨l, true);
        if (i == -1)
          return -1; 
        if (i == -2) {
          if (this.ILil.read(this.IL1Iii, 8192L) == -1L)
            return -1; 
          continue;
        } 
        int j = paramIL丨丨l.IL1Iii[i].size();
        this.IL1Iii.skip(j);
        return i;
      }  
    throw new IllegalStateException("closed");
  }
  
  public InputStream LL1IL() {
    return new IL1Iii(this);
  }
  
  public boolean Lil(long paramLong) throws IOException {
    if (paramLong >= 0L) {
      if (!this.I1I)
        while (true) {
          I1I i1I = this.IL1Iii;
          if (i1I.ILil < paramLong) {
            if (this.ILil.read(i1I, 8192L) == -1L)
              return false; 
            continue;
          } 
          return true;
        }  
      throw new IllegalStateException("closed");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public String LlLI1() throws IOException {
    return 丨il(Long.MAX_VALUE);
  }
  
  public String Ll丨1(Charset paramCharset) throws IOException {
    if (paramCharset != null) {
      this.IL1Iii.lIi丨I(this.ILil);
      return this.IL1Iii.Ll丨1(paramCharset);
    } 
    throw new IllegalArgumentException("charset == null");
  }
  
  public long L丨1丨1丨I() throws IOException {
    llliI(1L);
    int i = 0;
    while (true) {
      int j = i + 1;
      if (Lil(j)) {
        byte b = this.IL1Iii.丨iI丨丨LLl(i);
        if ((b < 48 || b > 57) && (i != 0 || b != 45)) {
          if (i != 0)
            break; 
          throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", new Object[] { Byte.valueOf(b) }));
        } 
        i = j;
        continue;
      } 
      break;
    } 
    return this.IL1Iii.L丨1丨1丨I();
  }
  
  public long L丨lLLL(byte paramByte) throws IOException {
    return 丨丨LLlI1(paramByte, 0L, Long.MAX_VALUE);
  }
  
  public void close() throws IOException {
    if (this.I1I)
      return; 
    this.I1I = true;
    this.ILil.close();
    this.IL1Iii.丨丨LLlI1();
  }
  
  public String iIi1() throws IOException {
    this.IL1Iii.lIi丨I(this.ILil);
    return this.IL1Iii.iIi1();
  }
  
  public void iI丨LLL1(I1I paramI1I, long paramLong) throws IOException {
    try {
      llliI(paramLong);
      this.IL1Iii.iI丨LLL1(paramI1I, paramLong);
      return;
    } catch (EOFException eOFException) {
      paramI1I.lIi丨I(this.IL1Iii);
      throw eOFException;
    } 
  }
  
  public boolean isOpen() {
    return this.I1I ^ true;
  }
  
  public long lI丨II() throws IOException {
    llliI(1L);
    int i = 0;
    while (true) {
      int j = i + 1;
      if (Lil(j)) {
        byte b = this.IL1Iii.丨iI丨丨LLl(i);
        if ((b < 48 || b > 57) && (b < 97 || b > 102) && (b < 65 || b > 70)) {
          if (i != 0)
            break; 
          throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[] { Byte.valueOf(b) }));
        } 
        i = j;
        continue;
      } 
      break;
    } 
    return this.IL1Iii.lI丨II();
  }
  
  public byte[] lI丨lii(long paramLong) throws IOException {
    llliI(paramLong);
    return this.IL1Iii.lI丨lii(paramLong);
  }
  
  public void llliI(long paramLong) throws IOException {
    if (Lil(paramLong))
      return; 
    throw new EOFException();
  }
  
  public int ll丨L1ii() throws IOException {
    llliI(4L);
    return this.IL1Iii.ll丨L1ii();
  }
  
  public boolean l丨Li1LL() throws IOException {
    if (!this.I1I) {
      boolean bool;
      if (this.IL1Iii.l丨Li1LL() && this.ILil.read(this.IL1Iii, 8192L) == -1L) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    throw new IllegalStateException("closed");
  }
  
  public int read(ByteBuffer paramByteBuffer) throws IOException {
    I1I i1I = this.IL1Iii;
    return (i1I.ILil == 0L && this.ILil.read(i1I, 8192L) == -1L) ? -1 : this.IL1Iii.read(paramByteBuffer);
  }
  
  public long read(I1I paramI1I, long paramLong) throws IOException {
    if (paramI1I != null) {
      if (paramLong >= 0L) {
        if (!this.I1I) {
          I1I i1I = this.IL1Iii;
          if (i1I.ILil == 0L && this.ILil.read(i1I, 8192L) == -1L)
            return -1L; 
          paramLong = Math.min(paramLong, this.IL1Iii.ILil);
          return this.IL1Iii.read(paramI1I, paramLong);
        } 
        throw new IllegalStateException("closed");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(paramLong);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("sink == null");
  }
  
  public byte readByte() throws IOException {
    llliI(1L);
    return this.IL1Iii.readByte();
  }
  
  public void readFully(byte[] paramArrayOfbyte) throws IOException {
    try {
      llliI(paramArrayOfbyte.length);
      this.IL1Iii.readFully(paramArrayOfbyte);
      return;
    } catch (EOFException eOFException) {
      int i = 0;
      while (true) {
        I1I i1I = this.IL1Iii;
        long l = i1I.ILil;
        if (l > 0L) {
          int j = i1I.read(paramArrayOfbyte, i, (int)l);
          if (j != -1) {
            i += j;
            continue;
          } 
          throw new AssertionError();
        } 
        throw eOFException;
      } 
    } 
  }
  
  public int readInt() throws IOException {
    llliI(4L);
    return this.IL1Iii.readInt();
  }
  
  public long readLong() throws IOException {
    llliI(8L);
    return this.IL1Iii.readLong();
  }
  
  public short readShort() throws IOException {
    llliI(2L);
    return this.IL1Iii.readShort();
  }
  
  public void skip(long paramLong) throws IOException {
    if (!this.I1I) {
      while (paramLong > 0L) {
        I1I i1I = this.IL1Iii;
        if (i1I.ILil != 0L || this.ILil.read(i1I, 8192L) != -1L) {
          long l = Math.min(paramLong, this.IL1Iii.iI1i丨I());
          this.IL1Iii.skip(l);
          paramLong -= l;
          continue;
        } 
        throw new EOFException();
      } 
      return;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I11L timeout() {
    return this.ILil.timeout();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("buffer(");
    stringBuilder.append(this.ILil);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public String 丨il(long paramLong) throws IOException {
    if (paramLong >= 0L) {
      long l1;
      if (paramLong == Long.MAX_VALUE) {
        l1 = Long.MAX_VALUE;
      } else {
        l1 = paramLong + 1L;
      } 
      long l2 = 丨丨LLlI1((byte)10, 0L, l1);
      if (l2 != -1L)
        return this.IL1Iii.l丨liiI1(l2); 
      if (l1 < Long.MAX_VALUE && Lil(l1) && this.IL1Iii.丨iI丨丨LLl(l1 - 1L) == 13 && Lil(1L + l1) && this.IL1Iii.丨iI丨丨LLl(l1) == 10)
        return this.IL1Iii.l丨liiI1(l1); 
      I1I i1I1 = new I1I();
      I1I i1I2 = this.IL1Iii;
      i1I2.l1IIi1丨(i1I1, 0L, Math.min(32L, i1I2.iI1i丨I()));
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\\n not found: limit=");
      stringBuilder1.append(Math.min(this.IL1Iii.iI1i丨I(), paramLong));
      stringBuilder1.append(" content=");
      stringBuilder1.append(i1I1.L11丨().hex());
      stringBuilder1.append('…');
      throw new EOFException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("limit < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public long 丨lL(I11li1 paramI11li1) throws IOException {
    if (paramI11li1 != null) {
      long l1 = 0L;
      while (this.ILil.read(this.IL1Iii, 8192L) != -1L) {
        long l = this.IL1Iii.llI();
        if (l > 0L) {
          l1 += l;
          paramI11li1.write(this.IL1Iii, l);
        } 
      } 
      long l2 = l1;
      if (this.IL1Iii.iI1i丨I() > 0L) {
        l2 = l1 + this.IL1Iii.iI1i丨I();
        I1I i1I = this.IL1Iii;
        paramI11li1.write(i1I, i1I.iI1i丨I());
      } 
      return l2;
    } 
    throw new IllegalArgumentException("sink == null");
  }
  
  public long 丨丨LLlI1(byte paramByte, long paramLong1, long paramLong2) throws IOException {
    if (!this.I1I) {
      if (paramLong1 >= 0L && paramLong2 >= paramLong1) {
        while (paramLong1 < paramLong2) {
          long l = this.IL1Iii.I丨Ii(paramByte, paramLong1, paramLong2);
          if (l != -1L)
            return l; 
          I1I i1I = this.IL1Iii;
          l = i1I.ILil;
          if (l >= paramLong2 || this.ILil.read(i1I, 8192L) == -1L)
            break; 
          paramLong1 = Math.max(paramLong1, l);
        } 
        return -1L;
      } 
      throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2) }));
    } 
    throw new IllegalStateException("closed");
  }
  
  public class IL1Iii extends InputStream {
    public final ll丨L1ii IL1Iii;
    
    public IL1Iii(ll丨L1ii this$0) {}
    
    public int available() throws IOException {
      ll丨L1ii ll丨L1ii1 = this.IL1Iii;
      if (!ll丨L1ii1.I1I)
        return (int)Math.min(ll丨L1ii1.IL1Iii.ILil, 2147483647L); 
      throw new IOException("closed");
    }
    
    public void close() throws IOException {
      this.IL1Iii.close();
    }
    
    public int read() throws IOException {
      ll丨L1ii ll丨L1ii1 = this.IL1Iii;
      if (!ll丨L1ii1.I1I) {
        I1I i1I = ll丨L1ii1.IL1Iii;
        return (i1I.ILil == 0L && ll丨L1ii1.ILil.read(i1I, 8192L) == -1L) ? -1 : (this.IL1Iii.IL1Iii.readByte() & 0xFF);
      } 
      throw new IOException("closed");
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (!this.IL1Iii.I1I) {
        llliI.ILil(param1ArrayOfbyte.length, param1Int1, param1Int2);
        ll丨L1ii ll丨L1ii1 = this.IL1Iii;
        I1I i1I = ll丨L1ii1.IL1Iii;
        return (i1I.ILil == 0L && ll丨L1ii1.ILil.read(i1I, 8192L) == -1L) ? -1 : this.IL1Iii.IL1Iii.read(param1ArrayOfbyte, param1Int1, param1Int2);
      } 
      throw new IOException("closed");
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.IL1Iii);
      stringBuilder.append(".inputStream()");
      return stringBuilder.toString();
    }
  }
}
