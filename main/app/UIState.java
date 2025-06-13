package com.main.app;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;

public class UIState {
  private static final String PREFS_FILE_NAME = ILil.IL1Iii.IL1Iii(new byte[] { 108, 121, 54, 64, 7, 69, 92 }, "90e4f1");
  
  private static volatile UIState instance;
  
  private int baseID = 0;
  
  private Context mContext;
  
  private Map<Integer, I1I> mMapHudIds = new HashMap<Integer, I1I>();
  
  public static UIState getInstance() {
    // Byte code:
    //   0: getstatic com/main/app/UIState.instance : Lcom/main/app/UIState;
    //   3: ifnonnull -> 39
    //   6: ldc com/main/app/UIState
    //   8: monitorenter
    //   9: getstatic com/main/app/UIState.instance : Lcom/main/app/UIState;
    //   12: ifnonnull -> 27
    //   15: new com/main/app/UIState
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: aload_0
    //   24: putstatic com/main/app/UIState.instance : Lcom/main/app/UIState;
    //   27: ldc com/main/app/UIState
    //   29: monitorexit
    //   30: goto -> 39
    //   33: astore_0
    //   34: ldc com/main/app/UIState
    //   36: monitorexit
    //   37: aload_0
    //   38: athrow
    //   39: getstatic com/main/app/UIState.instance : Lcom/main/app/UIState;
    //   42: areturn
    // Exception table:
    //   from	to	target	type
    //   9	27	33	finally
    //   27	30	33	finally
    //   34	37	33	finally
  }
  
  private void saveHudState() {
    SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PREFS_FILE_NAME, 0);
    String str = (new Gson()).toJson(this.mMapHudIds);
    sharedPreferences.edit().putString(ILil.IL1Iii.IL1Iii(new byte[] { 14, 77, 80, 65, 67, 87, 18, 93 }, "f84276"), str).apply();
  }
  
  public void clearHud() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mMapHudIds : Ljava/util/Map;
    //   6: invokeinterface isEmpty : ()Z
    //   11: ifne -> 27
    //   14: aload_0
    //   15: getfield mMapHudIds : Ljava/util/Map;
    //   18: invokeinterface clear : ()V
    //   23: aload_0
    //   24: invokespecial saveHudState : ()V
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public void createHud(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new com/main/app/UIState$I1I
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_2
    //   11: iconst_0
    //   12: putfield state : I
    //   15: aload_0
    //   16: getfield mMapHudIds : Ljava/util/Map;
    //   19: iload_1
    //   20: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   23: aload_2
    //   24: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   29: pop
    //   30: aload_0
    //   31: invokespecial saveHudState : ()V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_2
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_2
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	37	finally
  }
  
  public int getBaseId() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield baseID : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield baseID : I
    //   12: goto -> 19
    //   15: astore_2
    //   16: goto -> 109
    //   19: aload_0
    //   20: getfield baseID : I
    //   23: ifne -> 31
    //   26: aload_0
    //   27: iconst_1
    //   28: putfield baseID : I
    //   31: aload_0
    //   32: getfield mContext : Landroid/content/Context;
    //   35: getstatic com/main/app/UIState.PREFS_FILE_NAME : Ljava/lang/String;
    //   38: iconst_0
    //   39: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   42: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   47: bipush #6
    //   49: newarray byte
    //   51: dup
    //   52: iconst_0
    //   53: bipush #91
    //   55: bastore
    //   56: dup
    //   57: iconst_1
    //   58: bipush #85
    //   60: bastore
    //   61: dup
    //   62: iconst_2
    //   63: bipush #71
    //   65: bastore
    //   66: dup
    //   67: iconst_3
    //   68: bipush #81
    //   70: bastore
    //   71: dup
    //   72: iconst_4
    //   73: bipush #94
    //   75: bastore
    //   76: dup
    //   77: iconst_5
    //   78: bipush #6
    //   80: bastore
    //   81: ldc '94447b'
    //   83: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   86: aload_0
    //   87: getfield baseID : I
    //   90: invokeinterface putInt : (Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
    //   95: invokeinterface apply : ()V
    //   100: aload_0
    //   101: getfield baseID : I
    //   104: istore_1
    //   105: aload_0
    //   106: monitorexit
    //   107: iload_1
    //   108: ireturn
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_2
    //   112: athrow
    //   113: astore_2
    //   114: goto -> 19
    // Exception table:
    //   from	to	target	type
    //   2	12	113	java/lang/Exception
    //   2	12	15	finally
    //   19	31	15	finally
    //   31	105	15	finally
  }
  
  public ILil getFloatControlState() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mContext : Landroid/content/Context;
    //   6: getstatic com/main/app/UIState.PREFS_FILE_NAME : Ljava/lang/String;
    //   9: iconst_0
    //   10: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   13: astore_1
    //   14: new com/main/app/UIState$ILil
    //   17: astore_2
    //   18: aload_2
    //   19: invokespecial <init> : ()V
    //   22: aload_2
    //   23: aload_1
    //   24: bipush #9
    //   26: newarray byte
    //   28: dup
    //   29: iconst_0
    //   30: bipush #17
    //   32: bastore
    //   33: dup
    //   34: iconst_1
    //   35: bipush #94
    //   37: bastore
    //   38: dup
    //   39: iconst_2
    //   40: bipush #91
    //   42: bastore
    //   43: dup
    //   44: iconst_3
    //   45: bipush #19
    //   47: bastore
    //   48: dup
    //   49: iconst_4
    //   50: iconst_5
    //   51: bastore
    //   52: dup
    //   53: iconst_5
    //   54: bipush #93
    //   56: bastore
    //   57: dup
    //   58: bipush #6
    //   60: bipush #13
    //   62: bastore
    //   63: dup
    //   64: bipush #7
    //   66: bipush #87
    //   68: bastore
    //   69: dup
    //   70: bipush #8
    //   72: bipush #64
    //   74: bastore
    //   75: ldc 'b64dc1'
    //   77: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   80: iconst_1
    //   81: invokeinterface getBoolean : (Ljava/lang/String;Z)Z
    //   86: putfield IL1Iii : Z
    //   89: aload_2
    //   90: aload_1
    //   91: bipush #10
    //   93: newarray byte
    //   95: dup
    //   96: iconst_0
    //   97: iconst_0
    //   98: bastore
    //   99: dup
    //   100: iconst_1
    //   101: bipush #85
    //   103: bastore
    //   104: dup
    //   105: iconst_2
    //   106: bipush #11
    //   108: bastore
    //   109: dup
    //   110: iconst_3
    //   111: bipush #81
    //   113: bastore
    //   114: dup
    //   115: iconst_4
    //   116: bipush #23
    //   118: bastore
    //   119: dup
    //   120: iconst_5
    //   121: bipush #67
    //   123: bastore
    //   124: dup
    //   125: bipush #6
    //   127: bipush #9
    //   129: bastore
    //   130: dup
    //   131: bipush #7
    //   133: bipush #74
    //   135: bastore
    //   136: dup
    //   137: bipush #8
    //   139: bipush #59
    //   141: bastore
    //   142: dup
    //   143: bipush #9
    //   145: bipush #72
    //   147: bastore
    //   148: ldc 'f9d0c3'
    //   150: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   153: iconst_0
    //   154: invokeinterface getInt : (Ljava/lang/String;I)I
    //   159: putfield ILil : I
    //   162: aload_2
    //   163: aload_1
    //   164: bipush #10
    //   166: newarray byte
    //   168: dup
    //   169: iconst_0
    //   170: iconst_5
    //   171: bastore
    //   172: dup
    //   173: iconst_1
    //   174: bipush #8
    //   176: bastore
    //   177: dup
    //   178: iconst_2
    //   179: bipush #10
    //   181: bastore
    //   182: dup
    //   183: iconst_3
    //   184: bipush #85
    //   186: bastore
    //   187: dup
    //   188: iconst_4
    //   189: bipush #22
    //   191: bastore
    //   192: dup
    //   193: iconst_5
    //   194: bipush #18
    //   196: bastore
    //   197: dup
    //   198: bipush #6
    //   200: bipush #12
    //   202: bastore
    //   203: dup
    //   204: bipush #7
    //   206: bipush #23
    //   208: bastore
    //   209: dup
    //   210: bipush #8
    //   212: bipush #58
    //   214: bastore
    //   215: dup
    //   216: bipush #9
    //   218: bipush #77
    //   220: bastore
    //   221: ldc 'cde4bb'
    //   223: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   226: iconst_0
    //   227: invokeinterface getInt : (Ljava/lang/String;I)I
    //   232: putfield I1I : I
    //   235: aload_0
    //   236: monitorexit
    //   237: aload_2
    //   238: areturn
    //   239: astore_1
    //   240: aload_0
    //   241: monitorexit
    //   242: aload_1
    //   243: athrow
    // Exception table:
    //   from	to	target	type
    //   2	235	239	finally
  }
  
  public void hideHud(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mMapHudIds : Ljava/util/Map;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   15: ifeq -> 36
    //   18: aload_0
    //   19: getfield mMapHudIds : Ljava/util/Map;
    //   22: iload_1
    //   23: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   26: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   31: pop
    //   32: aload_0
    //   33: invokespecial saveHudState : ()V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	39	finally
  }
  
  public void initialize(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield mContext : Landroid/content/Context;
    //   7: aload_1
    //   8: getstatic com/main/app/UIState.PREFS_FILE_NAME : Ljava/lang/String;
    //   11: iconst_0
    //   12: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   15: astore_1
    //   16: aload_0
    //   17: aload_1
    //   18: bipush #6
    //   20: newarray byte
    //   22: dup
    //   23: iconst_0
    //   24: iconst_4
    //   25: bastore
    //   26: dup
    //   27: iconst_1
    //   28: iconst_5
    //   29: bastore
    //   30: dup
    //   31: iconst_2
    //   32: bipush #66
    //   34: bastore
    //   35: dup
    //   36: iconst_3
    //   37: iconst_4
    //   38: bastore
    //   39: dup
    //   40: iconst_4
    //   41: bipush #89
    //   43: bastore
    //   44: dup
    //   45: iconst_5
    //   46: iconst_0
    //   47: bastore
    //   48: ldc 'fd1a0d'
    //   50: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   53: iconst_0
    //   54: invokeinterface getInt : (Ljava/lang/String;I)I
    //   59: putfield baseID : I
    //   62: aload_1
    //   63: bipush #8
    //   65: newarray byte
    //   67: dup
    //   68: iconst_0
    //   69: bipush #81
    //   71: bastore
    //   72: dup
    //   73: iconst_1
    //   74: bipush #19
    //   76: bastore
    //   77: dup
    //   78: iconst_2
    //   79: bipush #80
    //   81: bastore
    //   82: dup
    //   83: iconst_3
    //   84: bipush #67
    //   86: bastore
    //   87: dup
    //   88: iconst_4
    //   89: bipush #67
    //   91: bastore
    //   92: dup
    //   93: iconst_5
    //   94: bipush #7
    //   96: bastore
    //   97: dup
    //   98: bipush #6
    //   100: bipush #77
    //   102: bastore
    //   103: dup
    //   104: bipush #7
    //   106: iconst_3
    //   107: bastore
    //   108: ldc '9f407f'
    //   110: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   113: ldc ''
    //   115: invokeinterface getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   120: astore_1
    //   121: aload_1
    //   122: invokevirtual isEmpty : ()Z
    //   125: istore_2
    //   126: iload_2
    //   127: ifne -> 166
    //   130: new com/google/gson/Gson
    //   133: astore_3
    //   134: aload_3
    //   135: invokespecial <init> : ()V
    //   138: new com/main/app/UIState$IL1Iii
    //   141: astore #4
    //   143: aload #4
    //   145: aload_0
    //   146: invokespecial <init> : (Lcom/main/app/UIState;)V
    //   149: aload_0
    //   150: aload_3
    //   151: aload_1
    //   152: aload #4
    //   154: invokevirtual getType : ()Ljava/lang/reflect/Type;
    //   157: invokevirtual fromJson : (Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
    //   160: checkcast java/util/Map
    //   163: putfield mMapHudIds : Ljava/util/Map;
    //   166: aload_0
    //   167: monitorexit
    //   168: return
    //   169: astore_1
    //   170: aload_0
    //   171: monitorexit
    //   172: aload_1
    //   173: athrow
    //   174: astore_1
    //   175: goto -> 166
    // Exception table:
    //   from	to	target	type
    //   2	126	169	finally
    //   130	166	174	java/lang/Exception
    //   130	166	169	finally
  }
  
  public void onUiShow() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic I丨iL : ()LILil/l丨Li1LL/IL1Iii/IL1Iii;
    //   5: aload_0
    //   6: getfield mMapHudIds : Ljava/util/Map;
    //   9: invokevirtual ILL : (Ljava/util/Map;)V
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  public void setFloatWndPos(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mContext : Landroid/content/Context;
    //   6: getstatic com/main/app/UIState.PREFS_FILE_NAME : Ljava/lang/String;
    //   9: iconst_0
    //   10: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   13: astore_3
    //   14: aload_3
    //   15: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   20: bipush #10
    //   22: newarray byte
    //   24: dup
    //   25: iconst_0
    //   26: bipush #86
    //   28: bastore
    //   29: dup
    //   30: iconst_1
    //   31: bipush #91
    //   33: bastore
    //   34: dup
    //   35: iconst_2
    //   36: bipush #86
    //   38: bastore
    //   39: dup
    //   40: iconst_3
    //   41: bipush #81
    //   43: bastore
    //   44: dup
    //   45: iconst_4
    //   46: bipush #23
    //   48: bastore
    //   49: dup
    //   50: iconst_5
    //   51: bipush #66
    //   53: bastore
    //   54: dup
    //   55: bipush #6
    //   57: bipush #95
    //   59: bastore
    //   60: dup
    //   61: bipush #7
    //   63: bipush #68
    //   65: bastore
    //   66: dup
    //   67: bipush #8
    //   69: bipush #102
    //   71: bastore
    //   72: dup
    //   73: bipush #9
    //   75: bipush #72
    //   77: bastore
    //   78: ldc '0790c2'
    //   80: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   83: iload_1
    //   84: invokeinterface putInt : (Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
    //   89: invokeinterface apply : ()V
    //   94: aload_3
    //   95: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   100: bipush #10
    //   102: newarray byte
    //   104: dup
    //   105: iconst_0
    //   106: bipush #86
    //   108: bastore
    //   109: dup
    //   110: iconst_1
    //   111: bipush #85
    //   113: bastore
    //   114: dup
    //   115: iconst_2
    //   116: bipush #9
    //   118: bastore
    //   119: dup
    //   120: iconst_3
    //   121: bipush #89
    //   123: bastore
    //   124: dup
    //   125: iconst_4
    //   126: bipush #22
    //   128: bastore
    //   129: dup
    //   130: iconst_5
    //   131: bipush #17
    //   133: bastore
    //   134: dup
    //   135: bipush #6
    //   137: bipush #95
    //   139: bastore
    //   140: dup
    //   141: bipush #7
    //   143: bipush #74
    //   145: bastore
    //   146: dup
    //   147: bipush #8
    //   149: bipush #57
    //   151: bastore
    //   152: dup
    //   153: bipush #9
    //   155: bipush #65
    //   157: bastore
    //   158: ldc '09f8ba'
    //   160: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   163: iload_2
    //   164: invokeinterface putInt : (Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
    //   169: invokeinterface apply : ()V
    //   174: aload_0
    //   175: monitorexit
    //   176: return
    //   177: astore_3
    //   178: aload_0
    //   179: monitorexit
    //   180: aload_3
    //   181: athrow
    // Exception table:
    //   from	to	target	type
    //   2	174	177	finally
  }
  
  public void showFloatWnd(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mContext : Landroid/content/Context;
    //   6: getstatic com/main/app/UIState.PREFS_FILE_NAME : Ljava/lang/String;
    //   9: iconst_0
    //   10: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   13: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   18: bipush #9
    //   20: newarray byte
    //   22: dup
    //   23: iconst_0
    //   24: bipush #16
    //   26: bastore
    //   27: dup
    //   28: iconst_1
    //   29: bipush #80
    //   31: bastore
    //   32: dup
    //   33: iconst_2
    //   34: bipush #92
    //   36: bastore
    //   37: dup
    //   38: iconst_3
    //   39: bipush #79
    //   41: bastore
    //   42: dup
    //   43: iconst_4
    //   44: iconst_2
    //   45: bastore
    //   46: dup
    //   47: iconst_5
    //   48: bipush #9
    //   50: bastore
    //   51: dup
    //   52: bipush #6
    //   54: bipush #12
    //   56: bastore
    //   57: dup
    //   58: bipush #7
    //   60: bipush #89
    //   62: bastore
    //   63: dup
    //   64: bipush #8
    //   66: bipush #71
    //   68: bastore
    //   69: ldc 'c838de'
    //   71: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   74: iload_1
    //   75: invokeinterface putBoolean : (Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
    //   80: invokeinterface apply : ()V
    //   85: aload_0
    //   86: monitorexit
    //   87: return
    //   88: astore_2
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_2
    //   92: athrow
    // Exception table:
    //   from	to	target	type
    //   2	85	88	finally
  }
  
  public void showHUD(int paramInt1, String paramString1, int paramInt2, String paramString2, String paramString3, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mMapHudIds : Ljava/util/Map;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast com/main/app/UIState$I1I
    //   18: astore #16
    //   20: aload #16
    //   22: ifnull -> 131
    //   25: aload #16
    //   27: aload_2
    //   28: putfield text : Ljava/lang/String;
    //   31: aload #16
    //   33: iload_3
    //   34: putfield size : I
    //   37: aload #16
    //   39: aload #4
    //   41: putfield color : Ljava/lang/String;
    //   44: aload #16
    //   46: aload #5
    //   48: putfield bg : Ljava/lang/String;
    //   51: aload #16
    //   53: iload #6
    //   55: putfield pos : I
    //   58: aload #16
    //   60: iload #7
    //   62: putfield x : I
    //   65: aload #16
    //   67: iload #8
    //   69: putfield y : I
    //   72: aload #16
    //   74: iload #9
    //   76: putfield width : I
    //   79: aload #16
    //   81: iload #10
    //   83: putfield height : I
    //   86: aload #16
    //   88: iload #11
    //   90: putfield paddleft : I
    //   93: aload #16
    //   95: iload #12
    //   97: putfield paddingtop : I
    //   100: aload #16
    //   102: iload #13
    //   104: putfield paddingright : I
    //   107: aload #16
    //   109: iload #14
    //   111: putfield paddingbottom : I
    //   114: aload #16
    //   116: iload #15
    //   118: putfield align : I
    //   121: aload #16
    //   123: iconst_1
    //   124: putfield state : I
    //   127: aload_0
    //   128: invokespecial saveHudState : ()V
    //   131: aload_0
    //   132: monitorexit
    //   133: return
    //   134: astore_2
    //   135: aload_0
    //   136: monitorexit
    //   137: aload_2
    //   138: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	134	finally
    //   25	131	134	finally
  }
  
  public static class I1I {
    @SerializedName("align")
    public int align;
    
    @SerializedName("bg")
    public String bg;
    
    @SerializedName("color")
    public String color;
    
    @SerializedName("height")
    public int height;
    
    @SerializedName("paddingbottom")
    public int paddingbottom;
    
    @SerializedName("paddingright")
    public int paddingright;
    
    @SerializedName("paddingtop")
    public int paddingtop;
    
    @SerializedName("paddleft")
    public int paddleft;
    
    @SerializedName("pos")
    public int pos;
    
    @SerializedName("size")
    public int size;
    
    @SerializedName("state")
    public int state;
    
    @SerializedName("text")
    public String text;
    
    @SerializedName("width")
    public int width;
    
    @SerializedName("x")
    public int x;
    
    @SerializedName("y")
    public int y;
  }
  
  public class IL1Iii extends TypeToken<Map<Integer, I1I>> {
    public IL1Iii(UIState this$0) {}
  }
  
  public static class ILil {
    public int I1I;
    
    public boolean IL1Iii;
    
    public int ILil;
  }
}
