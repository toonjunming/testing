package com.main.app;

import ILil.IL1Iii;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.io.File;

public final class ProjectActivity {
  public static void IL1Iii(Context paramContext, String paramString) {
    try {
      boolean bool;
      Intent intent;
      ContentResolver contentResolver = paramContext.getContentResolver();
      File file = new File();
      this(paramString);
      Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.fromFile(file)));
      if (bitmap.getWidth() > bitmap.getHeight()) {
        bool = false;
      } else {
        bool = true;
      } 
      if (!bitmap.isRecycled())
        bitmap.recycle(); 
      if (bool) {
        intent = new Intent();
        this(paramContext, ProjectPoritrait.class);
      } else {
        intent = new Intent(paramContext, ProjectLandsape.class);
      } 
      intent.putExtra(IL1Iii.IL1Iii(new byte[] { 72, 3, 17, 90 }, "8be2cc"), paramString);
      intent.setFlags(268435456);
      PendingIntent.getActivity(paramContext, 0, intent, 134217728).send();
    } catch (Exception exception) {}
  }
  
  public static class ProjectLandsape extends BaseActivity {
    public void onCreate(Bundle param1Bundle) {
      super.onCreate(param1Bundle);
      try {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
      } catch (Exception exception) {}
      IL1Iii();
    }
    
    public void onNewIntent(Intent param1Intent) {
      super.onNewIntent(param1Intent);
      setIntent(param1Intent);
      IL1Iii();
    }
  }
  
  public static class ProjectPoritrait extends BaseActivity {
    public void onCreate(Bundle param1Bundle) {
      super.onCreate(param1Bundle);
      try {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
      } catch (Exception exception) {}
      if (Build.VERSION.SDK_INT >= 19)
        getWindow().getDecorView().setSystemUiVisibility(4102); 
      IL1Iii();
    }
    
    public void onNewIntent(Intent param1Intent) {
      super.onNewIntent(param1Intent);
      setIntent(param1Intent);
      IL1Iii();
    }
  }
}
