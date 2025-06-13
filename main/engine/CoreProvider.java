package com.main.engine;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoreProvider extends ContentProvider {
  static {
  
  }
  
  public Bundle call(String paramString1, String paramString2, Bundle paramBundle) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   6: ifne -> 2494
    //   9: aload_1
    //   10: bipush #15
    //   12: newarray byte
    //   14: dup
    //   15: iconst_0
    //   16: bipush #74
    //   18: bastore
    //   19: dup
    //   20: iconst_1
    //   21: bipush #85
    //   23: bastore
    //   24: dup
    //   25: iconst_2
    //   26: bipush #93
    //   28: bastore
    //   29: dup
    //   30: iconst_3
    //   31: bipush #83
    //   33: bastore
    //   34: dup
    //   35: iconst_4
    //   36: bipush #98
    //   38: bastore
    //   39: dup
    //   40: iconst_5
    //   41: bipush #21
    //   43: bastore
    //   44: dup
    //   45: bipush #6
    //   47: bipush #92
    //   49: bastore
    //   50: dup
    //   51: bipush #7
    //   53: bipush #66
    //   55: bastore
    //   56: dup
    //   57: bipush #8
    //   59: bipush #96
    //   61: bastore
    //   62: dup
    //   63: bipush #9
    //   65: bipush #82
    //   67: bastore
    //   68: dup
    //   69: bipush #10
    //   71: bipush #69
    //   73: bastore
    //   74: dup
    //   75: bipush #11
    //   77: bipush #16
    //   79: bastore
    //   80: dup
    //   81: bipush #12
    //   83: bipush #80
    //   85: bastore
    //   86: dup
    //   87: bipush #13
    //   89: bipush #83
    //   91: bastore
    //   92: dup
    //   93: bipush #14
    //   95: bipush #86
    //   97: bastore
    //   98: ldc '90377f'
    //   100: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   103: invokevirtual compareTo : (Ljava/lang/String;)I
    //   106: ifne -> 122
    //   109: invokestatic I丨L : ()LILil/I丨L/ILil/Lil;
    //   112: invokevirtual Ilil : ()LILil/I丨L/ILil/丨l丨;
    //   115: checkcast android/app/INXCoreNative$Stub
    //   118: astore_1
    //   119: goto -> 2064
    //   122: aload_1
    //   123: bipush #18
    //   125: newarray byte
    //   127: dup
    //   128: iconst_0
    //   129: bipush #67
    //   131: bastore
    //   132: dup
    //   133: iconst_1
    //   134: bipush #86
    //   136: bastore
    //   137: dup
    //   138: iconst_2
    //   139: bipush #10
    //   141: bastore
    //   142: dup
    //   143: iconst_3
    //   144: bipush #84
    //   146: bastore
    //   147: dup
    //   148: iconst_4
    //   149: bipush #55
    //   151: bastore
    //   152: dup
    //   153: iconst_5
    //   154: bipush #18
    //   156: bastore
    //   157: dup
    //   158: bipush #6
    //   160: bipush #85
    //   162: bastore
    //   163: dup
    //   164: bipush #7
    //   166: bipush #65
    //   168: bastore
    //   169: dup
    //   170: bipush #8
    //   172: bipush #55
    //   174: bastore
    //   175: dup
    //   176: bipush #9
    //   178: bipush #85
    //   180: bastore
    //   181: dup
    //   182: bipush #10
    //   184: bipush #16
    //   186: bastore
    //   187: dup
    //   188: bipush #11
    //   190: bipush #23
    //   192: bastore
    //   193: dup
    //   194: bipush #12
    //   196: bipush #89
    //   198: bastore
    //   199: dup
    //   200: bipush #13
    //   202: bipush #80
    //   204: bastore
    //   205: dup
    //   206: bipush #14
    //   208: iconst_1
    //   209: bastore
    //   210: dup
    //   211: bipush #15
    //   213: bipush #113
    //   215: bastore
    //   216: dup
    //   217: bipush #16
    //   219: iconst_1
    //   220: bastore
    //   221: dup
    //   222: bipush #17
    //   224: iconst_2
    //   225: bastore
    //   226: ldc '03d0ba'
    //   228: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   231: invokevirtual compareTo : (Ljava/lang/String;)I
    //   234: ifne -> 250
    //   237: invokestatic I丨L : ()LILil/I丨L/ILil/Lil;
    //   240: invokevirtual IL1Iii : ()LILil/I丨L/ILil/丨l丨;
    //   243: checkcast android/app/INXCoreNative$Stub
    //   246: astore_1
    //   247: goto -> 2064
    //   250: aload_1
    //   251: bipush #21
    //   253: newarray byte
    //   255: dup
    //   256: iconst_0
    //   257: bipush #69
    //   259: bastore
    //   260: dup
    //   261: iconst_1
    //   262: bipush #85
    //   264: bastore
    //   265: dup
    //   266: iconst_2
    //   267: bipush #12
    //   269: bastore
    //   270: dup
    //   271: iconst_3
    //   272: bipush #84
    //   274: bastore
    //   275: dup
    //   276: iconst_4
    //   277: bipush #98
    //   279: bastore
    //   280: dup
    //   281: iconst_5
    //   282: bipush #74
    //   284: bastore
    //   285: dup
    //   286: bipush #6
    //   288: bipush #83
    //   290: bastore
    //   291: dup
    //   292: bipush #7
    //   294: bipush #66
    //   296: bastore
    //   297: dup
    //   298: bipush #8
    //   300: bipush #49
    //   302: bastore
    //   303: dup
    //   304: bipush #9
    //   306: bipush #85
    //   308: bastore
    //   309: dup
    //   310: bipush #10
    //   312: bipush #69
    //   314: bastore
    //   315: dup
    //   316: bipush #11
    //   318: bipush #79
    //   320: bastore
    //   321: dup
    //   322: bipush #12
    //   324: bipush #95
    //   326: bastore
    //   327: dup
    //   328: bipush #13
    //   330: bipush #83
    //   332: bastore
    //   333: dup
    //   334: bipush #14
    //   336: bipush #7
    //   338: bastore
    //   339: dup
    //   340: bipush #15
    //   342: bipush #99
    //   344: bastore
    //   345: dup
    //   346: bipush #16
    //   348: bipush #84
    //   350: bastore
    //   351: dup
    //   352: bipush #17
    //   354: bipush #75
    //   356: bastore
    //   357: dup
    //   358: bipush #18
    //   360: bipush #95
    //   362: bastore
    //   363: dup
    //   364: bipush #19
    //   366: bipush #64
    //   368: bastore
    //   369: dup
    //   370: bipush #20
    //   372: bipush #22
    //   374: bastore
    //   375: ldc '60b079'
    //   377: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   380: invokevirtual compareTo : (Ljava/lang/String;)I
    //   383: ifne -> 399
    //   386: invokestatic I丨L : ()LILil/I丨L/ILil/Lil;
    //   389: invokevirtual l丨Li1LL : ()LILil/I丨L/ILil/丨l丨;
    //   392: checkcast android/app/INXCoreNative$Stub
    //   395: astore_1
    //   396: goto -> 2064
    //   399: aload_1
    //   400: bipush #20
    //   402: newarray byte
    //   404: dup
    //   405: iconst_0
    //   406: bipush #16
    //   408: bastore
    //   409: dup
    //   410: iconst_1
    //   411: iconst_1
    //   412: bastore
    //   413: dup
    //   414: iconst_2
    //   415: bipush #15
    //   417: bastore
    //   418: dup
    //   419: iconst_3
    //   420: bipush #6
    //   422: bastore
    //   423: dup
    //   424: iconst_4
    //   425: bipush #96
    //   427: bastore
    //   428: dup
    //   429: iconst_5
    //   430: bipush #18
    //   432: bastore
    //   433: dup
    //   434: bipush #6
    //   436: bipush #6
    //   438: bastore
    //   439: dup
    //   440: bipush #7
    //   442: bipush #22
    //   444: bastore
    //   445: dup
    //   446: bipush #8
    //   448: bipush #50
    //   450: bastore
    //   451: dup
    //   452: bipush #9
    //   454: bipush #7
    //   456: bastore
    //   457: dup
    //   458: bipush #10
    //   460: bipush #71
    //   462: bastore
    //   463: dup
    //   464: bipush #11
    //   466: bipush #23
    //   468: bastore
    //   469: dup
    //   470: bipush #12
    //   472: bipush #10
    //   474: bastore
    //   475: dup
    //   476: bipush #13
    //   478: bipush #7
    //   480: bastore
    //   481: dup
    //   482: bipush #14
    //   484: iconst_4
    //   485: bastore
    //   486: dup
    //   487: bipush #15
    //   489: bipush #50
    //   491: bastore
    //   492: dup
    //   493: bipush #16
    //   495: bipush #71
    //   497: bastore
    //   498: dup
    //   499: bipush #17
    //   501: bipush #14
    //   503: bastore
    //   504: dup
    //   505: bipush #18
    //   507: bipush #27
    //   509: bastore
    //   510: dup
    //   511: bipush #19
    //   513: bipush #29
    //   515: bastore
    //   516: ldc 'cdab5a'
    //   518: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   521: invokevirtual compareTo : (Ljava/lang/String;)I
    //   524: ifne -> 956
    //   527: invokestatic I丨L : ()LILil/I丨L/ILil/Lil;
    //   530: invokevirtual l丨Li1LL : ()LILil/I丨L/ILil/丨l丨;
    //   533: checkcast android/app/INXCoreNative$Stub
    //   536: astore_1
    //   537: invokestatic iI : ()LILil/I丨L/ILil/i1;
    //   540: aload_3
    //   541: invokevirtual LIlLi : (Landroid/os/Bundle;)V
    //   544: new android/os/Bundle
    //   547: astore_3
    //   548: aload_3
    //   549: invokespecial <init> : ()V
    //   552: bipush #40
    //   554: newarray byte
    //   556: dup
    //   557: iconst_0
    //   558: bipush #84
    //   560: bastore
    //   561: dup
    //   562: iconst_1
    //   563: bipush #92
    //   565: bastore
    //   566: dup
    //   567: iconst_2
    //   568: bipush #89
    //   570: bastore
    //   571: dup
    //   572: iconst_3
    //   573: bipush #28
    //   575: bastore
    //   576: dup
    //   577: iconst_4
    //   578: bipush #92
    //   580: bastore
    //   581: dup
    //   582: iconst_5
    //   583: bipush #30
    //   585: bastore
    //   586: dup
    //   587: bipush #6
    //   589: bipush #25
    //   591: bastore
    //   592: dup
    //   593: bipush #7
    //   595: bipush #80
    //   597: bastore
    //   598: dup
    //   599: bipush #8
    //   601: bipush #91
    //   603: bastore
    //   604: dup
    //   605: bipush #9
    //   607: bipush #64
    //   609: bastore
    //   610: dup
    //   611: bipush #10
    //   613: bipush #87
    //   615: bastore
    //   616: dup
    //   617: bipush #11
    //   619: bipush #72
    //   621: bastore
    //   622: dup
    //   623: bipush #12
    //   625: bipush #64
    //   627: bastore
    //   628: dup
    //   629: bipush #13
    //   631: bipush #65
    //   633: bastore
    //   634: dup
    //   635: bipush #14
    //   637: bipush #85
    //   639: bastore
    //   640: dup
    //   641: bipush #15
    //   643: bipush #66
    //   645: bastore
    //   646: dup
    //   647: bipush #16
    //   649: bipush #66
    //   651: bastore
    //   652: dup
    //   653: bipush #17
    //   655: iconst_3
    //   656: bastore
    //   657: dup
    //   658: bipush #18
    //   660: bipush #69
    //   662: bastore
    //   663: dup
    //   664: bipush #19
    //   666: bipush #64
    //   668: bastore
    //   669: dup
    //   670: bipush #20
    //   672: bipush #26
    //   674: bastore
    //   675: dup
    //   676: bipush #21
    //   678: bipush #91
    //   680: bastore
    //   681: dup
    //   682: bipush #22
    //   684: bipush #92
    //   686: bastore
    //   687: dup
    //   688: bipush #23
    //   690: bipush #18
    //   692: bastore
    //   693: dup
    //   694: bipush #24
    //   696: bipush #82
    //   698: bastore
    //   699: dup
    //   700: bipush #25
    //   702: bipush #93
    //   704: bastore
    //   705: dup
    //   706: bipush #26
    //   708: bipush #64
    //   710: bastore
    //   711: dup
    //   712: bipush #27
    //   714: bipush #28
    //   716: bastore
    //   717: dup
    //   718: bipush #28
    //   720: bipush #87
    //   722: bastore
    //   723: dup
    //   724: bipush #29
    //   726: bipush #30
    //   728: bastore
    //   729: dup
    //   730: bipush #30
    //   732: bipush #67
    //   734: bastore
    //   735: dup
    //   736: bipush #31
    //   738: bipush #65
    //   740: bastore
    //   741: dup
    //   742: bipush #32
    //   744: bipush #85
    //   746: bastore
    //   747: dup
    //   748: bipush #33
    //   750: bipush #28
    //   752: bastore
    //   753: dup
    //   754: bipush #34
    //   756: bipush #112
    //   758: bastore
    //   759: dup
    //   760: bipush #35
    //   762: bipush #47
    //   764: bastore
    //   765: dup
    //   766: bipush #36
    //   768: bipush #121
    //   770: bastore
    //   771: dup
    //   772: bipush #37
    //   774: bipush #119
    //   776: bastore
    //   777: dup
    //   778: bipush #38
    //   780: bipush #113
    //   782: bastore
    //   783: dup
    //   784: bipush #39
    //   786: bipush #96
    //   788: bastore
    //   789: ldc '73422f'
    //   791: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   794: astore #4
    //   796: new com/main/engine/BinderContainer
    //   799: astore_2
    //   800: aload_2
    //   801: aload_1
    //   802: invokespecial <init> : (Landroid/os/IBinder;)V
    //   805: aload_3
    //   806: aload #4
    //   808: aload_2
    //   809: invokevirtual putParcelable : (Ljava/lang/String;Landroid/os/Parcelable;)V
    //   812: aload_3
    //   813: bipush #17
    //   815: newarray byte
    //   817: dup
    //   818: iconst_0
    //   819: iconst_5
    //   820: bastore
    //   821: dup
    //   822: iconst_1
    //   823: bipush #12
    //   825: bastore
    //   826: dup
    //   827: iconst_2
    //   828: bipush #92
    //   830: bastore
    //   831: dup
    //   832: iconst_3
    //   833: bipush #30
    //   835: bastore
    //   836: dup
    //   837: iconst_4
    //   838: bipush #12
    //   840: bastore
    //   841: dup
    //   842: iconst_5
    //   843: bipush #30
    //   845: bastore
    //   846: dup
    //   847: bipush #6
    //   849: bipush #72
    //   851: bastore
    //   852: dup
    //   853: bipush #7
    //   855: iconst_0
    //   856: bastore
    //   857: dup
    //   858: bipush #8
    //   860: bipush #94
    //   862: bastore
    //   863: dup
    //   864: bipush #9
    //   866: bipush #66
    //   868: bastore
    //   869: dup
    //   870: bipush #10
    //   872: bipush #7
    //   874: bastore
    //   875: dup
    //   876: bipush #11
    //   878: bipush #72
    //   880: bastore
    //   881: dup
    //   882: bipush #12
    //   884: bipush #18
    //   886: bastore
    //   887: dup
    //   888: bipush #13
    //   890: bipush #12
    //   892: bastore
    //   893: dup
    //   894: bipush #14
    //   896: bipush #90
    //   898: bastore
    //   899: dup
    //   900: bipush #15
    //   902: bipush #85
    //   904: bastore
    //   905: dup
    //   906: bipush #16
    //   908: bipush #12
    //   910: bastore
    //   911: ldc 'fc10bf'
    //   913: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   916: iconst_5
    //   917: newarray byte
    //   919: dup
    //   920: iconst_0
    //   921: bipush #91
    //   923: bastore
    //   924: dup
    //   925: iconst_1
    //   926: bipush #93
    //   928: bastore
    //   929: dup
    //   930: iconst_2
    //   931: bipush #94
    //   933: bastore
    //   934: dup
    //   935: iconst_3
    //   936: bipush #8
    //   938: bastore
    //   939: dup
    //   940: iconst_4
    //   941: bipush #10
    //   943: bastore
    //   944: ldc '382de7'
    //   946: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   949: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
    //   952: aload_0
    //   953: monitorexit
    //   954: aload_3
    //   955: areturn
    //   956: aload_1
    //   957: bipush #21
    //   959: newarray byte
    //   961: dup
    //   962: iconst_0
    //   963: bipush #16
    //   965: bastore
    //   966: dup
    //   967: iconst_1
    //   968: iconst_3
    //   969: bastore
    //   970: dup
    //   971: iconst_2
    //   972: bipush #92
    //   974: bastore
    //   975: dup
    //   976: iconst_3
    //   977: iconst_0
    //   978: bastore
    //   979: dup
    //   980: iconst_4
    //   981: bipush #54
    //   983: bastore
    //   984: dup
    //   985: iconst_5
    //   986: bipush #64
    //   988: bastore
    //   989: dup
    //   990: bipush #6
    //   992: bipush #6
    //   994: bastore
    //   995: dup
    //   996: bipush #7
    //   998: bipush #20
    //   1000: bastore
    //   1001: dup
    //   1002: bipush #8
    //   1004: bipush #97
    //   1006: bastore
    //   1007: dup
    //   1008: bipush #9
    //   1010: iconst_1
    //   1011: bastore
    //   1012: dup
    //   1013: bipush #10
    //   1015: bipush #17
    //   1017: bastore
    //   1018: dup
    //   1019: bipush #11
    //   1021: bipush #69
    //   1023: bastore
    //   1024: dup
    //   1025: bipush #12
    //   1027: bipush #10
    //   1029: bastore
    //   1030: dup
    //   1031: bipush #13
    //   1033: iconst_5
    //   1034: bastore
    //   1035: dup
    //   1036: bipush #14
    //   1038: bipush #87
    //   1040: bastore
    //   1041: dup
    //   1042: bipush #15
    //   1044: bipush #49
    //   1046: bastore
    //   1047: dup
    //   1048: bipush #16
    //   1050: bipush #10
    //   1052: bastore
    //   1053: dup
    //   1054: bipush #17
    //   1056: bipush #114
    //   1058: bastore
    //   1059: dup
    //   1060: bipush #18
    //   1062: bipush #22
    //   1064: bastore
    //   1065: dup
    //   1066: bipush #19
    //   1068: bipush #18
    //   1070: bastore
    //   1071: dup
    //   1072: bipush #20
    //   1074: bipush #93
    //   1076: bastore
    //   1077: ldc 'cf2dc3'
    //   1079: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   1082: invokevirtual compareTo : (Ljava/lang/String;)I
    //   1085: ifne -> 1518
    //   1088: invokestatic I丨L : ()LILil/I丨L/ILil/Lil;
    //   1091: invokevirtual l丨Li1LL : ()LILil/I丨L/ILil/丨l丨;
    //   1094: checkcast android/app/INXCoreNative$Stub
    //   1097: astore_1
    //   1098: invokestatic iI : ()LILil/I丨L/ILil/i1;
    //   1101: aload_3
    //   1102: invokevirtual iil1丨 : (Landroid/os/Bundle;)V
    //   1105: new android/os/Bundle
    //   1108: astore_3
    //   1109: aload_3
    //   1110: invokespecial <init> : ()V
    //   1113: bipush #40
    //   1115: newarray byte
    //   1117: dup
    //   1118: iconst_0
    //   1119: iconst_1
    //   1120: bastore
    //   1121: dup
    //   1122: iconst_1
    //   1123: bipush #91
    //   1125: bastore
    //   1126: dup
    //   1127: iconst_2
    //   1128: bipush #95
    //   1130: bastore
    //   1131: dup
    //   1132: iconst_3
    //   1133: bipush #23
    //   1135: bastore
    //   1136: dup
    //   1137: iconst_4
    //   1138: bipush #92
    //   1140: bastore
    //   1141: dup
    //   1142: iconst_5
    //   1143: bipush #74
    //   1145: bastore
    //   1146: dup
    //   1147: bipush #6
    //   1149: bipush #76
    //   1151: bastore
    //   1152: dup
    //   1153: bipush #7
    //   1155: bipush #87
    //   1157: bastore
    //   1158: dup
    //   1159: bipush #8
    //   1161: bipush #93
    //   1163: bastore
    //   1164: dup
    //   1165: bipush #9
    //   1167: bipush #75
    //   1169: bastore
    //   1170: dup
    //   1171: bipush #10
    //   1173: bipush #87
    //   1175: bastore
    //   1176: dup
    //   1177: bipush #11
    //   1179: bipush #28
    //   1181: bastore
    //   1182: dup
    //   1183: bipush #12
    //   1185: bipush #21
    //   1187: bastore
    //   1188: dup
    //   1189: bipush #13
    //   1191: bipush #70
    //   1193: bastore
    //   1194: dup
    //   1195: bipush #14
    //   1197: bipush #83
    //   1199: bastore
    //   1200: dup
    //   1201: bipush #15
    //   1203: bipush #73
    //   1205: bastore
    //   1206: dup
    //   1207: bipush #16
    //   1209: bipush #66
    //   1211: bastore
    //   1212: dup
    //   1213: bipush #17
    //   1215: bipush #87
    //   1217: bastore
    //   1218: dup
    //   1219: bipush #18
    //   1221: bipush #16
    //   1223: bastore
    //   1224: dup
    //   1225: bipush #19
    //   1227: bipush #71
    //   1229: bastore
    //   1230: dup
    //   1231: bipush #20
    //   1233: bipush #28
    //   1235: bastore
    //   1236: dup
    //   1237: bipush #21
    //   1239: bipush #80
    //   1241: bastore
    //   1242: dup
    //   1243: bipush #22
    //   1245: bipush #92
    //   1247: bastore
    //   1248: dup
    //   1249: bipush #23
    //   1251: bipush #70
    //   1253: bastore
    //   1254: dup
    //   1255: bipush #24
    //   1257: bipush #7
    //   1259: bastore
    //   1260: dup
    //   1261: bipush #25
    //   1263: bipush #90
    //   1265: bastore
    //   1266: dup
    //   1267: bipush #26
    //   1269: bipush #70
    //   1271: bastore
    //   1272: dup
    //   1273: bipush #27
    //   1275: bipush #23
    //   1277: bastore
    //   1278: dup
    //   1279: bipush #28
    //   1281: bipush #87
    //   1283: bastore
    //   1284: dup
    //   1285: bipush #29
    //   1287: bipush #74
    //   1289: bastore
    //   1290: dup
    //   1291: bipush #30
    //   1293: bipush #22
    //   1295: bastore
    //   1296: dup
    //   1297: bipush #31
    //   1299: bipush #70
    //   1301: bastore
    //   1302: dup
    //   1303: bipush #32
    //   1305: bipush #83
    //   1307: bastore
    //   1308: dup
    //   1309: bipush #33
    //   1311: bipush #23
    //   1313: bastore
    //   1314: dup
    //   1315: bipush #34
    //   1317: bipush #112
    //   1319: bastore
    //   1320: dup
    //   1321: bipush #35
    //   1323: bipush #123
    //   1325: bastore
    //   1326: dup
    //   1327: bipush #36
    //   1329: bipush #44
    //   1331: bastore
    //   1332: dup
    //   1333: bipush #37
    //   1335: bipush #112
    //   1337: bastore
    //   1338: dup
    //   1339: bipush #38
    //   1341: bipush #119
    //   1343: bastore
    //   1344: dup
    //   1345: bipush #39
    //   1347: bipush #107
    //   1349: bastore
    //   1350: ldc 'b42922'
    //   1352: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   1355: astore_2
    //   1356: new com/main/engine/BinderContainer
    //   1359: astore #4
    //   1361: aload #4
    //   1363: aload_1
    //   1364: invokespecial <init> : (Landroid/os/IBinder;)V
    //   1367: aload_3
    //   1368: aload_2
    //   1369: aload #4
    //   1371: invokevirtual putParcelable : (Ljava/lang/String;Landroid/os/Parcelable;)V
    //   1374: aload_3
    //   1375: bipush #17
    //   1377: newarray byte
    //   1379: dup
    //   1380: iconst_0
    //   1381: bipush #91
    //   1383: bastore
    //   1384: dup
    //   1385: iconst_1
    //   1386: bipush #13
    //   1388: bastore
    //   1389: dup
    //   1390: iconst_2
    //   1391: bipush #93
    //   1393: bastore
    //   1394: dup
    //   1395: iconst_3
    //   1396: bipush #26
    //   1398: bastore
    //   1399: dup
    //   1400: iconst_4
    //   1401: bipush #89
    //   1403: bastore
    //   1404: dup
    //   1405: iconst_5
    //   1406: bipush #26
    //   1408: bastore
    //   1409: dup
    //   1410: bipush #6
    //   1412: bipush #22
    //   1414: bastore
    //   1415: dup
    //   1416: bipush #7
    //   1418: iconst_1
    //   1419: bastore
    //   1420: dup
    //   1421: bipush #8
    //   1423: bipush #95
    //   1425: bastore
    //   1426: dup
    //   1427: bipush #9
    //   1429: bipush #70
    //   1431: bastore
    //   1432: dup
    //   1433: bipush #10
    //   1435: bipush #82
    //   1437: bastore
    //   1438: dup
    //   1439: bipush #11
    //   1441: bipush #76
    //   1443: bastore
    //   1444: dup
    //   1445: bipush #12
    //   1447: bipush #76
    //   1449: bastore
    //   1450: dup
    //   1451: bipush #13
    //   1453: bipush #13
    //   1455: bastore
    //   1456: dup
    //   1457: bipush #14
    //   1459: bipush #91
    //   1461: bastore
    //   1462: dup
    //   1463: bipush #15
    //   1465: bipush #81
    //   1467: bastore
    //   1468: dup
    //   1469: bipush #16
    //   1471: bipush #89
    //   1473: bastore
    //   1474: ldc '8b047b'
    //   1476: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   1479: iconst_5
    //   1480: newarray byte
    //   1482: dup
    //   1483: iconst_0
    //   1484: bipush #93
    //   1486: bastore
    //   1487: dup
    //   1488: iconst_1
    //   1489: iconst_4
    //   1490: bastore
    //   1491: dup
    //   1492: iconst_2
    //   1493: bipush #92
    //   1495: bastore
    //   1496: dup
    //   1497: iconst_3
    //   1498: bipush #93
    //   1500: bastore
    //   1501: dup
    //   1502: iconst_4
    //   1503: bipush #11
    //   1505: bastore
    //   1506: ldc '5a01dd'
    //   1508: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   1511: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
    //   1514: aload_0
    //   1515: monitorexit
    //   1516: aload_3
    //   1517: areturn
    //   1518: aload_1
    //   1519: bipush #20
    //   1521: newarray byte
    //   1523: dup
    //   1524: iconst_0
    //   1525: bipush #66
    //   1527: bastore
    //   1528: dup
    //   1529: iconst_1
    //   1530: bipush #92
    //   1532: bastore
    //   1533: dup
    //   1534: iconst_2
    //   1535: bipush #95
    //   1537: bastore
    //   1538: dup
    //   1539: iconst_3
    //   1540: bipush #85
    //   1542: bastore
    //   1543: dup
    //   1544: iconst_4
    //   1545: bipush #52
    //   1547: bastore
    //   1548: dup
    //   1549: iconst_5
    //   1550: bipush #65
    //   1552: bastore
    //   1553: dup
    //   1554: bipush #6
    //   1556: bipush #84
    //   1558: bastore
    //   1559: dup
    //   1560: bipush #7
    //   1562: bipush #75
    //   1564: bastore
    //   1565: dup
    //   1566: bipush #8
    //   1568: bipush #98
    //   1570: bastore
    //   1571: dup
    //   1572: bipush #9
    //   1574: bipush #84
    //   1576: bastore
    //   1577: dup
    //   1578: bipush #10
    //   1580: bipush #19
    //   1582: bastore
    //   1583: dup
    //   1584: bipush #11
    //   1586: bipush #68
    //   1588: bastore
    //   1589: dup
    //   1590: bipush #12
    //   1592: bipush #88
    //   1594: bastore
    //   1595: dup
    //   1596: bipush #13
    //   1598: bipush #90
    //   1600: bastore
    //   1601: dup
    //   1602: bipush #14
    //   1604: bipush #84
    //   1606: bastore
    //   1607: dup
    //   1608: bipush #15
    //   1610: bipush #98
    //   1612: bastore
    //   1613: dup
    //   1614: bipush #16
    //   1616: bipush #9
    //   1618: bastore
    //   1619: dup
    //   1620: bipush #17
    //   1622: bipush #87
    //   1624: bastore
    //   1625: dup
    //   1626: bipush #18
    //   1628: bipush #93
    //   1630: bastore
    //   1631: dup
    //   1632: bipush #19
    //   1634: bipush #85
    //   1636: bastore
    //   1637: ldc '1911a2'
    //   1639: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   1642: invokevirtual compareTo : (Ljava/lang/String;)I
    //   1645: ifne -> 2062
    //   1648: invokestatic I丨L : ()LILil/I丨L/ILil/Lil;
    //   1651: invokevirtual iI丨LLL1 : ()LILil/I丨L/ILil/iI1i丨I;
    //   1654: astore_1
    //   1655: aload_1
    //   1656: aload_0
    //   1657: invokevirtual getContext : ()Landroid/content/Context;
    //   1660: aload_3
    //   1661: invokevirtual lI丨lii : (Landroid/content/Context;Landroid/os/Bundle;)V
    //   1664: new android/os/Bundle
    //   1667: astore_2
    //   1668: aload_2
    //   1669: invokespecial <init> : ()V
    //   1672: aload_2
    //   1673: bipush #40
    //   1675: newarray byte
    //   1677: dup
    //   1678: iconst_0
    //   1679: bipush #82
    //   1681: bastore
    //   1682: dup
    //   1683: iconst_1
    //   1684: bipush #95
    //   1686: bastore
    //   1687: dup
    //   1688: iconst_2
    //   1689: bipush #14
    //   1691: bastore
    //   1692: dup
    //   1693: iconst_3
    //   1694: bipush #75
    //   1696: bastore
    //   1697: dup
    //   1698: iconst_4
    //   1699: bipush #86
    //   1701: bastore
    //   1702: dup
    //   1703: iconst_5
    //   1704: bipush #30
    //   1706: bastore
    //   1707: dup
    //   1708: bipush #6
    //   1710: bipush #31
    //   1712: bastore
    //   1713: dup
    //   1714: bipush #7
    //   1716: bipush #83
    //   1718: bastore
    //   1719: dup
    //   1720: bipush #8
    //   1722: bipush #12
    //   1724: bastore
    //   1725: dup
    //   1726: bipush #9
    //   1728: bipush #23
    //   1730: bastore
    //   1731: dup
    //   1732: bipush #10
    //   1734: bipush #93
    //   1736: bastore
    //   1737: dup
    //   1738: bipush #11
    //   1740: bipush #72
    //   1742: bastore
    //   1743: dup
    //   1744: bipush #12
    //   1746: bipush #70
    //   1748: bastore
    //   1749: dup
    //   1750: bipush #13
    //   1752: bipush #66
    //   1754: bastore
    //   1755: dup
    //   1756: bipush #14
    //   1758: iconst_2
    //   1759: bastore
    //   1760: dup
    //   1761: bipush #15
    //   1763: bipush #21
    //   1765: bastore
    //   1766: dup
    //   1767: bipush #16
    //   1769: bipush #72
    //   1771: bastore
    //   1772: dup
    //   1773: bipush #17
    //   1775: iconst_3
    //   1776: bastore
    //   1777: dup
    //   1778: bipush #18
    //   1780: bipush #67
    //   1782: bastore
    //   1783: dup
    //   1784: bipush #19
    //   1786: bipush #67
    //   1788: bastore
    //   1789: dup
    //   1790: bipush #20
    //   1792: bipush #77
    //   1794: bastore
    //   1795: dup
    //   1796: bipush #21
    //   1798: bipush #12
    //   1800: bastore
    //   1801: dup
    //   1802: bipush #22
    //   1804: bipush #86
    //   1806: bastore
    //   1807: dup
    //   1808: bipush #23
    //   1810: bipush #18
    //   1812: bastore
    //   1813: dup
    //   1814: bipush #24
    //   1816: bipush #84
    //   1818: bastore
    //   1819: dup
    //   1820: bipush #25
    //   1822: bipush #94
    //   1824: bastore
    //   1825: dup
    //   1826: bipush #26
    //   1828: bipush #23
    //   1830: bastore
    //   1831: dup
    //   1832: bipush #27
    //   1834: bipush #75
    //   1836: bastore
    //   1837: dup
    //   1838: bipush #28
    //   1840: bipush #93
    //   1842: bastore
    //   1843: dup
    //   1844: bipush #29
    //   1846: bipush #30
    //   1848: bastore
    //   1849: dup
    //   1850: bipush #30
    //   1852: bipush #69
    //   1854: bastore
    //   1855: dup
    //   1856: bipush #31
    //   1858: bipush #66
    //   1860: bastore
    //   1861: dup
    //   1862: bipush #32
    //   1864: iconst_2
    //   1865: bastore
    //   1866: dup
    //   1867: bipush #33
    //   1869: bipush #75
    //   1871: bastore
    //   1872: dup
    //   1873: bipush #34
    //   1875: bipush #122
    //   1877: bastore
    //   1878: dup
    //   1879: bipush #35
    //   1881: bipush #47
    //   1883: bastore
    //   1884: dup
    //   1885: bipush #36
    //   1887: bipush #127
    //   1889: bastore
    //   1890: dup
    //   1891: bipush #37
    //   1893: bipush #116
    //   1895: bastore
    //   1896: dup
    //   1897: bipush #38
    //   1899: bipush #38
    //   1901: bastore
    //   1902: dup
    //   1903: bipush #39
    //   1905: bipush #55
    //   1907: bastore
    //   1908: ldc '10ce8f'
    //   1910: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   1913: aload_1
    //   1914: invokevirtual putBinder : (Ljava/lang/String;Landroid/os/IBinder;)V
    //   1917: aload_2
    //   1918: bipush #17
    //   1920: newarray byte
    //   1922: dup
    //   1923: iconst_0
    //   1924: bipush #85
    //   1926: bastore
    //   1927: dup
    //   1928: iconst_1
    //   1929: bipush #95
    //   1931: bastore
    //   1932: dup
    //   1933: iconst_2
    //   1934: bipush #91
    //   1936: bastore
    //   1937: dup
    //   1938: iconst_3
    //   1939: bipush #27
    //   1941: bastore
    //   1942: dup
    //   1943: iconst_4
    //   1944: bipush #95
    //   1946: bastore
    //   1947: dup
    //   1948: iconst_5
    //   1949: bipush #26
    //   1951: bastore
    //   1952: dup
    //   1953: bipush #6
    //   1955: bipush #24
    //   1957: bastore
    //   1958: dup
    //   1959: bipush #7
    //   1961: bipush #83
    //   1963: bastore
    //   1964: dup
    //   1965: bipush #8
    //   1967: bipush #89
    //   1969: bastore
    //   1970: dup
    //   1971: bipush #9
    //   1973: bipush #71
    //   1975: bastore
    //   1976: dup
    //   1977: bipush #10
    //   1979: bipush #84
    //   1981: bastore
    //   1982: dup
    //   1983: bipush #11
    //   1985: bipush #76
    //   1987: bastore
    //   1988: dup
    //   1989: bipush #12
    //   1991: bipush #66
    //   1993: bastore
    //   1994: dup
    //   1995: bipush #13
    //   1997: bipush #95
    //   1999: bastore
    //   2000: dup
    //   2001: bipush #14
    //   2003: bipush #93
    //   2005: bastore
    //   2006: dup
    //   2007: bipush #15
    //   2009: bipush #80
    //   2011: bastore
    //   2012: dup
    //   2013: bipush #16
    //   2015: bipush #95
    //   2017: bastore
    //   2018: ldc '60651b'
    //   2020: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   2023: iconst_5
    //   2024: newarray byte
    //   2026: dup
    //   2027: iconst_0
    //   2028: bipush #92
    //   2030: bastore
    //   2031: dup
    //   2032: iconst_1
    //   2033: iconst_3
    //   2034: bastore
    //   2035: dup
    //   2036: iconst_2
    //   2037: bipush #13
    //   2039: bastore
    //   2040: dup
    //   2041: iconst_3
    //   2042: bipush #10
    //   2044: bastore
    //   2045: dup
    //   2046: iconst_4
    //   2047: bipush #86
    //   2049: bastore
    //   2050: ldc '4faf9c'
    //   2052: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   2055: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
    //   2058: aload_0
    //   2059: monitorexit
    //   2060: aload_2
    //   2061: areturn
    //   2062: aconst_null
    //   2063: astore_1
    //   2064: aload_1
    //   2065: ifnull -> 2494
    //   2068: aload_1
    //   2069: checkcast ILil/I丨L/ILil/丨l丨
    //   2072: aload_0
    //   2073: invokevirtual getContext : ()Landroid/content/Context;
    //   2076: aload_3
    //   2077: invokeinterface Ilil : (Landroid/content/Context;Landroid/os/Bundle;)V
    //   2082: new android/os/Bundle
    //   2085: astore_3
    //   2086: aload_3
    //   2087: invokespecial <init> : ()V
    //   2090: bipush #40
    //   2092: newarray byte
    //   2094: dup
    //   2095: iconst_0
    //   2096: bipush #83
    //   2098: bastore
    //   2099: dup
    //   2100: iconst_1
    //   2101: bipush #14
    //   2103: bastore
    //   2104: dup
    //   2105: iconst_2
    //   2106: bipush #89
    //   2108: bastore
    //   2109: dup
    //   2110: iconst_3
    //   2111: bipush #79
    //   2113: bastore
    //   2114: dup
    //   2115: iconst_4
    //   2116: bipush #8
    //   2118: bastore
    //   2119: dup
    //   2120: iconst_5
    //   2121: bipush #75
    //   2123: bastore
    //   2124: dup
    //   2125: bipush #6
    //   2127: bipush #30
    //   2129: bastore
    //   2130: dup
    //   2131: bipush #7
    //   2133: iconst_2
    //   2134: bastore
    //   2135: dup
    //   2136: bipush #8
    //   2138: bipush #91
    //   2140: bastore
    //   2141: dup
    //   2142: bipush #9
    //   2144: bipush #19
    //   2146: bastore
    //   2147: dup
    //   2148: bipush #10
    //   2150: iconst_3
    //   2151: bastore
    //   2152: dup
    //   2153: bipush #11
    //   2155: bipush #29
    //   2157: bastore
    //   2158: dup
    //   2159: bipush #12
    //   2161: bipush #71
    //   2163: bastore
    //   2164: dup
    //   2165: bipush #13
    //   2167: bipush #19
    //   2169: bastore
    //   2170: dup
    //   2171: bipush #14
    //   2173: bipush #85
    //   2175: bastore
    //   2176: dup
    //   2177: bipush #15
    //   2179: bipush #17
    //   2181: bastore
    //   2182: dup
    //   2183: bipush #16
    //   2185: bipush #22
    //   2187: bastore
    //   2188: dup
    //   2189: bipush #17
    //   2191: bipush #86
    //   2193: bastore
    //   2194: dup
    //   2195: bipush #18
    //   2197: bipush #66
    //   2199: bastore
    //   2200: dup
    //   2201: bipush #19
    //   2203: bipush #18
    //   2205: bastore
    //   2206: dup
    //   2207: bipush #20
    //   2209: bipush #26
    //   2211: bastore
    //   2212: dup
    //   2213: bipush #21
    //   2215: bipush #8
    //   2217: bastore
    //   2218: dup
    //   2219: bipush #22
    //   2221: bipush #8
    //   2223: bastore
    //   2224: dup
    //   2225: bipush #23
    //   2227: bipush #71
    //   2229: bastore
    //   2230: dup
    //   2231: bipush #24
    //   2233: bipush #85
    //   2235: bastore
    //   2236: dup
    //   2237: bipush #25
    //   2239: bipush #15
    //   2241: bastore
    //   2242: dup
    //   2243: bipush #26
    //   2245: bipush #64
    //   2247: bastore
    //   2248: dup
    //   2249: bipush #27
    //   2251: bipush #79
    //   2253: bastore
    //   2254: dup
    //   2255: bipush #28
    //   2257: iconst_3
    //   2258: bastore
    //   2259: dup
    //   2260: bipush #29
    //   2262: bipush #75
    //   2264: bastore
    //   2265: dup
    //   2266: bipush #30
    //   2268: bipush #68
    //   2270: bastore
    //   2271: dup
    //   2272: bipush #31
    //   2274: bipush #19
    //   2276: bastore
    //   2277: dup
    //   2278: bipush #32
    //   2280: bipush #85
    //   2282: bastore
    //   2283: dup
    //   2284: bipush #33
    //   2286: bipush #79
    //   2288: bastore
    //   2289: dup
    //   2290: bipush #34
    //   2292: bipush #36
    //   2294: bastore
    //   2295: dup
    //   2296: bipush #35
    //   2298: bipush #122
    //   2300: bastore
    //   2301: dup
    //   2302: bipush #36
    //   2304: bipush #126
    //   2306: bastore
    //   2307: dup
    //   2308: bipush #37
    //   2310: bipush #37
    //   2312: bastore
    //   2313: dup
    //   2314: bipush #38
    //   2316: bipush #113
    //   2318: bastore
    //   2319: dup
    //   2320: bipush #39
    //   2322: bipush #51
    //   2324: bastore
    //   2325: ldc '0a4af3'
    //   2327: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   2330: astore_2
    //   2331: new com/main/engine/BinderContainer
    //   2334: astore #4
    //   2336: aload #4
    //   2338: aload_1
    //   2339: invokespecial <init> : (Landroid/os/IBinder;)V
    //   2342: aload_3
    //   2343: aload_2
    //   2344: aload #4
    //   2346: invokevirtual putParcelable : (Ljava/lang/String;Landroid/os/Parcelable;)V
    //   2349: aload_3
    //   2350: bipush #17
    //   2352: newarray byte
    //   2354: dup
    //   2355: iconst_0
    //   2356: bipush #91
    //   2358: bastore
    //   2359: dup
    //   2360: iconst_1
    //   2361: bipush #14
    //   2363: bastore
    //   2364: dup
    //   2365: iconst_2
    //   2366: bipush #88
    //   2368: bastore
    //   2369: dup
    //   2370: iconst_3
    //   2371: bipush #30
    //   2373: bastore
    //   2374: dup
    //   2375: iconst_4
    //   2376: bipush #91
    //   2378: bastore
    //   2379: dup
    //   2380: iconst_5
    //   2381: bipush #77
    //   2383: bastore
    //   2384: dup
    //   2385: bipush #6
    //   2387: bipush #22
    //   2389: bastore
    //   2390: dup
    //   2391: bipush #7
    //   2393: iconst_2
    //   2394: bastore
    //   2395: dup
    //   2396: bipush #8
    //   2398: bipush #90
    //   2400: bastore
    //   2401: dup
    //   2402: bipush #9
    //   2404: bipush #66
    //   2406: bastore
    //   2407: dup
    //   2408: bipush #10
    //   2410: bipush #80
    //   2412: bastore
    //   2413: dup
    //   2414: bipush #11
    //   2416: bipush #27
    //   2418: bastore
    //   2419: dup
    //   2420: bipush #12
    //   2422: bipush #76
    //   2424: bastore
    //   2425: dup
    //   2426: bipush #13
    //   2428: bipush #14
    //   2430: bastore
    //   2431: dup
    //   2432: bipush #14
    //   2434: bipush #94
    //   2436: bastore
    //   2437: dup
    //   2438: bipush #15
    //   2440: bipush #85
    //   2442: bastore
    //   2443: dup
    //   2444: bipush #16
    //   2446: bipush #91
    //   2448: bastore
    //   2449: ldc '8a5055'
    //   2451: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   2454: iconst_5
    //   2455: newarray byte
    //   2457: dup
    //   2458: iconst_0
    //   2459: bipush #10
    //   2461: bastore
    //   2462: dup
    //   2463: iconst_1
    //   2464: bipush #93
    //   2466: bastore
    //   2467: dup
    //   2468: iconst_2
    //   2469: bipush #90
    //   2471: bastore
    //   2472: dup
    //   2473: iconst_3
    //   2474: bipush #92
    //   2476: bastore
    //   2477: dup
    //   2478: iconst_4
    //   2479: bipush #95
    //   2481: bastore
    //   2482: ldc 'b8600c'
    //   2484: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
    //   2487: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
    //   2490: aload_0
    //   2491: monitorexit
    //   2492: aload_3
    //   2493: areturn
    //   2494: aload_0
    //   2495: monitorexit
    //   2496: aconst_null
    //   2497: areturn
    //   2498: astore_1
    //   2499: aload_0
    //   2500: monitorexit
    //   2501: aload_1
    //   2502: athrow
    // Exception table:
    //   from	to	target	type
    //   2	119	2498	finally
    //   122	247	2498	finally
    //   250	396	2498	finally
    //   399	952	2498	finally
    //   956	1514	2498	finally
    //   1518	2058	2498	finally
    //   2068	2490	2498	finally
  }
  
  public int delete(@NonNull Uri paramUri, @Nullable String paramString, @Nullable String[] paramArrayOfString) {
    return 0;
  }
  
  @Nullable
  public String getType(@NonNull Uri paramUri) {
    return null;
  }
  
  @Nullable
  public Uri insert(@NonNull Uri paramUri, @Nullable ContentValues paramContentValues) {
    return null;
  }
  
  public boolean onCreate() {
    return false;
  }
  
  @Nullable
  public Cursor query(@NonNull Uri paramUri, @Nullable String[] paramArrayOfString1, @Nullable String paramString1, @Nullable String[] paramArrayOfString2, @Nullable String paramString2) {
    return null;
  }
  
  public int update(@NonNull Uri paramUri, @Nullable ContentValues paramContentValues, @Nullable String paramString, @Nullable String[] paramArrayOfString) {
    return 0;
  }
}
