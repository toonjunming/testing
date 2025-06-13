package javax.activation;

import com.sun.activation.registries.MimeTypeFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class MimetypesFileTypeMap extends FileTypeMap {
  private static final int PROG = 0;
  
  private static MimeTypeFile defDB;
  
  private static String defaultType = "application/octet-stream";
  
  private MimeTypeFile[] DB;
  
  public MimetypesFileTypeMap() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: new java/util/Vector
    //   7: dup
    //   8: iconst_5
    //   9: invokespecial <init> : (I)V
    //   12: astore_1
    //   13: aload_1
    //   14: aconst_null
    //   15: invokevirtual addElement : (Ljava/lang/Object;)V
    //   18: ldc 'MimetypesFileTypeMap: load HOME'
    //   20: invokestatic log : (Ljava/lang/String;)V
    //   23: ldc 'user.home'
    //   25: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   28: astore_2
    //   29: aload_2
    //   30: ifnull -> 78
    //   33: new java/lang/StringBuilder
    //   36: astore_3
    //   37: aload_3
    //   38: aload_2
    //   39: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   42: invokespecial <init> : (Ljava/lang/String;)V
    //   45: aload_3
    //   46: getstatic java/io/File.separator : Ljava/lang/String;
    //   49: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: pop
    //   53: aload_3
    //   54: ldc '.mime.types'
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload_0
    //   61: aload_3
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: invokespecial loadFile : (Ljava/lang/String;)Lcom/sun/activation/registries/MimeTypeFile;
    //   68: astore_2
    //   69: aload_2
    //   70: ifnull -> 78
    //   73: aload_1
    //   74: aload_2
    //   75: invokevirtual addElement : (Ljava/lang/Object;)V
    //   78: ldc 'MimetypesFileTypeMap: load SYS'
    //   80: invokestatic log : (Ljava/lang/String;)V
    //   83: new java/lang/StringBuilder
    //   86: astore_2
    //   87: aload_2
    //   88: ldc 'java.home'
    //   90: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   93: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   96: invokespecial <init> : (Ljava/lang/String;)V
    //   99: getstatic java/io/File.separator : Ljava/lang/String;
    //   102: astore_3
    //   103: aload_2
    //   104: aload_3
    //   105: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload_2
    //   110: ldc 'lib'
    //   112: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload_2
    //   117: aload_3
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload_2
    //   123: ldc 'mime.types'
    //   125: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_0
    //   130: aload_2
    //   131: invokevirtual toString : ()Ljava/lang/String;
    //   134: invokespecial loadFile : (Ljava/lang/String;)Lcom/sun/activation/registries/MimeTypeFile;
    //   137: astore_2
    //   138: aload_2
    //   139: ifnull -> 147
    //   142: aload_1
    //   143: aload_2
    //   144: invokevirtual addElement : (Ljava/lang/Object;)V
    //   147: ldc 'MimetypesFileTypeMap: load JAR'
    //   149: invokestatic log : (Ljava/lang/String;)V
    //   152: aload_0
    //   153: aload_1
    //   154: ldc 'mime.types'
    //   156: invokespecial loadAllResources : (Ljava/util/Vector;Ljava/lang/String;)V
    //   159: ldc 'MimetypesFileTypeMap: load DEF'
    //   161: invokestatic log : (Ljava/lang/String;)V
    //   164: ldc javax/activation/MimetypesFileTypeMap
    //   166: monitorenter
    //   167: getstatic javax/activation/MimetypesFileTypeMap.defDB : Lcom/sun/activation/registries/MimeTypeFile;
    //   170: ifnonnull -> 182
    //   173: aload_0
    //   174: ldc '/mimetypes.default'
    //   176: invokespecial loadResource : (Ljava/lang/String;)Lcom/sun/activation/registries/MimeTypeFile;
    //   179: putstatic javax/activation/MimetypesFileTypeMap.defDB : Lcom/sun/activation/registries/MimeTypeFile;
    //   182: ldc javax/activation/MimetypesFileTypeMap
    //   184: monitorexit
    //   185: getstatic javax/activation/MimetypesFileTypeMap.defDB : Lcom/sun/activation/registries/MimeTypeFile;
    //   188: astore_2
    //   189: aload_2
    //   190: ifnull -> 198
    //   193: aload_1
    //   194: aload_2
    //   195: invokevirtual addElement : (Ljava/lang/Object;)V
    //   198: aload_1
    //   199: invokevirtual size : ()I
    //   202: anewarray com/sun/activation/registries/MimeTypeFile
    //   205: astore_2
    //   206: aload_0
    //   207: aload_2
    //   208: putfield DB : [Lcom/sun/activation/registries/MimeTypeFile;
    //   211: aload_1
    //   212: aload_2
    //   213: invokevirtual copyInto : ([Ljava/lang/Object;)V
    //   216: return
    //   217: astore_1
    //   218: ldc javax/activation/MimetypesFileTypeMap
    //   220: monitorexit
    //   221: aload_1
    //   222: athrow
    //   223: astore_2
    //   224: goto -> 78
    //   227: astore_2
    //   228: goto -> 147
    // Exception table:
    //   from	to	target	type
    //   23	29	223	java/lang/SecurityException
    //   33	69	223	java/lang/SecurityException
    //   73	78	223	java/lang/SecurityException
    //   83	138	227	java/lang/SecurityException
    //   142	147	227	java/lang/SecurityException
    //   167	182	217	finally
    //   182	185	217	finally
    //   218	221	217	finally
  }
  
  public MimetypesFileTypeMap(InputStream paramInputStream) {
    this();
    try {
      MimeTypeFile[] arrayOfMimeTypeFile = this.DB;
      MimeTypeFile mimeTypeFile = new MimeTypeFile();
      this(paramInputStream);
      arrayOfMimeTypeFile[0] = mimeTypeFile;
    } catch (IOException iOException) {}
  }
  
  public MimetypesFileTypeMap(String paramString) throws IOException {
    this();
    this.DB[0] = new MimeTypeFile(paramString);
  }
  
  private void loadAllResources(Vector paramVector, String paramString) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: iconst_0
    //   6: istore #6
    //   8: invokestatic getContextClassLoader : ()Ljava/lang/ClassLoader;
    //   11: astore #8
    //   13: aload #8
    //   15: astore #7
    //   17: aload #8
    //   19: ifnonnull -> 31
    //   22: aload_0
    //   23: invokevirtual getClass : ()Ljava/lang/Class;
    //   26: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   29: astore #7
    //   31: aload #7
    //   33: ifnull -> 47
    //   36: aload #7
    //   38: aload_2
    //   39: invokestatic getResources : (Ljava/lang/ClassLoader;Ljava/lang/String;)[Ljava/net/URL;
    //   42: astore #11
    //   44: goto -> 53
    //   47: aload_2
    //   48: invokestatic getSystemResources : (Ljava/lang/String;)[Ljava/net/URL;
    //   51: astore #11
    //   53: aload #11
    //   55: ifnull -> 700
    //   58: invokestatic isLoggable : ()Z
    //   61: ifeq -> 69
    //   64: ldc 'MimetypesFileTypeMap: getResources'
    //   66: invokestatic log : (Ljava/lang/String;)V
    //   69: iconst_0
    //   70: istore_3
    //   71: iload_3
    //   72: istore #4
    //   74: iload #6
    //   76: aload #11
    //   78: arraylength
    //   79: if_icmplt -> 88
    //   82: iload_3
    //   83: istore #4
    //   85: goto -> 700
    //   88: aload #11
    //   90: iload #6
    //   92: aaload
    //   93: astore #12
    //   95: aconst_null
    //   96: astore #9
    //   98: aconst_null
    //   99: astore #10
    //   101: aconst_null
    //   102: astore #8
    //   104: iload_3
    //   105: istore #4
    //   107: invokestatic isLoggable : ()Z
    //   110: ifeq -> 153
    //   113: iload_3
    //   114: istore #4
    //   116: new java/lang/StringBuilder
    //   119: astore #7
    //   121: iload_3
    //   122: istore #4
    //   124: aload #7
    //   126: ldc 'MimetypesFileTypeMap: URL '
    //   128: invokespecial <init> : (Ljava/lang/String;)V
    //   131: iload_3
    //   132: istore #4
    //   134: aload #7
    //   136: aload #12
    //   138: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: iload_3
    //   143: istore #4
    //   145: aload #7
    //   147: invokevirtual toString : ()Ljava/lang/String;
    //   150: invokestatic log : (Ljava/lang/String;)V
    //   153: iconst_1
    //   154: istore #5
    //   156: iconst_1
    //   157: istore #4
    //   159: aload #12
    //   161: invokestatic openStream : (Ljava/net/URL;)Ljava/io/InputStream;
    //   164: astore #7
    //   166: aload #7
    //   168: ifnull -> 288
    //   171: aload #7
    //   173: astore #8
    //   175: aload #7
    //   177: astore #9
    //   179: aload #7
    //   181: astore #10
    //   183: new com/sun/activation/registries/MimeTypeFile
    //   186: astore #13
    //   188: aload #7
    //   190: astore #8
    //   192: aload #7
    //   194: astore #9
    //   196: aload #7
    //   198: astore #10
    //   200: aload #13
    //   202: aload #7
    //   204: invokespecial <init> : (Ljava/io/InputStream;)V
    //   207: aload #7
    //   209: astore #8
    //   211: aload #7
    //   213: astore #9
    //   215: aload #7
    //   217: astore #10
    //   219: aload_1
    //   220: aload #13
    //   222: invokevirtual addElement : (Ljava/lang/Object;)V
    //   225: invokestatic isLoggable : ()Z
    //   228: ifeq -> 259
    //   231: new java/lang/StringBuilder
    //   234: astore #8
    //   236: aload #8
    //   238: ldc 'MimetypesFileTypeMap: successfully loaded mime types from URL: '
    //   240: invokespecial <init> : (Ljava/lang/String;)V
    //   243: aload #8
    //   245: aload #12
    //   247: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   250: pop
    //   251: aload #8
    //   253: invokevirtual toString : ()Ljava/lang/String;
    //   256: invokestatic log : (Ljava/lang/String;)V
    //   259: iconst_1
    //   260: istore #5
    //   262: goto -> 388
    //   265: astore #9
    //   267: iconst_1
    //   268: istore_3
    //   269: goto -> 631
    //   272: astore #9
    //   274: iload #4
    //   276: istore_3
    //   277: goto -> 430
    //   280: astore #9
    //   282: iload #5
    //   284: istore_3
    //   285: goto -> 518
    //   288: iload_3
    //   289: istore #5
    //   291: aload #7
    //   293: astore #8
    //   295: aload #7
    //   297: astore #9
    //   299: aload #7
    //   301: astore #10
    //   303: invokestatic isLoggable : ()Z
    //   306: ifeq -> 388
    //   309: aload #7
    //   311: astore #8
    //   313: aload #7
    //   315: astore #9
    //   317: aload #7
    //   319: astore #10
    //   321: new java/lang/StringBuilder
    //   324: astore #13
    //   326: aload #7
    //   328: astore #8
    //   330: aload #7
    //   332: astore #9
    //   334: aload #7
    //   336: astore #10
    //   338: aload #13
    //   340: ldc 'MimetypesFileTypeMap: not loading mime types from URL: '
    //   342: invokespecial <init> : (Ljava/lang/String;)V
    //   345: aload #7
    //   347: astore #8
    //   349: aload #7
    //   351: astore #9
    //   353: aload #7
    //   355: astore #10
    //   357: aload #13
    //   359: aload #12
    //   361: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: aload #7
    //   367: astore #8
    //   369: aload #7
    //   371: astore #9
    //   373: aload #7
    //   375: astore #10
    //   377: aload #13
    //   379: invokevirtual toString : ()Ljava/lang/String;
    //   382: invokestatic log : (Ljava/lang/String;)V
    //   385: iload_3
    //   386: istore #5
    //   388: iload #5
    //   390: istore_3
    //   391: aload #7
    //   393: ifnull -> 616
    //   396: iload #5
    //   398: istore #4
    //   400: aload #7
    //   402: invokevirtual close : ()V
    //   405: iload #5
    //   407: istore_3
    //   408: goto -> 616
    //   411: astore #9
    //   413: aload #8
    //   415: astore #7
    //   417: goto -> 631
    //   420: astore #8
    //   422: aload #9
    //   424: astore #7
    //   426: aload #8
    //   428: astore #9
    //   430: aload #7
    //   432: astore #8
    //   434: iload_3
    //   435: istore #4
    //   437: invokestatic isLoggable : ()Z
    //   440: ifeq -> 501
    //   443: aload #7
    //   445: astore #8
    //   447: iload_3
    //   448: istore #4
    //   450: new java/lang/StringBuilder
    //   453: astore #10
    //   455: aload #7
    //   457: astore #8
    //   459: iload_3
    //   460: istore #4
    //   462: aload #10
    //   464: ldc 'MimetypesFileTypeMap: can't load '
    //   466: invokespecial <init> : (Ljava/lang/String;)V
    //   469: aload #7
    //   471: astore #8
    //   473: iload_3
    //   474: istore #4
    //   476: aload #10
    //   478: aload #12
    //   480: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   483: pop
    //   484: aload #7
    //   486: astore #8
    //   488: iload_3
    //   489: istore #4
    //   491: aload #10
    //   493: invokevirtual toString : ()Ljava/lang/String;
    //   496: aload #9
    //   498: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   501: iload_3
    //   502: istore #4
    //   504: aload #7
    //   506: ifnull -> 613
    //   509: goto -> 597
    //   512: astore #9
    //   514: aload #10
    //   516: astore #7
    //   518: aload #7
    //   520: astore #8
    //   522: iload_3
    //   523: istore #4
    //   525: invokestatic isLoggable : ()Z
    //   528: ifeq -> 589
    //   531: aload #7
    //   533: astore #8
    //   535: iload_3
    //   536: istore #4
    //   538: new java/lang/StringBuilder
    //   541: astore #10
    //   543: aload #7
    //   545: astore #8
    //   547: iload_3
    //   548: istore #4
    //   550: aload #10
    //   552: ldc 'MimetypesFileTypeMap: can't load '
    //   554: invokespecial <init> : (Ljava/lang/String;)V
    //   557: aload #7
    //   559: astore #8
    //   561: iload_3
    //   562: istore #4
    //   564: aload #10
    //   566: aload #12
    //   568: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload #7
    //   574: astore #8
    //   576: iload_3
    //   577: istore #4
    //   579: aload #10
    //   581: invokevirtual toString : ()Ljava/lang/String;
    //   584: aload #9
    //   586: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   589: iload_3
    //   590: istore #4
    //   592: aload #7
    //   594: ifnull -> 613
    //   597: aload #7
    //   599: invokevirtual close : ()V
    //   602: iload_3
    //   603: istore #4
    //   605: goto -> 613
    //   608: astore #7
    //   610: goto -> 660
    //   613: iload #4
    //   615: istore_3
    //   616: iinc #6, 1
    //   619: goto -> 71
    //   622: astore #9
    //   624: iload #4
    //   626: istore_3
    //   627: aload #8
    //   629: astore #7
    //   631: aload #7
    //   633: ifnull -> 644
    //   636: iload_3
    //   637: istore #4
    //   639: aload #7
    //   641: invokevirtual close : ()V
    //   644: iload_3
    //   645: istore #4
    //   647: aload #9
    //   649: athrow
    //   650: astore #7
    //   652: iload #4
    //   654: istore_3
    //   655: goto -> 660
    //   658: astore #7
    //   660: iload_3
    //   661: istore #4
    //   663: invokestatic isLoggable : ()Z
    //   666: ifeq -> 700
    //   669: new java/lang/StringBuilder
    //   672: dup
    //   673: ldc 'MimetypesFileTypeMap: can't load '
    //   675: invokespecial <init> : (Ljava/lang/String;)V
    //   678: astore #8
    //   680: aload #8
    //   682: aload_2
    //   683: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   686: pop
    //   687: aload #8
    //   689: invokevirtual toString : ()Ljava/lang/String;
    //   692: aload #7
    //   694: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   697: iload_3
    //   698: istore #4
    //   700: iload #4
    //   702: ifne -> 747
    //   705: ldc 'MimetypesFileTypeMap: !anyLoaded'
    //   707: invokestatic log : (Ljava/lang/String;)V
    //   710: new java/lang/StringBuilder
    //   713: dup
    //   714: ldc '/'
    //   716: invokespecial <init> : (Ljava/lang/String;)V
    //   719: astore #7
    //   721: aload #7
    //   723: aload_2
    //   724: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   727: pop
    //   728: aload_0
    //   729: aload #7
    //   731: invokevirtual toString : ()Ljava/lang/String;
    //   734: invokespecial loadResource : (Ljava/lang/String;)Lcom/sun/activation/registries/MimeTypeFile;
    //   737: astore_2
    //   738: aload_2
    //   739: ifnull -> 747
    //   742: aload_1
    //   743: aload_2
    //   744: invokevirtual addElement : (Ljava/lang/Object;)V
    //   747: return
    //   748: astore #7
    //   750: iload #5
    //   752: istore_3
    //   753: goto -> 616
    //   756: astore #7
    //   758: iload_3
    //   759: istore #4
    //   761: goto -> 613
    //   764: astore #7
    //   766: goto -> 644
    // Exception table:
    //   from	to	target	type
    //   8	13	658	java/lang/Exception
    //   22	31	658	java/lang/Exception
    //   36	44	658	java/lang/Exception
    //   47	53	658	java/lang/Exception
    //   58	69	658	java/lang/Exception
    //   74	82	650	java/lang/Exception
    //   107	113	650	java/lang/Exception
    //   116	121	650	java/lang/Exception
    //   124	131	650	java/lang/Exception
    //   134	142	650	java/lang/Exception
    //   145	153	650	java/lang/Exception
    //   159	166	512	java/io/IOException
    //   159	166	420	java/lang/SecurityException
    //   159	166	411	finally
    //   183	188	512	java/io/IOException
    //   183	188	420	java/lang/SecurityException
    //   183	188	411	finally
    //   200	207	512	java/io/IOException
    //   200	207	420	java/lang/SecurityException
    //   200	207	411	finally
    //   219	225	512	java/io/IOException
    //   219	225	420	java/lang/SecurityException
    //   219	225	411	finally
    //   225	259	280	java/io/IOException
    //   225	259	272	java/lang/SecurityException
    //   225	259	265	finally
    //   303	309	512	java/io/IOException
    //   303	309	420	java/lang/SecurityException
    //   303	309	411	finally
    //   321	326	512	java/io/IOException
    //   321	326	420	java/lang/SecurityException
    //   321	326	411	finally
    //   338	345	512	java/io/IOException
    //   338	345	420	java/lang/SecurityException
    //   338	345	411	finally
    //   357	365	512	java/io/IOException
    //   357	365	420	java/lang/SecurityException
    //   357	365	411	finally
    //   377	385	512	java/io/IOException
    //   377	385	420	java/lang/SecurityException
    //   377	385	411	finally
    //   400	405	748	java/io/IOException
    //   400	405	650	java/lang/Exception
    //   437	443	622	finally
    //   450	455	622	finally
    //   462	469	622	finally
    //   476	484	622	finally
    //   491	501	622	finally
    //   525	531	622	finally
    //   538	543	622	finally
    //   550	557	622	finally
    //   564	572	622	finally
    //   579	589	622	finally
    //   597	602	756	java/io/IOException
    //   597	602	608	java/lang/Exception
    //   639	644	764	java/io/IOException
    //   639	644	650	java/lang/Exception
    //   647	650	650	java/lang/Exception
  }
  
  private MimeTypeFile loadFile(String paramString) {
    try {
      MimeTypeFile mimeTypeFile2 = new MimeTypeFile();
      this(paramString);
      MimeTypeFile mimeTypeFile1 = mimeTypeFile2;
    } catch (IOException iOException) {
      iOException = null;
    } 
    return (MimeTypeFile)iOException;
  }
  
  private MimeTypeFile loadResource(String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: invokevirtual getClass : ()Ljava/lang/Class;
    //   6: aload_1
    //   7: invokestatic getResourceAsStream : (Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;
    //   10: astore_3
    //   11: aload_3
    //   12: ifnull -> 84
    //   15: aload_3
    //   16: astore_2
    //   17: new com/sun/activation/registries/MimeTypeFile
    //   20: astore #4
    //   22: aload_3
    //   23: astore_2
    //   24: aload #4
    //   26: aload_3
    //   27: invokespecial <init> : (Ljava/io/InputStream;)V
    //   30: aload_3
    //   31: astore_2
    //   32: invokestatic isLoggable : ()Z
    //   35: ifeq -> 73
    //   38: aload_3
    //   39: astore_2
    //   40: new java/lang/StringBuilder
    //   43: astore #5
    //   45: aload_3
    //   46: astore_2
    //   47: aload #5
    //   49: ldc 'MimetypesFileTypeMap: successfully loaded mime types file: '
    //   51: invokespecial <init> : (Ljava/lang/String;)V
    //   54: aload_3
    //   55: astore_2
    //   56: aload #5
    //   58: aload_1
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_3
    //   64: astore_2
    //   65: aload #5
    //   67: invokevirtual toString : ()Ljava/lang/String;
    //   70: invokestatic log : (Ljava/lang/String;)V
    //   73: aload_3
    //   74: ifnull -> 81
    //   77: aload_3
    //   78: invokevirtual close : ()V
    //   81: aload #4
    //   83: areturn
    //   84: aload_3
    //   85: astore_2
    //   86: invokestatic isLoggable : ()Z
    //   89: ifeq -> 127
    //   92: aload_3
    //   93: astore_2
    //   94: new java/lang/StringBuilder
    //   97: astore #4
    //   99: aload_3
    //   100: astore_2
    //   101: aload #4
    //   103: ldc 'MimetypesFileTypeMap: not loading mime types file: '
    //   105: invokespecial <init> : (Ljava/lang/String;)V
    //   108: aload_3
    //   109: astore_2
    //   110: aload #4
    //   112: aload_1
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: aload_3
    //   118: astore_2
    //   119: aload #4
    //   121: invokevirtual toString : ()Ljava/lang/String;
    //   124: invokestatic log : (Ljava/lang/String;)V
    //   127: aload_3
    //   128: ifnull -> 264
    //   131: aload_3
    //   132: invokevirtual close : ()V
    //   135: goto -> 264
    //   138: astore #4
    //   140: goto -> 156
    //   143: astore #4
    //   145: goto -> 212
    //   148: astore_1
    //   149: goto -> 267
    //   152: astore #4
    //   154: aconst_null
    //   155: astore_3
    //   156: aload_3
    //   157: astore_2
    //   158: invokestatic isLoggable : ()Z
    //   161: ifeq -> 201
    //   164: aload_3
    //   165: astore_2
    //   166: new java/lang/StringBuilder
    //   169: astore #5
    //   171: aload_3
    //   172: astore_2
    //   173: aload #5
    //   175: ldc 'MimetypesFileTypeMap: can't load '
    //   177: invokespecial <init> : (Ljava/lang/String;)V
    //   180: aload_3
    //   181: astore_2
    //   182: aload #5
    //   184: aload_1
    //   185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: pop
    //   189: aload_3
    //   190: astore_2
    //   191: aload #5
    //   193: invokevirtual toString : ()Ljava/lang/String;
    //   196: aload #4
    //   198: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   201: aload_3
    //   202: ifnull -> 264
    //   205: goto -> 131
    //   208: astore #4
    //   210: aconst_null
    //   211: astore_3
    //   212: aload_3
    //   213: astore_2
    //   214: invokestatic isLoggable : ()Z
    //   217: ifeq -> 257
    //   220: aload_3
    //   221: astore_2
    //   222: new java/lang/StringBuilder
    //   225: astore #5
    //   227: aload_3
    //   228: astore_2
    //   229: aload #5
    //   231: ldc 'MimetypesFileTypeMap: can't load '
    //   233: invokespecial <init> : (Ljava/lang/String;)V
    //   236: aload_3
    //   237: astore_2
    //   238: aload #5
    //   240: aload_1
    //   241: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload_3
    //   246: astore_2
    //   247: aload #5
    //   249: invokevirtual toString : ()Ljava/lang/String;
    //   252: aload #4
    //   254: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   257: aload_3
    //   258: ifnull -> 264
    //   261: goto -> 131
    //   264: aconst_null
    //   265: areturn
    //   266: astore_1
    //   267: aload_2
    //   268: ifnull -> 275
    //   271: aload_2
    //   272: invokevirtual close : ()V
    //   275: aload_1
    //   276: athrow
    //   277: astore_1
    //   278: goto -> 81
    //   281: astore_1
    //   282: goto -> 264
    //   285: astore_2
    //   286: goto -> 275
    // Exception table:
    //   from	to	target	type
    //   2	11	208	java/io/IOException
    //   2	11	152	java/lang/SecurityException
    //   2	11	148	finally
    //   17	22	143	java/io/IOException
    //   17	22	138	java/lang/SecurityException
    //   17	22	266	finally
    //   24	30	143	java/io/IOException
    //   24	30	138	java/lang/SecurityException
    //   24	30	266	finally
    //   32	38	143	java/io/IOException
    //   32	38	138	java/lang/SecurityException
    //   32	38	266	finally
    //   40	45	143	java/io/IOException
    //   40	45	138	java/lang/SecurityException
    //   40	45	266	finally
    //   47	54	143	java/io/IOException
    //   47	54	138	java/lang/SecurityException
    //   47	54	266	finally
    //   56	63	143	java/io/IOException
    //   56	63	138	java/lang/SecurityException
    //   56	63	266	finally
    //   65	73	143	java/io/IOException
    //   65	73	138	java/lang/SecurityException
    //   65	73	266	finally
    //   77	81	277	java/io/IOException
    //   86	92	143	java/io/IOException
    //   86	92	138	java/lang/SecurityException
    //   86	92	266	finally
    //   94	99	143	java/io/IOException
    //   94	99	138	java/lang/SecurityException
    //   94	99	266	finally
    //   101	108	143	java/io/IOException
    //   101	108	138	java/lang/SecurityException
    //   101	108	266	finally
    //   110	117	143	java/io/IOException
    //   110	117	138	java/lang/SecurityException
    //   110	117	266	finally
    //   119	127	143	java/io/IOException
    //   119	127	138	java/lang/SecurityException
    //   119	127	266	finally
    //   131	135	281	java/io/IOException
    //   158	164	266	finally
    //   166	171	266	finally
    //   173	180	266	finally
    //   182	189	266	finally
    //   191	201	266	finally
    //   214	220	266	finally
    //   222	227	266	finally
    //   229	236	266	finally
    //   238	245	266	finally
    //   247	257	266	finally
    //   271	275	285	java/io/IOException
  }
  
  public void addMimeTypes(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield DB : [Lcom/sun/activation/registries/MimeTypeFile;
    //   6: astore_2
    //   7: aload_2
    //   8: iconst_0
    //   9: aaload
    //   10: ifnonnull -> 25
    //   13: new com/sun/activation/registries/MimeTypeFile
    //   16: astore_3
    //   17: aload_3
    //   18: invokespecial <init> : ()V
    //   21: aload_2
    //   22: iconst_0
    //   23: aload_3
    //   24: aastore
    //   25: aload_0
    //   26: getfield DB : [Lcom/sun/activation/registries/MimeTypeFile;
    //   29: iconst_0
    //   30: aaload
    //   31: aload_1
    //   32: invokevirtual appendToRegistry : (Ljava/lang/String;)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	38	finally
    //   13	21	38	finally
    //   25	35	38	finally
  }
  
  public String getContentType(File paramFile) {
    return getContentType(paramFile.getName());
  }
  
  public String getContentType(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ldc '.'
    //   5: invokevirtual lastIndexOf : (Ljava/lang/String;)I
    //   8: istore_2
    //   9: iload_2
    //   10: ifge -> 21
    //   13: getstatic javax/activation/MimetypesFileTypeMap.defaultType : Ljava/lang/String;
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: areturn
    //   21: aload_1
    //   22: iload_2
    //   23: iconst_1
    //   24: iadd
    //   25: invokevirtual substring : (I)Ljava/lang/String;
    //   28: astore_1
    //   29: aload_1
    //   30: invokevirtual length : ()I
    //   33: ifne -> 44
    //   36: getstatic javax/activation/MimetypesFileTypeMap.defaultType : Ljava/lang/String;
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: areturn
    //   44: iconst_0
    //   45: istore_2
    //   46: aload_0
    //   47: getfield DB : [Lcom/sun/activation/registries/MimeTypeFile;
    //   50: astore_3
    //   51: iload_2
    //   52: aload_3
    //   53: arraylength
    //   54: if_icmplt -> 65
    //   57: getstatic javax/activation/MimetypesFileTypeMap.defaultType : Ljava/lang/String;
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: areturn
    //   65: aload_3
    //   66: iload_2
    //   67: aaload
    //   68: ifnonnull -> 74
    //   71: goto -> 90
    //   74: aload_3
    //   75: iload_2
    //   76: aaload
    //   77: aload_1
    //   78: invokevirtual getMIMETypeString : (Ljava/lang/String;)Ljava/lang/String;
    //   81: astore_3
    //   82: aload_3
    //   83: ifnull -> 90
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_3
    //   89: areturn
    //   90: iinc #2, 1
    //   93: goto -> 46
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	96	finally
    //   13	17	96	finally
    //   21	40	96	finally
    //   46	61	96	finally
    //   74	82	96	finally
  }
}
