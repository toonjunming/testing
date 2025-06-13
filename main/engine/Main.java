package com.main.engine;

import ILil.I丨L.ILil.丨il;
import ILil.I丨L.I丨L.I11li1;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;

public class Main {
  public static 丨il mCoreService;
  
  public static void main(String[] paramArrayOfString) {
    Looper.prepareMainLooper();
    Map<String, String> map = System.getenv();
    boolean bool = false;
    try {
      int j = Integer.parseInt(map.get(ILil.IL1Iii.IL1Iii(new byte[] { 103, 59, 53, 124 }, "3be96b")));
      String str1 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 51, 46, Byte.MAX_VALUE }, "ce801f"));
      String str2 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 119, 98, 125 }, "626e0b"));
      String str4 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 110, 117, 51, 103, 45, 121, 118 }, "80a4d6"));
      String str7 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 111, 124, 55, 102, 49 }, "95e9fd"));
      String str6 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 55, 124, 106, 103, 43 }, "a588cf"));
      String str5 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 116, Byte.MAX_VALUE, 102, 52, 105, 52 }, "065d6c"));
      String str3 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 125, 123, 53, 103, 109, 45 }, "92f72e"));
      String str8 = map.get(ILil.IL1Iii.IL1Iii(new byte[] { 47, 40, 114, 124, 44, 49 }, "ca08ec"));
      int i = Integer.parseInt(str4);
      try {
        if (!TextUtils.isEmpty(str7) && !TextUtils.isEmpty(str6))
          I11li1.iIi丨(Integer.parseInt(str7), Integer.parseInt(str6)); 
        if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str3))
          I11li1.I1II(Integer.parseInt(str5), Integer.parseInt(str3)); 
      } catch (Exception exception1) {}
      try {
        I11li1.Ii丨丨Li(str2);
        I11li1.lIiILl丨1(j);
        I11li1.ii1(str1);
        I11li1.丨L1l(str8);
        if (TextUtils.isEmpty(str8)) {
          I11li1.lI11lLL(null, str1, str2, j);
        } else {
          I11li1.lI11lLL(str8, str1, str2, j);
        } 
        I11li1.LIiiILi(str1, str2, j);
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        stringBuilder1.append(str1);
        stringBuilder1.append(ILil.IL1Iii.IL1Iii(new byte[] { 13, 70, 1, 85, 90, 21, 82 }, "74d85a"));
        I11li1.llL丨1(str1, stringBuilder1.toString());
      } catch (Exception null) {}
    } catch (Exception exception) {
      boolean bool1 = bool;
    } 
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 124, 107 }, "237044");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 94, 15, 92, 65, 68, 4, 69, 19, 15 }, "7a55da"));
    stringBuilder.append(exception.toString());
    Log.i(str, stringBuilder.toString());
  }
  
  public static final class IL1Iii implements Runnable {
    public final int IL1Iii;
    
    public IL1Iii(int param1Int) {}
    
    public void run() {
      I11li1.ILil();
      丨il 丨il = new 丨il(this.IL1Iii);
      Main.mCoreService = 丨il;
      丨il.llI(I11li1.丨1丨iIl());
    }
  }
}
