package okhttp3.internal.http2;

import I丨L.I1I;
import I丨L.I丨L;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Http2Writer implements Closeable {
  private static final Logger logger = Logger.getLogger(Http2.class.getName());
  
  private final boolean client;
  
  private boolean closed;
  
  private final I1I hpackBuffer;
  
  public final Hpack.Writer hpackWriter;
  
  private int maxFrameSize;
  
  private final I丨L sink;
  
  public Http2Writer(I丨L paramI丨L, boolean paramBoolean) {
    this.sink = paramI丨L;
    this.client = paramBoolean;
    paramI丨L = new I1I();
    this.hpackBuffer = (I1I)paramI丨L;
    this.hpackWriter = new Hpack.Writer((I1I)paramI丨L);
    this.maxFrameSize = 16384;
  }
  
  private void writeContinuationFrames(int paramInt, long paramLong) throws IOException {
    while (paramLong > 0L) {
      boolean bool;
      int i = (int)Math.min(this.maxFrameSize, paramLong);
      long l = i;
      paramLong -= l;
      if (paramLong == 0L) {
        bool = true;
      } else {
        bool = false;
      } 
      frameHeader(paramInt, i, (byte)9, bool);
      this.sink.write(this.hpackBuffer, l);
    } 
  }
  
  private static void writeMedium(I丨L paramI丨L, int paramInt) throws IOException {
    paramI丨L.writeByte(paramInt >>> 16 & 0xFF);
    paramI丨L.writeByte(paramInt >>> 8 & 0xFF);
    paramI丨L.writeByte(paramInt & 0xFF);
  }
  
  public void applyAndAckSettings(Settings paramSettings) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 60
    //   9: aload_0
    //   10: aload_1
    //   11: aload_0
    //   12: getfield maxFrameSize : I
    //   15: invokevirtual getMaxFrameSize : (I)I
    //   18: putfield maxFrameSize : I
    //   21: aload_1
    //   22: invokevirtual getHeaderTableSize : ()I
    //   25: iconst_m1
    //   26: if_icmpeq -> 40
    //   29: aload_0
    //   30: getfield hpackWriter : Lokhttp3/internal/http2/Hpack$Writer;
    //   33: aload_1
    //   34: invokevirtual getHeaderTableSize : ()I
    //   37: invokevirtual setHeaderTableSizeSetting : (I)V
    //   40: aload_0
    //   41: iconst_0
    //   42: iconst_0
    //   43: iconst_4
    //   44: iconst_1
    //   45: invokevirtual frameHeader : (IIBB)V
    //   48: aload_0
    //   49: getfield sink : LI丨L/I丨L;
    //   52: invokeinterface flush : ()V
    //   57: aload_0
    //   58: monitorexit
    //   59: return
    //   60: new java/io/IOException
    //   63: astore_1
    //   64: aload_1
    //   65: ldc 'closed'
    //   67: invokespecial <init> : (Ljava/lang/String;)V
    //   70: aload_1
    //   71: athrow
    //   72: astore_1
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_1
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   2	40	72	finally
    //   40	57	72	finally
    //   60	72	72	finally
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield closed : Z
    //   7: aload_0
    //   8: getfield sink : LI丨L/I丨L;
    //   11: invokeinterface close : ()V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  public void connectionPreface() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 85
    //   9: aload_0
    //   10: getfield client : Z
    //   13: istore_1
    //   14: iload_1
    //   15: ifne -> 21
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: getstatic okhttp3/internal/http2/Http2Writer.logger : Ljava/util/logging/Logger;
    //   24: astore_2
    //   25: aload_2
    //   26: getstatic java/util/logging/Level.FINE : Ljava/util/logging/Level;
    //   29: invokevirtual isLoggable : (Ljava/util/logging/Level;)Z
    //   32: ifeq -> 57
    //   35: aload_2
    //   36: ldc '>> CONNECTION %s'
    //   38: iconst_1
    //   39: anewarray java/lang/Object
    //   42: dup
    //   43: iconst_0
    //   44: getstatic okhttp3/internal/http2/Http2.CONNECTION_PREFACE : LI丨L/iI丨LLL1;
    //   47: invokevirtual hex : ()Ljava/lang/String;
    //   50: aastore
    //   51: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   54: invokevirtual fine : (Ljava/lang/String;)V
    //   57: aload_0
    //   58: getfield sink : LI丨L/I丨L;
    //   61: getstatic okhttp3/internal/http2/Http2.CONNECTION_PREFACE : LI丨L/iI丨LLL1;
    //   64: invokevirtual toByteArray : ()[B
    //   67: invokeinterface write : ([B)LI丨L/I丨L;
    //   72: pop
    //   73: aload_0
    //   74: getfield sink : LI丨L/I丨L;
    //   77: invokeinterface flush : ()V
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: new java/io/IOException
    //   88: astore_2
    //   89: aload_2
    //   90: ldc 'closed'
    //   92: invokespecial <init> : (Ljava/lang/String;)V
    //   95: aload_2
    //   96: athrow
    //   97: astore_2
    //   98: aload_0
    //   99: monitorexit
    //   100: aload_2
    //   101: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	97	finally
    //   21	57	97	finally
    //   57	82	97	finally
    //   85	97	97	finally
  }
  
  public void data(boolean paramBoolean, int paramInt1, I1I paramI1I, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 33
    //   9: iconst_0
    //   10: istore #5
    //   12: iload_1
    //   13: ifeq -> 20
    //   16: iconst_1
    //   17: i2b
    //   18: istore #5
    //   20: aload_0
    //   21: iload_2
    //   22: iload #5
    //   24: aload_3
    //   25: iload #4
    //   27: invokevirtual dataFrame : (IBLI丨L/I1I;I)V
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: new java/io/IOException
    //   36: astore_3
    //   37: aload_3
    //   38: ldc 'closed'
    //   40: invokespecial <init> : (Ljava/lang/String;)V
    //   43: aload_3
    //   44: athrow
    //   45: astore_3
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_3
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	45	finally
    //   20	30	45	finally
    //   33	45	45	finally
  }
  
  public void dataFrame(int paramInt1, byte paramByte, I1I paramI1I, int paramInt2) throws IOException {
    frameHeader(paramInt1, paramInt2, (byte)0, paramByte);
    if (paramInt2 > 0)
      this.sink.write(paramI1I, paramInt2); 
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 21
    //   9: aload_0
    //   10: getfield sink : LI丨L/I丨L;
    //   13: invokeinterface flush : ()V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: new java/io/IOException
    //   24: astore_1
    //   25: aload_1
    //   26: ldc 'closed'
    //   28: invokespecial <init> : (Ljava/lang/String;)V
    //   31: aload_1
    //   32: athrow
    //   33: astore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	33	finally
    //   21	33	33	finally
  }
  
  public void frameHeader(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2) throws IOException {
    Logger logger = logger;
    if (logger.isLoggable(Level.FINE))
      logger.fine(Http2.frameLog(false, paramInt1, paramInt2, paramByte1, paramByte2)); 
    int i = this.maxFrameSize;
    if (paramInt2 <= i) {
      if ((Integer.MIN_VALUE & paramInt1) == 0) {
        writeMedium(this.sink, paramInt2);
        this.sink.writeByte(paramByte1 & 0xFF);
        this.sink.writeByte(paramByte2 & 0xFF);
        this.sink.writeInt(paramInt1 & Integer.MAX_VALUE);
        return;
      } 
      throw Http2.illegalArgument("reserved bit set: %s", new Object[] { Integer.valueOf(paramInt1) });
    } 
    throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", new Object[] { Integer.valueOf(i), Integer.valueOf(paramInt2) });
  }
  
  public void goAway(int paramInt, ErrorCode paramErrorCode, byte[] paramArrayOfbyte) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 93
    //   9: aload_2
    //   10: getfield httpCode : I
    //   13: iconst_m1
    //   14: if_icmpeq -> 83
    //   17: aload_0
    //   18: iconst_0
    //   19: aload_3
    //   20: arraylength
    //   21: bipush #8
    //   23: iadd
    //   24: bipush #7
    //   26: iconst_0
    //   27: invokevirtual frameHeader : (IIBB)V
    //   30: aload_0
    //   31: getfield sink : LI丨L/I丨L;
    //   34: iload_1
    //   35: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   40: pop
    //   41: aload_0
    //   42: getfield sink : LI丨L/I丨L;
    //   45: aload_2
    //   46: getfield httpCode : I
    //   49: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   54: pop
    //   55: aload_3
    //   56: arraylength
    //   57: ifle -> 71
    //   60: aload_0
    //   61: getfield sink : LI丨L/I丨L;
    //   64: aload_3
    //   65: invokeinterface write : ([B)LI丨L/I丨L;
    //   70: pop
    //   71: aload_0
    //   72: getfield sink : LI丨L/I丨L;
    //   75: invokeinterface flush : ()V
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: ldc 'errorCode.httpCode == -1'
    //   85: iconst_0
    //   86: anewarray java/lang/Object
    //   89: invokestatic illegalArgument : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/IllegalArgumentException;
    //   92: athrow
    //   93: new java/io/IOException
    //   96: astore_2
    //   97: aload_2
    //   98: ldc 'closed'
    //   100: invokespecial <init> : (Ljava/lang/String;)V
    //   103: aload_2
    //   104: athrow
    //   105: astore_2
    //   106: aload_0
    //   107: monitorexit
    //   108: aload_2
    //   109: athrow
    // Exception table:
    //   from	to	target	type
    //   2	71	105	finally
    //   71	80	105	finally
    //   83	93	105	finally
    //   93	105	105	finally
  }
  
  public void headers(boolean paramBoolean, int paramInt, List<Header> paramList) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 123
    //   9: aload_0
    //   10: getfield hpackWriter : Lokhttp3/internal/http2/Hpack$Writer;
    //   13: aload_3
    //   14: invokevirtual writeHeaders : (Ljava/util/List;)V
    //   17: aload_0
    //   18: getfield hpackBuffer : LI丨L/I1I;
    //   21: invokevirtual iI1i丨I : ()J
    //   24: lstore #8
    //   26: aload_0
    //   27: getfield maxFrameSize : I
    //   30: i2l
    //   31: lload #8
    //   33: invokestatic min : (JJ)J
    //   36: l2i
    //   37: istore #6
    //   39: iload #6
    //   41: i2l
    //   42: lstore #10
    //   44: lload #8
    //   46: lload #10
    //   48: lcmp
    //   49: istore #7
    //   51: iload #7
    //   53: ifne -> 62
    //   56: iconst_4
    //   57: istore #4
    //   59: goto -> 65
    //   62: iconst_0
    //   63: istore #4
    //   65: iload #4
    //   67: istore #5
    //   69: iload_1
    //   70: ifeq -> 80
    //   73: iload #4
    //   75: iconst_1
    //   76: ior
    //   77: i2b
    //   78: istore #5
    //   80: aload_0
    //   81: iload_2
    //   82: iload #6
    //   84: iconst_1
    //   85: iload #5
    //   87: invokevirtual frameHeader : (IIBB)V
    //   90: aload_0
    //   91: getfield sink : LI丨L/I丨L;
    //   94: aload_0
    //   95: getfield hpackBuffer : LI丨L/I1I;
    //   98: lload #10
    //   100: invokeinterface write : (LI丨L/I1I;J)V
    //   105: iload #7
    //   107: ifle -> 120
    //   110: aload_0
    //   111: iload_2
    //   112: lload #8
    //   114: lload #10
    //   116: lsub
    //   117: invokespecial writeContinuationFrames : (IJ)V
    //   120: aload_0
    //   121: monitorexit
    //   122: return
    //   123: new java/io/IOException
    //   126: astore_3
    //   127: aload_3
    //   128: ldc 'closed'
    //   130: invokespecial <init> : (Ljava/lang/String;)V
    //   133: aload_3
    //   134: athrow
    //   135: astore_3
    //   136: aload_0
    //   137: monitorexit
    //   138: aload_3
    //   139: athrow
    // Exception table:
    //   from	to	target	type
    //   2	39	135	finally
    //   80	105	135	finally
    //   110	120	135	finally
    //   123	135	135	finally
  }
  
  public int maxDataLength() {
    return this.maxFrameSize;
  }
  
  public void ping(boolean paramBoolean, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 67
    //   9: iload_1
    //   10: ifeq -> 19
    //   13: iconst_1
    //   14: istore #4
    //   16: goto -> 22
    //   19: iconst_0
    //   20: istore #4
    //   22: aload_0
    //   23: iconst_0
    //   24: bipush #8
    //   26: bipush #6
    //   28: iload #4
    //   30: invokevirtual frameHeader : (IIBB)V
    //   33: aload_0
    //   34: getfield sink : LI丨L/I丨L;
    //   37: iload_2
    //   38: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   43: pop
    //   44: aload_0
    //   45: getfield sink : LI丨L/I丨L;
    //   48: iload_3
    //   49: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   54: pop
    //   55: aload_0
    //   56: getfield sink : LI丨L/I丨L;
    //   59: invokeinterface flush : ()V
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: new java/io/IOException
    //   70: astore #5
    //   72: aload #5
    //   74: ldc 'closed'
    //   76: invokespecial <init> : (Ljava/lang/String;)V
    //   79: aload #5
    //   81: athrow
    //   82: astore #5
    //   84: aload_0
    //   85: monitorexit
    //   86: aload #5
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	82	finally
    //   22	64	82	finally
    //   67	82	82	finally
  }
  
  public void pushPromise(int paramInt1, int paramInt2, List<Header> paramList) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 126
    //   9: aload_0
    //   10: getfield hpackWriter : Lokhttp3/internal/http2/Hpack$Writer;
    //   13: aload_3
    //   14: invokevirtual writeHeaders : (Ljava/util/List;)V
    //   17: aload_0
    //   18: getfield hpackBuffer : LI丨L/I1I;
    //   21: invokevirtual iI1i丨I : ()J
    //   24: lstore #7
    //   26: aload_0
    //   27: getfield maxFrameSize : I
    //   30: iconst_4
    //   31: isub
    //   32: i2l
    //   33: lload #7
    //   35: invokestatic min : (JJ)J
    //   38: l2i
    //   39: istore #6
    //   41: iload #6
    //   43: i2l
    //   44: lstore #9
    //   46: lload #7
    //   48: lload #9
    //   50: lcmp
    //   51: istore #5
    //   53: iload #5
    //   55: ifne -> 64
    //   58: iconst_4
    //   59: istore #4
    //   61: goto -> 67
    //   64: iconst_0
    //   65: istore #4
    //   67: aload_0
    //   68: iload_1
    //   69: iload #6
    //   71: iconst_4
    //   72: iadd
    //   73: iconst_5
    //   74: iload #4
    //   76: invokevirtual frameHeader : (IIBB)V
    //   79: aload_0
    //   80: getfield sink : LI丨L/I丨L;
    //   83: iload_2
    //   84: ldc 2147483647
    //   86: iand
    //   87: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   92: pop
    //   93: aload_0
    //   94: getfield sink : LI丨L/I丨L;
    //   97: aload_0
    //   98: getfield hpackBuffer : LI丨L/I1I;
    //   101: lload #9
    //   103: invokeinterface write : (LI丨L/I1I;J)V
    //   108: iload #5
    //   110: ifle -> 123
    //   113: aload_0
    //   114: iload_1
    //   115: lload #7
    //   117: lload #9
    //   119: lsub
    //   120: invokespecial writeContinuationFrames : (IJ)V
    //   123: aload_0
    //   124: monitorexit
    //   125: return
    //   126: new java/io/IOException
    //   129: astore_3
    //   130: aload_3
    //   131: ldc 'closed'
    //   133: invokespecial <init> : (Ljava/lang/String;)V
    //   136: aload_3
    //   137: athrow
    //   138: astore_3
    //   139: aload_0
    //   140: monitorexit
    //   141: aload_3
    //   142: athrow
    // Exception table:
    //   from	to	target	type
    //   2	41	138	finally
    //   67	108	138	finally
    //   113	123	138	finally
    //   126	138	138	finally
  }
  
  public void rstStream(int paramInt, ErrorCode paramErrorCode) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 61
    //   9: aload_2
    //   10: getfield httpCode : I
    //   13: iconst_m1
    //   14: if_icmpeq -> 51
    //   17: aload_0
    //   18: iload_1
    //   19: iconst_4
    //   20: iconst_3
    //   21: iconst_0
    //   22: invokevirtual frameHeader : (IIBB)V
    //   25: aload_0
    //   26: getfield sink : LI丨L/I丨L;
    //   29: aload_2
    //   30: getfield httpCode : I
    //   33: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   38: pop
    //   39: aload_0
    //   40: getfield sink : LI丨L/I丨L;
    //   43: invokeinterface flush : ()V
    //   48: aload_0
    //   49: monitorexit
    //   50: return
    //   51: new java/lang/IllegalArgumentException
    //   54: astore_2
    //   55: aload_2
    //   56: invokespecial <init> : ()V
    //   59: aload_2
    //   60: athrow
    //   61: new java/io/IOException
    //   64: astore_2
    //   65: aload_2
    //   66: ldc 'closed'
    //   68: invokespecial <init> : (Ljava/lang/String;)V
    //   71: aload_2
    //   72: athrow
    //   73: astore_2
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_2
    //   77: athrow
    // Exception table:
    //   from	to	target	type
    //   2	48	73	finally
    //   51	61	73	finally
    //   61	73	73	finally
  }
  
  public void settings(Settings paramSettings) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 111
    //   9: aload_1
    //   10: invokevirtual size : ()I
    //   13: istore_3
    //   14: iconst_0
    //   15: istore_2
    //   16: aload_0
    //   17: iconst_0
    //   18: iload_3
    //   19: bipush #6
    //   21: imul
    //   22: iconst_4
    //   23: iconst_0
    //   24: invokevirtual frameHeader : (IIBB)V
    //   27: iload_2
    //   28: bipush #10
    //   30: if_icmpge -> 99
    //   33: aload_1
    //   34: iload_2
    //   35: invokevirtual isSet : (I)Z
    //   38: ifne -> 44
    //   41: goto -> 93
    //   44: iload_2
    //   45: iconst_4
    //   46: if_icmpne -> 54
    //   49: iconst_3
    //   50: istore_3
    //   51: goto -> 67
    //   54: iload_2
    //   55: bipush #7
    //   57: if_icmpne -> 65
    //   60: iconst_4
    //   61: istore_3
    //   62: goto -> 67
    //   65: iload_2
    //   66: istore_3
    //   67: aload_0
    //   68: getfield sink : LI丨L/I丨L;
    //   71: iload_3
    //   72: invokeinterface writeShort : (I)LI丨L/I丨L;
    //   77: pop
    //   78: aload_0
    //   79: getfield sink : LI丨L/I丨L;
    //   82: aload_1
    //   83: iload_2
    //   84: invokevirtual get : (I)I
    //   87: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   92: pop
    //   93: iinc #2, 1
    //   96: goto -> 27
    //   99: aload_0
    //   100: getfield sink : LI丨L/I丨L;
    //   103: invokeinterface flush : ()V
    //   108: aload_0
    //   109: monitorexit
    //   110: return
    //   111: new java/io/IOException
    //   114: astore_1
    //   115: aload_1
    //   116: ldc 'closed'
    //   118: invokespecial <init> : (Ljava/lang/String;)V
    //   121: aload_1
    //   122: athrow
    //   123: astore_1
    //   124: aload_0
    //   125: monitorexit
    //   126: aload_1
    //   127: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	123	finally
    //   16	27	123	finally
    //   33	41	123	finally
    //   67	93	123	finally
    //   99	108	123	finally
    //   111	123	123	finally
  }
  
  public void windowUpdate(int paramInt, long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifne -> 73
    //   9: lload_2
    //   10: lconst_0
    //   11: lcmp
    //   12: ifeq -> 56
    //   15: lload_2
    //   16: ldc2_w 2147483647
    //   19: lcmp
    //   20: ifgt -> 56
    //   23: aload_0
    //   24: iload_1
    //   25: iconst_4
    //   26: bipush #8
    //   28: iconst_0
    //   29: invokevirtual frameHeader : (IIBB)V
    //   32: aload_0
    //   33: getfield sink : LI丨L/I丨L;
    //   36: lload_2
    //   37: l2i
    //   38: invokeinterface writeInt : (I)LI丨L/I丨L;
    //   43: pop
    //   44: aload_0
    //   45: getfield sink : LI丨L/I丨L;
    //   48: invokeinterface flush : ()V
    //   53: aload_0
    //   54: monitorexit
    //   55: return
    //   56: ldc 'windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s'
    //   58: iconst_1
    //   59: anewarray java/lang/Object
    //   62: dup
    //   63: iconst_0
    //   64: lload_2
    //   65: invokestatic valueOf : (J)Ljava/lang/Long;
    //   68: aastore
    //   69: invokestatic illegalArgument : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/IllegalArgumentException;
    //   72: athrow
    //   73: new java/io/IOException
    //   76: astore #4
    //   78: aload #4
    //   80: ldc 'closed'
    //   82: invokespecial <init> : (Ljava/lang/String;)V
    //   85: aload #4
    //   87: athrow
    //   88: astore #4
    //   90: aload_0
    //   91: monitorexit
    //   92: aload #4
    //   94: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	88	finally
    //   23	53	88	finally
    //   56	73	88	finally
    //   73	88	88	finally
  }
}
