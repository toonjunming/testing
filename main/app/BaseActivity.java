package com.main.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
  public Matrix I1I = new Matrix();
  
  public ImageView IL1Iii;
  
  public Matrix ILil = new Matrix();
  
  public PointF Ilil = new PointF();
  
  public int I丨L = 0;
  
  public GestureDetector I丨iL;
  
  public ImageView L丨1丨1丨I;
  
  public float iI丨LLL1 = 1.0F;
  
  public PointF l丨Li1LL = new PointF();
  
  public final double I1I(MotionEvent paramMotionEvent) {
    float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
    float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
    return Math.sqrt((f1 * f1 + f2 * f2));
  }
  
  public void IL1Iii() {
    setContentView(2131492923);
    ImageView imageView = findViewById(2131296552);
    this.L丨1丨1丨I = imageView;
    this.IL1Iii = imageView;
    this.I丨iL = new GestureDetector((Context)this, (GestureDetector.OnGestureListener)new IL1Iii(this));
    Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(ILil.IL1Iii.IL1Iii(new byte[] { 69, 3, 23, 80 }, "5bc838")));
    this.L丨1丨1丨I.setImageBitmap(bitmap);
  }
  
  public final void ILil(PointF paramPointF, MotionEvent paramMotionEvent) {
    float f1 = paramMotionEvent.getX(0);
    float f4 = paramMotionEvent.getX(1);
    float f3 = paramMotionEvent.getY(0);
    float f2 = paramMotionEvent.getY(1);
    paramPointF.set((f1 + f4) / 2.0F, (f3 + f2) / 2.0F);
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if (Build.VERSION.SDK_INT >= 19)
      getWindow().setFlags(67108864, 67108864); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    Matrix matrix;
    this.I丨iL.onTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    if (i != 0) {
      if (i != 1)
        if (i != 2) {
          if (i != 3)
            if (i != 5) {
              if (i != 6)
                return true; 
            } else {
              float f = (float)I1I(paramMotionEvent);
              this.iI丨LLL1 = f;
              if (f > 10.0F) {
                this.I1I.set(this.ILil);
                ILil(this.l丨Li1LL, paramMotionEvent);
                this.I丨L = 2;
              } 
              return true;
            }  
        } else {
          i = this.I丨L;
          if (i == 1) {
            float f4 = paramMotionEvent.getX();
            float f2 = this.Ilil.x;
            float f1 = paramMotionEvent.getY();
            float f3 = this.Ilil.y;
            this.ILil.postTranslate(f4 - f2, f1 - f3);
            this.IL1Iii.setImageMatrix(this.ILil);
            this.Ilil.set(paramMotionEvent.getX(), paramMotionEvent.getY());
          } else if (i == 2) {
            float f = (float)I1I(paramMotionEvent);
            if (f > 10.0F) {
              this.ILil.set(this.I1I);
              float f1 = f / this.iI丨LLL1;
              matrix = this.ILil;
              PointF pointF = this.l丨Li1LL;
              matrix.postScale(f1, f1, pointF.x, pointF.y);
              this.IL1Iii.setImageMatrix(this.ILil);
              this.iI丨LLL1 = f;
            } 
          } 
          return true;
        }  
      this.I丨L = 0;
    } else {
      this.ILil.set(this.IL1Iii.getImageMatrix());
      this.I1I.set(this.ILil);
      this.Ilil.set(matrix.getX(), matrix.getY());
      this.I丨L = 1;
    } 
    return true;
  }
  
  public class IL1Iii extends GestureDetector.SimpleOnGestureListener {
    public IL1Iii(BaseActivity this$0) {}
    
    public boolean onDown(MotionEvent param1MotionEvent) {
      return true;
    }
    
    public boolean onScroll(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
      return super.onScroll(param1MotionEvent1, param1MotionEvent2, param1Float1, param1Float2);
    }
  }
}
