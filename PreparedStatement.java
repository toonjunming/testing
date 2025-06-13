package com.mysql.jdbc;

import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class PreparedStatement extends StatementImpl implements PreparedStatement {
  private static final byte[] HEX_DIGITS = new byte[] { 
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
      65, 66, 67, 68, 69, 70 };
  
  private static final Constructor<?> JDBC_4_PSTMT_2_ARG_CTOR;
  
  private static final Constructor<?> JDBC_4_PSTMT_3_ARG_CTOR;
  
  private static final Constructor<?> JDBC_4_PSTMT_4_ARG_CTOR;
  
  public int batchCommandIndex = -1;
  
  public boolean batchHasPlainStatements = false;
  
  public String batchedValuesClause;
  
  private CharsetEncoder charsetEncoder;
  
  private boolean compensateForOnDuplicateKeyUpdate = false;
  
  private DatabaseMetaData dbmd = null;
  
  private SimpleDateFormat ddf;
  
  private boolean doPingInstead;
  
  public char firstCharOfStmt = Character.MIN_VALUE;
  
  public boolean isLoadDataQuery = false;
  
  public boolean[] isNull = null;
  
  private boolean[] isStream = null;
  
  public int numberOfExecutions = 0;
  
  public String originalSql = null;
  
  public int parameterCount;
  
  public MysqlParameterMetadata parameterMetaData;
  
  private InputStream[] parameterStreams = null;
  
  public int[] parameterTypes = null;
  
  private byte[][] parameterValues = null;
  
  public ParseInfo parseInfo;
  
  private ResultSetMetaData pstmtResultMetaData;
  
  public int rewrittenBatchSize = 0;
  
  public boolean serverSupportsFracSecs;
  
  private byte[][] staticSqlStrings = null;
  
  private byte[] streamConvertBuf = null;
  
  private int[] streamLengths = null;
  
  private SimpleDateFormat tdf;
  
  private SimpleDateFormat tsdf = null;
  
  public boolean useTrueBoolean = false;
  
  public boolean usingAnsiMode;
  
  public PreparedStatement(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    super(paramMySQLConnection, paramString);
    detectFractionalSecondsSupport();
    this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
  }
  
  public PreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2) throws SQLException {
    super(paramMySQLConnection, paramString2);
    if (paramString1 != null) {
      detectFractionalSecondsSupport();
      this.originalSql = paramString1;
      this.doPingInstead = paramString1.startsWith("/* ping */");
      this.dbmd = this.connection.getMetaData();
      this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
      this.parseInfo = new ParseInfo(paramString1, this.connection, this.dbmd, this.charEncoding, this.charConverter);
      initializeFromParseInfo();
      this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
      if (paramMySQLConnection.getRequiresEscapingEncoder())
        this.charsetEncoder = Charset.forName(paramMySQLConnection.getEncoding()).newEncoder(); 
      return;
    } 
    throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009", getExceptionInterceptor());
  }
  
  public PreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, ParseInfo paramParseInfo) throws SQLException {
    super(paramMySQLConnection, paramString2);
    if (paramString1 != null) {
      detectFractionalSecondsSupport();
      this.originalSql = paramString1;
      this.dbmd = this.connection.getMetaData();
      this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
      this.parseInfo = paramParseInfo;
      this.usingAnsiMode = this.connection.useAnsiQuotedIdentifiers() ^ true;
      initializeFromParseInfo();
      this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
      if (paramMySQLConnection.getRequiresEscapingEncoder())
        this.charsetEncoder = Charset.forName(paramMySQLConnection.getEncoding()).newEncoder(); 
      return;
    } 
    throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009", getExceptionInterceptor());
  }
  
  public static boolean canRewrite(String paramString, boolean paramBoolean, int paramInt1, int paramInt2) {
    boolean bool = StringUtils.startsWithIgnoreCaseAndWs(paramString, "INSERT", paramInt2);
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool) {
      Set<StringUtils.SearchMode> set = StringUtils.SEARCH_MODE__MRK_COM_WS;
      if (StringUtils.indexOfIgnoreCase(paramInt2, paramString, "SELECT", "\"'`", "\"'`", set) != -1)
        return false; 
      bool1 = bool2;
      if (paramBoolean) {
        paramInt1 = StringUtils.indexOfIgnoreCase(paramInt1, paramString, " UPDATE ");
        bool1 = bool2;
        if (paramInt1 != -1)
          if (StringUtils.indexOfIgnoreCase(paramInt1, paramString, "LAST_INSERT_ID", "\"'`", "\"'`", set) == -1) {
            bool1 = bool2;
          } else {
            bool1 = false;
          }  
      } 
      return bool1;
    } 
    if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "REPLACE", paramInt2) && StringUtils.indexOfIgnoreCase(paramInt2, paramString, "SELECT", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) == -1) {
      paramBoolean = bool1;
    } else {
      paramBoolean = false;
    } 
    return paramBoolean;
  }
  
  private void checkAllParametersSet(byte[] paramArrayOfbyte, InputStream paramInputStream, int paramInt) throws SQLException {
    if (paramArrayOfbyte != null || paramInputStream != null)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Messages.getString("PreparedStatement.40"));
    stringBuilder.append(paramInt + 1);
    throw SQLError.createSQLException(stringBuilder.toString(), "07001", getExceptionInterceptor());
  }
  
  private void doSSPSCompatibleTimezoneShift(int paramInt1, Timestamp paramTimestamp, int paramInt2, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Calendar calendar;
      if (this.connection.getUseJDBCCompliantTimezoneShift()) {
        calendar = this.connection.getUtcCalendar();
      } else {
        calendar = getCalendarInstanceForSessionOrNew();
      } 
      synchronized (TimeUtil.setProlepticIfNeeded(calendar, paramCalendar)) {
        Date date = paramCalendar.getTime();
        try {
          paramCalendar.setTime(paramTimestamp);
          int i = paramCalendar.get(1);
          int i1 = paramCalendar.get(2) + 1;
          int k = paramCalendar.get(5);
          int n = paramCalendar.get(11);
          int m = paramCalendar.get(12);
          int j = paramCalendar.get(13);
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append('\'');
          stringBuilder.append(i);
          stringBuilder.append("-");
          if (i1 < 10)
            stringBuilder.append('0'); 
          stringBuilder.append(i1);
          stringBuilder.append('-');
          if (k < 10)
            stringBuilder.append('0'); 
          stringBuilder.append(k);
          stringBuilder.append(' ');
          if (n < 10)
            stringBuilder.append('0'); 
          stringBuilder.append(n);
          stringBuilder.append(':');
          if (m < 10)
            stringBuilder.append('0'); 
          stringBuilder.append(m);
          stringBuilder.append(':');
          if (j < 10)
            stringBuilder.append('0'); 
          stringBuilder.append(j);
          stringBuilder.append('.');
          stringBuilder.append(TimeUtil.formatNanos(paramTimestamp.getNanos(), this.serverSupportsFracSecs, paramInt2));
          stringBuilder.append('\'');
          setInternal(paramInt1, stringBuilder.toString());
          return;
        } finally {
          paramCalendar.setTime(date);
        } 
      } 
    } 
  }
  
  private final void escapeblockFast(byte[] paramArrayOfbyte, Buffer paramBuffer, int paramInt) throws SQLException {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iconst_0
    //   4: istore #6
    //   6: iload #4
    //   8: iload_3
    //   9: if_icmpge -> 138
    //   12: aload_1
    //   13: iload #4
    //   15: baload
    //   16: istore #7
    //   18: iload #7
    //   20: ifne -> 63
    //   23: iload #4
    //   25: iload #6
    //   27: if_icmple -> 42
    //   30: aload_2
    //   31: aload_1
    //   32: iload #6
    //   34: iload #4
    //   36: iload #6
    //   38: isub
    //   39: invokevirtual writeBytesNoNull : ([BII)V
    //   42: aload_2
    //   43: bipush #92
    //   45: invokevirtual writeByte : (B)V
    //   48: aload_2
    //   49: bipush #48
    //   51: invokevirtual writeByte : (B)V
    //   54: iload #4
    //   56: iconst_1
    //   57: iadd
    //   58: istore #5
    //   60: goto -> 128
    //   63: iload #7
    //   65: bipush #92
    //   67: if_icmpeq -> 99
    //   70: iload #7
    //   72: bipush #39
    //   74: if_icmpeq -> 99
    //   77: iload #6
    //   79: istore #5
    //   81: aload_0
    //   82: getfield usingAnsiMode : Z
    //   85: ifne -> 128
    //   88: iload #6
    //   90: istore #5
    //   92: iload #7
    //   94: bipush #34
    //   96: if_icmpne -> 128
    //   99: iload #4
    //   101: iload #6
    //   103: if_icmple -> 118
    //   106: aload_2
    //   107: aload_1
    //   108: iload #6
    //   110: iload #4
    //   112: iload #6
    //   114: isub
    //   115: invokevirtual writeBytesNoNull : ([BII)V
    //   118: aload_2
    //   119: bipush #92
    //   121: invokevirtual writeByte : (B)V
    //   124: iload #4
    //   126: istore #5
    //   128: iinc #4, 1
    //   131: iload #5
    //   133: istore #6
    //   135: goto -> 6
    //   138: iload #6
    //   140: iload_3
    //   141: if_icmpge -> 155
    //   144: aload_2
    //   145: aload_1
    //   146: iload #6
    //   148: iload_3
    //   149: iload #6
    //   151: isub
    //   152: invokevirtual writeBytesNoNull : ([BII)V
    //   155: return
  }
  
  private final void escapeblockFast(byte[] paramArrayOfbyte, ByteArrayOutputStream paramByteArrayOutputStream, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iconst_0
    //   4: istore #6
    //   6: iload #4
    //   8: iload_3
    //   9: if_icmpge -> 138
    //   12: aload_1
    //   13: iload #4
    //   15: baload
    //   16: istore #7
    //   18: iload #7
    //   20: ifne -> 63
    //   23: iload #4
    //   25: iload #6
    //   27: if_icmple -> 42
    //   30: aload_2
    //   31: aload_1
    //   32: iload #6
    //   34: iload #4
    //   36: iload #6
    //   38: isub
    //   39: invokevirtual write : ([BII)V
    //   42: aload_2
    //   43: bipush #92
    //   45: invokevirtual write : (I)V
    //   48: aload_2
    //   49: bipush #48
    //   51: invokevirtual write : (I)V
    //   54: iload #4
    //   56: iconst_1
    //   57: iadd
    //   58: istore #5
    //   60: goto -> 128
    //   63: iload #7
    //   65: bipush #92
    //   67: if_icmpeq -> 99
    //   70: iload #7
    //   72: bipush #39
    //   74: if_icmpeq -> 99
    //   77: iload #6
    //   79: istore #5
    //   81: aload_0
    //   82: getfield usingAnsiMode : Z
    //   85: ifne -> 128
    //   88: iload #6
    //   90: istore #5
    //   92: iload #7
    //   94: bipush #34
    //   96: if_icmpne -> 128
    //   99: iload #4
    //   101: iload #6
    //   103: if_icmple -> 118
    //   106: aload_2
    //   107: aload_1
    //   108: iload #6
    //   110: iload #4
    //   112: iload #6
    //   114: isub
    //   115: invokevirtual write : ([BII)V
    //   118: aload_2
    //   119: bipush #92
    //   121: invokevirtual write : (I)V
    //   124: iload #4
    //   126: istore #5
    //   128: iinc #4, 1
    //   131: iload #5
    //   133: istore #6
    //   135: goto -> 6
    //   138: iload #6
    //   140: iload_3
    //   141: if_icmpge -> 155
    //   144: aload_2
    //   145: aload_1
    //   146: iload #6
    //   148: iload_3
    //   149: iload #6
    //   151: isub
    //   152: invokevirtual write : ([BII)V
    //   155: return
  }
  
  private String generateMultiStatementForBatch(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      StringBuilder stringBuilder = new StringBuilder();
      this((this.originalSql.length() + 1) * paramInt);
      stringBuilder.append(this.originalSql);
      for (byte b = 0; b < paramInt - 1; b++) {
        stringBuilder.append(';');
        stringBuilder.append(this.originalSql);
      } 
      return stringBuilder.toString();
    } 
  }
  
  private final String getDateTimePattern(String paramString, boolean paramBoolean) throws Exception {
    int i;
    Integer integer = Integer.valueOf(0);
    if (paramString != null) {
      i = paramString.length();
    } else {
      i = 0;
    } 
    if (i >= 8 && i <= 10) {
      boolean bool;
      byte b = 0;
      int k = 0;
      while (true) {
        if (b < i) {
          char c = paramString.charAt(b);
          if (!Character.isDigit(c) && c != '-') {
            boolean bool1 = false;
            break;
          } 
          int m = k;
          if (c == '-')
            m = k + 1; 
          b++;
          k = m;
          continue;
        } 
        bool = true;
        break;
      } 
      if (bool && k == 2)
        return "yyyy-MM-dd"; 
    } 
    int j = 0;
    while (true) {
      if (j < i) {
        char c = paramString.charAt(j);
        if (!Character.isDigit(c) && c != ':') {
          i = 0;
          break;
        } 
        j++;
        continue;
      } 
      i = 1;
      break;
    } 
    if (i != 0)
      return "HH:mm:ss"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" ");
    StringReader stringReader = new StringReader(stringBuilder.toString());
    ArrayList<Object[]> arrayList1 = new ArrayList();
    ArrayList<Object[]> arrayList2 = new ArrayList();
    arrayList1.add(new Object[] { Character.valueOf('y'), new StringBuilder(), integer });
    if (paramBoolean)
      arrayList1.add(new Object[] { Character.valueOf('h'), new StringBuilder(), integer }); 
    while (true) {
      i = stringReader.read();
      if (i != -1) {
        char c = (char)i;
        j = arrayList1.size();
        for (i = 0; i < j; i++) {
          Object[] arrayOfObject = arrayList1.get(i);
          int m = ((Integer)arrayOfObject[2]).intValue();
          char c1 = getSuccessor(((Character)arrayOfObject[0]).charValue(), m);
          if (!Character.isLetterOrDigit(c)) {
            if (c1 == ((Character)arrayOfObject[0]).charValue() && c1 != 'S') {
              arrayList2.add(arrayOfObject);
            } else {
              ((StringBuilder)arrayOfObject[1]).append(c);
              if (c1 == 'X' || c1 == 'Y')
                arrayOfObject[2] = Integer.valueOf(4); 
            } 
          } else {
            char c2;
            if (c1 == 'X') {
              StringBuilder stringBuilder2 = new StringBuilder(((StringBuilder)arrayOfObject[1]).toString());
              stringBuilder2.append('M');
              arrayList1.add(new Object[] { Character.valueOf('M'), stringBuilder2, Integer.valueOf(1) });
              c2 = 'y';
            } else {
              c2 = c1;
              if (c1 == 'Y') {
                StringBuilder stringBuilder2 = new StringBuilder(((StringBuilder)arrayOfObject[1]).toString());
                stringBuilder2.append('d');
                arrayList1.add(new Object[] { Character.valueOf('d'), stringBuilder2, Integer.valueOf(1) });
                c2 = 'M';
              } 
            } 
            ((StringBuilder)arrayOfObject[1]).append(c2);
            if (c2 == ((Character)arrayOfObject[0]).charValue()) {
              arrayOfObject[2] = Integer.valueOf(m + 1);
            } else {
              arrayOfObject[0] = Character.valueOf(c2);
              arrayOfObject[2] = Integer.valueOf(1);
            } 
          } 
        } 
        j = arrayList2.size();
        for (i = 0; i < j; i++)
          arrayList1.remove(arrayList2.get(i)); 
        arrayList2.clear();
        continue;
      } 
      int k = arrayList1.size();
      for (i = 0; i < k; i++) {
        boolean bool1;
        boolean bool2;
        Object[] arrayOfObject = arrayList1.get(i);
        char c = ((Character)arrayOfObject[0]).charValue();
        if (getSuccessor(c, ((Integer)arrayOfObject[2]).intValue()) != c) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if ((c == 's' || c == 'm' || (c == 'h' && paramBoolean)) && bool1) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool1 && c == 'd' && !paramBoolean) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (((StringBuilder)arrayOfObject[1]).toString().indexOf('W') != -1) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        if ((j == 0 && !bool1) || bool2)
          arrayList2.add(arrayOfObject); 
      } 
      j = arrayList2.size();
      for (i = 0; i < j; i++)
        arrayList1.remove(arrayList2.get(i)); 
      arrayList2.clear();
      StringBuilder stringBuilder1 = (StringBuilder)((Object[])arrayList1.get(0))[1];
      stringBuilder1.setLength(stringBuilder1.length() - 1);
      return stringBuilder1.toString();
    } 
  }
  
  public static PreparedStatement getInstance(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    if (!Util.isJdbc4())
      return new PreparedStatement(paramMySQLConnection, paramString); 
    Constructor<?> constructor = JDBC_4_PSTMT_2_ARG_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (PreparedStatement)Util.handleNewInstance(constructor, new Object[] { paramMySQLConnection, paramString }, exceptionInterceptor);
  }
  
  public static PreparedStatement getInstance(MySQLConnection paramMySQLConnection, String paramString1, String paramString2) throws SQLException {
    if (!Util.isJdbc4())
      return new PreparedStatement(paramMySQLConnection, paramString1, paramString2); 
    Constructor<?> constructor = JDBC_4_PSTMT_3_ARG_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (PreparedStatement)Util.handleNewInstance(constructor, new Object[] { paramMySQLConnection, paramString1, paramString2 }, exceptionInterceptor);
  }
  
  public static PreparedStatement getInstance(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, ParseInfo paramParseInfo) throws SQLException {
    if (!Util.isJdbc4())
      return new PreparedStatement(paramMySQLConnection, paramString1, paramString2, paramParseInfo); 
    Constructor<?> constructor = JDBC_4_PSTMT_4_ARG_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (PreparedStatement)Util.handleNewInstance(constructor, new Object[] { paramMySQLConnection, paramString1, paramString2, paramParseInfo }, exceptionInterceptor);
  }
  
  private final char getSuccessor(char paramChar, int paramInt) {
    byte b = 115;
    if (paramChar == 'y' && paramInt == 2) {
      b = 88;
    } else if (paramChar == 'y' && paramInt < 4) {
      b = 121;
    } else {
      if (paramChar == 'y')
        return 'M'; 
      if (paramChar == 'M' && paramInt == 2) {
        b = 89;
      } else {
        if (paramChar == 'M' && paramInt < 3)
          return 'M'; 
        if (paramChar == 'M' || (paramChar == 'd' && paramInt < 2)) {
          b = 100;
        } else if (paramChar == 'd' || (paramChar == 'H' && paramInt < 2)) {
          b = 72;
        } else if (paramChar == 'H' || (paramChar == 'm' && paramInt < 2)) {
          b = 109;
        } else if (paramChar != 'm' && (paramChar != 's' || paramInt >= 2)) {
          b = 87;
        } 
      } 
    } 
    return b;
  }
  
  private final void hexEscapeBlock(byte[] paramArrayOfbyte, Buffer paramBuffer, int paramInt) throws SQLException {
    for (byte b = 0; b < paramInt; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      int j = i / 16;
      byte[] arrayOfByte = HEX_DIGITS;
      paramBuffer.writeByte(arrayOfByte[j]);
      paramBuffer.writeByte(arrayOfByte[i % 16]);
    } 
  }
  
  private void initializeFromParseInfo() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ParseInfo parseInfo = this.parseInfo;
      byte[][] arrayOfByte = parseInfo.staticSql;
      this.staticSqlStrings = arrayOfByte;
      this.isLoadDataQuery = parseInfo.foundLoadData;
      this.firstCharOfStmt = parseInfo.firstStmtChar;
      int i = arrayOfByte.length - 1;
      this.parameterCount = i;
      this.parameterValues = new byte[i][];
      this.parameterStreams = new InputStream[i];
      this.isStream = new boolean[i];
      this.streamLengths = new int[i];
      this.isNull = new boolean[i];
      this.parameterTypes = new int[i];
      clearParameters();
      for (i = 0; i < this.parameterCount; i++)
        this.isStream[i] = false; 
      return;
    } 
  }
  
  private boolean isEscapeNeededForString(String paramString, int paramInt) {
    boolean bool2;
    byte b = 0;
    boolean bool1 = false;
    while (true) {
      bool2 = bool1;
      if (b < paramInt) {
        char c = paramString.charAt(b);
        if (c == '\000' || c == '\n' || c == '\r' || c == '\032' || c == '"' || c == '\'' || c == '\\')
          bool1 = true; 
        if (bool1) {
          bool2 = bool1;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return bool2;
  }
  
  private void newSetDateInternal(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      TimeZone timeZone;
      SimpleDateFormat simpleDateFormat2 = this.ddf;
      if (paramCalendar != null) {
        timeZone = null;
      } else if (this.connection.getNoTimezoneConversionForDateType()) {
        timeZone = this.connection.getDefaultTimeZone();
      } else {
        timeZone = this.connection.getServerTimezoneTZ();
      } 
      SimpleDateFormat simpleDateFormat1 = TimeUtil.getSimpleDateFormat(simpleDateFormat2, "''yyyy-MM-dd''", paramCalendar, timeZone);
      this.ddf = simpleDateFormat1;
      setInternal(paramInt, simpleDateFormat1.format(paramDate));
      return;
    } 
  }
  
  private void newSetTimeInternal(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      TimeZone timeZone;
      SimpleDateFormat simpleDateFormat2 = this.tdf;
      if (paramCalendar != null) {
        timeZone = null;
      } else {
        timeZone = this.connection.getServerTimezoneTZ();
      } 
      SimpleDateFormat simpleDateFormat1 = TimeUtil.getSimpleDateFormat(simpleDateFormat2, "''HH:mm:ss''", paramCalendar, timeZone);
      this.tdf = simpleDateFormat1;
      setInternal(paramInt, simpleDateFormat1.format(paramTime));
      return;
    } 
  }
  
  private void newSetTimestampInternal(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      TimeZone timeZone;
      SimpleDateFormat simpleDateFormat = this.tsdf;
      if (paramCalendar != null) {
        timeZone = null;
      } else {
        timeZone = this.connection.getServerTimezoneTZ();
      } 
      this.tsdf = TimeUtil.getSimpleDateFormat(simpleDateFormat, "''yyyy-MM-dd HH:mm:ss", paramCalendar, timeZone);
      StringBuffer stringBuffer = new StringBuffer();
      this();
      stringBuffer.append(this.tsdf.format(paramTimestamp));
      stringBuffer.append('.');
      stringBuffer.append(TimeUtil.formatNanos(paramTimestamp.getNanos(), this.serverSupportsFracSecs, 6));
      stringBuffer.append('\'');
      setInternal(paramInt, stringBuffer.toString());
      return;
    } 
  }
  
  public static int readFully(Reader paramReader, char[] paramArrayOfchar, int paramInt) throws IOException {
    int i;
    for (i = 0; i < paramInt; i += j) {
      int j = paramReader.read(paramArrayOfchar, i, paramInt - i);
      if (j < 0)
        break; 
    } 
    return i;
  }
  
  private final int readblock(InputStream paramInputStream, byte[] paramArrayOfbyte) throws SQLException {
    try {
      return paramInputStream.read(paramArrayOfbyte);
    } finally {
      paramInputStream = null;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("PreparedStatement.56"));
      stringBuilder.append(paramInputStream.getClass().getName());
      SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
      sQLException.initCause((Throwable)paramInputStream);
    } 
  }
  
  private final int readblock(InputStream paramInputStream, byte[] paramArrayOfbyte, int paramInt) throws SQLException {
    int i = paramInt;
    try {
      return paramInputStream.read(paramArrayOfbyte, 0, i);
    } finally {
      paramInputStream = null;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("PreparedStatement.56"));
      stringBuilder.append(paramInputStream.getClass().getName());
      SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
      sQLException.initCause((Throwable)paramInputStream);
    } 
  }
  
  private void setNumericObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: instanceof java/lang/Boolean
    //   4: istore #5
    //   6: iconst_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: astore #6
    //   12: iload #5
    //   14: ifeq -> 41
    //   17: aload_2
    //   18: checkcast java/lang/Boolean
    //   21: invokevirtual booleanValue : ()Z
    //   24: ifeq -> 33
    //   27: aload #6
    //   29: astore_2
    //   30: goto -> 217
    //   33: iconst_0
    //   34: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   37: astore_2
    //   38: goto -> 217
    //   41: aload_2
    //   42: instanceof java/lang/String
    //   45: ifeq -> 212
    //   48: iload_3
    //   49: bipush #-7
    //   51: if_icmpeq -> 159
    //   54: iload_3
    //   55: bipush #-6
    //   57: if_icmpeq -> 148
    //   60: iload_3
    //   61: bipush #-5
    //   63: if_icmpeq -> 137
    //   66: iload_3
    //   67: tableswitch default -> 100, 4 -> 148, 5 -> 148, 6 -> 126, 7 -> 115, 8 -> 126
    //   100: new java/math/BigDecimal
    //   103: dup
    //   104: aload_2
    //   105: checkcast java/lang/String
    //   108: invokespecial <init> : (Ljava/lang/String;)V
    //   111: astore_2
    //   112: goto -> 217
    //   115: aload_2
    //   116: checkcast java/lang/String
    //   119: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Float;
    //   122: astore_2
    //   123: goto -> 217
    //   126: aload_2
    //   127: checkcast java/lang/String
    //   130: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
    //   133: astore_2
    //   134: goto -> 217
    //   137: aload_2
    //   138: checkcast java/lang/String
    //   141: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Long;
    //   144: astore_2
    //   145: goto -> 217
    //   148: aload_2
    //   149: checkcast java/lang/String
    //   152: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Integer;
    //   155: astore_2
    //   156: goto -> 217
    //   159: ldc_w '1'
    //   162: aload_2
    //   163: invokevirtual equals : (Ljava/lang/Object;)Z
    //   166: ifne -> 201
    //   169: ldc_w '0'
    //   172: aload_2
    //   173: invokevirtual equals : (Ljava/lang/Object;)Z
    //   176: ifeq -> 182
    //   179: goto -> 201
    //   182: ldc_w 'true'
    //   185: aload_2
    //   186: checkcast java/lang/String
    //   189: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   192: ifeq -> 33
    //   195: aload #6
    //   197: astore_2
    //   198: goto -> 217
    //   201: aload_2
    //   202: checkcast java/lang/String
    //   205: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Integer;
    //   208: astore_2
    //   209: goto -> 217
    //   212: aload_2
    //   213: checkcast java/lang/Number
    //   216: astore_2
    //   217: iload_3
    //   218: bipush #-7
    //   220: if_icmpeq -> 485
    //   223: iload_3
    //   224: bipush #-6
    //   226: if_icmpeq -> 485
    //   229: iload_3
    //   230: bipush #-5
    //   232: if_icmpeq -> 473
    //   235: iload_3
    //   236: tableswitch default -> 280, 2 -> 307, 3 -> 307, 4 -> 485, 5 -> 485, 6 -> 295, 7 -> 283, 8 -> 295
    //   280: goto -> 494
    //   283: aload_0
    //   284: iload_1
    //   285: aload_2
    //   286: invokevirtual floatValue : ()F
    //   289: invokevirtual setFloat : (IF)V
    //   292: goto -> 494
    //   295: aload_0
    //   296: iload_1
    //   297: aload_2
    //   298: invokevirtual doubleValue : ()D
    //   301: invokevirtual setDouble : (ID)V
    //   304: goto -> 494
    //   307: aload_2
    //   308: instanceof java/math/BigDecimal
    //   311: ifeq -> 426
    //   314: aload_2
    //   315: checkcast java/math/BigDecimal
    //   318: iload #4
    //   320: invokevirtual setScale : (I)Ljava/math/BigDecimal;
    //   323: astore #6
    //   325: aload #6
    //   327: astore_2
    //   328: goto -> 348
    //   331: astore #6
    //   333: aload_2
    //   334: checkcast java/math/BigDecimal
    //   337: iload #4
    //   339: iconst_4
    //   340: invokevirtual setScale : (II)Ljava/math/BigDecimal;
    //   343: astore #6
    //   345: aload #6
    //   347: astore_2
    //   348: aload_0
    //   349: iload_1
    //   350: aload_2
    //   351: invokevirtual setBigDecimal : (ILjava/math/BigDecimal;)V
    //   354: goto -> 494
    //   357: astore #6
    //   359: new java/lang/StringBuilder
    //   362: dup
    //   363: invokespecial <init> : ()V
    //   366: astore #6
    //   368: aload #6
    //   370: ldc_w 'Can't set scale of ''
    //   373: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   376: pop
    //   377: aload #6
    //   379: iload #4
    //   381: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   384: pop
    //   385: aload #6
    //   387: ldc_w '' for DECIMAL argument ''
    //   390: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   393: pop
    //   394: aload #6
    //   396: aload_2
    //   397: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   400: pop
    //   401: aload #6
    //   403: ldc_w '''
    //   406: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   409: pop
    //   410: aload #6
    //   412: invokevirtual toString : ()Ljava/lang/String;
    //   415: ldc_w 'S1009'
    //   418: aload_0
    //   419: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   422: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   425: athrow
    //   426: aload_2
    //   427: instanceof java/math/BigInteger
    //   430: ifeq -> 454
    //   433: aload_0
    //   434: iload_1
    //   435: new java/math/BigDecimal
    //   438: dup
    //   439: aload_2
    //   440: checkcast java/math/BigInteger
    //   443: iload #4
    //   445: invokespecial <init> : (Ljava/math/BigInteger;I)V
    //   448: invokevirtual setBigDecimal : (ILjava/math/BigDecimal;)V
    //   451: goto -> 494
    //   454: aload_0
    //   455: iload_1
    //   456: new java/math/BigDecimal
    //   459: dup
    //   460: aload_2
    //   461: invokevirtual doubleValue : ()D
    //   464: invokespecial <init> : (D)V
    //   467: invokevirtual setBigDecimal : (ILjava/math/BigDecimal;)V
    //   470: goto -> 494
    //   473: aload_0
    //   474: iload_1
    //   475: aload_2
    //   476: invokevirtual longValue : ()J
    //   479: invokevirtual setLong : (IJ)V
    //   482: goto -> 494
    //   485: aload_0
    //   486: iload_1
    //   487: aload_2
    //   488: invokevirtual intValue : ()I
    //   491: invokevirtual setInt : (II)V
    //   494: return
    // Exception table:
    //   from	to	target	type
    //   314	325	331	java/lang/ArithmeticException
    //   333	345	357	java/lang/ArithmeticException
  }
  
  private final void setSerializableObject(int paramInt, Object paramObject) throws SQLException {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream();
      this(byteArrayOutputStream);
      objectOutputStream.writeObject(paramObject);
      objectOutputStream.flush();
      objectOutputStream.close();
      byteArrayOutputStream.flush();
      byteArrayOutputStream.close();
      byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
      paramObject = new ByteArrayInputStream();
      super(arrayOfByte);
      setBinaryStream(paramInt, (InputStream)paramObject, arrayOfByte.length);
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = -2;
      return;
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("PreparedStatement.54"));
      stringBuilder.append(exception.getClass().getName());
      SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void setTimeInternal(int paramInt, Time paramTime, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean) throws SQLException {
    if (paramTime == null) {
      setNull(paramInt, 92);
    } else {
      checkClosed();
      if (!this.useLegacyDatetimeCode) {
        newSetTimeInternal(paramInt, paramTime, paramCalendar);
      } else {
        Calendar calendar = getCalendarInstanceForSessionOrNew();
        Time time = TimeUtil.changeTimezone(this.connection, calendar, paramCalendar, paramTime, paramTimeZone, this.connection.getServerTimezoneTZ(), paramBoolean);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'");
        stringBuilder.append(time.toString());
        stringBuilder.append("'");
        setInternal(paramInt, stringBuilder.toString());
      } 
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 92;
    } 
  }
  
  private final void streamToBytes(Buffer paramBuffer, InputStream paramInputStream, boolean paramBoolean1, int paramInt, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #11
    //   11: aload #11
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield streamConvertBuf : [B
    //   18: ifnonnull -> 30
    //   21: aload_0
    //   22: sipush #4096
    //   25: newarray byte
    //   27: putfield streamConvertBuf : [B
    //   30: aload_0
    //   31: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   34: invokeinterface getEncoding : ()Ljava/lang/String;
    //   39: astore #12
    //   41: aload_0
    //   42: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   45: invokeinterface isNoBackslashEscapesSet : ()Z
    //   50: ifne -> 103
    //   53: aload_0
    //   54: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   57: invokeinterface getUseUnicode : ()Z
    //   62: ifeq -> 97
    //   65: aload #12
    //   67: ifnull -> 97
    //   70: aload #12
    //   72: invokestatic isMultibyteCharset : (Ljava/lang/String;)Z
    //   75: ifeq -> 97
    //   78: aload_0
    //   79: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   82: invokeinterface parserKnowsUnicode : ()Z
    //   87: istore #10
    //   89: iload #10
    //   91: ifne -> 97
    //   94: goto -> 103
    //   97: iconst_0
    //   98: istore #7
    //   100: goto -> 106
    //   103: iconst_1
    //   104: istore #7
    //   106: iload #4
    //   108: iconst_m1
    //   109: if_icmpne -> 115
    //   112: iconst_0
    //   113: istore #5
    //   115: iload #5
    //   117: ifeq -> 136
    //   120: aload_0
    //   121: aload_2
    //   122: aload_0
    //   123: getfield streamConvertBuf : [B
    //   126: iload #4
    //   128: invokespecial readblock : (Ljava/io/InputStream;[BI)I
    //   131: istore #6
    //   133: goto -> 147
    //   136: aload_0
    //   137: aload_2
    //   138: aload_0
    //   139: getfield streamConvertBuf : [B
    //   142: invokespecial readblock : (Ljava/io/InputStream;[B)I
    //   145: istore #6
    //   147: iload #4
    //   149: iload #6
    //   151: isub
    //   152: istore #9
    //   154: iload #7
    //   156: ifeq -> 169
    //   159: aload_1
    //   160: ldc_w 'x'
    //   163: invokevirtual writeStringNoNull : (Ljava/lang/String;)V
    //   166: goto -> 194
    //   169: aload_0
    //   170: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   173: invokeinterface getIO : ()Lcom/mysql/jdbc/MysqlIO;
    //   178: iconst_4
    //   179: iconst_1
    //   180: iconst_0
    //   181: invokevirtual versionMeetsMinimum : (III)Z
    //   184: ifeq -> 194
    //   187: aload_1
    //   188: ldc_w '_binary'
    //   191: invokevirtual writeStringNoNull : (Ljava/lang/String;)V
    //   194: iload #6
    //   196: istore #4
    //   198: iload #9
    //   200: istore #8
    //   202: iload_3
    //   203: ifeq -> 220
    //   206: aload_1
    //   207: bipush #39
    //   209: invokevirtual writeByte : (B)V
    //   212: iload #9
    //   214: istore #8
    //   216: iload #6
    //   218: istore #4
    //   220: iload #4
    //   222: ifle -> 328
    //   225: iload #7
    //   227: ifeq -> 244
    //   230: aload_0
    //   231: aload_0
    //   232: getfield streamConvertBuf : [B
    //   235: aload_1
    //   236: iload #4
    //   238: invokespecial hexEscapeBlock : ([BLcom/mysql/jdbc/Buffer;I)V
    //   241: goto -> 273
    //   244: iload_3
    //   245: ifeq -> 262
    //   248: aload_0
    //   249: aload_0
    //   250: getfield streamConvertBuf : [B
    //   253: aload_1
    //   254: iload #4
    //   256: invokespecial escapeblockFast : ([BLcom/mysql/jdbc/Buffer;I)V
    //   259: goto -> 273
    //   262: aload_1
    //   263: aload_0
    //   264: getfield streamConvertBuf : [B
    //   267: iconst_0
    //   268: iload #4
    //   270: invokevirtual writeBytesNoNull : ([BII)V
    //   273: iload #5
    //   275: ifeq -> 314
    //   278: aload_0
    //   279: aload_2
    //   280: aload_0
    //   281: getfield streamConvertBuf : [B
    //   284: iload #8
    //   286: invokespecial readblock : (Ljava/io/InputStream;[BI)I
    //   289: istore #6
    //   291: iload #6
    //   293: istore #4
    //   295: iload #6
    //   297: ifle -> 220
    //   300: iload #8
    //   302: iload #6
    //   304: isub
    //   305: istore #8
    //   307: iload #6
    //   309: istore #4
    //   311: goto -> 220
    //   314: aload_0
    //   315: aload_2
    //   316: aload_0
    //   317: getfield streamConvertBuf : [B
    //   320: invokespecial readblock : (Ljava/io/InputStream;[B)I
    //   323: istore #4
    //   325: goto -> 220
    //   328: iload_3
    //   329: ifeq -> 338
    //   332: aload_1
    //   333: bipush #39
    //   335: invokevirtual writeByte : (B)V
    //   338: aload_0
    //   339: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   342: invokeinterface getAutoClosePStmtStreams : ()Z
    //   347: istore_3
    //   348: iload_3
    //   349: ifeq -> 356
    //   352: aload_2
    //   353: invokevirtual close : ()V
    //   356: aload #11
    //   358: monitorexit
    //   359: return
    //   360: astore_1
    //   361: aload_1
    //   362: invokevirtual toString : ()Ljava/lang/String;
    //   365: ldc_w 'S1009'
    //   368: aconst_null
    //   369: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   372: astore #12
    //   374: aload #12
    //   376: aload_1
    //   377: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   380: pop
    //   381: aload #12
    //   383: athrow
    //   384: astore_1
    //   385: aload_0
    //   386: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   389: invokeinterface getAutoClosePStmtStreams : ()Z
    //   394: istore_3
    //   395: iload_3
    //   396: ifeq -> 403
    //   399: aload_2
    //   400: invokevirtual close : ()V
    //   403: aload_1
    //   404: athrow
    //   405: astore_1
    //   406: aload #11
    //   408: monitorexit
    //   409: aload_1
    //   410: athrow
    //   411: astore_1
    //   412: goto -> 356
    //   415: astore_2
    //   416: goto -> 403
    // Exception table:
    //   from	to	target	type
    //   14	30	384	finally
    //   30	41	384	finally
    //   41	65	360	java/lang/RuntimeException
    //   41	65	384	finally
    //   70	89	360	java/lang/RuntimeException
    //   70	89	384	finally
    //   120	133	384	finally
    //   136	147	384	finally
    //   159	166	384	finally
    //   169	194	384	finally
    //   206	212	384	finally
    //   230	241	384	finally
    //   248	259	384	finally
    //   262	273	384	finally
    //   278	291	384	finally
    //   314	325	384	finally
    //   332	338	384	finally
    //   338	348	405	finally
    //   352	356	411	java/io/IOException
    //   352	356	405	finally
    //   356	359	405	finally
    //   361	384	384	finally
    //   385	395	405	finally
    //   399	403	415	java/io/IOException
    //   399	403	405	finally
    //   403	405	405	finally
    //   406	409	405	finally
  }
  
  private final byte[] streamToBytes(InputStream paramInputStream, boolean paramBoolean1, int paramInt, boolean paramBoolean2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      paramInputStream.mark(2147483647);
      try {
        int i;
        if (this.streamConvertBuf == null)
          this.streamConvertBuf = new byte[4096]; 
        if (paramInt == -1)
          paramBoolean2 = false; 
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this();
        if (paramBoolean2) {
          i = readblock(paramInputStream, this.streamConvertBuf, paramInt);
        } else {
          i = readblock(paramInputStream, this.streamConvertBuf);
        } 
        int k = paramInt - i;
        paramInt = i;
        int j = k;
        if (paramBoolean1) {
          if (this.connection.versionMeetsMinimum(4, 1, 0)) {
            byteArrayOutputStream.write(95);
            byteArrayOutputStream.write(98);
            byteArrayOutputStream.write(105);
            byteArrayOutputStream.write(110);
            byteArrayOutputStream.write(97);
            byteArrayOutputStream.write(114);
            byteArrayOutputStream.write(121);
          } 
          byteArrayOutputStream.write(39);
          j = k;
          paramInt = i;
        } 
        while (paramInt > 0) {
          if (paramBoolean1) {
            escapeblockFast(this.streamConvertBuf, byteArrayOutputStream, paramInt);
          } else {
            byteArrayOutputStream.write(this.streamConvertBuf, 0, paramInt);
          } 
          if (paramBoolean2) {
            i = readblock(paramInputStream, this.streamConvertBuf, j);
            paramInt = i;
            if (i > 0) {
              j -= i;
              paramInt = i;
            } 
            continue;
          } 
          paramInt = readblock(paramInputStream, this.streamConvertBuf);
        } 
        if (paramBoolean1)
          byteArrayOutputStream.write(39); 
        return byteArrayOutputStream.toByteArray();
      } finally {
        try {
          iOException.reset();
        } catch (IOException iOException1) {}
        paramBoolean1 = this.connection.getAutoClosePStmtStreams();
        if (paramBoolean1)
          try {
            iOException.close();
          } catch (IOException iOException1) {} 
      } 
    } 
  }
  
  public void addBatch() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.batchedArgs == null) {
        ArrayList<Object> arrayList = new ArrayList();
        this();
        this.batchedArgs = arrayList;
      } 
      byte b = 0;
      while (true) {
        byte[][] arrayOfByte = this.parameterValues;
        if (b < arrayOfByte.length) {
          checkAllParametersSet(arrayOfByte[b], this.parameterStreams[b], b);
          b++;
          continue;
        } 
        List<Object> list = this.batchedArgs;
        BatchParams batchParams = new BatchParams();
        this(this, arrayOfByte, this.parameterStreams, this.isStream, this.streamLengths, this.isNull);
        list.add(batchParams);
        return;
      } 
    } 
  }
  
  public void addBatch(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.batchHasPlainStatements = true;
      super.addBatch(paramString);
      return;
    } 
  }
  
  public String asSql() throws SQLException {
    return asSql(false);
  }
  
  public String asSql(boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      try {
        int j = this.parameterCount;
        int k = getParameterIndexOffset();
        Object object = null;
        int i = this.batchCommandIndex;
        if (i != -1)
          object = this.batchedArgs.get(i); 
        for (i = 0; i < j + k; i++) {
          String str = this.charEncoding;
          if (str != null) {
            stringBuilder.append(StringUtils.toString(this.staticSqlStrings[i], str));
          } else {
            stringBuilder.append(StringUtils.toString(this.staticSqlStrings[i]));
          } 
          if (object != null && object instanceof String) {
            stringBuilder.append((String)object);
          } else {
            boolean bool;
            byte[] arrayOfByte;
            int m = this.batchCommandIndex;
            if (m == -1) {
              arrayOfByte = this.parameterValues[i];
            } else {
              arrayOfByte = ((BatchParams)object).parameterStrings[i];
            } 
            if (m == -1) {
              bool = this.isStream[i];
            } else {
              bool = ((BatchParams)object).isStream[i];
            } 
            if (arrayOfByte == null && !bool) {
              if (paramBoolean)
                stringBuilder.append("'"); 
              stringBuilder.append("** NOT SPECIFIED **");
              if (paramBoolean)
                stringBuilder.append("'"); 
            } else if (bool) {
              if (paramBoolean)
                stringBuilder.append("'"); 
              stringBuilder.append("** STREAM DATA **");
              if (paramBoolean)
                stringBuilder.append("'"); 
            } else {
              SingleByteCharsetConverter singleByteCharsetConverter = this.charConverter;
              if (singleByteCharsetConverter != null) {
                stringBuilder.append(singleByteCharsetConverter.toString(arrayOfByte));
              } else if (this.charEncoding != null) {
                String str1 = new String();
                this(arrayOfByte, this.charEncoding);
                stringBuilder.append(str1);
              } else {
                stringBuilder.append(StringUtils.toAsciiString(arrayOfByte));
              } 
            } 
          } 
        } 
        if (this.charEncoding != null) {
          stringBuilder.append(StringUtils.toString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()], this.charEncoding));
        } else {
          stringBuilder.append(StringUtils.toAsciiString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()]));
        } 
        object = stringBuilder.toString();
        return (String)object;
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        RuntimeException runtimeException = new RuntimeException();
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        stringBuilder1.append(Messages.getString("PreparedStatement.32"));
        stringBuilder1.append(this.charEncoding);
        stringBuilder1.append(Messages.getString("PreparedStatement.33"));
        this(stringBuilder1.toString());
        throw runtimeException;
      } 
    } 
  }
  
  public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
    return this.parseInfo.canRewriteAsMultiValueInsert;
  }
  
  public void checkBounds(int paramInt1, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: iconst_1
    //   14: if_icmplt -> 140
    //   17: iload_1
    //   18: aload_0
    //   19: getfield parameterCount : I
    //   22: if_icmpgt -> 55
    //   25: iload_2
    //   26: iconst_m1
    //   27: if_icmpne -> 52
    //   30: iload_1
    //   31: iconst_1
    //   32: if_icmpeq -> 38
    //   35: goto -> 52
    //   38: ldc_w 'Can't set IN parameter for return value of stored function call.'
    //   41: ldc_w 'S1009'
    //   44: aload_0
    //   45: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   48: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   51: athrow
    //   52: aload_3
    //   53: monitorexit
    //   54: return
    //   55: new java/lang/StringBuilder
    //   58: astore #4
    //   60: aload #4
    //   62: invokespecial <init> : ()V
    //   65: aload #4
    //   67: ldc_w 'PreparedStatement.51'
    //   70: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   73: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: pop
    //   77: aload #4
    //   79: iload_1
    //   80: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload #4
    //   86: ldc_w 'PreparedStatement.52'
    //   89: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   92: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: pop
    //   96: aload #4
    //   98: aload_0
    //   99: getfield parameterValues : [[B
    //   102: arraylength
    //   103: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: aload #4
    //   109: ldc_w 'PreparedStatement.53'
    //   112: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: aload #4
    //   121: invokevirtual toString : ()Ljava/lang/String;
    //   124: ldc_w 'S1009'
    //   127: aload_0
    //   128: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   131: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   134: athrow
    //   135: astore #4
    //   137: goto -> 197
    //   140: new java/lang/StringBuilder
    //   143: astore #4
    //   145: aload #4
    //   147: invokespecial <init> : ()V
    //   150: aload #4
    //   152: ldc_w 'PreparedStatement.49'
    //   155: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload #4
    //   164: iload_1
    //   165: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: aload #4
    //   171: ldc_w 'PreparedStatement.50'
    //   174: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: aload #4
    //   183: invokevirtual toString : ()Ljava/lang/String;
    //   186: ldc_w 'S1009'
    //   189: aload_0
    //   190: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   193: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   196: athrow
    //   197: aload_3
    //   198: monitorexit
    //   199: aload #4
    //   201: athrow
    // Exception table:
    //   from	to	target	type
    //   17	25	135	finally
    //   38	52	135	finally
    //   52	54	135	finally
    //   55	135	135	finally
    //   140	197	135	finally
    //   197	199	135	finally
  }
  
  public boolean checkReadOnlySafeStatement() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.firstCharOfStmt == 'S' || !this.connection.isReadOnly())
        return true; 
      return false;
    } 
  }
  
  public void clearBatch() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.batchHasPlainStatements = false;
      super.clearBatch();
      return;
    } 
  }
  
  public void clearParameters() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_0
    //   13: istore_1
    //   14: aload_0
    //   15: getfield parameterValues : [[B
    //   18: astore_3
    //   19: iload_1
    //   20: aload_3
    //   21: arraylength
    //   22: if_icmpge -> 63
    //   25: aload_3
    //   26: iload_1
    //   27: aconst_null
    //   28: aastore
    //   29: aload_0
    //   30: getfield parameterStreams : [Ljava/io/InputStream;
    //   33: iload_1
    //   34: aconst_null
    //   35: aastore
    //   36: aload_0
    //   37: getfield isStream : [Z
    //   40: iload_1
    //   41: iconst_0
    //   42: bastore
    //   43: aload_0
    //   44: getfield isNull : [Z
    //   47: iload_1
    //   48: iconst_0
    //   49: bastore
    //   50: aload_0
    //   51: getfield parameterTypes : [I
    //   54: iload_1
    //   55: iconst_0
    //   56: iastore
    //   57: iinc #1, 1
    //   60: goto -> 14
    //   63: aload_2
    //   64: monitorexit
    //   65: return
    //   66: astore_3
    //   67: aload_2
    //   68: monitorexit
    //   69: aload_3
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   14	25	66	finally
    //   29	57	66	finally
    //   63	65	66	finally
    //   67	69	66	finally
  }
  
  public int computeBatchSize(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      long[] arrayOfLong = computeMaxParameterSetSizeAndBatchSize(paramInt);
      long l2 = arrayOfLong[0];
      long l1 = arrayOfLong[1];
      int i = this.connection.getMaxAllowedPacket();
      if (l1 < (i - this.originalSql.length()))
        return paramInt; 
      paramInt = (int)Math.max(1L, (i - this.originalSql.length()) / l2);
      return paramInt;
    } 
  }
  
  public long[] computeMaxParameterSetSizeAndBatchSize(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #13
    //   11: aload #13
    //   13: monitorenter
    //   14: iconst_0
    //   15: istore_3
    //   16: lconst_0
    //   17: lstore #5
    //   19: lconst_0
    //   20: lstore #7
    //   22: iload_3
    //   23: iload_1
    //   24: if_icmpge -> 227
    //   27: aload_0
    //   28: getfield batchedArgs : Ljava/util/List;
    //   31: iload_3
    //   32: invokeinterface get : (I)Ljava/lang/Object;
    //   37: checkcast com/mysql/jdbc/PreparedStatement$BatchParams
    //   40: astore #16
    //   42: aload #16
    //   44: getfield isNull : [Z
    //   47: astore #14
    //   49: aload #16
    //   51: getfield isStream : [Z
    //   54: astore #15
    //   56: iconst_0
    //   57: istore #4
    //   59: lconst_0
    //   60: lstore #9
    //   62: iload #4
    //   64: aload #14
    //   66: arraylength
    //   67: if_icmpge -> 155
    //   70: aload #14
    //   72: iload #4
    //   74: baload
    //   75: ifne -> 137
    //   78: aload #15
    //   80: iload #4
    //   82: baload
    //   83: ifeq -> 120
    //   86: aload #16
    //   88: getfield streamLengths : [I
    //   91: iload #4
    //   93: iaload
    //   94: istore_2
    //   95: iload_2
    //   96: iconst_m1
    //   97: if_icmpeq -> 107
    //   100: iload_2
    //   101: iconst_2
    //   102: imul
    //   103: istore_2
    //   104: goto -> 130
    //   107: aload #16
    //   109: getfield parameterStrings : [[B
    //   112: iload #4
    //   114: aaload
    //   115: arraylength
    //   116: istore_2
    //   117: goto -> 130
    //   120: aload #16
    //   122: getfield parameterStrings : [[B
    //   125: iload #4
    //   127: aaload
    //   128: arraylength
    //   129: istore_2
    //   130: iload_2
    //   131: i2l
    //   132: lstore #11
    //   134: goto -> 142
    //   137: ldc2_w 4
    //   140: lstore #11
    //   142: lload #9
    //   144: lload #11
    //   146: ladd
    //   147: lstore #9
    //   149: iinc #4, 1
    //   152: goto -> 62
    //   155: aload_0
    //   156: invokevirtual getValuesClause : ()Ljava/lang/String;
    //   159: ifnull -> 173
    //   162: aload_0
    //   163: invokevirtual getValuesClause : ()Ljava/lang/String;
    //   166: invokevirtual length : ()I
    //   169: istore_2
    //   170: goto -> 181
    //   173: aload_0
    //   174: getfield originalSql : Ljava/lang/String;
    //   177: invokevirtual length : ()I
    //   180: istore_2
    //   181: lload #9
    //   183: iload_2
    //   184: iconst_1
    //   185: iadd
    //   186: i2l
    //   187: ladd
    //   188: lstore #11
    //   190: lload #7
    //   192: lload #11
    //   194: ladd
    //   195: lstore #9
    //   197: lload #5
    //   199: lstore #7
    //   201: lload #11
    //   203: lload #5
    //   205: lcmp
    //   206: ifle -> 213
    //   209: lload #11
    //   211: lstore #7
    //   213: iinc #3, 1
    //   216: lload #7
    //   218: lstore #5
    //   220: lload #9
    //   222: lstore #7
    //   224: goto -> 22
    //   227: aload #13
    //   229: monitorexit
    //   230: iconst_2
    //   231: newarray long
    //   233: dup
    //   234: iconst_0
    //   235: lload #5
    //   237: lastore
    //   238: dup
    //   239: iconst_1
    //   240: lload #7
    //   242: lastore
    //   243: areturn
    //   244: astore #14
    //   246: aload #13
    //   248: monitorexit
    //   249: aload #14
    //   251: athrow
    // Exception table:
    //   from	to	target	type
    //   27	56	244	finally
    //   62	70	244	finally
    //   86	95	244	finally
    //   107	117	244	finally
    //   120	130	244	finally
    //   155	170	244	finally
    //   173	181	244	finally
    //   227	244	244	finally
    //   246	249	244	finally
  }
  
  public boolean containsOnDuplicateKeyUpdateInSQL() {
    return this.parseInfo.isOnDuplicateKeyUpdate;
  }
  
  public void detectFractionalSecondsSupport() throws SQLException {
    boolean bool;
    if (this.connection != null && this.connection.versionMeetsMinimum(5, 6, 4)) {
      bool = true;
    } else {
      bool = false;
    } 
    this.serverSupportsFracSecs = bool;
  }
  
  public boolean execute() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      MySQLConnection mySQLConnection = this.connection;
      if (this.doPingInstead || checkReadOnlySafeStatement()) {
        boolean bool2;
        String str;
        CachedResultSetMetaData cachedResultSetMetaData;
        boolean bool3 = false;
        this.lastQueryIsOnDupKeyUpdate = false;
        if (this.retrieveGeneratedKeys)
          this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL(); 
        this.batchedGeneratedKeys = null;
        resetCancelledState();
        implicitlyCloseAllOpenResults();
        clearWarnings();
        if (this.doPingInstead) {
          doPingInstead();
          return true;
        } 
        setupStreamingTimeout(mySQLConnection);
        Buffer buffer = fillSendPacket();
        if (!mySQLConnection.getCatalog().equals(this.currentCatalog)) {
          str = mySQLConnection.getCatalog();
          mySQLConnection.setCatalog(this.currentCatalog);
        } else {
          str = null;
        } 
        if (mySQLConnection.getCacheResultSetMetadata()) {
          cachedResultSetMetaData = mySQLConnection.getCachedMetaData(this.originalSql);
        } else {
          cachedResultSetMetaData = null;
        } 
        if (cachedResultSetMetaData != null) {
          resultSetInternalMethods = (ResultSetInternalMethods)cachedResultSetMetaData.fields;
        } else {
          resultSetInternalMethods = null;
        } 
        if (this.retrieveGeneratedKeys) {
          bool1 = mySQLConnection.isReadInfoMsgEnabled();
          mySQLConnection.setReadInfoMsgEnabled(true);
        } else {
          bool1 = false;
        } 
        if (this.firstCharOfStmt == 'S') {
          i = this.maxRows;
        } else {
          i = -1;
        } 
        mySQLConnection.setSessionMaxRows(i);
        int i = this.maxRows;
        boolean bool = createStreamingResultSet();
        if (this.firstCharOfStmt == 'S') {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        ResultSetInternalMethods resultSetInternalMethods = executeInternal(i, buffer, bool, bool2, (Field[])resultSetInternalMethods, false);
        if (cachedResultSetMetaData != null) {
          mySQLConnection.initializeResultsMetadataFromCache(this.originalSql, cachedResultSetMetaData, resultSetInternalMethods);
        } else if (resultSetInternalMethods.reallyResult() && mySQLConnection.getCacheResultSetMetadata()) {
          mySQLConnection.initializeResultsMetadataFromCache(this.originalSql, null, resultSetInternalMethods);
        } 
        if (this.retrieveGeneratedKeys) {
          mySQLConnection.setReadInfoMsgEnabled(bool1);
          resultSetInternalMethods.setFirstCharOfQuery(this.firstCharOfStmt);
        } 
        if (str != null)
          mySQLConnection.setCatalog(str); 
        if (resultSetInternalMethods != null) {
          this.lastInsertId = resultSetInternalMethods.getUpdateID();
          this.results = resultSetInternalMethods;
        } 
        boolean bool1 = bool3;
        if (resultSetInternalMethods != null) {
          bool1 = bool3;
          if (resultSetInternalMethods.reallyResult())
            bool1 = true; 
        } 
        return bool1;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(Messages.getString("PreparedStatement.20"));
      stringBuilder.append(Messages.getString("PreparedStatement.21"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public long[] executeBatchInternal() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.connection.isReadOnly()) {
        null = this.batchedArgs;
        if (null == null || null.size() == 0)
          return new long[0]; 
        int i = this.timeoutInMillis;
        this.timeoutInMillis = 0;
        resetCancelledState();
        try {
          statementBegins();
          clearWarnings();
          if (!this.batchHasPlainStatements && this.connection.getRewriteBatchedStatements()) {
            if (canRewriteAsMultiValueInsertAtSqlLevel())
              return executeBatchedInserts(i); 
            if (this.connection.versionMeetsMinimum(4, 1, 0) && !this.batchHasPlainStatements) {
              null = this.batchedArgs;
              if (null != null && null.size() > 3)
                return executePreparedBatchAsMultiStatement(i); 
            } 
          } 
          return executeBatchSerially(i);
        } finally {
          this.statementExecuting.set(false);
          clearBatch();
        } 
      } 
      SQLException sQLException = new SQLException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(Messages.getString("PreparedStatement.25"));
      stringBuilder.append(Messages.getString("PreparedStatement.26"));
      this(stringBuilder.toString(), "S1009");
      throw sQLException;
    } 
  }
  
  public long[] executeBatchSerially(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #9
    //   11: aload #9
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   18: astore #10
    //   20: aload #10
    //   22: ifnonnull -> 30
    //   25: aload_0
    //   26: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   29: pop
    //   30: aload_0
    //   31: getfield batchedArgs : Ljava/util/List;
    //   34: astore #8
    //   36: aconst_null
    //   37: astore #7
    //   39: aconst_null
    //   40: astore #5
    //   42: aconst_null
    //   43: astore #6
    //   45: aload #8
    //   47: ifnull -> 721
    //   50: aload #8
    //   52: invokeinterface size : ()I
    //   57: istore_3
    //   58: iload_3
    //   59: newarray long
    //   61: astore #8
    //   63: iconst_0
    //   64: istore_2
    //   65: iload_2
    //   66: iload_3
    //   67: if_icmpge -> 83
    //   70: aload #8
    //   72: iload_2
    //   73: ldc2_w -3
    //   76: lastore
    //   77: iinc #2, 1
    //   80: goto -> 65
    //   83: aload #10
    //   85: invokeinterface getEnableQueryTimeouts : ()Z
    //   90: ifeq -> 169
    //   93: iload_1
    //   94: ifeq -> 169
    //   97: aload #10
    //   99: iconst_5
    //   100: iconst_0
    //   101: iconst_0
    //   102: invokeinterface versionMeetsMinimum : (III)Z
    //   107: ifeq -> 169
    //   110: new com/mysql/jdbc/StatementImpl$CancelTask
    //   113: astore #5
    //   115: aload #5
    //   117: aload_0
    //   118: aload_0
    //   119: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   122: aload #10
    //   124: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   129: aload #5
    //   131: iload_1
    //   132: i2l
    //   133: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   136: goto -> 172
    //   139: astore #7
    //   141: aload #5
    //   143: astore #6
    //   145: aload #7
    //   147: astore #5
    //   149: goto -> 616
    //   152: astore #6
    //   154: aload #5
    //   156: astore #7
    //   158: aload #6
    //   160: astore #5
    //   162: aload #7
    //   164: astore #6
    //   166: goto -> 625
    //   169: aconst_null
    //   170: astore #5
    //   172: aload_0
    //   173: getfield retrieveGeneratedKeys : Z
    //   176: istore #4
    //   178: iload #4
    //   180: ifeq -> 237
    //   183: new java/util/ArrayList
    //   186: astore #6
    //   188: aload #6
    //   190: iload_3
    //   191: invokespecial <init> : (I)V
    //   194: aload_0
    //   195: aload #6
    //   197: putfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   200: goto -> 237
    //   203: astore #6
    //   205: aload #5
    //   207: astore #7
    //   209: aload #6
    //   211: astore #5
    //   213: aload #7
    //   215: astore #6
    //   217: goto -> 616
    //   220: astore #6
    //   222: aload #5
    //   224: astore #7
    //   226: aload #6
    //   228: astore #5
    //   230: aload #7
    //   232: astore #6
    //   234: goto -> 625
    //   237: aload_0
    //   238: iconst_0
    //   239: putfield batchCommandIndex : I
    //   242: aconst_null
    //   243: astore #6
    //   245: aload_0
    //   246: getfield batchCommandIndex : I
    //   249: istore_1
    //   250: iload_1
    //   251: iload_3
    //   252: if_icmpge -> 519
    //   255: aload_0
    //   256: getfield batchedArgs : Ljava/util/List;
    //   259: iload_1
    //   260: invokeinterface get : (I)Ljava/lang/Object;
    //   265: astore #7
    //   267: aload #7
    //   269: instanceof java/lang/String
    //   272: ifeq -> 337
    //   275: aload #8
    //   277: aload_0
    //   278: getfield batchCommandIndex : I
    //   281: aload_0
    //   282: aload #7
    //   284: checkcast java/lang/String
    //   287: iconst_1
    //   288: aload_0
    //   289: getfield retrieveGeneratedKeys : Z
    //   292: invokevirtual executeUpdateInternal : (Ljava/lang/String;ZZ)J
    //   295: lastore
    //   296: aload_0
    //   297: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   300: invokeinterface getFirstCharOfQuery : ()C
    //   305: bipush #73
    //   307: if_icmpne -> 327
    //   310: aload_0
    //   311: aload #7
    //   313: checkcast java/lang/String
    //   316: invokevirtual containsOnDuplicateKeyInString : (Ljava/lang/String;)Z
    //   319: ifeq -> 327
    //   322: iconst_1
    //   323: istore_1
    //   324: goto -> 329
    //   327: iconst_0
    //   328: istore_1
    //   329: aload_0
    //   330: iload_1
    //   331: invokevirtual getBatchedGeneratedKeys : (I)V
    //   334: goto -> 474
    //   337: aload #7
    //   339: checkcast com/mysql/jdbc/PreparedStatement$BatchParams
    //   342: astore #14
    //   344: aload_0
    //   345: getfield batchCommandIndex : I
    //   348: istore_1
    //   349: aload #14
    //   351: getfield parameterStrings : [[B
    //   354: astore #12
    //   356: aload #14
    //   358: getfield parameterStreams : [Ljava/io/InputStream;
    //   361: astore #11
    //   363: aload #14
    //   365: getfield isStream : [Z
    //   368: astore #7
    //   370: aload #14
    //   372: getfield streamLengths : [I
    //   375: astore #13
    //   377: aload #14
    //   379: getfield isNull : [Z
    //   382: astore #14
    //   384: aload #8
    //   386: iload_1
    //   387: aload_0
    //   388: aload #12
    //   390: aload #11
    //   392: aload #7
    //   394: aload #13
    //   396: aload #14
    //   398: iconst_1
    //   399: invokevirtual executeUpdateInternal : ([[B[Ljava/io/InputStream;[Z[I[ZZ)J
    //   402: lastore
    //   403: aload_0
    //   404: invokevirtual containsOnDuplicateKeyUpdateInSQL : ()Z
    //   407: ifeq -> 415
    //   410: iconst_1
    //   411: istore_1
    //   412: goto -> 417
    //   415: iconst_0
    //   416: istore_1
    //   417: aload_0
    //   418: iload_1
    //   419: invokevirtual getBatchedGeneratedKeys : (I)V
    //   422: goto -> 474
    //   425: astore #6
    //   427: goto -> 432
    //   430: astore #6
    //   432: aload #8
    //   434: aload_0
    //   435: getfield batchCommandIndex : I
    //   438: ldc2_w -3
    //   441: lastore
    //   442: aload_0
    //   443: getfield continueBatchOnError : Z
    //   446: ifeq -> 487
    //   449: aload #6
    //   451: instanceof com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   454: ifne -> 487
    //   457: aload #6
    //   459: instanceof com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   462: ifne -> 487
    //   465: aload_0
    //   466: aload #6
    //   468: invokevirtual hasDeadlockOrTimeoutRolledBackTx : (Ljava/sql/SQLException;)Z
    //   471: ifne -> 487
    //   474: aload_0
    //   475: aload_0
    //   476: getfield batchCommandIndex : I
    //   479: iconst_1
    //   480: iadd
    //   481: putfield batchCommandIndex : I
    //   484: goto -> 245
    //   487: aload_0
    //   488: getfield batchCommandIndex : I
    //   491: istore_1
    //   492: iload_1
    //   493: newarray long
    //   495: astore #7
    //   497: aload #8
    //   499: iconst_0
    //   500: aload #7
    //   502: iconst_0
    //   503: iload_1
    //   504: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   507: aload #6
    //   509: aload #7
    //   511: aload_0
    //   512: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   515: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   518: athrow
    //   519: aload #6
    //   521: ifnonnull -> 562
    //   524: aload_0
    //   525: iconst_m1
    //   526: putfield batchCommandIndex : I
    //   529: aload #5
    //   531: ifnull -> 551
    //   534: aload #5
    //   536: invokevirtual cancel : ()Z
    //   539: pop
    //   540: aload #10
    //   542: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   547: invokevirtual purge : ()I
    //   550: pop
    //   551: aload_0
    //   552: invokevirtual resetCancelledState : ()V
    //   555: aload #8
    //   557: astore #5
    //   559: goto -> 721
    //   562: aload #6
    //   564: aload #8
    //   566: aload_0
    //   567: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   570: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   573: athrow
    //   574: astore #6
    //   576: goto -> 586
    //   579: astore #6
    //   581: goto -> 603
    //   584: astore #6
    //   586: aload #5
    //   588: astore #7
    //   590: aload #6
    //   592: astore #5
    //   594: aload #7
    //   596: astore #6
    //   598: goto -> 687
    //   601: astore #6
    //   603: aload #6
    //   605: astore #7
    //   607: aload #5
    //   609: astore #6
    //   611: goto -> 629
    //   614: astore #5
    //   616: goto -> 687
    //   619: astore #5
    //   621: aload #7
    //   623: astore #6
    //   625: aload #5
    //   627: astore #7
    //   629: aload_0
    //   630: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   633: pop
    //   634: aload #7
    //   636: athrow
    //   637: astore #5
    //   639: goto -> 687
    //   642: astore #5
    //   644: aload #8
    //   646: aload_0
    //   647: getfield batchCommandIndex : I
    //   650: ldc2_w -3
    //   653: lastore
    //   654: aload_0
    //   655: getfield batchCommandIndex : I
    //   658: newarray long
    //   660: astore #7
    //   662: aload #8
    //   664: iconst_0
    //   665: aload #7
    //   667: iconst_0
    //   668: aload_0
    //   669: getfield batchCommandIndex : I
    //   672: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   675: aload #5
    //   677: aload #7
    //   679: aload_0
    //   680: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   683: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   686: athrow
    //   687: aload_0
    //   688: iconst_m1
    //   689: putfield batchCommandIndex : I
    //   692: aload #6
    //   694: ifnull -> 714
    //   697: aload #6
    //   699: invokevirtual cancel : ()Z
    //   702: pop
    //   703: aload #10
    //   705: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   710: invokevirtual purge : ()I
    //   713: pop
    //   714: aload_0
    //   715: invokevirtual resetCancelledState : ()V
    //   718: aload #5
    //   720: athrow
    //   721: aload #5
    //   723: ifnull -> 729
    //   726: goto -> 734
    //   729: iconst_0
    //   730: newarray long
    //   732: astore #5
    //   734: aload #9
    //   736: monitorexit
    //   737: aload #5
    //   739: areturn
    //   740: astore #5
    //   742: aload #9
    //   744: monitorexit
    //   745: aload #5
    //   747: athrow
    // Exception table:
    //   from	to	target	type
    //   14	20	740	finally
    //   25	30	740	finally
    //   30	36	740	finally
    //   50	63	740	finally
    //   83	93	619	java/lang/NullPointerException
    //   83	93	614	finally
    //   97	122	619	java/lang/NullPointerException
    //   97	122	614	finally
    //   122	136	152	java/lang/NullPointerException
    //   122	136	139	finally
    //   172	178	601	java/lang/NullPointerException
    //   172	178	584	finally
    //   183	200	220	java/lang/NullPointerException
    //   183	200	203	finally
    //   237	242	601	java/lang/NullPointerException
    //   237	242	584	finally
    //   245	250	601	java/lang/NullPointerException
    //   245	250	584	finally
    //   255	267	220	java/lang/NullPointerException
    //   255	267	203	finally
    //   267	322	430	java/sql/SQLException
    //   267	322	220	java/lang/NullPointerException
    //   267	322	203	finally
    //   329	334	430	java/sql/SQLException
    //   329	334	220	java/lang/NullPointerException
    //   329	334	203	finally
    //   337	384	430	java/sql/SQLException
    //   337	384	220	java/lang/NullPointerException
    //   337	384	203	finally
    //   384	410	425	java/sql/SQLException
    //   384	410	220	java/lang/NullPointerException
    //   384	410	203	finally
    //   417	422	425	java/sql/SQLException
    //   417	422	220	java/lang/NullPointerException
    //   417	422	203	finally
    //   432	474	220	java/lang/NullPointerException
    //   432	474	203	finally
    //   474	484	220	java/lang/NullPointerException
    //   474	484	203	finally
    //   487	519	220	java/lang/NullPointerException
    //   487	519	203	finally
    //   524	529	740	finally
    //   534	551	740	finally
    //   551	555	740	finally
    //   562	574	579	java/lang/NullPointerException
    //   562	574	574	finally
    //   629	634	642	java/sql/SQLException
    //   629	634	637	finally
    //   634	637	637	finally
    //   644	687	637	finally
    //   687	692	740	finally
    //   697	714	740	finally
    //   714	721	740	finally
    //   729	734	740	finally
    //   734	737	740	finally
    //   742	745	740	finally
  }
  
  public long[] executeBatchedInserts(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #19
    //   11: aload #19
    //   13: monitorenter
    //   14: aload_0
    //   15: invokevirtual getValuesClause : ()Ljava/lang/String;
    //   18: astore #14
    //   20: aload_0
    //   21: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   24: astore #20
    //   26: aload #14
    //   28: ifnonnull -> 44
    //   31: aload_0
    //   32: iload_1
    //   33: invokevirtual executeBatchSerially : (I)[J
    //   36: astore #14
    //   38: aload #19
    //   40: monitorexit
    //   41: aload #14
    //   43: areturn
    //   44: aload_0
    //   45: getfield batchedArgs : Ljava/util/List;
    //   48: invokeinterface size : ()I
    //   53: istore_3
    //   54: aload_0
    //   55: getfield retrieveGeneratedKeys : Z
    //   58: ifeq -> 78
    //   61: new java/util/ArrayList
    //   64: astore #14
    //   66: aload #14
    //   68: iload_3
    //   69: invokespecial <init> : (I)V
    //   72: aload_0
    //   73: aload #14
    //   75: putfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   78: aload_0
    //   79: iload_3
    //   80: invokevirtual computeBatchSize : (I)I
    //   83: istore_2
    //   84: iload_2
    //   85: istore #4
    //   87: iload_3
    //   88: iload_2
    //   89: if_icmpge -> 95
    //   92: iload_3
    //   93: istore #4
    //   95: iload_3
    //   96: newarray long
    //   98: astore #21
    //   100: aload_0
    //   101: aload #20
    //   103: iload #4
    //   105: invokevirtual prepareBatchedInsertSQL : (Lcom/mysql/jdbc/MySQLConnection;I)Lcom/mysql/jdbc/PreparedStatement;
    //   108: astore #16
    //   110: aload #20
    //   112: invokeinterface getEnableQueryTimeouts : ()Z
    //   117: ifeq -> 171
    //   120: iload_1
    //   121: ifeq -> 171
    //   124: aload #20
    //   126: iconst_5
    //   127: iconst_0
    //   128: iconst_0
    //   129: invokeinterface versionMeetsMinimum : (III)Z
    //   134: ifeq -> 171
    //   137: new com/mysql/jdbc/StatementImpl$CancelTask
    //   140: astore #14
    //   142: aload #14
    //   144: aload_0
    //   145: aload #16
    //   147: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   150: aload #14
    //   152: astore #17
    //   154: aload #20
    //   156: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   161: aload #14
    //   163: iload_1
    //   164: i2l
    //   165: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   168: goto -> 174
    //   171: aconst_null
    //   172: astore #14
    //   174: iload_3
    //   175: iload #4
    //   177: if_icmpge -> 186
    //   180: iload_3
    //   181: istore #5
    //   183: goto -> 196
    //   186: aload #14
    //   188: astore #17
    //   190: iload_3
    //   191: iload #4
    //   193: idiv
    //   194: istore #5
    //   196: lconst_0
    //   197: lstore #12
    //   199: lconst_0
    //   200: lstore #8
    //   202: iconst_1
    //   203: istore_2
    //   204: iconst_0
    //   205: istore_1
    //   206: iconst_0
    //   207: istore #6
    //   209: aconst_null
    //   210: astore #15
    //   212: iload #6
    //   214: iload #5
    //   216: iload #4
    //   218: imul
    //   219: if_icmpge -> 369
    //   222: iload_2
    //   223: istore #7
    //   225: lload #8
    //   227: lstore #10
    //   229: aload #15
    //   231: astore #18
    //   233: iload #6
    //   235: ifeq -> 329
    //   238: iload_2
    //   239: istore #7
    //   241: lload #8
    //   243: lstore #10
    //   245: aload #15
    //   247: astore #18
    //   249: iload #6
    //   251: iload #4
    //   253: irem
    //   254: ifne -> 329
    //   257: aload #14
    //   259: astore #17
    //   261: aload #16
    //   263: invokevirtual executeLargeUpdate : ()J
    //   266: lstore #10
    //   268: lload #8
    //   270: lload #10
    //   272: ladd
    //   273: lstore #8
    //   275: goto -> 299
    //   278: astore #15
    //   280: aload #14
    //   282: astore #17
    //   284: aload_0
    //   285: iload_1
    //   286: iconst_1
    //   287: isub
    //   288: iload #4
    //   290: aload #21
    //   292: aload #15
    //   294: invokevirtual handleExceptionForBatch : (II[JLjava/sql/SQLException;)Ljava/sql/SQLException;
    //   297: astore #15
    //   299: aload #14
    //   301: astore #17
    //   303: aload_0
    //   304: aload #16
    //   306: invokevirtual getBatchedGeneratedKeys : (Ljava/sql/Statement;)V
    //   309: aload #14
    //   311: astore #17
    //   313: aload #16
    //   315: invokevirtual clearParameters : ()V
    //   318: iconst_1
    //   319: istore #7
    //   321: aload #15
    //   323: astore #18
    //   325: lload #8
    //   327: lstore #10
    //   329: aload #14
    //   331: astore #17
    //   333: aload_0
    //   334: aload #16
    //   336: iload #7
    //   338: aload_0
    //   339: getfield batchedArgs : Ljava/util/List;
    //   342: iload_1
    //   343: invokeinterface get : (I)Ljava/lang/Object;
    //   348: invokevirtual setOneBatchedParameterSet : (Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   351: istore_2
    //   352: iinc #6, 1
    //   355: iinc #1, 1
    //   358: lload #10
    //   360: lstore #8
    //   362: aload #18
    //   364: astore #15
    //   366: goto -> 212
    //   369: aload #14
    //   371: astore #17
    //   373: aload #16
    //   375: invokevirtual executeLargeUpdate : ()J
    //   378: lstore #10
    //   380: lload #8
    //   382: lload #10
    //   384: ladd
    //   385: lstore #8
    //   387: goto -> 411
    //   390: astore #15
    //   392: aload #14
    //   394: astore #17
    //   396: aload_0
    //   397: iload_1
    //   398: iconst_1
    //   399: isub
    //   400: iload #4
    //   402: aload #21
    //   404: aload #15
    //   406: invokevirtual handleExceptionForBatch : (II[JLjava/sql/SQLException;)Ljava/sql/SQLException;
    //   409: astore #15
    //   411: aload #14
    //   413: astore #17
    //   415: aload_0
    //   416: aload #16
    //   418: invokevirtual getBatchedGeneratedKeys : (Ljava/sql/Statement;)V
    //   421: iload_3
    //   422: iload_1
    //   423: isub
    //   424: istore #4
    //   426: aload #16
    //   428: ifnull -> 446
    //   431: aload #14
    //   433: astore #18
    //   435: aload #16
    //   437: invokevirtual close : ()V
    //   440: aconst_null
    //   441: astore #16
    //   443: goto -> 446
    //   446: lload #8
    //   448: lstore #10
    //   450: aload #16
    //   452: astore #17
    //   454: aload #15
    //   456: astore #18
    //   458: iload #4
    //   460: ifle -> 584
    //   463: aload_0
    //   464: aload #20
    //   466: iload #4
    //   468: invokevirtual prepareBatchedInsertSQL : (Lcom/mysql/jdbc/MySQLConnection;I)Lcom/mysql/jdbc/PreparedStatement;
    //   471: astore #17
    //   473: aload #14
    //   475: ifnull -> 497
    //   478: aload #14
    //   480: aload #17
    //   482: putfield toCancel : Lcom/mysql/jdbc/StatementImpl;
    //   485: goto -> 497
    //   488: astore #15
    //   490: aload #17
    //   492: astore #16
    //   494: goto -> 697
    //   497: iconst_1
    //   498: istore_2
    //   499: iload_1
    //   500: iload_3
    //   501: if_icmpge -> 528
    //   504: aload_0
    //   505: aload #17
    //   507: iload_2
    //   508: aload_0
    //   509: getfield batchedArgs : Ljava/util/List;
    //   512: iload_1
    //   513: invokeinterface get : (I)Ljava/lang/Object;
    //   518: invokevirtual setOneBatchedParameterSet : (Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   521: istore_2
    //   522: iinc #1, 1
    //   525: goto -> 499
    //   528: aload #17
    //   530: invokevirtual executeLargeUpdate : ()J
    //   533: lstore #10
    //   535: lload #8
    //   537: lload #10
    //   539: ladd
    //   540: lstore #8
    //   542: goto -> 562
    //   545: astore #15
    //   547: aload_0
    //   548: iload_1
    //   549: iconst_1
    //   550: isub
    //   551: iload #4
    //   553: aload #21
    //   555: aload #15
    //   557: invokevirtual handleExceptionForBatch : (II[JLjava/sql/SQLException;)Ljava/sql/SQLException;
    //   560: astore #15
    //   562: aload_0
    //   563: aload #17
    //   565: invokevirtual getBatchedGeneratedKeys : (Ljava/sql/Statement;)V
    //   568: lload #8
    //   570: lstore #10
    //   572: aload #15
    //   574: astore #18
    //   576: goto -> 584
    //   579: astore #15
    //   581: goto -> 697
    //   584: aload #18
    //   586: ifnonnull -> 681
    //   589: iload_3
    //   590: iconst_1
    //   591: if_icmple -> 629
    //   594: lload #12
    //   596: lstore #8
    //   598: lload #10
    //   600: lconst_0
    //   601: lcmp
    //   602: ifle -> 610
    //   605: ldc2_w -2
    //   608: lstore #8
    //   610: iconst_0
    //   611: istore_1
    //   612: iload_1
    //   613: iload_3
    //   614: if_icmpge -> 635
    //   617: aload #21
    //   619: iload_1
    //   620: lload #8
    //   622: lastore
    //   623: iinc #1, 1
    //   626: goto -> 612
    //   629: aload #21
    //   631: iconst_0
    //   632: lload #10
    //   634: lastore
    //   635: aload #17
    //   637: ifnull -> 649
    //   640: aload #14
    //   642: astore #18
    //   644: aload #17
    //   646: invokevirtual close : ()V
    //   649: aload #14
    //   651: ifnull -> 671
    //   654: aload #14
    //   656: invokevirtual cancel : ()Z
    //   659: pop
    //   660: aload #20
    //   662: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   667: invokevirtual purge : ()I
    //   670: pop
    //   671: aload_0
    //   672: invokevirtual resetCancelledState : ()V
    //   675: aload #19
    //   677: monitorexit
    //   678: aload #21
    //   680: areturn
    //   681: aload #17
    //   683: astore #16
    //   685: aload #18
    //   687: aload #21
    //   689: aload_0
    //   690: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   693: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   696: athrow
    //   697: aload #16
    //   699: ifnull -> 711
    //   702: aload #14
    //   704: astore #18
    //   706: aload #16
    //   708: invokevirtual close : ()V
    //   711: aload #14
    //   713: astore #18
    //   715: aload #15
    //   717: athrow
    //   718: astore #14
    //   720: aload #16
    //   722: astore #15
    //   724: goto -> 744
    //   727: astore #14
    //   729: aload #16
    //   731: astore #15
    //   733: goto -> 741
    //   736: astore #14
    //   738: aconst_null
    //   739: astore #15
    //   741: aconst_null
    //   742: astore #17
    //   744: aload #15
    //   746: ifnull -> 766
    //   749: aload #17
    //   751: astore #18
    //   753: aload #15
    //   755: invokevirtual close : ()V
    //   758: goto -> 766
    //   761: astore #14
    //   763: goto -> 773
    //   766: aload #17
    //   768: astore #18
    //   770: aload #14
    //   772: athrow
    //   773: aload #18
    //   775: ifnull -> 795
    //   778: aload #18
    //   780: invokevirtual cancel : ()Z
    //   783: pop
    //   784: aload #20
    //   786: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   791: invokevirtual purge : ()I
    //   794: pop
    //   795: aload_0
    //   796: invokevirtual resetCancelledState : ()V
    //   799: aload #14
    //   801: athrow
    //   802: astore #14
    //   804: aload #19
    //   806: monitorexit
    //   807: aload #14
    //   809: athrow
    // Exception table:
    //   from	to	target	type
    //   14	26	802	finally
    //   31	41	802	finally
    //   44	78	802	finally
    //   78	84	802	finally
    //   95	100	802	finally
    //   100	110	736	finally
    //   110	120	727	finally
    //   124	150	727	finally
    //   154	168	718	finally
    //   190	196	718	finally
    //   261	268	278	java/sql/SQLException
    //   261	268	718	finally
    //   284	299	718	finally
    //   303	309	718	finally
    //   313	318	718	finally
    //   333	352	718	finally
    //   373	380	390	java/sql/SQLException
    //   373	380	718	finally
    //   396	411	718	finally
    //   415	421	718	finally
    //   435	440	761	finally
    //   463	473	579	finally
    //   478	485	488	finally
    //   504	522	488	finally
    //   528	535	545	java/sql/SQLException
    //   528	535	488	finally
    //   547	562	488	finally
    //   562	568	488	finally
    //   644	649	761	finally
    //   654	671	802	finally
    //   671	678	802	finally
    //   685	697	579	finally
    //   706	711	761	finally
    //   715	718	761	finally
    //   753	758	761	finally
    //   770	773	761	finally
    //   778	795	802	finally
    //   795	802	802	finally
    //   804	807	802	finally
  }
  
  public ResultSetInternalMethods executeInternal(int paramInt, Buffer paramBuffer, boolean paramBoolean1, boolean paramBoolean2, Field[] paramArrayOfField, boolean paramBoolean3) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #11
    //   11: aload #11
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   18: astore #12
    //   20: aload_0
    //   21: aload_0
    //   22: getfield numberOfExecutions : I
    //   25: iconst_1
    //   26: iadd
    //   27: putfield numberOfExecutions : I
    //   30: aload #12
    //   32: invokeinterface getEnableQueryTimeouts : ()Z
    //   37: ifeq -> 100
    //   40: aload_0
    //   41: getfield timeoutInMillis : I
    //   44: ifeq -> 100
    //   47: aload #12
    //   49: iconst_5
    //   50: iconst_0
    //   51: iconst_0
    //   52: invokeinterface versionMeetsMinimum : (III)Z
    //   57: ifeq -> 100
    //   60: new com/mysql/jdbc/StatementImpl$CancelTask
    //   63: astore #9
    //   65: aload #9
    //   67: aload_0
    //   68: aload_0
    //   69: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   72: aload #12
    //   74: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   79: aload #9
    //   81: aload_0
    //   82: getfield timeoutInMillis : I
    //   85: i2l
    //   86: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   89: goto -> 103
    //   92: astore_2
    //   93: aload #9
    //   95: astore #5
    //   97: goto -> 351
    //   100: aconst_null
    //   101: astore #9
    //   103: iload #6
    //   105: ifne -> 112
    //   108: aload_0
    //   109: invokevirtual statementBegins : ()V
    //   112: aload_0
    //   113: getfield resultSetType : I
    //   116: istore #8
    //   118: aload_0
    //   119: getfield resultSetConcurrency : I
    //   122: istore #7
    //   124: aload_0
    //   125: getfield currentCatalog : Ljava/lang/String;
    //   128: astore #13
    //   130: aload #9
    //   132: astore #10
    //   134: aload #10
    //   136: astore #9
    //   138: aload #12
    //   140: aload_0
    //   141: aconst_null
    //   142: iload_1
    //   143: aload_2
    //   144: iload #8
    //   146: iload #7
    //   148: iload_3
    //   149: aload #13
    //   151: aload #5
    //   153: iload #6
    //   155: invokeinterface execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   160: astore #5
    //   162: aload #10
    //   164: astore_2
    //   165: aload #10
    //   167: ifnull -> 220
    //   170: aload #10
    //   172: astore #9
    //   174: aload #10
    //   176: invokevirtual cancel : ()Z
    //   179: pop
    //   180: aload #10
    //   182: astore #9
    //   184: aload #12
    //   186: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   191: invokevirtual purge : ()I
    //   194: pop
    //   195: aload #10
    //   197: astore #9
    //   199: aload #10
    //   201: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   204: astore_2
    //   205: aload_2
    //   206: ifnonnull -> 214
    //   209: aconst_null
    //   210: astore_2
    //   211: goto -> 220
    //   214: aload #10
    //   216: astore #9
    //   218: aload_2
    //   219: athrow
    //   220: aload_2
    //   221: astore #9
    //   223: aload_0
    //   224: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   227: astore #10
    //   229: aload_2
    //   230: astore #9
    //   232: aload #10
    //   234: monitorenter
    //   235: aload_0
    //   236: getfield wasCancelled : Z
    //   239: ifeq -> 278
    //   242: aload_0
    //   243: getfield wasCancelledByTimeout : Z
    //   246: ifeq -> 262
    //   249: new com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   252: astore #5
    //   254: aload #5
    //   256: invokespecial <init> : ()V
    //   259: goto -> 271
    //   262: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   265: dup
    //   266: invokespecial <init> : ()V
    //   269: astore #5
    //   271: aload_0
    //   272: invokevirtual resetCancelledState : ()V
    //   275: aload #5
    //   277: athrow
    //   278: aload #10
    //   280: monitorexit
    //   281: iload #6
    //   283: ifne -> 294
    //   286: aload_0
    //   287: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   290: iconst_0
    //   291: invokevirtual set : (Z)V
    //   294: aload_2
    //   295: ifnull -> 314
    //   298: aload_2
    //   299: invokevirtual cancel : ()Z
    //   302: pop
    //   303: aload #12
    //   305: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   310: invokevirtual purge : ()I
    //   313: pop
    //   314: aload #11
    //   316: monitorexit
    //   317: aload #5
    //   319: areturn
    //   320: astore #5
    //   322: aload #10
    //   324: monitorexit
    //   325: aload #5
    //   327: athrow
    //   328: astore #9
    //   330: goto -> 364
    //   333: astore #5
    //   335: goto -> 322
    //   338: astore_2
    //   339: aload #9
    //   341: astore #5
    //   343: goto -> 351
    //   346: astore_2
    //   347: aload #9
    //   349: astore #5
    //   351: aload_2
    //   352: astore #9
    //   354: aload #5
    //   356: astore_2
    //   357: goto -> 364
    //   360: astore #9
    //   362: aconst_null
    //   363: astore_2
    //   364: iload #6
    //   366: ifne -> 377
    //   369: aload_0
    //   370: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   373: iconst_0
    //   374: invokevirtual set : (Z)V
    //   377: aload_2
    //   378: ifnull -> 397
    //   381: aload_2
    //   382: invokevirtual cancel : ()Z
    //   385: pop
    //   386: aload #12
    //   388: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   393: invokevirtual purge : ()I
    //   396: pop
    //   397: aload #9
    //   399: athrow
    //   400: astore_2
    //   401: goto -> 412
    //   404: astore_2
    //   405: aload_0
    //   406: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   409: pop
    //   410: aload_2
    //   411: athrow
    //   412: aload #11
    //   414: monitorexit
    //   415: aload_2
    //   416: athrow
    // Exception table:
    //   from	to	target	type
    //   14	30	404	java/lang/NullPointerException
    //   14	30	400	finally
    //   30	72	360	finally
    //   72	89	92	finally
    //   108	112	346	finally
    //   112	130	346	finally
    //   138	162	338	finally
    //   174	180	338	finally
    //   184	195	338	finally
    //   199	205	338	finally
    //   218	220	338	finally
    //   223	229	338	finally
    //   232	235	338	finally
    //   235	259	320	finally
    //   262	271	320	finally
    //   271	278	320	finally
    //   278	281	320	finally
    //   286	294	404	java/lang/NullPointerException
    //   286	294	400	finally
    //   298	314	404	java/lang/NullPointerException
    //   298	314	400	finally
    //   314	317	400	finally
    //   322	325	333	finally
    //   325	328	328	finally
    //   369	377	404	java/lang/NullPointerException
    //   369	377	400	finally
    //   381	397	404	java/lang/NullPointerException
    //   381	397	400	finally
    //   397	400	404	java/lang/NullPointerException
    //   397	400	400	finally
    //   405	412	400	finally
    //   412	415	400	finally
  }
  
  public long executeLargeUpdate() throws SQLException {
    return executeUpdateInternal(true, false);
  }
  
  public long[] executePreparedBatchAsMultiStatement(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #18
    //   11: aload #18
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield batchedValuesClause : Ljava/lang/String;
    //   18: ifnonnull -> 59
    //   21: new java/lang/StringBuilder
    //   24: astore #12
    //   26: aload #12
    //   28: invokespecial <init> : ()V
    //   31: aload #12
    //   33: aload_0
    //   34: getfield originalSql : Ljava/lang/String;
    //   37: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: pop
    //   41: aload #12
    //   43: ldc_w ';'
    //   46: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: pop
    //   50: aload_0
    //   51: aload #12
    //   53: invokevirtual toString : ()Ljava/lang/String;
    //   56: putfield batchedValuesClause : Ljava/lang/String;
    //   59: aload_0
    //   60: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   63: astore #19
    //   65: aload #19
    //   67: invokeinterface getAllowMultiQueries : ()Z
    //   72: istore #11
    //   74: aload_0
    //   75: invokevirtual clearWarnings : ()V
    //   78: aload_0
    //   79: getfield batchedArgs : Ljava/util/List;
    //   82: invokeinterface size : ()I
    //   87: istore_3
    //   88: aload_0
    //   89: getfield retrieveGeneratedKeys : Z
    //   92: ifeq -> 112
    //   95: new java/util/ArrayList
    //   98: astore #12
    //   100: aload #12
    //   102: iload_3
    //   103: invokespecial <init> : (I)V
    //   106: aload_0
    //   107: aload #12
    //   109: putfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   112: aload_0
    //   113: iload_3
    //   114: invokevirtual computeBatchSize : (I)I
    //   117: istore_2
    //   118: iload_2
    //   119: istore #4
    //   121: iload_3
    //   122: iload_2
    //   123: if_icmpge -> 129
    //   126: iload_3
    //   127: istore #4
    //   129: aload_0
    //   130: getfield parseInfo : Lcom/mysql/jdbc/PreparedStatement$ParseInfo;
    //   133: getfield numberOfQueries : I
    //   136: iload_3
    //   137: imul
    //   138: newarray long
    //   140: astore #20
    //   142: iload #11
    //   144: ifne -> 157
    //   147: aload #19
    //   149: invokeinterface getIO : ()Lcom/mysql/jdbc/MysqlIO;
    //   154: invokevirtual enableMultiQueries : ()V
    //   157: aload_0
    //   158: getfield retrieveGeneratedKeys : Z
    //   161: ifeq -> 196
    //   164: aload #19
    //   166: aload_0
    //   167: iload #4
    //   169: invokespecial generateMultiStatementForBatch : (I)Ljava/lang/String;
    //   172: iconst_1
    //   173: invokeinterface prepareStatement : (Ljava/lang/String;I)Ljava/sql/PreparedStatement;
    //   178: checkcast com/mysql/jdbc/Wrapper
    //   181: ldc java/sql/PreparedStatement
    //   183: invokeinterface unwrap : (Ljava/lang/Class;)Ljava/lang/Object;
    //   188: checkcast java/sql/PreparedStatement
    //   191: astore #13
    //   193: goto -> 224
    //   196: aload #19
    //   198: aload_0
    //   199: iload #4
    //   201: invokespecial generateMultiStatementForBatch : (I)Ljava/lang/String;
    //   204: invokeinterface prepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   209: checkcast com/mysql/jdbc/Wrapper
    //   212: ldc java/sql/PreparedStatement
    //   214: invokeinterface unwrap : (Ljava/lang/Class;)Ljava/lang/Object;
    //   219: checkcast java/sql/PreparedStatement
    //   222: astore #13
    //   224: aload #19
    //   226: invokeinterface getEnableQueryTimeouts : ()Z
    //   231: istore #10
    //   233: iconst_0
    //   234: istore #6
    //   236: iload #10
    //   238: ifeq -> 295
    //   241: iload_1
    //   242: ifeq -> 295
    //   245: aload #19
    //   247: iconst_5
    //   248: iconst_0
    //   249: iconst_0
    //   250: invokeinterface versionMeetsMinimum : (III)Z
    //   255: ifeq -> 295
    //   258: new com/mysql/jdbc/StatementImpl$CancelTask
    //   261: astore #12
    //   263: aload #12
    //   265: aload_0
    //   266: aload #13
    //   268: checkcast com/mysql/jdbc/StatementImpl
    //   271: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   274: aload #12
    //   276: astore #14
    //   278: aload #19
    //   280: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   285: aload #12
    //   287: iload_1
    //   288: i2l
    //   289: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   292: goto -> 298
    //   295: aconst_null
    //   296: astore #12
    //   298: iload_3
    //   299: iload #4
    //   301: if_icmpge -> 310
    //   304: iload_3
    //   305: istore #5
    //   307: goto -> 320
    //   310: aload #12
    //   312: astore #14
    //   314: iload_3
    //   315: iload #4
    //   317: idiv
    //   318: istore #5
    //   320: iconst_1
    //   321: istore_2
    //   322: iconst_0
    //   323: istore_1
    //   324: iconst_0
    //   325: istore #8
    //   327: aconst_null
    //   328: astore #15
    //   330: iload #6
    //   332: iload #5
    //   334: iload #4
    //   336: imul
    //   337: if_icmpge -> 486
    //   340: iload_2
    //   341: istore #9
    //   343: iload #8
    //   345: istore #7
    //   347: aload #15
    //   349: astore #16
    //   351: iload #6
    //   353: ifeq -> 446
    //   356: iload_2
    //   357: istore #9
    //   359: iload #8
    //   361: istore #7
    //   363: aload #15
    //   365: astore #16
    //   367: iload #6
    //   369: iload #4
    //   371: irem
    //   372: ifne -> 446
    //   375: aload #12
    //   377: astore #14
    //   379: aload #13
    //   381: invokeinterface execute : ()Z
    //   386: pop
    //   387: goto -> 409
    //   390: astore #15
    //   392: aload #12
    //   394: astore #14
    //   396: aload_0
    //   397: iload_1
    //   398: iload #4
    //   400: aload #20
    //   402: aload #15
    //   404: invokevirtual handleExceptionForBatch : (II[JLjava/sql/SQLException;)Ljava/sql/SQLException;
    //   407: astore #15
    //   409: aload #12
    //   411: astore #14
    //   413: aload_0
    //   414: aload #13
    //   416: checkcast com/mysql/jdbc/StatementImpl
    //   419: iload #8
    //   421: aload #20
    //   423: invokevirtual processMultiCountsAndKeys : (Lcom/mysql/jdbc/StatementImpl;I[J)I
    //   426: istore #7
    //   428: aload #12
    //   430: astore #14
    //   432: aload #13
    //   434: invokeinterface clearParameters : ()V
    //   439: iconst_1
    //   440: istore #9
    //   442: aload #15
    //   444: astore #16
    //   446: aload #12
    //   448: astore #14
    //   450: aload_0
    //   451: aload #13
    //   453: iload #9
    //   455: aload_0
    //   456: getfield batchedArgs : Ljava/util/List;
    //   459: iload_1
    //   460: invokeinterface get : (I)Ljava/lang/Object;
    //   465: invokevirtual setOneBatchedParameterSet : (Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   468: istore_2
    //   469: iinc #6, 1
    //   472: iinc #1, 1
    //   475: iload #7
    //   477: istore #8
    //   479: aload #16
    //   481: astore #15
    //   483: goto -> 330
    //   486: aload #12
    //   488: astore #14
    //   490: aload #13
    //   492: invokeinterface execute : ()Z
    //   497: pop
    //   498: goto -> 522
    //   501: astore #15
    //   503: aload #12
    //   505: astore #14
    //   507: aload_0
    //   508: iload_1
    //   509: iconst_1
    //   510: isub
    //   511: iload #4
    //   513: aload #20
    //   515: aload #15
    //   517: invokevirtual handleExceptionForBatch : (II[JLjava/sql/SQLException;)Ljava/sql/SQLException;
    //   520: astore #15
    //   522: aload #12
    //   524: astore #14
    //   526: aload_0
    //   527: aload #13
    //   529: checkcast com/mysql/jdbc/StatementImpl
    //   532: iload #8
    //   534: aload #20
    //   536: invokevirtual processMultiCountsAndKeys : (Lcom/mysql/jdbc/StatementImpl;I[J)I
    //   539: istore #5
    //   541: aload #12
    //   543: astore #14
    //   545: aload #13
    //   547: invokeinterface clearParameters : ()V
    //   552: iload_3
    //   553: iload_1
    //   554: isub
    //   555: istore #4
    //   557: aload #13
    //   559: astore #16
    //   561: aload #13
    //   563: ifnull -> 580
    //   566: aload #12
    //   568: astore #14
    //   570: aload #13
    //   572: invokeinterface close : ()V
    //   577: aconst_null
    //   578: astore #16
    //   580: aload #16
    //   582: astore #14
    //   584: aload #15
    //   586: astore #17
    //   588: iload #4
    //   590: ifle -> 781
    //   593: aload #16
    //   595: astore #13
    //   597: aload_0
    //   598: getfield retrieveGeneratedKeys : Z
    //   601: ifeq -> 627
    //   604: aload #16
    //   606: astore #13
    //   608: aload #19
    //   610: aload_0
    //   611: iload #4
    //   613: invokespecial generateMultiStatementForBatch : (I)Ljava/lang/String;
    //   616: iconst_1
    //   617: invokeinterface prepareStatement : (Ljava/lang/String;I)Ljava/sql/PreparedStatement;
    //   622: astore #14
    //   624: goto -> 646
    //   627: aload #16
    //   629: astore #13
    //   631: aload #19
    //   633: aload_0
    //   634: iload #4
    //   636: invokespecial generateMultiStatementForBatch : (I)Ljava/lang/String;
    //   639: invokeinterface prepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   644: astore #14
    //   646: aload #12
    //   648: ifnull -> 665
    //   651: aload #14
    //   653: astore #13
    //   655: aload #12
    //   657: aload #14
    //   659: checkcast com/mysql/jdbc/StatementImpl
    //   662: putfield toCancel : Lcom/mysql/jdbc/StatementImpl;
    //   665: iconst_1
    //   666: istore_2
    //   667: iload_1
    //   668: iload_3
    //   669: if_icmpge -> 700
    //   672: aload #14
    //   674: astore #13
    //   676: aload_0
    //   677: aload #14
    //   679: iload_2
    //   680: aload_0
    //   681: getfield batchedArgs : Ljava/util/List;
    //   684: iload_1
    //   685: invokeinterface get : (I)Ljava/lang/Object;
    //   690: invokevirtual setOneBatchedParameterSet : (Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   693: istore_2
    //   694: iinc #1, 1
    //   697: goto -> 667
    //   700: aload #14
    //   702: astore #13
    //   704: aload #14
    //   706: invokeinterface execute : ()Z
    //   711: pop
    //   712: goto -> 736
    //   715: astore #15
    //   717: aload #14
    //   719: astore #13
    //   721: aload_0
    //   722: iload_1
    //   723: iconst_1
    //   724: isub
    //   725: iload #4
    //   727: aload #20
    //   729: aload #15
    //   731: invokevirtual handleExceptionForBatch : (II[JLjava/sql/SQLException;)Ljava/sql/SQLException;
    //   734: astore #15
    //   736: aload #14
    //   738: astore #13
    //   740: aload_0
    //   741: aload #14
    //   743: checkcast com/mysql/jdbc/StatementImpl
    //   746: iload #5
    //   748: aload #20
    //   750: invokevirtual processMultiCountsAndKeys : (Lcom/mysql/jdbc/StatementImpl;I[J)I
    //   753: pop
    //   754: aload #14
    //   756: astore #13
    //   758: aload #14
    //   760: invokeinterface clearParameters : ()V
    //   765: aload #15
    //   767: astore #17
    //   769: goto -> 781
    //   772: astore #15
    //   774: aload #13
    //   776: astore #14
    //   778: goto -> 934
    //   781: aload #12
    //   783: ifnull -> 840
    //   786: aload #14
    //   788: astore #13
    //   790: aload #12
    //   792: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   795: astore #15
    //   797: aload #15
    //   799: ifnonnull -> 833
    //   802: aload #14
    //   804: astore #13
    //   806: aload #12
    //   808: invokevirtual cancel : ()Z
    //   811: pop
    //   812: aload #14
    //   814: astore #13
    //   816: aload #19
    //   818: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   823: invokevirtual purge : ()I
    //   826: pop
    //   827: aconst_null
    //   828: astore #12
    //   830: goto -> 840
    //   833: aload #14
    //   835: astore #13
    //   837: aload #15
    //   839: athrow
    //   840: aload #17
    //   842: ifnonnull -> 920
    //   845: aload #14
    //   847: ifnull -> 869
    //   850: aload #12
    //   852: astore #13
    //   854: aload #14
    //   856: invokeinterface close : ()V
    //   861: goto -> 869
    //   864: astore #12
    //   866: goto -> 1039
    //   869: aload #12
    //   871: ifnull -> 891
    //   874: aload #12
    //   876: invokevirtual cancel : ()Z
    //   879: pop
    //   880: aload #19
    //   882: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   887: invokevirtual purge : ()I
    //   890: pop
    //   891: aload_0
    //   892: invokevirtual resetCancelledState : ()V
    //   895: iload #11
    //   897: ifne -> 910
    //   900: aload #19
    //   902: invokeinterface getIO : ()Lcom/mysql/jdbc/MysqlIO;
    //   907: invokevirtual disableMultiQueries : ()V
    //   910: aload_0
    //   911: invokevirtual clearBatch : ()V
    //   914: aload #18
    //   916: monitorexit
    //   917: aload #20
    //   919: areturn
    //   920: aload #17
    //   922: aload #20
    //   924: aload_0
    //   925: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   928: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   931: athrow
    //   932: astore #15
    //   934: aload #14
    //   936: ifnull -> 950
    //   939: aload #12
    //   941: astore #13
    //   943: aload #14
    //   945: invokeinterface close : ()V
    //   950: aload #12
    //   952: astore #13
    //   954: aload #15
    //   956: athrow
    //   957: astore #12
    //   959: aload #13
    //   961: astore #15
    //   963: aload #12
    //   965: astore #13
    //   967: aload #14
    //   969: astore #12
    //   971: goto -> 999
    //   974: astore #12
    //   976: aload #13
    //   978: astore #14
    //   980: aload #12
    //   982: astore #13
    //   984: goto -> 992
    //   987: astore #13
    //   989: aconst_null
    //   990: astore #14
    //   992: aconst_null
    //   993: astore #12
    //   995: aload #14
    //   997: astore #15
    //   999: aload #15
    //   1001: ifnull -> 1027
    //   1004: aload #12
    //   1006: astore #14
    //   1008: aload #15
    //   1010: invokeinterface close : ()V
    //   1015: goto -> 1027
    //   1018: astore #12
    //   1020: aload #14
    //   1022: astore #13
    //   1024: goto -> 1039
    //   1027: aload #12
    //   1029: astore #14
    //   1031: aload #13
    //   1033: athrow
    //   1034: astore #12
    //   1036: aconst_null
    //   1037: astore #13
    //   1039: aload #13
    //   1041: ifnull -> 1061
    //   1044: aload #13
    //   1046: invokevirtual cancel : ()Z
    //   1049: pop
    //   1050: aload #19
    //   1052: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   1057: invokevirtual purge : ()I
    //   1060: pop
    //   1061: aload_0
    //   1062: invokevirtual resetCancelledState : ()V
    //   1065: iload #11
    //   1067: ifne -> 1080
    //   1070: aload #19
    //   1072: invokeinterface getIO : ()Lcom/mysql/jdbc/MysqlIO;
    //   1077: invokevirtual disableMultiQueries : ()V
    //   1080: aload_0
    //   1081: invokevirtual clearBatch : ()V
    //   1084: aload #12
    //   1086: athrow
    //   1087: astore #12
    //   1089: aload #18
    //   1091: monitorexit
    //   1092: aload #12
    //   1094: athrow
    // Exception table:
    //   from	to	target	type
    //   14	59	1087	finally
    //   59	74	1087	finally
    //   74	112	1034	finally
    //   112	118	1034	finally
    //   129	142	1034	finally
    //   147	157	987	finally
    //   157	193	987	finally
    //   196	224	987	finally
    //   224	233	974	finally
    //   245	274	974	finally
    //   278	292	957	finally
    //   314	320	957	finally
    //   379	387	390	java/sql/SQLException
    //   379	387	957	finally
    //   396	409	957	finally
    //   413	428	957	finally
    //   432	439	957	finally
    //   450	469	957	finally
    //   490	498	501	java/sql/SQLException
    //   490	498	957	finally
    //   507	522	957	finally
    //   526	541	957	finally
    //   545	552	957	finally
    //   570	577	1018	finally
    //   597	604	772	finally
    //   608	624	772	finally
    //   631	646	772	finally
    //   655	665	772	finally
    //   676	694	772	finally
    //   704	712	715	java/sql/SQLException
    //   704	712	772	finally
    //   721	736	772	finally
    //   740	754	772	finally
    //   758	765	772	finally
    //   790	797	772	finally
    //   806	812	772	finally
    //   816	827	772	finally
    //   837	840	772	finally
    //   854	861	864	finally
    //   874	891	1087	finally
    //   891	895	1087	finally
    //   900	910	1087	finally
    //   910	917	1087	finally
    //   920	932	932	finally
    //   943	950	864	finally
    //   954	957	864	finally
    //   1008	1015	1018	finally
    //   1031	1034	1018	finally
    //   1044	1061	1087	finally
    //   1061	1065	1087	finally
    //   1070	1080	1087	finally
    //   1080	1087	1087	finally
    //   1089	1092	1087	finally
  }
  
  public ResultSet executeQuery() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      String str;
      CachedResultSetMetaData cachedResultSetMetaData;
      Field[] arrayOfField;
      MySQLConnection mySQLConnection = this.connection;
      checkForDml(this.originalSql, this.firstCharOfStmt);
      this.batchedGeneratedKeys = null;
      resetCancelledState();
      implicitlyCloseAllOpenResults();
      clearWarnings();
      if (this.doPingInstead) {
        doPingInstead();
        str = (String)this.results;
        return (ResultSet)str;
      } 
      setupStreamingTimeout(mySQLConnection);
      Buffer buffer = fillSendPacket();
      if (!mySQLConnection.getCatalog().equals(this.currentCatalog)) {
        str = mySQLConnection.getCatalog();
        mySQLConnection.setCatalog(this.currentCatalog);
      } else {
        str = null;
      } 
      if (mySQLConnection.getCacheResultSetMetadata()) {
        cachedResultSetMetaData = mySQLConnection.getCachedMetaData(this.originalSql);
      } else {
        cachedResultSetMetaData = null;
      } 
      if (cachedResultSetMetaData != null) {
        arrayOfField = cachedResultSetMetaData.fields;
      } else {
        arrayOfField = null;
      } 
      mySQLConnection.setSessionMaxRows(this.maxRows);
      this.results = executeInternal(this.maxRows, buffer, createStreamingResultSet(), true, arrayOfField, false);
      if (str != null)
        mySQLConnection.setCatalog(str); 
      if (cachedResultSetMetaData != null) {
        mySQLConnection.initializeResultsMetadataFromCache(this.originalSql, cachedResultSetMetaData, this.results);
      } else if (mySQLConnection.getCacheResultSetMetadata()) {
        mySQLConnection.initializeResultsMetadataFromCache(this.originalSql, null, this.results);
      } 
      this.lastInsertId = this.results.getUpdateID();
      return this.results;
    } 
  }
  
  public int executeUpdate() throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeUpdate());
  }
  
  public long executeUpdateInternal(boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #6
    //   11: aload #6
    //   13: monitorenter
    //   14: iload_1
    //   15: ifeq -> 27
    //   18: aload_0
    //   19: invokevirtual clearWarnings : ()V
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   27: aload_0
    //   28: aload_0
    //   29: getfield parameterValues : [[B
    //   32: aload_0
    //   33: getfield parameterStreams : [Ljava/io/InputStream;
    //   36: aload_0
    //   37: getfield isStream : [Z
    //   40: aload_0
    //   41: getfield streamLengths : [I
    //   44: aload_0
    //   45: getfield isNull : [Z
    //   48: iload_2
    //   49: invokevirtual executeUpdateInternal : ([[B[Ljava/io/InputStream;[Z[I[ZZ)J
    //   52: lstore_3
    //   53: aload #6
    //   55: monitorexit
    //   56: lload_3
    //   57: lreturn
    //   58: astore #5
    //   60: aload #6
    //   62: monitorexit
    //   63: aload #5
    //   65: athrow
    // Exception table:
    //   from	to	target	type
    //   18	27	58	finally
    //   27	56	58	finally
    //   60	63	58	finally
  }
  
  public long executeUpdateInternal(byte[][] paramArrayOfbyte, InputStream[] paramArrayOfInputStream, boolean[] paramArrayOfboolean1, int[] paramArrayOfint, boolean[] paramArrayOfboolean2, boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      MySQLConnection mySQLConnection = this.connection;
      if (!mySQLConnection.isReadOnly(false)) {
        if (this.firstCharOfStmt != 'S' || !isSelectQuery()) {
          String str;
          boolean bool;
          resetCancelledState();
          implicitlyCloseAllOpenResults();
          Buffer buffer = fillSendPacket(paramArrayOfbyte, paramArrayOfInputStream, paramArrayOfboolean1, paramArrayOfint);
          paramArrayOfbyte = null;
          if (!mySQLConnection.getCatalog().equals(this.currentCatalog)) {
            str = mySQLConnection.getCatalog();
            mySQLConnection.setCatalog(this.currentCatalog);
          } 
          mySQLConnection.setSessionMaxRows(-1);
          if (this.retrieveGeneratedKeys) {
            bool = mySQLConnection.isReadInfoMsgEnabled();
            mySQLConnection.setReadInfoMsgEnabled(true);
          } else {
            bool = false;
          } 
          ResultSetInternalMethods resultSetInternalMethods = executeInternal(-1, buffer, false, false, null, paramBoolean);
          if (this.retrieveGeneratedKeys) {
            mySQLConnection.setReadInfoMsgEnabled(bool);
            resultSetInternalMethods.setFirstCharOfQuery(this.firstCharOfStmt);
          } 
          if (str != null)
            mySQLConnection.setCatalog(str); 
          this.results = resultSetInternalMethods;
          this.updateCount = resultSetInternalMethods.getUpdateCount();
          if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate) {
            long l = this.updateCount;
            if (l == 2L || l == 0L)
              this.updateCount = 1L; 
          } 
          this.lastInsertId = resultSetInternalMethods.getUpdateID();
          return this.updateCount;
        } 
        throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", getExceptionInterceptor());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(Messages.getString("PreparedStatement.34"));
      stringBuilder.append(Messages.getString("PreparedStatement.35"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public Buffer fillSendPacket() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths);
    } 
  }
  
  public Buffer fillSendPacket(byte[][] paramArrayOfbyte, InputStream[] paramArrayOfInputStream, boolean[] paramArrayOfboolean, int[] paramArrayOfint) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      byte[] arrayOfByte;
      Buffer buffer = this.connection.getIO().getSharedSendPacket();
      buffer.clear();
      buffer.writeByte((byte)3);
      boolean bool = this.connection.getUseStreamLengthsInPrepStmts();
      String str = this.connection.getStatementComment();
      SingleByteCharsetConverter singleByteCharsetConverter = null;
      if (str != null) {
        singleByteCharsetConverter = this.charConverter;
        if (singleByteCharsetConverter != null) {
          arrayOfByte = singleByteCharsetConverter.toBytes(str);
        } else {
          arrayOfByte = StringUtils.getBytes(str, (SingleByteCharsetConverter)arrayOfByte, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
        } 
        i = arrayOfByte.length + 0 + 6;
      } else {
        i = 0;
      } 
      byte b = 0;
      int j;
      for (j = i; b < paramArrayOfbyte.length; j = i) {
        i = j;
        if (paramArrayOfboolean[b]) {
          i = j;
          if (bool)
            i = j + paramArrayOfint[b]; 
        } 
        b++;
      } 
      if (j != 0)
        buffer.ensureCapacity(j); 
      if (arrayOfByte != null) {
        buffer.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
        buffer.writeBytesNoNull(arrayOfByte);
        buffer.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
      } 
      for (int i = 0; i < paramArrayOfbyte.length; i++) {
        checkAllParametersSet(paramArrayOfbyte[i], paramArrayOfInputStream[i], i);
        buffer.writeBytesNoNull(this.staticSqlStrings[i]);
        if (paramArrayOfboolean[i]) {
          streamToBytes(buffer, paramArrayOfInputStream[i], true, paramArrayOfint[i], bool);
        } else {
          buffer.writeBytesNoNull(paramArrayOfbyte[i]);
        } 
      } 
      buffer.writeBytesNoNull(this.staticSqlStrings[paramArrayOfbyte.length]);
      return buffer;
    } 
  }
  
  public byte[] getBytesRepresentation(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.isStream[paramInt])
        return streamToBytes(this.parameterStreams[paramInt], false, this.streamLengths[paramInt], this.connection.getUseStreamLengthsInPrepStmts()); 
      byte[] arrayOfByte = this.parameterValues[paramInt];
      if (arrayOfByte == null)
        return null; 
      if (arrayOfByte[0] == 39 && arrayOfByte[arrayOfByte.length - 1] == 39) {
        byte[] arrayOfByte1 = new byte[arrayOfByte.length - 2];
        System.arraycopy(arrayOfByte, 1, arrayOfByte1, 0, arrayOfByte.length - 2);
        return arrayOfByte1;
      } 
      return arrayOfByte;
    } 
  }
  
  public byte[] getBytesRepresentationForBatch(int paramInt1, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Object object = this.batchedArgs.get(paramInt2);
      boolean bool = object instanceof String;
      if (bool)
        try {
          object = StringUtils.getBytes((String)object, this.charEncoding);
          return (byte[])object;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          object = new RuntimeException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append(Messages.getString("PreparedStatement.32"));
          stringBuilder.append(this.charEncoding);
          stringBuilder.append(Messages.getString("PreparedStatement.33"));
          this(stringBuilder.toString());
          throw object;
        }  
      object = object;
      if (((BatchParams)object).isStream[paramInt1]) {
        object = streamToBytes(((BatchParams)object).parameterStreams[paramInt1], false, ((BatchParams)object).streamLengths[paramInt1], this.connection.getUseStreamLengthsInPrepStmts());
        return (byte[])object;
      } 
      object = ((BatchParams)object).parameterStrings[paramInt1];
      if (object == null)
        return null; 
      if (object[0] == 39 && object[object.length - 1] == 39) {
        byte[] arrayOfByte = new byte[object.length - 2];
        System.arraycopy(object, 1, arrayOfByte, 0, object.length - 2);
        return arrayOfByte;
      } 
      return (byte[])object;
    } 
  }
  
  public String getDateTime(String paramString) {
    return TimeUtil.getSimpleDateFormat(null, paramString, null, null).format(new Date());
  }
  
  public int getLocationOfOnDuplicateKeyUpdate() throws SQLException {
    return this.parseInfo.locationOfOnDuplicateKeyUpdate;
  }
  
  public ResultSetMetaData getMetaData() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool = isSelectQuery();
      ResultSetMetaData resultSetMetaData3 = null;
      ResultSetMetaData resultSetMetaData2 = null;
      if (!bool)
        return null; 
      ResultSetMetaData resultSetMetaData1 = this.pstmtResultMetaData;
      if (resultSetMetaData1 == null) {
        ResultSet resultSet;
        StatementImpl statementImpl;
        try {
          statementImpl = new PreparedStatement();
          this(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);
        } finally {
          resultSetMetaData1 = null;
          statementImpl = null;
        } 
        resultSetMetaData2 = resultSetMetaData3;
        if (resultSet != null)
          try {
            resultSet.close();
            resultSetMetaData2 = resultSetMetaData3;
          } catch (SQLException sQLException2) {} 
        sQLException1 = sQLException2;
        if (statementImpl != null)
          try {
            statementImpl.close();
            sQLException1 = sQLException2;
          } catch (SQLException sQLException1) {} 
        if (sQLException1 != null)
          throw sQLException1; 
        throw resultSetMetaData1;
      } 
      resultSetMetaData1 = this.pstmtResultMetaData;
      return resultSetMetaData1;
    } 
  }
  
  public String getNonRewrittenSql() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      int i = this.originalSql.indexOf(" of: ");
      if (i != -1)
        return this.originalSql.substring(i + 5); 
      return this.originalSql;
    } 
  }
  
  public ParameterBindings getParameterBindings() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      EmulatedPreparedStatementBindings emulatedPreparedStatementBindings = new EmulatedPreparedStatementBindings();
      this(this);
      return emulatedPreparedStatementBindings;
    } 
  }
  
  public int getParameterIndexOffset() {
    return 0;
  }
  
  public ParameterMetaData getParameterMetaData() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.parameterMetaData == null)
        if (this.connection.getGenerateSimpleParameterMetadata()) {
          MysqlParameterMetadata mysqlParameterMetadata = new MysqlParameterMetadata();
          this(this.parameterCount);
          this.parameterMetaData = mysqlParameterMetadata;
        } else {
          MysqlParameterMetadata mysqlParameterMetadata = new MysqlParameterMetadata();
          this(null, this.parameterCount, getExceptionInterceptor());
          this.parameterMetaData = mysqlParameterMetadata;
        }  
      return this.parameterMetaData;
    } 
  }
  
  public ParseInfo getParseInfo() {
    return this.parseInfo;
  }
  
  public String getPreparedSql() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (this.rewrittenBatchSize == 0)
          return this.originalSql; 
        try {
          ParseInfo parseInfo = this.parseInfo;
          return parseInfo.getSqlForBatch(parseInfo);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          RuntimeException runtimeException = new RuntimeException();
          this(unsupportedEncodingException);
          throw runtimeException;
        } 
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public int getRewrittenBatchSize() {
    return this.rewrittenBatchSize;
  }
  
  public int getUpdateCount() throws SQLException {
    int i = super.getUpdateCount();
    null = i;
    if (containsOnDuplicateKeyUpdateInSQL()) {
      null = i;
      if (this.compensateForOnDuplicateKeyUpdate) {
        if (i != 2) {
          null = i;
          return (i == 0) ? 1 : null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return 1;
  }
  
  public String getValuesClause() throws SQLException {
    return this.parseInfo.valuesClause;
  }
  
  public boolean isNull(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.isNull[paramInt];
    } 
  }
  
  public boolean isSelectQuery() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
    } 
  }
  
  public PreparedStatement prepareBatchedInsertSQL(MySQLConnection paramMySQLConnection, int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      PreparedStatement preparedStatement = new PreparedStatement();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Rewritten batch of: ");
      stringBuilder.append(this.originalSql);
      this(paramMySQLConnection, stringBuilder.toString(), this.currentCatalog, this.parseInfo.getParseInfoForBatch(paramInt));
      preparedStatement.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
      preparedStatement.rewrittenBatchSize = paramInt;
      return preparedStatement;
    } 
  }
  
  public void realClose(boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    MySQLConnection mySQLConnection = this.connection;
    if (mySQLConnection == null)
      return; 
    synchronized (mySQLConnection.getConnectionMutex()) {
      if (this.isClosed)
        return; 
      if (this.useUsageAdvisor && this.numberOfExecutions <= 1) {
        String str = Messages.getString("PreparedStatement.43");
        ProfilerEventHandler profilerEventHandler = this.eventSink;
        ProfilerEvent profilerEvent = new ProfilerEvent();
        this((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, str);
        profilerEventHandler.consumeEvent(profilerEvent);
      } 
      super.realClose(paramBoolean1, paramBoolean2);
      this.dbmd = null;
      this.originalSql = null;
      this.staticSqlStrings = null;
      this.parameterValues = null;
      this.parameterStreams = null;
      this.isStream = null;
      this.streamLengths = null;
      this.isNull = null;
      this.streamConvertBuf = null;
      this.parameterTypes = null;
      return;
    } 
  }
  
  public void setArray(int paramInt, Array paramArray) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
    setAsciiStream(paramInt, paramInputStream, -1);
  }
  
  public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    if (paramInputStream == null) {
      setNull(paramInt1, 12);
    } else {
      setBinaryStream(paramInt1, paramInputStream, paramInt2);
    } 
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    setAsciiStream(paramInt, paramInputStream, (int)paramLong);
    this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 2005;
  }
  
  public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
    if (paramBigDecimal == null) {
      setNull(paramInt, 3);
    } else {
      setInternal(paramInt, StringUtils.fixDecimalExponent(StringUtils.consistentToString(paramBigDecimal)));
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 3;
    } 
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
    setBinaryStream(paramInt, paramInputStream, -1);
  }
  
  public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #5
    //   11: aload #5
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 28
    //   18: aload_0
    //   19: iload_1
    //   20: bipush #-2
    //   22: invokevirtual setNull : (II)V
    //   25: goto -> 135
    //   28: aload_0
    //   29: invokevirtual getParameterIndexOffset : ()I
    //   32: istore #4
    //   34: iload_1
    //   35: iconst_1
    //   36: if_icmplt -> 139
    //   39: iload_1
    //   40: aload_0
    //   41: getfield staticSqlStrings : [[B
    //   44: arraylength
    //   45: if_icmpgt -> 139
    //   48: iload #4
    //   50: iconst_m1
    //   51: if_icmpne -> 76
    //   54: iload_1
    //   55: iconst_1
    //   56: if_icmpeq -> 62
    //   59: goto -> 76
    //   62: ldc_w 'Can't set IN parameter for return value of stored function call.'
    //   65: ldc_w 'S1009'
    //   68: aload_0
    //   69: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   72: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   75: athrow
    //   76: aload_0
    //   77: getfield parameterStreams : [Ljava/io/InputStream;
    //   80: astore #6
    //   82: iinc #1, -1
    //   85: iload #4
    //   87: iload_1
    //   88: iadd
    //   89: istore #4
    //   91: aload #6
    //   93: iload #4
    //   95: aload_2
    //   96: aastore
    //   97: aload_0
    //   98: getfield isStream : [Z
    //   101: iload #4
    //   103: iconst_1
    //   104: bastore
    //   105: aload_0
    //   106: getfield streamLengths : [I
    //   109: iload #4
    //   111: iload_3
    //   112: iastore
    //   113: aload_0
    //   114: getfield isNull : [Z
    //   117: iload #4
    //   119: iconst_0
    //   120: bastore
    //   121: aload_0
    //   122: getfield parameterTypes : [I
    //   125: iload_1
    //   126: aload_0
    //   127: invokevirtual getParameterIndexOffset : ()I
    //   130: iadd
    //   131: sipush #2004
    //   134: iastore
    //   135: aload #5
    //   137: monitorexit
    //   138: return
    //   139: new java/lang/StringBuilder
    //   142: astore_2
    //   143: aload_2
    //   144: invokespecial <init> : ()V
    //   147: aload_2
    //   148: ldc_w 'PreparedStatement.2'
    //   151: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: aload_2
    //   159: iload_1
    //   160: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload_2
    //   165: ldc_w 'PreparedStatement.3'
    //   168: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_2
    //   176: aload_0
    //   177: getfield staticSqlStrings : [[B
    //   180: arraylength
    //   181: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload_2
    //   186: ldc_w 'PreparedStatement.4'
    //   189: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: pop
    //   196: aload_2
    //   197: invokevirtual toString : ()Ljava/lang/String;
    //   200: ldc_w 'S1009'
    //   203: aload_0
    //   204: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   207: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   210: athrow
    //   211: astore_2
    //   212: aload #5
    //   214: monitorexit
    //   215: aload_2
    //   216: athrow
    // Exception table:
    //   from	to	target	type
    //   18	25	211	finally
    //   28	34	211	finally
    //   39	48	211	finally
    //   62	76	211	finally
    //   76	82	211	finally
    //   97	135	211	finally
    //   135	138	211	finally
    //   139	211	211	finally
    //   212	215	211	finally
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    setBinaryStream(paramInt, paramInputStream, (int)paramLong);
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
    setBinaryStream(paramInt, paramInputStream);
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    setBinaryStream(paramInt, paramInputStream, (int)paramLong);
  }
  
  public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
    if (paramBlob == null) {
      setNull(paramInt, 2004);
    } else {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byteArrayOutputStream.write(39);
      escapeblockFast(paramBlob.getBytes(1L, (int)paramBlob.length()), byteArrayOutputStream, (int)paramBlob.length());
      byteArrayOutputStream.write(39);
      setInternal(paramInt, byteArrayOutputStream.toByteArray());
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 2004;
    } 
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
    if (this.useTrueBoolean) {
      String str;
      if (paramBoolean) {
        str = "1";
      } else {
        str = "0";
      } 
      setInternal(paramInt, str);
    } else {
      String str;
      if (paramBoolean) {
        str = "'t'";
      } else {
        str = "'f'";
      } 
      setInternal(paramInt, str);
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 16;
    } 
  }
  
  public void setByte(int paramInt, byte paramByte) throws SQLException {
    setInternal(paramInt, String.valueOf(paramByte));
    this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = -6;
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    setBytes(paramInt, paramArrayOfbyte, true, true);
    if (paramArrayOfbyte != null)
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = -2; 
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfbyte, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #10
    //   11: aload #10
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 28
    //   18: aload_0
    //   19: iload_1
    //   20: bipush #-2
    //   22: invokevirtual setNull : (II)V
    //   25: goto -> 431
    //   28: aload_0
    //   29: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   32: invokeinterface getEncoding : ()Ljava/lang/String;
    //   37: astore #11
    //   39: aload_0
    //   40: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   43: invokeinterface isNoBackslashEscapesSet : ()Z
    //   48: istore #9
    //   50: iconst_2
    //   51: istore #6
    //   53: iconst_0
    //   54: istore #5
    //   56: iconst_0
    //   57: istore #7
    //   59: iload #9
    //   61: ifne -> 435
    //   64: iload #4
    //   66: ifeq -> 101
    //   69: aload_0
    //   70: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   73: invokeinterface getUseUnicode : ()Z
    //   78: ifeq -> 101
    //   81: aload #11
    //   83: ifnull -> 101
    //   86: aload #11
    //   88: invokestatic isMultibyteCharset : (Ljava/lang/String;)Z
    //   91: istore #4
    //   93: iload #4
    //   95: ifeq -> 101
    //   98: goto -> 435
    //   101: aload_2
    //   102: arraylength
    //   103: istore #8
    //   105: iconst_1
    //   106: istore #5
    //   108: iload_3
    //   109: ifeq -> 130
    //   112: aload_0
    //   113: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   116: iconst_4
    //   117: iconst_1
    //   118: iconst_0
    //   119: invokeinterface versionMeetsMinimum : (III)Z
    //   124: ifeq -> 130
    //   127: goto -> 133
    //   130: iconst_0
    //   131: istore #5
    //   133: iload #5
    //   135: ifeq -> 142
    //   138: bipush #9
    //   140: istore #6
    //   142: new java/io/ByteArrayOutputStream
    //   145: astore #11
    //   147: aload #11
    //   149: iload #6
    //   151: iload #8
    //   153: iadd
    //   154: invokespecial <init> : (I)V
    //   157: iload #5
    //   159: ifeq -> 211
    //   162: aload #11
    //   164: bipush #95
    //   166: invokevirtual write : (I)V
    //   169: aload #11
    //   171: bipush #98
    //   173: invokevirtual write : (I)V
    //   176: aload #11
    //   178: bipush #105
    //   180: invokevirtual write : (I)V
    //   183: aload #11
    //   185: bipush #110
    //   187: invokevirtual write : (I)V
    //   190: aload #11
    //   192: bipush #97
    //   194: invokevirtual write : (I)V
    //   197: aload #11
    //   199: bipush #114
    //   201: invokevirtual write : (I)V
    //   204: aload #11
    //   206: bipush #121
    //   208: invokevirtual write : (I)V
    //   211: aload #11
    //   213: bipush #39
    //   215: invokevirtual write : (I)V
    //   218: iload #7
    //   220: istore #5
    //   222: iload #5
    //   224: iload #8
    //   226: if_icmpge -> 414
    //   229: aload_2
    //   230: iload #5
    //   232: baload
    //   233: istore #6
    //   235: iload #6
    //   237: ifeq -> 394
    //   240: iload #6
    //   242: bipush #10
    //   244: if_icmpeq -> 377
    //   247: iload #6
    //   249: bipush #13
    //   251: if_icmpeq -> 360
    //   254: iload #6
    //   256: bipush #26
    //   258: if_icmpeq -> 343
    //   261: iload #6
    //   263: bipush #34
    //   265: if_icmpeq -> 326
    //   268: iload #6
    //   270: bipush #39
    //   272: if_icmpeq -> 309
    //   275: iload #6
    //   277: bipush #92
    //   279: if_icmpeq -> 292
    //   282: aload #11
    //   284: iload #6
    //   286: invokevirtual write : (I)V
    //   289: goto -> 408
    //   292: aload #11
    //   294: bipush #92
    //   296: invokevirtual write : (I)V
    //   299: aload #11
    //   301: bipush #92
    //   303: invokevirtual write : (I)V
    //   306: goto -> 408
    //   309: aload #11
    //   311: bipush #92
    //   313: invokevirtual write : (I)V
    //   316: aload #11
    //   318: bipush #39
    //   320: invokevirtual write : (I)V
    //   323: goto -> 408
    //   326: aload #11
    //   328: bipush #92
    //   330: invokevirtual write : (I)V
    //   333: aload #11
    //   335: bipush #34
    //   337: invokevirtual write : (I)V
    //   340: goto -> 408
    //   343: aload #11
    //   345: bipush #92
    //   347: invokevirtual write : (I)V
    //   350: aload #11
    //   352: bipush #90
    //   354: invokevirtual write : (I)V
    //   357: goto -> 408
    //   360: aload #11
    //   362: bipush #92
    //   364: invokevirtual write : (I)V
    //   367: aload #11
    //   369: bipush #114
    //   371: invokevirtual write : (I)V
    //   374: goto -> 408
    //   377: aload #11
    //   379: bipush #92
    //   381: invokevirtual write : (I)V
    //   384: aload #11
    //   386: bipush #110
    //   388: invokevirtual write : (I)V
    //   391: goto -> 408
    //   394: aload #11
    //   396: bipush #92
    //   398: invokevirtual write : (I)V
    //   401: aload #11
    //   403: bipush #48
    //   405: invokevirtual write : (I)V
    //   408: iinc #5, 1
    //   411: goto -> 222
    //   414: aload #11
    //   416: bipush #39
    //   418: invokevirtual write : (I)V
    //   421: aload_0
    //   422: iload_1
    //   423: aload #11
    //   425: invokevirtual toByteArray : ()[B
    //   428: invokevirtual setInternal : (I[B)V
    //   431: aload #10
    //   433: monitorexit
    //   434: return
    //   435: new java/io/ByteArrayOutputStream
    //   438: astore #12
    //   440: aload #12
    //   442: aload_2
    //   443: arraylength
    //   444: iconst_2
    //   445: imul
    //   446: iconst_3
    //   447: iadd
    //   448: invokespecial <init> : (I)V
    //   451: aload #12
    //   453: bipush #120
    //   455: invokevirtual write : (I)V
    //   458: aload #12
    //   460: bipush #39
    //   462: invokevirtual write : (I)V
    //   465: iload #5
    //   467: aload_2
    //   468: arraylength
    //   469: if_icmpge -> 529
    //   472: aload_2
    //   473: iload #5
    //   475: baload
    //   476: sipush #255
    //   479: iand
    //   480: bipush #16
    //   482: idiv
    //   483: istore #7
    //   485: aload_2
    //   486: iload #5
    //   488: baload
    //   489: istore #6
    //   491: getstatic com/mysql/jdbc/PreparedStatement.HEX_DIGITS : [B
    //   494: astore #11
    //   496: aload #12
    //   498: aload #11
    //   500: iload #7
    //   502: baload
    //   503: invokevirtual write : (I)V
    //   506: aload #12
    //   508: aload #11
    //   510: iload #6
    //   512: sipush #255
    //   515: iand
    //   516: bipush #16
    //   518: irem
    //   519: baload
    //   520: invokevirtual write : (I)V
    //   523: iinc #5, 1
    //   526: goto -> 465
    //   529: aload #12
    //   531: bipush #39
    //   533: invokevirtual write : (I)V
    //   536: aload_0
    //   537: iload_1
    //   538: aload #12
    //   540: invokevirtual toByteArray : ()[B
    //   543: invokevirtual setInternal : (I[B)V
    //   546: aload #10
    //   548: monitorexit
    //   549: return
    //   550: astore #11
    //   552: aload #11
    //   554: invokevirtual toString : ()Ljava/lang/String;
    //   557: ldc_w 'S1009'
    //   560: aconst_null
    //   561: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   564: astore_2
    //   565: aload_2
    //   566: aload #11
    //   568: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   571: pop
    //   572: aload_2
    //   573: athrow
    //   574: astore_2
    //   575: aload_2
    //   576: athrow
    //   577: astore_2
    //   578: aload #10
    //   580: monitorexit
    //   581: aload_2
    //   582: athrow
    // Exception table:
    //   from	to	target	type
    //   18	25	577	finally
    //   28	39	577	finally
    //   39	50	574	java/sql/SQLException
    //   39	50	550	java/lang/RuntimeException
    //   39	50	577	finally
    //   69	81	574	java/sql/SQLException
    //   69	81	550	java/lang/RuntimeException
    //   69	81	577	finally
    //   86	93	574	java/sql/SQLException
    //   86	93	550	java/lang/RuntimeException
    //   86	93	577	finally
    //   101	105	577	finally
    //   112	127	577	finally
    //   142	157	577	finally
    //   162	211	577	finally
    //   211	218	577	finally
    //   282	289	577	finally
    //   292	306	577	finally
    //   309	323	577	finally
    //   326	340	577	finally
    //   343	357	577	finally
    //   360	374	577	finally
    //   377	391	577	finally
    //   394	408	577	finally
    //   414	431	577	finally
    //   431	434	577	finally
    //   435	465	574	java/sql/SQLException
    //   435	465	550	java/lang/RuntimeException
    //   435	465	577	finally
    //   465	485	574	java/sql/SQLException
    //   465	485	550	java/lang/RuntimeException
    //   465	485	577	finally
    //   491	523	574	java/sql/SQLException
    //   491	523	550	java/lang/RuntimeException
    //   491	523	577	finally
    //   529	546	574	java/sql/SQLException
    //   529	546	550	java/lang/RuntimeException
    //   529	546	577	finally
    //   546	549	577	finally
    //   552	574	577	finally
    //   575	577	577	finally
    //   578	581	577	finally
  }
  
  public void setBytesNoEscape(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length + 2];
    arrayOfByte[0] = 39;
    System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 1, paramArrayOfbyte.length);
    arrayOfByte[paramArrayOfbyte.length + 1] = 39;
    setInternal(paramInt, arrayOfByte);
  }
  
  public void setBytesNoEscapeNoQuotes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    setInternal(paramInt, paramArrayOfbyte);
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
    setCharacterStream(paramInt, paramReader, -1);
  }
  
  public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #5
    //   11: aload #5
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 27
    //   18: aload_0
    //   19: iload_1
    //   20: iconst_m1
    //   21: invokevirtual setNull : (II)V
    //   24: goto -> 254
    //   27: aload_0
    //   28: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   31: invokeinterface getUseStreamLengthsInPrepStmts : ()Z
    //   36: istore #4
    //   38: aload_0
    //   39: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   42: invokeinterface getClobCharacterEncoding : ()Ljava/lang/String;
    //   47: astore #6
    //   49: iload #4
    //   51: ifeq -> 163
    //   54: iload_3
    //   55: iconst_m1
    //   56: if_icmpeq -> 163
    //   59: iload_3
    //   60: newarray char
    //   62: astore #7
    //   64: aload_2
    //   65: aload #7
    //   67: iload_3
    //   68: invokestatic readFully : (Ljava/io/Reader;[CI)I
    //   71: istore_3
    //   72: aload #6
    //   74: ifnonnull -> 98
    //   77: new java/lang/String
    //   80: astore_2
    //   81: aload_2
    //   82: aload #7
    //   84: iconst_0
    //   85: iload_3
    //   86: invokespecial <init> : ([CII)V
    //   89: aload_0
    //   90: iload_1
    //   91: aload_2
    //   92: invokevirtual setString : (ILjava/lang/String;)V
    //   95: goto -> 238
    //   98: new java/lang/String
    //   101: astore_2
    //   102: aload_2
    //   103: aload #7
    //   105: iconst_0
    //   106: iload_3
    //   107: invokespecial <init> : ([CII)V
    //   110: aload_0
    //   111: iload_1
    //   112: aload_2
    //   113: aload #6
    //   115: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   118: invokevirtual setBytes : (I[B)V
    //   121: goto -> 238
    //   124: astore_2
    //   125: new java/lang/StringBuilder
    //   128: astore_2
    //   129: aload_2
    //   130: invokespecial <init> : ()V
    //   133: aload_2
    //   134: ldc_w 'Unsupported character encoding '
    //   137: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: pop
    //   141: aload_2
    //   142: aload #6
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: aload_2
    //   149: invokevirtual toString : ()Ljava/lang/String;
    //   152: ldc_w 'S1009'
    //   155: aload_0
    //   156: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   159: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   162: athrow
    //   163: sipush #4096
    //   166: newarray char
    //   168: astore #8
    //   170: new java/lang/StringBuilder
    //   173: astore #7
    //   175: aload #7
    //   177: invokespecial <init> : ()V
    //   180: aload_2
    //   181: aload #8
    //   183: invokevirtual read : ([C)I
    //   186: istore_3
    //   187: iload_3
    //   188: iconst_m1
    //   189: if_icmpeq -> 205
    //   192: aload #7
    //   194: aload #8
    //   196: iconst_0
    //   197: iload_3
    //   198: invokevirtual append : ([CII)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: goto -> 180
    //   205: aload #6
    //   207: ifnonnull -> 223
    //   210: aload_0
    //   211: iload_1
    //   212: aload #7
    //   214: invokevirtual toString : ()Ljava/lang/String;
    //   217: invokevirtual setString : (ILjava/lang/String;)V
    //   220: goto -> 238
    //   223: aload_0
    //   224: iload_1
    //   225: aload #7
    //   227: invokevirtual toString : ()Ljava/lang/String;
    //   230: aload #6
    //   232: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   235: invokevirtual setBytes : (I[B)V
    //   238: aload_0
    //   239: getfield parameterTypes : [I
    //   242: iload_1
    //   243: iconst_1
    //   244: isub
    //   245: aload_0
    //   246: invokevirtual getParameterIndexOffset : ()I
    //   249: iadd
    //   250: sipush #2005
    //   253: iastore
    //   254: aload #5
    //   256: monitorexit
    //   257: return
    //   258: astore_2
    //   259: new java/lang/StringBuilder
    //   262: astore_2
    //   263: aload_2
    //   264: invokespecial <init> : ()V
    //   267: aload_2
    //   268: ldc_w 'Unsupported character encoding '
    //   271: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   274: pop
    //   275: aload_2
    //   276: aload #6
    //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload_2
    //   283: invokevirtual toString : ()Ljava/lang/String;
    //   286: ldc_w 'S1009'
    //   289: aload_0
    //   290: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   293: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   296: athrow
    //   297: astore_2
    //   298: goto -> 317
    //   301: astore_2
    //   302: aload_2
    //   303: invokevirtual toString : ()Ljava/lang/String;
    //   306: ldc_w 'S1000'
    //   309: aload_0
    //   310: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   313: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   316: athrow
    //   317: aload #5
    //   319: monitorexit
    //   320: aload_2
    //   321: athrow
    // Exception table:
    //   from	to	target	type
    //   18	24	301	java/io/IOException
    //   18	24	297	finally
    //   27	49	301	java/io/IOException
    //   27	49	297	finally
    //   59	72	301	java/io/IOException
    //   59	72	297	finally
    //   77	95	301	java/io/IOException
    //   77	95	297	finally
    //   98	121	124	java/io/UnsupportedEncodingException
    //   98	121	301	java/io/IOException
    //   98	121	297	finally
    //   125	163	301	java/io/IOException
    //   125	163	297	finally
    //   163	180	301	java/io/IOException
    //   163	180	297	finally
    //   180	187	301	java/io/IOException
    //   180	187	297	finally
    //   192	202	301	java/io/IOException
    //   192	202	297	finally
    //   210	220	301	java/io/IOException
    //   210	220	297	finally
    //   223	238	258	java/io/UnsupportedEncodingException
    //   223	238	301	java/io/IOException
    //   223	238	297	finally
    //   238	254	301	java/io/IOException
    //   238	254	297	finally
    //   254	257	297	finally
    //   259	297	301	java/io/IOException
    //   259	297	297	finally
    //   302	317	297	finally
    //   317	320	297	finally
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    setCharacterStream(paramInt, paramReader, (int)paramLong);
  }
  
  public void setClob(int paramInt, Reader paramReader) throws SQLException {
    setCharacterStream(paramInt, paramReader);
  }
  
  public void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    setCharacterStream(paramInt, paramReader, paramLong);
  }
  
  public void setClob(int paramInt, Clob paramClob) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_2
    //   13: ifnonnull -> 27
    //   16: aload_0
    //   17: iload_1
    //   18: sipush #2005
    //   21: invokevirtual setNull : (II)V
    //   24: goto -> 105
    //   27: aload_0
    //   28: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   31: invokeinterface getClobCharacterEncoding : ()Ljava/lang/String;
    //   36: astore #4
    //   38: aload #4
    //   40: ifnonnull -> 65
    //   43: aload_0
    //   44: iload_1
    //   45: aload_2
    //   46: lconst_1
    //   47: aload_2
    //   48: invokeinterface length : ()J
    //   53: l2i
    //   54: invokeinterface getSubString : (JI)Ljava/lang/String;
    //   59: invokevirtual setString : (ILjava/lang/String;)V
    //   62: goto -> 89
    //   65: aload_0
    //   66: iload_1
    //   67: aload_2
    //   68: lconst_1
    //   69: aload_2
    //   70: invokeinterface length : ()J
    //   75: l2i
    //   76: invokeinterface getSubString : (JI)Ljava/lang/String;
    //   81: aload #4
    //   83: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   86: invokevirtual setBytes : (I[B)V
    //   89: aload_0
    //   90: getfield parameterTypes : [I
    //   93: iload_1
    //   94: iconst_1
    //   95: isub
    //   96: aload_0
    //   97: invokevirtual getParameterIndexOffset : ()I
    //   100: iadd
    //   101: sipush #2005
    //   104: iastore
    //   105: aload_3
    //   106: monitorexit
    //   107: return
    //   108: astore_2
    //   109: new java/lang/StringBuilder
    //   112: astore_2
    //   113: aload_2
    //   114: invokespecial <init> : ()V
    //   117: aload_2
    //   118: ldc_w 'Unsupported character encoding '
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload_2
    //   126: aload #4
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload_2
    //   133: invokevirtual toString : ()Ljava/lang/String;
    //   136: ldc_w 'S1009'
    //   139: aload_0
    //   140: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   143: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   146: athrow
    //   147: astore_2
    //   148: aload_3
    //   149: monitorexit
    //   150: aload_2
    //   151: athrow
    // Exception table:
    //   from	to	target	type
    //   16	24	147	finally
    //   27	38	147	finally
    //   43	62	147	finally
    //   65	89	108	java/io/UnsupportedEncodingException
    //   65	89	147	finally
    //   89	105	147	finally
    //   105	107	147	finally
    //   109	147	147	finally
    //   148	150	147	finally
  }
  
  public void setDate(int paramInt, Date paramDate) throws SQLException {
    setDate(paramInt, paramDate, null);
  }
  
  public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
    if (paramDate == null) {
      setNull(paramInt, 91);
    } else if (!this.useLegacyDatetimeCode) {
      newSetDateInternal(paramInt, paramDate, paramCalendar);
    } else {
      synchronized (checkClosed().getConnectionMutex()) {
        SimpleDateFormat simpleDateFormat = TimeUtil.getSimpleDateFormat(this.ddf, "''yyyy-MM-dd''", paramCalendar, null);
        this.ddf = simpleDateFormat;
        setInternal(paramInt, simpleDateFormat.format(paramDate));
        this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 91;
        return;
      } 
    } 
  }
  
  public void setDouble(int paramInt, double paramDouble) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.connection.getAllowNanAndInf() || (paramDouble != Double.POSITIVE_INFINITY && paramDouble != Double.NEGATIVE_INFINITY && !Double.isNaN(paramDouble))) {
        setInternal(paramInt, StringUtils.fixDecimalExponent(String.valueOf(paramDouble)));
        this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 8;
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("'");
      stringBuilder.append(paramDouble);
      stringBuilder.append("' is not a valid numeric or approximate numeric value");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public void setFloat(int paramInt, float paramFloat) throws SQLException {
    setInternal(paramInt, StringUtils.fixDecimalExponent(String.valueOf(paramFloat)));
    this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 6;
  }
  
  public void setInt(int paramInt1, int paramInt2) throws SQLException {
    setInternal(paramInt1, String.valueOf(paramInt2));
    this.parameterTypes[paramInt1 - 1 + getParameterIndexOffset()] = 4;
  }
  
  public final void setInternal(int paramInt, String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      byte[] arrayOfByte;
      SingleByteCharsetConverter singleByteCharsetConverter = this.charConverter;
      if (singleByteCharsetConverter != null) {
        arrayOfByte = singleByteCharsetConverter.toBytes(paramString);
      } else {
        arrayOfByte = StringUtils.getBytes((String)arrayOfByte, singleByteCharsetConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
      } 
      setInternal(paramInt, arrayOfByte);
      return;
    } 
  }
  
  public final void setInternal(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      int i = getParameterIndexOffset();
      checkBounds(paramInt, i);
      boolean[] arrayOfBoolean = this.isStream;
      paramInt = paramInt - 1 + i;
      arrayOfBoolean[paramInt] = false;
      this.isNull[paramInt] = false;
      this.parameterStreams[paramInt] = null;
      this.parameterValues[paramInt] = paramArrayOfbyte;
      return;
    } 
  }
  
  public void setLong(int paramInt, long paramLong) throws SQLException {
    setInternal(paramInt, String.valueOf(paramLong));
    this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = -5;
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
    setNCharacterStream(paramInt, paramReader, -1L);
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    String str;
    Object object = checkClosed().getConnectionMutex();
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    if (paramReader == null) {
      try {
        setNull(paramInt, -1);
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return;
      } catch (IOException iOException) {
        throw SQLError.createSQLException(iOException.toString(), "S1000", getExceptionInterceptor());
      } finally {}
    } else {
      if (this.connection.getUseStreamLengthsInPrepStmts() && paramLong != -1L) {
        int i = (int)paramLong;
        char[] arrayOfChar = new char[i];
        i = readFully(paramReader, arrayOfChar, i);
        str = new String();
        this(arrayOfChar, 0, i);
        setNString(paramInt, str);
      } else {
        char[] arrayOfChar = new char[4096];
        StringBuilder stringBuilder = new StringBuilder();
        this();
        while (true) {
          int i = str.read(arrayOfChar);
          if (i != -1) {
            stringBuilder.append(arrayOfChar, 0, i);
            continue;
          } 
          setNString(paramInt, stringBuilder.toString());
          this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 2011;
        } 
      } 
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 2011;
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw str;
  }
  
  public void setNClob(int paramInt, Reader paramReader) throws SQLException {
    setNCharacterStream(paramInt, paramReader);
  }
  
  public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    if (paramReader == null) {
      setNull(paramInt, -1);
    } else {
      setNCharacterStream(paramInt, paramReader, paramLong);
    } 
  }
  
  public void setNString(int paramInt, String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
        setString(paramInt, paramString);
        return;
      } 
      if (paramString == null) {
        setNull(paramInt, 1);
      } else {
        byte[] arrayOfByte;
        int i = paramString.length();
        StringBuilder stringBuilder = new StringBuilder();
        this((int)(paramString.length() * 1.1D + 4.0D));
        stringBuilder.append("_utf8");
        stringBuilder.append('\'');
        for (byte b = 0; b < i; b++) {
          char c = paramString.charAt(b);
          if (c != '\000') {
            if (c != '\n') {
              if (c != '\r') {
                if (c != '\032') {
                  if (c != '"') {
                    if (c != '\'') {
                      if (c != '\\') {
                        stringBuilder.append(c);
                      } else {
                        stringBuilder.append('\\');
                        stringBuilder.append('\\');
                      } 
                    } else {
                      stringBuilder.append('\\');
                      stringBuilder.append('\'');
                    } 
                  } else {
                    if (this.usingAnsiMode)
                      stringBuilder.append('\\'); 
                    stringBuilder.append('"');
                  } 
                } else {
                  stringBuilder.append('\\');
                  stringBuilder.append('Z');
                } 
              } else {
                stringBuilder.append('\\');
                stringBuilder.append('r');
              } 
            } else {
              stringBuilder.append('\\');
              stringBuilder.append('n');
            } 
          } else {
            stringBuilder.append('\\');
            stringBuilder.append('0');
          } 
        } 
        stringBuilder.append('\'');
        paramString = stringBuilder.toString();
        if (!this.isLoadDataQuery) {
          arrayOfByte = StringUtils.getBytes(paramString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
        } else {
          arrayOfByte = StringUtils.getBytes((String)arrayOfByte);
        } 
        setInternal(paramInt, arrayOfByte);
        this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = -9;
      } 
      return;
    } 
  }
  
  public void setNull(int paramInt1, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setInternal(paramInt1, "null");
      boolean[] arrayOfBoolean = this.isNull;
      arrayOfBoolean[getParameterIndexOffset() + --paramInt1] = true;
      this.parameterTypes[paramInt1 + getParameterIndexOffset()] = 0;
      return;
    } 
  }
  
  public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
    setNull(paramInt1, paramInt2);
    this.parameterTypes[paramInt1 - 1 + getParameterIndexOffset()] = 0;
  }
  
  public void setObject(int paramInt, Object paramObject) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_2
    //   13: ifnonnull -> 27
    //   16: aload_0
    //   17: iload_1
    //   18: sipush #1111
    //   21: invokevirtual setNull : (II)V
    //   24: goto -> 424
    //   27: aload_2
    //   28: instanceof java/lang/Byte
    //   31: ifeq -> 49
    //   34: aload_0
    //   35: iload_1
    //   36: aload_2
    //   37: checkcast java/lang/Byte
    //   40: invokevirtual intValue : ()I
    //   43: invokevirtual setInt : (II)V
    //   46: goto -> 424
    //   49: aload_2
    //   50: instanceof java/lang/String
    //   53: ifeq -> 68
    //   56: aload_0
    //   57: iload_1
    //   58: aload_2
    //   59: checkcast java/lang/String
    //   62: invokevirtual setString : (ILjava/lang/String;)V
    //   65: goto -> 424
    //   68: aload_2
    //   69: instanceof java/math/BigDecimal
    //   72: ifeq -> 87
    //   75: aload_0
    //   76: iload_1
    //   77: aload_2
    //   78: checkcast java/math/BigDecimal
    //   81: invokevirtual setBigDecimal : (ILjava/math/BigDecimal;)V
    //   84: goto -> 424
    //   87: aload_2
    //   88: instanceof java/lang/Short
    //   91: ifeq -> 109
    //   94: aload_0
    //   95: iload_1
    //   96: aload_2
    //   97: checkcast java/lang/Short
    //   100: invokevirtual shortValue : ()S
    //   103: invokevirtual setShort : (IS)V
    //   106: goto -> 424
    //   109: aload_2
    //   110: instanceof java/lang/Integer
    //   113: ifeq -> 131
    //   116: aload_0
    //   117: iload_1
    //   118: aload_2
    //   119: checkcast java/lang/Integer
    //   122: invokevirtual intValue : ()I
    //   125: invokevirtual setInt : (II)V
    //   128: goto -> 424
    //   131: aload_2
    //   132: instanceof java/lang/Long
    //   135: ifeq -> 153
    //   138: aload_0
    //   139: iload_1
    //   140: aload_2
    //   141: checkcast java/lang/Long
    //   144: invokevirtual longValue : ()J
    //   147: invokevirtual setLong : (IJ)V
    //   150: goto -> 424
    //   153: aload_2
    //   154: instanceof java/lang/Float
    //   157: ifeq -> 175
    //   160: aload_0
    //   161: iload_1
    //   162: aload_2
    //   163: checkcast java/lang/Float
    //   166: invokevirtual floatValue : ()F
    //   169: invokevirtual setFloat : (IF)V
    //   172: goto -> 424
    //   175: aload_2
    //   176: instanceof java/lang/Double
    //   179: ifeq -> 197
    //   182: aload_0
    //   183: iload_1
    //   184: aload_2
    //   185: checkcast java/lang/Double
    //   188: invokevirtual doubleValue : ()D
    //   191: invokevirtual setDouble : (ID)V
    //   194: goto -> 424
    //   197: aload_2
    //   198: instanceof [B
    //   201: ifeq -> 216
    //   204: aload_0
    //   205: iload_1
    //   206: aload_2
    //   207: checkcast [B
    //   210: invokevirtual setBytes : (I[B)V
    //   213: goto -> 424
    //   216: aload_2
    //   217: instanceof java/sql/Date
    //   220: ifeq -> 235
    //   223: aload_0
    //   224: iload_1
    //   225: aload_2
    //   226: checkcast java/sql/Date
    //   229: invokevirtual setDate : (ILjava/sql/Date;)V
    //   232: goto -> 424
    //   235: aload_2
    //   236: instanceof java/sql/Time
    //   239: ifeq -> 254
    //   242: aload_0
    //   243: iload_1
    //   244: aload_2
    //   245: checkcast java/sql/Time
    //   248: invokevirtual setTime : (ILjava/sql/Time;)V
    //   251: goto -> 424
    //   254: aload_2
    //   255: instanceof java/sql/Timestamp
    //   258: ifeq -> 273
    //   261: aload_0
    //   262: iload_1
    //   263: aload_2
    //   264: checkcast java/sql/Timestamp
    //   267: invokevirtual setTimestamp : (ILjava/sql/Timestamp;)V
    //   270: goto -> 424
    //   273: aload_2
    //   274: instanceof java/lang/Boolean
    //   277: ifeq -> 295
    //   280: aload_0
    //   281: iload_1
    //   282: aload_2
    //   283: checkcast java/lang/Boolean
    //   286: invokevirtual booleanValue : ()Z
    //   289: invokevirtual setBoolean : (IZ)V
    //   292: goto -> 424
    //   295: aload_2
    //   296: instanceof java/io/InputStream
    //   299: ifeq -> 315
    //   302: aload_0
    //   303: iload_1
    //   304: aload_2
    //   305: checkcast java/io/InputStream
    //   308: iconst_m1
    //   309: invokevirtual setBinaryStream : (ILjava/io/InputStream;I)V
    //   312: goto -> 424
    //   315: aload_2
    //   316: instanceof java/sql/Blob
    //   319: ifeq -> 334
    //   322: aload_0
    //   323: iload_1
    //   324: aload_2
    //   325: checkcast java/sql/Blob
    //   328: invokevirtual setBlob : (ILjava/sql/Blob;)V
    //   331: goto -> 424
    //   334: aload_2
    //   335: instanceof java/sql/Clob
    //   338: ifeq -> 353
    //   341: aload_0
    //   342: iload_1
    //   343: aload_2
    //   344: checkcast java/sql/Clob
    //   347: invokevirtual setClob : (ILjava/sql/Clob;)V
    //   350: goto -> 424
    //   353: aload_0
    //   354: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   357: invokeinterface getTreatUtilDateAsTimestamp : ()Z
    //   362: ifeq -> 399
    //   365: aload_2
    //   366: instanceof java/util/Date
    //   369: ifeq -> 399
    //   372: new java/sql/Timestamp
    //   375: astore #4
    //   377: aload #4
    //   379: aload_2
    //   380: checkcast java/util/Date
    //   383: invokevirtual getTime : ()J
    //   386: invokespecial <init> : (J)V
    //   389: aload_0
    //   390: iload_1
    //   391: aload #4
    //   393: invokevirtual setTimestamp : (ILjava/sql/Timestamp;)V
    //   396: goto -> 424
    //   399: aload_2
    //   400: instanceof java/math/BigInteger
    //   403: ifeq -> 418
    //   406: aload_0
    //   407: iload_1
    //   408: aload_2
    //   409: invokevirtual toString : ()Ljava/lang/String;
    //   412: invokevirtual setString : (ILjava/lang/String;)V
    //   415: goto -> 424
    //   418: aload_0
    //   419: iload_1
    //   420: aload_2
    //   421: invokespecial setSerializableObject : (ILjava/lang/Object;)V
    //   424: aload_3
    //   425: monitorexit
    //   426: return
    //   427: astore_2
    //   428: aload_3
    //   429: monitorexit
    //   430: aload_2
    //   431: athrow
    // Exception table:
    //   from	to	target	type
    //   16	24	427	finally
    //   27	46	427	finally
    //   49	65	427	finally
    //   68	84	427	finally
    //   87	106	427	finally
    //   109	128	427	finally
    //   131	150	427	finally
    //   153	172	427	finally
    //   175	194	427	finally
    //   197	213	427	finally
    //   216	232	427	finally
    //   235	251	427	finally
    //   254	270	427	finally
    //   273	292	427	finally
    //   295	312	427	finally
    //   315	331	427	finally
    //   334	350	427	finally
    //   353	396	427	finally
    //   399	415	427	finally
    //   418	424	427	finally
    //   424	426	427	finally
    //   428	430	427	finally
  }
  
  public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
    if (!(paramObject instanceof BigDecimal)) {
      setObject(paramInt1, paramObject, paramInt2, 0);
    } else {
      setObject(paramInt1, paramObject, paramInt2, ((BigDecimal)paramObject).scale());
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #8
    //   11: aload #8
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 33
    //   18: aload_0
    //   19: iload_1
    //   20: sipush #1111
    //   23: invokevirtual setNull : (II)V
    //   26: goto -> 818
    //   29: astore_2
    //   30: goto -> 945
    //   33: iload_3
    //   34: bipush #12
    //   36: if_icmpeq -> 784
    //   39: iconst_1
    //   40: istore #5
    //   42: iconst_1
    //   43: istore #6
    //   45: iload_3
    //   46: bipush #16
    //   48: if_icmpeq -> 615
    //   51: iload_3
    //   52: sipush #1111
    //   55: if_icmpeq -> 606
    //   58: iload_3
    //   59: sipush #2004
    //   62: if_icmpeq -> 523
    //   65: iload_3
    //   66: sipush #2005
    //   69: if_icmpeq -> 492
    //   72: iload_3
    //   73: tableswitch default -> 116, -7 -> 475, -6 -> 475, -5 -> 475, -4 -> 523, -3 -> 523, -2 -> 523, -1 -> 784
    //   116: iload_3
    //   117: tableswitch default -> 164, 1 -> 784, 2 -> 475, 3 -> 475, 4 -> 475, 5 -> 475, 6 -> 475, 7 -> 475, 8 -> 475
    //   164: iload_3
    //   165: tableswitch default -> 192, 91 -> 315, 92 -> 209, 93 -> 315
    //   192: ldc_w 'PreparedStatement.16'
    //   195: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   198: ldc_w 'S1000'
    //   201: aload_0
    //   202: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   205: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   208: athrow
    //   209: aload_2
    //   210: instanceof java/lang/String
    //   213: ifeq -> 265
    //   216: aconst_null
    //   217: aload_0
    //   218: aload_2
    //   219: checkcast java/lang/String
    //   222: iconst_1
    //   223: invokespecial getDateTimePattern : (Ljava/lang/String;Z)Ljava/lang/String;
    //   226: aconst_null
    //   227: aconst_null
    //   228: invokestatic getSimpleDateFormat : (Ljava/text/SimpleDateFormat;Ljava/lang/String;Ljava/util/Calendar;Ljava/util/TimeZone;)Ljava/text/SimpleDateFormat;
    //   231: astore #7
    //   233: new java/sql/Time
    //   236: astore #9
    //   238: aload #9
    //   240: aload #7
    //   242: aload_2
    //   243: checkcast java/lang/String
    //   246: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   249: invokevirtual getTime : ()J
    //   252: invokespecial <init> : (J)V
    //   255: aload_0
    //   256: iload_1
    //   257: aload #9
    //   259: invokevirtual setTime : (ILjava/sql/Time;)V
    //   262: goto -> 818
    //   265: aload_2
    //   266: instanceof java/sql/Timestamp
    //   269: ifeq -> 303
    //   272: aload_2
    //   273: checkcast java/sql/Timestamp
    //   276: astore #9
    //   278: new java/sql/Time
    //   281: astore #7
    //   283: aload #7
    //   285: aload #9
    //   287: invokevirtual getTime : ()J
    //   290: invokespecial <init> : (J)V
    //   293: aload_0
    //   294: iload_1
    //   295: aload #7
    //   297: invokevirtual setTime : (ILjava/sql/Time;)V
    //   300: goto -> 818
    //   303: aload_0
    //   304: iload_1
    //   305: aload_2
    //   306: checkcast java/sql/Time
    //   309: invokevirtual setTime : (ILjava/sql/Time;)V
    //   312: goto -> 818
    //   315: aload_2
    //   316: instanceof java/lang/String
    //   319: ifeq -> 362
    //   322: new java/text/ParsePosition
    //   325: astore #7
    //   327: aload #7
    //   329: iconst_0
    //   330: invokespecial <init> : (I)V
    //   333: aconst_null
    //   334: aload_0
    //   335: aload_2
    //   336: checkcast java/lang/String
    //   339: iconst_0
    //   340: invokespecial getDateTimePattern : (Ljava/lang/String;Z)Ljava/lang/String;
    //   343: aconst_null
    //   344: aconst_null
    //   345: invokestatic getSimpleDateFormat : (Ljava/text/SimpleDateFormat;Ljava/lang/String;Ljava/util/Calendar;Ljava/util/TimeZone;)Ljava/text/SimpleDateFormat;
    //   348: aload_2
    //   349: checkcast java/lang/String
    //   352: aload #7
    //   354: invokevirtual parse : (Ljava/lang/String;Ljava/text/ParsePosition;)Ljava/util/Date;
    //   357: astore #7
    //   359: goto -> 368
    //   362: aload_2
    //   363: checkcast java/util/Date
    //   366: astore #7
    //   368: iload_3
    //   369: bipush #91
    //   371: if_icmpeq -> 429
    //   374: iload_3
    //   375: bipush #93
    //   377: if_icmpeq -> 383
    //   380: goto -> 818
    //   383: aload #7
    //   385: instanceof java/sql/Timestamp
    //   388: ifeq -> 404
    //   391: aload_0
    //   392: iload_1
    //   393: aload #7
    //   395: checkcast java/sql/Timestamp
    //   398: invokevirtual setTimestamp : (ILjava/sql/Timestamp;)V
    //   401: goto -> 818
    //   404: new java/sql/Timestamp
    //   407: astore #9
    //   409: aload #9
    //   411: aload #7
    //   413: invokevirtual getTime : ()J
    //   416: invokespecial <init> : (J)V
    //   419: aload_0
    //   420: iload_1
    //   421: aload #9
    //   423: invokevirtual setTimestamp : (ILjava/sql/Timestamp;)V
    //   426: goto -> 818
    //   429: aload #7
    //   431: instanceof java/sql/Date
    //   434: ifeq -> 450
    //   437: aload_0
    //   438: iload_1
    //   439: aload #7
    //   441: checkcast java/sql/Date
    //   444: invokevirtual setDate : (ILjava/sql/Date;)V
    //   447: goto -> 818
    //   450: new java/sql/Date
    //   453: astore #9
    //   455: aload #9
    //   457: aload #7
    //   459: invokevirtual getTime : ()J
    //   462: invokespecial <init> : (J)V
    //   465: aload_0
    //   466: iload_1
    //   467: aload #9
    //   469: invokevirtual setDate : (ILjava/sql/Date;)V
    //   472: goto -> 818
    //   475: aload_0
    //   476: iload_1
    //   477: aload_2
    //   478: iload_3
    //   479: iload #4
    //   481: invokespecial setNumericObject : (ILjava/lang/Object;II)V
    //   484: goto -> 818
    //   487: astore #7
    //   489: goto -> 822
    //   492: aload_2
    //   493: instanceof java/sql/Clob
    //   496: ifeq -> 511
    //   499: aload_0
    //   500: iload_1
    //   501: aload_2
    //   502: checkcast java/sql/Clob
    //   505: invokevirtual setClob : (ILjava/sql/Clob;)V
    //   508: goto -> 818
    //   511: aload_0
    //   512: iload_1
    //   513: aload_2
    //   514: invokevirtual toString : ()Ljava/lang/String;
    //   517: invokevirtual setString : (ILjava/lang/String;)V
    //   520: goto -> 818
    //   523: aload_2
    //   524: instanceof [B
    //   527: ifeq -> 542
    //   530: aload_0
    //   531: iload_1
    //   532: aload_2
    //   533: checkcast [B
    //   536: invokevirtual setBytes : (I[B)V
    //   539: goto -> 818
    //   542: aload_2
    //   543: instanceof java/sql/Blob
    //   546: ifeq -> 561
    //   549: aload_0
    //   550: iload_1
    //   551: aload_2
    //   552: checkcast java/sql/Blob
    //   555: invokevirtual setBlob : (ILjava/sql/Blob;)V
    //   558: goto -> 818
    //   561: aload_0
    //   562: iload_1
    //   563: aload_2
    //   564: invokevirtual toString : ()Ljava/lang/String;
    //   567: aload_0
    //   568: getfield charConverter : Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   571: aload_0
    //   572: getfield charEncoding : Ljava/lang/String;
    //   575: aload_0
    //   576: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   579: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   584: aload_0
    //   585: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   588: invokeinterface parserKnowsUnicode : ()Z
    //   593: aload_0
    //   594: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   597: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   600: invokevirtual setBytes : (I[B)V
    //   603: goto -> 818
    //   606: aload_0
    //   607: iload_1
    //   608: aload_2
    //   609: invokespecial setSerializableObject : (ILjava/lang/Object;)V
    //   612: goto -> 818
    //   615: aload_2
    //   616: instanceof java/lang/Boolean
    //   619: ifeq -> 637
    //   622: aload_0
    //   623: iload_1
    //   624: aload_2
    //   625: checkcast java/lang/Boolean
    //   628: invokevirtual booleanValue : ()Z
    //   631: invokevirtual setBoolean : (IZ)V
    //   634: goto -> 818
    //   637: aload_2
    //   638: instanceof java/lang/String
    //   641: ifeq -> 694
    //   644: iload #6
    //   646: istore #5
    //   648: ldc_w 'true'
    //   651: aload_2
    //   652: checkcast java/lang/String
    //   655: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   658: ifne -> 684
    //   661: ldc_w '0'
    //   664: aload_2
    //   665: checkcast java/lang/String
    //   668: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   671: ifne -> 681
    //   674: iload #6
    //   676: istore #5
    //   678: goto -> 684
    //   681: iconst_0
    //   682: istore #5
    //   684: aload_0
    //   685: iload_1
    //   686: iload #5
    //   688: invokevirtual setBoolean : (IZ)V
    //   691: goto -> 818
    //   694: aload_2
    //   695: instanceof java/lang/Number
    //   698: ifeq -> 727
    //   701: aload_2
    //   702: checkcast java/lang/Number
    //   705: invokevirtual intValue : ()I
    //   708: ifeq -> 714
    //   711: goto -> 717
    //   714: iconst_0
    //   715: istore #5
    //   717: aload_0
    //   718: iload_1
    //   719: iload #5
    //   721: invokevirtual setBoolean : (IZ)V
    //   724: goto -> 818
    //   727: new java/lang/StringBuilder
    //   730: astore #7
    //   732: aload #7
    //   734: invokespecial <init> : ()V
    //   737: aload #7
    //   739: ldc_w 'No conversion from '
    //   742: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   745: pop
    //   746: aload #7
    //   748: aload_2
    //   749: invokevirtual getClass : ()Ljava/lang/Class;
    //   752: invokevirtual getName : ()Ljava/lang/String;
    //   755: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   758: pop
    //   759: aload #7
    //   761: ldc_w ' to Types.BOOLEAN possible.'
    //   764: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   767: pop
    //   768: aload #7
    //   770: invokevirtual toString : ()Ljava/lang/String;
    //   773: ldc_w 'S1009'
    //   776: aload_0
    //   777: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   780: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   783: athrow
    //   784: aload_2
    //   785: instanceof java/math/BigDecimal
    //   788: ifeq -> 809
    //   791: aload_0
    //   792: iload_1
    //   793: aload_2
    //   794: checkcast java/math/BigDecimal
    //   797: invokestatic consistentToString : (Ljava/math/BigDecimal;)Ljava/lang/String;
    //   800: invokestatic fixDecimalExponent : (Ljava/lang/String;)Ljava/lang/String;
    //   803: invokevirtual setString : (ILjava/lang/String;)V
    //   806: goto -> 818
    //   809: aload_0
    //   810: iload_1
    //   811: aload_2
    //   812: invokevirtual toString : ()Ljava/lang/String;
    //   815: invokevirtual setString : (ILjava/lang/String;)V
    //   818: aload #8
    //   820: monitorexit
    //   821: return
    //   822: aload #7
    //   824: instanceof java/sql/SQLException
    //   827: ifeq -> 836
    //   830: aload #7
    //   832: checkcast java/sql/SQLException
    //   835: athrow
    //   836: new java/lang/StringBuilder
    //   839: astore #9
    //   841: aload #9
    //   843: invokespecial <init> : ()V
    //   846: aload #9
    //   848: ldc_w 'PreparedStatement.17'
    //   851: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   854: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   857: pop
    //   858: aload #9
    //   860: aload_2
    //   861: invokevirtual getClass : ()Ljava/lang/Class;
    //   864: invokevirtual toString : ()Ljava/lang/String;
    //   867: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   870: pop
    //   871: aload #9
    //   873: ldc_w 'PreparedStatement.18'
    //   876: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   879: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   882: pop
    //   883: aload #9
    //   885: aload #7
    //   887: invokevirtual getClass : ()Ljava/lang/Class;
    //   890: invokevirtual getName : ()Ljava/lang/String;
    //   893: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   896: pop
    //   897: aload #9
    //   899: ldc_w 'PreparedStatement.19'
    //   902: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   905: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   908: pop
    //   909: aload #9
    //   911: aload #7
    //   913: invokevirtual getMessage : ()Ljava/lang/String;
    //   916: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   919: pop
    //   920: aload #9
    //   922: invokevirtual toString : ()Ljava/lang/String;
    //   925: ldc_w 'S1000'
    //   928: aload_0
    //   929: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   932: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   935: astore_2
    //   936: aload_2
    //   937: aload #7
    //   939: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   942: pop
    //   943: aload_2
    //   944: athrow
    //   945: aload #8
    //   947: monitorexit
    //   948: aload_2
    //   949: athrow
    // Exception table:
    //   from	to	target	type
    //   18	26	29	finally
    //   192	209	487	java/lang/Exception
    //   192	209	29	finally
    //   209	262	487	java/lang/Exception
    //   209	262	29	finally
    //   265	300	487	java/lang/Exception
    //   265	300	29	finally
    //   303	312	487	java/lang/Exception
    //   303	312	29	finally
    //   315	359	487	java/lang/Exception
    //   315	359	29	finally
    //   362	368	487	java/lang/Exception
    //   362	368	29	finally
    //   383	401	487	java/lang/Exception
    //   383	401	29	finally
    //   404	426	487	java/lang/Exception
    //   404	426	29	finally
    //   429	447	487	java/lang/Exception
    //   429	447	29	finally
    //   450	472	487	java/lang/Exception
    //   450	472	29	finally
    //   475	484	487	java/lang/Exception
    //   475	484	29	finally
    //   492	508	487	java/lang/Exception
    //   492	508	29	finally
    //   511	520	487	java/lang/Exception
    //   511	520	29	finally
    //   523	539	487	java/lang/Exception
    //   523	539	29	finally
    //   542	558	487	java/lang/Exception
    //   542	558	29	finally
    //   561	603	487	java/lang/Exception
    //   561	603	29	finally
    //   606	612	487	java/lang/Exception
    //   606	612	29	finally
    //   615	634	487	java/lang/Exception
    //   615	634	29	finally
    //   637	644	487	java/lang/Exception
    //   637	644	29	finally
    //   648	674	487	java/lang/Exception
    //   648	674	29	finally
    //   684	691	487	java/lang/Exception
    //   684	691	29	finally
    //   694	711	487	java/lang/Exception
    //   694	711	29	finally
    //   717	724	487	java/lang/Exception
    //   717	724	29	finally
    //   727	784	487	java/lang/Exception
    //   727	784	29	finally
    //   784	806	487	java/lang/Exception
    //   784	806	29	finally
    //   809	818	487	java/lang/Exception
    //   809	818	29	finally
    //   818	821	29	finally
    //   822	836	29	finally
    //   836	945	29	finally
    //   945	948	29	finally
  }
  
  public int setOneBatchedParameterSet(PreparedStatement paramPreparedStatement, int paramInt, Object paramObject) throws SQLException {
    paramObject = paramObject;
    boolean[] arrayOfBoolean2 = ((BatchParams)paramObject).isNull;
    boolean[] arrayOfBoolean1 = ((BatchParams)paramObject).isStream;
    for (byte b = 0; b < arrayOfBoolean2.length; b++) {
      if (arrayOfBoolean2[b]) {
        int i = paramInt + 1;
        paramPreparedStatement.setNull(paramInt, 0);
        paramInt = i;
      } else if (arrayOfBoolean1[b]) {
        int i = paramInt + 1;
        paramPreparedStatement.setBinaryStream(paramInt, ((BatchParams)paramObject).parameterStreams[b], ((BatchParams)paramObject).streamLengths[b]);
        paramInt = i;
      } else {
        ((PreparedStatement)paramPreparedStatement).setBytesNoEscapeNoQuotes(paramInt, ((BatchParams)paramObject).parameterStrings[b]);
        paramInt++;
      } 
    } 
    return paramInt;
  }
  
  public void setRef(int paramInt, Ref paramRef) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void setRetrieveGeneratedKeys(boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.retrieveGeneratedKeys = paramBoolean;
      return;
    } 
  }
  
  public void setShort(int paramInt, short paramShort) throws SQLException {
    setInternal(paramInt, String.valueOf(paramShort));
    this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 5;
  }
  
  public void setString(int paramInt, String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #8
    //   11: aload #8
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnonnull -> 27
    //   18: aload_0
    //   19: iload_1
    //   20: iconst_1
    //   21: invokevirtual setNull : (II)V
    //   24: goto -> 722
    //   27: aload_0
    //   28: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   31: pop
    //   32: aload_2
    //   33: invokevirtual length : ()I
    //   36: istore #6
    //   38: aload_0
    //   39: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   42: invokeinterface isNoBackslashEscapesSet : ()Z
    //   47: ifeq -> 226
    //   50: aload_0
    //   51: aload_2
    //   52: iload #6
    //   54: invokespecial isEscapeNeededForString : (Ljava/lang/String;I)Z
    //   57: ifne -> 166
    //   60: new java/lang/StringBuilder
    //   63: astore #9
    //   65: aload #9
    //   67: aload_2
    //   68: invokevirtual length : ()I
    //   71: iconst_2
    //   72: iadd
    //   73: invokespecial <init> : (I)V
    //   76: aload #9
    //   78: bipush #39
    //   80: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload #9
    //   86: aload_2
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: aload #9
    //   93: bipush #39
    //   95: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload_0
    //   100: getfield isLoadDataQuery : Z
    //   103: ifne -> 148
    //   106: aload #9
    //   108: invokevirtual toString : ()Ljava/lang/String;
    //   111: aload_0
    //   112: getfield charConverter : Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   115: aload_0
    //   116: getfield charEncoding : Ljava/lang/String;
    //   119: aload_0
    //   120: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   123: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   128: aload_0
    //   129: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   132: invokeinterface parserKnowsUnicode : ()Z
    //   137: aload_0
    //   138: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   141: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   144: astore_2
    //   145: goto -> 157
    //   148: aload #9
    //   150: invokevirtual toString : ()Ljava/lang/String;
    //   153: invokestatic getBytes : (Ljava/lang/String;)[B
    //   156: astore_2
    //   157: aload_0
    //   158: iload_1
    //   159: aload_2
    //   160: invokevirtual setInternal : (I[B)V
    //   163: goto -> 222
    //   166: aload_0
    //   167: getfield isLoadDataQuery : Z
    //   170: ifne -> 211
    //   173: aload_2
    //   174: aload_0
    //   175: getfield charConverter : Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   178: aload_0
    //   179: getfield charEncoding : Ljava/lang/String;
    //   182: aload_0
    //   183: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   186: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   191: aload_0
    //   192: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   195: invokeinterface parserKnowsUnicode : ()Z
    //   200: aload_0
    //   201: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   204: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   207: astore_2
    //   208: goto -> 216
    //   211: aload_2
    //   212: invokestatic getBytes : (Ljava/lang/String;)[B
    //   215: astore_2
    //   216: aload_0
    //   217: iload_1
    //   218: aload_2
    //   219: invokevirtual setBytes : (I[B)V
    //   222: aload #8
    //   224: monitorexit
    //   225: return
    //   226: aload_0
    //   227: getfield isLoadDataQuery : Z
    //   230: istore #7
    //   232: iconst_0
    //   233: istore #5
    //   235: iload #7
    //   237: ifne -> 259
    //   240: aload_0
    //   241: aload_2
    //   242: iload #6
    //   244: invokespecial isEscapeNeededForString : (Ljava/lang/String;I)Z
    //   247: ifeq -> 253
    //   250: goto -> 259
    //   253: iconst_1
    //   254: istore #4
    //   256: goto -> 604
    //   259: new java/lang/StringBuilder
    //   262: astore #11
    //   264: aload #11
    //   266: aload_2
    //   267: invokevirtual length : ()I
    //   270: i2d
    //   271: ldc2_w 1.1
    //   274: dmul
    //   275: d2i
    //   276: invokespecial <init> : (I)V
    //   279: aload #11
    //   281: bipush #39
    //   283: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   286: pop
    //   287: iconst_0
    //   288: istore #4
    //   290: iload #4
    //   292: iload #6
    //   294: if_icmpge -> 586
    //   297: aload_2
    //   298: iload #4
    //   300: invokevirtual charAt : (I)C
    //   303: istore_3
    //   304: iload_3
    //   305: ifeq -> 564
    //   308: iload_3
    //   309: bipush #10
    //   311: if_icmpeq -> 545
    //   314: iload_3
    //   315: bipush #13
    //   317: if_icmpeq -> 526
    //   320: iload_3
    //   321: bipush #26
    //   323: if_icmpeq -> 507
    //   326: iload_3
    //   327: bipush #34
    //   329: if_icmpeq -> 481
    //   332: iload_3
    //   333: bipush #39
    //   335: if_icmpeq -> 462
    //   338: iload_3
    //   339: bipush #92
    //   341: if_icmpeq -> 443
    //   344: iload_3
    //   345: sipush #165
    //   348: if_icmpeq -> 368
    //   351: iload_3
    //   352: sipush #8361
    //   355: if_icmpeq -> 368
    //   358: aload #11
    //   360: iload_3
    //   361: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: goto -> 580
    //   368: aload_0
    //   369: getfield charsetEncoder : Ljava/nio/charset/CharsetEncoder;
    //   372: ifnull -> 433
    //   375: iconst_1
    //   376: invokestatic allocate : (I)Ljava/nio/CharBuffer;
    //   379: astore #10
    //   381: iconst_1
    //   382: invokestatic allocate : (I)Ljava/nio/ByteBuffer;
    //   385: astore #9
    //   387: aload #10
    //   389: iload_3
    //   390: invokevirtual put : (C)Ljava/nio/CharBuffer;
    //   393: pop
    //   394: aload #10
    //   396: iconst_0
    //   397: invokevirtual position : (I)Ljava/nio/Buffer;
    //   400: pop
    //   401: aload_0
    //   402: getfield charsetEncoder : Ljava/nio/charset/CharsetEncoder;
    //   405: aload #10
    //   407: aload #9
    //   409: iconst_1
    //   410: invokevirtual encode : (Ljava/nio/CharBuffer;Ljava/nio/ByteBuffer;Z)Ljava/nio/charset/CoderResult;
    //   413: pop
    //   414: aload #9
    //   416: iconst_0
    //   417: invokevirtual get : (I)B
    //   420: bipush #92
    //   422: if_icmpne -> 433
    //   425: aload #11
    //   427: bipush #92
    //   429: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   432: pop
    //   433: aload #11
    //   435: iload_3
    //   436: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   439: pop
    //   440: goto -> 580
    //   443: aload #11
    //   445: bipush #92
    //   447: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   450: pop
    //   451: aload #11
    //   453: bipush #92
    //   455: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   458: pop
    //   459: goto -> 580
    //   462: aload #11
    //   464: bipush #92
    //   466: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   469: pop
    //   470: aload #11
    //   472: bipush #39
    //   474: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   477: pop
    //   478: goto -> 580
    //   481: aload_0
    //   482: getfield usingAnsiMode : Z
    //   485: ifeq -> 496
    //   488: aload #11
    //   490: bipush #92
    //   492: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   495: pop
    //   496: aload #11
    //   498: bipush #34
    //   500: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   503: pop
    //   504: goto -> 580
    //   507: aload #11
    //   509: bipush #92
    //   511: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   514: pop
    //   515: aload #11
    //   517: bipush #90
    //   519: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   522: pop
    //   523: goto -> 580
    //   526: aload #11
    //   528: bipush #92
    //   530: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   533: pop
    //   534: aload #11
    //   536: bipush #114
    //   538: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   541: pop
    //   542: goto -> 580
    //   545: aload #11
    //   547: bipush #92
    //   549: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   552: pop
    //   553: aload #11
    //   555: bipush #110
    //   557: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   560: pop
    //   561: goto -> 580
    //   564: aload #11
    //   566: bipush #92
    //   568: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload #11
    //   574: bipush #48
    //   576: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   579: pop
    //   580: iinc #4, 1
    //   583: goto -> 290
    //   586: aload #11
    //   588: bipush #39
    //   590: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   593: pop
    //   594: aload #11
    //   596: invokevirtual toString : ()Ljava/lang/String;
    //   599: astore_2
    //   600: iload #5
    //   602: istore #4
    //   604: aload_0
    //   605: getfield isLoadDataQuery : Z
    //   608: ifne -> 696
    //   611: iload #4
    //   613: ifeq -> 658
    //   616: aload_2
    //   617: bipush #39
    //   619: bipush #39
    //   621: aload_0
    //   622: getfield charConverter : Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   625: aload_0
    //   626: getfield charEncoding : Ljava/lang/String;
    //   629: aload_0
    //   630: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   633: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   638: aload_0
    //   639: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   642: invokeinterface parserKnowsUnicode : ()Z
    //   647: aload_0
    //   648: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   651: invokestatic getBytesWrapped : (Ljava/lang/String;CCLcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   654: astore_2
    //   655: goto -> 701
    //   658: aload_2
    //   659: aload_0
    //   660: getfield charConverter : Lcom/mysql/jdbc/SingleByteCharsetConverter;
    //   663: aload_0
    //   664: getfield charEncoding : Ljava/lang/String;
    //   667: aload_0
    //   668: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   671: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   676: aload_0
    //   677: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   680: invokeinterface parserKnowsUnicode : ()Z
    //   685: aload_0
    //   686: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   689: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   692: astore_2
    //   693: goto -> 701
    //   696: aload_2
    //   697: invokestatic getBytes : (Ljava/lang/String;)[B
    //   700: astore_2
    //   701: aload_0
    //   702: iload_1
    //   703: aload_2
    //   704: invokevirtual setInternal : (I[B)V
    //   707: aload_0
    //   708: getfield parameterTypes : [I
    //   711: iload_1
    //   712: iconst_1
    //   713: isub
    //   714: aload_0
    //   715: invokevirtual getParameterIndexOffset : ()I
    //   718: iadd
    //   719: bipush #12
    //   721: iastore
    //   722: aload #8
    //   724: monitorexit
    //   725: return
    //   726: astore_2
    //   727: aload #8
    //   729: monitorexit
    //   730: aload_2
    //   731: athrow
    // Exception table:
    //   from	to	target	type
    //   18	24	726	finally
    //   27	145	726	finally
    //   148	157	726	finally
    //   157	163	726	finally
    //   166	208	726	finally
    //   211	216	726	finally
    //   216	222	726	finally
    //   222	225	726	finally
    //   226	232	726	finally
    //   240	250	726	finally
    //   259	287	726	finally
    //   297	304	726	finally
    //   358	365	726	finally
    //   368	433	726	finally
    //   433	440	726	finally
    //   443	459	726	finally
    //   462	478	726	finally
    //   481	496	726	finally
    //   496	504	726	finally
    //   507	523	726	finally
    //   526	542	726	finally
    //   545	561	726	finally
    //   564	580	726	finally
    //   586	600	726	finally
    //   604	611	726	finally
    //   616	655	726	finally
    //   658	693	726	finally
    //   696	701	726	finally
    //   701	722	726	finally
    //   722	725	726	finally
    //   727	730	726	finally
  }
  
  public void setTime(int paramInt, Time paramTime) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setTimeInternal(paramInt, paramTime, null, this.connection.getDefaultTimeZone(), false);
      return;
    } 
  }
  
  public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setTimeInternal(paramInt, paramTime, paramCalendar, paramCalendar.getTimeZone(), true);
      return;
    } 
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield sendFractionalSeconds : Z
    //   18: ifeq -> 81
    //   21: aload_0
    //   22: getfield serverSupportsFracSecs : Z
    //   25: ifne -> 31
    //   28: goto -> 81
    //   31: aload_0
    //   32: getfield parameterMetaData : Lcom/mysql/jdbc/MysqlParameterMetadata;
    //   35: astore #5
    //   37: aload #5
    //   39: ifnull -> 76
    //   42: aload #5
    //   44: getfield metadata : Lcom/mysql/jdbc/ResultSetMetaData;
    //   47: astore #5
    //   49: iload_1
    //   50: aload #5
    //   52: getfield fields : [Lcom/mysql/jdbc/Field;
    //   55: arraylength
    //   56: if_icmpgt -> 76
    //   59: iload_1
    //   60: iflt -> 76
    //   63: aload #5
    //   65: iload_1
    //   66: invokevirtual getField : (I)Lcom/mysql/jdbc/Field;
    //   69: invokevirtual getDecimals : ()I
    //   72: istore_3
    //   73: goto -> 83
    //   76: iconst_m1
    //   77: istore_3
    //   78: goto -> 83
    //   81: iconst_0
    //   82: istore_3
    //   83: aload_0
    //   84: iload_1
    //   85: aload_2
    //   86: aconst_null
    //   87: aload_0
    //   88: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   91: invokeinterface getDefaultTimeZone : ()Ljava/util/TimeZone;
    //   96: iconst_0
    //   97: iload_3
    //   98: aload_0
    //   99: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   102: invokeinterface getUseSSPSCompatibleTimezoneShift : ()Z
    //   107: invokevirtual setTimestampInternal : (ILjava/sql/Timestamp;Ljava/util/Calendar;Ljava/util/TimeZone;ZIZ)V
    //   110: aload #4
    //   112: monitorexit
    //   113: return
    //   114: astore_2
    //   115: aload #4
    //   117: monitorexit
    //   118: aload_2
    //   119: athrow
    // Exception table:
    //   from	to	target	type
    //   14	28	114	finally
    //   31	37	114	finally
    //   42	59	114	finally
    //   63	73	114	finally
    //   83	113	114	finally
    //   115	118	114	finally
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #5
    //   11: aload #5
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield sendFractionalSeconds : Z
    //   18: ifeq -> 100
    //   21: aload_0
    //   22: getfield serverSupportsFracSecs : Z
    //   25: ifne -> 31
    //   28: goto -> 100
    //   31: aload_0
    //   32: getfield parameterMetaData : Lcom/mysql/jdbc/MysqlParameterMetadata;
    //   35: astore #6
    //   37: aload #6
    //   39: ifnull -> 94
    //   42: aload #6
    //   44: getfield metadata : Lcom/mysql/jdbc/ResultSetMetaData;
    //   47: astore #6
    //   49: iload_1
    //   50: aload #6
    //   52: getfield fields : [Lcom/mysql/jdbc/Field;
    //   55: arraylength
    //   56: if_icmpgt -> 94
    //   59: iload_1
    //   60: iflt -> 94
    //   63: aload #6
    //   65: iload_1
    //   66: invokevirtual getField : (I)Lcom/mysql/jdbc/Field;
    //   69: invokevirtual getDecimals : ()I
    //   72: ifle -> 94
    //   75: aload_0
    //   76: getfield parameterMetaData : Lcom/mysql/jdbc/MysqlParameterMetadata;
    //   79: getfield metadata : Lcom/mysql/jdbc/ResultSetMetaData;
    //   82: iload_1
    //   83: invokevirtual getField : (I)Lcom/mysql/jdbc/Field;
    //   86: invokevirtual getDecimals : ()I
    //   89: istore #4
    //   91: goto -> 103
    //   94: iconst_m1
    //   95: istore #4
    //   97: goto -> 103
    //   100: iconst_0
    //   101: istore #4
    //   103: aload_0
    //   104: iload_1
    //   105: aload_2
    //   106: aload_3
    //   107: aload_3
    //   108: invokevirtual getTimeZone : ()Ljava/util/TimeZone;
    //   111: iconst_1
    //   112: iload #4
    //   114: aload_0
    //   115: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   118: invokeinterface getUseSSPSCompatibleTimezoneShift : ()Z
    //   123: invokevirtual setTimestampInternal : (ILjava/sql/Timestamp;Ljava/util/Calendar;Ljava/util/TimeZone;ZIZ)V
    //   126: aload #5
    //   128: monitorexit
    //   129: return
    //   130: astore_2
    //   131: aload #5
    //   133: monitorexit
    //   134: aload_2
    //   135: athrow
    // Exception table:
    //   from	to	target	type
    //   14	28	130	finally
    //   31	37	130	finally
    //   42	59	130	finally
    //   63	91	130	finally
    //   103	129	130	finally
    //   131	134	130	finally
  }
  
  public void setTimestampInternal(int paramInt1, Timestamp paramTimestamp, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull -> 14
    //   4: aload_0
    //   5: iload_1
    //   6: bipush #93
    //   8: invokevirtual setNull : (II)V
    //   11: goto -> 330
    //   14: aload_0
    //   15: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   18: pop
    //   19: aload_2
    //   20: invokevirtual clone : ()Ljava/lang/Object;
    //   23: checkcast java/sql/Timestamp
    //   26: astore #9
    //   28: aload_0
    //   29: getfield serverSupportsFracSecs : Z
    //   32: ifeq -> 53
    //   35: aload #9
    //   37: astore_2
    //   38: aload_0
    //   39: getfield sendFractionalSeconds : Z
    //   42: ifne -> 59
    //   45: aload #9
    //   47: astore_2
    //   48: iload #6
    //   50: ifne -> 59
    //   53: aload #9
    //   55: invokestatic truncateFractionalSeconds : (Ljava/sql/Timestamp;)Ljava/sql/Timestamp;
    //   58: astore_2
    //   59: iload #6
    //   61: ifge -> 71
    //   64: bipush #6
    //   66: istore #6
    //   68: goto -> 71
    //   71: aload_2
    //   72: iload #6
    //   74: aload_0
    //   75: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   78: invokeinterface isServerTruncatesFracSecs : ()Z
    //   83: iconst_1
    //   84: ixor
    //   85: invokestatic adjustTimestampNanosPrecision : (Ljava/sql/Timestamp;IZ)Ljava/sql/Timestamp;
    //   88: astore #9
    //   90: aload_0
    //   91: getfield useLegacyDatetimeCode : Z
    //   94: ifne -> 108
    //   97: aload_0
    //   98: iload_1
    //   99: aload #9
    //   101: aload_3
    //   102: invokespecial newSetTimestampInternal : (ILjava/sql/Timestamp;Ljava/util/Calendar;)V
    //   105: goto -> 315
    //   108: aload_0
    //   109: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   112: invokeinterface getUseJDBCCompliantTimezoneShift : ()Z
    //   117: ifeq -> 133
    //   120: aload_0
    //   121: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   124: invokeinterface getUtcCalendar : ()Ljava/util/Calendar;
    //   129: astore_2
    //   130: goto -> 138
    //   133: aload_0
    //   134: invokevirtual getCalendarInstanceForSessionOrNew : ()Ljava/util/Calendar;
    //   137: astore_2
    //   138: aload_2
    //   139: aload_3
    //   140: invokestatic setProlepticIfNeeded : (Ljava/util/Calendar;Ljava/util/Calendar;)Ljava/util/Calendar;
    //   143: astore_2
    //   144: aload_0
    //   145: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   148: aload_2
    //   149: aload_3
    //   150: aload #9
    //   152: aload #4
    //   154: aload_0
    //   155: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   158: invokeinterface getServerTimezoneTZ : ()Ljava/util/TimeZone;
    //   163: iload #5
    //   165: invokestatic changeTimezone : (Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   168: astore_2
    //   169: iload #7
    //   171: ifeq -> 186
    //   174: aload_0
    //   175: iload_1
    //   176: aload_2
    //   177: iload #6
    //   179: aload_3
    //   180: invokespecial doSSPSCompatibleTimezoneShift : (ILjava/sql/Timestamp;ILjava/util/Calendar;)V
    //   183: goto -> 315
    //   186: aload_0
    //   187: monitorenter
    //   188: aload_0
    //   189: getfield tsdf : Ljava/text/SimpleDateFormat;
    //   192: ldc_w '''yyyy-MM-dd HH:mm:ss'
    //   195: aconst_null
    //   196: aconst_null
    //   197: invokestatic getSimpleDateFormat : (Ljava/text/SimpleDateFormat;Ljava/lang/String;Ljava/util/Calendar;Ljava/util/TimeZone;)Ljava/text/SimpleDateFormat;
    //   200: astore #4
    //   202: aload_0
    //   203: aload #4
    //   205: putfield tsdf : Ljava/text/SimpleDateFormat;
    //   208: aload #4
    //   210: invokevirtual getCalendar : ()Ljava/util/Calendar;
    //   213: aload_3
    //   214: invokestatic setProlepticIfNeeded : (Ljava/util/Calendar;Ljava/util/Calendar;)Ljava/util/Calendar;
    //   217: astore_3
    //   218: aload_0
    //   219: getfield tsdf : Ljava/text/SimpleDateFormat;
    //   222: invokevirtual getCalendar : ()Ljava/util/Calendar;
    //   225: aload_3
    //   226: if_acmpeq -> 237
    //   229: aload_0
    //   230: getfield tsdf : Ljava/text/SimpleDateFormat;
    //   233: aload_3
    //   234: invokevirtual setCalendar : (Ljava/util/Calendar;)V
    //   237: new java/lang/StringBuffer
    //   240: astore_3
    //   241: aload_3
    //   242: invokespecial <init> : ()V
    //   245: aload_3
    //   246: aload_0
    //   247: getfield tsdf : Ljava/text/SimpleDateFormat;
    //   250: aload_2
    //   251: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   257: pop
    //   258: iload #6
    //   260: ifle -> 297
    //   263: aload_2
    //   264: invokevirtual getNanos : ()I
    //   267: istore #8
    //   269: iload #8
    //   271: ifeq -> 297
    //   274: aload_3
    //   275: bipush #46
    //   277: invokevirtual append : (C)Ljava/lang/StringBuffer;
    //   280: pop
    //   281: aload_3
    //   282: iload #8
    //   284: aload_0
    //   285: getfield serverSupportsFracSecs : Z
    //   288: iload #6
    //   290: invokestatic formatNanos : (IZI)Ljava/lang/String;
    //   293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   296: pop
    //   297: aload_3
    //   298: bipush #39
    //   300: invokevirtual append : (C)Ljava/lang/StringBuffer;
    //   303: pop
    //   304: aload_0
    //   305: iload_1
    //   306: aload_3
    //   307: invokevirtual toString : ()Ljava/lang/String;
    //   310: invokevirtual setInternal : (ILjava/lang/String;)V
    //   313: aload_0
    //   314: monitorexit
    //   315: aload_0
    //   316: getfield parameterTypes : [I
    //   319: iload_1
    //   320: iconst_1
    //   321: isub
    //   322: aload_0
    //   323: invokevirtual getParameterIndexOffset : ()I
    //   326: iadd
    //   327: bipush #93
    //   329: iastore
    //   330: return
    //   331: astore_2
    //   332: aload_0
    //   333: monitorexit
    //   334: aload_2
    //   335: athrow
    // Exception table:
    //   from	to	target	type
    //   188	237	331	finally
    //   237	258	331	finally
    //   263	269	331	finally
    //   274	297	331	finally
    //   297	315	331	finally
    //   332	334	331	finally
  }
  
  public void setURL(int paramInt, URL paramURL) throws SQLException {
    if (paramURL != null) {
      setString(paramInt, paramURL.toString());
      this.parameterTypes[paramInt - 1 + getParameterIndexOffset()] = 70;
    } else {
      setNull(paramInt, 1);
    } 
  }
  
  @Deprecated
  public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    if (paramInputStream == null) {
      setNull(paramInt1, 12);
    } else {
      setBinaryStream(paramInt1, paramInputStream, paramInt2);
      this.parameterTypes[paramInt1 - 1 + getParameterIndexOffset()] = 2005;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append(": ");
    try {
      stringBuilder.append(asSql());
    } catch (SQLException sQLException) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("EXCEPTION: ");
      stringBuilder1.append(sQLException.toString());
      stringBuilder.append(stringBuilder1.toString());
    } 
    return stringBuilder.toString();
  }
  
  static {
    if (Util.isJdbc4()) {
      try {
        String str;
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.JDBC42PreparedStatement";
        } else {
          str = "com.mysql.jdbc.JDBC4PreparedStatement";
        } 
        JDBC_4_PSTMT_2_ARG_CTOR = Class.forName(str).getConstructor(new Class[] { MySQLConnection.class, String.class });
        JDBC_4_PSTMT_3_ARG_CTOR = Class.forName(str).getConstructor(new Class[] { MySQLConnection.class, String.class, String.class });
        JDBC_4_PSTMT_4_ARG_CTOR = Class.forName(str).getConstructor(new Class[] { MySQLConnection.class, String.class, String.class, ParseInfo.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_PSTMT_2_ARG_CTOR = null;
      JDBC_4_PSTMT_3_ARG_CTOR = null;
      JDBC_4_PSTMT_4_ARG_CTOR = null;
    } 
  }
  
  public static class AppendingBatchVisitor implements BatchVisitor {
    public LinkedList<byte[]> statementComponents = (LinkedList)new LinkedList<byte>();
    
    public PreparedStatement.BatchVisitor append(byte[] param1ArrayOfbyte) {
      this.statementComponents.addLast(param1ArrayOfbyte);
      return this;
    }
    
    public PreparedStatement.BatchVisitor decrement() {
      this.statementComponents.removeLast();
      return this;
    }
    
    public byte[][] getStaticSqlStrings() {
      byte[][] arrayOfByte = new byte[this.statementComponents.size()][];
      this.statementComponents.toArray(arrayOfByte);
      return arrayOfByte;
    }
    
    public PreparedStatement.BatchVisitor increment() {
      return this;
    }
    
    public PreparedStatement.BatchVisitor merge(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2) {
      byte[] arrayOfByte = new byte[param1ArrayOfbyte1.length + param1ArrayOfbyte2.length];
      System.arraycopy(param1ArrayOfbyte1, 0, arrayOfByte, 0, param1ArrayOfbyte1.length);
      System.arraycopy(param1ArrayOfbyte2, 0, arrayOfByte, param1ArrayOfbyte1.length, param1ArrayOfbyte2.length);
      this.statementComponents.addLast(arrayOfByte);
      return this;
    }
    
    public PreparedStatement.BatchVisitor mergeWithLast(byte[] param1ArrayOfbyte) {
      return this.statementComponents.isEmpty() ? append(param1ArrayOfbyte) : merge(this.statementComponents.removeLast(), param1ArrayOfbyte);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      Iterator<byte> iterator = this.statementComponents.iterator();
      while (iterator.hasNext())
        stringBuilder.append(StringUtils.toString((byte[])iterator.next())); 
      return stringBuilder.toString();
    }
  }
  
  public class BatchParams {
    public boolean[] isNull = null;
    
    public boolean[] isStream = null;
    
    public InputStream[] parameterStreams = null;
    
    public byte[][] parameterStrings = null;
    
    public int[] streamLengths = null;
    
    public final PreparedStatement this$0;
    
    public BatchParams(byte[][] param1ArrayOfbyte, InputStream[] param1ArrayOfInputStream, boolean[] param1ArrayOfboolean1, int[] param1ArrayOfint, boolean[] param1ArrayOfboolean2) {
      byte[][] arrayOfByte = new byte[param1ArrayOfbyte.length][];
      this.parameterStrings = arrayOfByte;
      this.parameterStreams = new InputStream[param1ArrayOfInputStream.length];
      this.isStream = new boolean[param1ArrayOfboolean1.length];
      this.streamLengths = new int[param1ArrayOfint.length];
      this.isNull = new boolean[param1ArrayOfboolean2.length];
      System.arraycopy(param1ArrayOfbyte, 0, arrayOfByte, 0, param1ArrayOfbyte.length);
      System.arraycopy(param1ArrayOfInputStream, 0, this.parameterStreams, 0, param1ArrayOfInputStream.length);
      System.arraycopy(param1ArrayOfboolean1, 0, this.isStream, 0, param1ArrayOfboolean1.length);
      System.arraycopy(param1ArrayOfint, 0, this.streamLengths, 0, param1ArrayOfint.length);
      System.arraycopy(param1ArrayOfboolean2, 0, this.isNull, 0, param1ArrayOfboolean2.length);
    }
  }
  
  public static interface BatchVisitor {
    BatchVisitor append(byte[] param1ArrayOfbyte);
    
    BatchVisitor decrement();
    
    BatchVisitor increment();
    
    BatchVisitor merge(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2);
    
    BatchVisitor mergeWithLast(byte[] param1ArrayOfbyte);
  }
  
  public class EmulatedPreparedStatementBindings implements ParameterBindings {
    private ResultSetImpl bindingsAsRs;
    
    private boolean[] parameterIsNull;
    
    public final PreparedStatement this$0;
    
    public EmulatedPreparedStatementBindings() throws SQLException {
      SQLException sQLException;
      ArrayList arrayList = new ArrayList();
      int j = PreparedStatement.this.parameterCount;
      boolean[] arrayOfBoolean2 = new boolean[j];
      this.parameterIsNull = arrayOfBoolean2;
      boolean[] arrayOfBoolean1 = PreparedStatement.this.isNull;
      int i = 0;
      System.arraycopy(arrayOfBoolean1, 0, arrayOfBoolean2, 0, j);
      j = PreparedStatement.this.parameterCount;
      byte[][] arrayOfByte = new byte[j][];
      Field[] arrayOfField = new Field[j];
      while (i < PreparedStatement.this.parameterCount) {
        j = PreparedStatement.this.batchCommandIndex;
        if (j == -1) {
          arrayOfByte[i] = PreparedStatement.this.getBytesRepresentation(i);
        } else {
          arrayOfByte[i] = PreparedStatement.this.getBytesRepresentationForBatch(i, j);
        } 
        int[] arrayOfInt = PreparedStatement.this.parameterTypes;
        if (arrayOfInt[i] == -2 || arrayOfInt[i] == 2004) {
          j = 63;
        } else {
          try {
            j = CharsetMapping.getCollationIndexForJavaEncoding(PreparedStatement.this.connection.getEncoding(), PreparedStatement.this.connection);
          } catch (SQLException null) {
            throw sQLException;
          } catch (RuntimeException runtimeException) {
            sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
            sQLException.initCause(runtimeException);
            throw sQLException;
          } 
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("parameter_");
        int k = i + 1;
        stringBuilder.append(k);
        Field field = new Field(null, stringBuilder.toString(), j, ((PreparedStatement)sQLException).parameterTypes[i], (arrayOfByte[i]).length);
        field.setConnection(((StatementImpl)sQLException).connection);
        arrayOfField[i] = field;
        i = k;
      } 
      runtimeException.add(new ByteArrayRow(arrayOfByte, sQLException.getExceptionInterceptor()));
      ResultSetImpl resultSetImpl = new ResultSetImpl(((StatementImpl)sQLException).connection.getCatalog(), arrayOfField, new RowDataStatic((List<ResultSetRow>)runtimeException), ((StatementImpl)sQLException).connection, null);
      this.bindingsAsRs = resultSetImpl;
      resultSetImpl.next();
    }
    
    public Array getArray(int param1Int) throws SQLException {
      return this.bindingsAsRs.getArray(param1Int);
    }
    
    public InputStream getAsciiStream(int param1Int) throws SQLException {
      return this.bindingsAsRs.getAsciiStream(param1Int);
    }
    
    public BigDecimal getBigDecimal(int param1Int) throws SQLException {
      return this.bindingsAsRs.getBigDecimal(param1Int);
    }
    
    public InputStream getBinaryStream(int param1Int) throws SQLException {
      return this.bindingsAsRs.getBinaryStream(param1Int);
    }
    
    public Blob getBlob(int param1Int) throws SQLException {
      return this.bindingsAsRs.getBlob(param1Int);
    }
    
    public boolean getBoolean(int param1Int) throws SQLException {
      return this.bindingsAsRs.getBoolean(param1Int);
    }
    
    public byte getByte(int param1Int) throws SQLException {
      return this.bindingsAsRs.getByte(param1Int);
    }
    
    public byte[] getBytes(int param1Int) throws SQLException {
      return this.bindingsAsRs.getBytes(param1Int);
    }
    
    public Reader getCharacterStream(int param1Int) throws SQLException {
      return this.bindingsAsRs.getCharacterStream(param1Int);
    }
    
    public Clob getClob(int param1Int) throws SQLException {
      return this.bindingsAsRs.getClob(param1Int);
    }
    
    public Date getDate(int param1Int) throws SQLException {
      return this.bindingsAsRs.getDate(param1Int);
    }
    
    public double getDouble(int param1Int) throws SQLException {
      return this.bindingsAsRs.getDouble(param1Int);
    }
    
    public float getFloat(int param1Int) throws SQLException {
      return this.bindingsAsRs.getFloat(param1Int);
    }
    
    public int getInt(int param1Int) throws SQLException {
      return this.bindingsAsRs.getInt(param1Int);
    }
    
    public long getLong(int param1Int) throws SQLException {
      return this.bindingsAsRs.getLong(param1Int);
    }
    
    public Reader getNCharacterStream(int param1Int) throws SQLException {
      return this.bindingsAsRs.getCharacterStream(param1Int);
    }
    
    public Reader getNClob(int param1Int) throws SQLException {
      return this.bindingsAsRs.getCharacterStream(param1Int);
    }
    
    public Object getObject(int param1Int) throws SQLException {
      PreparedStatement.this.checkBounds(param1Int, 0);
      boolean[] arrayOfBoolean = this.parameterIsNull;
      int i = param1Int - 1;
      if (arrayOfBoolean[i])
        return null; 
      i = PreparedStatement.this.parameterTypes[i];
      return (i != -6) ? ((i != -5) ? ((i != 4) ? ((i != 5) ? ((i != 6) ? ((i != 8) ? this.bindingsAsRs.getObject(param1Int) : Double.valueOf(getDouble(param1Int))) : Float.valueOf(getFloat(param1Int))) : Short.valueOf(getShort(param1Int))) : Integer.valueOf(getInt(param1Int))) : Long.valueOf(getLong(param1Int))) : Byte.valueOf(getByte(param1Int));
    }
    
    public Ref getRef(int param1Int) throws SQLException {
      return this.bindingsAsRs.getRef(param1Int);
    }
    
    public short getShort(int param1Int) throws SQLException {
      return this.bindingsAsRs.getShort(param1Int);
    }
    
    public String getString(int param1Int) throws SQLException {
      return this.bindingsAsRs.getString(param1Int);
    }
    
    public Time getTime(int param1Int) throws SQLException {
      return this.bindingsAsRs.getTime(param1Int);
    }
    
    public Timestamp getTimestamp(int param1Int) throws SQLException {
      return this.bindingsAsRs.getTimestamp(param1Int);
    }
    
    public URL getURL(int param1Int) throws SQLException {
      return this.bindingsAsRs.getURL(param1Int);
    }
    
    public boolean isNull(int param1Int) throws SQLException {
      PreparedStatement.this.checkBounds(param1Int, 0);
      return this.parameterIsNull[param1Int - 1];
    }
  }
  
  public class EndPoint {
    public int begin;
    
    public int end;
    
    public final PreparedStatement this$0;
    
    public EndPoint(int param1Int1, int param1Int2) {
      this.begin = param1Int1;
      this.end = param1Int2;
    }
  }
  
  public static final class ParseInfo {
    private ParseInfo batchHead;
    
    private ParseInfo batchODKUClause;
    
    private ParseInfo batchValues;
    
    public boolean canRewriteAsMultiValueInsert = false;
    
    public String charEncoding;
    
    public char firstStmtChar = Character.MIN_VALUE;
    
    public boolean foundLoadData = false;
    
    public boolean hasPlaceholders = false;
    
    public boolean isOnDuplicateKeyUpdate = false;
    
    public long lastUsed = 0L;
    
    public int locationOfOnDuplicateKeyUpdate = -1;
    
    public int numberOfQueries = 1;
    
    public boolean parametersInDuplicateKeyClause = false;
    
    public int statementLength = 0;
    
    public int statementStartPos = 0;
    
    public byte[][] staticSql = null;
    
    public String valuesClause;
    
    public ParseInfo(String param1String1, MySQLConnection param1MySQLConnection, DatabaseMetaData param1DatabaseMetaData, String param1String2, SingleByteCharsetConverter param1SingleByteCharsetConverter) throws SQLException {
      this(param1String1, param1MySQLConnection, param1DatabaseMetaData, param1String2, param1SingleByteCharsetConverter, true);
    }
    
    public ParseInfo(String param1String1, MySQLConnection param1MySQLConnection, DatabaseMetaData param1DatabaseMetaData, String param1String2, SingleByteCharsetConverter param1SingleByteCharsetConverter, boolean param1Boolean) throws SQLException {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial <init> : ()V
      //   4: aload_0
      //   5: iconst_0
      //   6: putfield firstStmtChar : C
      //   9: aload_0
      //   10: iconst_0
      //   11: putfield foundLoadData : Z
      //   14: aload_0
      //   15: lconst_0
      //   16: putfield lastUsed : J
      //   19: aload_0
      //   20: iconst_0
      //   21: putfield statementLength : I
      //   24: aload_0
      //   25: iconst_0
      //   26: putfield statementStartPos : I
      //   29: aload_0
      //   30: iconst_0
      //   31: putfield canRewriteAsMultiValueInsert : Z
      //   34: aload_0
      //   35: aconst_null
      //   36: putfield staticSql : [[B
      //   39: aload_0
      //   40: iconst_0
      //   41: putfield hasPlaceholders : Z
      //   44: aload_0
      //   45: iconst_1
      //   46: putfield numberOfQueries : I
      //   49: aload_0
      //   50: iconst_0
      //   51: putfield isOnDuplicateKeyUpdate : Z
      //   54: aload_0
      //   55: iconst_m1
      //   56: putfield locationOfOnDuplicateKeyUpdate : I
      //   59: aload_0
      //   60: iconst_0
      //   61: putfield parametersInDuplicateKeyClause : Z
      //   64: aload_1
      //   65: ifnull -> 1660
      //   68: aload_0
      //   69: aload #4
      //   71: putfield charEncoding : Ljava/lang/String;
      //   74: aload_0
      //   75: invokestatic currentTimeMillis : ()J
      //   78: putfield lastUsed : J
      //   81: aload_3
      //   82: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
      //   87: astore #26
      //   89: aload #26
      //   91: ifnull -> 123
      //   94: aload #26
      //   96: ldc ' '
      //   98: invokevirtual equals : (Ljava/lang/Object;)Z
      //   101: ifne -> 123
      //   104: aload #26
      //   106: invokevirtual length : ()I
      //   109: ifle -> 123
      //   112: aload #26
      //   114: iconst_0
      //   115: invokevirtual charAt : (I)C
      //   118: istore #17
      //   120: goto -> 126
      //   123: iconst_0
      //   124: istore #17
      //   126: aload_0
      //   127: aload_1
      //   128: invokevirtual length : ()I
      //   131: putfield statementLength : I
      //   134: new java/util/ArrayList
      //   137: astore #26
      //   139: aload #26
      //   141: invokespecial <init> : ()V
      //   144: aload_2
      //   145: invokeinterface isNoBackslashEscapesSet : ()Z
      //   150: istore #25
      //   152: aload_1
      //   153: invokestatic findStartOfStatement : (Ljava/lang/String;)I
      //   156: istore #9
      //   158: aload_0
      //   159: iload #9
      //   161: putfield statementStartPos : I
      //   164: iconst_0
      //   165: istore #18
      //   167: iconst_0
      //   168: istore #14
      //   170: iconst_0
      //   171: istore #13
      //   173: iconst_0
      //   174: istore #12
      //   176: iload #9
      //   178: aload_0
      //   179: getfield statementLength : I
      //   182: if_icmpge -> 1268
      //   185: aload_1
      //   186: iload #9
      //   188: invokevirtual charAt : (I)C
      //   191: istore #7
      //   193: aload_0
      //   194: getfield firstStmtChar : C
      //   197: ifne -> 279
      //   200: iload #7
      //   202: invokestatic isLetter : (C)Z
      //   205: ifeq -> 279
      //   208: iload #7
      //   210: invokestatic toUpperCase : (C)C
      //   213: istore #8
      //   215: aload_0
      //   216: iload #8
      //   218: putfield firstStmtChar : C
      //   221: iload #8
      //   223: bipush #73
      //   225: if_icmpne -> 279
      //   228: aload_1
      //   229: aload_2
      //   230: invokeinterface getDontCheckOnDuplicateKeyUpdateInSQL : ()Z
      //   235: aload_2
      //   236: invokeinterface getRewriteBatchedStatements : ()Z
      //   241: aload_2
      //   242: invokeinterface isNoBackslashEscapesSet : ()Z
      //   247: invokestatic getOnDuplicateKeyLocation : (Ljava/lang/String;ZZZ)I
      //   250: istore #10
      //   252: aload_0
      //   253: iload #10
      //   255: putfield locationOfOnDuplicateKeyUpdate : I
      //   258: iload #10
      //   260: iconst_m1
      //   261: if_icmpeq -> 270
      //   264: iconst_1
      //   265: istore #24
      //   267: goto -> 273
      //   270: iconst_0
      //   271: istore #24
      //   273: aload_0
      //   274: iload #24
      //   276: putfield isOnDuplicateKeyUpdate : Z
      //   279: iload #25
      //   281: ifne -> 324
      //   284: iload #7
      //   286: bipush #92
      //   288: if_icmpne -> 324
      //   291: iload #9
      //   293: aload_0
      //   294: getfield statementLength : I
      //   297: iconst_1
      //   298: isub
      //   299: if_icmpge -> 324
      //   302: iinc #9, 1
      //   305: iload #18
      //   307: istore #20
      //   309: iload #14
      //   311: istore #22
      //   313: iload #13
      //   315: istore #21
      //   317: iload #12
      //   319: istore #19
      //   321: goto -> 1246
      //   324: iload #14
      //   326: ifne -> 366
      //   329: iload #17
      //   331: ifeq -> 366
      //   334: iload #7
      //   336: iload #17
      //   338: if_icmpne -> 366
      //   341: iload #12
      //   343: iconst_1
      //   344: ixor
      //   345: istore #16
      //   347: iload #9
      //   349: istore #15
      //   351: iload #14
      //   353: istore #10
      //   355: iload #13
      //   357: istore #11
      //   359: iload #7
      //   361: istore #23
      //   363: goto -> 946
      //   366: iload #9
      //   368: istore #15
      //   370: iload #14
      //   372: istore #10
      //   374: iload #13
      //   376: istore #11
      //   378: iload #12
      //   380: istore #16
      //   382: iload #7
      //   384: istore #23
      //   386: iload #12
      //   388: ifne -> 946
      //   391: iload #14
      //   393: ifeq -> 553
      //   396: iload #7
      //   398: bipush #39
      //   400: if_icmpeq -> 410
      //   403: iload #7
      //   405: bipush #34
      //   407: if_icmpne -> 468
      //   410: iload #7
      //   412: iload #13
      //   414: if_icmpne -> 468
      //   417: iload #9
      //   419: aload_0
      //   420: getfield statementLength : I
      //   423: iconst_1
      //   424: isub
      //   425: if_icmpge -> 529
      //   428: iload #9
      //   430: iconst_1
      //   431: iadd
      //   432: istore #10
      //   434: aload_1
      //   435: iload #10
      //   437: invokevirtual charAt : (I)C
      //   440: iload #13
      //   442: if_icmpne -> 529
      //   445: iload #10
      //   447: istore #9
      //   449: iload #18
      //   451: istore #20
      //   453: iload #14
      //   455: istore #22
      //   457: iload #13
      //   459: istore #21
      //   461: iload #12
      //   463: istore #19
      //   465: goto -> 1246
      //   468: iload #7
      //   470: bipush #39
      //   472: if_icmpeq -> 502
      //   475: iload #9
      //   477: istore #15
      //   479: iload #14
      //   481: istore #10
      //   483: iload #13
      //   485: istore #11
      //   487: iload #12
      //   489: istore #16
      //   491: iload #7
      //   493: istore #23
      //   495: iload #7
      //   497: bipush #34
      //   499: if_icmpne -> 946
      //   502: iload #9
      //   504: istore #15
      //   506: iload #14
      //   508: istore #10
      //   510: iload #13
      //   512: istore #11
      //   514: iload #12
      //   516: istore #16
      //   518: iload #7
      //   520: istore #23
      //   522: iload #7
      //   524: iload #13
      //   526: if_icmpne -> 946
      //   529: iload #14
      //   531: iconst_1
      //   532: ixor
      //   533: istore #10
      //   535: iconst_0
      //   536: istore #11
      //   538: iload #9
      //   540: istore #15
      //   542: iload #12
      //   544: istore #16
      //   546: iload #7
      //   548: istore #23
      //   550: goto -> 946
      //   553: iload #7
      //   555: bipush #35
      //   557: if_icmpeq -> 836
      //   560: iload #7
      //   562: bipush #45
      //   564: if_icmpne -> 596
      //   567: iload #9
      //   569: iconst_1
      //   570: iadd
      //   571: istore #10
      //   573: iload #10
      //   575: aload_0
      //   576: getfield statementLength : I
      //   579: if_icmpge -> 596
      //   582: aload_1
      //   583: iload #10
      //   585: invokevirtual charAt : (I)C
      //   588: bipush #45
      //   590: if_icmpne -> 596
      //   593: goto -> 836
      //   596: iload #7
      //   598: bipush #47
      //   600: if_icmpne -> 780
      //   603: iload #9
      //   605: iconst_1
      //   606: iadd
      //   607: istore #19
      //   609: iload #19
      //   611: aload_0
      //   612: getfield statementLength : I
      //   615: if_icmpge -> 780
      //   618: iload #9
      //   620: istore #15
      //   622: iload #14
      //   624: istore #10
      //   626: iload #13
      //   628: istore #11
      //   630: iload #12
      //   632: istore #16
      //   634: iload #7
      //   636: istore #23
      //   638: aload_1
      //   639: iload #19
      //   641: invokevirtual charAt : (I)C
      //   644: bipush #42
      //   646: if_icmpne -> 946
      //   649: iload #9
      //   651: iconst_2
      //   652: iadd
      //   653: istore #11
      //   655: iload #11
      //   657: istore #10
      //   659: iload #10
      //   661: istore #9
      //   663: iload #7
      //   665: istore #19
      //   667: iload #11
      //   669: aload_0
      //   670: getfield statementLength : I
      //   673: if_icmpge -> 757
      //   676: iinc #10, 1
      //   679: aload_1
      //   680: iload #11
      //   682: invokevirtual charAt : (I)C
      //   685: bipush #42
      //   687: if_icmpne -> 751
      //   690: iload #11
      //   692: iconst_1
      //   693: iadd
      //   694: istore #9
      //   696: iload #9
      //   698: aload_0
      //   699: getfield statementLength : I
      //   702: if_icmpge -> 751
      //   705: aload_1
      //   706: iload #9
      //   708: invokevirtual charAt : (I)C
      //   711: bipush #47
      //   713: if_icmpne -> 751
      //   716: iinc #10, 1
      //   719: iload #10
      //   721: istore #9
      //   723: iload #7
      //   725: istore #19
      //   727: iload #10
      //   729: aload_0
      //   730: getfield statementLength : I
      //   733: if_icmpge -> 757
      //   736: aload_1
      //   737: iload #10
      //   739: invokevirtual charAt : (I)C
      //   742: istore #19
      //   744: iload #10
      //   746: istore #9
      //   748: goto -> 757
      //   751: iinc #11, 1
      //   754: goto -> 659
      //   757: iload #9
      //   759: istore #15
      //   761: iload #14
      //   763: istore #10
      //   765: iload #13
      //   767: istore #11
      //   769: iload #12
      //   771: istore #16
      //   773: iload #19
      //   775: istore #23
      //   777: goto -> 946
      //   780: iload #7
      //   782: bipush #39
      //   784: if_icmpeq -> 814
      //   787: iload #9
      //   789: istore #15
      //   791: iload #14
      //   793: istore #10
      //   795: iload #13
      //   797: istore #11
      //   799: iload #12
      //   801: istore #16
      //   803: iload #7
      //   805: istore #23
      //   807: iload #7
      //   809: bipush #34
      //   811: if_icmpne -> 946
      //   814: iload #7
      //   816: istore #11
      //   818: iconst_1
      //   819: istore #10
      //   821: iload #9
      //   823: istore #15
      //   825: iload #12
      //   827: istore #16
      //   829: iload #7
      //   831: istore #23
      //   833: goto -> 946
      //   836: aload_0
      //   837: getfield statementLength : I
      //   840: istore #11
      //   842: iload #9
      //   844: istore #10
      //   846: iload #10
      //   848: istore #9
      //   850: iload #18
      //   852: istore #20
      //   854: iload #14
      //   856: istore #22
      //   858: iload #13
      //   860: istore #21
      //   862: iload #12
      //   864: istore #19
      //   866: iload #10
      //   868: iload #11
      //   870: iconst_1
      //   871: isub
      //   872: if_icmpge -> 1246
      //   875: aload_1
      //   876: iload #10
      //   878: invokevirtual charAt : (I)C
      //   881: istore #15
      //   883: iload #10
      //   885: istore #9
      //   887: iload #18
      //   889: istore #20
      //   891: iload #14
      //   893: istore #22
      //   895: iload #13
      //   897: istore #21
      //   899: iload #12
      //   901: istore #19
      //   903: iload #15
      //   905: bipush #13
      //   907: if_icmpeq -> 1246
      //   910: iload #15
      //   912: bipush #10
      //   914: if_icmpne -> 940
      //   917: iload #10
      //   919: istore #9
      //   921: iload #18
      //   923: istore #20
      //   925: iload #14
      //   927: istore #22
      //   929: iload #13
      //   931: istore #21
      //   933: iload #12
      //   935: istore #19
      //   937: goto -> 1246
      //   940: iinc #10, 1
      //   943: goto -> 846
      //   946: iload #15
      //   948: istore #9
      //   950: iload #18
      //   952: istore #20
      //   954: iload #10
      //   956: istore #22
      //   958: iload #11
      //   960: istore #21
      //   962: iload #16
      //   964: istore #19
      //   966: iload #10
      //   968: ifne -> 1246
      //   971: iload #15
      //   973: istore #9
      //   975: iload #18
      //   977: istore #20
      //   979: iload #10
      //   981: istore #22
      //   983: iload #11
      //   985: istore #21
      //   987: iload #16
      //   989: istore #19
      //   991: iload #16
      //   993: ifne -> 1246
      //   996: iload #23
      //   998: bipush #63
      //   1000: if_icmpne -> 1112
      //   1003: aload #26
      //   1005: iconst_2
      //   1006: newarray int
      //   1008: dup
      //   1009: iconst_0
      //   1010: iload #18
      //   1012: iastore
      //   1013: dup
      //   1014: iconst_1
      //   1015: iload #15
      //   1017: iastore
      //   1018: invokevirtual add : (Ljava/lang/Object;)Z
      //   1021: pop
      //   1022: iload #15
      //   1024: iconst_1
      //   1025: iadd
      //   1026: istore #12
      //   1028: iload #15
      //   1030: istore #9
      //   1032: iload #12
      //   1034: istore #20
      //   1036: iload #10
      //   1038: istore #22
      //   1040: iload #11
      //   1042: istore #21
      //   1044: iload #16
      //   1046: istore #19
      //   1048: aload_0
      //   1049: getfield isOnDuplicateKeyUpdate : Z
      //   1052: ifeq -> 1246
      //   1055: iload #15
      //   1057: istore #9
      //   1059: iload #12
      //   1061: istore #20
      //   1063: iload #10
      //   1065: istore #22
      //   1067: iload #11
      //   1069: istore #21
      //   1071: iload #16
      //   1073: istore #19
      //   1075: iload #15
      //   1077: aload_0
      //   1078: getfield locationOfOnDuplicateKeyUpdate : I
      //   1081: if_icmple -> 1246
      //   1084: aload_0
      //   1085: iconst_1
      //   1086: putfield parametersInDuplicateKeyClause : Z
      //   1089: iload #15
      //   1091: istore #9
      //   1093: iload #12
      //   1095: istore #20
      //   1097: iload #10
      //   1099: istore #22
      //   1101: iload #11
      //   1103: istore #21
      //   1105: iload #16
      //   1107: istore #19
      //   1109: goto -> 1246
      //   1112: iload #15
      //   1114: istore #9
      //   1116: iload #18
      //   1118: istore #20
      //   1120: iload #10
      //   1122: istore #22
      //   1124: iload #11
      //   1126: istore #21
      //   1128: iload #16
      //   1130: istore #19
      //   1132: iload #23
      //   1134: bipush #59
      //   1136: if_icmpne -> 1246
      //   1139: iload #15
      //   1141: iconst_1
      //   1142: iadd
      //   1143: istore #12
      //   1145: iload #15
      //   1147: istore #9
      //   1149: iload #18
      //   1151: istore #20
      //   1153: iload #10
      //   1155: istore #22
      //   1157: iload #11
      //   1159: istore #21
      //   1161: iload #16
      //   1163: istore #19
      //   1165: iload #12
      //   1167: aload_0
      //   1168: getfield statementLength : I
      //   1171: if_icmpge -> 1246
      //   1174: iload #12
      //   1176: istore #9
      //   1178: iload #9
      //   1180: aload_0
      //   1181: getfield statementLength : I
      //   1184: if_icmpge -> 1208
      //   1187: aload_1
      //   1188: iload #9
      //   1190: invokevirtual charAt : (I)C
      //   1193: invokestatic isWhitespace : (C)Z
      //   1196: ifne -> 1202
      //   1199: goto -> 1208
      //   1202: iinc #9, 1
      //   1205: goto -> 1178
      //   1208: iload #9
      //   1210: aload_0
      //   1211: getfield statementLength : I
      //   1214: if_icmpge -> 1227
      //   1217: aload_0
      //   1218: aload_0
      //   1219: getfield numberOfQueries : I
      //   1222: iconst_1
      //   1223: iadd
      //   1224: putfield numberOfQueries : I
      //   1227: iinc #9, -1
      //   1230: iload #16
      //   1232: istore #19
      //   1234: iload #11
      //   1236: istore #21
      //   1238: iload #10
      //   1240: istore #22
      //   1242: iload #18
      //   1244: istore #20
      //   1246: iinc #9, 1
      //   1249: iload #20
      //   1251: istore #18
      //   1253: iload #22
      //   1255: istore #14
      //   1257: iload #21
      //   1259: istore #13
      //   1261: iload #19
      //   1263: istore #12
      //   1265: goto -> 176
      //   1268: aload_0
      //   1269: getfield firstStmtChar : C
      //   1272: bipush #76
      //   1274: if_icmpne -> 1302
      //   1277: aload_1
      //   1278: ldc 'LOAD DATA'
      //   1280: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
      //   1283: ifeq -> 1294
      //   1286: aload_0
      //   1287: iconst_1
      //   1288: putfield foundLoadData : Z
      //   1291: goto -> 1307
      //   1294: aload_0
      //   1295: iconst_0
      //   1296: putfield foundLoadData : Z
      //   1299: goto -> 1307
      //   1302: aload_0
      //   1303: iconst_0
      //   1304: putfield foundLoadData : Z
      //   1307: aload #26
      //   1309: iconst_2
      //   1310: newarray int
      //   1312: dup
      //   1313: iconst_0
      //   1314: iload #18
      //   1316: iastore
      //   1317: dup
      //   1318: iconst_1
      //   1319: aload_0
      //   1320: getfield statementLength : I
      //   1323: iastore
      //   1324: invokevirtual add : (Ljava/lang/Object;)Z
      //   1327: pop
      //   1328: aload #26
      //   1330: invokevirtual size : ()I
      //   1333: anewarray [B
      //   1336: astore #27
      //   1338: aload_0
      //   1339: aload #27
      //   1341: putfield staticSql : [[B
      //   1344: aload #27
      //   1346: arraylength
      //   1347: iconst_1
      //   1348: if_icmple -> 1357
      //   1351: iconst_1
      //   1352: istore #24
      //   1354: goto -> 1360
      //   1357: iconst_0
      //   1358: istore #24
      //   1360: aload_0
      //   1361: iload #24
      //   1363: putfield hasPlaceholders : Z
      //   1366: iconst_0
      //   1367: istore #9
      //   1369: iload #9
      //   1371: aload_0
      //   1372: getfield staticSql : [[B
      //   1375: arraylength
      //   1376: if_icmpge -> 1576
      //   1379: aload #26
      //   1381: iload #9
      //   1383: invokevirtual get : (I)Ljava/lang/Object;
      //   1386: checkcast [I
      //   1389: astore #27
      //   1391: aload #27
      //   1393: iconst_1
      //   1394: iaload
      //   1395: istore #10
      //   1397: aload #27
      //   1399: iconst_0
      //   1400: iaload
      //   1401: istore #11
      //   1403: iload #10
      //   1405: iload #11
      //   1407: isub
      //   1408: istore #12
      //   1410: aload_0
      //   1411: getfield foundLoadData : Z
      //   1414: ifeq -> 1435
      //   1417: aload_0
      //   1418: getfield staticSql : [[B
      //   1421: iload #9
      //   1423: aload_1
      //   1424: iload #11
      //   1426: iload #12
      //   1428: invokestatic getBytes : (Ljava/lang/String;II)[B
      //   1431: aastore
      //   1432: goto -> 1570
      //   1435: aload #4
      //   1437: ifnonnull -> 1489
      //   1440: iload #12
      //   1442: newarray byte
      //   1444: astore #27
      //   1446: iconst_0
      //   1447: istore #10
      //   1449: iload #10
      //   1451: iload #12
      //   1453: if_icmpge -> 1477
      //   1456: aload #27
      //   1458: iload #10
      //   1460: aload_1
      //   1461: iload #11
      //   1463: iload #10
      //   1465: iadd
      //   1466: invokevirtual charAt : (I)C
      //   1469: i2b
      //   1470: bastore
      //   1471: iinc #10, 1
      //   1474: goto -> 1449
      //   1477: aload_0
      //   1478: getfield staticSql : [[B
      //   1481: iload #9
      //   1483: aload #27
      //   1485: aastore
      //   1486: goto -> 1570
      //   1489: aload #5
      //   1491: ifnull -> 1534
      //   1494: aload_0
      //   1495: getfield staticSql : [[B
      //   1498: iload #9
      //   1500: aload_1
      //   1501: aload #5
      //   1503: aload #4
      //   1505: aload_2
      //   1506: invokeinterface getServerCharset : ()Ljava/lang/String;
      //   1511: iload #11
      //   1513: iload #12
      //   1515: aload_2
      //   1516: invokeinterface parserKnowsUnicode : ()Z
      //   1521: aload_2
      //   1522: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
      //   1527: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;IIZLcom/mysql/jdbc/ExceptionInterceptor;)[B
      //   1530: aastore
      //   1531: goto -> 1570
      //   1534: aload_0
      //   1535: getfield staticSql : [[B
      //   1538: iload #9
      //   1540: aload_1
      //   1541: aload #4
      //   1543: aload_2
      //   1544: invokeinterface getServerCharset : ()Ljava/lang/String;
      //   1549: iload #11
      //   1551: iload #12
      //   1553: aload_2
      //   1554: invokeinterface parserKnowsUnicode : ()Z
      //   1559: aload_2
      //   1560: aload_2
      //   1561: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
      //   1566: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIZLcom/mysql/jdbc/MySQLConnection;Lcom/mysql/jdbc/ExceptionInterceptor;)[B
      //   1569: aastore
      //   1570: iinc #9, 1
      //   1573: goto -> 1369
      //   1576: iload #6
      //   1578: ifeq -> 1655
      //   1581: aload_0
      //   1582: getfield numberOfQueries : I
      //   1585: iconst_1
      //   1586: if_icmpne -> 1621
      //   1589: aload_0
      //   1590: getfield parametersInDuplicateKeyClause : Z
      //   1593: ifne -> 1621
      //   1596: aload_1
      //   1597: aload_0
      //   1598: getfield isOnDuplicateKeyUpdate : Z
      //   1601: aload_0
      //   1602: getfield locationOfOnDuplicateKeyUpdate : I
      //   1605: aload_0
      //   1606: getfield statementStartPos : I
      //   1609: invokestatic canRewrite : (Ljava/lang/String;ZII)Z
      //   1612: ifeq -> 1621
      //   1615: iconst_1
      //   1616: istore #6
      //   1618: goto -> 1624
      //   1621: iconst_0
      //   1622: istore #6
      //   1624: aload_0
      //   1625: iload #6
      //   1627: putfield canRewriteAsMultiValueInsert : Z
      //   1630: iload #6
      //   1632: ifeq -> 1655
      //   1635: aload_2
      //   1636: invokeinterface getRewriteBatchedStatements : ()Z
      //   1641: ifeq -> 1655
      //   1644: aload_0
      //   1645: aload_1
      //   1646: aload_2
      //   1647: aload_3
      //   1648: aload #4
      //   1650: aload #5
      //   1652: invokespecial buildRewriteBatchedParams : (Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;Ljava/sql/DatabaseMetaData;Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;)V
      //   1655: return
      //   1656: astore_2
      //   1657: goto -> 1677
      //   1660: ldc 'PreparedStatement.61'
      //   1662: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
      //   1665: ldc 'S1009'
      //   1667: aload_2
      //   1668: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
      //   1673: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
      //   1676: athrow
      //   1677: new java/lang/StringBuilder
      //   1680: dup
      //   1681: invokespecial <init> : ()V
      //   1684: astore_3
      //   1685: aload_3
      //   1686: ldc 'Parse error for '
      //   1688: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1691: pop
      //   1692: aload_3
      //   1693: aload_1
      //   1694: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1697: pop
      //   1698: new java/sql/SQLException
      //   1701: dup
      //   1702: aload_3
      //   1703: invokevirtual toString : ()Ljava/lang/String;
      //   1706: invokespecial <init> : (Ljava/lang/String;)V
      //   1709: astore_1
      //   1710: aload_1
      //   1711: aload_2
      //   1712: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
      //   1715: pop
      //   1716: aload_1
      //   1717: athrow
      // Exception table:
      //   from	to	target	type
      //   68	89	1656	java/lang/StringIndexOutOfBoundsException
      //   94	120	1656	java/lang/StringIndexOutOfBoundsException
      //   126	164	1656	java/lang/StringIndexOutOfBoundsException
      //   176	221	1656	java/lang/StringIndexOutOfBoundsException
      //   228	258	1656	java/lang/StringIndexOutOfBoundsException
      //   273	279	1656	java/lang/StringIndexOutOfBoundsException
      //   291	302	1656	java/lang/StringIndexOutOfBoundsException
      //   417	428	1656	java/lang/StringIndexOutOfBoundsException
      //   434	445	1656	java/lang/StringIndexOutOfBoundsException
      //   573	593	1656	java/lang/StringIndexOutOfBoundsException
      //   609	618	1656	java/lang/StringIndexOutOfBoundsException
      //   638	649	1656	java/lang/StringIndexOutOfBoundsException
      //   667	676	1656	java/lang/StringIndexOutOfBoundsException
      //   679	690	1656	java/lang/StringIndexOutOfBoundsException
      //   696	716	1656	java/lang/StringIndexOutOfBoundsException
      //   727	744	1656	java/lang/StringIndexOutOfBoundsException
      //   836	842	1656	java/lang/StringIndexOutOfBoundsException
      //   875	883	1656	java/lang/StringIndexOutOfBoundsException
      //   1003	1022	1656	java/lang/StringIndexOutOfBoundsException
      //   1048	1055	1656	java/lang/StringIndexOutOfBoundsException
      //   1075	1089	1656	java/lang/StringIndexOutOfBoundsException
      //   1165	1174	1656	java/lang/StringIndexOutOfBoundsException
      //   1178	1199	1656	java/lang/StringIndexOutOfBoundsException
      //   1208	1227	1656	java/lang/StringIndexOutOfBoundsException
      //   1268	1291	1656	java/lang/StringIndexOutOfBoundsException
      //   1294	1299	1656	java/lang/StringIndexOutOfBoundsException
      //   1302	1307	1656	java/lang/StringIndexOutOfBoundsException
      //   1307	1351	1656	java/lang/StringIndexOutOfBoundsException
      //   1360	1366	1656	java/lang/StringIndexOutOfBoundsException
      //   1369	1391	1656	java/lang/StringIndexOutOfBoundsException
      //   1410	1432	1656	java/lang/StringIndexOutOfBoundsException
      //   1440	1446	1656	java/lang/StringIndexOutOfBoundsException
      //   1456	1471	1656	java/lang/StringIndexOutOfBoundsException
      //   1477	1486	1656	java/lang/StringIndexOutOfBoundsException
      //   1494	1531	1656	java/lang/StringIndexOutOfBoundsException
      //   1534	1570	1656	java/lang/StringIndexOutOfBoundsException
      //   1660	1677	1656	java/lang/StringIndexOutOfBoundsException
    }
    
    private ParseInfo(byte[][] param1ArrayOfbyte, char param1Char, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2, int param1Int3) {
      this.firstStmtChar = param1Char;
      this.foundLoadData = param1Boolean1;
      this.isOnDuplicateKeyUpdate = param1Boolean2;
      this.locationOfOnDuplicateKeyUpdate = param1Int1;
      this.statementLength = param1Int2;
      this.statementStartPos = param1Int3;
      this.staticSql = param1ArrayOfbyte;
    }
    
    private void buildInfoForBatch(int param1Int, PreparedStatement.BatchVisitor param1BatchVisitor) {
      boolean bool1 = this.hasPlaceholders;
      boolean bool = true;
      if (!bool1) {
        if (param1Int == 1) {
          param1BatchVisitor.append(this.staticSql[0]);
          return;
        } 
        param1BatchVisitor.append(this.batchHead.staticSql[0]).increment();
        int m = param1Int - 1;
        param1Int = m;
        if (this.batchODKUClause != null)
          param1Int = m - 1; 
        byte[] arrayOfByte = this.batchValues.staticSql[0];
        for (m = 0; m < param1Int; m++)
          param1BatchVisitor.mergeWithLast(arrayOfByte).increment(); 
        ParseInfo parseInfo1 = this.batchODKUClause;
        if (parseInfo1 != null)
          param1BatchVisitor.mergeWithLast(parseInfo1.staticSql[0]).increment(); 
        return;
      } 
      byte[][] arrayOfByte3 = this.batchHead.staticSql;
      int j = arrayOfByte3.length - 1;
      byte[] arrayOfByte1 = arrayOfByte3[j];
      int i;
      for (i = 0; i < j; i++)
        param1BatchVisitor.append(arrayOfByte3[i]).increment(); 
      j = param1Int - 1;
      i = j;
      if (this.batchODKUClause != null)
        i = j - 1; 
      byte[][] arrayOfByte5 = this.batchValues.staticSql;
      j = arrayOfByte5.length;
      byte[] arrayOfByte4 = arrayOfByte5[0];
      int k = j - 1;
      byte[] arrayOfByte2 = arrayOfByte5[k];
      for (j = 0; j < i; j++) {
        param1BatchVisitor.merge(arrayOfByte2, arrayOfByte4).increment();
        for (byte b = 1; b < k; b++)
          param1BatchVisitor.append(arrayOfByte5[b]).increment(); 
      } 
      ParseInfo parseInfo = this.batchODKUClause;
      if (parseInfo != null) {
        byte[][] arrayOfByte = parseInfo.staticSql;
        j = arrayOfByte.length;
        byte[] arrayOfByte6 = arrayOfByte[0];
        byte[] arrayOfByte7 = arrayOfByte[j - 1];
        if (param1Int > 1) {
          if (i > 0)
            arrayOfByte1 = arrayOfByte2; 
          param1BatchVisitor.merge(arrayOfByte1, arrayOfByte6).increment();
          for (param1Int = bool; param1Int < j; param1Int++)
            param1BatchVisitor.append(arrayOfByte[param1Int]).increment(); 
        } else {
          param1BatchVisitor.append(arrayOfByte7).increment();
        } 
      } else {
        param1BatchVisitor.append(arrayOfByte1);
      } 
    }
    
    private void buildRewriteBatchedParams(String param1String1, MySQLConnection param1MySQLConnection, DatabaseMetaData param1DatabaseMetaData, String param1String2, SingleByteCharsetConverter param1SingleByteCharsetConverter) throws SQLException {
      String str1 = param1String1;
      this.valuesClause = extractValuesClause(str1, param1MySQLConnection.getMetaData().getIdentifierQuoteString());
      if (this.isOnDuplicateKeyUpdate) {
        param1String1 = str1.substring(this.locationOfOnDuplicateKeyUpdate);
      } else {
        param1String1 = null;
      } 
      String str2 = str1;
      if (this.isOnDuplicateKeyUpdate)
        str2 = str1.substring(0, this.locationOfOnDuplicateKeyUpdate); 
      this.batchHead = new ParseInfo(str2, param1MySQLConnection, param1DatabaseMetaData, param1String2, param1SingleByteCharsetConverter, false);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(",");
      stringBuilder.append(this.valuesClause);
      this.batchValues = new ParseInfo(stringBuilder.toString(), param1MySQLConnection, param1DatabaseMetaData, param1String2, param1SingleByteCharsetConverter, false);
      this.batchODKUClause = null;
      if (param1String1 != null && param1String1.length() > 0) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(",");
        stringBuilder.append(this.valuesClause);
        stringBuilder.append(" ");
        stringBuilder.append(param1String1);
        this.batchODKUClause = new ParseInfo(stringBuilder.toString(), param1MySQLConnection, param1DatabaseMetaData, param1String2, param1SingleByteCharsetConverter, false);
      } 
    }
    
    private String extractValuesClause(String param1String1, String param1String2) throws SQLException {
      int i;
      int k;
      int j = this.statementStartPos;
      label35: while (true) {
        int m = -1;
        while (true) {
          k = m;
          if (m == -1) {
            if (param1String2.length() > 0) {
              i = StringUtils.indexOfIgnoreCase(j, param1String1, "VALUES", param1String2, param1String2, StringUtils.SEARCH_MODE__MRK_COM_WS);
            } else {
              i = StringUtils.indexOfIgnoreCase(j, param1String1, "VALUES");
            } 
            k = i;
            if (i > 0) {
              char c = param1String1.charAt(i - 1);
              if (!Character.isWhitespace(c) && c != ')' && c != '`') {
                j = i + 6;
                continue label35;
              } 
              k = i + 6;
              c = param1String1.charAt(k);
              m = i;
              if (!Character.isWhitespace(c)) {
                m = i;
                if (c != '(')
                  j = k; 
              } 
              continue;
            } 
          } 
          break;
        } 
        break;
      } 
      if (k == -1)
        return null; 
      j = param1String1.indexOf('(', k + 6);
      if (j == -1)
        return null; 
      if (this.isOnDuplicateKeyUpdate) {
        i = this.locationOfOnDuplicateKeyUpdate;
      } else {
        i = param1String1.length();
      } 
      return param1String1.substring(j, i);
    }
    
    public ParseInfo getParseInfoForBatch(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new com/mysql/jdbc/PreparedStatement$AppendingBatchVisitor
      //   5: astore_2
      //   6: aload_2
      //   7: invokespecial <init> : ()V
      //   10: aload_0
      //   11: iload_1
      //   12: aload_2
      //   13: invokespecial buildInfoForBatch : (ILcom/mysql/jdbc/PreparedStatement$BatchVisitor;)V
      //   16: new com/mysql/jdbc/PreparedStatement$ParseInfo
      //   19: dup
      //   20: aload_2
      //   21: invokevirtual getStaticSqlStrings : ()[[B
      //   24: aload_0
      //   25: getfield firstStmtChar : C
      //   28: aload_0
      //   29: getfield foundLoadData : Z
      //   32: aload_0
      //   33: getfield isOnDuplicateKeyUpdate : Z
      //   36: aload_0
      //   37: getfield locationOfOnDuplicateKeyUpdate : I
      //   40: aload_0
      //   41: getfield statementLength : I
      //   44: aload_0
      //   45: getfield statementStartPos : I
      //   48: invokespecial <init> : ([[BCZZIII)V
      //   51: astore_2
      //   52: aload_0
      //   53: monitorexit
      //   54: aload_2
      //   55: areturn
      //   56: astore_2
      //   57: aload_0
      //   58: monitorexit
      //   59: aload_2
      //   60: athrow
      // Exception table:
      //   from	to	target	type
      //   2	52	56	finally
    }
    
    public String getSqlForBatch(int param1Int) throws UnsupportedEncodingException {
      return getSqlForBatch(getParseInfoForBatch(param1Int));
    }
    
    public String getSqlForBatch(ParseInfo param1ParseInfo) throws UnsupportedEncodingException {
      byte[][] arrayOfByte = param1ParseInfo.staticSql;
      int k = arrayOfByte.length;
      boolean bool = false;
      int j = 0;
      int i = 0;
      while (j < k) {
        i = i + (arrayOfByte[j]).length + 1;
        j++;
      } 
      StringBuilder stringBuilder = new StringBuilder(i);
      i = bool;
      while (true) {
        j = k - 1;
        if (i < j) {
          stringBuilder.append(StringUtils.toString(arrayOfByte[i], this.charEncoding));
          stringBuilder.append("?");
          i++;
          continue;
        } 
        stringBuilder.append(StringUtils.toString(arrayOfByte[j]));
        return stringBuilder.toString();
      } 
    }
  }
}
