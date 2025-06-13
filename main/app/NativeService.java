package com.main.app;

import ILil.I丨L.ILil.LL1IL;
import ILil.I丨L.ILil.L丨1丨1丨I;
import ILil.I丨L.ILil.ill1LI1l;
import ILil.I丨L.I丨L.I11li1;
import android.app.INXCoreNative;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.view.Display;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.main.engine.ScriptMain;
import com.nx.assist.lua.LuaEngine;

public class NativeService extends Service {
  public WindowManager I1I;
  
  public Intent IL1Iii = null;
  
  public int ILil = 0;
  
  public final void Ilil() {
    int i = this.ILil;
  }
  
  public void I丨L(int paramInt, String paramString) {
    (new Thread(new IL1Iii(this, paramInt, paramString))).start();
  }
  
  public void iI丨LLL1() {
    try {
      Process.killProcess(Process.myPid());
    } catch (Exception exception) {}
  }
  
  public final void l丨Li1LL(Context paramContext, int paramInt1, int paramInt2, String paramString1, String paramString2, Class<?> paramClass) {
    try {
      String str1 = paramContext.getPackageName();
      String str2 = paramContext.getPackageName();
      if (Build.VERSION.SDK_INT >= 26) {
        NotificationChannel notificationChannel = new NotificationChannel();
        this(str1, str2, 1);
        notificationChannel.setSound(null, null);
        ((NotificationManager)getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
                95, 95, 22, 93, 82, 81, 82, 81, 22, 93, 
                91, 86 }, "10b448"))).createNotificationChannel(notificationChannel);
      } 
      Intent intent = new Intent();
      this((Context)this, MainActivity.class);
      PendingIntent.getActivity((Context)this, 0, intent, 0);
      NotificationCompat.Builder builder = new NotificationCompat.Builder();
      this((Context)this, str1);
      startForeground(1, builder.setContentTitle(paramString1).setContentText(paramString2).setWhen(System.currentTimeMillis()).setSmallIcon(2131558414).setLargeIcon(BitmapFactory.decodeResource(getResources(), 2131558414)).build());
    } catch (Exception exception) {}
  }
  
  @Nullable
  public IBinder onBind(Intent paramIntent) {
    return (IBinder)new ILil(this);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    I11li1.LL丨((Context)this);
    Display display = this.I1I.getDefaultDisplay();
    ill1LI1l.I1I().IL1Iii(display.getRotation());
  }
  
  public void onCreate() {
    super.onCreate();
    l丨Li1LL((Context)this, (int)(System.currentTimeMillis() / 1000L), 2131558414, getResources().getString(2131689501), getResources().getString(2131689501), MainActivity.class);
    I11li1.LLI((Context)this);
    LuaEngine.Init((Context)this);
    this.I1I = (WindowManager)getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 17, 11, 86, 85, 92, 20 }, "fb813c"));
    I11li1.LL丨((Context)this);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    super.onStartCommand(paramIntent, paramInt1, paramInt2);
    if (paramIntent != null)
      paramIntent.getStringExtra(ILil.IL1Iii.IL1Iii(new byte[] { 80, 82, 21, 90, 14, 13 }, "11a3ac")); 
    return 1;
  }
  
  public class IL1Iii implements Runnable {
    public final NativeService I1I;
    
    public final int IL1Iii;
    
    public final String ILil;
    
    public IL1Iii(NativeService this$0, int param1Int, String param1String) {}
    
    public void run() {
      try {
        if (Thread.currentThread().getContextClassLoader() == null)
          Thread.currentThread().setContextClassLoader(NativeService.class.getClassLoader()); 
        int i = Native.getNativeType();
        ApplicationInfo applicationInfo = this.I1I.getApplicationInfo();
        String str1 = ILil.IL1Iii.IL1Iii(new byte[] { 82 }, "bcf498");
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        stringBuilder1.append(i);
        stringBuilder1.append("");
        String str2 = stringBuilder1.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        this();
        stringBuilder2.append(this.IL1Iii);
        stringBuilder2.append("");
        ScriptMain.main(new String[] { str1, str2, stringBuilder2.toString(), this.I1I.getPackageName(), applicationInfo.sourceDir, this.ILil });
      } catch (Exception exception) {}
      try {
        Process.killProcess(Process.myPid());
      } catch (Exception exception) {}
    }
  }
  
  public class ILil extends LL1IL.IL1Iii {
    public final NativeService ILil;
    
    public ILil(NativeService this$0) {}
    
    public void I1I(INXCoreNative param1INXCoreNative) throws RemoteException {
      L丨1丨1丨I.LL1IL().I1I(param1INXCoreNative);
    }
    
    public void ILL(Intent param1Intent) throws RemoteException {
      NativeService.I1I(this.ILil, param1Intent);
      NativeService.ILil(this.ILil);
    }
    
    public void I丨iL(int param1Int, String param1String) throws RemoteException {
      NativeService.IL1Iii(this.ILil, param1Int);
      this.ILil.I丨L(param1Int, param1String);
      NativeService.ILil(this.ILil);
    }
    
    public void Lil(int param1Int) throws RemoteException {
      this.ILil.iI丨LLL1();
    }
  }
}
