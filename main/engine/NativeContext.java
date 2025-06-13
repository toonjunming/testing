package com.main.engine;

import ILil.IL1Iii;
import ILil.I丨L.I丨L.I11li1;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NativeContext extends Context {
  private Context mContext = null;
  
  public NativeContext(Context paramContext) {
    this.mContext = paramContext;
  }
  
  public boolean bindService(Intent paramIntent, @NonNull ServiceConnection paramServiceConnection, int paramInt) {
    return false;
  }
  
  public int checkCallingOrSelfPermission(@NonNull String paramString) {
    return 0;
  }
  
  public int checkCallingOrSelfUriPermission(Uri paramUri, int paramInt) {
    return 0;
  }
  
  public int checkCallingPermission(@NonNull String paramString) {
    return 0;
  }
  
  public int checkCallingUriPermission(Uri paramUri, int paramInt) {
    return 0;
  }
  
  public int checkPermission(@NonNull String paramString, int paramInt1, int paramInt2) {
    return 0;
  }
  
  public int checkSelfPermission(@NonNull String paramString) {
    return 0;
  }
  
  public int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3) {
    return 0;
  }
  
  public int checkUriPermission(@Nullable Uri paramUri, @Nullable String paramString1, @Nullable String paramString2, int paramInt1, int paramInt2, int paramInt3) {
    return 0;
  }
  
  public void clearWallpaper() throws IOException {}
  
  public Context createConfigurationContext(@NonNull Configuration paramConfiguration) {
    return null;
  }
  
  public Context createContextForSplit(String paramString) throws PackageManager.NameNotFoundException {
    return null;
  }
  
  public Context createDeviceProtectedStorageContext() {
    return null;
  }
  
  public Context createDisplayContext(@NonNull Display paramDisplay) {
    return null;
  }
  
  public Context createPackageContext(String paramString, int paramInt) throws PackageManager.NameNotFoundException {
    return null;
  }
  
  public String[] databaseList() {
    return new String[0];
  }
  
  public boolean deleteDatabase(String paramString) {
    return false;
  }
  
  public boolean deleteFile(String paramString) {
    return false;
  }
  
  public boolean deleteSharedPreferences(String paramString) {
    return false;
  }
  
  public void enforceCallingOrSelfPermission(@NonNull String paramString1, @Nullable String paramString2) {}
  
  public void enforceCallingOrSelfUriPermission(Uri paramUri, int paramInt, String paramString) {}
  
  public void enforceCallingPermission(@NonNull String paramString1, @Nullable String paramString2) {}
  
  public void enforceCallingUriPermission(Uri paramUri, int paramInt, String paramString) {}
  
  public void enforcePermission(@NonNull String paramString1, int paramInt1, int paramInt2, @Nullable String paramString2) {}
  
  public void enforceUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, String paramString) {}
  
  public void enforceUriPermission(@Nullable Uri paramUri, @Nullable String paramString1, @Nullable String paramString2, int paramInt1, int paramInt2, int paramInt3, @Nullable String paramString3) {}
  
  public String[] fileList() {
    return new String[0];
  }
  
  public Context getApplicationContext() {
    return this.mContext.getApplicationContext();
  }
  
  public ApplicationInfo getApplicationInfo() {
    return null;
  }
  
  public AssetManager getAssets() {
    return this.mContext.getAssets();
  }
  
  public File getCacheDir() {
    return null;
  }
  
  public ClassLoader getClassLoader() {
    return this.mContext.getClassLoader();
  }
  
  public File getCodeCacheDir() {
    return null;
  }
  
  public ContentResolver getContentResolver() {
    return this.mContext.getContentResolver();
  }
  
  public File getDataDir() {
    return null;
  }
  
  public File getDatabasePath(String paramString) {
    return null;
  }
  
  public File getDir(String paramString, int paramInt) {
    return null;
  }
  
  @Nullable
  public File getExternalCacheDir() {
    return null;
  }
  
  public File[] getExternalCacheDirs() {
    return new File[0];
  }
  
  @Nullable
  public File getExternalFilesDir(@Nullable String paramString) {
    return null;
  }
  
  public File[] getExternalFilesDirs(String paramString) {
    return new File[0];
  }
  
  public File[] getExternalMediaDirs() {
    return new File[0];
  }
  
  public File getFileStreamPath(String paramString) {
    return null;
  }
  
  public File getFilesDir() {
    File file = new File((new File(I11li1.丨丨())).getParentFile(), IL1Iii.IL1Iii(new byte[] { 86, 92, 95, 85, 23 }, "0530dd"));
    if (!file.exists() || !file.isDirectory())
      file.mkdirs(); 
    return file;
  }
  
  public Looper getMainLooper() {
    return this.mContext.getMainLooper();
  }
  
  public File getNoBackupFilesDir() {
    return null;
  }
  
  public File getObbDir() {
    return null;
  }
  
  public File[] getObbDirs() {
    return new File[0];
  }
  
  public String getPackageCodePath() {
    return null;
  }
  
  public PackageManager getPackageManager() {
    return this.mContext.getPackageManager();
  }
  
  public String getPackageName() {
    return I11li1.丨1丨iIl();
  }
  
  public String getPackageResourcePath() {
    return null;
  }
  
  public Resources getResources() {
    return this.mContext.getResources();
  }
  
  public SharedPreferences getSharedPreferences(String paramString, int paramInt) {
    return null;
  }
  
  public Object getSystemService(@NonNull String paramString) {
    return null;
  }
  
  @Nullable
  public String getSystemServiceName(@NonNull Class<?> paramClass) {
    return null;
  }
  
  public Resources.Theme getTheme() {
    return null;
  }
  
  public Drawable getWallpaper() {
    return null;
  }
  
  public int getWallpaperDesiredMinimumHeight() {
    return 0;
  }
  
  public int getWallpaperDesiredMinimumWidth() {
    return 0;
  }
  
  public void grantUriPermission(String paramString, Uri paramUri, int paramInt) {}
  
  public boolean isDeviceProtectedStorage() {
    return false;
  }
  
  public boolean moveDatabaseFrom(Context paramContext, String paramString) {
    return false;
  }
  
  public boolean moveSharedPreferencesFrom(Context paramContext, String paramString) {
    return false;
  }
  
  public FileInputStream openFileInput(String paramString) throws FileNotFoundException {
    return null;
  }
  
  public FileOutputStream openFileOutput(String paramString, int paramInt) throws FileNotFoundException {
    return null;
  }
  
  public SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory) {
    return null;
  }
  
  public SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory, @Nullable DatabaseErrorHandler paramDatabaseErrorHandler) {
    return null;
  }
  
  public Drawable peekWallpaper() {
    return null;
  }
  
  @Nullable
  public Intent registerReceiver(@Nullable BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter) {
    return null;
  }
  
  @Nullable
  public Intent registerReceiver(@Nullable BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, int paramInt) {
    return null;
  }
  
  @Nullable
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, @Nullable String paramString, @Nullable Handler paramHandler) {
    return null;
  }
  
  @Nullable
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, @Nullable String paramString, @Nullable Handler paramHandler, int paramInt) {
    return null;
  }
  
  public void removeStickyBroadcast(Intent paramIntent) {}
  
  public void removeStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void revokeUriPermission(Uri paramUri, int paramInt) {}
  
  public void revokeUriPermission(String paramString, Uri paramUri, int paramInt) {}
  
  public void sendBroadcast(Intent paramIntent) {}
  
  public void sendBroadcast(Intent paramIntent, @Nullable String paramString) {}
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, @Nullable String paramString) {}
  
  public void sendOrderedBroadcast(Intent paramIntent, @Nullable String paramString) {}
  
  public void sendOrderedBroadcast(@NonNull Intent paramIntent, @Nullable String paramString1, @Nullable BroadcastReceiver paramBroadcastReceiver, @Nullable Handler paramHandler, int paramInt, @Nullable String paramString2, @Nullable Bundle paramBundle) {}
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, @Nullable String paramString1, BroadcastReceiver paramBroadcastReceiver, @Nullable Handler paramHandler, int paramInt, @Nullable String paramString2, @Nullable Bundle paramBundle) {}
  
  public void sendStickyBroadcast(Intent paramIntent) {}
  
  public void sendStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void sendStickyOrderedBroadcast(Intent paramIntent, BroadcastReceiver paramBroadcastReceiver, @Nullable Handler paramHandler, int paramInt, @Nullable String paramString, @Nullable Bundle paramBundle) {}
  
  public void sendStickyOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, BroadcastReceiver paramBroadcastReceiver, @Nullable Handler paramHandler, int paramInt, @Nullable String paramString, @Nullable Bundle paramBundle) {}
  
  public void setTheme(int paramInt) {}
  
  public void setWallpaper(Bitmap paramBitmap) throws IOException {}
  
  public void setWallpaper(InputStream paramInputStream) throws IOException {}
  
  public void startActivities(Intent[] paramArrayOfIntent) {}
  
  public void startActivities(Intent[] paramArrayOfIntent, Bundle paramBundle) {}
  
  public void startActivity(Intent paramIntent) {}
  
  public void startActivity(Intent paramIntent, @Nullable Bundle paramBundle) {}
  
  @Nullable
  public ComponentName startForegroundService(Intent paramIntent) {
    return null;
  }
  
  public boolean startInstrumentation(@NonNull ComponentName paramComponentName, @Nullable String paramString, @Nullable Bundle paramBundle) {
    return false;
  }
  
  public void startIntentSender(IntentSender paramIntentSender, @Nullable Intent paramIntent, int paramInt1, int paramInt2, int paramInt3) throws IntentSender.SendIntentException {}
  
  public void startIntentSender(IntentSender paramIntentSender, @Nullable Intent paramIntent, int paramInt1, int paramInt2, int paramInt3, @Nullable Bundle paramBundle) throws IntentSender.SendIntentException {}
  
  @Nullable
  public ComponentName startService(Intent paramIntent) {
    return null;
  }
  
  public boolean stopService(Intent paramIntent) {
    return false;
  }
  
  public void unbindService(@NonNull ServiceConnection paramServiceConnection) {}
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver) {}
}
