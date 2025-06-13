package com.main.app;

import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.I丨L.I11li1;
import ILil.I丨L.I丨L.lI丨lii;
import ILil.I丨L.I丨L.ll丨L1ii;
import Ilil.ILil.IL1Iii.L丨1丨1丨I;
import Ilil.ILil.IL1Iii.iIi1;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.greenrobot.eventbus.ThreadMode;

public class SplashActivity extends Activity {
  public boolean I1I = false;
  
  public lI丨lii IL1Iii = null;
  
  public boolean ILil = false;
  
  public Thread I丨L = null;
  
  public final int ILL(int paramInt) {
    float f = (getResources().getDisplayMetrics()).density;
    return Math.round(paramInt * f);
  }
  
  public final void IL丨丨l() {
    ILil.I丨L.I1I.I1I.Ilil((Context)this).iI丨LLL1(new ILil(this));
  }
  
  public final void Lil() {
    this.IL1Iii = new lI丨lii();
    TextView textView = (TextView)findViewById(2131296667);
    findViewById(2131296366);
    textView.setText(I11li1.IIi1II((Context)this));
    if (!isTaskRoot())
      finish(); 
  }
  
  public final void LlLI1() {
    runOnUiThread(new iI丨LLL1(this));
  }
  
  public final void Ll丨1() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield I丨L : Ljava/lang/Thread;
    //   6: ifnonnull -> 36
    //   9: new java/lang/Thread
    //   12: astore_1
    //   13: new com/main/app/SplashActivity$l丨Li1LL
    //   16: astore_2
    //   17: aload_2
    //   18: aload_0
    //   19: invokespecial <init> : (Lcom/main/app/SplashActivity;)V
    //   22: aload_1
    //   23: aload_2
    //   24: invokespecial <init> : (Ljava/lang/Runnable;)V
    //   27: aload_0
    //   28: aload_1
    //   29: putfield I丨L : Ljava/lang/Thread;
    //   32: aload_1
    //   33: invokevirtual start : ()V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	39	finally
  }
  
  public void finish() {
    super.finish();
    if (I11li1.l1i丨I丨I丨((Context)this))
      L丨1丨1丨I.I1I().ll丨L1ii(this); 
  }
  
  public final void lIi丨I() {
    I11li1.lILIlI(new I丨iL(this));
  }
  
  public final void lI丨lii() {
    Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 47, 110 }, "a609b8"), ILil.IL1Iii.IL1Iii(new byte[] { 
            16, 71, 88, 16, 65, 96, 10, 71, 81, 50, 
            89, 66, 4, 90, 87, 49, 80, 69, 21, 90, 
            90, 7 }, "c39b57"));
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 
          70, 81, 21, 76, 94, 89, 82, 71, 62, 93, 
          89, 65 }, "54a877");
    boolean bool = true;
    ll丨L1ii.I丨L((Context)this, str, true);
    if (getResources().getInteger(2131361807) != 1)
      bool = false; 
    I11li1.I1LilL(I11li1.LIlLi((Context)this, 2131296624, bool));
    I11li1.il1L丨(I11li1.LIlLi((Context)this, 2131296623, false));
    ILil.I丨L.I丨L.I1I.Ilil().I丨L();
    I11li1.LIIIiI((Context)this);
    App.ILil().Ilil();
  }
  
  public final void ll丨L1ii() {
    boolean bool;
    ILil.I丨L.IL1Iii.ILil iLil = new ILil.I丨L.IL1Iii.ILil(this);
    ll丨L1ii.I丨L((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
            68, 86, 22, 65, 92, 11, 80, 64, 61, 80, 
            91, 19 }, "73b55e"), true);
    if (getResources().getInteger(2131361807) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    I11li1.I1LilL(I11li1.LIlLi((Context)this, 2131296624, bool));
    I11li1.il1L丨(I11li1.LIlLi((Context)this, 2131296623, false));
    ILil.I丨L.I丨L.I1I.Ilil().I丨L();
    I11li1.LIIIiI((Context)this);
    iLil.I丨iL();
    this.I1I = true;
    App.ILil().I1I();
    if (Build.VERSION.SDK_INT < 23)
      finish(); 
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492974);
    if (I11li1.LIIl丨丨L((Context)this))
      Native.registerDebugService(I11li1.l丨liiI1((Context)this)); 
    Lil();
    ILil.I丨L.I丨L.I1I.Ilil().I丨L();
    if (I11li1.l1i丨I丨I丨((Context)this))
      L丨1丨1丨I.I1I().Lil(this); 
  }
  
  @iIi1(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LI丨丨l丨l paramLI丨丨l丨l) {
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 119, 106 }, "92b69c");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
            9, 87, 40, 6, 64, 21, 7, 94, 0, 38, 
            69, 3, 8, 77, 88, 93 }, "f9ec3f"));
    stringBuilder.append(paramLI丨丨l丨l.I1I());
    Log.i(str, stringBuilder.toString());
    if (paramLI丨丨l丨l.I1I() == 203) {
      String str1 = paramLI丨丨l丨l.IL1Iii();
      if (ILil.IL1Iii.IL1Iii(new byte[] { 87 }, "fb68b5").equals(str1)) {
        startActivity(new Intent((Context)this, PluginActivity.class));
        finish();
      } else {
        I11li1.LlLl111l((Context)this, getResources().getString(2131689581));
      } 
    } 
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    if (I11li1.L丨1丨1丨I(paramInt, paramArrayOfint)) {
      IL丨丨l();
    } else if (!I11li1.iIlLiL((Context)this)) {
      ILil.I丨L.I丨L.l丨Li1LL.ILil(this, ILil.IL1Iii.IL1Iii(new byte[] { 
              -118, -20, -46, -122, -70, -14, -124, -1, -26, -118, 
              -84, -12, -121, -58, -44, -117, -127, -63, -124, -21, 
              -18, -122, -65, -52, -124, -20, -19, -123, -88, -25, 
              -121, -14, -14, 79, -46, -26, -37, -121, -30, -40, 
              -48, -21, -12, -124, -45, -21, -48, -44, -45, -118, 
              -46, -48, -35, -37, -27, -123, -2, -41, -45, -22, 
              -57, -118, -38, -8, -48, -31, -57 }, "bbec5d"), ILil.IL1Iii.IL1Iii(new byte[] { 
              -126, -4, -77, -120, -1, -94, -116, -49, -114, -122, 
              -37, -100 }, "da0af2"), new IL1Iii(this));
    } else {
      IL丨丨l();
    } 
  }
  
  public void onResume() {
    super.onResume();
    if (this.I1I) {
      finish();
      return;
    } 
    boolean bool = this.IL1Iii.I1I((Context)this);
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 45, 61 }, "ce3c00");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
            93, 22, 113, 82, 80, 93, 83, 23, 92, 70, 
            93, 82, 103, 17, 82, 65, 71, 119, 88, 9, 
            92, 68, 86, 82, 9, 88, 14 }, "4e3336"));
    stringBuilder.append(bool);
    Log.i(str, stringBuilder.toString());
    if (bool) {
      if (!this.ILil) {
        this.ILil = true;
        if (I11li1.l1ILIl(this))
          IL丨丨l(); 
      } 
    } else {
      I11li1.iI丨LLL1(this);
    } 
  }
  
  public final void 丨il(Bitmap paramBitmap, String paramString1, String paramString2, String paramString3) {
    FrameLayout frameLayout = new FrameLayout((Context)this);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
    frameLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    RelativeLayout relativeLayout = new RelativeLayout((Context)this);
    relativeLayout.setId(View.generateViewId());
    relativeLayout.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
    relativeLayout.setGravity(17);
    ImageView imageView = new ImageView((Context)this);
    imageView.setId(View.generateViewId());
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
    imageView.setImageBitmap(paramBitmap);
    TextView textView = new TextView((Context)this);
    textView.setId(View.generateViewId());
    textView.setText(ILil.IL1Iii.IL1Iii(new byte[] { 82, 6, 66 }, "c6105c"));
    textView.setTextSize(14.0F);
    textView.setTextColor(-1);
    textView.setGravity(17);
    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ILL(80), ILL(30));
    layoutParams1.addRule(10);
    layoutParams1.addRule(21);
    layoutParams1.setMargins(0, ILL(15), ILL(15), 0);
    textView.setLayoutParams((ViewGroup.LayoutParams)layoutParams1);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(-1);
    gradientDrawable.setCornerRadius(8.0F);
    textView.setBackgroundResource(2131230838);
    relativeLayout.addView((View)imageView);
    relativeLayout.addView((View)textView);
    frameLayout.addView((View)relativeLayout);
    ((ViewGroup)getWindow().getDecorView().findViewById(16908290)).addView((View)frameLayout, (ViewGroup.LayoutParams)layoutParams);
    imageView.setOnClickListener(new I1I(this, paramString2, paramString1, paramString3));
    (new I丨L(this, 10000L, 1000L, textView)).start();
  }
  
  public class I1I implements View.OnClickListener {
    public final String I1I;
    
    public final String IL1Iii;
    
    public final String ILil;
    
    public final SplashActivity I丨L;
    
    public I1I(SplashActivity this$0, String param1String1, String param1String2, String param1String3) {}
    
    public void onClick(View param1View) {
      if (ILil.IL1Iii.IL1Iii(new byte[] { 88, 17, 11, 10 }, "0eff56").compareTo(this.IL1Iii) == 0) {
        Intent intent = new Intent();
        intent.setAction(ILil.IL1Iii.IL1Iii(new byte[] { 
                5, 92, 81, 74, 87, 81, 0, 28, 92, 86, 
                76, 93, 10, 70, 27, 89, 91, 76, 13, 93, 
                91, 22, 110, 113, 33, 101 }, "d25888"));
        intent.setData(Uri.parse(this.ILil));
        this.I丨L.startActivity(intent);
      } else {
        ILil.I丨L.I1I.I1I.Ilil((Context)this.I丨L).I丨L(this.ILil, this.I1I, "");
      } 
    }
  }
  
  public class IL1Iii implements ILil.I丨L.I丨L.l丨Li1LL.I1I {
    public final SplashActivity IL1Iii;
    
    public IL1Iii(SplashActivity this$0) {}
    
    public void IL1Iii(ILil.I丨L.I丨L.l丨Li1LL.I丨L param1I丨L) {
      if (param1I丨L == ILil.I丨L.I丨L.l丨Li1LL.I丨L.Ok) {
        SplashActivity.IL1Iii(this.IL1Iii, false);
        I11li1.LLii1I(this.IL1Iii);
      } else {
        SplashActivity.ILil(this.IL1Iii);
      } 
    }
  }
  
  public class ILil implements ILil.I丨L.I1I.I1I.ILil {
    public final SplashActivity IL1Iii;
    
    public ILil(SplashActivity this$0) {}
    
    public void IL1Iii() {
      SplashActivity.I丨L(this.IL1Iii);
    }
    
    public void ILil(Bitmap param1Bitmap, String param1String1, String param1String2, String param1String3) {
      I11li1.lILIlI(new IL1Iii(this, param1Bitmap, param1String1, param1String2, param1String3));
    }
    
    public class IL1Iii implements Runnable {
      public final String I1I;
      
      public final Bitmap IL1Iii;
      
      public final String ILil;
      
      public final SplashActivity.ILil Ilil;
      
      public final String I丨L;
      
      public IL1Iii(SplashActivity.ILil this$0, Bitmap param2Bitmap, String param2String1, String param2String2, String param2String3) {}
      
      public void run() {
        SplashActivity.I1I(this.Ilil.IL1Iii, this.IL1Iii, this.ILil, this.I1I, this.I丨L);
      }
    }
  }
  
  public class IL1Iii implements Runnable {
    public final String I1I;
    
    public final Bitmap IL1Iii;
    
    public final String ILil;
    
    public final SplashActivity.ILil Ilil;
    
    public final String I丨L;
    
    public IL1Iii(SplashActivity this$0, Bitmap param1Bitmap, String param1String1, String param1String2, String param1String3) {}
    
    public void run() {
      SplashActivity.I1I(this.Ilil.IL1Iii, this.IL1Iii, this.ILil, this.I1I, this.I丨L);
    }
  }
  
  public class I丨L extends CountDownTimer {
    public final TextView IL1Iii;
    
    public final SplashActivity ILil;
    
    public I丨L(SplashActivity this$0, long param1Long1, long param1Long2, TextView param1TextView) {
      super(param1Long1, param1Long2);
    }
    
    public void onFinish() {
      this.IL1Iii.setText(ILil.IL1Iii.IL1Iii(new byte[] { 
              -47, -69, Byte.MIN_VALUE, -124, -76, -119, -34, -122, -94, -124, 
              -74, -105 }, "699a32"));
      ILil.I丨L.I1I.I1I.Ilil((Context)this.ILil).l丨Li1LL();
      I11li1.II丨丨1LL(5, 5);
      SplashActivity.I丨L(this.ILil);
      this.IL1Iii.setOnClickListener(new IL1Iii(this));
    }
    
    public void onTick(long param1Long) {
      param1Long /= 1000L;
      TextView textView = this.IL1Iii;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1Long);
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { -33, -98, -91 }, "8977e6"));
      textView.setText(stringBuilder.toString());
    }
    
    public class IL1Iii implements View.OnClickListener {
      public final SplashActivity.I丨L IL1Iii;
      
      public IL1Iii(SplashActivity.I丨L this$0) {}
      
      public void onClick(View param2View) {
        SplashActivity.I丨L(this.IL1Iii.ILil);
      }
    }
  }
  
  public class IL1Iii implements View.OnClickListener {
    public final SplashActivity.I丨L IL1Iii;
    
    public IL1Iii(SplashActivity this$0) {}
    
    public void onClick(View param1View) {
      SplashActivity.I丨L(this.IL1Iii.ILil);
    }
  }
  
  public class I丨iL implements Runnable {
    public final SplashActivity IL1Iii;
    
    public I丨iL(SplashActivity this$0) {}
    
    public void run() {
      if (ILil.I丨L.I丨L.llliI.IL1Iii.iI丨LLL1().I1I((Context)this.IL1Iii)) {
        if (I11li1.l1i丨I丨I丨((Context)this.IL1Iii)) {
          SplashActivity.I丨iL(this.IL1Iii);
        } else if (I11li1.丨LIL1li((Context)this.IL1Iii)) {
          if (I11li1.LIIl丨丨L((Context)this.IL1Iii) || I11li1.IliI((Context)this.IL1Iii)) {
            Intent intent = new Intent((Context)this.IL1Iii, MainActivity.class);
            this.IL1Iii.startActivity(intent);
          } else {
            Intent intent = new Intent((Context)this.IL1Iii, ScriptActivity.class);
            this.IL1Iii.startActivity(intent);
          } 
          this.IL1Iii.finish();
        } else {
          SplashActivity.L丨1丨1丨I(this.IL1Iii);
        } 
      } else {
        SplashActivity.IL1Iii(this.IL1Iii, false);
        ILil.I丨L.I丨L.llliI.IL1Iii.iI丨LLL1().ILil((Context)this.IL1Iii);
      } 
    }
  }
  
  public class iI丨LLL1 implements Runnable {
    public final SplashActivity IL1Iii;
    
    public iI丨LLL1(SplashActivity this$0) {}
    
    public void run() {
      ILil.I丨L.I丨L.l丨Li1LL.ILil(this.IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
              -45, -8, -68, -41, -67, -60, -48, -3, -103, -44, 
              -105, -60, -36, -50, -67, -41, -109, -44, -35, -48, 
              -108, 30, -33, -54, -126, -115, -79, -78, -46, -30, 
              -113, -115, -74, -65, -33, -54, -96 }, "5d127e"), ILil.IL1Iii.IL1Iii(new byte[] { 
              -38, -91, -3, -119, -50, -101, -43, -66, -12, -122, 
              -59, -114 }, "31daa4"), new IL1Iii(this));
    }
    
    public class IL1Iii implements ILil.I丨L.I丨L.l丨Li1LL.I1I {
      public final SplashActivity.iI丨LLL1 IL1Iii;
      
      public IL1Iii(SplashActivity.iI丨LLL1 this$0) {}
      
      public void IL1Iii(ILil.I丨L.I丨L.l丨Li1LL.I丨L param2I丨L) {
        this.IL1Iii.IL1Iii.finish();
      }
    }
  }
  
  public class IL1Iii implements ILil.I丨L.I丨L.l丨Li1LL.I1I {
    public final SplashActivity.iI丨LLL1 IL1Iii;
    
    public IL1Iii(SplashActivity this$0) {}
    
    public void IL1Iii(ILil.I丨L.I丨L.l丨Li1LL.I丨L param1I丨L) {
      this.IL1Iii.IL1Iii.finish();
    }
  }
  
  public class l丨Li1LL implements Runnable {
    public final SplashActivity IL1Iii;
    
    public l丨Li1LL(SplashActivity this$0) {}
    
    public void run() {
      if (!ILil.I丨L.I1I.I1I.Ilil((Context)this.IL1Iii).I1I(false)) {
        SplashActivity.Ilil(this.IL1Iii);
        SplashActivity.l丨Li1LL(this.IL1Iii, null);
        return;
      } 
      SplashActivity.iI丨LLL1(this.IL1Iii);
      SplashActivity.l丨Li1LL(this.IL1Iii, null);
    }
  }
}
