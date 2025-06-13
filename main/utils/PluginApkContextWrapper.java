package com.main.utils;

import ILil.IL1Iii;
import ILil.I丨L.I丨L.Lil;
import ILil.l丨Li1LL.IL1Iii.ILil;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

public class PluginApkContextWrapper extends ContextWrapper {
  private final Lil mApkLoader;
  
  private Resources.Theme mTheme;
  
  public PluginApkContextWrapper(Context paramContext, Lil paramLil) {
    super(paramContext);
    this.mApkLoader = paramLil;
    this.mTheme = paramLil.I丨L().newTheme();
  }
  
  private Context getBaseContextInner(Context paramContext) {
    while (paramContext instanceof ContextWrapper)
      paramContext = ((ContextWrapper)paramContext).getBaseContext(); 
    return paramContext;
  }
  
  public AssetManager getAssets() {
    return this.mApkLoader.IL1Iii();
  }
  
  public Context getBaseContext() {
    return getBaseContextInner(super.getBaseContext());
  }
  
  public ClassLoader getClassLoader() {
    return this.mApkLoader.I1I();
  }
  
  public Resources getResources() {
    return this.mApkLoader.I丨L();
  }
  
  public Object getSystemService(String paramString) {
    return IL1Iii.IL1Iii(new byte[] { 
          85, 83, 78, 93, 69, 66, 102, 91, 89, 84, 
          92, 87, 77, 87, 69 }, "927206").equals(paramString) ? ILil.IL1Iii(getBaseContext(), this.mApkLoader.I1I()).cloneInContext((Context)this) : super.getSystemService(paramString);
  }
  
  public Resources.Theme getTheme() {
    Resources.Theme theme2 = this.mTheme;
    Resources.Theme theme1 = theme2;
    if (theme2 == null)
      theme1 = super.getTheme(); 
    return theme1;
  }
  
  public void setTheme(int paramInt) {
    String str = IL1Iii.IL1Iii(new byte[] { 47, 108 }, "a40bff");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 69, 82, 23, 98, 81, 87, 91, 82, 94, 8 }, "67c692"));
    stringBuilder.append(paramInt);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 14 }, "505800"));
    stringBuilder.append(this.mTheme);
    Log.i(str, stringBuilder.toString());
    try {
      this.mTheme.applyStyle(paramInt, true);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}
