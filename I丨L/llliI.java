package I丨L;

import java.nio.charset.Charset;

public final class llliI {
  public static final Charset IL1Iii = Charset.forName("UTF-8");
  
  public static int I1I(int paramInt) {
    return (paramInt & 0xFF) << 24 | (0xFF000000 & paramInt) >>> 24 | (0xFF0000 & paramInt) >>> 8 | (0xFF00 & paramInt) << 8;
  }
  
  public static boolean IL1Iii(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
    for (byte b = 0; b < paramInt3; b++) {
      if (paramArrayOfbyte1[b + paramInt1] != paramArrayOfbyte2[b + paramInt2])
        return false; 
    } 
    return true;
  }
  
  public static void ILil(long paramLong1, long paramLong2, long paramLong3) {
    if ((paramLong2 | paramLong3) >= 0L && paramLong2 <= paramLong1 && paramLong1 - paramLong2 >= paramLong3)
      return; 
    throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) }));
  }
  
  public static void Ilil(Throwable paramThrowable) {
    l丨Li1LL(paramThrowable);
    throw null;
  }
  
  public static short I丨L(short paramShort) {
    int i = paramShort & 0xFFFF;
    return (short)((i & 0xFF) << 8 | (0xFF00 & i) >>> 8);
  }
  
  public static <T extends Throwable> void l丨Li1LL(Throwable paramThrowable) throws T {
    throw (T)paramThrowable;
  }
}
