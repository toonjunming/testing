package com.mysql.jdbc;

public class EscapeTokenizer {
  private static final char CHR_BEGIN_TOKEN = '{';
  
  private static final char CHR_COMMENT = '-';
  
  private static final char CHR_CR = '\r';
  
  private static final char CHR_DBL_QUOTE = '"';
  
  private static final char CHR_END_TOKEN = '}';
  
  private static final char CHR_ESCAPE = '\\';
  
  private static final char CHR_LF = '\n';
  
  private static final char CHR_SGL_QUOTE = '\'';
  
  private static final char CHR_VARIABLE = '@';
  
  private int bracesLevel = 0;
  
  private boolean emittingEscapeCode = false;
  
  private boolean inQuotes = false;
  
  private int pos = 0;
  
  private char quoteChar = Character.MIN_VALUE;
  
  private boolean sawVariableUse = false;
  
  private String source = null;
  
  private int sourceLength = 0;
  
  public EscapeTokenizer(String paramString) {
    this.source = paramString;
    this.sourceLength = paramString.length();
    this.pos = 0;
  }
  
  public boolean hasMoreTokens() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield pos : I
    //   6: istore_1
    //   7: aload_0
    //   8: getfield sourceLength : I
    //   11: istore_2
    //   12: iload_1
    //   13: iload_2
    //   14: if_icmpge -> 22
    //   17: iconst_1
    //   18: istore_3
    //   19: goto -> 24
    //   22: iconst_0
    //   23: istore_3
    //   24: aload_0
    //   25: monitorexit
    //   26: iload_3
    //   27: ireturn
    //   28: astore #4
    //   30: aload_0
    //   31: monitorexit
    //   32: aload #4
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	28	finally
  }
  
  public String nextToken() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore #5
    //   7: aload #5
    //   9: invokespecial <init> : ()V
    //   12: aload_0
    //   13: getfield emittingEscapeCode : Z
    //   16: ifeq -> 32
    //   19: aload #5
    //   21: ldc '{'
    //   23: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: pop
    //   27: aload_0
    //   28: iconst_0
    //   29: putfield emittingEscapeCode : Z
    //   32: iconst_0
    //   33: istore_3
    //   34: aload_0
    //   35: getfield pos : I
    //   38: istore_2
    //   39: iload_2
    //   40: aload_0
    //   41: getfield sourceLength : I
    //   44: if_icmpge -> 506
    //   47: aload_0
    //   48: getfield source : Ljava/lang/String;
    //   51: iload_2
    //   52: invokevirtual charAt : (I)C
    //   55: istore_1
    //   56: iload_1
    //   57: bipush #92
    //   59: if_icmpne -> 76
    //   62: aload #5
    //   64: iload_1
    //   65: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: iload_3
    //   70: iconst_1
    //   71: ixor
    //   72: istore_2
    //   73: goto -> 491
    //   76: iload_1
    //   77: bipush #39
    //   79: if_icmpeq -> 88
    //   82: iload_1
    //   83: bipush #34
    //   85: if_icmpne -> 195
    //   88: iload_3
    //   89: ifne -> 195
    //   92: aload #5
    //   94: iload_1
    //   95: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload_0
    //   100: getfield inQuotes : Z
    //   103: ifeq -> 180
    //   106: iload_3
    //   107: istore_2
    //   108: iload_1
    //   109: aload_0
    //   110: getfield quoteChar : C
    //   113: if_icmpne -> 491
    //   116: aload_0
    //   117: getfield pos : I
    //   120: istore_2
    //   121: iload_2
    //   122: iconst_1
    //   123: iadd
    //   124: aload_0
    //   125: getfield sourceLength : I
    //   128: if_icmpge -> 170
    //   131: aload_0
    //   132: getfield source : Ljava/lang/String;
    //   135: iload_2
    //   136: iconst_1
    //   137: iadd
    //   138: invokevirtual charAt : (I)C
    //   141: aload_0
    //   142: getfield quoteChar : C
    //   145: if_icmpne -> 170
    //   148: aload #5
    //   150: iload_1
    //   151: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   154: pop
    //   155: aload_0
    //   156: aload_0
    //   157: getfield pos : I
    //   160: iconst_1
    //   161: iadd
    //   162: putfield pos : I
    //   165: iload_3
    //   166: istore_2
    //   167: goto -> 491
    //   170: aload_0
    //   171: iconst_0
    //   172: putfield inQuotes : Z
    //   175: iload_3
    //   176: istore_2
    //   177: goto -> 491
    //   180: aload_0
    //   181: iconst_1
    //   182: putfield inQuotes : Z
    //   185: aload_0
    //   186: iload_1
    //   187: putfield quoteChar : C
    //   190: iload_3
    //   191: istore_2
    //   192: goto -> 491
    //   195: iload_1
    //   196: bipush #10
    //   198: if_icmpeq -> 482
    //   201: iload_1
    //   202: bipush #13
    //   204: if_icmpne -> 210
    //   207: goto -> 482
    //   210: aload_0
    //   211: getfield inQuotes : Z
    //   214: ifne -> 472
    //   217: iload_3
    //   218: ifne -> 472
    //   221: iload_1
    //   222: bipush #45
    //   224: if_icmpne -> 343
    //   227: aload #5
    //   229: iload_1
    //   230: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   233: pop
    //   234: aload_0
    //   235: getfield pos : I
    //   238: istore #4
    //   240: iload_3
    //   241: istore_2
    //   242: iload #4
    //   244: iconst_1
    //   245: iadd
    //   246: aload_0
    //   247: getfield sourceLength : I
    //   250: if_icmpge -> 491
    //   253: iload_3
    //   254: istore_2
    //   255: aload_0
    //   256: getfield source : Ljava/lang/String;
    //   259: iload #4
    //   261: iconst_1
    //   262: iadd
    //   263: invokevirtual charAt : (I)C
    //   266: bipush #45
    //   268: if_icmpne -> 491
    //   271: iload_1
    //   272: istore_2
    //   273: aload_0
    //   274: getfield pos : I
    //   277: iconst_1
    //   278: iadd
    //   279: istore #4
    //   281: aload_0
    //   282: iload #4
    //   284: putfield pos : I
    //   287: iload #4
    //   289: aload_0
    //   290: getfield sourceLength : I
    //   293: if_icmpge -> 330
    //   296: iload_2
    //   297: bipush #10
    //   299: if_icmpeq -> 330
    //   302: iload_2
    //   303: bipush #13
    //   305: if_icmpeq -> 330
    //   308: aload_0
    //   309: getfield source : Ljava/lang/String;
    //   312: iload #4
    //   314: invokevirtual charAt : (I)C
    //   317: istore_1
    //   318: aload #5
    //   320: iload_1
    //   321: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   324: pop
    //   325: iload_1
    //   326: istore_2
    //   327: goto -> 273
    //   330: aload_0
    //   331: iload #4
    //   333: iconst_1
    //   334: isub
    //   335: putfield pos : I
    //   338: iload_3
    //   339: istore_2
    //   340: goto -> 491
    //   343: iload_1
    //   344: bipush #123
    //   346: if_icmpne -> 405
    //   349: aload_0
    //   350: getfield bracesLevel : I
    //   353: iconst_1
    //   354: iadd
    //   355: istore_2
    //   356: aload_0
    //   357: iload_2
    //   358: putfield bracesLevel : I
    //   361: iload_2
    //   362: iconst_1
    //   363: if_icmpne -> 393
    //   366: aload_0
    //   367: iconst_1
    //   368: putfield emittingEscapeCode : Z
    //   371: aload_0
    //   372: aload_0
    //   373: getfield pos : I
    //   376: iconst_1
    //   377: iadd
    //   378: putfield pos : I
    //   381: aload #5
    //   383: invokevirtual toString : ()Ljava/lang/String;
    //   386: astore #5
    //   388: aload_0
    //   389: monitorexit
    //   390: aload #5
    //   392: areturn
    //   393: aload #5
    //   395: iload_1
    //   396: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   399: pop
    //   400: iload_3
    //   401: istore_2
    //   402: goto -> 491
    //   405: iload_1
    //   406: bipush #125
    //   408: if_icmpne -> 461
    //   411: aload #5
    //   413: iload_1
    //   414: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   417: pop
    //   418: aload_0
    //   419: getfield bracesLevel : I
    //   422: iconst_1
    //   423: isub
    //   424: istore #4
    //   426: aload_0
    //   427: iload #4
    //   429: putfield bracesLevel : I
    //   432: iload_3
    //   433: istore_2
    //   434: iload #4
    //   436: ifne -> 491
    //   439: aload_0
    //   440: aload_0
    //   441: getfield pos : I
    //   444: iconst_1
    //   445: iadd
    //   446: putfield pos : I
    //   449: aload #5
    //   451: invokevirtual toString : ()Ljava/lang/String;
    //   454: astore #5
    //   456: aload_0
    //   457: monitorexit
    //   458: aload #5
    //   460: areturn
    //   461: iload_1
    //   462: bipush #64
    //   464: if_icmpne -> 472
    //   467: aload_0
    //   468: iconst_1
    //   469: putfield sawVariableUse : Z
    //   472: aload #5
    //   474: iload_1
    //   475: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   478: pop
    //   479: goto -> 489
    //   482: aload #5
    //   484: iload_1
    //   485: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   488: pop
    //   489: iconst_0
    //   490: istore_2
    //   491: aload_0
    //   492: aload_0
    //   493: getfield pos : I
    //   496: iconst_1
    //   497: iadd
    //   498: putfield pos : I
    //   501: iload_2
    //   502: istore_3
    //   503: goto -> 34
    //   506: aload #5
    //   508: invokevirtual toString : ()Ljava/lang/String;
    //   511: astore #5
    //   513: aload_0
    //   514: monitorexit
    //   515: aload #5
    //   517: areturn
    //   518: astore #5
    //   520: aload_0
    //   521: monitorexit
    //   522: aload #5
    //   524: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	518	finally
    //   34	56	518	finally
    //   62	69	518	finally
    //   92	106	518	finally
    //   108	165	518	finally
    //   170	175	518	finally
    //   180	190	518	finally
    //   210	217	518	finally
    //   227	240	518	finally
    //   242	253	518	finally
    //   255	271	518	finally
    //   273	296	518	finally
    //   308	325	518	finally
    //   330	338	518	finally
    //   349	361	518	finally
    //   366	388	518	finally
    //   393	400	518	finally
    //   411	432	518	finally
    //   439	456	518	finally
    //   467	472	518	finally
    //   472	479	518	finally
    //   482	489	518	finally
    //   491	501	518	finally
    //   506	513	518	finally
  }
  
  public boolean sawVariableUse() {
    return this.sawVariableUse;
  }
}
