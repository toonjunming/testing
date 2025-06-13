package I丨L;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class ILL implements 丨lL {
  public final Inflater I1I;
  
  public int IL1Iii = 0;
  
  public final l丨Li1LL ILil;
  
  public final CRC32 Ilil = new CRC32();
  
  public final Ll丨1 I丨L;
  
  public ILL(丨lL param丨lL) {
    if (param丨lL != null) {
      Inflater inflater = new Inflater(true);
      this.I1I = inflater;
      param丨lL = lIi丨I.I丨L(param丨lL);
      this.ILil = (l丨Li1LL)param丨lL;
      this.I丨L = new Ll丨1((l丨Li1LL)param丨lL, inflater);
      return;
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public void close() throws IOException {
    this.I丨L.close();
  }
  
  public final void l1IIi1丨(I1I paramI1I, long paramLong1, long paramLong2) {
    lI丨lii lI丨lii = paramI1I.IL1Iii;
    while (true) {
      int i = lI丨lii.I1I;
      int j = lI丨lii.ILil;
      if (paramLong1 >= (i - j)) {
        paramLong1 -= (i - j);
        lI丨lii = lI丨lii.l丨Li1LL;
        continue;
      } 
      break;
    } 
    while (paramLong2 > 0L) {
      int j = (int)(lI丨lii.ILil + paramLong1);
      int i = (int)Math.min((lI丨lii.I1I - j), paramLong2);
      this.Ilil.update(lI丨lii.IL1Iii, j, i);
      paramLong2 -= i;
      lI丨lii = lI丨lii.l丨Li1LL;
      paramLong1 = 0L;
    } 
  }
  
  public final void llI() throws IOException {
    丨丨LLlI1("CRC", this.ILil.ll丨L1ii(), (int)this.Ilil.getValue());
    丨丨LLlI1("ISIZE", this.ILil.ll丨L1ii(), (int)this.I1I.getBytesWritten());
  }
  
  public long read(I1I paramI1I, long paramLong) throws IOException {
    int i = paramLong cmp 0L;
    if (i >= 0) {
      if (i == 0)
        return 0L; 
      if (this.IL1Iii == 0) {
        丨丨();
        this.IL1Iii = 1;
      } 
      if (this.IL1Iii == 1) {
        long l = paramI1I.ILil;
        paramLong = this.I丨L.read(paramI1I, paramLong);
        if (paramLong != -1L) {
          l1IIi1丨(paramI1I, l, paramLong);
          return paramLong;
        } 
        this.IL1Iii = 2;
      } 
      if (this.IL1Iii == 2) {
        llI();
        this.IL1Iii = 3;
        if (!this.ILil.l丨Li1LL())
          throw new IOException("gzip finished without exhausting source"); 
      } 
      return -1L;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public I11L timeout() {
    return this.ILil.timeout();
  }
  
  public final void 丨丨() throws IOException {
    boolean bool;
    this.ILil.llliI(10L);
    byte b = this.ILil.IL1Iii().丨iI丨丨LLl(3L);
    if ((b >> 1 & 0x1) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      l1IIi1丨(this.ILil.IL1Iii(), 0L, 10L); 
    丨丨LLlI1("ID1ID2", 8075, this.ILil.readShort());
    this.ILil.skip(8L);
    if ((b >> 2 & 0x1) == 1) {
      this.ILil.llliI(2L);
      if (bool)
        l1IIi1丨(this.ILil.IL1Iii(), 0L, 2L); 
      short s = this.ILil.IL1Iii().I11li1();
      l丨Li1LL l丨Li1LL1 = this.ILil;
      long l = s;
      l丨Li1LL1.llliI(l);
      if (bool)
        l1IIi1丨(this.ILil.IL1Iii(), 0L, l); 
      this.ILil.skip(l);
    } 
    if ((b >> 3 & 0x1) == 1) {
      long l = this.ILil.L丨lLLL((byte)0);
      if (l != -1L) {
        if (bool)
          l1IIi1丨(this.ILil.IL1Iii(), 0L, l + 1L); 
        this.ILil.skip(l + 1L);
      } else {
        throw new EOFException();
      } 
    } 
    if ((b >> 4 & 0x1) == 1) {
      long l = this.ILil.L丨lLLL((byte)0);
      if (l != -1L) {
        if (bool)
          l1IIi1丨(this.ILil.IL1Iii(), 0L, l + 1L); 
        this.ILil.skip(l + 1L);
      } else {
        throw new EOFException();
      } 
    } 
    if (bool) {
      丨丨LLlI1("FHCRC", this.ILil.I11li1(), (short)(int)this.Ilil.getValue());
      this.Ilil.reset();
    } 
  }
  
  public final void 丨丨LLlI1(String paramString, int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 == paramInt1)
      return; 
    throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt1) }));
  }
}
