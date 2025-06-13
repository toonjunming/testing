package com.mysql.jdbc.util;

import com.mysql.jdbc.log.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ReadAheadInputStream extends InputStream {
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  
  private byte[] buf;
  
  public int currentPosition;
  
  public boolean doDebug = false;
  
  public int endOfCurrentData;
  
  public Log log;
  
  private InputStream underlyingStream;
  
  public ReadAheadInputStream(InputStream paramInputStream, int paramInt, boolean paramBoolean, Log paramLog) {
    this.underlyingStream = paramInputStream;
    this.buf = new byte[paramInt];
    this.doDebug = paramBoolean;
    this.log = paramLog;
  }
  
  public ReadAheadInputStream(InputStream paramInputStream, boolean paramBoolean, Log paramLog) {
    this(paramInputStream, 4096, paramBoolean, paramLog);
  }
  
  private void checkClosed() throws IOException {
    if (this.buf != null)
      return; 
    throw new IOException("Stream closed");
  }
  
  private void fill(int paramInt) throws IOException {
    checkClosed();
    this.currentPosition = 0;
    this.endOfCurrentData = 0;
    int j = Math.min(this.buf.length - 0, paramInt);
    int k = this.underlyingStream.available();
    int i = j;
    if (k > j)
      i = Math.min(this.buf.length - this.currentPosition, k); 
    if (this.doDebug) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("  ReadAheadInputStream.fill(");
      stringBuilder.append(paramInt);
      stringBuilder.append("), buffer_size=");
      stringBuilder.append(this.buf.length);
      stringBuilder.append(", current_position=");
      stringBuilder.append(this.currentPosition);
      stringBuilder.append(", need to read ");
      stringBuilder.append(Math.min(this.buf.length - this.currentPosition, paramInt));
      stringBuilder.append(" bytes to fill request,");
      if (k > 0) {
        stringBuilder.append(" underlying InputStream reports ");
        stringBuilder.append(k);
        stringBuilder.append(" total bytes available,");
      } 
      stringBuilder.append(" attempting to read ");
      stringBuilder.append(i);
      stringBuilder.append(" bytes.");
      Log log = this.log;
      if (log != null) {
        log.logTrace(stringBuilder.toString());
      } else {
        System.err.println(stringBuilder.toString());
      } 
    } 
    paramInt = this.underlyingStream.read(this.buf, this.currentPosition, i);
    if (paramInt > 0)
      this.endOfCurrentData = paramInt + this.currentPosition; 
  }
  
  private int readFromUnderlyingStreamIfNecessary(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    checkClosed();
    int j = this.endOfCurrentData - this.currentPosition;
    if (this.doDebug) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("ReadAheadInputStream.readIfNecessary(");
      stringBuilder.append(Arrays.toString(paramArrayOfbyte));
      stringBuilder.append(",");
      stringBuilder.append(paramInt1);
      stringBuilder.append(",");
      stringBuilder.append(paramInt2);
      stringBuilder.append(")");
      if (j <= 0) {
        stringBuilder.append(" not all data available in buffer, must read from stream");
        if (paramInt2 >= this.buf.length)
          stringBuilder.append(", amount requested > buffer, returning direct read() from stream"); 
      } 
      Log log = this.log;
      if (log != null) {
        log.logTrace(stringBuilder.toString());
      } else {
        System.err.println(stringBuilder.toString());
      } 
    } 
    int i = j;
    if (j <= 0) {
      if (paramInt2 >= this.buf.length)
        return this.underlyingStream.read(paramArrayOfbyte, paramInt1, paramInt2); 
      fill(paramInt2);
      j = this.endOfCurrentData - this.currentPosition;
      i = j;
      if (j <= 0)
        return -1; 
    } 
    j = paramInt2;
    if (i < paramInt2)
      j = i; 
    System.arraycopy(this.buf, this.currentPosition, paramArrayOfbyte, paramInt1, j);
    this.currentPosition += j;
    return j;
  }
  
  public int available() throws IOException {
    checkClosed();
    return this.underlyingStream.available() + this.endOfCurrentData - this.currentPosition;
  }
  
  public void close() throws IOException {
    InputStream inputStream = this.underlyingStream;
    if (inputStream != null)
      try {
        inputStream.close();
      } finally {
        this.underlyingStream = null;
        this.buf = null;
        this.log = null;
      }  
  }
  
  public boolean markSupported() {
    return false;
  }
  
  public int read() throws IOException {
    checkClosed();
    if (this.currentPosition >= this.endOfCurrentData) {
      fill(1);
      if (this.currentPosition >= this.endOfCurrentData)
        return -1; 
    } 
    byte[] arrayOfByte = this.buf;
    int i = this.currentPosition;
    this.currentPosition = i + 1;
    return arrayOfByte[i] & 0xFF;
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: iload_2
    //   7: iload_3
    //   8: iadd
    //   9: istore #5
    //   11: aload_1
    //   12: arraylength
    //   13: istore #4
    //   15: iload_2
    //   16: iload_3
    //   17: ior
    //   18: iload #5
    //   20: ior
    //   21: iload #4
    //   23: iload #5
    //   25: isub
    //   26: ior
    //   27: iflt -> 119
    //   30: iconst_0
    //   31: istore #4
    //   33: iload_3
    //   34: ifne -> 41
    //   37: aload_0
    //   38: monitorexit
    //   39: iconst_0
    //   40: ireturn
    //   41: aload_0
    //   42: aload_1
    //   43: iload_2
    //   44: iload #4
    //   46: iadd
    //   47: iload_3
    //   48: iload #4
    //   50: isub
    //   51: invokespecial readFromUnderlyingStreamIfNecessary : ([BII)I
    //   54: istore #5
    //   56: iload #5
    //   58: ifgt -> 75
    //   61: iload #4
    //   63: istore_2
    //   64: iload #4
    //   66: ifne -> 115
    //   69: iload #5
    //   71: istore_2
    //   72: goto -> 115
    //   75: iload #4
    //   77: iload #5
    //   79: iadd
    //   80: istore #5
    //   82: iload #5
    //   84: iload_3
    //   85: if_icmplt -> 94
    //   88: iload #5
    //   90: istore_2
    //   91: goto -> 115
    //   94: aload_0
    //   95: getfield underlyingStream : Ljava/io/InputStream;
    //   98: invokevirtual available : ()I
    //   101: istore #6
    //   103: iload #5
    //   105: istore #4
    //   107: iload #6
    //   109: ifgt -> 41
    //   112: iload #5
    //   114: istore_2
    //   115: aload_0
    //   116: monitorexit
    //   117: iload_2
    //   118: ireturn
    //   119: new java/lang/IndexOutOfBoundsException
    //   122: astore_1
    //   123: aload_1
    //   124: invokespecial <init> : ()V
    //   127: aload_1
    //   128: athrow
    //   129: astore_1
    //   130: aload_0
    //   131: monitorexit
    //   132: aload_1
    //   133: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	129	finally
    //   11	15	129	finally
    //   41	56	129	finally
    //   94	103	129	finally
    //   119	129	129	finally
  }
  
  public long skip(long paramLong) throws IOException {
    checkClosed();
    if (paramLong <= 0L)
      return 0L; 
    long l2 = (this.endOfCurrentData - this.currentPosition);
    long l1 = l2;
    if (l2 <= 0L) {
      fill((int)paramLong);
      l2 = (this.endOfCurrentData - this.currentPosition);
      l1 = l2;
      if (l2 <= 0L)
        return 0L; 
    } 
    l2 = paramLong;
    if (l1 < paramLong)
      l2 = l1; 
    this.currentPosition = (int)(this.currentPosition + l2);
    return l2;
  }
}
