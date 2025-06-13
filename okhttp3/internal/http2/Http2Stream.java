package okhttp3.internal.http2;

import I丨L.I11L;
import I丨L.I11li1;
import I丨L.I1I;
import I丨L.IL1Iii;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.internal.Util;

public final class Http2Stream {
  public static final boolean $assertionsDisabled = false;
  
  public long bytesLeftInWriteWindow;
  
  public final Http2Connection connection;
  
  @Nullable
  public ErrorCode errorCode;
  
  @Nullable
  public IOException errorException;
  
  private boolean hasResponseHeaders;
  
  private final Deque<Headers> headersQueue;
  
  public final int id;
  
  public final StreamTimeout readTimeout;
  
  public final FramingSink sink;
  
  private final FramingSource source;
  
  public long unacknowledgedBytesRead = 0L;
  
  public final StreamTimeout writeTimeout;
  
  public Http2Stream(int paramInt, Http2Connection paramHttp2Connection, boolean paramBoolean1, boolean paramBoolean2, @Nullable Headers paramHeaders) {
    ArrayDeque<Headers> arrayDeque = new ArrayDeque();
    this.headersQueue = arrayDeque;
    this.readTimeout = new StreamTimeout();
    this.writeTimeout = new StreamTimeout();
    Objects.requireNonNull(paramHttp2Connection, "connection == null");
    this.id = paramInt;
    this.connection = paramHttp2Connection;
    this.bytesLeftInWriteWindow = paramHttp2Connection.peerSettings.getInitialWindowSize();
    FramingSource framingSource = new FramingSource(paramHttp2Connection.okHttpSettings.getInitialWindowSize());
    this.source = framingSource;
    FramingSink framingSink = new FramingSink();
    this.sink = framingSink;
    framingSource.finished = paramBoolean2;
    framingSink.finished = paramBoolean1;
    if (paramHeaders != null)
      arrayDeque.add(paramHeaders); 
    if (!isLocallyInitiated() || paramHeaders == null) {
      if (isLocallyInitiated() || paramHeaders != null)
        return; 
      throw new IllegalStateException("remotely-initiated streams should have headers");
    } 
    throw new IllegalStateException("locally-initiated streams shouldn't have headers yet");
  }
  
  private boolean closeInternal(ErrorCode paramErrorCode, @Nullable IOException paramIOException) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   6: ifnull -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_0
    //   14: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   17: getfield finished : Z
    //   20: ifeq -> 37
    //   23: aload_0
    //   24: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   27: getfield finished : Z
    //   30: ifeq -> 37
    //   33: aload_0
    //   34: monitorexit
    //   35: iconst_0
    //   36: ireturn
    //   37: aload_0
    //   38: aload_1
    //   39: putfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   42: aload_0
    //   43: aload_2
    //   44: putfield errorException : Ljava/io/IOException;
    //   47: aload_0
    //   48: invokevirtual notifyAll : ()V
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_0
    //   54: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   57: aload_0
    //   58: getfield id : I
    //   61: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   64: pop
    //   65: iconst_1
    //   66: ireturn
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	67	finally
    //   13	35	67	finally
    //   37	53	67	finally
    //   68	70	67	finally
  }
  
  public void addBytesToWriteWindow(long paramLong) {
    this.bytesLeftInWriteWindow += paramLong;
    if (paramLong > 0L)
      notifyAll(); 
  }
  
  public void cancelStreamIfNecessary() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   6: astore_3
    //   7: aload_3
    //   8: getfield finished : Z
    //   11: ifne -> 45
    //   14: aload_3
    //   15: getfield closed : Z
    //   18: ifeq -> 45
    //   21: aload_0
    //   22: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   25: astore_3
    //   26: aload_3
    //   27: getfield finished : Z
    //   30: ifne -> 40
    //   33: aload_3
    //   34: getfield closed : Z
    //   37: ifeq -> 45
    //   40: iconst_1
    //   41: istore_1
    //   42: goto -> 47
    //   45: iconst_0
    //   46: istore_1
    //   47: aload_0
    //   48: invokevirtual isOpen : ()Z
    //   51: istore_2
    //   52: aload_0
    //   53: monitorexit
    //   54: iload_1
    //   55: ifeq -> 69
    //   58: aload_0
    //   59: getstatic okhttp3/internal/http2/ErrorCode.CANCEL : Lokhttp3/internal/http2/ErrorCode;
    //   62: aconst_null
    //   63: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Ljava/io/IOException;)V
    //   66: goto -> 85
    //   69: iload_2
    //   70: ifne -> 85
    //   73: aload_0
    //   74: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   77: aload_0
    //   78: getfield id : I
    //   81: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   84: pop
    //   85: return
    //   86: astore_3
    //   87: aload_0
    //   88: monitorexit
    //   89: aload_3
    //   90: athrow
    // Exception table:
    //   from	to	target	type
    //   2	40	86	finally
    //   47	54	86	finally
    //   87	89	86	finally
  }
  
  public void checkOutNotClosed() throws IOException {
    FramingSink framingSink = this.sink;
    if (!framingSink.closed) {
      if (!framingSink.finished) {
        if (this.errorCode != null) {
          IOException iOException = this.errorException;
          if (iOException == null)
            iOException = new StreamResetException(this.errorCode); 
          throw iOException;
        } 
        return;
      } 
      throw new IOException("stream finished");
    } 
    throw new IOException("stream closed");
  }
  
  public void close(ErrorCode paramErrorCode, @Nullable IOException paramIOException) throws IOException {
    if (!closeInternal(paramErrorCode, paramIOException))
      return; 
    this.connection.writeSynReset(this.id, paramErrorCode);
  }
  
  public void closeLater(ErrorCode paramErrorCode) {
    if (!closeInternal(paramErrorCode, null))
      return; 
    this.connection.writeSynResetLater(this.id, paramErrorCode);
  }
  
  public void enqueueTrailers(Headers paramHeaders) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   6: getfield finished : Z
    //   9: ifne -> 43
    //   12: aload_1
    //   13: invokevirtual size : ()I
    //   16: ifeq -> 31
    //   19: aload_0
    //   20: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   23: aload_1
    //   24: invokestatic access$302 : (Lokhttp3/internal/http2/Http2Stream$FramingSink;Lokhttp3/Headers;)Lokhttp3/Headers;
    //   27: pop
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: new java/lang/IllegalArgumentException
    //   34: astore_1
    //   35: aload_1
    //   36: ldc 'trailers.size() == 0'
    //   38: invokespecial <init> : (Ljava/lang/String;)V
    //   41: aload_1
    //   42: athrow
    //   43: new java/lang/IllegalStateException
    //   46: astore_1
    //   47: aload_1
    //   48: ldc 'already finished'
    //   50: invokespecial <init> : (Ljava/lang/String;)V
    //   53: aload_1
    //   54: athrow
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	55	finally
    //   31	43	55	finally
    //   43	55	55	finally
    //   56	58	55	finally
  }
  
  public Http2Connection getConnection() {
    return this.connection;
  }
  
  public ErrorCode getErrorCode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
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
  
  public int getId() {
    return this.id;
  }
  
  public I11li1 getSink() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hasResponseHeaders : Z
    //   6: ifne -> 31
    //   9: aload_0
    //   10: invokevirtual isLocallyInitiated : ()Z
    //   13: ifeq -> 19
    //   16: goto -> 31
    //   19: new java/lang/IllegalStateException
    //   22: astore_1
    //   23: aload_1
    //   24: ldc 'reply before requesting the sink'
    //   26: invokespecial <init> : (Ljava/lang/String;)V
    //   29: aload_1
    //   30: athrow
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_0
    //   34: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   37: areturn
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	38	finally
    //   19	31	38	finally
    //   31	33	38	finally
    //   39	41	38	finally
  }
  
  public 丨lL getSource() {
    return this.source;
  }
  
  public boolean isLocallyInitiated() {
    boolean bool;
    int i = this.id;
    boolean bool1 = true;
    if ((i & 0x1) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.connection.client == bool) {
      bool = bool1;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOpen() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: iconst_0
    //   14: ireturn
    //   15: aload_0
    //   16: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   19: astore_2
    //   20: aload_2
    //   21: getfield finished : Z
    //   24: ifne -> 34
    //   27: aload_2
    //   28: getfield closed : Z
    //   31: ifeq -> 66
    //   34: aload_0
    //   35: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   38: astore_2
    //   39: aload_2
    //   40: getfield finished : Z
    //   43: ifne -> 53
    //   46: aload_2
    //   47: getfield closed : Z
    //   50: ifeq -> 66
    //   53: aload_0
    //   54: getfield hasResponseHeaders : Z
    //   57: istore_1
    //   58: iload_1
    //   59: ifeq -> 66
    //   62: aload_0
    //   63: monitorexit
    //   64: iconst_0
    //   65: ireturn
    //   66: aload_0
    //   67: monitorexit
    //   68: iconst_1
    //   69: ireturn
    //   70: astore_2
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_2
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	70	finally
    //   15	34	70	finally
    //   34	53	70	finally
    //   53	58	70	finally
  }
  
  public I11L readTimeout() {
    return this.readTimeout;
  }
  
  public void receiveData(l丨Li1LL paraml丨Li1LL, int paramInt) throws IOException {
    this.source.receive(paraml丨Li1LL, paramInt);
  }
  
  public void receiveHeaders(Headers paramHeaders, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hasResponseHeaders : Z
    //   6: ifeq -> 28
    //   9: iload_2
    //   10: ifne -> 16
    //   13: goto -> 28
    //   16: aload_0
    //   17: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   20: aload_1
    //   21: invokestatic access$202 : (Lokhttp3/internal/http2/Http2Stream$FramingSource;Lokhttp3/Headers;)Lokhttp3/Headers;
    //   24: pop
    //   25: goto -> 44
    //   28: aload_0
    //   29: iconst_1
    //   30: putfield hasResponseHeaders : Z
    //   33: aload_0
    //   34: getfield headersQueue : Ljava/util/Deque;
    //   37: aload_1
    //   38: invokeinterface add : (Ljava/lang/Object;)Z
    //   43: pop
    //   44: iload_2
    //   45: ifeq -> 56
    //   48: aload_0
    //   49: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   52: iconst_1
    //   53: putfield finished : Z
    //   56: aload_0
    //   57: invokevirtual isOpen : ()Z
    //   60: istore_2
    //   61: aload_0
    //   62: invokevirtual notifyAll : ()V
    //   65: aload_0
    //   66: monitorexit
    //   67: iload_2
    //   68: ifne -> 83
    //   71: aload_0
    //   72: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   75: aload_0
    //   76: getfield id : I
    //   79: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   82: pop
    //   83: return
    //   84: astore_1
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_1
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	84	finally
    //   16	25	84	finally
    //   28	44	84	finally
    //   48	56	84	finally
    //   56	67	84	finally
    //   85	87	84	finally
  }
  
  public void receiveRstStream(ErrorCode paramErrorCode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   6: ifnonnull -> 18
    //   9: aload_0
    //   10: aload_1
    //   11: putfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   14: aload_0
    //   15: invokevirtual notifyAll : ()V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  public Headers takeHeaders() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
    //   6: invokevirtual enter : ()V
    //   9: aload_0
    //   10: getfield headersQueue : Ljava/util/Deque;
    //   13: invokeinterface isEmpty : ()Z
    //   18: ifeq -> 35
    //   21: aload_0
    //   22: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   25: ifnonnull -> 35
    //   28: aload_0
    //   29: invokevirtual waitForIo : ()V
    //   32: goto -> 9
    //   35: aload_0
    //   36: getfield readTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
    //   39: invokevirtual exitAndThrowIfTimedOut : ()V
    //   42: aload_0
    //   43: getfield headersQueue : Ljava/util/Deque;
    //   46: invokeinterface isEmpty : ()Z
    //   51: ifne -> 71
    //   54: aload_0
    //   55: getfield headersQueue : Ljava/util/Deque;
    //   58: invokeinterface removeFirst : ()Ljava/lang/Object;
    //   63: checkcast okhttp3/Headers
    //   66: astore_1
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_1
    //   70: areturn
    //   71: aload_0
    //   72: getfield errorException : Ljava/io/IOException;
    //   75: astore_1
    //   76: aload_1
    //   77: ifnull -> 83
    //   80: goto -> 95
    //   83: new okhttp3/internal/http2/StreamResetException
    //   86: dup
    //   87: aload_0
    //   88: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   91: invokespecial <init> : (Lokhttp3/internal/http2/ErrorCode;)V
    //   94: astore_1
    //   95: aload_1
    //   96: athrow
    //   97: astore_1
    //   98: aload_0
    //   99: getfield readTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
    //   102: invokevirtual exitAndThrowIfTimedOut : ()V
    //   105: aload_1
    //   106: athrow
    //   107: astore_1
    //   108: aload_0
    //   109: monitorexit
    //   110: aload_1
    //   111: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	107	finally
    //   9	32	97	finally
    //   35	67	107	finally
    //   71	76	107	finally
    //   83	95	107	finally
    //   95	97	107	finally
    //   98	107	107	finally
  }
  
  public Headers trailers() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   6: ifnull -> 35
    //   9: aload_0
    //   10: getfield errorException : Ljava/io/IOException;
    //   13: astore_1
    //   14: aload_1
    //   15: ifnull -> 21
    //   18: goto -> 33
    //   21: new okhttp3/internal/http2/StreamResetException
    //   24: dup
    //   25: aload_0
    //   26: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   29: invokespecial <init> : (Lokhttp3/internal/http2/ErrorCode;)V
    //   32: astore_1
    //   33: aload_1
    //   34: athrow
    //   35: aload_0
    //   36: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   39: astore_1
    //   40: aload_1
    //   41: getfield finished : Z
    //   44: ifeq -> 99
    //   47: aload_1
    //   48: invokestatic access$000 : (Lokhttp3/internal/http2/Http2Stream$FramingSource;)LI丨L/I1I;
    //   51: invokevirtual l丨Li1LL : ()Z
    //   54: ifeq -> 99
    //   57: aload_0
    //   58: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   61: invokestatic access$100 : (Lokhttp3/internal/http2/Http2Stream$FramingSource;)LI丨L/I1I;
    //   64: invokevirtual l丨Li1LL : ()Z
    //   67: ifeq -> 99
    //   70: aload_0
    //   71: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   74: invokestatic access$200 : (Lokhttp3/internal/http2/Http2Stream$FramingSource;)Lokhttp3/Headers;
    //   77: ifnull -> 91
    //   80: aload_0
    //   81: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   84: invokestatic access$200 : (Lokhttp3/internal/http2/Http2Stream$FramingSource;)Lokhttp3/Headers;
    //   87: astore_1
    //   88: goto -> 95
    //   91: getstatic okhttp3/internal/Util.EMPTY_HEADERS : Lokhttp3/Headers;
    //   94: astore_1
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_1
    //   98: areturn
    //   99: new java/lang/IllegalStateException
    //   102: astore_1
    //   103: aload_1
    //   104: ldc_w 'too early; can't read the trailers yet'
    //   107: invokespecial <init> : (Ljava/lang/String;)V
    //   110: aload_1
    //   111: athrow
    //   112: astore_1
    //   113: aload_0
    //   114: monitorexit
    //   115: aload_1
    //   116: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	112	finally
    //   21	33	112	finally
    //   33	35	112	finally
    //   35	88	112	finally
    //   91	95	112	finally
    //   99	112	112	finally
  }
  
  public void waitForIo() throws InterruptedIOException {
    try {
      wait();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new InterruptedIOException();
    } 
  }
  
  public void writeHeaders(List<Header> paramList, boolean paramBoolean1, boolean paramBoolean2) throws IOException {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 'headers == null'
    //   4: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: iconst_1
    //   12: putfield hasResponseHeaders : Z
    //   15: iload_2
    //   16: ifeq -> 27
    //   19: aload_0
    //   20: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   23: iconst_1
    //   24: putfield finished : Z
    //   27: aload_0
    //   28: monitorexit
    //   29: iload_3
    //   30: istore #4
    //   32: iload_3
    //   33: ifne -> 79
    //   36: aload_0
    //   37: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   40: astore #5
    //   42: aload #5
    //   44: monitorenter
    //   45: aload_0
    //   46: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   49: getfield bytesLeftInWriteWindow : J
    //   52: lconst_0
    //   53: lcmp
    //   54: ifne -> 62
    //   57: iconst_1
    //   58: istore_3
    //   59: goto -> 64
    //   62: iconst_0
    //   63: istore_3
    //   64: aload #5
    //   66: monitorexit
    //   67: iload_3
    //   68: istore #4
    //   70: goto -> 79
    //   73: astore_1
    //   74: aload #5
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    //   79: aload_0
    //   80: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   83: aload_0
    //   84: getfield id : I
    //   87: iload_2
    //   88: aload_1
    //   89: invokevirtual writeHeaders : (IZLjava/util/List;)V
    //   92: iload #4
    //   94: ifeq -> 104
    //   97: aload_0
    //   98: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   101: invokevirtual flush : ()V
    //   104: return
    //   105: astore_1
    //   106: aload_0
    //   107: monitorexit
    //   108: aload_1
    //   109: athrow
    // Exception table:
    //   from	to	target	type
    //   10	15	105	finally
    //   19	27	105	finally
    //   27	29	105	finally
    //   45	57	73	finally
    //   64	67	73	finally
    //   74	77	73	finally
    //   106	108	105	finally
  }
  
  public I11L writeTimeout() {
    return this.writeTimeout;
  }
  
  public final class FramingSink implements I11li1 {
    public static final boolean $assertionsDisabled = false;
    
    private static final long EMIT_BUFFER_SIZE = 16384L;
    
    public boolean closed;
    
    public boolean finished;
    
    private final I1I sendBuffer = new I1I();
    
    public final Http2Stream this$0;
    
    private Headers trailers;
    
    private void emitFrame(boolean param1Boolean) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   4: astore #4
      //   6: aload #4
      //   8: monitorenter
      //   9: aload_0
      //   10: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   13: getfield writeTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
      //   16: invokevirtual enter : ()V
      //   19: aload_0
      //   20: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   23: astore #5
      //   25: aload #5
      //   27: getfield bytesLeftInWriteWindow : J
      //   30: lconst_0
      //   31: lcmp
      //   32: ifgt -> 65
      //   35: aload_0
      //   36: getfield finished : Z
      //   39: ifne -> 65
      //   42: aload_0
      //   43: getfield closed : Z
      //   46: ifne -> 65
      //   49: aload #5
      //   51: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
      //   54: ifnonnull -> 65
      //   57: aload #5
      //   59: invokevirtual waitForIo : ()V
      //   62: goto -> 19
      //   65: aload #5
      //   67: getfield writeTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
      //   70: invokevirtual exitAndThrowIfTimedOut : ()V
      //   73: aload_0
      //   74: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   77: invokevirtual checkOutNotClosed : ()V
      //   80: aload_0
      //   81: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   84: getfield bytesLeftInWriteWindow : J
      //   87: aload_0
      //   88: getfield sendBuffer : LI丨L/I1I;
      //   91: invokevirtual iI1i丨I : ()J
      //   94: invokestatic min : (JJ)J
      //   97: lstore_2
      //   98: aload_0
      //   99: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   102: astore #5
      //   104: aload #5
      //   106: aload #5
      //   108: getfield bytesLeftInWriteWindow : J
      //   111: lload_2
      //   112: lsub
      //   113: putfield bytesLeftInWriteWindow : J
      //   116: aload #4
      //   118: monitorexit
      //   119: aload #5
      //   121: getfield writeTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
      //   124: invokevirtual enter : ()V
      //   127: iload_1
      //   128: ifeq -> 153
      //   131: lload_2
      //   132: aload_0
      //   133: getfield sendBuffer : LI丨L/I1I;
      //   136: invokevirtual iI1i丨I : ()J
      //   139: lcmp
      //   140: ifne -> 153
      //   143: iconst_1
      //   144: istore_1
      //   145: goto -> 155
      //   148: astore #4
      //   150: goto -> 191
      //   153: iconst_0
      //   154: istore_1
      //   155: aload_0
      //   156: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   159: astore #4
      //   161: aload #4
      //   163: getfield connection : Lokhttp3/internal/http2/Http2Connection;
      //   166: aload #4
      //   168: getfield id : I
      //   171: iload_1
      //   172: aload_0
      //   173: getfield sendBuffer : LI丨L/I1I;
      //   176: lload_2
      //   177: invokevirtual writeData : (IZLI丨L/I1I;J)V
      //   180: aload_0
      //   181: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   184: getfield writeTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
      //   187: invokevirtual exitAndThrowIfTimedOut : ()V
      //   190: return
      //   191: aload_0
      //   192: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   195: getfield writeTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
      //   198: invokevirtual exitAndThrowIfTimedOut : ()V
      //   201: aload #4
      //   203: athrow
      //   204: astore #5
      //   206: aload_0
      //   207: getfield this$0 : Lokhttp3/internal/http2/Http2Stream;
      //   210: getfield writeTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
      //   213: invokevirtual exitAndThrowIfTimedOut : ()V
      //   216: aload #5
      //   218: athrow
      //   219: astore #5
      //   221: aload #4
      //   223: monitorexit
      //   224: aload #5
      //   226: athrow
      // Exception table:
      //   from	to	target	type
      //   9	19	219	finally
      //   19	62	204	finally
      //   65	119	219	finally
      //   131	143	148	finally
      //   155	180	148	finally
      //   206	219	219	finally
      //   221	224	219	finally
    }
    
    public void close() throws IOException {
      synchronized (Http2Stream.this) {
        if (this.closed)
          return; 
        if (!Http2Stream.this.sink.finished) {
          boolean bool1;
          boolean bool2;
          if (this.sendBuffer.iI1i丨I() > 0L) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (this.trailers != null) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
          if (bool2) {
            while (this.sendBuffer.iI1i丨I() > 0L)
              emitFrame(false); 
            null = Http2Stream.this;
            null.connection.writeHeaders(null.id, true, Util.toHeaderBlock(this.trailers));
          } else if (bool1) {
            while (this.sendBuffer.iI1i丨I() > 0L)
              emitFrame(true); 
          } else {
            null = Http2Stream.this;
            null.connection.writeData(null.id, true, null, 0L);
          } 
        } 
        synchronized (Http2Stream.this) {
          this.closed = true;
          Http2Stream.this.connection.flush();
          Http2Stream.this.cancelStreamIfNecessary();
          return;
        } 
      } 
    }
    
    public void flush() throws IOException {
      synchronized (Http2Stream.this) {
        Http2Stream.this.checkOutNotClosed();
        while (this.sendBuffer.iI1i丨I() > 0L) {
          emitFrame(false);
          Http2Stream.this.connection.flush();
        } 
        return;
      } 
    }
    
    public I11L timeout() {
      return Http2Stream.this.writeTimeout;
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      this.sendBuffer.write(param1I1I, param1Long);
      while (this.sendBuffer.iI1i丨I() >= 16384L)
        emitFrame(false); 
    }
  }
  
  public final class FramingSource implements 丨lL {
    public static final boolean $assertionsDisabled = false;
    
    public boolean closed;
    
    public boolean finished;
    
    private final long maxByteCount;
    
    private final I1I readBuffer = new I1I();
    
    private final I1I receiveBuffer = new I1I();
    
    public final Http2Stream this$0;
    
    private Headers trailers;
    
    public FramingSource(long param1Long) {
      this.maxByteCount = param1Long;
    }
    
    private void updateConnectionFlowControl(long param1Long) {
      Http2Stream.this.connection.updateConnectionFlowControl(param1Long);
    }
    
    public void close() throws IOException {
      synchronized (Http2Stream.this) {
        this.closed = true;
        long l = this.readBuffer.iI1i丨I();
        this.readBuffer.丨丨LLlI1();
        Http2Stream.this.notifyAll();
        if (l > 0L)
          updateConnectionFlowControl(l); 
        Http2Stream.this.cancelStreamIfNecessary();
        return;
      } 
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      if (param1Long >= 0L)
        while (true) {
          IOException iOException = null;
          synchronized (Http2Stream.this) {
            Http2Stream.this.readTimeout.enter();
            try {
              Http2Stream http2Stream = Http2Stream.this;
              if (http2Stream.errorCode != null) {
                iOException = http2Stream.errorException;
                if (iOException == null)
                  iOException = new StreamResetException(Http2Stream.this.errorCode); 
              } 
              if (!this.closed) {
                if (this.readBuffer.iI1i丨I() > 0L) {
                  I1I i1I = this.readBuffer;
                  long l1 = i1I.read(param1I1I, Math.min(param1Long, i1I.iI1i丨I()));
                  Http2Stream http2Stream1 = Http2Stream.this;
                  long l2 = http2Stream1.unacknowledgedBytesRead + l1;
                  http2Stream1.unacknowledgedBytesRead = l2;
                  param1Long = l1;
                  if (iOException == null) {
                    param1Long = l1;
                    if (l2 >= (http2Stream1.connection.okHttpSettings.getInitialWindowSize() / 2)) {
                      http2Stream1 = Http2Stream.this;
                      http2Stream1.connection.writeWindowUpdateLater(http2Stream1.id, http2Stream1.unacknowledgedBytesRead);
                      Http2Stream.this.unacknowledgedBytesRead = 0L;
                      param1Long = l1;
                    } 
                  } 
                } else {
                  if (!this.finished && iOException == null) {
                    Http2Stream.this.waitForIo();
                    Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
                    continue;
                  } 
                  param1Long = -1L;
                } 
                Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
                if (param1Long != -1L)
                  return param1Long; 
                if (iOException == null)
                  return -1L; 
                throw iOException;
              } 
              IOException iOException1 = new IOException();
              this("stream closed");
              throw iOException1;
            } finally {
              Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
            } 
          } 
        }  
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(param1Long);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public void receive(l丨Li1LL param1l丨Li1LL, long param1Long) throws IOException {
      while (param1Long > 0L) {
        synchronized (Http2Stream.this) {
          boolean bool1;
          boolean bool = this.finished;
          long l1 = this.readBuffer.iI1i丨I();
          long l2 = this.maxByteCount;
          boolean bool2 = true;
          if (l1 + param1Long > l2) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool1) {
            param1l丨Li1LL.skip(param1Long);
            Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
            return;
          } 
          if (bool) {
            param1l丨Li1LL.skip(param1Long);
            return;
          } 
          l1 = param1l丨Li1LL.read(this.receiveBuffer, param1Long);
          if (l1 != -1L) {
            l2 = param1Long - l1;
            synchronized (Http2Stream.this) {
              if (this.closed) {
                l1 = this.receiveBuffer.iI1i丨I();
                this.receiveBuffer.丨丨LLlI1();
              } else {
                if (this.readBuffer.iI1i丨I() == 0L) {
                  bool1 = bool2;
                } else {
                  bool1 = false;
                } 
                this.readBuffer.lIi丨I(this.receiveBuffer);
                if (bool1)
                  Http2Stream.this.notifyAll(); 
                l1 = 0L;
              } 
              param1Long = l2;
              if (l1 > 0L) {
                updateConnectionFlowControl(l1);
                param1Long = l2;
              } 
            } 
            continue;
          } 
          throw new EOFException();
        } 
      } 
    }
    
    public I11L timeout() {
      return Http2Stream.this.readTimeout;
    }
  }
  
  public class StreamTimeout extends IL1Iii {
    public final Http2Stream this$0;
    
    public void exitAndThrowIfTimedOut() throws IOException {
      if (!exit())
        return; 
      throw newTimeoutException(null);
    }
    
    public IOException newTimeoutException(IOException param1IOException) {
      SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
      if (param1IOException != null)
        socketTimeoutException.initCause(param1IOException); 
      return socketTimeoutException;
    }
    
    public void timedOut() {
      Http2Stream.this.closeLater(ErrorCode.CANCEL);
      Http2Stream.this.connection.sendDegradedPingLater();
    }
  }
}
