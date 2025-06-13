package com.main.engine;

import ILil.I丨L.ILil.L11丨丨丨1;
import android.os.IBinder;
import android.util.Log;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TsEventNative {
  private static final String TAG = ILil.IL1Iii.IL1Iii(new byte[] { 
        53, 67, 38, 16, 82, 91, 21, 126, 2, 18, 
        94, 67, 4 }, "a0cf75");
  
  private static Map<Integer, L11丨丨丨1> listeners = new ConcurrentHashMap<Integer, L11丨丨丨1>();
  
  public static void onEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    // Byte code:
    //   0: ldc com/main/engine/TsEventNative
    //   2: monitorenter
    //   3: getstatic com/main/engine/TsEventNative.listeners : Ljava/util/Map;
    //   6: invokeinterface entrySet : ()Ljava/util/Set;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore #5
    //   18: aload #5
    //   20: invokeinterface hasNext : ()Z
    //   25: ifeq -> 292
    //   28: aload #5
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast java/util/Map$Entry
    //   38: invokeinterface getValue : ()Ljava/lang/Object;
    //   43: checkcast ILil/I丨L/ILil/L11丨丨丨1
    //   46: astore #6
    //   48: aload #6
    //   50: invokeinterface asBinder : ()Landroid/os/IBinder;
    //   55: invokeinterface isBinderAlive : ()Z
    //   60: ifeq -> 79
    //   63: aload #6
    //   65: iload_0
    //   66: iload_1
    //   67: iload_2
    //   68: iload_3
    //   69: iload #4
    //   71: invokeinterface Ll丨1 : (IIIII)V
    //   76: goto -> 18
    //   79: aload #5
    //   81: invokeinterface remove : ()V
    //   86: goto -> 18
    //   89: astore #6
    //   91: getstatic com/main/engine/TsEventNative.TAG : Ljava/lang/String;
    //   94: astore #8
    //   96: new java/lang/StringBuilder
    //   99: astore #7
    //   101: aload #7
    //   103: invokespecial <init> : ()V
    //   106: aload #7
    //   108: bipush #26
    //   110: newarray byte
    //   112: dup
    //   113: iconst_0
    //   114: bipush #119
    //   116: bastore
    //   117: dup
    //   118: iconst_1
    //   119: bipush #7
    //   121: bastore
    //   122: dup
    //   123: iconst_2
    //   124: bipush #15
    //   126: bastore
    //   127: dup
    //   128: iconst_3
    //   129: bipush #10
    //   131: bastore
    //   132: dup
    //   133: iconst_4
    //   134: iconst_1
    //   135: bastore
    //   136: dup
    //   137: iconst_5
    //   138: iconst_5
    //   139: bastore
    //   140: dup
    //   141: bipush #6
    //   143: bipush #17
    //   145: bastore
    //   146: dup
    //   147: bipush #7
    //   149: bipush #18
    //   151: bastore
    //   152: dup
    //   153: bipush #8
    //   155: bipush #9
    //   157: bastore
    //   158: dup
    //   159: bipush #9
    //   161: bipush #70
    //   163: bastore
    //   164: dup
    //   165: bipush #10
    //   167: bipush #13
    //   169: bastore
    //   170: dup
    //   171: bipush #11
    //   173: bipush #15
    //   175: bastore
    //   176: dup
    //   177: bipush #12
    //   179: bipush #71
    //   181: bastore
    //   182: dup
    //   183: bipush #13
    //   185: bipush #9
    //   187: bastore
    //   188: dup
    //   189: bipush #14
    //   191: bipush #13
    //   193: bastore
    //   194: dup
    //   195: bipush #15
    //   197: iconst_3
    //   198: bastore
    //   199: dup
    //   200: bipush #16
    //   202: bipush #68
    //   204: bastore
    //   205: dup
    //   206: bipush #17
    //   208: bipush #14
    //   210: bastore
    //   211: dup
    //   212: bipush #18
    //   214: bipush #95
    //   216: bastore
    //   217: dup
    //   218: bipush #19
    //   220: bipush #35
    //   222: bastore
    //   223: dup
    //   224: bipush #20
    //   226: bipush #16
    //   228: bastore
    //   229: dup
    //   230: bipush #21
    //   232: iconst_3
    //   233: bastore
    //   234: dup
    //   235: bipush #22
    //   237: bipush #10
    //   239: bastore
    //   240: dup
    //   241: bipush #23
    //   243: bipush #21
    //   245: bastore
    //   246: dup
    //   247: bipush #24
    //   249: bipush #11
    //   251: bastore
    //   252: dup
    //   253: bipush #25
    //   255: bipush #70
    //   257: bastore
    //   258: ldc '1fffda'
    //   260: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   266: pop
    //   267: aload #7
    //   269: aload #6
    //   271: invokevirtual getMessage : ()Ljava/lang/String;
    //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: pop
    //   278: aload #8
    //   280: aload #7
    //   282: invokevirtual toString : ()Ljava/lang/String;
    //   285: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   288: pop
    //   289: goto -> 18
    //   292: ldc com/main/engine/TsEventNative
    //   294: monitorexit
    //   295: return
    //   296: astore #5
    //   298: ldc com/main/engine/TsEventNative
    //   300: monitorexit
    //   301: aload #5
    //   303: athrow
    // Exception table:
    //   from	to	target	type
    //   3	18	296	finally
    //   18	48	296	finally
    //   48	76	89	java/lang/Exception
    //   48	76	296	finally
    //   79	86	89	java/lang/Exception
    //   79	86	296	finally
    //   91	289	296	finally
  }
  
  public static void removeITsEventListener(int paramInt) {
    // Byte code:
    //   0: ldc com/main/engine/TsEventNative
    //   2: monitorenter
    //   3: getstatic com/main/engine/TsEventNative.listeners : Ljava/util/Map;
    //   6: iload_0
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: ldc com/main/engine/TsEventNative
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: ldc com/main/engine/TsEventNative
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	20	finally
  }
  
  public static void setITsEventListener(int paramInt, L11丨丨丨1 paramL11丨丨丨1) {
    /* monitor enter TypeReferenceDotClassExpression{ObjectType{com/main/engine/TsEventNative}} */
    try {
      IBinder iBinder = paramL11丨丨丨1.asBinder();
      IL1Iii iL1Iii = new IL1Iii();
      this(paramInt);
      iBinder.linkToDeath(iL1Iii, 0);
      listeners.put(Integer.valueOf(paramInt), paramL11丨丨丨1);
    } catch (Exception exception) {
      String str = TAG;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              112, 86, 10, 90, 3, 86, 22, 67, 12, 22, 
              10, 91, 88, 92, 67, 66, 9, 18, 82, 82, 
              2, 66, 14, 8, 22 }, "67c6f2"));
      stringBuilder.append(exception.getMessage());
      Log.e(str, stringBuilder.toString());
    } finally {}
    /* monitor exit TypeReferenceDotClassExpression{ObjectType{com/main/engine/TsEventNative}} */
  }
  
  public static native void start();
  
  public static final class IL1Iii implements IBinder.DeathRecipient {
    public final int IL1Iii;
    
    public IL1Iii(int param1Int) {}
    
    public void binderDied() {
      TsEventNative.removeITsEventListener(this.IL1Iii);
    }
  }
}
