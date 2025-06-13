package com.main.widget;

import ILil.IL1Iii;
import ILil.l丨Li1LL.I丨L.IL1Iii;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

public class CircleButton extends ImageView {
  public int I1I;
  
  public int IL1Iii;
  
  public String ILL = IL1Iii.IL1Iii(new byte[] { 2, 3, 5, 5, 64, 13, 18 }, "ffcd5a");
  
  public int ILil;
  
  public int IL丨丨l = 12;
  
  public Paint Ilil;
  
  public int I丨L;
  
  public float I丨iL;
  
  public int Lil;
  
  public Drawable LlLI1;
  
  public int Ll丨1 = -16777216;
  
  public int L丨1丨1丨I;
  
  public Paint iI丨LLL1;
  
  public Typeface lIi丨I = Typeface.DEFAULT;
  
  public ObjectAnimator lI丨lii;
  
  public Context ll丨L1ii;
  
  public Paint l丨Li1LL;
  
  public int 丨il = -16777216;
  
  public CircleButton(Context paramContext) {
    super(paramContext);
    I1I(paramContext, null);
  }
  
  public CircleButton(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    I1I(paramContext, paramAttributeSet);
  }
  
  public CircleButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    I1I(paramContext, paramAttributeSet);
  }
  
  public final void I1I(Context paramContext, AttributeSet paramAttributeSet) {
    setFocusable(true);
    setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    this.ll丨L1ii = paramContext;
    setClickable(true);
    Paint paint = new Paint(1);
    this.Ilil = paint;
    paint.setStyle(Paint.Style.FILL);
    paint = new Paint(1);
    this.l丨Li1LL = paint;
    paint.setStyle(Paint.Style.STROKE);
    this.iI丨LLL1 = new Paint(1);
    this.L丨1丨1丨I = (int)TypedValue.applyDimension(1, 5.0F, getResources().getDisplayMetrics());
    int i = -16777216;
    if (paramAttributeSet != null) {
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, IL1Iii.CircleButton);
      i = typedArray.getColor(0, -16777216);
      this.L丨1丨1丨I = (int)typedArray.getDimension(1, this.L丨1丨1丨I);
      this.ILL = typedArray.getString(2);
      this.IL丨丨l = (int)typedArray.getDimension(4, this.IL丨丨l);
      this.Ll丨1 = typedArray.getColor(3, this.Ll丨1);
      typedArray.recycle();
    } 
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    String str = IL1Iii.IL1Iii(new byte[] { 101, 43, 115, 117, 49, 99, 100, 36, 121, 116 }, "0e70c0");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 17, 7, 74, 67, 101, 89, 31, 7, 8 }, "eb2760"));
    stringBuilder.append(this.IL丨丨l);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 22, 17, 80, 84, 94, 0, 12 }, "6b352e"));
    stringBuilder.append(f);
    Log.e(str, stringBuilder.toString());
    set_bg_color(i);
    I丨L();
    this.l丨Li1LL.setStrokeWidth(this.L丨1丨1丨I);
    i = getResources().getInteger(17694720);
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, IL1Iii.IL1Iii(new byte[] { 
            83, 94, 90, 95, 86, 64, 91, 95, 93, 98, 
            69, 91, 85, 66, 86, 65, 68 }, "203274"), new float[] { 0.0F, 0.0F });
    this.lI丨lii = objectAnimator;
    objectAnimator.setDuration(i);
  }
  
  public final int IL1Iii(int paramInt1, int paramInt2) {
    return Color.argb(Math.min(255, Color.alpha(paramInt1)), Math.min(255, Color.red(paramInt1) + paramInt2), Math.min(255, Color.green(paramInt1) + paramInt2), Math.min(255, Color.blue(paramInt1) + paramInt2));
  }
  
  public final void ILil() {
    this.lI丨lii.setFloatValues(new float[] { this.L丨1丨1丨I, 0.0F });
    this.lI丨lii.start();
  }
  
  public final void Ilil() {
    this.lI丨lii.setFloatValues(new float[] { this.I丨iL, this.L丨1丨1丨I });
    this.lI丨lii.start();
  }
  
  public final void I丨L() {
    this.iI丨LLL1.setTextSize(this.IL丨丨l);
    this.iI丨LLL1.setTypeface(this.lIi丨I);
    this.iI丨LLL1.setTextAlign(Paint.Align.CENTER);
    this.iI丨LLL1.setColor(this.Ll丨1);
    invalidate();
  }
  
  public float getAnimationProgress() {
    return this.I丨iL;
  }
  
  public void onDraw(Canvas paramCanvas) {
    int i = this.I1I;
    int j = this.L丨1丨1丨I;
    this.I丨L = i - j - j / 2;
    this.l丨Li1LL.setStrokeWidth(j);
    paramCanvas.drawCircle(this.IL1Iii, this.ILil, this.I丨L + this.I丨iL, this.l丨Li1LL);
    paramCanvas.drawCircle(this.IL1Iii, this.ILil, (this.I1I - this.L丨1丨1丨I), this.Ilil);
    if (this.ILL != null) {
      Paint.FontMetrics fontMetrics = this.iI丨LLL1.getFontMetrics();
      j = (int)(fontMetrics.top - fontMetrics.bottom) / 2;
      i = this.ILil;
      j /= 2;
      paramCanvas.drawText(this.ILL, this.IL1Iii, (i - j), this.iI丨LLL1);
      String str = IL1Iii.IL1Iii(new byte[] { 101, 87, 21, 66 }, "12f6ce");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 21, 84, 73, 76, 69, 92, 27, 84, 11 }, "a11865"));
      stringBuilder.append(this.IL丨丨l);
      Log.e(str, stringBuilder.toString());
    } else {
      Drawable drawable = this.LlLI1;
      if (drawable != null)
        set_fg_drawable(drawable); 
    } 
    super.onDraw(paramCanvas);
  }
  
  public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.IL1Iii = paramInt1 / 2;
    this.ILil = paramInt2 / 2;
    paramInt1 = Math.min(paramInt1, paramInt2) / 2;
    this.I1I = paramInt1;
    paramInt2 = this.L丨1丨1丨I;
    this.I丨L = paramInt1 - paramInt2 - paramInt2 / 2;
  }
  
  public void setAnimationProgress(float paramFloat) {
    this.I丨iL = paramFloat;
    invalidate();
  }
  
  public void setPressed(boolean paramBoolean) {
    super.setPressed(paramBoolean);
    Paint paint = this.Ilil;
    if (paint != null) {
      int i;
      if (paramBoolean) {
        i = this.Lil;
      } else {
        i = this.丨il;
      } 
      paint.setColor(i);
    } 
    if (paramBoolean) {
      Ilil();
    } else {
      ILil();
    } 
  }
  
  public void set_bg_color(int paramInt) {
    this.丨il = paramInt;
    this.Lil = IL1Iii(paramInt, 10);
    this.Ilil.setColor(this.丨il);
    this.l丨Li1LL.setColor(this.丨il);
    this.l丨Li1LL.setAlpha(75);
    invalidate();
  }
  
  public void set_fg_drawable(Drawable paramDrawable) {
    setImageDrawable(paramDrawable);
    invalidate();
  }
  
  public void set_pressed_ring_width(int paramInt) {
    this.L丨1丨1丨I = (int)TypedValue.applyDimension(1, paramInt, this.ll丨L1ii.getResources().getDisplayMetrics());
    invalidate();
  }
  
  public void set_text(String paramString) {
    this.ILL = paramString;
    invalidate();
  }
  
  public void set_text_color(int paramInt) {
    this.Ll丨1 = paramInt;
    this.iI丨LLL1.setColor(paramInt);
    invalidate();
  }
  
  public void set_text_size_dp(int paramInt) {
    float f = (this.ll丨L1ii.getResources().getDisplayMetrics()).density;
    paramInt = (int)(paramInt * f + 0.5F);
    this.IL丨丨l = paramInt;
    this.iI丨LLL1.setTextSize(paramInt);
    invalidate();
  }
  
  public void set_typeface(Typeface paramTypeface) {
    this.lIi丨I = paramTypeface;
    this.iI丨LLL1.setTypeface(paramTypeface);
    invalidate();
  }
}
