package com.main.engine;

import ILil.I丨L.ILil.L1iI1;
import ILil.I丨L.ILil.iI丨Li丨lI;
import ILil.I丨L.I丨L.I11li1;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.Map;

public class UiAutoMain {
  private static Handler mMainHandler;
  
  private static iI丨Li丨lI mUiAutoProxy;
  
  public static void main(String[] paramArrayOfString) {
    Looper.prepareMainLooper();
    String str = "";
    Map<String, String> map = System.getenv();
    try {
      String str1 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 50, 125, Byte.MAX_VALUE }, "b682e6"));
      try {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(str1);
        stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 8, 87, 20, 67, 90 }, "26a75c"));
        String str2 = stringBuilder.toString();
        I11li1.ii1(str1);
        I11li1.llL丨1(str1, str2);
        str2 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 111, 121, 102, 110, 101 }, "90412b"));
        String str5 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 96, 40, 98, 59, 122 }, "6a0d28"));
        String str3 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 117, 125, 103, 54, 57, 100 }, "144ff3"));
        String str4 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 113, 47, 97, 51, 109, 43 }, "5f2c2c"));
        try {
          if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str5))
            I11li1.iIi丨(Integer.parseInt(str2), Integer.parseInt(str5)); 
        } catch (Exception null) {}
        str2 = str1;
        try {
          if (!TextUtils.isEmpty(str3)) {
            str2 = str1;
            if (!TextUtils.isEmpty(str4)) {
              I11li1.I1II(Integer.parseInt(str3), Integer.parseInt(str4));
              str2 = str1;
            } 
          } 
        } catch (Exception exception) {
          String str6 = str1;
        } 
      } catch (Exception exception2) {}
    } catch (Exception exception1) {
      exception1 = exception2;
    } 
    exception2 = exception1;
    mUiAutoProxy = new iI丨Li丨lI((String)exception2);
    mMainHandler = new Handler();
    L1iI1.I1I();
    mMainHandler.post(new IL1Iii());
    Looper.loop();
  }
  
  public static final class IL1Iii implements Runnable {
    public void run() {
      UiAutoMain.mUiAutoProxy.I11L();
    }
  }
}
