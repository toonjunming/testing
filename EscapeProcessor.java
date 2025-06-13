package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class EscapeProcessor {
  private static Map<String, String> JDBC_CONVERT_TO_MYSQL_TYPE_MAP;
  
  private static Map<String, String> JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("BIGINT", "0 + ?");
    hashMap.put("BINARY", "BINARY");
    hashMap.put("BIT", "0 + ?");
    hashMap.put("CHAR", "CHAR");
    hashMap.put("DATE", "DATE");
    hashMap.put("DECIMAL", "0.0 + ?");
    hashMap.put("DOUBLE", "0.0 + ?");
    hashMap.put("FLOAT", "0.0 + ?");
    hashMap.put("INTEGER", "0 + ?");
    hashMap.put("LONGVARBINARY", "BINARY");
    hashMap.put("LONGVARCHAR", "CONCAT(?)");
    hashMap.put("REAL", "0.0 + ?");
    hashMap.put("SMALLINT", "CONCAT(?)");
    hashMap.put("TIME", "TIME");
    hashMap.put("TIMESTAMP", "DATETIME");
    hashMap.put("TINYINT", "CONCAT(?)");
    hashMap.put("VARBINARY", "BINARY");
    hashMap.put("VARCHAR", "CONCAT(?)");
    JDBC_CONVERT_TO_MYSQL_TYPE_MAP = Collections.unmodifiableMap(hashMap);
    hashMap = new HashMap<Object, Object>(JDBC_CONVERT_TO_MYSQL_TYPE_MAP);
    hashMap.put("BINARY", "CONCAT(?)");
    hashMap.put("CHAR", "CONCAT(?)");
    hashMap.remove("DATE");
    hashMap.put("LONGVARBINARY", "CONCAT(?)");
    hashMap.remove("TIME");
    hashMap.remove("TIMESTAMP");
    hashMap.put("VARBINARY", "CONCAT(?)");
    JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP = Collections.unmodifiableMap(hashMap);
  }
  
  public static final Object escapeSQL(String paramString, boolean paramBoolean, MySQLConnection paramMySQLConnection) throws SQLException {
    String str = null;
    if (paramString == null)
      return null; 
    int i = paramString.indexOf('{');
    if (i == -1) {
      i = -1;
    } else {
      i = paramString.indexOf('}', i);
    } 
    if (i == -1)
      return paramString; 
    StringBuilder stringBuilder = new StringBuilder();
    EscapeTokenizer escapeTokenizer = new EscapeTokenizer(paramString);
    int j = 0;
    i = 0;
    boolean bool = false;
    while (true) {
      Object object;
      boolean bool1 = escapeTokenizer.hasMoreTokens();
      boolean bool2 = true;
      if (bool1) {
        Object object1;
        String str2 = escapeTokenizer.nextToken();
        paramString = str;
        int k = j;
        int m = i;
        bool1 = bool;
        if (str2.length() != 0) {
          StringBuilder stringBuilder1;
          if (str2.charAt(0) == '{') {
            if (str2.endsWith("}")) {
              if (str2.length() > 2 && str2.indexOf('{', 2) != -1) {
                StringBuilder stringBuilder2 = new StringBuilder(str2.substring(0, 1));
                object1 = escapeSQL(str2.substring(1, str2.length() - 1), paramBoolean, paramMySQLConnection);
                if (object1 instanceof String) {
                  object1 = object1;
                } else {
                  EscapeProcessorResult escapeProcessorResult1 = (EscapeProcessorResult)object1;
                  object1 = escapeProcessorResult1.escapedSql;
                  k = i;
                  if (i != 1)
                    k = escapeProcessorResult1.usesVariables; 
                  i = k;
                } 
                stringBuilder2.append((String)object1);
                stringBuilder2.append('}');
                str2 = stringBuilder2.toString();
              } 
              paramString = removeWhitespace(str2);
              if (StringUtils.startsWithIgnoreCase(paramString, "{escape")) {
                paramString = str;
                try {
                  StringTokenizer stringTokenizer = new StringTokenizer();
                  paramString = str;
                  this(str2, " '");
                  paramString = str;
                  stringTokenizer.nextToken();
                  paramString = str;
                  str = stringTokenizer.nextToken();
                  paramString = str;
                  if (str.length() < 3) {
                    paramString = str;
                    stringBuilder.append(str2);
                    paramString = str;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } else {
                    paramString = str;
                    str = str.substring(1, str.length() - 1);
                    paramString = str;
                    k = 1;
                    m = i;
                    bool1 = bool;
                  } 
                } catch (NoSuchElementException noSuchElementException) {
                  stringBuilder.append(str2);
                  k = j;
                  m = i;
                  bool1 = bool;
                } 
              } else if (StringUtils.startsWithIgnoreCase(paramString, "{fn")) {
                paramString = str2.substring(str2.toLowerCase().indexOf("fn ") + 3, str2.length() - 1);
                if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "convert")) {
                  stringBuilder.append(processConvertToken(paramString, paramBoolean, paramMySQLConnection));
                  object1 = noSuchElementException;
                  k = j;
                  m = i;
                  bool1 = bool;
                } else {
                  stringBuilder.append((String)object1);
                  object1 = noSuchElementException;
                  k = j;
                  m = i;
                  bool1 = bool;
                } 
              } else {
                NoSuchElementException noSuchElementException1;
                if (StringUtils.startsWithIgnoreCase((String)object1, "{d")) {
                  k = str2.indexOf('\'') + 1;
                  m = str2.lastIndexOf('\'');
                  if (k == -1 || m == -1) {
                    stringBuilder.append(str2);
                    object1 = noSuchElementException;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } else {
                    String str3 = str2.substring(k, m);
                    try {
                      StringTokenizer stringTokenizer = new StringTokenizer();
                      this(str3, " -");
                      String str4 = stringTokenizer.nextToken();
                      str2 = stringTokenizer.nextToken();
                      String str5 = stringTokenizer.nextToken();
                      StringBuilder stringBuilder2 = new StringBuilder();
                      this();
                      stringBuilder2.append("'");
                      stringBuilder2.append(str4);
                      stringBuilder2.append("-");
                      stringBuilder2.append(str2);
                      stringBuilder2.append("-");
                      stringBuilder2.append(str5);
                      stringBuilder2.append("'");
                      stringBuilder.append(stringBuilder2.toString());
                      noSuchElementException1 = noSuchElementException;
                      k = j;
                      m = i;
                      bool1 = bool;
                    } catch (NoSuchElementException noSuchElementException2) {
                      stringBuilder1 = new StringBuilder();
                      stringBuilder1.append("Syntax error for DATE escape sequence '");
                      stringBuilder1.append((String)noSuchElementException1);
                      stringBuilder1.append("'");
                      throw SQLError.createSQLException(stringBuilder1.toString(), "42000", paramMySQLConnection.getExceptionInterceptor());
                    } 
                  } 
                } else {
                  StringBuilder stringBuilder2;
                  if (StringUtils.startsWithIgnoreCase((String)noSuchElementException1, "{ts")) {
                    processTimestampToken(paramMySQLConnection, stringBuilder, str2);
                    stringBuilder2 = stringBuilder1;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } else if (StringUtils.startsWithIgnoreCase((String)stringBuilder2, "{t")) {
                    processTimeToken(paramMySQLConnection, stringBuilder, str2);
                    stringBuilder2 = stringBuilder1;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } else if (StringUtils.startsWithIgnoreCase((String)stringBuilder2, "{call") || StringUtils.startsWithIgnoreCase((String)stringBuilder2, "{?=call")) {
                    m = StringUtils.indexOfIgnoreCase(str2, "CALL") + 5;
                    k = str2.length() - 1;
                    if (StringUtils.startsWithIgnoreCase((String)stringBuilder2, "{?=call")) {
                      stringBuilder.append("SELECT ");
                      stringBuilder.append(str2.substring(m, k));
                      bool = bool2;
                    } else {
                      stringBuilder.append("CALL ");
                      stringBuilder.append(str2.substring(m, k));
                      bool = false;
                    } 
                    while (--k >= m) {
                      char c = str2.charAt(k);
                      if (Character.isWhitespace(c)) {
                        k--;
                        continue;
                      } 
                      if (c != ')')
                        stringBuilder.append("()"); 
                    } 
                    stringBuilder2 = stringBuilder1;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } else if (StringUtils.startsWithIgnoreCase((String)stringBuilder2, "{oj")) {
                    stringBuilder.append(str2);
                    stringBuilder2 = stringBuilder1;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } else {
                    stringBuilder.append(str2);
                    stringBuilder2 = stringBuilder1;
                    k = j;
                    m = i;
                    bool1 = bool;
                  } 
                } 
              } 
            } else {
              object1 = new StringBuilder();
              object1.append("Not a valid escape sequence: ");
              object1.append(str2);
              throw SQLError.createSQLException(object1.toString(), paramMySQLConnection.getExceptionInterceptor());
            } 
          } else {
            stringBuilder.append(str2);
            bool1 = bool;
            m = i;
            k = j;
            object1 = stringBuilder1;
          } 
        } 
        object = object1;
        j = k;
        i = m;
        bool = bool1;
        continue;
      } 
      paramString = stringBuilder.toString();
      String str1 = paramString;
      if (j != 0)
        while (true) {
          str1 = paramString;
          if (paramString.indexOf((String)object) != -1) {
            int k = paramString.indexOf((String)object);
            str1 = paramString.substring(0, k);
            String str3 = paramString.substring(k + 1, paramString.length());
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(str1);
            stringBuilder1.append("\\");
            stringBuilder1.append(str3);
            String str2 = stringBuilder1.toString();
            continue;
          } 
          break;
        }  
      EscapeProcessorResult escapeProcessorResult = new EscapeProcessorResult();
      escapeProcessorResult.escapedSql = str1;
      escapeProcessorResult.callingStoredFunction = bool;
      if (i != 1)
        if (escapeTokenizer.sawVariableUse()) {
          escapeProcessorResult.usesVariables = 1;
        } else {
          escapeProcessorResult.usesVariables = 0;
        }  
      return escapeProcessorResult;
    } 
  }
  
  private static String processConvertToken(String paramString, boolean paramBoolean, MySQLConnection paramMySQLConnection) throws SQLException {
    StringBuilder stringBuilder1;
    StringBuilder stringBuilder2;
    int i = paramString.indexOf("(");
    if (i != -1) {
      int j = paramString.lastIndexOf(",");
      if (j != -1) {
        int k = paramString.indexOf(')', j);
        if (k != -1) {
          String str2 = paramString.substring(i + 1, j);
          String str3 = paramString.substring(j + 1, k);
          String str1 = str3.trim();
          paramString = str1;
          if (StringUtils.startsWithIgnoreCase(str1, "SQL_"))
            paramString = str1.substring(4, str1.length()); 
          if (paramBoolean) {
            paramString = JDBC_CONVERT_TO_MYSQL_TYPE_MAP.get(paramString.toUpperCase(Locale.ENGLISH));
          } else {
            paramString = JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP.get(paramString.toUpperCase(Locale.ENGLISH));
            if (paramString == null) {
              stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Can't find conversion re-write for type '");
              stringBuilder1.append(str3);
              stringBuilder1.append("' that is applicable for this server version while processing escape tokens.");
              throw SQLError.createSQLException(stringBuilder1.toString(), "S1000", paramMySQLConnection.getExceptionInterceptor());
            } 
          } 
          if (stringBuilder1 != null) {
            i = stringBuilder1.indexOf("?");
            if (i != -1) {
              StringBuilder stringBuilder5 = new StringBuilder(stringBuilder1.substring(0, i));
              stringBuilder5.append(str2);
              stringBuilder5.append(stringBuilder1.substring(i + 1, stringBuilder1.length()));
              return stringBuilder5.toString();
            } 
            stringBuilder2 = new StringBuilder("CAST(");
            stringBuilder2.append(str2);
            stringBuilder2.append(" AS ");
            stringBuilder2.append((String)stringBuilder1);
            stringBuilder2.append(")");
            return stringBuilder2.toString();
          } 
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Unsupported conversion type '");
          stringBuilder1.append(str3.trim());
          stringBuilder1.append("' found while processing escape token.");
          throw SQLError.createSQLException(stringBuilder1.toString(), "S1000", stringBuilder2.getExceptionInterceptor());
        } 
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Syntax error while processing {fn convert (... , ...)} token, missing closing parenthesis in token '");
        stringBuilder4.append((String)stringBuilder1);
        stringBuilder4.append("'.");
        throw SQLError.createSQLException(stringBuilder4.toString(), "42000", stringBuilder2.getExceptionInterceptor());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Syntax error while processing {fn convert (... , ...)} token, missing comma in token '");
      stringBuilder.append((String)stringBuilder1);
      stringBuilder.append("'.");
      throw SQLError.createSQLException(stringBuilder.toString(), "42000", stringBuilder2.getExceptionInterceptor());
    } 
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append("Syntax error while processing {fn convert (... , ...)} token, missing opening parenthesis in token '");
    stringBuilder3.append((String)stringBuilder1);
    stringBuilder3.append("'.");
    throw SQLError.createSQLException(stringBuilder3.toString(), "42000", stringBuilder2.getExceptionInterceptor());
  }
  
  private static void processTimeToken(MySQLConnection paramMySQLConnection, StringBuilder paramStringBuilder, String paramString) throws SQLException {
    int i = paramString.indexOf('\'');
    boolean bool = true;
    i++;
    int j = paramString.lastIndexOf('\'');
    if (i == -1 || j == -1) {
      paramStringBuilder.append(paramString);
      return;
    } 
    String str = paramString.substring(i, j);
    try {
      StringTokenizer stringTokenizer = new StringTokenizer();
      this(str, " :.");
      String str2 = stringTokenizer.nextToken();
      String str4 = stringTokenizer.nextToken();
      String str3 = stringTokenizer.nextToken();
      String str1 = "";
      if (stringTokenizer.hasMoreTokens() && paramMySQLConnection.versionMeetsMinimum(5, 6, 4)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(".");
        stringBuilder.append(stringTokenizer.nextToken());
        str1 = stringBuilder.toString();
      } else {
        bool = false;
      } 
      if (!paramMySQLConnection.getUseTimezone() || !paramMySQLConnection.getUseLegacyDatetimeCode()) {
        paramStringBuilder.append("'");
        paramStringBuilder.append(str2);
        paramStringBuilder.append(":");
        paramStringBuilder.append(str4);
        paramStringBuilder.append(":");
        paramStringBuilder.append(str3);
        paramStringBuilder.append(str1);
        paramStringBuilder.append("'");
        return;
      } 
      Calendar calendar = paramMySQLConnection.getCalendarInstanceForSessionOrNew();
      try {
        Time time = TimeUtil.changeTimezone(paramMySQLConnection, calendar, (Calendar)null, TimeUtil.fastTimeCreate(calendar, Integer.parseInt(str2), Integer.parseInt(str4), Integer.parseInt(str3), paramMySQLConnection.getExceptionInterceptor()), calendar.getTimeZone(), paramMySQLConnection.getServerTimezoneTZ(), false);
        paramStringBuilder.append("'");
        paramStringBuilder.append(time.toString());
        if (bool)
          paramStringBuilder.append(str1); 
        paramStringBuilder.append("'");
      } catch (NumberFormatException numberFormatException) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Syntax error in TIMESTAMP escape sequence '");
        stringBuilder.append(paramString);
        stringBuilder.append("'.");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramMySQLConnection.getExceptionInterceptor());
      } 
    } catch (NoSuchElementException noSuchElementException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Syntax error for escape sequence '");
      stringBuilder.append(str);
      stringBuilder.append("'");
      throw SQLError.createSQLException(stringBuilder.toString(), "42000", paramMySQLConnection.getExceptionInterceptor());
    } 
  }
  
  private static void processTimestampToken(MySQLConnection paramMySQLConnection, StringBuilder paramStringBuilder, String paramString) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: bipush #39
    //   3: invokevirtual indexOf : (I)I
    //   6: iconst_1
    //   7: iadd
    //   8: istore #4
    //   10: aload_2
    //   11: bipush #39
    //   13: invokevirtual lastIndexOf : (I)I
    //   16: istore_3
    //   17: iload #4
    //   19: iconst_m1
    //   20: if_icmpeq -> 786
    //   23: iload_3
    //   24: iconst_m1
    //   25: if_icmpne -> 31
    //   28: goto -> 786
    //   31: aload_2
    //   32: iload #4
    //   34: iload_3
    //   35: invokevirtual substring : (II)Ljava/lang/String;
    //   38: astore #11
    //   40: aload_0
    //   41: invokeinterface getUseLegacyDatetimeCode : ()Z
    //   46: ifne -> 157
    //   49: aload #11
    //   51: invokestatic valueOf : (Ljava/lang/String;)Ljava/sql/Timestamp;
    //   54: astore_2
    //   55: aload_0
    //   56: invokeinterface isServerTruncatesFracSecs : ()Z
    //   61: ifne -> 70
    //   64: iconst_1
    //   65: istore #10
    //   67: goto -> 73
    //   70: iconst_0
    //   71: istore #10
    //   73: aload_2
    //   74: bipush #6
    //   76: iload #10
    //   78: invokestatic adjustTimestampNanosPrecision : (Ljava/sql/Timestamp;IZ)Ljava/sql/Timestamp;
    //   81: astore_2
    //   82: aload_1
    //   83: aconst_null
    //   84: ldc_w '''yyyy-MM-dd HH:mm:ss'
    //   87: aconst_null
    //   88: aload_0
    //   89: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   94: invokestatic getSimpleDateFormat : (Ljava/text/SimpleDateFormat;Ljava/lang/String;Ljava/util/Calendar;Ljava/util/TimeZone;)Ljava/text/SimpleDateFormat;
    //   97: aload_2
    //   98: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload_2
    //   106: invokevirtual getNanos : ()I
    //   109: ifle -> 147
    //   112: aload_0
    //   113: iconst_5
    //   114: bipush #6
    //   116: iconst_4
    //   117: invokeinterface versionMeetsMinimum : (III)Z
    //   122: ifeq -> 147
    //   125: aload_1
    //   126: bipush #46
    //   128: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload_1
    //   133: aload_2
    //   134: invokevirtual getNanos : ()I
    //   137: iconst_1
    //   138: bipush #6
    //   140: invokestatic formatNanos : (IZI)Ljava/lang/String;
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload_1
    //   148: bipush #39
    //   150: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: goto -> 792
    //   157: new java/util/StringTokenizer
    //   160: astore #19
    //   162: aload #19
    //   164: aload #11
    //   166: ldc_w ' .-:'
    //   169: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   172: aload #19
    //   174: invokevirtual nextToken : ()Ljava/lang/String;
    //   177: astore #14
    //   179: aload #19
    //   181: invokevirtual nextToken : ()Ljava/lang/String;
    //   184: astore #16
    //   186: aload #19
    //   188: invokevirtual nextToken : ()Ljava/lang/String;
    //   191: astore #17
    //   193: aload #19
    //   195: invokevirtual nextToken : ()Ljava/lang/String;
    //   198: astore #15
    //   200: aload #19
    //   202: invokevirtual nextToken : ()Ljava/lang/String;
    //   205: astore #13
    //   207: aload #19
    //   209: invokevirtual nextToken : ()Ljava/lang/String;
    //   212: astore #18
    //   214: aload #19
    //   216: invokevirtual hasMoreTokens : ()Z
    //   219: istore #10
    //   221: iload #10
    //   223: ifeq -> 281
    //   226: aload_0
    //   227: iconst_5
    //   228: bipush #6
    //   230: iconst_4
    //   231: invokeinterface versionMeetsMinimum : (III)Z
    //   236: ifeq -> 281
    //   239: new java/lang/StringBuilder
    //   242: astore #12
    //   244: aload #12
    //   246: invokespecial <init> : ()V
    //   249: aload #12
    //   251: ldc_w '.'
    //   254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: pop
    //   258: aload #12
    //   260: aload #19
    //   262: invokevirtual nextToken : ()Ljava/lang/String;
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload #12
    //   271: invokevirtual toString : ()Ljava/lang/String;
    //   274: astore #12
    //   276: iconst_1
    //   277: istore_3
    //   278: goto -> 288
    //   281: ldc_w ''
    //   284: astore #12
    //   286: iconst_0
    //   287: istore_3
    //   288: aload_0
    //   289: invokeinterface getUseTimezone : ()Z
    //   294: ifne -> 410
    //   297: aload_0
    //   298: invokeinterface getUseJDBCCompliantTimezoneShift : ()Z
    //   303: ifne -> 410
    //   306: aload_1
    //   307: ldc '''
    //   309: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   312: pop
    //   313: aload_1
    //   314: aload #14
    //   316: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: pop
    //   320: aload_1
    //   321: ldc '-'
    //   323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   326: pop
    //   327: aload_1
    //   328: aload #16
    //   330: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   333: pop
    //   334: aload_1
    //   335: ldc '-'
    //   337: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: pop
    //   341: aload_1
    //   342: aload #17
    //   344: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   347: pop
    //   348: aload_1
    //   349: ldc_w ' '
    //   352: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   355: pop
    //   356: aload_1
    //   357: aload #15
    //   359: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   362: pop
    //   363: aload_1
    //   364: ldc_w ':'
    //   367: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   370: pop
    //   371: aload_1
    //   372: aload #13
    //   374: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   377: pop
    //   378: aload_1
    //   379: ldc_w ':'
    //   382: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   385: pop
    //   386: aload_1
    //   387: aload #18
    //   389: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: pop
    //   393: aload_1
    //   394: aload #12
    //   396: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   399: pop
    //   400: aload_1
    //   401: ldc '''
    //   403: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   406: pop
    //   407: goto -> 792
    //   410: aload_0
    //   411: invokeinterface getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   416: astore #19
    //   418: aload #14
    //   420: invokestatic parseInt : (Ljava/lang/String;)I
    //   423: istore #6
    //   425: aload #16
    //   427: invokestatic parseInt : (Ljava/lang/String;)I
    //   430: istore #9
    //   432: aload #17
    //   434: invokestatic parseInt : (Ljava/lang/String;)I
    //   437: istore #5
    //   439: aload #15
    //   441: invokestatic parseInt : (Ljava/lang/String;)I
    //   444: istore #4
    //   446: aload #13
    //   448: invokestatic parseInt : (Ljava/lang/String;)I
    //   451: istore #7
    //   453: aload #18
    //   455: invokestatic parseInt : (Ljava/lang/String;)I
    //   458: istore #8
    //   460: aload_0
    //   461: invokeinterface getUseGmtMillisForDatetimes : ()Z
    //   466: istore #10
    //   468: iload #10
    //   470: ifeq -> 487
    //   473: ldc_w 'GMT'
    //   476: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   479: invokestatic getInstance : (Ljava/util/TimeZone;)Ljava/util/Calendar;
    //   482: astore #13
    //   484: goto -> 490
    //   487: aconst_null
    //   488: astore #13
    //   490: aload_0
    //   491: aload #19
    //   493: aconst_null
    //   494: iload #10
    //   496: aload #13
    //   498: aload #19
    //   500: iload #6
    //   502: iload #9
    //   504: iload #5
    //   506: iload #4
    //   508: iload #7
    //   510: iload #8
    //   512: iconst_0
    //   513: invokestatic fastTimestampCreate : (ZLjava/util/Calendar;Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
    //   516: aload #19
    //   518: invokevirtual getTimeZone : ()Ljava/util/TimeZone;
    //   521: aload_0
    //   522: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   527: iconst_0
    //   528: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   531: astore #13
    //   533: aload_1
    //   534: ldc '''
    //   536: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   539: pop
    //   540: aload #13
    //   542: invokevirtual toString : ()Ljava/lang/String;
    //   545: astore #14
    //   547: aload #14
    //   549: ldc_w '.'
    //   552: invokevirtual indexOf : (Ljava/lang/String;)I
    //   555: istore #4
    //   557: aload #14
    //   559: astore #13
    //   561: iload #4
    //   563: iconst_m1
    //   564: if_icmpeq -> 577
    //   567: aload #14
    //   569: iconst_0
    //   570: iload #4
    //   572: invokevirtual substring : (II)Ljava/lang/String;
    //   575: astore #13
    //   577: aload_1
    //   578: aload #13
    //   580: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   583: pop
    //   584: iload_3
    //   585: ifeq -> 595
    //   588: aload_1
    //   589: aload #12
    //   591: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   594: pop
    //   595: aload_1
    //   596: ldc '''
    //   598: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   601: pop
    //   602: goto -> 792
    //   605: astore_1
    //   606: new java/lang/StringBuilder
    //   609: astore_1
    //   610: aload_1
    //   611: invokespecial <init> : ()V
    //   614: aload_1
    //   615: ldc_w 'Syntax error in TIMESTAMP escape sequence ''
    //   618: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   621: pop
    //   622: aload_1
    //   623: aload_2
    //   624: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   627: pop
    //   628: aload_1
    //   629: ldc_w ''.'
    //   632: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   635: pop
    //   636: aload_1
    //   637: invokevirtual toString : ()Ljava/lang/String;
    //   640: ldc_w 'S1009'
    //   643: aload_0
    //   644: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   649: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   652: athrow
    //   653: astore_1
    //   654: new java/lang/StringBuilder
    //   657: astore_2
    //   658: aload_2
    //   659: invokespecial <init> : ()V
    //   662: aload_2
    //   663: ldc_w 'Syntax error for TIMESTAMP escape sequence ''
    //   666: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   669: pop
    //   670: aload #11
    //   672: astore_1
    //   673: aload_2
    //   674: aload_1
    //   675: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   678: pop
    //   679: aload_2
    //   680: ldc '''
    //   682: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   685: pop
    //   686: aload_2
    //   687: invokevirtual toString : ()Ljava/lang/String;
    //   690: astore_2
    //   691: aload_0
    //   692: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   697: astore_1
    //   698: aload_2
    //   699: ldc '42000'
    //   701: aload_1
    //   702: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   705: athrow
    //   706: astore_1
    //   707: goto -> 732
    //   710: astore_1
    //   711: goto -> 722
    //   714: astore_1
    //   715: goto -> 719
    //   718: astore_1
    //   719: aload #11
    //   721: astore_2
    //   722: aload #11
    //   724: astore_2
    //   725: goto -> 732
    //   728: astore_1
    //   729: aload #11
    //   731: astore_2
    //   732: new java/lang/StringBuilder
    //   735: dup
    //   736: invokespecial <init> : ()V
    //   739: astore_2
    //   740: aload_2
    //   741: ldc_w 'Syntax error for TIMESTAMP escape sequence ''
    //   744: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   747: pop
    //   748: aload_2
    //   749: aload #11
    //   751: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   754: pop
    //   755: aload_2
    //   756: ldc '''
    //   758: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   761: pop
    //   762: aload_2
    //   763: invokevirtual toString : ()Ljava/lang/String;
    //   766: ldc '42000'
    //   768: aload_0
    //   769: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   774: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   777: astore_0
    //   778: aload_0
    //   779: aload_1
    //   780: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   783: pop
    //   784: aload_0
    //   785: athrow
    //   786: aload_1
    //   787: aload_2
    //   788: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   791: pop
    //   792: return
    //   793: astore_1
    //   794: goto -> 654
    // Exception table:
    //   from	to	target	type
    //   40	64	728	java/lang/IllegalArgumentException
    //   73	147	728	java/lang/IllegalArgumentException
    //   147	154	728	java/lang/IllegalArgumentException
    //   157	172	728	java/lang/IllegalArgumentException
    //   172	221	653	java/util/NoSuchElementException
    //   172	221	728	java/lang/IllegalArgumentException
    //   226	276	793	java/util/NoSuchElementException
    //   226	276	718	java/lang/IllegalArgumentException
    //   288	407	793	java/util/NoSuchElementException
    //   288	407	718	java/lang/IllegalArgumentException
    //   410	418	793	java/util/NoSuchElementException
    //   410	418	718	java/lang/IllegalArgumentException
    //   418	468	605	java/lang/NumberFormatException
    //   418	468	793	java/util/NoSuchElementException
    //   418	468	718	java/lang/IllegalArgumentException
    //   473	484	605	java/lang/NumberFormatException
    //   473	484	793	java/util/NoSuchElementException
    //   473	484	718	java/lang/IllegalArgumentException
    //   490	557	605	java/lang/NumberFormatException
    //   490	557	793	java/util/NoSuchElementException
    //   490	557	718	java/lang/IllegalArgumentException
    //   567	577	605	java/lang/NumberFormatException
    //   567	577	793	java/util/NoSuchElementException
    //   567	577	718	java/lang/IllegalArgumentException
    //   577	584	605	java/lang/NumberFormatException
    //   577	584	793	java/util/NoSuchElementException
    //   577	584	718	java/lang/IllegalArgumentException
    //   588	595	605	java/lang/NumberFormatException
    //   588	595	793	java/util/NoSuchElementException
    //   588	595	718	java/lang/IllegalArgumentException
    //   595	602	605	java/lang/NumberFormatException
    //   595	602	793	java/util/NoSuchElementException
    //   595	602	718	java/lang/IllegalArgumentException
    //   606	653	793	java/util/NoSuchElementException
    //   606	653	718	java/lang/IllegalArgumentException
    //   654	662	718	java/lang/IllegalArgumentException
    //   662	670	714	java/lang/IllegalArgumentException
    //   673	698	710	java/lang/IllegalArgumentException
    //   698	706	706	java/lang/IllegalArgumentException
  }
  
  private static String removeWhitespace(String paramString) {
    if (paramString == null)
      return null; 
    int i = paramString.length();
    StringBuilder stringBuilder = new StringBuilder(i);
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (!Character.isWhitespace(c))
        stringBuilder.append(c); 
    } 
    return stringBuilder.toString();
  }
}
