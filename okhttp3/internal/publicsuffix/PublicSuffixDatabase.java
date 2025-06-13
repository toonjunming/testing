package okhttp3.internal.publicsuffix;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.internal.platform.Platform;

public final class PublicSuffixDatabase {
  private static final String[] EMPTY_RULE;
  
  private static final byte EXCEPTION_MARKER = 33;
  
  private static final String[] PREVAILING_RULE;
  
  public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
  
  private static final byte[] WILDCARD_LABEL = new byte[] { 42 };
  
  private static final PublicSuffixDatabase instance;
  
  private final AtomicBoolean listRead = new AtomicBoolean(false);
  
  private byte[] publicSuffixExceptionListBytes;
  
  private byte[] publicSuffixListBytes;
  
  private final CountDownLatch readCompleteLatch = new CountDownLatch(1);
  
  static {
    EMPTY_RULE = new String[0];
    PREVAILING_RULE = new String[] { "*" };
    instance = new PublicSuffixDatabase();
  }
  
  private static String binarySearchBytes(byte[] paramArrayOfbyte, byte[][] paramArrayOfbyte1, int paramInt) {
    int j = paramArrayOfbyte.length;
    int i = 0;
    label55: while (i < j) {
      int m;
      int i1;
      int i2;
      int k;
      for (k = (i + j) / 2; k > -1 && paramArrayOfbyte[k] != 10; k--);
      int n = k + 1;
      k = 1;
      while (true) {
        i1 = n + k;
        if (paramArrayOfbyte[i1] != 10) {
          k++;
          continue;
        } 
        i2 = i1 - n;
        int i3 = paramInt;
        m = 0;
        k = 0;
        byte b = 0;
        while (true) {
          int i4;
          if (m) {
            i4 = 46;
            m = 0;
          } else {
            byte b1 = paramArrayOfbyte1[i3][k];
            i4 = b1 & 0xFF;
          } 
          i4 -= paramArrayOfbyte[n + b] & 0xFF;
          if (i4 == 0) {
            b++;
            k++;
            if (b != i2)
              if ((paramArrayOfbyte1[i3]).length == k) {
                if (i3 != paramArrayOfbyte1.length - 1) {
                  i3++;
                  m = 1;
                  k = -1;
                  continue;
                } 
              } else {
                continue;
              }  
          } 
          if (i4 < 0)
            continue; 
          if (i4 > 0)
            continue; 
          m = i2 - b;
          for (k = (paramArrayOfbyte1[i3]).length - k; ++i3 < paramArrayOfbyte1.length; k += (paramArrayOfbyte1[i3]).length);
          break;
        } 
        return (String)paramArrayOfbyte;
      } 
      if (k < m) {
        j = n - 1;
        continue label55;
      } 
      if (k > m) {
        i = i1 + 1;
        continue label55;
      } 
      String str = new String(paramArrayOfbyte, n, i2, StandardCharsets.UTF_8);
    } 
    return null;
  }
  
  private String[] findMatchingRule(String[] paramArrayOfString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield listRead : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   4: invokevirtual get : ()Z
    //   7: istore #5
    //   9: iconst_0
    //   10: istore_3
    //   11: iload #5
    //   13: ifne -> 35
    //   16: aload_0
    //   17: getfield listRead : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   20: iconst_0
    //   21: iconst_1
    //   22: invokevirtual compareAndSet : (ZZ)Z
    //   25: ifeq -> 35
    //   28: aload_0
    //   29: invokespecial readTheListUninterruptibly : ()V
    //   32: goto -> 53
    //   35: aload_0
    //   36: getfield readCompleteLatch : Ljava/util/concurrent/CountDownLatch;
    //   39: invokevirtual await : ()V
    //   42: goto -> 53
    //   45: astore #6
    //   47: invokestatic currentThread : ()Ljava/lang/Thread;
    //   50: invokevirtual interrupt : ()V
    //   53: aload_0
    //   54: monitorenter
    //   55: aload_0
    //   56: getfield publicSuffixListBytes : [B
    //   59: ifnull -> 357
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_1
    //   65: arraylength
    //   66: istore #4
    //   68: iload #4
    //   70: anewarray [B
    //   73: astore #9
    //   75: iconst_0
    //   76: istore_2
    //   77: iload_2
    //   78: aload_1
    //   79: arraylength
    //   80: if_icmpge -> 102
    //   83: aload #9
    //   85: iload_2
    //   86: aload_1
    //   87: iload_2
    //   88: aaload
    //   89: getstatic java/nio/charset/StandardCharsets.UTF_8 : Ljava/nio/charset/Charset;
    //   92: invokevirtual getBytes : (Ljava/nio/charset/Charset;)[B
    //   95: aastore
    //   96: iinc #2, 1
    //   99: goto -> 77
    //   102: iconst_0
    //   103: istore_2
    //   104: aconst_null
    //   105: astore #8
    //   107: iload_2
    //   108: iload #4
    //   110: if_icmpge -> 137
    //   113: aload_0
    //   114: getfield publicSuffixListBytes : [B
    //   117: aload #9
    //   119: iload_2
    //   120: invokestatic binarySearchBytes : ([B[[BI)Ljava/lang/String;
    //   123: astore_1
    //   124: aload_1
    //   125: ifnull -> 131
    //   128: goto -> 139
    //   131: iinc #2, 1
    //   134: goto -> 104
    //   137: aconst_null
    //   138: astore_1
    //   139: iload #4
    //   141: iconst_1
    //   142: if_icmple -> 199
    //   145: aload #9
    //   147: invokevirtual clone : ()Ljava/lang/Object;
    //   150: checkcast [[B
    //   153: astore #7
    //   155: iconst_0
    //   156: istore_2
    //   157: iload_2
    //   158: aload #7
    //   160: arraylength
    //   161: iconst_1
    //   162: isub
    //   163: if_icmpge -> 199
    //   166: aload #7
    //   168: iload_2
    //   169: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.WILDCARD_LABEL : [B
    //   172: aastore
    //   173: aload_0
    //   174: getfield publicSuffixListBytes : [B
    //   177: aload #7
    //   179: iload_2
    //   180: invokestatic binarySearchBytes : ([B[[BI)Ljava/lang/String;
    //   183: astore #6
    //   185: aload #6
    //   187: ifnull -> 193
    //   190: goto -> 202
    //   193: iinc #2, 1
    //   196: goto -> 157
    //   199: aconst_null
    //   200: astore #6
    //   202: aload #8
    //   204: astore #7
    //   206: aload #6
    //   208: ifnull -> 251
    //   211: iload_3
    //   212: istore_2
    //   213: aload #8
    //   215: astore #7
    //   217: iload_2
    //   218: iload #4
    //   220: iconst_1
    //   221: isub
    //   222: if_icmpge -> 251
    //   225: aload_0
    //   226: getfield publicSuffixExceptionListBytes : [B
    //   229: aload #9
    //   231: iload_2
    //   232: invokestatic binarySearchBytes : ([B[[BI)Ljava/lang/String;
    //   235: astore #7
    //   237: aload #7
    //   239: ifnull -> 245
    //   242: goto -> 251
    //   245: iinc #2, 1
    //   248: goto -> 213
    //   251: aload #7
    //   253: ifnull -> 288
    //   256: new java/lang/StringBuilder
    //   259: dup
    //   260: invokespecial <init> : ()V
    //   263: astore_1
    //   264: aload_1
    //   265: ldc '!'
    //   267: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: pop
    //   271: aload_1
    //   272: aload #7
    //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: pop
    //   278: aload_1
    //   279: invokevirtual toString : ()Ljava/lang/String;
    //   282: ldc '\.'
    //   284: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   287: areturn
    //   288: aload_1
    //   289: ifnonnull -> 301
    //   292: aload #6
    //   294: ifnonnull -> 301
    //   297: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.PREVAILING_RULE : [Ljava/lang/String;
    //   300: areturn
    //   301: aload_1
    //   302: ifnull -> 315
    //   305: aload_1
    //   306: ldc '\.'
    //   308: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   311: astore_1
    //   312: goto -> 319
    //   315: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.EMPTY_RULE : [Ljava/lang/String;
    //   318: astore_1
    //   319: aload #6
    //   321: ifnull -> 336
    //   324: aload #6
    //   326: ldc '\.'
    //   328: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   331: astore #6
    //   333: goto -> 341
    //   336: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.EMPTY_RULE : [Ljava/lang/String;
    //   339: astore #6
    //   341: aload_1
    //   342: arraylength
    //   343: aload #6
    //   345: arraylength
    //   346: if_icmple -> 352
    //   349: goto -> 355
    //   352: aload #6
    //   354: astore_1
    //   355: aload_1
    //   356: areturn
    //   357: new java/lang/IllegalStateException
    //   360: astore_1
    //   361: aload_1
    //   362: ldc 'Unable to load publicsuffixes.gz resource from the classpath.'
    //   364: invokespecial <init> : (Ljava/lang/String;)V
    //   367: aload_1
    //   368: athrow
    //   369: astore_1
    //   370: aload_0
    //   371: monitorexit
    //   372: aload_1
    //   373: athrow
    // Exception table:
    //   from	to	target	type
    //   35	42	45	java/lang/InterruptedException
    //   55	64	369	finally
    //   357	369	369	finally
    //   370	372	369	finally
  }
  
  public static PublicSuffixDatabase get() {
    return instance;
  }
  
  private void readTheList() throws IOException {
    // Byte code:
    //   0: ldc okhttp3/internal/publicsuffix/PublicSuffixDatabase
    //   2: ldc 'publicsuffixes.gz'
    //   4: invokevirtual getResourceAsStream : (Ljava/lang/String;)Ljava/io/InputStream;
    //   7: astore_1
    //   8: aload_1
    //   9: ifnonnull -> 13
    //   12: return
    //   13: new I丨L/ILL
    //   16: dup
    //   17: aload_1
    //   18: invokestatic ILL : (Ljava/io/InputStream;)LI丨L/丨lL;
    //   21: invokespecial <init> : (LI丨L/丨lL;)V
    //   24: invokestatic I丨L : (LI丨L/丨lL;)LI丨L/l丨Li1LL;
    //   27: astore_1
    //   28: aload_1
    //   29: invokeinterface readInt : ()I
    //   34: newarray byte
    //   36: astore_3
    //   37: aload_1
    //   38: aload_3
    //   39: invokeinterface readFully : ([B)V
    //   44: aload_1
    //   45: invokeinterface readInt : ()I
    //   50: newarray byte
    //   52: astore_2
    //   53: aload_1
    //   54: aload_2
    //   55: invokeinterface readFully : ([B)V
    //   60: aload_1
    //   61: ifnull -> 70
    //   64: aload_1
    //   65: invokeinterface close : ()V
    //   70: aload_0
    //   71: monitorenter
    //   72: aload_0
    //   73: aload_3
    //   74: putfield publicSuffixListBytes : [B
    //   77: aload_0
    //   78: aload_2
    //   79: putfield publicSuffixExceptionListBytes : [B
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_0
    //   85: getfield readCompleteLatch : Ljava/util/concurrent/CountDownLatch;
    //   88: invokevirtual countDown : ()V
    //   91: return
    //   92: astore_1
    //   93: aload_0
    //   94: monitorexit
    //   95: aload_1
    //   96: athrow
    //   97: astore_2
    //   98: aload_2
    //   99: athrow
    //   100: astore_3
    //   101: aload_1
    //   102: ifnull -> 120
    //   105: aload_1
    //   106: invokeinterface close : ()V
    //   111: goto -> 120
    //   114: astore_1
    //   115: aload_2
    //   116: aload_1
    //   117: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
    //   120: aload_3
    //   121: athrow
    // Exception table:
    //   from	to	target	type
    //   28	60	97	finally
    //   72	84	92	finally
    //   93	95	92	finally
    //   98	100	100	finally
    //   105	111	114	finally
  }
  
  private void readTheListUninterruptibly() {
    boolean bool = false;
    while (true) {
      Exception exception;
      try {
        readTheList();
        if (bool)
          Thread.currentThread().interrupt(); 
        return;
      } catch (InterruptedIOException null) {
        Thread.interrupted();
        bool = true;
        continue;
      } catch (IOException null) {
        Platform.get().log(5, "Failed to read public suffix list", exception);
        if (bool)
          Thread.currentThread().interrupt(); 
        return;
      } finally {}
      if (bool)
        Thread.currentThread().interrupt(); 
      throw exception;
    } 
  }
  
  public String getEffectiveTldPlusOne(String paramString) {
    int j;
    Objects.requireNonNull(paramString, "domain == null");
    String[] arrayOfString2 = IDN.toUnicode(paramString).split("\\.");
    String[] arrayOfString3 = findMatchingRule(arrayOfString2);
    if (arrayOfString2.length == arrayOfString3.length && arrayOfString3[0].charAt(0) != '!')
      return null; 
    if (arrayOfString3[0].charAt(0) == '!') {
      j = arrayOfString2.length;
      i = arrayOfString3.length;
    } else {
      j = arrayOfString2.length;
      i = arrayOfString3.length + 1;
    } 
    int i = j - i;
    StringBuilder stringBuilder = new StringBuilder();
    String[] arrayOfString1 = paramString.split("\\.");
    while (i < arrayOfString1.length) {
      stringBuilder.append(arrayOfString1[i]);
      stringBuilder.append('.');
      i++;
    } 
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }
  
  public void setListBytes(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    this.publicSuffixListBytes = paramArrayOfbyte1;
    this.publicSuffixExceptionListBytes = paramArrayOfbyte2;
    this.listRead.set(true);
    this.readCompleteLatch.countDown();
  }
}
