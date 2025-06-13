package com.main.engine;

import ILil.I丨L.ILil.iIilII1;
import ILil.I丨L.I丨L.I11li1;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;

public class ScriptProxy {
  private static Handler mMainHandler;
  
  private static iIilII1 mProxy;
  
  public static void main(String[] paramArrayOfString) {
    try {
      int i = Integer.parseInt(paramArrayOfString[0]);
      String str2 = paramArrayOfString[1];
      String str1 = paramArrayOfString[2];
      I11li1.ii1(str2);
      I11li1.lIiILl丨1(i);
      I11li1.lIlIl(str2, str1, i);
      Map<String, String> map = System.getenv();
      if (map != null) {
        String str4 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 103, 43, 106, 61, 49 }, "1b8bfd"));
        String str5 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 100, 47, 103, 61, 124 }, "2f5b43"));
        try {
          if (!TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str5))
            I11li1.iIi丨(Integer.parseInt(str4), Integer.parseInt(str5)); 
        } catch (Exception exception) {}
        str4 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 125, 112, 96, 101, 107, 49 }, "99354f"));
        String str3 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 38, Byte.MAX_VALUE, 96, 97, 61, 41 }, "b631ba"));
        try {
          if (!TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str3))
            I11li1.I1II(Integer.parseInt(str4), Integer.parseInt(str3)); 
        } catch (Exception exception) {}
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 8, 68, 66, 88, 25, 65 }, "2407a8"));
      I11li1.llL丨1(str2, stringBuilder.toString());
      if (Looper.getMainLooper() == null) {
        Looper.prepareMainLooper();
      } else {
        Looper.prepare();
      } 
      Handler handler = new Handler();
      mMainHandler = handler;
      handler.post(new IL1Iii());
      Looper.loop();
      return;
    } catch (Exception exception) {
      Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 121, 110 }, "7620b9"), exception.toString());
      return;
    } 
  }
  
  public static final class IL1Iii implements Runnable {
    public void run() {
      ScriptProxy.access$002(new iIilII1(I11li1.丨1丨iIl()));
      ScriptProxy.mProxy.丨l丨();
    }
  }
}
