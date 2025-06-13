package com.mysql.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class ResultSetRow {
  public ExceptionInterceptor exceptionInterceptor;
  
  public Field[] metadata;
  
  public ResultSetRow(ExceptionInterceptor paramExceptionInterceptor) {
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  public abstract void closeOpenStreams();
  
  public abstract InputStream getBinaryInputStream(int paramInt) throws SQLException;
  
  public abstract int getBytesSize();
  
  public abstract byte[] getColumnValue(int paramInt) throws SQLException;
  
  public abstract Date getDateFast(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException;
  
  public final Date getDateFast(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException {
    // Byte code:
    //   0: iload #4
    //   2: istore #8
    //   4: aload_2
    //   5: ifnonnull -> 10
    //   8: aconst_null
    //   9: areturn
    //   10: iconst_0
    //   11: istore #4
    //   13: iload #4
    //   15: iload #8
    //   17: if_icmpge -> 52
    //   20: aload_2
    //   21: iload_3
    //   22: iload #4
    //   24: iadd
    //   25: baload
    //   26: bipush #58
    //   28: if_icmpne -> 37
    //   31: iconst_1
    //   32: istore #4
    //   34: goto -> 55
    //   37: iinc #4, 1
    //   40: goto -> 13
    //   43: astore #5
    //   45: goto -> 991
    //   48: astore_2
    //   49: goto -> 1035
    //   52: iconst_0
    //   53: istore #4
    //   55: iconst_0
    //   56: istore #9
    //   58: iload #9
    //   60: iload #8
    //   62: if_icmpge -> 155
    //   65: aload_2
    //   66: iload_3
    //   67: iload #9
    //   69: iadd
    //   70: baload
    //   71: istore #10
    //   73: iload #10
    //   75: bipush #32
    //   77: if_icmpeq -> 94
    //   80: iload #10
    //   82: bipush #45
    //   84: if_icmpeq -> 94
    //   87: iload #10
    //   89: bipush #47
    //   91: if_icmpne -> 97
    //   94: iconst_0
    //   95: istore #4
    //   97: iload #10
    //   99: bipush #48
    //   101: if_icmpeq -> 149
    //   104: iload #10
    //   106: bipush #32
    //   108: if_icmpeq -> 149
    //   111: iload #10
    //   113: bipush #58
    //   115: if_icmpeq -> 149
    //   118: iload #10
    //   120: bipush #45
    //   122: if_icmpeq -> 149
    //   125: iload #10
    //   127: bipush #47
    //   129: if_icmpeq -> 149
    //   132: iload #10
    //   134: bipush #46
    //   136: if_icmpeq -> 149
    //   139: iconst_0
    //   140: istore #10
    //   142: iload #4
    //   144: istore #9
    //   146: goto -> 162
    //   149: iinc #9, 1
    //   152: goto -> 58
    //   155: iconst_1
    //   156: istore #10
    //   158: iload #4
    //   160: istore #9
    //   162: iconst_0
    //   163: istore #4
    //   165: iload #4
    //   167: iload #8
    //   169: if_icmpge -> 192
    //   172: aload_2
    //   173: iload_3
    //   174: iload #4
    //   176: iadd
    //   177: baload
    //   178: bipush #46
    //   180: if_icmpne -> 186
    //   183: goto -> 195
    //   186: iinc #4, 1
    //   189: goto -> 165
    //   192: iconst_m1
    //   193: istore #4
    //   195: iload #4
    //   197: iconst_m1
    //   198: if_icmple -> 205
    //   201: iload #4
    //   203: istore #8
    //   205: iload #9
    //   207: ifne -> 309
    //   210: iload #10
    //   212: ifeq -> 309
    //   215: ldc 'convertToNull'
    //   217: aload #5
    //   219: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   224: invokevirtual equals : (Ljava/lang/Object;)Z
    //   227: ifeq -> 232
    //   230: aconst_null
    //   231: areturn
    //   232: ldc 'exception'
    //   234: aload #5
    //   236: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   241: invokevirtual equals : (Ljava/lang/Object;)Z
    //   244: ifne -> 258
    //   247: aload #6
    //   249: aload #7
    //   251: iconst_1
    //   252: iconst_1
    //   253: iconst_1
    //   254: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   257: areturn
    //   258: new java/lang/StringBuilder
    //   261: astore #5
    //   263: aload #5
    //   265: invokespecial <init> : ()V
    //   268: aload #5
    //   270: ldc 'Value ''
    //   272: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   275: pop
    //   276: aload #5
    //   278: aload_2
    //   279: invokestatic toString : ([B)Ljava/lang/String;
    //   282: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload #5
    //   288: ldc '' can not be represented as java.sql.Date'
    //   290: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: pop
    //   294: aload #5
    //   296: invokevirtual toString : ()Ljava/lang/String;
    //   299: ldc 'S1009'
    //   301: aload_0
    //   302: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   305: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   308: athrow
    //   309: aload_0
    //   310: getfield metadata : [Lcom/mysql/jdbc/Field;
    //   313: iload_1
    //   314: aaload
    //   315: invokevirtual getMysqlType : ()I
    //   318: istore #4
    //   320: iload #4
    //   322: bipush #7
    //   324: if_icmpne -> 696
    //   327: iload #8
    //   329: iconst_2
    //   330: if_icmpeq -> 653
    //   333: iload #8
    //   335: iconst_4
    //   336: if_icmpeq -> 595
    //   339: iload #8
    //   341: bipush #6
    //   343: if_icmpeq -> 524
    //   346: iload #8
    //   348: bipush #8
    //   350: if_icmpeq -> 474
    //   353: iload #8
    //   355: bipush #10
    //   357: if_icmpeq -> 524
    //   360: iload #8
    //   362: bipush #12
    //   364: if_icmpeq -> 524
    //   367: iload #8
    //   369: bipush #14
    //   371: if_icmpeq -> 474
    //   374: iload #8
    //   376: bipush #19
    //   378: if_icmpeq -> 433
    //   381: iload #8
    //   383: bipush #21
    //   385: if_icmpeq -> 433
    //   388: iload #8
    //   390: bipush #29
    //   392: if_icmpne -> 398
    //   395: goto -> 433
    //   398: ldc 'ResultSet.Bad_format_for_Date'
    //   400: iconst_2
    //   401: anewarray java/lang/Object
    //   404: dup
    //   405: iconst_0
    //   406: aload_2
    //   407: invokestatic toString : ([B)Ljava/lang/String;
    //   410: aastore
    //   411: dup
    //   412: iconst_1
    //   413: iload_1
    //   414: iconst_1
    //   415: iadd
    //   416: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   419: aastore
    //   420: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   423: ldc 'S1009'
    //   425: aload_0
    //   426: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   429: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   432: athrow
    //   433: aload #6
    //   435: aload #7
    //   437: aload_2
    //   438: iload_3
    //   439: iconst_0
    //   440: iadd
    //   441: iload_3
    //   442: iconst_4
    //   443: iadd
    //   444: invokestatic getInt : ([BII)I
    //   447: aload_2
    //   448: iload_3
    //   449: iconst_5
    //   450: iadd
    //   451: iload_3
    //   452: bipush #7
    //   454: iadd
    //   455: invokestatic getInt : ([BII)I
    //   458: aload_2
    //   459: iload_3
    //   460: bipush #8
    //   462: iadd
    //   463: iload_3
    //   464: bipush #10
    //   466: iadd
    //   467: invokestatic getInt : ([BII)I
    //   470: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   473: areturn
    //   474: iload_3
    //   475: iconst_4
    //   476: iadd
    //   477: istore #4
    //   479: aload_2
    //   480: iload_3
    //   481: iconst_0
    //   482: iadd
    //   483: iload #4
    //   485: invokestatic getInt : ([BII)I
    //   488: istore #9
    //   490: iload_3
    //   491: bipush #6
    //   493: iadd
    //   494: istore #8
    //   496: aload #6
    //   498: aload #7
    //   500: iload #9
    //   502: aload_2
    //   503: iload #4
    //   505: iload #8
    //   507: invokestatic getInt : ([BII)I
    //   510: aload_2
    //   511: iload #8
    //   513: iload_3
    //   514: bipush #8
    //   516: iadd
    //   517: invokestatic getInt : ([BII)I
    //   520: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   523: areturn
    //   524: iload_3
    //   525: iconst_2
    //   526: iadd
    //   527: istore #9
    //   529: aload_2
    //   530: iload_3
    //   531: iconst_0
    //   532: iadd
    //   533: iload #9
    //   535: invokestatic getInt : ([BII)I
    //   538: istore #8
    //   540: iload #8
    //   542: istore #4
    //   544: iload #8
    //   546: bipush #69
    //   548: if_icmpgt -> 558
    //   551: iload #8
    //   553: bipush #100
    //   555: iadd
    //   556: istore #4
    //   558: iload_3
    //   559: iconst_4
    //   560: iadd
    //   561: istore #8
    //   563: aload #6
    //   565: aload #7
    //   567: iload #4
    //   569: sipush #1900
    //   572: iadd
    //   573: aload_2
    //   574: iload #9
    //   576: iload #8
    //   578: invokestatic getInt : ([BII)I
    //   581: aload_2
    //   582: iload #8
    //   584: iload_3
    //   585: bipush #6
    //   587: iadd
    //   588: invokestatic getInt : ([BII)I
    //   591: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   594: areturn
    //   595: iload_3
    //   596: iconst_4
    //   597: iadd
    //   598: istore #9
    //   600: aload_2
    //   601: iload_3
    //   602: iconst_0
    //   603: iadd
    //   604: iload #9
    //   606: invokestatic getInt : ([BII)I
    //   609: istore #8
    //   611: iload #8
    //   613: istore #4
    //   615: iload #8
    //   617: bipush #69
    //   619: if_icmpgt -> 629
    //   622: iload #8
    //   624: bipush #100
    //   626: iadd
    //   627: istore #4
    //   629: aload #6
    //   631: aload #7
    //   633: iload #4
    //   635: sipush #1900
    //   638: iadd
    //   639: aload_2
    //   640: iload_3
    //   641: iconst_2
    //   642: iadd
    //   643: iload #9
    //   645: invokestatic getInt : ([BII)I
    //   648: iconst_1
    //   649: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   652: areturn
    //   653: aload_2
    //   654: iload_3
    //   655: iconst_0
    //   656: iadd
    //   657: iload_3
    //   658: iconst_2
    //   659: iadd
    //   660: invokestatic getInt : ([BII)I
    //   663: istore #4
    //   665: iload #4
    //   667: istore_3
    //   668: iload #4
    //   670: bipush #69
    //   672: if_icmpgt -> 681
    //   675: iload #4
    //   677: bipush #100
    //   679: iadd
    //   680: istore_3
    //   681: aload #6
    //   683: aload #7
    //   685: iload_3
    //   686: sipush #1900
    //   689: iadd
    //   690: iconst_1
    //   691: iconst_1
    //   692: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   695: areturn
    //   696: aload_0
    //   697: getfield metadata : [Lcom/mysql/jdbc/Field;
    //   700: iload_1
    //   701: aaload
    //   702: invokevirtual getMysqlType : ()I
    //   705: bipush #13
    //   707: if_icmpne -> 783
    //   710: iload #8
    //   712: iconst_2
    //   713: if_icmpeq -> 739
    //   716: iload #8
    //   718: iconst_1
    //   719: if_icmpne -> 725
    //   722: goto -> 739
    //   725: aload_2
    //   726: iload_3
    //   727: iconst_0
    //   728: iadd
    //   729: iload_3
    //   730: iconst_4
    //   731: iadd
    //   732: invokestatic getInt : ([BII)I
    //   735: istore_3
    //   736: goto -> 772
    //   739: aload_2
    //   740: iload_3
    //   741: iload #8
    //   743: iload_3
    //   744: iadd
    //   745: invokestatic getInt : ([BII)I
    //   748: istore #4
    //   750: iload #4
    //   752: istore_3
    //   753: iload #4
    //   755: bipush #69
    //   757: if_icmpgt -> 766
    //   760: iload #4
    //   762: bipush #100
    //   764: iadd
    //   765: istore_3
    //   766: wide iinc #3 1900
    //   772: aload #6
    //   774: aload #7
    //   776: iload_3
    //   777: iconst_1
    //   778: iconst_1
    //   779: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   782: areturn
    //   783: aload_0
    //   784: getfield metadata : [Lcom/mysql/jdbc/Field;
    //   787: iload_1
    //   788: aaload
    //   789: invokevirtual getMysqlType : ()I
    //   792: bipush #11
    //   794: if_icmpne -> 810
    //   797: aload #6
    //   799: aload #7
    //   801: sipush #1970
    //   804: iconst_1
    //   805: iconst_1
    //   806: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   809: areturn
    //   810: iload #8
    //   812: bipush #10
    //   814: if_icmpge -> 876
    //   817: iload #8
    //   819: bipush #8
    //   821: if_icmpne -> 841
    //   824: aload #6
    //   826: aload #7
    //   828: sipush #1970
    //   831: iconst_1
    //   832: iconst_1
    //   833: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   836: astore #5
    //   838: aload #5
    //   840: areturn
    //   841: ldc 'ResultSet.Bad_format_for_Date'
    //   843: iconst_2
    //   844: anewarray java/lang/Object
    //   847: dup
    //   848: iconst_0
    //   849: aload_2
    //   850: invokestatic toString : ([B)Ljava/lang/String;
    //   853: aastore
    //   854: dup
    //   855: iconst_1
    //   856: iload_1
    //   857: iconst_1
    //   858: iadd
    //   859: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   862: aastore
    //   863: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   866: ldc 'S1009'
    //   868: aload_0
    //   869: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   872: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   875: athrow
    //   876: iload #8
    //   878: bipush #18
    //   880: if_icmpeq -> 924
    //   883: aload_2
    //   884: iload_3
    //   885: iconst_0
    //   886: iadd
    //   887: iload_3
    //   888: iconst_4
    //   889: iadd
    //   890: invokestatic getInt : ([BII)I
    //   893: istore #8
    //   895: aload_2
    //   896: iload_3
    //   897: iconst_5
    //   898: iadd
    //   899: iload_3
    //   900: bipush #7
    //   902: iadd
    //   903: invokestatic getInt : ([BII)I
    //   906: istore #4
    //   908: aload_2
    //   909: iload_3
    //   910: bipush #8
    //   912: iadd
    //   913: iload_3
    //   914: bipush #10
    //   916: iadd
    //   917: invokestatic getInt : ([BII)I
    //   920: istore_3
    //   921: goto -> 974
    //   924: new java/util/StringTokenizer
    //   927: astore #5
    //   929: aload #5
    //   931: aload_2
    //   932: iload_3
    //   933: iload #8
    //   935: ldc 'ISO8859_1'
    //   937: invokestatic toString : ([BIILjava/lang/String;)Ljava/lang/String;
    //   940: ldc '- '
    //   942: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   945: aload #5
    //   947: invokevirtual nextToken : ()Ljava/lang/String;
    //   950: invokestatic parseInt : (Ljava/lang/String;)I
    //   953: istore #8
    //   955: aload #5
    //   957: invokevirtual nextToken : ()Ljava/lang/String;
    //   960: invokestatic parseInt : (Ljava/lang/String;)I
    //   963: istore #4
    //   965: aload #5
    //   967: invokevirtual nextToken : ()Ljava/lang/String;
    //   970: invokestatic parseInt : (Ljava/lang/String;)I
    //   973: istore_3
    //   974: aload #6
    //   976: aload #7
    //   978: iload #8
    //   980: iload #4
    //   982: iload_3
    //   983: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   986: astore #5
    //   988: aload #5
    //   990: areturn
    //   991: ldc 'ResultSet.Bad_format_for_Date'
    //   993: iconst_2
    //   994: anewarray java/lang/Object
    //   997: dup
    //   998: iconst_0
    //   999: aload_2
    //   1000: invokestatic toString : ([B)Ljava/lang/String;
    //   1003: aastore
    //   1004: dup
    //   1005: iconst_1
    //   1006: iload_1
    //   1007: iconst_1
    //   1008: iadd
    //   1009: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1012: aastore
    //   1013: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1016: ldc 'S1009'
    //   1018: aload_0
    //   1019: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1022: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1025: astore_2
    //   1026: aload_2
    //   1027: aload #5
    //   1029: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1032: pop
    //   1033: aload_2
    //   1034: athrow
    //   1035: aload_2
    //   1036: athrow
    // Exception table:
    //   from	to	target	type
    //   215	230	48	java/sql/SQLException
    //   215	230	43	java/lang/Exception
    //   232	258	48	java/sql/SQLException
    //   232	258	43	java/lang/Exception
    //   258	309	48	java/sql/SQLException
    //   258	309	43	java/lang/Exception
    //   309	320	48	java/sql/SQLException
    //   309	320	43	java/lang/Exception
    //   398	433	48	java/sql/SQLException
    //   398	433	43	java/lang/Exception
    //   433	474	48	java/sql/SQLException
    //   433	474	43	java/lang/Exception
    //   479	490	48	java/sql/SQLException
    //   479	490	43	java/lang/Exception
    //   496	524	48	java/sql/SQLException
    //   496	524	43	java/lang/Exception
    //   529	540	48	java/sql/SQLException
    //   529	540	43	java/lang/Exception
    //   563	595	48	java/sql/SQLException
    //   563	595	43	java/lang/Exception
    //   600	611	48	java/sql/SQLException
    //   600	611	43	java/lang/Exception
    //   629	653	48	java/sql/SQLException
    //   629	653	43	java/lang/Exception
    //   653	665	48	java/sql/SQLException
    //   653	665	43	java/lang/Exception
    //   681	696	48	java/sql/SQLException
    //   681	696	43	java/lang/Exception
    //   696	710	48	java/sql/SQLException
    //   696	710	43	java/lang/Exception
    //   725	736	48	java/sql/SQLException
    //   725	736	43	java/lang/Exception
    //   739	750	48	java/sql/SQLException
    //   739	750	43	java/lang/Exception
    //   772	783	48	java/sql/SQLException
    //   772	783	43	java/lang/Exception
    //   783	810	48	java/sql/SQLException
    //   783	810	43	java/lang/Exception
    //   824	838	48	java/sql/SQLException
    //   824	838	43	java/lang/Exception
    //   841	876	48	java/sql/SQLException
    //   841	876	43	java/lang/Exception
    //   883	921	48	java/sql/SQLException
    //   883	921	43	java/lang/Exception
    //   924	974	48	java/sql/SQLException
    //   924	974	43	java/lang/Exception
    //   974	988	48	java/sql/SQLException
    //   974	988	43	java/lang/Exception
  }
  
  public abstract int getInt(int paramInt) throws SQLException;
  
  public abstract long getLong(int paramInt) throws SQLException;
  
  public abstract Date getNativeDate(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException;
  
  public Date getNativeDate(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException {
    // Byte code:
    //   0: iconst_0
    //   1: istore #8
    //   3: iload #4
    //   5: ifeq -> 51
    //   8: aload_2
    //   9: iload_3
    //   10: iconst_0
    //   11: iadd
    //   12: baload
    //   13: sipush #255
    //   16: iand
    //   17: aload_2
    //   18: iload_3
    //   19: iconst_1
    //   20: iadd
    //   21: baload
    //   22: sipush #255
    //   25: iand
    //   26: bipush #8
    //   28: ishl
    //   29: ior
    //   30: istore #8
    //   32: aload_2
    //   33: iload_3
    //   34: iconst_2
    //   35: iadd
    //   36: baload
    //   37: istore #9
    //   39: aload_2
    //   40: iload_3
    //   41: iconst_3
    //   42: iadd
    //   43: baload
    //   44: istore_1
    //   45: iload #9
    //   47: istore_3
    //   48: goto -> 55
    //   51: iconst_0
    //   52: istore_1
    //   53: iconst_0
    //   54: istore_3
    //   55: iload #4
    //   57: ifeq -> 103
    //   60: iload_3
    //   61: istore #10
    //   63: iload #8
    //   65: istore #9
    //   67: iload_1
    //   68: istore #4
    //   70: iload #8
    //   72: ifne -> 144
    //   75: iload_3
    //   76: istore #10
    //   78: iload #8
    //   80: istore #9
    //   82: iload_1
    //   83: istore #4
    //   85: iload_3
    //   86: ifne -> 144
    //   89: iload_3
    //   90: istore #10
    //   92: iload #8
    //   94: istore #9
    //   96: iload_1
    //   97: istore #4
    //   99: iload_1
    //   100: ifne -> 144
    //   103: ldc 'convertToNull'
    //   105: aload #5
    //   107: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   112: invokevirtual equals : (Ljava/lang/Object;)Z
    //   115: ifeq -> 120
    //   118: aconst_null
    //   119: areturn
    //   120: ldc 'exception'
    //   122: aload #5
    //   124: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   129: invokevirtual equals : (Ljava/lang/Object;)Z
    //   132: ifne -> 191
    //   135: iconst_1
    //   136: istore #9
    //   138: iconst_1
    //   139: istore #4
    //   141: iconst_1
    //   142: istore #10
    //   144: aload #6
    //   146: getfield useLegacyDatetimeCode : Z
    //   149: ifne -> 164
    //   152: iload #9
    //   154: iload #10
    //   156: iload #4
    //   158: aload #7
    //   160: invokestatic fastDateCreate : (IIILjava/util/Calendar;)Ljava/sql/Date;
    //   163: areturn
    //   164: aload #7
    //   166: astore_2
    //   167: aload #7
    //   169: ifnonnull -> 178
    //   172: aload #6
    //   174: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   177: astore_2
    //   178: aload #6
    //   180: aload_2
    //   181: iload #9
    //   183: iload #10
    //   185: iload #4
    //   187: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   190: areturn
    //   191: ldc 'Value '0000-00-00' can not be represented as java.sql.Date'
    //   193: ldc 'S1009'
    //   195: aload_0
    //   196: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   199: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   202: athrow
  }
  
  public abstract Object getNativeDateTimeValue(int paramInt1, Calendar paramCalendar, int paramInt2, int paramInt3, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException;
  
  public Object getNativeDateTimeValue(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, Calendar paramCalendar, int paramInt4, int paramInt5, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: aload #10
    //   8: invokeinterface getUseJDBCCompliantTimezoneShift : ()Z
    //   13: ifeq -> 28
    //   16: aload #10
    //   18: invokeinterface getUtcCalendar : ()Ljava/util/Calendar;
    //   23: astore #20
    //   25: goto -> 35
    //   28: aload #11
    //   30: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   33: astore #20
    //   35: iconst_0
    //   36: istore #13
    //   38: iload #7
    //   40: bipush #7
    //   42: if_icmpeq -> 212
    //   45: iload #7
    //   47: tableswitch default -> 72, 10 -> 140, 11 -> 75, 12 -> 212
    //   72: goto -> 397
    //   75: iload #4
    //   77: ifeq -> 105
    //   80: aload_2
    //   81: iload_3
    //   82: iconst_5
    //   83: iadd
    //   84: baload
    //   85: istore #7
    //   87: aload_2
    //   88: iload_3
    //   89: bipush #6
    //   91: iadd
    //   92: baload
    //   93: istore #4
    //   95: aload_2
    //   96: iload_3
    //   97: bipush #7
    //   99: iadd
    //   100: baload
    //   101: istore_3
    //   102: goto -> 113
    //   105: iconst_0
    //   106: istore #7
    //   108: iconst_0
    //   109: istore #4
    //   111: iconst_0
    //   112: istore_3
    //   113: iload #7
    //   115: istore #15
    //   117: iload #4
    //   119: istore #14
    //   121: iload_3
    //   122: istore #12
    //   124: sipush #1970
    //   127: istore #4
    //   129: iconst_1
    //   130: istore #13
    //   132: iconst_1
    //   133: istore_3
    //   134: iconst_1
    //   135: istore #7
    //   137: goto -> 414
    //   140: iload #4
    //   142: ifeq -> 394
    //   145: aload_2
    //   146: iload_3
    //   147: iconst_0
    //   148: iadd
    //   149: baload
    //   150: istore #17
    //   152: aload_2
    //   153: iload_3
    //   154: iconst_1
    //   155: iadd
    //   156: baload
    //   157: istore #18
    //   159: aload_2
    //   160: iload_3
    //   161: iconst_2
    //   162: iadd
    //   163: baload
    //   164: istore #4
    //   166: aload_2
    //   167: iload_3
    //   168: iconst_3
    //   169: iadd
    //   170: baload
    //   171: istore #7
    //   173: iload #4
    //   175: istore_3
    //   176: iconst_1
    //   177: istore #13
    //   179: iconst_0
    //   180: istore #15
    //   182: iconst_0
    //   183: istore #14
    //   185: iconst_0
    //   186: istore #12
    //   188: iconst_0
    //   189: istore #16
    //   191: iload #17
    //   193: sipush #255
    //   196: iand
    //   197: iload #18
    //   199: sipush #255
    //   202: iand
    //   203: bipush #8
    //   205: ishl
    //   206: ior
    //   207: istore #4
    //   209: goto -> 417
    //   212: iload #4
    //   214: ifeq -> 394
    //   217: aload_2
    //   218: iload_3
    //   219: iconst_0
    //   220: iadd
    //   221: baload
    //   222: sipush #255
    //   225: iand
    //   226: aload_2
    //   227: iload_3
    //   228: iconst_1
    //   229: iadd
    //   230: baload
    //   231: sipush #255
    //   234: iand
    //   235: bipush #8
    //   237: ishl
    //   238: ior
    //   239: istore #13
    //   241: aload_2
    //   242: iload_3
    //   243: iconst_2
    //   244: iadd
    //   245: baload
    //   246: istore #16
    //   248: aload_2
    //   249: iload_3
    //   250: iconst_3
    //   251: iadd
    //   252: baload
    //   253: istore #7
    //   255: iload #4
    //   257: iconst_4
    //   258: if_icmple -> 286
    //   261: aload_2
    //   262: iload_3
    //   263: iconst_4
    //   264: iadd
    //   265: baload
    //   266: istore #15
    //   268: aload_2
    //   269: iload_3
    //   270: iconst_5
    //   271: iadd
    //   272: baload
    //   273: istore #14
    //   275: aload_2
    //   276: iload_3
    //   277: bipush #6
    //   279: iadd
    //   280: baload
    //   281: istore #12
    //   283: goto -> 295
    //   286: iconst_0
    //   287: istore #15
    //   289: iconst_0
    //   290: istore #14
    //   292: iconst_0
    //   293: istore #12
    //   295: iload #4
    //   297: bipush #7
    //   299: if_icmple -> 379
    //   302: aload_2
    //   303: iload_3
    //   304: bipush #7
    //   306: iadd
    //   307: baload
    //   308: sipush #255
    //   311: iand
    //   312: aload_2
    //   313: iload_3
    //   314: bipush #8
    //   316: iadd
    //   317: baload
    //   318: sipush #255
    //   321: iand
    //   322: bipush #8
    //   324: ishl
    //   325: ior
    //   326: aload_2
    //   327: iload_3
    //   328: bipush #9
    //   330: iadd
    //   331: baload
    //   332: sipush #255
    //   335: iand
    //   336: bipush #16
    //   338: ishl
    //   339: ior
    //   340: aload_2
    //   341: iload_3
    //   342: bipush #10
    //   344: iadd
    //   345: baload
    //   346: sipush #255
    //   349: iand
    //   350: bipush #24
    //   352: ishl
    //   353: ior
    //   354: sipush #1000
    //   357: imul
    //   358: istore #17
    //   360: iconst_1
    //   361: istore_3
    //   362: iload #13
    //   364: istore #4
    //   366: iload_3
    //   367: istore #13
    //   369: iload #16
    //   371: istore_3
    //   372: iload #17
    //   374: istore #16
    //   376: goto -> 417
    //   379: iconst_1
    //   380: istore_3
    //   381: iload #13
    //   383: istore #4
    //   385: iload_3
    //   386: istore #13
    //   388: iload #16
    //   390: istore_3
    //   391: goto -> 414
    //   394: iconst_1
    //   395: istore #13
    //   397: iconst_0
    //   398: istore #4
    //   400: iconst_0
    //   401: istore_3
    //   402: iconst_0
    //   403: istore #7
    //   405: iconst_0
    //   406: istore #15
    //   408: iconst_0
    //   409: istore #14
    //   411: iconst_0
    //   412: istore #12
    //   414: iconst_0
    //   415: istore #16
    //   417: iload #6
    //   419: tableswitch default -> 444, 91 -> 710, 92 -> 627, 93 -> 456
    //   444: new java/sql/SQLException
    //   447: dup
    //   448: ldc 'Internal error - conversion method doesn't support this type'
    //   450: ldc 'S1000'
    //   452: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   455: athrow
    //   456: iload #13
    //   458: ifeq -> 612
    //   461: iload #4
    //   463: ifne -> 529
    //   466: iload_3
    //   467: ifne -> 529
    //   470: iload #7
    //   472: ifne -> 529
    //   475: ldc 'convertToNull'
    //   477: aload #10
    //   479: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   484: invokevirtual equals : (Ljava/lang/Object;)Z
    //   487: ifeq -> 492
    //   490: aconst_null
    //   491: areturn
    //   492: ldc 'exception'
    //   494: aload #10
    //   496: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   501: invokevirtual equals : (Ljava/lang/Object;)Z
    //   504: ifne -> 517
    //   507: iconst_1
    //   508: istore_1
    //   509: iconst_1
    //   510: istore_3
    //   511: iconst_1
    //   512: istore #7
    //   514: goto -> 532
    //   517: new java/sql/SQLException
    //   520: dup
    //   521: ldc 'Value '0000-00-00' can not be represented as java.sql.Timestamp'
    //   523: ldc 'S1009'
    //   525: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   528: athrow
    //   529: iload #4
    //   531: istore_1
    //   532: aload #11
    //   534: getfield useLegacyDatetimeCode : Z
    //   537: ifne -> 558
    //   540: aload #8
    //   542: iload_1
    //   543: iload_3
    //   544: iload #7
    //   546: iload #15
    //   548: iload #14
    //   550: iload #12
    //   552: iload #16
    //   554: invokestatic fastTimestampCreate : (Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   557: areturn
    //   558: aload #10
    //   560: invokeinterface getUseGmtMillisForDatetimes : ()Z
    //   565: istore #19
    //   567: aload #10
    //   569: aload #20
    //   571: aload #5
    //   573: aload #11
    //   575: aload #11
    //   577: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   580: iload_1
    //   581: iload_3
    //   582: iload #7
    //   584: iload #15
    //   586: iload #14
    //   588: iload #12
    //   590: iload #16
    //   592: iload #19
    //   594: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   597: aload #10
    //   599: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   604: aload #8
    //   606: iload #9
    //   608: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   611: areturn
    //   612: aload #11
    //   614: iload_1
    //   615: iconst_1
    //   616: iadd
    //   617: aload #5
    //   619: aload #8
    //   621: iload #9
    //   623: invokevirtual getNativeTimestampViaParseConversion : (ILjava/util/Calendar;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   626: areturn
    //   627: iload #13
    //   629: ifeq -> 695
    //   632: aload #11
    //   634: getfield useLegacyDatetimeCode : Z
    //   637: ifne -> 656
    //   640: iload #15
    //   642: iload #14
    //   644: iload #12
    //   646: aload #5
    //   648: aload_0
    //   649: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   652: invokestatic fastTimeCreate : (IIILjava/util/Calendar;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/Time;
    //   655: areturn
    //   656: aload #10
    //   658: aload #20
    //   660: aload #5
    //   662: aload #11
    //   664: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   667: iload #15
    //   669: iload #14
    //   671: iload #12
    //   673: aload_0
    //   674: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   677: invokestatic fastTimeCreate : (Ljava/util/Calendar;IIILcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/Time;
    //   680: aload #10
    //   682: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   687: aload #8
    //   689: iload #9
    //   691: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
    //   694: areturn
    //   695: aload #11
    //   697: iload_1
    //   698: iconst_1
    //   699: iadd
    //   700: aload #5
    //   702: aload #8
    //   704: iload #9
    //   706: invokevirtual getNativeTimeViaParseConversion : (ILjava/util/Calendar;Ljava/util/TimeZone;Z)Ljava/sql/Time;
    //   709: areturn
    //   710: iload #13
    //   712: ifeq -> 819
    //   715: iload #4
    //   717: ifne -> 784
    //   720: iload_3
    //   721: ifne -> 784
    //   724: iload #7
    //   726: ifne -> 784
    //   729: ldc 'convertToNull'
    //   731: aload #10
    //   733: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   738: invokevirtual equals : (Ljava/lang/Object;)Z
    //   741: ifeq -> 746
    //   744: aconst_null
    //   745: areturn
    //   746: ldc 'exception'
    //   748: aload #10
    //   750: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   755: invokevirtual equals : (Ljava/lang/Object;)Z
    //   758: ifne -> 772
    //   761: iconst_1
    //   762: istore #4
    //   764: iconst_1
    //   765: istore_3
    //   766: iconst_1
    //   767: istore #7
    //   769: goto -> 784
    //   772: new java/sql/SQLException
    //   775: dup
    //   776: ldc 'Value '0000-00-00' can not be represented as java.sql.Date'
    //   778: ldc 'S1009'
    //   780: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   783: athrow
    //   784: aload #11
    //   786: getfield useLegacyDatetimeCode : Z
    //   789: ifne -> 803
    //   792: iload #4
    //   794: iload_3
    //   795: iload #7
    //   797: aload #5
    //   799: invokestatic fastDateCreate : (IIILjava/util/Calendar;)Ljava/sql/Date;
    //   802: areturn
    //   803: aload #11
    //   805: aload #11
    //   807: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   810: iload #4
    //   812: iload_3
    //   813: iload #7
    //   815: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   818: areturn
    //   819: aload #11
    //   821: iload_1
    //   822: iconst_1
    //   823: iadd
    //   824: invokevirtual getNativeDateViaParseConversion : (I)Ljava/sql/Date;
    //   827: areturn
  }
  
  public abstract double getNativeDouble(int paramInt) throws SQLException;
  
  public double getNativeDouble(byte[] paramArrayOfbyte, int paramInt) {
    long l1 = (paramArrayOfbyte[paramInt + 0] & 0xFF);
    long l4 = (paramArrayOfbyte[paramInt + 1] & 0xFF);
    long l5 = (paramArrayOfbyte[paramInt + 2] & 0xFF);
    long l7 = (paramArrayOfbyte[paramInt + 3] & 0xFF);
    long l2 = (paramArrayOfbyte[paramInt + 4] & 0xFF);
    long l6 = (paramArrayOfbyte[paramInt + 5] & 0xFF);
    long l3 = (paramArrayOfbyte[paramInt + 6] & 0xFF);
    return Double.longBitsToDouble((paramArrayOfbyte[paramInt + 7] & 0xFF) << 56L | l1 | l4 << 8L | l5 << 16L | l7 << 24L | l2 << 32L | l6 << 40L | l3 << 48L);
  }
  
  public abstract float getNativeFloat(int paramInt) throws SQLException;
  
  public float getNativeFloat(byte[] paramArrayOfbyte, int paramInt) {
    byte b2 = paramArrayOfbyte[paramInt + 0];
    byte b1 = paramArrayOfbyte[paramInt + 1];
    byte b3 = paramArrayOfbyte[paramInt + 2];
    return Float.intBitsToFloat((paramArrayOfbyte[paramInt + 3] & 0xFF) << 24 | b2 & 0xFF | (b1 & 0xFF) << 8 | (b3 & 0xFF) << 16);
  }
  
  public abstract int getNativeInt(int paramInt) throws SQLException;
  
  public int getNativeInt(byte[] paramArrayOfbyte, int paramInt) {
    byte b2 = paramArrayOfbyte[paramInt + 0];
    byte b1 = paramArrayOfbyte[paramInt + 1];
    byte b3 = paramArrayOfbyte[paramInt + 2];
    return (paramArrayOfbyte[paramInt + 3] & 0xFF) << 24 | b2 & 0xFF | (b1 & 0xFF) << 8 | (b3 & 0xFF) << 16;
  }
  
  public abstract long getNativeLong(int paramInt) throws SQLException;
  
  public long getNativeLong(byte[] paramArrayOfbyte, int paramInt) {
    long l4 = (paramArrayOfbyte[paramInt + 0] & 0xFF);
    long l6 = (paramArrayOfbyte[paramInt + 1] & 0xFF);
    long l2 = (paramArrayOfbyte[paramInt + 2] & 0xFF);
    long l7 = (paramArrayOfbyte[paramInt + 3] & 0xFF);
    long l5 = (paramArrayOfbyte[paramInt + 4] & 0xFF);
    long l1 = (paramArrayOfbyte[paramInt + 5] & 0xFF);
    long l3 = (paramArrayOfbyte[paramInt + 6] & 0xFF);
    return (paramArrayOfbyte[paramInt + 7] & 0xFF) << 56L | l4 | l6 << 8L | l2 << 16L | l7 << 24L | l5 << 32L | l1 << 40L | l3 << 48L;
  }
  
  public abstract short getNativeShort(int paramInt) throws SQLException;
  
  public short getNativeShort(byte[] paramArrayOfbyte, int paramInt) {
    byte b = paramArrayOfbyte[paramInt + 0];
    return (short)((paramArrayOfbyte[paramInt + 1] & 0xFF) << 8 | b & 0xFF);
  }
  
  public abstract Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException;
  
  public Time getNativeTime(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    byte b = 0;
    if (paramInt3 != 0) {
      b = paramArrayOfbyte[paramInt2 + 5];
      paramInt1 = paramArrayOfbyte[paramInt2 + 6];
      paramInt3 = paramArrayOfbyte[paramInt2 + 7];
      paramInt2 = b;
    } else {
      paramInt1 = 0;
      paramInt3 = 0;
      paramInt2 = b;
    } 
    if (!paramResultSetImpl.useLegacyDatetimeCode)
      return TimeUtil.fastTimeCreate(paramInt2, paramInt1, paramInt3, paramCalendar, this.exceptionInterceptor); 
    Calendar calendar = paramResultSetImpl.getCalendarInstanceForSessionOrNew();
    return TimeUtil.changeTimezone(paramMySQLConnection, calendar, paramCalendar, TimeUtil.fastTimeCreate(calendar, paramInt2, paramInt1, paramInt3, this.exceptionInterceptor), paramMySQLConnection.getServerTimezoneTZ(), paramTimeZone, paramBoolean);
  }
  
  public abstract Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException;
  
  public Timestamp getNativeTimestamp(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    Calendar calendar;
    boolean bool1;
    boolean bool2;
    boolean bool3;
    boolean bool4;
    boolean bool5;
    int i = 0;
    if (paramInt2 != 0) {
      i = paramArrayOfbyte[paramInt1 + 0] & 0xFF | (paramArrayOfbyte[paramInt1 + 1] & 0xFF) << 8;
      bool5 = paramArrayOfbyte[paramInt1 + 2];
      bool4 = paramArrayOfbyte[paramInt1 + 3];
      if (paramInt2 > 4) {
        bool3 = paramArrayOfbyte[paramInt1 + 4];
        bool2 = paramArrayOfbyte[paramInt1 + 5];
        bool1 = paramArrayOfbyte[paramInt1 + 6];
      } else {
        bool3 = false;
        bool2 = false;
        bool1 = false;
      } 
      if (paramInt2 > 7) {
        int j = (paramArrayOfbyte[paramInt1 + 7] & 0xFF | (paramArrayOfbyte[paramInt1 + 8] & 0xFF) << 8 | (paramArrayOfbyte[paramInt1 + 9] & 0xFF) << 16 | (paramArrayOfbyte[paramInt1 + 10] & 0xFF) << 24) * 1000;
        paramInt1 = i;
        byte b = bool2;
        i = bool1;
        bool1 = bool5;
        bool2 = bool4;
        bool4 = b;
        bool5 = i;
        i = j;
      } else {
        paramInt1 = i;
        byte b = bool2;
        i = bool1;
        bool1 = bool5;
        bool2 = bool4;
        bool4 = b;
        bool5 = i;
        i = 0;
      } 
    } else {
      bool1 = false;
      bool2 = false;
      bool3 = false;
      bool4 = false;
      bool5 = false;
      paramInt1 = i;
      i = 0;
    } 
    if (paramInt2 == 0 || (paramInt1 == 0 && !bool1 && !bool2)) {
      if ("convertToNull".equals(paramMySQLConnection.getZeroDateTimeBehavior()))
        return null; 
      if (!"exception".equals(paramMySQLConnection.getZeroDateTimeBehavior())) {
        paramInt2 = 1;
        paramInt1 = 1;
        bool2 = true;
      } else {
        throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", this.exceptionInterceptor);
      } 
    } else {
      paramInt2 = paramInt1;
      paramInt1 = bool1;
    } 
    if (!paramResultSetImpl.useLegacyDatetimeCode)
      return TimeUtil.fastTimestampCreate(paramTimeZone, paramInt2, paramInt1, bool2, bool3, bool4, bool5, i); 
    boolean bool = paramMySQLConnection.getUseGmtMillisForDatetimes();
    if (paramMySQLConnection.getUseJDBCCompliantTimezoneShift()) {
      calendar = paramMySQLConnection.getUtcCalendar();
    } else {
      calendar = paramResultSetImpl.getCalendarInstanceForSessionOrNew();
    } 
    return TimeUtil.changeTimezone(paramMySQLConnection, calendar, paramCalendar, paramResultSetImpl.fastTimestampCreate(calendar, paramInt2, paramInt1, bool2, bool3, bool4, bool5, i, bool), paramMySQLConnection.getServerTimezoneTZ(), paramTimeZone, paramBoolean);
  }
  
  public abstract Reader getReader(int paramInt) throws SQLException;
  
  public abstract String getString(int paramInt, String paramString, MySQLConnection paramMySQLConnection) throws SQLException;
  
  public String getString(String paramString, MySQLConnection paramMySQLConnection, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws SQLException {
    if (paramMySQLConnection != null && paramMySQLConnection.getUseUnicode()) {
      StringBuilder stringBuilder;
      if (paramString == null) {
        try {
          String str = StringUtils.toString(paramArrayOfbyte);
          paramString = str;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("ResultSet.Unsupported_character_encoding____101"));
          stringBuilder.append(paramString);
          stringBuilder.append("'.");
          throw SQLError.createSQLException(stringBuilder.toString(), "0S100", this.exceptionInterceptor);
        } 
      } else {
        SingleByteCharsetConverter singleByteCharsetConverter = stringBuilder.getCharsetConverter(paramString);
        if (singleByteCharsetConverter != null) {
          String str = singleByteCharsetConverter.toString(paramArrayOfbyte, paramInt1, paramInt2);
          paramString = str;
        } else {
          String str = StringUtils.toString(paramArrayOfbyte, paramInt1, paramInt2, paramString);
          paramString = str;
        } 
      } 
    } else {
      paramString = StringUtils.toAsciiString(paramArrayOfbyte, paramInt1, paramInt2);
    } 
    return paramString;
  }
  
  public abstract Time getTimeFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException;
  
  public Time getTimeFast(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: iconst_0
    //   7: istore #10
    //   9: iload #10
    //   11: iload #4
    //   13: if_icmpge -> 44
    //   16: aload_2
    //   17: iload_3
    //   18: iload #10
    //   20: iadd
    //   21: baload
    //   22: bipush #58
    //   24: if_icmpne -> 33
    //   27: iconst_1
    //   28: istore #11
    //   30: goto -> 47
    //   33: iinc #10, 1
    //   36: goto -> 9
    //   39: astore #5
    //   41: goto -> 1046
    //   44: iconst_0
    //   45: istore #11
    //   47: iconst_0
    //   48: istore #10
    //   50: iload #10
    //   52: iload #4
    //   54: if_icmpge -> 77
    //   57: aload_2
    //   58: iload_3
    //   59: iload #10
    //   61: iadd
    //   62: baload
    //   63: bipush #46
    //   65: if_icmpne -> 71
    //   68: goto -> 80
    //   71: iinc #10, 1
    //   74: goto -> 50
    //   77: iconst_m1
    //   78: istore #10
    //   80: iconst_0
    //   81: istore #12
    //   83: iload #12
    //   85: iload #4
    //   87: if_icmpge -> 176
    //   90: aload_2
    //   91: iload_3
    //   92: iload #12
    //   94: iadd
    //   95: baload
    //   96: istore #13
    //   98: iload #13
    //   100: bipush #32
    //   102: if_icmpeq -> 119
    //   105: iload #13
    //   107: bipush #45
    //   109: if_icmpeq -> 119
    //   112: iload #13
    //   114: bipush #47
    //   116: if_icmpne -> 122
    //   119: iconst_0
    //   120: istore #11
    //   122: iload #13
    //   124: bipush #48
    //   126: if_icmpeq -> 170
    //   129: iload #13
    //   131: bipush #32
    //   133: if_icmpeq -> 170
    //   136: iload #13
    //   138: bipush #58
    //   140: if_icmpeq -> 170
    //   143: iload #13
    //   145: bipush #45
    //   147: if_icmpeq -> 170
    //   150: iload #13
    //   152: bipush #47
    //   154: if_icmpeq -> 170
    //   157: iload #13
    //   159: bipush #46
    //   161: if_icmpeq -> 170
    //   164: iconst_0
    //   165: istore #12
    //   167: goto -> 179
    //   170: iinc #12, 1
    //   173: goto -> 83
    //   176: iconst_1
    //   177: istore #12
    //   179: iload #11
    //   181: ifne -> 284
    //   184: iload #12
    //   186: ifeq -> 284
    //   189: ldc 'convertToNull'
    //   191: aload #8
    //   193: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   198: invokevirtual equals : (Ljava/lang/Object;)Z
    //   201: ifeq -> 206
    //   204: aconst_null
    //   205: areturn
    //   206: ldc 'exception'
    //   208: aload #8
    //   210: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   215: invokevirtual equals : (Ljava/lang/Object;)Z
    //   218: ifne -> 232
    //   221: aload #9
    //   223: aload #5
    //   225: iconst_0
    //   226: iconst_0
    //   227: iconst_0
    //   228: invokevirtual fastTimeCreate : (Ljava/util/Calendar;III)Ljava/sql/Time;
    //   231: areturn
    //   232: new java/lang/StringBuilder
    //   235: astore #5
    //   237: aload #5
    //   239: invokespecial <init> : ()V
    //   242: aload #5
    //   244: ldc 'Value ''
    //   246: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   249: pop
    //   250: aload #5
    //   252: aload_2
    //   253: invokestatic toString : ([B)Ljava/lang/String;
    //   256: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: pop
    //   260: aload #5
    //   262: ldc_w '' can not be represented as java.sql.Time'
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload #5
    //   271: invokevirtual toString : ()Ljava/lang/String;
    //   274: ldc 'S1009'
    //   276: aload_0
    //   277: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   280: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   283: athrow
    //   284: aload_0
    //   285: getfield metadata : [Lcom/mysql/jdbc/Field;
    //   288: iload_1
    //   289: aaload
    //   290: astore #14
    //   292: iload #10
    //   294: iconst_m1
    //   295: if_icmpeq -> 372
    //   298: iload #10
    //   300: iconst_2
    //   301: iadd
    //   302: iload #4
    //   304: if_icmpgt -> 362
    //   307: aload_2
    //   308: iload_3
    //   309: iload #10
    //   311: iadd
    //   312: iconst_1
    //   313: iadd
    //   314: iload_3
    //   315: iload #4
    //   317: iadd
    //   318: invokestatic getInt : ([BII)I
    //   321: pop
    //   322: iload #4
    //   324: iload #10
    //   326: iconst_1
    //   327: iadd
    //   328: isub
    //   329: istore #11
    //   331: iload #10
    //   333: istore #4
    //   335: iload #11
    //   337: bipush #9
    //   339: if_icmpge -> 372
    //   342: ldc2_w 10.0
    //   345: bipush #9
    //   347: iload #11
    //   349: isub
    //   350: i2d
    //   351: invokestatic pow : (DD)D
    //   354: pop2
    //   355: iload #10
    //   357: istore #4
    //   359: goto -> 372
    //   362: new java/lang/IllegalArgumentException
    //   365: astore_2
    //   366: aload_2
    //   367: invokespecial <init> : ()V
    //   370: aload_2
    //   371: athrow
    //   372: aload #14
    //   374: invokevirtual getMysqlType : ()I
    //   377: istore #10
    //   379: iload #10
    //   381: bipush #7
    //   383: if_icmpne -> 681
    //   386: iload #4
    //   388: bipush #10
    //   390: if_icmpeq -> 578
    //   393: iload #4
    //   395: bipush #12
    //   397: if_icmpeq -> 528
    //   400: iload #4
    //   402: bipush #14
    //   404: if_icmpeq -> 528
    //   407: iload #4
    //   409: bipush #19
    //   411: if_icmpne -> 464
    //   414: iload_3
    //   415: iload #4
    //   417: iadd
    //   418: istore #10
    //   420: aload_2
    //   421: iload #10
    //   423: bipush #8
    //   425: isub
    //   426: iload #10
    //   428: bipush #6
    //   430: isub
    //   431: invokestatic getInt : ([BII)I
    //   434: istore #4
    //   436: aload_2
    //   437: iload #10
    //   439: iconst_5
    //   440: isub
    //   441: iload #10
    //   443: iconst_3
    //   444: isub
    //   445: invokestatic getInt : ([BII)I
    //   448: istore_3
    //   449: aload_2
    //   450: iload #10
    //   452: iconst_2
    //   453: isub
    //   454: iload #10
    //   456: invokestatic getInt : ([BII)I
    //   459: istore #10
    //   461: goto -> 610
    //   464: new java/lang/StringBuilder
    //   467: astore_2
    //   468: aload_2
    //   469: invokespecial <init> : ()V
    //   472: aload_2
    //   473: ldc_w 'ResultSet.Timestamp_too_small_to_convert_to_Time_value_in_column__257'
    //   476: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   479: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   482: pop
    //   483: aload_2
    //   484: iload_1
    //   485: iconst_1
    //   486: iadd
    //   487: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   490: pop
    //   491: aload_2
    //   492: ldc_w '('
    //   495: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   498: pop
    //   499: aload_2
    //   500: aload #14
    //   502: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   505: pop
    //   506: aload_2
    //   507: ldc_w ').'
    //   510: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   513: pop
    //   514: aload_2
    //   515: invokevirtual toString : ()Ljava/lang/String;
    //   518: ldc 'S1009'
    //   520: aload_0
    //   521: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   524: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   527: athrow
    //   528: iload_3
    //   529: iload #4
    //   531: iadd
    //   532: istore #11
    //   534: iload #11
    //   536: iconst_4
    //   537: isub
    //   538: istore_3
    //   539: aload_2
    //   540: iload #11
    //   542: bipush #6
    //   544: isub
    //   545: iload_3
    //   546: invokestatic getInt : ([BII)I
    //   549: istore #4
    //   551: iload #11
    //   553: iconst_2
    //   554: isub
    //   555: istore #10
    //   557: aload_2
    //   558: iload_3
    //   559: iload #10
    //   561: invokestatic getInt : ([BII)I
    //   564: istore_3
    //   565: aload_2
    //   566: iload #10
    //   568: iload #11
    //   570: invokestatic getInt : ([BII)I
    //   573: istore #10
    //   575: goto -> 610
    //   578: iload_3
    //   579: bipush #8
    //   581: iadd
    //   582: istore #10
    //   584: aload_2
    //   585: iload_3
    //   586: bipush #6
    //   588: iadd
    //   589: iload #10
    //   591: invokestatic getInt : ([BII)I
    //   594: istore #4
    //   596: aload_2
    //   597: iload #10
    //   599: iload_3
    //   600: bipush #10
    //   602: iadd
    //   603: invokestatic getInt : ([BII)I
    //   606: istore_3
    //   607: iconst_0
    //   608: istore #10
    //   610: new java/sql/SQLWarning
    //   613: astore_2
    //   614: new java/lang/StringBuilder
    //   617: astore #15
    //   619: aload #15
    //   621: invokespecial <init> : ()V
    //   624: aload #15
    //   626: ldc_w 'ResultSet.Precision_lost_converting_TIMESTAMP_to_Time_with_getTime()_on_column__261'
    //   629: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   632: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   635: pop
    //   636: aload #15
    //   638: iload_1
    //   639: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   642: pop
    //   643: aload #15
    //   645: ldc_w '('
    //   648: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   651: pop
    //   652: aload #15
    //   654: aload #14
    //   656: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   659: pop
    //   660: aload #15
    //   662: ldc_w ').'
    //   665: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   668: pop
    //   669: aload_2
    //   670: aload #15
    //   672: invokevirtual toString : ()Ljava/lang/String;
    //   675: invokespecial <init> : (Ljava/lang/String;)V
    //   678: goto -> 967
    //   681: aload #14
    //   683: invokevirtual getMysqlType : ()I
    //   686: bipush #12
    //   688: if_icmpne -> 805
    //   691: aload_2
    //   692: iload_3
    //   693: bipush #11
    //   695: iadd
    //   696: iload_3
    //   697: bipush #13
    //   699: iadd
    //   700: invokestatic getInt : ([BII)I
    //   703: istore #4
    //   705: aload_2
    //   706: iload_3
    //   707: bipush #14
    //   709: iadd
    //   710: iload_3
    //   711: bipush #16
    //   713: iadd
    //   714: invokestatic getInt : ([BII)I
    //   717: istore #10
    //   719: aload_2
    //   720: iload_3
    //   721: bipush #17
    //   723: iadd
    //   724: iload_3
    //   725: bipush #19
    //   727: iadd
    //   728: invokestatic getInt : ([BII)I
    //   731: istore #11
    //   733: new java/lang/StringBuilder
    //   736: astore_2
    //   737: aload_2
    //   738: invokespecial <init> : ()V
    //   741: aload_2
    //   742: ldc_w 'ResultSet.Precision_lost_converting_DATETIME_to_Time_with_getTime()_on_column__264'
    //   745: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   748: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   751: pop
    //   752: aload_2
    //   753: iload_1
    //   754: iconst_1
    //   755: iadd
    //   756: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   759: pop
    //   760: aload_2
    //   761: ldc_w '('
    //   764: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   767: pop
    //   768: aload_2
    //   769: aload #14
    //   771: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   774: pop
    //   775: aload_2
    //   776: ldc_w ').'
    //   779: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   782: pop
    //   783: new java/sql/SQLWarning
    //   786: dup
    //   787: aload_2
    //   788: invokevirtual toString : ()Ljava/lang/String;
    //   791: invokespecial <init> : (Ljava/lang/String;)V
    //   794: pop
    //   795: iload #10
    //   797: istore_3
    //   798: iload #11
    //   800: istore #10
    //   802: goto -> 967
    //   805: aload #14
    //   807: invokevirtual getMysqlType : ()I
    //   810: bipush #10
    //   812: if_icmpne -> 825
    //   815: aload #9
    //   817: aconst_null
    //   818: iconst_0
    //   819: iconst_0
    //   820: iconst_0
    //   821: invokevirtual fastTimeCreate : (Ljava/util/Calendar;III)Ljava/sql/Time;
    //   824: areturn
    //   825: iload #4
    //   827: iconst_5
    //   828: if_icmpeq -> 909
    //   831: iload #4
    //   833: bipush #8
    //   835: if_icmpne -> 841
    //   838: goto -> 909
    //   841: new java/lang/StringBuilder
    //   844: astore #5
    //   846: aload #5
    //   848: invokespecial <init> : ()V
    //   851: aload #5
    //   853: ldc_w 'ResultSet.Bad_format_for_Time____267'
    //   856: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   859: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   862: pop
    //   863: aload #5
    //   865: aload_2
    //   866: invokestatic toString : ([B)Ljava/lang/String;
    //   869: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   872: pop
    //   873: aload #5
    //   875: ldc_w 'ResultSet.___in_column__268'
    //   878: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   881: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   884: pop
    //   885: aload #5
    //   887: iload_1
    //   888: iconst_1
    //   889: iadd
    //   890: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   893: pop
    //   894: aload #5
    //   896: invokevirtual toString : ()Ljava/lang/String;
    //   899: ldc 'S1009'
    //   901: aload_0
    //   902: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   905: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   908: athrow
    //   909: aload_2
    //   910: iload_3
    //   911: iconst_0
    //   912: iadd
    //   913: iload_3
    //   914: iconst_2
    //   915: iadd
    //   916: invokestatic getInt : ([BII)I
    //   919: istore #11
    //   921: aload_2
    //   922: iload_3
    //   923: iconst_3
    //   924: iadd
    //   925: iload_3
    //   926: iconst_5
    //   927: iadd
    //   928: invokestatic getInt : ([BII)I
    //   931: istore #10
    //   933: iload #4
    //   935: iconst_5
    //   936: if_icmpne -> 944
    //   939: iconst_0
    //   940: istore_1
    //   941: goto -> 957
    //   944: aload_2
    //   945: iload_3
    //   946: bipush #6
    //   948: iadd
    //   949: iload_3
    //   950: bipush #8
    //   952: iadd
    //   953: invokestatic getInt : ([BII)I
    //   956: istore_1
    //   957: iload #11
    //   959: istore #4
    //   961: iload #10
    //   963: istore_3
    //   964: iload_1
    //   965: istore #10
    //   967: aload #9
    //   969: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   972: astore_2
    //   973: aload #9
    //   975: getfield useLegacyDatetimeCode : Z
    //   978: ifne -> 1013
    //   981: aload #5
    //   983: ifnonnull -> 998
    //   986: aload #6
    //   988: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   991: invokestatic getInstance : (Ljava/util/TimeZone;Ljava/util/Locale;)Ljava/util/Calendar;
    //   994: astore_2
    //   995: goto -> 1001
    //   998: aload #5
    //   1000: astore_2
    //   1001: aload #9
    //   1003: aload_2
    //   1004: iload #4
    //   1006: iload_3
    //   1007: iload #10
    //   1009: invokevirtual fastTimeCreate : (Ljava/util/Calendar;III)Ljava/sql/Time;
    //   1012: areturn
    //   1013: aload #8
    //   1015: aload_2
    //   1016: aload #5
    //   1018: aload #9
    //   1020: aload_2
    //   1021: iload #4
    //   1023: iload_3
    //   1024: iload #10
    //   1026: invokevirtual fastTimeCreate : (Ljava/util/Calendar;III)Ljava/sql/Time;
    //   1029: aload #8
    //   1031: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   1036: aload #6
    //   1038: iload #7
    //   1040: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
    //   1043: astore_2
    //   1044: aload_2
    //   1045: areturn
    //   1046: aload #5
    //   1048: invokevirtual toString : ()Ljava/lang/String;
    //   1051: ldc 'S1009'
    //   1053: aload_0
    //   1054: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1057: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1060: astore_2
    //   1061: aload_2
    //   1062: aload #5
    //   1064: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1067: pop
    //   1068: aload_2
    //   1069: athrow
    // Exception table:
    //   from	to	target	type
    //   189	204	39	java/lang/RuntimeException
    //   206	232	39	java/lang/RuntimeException
    //   232	284	39	java/lang/RuntimeException
    //   284	292	39	java/lang/RuntimeException
    //   307	322	39	java/lang/RuntimeException
    //   342	355	39	java/lang/RuntimeException
    //   362	372	39	java/lang/RuntimeException
    //   372	379	39	java/lang/RuntimeException
    //   420	461	39	java/lang/RuntimeException
    //   464	528	39	java/lang/RuntimeException
    //   539	551	39	java/lang/RuntimeException
    //   557	575	39	java/lang/RuntimeException
    //   584	607	39	java/lang/RuntimeException
    //   610	678	39	java/lang/RuntimeException
    //   681	795	39	java/lang/RuntimeException
    //   805	825	39	java/lang/RuntimeException
    //   841	909	39	java/lang/RuntimeException
    //   909	933	39	java/lang/RuntimeException
    //   944	957	39	java/lang/RuntimeException
    //   967	981	39	java/lang/RuntimeException
    //   986	995	39	java/lang/RuntimeException
    //   1001	1013	39	java/lang/RuntimeException
    //   1013	1044	39	java/lang/RuntimeException
  }
  
  public abstract Timestamp getTimestampFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean1, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, boolean paramBoolean2, boolean paramBoolean3) throws SQLException;
  
  public Timestamp getTimestampFast(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean1, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, boolean paramBoolean2, boolean paramBoolean3) throws SQLException {
    // Byte code:
    //   0: iload #4
    //   2: istore #13
    //   4: iload #11
    //   6: ifeq -> 21
    //   9: aload #8
    //   11: invokeinterface getUtcCalendar : ()Ljava/util/Calendar;
    //   16: astore #19
    //   18: goto -> 28
    //   21: aload #9
    //   23: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   26: astore #19
    //   28: aload #19
    //   30: aload #5
    //   32: invokestatic setProlepticIfNeeded : (Ljava/util/Calendar;Ljava/util/Calendar;)Ljava/util/Calendar;
    //   35: astore #19
    //   37: iconst_0
    //   38: istore #4
    //   40: iload #4
    //   42: iload #13
    //   44: if_icmpge -> 70
    //   47: aload_2
    //   48: iload_3
    //   49: iload #4
    //   51: iadd
    //   52: baload
    //   53: bipush #58
    //   55: if_icmpne -> 64
    //   58: iconst_1
    //   59: istore #4
    //   61: goto -> 73
    //   64: iinc #4, 1
    //   67: goto -> 40
    //   70: iconst_0
    //   71: istore #4
    //   73: iconst_0
    //   74: istore #12
    //   76: iload #12
    //   78: iload #13
    //   80: if_icmpge -> 169
    //   83: aload_2
    //   84: iload_3
    //   85: iload #12
    //   87: iadd
    //   88: baload
    //   89: istore #14
    //   91: iload #14
    //   93: bipush #32
    //   95: if_icmpeq -> 112
    //   98: iload #14
    //   100: bipush #45
    //   102: if_icmpeq -> 112
    //   105: iload #14
    //   107: bipush #47
    //   109: if_icmpne -> 115
    //   112: iconst_0
    //   113: istore #4
    //   115: iload #14
    //   117: bipush #48
    //   119: if_icmpeq -> 163
    //   122: iload #14
    //   124: bipush #32
    //   126: if_icmpeq -> 163
    //   129: iload #14
    //   131: bipush #58
    //   133: if_icmpeq -> 163
    //   136: iload #14
    //   138: bipush #45
    //   140: if_icmpeq -> 163
    //   143: iload #14
    //   145: bipush #47
    //   147: if_icmpeq -> 163
    //   150: iload #14
    //   152: bipush #46
    //   154: if_icmpeq -> 163
    //   157: iconst_0
    //   158: istore #12
    //   160: goto -> 172
    //   163: iinc #12, 1
    //   166: goto -> 76
    //   169: iconst_1
    //   170: istore #12
    //   172: iload #4
    //   174: ifne -> 303
    //   177: iload #12
    //   179: ifeq -> 303
    //   182: ldc 'convertToNull'
    //   184: aload #8
    //   186: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   191: invokevirtual equals : (Ljava/lang/Object;)Z
    //   194: ifeq -> 199
    //   197: aconst_null
    //   198: areturn
    //   199: ldc 'exception'
    //   201: aload #8
    //   203: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   208: invokevirtual equals : (Ljava/lang/Object;)Z
    //   211: ifne -> 251
    //   214: aload #9
    //   216: getfield useLegacyDatetimeCode : Z
    //   219: ifne -> 235
    //   222: aload #6
    //   224: iconst_1
    //   225: iconst_1
    //   226: iconst_1
    //   227: iconst_0
    //   228: iconst_0
    //   229: iconst_0
    //   230: iconst_0
    //   231: invokestatic fastTimestampCreate : (Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   234: areturn
    //   235: aload #9
    //   237: aconst_null
    //   238: iconst_1
    //   239: iconst_1
    //   240: iconst_1
    //   241: iconst_0
    //   242: iconst_0
    //   243: iconst_0
    //   244: iconst_0
    //   245: iload #10
    //   247: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   250: areturn
    //   251: new java/lang/StringBuilder
    //   254: astore #5
    //   256: aload #5
    //   258: invokespecial <init> : ()V
    //   261: aload #5
    //   263: ldc 'Value ''
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload #5
    //   271: aload_2
    //   272: invokestatic toString : ([B)Ljava/lang/String;
    //   275: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload #5
    //   281: ldc_w '' can not be represented as java.sql.Timestamp'
    //   284: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   287: pop
    //   288: aload #5
    //   290: invokevirtual toString : ()Ljava/lang/String;
    //   293: ldc 'S1009'
    //   295: aload_0
    //   296: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   299: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   302: athrow
    //   303: aload_0
    //   304: getfield metadata : [Lcom/mysql/jdbc/Field;
    //   307: iload_1
    //   308: aaload
    //   309: invokevirtual getMysqlType : ()I
    //   312: bipush #13
    //   314: if_icmpne -> 385
    //   317: aload #9
    //   319: getfield useLegacyDatetimeCode : Z
    //   322: ifne -> 343
    //   325: aload #6
    //   327: aload_2
    //   328: iload_3
    //   329: iconst_4
    //   330: invokestatic getInt : ([BII)I
    //   333: iconst_1
    //   334: iconst_1
    //   335: iconst_0
    //   336: iconst_0
    //   337: iconst_0
    //   338: iconst_0
    //   339: invokestatic fastTimestampCreate : (Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   342: areturn
    //   343: aload #8
    //   345: aload #19
    //   347: aload #5
    //   349: aload #9
    //   351: aload #19
    //   353: aload_2
    //   354: iload_3
    //   355: iconst_4
    //   356: invokestatic getInt : ([BII)I
    //   359: iconst_1
    //   360: iconst_1
    //   361: iconst_0
    //   362: iconst_0
    //   363: iconst_0
    //   364: iconst_0
    //   365: iload #10
    //   367: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   370: aload #8
    //   372: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   377: aload #6
    //   379: iload #7
    //   381: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   384: areturn
    //   385: iconst_0
    //   386: istore #4
    //   388: iload #4
    //   390: iload #13
    //   392: if_icmpge -> 415
    //   395: aload_2
    //   396: iload_3
    //   397: iload #4
    //   399: iadd
    //   400: baload
    //   401: bipush #46
    //   403: if_icmpne -> 409
    //   406: goto -> 418
    //   409: iinc #4, 1
    //   412: goto -> 388
    //   415: iconst_m1
    //   416: istore #4
    //   418: iload_3
    //   419: iload #13
    //   421: iadd
    //   422: istore #14
    //   424: iload #4
    //   426: iload #14
    //   428: iconst_1
    //   429: isub
    //   430: if_icmpne -> 442
    //   433: iload #13
    //   435: iconst_1
    //   436: isub
    //   437: istore #12
    //   439: goto -> 534
    //   442: iload #13
    //   444: istore #12
    //   446: iload #4
    //   448: iconst_m1
    //   449: if_icmpeq -> 534
    //   452: iload #4
    //   454: iconst_2
    //   455: iadd
    //   456: iload #13
    //   458: if_icmpgt -> 524
    //   461: aload_2
    //   462: iload_3
    //   463: iload #4
    //   465: iadd
    //   466: iconst_1
    //   467: iadd
    //   468: iload #14
    //   470: invokestatic getInt : ([BII)I
    //   473: istore #14
    //   475: iload #13
    //   477: iload #4
    //   479: iconst_1
    //   480: iadd
    //   481: isub
    //   482: istore #13
    //   484: iload #14
    //   486: istore #12
    //   488: iload #13
    //   490: bipush #9
    //   492: if_icmpge -> 513
    //   495: iload #14
    //   497: ldc2_w 10.0
    //   500: bipush #9
    //   502: iload #13
    //   504: isub
    //   505: i2d
    //   506: invokestatic pow : (DD)D
    //   509: d2i
    //   510: imul
    //   511: istore #12
    //   513: iload #12
    //   515: istore #16
    //   517: iload #4
    //   519: istore #12
    //   521: goto -> 537
    //   524: new java/lang/IllegalArgumentException
    //   527: astore_2
    //   528: aload_2
    //   529: invokespecial <init> : ()V
    //   532: aload_2
    //   533: athrow
    //   534: iconst_0
    //   535: istore #16
    //   537: iload #12
    //   539: iconst_2
    //   540: if_icmpeq -> 1548
    //   543: iload #12
    //   545: iconst_4
    //   546: if_icmpeq -> 1493
    //   549: iload #12
    //   551: bipush #6
    //   553: if_icmpeq -> 1418
    //   556: iload #12
    //   558: bipush #8
    //   560: if_icmpeq -> 1258
    //   563: iload #12
    //   565: bipush #10
    //   567: if_icmpeq -> 1044
    //   570: iload #12
    //   572: bipush #12
    //   574: if_icmpeq -> 917
    //   577: iload #12
    //   579: bipush #14
    //   581: if_icmpeq -> 803
    //   584: iload #12
    //   586: bipush #29
    //   588: if_icmpeq -> 716
    //   591: iload #12
    //   593: tableswitch default -> 640, 19 -> 716, 20 -> 716, 21 -> 716, 22 -> 716, 23 -> 716, 24 -> 716, 25 -> 716, 26 -> 716
    //   640: new java/sql/SQLException
    //   643: astore #6
    //   645: new java/lang/StringBuilder
    //   648: astore #5
    //   650: aload #5
    //   652: invokespecial <init> : ()V
    //   655: aload #5
    //   657: ldc_w 'Bad format for Timestamp ''
    //   660: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   663: pop
    //   664: aload #5
    //   666: aload_2
    //   667: invokestatic toString : ([B)Ljava/lang/String;
    //   670: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   673: pop
    //   674: aload #5
    //   676: ldc_w '' in column '
    //   679: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   682: pop
    //   683: aload #5
    //   685: iload_1
    //   686: iconst_1
    //   687: iadd
    //   688: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   691: pop
    //   692: aload #5
    //   694: ldc_w '.'
    //   697: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   700: pop
    //   701: aload #6
    //   703: aload #5
    //   705: invokevirtual toString : ()Ljava/lang/String;
    //   708: ldc 'S1009'
    //   710: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   713: aload #6
    //   715: athrow
    //   716: aload_2
    //   717: iload_3
    //   718: iconst_0
    //   719: iadd
    //   720: iload_3
    //   721: iconst_4
    //   722: iadd
    //   723: invokestatic getInt : ([BII)I
    //   726: istore #17
    //   728: aload_2
    //   729: iload_3
    //   730: iconst_5
    //   731: iadd
    //   732: iload_3
    //   733: bipush #7
    //   735: iadd
    //   736: invokestatic getInt : ([BII)I
    //   739: istore #4
    //   741: aload_2
    //   742: iload_3
    //   743: bipush #8
    //   745: iadd
    //   746: iload_3
    //   747: bipush #10
    //   749: iadd
    //   750: invokestatic getInt : ([BII)I
    //   753: istore #12
    //   755: aload_2
    //   756: iload_3
    //   757: bipush #11
    //   759: iadd
    //   760: iload_3
    //   761: bipush #13
    //   763: iadd
    //   764: invokestatic getInt : ([BII)I
    //   767: istore #13
    //   769: aload_2
    //   770: iload_3
    //   771: bipush #14
    //   773: iadd
    //   774: iload_3
    //   775: bipush #16
    //   777: iadd
    //   778: invokestatic getInt : ([BII)I
    //   781: istore #14
    //   783: aload_2
    //   784: iload_3
    //   785: bipush #17
    //   787: iadd
    //   788: iload_3
    //   789: bipush #19
    //   791: iadd
    //   792: invokestatic getInt : ([BII)I
    //   795: istore #15
    //   797: iload #17
    //   799: istore_3
    //   800: goto -> 1597
    //   803: iload_3
    //   804: iconst_4
    //   805: iadd
    //   806: istore #4
    //   808: aload_2
    //   809: iload_3
    //   810: iconst_0
    //   811: iadd
    //   812: iload #4
    //   814: invokestatic getInt : ([BII)I
    //   817: istore #15
    //   819: iload_3
    //   820: bipush #6
    //   822: iadd
    //   823: istore #12
    //   825: aload_2
    //   826: iload #4
    //   828: iload #12
    //   830: invokestatic getInt : ([BII)I
    //   833: istore #4
    //   835: iload_3
    //   836: bipush #8
    //   838: iadd
    //   839: istore #14
    //   841: aload_2
    //   842: iload #12
    //   844: iload #14
    //   846: invokestatic getInt : ([BII)I
    //   849: istore #12
    //   851: iload_3
    //   852: bipush #10
    //   854: iadd
    //   855: istore #13
    //   857: aload_2
    //   858: iload #14
    //   860: iload #13
    //   862: invokestatic getInt : ([BII)I
    //   865: istore #14
    //   867: iload_3
    //   868: bipush #12
    //   870: iadd
    //   871: istore #17
    //   873: aload_2
    //   874: iload #13
    //   876: iload #17
    //   878: invokestatic getInt : ([BII)I
    //   881: istore #13
    //   883: aload_2
    //   884: iload #17
    //   886: iload_3
    //   887: bipush #14
    //   889: iadd
    //   890: invokestatic getInt : ([BII)I
    //   893: istore #17
    //   895: iload #15
    //   897: istore_3
    //   898: iload #17
    //   900: istore #15
    //   902: iload #13
    //   904: istore #17
    //   906: iload #14
    //   908: istore #13
    //   910: iload #17
    //   912: istore #14
    //   914: goto -> 1597
    //   917: iload_3
    //   918: iconst_2
    //   919: iadd
    //   920: istore #13
    //   922: aload_2
    //   923: iload_3
    //   924: iconst_0
    //   925: iadd
    //   926: iload #13
    //   928: invokestatic getInt : ([BII)I
    //   931: istore #12
    //   933: iload #12
    //   935: istore #4
    //   937: iload #12
    //   939: bipush #69
    //   941: if_icmpgt -> 951
    //   944: iload #12
    //   946: bipush #100
    //   948: iadd
    //   949: istore #4
    //   951: iload_3
    //   952: iconst_4
    //   953: iadd
    //   954: istore #12
    //   956: aload_2
    //   957: iload #13
    //   959: iload #12
    //   961: invokestatic getInt : ([BII)I
    //   964: istore #13
    //   966: iload_3
    //   967: bipush #6
    //   969: iadd
    //   970: istore #14
    //   972: aload_2
    //   973: iload #12
    //   975: iload #14
    //   977: invokestatic getInt : ([BII)I
    //   980: istore #12
    //   982: iload_3
    //   983: bipush #8
    //   985: iadd
    //   986: istore #15
    //   988: aload_2
    //   989: iload #14
    //   991: iload #15
    //   993: invokestatic getInt : ([BII)I
    //   996: istore #14
    //   998: iload_3
    //   999: bipush #10
    //   1001: iadd
    //   1002: istore #18
    //   1004: aload_2
    //   1005: iload #15
    //   1007: iload #18
    //   1009: invokestatic getInt : ([BII)I
    //   1012: istore #17
    //   1014: aload_2
    //   1015: iload #18
    //   1017: iload_3
    //   1018: bipush #12
    //   1020: iadd
    //   1021: invokestatic getInt : ([BII)I
    //   1024: istore #15
    //   1026: iload #4
    //   1028: sipush #1900
    //   1031: iadd
    //   1032: istore_3
    //   1033: iload #13
    //   1035: istore #4
    //   1037: iload #17
    //   1039: istore #13
    //   1041: goto -> 902
    //   1044: iconst_0
    //   1045: istore #4
    //   1047: iload #4
    //   1049: iload #12
    //   1051: if_icmpge -> 1077
    //   1054: aload_2
    //   1055: iload_3
    //   1056: iload #4
    //   1058: iadd
    //   1059: baload
    //   1060: bipush #45
    //   1062: if_icmpne -> 1071
    //   1065: iconst_1
    //   1066: istore #4
    //   1068: goto -> 1080
    //   1071: iinc #4, 1
    //   1074: goto -> 1047
    //   1077: iconst_0
    //   1078: istore #4
    //   1080: aload_0
    //   1081: getfield metadata : [Lcom/mysql/jdbc/Field;
    //   1084: iload_1
    //   1085: aaload
    //   1086: invokevirtual getMysqlType : ()I
    //   1089: bipush #10
    //   1091: if_icmpeq -> 1213
    //   1094: iload #4
    //   1096: ifeq -> 1102
    //   1099: goto -> 1213
    //   1102: iload_3
    //   1103: iconst_2
    //   1104: iadd
    //   1105: istore #13
    //   1107: aload_2
    //   1108: iload_3
    //   1109: iconst_0
    //   1110: iadd
    //   1111: iload #13
    //   1113: invokestatic getInt : ([BII)I
    //   1116: istore #12
    //   1118: iload #12
    //   1120: istore #4
    //   1122: iload #12
    //   1124: bipush #69
    //   1126: if_icmpgt -> 1136
    //   1129: iload #12
    //   1131: bipush #100
    //   1133: iadd
    //   1134: istore #4
    //   1136: iload_3
    //   1137: iconst_4
    //   1138: iadd
    //   1139: istore #12
    //   1141: aload_2
    //   1142: iload #13
    //   1144: iload #12
    //   1146: invokestatic getInt : ([BII)I
    //   1149: istore #13
    //   1151: iload_3
    //   1152: bipush #6
    //   1154: iadd
    //   1155: istore #14
    //   1157: aload_2
    //   1158: iload #12
    //   1160: iload #14
    //   1162: invokestatic getInt : ([BII)I
    //   1165: istore #12
    //   1167: iload_3
    //   1168: bipush #8
    //   1170: iadd
    //   1171: istore #17
    //   1173: aload_2
    //   1174: iload #14
    //   1176: iload #17
    //   1178: invokestatic getInt : ([BII)I
    //   1181: istore #15
    //   1183: aload_2
    //   1184: iload #17
    //   1186: iload_3
    //   1187: bipush #10
    //   1189: iadd
    //   1190: invokestatic getInt : ([BII)I
    //   1193: istore #14
    //   1195: iload #4
    //   1197: sipush #1900
    //   1200: iadd
    //   1201: istore_3
    //   1202: iload #13
    //   1204: istore #4
    //   1206: iload #15
    //   1208: istore #13
    //   1210: goto -> 1594
    //   1213: aload_2
    //   1214: iload_3
    //   1215: iconst_0
    //   1216: iadd
    //   1217: iload_3
    //   1218: iconst_4
    //   1219: iadd
    //   1220: invokestatic getInt : ([BII)I
    //   1223: istore #4
    //   1225: aload_2
    //   1226: iload_3
    //   1227: iconst_5
    //   1228: iadd
    //   1229: iload_3
    //   1230: bipush #7
    //   1232: iadd
    //   1233: invokestatic getInt : ([BII)I
    //   1236: istore #13
    //   1238: aload_2
    //   1239: iload_3
    //   1240: bipush #8
    //   1242: iadd
    //   1243: iload_3
    //   1244: bipush #10
    //   1246: iadd
    //   1247: invokestatic getInt : ([BII)I
    //   1250: istore #12
    //   1252: iload #13
    //   1254: istore_3
    //   1255: goto -> 1405
    //   1258: iconst_0
    //   1259: istore #4
    //   1261: iload #4
    //   1263: iload #12
    //   1265: if_icmpge -> 1291
    //   1268: aload_2
    //   1269: iload_3
    //   1270: iload #4
    //   1272: iadd
    //   1273: baload
    //   1274: bipush #58
    //   1276: if_icmpne -> 1285
    //   1279: iconst_1
    //   1280: istore #4
    //   1282: goto -> 1294
    //   1285: iinc #4, 1
    //   1288: goto -> 1261
    //   1291: iconst_0
    //   1292: istore #4
    //   1294: iload #4
    //   1296: ifeq -> 1350
    //   1299: aload_2
    //   1300: iload_3
    //   1301: iconst_0
    //   1302: iadd
    //   1303: iload_3
    //   1304: iconst_2
    //   1305: iadd
    //   1306: invokestatic getInt : ([BII)I
    //   1309: istore #13
    //   1311: aload_2
    //   1312: iload_3
    //   1313: iconst_3
    //   1314: iadd
    //   1315: iload_3
    //   1316: iconst_5
    //   1317: iadd
    //   1318: invokestatic getInt : ([BII)I
    //   1321: istore #14
    //   1323: aload_2
    //   1324: iload_3
    //   1325: bipush #6
    //   1327: iadd
    //   1328: iload_3
    //   1329: bipush #8
    //   1331: iadd
    //   1332: invokestatic getInt : ([BII)I
    //   1335: istore #15
    //   1337: sipush #1970
    //   1340: istore_3
    //   1341: iconst_1
    //   1342: istore #4
    //   1344: iconst_1
    //   1345: istore #12
    //   1347: goto -> 1597
    //   1350: iload_3
    //   1351: iconst_4
    //   1352: iadd
    //   1353: istore #13
    //   1355: aload_2
    //   1356: iload_3
    //   1357: iconst_0
    //   1358: iadd
    //   1359: iload #13
    //   1361: invokestatic getInt : ([BII)I
    //   1364: istore #4
    //   1366: iload_3
    //   1367: bipush #6
    //   1369: iadd
    //   1370: istore #12
    //   1372: aload_2
    //   1373: iload #13
    //   1375: iload #12
    //   1377: invokestatic getInt : ([BII)I
    //   1380: istore #13
    //   1382: aload_2
    //   1383: iload #12
    //   1385: iload_3
    //   1386: bipush #8
    //   1388: iadd
    //   1389: invokestatic getInt : ([BII)I
    //   1392: istore #12
    //   1394: wide iinc #4 -1900
    //   1400: iload #13
    //   1402: iconst_1
    //   1403: isub
    //   1404: istore_3
    //   1405: iload_3
    //   1406: istore #13
    //   1408: iload #4
    //   1410: istore_3
    //   1411: iload #13
    //   1413: istore #4
    //   1415: goto -> 1588
    //   1418: iload_3
    //   1419: iconst_2
    //   1420: iadd
    //   1421: istore #13
    //   1423: aload_2
    //   1424: iload_3
    //   1425: iconst_0
    //   1426: iadd
    //   1427: iload #13
    //   1429: invokestatic getInt : ([BII)I
    //   1432: istore #12
    //   1434: iload #12
    //   1436: istore #4
    //   1438: iload #12
    //   1440: bipush #69
    //   1442: if_icmpgt -> 1452
    //   1445: iload #12
    //   1447: bipush #100
    //   1449: iadd
    //   1450: istore #4
    //   1452: iload_3
    //   1453: iconst_4
    //   1454: iadd
    //   1455: istore #12
    //   1457: aload_2
    //   1458: iload #13
    //   1460: iload #12
    //   1462: invokestatic getInt : ([BII)I
    //   1465: istore #13
    //   1467: aload_2
    //   1468: iload #12
    //   1470: iload_3
    //   1471: bipush #6
    //   1473: iadd
    //   1474: invokestatic getInt : ([BII)I
    //   1477: istore #12
    //   1479: iload #4
    //   1481: sipush #1900
    //   1484: iadd
    //   1485: istore_3
    //   1486: iload #13
    //   1488: istore #4
    //   1490: goto -> 1588
    //   1493: iload_3
    //   1494: iconst_2
    //   1495: iadd
    //   1496: istore #13
    //   1498: aload_2
    //   1499: iload_3
    //   1500: iconst_0
    //   1501: iadd
    //   1502: iload #13
    //   1504: invokestatic getInt : ([BII)I
    //   1507: istore #12
    //   1509: iload #12
    //   1511: istore #4
    //   1513: iload #12
    //   1515: bipush #69
    //   1517: if_icmpgt -> 1527
    //   1520: iload #12
    //   1522: bipush #100
    //   1524: iadd
    //   1525: istore #4
    //   1527: aload_2
    //   1528: iload #13
    //   1530: iload_3
    //   1531: iconst_4
    //   1532: iadd
    //   1533: invokestatic getInt : ([BII)I
    //   1536: istore #12
    //   1538: iload #4
    //   1540: istore_3
    //   1541: iload #12
    //   1543: istore #4
    //   1545: goto -> 1585
    //   1548: aload_2
    //   1549: iload_3
    //   1550: iconst_0
    //   1551: iadd
    //   1552: iload_3
    //   1553: iconst_2
    //   1554: iadd
    //   1555: invokestatic getInt : ([BII)I
    //   1558: istore #4
    //   1560: iload #4
    //   1562: istore_3
    //   1563: iload #4
    //   1565: bipush #69
    //   1567: if_icmpgt -> 1576
    //   1570: iload #4
    //   1572: bipush #100
    //   1574: iadd
    //   1575: istore_3
    //   1576: wide iinc #3 1900
    //   1582: iconst_1
    //   1583: istore #4
    //   1585: iconst_1
    //   1586: istore #12
    //   1588: iconst_0
    //   1589: istore #13
    //   1591: iconst_0
    //   1592: istore #14
    //   1594: iconst_0
    //   1595: istore #15
    //   1597: aload #9
    //   1599: getfield useLegacyDatetimeCode : Z
    //   1602: ifne -> 1624
    //   1605: aload #6
    //   1607: iload_3
    //   1608: iload #4
    //   1610: iload #12
    //   1612: iload #13
    //   1614: iload #14
    //   1616: iload #15
    //   1618: iload #16
    //   1620: invokestatic fastTimestampCreate : (Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   1623: areturn
    //   1624: aload #8
    //   1626: aload #19
    //   1628: aload #5
    //   1630: aload #9
    //   1632: aload #19
    //   1634: iload_3
    //   1635: iload #4
    //   1637: iload #12
    //   1639: iload #13
    //   1641: iload #14
    //   1643: iload #15
    //   1645: iload #16
    //   1647: iload #10
    //   1649: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   1652: aload #8
    //   1654: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   1659: aload #6
    //   1661: iload #7
    //   1663: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   1666: astore_2
    //   1667: aload_2
    //   1668: areturn
    //   1669: astore_2
    //   1670: new java/lang/StringBuilder
    //   1673: dup
    //   1674: invokespecial <init> : ()V
    //   1677: astore #5
    //   1679: aload #5
    //   1681: ldc_w 'Cannot convert value ''
    //   1684: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1687: pop
    //   1688: aload #5
    //   1690: aload_0
    //   1691: iload_1
    //   1692: ldc 'ISO8859_1'
    //   1694: aload #8
    //   1696: invokevirtual getString : (ILjava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)Ljava/lang/String;
    //   1699: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1702: pop
    //   1703: aload #5
    //   1705: ldc_w '' from column '
    //   1708: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1711: pop
    //   1712: aload #5
    //   1714: iload_1
    //   1715: iconst_1
    //   1716: iadd
    //   1717: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1720: pop
    //   1721: aload #5
    //   1723: ldc_w ' to TIMESTAMP.'
    //   1726: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1729: pop
    //   1730: aload #5
    //   1732: invokevirtual toString : ()Ljava/lang/String;
    //   1735: ldc 'S1009'
    //   1737: aload_0
    //   1738: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1741: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1744: astore #5
    //   1746: aload #5
    //   1748: aload_2
    //   1749: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1752: pop
    //   1753: aload #5
    //   1755: athrow
    // Exception table:
    //   from	to	target	type
    //   9	18	1669	java/lang/RuntimeException
    //   21	28	1669	java/lang/RuntimeException
    //   28	37	1669	java/lang/RuntimeException
    //   182	197	1669	java/lang/RuntimeException
    //   199	235	1669	java/lang/RuntimeException
    //   235	251	1669	java/lang/RuntimeException
    //   251	303	1669	java/lang/RuntimeException
    //   303	343	1669	java/lang/RuntimeException
    //   343	385	1669	java/lang/RuntimeException
    //   461	475	1669	java/lang/RuntimeException
    //   495	513	1669	java/lang/RuntimeException
    //   524	534	1669	java/lang/RuntimeException
    //   640	716	1669	java/lang/RuntimeException
    //   716	797	1669	java/lang/RuntimeException
    //   808	819	1669	java/lang/RuntimeException
    //   825	835	1669	java/lang/RuntimeException
    //   841	851	1669	java/lang/RuntimeException
    //   857	867	1669	java/lang/RuntimeException
    //   873	895	1669	java/lang/RuntimeException
    //   922	933	1669	java/lang/RuntimeException
    //   956	966	1669	java/lang/RuntimeException
    //   972	982	1669	java/lang/RuntimeException
    //   988	998	1669	java/lang/RuntimeException
    //   1004	1026	1669	java/lang/RuntimeException
    //   1080	1094	1669	java/lang/RuntimeException
    //   1107	1118	1669	java/lang/RuntimeException
    //   1141	1151	1669	java/lang/RuntimeException
    //   1157	1167	1669	java/lang/RuntimeException
    //   1173	1195	1669	java/lang/RuntimeException
    //   1213	1252	1669	java/lang/RuntimeException
    //   1299	1337	1669	java/lang/RuntimeException
    //   1355	1366	1669	java/lang/RuntimeException
    //   1372	1394	1669	java/lang/RuntimeException
    //   1423	1434	1669	java/lang/RuntimeException
    //   1457	1479	1669	java/lang/RuntimeException
    //   1498	1509	1669	java/lang/RuntimeException
    //   1527	1538	1669	java/lang/RuntimeException
    //   1548	1560	1669	java/lang/RuntimeException
    //   1597	1624	1669	java/lang/RuntimeException
    //   1624	1667	1669	java/lang/RuntimeException
  }
  
  public abstract boolean isFloatingPointNumber(int paramInt) throws SQLException;
  
  public abstract boolean isNull(int paramInt) throws SQLException;
  
  public abstract long length(int paramInt) throws SQLException;
  
  public abstract void setColumnValue(int paramInt, byte[] paramArrayOfbyte) throws SQLException;
  
  public ResultSetRow setMetadata(Field[] paramArrayOfField) throws SQLException {
    this.metadata = paramArrayOfField;
    return this;
  }
}
