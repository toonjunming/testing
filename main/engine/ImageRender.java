package com.main.engine;

import ILil.IL1Iii;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;

public class ImageRender implements SurfaceTexture.OnFrameAvailableListener {
  private boolean bUpdate = false;
  
  private boolean isSetBufferSizeFailed = false;
  
  private long mHandle = 0L;
  
  private Surface mSurface;
  
  private SurfaceTexture mTex;
  
  private native Object nativeAquireImage(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private native boolean nativeClose(long paramLong);
  
  private native long nativeOpen(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  private native boolean nativeUpdate(long paramLong);
  
  public static void setFrameAvaListener(Object paramObject, SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2) {
    if (paramObject == null)
      return; 
    ((ImageRender)paramObject).setFrameAvaListenerImp(paramSurfaceTexture, paramInt1, paramInt2);
  }
  
  private void setFrameAvaListenerImp(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2) {
    try {
      Surface surface = this.mSurface;
      if (surface != null)
        surface.release(); 
    } catch (Exception exception) {}
    try {
      paramSurfaceTexture.setDefaultBufferSize(paramInt1, paramInt2);
    } catch (Exception exception) {
      String str1 = IL1Iii.IL1Iii(new byte[] { 122, 97 }, "495b33");
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(IL1Iii.IL1Iii(new byte[] { 80, 26, 84, 16 }, "5bd88c"));
      stringBuilder1.append(exception.toString());
      stringBuilder1.append(IL1Iii.IL1Iii(new byte[] { 75 }, "b6a927"));
      Log.i(str1, stringBuilder1.toString());
      boolean bool = exception instanceof SecurityException;
      this.isSetBufferSizeFailed = true;
    } 
    try {
      Surface surface = new Surface();
      this(paramSurfaceTexture);
      this.mSurface = surface;
    } catch (Exception exception) {
      String str1 = IL1Iii.IL1Iii(new byte[] { 44, 104 }, "b0a4c4");
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(IL1Iii.IL1Iii(new byte[] { 1, 73, 87, 74 }, "d1fb5c"));
      stringBuilder1.append(exception.toString());
      stringBuilder1.append(IL1Iii.IL1Iii(new byte[] { 79 }, "f562bc"));
      Log.i(str1, stringBuilder1.toString());
    } 
    this.mTex = paramSurfaceTexture;
    String str = IL1Iii.IL1Iii(new byte[] { 42, 61 }, "deeb93");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 85, 101, 20, 16, 94, 4, 91, 83, 73 }, "86ab8e"));
    stringBuilder.append(this.mSurface);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 30 }, "7c8247"));
    Log.i(str, stringBuilder.toString());
    paramSurfaceTexture.setOnFrameAvailableListener(this);
  }
  
  public Bitmap acquireImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mHandle : J
    //   6: lstore #5
    //   8: lload #5
    //   10: lconst_0
    //   11: lcmp
    //   12: ifeq -> 45
    //   15: aload_0
    //   16: lload #5
    //   18: iload_1
    //   19: iload_2
    //   20: iload_3
    //   21: iload #4
    //   23: invokespecial nativeAquireImage : (JIIII)Ljava/lang/Object;
    //   26: astore #7
    //   28: aload #7
    //   30: ifnull -> 45
    //   33: aload #7
    //   35: checkcast android/graphics/Bitmap
    //   38: astore #7
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #7
    //   44: areturn
    //   45: aload_0
    //   46: monitorexit
    //   47: aconst_null
    //   48: areturn
    //   49: astore #7
    //   51: aload_0
    //   52: monitorexit
    //   53: aload #7
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	49	finally
    //   15	28	49	finally
    //   33	42	49	finally
    //   45	47	49	finally
    //   51	53	49	finally
  }
  
  public Surface getSurface() {
    return this.mSurface;
  }
  
  public boolean isSurfaceOk() {
    return this.isSetBufferSizeFailed ^ true;
  }
  
  public boolean isUpdate() {
    return this.bUpdate;
  }
  
  public void onFrameAvailable(SurfaceTexture paramSurfaceTexture) {
    this.bUpdate = true;
    nativeUpdate(this.mHandle);
  }
  
  public boolean open(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    long l = nativeOpen(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    this.mHandle = l;
    return (l != 0L);
  }
  
  public boolean stop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mHandle : J
    //   6: lstore_1
    //   7: lload_1
    //   8: lconst_0
    //   9: lcmp
    //   10: ifeq -> 19
    //   13: aload_0
    //   14: lload_1
    //   15: invokespecial nativeClose : (J)Z
    //   18: pop
    //   19: aload_0
    //   20: lconst_0
    //   21: putfield mHandle : J
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_0
    //   27: getfield mSurface : Landroid/view/Surface;
    //   30: astore_3
    //   31: aload_3
    //   32: ifnull -> 44
    //   35: aload_3
    //   36: invokevirtual release : ()V
    //   39: aload_0
    //   40: aconst_null
    //   41: putfield mSurface : Landroid/view/Surface;
    //   44: aload_0
    //   45: getfield mTex : Landroid/graphics/SurfaceTexture;
    //   48: astore_3
    //   49: aload_3
    //   50: ifnull -> 62
    //   53: aload_3
    //   54: invokevirtual release : ()V
    //   57: aload_0
    //   58: aconst_null
    //   59: putfield mTex : Landroid/graphics/SurfaceTexture;
    //   62: iconst_0
    //   63: ireturn
    //   64: astore_3
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_3
    //   68: athrow
    //   69: astore_3
    //   70: goto -> 39
    //   73: astore_3
    //   74: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   2	7	64	finally
    //   13	19	64	finally
    //   19	26	64	finally
    //   35	39	69	java/lang/Exception
    //   53	57	73	java/lang/Exception
    //   65	67	64	finally
  }
}
