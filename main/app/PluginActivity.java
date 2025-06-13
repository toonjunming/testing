package com.main.app;

import ILil.IL1Iii;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PluginActivity extends AppCompatActivity {
  public Method I1I;
  
  public Method IL1Iii;
  
  public Method ILL;
  
  public Method ILil;
  
  public Method Ilil;
  
  public Method I丨L;
  
  public Method I丨iL;
  
  public Object Ll丨1;
  
  public Method L丨1丨1丨I;
  
  public Method iI丨LLL1;
  
  public Context lIi丨I;
  
  public Method l丨Li1LL;
  
  public Method 丨il;
  
  public final Method IL1Iii(Class<?> paramClass, String paramString, Class<?>... paramVarArgs) {
    while (paramClass != null) {
      try {
        return paramClass.getDeclaredMethod(paramString, paramVarArgs);
      } catch (NoSuchMethodException noSuchMethodException) {
        paramClass = paramClass.getSuperclass();
      } 
    } 
    return null;
  }
  
  public final void ILil() {
    try {
      Class<?> clazz = PluginService.getInstance().getApkLoader().Ilil(PluginService.getInstance().getPluginActivityName());
      this.IL1Iii = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 88, 91, 123, 64, 81, 86, 67, 80 }, "758247"), new Class[] { Activity.class });
      this.ILil = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 12, 11, 37, 16, 83, 2, 23, 0 }, "cefb6c"), new Class[] { Activity.class, Bundle.class, Context.class });
      this.I丨iL = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 86, 89, 118, 83, 71, 16, 75, 88, 75 }, "97264d"), new Class[0]);
      this.I丨L = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 5, 81, 8, 80, 21, 94 }, "c8f9f6"), new Class[0]);
      this.Ilil = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 13, 93, 101, 0, 71, 65, 15, 86 }, "b37e44"), new Class[0]);
      this.I1I = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 95, 86, 99, 88, 19, 71, 85 }, "0839f4"), new Class[0]);
      this.l丨Li1LL = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 94, 90, 99, 66, 89, 19, 69 }, "14068a"), new Class[0]);
      this.iI丨LLL1 = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 12, 11, 97, 77, 86, 21 }, "ce299e"), new Class[0]);
      String str = IL1Iii.IL1Iii(new byte[] { 90, 10, 115, 84, 27, 125, 90, 19, 86 }, "5d81b9");
      Class<int> clazz1 = int.class;
      this.L丨1丨1丨I = IL1Iii(clazz, str, new Class[] { clazz1, KeyEvent.class });
      this.丨il = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 
              88, 12, 100, 6, 19, 65, 82, 17, 66, 51, 
              7, 70, 90, 11, 69, 16, 11, 91, 89, 17, 
              100, 6, 17, 65, 91, 22 }, "7b6cb4"), new Class[] { clazz1, String[].class, int[].class });
      this.ILL = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 
              95, 94, 113, 87, 67, 12, 70, 89, 68, 77, 
              101, 0, 67, 69, 92, 64 }, "00047e"), new Class[] { clazz1, clazz1, Intent.class });
      this.Ll丨1 = clazz.newInstance();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void finish() {
    super.finish();
    try {
      PluginService.getInstance().setCurrentActivity(null);
    } catch (Exception exception) {}
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.I丨L;
        if (method != null)
          method.invoke(object, new Object[0]); 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, @Nullable Intent paramIntent) {
    super.onActivityResult(paramInt2, paramInt2, paramIntent);
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.ILL;
        if (method != null)
          method.invoke(object, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramIntent }); 
      } 
    } catch (Exception exception) {}
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492895);
    Resources resources = PluginService.getInstance().getApkLoader().I丨L();
    if (resources != null)
      try {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper();
        this(getBaseContext(), 0);
        this.lIi丨I = (Context)contextThemeWrapper;
        Class<?> clazz = contextThemeWrapper.getClass();
        Field field2 = clazz.getDeclaredField(IL1Iii.IL1Iii(new byte[] { 8, 102, 0, 74, 91, 70, 23, 87, 0, 74 }, "e4e943"));
        field2.setAccessible(true);
        field2.set(this.lIi丨I, resources);
        Field field1 = Class.forName(IL1Iii.IL1Iii(new byte[] { 
                2, 93, 15, 26, 84, 13, 14, 85, 14, 81, 
                29, 3, 15, 86, 16, 91, 90, 6, 79, 95, 
                3, 64, 86, 16, 8, 83, 14, 26, 97, 70, 
                18, 70, 27, 88, 86 }, "a2b43b")).getDeclaredField(IL1Iii.IL1Iii(new byte[] { 
                49, 89, 6, 85, 6, 62, 40, 80, 23, 93, 
                17, 8, 4, 93, 32, 87, 14, 17, 10, 95, 
                6, 86, 23, 18, 58, 125, 10, 95, 11, 21 }, "e1c8ca"));
        field1.setAccessible(true);
        Object object = field1.get((Object)null);
        if (object != null) {
          int i = ((Integer)object).intValue();
          Field field = clazz.getDeclaredField(IL1Iii.IL1Iii(new byte[] { 
                  88, 96, 94, 82, 14, 83, 103, 81, 69, 88, 
                  22, 68, 86, 81 }, "5467c6"));
          field.setAccessible(true);
          field.set(this.lIi丨I, Integer.valueOf(i));
        } 
      } catch (Exception exception) {} 
    try {
      PluginService.getInstance().setCurrentActivity(this);
    } catch (Exception exception) {}
    ILil();
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.IL1Iii;
        if (method != null || this.ILil != null) {
          View view;
          Method method1 = this.ILil;
          if (method1 != null) {
            view = (View)method1.invoke(object, new Object[] { this, paramBundle, this.lIi丨I });
          } else {
            view = (View)method.invoke(object, new Object[] { this });
          } 
          if (view != null) {
            FrameLayout frameLayout = findViewById(2131296567);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams();
            this(-1, -1);
            frameLayout.addView(view, (ViewGroup.LayoutParams)layoutParams);
          } 
        } 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void onDestroy() {
    super.onDestroy();
    try {
      PluginService.getInstance().setCurrentActivity(null);
    } catch (Exception exception) {}
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.I丨iL;
        if (method != null)
          method.invoke(object, new Object[0]); 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.L丨1丨1丨I;
        if (method != null) {
          boolean bool = ((Boolean)method.invoke(object, new Object[] { Integer.valueOf(paramInt), paramKeyEvent })).booleanValue();
          if (bool)
            return bool; 
        } 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public void onPause() {
    super.onPause();
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.I1I;
        if (method != null)
          method.invoke(object, new Object[0]); 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.丨il;
        if (method != null)
          method.invoke(object, new Object[] { Integer.valueOf(paramInt), paramArrayOfString, paramArrayOfint }); 
      } 
    } catch (Exception exception) {}
  }
  
  public void onResume() {
    super.onResume();
    try {
      Object object = this.Ll丨1;
      if (object != null) {
        Method method = this.Ilil;
        if (method != null)
          method.invoke(object, new Object[0]); 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void onStart() {
    super.onStart();
    try {
      Object object = this.Ll丨1;
      if (object != null && this.I1I != null)
        this.l丨Li1LL.invoke(object, new Object[0]); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void onStop() {
    super.onStop();
    try {
      Object object = this.Ll丨1;
      if (object != null && this.I1I != null)
        this.iI丨LLL1.invoke(object, new Object[0]); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}
