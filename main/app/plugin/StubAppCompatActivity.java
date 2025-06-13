package com.main.app.plugin;

import ILil.IL1Iii;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.main.app.PluginService;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StubAppCompatActivity extends AppCompatActivity {
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
        String str = IL1Iii.IL1Iii(new byte[] { 116, 102, 54, 41 }, "84fe4f");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 6, 90, 76, 68, 81, 75, 23, 12 }, "e66d49"));
        stringBuilder.append(paramClass);
        Log.i(str, stringBuilder.toString());
      } 
    } 
    return null;
  }
  
  public final void ILil() {
    try {
      String str1 = getIntent().getStringExtra(IL1Iii.IL1Iii(new byte[] { 
              23, 4, 22, 6, 80, 22, 0, 9, 5, 18, 
              70 }, "ceda5b"));
      Class<?> clazz = PluginService.getInstance().getApkLoader().Ilil(str1);
      this.IL1Iii = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 93, 11, 119, 64, 84, 87, 70, 0 }, "2e4216"), new Class[] { Activity.class });
      this.ILil = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 95, 86, 118, 23, 80, 82, 68, 93 }, "085e53"), new Class[] { Activity.class, Bundle.class, Context.class });
      this.I丨iL = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 13, 10, 112, 84, 17, 77, 16, 11, 77 }, "bd41b9"), new Class[0]);
      this.I丨L = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 82, 10, 92, 80, 21, 90 }, "4c29f2"), new Class[0]);
      this.Ilil = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 14, 94, 54, 84, 23, 17, 12, 85 }, "a0d1dd"), new Class[0]);
      this.I1I = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 91, 15, 96, 87, 19, 18, 81 }, "4a06fa"), new Class[0]);
      this.l丨Li1LL = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 89, 93, 101, 65, 87, 69, 66 }, "636567"), new Class[0]);
      this.iI丨LLL1 = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 11, 88, 97, 21, 11, 68 }, "d62ad4"), new Class[0]);
      String str2 = IL1Iii.IL1Iii(new byte[] { 10, 95, 121, 81, 74, 32, 10, 70, 92 }, "e1243d");
      Class<int> clazz1 = int.class;
      this.L丨1丨1丨I = IL1Iii(clazz, str2, new Class[] { clazz1, KeyEvent.class });
      this.丨il = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 
              10, 90, 96, 7, 64, 70, 0, 71, 70, 50, 
              84, 65, 8, 93, 65, 17, 88, 92, 11, 71, 
              96, 7, 66, 70, 9, 64 }, "e42b13"), new Class[] { clazz1, String[].class, int[].class });
      this.ILL = IL1Iii(clazz, IL1Iii.IL1Iii(new byte[] { 
              12, 10, 114, 87, 66, 11, 21, 13, 71, 77, 
              100, 7, 16, 17, 95, 64 }, "cd346b"), new Class[] { clazz1, clazz1, Intent.class });
      this.Ll丨1 = clazz.newInstance();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void finish() {
    super.finish();
    try {
      PluginService.getInstance().removeActivity(this);
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
        Field field2 = clazz.getDeclaredField(IL1Iii.IL1Iii(new byte[] { 12, 102, 83, 69, 91, 69, 19, 87, 83, 69 }, "a46640"));
        field2.setAccessible(true);
        field2.set(this.lIi丨I, resources);
        Field field1 = Class.forName(IL1Iii.IL1Iii(new byte[] { 
                2, 10, 90, 74, 80, 86, 14, 2, 91, 1, 
                25, 88, 15, 1, 69, 11, 94, 93, 79, 8, 
                86, 16, 82, 75, 8, 4, 91, 74, 101, 29, 
                18, 17, 78, 8, 82 }, "ae7d79")).getDeclaredField(IL1Iii.IL1Iii(new byte[] { 
                101, 91, 0, 93, 4, 60, 124, 82, 17, 85, 
                19, 10, 80, 95, 38, 95, 12, 19, 94, 93, 
                0, 94, 21, 16, 110, Byte.MAX_VALUE, 12, 87, 9, 23 }, "13e0ac"));
        field1.setAccessible(true);
        Object object = field1.get((Object)null);
        if (object != null) {
          int i = ((Integer)object).intValue();
          Field field = clazz.getDeclaredField(IL1Iii.IL1Iii(new byte[] { 
                  88, 96, 10, 7, 15, 81, 103, 81, 17, 13, 
                  23, 70, 86, 81 }, "54bbb4"));
          field.setAccessible(true);
          field.set(this.lIi丨I, Integer.valueOf(i));
        } 
      } catch (Exception exception) {} 
    try {
      PluginService.getInstance().recordActivity(this);
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
