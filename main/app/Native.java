package com.main.app;

import ILil.IL1Iii;
import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.ILil.i1;
import ILil.I丨L.ILil.iI;
import ILil.I丨L.I丨L.丨il;
import Ilil.ILil.IL1Iii.L丨1丨1丨I;
import android.content.Context;
import android.graphics.Bitmap;
import com.main.engine.RotateMgr;
import java.io.ByteArrayOutputStream;

public class Native {
  public static native boolean checkIsDebug();
  
  public static native int checkProxy();
  
  public static native boolean chmod(String paramString);
  
  public static native boolean chmodExe(String paramString);
  
  public static native boolean connectService(String paramString, int paramInt, boolean paramBoolean);
  
  public static native boolean createSymbolicLink(String paramString1, String paramString2);
  
  public static native int getNativeType();
  
  public static String getNodeXml(int paramInt) {
    return i1.iI().l1Lll(paramInt);
  }
  
  public static native String getResult(String paramString1, String paramString2, String paramString3);
  
  public static int getVersion() {
    return 202;
  }
  
  public static native int init(Context paramContext);
  
  public static native int initNative();
  
  public static boolean isPrepare() {
    boolean bool;
    if (MainService.llliI() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static String onEvent(int paramInt, String paramString1, String paramString2) {
    if ((paramInt >= 1 && paramInt < 4) || paramInt == 7 || paramInt == 207 || paramInt == 208) {
      L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(paramInt, paramString1, paramString2));
      return (paramInt == 3) ? ((MainService.llliI() != null && MainService.llliI().丨丨()) ? IL1Iii.IL1Iii(new byte[] { 5 }, "7cdfae") : IL1Iii.IL1Iii(new byte[] { 5 }, "679d79")) : IL1Iii.IL1Iii(new byte[] { 6 }, "7b1af2");
    } 
    return (paramInt == 4) ? 丨il.I1I() : "";
  }
  
  public static byte[] onSnapShot(int paramInt1, int paramInt2, int paramInt3) {
    return i1.iI().L1iI1(paramInt2, paramInt3);
  }
  
  public static native int registerDebugService(int paramInt);
  
  public static native int registerService();
  
  public static native int runRootEngine(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public static native boolean sendEvent(int paramInt1, int paramInt2, String paramString1, String paramString2);
  
  public static native void setServiceOk();
  
  public static Bitmap snapShot(int paramInt1, int paramInt2, int paramInt3) {
    return iI.IL丨丨l(paramInt1, paramInt2, RotateMgr.Ilil().ILil());
  }
  
  public static byte[] snapShotFile(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    Bitmap bitmap = iI.IL丨丨l(paramInt1, paramInt2, RotateMgr.Ilil().ILil());
    if (bitmap != null)
      try {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this();
        if (paramInt4 == 0) {
          bitmap.compress(Bitmap.CompressFormat.PNG, paramInt5, byteArrayOutputStream);
        } else {
          bitmap.compress(Bitmap.CompressFormat.JPEG, paramInt5, byteArrayOutputStream);
        } 
        return byteArrayOutputStream.toByteArray();
      } catch (Exception exception) {
      
      } finally {
        if (bitmap != null && !bitmap.isRecycled())
          bitmap.recycle(); 
      }  
    return null;
  }
  
  public static native boolean takeCaptureFile(String paramString, int paramInt1, int paramInt2, int paramInt3);
  
  public static native int unZip(String paramString1, String paramString2);
  
  public static native int unZipFd(int paramInt, String paramString);
}
