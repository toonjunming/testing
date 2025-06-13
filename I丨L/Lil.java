package I丨L;

import java.io.IOException;

public final class Lil implements 丨lL {
  public lI丨lii I1I;
  
  public final l丨Li1LL IL1Iii;
  
  public final I1I ILil;
  
  public boolean Ilil;
  
  public int I丨L;
  
  public long l丨Li1LL;
  
  public Lil(l丨Li1LL paraml丨Li1LL) {
    byte b;
    this.IL1Iii = paraml丨Li1LL;
    paraml丨Li1LL = paraml丨Li1LL.IL1Iii();
    this.ILil = (I1I)paraml丨Li1LL;
    lI丨lii lI丨lii1 = ((I1I)paraml丨Li1LL).IL1Iii;
    this.I1I = lI丨lii1;
    if (lI丨lii1 != null) {
      b = lI丨lii1.ILil;
    } else {
      b = -1;
    } 
    this.I丨L = b;
  }
  
  public void close() throws IOException {
    this.Ilil = true;
  }
  
  public long read(I1I paramI1I, long paramLong) throws IOException {
    if (!this.Ilil) {
      lI丨lii lI丨lii1 = this.I1I;
      if (lI丨lii1 != null) {
        lI丨lii lI丨lii2 = this.ILil.IL1Iii;
        if (lI丨lii1 != lI丨lii2 || this.I丨L != lI丨lii2.ILil)
          throw new IllegalStateException("Peek source is invalid because upstream source was used"); 
      } 
      this.IL1Iii.Lil(this.l丨Li1LL + paramLong);
      if (this.I1I == null) {
        lI丨lii1 = this.ILil.IL1Iii;
        if (lI丨lii1 != null) {
          this.I1I = lI丨lii1;
          this.I丨L = lI丨lii1.ILil;
        } 
      } 
      paramLong = Math.min(paramLong, this.ILil.ILil - this.l丨Li1LL);
      if (paramLong <= 0L)
        return -1L; 
      this.ILil.l1IIi1丨(paramI1I, this.l丨Li1LL, paramLong);
      this.l丨Li1LL += paramLong;
      return paramLong;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I11L timeout() {
    return this.IL1Iii.timeout();
  }
}
