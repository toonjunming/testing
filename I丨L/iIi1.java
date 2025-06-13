package I丨L;

import javax.annotation.Nullable;

public final class iIi1 {
  @Nullable
  public static lI丨lii IL1Iii;
  
  public static long ILil;
  
  public static void IL1Iii(lI丨lii paramlI丨lii) {
    // Byte code:
    //   0: aload_0
    //   1: getfield l丨Li1LL : LI丨L/lI丨lii;
    //   4: ifnonnull -> 84
    //   7: aload_0
    //   8: getfield iI丨LLL1 : LI丨L/lI丨lii;
    //   11: ifnonnull -> 84
    //   14: aload_0
    //   15: getfield I丨L : Z
    //   18: ifeq -> 22
    //   21: return
    //   22: ldc I丨L/iIi1
    //   24: monitorenter
    //   25: getstatic I丨L/iIi1.ILil : J
    //   28: lstore_1
    //   29: lload_1
    //   30: ldc2_w 8192
    //   33: ladd
    //   34: ldc2_w 65536
    //   37: lcmp
    //   38: ifle -> 45
    //   41: ldc I丨L/iIi1
    //   43: monitorexit
    //   44: return
    //   45: lload_1
    //   46: ldc2_w 8192
    //   49: ladd
    //   50: putstatic I丨L/iIi1.ILil : J
    //   53: aload_0
    //   54: getstatic I丨L/iIi1.IL1Iii : LI丨L/lI丨lii;
    //   57: putfield l丨Li1LL : LI丨L/lI丨lii;
    //   60: aload_0
    //   61: iconst_0
    //   62: putfield I1I : I
    //   65: aload_0
    //   66: iconst_0
    //   67: putfield ILil : I
    //   70: aload_0
    //   71: putstatic I丨L/iIi1.IL1Iii : LI丨L/lI丨lii;
    //   74: ldc I丨L/iIi1
    //   76: monitorexit
    //   77: return
    //   78: astore_0
    //   79: ldc I丨L/iIi1
    //   81: monitorexit
    //   82: aload_0
    //   83: athrow
    //   84: new java/lang/IllegalArgumentException
    //   87: dup
    //   88: invokespecial <init> : ()V
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   25	29	78	finally
    //   41	44	78	finally
    //   45	77	78	finally
    //   79	82	78	finally
  }
  
  public static lI丨lii ILil() {
    // Byte code:
    //   0: ldc I丨L/iIi1
    //   2: monitorenter
    //   3: getstatic I丨L/iIi1.IL1Iii : LI丨L/lI丨lii;
    //   6: astore_0
    //   7: aload_0
    //   8: ifnull -> 38
    //   11: aload_0
    //   12: getfield l丨Li1LL : LI丨L/lI丨lii;
    //   15: putstatic I丨L/iIi1.IL1Iii : LI丨L/lI丨lii;
    //   18: aload_0
    //   19: aconst_null
    //   20: putfield l丨Li1LL : LI丨L/lI丨lii;
    //   23: getstatic I丨L/iIi1.ILil : J
    //   26: ldc2_w 8192
    //   29: lsub
    //   30: putstatic I丨L/iIi1.ILil : J
    //   33: ldc I丨L/iIi1
    //   35: monitorexit
    //   36: aload_0
    //   37: areturn
    //   38: ldc I丨L/iIi1
    //   40: monitorexit
    //   41: new I丨L/lI丨lii
    //   44: dup
    //   45: invokespecial <init> : ()V
    //   48: areturn
    //   49: astore_0
    //   50: ldc I丨L/iIi1
    //   52: monitorexit
    //   53: aload_0
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	49	finally
    //   11	36	49	finally
    //   38	41	49	finally
    //   50	53	49	finally
  }
}
