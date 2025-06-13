package I丨L;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class IL1Iii extends I11L {
  private static final long IDLE_TIMEOUT_MILLIS;
  
  private static final long IDLE_TIMEOUT_NANOS;
  
  private static final int TIMEOUT_WRITE_SIZE = 65536;
  
  @Nullable
  public static IL1Iii head;
  
  private boolean inQueue;
  
  @Nullable
  private IL1Iii next;
  
  private long timeoutAt;
  
  static {
    long l = TimeUnit.SECONDS.toMillis(60L);
    IDLE_TIMEOUT_MILLIS = l;
    IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(l);
  }
  
  @Nullable
  public static IL1Iii awaitTimeout() throws InterruptedException {
    IL1Iii iL1Iii1 = head.next;
    IL1Iii iL1Iii2 = null;
    if (iL1Iii1 == null) {
      long l1 = System.nanoTime();
      IL1Iii.class.wait(IDLE_TIMEOUT_MILLIS);
      iL1Iii1 = iL1Iii2;
      if (head.next == null) {
        iL1Iii1 = iL1Iii2;
        if (System.nanoTime() - l1 >= IDLE_TIMEOUT_NANOS)
          iL1Iii1 = head; 
      } 
      return iL1Iii1;
    } 
    long l = iL1Iii1.remainingNanos(System.nanoTime());
    if (l > 0L) {
      long l1 = l / 1000000L;
      IL1Iii.class.wait(l1, (int)(l - 1000000L * l1));
      return null;
    } 
    head.next = iL1Iii1.next;
    iL1Iii1.next = null;
    return iL1Iii1;
  }
  
  private static boolean cancelScheduledTimeout(IL1Iii paramIL1Iii) {
    // Byte code:
    //   0: ldc I丨L/IL1Iii
    //   2: monitorenter
    //   3: getstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 44
    //   11: aload_1
    //   12: getfield next : LI丨L/IL1Iii;
    //   15: astore_2
    //   16: aload_2
    //   17: aload_0
    //   18: if_acmpne -> 39
    //   21: aload_1
    //   22: aload_0
    //   23: getfield next : LI丨L/IL1Iii;
    //   26: putfield next : LI丨L/IL1Iii;
    //   29: aload_0
    //   30: aconst_null
    //   31: putfield next : LI丨L/IL1Iii;
    //   34: ldc I丨L/IL1Iii
    //   36: monitorexit
    //   37: iconst_0
    //   38: ireturn
    //   39: aload_2
    //   40: astore_1
    //   41: goto -> 7
    //   44: ldc I丨L/IL1Iii
    //   46: monitorexit
    //   47: iconst_1
    //   48: ireturn
    //   49: astore_0
    //   50: ldc I丨L/IL1Iii
    //   52: monitorexit
    //   53: aload_0
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	49	finally
    //   11	16	49	finally
    //   21	34	49	finally
  }
  
  private long remainingNanos(long paramLong) {
    return this.timeoutAt - paramLong;
  }
  
  private static void scheduleTimeout(IL1Iii paramIL1Iii, long paramLong, boolean paramBoolean) {
    // Byte code:
    //   0: ldc I丨L/IL1Iii
    //   2: monitorenter
    //   3: getstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
    //   6: ifnonnull -> 39
    //   9: new I丨L/IL1Iii
    //   12: astore #7
    //   14: aload #7
    //   16: invokespecial <init> : ()V
    //   19: aload #7
    //   21: putstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
    //   24: new I丨L/IL1Iii$I1I
    //   27: astore #7
    //   29: aload #7
    //   31: invokespecial <init> : ()V
    //   34: aload #7
    //   36: invokevirtual start : ()V
    //   39: invokestatic nanoTime : ()J
    //   42: lstore #5
    //   44: lload_1
    //   45: lconst_0
    //   46: lcmp
    //   47: istore #4
    //   49: iload #4
    //   51: ifeq -> 79
    //   54: iload_3
    //   55: ifeq -> 79
    //   58: aload_0
    //   59: lload_1
    //   60: aload_0
    //   61: invokevirtual deadlineNanoTime : ()J
    //   64: lload #5
    //   66: lsub
    //   67: invokestatic min : (JJ)J
    //   70: lload #5
    //   72: ladd
    //   73: putfield timeoutAt : J
    //   76: goto -> 107
    //   79: iload #4
    //   81: ifeq -> 95
    //   84: aload_0
    //   85: lload_1
    //   86: lload #5
    //   88: ladd
    //   89: putfield timeoutAt : J
    //   92: goto -> 107
    //   95: iload_3
    //   96: ifeq -> 188
    //   99: aload_0
    //   100: aload_0
    //   101: invokevirtual deadlineNanoTime : ()J
    //   104: putfield timeoutAt : J
    //   107: aload_0
    //   108: lload #5
    //   110: invokespecial remainingNanos : (J)J
    //   113: lstore_1
    //   114: getstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
    //   117: astore #7
    //   119: aload #7
    //   121: getfield next : LI丨L/IL1Iii;
    //   124: astore #8
    //   126: aload #8
    //   128: ifnull -> 156
    //   131: lload_1
    //   132: aload #8
    //   134: lload #5
    //   136: invokespecial remainingNanos : (J)J
    //   139: lcmp
    //   140: ifge -> 146
    //   143: goto -> 156
    //   146: aload #7
    //   148: getfield next : LI丨L/IL1Iii;
    //   151: astore #7
    //   153: goto -> 119
    //   156: aload_0
    //   157: aload #7
    //   159: getfield next : LI丨L/IL1Iii;
    //   162: putfield next : LI丨L/IL1Iii;
    //   165: aload #7
    //   167: aload_0
    //   168: putfield next : LI丨L/IL1Iii;
    //   171: aload #7
    //   173: getstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
    //   176: if_acmpne -> 184
    //   179: ldc I丨L/IL1Iii
    //   181: invokevirtual notify : ()V
    //   184: ldc I丨L/IL1Iii
    //   186: monitorexit
    //   187: return
    //   188: new java/lang/AssertionError
    //   191: astore_0
    //   192: aload_0
    //   193: invokespecial <init> : ()V
    //   196: aload_0
    //   197: athrow
    //   198: astore_0
    //   199: ldc I丨L/IL1Iii
    //   201: monitorexit
    //   202: aload_0
    //   203: athrow
    // Exception table:
    //   from	to	target	type
    //   3	39	198	finally
    //   39	44	198	finally
    //   58	76	198	finally
    //   84	92	198	finally
    //   99	107	198	finally
    //   107	119	198	finally
    //   119	126	198	finally
    //   131	143	198	finally
    //   146	153	198	finally
    //   156	184	198	finally
    //   188	198	198	finally
  }
  
  public final void enter() {
    if (!this.inQueue) {
      long l = timeoutNanos();
      boolean bool = hasDeadline();
      if (l == 0L && !bool)
        return; 
      this.inQueue = true;
      scheduleTimeout(this, l, bool);
      return;
    } 
    throw new IllegalStateException("Unbalanced enter/exit");
  }
  
  public final IOException exit(IOException paramIOException) throws IOException {
    return !exit() ? paramIOException : newTimeoutException(paramIOException);
  }
  
  public final void exit(boolean paramBoolean) throws IOException {
    if (!exit() || !paramBoolean)
      return; 
    throw newTimeoutException(null);
  }
  
  public final boolean exit() {
    if (!this.inQueue)
      return false; 
    this.inQueue = false;
    return cancelScheduledTimeout(this);
  }
  
  public IOException newTimeoutException(@Nullable IOException paramIOException) {
    InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
    if (paramIOException != null)
      interruptedIOException.initCause(paramIOException); 
    return interruptedIOException;
  }
  
  public final I11li1 sink(I11li1 paramI11li1) {
    return new IL1Iii(this, paramI11li1);
  }
  
  public final 丨lL source(丨lL param丨lL) {
    return new ILil(this, param丨lL);
  }
  
  public void timedOut() {}
  
  public static final class I1I extends Thread {
    public I1I() {
      super("Okio Watchdog");
      setDaemon(true);
    }
    
    public void run() {
      // Byte code:
      //   0: ldc I丨L/IL1Iii
      //   2: monitorenter
      //   3: invokestatic awaitTimeout : ()LI丨L/IL1Iii;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 17
      //   11: ldc I丨L/IL1Iii
      //   13: monitorexit
      //   14: goto -> 0
      //   17: aload_1
      //   18: getstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
      //   21: if_acmpne -> 32
      //   24: aconst_null
      //   25: putstatic I丨L/IL1Iii.head : LI丨L/IL1Iii;
      //   28: ldc I丨L/IL1Iii
      //   30: monitorexit
      //   31: return
      //   32: ldc I丨L/IL1Iii
      //   34: monitorexit
      //   35: aload_1
      //   36: invokevirtual timedOut : ()V
      //   39: goto -> 0
      //   42: astore_1
      //   43: ldc I丨L/IL1Iii
      //   45: monitorexit
      //   46: aload_1
      //   47: athrow
      //   48: astore_1
      //   49: goto -> 0
      // Exception table:
      //   from	to	target	type
      //   0	3	48	java/lang/InterruptedException
      //   3	7	42	finally
      //   11	14	42	finally
      //   17	31	42	finally
      //   32	35	42	finally
      //   35	39	48	java/lang/InterruptedException
      //   43	46	42	finally
      //   46	48	48	java/lang/InterruptedException
    }
  }
  
  public class IL1Iii implements I11li1 {
    public final I11li1 IL1Iii;
    
    public final IL1Iii ILil;
    
    public IL1Iii(IL1Iii this$0, I11li1 param1I11li1) {}
    
    public void close() throws IOException {
      Exception exception;
      this.ILil.enter();
      try {
        this.IL1Iii.close();
        this.ILil.exit(true);
        return;
      } catch (IOException null) {
        throw this.ILil.exit(exception);
      } finally {}
      this.ILil.exit(false);
      throw exception;
    }
    
    public void flush() throws IOException {
      Exception exception;
      this.ILil.enter();
      try {
        this.IL1Iii.flush();
        this.ILil.exit(true);
        return;
      } catch (IOException null) {
        throw this.ILil.exit(exception);
      } finally {}
      this.ILil.exit(false);
      throw exception;
    }
    
    public I11L timeout() {
      return this.ILil;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("AsyncTimeout.sink(");
      stringBuilder.append(this.IL1Iii);
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      llliI.ILil(param1I1I.ILil, 0L, param1Long);
      while (true) {
        long l = 0L;
        if (param1Long > 0L) {
          long l1;
          lI丨lii lI丨lii = param1I1I.IL1Iii;
          while (true) {
            l1 = l;
            if (l < 65536L) {
              l += (lI丨lii.I1I - lI丨lii.ILil);
              if (l >= param1Long) {
                l1 = param1Long;
                break;
              } 
              lI丨lii = lI丨lii.l丨Li1LL;
              continue;
            } 
            break;
          } 
          this.ILil.enter();
          try {
            this.IL1Iii.write(param1I1I, l1);
            param1Long -= l1;
            this.ILil.exit(true);
            continue;
          } catch (IOException iOException) {
            throw this.ILil.exit(iOException);
          } finally {}
          this.ILil.exit(false);
          throw param1I1I;
        } 
        break;
      } 
    }
  }
  
  public class ILil implements 丨lL {
    public final 丨lL IL1Iii;
    
    public final IL1Iii ILil;
    
    public ILil(IL1Iii this$0, 丨lL param1丨lL) {}
    
    public void close() throws IOException {
      Exception exception;
      this.ILil.enter();
      try {
        this.IL1Iii.close();
        this.ILil.exit(true);
        return;
      } catch (IOException null) {
        throw this.ILil.exit(exception);
      } finally {}
      this.ILil.exit(false);
      throw exception;
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      this.ILil.enter();
      try {
        param1Long = this.IL1Iii.read(param1I1I, param1Long);
        this.ILil.exit(true);
        return param1Long;
      } catch (IOException iOException) {
        throw this.ILil.exit(iOException);
      } finally {}
      this.ILil.exit(false);
      throw param1I1I;
    }
    
    public I11L timeout() {
      return this.ILil;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("AsyncTimeout.source(");
      stringBuilder.append(this.IL1Iii);
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
  }
}
