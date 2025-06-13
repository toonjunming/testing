package okhttp3;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public final class CacheControl {
  public static final CacheControl FORCE_CACHE;
  
  public static final CacheControl FORCE_NETWORK = (new Builder()).noCache().build();
  
  @Nullable
  public String headerValue;
  
  private final boolean immutable;
  
  private final boolean isPrivate;
  
  private final boolean isPublic;
  
  private final int maxAgeSeconds;
  
  private final int maxStaleSeconds;
  
  private final int minFreshSeconds;
  
  private final boolean mustRevalidate;
  
  private final boolean noCache;
  
  private final boolean noStore;
  
  private final boolean noTransform;
  
  private final boolean onlyIfCached;
  
  private final int sMaxAgeSeconds;
  
  static {
    FORCE_CACHE = (new Builder()).onlyIfCached().maxStale(2147483647, TimeUnit.SECONDS).build();
  }
  
  public CacheControl(Builder paramBuilder) {
    this.noCache = paramBuilder.noCache;
    this.noStore = paramBuilder.noStore;
    this.maxAgeSeconds = paramBuilder.maxAgeSeconds;
    this.sMaxAgeSeconds = -1;
    this.isPrivate = false;
    this.isPublic = false;
    this.mustRevalidate = false;
    this.maxStaleSeconds = paramBuilder.maxStaleSeconds;
    this.minFreshSeconds = paramBuilder.minFreshSeconds;
    this.onlyIfCached = paramBuilder.onlyIfCached;
    this.noTransform = paramBuilder.noTransform;
    this.immutable = paramBuilder.immutable;
  }
  
  private CacheControl(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, int paramInt3, int paramInt4, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, @Nullable String paramString) {
    this.noCache = paramBoolean1;
    this.noStore = paramBoolean2;
    this.maxAgeSeconds = paramInt1;
    this.sMaxAgeSeconds = paramInt2;
    this.isPrivate = paramBoolean3;
    this.isPublic = paramBoolean4;
    this.mustRevalidate = paramBoolean5;
    this.maxStaleSeconds = paramInt3;
    this.minFreshSeconds = paramInt4;
    this.onlyIfCached = paramBoolean6;
    this.noTransform = paramBoolean7;
    this.immutable = paramBoolean8;
    this.headerValue = paramString;
  }
  
  private String headerValue() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.noCache)
      stringBuilder.append("no-cache, "); 
    if (this.noStore)
      stringBuilder.append("no-store, "); 
    if (this.maxAgeSeconds != -1) {
      stringBuilder.append("max-age=");
      stringBuilder.append(this.maxAgeSeconds);
      stringBuilder.append(", ");
    } 
    if (this.sMaxAgeSeconds != -1) {
      stringBuilder.append("s-maxage=");
      stringBuilder.append(this.sMaxAgeSeconds);
      stringBuilder.append(", ");
    } 
    if (this.isPrivate)
      stringBuilder.append("private, "); 
    if (this.isPublic)
      stringBuilder.append("public, "); 
    if (this.mustRevalidate)
      stringBuilder.append("must-revalidate, "); 
    if (this.maxStaleSeconds != -1) {
      stringBuilder.append("max-stale=");
      stringBuilder.append(this.maxStaleSeconds);
      stringBuilder.append(", ");
    } 
    if (this.minFreshSeconds != -1) {
      stringBuilder.append("min-fresh=");
      stringBuilder.append(this.minFreshSeconds);
      stringBuilder.append(", ");
    } 
    if (this.onlyIfCached)
      stringBuilder.append("only-if-cached, "); 
    if (this.noTransform)
      stringBuilder.append("no-transform, "); 
    if (this.immutable)
      stringBuilder.append("immutable, "); 
    if (stringBuilder.length() == 0)
      return ""; 
    stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
    return stringBuilder.toString();
  }
  
  public static CacheControl parse(Headers paramHeaders) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: istore #13
    //   6: iconst_0
    //   7: istore #7
    //   9: iconst_1
    //   10: istore_2
    //   11: aconst_null
    //   12: astore #30
    //   14: iconst_0
    //   15: istore #21
    //   17: iconst_0
    //   18: istore #20
    //   20: iconst_m1
    //   21: istore #6
    //   23: iconst_m1
    //   24: istore #5
    //   26: iconst_0
    //   27: istore #19
    //   29: iconst_0
    //   30: istore #18
    //   32: iconst_0
    //   33: istore #17
    //   35: iconst_m1
    //   36: istore #4
    //   38: iconst_m1
    //   39: istore_3
    //   40: iconst_0
    //   41: istore #16
    //   43: iconst_0
    //   44: istore #14
    //   46: iconst_0
    //   47: istore #15
    //   49: iload #7
    //   51: iload #13
    //   53: if_icmpge -> 1212
    //   56: aload_0
    //   57: iload #7
    //   59: invokevirtual name : (I)Ljava/lang/String;
    //   62: astore #33
    //   64: aload_0
    //   65: iload #7
    //   67: invokevirtual value : (I)Ljava/lang/String;
    //   70: astore #32
    //   72: aload #33
    //   74: ldc 'Cache-Control'
    //   76: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   79: ifeq -> 97
    //   82: aload #30
    //   84: ifnull -> 90
    //   87: goto -> 160
    //   90: aload #32
    //   92: astore #30
    //   94: goto -> 162
    //   97: iload_2
    //   98: istore #12
    //   100: aload #30
    //   102: astore #31
    //   104: iload #21
    //   106: istore #26
    //   108: iload #20
    //   110: istore #28
    //   112: iload #6
    //   114: istore #8
    //   116: iload #5
    //   118: istore_1
    //   119: iload #19
    //   121: istore #22
    //   123: iload #18
    //   125: istore #27
    //   127: iload #17
    //   129: istore #23
    //   131: iload #4
    //   133: istore #9
    //   135: iload_3
    //   136: istore #10
    //   138: iload #16
    //   140: istore #25
    //   142: iload #14
    //   144: istore #24
    //   146: iload #15
    //   148: istore #29
    //   150: aload #33
    //   152: ldc 'Pragma'
    //   154: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   157: ifeq -> 1153
    //   160: iconst_0
    //   161: istore_2
    //   162: iconst_0
    //   163: istore #11
    //   165: iload_2
    //   166: istore #12
    //   168: aload #30
    //   170: astore #31
    //   172: iload #21
    //   174: istore #26
    //   176: iload #20
    //   178: istore #28
    //   180: iload #6
    //   182: istore #8
    //   184: iload #5
    //   186: istore_1
    //   187: iload #19
    //   189: istore #22
    //   191: iload #18
    //   193: istore #27
    //   195: iload #17
    //   197: istore #23
    //   199: iload #4
    //   201: istore #9
    //   203: iload_3
    //   204: istore #10
    //   206: iload #16
    //   208: istore #25
    //   210: iload #14
    //   212: istore #24
    //   214: iload #15
    //   216: istore #29
    //   218: iload #11
    //   220: aload #32
    //   222: invokevirtual length : ()I
    //   225: if_icmpge -> 1153
    //   228: aload #32
    //   230: iload #11
    //   232: ldc '=,;'
    //   234: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   237: istore_1
    //   238: aload #32
    //   240: iload #11
    //   242: iload_1
    //   243: invokevirtual substring : (II)Ljava/lang/String;
    //   246: invokevirtual trim : ()Ljava/lang/String;
    //   249: astore #33
    //   251: iload_1
    //   252: aload #32
    //   254: invokevirtual length : ()I
    //   257: if_icmpeq -> 372
    //   260: aload #32
    //   262: iload_1
    //   263: invokevirtual charAt : (I)C
    //   266: bipush #44
    //   268: if_icmpeq -> 372
    //   271: aload #32
    //   273: iload_1
    //   274: invokevirtual charAt : (I)C
    //   277: bipush #59
    //   279: if_icmpne -> 285
    //   282: goto -> 372
    //   285: aload #32
    //   287: iload_1
    //   288: iconst_1
    //   289: iadd
    //   290: invokestatic skipWhitespace : (Ljava/lang/String;I)I
    //   293: istore #8
    //   295: iload #8
    //   297: aload #32
    //   299: invokevirtual length : ()I
    //   302: if_icmpge -> 346
    //   305: aload #32
    //   307: iload #8
    //   309: invokevirtual charAt : (I)C
    //   312: bipush #34
    //   314: if_icmpne -> 346
    //   317: iinc #8, 1
    //   320: aload #32
    //   322: iload #8
    //   324: ldc '"'
    //   326: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   329: istore_1
    //   330: aload #32
    //   332: iload #8
    //   334: iload_1
    //   335: invokevirtual substring : (II)Ljava/lang/String;
    //   338: astore #31
    //   340: iinc #1, 1
    //   343: goto -> 378
    //   346: aload #32
    //   348: iload #8
    //   350: ldc ',;'
    //   352: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   355: istore_1
    //   356: aload #32
    //   358: iload #8
    //   360: iload_1
    //   361: invokevirtual substring : (II)Ljava/lang/String;
    //   364: invokevirtual trim : ()Ljava/lang/String;
    //   367: astore #31
    //   369: goto -> 378
    //   372: iinc #1, 1
    //   375: aconst_null
    //   376: astore #31
    //   378: ldc 'no-cache'
    //   380: aload #33
    //   382: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   385: ifeq -> 433
    //   388: iconst_1
    //   389: istore #22
    //   391: iload #20
    //   393: istore #23
    //   395: iload #6
    //   397: istore #8
    //   399: iload #5
    //   401: istore #9
    //   403: iload #19
    //   405: istore #24
    //   407: iload #18
    //   409: istore #25
    //   411: iload #17
    //   413: istore #26
    //   415: iload #4
    //   417: istore #10
    //   419: iload_3
    //   420: istore #12
    //   422: iload #16
    //   424: istore #27
    //   426: iload #14
    //   428: istore #28
    //   430: goto -> 1104
    //   433: ldc 'no-store'
    //   435: aload #33
    //   437: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   440: ifeq -> 488
    //   443: iconst_1
    //   444: istore #23
    //   446: iload #21
    //   448: istore #22
    //   450: iload #6
    //   452: istore #8
    //   454: iload #5
    //   456: istore #9
    //   458: iload #19
    //   460: istore #24
    //   462: iload #18
    //   464: istore #25
    //   466: iload #17
    //   468: istore #26
    //   470: iload #4
    //   472: istore #10
    //   474: iload_3
    //   475: istore #12
    //   477: iload #16
    //   479: istore #27
    //   481: iload #14
    //   483: istore #28
    //   485: goto -> 1104
    //   488: ldc 'max-age'
    //   490: aload #33
    //   492: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   495: ifeq -> 548
    //   498: aload #31
    //   500: iconst_m1
    //   501: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   504: istore #8
    //   506: iload #21
    //   508: istore #22
    //   510: iload #20
    //   512: istore #23
    //   514: iload #5
    //   516: istore #9
    //   518: iload #19
    //   520: istore #24
    //   522: iload #18
    //   524: istore #25
    //   526: iload #17
    //   528: istore #26
    //   530: iload #4
    //   532: istore #10
    //   534: iload_3
    //   535: istore #12
    //   537: iload #16
    //   539: istore #27
    //   541: iload #14
    //   543: istore #28
    //   545: goto -> 1104
    //   548: ldc 's-maxage'
    //   550: aload #33
    //   552: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   555: ifeq -> 608
    //   558: aload #31
    //   560: iconst_m1
    //   561: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   564: istore #9
    //   566: iload #21
    //   568: istore #22
    //   570: iload #20
    //   572: istore #23
    //   574: iload #6
    //   576: istore #8
    //   578: iload #19
    //   580: istore #24
    //   582: iload #18
    //   584: istore #25
    //   586: iload #17
    //   588: istore #26
    //   590: iload #4
    //   592: istore #10
    //   594: iload_3
    //   595: istore #12
    //   597: iload #16
    //   599: istore #27
    //   601: iload #14
    //   603: istore #28
    //   605: goto -> 1104
    //   608: ldc 'private'
    //   610: aload #33
    //   612: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   615: ifeq -> 663
    //   618: iconst_1
    //   619: istore #24
    //   621: iload #21
    //   623: istore #22
    //   625: iload #20
    //   627: istore #23
    //   629: iload #6
    //   631: istore #8
    //   633: iload #5
    //   635: istore #9
    //   637: iload #18
    //   639: istore #25
    //   641: iload #17
    //   643: istore #26
    //   645: iload #4
    //   647: istore #10
    //   649: iload_3
    //   650: istore #12
    //   652: iload #16
    //   654: istore #27
    //   656: iload #14
    //   658: istore #28
    //   660: goto -> 1104
    //   663: ldc 'public'
    //   665: aload #33
    //   667: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   670: ifeq -> 718
    //   673: iconst_1
    //   674: istore #25
    //   676: iload #21
    //   678: istore #22
    //   680: iload #20
    //   682: istore #23
    //   684: iload #6
    //   686: istore #8
    //   688: iload #5
    //   690: istore #9
    //   692: iload #19
    //   694: istore #24
    //   696: iload #17
    //   698: istore #26
    //   700: iload #4
    //   702: istore #10
    //   704: iload_3
    //   705: istore #12
    //   707: iload #16
    //   709: istore #27
    //   711: iload #14
    //   713: istore #28
    //   715: goto -> 1104
    //   718: ldc 'must-revalidate'
    //   720: aload #33
    //   722: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   725: ifeq -> 773
    //   728: iconst_1
    //   729: istore #26
    //   731: iload #21
    //   733: istore #22
    //   735: iload #20
    //   737: istore #23
    //   739: iload #6
    //   741: istore #8
    //   743: iload #5
    //   745: istore #9
    //   747: iload #19
    //   749: istore #24
    //   751: iload #18
    //   753: istore #25
    //   755: iload #4
    //   757: istore #10
    //   759: iload_3
    //   760: istore #12
    //   762: iload #16
    //   764: istore #27
    //   766: iload #14
    //   768: istore #28
    //   770: goto -> 1104
    //   773: ldc 'max-stale'
    //   775: aload #33
    //   777: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   780: ifeq -> 834
    //   783: aload #31
    //   785: ldc 2147483647
    //   787: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   790: istore #10
    //   792: iload #21
    //   794: istore #22
    //   796: iload #20
    //   798: istore #23
    //   800: iload #6
    //   802: istore #8
    //   804: iload #5
    //   806: istore #9
    //   808: iload #19
    //   810: istore #24
    //   812: iload #18
    //   814: istore #25
    //   816: iload #17
    //   818: istore #26
    //   820: iload_3
    //   821: istore #12
    //   823: iload #16
    //   825: istore #27
    //   827: iload #14
    //   829: istore #28
    //   831: goto -> 1104
    //   834: ldc 'min-fresh'
    //   836: aload #33
    //   838: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   841: ifeq -> 895
    //   844: aload #31
    //   846: iconst_m1
    //   847: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   850: istore #12
    //   852: iload #21
    //   854: istore #22
    //   856: iload #20
    //   858: istore #23
    //   860: iload #6
    //   862: istore #8
    //   864: iload #5
    //   866: istore #9
    //   868: iload #19
    //   870: istore #24
    //   872: iload #18
    //   874: istore #25
    //   876: iload #17
    //   878: istore #26
    //   880: iload #4
    //   882: istore #10
    //   884: iload #16
    //   886: istore #27
    //   888: iload #14
    //   890: istore #28
    //   892: goto -> 1104
    //   895: ldc 'only-if-cached'
    //   897: aload #33
    //   899: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   902: ifeq -> 950
    //   905: iconst_1
    //   906: istore #27
    //   908: iload #21
    //   910: istore #22
    //   912: iload #20
    //   914: istore #23
    //   916: iload #6
    //   918: istore #8
    //   920: iload #5
    //   922: istore #9
    //   924: iload #19
    //   926: istore #24
    //   928: iload #18
    //   930: istore #25
    //   932: iload #17
    //   934: istore #26
    //   936: iload #4
    //   938: istore #10
    //   940: iload_3
    //   941: istore #12
    //   943: iload #14
    //   945: istore #28
    //   947: goto -> 1104
    //   950: ldc 'no-transform'
    //   952: aload #33
    //   954: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   957: ifeq -> 1005
    //   960: iconst_1
    //   961: istore #28
    //   963: iload #21
    //   965: istore #22
    //   967: iload #20
    //   969: istore #23
    //   971: iload #6
    //   973: istore #8
    //   975: iload #5
    //   977: istore #9
    //   979: iload #19
    //   981: istore #24
    //   983: iload #18
    //   985: istore #25
    //   987: iload #17
    //   989: istore #26
    //   991: iload #4
    //   993: istore #10
    //   995: iload_3
    //   996: istore #12
    //   998: iload #16
    //   1000: istore #27
    //   1002: goto -> 1104
    //   1005: iload #21
    //   1007: istore #22
    //   1009: iload #20
    //   1011: istore #23
    //   1013: iload #6
    //   1015: istore #8
    //   1017: iload #5
    //   1019: istore #9
    //   1021: iload #19
    //   1023: istore #24
    //   1025: iload #18
    //   1027: istore #25
    //   1029: iload #17
    //   1031: istore #26
    //   1033: iload #4
    //   1035: istore #10
    //   1037: iload_3
    //   1038: istore #12
    //   1040: iload #16
    //   1042: istore #27
    //   1044: iload #14
    //   1046: istore #28
    //   1048: ldc 'immutable'
    //   1050: aload #33
    //   1052: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1055: ifeq -> 1104
    //   1058: iconst_1
    //   1059: istore #15
    //   1061: iload #14
    //   1063: istore #28
    //   1065: iload #16
    //   1067: istore #27
    //   1069: iload_3
    //   1070: istore #12
    //   1072: iload #4
    //   1074: istore #10
    //   1076: iload #17
    //   1078: istore #26
    //   1080: iload #18
    //   1082: istore #25
    //   1084: iload #19
    //   1086: istore #24
    //   1088: iload #5
    //   1090: istore #9
    //   1092: iload #6
    //   1094: istore #8
    //   1096: iload #20
    //   1098: istore #23
    //   1100: iload #21
    //   1102: istore #22
    //   1104: iload_1
    //   1105: istore #11
    //   1107: iload #22
    //   1109: istore #21
    //   1111: iload #23
    //   1113: istore #20
    //   1115: iload #8
    //   1117: istore #6
    //   1119: iload #9
    //   1121: istore #5
    //   1123: iload #24
    //   1125: istore #19
    //   1127: iload #25
    //   1129: istore #18
    //   1131: iload #26
    //   1133: istore #17
    //   1135: iload #10
    //   1137: istore #4
    //   1139: iload #12
    //   1141: istore_3
    //   1142: iload #27
    //   1144: istore #16
    //   1146: iload #28
    //   1148: istore #14
    //   1150: goto -> 165
    //   1153: iinc #7, 1
    //   1156: iload #12
    //   1158: istore_2
    //   1159: aload #31
    //   1161: astore #30
    //   1163: iload #26
    //   1165: istore #21
    //   1167: iload #28
    //   1169: istore #20
    //   1171: iload #8
    //   1173: istore #6
    //   1175: iload_1
    //   1176: istore #5
    //   1178: iload #22
    //   1180: istore #19
    //   1182: iload #27
    //   1184: istore #18
    //   1186: iload #23
    //   1188: istore #17
    //   1190: iload #9
    //   1192: istore #4
    //   1194: iload #10
    //   1196: istore_3
    //   1197: iload #25
    //   1199: istore #16
    //   1201: iload #24
    //   1203: istore #14
    //   1205: iload #29
    //   1207: istore #15
    //   1209: goto -> 49
    //   1212: iload_2
    //   1213: ifne -> 1222
    //   1216: aconst_null
    //   1217: astore #30
    //   1219: goto -> 1222
    //   1222: new okhttp3/CacheControl
    //   1225: dup
    //   1226: iload #21
    //   1228: iload #20
    //   1230: iload #6
    //   1232: iload #5
    //   1234: iload #19
    //   1236: iload #18
    //   1238: iload #17
    //   1240: iload #4
    //   1242: iload_3
    //   1243: iload #16
    //   1245: iload #14
    //   1247: iload #15
    //   1249: aload #30
    //   1251: invokespecial <init> : (ZZIIZZZIIZZZLjava/lang/String;)V
    //   1254: areturn
  }
  
  public boolean immutable() {
    return this.immutable;
  }
  
  public boolean isPrivate() {
    return this.isPrivate;
  }
  
  public boolean isPublic() {
    return this.isPublic;
  }
  
  public int maxAgeSeconds() {
    return this.maxAgeSeconds;
  }
  
  public int maxStaleSeconds() {
    return this.maxStaleSeconds;
  }
  
  public int minFreshSeconds() {
    return this.minFreshSeconds;
  }
  
  public boolean mustRevalidate() {
    return this.mustRevalidate;
  }
  
  public boolean noCache() {
    return this.noCache;
  }
  
  public boolean noStore() {
    return this.noStore;
  }
  
  public boolean noTransform() {
    return this.noTransform;
  }
  
  public boolean onlyIfCached() {
    return this.onlyIfCached;
  }
  
  public int sMaxAgeSeconds() {
    return this.sMaxAgeSeconds;
  }
  
  public String toString() {
    String str = this.headerValue;
    if (str == null) {
      str = headerValue();
      this.headerValue = str;
    } 
    return str;
  }
  
  public static final class Builder {
    public boolean immutable;
    
    public int maxAgeSeconds = -1;
    
    public int maxStaleSeconds = -1;
    
    public int minFreshSeconds = -1;
    
    public boolean noCache;
    
    public boolean noStore;
    
    public boolean noTransform;
    
    public boolean onlyIfCached;
    
    public CacheControl build() {
      return new CacheControl(this);
    }
    
    public Builder immutable() {
      this.immutable = true;
      return this;
    }
    
    public Builder maxAge(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int >= 0) {
        long l = param1TimeUnit.toSeconds(param1Int);
        if (l > 2147483647L) {
          param1Int = Integer.MAX_VALUE;
        } else {
          param1Int = (int)l;
        } 
        this.maxAgeSeconds = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("maxAge < 0: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder maxStale(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int >= 0) {
        long l = param1TimeUnit.toSeconds(param1Int);
        if (l > 2147483647L) {
          param1Int = Integer.MAX_VALUE;
        } else {
          param1Int = (int)l;
        } 
        this.maxStaleSeconds = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("maxStale < 0: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder minFresh(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int >= 0) {
        long l = param1TimeUnit.toSeconds(param1Int);
        if (l > 2147483647L) {
          param1Int = Integer.MAX_VALUE;
        } else {
          param1Int = (int)l;
        } 
        this.minFreshSeconds = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("minFresh < 0: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder noCache() {
      this.noCache = true;
      return this;
    }
    
    public Builder noStore() {
      this.noStore = true;
      return this;
    }
    
    public Builder noTransform() {
      this.noTransform = true;
      return this;
    }
    
    public Builder onlyIfCached() {
      this.onlyIfCached = true;
      return this;
    }
  }
}
