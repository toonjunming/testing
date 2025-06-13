package com.maning.mndialoglibrary.view;

import ILil.Ilil.IL1Iii.l丨Li1LL;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class MNHudProgressWheel extends View {
  public ILil I11li1;
  
  public int I1I = 4;
  
  public int IL1Iii = 28;
  
  public int ILL = 16777215;
  
  public int ILil = 4;
  
  public RectF IL丨丨l = new RectF();
  
  public double Ilil = 0.0D;
  
  public boolean I丨L = false;
  
  public boolean I丨iL = true;
  
  public float Lil = 230.0F;
  
  public long LlLI1 = 0L;
  
  public Paint Ll丨1 = new Paint();
  
  public long L丨1丨1丨I = 0L;
  
  public float iIi1 = 0.0F;
  
  public boolean iIlLiL = false;
  
  public float iI丨LLL1 = 0.0F;
  
  public Paint lIi丨I = new Paint();
  
  public float lI丨lii = 0.0F;
  
  public boolean ll丨L1ii;
  
  public double l丨Li1LL = 460.0D;
  
  public int 丨il = -1442840576;
  
  public boolean 丨lL;
  
  public MNHudProgressWheel(Context paramContext) {
    super(paramContext);
    I丨L();
  }
  
  public MNHudProgressWheel(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    IL1Iii(paramContext.obtainStyledAttributes(paramAttributeSet, l丨Li1LL.MNHudProgressWheel));
    I丨L();
  }
  
  public final void I1I(float paramFloat) {
    ILil iLil = this.I11li1;
    if (iLil != null)
      iLil.IL1Iii(paramFloat); 
  }
  
  public final void IL1Iii(TypedArray paramTypedArray) {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    this.ILil = (int)TypedValue.applyDimension(1, this.ILil, displayMetrics);
    this.I1I = (int)TypedValue.applyDimension(1, this.I1I, displayMetrics);
    int i = (int)TypedValue.applyDimension(1, this.IL1Iii, displayMetrics);
    this.IL1Iii = i;
    this.IL1Iii = (int)paramTypedArray.getDimension(l丨Li1LL.MNHudProgressWheel_mn_circleRadius, i);
    this.I丨L = paramTypedArray.getBoolean(l丨Li1LL.MNHudProgressWheel_mn_fillRadius, false);
    this.ILil = (int)paramTypedArray.getDimension(l丨Li1LL.MNHudProgressWheel_mn_barWidth, this.ILil);
    this.I1I = (int)paramTypedArray.getDimension(l丨Li1LL.MNHudProgressWheel_mn_rimWidth, this.I1I);
    this.Lil = paramTypedArray.getFloat(l丨Li1LL.MNHudProgressWheel_mn_spinSpeed, this.Lil / 360.0F) * 360.0F;
    this.l丨Li1LL = paramTypedArray.getInt(l丨Li1LL.MNHudProgressWheel_mn_barSpinCycleTime, (int)this.l丨Li1LL);
    this.丨il = paramTypedArray.getColor(l丨Li1LL.MNHudProgressWheel_mn_barColor, this.丨il);
    this.ILL = paramTypedArray.getColor(l丨Li1LL.MNHudProgressWheel_mn_rimColor, this.ILL);
    this.ll丨L1ii = paramTypedArray.getBoolean(l丨Li1LL.MNHudProgressWheel_mn_linearProgress, false);
    if (paramTypedArray.getBoolean(l丨Li1LL.MNHudProgressWheel_mn_progressIndeterminate, false))
      iI丨LLL1(); 
    paramTypedArray.recycle();
  }
  
  public final void ILil() {
    if (this.I11li1 != null) {
      float f = Math.round(this.lI丨lii * 100.0F / 360.0F) / 100.0F;
      this.I11li1.IL1Iii(f);
    } 
  }
  
  public final void Ilil(int paramInt1, int paramInt2) {
    int i = getPaddingTop();
    int j = getPaddingBottom();
    int k = getPaddingLeft();
    int m = getPaddingRight();
    if (!this.I丨L) {
      m = paramInt1 - k - m;
      paramInt1 = Math.min(Math.min(m, paramInt2 - j - i), this.IL1Iii * 2 - this.ILil * 2);
      k = (m - paramInt1) / 2 + k;
      paramInt2 = (paramInt2 - i - j - paramInt1) / 2 + i;
      i = this.ILil;
      this.IL丨丨l = new RectF((k + i), (paramInt2 + i), (k + paramInt1 - i), (paramInt2 + paramInt1 - i));
    } else {
      int n = this.ILil;
      this.IL丨丨l = new RectF((k + n), (i + n), (paramInt1 - m - n), (paramInt2 - j - n));
    } 
  }
  
  @TargetApi(17)
  public final void I丨L() {
    float f;
    boolean bool;
    if (Build.VERSION.SDK_INT >= 17) {
      f = Settings.Global.getFloat(getContext().getContentResolver(), "animator_duration_scale", 1.0F);
    } else {
      f = Settings.System.getFloat(getContext().getContentResolver(), "animator_duration_scale", 1.0F);
    } 
    if (f != 0.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    this.丨lL = bool;
  }
  
  public final void I丨iL(long paramLong) {
    long l = this.L丨1丨1丨I;
    if (l >= 200L) {
      double d2 = this.Ilil + paramLong;
      this.Ilil = d2;
      double d1 = this.l丨Li1LL;
      if (d2 > d1) {
        this.Ilil = d2 - d1;
        this.L丨1丨1丨I = 0L;
        this.I丨iL ^= 0x1;
      } 
      float f = (float)Math.cos((this.Ilil / d1 + 1.0D) * Math.PI) / 2.0F + 0.5F;
      if (this.I丨iL) {
        this.iI丨LLL1 = f * 254.0F;
      } else {
        f = (1.0F - f) * 254.0F;
        this.lI丨lii += this.iI丨LLL1 - f;
        this.iI丨LLL1 = f;
      } 
    } else {
      this.L丨1丨1丨I = l + paramLong;
    } 
  }
  
  public int getBarColor() {
    return this.丨il;
  }
  
  public int getBarWidth() {
    return this.ILil;
  }
  
  public int getCircleRadius() {
    return this.IL1Iii;
  }
  
  public float getProgress() {
    float f;
    if (this.iIlLiL) {
      f = -1.0F;
    } else {
      f = this.lI丨lii / 360.0F;
    } 
    return f;
  }
  
  public int getRimColor() {
    return this.ILL;
  }
  
  public int getRimWidth() {
    return this.I1I;
  }
  
  public float getSpinSpeed() {
    return this.Lil / 360.0F;
  }
  
  public void iI丨LLL1() {
    this.LlLI1 = SystemClock.uptimeMillis();
    this.iIlLiL = true;
    invalidate();
  }
  
  public final void l丨Li1LL() {
    this.Ll丨1.setColor(this.丨il);
    this.Ll丨1.setAntiAlias(true);
    this.Ll丨1.setStyle(Paint.Style.STROKE);
    this.Ll丨1.setStrokeWidth(this.ILil);
    this.lIi丨I.setColor(this.ILL);
    this.lIi丨I.setAntiAlias(true);
    this.lIi丨I.setStyle(Paint.Style.STROKE);
    this.lIi丨I.setStrokeWidth(this.I1I);
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    paramCanvas.drawArc(this.IL丨丨l, 360.0F, 360.0F, false, this.lIi丨I);
    if (!this.丨lL)
      return; 
    boolean bool = this.iIlLiL;
    float f = 0.0F;
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool) {
      long l = SystemClock.uptimeMillis() - this.LlLI1;
      float f1 = (float)l * this.Lil / 1000.0F;
      I丨iL(l);
      f1 = this.lI丨lii + f1;
      this.lI丨lii = f1;
      if (f1 > 360.0F) {
        this.lI丨lii = f1 - 360.0F;
        I1I(-1.0F);
      } 
      this.LlLI1 = SystemClock.uptimeMillis();
      f = this.lI丨lii;
      f1 = this.iI丨LLL1;
      if (isInEditMode()) {
        f = 0.0F;
        f1 = 135.0F;
      } else {
        f -= 90.0F;
        f1 += 16.0F;
      } 
      paramCanvas.drawArc(this.IL丨丨l, f, f1, false, this.Ll丨1);
    } else {
      float f2 = this.lI丨lii;
      if (f2 != this.iIi1) {
        float f3 = (float)(SystemClock.uptimeMillis() - this.LlLI1) / 1000.0F;
        float f4 = this.Lil;
        this.lI丨lii = Math.min(this.lI丨lii + f3 * f4, this.iIi1);
        this.LlLI1 = SystemClock.uptimeMillis();
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      if (f2 != this.lI丨lii)
        ILil(); 
      f2 = this.lI丨lii;
      float f1 = f2;
      if (!this.ll丨L1ii) {
        f = (float)(1.0D - Math.pow((1.0F - f2 / 360.0F), 4.0F));
        f1 = (float)(1.0D - Math.pow((1.0F - this.lI丨lii / 360.0F), 2.0F));
        f *= 360.0F;
        f1 *= 360.0F;
      } 
      if (isInEditMode())
        f1 = 360.0F; 
      paramCanvas.drawArc(this.IL丨丨l, f - 90.0F, f1, false, this.Ll丨1);
    } 
    if (bool1)
      invalidate(); 
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    int k = this.IL1Iii + getPaddingLeft() + getPaddingRight();
    int i = this.IL1Iii + getPaddingTop() + getPaddingBottom();
    int i1 = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt1);
    int n = View.MeasureSpec.getMode(paramInt2);
    int j = View.MeasureSpec.getSize(paramInt2);
    if (i1 == 1073741824) {
      paramInt1 = m;
    } else {
      paramInt1 = k;
      if (i1 == Integer.MIN_VALUE)
        paramInt1 = Math.min(k, m); 
    } 
    if (n == 1073741824 || i1 == 1073741824) {
      paramInt2 = j;
    } else {
      paramInt2 = i;
      if (n == Integer.MIN_VALUE)
        paramInt2 = Math.min(i, j); 
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof WheelSavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    WheelSavedState wheelSavedState = (WheelSavedState)paramParcelable;
    super.onRestoreInstanceState(wheelSavedState.getSuperState());
    this.lI丨lii = wheelSavedState.IL1Iii;
    this.iIi1 = wheelSavedState.ILil;
    this.iIlLiL = wheelSavedState.I1I;
    this.Lil = wheelSavedState.I丨L;
    this.ILil = wheelSavedState.Ilil;
    this.丨il = wheelSavedState.l丨Li1LL;
    this.I1I = wheelSavedState.iI丨LLL1;
    this.ILL = wheelSavedState.I丨iL;
    this.IL1Iii = wheelSavedState.L丨1丨1丨I;
    this.ll丨L1ii = wheelSavedState.丨il;
    this.I丨L = wheelSavedState.ILL;
    this.LlLI1 = SystemClock.uptimeMillis();
  }
  
  public Parcelable onSaveInstanceState() {
    WheelSavedState wheelSavedState = new WheelSavedState(super.onSaveInstanceState());
    wheelSavedState.IL1Iii = this.lI丨lii;
    wheelSavedState.ILil = this.iIi1;
    wheelSavedState.I1I = this.iIlLiL;
    wheelSavedState.I丨L = this.Lil;
    wheelSavedState.Ilil = this.ILil;
    wheelSavedState.l丨Li1LL = this.丨il;
    wheelSavedState.iI丨LLL1 = this.I1I;
    wheelSavedState.I丨iL = this.ILL;
    wheelSavedState.L丨1丨1丨I = this.IL1Iii;
    wheelSavedState.丨il = this.ll丨L1ii;
    wheelSavedState.ILL = this.I丨L;
    return (Parcelable)wheelSavedState;
  }
  
  public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    Ilil(paramInt1, paramInt2);
    l丨Li1LL();
    invalidate();
  }
  
  public void onVisibilityChanged(View paramView, int paramInt) {
    super.onVisibilityChanged(paramView, paramInt);
    if (paramInt == 0)
      this.LlLI1 = SystemClock.uptimeMillis(); 
  }
  
  public void setBarColor(int paramInt) {
    this.丨il = paramInt;
    l丨Li1LL();
    if (!this.iIlLiL)
      invalidate(); 
  }
  
  public void setBarWidth(int paramInt) {
    this.ILil = paramInt;
    if (!this.iIlLiL)
      invalidate(); 
  }
  
  public void setCallback(ILil paramILil) {
    this.I11li1 = paramILil;
    if (!this.iIlLiL)
      ILil(); 
  }
  
  public void setCircleRadius(int paramInt) {
    this.IL1Iii = paramInt;
    if (!this.iIlLiL)
      invalidate(); 
  }
  
  public void setInstantProgress(float paramFloat) {
    float f;
    if (this.iIlLiL) {
      this.lI丨lii = 0.0F;
      this.iIlLiL = false;
    } 
    if (paramFloat > 1.0F) {
      f = paramFloat - 1.0F;
    } else {
      f = paramFloat;
      if (paramFloat < 0.0F)
        f = 0.0F; 
    } 
    if (f == this.iIi1)
      return; 
    paramFloat = Math.min(f * 360.0F, 360.0F);
    this.iIi1 = paramFloat;
    this.lI丨lii = paramFloat;
    this.LlLI1 = SystemClock.uptimeMillis();
    invalidate();
  }
  
  public void setLinearProgress(boolean paramBoolean) {
    this.ll丨L1ii = paramBoolean;
    if (!this.iIlLiL)
      invalidate(); 
  }
  
  public void setProgress(float paramFloat) {
    float f;
    if (this.iIlLiL) {
      this.lI丨lii = 0.0F;
      this.iIlLiL = false;
      ILil();
    } 
    if (paramFloat > 1.0F) {
      f = paramFloat - 1.0F;
    } else {
      f = paramFloat;
      if (paramFloat < 0.0F)
        f = 0.0F; 
    } 
    paramFloat = this.iIi1;
    if (f == paramFloat)
      return; 
    if (this.lI丨lii == paramFloat)
      this.LlLI1 = SystemClock.uptimeMillis(); 
    this.iIi1 = Math.min(f * 360.0F, 360.0F);
    invalidate();
  }
  
  public void setRimColor(int paramInt) {
    this.ILL = paramInt;
    l丨Li1LL();
    if (!this.iIlLiL)
      invalidate(); 
  }
  
  public void setRimWidth(int paramInt) {
    this.I1I = paramInt;
    if (!this.iIlLiL)
      invalidate(); 
  }
  
  public void setSpinSpeed(float paramFloat) {
    this.Lil = paramFloat * 360.0F;
  }
  
  public static interface ILil {
    void IL1Iii(float param1Float);
  }
  
  public static class WheelSavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<WheelSavedState> CREATOR = new IL1Iii();
    
    public boolean I1I;
    
    public float IL1Iii;
    
    public boolean ILL;
    
    public float ILil;
    
    public int Ilil;
    
    public float I丨L;
    
    public int I丨iL;
    
    public int L丨1丨1丨I;
    
    public int iI丨LLL1;
    
    public int l丨Li1LL;
    
    public boolean 丨il;
    
    public WheelSavedState(Parcel param1Parcel) {
      super(param1Parcel);
      boolean bool1;
      this.IL1Iii = param1Parcel.readFloat();
      this.ILil = param1Parcel.readFloat();
      byte b = param1Parcel.readByte();
      boolean bool2 = true;
      if (b != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.I1I = bool1;
      this.I丨L = param1Parcel.readFloat();
      this.Ilil = param1Parcel.readInt();
      this.l丨Li1LL = param1Parcel.readInt();
      this.iI丨LLL1 = param1Parcel.readInt();
      this.I丨iL = param1Parcel.readInt();
      this.L丨1丨1丨I = param1Parcel.readInt();
      if (param1Parcel.readByte() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.丨il = bool1;
      if (param1Parcel.readByte() != 0) {
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      this.ILL = bool1;
    }
    
    public WheelSavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeFloat(this.IL1Iii);
      param1Parcel.writeFloat(this.ILil);
      param1Parcel.writeByte((byte)this.I1I);
      param1Parcel.writeFloat(this.I丨L);
      param1Parcel.writeInt(this.Ilil);
      param1Parcel.writeInt(this.l丨Li1LL);
      param1Parcel.writeInt(this.iI丨LLL1);
      param1Parcel.writeInt(this.I丨iL);
      param1Parcel.writeInt(this.L丨1丨1丨I);
      param1Parcel.writeByte((byte)this.丨il);
      param1Parcel.writeByte((byte)this.ILL);
    }
    
    public static final class IL1Iii implements Parcelable.Creator<WheelSavedState> {
      public MNHudProgressWheel.WheelSavedState IL1Iii(Parcel param2Parcel) {
        return new MNHudProgressWheel.WheelSavedState(param2Parcel, null);
      }
      
      public MNHudProgressWheel.WheelSavedState[] ILil(int param2Int) {
        return new MNHudProgressWheel.WheelSavedState[param2Int];
      }
    }
  }
  
  public static final class IL1Iii implements Parcelable.Creator<WheelSavedState> {
    public MNHudProgressWheel.WheelSavedState IL1Iii(Parcel param1Parcel) {
      return new MNHudProgressWheel.WheelSavedState(param1Parcel, null);
    }
    
    public MNHudProgressWheel.WheelSavedState[] ILil(int param1Int) {
      return new MNHudProgressWheel.WheelSavedState[param1Int];
    }
  }
}
