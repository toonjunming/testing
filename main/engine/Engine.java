package com.main.engine;

import ILil.IL1Iii;
import ILil.I丨L.ILil.I丨;
import ILil.I丨L.ILil.I丨Ii;
import ILil.I丨L.ILil.L丨1丨1丨I;
import ILil.I丨L.I丨L.I11L;
import ILil.I丨L.I丨L.I11li1;
import ILil.I丨L.I丨L.L丨1丨1丨I;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.nx.assist.lua.IOnExitCallBack;
import com.nx.assist.lua.LuaEngine;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class Engine {
  private static Map<Integer, Long> mCacheLong;
  
  private static Map<Integer, String> mCacheStr = new HashMap<Integer, String>();
  
  private static IOnExitCallBack mOnExitCallBack;
  
  static {
    mCacheLong = new HashMap<Integer, Long>();
    mOnExitCallBack = null;
  }
  
  public static native void debugAction(String paramString);
  
  private static boolean extractApkAssets(String paramString1, String paramString2) {
    String str = I11li1.丨丨();
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 
              84, 74, 65, 85, 70, 66, 26, 75, 87, 67, 
              29 }, "592021"));
      stringBuilder.append(paramString1);
      return I11li1.i丨L丨iiI(str, stringBuilder.toString(), paramString2, paramString1);
    } catch (Exception exception) {
      paramString2 = IL1Iii.IL1Iii(new byte[] { 120, 62 }, "6faf95");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 
              92, 75, 22, 69, 3, 87, 77, 114, 18, 92, 
              35, 71, 74, 86, 22, 68, 66, 81, 75, 65, 
              95, 9 }, "93b7b4"));
      stringBuilder.append(exception.toString());
      L丨1丨1丨I.I1I(paramString2, stringBuilder.toString());
      return false;
    } 
  }
  
  public static native int init(Object paramObject1, Object paramObject2, Object paramObject3);
  
  public static boolean installLR(String paramString) {
    try {
      sendExit(0);
      return L丨1丨1丨I.LL1IL().llI(paramString);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isMatch(String paramString1, String paramString2) {
    boolean bool;
    String str = paramString1;
    if (paramString1 == null)
      str = ""; 
    paramString1 = paramString2;
    if (paramString2 == null)
      paramString1 = ""; 
    if (str != null && str.matches(paramString1)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static native String notifyEvent(int paramInt, String paramString);
  
  public static int obtainImage(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    try {
      return L丨1丨1丨I.LL1IL().丨丨丨丨(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3, paramInt1, paramInt2, paramInt3, paramInt4);
    } catch (Exception exception) {
      return -1;
    } 
  }
  
  public static byte[] obtainImageBytes(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    Bitmap bitmap = LuaEngine.snapShot(paramInt1, paramInt2, paramInt3, paramInt4);
    return (bitmap != null) ? I11li1.illl(bitmap, paramInt5, paramInt6) : null;
  }
  
  public static Bitmap obtainImageEx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #6
    //   3: invokestatic getSnapCacheBitmap : ()Landroid/graphics/Bitmap;
    //   6: astore #5
    //   8: aload #5
    //   10: ifnonnull -> 145
    //   13: invokestatic IL1Iii : ()LILil/I丨L/ILil/LlLiL丨L丨;
    //   16: iload_0
    //   17: iload_1
    //   18: iload_2
    //   19: iload_3
    //   20: invokevirtual I1I : (IIII)Landroid/graphics/Bitmap;
    //   23: astore #5
    //   25: aload #4
    //   27: iconst_0
    //   28: iconst_1
    //   29: iastore
    //   30: aload #5
    //   32: astore #4
    //   34: aload #5
    //   36: invokevirtual getConfig : ()Landroid/graphics/Bitmap$Config;
    //   39: invokevirtual toString : ()Ljava/lang/String;
    //   42: bipush #8
    //   44: newarray byte
    //   46: dup
    //   47: iconst_0
    //   48: bipush #123
    //   50: bastore
    //   51: dup
    //   52: iconst_1
    //   53: bipush #115
    //   55: bastore
    //   56: dup
    //   57: iconst_2
    //   58: bipush #99
    //   60: bastore
    //   61: dup
    //   62: iconst_3
    //   63: bipush #116
    //   65: bastore
    //   66: dup
    //   67: iconst_4
    //   68: bipush #100
    //   70: bastore
    //   71: dup
    //   72: iconst_5
    //   73: bipush #36
    //   75: bastore
    //   76: dup
    //   77: bipush #6
    //   79: bipush #97
    //   81: bastore
    //   82: dup
    //   83: bipush #7
    //   85: bipush #119
    //   87: bastore
    //   88: ldc '32103e'
    //   90: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   93: invokevirtual compareTo : (Ljava/lang/String;)I
    //   96: ifne -> 142
    //   99: aload #5
    //   101: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
    //   104: iconst_1
    //   105: invokevirtual copy : (Landroid/graphics/Bitmap$Config;Z)Landroid/graphics/Bitmap;
    //   108: astore #7
    //   110: aload #5
    //   112: invokevirtual isRecycled : ()Z
    //   115: ifne -> 130
    //   118: aload #5
    //   120: invokevirtual recycle : ()V
    //   123: aload #6
    //   125: astore #4
    //   127: goto -> 134
    //   130: aload #5
    //   132: astore #4
    //   134: aload #7
    //   136: ifnull -> 142
    //   139: aload #7
    //   141: areturn
    //   142: aload #4
    //   144: areturn
    //   145: aload #4
    //   147: iconst_0
    //   148: iconst_0
    //   149: iastore
    //   150: aload #5
    //   152: areturn
    //   153: astore #4
    //   155: aconst_null
    //   156: areturn
    // Exception table:
    //   from	to	target	type
    //   3	8	153	java/lang/Exception
    //   13	25	153	java/lang/Exception
    //   34	123	153	java/lang/Exception
  }
  
  public static String obtainXml(long paramLong) {
    Bundle bundle = L丨1丨1丨I.LL1IL().丨丨LLlI1();
    return (bundle != null) ? I11li1.I1i1LL1(bundle) : "";
  }
  
  public static void onExitCallBack(boolean paramBoolean, int paramInt) {
    String str = IL1Iii.IL1Iii(new byte[] { 126, 108 }, "04d240");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 
            87, 94, 113, 25, 89, 66, 123, 81, 88, 13, 
            114, 87, 91, 91, 5, 73 }, "804a06"));
    stringBuilder.append(mOnExitCallBack);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 11 }, "08369b"));
    stringBuilder.append(paramBoolean);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 29 }, "1b3b5a"));
    stringBuilder.append(paramInt);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 75 }, "b3834f"));
    Log.i(str, stringBuilder.toString());
    sendExit(paramInt);
    IOnExitCallBack iOnExitCallBack = mOnExitCallBack;
    if (iOnExitCallBack != null)
      iOnExitCallBack.onExitCallback(paramBoolean, paramInt); 
  }
  
  public static native void onTouchEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public static native String paddleOcr(Object paramObject);
  
  public static native int pause();
  
  public static native String pluginEvent(int paramInt, String paramString);
  
  public static void printLog(int paramInt, String paramString1, String paramString2) {
    try {
      L丨1丨1丨I.LL1IL().I1L丨11L(paramInt, paramString1, paramString2);
    } catch (Exception exception) {}
  }
  
  public static void registerExitCallback(IOnExitCallBack paramIOnExitCallBack) {
    mOnExitCallBack = paramIOnExitCallBack;
  }
  
  public static native void reset();
  
  public static boolean restart() {
    boolean bool1;
    boolean bool2 = false;
    try {
      sendExit(0);
      bool1 = L丨1丨1丨I.LL1IL().l丨liiI1();
    } catch (Exception exception) {
      bool1 = bool2;
    } 
    return bool1;
  }
  
  public static native int resume();
  
  public static native String sendEvent(int paramInt1, int paramInt2, String paramString);
  
  public static long sendEventWithLong(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #7
    //   3: iconst_1
    //   4: istore #5
    //   6: iload_0
    //   7: iconst_1
    //   8: if_icmpne -> 15
    //   11: invokestatic currentTimeMillis : ()J
    //   14: lreturn
    //   15: iload_0
    //   16: iconst_2
    //   17: if_icmpne -> 325
    //   20: iconst_0
    //   21: istore #6
    //   23: iload_1
    //   24: bipush #6
    //   26: if_icmpne -> 56
    //   29: invokestatic ii丨丨LL : ()Z
    //   32: ifeq -> 171
    //   35: iload #6
    //   37: istore #5
    //   39: iload_2
    //   40: iconst_1
    //   41: if_icmpne -> 47
    //   44: iconst_1
    //   45: istore #5
    //   47: iload #5
    //   49: invokestatic ii : (Z)I
    //   52: pop
    //   53: goto -> 171
    //   56: iload_1
    //   57: bipush #7
    //   59: if_icmpne -> 82
    //   62: iload_2
    //   63: iconst_1
    //   64: if_icmpne -> 70
    //   67: goto -> 73
    //   70: iconst_0
    //   71: istore #5
    //   73: iload #5
    //   75: invokestatic Li1iLi : (Z)I
    //   78: istore_0
    //   79: iload_0
    //   80: i2l
    //   81: lreturn
    //   82: iload_1
    //   83: iconst_5
    //   84: if_icmpne -> 117
    //   87: invokestatic ii丨丨LL : ()Z
    //   90: ifeq -> 171
    //   93: iload_2
    //   94: iconst_1
    //   95: if_icmpne -> 105
    //   98: iload #7
    //   100: istore #5
    //   102: goto -> 108
    //   105: iconst_0
    //   106: istore #5
    //   108: iload #5
    //   110: invokestatic LLlIiil : (Z)I
    //   113: istore_0
    //   114: goto -> 79
    //   117: iload_1
    //   118: bipush #9
    //   120: if_icmpne -> 171
    //   123: invokestatic LL1IL : ()LILil/I丨L/ILil/L丨1丨1丨I;
    //   126: invokevirtual L丨1l : ()Z
    //   129: ifeq -> 171
    //   132: invokestatic ii丨丨LL : ()Z
    //   135: ifeq -> 171
    //   138: aload_3
    //   139: invokestatic i丨LI : (Ljava/lang/String;)Ljava/lang/String;
    //   142: astore_3
    //   143: aload_3
    //   144: ifnull -> 169
    //   147: aload_3
    //   148: iconst_1
    //   149: newarray byte
    //   151: dup
    //   152: iconst_0
    //   153: iconst_4
    //   154: bastore
    //   155: ldc_w '54b08c'
    //   158: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   161: invokevirtual equals : (Ljava/lang/Object;)Z
    //   164: ifeq -> 169
    //   167: lconst_1
    //   168: lreturn
    //   169: lconst_0
    //   170: lreturn
    //   171: iload_1
    //   172: bipush #9
    //   174: if_icmplt -> 210
    //   177: invokestatic LL1IL : ()LILil/I丨L/ILil/L丨1丨1丨I;
    //   180: bipush #10
    //   182: iload_1
    //   183: iload_2
    //   184: aload_3
    //   185: invokevirtual iI丨LLL1 : (IIILjava/lang/String;)Ljava/lang/String;
    //   188: astore_3
    //   189: aload_3
    //   190: ifnull -> 208
    //   193: aload_3
    //   194: invokestatic parseLong : (Ljava/lang/String;)J
    //   197: invokestatic valueOf : (J)Ljava/lang/Long;
    //   200: invokevirtual longValue : ()J
    //   203: lstore #8
    //   205: lload #8
    //   207: lreturn
    //   208: lconst_0
    //   209: lreturn
    //   210: getstatic com/main/engine/Engine.mCacheLong : Ljava/util/Map;
    //   213: astore #4
    //   215: aload #4
    //   217: monitorenter
    //   218: iload_1
    //   219: iconst_1
    //   220: if_icmpne -> 258
    //   223: getstatic com/main/engine/Engine.mCacheLong : Ljava/util/Map;
    //   226: iload_1
    //   227: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   230: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   235: checkcast java/lang/Long
    //   238: astore #10
    //   240: aload #10
    //   242: ifnull -> 258
    //   245: aload #10
    //   247: invokevirtual longValue : ()J
    //   250: lstore #8
    //   252: aload #4
    //   254: monitorexit
    //   255: lload #8
    //   257: lreturn
    //   258: invokestatic LL1IL : ()LILil/I丨L/ILil/L丨1丨1丨I;
    //   261: bipush #10
    //   263: iload_1
    //   264: iload_2
    //   265: aload_3
    //   266: invokevirtual iI丨LLL1 : (IIILjava/lang/String;)Ljava/lang/String;
    //   269: astore_3
    //   270: aload_3
    //   271: ifnull -> 313
    //   274: aload_3
    //   275: invokestatic parseLong : (Ljava/lang/String;)J
    //   278: invokestatic valueOf : (J)Ljava/lang/Long;
    //   281: astore_3
    //   282: iload_1
    //   283: iconst_1
    //   284: if_icmpne -> 301
    //   287: getstatic com/main/engine/Engine.mCacheLong : Ljava/util/Map;
    //   290: iload_1
    //   291: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   294: aload_3
    //   295: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   300: pop
    //   301: aload_3
    //   302: invokevirtual longValue : ()J
    //   305: lstore #8
    //   307: aload #4
    //   309: monitorexit
    //   310: lload #8
    //   312: lreturn
    //   313: aload #4
    //   315: monitorexit
    //   316: goto -> 325
    //   319: astore_3
    //   320: aload #4
    //   322: monitorexit
    //   323: aload_3
    //   324: athrow
    //   325: lconst_0
    //   326: lreturn
    //   327: astore_3
    //   328: goto -> 208
    //   331: astore_3
    //   332: goto -> 313
    // Exception table:
    //   from	to	target	type
    //   193	205	327	java/lang/Exception
    //   223	240	319	finally
    //   245	255	319	finally
    //   258	270	319	finally
    //   274	282	331	java/lang/Exception
    //   274	282	319	finally
    //   287	301	331	java/lang/Exception
    //   287	301	319	finally
    //   301	307	331	java/lang/Exception
    //   301	307	319	finally
    //   307	310	319	finally
    //   313	316	319	finally
    //   320	323	319	finally
  }
  
  public static String sendEventWithString(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2) {
    if (paramInt1 == 2)
      return I11li1.lI丨lii(paramString1, paramString2); 
    if (paramInt1 == 3)
      return I11li1.lIi丨I(paramString1, paramString2); 
    if (paramInt1 == 5) {
      paramString1 = I11li1.丨IillIi(paramString1);
      return (paramString1 == null) ? paramString2 : paramString1;
    } 
    if (paramInt1 == 6) {
      if (paramInt2 == 6)
        return I11li1.i丨ILiiLl(IL1Iii.IL1Iii(new byte[] { 
                10, 21, 21, 64, 89, 76, 77, 22, 22, 71, 
                77, 1, 3, 8, 5, 69, 77, 0, 13, 12 }, "baa0cc")); 
      boolean bool2 = false;
      boolean bool1 = false;
      if (paramInt2 == 7) {
        if (L丨1丨1丨I.LL1IL().L丨1l()) {
          if (I11li1.ii丨丨LL()) {
            if (paramInt3 == 1)
              bool1 = true; 
            return I11li1.I11li1(paramString1, 0L, bool1);
          } 
          return L丨1丨1丨I.LL1IL().iI丨LLL1(11, paramInt2, paramInt3, paramString1);
        } 
        bool1 = bool2;
        if (paramInt3 == 1)
          bool1 = true; 
        return I11li1.I11li1(paramString1, 0L, bool1);
      } 
      if (paramInt2 == 9) {
        if (!I11li1.ii丨丨LL()) {
          if (paramInt3 == 1) {
            I丨Ii.IL1Iii(paramString1);
          } else {
            I丨Ii.ILil();
          } 
          return "";
        } 
        File file = L丨1丨1丨I.LL1IL().lI丨II();
        try {
          File file1 = new File();
          this(file, IL1Iii.IL1Iii(new byte[] { -117, -45, -79, -124, -37, -93, 76, 20, 80, 17 }, "cf5ba3"));
          file = new File();
          this(paramString1);
          if (file.getParentFile().getAbsolutePath().compareTo(file1.getAbsolutePath()) == 0) {
            String str = file.getName();
            paramString1 = str;
          } 
        } catch (Exception exception) {}
      } else {
        if (paramInt2 == 13)
          return I11li1.丨1丨iIl(); 
        if (paramInt2 >= 10) {
          Bundle bundle;
          if (paramInt2 == 11) {
            StringBuilder stringBuilder;
            if (I11li1.ii丨丨LL() && paramInt3 == 1) {
              if (exception.length() > 0) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append(IL1Iii.IL1Iii(new byte[] { 
                        89, 15, 20, 66, 67, 80, 74, 22, 20, 28, 
                        89, 17 }, "8b4171"));
                stringBuilder1.append(paramString1);
                stringBuilder1.append(IL1Iii.IL1Iii(new byte[] { 22 }, "9bbc61"));
                stringBuilder1.append((String)exception);
                I11li1.I11li1(stringBuilder1.toString(), 5000L, true);
              } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 
                        70, 89, 14, 91, 95, 80, 31, 20, 76, 69, 
                        20 }, "f4a545"));
                stringBuilder.append(paramString1);
                stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 
                        22, 30, 86, 65, 87, 89, 82, 65, 90, 8, 
                        82, 25, 95, 93, 65, 4, 88, 67, 24, 80, 
                        84, 21, 83, 80, 89, 65, 76, 79, 122, 118, 
                        99, 125, 118, 41, 115, 101, 22, 2 }, "635a67"));
                I11li1.I11li1(stringBuilder.toString(), 5000L, true);
              } 
              return IL1Iii.IL1Iii(new byte[] { 84 }, "ed17df");
            } 
            JSONObject jSONObject = new JSONObject();
            try {
              if (!TextUtils.isEmpty(paramString1))
                jSONObject.put(IL1Iii.IL1Iii(new byte[] { 17, 92, 6 }, "a7ab06"), paramString1); 
              if (!TextUtils.isEmpty(stringBuilder))
                jSONObject.put(IL1Iii.IL1Iii(new byte[] { 91, 8, 70 }, "8d55fe"), stringBuilder); 
            } catch (Exception exception1) {}
            paramString1 = jSONObject.toString();
          } else if (paramInt2 == 12) {
            if (I11li1.ii丨丨LL()) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 
                      80, 91, 70, 3, 11, 20, 82, 83, 75, 22, 
                      16, 9, 65, 22 }, "16fedf"));
              stringBuilder.append(paramString1);
              I11li1.I11li1(stringBuilder.toString(), 5000L, true);
              return IL1Iii.IL1Iii(new byte[] { 9 }, "87e8d9");
            } 
          } else if (paramInt2 == 15) {
            if (I11li1.ii丨丨LL())
              return I11li1.l1Lll(); 
          } else if (paramInt2 == 16) {
            if (I11li1.ii丨丨LL())
              return I11li1.lL(); 
          } else if (paramInt2 == 17) {
            if (I11li1.ii丨丨LL()) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("");
              stringBuilder.append(I11li1.丨IlL1ll1(paramString1));
              return stringBuilder.toString();
            } 
          } else {
            String str;
            if (paramInt2 == 18) {
              if (I11li1.ii丨丨LL()) {
                str = I11li1.il丨l丨();
                if (str != null)
                  return str; 
              } 
            } else if (paramInt2 == 19) {
              if (I11li1.ii丨丨LL())
                return I11li1.lILi1lLl(paramString1) ? IL1Iii.IL1Iii(new byte[] { 85 }, "d4a92b") : IL1Iii.IL1Iii(new byte[] { 1 }, "1dbf96"); 
            } else if (paramInt2 == 20) {
              try {
                JSONObject jSONObject = new JSONObject();
                this();
                if (TextUtils.isEmpty(paramString1))
                  return IL1Iii.IL1Iii(new byte[] { 82 }, "b02011"); 
                jSONObject.put(IL1Iii.IL1Iii(new byte[] { 13, 65, 92, 81, 7, 68 }, "c413b6"), paramString1);
                if (!TextUtils.isEmpty(str))
                  jSONObject.put(IL1Iii.IL1Iii(new byte[] { 0, 88, 86, 70, 82, 91, 23 }, "c78275"), str); 
                str = jSONObject.toString();
                paramString1 = str;
              } catch (Exception exception1) {}
            } else {
              if (paramInt2 == 34)
                return obtainXml(10000L); 
              if (paramInt2 == 42)
                return extractApkAssets(paramString1, (String)exception1) ? IL1Iii.IL1Iii(new byte[] { 4 }, "50c333") : IL1Iii.IL1Iii(new byte[] { 86 }, "f0ea64"); 
              if (paramInt2 == 37 || paramInt2 == 40 || paramInt2 == 41 || paramInt2 == 43 || paramInt2 == 46) {
                bundle = I丨.Ilil(paramInt2, paramInt3, paramString1, 10000L);
                return (bundle != null) ? Integer.toString(bundle.getInt(IL1Iii.IL1Iii(new byte[] { 67, 84, 16 }, "11d6e2"))) : IL1Iii.IL1Iii(new byte[] { 8 }, "82c524");
              } 
              if (paramInt2 == 47) {
                bundle = I丨.Ilil(paramInt2, paramInt3, (String)bundle, 60000L);
                return (bundle != null) ? Integer.toString(bundle.getInt(IL1Iii.IL1Iii(new byte[] { 70, 1, 77 }, "4d9c51"))) : IL1Iii.IL1Iii(new byte[] { 84 }, "d99fbc");
              } 
            } 
          } 
          return L丨1丨1丨I.LL1IL().iI丨LLL1(11, paramInt2, paramInt3, (String)bundle);
        } 
      } 
      synchronized (mCacheStr) {
        String str = mCacheStr.get(Integer.valueOf(paramInt2));
        if (str != null)
          return str; 
        if (paramInt2 == 1) {
          paramString1 = I11li1.i丨1I1I1l();
        } else if (paramInt2 == 2) {
          paramString1 = I11li1.I丨Ii();
        } else {
          paramString1 = L丨1丨1丨I.LL1IL().iI丨LLL1(11, paramInt2, paramInt3, paramString1);
        } 
        if (paramString1 != null)
          mCacheStr.put(Integer.valueOf(paramInt2), paramString1); 
        return paramString1;
      } 
    } 
    return "";
  }
  
  public static void sendExit(int paramInt) {
    sendEventWithString(6, 60, paramInt, "", "");
  }
  
  public static boolean sendKeyEvent(int paramInt1, int paramInt2) {
    try {
      return L丨1丨1丨I.LL1IL().sendKeyEvent(paramInt1, paramInt2);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean sendTouchEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    try {
      return L丨1丨1丨I.LL1IL().sendTouchEvent(paramInt1, paramInt2, paramInt3, paramInt4);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static native void setDisplayInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public static native void setWorkPath(String paramString1, String paramString2);
  
  public static native int start(long paramLong, int paramInt, String paramString1, String paramString2, String paramString3);
  
  public static void startCatchPoint() {
    L丨1丨1丨I.LL1IL().l1IIi1丨();
  }
  
  public static boolean unzip(String paramString1, String paramString2, String paramString3, String paramString4) {
    return I11L.IL1Iii(paramString1, paramString2, paramString3, paramString4);
  }
  
  public static native void updateSnapCache();
}
