package com.maning.mndialoglibrary.view;

import ILil.Ilil.IL1Iii.l丨Li1LL;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class MNHudCircularProgressBar extends View {
  public float I1I = 10.0F;
  
  public float IL1Iii = 0.0F;
  
  public Paint ILL;
  
  public float ILil = 0.0F;
  
  public int Ilil = -16777216;
  
  public float I丨L = 10.0F;
  
  public int I丨iL = -90;
  
  public RectF L丨1丨1丨I;
  
  public long iI丨LLL1 = 300L;
  
  public int l丨Li1LL = -7829368;
  
  public Paint 丨il;
  
  public MNHudCircularProgressBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    ILil(paramContext, paramAttributeSet);
  }
  
  public void I1I(float paramFloat, boolean paramBoolean) {
    float f = 100.0F;
    if (paramFloat <= 100.0F)
      f = paramFloat; 
    this.IL1Iii = f;
    if (paramBoolean) {
      Ilil();
      this.ILil = paramFloat;
    } else {
      invalidate();
    } 
  }
  
  public final void ILil(Context paramContext, AttributeSet paramAttributeSet) {
    this.L丨1丨1丨I = new RectF();
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, l丨Li1LL.MNHudCircularProgressBar, 0, 0);
    try {
      this.IL1Iii = typedArray.getFloat(l丨Li1LL.MNHudCircularProgressBar_mn_progress, this.IL1Iii);
      this.I1I = typedArray.getDimension(l丨Li1LL.MNHudCircularProgressBar_mn_progressbar_width, this.I1I);
      this.I丨L = typedArray.getDimension(l丨Li1LL.MNHudCircularProgressBar_mn_background_progressbar_width, this.I丨L);
      this.Ilil = typedArray.getInt(l丨Li1LL.MNHudCircularProgressBar_mn_progressbar_color, this.Ilil);
      this.l丨Li1LL = typedArray.getInt(l丨Li1LL.MNHudCircularProgressBar_mn_background_progressbar_color, this.l丨Li1LL);
      typedArray.recycle();
      Paint paint = new Paint(1);
      this.丨il = paint;
      paint.setColor(this.l丨Li1LL);
      this.丨il.setStyle(Paint.Style.STROKE);
      this.丨il.setStrokeWidth(this.I丨L);
      paint = new Paint(1);
      this.ILL = paint;
      paint.setColor(this.Ilil);
      this.ILL.setStyle(Paint.Style.STROKE);
      return;
    } finally {
      typedArray.recycle();
    } 
  }
  
  public void Ilil() {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { this.ILil, this.IL1Iii });
    valueAnimator.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
    valueAnimator.setDuration(this.iI丨LLL1);
    valueAnimator.addUpdateListener(new IL1Iii(this));
    valueAnimator.start();
  }
  
  public void I丨L(float paramFloat, int paramInt) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", new float[] { paramFloat });
    objectAnimator.setDuration(paramInt);
    objectAnimator.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
    objectAnimator.start();
  }
  
  public int getBackgroundColor() {
    return this.l丨Li1LL;
  }
  
  public float getBackgroundProgressBarWidth() {
    return this.I丨L;
  }
  
  public int getColor() {
    return this.Ilil;
  }
  
  public float getProgress() {
    return this.IL1Iii;
  }
  
  public float getProgressBarWidth() {
    return this.I1I;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    paramCanvas.drawOval(this.L丨1丨1丨I, this.丨il);
    float f = this.IL1Iii * 360.0F / 100.0F;
    paramCanvas.drawArc(this.L丨1丨1丨I, this.I丨iL, f, false, this.ILL);
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    paramInt2 = View.getDefaultSize(getSuggestedMinimumHeight(), paramInt2);
    paramInt1 = Math.min(View.getDefaultSize(getSuggestedMinimumWidth(), paramInt1), paramInt2);
    setMeasuredDimension(paramInt1, paramInt1);
    float f1 = this.I1I;
    float f2 = this.I丨L;
    if (f1 <= f2)
      f1 = f2; 
    RectF rectF = this.L丨1丨1丨I;
    f2 = f1 / 2.0F;
    f1 = 0.0F + f2;
    f2 = paramInt1 - f2;
    rectF.set(f1, f1, f2, f2);
  }
  
  public void setBackgroundColor(int paramInt) {
    this.l丨Li1LL = paramInt;
    this.丨il.setColor(paramInt);
    invalidate();
    requestLayout();
  }
  
  public void setBackgroundProgressBarWidth(float paramFloat) {
    this.I丨L = paramFloat;
    this.丨il.setStrokeWidth(paramFloat);
    requestLayout();
    invalidate();
  }
  
  public void setColor(int paramInt) {
    this.Ilil = paramInt;
    this.ILL.setColor(paramInt);
    invalidate();
    requestLayout();
  }
  
  public void setProgress(float paramFloat) {
    I1I(paramFloat, true);
  }
  
  public void setProgressBarWidth(float paramFloat) {
    this.I1I = paramFloat;
    this.ILL.setStrokeWidth(paramFloat);
    requestLayout();
    invalidate();
  }
  
  public void setProgressWithAnimation(float paramFloat) {
    I丨L(paramFloat, 1500);
  }
  
  public class IL1Iii implements ValueAnimator.AnimatorUpdateListener {
    public final MNHudCircularProgressBar IL1Iii;
    
    public IL1Iii(MNHudCircularProgressBar this$0) {}
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      MNHudCircularProgressBar.IL1Iii(this.IL1Iii, ((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
      this.IL1Iii.postInvalidate();
    }
  }
}
