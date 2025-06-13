package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class DatabaseMetaData implements DatabaseMetaData {
  private static final int DEFERRABILITY = 13;
  
  private static final int DELETE_RULE = 10;
  
  private static final int FKCOLUMN_NAME = 7;
  
  private static final int FKTABLE_CAT = 4;
  
  private static final int FKTABLE_NAME = 6;
  
  private static final int FKTABLE_SCHEM = 5;
  
  private static final int FK_NAME = 11;
  
  private static final Constructor<?> JDBC_4_DBMD_IS_CTOR;
  
  private static final Constructor<?> JDBC_4_DBMD_SHOW_CTOR;
  
  private static final int KEY_SEQ = 8;
  
  public static final int MAX_IDENTIFIER_LENGTH = 64;
  
  private static final String[] MYSQL_KEYWORDS;
  
  private static final int PKCOLUMN_NAME = 3;
  
  private static final int PKTABLE_CAT = 0;
  
  private static final int PKTABLE_NAME = 2;
  
  private static final int PKTABLE_SCHEM = 1;
  
  private static final int PK_NAME = 12;
  
  private static final String[] SQL2003_KEYWORDS;
  
  private static final String[] SQL92_KEYWORDS;
  
  private static final String SUPPORTS_FK = "SUPPORTS_FK";
  
  public static final byte[] SYSTEM_TABLE_AS_BYTES;
  
  public static final byte[] TABLE_AS_BYTES = "TABLE".getBytes();
  
  private static final int UPDATE_RULE = 9;
  
  public static final byte[] VIEW_AS_BYTES;
  
  private static volatile String mysqlKeywords;
  
  public MySQLConnection conn;
  
  public String database = null;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  public final String quotedId;
  
  static {
    SYSTEM_TABLE_AS_BYTES = "SYSTEM TABLE".getBytes();
    VIEW_AS_BYTES = "VIEW".getBytes();
    if (Util.isJdbc4()) {
      try {
        JDBC_4_DBMD_SHOW_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaData").getConstructor(new Class[] { MySQLConnection.class, String.class });
        JDBC_4_DBMD_IS_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaDataUsingInfoSchema").getConstructor(new Class[] { MySQLConnection.class, String.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_DBMD_IS_CTOR = null;
      JDBC_4_DBMD_SHOW_CTOR = null;
    } 
    MYSQL_KEYWORDS = new String[] { 
        "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", 
        "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", 
        "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", 
        "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", 
        "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE", 
        "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", 
        "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", 
        "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GENERATED", "GET", "GRANT", "GROUP", "HAVING", 
        "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", 
        "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", 
        "INTERVAL", "INTO", "IO_AFTER_GTIDS", "IO_BEFORE_GTIDS", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", 
        "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", 
        "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND", "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE", 
        "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", 
        "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTIMIZER_COSTS", "OPTION", "OPTIONALLY", "OR", "ORDER", 
        "OUT", "OUTER", "OUTFILE", "PARTITION", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", 
        "READS", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", 
        "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", 
        "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", 
        "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STORED", "STRAIGHT_JOIN", "TABLE", 
        "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", 
        "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", 
        "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE", 
        "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL" };
    SQL92_KEYWORDS = new String[] { 
        "ABSOLUTE", "ACTION", "ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "AS", 
        "ASC", "ASSERTION", "AT", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIT", "BIT_LENGTH", "BOTH", 
        "BY", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", 
        "CHECK", "CLOSE", "COALESCE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONNECT", "CONNECTION", "CONSTRAINT", 
        "CONSTRAINTS", "CONTINUE", "CONVERT", "CORRESPONDING", "COUNT", "CREATE", "CROSS", "CURRENT", "CURRENT_DATE", "CURRENT_TIME", 
        "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", 
        "DEFERRABLE", "DEFERRED", "DELETE", "DESC", "DESCRIBE", "DESCRIPTOR", "DIAGNOSTICS", "DISCONNECT", "DISTINCT", "DOMAIN", 
        "DOUBLE", "DROP", "ELSE", "END", "END-EXEC", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", 
        "EXISTS", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", 
        "FROM", "FULL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "HAVING", "HOUR", 
        "IDENTITY", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER", "INPUT", "INSENSITIVE", "INSERT", "INT", 
        "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LAST", 
        "LEADING", "LEFT", "LEVEL", "LIKE", "LOCAL", "LOWER", "MATCH", "MAX", "MIN", "MINUTE", 
        "MODULE", "MONTH", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NEXT", "NO", "NOT", "NULL", 
        "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER", 
        "OUTER", "OUTPUT", "OVERLAPS", "PAD", "PARTIAL", "POSITION", "PRECISION", "PREPARE", "PRESERVE", "PRIMARY", 
        "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "REAL", "REFERENCES", "RELATIVE", "RESTRICT", "REVOKE", 
        "RIGHT", "ROLLBACK", "ROWS", "SCHEMA", "SCROLL", "SECOND", "SECTION", "SELECT", "SESSION", "SESSION_USER", 
        "SET", "SIZE", "SMALLINT", "SOME", "SPACE", "SQL", "SQLCODE", "SQLERROR", "SQLSTATE", "SUBSTRING", 
        "SUM", "SYSTEM_USER", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", 
        "TRAILING", "TRANSACTION", "TRANSLATE", "TRANSLATION", "TRIM", "TRUE", "UNION", "UNIQUE", "UNKNOWN", "UPDATE", 
        "UPPER", "USAGE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW", "WHEN", 
        "WHENEVER", "WHERE", "WITH", "WORK", "WRITE", "YEAR", "ZONE" };
    SQL2003_KEYWORDS = new String[] { 
        "ABS", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASENSITIVE", 
        "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIGINT", "BINARY", "BLOB", 
        "BOOLEAN", "BOTH", "BY", "CALL", "CALLED", "CARDINALITY", "CASCADED", "CASE", "CAST", "CEIL", 
        "CEILING", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLOB", "CLOSE", "COALESCE", "COLLATE", 
        "COLLECT", "COLUMN", "COMMIT", "CONDITION", "CONNECT", "CONSTRAINT", "CONVERT", "CORR", "CORRESPONDING", "COUNT", 
        "COVAR_POP", "COVAR_SAMP", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", 
        "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY", "DEALLOCATE", 
        "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DENSE_RANK", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT", 
        "DISTINCT", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END", "END-EXEC", "ESCAPE", 
        "EVERY", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXP", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", 
        "FILTER", "FLOAT", "FLOOR", "FOR", "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "FUSION", 
        "GET", "GLOBAL", "GRANT", "GROUP", "GROUPING", "HAVING", "HOLD", "HOUR", "IDENTITY", "IN", 
        "INDICATOR", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERSECTION", "INTERVAL", 
        "INTO", "IS", "JOIN", "LANGUAGE", "LARGE", "LATERAL", "LEADING", "LEFT", "LIKE", "LN", 
        "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOWER", "MATCH", "MAX", "MEMBER", "MERGE", "METHOD", "MIN", 
        "MINUTE", "MOD", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", 
        "NEW", "NO", "NONE", "NORMALIZE", "NOT", "NULL", "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", 
        "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT", "OUTER", "OVER", "OVERLAPS", 
        "OVERLAY", "PARAMETER", "PARTITION", "PERCENTILE_CONT", "PERCENTILE_DISC", "PERCENT_RANK", "POSITION", "POWER", "PRECISION", "PREPARE", 
        "PRIMARY", "PROCEDURE", "RANGE", "RANK", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", 
        "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY", "REGR_SYY", "RELEASE", 
        "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "ROW_NUMBER", 
        "SAVEPOINT", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SELECT", "SENSITIVE", "SESSION_USER", "SET", "SIMILAR", 
        "SMALLINT", "SOME", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQRT", "START", 
        "STATIC", "STDDEV_POP", "STDDEV_SAMP", "SUBMULTISET", "SUBSTRING", "SUM", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE", 
        "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATE", "TRANSLATION", 
        "TREAT", "TRIGGER", "TRIM", "TRUE", "UESCAPE", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", 
        "UPPER", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VAR_POP", "VAR_SAMP", "WHEN", 
        "WHENEVER", "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "YEAR" };
    mysqlKeywords = null;
  }
  
  public DatabaseMetaData(MySQLConnection paramMySQLConnection, String paramString) {
    this.conn = paramMySQLConnection;
    this.database = paramString;
    this.exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    try {
      String str = getIdentifierQuoteString();
      this.quotedId = str;
    } catch (SQLException sQLException) {
      AssertionFailedException.shouldNotHappen(sQLException);
      this.quotedId = null;
    } finally {}
  }
  
  private ResultSet buildResultSet(Field[] paramArrayOfField, ArrayList<ResultSetRow> paramArrayList) throws SQLException {
    return buildResultSet(paramArrayOfField, paramArrayList, this.conn);
  }
  
  public static ResultSet buildResultSet(Field[] paramArrayOfField, ArrayList<ResultSetRow> paramArrayList, MySQLConnection paramMySQLConnection) throws SQLException {
    int i = paramArrayOfField.length;
    for (byte b = 0; b < i; b++) {
      int j = paramArrayOfField[b].getSQLType();
      if (j == -1 || j == 1 || j == 12)
        paramArrayOfField[b].setEncoding(paramMySQLConnection.getCharacterSetMetadata(), paramMySQLConnection); 
      paramArrayOfField[b].setConnection(paramMySQLConnection);
      paramArrayOfField[b].setUseOldNameMetadata(true);
    } 
    return ResultSetImpl.getInstance(paramMySQLConnection.getCatalog(), paramArrayOfField, new RowDataStatic(paramArrayList), paramMySQLConnection, null, false);
  }
  
  private ResultSetRow convertTypeDescriptorToProcedureRow(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, TypeDescriptor paramTypeDescriptor, boolean paramBoolean4, int paramInt) throws SQLException {
    byte[] arrayOfByte2;
    byte[] arrayOfByte1;
    byte[][] arrayOfByte;
    if (paramBoolean4) {
      arrayOfByte = new byte[17][];
    } else {
      arrayOfByte = new byte[20][];
    } 
    arrayOfByte[0] = paramArrayOfbyte2;
    arrayOfByte[1] = null;
    arrayOfByte[2] = paramArrayOfbyte1;
    arrayOfByte[3] = s2b(paramString);
    arrayOfByte[4] = s2b(String.valueOf(getColumnType(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4)));
    arrayOfByte[5] = s2b(Short.toString(paramTypeDescriptor.dataType));
    arrayOfByte[6] = s2b(paramTypeDescriptor.typeName);
    Integer integer2 = paramTypeDescriptor.columnSize;
    if (integer2 == null) {
      integer2 = null;
    } else {
      arrayOfByte2 = s2b(integer2.toString());
    } 
    arrayOfByte[7] = arrayOfByte2;
    arrayOfByte[8] = arrayOfByte[7];
    Integer integer1 = paramTypeDescriptor.decimalDigits;
    if (integer1 == null) {
      integer1 = null;
    } else {
      arrayOfByte1 = s2b(integer1.toString());
    } 
    arrayOfByte[9] = arrayOfByte1;
    arrayOfByte[10] = s2b(Integer.toString(paramTypeDescriptor.numPrecRadix));
    int i = paramTypeDescriptor.nullability;
    if (i != 0) {
      if (i != 1) {
        if (i == 2) {
          arrayOfByte[11] = s2b(String.valueOf(2));
        } else {
          throw SQLError.createSQLException("Internal error while parsing callable statement metadata (unknown nullability value fount)", "S1000", getExceptionInterceptor());
        } 
      } else {
        arrayOfByte[11] = s2b(String.valueOf(1));
      } 
    } else {
      arrayOfByte[11] = s2b(String.valueOf(0));
    } 
    arrayOfByte[12] = null;
    if (paramBoolean4) {
      arrayOfByte[13] = null;
      arrayOfByte[14] = s2b(String.valueOf(paramInt));
      arrayOfByte[15] = s2b(paramTypeDescriptor.isNullable);
      arrayOfByte[16] = paramArrayOfbyte1;
    } else {
      arrayOfByte[13] = null;
      arrayOfByte[14] = null;
      arrayOfByte[15] = null;
      arrayOfByte[16] = null;
      arrayOfByte[17] = s2b(String.valueOf(paramInt));
      arrayOfByte[18] = s2b(paramTypeDescriptor.isNullable);
      arrayOfByte[19] = paramArrayOfbyte1;
    } 
    return new ByteArrayRow(arrayOfByte, getExceptionInterceptor());
  }
  
  private int endPositionOfParameterDeclaration(int paramInt, String paramString1, String paramString2) throws SQLException {
    int j = 1;
    int i = paramInt + 1;
    paramInt = j;
    while (paramInt > 0 && i < paramString1.length()) {
      Set<StringUtils.SearchMode> set;
      if (this.conn.isNoBackslashEscapesSet()) {
        set = StringUtils.SEARCH_MODE__MRK_COM_WS;
      } else {
        set = StringUtils.SEARCH_MODE__ALL;
      } 
      j = StringUtils.indexOfIgnoreCase(i, paramString1, ")", paramString2, paramString2, set);
      if (j != -1) {
        if (this.conn.isNoBackslashEscapesSet()) {
          set = StringUtils.SEARCH_MODE__MRK_COM_WS;
        } else {
          set = StringUtils.SEARCH_MODE__ALL;
        } 
        i = StringUtils.indexOfIgnoreCase(i, paramString1, "(", paramString2, paramString2, set);
        if (i != -1 && i < j) {
          paramInt++;
          i = j + 1;
          continue;
        } 
        paramInt--;
        i = j;
        continue;
      } 
      throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
    } 
    return i;
  }
  
  private int findEndOfReturnsClause(String paramString, int paramInt) throws SQLException {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #7
    //   9: aload #7
    //   11: aload_0
    //   12: getfield quotedId : Ljava/lang/String;
    //   15: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: pop
    //   19: aload #7
    //   21: ldc_w '('
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload #7
    //   30: invokevirtual toString : ()Ljava/lang/String;
    //   33: astore #8
    //   35: new java/lang/StringBuilder
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: astore #7
    //   44: aload #7
    //   46: aload_0
    //   47: getfield quotedId : Ljava/lang/String;
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload #7
    //   56: ldc_w ')'
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload #7
    //   65: invokevirtual toString : ()Ljava/lang/String;
    //   68: astore #9
    //   70: iload_2
    //   71: bipush #7
    //   73: iadd
    //   74: iconst_1
    //   75: iadd
    //   76: istore #6
    //   78: iconst_0
    //   79: istore_3
    //   80: iconst_m1
    //   81: istore_2
    //   82: iload_3
    //   83: bipush #11
    //   85: if_icmpge -> 245
    //   88: bipush #11
    //   90: anewarray java/lang/String
    //   93: dup
    //   94: iconst_0
    //   95: ldc_w 'LANGUAGE'
    //   98: aastore
    //   99: dup
    //   100: iconst_1
    //   101: ldc_w 'NOT'
    //   104: aastore
    //   105: dup
    //   106: iconst_2
    //   107: ldc_w 'DETERMINISTIC'
    //   110: aastore
    //   111: dup
    //   112: iconst_3
    //   113: ldc_w 'CONTAINS'
    //   116: aastore
    //   117: dup
    //   118: iconst_4
    //   119: ldc_w 'NO'
    //   122: aastore
    //   123: dup
    //   124: iconst_5
    //   125: ldc_w 'READ'
    //   128: aastore
    //   129: dup
    //   130: bipush #6
    //   132: ldc_w 'MODIFIES'
    //   135: aastore
    //   136: dup
    //   137: bipush #7
    //   139: ldc_w 'SQL'
    //   142: aastore
    //   143: dup
    //   144: bipush #8
    //   146: ldc_w 'COMMENT'
    //   149: aastore
    //   150: dup
    //   151: bipush #9
    //   153: ldc_w 'BEGIN'
    //   156: aastore
    //   157: dup
    //   158: bipush #10
    //   160: ldc_w 'RETURN'
    //   163: aastore
    //   164: iload_3
    //   165: aaload
    //   166: astore #10
    //   168: aload_0
    //   169: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   172: invokeinterface isNoBackslashEscapesSet : ()Z
    //   177: ifeq -> 188
    //   180: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__MRK_COM_WS : Ljava/util/Set;
    //   183: astore #7
    //   185: goto -> 193
    //   188: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   191: astore #7
    //   193: iload #6
    //   195: aload_1
    //   196: aload #10
    //   198: aload #8
    //   200: aload #9
    //   202: aload #7
    //   204: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   207: istore #5
    //   209: iload_2
    //   210: istore #4
    //   212: iload #5
    //   214: iconst_m1
    //   215: if_icmpeq -> 236
    //   218: iload_2
    //   219: iconst_m1
    //   220: if_icmpeq -> 232
    //   223: iload_2
    //   224: istore #4
    //   226: iload #5
    //   228: iload_2
    //   229: if_icmpge -> 236
    //   232: iload #5
    //   234: istore #4
    //   236: iinc #3, 1
    //   239: iload #4
    //   241: istore_2
    //   242: goto -> 82
    //   245: iload_2
    //   246: iconst_m1
    //   247: if_icmpeq -> 252
    //   250: iload_2
    //   251: ireturn
    //   252: aload_0
    //   253: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   256: invokeinterface isNoBackslashEscapesSet : ()Z
    //   261: ifeq -> 272
    //   264: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__MRK_COM_WS : Ljava/util/Set;
    //   267: astore #7
    //   269: goto -> 277
    //   272: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   275: astore #7
    //   277: iload #6
    //   279: aload_1
    //   280: ldc_w ':'
    //   283: aload #8
    //   285: aload #9
    //   287: aload #7
    //   289: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   292: istore_2
    //   293: iload_2
    //   294: iconst_m1
    //   295: if_icmpeq -> 321
    //   298: iload_2
    //   299: ifle -> 321
    //   302: aload_1
    //   303: iload_2
    //   304: invokevirtual charAt : (I)C
    //   307: invokestatic isWhitespace : (C)Z
    //   310: ifeq -> 315
    //   313: iload_2
    //   314: ireturn
    //   315: iinc #2, -1
    //   318: goto -> 298
    //   321: ldc_w 'Internal error when parsing callable statement metadata'
    //   324: ldc_w 'S1000'
    //   327: aload_0
    //   328: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   331: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   334: athrow
  }
  
  private void getCallStmtParameterTypes(String paramString1, String paramString2, ProcedureType paramProcedureType, String paramString3, List<ResultSetRow> paramList, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_1
    //   1: astore #18
    //   3: aload #4
    //   5: ifnonnull -> 42
    //   8: aload_0
    //   9: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   12: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   17: ifeq -> 28
    //   20: ldc_w '%'
    //   23: astore #15
    //   25: goto -> 46
    //   28: ldc_w 'Parameter/Column name pattern can not be NULL or empty.'
    //   31: ldc_w 'S1009'
    //   34: aload_0
    //   35: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   38: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   41: athrow
    //   42: aload #4
    //   44: astore #15
    //   46: aload_0
    //   47: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   50: invokeinterface getMetadataSafeStatement : ()Ljava/sql/Statement;
    //   55: astore #16
    //   57: aload #16
    //   59: astore #4
    //   61: aload_0
    //   62: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   65: invokeinterface getCatalog : ()Ljava/lang/String;
    //   70: astore #19
    //   72: aload_0
    //   73: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   76: invokeinterface lowerCaseTableNames : ()Z
    //   81: istore #13
    //   83: aload #18
    //   85: astore #17
    //   87: iload #13
    //   89: ifeq -> 248
    //   92: aload #18
    //   94: astore #17
    //   96: aload #18
    //   98: ifnull -> 248
    //   101: aload #18
    //   103: astore #17
    //   105: aload_1
    //   106: invokevirtual length : ()I
    //   109: ifeq -> 248
    //   112: aload #18
    //   114: astore #17
    //   116: aload #19
    //   118: ifnull -> 248
    //   121: aload #19
    //   123: invokevirtual length : ()I
    //   126: istore #7
    //   128: aload #18
    //   130: astore #17
    //   132: iload #7
    //   134: ifeq -> 248
    //   137: aload_0
    //   138: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   141: aload #18
    //   143: aload_0
    //   144: getfield quotedId : Ljava/lang/String;
    //   147: invokestatic unQuoteIdentifier : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   150: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   155: aload #4
    //   157: ldc_w 'SELECT DATABASE()'
    //   160: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   165: astore_1
    //   166: aload_1
    //   167: invokeinterface next : ()Z
    //   172: pop
    //   173: aload_1
    //   174: iconst_1
    //   175: invokeinterface getString : (I)Ljava/lang/String;
    //   180: astore #18
    //   182: aload_0
    //   183: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   186: aload #19
    //   188: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   193: aload #18
    //   195: astore #17
    //   197: aload_1
    //   198: ifnull -> 248
    //   201: aload_1
    //   202: invokeinterface close : ()V
    //   207: aload #18
    //   209: astore #17
    //   211: goto -> 248
    //   214: astore_2
    //   215: goto -> 221
    //   218: astore_2
    //   219: aconst_null
    //   220: astore_1
    //   221: aload_0
    //   222: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   225: aload #19
    //   227: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   232: aload_1
    //   233: ifnull -> 242
    //   236: aload_1
    //   237: invokeinterface close : ()V
    //   242: aload_2
    //   243: athrow
    //   244: astore_1
    //   245: goto -> 1696
    //   248: aload #4
    //   250: invokeinterface getMaxRows : ()I
    //   255: istore #7
    //   257: iload #7
    //   259: ifeq -> 270
    //   262: aload #4
    //   264: iconst_0
    //   265: invokeinterface setMaxRows : (I)V
    //   270: ldc_w ' '
    //   273: aload_0
    //   274: getfield quotedId : Ljava/lang/String;
    //   277: invokevirtual equals : (Ljava/lang/Object;)Z
    //   280: istore #13
    //   282: iload #13
    //   284: ifne -> 334
    //   287: aload_0
    //   288: getfield quotedId : Ljava/lang/String;
    //   291: astore #18
    //   293: aload_0
    //   294: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   297: invokeinterface isNoBackslashEscapesSet : ()Z
    //   302: ifeq -> 312
    //   305: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__MRK_COM_WS : Ljava/util/Set;
    //   308: astore_1
    //   309: goto -> 316
    //   312: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   315: astore_1
    //   316: iconst_0
    //   317: aload_2
    //   318: ldc_w '.'
    //   321: aload #18
    //   323: aload #18
    //   325: aload_1
    //   326: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   329: istore #7
    //   331: goto -> 343
    //   334: aload_2
    //   335: ldc_w '.'
    //   338: invokevirtual indexOf : (Ljava/lang/String;)I
    //   341: istore #7
    //   343: iload #7
    //   345: iconst_m1
    //   346: if_icmpeq -> 382
    //   349: iload #7
    //   351: iconst_1
    //   352: iadd
    //   353: istore #8
    //   355: iload #8
    //   357: aload_2
    //   358: invokevirtual length : ()I
    //   361: if_icmpge -> 382
    //   364: aload_2
    //   365: iconst_0
    //   366: iload #7
    //   368: invokevirtual substring : (II)Ljava/lang/String;
    //   371: astore_1
    //   372: aload_2
    //   373: iload #8
    //   375: invokevirtual substring : (I)Ljava/lang/String;
    //   378: astore_2
    //   379: goto -> 401
    //   382: aload #17
    //   384: aload_0
    //   385: getfield quotedId : Ljava/lang/String;
    //   388: aload_0
    //   389: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   392: invokeinterface getPedantic : ()Z
    //   397: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
    //   400: astore_1
    //   401: aload_2
    //   402: aload_0
    //   403: getfield quotedId : Ljava/lang/String;
    //   406: invokestatic unQuoteIdentifier : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   409: astore #18
    //   411: aload #18
    //   413: ldc_w 'UTF-8'
    //   416: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   419: astore #17
    //   421: goto -> 434
    //   424: astore #17
    //   426: aload_0
    //   427: aload #18
    //   429: invokevirtual s2b : (Ljava/lang/String;)[B
    //   432: astore #17
    //   434: aload_1
    //   435: aload_0
    //   436: getfield quotedId : Ljava/lang/String;
    //   439: invokestatic unQuoteIdentifier : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   442: astore #19
    //   444: aload #19
    //   446: ldc_w 'UTF-8'
    //   449: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   452: astore #18
    //   454: goto -> 467
    //   457: astore #18
    //   459: aload_0
    //   460: aload #19
    //   462: invokevirtual s2b : (Ljava/lang/String;)[B
    //   465: astore #18
    //   467: new java/lang/StringBuilder
    //   470: astore #19
    //   472: aload #19
    //   474: invokespecial <init> : ()V
    //   477: aload #19
    //   479: aload_1
    //   480: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   483: pop
    //   484: aload #19
    //   486: bipush #46
    //   488: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   491: pop
    //   492: aload #19
    //   494: aload_2
    //   495: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   498: pop
    //   499: getstatic com/mysql/jdbc/DatabaseMetaData$ProcedureType.PROCEDURE : Lcom/mysql/jdbc/DatabaseMetaData$ProcedureType;
    //   502: astore_1
    //   503: aload_3
    //   504: aload_1
    //   505: if_acmpne -> 553
    //   508: new java/lang/StringBuilder
    //   511: astore_1
    //   512: aload_1
    //   513: invokespecial <init> : ()V
    //   516: aload_1
    //   517: ldc_w 'SHOW CREATE PROCEDURE '
    //   520: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   523: pop
    //   524: aload_1
    //   525: aload #19
    //   527: invokevirtual toString : ()Ljava/lang/String;
    //   530: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   533: pop
    //   534: aload #4
    //   536: aload_1
    //   537: invokevirtual toString : ()Ljava/lang/String;
    //   540: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   545: astore_1
    //   546: ldc_w 'Create Procedure'
    //   549: astore_2
    //   550: goto -> 595
    //   553: new java/lang/StringBuilder
    //   556: astore_1
    //   557: aload_1
    //   558: invokespecial <init> : ()V
    //   561: aload_1
    //   562: ldc_w 'SHOW CREATE FUNCTION '
    //   565: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   568: pop
    //   569: aload_1
    //   570: aload #19
    //   572: invokevirtual toString : ()Ljava/lang/String;
    //   575: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   578: pop
    //   579: aload #4
    //   581: aload_1
    //   582: invokevirtual toString : ()Ljava/lang/String;
    //   585: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   590: astore_1
    //   591: ldc_w 'Create Function'
    //   594: astore_2
    //   595: aload_1
    //   596: invokeinterface next : ()Z
    //   601: istore #13
    //   603: iload #13
    //   605: ifeq -> 1146
    //   608: aload_1
    //   609: aload_2
    //   610: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   615: astore #20
    //   617: aload_0
    //   618: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   621: invokeinterface getNoAccessToProcedureBodies : ()Z
    //   626: istore #13
    //   628: iload #13
    //   630: ifne -> 663
    //   633: aload #20
    //   635: ifnull -> 649
    //   638: aload #20
    //   640: invokevirtual length : ()I
    //   643: ifeq -> 649
    //   646: goto -> 663
    //   649: ldc_w 'User does not have access to metadata required to determine stored procedure parameter types. If rights can not be granted, configure connection with "noAccessToProcedureBodies=true" to have driver generate parameters that represent INOUT strings irregardless of actual parameter types.'
    //   652: ldc_w 'S1000'
    //   655: aload_0
    //   656: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   659: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   662: athrow
    //   663: aload_1
    //   664: ldc_w 'sql_mode'
    //   667: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   672: ldc_w 'ANSI'
    //   675: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
    //   678: istore #7
    //   680: iload #7
    //   682: iconst_m1
    //   683: if_icmpeq -> 703
    //   686: iconst_1
    //   687: istore #7
    //   689: goto -> 706
    //   692: astore_3
    //   693: aload_1
    //   694: astore_2
    //   695: aload_3
    //   696: astore_1
    //   697: aload #4
    //   699: astore_3
    //   700: goto -> 1709
    //   703: iconst_0
    //   704: istore #7
    //   706: iload #7
    //   708: ifeq -> 719
    //   711: ldc_w '`"'
    //   714: astore #19
    //   716: goto -> 724
    //   719: ldc_w '`'
    //   722: astore #19
    //   724: new java/lang/StringBuilder
    //   727: astore_2
    //   728: aload_2
    //   729: invokespecial <init> : ()V
    //   732: aload_2
    //   733: ldc_w '''
    //   736: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   739: pop
    //   740: aload_2
    //   741: aload #19
    //   743: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   746: pop
    //   747: aload_2
    //   748: invokevirtual toString : ()Ljava/lang/String;
    //   751: astore #21
    //   753: new java/lang/StringBuilder
    //   756: astore_2
    //   757: aload_2
    //   758: invokespecial <init> : ()V
    //   761: aload_2
    //   762: ldc_w '('
    //   765: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   768: pop
    //   769: aload_2
    //   770: aload #19
    //   772: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   775: pop
    //   776: aload_2
    //   777: invokevirtual toString : ()Ljava/lang/String;
    //   780: astore_2
    //   781: new java/lang/StringBuilder
    //   784: astore #22
    //   786: aload #22
    //   788: invokespecial <init> : ()V
    //   791: aload #22
    //   793: ldc_w ')'
    //   796: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   799: pop
    //   800: aload #22
    //   802: aload #19
    //   804: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   807: pop
    //   808: aload #22
    //   810: invokevirtual toString : ()Ljava/lang/String;
    //   813: astore #19
    //   815: aload #20
    //   817: ifnull -> 1135
    //   820: aload #20
    //   822: invokevirtual length : ()I
    //   825: ifeq -> 1135
    //   828: aload #20
    //   830: aload #21
    //   832: aload #21
    //   834: iconst_1
    //   835: iconst_0
    //   836: iconst_1
    //   837: iconst_1
    //   838: invokestatic stripComments : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZ)Ljava/lang/String;
    //   841: astore #21
    //   843: aload_0
    //   844: getfield quotedId : Ljava/lang/String;
    //   847: astore #22
    //   849: aload_0
    //   850: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   853: invokeinterface isNoBackslashEscapesSet : ()Z
    //   858: istore #13
    //   860: iload #13
    //   862: ifeq -> 873
    //   865: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__MRK_COM_WS : Ljava/util/Set;
    //   868: astore #20
    //   870: goto -> 878
    //   873: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   876: astore #20
    //   878: iconst_0
    //   879: aload #21
    //   881: ldc_w '('
    //   884: aload #22
    //   886: aload #22
    //   888: aload #20
    //   890: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   893: istore #9
    //   895: aload_0
    //   896: iload #9
    //   898: aload #21
    //   900: aload_0
    //   901: getfield quotedId : Ljava/lang/String;
    //   904: invokespecial endPositionOfParameterDeclaration : (ILjava/lang/String;Ljava/lang/String;)I
    //   907: istore #11
    //   909: aload_3
    //   910: getstatic com/mysql/jdbc/DatabaseMetaData$ProcedureType.FUNCTION : Lcom/mysql/jdbc/DatabaseMetaData$ProcedureType;
    //   913: if_acmpne -> 1073
    //   916: aload_0
    //   917: getfield quotedId : Ljava/lang/String;
    //   920: astore #20
    //   922: aload_0
    //   923: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   926: invokeinterface isNoBackslashEscapesSet : ()Z
    //   931: istore #13
    //   933: iload #13
    //   935: ifeq -> 945
    //   938: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__MRK_COM_WS : Ljava/util/Set;
    //   941: astore_3
    //   942: goto -> 949
    //   945: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   948: astore_3
    //   949: iconst_0
    //   950: aload #21
    //   952: ldc_w ' RETURNS '
    //   955: aload #20
    //   957: aload #20
    //   959: aload_3
    //   960: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   963: istore #8
    //   965: aload_0
    //   966: aload #21
    //   968: iload #8
    //   970: invokespecial findEndOfReturnsClause : (Ljava/lang/String;I)I
    //   973: istore #10
    //   975: iinc #8, 8
    //   978: aload #21
    //   980: invokevirtual length : ()I
    //   983: istore #12
    //   985: iload #8
    //   987: iload #12
    //   989: if_icmpge -> 1015
    //   992: aload #21
    //   994: iload #8
    //   996: invokevirtual charAt : (I)C
    //   999: invokestatic isWhitespace : (C)Z
    //   1002: istore #13
    //   1004: iload #13
    //   1006: ifeq -> 1015
    //   1009: iinc #8, 1
    //   1012: goto -> 978
    //   1015: aload #21
    //   1017: iload #8
    //   1019: iload #10
    //   1021: invokevirtual substring : (II)Ljava/lang/String;
    //   1024: invokevirtual trim : ()Ljava/lang/String;
    //   1027: astore_3
    //   1028: new com/mysql/jdbc/DatabaseMetaData$TypeDescriptor
    //   1031: astore #20
    //   1033: aload #20
    //   1035: aload_0
    //   1036: aload_3
    //   1037: ldc_w 'YES'
    //   1040: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;)V
    //   1043: aload #5
    //   1045: aload_0
    //   1046: aload #17
    //   1048: aload #18
    //   1050: ldc_w ''
    //   1053: iconst_0
    //   1054: iconst_0
    //   1055: iconst_1
    //   1056: aload #20
    //   1058: iload #6
    //   1060: iconst_0
    //   1061: invokespecial convertTypeDescriptorToProcedureRow : ([B[BLjava/lang/String;ZZZLcom/mysql/jdbc/DatabaseMetaData$TypeDescriptor;ZI)Lcom/mysql/jdbc/ResultSetRow;
    //   1064: invokeinterface add : (Ljava/lang/Object;)Z
    //   1069: pop
    //   1070: goto -> 1073
    //   1073: iload #9
    //   1075: iconst_m1
    //   1076: if_icmpeq -> 1110
    //   1079: iload #11
    //   1081: iconst_m1
    //   1082: if_icmpeq -> 1110
    //   1085: aload #21
    //   1087: iload #9
    //   1089: iconst_1
    //   1090: iadd
    //   1091: iload #11
    //   1093: invokevirtual substring : (II)Ljava/lang/String;
    //   1096: astore #20
    //   1098: aload_2
    //   1099: astore_3
    //   1100: aload #19
    //   1102: astore #4
    //   1104: aload #20
    //   1106: astore_2
    //   1107: goto -> 1156
    //   1110: ldc_w 'Internal error when parsing callable statement metadata'
    //   1113: ldc_w 'S1000'
    //   1116: aload_0
    //   1117: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1120: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1123: athrow
    //   1124: astore_3
    //   1125: aload_1
    //   1126: astore_2
    //   1127: aload_3
    //   1128: astore_1
    //   1129: aload #16
    //   1131: astore_3
    //   1132: goto -> 1709
    //   1135: aload_2
    //   1136: astore_3
    //   1137: aload #19
    //   1139: astore #4
    //   1141: aconst_null
    //   1142: astore_2
    //   1143: goto -> 1156
    //   1146: aconst_null
    //   1147: astore_2
    //   1148: aconst_null
    //   1149: astore_3
    //   1150: aconst_null
    //   1151: astore #4
    //   1153: iconst_0
    //   1154: istore #7
    //   1156: ldc_w 'S1000'
    //   1159: astore #20
    //   1161: ldc_w 'YES'
    //   1164: astore #22
    //   1166: ldc_w '`'
    //   1169: astore #21
    //   1171: aload_1
    //   1172: ifnull -> 1188
    //   1175: aload_1
    //   1176: invokeinterface close : ()V
    //   1181: goto -> 1188
    //   1184: astore_1
    //   1185: goto -> 1190
    //   1188: aconst_null
    //   1189: astore_1
    //   1190: aload_1
    //   1191: astore #19
    //   1193: aload #16
    //   1195: ifnull -> 1213
    //   1198: aload #16
    //   1200: invokeinterface close : ()V
    //   1205: aload_1
    //   1206: astore #19
    //   1208: goto -> 1213
    //   1211: astore #19
    //   1213: aload #19
    //   1215: ifnonnull -> 1681
    //   1218: aload_2
    //   1219: ifnull -> 1680
    //   1222: aload_2
    //   1223: ldc_w ','
    //   1226: aload_3
    //   1227: aload #4
    //   1229: iconst_1
    //   1230: invokestatic split : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
    //   1233: astore #19
    //   1235: aload #19
    //   1237: invokeinterface size : ()I
    //   1242: istore #10
    //   1244: iconst_0
    //   1245: istore #8
    //   1247: iconst_1
    //   1248: istore #9
    //   1250: aload #22
    //   1252: astore_2
    //   1253: aload #21
    //   1255: astore_3
    //   1256: aload #20
    //   1258: astore #4
    //   1260: iload #8
    //   1262: iload #10
    //   1264: if_icmpge -> 1680
    //   1267: aload #19
    //   1269: iload #8
    //   1271: invokeinterface get : (I)Ljava/lang/Object;
    //   1276: checkcast java/lang/String
    //   1279: astore_1
    //   1280: aload_1
    //   1281: invokevirtual trim : ()Ljava/lang/String;
    //   1284: invokevirtual length : ()I
    //   1287: ifne -> 1293
    //   1290: goto -> 1680
    //   1293: new java/util/StringTokenizer
    //   1296: dup
    //   1297: aload_1
    //   1298: ldc_w '[\t\n\x0B\f\r]'
    //   1301: ldc_w ' '
    //   1304: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1307: ldc_w ' \\t'
    //   1310: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   1313: astore #20
    //   1315: aload #20
    //   1317: invokevirtual hasMoreTokens : ()Z
    //   1320: ifeq -> 1667
    //   1323: aload #20
    //   1325: invokevirtual nextToken : ()Ljava/lang/String;
    //   1328: astore #16
    //   1330: aload #16
    //   1332: ldc_w 'OUT'
    //   1335: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1338: ifeq -> 1377
    //   1341: aload #20
    //   1343: invokevirtual hasMoreTokens : ()Z
    //   1346: ifeq -> 1364
    //   1349: aload #20
    //   1351: invokevirtual nextToken : ()Ljava/lang/String;
    //   1354: astore_1
    //   1355: iconst_1
    //   1356: istore #13
    //   1358: iconst_0
    //   1359: istore #14
    //   1361: goto -> 1471
    //   1364: ldc_w 'Internal error when parsing callable statement metadata (missing parameter name)'
    //   1367: aload #4
    //   1369: aload_0
    //   1370: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1373: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1376: athrow
    //   1377: aload #16
    //   1379: ldc_w 'INOUT'
    //   1382: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1385: ifeq -> 1421
    //   1388: aload #20
    //   1390: invokevirtual hasMoreTokens : ()Z
    //   1393: ifeq -> 1408
    //   1396: aload #20
    //   1398: invokevirtual nextToken : ()Ljava/lang/String;
    //   1401: astore_1
    //   1402: iconst_1
    //   1403: istore #13
    //   1405: goto -> 1468
    //   1408: ldc_w 'Internal error when parsing callable statement metadata (missing parameter name)'
    //   1411: aload #4
    //   1413: aload_0
    //   1414: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1417: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1420: athrow
    //   1421: aload #16
    //   1423: astore_1
    //   1424: aload #16
    //   1426: ldc_w 'IN'
    //   1429: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1432: ifeq -> 1465
    //   1435: aload #20
    //   1437: invokevirtual hasMoreTokens : ()Z
    //   1440: ifeq -> 1452
    //   1443: aload #20
    //   1445: invokevirtual nextToken : ()Ljava/lang/String;
    //   1448: astore_1
    //   1449: goto -> 1465
    //   1452: ldc_w 'Internal error when parsing callable statement metadata (missing parameter name)'
    //   1455: aload #4
    //   1457: aload_0
    //   1458: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1461: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1464: athrow
    //   1465: iconst_0
    //   1466: istore #13
    //   1468: iconst_1
    //   1469: istore #14
    //   1471: aload #20
    //   1473: invokevirtual hasMoreTokens : ()Z
    //   1476: ifeq -> 1654
    //   1479: new java/lang/StringBuilder
    //   1482: dup
    //   1483: aload #20
    //   1485: invokevirtual nextToken : ()Ljava/lang/String;
    //   1488: invokespecial <init> : (Ljava/lang/String;)V
    //   1491: astore #16
    //   1493: aload #20
    //   1495: invokevirtual hasMoreTokens : ()Z
    //   1498: ifeq -> 1524
    //   1501: aload #16
    //   1503: ldc_w ' '
    //   1506: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1509: pop
    //   1510: aload #16
    //   1512: aload #20
    //   1514: invokevirtual nextToken : ()Ljava/lang/String;
    //   1517: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1520: pop
    //   1521: goto -> 1493
    //   1524: new com/mysql/jdbc/DatabaseMetaData$TypeDescriptor
    //   1527: dup
    //   1528: aload_0
    //   1529: aload #16
    //   1531: invokevirtual toString : ()Ljava/lang/String;
    //   1534: aload_2
    //   1535: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;)V
    //   1538: astore #20
    //   1540: aload_1
    //   1541: aload_3
    //   1542: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   1545: ifeq -> 1556
    //   1548: aload_1
    //   1549: aload_3
    //   1550: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   1553: ifne -> 1590
    //   1556: aload_1
    //   1557: astore #16
    //   1559: iload #7
    //   1561: ifeq -> 1603
    //   1564: aload_1
    //   1565: astore #16
    //   1567: aload_1
    //   1568: ldc_w '"'
    //   1571: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   1574: ifeq -> 1603
    //   1577: aload_1
    //   1578: astore #16
    //   1580: aload_1
    //   1581: ldc_w '"'
    //   1584: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   1587: ifeq -> 1603
    //   1590: aload_1
    //   1591: iconst_1
    //   1592: aload_1
    //   1593: invokevirtual length : ()I
    //   1596: iconst_1
    //   1597: isub
    //   1598: invokevirtual substring : (II)Ljava/lang/String;
    //   1601: astore #16
    //   1603: aload #16
    //   1605: aload #15
    //   1607: invokestatic wildCompareIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)Z
    //   1610: ifeq -> 1648
    //   1613: aload #5
    //   1615: aload_0
    //   1616: aload #17
    //   1618: aload #18
    //   1620: aload #16
    //   1622: iload #13
    //   1624: iload #14
    //   1626: iconst_0
    //   1627: aload #20
    //   1629: iload #6
    //   1631: iload #9
    //   1633: invokespecial convertTypeDescriptorToProcedureRow : ([B[BLjava/lang/String;ZZZLcom/mysql/jdbc/DatabaseMetaData$TypeDescriptor;ZI)Lcom/mysql/jdbc/ResultSetRow;
    //   1636: invokeinterface add : (Ljava/lang/Object;)Z
    //   1641: pop
    //   1642: iinc #9, 1
    //   1645: goto -> 1648
    //   1648: iinc #8, 1
    //   1651: goto -> 1260
    //   1654: ldc_w 'Internal error when parsing callable statement metadata (missing parameter type)'
    //   1657: aload #4
    //   1659: aload_0
    //   1660: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1663: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1666: athrow
    //   1667: ldc_w 'Internal error when parsing callable statement metadata (unknown output from 'SHOW CREATE PROCEDURE')'
    //   1670: aload #4
    //   1672: aload_0
    //   1673: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1676: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1679: athrow
    //   1680: return
    //   1681: aload #19
    //   1683: athrow
    //   1684: astore_3
    //   1685: aload_1
    //   1686: astore_2
    //   1687: aload_3
    //   1688: astore_1
    //   1689: aload #4
    //   1691: astore_3
    //   1692: goto -> 1709
    //   1695: astore_1
    //   1696: aconst_null
    //   1697: astore_2
    //   1698: aload #4
    //   1700: astore_3
    //   1701: goto -> 1709
    //   1704: astore_1
    //   1705: aconst_null
    //   1706: astore_2
    //   1707: aconst_null
    //   1708: astore_3
    //   1709: aload_2
    //   1710: ifnull -> 1726
    //   1713: aload_2
    //   1714: invokeinterface close : ()V
    //   1719: goto -> 1726
    //   1722: astore_2
    //   1723: goto -> 1728
    //   1726: aconst_null
    //   1727: astore_2
    //   1728: aload_2
    //   1729: astore #4
    //   1731: aload_3
    //   1732: ifnull -> 1749
    //   1735: aload_3
    //   1736: invokeinterface close : ()V
    //   1741: aload_2
    //   1742: astore #4
    //   1744: goto -> 1749
    //   1747: astore #4
    //   1749: aload #4
    //   1751: ifnull -> 1757
    //   1754: aload #4
    //   1756: athrow
    //   1757: aload_1
    //   1758: athrow
    //   1759: astore_2
    //   1760: goto -> 703
    // Exception table:
    //   from	to	target	type
    //   46	57	1704	finally
    //   61	83	1695	finally
    //   105	112	244	finally
    //   121	128	244	finally
    //   137	166	218	finally
    //   166	182	214	finally
    //   182	193	244	finally
    //   201	207	244	finally
    //   221	232	244	finally
    //   236	242	244	finally
    //   242	244	244	finally
    //   248	257	1695	finally
    //   262	270	244	finally
    //   270	282	1695	finally
    //   287	309	244	finally
    //   312	316	244	finally
    //   316	331	244	finally
    //   334	343	1695	finally
    //   355	379	244	finally
    //   382	401	1695	finally
    //   401	411	1695	finally
    //   411	421	424	java/io/UnsupportedEncodingException
    //   411	421	244	finally
    //   426	434	1695	finally
    //   434	444	1695	finally
    //   444	454	457	java/io/UnsupportedEncodingException
    //   444	454	244	finally
    //   459	467	1695	finally
    //   467	503	1695	finally
    //   508	546	244	finally
    //   553	591	1695	finally
    //   595	603	1684	finally
    //   608	628	1684	finally
    //   638	646	692	finally
    //   649	663	692	finally
    //   663	680	1759	java/sql/SQLException
    //   663	680	692	finally
    //   724	815	1684	finally
    //   820	860	1684	finally
    //   865	870	692	finally
    //   873	878	1684	finally
    //   878	933	1684	finally
    //   938	942	692	finally
    //   945	949	1684	finally
    //   949	975	1684	finally
    //   978	985	1684	finally
    //   992	1004	692	finally
    //   1015	1043	1684	finally
    //   1043	1070	1124	finally
    //   1085	1098	1124	finally
    //   1110	1124	1124	finally
    //   1175	1181	1184	java/sql/SQLException
    //   1198	1205	1211	java/sql/SQLException
    //   1713	1719	1722	java/sql/SQLException
    //   1735	1741	1747	java/sql/SQLException
  }
  
  private int getCascadeDeleteOption(String paramString) {
    int i = paramString.indexOf("ON DELETE");
    if (i != -1) {
      paramString = paramString.substring(i, paramString.length());
      if (paramString.startsWith("ON DELETE CASCADE"))
        return 0; 
      if (paramString.startsWith("ON DELETE SET NULL"))
        return 2; 
      if (paramString.startsWith("ON DELETE RESTRICT"))
        return 1; 
      if (paramString.startsWith("ON DELETE NO ACTION"));
    } 
    return 3;
  }
  
  private int getCascadeUpdateOption(String paramString) {
    int i = paramString.indexOf("ON UPDATE");
    if (i != -1) {
      paramString = paramString.substring(i, paramString.length());
      if (paramString.startsWith("ON UPDATE CASCADE"))
        return 0; 
      if (paramString.startsWith("ON UPDATE SET NULL"))
        return 2; 
      if (paramString.startsWith("ON UPDATE RESTRICT"))
        return 1; 
      if (paramString.startsWith("ON UPDATE NO ACTION"));
    } 
    return 3;
  }
  
  public static DatabaseMetaData getInstance(MySQLConnection paramMySQLConnection, String paramString, boolean paramBoolean) throws SQLException {
    if (!Util.isJdbc4())
      return (paramBoolean && paramMySQLConnection.getUseInformationSchema() && paramMySQLConnection.versionMeetsMinimum(5, 0, 7)) ? new DatabaseMetaDataUsingInfoSchema(paramMySQLConnection, paramString) : new DatabaseMetaData(paramMySQLConnection, paramString); 
    if (paramBoolean && paramMySQLConnection.getUseInformationSchema() && paramMySQLConnection.versionMeetsMinimum(5, 0, 7)) {
      Constructor<?> constructor1 = JDBC_4_DBMD_IS_CTOR;
      ExceptionInterceptor exceptionInterceptor1 = paramMySQLConnection.getExceptionInterceptor();
      return (DatabaseMetaData)Util.handleNewInstance(constructor1, new Object[] { paramMySQLConnection, paramString }, exceptionInterceptor1);
    } 
    Constructor<?> constructor = JDBC_4_DBMD_SHOW_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMySQLConnection.getExceptionInterceptor();
    return (DatabaseMetaData)Util.handleNewInstance(constructor, new Object[] { paramMySQLConnection, paramString }, exceptionInterceptor);
  }
  
  private void getResultsImpl(String paramString1, String paramString2, String paramString3, List<ResultSetRow> paramList, String paramString4, boolean paramBoolean) throws SQLException {
    LocalAndReferencedColumns localAndReferencedColumns = parseTableStatusIntoLocalAndReferencedColumns(paramString3);
    if (paramBoolean && !localAndReferencedColumns.referencedTable.equals(paramString2))
      return; 
    if (localAndReferencedColumns.localColumnsList.size() == localAndReferencedColumns.referencedColumnsList.size()) {
      Iterator<String> iterator1 = localAndReferencedColumns.localColumnsList.iterator();
      Iterator<String> iterator2 = localAndReferencedColumns.referencedColumnsList.iterator();
      for (byte b = 1; iterator1.hasNext(); b++) {
        byte[] arrayOfByte1;
        String str1;
        String str2 = StringUtils.unQuoteIdentifier(iterator1.next(), this.quotedId);
        String str3 = StringUtils.unQuoteIdentifier(iterator2.next(), this.quotedId);
        if (paramString1 == null) {
          arrayOfByte1 = new byte[0];
        } else {
          arrayOfByte1 = s2b(paramString1);
        } 
        if (paramBoolean) {
          str1 = paramString4;
        } else {
          str1 = paramString2;
        } 
        byte[] arrayOfByte3 = s2b(str1);
        byte[] arrayOfByte5 = s2b(str2);
        byte[] arrayOfByte4 = s2b(localAndReferencedColumns.referencedCatalog);
        if (paramBoolean) {
          str1 = paramString2;
        } else {
          str1 = localAndReferencedColumns.referencedTable;
        } 
        byte[] arrayOfByte2 = s2b(str1);
        byte[] arrayOfByte6 = s2b(str3);
        byte[] arrayOfByte8 = s2b(Integer.toString(b));
        int[] arrayOfInt = getForeignKeyActions(paramString3);
        byte[] arrayOfByte7 = s2b(Integer.toString(arrayOfInt[1]));
        byte[] arrayOfByte10 = s2b(Integer.toString(arrayOfInt[0]));
        byte[] arrayOfByte11 = s2b(localAndReferencedColumns.constraintName);
        byte[] arrayOfByte9 = s2b(Integer.toString(7));
        ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
        paramList.add(new ByteArrayRow(new byte[][] { 
                arrayOfByte4, null, arrayOfByte2, arrayOfByte6, arrayOfByte1, null, arrayOfByte3, arrayOfByte5, arrayOfByte8, arrayOfByte7, 
                arrayOfByte10, arrayOfByte11, null, arrayOfByte9 }, exceptionInterceptor));
      } 
      return;
    } 
    throw SQLError.createSQLException("Error parsing foreign keys definition, number of local and referenced columns is not the same.", "S1000", getExceptionInterceptor());
  }
  
  public boolean allProceduresAreCallable() throws SQLException {
    return false;
  }
  
  public boolean allTablesAreSelectable() throws SQLException {
    return false;
  }
  
  public void convertToJdbcFunctionList(String paramString1, ResultSet paramResultSet, boolean paramBoolean, String paramString2, List<ComparableWrapper<String, ResultSetRow>> paramList, int paramInt, Field[] paramArrayOfField) throws SQLException {
    while (paramResultSet.next()) {
      if (paramBoolean) {
        String str = paramResultSet.getString(1);
        if ((paramString2 != null || str != null) && (paramString2 == null || !paramString2.equals(str))) {
          boolean bool1 = false;
          continue;
        } 
      } 
      boolean bool = true;
      continue;
      if (SYNTHETIC_LOCAL_VARIABLE_8 != null) {
        byte[][] arrayOfByte;
        String str = paramResultSet.getString(paramInt);
        if (paramArrayOfField != null && paramArrayOfField.length == 9) {
          byte[] arrayOfByte1;
          byte[][] arrayOfByte2 = new byte[9][];
          if (paramString1 == null) {
            arrayOfByte1 = null;
          } else {
            arrayOfByte1 = s2b(paramString1);
          } 
          arrayOfByte2[0] = arrayOfByte1;
          arrayOfByte2[1] = null;
          arrayOfByte2[2] = s2b(str);
          arrayOfByte2[3] = null;
          arrayOfByte2[4] = null;
          arrayOfByte2[5] = null;
          arrayOfByte2[6] = s2b(paramResultSet.getString("comment"));
          arrayOfByte2[7] = s2b(Integer.toString(2));
          arrayOfByte2[8] = s2b(str);
          arrayOfByte = arrayOfByte2;
        } else {
          byte[] arrayOfByte1;
          byte[][] arrayOfByte2 = new byte[6][];
          if (paramString1 == null) {
            arrayOfByte1 = null;
          } else {
            arrayOfByte1 = s2b(paramString1);
          } 
          arrayOfByte2[0] = arrayOfByte1;
          arrayOfByte2[1] = null;
          arrayOfByte2[2] = s2b(str);
          arrayOfByte2[3] = s2b(paramResultSet.getString("comment"));
          arrayOfByte2[4] = s2b(Integer.toString(getJDBC4FunctionNoTableConstant()));
          arrayOfByte2[5] = s2b(str);
          arrayOfByte = arrayOfByte2;
        } 
        paramList.add(new ComparableWrapper<String, ResultSetRow>(getFullyQualifiedName(paramString1, str), new ByteArrayRow(arrayOfByte, getExceptionInterceptor())));
      } 
    } 
  }
  
  public void convertToJdbcProcedureList(boolean paramBoolean1, String paramString1, ResultSet paramResultSet, boolean paramBoolean2, String paramString2, List<ComparableWrapper<String, ResultSetRow>> paramList, int paramInt) throws SQLException {
    while (paramResultSet.next()) {
      boolean bool2 = false;
      if (paramBoolean2) {
        String str = paramResultSet.getString(1);
        if ((paramString2 != null || str != null) && (paramString2 == null || !paramString2.equals(str))) {
          boolean bool = false;
          continue;
        } 
      } 
      boolean bool1 = true;
      continue;
      if (SYNTHETIC_LOCAL_VARIABLE_8 != null) {
        boolean bool;
        byte[] arrayOfByte1;
        String str1;
        String str2 = paramResultSet.getString(paramInt);
        if (paramString1 == null) {
          arrayOfByte1 = null;
        } else {
          arrayOfByte1 = s2b(paramString1);
        } 
        byte[] arrayOfByte3 = s2b(str2);
        byte[] arrayOfByte4 = s2b(paramResultSet.getString("comment"));
        if (paramBoolean1)
          bool = "FUNCTION".equalsIgnoreCase(paramResultSet.getString("type")); 
        if (bool) {
          str1 = Integer.toString(2);
        } else {
          str1 = Integer.toString(1);
        } 
        byte[] arrayOfByte2 = s2b(str1);
        byte[] arrayOfByte5 = s2b(str2);
        String str3 = getFullyQualifiedName(paramString1, str2);
        ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
        paramList.add(new ComparableWrapper<String, ResultSetRow>(str3, new ByteArrayRow(new byte[][] { arrayOfByte1, null, arrayOfByte3, null, null, null, arrayOfByte4, arrayOfByte2, arrayOfByte5 }, exceptionInterceptor)));
      } 
    } 
  }
  
  public Field[] createColumnsFields() {
    return new Field[] { 
        new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "COLUMN_NAME", 1, 32), new Field("", "DATA_TYPE", 4, 5), new Field("", "TYPE_NAME", 1, 16), new Field("", "COLUMN_SIZE", 4, Integer.toString(2147483647).length()), new Field("", "BUFFER_LENGTH", 4, 10), new Field("", "DECIMAL_DIGITS", 4, 10), new Field("", "NUM_PREC_RADIX", 4, 10), 
        new Field("", "NULLABLE", 4, 10), new Field("", "REMARKS", 1, 0), new Field("", "COLUMN_DEF", 1, 0), new Field("", "SQL_DATA_TYPE", 4, 10), new Field("", "SQL_DATETIME_SUB", 4, 10), new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(2147483647).length()), new Field("", "ORDINAL_POSITION", 4, 10), new Field("", "IS_NULLABLE", 1, 3), new Field("", "SCOPE_CATALOG", 1, 255), new Field("", "SCOPE_SCHEMA", 1, 255), 
        new Field("", "SCOPE_TABLE", 1, 255), new Field("", "SOURCE_DATA_TYPE", 5, 10), new Field("", "IS_AUTOINCREMENT", 1, 3), new Field("", "IS_GENERATEDCOLUMN", 1, 3) };
  }
  
  public Field[] createFieldMetadataForGetProcedures() {
    return new Field[] { new Field("", "PROCEDURE_CAT", 1, 255), new Field("", "PROCEDURE_SCHEM", 1, 255), new Field("", "PROCEDURE_NAME", 1, 255), new Field("", "reserved1", 1, 0), new Field("", "reserved2", 1, 0), new Field("", "reserved3", 1, 0), new Field("", "REMARKS", 1, 255), new Field("", "PROCEDURE_TYPE", 5, 6), new Field("", "SPECIFIC_NAME", 1, 255) };
  }
  
  public Field[] createFkMetadataFields() {
    return new Field[] { 
        new Field("", "PKTABLE_CAT", 1, 255), new Field("", "PKTABLE_SCHEM", 1, 0), new Field("", "PKTABLE_NAME", 1, 255), new Field("", "PKCOLUMN_NAME", 1, 32), new Field("", "FKTABLE_CAT", 1, 255), new Field("", "FKTABLE_SCHEM", 1, 0), new Field("", "FKTABLE_NAME", 1, 255), new Field("", "FKCOLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 2), new Field("", "UPDATE_RULE", 5, 2), 
        new Field("", "DELETE_RULE", 5, 2), new Field("", "FK_NAME", 1, 0), new Field("", "PK_NAME", 1, 0), new Field("", "DEFERRABILITY", 5, 2) };
  }
  
  public Field[] createFunctionColumnsFields() {
    return new Field[] { 
        new Field("", "FUNCTION_CAT", 12, 512), new Field("", "FUNCTION_SCHEM", 12, 512), new Field("", "FUNCTION_NAME", 12, 512), new Field("", "COLUMN_NAME", 12, 512), new Field("", "COLUMN_TYPE", 12, 64), new Field("", "DATA_TYPE", 5, 6), new Field("", "TYPE_NAME", 12, 64), new Field("", "PRECISION", 4, 12), new Field("", "LENGTH", 4, 12), new Field("", "SCALE", 5, 12), 
        new Field("", "RADIX", 5, 6), new Field("", "NULLABLE", 5, 6), new Field("", "REMARKS", 12, 512), new Field("", "CHAR_OCTET_LENGTH", 4, 32), new Field("", "ORDINAL_POSITION", 4, 32), new Field("", "IS_NULLABLE", 12, 12), new Field("", "SPECIFIC_NAME", 12, 64) };
  }
  
  public Field[] createIndexInfoFields() {
    Field[] arrayOfField = new Field[13];
    arrayOfField[0] = new Field("", "TABLE_CAT", 1, 255);
    arrayOfField[1] = new Field("", "TABLE_SCHEM", 1, 0);
    arrayOfField[2] = new Field("", "TABLE_NAME", 1, 255);
    arrayOfField[3] = new Field("", "NON_UNIQUE", 16, 4);
    arrayOfField[4] = new Field("", "INDEX_QUALIFIER", 1, 1);
    arrayOfField[5] = new Field("", "INDEX_NAME", 1, 32);
    arrayOfField[6] = new Field("", "TYPE", 5, 32);
    arrayOfField[7] = new Field("", "ORDINAL_POSITION", 5, 5);
    arrayOfField[8] = new Field("", "COLUMN_NAME", 1, 32);
    arrayOfField[9] = new Field("", "ASC_OR_DESC", 1, 1);
    if (Util.isJdbc42()) {
      arrayOfField[10] = new Field("", "CARDINALITY", -5, 20);
      arrayOfField[11] = new Field("", "PAGES", -5, 20);
    } else {
      arrayOfField[10] = new Field("", "CARDINALITY", 4, 20);
      arrayOfField[11] = new Field("", "PAGES", 4, 10);
    } 
    arrayOfField[12] = new Field("", "FILTER_CONDITION", 1, 32);
    return arrayOfField;
  }
  
  public Field[] createProcedureColumnsFields() {
    return new Field[] { 
        new Field("", "PROCEDURE_CAT", 1, 512), new Field("", "PROCEDURE_SCHEM", 1, 512), new Field("", "PROCEDURE_NAME", 1, 512), new Field("", "COLUMN_NAME", 1, 512), new Field("", "COLUMN_TYPE", 1, 64), new Field("", "DATA_TYPE", 5, 6), new Field("", "TYPE_NAME", 1, 64), new Field("", "PRECISION", 4, 12), new Field("", "LENGTH", 4, 12), new Field("", "SCALE", 5, 12), 
        new Field("", "RADIX", 5, 6), new Field("", "NULLABLE", 5, 6), new Field("", "REMARKS", 1, 512), new Field("", "COLUMN_DEF", 1, 512), new Field("", "SQL_DATA_TYPE", 4, 12), new Field("", "SQL_DATETIME_SUB", 4, 12), new Field("", "CHAR_OCTET_LENGTH", 4, 12), new Field("", "ORDINAL_POSITION", 4, 12), new Field("", "IS_NULLABLE", 1, 512), new Field("", "SPECIFIC_NAME", 1, 512) };
  }
  
  public Field[] createTablesFields() {
    return new Field[] { new Field("", "TABLE_CAT", 12, 255), new Field("", "TABLE_SCHEM", 12, 0), new Field("", "TABLE_NAME", 12, 255), new Field("", "TABLE_TYPE", 12, 5), new Field("", "REMARKS", 12, 0), new Field("", "TYPE_CAT", 12, 0), new Field("", "TYPE_SCHEM", 12, 0), new Field("", "TYPE_NAME", 12, 0), new Field("", "SELF_REFERENCING_COL_NAME", 12, 0), new Field("", "REF_GENERATION", 12, 0) };
  }
  
  public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
    return true;
  }
  
  public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
    return false;
  }
  
  public boolean deletesAreDetected(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
    return true;
  }
  
  public List<ResultSetRow> extractForeignKeyForTable(ArrayList<ResultSetRow> paramArrayList, ResultSet paramResultSet, String paramString) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: iconst_1
    //   2: invokeinterface getBytes : (I)[B
    //   7: astore #13
    //   9: aload_0
    //   10: ldc 'SUPPORTS_FK'
    //   12: invokevirtual s2b : (Ljava/lang/String;)[B
    //   15: astore #14
    //   17: new java/util/StringTokenizer
    //   20: dup
    //   21: aload_2
    //   22: iconst_2
    //   23: invokeinterface getString : (I)Ljava/lang/String;
    //   28: ldc_w '\\n'
    //   31: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   34: astore #16
    //   36: new java/lang/StringBuilder
    //   39: dup
    //   40: ldc_w 'comment; '
    //   43: invokespecial <init> : (Ljava/lang/String;)V
    //   46: astore #15
    //   48: iconst_1
    //   49: istore #4
    //   51: aload #16
    //   53: invokevirtual hasMoreTokens : ()Z
    //   56: ifeq -> 715
    //   59: aload #16
    //   61: invokevirtual nextToken : ()Ljava/lang/String;
    //   64: invokevirtual trim : ()Ljava/lang/String;
    //   67: astore_2
    //   68: aload_2
    //   69: ldc 'CONSTRAINT'
    //   71: invokestatic startsWithIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)Z
    //   74: ifeq -> 190
    //   77: aload_2
    //   78: aload_0
    //   79: getfield quotedId : Ljava/lang/String;
    //   82: iconst_0
    //   83: invokestatic indexOfQuoteDoubleAware : (Ljava/lang/String;Ljava/lang/String;I)I
    //   86: istore #5
    //   88: iload #5
    //   90: iconst_m1
    //   91: if_icmpne -> 109
    //   94: aload_2
    //   95: ldc_w '"'
    //   98: invokevirtual indexOf : (Ljava/lang/String;)I
    //   101: istore #5
    //   103: iconst_0
    //   104: istore #6
    //   106: goto -> 112
    //   109: iconst_1
    //   110: istore #6
    //   112: iload #5
    //   114: iconst_m1
    //   115: if_icmpeq -> 190
    //   118: iload #6
    //   120: ifeq -> 140
    //   123: aload_2
    //   124: aload_0
    //   125: getfield quotedId : Ljava/lang/String;
    //   128: iload #5
    //   130: iconst_1
    //   131: iadd
    //   132: invokestatic indexOfQuoteDoubleAware : (Ljava/lang/String;Ljava/lang/String;I)I
    //   135: istore #6
    //   137: goto -> 153
    //   140: aload_2
    //   141: ldc_w '"'
    //   144: iload #5
    //   146: iconst_1
    //   147: iadd
    //   148: invokestatic indexOfQuoteDoubleAware : (Ljava/lang/String;Ljava/lang/String;I)I
    //   151: istore #6
    //   153: iload #6
    //   155: iconst_m1
    //   156: if_icmpeq -> 190
    //   159: aload_2
    //   160: iload #5
    //   162: iconst_1
    //   163: iadd
    //   164: iload #6
    //   166: invokevirtual substring : (II)Ljava/lang/String;
    //   169: astore #9
    //   171: aload_2
    //   172: iload #6
    //   174: iconst_1
    //   175: iadd
    //   176: aload_2
    //   177: invokevirtual length : ()I
    //   180: invokevirtual substring : (II)Ljava/lang/String;
    //   183: invokevirtual trim : ()Ljava/lang/String;
    //   186: astore_2
    //   187: goto -> 193
    //   190: aconst_null
    //   191: astore #9
    //   193: aload_2
    //   194: ldc_w 'FOREIGN KEY'
    //   197: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   200: ifeq -> 704
    //   203: aload_2
    //   204: astore #10
    //   206: aload_2
    //   207: ldc_w ','
    //   210: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   213: ifeq -> 229
    //   216: aload_2
    //   217: iconst_0
    //   218: aload_2
    //   219: invokevirtual length : ()I
    //   222: iconst_1
    //   223: isub
    //   224: invokevirtual substring : (II)Ljava/lang/String;
    //   227: astore #10
    //   229: aload #10
    //   231: ldc_w 'FOREIGN KEY'
    //   234: invokevirtual indexOf : (Ljava/lang/String;)I
    //   237: istore #5
    //   239: aload_3
    //   240: aload_0
    //   241: getfield quotedId : Ljava/lang/String;
    //   244: aload_0
    //   245: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   248: invokeinterface getPedantic : ()Z
    //   253: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
    //   256: astore_2
    //   257: iload #5
    //   259: iconst_m1
    //   260: if_icmpeq -> 515
    //   263: iload #5
    //   265: bipush #11
    //   267: iadd
    //   268: istore #6
    //   270: aload_0
    //   271: getfield quotedId : Ljava/lang/String;
    //   274: astore #7
    //   276: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   279: astore #8
    //   281: iload #6
    //   283: aload #10
    //   285: ldc_w 'REFERENCES'
    //   288: aload #7
    //   290: aload #7
    //   292: aload #8
    //   294: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   297: istore #5
    //   299: iload #5
    //   301: iconst_m1
    //   302: if_icmpeq -> 515
    //   305: aload #10
    //   307: bipush #40
    //   309: iload #6
    //   311: invokevirtual indexOf : (II)I
    //   314: istore #6
    //   316: aload_0
    //   317: getfield quotedId : Ljava/lang/String;
    //   320: astore #7
    //   322: aload #10
    //   324: iload #6
    //   326: iconst_1
    //   327: iadd
    //   328: iload #6
    //   330: aload #10
    //   332: ldc_w ')'
    //   335: aload #7
    //   337: aload #7
    //   339: aload #8
    //   341: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   344: invokevirtual substring : (II)Ljava/lang/String;
    //   347: astore #11
    //   349: iload #5
    //   351: bipush #10
    //   353: iadd
    //   354: istore #6
    //   356: aload_0
    //   357: getfield quotedId : Ljava/lang/String;
    //   360: astore #7
    //   362: iload #6
    //   364: aload #10
    //   366: ldc_w '('
    //   369: aload #7
    //   371: aload #7
    //   373: aload #8
    //   375: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   378: istore #5
    //   380: iload #5
    //   382: iconst_m1
    //   383: if_icmpeq -> 506
    //   386: aload #10
    //   388: iload #6
    //   390: iload #5
    //   392: invokevirtual substring : (II)Ljava/lang/String;
    //   395: astore #12
    //   397: iload #5
    //   399: iconst_1
    //   400: iadd
    //   401: istore #6
    //   403: aload_0
    //   404: getfield quotedId : Ljava/lang/String;
    //   407: astore #7
    //   409: iload #6
    //   411: aload #10
    //   413: ldc_w ')'
    //   416: aload #7
    //   418: aload #7
    //   420: aload #8
    //   422: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   425: istore #5
    //   427: iload #5
    //   429: iconst_m1
    //   430: if_icmpeq -> 447
    //   433: aload #10
    //   435: iload #6
    //   437: iload #5
    //   439: invokevirtual substring : (II)Ljava/lang/String;
    //   442: astore #7
    //   444: goto -> 450
    //   447: aconst_null
    //   448: astore #7
    //   450: aload_0
    //   451: getfield quotedId : Ljava/lang/String;
    //   454: astore #17
    //   456: iconst_0
    //   457: aload #12
    //   459: ldc_w '.'
    //   462: aload #17
    //   464: aload #17
    //   466: aload #8
    //   468: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   471: istore #5
    //   473: aload #12
    //   475: astore #8
    //   477: iload #5
    //   479: iconst_m1
    //   480: if_icmpeq -> 503
    //   483: aload #12
    //   485: iconst_0
    //   486: iload #5
    //   488: invokevirtual substring : (II)Ljava/lang/String;
    //   491: astore_2
    //   492: aload #12
    //   494: iload #5
    //   496: iconst_1
    //   497: iadd
    //   498: invokevirtual substring : (I)Ljava/lang/String;
    //   501: astore #8
    //   503: goto -> 524
    //   506: aconst_null
    //   507: astore #7
    //   509: aconst_null
    //   510: astore #8
    //   512: goto -> 524
    //   515: aconst_null
    //   516: astore #7
    //   518: aconst_null
    //   519: astore #8
    //   521: aconst_null
    //   522: astore #11
    //   524: iload #4
    //   526: ifne -> 541
    //   529: aload #15
    //   531: ldc_w '; '
    //   534: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   537: pop
    //   538: goto -> 544
    //   541: iconst_0
    //   542: istore #4
    //   544: aload #9
    //   546: ifnull -> 560
    //   549: aload #15
    //   551: aload #9
    //   553: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   556: pop
    //   557: goto -> 569
    //   560: aload #15
    //   562: ldc_w 'not_available'
    //   565: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   568: pop
    //   569: aload #15
    //   571: ldc_w '('
    //   574: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   577: pop
    //   578: aload #15
    //   580: aload #11
    //   582: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   585: pop
    //   586: aload #15
    //   588: ldc_w ') REFER '
    //   591: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   594: pop
    //   595: aload #15
    //   597: aload_2
    //   598: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   601: pop
    //   602: aload #15
    //   604: ldc_w '/'
    //   607: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   610: pop
    //   611: aload #15
    //   613: aload #8
    //   615: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   618: pop
    //   619: aload #15
    //   621: ldc_w '('
    //   624: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   627: pop
    //   628: aload #15
    //   630: aload #7
    //   632: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   635: pop
    //   636: aload #15
    //   638: ldc_w ')'
    //   641: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: pop
    //   645: aload #10
    //   647: ldc_w ')'
    //   650: invokevirtual lastIndexOf : (Ljava/lang/String;)I
    //   653: istore #6
    //   655: iload #4
    //   657: istore #5
    //   659: iload #6
    //   661: aload #10
    //   663: invokevirtual length : ()I
    //   666: iconst_1
    //   667: isub
    //   668: if_icmpeq -> 708
    //   671: aload #10
    //   673: iload #6
    //   675: iconst_1
    //   676: iadd
    //   677: invokevirtual substring : (I)Ljava/lang/String;
    //   680: astore_2
    //   681: aload #15
    //   683: ldc_w ' '
    //   686: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   689: pop
    //   690: aload #15
    //   692: aload_2
    //   693: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   696: pop
    //   697: iload #4
    //   699: istore #5
    //   701: goto -> 708
    //   704: iload #4
    //   706: istore #5
    //   708: iload #5
    //   710: istore #4
    //   712: goto -> 51
    //   715: aload_0
    //   716: aload #15
    //   718: invokevirtual toString : ()Ljava/lang/String;
    //   721: invokevirtual s2b : (Ljava/lang/String;)[B
    //   724: astore_2
    //   725: aload_0
    //   726: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   729: astore_3
    //   730: aload_1
    //   731: new com/mysql/jdbc/ByteArrayRow
    //   734: dup
    //   735: iconst_3
    //   736: anewarray [B
    //   739: dup
    //   740: iconst_0
    //   741: aload #13
    //   743: aastore
    //   744: dup
    //   745: iconst_1
    //   746: aload #14
    //   748: aastore
    //   749: dup
    //   750: iconst_2
    //   751: aload_2
    //   752: aastore
    //   753: aload_3
    //   754: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
    //   757: invokevirtual add : (Ljava/lang/Object;)Z
    //   760: pop
    //   761: aload_1
    //   762: areturn
  }
  
  public ResultSet extractForeignKeyFromCreateTable(String paramString1, String paramString2) throws SQLException {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #8
    //   9: aconst_null
    //   10: astore #6
    //   12: aconst_null
    //   13: astore #5
    //   15: aload_2
    //   16: ifnull -> 29
    //   19: aload #8
    //   21: aload_2
    //   22: invokevirtual add : (Ljava/lang/Object;)Z
    //   25: pop
    //   26: goto -> 87
    //   29: aload_0
    //   30: aload_1
    //   31: ldc_w ''
    //   34: ldc_w '%'
    //   37: iconst_1
    //   38: anewarray java/lang/String
    //   41: dup
    //   42: iconst_0
    //   43: ldc 'TABLE'
    //   45: aastore
    //   46: invokevirtual getTables : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/ResultSet;
    //   49: astore_2
    //   50: aload_2
    //   51: invokeinterface next : ()Z
    //   56: ifeq -> 77
    //   59: aload #8
    //   61: aload_2
    //   62: ldc_w 'TABLE_NAME'
    //   65: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   70: invokevirtual add : (Ljava/lang/Object;)Z
    //   73: pop
    //   74: goto -> 50
    //   77: aload_2
    //   78: ifnull -> 87
    //   81: aload_2
    //   82: invokeinterface close : ()V
    //   87: new java/util/ArrayList
    //   90: dup
    //   91: invokespecial <init> : ()V
    //   94: astore #13
    //   96: new com/mysql/jdbc/Field
    //   99: dup
    //   100: ldc_w ''
    //   103: ldc_w 'Name'
    //   106: iconst_1
    //   107: ldc_w 2147483647
    //   110: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   113: astore #12
    //   115: iconst_0
    //   116: istore_3
    //   117: new com/mysql/jdbc/Field
    //   120: dup
    //   121: ldc_w ''
    //   124: ldc_w 'Type'
    //   127: iconst_1
    //   128: sipush #255
    //   131: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   134: astore #10
    //   136: new com/mysql/jdbc/Field
    //   139: dup
    //   140: ldc_w ''
    //   143: ldc_w 'Comment'
    //   146: iconst_1
    //   147: ldc_w 2147483647
    //   150: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   153: astore #11
    //   155: aload #8
    //   157: invokevirtual size : ()I
    //   160: istore #4
    //   162: aload_0
    //   163: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   166: invokeinterface getMetadataSafeStatement : ()Ljava/sql/Statement;
    //   171: astore #9
    //   173: aload #5
    //   175: astore_2
    //   176: iload_3
    //   177: iload #4
    //   179: if_icmpge -> 374
    //   182: aload_2
    //   183: astore #5
    //   185: aload #8
    //   187: iload_3
    //   188: invokevirtual get : (I)Ljava/lang/Object;
    //   191: checkcast java/lang/String
    //   194: astore #6
    //   196: aload_2
    //   197: astore #5
    //   199: new java/lang/StringBuilder
    //   202: astore #7
    //   204: aload_2
    //   205: astore #5
    //   207: aload #7
    //   209: ldc_w 'SHOW CREATE TABLE '
    //   212: invokespecial <init> : (Ljava/lang/String;)V
    //   215: aload_2
    //   216: astore #5
    //   218: aload #7
    //   220: aload_0
    //   221: aload_1
    //   222: aload #6
    //   224: invokevirtual getFullyQualifiedName : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   227: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: pop
    //   231: aload_2
    //   232: astore #5
    //   234: aload #7
    //   236: invokevirtual toString : ()Ljava/lang/String;
    //   239: astore #6
    //   241: aload_2
    //   242: astore #5
    //   244: aload #9
    //   246: aload #6
    //   248: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   253: astore #7
    //   255: aload #7
    //   257: astore #6
    //   259: aload #7
    //   261: astore #5
    //   263: aload #7
    //   265: invokeinterface next : ()Z
    //   270: ifeq -> 338
    //   273: aload #7
    //   275: astore #5
    //   277: aload_0
    //   278: aload #13
    //   280: aload #7
    //   282: aload_1
    //   283: invokevirtual extractForeignKeyForTable : (Ljava/util/ArrayList;Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/util/List;
    //   286: pop
    //   287: goto -> 255
    //   290: astore #7
    //   292: aload_2
    //   293: astore #6
    //   295: aload_2
    //   296: astore #5
    //   298: ldc_w '42S02'
    //   301: aload #7
    //   303: invokevirtual getSQLState : ()Ljava/lang/String;
    //   306: invokevirtual equals : (Ljava/lang/Object;)Z
    //   309: ifne -> 338
    //   312: aload_2
    //   313: astore #5
    //   315: aload #7
    //   317: invokevirtual getErrorCode : ()I
    //   320: sipush #1146
    //   323: if_icmpne -> 332
    //   326: aload_2
    //   327: astore #6
    //   329: goto -> 338
    //   332: aload_2
    //   333: astore #5
    //   335: aload #7
    //   337: athrow
    //   338: iinc #3, 1
    //   341: aload #6
    //   343: astore_2
    //   344: goto -> 176
    //   347: astore_1
    //   348: aload #5
    //   350: ifnull -> 360
    //   353: aload #5
    //   355: invokeinterface close : ()V
    //   360: aload #9
    //   362: ifnull -> 372
    //   365: aload #9
    //   367: invokeinterface close : ()V
    //   372: aload_1
    //   373: athrow
    //   374: aload_2
    //   375: ifnull -> 384
    //   378: aload_2
    //   379: invokeinterface close : ()V
    //   384: aload #9
    //   386: ifnull -> 396
    //   389: aload #9
    //   391: invokeinterface close : ()V
    //   396: aload_0
    //   397: iconst_3
    //   398: anewarray com/mysql/jdbc/Field
    //   401: dup
    //   402: iconst_0
    //   403: aload #12
    //   405: aastore
    //   406: dup
    //   407: iconst_1
    //   408: aload #10
    //   410: aastore
    //   411: dup
    //   412: iconst_2
    //   413: aload #11
    //   415: aastore
    //   416: aload #13
    //   418: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
    //   421: areturn
    //   422: astore_1
    //   423: goto -> 430
    //   426: astore_1
    //   427: aload #6
    //   429: astore_2
    //   430: aload_2
    //   431: ifnull -> 440
    //   434: aload_2
    //   435: invokeinterface close : ()V
    //   440: aload_1
    //   441: athrow
    // Exception table:
    //   from	to	target	type
    //   29	50	426	finally
    //   50	74	422	finally
    //   185	196	347	finally
    //   199	204	347	finally
    //   207	215	347	finally
    //   218	231	347	finally
    //   234	241	347	finally
    //   244	255	290	java/sql/SQLException
    //   244	255	347	finally
    //   263	273	347	finally
    //   277	287	347	finally
    //   298	312	347	finally
    //   315	326	347	finally
    //   335	338	347	finally
  }
  
  public boolean generatedKeyAlwaysReturned() throws SQLException {
    return true;
  }
  
  public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    Field field17 = new Field("", "TYPE_CAT", 1, 32);
    Field field19 = new Field("", "TYPE_SCHEM", 1, 32);
    Field field6 = new Field("", "TYPE_NAME", 1, 32);
    Field field15 = new Field("", "ATTR_NAME", 1, 32);
    Field field2 = new Field("", "DATA_TYPE", 5, 32);
    Field field4 = new Field("", "ATTR_TYPE_NAME", 1, 32);
    Field field9 = new Field("", "ATTR_SIZE", 4, 32);
    Field field21 = new Field("", "DECIMAL_DIGITS", 4, 32);
    Field field8 = new Field("", "NUM_PREC_RADIX", 4, 32);
    Field field7 = new Field("", "NULLABLE ", 4, 32);
    Field field5 = new Field("", "REMARKS", 1, 32);
    Field field3 = new Field("", "ATTR_DEF", 1, 32);
    Field field18 = new Field("", "SQL_DATA_TYPE", 4, 32);
    Field field10 = new Field("", "SQL_DATETIME_SUB", 4, 32);
    Field field11 = new Field("", "CHAR_OCTET_LENGTH", 4, 32);
    Field field20 = new Field("", "ORDINAL_POSITION", 4, 32);
    Field field1 = new Field("", "IS_NULLABLE", 1, 32);
    Field field12 = new Field("", "SCOPE_CATALOG", 1, 32);
    Field field13 = new Field("", "SCOPE_SCHEMA", 1, 32);
    Field field16 = new Field("", "SCOPE_TABLE", 1, 32);
    Field field14 = new Field("", "SOURCE_DATA_TYPE", 5, 32);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { 
          field17, field19, field6, field15, field2, field4, field9, field21, field8, field7, 
          field5, field3, field18, field10, field11, field20, field1, field12, field13, field16, 
          field14 }, arrayList);
  }
  
  public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) throws SQLException {
    if (paramString3 != null) {
      Field field3 = new Field("", "SCOPE", 5, 5);
      Field field8 = new Field("", "COLUMN_NAME", 1, 32);
      Field field1 = new Field("", "DATA_TYPE", 4, 32);
      Field field5 = new Field("", "TYPE_NAME", 1, 32);
      Field field2 = new Field("", "COLUMN_SIZE", 4, 10);
      Field field7 = new Field("", "BUFFER_LENGTH", 4, 10);
      Field field6 = new Field("", "DECIMAL_DIGITS", 5, 10);
      Field field4 = new Field("", "PSEUDO_COLUMN", 5, 5);
      ArrayList<ResultSetRow> arrayList = new ArrayList();
      Statement statement = this.conn.getMetadataSafeStatement();
      try {
        IterateBlock<String> iterateBlock = new IterateBlock<String>() {
            public final DatabaseMetaData this$0;
            
            public final ArrayList val$rows;
            
            public final Statement val$stmt;
            
            public final String val$table;
            
            public void forEach(String param1String) throws SQLException {
              // Byte code:
              //   0: aconst_null
              //   1: astore #7
              //   3: aconst_null
              //   4: astore #8
              //   6: aload #8
              //   8: astore #6
              //   10: aload #7
              //   12: astore #5
              //   14: new java/lang/StringBuilder
              //   17: astore #9
              //   19: aload #8
              //   21: astore #6
              //   23: aload #7
              //   25: astore #5
              //   27: aload #9
              //   29: ldc 'SHOW COLUMNS FROM '
              //   31: invokespecial <init> : (Ljava/lang/String;)V
              //   34: aload #8
              //   36: astore #6
              //   38: aload #7
              //   40: astore #5
              //   42: aload_0
              //   43: getfield val$table : Ljava/lang/String;
              //   46: astore #11
              //   48: aload #8
              //   50: astore #6
              //   52: aload #7
              //   54: astore #5
              //   56: aload_0
              //   57: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   60: astore #10
              //   62: aload #8
              //   64: astore #6
              //   66: aload #7
              //   68: astore #5
              //   70: aload #9
              //   72: aload #11
              //   74: aload #10
              //   76: getfield quotedId : Ljava/lang/String;
              //   79: aload #10
              //   81: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
              //   84: invokeinterface getPedantic : ()Z
              //   89: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
              //   92: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   95: pop
              //   96: aload #8
              //   98: astore #6
              //   100: aload #7
              //   102: astore #5
              //   104: aload #9
              //   106: ldc ' FROM '
              //   108: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   111: pop
              //   112: aload #8
              //   114: astore #6
              //   116: aload #7
              //   118: astore #5
              //   120: aload_0
              //   121: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   124: astore #10
              //   126: aload #8
              //   128: astore #6
              //   130: aload #7
              //   132: astore #5
              //   134: aload #9
              //   136: aload_1
              //   137: aload #10
              //   139: getfield quotedId : Ljava/lang/String;
              //   142: aload #10
              //   144: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
              //   147: invokeinterface getPedantic : ()Z
              //   152: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
              //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   158: pop
              //   159: aload #8
              //   161: astore #6
              //   163: aload #7
              //   165: astore #5
              //   167: aload_0
              //   168: getfield val$stmt : Ljava/sql/Statement;
              //   171: aload #9
              //   173: invokevirtual toString : ()Ljava/lang/String;
              //   176: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
              //   181: astore_1
              //   182: aload_1
              //   183: astore #6
              //   185: aload_1
              //   186: astore #5
              //   188: aload_1
              //   189: invokeinterface next : ()Z
              //   194: ifeq -> 790
              //   197: aload_1
              //   198: astore #6
              //   200: aload_1
              //   201: astore #5
              //   203: aload_1
              //   204: ldc 'Key'
              //   206: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
              //   211: astore #7
              //   213: aload #7
              //   215: ifnull -> 182
              //   218: aload_1
              //   219: astore #6
              //   221: aload_1
              //   222: astore #5
              //   224: aload #7
              //   226: ldc 'PRI'
              //   228: invokestatic startsWithIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)Z
              //   231: ifeq -> 182
              //   234: aload_1
              //   235: astore #6
              //   237: aload_1
              //   238: astore #5
              //   240: iconst_2
              //   241: invokestatic toString : (I)Ljava/lang/String;
              //   244: invokevirtual getBytes : ()[B
              //   247: astore #9
              //   249: iconst_0
              //   250: istore_3
              //   251: aload_1
              //   252: astore #6
              //   254: aload_1
              //   255: astore #5
              //   257: aload_1
              //   258: ldc 'Field'
              //   260: invokeinterface getBytes : (Ljava/lang/String;)[B
              //   265: astore #10
              //   267: aload_1
              //   268: astore #6
              //   270: aload_1
              //   271: astore #5
              //   273: aload_1
              //   274: ldc 'Type'
              //   276: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
              //   281: astore #8
              //   283: aload_1
              //   284: astore #6
              //   286: aload_1
              //   287: astore #5
              //   289: invokestatic getMaxBuf : ()I
              //   292: istore_2
              //   293: aload_1
              //   294: astore #6
              //   296: aload_1
              //   297: astore #5
              //   299: aload #8
              //   301: ldc 'enum'
              //   303: invokevirtual indexOf : (Ljava/lang/String;)I
              //   306: istore #4
              //   308: iload #4
              //   310: iconst_m1
              //   311: if_icmpeq -> 414
              //   314: aload_1
              //   315: astore #6
              //   317: aload_1
              //   318: astore #5
              //   320: aload #8
              //   322: aload #8
              //   324: ldc '('
              //   326: invokevirtual indexOf : (Ljava/lang/String;)I
              //   329: aload #8
              //   331: ldc ')'
              //   333: invokevirtual indexOf : (Ljava/lang/String;)I
              //   336: invokevirtual substring : (II)Ljava/lang/String;
              //   339: astore #7
              //   341: aload_1
              //   342: astore #6
              //   344: aload_1
              //   345: astore #5
              //   347: new java/util/StringTokenizer
              //   350: astore #8
              //   352: aload_1
              //   353: astore #6
              //   355: aload_1
              //   356: astore #5
              //   358: aload #8
              //   360: aload #7
              //   362: ldc ','
              //   364: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
              //   367: iconst_0
              //   368: istore_2
              //   369: aload_1
              //   370: astore #6
              //   372: aload_1
              //   373: astore #5
              //   375: aload #8
              //   377: invokevirtual hasMoreTokens : ()Z
              //   380: ifeq -> 407
              //   383: aload_1
              //   384: astore #6
              //   386: aload_1
              //   387: astore #5
              //   389: iload_2
              //   390: aload #8
              //   392: invokevirtual nextToken : ()Ljava/lang/String;
              //   395: invokevirtual length : ()I
              //   398: iconst_2
              //   399: isub
              //   400: invokestatic max : (II)I
              //   403: istore_2
              //   404: goto -> 369
              //   407: ldc 'enum'
              //   409: astore #7
              //   411: goto -> 571
              //   414: aload #8
              //   416: astore #7
              //   418: aload_1
              //   419: astore #6
              //   421: aload_1
              //   422: astore #5
              //   424: aload #8
              //   426: ldc '('
              //   428: invokevirtual indexOf : (Ljava/lang/String;)I
              //   431: iconst_m1
              //   432: if_icmpeq -> 571
              //   435: aload_1
              //   436: astore #6
              //   438: aload_1
              //   439: astore #5
              //   441: aload #8
              //   443: ldc ','
              //   445: invokevirtual indexOf : (Ljava/lang/String;)I
              //   448: iconst_m1
              //   449: if_icmpeq -> 517
              //   452: aload_1
              //   453: astore #6
              //   455: aload_1
              //   456: astore #5
              //   458: aload #8
              //   460: aload #8
              //   462: ldc '('
              //   464: invokevirtual indexOf : (Ljava/lang/String;)I
              //   467: iconst_1
              //   468: iadd
              //   469: aload #8
              //   471: ldc ','
              //   473: invokevirtual indexOf : (Ljava/lang/String;)I
              //   476: invokevirtual substring : (II)Ljava/lang/String;
              //   479: invokestatic parseInt : (Ljava/lang/String;)I
              //   482: istore_2
              //   483: aload_1
              //   484: astore #6
              //   486: aload_1
              //   487: astore #5
              //   489: aload #8
              //   491: aload #8
              //   493: ldc ','
              //   495: invokevirtual indexOf : (Ljava/lang/String;)I
              //   498: iconst_1
              //   499: iadd
              //   500: aload #8
              //   502: ldc ')'
              //   504: invokevirtual indexOf : (Ljava/lang/String;)I
              //   507: invokevirtual substring : (II)Ljava/lang/String;
              //   510: invokestatic parseInt : (Ljava/lang/String;)I
              //   513: istore_3
              //   514: goto -> 550
              //   517: aload_1
              //   518: astore #6
              //   520: aload_1
              //   521: astore #5
              //   523: aload #8
              //   525: aload #8
              //   527: ldc '('
              //   529: invokevirtual indexOf : (Ljava/lang/String;)I
              //   532: iconst_1
              //   533: iadd
              //   534: aload #8
              //   536: ldc ')'
              //   538: invokevirtual indexOf : (Ljava/lang/String;)I
              //   541: invokevirtual substring : (II)Ljava/lang/String;
              //   544: invokestatic parseInt : (Ljava/lang/String;)I
              //   547: istore_2
              //   548: iconst_0
              //   549: istore_3
              //   550: aload_1
              //   551: astore #6
              //   553: aload_1
              //   554: astore #5
              //   556: aload #8
              //   558: iconst_0
              //   559: aload #8
              //   561: ldc '('
              //   563: invokevirtual indexOf : (Ljava/lang/String;)I
              //   566: invokevirtual substring : (II)Ljava/lang/String;
              //   569: astore #7
              //   571: aload_1
              //   572: astore #6
              //   574: aload_1
              //   575: astore #5
              //   577: aload_0
              //   578: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   581: aload #7
              //   583: invokestatic mysqlToJavaType : (Ljava/lang/String;)I
              //   586: invokestatic valueOf : (I)Ljava/lang/String;
              //   589: invokevirtual s2b : (Ljava/lang/String;)[B
              //   592: astore #8
              //   594: aload_1
              //   595: astore #6
              //   597: aload_1
              //   598: astore #5
              //   600: aload_0
              //   601: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   604: aload #7
              //   606: invokevirtual s2b : (Ljava/lang/String;)[B
              //   609: astore #16
              //   611: iload_2
              //   612: iload_3
              //   613: iadd
              //   614: istore_2
              //   615: aload_1
              //   616: astore #6
              //   618: aload_1
              //   619: astore #5
              //   621: iload_2
              //   622: invokestatic toString : (I)Ljava/lang/String;
              //   625: invokevirtual getBytes : ()[B
              //   628: astore #13
              //   630: aload_1
              //   631: astore #6
              //   633: aload_1
              //   634: astore #5
              //   636: iload_2
              //   637: invokestatic toString : (I)Ljava/lang/String;
              //   640: invokevirtual getBytes : ()[B
              //   643: astore #17
              //   645: aload_1
              //   646: astore #6
              //   648: aload_1
              //   649: astore #5
              //   651: iload_3
              //   652: invokestatic toString : (I)Ljava/lang/String;
              //   655: invokevirtual getBytes : ()[B
              //   658: astore #15
              //   660: aload_1
              //   661: astore #6
              //   663: aload_1
              //   664: astore #5
              //   666: iconst_1
              //   667: invokestatic toString : (I)Ljava/lang/String;
              //   670: invokevirtual getBytes : ()[B
              //   673: astore #11
              //   675: aload_1
              //   676: astore #6
              //   678: aload_1
              //   679: astore #5
              //   681: aload_0
              //   682: getfield val$rows : Ljava/util/ArrayList;
              //   685: astore #14
              //   687: aload_1
              //   688: astore #6
              //   690: aload_1
              //   691: astore #5
              //   693: new com/mysql/jdbc/ByteArrayRow
              //   696: astore #12
              //   698: aload_1
              //   699: astore #6
              //   701: aload_1
              //   702: astore #5
              //   704: aload_0
              //   705: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   708: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
              //   711: astore #7
              //   713: aload_1
              //   714: astore #6
              //   716: aload_1
              //   717: astore #5
              //   719: aload #12
              //   721: bipush #8
              //   723: anewarray [B
              //   726: dup
              //   727: iconst_0
              //   728: aload #9
              //   730: aastore
              //   731: dup
              //   732: iconst_1
              //   733: aload #10
              //   735: aastore
              //   736: dup
              //   737: iconst_2
              //   738: aload #8
              //   740: aastore
              //   741: dup
              //   742: iconst_3
              //   743: aload #16
              //   745: aastore
              //   746: dup
              //   747: iconst_4
              //   748: aload #13
              //   750: aastore
              //   751: dup
              //   752: iconst_5
              //   753: aload #17
              //   755: aastore
              //   756: dup
              //   757: bipush #6
              //   759: aload #15
              //   761: aastore
              //   762: dup
              //   763: bipush #7
              //   765: aload #11
              //   767: aastore
              //   768: aload #7
              //   770: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
              //   773: aload_1
              //   774: astore #6
              //   776: aload_1
              //   777: astore #5
              //   779: aload #14
              //   781: aload #12
              //   783: invokevirtual add : (Ljava/lang/Object;)Z
              //   786: pop
              //   787: goto -> 182
              //   790: aload_1
              //   791: ifnull -> 835
              //   794: aload_1
              //   795: invokeinterface close : ()V
              //   800: goto -> 835
              //   803: astore_1
              //   804: goto -> 842
              //   807: astore_1
              //   808: aload #5
              //   810: astore #6
              //   812: ldc '42S02'
              //   814: aload_1
              //   815: invokevirtual getSQLState : ()Ljava/lang/String;
              //   818: invokevirtual equals : (Ljava/lang/Object;)Z
              //   821: ifeq -> 836
              //   824: aload #5
              //   826: ifnull -> 835
              //   829: aload #5
              //   831: astore_1
              //   832: goto -> 794
              //   835: return
              //   836: aload #5
              //   838: astore #6
              //   840: aload_1
              //   841: athrow
              //   842: aload #6
              //   844: ifnull -> 854
              //   847: aload #6
              //   849: invokeinterface close : ()V
              //   854: aload_1
              //   855: athrow
              //   856: astore_1
              //   857: goto -> 835
              //   860: astore #5
              //   862: goto -> 854
              // Exception table:
              //   from	to	target	type
              //   14	19	807	java/sql/SQLException
              //   14	19	803	finally
              //   27	34	807	java/sql/SQLException
              //   27	34	803	finally
              //   42	48	807	java/sql/SQLException
              //   42	48	803	finally
              //   56	62	807	java/sql/SQLException
              //   56	62	803	finally
              //   70	96	807	java/sql/SQLException
              //   70	96	803	finally
              //   104	112	807	java/sql/SQLException
              //   104	112	803	finally
              //   120	126	807	java/sql/SQLException
              //   120	126	803	finally
              //   134	159	807	java/sql/SQLException
              //   134	159	803	finally
              //   167	182	807	java/sql/SQLException
              //   167	182	803	finally
              //   188	197	807	java/sql/SQLException
              //   188	197	803	finally
              //   203	213	807	java/sql/SQLException
              //   203	213	803	finally
              //   224	234	807	java/sql/SQLException
              //   224	234	803	finally
              //   240	249	807	java/sql/SQLException
              //   240	249	803	finally
              //   257	267	807	java/sql/SQLException
              //   257	267	803	finally
              //   273	283	807	java/sql/SQLException
              //   273	283	803	finally
              //   289	293	807	java/sql/SQLException
              //   289	293	803	finally
              //   299	308	807	java/sql/SQLException
              //   299	308	803	finally
              //   320	341	807	java/sql/SQLException
              //   320	341	803	finally
              //   347	352	807	java/sql/SQLException
              //   347	352	803	finally
              //   358	367	807	java/sql/SQLException
              //   358	367	803	finally
              //   375	383	807	java/sql/SQLException
              //   375	383	803	finally
              //   389	404	807	java/sql/SQLException
              //   389	404	803	finally
              //   424	435	807	java/sql/SQLException
              //   424	435	803	finally
              //   441	452	807	java/sql/SQLException
              //   441	452	803	finally
              //   458	483	807	java/sql/SQLException
              //   458	483	803	finally
              //   489	514	807	java/sql/SQLException
              //   489	514	803	finally
              //   523	548	807	java/sql/SQLException
              //   523	548	803	finally
              //   556	571	807	java/sql/SQLException
              //   556	571	803	finally
              //   577	594	807	java/sql/SQLException
              //   577	594	803	finally
              //   600	611	807	java/sql/SQLException
              //   600	611	803	finally
              //   621	630	807	java/sql/SQLException
              //   621	630	803	finally
              //   636	645	807	java/sql/SQLException
              //   636	645	803	finally
              //   651	660	807	java/sql/SQLException
              //   651	660	803	finally
              //   666	675	807	java/sql/SQLException
              //   666	675	803	finally
              //   681	687	807	java/sql/SQLException
              //   681	687	803	finally
              //   693	698	807	java/sql/SQLException
              //   693	698	803	finally
              //   704	713	807	java/sql/SQLException
              //   704	713	803	finally
              //   719	773	807	java/sql/SQLException
              //   719	773	803	finally
              //   779	787	807	java/sql/SQLException
              //   779	787	803	finally
              //   794	800	856	java/lang/Exception
              //   812	824	803	finally
              //   840	842	803	finally
              //   847	854	860	java/lang/Exception
            }
          };
        super(this, getCatalogIterator(paramString1), paramString3, statement, arrayList);
        iterateBlock.doForAll();
        return buildResultSet(new Field[] { field3, field8, field1, field5, field2, field7, field6, field4 }, arrayList);
      } finally {
        if (statement != null)
          statement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public IteratorWithCleanup<String> getCatalogIterator(String paramString) throws SQLException {
    ResultSetIterator resultSetIterator;
    if (paramString != null) {
      if (!paramString.equals("")) {
        SingleStringIterator singleStringIterator;
        if (this.conn.getPedantic()) {
          singleStringIterator = new SingleStringIterator(paramString);
        } else {
          singleStringIterator = new SingleStringIterator(StringUtils.unQuoteIdentifier((String)singleStringIterator, this.quotedId));
        } 
      } else {
        SingleStringIterator singleStringIterator = new SingleStringIterator(this.database);
      } 
    } else if (this.conn.getNullCatalogMeansCurrent()) {
      SingleStringIterator singleStringIterator = new SingleStringIterator(this.database);
    } else {
      resultSetIterator = new ResultSetIterator(getCatalogs(), 1);
    } 
    return resultSetIterator;
  }
  
  public String getCatalogSeparator() throws SQLException {
    return ".";
  }
  
  public String getCatalogTerm() throws SQLException {
    return "database";
  }
  
  public ResultSet getCatalogs() throws SQLException {
    // Byte code:
    //   0: aconst_null
    //   1: astore #5
    //   3: aconst_null
    //   4: astore_2
    //   5: aload_0
    //   6: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   9: invokeinterface getMetadataSafeStatement : ()Ljava/sql/Statement;
    //   14: astore_3
    //   15: aload_3
    //   16: ldc_w 'SHOW DATABASES'
    //   19: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   24: astore #4
    //   26: aload #4
    //   28: astore_2
    //   29: aload #4
    //   31: invokeinterface last : ()Z
    //   36: ifeq -> 63
    //   39: aload #4
    //   41: astore_2
    //   42: aload #4
    //   44: invokeinterface getRow : ()I
    //   49: istore_1
    //   50: aload #4
    //   52: astore_2
    //   53: aload #4
    //   55: invokeinterface beforeFirst : ()V
    //   60: goto -> 65
    //   63: iconst_0
    //   64: istore_1
    //   65: aload #4
    //   67: astore_2
    //   68: new java/util/ArrayList
    //   71: astore #7
    //   73: aload #4
    //   75: astore_2
    //   76: aload #7
    //   78: iload_1
    //   79: invokespecial <init> : (I)V
    //   82: aload #4
    //   84: astore_2
    //   85: aload #4
    //   87: invokeinterface next : ()Z
    //   92: ifeq -> 117
    //   95: aload #4
    //   97: astore_2
    //   98: aload #7
    //   100: aload #4
    //   102: iconst_1
    //   103: invokeinterface getString : (I)Ljava/lang/String;
    //   108: invokeinterface add : (Ljava/lang/Object;)Z
    //   113: pop
    //   114: goto -> 82
    //   117: aload #4
    //   119: astore_2
    //   120: aload #7
    //   122: invokestatic sort : (Ljava/util/List;)V
    //   125: aload #4
    //   127: astore_2
    //   128: new com/mysql/jdbc/Field
    //   131: astore #6
    //   133: aload #4
    //   135: astore_2
    //   136: aload #6
    //   138: ldc_w ''
    //   141: ldc_w 'TABLE_CAT'
    //   144: bipush #12
    //   146: aload #4
    //   148: invokeinterface getMetaData : ()Ljava/sql/ResultSetMetaData;
    //   153: iconst_1
    //   154: invokeinterface getColumnDisplaySize : (I)I
    //   159: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   162: aload #4
    //   164: astore_2
    //   165: new java/util/ArrayList
    //   168: astore #5
    //   170: aload #4
    //   172: astore_2
    //   173: aload #5
    //   175: iload_1
    //   176: invokespecial <init> : (I)V
    //   179: aload #4
    //   181: astore_2
    //   182: aload #7
    //   184: invokeinterface iterator : ()Ljava/util/Iterator;
    //   189: astore #8
    //   191: aload #4
    //   193: astore_2
    //   194: aload #8
    //   196: invokeinterface hasNext : ()Z
    //   201: ifeq -> 273
    //   204: aload #4
    //   206: astore_2
    //   207: aload_0
    //   208: aload #8
    //   210: invokeinterface next : ()Ljava/lang/Object;
    //   215: checkcast java/lang/String
    //   218: invokevirtual s2b : (Ljava/lang/String;)[B
    //   221: astore #10
    //   223: aload #4
    //   225: astore_2
    //   226: new com/mysql/jdbc/ByteArrayRow
    //   229: astore #9
    //   231: aload #4
    //   233: astore_2
    //   234: aload_0
    //   235: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   238: astore #7
    //   240: aload #4
    //   242: astore_2
    //   243: aload #9
    //   245: iconst_1
    //   246: anewarray [B
    //   249: dup
    //   250: iconst_0
    //   251: aload #10
    //   253: aastore
    //   254: aload #7
    //   256: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
    //   259: aload #4
    //   261: astore_2
    //   262: aload #5
    //   264: aload #9
    //   266: invokevirtual add : (Ljava/lang/Object;)Z
    //   269: pop
    //   270: goto -> 191
    //   273: aload #4
    //   275: astore_2
    //   276: aload_0
    //   277: iconst_1
    //   278: anewarray com/mysql/jdbc/Field
    //   281: dup
    //   282: iconst_0
    //   283: aload #6
    //   285: aastore
    //   286: aload #5
    //   288: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
    //   291: astore #5
    //   293: aload #4
    //   295: ifnull -> 313
    //   298: aload #4
    //   300: invokeinterface close : ()V
    //   305: goto -> 313
    //   308: astore_2
    //   309: aload_2
    //   310: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   313: aload_3
    //   314: ifnull -> 331
    //   317: aload_3
    //   318: invokeinterface close : ()V
    //   323: goto -> 331
    //   326: astore_2
    //   327: aload_2
    //   328: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   331: aload #5
    //   333: areturn
    //   334: astore #4
    //   336: aload_2
    //   337: astore #5
    //   339: aload #4
    //   341: astore_2
    //   342: goto -> 348
    //   345: astore_2
    //   346: aconst_null
    //   347: astore_3
    //   348: aload #5
    //   350: ifnull -> 370
    //   353: aload #5
    //   355: invokeinterface close : ()V
    //   360: goto -> 370
    //   363: astore #4
    //   365: aload #4
    //   367: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   370: aload_3
    //   371: ifnull -> 388
    //   374: aload_3
    //   375: invokeinterface close : ()V
    //   380: goto -> 388
    //   383: astore_3
    //   384: aload_3
    //   385: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   388: aload_2
    //   389: athrow
    // Exception table:
    //   from	to	target	type
    //   5	15	345	finally
    //   15	26	334	finally
    //   29	39	334	finally
    //   42	50	334	finally
    //   53	60	334	finally
    //   68	73	334	finally
    //   76	82	334	finally
    //   85	95	334	finally
    //   98	114	334	finally
    //   120	125	334	finally
    //   128	133	334	finally
    //   136	162	334	finally
    //   165	170	334	finally
    //   173	179	334	finally
    //   182	191	334	finally
    //   194	204	334	finally
    //   207	223	334	finally
    //   226	231	334	finally
    //   234	240	334	finally
    //   243	259	334	finally
    //   262	270	334	finally
    //   276	293	334	finally
    //   298	305	308	java/sql/SQLException
    //   317	323	326	java/sql/SQLException
    //   353	360	363	java/sql/SQLException
    //   374	380	383	java/sql/SQLException
  }
  
  public ResultSet getClientInfoProperties() throws SQLException {
    Field field4 = new Field("", "NAME", 12, 255);
    Field field2 = new Field("", "MAX_LEN", 4, 10);
    Field field3 = new Field("", "DEFAULT_VALUE", 12, 255);
    Field field1 = new Field("", "DESCRIPTION", 12, 255);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    MySQLConnection mySQLConnection = this.conn;
    return buildResultSet(new Field[] { field4, field2, field3, field1 }, arrayList, mySQLConnection);
  }
  
  public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    // Byte code:
    //   0: new com/mysql/jdbc/Field
    //   3: dup
    //   4: ldc_w ''
    //   7: ldc_w 'TABLE_CAT'
    //   10: iconst_1
    //   11: bipush #64
    //   13: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   16: astore #13
    //   18: new com/mysql/jdbc/Field
    //   21: dup
    //   22: ldc_w ''
    //   25: ldc_w 'TABLE_SCHEM'
    //   28: iconst_1
    //   29: iconst_1
    //   30: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   33: astore #12
    //   35: new com/mysql/jdbc/Field
    //   38: dup
    //   39: ldc_w ''
    //   42: ldc_w 'TABLE_NAME'
    //   45: iconst_1
    //   46: bipush #64
    //   48: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   51: astore #8
    //   53: new com/mysql/jdbc/Field
    //   56: dup
    //   57: ldc_w ''
    //   60: ldc_w 'COLUMN_NAME'
    //   63: iconst_1
    //   64: bipush #64
    //   66: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   69: astore #11
    //   71: new com/mysql/jdbc/Field
    //   74: dup
    //   75: ldc_w ''
    //   78: ldc_w 'GRANTOR'
    //   81: iconst_1
    //   82: bipush #77
    //   84: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   87: astore #9
    //   89: new com/mysql/jdbc/Field
    //   92: dup
    //   93: ldc_w ''
    //   96: ldc_w 'GRANTEE'
    //   99: iconst_1
    //   100: bipush #77
    //   102: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   105: astore #15
    //   107: new com/mysql/jdbc/Field
    //   110: dup
    //   111: ldc_w ''
    //   114: ldc_w 'PRIVILEGE'
    //   117: iconst_1
    //   118: bipush #64
    //   120: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   123: astore #7
    //   125: new com/mysql/jdbc/Field
    //   128: dup
    //   129: ldc_w ''
    //   132: ldc_w 'IS_GRANTABLE'
    //   135: iconst_1
    //   136: iconst_3
    //   137: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   140: astore #14
    //   142: new java/util/ArrayList
    //   145: dup
    //   146: invokespecial <init> : ()V
    //   149: astore #10
    //   151: aload_0
    //   152: ldc_w 'SELECT c.host, c.db, t.grantor, c.user, c.table_name, c.column_name, c.column_priv FROM mysql.columns_priv c, mysql.tables_priv t WHERE c.host = t.host AND c.db = t.db AND c.table_name = t.table_name AND c.db LIKE ? AND c.table_name = ? AND c.column_name LIKE ?'
    //   155: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   158: astore #6
    //   160: aload_1
    //   161: ifnull -> 187
    //   164: aload_1
    //   165: invokevirtual length : ()I
    //   168: istore #5
    //   170: iload #5
    //   172: ifeq -> 187
    //   175: goto -> 191
    //   178: astore_1
    //   179: aconst_null
    //   180: astore_2
    //   181: aload #6
    //   183: astore_3
    //   184: goto -> 628
    //   187: ldc_w '%'
    //   190: astore_1
    //   191: aload #6
    //   193: iconst_1
    //   194: aload_1
    //   195: invokeinterface setString : (ILjava/lang/String;)V
    //   200: aload #6
    //   202: iconst_2
    //   203: aload_3
    //   204: invokeinterface setString : (ILjava/lang/String;)V
    //   209: aload #6
    //   211: iconst_3
    //   212: aload #4
    //   214: invokeinterface setString : (ILjava/lang/String;)V
    //   219: aload #6
    //   221: invokeinterface executeQuery : ()Ljava/sql/ResultSet;
    //   226: astore_2
    //   227: aload_2
    //   228: invokeinterface next : ()Z
    //   233: ifeq -> 527
    //   236: aload_2
    //   237: iconst_1
    //   238: invokeinterface getString : (I)Ljava/lang/String;
    //   243: astore #18
    //   245: aload_2
    //   246: iconst_2
    //   247: invokeinterface getString : (I)Ljava/lang/String;
    //   252: astore #4
    //   254: aload_2
    //   255: iconst_3
    //   256: invokeinterface getString : (I)Ljava/lang/String;
    //   261: astore #16
    //   263: aload_2
    //   264: iconst_4
    //   265: invokeinterface getString : (I)Ljava/lang/String;
    //   270: astore_1
    //   271: aload_1
    //   272: ifnull -> 288
    //   275: aload_1
    //   276: invokevirtual length : ()I
    //   279: ifne -> 285
    //   282: goto -> 288
    //   285: goto -> 292
    //   288: ldc_w '%'
    //   291: astore_1
    //   292: new java/lang/StringBuilder
    //   295: astore #17
    //   297: aload #17
    //   299: aload_1
    //   300: invokespecial <init> : (Ljava/lang/String;)V
    //   303: aload #18
    //   305: ifnull -> 337
    //   308: aload_0
    //   309: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   312: invokeinterface getUseHostsInPrivileges : ()Z
    //   317: ifeq -> 337
    //   320: aload #17
    //   322: ldc_w '@'
    //   325: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: pop
    //   329: aload #17
    //   331: aload #18
    //   333: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   336: pop
    //   337: aload_2
    //   338: bipush #6
    //   340: invokeinterface getString : (I)Ljava/lang/String;
    //   345: astore_1
    //   346: aload_2
    //   347: bipush #7
    //   349: invokeinterface getString : (I)Ljava/lang/String;
    //   354: astore #18
    //   356: aload #18
    //   358: ifnull -> 524
    //   361: aload #18
    //   363: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   366: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   369: astore #19
    //   371: new java/util/StringTokenizer
    //   374: astore #18
    //   376: aload #18
    //   378: aload #19
    //   380: ldc_w ','
    //   383: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   386: aload #18
    //   388: invokevirtual hasMoreTokens : ()Z
    //   391: ifeq -> 524
    //   394: aload #18
    //   396: invokevirtual nextToken : ()Ljava/lang/String;
    //   399: invokevirtual trim : ()Ljava/lang/String;
    //   402: astore #20
    //   404: bipush #8
    //   406: anewarray [B
    //   409: astore #19
    //   411: aload #19
    //   413: iconst_0
    //   414: aload_0
    //   415: aload #4
    //   417: invokevirtual s2b : (Ljava/lang/String;)[B
    //   420: aastore
    //   421: aload #19
    //   423: iconst_1
    //   424: aconst_null
    //   425: aastore
    //   426: aload #19
    //   428: iconst_2
    //   429: aload_0
    //   430: aload_3
    //   431: invokevirtual s2b : (Ljava/lang/String;)[B
    //   434: aastore
    //   435: aload #19
    //   437: iconst_3
    //   438: aload_0
    //   439: aload_1
    //   440: invokevirtual s2b : (Ljava/lang/String;)[B
    //   443: aastore
    //   444: aload #16
    //   446: ifnull -> 462
    //   449: aload #19
    //   451: iconst_4
    //   452: aload_0
    //   453: aload #16
    //   455: invokevirtual s2b : (Ljava/lang/String;)[B
    //   458: aastore
    //   459: goto -> 467
    //   462: aload #19
    //   464: iconst_4
    //   465: aconst_null
    //   466: aastore
    //   467: aload #19
    //   469: iconst_5
    //   470: aload_0
    //   471: aload #17
    //   473: invokevirtual toString : ()Ljava/lang/String;
    //   476: invokevirtual s2b : (Ljava/lang/String;)[B
    //   479: aastore
    //   480: aload #19
    //   482: bipush #6
    //   484: aload_0
    //   485: aload #20
    //   487: invokevirtual s2b : (Ljava/lang/String;)[B
    //   490: aastore
    //   491: aload #19
    //   493: bipush #7
    //   495: aconst_null
    //   496: aastore
    //   497: new com/mysql/jdbc/ByteArrayRow
    //   500: astore #20
    //   502: aload #20
    //   504: aload #19
    //   506: aload_0
    //   507: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   510: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
    //   513: aload #10
    //   515: aload #20
    //   517: invokevirtual add : (Ljava/lang/Object;)Z
    //   520: pop
    //   521: goto -> 386
    //   524: goto -> 227
    //   527: aload_2
    //   528: ifnull -> 541
    //   531: aload_2
    //   532: invokeinterface close : ()V
    //   537: goto -> 541
    //   540: astore_1
    //   541: aload #6
    //   543: ifnull -> 553
    //   546: aload #6
    //   548: invokeinterface close : ()V
    //   553: aload_0
    //   554: bipush #8
    //   556: anewarray com/mysql/jdbc/Field
    //   559: dup
    //   560: iconst_0
    //   561: aload #13
    //   563: aastore
    //   564: dup
    //   565: iconst_1
    //   566: aload #12
    //   568: aastore
    //   569: dup
    //   570: iconst_2
    //   571: aload #8
    //   573: aastore
    //   574: dup
    //   575: iconst_3
    //   576: aload #11
    //   578: aastore
    //   579: dup
    //   580: iconst_4
    //   581: aload #9
    //   583: aastore
    //   584: dup
    //   585: iconst_5
    //   586: aload #15
    //   588: aastore
    //   589: dup
    //   590: bipush #6
    //   592: aload #7
    //   594: aastore
    //   595: dup
    //   596: bipush #7
    //   598: aload #14
    //   600: aastore
    //   601: aload #10
    //   603: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
    //   606: areturn
    //   607: astore_1
    //   608: aload #6
    //   610: astore_3
    //   611: goto -> 628
    //   614: astore_1
    //   615: aconst_null
    //   616: astore_2
    //   617: aload #6
    //   619: astore_3
    //   620: goto -> 628
    //   623: astore_1
    //   624: aconst_null
    //   625: astore_3
    //   626: aconst_null
    //   627: astore_2
    //   628: aload_2
    //   629: ifnull -> 642
    //   632: aload_2
    //   633: invokeinterface close : ()V
    //   638: goto -> 642
    //   641: astore_2
    //   642: aload_3
    //   643: ifnull -> 652
    //   646: aload_3
    //   647: invokeinterface close : ()V
    //   652: aload_1
    //   653: athrow
    //   654: astore_1
    //   655: goto -> 553
    //   658: astore_2
    //   659: goto -> 652
    // Exception table:
    //   from	to	target	type
    //   151	160	623	finally
    //   164	170	178	finally
    //   191	227	614	finally
    //   227	271	607	finally
    //   275	282	607	finally
    //   292	303	607	finally
    //   308	337	607	finally
    //   337	356	607	finally
    //   361	386	607	finally
    //   386	421	607	finally
    //   426	444	607	finally
    //   449	459	607	finally
    //   467	491	607	finally
    //   497	521	607	finally
    //   531	537	540	java/lang/Exception
    //   546	553	654	java/lang/Exception
    //   632	638	641	java/lang/Exception
    //   646	652	658	java/lang/Exception
  }
  
  public int getColumnType(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    return (paramBoolean2 && paramBoolean1) ? 2 : (paramBoolean2 ? 1 : (paramBoolean1 ? 4 : (paramBoolean3 ? 5 : 0)));
  }
  
  public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    String str = paramString4;
    if (paramString4 == null)
      if (this.conn.getNullNamePatternMatchesAll()) {
        str = "%";
      } else {
        throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }  
    Field[] arrayOfField = createColumnsFields();
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    Statement statement = this.conn.getMetadataSafeStatement();
    try {
      IterateBlock<String> iterateBlock = new IterateBlock<String>() {
          public final DatabaseMetaData this$0;
          
          public final String val$colPattern;
          
          public final ArrayList val$rows;
          
          public final String val$schemaPattern;
          
          public final Statement val$stmt;
          
          public final String val$tableNamePattern;
          
          public void forEach(String param1String) throws SQLException {
            // Byte code:
            //   0: ldc ' FROM '
            //   2: astore #6
            //   4: ldc 'COLUMNS FROM '
            //   6: astore #8
            //   8: ldc 'SHOW '
            //   10: astore #10
            //   12: new java/util/ArrayList
            //   15: dup
            //   16: invokespecial <init> : ()V
            //   19: astore #9
            //   21: aload_0
            //   22: getfield val$tableNamePattern : Ljava/lang/String;
            //   25: astore #7
            //   27: aload #7
            //   29: ifnonnull -> 141
            //   32: aload_0
            //   33: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   36: aload_1
            //   37: aload_0
            //   38: getfield val$schemaPattern : Ljava/lang/String;
            //   41: ldc '%'
            //   43: iconst_0
            //   44: anewarray java/lang/String
            //   47: invokevirtual getTables : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/ResultSet;
            //   50: astore #7
            //   52: aload #7
            //   54: invokeinterface next : ()Z
            //   59: ifeq -> 80
            //   62: aload #9
            //   64: aload #7
            //   66: ldc 'TABLE_NAME'
            //   68: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   73: invokevirtual add : (Ljava/lang/Object;)Z
            //   76: pop
            //   77: goto -> 52
            //   80: aload #7
            //   82: ifnull -> 211
            //   85: aload #7
            //   87: invokeinterface close : ()V
            //   92: goto -> 211
            //   95: astore #7
            //   97: aload #7
            //   99: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
            //   102: goto -> 211
            //   105: astore_1
            //   106: aload #7
            //   108: astore #6
            //   110: goto -> 117
            //   113: astore_1
            //   114: aconst_null
            //   115: astore #6
            //   117: aload #6
            //   119: ifnull -> 139
            //   122: aload #6
            //   124: invokeinterface close : ()V
            //   129: goto -> 139
            //   132: astore #6
            //   134: aload #6
            //   136: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
            //   139: aload_1
            //   140: athrow
            //   141: aload_0
            //   142: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   145: aload_1
            //   146: aload_0
            //   147: getfield val$schemaPattern : Ljava/lang/String;
            //   150: aload #7
            //   152: iconst_0
            //   153: anewarray java/lang/String
            //   156: invokevirtual getTables : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/ResultSet;
            //   159: astore #7
            //   161: aload #7
            //   163: invokeinterface next : ()Z
            //   168: ifeq -> 189
            //   171: aload #9
            //   173: aload #7
            //   175: ldc 'TABLE_NAME'
            //   177: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   182: invokevirtual add : (Ljava/lang/Object;)Z
            //   185: pop
            //   186: goto -> 161
            //   189: aload #7
            //   191: ifnull -> 211
            //   194: aload #7
            //   196: invokeinterface close : ()V
            //   201: goto -> 211
            //   204: astore #7
            //   206: aload #7
            //   208: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
            //   211: aload #9
            //   213: invokevirtual iterator : ()Ljava/util/Iterator;
            //   216: astore #7
            //   218: aload #7
            //   220: invokeinterface hasNext : ()Z
            //   225: ifeq -> 1541
            //   228: aload #7
            //   230: invokeinterface next : ()Ljava/lang/Object;
            //   235: checkcast java/lang/String
            //   238: astore #14
            //   240: new java/lang/StringBuilder
            //   243: astore #12
            //   245: aload #12
            //   247: aload #10
            //   249: invokespecial <init> : (Ljava/lang/String;)V
            //   252: aload_0
            //   253: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   256: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   259: iconst_4
            //   260: iconst_1
            //   261: iconst_0
            //   262: invokeinterface versionMeetsMinimum : (III)Z
            //   267: istore #5
            //   269: iload #5
            //   271: ifeq -> 282
            //   274: aload #12
            //   276: ldc 'FULL '
            //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   281: pop
            //   282: aload #12
            //   284: aload #8
            //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   289: pop
            //   290: aload_0
            //   291: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   294: astore #9
            //   296: aload #12
            //   298: aload #14
            //   300: aload #9
            //   302: getfield quotedId : Ljava/lang/String;
            //   305: aload #9
            //   307: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   310: invokeinterface getPedantic : ()Z
            //   315: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   321: pop
            //   322: aload #12
            //   324: aload #6
            //   326: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   329: pop
            //   330: aload_0
            //   331: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   334: astore #9
            //   336: aload #12
            //   338: aload_1
            //   339: aload #9
            //   341: getfield quotedId : Ljava/lang/String;
            //   344: aload #9
            //   346: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   349: invokeinterface getPedantic : ()Z
            //   354: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   357: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   360: pop
            //   361: aload #12
            //   363: ldc ' LIKE '
            //   365: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   368: pop
            //   369: aload #12
            //   371: aload_0
            //   372: getfield val$colPattern : Ljava/lang/String;
            //   375: ldc '''
            //   377: iconst_1
            //   378: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   381: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   384: pop
            //   385: aload_0
            //   386: getfield val$colPattern : Ljava/lang/String;
            //   389: ldc '%'
            //   391: invokevirtual equals : (Ljava/lang/Object;)Z
            //   394: istore #5
            //   396: iload #5
            //   398: ifne -> 596
            //   401: new java/lang/StringBuilder
            //   404: astore #9
            //   406: aload #9
            //   408: aload #10
            //   410: invokespecial <init> : (Ljava/lang/String;)V
            //   413: aload_0
            //   414: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   417: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   420: iconst_4
            //   421: iconst_1
            //   422: iconst_0
            //   423: invokeinterface versionMeetsMinimum : (III)Z
            //   428: ifeq -> 439
            //   431: aload #9
            //   433: ldc 'FULL '
            //   435: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   438: pop
            //   439: aload #9
            //   441: aload #8
            //   443: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   446: pop
            //   447: aload_0
            //   448: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   451: astore #11
            //   453: aload #9
            //   455: aload #14
            //   457: aload #11
            //   459: getfield quotedId : Ljava/lang/String;
            //   462: aload #11
            //   464: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   467: invokeinterface getPedantic : ()Z
            //   472: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   475: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   478: pop
            //   479: aload #9
            //   481: aload #6
            //   483: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   486: pop
            //   487: aload_0
            //   488: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   491: astore #11
            //   493: aload #9
            //   495: aload_1
            //   496: aload #11
            //   498: getfield quotedId : Ljava/lang/String;
            //   501: aload #11
            //   503: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   506: invokeinterface getPedantic : ()Z
            //   511: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   514: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   517: pop
            //   518: aload_0
            //   519: getfield val$stmt : Ljava/sql/Statement;
            //   522: aload #9
            //   524: invokevirtual toString : ()Ljava/lang/String;
            //   527: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
            //   532: astore #9
            //   534: new java/util/HashMap
            //   537: astore #11
            //   539: aload #11
            //   541: invokespecial <init> : ()V
            //   544: iconst_1
            //   545: istore_2
            //   546: aload #9
            //   548: invokeinterface next : ()Z
            //   553: ifeq -> 583
            //   556: aload #11
            //   558: aload #9
            //   560: ldc 'Field'
            //   562: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   567: iload_2
            //   568: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   571: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   576: pop
            //   577: iinc #2, 1
            //   580: goto -> 546
            //   583: iconst_1
            //   584: istore_3
            //   585: goto -> 604
            //   588: astore_1
            //   589: aload #9
            //   591: astore #6
            //   593: goto -> 1527
            //   596: iconst_0
            //   597: istore_3
            //   598: aconst_null
            //   599: astore #11
            //   601: aconst_null
            //   602: astore #9
            //   604: aload_0
            //   605: getfield val$stmt : Ljava/sql/Statement;
            //   608: aload #12
            //   610: invokevirtual toString : ()Ljava/lang/String;
            //   613: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
            //   618: astore #12
            //   620: iconst_1
            //   621: istore #4
            //   623: aload #12
            //   625: invokeinterface next : ()Z
            //   630: ifeq -> 1492
            //   633: bipush #24
            //   635: anewarray [B
            //   638: astore #15
            //   640: aload #15
            //   642: iconst_0
            //   643: aload_0
            //   644: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   647: aload_1
            //   648: invokevirtual s2b : (Ljava/lang/String;)[B
            //   651: aastore
            //   652: aload #15
            //   654: iconst_1
            //   655: aconst_null
            //   656: aastore
            //   657: aload #15
            //   659: iconst_2
            //   660: aload_0
            //   661: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   664: aload #14
            //   666: invokevirtual s2b : (Ljava/lang/String;)[B
            //   669: aastore
            //   670: aload #15
            //   672: iconst_3
            //   673: aload #12
            //   675: ldc 'Field'
            //   677: invokeinterface getBytes : (Ljava/lang/String;)[B
            //   682: aastore
            //   683: new com/mysql/jdbc/DatabaseMetaData$TypeDescriptor
            //   686: astore #13
            //   688: aload #13
            //   690: aload_0
            //   691: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   694: aload #12
            //   696: ldc 'Type'
            //   698: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   703: aload #12
            //   705: ldc 'Null'
            //   707: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   712: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;)V
            //   715: aload #15
            //   717: iconst_4
            //   718: aload #13
            //   720: getfield dataType : S
            //   723: invokestatic toString : (S)Ljava/lang/String;
            //   726: invokevirtual getBytes : ()[B
            //   729: aastore
            //   730: aload #15
            //   732: iconst_5
            //   733: aload_0
            //   734: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   737: aload #13
            //   739: getfield typeName : Ljava/lang/String;
            //   742: invokevirtual s2b : (Ljava/lang/String;)[B
            //   745: aastore
            //   746: aload #13
            //   748: getfield columnSize : Ljava/lang/Integer;
            //   751: astore #9
            //   753: aload #9
            //   755: ifnonnull -> 767
            //   758: aload #15
            //   760: bipush #6
            //   762: aconst_null
            //   763: aastore
            //   764: goto -> 930
            //   767: aload #12
            //   769: ldc 'Collation'
            //   771: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   776: astore #9
            //   778: aload #9
            //   780: ifnull -> 868
            //   783: ldc 'TEXT'
            //   785: aload #13
            //   787: getfield typeName : Ljava/lang/String;
            //   790: invokevirtual equals : (Ljava/lang/Object;)Z
            //   793: ifne -> 822
            //   796: ldc 'TINYTEXT'
            //   798: aload #13
            //   800: getfield typeName : Ljava/lang/String;
            //   803: invokevirtual equals : (Ljava/lang/Object;)Z
            //   806: ifne -> 822
            //   809: ldc 'MEDIUMTEXT'
            //   811: aload #13
            //   813: getfield typeName : Ljava/lang/String;
            //   816: invokevirtual equals : (Ljava/lang/Object;)Z
            //   819: ifeq -> 868
            //   822: aload #9
            //   824: ldc 'ucs2'
            //   826: invokevirtual indexOf : (Ljava/lang/String;)I
            //   829: iconst_m1
            //   830: if_icmpgt -> 863
            //   833: aload #9
            //   835: ldc 'utf16'
            //   837: invokevirtual indexOf : (Ljava/lang/String;)I
            //   840: iconst_m1
            //   841: if_icmple -> 847
            //   844: goto -> 863
            //   847: aload #9
            //   849: ldc 'utf32'
            //   851: invokevirtual indexOf : (Ljava/lang/String;)I
            //   854: iconst_m1
            //   855: if_icmple -> 868
            //   858: iconst_4
            //   859: istore_2
            //   860: goto -> 870
            //   863: iconst_2
            //   864: istore_2
            //   865: goto -> 870
            //   868: iconst_1
            //   869: istore_2
            //   870: iload_2
            //   871: iconst_1
            //   872: if_icmpne -> 895
            //   875: aload_0
            //   876: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   879: aload #13
            //   881: getfield columnSize : Ljava/lang/Integer;
            //   884: invokevirtual toString : ()Ljava/lang/String;
            //   887: invokevirtual s2b : (Ljava/lang/String;)[B
            //   890: astore #9
            //   892: goto -> 923
            //   895: aload_0
            //   896: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   899: aload #13
            //   901: getfield columnSize : Ljava/lang/Integer;
            //   904: invokevirtual intValue : ()I
            //   907: iload_2
            //   908: idiv
            //   909: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   912: invokevirtual toString : ()Ljava/lang/String;
            //   915: invokevirtual s2b : (Ljava/lang/String;)[B
            //   918: astore #9
            //   920: goto -> 892
            //   923: aload #15
            //   925: bipush #6
            //   927: aload #9
            //   929: aastore
            //   930: aload #15
            //   932: bipush #7
            //   934: aload_0
            //   935: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   938: aload #13
            //   940: getfield bufferLength : I
            //   943: invokestatic toString : (I)Ljava/lang/String;
            //   946: invokevirtual s2b : (Ljava/lang/String;)[B
            //   949: aastore
            //   950: aload #13
            //   952: getfield decimalDigits : Ljava/lang/Integer;
            //   955: astore #9
            //   957: aload #9
            //   959: ifnonnull -> 968
            //   962: aconst_null
            //   963: astore #9
            //   965: goto -> 982
            //   968: aload_0
            //   969: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   972: aload #9
            //   974: invokevirtual toString : ()Ljava/lang/String;
            //   977: invokevirtual s2b : (Ljava/lang/String;)[B
            //   980: astore #9
            //   982: aload #15
            //   984: bipush #8
            //   986: aload #9
            //   988: aastore
            //   989: aload #15
            //   991: bipush #9
            //   993: aload_0
            //   994: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   997: aload #13
            //   999: getfield numPrecRadix : I
            //   1002: invokestatic toString : (I)Ljava/lang/String;
            //   1005: invokevirtual s2b : (Ljava/lang/String;)[B
            //   1008: aastore
            //   1009: aload #15
            //   1011: bipush #10
            //   1013: aload_0
            //   1014: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1017: aload #13
            //   1019: getfield nullability : I
            //   1022: invokestatic toString : (I)Ljava/lang/String;
            //   1025: invokevirtual s2b : (Ljava/lang/String;)[B
            //   1028: aastore
            //   1029: aload_0
            //   1030: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1033: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   1036: astore #9
            //   1038: aload #9
            //   1040: iconst_4
            //   1041: iconst_1
            //   1042: iconst_0
            //   1043: invokeinterface versionMeetsMinimum : (III)Z
            //   1048: ifeq -> 1068
            //   1051: aload #15
            //   1053: bipush #11
            //   1055: aload #12
            //   1057: ldc 'Comment'
            //   1059: invokeinterface getBytes : (Ljava/lang/String;)[B
            //   1064: aastore
            //   1065: goto -> 1095
            //   1068: aload #15
            //   1070: bipush #11
            //   1072: aload #12
            //   1074: ldc 'Extra'
            //   1076: invokeinterface getBytes : (Ljava/lang/String;)[B
            //   1081: aastore
            //   1082: goto -> 1095
            //   1085: astore #9
            //   1087: aload #15
            //   1089: bipush #11
            //   1091: iconst_0
            //   1092: newarray byte
            //   1094: aastore
            //   1095: aload #15
            //   1097: bipush #12
            //   1099: aload #12
            //   1101: ldc 'Default'
            //   1103: invokeinterface getBytes : (Ljava/lang/String;)[B
            //   1108: aastore
            //   1109: aload #15
            //   1111: bipush #13
            //   1113: iconst_1
            //   1114: newarray byte
            //   1116: dup
            //   1117: iconst_0
            //   1118: bipush #48
            //   1120: bastore
            //   1121: aastore
            //   1122: aload #15
            //   1124: bipush #14
            //   1126: iconst_1
            //   1127: newarray byte
            //   1129: dup
            //   1130: iconst_0
            //   1131: bipush #48
            //   1133: bastore
            //   1134: aastore
            //   1135: aload #13
            //   1137: getfield typeName : Ljava/lang/String;
            //   1140: ldc_w 'CHAR'
            //   1143: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
            //   1146: iconst_m1
            //   1147: if_icmpne -> 1206
            //   1150: aload #13
            //   1152: getfield typeName : Ljava/lang/String;
            //   1155: ldc_w 'BLOB'
            //   1158: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
            //   1161: iconst_m1
            //   1162: if_icmpne -> 1206
            //   1165: aload #13
            //   1167: getfield typeName : Ljava/lang/String;
            //   1170: ldc 'TEXT'
            //   1172: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
            //   1175: iconst_m1
            //   1176: if_icmpne -> 1206
            //   1179: aload #13
            //   1181: getfield typeName : Ljava/lang/String;
            //   1184: ldc_w 'BINARY'
            //   1187: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
            //   1190: iconst_m1
            //   1191: if_icmpeq -> 1197
            //   1194: goto -> 1206
            //   1197: aload #15
            //   1199: bipush #15
            //   1201: aconst_null
            //   1202: aastore
            //   1203: goto -> 1216
            //   1206: aload #15
            //   1208: bipush #15
            //   1210: aload #15
            //   1212: bipush #6
            //   1214: aaload
            //   1215: aastore
            //   1216: iload_3
            //   1217: ifne -> 1239
            //   1220: aload #15
            //   1222: bipush #16
            //   1224: iload #4
            //   1226: invokestatic toString : (I)Ljava/lang/String;
            //   1229: invokevirtual getBytes : ()[B
            //   1232: aastore
            //   1233: iinc #4, 1
            //   1236: goto -> 1278
            //   1239: aload #11
            //   1241: aload #12
            //   1243: ldc 'Field'
            //   1245: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   1250: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
            //   1255: checkcast java/lang/Integer
            //   1258: astore #9
            //   1260: aload #9
            //   1262: ifnull -> 1475
            //   1265: aload #15
            //   1267: bipush #16
            //   1269: aload #9
            //   1271: invokevirtual toString : ()Ljava/lang/String;
            //   1274: invokevirtual getBytes : ()[B
            //   1277: aastore
            //   1278: aload #15
            //   1280: bipush #17
            //   1282: aload_0
            //   1283: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1286: aload #13
            //   1288: getfield isNullable : Ljava/lang/String;
            //   1291: invokevirtual s2b : (Ljava/lang/String;)[B
            //   1294: aastore
            //   1295: aload #15
            //   1297: bipush #18
            //   1299: aconst_null
            //   1300: aastore
            //   1301: aload #15
            //   1303: bipush #19
            //   1305: aconst_null
            //   1306: aastore
            //   1307: aload #15
            //   1309: bipush #20
            //   1311: aconst_null
            //   1312: aastore
            //   1313: aload #15
            //   1315: bipush #21
            //   1317: aconst_null
            //   1318: aastore
            //   1319: aload #15
            //   1321: bipush #22
            //   1323: aload_0
            //   1324: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1327: ldc_w ''
            //   1330: invokevirtual s2b : (Ljava/lang/String;)[B
            //   1333: aastore
            //   1334: aload #12
            //   1336: ldc 'Extra'
            //   1338: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
            //   1343: astore #16
            //   1345: aload #16
            //   1347: ifnull -> 1439
            //   1350: aload_0
            //   1351: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1354: astore #17
            //   1356: aload #16
            //   1358: ldc_w 'auto_increment'
            //   1361: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
            //   1364: istore_2
            //   1365: ldc_w 'NO'
            //   1368: astore #13
            //   1370: iload_2
            //   1371: iconst_m1
            //   1372: if_icmpeq -> 1383
            //   1375: ldc_w 'YES'
            //   1378: astore #9
            //   1380: goto -> 1388
            //   1383: ldc_w 'NO'
            //   1386: astore #9
            //   1388: aload #15
            //   1390: bipush #22
            //   1392: aload #17
            //   1394: aload #9
            //   1396: invokevirtual s2b : (Ljava/lang/String;)[B
            //   1399: aastore
            //   1400: aload_0
            //   1401: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1404: astore #17
            //   1406: aload #13
            //   1408: astore #9
            //   1410: aload #16
            //   1412: ldc_w 'generated'
            //   1415: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
            //   1418: iconst_m1
            //   1419: if_icmpeq -> 1427
            //   1422: ldc_w 'YES'
            //   1425: astore #9
            //   1427: aload #15
            //   1429: bipush #23
            //   1431: aload #17
            //   1433: aload #9
            //   1435: invokevirtual s2b : (Ljava/lang/String;)[B
            //   1438: aastore
            //   1439: aload_0
            //   1440: getfield val$rows : Ljava/util/ArrayList;
            //   1443: astore #13
            //   1445: new com/mysql/jdbc/ByteArrayRow
            //   1448: astore #9
            //   1450: aload #9
            //   1452: aload #15
            //   1454: aload_0
            //   1455: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1458: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   1461: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   1464: aload #13
            //   1466: aload #9
            //   1468: invokevirtual add : (Ljava/lang/Object;)Z
            //   1471: pop
            //   1472: goto -> 623
            //   1475: ldc_w 'Can not find column in full column list to determine true ordinal position.'
            //   1478: ldc_w 'S1000'
            //   1481: aload_0
            //   1482: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1485: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   1488: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
            //   1491: athrow
            //   1492: aload #12
            //   1494: ifnull -> 1504
            //   1497: aload #12
            //   1499: invokeinterface close : ()V
            //   1504: goto -> 218
            //   1507: astore_1
            //   1508: aload #12
            //   1510: astore #6
            //   1512: goto -> 1527
            //   1515: astore_1
            //   1516: aload #9
            //   1518: astore #6
            //   1520: goto -> 1527
            //   1523: astore_1
            //   1524: aconst_null
            //   1525: astore #6
            //   1527: aload #6
            //   1529: ifnull -> 1539
            //   1532: aload #6
            //   1534: invokeinterface close : ()V
            //   1539: aload_1
            //   1540: athrow
            //   1541: return
            //   1542: astore_1
            //   1543: aload #7
            //   1545: astore #6
            //   1547: goto -> 1554
            //   1550: astore_1
            //   1551: aconst_null
            //   1552: astore #6
            //   1554: aload #6
            //   1556: ifnull -> 1576
            //   1559: aload #6
            //   1561: invokeinterface close : ()V
            //   1566: goto -> 1576
            //   1569: astore #6
            //   1571: aload #6
            //   1573: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
            //   1576: aload_1
            //   1577: athrow
            //   1578: astore #9
            //   1580: goto -> 1087
            //   1583: astore #9
            //   1585: goto -> 1504
            //   1588: astore #6
            //   1590: goto -> 1539
            // Exception table:
            //   from	to	target	type
            //   32	52	113	finally
            //   52	77	105	finally
            //   85	92	95	java/lang/Exception
            //   122	129	132	java/lang/Exception
            //   141	161	1550	finally
            //   161	186	1542	finally
            //   194	201	204	java/sql/SQLException
            //   240	269	1523	finally
            //   274	282	1523	finally
            //   282	396	1523	finally
            //   401	439	1523	finally
            //   439	534	1523	finally
            //   534	544	588	finally
            //   546	577	588	finally
            //   604	620	1515	finally
            //   623	652	1507	finally
            //   657	753	1507	finally
            //   767	778	1507	finally
            //   783	822	1507	finally
            //   822	844	1507	finally
            //   847	858	1507	finally
            //   875	892	1507	finally
            //   895	920	1507	finally
            //   930	957	1507	finally
            //   968	982	1507	finally
            //   989	1029	1507	finally
            //   1029	1038	1085	java/lang/Exception
            //   1029	1038	1507	finally
            //   1038	1065	1578	java/lang/Exception
            //   1038	1065	1507	finally
            //   1068	1082	1578	java/lang/Exception
            //   1068	1082	1507	finally
            //   1087	1095	1507	finally
            //   1095	1194	1507	finally
            //   1220	1233	1507	finally
            //   1239	1260	1507	finally
            //   1265	1278	1507	finally
            //   1278	1295	1507	finally
            //   1319	1345	1507	finally
            //   1350	1365	1507	finally
            //   1388	1406	1507	finally
            //   1410	1422	1507	finally
            //   1427	1439	1507	finally
            //   1439	1472	1507	finally
            //   1475	1492	1507	finally
            //   1497	1504	1583	java/lang/Exception
            //   1532	1539	1588	java/lang/Exception
            //   1559	1566	1569	java/sql/SQLException
          }
        };
      super(this, getCatalogIterator(paramString1), paramString3, paramString2, str, statement, arrayList);
      iterateBlock.doForAll();
      return buildResultSet(arrayOfField, arrayList);
    } finally {
      if (statement != null)
        statement.close(); 
    } 
  }
  
  public Connection getConnection() throws SQLException {
    return this.conn;
  }
  
  public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws SQLException {
    if (paramString3 != null) {
      Field[] arrayOfField = createFkMetadataFields();
      ArrayList<ResultSetRow> arrayList = new ArrayList();
      if (this.conn.versionMeetsMinimum(3, 23, 0)) {
        Statement statement = this.conn.getMetadataSafeStatement();
        try {
          IterateBlock<String> iterateBlock = new IterateBlock<String>() {
              public final DatabaseMetaData this$0;
              
              public final String val$foreignCatalog;
              
              public final String val$foreignSchema;
              
              public final String val$foreignTable;
              
              public final String val$primaryCatalog;
              
              public final String val$primarySchema;
              
              public final String val$primaryTable;
              
              public final Statement val$stmt;
              
              public final ArrayList val$tuples;
              
              public void forEach(String param1String) throws SQLException {
                // Byte code:
                //   0: aconst_null
                //   1: astore #6
                //   3: iconst_0
                //   4: istore_3
                //   5: aload_0
                //   6: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   9: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
                //   12: iconst_3
                //   13: bipush #23
                //   15: bipush #50
                //   17: invokeinterface versionMeetsMinimum : (III)Z
                //   22: istore #4
                //   24: iload #4
                //   26: ifeq -> 46
                //   29: aload_0
                //   30: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   33: aload_1
                //   34: aconst_null
                //   35: invokevirtual extractForeignKeyFromCreateTable : (Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
                //   38: astore_1
                //   39: goto -> 107
                //   42: astore_1
                //   43: goto -> 763
                //   46: new java/lang/StringBuilder
                //   49: astore #7
                //   51: aload #7
                //   53: ldc 'SHOW TABLE STATUS FROM '
                //   55: invokespecial <init> : (Ljava/lang/String;)V
                //   58: aload_0
                //   59: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   62: astore #8
                //   64: aload #7
                //   66: aload_1
                //   67: aload #8
                //   69: getfield quotedId : Ljava/lang/String;
                //   72: aload #8
                //   74: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
                //   77: invokeinterface getPedantic : ()Z
                //   82: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
                //   85: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   88: pop
                //   89: aload_0
                //   90: getfield val$stmt : Ljava/sql/Statement;
                //   93: aload #7
                //   95: invokevirtual toString : ()Ljava/lang/String;
                //   98: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
                //   103: astore_1
                //   104: goto -> 39
                //   107: aload_0
                //   108: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   111: aload_0
                //   112: getfield val$foreignTable : Ljava/lang/String;
                //   115: invokevirtual getTableNameWithCase : (Ljava/lang/String;)Ljava/lang/String;
                //   118: astore #11
                //   120: aload_0
                //   121: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   124: aload_0
                //   125: getfield val$primaryTable : Ljava/lang/String;
                //   128: invokevirtual getTableNameWithCase : (Ljava/lang/String;)Ljava/lang/String;
                //   131: astore #10
                //   133: aload_1
                //   134: invokeinterface next : ()Z
                //   139: ifeq -> 728
                //   142: aload_1
                //   143: ldc 'Type'
                //   145: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   150: astore #6
                //   152: iload_3
                //   153: istore #4
                //   155: aload #6
                //   157: ifnull -> 722
                //   160: aload #6
                //   162: ldc 'innodb'
                //   164: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
                //   167: ifne -> 183
                //   170: iload_3
                //   171: istore #4
                //   173: aload #6
                //   175: ldc 'SUPPORTS_FK'
                //   177: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
                //   180: ifeq -> 722
                //   183: aload_1
                //   184: ldc 'Comment'
                //   186: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   191: invokevirtual trim : ()Ljava/lang/String;
                //   194: astore #6
                //   196: iload_3
                //   197: istore #4
                //   199: aload #6
                //   201: ifnull -> 722
                //   204: new java/util/StringTokenizer
                //   207: astore #12
                //   209: aload #12
                //   211: aload #6
                //   213: ldc ';'
                //   215: iconst_0
                //   216: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Z)V
                //   219: iload_3
                //   220: istore #5
                //   222: aload #12
                //   224: invokevirtual hasMoreTokens : ()Z
                //   227: ifeq -> 239
                //   230: aload #12
                //   232: invokevirtual nextToken : ()Ljava/lang/String;
                //   235: pop
                //   236: iload_3
                //   237: istore #5
                //   239: iload #5
                //   241: istore #4
                //   243: aload #12
                //   245: invokevirtual hasMoreTokens : ()Z
                //   248: ifeq -> 722
                //   251: aload #12
                //   253: invokevirtual nextToken : ()Ljava/lang/String;
                //   256: astore #15
                //   258: aload_0
                //   259: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   262: aload #15
                //   264: invokevirtual parseTableStatusIntoLocalAndReferencedColumns : (Ljava/lang/String;)Lcom/mysql/jdbc/DatabaseMetaData$LocalAndReferencedColumns;
                //   267: astore #13
                //   269: aload #13
                //   271: getfield localColumnsList : Ljava/util/List;
                //   274: invokeinterface iterator : ()Ljava/util/Iterator;
                //   279: astore #14
                //   281: aload #13
                //   283: getfield referencedColumnsList : Ljava/util/List;
                //   286: invokeinterface iterator : ()Ljava/util/Iterator;
                //   291: astore #16
                //   293: iconst_0
                //   294: istore_2
                //   295: aload #14
                //   297: invokeinterface hasNext : ()Z
                //   302: ifeq -> 239
                //   305: aload #14
                //   307: invokeinterface next : ()Ljava/lang/Object;
                //   312: checkcast java/lang/String
                //   315: aload_0
                //   316: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   319: getfield quotedId : Ljava/lang/String;
                //   322: invokestatic unQuoteIdentifier : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
                //   325: astore #8
                //   327: aload_0
                //   328: getfield val$foreignCatalog : Ljava/lang/String;
                //   331: astore #6
                //   333: aload #6
                //   335: ifnonnull -> 344
                //   338: aconst_null
                //   339: astore #6
                //   341: goto -> 355
                //   344: aload_0
                //   345: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   348: aload #6
                //   350: invokevirtual s2b : (Ljava/lang/String;)[B
                //   353: astore #6
                //   355: aload_0
                //   356: getfield val$foreignSchema : Ljava/lang/String;
                //   359: astore #7
                //   361: aload #7
                //   363: ifnonnull -> 372
                //   366: aconst_null
                //   367: astore #7
                //   369: goto -> 383
                //   372: aload_0
                //   373: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   376: aload #7
                //   378: invokevirtual s2b : (Ljava/lang/String;)[B
                //   381: astore #7
                //   383: aload_1
                //   384: ldc 'Name'
                //   386: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   391: astore #9
                //   393: aload #9
                //   395: aload #11
                //   397: invokevirtual compareTo : (Ljava/lang/String;)I
                //   400: ifeq -> 406
                //   403: goto -> 497
                //   406: aload_0
                //   407: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   410: aload #9
                //   412: invokevirtual s2b : (Ljava/lang/String;)[B
                //   415: astore #17
                //   417: aload_0
                //   418: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   421: aload #8
                //   423: invokevirtual s2b : (Ljava/lang/String;)[B
                //   426: astore #18
                //   428: aload_0
                //   429: getfield val$primaryCatalog : Ljava/lang/String;
                //   432: astore #8
                //   434: aload #8
                //   436: ifnonnull -> 445
                //   439: aconst_null
                //   440: astore #8
                //   442: goto -> 456
                //   445: aload_0
                //   446: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   449: aload #8
                //   451: invokevirtual s2b : (Ljava/lang/String;)[B
                //   454: astore #8
                //   456: aload_0
                //   457: getfield val$primarySchema : Ljava/lang/String;
                //   460: astore #9
                //   462: aload #9
                //   464: ifnonnull -> 473
                //   467: aconst_null
                //   468: astore #9
                //   470: goto -> 484
                //   473: aload_0
                //   474: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   477: aload #9
                //   479: invokevirtual s2b : (Ljava/lang/String;)[B
                //   482: astore #9
                //   484: aload #13
                //   486: getfield referencedTable : Ljava/lang/String;
                //   489: aload #10
                //   491: invokevirtual compareTo : (Ljava/lang/String;)I
                //   494: ifeq -> 503
                //   497: iconst_0
                //   498: istore #5
                //   500: goto -> 295
                //   503: aload_0
                //   504: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   507: aload #13
                //   509: getfield referencedTable : Ljava/lang/String;
                //   512: invokevirtual s2b : (Ljava/lang/String;)[B
                //   515: astore #20
                //   517: aload_0
                //   518: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   521: aload #16
                //   523: invokeinterface next : ()Ljava/lang/Object;
                //   528: checkcast java/lang/String
                //   531: aload_0
                //   532: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   535: getfield quotedId : Ljava/lang/String;
                //   538: invokestatic unQuoteIdentifier : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
                //   541: invokevirtual s2b : (Ljava/lang/String;)[B
                //   544: astore #19
                //   546: iload_2
                //   547: invokestatic toString : (I)Ljava/lang/String;
                //   550: invokevirtual getBytes : ()[B
                //   553: astore #22
                //   555: aload_0
                //   556: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   559: aload #15
                //   561: invokevirtual getForeignKeyActions : (Ljava/lang/String;)[I
                //   564: astore #23
                //   566: aload #23
                //   568: iconst_1
                //   569: iaload
                //   570: invokestatic toString : (I)Ljava/lang/String;
                //   573: invokevirtual getBytes : ()[B
                //   576: astore #21
                //   578: aload #23
                //   580: iconst_0
                //   581: iaload
                //   582: invokestatic toString : (I)Ljava/lang/String;
                //   585: invokevirtual getBytes : ()[B
                //   588: astore #25
                //   590: bipush #7
                //   592: invokestatic toString : (I)Ljava/lang/String;
                //   595: invokevirtual getBytes : ()[B
                //   598: astore #26
                //   600: aload_0
                //   601: getfield val$tuples : Ljava/util/ArrayList;
                //   604: astore #24
                //   606: new com/mysql/jdbc/ByteArrayRow
                //   609: astore #23
                //   611: aload_0
                //   612: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   615: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
                //   618: astore #27
                //   620: aload #23
                //   622: bipush #14
                //   624: anewarray [B
                //   627: dup
                //   628: iconst_0
                //   629: aload #8
                //   631: aastore
                //   632: dup
                //   633: iconst_1
                //   634: aload #9
                //   636: aastore
                //   637: dup
                //   638: iconst_2
                //   639: aload #20
                //   641: aastore
                //   642: dup
                //   643: iconst_3
                //   644: aload #19
                //   646: aastore
                //   647: dup
                //   648: iconst_4
                //   649: aload #6
                //   651: aastore
                //   652: dup
                //   653: iconst_5
                //   654: aload #7
                //   656: aastore
                //   657: dup
                //   658: bipush #6
                //   660: aload #17
                //   662: aastore
                //   663: dup
                //   664: bipush #7
                //   666: aload #18
                //   668: aastore
                //   669: dup
                //   670: bipush #8
                //   672: aload #22
                //   674: aastore
                //   675: dup
                //   676: bipush #9
                //   678: aload #21
                //   680: aastore
                //   681: dup
                //   682: bipush #10
                //   684: aload #25
                //   686: aastore
                //   687: dup
                //   688: bipush #11
                //   690: aconst_null
                //   691: aastore
                //   692: dup
                //   693: bipush #12
                //   695: aconst_null
                //   696: aastore
                //   697: dup
                //   698: bipush #13
                //   700: aload #26
                //   702: aastore
                //   703: aload #27
                //   705: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
                //   708: aload #24
                //   710: aload #23
                //   712: invokevirtual add : (Ljava/lang/Object;)Z
                //   715: pop
                //   716: iinc #2, 1
                //   719: goto -> 497
                //   722: iload #4
                //   724: istore_3
                //   725: goto -> 133
                //   728: aload_1
                //   729: ifnull -> 746
                //   732: aload_1
                //   733: invokeinterface close : ()V
                //   738: goto -> 746
                //   741: astore_1
                //   742: aload_1
                //   743: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
                //   746: return
                //   747: astore #6
                //   749: aload_1
                //   750: astore #7
                //   752: aload #6
                //   754: astore_1
                //   755: aload #7
                //   757: astore #6
                //   759: goto -> 763
                //   762: astore_1
                //   763: aload #6
                //   765: ifnull -> 785
                //   768: aload #6
                //   770: invokeinterface close : ()V
                //   775: goto -> 785
                //   778: astore #6
                //   780: aload #6
                //   782: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
                //   785: aload_1
                //   786: athrow
                // Exception table:
                //   from	to	target	type
                //   5	24	762	finally
                //   29	39	42	finally
                //   46	104	762	finally
                //   107	133	747	finally
                //   133	152	747	finally
                //   160	170	747	finally
                //   173	183	747	finally
                //   183	196	747	finally
                //   204	219	747	finally
                //   222	236	747	finally
                //   243	293	747	finally
                //   295	333	747	finally
                //   344	355	747	finally
                //   355	361	747	finally
                //   372	383	747	finally
                //   383	403	747	finally
                //   406	434	747	finally
                //   445	456	747	finally
                //   456	462	747	finally
                //   473	484	747	finally
                //   484	497	747	finally
                //   503	716	747	finally
                //   732	738	741	java/lang/Exception
                //   768	775	778	java/lang/Exception
              }
            };
          super(this, getCatalogIterator(paramString4), statement, paramString6, paramString3, paramString4, paramString5, paramString1, paramString2, arrayList);
          iterateBlock.doForAll();
        } finally {
          if (statement != null)
            statement.close(); 
        } 
      } 
      return buildResultSet(arrayOfField, arrayList);
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public int getDatabaseMajorVersion() throws SQLException {
    return this.conn.getServerMajorVersion();
  }
  
  public int getDatabaseMinorVersion() throws SQLException {
    return this.conn.getServerMinorVersion();
  }
  
  public String getDatabaseProductName() throws SQLException {
    return "MySQL";
  }
  
  public String getDatabaseProductVersion() throws SQLException {
    return this.conn.getServerVersion();
  }
  
  public int getDefaultTransactionIsolation() throws SQLException {
    return this.conn.supportsIsolationLevel() ? 2 : 0;
  }
  
  public int getDriverMajorVersion() {
    return NonRegisteringDriver.getMajorVersionInternal();
  }
  
  public int getDriverMinorVersion() {
    return NonRegisteringDriver.getMinorVersionInternal();
  }
  
  public String getDriverName() throws SQLException {
    return "MySQL Connector Java";
  }
  
  public String getDriverVersion() throws SQLException {
    return "mysql-connector-java-5.1.47 ( Revision: fe1903b1ecb4a96a917f7ed3190d80c049b1de29 )";
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  public void getExportKeyResults(String paramString1, String paramString2, String paramString3, List<ResultSetRow> paramList, String paramString4) throws SQLException {
    getResultsImpl(paramString1, paramString2, paramString3, paramList, paramString4, true);
  }
  
  public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
    if (paramString3 != null) {
      Field[] arrayOfField = createFkMetadataFields();
      ArrayList<ResultSetRow> arrayList = new ArrayList();
      if (this.conn.versionMeetsMinimum(3, 23, 0)) {
        Statement statement = this.conn.getMetadataSafeStatement();
        try {
          IterateBlock<String> iterateBlock = new IterateBlock<String>() {
              public final DatabaseMetaData this$0;
              
              public final ArrayList val$rows;
              
              public final Statement val$stmt;
              
              public final String val$table;
              
              public void forEach(String param1String) throws SQLException {
                // Byte code:
                //   0: aconst_null
                //   1: astore_2
                //   2: aload_2
                //   3: astore_3
                //   4: aload_0
                //   5: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   8: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
                //   11: iconst_3
                //   12: bipush #23
                //   14: bipush #50
                //   16: invokeinterface versionMeetsMinimum : (III)Z
                //   21: ifeq -> 39
                //   24: aload_2
                //   25: astore_3
                //   26: aload_0
                //   27: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   30: aload_1
                //   31: aconst_null
                //   32: invokevirtual extractForeignKeyFromCreateTable : (Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
                //   35: astore_2
                //   36: goto -> 107
                //   39: aload_2
                //   40: astore_3
                //   41: new java/lang/StringBuilder
                //   44: astore #4
                //   46: aload_2
                //   47: astore_3
                //   48: aload #4
                //   50: ldc 'SHOW TABLE STATUS FROM '
                //   52: invokespecial <init> : (Ljava/lang/String;)V
                //   55: aload_2
                //   56: astore_3
                //   57: aload_0
                //   58: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   61: astore #5
                //   63: aload_2
                //   64: astore_3
                //   65: aload #4
                //   67: aload_1
                //   68: aload #5
                //   70: getfield quotedId : Ljava/lang/String;
                //   73: aload #5
                //   75: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
                //   78: invokeinterface getPedantic : ()Z
                //   83: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
                //   86: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   89: pop
                //   90: aload_2
                //   91: astore_3
                //   92: aload_0
                //   93: getfield val$stmt : Ljava/sql/Statement;
                //   96: aload #4
                //   98: invokevirtual toString : ()Ljava/lang/String;
                //   101: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
                //   106: astore_2
                //   107: aload_2
                //   108: astore_3
                //   109: aload_0
                //   110: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   113: aload_0
                //   114: getfield val$table : Ljava/lang/String;
                //   117: invokevirtual getTableNameWithCase : (Ljava/lang/String;)Ljava/lang/String;
                //   120: astore #4
                //   122: aload_2
                //   123: astore_3
                //   124: aload_2
                //   125: invokeinterface next : ()Z
                //   130: ifeq -> 279
                //   133: aload_2
                //   134: astore_3
                //   135: aload_2
                //   136: ldc 'Type'
                //   138: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   143: astore #5
                //   145: aload #5
                //   147: ifnull -> 122
                //   150: aload_2
                //   151: astore_3
                //   152: aload #5
                //   154: ldc 'innodb'
                //   156: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
                //   159: ifne -> 174
                //   162: aload_2
                //   163: astore_3
                //   164: aload #5
                //   166: ldc 'SUPPORTS_FK'
                //   168: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
                //   171: ifeq -> 122
                //   174: aload_2
                //   175: astore_3
                //   176: aload_2
                //   177: ldc 'Comment'
                //   179: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   184: invokevirtual trim : ()Ljava/lang/String;
                //   187: astore #6
                //   189: aload #6
                //   191: ifnull -> 122
                //   194: aload_2
                //   195: astore_3
                //   196: new java/util/StringTokenizer
                //   199: astore #5
                //   201: aload_2
                //   202: astore_3
                //   203: aload #5
                //   205: aload #6
                //   207: ldc ';'
                //   209: iconst_0
                //   210: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Z)V
                //   213: aload_2
                //   214: astore_3
                //   215: aload #5
                //   217: invokevirtual hasMoreTokens : ()Z
                //   220: ifeq -> 122
                //   223: aload_2
                //   224: astore_3
                //   225: aload #5
                //   227: invokevirtual nextToken : ()Ljava/lang/String;
                //   230: pop
                //   231: aload_2
                //   232: astore_3
                //   233: aload #5
                //   235: invokevirtual hasMoreTokens : ()Z
                //   238: ifeq -> 122
                //   241: aload_2
                //   242: astore_3
                //   243: aload #5
                //   245: invokevirtual nextToken : ()Ljava/lang/String;
                //   248: astore #6
                //   250: aload_2
                //   251: astore_3
                //   252: aload_0
                //   253: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   256: aload_1
                //   257: aload #4
                //   259: aload #6
                //   261: aload_0
                //   262: getfield val$rows : Ljava/util/ArrayList;
                //   265: aload_2
                //   266: ldc 'Name'
                //   268: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   273: invokevirtual getExportKeyResults : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;)V
                //   276: goto -> 231
                //   279: aload_2
                //   280: ifnull -> 297
                //   283: aload_2
                //   284: invokeinterface close : ()V
                //   289: goto -> 297
                //   292: astore_1
                //   293: aload_1
                //   294: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
                //   297: return
                //   298: astore_1
                //   299: aload_3
                //   300: ifnull -> 317
                //   303: aload_3
                //   304: invokeinterface close : ()V
                //   309: goto -> 317
                //   312: astore_2
                //   313: aload_2
                //   314: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
                //   317: aload_1
                //   318: athrow
                // Exception table:
                //   from	to	target	type
                //   4	24	298	finally
                //   26	36	298	finally
                //   41	46	298	finally
                //   48	55	298	finally
                //   57	63	298	finally
                //   65	90	298	finally
                //   92	107	298	finally
                //   109	122	298	finally
                //   124	133	298	finally
                //   135	145	298	finally
                //   152	162	298	finally
                //   164	174	298	finally
                //   176	189	298	finally
                //   196	201	298	finally
                //   203	213	298	finally
                //   215	223	298	finally
                //   225	231	298	finally
                //   233	241	298	finally
                //   243	250	298	finally
                //   252	276	298	finally
                //   283	289	292	java/sql/SQLException
                //   303	309	312	java/sql/SQLException
              }
            };
          super(this, getCatalogIterator(paramString1), statement, paramString3, arrayList);
          iterateBlock.doForAll();
        } finally {
          if (statement != null)
            statement.close(); 
        } 
      } 
      return buildResultSet(arrayOfField, arrayList);
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public String getExtraNameCharacters() throws SQLException {
    return "#@";
  }
  
  public int[] getForeignKeyActions(String paramString) {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = 3;
    arrayOfInt[1] = 3;
    int i = paramString.lastIndexOf(")");
    if (i != paramString.length() - 1) {
      paramString = paramString.substring(i + 1).trim().toUpperCase(Locale.ENGLISH);
      arrayOfInt[0] = getCascadeDeleteOption(paramString);
      arrayOfInt[1] = getCascadeUpdateOption(paramString);
    } 
    return arrayOfInt;
  }
  
  public String getFullyQualifiedName(String paramString1, String paramString2) {
    String str = paramString1;
    if (paramString1 == null)
      str = ""; 
    StringBuilder stringBuilder = new StringBuilder(StringUtils.quoteIdentifier(str, this.quotedId, this.conn.getPedantic()));
    stringBuilder.append('.');
    stringBuilder.append(StringUtils.quoteIdentifier(paramString2, this.quotedId, this.conn.getPedantic()));
    return stringBuilder.toString();
  }
  
  public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    return getProcedureOrFunctionColumns(createFunctionColumnsFields(), paramString1, paramString2, paramString3, paramString4, false, true);
  }
  
  public ResultSet getFunctions(String paramString1, String paramString2, String paramString3) throws SQLException {
    return getProceduresAndOrFunctions(new Field[] { new Field("", "FUNCTION_CAT", 1, 255), new Field("", "FUNCTION_SCHEM", 1, 255), new Field("", "FUNCTION_NAME", 1, 255), new Field("", "REMARKS", 1, 255), new Field("", "FUNCTION_TYPE", 5, 6), new Field("", "SPECIFIC_NAME", 1, 255) }paramString1, paramString2, paramString3, false, true);
  }
  
  public String getIdentifierQuoteString() throws SQLException {
    if (this.conn.supportsQuotedIdentifiers()) {
      String str;
      if (this.conn.useAnsiQuotedIdentifiers()) {
        str = "\"";
      } else {
        str = "`";
      } 
      return str;
    } 
    return " ";
  }
  
  public void getImportKeyResults(String paramString1, String paramString2, String paramString3, List<ResultSetRow> paramList) throws SQLException {
    getResultsImpl(paramString1, paramString2, paramString3, paramList, null, false);
  }
  
  public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
    if (paramString3 != null) {
      Field[] arrayOfField = createFkMetadataFields();
      ArrayList<ResultSetRow> arrayList = new ArrayList();
      if (this.conn.versionMeetsMinimum(3, 23, 0)) {
        Statement statement = this.conn.getMetadataSafeStatement();
        try {
          IterateBlock<String> iterateBlock = new IterateBlock<String>() {
              public final DatabaseMetaData this$0;
              
              public final ArrayList val$rows;
              
              public final Statement val$stmt;
              
              public final String val$table;
              
              public void forEach(String param1String) throws SQLException {
                // Byte code:
                //   0: aconst_null
                //   1: astore_2
                //   2: aload_2
                //   3: astore_3
                //   4: aload_0
                //   5: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   8: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
                //   11: iconst_3
                //   12: bipush #23
                //   14: bipush #50
                //   16: invokeinterface versionMeetsMinimum : (III)Z
                //   21: ifeq -> 42
                //   24: aload_2
                //   25: astore_3
                //   26: aload_0
                //   27: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   30: aload_1
                //   31: aload_0
                //   32: getfield val$table : Ljava/lang/String;
                //   35: invokevirtual extractForeignKeyFromCreateTable : (Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
                //   38: astore_2
                //   39: goto -> 148
                //   42: aload_2
                //   43: astore_3
                //   44: new java/lang/StringBuilder
                //   47: astore #5
                //   49: aload_2
                //   50: astore_3
                //   51: aload #5
                //   53: ldc 'SHOW TABLE STATUS '
                //   55: invokespecial <init> : (Ljava/lang/String;)V
                //   58: aload_2
                //   59: astore_3
                //   60: aload #5
                //   62: ldc ' FROM '
                //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   67: pop
                //   68: aload_2
                //   69: astore_3
                //   70: aload_0
                //   71: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   74: astore #4
                //   76: aload_2
                //   77: astore_3
                //   78: aload #5
                //   80: aload_1
                //   81: aload #4
                //   83: getfield quotedId : Ljava/lang/String;
                //   86: aload #4
                //   88: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
                //   91: invokeinterface getPedantic : ()Z
                //   96: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
                //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   102: pop
                //   103: aload_2
                //   104: astore_3
                //   105: aload #5
                //   107: ldc ' LIKE '
                //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   112: pop
                //   113: aload_2
                //   114: astore_3
                //   115: aload #5
                //   117: aload_0
                //   118: getfield val$table : Ljava/lang/String;
                //   121: ldc '''
                //   123: iconst_1
                //   124: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
                //   127: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   130: pop
                //   131: aload_2
                //   132: astore_3
                //   133: aload_0
                //   134: getfield val$stmt : Ljava/sql/Statement;
                //   137: aload #5
                //   139: invokevirtual toString : ()Ljava/lang/String;
                //   142: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
                //   147: astore_2
                //   148: aload_2
                //   149: astore_3
                //   150: aload_2
                //   151: invokeinterface next : ()Z
                //   156: ifeq -> 299
                //   159: aload_2
                //   160: astore_3
                //   161: aload_2
                //   162: ldc 'Type'
                //   164: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   169: astore #4
                //   171: aload #4
                //   173: ifnull -> 148
                //   176: aload_2
                //   177: astore_3
                //   178: aload #4
                //   180: ldc 'innodb'
                //   182: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
                //   185: ifne -> 200
                //   188: aload_2
                //   189: astore_3
                //   190: aload #4
                //   192: ldc 'SUPPORTS_FK'
                //   194: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
                //   197: ifeq -> 148
                //   200: aload_2
                //   201: astore_3
                //   202: aload_2
                //   203: ldc 'Comment'
                //   205: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
                //   210: invokevirtual trim : ()Ljava/lang/String;
                //   213: astore #5
                //   215: aload #5
                //   217: ifnull -> 148
                //   220: aload_2
                //   221: astore_3
                //   222: new java/util/StringTokenizer
                //   225: astore #4
                //   227: aload_2
                //   228: astore_3
                //   229: aload #4
                //   231: aload #5
                //   233: ldc ';'
                //   235: iconst_0
                //   236: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Z)V
                //   239: aload_2
                //   240: astore_3
                //   241: aload #4
                //   243: invokevirtual hasMoreTokens : ()Z
                //   246: ifeq -> 148
                //   249: aload_2
                //   250: astore_3
                //   251: aload #4
                //   253: invokevirtual nextToken : ()Ljava/lang/String;
                //   256: pop
                //   257: aload_2
                //   258: astore_3
                //   259: aload #4
                //   261: invokevirtual hasMoreTokens : ()Z
                //   264: ifeq -> 148
                //   267: aload_2
                //   268: astore_3
                //   269: aload #4
                //   271: invokevirtual nextToken : ()Ljava/lang/String;
                //   274: astore #5
                //   276: aload_2
                //   277: astore_3
                //   278: aload_0
                //   279: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
                //   282: aload_1
                //   283: aload_0
                //   284: getfield val$table : Ljava/lang/String;
                //   287: aload #5
                //   289: aload_0
                //   290: getfield val$rows : Ljava/util/ArrayList;
                //   293: invokevirtual getImportKeyResults : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V
                //   296: goto -> 257
                //   299: aload_2
                //   300: ifnull -> 317
                //   303: aload_2
                //   304: invokeinterface close : ()V
                //   309: goto -> 317
                //   312: astore_1
                //   313: aload_1
                //   314: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
                //   317: return
                //   318: astore_1
                //   319: aload_3
                //   320: ifnull -> 337
                //   323: aload_3
                //   324: invokeinterface close : ()V
                //   329: goto -> 337
                //   332: astore_2
                //   333: aload_2
                //   334: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
                //   337: aload_1
                //   338: athrow
                // Exception table:
                //   from	to	target	type
                //   4	24	318	finally
                //   26	39	318	finally
                //   44	49	318	finally
                //   51	58	318	finally
                //   60	68	318	finally
                //   70	76	318	finally
                //   78	103	318	finally
                //   105	113	318	finally
                //   115	131	318	finally
                //   133	148	318	finally
                //   150	159	318	finally
                //   161	171	318	finally
                //   178	188	318	finally
                //   190	200	318	finally
                //   202	215	318	finally
                //   222	227	318	finally
                //   229	239	318	finally
                //   241	249	318	finally
                //   251	257	318	finally
                //   259	267	318	finally
                //   269	276	318	finally
                //   278	296	318	finally
                //   303	309	312	java/sql/SQLException
                //   323	329	332	java/sql/SQLException
              }
            };
          super(this, getCatalogIterator(paramString1), paramString3, statement, arrayList);
          iterateBlock.doForAll();
        } finally {
          if (statement != null)
            statement.close(); 
        } 
      } 
      return buildResultSet(arrayOfField, arrayList);
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    Field[] arrayOfField = createIndexInfoFields();
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    Statement statement = this.conn.getMetadataSafeStatement();
    try {
      IterateBlock<String> iterateBlock = new IterateBlock<String>() {
          public final DatabaseMetaData this$0;
          
          public final SortedMap val$sortedRows;
          
          public final Statement val$stmt;
          
          public final String val$table;
          
          public final boolean val$unique;
          
          public void forEach(String param1String) throws SQLException {
            ResultSet resultSet = null;
            try {
              StringBuilder stringBuilder = new StringBuilder();
              this("SHOW INDEX FROM ");
              String str = table;
              DatabaseMetaData databaseMetaData = DatabaseMetaData.this;
              stringBuilder.append(StringUtils.quoteIdentifier(str, databaseMetaData.quotedId, databaseMetaData.conn.getPedantic()));
              stringBuilder.append(" FROM ");
              databaseMetaData = DatabaseMetaData.this;
              stringBuilder.append(StringUtils.quoteIdentifier(param1String, databaseMetaData.quotedId, databaseMetaData.conn.getPedantic()));
              try {
                ResultSet resultSet1 = stmt.executeQuery(stringBuilder.toString());
                resultSet = resultSet1;
              } catch (SQLException sQLException) {
                int i = sQLException.getErrorCode();
                if ("42S02".equals(sQLException.getSQLState()) || i == 1146) {
                  resultSet = null;
                } else {
                  throw sQLException;
                } 
              } 
              while (true) {
                if (resultSet != null)
                  try {
                    if (resultSet.next()) {
                      boolean bool1;
                      boolean bool2;
                      byte[] arrayOfByte2;
                      DatabaseMetaData databaseMetaData1;
                      String str1;
                      byte[][] arrayOfByte = new byte[14][];
                      if (param1String == null) {
                        arrayOfByte2 = new byte[0];
                      } else {
                        arrayOfByte2 = DatabaseMetaData.this.s2b(param1String);
                      } 
                      arrayOfByte[0] = arrayOfByte2;
                      arrayOfByte[1] = null;
                      arrayOfByte[2] = resultSet.getBytes("Table");
                      if (resultSet.getInt("Non_unique") == 0) {
                        bool1 = true;
                      } else {
                        bool1 = false;
                      } 
                      if (!bool1) {
                        databaseMetaData1 = DatabaseMetaData.this;
                        str1 = "true";
                      } else {
                        databaseMetaData1 = DatabaseMetaData.this;
                        str1 = "false";
                      } 
                      byte[] arrayOfByte1 = databaseMetaData1.s2b(str1);
                      arrayOfByte[3] = arrayOfByte1;
                      arrayOfByte[4] = new byte[0];
                      arrayOfByte[5] = resultSet.getBytes("Key_name");
                      arrayOfByte[6] = Integer.toString(3).getBytes();
                      arrayOfByte[7] = resultSet.getBytes("Seq_in_index");
                      arrayOfByte[8] = resultSet.getBytes("Column_name");
                      arrayOfByte[9] = resultSet.getBytes("Collation");
                      long l2 = resultSet.getLong("Cardinality");
                      long l1 = l2;
                      if (!Util.isJdbc42()) {
                        l1 = l2;
                        if (l2 > 2147483647L)
                          l1 = 2147483647L; 
                      } 
                      arrayOfByte[10] = DatabaseMetaData.this.s2b(String.valueOf(l1));
                      arrayOfByte[11] = DatabaseMetaData.this.s2b("0");
                      arrayOfByte[12] = null;
                      DatabaseMetaData.IndexMetaDataKey indexMetaDataKey = new DatabaseMetaData.IndexMetaDataKey();
                      DatabaseMetaData databaseMetaData2 = DatabaseMetaData.this;
                      if (!bool1) {
                        bool2 = true;
                      } else {
                        bool2 = false;
                      } 
                      this(bool2, (short)3, resultSet.getString("Key_name").toLowerCase(), resultSet.getShort("Seq_in_index"));
                      if (unique) {
                        if (bool1) {
                          SortedMap<DatabaseMetaData.IndexMetaDataKey, ByteArrayRow> sortedMap1 = sortedRows;
                          ByteArrayRow byteArrayRow1 = new ByteArrayRow();
                          this(arrayOfByte, DatabaseMetaData.this.getExceptionInterceptor());
                          sortedMap1.put(indexMetaDataKey, byteArrayRow1);
                        } 
                        continue;
                      } 
                      SortedMap<DatabaseMetaData.IndexMetaDataKey, ByteArrayRow> sortedMap = sortedRows;
                      ByteArrayRow byteArrayRow = new ByteArrayRow();
                      this(arrayOfByte, DatabaseMetaData.this.getExceptionInterceptor());
                      sortedMap.put(indexMetaDataKey, byteArrayRow);
                      continue;
                    } 
                  } finally {} 
                return;
              } 
            } finally {
              if (resultSet != null)
                try {
                  resultSet.close();
                } catch (Exception exception) {} 
            } 
          }
        };
      super(this, getCatalogIterator(paramString1), paramString3, statement, paramBoolean1, treeMap);
      iterateBlock.doForAll();
      Iterator iterator = treeMap.values().iterator();
      while (iterator.hasNext())
        arrayList.add(iterator.next()); 
      return buildResultSet(arrayOfField, arrayList);
    } finally {
      if (statement != null)
        statement.close(); 
    } 
  }
  
  public int getJDBC4FunctionNoTableConstant() {
    return 0;
  }
  
  public int getJDBCMajorVersion() throws SQLException {
    return 4;
  }
  
  public int getJDBCMinorVersion() throws SQLException {
    return 0;
  }
  
  public int getMaxBinaryLiteralLength() throws SQLException {
    return 16777208;
  }
  
  public int getMaxCatalogNameLength() throws SQLException {
    return 32;
  }
  
  public int getMaxCharLiteralLength() throws SQLException {
    return 16777208;
  }
  
  public int getMaxColumnNameLength() throws SQLException {
    return 64;
  }
  
  public int getMaxColumnsInGroupBy() throws SQLException {
    return 64;
  }
  
  public int getMaxColumnsInIndex() throws SQLException {
    return 16;
  }
  
  public int getMaxColumnsInOrderBy() throws SQLException {
    return 64;
  }
  
  public int getMaxColumnsInSelect() throws SQLException {
    return 256;
  }
  
  public int getMaxColumnsInTable() throws SQLException {
    return 512;
  }
  
  public int getMaxConnections() throws SQLException {
    return 0;
  }
  
  public int getMaxCursorNameLength() throws SQLException {
    return 64;
  }
  
  public int getMaxIndexLength() throws SQLException {
    return 256;
  }
  
  public int getMaxProcedureNameLength() throws SQLException {
    return 0;
  }
  
  public int getMaxRowSize() throws SQLException {
    return 2147483639;
  }
  
  public int getMaxSchemaNameLength() throws SQLException {
    return 0;
  }
  
  public int getMaxStatementLength() throws SQLException {
    return MysqlIO.getMaxBuf() - 4;
  }
  
  public int getMaxStatements() throws SQLException {
    return 0;
  }
  
  public int getMaxTableNameLength() throws SQLException {
    return 64;
  }
  
  public int getMaxTablesInSelect() throws SQLException {
    return 256;
  }
  
  public int getMaxUserNameLength() throws SQLException {
    return 16;
  }
  
  public String getNumericFunctions() throws SQLException {
    return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
  }
  
  public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
    Field field4 = new Field("", "TABLE_CAT", 1, 255);
    Field field6 = new Field("", "TABLE_SCHEM", 1, 0);
    Field field2 = new Field("", "TABLE_NAME", 1, 255);
    Field field3 = new Field("", "COLUMN_NAME", 1, 32);
    Field field1 = new Field("", "KEY_SEQ", 5, 5);
    Field field5 = new Field("", "PK_NAME", 1, 32);
    if (paramString3 != null) {
      ArrayList<ResultSetRow> arrayList = new ArrayList();
      Statement statement = this.conn.getMetadataSafeStatement();
      try {
        IterateBlock<String> iterateBlock = new IterateBlock<String>() {
            public final DatabaseMetaData this$0;
            
            public final ArrayList val$rows;
            
            public final Statement val$stmt;
            
            public final String val$table;
            
            public void forEach(String param1String) throws SQLException {
              ResultSet resultSet;
              byte[] arrayOfByte = null;
              try {
                StringBuilder stringBuilder = new StringBuilder();
                this("SHOW KEYS FROM ");
                String str = table;
                DatabaseMetaData databaseMetaData = DatabaseMetaData.this;
                stringBuilder.append(StringUtils.quoteIdentifier(str, databaseMetaData.quotedId, databaseMetaData.conn.getPedantic()));
                stringBuilder.append(" FROM ");
                databaseMetaData = DatabaseMetaData.this;
                stringBuilder.append(StringUtils.quoteIdentifier(param1String, databaseMetaData.quotedId, databaseMetaData.conn.getPedantic()));
                ResultSet resultSet1 = stmt.executeQuery(stringBuilder.toString());
                try {
                  TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
                  this();
                  while (resultSet1.next()) {
                    str = resultSet1.getString("Key_name");
                    if (str != null && (str.equalsIgnoreCase("PRIMARY") || str.equalsIgnoreCase("PRI"))) {
                      if (param1String == null) {
                        arrayOfByte = new byte[0];
                      } else {
                        arrayOfByte = DatabaseMetaData.this.s2b(param1String);
                      } 
                      byte[] arrayOfByte1 = DatabaseMetaData.this.s2b(table);
                      String str1 = resultSet1.getString("Column_name");
                      treeMap.put(str1, new byte[][] { arrayOfByte, null, arrayOfByte1, this.this$0.s2b(str1), this.this$0.s2b(resultSet1.getString("Seq_in_index")), this.this$0.s2b(str) });
                    } 
                  } 
                  Iterator<byte[][]> iterator = treeMap.values().iterator();
                  while (iterator.hasNext()) {
                    ArrayList<ByteArrayRow> arrayList = rows;
                    ByteArrayRow byteArrayRow = new ByteArrayRow();
                    this(iterator.next(), DatabaseMetaData.this.getExceptionInterceptor());
                    arrayList.add(byteArrayRow);
                  } 
                  return;
                } finally {
                  param1String = null;
                } 
              } finally {}
              if (resultSet != null)
                try {
                  resultSet.close();
                } catch (Exception exception) {} 
              throw param1String;
            }
          };
        super(this, getCatalogIterator(paramString1), paramString3, statement, arrayList);
        iterateBlock.doForAll();
        return buildResultSet(new Field[] { field4, field6, field2, field3, field1, field5 }, arrayList);
      } finally {
        if (statement != null)
          statement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    return getProcedureOrFunctionColumns(createProcedureColumnsFields(), paramString1, paramString2, paramString3, paramString4, true, true);
  }
  
  public ResultSet getProcedureOrFunctionColumns(Field[] paramArrayOfField, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload #4
    //   2: astore #9
    //   4: new java/util/ArrayList
    //   7: dup
    //   8: invokespecial <init> : ()V
    //   11: astore #12
    //   13: aload_0
    //   14: invokevirtual supportsStoredProcedures : ()Z
    //   17: ifeq -> 317
    //   20: aconst_null
    //   21: astore #10
    //   23: aconst_null
    //   24: astore #11
    //   26: aload #9
    //   28: ifnull -> 58
    //   31: aload #9
    //   33: ldc_w '%'
    //   36: invokevirtual equals : (Ljava/lang/Object;)Z
    //   39: ifne -> 58
    //   42: aload #4
    //   44: invokestatic sanitizeProcOrFuncName : (Ljava/lang/String;)Ljava/lang/String;
    //   47: astore #4
    //   49: goto -> 61
    //   52: astore_1
    //   53: aconst_null
    //   54: astore_2
    //   55: goto -> 289
    //   58: aconst_null
    //   59: astore #4
    //   61: aload #4
    //   63: ifnonnull -> 73
    //   66: aload #9
    //   68: astore #4
    //   70: goto -> 134
    //   73: aload #4
    //   75: aload_2
    //   76: aload_0
    //   77: getfield quotedId : Ljava/lang/String;
    //   80: aload_0
    //   81: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   84: invokeinterface isNoBackslashEscapesSet : ()Z
    //   89: invokestatic splitDBdotName : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
    //   92: astore #9
    //   94: aload #9
    //   96: invokeinterface size : ()I
    //   101: iconst_2
    //   102: if_icmpne -> 134
    //   105: aload #9
    //   107: iconst_0
    //   108: invokeinterface get : (I)Ljava/lang/Object;
    //   113: checkcast java/lang/String
    //   116: astore #4
    //   118: aload #9
    //   120: iconst_1
    //   121: invokeinterface get : (I)Ljava/lang/Object;
    //   126: checkcast java/lang/String
    //   129: astore #4
    //   131: goto -> 70
    //   134: aload_0
    //   135: aload_0
    //   136: invokevirtual createFieldMetadataForGetProcedures : ()[Lcom/mysql/jdbc/Field;
    //   139: aload_2
    //   140: aload_3
    //   141: aload #4
    //   143: iload #6
    //   145: iload #7
    //   147: invokevirtual getProceduresAndOrFunctions : ([Lcom/mysql/jdbc/Field;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZ)Ljava/sql/ResultSet;
    //   150: astore #4
    //   152: iconst_0
    //   153: istore #8
    //   155: aload #4
    //   157: invokeinterface next : ()Z
    //   162: ifeq -> 241
    //   165: new com/mysql/jdbc/DatabaseMetaData$ComparableWrapper
    //   168: astore #13
    //   170: aload_0
    //   171: aload #4
    //   173: iconst_1
    //   174: invokeinterface getString : (I)Ljava/lang/String;
    //   179: aload #4
    //   181: iconst_3
    //   182: invokeinterface getString : (I)Ljava/lang/String;
    //   187: invokevirtual getFullyQualifiedName : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   190: astore #9
    //   192: aload #4
    //   194: bipush #8
    //   196: invokeinterface getShort : (I)S
    //   201: iconst_1
    //   202: if_icmpne -> 212
    //   205: getstatic com/mysql/jdbc/DatabaseMetaData$ProcedureType.PROCEDURE : Lcom/mysql/jdbc/DatabaseMetaData$ProcedureType;
    //   208: astore_3
    //   209: goto -> 216
    //   212: getstatic com/mysql/jdbc/DatabaseMetaData$ProcedureType.FUNCTION : Lcom/mysql/jdbc/DatabaseMetaData$ProcedureType;
    //   215: astore_3
    //   216: aload #13
    //   218: aload_0
    //   219: aload #9
    //   221: aload_3
    //   222: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/Object;Ljava/lang/Object;)V
    //   225: aload #12
    //   227: aload #13
    //   229: invokeinterface add : (Ljava/lang/Object;)Z
    //   234: pop
    //   235: iconst_1
    //   236: istore #8
    //   238: goto -> 155
    //   241: iload #8
    //   243: ifne -> 249
    //   246: goto -> 254
    //   249: aload #12
    //   251: invokestatic sort : (Ljava/util/List;)V
    //   254: aload #11
    //   256: astore_3
    //   257: aload #4
    //   259: ifnull -> 276
    //   262: aload #4
    //   264: invokeinterface close : ()V
    //   269: aload #11
    //   271: astore_3
    //   272: goto -> 276
    //   275: astore_3
    //   276: aload_3
    //   277: ifnonnull -> 283
    //   280: goto -> 317
    //   283: aload_3
    //   284: athrow
    //   285: astore_1
    //   286: aload #4
    //   288: astore_2
    //   289: aload #10
    //   291: astore_3
    //   292: aload_2
    //   293: ifnull -> 309
    //   296: aload_2
    //   297: invokeinterface close : ()V
    //   302: aload #10
    //   304: astore_3
    //   305: goto -> 309
    //   308: astore_3
    //   309: aload_3
    //   310: ifnull -> 315
    //   313: aload_3
    //   314: athrow
    //   315: aload_1
    //   316: athrow
    //   317: new java/util/ArrayList
    //   320: dup
    //   321: invokespecial <init> : ()V
    //   324: astore #4
    //   326: aload #12
    //   328: invokeinterface iterator : ()Ljava/util/Iterator;
    //   333: astore #9
    //   335: aload #9
    //   337: invokeinterface hasNext : ()Z
    //   342: ifeq -> 500
    //   345: aload #9
    //   347: invokeinterface next : ()Ljava/lang/Object;
    //   352: checkcast com/mysql/jdbc/DatabaseMetaData$ComparableWrapper
    //   355: astore_3
    //   356: aload_3
    //   357: invokevirtual getKey : ()Ljava/lang/Object;
    //   360: checkcast java/lang/String
    //   363: astore #10
    //   365: aload_3
    //   366: invokevirtual getValue : ()Ljava/lang/Object;
    //   369: checkcast com/mysql/jdbc/DatabaseMetaData$ProcedureType
    //   372: astore #11
    //   374: ldc_w ' '
    //   377: aload_0
    //   378: getfield quotedId : Ljava/lang/String;
    //   381: invokevirtual equals : (Ljava/lang/Object;)Z
    //   384: ifne -> 435
    //   387: aload_0
    //   388: getfield quotedId : Ljava/lang/String;
    //   391: astore #12
    //   393: aload_0
    //   394: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   397: invokeinterface isNoBackslashEscapesSet : ()Z
    //   402: ifeq -> 412
    //   405: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__MRK_COM_WS : Ljava/util/Set;
    //   408: astore_3
    //   409: goto -> 416
    //   412: getstatic com/mysql/jdbc/StringUtils.SEARCH_MODE__ALL : Ljava/util/Set;
    //   415: astore_3
    //   416: iconst_0
    //   417: aload #10
    //   419: ldc_w '.'
    //   422: aload #12
    //   424: aload #12
    //   426: aload_3
    //   427: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Set;)I
    //   430: istore #8
    //   432: goto -> 445
    //   435: aload #10
    //   437: ldc_w '.'
    //   440: invokevirtual indexOf : (Ljava/lang/String;)I
    //   443: istore #8
    //   445: iload #8
    //   447: ifle -> 466
    //   450: aload #10
    //   452: iconst_0
    //   453: iload #8
    //   455: invokevirtual substring : (II)Ljava/lang/String;
    //   458: aload_0
    //   459: getfield quotedId : Ljava/lang/String;
    //   462: invokestatic unQuoteIdentifier : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   465: astore_2
    //   466: aload_1
    //   467: arraylength
    //   468: bipush #17
    //   470: if_icmpne -> 479
    //   473: iconst_1
    //   474: istore #6
    //   476: goto -> 482
    //   479: iconst_0
    //   480: istore #6
    //   482: aload_0
    //   483: aload_2
    //   484: aload #10
    //   486: aload #11
    //   488: aload #5
    //   490: aload #4
    //   492: iload #6
    //   494: invokespecial getCallStmtParameterTypes : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/DatabaseMetaData$ProcedureType;Ljava/lang/String;Ljava/util/List;Z)V
    //   497: goto -> 335
    //   500: aload_0
    //   501: aload_1
    //   502: aload #4
    //   504: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
    //   507: areturn
    // Exception table:
    //   from	to	target	type
    //   31	49	52	finally
    //   73	131	52	finally
    //   134	152	52	finally
    //   155	209	285	finally
    //   212	216	285	finally
    //   216	235	285	finally
    //   249	254	285	finally
    //   262	269	275	java/sql/SQLException
    //   296	302	308	java/sql/SQLException
  }
  
  public String getProcedureTerm() throws SQLException {
    return "PROCEDURE";
  }
  
  public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
    return getProceduresAndOrFunctions(createFieldMetadataForGetProcedures(), paramString1, paramString2, paramString3, true, true);
  }
  
  public ResultSet getProceduresAndOrFunctions(Field[] paramArrayOfField, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload #4
    //   2: ifnull -> 22
    //   5: aload #4
    //   7: astore_3
    //   8: aload #4
    //   10: invokevirtual length : ()I
    //   13: ifne -> 19
    //   16: goto -> 22
    //   19: goto -> 41
    //   22: aload_0
    //   23: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   26: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   31: ifeq -> 141
    //   34: ldc_w '%'
    //   37: astore_3
    //   38: goto -> 19
    //   41: new java/util/ArrayList
    //   44: dup
    //   45: invokespecial <init> : ()V
    //   48: astore #4
    //   50: aload_0
    //   51: invokevirtual supportsStoredProcedures : ()Z
    //   54: ifeq -> 133
    //   57: new java/util/ArrayList
    //   60: dup
    //   61: invokespecial <init> : ()V
    //   64: astore #7
    //   66: new com/mysql/jdbc/DatabaseMetaData$8
    //   69: dup
    //   70: aload_0
    //   71: aload_0
    //   72: aload_2
    //   73: invokevirtual getCatalogIterator : (Ljava/lang/String;)Lcom/mysql/jdbc/DatabaseMetaData$IteratorWithCleanup;
    //   76: iload #5
    //   78: iload #6
    //   80: aload_3
    //   81: aload #7
    //   83: aload_1
    //   84: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Lcom/mysql/jdbc/DatabaseMetaData$IteratorWithCleanup;ZZLjava/lang/String;Ljava/util/List;[Lcom/mysql/jdbc/Field;)V
    //   87: invokevirtual doForAll : ()V
    //   90: aload #7
    //   92: invokestatic sort : (Ljava/util/List;)V
    //   95: aload #7
    //   97: invokeinterface iterator : ()Ljava/util/Iterator;
    //   102: astore_2
    //   103: aload_2
    //   104: invokeinterface hasNext : ()Z
    //   109: ifeq -> 133
    //   112: aload #4
    //   114: aload_2
    //   115: invokeinterface next : ()Ljava/lang/Object;
    //   120: checkcast com/mysql/jdbc/DatabaseMetaData$ComparableWrapper
    //   123: invokevirtual getValue : ()Ljava/lang/Object;
    //   126: invokevirtual add : (Ljava/lang/Object;)Z
    //   129: pop
    //   130: goto -> 103
    //   133: aload_0
    //   134: aload_1
    //   135: aload #4
    //   137: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
    //   140: areturn
    //   141: ldc_w 'Procedure name pattern can not be NULL or empty.'
    //   144: ldc_w 'S1009'
    //   147: aload_0
    //   148: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   151: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   154: athrow
  }
  
  public ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    Field field11 = new Field("", "TABLE_CAT", 12, 512);
    Field field7 = new Field("", "TABLE_SCHEM", 12, 512);
    Field field4 = new Field("", "TABLE_NAME", 12, 512);
    Field field8 = new Field("", "COLUMN_NAME", 12, 512);
    Field field5 = new Field("", "DATA_TYPE", 4, 12);
    Field field1 = new Field("", "COLUMN_SIZE", 4, 12);
    Field field6 = new Field("", "DECIMAL_DIGITS", 4, 12);
    Field field12 = new Field("", "NUM_PREC_RADIX", 4, 12);
    Field field3 = new Field("", "COLUMN_USAGE", 12, 512);
    Field field9 = new Field("", "REMARKS", 12, 512);
    Field field2 = new Field("", "CHAR_OCTET_LENGTH", 4, 12);
    Field field10 = new Field("", "IS_NULLABLE", 12, 512);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { 
          field11, field7, field4, field8, field5, field1, field6, field12, field3, field9, 
          field2, field10 }, arrayList);
  }
  
  public int getResultSetHoldability() throws SQLException {
    return 1;
  }
  
  public String getSQLKeywords() throws SQLException {
    // Byte code:
    //   0: getstatic com/mysql/jdbc/DatabaseMetaData.mysqlKeywords : Ljava/lang/String;
    //   3: ifnull -> 10
    //   6: getstatic com/mysql/jdbc/DatabaseMetaData.mysqlKeywords : Ljava/lang/String;
    //   9: areturn
    //   10: ldc com/mysql/jdbc/DatabaseMetaData
    //   12: monitorenter
    //   13: getstatic com/mysql/jdbc/DatabaseMetaData.mysqlKeywords : Ljava/lang/String;
    //   16: ifnull -> 28
    //   19: getstatic com/mysql/jdbc/DatabaseMetaData.mysqlKeywords : Ljava/lang/String;
    //   22: astore_1
    //   23: ldc com/mysql/jdbc/DatabaseMetaData
    //   25: monitorexit
    //   26: aload_1
    //   27: areturn
    //   28: new java/util/TreeSet
    //   31: astore_3
    //   32: aload_3
    //   33: invokespecial <init> : ()V
    //   36: new java/lang/StringBuilder
    //   39: astore_2
    //   40: aload_2
    //   41: invokespecial <init> : ()V
    //   44: aload_3
    //   45: getstatic com/mysql/jdbc/DatabaseMetaData.MYSQL_KEYWORDS : [Ljava/lang/String;
    //   48: invokestatic addAll : (Ljava/util/Collection;[Ljava/lang/Object;)Z
    //   51: pop
    //   52: invokestatic isJdbc4 : ()Z
    //   55: ifeq -> 65
    //   58: getstatic com/mysql/jdbc/DatabaseMetaData.SQL2003_KEYWORDS : [Ljava/lang/String;
    //   61: astore_1
    //   62: goto -> 69
    //   65: getstatic com/mysql/jdbc/DatabaseMetaData.SQL92_KEYWORDS : [Ljava/lang/String;
    //   68: astore_1
    //   69: aload_3
    //   70: aload_1
    //   71: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   74: invokeinterface removeAll : (Ljava/util/Collection;)Z
    //   79: pop
    //   80: aload_3
    //   81: invokeinterface iterator : ()Ljava/util/Iterator;
    //   86: astore_1
    //   87: aload_1
    //   88: invokeinterface hasNext : ()Z
    //   93: ifeq -> 123
    //   96: aload_1
    //   97: invokeinterface next : ()Ljava/lang/Object;
    //   102: checkcast java/lang/String
    //   105: astore_3
    //   106: aload_2
    //   107: ldc_w ','
    //   110: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: pop
    //   114: aload_2
    //   115: aload_3
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: goto -> 87
    //   123: aload_2
    //   124: iconst_1
    //   125: invokevirtual substring : (I)Ljava/lang/String;
    //   128: putstatic com/mysql/jdbc/DatabaseMetaData.mysqlKeywords : Ljava/lang/String;
    //   131: getstatic com/mysql/jdbc/DatabaseMetaData.mysqlKeywords : Ljava/lang/String;
    //   134: astore_1
    //   135: ldc com/mysql/jdbc/DatabaseMetaData
    //   137: monitorexit
    //   138: aload_1
    //   139: areturn
    //   140: astore_1
    //   141: ldc com/mysql/jdbc/DatabaseMetaData
    //   143: monitorexit
    //   144: aload_1
    //   145: athrow
    // Exception table:
    //   from	to	target	type
    //   13	26	140	finally
    //   28	62	140	finally
    //   65	69	140	finally
    //   69	87	140	finally
    //   87	120	140	finally
    //   123	138	140	finally
    //   141	144	140	finally
  }
  
  public int getSQLStateType() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0) ? 2 : (this.conn.getUseSqlStateCodes() ? 2 : 1);
  }
  
  public String getSchemaTerm() throws SQLException {
    return "";
  }
  
  public ResultSet getSchemas() throws SQLException {
    Field field2 = new Field("", "TABLE_SCHEM", 1, 0);
    Field field1 = new Field("", "TABLE_CATALOG", 1, 0);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { field2, field1 }, arrayList);
  }
  
  public ResultSet getSchemas(String paramString1, String paramString2) throws SQLException {
    Field field1 = new Field("", "TABLE_SCHEM", 12, 255);
    Field field2 = new Field("", "TABLE_CATALOG", 12, 255);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { field1, field2 }, arrayList);
  }
  
  public String getSearchStringEscape() throws SQLException {
    return "\\";
  }
  
  public String getStringFunctions() throws SQLException {
    return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
  }
  
  public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) throws SQLException {
    Field field4 = new Field("", "TABLE_CAT", 1, 32);
    Field field1 = new Field("", "TABLE_SCHEM", 1, 32);
    Field field3 = new Field("", "TABLE_NAME", 1, 32);
    Field field2 = new Field("", "SUPERTABLE_NAME", 1, 32);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { field4, field1, field3, field2 }, arrayList);
  }
  
  public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) throws SQLException {
    Field field1 = new Field("", "TYPE_CAT", 1, 32);
    Field field6 = new Field("", "TYPE_SCHEM", 1, 32);
    Field field5 = new Field("", "TYPE_NAME", 1, 32);
    Field field4 = new Field("", "SUPERTYPE_CAT", 1, 32);
    Field field3 = new Field("", "SUPERTYPE_SCHEM", 1, 32);
    Field field2 = new Field("", "SUPERTYPE_NAME", 1, 32);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { field1, field6, field5, field4, field3, field2 }, arrayList);
  }
  
  public String getSystemFunctions() throws SQLException {
    return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
  }
  
  public String getTableNameWithCase(String paramString) {
    String str = paramString;
    if (this.conn.lowerCaseTableNames())
      str = paramString.toLowerCase(); 
    return str;
  }
  
  public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) throws SQLException {
    // Byte code:
    //   0: aload_3
    //   1: ifnonnull -> 37
    //   4: aload_0
    //   5: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   8: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   13: ifeq -> 23
    //   16: ldc_w '%'
    //   19: astore_3
    //   20: goto -> 37
    //   23: ldc_w 'Table name pattern can not be NULL or empty.'
    //   26: ldc_w 'S1009'
    //   29: aload_0
    //   30: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   33: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   36: athrow
    //   37: new com/mysql/jdbc/Field
    //   40: dup
    //   41: ldc_w ''
    //   44: ldc_w 'TABLE_CAT'
    //   47: iconst_1
    //   48: bipush #64
    //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   53: astore #10
    //   55: new com/mysql/jdbc/Field
    //   58: dup
    //   59: ldc_w ''
    //   62: ldc_w 'TABLE_SCHEM'
    //   65: iconst_1
    //   66: iconst_1
    //   67: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   70: astore #11
    //   72: new com/mysql/jdbc/Field
    //   75: dup
    //   76: ldc_w ''
    //   79: ldc_w 'TABLE_NAME'
    //   82: iconst_1
    //   83: bipush #64
    //   85: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   88: astore #13
    //   90: new com/mysql/jdbc/Field
    //   93: dup
    //   94: ldc_w ''
    //   97: ldc_w 'GRANTOR'
    //   100: iconst_1
    //   101: bipush #77
    //   103: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   106: astore #12
    //   108: new com/mysql/jdbc/Field
    //   111: dup
    //   112: ldc_w ''
    //   115: ldc_w 'GRANTEE'
    //   118: iconst_1
    //   119: bipush #77
    //   121: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   124: astore #7
    //   126: new com/mysql/jdbc/Field
    //   129: dup
    //   130: ldc_w ''
    //   133: ldc_w 'PRIVILEGE'
    //   136: iconst_1
    //   137: bipush #64
    //   139: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   142: astore #8
    //   144: new com/mysql/jdbc/Field
    //   147: dup
    //   148: ldc_w ''
    //   151: ldc_w 'IS_GRANTABLE'
    //   154: iconst_1
    //   155: iconst_3
    //   156: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   159: astore #14
    //   161: new java/util/ArrayList
    //   164: dup
    //   165: invokespecial <init> : ()V
    //   168: astore #9
    //   170: aload_0
    //   171: ldc_w 'SELECT host,db,table_name,grantor,user,table_priv FROM mysql.tables_priv WHERE db LIKE ? AND table_name LIKE ?'
    //   174: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   177: astore #6
    //   179: aload_1
    //   180: ifnull -> 209
    //   183: aload_1
    //   184: invokevirtual length : ()I
    //   187: istore #4
    //   189: iload #4
    //   191: ifeq -> 209
    //   194: aload_1
    //   195: astore #5
    //   197: goto -> 214
    //   200: astore_1
    //   201: aconst_null
    //   202: astore_2
    //   203: aload #6
    //   205: astore_3
    //   206: goto -> 690
    //   209: ldc_w '%'
    //   212: astore #5
    //   214: aload #6
    //   216: iconst_1
    //   217: aload #5
    //   219: invokeinterface setString : (ILjava/lang/String;)V
    //   224: aload #6
    //   226: iconst_2
    //   227: aload_3
    //   228: invokeinterface setString : (ILjava/lang/String;)V
    //   233: aload #6
    //   235: invokeinterface executeQuery : ()Ljava/sql/ResultSet;
    //   240: astore #5
    //   242: aload #5
    //   244: invokeinterface next : ()Z
    //   249: ifeq -> 590
    //   252: aload #5
    //   254: iconst_1
    //   255: invokeinterface getString : (I)Ljava/lang/String;
    //   260: astore #19
    //   262: aload #5
    //   264: iconst_2
    //   265: invokeinterface getString : (I)Ljava/lang/String;
    //   270: astore #15
    //   272: aload #5
    //   274: iconst_3
    //   275: invokeinterface getString : (I)Ljava/lang/String;
    //   280: astore #16
    //   282: aload #5
    //   284: iconst_4
    //   285: invokeinterface getString : (I)Ljava/lang/String;
    //   290: astore #17
    //   292: aload #5
    //   294: iconst_5
    //   295: invokeinterface getString : (I)Ljava/lang/String;
    //   300: astore_3
    //   301: aload_3
    //   302: ifnull -> 318
    //   305: aload_3
    //   306: invokevirtual length : ()I
    //   309: ifne -> 315
    //   312: goto -> 318
    //   315: goto -> 322
    //   318: ldc_w '%'
    //   321: astore_3
    //   322: new java/lang/StringBuilder
    //   325: astore #18
    //   327: aload #18
    //   329: aload_3
    //   330: invokespecial <init> : (Ljava/lang/String;)V
    //   333: aload #19
    //   335: ifnull -> 367
    //   338: aload_0
    //   339: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   342: invokeinterface getUseHostsInPrivileges : ()Z
    //   347: ifeq -> 367
    //   350: aload #18
    //   352: ldc_w '@'
    //   355: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   358: pop
    //   359: aload #18
    //   361: aload #19
    //   363: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   366: pop
    //   367: aload #5
    //   369: bipush #6
    //   371: invokeinterface getString : (I)Ljava/lang/String;
    //   376: astore_3
    //   377: aload_3
    //   378: ifnull -> 587
    //   381: aload_3
    //   382: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   385: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   388: astore_3
    //   389: new java/util/StringTokenizer
    //   392: astore #19
    //   394: aload #19
    //   396: aload_3
    //   397: ldc_w ','
    //   400: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   403: aload #19
    //   405: invokevirtual hasMoreTokens : ()Z
    //   408: ifeq -> 587
    //   411: aload #19
    //   413: invokevirtual nextToken : ()Ljava/lang/String;
    //   416: invokevirtual trim : ()Ljava/lang/String;
    //   419: astore #22
    //   421: aload_0
    //   422: aload_1
    //   423: aload_2
    //   424: aload #16
    //   426: ldc_w '%'
    //   429: invokevirtual getColumns : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
    //   432: astore_3
    //   433: aload_3
    //   434: invokeinterface next : ()Z
    //   439: ifeq -> 553
    //   442: bipush #8
    //   444: anewarray [B
    //   447: astore #20
    //   449: aload #20
    //   451: iconst_0
    //   452: aload_0
    //   453: aload #15
    //   455: invokevirtual s2b : (Ljava/lang/String;)[B
    //   458: aastore
    //   459: aload #20
    //   461: iconst_1
    //   462: aconst_null
    //   463: aastore
    //   464: aload #20
    //   466: iconst_2
    //   467: aload_0
    //   468: aload #16
    //   470: invokevirtual s2b : (Ljava/lang/String;)[B
    //   473: aastore
    //   474: aload #17
    //   476: ifnull -> 492
    //   479: aload #20
    //   481: iconst_3
    //   482: aload_0
    //   483: aload #17
    //   485: invokevirtual s2b : (Ljava/lang/String;)[B
    //   488: aastore
    //   489: goto -> 497
    //   492: aload #20
    //   494: iconst_3
    //   495: aconst_null
    //   496: aastore
    //   497: aload #20
    //   499: iconst_4
    //   500: aload_0
    //   501: aload #18
    //   503: invokevirtual toString : ()Ljava/lang/String;
    //   506: invokevirtual s2b : (Ljava/lang/String;)[B
    //   509: aastore
    //   510: aload #20
    //   512: iconst_5
    //   513: aload_0
    //   514: aload #22
    //   516: invokevirtual s2b : (Ljava/lang/String;)[B
    //   519: aastore
    //   520: aload #20
    //   522: bipush #6
    //   524: aconst_null
    //   525: aastore
    //   526: new com/mysql/jdbc/ByteArrayRow
    //   529: astore #21
    //   531: aload #21
    //   533: aload #20
    //   535: aload_0
    //   536: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   539: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
    //   542: aload #9
    //   544: aload #21
    //   546: invokevirtual add : (Ljava/lang/Object;)Z
    //   549: pop
    //   550: goto -> 433
    //   553: aload_3
    //   554: ifnull -> 563
    //   557: aload_3
    //   558: invokeinterface close : ()V
    //   563: goto -> 403
    //   566: astore_1
    //   567: aload_3
    //   568: astore_2
    //   569: goto -> 575
    //   572: astore_1
    //   573: aconst_null
    //   574: astore_2
    //   575: aload_2
    //   576: ifnull -> 585
    //   579: aload_2
    //   580: invokeinterface close : ()V
    //   585: aload_1
    //   586: athrow
    //   587: goto -> 242
    //   590: aload #5
    //   592: ifnull -> 606
    //   595: aload #5
    //   597: invokeinterface close : ()V
    //   602: goto -> 606
    //   605: astore_1
    //   606: aload #6
    //   608: ifnull -> 618
    //   611: aload #6
    //   613: invokeinterface close : ()V
    //   618: aload_0
    //   619: bipush #7
    //   621: anewarray com/mysql/jdbc/Field
    //   624: dup
    //   625: iconst_0
    //   626: aload #10
    //   628: aastore
    //   629: dup
    //   630: iconst_1
    //   631: aload #11
    //   633: aastore
    //   634: dup
    //   635: iconst_2
    //   636: aload #13
    //   638: aastore
    //   639: dup
    //   640: iconst_3
    //   641: aload #12
    //   643: aastore
    //   644: dup
    //   645: iconst_4
    //   646: aload #7
    //   648: aastore
    //   649: dup
    //   650: iconst_5
    //   651: aload #8
    //   653: aastore
    //   654: dup
    //   655: bipush #6
    //   657: aload #14
    //   659: aastore
    //   660: aload #9
    //   662: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
    //   665: areturn
    //   666: astore_1
    //   667: aload #5
    //   669: astore_2
    //   670: aload #6
    //   672: astore_3
    //   673: goto -> 690
    //   676: astore_1
    //   677: aconst_null
    //   678: astore_2
    //   679: aload #6
    //   681: astore_3
    //   682: goto -> 690
    //   685: astore_1
    //   686: aconst_null
    //   687: astore_3
    //   688: aconst_null
    //   689: astore_2
    //   690: aload_2
    //   691: ifnull -> 704
    //   694: aload_2
    //   695: invokeinterface close : ()V
    //   700: goto -> 704
    //   703: astore_2
    //   704: aload_3
    //   705: ifnull -> 714
    //   708: aload_3
    //   709: invokeinterface close : ()V
    //   714: aload_1
    //   715: athrow
    //   716: astore_3
    //   717: goto -> 563
    //   720: astore_2
    //   721: goto -> 585
    //   724: astore_1
    //   725: goto -> 618
    //   728: astore_2
    //   729: goto -> 714
    // Exception table:
    //   from	to	target	type
    //   170	179	685	finally
    //   183	189	200	finally
    //   214	242	676	finally
    //   242	301	666	finally
    //   305	312	666	finally
    //   322	333	666	finally
    //   338	367	666	finally
    //   367	377	666	finally
    //   381	403	666	finally
    //   403	421	666	finally
    //   421	433	572	finally
    //   433	459	566	finally
    //   464	474	566	finally
    //   479	489	566	finally
    //   497	520	566	finally
    //   526	550	566	finally
    //   557	563	716	java/lang/Exception
    //   557	563	666	finally
    //   579	585	720	java/lang/Exception
    //   579	585	666	finally
    //   585	587	666	finally
    //   595	602	605	java/lang/Exception
    //   611	618	724	java/lang/Exception
    //   694	700	703	java/lang/Exception
    //   708	714	728	java/lang/Exception
  }
  
  public ResultSet getTableTypes() throws SQLException {
    ArrayList<ByteArrayRow> arrayList = new ArrayList();
    Field field = new Field("", "TABLE_TYPE", 12, 256);
    boolean bool = this.conn.versionMeetsMinimum(5, 0, 1);
    byte[] arrayOfByte1 = TableType.LOCAL_TEMPORARY.asBytes();
    ExceptionInterceptor exceptionInterceptor2 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { arrayOfByte1 }, exceptionInterceptor2));
    arrayOfByte1 = TableType.SYSTEM_TABLE.asBytes();
    exceptionInterceptor2 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { arrayOfByte1 }, exceptionInterceptor2));
    if (bool) {
      arrayOfByte1 = TableType.SYSTEM_VIEW.asBytes();
      exceptionInterceptor2 = getExceptionInterceptor();
      arrayList.add(new ByteArrayRow(new byte[][] { arrayOfByte1 }, exceptionInterceptor2));
    } 
    byte[] arrayOfByte2 = TableType.TABLE.asBytes();
    ExceptionInterceptor exceptionInterceptor1 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { arrayOfByte2 }, exceptionInterceptor1));
    if (bool) {
      byte[] arrayOfByte = TableType.VIEW.asBytes();
      ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
      arrayList.add(new ByteArrayRow(new byte[][] { arrayOfByte }, exceptionInterceptor));
    } 
    return buildResultSet(new Field[] { field }, (ArrayList)arrayList);
  }
  
  public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) throws SQLException {
    paramString2 = paramString3;
    if (paramString3 == null)
      if (this.conn.getNullNamePatternMatchesAll()) {
        paramString2 = "%";
      } else {
        throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }  
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    Statement statement = this.conn.getMetadataSafeStatement();
    if (paramString1 == null || paramString1.length() == 0) {
      if (this.conn.getNullCatalogMeansCurrent()) {
        paramString3 = this.database;
      } else {
        paramString3 = "";
      } 
    } else {
      paramString3 = paramString1;
    } 
    List<String> list = StringUtils.splitDBdotName(paramString2, paramString3, this.quotedId, this.conn.isNoBackslashEscapesSet());
    if (list.size() == 2)
      paramString2 = list.get(1); 
    try {
      IterateBlock<String> iterateBlock = new IterateBlock<String>() {
          public final DatabaseMetaData this$0;
          
          public final SortedMap val$sortedRows;
          
          public final Statement val$stmt;
          
          public final String val$tableNamePat;
          
          public final String[] val$types;
          
          public void forEach(String param1String) throws SQLException {
            // Byte code:
            //   0: ldc 'information_schema'
            //   2: aload_1
            //   3: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
            //   6: ifne -> 36
            //   9: ldc 'mysql'
            //   11: aload_1
            //   12: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
            //   15: ifne -> 36
            //   18: ldc 'performance_schema'
            //   20: aload_1
            //   21: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
            //   24: ifeq -> 30
            //   27: goto -> 36
            //   30: iconst_0
            //   31: istore #7
            //   33: goto -> 39
            //   36: iconst_1
            //   37: istore #7
            //   39: aconst_null
            //   40: astore #14
            //   42: aload_0
            //   43: getfield val$stmt : Ljava/sql/Statement;
            //   46: astore #16
            //   48: new java/lang/StringBuilder
            //   51: astore #15
            //   53: aload #15
            //   55: invokespecial <init> : ()V
            //   58: aload_0
            //   59: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   62: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   65: iconst_5
            //   66: iconst_0
            //   67: iconst_2
            //   68: invokeinterface versionMeetsMinimum : (III)Z
            //   73: istore #13
            //   75: iload #13
            //   77: ifne -> 87
            //   80: ldc 'SHOW TABLES FROM '
            //   82: astore #14
            //   84: goto -> 91
            //   87: ldc 'SHOW FULL TABLES FROM '
            //   89: astore #14
            //   91: aload #15
            //   93: aload #14
            //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   98: pop
            //   99: aload_0
            //   100: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   103: astore #14
            //   105: aload #15
            //   107: aload_1
            //   108: aload #14
            //   110: getfield quotedId : Ljava/lang/String;
            //   113: aload #14
            //   115: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   118: invokeinterface getPedantic : ()Z
            //   123: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   126: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   129: pop
            //   130: aload #15
            //   132: ldc ' LIKE '
            //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   137: pop
            //   138: aload #15
            //   140: aload_0
            //   141: getfield val$tableNamePat : Ljava/lang/String;
            //   144: ldc '''
            //   146: iconst_1
            //   147: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
            //   150: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   153: pop
            //   154: aload #16
            //   156: aload #15
            //   158: invokevirtual toString : ()Ljava/lang/String;
            //   161: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
            //   166: astore #15
            //   168: aload_0
            //   169: getfield val$types : [Ljava/lang/String;
            //   172: astore #14
            //   174: aload #14
            //   176: ifnull -> 436
            //   179: aload #14
            //   181: arraylength
            //   182: ifne -> 188
            //   185: goto -> 436
            //   188: iconst_0
            //   189: istore #8
            //   191: iconst_0
            //   192: istore #6
            //   194: iconst_0
            //   195: istore #5
            //   197: iconst_0
            //   198: istore #4
            //   200: iconst_0
            //   201: istore_3
            //   202: iconst_0
            //   203: istore_2
            //   204: aload_0
            //   205: getfield val$types : [Ljava/lang/String;
            //   208: astore #14
            //   210: iload #8
            //   212: aload #14
            //   214: arraylength
            //   215: if_icmpge -> 418
            //   218: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   221: aload #14
            //   223: iload #8
            //   225: aaload
            //   226: invokevirtual equalsTo : (Ljava/lang/String;)Z
            //   229: ifeq -> 249
            //   232: iconst_1
            //   233: istore #9
            //   235: iload #5
            //   237: istore #10
            //   239: iload #4
            //   241: istore #11
            //   243: iload_3
            //   244: istore #12
            //   246: goto -> 397
            //   249: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.VIEW : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   252: aload_0
            //   253: getfield val$types : [Ljava/lang/String;
            //   256: iload #8
            //   258: aaload
            //   259: invokevirtual equalsTo : (Ljava/lang/String;)Z
            //   262: ifeq -> 282
            //   265: iconst_1
            //   266: istore #10
            //   268: iload #6
            //   270: istore #9
            //   272: iload #4
            //   274: istore #11
            //   276: iload_3
            //   277: istore #12
            //   279: goto -> 397
            //   282: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.SYSTEM_TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   285: aload_0
            //   286: getfield val$types : [Ljava/lang/String;
            //   289: iload #8
            //   291: aaload
            //   292: invokevirtual equalsTo : (Ljava/lang/String;)Z
            //   295: ifeq -> 315
            //   298: iconst_1
            //   299: istore #11
            //   301: iload #6
            //   303: istore #9
            //   305: iload #5
            //   307: istore #10
            //   309: iload_3
            //   310: istore #12
            //   312: goto -> 397
            //   315: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.SYSTEM_VIEW : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   318: aload_0
            //   319: getfield val$types : [Ljava/lang/String;
            //   322: iload #8
            //   324: aaload
            //   325: invokevirtual equalsTo : (Ljava/lang/String;)Z
            //   328: ifeq -> 349
            //   331: iconst_1
            //   332: istore #12
            //   334: iload #6
            //   336: istore #9
            //   338: iload #5
            //   340: istore #10
            //   342: iload #4
            //   344: istore #11
            //   346: goto -> 397
            //   349: iload #6
            //   351: istore #9
            //   353: iload #5
            //   355: istore #10
            //   357: iload #4
            //   359: istore #11
            //   361: iload_3
            //   362: istore #12
            //   364: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.LOCAL_TEMPORARY : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   367: aload_0
            //   368: getfield val$types : [Ljava/lang/String;
            //   371: iload #8
            //   373: aaload
            //   374: invokevirtual equalsTo : (Ljava/lang/String;)Z
            //   377: ifeq -> 397
            //   380: iconst_1
            //   381: istore_2
            //   382: iload_3
            //   383: istore #12
            //   385: iload #4
            //   387: istore #11
            //   389: iload #5
            //   391: istore #10
            //   393: iload #6
            //   395: istore #9
            //   397: iinc #8, 1
            //   400: iload #9
            //   402: istore #6
            //   404: iload #10
            //   406: istore #5
            //   408: iload #11
            //   410: istore #4
            //   412: iload #12
            //   414: istore_3
            //   415: goto -> 204
            //   418: iload #4
            //   420: istore #8
            //   422: iload #5
            //   424: istore #4
            //   426: iload #8
            //   428: istore #5
            //   430: iload_2
            //   431: istore #8
            //   433: goto -> 450
            //   436: iconst_1
            //   437: istore #6
            //   439: iconst_1
            //   440: istore #4
            //   442: iconst_1
            //   443: istore #5
            //   445: iconst_1
            //   446: istore_3
            //   447: iconst_1
            //   448: istore #8
            //   450: aload_0
            //   451: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   454: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
            //   457: iconst_5
            //   458: iconst_0
            //   459: iconst_2
            //   460: invokeinterface versionMeetsMinimum : (III)Z
            //   465: istore #13
            //   467: iload #13
            //   469: ifeq -> 503
            //   472: aload #15
            //   474: ldc 'table_type'
            //   476: invokeinterface findColumn : (Ljava/lang/String;)I
            //   481: istore_2
            //   482: iconst_1
            //   483: istore #9
            //   485: goto -> 508
            //   488: astore #14
            //   490: aload #15
            //   492: ldc 'Type'
            //   494: invokeinterface findColumn : (Ljava/lang/String;)I
            //   499: istore_2
            //   500: goto -> 482
            //   503: iconst_1
            //   504: istore_2
            //   505: iconst_0
            //   506: istore #9
            //   508: aload #15
            //   510: invokeinterface next : ()Z
            //   515: ifeq -> 1353
            //   518: bipush #10
            //   520: anewarray [B
            //   523: astore #16
            //   525: aload_1
            //   526: ifnonnull -> 535
            //   529: aconst_null
            //   530: astore #14
            //   532: goto -> 545
            //   535: aload_0
            //   536: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   539: aload_1
            //   540: invokevirtual s2b : (Ljava/lang/String;)[B
            //   543: astore #14
            //   545: aload #16
            //   547: iconst_0
            //   548: aload #14
            //   550: aastore
            //   551: aload #16
            //   553: iconst_1
            //   554: aconst_null
            //   555: aastore
            //   556: aload #16
            //   558: iconst_2
            //   559: aload #15
            //   561: iconst_1
            //   562: invokeinterface getBytes : (I)[B
            //   567: aastore
            //   568: aload #16
            //   570: iconst_4
            //   571: iconst_0
            //   572: newarray byte
            //   574: aastore
            //   575: aload #16
            //   577: iconst_5
            //   578: aconst_null
            //   579: aastore
            //   580: aload #16
            //   582: bipush #6
            //   584: aconst_null
            //   585: aastore
            //   586: aload #16
            //   588: bipush #7
            //   590: aconst_null
            //   591: aastore
            //   592: aload #16
            //   594: bipush #8
            //   596: aconst_null
            //   597: aastore
            //   598: aload #16
            //   600: bipush #9
            //   602: aconst_null
            //   603: aastore
            //   604: iload #9
            //   606: ifeq -> 1262
            //   609: aload #15
            //   611: iload_2
            //   612: invokeinterface getString : (I)Ljava/lang/String;
            //   617: astore #14
            //   619: getstatic com/mysql/jdbc/DatabaseMetaData$11.$SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType : [I
            //   622: aload #14
            //   624: invokestatic getTableTypeCompliantWith : (Ljava/lang/String;)Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   627: invokevirtual ordinal : ()I
            //   630: iaload
            //   631: istore #10
            //   633: iload #10
            //   635: iconst_1
            //   636: if_icmpeq -> 1097
            //   639: iload #10
            //   641: iconst_2
            //   642: if_icmpeq -> 1009
            //   645: iload #10
            //   647: iconst_3
            //   648: if_icmpeq -> 921
            //   651: iload #10
            //   653: iconst_4
            //   654: if_icmpeq -> 834
            //   657: iload #10
            //   659: iconst_5
            //   660: if_icmpeq -> 746
            //   663: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   666: astore #18
            //   668: aload #16
            //   670: iconst_3
            //   671: aload #18
            //   673: invokevirtual asBytes : ()[B
            //   676: aastore
            //   677: aload_0
            //   678: getfield val$sortedRows : Ljava/util/SortedMap;
            //   681: astore #14
            //   683: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   686: astore #17
            //   688: aload #17
            //   690: aload_0
            //   691: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   694: aload #18
            //   696: invokevirtual getName : ()Ljava/lang/String;
            //   699: aload_1
            //   700: aconst_null
            //   701: aload #15
            //   703: iconst_1
            //   704: invokeinterface getString : (I)Ljava/lang/String;
            //   709: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   712: new com/mysql/jdbc/ByteArrayRow
            //   715: astore #18
            //   717: aload #18
            //   719: aload #16
            //   721: aload_0
            //   722: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   725: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   728: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   731: aload #14
            //   733: aload #17
            //   735: aload #18
            //   737: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   742: pop
            //   743: goto -> 1350
            //   746: iload #8
            //   748: ifeq -> 1350
            //   751: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.LOCAL_TEMPORARY : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   754: astore #18
            //   756: aload #16
            //   758: iconst_3
            //   759: aload #18
            //   761: invokevirtual asBytes : ()[B
            //   764: aastore
            //   765: aload_0
            //   766: getfield val$sortedRows : Ljava/util/SortedMap;
            //   769: astore #17
            //   771: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   774: astore #14
            //   776: aload #14
            //   778: aload_0
            //   779: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   782: aload #18
            //   784: invokevirtual getName : ()Ljava/lang/String;
            //   787: aload_1
            //   788: aconst_null
            //   789: aload #15
            //   791: iconst_1
            //   792: invokeinterface getString : (I)Ljava/lang/String;
            //   797: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   800: new com/mysql/jdbc/ByteArrayRow
            //   803: astore #18
            //   805: aload #18
            //   807: aload #16
            //   809: aload_0
            //   810: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   813: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   816: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   819: aload #17
            //   821: aload #14
            //   823: aload #18
            //   825: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   830: pop
            //   831: goto -> 1350
            //   834: iload_3
            //   835: ifeq -> 1350
            //   838: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.SYSTEM_VIEW : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   841: astore #18
            //   843: aload #16
            //   845: iconst_3
            //   846: aload #18
            //   848: invokevirtual asBytes : ()[B
            //   851: aastore
            //   852: aload_0
            //   853: getfield val$sortedRows : Ljava/util/SortedMap;
            //   856: astore #17
            //   858: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   861: astore #14
            //   863: aload #14
            //   865: aload_0
            //   866: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   869: aload #18
            //   871: invokevirtual getName : ()Ljava/lang/String;
            //   874: aload_1
            //   875: aconst_null
            //   876: aload #15
            //   878: iconst_1
            //   879: invokeinterface getString : (I)Ljava/lang/String;
            //   884: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   887: new com/mysql/jdbc/ByteArrayRow
            //   890: astore #18
            //   892: aload #18
            //   894: aload #16
            //   896: aload_0
            //   897: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   900: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   903: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   906: aload #17
            //   908: aload #14
            //   910: aload #18
            //   912: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   917: pop
            //   918: goto -> 1350
            //   921: iload #5
            //   923: ifeq -> 1350
            //   926: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.SYSTEM_TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   929: astore #18
            //   931: aload #16
            //   933: iconst_3
            //   934: aload #18
            //   936: invokevirtual asBytes : ()[B
            //   939: aastore
            //   940: aload_0
            //   941: getfield val$sortedRows : Ljava/util/SortedMap;
            //   944: astore #14
            //   946: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   949: astore #17
            //   951: aload #17
            //   953: aload_0
            //   954: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   957: aload #18
            //   959: invokevirtual getName : ()Ljava/lang/String;
            //   962: aload_1
            //   963: aconst_null
            //   964: aload #15
            //   966: iconst_1
            //   967: invokeinterface getString : (I)Ljava/lang/String;
            //   972: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   975: new com/mysql/jdbc/ByteArrayRow
            //   978: astore #18
            //   980: aload #18
            //   982: aload #16
            //   984: aload_0
            //   985: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   988: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   991: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   994: aload #14
            //   996: aload #17
            //   998: aload #18
            //   1000: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   1005: pop
            //   1006: goto -> 1350
            //   1009: iload #4
            //   1011: ifeq -> 1350
            //   1014: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.VIEW : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   1017: astore #18
            //   1019: aload #16
            //   1021: iconst_3
            //   1022: aload #18
            //   1024: invokevirtual asBytes : ()[B
            //   1027: aastore
            //   1028: aload_0
            //   1029: getfield val$sortedRows : Ljava/util/SortedMap;
            //   1032: astore #17
            //   1034: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   1037: astore #14
            //   1039: aload #14
            //   1041: aload_0
            //   1042: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1045: aload #18
            //   1047: invokevirtual getName : ()Ljava/lang/String;
            //   1050: aload_1
            //   1051: aconst_null
            //   1052: aload #15
            //   1054: iconst_1
            //   1055: invokeinterface getString : (I)Ljava/lang/String;
            //   1060: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   1063: new com/mysql/jdbc/ByteArrayRow
            //   1066: astore #18
            //   1068: aload #18
            //   1070: aload #16
            //   1072: aload_0
            //   1073: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1076: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   1079: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   1082: aload #17
            //   1084: aload #14
            //   1086: aload #18
            //   1088: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   1093: pop
            //   1094: goto -> 1350
            //   1097: iload #7
            //   1099: ifeq -> 1156
            //   1102: iload #5
            //   1104: ifeq -> 1156
            //   1107: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.SYSTEM_TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   1110: astore #17
            //   1112: aload #16
            //   1114: iconst_3
            //   1115: aload #17
            //   1117: invokevirtual asBytes : ()[B
            //   1120: aastore
            //   1121: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   1124: astore #14
            //   1126: aload #14
            //   1128: aload_0
            //   1129: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1132: aload #17
            //   1134: invokevirtual getName : ()Ljava/lang/String;
            //   1137: aload_1
            //   1138: aconst_null
            //   1139: aload #15
            //   1141: iconst_1
            //   1142: invokeinterface getString : (I)Ljava/lang/String;
            //   1147: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   1150: iconst_1
            //   1151: istore #10
            //   1153: goto -> 1217
            //   1156: iload #7
            //   1158: ifne -> 1211
            //   1161: iload #6
            //   1163: ifeq -> 1211
            //   1166: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   1169: astore #14
            //   1171: aload #16
            //   1173: iconst_3
            //   1174: aload #14
            //   1176: invokevirtual asBytes : ()[B
            //   1179: aastore
            //   1180: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   1183: dup
            //   1184: aload_0
            //   1185: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1188: aload #14
            //   1190: invokevirtual getName : ()Ljava/lang/String;
            //   1193: aload_1
            //   1194: aconst_null
            //   1195: aload #15
            //   1197: iconst_1
            //   1198: invokeinterface getString : (I)Ljava/lang/String;
            //   1203: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   1206: astore #14
            //   1208: goto -> 1150
            //   1211: iconst_0
            //   1212: istore #10
            //   1214: aconst_null
            //   1215: astore #14
            //   1217: iload #10
            //   1219: ifeq -> 1350
            //   1222: aload_0
            //   1223: getfield val$sortedRows : Ljava/util/SortedMap;
            //   1226: astore #18
            //   1228: new com/mysql/jdbc/ByteArrayRow
            //   1231: astore #17
            //   1233: aload #17
            //   1235: aload #16
            //   1237: aload_0
            //   1238: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1241: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   1244: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   1247: aload #18
            //   1249: aload #14
            //   1251: aload #17
            //   1253: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   1258: pop
            //   1259: goto -> 1350
            //   1262: iload #6
            //   1264: ifeq -> 1350
            //   1267: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.TABLE : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
            //   1270: astore #18
            //   1272: aload #16
            //   1274: iconst_3
            //   1275: aload #18
            //   1277: invokevirtual asBytes : ()[B
            //   1280: aastore
            //   1281: aload_0
            //   1282: getfield val$sortedRows : Ljava/util/SortedMap;
            //   1285: astore #14
            //   1287: new com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey
            //   1290: astore #17
            //   1292: aload #17
            //   1294: aload_0
            //   1295: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1298: aload #18
            //   1300: invokevirtual getName : ()Ljava/lang/String;
            //   1303: aload_1
            //   1304: aconst_null
            //   1305: aload #15
            //   1307: iconst_1
            //   1308: invokeinterface getString : (I)Ljava/lang/String;
            //   1313: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   1316: new com/mysql/jdbc/ByteArrayRow
            //   1319: astore #18
            //   1321: aload #18
            //   1323: aload #16
            //   1325: aload_0
            //   1326: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
            //   1329: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
            //   1332: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
            //   1335: aload #14
            //   1337: aload #17
            //   1339: aload #18
            //   1341: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   1346: pop
            //   1347: goto -> 1350
            //   1350: goto -> 508
            //   1353: aload #15
            //   1355: ifnull -> 1365
            //   1358: aload #15
            //   1360: invokeinterface close : ()V
            //   1365: return
            //   1366: astore_1
            //   1367: aload #15
            //   1369: astore #14
            //   1371: goto -> 1397
            //   1374: astore_1
            //   1375: aconst_null
            //   1376: astore #14
            //   1378: goto -> 1397
            //   1381: astore_1
            //   1382: ldc '08S01'
            //   1384: aload_1
            //   1385: invokevirtual getSQLState : ()Ljava/lang/String;
            //   1388: invokevirtual equals : (Ljava/lang/Object;)Z
            //   1391: ifne -> 1395
            //   1394: return
            //   1395: aload_1
            //   1396: athrow
            //   1397: aload #14
            //   1399: ifnull -> 1409
            //   1402: aload #14
            //   1404: invokeinterface close : ()V
            //   1409: aload_1
            //   1410: athrow
            //   1411: astore #14
            //   1413: goto -> 503
            //   1416: astore_1
            //   1417: goto -> 1365
            //   1420: astore #14
            //   1422: goto -> 1409
            // Exception table:
            //   from	to	target	type
            //   42	75	1381	java/sql/SQLException
            //   42	75	1374	finally
            //   91	168	1381	java/sql/SQLException
            //   91	168	1374	finally
            //   168	174	1366	finally
            //   179	185	1366	finally
            //   204	232	1366	finally
            //   249	265	1366	finally
            //   282	298	1366	finally
            //   315	331	1366	finally
            //   364	380	1366	finally
            //   450	467	1366	finally
            //   472	482	488	java/sql/SQLException
            //   472	482	1366	finally
            //   490	500	1411	java/sql/SQLException
            //   490	500	1366	finally
            //   508	525	1366	finally
            //   535	545	1366	finally
            //   556	575	1366	finally
            //   609	633	1366	finally
            //   663	743	1366	finally
            //   751	831	1366	finally
            //   838	918	1366	finally
            //   926	1006	1366	finally
            //   1014	1094	1366	finally
            //   1107	1150	1366	finally
            //   1166	1208	1366	finally
            //   1222	1259	1366	finally
            //   1267	1347	1366	finally
            //   1358	1365	1416	java/lang/Exception
            //   1382	1394	1374	finally
            //   1395	1397	1374	finally
            //   1402	1409	1420	java/lang/Exception
          }
        };
      super(this, getCatalogIterator(paramString1), statement, paramString2, paramArrayOfString, treeMap);
      iterateBlock.doForAll();
      if (statement != null)
        statement.close(); 
      return buildResultSet(createTablesFields(), arrayList);
    } finally {
      if (statement != null)
        statement.close(); 
    } 
  }
  
  public String getTimeDateFunctions() throws SQLException {
    return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
  }
  
  public ResultSet getTypeInfo() throws SQLException {
    String str2;
    String str1;
    Field field11 = new Field("", "TYPE_NAME", 1, 32);
    Field field10 = new Field("", "DATA_TYPE", 4, 5);
    Field field1 = new Field("", "PRECISION", 4, 10);
    Field field16 = new Field("", "LITERAL_PREFIX", 1, 4);
    Field field7 = new Field("", "LITERAL_SUFFIX", 1, 4);
    Field field12 = new Field("", "CREATE_PARAMS", 1, 32);
    Field field2 = new Field("", "NULLABLE", 5, 5);
    Field field5 = new Field("", "CASE_SENSITIVE", 16, 3);
    Field field8 = new Field("", "SEARCHABLE", 5, 3);
    Field field3 = new Field("", "UNSIGNED_ATTRIBUTE", 16, 3);
    Field field6 = new Field("", "FIXED_PREC_SCALE", 16, 3);
    Field field4 = new Field("", "AUTO_INCREMENT", 16, 3);
    Field field9 = new Field("", "LOCAL_TYPE_NAME", 1, 32);
    Field field13 = new Field("", "MINIMUM_SCALE", 5, 5);
    Field field15 = new Field("", "MAXIMUM_SCALE", 5, 5);
    Field field17 = new Field("", "SQL_DATA_TYPE", 4, 10);
    Field field18 = new Field("", "SQL_DATETIME_SUB", 4, 10);
    Field field14 = new Field("", "NUM_PREC_RADIX", 4, 10);
    ArrayList<ByteArrayRow> arrayList = new ArrayList();
    byte[] arrayOfByte44 = s2b("BIT");
    byte[] arrayOfByte11 = Integer.toString(-7).getBytes();
    byte[] arrayOfByte14 = s2b("1");
    byte[] arrayOfByte57 = s2b("");
    byte[] arrayOfByte29 = s2b("");
    byte[] arrayOfByte48 = s2b("");
    byte[] arrayOfByte35 = Integer.toString(1).getBytes();
    byte[] arrayOfByte34 = s2b("true");
    byte[] arrayOfByte23 = Integer.toString(3).getBytes();
    byte[] arrayOfByte20 = s2b("false");
    byte[] arrayOfByte52 = s2b("false");
    byte[] arrayOfByte40 = s2b("false");
    byte[] arrayOfByte54 = s2b("BIT");
    byte[] arrayOfByte8 = s2b("0");
    byte[] arrayOfByte17 = s2b("0");
    byte[] arrayOfByte37 = s2b("0");
    byte[] arrayOfByte12 = s2b("0");
    byte[] arrayOfByte27 = s2b("10");
    ExceptionInterceptor exceptionInterceptor1 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte44, arrayOfByte11, arrayOfByte14, arrayOfByte57, arrayOfByte29, arrayOfByte48, arrayOfByte35, arrayOfByte34, arrayOfByte23, arrayOfByte20, 
            arrayOfByte52, arrayOfByte40, arrayOfByte54, arrayOfByte8, arrayOfByte17, arrayOfByte37, arrayOfByte12, arrayOfByte27 }, exceptionInterceptor1));
    arrayOfByte11 = s2b("BOOL");
    arrayOfByte29 = Integer.toString(-7).getBytes();
    byte[] arrayOfByte3 = s2b("1");
    arrayOfByte12 = s2b("");
    arrayOfByte48 = s2b("");
    arrayOfByte37 = s2b("");
    arrayOfByte17 = Integer.toString(1).getBytes();
    arrayOfByte27 = s2b("true");
    arrayOfByte35 = Integer.toString(3).getBytes();
    arrayOfByte44 = s2b("false");
    arrayOfByte52 = s2b("false");
    arrayOfByte57 = s2b("false");
    arrayOfByte54 = s2b("BOOL");
    arrayOfByte14 = s2b("0");
    arrayOfByte8 = s2b("0");
    arrayOfByte20 = s2b("0");
    arrayOfByte40 = s2b("0");
    arrayOfByte23 = s2b("10");
    ExceptionInterceptor exceptionInterceptor23 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte11, arrayOfByte29, arrayOfByte3, arrayOfByte12, arrayOfByte48, arrayOfByte37, arrayOfByte17, arrayOfByte27, arrayOfByte35, arrayOfByte44, 
            arrayOfByte52, arrayOfByte57, arrayOfByte54, arrayOfByte14, arrayOfByte8, arrayOfByte20, arrayOfByte40, arrayOfByte23 }, exceptionInterceptor23));
    arrayOfByte37 = s2b("TINYINT");
    arrayOfByte35 = Integer.toString(-6).getBytes();
    arrayOfByte54 = s2b("3");
    arrayOfByte23 = s2b("");
    arrayOfByte52 = s2b("");
    arrayOfByte12 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte11 = Integer.toString(1).getBytes();
    byte[] arrayOfByte33 = s2b("false");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte20 = s2b("true");
    arrayOfByte29 = s2b("false");
    arrayOfByte57 = s2b("true");
    arrayOfByte27 = s2b("TINYINT");
    arrayOfByte3 = s2b("0");
    arrayOfByte17 = s2b("0");
    arrayOfByte44 = s2b("0");
    arrayOfByte48 = s2b("0");
    arrayOfByte40 = s2b("10");
    ExceptionInterceptor exceptionInterceptor5 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte37, arrayOfByte35, arrayOfByte54, arrayOfByte23, arrayOfByte52, arrayOfByte12, arrayOfByte11, arrayOfByte33, arrayOfByte14, arrayOfByte20, 
            arrayOfByte29, arrayOfByte57, arrayOfByte27, arrayOfByte3, arrayOfByte17, arrayOfByte44, arrayOfByte48, arrayOfByte40 }, exceptionInterceptor5));
    arrayOfByte12 = s2b("TINYINT UNSIGNED");
    arrayOfByte17 = Integer.toString(-6).getBytes();
    arrayOfByte27 = s2b("3");
    arrayOfByte35 = s2b("");
    arrayOfByte29 = s2b("");
    arrayOfByte11 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte54 = Integer.toString(1).getBytes();
    arrayOfByte23 = s2b("false");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte20 = s2b("true");
    arrayOfByte52 = s2b("false");
    arrayOfByte3 = s2b("true");
    arrayOfByte37 = s2b("TINYINT UNSIGNED");
    arrayOfByte48 = s2b("0");
    arrayOfByte57 = s2b("0");
    arrayOfByte33 = s2b("0");
    arrayOfByte44 = s2b("0");
    byte[] arrayOfByte7 = s2b("10");
    ExceptionInterceptor exceptionInterceptor26 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte12, arrayOfByte17, arrayOfByte27, arrayOfByte35, arrayOfByte29, arrayOfByte11, arrayOfByte54, arrayOfByte23, arrayOfByte14, arrayOfByte20, 
            arrayOfByte52, arrayOfByte3, arrayOfByte37, arrayOfByte48, arrayOfByte57, arrayOfByte33, arrayOfByte44, arrayOfByte7 }, exceptionInterceptor26));
    arrayOfByte20 = s2b("BIGINT");
    arrayOfByte14 = Integer.toString(-5).getBytes();
    arrayOfByte48 = s2b("19");
    arrayOfByte12 = s2b("");
    arrayOfByte44 = s2b("");
    arrayOfByte57 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte33 = Integer.toString(1).getBytes();
    arrayOfByte37 = s2b("false");
    arrayOfByte54 = Integer.toString(3).getBytes();
    arrayOfByte29 = s2b("true");
    arrayOfByte7 = s2b("false");
    arrayOfByte35 = s2b("true");
    arrayOfByte52 = s2b("BIGINT");
    arrayOfByte11 = s2b("0");
    arrayOfByte17 = s2b("0");
    arrayOfByte3 = s2b("0");
    arrayOfByte23 = s2b("0");
    byte[] arrayOfByte39 = s2b("10");
    ExceptionInterceptor exceptionInterceptor18 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte20, arrayOfByte14, arrayOfByte48, arrayOfByte12, arrayOfByte44, arrayOfByte57, arrayOfByte33, arrayOfByte37, arrayOfByte54, arrayOfByte29, 
            arrayOfByte7, arrayOfByte35, arrayOfByte52, arrayOfByte11, arrayOfByte17, arrayOfByte3, arrayOfByte23, arrayOfByte39 }, exceptionInterceptor18));
    arrayOfByte14 = s2b("BIGINT UNSIGNED");
    arrayOfByte20 = Integer.toString(-5).getBytes();
    arrayOfByte12 = s2b("20");
    arrayOfByte39 = s2b("");
    arrayOfByte35 = s2b("");
    arrayOfByte52 = s2b("[(M)] [ZEROFILL]");
    arrayOfByte7 = Integer.toString(1).getBytes();
    arrayOfByte44 = s2b("false");
    arrayOfByte54 = Integer.toString(3).getBytes();
    arrayOfByte23 = s2b("true");
    arrayOfByte11 = s2b("false");
    arrayOfByte29 = s2b("true");
    arrayOfByte37 = s2b("BIGINT UNSIGNED");
    byte[] arrayOfByte26 = s2b("0");
    arrayOfByte48 = s2b("0");
    arrayOfByte3 = s2b("0");
    arrayOfByte17 = s2b("0");
    arrayOfByte33 = s2b("10");
    ExceptionInterceptor exceptionInterceptor38 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte14, arrayOfByte20, arrayOfByte12, arrayOfByte39, arrayOfByte35, arrayOfByte52, arrayOfByte7, arrayOfByte44, arrayOfByte54, arrayOfByte23, 
            arrayOfByte11, arrayOfByte29, arrayOfByte37, arrayOfByte26, arrayOfByte48, arrayOfByte3, arrayOfByte17, arrayOfByte33 }, exceptionInterceptor38));
    arrayOfByte12 = s2b("LONG VARBINARY");
    arrayOfByte3 = Integer.toString(-4).getBytes();
    arrayOfByte35 = s2b("16777215");
    arrayOfByte39 = s2b("'");
    arrayOfByte37 = s2b("'");
    arrayOfByte52 = s2b("");
    arrayOfByte14 = Integer.toString(1).getBytes();
    arrayOfByte23 = s2b("true");
    arrayOfByte29 = Integer.toString(3).getBytes();
    arrayOfByte17 = s2b("false");
    arrayOfByte7 = s2b("false");
    arrayOfByte44 = s2b("false");
    arrayOfByte48 = s2b("LONG VARBINARY");
    arrayOfByte20 = s2b("0");
    byte[] arrayOfByte56 = s2b("0");
    arrayOfByte26 = s2b("0");
    arrayOfByte33 = s2b("0");
    arrayOfByte54 = s2b("10");
    ExceptionInterceptor exceptionInterceptor7 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte12, arrayOfByte3, arrayOfByte35, arrayOfByte39, arrayOfByte37, arrayOfByte52, arrayOfByte14, arrayOfByte23, arrayOfByte29, arrayOfByte17, 
            arrayOfByte7, arrayOfByte44, arrayOfByte48, arrayOfByte20, arrayOfByte56, arrayOfByte26, arrayOfByte33, arrayOfByte54 }, exceptionInterceptor7));
    arrayOfByte35 = s2b("MEDIUMBLOB");
    arrayOfByte33 = Integer.toString(-4).getBytes();
    arrayOfByte52 = s2b("16777215");
    arrayOfByte39 = s2b("'");
    arrayOfByte56 = s2b("'");
    arrayOfByte48 = s2b("");
    arrayOfByte20 = Integer.toString(1).getBytes();
    arrayOfByte7 = s2b("true");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte29 = s2b("false");
    arrayOfByte3 = s2b("false");
    arrayOfByte12 = s2b("false");
    byte[] arrayOfByte10 = s2b("MEDIUMBLOB");
    arrayOfByte23 = s2b("0");
    arrayOfByte37 = s2b("0");
    arrayOfByte26 = s2b("0");
    arrayOfByte44 = s2b("0");
    arrayOfByte17 = s2b("10");
    ExceptionInterceptor exceptionInterceptor36 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte35, arrayOfByte33, arrayOfByte52, arrayOfByte39, arrayOfByte56, arrayOfByte48, arrayOfByte20, arrayOfByte7, arrayOfByte14, arrayOfByte29, 
            arrayOfByte3, arrayOfByte12, arrayOfByte10, arrayOfByte23, arrayOfByte37, arrayOfByte26, arrayOfByte44, arrayOfByte17 }, exceptionInterceptor36));
    arrayOfByte23 = s2b("LONGBLOB");
    arrayOfByte37 = Integer.toString(-4).getBytes();
    arrayOfByte3 = Integer.toString(2147483647).getBytes();
    arrayOfByte10 = s2b("'");
    arrayOfByte12 = s2b("'");
    arrayOfByte44 = s2b("");
    arrayOfByte14 = Integer.toString(1).getBytes();
    arrayOfByte56 = s2b("true");
    arrayOfByte35 = Integer.toString(3).getBytes();
    arrayOfByte7 = s2b("false");
    arrayOfByte39 = s2b("false");
    arrayOfByte48 = s2b("false");
    arrayOfByte33 = s2b("LONGBLOB");
    arrayOfByte20 = s2b("0");
    arrayOfByte17 = s2b("0");
    arrayOfByte29 = s2b("0");
    arrayOfByte26 = s2b("0");
    byte[] arrayOfByte53 = s2b("10");
    ExceptionInterceptor exceptionInterceptor35 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte23, arrayOfByte37, arrayOfByte3, arrayOfByte10, arrayOfByte12, arrayOfByte44, arrayOfByte14, arrayOfByte56, arrayOfByte35, arrayOfByte7, 
            arrayOfByte39, arrayOfByte48, arrayOfByte33, arrayOfByte20, arrayOfByte17, arrayOfByte29, arrayOfByte26, arrayOfByte53 }, exceptionInterceptor35));
    arrayOfByte17 = s2b("BLOB");
    arrayOfByte3 = Integer.toString(-4).getBytes();
    arrayOfByte48 = s2b("65535");
    arrayOfByte56 = s2b("'");
    arrayOfByte12 = s2b("'");
    arrayOfByte44 = s2b("");
    arrayOfByte7 = Integer.toString(1).getBytes();
    arrayOfByte39 = s2b("true");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte37 = s2b("false");
    arrayOfByte35 = s2b("false");
    arrayOfByte20 = s2b("false");
    arrayOfByte10 = s2b("BLOB");
    arrayOfByte53 = s2b("0");
    byte[] arrayOfByte51 = s2b("0");
    arrayOfByte23 = s2b("0");
    arrayOfByte26 = s2b("0");
    arrayOfByte29 = s2b("10");
    ExceptionInterceptor exceptionInterceptor22 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte17, arrayOfByte3, arrayOfByte48, arrayOfByte56, arrayOfByte12, arrayOfByte44, arrayOfByte7, arrayOfByte39, arrayOfByte14, arrayOfByte37, 
            arrayOfByte35, arrayOfByte20, arrayOfByte10, arrayOfByte53, arrayOfByte51, arrayOfByte23, arrayOfByte26, arrayOfByte29 }, exceptionInterceptor22));
    arrayOfByte20 = s2b("TINYBLOB");
    arrayOfByte56 = Integer.toString(-4).getBytes();
    arrayOfByte3 = s2b("255");
    arrayOfByte44 = s2b("'");
    arrayOfByte7 = s2b("'");
    arrayOfByte29 = s2b("");
    arrayOfByte37 = Integer.toString(1).getBytes();
    arrayOfByte53 = s2b("true");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte23 = s2b("false");
    arrayOfByte35 = s2b("false");
    arrayOfByte51 = s2b("false");
    byte[] arrayOfByte32 = s2b("TINYBLOB");
    arrayOfByte39 = s2b("0");
    arrayOfByte12 = s2b("0");
    arrayOfByte48 = s2b("0");
    arrayOfByte17 = s2b("0");
    arrayOfByte10 = s2b("10");
    ExceptionInterceptor exceptionInterceptor17 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte20, arrayOfByte56, arrayOfByte3, arrayOfByte44, arrayOfByte7, arrayOfByte29, arrayOfByte37, arrayOfByte53, arrayOfByte14, arrayOfByte23, 
            arrayOfByte35, arrayOfByte51, arrayOfByte32, arrayOfByte39, arrayOfByte12, arrayOfByte48, arrayOfByte17, arrayOfByte10 }, exceptionInterceptor17));
    arrayOfByte10 = s2b("VARBINARY");
    arrayOfByte7 = Integer.toString(-3).getBytes();
    if (this.conn.versionMeetsMinimum(5, 0, 3)) {
      str2 = "65535";
    } else {
      str2 = "255";
    } 
    arrayOfByte56 = s2b(str2);
    arrayOfByte32 = s2b("'");
    arrayOfByte12 = s2b("'");
    arrayOfByte14 = s2b("(M)");
    arrayOfByte51 = Integer.toString(1).getBytes();
    arrayOfByte39 = s2b("true");
    arrayOfByte53 = Integer.toString(3).getBytes();
    byte[] arrayOfByte2 = s2b("false");
    byte[] arrayOfByte25 = s2b("false");
    arrayOfByte44 = s2b("false");
    arrayOfByte37 = s2b("VARBINARY");
    arrayOfByte17 = s2b("0");
    arrayOfByte29 = s2b("0");
    arrayOfByte35 = s2b("0");
    arrayOfByte48 = s2b("0");
    arrayOfByte23 = s2b("10");
    ExceptionInterceptor exceptionInterceptor12 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte10, arrayOfByte7, arrayOfByte56, arrayOfByte32, arrayOfByte12, arrayOfByte14, arrayOfByte51, arrayOfByte39, arrayOfByte53, arrayOfByte2, 
            arrayOfByte25, arrayOfByte44, arrayOfByte37, arrayOfByte17, arrayOfByte29, arrayOfByte35, arrayOfByte48, arrayOfByte23 }, exceptionInterceptor12));
    arrayOfByte53 = s2b("BINARY");
    arrayOfByte32 = Integer.toString(-2).getBytes();
    byte[] arrayOfByte19 = s2b("255");
    arrayOfByte23 = s2b("'");
    arrayOfByte56 = s2b("'");
    arrayOfByte10 = s2b("(M)");
    arrayOfByte25 = Integer.toString(1).getBytes();
    arrayOfByte12 = s2b("true");
    arrayOfByte44 = Integer.toString(3).getBytes();
    arrayOfByte39 = s2b("false");
    arrayOfByte37 = s2b("false");
    arrayOfByte48 = s2b("false");
    arrayOfByte35 = s2b("BINARY");
    arrayOfByte29 = s2b("0");
    arrayOfByte51 = s2b("0");
    arrayOfByte2 = s2b("0");
    arrayOfByte14 = s2b("0");
    arrayOfByte7 = s2b("10");
    ExceptionInterceptor exceptionInterceptor10 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte53, arrayOfByte32, arrayOfByte19, arrayOfByte23, arrayOfByte56, arrayOfByte10, arrayOfByte25, arrayOfByte12, arrayOfByte44, arrayOfByte39, 
            arrayOfByte37, arrayOfByte48, arrayOfByte35, arrayOfByte29, arrayOfByte51, arrayOfByte2, arrayOfByte14, arrayOfByte7 }, exceptionInterceptor10));
    arrayOfByte35 = s2b("LONG VARCHAR");
    arrayOfByte10 = Integer.toString(-1).getBytes();
    arrayOfByte14 = s2b("16777215");
    arrayOfByte48 = s2b("'");
    arrayOfByte51 = s2b("'");
    arrayOfByte23 = s2b("");
    arrayOfByte19 = Integer.toString(1).getBytes();
    arrayOfByte25 = s2b("false");
    arrayOfByte37 = Integer.toString(3).getBytes();
    arrayOfByte7 = s2b("false");
    byte[] arrayOfByte16 = s2b("false");
    arrayOfByte56 = s2b("false");
    arrayOfByte53 = s2b("LONG VARCHAR");
    arrayOfByte39 = s2b("0");
    arrayOfByte44 = s2b("0");
    arrayOfByte29 = s2b("0");
    arrayOfByte2 = s2b("0");
    arrayOfByte12 = s2b("10");
    ExceptionInterceptor exceptionInterceptor21 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte35, arrayOfByte10, arrayOfByte14, arrayOfByte48, arrayOfByte51, arrayOfByte23, arrayOfByte19, arrayOfByte25, arrayOfByte37, arrayOfByte7, 
            arrayOfByte16, arrayOfByte56, arrayOfByte53, arrayOfByte39, arrayOfByte44, arrayOfByte29, arrayOfByte2, arrayOfByte12 }, exceptionInterceptor21));
    arrayOfByte12 = s2b("MEDIUMTEXT");
    arrayOfByte29 = Integer.toString(-1).getBytes();
    arrayOfByte53 = s2b("16777215");
    arrayOfByte25 = s2b("'");
    arrayOfByte16 = s2b("'");
    arrayOfByte44 = s2b("");
    arrayOfByte2 = Integer.toString(1).getBytes();
    arrayOfByte19 = s2b("false");
    byte[] arrayOfByte31 = Integer.toString(3).getBytes();
    arrayOfByte56 = s2b("false");
    arrayOfByte14 = s2b("false");
    arrayOfByte39 = s2b("false");
    arrayOfByte10 = s2b("MEDIUMTEXT");
    arrayOfByte23 = s2b("0");
    arrayOfByte48 = s2b("0");
    arrayOfByte35 = s2b("0");
    arrayOfByte51 = s2b("0");
    arrayOfByte37 = s2b("10");
    ExceptionInterceptor exceptionInterceptor4 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte12, arrayOfByte29, arrayOfByte53, arrayOfByte25, arrayOfByte16, arrayOfByte44, arrayOfByte2, arrayOfByte19, arrayOfByte31, arrayOfByte56, 
            arrayOfByte14, arrayOfByte39, arrayOfByte10, arrayOfByte23, arrayOfByte48, arrayOfByte35, arrayOfByte51, arrayOfByte37 }, exceptionInterceptor4));
    arrayOfByte12 = s2b("LONGTEXT");
    arrayOfByte16 = Integer.toString(-1).getBytes();
    arrayOfByte29 = Integer.toString(2147483647).getBytes();
    arrayOfByte53 = s2b("'");
    arrayOfByte44 = s2b("'");
    arrayOfByte23 = s2b("");
    arrayOfByte48 = Integer.toString(1).getBytes();
    arrayOfByte56 = s2b("false");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte37 = s2b("false");
    arrayOfByte2 = s2b("false");
    byte[] arrayOfByte6 = s2b("false");
    arrayOfByte51 = s2b("LONGTEXT");
    arrayOfByte19 = s2b("0");
    arrayOfByte35 = s2b("0");
    arrayOfByte10 = s2b("0");
    arrayOfByte31 = s2b("0");
    arrayOfByte25 = s2b("10");
    ExceptionInterceptor exceptionInterceptor25 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte12, arrayOfByte16, arrayOfByte29, arrayOfByte53, arrayOfByte44, arrayOfByte23, arrayOfByte48, arrayOfByte56, arrayOfByte14, arrayOfByte37, 
            arrayOfByte2, arrayOfByte6, arrayOfByte51, arrayOfByte19, arrayOfByte35, arrayOfByte10, arrayOfByte31, arrayOfByte25 }, exceptionInterceptor25));
    arrayOfByte29 = s2b("TEXT");
    arrayOfByte37 = Integer.toString(-1).getBytes();
    arrayOfByte23 = s2b("65535");
    arrayOfByte51 = s2b("'");
    arrayOfByte10 = s2b("'");
    arrayOfByte19 = s2b("");
    arrayOfByte25 = Integer.toString(1).getBytes();
    arrayOfByte35 = s2b("false");
    arrayOfByte6 = Integer.toString(3).getBytes();
    arrayOfByte12 = s2b("false");
    arrayOfByte56 = s2b("false");
    arrayOfByte2 = s2b("false");
    arrayOfByte16 = s2b("TEXT");
    arrayOfByte14 = s2b("0");
    arrayOfByte31 = s2b("0");
    byte[] arrayOfByte38 = s2b("0");
    arrayOfByte44 = s2b("0");
    arrayOfByte53 = s2b("10");
    ExceptionInterceptor exceptionInterceptor32 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte29, arrayOfByte37, arrayOfByte23, arrayOfByte51, arrayOfByte10, arrayOfByte19, arrayOfByte25, arrayOfByte35, arrayOfByte6, arrayOfByte12, 
            arrayOfByte56, arrayOfByte2, arrayOfByte16, arrayOfByte14, arrayOfByte31, arrayOfByte38, arrayOfByte44, arrayOfByte53 }, exceptionInterceptor32));
    arrayOfByte2 = s2b("TINYTEXT");
    arrayOfByte35 = Integer.toString(-1).getBytes();
    arrayOfByte10 = s2b("255");
    arrayOfByte12 = s2b("'");
    arrayOfByte53 = s2b("'");
    arrayOfByte29 = s2b("");
    arrayOfByte31 = Integer.toString(1).getBytes();
    arrayOfByte6 = s2b("false");
    arrayOfByte14 = Integer.toString(3).getBytes();
    arrayOfByte19 = s2b("false");
    arrayOfByte23 = s2b("false");
    arrayOfByte44 = s2b("false");
    arrayOfByte56 = s2b("TINYTEXT");
    byte[] arrayOfByte47 = s2b("0");
    arrayOfByte38 = s2b("0");
    arrayOfByte16 = s2b("0");
    arrayOfByte25 = s2b("0");
    arrayOfByte37 = s2b("10");
    ExceptionInterceptor exceptionInterceptor34 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte2, arrayOfByte35, arrayOfByte10, arrayOfByte12, arrayOfByte53, arrayOfByte29, arrayOfByte31, arrayOfByte6, arrayOfByte14, arrayOfByte19, 
            arrayOfByte23, arrayOfByte44, arrayOfByte56, arrayOfByte47, arrayOfByte38, arrayOfByte16, arrayOfByte25, arrayOfByte37 }, exceptionInterceptor34));
    arrayOfByte38 = s2b("CHAR");
    arrayOfByte6 = Integer.toString(1).getBytes();
    arrayOfByte47 = s2b("255");
    arrayOfByte37 = s2b("'");
    arrayOfByte23 = s2b("'");
    arrayOfByte10 = s2b("(M)");
    arrayOfByte29 = Integer.toString(1).getBytes();
    arrayOfByte12 = s2b("false");
    arrayOfByte35 = Integer.toString(3).getBytes();
    arrayOfByte16 = s2b("false");
    arrayOfByte19 = s2b("false");
    byte[] arrayOfByte50 = s2b("false");
    arrayOfByte14 = s2b("CHAR");
    arrayOfByte31 = s2b("0");
    arrayOfByte2 = s2b("0");
    arrayOfByte53 = s2b("0");
    arrayOfByte56 = s2b("0");
    arrayOfByte25 = s2b("10");
    ExceptionInterceptor exceptionInterceptor29 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte38, arrayOfByte6, arrayOfByte47, arrayOfByte37, arrayOfByte23, arrayOfByte10, arrayOfByte29, arrayOfByte12, arrayOfByte35, arrayOfByte16, 
            arrayOfByte19, arrayOfByte50, arrayOfByte14, arrayOfByte31, arrayOfByte2, arrayOfByte53, arrayOfByte56, arrayOfByte25 }, exceptionInterceptor29));
    char c = 'þ';
    if (this.conn.versionMeetsMinimum(5, 0, 3))
      if (this.conn.versionMeetsMinimum(5, 0, 6)) {
        c = 'A';
      } else {
        c = '@';
      }  
    arrayOfByte50 = s2b("NUMERIC");
    arrayOfByte53 = Integer.toString(2).getBytes();
    arrayOfByte37 = s2b(String.valueOf(c));
    arrayOfByte10 = s2b("");
    arrayOfByte38 = s2b("");
    arrayOfByte23 = s2b("[(M[,D])] [ZEROFILL]");
    arrayOfByte35 = Integer.toString(1).getBytes();
    arrayOfByte16 = s2b("false");
    arrayOfByte6 = Integer.toString(3).getBytes();
    arrayOfByte12 = s2b("false");
    arrayOfByte25 = s2b("false");
    arrayOfByte29 = s2b("true");
    arrayOfByte14 = s2b("NUMERIC");
    arrayOfByte2 = s2b("-308");
    byte[] arrayOfByte43 = s2b("308");
    arrayOfByte31 = s2b("0");
    arrayOfByte19 = s2b("0");
    arrayOfByte56 = s2b("10");
    ExceptionInterceptor exceptionInterceptor31 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte50, arrayOfByte53, arrayOfByte37, arrayOfByte10, arrayOfByte38, arrayOfByte23, arrayOfByte35, arrayOfByte16, arrayOfByte6, arrayOfByte12, 
            arrayOfByte25, arrayOfByte29, arrayOfByte14, arrayOfByte2, arrayOfByte43, arrayOfByte31, arrayOfByte19, arrayOfByte56 }, exceptionInterceptor31));
    arrayOfByte38 = s2b("DECIMAL");
    arrayOfByte43 = Integer.toString(3).getBytes();
    arrayOfByte10 = s2b(String.valueOf(c));
    arrayOfByte25 = s2b("");
    arrayOfByte12 = s2b("");
    arrayOfByte2 = s2b("[(M[,D])] [ZEROFILL]");
    arrayOfByte19 = Integer.toString(1).getBytes();
    arrayOfByte6 = s2b("false");
    arrayOfByte53 = Integer.toString(3).getBytes();
    arrayOfByte56 = s2b("false");
    arrayOfByte31 = s2b("false");
    arrayOfByte50 = s2b("true");
    arrayOfByte37 = s2b("DECIMAL");
    arrayOfByte35 = s2b("-308");
    arrayOfByte29 = s2b("308");
    arrayOfByte16 = s2b("0");
    arrayOfByte14 = s2b("0");
    byte[] arrayOfByte46 = s2b("10");
    ExceptionInterceptor exceptionInterceptor14 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte38, arrayOfByte43, arrayOfByte10, arrayOfByte25, arrayOfByte12, arrayOfByte2, arrayOfByte19, arrayOfByte6, arrayOfByte53, arrayOfByte56, 
            arrayOfByte31, arrayOfByte50, arrayOfByte37, arrayOfByte35, arrayOfByte29, arrayOfByte16, arrayOfByte14, arrayOfByte46 }, exceptionInterceptor14));
    arrayOfByte10 = s2b("INTEGER");
    arrayOfByte19 = Integer.toString(4).getBytes();
    arrayOfByte16 = s2b("10");
    arrayOfByte53 = s2b("");
    arrayOfByte35 = s2b("");
    arrayOfByte14 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte2 = Integer.toString(1).getBytes();
    arrayOfByte25 = s2b("false");
    arrayOfByte12 = Integer.toString(3).getBytes();
    arrayOfByte6 = s2b("true");
    arrayOfByte38 = s2b("false");
    arrayOfByte31 = s2b("true");
    arrayOfByte56 = s2b("INTEGER");
    arrayOfByte50 = s2b("0");
    arrayOfByte37 = s2b("0");
    arrayOfByte46 = s2b("0");
    arrayOfByte29 = s2b("0");
    byte[] arrayOfByte22 = s2b("10");
    ExceptionInterceptor exceptionInterceptor28 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte10, arrayOfByte19, arrayOfByte16, arrayOfByte53, arrayOfByte35, arrayOfByte14, arrayOfByte2, arrayOfByte25, arrayOfByte12, arrayOfByte6, 
            arrayOfByte38, arrayOfByte31, arrayOfByte56, arrayOfByte50, arrayOfByte37, arrayOfByte46, arrayOfByte29, arrayOfByte22 }, exceptionInterceptor28));
    arrayOfByte6 = s2b("INTEGER UNSIGNED");
    arrayOfByte35 = Integer.toString(4).getBytes();
    arrayOfByte29 = s2b("10");
    arrayOfByte25 = s2b("");
    arrayOfByte22 = s2b("");
    arrayOfByte46 = s2b("[(M)] [ZEROFILL]");
    byte[] arrayOfByte42 = Integer.toString(1).getBytes();
    arrayOfByte16 = s2b("false");
    arrayOfByte12 = Integer.toString(3).getBytes();
    arrayOfByte53 = s2b("true");
    arrayOfByte50 = s2b("false");
    arrayOfByte38 = s2b("true");
    arrayOfByte31 = s2b("INTEGER UNSIGNED");
    arrayOfByte37 = s2b("0");
    arrayOfByte56 = s2b("0");
    arrayOfByte19 = s2b("0");
    arrayOfByte14 = s2b("0");
    arrayOfByte2 = s2b("10");
    ExceptionInterceptor exceptionInterceptor6 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte6, arrayOfByte35, arrayOfByte29, arrayOfByte25, arrayOfByte22, arrayOfByte46, arrayOfByte42, arrayOfByte16, arrayOfByte12, arrayOfByte53, 
            arrayOfByte50, arrayOfByte38, arrayOfByte31, arrayOfByte37, arrayOfByte56, arrayOfByte19, arrayOfByte14, arrayOfByte2 }, exceptionInterceptor6));
    arrayOfByte38 = s2b("INT");
    arrayOfByte46 = Integer.toString(4).getBytes();
    arrayOfByte2 = s2b("10");
    arrayOfByte29 = s2b("");
    arrayOfByte25 = s2b("");
    arrayOfByte12 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte14 = Integer.toString(1).getBytes();
    byte[] arrayOfByte9 = s2b("false");
    arrayOfByte53 = Integer.toString(3).getBytes();
    arrayOfByte16 = s2b("true");
    arrayOfByte22 = s2b("false");
    arrayOfByte19 = s2b("true");
    arrayOfByte42 = s2b("INT");
    arrayOfByte56 = s2b("0");
    arrayOfByte35 = s2b("0");
    arrayOfByte37 = s2b("0");
    arrayOfByte6 = s2b("0");
    arrayOfByte50 = s2b("10");
    ExceptionInterceptor exceptionInterceptor20 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte38, arrayOfByte46, arrayOfByte2, arrayOfByte29, arrayOfByte25, arrayOfByte12, arrayOfByte14, arrayOfByte9, arrayOfByte53, arrayOfByte16, 
            arrayOfByte22, arrayOfByte19, arrayOfByte42, arrayOfByte56, arrayOfByte35, arrayOfByte37, arrayOfByte6, arrayOfByte50 }, exceptionInterceptor20));
    arrayOfByte50 = s2b("INT UNSIGNED");
    arrayOfByte2 = Integer.toString(4).getBytes();
    arrayOfByte19 = s2b("10");
    arrayOfByte9 = s2b("");
    arrayOfByte35 = s2b("");
    arrayOfByte53 = s2b("[(M)] [ZEROFILL]");
    arrayOfByte46 = Integer.toString(1).getBytes();
    arrayOfByte16 = s2b("false");
    arrayOfByte38 = Integer.toString(3).getBytes();
    arrayOfByte6 = s2b("true");
    arrayOfByte12 = s2b("false");
    arrayOfByte42 = s2b("true");
    arrayOfByte14 = s2b("INT UNSIGNED");
    byte[] arrayOfByte30 = s2b("0");
    arrayOfByte29 = s2b("0");
    arrayOfByte25 = s2b("0");
    arrayOfByte37 = s2b("0");
    arrayOfByte56 = s2b("10");
    ExceptionInterceptor exceptionInterceptor13 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte50, arrayOfByte2, arrayOfByte19, arrayOfByte9, arrayOfByte35, arrayOfByte53, arrayOfByte46, arrayOfByte16, arrayOfByte38, arrayOfByte6, 
            arrayOfByte12, arrayOfByte42, arrayOfByte14, arrayOfByte30, arrayOfByte29, arrayOfByte25, arrayOfByte37, arrayOfByte56 }, exceptionInterceptor13));
    arrayOfByte38 = s2b("MEDIUMINT");
    arrayOfByte29 = Integer.toString(4).getBytes();
    arrayOfByte35 = s2b("7");
    arrayOfByte14 = s2b("");
    arrayOfByte9 = s2b("");
    arrayOfByte6 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte53 = Integer.toString(1).getBytes();
    arrayOfByte37 = s2b("false");
    arrayOfByte50 = Integer.toString(3).getBytes();
    arrayOfByte16 = s2b("true");
    arrayOfByte2 = s2b("false");
    arrayOfByte56 = s2b("true");
    arrayOfByte42 = s2b("MEDIUMINT");
    arrayOfByte25 = s2b("0");
    arrayOfByte19 = s2b("0");
    arrayOfByte46 = s2b("0");
    arrayOfByte12 = s2b("0");
    arrayOfByte30 = s2b("10");
    exceptionInterceptor13 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte38, arrayOfByte29, arrayOfByte35, arrayOfByte14, arrayOfByte9, arrayOfByte6, arrayOfByte53, arrayOfByte37, arrayOfByte50, arrayOfByte16, 
            arrayOfByte2, arrayOfByte56, arrayOfByte42, arrayOfByte25, arrayOfByte19, arrayOfByte46, arrayOfByte12, arrayOfByte30 }, exceptionInterceptor13));
    arrayOfByte35 = s2b("MEDIUMINT UNSIGNED");
    arrayOfByte6 = Integer.toString(4).getBytes();
    arrayOfByte12 = s2b("8");
    byte[] arrayOfByte21 = s2b("");
    arrayOfByte50 = s2b("");
    arrayOfByte29 = s2b("[(M)] [ZEROFILL]");
    arrayOfByte19 = Integer.toString(1).getBytes();
    arrayOfByte46 = s2b("false");
    arrayOfByte37 = Integer.toString(3).getBytes();
    arrayOfByte53 = s2b("true");
    arrayOfByte2 = s2b("false");
    arrayOfByte42 = s2b("true");
    arrayOfByte14 = s2b("MEDIUMINT UNSIGNED");
    arrayOfByte30 = s2b("0");
    arrayOfByte38 = s2b("0");
    arrayOfByte9 = s2b("0");
    arrayOfByte25 = s2b("0");
    arrayOfByte16 = s2b("10");
    ExceptionInterceptor exceptionInterceptor37 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte35, arrayOfByte6, arrayOfByte12, arrayOfByte21, arrayOfByte50, arrayOfByte29, arrayOfByte19, arrayOfByte46, arrayOfByte37, arrayOfByte53, 
            arrayOfByte2, arrayOfByte42, arrayOfByte14, arrayOfByte30, arrayOfByte38, arrayOfByte9, arrayOfByte25, arrayOfByte16 }, exceptionInterceptor37));
    arrayOfByte42 = s2b("SMALLINT");
    arrayOfByte14 = Integer.toString(5).getBytes();
    byte[] arrayOfByte55 = s2b("5");
    arrayOfByte46 = s2b("");
    arrayOfByte12 = s2b("");
    arrayOfByte9 = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    arrayOfByte2 = Integer.toString(1).getBytes();
    arrayOfByte19 = s2b("false");
    arrayOfByte38 = Integer.toString(3).getBytes();
    arrayOfByte50 = s2b("true");
    arrayOfByte25 = s2b("false");
    arrayOfByte30 = s2b("true");
    arrayOfByte6 = s2b("SMALLINT");
    arrayOfByte29 = s2b("0");
    arrayOfByte21 = s2b("0");
    arrayOfByte37 = s2b("0");
    arrayOfByte35 = s2b("0");
    arrayOfByte53 = s2b("10");
    ExceptionInterceptor exceptionInterceptor9 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte42, arrayOfByte14, arrayOfByte55, arrayOfByte46, arrayOfByte12, arrayOfByte9, arrayOfByte2, arrayOfByte19, arrayOfByte38, arrayOfByte50, 
            arrayOfByte25, arrayOfByte30, arrayOfByte6, arrayOfByte29, arrayOfByte21, arrayOfByte37, arrayOfByte35, arrayOfByte53 }, exceptionInterceptor9));
    arrayOfByte21 = s2b("SMALLINT UNSIGNED");
    byte[] arrayOfByte15 = Integer.toString(5).getBytes();
    arrayOfByte9 = s2b("5");
    arrayOfByte25 = s2b("");
    arrayOfByte55 = s2b("");
    arrayOfByte42 = s2b("[(M)] [ZEROFILL]");
    arrayOfByte30 = Integer.toString(1).getBytes();
    arrayOfByte37 = s2b("false");
    arrayOfByte12 = Integer.toString(3).getBytes();
    arrayOfByte46 = s2b("true");
    arrayOfByte50 = s2b("false");
    arrayOfByte29 = s2b("true");
    arrayOfByte2 = s2b("SMALLINT UNSIGNED");
    arrayOfByte38 = s2b("0");
    arrayOfByte35 = s2b("0");
    arrayOfByte53 = s2b("0");
    arrayOfByte6 = s2b("0");
    arrayOfByte19 = s2b("10");
    ExceptionInterceptor exceptionInterceptor8 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte21, arrayOfByte15, arrayOfByte9, arrayOfByte25, arrayOfByte55, arrayOfByte42, arrayOfByte30, arrayOfByte37, arrayOfByte12, arrayOfByte46, 
            arrayOfByte50, arrayOfByte29, arrayOfByte2, arrayOfByte38, arrayOfByte35, arrayOfByte53, arrayOfByte6, arrayOfByte19 }, exceptionInterceptor8));
    arrayOfByte55 = s2b("FLOAT");
    arrayOfByte30 = Integer.toString(7).getBytes();
    arrayOfByte35 = s2b("10");
    arrayOfByte42 = s2b("");
    arrayOfByte50 = s2b("");
    arrayOfByte19 = s2b("[(M,D)] [ZEROFILL]");
    arrayOfByte46 = Integer.toString(1).getBytes();
    arrayOfByte38 = s2b("false");
    arrayOfByte15 = Integer.toString(3).getBytes();
    arrayOfByte2 = s2b("false");
    arrayOfByte9 = s2b("false");
    arrayOfByte53 = s2b("true");
    arrayOfByte12 = s2b("FLOAT");
    arrayOfByte29 = s2b("-38");
    byte[] arrayOfByte13 = s2b("38");
    arrayOfByte25 = s2b("0");
    arrayOfByte21 = s2b("0");
    arrayOfByte6 = s2b("10");
    ExceptionInterceptor exceptionInterceptor24 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte55, arrayOfByte30, arrayOfByte35, arrayOfByte42, arrayOfByte50, arrayOfByte19, arrayOfByte46, arrayOfByte38, arrayOfByte15, arrayOfByte2, 
            arrayOfByte9, arrayOfByte53, arrayOfByte12, arrayOfByte29, arrayOfByte13, arrayOfByte25, arrayOfByte21, arrayOfByte6 }, exceptionInterceptor24));
    arrayOfByte9 = s2b("DOUBLE");
    arrayOfByte6 = Integer.toString(8).getBytes();
    arrayOfByte35 = s2b("17");
    arrayOfByte29 = s2b("");
    arrayOfByte2 = s2b("");
    arrayOfByte30 = s2b("[(M,D)] [ZEROFILL]");
    arrayOfByte13 = Integer.toString(1).getBytes();
    arrayOfByte19 = s2b("false");
    byte[] arrayOfByte36 = Integer.toString(3).getBytes();
    arrayOfByte15 = s2b("false");
    arrayOfByte12 = s2b("false");
    arrayOfByte55 = s2b("true");
    arrayOfByte50 = s2b("DOUBLE");
    arrayOfByte53 = s2b("-308");
    arrayOfByte21 = s2b("308");
    arrayOfByte46 = s2b("0");
    arrayOfByte42 = s2b("0");
    arrayOfByte38 = s2b("10");
    ExceptionInterceptor exceptionInterceptor16 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte9, arrayOfByte6, arrayOfByte35, arrayOfByte29, arrayOfByte2, arrayOfByte30, arrayOfByte13, arrayOfByte19, arrayOfByte36, arrayOfByte15, 
            arrayOfByte12, arrayOfByte55, arrayOfByte50, arrayOfByte53, arrayOfByte21, arrayOfByte46, arrayOfByte42, arrayOfByte38 }, exceptionInterceptor16));
    arrayOfByte36 = s2b("DOUBLE PRECISION");
    arrayOfByte53 = Integer.toString(8).getBytes();
    arrayOfByte12 = s2b("17");
    arrayOfByte21 = s2b("");
    arrayOfByte55 = s2b("");
    arrayOfByte50 = s2b("[(M,D)] [ZEROFILL]");
    arrayOfByte15 = Integer.toString(1).getBytes();
    arrayOfByte13 = s2b("false");
    arrayOfByte46 = Integer.toString(3).getBytes();
    arrayOfByte35 = s2b("false");
    arrayOfByte19 = s2b("false");
    arrayOfByte9 = s2b("true");
    arrayOfByte30 = s2b("DOUBLE PRECISION");
    arrayOfByte42 = s2b("-308");
    arrayOfByte38 = s2b("308");
    byte[] arrayOfByte24 = s2b("0");
    arrayOfByte2 = s2b("0");
    arrayOfByte29 = s2b("10");
    ExceptionInterceptor exceptionInterceptor3 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte36, arrayOfByte53, arrayOfByte12, arrayOfByte21, arrayOfByte55, arrayOfByte50, arrayOfByte15, arrayOfByte13, arrayOfByte46, arrayOfByte35, 
            arrayOfByte19, arrayOfByte9, arrayOfByte30, arrayOfByte42, arrayOfByte38, arrayOfByte24, arrayOfByte2, arrayOfByte29 }, exceptionInterceptor3));
    arrayOfByte13 = s2b("REAL");
    arrayOfByte2 = Integer.toString(8).getBytes();
    arrayOfByte15 = s2b("17");
    arrayOfByte24 = s2b("");
    byte[] arrayOfByte5 = s2b("");
    arrayOfByte55 = s2b("[(M,D)] [ZEROFILL]");
    arrayOfByte12 = Integer.toString(1).getBytes();
    arrayOfByte30 = s2b("false");
    arrayOfByte35 = Integer.toString(3).getBytes();
    arrayOfByte36 = s2b("false");
    arrayOfByte21 = s2b("false");
    arrayOfByte38 = s2b("true");
    arrayOfByte19 = s2b("REAL");
    arrayOfByte53 = s2b("-308");
    arrayOfByte9 = s2b("308");
    arrayOfByte50 = s2b("0");
    arrayOfByte29 = s2b("0");
    arrayOfByte46 = s2b("10");
    ExceptionInterceptor exceptionInterceptor27 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte13, arrayOfByte2, arrayOfByte15, arrayOfByte24, arrayOfByte5, arrayOfByte55, arrayOfByte12, arrayOfByte30, arrayOfByte35, arrayOfByte36, 
            arrayOfByte21, arrayOfByte38, arrayOfByte19, arrayOfByte53, arrayOfByte9, arrayOfByte50, arrayOfByte29, arrayOfByte46 }, exceptionInterceptor27));
    arrayOfByte9 = s2b("VARCHAR");
    arrayOfByte5 = Integer.toString(12).getBytes();
    if (this.conn.versionMeetsMinimum(5, 0, 3)) {
      str1 = "65535";
    } else {
      str1 = "255";
    } 
    arrayOfByte13 = s2b(str1);
    arrayOfByte55 = s2b("'");
    arrayOfByte53 = s2b("'");
    arrayOfByte24 = s2b("(M)");
    byte[] arrayOfByte1 = Integer.toString(1).getBytes();
    arrayOfByte35 = s2b("false");
    arrayOfByte15 = Integer.toString(3).getBytes();
    arrayOfByte21 = s2b("false");
    arrayOfByte19 = s2b("false");
    arrayOfByte12 = s2b("false");
    arrayOfByte38 = s2b("VARCHAR");
    arrayOfByte46 = s2b("0");
    byte[] arrayOfByte41 = s2b("0");
    arrayOfByte36 = s2b("0");
    arrayOfByte30 = s2b("0");
    arrayOfByte29 = s2b("10");
    ExceptionInterceptor exceptionInterceptor33 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte9, arrayOfByte5, arrayOfByte13, arrayOfByte55, arrayOfByte53, arrayOfByte24, arrayOfByte1, arrayOfByte35, arrayOfByte15, arrayOfByte21, 
            arrayOfByte19, arrayOfByte12, arrayOfByte38, arrayOfByte46, arrayOfByte41, arrayOfByte36, arrayOfByte30, arrayOfByte29 }, exceptionInterceptor33));
    arrayOfByte35 = s2b("ENUM");
    arrayOfByte1 = Integer.toString(12).getBytes();
    arrayOfByte24 = s2b("65535");
    arrayOfByte38 = s2b("'");
    arrayOfByte5 = s2b("'");
    arrayOfByte15 = s2b("");
    arrayOfByte30 = Integer.toString(1).getBytes();
    arrayOfByte9 = s2b("false");
    arrayOfByte53 = Integer.toString(3).getBytes();
    arrayOfByte36 = s2b("false");
    arrayOfByte41 = s2b("false");
    arrayOfByte55 = s2b("false");
    arrayOfByte46 = s2b("ENUM");
    byte[] arrayOfByte49 = s2b("0");
    arrayOfByte19 = s2b("0");
    arrayOfByte21 = s2b("0");
    arrayOfByte12 = s2b("0");
    arrayOfByte13 = s2b("10");
    ExceptionInterceptor exceptionInterceptor19 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte35, arrayOfByte1, arrayOfByte24, arrayOfByte38, arrayOfByte5, arrayOfByte15, arrayOfByte30, arrayOfByte9, arrayOfByte53, arrayOfByte36, 
            arrayOfByte41, arrayOfByte55, arrayOfByte46, arrayOfByte49, arrayOfByte19, arrayOfByte21, arrayOfByte12, arrayOfByte13 }, exceptionInterceptor19));
    arrayOfByte9 = s2b("SET");
    arrayOfByte12 = Integer.toString(12).getBytes();
    arrayOfByte36 = s2b("64");
    arrayOfByte1 = s2b("'");
    arrayOfByte55 = s2b("'");
    arrayOfByte46 = s2b("");
    arrayOfByte21 = Integer.toString(1).getBytes();
    arrayOfByte19 = s2b("false");
    arrayOfByte35 = Integer.toString(3).getBytes();
    arrayOfByte5 = s2b("false");
    arrayOfByte38 = s2b("false");
    arrayOfByte53 = s2b("false");
    arrayOfByte30 = s2b("SET");
    arrayOfByte49 = s2b("0");
    arrayOfByte15 = s2b("0");
    arrayOfByte41 = s2b("0");
    arrayOfByte13 = s2b("0");
    arrayOfByte24 = s2b("10");
    exceptionInterceptor19 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte9, arrayOfByte12, arrayOfByte36, arrayOfByte1, arrayOfByte55, arrayOfByte46, arrayOfByte21, arrayOfByte19, arrayOfByte35, arrayOfByte5, 
            arrayOfByte38, arrayOfByte53, arrayOfByte30, arrayOfByte49, arrayOfByte15, arrayOfByte41, arrayOfByte13, arrayOfByte24 }, exceptionInterceptor19));
    arrayOfByte13 = s2b("DATE");
    arrayOfByte55 = Integer.toString(91).getBytes();
    arrayOfByte38 = s2b("0");
    arrayOfByte12 = s2b("'");
    arrayOfByte53 = s2b("'");
    arrayOfByte35 = s2b("");
    arrayOfByte5 = Integer.toString(1).getBytes();
    arrayOfByte15 = s2b("false");
    arrayOfByte24 = Integer.toString(3).getBytes();
    arrayOfByte21 = s2b("false");
    arrayOfByte9 = s2b("false");
    arrayOfByte41 = s2b("false");
    arrayOfByte46 = s2b("DATE");
    arrayOfByte36 = s2b("0");
    arrayOfByte1 = s2b("0");
    byte[] arrayOfByte28 = s2b("0");
    arrayOfByte49 = s2b("0");
    arrayOfByte30 = s2b("10");
    ExceptionInterceptor exceptionInterceptor11 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte13, arrayOfByte55, arrayOfByte38, arrayOfByte12, arrayOfByte53, arrayOfByte35, arrayOfByte5, arrayOfByte15, arrayOfByte24, arrayOfByte21, 
            arrayOfByte9, arrayOfByte41, arrayOfByte46, arrayOfByte36, arrayOfByte1, arrayOfByte28, arrayOfByte49, arrayOfByte30 }, exceptionInterceptor11));
    arrayOfByte15 = s2b("TIME");
    arrayOfByte55 = Integer.toString(92).getBytes();
    arrayOfByte1 = s2b("0");
    arrayOfByte49 = s2b("'");
    arrayOfByte5 = s2b("'");
    arrayOfByte38 = s2b("");
    arrayOfByte41 = Integer.toString(1).getBytes();
    arrayOfByte28 = s2b("false");
    arrayOfByte21 = Integer.toString(3).getBytes();
    arrayOfByte53 = s2b("false");
    arrayOfByte9 = s2b("false");
    arrayOfByte35 = s2b("false");
    arrayOfByte12 = s2b("TIME");
    arrayOfByte13 = s2b("0");
    arrayOfByte36 = s2b("0");
    arrayOfByte30 = s2b("0");
    byte[] arrayOfByte18 = s2b("0");
    arrayOfByte24 = s2b("10");
    ExceptionInterceptor exceptionInterceptor30 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte15, arrayOfByte55, arrayOfByte1, arrayOfByte49, arrayOfByte5, arrayOfByte38, arrayOfByte41, arrayOfByte28, arrayOfByte21, arrayOfByte53, 
            arrayOfByte9, arrayOfByte35, arrayOfByte12, arrayOfByte13, arrayOfByte36, arrayOfByte30, arrayOfByte18, arrayOfByte24 }, exceptionInterceptor30));
    arrayOfByte41 = s2b("DATETIME");
    arrayOfByte1 = Integer.toString(93).getBytes();
    arrayOfByte36 = s2b("0");
    arrayOfByte53 = s2b("'");
    arrayOfByte15 = s2b("'");
    arrayOfByte21 = s2b("");
    arrayOfByte9 = Integer.toString(1).getBytes();
    byte[] arrayOfByte45 = s2b("false");
    arrayOfByte18 = Integer.toString(3).getBytes();
    arrayOfByte55 = s2b("false");
    arrayOfByte38 = s2b("false");
    arrayOfByte12 = s2b("false");
    arrayOfByte35 = s2b("DATETIME");
    arrayOfByte28 = s2b("0");
    arrayOfByte30 = s2b("0");
    arrayOfByte13 = s2b("0");
    arrayOfByte49 = s2b("0");
    arrayOfByte24 = s2b("10");
    ExceptionInterceptor exceptionInterceptor2 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte41, arrayOfByte1, arrayOfByte36, arrayOfByte53, arrayOfByte15, arrayOfByte21, arrayOfByte9, arrayOfByte45, arrayOfByte18, arrayOfByte55, 
            arrayOfByte38, arrayOfByte12, arrayOfByte35, arrayOfByte28, arrayOfByte30, arrayOfByte13, arrayOfByte49, arrayOfByte24 }, exceptionInterceptor2));
    arrayOfByte41 = s2b("TIMESTAMP");
    arrayOfByte35 = Integer.toString(93).getBytes();
    arrayOfByte38 = s2b("0");
    arrayOfByte30 = s2b("'");
    arrayOfByte1 = s2b("'");
    arrayOfByte49 = s2b("[(M)]");
    arrayOfByte55 = Integer.toString(1).getBytes();
    arrayOfByte15 = s2b("false");
    arrayOfByte9 = Integer.toString(3).getBytes();
    arrayOfByte36 = s2b("false");
    arrayOfByte12 = s2b("false");
    arrayOfByte28 = s2b("false");
    arrayOfByte53 = s2b("TIMESTAMP");
    byte[] arrayOfByte4 = s2b("0");
    arrayOfByte21 = s2b("0");
    arrayOfByte13 = s2b("0");
    arrayOfByte18 = s2b("0");
    arrayOfByte45 = s2b("10");
    ExceptionInterceptor exceptionInterceptor15 = getExceptionInterceptor();
    arrayList.add(new ByteArrayRow(new byte[][] { 
            arrayOfByte41, arrayOfByte35, arrayOfByte38, arrayOfByte30, arrayOfByte1, arrayOfByte49, arrayOfByte55, arrayOfByte15, arrayOfByte9, arrayOfByte36, 
            arrayOfByte12, arrayOfByte28, arrayOfByte53, arrayOfByte4, arrayOfByte21, arrayOfByte13, arrayOfByte18, arrayOfByte45 }, exceptionInterceptor15));
    return buildResultSet(new Field[] { 
          field11, field10, field1, field16, field7, field12, field2, field5, field8, field3, 
          field6, field4, field9, field13, field15, field17, field18, field14 }, (ArrayList)arrayList);
  }
  
  public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) throws SQLException {
    Field field5 = new Field("", "TYPE_CAT", 12, 32);
    Field field3 = new Field("", "TYPE_SCHEM", 12, 32);
    Field field2 = new Field("", "TYPE_NAME", 12, 32);
    Field field4 = new Field("", "CLASS_NAME", 12, 32);
    Field field7 = new Field("", "DATA_TYPE", 4, 10);
    Field field6 = new Field("", "REMARKS", 12, 32);
    Field field1 = new Field("", "BASE_TYPE", 5, 10);
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    return buildResultSet(new Field[] { field5, field3, field2, field4, field7, field6, field1 }, arrayList);
  }
  
  public String getURL() throws SQLException {
    return this.conn.getURL();
  }
  
  public String getUserName() throws SQLException {
    if (this.conn.getUseHostsInPrivileges()) {
      Statement statement;
      Exception exception;
      ResultSet resultSet2 = null;
      ResultSet resultSet1 = null;
      try {
        statement = this.conn.getMetadataSafeStatement();
      } finally {
        resultSet1 = null;
      } 
      if (exception != null)
        try {
          exception.close();
        } catch (Exception exception1) {
          AssertionFailedException.shouldNotHappen(exception1);
        }  
      if (statement != null)
        try {
          statement.close();
        } catch (Exception exception1) {
          AssertionFailedException.shouldNotHappen(exception1);
        }  
      throw resultSet1;
    } 
    return this.conn.getUser();
  }
  
  public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) throws SQLException {
    if (paramString3 != null) {
      Field field2 = new Field("", "SCOPE", 5, 5);
      Field field5 = new Field("", "COLUMN_NAME", 1, 32);
      Field field1 = new Field("", "DATA_TYPE", 4, 5);
      Field field6 = new Field("", "TYPE_NAME", 1, 16);
      Field field7 = new Field("", "COLUMN_SIZE", 4, 16);
      Field field8 = new Field("", "BUFFER_LENGTH", 4, 16);
      Field field4 = new Field("", "DECIMAL_DIGITS", 5, 16);
      Field field3 = new Field("", "PSEUDO_COLUMN", 5, 5);
      ArrayList<ResultSetRow> arrayList = new ArrayList();
      Statement statement = this.conn.getMetadataSafeStatement();
      try {
        IterateBlock<String> iterateBlock = new IterateBlock<String>() {
            public final DatabaseMetaData this$0;
            
            public final ArrayList val$rows;
            
            public final Statement val$stmt;
            
            public final String val$table;
            
            public void forEach(String param1String) throws SQLException {
              // Byte code:
              //   0: aload_0
              //   1: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   4: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
              //   7: iconst_5
              //   8: iconst_0
              //   9: iconst_0
              //   10: invokeinterface versionMeetsMinimum : (III)Z
              //   15: istore #6
              //   17: aconst_null
              //   18: astore #10
              //   20: aconst_null
              //   21: astore #9
              //   23: aload #9
              //   25: astore #8
              //   27: new java/lang/StringBuilder
              //   30: astore #12
              //   32: aload #9
              //   34: astore #8
              //   36: aload #12
              //   38: ldc ' Extra LIKE '%on update CURRENT_TIMESTAMP%''
              //   40: invokespecial <init> : (Ljava/lang/String;)V
              //   43: aload #9
              //   45: astore #8
              //   47: new java/util/ArrayList
              //   50: astore #14
              //   52: aload #9
              //   54: astore #8
              //   56: aload #14
              //   58: invokespecial <init> : ()V
              //   61: aload #9
              //   63: astore #8
              //   65: aload_0
              //   66: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   69: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
              //   72: iconst_5
              //   73: iconst_1
              //   74: bipush #23
              //   76: invokeinterface versionMeetsMinimum : (III)Z
              //   81: istore #7
              //   83: iconst_2
              //   84: istore #4
              //   86: iload #7
              //   88: ifne -> 572
              //   91: aload #9
              //   93: astore #8
              //   95: new java/lang/StringBuilder
              //   98: astore #13
              //   100: aload #9
              //   102: astore #8
              //   104: aload #13
              //   106: invokespecial <init> : ()V
              //   109: aload #9
              //   111: astore #8
              //   113: new java/lang/StringBuilder
              //   116: astore #11
              //   118: aload #9
              //   120: astore #8
              //   122: aload #11
              //   124: ldc 'SHOW CREATE TABLE '
              //   126: invokespecial <init> : (Ljava/lang/String;)V
              //   129: aload #9
              //   131: astore #8
              //   133: aload #11
              //   135: aload_0
              //   136: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   139: aload_1
              //   140: aload_0
              //   141: getfield val$table : Ljava/lang/String;
              //   144: invokevirtual getFullyQualifiedName : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
              //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   150: pop
              //   151: aload #9
              //   153: astore #8
              //   155: aload #11
              //   157: invokevirtual toString : ()Ljava/lang/String;
              //   160: astore #11
              //   162: aload #9
              //   164: astore #8
              //   166: aload_0
              //   167: getfield val$stmt : Ljava/sql/Statement;
              //   170: aload #11
              //   172: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
              //   177: astore #11
              //   179: iconst_1
              //   180: istore_3
              //   181: aload #13
              //   183: astore #12
              //   185: aload #11
              //   187: astore #8
              //   189: aload #11
              //   191: astore #10
              //   193: aload #11
              //   195: astore #9
              //   197: aload #11
              //   199: invokeinterface next : ()Z
              //   204: ifeq -> 575
              //   207: aload #11
              //   209: astore #10
              //   211: aload #11
              //   213: astore #9
              //   215: aload #11
              //   217: iload #4
              //   219: invokeinterface getString : (I)Ljava/lang/String;
              //   224: astore #12
              //   226: aload #11
              //   228: astore #10
              //   230: aload #11
              //   232: astore #9
              //   234: new java/util/StringTokenizer
              //   237: astore #8
              //   239: aload #11
              //   241: astore #10
              //   243: aload #11
              //   245: astore #9
              //   247: aload #8
              //   249: aload #12
              //   251: ldc '\\n'
              //   253: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
              //   256: iload_3
              //   257: istore_2
              //   258: iload #4
              //   260: istore_3
              //   261: iload_3
              //   262: istore #4
              //   264: iload_2
              //   265: istore_3
              //   266: aload #11
              //   268: astore #10
              //   270: aload #11
              //   272: astore #9
              //   274: aload #8
              //   276: invokevirtual hasMoreTokens : ()Z
              //   279: ifeq -> 181
              //   282: aload #11
              //   284: astore #10
              //   286: aload #11
              //   288: astore #9
              //   290: aload #8
              //   292: invokevirtual nextToken : ()Ljava/lang/String;
              //   295: invokevirtual trim : ()Ljava/lang/String;
              //   298: astore #12
              //   300: iload_2
              //   301: istore #4
              //   303: aload #11
              //   305: astore #10
              //   307: aload #11
              //   309: astore #9
              //   311: aload #12
              //   313: ldc 'on update CURRENT_TIMESTAMP'
              //   315: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
              //   318: iconst_m1
              //   319: if_icmple -> 564
              //   322: aload #11
              //   324: astore #10
              //   326: aload #11
              //   328: astore #9
              //   330: aload #12
              //   332: aload_0
              //   333: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   336: getfield quotedId : Ljava/lang/String;
              //   339: invokevirtual indexOf : (Ljava/lang/String;)I
              //   342: istore_3
              //   343: iload_3
              //   344: iconst_m1
              //   345: if_icmpne -> 370
              //   348: aload #11
              //   350: astore #10
              //   352: aload #11
              //   354: astore #9
              //   356: aload #12
              //   358: ldc '"'
              //   360: invokevirtual indexOf : (Ljava/lang/String;)I
              //   363: istore_3
              //   364: iconst_0
              //   365: istore #5
              //   367: goto -> 373
              //   370: iconst_1
              //   371: istore #5
              //   373: iload_2
              //   374: istore #4
              //   376: iload_3
              //   377: iconst_m1
              //   378: if_icmpeq -> 564
              //   381: iload #5
              //   383: ifeq -> 414
              //   386: aload #11
              //   388: astore #10
              //   390: aload #11
              //   392: astore #9
              //   394: aload #12
              //   396: aload_0
              //   397: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   400: getfield quotedId : Ljava/lang/String;
              //   403: iload_3
              //   404: iconst_1
              //   405: iadd
              //   406: invokevirtual indexOf : (Ljava/lang/String;I)I
              //   409: istore #5
              //   411: goto -> 434
              //   414: aload #11
              //   416: astore #10
              //   418: aload #11
              //   420: astore #9
              //   422: aload #12
              //   424: ldc '"'
              //   426: iload_3
              //   427: iconst_1
              //   428: iadd
              //   429: invokevirtual indexOf : (Ljava/lang/String;I)I
              //   432: istore #5
              //   434: iload_2
              //   435: istore #4
              //   437: iload #5
              //   439: iconst_m1
              //   440: if_icmpeq -> 564
              //   443: iload #6
              //   445: ifeq -> 535
              //   448: iload_2
              //   449: ifne -> 471
              //   452: aload #11
              //   454: astore #10
              //   456: aload #11
              //   458: astore #9
              //   460: aload #13
              //   462: ldc ' or'
              //   464: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   467: pop
              //   468: goto -> 473
              //   471: iconst_0
              //   472: istore_2
              //   473: aload #11
              //   475: astore #10
              //   477: aload #11
              //   479: astore #9
              //   481: aload #13
              //   483: ldc ' Field=''
              //   485: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   488: pop
              //   489: aload #11
              //   491: astore #10
              //   493: aload #11
              //   495: astore #9
              //   497: aload #13
              //   499: aload #12
              //   501: iload_3
              //   502: iconst_1
              //   503: iadd
              //   504: iload #5
              //   506: invokevirtual substring : (II)Ljava/lang/String;
              //   509: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   512: pop
              //   513: aload #11
              //   515: astore #10
              //   517: aload #11
              //   519: astore #9
              //   521: aload #13
              //   523: ldc '''
              //   525: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   528: pop
              //   529: iload_2
              //   530: istore #4
              //   532: goto -> 564
              //   535: aload #11
              //   537: astore #10
              //   539: aload #11
              //   541: astore #9
              //   543: aload #14
              //   545: aload #12
              //   547: iload_3
              //   548: iconst_1
              //   549: iadd
              //   550: iload #5
              //   552: invokevirtual substring : (II)Ljava/lang/String;
              //   555: invokeinterface add : (Ljava/lang/Object;)Z
              //   560: pop
              //   561: iload_2
              //   562: istore #4
              //   564: iconst_2
              //   565: istore_3
              //   566: iload #4
              //   568: istore_2
              //   569: goto -> 261
              //   572: aconst_null
              //   573: astore #8
              //   575: aload #8
              //   577: astore #10
              //   579: aload #8
              //   581: astore #9
              //   583: aload #12
              //   585: invokevirtual length : ()I
              //   588: ifgt -> 613
              //   591: aload #8
              //   593: astore #11
              //   595: aload #8
              //   597: astore #10
              //   599: aload #8
              //   601: astore #9
              //   603: aload #14
              //   605: invokeinterface size : ()I
              //   610: ifle -> 1217
              //   613: aload #8
              //   615: astore #10
              //   617: aload #8
              //   619: astore #9
              //   621: new java/lang/StringBuilder
              //   624: astore #11
              //   626: aload #8
              //   628: astore #10
              //   630: aload #8
              //   632: astore #9
              //   634: aload #11
              //   636: ldc 'SHOW COLUMNS FROM '
              //   638: invokespecial <init> : (Ljava/lang/String;)V
              //   641: aload #8
              //   643: astore #10
              //   645: aload #8
              //   647: astore #9
              //   649: aload_0
              //   650: getfield val$table : Ljava/lang/String;
              //   653: astore #15
              //   655: aload #8
              //   657: astore #10
              //   659: aload #8
              //   661: astore #9
              //   663: aload_0
              //   664: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   667: astore #13
              //   669: aload #8
              //   671: astore #10
              //   673: aload #8
              //   675: astore #9
              //   677: aload #11
              //   679: aload #15
              //   681: aload #13
              //   683: getfield quotedId : Ljava/lang/String;
              //   686: aload #13
              //   688: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
              //   691: invokeinterface getPedantic : ()Z
              //   696: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
              //   699: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   702: pop
              //   703: aload #8
              //   705: astore #10
              //   707: aload #8
              //   709: astore #9
              //   711: aload #11
              //   713: ldc ' FROM '
              //   715: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   718: pop
              //   719: aload #8
              //   721: astore #10
              //   723: aload #8
              //   725: astore #9
              //   727: aload_0
              //   728: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   731: astore #13
              //   733: aload #8
              //   735: astore #10
              //   737: aload #8
              //   739: astore #9
              //   741: aload #11
              //   743: aload_1
              //   744: aload #13
              //   746: getfield quotedId : Ljava/lang/String;
              //   749: aload #13
              //   751: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
              //   754: invokeinterface getPedantic : ()Z
              //   759: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
              //   762: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   765: pop
              //   766: iload #6
              //   768: ifeq -> 806
              //   771: aload #8
              //   773: astore #10
              //   775: aload #8
              //   777: astore #9
              //   779: aload #11
              //   781: ldc ' WHERE'
              //   783: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   786: pop
              //   787: aload #8
              //   789: astore #10
              //   791: aload #8
              //   793: astore #9
              //   795: aload #11
              //   797: aload #12
              //   799: invokevirtual toString : ()Ljava/lang/String;
              //   802: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   805: pop
              //   806: aload #8
              //   808: astore #10
              //   810: aload #8
              //   812: astore #9
              //   814: aload_0
              //   815: getfield val$stmt : Ljava/sql/Statement;
              //   818: aload #11
              //   820: invokevirtual toString : ()Ljava/lang/String;
              //   823: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
              //   828: astore_1
              //   829: aload_1
              //   830: astore #10
              //   832: aload_1
              //   833: astore #9
              //   835: aload_1
              //   836: invokeinterface next : ()Z
              //   841: istore #7
              //   843: aload_1
              //   844: astore #11
              //   846: iload #7
              //   848: ifeq -> 1217
              //   851: iload #6
              //   853: ifne -> 880
              //   856: aload_1
              //   857: astore #10
              //   859: aload_1
              //   860: astore #9
              //   862: aload #14
              //   864: aload_1
              //   865: ldc 'Field'
              //   867: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
              //   872: invokeinterface contains : (Ljava/lang/Object;)Z
              //   877: ifeq -> 829
              //   880: aload_1
              //   881: astore #10
              //   883: aload_1
              //   884: astore #9
              //   886: new com/mysql/jdbc/DatabaseMetaData$TypeDescriptor
              //   889: astore #11
              //   891: aload_1
              //   892: astore #10
              //   894: aload_1
              //   895: astore #9
              //   897: aload #11
              //   899: aload_0
              //   900: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   903: aload_1
              //   904: ldc 'Type'
              //   906: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
              //   911: aload_1
              //   912: ldc 'Null'
              //   914: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
              //   919: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;)V
              //   922: aload_1
              //   923: astore #10
              //   925: aload_1
              //   926: astore #9
              //   928: aload_1
              //   929: ldc 'Field'
              //   931: invokeinterface getBytes : (Ljava/lang/String;)[B
              //   936: astore #13
              //   938: aload_1
              //   939: astore #10
              //   941: aload_1
              //   942: astore #9
              //   944: aload #11
              //   946: getfield dataType : S
              //   949: invokestatic toString : (S)Ljava/lang/String;
              //   952: invokevirtual getBytes : ()[B
              //   955: astore #12
              //   957: aload_1
              //   958: astore #10
              //   960: aload_1
              //   961: astore #9
              //   963: aload_0
              //   964: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   967: aload #11
              //   969: getfield typeName : Ljava/lang/String;
              //   972: invokevirtual s2b : (Ljava/lang/String;)[B
              //   975: astore #15
              //   977: aload_1
              //   978: astore #10
              //   980: aload_1
              //   981: astore #9
              //   983: aload #11
              //   985: getfield columnSize : Ljava/lang/Integer;
              //   988: astore #8
              //   990: aload #8
              //   992: ifnonnull -> 1001
              //   995: aconst_null
              //   996: astore #8
              //   998: goto -> 1021
              //   1001: aload_1
              //   1002: astore #10
              //   1004: aload_1
              //   1005: astore #9
              //   1007: aload_0
              //   1008: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   1011: aload #8
              //   1013: invokevirtual toString : ()Ljava/lang/String;
              //   1016: invokevirtual s2b : (Ljava/lang/String;)[B
              //   1019: astore #8
              //   1021: aload_1
              //   1022: astore #10
              //   1024: aload_1
              //   1025: astore #9
              //   1027: aload_0
              //   1028: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   1031: aload #11
              //   1033: getfield bufferLength : I
              //   1036: invokestatic toString : (I)Ljava/lang/String;
              //   1039: invokevirtual s2b : (Ljava/lang/String;)[B
              //   1042: astore #16
              //   1044: aload_1
              //   1045: astore #10
              //   1047: aload_1
              //   1048: astore #9
              //   1050: aload #11
              //   1052: getfield decimalDigits : Ljava/lang/Integer;
              //   1055: astore #11
              //   1057: aload #11
              //   1059: ifnonnull -> 1068
              //   1062: aconst_null
              //   1063: astore #11
              //   1065: goto -> 1088
              //   1068: aload_1
              //   1069: astore #10
              //   1071: aload_1
              //   1072: astore #9
              //   1074: aload_0
              //   1075: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   1078: aload #11
              //   1080: invokevirtual toString : ()Ljava/lang/String;
              //   1083: invokevirtual s2b : (Ljava/lang/String;)[B
              //   1086: astore #11
              //   1088: aload_1
              //   1089: astore #10
              //   1091: aload_1
              //   1092: astore #9
              //   1094: iconst_1
              //   1095: invokestatic toString : (I)Ljava/lang/String;
              //   1098: invokevirtual getBytes : ()[B
              //   1101: astore #17
              //   1103: aload_1
              //   1104: astore #10
              //   1106: aload_1
              //   1107: astore #9
              //   1109: aload_0
              //   1110: getfield val$rows : Ljava/util/ArrayList;
              //   1113: astore #20
              //   1115: aload_1
              //   1116: astore #10
              //   1118: aload_1
              //   1119: astore #9
              //   1121: new com/mysql/jdbc/ByteArrayRow
              //   1124: astore #18
              //   1126: aload_1
              //   1127: astore #10
              //   1129: aload_1
              //   1130: astore #9
              //   1132: aload_0
              //   1133: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
              //   1136: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
              //   1139: astore #19
              //   1141: aload_1
              //   1142: astore #10
              //   1144: aload_1
              //   1145: astore #9
              //   1147: aload #18
              //   1149: bipush #8
              //   1151: anewarray [B
              //   1154: dup
              //   1155: iconst_0
              //   1156: aconst_null
              //   1157: aastore
              //   1158: dup
              //   1159: iconst_1
              //   1160: aload #13
              //   1162: aastore
              //   1163: dup
              //   1164: iconst_2
              //   1165: aload #12
              //   1167: aastore
              //   1168: dup
              //   1169: iconst_3
              //   1170: aload #15
              //   1172: aastore
              //   1173: dup
              //   1174: iconst_4
              //   1175: aload #8
              //   1177: aastore
              //   1178: dup
              //   1179: iconst_5
              //   1180: aload #16
              //   1182: aastore
              //   1183: dup
              //   1184: bipush #6
              //   1186: aload #11
              //   1188: aastore
              //   1189: dup
              //   1190: bipush #7
              //   1192: aload #17
              //   1194: aastore
              //   1195: aload #19
              //   1197: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
              //   1200: aload_1
              //   1201: astore #10
              //   1203: aload_1
              //   1204: astore #9
              //   1206: aload #20
              //   1208: aload #18
              //   1210: invokevirtual add : (Ljava/lang/Object;)Z
              //   1213: pop
              //   1214: goto -> 829
              //   1217: aload #11
              //   1219: ifnull -> 1291
              //   1222: aload #11
              //   1224: invokeinterface close : ()V
              //   1229: goto -> 1291
              //   1232: astore_1
              //   1233: aload #10
              //   1235: astore #8
              //   1237: goto -> 1298
              //   1240: astore #8
              //   1242: aload #9
              //   1244: astore_1
              //   1245: aload #8
              //   1247: astore #9
              //   1249: goto -> 1261
              //   1252: astore_1
              //   1253: goto -> 1298
              //   1256: astore #9
              //   1258: aload #10
              //   1260: astore_1
              //   1261: aload_1
              //   1262: astore #8
              //   1264: ldc '42S02'
              //   1266: aload #9
              //   1268: invokevirtual getSQLState : ()Ljava/lang/String;
              //   1271: invokevirtual equals : (Ljava/lang/Object;)Z
              //   1274: istore #6
              //   1276: iload #6
              //   1278: ifeq -> 1292
              //   1281: aload_1
              //   1282: ifnull -> 1291
              //   1285: aload_1
              //   1286: invokeinterface close : ()V
              //   1291: return
              //   1292: aload_1
              //   1293: astore #8
              //   1295: aload #9
              //   1297: athrow
              //   1298: aload #8
              //   1300: ifnull -> 1310
              //   1303: aload #8
              //   1305: invokeinterface close : ()V
              //   1310: aload_1
              //   1311: athrow
              //   1312: astore_1
              //   1313: goto -> 1291
              //   1316: astore #8
              //   1318: goto -> 1310
              // Exception table:
              //   from	to	target	type
              //   27	32	1256	java/sql/SQLException
              //   27	32	1252	finally
              //   36	43	1256	java/sql/SQLException
              //   36	43	1252	finally
              //   47	52	1256	java/sql/SQLException
              //   47	52	1252	finally
              //   56	61	1256	java/sql/SQLException
              //   56	61	1252	finally
              //   65	83	1256	java/sql/SQLException
              //   65	83	1252	finally
              //   95	100	1256	java/sql/SQLException
              //   95	100	1252	finally
              //   104	109	1256	java/sql/SQLException
              //   104	109	1252	finally
              //   113	118	1256	java/sql/SQLException
              //   113	118	1252	finally
              //   122	129	1256	java/sql/SQLException
              //   122	129	1252	finally
              //   133	151	1256	java/sql/SQLException
              //   133	151	1252	finally
              //   155	162	1256	java/sql/SQLException
              //   155	162	1252	finally
              //   166	179	1256	java/sql/SQLException
              //   166	179	1252	finally
              //   197	207	1240	java/sql/SQLException
              //   197	207	1232	finally
              //   215	226	1240	java/sql/SQLException
              //   215	226	1232	finally
              //   234	239	1240	java/sql/SQLException
              //   234	239	1232	finally
              //   247	256	1240	java/sql/SQLException
              //   247	256	1232	finally
              //   274	282	1240	java/sql/SQLException
              //   274	282	1232	finally
              //   290	300	1240	java/sql/SQLException
              //   290	300	1232	finally
              //   311	322	1240	java/sql/SQLException
              //   311	322	1232	finally
              //   330	343	1240	java/sql/SQLException
              //   330	343	1232	finally
              //   356	364	1240	java/sql/SQLException
              //   356	364	1232	finally
              //   394	411	1240	java/sql/SQLException
              //   394	411	1232	finally
              //   422	434	1240	java/sql/SQLException
              //   422	434	1232	finally
              //   460	468	1240	java/sql/SQLException
              //   460	468	1232	finally
              //   481	489	1240	java/sql/SQLException
              //   481	489	1232	finally
              //   497	513	1240	java/sql/SQLException
              //   497	513	1232	finally
              //   521	529	1240	java/sql/SQLException
              //   521	529	1232	finally
              //   543	561	1240	java/sql/SQLException
              //   543	561	1232	finally
              //   583	591	1240	java/sql/SQLException
              //   583	591	1232	finally
              //   603	613	1240	java/sql/SQLException
              //   603	613	1232	finally
              //   621	626	1240	java/sql/SQLException
              //   621	626	1232	finally
              //   634	641	1240	java/sql/SQLException
              //   634	641	1232	finally
              //   649	655	1240	java/sql/SQLException
              //   649	655	1232	finally
              //   663	669	1240	java/sql/SQLException
              //   663	669	1232	finally
              //   677	703	1240	java/sql/SQLException
              //   677	703	1232	finally
              //   711	719	1240	java/sql/SQLException
              //   711	719	1232	finally
              //   727	733	1240	java/sql/SQLException
              //   727	733	1232	finally
              //   741	766	1240	java/sql/SQLException
              //   741	766	1232	finally
              //   779	787	1240	java/sql/SQLException
              //   779	787	1232	finally
              //   795	806	1240	java/sql/SQLException
              //   795	806	1232	finally
              //   814	829	1240	java/sql/SQLException
              //   814	829	1232	finally
              //   835	843	1240	java/sql/SQLException
              //   835	843	1232	finally
              //   862	880	1240	java/sql/SQLException
              //   862	880	1232	finally
              //   886	891	1240	java/sql/SQLException
              //   886	891	1232	finally
              //   897	922	1240	java/sql/SQLException
              //   897	922	1232	finally
              //   928	938	1240	java/sql/SQLException
              //   928	938	1232	finally
              //   944	957	1240	java/sql/SQLException
              //   944	957	1232	finally
              //   963	977	1240	java/sql/SQLException
              //   963	977	1232	finally
              //   983	990	1240	java/sql/SQLException
              //   983	990	1232	finally
              //   1007	1021	1240	java/sql/SQLException
              //   1007	1021	1232	finally
              //   1027	1044	1240	java/sql/SQLException
              //   1027	1044	1232	finally
              //   1050	1057	1240	java/sql/SQLException
              //   1050	1057	1232	finally
              //   1074	1088	1240	java/sql/SQLException
              //   1074	1088	1232	finally
              //   1094	1103	1240	java/sql/SQLException
              //   1094	1103	1232	finally
              //   1109	1115	1240	java/sql/SQLException
              //   1109	1115	1232	finally
              //   1121	1126	1240	java/sql/SQLException
              //   1121	1126	1232	finally
              //   1132	1141	1240	java/sql/SQLException
              //   1132	1141	1232	finally
              //   1147	1200	1240	java/sql/SQLException
              //   1147	1200	1232	finally
              //   1206	1214	1240	java/sql/SQLException
              //   1206	1214	1232	finally
              //   1222	1229	1312	java/lang/Exception
              //   1264	1276	1252	finally
              //   1285	1291	1312	java/lang/Exception
              //   1295	1298	1252	finally
              //   1303	1310	1316	java/lang/Exception
            }
          };
        super(this, getCatalogIterator(paramString1), paramString3, statement, arrayList);
        iterateBlock.doForAll();
        return buildResultSet(new Field[] { field2, field5, field1, field6, field7, field8, field4, field3 }, arrayList);
      } finally {
        if (statement != null)
          statement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public boolean insertsAreDetected(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean isCatalogAtStart() throws SQLException {
    return true;
  }
  
  public boolean isReadOnly() throws SQLException {
    return false;
  }
  
  public boolean locatorsUpdateCopy() throws SQLException {
    return this.conn.getEmulateLocators() ^ true;
  }
  
  public boolean nullPlusNonNullIsNull() throws SQLException {
    return true;
  }
  
  public boolean nullsAreSortedAtEnd() throws SQLException {
    return false;
  }
  
  public boolean nullsAreSortedAtStart() throws SQLException {
    MySQLConnection mySQLConnection = this.conn;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (mySQLConnection.versionMeetsMinimum(4, 0, 2)) {
      bool1 = bool2;
      if (!this.conn.versionMeetsMinimum(4, 0, 11))
        bool1 = true; 
    } 
    return bool1;
  }
  
  public boolean nullsAreSortedHigh() throws SQLException {
    return false;
  }
  
  public boolean nullsAreSortedLow() throws SQLException {
    return nullsAreSortedHigh() ^ true;
  }
  
  public boolean othersDeletesAreVisible(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean othersInsertsAreVisible(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean othersUpdatesAreVisible(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean ownDeletesAreVisible(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean ownInsertsAreVisible(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean ownUpdatesAreVisible(int paramInt) throws SQLException {
    return false;
  }
  
  public LocalAndReferencedColumns parseTableStatusIntoLocalAndReferencedColumns(String paramString) throws SQLException {
    String str = this.quotedId;
    Set<StringUtils.SearchMode> set = StringUtils.SEARCH_MODE__ALL;
    int i = StringUtils.indexOfIgnoreCase(0, paramString, "(", str, str, set);
    if (i != -1) {
      str = StringUtils.unQuoteIdentifier(paramString.substring(0, i).trim(), this.quotedId);
      String str1 = paramString.substring(i, paramString.length()).trim();
      paramString = this.quotedId;
      i = StringUtils.indexOfIgnoreCase(0, str1, ")", paramString, paramString, set);
      if (i != -1) {
        paramString = str1.substring(1, i);
        String str2 = this.quotedId;
        int j = StringUtils.indexOfIgnoreCase(0, str1, "REFER ", str2, str2, set);
        if (j != -1) {
          String str3 = this.quotedId;
          Set<StringUtils.SearchMode> set1 = StringUtils.SEARCH_MODE__MRK_COM_WS;
          i = StringUtils.indexOfIgnoreCase(j, str1, "(", str3, str3, set1);
          if (i != -1) {
            str3 = str1.substring(j + 6, i);
            String str4 = this.quotedId;
            j = StringUtils.indexOfIgnoreCase(0, str3, "/", str4, str4, set1);
            if (j != -1) {
              String str5 = StringUtils.unQuoteIdentifier(str3.substring(0, j), this.quotedId);
              str3 = StringUtils.unQuoteIdentifier(str3.substring(j + 1).trim(), this.quotedId);
              str4 = this.quotedId;
              j = StringUtils.indexOfIgnoreCase(i, str1, ")", str4, str4, set);
              if (j != -1) {
                String str6 = str1.substring(i + 1, j);
                str1 = this.quotedId;
                List<String> list = StringUtils.split(str6, ",", str1, str1, false);
                str1 = this.quotedId;
                return new LocalAndReferencedColumns(StringUtils.split(paramString, ",", str1, str1, false), list, str, str5, str3);
              } 
              throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of referenced columns list.", "S1000", getExceptionInterceptor());
            } 
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find name of referenced catalog.", "S1000", getExceptionInterceptor());
          } 
          throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced columns list.", "S1000", getExceptionInterceptor());
        } 
        throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced tables list.", "S1000", getExceptionInterceptor());
      } 
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of local columns list.", "S1000", getExceptionInterceptor());
    } 
    throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of local columns list.", "S1000", getExceptionInterceptor());
  }
  
  public PreparedStatement prepareMetaDataSafeStatement(String paramString) throws SQLException {
    PreparedStatement preparedStatement = this.conn.clientPrepareStatement(paramString);
    if (preparedStatement.getMaxRows() != 0)
      preparedStatement.setMaxRows(0); 
    ((Statement)preparedStatement).setHoldResultsOpenOverClose(true);
    return preparedStatement;
  }
  
  public boolean providesQueryObjectGenerator() throws SQLException {
    return false;
  }
  
  public byte[] s2b(String paramString) throws SQLException {
    return (paramString == null) ? null : StringUtils.getBytes(paramString, this.conn.getCharacterSetMetadata(), this.conn.getServerCharset(), this.conn.parserKnowsUnicode(), this.conn, getExceptionInterceptor());
  }
  
  public boolean storesLowerCaseIdentifiers() throws SQLException {
    return this.conn.storesLowerCaseTableName();
  }
  
  public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
    return this.conn.storesLowerCaseTableName();
  }
  
  public boolean storesMixedCaseIdentifiers() throws SQLException {
    return this.conn.storesLowerCaseTableName() ^ true;
  }
  
  public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
    return this.conn.storesLowerCaseTableName() ^ true;
  }
  
  public boolean storesUpperCaseIdentifiers() throws SQLException {
    return false;
  }
  
  public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
    return true;
  }
  
  public boolean supportsANSI92EntryLevelSQL() throws SQLException {
    return true;
  }
  
  public boolean supportsANSI92FullSQL() throws SQLException {
    return false;
  }
  
  public boolean supportsANSI92IntermediateSQL() throws SQLException {
    return false;
  }
  
  public boolean supportsAlterTableWithAddColumn() throws SQLException {
    return true;
  }
  
  public boolean supportsAlterTableWithDropColumn() throws SQLException {
    return true;
  }
  
  public boolean supportsBatchUpdates() throws SQLException {
    return true;
  }
  
  public boolean supportsCatalogsInDataManipulation() throws SQLException {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInProcedureCalls() throws SQLException {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInTableDefinitions() throws SQLException {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsColumnAliasing() throws SQLException {
    return true;
  }
  
  public boolean supportsConvert() throws SQLException {
    return false;
  }
  
  public boolean supportsConvert(int paramInt1, int paramInt2) throws SQLException {
    if (paramInt1 != 12)
      if (paramInt1 != 1111) {
        switch (paramInt1) {
          default:
            switch (paramInt1) {
              default:
                switch (paramInt1) {
                  default:
                    return false;
                  case 93:
                    return !(paramInt2 != -4 && paramInt2 != -3 && paramInt2 != -2 && paramInt2 != -1 && paramInt2 != 1 && paramInt2 != 12 && paramInt2 != 91 && paramInt2 != 92);
                  case 92:
                    return !(paramInt2 != -4 && paramInt2 != -3 && paramInt2 != -2 && paramInt2 != -1 && paramInt2 != 1 && paramInt2 != 12);
                  case 91:
                    break;
                } 
                return !(paramInt2 != -4 && paramInt2 != -3 && paramInt2 != -2 && paramInt2 != -1 && paramInt2 != 1 && paramInt2 != 12);
              case 2:
              case 3:
              case 4:
              case 5:
              case 6:
              case 7:
              case 8:
                switch (paramInt2) {
                  default:
                    return false;
                  case -6:
                  case -5:
                  case -4:
                  case -3:
                  case -2:
                  case -1:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                  case 12:
                    break;
                } 
                return true;
              case 1:
                break;
            } 
            break;
          case -6:
          case -5:
          
          case -4:
          case -3:
          case -2:
          case -1:
            break;
        } 
      } else {
        return !(paramInt2 != -4 && paramInt2 != -3 && paramInt2 != -2 && paramInt2 != -1 && paramInt2 != 1 && paramInt2 != 12);
      }  
    if (paramInt2 != 12 && paramInt2 != 1111)
      switch (paramInt2) {
        default:
          switch (paramInt2) {
            default:
              switch (paramInt2) {
                default:
                  return false;
                case 91:
                case 92:
                case 93:
                  break;
              } 
              break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
              break;
          } 
          break;
        case -6:
        case -5:
        case -4:
        case -3:
        case -2:
        case -1:
          break;
      }  
    return true;
  }
  
  public boolean supportsCoreSQLGrammar() throws SQLException {
    return true;
  }
  
  public boolean supportsCorrelatedSubqueries() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
    return false;
  }
  
  public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
    return false;
  }
  
  public boolean supportsDifferentTableCorrelationNames() throws SQLException {
    return true;
  }
  
  public boolean supportsExpressionsInOrderBy() throws SQLException {
    return true;
  }
  
  public boolean supportsExtendedSQLGrammar() throws SQLException {
    return false;
  }
  
  public boolean supportsFullOuterJoins() throws SQLException {
    return false;
  }
  
  public boolean supportsGetGeneratedKeys() {
    return true;
  }
  
  public boolean supportsGroupBy() throws SQLException {
    return true;
  }
  
  public boolean supportsGroupByBeyondSelect() throws SQLException {
    return true;
  }
  
  public boolean supportsGroupByUnrelated() throws SQLException {
    return true;
  }
  
  public boolean supportsIntegrityEnhancementFacility() throws SQLException {
    return !!this.conn.getOverrideSupportsIntegrityEnhancementFacility();
  }
  
  public boolean supportsLikeEscapeClause() throws SQLException {
    return true;
  }
  
  public boolean supportsLimitedOuterJoins() throws SQLException {
    return true;
  }
  
  public boolean supportsMinimumSQLGrammar() throws SQLException {
    return true;
  }
  
  public boolean supportsMixedCaseIdentifiers() throws SQLException {
    return this.conn.lowerCaseTableNames() ^ true;
  }
  
  public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
    return this.conn.lowerCaseTableNames() ^ true;
  }
  
  public boolean supportsMultipleOpenResults() throws SQLException {
    return true;
  }
  
  public boolean supportsMultipleResultSets() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsMultipleTransactions() throws SQLException {
    return true;
  }
  
  public boolean supportsNamedParameters() throws SQLException {
    return false;
  }
  
  public boolean supportsNonNullableColumns() throws SQLException {
    return true;
  }
  
  public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
    return false;
  }
  
  public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
    return false;
  }
  
  public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
    return false;
  }
  
  public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
    return false;
  }
  
  public boolean supportsOrderByUnrelated() throws SQLException {
    return false;
  }
  
  public boolean supportsOuterJoins() throws SQLException {
    return true;
  }
  
  public boolean supportsPositionedDelete() throws SQLException {
    return false;
  }
  
  public boolean supportsPositionedUpdate() throws SQLException {
    return false;
  }
  
  public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2) throws SQLException {
    switch (paramInt1) {
      default:
        throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
      case 1005:
        return false;
      case 1004:
        if (paramInt2 == 1007 || paramInt2 == 1008)
          return true; 
        throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
      case 1003:
        break;
    } 
    if (paramInt2 == 1007 || paramInt2 == 1008)
      return true; 
    throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
  }
  
  public boolean supportsResultSetHoldability(int paramInt) throws SQLException {
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    return bool;
  }
  
  public boolean supportsResultSetType(int paramInt) throws SQLException {
    boolean bool;
    if (paramInt == 1004) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean supportsSavepoints() throws SQLException {
    MySQLConnection mySQLConnection = this.conn;
    boolean bool = false;
    if (mySQLConnection.versionMeetsMinimum(4, 0, 14) || this.conn.versionMeetsMinimum(4, 1, 1))
      bool = true; 
    return bool;
  }
  
  public boolean supportsSchemasInDataManipulation() throws SQLException {
    return false;
  }
  
  public boolean supportsSchemasInIndexDefinitions() throws SQLException {
    return false;
  }
  
  public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
    return false;
  }
  
  public boolean supportsSchemasInProcedureCalls() throws SQLException {
    return false;
  }
  
  public boolean supportsSchemasInTableDefinitions() throws SQLException {
    return false;
  }
  
  public boolean supportsSelectForUpdate() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 0, 0);
  }
  
  public boolean supportsStatementPooling() throws SQLException {
    return false;
  }
  
  public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
    return true;
  }
  
  public boolean supportsStoredProcedures() throws SQLException {
    return this.conn.versionMeetsMinimum(5, 0, 0);
  }
  
  public boolean supportsSubqueriesInComparisons() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsSubqueriesInExists() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsSubqueriesInIns() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsSubqueriesInQuantifieds() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsTableCorrelationNames() throws SQLException {
    return true;
  }
  
  public boolean supportsTransactionIsolationLevel(int paramInt) throws SQLException {
    return this.conn.supportsIsolationLevel() ? (!(paramInt != 1 && paramInt != 2 && paramInt != 4 && paramInt != 8)) : false;
  }
  
  public boolean supportsTransactions() throws SQLException {
    return this.conn.supportsTransactions();
  }
  
  public boolean supportsUnion() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 0, 0);
  }
  
  public boolean supportsUnionAll() throws SQLException {
    return this.conn.versionMeetsMinimum(4, 0, 0);
  }
  
  public boolean updatesAreDetected(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean usesLocalFilePerTable() throws SQLException {
    return false;
  }
  
  public boolean usesLocalFiles() throws SQLException {
    return false;
  }
  
  public class ComparableWrapper<K extends Comparable<? super K>, V> implements Comparable<ComparableWrapper<K, V>> {
    public static final boolean $assertionsDisabled = false;
    
    public K key;
    
    public final DatabaseMetaData this$0;
    
    public V value;
    
    public ComparableWrapper(K param1K, V param1V) {
      this.key = param1K;
      this.value = param1V;
    }
    
    public int compareTo(ComparableWrapper<K, V> param1ComparableWrapper) {
      return ((Comparable)getKey()).compareTo(param1ComparableWrapper.getKey());
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object == null)
        return false; 
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof ComparableWrapper))
        return false; 
      param1Object = ((ComparableWrapper)param1Object).getKey();
      return this.key.equals(param1Object);
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public int hashCode() {
      return 0;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{KEY:");
      stringBuilder.append(this.key);
      stringBuilder.append("; VALUE:");
      stringBuilder.append(this.value);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }
  
  public class IndexMetaDataKey implements Comparable<IndexMetaDataKey> {
    public static final boolean $assertionsDisabled = false;
    
    public String columnIndexName;
    
    public Boolean columnNonUnique;
    
    public Short columnOrdinalPosition;
    
    public Short columnType;
    
    public final DatabaseMetaData this$0;
    
    public IndexMetaDataKey(boolean param1Boolean, short param1Short1, String param1String, short param1Short2) {
      this.columnNonUnique = Boolean.valueOf(param1Boolean);
      this.columnType = Short.valueOf(param1Short1);
      this.columnIndexName = param1String;
      this.columnOrdinalPosition = Short.valueOf(param1Short2);
    }
    
    public int compareTo(IndexMetaDataKey param1IndexMetaDataKey) {
      int i = this.columnNonUnique.compareTo(param1IndexMetaDataKey.columnNonUnique);
      if (i != 0)
        return i; 
      i = this.columnType.compareTo(param1IndexMetaDataKey.columnType);
      if (i != 0)
        return i; 
      i = this.columnIndexName.compareTo(param1IndexMetaDataKey.columnIndexName);
      return (i != 0) ? i : this.columnOrdinalPosition.compareTo(param1IndexMetaDataKey.columnOrdinalPosition);
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = false;
      if (param1Object == null)
        return false; 
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof IndexMetaDataKey))
        return false; 
      if (compareTo((IndexMetaDataKey)param1Object) == 0)
        bool = true; 
      return bool;
    }
    
    public int hashCode() {
      return 0;
    }
  }
  
  public abstract class IteratorWithCleanup<T> {
    public final DatabaseMetaData this$0;
    
    public abstract void close() throws SQLException;
    
    public abstract boolean hasNext() throws SQLException;
    
    public abstract T next() throws SQLException;
  }
  
  public class LocalAndReferencedColumns {
    public String constraintName;
    
    public List<String> localColumnsList;
    
    public String referencedCatalog;
    
    public List<String> referencedColumnsList;
    
    public String referencedTable;
    
    public final DatabaseMetaData this$0;
    
    public LocalAndReferencedColumns(List<String> param1List1, List<String> param1List2, String param1String1, String param1String2, String param1String3) {
      this.localColumnsList = param1List1;
      this.referencedColumnsList = param1List2;
      this.constraintName = param1String1;
      this.referencedTable = param1String3;
      this.referencedCatalog = param1String2;
    }
  }
  
  public enum ProcedureType {
    FUNCTION, PROCEDURE;
    
    private static final ProcedureType[] $VALUES;
    
    static {
      ProcedureType procedureType2 = new ProcedureType("PROCEDURE", 0);
      PROCEDURE = procedureType2;
      ProcedureType procedureType1 = new ProcedureType("FUNCTION", 1);
      FUNCTION = procedureType1;
      $VALUES = new ProcedureType[] { procedureType2, procedureType1 };
    }
  }
  
  public class ResultSetIterator extends IteratorWithCleanup<String> {
    public int colIndex;
    
    public ResultSet resultSet;
    
    public final DatabaseMetaData this$0;
    
    public ResultSetIterator(ResultSet param1ResultSet, int param1Int) {
      this.resultSet = param1ResultSet;
      this.colIndex = param1Int;
    }
    
    public void close() throws SQLException {
      this.resultSet.close();
    }
    
    public boolean hasNext() throws SQLException {
      return this.resultSet.next();
    }
    
    public String next() throws SQLException {
      return this.resultSet.getObject(this.colIndex).toString();
    }
  }
  
  public class SingleStringIterator extends IteratorWithCleanup<String> {
    public boolean onFirst = true;
    
    public final DatabaseMetaData this$0;
    
    public String value;
    
    public SingleStringIterator(String param1String) {
      this.value = param1String;
    }
    
    public void close() throws SQLException {}
    
    public boolean hasNext() throws SQLException {
      return this.onFirst;
    }
    
    public String next() throws SQLException {
      this.onFirst = false;
      return this.value;
    }
  }
  
  public class TableMetaDataKey implements Comparable<TableMetaDataKey> {
    public static final boolean $assertionsDisabled = false;
    
    public String tableCat;
    
    public String tableName;
    
    public String tableSchem;
    
    public String tableType;
    
    public final DatabaseMetaData this$0;
    
    public TableMetaDataKey(String param1String1, String param1String2, String param1String3, String param1String4) {
      String str = param1String1;
      if (param1String1 == null)
        str = ""; 
      this.tableType = str;
      str = param1String2;
      if (param1String2 == null)
        str = ""; 
      this.tableCat = str;
      str = param1String3;
      if (param1String3 == null)
        str = ""; 
      this.tableSchem = str;
      str = param1String4;
      if (param1String4 == null)
        str = ""; 
      this.tableName = str;
    }
    
    public int compareTo(TableMetaDataKey param1TableMetaDataKey) {
      int i = this.tableType.compareTo(param1TableMetaDataKey.tableType);
      if (i != 0)
        return i; 
      i = this.tableCat.compareTo(param1TableMetaDataKey.tableCat);
      if (i != 0)
        return i; 
      i = this.tableSchem.compareTo(param1TableMetaDataKey.tableSchem);
      return (i != 0) ? i : this.tableName.compareTo(param1TableMetaDataKey.tableName);
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = false;
      if (param1Object == null)
        return false; 
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof TableMetaDataKey))
        return false; 
      if (compareTo((TableMetaDataKey)param1Object) == 0)
        bool = true; 
      return bool;
    }
    
    public int hashCode() {
      return 0;
    }
  }
  
  public enum TableType {
    LOCAL_TEMPORARY, SYSTEM_TABLE, SYSTEM_VIEW, TABLE, UNKNOWN, VIEW;
    
    private static final TableType[] $VALUES;
    
    private String name;
    
    private byte[] nameAsBytes;
    
    private String[] synonyms;
    
    static {
      TableType tableType6 = new TableType("LOCAL_TEMPORARY", 0, "LOCAL TEMPORARY");
      LOCAL_TEMPORARY = tableType6;
      TableType tableType1 = new TableType("SYSTEM_TABLE", 1, "SYSTEM TABLE");
      SYSTEM_TABLE = tableType1;
      TableType tableType2 = new TableType("SYSTEM_VIEW", 2, "SYSTEM VIEW");
      SYSTEM_VIEW = tableType2;
      TableType tableType4 = new TableType("TABLE", 3, "TABLE", new String[] { "BASE TABLE" });
      TABLE = tableType4;
      TableType tableType5 = new TableType("VIEW", 4, "VIEW");
      VIEW = tableType5;
      TableType tableType3 = new TableType("UNKNOWN", 5, "UNKNOWN");
      UNKNOWN = tableType3;
      $VALUES = new TableType[] { tableType6, tableType1, tableType2, tableType4, tableType5, tableType3 };
    }
    
    TableType(String param1String1, String[] param1ArrayOfString) {
      this.name = param1String1;
      this.nameAsBytes = param1String1.getBytes();
      this.synonyms = param1ArrayOfString;
    }
    
    public static TableType getTableTypeCompliantWith(String param1String) {
      for (TableType tableType : values()) {
        if (tableType.compliesWith(param1String))
          return tableType; 
      } 
      return UNKNOWN;
    }
    
    public static TableType getTableTypeEqualTo(String param1String) {
      for (TableType tableType : values()) {
        if (tableType.equalsTo(param1String))
          return tableType; 
      } 
      return UNKNOWN;
    }
    
    public byte[] asBytes() {
      return this.nameAsBytes;
    }
    
    public boolean compliesWith(String param1String) {
      if (equalsTo(param1String))
        return true; 
      String[] arrayOfString = this.synonyms;
      if (arrayOfString != null) {
        int i = arrayOfString.length;
        for (byte b = 0; b < i; b++) {
          if (arrayOfString[b].equalsIgnoreCase(param1String))
            return true; 
        } 
      } 
      return false;
    }
    
    public boolean equalsTo(String param1String) {
      return this.name.equalsIgnoreCase(param1String);
    }
    
    public String getName() {
      return this.name;
    }
  }
  
  public class TypeDescriptor {
    public int bufferLength;
    
    public int charOctetLength;
    
    public Integer columnSize;
    
    public short dataType;
    
    public Integer decimalDigits;
    
    public String isNullable;
    
    public int nullability;
    
    public int numPrecRadix;
    
    public final DatabaseMetaData this$0;
    
    public String typeName;
    
    public TypeDescriptor(String param1String1, String param1String2) throws SQLException {
      String str;
      Integer integer = Integer.valueOf(10);
      this.numPrecRadix = 10;
      if (param1String1 != null) {
        String str2;
        if (param1String1.indexOf("(") != -1) {
          str2 = param1String1.substring(0, param1String1.indexOf("(")).trim();
        } else {
          str2 = param1String1;
        } 
        int i = StringUtils.indexOfIgnoreCase(str2, "unsigned");
        String str1 = str2;
        if (i != -1)
          str1 = str2.substring(0, i - 1); 
        if (StringUtils.indexOfIgnoreCase(param1String1, "unsigned") != -1 && StringUtils.indexOfIgnoreCase(param1String1, "set") != 0 && StringUtils.indexOfIgnoreCase(param1String1, "enum") != 0) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append(" unsigned");
          String str4 = stringBuilder.toString();
          i = 1;
        } else {
          str2 = str1;
          i = 0;
        } 
        String str3 = str2;
        if (DatabaseMetaData.this.conn.getCapitalizeTypeNames())
          str3 = str2.toUpperCase(Locale.ENGLISH); 
        this.dataType = (short)MysqlDefs.mysqlToJavaType(str1);
        this.typeName = str3;
        if (StringUtils.startsWithIgnoreCase(param1String1, "enum")) {
          StringTokenizer stringTokenizer = new StringTokenizer(param1String1.substring(param1String1.indexOf("("), param1String1.lastIndexOf(")")), ",");
          for (i = 0; stringTokenizer.hasMoreTokens(); i = Math.max(i, stringTokenizer.nextToken().length() - 2));
          this.columnSize = Integer.valueOf(i);
          this.decimalDigits = null;
        } else {
          StringTokenizer stringTokenizer;
          if (StringUtils.startsWithIgnoreCase(param1String1, "set")) {
            stringTokenizer = new StringTokenizer(param1String1.substring(param1String1.indexOf("(") + 1, param1String1.lastIndexOf(")")), ",");
            i = stringTokenizer.countTokens();
            if (i > 0) {
              i = i - 1 + 0;
            } else {
              i = 0;
            } 
            while (stringTokenizer.hasMoreTokens()) {
              int j;
              str = stringTokenizer.nextToken().trim();
              if (str.startsWith("'") && str.endsWith("'")) {
                j = str.length() - 2;
              } else {
                j = str.length();
              } 
              i += j;
            } 
            this.columnSize = Integer.valueOf(i);
            this.decimalDigits = null;
          } else if (stringTokenizer.indexOf(",") != -1) {
            this.columnSize = Integer.valueOf(stringTokenizer.substring(stringTokenizer.indexOf("(") + 1, stringTokenizer.indexOf(",")).trim());
            this.decimalDigits = Integer.valueOf(stringTokenizer.substring(stringTokenizer.indexOf(",") + 1, stringTokenizer.indexOf(")")).trim());
          } else {
            this.columnSize = null;
            this.decimalDigits = null;
            if ((StringUtils.indexOfIgnoreCase((String)stringTokenizer, "char") != -1 || StringUtils.indexOfIgnoreCase((String)stringTokenizer, "text") != -1 || StringUtils.indexOfIgnoreCase((String)stringTokenizer, "blob") != -1 || StringUtils.indexOfIgnoreCase((String)stringTokenizer, "binary") != -1 || StringUtils.indexOfIgnoreCase((String)stringTokenizer, "bit") != -1) && stringTokenizer.indexOf("(") != -1) {
              int j = stringTokenizer.indexOf(")");
              i = j;
              if (j == -1)
                i = stringTokenizer.length(); 
              this.columnSize = Integer.valueOf(stringTokenizer.substring(stringTokenizer.indexOf("(") + 1, i).trim());
              if (((DatabaseMetaData)str).conn.getTinyInt1isBit() && this.columnSize.intValue() == 1 && StringUtils.startsWithIgnoreCase((String)stringTokenizer, 0, "tinyint"))
                if (((DatabaseMetaData)str).conn.getTransformedBitIsBoolean()) {
                  this.dataType = 16;
                  this.typeName = "BOOLEAN";
                } else {
                  this.dataType = -7;
                  this.typeName = "BIT";
                }  
            } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "tinyint")) {
              if (((DatabaseMetaData)str).conn.getTinyInt1isBit() && stringTokenizer.indexOf("(1)") != -1) {
                if (((DatabaseMetaData)str).conn.getTransformedBitIsBoolean()) {
                  this.dataType = 16;
                  this.typeName = "BOOLEAN";
                } else {
                  this.dataType = -7;
                  this.typeName = "BIT";
                } 
              } else {
                this.columnSize = Integer.valueOf(3);
                this.decimalDigits = Integer.valueOf(0);
              } 
            } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "smallint")) {
              this.columnSize = Integer.valueOf(5);
              this.decimalDigits = Integer.valueOf(0);
            } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "mediumint")) {
              if (i != 0) {
                i = 8;
              } else {
                i = 7;
              } 
              this.columnSize = Integer.valueOf(i);
              this.decimalDigits = Integer.valueOf(0);
            } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "int")) {
              this.columnSize = integer;
              this.decimalDigits = Integer.valueOf(0);
            } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "integer")) {
              this.columnSize = integer;
              this.decimalDigits = Integer.valueOf(0);
            } else {
              boolean bool = StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "bigint");
              byte b = 19;
              if (bool) {
                if (i != 0)
                  b = 20; 
                this.columnSize = Integer.valueOf(b);
                this.decimalDigits = Integer.valueOf(0);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "int24")) {
                this.columnSize = Integer.valueOf(19);
                this.decimalDigits = Integer.valueOf(0);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "real")) {
                this.columnSize = Integer.valueOf(12);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "float")) {
                this.columnSize = Integer.valueOf(12);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "decimal")) {
                this.columnSize = Integer.valueOf(12);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "numeric")) {
                this.columnSize = Integer.valueOf(12);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "double")) {
                this.columnSize = Integer.valueOf(22);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "char")) {
                this.columnSize = Integer.valueOf(1);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "varchar")) {
                this.columnSize = Integer.valueOf(255);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "timestamp")) {
                this.columnSize = Integer.valueOf(19);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "datetime")) {
                this.columnSize = Integer.valueOf(19);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "date")) {
                this.columnSize = integer;
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "time")) {
                this.columnSize = Integer.valueOf(8);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "tinyblob")) {
                this.columnSize = Integer.valueOf(255);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "blob")) {
                this.columnSize = Integer.valueOf(65535);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "mediumblob")) {
                this.columnSize = Integer.valueOf(16777215);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "longblob")) {
                this.columnSize = Integer.valueOf(2147483647);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "tinytext")) {
                this.columnSize = Integer.valueOf(255);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "text")) {
                this.columnSize = Integer.valueOf(65535);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "mediumtext")) {
                this.columnSize = Integer.valueOf(16777215);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "longtext")) {
                this.columnSize = Integer.valueOf(2147483647);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "enum")) {
                this.columnSize = Integer.valueOf(255);
              } else if (StringUtils.startsWithIgnoreCaseAndWs((String)stringTokenizer, "set")) {
                this.columnSize = Integer.valueOf(255);
              } 
            } 
          } 
        } 
        this.bufferLength = MysqlIO.getMaxBuf();
        this.numPrecRadix = 10;
        if (param1String2 != null) {
          if (param1String2.equals("YES")) {
            this.nullability = 1;
            this.isNullable = "YES";
          } else if (param1String2.equals("UNKNOWN")) {
            this.nullability = 2;
            this.isNullable = "";
          } else {
            this.nullability = 0;
            this.isNullable = "NO";
          } 
        } else {
          this.nullability = 0;
          this.isNullable = "NO";
        } 
        return;
      } 
      throw SQLError.createSQLException("NULL typeinfo not supported.", "S1009", str.getExceptionInterceptor());
    }
  }
}
