package com.mysql.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class Blob implements Blob, OutputStreamWatcher {
  private byte[] binaryData = null;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private boolean isClosed = false;
  
  public Blob(ExceptionInterceptor paramExceptionInterceptor) {
    setBinaryData(Constants.EMPTY_BYTE_ARRAY);
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  public Blob(byte[] paramArrayOfbyte, ExceptionInterceptor paramExceptionInterceptor) {
    setBinaryData(paramArrayOfbyte);
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  public Blob(byte[] paramArrayOfbyte, ResultSetInternalMethods paramResultSetInternalMethods, int paramInt) {
    setBinaryData(paramArrayOfbyte);
  }
  
  private void checkClosed() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isClosed : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: ldc 'Invalid operation on closed BLOB'
    //   16: ldc 'S1009'
    //   18: aload_0
    //   19: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   22: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   25: athrow
    //   26: astore_2
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_2
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	26	finally
    //   14	26	26	finally
  }
  
  private byte[] getBinaryData() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield binaryData : [B
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  private void setBinaryData(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield binaryData : [B
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void free() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield binaryData : [B
    //   7: aload_0
    //   8: iconst_1
    //   9: putfield isClosed : Z
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
  
  public InputStream getBinaryStream() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: new java/io/ByteArrayInputStream
    //   9: dup
    //   10: aload_0
    //   11: invokespecial getBinaryData : ()[B
    //   14: invokespecial <init> : ([B)V
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: areturn
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	22	finally
  }
  
  public InputStream getBinaryStream(long paramLong1, long paramLong2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: lload_1
    //   7: lconst_1
    //   8: lcmp
    //   9: iflt -> 88
    //   12: lload_1
    //   13: lconst_1
    //   14: lsub
    //   15: lstore_1
    //   16: aload_0
    //   17: getfield binaryData : [B
    //   20: astore #5
    //   22: lload_1
    //   23: aload #5
    //   25: arraylength
    //   26: i2l
    //   27: lcmp
    //   28: ifgt -> 76
    //   31: lload_1
    //   32: lload_3
    //   33: ladd
    //   34: aload #5
    //   36: arraylength
    //   37: i2l
    //   38: lcmp
    //   39: ifgt -> 64
    //   42: new java/io/ByteArrayInputStream
    //   45: dup
    //   46: aload_0
    //   47: invokespecial getBinaryData : ()[B
    //   50: lload_1
    //   51: l2i
    //   52: lload_3
    //   53: l2i
    //   54: invokespecial <init> : ([BII)V
    //   57: astore #5
    //   59: aload_0
    //   60: monitorexit
    //   61: aload #5
    //   63: areturn
    //   64: ldc '"pos" + "length" arguments can not be larger than the BLOB's length.'
    //   66: ldc 'S1009'
    //   68: aload_0
    //   69: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   72: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   75: athrow
    //   76: ldc '"pos" argument can not be larger than the BLOB's length.'
    //   78: ldc 'S1009'
    //   80: aload_0
    //   81: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   84: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   87: athrow
    //   88: ldc '"pos" argument can not be < 1.'
    //   90: ldc 'S1009'
    //   92: aload_0
    //   93: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   96: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   99: athrow
    //   100: astore #5
    //   102: aload_0
    //   103: monitorexit
    //   104: aload #5
    //   106: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	100	finally
    //   16	59	100	finally
    //   64	76	100	finally
    //   76	88	100	finally
    //   88	100	100	finally
  }
  
  public byte[] getBytes(long paramLong, int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: lload_1
    //   7: lconst_1
    //   8: lcmp
    //   9: iflt -> 90
    //   12: lload_1
    //   13: lconst_1
    //   14: lsub
    //   15: lstore_1
    //   16: aload_0
    //   17: getfield binaryData : [B
    //   20: astore #4
    //   22: lload_1
    //   23: aload #4
    //   25: arraylength
    //   26: i2l
    //   27: lcmp
    //   28: ifgt -> 78
    //   31: iload_3
    //   32: i2l
    //   33: lload_1
    //   34: ladd
    //   35: aload #4
    //   37: arraylength
    //   38: i2l
    //   39: lcmp
    //   40: ifgt -> 66
    //   43: iload_3
    //   44: newarray byte
    //   46: astore #4
    //   48: aload_0
    //   49: invokespecial getBinaryData : ()[B
    //   52: lload_1
    //   53: l2i
    //   54: aload #4
    //   56: iconst_0
    //   57: iload_3
    //   58: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   61: aload_0
    //   62: monitorexit
    //   63: aload #4
    //   65: areturn
    //   66: ldc '"pos" + "length" arguments can not be larger than the BLOB's length.'
    //   68: ldc 'S1009'
    //   70: aload_0
    //   71: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   74: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   77: athrow
    //   78: ldc '"pos" argument can not be larger than the BLOB's length.'
    //   80: ldc 'S1009'
    //   82: aload_0
    //   83: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   86: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   89: athrow
    //   90: ldc 'Blob.2'
    //   92: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   95: ldc 'S1009'
    //   97: aload_0
    //   98: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   101: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   104: athrow
    //   105: astore #4
    //   107: aload_0
    //   108: monitorexit
    //   109: aload #4
    //   111: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	105	finally
    //   16	61	105	finally
    //   66	78	105	finally
    //   78	90	105	finally
    //   90	105	105	finally
  }
  
  public long length() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial getBinaryData : ()[B
    //   10: arraylength
    //   11: istore_1
    //   12: iload_1
    //   13: i2l
    //   14: lstore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: lload_2
    //   18: lreturn
    //   19: astore #4
    //   21: aload_0
    //   22: monitorexit
    //   23: aload #4
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	19	finally
  }
  
  public long position(Blob paramBlob, long paramLong) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: aload_1
    //   8: lconst_0
    //   9: aload_1
    //   10: invokeinterface length : ()J
    //   15: l2i
    //   16: invokeinterface getBytes : (JI)[B
    //   21: lload_2
    //   22: invokevirtual position : ([BJ)J
    //   25: lstore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: lload_2
    //   29: lreturn
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	30	finally
  }
  
  public long position(byte[] paramArrayOfbyte, long paramLong) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'Not implemented'
    //   4: aload_0
    //   5: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   8: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   11: athrow
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	12	finally
  }
  
  public OutputStream setBinaryStream(long paramLong) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: lload_1
    //   7: lconst_1
    //   8: lcmp
    //   9: iflt -> 48
    //   12: new com/mysql/jdbc/WatchableOutputStream
    //   15: astore_3
    //   16: aload_3
    //   17: invokespecial <init> : ()V
    //   20: aload_3
    //   21: aload_0
    //   22: invokevirtual setWatcher : (Lcom/mysql/jdbc/OutputStreamWatcher;)V
    //   25: lload_1
    //   26: lconst_0
    //   27: lcmp
    //   28: ifle -> 44
    //   31: aload_3
    //   32: aload_0
    //   33: getfield binaryData : [B
    //   36: iconst_0
    //   37: lload_1
    //   38: lconst_1
    //   39: lsub
    //   40: l2i
    //   41: invokevirtual write : ([BII)V
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_3
    //   47: areturn
    //   48: ldc 'Blob.0'
    //   50: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   53: ldc 'S1009'
    //   55: aload_0
    //   56: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   59: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   62: athrow
    //   63: astore_3
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_3
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	63	finally
    //   12	25	63	finally
    //   31	44	63	finally
    //   48	63	63	finally
  }
  
  public int setBytes(long paramLong, byte[] paramArrayOfbyte) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: lload_1
    //   8: aload_3
    //   9: iconst_0
    //   10: aload_3
    //   11: arraylength
    //   12: invokevirtual setBytes : (J[BII)I
    //   15: istore #4
    //   17: aload_0
    //   18: monitorexit
    //   19: iload #4
    //   21: ireturn
    //   22: astore_3
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_3
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	22	finally
  }
  
  public int setBytes(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: lload_1
    //   8: invokevirtual setBinaryStream : (J)Ljava/io/OutputStream;
    //   11: astore #6
    //   13: aload #6
    //   15: aload_3
    //   16: iload #4
    //   18: iload #5
    //   20: invokevirtual write : ([BII)V
    //   23: aload #6
    //   25: invokevirtual close : ()V
    //   28: aload_0
    //   29: monitorexit
    //   30: iload #5
    //   32: ireturn
    //   33: astore_3
    //   34: goto -> 64
    //   37: astore_3
    //   38: ldc 'Blob.1'
    //   40: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   43: ldc 'S1000'
    //   45: aload_0
    //   46: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   49: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   52: astore #7
    //   54: aload #7
    //   56: aload_3
    //   57: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   60: pop
    //   61: aload #7
    //   63: athrow
    //   64: aload #6
    //   66: invokevirtual close : ()V
    //   69: aload_3
    //   70: athrow
    //   71: astore_3
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_3
    //   75: athrow
    //   76: astore_3
    //   77: goto -> 28
    //   80: astore #6
    //   82: goto -> 69
    // Exception table:
    //   from	to	target	type
    //   2	13	71	finally
    //   13	23	37	java/io/IOException
    //   13	23	33	finally
    //   23	28	76	java/io/IOException
    //   23	28	71	finally
    //   38	64	33	finally
    //   64	69	80	java/io/IOException
    //   64	69	71	finally
    //   69	71	71	finally
  }
  
  public void streamClosed(WatchableOutputStream paramWatchableOutputStream) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual size : ()I
    //   6: istore_2
    //   7: aload_0
    //   8: getfield binaryData : [B
    //   11: astore_3
    //   12: iload_2
    //   13: aload_3
    //   14: arraylength
    //   15: if_icmpge -> 28
    //   18: aload_1
    //   19: aload_3
    //   20: iload_2
    //   21: aload_3
    //   22: arraylength
    //   23: iload_2
    //   24: isub
    //   25: invokevirtual write : ([BII)V
    //   28: aload_0
    //   29: aload_1
    //   30: invokevirtual toByteArray : ()[B
    //   33: putfield binaryData : [B
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	39	finally
    //   28	36	39	finally
  }
  
  public void streamClosed(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield binaryData : [B
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void truncate(long paramLong) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: lload_1
    //   7: lconst_0
    //   8: lcmp
    //   9: iflt -> 64
    //   12: lload_1
    //   13: aload_0
    //   14: getfield binaryData : [B
    //   17: arraylength
    //   18: i2l
    //   19: lcmp
    //   20: ifgt -> 52
    //   23: lload_1
    //   24: l2i
    //   25: istore_3
    //   26: iload_3
    //   27: newarray byte
    //   29: astore #4
    //   31: aload_0
    //   32: invokespecial getBinaryData : ()[B
    //   35: iconst_0
    //   36: aload #4
    //   38: iconst_0
    //   39: iload_3
    //   40: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   43: aload_0
    //   44: aload #4
    //   46: putfield binaryData : [B
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: ldc '"len" argument can not be larger than the BLOB's length.'
    //   54: ldc 'S1009'
    //   56: aload_0
    //   57: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   60: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   63: athrow
    //   64: ldc '"len" argument can not be < 1.'
    //   66: ldc 'S1009'
    //   68: aload_0
    //   69: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   72: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   75: athrow
    //   76: astore #4
    //   78: aload_0
    //   79: monitorexit
    //   80: aload #4
    //   82: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	76	finally
    //   12	23	76	finally
    //   26	49	76	finally
    //   52	64	76	finally
    //   64	76	76	finally
  }
}
