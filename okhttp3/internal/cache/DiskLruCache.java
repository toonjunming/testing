package okhttp3.internal.cache;

import I丨L.I11li1;
import I丨L.I丨L;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.io.FileSystem;

public final class DiskLruCache implements Closeable, Flushable {
  public static final boolean $assertionsDisabled = false;
  
  public static final long ANY_SEQUENCE_NUMBER = -1L;
  
  private static final String CLEAN = "CLEAN";
  
  private static final String DIRTY = "DIRTY";
  
  public static final String JOURNAL_FILE = "journal";
  
  public static final String JOURNAL_FILE_BACKUP = "journal.bkp";
  
  public static final String JOURNAL_FILE_TEMP = "journal.tmp";
  
  public static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
  
  public static final String MAGIC = "libcore.io.DiskLruCache";
  
  private static final String READ = "READ";
  
  private static final String REMOVE = "REMOVE";
  
  public static final String VERSION_1 = "1";
  
  private final int appVersion;
  
  private final Runnable cleanupRunnable = new Runnable() {
      public final DiskLruCache this$0;
      
      public void run() {
        synchronized (DiskLruCache.this) {
          boolean bool;
          DiskLruCache diskLruCache = DiskLruCache.this;
          if (!diskLruCache.initialized) {
            bool = true;
          } else {
            bool = false;
          } 
          if (bool | diskLruCache.closed)
            return; 
          try {
            diskLruCache.trimToSize();
          } catch (IOException iOException) {
            DiskLruCache.this.mostRecentTrimFailed = true;
          } 
          try {
            if (DiskLruCache.this.journalRebuildRequired()) {
              DiskLruCache.this.rebuildJournal();
              DiskLruCache.this.redundantOpCount = 0;
            } 
          } catch (IOException iOException) {
            DiskLruCache diskLruCache1 = DiskLruCache.this;
            diskLruCache1.mostRecentRebuildFailed = true;
            diskLruCache1.journalWriter = lIi丨I.I1I(lIi丨I.ILil());
          } 
          return;
        } 
      }
    };
  
  public boolean closed;
  
  public final File directory;
  
  private final Executor executor;
  
  public final FileSystem fileSystem;
  
  public boolean hasJournalErrors;
  
  public boolean initialized;
  
  private final File journalFile;
  
  private final File journalFileBackup;
  
  private final File journalFileTmp;
  
  public I丨L journalWriter;
  
  public final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<String, Entry>(0, 0.75F, true);
  
  private long maxSize;
  
  public boolean mostRecentRebuildFailed;
  
  public boolean mostRecentTrimFailed;
  
  private long nextSequenceNumber = 0L;
  
  public int redundantOpCount;
  
  private long size = 0L;
  
  public final int valueCount;
  
  public DiskLruCache(FileSystem paramFileSystem, File paramFile, int paramInt1, int paramInt2, long paramLong, Executor paramExecutor) {
    this.fileSystem = paramFileSystem;
    this.directory = paramFile;
    this.appVersion = paramInt1;
    this.journalFile = new File(paramFile, "journal");
    this.journalFileTmp = new File(paramFile, "journal.tmp");
    this.journalFileBackup = new File(paramFile, "journal.bkp");
    this.valueCount = paramInt2;
    this.maxSize = paramLong;
    this.executor = paramExecutor;
  }
  
  private void checkNotClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isClosed : ()Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: new java/lang/IllegalStateException
    //   17: astore_2
    //   18: aload_2
    //   19: ldc 'cache is closed'
    //   21: invokespecial <init> : (Ljava/lang/String;)V
    //   24: aload_2
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
  
  public static DiskLruCache create(FileSystem paramFileSystem, File paramFile, int paramInt1, int paramInt2, long paramLong) {
    if (paramLong > 0L) {
      if (paramInt2 > 0)
        return new DiskLruCache(paramFileSystem, paramFile, paramInt1, paramInt2, paramLong, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory("OkHttp DiskLruCache", true))); 
      throw new IllegalArgumentException("valueCount <= 0");
    } 
    throw new IllegalArgumentException("maxSize <= 0");
  }
  
  private I丨L newJournalWriter() throws FileNotFoundException {
    return lIi丨I.I1I(new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile)) {
          public static final boolean $assertionsDisabled = false;
          
          public final DiskLruCache this$0;
          
          public void onException(IOException param1IOException) {
            DiskLruCache.this.hasJournalErrors = true;
          }
        });
  }
  
  private void processJournal() throws IOException {
    this.fileSystem.delete(this.journalFileTmp);
    Iterator<Entry> iterator = this.lruEntries.values().iterator();
    while (iterator.hasNext()) {
      Entry entry = iterator.next();
      Editor editor = entry.currentEditor;
      byte b1 = 0;
      byte b2 = 0;
      if (editor == null) {
        for (b1 = b2; b1 < this.valueCount; b1++)
          this.size += entry.lengths[b1]; 
        continue;
      } 
      entry.currentEditor = null;
      while (b1 < this.valueCount) {
        this.fileSystem.delete(entry.cleanFiles[b1]);
        this.fileSystem.delete(entry.dirtyFiles[b1]);
        b1++;
      } 
      iterator.remove();
    } 
  }
  
  private void readJournal() throws IOException {
    l丨Li1LL l丨Li1LL = lIi丨I.I丨L(this.fileSystem.source(this.journalFile));
    try {
      String str4 = l丨Li1LL.LlLI1();
      String str2 = l丨Li1LL.LlLI1();
      String str5 = l丨Li1LL.LlLI1();
      String str1 = l丨Li1LL.LlLI1();
      String str3 = l丨Li1LL.LlLI1();
      if ("libcore.io.DiskLruCache".equals(str4) && "1".equals(str2) && Integer.toString(this.appVersion).equals(str5) && Integer.toString(this.valueCount).equals(str1)) {
        boolean bool = "".equals(str3);
        if (bool) {
          byte b = 0;
          try {
            while (true) {
              readJournalLine(l丨Li1LL.LlLI1());
              b++;
            } 
          } catch (EOFException eOFException) {
            this.redundantOpCount = b - this.lruEntries.size();
            if (!l丨Li1LL.l丨Li1LL()) {
              rebuildJournal();
            } else {
              this.journalWriter = newJournalWriter();
            } 
            return;
          } 
        } 
      } 
      IOException iOException = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("unexpected journal header: [");
      stringBuilder.append(str4);
      stringBuilder.append(", ");
      stringBuilder.append(str2);
      stringBuilder.append(", ");
      stringBuilder.append((String)eOFException);
      stringBuilder.append(", ");
      stringBuilder.append(str3);
      stringBuilder.append("]");
      this(stringBuilder.toString());
      throw iOException;
    } finally {
      Exception exception = null;
    } 
  }
  
  private void readJournalLine(String paramString) throws IOException {
    String[] arrayOfString;
    int i = paramString.indexOf(' ');
    if (i != -1) {
      String str;
      int j = i + 1;
      int k = paramString.indexOf(' ', j);
      if (k == -1) {
        String str1 = paramString.substring(j);
        str = str1;
        if (i == 6) {
          str = str1;
          if (paramString.startsWith("REMOVE")) {
            this.lruEntries.remove(str1);
            return;
          } 
        } 
      } else {
        str = paramString.substring(j, k);
      } 
      Entry entry2 = this.lruEntries.get(str);
      Entry entry1 = entry2;
      if (entry2 == null) {
        entry1 = new Entry(str);
        this.lruEntries.put(str, entry1);
      } 
      if (k != -1 && i == 5 && paramString.startsWith("CLEAN")) {
        arrayOfString = paramString.substring(k + 1).split(" ");
        entry1.readable = true;
        entry1.currentEditor = null;
        entry1.setLengths(arrayOfString);
      } else if (k == -1 && i == 5 && arrayOfString.startsWith("DIRTY")) {
        entry1.currentEditor = new Editor(entry1);
      } else if (k != -1 || i != 4 || !arrayOfString.startsWith("READ")) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("unexpected journal line: ");
        stringBuilder1.append((String)arrayOfString);
        throw new IOException(stringBuilder1.toString());
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("unexpected journal line: ");
    stringBuilder.append((String)arrayOfString);
    throw new IOException(stringBuilder.toString());
  }
  
  private void validateKey(String paramString) {
    if (LEGAL_KEY_PATTERN.matcher(paramString).matches())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("keys must match regex [a-z0-9_-]{1,120}: \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\"");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield initialized : Z
    //   6: ifeq -> 105
    //   9: aload_0
    //   10: getfield closed : Z
    //   13: ifeq -> 19
    //   16: goto -> 105
    //   19: aload_0
    //   20: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   23: invokevirtual values : ()Ljava/util/Collection;
    //   26: aload_0
    //   27: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   30: invokevirtual size : ()I
    //   33: anewarray okhttp3/internal/cache/DiskLruCache$Entry
    //   36: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   41: checkcast [Lokhttp3/internal/cache/DiskLruCache$Entry;
    //   44: astore_3
    //   45: aload_3
    //   46: arraylength
    //   47: istore_2
    //   48: iconst_0
    //   49: istore_1
    //   50: iload_1
    //   51: iload_2
    //   52: if_icmpge -> 79
    //   55: aload_3
    //   56: iload_1
    //   57: aaload
    //   58: getfield currentEditor : Lokhttp3/internal/cache/DiskLruCache$Editor;
    //   61: astore #4
    //   63: aload #4
    //   65: ifnull -> 73
    //   68: aload #4
    //   70: invokevirtual abort : ()V
    //   73: iinc #1, 1
    //   76: goto -> 50
    //   79: aload_0
    //   80: invokevirtual trimToSize : ()V
    //   83: aload_0
    //   84: getfield journalWriter : LI丨L/I丨L;
    //   87: invokeinterface close : ()V
    //   92: aload_0
    //   93: aconst_null
    //   94: putfield journalWriter : LI丨L/I丨L;
    //   97: aload_0
    //   98: iconst_1
    //   99: putfield closed : Z
    //   102: aload_0
    //   103: monitorexit
    //   104: return
    //   105: aload_0
    //   106: iconst_1
    //   107: putfield closed : Z
    //   110: aload_0
    //   111: monitorexit
    //   112: return
    //   113: astore_3
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_3
    //   117: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	113	finally
    //   19	48	113	finally
    //   55	63	113	finally
    //   68	73	113	finally
    //   79	102	113	finally
    //   105	110	113	finally
  }
  
  public void completeEdit(Editor paramEditor, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: getfield entry : Lokhttp3/internal/cache/DiskLruCache$Entry;
    //   6: astore #10
    //   8: aload #10
    //   10: getfield currentEditor : Lokhttp3/internal/cache/DiskLruCache$Editor;
    //   13: aload_1
    //   14: if_acmpne -> 481
    //   17: iconst_0
    //   18: istore #5
    //   20: iload #5
    //   22: istore #4
    //   24: iload_2
    //   25: ifeq -> 140
    //   28: iload #5
    //   30: istore #4
    //   32: aload #10
    //   34: getfield readable : Z
    //   37: ifne -> 140
    //   40: iconst_0
    //   41: istore_3
    //   42: iload #5
    //   44: istore #4
    //   46: iload_3
    //   47: aload_0
    //   48: getfield valueCount : I
    //   51: if_icmpge -> 140
    //   54: aload_1
    //   55: getfield written : [Z
    //   58: iload_3
    //   59: baload
    //   60: ifeq -> 95
    //   63: aload_0
    //   64: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   67: aload #10
    //   69: getfield dirtyFiles : [Ljava/io/File;
    //   72: iload_3
    //   73: aaload
    //   74: invokeinterface exists : (Ljava/io/File;)Z
    //   79: ifne -> 89
    //   82: aload_1
    //   83: invokevirtual abort : ()V
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: iinc #3, 1
    //   92: goto -> 42
    //   95: aload_1
    //   96: invokevirtual abort : ()V
    //   99: new java/lang/IllegalStateException
    //   102: astore_1
    //   103: new java/lang/StringBuilder
    //   106: astore #10
    //   108: aload #10
    //   110: invokespecial <init> : ()V
    //   113: aload #10
    //   115: ldc_w 'Newly created entry didn't create value for index '
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload #10
    //   124: iload_3
    //   125: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_1
    //   130: aload #10
    //   132: invokevirtual toString : ()Ljava/lang/String;
    //   135: invokespecial <init> : (Ljava/lang/String;)V
    //   138: aload_1
    //   139: athrow
    //   140: iload #4
    //   142: aload_0
    //   143: getfield valueCount : I
    //   146: if_icmpge -> 263
    //   149: aload #10
    //   151: getfield dirtyFiles : [Ljava/io/File;
    //   154: iload #4
    //   156: aaload
    //   157: astore_1
    //   158: iload_2
    //   159: ifeq -> 247
    //   162: aload_0
    //   163: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   166: aload_1
    //   167: invokeinterface exists : (Ljava/io/File;)Z
    //   172: ifeq -> 257
    //   175: aload #10
    //   177: getfield cleanFiles : [Ljava/io/File;
    //   180: iload #4
    //   182: aaload
    //   183: astore #11
    //   185: aload_0
    //   186: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   189: aload_1
    //   190: aload #11
    //   192: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   197: aload #10
    //   199: getfield lengths : [J
    //   202: iload #4
    //   204: laload
    //   205: lstore #8
    //   207: aload_0
    //   208: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   211: aload #11
    //   213: invokeinterface size : (Ljava/io/File;)J
    //   218: lstore #6
    //   220: aload #10
    //   222: getfield lengths : [J
    //   225: iload #4
    //   227: lload #6
    //   229: lastore
    //   230: aload_0
    //   231: aload_0
    //   232: getfield size : J
    //   235: lload #8
    //   237: lsub
    //   238: lload #6
    //   240: ladd
    //   241: putfield size : J
    //   244: goto -> 257
    //   247: aload_0
    //   248: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   251: aload_1
    //   252: invokeinterface delete : (Ljava/io/File;)V
    //   257: iinc #4, 1
    //   260: goto -> 140
    //   263: aload_0
    //   264: aload_0
    //   265: getfield redundantOpCount : I
    //   268: iconst_1
    //   269: iadd
    //   270: putfield redundantOpCount : I
    //   273: aload #10
    //   275: aconst_null
    //   276: putfield currentEditor : Lokhttp3/internal/cache/DiskLruCache$Editor;
    //   279: aload #10
    //   281: getfield readable : Z
    //   284: iload_2
    //   285: ior
    //   286: ifeq -> 378
    //   289: aload #10
    //   291: iconst_1
    //   292: putfield readable : Z
    //   295: aload_0
    //   296: getfield journalWriter : LI丨L/I丨L;
    //   299: ldc 'CLEAN'
    //   301: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   306: bipush #32
    //   308: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   313: pop
    //   314: aload_0
    //   315: getfield journalWriter : LI丨L/I丨L;
    //   318: aload #10
    //   320: getfield key : Ljava/lang/String;
    //   323: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   328: pop
    //   329: aload #10
    //   331: aload_0
    //   332: getfield journalWriter : LI丨L/I丨L;
    //   335: invokevirtual writeLengths : (LI丨L/I丨L;)V
    //   338: aload_0
    //   339: getfield journalWriter : LI丨L/I丨L;
    //   342: bipush #10
    //   344: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   349: pop
    //   350: iload_2
    //   351: ifeq -> 437
    //   354: aload_0
    //   355: getfield nextSequenceNumber : J
    //   358: lstore #6
    //   360: aload_0
    //   361: lconst_1
    //   362: lload #6
    //   364: ladd
    //   365: putfield nextSequenceNumber : J
    //   368: aload #10
    //   370: lload #6
    //   372: putfield sequenceNumber : J
    //   375: goto -> 437
    //   378: aload_0
    //   379: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   382: aload #10
    //   384: getfield key : Ljava/lang/String;
    //   387: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   390: pop
    //   391: aload_0
    //   392: getfield journalWriter : LI丨L/I丨L;
    //   395: ldc 'REMOVE'
    //   397: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   402: bipush #32
    //   404: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   409: pop
    //   410: aload_0
    //   411: getfield journalWriter : LI丨L/I丨L;
    //   414: aload #10
    //   416: getfield key : Ljava/lang/String;
    //   419: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   424: pop
    //   425: aload_0
    //   426: getfield journalWriter : LI丨L/I丨L;
    //   429: bipush #10
    //   431: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   436: pop
    //   437: aload_0
    //   438: getfield journalWriter : LI丨L/I丨L;
    //   441: invokeinterface flush : ()V
    //   446: aload_0
    //   447: getfield size : J
    //   450: aload_0
    //   451: getfield maxSize : J
    //   454: lcmp
    //   455: ifgt -> 465
    //   458: aload_0
    //   459: invokevirtual journalRebuildRequired : ()Z
    //   462: ifeq -> 478
    //   465: aload_0
    //   466: getfield executor : Ljava/util/concurrent/Executor;
    //   469: aload_0
    //   470: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   473: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   478: aload_0
    //   479: monitorexit
    //   480: return
    //   481: new java/lang/IllegalStateException
    //   484: astore_1
    //   485: aload_1
    //   486: invokespecial <init> : ()V
    //   489: aload_1
    //   490: athrow
    //   491: astore_1
    //   492: aload_0
    //   493: monitorexit
    //   494: aload_1
    //   495: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	491	finally
    //   32	40	491	finally
    //   46	86	491	finally
    //   95	140	491	finally
    //   140	158	491	finally
    //   162	244	491	finally
    //   247	257	491	finally
    //   263	350	491	finally
    //   354	375	491	finally
    //   378	437	491	finally
    //   437	465	491	finally
    //   465	478	491	finally
    //   481	491	491	finally
  }
  
  public void delete() throws IOException {
    close();
    this.fileSystem.deleteContents(this.directory);
  }
  
  @Nullable
  public Editor edit(String paramString) throws IOException {
    return edit(paramString, -1L);
  }
  
  public Editor edit(String paramString, long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: invokespecial checkNotClosed : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial validateKey : (Ljava/lang/String;)V
    //   15: aload_0
    //   16: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   19: aload_1
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast okhttp3/internal/cache/DiskLruCache$Entry
    //   26: astore #8
    //   28: lload_2
    //   29: ldc2_w -1
    //   32: lcmp
    //   33: ifeq -> 59
    //   36: aload #8
    //   38: ifnull -> 55
    //   41: aload #8
    //   43: getfield sequenceNumber : J
    //   46: lstore #5
    //   48: lload #5
    //   50: lload_2
    //   51: lcmp
    //   52: ifeq -> 59
    //   55: aload_0
    //   56: monitorexit
    //   57: aconst_null
    //   58: areturn
    //   59: aload #8
    //   61: ifnull -> 80
    //   64: aload #8
    //   66: getfield currentEditor : Lokhttp3/internal/cache/DiskLruCache$Editor;
    //   69: astore #7
    //   71: aload #7
    //   73: ifnull -> 80
    //   76: aload_0
    //   77: monitorexit
    //   78: aconst_null
    //   79: areturn
    //   80: aload_0
    //   81: getfield mostRecentTrimFailed : Z
    //   84: ifne -> 206
    //   87: aload_0
    //   88: getfield mostRecentRebuildFailed : Z
    //   91: ifeq -> 97
    //   94: goto -> 206
    //   97: aload_0
    //   98: getfield journalWriter : LI丨L/I丨L;
    //   101: ldc 'DIRTY'
    //   103: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   108: bipush #32
    //   110: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   115: aload_1
    //   116: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   121: bipush #10
    //   123: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   128: pop
    //   129: aload_0
    //   130: getfield journalWriter : LI丨L/I丨L;
    //   133: invokeinterface flush : ()V
    //   138: aload_0
    //   139: getfield hasJournalErrors : Z
    //   142: istore #4
    //   144: iload #4
    //   146: ifeq -> 153
    //   149: aload_0
    //   150: monitorexit
    //   151: aconst_null
    //   152: areturn
    //   153: aload #8
    //   155: astore #7
    //   157: aload #8
    //   159: ifnonnull -> 185
    //   162: new okhttp3/internal/cache/DiskLruCache$Entry
    //   165: astore #7
    //   167: aload #7
    //   169: aload_0
    //   170: aload_1
    //   171: invokespecial <init> : (Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;)V
    //   174: aload_0
    //   175: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   178: aload_1
    //   179: aload #7
    //   181: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   184: pop
    //   185: new okhttp3/internal/cache/DiskLruCache$Editor
    //   188: astore_1
    //   189: aload_1
    //   190: aload_0
    //   191: aload #7
    //   193: invokespecial <init> : (Lokhttp3/internal/cache/DiskLruCache;Lokhttp3/internal/cache/DiskLruCache$Entry;)V
    //   196: aload #7
    //   198: aload_1
    //   199: putfield currentEditor : Lokhttp3/internal/cache/DiskLruCache$Editor;
    //   202: aload_0
    //   203: monitorexit
    //   204: aload_1
    //   205: areturn
    //   206: aload_0
    //   207: getfield executor : Ljava/util/concurrent/Executor;
    //   210: aload_0
    //   211: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   214: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   219: aload_0
    //   220: monitorexit
    //   221: aconst_null
    //   222: areturn
    //   223: astore_1
    //   224: aload_0
    //   225: monitorexit
    //   226: aload_1
    //   227: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	223	finally
    //   41	48	223	finally
    //   64	71	223	finally
    //   80	94	223	finally
    //   97	144	223	finally
    //   162	185	223	finally
    //   185	202	223	finally
    //   206	219	223	finally
  }
  
  public void evictAll() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: invokevirtual values : ()Ljava/util/Collection;
    //   13: aload_0
    //   14: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   17: invokevirtual size : ()I
    //   20: anewarray okhttp3/internal/cache/DiskLruCache$Entry
    //   23: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   28: checkcast [Lokhttp3/internal/cache/DiskLruCache$Entry;
    //   31: astore_3
    //   32: aload_3
    //   33: arraylength
    //   34: istore_2
    //   35: iconst_0
    //   36: istore_1
    //   37: iload_1
    //   38: iload_2
    //   39: if_icmpge -> 56
    //   42: aload_0
    //   43: aload_3
    //   44: iload_1
    //   45: aaload
    //   46: invokevirtual removeEntry : (Lokhttp3/internal/cache/DiskLruCache$Entry;)Z
    //   49: pop
    //   50: iinc #1, 1
    //   53: goto -> 37
    //   56: aload_0
    //   57: iconst_0
    //   58: putfield mostRecentTrimFailed : Z
    //   61: aload_0
    //   62: monitorexit
    //   63: return
    //   64: astore_3
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_3
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	35	64	finally
    //   42	50	64	finally
    //   56	61	64	finally
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield initialized : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: invokespecial checkNotClosed : ()V
    //   18: aload_0
    //   19: invokevirtual trimToSize : ()V
    //   22: aload_0
    //   23: getfield journalWriter : LI丨L/I丨L;
    //   26: invokeinterface flush : ()V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_2
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	34	finally
    //   14	31	34	finally
  }
  
  public Snapshot get(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: invokespecial checkNotClosed : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial validateKey : (Ljava/lang/String;)V
    //   15: aload_0
    //   16: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   19: aload_1
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast okhttp3/internal/cache/DiskLruCache$Entry
    //   26: astore_2
    //   27: aload_2
    //   28: ifnull -> 120
    //   31: aload_2
    //   32: getfield readable : Z
    //   35: ifne -> 41
    //   38: goto -> 120
    //   41: aload_2
    //   42: invokevirtual snapshot : ()Lokhttp3/internal/cache/DiskLruCache$Snapshot;
    //   45: astore_2
    //   46: aload_2
    //   47: ifnonnull -> 54
    //   50: aload_0
    //   51: monitorexit
    //   52: aconst_null
    //   53: areturn
    //   54: aload_0
    //   55: aload_0
    //   56: getfield redundantOpCount : I
    //   59: iconst_1
    //   60: iadd
    //   61: putfield redundantOpCount : I
    //   64: aload_0
    //   65: getfield journalWriter : LI丨L/I丨L;
    //   68: ldc 'READ'
    //   70: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   75: bipush #32
    //   77: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   82: aload_1
    //   83: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   88: bipush #10
    //   90: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   95: pop
    //   96: aload_0
    //   97: invokevirtual journalRebuildRequired : ()Z
    //   100: ifeq -> 116
    //   103: aload_0
    //   104: getfield executor : Ljava/util/concurrent/Executor;
    //   107: aload_0
    //   108: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   111: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_2
    //   119: areturn
    //   120: aload_0
    //   121: monitorexit
    //   122: aconst_null
    //   123: areturn
    //   124: astore_1
    //   125: aload_0
    //   126: monitorexit
    //   127: aload_1
    //   128: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	124	finally
    //   31	38	124	finally
    //   41	46	124	finally
    //   54	116	124	finally
  }
  
  public File getDirectory() {
    return this.directory;
  }
  
  public long getMaxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void initialize() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield initialized : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifeq -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   18: aload_0
    //   19: getfield journalFileBackup : Ljava/io/File;
    //   22: invokeinterface exists : (Ljava/io/File;)Z
    //   27: ifeq -> 79
    //   30: aload_0
    //   31: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   34: aload_0
    //   35: getfield journalFile : Ljava/io/File;
    //   38: invokeinterface exists : (Ljava/io/File;)Z
    //   43: ifeq -> 62
    //   46: aload_0
    //   47: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   50: aload_0
    //   51: getfield journalFileBackup : Ljava/io/File;
    //   54: invokeinterface delete : (Ljava/io/File;)V
    //   59: goto -> 79
    //   62: aload_0
    //   63: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   66: aload_0
    //   67: getfield journalFileBackup : Ljava/io/File;
    //   70: aload_0
    //   71: getfield journalFile : Ljava/io/File;
    //   74: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   79: aload_0
    //   80: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   83: aload_0
    //   84: getfield journalFile : Ljava/io/File;
    //   87: invokeinterface exists : (Ljava/io/File;)Z
    //   92: istore_1
    //   93: iload_1
    //   94: ifeq -> 206
    //   97: aload_0
    //   98: invokespecial readJournal : ()V
    //   101: aload_0
    //   102: invokespecial processJournal : ()V
    //   105: aload_0
    //   106: iconst_1
    //   107: putfield initialized : Z
    //   110: aload_0
    //   111: monitorexit
    //   112: return
    //   113: astore_2
    //   114: invokestatic get : ()Lokhttp3/internal/platform/Platform;
    //   117: astore_3
    //   118: new java/lang/StringBuilder
    //   121: astore #4
    //   123: aload #4
    //   125: invokespecial <init> : ()V
    //   128: aload #4
    //   130: ldc_w 'DiskLruCache '
    //   133: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: pop
    //   137: aload #4
    //   139: aload_0
    //   140: getfield directory : Ljava/io/File;
    //   143: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload #4
    //   149: ldc_w ' is corrupt: '
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload #4
    //   158: aload_2
    //   159: invokevirtual getMessage : ()Ljava/lang/String;
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload #4
    //   168: ldc_w ', removing'
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_3
    //   176: iconst_5
    //   177: aload #4
    //   179: invokevirtual toString : ()Ljava/lang/String;
    //   182: aload_2
    //   183: invokevirtual log : (ILjava/lang/String;Ljava/lang/Throwable;)V
    //   186: aload_0
    //   187: invokevirtual delete : ()V
    //   190: aload_0
    //   191: iconst_0
    //   192: putfield closed : Z
    //   195: goto -> 206
    //   198: astore_2
    //   199: aload_0
    //   200: iconst_0
    //   201: putfield closed : Z
    //   204: aload_2
    //   205: athrow
    //   206: aload_0
    //   207: invokevirtual rebuildJournal : ()V
    //   210: aload_0
    //   211: iconst_1
    //   212: putfield initialized : Z
    //   215: aload_0
    //   216: monitorexit
    //   217: return
    //   218: astore_2
    //   219: aload_0
    //   220: monitorexit
    //   221: aload_2
    //   222: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	218	finally
    //   14	59	218	finally
    //   62	79	218	finally
    //   79	93	218	finally
    //   97	110	113	java/io/IOException
    //   97	110	218	finally
    //   114	186	218	finally
    //   186	190	198	finally
    //   190	195	218	finally
    //   199	206	218	finally
    //   206	215	218	finally
  }
  
  public boolean isClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean journalRebuildRequired() {
    boolean bool;
    int i = this.redundantOpCount;
    if (i >= 2000 && i >= this.lruEntries.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void rebuildJournal() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : LI丨L/I丨L;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 17
    //   11: aload_1
    //   12: invokeinterface close : ()V
    //   17: aload_0
    //   18: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   21: aload_0
    //   22: getfield journalFileTmp : Ljava/io/File;
    //   25: invokeinterface sink : (Ljava/io/File;)LI丨L/I11li1;
    //   30: invokestatic I1I : (LI丨L/I11li1;)LI丨L/I丨L;
    //   33: astore_1
    //   34: aload_1
    //   35: ldc 'libcore.io.DiskLruCache'
    //   37: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   42: bipush #10
    //   44: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   49: pop
    //   50: aload_1
    //   51: ldc '1'
    //   53: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   58: bipush #10
    //   60: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   65: pop
    //   66: aload_1
    //   67: aload_0
    //   68: getfield appVersion : I
    //   71: i2l
    //   72: invokeinterface 丨l丨 : (J)LI丨L/I丨L;
    //   77: bipush #10
    //   79: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   84: pop
    //   85: aload_1
    //   86: aload_0
    //   87: getfield valueCount : I
    //   90: i2l
    //   91: invokeinterface 丨l丨 : (J)LI丨L/I丨L;
    //   96: bipush #10
    //   98: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   103: pop
    //   104: aload_1
    //   105: bipush #10
    //   107: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   112: pop
    //   113: aload_0
    //   114: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   117: invokevirtual values : ()Ljava/util/Collection;
    //   120: invokeinterface iterator : ()Ljava/util/Iterator;
    //   125: astore_3
    //   126: aload_3
    //   127: invokeinterface hasNext : ()Z
    //   132: ifeq -> 235
    //   135: aload_3
    //   136: invokeinterface next : ()Ljava/lang/Object;
    //   141: checkcast okhttp3/internal/cache/DiskLruCache$Entry
    //   144: astore_2
    //   145: aload_2
    //   146: getfield currentEditor : Lokhttp3/internal/cache/DiskLruCache$Editor;
    //   149: ifnull -> 191
    //   152: aload_1
    //   153: ldc 'DIRTY'
    //   155: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   160: bipush #32
    //   162: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   167: pop
    //   168: aload_1
    //   169: aload_2
    //   170: getfield key : Ljava/lang/String;
    //   173: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   178: pop
    //   179: aload_1
    //   180: bipush #10
    //   182: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   187: pop
    //   188: goto -> 126
    //   191: aload_1
    //   192: ldc 'CLEAN'
    //   194: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   199: bipush #32
    //   201: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   206: pop
    //   207: aload_1
    //   208: aload_2
    //   209: getfield key : Ljava/lang/String;
    //   212: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
    //   217: pop
    //   218: aload_2
    //   219: aload_1
    //   220: invokevirtual writeLengths : (LI丨L/I丨L;)V
    //   223: aload_1
    //   224: bipush #10
    //   226: invokeinterface writeByte : (I)LI丨L/I丨L;
    //   231: pop
    //   232: goto -> 126
    //   235: aload_1
    //   236: ifnull -> 244
    //   239: aconst_null
    //   240: aload_1
    //   241: invokestatic $closeResource : (Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   244: aload_0
    //   245: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   248: aload_0
    //   249: getfield journalFile : Ljava/io/File;
    //   252: invokeinterface exists : (Ljava/io/File;)Z
    //   257: ifeq -> 277
    //   260: aload_0
    //   261: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   264: aload_0
    //   265: getfield journalFile : Ljava/io/File;
    //   268: aload_0
    //   269: getfield journalFileBackup : Ljava/io/File;
    //   272: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   277: aload_0
    //   278: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   281: aload_0
    //   282: getfield journalFileTmp : Ljava/io/File;
    //   285: aload_0
    //   286: getfield journalFile : Ljava/io/File;
    //   289: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   294: aload_0
    //   295: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   298: aload_0
    //   299: getfield journalFileBackup : Ljava/io/File;
    //   302: invokeinterface delete : (Ljava/io/File;)V
    //   307: aload_0
    //   308: aload_0
    //   309: invokespecial newJournalWriter : ()LI丨L/I丨L;
    //   312: putfield journalWriter : LI丨L/I丨L;
    //   315: aload_0
    //   316: iconst_0
    //   317: putfield hasJournalErrors : Z
    //   320: aload_0
    //   321: iconst_0
    //   322: putfield mostRecentRebuildFailed : Z
    //   325: aload_0
    //   326: monitorexit
    //   327: return
    //   328: astore_2
    //   329: aload_2
    //   330: athrow
    //   331: astore_3
    //   332: aload_1
    //   333: ifnull -> 341
    //   336: aload_2
    //   337: aload_1
    //   338: invokestatic $closeResource : (Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   341: aload_3
    //   342: athrow
    //   343: astore_1
    //   344: aload_0
    //   345: monitorexit
    //   346: aload_1
    //   347: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	343	finally
    //   11	17	343	finally
    //   17	34	343	finally
    //   34	126	328	finally
    //   126	188	328	finally
    //   191	232	328	finally
    //   239	244	343	finally
    //   244	277	343	finally
    //   277	325	343	finally
    //   329	331	331	finally
    //   336	341	343	finally
    //   341	343	343	finally
  }
  
  public boolean remove(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: invokespecial checkNotClosed : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial validateKey : (Ljava/lang/String;)V
    //   15: aload_0
    //   16: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   19: aload_1
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast okhttp3/internal/cache/DiskLruCache$Entry
    //   26: astore_1
    //   27: aload_1
    //   28: ifnonnull -> 35
    //   31: aload_0
    //   32: monitorexit
    //   33: iconst_0
    //   34: ireturn
    //   35: aload_0
    //   36: aload_1
    //   37: invokevirtual removeEntry : (Lokhttp3/internal/cache/DiskLruCache$Entry;)Z
    //   40: istore_2
    //   41: iload_2
    //   42: ifeq -> 62
    //   45: aload_0
    //   46: getfield size : J
    //   49: aload_0
    //   50: getfield maxSize : J
    //   53: lcmp
    //   54: ifgt -> 62
    //   57: aload_0
    //   58: iconst_0
    //   59: putfield mostRecentTrimFailed : Z
    //   62: aload_0
    //   63: monitorexit
    //   64: iload_2
    //   65: ireturn
    //   66: astore_1
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_1
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	66	finally
    //   35	41	66	finally
    //   45	62	66	finally
  }
  
  public boolean removeEntry(Entry paramEntry) throws IOException {
    Editor editor = paramEntry.currentEditor;
    if (editor != null)
      editor.detach(); 
    for (byte b = 0; b < this.valueCount; b++) {
      this.fileSystem.delete(paramEntry.cleanFiles[b]);
      long l = this.size;
      long[] arrayOfLong = paramEntry.lengths;
      this.size = l - arrayOfLong[b];
      arrayOfLong[b] = 0L;
    } 
    this.redundantOpCount++;
    this.journalWriter.ILL("REMOVE").writeByte(32).ILL(paramEntry.key).writeByte(10);
    this.lruEntries.remove(paramEntry.key);
    if (journalRebuildRequired())
      this.executor.execute(this.cleanupRunnable); 
    return true;
  }
  
  public void setMaxSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: putfield maxSize : J
    //   7: aload_0
    //   8: getfield initialized : Z
    //   11: ifeq -> 27
    //   14: aload_0
    //   15: getfield executor : Ljava/util/concurrent/Executor;
    //   18: aload_0
    //   19: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   22: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_3
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_3
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public long size() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: getfield size : J
    //   10: lstore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: lload_1
    //   14: lreturn
    //   15: astore_3
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_3
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public Iterator<Snapshot> snapshots() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: new okhttp3/internal/cache/DiskLruCache$3
    //   9: dup
    //   10: aload_0
    //   11: invokespecial <init> : (Lokhttp3/internal/cache/DiskLruCache;)V
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: areturn
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  public void trimToSize() throws IOException {
    while (this.size > this.maxSize)
      removeEntry(this.lruEntries.values().iterator().next()); 
    this.mostRecentTrimFailed = false;
  }
  
  public final class Editor {
    private boolean done;
    
    public final DiskLruCache.Entry entry;
    
    public final DiskLruCache this$0;
    
    public final boolean[] written;
    
    public Editor(DiskLruCache.Entry param1Entry) {
      boolean[] arrayOfBoolean;
      this.entry = param1Entry;
      if (param1Entry.readable) {
        DiskLruCache.this = null;
      } else {
        arrayOfBoolean = new boolean[DiskLruCache.this.valueCount];
      } 
      this.written = arrayOfBoolean;
    }
    
    public void abort() throws IOException {
      synchronized (DiskLruCache.this) {
        if (!this.done) {
          if (this.entry.currentEditor == this)
            DiskLruCache.this.completeEdit(this, false); 
          this.done = true;
          return;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public void abortUnlessCommitted() {
      synchronized (DiskLruCache.this) {
        if (!this.done) {
          Editor editor = this.entry.currentEditor;
          if (editor == this)
            try {
              DiskLruCache.this.completeEdit(this, false);
            } catch (IOException iOException) {} 
        } 
        return;
      } 
    }
    
    public void commit() throws IOException {
      synchronized (DiskLruCache.this) {
        if (!this.done) {
          if (this.entry.currentEditor == this)
            DiskLruCache.this.completeEdit(this, true); 
          this.done = true;
          return;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public void detach() {
      if (this.entry.currentEditor == this) {
        byte b = 0;
        while (true) {
          DiskLruCache diskLruCache = DiskLruCache.this;
          if (b < diskLruCache.valueCount) {
            try {
              diskLruCache.fileSystem.delete(this.entry.dirtyFiles[b]);
            } catch (IOException iOException) {}
            b++;
            continue;
          } 
          this.entry.currentEditor = null;
          return;
        } 
      } 
    }
    
    public I11li1 newSink(int param1Int) {
      synchronized (DiskLruCache.this) {
        if (!this.done) {
          I11li1 i11li1;
          DiskLruCache.Entry entry = this.entry;
          if (entry.currentEditor != this) {
            i11li1 = lIi丨I.ILil();
            return i11li1;
          } 
          if (!((DiskLruCache.Entry)i11li1).readable)
            this.written[param1Int] = true; 
          File file = ((DiskLruCache.Entry)i11li1).dirtyFiles[param1Int];
          try {
            I11li1 i11li11 = DiskLruCache.this.fileSystem.sink(file);
            FaultHidingSink faultHidingSink = new FaultHidingSink() {
                public final DiskLruCache.Editor this$1;
                
                public void onException(IOException param2IOException) {
                  synchronized (DiskLruCache.this) {
                    DiskLruCache.Editor.this.detach();
                    return;
                  } 
                }
              };
            super(this, i11li11);
            return faultHidingSink;
          } catch (FileNotFoundException fileNotFoundException) {
            return lIi丨I.ILil();
          } 
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public 丨lL newSource(int param1Int) {
      synchronized (DiskLruCache.this) {
        if (!this.done) {
          DiskLruCache.Entry entry = this.entry;
          if (entry.readable) {
            Editor editor = entry.currentEditor;
            if (editor == this)
              try {
                return DiskLruCache.this.fileSystem.source(entry.cleanFiles[param1Int]);
              } catch (FileNotFoundException fileNotFoundException) {
                return null;
              }  
          } 
          return null;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
  }
  
  public class null extends FaultHidingSink {
    public final DiskLruCache.Editor this$1;
    
    public null(I11li1 param1I11li1) {
      super(param1I11li1);
    }
    
    public void onException(IOException param1IOException) {
      synchronized (DiskLruCache.this) {
        this.this$1.detach();
        return;
      } 
    }
  }
  
  public final class Entry {
    public final File[] cleanFiles;
    
    public DiskLruCache.Editor currentEditor;
    
    public final File[] dirtyFiles;
    
    public final String key;
    
    public final long[] lengths;
    
    public boolean readable;
    
    public long sequenceNumber;
    
    public final DiskLruCache this$0;
    
    public Entry(String param1String) {
      this.key = param1String;
      int i = DiskLruCache.this.valueCount;
      this.lengths = new long[i];
      this.cleanFiles = new File[i];
      this.dirtyFiles = new File[i];
      StringBuilder stringBuilder = new StringBuilder(param1String);
      stringBuilder.append('.');
      int j = stringBuilder.length();
      for (i = 0; i < DiskLruCache.this.valueCount; i++) {
        stringBuilder.append(i);
        this.cleanFiles[i] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.append(".tmp");
        this.dirtyFiles[i] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.setLength(j);
      } 
    }
    
    private IOException invalidLengths(String[] param1ArrayOfString) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected journal line: ");
      stringBuilder.append(Arrays.toString((Object[])param1ArrayOfString));
      throw new IOException(stringBuilder.toString());
    }
    
    public void setLengths(String[] param1ArrayOfString) throws IOException {
      if (param1ArrayOfString.length == DiskLruCache.this.valueCount) {
        byte b = 0;
        try {
          while (b < param1ArrayOfString.length) {
            this.lengths[b] = Long.parseLong(param1ArrayOfString[b]);
            b++;
          } 
          return;
        } catch (NumberFormatException numberFormatException) {
          throw invalidLengths(param1ArrayOfString);
        } 
      } 
      throw invalidLengths(param1ArrayOfString);
    }
    
    public DiskLruCache.Snapshot snapshot() {
      if (Thread.holdsLock(DiskLruCache.this)) {
        丨lL[] arrayOf丨lL = new 丨lL[DiskLruCache.this.valueCount];
        long[] arrayOfLong = (long[])this.lengths.clone();
        boolean bool = false;
        byte b = 0;
        try {
          while (true) {
            DiskLruCache diskLruCache = DiskLruCache.this;
            if (b < diskLruCache.valueCount) {
              arrayOf丨lL[b] = diskLruCache.fileSystem.source(this.cleanFiles[b]);
              b++;
              continue;
            } 
            return new DiskLruCache.Snapshot(this.key, this.sequenceNumber, arrayOf丨lL, arrayOfLong);
          } 
        } catch (FileNotFoundException fileNotFoundException) {
          DiskLruCache diskLruCache;
          b = bool;
          while (true) {
            diskLruCache = DiskLruCache.this;
            if (b < diskLruCache.valueCount && arrayOf丨lL[b] != null) {
              Util.closeQuietly(arrayOf丨lL[b]);
              b++;
              continue;
            } 
            break;
          } 
          try {
            diskLruCache.removeEntry(this);
          } catch (IOException iOException) {}
          return null;
        } 
      } 
      throw new AssertionError();
    }
    
    public void writeLengths(I丨L param1I丨L) throws IOException {
      for (long l : this.lengths)
        param1I丨L.writeByte(32).丨l丨(l); 
    }
  }
  
  public final class Snapshot implements Closeable {
    private final String key;
    
    private final long[] lengths;
    
    private final long sequenceNumber;
    
    private final 丨lL[] sources;
    
    public final DiskLruCache this$0;
    
    public Snapshot(String param1String, long param1Long, 丨lL[] param1ArrayOf丨lL, long[] param1ArrayOflong) {
      this.key = param1String;
      this.sequenceNumber = param1Long;
      this.sources = param1ArrayOf丨lL;
      this.lengths = param1ArrayOflong;
    }
    
    public void close() {
      丨lL[] arrayOf丨lL = this.sources;
      int i = arrayOf丨lL.length;
      for (byte b = 0; b < i; b++)
        Util.closeQuietly(arrayOf丨lL[b]); 
    }
    
    @Nullable
    public DiskLruCache.Editor edit() throws IOException {
      return DiskLruCache.this.edit(this.key, this.sequenceNumber);
    }
    
    public long getLength(int param1Int) {
      return this.lengths[param1Int];
    }
    
    public 丨lL getSource(int param1Int) {
      return this.sources[param1Int];
    }
    
    public String key() {
      return this.key;
    }
  }
}
