package com.main.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatImageView;

public class ImageViewEx extends AppCompatImageView {
  public Bitmap I1I;
  
  public float IL1Iii;
  
  public float ILil;
  
  public float Ilil;
  
  public float I丨L;
  
  public float iI丨LLL1;
  
  public float l丨Li1LL;
  
  public ImageViewEx(Context paramContext) {
    super(paramContext);
    IL1Iii();
  }
  
  public ImageViewEx(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    IL1Iii();
  }
  
  public ImageViewEx(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    IL1Iii();
  }
  
  public final void IL1Iii() {}
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.I1I != null) {
      paramCanvas.save();
      paramCanvas.translate(this.l丨Li1LL + getX(), this.iI丨LLL1 + getY());
      paramCanvas.drawBitmap(this.I1I, 0.0F, 0.0F, null);
      paramCanvas.restore();
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    if (i != 0) {
      if (i != 2)
        return false; 
      float f2 = paramMotionEvent.getRawX();
      float f3 = this.IL1Iii;
      float f4 = paramMotionEvent.getRawY();
      float f1 = this.ILil;
      setX(getX() + f2 - f3);
      setY(getY() + f4 - f1);
      this.IL1Iii = paramMotionEvent.getRawX();
      this.ILil = paramMotionEvent.getRawY();
    } else {
      getX();
      paramMotionEvent.getRawX();
      getY();
      paramMotionEvent.getRawY();
      this.IL1Iii = paramMotionEvent.getRawX();
      this.ILil = paramMotionEvent.getRawY();
    } 
    return true;
  }
  
  public void setImageBitmap(Bitmap paramBitmap) {
    this.I1I = paramBitmap;
    this.I丨L = paramBitmap.getWidth();
    this.Ilil = paramBitmap.getHeight();
    this.l丨Li1LL = (getWidth() - this.I丨L) / 2.0F;
    this.iI丨LLL1 = (getHeight() - this.Ilil) / 2.0F;
    super.setImageBitmap(paramBitmap);
  }
}
