package I丨L;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class Ll丨1 implements 丨lL {
  public int I1I;
  
  public final l丨Li1LL IL1Iii;
  
  public final Inflater ILil;
  
  public boolean I丨L;
  
  public Ll丨1(l丨Li1LL paraml丨Li1LL, Inflater paramInflater) {
    if (paraml丨Li1LL != null) {
      if (paramInflater != null) {
        this.IL1Iii = paraml丨Li1LL;
        this.ILil = paramInflater;
        return;
      } 
      throw new IllegalArgumentException("inflater == null");
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public void close() throws IOException {
    if (this.I丨L)
      return; 
    this.ILil.end();
    this.I丨L = true;
    this.IL1Iii.close();
  }
  
  public long read(I1I paramI1I, long paramLong) throws IOException {
    int i = paramLong cmp 0L;
    if (i >= 0) {
      if (!this.I丨L) {
        if (i == 0)
          return 0L; 
        while (true) {
          boolean bool = 丨丨LLlI1();
          try {
            lI丨lii lI丨lii = paramI1I.l丨丨i11(1);
            i = (int)Math.min(paramLong, (8192 - lI丨lii.I1I));
            i = this.ILil.inflate(lI丨lii.IL1Iii, lI丨lii.I1I, i);
            if (i > 0) {
              lI丨lii.I1I += i;
              paramLong = paramI1I.ILil;
              long l = i;
              paramI1I.ILil = paramLong + l;
              return l;
            } 
            if (this.ILil.finished() || this.ILil.needsDictionary()) {
              丨丨();
              if (lI丨lii.ILil == lI丨lii.I1I) {
                paramI1I.IL1Iii = lI丨lii.ILil();
                iIi1.IL1Iii(lI丨lii);
              } 
              return -1L;
            } 
            if (!bool)
              continue; 
            EOFException eOFException = new EOFException();
            this("source exhausted prematurely");
            throw eOFException;
          } catch (DataFormatException dataFormatException) {
            throw new IOException(dataFormatException);
          } 
        } 
      } 
      throw new IllegalStateException("closed");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public I11L timeout() {
    return this.IL1Iii.timeout();
  }
  
  public final void 丨丨() throws IOException {
    int i = this.I1I;
    if (i == 0)
      return; 
    i -= this.ILil.getRemaining();
    this.I1I -= i;
    this.IL1Iii.skip(i);
  }
  
  public final boolean 丨丨LLlI1() throws IOException {
    if (!this.ILil.needsInput())
      return false; 
    丨丨();
    if (this.ILil.getRemaining() == 0) {
      if (this.IL1Iii.l丨Li1LL())
        return true; 
      lI丨lii lI丨lii = (this.IL1Iii.IL1Iii()).IL1Iii;
      int j = lI丨lii.I1I;
      int i = lI丨lii.ILil;
      j -= i;
      this.I1I = j;
      this.ILil.setInput(lI丨lii.IL1Iii, i, j);
      return false;
    } 
    throw new IllegalStateException("?");
  }
}
