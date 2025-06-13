package okhttp3.internal.cache2;

import I丨L.I11L;
import I丨L.I1I;
import I丨L.iI丨LLL1;
import I丨L.丨lL;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;

public final class Relay {
  private static final long FILE_HEADER_SIZE = 32L;
  
  public static final iI丨LLL1 PREFIX_CLEAN = iI丨LLL1.encodeUtf8("OkHttp cache v1\n");
  
  public static final iI丨LLL1 PREFIX_DIRTY = iI丨LLL1.encodeUtf8("OkHttp DIRTY :(\n");
  
  private static final int SOURCE_FILE = 2;
  
  private static final int SOURCE_UPSTREAM = 1;
  
  public final I1I buffer;
  
  public final long bufferMaxSize;
  
  public boolean complete;
  
  public RandomAccessFile file;
  
  private final iI丨LLL1 metadata;
  
  public int sourceCount;
  
  public 丨lL upstream;
  
  public final I1I upstreamBuffer;
  
  public long upstreamPos;
  
  public Thread upstreamReader;
  
  private Relay(RandomAccessFile paramRandomAccessFile, 丨lL param丨lL, long paramLong1, iI丨LLL1 paramiI丨LLL1, long paramLong2) {
    boolean bool;
    this.upstreamBuffer = new I1I();
    this.buffer = new I1I();
    this.file = paramRandomAccessFile;
    this.upstream = param丨lL;
    if (param丨lL == null) {
      bool = true;
    } else {
      bool = false;
    } 
    this.complete = bool;
    this.upstreamPos = paramLong1;
    this.metadata = paramiI丨LLL1;
    this.bufferMaxSize = paramLong2;
  }
  
  public static Relay edit(File paramFile, 丨lL param丨lL, iI丨LLL1 paramiI丨LLL1, long paramLong) throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "rw");
    Relay relay = new Relay(randomAccessFile, param丨lL, 0L, paramiI丨LLL1, paramLong);
    randomAccessFile.setLength(0L);
    relay.writeHeader(PREFIX_DIRTY, -1L, -1L);
    return relay;
  }
  
  public static Relay read(File paramFile) throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "rw");
    FileOperator fileOperator = new FileOperator(randomAccessFile.getChannel());
    I1I i1I = new I1I();
    fileOperator.read(0L, i1I, 32L);
    iI丨LLL1 iI丨LLL11 = PREFIX_CLEAN;
    if (i1I.ILil(iI丨LLL11.size()).equals(iI丨LLL11)) {
      long l1 = i1I.readLong();
      long l2 = i1I.readLong();
      I1I i1I1 = new I1I();
      fileOperator.read(l1 + 32L, i1I1, l2);
      return new Relay(randomAccessFile, null, l1, i1I1.L11丨(), 0L);
    } 
    throw new IOException("unreadable cache file");
  }
  
  private void writeHeader(iI丨LLL1 paramiI丨LLL1, long paramLong1, long paramLong2) throws IOException {
    I1I i1I = new I1I();
    i1I.iIilII1(paramiI丨LLL1);
    i1I.iI丨Li丨lI(paramLong1);
    i1I.iI丨Li丨lI(paramLong2);
    if (i1I.iI1i丨I() == 32L) {
      (new FileOperator(this.file.getChannel())).write(0L, i1I, 32L);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  private void writeMetadata(long paramLong) throws IOException {
    I1I i1I = new I1I();
    i1I.iIilII1(this.metadata);
    (new FileOperator(this.file.getChannel())).write(32L + paramLong, i1I, this.metadata.size());
  }
  
  public void commit(long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: lload_1
    //   2: invokespecial writeMetadata : (J)V
    //   5: aload_0
    //   6: getfield file : Ljava/io/RandomAccessFile;
    //   9: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   12: iconst_0
    //   13: invokevirtual force : (Z)V
    //   16: aload_0
    //   17: getstatic okhttp3/internal/cache2/Relay.PREFIX_CLEAN : LI丨L/iI丨LLL1;
    //   20: lload_1
    //   21: aload_0
    //   22: getfield metadata : LI丨L/iI丨LLL1;
    //   25: invokevirtual size : ()I
    //   28: i2l
    //   29: invokespecial writeHeader : (LI丨L/iI丨LLL1;JJ)V
    //   32: aload_0
    //   33: getfield file : Ljava/io/RandomAccessFile;
    //   36: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   39: iconst_0
    //   40: invokevirtual force : (Z)V
    //   43: aload_0
    //   44: monitorenter
    //   45: aload_0
    //   46: iconst_1
    //   47: putfield complete : Z
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_0
    //   53: getfield upstream : LI丨L/丨lL;
    //   56: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield upstream : LI丨L/丨lL;
    //   64: return
    //   65: astore_3
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_3
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   45	52	65	finally
    //   66	68	65	finally
  }
  
  public boolean isClosed() {
    boolean bool;
    if (this.file == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public iI丨LLL1 metadata() {
    return this.metadata;
  }
  
  public 丨lL newSource() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield file : Ljava/io/RandomAccessFile;
    //   6: ifnonnull -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: aconst_null
    //   12: areturn
    //   13: aload_0
    //   14: aload_0
    //   15: getfield sourceCount : I
    //   18: iconst_1
    //   19: iadd
    //   20: putfield sourceCount : I
    //   23: aload_0
    //   24: monitorexit
    //   25: new okhttp3/internal/cache2/Relay$RelaySource
    //   28: dup
    //   29: aload_0
    //   30: invokespecial <init> : (Lokhttp3/internal/cache2/Relay;)V
    //   33: areturn
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	34	finally
    //   13	25	34	finally
    //   35	37	34	finally
  }
  
  public class RelaySource implements 丨lL {
    private FileOperator fileOperator = new FileOperator(Relay.this.file.getChannel());
    
    private long sourcePos;
    
    public final Relay this$0;
    
    private final I11L timeout = new I11L();
    
    public void close() throws IOException {
      if (this.fileOperator == null)
        return; 
      null = null;
      this.fileOperator = null;
      synchronized (Relay.this) {
        Relay relay = Relay.this;
        int i = relay.sourceCount - 1;
        relay.sourceCount = i;
        if (i == 0) {
          null = relay.file;
          relay.file = null;
        } 
        if (null != null)
          Util.closeQuietly(null); 
        return;
      } 
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: getfield fileOperator : Lokhttp3/internal/cache2/FileOperator;
      //   4: ifnull -> 541
      //   7: aload_0
      //   8: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   11: astore #11
      //   13: aload #11
      //   15: monitorenter
      //   16: aload_0
      //   17: getfield sourcePos : J
      //   20: lstore #7
      //   22: aload_0
      //   23: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   26: astore #12
      //   28: aload #12
      //   30: getfield upstreamPos : J
      //   33: lstore #5
      //   35: lload #7
      //   37: lload #5
      //   39: lcmp
      //   40: ifne -> 95
      //   43: aload #12
      //   45: getfield complete : Z
      //   48: ifeq -> 58
      //   51: aload #11
      //   53: monitorexit
      //   54: ldc2_w -1
      //   57: lreturn
      //   58: aload #12
      //   60: getfield upstreamReader : Ljava/lang/Thread;
      //   63: ifnull -> 78
      //   66: aload_0
      //   67: getfield timeout : LI丨L/I11L;
      //   70: aload #12
      //   72: invokevirtual waitUntilNotified : (Ljava/lang/Object;)V
      //   75: goto -> 16
      //   78: aload #12
      //   80: invokestatic currentThread : ()Ljava/lang/Thread;
      //   83: putfield upstreamReader : Ljava/lang/Thread;
      //   86: iconst_1
      //   87: istore #4
      //   89: aload #11
      //   91: monitorexit
      //   92: goto -> 128
      //   95: lload #5
      //   97: aload #12
      //   99: getfield buffer : LI丨L/I1I;
      //   102: invokevirtual iI1i丨I : ()J
      //   105: lsub
      //   106: lstore #7
      //   108: aload_0
      //   109: getfield sourcePos : J
      //   112: lstore #9
      //   114: lload #9
      //   116: lload #7
      //   118: lcmp
      //   119: ifge -> 490
      //   122: aload #11
      //   124: monitorexit
      //   125: iconst_2
      //   126: istore #4
      //   128: iload #4
      //   130: iconst_2
      //   131: if_icmpne -> 175
      //   134: lload_2
      //   135: lload #5
      //   137: aload_0
      //   138: getfield sourcePos : J
      //   141: lsub
      //   142: invokestatic min : (JJ)J
      //   145: lstore_2
      //   146: aload_0
      //   147: getfield fileOperator : Lokhttp3/internal/cache2/FileOperator;
      //   150: aload_0
      //   151: getfield sourcePos : J
      //   154: ldc2_w 32
      //   157: ladd
      //   158: aload_1
      //   159: lload_2
      //   160: invokevirtual read : (JLI丨L/I1I;J)V
      //   163: aload_0
      //   164: aload_0
      //   165: getfield sourcePos : J
      //   168: lload_2
      //   169: ladd
      //   170: putfield sourcePos : J
      //   173: lload_2
      //   174: lreturn
      //   175: aload_0
      //   176: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   179: astore #11
      //   181: aload #11
      //   183: getfield upstream : LI丨L/丨lL;
      //   186: aload #11
      //   188: getfield upstreamBuffer : LI丨L/I1I;
      //   191: aload #11
      //   193: getfield bufferMaxSize : J
      //   196: invokeinterface read : (LI丨L/I1I;J)J
      //   201: lstore #7
      //   203: lload #7
      //   205: ldc2_w -1
      //   208: lcmp
      //   209: ifne -> 258
      //   212: aload_0
      //   213: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   216: lload #5
      //   218: invokevirtual commit : (J)V
      //   221: aload_0
      //   222: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   225: astore_1
      //   226: aload_1
      //   227: monitorenter
      //   228: aload_0
      //   229: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   232: astore #11
      //   234: aload #11
      //   236: aconst_null
      //   237: putfield upstreamReader : Ljava/lang/Thread;
      //   240: aload #11
      //   242: invokevirtual notifyAll : ()V
      //   245: aload_1
      //   246: monitorexit
      //   247: ldc2_w -1
      //   250: lreturn
      //   251: astore #11
      //   253: aload_1
      //   254: monitorexit
      //   255: aload #11
      //   257: athrow
      //   258: lload #7
      //   260: lload_2
      //   261: invokestatic min : (JJ)J
      //   264: lstore_2
      //   265: aload_0
      //   266: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   269: getfield upstreamBuffer : LI丨L/I1I;
      //   272: aload_1
      //   273: lconst_0
      //   274: lload_2
      //   275: invokevirtual l1IIi1丨 : (LI丨L/I1I;JJ)LI丨L/I1I;
      //   278: pop
      //   279: aload_0
      //   280: aload_0
      //   281: getfield sourcePos : J
      //   284: lload_2
      //   285: ladd
      //   286: putfield sourcePos : J
      //   289: aload_0
      //   290: getfield fileOperator : Lokhttp3/internal/cache2/FileOperator;
      //   293: lload #5
      //   295: ldc2_w 32
      //   298: ladd
      //   299: aload_0
      //   300: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   303: getfield upstreamBuffer : LI丨L/I1I;
      //   306: invokevirtual 丨丨 : ()LI丨L/I1I;
      //   309: lload #7
      //   311: invokevirtual write : (JLI丨L/I1I;J)V
      //   314: aload_0
      //   315: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   318: astore_1
      //   319: aload_1
      //   320: monitorenter
      //   321: aload_0
      //   322: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   325: astore #11
      //   327: aload #11
      //   329: getfield buffer : LI丨L/I1I;
      //   332: aload #11
      //   334: getfield upstreamBuffer : LI丨L/I1I;
      //   337: lload #7
      //   339: invokevirtual write : (LI丨L/I1I;J)V
      //   342: aload_0
      //   343: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   346: getfield buffer : LI丨L/I1I;
      //   349: invokevirtual iI1i丨I : ()J
      //   352: lstore #5
      //   354: aload_0
      //   355: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   358: astore #11
      //   360: lload #5
      //   362: aload #11
      //   364: getfield bufferMaxSize : J
      //   367: lcmp
      //   368: ifle -> 396
      //   371: aload #11
      //   373: getfield buffer : LI丨L/I1I;
      //   376: astore #11
      //   378: aload #11
      //   380: aload #11
      //   382: invokevirtual iI1i丨I : ()J
      //   385: aload_0
      //   386: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   389: getfield bufferMaxSize : J
      //   392: lsub
      //   393: invokevirtual skip : (J)V
      //   396: aload_0
      //   397: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   400: astore #11
      //   402: aload #11
      //   404: aload #11
      //   406: getfield upstreamPos : J
      //   409: lload #7
      //   411: ladd
      //   412: putfield upstreamPos : J
      //   415: aload_1
      //   416: monitorexit
      //   417: aload #11
      //   419: monitorenter
      //   420: aload_0
      //   421: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   424: astore_1
      //   425: aload_1
      //   426: aconst_null
      //   427: putfield upstreamReader : Ljava/lang/Thread;
      //   430: aload_1
      //   431: invokevirtual notifyAll : ()V
      //   434: aload #11
      //   436: monitorexit
      //   437: lload_2
      //   438: lreturn
      //   439: astore_1
      //   440: aload #11
      //   442: monitorexit
      //   443: aload_1
      //   444: athrow
      //   445: astore #11
      //   447: aload_1
      //   448: monitorexit
      //   449: aload #11
      //   451: athrow
      //   452: astore #11
      //   454: aload_0
      //   455: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   458: astore_1
      //   459: aload_1
      //   460: monitorenter
      //   461: aload_0
      //   462: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   465: astore #12
      //   467: aload #12
      //   469: aconst_null
      //   470: putfield upstreamReader : Ljava/lang/Thread;
      //   473: aload #12
      //   475: invokevirtual notifyAll : ()V
      //   478: aload_1
      //   479: monitorexit
      //   480: aload #11
      //   482: athrow
      //   483: astore #11
      //   485: aload_1
      //   486: monitorexit
      //   487: aload #11
      //   489: athrow
      //   490: lload_2
      //   491: lload #5
      //   493: lload #9
      //   495: lsub
      //   496: invokestatic min : (JJ)J
      //   499: lstore_2
      //   500: aload_0
      //   501: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   504: getfield buffer : LI丨L/I1I;
      //   507: aload_1
      //   508: aload_0
      //   509: getfield sourcePos : J
      //   512: lload #7
      //   514: lsub
      //   515: lload_2
      //   516: invokevirtual l1IIi1丨 : (LI丨L/I1I;JJ)LI丨L/I1I;
      //   519: pop
      //   520: aload_0
      //   521: aload_0
      //   522: getfield sourcePos : J
      //   525: lload_2
      //   526: ladd
      //   527: putfield sourcePos : J
      //   530: aload #11
      //   532: monitorexit
      //   533: lload_2
      //   534: lreturn
      //   535: astore_1
      //   536: aload #11
      //   538: monitorexit
      //   539: aload_1
      //   540: athrow
      //   541: new java/lang/IllegalStateException
      //   544: dup
      //   545: ldc 'closed'
      //   547: invokespecial <init> : (Ljava/lang/String;)V
      //   550: athrow
      // Exception table:
      //   from	to	target	type
      //   16	35	535	finally
      //   43	54	535	finally
      //   58	75	535	finally
      //   78	86	535	finally
      //   89	92	535	finally
      //   95	114	535	finally
      //   122	125	535	finally
      //   175	203	452	finally
      //   212	221	452	finally
      //   228	247	251	finally
      //   253	255	251	finally
      //   258	321	452	finally
      //   321	396	445	finally
      //   396	417	445	finally
      //   420	437	439	finally
      //   440	443	439	finally
      //   447	449	445	finally
      //   449	452	452	finally
      //   461	480	483	finally
      //   485	487	483	finally
      //   490	533	535	finally
      //   536	539	535	finally
    }
    
    public I11L timeout() {
      return this.timeout;
    }
  }
}
