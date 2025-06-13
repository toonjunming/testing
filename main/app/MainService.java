package com.main.app;

import ILil.I丨L.ILil.I丨Ii;
import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.ILil.LL1IL;
import ILil.I丨L.ILil.Lil;
import ILil.I丨L.ILil.LlLI1;
import ILil.I丨L.ILil.L丨lLLL;
import ILil.I丨L.ILil.i1;
import ILil.I丨L.ILil.iIi1;
import ILil.I丨L.ILil.ill1LI1l;
import ILil.I丨L.ILil.l1Lll;
import ILil.I丨L.ILil.lI丨II;
import ILil.I丨L.ILil.lL;
import ILil.I丨L.ILil.l丨丨i11;
import ILil.I丨L.I丨L.I11li1;
import Ilil.ILil.IL1Iii.iIi1;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.INXCoreNative;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.nx.assist.UIMgr;
import com.nx.assist.log.Console;
import com.nx.uiauto.accessibility.AccessibilityService;
import java.io.File;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class MainService extends Service implements LL1IL, View.OnClickListener {
  public static MainService ll丨L1ii;
  
  public boolean I1I = false;
  
  public KeyguardManager.KeyguardLock IL1Iii = null;
  
  public boolean ILL = false;
  
  public Handler ILil = null;
  
  public Ll丨1 IL丨丨l = new Ll丨1(this);
  
  public iIi1 Ilil = null;
  
  public WindowManager I丨L;
  
  public boolean I丨iL = false;
  
  public boolean Lil = false;
  
  public boolean LlLI1 = false;
  
  public ServiceConnection Ll丨1;
  
  public boolean L丨1丨1丨I = false;
  
  public boolean iI丨LLL1 = false;
  
  public lI丨II lIi丨I = null;
  
  public String l丨Li1LL = "";
  
  public ILil.I丨L.I丨L.Ll丨1 丨il;
  
  public static MainService llliI() {
    return ll丨L1ii;
  }
  
  public final void I11L() {
    if (!this.Ilil.getRunState()) {
      this.L丨1丨1丨I = true;
      丨lL();
    } else {
      this.L丨1丨1丨I = true;
      Lil(6);
    } 
    I11li1();
  }
  
  public final void I11li1() {
    try {
      lI丨II lI丨II1 = this.lIi丨I;
      if (lI丨II1 != null)
        lI丨II1.sendCmd(7, 0, 0, ""); 
    } catch (Exception exception) {}
  }
  
  public void I1I(INXCoreNative paramINXCoreNative) throws RemoteException {}
  
  public void ILL(Intent paramIntent) throws RemoteException {}
  
  public final void I丨Ii(boolean paramBoolean) {
    if (!I11li1.Lil丨I丨((Context)this))
      return; 
    if (!paramBoolean) {
      if (this.Ilil.getParent() != null) {
        this.I丨L.removeView((View)this.Ilil);
        UIState.getInstance().showFloatWnd(false);
      } 
    } else if (this.Ilil.getParent() == null) {
      WindowManager windowManager = this.I丨L;
      iIi1 iIi11 = this.Ilil;
      windowManager.addView((View)iIi11, (ViewGroup.LayoutParams)iIi11.getLayoutParams());
      UIState.getInstance().showFloatWnd(true);
    } 
  }
  
  public void I丨iL(int paramInt, String paramString) {
    丨丨LLlI1(paramString, false);
  }
  
  public final void L11丨(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
    try {
      lI丨II lI丨II1 = this.lIi丨I;
      if (lI丨II1 != null)
        lI丨II1.sendCmd(2, 0, 0, paramString); 
    } catch (Exception exception) {}
    this.Ll丨1 = new IL1Iii(this, paramBoolean1, paramString, paramBoolean2);
    bindService(new Intent((Context)this, PluginService.class), this.Ll丨1, 3);
  }
  
  public void L11丨丨丨1(String paramString) {
    if (TextUtils.isEmpty(paramString)) {
      I11li1.iLiliI丨(ILil.IL1Iii.IL1Iii(new byte[] { 
              -45, -120, -74, -47, -61, -68, -33, -77, -69, -47, 
              -12, -104, -34, -80, -84, -46, -8, -101 }, "6464d7"));
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              -122, -123, -80, -47, -62, -71, -124, -70, -99, -46, 
              -2, -122, -123, -81, Byte.MIN_VALUE, -36, -31, -88, -123, -91, 
              -100, 9, 91 }, "c904e2"));
      stringBuilder.append(paramString);
      I11li1.iLiliI丨(stringBuilder.toString());
    } 
    if (!TextUtils.isEmpty(paramString)) {
      File file = new File(paramString);
      String str = ILil.IL1Iii.IL1Iii(new byte[] { 122, 96 }, "48ccc5");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              17, 93, 68, 77, 83, 68, 23, 107, 84, 75, 
              91, 70, 23, 24, 85, 92, 85, 95, 13, 5, 
              9 }, "c87926"));
      stringBuilder.append(file.exists());
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 8 }, "3e2133"));
      stringBuilder.append(file.getAbsolutePath());
      Log.i(str, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              -47, -118, -77, -35, -63, -72, -45, -75, -98, -34, 
              -3, -121, -46, -96, -125, -47, -31, -66, -47, -90, 
              -100, 5, 88 }, "4638f3"));
      stringBuilder.append(file.exists());
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 9 }, "2c439c"));
      stringBuilder.append(file.getAbsolutePath());
      I11li1.iLiliI丨(stringBuilder.toString());
      if (file.exists()) {
        if (!file.renameTo(new File(this.l丨Li1LL))) {
          String str1 = ILil.IL1Iii.IL1Iii(new byte[] { 119, 59 }, "9c5454");
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append(ILil.IL1Iii.IL1Iii(new byte[] { 
                  67, 84, 8, 5, 12, 7, 17, 87, 7, 13, 
                  13, 7, 85, 12, 88 }, "11fdab"));
          stringBuilder2.append(file.getAbsolutePath());
          Log.i(str1, stringBuilder2.toString());
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(ILil.IL1Iii.IL1Iii(new byte[] { 
                  -122, -31, -56, -45, -82, -124, -121, -12, -43, -36, 
                  -78, -67, -124, -13, -40, -48, -91, -67, -124, -58, 
                  -44, -35, -127, -107, 92, 92 }, "abe550"));
          stringBuilder1.append(file.getAbsolutePath());
          I11li1.iLiliI丨(stringBuilder1.toString());
          Lil(7);
          return;
        } 
        I11li1.iLiliI丨(ILil.IL1Iii.IL1Iii(new byte[] { 
                -119, -68, -8, -43, -1, -54, -124, -106, -21, -37, 
                -64, -29, -124, -106, -18, -43, -21, -10, -124, -124, 
                -30, -42, -60, -19, -120, -65, -17, -42, -13, -55 }, "a8b3cf"));
        LlLI1.IL1Iii().Ilil((new File(this.l丨Li1LL)).lastModified(), (Context)this);
      } else {
        I11li1.iLiliI丨(ILil.IL1Iii.IL1Iii(new byte[] { 
                -116, -80, -84, -41, -83, -55, -127, -102, -65, -39, 
                -110, -32, -127, -112, -121, -39, -123, -64, -125, -81, 
                -126, -41, -65, -64, -125, -113, -91, -41, -84, -6, 
                -116, -80, -84, -41, -83, -55 }, "d4611e"));
        Lil(8);
        return;
      } 
    } 
    this.iI丨LLL1 = true;
    Lil(9);
  }
  
  public final void LI丨丨l丨l(String paramString) {
    this.ILil.post(new L丨1丨1丨I(this, paramString));
  }
  
  public final void LL1IL(boolean paramBoolean) {
    I11li1.lILIlI(new I丨iL(this, paramBoolean));
  }
  
  public void Lil(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_2
    //   11: bipush #19
    //   13: newarray byte
    //   15: dup
    //   16: iconst_0
    //   17: bipush #-38
    //   19: bastore
    //   20: dup
    //   21: iconst_1
    //   22: bipush #-108
    //   24: bastore
    //   25: dup
    //   26: iconst_2
    //   27: bipush #-98
    //   29: bastore
    //   30: dup
    //   31: iconst_3
    //   32: bipush #-121
    //   34: bastore
    //   35: dup
    //   36: iconst_4
    //   37: bipush #-66
    //   39: bastore
    //   40: dup
    //   41: iconst_5
    //   42: bipush #-89
    //   44: bastore
    //   45: dup
    //   46: bipush #6
    //   48: bipush #-43
    //   50: bastore
    //   51: dup
    //   52: bipush #7
    //   54: bipush #-120
    //   56: bastore
    //   57: dup
    //   58: bipush #8
    //   60: bipush #-85
    //   62: bastore
    //   63: dup
    //   64: bipush #9
    //   66: bipush #-124
    //   68: bastore
    //   69: dup
    //   70: bipush #10
    //   72: bipush #-84
    //   74: bastore
    //   75: dup
    //   76: bipush #11
    //   78: bipush #-87
    //   80: bastore
    //   81: dup
    //   82: bipush #12
    //   84: bipush #-38
    //   86: bastore
    //   87: dup
    //   88: bipush #13
    //   90: bipush #-73
    //   92: bastore
    //   93: dup
    //   94: bipush #14
    //   96: bipush #-94
    //   98: bastore
    //   99: dup
    //   100: bipush #15
    //   102: bipush #-124
    //   104: bastore
    //   105: dup
    //   106: bipush #16
    //   108: bipush #-83
    //   110: bastore
    //   111: dup
    //   112: bipush #17
    //   114: bipush #-102
    //   116: bastore
    //   117: dup
    //   118: bipush #18
    //   120: bipush #8
    //   122: bastore
    //   123: ldc_w '238b16'
    //   126: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   129: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: pop
    //   133: aload_2
    //   134: iload_1
    //   135: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   138: pop
    //   139: aload_2
    //   140: invokevirtual toString : ()Ljava/lang/String;
    //   143: invokestatic iLiliI丨 : (Ljava/lang/String;)V
    //   146: aload_0
    //   147: getfield Ilil : LILil/I丨L/ILil/iIi1;
    //   150: invokevirtual getPauseState : ()Z
    //   153: ifeq -> 171
    //   156: aload_0
    //   157: getfield Ilil : LILil/I丨L/ILil/iIi1;
    //   160: iconst_0
    //   161: invokevirtual setPauseState : (Z)V
    //   164: invokestatic iI : ()LILil/I丨L/ILil/i1;
    //   167: invokevirtual 丨IillIi : ()Z
    //   170: pop
    //   171: invokestatic iI : ()LILil/I丨L/ILil/i1;
    //   174: invokevirtual iI1L1丨Ll : ()V
    //   177: aload_0
    //   178: monitorexit
    //   179: return
    //   180: astore_2
    //   181: aload_0
    //   182: monitorexit
    //   183: aload_2
    //   184: athrow
    // Exception table:
    //   from	to	target	type
    //   2	171	180	finally
    //   171	177	180	finally
  }
  
  public final void L丨1l(int paramInt1, int paramInt2) {
    if (!I11li1.Lil丨I丨((Context)this))
      return; 
    if (this.Ilil.getParent() != null) {
      this.Ilil.Ll丨1();
      this.Ilil.Lil(paramInt1, paramInt2);
    } 
  }
  
  public final void L丨lLLL() {
    try {
      String str = ILil.IL1Iii.IL1Iii(new byte[] { 45, 102, 50 }, "a4be8c");
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              88, 71, 84, 94, 51, 85, 66, 80, 88, 94, 
              34, 90, 67, 94, 71, 89, 23, 64, 13 }, "7710c9"));
      stringBuilder.append(this.lIi丨I);
      Log.i(str, stringBuilder.toString());
      lI丨II lI丨II1 = this.lIi丨I;
      if (lI丨II1 != null)
        lI丨II1.sendCmd(3, 0, 0, ""); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public IBinder asBinder() {
    return null;
  }
  
  public final void il丨l丨(boolean paramBoolean, String paramString) {
    // Byte code:
    //   0: iload_1
    //   1: ifne -> 33
    //   4: aload_0
    //   5: invokestatic LIIl丨丨L : (Landroid/content/Context;)Z
    //   8: ifne -> 18
    //   11: aload_0
    //   12: invokestatic IliI : (Landroid/content/Context;)Z
    //   15: ifeq -> 33
    //   18: new android/content/Intent
    //   21: astore_2
    //   22: aload_2
    //   23: aload_0
    //   24: ldc_w com/main/app/MainActivity
    //   27: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
    //   30: goto -> 126
    //   33: aload_0
    //   34: invokestatic 丨LIL1li : (Landroid/content/Context;)Z
    //   37: ifne -> 62
    //   40: iload_1
    //   41: ifeq -> 47
    //   44: goto -> 62
    //   47: new android/content/Intent
    //   50: dup
    //   51: aload_0
    //   52: ldc_w com/main/app/SettingsActivity
    //   55: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
    //   58: astore_2
    //   59: goto -> 126
    //   62: new android/content/Intent
    //   65: astore_3
    //   66: aload_3
    //   67: aload_0
    //   68: ldc_w com/main/app/ScriptActivity
    //   71: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
    //   74: iload_1
    //   75: ifeq -> 124
    //   78: aload_3
    //   79: bipush #6
    //   81: newarray byte
    //   83: dup
    //   84: iconst_0
    //   85: bipush #22
    //   87: bastore
    //   88: dup
    //   89: iconst_1
    //   90: bipush #80
    //   92: bastore
    //   93: dup
    //   94: iconst_2
    //   95: bipush #83
    //   97: bastore
    //   98: dup
    //   99: iconst_3
    //   100: bipush #81
    //   102: bastore
    //   103: dup
    //   104: iconst_4
    //   105: bipush #85
    //   107: bastore
    //   108: dup
    //   109: iconst_5
    //   110: bipush #85
    //   112: bastore
    //   113: ldc_w 'c95890'
    //   116: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   119: aload_2
    //   120: invokevirtual putExtra : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   123: pop
    //   124: aload_3
    //   125: astore_2
    //   126: aload_2
    //   127: ldc_w 268435456
    //   130: invokevirtual setFlags : (I)Landroid/content/Intent;
    //   133: pop
    //   134: aload_0
    //   135: iconst_0
    //   136: aload_2
    //   137: ldc_w 134217728
    //   140: invokestatic getActivity : (Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
    //   143: invokevirtual send : ()V
    //   146: goto -> 154
    //   149: astore_2
    //   150: aload_2
    //   151: invokevirtual printStackTrace : ()V
    //   154: return
    // Exception table:
    //   from	to	target	type
    //   4	18	149	java/lang/Exception
    //   18	30	149	java/lang/Exception
    //   33	40	149	java/lang/Exception
    //   47	59	149	java/lang/Exception
    //   62	74	149	java/lang/Exception
    //   78	124	149	java/lang/Exception
    //   126	146	149	java/lang/Exception
  }
  
  public final void l1IIi1丨(double paramDouble1, double paramDouble2) {
    if (!I11li1.Lil丨I丨((Context)this))
      return; 
    if (this.Ilil.getParent() != null) {
      Point point = I11li1.IIi丨丨I((Context)this);
      double d1 = paramDouble1;
      if (paramDouble1 < 0.0D)
        d1 = 0.0D; 
      double d3 = 1.0D;
      double d2 = d1;
      if (d1 > 1.0D)
        d2 = 1.0D; 
      paramDouble1 = paramDouble2;
      if (paramDouble2 < 0.0D)
        paramDouble1 = 0.0D; 
      if (paramDouble1 > 1.0D)
        paramDouble1 = d3; 
      int j = (int)(point.x * d2);
      int i = (int)(point.y * paramDouble1);
      this.Ilil.Ll丨1();
      this.Ilil.Lil(j, i);
      UIState.getInstance().setFloatWndPos(j, i);
    } 
  }
  
  public final void lI丨II() {
    I11li1.lILIlI(new iI丨LLL1(this));
  }
  
  public String llI(int paramInt, String paramString) {
    try {
      lI丨II lI丨II1 = this.lIi丨I;
      if (lI丨II1 != null)
        return lI丨II1.sendCmd(6, paramInt, 0, paramString); 
    } catch (Exception exception) {}
    return null;
  }
  
  @Nullable
  public IBinder onBind(Intent paramIntent) {
    return (IBinder)new lIi丨I(this);
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i == 2131296470) {
      if (!this.Ilil.getRunState()) {
        I丨iL(-1, null);
      } else {
        Lil(5);
      } 
    } else if (i == 2131296471) {
      il丨l丨(false, null);
    } else if (i == 2131296469) {
      lI丨II();
    } else if (i == 2131296468) {
      I11L();
    } else if (i == 2131296472) {
      if (i1.iI().丨i1丨1i()) {
        i1.iI().i丨1I1I1l();
      } else {
        I11li1.LlLl111l((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
                -116, -55, -123, -43, -29, -65, -116, -39, -94, -40, 
                -57, -69, -116, -30, -88, -42, -6, -101 }, "df20f7"));
      } 
    } 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    I11li1.LL丨((Context)this);
    l丨丨i11.l丨Li1LL().Ll丨1();
    WindowManager windowManager = this.I丨L;
    if (windowManager != null) {
      Display display = windowManager.getDefaultDisplay();
      ill1LI1l.I1I().IL1Iii(display.getRotation());
    } 
    i1.iI().I1IILIIL();
  }
  
  @SuppressLint({"ServiceCast"})
  public void onCreate() {
    super.onCreate();
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 44, 109 }, "b51c03");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 75, 6, 65, 20, 90, 80, 93, 67 }, "8c3b33"));
    stringBuilder.append(this);
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 20, 74, 20, 91 }, "48a511"));
    Log.i(str, stringBuilder.toString());
    丨丨丨丨((Context)this, (int)(System.currentTimeMillis() / 1000L), 2131558414, getResources().getString(2131689501), getResources().getString(2131689501), MainActivity.class);
    ll丨L1ii = this;
    Native.setServiceOk();
    this.丨il = new ILil.I丨L.I丨L.Ll丨1((Context)this);
    this.l丨Li1LL = I11li1.L1iI1((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 75, 1, 67, 95, 21, 22, 22, 14, 67, 92 }, "8b16eb"), false);
    File file = new File(this.l丨Li1LL);
    if (ILil.I丨L.I丨L.ILil.IL1Iii((Context)this).I丨L() || (!file.exists() && !I11li1.LIIl丨丨L((Context)this))) {
      Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 122, 62 }, "4fab5c"), ILil.IL1Iii.IL1Iii(new byte[] { 
              65, 71, 84, 7, 76, 86, 20, 68, 83, 20, 
              81, 67, 64, 22, 17, 71 }, "470f83"));
      if (file.exists())
        file.delete(); 
      I11li1.I11L((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 16, 87, 68, 81, 70, 76, 77, 88, 68, 82 }, "c46868"), ILil.IL1Iii.IL1Iii(new byte[] { 71, 85, 68, 80, 69, 66, 26, 90, 68, 83 }, "466956"));
      if (file.exists()) {
        long l = file.lastModified();
        LlLI1.IL1Iii().Ilil(l, (Context)this);
      } 
    } else if (I11li1.LIIl丨丨L((Context)this) && file.exists()) {
      file.delete();
    } 
    I11li1.LL丨((Context)this);
    ill1LI1l.I1I().I丨L(new I丨L(this));
    Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().Lil(this);
    this.ILil = new Handler();
    try {
      KeyguardManager.KeyguardLock keyguardLock = ((KeyguardManager)getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 10, 3, 24, 82, 67, 2, 19, 2 }, "afa56c"))).newKeyguardLock(MainService.class.toString());
      this.IL1Iii = keyguardLock;
      keyguardLock.disableKeyguard();
    } catch (Exception exception) {}
    i1.iI().iiIIi丨11((Context)this);
    this.I丨L = (WindowManager)getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 70, 11, 93, 5, 13, 22 }, "1b3aba"));
    iIi1 iIi11 = new iIi1((Context)this);
    this.Ilil = iIi11;
    iIi11.setOnClickListener(this);
    IntentFilter intentFilter = new IntentFilter(ILil.IL1Iii.IL1Iii(new byte[] { 
            7, 87, 82, 67, 95, 80, 2, 23, 95, 95, 
            68, 92, 8, 77, 24, 80, 83, 77, 15, 86, 
            88, 31, 114, 120, 50, 109, 115, 99, 105, 102, 
            37, 113, 119, Byte.MAX_VALUE, 119, 124, 34 }, "f96109"));
    registerReceiver(new ILL(this), intentFilter);
    Console.getInstance().init((Context)this);
    l丨丨i11.l丨Li1LL().I丨iL((Context)this);
  }
  
  public void onDestroy() {
    super.onDestroy();
    Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().ll丨L1ii(this);
  }
  
  @iIi1(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LI丨丨l丨l paramLI丨丨l丨l) {
    String str;
    if (paramLI丨丨l丨l.I1I() == 1) {
      str = paramLI丨丨l丨l.IL1Iii();
      if (str != null && (new File(str)).exists())
        ProjectActivity.IL1Iii((Context)this, str); 
    } else {
      if (str.I1I() == 2 || str.I1I() == 207) {
        File file = new File(str.IL1Iii());
        if (file.exists()) {
          File file1 = new File(this.l丨Li1LL);
          file.renameTo(file1);
          if (file1.exists())
            LlLI1.IL1Iii().Ilil(file1.lastModified(), (Context)this); 
        } 
        i1 i1 = i1.iI();
        if (i1 != null && str.I1I() == 207)
          i1.i丨ILiiLl(str.ILil()); 
        I丨iL(-1, this.l丨Li1LL);
        return;
      } 
      if (str.I1I() == 3) {
        Lil(1);
      } else if (str.I1I() == 5) {
        try {
          JSONObject jSONObject = new JSONObject();
          this(str.IL1Iii());
          l1IIi1丨(jSONObject.getDouble(ILil.IL1Iii.IL1Iii(new byte[] { 78 }, "62d740")), jSONObject.getDouble(ILil.IL1Iii.IL1Iii(new byte[] { 72 }, "1b8220")));
        } catch (Exception exception) {}
      } else if (exception.I1I() == 6) {
        I丨Ii(Boolean.parseBoolean(exception.IL1Iii()));
      } else if (exception.I1I() == 7) {
        File file = new File(exception.IL1Iii());
        if (file.exists()) {
          File file1 = new File(this.l丨Li1LL);
          file.renameTo(file1);
          if (file1.exists()) {
            LlLI1.IL1Iii().Ilil(file1.lastModified(), (Context)this);
            il丨l丨(true, exception.ILil());
          } 
        } 
      } else if (exception.I1I() == 99) {
        L11丨丨丨1(exception.IL1Iii());
      } else if (exception.I1I() == 100) {
        if (this.I1I) {
          this.L丨1丨1丨I = true;
          Lil(2);
        } else {
          this.L丨1丨1丨I = true;
          丨lL();
        } 
      } else if (exception.I1I() == 101) {
        String str1 = exception.IL1Iii();
        if (!TextUtils.isEmpty(str1) && str1.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 5 }, "721d53")) == 0)
          I11li1.LlLl111l((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
                  -114, -104, -59, -33, -53, -72, -125, -110, -48, -33, 
                  -48, -100, -119, -118, -19, -33, -32, -93, Byte.MIN_VALUE, -86, 
                  -51, -48, -33, -86, Byte.MIN_VALUE, -85, -2 }, "f6a7d9")); 
        if (this.I1I)
          丨丨丨1丨(Integer.parseInt(exception.IL1Iii())); 
      } else if (exception.I1I() == 102) {
        丨l丨();
      } else if (exception.I1I() == 103) {
        AccessibilityService.getInstance();
      } else {
        String str1;
        if (exception.I1I() == 105) {
          if (I11li1.LIlLi((Context)this, 2131296636, I11li1.l丨丨i11((Context)this))) {
            str1 = exception.IL1Iii();
            if (str1 != null && str1.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 9 }, "9adb33")) == 0) {
              if (!this.I1I)
                丨丨LLlI1(null, false); 
            } else if (str1 != null && str1.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 3 }, "25658a")) == 0 && this.I1I) {
              Lil(3);
            } 
          } 
        } else if (str1.I1I() == 106) {
          this.丨il.Ilil();
        } else if (str1.I1I() == 108) {
          str1.ILil();
          int i = Integer.parseInt(str1.ILil());
          i1.iI().Liil1L1l(i, str1.IL1Iii());
        } else if (str1.I1I() == 200) {
          String str2 = str1.IL1Iii();
          L11丨(str1.ILil(), false, ILil.IL1Iii.IL1Iii(new byte[] { 18, 22, 70, 93 }, "fd383e").equals(str2));
        } else if (str1.I1I() == 201) {
          L丨lLLL();
        } else if (str1.I1I() == 202) {
          L11丨(str1.ILil(), true, false);
        } else if (str1.I1I() == 205) {
          if (I11li1.Li((Context)this))
            this.ILil.postDelayed(new l丨Li1LL(this), 1000L); 
        } else if (str1.I1I() == 208) {
          str1 = str1.IL1Iii();
          i1.iI().LiL1(0, str1);
        } else if (str1.I1I() == 209) {
          I11li1.LlLl111l((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
                  -44, -117, -14, -46, -21, -17, 112, 102, 49, -45, 
                  -38, -20, 120, 114, 36, -48, -21, -22, -41, -86, 
                  -51, -45, -38, -17, -44, -70, -40, -34, -25, -17, 
                  -39, -103, -42, -45, -38, -23, -39, -117, -36, -46, 
                  -52, -21, -39, -107, -28, -47, -2, -30, -41, -96, 
                  -47, -48, -8, -26 }, "16a7bb"));
        } 
      } 
    } 
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    super.onStartCommand(paramIntent, paramInt1, paramInt2);
    if (paramIntent != null) {
      String str = paramIntent.getStringExtra(ILil.IL1Iii.IL1Iii(new byte[] { 7, 5, 18, 92, 14, 12 }, "fff5ab"));
    } else {
      paramIntent = null;
    } 
    boolean bool = this.Lil;
    if (!bool && I11li1.丨l丨L1((Context)this)) {
      this.Lil = true;
      L11丨(ILil.IL1Iii.IL1Iii(new byte[] { 
              88, 71, 69, 83, 64, 68, 22, 88, 68, 70, 
              88, 66, 94, 93, 88, 24, 85, 71, 82 }, "946647"), false, false);
    } 
    if (paramIntent != null && paramIntent.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 81, 92, 91, 21, 3, 72, 72 }, "822ab8")) == 0) {
      if (bool)
        if (this.LlLI1) {
          Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(203, ILil.IL1Iii.IL1Iii(new byte[] { 7 }, "6f65b3"), ""));
        } else {
          Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(203, ILil.IL1Iii.IL1Iii(new byte[] { 84 }, "d564df"), ""));
        }  
    } else if (paramIntent != null && paramIntent.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 66, 71, 80, 69, 16 }, "1317d0")) == 0) {
      if (!this.Ilil.getRunState())
        I丨iL(-1, null); 
    } else if (paramIntent != null && paramIntent.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 64, 77, 10, 20 }, "39ede8")) == 0) {
      if (this.Ilil.getRunState())
        Lil(4); 
    } else if (paramIntent != null && paramIntent.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 71, 10, 89, 66, 7, 84, 91, 3, 66 }, "4b65a8")) == 0) {
      丨iI丨丨LLl(false);
    } else {
      UIState.ILil iLil;
      if (paramIntent != null && paramIntent.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 64, 90, 70, 92, 17, 16 }, "3945ad")) == 0) {
        if (i1.iI().丨i1丨1i()) {
          丨iI丨丨LLl(true);
          if (!this.ILL) {
            this.ILL = true;
            if (i1.iI().LlIl丨())
              this.Ilil.setPauseState(true); 
            UIState.getInstance().onUiShow();
            iLil = UIState.getInstance().getFloatControlState();
            if (iLil.IL1Iii) {
              L丨1l(iLil.ILil, iLil.I1I);
            } else {
              I丨Ii(false);
            } 
          } 
        } 
        i1.iI().丨ILI((Context)this);
      } else {
        if (iLil != null && iLil.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 90, 4, 3, 70, 92, 11, 71, 4 }, "1af60b")) == 0)
          return 1; 
        if (iLil != null && iLil.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 82, 74, 89, 16 }, "720dc6")) == 0) {
          I11L();
          return 1;
        } 
        if (iLil != null && iLil.compareTo(ILil.IL1Iii.IL1Iii(new byte[] { 89, 11, 10, 18 }, "0ecf77")) == 0)
          I11li1.丨l丨L1((Context)this); 
      } 
    } 
    return 1;
  }
  
  public void sendLog(int paramInt, String paramString1, String paramString2, long paramLong, boolean paramBoolean) {
    try {
      lI丨II lI丨II1 = this.lIi丨I;
      if (lI丨II1 != null)
        lI丨II1.sendLog(paramInt, paramString1, paramString2, paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public final void 丨iI丨丨LLl(boolean paramBoolean) {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      if (I11li1.Lil丨I丨((Context)this) && this.Ilil.getParent() == null) {
        WindowManager windowManager = this.I丨L;
        iIi1 iIi11 = this.Ilil;
        windowManager.addView((View)iIi11, (ViewGroup.LayoutParams)iIi11.getLayoutParams());
      } 
      this.I1I = paramBoolean;
      this.Ilil.setRunState(paramBoolean);
    } else {
      this.ILil.post(new 丨il(this, paramBoolean));
    } 
  }
  
  public final void 丨lL() {
    if (this.L丨1丨1丨I) {
      this.L丨1丨1丨I = false;
      if (this.Ilil.getParent() != null)
        this.I丨L.removeView((View)this.Ilil); 
      App app = (App)getApplication();
      if (app != null) {
        Activity activity = app.IL1Iii();
        if (activity != null)
          activity.finish(); 
      } 
    } 
  }
  
  public final void 丨l丨() {
    this.丨il.I丨L(ILil.IL1Iii.IL1Iii(new byte[] { 
            -114, -76, -7, -48, -85, -50, -125, -70, -61, -34, 
            -118, -33, -126, -120, -50, -34, -104, -43, -127, -104, 
            -18, -47, -102, -21 }, "f0c67b"));
    ILil.l丨Li1LL.IL1Iii.IL1Iii.I丨iL().Ilil();
    UIMgr.getInstance().begin();
    if (I11li1.LIlLi((Context)this, 2131296635, false))
      I11li1.I1I(1000L); 
    try {
      lI丨II lI丨II1 = this.lIi丨I;
      if (lI丨II1 != null)
        lI丨II1.sendCmd(4, 0, 0, ""); 
    } catch (Exception exception) {}
  }
  
  public boolean 丨丨() {
    return i1.iI().丨i1丨1i();
  }
  
  public void 丨丨LLlI1(String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: astore_3
    //   4: aload_1
    //   5: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   8: ifeq -> 16
    //   11: aload_0
    //   12: getfield l丨Li1LL : Ljava/lang/String;
    //   15: astore_3
    //   16: aload_0
    //   17: getfield I1I : Z
    //   20: ifeq -> 27
    //   23: iload_2
    //   24: ifeq -> 45
    //   27: aload_0
    //   28: iconst_1
    //   29: putfield I1I : Z
    //   32: aload_0
    //   33: iconst_1
    //   34: invokevirtual 丨iI丨丨LLl : (Z)V
    //   37: invokestatic iI : ()LILil/I丨L/ILil/i1;
    //   40: aload_0
    //   41: aload_3
    //   42: invokevirtual lLIL1 : (Landroid/content/Context;Ljava/lang/String;)V
    //   45: aload_0
    //   46: monitorexit
    //   47: return
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_1
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   4	16	48	finally
    //   16	23	48	finally
    //   27	45	48	finally
  }
  
  public final void 丨丨丨1丨(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
            90, 86, 49, 2, 74, 90, 69, 76, 49, 21, 
            87, 67, 29 }, "58ba83"));
    stringBuilder.append(paramInt);
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 16 }, "9009c7"));
    I11li1.iLiliI丨(stringBuilder.toString());
    Console.getInstance().lock(false);
    this.丨il.Ilil();
    ILil.l丨Li1LL.IL1Iii.IL1Iii.I丨iL().iI丨LLL1();
    UIMgr.getInstance().finish();
    ILil.l丨Li1LL.ILil.I1I.L丨1l().Ilil();
    ILil.l丨Li1LL.IL1Iii.I丨L.I丨L((Context)this).Ilil();
    i1.iI().ill1LI1l();
    I丨Ii.ILil();
    (new Thread(new ILil(this))).start();
    if (!this.iI丨LLL1) {
      丨iI丨丨LLl(false);
      if (paramInt == 0) {
        LI丨丨l丨l(getResources().getString(2131689515));
      } else if (I11li1.LIlLi((Context)this, 2131296633, true)) {
        LI丨丨l丨l(getResources().getString(2131689594));
      } 
      Native.sendEvent(1, 0, null, null);
      丨lL();
      try {
        lI丨II lI丨II1 = this.lIi丨I;
        if (lI丨II1 != null)
          lI丨II1.sendCmd(5, 0, 0, ""); 
      } catch (Exception exception) {}
    } else {
      this.L丨1丨1丨I = false;
      丨iI丨丨LLl(false);
      this.iI丨LLL1 = false;
      this.ILil.postDelayed(new I1I(this), 1000L);
    } 
  }
  
  public final void 丨丨丨丨(Context paramContext, int paramInt1, int paramInt2, String paramString1, String paramString2, Class<?> paramClass) {
    try {
      if (this.I丨iL)
        return; 
      String str1 = paramContext.getPackageName();
      String str2 = paramContext.getPackageName();
      if (Build.VERSION.SDK_INT >= 26) {
        NotificationChannel notificationChannel = new NotificationChannel();
        this(str1, str2, 4);
        ((NotificationManager)getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
                88, 86, 16, 15, 81, 94, 85, 88, 16, 15, 
                88, 89 }, "69df77"))).createNotificationChannel(notificationChannel);
      } 
      Intent intent = new Intent();
      this((Context)this, MainActivity.class);
      PendingIntent.getActivity((Context)this, 0, intent, 0);
      NotificationCompat.Builder builder = new NotificationCompat.Builder();
      this((Context)this, str1);
      startForeground(1, builder.setContentTitle(paramString1).setContentText(paramString2).setWhen(System.currentTimeMillis()).setSmallIcon(2131558414).setLargeIcon(BitmapFactory.decodeResource(getResources(), 2131558414)).build());
      this.I丨iL = true;
    } catch (Exception exception) {}
  }
  
  public class I1I implements Runnable {
    public final MainService IL1Iii;
    
    public I1I(MainService this$0) {}
    
    public void run() {
      this.IL1Iii.丨丨LLlI1(null, true);
    }
  }
  
  public class IL1Iii implements ServiceConnection {
    public final boolean I1I;
    
    public final boolean IL1Iii;
    
    public final String ILil;
    
    public final MainService I丨L;
    
    public IL1Iii(MainService this$0, boolean param1Boolean1, String param1String, boolean param1Boolean2) {}
    
    public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
      lI丨II lI丨II = lI丨II.IL1Iii.ILil(param1IBinder);
      try {
        boolean bool;
        MainService.ILil(this.I丨L, lI丨II);
        lI丨II.lIi丨I(MainService.Ilil(this.I丨L));
        if (this.IL1Iii) {
          bool = true;
        } else {
          bool = false;
        } 
        String str = lI丨II.sendCmd(1, bool, 0, this.ILil);
        boolean bool1 = ILil.IL1Iii.IL1Iii(new byte[] { 16, 68, 20, 83 }, "d6a611").equals(str);
        if (bool1) {
          MainService.iI丨LLL1(this.I丨L, true);
          Ilil.ILil.IL1Iii.L丨1丨1丨I l丨1丨1丨I = Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I();
          LI丨丨l丨l lI丨丨l丨l = new LI丨丨l丨l();
          this(203, ILil.IL1Iii.IL1Iii(new byte[] { 9 }, "899615"), "");
          l丨1丨1丨I.ILL(lI丨丨l丨l);
        } else {
          MainService.iI丨LLL1(this.I丨L, false);
          Ilil.ILil.IL1Iii.L丨1丨1丨I l丨1丨1丨I = Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I();
          LI丨丨l丨l lI丨丨l丨l = new LI丨丨l丨l();
          this(203, ILil.IL1Iii.IL1Iii(new byte[] { 83 }, "c5d1c6"), "");
          l丨1丨1丨I.ILL(lI丨丨l丨l);
        } 
        if (this.I1I)
          if (ILil.IL1Iii.IL1Iii(new byte[] { 22, 22, 70, 82 }, "bd37ea").equals(str)) {
            MainService mainService = this.I丨L;
            I11li1.LlLl111l((Context)mainService, mainService.getResources().getString(2131689582));
          } else {
            MainService mainService = this.I丨L;
            I11li1.LlLl111l((Context)mainService, mainService.getResources().getString(2131689581));
          }  
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
    
    public void onServiceDisconnected(ComponentName param1ComponentName) {
      try {
        Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 118, 96 }, "887095"), ILil.IL1Iii.IL1Iii(new byte[] { 
                13, 11, 103, 82, 67, 21, 11, 6, 81, 115, 
                88, 16, 1, 10, 90, 89, 84, 0, 22, 0, 
                80 }, "be471c"));
        this.I丨L.unbindService(this);
        MainService.ILil(this.I丨L, null);
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
  }
  
  public class ILL extends BroadcastReceiver {
    public ILL(MainService this$0) {}
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      if (ILil.IL1Iii.IL1Iii(new byte[] { 
            84, 12, 2, 19, 92, 89, 81, 76, 15, 15, 
            71, 85, 91, 22, 72, 0, 80, 68, 92, 13, 
            8, 79, 113, 113, 97, 54, 35, 51, 106, 111, 
            118, 42, 39, 47, 116, 117, 113 }, "5bfa30").equals(param1Intent.getAction())) {
        int i = param1Intent.getIntExtra(ILil.IL1Iii.IL1Iii(new byte[] { 15, 1, 70, 80, 94 }, "cd0523"), 0);
        int j = param1Intent.getIntExtra(ILil.IL1Iii.IL1Iii(new byte[] { 64, 86, 3, 88, 83 }, "35b469"), 100);
        if (j > 0)
          I11li1.L11LlL(i * 100 / j); 
      } 
    }
  }
  
  public class ILil implements Runnable {
    public ILil(MainService this$0) {}
    
    public void run() {
      lL.LlLI1().iI丨LLL1();
      l1Lll.lI丨lii().iI丨LLL1();
      if (ILil.I丨L.ILil.Ll丨1.iI丨LLL1() != null)
        ILil.I丨L.ILil.Ll丨1.iI丨LLL1().Ilil(); 
    }
  }
  
  public class I丨L implements ill1LI1l.ILil {
    public I丨L(MainService this$0) {}
    
    public void IL1Iii(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {}
  }
  
  public class I丨iL implements Runnable {
    public final boolean IL1Iii;
    
    public final MainService ILil;
    
    public I丨iL(MainService this$0, boolean param1Boolean) {}
    
    public void run() {
      if (i1.iI().丨i1丨1i())
        if (this.IL1Iii) {
          if (!i1.iI().LlIl丨()) {
            MainService.iIi1(this.ILil).setPauseState(true);
            i1.iI().lI1I1();
          } 
        } else if (i1.iI().LlIl丨()) {
          MainService.iIi1(this.ILil).setPauseState(false);
          i1.iI().丨IillIi();
        }  
    }
  }
  
  public class Ll丨1 extends L丨lLLL.IL1Iii {
    public final MainService ILil;
    
    public Ll丨1(MainService this$0) {}
    
    public int IL1Iii() throws RemoteException {
      Lil.IL1Iii iL1Iii = I11li1.丨i1丨1i((Context)this.ILil);
      return (iL1Iii == Lil.IL1Iii.HANDLE) ? 0 : ((iL1Iii == Lil.IL1Iii.ROOT) ? 1 : ((iL1Iii == Lil.IL1Iii.ACC) ? 2 : -1));
    }
    
    public boolean IL丨丨l() throws RemoteException {
      return i1.iI().IL丨丨l();
    }
    
    public boolean L丨1丨1丨I() throws RemoteException {
      return i1.iI().L丨1丨1丨I();
    }
    
    public Bitmap l丨Li1LL(int param1Int1, int param1Int2, int param1Int3, int param1Int4) throws RemoteException {
      return i1.iI().lL(0, 100);
    }
    
    public String sendCmd(int param1Int1, int param1Int2, int param1Int3, String param1String) throws RemoteException {
      if (param1Int1 == 6)
        return i1.iI().LL(param1Int2, param1String); 
      if (param1Int1 == 7)
        return i1.iI().丨i1丨1i() ? (i1.iI().LlIl丨() ? ILil.IL1Iii.IL1Iii(new byte[] { 10 }, "8f7026") : ILil.IL1Iii.IL1Iii(new byte[] { 84 }, "ea22ea")) : ILil.IL1Iii.IL1Iii(new byte[] { 81 }, "a385e3"); 
      if (param1Int1 == 8) {
        try {
          if (!MainService.LlLI1(this.ILil)) {
            this.ILil.丨丨LLlI1(null, false);
            return ILil.IL1Iii.IL1Iii(new byte[] { 77, 16, 23, 3 }, "9bbfb3");
          } 
          return ILil.IL1Iii.IL1Iii(new byte[] { 7, 7, 15, 18, 6 }, "afcac7");
        } catch (Exception exception) {}
      } else {
        if (param1Int1 == 9) {
          if (MainService.LlLI1(this.ILil)) {
            this.ILil.Lil(3);
            return ILil.IL1Iii.IL1Iii(new byte[] { 18, 71, 19, 85 }, "f5f08a");
          } 
          return ILil.IL1Iii.IL1Iii(new byte[] { 87, 4, 10, 21, 80 }, "1eff56");
        } 
        if (param1Int1 == 10) {
          if (i1.iI().丨i1丨1i()) {
            if (param1Int2 == 0) {
              MainService.ll丨L1ii(this.ILil, true);
            } else {
              MainService.ll丨L1ii(this.ILil, false);
            } 
            return ILil.IL1Iii.IL1Iii(new byte[] { 16, 23, 65, 87 }, "de424e");
          } 
          return ILil.IL1Iii.IL1Iii(new byte[] { 82, 83, 85, 66, 86 }, "429134");
        } 
        if (param1Int1 == 11) {
          if (exception != null) {
            File file = new File();
            this((String)exception);
            if (file.exists()) {
              Ilil.ILil.IL1Iii.L丨1丨1丨I l丨1丨1丨I = Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I();
              LI丨丨l丨l lI丨丨l丨l = new LI丨丨l丨l();
              this(202, "", file.getAbsolutePath());
              l丨1丨1丨I.ILL(lI丨丨l丨l);
              return ILil.IL1Iii.IL1Iii(new byte[] { 21, 17, 64, 85 }, "ac50ed");
            } 
          } 
        } else if (param1Int1 == 12) {
          try {
            File file = new File();
            this((String)exception);
            if (file.exists()) {
              File file1 = new File();
              this(MainService.lI丨lii(this.ILil));
              if (file1.exists())
                file1.delete(); 
              if (I11li1.ILL(file.getAbsolutePath(), file1.getAbsolutePath()) && file1.exists()) {
                LlLI1.IL1Iii().Ilil(file1.lastModified(), (Context)this.ILil);
                return ILil.IL1Iii.IL1Iii(new byte[] { 69, 74, 65, 6 }, "184c43");
              } 
            } 
          } catch (Exception exception1) {
            exception1.printStackTrace();
          } 
        } 
      } 
      return null;
    }
  }
  
  public class L丨1丨1丨I implements Runnable {
    public final String IL1Iii;
    
    public final MainService ILil;
    
    public L丨1丨1丨I(MainService this$0, String param1String) {}
    
    public void run() {
      I11li1.LlLl111l((Context)this.ILil, this.IL1Iii);
    }
  }
  
  public class iI丨LLL1 implements Runnable {
    public final MainService IL1Iii;
    
    public iI丨LLL1(MainService this$0) {}
    
    public void run() {
      if (i1.iI().丨i1丨1i()) {
        if (MainService.iIi1(this.IL1Iii).getPauseState()) {
          MainService.iIi1(this.IL1Iii).setPauseState(false);
          i1.iI().丨IillIi();
        } else {
          MainService.iIi1(this.IL1Iii).setPauseState(true);
          i1.iI().lI1I1();
        } 
      } else {
        MainService.iIi1(this.IL1Iii).setPauseState(false);
      } 
    }
  }
  
  public class lIi丨I extends Binder {
    public final MainService IL1Iii;
    
    public lIi丨I(MainService this$0) {}
    
    public LL1IL IL1Iii() {
      return this.IL1Iii;
    }
  }
  
  public class l丨Li1LL implements Runnable {
    public final MainService IL1Iii;
    
    public l丨Li1LL(MainService this$0) {}
    
    public void run() {
      if (!MainService.LlLI1(this.IL1Iii))
        this.IL1Iii.丨丨LLlI1(null, true); 
    }
  }
  
  public class 丨il implements Runnable {
    public final boolean IL1Iii;
    
    public final MainService ILil;
    
    public 丨il(MainService this$0, boolean param1Boolean) {}
    
    public void run() {
      MainService.iIlLiL(this.ILil, this.IL1Iii);
    }
  }
}
