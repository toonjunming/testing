package com.main.engine;

import ILil.I丨L.ILil.LlLiL丨L丨;
import ILil.I丨L.ILil.L丨1l;
import ILil.I丨L.ILil.L丨1丨1丨I;
import ILil.I丨L.I丨L.I11li1;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.nx.assist.lua.LuaEngine;
import java.util.Map;

public class ScriptMain {
  private static L丨1丨1丨I mCoreScript;
  
  private static Handler mMainHandler;
  
  public static void main(String[] paramArrayOfString) {
    Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 120, 61 }, "6e24ed"), ILil.IL1Iii.IL1Iii(new byte[] { 
            89, 8, 14, 4, 92, 4, 89, 8, 14, 4, 
            92, 4, 89, 8, 14, 84, 0, 80, 10, 8, 
            14, 4, 92, 4, 89, 8, 14, 4, 92, 4, 
            89, 8, 14, 4, 92, 4, 89, 8, 14 }, "d539a9"));
    try {
      int i = Integer.parseInt(paramArrayOfString[0]);
      int k = Integer.parseInt(paramArrayOfString[1]);
      int j = Integer.parseInt(paramArrayOfString[2]);
      String str3 = paramArrayOfString[3];
      String str2 = paramArrayOfString[4];
      String str1 = paramArrayOfString[5];
      I11li1.Ii丨丨Li(str2);
      I11li1.ii1(str3);
      I11li1.lIiILl丨1(k);
      if (i == 1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str3);
        stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 91, 71, 87 }, "a4461e"));
        I11li1.llL丨1(str3, stringBuilder.toString());
      } 
      if (Looper.getMainLooper() == null) {
        Looper.prepareMainLooper();
      } else {
        Looper.prepare();
      } 
      if (i == 1) {
        I11li1.ILil();
        LuaEngine.Init(I11li1.丨ili丨());
        L丨1l.I1I().Ll丨1(I11li1.丨ili丨());
        Map<String, String> map = System.getenv();
        if (map != null) {
          String str5 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 103, 126, 102, 60, 100 }, "174c35"));
          String str6 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 51, 122, 96, 108, 44 }, "e323d1"));
          try {
            if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str6))
              I11li1.iIi丨(Integer.parseInt(str5), Integer.parseInt(str6)); 
          } catch (Exception exception) {}
          str5 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 112, 45, 54, 100, 58, 50 }, "4de4ee"));
          str6 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 125, 45, 106, 98, 58, 121 }, "9d92e1"));
          try {
            if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str6))
              I11li1.I1II(Integer.parseInt(str5), Integer.parseInt(str6)); 
          } catch (Exception exception) {}
          str5 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 99, 45, 37, 98, 123, 41, 116, 38 }, "0cd26f"));
          str6 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 48, 115, 51 }, "c0deca"));
          String str4 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 96, 119, 41 }, "34a0cd"));
          try {
            if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str6)) {
              boolean bool = Boolean.parseBoolean(str5);
              int n = Integer.parseInt(str6);
              int m = Integer.parseInt(str4);
              if (bool)
                LlLiL丨L丨.IL1Iii().ILil(n, m); 
            } 
          } catch (Exception exception) {}
        } 
      } 
      mCoreScript = new L丨1丨1丨I(i, k, j, str3, str2, str1);
      Handler handler = new Handler();
      mMainHandler = handler;
      handler.post(new IL1Iii());
      Looper.loop();
    } catch (Exception exception) {}
  }
  
  public static final class IL1Iii implements Runnable {
    public void run() {
      ScriptMain.mCoreScript.l丨丨i11();
    }
  }
}
