package I丨L;

import javax.annotation.Nullable;

public final class lI丨lii {
  public int I1I;
  
  public final byte[] IL1Iii = new byte[8192];
  
  public int ILil;
  
  public boolean Ilil;
  
  public boolean I丨L;
  
  public lI丨lii iI丨LLL1;
  
  public lI丨lii l丨Li1LL;
  
  public lI丨lii() {
    this.Ilil = true;
    this.I丨L = false;
  }
  
  public lI丨lii(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    this.ILil = paramInt1;
    this.I1I = paramInt2;
    this.I丨L = paramBoolean1;
    this.Ilil = paramBoolean2;
  }
  
  public final lI丨lii I1I(lI丨lii paramlI丨lii) {
    paramlI丨lii.iI丨LLL1 = this;
    paramlI丨lii.l丨Li1LL = this.l丨Li1LL;
    this.l丨Li1LL.iI丨LLL1 = paramlI丨lii;
    this.l丨Li1LL = paramlI丨lii;
    return paramlI丨lii;
  }
  
  public final void IL1Iii() {
    lI丨lii lI丨lii1 = this.iI丨LLL1;
    if (lI丨lii1 != this) {
      int i;
      if (!lI丨lii1.Ilil)
        return; 
      int k = this.I1I - this.ILil;
      int j = lI丨lii1.I1I;
      if (lI丨lii1.I丨L) {
        i = 0;
      } else {
        i = lI丨lii1.ILil;
      } 
      if (k > 8192 - j + i)
        return; 
      iI丨LLL1(lI丨lii1, k);
      ILil();
      iIi1.IL1Iii(this);
      return;
    } 
    throw new IllegalStateException();
  }
  
  @Nullable
  public final lI丨lii ILil() {
    lI丨lii lI丨lii1;
    lI丨lii lI丨lii2 = this.l丨Li1LL;
    if (lI丨lii2 != this) {
      lI丨lii1 = lI丨lii2;
    } else {
      lI丨lii1 = null;
    } 
    lI丨lii lI丨lii3 = this.iI丨LLL1;
    lI丨lii3.l丨Li1LL = lI丨lii2;
    this.l丨Li1LL.iI丨LLL1 = lI丨lii3;
    this.l丨Li1LL = null;
    this.iI丨LLL1 = null;
    return lI丨lii1;
  }
  
  public final lI丨lii Ilil(int paramInt) {
    if (paramInt > 0 && paramInt <= this.I1I - this.ILil) {
      lI丨lii lI丨lii1;
      if (paramInt >= 1024) {
        lI丨lii1 = I丨L();
      } else {
        lI丨lii1 = iIi1.ILil();
        System.arraycopy(this.IL1Iii, this.ILil, lI丨lii1.IL1Iii, 0, paramInt);
      } 
      lI丨lii1.I1I = lI丨lii1.ILil + paramInt;
      this.ILil += paramInt;
      this.iI丨LLL1.I1I(lI丨lii1);
      return lI丨lii1;
    } 
    throw new IllegalArgumentException();
  }
  
  public final lI丨lii I丨L() {
    this.I丨L = true;
    return new lI丨lii(this.IL1Iii, this.ILil, this.I1I, true, false);
  }
  
  public final void iI丨LLL1(lI丨lii paramlI丨lii, int paramInt) {
    if (paramlI丨lii.Ilil) {
      int i = paramlI丨lii.I1I;
      if (i + paramInt > 8192)
        if (!paramlI丨lii.I丨L) {
          int j = paramlI丨lii.ILil;
          if (i + paramInt - j <= 8192) {
            byte[] arrayOfByte = paramlI丨lii.IL1Iii;
            System.arraycopy(arrayOfByte, j, arrayOfByte, 0, i - j);
            paramlI丨lii.I1I -= paramlI丨lii.ILil;
            paramlI丨lii.ILil = 0;
          } else {
            throw new IllegalArgumentException();
          } 
        } else {
          throw new IllegalArgumentException();
        }  
      System.arraycopy(this.IL1Iii, this.ILil, paramlI丨lii.IL1Iii, paramlI丨lii.I1I, paramInt);
      paramlI丨lii.I1I += paramInt;
      this.ILil += paramInt;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public final lI丨lii l丨Li1LL() {
    return new lI丨lii((byte[])this.IL1Iii.clone(), this.ILil, this.I1I, false, true);
  }
}
