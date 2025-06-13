package com.mysql.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

public class Field {
  private static final int AUTO_INCREMENT_FLAG = 512;
  
  private static final int NO_CHARSET_INFO = -1;
  
  private byte[] buffer;
  
  private int colDecimals;
  
  private short colFlag;
  
  private int collationIndex = 0;
  
  private String collationName = null;
  
  private MySQLConnection connection = null;
  
  private String databaseName = null;
  
  private int databaseNameLength = -1;
  
  private int databaseNameStart = -1;
  
  public int defaultValueLength = -1;
  
  public int defaultValueStart = -1;
  
  private String encoding = null;
  
  private String fullName = null;
  
  private String fullOriginalName = null;
  
  private boolean isImplicitTempTable = false;
  
  private boolean isSingleBit;
  
  private long length;
  
  private int maxBytesPerChar;
  
  private int mysqlType = -1;
  
  private String name;
  
  private int nameLength;
  
  private int nameStart;
  
  private String originalColumnName = null;
  
  private int originalColumnNameLength = -1;
  
  private int originalColumnNameStart = -1;
  
  private String originalTableName = null;
  
  private int originalTableNameLength = -1;
  
  private int originalTableNameStart = -1;
  
  private int precisionAdjustFactor = 0;
  
  private int sqlType = -1;
  
  private String tableName;
  
  private int tableNameLength;
  
  private int tableNameStart;
  
  private boolean useOldNameMetadata = false;
  
  private final boolean valueNeedsQuoting;
  
  public Field(MySQLConnection paramMySQLConnection, byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong, int paramInt11, short paramShort, int paramInt12, int paramInt13, int paramInt14, int paramInt15) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: iconst_0
    //   5: istore #24
    //   7: aload_0
    //   8: iconst_0
    //   9: putfield collationIndex : I
    //   12: aload_0
    //   13: aconst_null
    //   14: putfield encoding : Ljava/lang/String;
    //   17: aload_0
    //   18: aconst_null
    //   19: putfield collationName : Ljava/lang/String;
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   27: aload_0
    //   28: aconst_null
    //   29: putfield databaseName : Ljava/lang/String;
    //   32: aload_0
    //   33: iconst_m1
    //   34: putfield databaseNameLength : I
    //   37: aload_0
    //   38: iconst_m1
    //   39: putfield databaseNameStart : I
    //   42: aload_0
    //   43: iconst_m1
    //   44: putfield defaultValueLength : I
    //   47: aload_0
    //   48: iconst_m1
    //   49: putfield defaultValueStart : I
    //   52: aload_0
    //   53: aconst_null
    //   54: putfield fullName : Ljava/lang/String;
    //   57: aload_0
    //   58: aconst_null
    //   59: putfield fullOriginalName : Ljava/lang/String;
    //   62: aload_0
    //   63: iconst_0
    //   64: putfield isImplicitTempTable : Z
    //   67: aload_0
    //   68: iconst_m1
    //   69: putfield mysqlType : I
    //   72: aload_0
    //   73: aconst_null
    //   74: putfield originalColumnName : Ljava/lang/String;
    //   77: aload_0
    //   78: iconst_m1
    //   79: putfield originalColumnNameLength : I
    //   82: aload_0
    //   83: iconst_m1
    //   84: putfield originalColumnNameStart : I
    //   87: aload_0
    //   88: aconst_null
    //   89: putfield originalTableName : Ljava/lang/String;
    //   92: aload_0
    //   93: iconst_m1
    //   94: putfield originalTableNameLength : I
    //   97: aload_0
    //   98: iconst_m1
    //   99: putfield originalTableNameStart : I
    //   102: aload_0
    //   103: iconst_0
    //   104: putfield precisionAdjustFactor : I
    //   107: aload_0
    //   108: iconst_m1
    //   109: putfield sqlType : I
    //   112: aload_0
    //   113: iconst_0
    //   114: putfield useOldNameMetadata : Z
    //   117: aload_0
    //   118: aload_1
    //   119: putfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   122: aload_0
    //   123: aload_2
    //   124: putfield buffer : [B
    //   127: aload_0
    //   128: iload #9
    //   130: putfield nameStart : I
    //   133: aload_0
    //   134: iload #10
    //   136: putfield nameLength : I
    //   139: aload_0
    //   140: iload #5
    //   142: putfield tableNameStart : I
    //   145: aload_0
    //   146: iload #6
    //   148: putfield tableNameLength : I
    //   151: aload_0
    //   152: lload #13
    //   154: putfield length : J
    //   157: aload_0
    //   158: iload #16
    //   160: putfield colFlag : S
    //   163: aload_0
    //   164: iload #17
    //   166: putfield colDecimals : I
    //   169: aload_0
    //   170: iload #15
    //   172: putfield mysqlType : I
    //   175: aload_0
    //   176: iload_3
    //   177: putfield databaseNameStart : I
    //   180: aload_0
    //   181: iload #4
    //   183: putfield databaseNameLength : I
    //   186: aload_0
    //   187: iload #7
    //   189: putfield originalTableNameStart : I
    //   192: aload_0
    //   193: iload #8
    //   195: putfield originalTableNameLength : I
    //   198: aload_0
    //   199: iload #11
    //   201: putfield originalColumnNameStart : I
    //   204: aload_0
    //   205: iload #12
    //   207: putfield originalColumnNameLength : I
    //   210: aload_0
    //   211: iload #18
    //   213: putfield defaultValueStart : I
    //   216: aload_0
    //   217: iload #19
    //   219: putfield defaultValueLength : I
    //   222: aload_0
    //   223: iload #20
    //   225: putfield collationIndex : I
    //   228: aload_0
    //   229: iload #15
    //   231: invokestatic mysqlToJavaType : (I)I
    //   234: putfield sqlType : I
    //   237: aload_0
    //   238: invokespecial checkForImplicitTemporaryTable : ()V
    //   241: aload_0
    //   242: getfield originalTableNameLength : I
    //   245: ifne -> 253
    //   248: iconst_1
    //   249: istore_3
    //   250: goto -> 255
    //   253: iconst_0
    //   254: istore_3
    //   255: aload_0
    //   256: getfield mysqlType : I
    //   259: sipush #252
    //   262: if_icmpne -> 394
    //   265: aload_0
    //   266: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   269: invokeinterface getBlobsAreStrings : ()Z
    //   274: ifne -> 382
    //   277: aload_0
    //   278: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   281: invokeinterface getFunctionsNeverReturnBlobs : ()Z
    //   286: ifeq -> 296
    //   289: iload_3
    //   290: ifeq -> 296
    //   293: goto -> 382
    //   296: aload_0
    //   297: getfield collationIndex : I
    //   300: bipush #63
    //   302: if_icmpeq -> 338
    //   305: aload_0
    //   306: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   309: iconst_4
    //   310: iconst_1
    //   311: iconst_0
    //   312: invokeinterface versionMeetsMinimum : (III)Z
    //   317: ifne -> 323
    //   320: goto -> 338
    //   323: aload_0
    //   324: sipush #253
    //   327: putfield mysqlType : I
    //   330: aload_0
    //   331: iconst_m1
    //   332: putfield sqlType : I
    //   335: goto -> 394
    //   338: aload_0
    //   339: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   342: invokeinterface getUseBlobToStoreUTF8OutsideBMP : ()Z
    //   347: ifeq -> 364
    //   350: aload_0
    //   351: invokespecial shouldSetupForUtf8StringInBlob : ()Z
    //   354: ifeq -> 364
    //   357: aload_0
    //   358: invokespecial setupForUtf8StringInBlob : ()V
    //   361: goto -> 394
    //   364: aload_0
    //   365: invokespecial setBlobTypeBasedOnLength : ()V
    //   368: aload_0
    //   369: aload_0
    //   370: getfield mysqlType : I
    //   373: invokestatic mysqlToJavaType : (I)I
    //   376: putfield sqlType : I
    //   379: goto -> 394
    //   382: aload_0
    //   383: bipush #12
    //   385: putfield sqlType : I
    //   388: aload_0
    //   389: bipush #15
    //   391: putfield mysqlType : I
    //   394: aload_0
    //   395: getfield sqlType : I
    //   398: bipush #-6
    //   400: if_icmpne -> 457
    //   403: aload_0
    //   404: getfield length : J
    //   407: lconst_1
    //   408: lcmp
    //   409: ifne -> 457
    //   412: aload_0
    //   413: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   416: invokeinterface getTinyInt1isBit : ()Z
    //   421: ifeq -> 457
    //   424: aload_1
    //   425: invokeinterface getTinyInt1isBit : ()Z
    //   430: ifeq -> 457
    //   433: aload_1
    //   434: invokeinterface getTransformedBitIsBoolean : ()Z
    //   439: ifeq -> 451
    //   442: aload_0
    //   443: bipush #16
    //   445: putfield sqlType : I
    //   448: goto -> 457
    //   451: aload_0
    //   452: bipush #-7
    //   454: putfield sqlType : I
    //   457: aload_0
    //   458: invokespecial isNativeNumericType : ()Z
    //   461: ifne -> 839
    //   464: aload_0
    //   465: invokespecial isNativeDateTimeType : ()Z
    //   468: ifne -> 839
    //   471: aload_0
    //   472: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   475: aload_0
    //   476: getfield collationIndex : I
    //   479: invokeinterface getEncodingForIndex : (I)Ljava/lang/String;
    //   484: astore_1
    //   485: aload_0
    //   486: aload_1
    //   487: putfield encoding : Ljava/lang/String;
    //   490: ldc 'UnicodeBig'
    //   492: aload_1
    //   493: invokevirtual equals : (Ljava/lang/Object;)Z
    //   496: ifeq -> 505
    //   499: aload_0
    //   500: ldc 'UTF-16'
    //   502: putfield encoding : Ljava/lang/String;
    //   505: aload_0
    //   506: getfield mysqlType : I
    //   509: sipush #245
    //   512: if_icmpne -> 521
    //   515: aload_0
    //   516: ldc 'UTF-8'
    //   518: putfield encoding : Ljava/lang/String;
    //   521: aload_0
    //   522: invokevirtual isBinary : ()Z
    //   525: istore #23
    //   527: aload_0
    //   528: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   531: iconst_4
    //   532: iconst_1
    //   533: iconst_0
    //   534: invokeinterface versionMeetsMinimum : (III)Z
    //   539: ifeq -> 610
    //   542: aload_0
    //   543: getfield mysqlType : I
    //   546: sipush #253
    //   549: if_icmpne -> 610
    //   552: iload #23
    //   554: ifeq -> 610
    //   557: aload_0
    //   558: getfield collationIndex : I
    //   561: bipush #63
    //   563: if_icmpne -> 610
    //   566: aload_0
    //   567: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   570: invokeinterface getFunctionsNeverReturnBlobs : ()Z
    //   575: ifeq -> 597
    //   578: iload_3
    //   579: ifeq -> 597
    //   582: aload_0
    //   583: bipush #12
    //   585: putfield sqlType : I
    //   588: aload_0
    //   589: bipush #15
    //   591: putfield mysqlType : I
    //   594: goto -> 610
    //   597: aload_0
    //   598: invokevirtual isOpaqueBinary : ()Z
    //   601: ifeq -> 610
    //   604: aload_0
    //   605: bipush #-3
    //   607: putfield sqlType : I
    //   610: aload_0
    //   611: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   614: iconst_4
    //   615: iconst_1
    //   616: iconst_0
    //   617: invokeinterface versionMeetsMinimum : (III)Z
    //   622: ifeq -> 674
    //   625: aload_0
    //   626: getfield mysqlType : I
    //   629: sipush #254
    //   632: if_icmpne -> 674
    //   635: iload #23
    //   637: ifeq -> 674
    //   640: aload_0
    //   641: getfield collationIndex : I
    //   644: bipush #63
    //   646: if_icmpne -> 674
    //   649: aload_0
    //   650: invokevirtual isOpaqueBinary : ()Z
    //   653: ifeq -> 674
    //   656: aload_0
    //   657: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   660: invokeinterface getBlobsAreStrings : ()Z
    //   665: ifne -> 674
    //   668: aload_0
    //   669: bipush #-2
    //   671: putfield sqlType : I
    //   674: iload #23
    //   676: istore #21
    //   678: aload_0
    //   679: getfield mysqlType : I
    //   682: bipush #16
    //   684: if_icmpne -> 795
    //   687: aload_0
    //   688: getfield length : J
    //   691: lstore #13
    //   693: lload #13
    //   695: lconst_0
    //   696: lcmp
    //   697: ifeq -> 747
    //   700: iload #24
    //   702: istore #22
    //   704: lload #13
    //   706: lconst_1
    //   707: lcmp
    //   708: ifne -> 750
    //   711: aload_0
    //   712: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   715: iconst_5
    //   716: iconst_0
    //   717: bipush #21
    //   719: invokeinterface versionMeetsMinimum : (III)Z
    //   724: ifne -> 747
    //   727: iload #24
    //   729: istore #22
    //   731: aload_0
    //   732: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   735: iconst_5
    //   736: iconst_1
    //   737: bipush #10
    //   739: invokeinterface versionMeetsMinimum : (III)Z
    //   744: ifeq -> 750
    //   747: iconst_1
    //   748: istore #22
    //   750: aload_0
    //   751: iload #22
    //   753: putfield isSingleBit : Z
    //   756: iload #23
    //   758: istore #21
    //   760: iload #22
    //   762: ifne -> 795
    //   765: aload_0
    //   766: getfield colFlag : S
    //   769: sipush #128
    //   772: ior
    //   773: i2s
    //   774: istore #16
    //   776: aload_0
    //   777: iload #16
    //   779: putfield colFlag : S
    //   782: aload_0
    //   783: iload #16
    //   785: bipush #16
    //   787: ior
    //   788: i2s
    //   789: putfield colFlag : S
    //   792: iconst_1
    //   793: istore #21
    //   795: aload_0
    //   796: getfield sqlType : I
    //   799: istore_3
    //   800: iload_3
    //   801: bipush #-4
    //   803: if_icmpne -> 819
    //   806: iload #21
    //   808: ifne -> 819
    //   811: aload_0
    //   812: iconst_m1
    //   813: putfield sqlType : I
    //   816: goto -> 845
    //   819: iload_3
    //   820: bipush #-3
    //   822: if_icmpne -> 845
    //   825: iload #21
    //   827: ifne -> 845
    //   830: aload_0
    //   831: bipush #12
    //   833: putfield sqlType : I
    //   836: goto -> 845
    //   839: aload_0
    //   840: ldc 'US-ASCII'
    //   842: putfield encoding : Ljava/lang/String;
    //   845: aload_0
    //   846: invokevirtual isUnsigned : ()Z
    //   849: ifne -> 897
    //   852: aload_0
    //   853: getfield mysqlType : I
    //   856: istore_3
    //   857: iload_3
    //   858: ifeq -> 889
    //   861: iload_3
    //   862: sipush #246
    //   865: if_icmpeq -> 889
    //   868: iload_3
    //   869: iconst_4
    //   870: if_icmpeq -> 881
    //   873: iload_3
    //   874: iconst_5
    //   875: if_icmpeq -> 881
    //   878: goto -> 920
    //   881: aload_0
    //   882: iconst_1
    //   883: putfield precisionAdjustFactor : I
    //   886: goto -> 920
    //   889: aload_0
    //   890: iconst_m1
    //   891: putfield precisionAdjustFactor : I
    //   894: goto -> 920
    //   897: aload_0
    //   898: getfield mysqlType : I
    //   901: istore_3
    //   902: iload_3
    //   903: iconst_4
    //   904: if_icmpeq -> 915
    //   907: iload_3
    //   908: iconst_5
    //   909: if_icmpeq -> 915
    //   912: goto -> 920
    //   915: aload_0
    //   916: iconst_1
    //   917: putfield precisionAdjustFactor : I
    //   920: aload_0
    //   921: aload_0
    //   922: invokespecial determineNeedsQuoting : ()Z
    //   925: putfield valueNeedsQuoting : Z
    //   928: return
  }
  
  public Field(MySQLConnection paramMySQLConnection, byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, short paramShort, int paramInt7) throws SQLException {
    this(paramMySQLConnection, paramArrayOfbyte, -1, -1, paramInt3, paramInt4, -1, -1, paramInt1, paramInt2, -1, -1, paramInt5, paramInt6, paramShort, paramInt7, -1, -1, -1);
  }
  
  public Field(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    this.tableName = paramString1;
    this.name = paramString2;
    this.length = paramInt2;
    this.sqlType = paramInt1;
    this.colFlag = 0;
    this.colDecimals = 0;
    this.valueNeedsQuoting = determineNeedsQuoting();
  }
  
  public Field(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3) {
    this.tableName = paramString1;
    this.name = paramString2;
    this.length = paramInt3;
    this.sqlType = paramInt2;
    this.colFlag = 0;
    this.colDecimals = 0;
    this.collationIndex = paramInt1;
    this.valueNeedsQuoting = determineNeedsQuoting();
    paramInt1 = this.sqlType;
    if (paramInt1 == -3 || paramInt1 == -2) {
      short s = (short)(this.colFlag | 0x80);
      this.colFlag = s;
      this.colFlag = (short)(s | 0x10);
    } 
  }
  
  private void checkForImplicitTemporaryTable() {
    // Byte code:
    //   0: aload_0
    //   1: getfield tableNameLength : I
    //   4: iconst_5
    //   5: if_icmple -> 71
    //   8: aload_0
    //   9: getfield buffer : [B
    //   12: astore_3
    //   13: aload_0
    //   14: getfield tableNameStart : I
    //   17: istore_1
    //   18: aload_3
    //   19: iload_1
    //   20: baload
    //   21: bipush #35
    //   23: if_icmpne -> 71
    //   26: aload_3
    //   27: iload_1
    //   28: iconst_1
    //   29: iadd
    //   30: baload
    //   31: bipush #115
    //   33: if_icmpne -> 71
    //   36: aload_3
    //   37: iload_1
    //   38: iconst_2
    //   39: iadd
    //   40: baload
    //   41: bipush #113
    //   43: if_icmpne -> 71
    //   46: aload_3
    //   47: iload_1
    //   48: iconst_3
    //   49: iadd
    //   50: baload
    //   51: bipush #108
    //   53: if_icmpne -> 71
    //   56: aload_3
    //   57: iload_1
    //   58: iconst_4
    //   59: iadd
    //   60: baload
    //   61: bipush #95
    //   63: if_icmpne -> 71
    //   66: iconst_1
    //   67: istore_2
    //   68: goto -> 73
    //   71: iconst_0
    //   72: istore_2
    //   73: aload_0
    //   74: iload_2
    //   75: putfield isImplicitTempTable : Z
    //   78: return
  }
  
  private boolean determineNeedsQuoting() {
    int i = this.sqlType;
    if (i != -7 && i != -6 && i != -5)
      switch (i) {
        default:
          return true;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
          break;
      }  
    return false;
  }
  
  private String getStringFromBytes(int paramInt1, int paramInt2) throws SQLException {
    String str;
    SingleByteCharsetConverter singleByteCharsetConverter = null;
    if (paramInt1 == -1 || paramInt2 == -1)
      return null; 
    if (paramInt2 == 0)
      return ""; 
    MySQLConnection mySQLConnection = this.connection;
    if (mySQLConnection != null) {
      if (mySQLConnection.getUseUnicode()) {
        String str1 = this.connection.getCharacterSetMetadata();
        str = str1;
        if (str1 == null)
          str = this.connection.getEncoding(); 
        if (str != null) {
          MySQLConnection mySQLConnection1 = this.connection;
          if (mySQLConnection1 != null)
            singleByteCharsetConverter = mySQLConnection1.getCharsetConverter(str); 
          if (singleByteCharsetConverter != null) {
            str = singleByteCharsetConverter.toString(this.buffer, paramInt1, paramInt2);
          } else {
            try {
              String str2 = StringUtils.toString(this.buffer, paramInt1, paramInt2, str);
              str = str2;
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(Messages.getString("Field.12"));
              stringBuilder.append(str);
              stringBuilder.append(Messages.getString("Field.13"));
              throw new RuntimeException(stringBuilder.toString());
            } 
          } 
        } else {
          str = StringUtils.toAsciiString(this.buffer, paramInt1, paramInt2);
        } 
      } else {
        str = StringUtils.toAsciiString(this.buffer, paramInt1, paramInt2);
      } 
    } else {
      str = StringUtils.toAsciiString(this.buffer, paramInt1, paramInt2);
    } 
    return str;
  }
  
  private boolean isImplicitTemporaryTable() {
    return this.isImplicitTempTable;
  }
  
  private boolean isNativeDateTimeType() {
    int i = this.mysqlType;
    return (i == 10 || i == 14 || i == 12 || i == 11 || i == 7);
  }
  
  private boolean isNativeNumericType() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mysqlType : I
    //   4: istore_1
    //   5: iconst_1
    //   6: istore_3
    //   7: iload_1
    //   8: iconst_1
    //   9: if_icmplt -> 19
    //   12: iload_3
    //   13: istore_2
    //   14: iload_1
    //   15: iconst_5
    //   16: if_icmple -> 40
    //   19: iload_3
    //   20: istore_2
    //   21: iload_1
    //   22: bipush #8
    //   24: if_icmpeq -> 40
    //   27: iload_1
    //   28: bipush #13
    //   30: if_icmpne -> 38
    //   33: iload_3
    //   34: istore_2
    //   35: goto -> 40
    //   38: iconst_0
    //   39: istore_2
    //   40: iload_2
    //   41: ireturn
  }
  
  private void setBlobTypeBasedOnLength() {
    long l = this.length;
    if (l == 255L) {
      this.mysqlType = 249;
    } else if (l == 65535L) {
      this.mysqlType = 252;
    } else if (l == 16777215L) {
      this.mysqlType = 250;
    } else if (l == 4294967295L) {
      this.mysqlType = 251;
    } 
  }
  
  private void setupForUtf8StringInBlob() {
    long l = this.length;
    if (l == 255L || l == 65535L) {
      this.mysqlType = 15;
      this.sqlType = 12;
    } else {
      this.mysqlType = 253;
      this.sqlType = -1;
    } 
    this.collationIndex = 33;
  }
  
  private boolean shouldSetupForUtf8StringInBlob() throws SQLException {
    String str1 = this.connection.getUtf8OutsideBmpIncludedColumnNamePattern();
    String str2 = this.connection.getUtf8OutsideBmpExcludedColumnNamePattern();
    if (str2 != null && !StringUtils.isEmptyOrWhitespaceOnly(str2))
      try {
        if (getOriginalName().matches(str2)) {
          if (str1 != null) {
            boolean bool = StringUtils.isEmptyOrWhitespaceOnly(str1);
            if (!bool)
              try {
                bool = getOriginalName().matches(str1);
                if (bool)
                  return true; 
              } catch (PatternSyntaxException patternSyntaxException) {
                SQLException sQLException = SQLError.createSQLException("Illegal regex specified for \"utf8OutsideBmpIncludedColumnNamePattern\"", "S1009", this.connection.getExceptionInterceptor());
                if (!this.connection.getParanoid())
                  sQLException.initCause(patternSyntaxException); 
                throw sQLException;
              }  
          } 
          return false;
        } 
      } catch (PatternSyntaxException patternSyntaxException) {
        SQLException sQLException = SQLError.createSQLException("Illegal regex specified for \"utf8OutsideBmpExcludedColumnNamePattern\"", "S1009", this.connection.getExceptionInterceptor());
        if (!this.connection.getParanoid())
          sQLException.initCause(patternSyntaxException); 
        throw sQLException;
      }  
    return true;
  }
  
  public String getCollation() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield collationName : Ljava/lang/String;
    //   6: ifnonnull -> 399
    //   9: aload_0
    //   10: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnull -> 399
    //   18: aload_2
    //   19: iconst_4
    //   20: iconst_1
    //   21: iconst_0
    //   22: invokeinterface versionMeetsMinimum : (III)Z
    //   27: ifeq -> 399
    //   30: aload_0
    //   31: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   34: invokeinterface getUseDynamicCharsetInfo : ()Z
    //   39: istore_1
    //   40: aconst_null
    //   41: astore_3
    //   42: aconst_null
    //   43: astore #4
    //   45: iload_1
    //   46: ifeq -> 363
    //   49: aload_0
    //   50: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   53: invokeinterface getMetaData : ()Ljava/sql/DatabaseMetaData;
    //   58: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
    //   63: astore #5
    //   65: aload #5
    //   67: astore_2
    //   68: ldc_w ' '
    //   71: aload #5
    //   73: invokevirtual equals : (Ljava/lang/Object;)Z
    //   76: ifeq -> 82
    //   79: ldc ''
    //   81: astore_2
    //   82: aload_0
    //   83: invokevirtual getDatabaseName : ()Ljava/lang/String;
    //   86: astore #8
    //   88: aload_0
    //   89: invokevirtual getOriginalTableName : ()Ljava/lang/String;
    //   92: astore #5
    //   94: aload_0
    //   95: invokevirtual getOriginalName : ()Ljava/lang/String;
    //   98: astore #6
    //   100: aload #8
    //   102: ifnull -> 399
    //   105: aload #8
    //   107: invokevirtual length : ()I
    //   110: ifeq -> 399
    //   113: aload #5
    //   115: ifnull -> 399
    //   118: aload #5
    //   120: invokevirtual length : ()I
    //   123: ifeq -> 399
    //   126: aload #6
    //   128: ifnull -> 399
    //   131: aload #6
    //   133: invokevirtual length : ()I
    //   136: ifeq -> 399
    //   139: new java/lang/StringBuilder
    //   142: astore #7
    //   144: aload #7
    //   146: aload #8
    //   148: invokevirtual length : ()I
    //   151: aload #5
    //   153: invokevirtual length : ()I
    //   156: iadd
    //   157: bipush #28
    //   159: iadd
    //   160: invokespecial <init> : (I)V
    //   163: aload #7
    //   165: ldc_w 'SHOW FULL COLUMNS FROM '
    //   168: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: aload #7
    //   174: aload_2
    //   175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: aload #7
    //   181: aload #8
    //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload #7
    //   189: aload_2
    //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload #7
    //   196: ldc_w '.'
    //   199: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: pop
    //   203: aload #7
    //   205: aload_2
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload #7
    //   212: aload #5
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload #7
    //   220: aload_2
    //   221: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload_0
    //   226: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   229: invokeinterface createStatement : ()Ljava/sql/Statement;
    //   234: astore #5
    //   236: aload #4
    //   238: astore_2
    //   239: aload #5
    //   241: aload #7
    //   243: invokevirtual toString : ()Ljava/lang/String;
    //   246: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   251: astore_3
    //   252: aload_3
    //   253: astore_2
    //   254: aload_3
    //   255: invokeinterface next : ()Z
    //   260: ifeq -> 297
    //   263: aload_3
    //   264: astore_2
    //   265: aload #6
    //   267: aload_3
    //   268: ldc_w 'Field'
    //   271: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   276: invokevirtual equals : (Ljava/lang/Object;)Z
    //   279: ifeq -> 252
    //   282: aload_3
    //   283: astore_2
    //   284: aload_0
    //   285: aload_3
    //   286: ldc_w 'Collation'
    //   289: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   294: putfield collationName : Ljava/lang/String;
    //   297: aload_3
    //   298: ifnull -> 307
    //   301: aload_3
    //   302: invokeinterface close : ()V
    //   307: aload #5
    //   309: ifnull -> 399
    //   312: aload #5
    //   314: invokeinterface close : ()V
    //   319: goto -> 399
    //   322: astore #4
    //   324: aload #5
    //   326: astore_3
    //   327: goto -> 340
    //   330: astore #4
    //   332: aconst_null
    //   333: astore #5
    //   335: aload_3
    //   336: astore_2
    //   337: aload #5
    //   339: astore_3
    //   340: aload_2
    //   341: ifnull -> 350
    //   344: aload_2
    //   345: invokeinterface close : ()V
    //   350: aload_3
    //   351: ifnull -> 360
    //   354: aload_3
    //   355: invokeinterface close : ()V
    //   360: aload #4
    //   362: athrow
    //   363: aload_0
    //   364: getstatic com/mysql/jdbc/CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME : [Ljava/lang/String;
    //   367: aload_0
    //   368: getfield collationIndex : I
    //   371: aaload
    //   372: putfield collationName : Ljava/lang/String;
    //   375: goto -> 399
    //   378: astore_2
    //   379: aload_2
    //   380: invokevirtual toString : ()Ljava/lang/String;
    //   383: ldc_w 'S1009'
    //   386: aconst_null
    //   387: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   390: astore_3
    //   391: aload_3
    //   392: aload_2
    //   393: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   396: pop
    //   397: aload_3
    //   398: athrow
    //   399: aload_0
    //   400: getfield collationName : Ljava/lang/String;
    //   403: astore_2
    //   404: aload_0
    //   405: monitorexit
    //   406: aload_2
    //   407: areturn
    //   408: astore_2
    //   409: aload_0
    //   410: monitorexit
    //   411: aload_2
    //   412: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	408	finally
    //   18	40	408	finally
    //   49	65	408	finally
    //   68	79	408	finally
    //   82	100	408	finally
    //   105	113	408	finally
    //   118	126	408	finally
    //   131	225	408	finally
    //   225	236	330	finally
    //   239	252	322	finally
    //   254	263	322	finally
    //   265	282	322	finally
    //   284	297	322	finally
    //   301	307	408	finally
    //   312	319	408	finally
    //   344	350	408	finally
    //   354	360	408	finally
    //   360	363	408	finally
    //   363	375	378	java/lang/RuntimeException
    //   363	375	408	finally
    //   379	399	408	finally
    //   399	404	408	finally
  }
  
  public String getColumnLabel() throws SQLException {
    return getName();
  }
  
  public String getDatabaseName() throws SQLException {
    if (this.databaseName == null) {
      int i = this.databaseNameStart;
      if (i != -1) {
        int j = this.databaseNameLength;
        if (j != -1)
          this.databaseName = getStringFromBytes(i, j); 
      } 
    } 
    return this.databaseName;
  }
  
  public int getDecimals() {
    return this.colDecimals;
  }
  
  public String getEncoding() throws SQLException {
    return this.encoding;
  }
  
  public String getFullName() throws SQLException {
    if (this.fullName == null) {
      StringBuilder stringBuilder = new StringBuilder(getTableName().length() + 1 + getName().length());
      stringBuilder.append(this.tableName);
      stringBuilder.append('.');
      stringBuilder.append(this.name);
      this.fullName = stringBuilder.toString();
    } 
    return this.fullName;
  }
  
  public String getFullOriginalName() throws SQLException {
    getOriginalName();
    if (this.originalColumnName == null)
      return null; 
    if (this.fullName == null) {
      StringBuilder stringBuilder = new StringBuilder(getOriginalTableName().length() + 1 + getOriginalName().length());
      stringBuilder.append(this.originalTableName);
      stringBuilder.append('.');
      stringBuilder.append(this.originalColumnName);
      this.fullOriginalName = stringBuilder.toString();
    } 
    return this.fullOriginalName;
  }
  
  public long getLength() {
    return this.length;
  }
  
  public int getMaxBytesPerCharacter() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxBytesPerChar : I
    //   6: ifne -> 33
    //   9: aload_0
    //   10: aload_0
    //   11: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   14: aload_0
    //   15: getfield collationIndex : I
    //   18: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   21: aload_0
    //   22: invokevirtual getEncoding : ()Ljava/lang/String;
    //   25: invokeinterface getMaxBytesPerChar : (Ljava/lang/Integer;Ljava/lang/String;)I
    //   30: putfield maxBytesPerChar : I
    //   33: aload_0
    //   34: getfield maxBytesPerChar : I
    //   37: istore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: iload_1
    //   41: ireturn
    //   42: astore_2
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_2
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	42	finally
    //   33	38	42	finally
  }
  
  public int getMysqlType() {
    return this.mysqlType;
  }
  
  public String getName() throws SQLException {
    if (this.name == null)
      this.name = getStringFromBytes(this.nameStart, this.nameLength); 
    return this.name;
  }
  
  public String getNameNoAliases() throws SQLException {
    if (this.useOldNameMetadata)
      return getName(); 
    MySQLConnection mySQLConnection = this.connection;
    return (mySQLConnection != null && mySQLConnection.versionMeetsMinimum(4, 1, 0)) ? getOriginalName() : getName();
  }
  
  public String getOriginalName() throws SQLException {
    if (this.originalColumnName == null) {
      int i = this.originalColumnNameStart;
      if (i != -1) {
        int j = this.originalColumnNameLength;
        if (j != -1)
          this.originalColumnName = getStringFromBytes(i, j); 
      } 
    } 
    return this.originalColumnName;
  }
  
  public String getOriginalTableName() throws SQLException {
    if (this.originalTableName == null) {
      int i = this.originalTableNameStart;
      if (i != -1) {
        int j = this.originalTableNameLength;
        if (j != -1)
          this.originalTableName = getStringFromBytes(i, j); 
      } 
    } 
    return this.originalTableName;
  }
  
  public int getPrecisionAdjustFactor() {
    return this.precisionAdjustFactor;
  }
  
  public int getSQLType() {
    return this.sqlType;
  }
  
  public String getTable() throws SQLException {
    return getTableName();
  }
  
  public String getTableName() throws SQLException {
    if (this.tableName == null)
      this.tableName = getStringFromBytes(this.tableNameStart, this.tableNameLength); 
    return this.tableName;
  }
  
  public String getTableNameNoAliases() throws SQLException {
    return this.connection.versionMeetsMinimum(4, 1, 0) ? getOriginalTableName() : getTableName();
  }
  
  public boolean getvalueNeedsQuoting() {
    return this.valueNeedsQuoting;
  }
  
  public boolean isAutoIncrement() {
    boolean bool;
    if ((this.colFlag & 0x200) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isBinary() {
    boolean bool;
    if ((this.colFlag & 0x80) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isBlob() {
    boolean bool;
    if ((this.colFlag & 0x10) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isCharsetApplicableType() {
    int i = this.mysqlType;
    return (i == 247 || i == 245 || i == 248 || i == 254 || i == 253 || i == 15);
  }
  
  public boolean isMultipleKey() {
    boolean bool;
    if ((this.colFlag & 0x8) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isNotNull() {
    short s = this.colFlag;
    boolean bool = true;
    if ((s & 0x1) <= 0)
      bool = false; 
    return bool;
  }
  
  public boolean isOpaqueBinary() throws SQLException {
    int i = this.collationIndex;
    boolean bool = true;
    if (i == 63 && isBinary() && (getMysqlType() == 254 || getMysqlType() == 253)) {
      if (this.originalTableNameLength == 0) {
        MySQLConnection mySQLConnection = this.connection;
        if (mySQLConnection != null && !mySQLConnection.versionMeetsMinimum(5, 0, 25))
          return false; 
      } 
      return isImplicitTemporaryTable() ^ true;
    } 
    if (!this.connection.versionMeetsMinimum(4, 1, 0) || !"binary".equalsIgnoreCase(getEncoding()))
      bool = false; 
    return bool;
  }
  
  public boolean isPrimaryKey() {
    boolean bool;
    if ((this.colFlag & 0x2) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isReadOnly() throws SQLException {
    MySQLConnection mySQLConnection = this.connection;
    boolean bool = true;
    if (mySQLConnection.versionMeetsMinimum(4, 1, 0)) {
      String str1 = getOriginalName();
      String str2 = getOriginalTableName();
      boolean bool1 = bool;
      if (str1 != null) {
        bool1 = bool;
        if (str1.length() > 0) {
          bool1 = bool;
          if (str2 != null)
            if (str2.length() <= 0) {
              bool1 = bool;
            } else {
              bool1 = false;
            }  
        } 
      } 
      return bool1;
    } 
    return false;
  }
  
  public boolean isSingleBit() {
    return this.isSingleBit;
  }
  
  public boolean isUniqueKey() {
    boolean bool;
    if ((this.colFlag & 0x4) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isUnsigned() {
    boolean bool;
    if ((this.colFlag & 0x20) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isZeroFill() {
    boolean bool;
    if ((this.colFlag & 0x40) > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setConnection(MySQLConnection paramMySQLConnection) {
    this.connection = paramMySQLConnection;
    if (this.encoding == null || this.collationIndex == 0)
      this.encoding = paramMySQLConnection.getEncoding(); 
  }
  
  public void setEncoding(String paramString, Connection paramConnection) throws SQLException {
    this.encoding = paramString;
    try {
      this.collationIndex = CharsetMapping.getCollationIndexForJavaEncoding(paramString, paramConnection);
      return;
    } catch (RuntimeException runtimeException) {
      SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
      sQLException.initCause(runtimeException);
      throw sQLException;
    } 
  }
  
  public void setMysqlType(int paramInt) {
    this.mysqlType = paramInt;
    this.sqlType = MysqlDefs.mysqlToJavaType(paramInt);
  }
  
  public void setUnsigned() {
    this.colFlag = (short)(this.colFlag | 0x20);
  }
  
  public void setUseOldNameMetadata(boolean paramBoolean) {
    this.useOldNameMetadata = paramBoolean;
  }
  
  public String toString() {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(super.toString());
      stringBuilder.append("[");
      stringBuilder.append("catalog=");
      stringBuilder.append(getDatabaseName());
      stringBuilder.append(",tableName=");
      stringBuilder.append(getTableName());
      stringBuilder.append(",originalTableName=");
      stringBuilder.append(getOriginalTableName());
      stringBuilder.append(",columnName=");
      stringBuilder.append(getName());
      stringBuilder.append(",originalColumnName=");
      stringBuilder.append(getOriginalName());
      stringBuilder.append(",mysqlType=");
      stringBuilder.append(getMysqlType());
      stringBuilder.append("(");
      stringBuilder.append(MysqlDefs.typeToName(getMysqlType()));
      stringBuilder.append(")");
      stringBuilder.append(",flags=");
      if (isAutoIncrement())
        stringBuilder.append(" AUTO_INCREMENT"); 
      if (isPrimaryKey())
        stringBuilder.append(" PRIMARY_KEY"); 
      if (isUniqueKey())
        stringBuilder.append(" UNIQUE_KEY"); 
      if (isBinary())
        stringBuilder.append(" BINARY"); 
      if (isBlob())
        stringBuilder.append(" BLOB"); 
      if (isMultipleKey())
        stringBuilder.append(" MULTI_KEY"); 
      if (isUnsigned())
        stringBuilder.append(" UNSIGNED"); 
      if (isZeroFill())
        stringBuilder.append(" ZEROFILL"); 
      stringBuilder.append(", charsetIndex=");
      stringBuilder.append(this.collationIndex);
      stringBuilder.append(", charsetName=");
      stringBuilder.append(this.encoding);
      return stringBuilder.toString();
    } finally {
      Exception exception = null;
    } 
  }
}
