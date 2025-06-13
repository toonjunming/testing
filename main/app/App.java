package com.main.app;

import ILil.IL1Iii;
import ILil.I丨L.I丨L.I11li1;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.tencent.smtt.sdk.QbSdk;
import java.util.HashMap;
import java.util.Map;

public class App extends Application implements Application.ActivityLifecycleCallbacks {
  public static App ILil = null;
  
  public Activity IL1Iii;
  
  public static App ILil() {
    return ILil;
  }
  
  public void I1I() {
    if (getResources().getInteger(2131361808) == 1) {
      Intent intent = new Intent((Context)this, MainService.class);
      intent.putExtra(IL1Iii.IL1Iii(new byte[] { 2, 0, 66, 80, 12, 12 }, "cc69cb"), IL1Iii.IL1Iii(new byte[] { 74, 17, 0, 65, 69 }, "9ea316"));
      intent.setFlags(268435456);
      startService(intent);
    } 
  }
  
  public Activity IL1Iii() {
    return this.IL1Iii;
  }
  
  public void Ilil() {
    Intent intent = new Intent((Context)this, MainService.class);
    intent.setFlags(268435456);
    intent.putExtra(IL1Iii.IL1Iii(new byte[] { 7, 82, 23, 92, 88, 13 }, "f1c57c"), IL1Iii.IL1Iii(new byte[] { 15, 11, 90, 77, 88, 21, 22 }, "fe399e"));
    startService(intent);
  }
  
  public final void I丨L(Activity paramActivity) {
    this.IL1Iii = paramActivity;
  }
  
  public void attachBaseContext(Context paramContext) {
    super.attachBaseContext(paramContext);
  }
  
  public void onActivityCreated(Activity paramActivity, Bundle paramBundle) {
    I丨L(paramActivity);
  }
  
  public void onActivityDestroyed(Activity paramActivity) {
    if (IL1Iii() == paramActivity)
      I丨L(null); 
  }
  
  public void onActivityPaused(Activity paramActivity) {
    if (IL1Iii() == paramActivity)
      I丨L(null); 
  }
  
  public void onActivityResumed(Activity paramActivity) {
    I丨L(paramActivity);
  }
  
  public void onActivitySaveInstanceState(@NonNull Activity paramActivity, @NonNull Bundle paramBundle) {
    I丨L(paramActivity);
  }
  
  public void onActivityStarted(Activity paramActivity) {
    I丨L(paramActivity);
  }
  
  public void onActivityStopped(Activity paramActivity) {
    if (IL1Iii() == paramActivity)
      I丨L(null); 
  }
  
  public void onCreate() {
    super.onCreate();
    ILil = this;
    I11li1.IL1Iii(this);
    registerActivityLifecycleCallbacks(this);
    try {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this();
      hashMap.put(IL1Iii.IL1Iii(new byte[] { 
              76, 69, 1, 60, 67, 69, 92, 83, 0, 26, 
              111, 86, 85, 87, 23, 16, 92, 90, 88, 82, 
              1, 17 }, "96dc05"), Boolean.TRUE);
      QbSdk.initTbsSettings((Map)hashMap);
      QbSdk.initX5Environment((Context)this, null);
    } catch (Exception exception) {}
  }
  
  static {
    System.loadLibrary(IL1Iii.IL1Iii(new byte[] { 6, 19, 74, 60, 16, 92, 4, 74, 4, 7 }, "e8acc4"));
    System.loadLibrary(IL1Iii.IL1Iii(new byte[] { 91, 3, 68, 13, 67, 0 }, "5b0d5e"));
    System.loadLibrary(IL1Iii.IL1Iii(new byte[] { 89, 95, 81, 19, 65, 11, 92, 65 }, "026f5b"));
  }
}
