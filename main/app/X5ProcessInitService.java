package com.main.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.tencent.smtt.sdk.QbSdk;

public class X5ProcessInitService extends Service {
  public static final String IL1Iii = ILil.IL1Iii.IL1Iii(new byte[] { 
        108, 86, 99, 75, 88, 82, 81, 16, 64, 112, 
        89, 88, 64, 48, 86, 75, 65, 88, 87, 6 }, "4c3971");
  
  @Nullable
  public IBinder onBind(Intent paramIntent) {
    Log.i(IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
            96, 0, 74, 64, 95, 86, 86, 69, 119, 88, 
            116, 92, 93, 1 }, "3e8665"));
    return null;
  }
  
  public void onCreate() {
    QbSdk.preInit(getApplicationContext(), new IL1Iii(this));
  }
  
  public class IL1Iii implements QbSdk.PreInitCallback {
    public IL1Iii(X5ProcessInitService this$0) {}
    
    public void onCoreInitFinished() {}
    
    public void onViewInitFinished(boolean param1Boolean) {
      String str = ILil.IL1Iii.IL1Iii(new byte[] { 
            110, 81, 101, 17, 90, 5, 83, 23, 70, 42, 
            91, 15, 66, 55, 80, 17, 67, 15, 85, 1 }, "6d5c5f");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              80, 86, 90, 71, 21, 79, 92, 90, 19, 67, 
              71, 87, 90, 93, 64, 64, 21, 64, 12, 2, 
              19 }, "983358"));
      stringBuilder.append(param1Boolean);
      Log.i(str, stringBuilder.toString());
    }
  }
}
