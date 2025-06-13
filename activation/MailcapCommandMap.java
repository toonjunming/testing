package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MailcapCommandMap extends CommandMap {
  private static final int PROG = 0;
  
  private static MailcapFile defDB;
  
  private MailcapFile[] DB;
  
  public MailcapCommandMap() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: new java/util/ArrayList
    //   7: dup
    //   8: iconst_5
    //   9: invokespecial <init> : (I)V
    //   12: astore_1
    //   13: aload_1
    //   14: aconst_null
    //   15: invokeinterface add : (Ljava/lang/Object;)Z
    //   20: pop
    //   21: ldc 'MailcapCommandMap: load HOME'
    //   23: invokestatic log : (Ljava/lang/String;)V
    //   26: ldc 'user.home'
    //   28: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   31: astore_3
    //   32: aload_3
    //   33: ifnull -> 84
    //   36: new java/lang/StringBuilder
    //   39: astore_2
    //   40: aload_2
    //   41: aload_3
    //   42: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   45: invokespecial <init> : (Ljava/lang/String;)V
    //   48: aload_2
    //   49: getstatic java/io/File.separator : Ljava/lang/String;
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: pop
    //   56: aload_2
    //   57: ldc '.mailcap'
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_0
    //   64: aload_2
    //   65: invokevirtual toString : ()Ljava/lang/String;
    //   68: invokespecial loadFile : (Ljava/lang/String;)Lcom/sun/activation/registries/MailcapFile;
    //   71: astore_2
    //   72: aload_2
    //   73: ifnull -> 84
    //   76: aload_1
    //   77: aload_2
    //   78: invokeinterface add : (Ljava/lang/Object;)Z
    //   83: pop
    //   84: ldc 'MailcapCommandMap: load SYS'
    //   86: invokestatic log : (Ljava/lang/String;)V
    //   89: new java/lang/StringBuilder
    //   92: astore_3
    //   93: aload_3
    //   94: ldc 'java.home'
    //   96: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   99: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   102: invokespecial <init> : (Ljava/lang/String;)V
    //   105: getstatic java/io/File.separator : Ljava/lang/String;
    //   108: astore_2
    //   109: aload_3
    //   110: aload_2
    //   111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: pop
    //   115: aload_3
    //   116: ldc 'lib'
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload_3
    //   123: aload_2
    //   124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: aload_3
    //   129: ldc 'mailcap'
    //   131: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: pop
    //   135: aload_0
    //   136: aload_3
    //   137: invokevirtual toString : ()Ljava/lang/String;
    //   140: invokespecial loadFile : (Ljava/lang/String;)Lcom/sun/activation/registries/MailcapFile;
    //   143: astore_2
    //   144: aload_2
    //   145: ifnull -> 156
    //   148: aload_1
    //   149: aload_2
    //   150: invokeinterface add : (Ljava/lang/Object;)Z
    //   155: pop
    //   156: ldc 'MailcapCommandMap: load JAR'
    //   158: invokestatic log : (Ljava/lang/String;)V
    //   161: aload_0
    //   162: aload_1
    //   163: ldc 'mailcap'
    //   165: invokespecial loadAllResources : (Ljava/util/List;Ljava/lang/String;)V
    //   168: ldc 'MailcapCommandMap: load DEF'
    //   170: invokestatic log : (Ljava/lang/String;)V
    //   173: ldc javax/activation/MailcapCommandMap
    //   175: monitorenter
    //   176: getstatic javax/activation/MailcapCommandMap.defDB : Lcom/sun/activation/registries/MailcapFile;
    //   179: ifnonnull -> 191
    //   182: aload_0
    //   183: ldc 'mailcap.default'
    //   185: invokespecial loadResource : (Ljava/lang/String;)Lcom/sun/activation/registries/MailcapFile;
    //   188: putstatic javax/activation/MailcapCommandMap.defDB : Lcom/sun/activation/registries/MailcapFile;
    //   191: ldc javax/activation/MailcapCommandMap
    //   193: monitorexit
    //   194: getstatic javax/activation/MailcapCommandMap.defDB : Lcom/sun/activation/registries/MailcapFile;
    //   197: astore_2
    //   198: aload_2
    //   199: ifnull -> 210
    //   202: aload_1
    //   203: aload_2
    //   204: invokeinterface add : (Ljava/lang/Object;)Z
    //   209: pop
    //   210: aload_1
    //   211: invokeinterface size : ()I
    //   216: anewarray com/sun/activation/registries/MailcapFile
    //   219: astore_2
    //   220: aload_0
    //   221: aload_2
    //   222: putfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   225: aload_0
    //   226: aload_1
    //   227: aload_2
    //   228: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   233: checkcast [Lcom/sun/activation/registries/MailcapFile;
    //   236: putfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   239: return
    //   240: astore_1
    //   241: ldc javax/activation/MailcapCommandMap
    //   243: monitorexit
    //   244: aload_1
    //   245: athrow
    //   246: astore_2
    //   247: goto -> 84
    //   250: astore_2
    //   251: goto -> 156
    // Exception table:
    //   from	to	target	type
    //   26	32	246	java/lang/SecurityException
    //   36	72	246	java/lang/SecurityException
    //   76	84	246	java/lang/SecurityException
    //   89	144	250	java/lang/SecurityException
    //   148	156	250	java/lang/SecurityException
    //   176	191	240	finally
    //   191	194	240	finally
    //   241	244	240	finally
  }
  
  public MailcapCommandMap(InputStream paramInputStream) {
    this();
    LogSupport.log("MailcapCommandMap: load PROG");
    MailcapFile[] arrayOfMailcapFile = this.DB;
    if (arrayOfMailcapFile[0] == null)
      try {
        MailcapFile mailcapFile = new MailcapFile();
        this(paramInputStream);
        arrayOfMailcapFile[0] = mailcapFile;
      } catch (IOException iOException) {} 
  }
  
  public MailcapCommandMap(String paramString) throws IOException {
    this();
    if (LogSupport.isLoggable()) {
      StringBuilder stringBuilder = new StringBuilder("MailcapCommandMap: load PROG from ");
      stringBuilder.append(paramString);
      LogSupport.log(stringBuilder.toString());
    } 
    MailcapFile[] arrayOfMailcapFile = this.DB;
    if (arrayOfMailcapFile[0] == null)
      arrayOfMailcapFile[0] = new MailcapFile(paramString); 
  }
  
  private void appendCmdsToList(Map paramMap, List<CommandInfo> paramList) {
    Iterator<String> iterator = paramMap.keySet().iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      String str = iterator.next();
      Iterator<String> iterator1 = ((List)paramMap.get(str)).iterator();
      while (iterator1.hasNext())
        paramList.add(new CommandInfo(str, iterator1.next())); 
    } 
  }
  
  private void appendPrefCmdsToList(Map paramMap, List<CommandInfo> paramList) {
    Iterator<String> iterator = paramMap.keySet().iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      String str = iterator.next();
      if (!checkForVerb(paramList, str))
        paramList.add(new CommandInfo(str, ((List<String>)paramMap.get(str)).get(0))); 
    } 
  }
  
  private boolean checkForVerb(List paramList, String paramString) {
    Iterator<CommandInfo> iterator = paramList.iterator();
    while (true) {
      if (!iterator.hasNext())
        return false; 
      if (((CommandInfo)iterator.next()).getCommandName().equals(paramString))
        return true; 
    } 
  }
  
  private DataContentHandler getDataContentHandler(String paramString) {
    if (LogSupport.isLoggable())
      LogSupport.log("    got content-handler"); 
    if (LogSupport.isLoggable()) {
      StringBuilder stringBuilder = new StringBuilder("      class ");
      stringBuilder.append(paramString);
      LogSupport.log(stringBuilder.toString());
    } 
    try {
      Class<?> clazz;
      ClassLoader classLoader2 = SecuritySupport.getContextClassLoader();
      ClassLoader classLoader1 = classLoader2;
      if (classLoader2 == null)
        classLoader1 = getClass().getClassLoader(); 
      try {
        clazz = classLoader1.loadClass(paramString);
      } catch (Exception exception) {
        clazz = Class.forName(paramString);
      } 
      if (clazz != null)
        return (DataContentHandler)clazz.newInstance(); 
    } catch (IllegalAccessException illegalAccessException) {
      if (LogSupport.isLoggable()) {
        StringBuilder stringBuilder = new StringBuilder("Can't load DCH ");
        stringBuilder.append(paramString);
        LogSupport.log(stringBuilder.toString(), illegalAccessException);
      } 
    } catch (ClassNotFoundException classNotFoundException) {
      if (LogSupport.isLoggable()) {
        StringBuilder stringBuilder = new StringBuilder("Can't load DCH ");
        stringBuilder.append(paramString);
        LogSupport.log(stringBuilder.toString(), classNotFoundException);
      } 
    } catch (InstantiationException instantiationException) {
      if (LogSupport.isLoggable()) {
        StringBuilder stringBuilder = new StringBuilder("Can't load DCH ");
        stringBuilder.append(paramString);
        LogSupport.log(stringBuilder.toString(), instantiationException);
      } 
    } 
    return null;
  }
  
  private void loadAllResources(List paramList, String paramString) {
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
    //   55: ifnull -> 703
    //   58: invokestatic isLoggable : ()Z
    //   61: ifeq -> 69
    //   64: ldc 'MailcapCommandMap: getResources'
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
    //   85: goto -> 703
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
    //   126: ldc 'MailcapCommandMap: URL '
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
    //   154: istore #4
    //   156: iconst_1
    //   157: istore #5
    //   159: aload #12
    //   161: invokestatic openStream : (Ljava/net/URL;)Ljava/io/InputStream;
    //   164: astore #7
    //   166: aload #7
    //   168: ifnull -> 291
    //   171: aload #7
    //   173: astore #8
    //   175: aload #7
    //   177: astore #9
    //   179: aload #7
    //   181: astore #10
    //   183: new com/sun/activation/registries/MailcapFile
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
    //   222: invokeinterface add : (Ljava/lang/Object;)Z
    //   227: pop
    //   228: invokestatic isLoggable : ()Z
    //   231: ifeq -> 262
    //   234: new java/lang/StringBuilder
    //   237: astore #8
    //   239: aload #8
    //   241: ldc 'MailcapCommandMap: successfully loaded mailcap file from URL: '
    //   243: invokespecial <init> : (Ljava/lang/String;)V
    //   246: aload #8
    //   248: aload #12
    //   250: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload #8
    //   256: invokevirtual toString : ()Ljava/lang/String;
    //   259: invokestatic log : (Ljava/lang/String;)V
    //   262: iconst_1
    //   263: istore #5
    //   265: goto -> 391
    //   268: astore #9
    //   270: iconst_1
    //   271: istore_3
    //   272: goto -> 634
    //   275: astore #9
    //   277: iload #5
    //   279: istore_3
    //   280: goto -> 433
    //   283: astore #9
    //   285: iload #4
    //   287: istore_3
    //   288: goto -> 521
    //   291: iload_3
    //   292: istore #5
    //   294: aload #7
    //   296: astore #8
    //   298: aload #7
    //   300: astore #9
    //   302: aload #7
    //   304: astore #10
    //   306: invokestatic isLoggable : ()Z
    //   309: ifeq -> 391
    //   312: aload #7
    //   314: astore #8
    //   316: aload #7
    //   318: astore #9
    //   320: aload #7
    //   322: astore #10
    //   324: new java/lang/StringBuilder
    //   327: astore #13
    //   329: aload #7
    //   331: astore #8
    //   333: aload #7
    //   335: astore #9
    //   337: aload #7
    //   339: astore #10
    //   341: aload #13
    //   343: ldc 'MailcapCommandMap: not loading mailcap file from URL: '
    //   345: invokespecial <init> : (Ljava/lang/String;)V
    //   348: aload #7
    //   350: astore #8
    //   352: aload #7
    //   354: astore #9
    //   356: aload #7
    //   358: astore #10
    //   360: aload #13
    //   362: aload #12
    //   364: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   367: pop
    //   368: aload #7
    //   370: astore #8
    //   372: aload #7
    //   374: astore #9
    //   376: aload #7
    //   378: astore #10
    //   380: aload #13
    //   382: invokevirtual toString : ()Ljava/lang/String;
    //   385: invokestatic log : (Ljava/lang/String;)V
    //   388: iload_3
    //   389: istore #5
    //   391: iload #5
    //   393: istore_3
    //   394: aload #7
    //   396: ifnull -> 619
    //   399: iload #5
    //   401: istore #4
    //   403: aload #7
    //   405: invokevirtual close : ()V
    //   408: iload #5
    //   410: istore_3
    //   411: goto -> 619
    //   414: astore #9
    //   416: aload #8
    //   418: astore #7
    //   420: goto -> 634
    //   423: astore #8
    //   425: aload #9
    //   427: astore #7
    //   429: aload #8
    //   431: astore #9
    //   433: aload #7
    //   435: astore #8
    //   437: iload_3
    //   438: istore #4
    //   440: invokestatic isLoggable : ()Z
    //   443: ifeq -> 504
    //   446: aload #7
    //   448: astore #8
    //   450: iload_3
    //   451: istore #4
    //   453: new java/lang/StringBuilder
    //   456: astore #10
    //   458: aload #7
    //   460: astore #8
    //   462: iload_3
    //   463: istore #4
    //   465: aload #10
    //   467: ldc 'MailcapCommandMap: can't load '
    //   469: invokespecial <init> : (Ljava/lang/String;)V
    //   472: aload #7
    //   474: astore #8
    //   476: iload_3
    //   477: istore #4
    //   479: aload #10
    //   481: aload #12
    //   483: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   486: pop
    //   487: aload #7
    //   489: astore #8
    //   491: iload_3
    //   492: istore #4
    //   494: aload #10
    //   496: invokevirtual toString : ()Ljava/lang/String;
    //   499: aload #9
    //   501: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   504: iload_3
    //   505: istore #4
    //   507: aload #7
    //   509: ifnull -> 616
    //   512: goto -> 600
    //   515: astore #9
    //   517: aload #10
    //   519: astore #7
    //   521: aload #7
    //   523: astore #8
    //   525: iload_3
    //   526: istore #4
    //   528: invokestatic isLoggable : ()Z
    //   531: ifeq -> 592
    //   534: aload #7
    //   536: astore #8
    //   538: iload_3
    //   539: istore #4
    //   541: new java/lang/StringBuilder
    //   544: astore #10
    //   546: aload #7
    //   548: astore #8
    //   550: iload_3
    //   551: istore #4
    //   553: aload #10
    //   555: ldc 'MailcapCommandMap: can't load '
    //   557: invokespecial <init> : (Ljava/lang/String;)V
    //   560: aload #7
    //   562: astore #8
    //   564: iload_3
    //   565: istore #4
    //   567: aload #10
    //   569: aload #12
    //   571: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   574: pop
    //   575: aload #7
    //   577: astore #8
    //   579: iload_3
    //   580: istore #4
    //   582: aload #10
    //   584: invokevirtual toString : ()Ljava/lang/String;
    //   587: aload #9
    //   589: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   592: iload_3
    //   593: istore #4
    //   595: aload #7
    //   597: ifnull -> 616
    //   600: aload #7
    //   602: invokevirtual close : ()V
    //   605: iload_3
    //   606: istore #4
    //   608: goto -> 616
    //   611: astore #7
    //   613: goto -> 663
    //   616: iload #4
    //   618: istore_3
    //   619: iinc #6, 1
    //   622: goto -> 71
    //   625: astore #9
    //   627: iload #4
    //   629: istore_3
    //   630: aload #8
    //   632: astore #7
    //   634: aload #7
    //   636: ifnull -> 647
    //   639: iload_3
    //   640: istore #4
    //   642: aload #7
    //   644: invokevirtual close : ()V
    //   647: iload_3
    //   648: istore #4
    //   650: aload #9
    //   652: athrow
    //   653: astore #7
    //   655: iload #4
    //   657: istore_3
    //   658: goto -> 663
    //   661: astore #7
    //   663: iload_3
    //   664: istore #4
    //   666: invokestatic isLoggable : ()Z
    //   669: ifeq -> 703
    //   672: new java/lang/StringBuilder
    //   675: dup
    //   676: ldc 'MailcapCommandMap: can't load '
    //   678: invokespecial <init> : (Ljava/lang/String;)V
    //   681: astore #8
    //   683: aload #8
    //   685: aload_2
    //   686: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   689: pop
    //   690: aload #8
    //   692: invokevirtual toString : ()Ljava/lang/String;
    //   695: aload #7
    //   697: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   700: iload_3
    //   701: istore #4
    //   703: iload #4
    //   705: ifne -> 761
    //   708: invokestatic isLoggable : ()Z
    //   711: ifeq -> 720
    //   714: ldc_w 'MailcapCommandMap: !anyLoaded'
    //   717: invokestatic log : (Ljava/lang/String;)V
    //   720: new java/lang/StringBuilder
    //   723: dup
    //   724: ldc_w '/'
    //   727: invokespecial <init> : (Ljava/lang/String;)V
    //   730: astore #7
    //   732: aload #7
    //   734: aload_2
    //   735: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   738: pop
    //   739: aload_0
    //   740: aload #7
    //   742: invokevirtual toString : ()Ljava/lang/String;
    //   745: invokespecial loadResource : (Ljava/lang/String;)Lcom/sun/activation/registries/MailcapFile;
    //   748: astore_2
    //   749: aload_2
    //   750: ifnull -> 761
    //   753: aload_1
    //   754: aload_2
    //   755: invokeinterface add : (Ljava/lang/Object;)Z
    //   760: pop
    //   761: return
    //   762: astore #7
    //   764: iload #5
    //   766: istore_3
    //   767: goto -> 619
    //   770: astore #7
    //   772: iload_3
    //   773: istore #4
    //   775: goto -> 616
    //   778: astore #7
    //   780: goto -> 647
    // Exception table:
    //   from	to	target	type
    //   8	13	661	java/lang/Exception
    //   22	31	661	java/lang/Exception
    //   36	44	661	java/lang/Exception
    //   47	53	661	java/lang/Exception
    //   58	69	661	java/lang/Exception
    //   74	82	653	java/lang/Exception
    //   107	113	653	java/lang/Exception
    //   116	121	653	java/lang/Exception
    //   124	131	653	java/lang/Exception
    //   134	142	653	java/lang/Exception
    //   145	153	653	java/lang/Exception
    //   159	166	515	java/io/IOException
    //   159	166	423	java/lang/SecurityException
    //   159	166	414	finally
    //   183	188	515	java/io/IOException
    //   183	188	423	java/lang/SecurityException
    //   183	188	414	finally
    //   200	207	515	java/io/IOException
    //   200	207	423	java/lang/SecurityException
    //   200	207	414	finally
    //   219	228	515	java/io/IOException
    //   219	228	423	java/lang/SecurityException
    //   219	228	414	finally
    //   228	262	283	java/io/IOException
    //   228	262	275	java/lang/SecurityException
    //   228	262	268	finally
    //   306	312	515	java/io/IOException
    //   306	312	423	java/lang/SecurityException
    //   306	312	414	finally
    //   324	329	515	java/io/IOException
    //   324	329	423	java/lang/SecurityException
    //   324	329	414	finally
    //   341	348	515	java/io/IOException
    //   341	348	423	java/lang/SecurityException
    //   341	348	414	finally
    //   360	368	515	java/io/IOException
    //   360	368	423	java/lang/SecurityException
    //   360	368	414	finally
    //   380	388	515	java/io/IOException
    //   380	388	423	java/lang/SecurityException
    //   380	388	414	finally
    //   403	408	762	java/io/IOException
    //   403	408	653	java/lang/Exception
    //   440	446	625	finally
    //   453	458	625	finally
    //   465	472	625	finally
    //   479	487	625	finally
    //   494	504	625	finally
    //   528	534	625	finally
    //   541	546	625	finally
    //   553	560	625	finally
    //   567	575	625	finally
    //   582	592	625	finally
    //   600	605	770	java/io/IOException
    //   600	605	611	java/lang/Exception
    //   642	647	778	java/io/IOException
    //   642	647	653	java/lang/Exception
    //   650	653	653	java/lang/Exception
  }
  
  private MailcapFile loadFile(String paramString) {
    try {
      MailcapFile mailcapFile2 = new MailcapFile();
      this(paramString);
      MailcapFile mailcapFile1 = mailcapFile2;
    } catch (IOException iOException) {
      iOException = null;
    } 
    return (MailcapFile)iOException;
  }
  
  private MailcapFile loadResource(String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: invokevirtual getClass : ()Ljava/lang/Class;
    //   6: aload_1
    //   7: invokestatic getResourceAsStream : (Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;
    //   10: astore_3
    //   11: aload_3
    //   12: ifnull -> 85
    //   15: aload_3
    //   16: astore_2
    //   17: new com/sun/activation/registries/MailcapFile
    //   20: astore #4
    //   22: aload_3
    //   23: astore_2
    //   24: aload #4
    //   26: aload_3
    //   27: invokespecial <init> : (Ljava/io/InputStream;)V
    //   30: aload_3
    //   31: astore_2
    //   32: invokestatic isLoggable : ()Z
    //   35: ifeq -> 74
    //   38: aload_3
    //   39: astore_2
    //   40: new java/lang/StringBuilder
    //   43: astore #5
    //   45: aload_3
    //   46: astore_2
    //   47: aload #5
    //   49: ldc_w 'MailcapCommandMap: successfully loaded mailcap file: '
    //   52: invokespecial <init> : (Ljava/lang/String;)V
    //   55: aload_3
    //   56: astore_2
    //   57: aload #5
    //   59: aload_1
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload_3
    //   65: astore_2
    //   66: aload #5
    //   68: invokevirtual toString : ()Ljava/lang/String;
    //   71: invokestatic log : (Ljava/lang/String;)V
    //   74: aload_3
    //   75: ifnull -> 82
    //   78: aload_3
    //   79: invokevirtual close : ()V
    //   82: aload #4
    //   84: areturn
    //   85: aload_3
    //   86: astore_2
    //   87: invokestatic isLoggable : ()Z
    //   90: ifeq -> 129
    //   93: aload_3
    //   94: astore_2
    //   95: new java/lang/StringBuilder
    //   98: astore #4
    //   100: aload_3
    //   101: astore_2
    //   102: aload #4
    //   104: ldc_w 'MailcapCommandMap: not loading mailcap file: '
    //   107: invokespecial <init> : (Ljava/lang/String;)V
    //   110: aload_3
    //   111: astore_2
    //   112: aload #4
    //   114: aload_1
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: aload_3
    //   120: astore_2
    //   121: aload #4
    //   123: invokevirtual toString : ()Ljava/lang/String;
    //   126: invokestatic log : (Ljava/lang/String;)V
    //   129: aload_3
    //   130: ifnull -> 266
    //   133: aload_3
    //   134: invokevirtual close : ()V
    //   137: goto -> 266
    //   140: astore #4
    //   142: goto -> 158
    //   145: astore #4
    //   147: goto -> 214
    //   150: astore_1
    //   151: goto -> 269
    //   154: astore #4
    //   156: aconst_null
    //   157: astore_3
    //   158: aload_3
    //   159: astore_2
    //   160: invokestatic isLoggable : ()Z
    //   163: ifeq -> 203
    //   166: aload_3
    //   167: astore_2
    //   168: new java/lang/StringBuilder
    //   171: astore #5
    //   173: aload_3
    //   174: astore_2
    //   175: aload #5
    //   177: ldc 'MailcapCommandMap: can't load '
    //   179: invokespecial <init> : (Ljava/lang/String;)V
    //   182: aload_3
    //   183: astore_2
    //   184: aload #5
    //   186: aload_1
    //   187: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: pop
    //   191: aload_3
    //   192: astore_2
    //   193: aload #5
    //   195: invokevirtual toString : ()Ljava/lang/String;
    //   198: aload #4
    //   200: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   203: aload_3
    //   204: ifnull -> 266
    //   207: goto -> 133
    //   210: astore #4
    //   212: aconst_null
    //   213: astore_3
    //   214: aload_3
    //   215: astore_2
    //   216: invokestatic isLoggable : ()Z
    //   219: ifeq -> 259
    //   222: aload_3
    //   223: astore_2
    //   224: new java/lang/StringBuilder
    //   227: astore #5
    //   229: aload_3
    //   230: astore_2
    //   231: aload #5
    //   233: ldc 'MailcapCommandMap: can't load '
    //   235: invokespecial <init> : (Ljava/lang/String;)V
    //   238: aload_3
    //   239: astore_2
    //   240: aload #5
    //   242: aload_1
    //   243: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   246: pop
    //   247: aload_3
    //   248: astore_2
    //   249: aload #5
    //   251: invokevirtual toString : ()Ljava/lang/String;
    //   254: aload #4
    //   256: invokestatic log : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   259: aload_3
    //   260: ifnull -> 266
    //   263: goto -> 133
    //   266: aconst_null
    //   267: areturn
    //   268: astore_1
    //   269: aload_2
    //   270: ifnull -> 277
    //   273: aload_2
    //   274: invokevirtual close : ()V
    //   277: aload_1
    //   278: athrow
    //   279: astore_1
    //   280: goto -> 82
    //   283: astore_1
    //   284: goto -> 266
    //   287: astore_2
    //   288: goto -> 277
    // Exception table:
    //   from	to	target	type
    //   2	11	210	java/io/IOException
    //   2	11	154	java/lang/SecurityException
    //   2	11	150	finally
    //   17	22	145	java/io/IOException
    //   17	22	140	java/lang/SecurityException
    //   17	22	268	finally
    //   24	30	145	java/io/IOException
    //   24	30	140	java/lang/SecurityException
    //   24	30	268	finally
    //   32	38	145	java/io/IOException
    //   32	38	140	java/lang/SecurityException
    //   32	38	268	finally
    //   40	45	145	java/io/IOException
    //   40	45	140	java/lang/SecurityException
    //   40	45	268	finally
    //   47	55	145	java/io/IOException
    //   47	55	140	java/lang/SecurityException
    //   47	55	268	finally
    //   57	64	145	java/io/IOException
    //   57	64	140	java/lang/SecurityException
    //   57	64	268	finally
    //   66	74	145	java/io/IOException
    //   66	74	140	java/lang/SecurityException
    //   66	74	268	finally
    //   78	82	279	java/io/IOException
    //   87	93	145	java/io/IOException
    //   87	93	140	java/lang/SecurityException
    //   87	93	268	finally
    //   95	100	145	java/io/IOException
    //   95	100	140	java/lang/SecurityException
    //   95	100	268	finally
    //   102	110	145	java/io/IOException
    //   102	110	140	java/lang/SecurityException
    //   102	110	268	finally
    //   112	119	145	java/io/IOException
    //   112	119	140	java/lang/SecurityException
    //   112	119	268	finally
    //   121	129	145	java/io/IOException
    //   121	129	140	java/lang/SecurityException
    //   121	129	268	finally
    //   133	137	283	java/io/IOException
    //   160	166	268	finally
    //   168	173	268	finally
    //   175	182	268	finally
    //   184	191	268	finally
    //   193	203	268	finally
    //   216	222	268	finally
    //   224	229	268	finally
    //   231	238	268	finally
    //   240	247	268	finally
    //   249	259	268	finally
    //   273	277	287	java/io/IOException
  }
  
  public void addMailcap(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc_w 'MailcapCommandMap: add to PROG'
    //   5: invokestatic log : (Ljava/lang/String;)V
    //   8: aload_0
    //   9: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   12: astore_2
    //   13: aload_2
    //   14: iconst_0
    //   15: aaload
    //   16: ifnonnull -> 31
    //   19: new com/sun/activation/registries/MailcapFile
    //   22: astore_3
    //   23: aload_3
    //   24: invokespecial <init> : ()V
    //   27: aload_2
    //   28: iconst_0
    //   29: aload_3
    //   30: aastore
    //   31: aload_0
    //   32: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   35: iconst_0
    //   36: aaload
    //   37: aload_1
    //   38: invokevirtual appendToMailcap : (Ljava/lang/String;)V
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	44	finally
    //   19	27	44	finally
    //   31	41	44	finally
  }
  
  public DataContentHandler createDataContentHandler(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic isLoggable : ()Z
    //   5: ifeq -> 36
    //   8: new java/lang/StringBuilder
    //   11: astore #4
    //   13: aload #4
    //   15: ldc_w 'MailcapCommandMap: createDataContentHandler for '
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: aload #4
    //   23: aload_1
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload #4
    //   30: invokevirtual toString : ()Ljava/lang/String;
    //   33: invokestatic log : (Ljava/lang/String;)V
    //   36: aload_1
    //   37: astore #4
    //   39: aload_1
    //   40: ifnull -> 52
    //   43: aload_1
    //   44: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   47: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   50: astore #4
    //   52: iconst_0
    //   53: istore_2
    //   54: aload_0
    //   55: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   58: astore_1
    //   59: iload_2
    //   60: aload_1
    //   61: arraylength
    //   62: if_icmplt -> 185
    //   65: iconst_0
    //   66: istore_2
    //   67: aload_0
    //   68: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   71: astore_1
    //   72: aload_1
    //   73: arraylength
    //   74: istore_3
    //   75: iload_2
    //   76: iload_3
    //   77: if_icmplt -> 84
    //   80: aload_0
    //   81: monitorexit
    //   82: aconst_null
    //   83: areturn
    //   84: aload_1
    //   85: iload_2
    //   86: aaload
    //   87: ifnonnull -> 93
    //   90: goto -> 179
    //   93: invokestatic isLoggable : ()Z
    //   96: ifeq -> 123
    //   99: new java/lang/StringBuilder
    //   102: astore_1
    //   103: aload_1
    //   104: ldc_w '  search fallback DB #'
    //   107: invokespecial <init> : (Ljava/lang/String;)V
    //   110: aload_1
    //   111: iload_2
    //   112: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload_1
    //   117: invokevirtual toString : ()Ljava/lang/String;
    //   120: invokestatic log : (Ljava/lang/String;)V
    //   123: aload_0
    //   124: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   127: iload_2
    //   128: aaload
    //   129: aload #4
    //   131: invokevirtual getMailcapFallbackList : (Ljava/lang/String;)Ljava/util/Map;
    //   134: astore_1
    //   135: aload_1
    //   136: ifnull -> 179
    //   139: aload_1
    //   140: ldc_w 'content-handler'
    //   143: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   148: checkcast java/util/List
    //   151: astore_1
    //   152: aload_1
    //   153: ifnull -> 179
    //   156: aload_0
    //   157: aload_1
    //   158: iconst_0
    //   159: invokeinterface get : (I)Ljava/lang/Object;
    //   164: checkcast java/lang/String
    //   167: invokespecial getDataContentHandler : (Ljava/lang/String;)Ljavax/activation/DataContentHandler;
    //   170: astore_1
    //   171: aload_1
    //   172: ifnull -> 179
    //   175: aload_0
    //   176: monitorexit
    //   177: aload_1
    //   178: areturn
    //   179: iinc #2, 1
    //   182: goto -> 67
    //   185: aload_1
    //   186: iload_2
    //   187: aaload
    //   188: ifnonnull -> 194
    //   191: goto -> 280
    //   194: invokestatic isLoggable : ()Z
    //   197: ifeq -> 224
    //   200: new java/lang/StringBuilder
    //   203: astore_1
    //   204: aload_1
    //   205: ldc_w '  search DB #'
    //   208: invokespecial <init> : (Ljava/lang/String;)V
    //   211: aload_1
    //   212: iload_2
    //   213: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload_1
    //   218: invokevirtual toString : ()Ljava/lang/String;
    //   221: invokestatic log : (Ljava/lang/String;)V
    //   224: aload_0
    //   225: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   228: iload_2
    //   229: aaload
    //   230: aload #4
    //   232: invokevirtual getMailcapList : (Ljava/lang/String;)Ljava/util/Map;
    //   235: astore_1
    //   236: aload_1
    //   237: ifnull -> 280
    //   240: aload_1
    //   241: ldc_w 'content-handler'
    //   244: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   249: checkcast java/util/List
    //   252: astore_1
    //   253: aload_1
    //   254: ifnull -> 280
    //   257: aload_0
    //   258: aload_1
    //   259: iconst_0
    //   260: invokeinterface get : (I)Ljava/lang/Object;
    //   265: checkcast java/lang/String
    //   268: invokespecial getDataContentHandler : (Ljava/lang/String;)Ljavax/activation/DataContentHandler;
    //   271: astore_1
    //   272: aload_1
    //   273: ifnull -> 280
    //   276: aload_0
    //   277: monitorexit
    //   278: aload_1
    //   279: areturn
    //   280: iinc #2, 1
    //   283: goto -> 54
    //   286: astore_1
    //   287: aload_0
    //   288: monitorexit
    //   289: aload_1
    //   290: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	286	finally
    //   43	52	286	finally
    //   54	65	286	finally
    //   67	75	286	finally
    //   93	123	286	finally
    //   123	135	286	finally
    //   139	152	286	finally
    //   156	171	286	finally
    //   194	224	286	finally
    //   224	236	286	finally
    //   240	253	286	finally
    //   257	272	286	finally
  }
  
  public CommandInfo[] getAllCommands(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #5
    //   7: aload #5
    //   9: invokespecial <init> : ()V
    //   12: aload_1
    //   13: astore #4
    //   15: aload_1
    //   16: ifnull -> 28
    //   19: aload_1
    //   20: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   23: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   26: astore #4
    //   28: iconst_0
    //   29: istore_3
    //   30: iconst_0
    //   31: istore_2
    //   32: aload_0
    //   33: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   36: astore_1
    //   37: iload_2
    //   38: aload_1
    //   39: arraylength
    //   40: if_icmplt -> 116
    //   43: iload_3
    //   44: istore_2
    //   45: aload_0
    //   46: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   49: astore_1
    //   50: iload_2
    //   51: aload_1
    //   52: arraylength
    //   53: if_icmplt -> 81
    //   56: aload #5
    //   58: aload #5
    //   60: invokeinterface size : ()I
    //   65: anewarray javax/activation/CommandInfo
    //   68: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   73: checkcast [Ljavax/activation/CommandInfo;
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: areturn
    //   81: aload_1
    //   82: iload_2
    //   83: aaload
    //   84: ifnonnull -> 90
    //   87: goto -> 110
    //   90: aload_1
    //   91: iload_2
    //   92: aaload
    //   93: aload #4
    //   95: invokevirtual getMailcapFallbackList : (Ljava/lang/String;)Ljava/util/Map;
    //   98: astore_1
    //   99: aload_1
    //   100: ifnull -> 110
    //   103: aload_0
    //   104: aload_1
    //   105: aload #5
    //   107: invokespecial appendCmdsToList : (Ljava/util/Map;Ljava/util/List;)V
    //   110: iinc #2, 1
    //   113: goto -> 45
    //   116: aload_1
    //   117: iload_2
    //   118: aaload
    //   119: ifnonnull -> 125
    //   122: goto -> 145
    //   125: aload_1
    //   126: iload_2
    //   127: aaload
    //   128: aload #4
    //   130: invokevirtual getMailcapList : (Ljava/lang/String;)Ljava/util/Map;
    //   133: astore_1
    //   134: aload_1
    //   135: ifnull -> 145
    //   138: aload_0
    //   139: aload_1
    //   140: aload #5
    //   142: invokespecial appendCmdsToList : (Ljava/util/Map;Ljava/util/List;)V
    //   145: iinc #2, 1
    //   148: goto -> 32
    //   151: astore_1
    //   152: aload_0
    //   153: monitorexit
    //   154: aload_1
    //   155: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	151	finally
    //   19	28	151	finally
    //   32	43	151	finally
    //   45	77	151	finally
    //   90	99	151	finally
    //   103	110	151	finally
    //   125	134	151	finally
    //   138	145	151	finally
  }
  
  public CommandInfo getCommand(String paramString1, String paramString2) {
    /* monitor enter ThisExpression{ObjectType{javax/activation/MailcapCommandMap}} */
    String str = paramString1;
    if (paramString1 != null)
      try {
        str = paramString1.toLowerCase(Locale.ENGLISH);
      } finally {} 
    for (byte b = 0;; b++) {
      CommandInfo commandInfo;
      MailcapFile[] arrayOfMailcapFile = this.DB;
      if (b >= arrayOfMailcapFile.length) {
        for (b = 0;; b++) {
          arrayOfMailcapFile = this.DB;
          int i = arrayOfMailcapFile.length;
          if (b >= i) {
            /* monitor exit ThisExpression{ObjectType{javax/activation/MailcapCommandMap}} */
            return null;
          } 
          if (arrayOfMailcapFile[b] != null) {
            Map map = arrayOfMailcapFile[b].getMailcapFallbackList(str);
            if (map != null) {
              List<String> list = (List)map.get(paramString2);
              if (list != null) {
                String str1 = list.get(0);
                if (str1 != null) {
                  commandInfo = new CommandInfo(paramString2, str1);
                  /* monitor exit ThisExpression{ObjectType{javax/activation/MailcapCommandMap}} */
                  return commandInfo;
                } 
              } 
            } 
          } 
        } 
        break;
      } 
      if (commandInfo[b] != null) {
        Map map = commandInfo[b].getMailcapList(str);
        if (map != null) {
          List<String> list = (List)map.get(paramString2);
          if (list != null) {
            String str1 = list.get(0);
            if (str1 != null) {
              CommandInfo commandInfo1 = new CommandInfo(paramString2, str1);
              /* monitor exit ThisExpression{ObjectType{javax/activation/MailcapCommandMap}} */
              return commandInfo1;
            } 
          } 
        } 
      } 
    } 
  }
  
  public String[] getMimeTypes() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: iconst_0
    //   11: istore_1
    //   12: aload_0
    //   13: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   16: astore #4
    //   18: iload_1
    //   19: aload #4
    //   21: arraylength
    //   22: if_icmplt -> 48
    //   25: aload_3
    //   26: aload_3
    //   27: invokeinterface size : ()I
    //   32: anewarray java/lang/String
    //   35: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   40: checkcast [Ljava/lang/String;
    //   43: astore_3
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_3
    //   47: areturn
    //   48: aload #4
    //   50: iload_1
    //   51: aaload
    //   52: ifnonnull -> 58
    //   55: goto -> 114
    //   58: aload #4
    //   60: iload_1
    //   61: aaload
    //   62: invokevirtual getMimeTypes : ()[Ljava/lang/String;
    //   65: astore #4
    //   67: aload #4
    //   69: ifnull -> 114
    //   72: iconst_0
    //   73: istore_2
    //   74: iload_2
    //   75: aload #4
    //   77: arraylength
    //   78: if_icmplt -> 84
    //   81: goto -> 114
    //   84: aload_3
    //   85: aload #4
    //   87: iload_2
    //   88: aaload
    //   89: invokeinterface contains : (Ljava/lang/Object;)Z
    //   94: ifne -> 108
    //   97: aload_3
    //   98: aload #4
    //   100: iload_2
    //   101: aaload
    //   102: invokeinterface add : (Ljava/lang/Object;)Z
    //   107: pop
    //   108: iinc #2, 1
    //   111: goto -> 74
    //   114: iinc #1, 1
    //   117: goto -> 12
    //   120: astore_3
    //   121: aload_0
    //   122: monitorexit
    //   123: aload_3
    //   124: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	120	finally
    //   12	44	120	finally
    //   58	67	120	finally
    //   74	81	120	finally
    //   84	108	120	finally
  }
  
  public String[] getNativeCommands(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #5
    //   7: aload #5
    //   9: invokespecial <init> : ()V
    //   12: aload_1
    //   13: astore #4
    //   15: aload_1
    //   16: ifnull -> 28
    //   19: aload_1
    //   20: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   23: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   26: astore #4
    //   28: iconst_0
    //   29: istore_2
    //   30: aload_0
    //   31: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   34: astore_1
    //   35: iload_2
    //   36: aload_1
    //   37: arraylength
    //   38: if_icmplt -> 66
    //   41: aload #5
    //   43: aload #5
    //   45: invokeinterface size : ()I
    //   50: anewarray java/lang/String
    //   53: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   58: checkcast [Ljava/lang/String;
    //   61: astore_1
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_1
    //   65: areturn
    //   66: aload_1
    //   67: iload_2
    //   68: aaload
    //   69: ifnonnull -> 75
    //   72: goto -> 129
    //   75: aload_1
    //   76: iload_2
    //   77: aaload
    //   78: aload #4
    //   80: invokevirtual getNativeCommands : (Ljava/lang/String;)[Ljava/lang/String;
    //   83: astore_1
    //   84: aload_1
    //   85: ifnull -> 129
    //   88: iconst_0
    //   89: istore_3
    //   90: iload_3
    //   91: aload_1
    //   92: arraylength
    //   93: if_icmplt -> 99
    //   96: goto -> 129
    //   99: aload #5
    //   101: aload_1
    //   102: iload_3
    //   103: aaload
    //   104: invokeinterface contains : (Ljava/lang/Object;)Z
    //   109: ifne -> 123
    //   112: aload #5
    //   114: aload_1
    //   115: iload_3
    //   116: aaload
    //   117: invokeinterface add : (Ljava/lang/Object;)Z
    //   122: pop
    //   123: iinc #3, 1
    //   126: goto -> 90
    //   129: iinc #2, 1
    //   132: goto -> 30
    //   135: astore_1
    //   136: aload_0
    //   137: monitorexit
    //   138: aload_1
    //   139: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	135	finally
    //   19	28	135	finally
    //   30	62	135	finally
    //   75	84	135	finally
    //   90	96	135	finally
    //   99	123	135	finally
  }
  
  public CommandInfo[] getPreferredCommands(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #5
    //   7: aload #5
    //   9: invokespecial <init> : ()V
    //   12: aload_1
    //   13: astore #4
    //   15: aload_1
    //   16: ifnull -> 28
    //   19: aload_1
    //   20: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   23: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   26: astore #4
    //   28: iconst_0
    //   29: istore_3
    //   30: iconst_0
    //   31: istore_2
    //   32: aload_0
    //   33: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   36: astore_1
    //   37: iload_2
    //   38: aload_1
    //   39: arraylength
    //   40: if_icmplt -> 116
    //   43: iload_3
    //   44: istore_2
    //   45: aload_0
    //   46: getfield DB : [Lcom/sun/activation/registries/MailcapFile;
    //   49: astore_1
    //   50: iload_2
    //   51: aload_1
    //   52: arraylength
    //   53: if_icmplt -> 81
    //   56: aload #5
    //   58: aload #5
    //   60: invokeinterface size : ()I
    //   65: anewarray javax/activation/CommandInfo
    //   68: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   73: checkcast [Ljavax/activation/CommandInfo;
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: areturn
    //   81: aload_1
    //   82: iload_2
    //   83: aaload
    //   84: ifnonnull -> 90
    //   87: goto -> 110
    //   90: aload_1
    //   91: iload_2
    //   92: aaload
    //   93: aload #4
    //   95: invokevirtual getMailcapFallbackList : (Ljava/lang/String;)Ljava/util/Map;
    //   98: astore_1
    //   99: aload_1
    //   100: ifnull -> 110
    //   103: aload_0
    //   104: aload_1
    //   105: aload #5
    //   107: invokespecial appendPrefCmdsToList : (Ljava/util/Map;Ljava/util/List;)V
    //   110: iinc #2, 1
    //   113: goto -> 45
    //   116: aload_1
    //   117: iload_2
    //   118: aaload
    //   119: ifnonnull -> 125
    //   122: goto -> 145
    //   125: aload_1
    //   126: iload_2
    //   127: aaload
    //   128: aload #4
    //   130: invokevirtual getMailcapList : (Ljava/lang/String;)Ljava/util/Map;
    //   133: astore_1
    //   134: aload_1
    //   135: ifnull -> 145
    //   138: aload_0
    //   139: aload_1
    //   140: aload #5
    //   142: invokespecial appendPrefCmdsToList : (Ljava/util/Map;Ljava/util/List;)V
    //   145: iinc #2, 1
    //   148: goto -> 32
    //   151: astore_1
    //   152: aload_0
    //   153: monitorexit
    //   154: aload_1
    //   155: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	151	finally
    //   19	28	151	finally
    //   32	43	151	finally
    //   45	77	151	finally
    //   90	99	151	finally
    //   103	110	151	finally
    //   125	134	151	finally
    //   138	145	151	finally
  }
}
