package com.mysql.jdbc;

import com.mysql.jdbc.log.LogUtils;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ResultSetImpl implements ResultSetInternalMethods {
  public static final char[] EMPTY_SPACE;
  
  private static final Constructor<?> JDBC_4_RS_4_ARG_CTOR;
  
  private static final Constructor<?> JDBC_4_RS_5_ARG_CTOR;
  
  private static final Constructor<?> JDBC_4_UPD_RS_5_ARG_CTOR;
  
  public static final double MAX_DIFF_PREC;
  
  public static final double MIN_DIFF_PREC = Float.parseFloat(Float.toString(Float.MIN_VALUE)) - Double.parseDouble(Float.toString(Float.MIN_VALUE));
  
  public static int resultCounter;
  
  public String catalog = null;
  
  public Map<String, Integer> columnLabelToIndex = null;
  
  public Map<String, Integer> columnNameToIndex = null;
  
  public Map<String, Integer> columnToIndexCache = null;
  
  public boolean[] columnUsed = null;
  
  public volatile MySQLConnection connection;
  
  public long connectionId = 0L;
  
  public int currentRow = -1;
  
  public boolean doingUpdates = false;
  
  public ProfilerEventHandler eventSink = null;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  public Calendar fastClientCal = null;
  
  public Calendar fastDefaultCal = null;
  
  public int fetchDirection = 1000;
  
  public int fetchSize = 0;
  
  public Field[] fields;
  
  public char firstCharOfQuery;
  
  public Map<String, Integer> fullColumnNameToIndex = null;
  
  public Calendar gmtCalendar = null;
  
  public boolean hasBuiltIndexMapping = false;
  
  private String invalidRowReason = null;
  
  public boolean isBinaryEncoded = false;
  
  public boolean isClosed = false;
  
  private boolean jdbcCompliantTruncationForReads;
  
  public ResultSetInternalMethods nextResultSet = null;
  
  public boolean onInsertRow = false;
  
  private boolean onValidRow = false;
  
  public StatementImpl owningStatement;
  
  private boolean padCharsWithSpace = false;
  
  public String pointOfOrigin;
  
  public boolean profileSql = false;
  
  public boolean reallyResult = false;
  
  public int resultId;
  
  public int resultSetConcurrency = 0;
  
  public int resultSetType = 0;
  
  public boolean retainOwningStatement;
  
  public RowData rowData;
  
  public String serverInfo = null;
  
  private TimeZone serverTimeZoneTz;
  
  public PreparedStatement statementUsedForFetchingRows;
  
  public ResultSetRow thisRow = null;
  
  public long updateCount;
  
  public long updateId = -1L;
  
  private boolean useColumnNamesInFindColumn;
  
  public boolean useFastDateParsing = false;
  
  private boolean useFastIntParsing = true;
  
  public boolean useLegacyDatetimeCode;
  
  private boolean useStrictFloatingPoint = false;
  
  public boolean useUsageAdvisor = false;
  
  public SQLWarning warningChain = null;
  
  public boolean wasNullFlag = false;
  
  public Statement wrapperStatement;
  
  static {
    MAX_DIFF_PREC = Float.parseFloat(Float.toString(Float.MAX_VALUE)) - Double.parseDouble(Float.toString(Float.MAX_VALUE));
    resultCounter = 1;
    EMPTY_SPACE = new char[255];
    while (true) {
      char[] arrayOfChar = EMPTY_SPACE;
      if (b < arrayOfChar.length) {
        arrayOfChar[b] = ' ';
        b++;
        continue;
      } 
      break;
    } 
  }
  
  public ResultSetImpl(long paramLong1, long paramLong2, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) {
    this.updateCount = paramLong1;
    this.updateId = paramLong2;
    this.reallyResult = false;
    this.fields = new Field[0];
    this.connection = paramMySQLConnection;
    this.owningStatement = paramStatementImpl;
    this.retainOwningStatement = false;
    if (this.connection != null) {
      this.exceptionInterceptor = this.connection.getExceptionInterceptor();
      this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
      this.connectionId = this.connection.getId();
      this.serverTimeZoneTz = this.connection.getServerTimezoneTZ();
      this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
      this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
    } 
  }
  
  public ResultSetImpl(String paramString, Field[] paramArrayOfField, RowData paramRowData, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) throws SQLException {
    this.connection = paramMySQLConnection;
    this.retainOwningStatement = false;
    if (this.connection != null) {
      this.exceptionInterceptor = this.connection.getExceptionInterceptor();
      this.useStrictFloatingPoint = this.connection.getStrictFloatingPoint();
      this.connectionId = this.connection.getId();
      this.useFastDateParsing = this.connection.getUseFastDateParsing();
      this.profileSql = this.connection.getProfileSql();
      this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
      this.jdbcCompliantTruncationForReads = this.connection.getJdbcCompliantTruncationForReads();
      this.useFastIntParsing = this.connection.getUseFastIntParsing();
      this.serverTimeZoneTz = this.connection.getServerTimezoneTZ();
      this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
    } 
    this.owningStatement = paramStatementImpl;
    this.catalog = paramString;
    this.fields = paramArrayOfField;
    this.rowData = paramRowData;
    this.updateCount = paramRowData.size();
    this.reallyResult = true;
    if (this.rowData.size() > 0) {
      if (this.updateCount == 1L && this.thisRow == null) {
        this.rowData.close();
        this.updateCount = -1L;
      } 
    } else {
      this.thisRow = null;
    } 
    this.rowData.setOwner(this);
    if (this.fields != null)
      initializeWithMetadata(); 
    this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
    this.useColumnNamesInFindColumn = this.connection.getUseColumnNamesInFindColumn();
    setRowPositionValidity();
  }
  
  public static boolean arraysEqual(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    boolean bool = true;
    if (paramArrayOfbyte1 == null) {
      if (paramArrayOfbyte2 != null)
        bool = false; 
      return bool;
    } 
    if (paramArrayOfbyte2 == null)
      return false; 
    if (paramArrayOfbyte1.length != paramArrayOfbyte2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfbyte1.length; b++) {
      if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b])
        return false; 
    } 
    return true;
  }
  
  private boolean byteArrayToBoolean(int paramInt) throws SQLException {
    byte[] arrayOfByte = this.thisRow.getColumnValue(paramInt);
    boolean bool2 = true;
    if (arrayOfByte == null) {
      this.wasNullFlag = true;
      return false;
    } 
    this.wasNullFlag = false;
    if (arrayOfByte.length == 0)
      return false; 
    paramInt = arrayOfByte[0];
    if (paramInt == 49)
      return true; 
    if (paramInt == 48)
      return false; 
    boolean bool1 = bool2;
    if (paramInt != -1)
      if (paramInt > 0) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  private void checkForIntegerTruncation(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) throws SQLException {
    if (this.jdbcCompliantTruncationForReads && (paramInt2 == Integer.MIN_VALUE || paramInt2 == Integer.MAX_VALUE)) {
      String str2;
      String str1 = null;
      if (paramArrayOfbyte == null)
        str1 = this.thisRow.getString(paramInt1, this.fields[paramInt1].getEncoding(), this.connection); 
      if (str1 == null) {
        str2 = StringUtils.toString(paramArrayOfbyte);
      } else {
        str2 = str1;
      } 
      long l = Long.parseLong(str2);
      if (l < -2147483648L || l > 2147483647L) {
        str2 = str1;
        if (str1 == null)
          str2 = StringUtils.toString(paramArrayOfbyte); 
        throwRangeException(str2, paramInt1 + 1, 4);
      } 
    } 
  }
  
  private void checkForLongTruncation(int paramInt, byte[] paramArrayOfbyte, long paramLong) throws SQLException {
    if (paramLong == Long.MIN_VALUE || paramLong == Long.MAX_VALUE) {
      String str2;
      String str1 = null;
      if (paramArrayOfbyte == null)
        str1 = this.thisRow.getString(paramInt, this.fields[paramInt].getEncoding(), this.connection); 
      if (str1 == null) {
        str2 = StringUtils.toString(paramArrayOfbyte);
      } else {
        str2 = str1;
      } 
      double d = Double.parseDouble(str2);
      if (d < -9.223372036854776E18D || d > 9.223372036854776E18D) {
        str2 = str1;
        if (str1 == null)
          str2 = StringUtils.toString(paramArrayOfbyte); 
        throwRangeException(str2, paramInt + 1, -5);
      } 
    } 
  }
  
  public static BigInteger convertLongToUlong(long paramLong) {
    byte b2 = (byte)(int)(0xFFL & paramLong);
    byte b3 = (byte)(int)(paramLong >>> 8L);
    byte b6 = (byte)(int)(paramLong >>> 16L);
    byte b7 = (byte)(int)(paramLong >>> 24L);
    byte b1 = (byte)(int)(paramLong >>> 32L);
    byte b4 = (byte)(int)(paramLong >>> 40L);
    byte b5 = (byte)(int)(paramLong >>> 48L);
    return new BigInteger(1, new byte[] { (byte)(int)(paramLong >>> 56L), b5, b4, b1, b7, b6, b3, b2 });
  }
  
  private String convertToZeroLiteralStringWithEmptyCheck() throws SQLException {
    if (this.connection.getEmptyStringsConvertToZero())
      return "0"; 
    throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018", getExceptionInterceptor());
  }
  
  private int convertToZeroWithEmptyCheck() throws SQLException {
    if (this.connection.getEmptyStringsConvertToZero())
      return 0; 
    throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018", getExceptionInterceptor());
  }
  
  private String extractStringFromNativeColumn(int paramInt1, int paramInt2) throws SQLException {
    paramInt1--;
    this.wasNullFlag = false;
    if (this.thisRow.isNull(paramInt1)) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    String str = this.fields[paramInt1].getEncoding();
    return this.thisRow.getString(paramInt1, str, this.connection);
  }
  
  private final BigDecimal getBigDecimalFromString(String paramString, int paramInt1, int paramInt2) throws SQLException {
    if (paramString != null) {
      if (paramString.length() == 0) {
        BigDecimal bigDecimal = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
        try {
          return bigDecimal.setScale(paramInt2);
        } catch (ArithmeticException arithmeticException) {
          try {
            return bigDecimal.setScale(paramInt2, 4);
          } catch (ArithmeticException arithmeticException1) {
            throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { paramString, Integer.valueOf(paramInt1) }), "S1009");
          } 
        } 
      } 
      try {
        null = new BigDecimal();
        this(paramString);
        return null.setScale(paramInt2);
      } catch (ArithmeticException arithmeticException) {
        try {
          null = new BigDecimal();
          this(paramString);
          return null.setScale(paramInt2, 4);
        } catch (ArithmeticException arithmeticException1) {
          SQLException sQLException = new SQLException();
          this(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { paramString, Integer.valueOf(paramInt1) }), "S1009");
          throw sQLException;
        } 
      } catch (NumberFormatException numberFormatException) {}
      Field[] arrayOfField = this.fields;
      int i = paramInt1 - 1;
      if (arrayOfField[i].getMysqlType() == 16) {
        long l = getNumericRepresentationOfSQLBitType(paramInt1);
        try {
          null = new BigDecimal();
          this(l);
          return null.setScale(paramInt2);
        } catch (ArithmeticException arithmeticException) {
          try {
            null = new BigDecimal();
            this(l);
            return null.setScale(paramInt2, 4);
          } catch (ArithmeticException arithmeticException1) {
            throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { paramString, Integer.valueOf(paramInt1) }), "S1009");
          } 
        } 
      } 
      if (this.fields[i].getMysqlType() == 1 && this.connection.getTinyInt1isBit() && this.fields[i].getLength() == 1L)
        return (new BigDecimal(paramString.equalsIgnoreCase("true"))).setScale(paramInt2); 
      throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { paramString, Integer.valueOf(paramInt1) }), "S1009");
    } 
    return null;
  }
  
  private final boolean getBooleanFromString(String paramString) throws SQLException {
    boolean bool = false;
    null = bool;
    if (paramString != null) {
      null = bool;
      if (paramString.length() > 0) {
        char c = Character.toLowerCase(paramString.charAt(0));
        if (c != 't' && c != 'y' && c != '1') {
          null = bool;
          return paramString.equals("-1") ? true : null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  private final byte getByteFromString(String paramString, int paramInt) throws SQLException {
    if (paramString != null && paramString.length() == 0) {
      paramInt = convertToZeroWithEmptyCheck();
      return (byte)paramInt;
    } 
    if (paramString == null)
      return 0; 
    paramString = paramString.trim();
    try {
      if (paramString.indexOf(".") != -1) {
        double d = Double.parseDouble(paramString);
        if (this.jdbcCompliantTruncationForReads && (d < -128.0D || d > 127.0D))
          throwRangeException(paramString, paramInt, -6); 
        paramInt = (int)d;
        return (byte)paramInt;
      } 
      long l = Long.parseLong(paramString);
      if (this.jdbcCompliantTruncationForReads && (l < -128L || l > 127L))
        throwRangeException(String.valueOf(l), paramInt, -6); 
      paramInt = (int)l;
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("ResultSet.Value____173"));
      stringBuilder.append(paramString);
      stringBuilder.append(Messages.getString("ResultSet.___is_out_of_range_[-127,127]_174"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
    return (byte)paramInt;
  }
  
  private final byte[] getBytesFromString(String paramString) throws SQLException {
    return (paramString != null) ? StringUtils.getBytes(paramString, this.connection.getEncoding(), this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), this.connection, getExceptionInterceptor()) : null;
  }
  
  private final Reader getCharacterStreamFromString(String paramString) throws SQLException {
    return (paramString != null) ? new StringReader(paramString) : null;
  }
  
  private final Clob getClobFromString(String paramString) throws SQLException {
    return new Clob(paramString, getExceptionInterceptor());
  }
  
  private final Date getDateFromString(String paramString, int paramInt, Calendar paramCalendar) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield wasNullFlag : Z
    //   5: aload_1
    //   6: ifnonnull -> 16
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield wasNullFlag : Z
    //   14: aconst_null
    //   15: areturn
    //   16: aload_1
    //   17: invokevirtual trim : ()Ljava/lang/String;
    //   20: astore #8
    //   22: aload #8
    //   24: astore_1
    //   25: aload #8
    //   27: ldc_w '.'
    //   30: invokevirtual indexOf : (Ljava/lang/String;)I
    //   33: istore #4
    //   35: aload #8
    //   37: astore #7
    //   39: iload #4
    //   41: iconst_m1
    //   42: if_icmple -> 58
    //   45: aload #8
    //   47: astore_1
    //   48: aload #8
    //   50: iconst_0
    //   51: iload #4
    //   53: invokevirtual substring : (II)Ljava/lang/String;
    //   56: astore #7
    //   58: aload #7
    //   60: astore_1
    //   61: aload #7
    //   63: ldc_w '0'
    //   66: invokevirtual equals : (Ljava/lang/Object;)Z
    //   69: ifne -> 890
    //   72: aload #7
    //   74: astore_1
    //   75: aload #7
    //   77: ldc_w '0000-00-00'
    //   80: invokevirtual equals : (Ljava/lang/Object;)Z
    //   83: ifne -> 890
    //   86: aload #7
    //   88: astore_1
    //   89: aload #7
    //   91: ldc_w '0000-00-00 00:00:00'
    //   94: invokevirtual equals : (Ljava/lang/Object;)Z
    //   97: ifne -> 890
    //   100: aload #7
    //   102: astore_1
    //   103: aload #7
    //   105: ldc_w '00000000000000'
    //   108: invokevirtual equals : (Ljava/lang/Object;)Z
    //   111: ifne -> 890
    //   114: aload #7
    //   116: astore_1
    //   117: aload #7
    //   119: ldc_w '0'
    //   122: invokevirtual equals : (Ljava/lang/Object;)Z
    //   125: ifeq -> 131
    //   128: goto -> 890
    //   131: aload #7
    //   133: astore_1
    //   134: aload_0
    //   135: getfield fields : [Lcom/mysql/jdbc/Field;
    //   138: astore #8
    //   140: iload_2
    //   141: iconst_1
    //   142: isub
    //   143: istore #4
    //   145: aload #7
    //   147: astore_1
    //   148: aload #8
    //   150: iload #4
    //   152: aaload
    //   153: invokevirtual getMysqlType : ()I
    //   156: bipush #7
    //   158: if_icmpne -> 533
    //   161: aload #7
    //   163: astore_1
    //   164: aload #7
    //   166: invokevirtual length : ()I
    //   169: istore #4
    //   171: iload #4
    //   173: iconst_2
    //   174: if_icmpeq -> 483
    //   177: iload #4
    //   179: iconst_4
    //   180: if_icmpeq -> 424
    //   183: iload #4
    //   185: bipush #6
    //   187: if_icmpeq -> 355
    //   190: iload #4
    //   192: bipush #8
    //   194: if_icmpeq -> 313
    //   197: iload #4
    //   199: bipush #10
    //   201: if_icmpeq -> 355
    //   204: iload #4
    //   206: bipush #12
    //   208: if_icmpeq -> 355
    //   211: iload #4
    //   213: bipush #14
    //   215: if_icmpeq -> 313
    //   218: iload #4
    //   220: bipush #19
    //   222: if_icmpeq -> 271
    //   225: iload #4
    //   227: bipush #21
    //   229: if_icmpne -> 235
    //   232: goto -> 271
    //   235: aload #7
    //   237: astore_1
    //   238: ldc_w 'ResultSet.Bad_format_for_Date'
    //   241: iconst_2
    //   242: anewarray java/lang/Object
    //   245: dup
    //   246: iconst_0
    //   247: aload #7
    //   249: aastore
    //   250: dup
    //   251: iconst_1
    //   252: iload_2
    //   253: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   256: aastore
    //   257: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   260: ldc_w 'S1009'
    //   263: aload_0
    //   264: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   267: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   270: athrow
    //   271: aload #7
    //   273: astore_1
    //   274: aload_0
    //   275: aload_3
    //   276: aload #7
    //   278: iconst_0
    //   279: iconst_4
    //   280: invokevirtual substring : (II)Ljava/lang/String;
    //   283: invokestatic parseInt : (Ljava/lang/String;)I
    //   286: aload #7
    //   288: iconst_5
    //   289: bipush #7
    //   291: invokevirtual substring : (II)Ljava/lang/String;
    //   294: invokestatic parseInt : (Ljava/lang/String;)I
    //   297: aload #7
    //   299: bipush #8
    //   301: bipush #10
    //   303: invokevirtual substring : (II)Ljava/lang/String;
    //   306: invokestatic parseInt : (Ljava/lang/String;)I
    //   309: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   312: areturn
    //   313: aload #7
    //   315: astore_1
    //   316: aload_0
    //   317: aload_3
    //   318: aload #7
    //   320: iconst_0
    //   321: iconst_4
    //   322: invokevirtual substring : (II)Ljava/lang/String;
    //   325: invokestatic parseInt : (Ljava/lang/String;)I
    //   328: aload #7
    //   330: iconst_4
    //   331: bipush #6
    //   333: invokevirtual substring : (II)Ljava/lang/String;
    //   336: invokestatic parseInt : (Ljava/lang/String;)I
    //   339: aload #7
    //   341: bipush #6
    //   343: bipush #8
    //   345: invokevirtual substring : (II)Ljava/lang/String;
    //   348: invokestatic parseInt : (Ljava/lang/String;)I
    //   351: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   354: areturn
    //   355: aload #7
    //   357: astore_1
    //   358: aload #7
    //   360: iconst_0
    //   361: iconst_2
    //   362: invokevirtual substring : (II)Ljava/lang/String;
    //   365: invokestatic parseInt : (Ljava/lang/String;)I
    //   368: istore #5
    //   370: iload #5
    //   372: istore #4
    //   374: iload #5
    //   376: bipush #69
    //   378: if_icmpgt -> 388
    //   381: iload #5
    //   383: bipush #100
    //   385: iadd
    //   386: istore #4
    //   388: aload #7
    //   390: astore_1
    //   391: aload_0
    //   392: aload_3
    //   393: iload #4
    //   395: sipush #1900
    //   398: iadd
    //   399: aload #7
    //   401: iconst_2
    //   402: iconst_4
    //   403: invokevirtual substring : (II)Ljava/lang/String;
    //   406: invokestatic parseInt : (Ljava/lang/String;)I
    //   409: aload #7
    //   411: iconst_4
    //   412: bipush #6
    //   414: invokevirtual substring : (II)Ljava/lang/String;
    //   417: invokestatic parseInt : (Ljava/lang/String;)I
    //   420: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   423: areturn
    //   424: aload #7
    //   426: astore_1
    //   427: aload #7
    //   429: iconst_0
    //   430: iconst_4
    //   431: invokevirtual substring : (II)Ljava/lang/String;
    //   434: invokestatic parseInt : (Ljava/lang/String;)I
    //   437: istore #5
    //   439: iload #5
    //   441: istore #4
    //   443: iload #5
    //   445: bipush #69
    //   447: if_icmpgt -> 457
    //   450: iload #5
    //   452: bipush #100
    //   454: iadd
    //   455: istore #4
    //   457: aload #7
    //   459: astore_1
    //   460: aload_0
    //   461: aload_3
    //   462: iload #4
    //   464: sipush #1900
    //   467: iadd
    //   468: aload #7
    //   470: iconst_2
    //   471: iconst_4
    //   472: invokevirtual substring : (II)Ljava/lang/String;
    //   475: invokestatic parseInt : (Ljava/lang/String;)I
    //   478: iconst_1
    //   479: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   482: areturn
    //   483: aload #7
    //   485: astore_1
    //   486: aload #7
    //   488: iconst_0
    //   489: iconst_2
    //   490: invokevirtual substring : (II)Ljava/lang/String;
    //   493: invokestatic parseInt : (Ljava/lang/String;)I
    //   496: istore #5
    //   498: iload #5
    //   500: istore #4
    //   502: iload #5
    //   504: bipush #69
    //   506: if_icmpgt -> 516
    //   509: iload #5
    //   511: bipush #100
    //   513: iadd
    //   514: istore #4
    //   516: aload #7
    //   518: astore_1
    //   519: aload_0
    //   520: aload_3
    //   521: iload #4
    //   523: sipush #1900
    //   526: iadd
    //   527: iconst_1
    //   528: iconst_1
    //   529: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   532: areturn
    //   533: aload #7
    //   535: astore_1
    //   536: aload_0
    //   537: getfield fields : [Lcom/mysql/jdbc/Field;
    //   540: iload #4
    //   542: aaload
    //   543: invokevirtual getMysqlType : ()I
    //   546: bipush #13
    //   548: if_icmpne -> 643
    //   551: aload #7
    //   553: astore_1
    //   554: aload #7
    //   556: invokevirtual length : ()I
    //   559: iconst_2
    //   560: if_icmpeq -> 596
    //   563: aload #7
    //   565: astore_1
    //   566: aload #7
    //   568: invokevirtual length : ()I
    //   571: iconst_1
    //   572: if_icmpne -> 578
    //   575: goto -> 596
    //   578: aload #7
    //   580: astore_1
    //   581: aload #7
    //   583: iconst_0
    //   584: iconst_4
    //   585: invokevirtual substring : (II)Ljava/lang/String;
    //   588: invokestatic parseInt : (Ljava/lang/String;)I
    //   591: istore #4
    //   593: goto -> 630
    //   596: aload #7
    //   598: astore_1
    //   599: aload #7
    //   601: invokestatic parseInt : (Ljava/lang/String;)I
    //   604: istore #5
    //   606: iload #5
    //   608: istore #4
    //   610: iload #5
    //   612: bipush #69
    //   614: if_icmpgt -> 624
    //   617: iload #5
    //   619: bipush #100
    //   621: iadd
    //   622: istore #4
    //   624: wide iinc #4 1900
    //   630: aload #7
    //   632: astore_1
    //   633: aload_0
    //   634: aload_3
    //   635: iload #4
    //   637: iconst_1
    //   638: iconst_1
    //   639: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   642: areturn
    //   643: aload #7
    //   645: astore_1
    //   646: aload_0
    //   647: getfield fields : [Lcom/mysql/jdbc/Field;
    //   650: iload #4
    //   652: aaload
    //   653: invokevirtual getMysqlType : ()I
    //   656: bipush #11
    //   658: if_icmpne -> 675
    //   661: aload #7
    //   663: astore_1
    //   664: aload_0
    //   665: aload_3
    //   666: sipush #1970
    //   669: iconst_1
    //   670: iconst_1
    //   671: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   674: areturn
    //   675: aload #7
    //   677: astore_1
    //   678: aload #7
    //   680: invokevirtual length : ()I
    //   683: bipush #10
    //   685: if_icmpge -> 751
    //   688: aload #7
    //   690: astore_1
    //   691: aload #7
    //   693: invokevirtual length : ()I
    //   696: bipush #8
    //   698: if_icmpne -> 715
    //   701: aload #7
    //   703: astore_1
    //   704: aload_0
    //   705: aload_3
    //   706: sipush #1970
    //   709: iconst_1
    //   710: iconst_1
    //   711: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   714: areturn
    //   715: aload #7
    //   717: astore_1
    //   718: ldc_w 'ResultSet.Bad_format_for_Date'
    //   721: iconst_2
    //   722: anewarray java/lang/Object
    //   725: dup
    //   726: iconst_0
    //   727: aload #7
    //   729: aastore
    //   730: dup
    //   731: iconst_1
    //   732: iload_2
    //   733: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   736: aastore
    //   737: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   740: ldc_w 'S1009'
    //   743: aload_0
    //   744: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   747: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   750: athrow
    //   751: aload #7
    //   753: astore_1
    //   754: aload #7
    //   756: invokevirtual length : ()I
    //   759: bipush #18
    //   761: if_icmpeq -> 815
    //   764: aload #7
    //   766: astore_1
    //   767: aload #7
    //   769: iconst_0
    //   770: iconst_4
    //   771: invokevirtual substring : (II)Ljava/lang/String;
    //   774: invokestatic parseInt : (Ljava/lang/String;)I
    //   777: istore #4
    //   779: aload #7
    //   781: astore_1
    //   782: aload #7
    //   784: iconst_5
    //   785: bipush #7
    //   787: invokevirtual substring : (II)Ljava/lang/String;
    //   790: invokestatic parseInt : (Ljava/lang/String;)I
    //   793: istore #5
    //   795: aload #7
    //   797: astore_1
    //   798: aload #7
    //   800: bipush #8
    //   802: bipush #10
    //   804: invokevirtual substring : (II)Ljava/lang/String;
    //   807: invokestatic parseInt : (Ljava/lang/String;)I
    //   810: istore #6
    //   812: goto -> 875
    //   815: aload #7
    //   817: astore_1
    //   818: new java/util/StringTokenizer
    //   821: astore #8
    //   823: aload #7
    //   825: astore_1
    //   826: aload #8
    //   828: aload #7
    //   830: ldc_w '- '
    //   833: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   836: aload #7
    //   838: astore_1
    //   839: aload #8
    //   841: invokevirtual nextToken : ()Ljava/lang/String;
    //   844: invokestatic parseInt : (Ljava/lang/String;)I
    //   847: istore #4
    //   849: aload #7
    //   851: astore_1
    //   852: aload #8
    //   854: invokevirtual nextToken : ()Ljava/lang/String;
    //   857: invokestatic parseInt : (Ljava/lang/String;)I
    //   860: istore #5
    //   862: aload #7
    //   864: astore_1
    //   865: aload #8
    //   867: invokevirtual nextToken : ()Ljava/lang/String;
    //   870: invokestatic parseInt : (Ljava/lang/String;)I
    //   873: istore #6
    //   875: aload #7
    //   877: astore_1
    //   878: aload_0
    //   879: aload_3
    //   880: iload #4
    //   882: iload #5
    //   884: iload #6
    //   886: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   889: areturn
    //   890: aload #7
    //   892: astore_1
    //   893: ldc_w 'convertToNull'
    //   896: aload_0
    //   897: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   900: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   905: invokevirtual equals : (Ljava/lang/Object;)Z
    //   908: ifeq -> 921
    //   911: aload #7
    //   913: astore_1
    //   914: aload_0
    //   915: iconst_1
    //   916: putfield wasNullFlag : Z
    //   919: aconst_null
    //   920: areturn
    //   921: aload #7
    //   923: astore_1
    //   924: ldc_w 'exception'
    //   927: aload_0
    //   928: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   931: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   936: invokevirtual equals : (Ljava/lang/Object;)Z
    //   939: ifne -> 954
    //   942: aload #7
    //   944: astore_1
    //   945: aload_0
    //   946: aload_3
    //   947: iconst_1
    //   948: iconst_1
    //   949: iconst_1
    //   950: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   953: areturn
    //   954: aload #7
    //   956: astore_1
    //   957: new java/lang/StringBuilder
    //   960: astore_3
    //   961: aload #7
    //   963: astore_1
    //   964: aload_3
    //   965: invokespecial <init> : ()V
    //   968: aload #7
    //   970: astore_1
    //   971: aload_3
    //   972: ldc_w 'Value ''
    //   975: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   978: pop
    //   979: aload #7
    //   981: astore_1
    //   982: aload_3
    //   983: aload #7
    //   985: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   988: pop
    //   989: aload #7
    //   991: astore_1
    //   992: aload_3
    //   993: ldc_w '' can not be represented as java.sql.Date'
    //   996: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   999: pop
    //   1000: aload #7
    //   1002: astore_1
    //   1003: aload_3
    //   1004: invokevirtual toString : ()Ljava/lang/String;
    //   1007: ldc_w 'S1009'
    //   1010: aload_0
    //   1011: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1014: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1017: athrow
    //   1018: astore_3
    //   1019: goto -> 1023
    //   1022: astore_3
    //   1023: ldc_w 'ResultSet.Bad_format_for_Date'
    //   1026: iconst_2
    //   1027: anewarray java/lang/Object
    //   1030: dup
    //   1031: iconst_0
    //   1032: aload_1
    //   1033: aastore
    //   1034: dup
    //   1035: iconst_1
    //   1036: iload_2
    //   1037: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1040: aastore
    //   1041: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1044: ldc_w 'S1009'
    //   1047: aload_0
    //   1048: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1051: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1054: astore_1
    //   1055: aload_1
    //   1056: aload_3
    //   1057: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1060: pop
    //   1061: aload_1
    //   1062: athrow
    //   1063: astore_1
    //   1064: aload_1
    //   1065: athrow
    // Exception table:
    //   from	to	target	type
    //   0	5	1063	java/sql/SQLException
    //   0	5	1022	java/lang/Exception
    //   9	14	1063	java/sql/SQLException
    //   9	14	1022	java/lang/Exception
    //   16	22	1063	java/sql/SQLException
    //   16	22	1022	java/lang/Exception
    //   25	35	1063	java/sql/SQLException
    //   25	35	1018	java/lang/Exception
    //   48	58	1063	java/sql/SQLException
    //   48	58	1018	java/lang/Exception
    //   61	72	1063	java/sql/SQLException
    //   61	72	1018	java/lang/Exception
    //   75	86	1063	java/sql/SQLException
    //   75	86	1018	java/lang/Exception
    //   89	100	1063	java/sql/SQLException
    //   89	100	1018	java/lang/Exception
    //   103	114	1063	java/sql/SQLException
    //   103	114	1018	java/lang/Exception
    //   117	128	1063	java/sql/SQLException
    //   117	128	1018	java/lang/Exception
    //   134	140	1063	java/sql/SQLException
    //   134	140	1018	java/lang/Exception
    //   148	161	1063	java/sql/SQLException
    //   148	161	1018	java/lang/Exception
    //   164	171	1063	java/sql/SQLException
    //   164	171	1018	java/lang/Exception
    //   238	271	1063	java/sql/SQLException
    //   238	271	1018	java/lang/Exception
    //   274	313	1063	java/sql/SQLException
    //   274	313	1018	java/lang/Exception
    //   316	355	1063	java/sql/SQLException
    //   316	355	1018	java/lang/Exception
    //   358	370	1063	java/sql/SQLException
    //   358	370	1018	java/lang/Exception
    //   391	424	1063	java/sql/SQLException
    //   391	424	1018	java/lang/Exception
    //   427	439	1063	java/sql/SQLException
    //   427	439	1018	java/lang/Exception
    //   460	483	1063	java/sql/SQLException
    //   460	483	1018	java/lang/Exception
    //   486	498	1063	java/sql/SQLException
    //   486	498	1018	java/lang/Exception
    //   519	533	1063	java/sql/SQLException
    //   519	533	1018	java/lang/Exception
    //   536	551	1063	java/sql/SQLException
    //   536	551	1018	java/lang/Exception
    //   554	563	1063	java/sql/SQLException
    //   554	563	1018	java/lang/Exception
    //   566	575	1063	java/sql/SQLException
    //   566	575	1018	java/lang/Exception
    //   581	593	1063	java/sql/SQLException
    //   581	593	1018	java/lang/Exception
    //   599	606	1063	java/sql/SQLException
    //   599	606	1018	java/lang/Exception
    //   633	643	1063	java/sql/SQLException
    //   633	643	1018	java/lang/Exception
    //   646	661	1063	java/sql/SQLException
    //   646	661	1018	java/lang/Exception
    //   664	675	1063	java/sql/SQLException
    //   664	675	1018	java/lang/Exception
    //   678	688	1063	java/sql/SQLException
    //   678	688	1018	java/lang/Exception
    //   691	701	1063	java/sql/SQLException
    //   691	701	1018	java/lang/Exception
    //   704	715	1063	java/sql/SQLException
    //   704	715	1018	java/lang/Exception
    //   718	751	1063	java/sql/SQLException
    //   718	751	1018	java/lang/Exception
    //   754	764	1063	java/sql/SQLException
    //   754	764	1018	java/lang/Exception
    //   767	779	1063	java/sql/SQLException
    //   767	779	1018	java/lang/Exception
    //   782	795	1063	java/sql/SQLException
    //   782	795	1018	java/lang/Exception
    //   798	812	1063	java/sql/SQLException
    //   798	812	1018	java/lang/Exception
    //   818	823	1063	java/sql/SQLException
    //   818	823	1018	java/lang/Exception
    //   826	836	1063	java/sql/SQLException
    //   826	836	1018	java/lang/Exception
    //   839	849	1063	java/sql/SQLException
    //   839	849	1018	java/lang/Exception
    //   852	862	1063	java/sql/SQLException
    //   852	862	1018	java/lang/Exception
    //   865	875	1063	java/sql/SQLException
    //   865	875	1018	java/lang/Exception
    //   878	890	1063	java/sql/SQLException
    //   878	890	1018	java/lang/Exception
    //   893	911	1063	java/sql/SQLException
    //   893	911	1018	java/lang/Exception
    //   914	919	1063	java/sql/SQLException
    //   914	919	1018	java/lang/Exception
    //   924	942	1063	java/sql/SQLException
    //   924	942	1018	java/lang/Exception
    //   945	954	1063	java/sql/SQLException
    //   945	954	1018	java/lang/Exception
    //   957	961	1063	java/sql/SQLException
    //   957	961	1018	java/lang/Exception
    //   964	968	1063	java/sql/SQLException
    //   964	968	1018	java/lang/Exception
    //   971	979	1063	java/sql/SQLException
    //   971	979	1018	java/lang/Exception
    //   982	989	1063	java/sql/SQLException
    //   982	989	1018	java/lang/Exception
    //   992	1000	1063	java/sql/SQLException
    //   992	1000	1018	java/lang/Exception
    //   1003	1018	1063	java/sql/SQLException
    //   1003	1018	1018	java/lang/Exception
  }
  
  private TimeZone getDefaultTimeZone() {
    TimeZone timeZone;
    if (this.useLegacyDatetimeCode) {
      timeZone = this.connection.getDefaultTimeZone();
    } else {
      timeZone = this.serverTimeZoneTz;
    } 
    return timeZone;
  }
  
  private final double getDoubleFromString(String paramString, int paramInt) throws SQLException {
    return getDoubleInternal(paramString, paramInt);
  }
  
  private Calendar getFastClientCalendar() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield fastClientCal : Ljava/util/Calendar;
    //   6: ifnonnull -> 25
    //   9: new java/util/GregorianCalendar
    //   12: astore_1
    //   13: aload_1
    //   14: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   17: invokespecial <init> : (Ljava/util/Locale;)V
    //   20: aload_0
    //   21: aload_1
    //   22: putfield fastClientCal : Ljava/util/Calendar;
    //   25: aload_0
    //   26: getfield fastClientCal : Ljava/util/Calendar;
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: areturn
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	34	finally
    //   25	30	34	finally
  }
  
  private Calendar getFastDefaultCalendar() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield fastDefaultCal : Ljava/util/Calendar;
    //   6: ifnonnull -> 33
    //   9: new java/util/GregorianCalendar
    //   12: astore_1
    //   13: aload_1
    //   14: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   17: invokespecial <init> : (Ljava/util/Locale;)V
    //   20: aload_0
    //   21: aload_1
    //   22: putfield fastDefaultCal : Ljava/util/Calendar;
    //   25: aload_1
    //   26: aload_0
    //   27: invokespecial getDefaultTimeZone : ()Ljava/util/TimeZone;
    //   30: invokevirtual setTimeZone : (Ljava/util/TimeZone;)V
    //   33: aload_0
    //   34: getfield fastDefaultCal : Ljava/util/Calendar;
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: areturn
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	42	finally
    //   33	38	42	finally
  }
  
  private final float getFloatFromString(String paramString, int paramInt) throws SQLException {
    if (paramString != null)
      try {
        if (paramString.length() == 0)
          return convertToZeroWithEmptyCheck(); 
        float f = Float.parseFloat(paramString);
        if (this.jdbcCompliantTruncationForReads && (f == Float.MIN_VALUE || f == Float.MAX_VALUE)) {
          double d = Double.parseDouble(paramString);
          if (d < 1.401298464324817E-45D - MIN_DIFF_PREC || d > 3.4028234663852886E38D - MAX_DIFF_PREC)
            throwRangeException(String.valueOf(d), paramInt, 6); 
        } 
        return f;
      } catch (NumberFormatException numberFormatException) {
        try {
          Double double_ = new Double();
          this(paramString);
          float f = double_.floatValue();
          boolean bool = this.jdbcCompliantTruncationForReads;
          if (bool && ((bool && f == Float.NEGATIVE_INFINITY) || f == Float.POSITIVE_INFINITY))
            throwRangeException(double_.toString(), paramInt, 6); 
          return f;
        } catch (NumberFormatException numberFormatException1) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getFloat()_-____200"));
          stringBuilder.append(paramString);
          stringBuilder.append(Messages.getString("ResultSet.___in_column__201"));
          stringBuilder.append(paramInt);
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
        } 
      }  
    return 0.0F;
  }
  
  public static ResultSetImpl getInstance(long paramLong1, long paramLong2, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) throws SQLException {
    if (!Util.isJdbc4())
      return new ResultSetImpl(paramLong1, paramLong2, paramMySQLConnection, paramStatementImpl); 
    Constructor<?> constructor = JDBC_4_RS_4_ARG_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (ResultSetImpl)Util.handleNewInstance(constructor, new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2), paramMySQLConnection, paramStatementImpl }, exceptionInterceptor);
  }
  
  public static ResultSetImpl getInstance(String paramString, Field[] paramArrayOfField, RowData paramRowData, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl, boolean paramBoolean) throws SQLException {
    if (!Util.isJdbc4())
      return !paramBoolean ? new ResultSetImpl(paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl) : new UpdatableResultSet(paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl); 
    if (!paramBoolean) {
      Constructor<?> constructor1 = JDBC_4_RS_5_ARG_CTOR;
      ExceptionInterceptor exceptionInterceptor1 = paramMySQLConnection.getExceptionInterceptor();
      return (ResultSetImpl)Util.handleNewInstance(constructor1, new Object[] { paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl }, exceptionInterceptor1);
    } 
    Constructor<?> constructor = JDBC_4_UPD_RS_5_ARG_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (ResultSetImpl)Util.handleNewInstance(constructor, new Object[] { paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl }, exceptionInterceptor);
  }
  
  private final int getIntFromString(String paramString, int paramInt) throws SQLException {
    if (paramString != null) {
      String str = paramString;
      try {
        if (paramString.length() == 0) {
          str = paramString;
          return convertToZeroWithEmptyCheck();
        } 
        str = paramString;
        if (paramString.indexOf("e") == -1) {
          str = paramString;
          if (paramString.indexOf("E") == -1) {
            str = paramString;
            if (paramString.indexOf(".") == -1) {
              str = paramString;
              paramString = paramString.trim();
              str = paramString;
              int i = Integer.parseInt(paramString);
              str = paramString;
              if (this.jdbcCompliantTruncationForReads && (i == Integer.MIN_VALUE || i == Integer.MAX_VALUE)) {
                str = paramString;
                long l = Long.parseLong(paramString);
                if (l < -2147483648L || l > 2147483647L) {
                  str = paramString;
                  throwRangeException(String.valueOf(l), paramInt, 4);
                } 
              } 
              return i;
            } 
          } 
        } 
        str = paramString;
        double d = Double.parseDouble(paramString);
        str = paramString;
        if (this.jdbcCompliantTruncationForReads && (d < -2.147483648E9D || d > 2.147483647E9D)) {
          str = paramString;
          throwRangeException(String.valueOf(d), paramInt, 4);
        } 
        return (int)d;
      } catch (NumberFormatException numberFormatException) {
        try {
          double d = Double.parseDouble(str);
          if (this.jdbcCompliantTruncationForReads && (d < -2.147483648E9D || d > 2.147483647E9D))
            throwRangeException(String.valueOf(d), paramInt, 4); 
          return (int)d;
        } catch (NumberFormatException numberFormatException1) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____206"));
          stringBuilder.append(str);
          stringBuilder.append(Messages.getString("ResultSet.___in_column__207"));
          stringBuilder.append(paramInt);
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
        } 
      } 
    } 
    return 0;
  }
  
  private int getIntWithOverflowCheck(int paramInt) throws SQLException {
    int i = this.thisRow.getInt(paramInt);
    checkForIntegerTruncation(paramInt, null, i);
    return i;
  }
  
  private long getLong(int paramInt, boolean paramBoolean) throws SQLException {
    checkRowPos();
    checkColumnBounds(paramInt);
    if (!this.isBinaryEncoded) {
      int i = paramInt - 1;
      if (this.thisRow.isNull(i)) {
        this.wasNullFlag = true;
        return 0L;
      } 
      this.wasNullFlag = false;
      if (this.fields[i].getMysqlType() == 16)
        return getNumericRepresentationOfSQLBitType(paramInt); 
      if (this.useFastIntParsing) {
        if (this.thisRow.length(i) == 0L)
          return convertToZeroWithEmptyCheck(); 
        if (!this.thisRow.isFloatingPointNumber(i))
          try {
            return getLongWithOverflowCheck(i, paramBoolean);
          } catch (NumberFormatException null) {
            try {
              return parseLongAsDouble(i, this.thisRow.getString(i, this.fields[i].getEncoding(), this.connection));
            } catch (NumberFormatException numberFormatException) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79"));
              stringBuilder.append(this.thisRow.getString(i, this.fields[i].getEncoding(), this.connection));
              stringBuilder.append("'");
              throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
            } 
          }  
      } 
      numberFormatException2 = null;
      try {
        String str = getString(paramInt);
        if (str == null)
          return 0L; 
        try {
          return (str.length() == 0) ? convertToZeroWithEmptyCheck() : ((str.indexOf("e") == -1 && str.indexOf("E") == -1) ? parseLongWithOverflowCheck(i, null, str, paramBoolean) : parseLongAsDouble(i, str));
        } catch (NumberFormatException numberFormatException2) {}
      } catch (NumberFormatException numberFormatException1) {
        numberFormatException1 = numberFormatException2;
      } 
      try {
        return parseLongAsDouble(i, (String)numberFormatException1);
      } catch (NumberFormatException numberFormatException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79"));
        stringBuilder.append((String)numberFormatException1);
        stringBuilder.append("'");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      } 
    } 
    return getNativeLong(paramInt, paramBoolean, true);
  }
  
  private final long getLongFromString(String paramString, int paramInt) throws SQLException {
    if (paramString != null)
      try {
        return (paramString.length() == 0) ? convertToZeroWithEmptyCheck() : ((paramString.indexOf("e") == -1 && paramString.indexOf("E") == -1) ? parseLongWithOverflowCheck(paramInt, null, paramString, true) : parseLongAsDouble(paramInt, paramString));
      } catch (NumberFormatException numberFormatException) {
        try {
          return parseLongAsDouble(paramInt, paramString);
        } catch (NumberFormatException numberFormatException1) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____211"));
          stringBuilder.append(paramString);
          stringBuilder.append(Messages.getString("ResultSet.___in_column__212"));
          stringBuilder.append(paramInt + 1);
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
        } 
      }  
    return 0L;
  }
  
  private long getLongWithOverflowCheck(int paramInt, boolean paramBoolean) throws SQLException {
    long l = this.thisRow.getLong(paramInt);
    if (paramBoolean)
      checkForLongTruncation(paramInt, null, l); 
    return l;
  }
  
  private String getNativeConvertToString(int paramInt, Field paramField) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #15
    //   11: aload #15
    //   13: monitorenter
    //   14: aload_2
    //   15: invokevirtual getSQLType : ()I
    //   18: istore #7
    //   20: aload_2
    //   21: invokevirtual getMysqlType : ()I
    //   24: istore #6
    //   26: iload #7
    //   28: bipush #12
    //   30: if_icmpeq -> 1298
    //   33: iload #7
    //   35: bipush #16
    //   37: if_icmpeq -> 1270
    //   40: iload #7
    //   42: tableswitch default -> 84, -7 -> 1254, -6 -> 1198, -5 -> 1126, -4 -> 905, -3 -> 905, -2 -> 905, -1 -> 1298
    //   84: iload #7
    //   86: tableswitch default -> 132, 1 -> 1298, 2 -> 790, 3 -> 790, 4 -> 723, 5 -> 670, 6 -> 644, 7 -> 616, 8 -> 644
    //   132: iload #7
    //   134: tableswitch default -> 160, 91 -> 393, 92 -> 357, 93 -> 173
    //   160: aload_0
    //   161: iload_1
    //   162: iload #6
    //   164: invokespecial extractStringFromNativeColumn : (II)Ljava/lang/String;
    //   167: astore_2
    //   168: aload #15
    //   170: monitorexit
    //   171: aload_2
    //   172: areturn
    //   173: aload_0
    //   174: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   177: invokeinterface getNoDatetimeStringSync : ()Z
    //   182: ifeq -> 269
    //   185: aload_0
    //   186: iload_1
    //   187: iconst_1
    //   188: invokevirtual getNativeBytes : (IZ)[B
    //   191: astore_2
    //   192: aload_2
    //   193: ifnonnull -> 201
    //   196: aload #15
    //   198: monitorexit
    //   199: aconst_null
    //   200: areturn
    //   201: aload_2
    //   202: arraylength
    //   203: ifne -> 213
    //   206: aload #15
    //   208: monitorexit
    //   209: ldc_w '0000-00-00 00:00:00'
    //   212: areturn
    //   213: aload_2
    //   214: iconst_0
    //   215: baload
    //   216: istore #8
    //   218: aload_2
    //   219: iconst_1
    //   220: baload
    //   221: istore #10
    //   223: aload_2
    //   224: iconst_2
    //   225: baload
    //   226: istore #9
    //   228: aload_2
    //   229: iconst_3
    //   230: baload
    //   231: istore #7
    //   233: iload #8
    //   235: sipush #255
    //   238: iand
    //   239: iload #10
    //   241: sipush #255
    //   244: iand
    //   245: bipush #8
    //   247: ishl
    //   248: ior
    //   249: ifne -> 269
    //   252: iload #9
    //   254: ifne -> 269
    //   257: iload #7
    //   259: ifne -> 269
    //   262: aload #15
    //   264: monitorexit
    //   265: ldc_w '0000-00-00 00:00:00'
    //   268: areturn
    //   269: aload_0
    //   270: iload_1
    //   271: aconst_null
    //   272: aload_0
    //   273: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   276: invokeinterface getDefaultTimeZone : ()Ljava/util/TimeZone;
    //   281: iconst_0
    //   282: invokespecial getNativeTimestamp : (ILjava/util/Calendar;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   285: astore_2
    //   286: aload_2
    //   287: ifnonnull -> 295
    //   290: aload #15
    //   292: monitorexit
    //   293: aconst_null
    //   294: areturn
    //   295: aload_2
    //   296: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   299: astore_2
    //   300: aload_0
    //   301: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   304: invokeinterface getNoDatetimeStringSync : ()Z
    //   309: ifne -> 317
    //   312: aload #15
    //   314: monitorexit
    //   315: aload_2
    //   316: areturn
    //   317: aload_2
    //   318: ldc_w '.0'
    //   321: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   324: ifeq -> 344
    //   327: aload_2
    //   328: iconst_0
    //   329: aload_2
    //   330: invokevirtual length : ()I
    //   333: iconst_2
    //   334: isub
    //   335: invokevirtual substring : (II)Ljava/lang/String;
    //   338: astore_2
    //   339: aload #15
    //   341: monitorexit
    //   342: aload_2
    //   343: areturn
    //   344: aload_0
    //   345: iload_1
    //   346: iload #6
    //   348: invokespecial extractStringFromNativeColumn : (II)Ljava/lang/String;
    //   351: astore_2
    //   352: aload #15
    //   354: monitorexit
    //   355: aload_2
    //   356: areturn
    //   357: aload_0
    //   358: iload_1
    //   359: aconst_null
    //   360: aload_0
    //   361: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   364: invokeinterface getDefaultTimeZone : ()Ljava/util/TimeZone;
    //   369: iconst_0
    //   370: invokespecial getNativeTime : (ILjava/util/Calendar;Ljava/util/TimeZone;Z)Ljava/sql/Time;
    //   373: astore_2
    //   374: aload_2
    //   375: ifnonnull -> 383
    //   378: aload #15
    //   380: monitorexit
    //   381: aconst_null
    //   382: areturn
    //   383: aload_2
    //   384: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   387: astore_2
    //   388: aload #15
    //   390: monitorexit
    //   391: aload_2
    //   392: areturn
    //   393: iload #6
    //   395: bipush #13
    //   397: if_icmpne -> 495
    //   400: aload_0
    //   401: iload_1
    //   402: invokevirtual getNativeShort : (I)S
    //   405: istore_1
    //   406: aload_0
    //   407: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   410: invokeinterface getYearIsDateType : ()Z
    //   415: ifne -> 438
    //   418: aload_0
    //   419: getfield wasNullFlag : Z
    //   422: ifeq -> 430
    //   425: aload #15
    //   427: monitorexit
    //   428: aconst_null
    //   429: areturn
    //   430: aload #15
    //   432: monitorexit
    //   433: iload_1
    //   434: invokestatic valueOf : (I)Ljava/lang/String;
    //   437: areturn
    //   438: iload_1
    //   439: istore #6
    //   441: aload_2
    //   442: invokevirtual getLength : ()J
    //   445: ldc2_w 2
    //   448: lcmp
    //   449: ifne -> 477
    //   452: iload_1
    //   453: istore #6
    //   455: iload_1
    //   456: bipush #69
    //   458: if_icmpgt -> 468
    //   461: iload_1
    //   462: bipush #100
    //   464: iadd
    //   465: i2s
    //   466: istore #6
    //   468: iload #6
    //   470: sipush #1900
    //   473: iadd
    //   474: i2s
    //   475: istore #6
    //   477: aload_0
    //   478: aconst_null
    //   479: iload #6
    //   481: iconst_1
    //   482: iconst_1
    //   483: invokevirtual fastDateCreate : (Ljava/util/Calendar;III)Ljava/sql/Date;
    //   486: invokevirtual toString : ()Ljava/lang/String;
    //   489: astore_2
    //   490: aload #15
    //   492: monitorexit
    //   493: aload_2
    //   494: areturn
    //   495: aload_0
    //   496: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   499: invokeinterface getNoDatetimeStringSync : ()Z
    //   504: ifeq -> 591
    //   507: aload_0
    //   508: iload_1
    //   509: iconst_1
    //   510: invokevirtual getNativeBytes : (IZ)[B
    //   513: astore_2
    //   514: aload_2
    //   515: ifnonnull -> 523
    //   518: aload #15
    //   520: monitorexit
    //   521: aconst_null
    //   522: areturn
    //   523: aload_2
    //   524: arraylength
    //   525: ifne -> 535
    //   528: aload #15
    //   530: monitorexit
    //   531: ldc_w '0000-00-00'
    //   534: areturn
    //   535: aload_2
    //   536: iconst_0
    //   537: baload
    //   538: istore #8
    //   540: aload_2
    //   541: iconst_1
    //   542: baload
    //   543: istore #6
    //   545: aload_2
    //   546: iconst_2
    //   547: baload
    //   548: istore #7
    //   550: aload_2
    //   551: iconst_3
    //   552: baload
    //   553: istore #9
    //   555: iload #8
    //   557: sipush #255
    //   560: iand
    //   561: iload #6
    //   563: sipush #255
    //   566: iand
    //   567: bipush #8
    //   569: ishl
    //   570: ior
    //   571: ifne -> 591
    //   574: iload #7
    //   576: ifne -> 591
    //   579: iload #9
    //   581: ifne -> 591
    //   584: aload #15
    //   586: monitorexit
    //   587: ldc_w '0000-00-00'
    //   590: areturn
    //   591: aload_0
    //   592: iload_1
    //   593: invokevirtual getNativeDate : (I)Ljava/sql/Date;
    //   596: astore_2
    //   597: aload_2
    //   598: ifnonnull -> 606
    //   601: aload #15
    //   603: monitorexit
    //   604: aconst_null
    //   605: areturn
    //   606: aload_2
    //   607: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   610: astore_2
    //   611: aload #15
    //   613: monitorexit
    //   614: aload_2
    //   615: areturn
    //   616: aload_0
    //   617: iload_1
    //   618: invokevirtual getNativeFloat : (I)F
    //   621: fstore #5
    //   623: aload_0
    //   624: getfield wasNullFlag : Z
    //   627: ifeq -> 635
    //   630: aload #15
    //   632: monitorexit
    //   633: aconst_null
    //   634: areturn
    //   635: aload #15
    //   637: monitorexit
    //   638: fload #5
    //   640: invokestatic valueOf : (F)Ljava/lang/String;
    //   643: areturn
    //   644: aload_0
    //   645: iload_1
    //   646: invokevirtual getNativeDouble : (I)D
    //   649: dstore_3
    //   650: aload_0
    //   651: getfield wasNullFlag : Z
    //   654: ifeq -> 662
    //   657: aload #15
    //   659: monitorexit
    //   660: aconst_null
    //   661: areturn
    //   662: aload #15
    //   664: monitorexit
    //   665: dload_3
    //   666: invokestatic valueOf : (D)Ljava/lang/String;
    //   669: areturn
    //   670: aload_0
    //   671: iload_1
    //   672: iconst_0
    //   673: invokevirtual getNativeInt : (IZ)I
    //   676: istore_1
    //   677: aload_0
    //   678: getfield wasNullFlag : Z
    //   681: ifeq -> 689
    //   684: aload #15
    //   686: monitorexit
    //   687: aconst_null
    //   688: areturn
    //   689: aload_2
    //   690: invokevirtual isUnsigned : ()Z
    //   693: ifeq -> 715
    //   696: iload_1
    //   697: iflt -> 703
    //   700: goto -> 715
    //   703: aload #15
    //   705: monitorexit
    //   706: iload_1
    //   707: ldc_w 65535
    //   710: iand
    //   711: invokestatic valueOf : (I)Ljava/lang/String;
    //   714: areturn
    //   715: aload #15
    //   717: monitorexit
    //   718: iload_1
    //   719: invokestatic valueOf : (I)Ljava/lang/String;
    //   722: areturn
    //   723: aload_0
    //   724: iload_1
    //   725: iconst_0
    //   726: invokevirtual getNativeInt : (IZ)I
    //   729: istore_1
    //   730: aload_0
    //   731: getfield wasNullFlag : Z
    //   734: ifeq -> 742
    //   737: aload #15
    //   739: monitorexit
    //   740: aconst_null
    //   741: areturn
    //   742: aload_2
    //   743: invokevirtual isUnsigned : ()Z
    //   746: ifeq -> 782
    //   749: iload_1
    //   750: ifge -> 782
    //   753: aload_2
    //   754: invokevirtual getMysqlType : ()I
    //   757: bipush #9
    //   759: if_icmpne -> 765
    //   762: goto -> 782
    //   765: iload_1
    //   766: i2l
    //   767: lstore #11
    //   769: aload #15
    //   771: monitorexit
    //   772: lload #11
    //   774: ldc2_w 4294967295
    //   777: land
    //   778: invokestatic valueOf : (J)Ljava/lang/String;
    //   781: areturn
    //   782: aload #15
    //   784: monitorexit
    //   785: iload_1
    //   786: invokestatic valueOf : (I)Ljava/lang/String;
    //   789: areturn
    //   790: aload_0
    //   791: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   794: iload_1
    //   795: iconst_1
    //   796: isub
    //   797: invokevirtual getColumnValue : (I)[B
    //   800: invokestatic toAsciiString : ([B)Ljava/lang/String;
    //   803: astore_2
    //   804: aload_2
    //   805: ifnull -> 895
    //   808: aload_0
    //   809: iconst_0
    //   810: putfield wasNullFlag : Z
    //   813: aload_2
    //   814: invokevirtual length : ()I
    //   817: ifne -> 839
    //   820: new java/math/BigDecimal
    //   823: astore_2
    //   824: aload_2
    //   825: iconst_0
    //   826: invokespecial <init> : (I)V
    //   829: aload_2
    //   830: invokevirtual toString : ()Ljava/lang/String;
    //   833: astore_2
    //   834: aload #15
    //   836: monitorexit
    //   837: aload_2
    //   838: areturn
    //   839: new java/math/BigDecimal
    //   842: astore #14
    //   844: aload #14
    //   846: aload_2
    //   847: invokespecial <init> : (Ljava/lang/String;)V
    //   850: aload #14
    //   852: invokevirtual toString : ()Ljava/lang/String;
    //   855: astore_2
    //   856: aload #15
    //   858: monitorexit
    //   859: aload_2
    //   860: areturn
    //   861: astore #14
    //   863: ldc_w 'ResultSet.Bad_format_for_BigDecimal'
    //   866: iconst_2
    //   867: anewarray java/lang/Object
    //   870: dup
    //   871: iconst_0
    //   872: aload_2
    //   873: aastore
    //   874: dup
    //   875: iconst_1
    //   876: iload_1
    //   877: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   880: aastore
    //   881: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   884: ldc_w 'S1009'
    //   887: aload_0
    //   888: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   891: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   894: athrow
    //   895: aload_0
    //   896: iconst_1
    //   897: putfield wasNullFlag : Z
    //   900: aload #15
    //   902: monitorexit
    //   903: aconst_null
    //   904: areturn
    //   905: aload_2
    //   906: invokevirtual isBlob : ()Z
    //   909: ifne -> 925
    //   912: aload_0
    //   913: iload_1
    //   914: iload #6
    //   916: invokespecial extractStringFromNativeColumn : (II)Ljava/lang/String;
    //   919: astore_2
    //   920: aload #15
    //   922: monitorexit
    //   923: aload_2
    //   924: areturn
    //   925: aload_2
    //   926: invokevirtual isBinary : ()Z
    //   929: ifne -> 945
    //   932: aload_0
    //   933: iload_1
    //   934: iload #6
    //   936: invokespecial extractStringFromNativeColumn : (II)Ljava/lang/String;
    //   939: astore_2
    //   940: aload #15
    //   942: monitorexit
    //   943: aload_2
    //   944: areturn
    //   945: aload_0
    //   946: iload_1
    //   947: invokevirtual getBytes : (I)[B
    //   950: astore #14
    //   952: aload_0
    //   953: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   956: invokeinterface getAutoDeserialize : ()Z
    //   961: ifeq -> 1113
    //   964: aload #14
    //   966: ifnull -> 1113
    //   969: aload #14
    //   971: arraylength
    //   972: iconst_2
    //   973: if_icmplt -> 1113
    //   976: aload #14
    //   978: astore_2
    //   979: aload #14
    //   981: iconst_0
    //   982: baload
    //   983: bipush #-84
    //   985: if_icmpne -> 1103
    //   988: aload #14
    //   990: iconst_1
    //   991: baload
    //   992: istore_1
    //   993: aload #14
    //   995: astore_2
    //   996: iload_1
    //   997: bipush #-19
    //   999: if_icmpne -> 1103
    //   1002: new java/io/ByteArrayInputStream
    //   1005: astore #17
    //   1007: aload #17
    //   1009: aload #14
    //   1011: invokespecial <init> : ([B)V
    //   1014: new java/io/ObjectInputStream
    //   1017: astore #16
    //   1019: aload #16
    //   1021: aload #17
    //   1023: invokespecial <init> : (Ljava/io/InputStream;)V
    //   1026: aload #16
    //   1028: invokevirtual readObject : ()Ljava/lang/Object;
    //   1031: astore_2
    //   1032: aload #16
    //   1034: invokevirtual close : ()V
    //   1037: aload #17
    //   1039: invokevirtual close : ()V
    //   1042: goto -> 1103
    //   1045: astore_2
    //   1046: new java/lang/StringBuilder
    //   1049: astore #14
    //   1051: aload #14
    //   1053: invokespecial <init> : ()V
    //   1056: aload #14
    //   1058: ldc_w 'ResultSet.Class_not_found___91'
    //   1061: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1064: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1067: pop
    //   1068: aload #14
    //   1070: aload_2
    //   1071: invokevirtual toString : ()Ljava/lang/String;
    //   1074: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1077: pop
    //   1078: aload #14
    //   1080: ldc_w 'ResultSet._while_reading_serialized_object_92'
    //   1083: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1086: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1089: pop
    //   1090: aload #14
    //   1092: invokevirtual toString : ()Ljava/lang/String;
    //   1095: aload_0
    //   1096: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1099: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1102: athrow
    //   1103: aload_2
    //   1104: invokevirtual toString : ()Ljava/lang/String;
    //   1107: astore_2
    //   1108: aload #15
    //   1110: monitorexit
    //   1111: aload_2
    //   1112: areturn
    //   1113: aload_0
    //   1114: iload_1
    //   1115: iload #6
    //   1117: invokespecial extractStringFromNativeColumn : (II)Ljava/lang/String;
    //   1120: astore_2
    //   1121: aload #15
    //   1123: monitorexit
    //   1124: aload_2
    //   1125: areturn
    //   1126: aload_2
    //   1127: invokevirtual isUnsigned : ()Z
    //   1130: ifne -> 1163
    //   1133: aload_0
    //   1134: iload_1
    //   1135: iconst_0
    //   1136: iconst_1
    //   1137: invokevirtual getNativeLong : (IZZ)J
    //   1140: lstore #11
    //   1142: aload_0
    //   1143: getfield wasNullFlag : Z
    //   1146: ifeq -> 1154
    //   1149: aload #15
    //   1151: monitorexit
    //   1152: aconst_null
    //   1153: areturn
    //   1154: aload #15
    //   1156: monitorexit
    //   1157: lload #11
    //   1159: invokestatic valueOf : (J)Ljava/lang/String;
    //   1162: areturn
    //   1163: aload_0
    //   1164: iload_1
    //   1165: iconst_0
    //   1166: iconst_0
    //   1167: invokevirtual getNativeLong : (IZZ)J
    //   1170: lstore #11
    //   1172: aload_0
    //   1173: getfield wasNullFlag : Z
    //   1176: ifeq -> 1184
    //   1179: aload #15
    //   1181: monitorexit
    //   1182: aconst_null
    //   1183: areturn
    //   1184: lload #11
    //   1186: invokestatic convertLongToUlong : (J)Ljava/math/BigInteger;
    //   1189: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   1192: astore_2
    //   1193: aload #15
    //   1195: monitorexit
    //   1196: aload_2
    //   1197: areturn
    //   1198: aload_0
    //   1199: iload_1
    //   1200: iconst_0
    //   1201: invokevirtual getNativeByte : (IZ)B
    //   1204: istore_1
    //   1205: aload_0
    //   1206: getfield wasNullFlag : Z
    //   1209: ifeq -> 1217
    //   1212: aload #15
    //   1214: monitorexit
    //   1215: aconst_null
    //   1216: areturn
    //   1217: aload_2
    //   1218: invokevirtual isUnsigned : ()Z
    //   1221: ifeq -> 1246
    //   1224: iload_1
    //   1225: iflt -> 1231
    //   1228: goto -> 1246
    //   1231: iload_1
    //   1232: sipush #255
    //   1235: iand
    //   1236: i2s
    //   1237: istore_1
    //   1238: aload #15
    //   1240: monitorexit
    //   1241: iload_1
    //   1242: invokestatic valueOf : (I)Ljava/lang/String;
    //   1245: areturn
    //   1246: aload #15
    //   1248: monitorexit
    //   1249: iload_1
    //   1250: invokestatic valueOf : (I)Ljava/lang/String;
    //   1253: areturn
    //   1254: aload_0
    //   1255: iload_1
    //   1256: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   1259: lstore #11
    //   1261: aload #15
    //   1263: monitorexit
    //   1264: lload #11
    //   1266: invokestatic valueOf : (J)Ljava/lang/String;
    //   1269: areturn
    //   1270: aload_0
    //   1271: iload_1
    //   1272: invokevirtual getBoolean : (I)Z
    //   1275: istore #13
    //   1277: aload_0
    //   1278: getfield wasNullFlag : Z
    //   1281: ifeq -> 1289
    //   1284: aload #15
    //   1286: monitorexit
    //   1287: aconst_null
    //   1288: areturn
    //   1289: aload #15
    //   1291: monitorexit
    //   1292: iload #13
    //   1294: invokestatic valueOf : (Z)Ljava/lang/String;
    //   1297: areturn
    //   1298: aload_0
    //   1299: iload_1
    //   1300: iload #6
    //   1302: invokespecial extractStringFromNativeColumn : (II)Ljava/lang/String;
    //   1305: astore_2
    //   1306: aload #15
    //   1308: monitorexit
    //   1309: aload_2
    //   1310: areturn
    //   1311: astore_2
    //   1312: aload #15
    //   1314: monitorexit
    //   1315: aload_2
    //   1316: athrow
    //   1317: astore_2
    //   1318: aload #14
    //   1320: astore_2
    //   1321: goto -> 1103
    // Exception table:
    //   from	to	target	type
    //   14	26	1311	finally
    //   160	171	1311	finally
    //   173	192	1311	finally
    //   196	199	1311	finally
    //   201	209	1311	finally
    //   262	265	1311	finally
    //   269	286	1311	finally
    //   290	293	1311	finally
    //   295	315	1311	finally
    //   317	342	1311	finally
    //   344	355	1311	finally
    //   357	374	1311	finally
    //   378	381	1311	finally
    //   383	391	1311	finally
    //   400	428	1311	finally
    //   430	438	1311	finally
    //   441	452	1311	finally
    //   477	493	1311	finally
    //   495	514	1311	finally
    //   518	521	1311	finally
    //   523	531	1311	finally
    //   584	587	1311	finally
    //   591	597	1311	finally
    //   601	604	1311	finally
    //   606	614	1311	finally
    //   616	633	1311	finally
    //   635	644	1311	finally
    //   644	660	1311	finally
    //   662	670	1311	finally
    //   670	687	1311	finally
    //   689	696	1311	finally
    //   703	715	1311	finally
    //   715	723	1311	finally
    //   723	740	1311	finally
    //   742	749	1311	finally
    //   753	762	1311	finally
    //   769	782	1311	finally
    //   782	790	1311	finally
    //   790	804	1311	finally
    //   808	837	1311	finally
    //   839	850	861	java/lang/NumberFormatException
    //   839	850	1311	finally
    //   850	859	1311	finally
    //   863	895	1311	finally
    //   895	903	1311	finally
    //   905	923	1311	finally
    //   925	943	1311	finally
    //   945	964	1311	finally
    //   969	976	1311	finally
    //   1002	1042	1045	java/lang/ClassNotFoundException
    //   1002	1042	1317	java/io/IOException
    //   1002	1042	1311	finally
    //   1046	1103	1311	finally
    //   1103	1111	1311	finally
    //   1113	1124	1311	finally
    //   1126	1152	1311	finally
    //   1154	1163	1311	finally
    //   1163	1182	1311	finally
    //   1184	1196	1311	finally
    //   1198	1215	1311	finally
    //   1217	1224	1311	finally
    //   1238	1246	1311	finally
    //   1246	1254	1311	finally
    //   1254	1270	1311	finally
    //   1270	1287	1311	finally
    //   1289	1298	1311	finally
    //   1298	1309	1311	finally
    //   1312	1315	1311	finally
  }
  
  private Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    Time time;
    checkRowPos();
    checkColumnBounds(paramInt);
    int i = paramInt - 1;
    paramInt = this.fields[i].getMysqlType();
    if (paramInt == 11) {
      time = this.thisRow.getNativeTime(i, paramCalendar, paramTimeZone, paramBoolean, this.connection, this);
    } else {
      time = (Time)this.thisRow.getNativeDateTimeValue(i, null, 92, paramInt, paramTimeZone, paramBoolean, this.connection, this);
    } 
    if (time == null) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    return time;
  }
  
  private Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    Timestamp timestamp;
    checkRowPos();
    checkColumnBounds(paramInt);
    int i = this.fields[--paramInt].getMysqlType();
    if (i != 7 && i != 12) {
      timestamp = (Timestamp)this.thisRow.getNativeDateTimeValue(paramInt, null, 93, i, paramTimeZone, paramBoolean, this.connection, this);
    } else {
      timestamp = this.thisRow.getNativeTimestamp(paramInt, (Calendar)timestamp, paramTimeZone, paramBoolean, this.connection, this);
    } 
    if (timestamp == null) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    return timestamp;
  }
  
  private long getNumericRepresentationOfSQLBitType(int paramInt) throws SQLException {
    ResultSetRow resultSetRow = this.thisRow;
    byte[] arrayOfByte = resultSetRow.getColumnValue(--paramInt);
    boolean bool = this.fields[paramInt].isSingleBit();
    boolean bool2 = false;
    if (bool || arrayOfByte.length == 1)
      return arrayOfByte[0]; 
    long[] arrayOfLong = new long[arrayOfByte.length];
    paramInt = arrayOfByte.length - 1;
    boolean bool1 = false;
    while (paramInt >= 0) {
      arrayOfLong[paramInt] = (arrayOfByte[paramInt] & 0xFF) << bool1;
      bool1 += true;
      paramInt--;
    } 
    long l = 0L;
    for (paramInt = bool2; paramInt < arrayOfByte.length; paramInt++)
      l |= arrayOfLong[paramInt]; 
    return l;
  }
  
  private Object getObjectDeserializingIfNeeded(int paramInt) throws SQLException {
    Field field = this.fields[paramInt - 1];
    if (field.isBinary() || field.isBlob()) {
      Object object;
      byte[] arrayOfByte2 = getBytes(paramInt);
      byte[] arrayOfByte1 = arrayOfByte2;
      if (this.connection.getAutoDeserialize()) {
        arrayOfByte1 = arrayOfByte2;
        if (arrayOfByte2 != null) {
          arrayOfByte1 = arrayOfByte2;
          if (arrayOfByte2.length >= 2)
            if (arrayOfByte2[0] == -84 && arrayOfByte2[1] == -19) {
              StringBuilder stringBuilder;
              try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
                this(arrayOfByte2);
                ObjectInputStream objectInputStream = new ObjectInputStream();
                this(byteArrayInputStream);
                object = objectInputStream.readObject();
                objectInputStream.close();
                byteArrayInputStream.close();
              } catch (ClassNotFoundException null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(Messages.getString("ResultSet.Class_not_found___91"));
                stringBuilder.append(object.toString());
                stringBuilder.append(Messages.getString("ResultSet._while_reading_serialized_object_92"));
                throw SQLError.createSQLException(stringBuilder.toString(), getExceptionInterceptor());
              } catch (IOException iOException) {
                object = stringBuilder;
              } 
            } else {
              return getString(paramInt);
            }  
        } 
      } 
      return object;
    } 
    return getBytes(paramInt);
  }
  
  private final short getShortFromString(String paramString, int paramInt) throws SQLException {
    if (paramString != null)
      try {
        return (paramString.length() == 0) ? (short)convertToZeroWithEmptyCheck() : ((paramString.indexOf("e") == -1 && paramString.indexOf("E") == -1 && paramString.indexOf(".") == -1) ? parseShortWithOverflowCheck(paramInt, null, paramString) : parseShortAsDouble(paramInt, paramString));
      } catch (NumberFormatException numberFormatException) {
        try {
          return parseShortAsDouble(paramInt, paramString);
        } catch (NumberFormatException numberFormatException1) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____217"));
          stringBuilder.append(paramString);
          stringBuilder.append(Messages.getString("ResultSet.___in_column__218"));
          stringBuilder.append(paramInt);
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
        } 
      }  
    return 0;
  }
  
  private String getStringForClob(int paramInt) throws SQLException {
    StringBuilder stringBuilder;
    String str = this.connection.getClobCharacterEncoding();
    if (str == null) {
      if (!this.isBinaryEncoded) {
        String str1 = getString(paramInt);
      } else {
        String str1 = getNativeString(paramInt);
      } 
    } else {
      try {
        byte[] arrayOfByte;
        if (!this.isBinaryEncoded) {
          arrayOfByte = getBytes(paramInt);
        } else {
          arrayOfByte = getNativeBytes(paramInt, true);
        } 
        if (arrayOfByte != null) {
          String str1 = StringUtils.toString(arrayOfByte, str);
        } else {
          arrayOfByte = null;
        } 
        return (String)arrayOfByte;
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported character encoding ");
        stringBuilder.append(str);
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      } 
    } 
    return (String)stringBuilder;
  }
  
  private Time getTimeFromString(String paramString, Calendar paramCalendar, int paramInt, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    Time time;
    Object object = checkClosed().getConnectionMutex();
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    if (paramString == null) {
      try {
        this.wasNullFlag = true;
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return null;
      } catch (RuntimeException runtimeException) {
        SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", getExceptionInterceptor());
        sQLException.initCause(runtimeException);
        throw sQLException;
      } finally {}
    } else {
      Time time1;
      SQLWarning sQLWarning1;
      StringBuilder stringBuilder;
      SQLWarning sQLWarning2;
      String str = paramString.trim();
      int j = str.indexOf(".");
      int k = 0;
      int i = 0;
      paramString = str;
      if (j > -1)
        paramString = str.substring(0, j); 
      if (paramString.equals("0") || paramString.equals("0000-00-00") || paramString.equals("0000-00-00 00:00:00") || paramString.equals("00000000000000")) {
        if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
          this.wasNullFlag = true;
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          return null;
        } 
        if (!"exception".equals(this.connection.getZeroDateTimeBehavior())) {
          time1 = fastTimeCreate((Calendar)runtimeException, 0, 0, 0);
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          return time1;
        } 
        stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Value '");
        stringBuilder.append((String)time1);
        stringBuilder.append("' can not be represented as java.sql.Time");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      } 
      this.wasNullFlag = false;
      Field[] arrayOfField = this.fields;
      int m = paramInt - 1;
      Field field = arrayOfField[m];
      if (field.getMysqlType() == 7) {
        StringBuilder stringBuilder1;
        k = time1.length();
        if (k != 10) {
          if (k != 12 && k != 14) {
            if (k == 19) {
              i = Integer.parseInt(time1.substring(k - 8, k - 6));
              j = Integer.parseInt(time1.substring(k - 5, k - 3));
              k = Integer.parseInt(time1.substring(k - 2, k));
            } else {
              stringBuilder1 = new StringBuilder();
              this();
              stringBuilder1.append(Messages.getString("ResultSet.Timestamp_too_small_to_convert_to_Time_value_in_column__257"));
              stringBuilder1.append(paramInt);
              stringBuilder1.append("(");
              stringBuilder1.append(this.fields[m]);
              stringBuilder1.append(").");
              throw SQLError.createSQLException(stringBuilder1.toString(), "S1009", getExceptionInterceptor());
            } 
          } else {
            j = k - 4;
            i = Integer.parseInt(stringBuilder1.substring(k - 6, j));
            int n = k - 2;
            j = Integer.parseInt(stringBuilder1.substring(j, n));
            k = Integer.parseInt(stringBuilder1.substring(n, k));
          } 
        } else {
          int n = Integer.parseInt(stringBuilder1.substring(6, 8));
          j = Integer.parseInt(stringBuilder1.substring(8, 10));
          k = i;
          i = n;
        } 
        sQLWarning1 = new SQLWarning();
        StringBuilder stringBuilder2 = new StringBuilder();
        this();
        stringBuilder2.append(Messages.getString("ResultSet.Precision_lost_converting_TIMESTAMP_to_Time_with_getTime()_on_column__261"));
        stringBuilder2.append(paramInt);
        stringBuilder2.append("(");
        stringBuilder2.append(this.fields[m]);
        stringBuilder2.append(").");
        this(stringBuilder2.toString());
        sQLWarning2 = this.warningChain;
        if (sQLWarning2 == null) {
          this.warningChain = sQLWarning1;
        } else {
          sQLWarning2.setNextWarning(sQLWarning1);
        } 
      } else {
        SQLWarning sQLWarning;
        if (sQLWarning2.getMysqlType() == 12) {
          i = Integer.parseInt(sQLWarning1.substring(11, 13));
          j = Integer.parseInt(sQLWarning1.substring(14, 16));
          k = Integer.parseInt(sQLWarning1.substring(17, 19));
          sQLWarning1 = new SQLWarning();
          StringBuilder stringBuilder1 = new StringBuilder();
          this();
          stringBuilder1.append(Messages.getString("ResultSet.Precision_lost_converting_DATETIME_to_Time_with_getTime()_on_column__264"));
          stringBuilder1.append(paramInt);
          stringBuilder1.append("(");
          stringBuilder1.append(this.fields[m]);
          stringBuilder1.append(").");
          this(stringBuilder1.toString());
          sQLWarning = this.warningChain;
          if (sQLWarning == null) {
            this.warningChain = sQLWarning1;
          } else {
            sQLWarning.setNextWarning(sQLWarning1);
          } 
        } else {
          Time time2;
          if (sQLWarning.getMysqlType() == 10) {
            time2 = fastTimeCreate((Calendar)stringBuilder, 0, 0, 0);
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
            return time2;
          } 
          if (time2.length() == 5 || time2.length() == 8) {
            i = Integer.parseInt(time2.substring(0, 2));
            j = Integer.parseInt(time2.substring(3, 5));
            if (time2.length() != 5)
              k = Integer.parseInt(time2.substring(6)); 
          } else {
            stringBuilder = new StringBuilder();
            this();
            stringBuilder.append(Messages.getString("ResultSet.Bad_format_for_Time____267"));
            stringBuilder.append((String)time2);
            stringBuilder.append(Messages.getString("ResultSet.___in_column__268"));
            stringBuilder.append(paramInt);
            throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
          } 
        } 
      } 
      Calendar calendar = getCalendarInstanceForSessionOrNew();
      time = TimeUtil.changeTimezone(this.connection, calendar, (Calendar)stringBuilder, fastTimeCreate(calendar, i, j, k), this.connection.getServerTimezoneTZ(), paramTimeZone, paramBoolean);
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      return time;
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw time;
  }
  
  private Time getTimeInternal(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    checkRowPos();
    if (this.isBinaryEncoded)
      return getNativeTime(paramInt, paramCalendar, paramTimeZone, paramBoolean); 
    if (!this.useFastDateParsing)
      return getTimeFromString(getStringInternal(paramInt, false), paramCalendar, paramInt, paramTimeZone, paramBoolean); 
    checkColumnBounds(paramInt);
    if (this.thisRow.isNull(--paramInt)) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    return this.thisRow.getTimeFast(paramInt, paramCalendar, paramTimeZone, paramBoolean, this.connection, this);
  }
  
  private Timestamp getTimestampFromString(int paramInt, Calendar paramCalendar, String paramString, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield wasNullFlag : Z
    //   5: aload_3
    //   6: ifnonnull -> 16
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield wasNullFlag : Z
    //   14: aconst_null
    //   15: areturn
    //   16: aload_3
    //   17: invokevirtual trim : ()Ljava/lang/String;
    //   20: astore #14
    //   22: aload #14
    //   24: invokevirtual length : ()I
    //   27: istore #8
    //   29: aload_0
    //   30: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   33: invokeinterface getUseJDBCCompliantTimezoneShift : ()Z
    //   38: ifeq -> 54
    //   41: aload_0
    //   42: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   45: invokeinterface getUtcCalendar : ()Ljava/util/Calendar;
    //   50: astore_3
    //   51: goto -> 59
    //   54: aload_0
    //   55: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   58: astore_3
    //   59: aload_0
    //   60: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   63: invokeinterface getUseGmtMillisForDatetimes : ()Z
    //   68: istore #13
    //   70: iload #8
    //   72: ifle -> 234
    //   75: aload #14
    //   77: iconst_0
    //   78: invokevirtual charAt : (I)C
    //   81: bipush #48
    //   83: if_icmpne -> 234
    //   86: aload #14
    //   88: ldc_w '0000-00-00'
    //   91: invokevirtual equals : (Ljava/lang/Object;)Z
    //   94: ifne -> 130
    //   97: aload #14
    //   99: ldc_w '0000-00-00 00:00:00'
    //   102: invokevirtual equals : (Ljava/lang/Object;)Z
    //   105: ifne -> 130
    //   108: aload #14
    //   110: ldc_w '00000000000000'
    //   113: invokevirtual equals : (Ljava/lang/Object;)Z
    //   116: ifne -> 130
    //   119: aload #14
    //   121: ldc_w '0'
    //   124: invokevirtual equals : (Ljava/lang/Object;)Z
    //   127: ifeq -> 234
    //   130: ldc_w 'convertToNull'
    //   133: aload_0
    //   134: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   137: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   142: invokevirtual equals : (Ljava/lang/Object;)Z
    //   145: ifeq -> 155
    //   148: aload_0
    //   149: iconst_1
    //   150: putfield wasNullFlag : Z
    //   153: aconst_null
    //   154: areturn
    //   155: ldc_w 'exception'
    //   158: aload_0
    //   159: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   162: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   167: invokevirtual equals : (Ljava/lang/Object;)Z
    //   170: ifne -> 188
    //   173: aload_0
    //   174: aconst_null
    //   175: iconst_1
    //   176: iconst_1
    //   177: iconst_1
    //   178: iconst_0
    //   179: iconst_0
    //   180: iconst_0
    //   181: iconst_0
    //   182: iload #13
    //   184: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   187: areturn
    //   188: new java/lang/StringBuilder
    //   191: astore_2
    //   192: aload_2
    //   193: invokespecial <init> : ()V
    //   196: aload_2
    //   197: ldc_w 'Value ''
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_2
    //   205: aload #14
    //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload_2
    //   212: ldc_w '' can not be represented as java.sql.Timestamp'
    //   215: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: pop
    //   219: aload_2
    //   220: invokevirtual toString : ()Ljava/lang/String;
    //   223: ldc_w 'S1009'
    //   226: aload_0
    //   227: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   230: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   233: athrow
    //   234: aload_0
    //   235: getfield fields : [Lcom/mysql/jdbc/Field;
    //   238: astore #15
    //   240: iload_1
    //   241: iconst_1
    //   242: isub
    //   243: istore #9
    //   245: aload #15
    //   247: iload #9
    //   249: aaload
    //   250: invokevirtual getMysqlType : ()I
    //   253: bipush #13
    //   255: if_icmpne -> 333
    //   258: aload_0
    //   259: getfield useLegacyDatetimeCode : Z
    //   262: ifne -> 287
    //   265: aload #4
    //   267: aload #14
    //   269: iconst_0
    //   270: iconst_4
    //   271: invokevirtual substring : (II)Ljava/lang/String;
    //   274: invokestatic parseInt : (Ljava/lang/String;)I
    //   277: iconst_1
    //   278: iconst_1
    //   279: iconst_0
    //   280: iconst_0
    //   281: iconst_0
    //   282: iconst_0
    //   283: invokestatic fastTimestampCreate : (Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   286: areturn
    //   287: aload_0
    //   288: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   291: aload_3
    //   292: aload_2
    //   293: aload_0
    //   294: aload_3
    //   295: aload #14
    //   297: iconst_0
    //   298: iconst_4
    //   299: invokevirtual substring : (II)Ljava/lang/String;
    //   302: invokestatic parseInt : (Ljava/lang/String;)I
    //   305: iconst_1
    //   306: iconst_1
    //   307: iconst_0
    //   308: iconst_0
    //   309: iconst_0
    //   310: iconst_0
    //   311: iload #13
    //   313: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   316: aload_0
    //   317: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   320: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   325: aload #4
    //   327: iload #5
    //   329: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   332: areturn
    //   333: aload #14
    //   335: ldc_w '.'
    //   338: invokevirtual indexOf : (Ljava/lang/String;)I
    //   341: istore #7
    //   343: iload #7
    //   345: iload #8
    //   347: iconst_1
    //   348: isub
    //   349: if_icmpne -> 361
    //   352: iload #8
    //   354: iconst_1
    //   355: isub
    //   356: istore #6
    //   358: goto -> 450
    //   361: iload #8
    //   363: istore #6
    //   365: iload #7
    //   367: iconst_m1
    //   368: if_icmpeq -> 450
    //   371: iload #7
    //   373: iconst_2
    //   374: iadd
    //   375: iload #8
    //   377: if_icmpgt -> 440
    //   380: iload #7
    //   382: iconst_1
    //   383: iadd
    //   384: istore #10
    //   386: aload #14
    //   388: iload #10
    //   390: invokevirtual substring : (I)Ljava/lang/String;
    //   393: invokestatic parseInt : (Ljava/lang/String;)I
    //   396: istore #6
    //   398: iload #8
    //   400: iload #10
    //   402: isub
    //   403: istore #8
    //   405: iload #8
    //   407: bipush #9
    //   409: if_icmpge -> 433
    //   412: iload #6
    //   414: ldc2_w 10.0
    //   417: bipush #9
    //   419: iload #8
    //   421: isub
    //   422: i2d
    //   423: invokestatic pow : (DD)D
    //   426: d2i
    //   427: imul
    //   428: istore #6
    //   430: goto -> 433
    //   433: iload #6
    //   435: istore #12
    //   437: goto -> 457
    //   440: new java/lang/IllegalArgumentException
    //   443: astore_2
    //   444: aload_2
    //   445: invokespecial <init> : ()V
    //   448: aload_2
    //   449: athrow
    //   450: iconst_0
    //   451: istore #12
    //   453: iload #6
    //   455: istore #7
    //   457: iload #7
    //   459: iconst_2
    //   460: if_icmpeq -> 1296
    //   463: iload #7
    //   465: iconst_4
    //   466: if_icmpeq -> 1245
    //   469: iload #7
    //   471: bipush #6
    //   473: if_icmpeq -> 1167
    //   476: iload #7
    //   478: bipush #8
    //   480: if_icmpeq -> 1052
    //   483: iload #7
    //   485: bipush #10
    //   487: if_icmpeq -> 888
    //   490: iload #7
    //   492: bipush #12
    //   494: if_icmpeq -> 782
    //   497: iload #7
    //   499: bipush #14
    //   501: if_icmpeq -> 698
    //   504: iload #7
    //   506: tableswitch default -> 552, 19 -> 614, 20 -> 614, 21 -> 614, 22 -> 614, 23 -> 614, 24 -> 614, 25 -> 614, 26 -> 614
    //   552: new java/sql/SQLException
    //   555: astore_2
    //   556: new java/lang/StringBuilder
    //   559: astore_3
    //   560: aload_3
    //   561: invokespecial <init> : ()V
    //   564: aload_3
    //   565: ldc_w 'Bad format for Timestamp ''
    //   568: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload_3
    //   573: aload #14
    //   575: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   578: pop
    //   579: aload_3
    //   580: ldc_w '' in column '
    //   583: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   586: pop
    //   587: aload_3
    //   588: iload_1
    //   589: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   592: pop
    //   593: aload_3
    //   594: ldc_w '.'
    //   597: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   600: pop
    //   601: aload_2
    //   602: aload_3
    //   603: invokevirtual toString : ()Ljava/lang/String;
    //   606: ldc_w 'S1009'
    //   609: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   612: aload_2
    //   613: athrow
    //   614: aload #14
    //   616: iconst_0
    //   617: iconst_4
    //   618: invokevirtual substring : (II)Ljava/lang/String;
    //   621: invokestatic parseInt : (Ljava/lang/String;)I
    //   624: istore #6
    //   626: aload #14
    //   628: iconst_5
    //   629: bipush #7
    //   631: invokevirtual substring : (II)Ljava/lang/String;
    //   634: invokestatic parseInt : (Ljava/lang/String;)I
    //   637: istore #7
    //   639: aload #14
    //   641: bipush #8
    //   643: bipush #10
    //   645: invokevirtual substring : (II)Ljava/lang/String;
    //   648: invokestatic parseInt : (Ljava/lang/String;)I
    //   651: istore #8
    //   653: aload #14
    //   655: bipush #11
    //   657: bipush #13
    //   659: invokevirtual substring : (II)Ljava/lang/String;
    //   662: invokestatic parseInt : (Ljava/lang/String;)I
    //   665: istore #9
    //   667: aload #14
    //   669: bipush #14
    //   671: bipush #16
    //   673: invokevirtual substring : (II)Ljava/lang/String;
    //   676: invokestatic parseInt : (Ljava/lang/String;)I
    //   679: istore #10
    //   681: aload #14
    //   683: bipush #17
    //   685: bipush #19
    //   687: invokevirtual substring : (II)Ljava/lang/String;
    //   690: invokestatic parseInt : (Ljava/lang/String;)I
    //   693: istore #11
    //   695: goto -> 1347
    //   698: aload #14
    //   700: iconst_0
    //   701: iconst_4
    //   702: invokevirtual substring : (II)Ljava/lang/String;
    //   705: invokestatic parseInt : (Ljava/lang/String;)I
    //   708: istore #6
    //   710: aload #14
    //   712: iconst_4
    //   713: bipush #6
    //   715: invokevirtual substring : (II)Ljava/lang/String;
    //   718: invokestatic parseInt : (Ljava/lang/String;)I
    //   721: istore #7
    //   723: aload #14
    //   725: bipush #6
    //   727: bipush #8
    //   729: invokevirtual substring : (II)Ljava/lang/String;
    //   732: invokestatic parseInt : (Ljava/lang/String;)I
    //   735: istore #8
    //   737: aload #14
    //   739: bipush #8
    //   741: bipush #10
    //   743: invokevirtual substring : (II)Ljava/lang/String;
    //   746: invokestatic parseInt : (Ljava/lang/String;)I
    //   749: istore #9
    //   751: aload #14
    //   753: bipush #10
    //   755: bipush #12
    //   757: invokevirtual substring : (II)Ljava/lang/String;
    //   760: invokestatic parseInt : (Ljava/lang/String;)I
    //   763: istore #10
    //   765: aload #14
    //   767: bipush #12
    //   769: bipush #14
    //   771: invokevirtual substring : (II)Ljava/lang/String;
    //   774: invokestatic parseInt : (Ljava/lang/String;)I
    //   777: istore #11
    //   779: goto -> 1347
    //   782: aload #14
    //   784: iconst_0
    //   785: iconst_2
    //   786: invokevirtual substring : (II)Ljava/lang/String;
    //   789: invokestatic parseInt : (Ljava/lang/String;)I
    //   792: istore #7
    //   794: iload #7
    //   796: istore #6
    //   798: iload #7
    //   800: bipush #69
    //   802: if_icmpgt -> 812
    //   805: iload #7
    //   807: bipush #100
    //   809: iadd
    //   810: istore #6
    //   812: aload #14
    //   814: iconst_2
    //   815: iconst_4
    //   816: invokevirtual substring : (II)Ljava/lang/String;
    //   819: invokestatic parseInt : (Ljava/lang/String;)I
    //   822: istore #7
    //   824: aload #14
    //   826: iconst_4
    //   827: bipush #6
    //   829: invokevirtual substring : (II)Ljava/lang/String;
    //   832: invokestatic parseInt : (Ljava/lang/String;)I
    //   835: istore #8
    //   837: aload #14
    //   839: bipush #6
    //   841: bipush #8
    //   843: invokevirtual substring : (II)Ljava/lang/String;
    //   846: invokestatic parseInt : (Ljava/lang/String;)I
    //   849: istore #9
    //   851: aload #14
    //   853: bipush #8
    //   855: bipush #10
    //   857: invokevirtual substring : (II)Ljava/lang/String;
    //   860: invokestatic parseInt : (Ljava/lang/String;)I
    //   863: istore #10
    //   865: aload #14
    //   867: bipush #10
    //   869: bipush #12
    //   871: invokevirtual substring : (II)Ljava/lang/String;
    //   874: invokestatic parseInt : (Ljava/lang/String;)I
    //   877: istore #11
    //   879: wide iinc #6 1900
    //   885: goto -> 1347
    //   888: aload_0
    //   889: getfield fields : [Lcom/mysql/jdbc/Field;
    //   892: iload #9
    //   894: aaload
    //   895: invokevirtual getMysqlType : ()I
    //   898: bipush #10
    //   900: if_icmpeq -> 1010
    //   903: aload #14
    //   905: ldc_w '-'
    //   908: invokevirtual indexOf : (Ljava/lang/String;)I
    //   911: iconst_m1
    //   912: if_icmpeq -> 918
    //   915: goto -> 1010
    //   918: aload #14
    //   920: iconst_0
    //   921: iconst_2
    //   922: invokevirtual substring : (II)Ljava/lang/String;
    //   925: invokestatic parseInt : (Ljava/lang/String;)I
    //   928: istore #7
    //   930: iload #7
    //   932: istore #6
    //   934: iload #7
    //   936: bipush #69
    //   938: if_icmpgt -> 948
    //   941: iload #7
    //   943: bipush #100
    //   945: iadd
    //   946: istore #6
    //   948: aload #14
    //   950: iconst_2
    //   951: iconst_4
    //   952: invokevirtual substring : (II)Ljava/lang/String;
    //   955: invokestatic parseInt : (Ljava/lang/String;)I
    //   958: istore #7
    //   960: aload #14
    //   962: iconst_4
    //   963: bipush #6
    //   965: invokevirtual substring : (II)Ljava/lang/String;
    //   968: invokestatic parseInt : (Ljava/lang/String;)I
    //   971: istore #8
    //   973: aload #14
    //   975: bipush #6
    //   977: bipush #8
    //   979: invokevirtual substring : (II)Ljava/lang/String;
    //   982: invokestatic parseInt : (Ljava/lang/String;)I
    //   985: istore #9
    //   987: aload #14
    //   989: bipush #8
    //   991: bipush #10
    //   993: invokevirtual substring : (II)Ljava/lang/String;
    //   996: invokestatic parseInt : (Ljava/lang/String;)I
    //   999: istore #10
    //   1001: wide iinc #6 1900
    //   1007: goto -> 1344
    //   1010: aload #14
    //   1012: iconst_0
    //   1013: iconst_4
    //   1014: invokevirtual substring : (II)Ljava/lang/String;
    //   1017: invokestatic parseInt : (Ljava/lang/String;)I
    //   1020: istore #6
    //   1022: aload #14
    //   1024: iconst_5
    //   1025: bipush #7
    //   1027: invokevirtual substring : (II)Ljava/lang/String;
    //   1030: invokestatic parseInt : (Ljava/lang/String;)I
    //   1033: istore #7
    //   1035: aload #14
    //   1037: bipush #8
    //   1039: bipush #10
    //   1041: invokevirtual substring : (II)Ljava/lang/String;
    //   1044: invokestatic parseInt : (Ljava/lang/String;)I
    //   1047: istore #8
    //   1049: goto -> 1338
    //   1052: aload #14
    //   1054: ldc_w ':'
    //   1057: invokevirtual indexOf : (Ljava/lang/String;)I
    //   1060: iconst_m1
    //   1061: if_icmpeq -> 1116
    //   1064: aload #14
    //   1066: iconst_0
    //   1067: iconst_2
    //   1068: invokevirtual substring : (II)Ljava/lang/String;
    //   1071: invokestatic parseInt : (Ljava/lang/String;)I
    //   1074: istore #9
    //   1076: aload #14
    //   1078: iconst_3
    //   1079: iconst_5
    //   1080: invokevirtual substring : (II)Ljava/lang/String;
    //   1083: invokestatic parseInt : (Ljava/lang/String;)I
    //   1086: istore #10
    //   1088: aload #14
    //   1090: bipush #6
    //   1092: bipush #8
    //   1094: invokevirtual substring : (II)Ljava/lang/String;
    //   1097: invokestatic parseInt : (Ljava/lang/String;)I
    //   1100: istore #11
    //   1102: sipush #1970
    //   1105: istore #6
    //   1107: iconst_1
    //   1108: istore #7
    //   1110: iconst_1
    //   1111: istore #8
    //   1113: goto -> 1347
    //   1116: aload #14
    //   1118: iconst_0
    //   1119: iconst_4
    //   1120: invokevirtual substring : (II)Ljava/lang/String;
    //   1123: invokestatic parseInt : (Ljava/lang/String;)I
    //   1126: istore #8
    //   1128: aload #14
    //   1130: iconst_4
    //   1131: bipush #6
    //   1133: invokevirtual substring : (II)Ljava/lang/String;
    //   1136: invokestatic parseInt : (Ljava/lang/String;)I
    //   1139: istore #7
    //   1141: aload #14
    //   1143: bipush #6
    //   1145: bipush #8
    //   1147: invokevirtual substring : (II)Ljava/lang/String;
    //   1150: invokestatic parseInt : (Ljava/lang/String;)I
    //   1153: istore #6
    //   1155: wide iinc #8 -1900
    //   1161: iinc #7, -1
    //   1164: goto -> 1230
    //   1167: aload #14
    //   1169: iconst_0
    //   1170: iconst_2
    //   1171: invokevirtual substring : (II)Ljava/lang/String;
    //   1174: invokestatic parseInt : (Ljava/lang/String;)I
    //   1177: istore #7
    //   1179: iload #7
    //   1181: istore #6
    //   1183: iload #7
    //   1185: bipush #69
    //   1187: if_icmpgt -> 1197
    //   1190: iload #7
    //   1192: bipush #100
    //   1194: iadd
    //   1195: istore #6
    //   1197: iload #6
    //   1199: sipush #1900
    //   1202: iadd
    //   1203: istore #8
    //   1205: aload #14
    //   1207: iconst_2
    //   1208: iconst_4
    //   1209: invokevirtual substring : (II)Ljava/lang/String;
    //   1212: invokestatic parseInt : (Ljava/lang/String;)I
    //   1215: istore #7
    //   1217: aload #14
    //   1219: iconst_4
    //   1220: bipush #6
    //   1222: invokevirtual substring : (II)Ljava/lang/String;
    //   1225: invokestatic parseInt : (Ljava/lang/String;)I
    //   1228: istore #6
    //   1230: iload #6
    //   1232: istore #9
    //   1234: iload #8
    //   1236: istore #6
    //   1238: iload #9
    //   1240: istore #8
    //   1242: goto -> 1338
    //   1245: aload #14
    //   1247: iconst_0
    //   1248: iconst_2
    //   1249: invokevirtual substring : (II)Ljava/lang/String;
    //   1252: invokestatic parseInt : (Ljava/lang/String;)I
    //   1255: istore #7
    //   1257: iload #7
    //   1259: istore #6
    //   1261: iload #7
    //   1263: bipush #69
    //   1265: if_icmpgt -> 1275
    //   1268: iload #7
    //   1270: bipush #100
    //   1272: iadd
    //   1273: istore #6
    //   1275: aload #14
    //   1277: iconst_2
    //   1278: iconst_4
    //   1279: invokevirtual substring : (II)Ljava/lang/String;
    //   1282: invokestatic parseInt : (Ljava/lang/String;)I
    //   1285: istore #7
    //   1287: wide iinc #6 1900
    //   1293: goto -> 1335
    //   1296: aload #14
    //   1298: iconst_0
    //   1299: iconst_2
    //   1300: invokevirtual substring : (II)Ljava/lang/String;
    //   1303: invokestatic parseInt : (Ljava/lang/String;)I
    //   1306: istore #7
    //   1308: iload #7
    //   1310: istore #6
    //   1312: iload #7
    //   1314: bipush #69
    //   1316: if_icmpgt -> 1326
    //   1319: iload #7
    //   1321: bipush #100
    //   1323: iadd
    //   1324: istore #6
    //   1326: wide iinc #6 1900
    //   1332: iconst_1
    //   1333: istore #7
    //   1335: iconst_1
    //   1336: istore #8
    //   1338: iconst_0
    //   1339: istore #9
    //   1341: iconst_0
    //   1342: istore #10
    //   1344: iconst_0
    //   1345: istore #11
    //   1347: aload_0
    //   1348: getfield useLegacyDatetimeCode : Z
    //   1351: ifne -> 1374
    //   1354: aload #4
    //   1356: iload #6
    //   1358: iload #7
    //   1360: iload #8
    //   1362: iload #9
    //   1364: iload #10
    //   1366: iload #11
    //   1368: iload #12
    //   1370: invokestatic fastTimestampCreate : (Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   1373: areturn
    //   1374: aload_0
    //   1375: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1378: aload_3
    //   1379: aload_2
    //   1380: aload_0
    //   1381: aload_3
    //   1382: iload #6
    //   1384: iload #7
    //   1386: iload #8
    //   1388: iload #9
    //   1390: iload #10
    //   1392: iload #11
    //   1394: iload #12
    //   1396: iload #13
    //   1398: invokevirtual fastTimestampCreate : (Ljava/util/Calendar;IIIIIIIZ)Ljava/sql/Timestamp;
    //   1401: aload_0
    //   1402: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1405: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   1410: aload #4
    //   1412: iload #5
    //   1414: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   1417: astore_2
    //   1418: aload_2
    //   1419: areturn
    //   1420: astore_2
    //   1421: aload #14
    //   1423: astore_3
    //   1424: goto -> 1428
    //   1427: astore_2
    //   1428: new java/lang/StringBuilder
    //   1431: dup
    //   1432: invokespecial <init> : ()V
    //   1435: astore #4
    //   1437: aload #4
    //   1439: ldc_w 'Cannot convert value ''
    //   1442: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1445: pop
    //   1446: aload #4
    //   1448: aload_3
    //   1449: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1452: pop
    //   1453: aload #4
    //   1455: ldc_w '' from column '
    //   1458: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1461: pop
    //   1462: aload #4
    //   1464: iload_1
    //   1465: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1468: pop
    //   1469: aload #4
    //   1471: ldc_w ' to TIMESTAMP.'
    //   1474: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1477: pop
    //   1478: aload #4
    //   1480: invokevirtual toString : ()Ljava/lang/String;
    //   1483: ldc_w 'S1009'
    //   1486: aload_0
    //   1487: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1490: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1493: astore_3
    //   1494: aload_3
    //   1495: aload_2
    //   1496: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1499: pop
    //   1500: aload_3
    //   1501: athrow
    // Exception table:
    //   from	to	target	type
    //   0	5	1427	java/lang/RuntimeException
    //   9	14	1427	java/lang/RuntimeException
    //   16	22	1427	java/lang/RuntimeException
    //   22	51	1420	java/lang/RuntimeException
    //   54	59	1420	java/lang/RuntimeException
    //   59	70	1420	java/lang/RuntimeException
    //   75	130	1420	java/lang/RuntimeException
    //   130	153	1420	java/lang/RuntimeException
    //   155	188	1420	java/lang/RuntimeException
    //   188	234	1420	java/lang/RuntimeException
    //   234	240	1420	java/lang/RuntimeException
    //   245	287	1420	java/lang/RuntimeException
    //   287	333	1420	java/lang/RuntimeException
    //   333	343	1420	java/lang/RuntimeException
    //   386	398	1420	java/lang/RuntimeException
    //   412	430	1420	java/lang/RuntimeException
    //   440	450	1420	java/lang/RuntimeException
    //   552	614	1420	java/lang/RuntimeException
    //   614	695	1420	java/lang/RuntimeException
    //   698	779	1420	java/lang/RuntimeException
    //   782	794	1420	java/lang/RuntimeException
    //   812	879	1420	java/lang/RuntimeException
    //   888	915	1420	java/lang/RuntimeException
    //   918	930	1420	java/lang/RuntimeException
    //   948	1001	1420	java/lang/RuntimeException
    //   1010	1049	1420	java/lang/RuntimeException
    //   1052	1102	1420	java/lang/RuntimeException
    //   1116	1155	1420	java/lang/RuntimeException
    //   1167	1179	1420	java/lang/RuntimeException
    //   1205	1230	1420	java/lang/RuntimeException
    //   1245	1257	1420	java/lang/RuntimeException
    //   1275	1287	1420	java/lang/RuntimeException
    //   1296	1308	1420	java/lang/RuntimeException
    //   1347	1374	1420	java/lang/RuntimeException
    //   1374	1418	1420	java/lang/RuntimeException
  }
  
  private Timestamp getTimestampInternal(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    Timestamp timestamp;
    if (this.isBinaryEncoded)
      return getNativeTimestamp(paramInt, paramCalendar, paramTimeZone, paramBoolean); 
    if (!this.useFastDateParsing) {
      timestamp = getTimestampFromString(paramInt, paramCalendar, getStringInternal(paramInt, false), paramTimeZone, paramBoolean);
    } else {
      checkClosed();
      checkRowPos();
      checkColumnBounds(paramInt);
      timestamp = this.thisRow.getTimestampFast(paramInt - 1, (Calendar)timestamp, paramTimeZone, paramBoolean, this.connection, this, this.connection.getUseGmtMillisForDatetimes(), this.connection.getUseJDBCCompliantTimezoneShift());
    } 
    if (timestamp == null) {
      this.wasNullFlag = true;
    } else {
      this.wasNullFlag = false;
    } 
    return timestamp;
  }
  
  private void issueConversionViaParsingWarning(String paramString, int paramInt, Object paramObject, Field paramField, int[] paramArrayOfint) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      StringBuilder stringBuilder2 = new StringBuilder();
      this();
      StatementImpl statementImpl2 = this.owningStatement;
      if (statementImpl2 != null && statementImpl2 instanceof PreparedStatement) {
        stringBuilder2.append(Messages.getString("ResultSet.CostlyConversionCreatedFromQuery"));
        stringBuilder2.append(((PreparedStatement)this.owningStatement).originalSql);
        stringBuilder2.append("\n\n");
      } else {
        stringBuilder2.append(".");
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      boolean bool = false;
      int i;
      for (i = 0; i < paramArrayOfint.length; i++) {
        stringBuilder1.append(MysqlDefs.typeToName(paramArrayOfint[i]));
        stringBuilder1.append("\n");
      } 
      String str2 = paramField.getOriginalName();
      String str3 = paramField.getOriginalTableName();
      String str4 = stringBuilder2.toString();
      if (paramObject != null) {
        paramObject = paramObject.getClass().getName();
      } else {
        i = paramField.getSQLType();
        boolean bool1 = paramField.isUnsigned();
        int j = paramField.getMysqlType();
        if (paramField.isBinary() || paramField.isBlob())
          bool = true; 
        paramObject = ResultSetMetaData.getClassNameForJavaType(i, bool1, j, bool, paramField.isOpaqueBinary(), this.connection.getYearIsDateType());
      } 
      String str1 = Messages.getString("ResultSet.CostlyConversion", new Object[] { paramString, Integer.valueOf(paramInt + 1), str2, str3, str4, paramObject, MysqlDefs.typeToName(paramField.getMysqlType()), stringBuilder1.toString() });
      ProfilerEventHandler profilerEventHandler = this.eventSink;
      paramObject = new ProfilerEvent();
      StatementImpl statementImpl1 = this.owningStatement;
      if (statementImpl1 == null) {
        paramString = "N/A";
      } else {
        paramString = statementImpl1.currentCatalog;
      } 
      long l = this.connectionId;
      if (statementImpl1 == null) {
        paramInt = -1;
      } else {
        paramInt = statementImpl1.getId();
      } 
      super((byte)0, "", paramString, l, paramInt, this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, str1);
      profilerEventHandler.consumeEvent((ProfilerEvent)paramObject);
      return;
    } 
  }
  
  private int parseIntAsDouble(int paramInt, String paramString) throws NumberFormatException, SQLException {
    if (paramString == null)
      return 0; 
    double d = Double.parseDouble(paramString);
    if (this.jdbcCompliantTruncationForReads && (d < -2.147483648E9D || d > 2.147483647E9D))
      throwRangeException(String.valueOf(d), paramInt, 4); 
    return (int)d;
  }
  
  private long parseLongAsDouble(int paramInt, String paramString) throws NumberFormatException, SQLException {
    if (paramString == null)
      return 0L; 
    double d = Double.parseDouble(paramString);
    if (this.jdbcCompliantTruncationForReads && (d < -9.223372036854776E18D || d > 9.223372036854776E18D))
      throwRangeException(paramString, paramInt + 1, -5); 
    return (long)d;
  }
  
  private long parseLongWithOverflowCheck(int paramInt, byte[] paramArrayOfbyte, String paramString, boolean paramBoolean) throws NumberFormatException, SQLException {
    long l;
    if (paramArrayOfbyte == null && paramString == null)
      return 0L; 
    if (paramArrayOfbyte != null) {
      l = StringUtils.getLong(paramArrayOfbyte);
    } else {
      l = Long.parseLong(paramString.trim());
    } 
    if (paramBoolean && this.jdbcCompliantTruncationForReads)
      checkForLongTruncation(paramInt, paramArrayOfbyte, l); 
    return l;
  }
  
  private short parseShortAsDouble(int paramInt, String paramString) throws NumberFormatException, SQLException {
    if (paramString == null)
      return 0; 
    double d = Double.parseDouble(paramString);
    if (this.jdbcCompliantTruncationForReads && (d < -32768.0D || d > 32767.0D))
      throwRangeException(String.valueOf(d), paramInt, 5); 
    return (short)(int)d;
  }
  
  private short parseShortWithOverflowCheck(int paramInt, byte[] paramArrayOfbyte, String paramString) throws NumberFormatException, SQLException {
    short s;
    if (paramArrayOfbyte == null && paramString == null)
      return 0; 
    if (paramArrayOfbyte != null) {
      s = StringUtils.getShort(paramArrayOfbyte);
    } else {
      paramString = paramString.trim();
      s = Short.parseShort(paramString);
    } 
    if (this.jdbcCompliantTruncationForReads && (s == Short.MIN_VALUE || s == Short.MAX_VALUE)) {
      String str;
      if (paramString == null) {
        str = StringUtils.toString(paramArrayOfbyte);
      } else {
        str = paramString;
      } 
      long l = Long.parseLong(str);
      if (l < -32768L || l > 32767L) {
        str = paramString;
        if (paramString == null)
          str = StringUtils.toString(paramArrayOfbyte); 
        throwRangeException(str, paramInt, 5);
      } 
    } 
    return s;
  }
  
  private void setRowPositionValidity() throws SQLException {
    if (!this.rowData.isDynamic() && this.rowData.size() == 0) {
      this.invalidRowReason = Messages.getString("ResultSet.Illegal_operation_on_empty_result_set");
      this.onValidRow = false;
    } else if (this.rowData.isBeforeFirst()) {
      this.invalidRowReason = Messages.getString("ResultSet.Before_start_of_result_set_146");
      this.onValidRow = false;
    } else if (this.rowData.isAfterLast()) {
      this.invalidRowReason = Messages.getString("ResultSet.After_end_of_result_set_148");
      this.onValidRow = false;
    } else {
      this.onValidRow = true;
      this.invalidRowReason = null;
    } 
  }
  
  private void throwRangeException(String paramString, int paramInt1, int paramInt2) throws SQLException {
    String str;
    if (paramInt2 != -6) {
      if (paramInt2 != -5) {
        StringBuilder stringBuilder1;
        StringBuilder stringBuilder2;
        switch (paramInt2) {
          default:
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append(" (JDBC type '");
            stringBuilder1.append(paramInt2);
            stringBuilder1.append("')");
            str = stringBuilder1.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("'");
            stringBuilder2.append(paramString);
            stringBuilder2.append("' in column '");
            stringBuilder2.append(paramInt1);
            stringBuilder2.append("' is outside valid range for the datatype ");
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            throw SQLError.createSQLException(stringBuilder2.toString(), "22003", getExceptionInterceptor());
          case 8:
            str = "DOUBLE";
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("'");
            stringBuilder2.append(paramString);
            stringBuilder2.append("' in column '");
            stringBuilder2.append(paramInt1);
            stringBuilder2.append("' is outside valid range for the datatype ");
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            throw SQLError.createSQLException(stringBuilder2.toString(), "22003", getExceptionInterceptor());
          case 7:
            str = "REAL";
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("'");
            stringBuilder2.append(paramString);
            stringBuilder2.append("' in column '");
            stringBuilder2.append(paramInt1);
            stringBuilder2.append("' is outside valid range for the datatype ");
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            throw SQLError.createSQLException(stringBuilder2.toString(), "22003", getExceptionInterceptor());
          case 6:
            str = "FLOAT";
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("'");
            stringBuilder2.append(paramString);
            stringBuilder2.append("' in column '");
            stringBuilder2.append(paramInt1);
            stringBuilder2.append("' is outside valid range for the datatype ");
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            throw SQLError.createSQLException(stringBuilder2.toString(), "22003", getExceptionInterceptor());
          case 5:
            str = "SMALLINT";
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("'");
            stringBuilder2.append(paramString);
            stringBuilder2.append("' in column '");
            stringBuilder2.append(paramInt1);
            stringBuilder2.append("' is outside valid range for the datatype ");
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            throw SQLError.createSQLException(stringBuilder2.toString(), "22003", getExceptionInterceptor());
          case 4:
            str = "INTEGER";
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("'");
            stringBuilder2.append(paramString);
            stringBuilder2.append("' in column '");
            stringBuilder2.append(paramInt1);
            stringBuilder2.append("' is outside valid range for the datatype ");
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            throw SQLError.createSQLException(stringBuilder2.toString(), "22003", getExceptionInterceptor());
          case 3:
            break;
        } 
        str = "DECIMAL";
      } else {
        str = "BIGINT";
      } 
    } else {
      str = "TINYINT";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("'");
    stringBuilder.append(paramString);
    stringBuilder.append("' in column '");
    stringBuilder.append(paramInt1);
    stringBuilder.append("' is outside valid range for the datatype ");
    stringBuilder.append(str);
    stringBuilder.append(".");
    throw SQLError.createSQLException(stringBuilder.toString(), "22003", getExceptionInterceptor());
  }
  
  public boolean absolute(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      int i = this.rowData.size();
      boolean bool = true;
      if (i != 0) {
        if (this.onInsertRow)
          this.onInsertRow = false; 
        if (this.doingUpdates)
          this.doingUpdates = false; 
        ResultSetRow resultSetRow = this.thisRow;
        if (resultSetRow != null)
          resultSetRow.closeOpenStreams(); 
        if (paramInt == 0) {
          beforeFirst();
        } else {
          if (paramInt == 1) {
            bool = first();
          } else if (paramInt == -1) {
            bool = last();
          } else {
            if (paramInt > this.rowData.size()) {
              afterLast();
            } else if (paramInt < 0) {
              paramInt = this.rowData.size() + paramInt + 1;
              if (paramInt <= 0) {
                beforeFirst();
              } else {
                bool = absolute(paramInt);
                setRowPositionValidity();
                return bool;
              } 
            } else {
              this.rowData.setCurrentRow(--paramInt);
              this.thisRow = this.rowData.getAt(paramInt);
              setRowPositionValidity();
              return bool;
            } 
            bool = false;
          } 
          setRowPositionValidity();
          return bool;
        } 
      } 
      bool = false;
    } 
  }
  
  public void afterLast() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      ResultSetRow resultSetRow = this.thisRow;
      if (resultSetRow != null)
        resultSetRow.closeOpenStreams(); 
      if (this.rowData.size() != 0) {
        this.rowData.afterLast();
        this.thisRow = null;
      } 
      setRowPositionValidity();
      return;
    } 
  }
  
  public void beforeFirst() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      if (this.rowData.size() == 0)
        return; 
      ResultSetRow resultSetRow = this.thisRow;
      if (resultSetRow != null)
        resultSetRow.closeOpenStreams(); 
      this.rowData.beforeFirst();
      this.thisRow = null;
      setRowPositionValidity();
      return;
    } 
  }
  
  public void buildIndexMapping() throws SQLException {
    int i = this.fields.length;
    Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
    this.columnLabelToIndex = new TreeMap<String, Integer>(comparator);
    this.fullColumnNameToIndex = new TreeMap<String, Integer>(comparator);
    this.columnNameToIndex = new TreeMap<String, Integer>(comparator);
    while (--i >= 0) {
      Integer integer = Integer.valueOf(i);
      String str2 = this.fields[i].getOriginalName();
      String str1 = this.fields[i].getName();
      String str3 = this.fields[i].getFullName();
      if (str1 != null)
        this.columnLabelToIndex.put(str1, integer); 
      if (str3 != null)
        this.fullColumnNameToIndex.put(str3, integer); 
      if (str2 != null)
        this.columnNameToIndex.put(str2, integer); 
      i--;
    } 
    this.hasBuiltIndexMapping = true;
  }
  
  public void cancelRowUpdates() throws SQLException {
    throw new NotUpdatable();
  }
  
  public final MySQLConnection checkClosed() throws SQLException {
    MySQLConnection mySQLConnection = this.connection;
    if (mySQLConnection != null)
      return mySQLConnection; 
    throw SQLError.createSQLException(Messages.getString("ResultSet.Operation_not_allowed_after_ResultSet_closed_144"), "S1000", getExceptionInterceptor());
  }
  
  public final void checkColumnBounds(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: iconst_1
    //   14: if_icmplt -> 95
    //   17: iload_1
    //   18: aload_0
    //   19: getfield fields : [Lcom/mysql/jdbc/Field;
    //   22: arraylength
    //   23: if_icmpgt -> 52
    //   26: aload_0
    //   27: getfield profileSql : Z
    //   30: ifne -> 40
    //   33: aload_0
    //   34: getfield useUsageAdvisor : Z
    //   37: ifeq -> 49
    //   40: aload_0
    //   41: getfield columnUsed : [Z
    //   44: iload_1
    //   45: iconst_1
    //   46: isub
    //   47: iconst_1
    //   48: bastore
    //   49: aload_3
    //   50: monitorexit
    //   51: return
    //   52: ldc_w 'ResultSet.Column_Index_out_of_range_high'
    //   55: iconst_2
    //   56: anewarray java/lang/Object
    //   59: dup
    //   60: iconst_0
    //   61: iload_1
    //   62: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   65: aastore
    //   66: dup
    //   67: iconst_1
    //   68: aload_0
    //   69: getfield fields : [Lcom/mysql/jdbc/Field;
    //   72: arraylength
    //   73: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   76: aastore
    //   77: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   80: ldc_w 'S1009'
    //   83: aload_0
    //   84: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   87: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   90: athrow
    //   91: astore_2
    //   92: goto -> 134
    //   95: ldc_w 'ResultSet.Column_Index_out_of_range_low'
    //   98: iconst_2
    //   99: anewarray java/lang/Object
    //   102: dup
    //   103: iconst_0
    //   104: iload_1
    //   105: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   108: aastore
    //   109: dup
    //   110: iconst_1
    //   111: aload_0
    //   112: getfield fields : [Lcom/mysql/jdbc/Field;
    //   115: arraylength
    //   116: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   119: aastore
    //   120: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   123: ldc_w 'S1009'
    //   126: aload_0
    //   127: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   130: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   133: athrow
    //   134: aload_3
    //   135: monitorexit
    //   136: aload_2
    //   137: athrow
    // Exception table:
    //   from	to	target	type
    //   17	40	91	finally
    //   40	49	91	finally
    //   49	51	91	finally
    //   52	91	91	finally
    //   95	134	91	finally
    //   134	136	91	finally
  }
  
  public void checkRowPos() throws SQLException {
    checkClosed();
    if (this.onValidRow)
      return; 
    throw SQLError.createSQLException(this.invalidRowReason, "S1000", getExceptionInterceptor());
  }
  
  public void clearNextResult() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield nextResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void clearWarnings() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.warningChain = null;
      return;
    } 
  }
  
  public void close() throws SQLException {
    realClose(true);
  }
  
  public ResultSetInternalMethods copy() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetImpl resultSetImpl = getInstance(this.catalog, this.fields, this.rowData, this.connection, this.owningStatement, false);
      if (this.isBinaryEncoded)
        resultSetImpl.setBinaryEncoded(); 
      return resultSetImpl;
    } 
  }
  
  public void deleteRow() throws SQLException {
    throw new NotUpdatable();
  }
  
  public Date fastDateCreate(Calendar paramCalendar, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #7
    //   11: aload #7
    //   13: monitorenter
    //   14: aload_1
    //   15: ifnonnull -> 48
    //   18: aload_0
    //   19: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   22: invokeinterface getNoTimezoneConversionForDateType : ()Z
    //   27: ifeq -> 39
    //   30: aload_0
    //   31: invokespecial getFastClientCalendar : ()Ljava/util/Calendar;
    //   34: astore #6
    //   36: goto -> 45
    //   39: aload_0
    //   40: invokespecial getFastDefaultCalendar : ()Ljava/util/Calendar;
    //   43: astore #6
    //   45: goto -> 51
    //   48: aload_1
    //   49: astore #6
    //   51: aload_0
    //   52: getfield useLegacyDatetimeCode : Z
    //   55: ifne -> 73
    //   58: iload_2
    //   59: iload_3
    //   60: iload #4
    //   62: aload #6
    //   64: invokestatic fastDateCreate : (IIILjava/util/Calendar;)Ljava/sql/Date;
    //   67: astore_1
    //   68: aload #7
    //   70: monitorexit
    //   71: aload_1
    //   72: areturn
    //   73: aload_1
    //   74: ifnonnull -> 107
    //   77: aload_0
    //   78: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   81: invokeinterface getNoTimezoneConversionForDateType : ()Z
    //   86: ifne -> 107
    //   89: aload_0
    //   90: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   93: invokeinterface getUseGmtMillisForDatetimes : ()Z
    //   98: ifeq -> 107
    //   101: iconst_1
    //   102: istore #5
    //   104: goto -> 110
    //   107: iconst_0
    //   108: istore #5
    //   110: iload #5
    //   112: ifeq -> 123
    //   115: aload_0
    //   116: invokevirtual getGmtCalendar : ()Ljava/util/Calendar;
    //   119: astore_1
    //   120: goto -> 126
    //   123: aload #6
    //   125: astore_1
    //   126: iload #5
    //   128: aload_1
    //   129: aload #6
    //   131: iload_2
    //   132: iload_3
    //   133: iload #4
    //   135: invokestatic fastDateCreate : (ZLjava/util/Calendar;Ljava/util/Calendar;III)Ljava/sql/Date;
    //   138: astore_1
    //   139: aload #7
    //   141: monitorexit
    //   142: aload_1
    //   143: areturn
    //   144: astore_1
    //   145: aload #7
    //   147: monitorexit
    //   148: aload_1
    //   149: athrow
    // Exception table:
    //   from	to	target	type
    //   18	36	144	finally
    //   39	45	144	finally
    //   51	71	144	finally
    //   77	101	144	finally
    //   115	120	144	finally
    //   126	142	144	finally
    //   145	148	144	finally
  }
  
  public Time fastTimeCreate(Calendar paramCalendar, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Calendar calendar;
      if (!this.useLegacyDatetimeCode) {
        time1 = TimeUtil.fastTimeCreate(paramInt1, paramInt2, paramInt3, paramCalendar, getExceptionInterceptor());
        return time1;
      } 
      Time time2 = time1;
      if (time1 == null)
        calendar = getFastDefaultCalendar(); 
      Time time1 = TimeUtil.fastTimeCreate(calendar, paramInt1, paramInt2, paramInt3, getExceptionInterceptor());
      return time1;
    } 
  }
  
  public Timestamp fastTimestampCreate(Calendar paramCalendar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Timestamp timestamp;
      Calendar calendar1;
      Calendar calendar2;
      if (!this.useLegacyDatetimeCode) {
        timestamp = TimeUtil.fastTimestampCreate(paramCalendar.getTimeZone(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
        return timestamp;
      } 
      if (timestamp == null)
        calendar1 = getFastDefaultCalendar(); 
      if (paramBoolean) {
        calendar2 = getGmtCalendar();
      } else {
        calendar2 = null;
      } 
      return TimeUtil.fastTimestampCreate(paramBoolean, calendar2, calendar1, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
    } 
  }
  
  public int findColumn(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.hasBuiltIndexMapping)
        buildIndexMapping(); 
      Integer integer1 = this.columnToIndexCache.get(paramString);
      if (integer1 != null) {
        int i = integer1.intValue();
        return i + 1;
      } 
      Integer integer2 = this.columnLabelToIndex.get(paramString);
      integer1 = integer2;
      if (integer2 == null) {
        integer1 = integer2;
        if (this.useColumnNamesInFindColumn)
          integer1 = this.columnNameToIndex.get(paramString); 
      } 
      integer2 = integer1;
      if (integer1 == null)
        integer2 = this.fullColumnNameToIndex.get(paramString); 
      if (integer2 != null) {
        this.columnToIndexCache.put(paramString, integer2);
        int i = integer2.intValue();
        return i + 1;
      } 
      byte b = 0;
      while (true) {
        Field[] arrayOfField = this.fields;
        if (b < arrayOfField.length) {
          if (arrayOfField[b].getName().equalsIgnoreCase(paramString))
            return b + 1; 
          if (this.fields[b].getFullName().equalsIgnoreCase(paramString))
            return b + 1; 
          b++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(Messages.getString("ResultSet.Column____112"));
        stringBuilder.append(paramString);
        stringBuilder.append(Messages.getString("ResultSet.___not_found._113"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S0022", getExceptionInterceptor());
      } 
    } 
  }
  
  public boolean first() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_1
    //   13: istore_1
    //   14: aload_0
    //   15: getfield rowData : Lcom/mysql/jdbc/RowData;
    //   18: invokeinterface isEmpty : ()Z
    //   23: ifeq -> 31
    //   26: iconst_0
    //   27: istore_1
    //   28: goto -> 77
    //   31: aload_0
    //   32: getfield onInsertRow : Z
    //   35: ifeq -> 43
    //   38: aload_0
    //   39: iconst_0
    //   40: putfield onInsertRow : Z
    //   43: aload_0
    //   44: getfield doingUpdates : Z
    //   47: ifeq -> 55
    //   50: aload_0
    //   51: iconst_0
    //   52: putfield doingUpdates : Z
    //   55: aload_0
    //   56: getfield rowData : Lcom/mysql/jdbc/RowData;
    //   59: invokeinterface beforeFirst : ()V
    //   64: aload_0
    //   65: aload_0
    //   66: getfield rowData : Lcom/mysql/jdbc/RowData;
    //   69: invokeinterface next : ()Lcom/mysql/jdbc/ResultSetRow;
    //   74: putfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   77: aload_0
    //   78: invokespecial setRowPositionValidity : ()V
    //   81: aload_2
    //   82: monitorexit
    //   83: iload_1
    //   84: ireturn
    //   85: astore_3
    //   86: aload_2
    //   87: monitorexit
    //   88: aload_3
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   14	26	85	finally
    //   31	43	85	finally
    //   43	55	85	finally
    //   55	77	85	finally
    //   77	83	85	finally
    //   86	88	85	finally
  }
  
  public Array getArray(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public Array getArray(String paramString) throws SQLException {
    return getArray(findColumn(paramString));
  }
  
  public InputStream getAsciiStream(int paramInt) throws SQLException {
    checkRowPos();
    return !this.isBinaryEncoded ? getBinaryStream(paramInt) : getNativeBinaryStream(paramInt);
  }
  
  public InputStream getAsciiStream(String paramString) throws SQLException {
    return getAsciiStream(findColumn(paramString));
  }
  
  public BigDecimal getBigDecimal(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      String str = getString(paramInt);
      if (str != null) {
        if (str.length() == 0)
          return new BigDecimal(convertToZeroLiteralStringWithEmptyCheck()); 
        try {
          return new BigDecimal(str);
        } catch (NumberFormatException numberFormatException) {
          throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { str, Integer.valueOf(paramInt) }), "S1009", getExceptionInterceptor());
        } 
      } 
      return null;
    } 
    return getNativeBigDecimal(paramInt);
  }
  
  @Deprecated
  public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
    if (!this.isBinaryEncoded) {
      String str = getString(paramInt1);
      if (str != null) {
        BigDecimal bigDecimal;
        if (str.length() == 0) {
          bigDecimal = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
          try {
            return bigDecimal.setScale(paramInt2);
          } catch (ArithmeticException arithmeticException) {
            try {
              return bigDecimal.setScale(paramInt2, 4);
            } catch (ArithmeticException arithmeticException1) {
              throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { str, Integer.valueOf(paramInt1) }), "S1009", getExceptionInterceptor());
            } 
          } 
        } 
        try {
          bigDecimal = new BigDecimal();
          this(str);
        } catch (NumberFormatException numberFormatException) {
          if (this.fields[paramInt1 - 1].getMysqlType() == 16) {
            bigDecimal = new BigDecimal(getNumericRepresentationOfSQLBitType(paramInt1));
          } else {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { Integer.valueOf(paramInt1), str }), "S1009", getExceptionInterceptor());
          } 
        } 
        try {
          return bigDecimal.setScale(paramInt2);
        } catch (ArithmeticException arithmeticException) {
          try {
            return bigDecimal.setScale(paramInt2, 4);
          } catch (ArithmeticException arithmeticException1) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { Integer.valueOf(paramInt1), str }), "S1009", getExceptionInterceptor());
          } 
        } 
      } 
      return null;
    } 
    return getNativeBigDecimal(paramInt1, paramInt2);
  }
  
  public BigDecimal getBigDecimal(String paramString) throws SQLException {
    return getBigDecimal(findColumn(paramString));
  }
  
  @Deprecated
  public BigDecimal getBigDecimal(String paramString, int paramInt) throws SQLException {
    return getBigDecimal(findColumn(paramString), paramInt);
  }
  
  public InputStream getBinaryStream(int paramInt) throws SQLException {
    checkRowPos();
    if (!this.isBinaryEncoded) {
      checkColumnBounds(paramInt);
      if (this.thisRow.isNull(--paramInt)) {
        this.wasNullFlag = true;
        return null;
      } 
      this.wasNullFlag = false;
      return this.thisRow.getBinaryInputStream(paramInt);
    } 
    return getNativeBinaryStream(paramInt);
  }
  
  public InputStream getBinaryStream(String paramString) throws SQLException {
    return getBinaryStream(findColumn(paramString));
  }
  
  public Blob getBlob(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      checkRowPos();
      checkColumnBounds(paramInt);
      int i = paramInt - 1;
      if (this.thisRow.isNull(i)) {
        this.wasNullFlag = true;
      } else {
        this.wasNullFlag = false;
      } 
      return (Blob)(this.wasNullFlag ? null : (!this.connection.getEmulateLocators() ? new Blob(this.thisRow.getColumnValue(i), getExceptionInterceptor()) : new BlobFromLocator(this, paramInt, getExceptionInterceptor())));
    } 
    return getNativeBlob(paramInt);
  }
  
  public Blob getBlob(String paramString) throws SQLException {
    return getBlob(findColumn(paramString));
  }
  
  public boolean getBoolean(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    int i = paramInt - 1;
    Field field = this.fields[i];
    if (field.getMysqlType() == 16)
      return byteArrayToBoolean(i); 
    boolean bool1 = false;
    boolean bool2 = false;
    this.wasNullFlag = false;
    int j = field.getSQLType();
    if (j != -7 && j != -6 && j != -5)
      if (j != 16) {
        switch (j) {
          default:
            if (this.connection.getPedantic())
              if (j != -4 && j != -3 && j != -2 && j != 70 && j != 2000) {
                switch (j) {
                  default:
                    switch (j) {
                      default:
                        break;
                      case 2002:
                      case 2003:
                      case 2004:
                      case 2005:
                      case 2006:
                        break;
                    } 
                  case 91:
                  case 92:
                  case 93:
                    throw SQLError.createSQLException("Required type conversion not allowed", "22018", getExceptionInterceptor());
                } 
              } else {
              
              }  
            if (j == -2 || j == -3 || j == -4 || j == 2004)
              return byteArrayToBoolean(i); 
            if (this.useUsageAdvisor)
              issueConversionViaParsingWarning("getBoolean()", paramInt, this.thisRow.getColumnValue(i), this.fields[paramInt], new int[] { 16, 5, 1, 2, 3, 8, 4 }); 
            return getBooleanFromString(getString(paramInt));
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
          case 7:
          case 8:
            break;
        } 
      } else {
        if (field.getMysqlType() == -1)
          return getBooleanFromString(getString(paramInt)); 
        long l1 = getLong(paramInt, false);
        if (l1 != -1L) {
          bool1 = bool2;
          if (l1 > 0L)
            bool1 = true; 
          return bool1;
        } 
        bool1 = true;
      }  
    long l = getLong(paramInt, false);
    if (l == -1L || l > 0L)
      bool1 = true; 
    return bool1;
  }
  
  public boolean getBoolean(String paramString) throws SQLException {
    return getBoolean(findColumn(paramString));
  }
  
  public byte getByte(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      String str = getString(paramInt);
      return (this.wasNullFlag || str == null) ? 0 : getByteFromString(str, paramInt);
    } 
    return getNativeByte(paramInt);
  }
  
  public byte getByte(String paramString) throws SQLException {
    return getByte(findColumn(paramString));
  }
  
  public byte[] getBytes(int paramInt) throws SQLException {
    return getBytes(paramInt, false);
  }
  
  public byte[] getBytes(int paramInt, boolean paramBoolean) throws SQLException {
    if (!this.isBinaryEncoded) {
      checkRowPos();
      checkColumnBounds(paramInt);
      if (this.thisRow.isNull(--paramInt)) {
        this.wasNullFlag = true;
      } else {
        this.wasNullFlag = false;
      } 
      return this.wasNullFlag ? null : this.thisRow.getColumnValue(paramInt);
    } 
    return getNativeBytes(paramInt, paramBoolean);
  }
  
  public byte[] getBytes(String paramString) throws SQLException {
    return getBytes(findColumn(paramString));
  }
  
  public int getBytesSize() throws SQLException {
    RowData rowData = this.rowData;
    checkClosed();
    if (rowData instanceof RowDataStatic) {
      int j = rowData.size();
      byte b = 0;
      int i = 0;
      while (b < j) {
        i += rowData.getAt(b).getBytesSize();
        b++;
      } 
      return i;
    } 
    return -1;
  }
  
  public Calendar getCalendarInstanceForSessionOrNew() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.connection != null)
        return this.connection.getCalendarInstanceForSessionOrNew(); 
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      this();
      return gregorianCalendar;
    } 
  }
  
  public Reader getCharacterStream(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      checkColumnBounds(paramInt);
      if (this.thisRow.isNull(--paramInt)) {
        this.wasNullFlag = true;
        return null;
      } 
      this.wasNullFlag = false;
      return this.thisRow.getReader(paramInt);
    } 
    return getNativeCharacterStream(paramInt);
  }
  
  public Reader getCharacterStream(String paramString) throws SQLException {
    return getCharacterStream(findColumn(paramString));
  }
  
  public Clob getClob(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      String str = getStringForClob(paramInt);
      return (str == null) ? null : new Clob(str, getExceptionInterceptor());
    } 
    return getNativeClob(paramInt);
  }
  
  public Clob getClob(String paramString) throws SQLException {
    return getClob(findColumn(paramString));
  }
  
  public int getConcurrency() throws SQLException {
    return 1007;
  }
  
  public String getCursorName() throws SQLException {
    throw SQLError.createSQLException(Messages.getString("ResultSet.Positioned_Update_not_supported"), "S1C00", getExceptionInterceptor());
  }
  
  public Date getDate(int paramInt) throws SQLException {
    return getDate(paramInt, (Calendar)null);
  }
  
  public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
    if (this.isBinaryEncoded)
      return getNativeDate(paramInt, paramCalendar); 
    if (!this.useFastDateParsing) {
      String str = getStringInternal(paramInt, false);
      return (str == null) ? null : getDateFromString(str, paramInt, paramCalendar);
    } 
    checkColumnBounds(paramInt);
    Date date = this.thisRow.getDateFast(--paramInt, this.connection, this, paramCalendar);
    if (this.thisRow.isNull(paramInt) || date == null) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    return date;
  }
  
  public Date getDate(String paramString) throws SQLException {
    return getDate(findColumn(paramString));
  }
  
  public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
    return getDate(findColumn(paramString), paramCalendar);
  }
  
  public double getDouble(int paramInt) throws SQLException {
    return !this.isBinaryEncoded ? getDoubleInternal(paramInt) : getNativeDouble(paramInt);
  }
  
  public double getDouble(String paramString) throws SQLException {
    return getDouble(findColumn(paramString));
  }
  
  public double getDoubleInternal(int paramInt) throws SQLException {
    return getDoubleInternal(getString(paramInt), paramInt);
  }
  
  public double getDoubleInternal(String paramString, int paramInt) throws SQLException {
    if (paramString == null)
      return 0.0D; 
    try {
      if (paramString.length() == 0)
        return convertToZeroWithEmptyCheck(); 
      double d2 = Double.parseDouble(paramString);
      boolean bool = this.useStrictFloatingPoint;
      double d1 = d2;
      if (bool)
        if (d2 == 2.147483648E9D) {
          d1 = 2.147483647E9D;
        } else if (d2 == 1.0000000036275E-15D) {
          d1 = 1.0E-15D;
        } else if (d2 == 9.999999869911E14D) {
          d1 = 9.99999999999999E14D;
        } else if (d2 == 1.4012984643248E-45D || d2 == 1.4013E-45D) {
          d1 = 1.4E-45D;
        } else {
          if (d2 == 3.4028234663853E37D)
            return 3.4028235E37D; 
          if (d2 == -2.14748E9D) {
            d1 = -2.147483648E9D;
          } else {
            d1 = d2;
            if (d2 == 3.40282E37D)
              return 3.4028235E37D; 
          } 
        }  
      return d1;
    } catch (NumberFormatException numberFormatException) {
      if (this.fields[paramInt - 1].getMysqlType() == 16)
        return getNumericRepresentationOfSQLBitType(paramInt); 
      throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_number", new Object[] { paramString, Integer.valueOf(paramInt) }), "S1009", getExceptionInterceptor());
    } 
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  public int getFetchDirection() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.fetchDirection;
    } 
  }
  
  public int getFetchSize() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.fetchSize;
    } 
  }
  
  public char getFirstCharOfQuery() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        return this.firstCharOfQuery;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public float getFloat(int paramInt) throws SQLException {
    return !this.isBinaryEncoded ? getFloatFromString(getString(paramInt), paramInt) : getNativeFloat(paramInt);
  }
  
  public float getFloat(String paramString) throws SQLException {
    return getFloat(findColumn(paramString));
  }
  
  public Calendar getGmtCalendar() {
    if (this.gmtCalendar == null)
      this.gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
    return this.gmtCalendar;
  }
  
  public int getInt(int paramInt) throws SQLException {
    checkRowPos();
    checkColumnBounds(paramInt);
    if (!this.isBinaryEncoded) {
      int i = paramInt - 1;
      if (this.thisRow.isNull(i)) {
        this.wasNullFlag = true;
        return 0;
      } 
      this.wasNullFlag = false;
      if (this.fields[i].getMysqlType() == 16) {
        long l = getNumericRepresentationOfSQLBitType(paramInt);
        if (this.jdbcCompliantTruncationForReads && (l < -2147483648L || l > 2147483647L))
          throwRangeException(String.valueOf(l), paramInt, 4); 
        return (int)l;
      } 
      if (this.useFastIntParsing) {
        if (this.thisRow.length(i) == 0L)
          return convertToZeroWithEmptyCheck(); 
        if (!this.thisRow.isFloatingPointNumber(i))
          try {
            return getIntWithOverflowCheck(i);
          } catch (NumberFormatException numberFormatException) {
            try {
              return parseIntAsDouble(paramInt, this.thisRow.getString(i, this.fields[i].getEncoding(), this.connection));
            } catch (NumberFormatException numberFormatException1) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74"));
              stringBuilder.append(this.thisRow.getString(i, this.fields[i].getEncoding(), this.connection));
              stringBuilder.append("'");
              throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
            } 
          }  
      } 
      String str = null;
      try {
        String str1 = getString(paramInt);
        if (str1 == null)
          return 0; 
        try {
          if (str1.length() == 0)
            return convertToZeroWithEmptyCheck(); 
          if (str1.indexOf("e") == -1 && str1.indexOf("E") == -1 && str1.indexOf(".") == -1) {
            int j = Integer.parseInt(str1);
            checkForIntegerTruncation(i, null, j);
            return j;
          } 
          i = parseIntAsDouble(paramInt, str1);
          checkForIntegerTruncation(paramInt, null, i);
          return i;
        } catch (NumberFormatException numberFormatException) {
          str = str1;
        } 
      } catch (NumberFormatException numberFormatException) {}
      try {
        return parseIntAsDouble(paramInt, str);
      } catch (NumberFormatException numberFormatException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74"));
        stringBuilder.append(str);
        stringBuilder.append("'");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      } 
    } 
    return getNativeInt(paramInt);
  }
  
  public int getInt(String paramString) throws SQLException {
    return getInt(findColumn(paramString));
  }
  
  public long getLong(int paramInt) throws SQLException {
    return getLong(paramInt, true);
  }
  
  public long getLong(String paramString) throws SQLException {
    return getLong(findColumn(paramString));
  }
  
  public ResultSetMetaData getMetaData() throws SQLException {
    checkClosed();
    return new ResultSetMetaData(this.fields, this.connection.getUseOldAliasMetadataBehavior(), this.connection.getYearIsDateType(), getExceptionInterceptor());
  }
  
  public Array getNativeArray(int paramInt) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public InputStream getNativeAsciiStream(int paramInt) throws SQLException {
    checkRowPos();
    return getNativeBinaryStream(paramInt);
  }
  
  public BigDecimal getNativeBigDecimal(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    return getNativeBigDecimal(paramInt, this.fields[paramInt - 1].getDecimals());
  }
  
  public BigDecimal getNativeBigDecimal(int paramInt1, int paramInt2) throws SQLException {
    String str;
    checkColumnBounds(paramInt1);
    Field[] arrayOfField = this.fields;
    int i = paramInt1 - 1;
    Field field = arrayOfField[i];
    byte[] arrayOfByte = this.thisRow.getColumnValue(i);
    if (arrayOfByte == null) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    i = field.getSQLType();
    if (i != 2 && i != 3) {
      str = getNativeString(paramInt1);
    } else {
      str = StringUtils.toAsciiString(arrayOfByte);
    } 
    return getBigDecimalFromString(str, paramInt1, paramInt2);
  }
  
  public InputStream getNativeBinaryStream(int paramInt) throws SQLException {
    checkRowPos();
    int j = paramInt - 1;
    if (this.thisRow.isNull(j)) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    int i = this.fields[j].getSQLType();
    if (i != -7 && i != 2004 && i != -4 && i != -3 && i != -2) {
      byte[] arrayOfByte = getNativeBytes(paramInt, false);
      return (arrayOfByte != null) ? new ByteArrayInputStream(arrayOfByte) : null;
    } 
    return this.thisRow.getBinaryInputStream(j);
  }
  
  public Blob getNativeBlob(int paramInt) throws SQLException {
    checkRowPos();
    checkColumnBounds(paramInt);
    ResultSetRow resultSetRow = this.thisRow;
    int i = paramInt - 1;
    byte[] arrayOfByte = resultSetRow.getColumnValue(i);
    if (arrayOfByte == null) {
      this.wasNullFlag = true;
    } else {
      this.wasNullFlag = false;
    } 
    if (this.wasNullFlag)
      return null; 
    switch (this.fields[i].getMysqlType()) {
      default:
        arrayOfByte = getNativeBytes(paramInt, false);
        break;
      case 249:
      case 250:
      case 251:
      case 252:
        break;
    } 
    return (Blob)(!this.connection.getEmulateLocators() ? new Blob(arrayOfByte, getExceptionInterceptor()) : new BlobFromLocator(this, paramInt, getExceptionInterceptor()));
  }
  
  public byte getNativeByte(int paramInt) throws SQLException {
    return getNativeByte(paramInt, true);
  }
  
  public byte getNativeByte(int paramInt, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkRowPos : ()V
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual checkColumnBounds : (I)V
    //   9: aload_0
    //   10: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   13: iload_1
    //   14: iconst_1
    //   15: isub
    //   16: invokevirtual getColumnValue : (I)[B
    //   19: astore #10
    //   21: aload #10
    //   23: ifnonnull -> 33
    //   26: aload_0
    //   27: iconst_1
    //   28: putfield wasNullFlag : Z
    //   31: iconst_0
    //   32: ireturn
    //   33: aload_0
    //   34: iconst_0
    //   35: putfield wasNullFlag : Z
    //   38: iload_1
    //   39: iconst_1
    //   40: isub
    //   41: istore #7
    //   43: aload_0
    //   44: getfield fields : [Lcom/mysql/jdbc/Field;
    //   47: iload #7
    //   49: aaload
    //   50: astore #11
    //   52: aload #11
    //   54: invokevirtual getMysqlType : ()I
    //   57: istore_1
    //   58: iload_1
    //   59: iconst_1
    //   60: if_icmpeq -> 527
    //   63: iload_1
    //   64: iconst_2
    //   65: if_icmpeq -> 474
    //   68: iload_1
    //   69: iconst_3
    //   70: if_icmpeq -> 420
    //   73: iload_1
    //   74: iconst_4
    //   75: if_icmpeq -> 362
    //   78: iload_1
    //   79: iconst_5
    //   80: if_icmpeq -> 304
    //   83: iload_1
    //   84: bipush #8
    //   86: if_icmpeq -> 244
    //   89: iload_1
    //   90: bipush #9
    //   92: if_icmpeq -> 420
    //   95: iload_1
    //   96: bipush #13
    //   98: if_icmpeq -> 474
    //   101: iload_1
    //   102: bipush #16
    //   104: if_icmpeq -> 186
    //   107: aload_0
    //   108: getfield useUsageAdvisor : Z
    //   111: ifeq -> 170
    //   114: aload_0
    //   115: ldc_w 'getByte()'
    //   118: iload #7
    //   120: aload_0
    //   121: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   124: iload #7
    //   126: iconst_1
    //   127: isub
    //   128: invokevirtual getColumnValue : (I)[B
    //   131: aload_0
    //   132: getfield fields : [Lcom/mysql/jdbc/Field;
    //   135: iload #7
    //   137: aaload
    //   138: bipush #6
    //   140: newarray int
    //   142: dup
    //   143: iconst_0
    //   144: iconst_5
    //   145: iastore
    //   146: dup
    //   147: iconst_1
    //   148: iconst_1
    //   149: iastore
    //   150: dup
    //   151: iconst_2
    //   152: iconst_2
    //   153: iastore
    //   154: dup
    //   155: iconst_3
    //   156: iconst_3
    //   157: iastore
    //   158: dup
    //   159: iconst_4
    //   160: bipush #8
    //   162: iastore
    //   163: dup
    //   164: iconst_5
    //   165: iconst_4
    //   166: iastore
    //   167: invokespecial issueConversionViaParsingWarning : (Ljava/lang/String;ILjava/lang/Object;Lcom/mysql/jdbc/Field;[I)V
    //   170: iload #7
    //   172: iconst_1
    //   173: iadd
    //   174: istore_1
    //   175: aload_0
    //   176: aload_0
    //   177: iload_1
    //   178: invokevirtual getNativeString : (I)Ljava/lang/String;
    //   181: iload_1
    //   182: invokespecial getByteFromString : (Ljava/lang/String;I)B
    //   185: ireturn
    //   186: iload #7
    //   188: iconst_1
    //   189: iadd
    //   190: istore_1
    //   191: aload_0
    //   192: iload_1
    //   193: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   196: lstore #8
    //   198: iload_2
    //   199: ifeq -> 239
    //   202: aload_0
    //   203: getfield jdbcCompliantTruncationForReads : Z
    //   206: ifeq -> 239
    //   209: lload #8
    //   211: ldc2_w -128
    //   214: lcmp
    //   215: iflt -> 227
    //   218: lload #8
    //   220: ldc2_w 127
    //   223: lcmp
    //   224: ifle -> 239
    //   227: aload_0
    //   228: lload #8
    //   230: invokestatic valueOf : (J)Ljava/lang/String;
    //   233: iload_1
    //   234: bipush #-6
    //   236: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   239: lload #8
    //   241: l2i
    //   242: i2b
    //   243: ireturn
    //   244: iload #7
    //   246: iconst_1
    //   247: iadd
    //   248: istore_1
    //   249: aload_0
    //   250: iload_1
    //   251: iconst_0
    //   252: iconst_1
    //   253: invokevirtual getNativeLong : (IZZ)J
    //   256: lstore #8
    //   258: iload_2
    //   259: ifeq -> 299
    //   262: aload_0
    //   263: getfield jdbcCompliantTruncationForReads : Z
    //   266: ifeq -> 299
    //   269: lload #8
    //   271: ldc2_w -128
    //   274: lcmp
    //   275: iflt -> 287
    //   278: lload #8
    //   280: ldc2_w 127
    //   283: lcmp
    //   284: ifle -> 299
    //   287: aload_0
    //   288: lload #8
    //   290: invokestatic valueOf : (J)Ljava/lang/String;
    //   293: iload_1
    //   294: bipush #-6
    //   296: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   299: lload #8
    //   301: l2i
    //   302: i2b
    //   303: ireturn
    //   304: iload #7
    //   306: iconst_1
    //   307: iadd
    //   308: istore_1
    //   309: aload_0
    //   310: iload_1
    //   311: invokevirtual getNativeDouble : (I)D
    //   314: dstore #4
    //   316: iload_2
    //   317: ifeq -> 357
    //   320: aload_0
    //   321: getfield jdbcCompliantTruncationForReads : Z
    //   324: ifeq -> 357
    //   327: dload #4
    //   329: ldc2_w -128.0
    //   332: dcmpg
    //   333: iflt -> 345
    //   336: dload #4
    //   338: ldc2_w 127.0
    //   341: dcmpl
    //   342: ifle -> 357
    //   345: aload_0
    //   346: dload #4
    //   348: invokestatic valueOf : (D)Ljava/lang/String;
    //   351: iload_1
    //   352: bipush #-6
    //   354: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   357: dload #4
    //   359: d2i
    //   360: i2b
    //   361: ireturn
    //   362: iload #7
    //   364: iconst_1
    //   365: iadd
    //   366: istore_1
    //   367: aload_0
    //   368: iload_1
    //   369: invokevirtual getNativeFloat : (I)F
    //   372: fstore #6
    //   374: iload_2
    //   375: ifeq -> 415
    //   378: aload_0
    //   379: getfield jdbcCompliantTruncationForReads : Z
    //   382: ifeq -> 415
    //   385: fload #6
    //   387: ldc_w -128.0
    //   390: fcmpg
    //   391: iflt -> 403
    //   394: fload #6
    //   396: ldc_w 127.0
    //   399: fcmpl
    //   400: ifle -> 415
    //   403: aload_0
    //   404: fload #6
    //   406: invokestatic valueOf : (F)Ljava/lang/String;
    //   409: iload_1
    //   410: bipush #-6
    //   412: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   415: fload #6
    //   417: f2i
    //   418: i2b
    //   419: ireturn
    //   420: iload #7
    //   422: iconst_1
    //   423: iadd
    //   424: istore_1
    //   425: aload_0
    //   426: iload_1
    //   427: iconst_0
    //   428: invokevirtual getNativeInt : (IZ)I
    //   431: istore #7
    //   433: iload_2
    //   434: ifeq -> 470
    //   437: aload_0
    //   438: getfield jdbcCompliantTruncationForReads : Z
    //   441: ifeq -> 470
    //   444: iload #7
    //   446: bipush #-128
    //   448: if_icmplt -> 458
    //   451: iload #7
    //   453: bipush #127
    //   455: if_icmple -> 470
    //   458: aload_0
    //   459: iload #7
    //   461: invokestatic valueOf : (I)Ljava/lang/String;
    //   464: iload_1
    //   465: bipush #-6
    //   467: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   470: iload #7
    //   472: i2b
    //   473: ireturn
    //   474: iload #7
    //   476: iconst_1
    //   477: iadd
    //   478: istore_1
    //   479: aload_0
    //   480: iload_1
    //   481: invokevirtual getNativeShort : (I)S
    //   484: istore #7
    //   486: iload_2
    //   487: ifeq -> 523
    //   490: aload_0
    //   491: getfield jdbcCompliantTruncationForReads : Z
    //   494: ifeq -> 523
    //   497: iload #7
    //   499: bipush #-128
    //   501: if_icmplt -> 511
    //   504: iload #7
    //   506: bipush #127
    //   508: if_icmple -> 523
    //   511: aload_0
    //   512: iload #7
    //   514: invokestatic valueOf : (I)Ljava/lang/String;
    //   517: iload_1
    //   518: bipush #-6
    //   520: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   523: iload #7
    //   525: i2b
    //   526: ireturn
    //   527: aload #10
    //   529: iconst_0
    //   530: baload
    //   531: istore_3
    //   532: aload #11
    //   534: invokevirtual isUnsigned : ()Z
    //   537: ifne -> 542
    //   540: iload_3
    //   541: ireturn
    //   542: iload_3
    //   543: iflt -> 551
    //   546: iload_3
    //   547: istore_1
    //   548: goto -> 557
    //   551: iload_3
    //   552: sipush #256
    //   555: iadd
    //   556: istore_1
    //   557: iload_1
    //   558: i2s
    //   559: istore_1
    //   560: iload_2
    //   561: ifeq -> 591
    //   564: aload_0
    //   565: getfield jdbcCompliantTruncationForReads : Z
    //   568: ifeq -> 591
    //   571: iload_1
    //   572: bipush #127
    //   574: if_icmple -> 591
    //   577: aload_0
    //   578: iload_1
    //   579: invokestatic valueOf : (I)Ljava/lang/String;
    //   582: iload #7
    //   584: iconst_1
    //   585: iadd
    //   586: bipush #-6
    //   588: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   591: iload_1
    //   592: i2b
    //   593: ireturn
  }
  
  public byte[] getNativeBytes(int paramInt, boolean paramBoolean) throws SQLException {
    checkRowPos();
    checkColumnBounds(paramInt);
    ResultSetRow resultSetRow = this.thisRow;
    int i = paramInt - 1;
    byte[] arrayOfByte = resultSetRow.getColumnValue(i);
    if (arrayOfByte == null) {
      this.wasNullFlag = true;
    } else {
      this.wasNullFlag = false;
    } 
    if (this.wasNullFlag)
      return null; 
    Field field = this.fields[i];
    i = field.getMysqlType();
    if (paramBoolean)
      i = 252; 
    if (i != 15)
      if (i != 16) {
        switch (i) {
          case 249:
          case 250:
          case 251:
          case 252:
            return arrayOfByte;
          case 253:
          case 254:
            if (arrayOfByte instanceof byte[])
              return arrayOfByte; 
            break;
        } 
        i = field.getSQLType();
        return (i == -3 || i == -2) ? arrayOfByte : getBytesFromString(getNativeString(paramInt));
      }  
  }
  
  public Reader getNativeCharacterStream(int paramInt) throws SQLException {
    int i = paramInt - 1;
    int j = this.fields[i].getSQLType();
    if (j != -1 && j != 1 && j != 12 && j != 2005) {
      String str = getStringForClob(paramInt);
      return (str == null) ? null : getCharacterStreamFromString(str);
    } 
    if (this.thisRow.isNull(i)) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    return this.thisRow.getReader(i);
  }
  
  public Clob getNativeClob(int paramInt) throws SQLException {
    String str = getStringForClob(paramInt);
    return (str == null) ? null : getClobFromString(str);
  }
  
  public Date getNativeDate(int paramInt) throws SQLException {
    return getNativeDate(paramInt, null);
  }
  
  public Date getNativeDate(int paramInt, Calendar paramCalendar) throws SQLException {
    Date date;
    checkRowPos();
    checkColumnBounds(paramInt);
    int i = this.fields[--paramInt].getMysqlType();
    if (i == 10) {
      date = this.thisRow.getNativeDate(paramInt, this.connection, this, paramCalendar);
    } else {
      TimeZone timeZone;
      boolean bool;
      if (date != null) {
        timeZone = date.getTimeZone();
      } else {
        timeZone = getDefaultTimeZone();
      } 
      if (timeZone != null && !timeZone.equals(getDefaultTimeZone())) {
        bool = true;
      } else {
        bool = false;
      } 
      date = (Date)this.thisRow.getNativeDateTimeValue(paramInt, null, 91, i, timeZone, bool, this.connection, this);
    } 
    if (date == null) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    return date;
  }
  
  public Date getNativeDateViaParseConversion(int paramInt) throws SQLException {
    if (this.useUsageAdvisor) {
      ResultSetRow resultSetRow = this.thisRow;
      int i = paramInt - 1;
      issueConversionViaParsingWarning("getDate()", paramInt, resultSetRow.getColumnValue(i), this.fields[i], new int[] { 10 });
    } 
    return getDateFromString(getNativeString(paramInt), paramInt, null);
  }
  
  public double getNativeDouble(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkRowPos : ()V
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual checkColumnBounds : (I)V
    //   9: iinc #1, -1
    //   12: aload_0
    //   13: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   16: iload_1
    //   17: invokevirtual isNull : (I)Z
    //   20: ifeq -> 30
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield wasNullFlag : Z
    //   28: dconst_0
    //   29: dreturn
    //   30: aload_0
    //   31: iconst_0
    //   32: putfield wasNullFlag : Z
    //   35: aload_0
    //   36: getfield fields : [Lcom/mysql/jdbc/Field;
    //   39: iload_1
    //   40: aaload
    //   41: astore #5
    //   43: aload #5
    //   45: invokevirtual getMysqlType : ()I
    //   48: istore_2
    //   49: iload_2
    //   50: iconst_1
    //   51: if_icmpeq -> 275
    //   54: iload_2
    //   55: iconst_2
    //   56: if_icmpeq -> 249
    //   59: iload_2
    //   60: iconst_3
    //   61: if_icmpeq -> 223
    //   64: iload_2
    //   65: iconst_4
    //   66: if_icmpeq -> 214
    //   69: iload_2
    //   70: iconst_5
    //   71: if_icmpeq -> 205
    //   74: iload_2
    //   75: bipush #8
    //   77: if_icmpeq -> 178
    //   80: iload_2
    //   81: bipush #9
    //   83: if_icmpeq -> 223
    //   86: iload_2
    //   87: bipush #13
    //   89: if_icmpeq -> 249
    //   92: iload_2
    //   93: bipush #16
    //   95: if_icmpeq -> 169
    //   98: iload_1
    //   99: iconst_1
    //   100: iadd
    //   101: istore_2
    //   102: aload_0
    //   103: iload_2
    //   104: invokevirtual getNativeString : (I)Ljava/lang/String;
    //   107: astore #5
    //   109: aload_0
    //   110: getfield useUsageAdvisor : Z
    //   113: ifeq -> 161
    //   116: aload_0
    //   117: ldc_w 'getDouble()'
    //   120: iload_1
    //   121: aload #5
    //   123: aload_0
    //   124: getfield fields : [Lcom/mysql/jdbc/Field;
    //   127: iload_1
    //   128: aaload
    //   129: bipush #6
    //   131: newarray int
    //   133: dup
    //   134: iconst_0
    //   135: iconst_5
    //   136: iastore
    //   137: dup
    //   138: iconst_1
    //   139: iconst_1
    //   140: iastore
    //   141: dup
    //   142: iconst_2
    //   143: iconst_2
    //   144: iastore
    //   145: dup
    //   146: iconst_3
    //   147: iconst_3
    //   148: iastore
    //   149: dup
    //   150: iconst_4
    //   151: bipush #8
    //   153: iastore
    //   154: dup
    //   155: iconst_5
    //   156: iconst_4
    //   157: iastore
    //   158: invokespecial issueConversionViaParsingWarning : (Ljava/lang/String;ILjava/lang/Object;Lcom/mysql/jdbc/Field;[I)V
    //   161: aload_0
    //   162: aload #5
    //   164: iload_2
    //   165: invokespecial getDoubleFromString : (Ljava/lang/String;I)D
    //   168: dreturn
    //   169: aload_0
    //   170: iload_1
    //   171: iconst_1
    //   172: iadd
    //   173: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   176: l2d
    //   177: dreturn
    //   178: aload_0
    //   179: iload_1
    //   180: iconst_1
    //   181: iadd
    //   182: invokevirtual getNativeLong : (I)J
    //   185: lstore_3
    //   186: aload #5
    //   188: invokevirtual isUnsigned : ()Z
    //   191: ifne -> 197
    //   194: lload_3
    //   195: l2d
    //   196: dreturn
    //   197: lload_3
    //   198: invokestatic convertLongToUlong : (J)Ljava/math/BigInteger;
    //   201: invokevirtual doubleValue : ()D
    //   204: dreturn
    //   205: aload_0
    //   206: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   209: iload_1
    //   210: invokevirtual getNativeDouble : (I)D
    //   213: dreturn
    //   214: aload_0
    //   215: iload_1
    //   216: iconst_1
    //   217: iadd
    //   218: invokevirtual getNativeFloat : (I)F
    //   221: f2d
    //   222: dreturn
    //   223: aload #5
    //   225: invokevirtual isUnsigned : ()Z
    //   228: ifne -> 240
    //   231: aload_0
    //   232: iload_1
    //   233: iconst_1
    //   234: iadd
    //   235: invokevirtual getNativeInt : (I)I
    //   238: i2d
    //   239: dreturn
    //   240: aload_0
    //   241: iload_1
    //   242: iconst_1
    //   243: iadd
    //   244: invokevirtual getNativeLong : (I)J
    //   247: l2d
    //   248: dreturn
    //   249: aload #5
    //   251: invokevirtual isUnsigned : ()Z
    //   254: ifne -> 266
    //   257: aload_0
    //   258: iload_1
    //   259: iconst_1
    //   260: iadd
    //   261: invokevirtual getNativeShort : (I)S
    //   264: i2d
    //   265: dreturn
    //   266: aload_0
    //   267: iload_1
    //   268: iconst_1
    //   269: iadd
    //   270: invokevirtual getNativeInt : (I)I
    //   273: i2d
    //   274: dreturn
    //   275: aload #5
    //   277: invokevirtual isUnsigned : ()Z
    //   280: ifne -> 292
    //   283: aload_0
    //   284: iload_1
    //   285: iconst_1
    //   286: iadd
    //   287: invokevirtual getNativeByte : (I)B
    //   290: i2d
    //   291: dreturn
    //   292: aload_0
    //   293: iload_1
    //   294: iconst_1
    //   295: iadd
    //   296: invokevirtual getNativeShort : (I)S
    //   299: i2d
    //   300: dreturn
  }
  
  public float getNativeFloat(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkRowPos : ()V
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual checkColumnBounds : (I)V
    //   9: iinc #1, -1
    //   12: aload_0
    //   13: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   16: iload_1
    //   17: invokevirtual isNull : (I)Z
    //   20: ifeq -> 30
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield wasNullFlag : Z
    //   28: fconst_0
    //   29: freturn
    //   30: aload_0
    //   31: iconst_0
    //   32: putfield wasNullFlag : Z
    //   35: aload_0
    //   36: getfield fields : [Lcom/mysql/jdbc/Field;
    //   39: iload_1
    //   40: aaload
    //   41: astore #6
    //   43: aload #6
    //   45: invokevirtual getMysqlType : ()I
    //   48: istore_3
    //   49: iload_3
    //   50: iconst_1
    //   51: if_icmpeq -> 334
    //   54: iload_3
    //   55: iconst_2
    //   56: if_icmpeq -> 308
    //   59: iload_3
    //   60: iconst_3
    //   61: if_icmpeq -> 282
    //   64: iload_3
    //   65: iconst_4
    //   66: if_icmpeq -> 273
    //   69: iload_3
    //   70: iconst_5
    //   71: if_icmpeq -> 208
    //   74: iload_3
    //   75: bipush #8
    //   77: if_icmpeq -> 178
    //   80: iload_3
    //   81: bipush #9
    //   83: if_icmpeq -> 282
    //   86: iload_3
    //   87: bipush #13
    //   89: if_icmpeq -> 308
    //   92: iload_3
    //   93: bipush #16
    //   95: if_icmpeq -> 169
    //   98: iload_1
    //   99: iconst_1
    //   100: iadd
    //   101: istore_3
    //   102: aload_0
    //   103: iload_3
    //   104: invokevirtual getNativeString : (I)Ljava/lang/String;
    //   107: astore #6
    //   109: aload_0
    //   110: getfield useUsageAdvisor : Z
    //   113: ifeq -> 161
    //   116: aload_0
    //   117: ldc_w 'getFloat()'
    //   120: iload_1
    //   121: aload #6
    //   123: aload_0
    //   124: getfield fields : [Lcom/mysql/jdbc/Field;
    //   127: iload_1
    //   128: aaload
    //   129: bipush #6
    //   131: newarray int
    //   133: dup
    //   134: iconst_0
    //   135: iconst_5
    //   136: iastore
    //   137: dup
    //   138: iconst_1
    //   139: iconst_1
    //   140: iastore
    //   141: dup
    //   142: iconst_2
    //   143: iconst_2
    //   144: iastore
    //   145: dup
    //   146: iconst_3
    //   147: iconst_3
    //   148: iastore
    //   149: dup
    //   150: iconst_4
    //   151: bipush #8
    //   153: iastore
    //   154: dup
    //   155: iconst_5
    //   156: iconst_4
    //   157: iastore
    //   158: invokespecial issueConversionViaParsingWarning : (Ljava/lang/String;ILjava/lang/Object;Lcom/mysql/jdbc/Field;[I)V
    //   161: aload_0
    //   162: aload #6
    //   164: iload_3
    //   165: invokespecial getFloatFromString : (Ljava/lang/String;I)F
    //   168: freturn
    //   169: aload_0
    //   170: iload_1
    //   171: iconst_1
    //   172: iadd
    //   173: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   176: l2f
    //   177: freturn
    //   178: aload_0
    //   179: iload_1
    //   180: iconst_1
    //   181: iadd
    //   182: invokevirtual getNativeLong : (I)J
    //   185: lstore #4
    //   187: aload #6
    //   189: invokevirtual isUnsigned : ()Z
    //   192: ifne -> 199
    //   195: lload #4
    //   197: l2f
    //   198: freturn
    //   199: lload #4
    //   201: invokestatic convertLongToUlong : (J)Ljava/math/BigInteger;
    //   204: invokevirtual floatValue : ()F
    //   207: freturn
    //   208: iinc #1, 1
    //   211: new java/lang/Double
    //   214: dup
    //   215: aload_0
    //   216: iload_1
    //   217: invokevirtual getNativeDouble : (I)D
    //   220: invokespecial <init> : (D)V
    //   223: astore #6
    //   225: aload #6
    //   227: invokevirtual floatValue : ()F
    //   230: fstore_2
    //   231: aload_0
    //   232: getfield jdbcCompliantTruncationForReads : Z
    //   235: ifeq -> 246
    //   238: fload_2
    //   239: ldc_w -Infinity
    //   242: fcmpl
    //   243: ifeq -> 254
    //   246: fload_2
    //   247: ldc_w Infinity
    //   250: fcmpl
    //   251: ifne -> 266
    //   254: aload_0
    //   255: aload #6
    //   257: invokevirtual toString : ()Ljava/lang/String;
    //   260: iload_1
    //   261: bipush #6
    //   263: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   266: aload_0
    //   267: iload_1
    //   268: invokevirtual getNativeDouble : (I)D
    //   271: d2f
    //   272: freturn
    //   273: aload_0
    //   274: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   277: iload_1
    //   278: invokevirtual getNativeFloat : (I)F
    //   281: freturn
    //   282: aload #6
    //   284: invokevirtual isUnsigned : ()Z
    //   287: ifne -> 299
    //   290: aload_0
    //   291: iload_1
    //   292: iconst_1
    //   293: iadd
    //   294: invokevirtual getNativeInt : (I)I
    //   297: i2f
    //   298: freturn
    //   299: aload_0
    //   300: iload_1
    //   301: iconst_1
    //   302: iadd
    //   303: invokevirtual getNativeLong : (I)J
    //   306: l2f
    //   307: freturn
    //   308: aload #6
    //   310: invokevirtual isUnsigned : ()Z
    //   313: ifne -> 325
    //   316: aload_0
    //   317: iload_1
    //   318: iconst_1
    //   319: iadd
    //   320: invokevirtual getNativeShort : (I)S
    //   323: i2f
    //   324: freturn
    //   325: aload_0
    //   326: iload_1
    //   327: iconst_1
    //   328: iadd
    //   329: invokevirtual getNativeInt : (I)I
    //   332: i2f
    //   333: freturn
    //   334: aload #6
    //   336: invokevirtual isUnsigned : ()Z
    //   339: ifne -> 351
    //   342: aload_0
    //   343: iload_1
    //   344: iconst_1
    //   345: iadd
    //   346: invokevirtual getNativeByte : (I)B
    //   349: i2f
    //   350: freturn
    //   351: aload_0
    //   352: iload_1
    //   353: iconst_1
    //   354: iadd
    //   355: invokevirtual getNativeShort : (I)S
    //   358: i2f
    //   359: freturn
  }
  
  public int getNativeInt(int paramInt) throws SQLException {
    return getNativeInt(paramInt, true);
  }
  
  public int getNativeInt(int paramInt, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkRowPos : ()V
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual checkColumnBounds : (I)V
    //   9: iinc #1, -1
    //   12: aload_0
    //   13: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   16: iload_1
    //   17: invokevirtual isNull : (I)Z
    //   20: ifeq -> 30
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield wasNullFlag : Z
    //   28: iconst_0
    //   29: ireturn
    //   30: aload_0
    //   31: iconst_0
    //   32: putfield wasNullFlag : Z
    //   35: aload_0
    //   36: getfield fields : [Lcom/mysql/jdbc/Field;
    //   39: iload_1
    //   40: aaload
    //   41: astore #8
    //   43: aload #8
    //   45: invokevirtual getMysqlType : ()I
    //   48: istore #5
    //   50: iload #5
    //   52: iconst_1
    //   53: if_icmpeq -> 511
    //   56: iload #5
    //   58: iconst_2
    //   59: if_icmpeq -> 470
    //   62: iload #5
    //   64: iconst_3
    //   65: if_icmpeq -> 391
    //   68: iload #5
    //   70: iconst_4
    //   71: if_icmpeq -> 341
    //   74: iload #5
    //   76: iconst_5
    //   77: if_icmpeq -> 292
    //   80: iload #5
    //   82: bipush #8
    //   84: if_icmpeq -> 236
    //   87: iload #5
    //   89: bipush #9
    //   91: if_icmpeq -> 391
    //   94: iload #5
    //   96: bipush #13
    //   98: if_icmpeq -> 470
    //   101: iload #5
    //   103: bipush #16
    //   105: if_icmpeq -> 182
    //   108: iload_1
    //   109: iconst_1
    //   110: iadd
    //   111: istore #5
    //   113: aload_0
    //   114: iload #5
    //   116: invokevirtual getNativeString : (I)Ljava/lang/String;
    //   119: astore #8
    //   121: aload_0
    //   122: getfield useUsageAdvisor : Z
    //   125: ifeq -> 173
    //   128: aload_0
    //   129: ldc_w 'getInt()'
    //   132: iload_1
    //   133: aload #8
    //   135: aload_0
    //   136: getfield fields : [Lcom/mysql/jdbc/Field;
    //   139: iload_1
    //   140: aaload
    //   141: bipush #6
    //   143: newarray int
    //   145: dup
    //   146: iconst_0
    //   147: iconst_5
    //   148: iastore
    //   149: dup
    //   150: iconst_1
    //   151: iconst_1
    //   152: iastore
    //   153: dup
    //   154: iconst_2
    //   155: iconst_2
    //   156: iastore
    //   157: dup
    //   158: iconst_3
    //   159: iconst_3
    //   160: iastore
    //   161: dup
    //   162: iconst_4
    //   163: bipush #8
    //   165: iastore
    //   166: dup
    //   167: iconst_5
    //   168: iconst_4
    //   169: iastore
    //   170: invokespecial issueConversionViaParsingWarning : (Ljava/lang/String;ILjava/lang/Object;Lcom/mysql/jdbc/Field;[I)V
    //   173: aload_0
    //   174: aload #8
    //   176: iload #5
    //   178: invokespecial getIntFromString : (Ljava/lang/String;I)I
    //   181: ireturn
    //   182: iinc #1, 1
    //   185: aload_0
    //   186: iload_1
    //   187: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   190: lstore #6
    //   192: iload_2
    //   193: ifeq -> 232
    //   196: aload_0
    //   197: getfield jdbcCompliantTruncationForReads : Z
    //   200: ifeq -> 232
    //   203: lload #6
    //   205: ldc2_w -2147483648
    //   208: lcmp
    //   209: iflt -> 221
    //   212: lload #6
    //   214: ldc2_w 2147483647
    //   217: lcmp
    //   218: ifle -> 232
    //   221: aload_0
    //   222: lload #6
    //   224: invokestatic valueOf : (J)Ljava/lang/String;
    //   227: iload_1
    //   228: iconst_4
    //   229: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   232: lload #6
    //   234: l2i
    //   235: ireturn
    //   236: iinc #1, 1
    //   239: aload_0
    //   240: iload_1
    //   241: iconst_0
    //   242: iconst_1
    //   243: invokevirtual getNativeLong : (IZZ)J
    //   246: lstore #6
    //   248: iload_2
    //   249: ifeq -> 288
    //   252: aload_0
    //   253: getfield jdbcCompliantTruncationForReads : Z
    //   256: ifeq -> 288
    //   259: lload #6
    //   261: ldc2_w -2147483648
    //   264: lcmp
    //   265: iflt -> 277
    //   268: lload #6
    //   270: ldc2_w 2147483647
    //   273: lcmp
    //   274: ifle -> 288
    //   277: aload_0
    //   278: lload #6
    //   280: invokestatic valueOf : (J)Ljava/lang/String;
    //   283: iload_1
    //   284: iconst_4
    //   285: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   288: lload #6
    //   290: l2i
    //   291: ireturn
    //   292: iinc #1, 1
    //   295: aload_0
    //   296: iload_1
    //   297: invokevirtual getNativeDouble : (I)D
    //   300: dstore_3
    //   301: iload_2
    //   302: ifeq -> 338
    //   305: aload_0
    //   306: getfield jdbcCompliantTruncationForReads : Z
    //   309: ifeq -> 338
    //   312: dload_3
    //   313: ldc2_w -2.147483648E9
    //   316: dcmpg
    //   317: iflt -> 328
    //   320: dload_3
    //   321: ldc2_w 2.147483647E9
    //   324: dcmpl
    //   325: ifle -> 338
    //   328: aload_0
    //   329: dload_3
    //   330: invokestatic valueOf : (D)Ljava/lang/String;
    //   333: iload_1
    //   334: iconst_4
    //   335: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   338: dload_3
    //   339: d2i
    //   340: ireturn
    //   341: iinc #1, 1
    //   344: aload_0
    //   345: iload_1
    //   346: invokevirtual getNativeFloat : (I)F
    //   349: f2d
    //   350: dstore_3
    //   351: iload_2
    //   352: ifeq -> 388
    //   355: aload_0
    //   356: getfield jdbcCompliantTruncationForReads : Z
    //   359: ifeq -> 388
    //   362: dload_3
    //   363: ldc2_w -2.147483648E9
    //   366: dcmpg
    //   367: iflt -> 378
    //   370: dload_3
    //   371: ldc2_w 2.147483647E9
    //   374: dcmpl
    //   375: ifle -> 388
    //   378: aload_0
    //   379: dload_3
    //   380: invokestatic valueOf : (D)Ljava/lang/String;
    //   383: iload_1
    //   384: iconst_4
    //   385: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   388: dload_3
    //   389: d2i
    //   390: ireturn
    //   391: aload_0
    //   392: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   395: iload_1
    //   396: invokevirtual getNativeInt : (I)I
    //   399: istore #5
    //   401: aload #8
    //   403: invokevirtual isUnsigned : ()Z
    //   406: ifne -> 412
    //   409: iload #5
    //   411: ireturn
    //   412: iload #5
    //   414: i2l
    //   415: lstore #6
    //   417: iload #5
    //   419: iflt -> 425
    //   422: goto -> 433
    //   425: lload #6
    //   427: ldc2_w 4294967296
    //   430: ladd
    //   431: lstore #6
    //   433: iload_2
    //   434: ifeq -> 466
    //   437: aload_0
    //   438: getfield jdbcCompliantTruncationForReads : Z
    //   441: ifeq -> 466
    //   444: lload #6
    //   446: ldc2_w 2147483647
    //   449: lcmp
    //   450: ifle -> 466
    //   453: aload_0
    //   454: lload #6
    //   456: invokestatic valueOf : (J)Ljava/lang/String;
    //   459: iload_1
    //   460: iconst_1
    //   461: iadd
    //   462: iconst_4
    //   463: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   466: lload #6
    //   468: l2i
    //   469: ireturn
    //   470: aload_0
    //   471: iload_1
    //   472: iconst_1
    //   473: iadd
    //   474: iconst_0
    //   475: invokevirtual getNativeShort : (IZ)S
    //   478: istore #5
    //   480: iload #5
    //   482: istore_1
    //   483: aload #8
    //   485: invokevirtual isUnsigned : ()Z
    //   488: ifeq -> 509
    //   491: iload #5
    //   493: iflt -> 502
    //   496: iload #5
    //   498: istore_1
    //   499: goto -> 509
    //   502: iload #5
    //   504: ldc_w 65536
    //   507: iadd
    //   508: istore_1
    //   509: iload_1
    //   510: ireturn
    //   511: aload_0
    //   512: iload_1
    //   513: iconst_1
    //   514: iadd
    //   515: iconst_0
    //   516: invokevirtual getNativeByte : (IZ)B
    //   519: istore #5
    //   521: iload #5
    //   523: istore_1
    //   524: aload #8
    //   526: invokevirtual isUnsigned : ()Z
    //   529: ifeq -> 550
    //   532: iload #5
    //   534: iflt -> 543
    //   537: iload #5
    //   539: istore_1
    //   540: goto -> 550
    //   543: iload #5
    //   545: sipush #256
    //   548: iadd
    //   549: istore_1
    //   550: iload_1
    //   551: ireturn
  }
  
  public long getNativeLong(int paramInt) throws SQLException {
    return getNativeLong(paramInt, true, true);
  }
  
  public long getNativeLong(int paramInt, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    BigInteger bigInteger;
    checkRowPos();
    checkColumnBounds(paramInt);
    if (this.thisRow.isNull(--paramInt)) {
      this.wasNullFlag = true;
      return 0L;
    } 
    this.wasNullFlag = false;
    Field field = this.fields[paramInt];
    int i = field.getMysqlType();
    if (i != 1) {
      if (i != 2) {
        if (i != 3)
          if (i != 4) {
            if (i != 5) {
              String str;
              if (i != 8) {
                if (i != 9) {
                  if (i != 13) {
                    if (i != 16) {
                      i = paramInt + 1;
                      str = getNativeString(i);
                      if (this.useUsageAdvisor)
                        issueConversionViaParsingWarning("getLong()", paramInt, str, this.fields[paramInt], new int[] { 5, 1, 2, 3, 8, 4 }); 
                      return getLongFromString(str, i);
                    } 
                    return getNumericRepresentationOfSQLBitType(paramInt + 1);
                  } 
                  return getNativeShort(paramInt + 1);
                } 
              } else {
                long l = this.thisRow.getNativeLong(paramInt);
                if (!str.isUnsigned() || !paramBoolean2)
                  return l; 
                bigInteger = convertLongToUlong(l);
                if (paramBoolean1 && this.jdbcCompliantTruncationForReads && (bigInteger.compareTo(new BigInteger(String.valueOf(Long.MAX_VALUE))) > 0 || bigInteger.compareTo(new BigInteger(String.valueOf(Long.MIN_VALUE))) < 0))
                  throwRangeException(bigInteger.toString(), paramInt + 1, -5); 
                return getLongFromString(bigInteger.toString(), paramInt);
              } 
            } else {
              double d = getNativeDouble(++paramInt);
              if (paramBoolean1 && this.jdbcCompliantTruncationForReads && (d < -9.223372036854776E18D || d > 9.223372036854776E18D))
                throwRangeException(String.valueOf(d), paramInt, -5); 
              return (long)d;
            } 
          } else {
            double d = getNativeFloat(++paramInt);
            if (paramBoolean1 && this.jdbcCompliantTruncationForReads && (d < -9.223372036854776E18D || d > 9.223372036854776E18D))
              throwRangeException(String.valueOf(d), paramInt, -5); 
            return (long)d;
          }  
        paramInt = getNativeInt(paramInt + 1, false);
        return (!bigInteger.isUnsigned() || paramInt >= 0) ? paramInt : (paramInt + 4294967296L);
      } 
      return !bigInteger.isUnsigned() ? getNativeShort(paramInt + 1) : getNativeInt(paramInt + 1, false);
    } 
    return !bigInteger.isUnsigned() ? getNativeByte(paramInt + 1) : getNativeInt(paramInt + 1);
  }
  
  public Ref getNativeRef(int paramInt) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public short getNativeShort(int paramInt) throws SQLException {
    return getNativeShort(paramInt, true);
  }
  
  public short getNativeShort(int paramInt, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkRowPos : ()V
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual checkColumnBounds : (I)V
    //   9: iinc #1, -1
    //   12: aload_0
    //   13: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   16: iload_1
    //   17: invokevirtual isNull : (I)Z
    //   20: ifeq -> 30
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield wasNullFlag : Z
    //   28: iconst_0
    //   29: ireturn
    //   30: aload_0
    //   31: iconst_0
    //   32: putfield wasNullFlag : Z
    //   35: aload_0
    //   36: getfield fields : [Lcom/mysql/jdbc/Field;
    //   39: iload_1
    //   40: aaload
    //   41: astore #10
    //   43: aload #10
    //   45: invokevirtual getMysqlType : ()I
    //   48: istore #7
    //   50: iload #7
    //   52: iconst_1
    //   53: if_icmpeq -> 666
    //   56: iload #7
    //   58: iconst_2
    //   59: if_icmpeq -> 604
    //   62: iload #7
    //   64: iconst_3
    //   65: if_icmpeq -> 495
    //   68: iload #7
    //   70: iconst_4
    //   71: if_icmpeq -> 440
    //   74: iload #7
    //   76: iconst_5
    //   77: if_icmpeq -> 385
    //   80: iload #7
    //   82: bipush #8
    //   84: if_icmpeq -> 237
    //   87: iload #7
    //   89: bipush #9
    //   91: if_icmpeq -> 495
    //   94: iload #7
    //   96: bipush #13
    //   98: if_icmpeq -> 604
    //   101: iload #7
    //   103: bipush #16
    //   105: if_icmpeq -> 182
    //   108: iload_1
    //   109: iconst_1
    //   110: iadd
    //   111: istore #7
    //   113: aload_0
    //   114: iload #7
    //   116: invokevirtual getNativeString : (I)Ljava/lang/String;
    //   119: astore #10
    //   121: aload_0
    //   122: getfield useUsageAdvisor : Z
    //   125: ifeq -> 173
    //   128: aload_0
    //   129: ldc_w 'getShort()'
    //   132: iload_1
    //   133: aload #10
    //   135: aload_0
    //   136: getfield fields : [Lcom/mysql/jdbc/Field;
    //   139: iload_1
    //   140: aaload
    //   141: bipush #6
    //   143: newarray int
    //   145: dup
    //   146: iconst_0
    //   147: iconst_5
    //   148: iastore
    //   149: dup
    //   150: iconst_1
    //   151: iconst_1
    //   152: iastore
    //   153: dup
    //   154: iconst_2
    //   155: iconst_2
    //   156: iastore
    //   157: dup
    //   158: iconst_3
    //   159: iconst_3
    //   160: iastore
    //   161: dup
    //   162: iconst_4
    //   163: bipush #8
    //   165: iastore
    //   166: dup
    //   167: iconst_5
    //   168: iconst_4
    //   169: iastore
    //   170: invokespecial issueConversionViaParsingWarning : (Ljava/lang/String;ILjava/lang/Object;Lcom/mysql/jdbc/Field;[I)V
    //   173: aload_0
    //   174: aload #10
    //   176: iload #7
    //   178: invokespecial getShortFromString : (Ljava/lang/String;I)S
    //   181: ireturn
    //   182: iinc #1, 1
    //   185: aload_0
    //   186: iload_1
    //   187: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   190: lstore #8
    //   192: iload_2
    //   193: ifeq -> 232
    //   196: aload_0
    //   197: getfield jdbcCompliantTruncationForReads : Z
    //   200: ifeq -> 232
    //   203: lload #8
    //   205: ldc2_w -32768
    //   208: lcmp
    //   209: iflt -> 221
    //   212: lload #8
    //   214: ldc2_w 32767
    //   217: lcmp
    //   218: ifle -> 232
    //   221: aload_0
    //   222: lload #8
    //   224: invokestatic valueOf : (J)Ljava/lang/String;
    //   227: iload_1
    //   228: iconst_5
    //   229: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   232: lload #8
    //   234: l2i
    //   235: i2s
    //   236: ireturn
    //   237: iinc #1, 1
    //   240: aload_0
    //   241: iload_1
    //   242: iconst_0
    //   243: iconst_0
    //   244: invokevirtual getNativeLong : (IZZ)J
    //   247: lstore #8
    //   249: aload #10
    //   251: invokevirtual isUnsigned : ()Z
    //   254: ifne -> 302
    //   257: iload_2
    //   258: ifeq -> 297
    //   261: aload_0
    //   262: getfield jdbcCompliantTruncationForReads : Z
    //   265: ifeq -> 297
    //   268: lload #8
    //   270: ldc2_w -32768
    //   273: lcmp
    //   274: iflt -> 286
    //   277: lload #8
    //   279: ldc2_w 32767
    //   282: lcmp
    //   283: ifle -> 297
    //   286: aload_0
    //   287: lload #8
    //   289: invokestatic valueOf : (J)Ljava/lang/String;
    //   292: iload_1
    //   293: iconst_5
    //   294: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   297: lload #8
    //   299: l2i
    //   300: i2s
    //   301: ireturn
    //   302: lload #8
    //   304: invokestatic convertLongToUlong : (J)Ljava/math/BigInteger;
    //   307: astore #10
    //   309: iload_2
    //   310: ifeq -> 373
    //   313: aload_0
    //   314: getfield jdbcCompliantTruncationForReads : Z
    //   317: ifeq -> 373
    //   320: aload #10
    //   322: new java/math/BigInteger
    //   325: dup
    //   326: sipush #32767
    //   329: invokestatic valueOf : (I)Ljava/lang/String;
    //   332: invokespecial <init> : (Ljava/lang/String;)V
    //   335: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   338: ifgt -> 362
    //   341: aload #10
    //   343: new java/math/BigInteger
    //   346: dup
    //   347: sipush #-32768
    //   350: invokestatic valueOf : (I)Ljava/lang/String;
    //   353: invokespecial <init> : (Ljava/lang/String;)V
    //   356: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   359: ifge -> 373
    //   362: aload_0
    //   363: aload #10
    //   365: invokevirtual toString : ()Ljava/lang/String;
    //   368: iload_1
    //   369: iconst_5
    //   370: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   373: aload_0
    //   374: aload #10
    //   376: invokevirtual toString : ()Ljava/lang/String;
    //   379: iload_1
    //   380: invokespecial getIntFromString : (Ljava/lang/String;I)I
    //   383: i2s
    //   384: ireturn
    //   385: iinc #1, 1
    //   388: aload_0
    //   389: iload_1
    //   390: invokevirtual getNativeDouble : (I)D
    //   393: dstore #4
    //   395: iload_2
    //   396: ifeq -> 435
    //   399: aload_0
    //   400: getfield jdbcCompliantTruncationForReads : Z
    //   403: ifeq -> 435
    //   406: dload #4
    //   408: ldc2_w -32768.0
    //   411: dcmpg
    //   412: iflt -> 424
    //   415: dload #4
    //   417: ldc2_w 32767.0
    //   420: dcmpl
    //   421: ifle -> 435
    //   424: aload_0
    //   425: dload #4
    //   427: invokestatic valueOf : (D)Ljava/lang/String;
    //   430: iload_1
    //   431: iconst_5
    //   432: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   435: dload #4
    //   437: d2i
    //   438: i2s
    //   439: ireturn
    //   440: iinc #1, 1
    //   443: aload_0
    //   444: iload_1
    //   445: invokevirtual getNativeFloat : (I)F
    //   448: fstore #6
    //   450: iload_2
    //   451: ifeq -> 490
    //   454: aload_0
    //   455: getfield jdbcCompliantTruncationForReads : Z
    //   458: ifeq -> 490
    //   461: fload #6
    //   463: ldc_w -32768.0
    //   466: fcmpg
    //   467: iflt -> 479
    //   470: fload #6
    //   472: ldc_w 32767.0
    //   475: fcmpl
    //   476: ifle -> 490
    //   479: aload_0
    //   480: fload #6
    //   482: invokestatic valueOf : (F)Ljava/lang/String;
    //   485: iload_1
    //   486: iconst_5
    //   487: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   490: fload #6
    //   492: f2i
    //   493: i2s
    //   494: ireturn
    //   495: aload #10
    //   497: invokevirtual isUnsigned : ()Z
    //   500: ifne -> 556
    //   503: iinc #1, 1
    //   506: aload_0
    //   507: iload_1
    //   508: iconst_0
    //   509: invokevirtual getNativeInt : (IZ)I
    //   512: istore #7
    //   514: iload_2
    //   515: ifeq -> 533
    //   518: aload_0
    //   519: getfield jdbcCompliantTruncationForReads : Z
    //   522: ifeq -> 533
    //   525: iload #7
    //   527: sipush #32767
    //   530: if_icmpgt -> 541
    //   533: iload #7
    //   535: sipush #-32768
    //   538: if_icmpge -> 552
    //   541: aload_0
    //   542: iload #7
    //   544: invokestatic valueOf : (I)Ljava/lang/String;
    //   547: iload_1
    //   548: iconst_5
    //   549: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   552: iload #7
    //   554: i2s
    //   555: ireturn
    //   556: iinc #1, 1
    //   559: aload_0
    //   560: iload_1
    //   561: iconst_0
    //   562: iconst_1
    //   563: invokevirtual getNativeLong : (IZZ)J
    //   566: lstore #8
    //   568: iload_2
    //   569: ifeq -> 599
    //   572: aload_0
    //   573: getfield jdbcCompliantTruncationForReads : Z
    //   576: ifeq -> 599
    //   579: lload #8
    //   581: ldc2_w 32767
    //   584: lcmp
    //   585: ifle -> 599
    //   588: aload_0
    //   589: lload #8
    //   591: invokestatic valueOf : (J)Ljava/lang/String;
    //   594: iload_1
    //   595: iconst_5
    //   596: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   599: lload #8
    //   601: l2i
    //   602: i2s
    //   603: ireturn
    //   604: aload_0
    //   605: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   608: iload_1
    //   609: invokevirtual getNativeShort : (I)S
    //   612: istore_3
    //   613: aload #10
    //   615: invokevirtual isUnsigned : ()Z
    //   618: ifne -> 623
    //   621: iload_3
    //   622: ireturn
    //   623: ldc_w 65535
    //   626: iload_3
    //   627: iand
    //   628: istore #7
    //   630: iload_2
    //   631: ifeq -> 662
    //   634: aload_0
    //   635: getfield jdbcCompliantTruncationForReads : Z
    //   638: ifeq -> 662
    //   641: iload #7
    //   643: sipush #32767
    //   646: if_icmple -> 662
    //   649: aload_0
    //   650: iload #7
    //   652: invokestatic valueOf : (I)Ljava/lang/String;
    //   655: iload_1
    //   656: iconst_1
    //   657: iadd
    //   658: iconst_5
    //   659: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   662: iload #7
    //   664: i2s
    //   665: ireturn
    //   666: aload_0
    //   667: iload_1
    //   668: iconst_1
    //   669: iadd
    //   670: iconst_0
    //   671: invokevirtual getNativeByte : (IZ)B
    //   674: istore_1
    //   675: aload #10
    //   677: invokevirtual isUnsigned : ()Z
    //   680: ifeq -> 697
    //   683: iload_1
    //   684: iflt -> 690
    //   687: goto -> 697
    //   690: iload_1
    //   691: sipush #256
    //   694: iadd
    //   695: i2s
    //   696: ireturn
    //   697: iload_1
    //   698: i2s
    //   699: ireturn
  }
  
  public String getNativeString(int paramInt) throws SQLException {
    checkRowPos();
    checkColumnBounds(paramInt);
    if (this.fields != null) {
      ResultSetRow resultSetRow = this.thisRow;
      int i = paramInt - 1;
      if (resultSetRow.isNull(i)) {
        this.wasNullFlag = true;
        return null;
      } 
      this.wasNullFlag = false;
      Field field = this.fields[i];
      String str2 = getNativeConvertToString(paramInt, field);
      paramInt = field.getMysqlType();
      String str1 = str2;
      if (paramInt != 7) {
        str1 = str2;
        if (paramInt != 10) {
          str1 = str2;
          if (field.isZeroFill()) {
            str1 = str2;
            if (str2 != null) {
              paramInt = str2.length();
              StringBuilder stringBuilder = new StringBuilder(paramInt);
              long l2 = field.getLength();
              long l3 = paramInt;
              for (long l1 = 0L; l1 < l2 - l3; l1++)
                stringBuilder.append('0'); 
              stringBuilder.append(str2);
              str1 = stringBuilder.toString();
            } 
          } 
        } 
      } 
      return str1;
    } 
    throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_133"), "S1002", getExceptionInterceptor());
  }
  
  public Time getNativeTimeViaParseConversion(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    if (this.useUsageAdvisor) {
      ResultSetRow resultSetRow = this.thisRow;
      int i = paramInt - 1;
      issueConversionViaParsingWarning("getTime()", paramInt, resultSetRow.getColumnValue(i), this.fields[i], new int[] { 11 });
    } 
    return getTimeFromString(getNativeString(paramInt), paramCalendar, paramInt, paramTimeZone, paramBoolean);
  }
  
  public Timestamp getNativeTimestampViaParseConversion(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    if (this.useUsageAdvisor) {
      ResultSetRow resultSetRow = this.thisRow;
      int i = paramInt - 1;
      issueConversionViaParsingWarning("getTimestamp()", paramInt, resultSetRow.getColumnValue(i), this.fields[i], new int[] { 7, 12 });
    } 
    return getTimestampFromString(paramInt, paramCalendar, getNativeString(paramInt), paramTimeZone, paramBoolean);
  }
  
  public URL getNativeURL(int paramInt) throws SQLException {
    String str = getString(paramInt);
    if (str == null)
      return null; 
    try {
      return new URL(str);
    } catch (MalformedURLException malformedURLException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("ResultSet.Malformed_URL____141"));
      stringBuilder.append(str);
      stringBuilder.append("'");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public InputStream getNativeUnicodeStream(int paramInt) throws SQLException {
    checkRowPos();
    return getBinaryStream(paramInt);
  }
  
  public ResultSetInternalMethods getNextResultSet() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield nextResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Object getObject(int paramInt) throws SQLException {
    String str;
    checkRowPos();
    checkColumnBounds(paramInt);
    int i = paramInt - 1;
    if (this.thisRow.isNull(i)) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    Field field = this.fields[i];
    i = field.getSQLType();
    if (i != 12)
      if (i != 16) {
        switch (i) {
          default:
            switch (i) {
              default:
                switch (i) {
                  default:
                    return getString(paramInt);
                  case 93:
                    return getTimestamp(paramInt);
                  case 92:
                    return getTime(paramInt);
                  case 91:
                    break;
                } 
                return (field.getMysqlType() == 13 && !this.connection.getYearIsDateType()) ? Short.valueOf(getShort(paramInt)) : getDate(paramInt);
              case 7:
                return new Float(getFloat(paramInt));
              case 6:
              case 8:
                return new Double(getDouble(paramInt));
              case 5:
                return Integer.valueOf(getInt(paramInt));
              case 4:
                return (!field.isUnsigned() || field.getMysqlType() == 9) ? Integer.valueOf(getInt(paramInt)) : Long.valueOf(getLong(paramInt));
              case 2:
              case 3:
                str = getString(paramInt);
                if (str != null) {
                  if (str.length() == 0)
                    return new BigDecimal(0); 
                  try {
                    return new BigDecimal(str);
                  } catch (NumberFormatException numberFormatException) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { str, Integer.valueOf(paramInt) }), "S1009", getExceptionInterceptor());
                  } 
                } 
                return null;
              case 1:
                break;
            } 
            break;
          case -1:
            return !str.isOpaqueBinary() ? getStringForClob(paramInt) : getBytes(paramInt);
          case -4:
          case -3:
          case -2:
            return (str.getMysqlType() == 255) ? getBytes(paramInt) : getObjectDeserializingIfNeeded(paramInt);
          case -5:
            if (!str.isUnsigned())
              return Long.valueOf(getLong(paramInt)); 
            str = getString(paramInt);
            if (str == null)
              return null; 
            try {
              return new BigInteger(str);
            } catch (NumberFormatException numberFormatException) {
              throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigInteger", new Object[] { Integer.valueOf(paramInt), str }), "S1009", getExceptionInterceptor());
            } 
          case -6:
            return !str.isUnsigned() ? Integer.valueOf(getByte(paramInt)) : Integer.valueOf(getInt(paramInt));
          case -7:
            return (str.getMysqlType() == 16 && !str.isSingleBit()) ? getObjectDeserializingIfNeeded(paramInt) : Boolean.valueOf(getBoolean(paramInt));
        } 
      } else {
        return Boolean.valueOf(getBoolean(paramInt));
      }  
    return !str.isOpaqueBinary() ? getString(paramInt) : getBytes(paramInt);
  }
  
  public <T> T getObject(int paramInt, Class<T> paramClass) throws SQLException {
    if (paramClass != null) {
      SQLException sQLException;
      if (paramClass.equals(String.class))
        return (T)getString(paramInt); 
      if (paramClass.equals(BigDecimal.class))
        return (T)getBigDecimal(paramInt); 
      if (paramClass.equals(Boolean.class) || paramClass.equals(boolean.class))
        return (T)Boolean.valueOf(getBoolean(paramInt)); 
      if (paramClass.equals(Integer.class) || paramClass.equals(int.class))
        return (T)Integer.valueOf(getInt(paramInt)); 
      if (paramClass.equals(Long.class) || paramClass.equals(long.class))
        return (T)Long.valueOf(getLong(paramInt)); 
      if (paramClass.equals(Float.class) || paramClass.equals(float.class))
        return (T)Float.valueOf(getFloat(paramInt)); 
      if (paramClass.equals(Double.class) || paramClass.equals(double.class))
        return (T)Double.valueOf(getDouble(paramInt)); 
      if (paramClass.equals(byte[].class))
        return (T)getBytes(paramInt); 
      if (paramClass.equals(Date.class))
        return (T)getDate(paramInt); 
      if (paramClass.equals(Time.class))
        return (T)getTime(paramInt); 
      if (paramClass.equals(Timestamp.class))
        return (T)getTimestamp(paramInt); 
      if (paramClass.equals(Clob.class))
        return (T)getClob(paramInt); 
      if (paramClass.equals(Blob.class))
        return (T)getBlob(paramInt); 
      if (paramClass.equals(Array.class))
        return (T)getArray(paramInt); 
      if (paramClass.equals(Ref.class))
        return (T)getRef(paramInt); 
      if (paramClass.equals(URL.class))
        return (T)getURL(paramInt); 
      if (this.connection.getAutoDeserialize())
        try {
          return paramClass.cast(getObject(paramInt));
        } catch (ClassCastException classCastException) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Conversion not supported for type ");
          stringBuilder1.append(paramClass.getName());
          sQLException = SQLError.createSQLException(stringBuilder1.toString(), "S1009", getExceptionInterceptor());
          sQLException.initCause(classCastException);
          throw sQLException;
        }  
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Conversion not supported for type ");
      stringBuilder.append(sQLException.getName());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
    throw SQLError.createSQLException("Type parameter can not be null", "S1009", getExceptionInterceptor());
  }
  
  public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
    return getObject(paramInt);
  }
  
  public Object getObject(String paramString) throws SQLException {
    return getObject(findColumn(paramString));
  }
  
  public <T> T getObject(String paramString, Class<T> paramClass) throws SQLException {
    return getObject(findColumn(paramString), paramClass);
  }
  
  public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
    return getObject(findColumn(paramString), paramMap);
  }
  
  public Object getObjectStoredProc(int paramInt1, int paramInt2) throws SQLException {
    checkRowPos();
    checkColumnBounds(paramInt1);
    ResultSetRow resultSetRow = this.thisRow;
    int i = paramInt1 - 1;
    if (resultSetRow.getColumnValue(i) == null) {
      this.wasNullFlag = true;
      return null;
    } 
    this.wasNullFlag = false;
    Field field = this.fields[i];
    if (paramInt2 != 12) {
      if (paramInt2 != 16) {
        String str;
        switch (paramInt2) {
          default:
            switch (paramInt2) {
              default:
                switch (paramInt2) {
                  default:
                    return getString(paramInt1);
                  case 93:
                    return getTimestamp(paramInt1);
                  case 92:
                    return getTime(paramInt1);
                  case 91:
                    break;
                } 
                return (field.getMysqlType() == 13 && !this.connection.getYearIsDateType()) ? Short.valueOf(getShort(paramInt1)) : getDate(paramInt1);
              case 8:
                return new Double(getDouble(paramInt1));
              case 7:
                return new Float(getFloat(paramInt1));
              case 6:
                return !this.connection.getRunningCTS13() ? new Double(getFloat(paramInt1)) : new Float(getFloat(paramInt1));
              case 5:
                return Integer.valueOf(getInt(paramInt1));
              case 4:
                return (!field.isUnsigned() || field.getMysqlType() == 9) ? Integer.valueOf(getInt(paramInt1)) : Long.valueOf(getLong(paramInt1));
              case 2:
              case 3:
                str = getString(paramInt1);
                if (str != null) {
                  if (str.length() == 0)
                    return new BigDecimal(0); 
                  try {
                    return new BigDecimal(str);
                  } catch (NumberFormatException numberFormatException) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { str, Integer.valueOf(paramInt1) }), "S1009", getExceptionInterceptor());
                  } 
                } 
                return null;
              case 1:
                break;
            } 
            return getString(paramInt1);
          case -1:
            return getStringForClob(paramInt1);
          case -4:
          case -3:
          case -2:
            return getBytes(paramInt1);
          case -5:
            return str.isUnsigned() ? getBigDecimal(paramInt1) : Long.valueOf(getLong(paramInt1));
          case -6:
            return Integer.valueOf(getInt(paramInt1));
          case -7:
            break;
        } 
      } 
      return Boolean.valueOf(getBoolean(paramInt1));
    } 
    return getString(paramInt1);
  }
  
  public Object getObjectStoredProc(int paramInt1, Map<Object, Object> paramMap, int paramInt2) throws SQLException {
    return getObjectStoredProc(paramInt1, paramInt2);
  }
  
  public Object getObjectStoredProc(String paramString, int paramInt) throws SQLException {
    return getObjectStoredProc(findColumn(paramString), paramInt);
  }
  
  public Object getObjectStoredProc(String paramString, Map<Object, Object> paramMap, int paramInt) throws SQLException {
    return getObjectStoredProc(findColumn(paramString), paramMap, paramInt);
  }
  
  public Ref getRef(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public Ref getRef(String paramString) throws SQLException {
    return getRef(findColumn(paramString));
  }
  
  public int getRow() throws SQLException {
    checkClosed();
    int i = this.rowData.getCurrentRowNumber();
    if (!this.rowData.isDynamic() && (i < 0 || this.rowData.isAfterLast() || this.rowData.isEmpty())) {
      i = 0;
    } else {
      i++;
    } 
    return i;
  }
  
  public String getServerInfo() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        return this.serverInfo;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public short getShort(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkRowPos : ()V
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual checkColumnBounds : (I)V
    //   9: aload_0
    //   10: getfield isBinaryEncoded : Z
    //   13: ifne -> 450
    //   16: aload_0
    //   17: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   20: astore #9
    //   22: iload_1
    //   23: iconst_1
    //   24: isub
    //   25: istore_3
    //   26: aload #9
    //   28: iload_3
    //   29: invokevirtual isNull : (I)Z
    //   32: istore #6
    //   34: iconst_1
    //   35: istore #5
    //   37: iload #6
    //   39: ifeq -> 49
    //   42: aload_0
    //   43: iconst_1
    //   44: putfield wasNullFlag : Z
    //   47: iconst_0
    //   48: ireturn
    //   49: aload_0
    //   50: iconst_0
    //   51: putfield wasNullFlag : Z
    //   54: aload_0
    //   55: getfield fields : [Lcom/mysql/jdbc/Field;
    //   58: iload_3
    //   59: aaload
    //   60: invokevirtual getMysqlType : ()I
    //   63: bipush #16
    //   65: if_icmpne -> 118
    //   68: aload_0
    //   69: iload_1
    //   70: invokespecial getNumericRepresentationOfSQLBitType : (I)J
    //   73: lstore #7
    //   75: aload_0
    //   76: getfield jdbcCompliantTruncationForReads : Z
    //   79: ifeq -> 111
    //   82: lload #7
    //   84: ldc2_w -32768
    //   87: lcmp
    //   88: iflt -> 100
    //   91: lload #7
    //   93: ldc2_w 32767
    //   96: lcmp
    //   97: ifle -> 111
    //   100: aload_0
    //   101: lload #7
    //   103: invokestatic valueOf : (J)Ljava/lang/String;
    //   106: iload_1
    //   107: iconst_5
    //   108: invokespecial throwRangeException : (Ljava/lang/String;II)V
    //   111: lload #7
    //   113: l2i
    //   114: istore_1
    //   115: iload_1
    //   116: i2s
    //   117: ireturn
    //   118: aload_0
    //   119: getfield useFastIntParsing : Z
    //   122: istore #6
    //   124: aconst_null
    //   125: astore #9
    //   127: iload #6
    //   129: ifeq -> 295
    //   132: aload_0
    //   133: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   136: iload_3
    //   137: invokevirtual getColumnValue : (I)[B
    //   140: astore #10
    //   142: aload #10
    //   144: arraylength
    //   145: ifne -> 156
    //   148: aload_0
    //   149: invokespecial convertToZeroWithEmptyCheck : ()I
    //   152: istore_1
    //   153: goto -> 115
    //   156: iconst_0
    //   157: istore #4
    //   159: iload #4
    //   161: aload #10
    //   163: arraylength
    //   164: if_icmpge -> 204
    //   167: iload #5
    //   169: istore_3
    //   170: aload #10
    //   172: iload #4
    //   174: baload
    //   175: i2c
    //   176: bipush #101
    //   178: if_icmpeq -> 206
    //   181: aload #10
    //   183: iload #4
    //   185: baload
    //   186: i2c
    //   187: bipush #69
    //   189: if_icmpne -> 198
    //   192: iload #5
    //   194: istore_3
    //   195: goto -> 206
    //   198: iinc #4, 1
    //   201: goto -> 159
    //   204: iconst_0
    //   205: istore_3
    //   206: iload_3
    //   207: ifne -> 295
    //   210: aload_0
    //   211: iload_1
    //   212: aload #10
    //   214: aconst_null
    //   215: invokespecial parseShortWithOverflowCheck : (I[BLjava/lang/String;)S
    //   218: istore_2
    //   219: iload_2
    //   220: ireturn
    //   221: astore #9
    //   223: aload_0
    //   224: iload_1
    //   225: aload #10
    //   227: invokestatic toString : ([B)Ljava/lang/String;
    //   230: invokespecial parseShortAsDouble : (ILjava/lang/String;)S
    //   233: istore_2
    //   234: iload_2
    //   235: ireturn
    //   236: astore #9
    //   238: new java/lang/StringBuilder
    //   241: dup
    //   242: invokespecial <init> : ()V
    //   245: astore #9
    //   247: aload #9
    //   249: ldc_w 'ResultSet.Invalid_value_for_getShort()_-____96'
    //   252: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   255: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: pop
    //   259: aload #9
    //   261: aload #10
    //   263: invokestatic toString : ([B)Ljava/lang/String;
    //   266: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload #9
    //   272: ldc_w '''
    //   275: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload #9
    //   281: invokevirtual toString : ()Ljava/lang/String;
    //   284: ldc_w 'S1009'
    //   287: aload_0
    //   288: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   291: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   294: athrow
    //   295: aload_0
    //   296: iload_1
    //   297: invokevirtual getString : (I)Ljava/lang/String;
    //   300: astore #10
    //   302: aload #10
    //   304: ifnonnull -> 309
    //   307: iconst_0
    //   308: ireturn
    //   309: aload #10
    //   311: invokevirtual length : ()I
    //   314: ifne -> 323
    //   317: aload_0
    //   318: invokespecial convertToZeroWithEmptyCheck : ()I
    //   321: i2s
    //   322: ireturn
    //   323: aload #10
    //   325: ldc_w 'e'
    //   328: invokevirtual indexOf : (Ljava/lang/String;)I
    //   331: iconst_m1
    //   332: if_icmpne -> 368
    //   335: aload #10
    //   337: ldc_w 'E'
    //   340: invokevirtual indexOf : (Ljava/lang/String;)I
    //   343: iconst_m1
    //   344: if_icmpne -> 368
    //   347: aload #10
    //   349: ldc_w '.'
    //   352: invokevirtual indexOf : (Ljava/lang/String;)I
    //   355: iconst_m1
    //   356: if_icmpne -> 368
    //   359: aload_0
    //   360: iload_1
    //   361: aconst_null
    //   362: aload #10
    //   364: invokespecial parseShortWithOverflowCheck : (I[BLjava/lang/String;)S
    //   367: ireturn
    //   368: aload_0
    //   369: iload_1
    //   370: aload #10
    //   372: invokespecial parseShortAsDouble : (ILjava/lang/String;)S
    //   375: istore_2
    //   376: iload_2
    //   377: ireturn
    //   378: astore #9
    //   380: aload #10
    //   382: astore #9
    //   384: aload_0
    //   385: iload_1
    //   386: aload #9
    //   388: invokespecial parseShortAsDouble : (ILjava/lang/String;)S
    //   391: istore_2
    //   392: iload_2
    //   393: ireturn
    //   394: astore #10
    //   396: new java/lang/StringBuilder
    //   399: dup
    //   400: invokespecial <init> : ()V
    //   403: astore #10
    //   405: aload #10
    //   407: ldc_w 'ResultSet.Invalid_value_for_getShort()_-____96'
    //   410: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   413: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: pop
    //   417: aload #10
    //   419: aload #9
    //   421: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   424: pop
    //   425: aload #10
    //   427: ldc_w '''
    //   430: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   433: pop
    //   434: aload #10
    //   436: invokevirtual toString : ()Ljava/lang/String;
    //   439: ldc_w 'S1009'
    //   442: aload_0
    //   443: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   446: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   449: athrow
    //   450: aload_0
    //   451: iload_1
    //   452: invokevirtual getNativeShort : (I)S
    //   455: ireturn
    //   456: astore #10
    //   458: goto -> 384
    // Exception table:
    //   from	to	target	type
    //   210	219	221	java/lang/NumberFormatException
    //   223	234	236	java/lang/NumberFormatException
    //   295	302	456	java/lang/NumberFormatException
    //   309	323	378	java/lang/NumberFormatException
    //   323	368	378	java/lang/NumberFormatException
    //   368	376	378	java/lang/NumberFormatException
    //   384	392	394	java/lang/NumberFormatException
  }
  
  public short getShort(String paramString) throws SQLException {
    return getShort(findColumn(paramString));
  }
  
  public Statement getStatement() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        Statement statement = this.wrapperStatement;
        if (statement != null)
          return statement; 
        statement = this.owningStatement;
        return statement;
      } 
    } catch (SQLException sQLException) {
      if (this.retainOwningStatement) {
        Statement statement = this.wrapperStatement;
        return (statement != null) ? statement : this.owningStatement;
      } 
      throw SQLError.createSQLException("Operation not allowed on closed ResultSet. Statements can be retained over result set closure by setting the connection property \"retainStatementAfterResultSetClose\" to \"true\".", "S1000", getExceptionInterceptor());
    } 
  }
  
  public String getString(int paramInt) throws SQLException {
    String str2 = getStringInternal(paramInt, true);
    String str1 = str2;
    if (this.padCharsWithSpace) {
      str1 = str2;
      if (str2 != null) {
        Field field = this.fields[paramInt - 1];
        str1 = str2;
        if (field.getMysqlType() == 254) {
          int i = (int)field.getLength() / field.getMaxBytesPerCharacter();
          paramInt = str2.length();
          str1 = str2;
          if (paramInt < i) {
            StringBuilder stringBuilder = new StringBuilder(i);
            stringBuilder.append(str2);
            stringBuilder.append(EMPTY_SPACE, 0, i - paramInt);
            str1 = stringBuilder.toString();
          } 
        } 
      } 
    } 
    return str1;
  }
  
  public String getString(String paramString) throws SQLException {
    return getString(findColumn(paramString));
  }
  
  public String getStringInternal(int paramInt, boolean paramBoolean) throws SQLException {
    if (!this.isBinaryEncoded) {
      checkRowPos();
      checkColumnBounds(paramInt);
      if (this.fields != null) {
        byte[] arrayOfByte;
        Date date;
        int i = paramInt - 1;
        if (this.thisRow.isNull(i)) {
          this.wasNullFlag = true;
          return null;
        } 
        this.wasNullFlag = false;
        Field field = this.fields[i];
        if (field.getMysqlType() == 16) {
          if (field.isSingleBit()) {
            arrayOfByte = this.thisRow.getColumnValue(i);
            return (arrayOfByte.length == 0) ? String.valueOf(convertToZeroWithEmptyCheck()) : String.valueOf(arrayOfByte[0]);
          } 
          return String.valueOf(getNumericRepresentationOfSQLBitType(paramInt));
        } 
        String str = arrayOfByte.getEncoding();
        str = this.thisRow.getString(i, str, this.connection);
        if (arrayOfByte.getMysqlType() == 13) {
          if (!this.connection.getYearIsDateType())
            return str; 
          date = getDateFromString(str, paramInt, null);
          if (date == null) {
            this.wasNullFlag = true;
            return null;
          } 
          this.wasNullFlag = false;
          return date.toString();
        } 
        if (paramBoolean && !this.connection.getNoDatetimeStringSync()) {
          Timestamp timestamp;
          Time time;
          switch (date.getSQLType()) {
            default:
              return str;
            case 93:
              timestamp = getTimestampFromString(paramInt, null, str, getDefaultTimeZone(), false);
              if (timestamp == null) {
                this.wasNullFlag = true;
                return null;
              } 
              this.wasNullFlag = false;
              return timestamp.toString();
            case 92:
              time = getTimeFromString(str, null, paramInt, getDefaultTimeZone(), false);
              if (time == null) {
                this.wasNullFlag = true;
                return null;
              } 
              this.wasNullFlag = false;
              return time.toString();
            case 91:
              break;
          } 
          Date date1 = getDateFromString(str, paramInt, null);
          if (date1 == null) {
            this.wasNullFlag = true;
            return null;
          } 
          this.wasNullFlag = false;
          return date1.toString();
        } 
      } 
      throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_99"), "S1002", getExceptionInterceptor());
    } 
    return getNativeString(paramInt);
  }
  
  public Time getTime(int paramInt) throws SQLException {
    return getTimeInternal(paramInt, null, getDefaultTimeZone(), false);
  }
  
  public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
    TimeZone timeZone;
    if (paramCalendar != null) {
      timeZone = paramCalendar.getTimeZone();
    } else {
      timeZone = getDefaultTimeZone();
    } 
    return getTimeInternal(paramInt, paramCalendar, timeZone, true);
  }
  
  public Time getTime(String paramString) throws SQLException {
    return getTime(findColumn(paramString));
  }
  
  public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
    return getTime(findColumn(paramString), paramCalendar);
  }
  
  public Timestamp getTimestamp(int paramInt) throws SQLException {
    return getTimestampInternal(paramInt, null, getDefaultTimeZone(), false);
  }
  
  public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
    TimeZone timeZone;
    if (paramCalendar != null) {
      timeZone = paramCalendar.getTimeZone();
    } else {
      timeZone = getDefaultTimeZone();
    } 
    return getTimestampInternal(paramInt, paramCalendar, timeZone, true);
  }
  
  public Timestamp getTimestamp(String paramString) throws SQLException {
    return getTimestamp(findColumn(paramString));
  }
  
  public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
    return getTimestamp(findColumn(paramString), paramCalendar);
  }
  
  public int getType() throws SQLException {
    return this.resultSetType;
  }
  
  public URL getURL(int paramInt) throws SQLException {
    String str = getString(paramInt);
    if (str == null)
      return null; 
    try {
      return new URL(str);
    } catch (MalformedURLException malformedURLException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("ResultSet.Malformed_URL____104"));
      stringBuilder.append(str);
      stringBuilder.append("'");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public URL getURL(String paramString) throws SQLException {
    paramString = getString(paramString);
    if (paramString == null)
      return null; 
    try {
      return new URL(paramString);
    } catch (MalformedURLException malformedURLException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("ResultSet.Malformed_URL____107"));
      stringBuilder.append(paramString);
      stringBuilder.append("'");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  @Deprecated
  public InputStream getUnicodeStream(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      checkRowPos();
      return getBinaryStream(paramInt);
    } 
    return getNativeBinaryStream(paramInt);
  }
  
  @Deprecated
  public InputStream getUnicodeStream(String paramString) throws SQLException {
    return getUnicodeStream(findColumn(paramString));
  }
  
  public long getUpdateCount() {
    return this.updateCount;
  }
  
  public long getUpdateID() {
    return this.updateId;
  }
  
  public SQLWarning getWarnings() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.warningChain;
    } 
  }
  
  public void initializeFromCachedMetaData(CachedResultSetMetaData paramCachedResultSetMetaData) {
    this.fields = paramCachedResultSetMetaData.fields;
    this.columnLabelToIndex = paramCachedResultSetMetaData.columnNameToIndex;
    this.fullColumnNameToIndex = paramCachedResultSetMetaData.fullColumnNameToIndex;
    this.hasBuiltIndexMapping = true;
  }
  
  public void initializeWithMetadata() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.rowData.setMetadata(this.fields);
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this();
      this.columnToIndexCache = (Map)hashMap;
      if (this.profileSql || this.connection.getUseUsageAdvisor()) {
        this.columnUsed = new boolean[this.fields.length];
        Throwable throwable = new Throwable();
        this();
        this.pointOfOrigin = LogUtils.findCallingClassAndMethod(throwable);
        int i = resultCounter;
        resultCounter = i + 1;
        this.resultId = i;
        this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
        this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
      } 
      if (this.connection.getGatherPerformanceMetrics()) {
        this.connection.incrementNumberOfResultSetsCreated();
        HashSet<String> hashSet = new HashSet();
        this();
        byte b = 0;
        while (true) {
          Field[] arrayOfField = this.fields;
          if (b < arrayOfField.length) {
            Field field = arrayOfField[b];
            String str2 = field.getOriginalTableName();
            String str1 = str2;
            if (str2 == null)
              str1 = field.getTableName(); 
            if (str1 != null) {
              str2 = str1;
              if (this.connection.lowerCaseTableNames())
                str2 = str1.toLowerCase(); 
              hashSet.add(str2);
            } 
            b++;
            continue;
          } 
          this.connection.reportNumberOfTablesAccessed(hashSet.size());
          break;
        } 
      } 
      return;
    } 
  }
  
  public void insertRow() throws SQLException {
    throw new NotUpdatable();
  }
  
  public boolean isAfterLast() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.rowData.isAfterLast();
    } 
  }
  
  public boolean isBeforeFirst() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.rowData.isBeforeFirst();
    } 
  }
  
  public boolean isClosed() throws SQLException {
    return this.isClosed;
  }
  
  public boolean isFirst() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.rowData.isFirst();
    } 
  }
  
  public boolean isLast() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.rowData.isLast();
    } 
  }
  
  public boolean last() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_1
    //   13: istore_1
    //   14: aload_0
    //   15: getfield rowData : Lcom/mysql/jdbc/RowData;
    //   18: invokeinterface size : ()I
    //   23: ifne -> 31
    //   26: iconst_0
    //   27: istore_1
    //   28: goto -> 90
    //   31: aload_0
    //   32: getfield onInsertRow : Z
    //   35: ifeq -> 43
    //   38: aload_0
    //   39: iconst_0
    //   40: putfield onInsertRow : Z
    //   43: aload_0
    //   44: getfield doingUpdates : Z
    //   47: ifeq -> 55
    //   50: aload_0
    //   51: iconst_0
    //   52: putfield doingUpdates : Z
    //   55: aload_0
    //   56: getfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   59: astore_3
    //   60: aload_3
    //   61: ifnull -> 68
    //   64: aload_3
    //   65: invokevirtual closeOpenStreams : ()V
    //   68: aload_0
    //   69: getfield rowData : Lcom/mysql/jdbc/RowData;
    //   72: invokeinterface beforeLast : ()V
    //   77: aload_0
    //   78: aload_0
    //   79: getfield rowData : Lcom/mysql/jdbc/RowData;
    //   82: invokeinterface next : ()Lcom/mysql/jdbc/ResultSetRow;
    //   87: putfield thisRow : Lcom/mysql/jdbc/ResultSetRow;
    //   90: aload_0
    //   91: invokespecial setRowPositionValidity : ()V
    //   94: aload_2
    //   95: monitorexit
    //   96: iload_1
    //   97: ireturn
    //   98: astore_3
    //   99: aload_2
    //   100: monitorexit
    //   101: aload_3
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   14	26	98	finally
    //   31	43	98	finally
    //   43	55	98	finally
    //   55	60	98	finally
    //   64	68	98	finally
    //   68	90	98	finally
    //   90	96	98	finally
    //   99	101	98	finally
  }
  
  public void moveToCurrentRow() throws SQLException {
    throw new NotUpdatable();
  }
  
  public void moveToInsertRow() throws SQLException {
    throw new NotUpdatable();
  }
  
  public boolean next() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool1 = this.onInsertRow;
      boolean bool = false;
      if (bool1)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      if (reallyResult()) {
        ResultSetRow resultSetRow = this.thisRow;
        if (resultSetRow != null)
          resultSetRow.closeOpenStreams(); 
        if (this.rowData.size() != 0) {
          resultSetRow = this.rowData.next();
          this.thisRow = resultSetRow;
          if (resultSetRow != null) {
            clearWarnings();
            bool = true;
          } 
        } 
        setRowPositionValidity();
        return bool;
      } 
      throw SQLError.createSQLException(Messages.getString("ResultSet.ResultSet_is_from_UPDATE._No_Data_115"), "S1000", getExceptionInterceptor());
    } 
  }
  
  public void populateCachedMetaData(CachedResultSetMetaData paramCachedResultSetMetaData) throws SQLException {
    paramCachedResultSetMetaData.fields = this.fields;
    paramCachedResultSetMetaData.columnNameToIndex = this.columnLabelToIndex;
    paramCachedResultSetMetaData.fullColumnNameToIndex = this.fullColumnNameToIndex;
    paramCachedResultSetMetaData.metadata = getMetaData();
  }
  
  public boolean prev() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool1;
      int j = this.rowData.getCurrentRowNumber();
      ResultSetRow resultSetRow = this.thisRow;
      if (resultSetRow != null)
        resultSetRow.closeOpenStreams(); 
      int i = j - 1;
      boolean bool2 = false;
      if (i >= 0) {
        i = j - 1;
        this.rowData.setCurrentRow(i);
        this.thisRow = this.rowData.getAt(i);
        bool1 = true;
      } else {
        bool1 = bool2;
        if (i == -1) {
          this.rowData.setCurrentRow(j - 1);
          this.thisRow = null;
          bool1 = bool2;
        } 
      } 
      setRowPositionValidity();
      return bool1;
    } 
  }
  
  public boolean previous() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      return prev();
    } 
  }
  
  public void realClose(boolean paramBoolean) throws SQLException {
    null = this.connection;
    if (null == null)
      return; 
    synchronized (null.getConnectionMutex()) {
      if (this.isClosed)
        return; 
      try {
        if (this.useUsageAdvisor) {
          if (!paramBoolean) {
            int i;
            String str;
            ProfilerEventHandler profilerEventHandler = this.eventSink;
            ProfilerEvent profilerEvent = new ProfilerEvent();
            StatementImpl statementImpl1 = this.owningStatement;
            if (statementImpl1 == null) {
              str = "N/A";
            } else {
              str = statementImpl1.currentCatalog;
            } 
            long l = this.connectionId;
            if (statementImpl1 == null) {
              i = -1;
            } else {
              i = statementImpl1.getId();
            } 
            this((byte)0, "", str, l, i, this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.ResultSet_implicitly_closed_by_driver"));
            profilerEventHandler.consumeEvent(profilerEvent);
          } 
          RowData rowData = this.rowData;
          if (rowData instanceof RowDataStatic) {
            if (rowData.size() > this.connection.getResultSetSizeThreshold()) {
              int i;
              String str;
              ProfilerEventHandler profilerEventHandler = this.eventSink;
              ProfilerEvent profilerEvent = new ProfilerEvent();
              StatementImpl statementImpl1 = this.owningStatement;
              if (statementImpl1 == null) {
                str = Messages.getString("ResultSet.N/A_159");
              } else {
                str = ((StatementImpl)str).currentCatalog;
              } 
              long l = this.connectionId;
              StatementImpl statementImpl2 = this.owningStatement;
              if (statementImpl2 == null) {
                i = -1;
              } else {
                i = statementImpl2.getId();
              } 
              this((byte)0, "", str, l, i, this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Too_Large_Result_Set", new Object[] { Integer.valueOf(this.rowData.size()), Integer.valueOf(this.connection.getResultSetSizeThreshold()) }));
              profilerEventHandler.consumeEvent(profilerEvent);
            } 
            if (!isLast() && !isAfterLast() && this.rowData.size() != 0) {
              int i;
              String str;
              ProfilerEventHandler profilerEventHandler = this.eventSink;
              ProfilerEvent profilerEvent = new ProfilerEvent();
              StatementImpl statementImpl1 = this.owningStatement;
              if (statementImpl1 == null) {
                str = Messages.getString("ResultSet.N/A_159");
              } else {
                str = ((StatementImpl)str).currentCatalog;
              } 
              long l = this.connectionId;
              StatementImpl statementImpl2 = this.owningStatement;
              if (statementImpl2 == null) {
                i = -1;
              } else {
                i = statementImpl2.getId();
              } 
              this((byte)0, "", str, l, i, this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Possible_incomplete_traversal_of_result_set", new Object[] { Integer.valueOf(getRow()), Integer.valueOf(this.rowData.size()) }));
              profilerEventHandler.consumeEvent(profilerEvent);
            } 
          } 
          if (this.columnUsed.length > 0 && !this.rowData.wasEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            this(Messages.getString("ResultSet.The_following_columns_were_never_referenced"));
            int i = 0;
            byte b = 0;
            while (true) {
              boolean[] arrayOfBoolean = this.columnUsed;
              if (b < arrayOfBoolean.length) {
                int j = i;
                if (!arrayOfBoolean[b]) {
                  if (!i) {
                    i = 1;
                  } else {
                    stringBuilder.append(", ");
                  } 
                  stringBuilder.append(this.fields[b].getFullName());
                  j = i;
                } 
                b++;
                i = j;
                continue;
              } 
              if (i) {
                String str;
                ProfilerEventHandler profilerEventHandler = this.eventSink;
                ProfilerEvent profilerEvent = new ProfilerEvent();
                StatementImpl statementImpl1 = this.owningStatement;
                if (statementImpl1 == null) {
                  str = "N/A";
                } else {
                  str = statementImpl1.currentCatalog;
                } 
                long l = this.connectionId;
                if (statementImpl1 == null) {
                  i = -1;
                } else {
                  i = statementImpl1.getId();
                } 
                this((byte)0, "", str, l, i, 0, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, stringBuilder.toString());
                profilerEventHandler.consumeEvent(profilerEvent);
              } 
              break;
            } 
          } 
        } 
        StatementImpl statementImpl = this.owningStatement;
        if (statementImpl != null && paramBoolean)
          statementImpl.removeOpenResultSet(this); 
        RowData rowData1 = this.rowData;
        if (rowData1 != null)
          try {
            rowData1.close();
          } catch (SQLException sQLException) {} 
        rowData1 = null;
        PreparedStatement preparedStatement = this.statementUsedForFetchingRows;
        RowData rowData2 = rowData1;
        if (preparedStatement != null)
          try {
            preparedStatement.realClose(true, false);
            rowData2 = rowData1;
          } catch (SQLException sQLException) {
            if (rowData1 != null) {
              rowData1.setNextException(sQLException);
              rowData2 = rowData1;
            } 
          }  
        this.rowData = null;
        this.fields = null;
        this.columnLabelToIndex = null;
        this.fullColumnNameToIndex = null;
        this.columnToIndexCache = null;
        this.eventSink = null;
        this.warningChain = null;
        if (!this.retainOwningStatement)
          this.owningStatement = null; 
        this.catalog = null;
        this.serverInfo = null;
        this.thisRow = null;
        this.fastDefaultCal = null;
        this.fastClientCal = null;
        this.connection = null;
        this.isClosed = true;
        if (rowData2 == null)
          return; 
        throw rowData2;
      } finally {
        StatementImpl statementImpl = this.owningStatement;
        if (statementImpl != null && paramBoolean)
          statementImpl.removeOpenResultSet(this); 
        RowData rowData1 = this.rowData;
        if (rowData1 != null)
          try {
            rowData1.close();
          } catch (SQLException sQLException) {} 
        rowData1 = null;
        PreparedStatement preparedStatement = this.statementUsedForFetchingRows;
        RowData rowData2 = rowData1;
        if (preparedStatement != null)
          try {
            preparedStatement.realClose(true, false);
            rowData2 = rowData1;
          } catch (SQLException sQLException) {
            if (rowData1 != null) {
              rowData1.setNextException(sQLException);
              rowData2 = rowData1;
            } 
          }  
        this.rowData = null;
        this.fields = null;
        this.columnLabelToIndex = null;
        this.fullColumnNameToIndex = null;
        this.columnToIndexCache = null;
        this.eventSink = null;
        this.warningChain = null;
        if (!this.retainOwningStatement)
          this.owningStatement = null; 
        this.catalog = null;
        this.serverInfo = null;
        this.thisRow = null;
        this.fastDefaultCal = null;
        this.fastClientCal = null;
        this.connection = null;
        this.isClosed = true;
        if (rowData2 != null)
          throw rowData2; 
      } 
    } 
  }
  
  public boolean reallyResult() {
    return (this.rowData != null) ? true : this.reallyResult;
  }
  
  public void redefineFieldsForDBMD(Field[] paramArrayOfField) {
    this.fields = paramArrayOfField;
    byte b = 0;
    while (true) {
      paramArrayOfField = this.fields;
      if (b < paramArrayOfField.length) {
        paramArrayOfField[b].setUseOldNameMetadata(true);
        this.fields[b].setConnection(this.connection);
        b++;
        continue;
      } 
      break;
    } 
  }
  
  public void refreshRow() throws SQLException {
    throw new NotUpdatable();
  }
  
  public boolean relative(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      int i = this.rowData.size();
      boolean bool2 = false;
      if (i == 0) {
        setRowPositionValidity();
        return false;
      } 
      ResultSetRow resultSetRow = this.thisRow;
      if (resultSetRow != null)
        resultSetRow.closeOpenStreams(); 
      this.rowData.moveRowRelative(paramInt);
      RowData rowData = this.rowData;
      this.thisRow = rowData.getAt(rowData.getCurrentRowNumber());
      setRowPositionValidity();
      boolean bool1 = bool2;
      if (!this.rowData.isAfterLast()) {
        bool1 = bool2;
        if (!this.rowData.isBeforeFirst())
          bool1 = true; 
      } 
      return bool1;
    } 
  }
  
  public boolean rowDeleted() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public boolean rowInserted() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public boolean rowUpdated() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void setBinaryEncoded() {
    this.isBinaryEncoded = true;
  }
  
  public void setFetchDirection(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: sipush #1000
    //   16: if_icmpeq -> 53
    //   19: iload_1
    //   20: sipush #1001
    //   23: if_icmpeq -> 53
    //   26: iload_1
    //   27: sipush #1002
    //   30: if_icmpne -> 36
    //   33: goto -> 53
    //   36: ldc_w 'ResultSet.Illegal_value_for_fetch_direction_64'
    //   39: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   42: ldc_w 'S1009'
    //   45: aload_0
    //   46: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   49: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   52: athrow
    //   53: aload_0
    //   54: iload_1
    //   55: putfield fetchDirection : I
    //   58: aload_3
    //   59: monitorexit
    //   60: return
    //   61: astore_2
    //   62: aload_3
    //   63: monitorexit
    //   64: aload_2
    //   65: athrow
    // Exception table:
    //   from	to	target	type
    //   36	53	61	finally
    //   53	60	61	finally
    //   62	64	61	finally
  }
  
  public void setFetchSize(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: iflt -> 28
    //   16: aload_0
    //   17: iload_1
    //   18: putfield fetchSize : I
    //   21: aload_3
    //   22: monitorexit
    //   23: return
    //   24: astore_2
    //   25: goto -> 45
    //   28: ldc_w 'ResultSet.Value_must_be_between_0_and_getMaxRows()_66'
    //   31: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   34: ldc_w 'S1009'
    //   37: aload_0
    //   38: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   41: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   44: athrow
    //   45: aload_3
    //   46: monitorexit
    //   47: aload_2
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   16	23	24	finally
    //   28	45	24	finally
    //   45	47	24	finally
  }
  
  public void setFirstCharOfQuery(char paramChar) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.firstCharOfQuery = paramChar;
        return;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public void setNextResultSet(ResultSetInternalMethods paramResultSetInternalMethods) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield nextResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void setOwningStatement(StatementImpl paramStatementImpl) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.owningStatement = paramStatementImpl;
        return;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public void setResultSetConcurrency(int paramInt) {
    Exception exception;
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.resultSetConcurrency = paramInt;
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
        return;
      } 
    } catch (SQLException null) {
      RuntimeException runtimeException = new RuntimeException();
      this(exception);
      throw runtimeException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    throw exception;
  }
  
  public void setResultSetType(int paramInt) {
    Exception exception;
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.resultSetType = paramInt;
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
        return;
      } 
    } catch (SQLException null) {
      RuntimeException runtimeException = new RuntimeException();
      this(exception);
      throw runtimeException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    throw exception;
  }
  
  public void setServerInfo(String paramString) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.serverInfo = paramString;
        return;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public void setStatementUsedForFetchingRows(PreparedStatement paramPreparedStatement) {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.statementUsedForFetchingRows = paramPreparedStatement;
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
        return;
      } 
    } catch (SQLException sQLException) {
      RuntimeException runtimeException = new RuntimeException();
      this(sQLException);
      throw runtimeException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    throw paramPreparedStatement;
  }
  
  public void setWrapperStatement(Statement paramStatement) {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.wrapperStatement = paramStatement;
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
        return;
      } 
    } catch (SQLException sQLException) {
      RuntimeException runtimeException = new RuntimeException();
      this(sQLException);
      throw runtimeException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/ResultSetImpl}} */
    throw paramStatement;
  }
  
  public String toString() {
    if (this.reallyResult)
      return super.toString(); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Result set representing update count of ");
    stringBuilder.append(this.updateCount);
    return stringBuilder.toString();
  }
  
  public void updateArray(int paramInt, Array paramArray) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void updateArray(String paramString, Array paramArray) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    updateAsciiStream(findColumn(paramString), paramInputStream, paramInt);
  }
  
  public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
    updateBigDecimal(findColumn(paramString), paramBigDecimal);
  }
  
  public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    updateBinaryStream(findColumn(paramString), paramInputStream, paramInt);
  }
  
  public void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBlob(String paramString, Blob paramBlob) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBoolean(String paramString, boolean paramBoolean) throws SQLException {
    updateBoolean(findColumn(paramString), paramBoolean);
  }
  
  public void updateByte(int paramInt, byte paramByte) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateByte(String paramString, byte paramByte) throws SQLException {
    updateByte(findColumn(paramString), paramByte);
  }
  
  public void updateBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
    updateBytes(findColumn(paramString), paramArrayOfbyte);
  }
  
  public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
    updateCharacterStream(findColumn(paramString), paramReader, paramInt);
  }
  
  public void updateClob(int paramInt, Clob paramClob) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void updateClob(String paramString, Clob paramClob) throws SQLException {
    updateClob(findColumn(paramString), paramClob);
  }
  
  public void updateDate(int paramInt, Date paramDate) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateDate(String paramString, Date paramDate) throws SQLException {
    updateDate(findColumn(paramString), paramDate);
  }
  
  public void updateDouble(int paramInt, double paramDouble) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateDouble(String paramString, double paramDouble) throws SQLException {
    updateDouble(findColumn(paramString), paramDouble);
  }
  
  public void updateFloat(int paramInt, float paramFloat) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateFloat(String paramString, float paramFloat) throws SQLException {
    updateFloat(findColumn(paramString), paramFloat);
  }
  
  public void updateInt(int paramInt1, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateInt(String paramString, int paramInt) throws SQLException {
    updateInt(findColumn(paramString), paramInt);
  }
  
  public void updateLong(int paramInt, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateLong(String paramString, long paramLong) throws SQLException {
    updateLong(findColumn(paramString), paramLong);
  }
  
  public void updateNull(int paramInt) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNull(String paramString) throws SQLException {
    updateNull(findColumn(paramString));
  }
  
  public void updateObject(int paramInt, Object paramObject) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateObject(String paramString, Object paramObject) throws SQLException {
    updateObject(findColumn(paramString), paramObject);
  }
  
  public void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
    updateObject(findColumn(paramString), paramObject);
  }
  
  public void updateRef(int paramInt, Ref paramRef) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void updateRef(String paramString, Ref paramRef) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void updateRow() throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateShort(int paramInt, short paramShort) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateShort(String paramString, short paramShort) throws SQLException {
    updateShort(findColumn(paramString), paramShort);
  }
  
  public void updateString(int paramInt, String paramString) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateString(String paramString1, String paramString2) throws SQLException {
    updateString(findColumn(paramString1), paramString2);
  }
  
  public void updateTime(int paramInt, Time paramTime) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateTime(String paramString, Time paramTime) throws SQLException {
    updateTime(findColumn(paramString), paramTime);
  }
  
  public void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
    updateTimestamp(findColumn(paramString), paramTimestamp);
  }
  
  public boolean wasNull() throws SQLException {
    return this.wasNullFlag;
  }
  
  static {
    boolean bool = Util.isJdbc4();
    byte b = 0;
    if (bool) {
      try {
        String str;
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.JDBC42ResultSet";
        } else {
          str = "com.mysql.jdbc.JDBC4ResultSet";
        } 
        Class<?> clazz = Class.forName(str);
        Class<long> clazz1 = long.class;
        JDBC_4_RS_4_ARG_CTOR = clazz.getConstructor(new Class[] { clazz1, clazz1, MySQLConnection.class, StatementImpl.class });
        JDBC_4_RS_5_ARG_CTOR = Class.forName(str).getConstructor(new Class[] { String.class, Field[].class, RowData.class, MySQLConnection.class, StatementImpl.class });
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.JDBC42UpdatableResultSet";
        } else {
          str = "com.mysql.jdbc.JDBC4UpdatableResultSet";
        } 
        JDBC_4_UPD_RS_5_ARG_CTOR = Class.forName(str).getConstructor(new Class[] { String.class, Field[].class, RowData.class, MySQLConnection.class, StatementImpl.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_RS_4_ARG_CTOR = null;
      JDBC_4_RS_5_ARG_CTOR = null;
      JDBC_4_UPD_RS_5_ARG_CTOR = null;
    } 
  }
}
