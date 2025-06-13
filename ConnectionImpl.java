package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.LogFactory;
import com.mysql.jdbc.log.LogUtils;
import com.mysql.jdbc.log.NullLogger;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import com.mysql.jdbc.util.LRUCache;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLPermission;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class ConnectionImpl extends ConnectionPropertiesImpl implements MySQLConnection {
  private static final SQLPermission ABORT_PERM;
  
  private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER;
  
  public static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";
  
  private static final int DEFAULT_RESULT_SET_CONCURRENCY = 1007;
  
  private static final int DEFAULT_RESULT_SET_TYPE = 1003;
  
  private static final int HISTOGRAM_BUCKETS = 20;
  
  private static final Constructor<?> JDBC_4_CONNECTION_CTOR;
  
  public static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";
  
  private static final String LOGGER_INSTANCE_NAME = "MySQL";
  
  private static final Log NULL_LOGGER;
  
  private static final String SERVER_VERSION_STRING_VAR_NAME = "server_version_string";
  
  private static final SQLPermission SET_NETWORK_TIMEOUT_PERM = new SQLPermission("setNetworkTimeout");
  
  public static Map<?, ?> charsetMap;
  
  private static final Map<String, Map<String, Integer>> customCharsetToMblenMapByUrl;
  
  private static final Map<String, Map<Integer, String>> customIndexToCharsetMapByUrl;
  
  private static Map<String, Integer> mapTransIsolationNameToValue;
  
  private static final Random random;
  
  public static Map<?, ?> roundRobinStatsMap;
  
  private static final long serialVersionUID = 2877471301981509474L;
  
  private boolean autoCommit;
  
  private int autoIncrementIncrement;
  
  private CacheAdapter<String, PreparedStatement.ParseInfo> cachedPreparedStatementParams;
  
  private transient Timer cancelTimer;
  
  private String characterSetMetadata;
  
  private String characterSetResultsOnServer;
  
  private final Map<String, Object> charsetConverterMap;
  
  private long connectionCreationTimeMillis;
  
  private long connectionId;
  
  private List<Extension> connectionLifecycleInterceptors;
  
  private String database;
  
  private DatabaseMetaData dbmd;
  
  private TimeZone defaultTimeZone;
  
  private String errorMessageEncoding;
  
  private ProfilerEventHandler eventSink;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private Throwable forceClosedReason;
  
  private boolean hasIsolationLevels;
  
  private boolean hasQuotedIdentifiers;
  
  private boolean hasTriedMasterFlag;
  
  private String host;
  
  private String hostPortPair;
  
  public Map<Integer, String> indexToCustomMysqlCharset;
  
  private transient MysqlIO io;
  
  private boolean isClientTzUTC;
  
  private boolean isClosed;
  
  private boolean isInGlobalTx;
  
  private boolean isRunningOnJDK13;
  
  private boolean isServerTzUTC;
  
  private int isolationLevel;
  
  private long lastQueryFinishedTime;
  
  private transient Log log;
  
  private long longestQueryTimeMs;
  
  private boolean lowerCaseTableNames;
  
  private long maximumNumberTablesAccessed;
  
  private long metricsLastReportedMs;
  
  private long minimumNumberTablesAccessed;
  
  private String myURL;
  
  private Map<String, Integer> mysqlCharsetToCustomMblen;
  
  private boolean needsPing;
  
  private int netBufferLength;
  
  private boolean noBackslashEscapes;
  
  private long[] numTablesMetricsHistBreakpoints;
  
  private int[] numTablesMetricsHistCounts;
  
  private long numberOfPreparedExecutes;
  
  private long numberOfPrepares;
  
  private long numberOfQueriesIssued;
  
  private long numberOfResultSetsCreated;
  
  private long[] oldHistBreakpoints;
  
  private int[] oldHistCounts;
  
  private final CopyOnWriteArrayList<Statement> openStatements;
  
  private String origDatabaseToConnectTo;
  
  private String origHostToConnectTo;
  
  private int origPortToConnectTo;
  
  private LRUCache<CompoundCacheKey, CallableStatement.CallableStatementParamInfo> parsedCallableStatementCache;
  
  private boolean parserKnowsUnicode;
  
  private String password;
  
  private long[] perfMetricsHistBreakpoints;
  
  private int[] perfMetricsHistCounts;
  
  private String pointOfOrigin;
  
  private int port;
  
  public Properties props;
  
  private MySQLConnection proxy;
  
  private long queryTimeCount;
  
  private double queryTimeMean;
  
  private double queryTimeSum;
  
  private double queryTimeSumSquares;
  
  private boolean readInfoMsg;
  
  private boolean readOnly;
  
  private InvocationHandler realProxy;
  
  private boolean requiresEscapingEncoder;
  
  public LRUCache<String, CachedResultSetMetaData> resultSetMetadataCache;
  
  private CacheAdapter<String, Map<String, String>> serverConfigCache;
  
  private LRUCache<CompoundCacheKey, ServerPreparedStatement> serverSideStatementCache;
  
  private LRUCache<String, Boolean> serverSideStatementCheckCache;
  
  private TimeZone serverTimezoneTZ;
  
  private boolean serverTruncatesFracSecs;
  
  private Map<String, String> serverVariables;
  
  private Calendar sessionCalendar;
  
  private int sessionMaxRows;
  
  private long shortestQueryTimeMs;
  
  private String statementComment;
  
  private List<StatementInterceptorV2> statementInterceptors;
  
  private boolean storesLowerCaseTableName;
  
  private double totalQueryTimeMs;
  
  private boolean transactionsSupported;
  
  private Map<String, Class<?>> typeMap;
  
  private boolean useAnsiQuotes;
  
  private boolean usePlatformCharsetConverters;
  
  private boolean useServerPreparedStmts;
  
  private String user;
  
  private Calendar utcCalendar;
  
  static {
    ABORT_PERM = new SQLPermission("abort");
    CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();
    mapTransIsolationNameToValue = null;
    NULL_LOGGER = new NullLogger("MySQL");
    customIndexToCharsetMapByUrl = new HashMap<String, Map<Integer, String>>();
    customCharsetToMblenMapByUrl = new HashMap<String, Map<String, Integer>>();
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>(8);
    mapTransIsolationNameToValue = (Map)hashMap;
    Integer integer = Integer.valueOf(1);
    hashMap.put("READ-UNCOMMITED", integer);
    mapTransIsolationNameToValue.put("READ-UNCOMMITTED", integer);
    mapTransIsolationNameToValue.put("READ-COMMITTED", Integer.valueOf(2));
    mapTransIsolationNameToValue.put("REPEATABLE-READ", Integer.valueOf(4));
    mapTransIsolationNameToValue.put("SERIALIZABLE", Integer.valueOf(8));
    if (Util.isJdbc4()) {
      try {
        JDBC_4_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4Connection").getConstructor(new Class[] { String.class, int.class, Properties.class, String.class, String.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_CONNECTION_CTOR = null;
    } 
    random = new Random();
  }
  
  public ConnectionImpl() {
    this.proxy = null;
    this.realProxy = null;
    this.autoCommit = true;
    this.characterSetMetadata = null;
    this.characterSetResultsOnServer = null;
    this.charsetConverterMap = new HashMap<String, Object>(CharsetMapping.getNumberOfCharsetsConfigured());
    this.connectionCreationTimeMillis = 0L;
    this.database = null;
    this.dbmd = null;
    this.hasIsolationLevels = false;
    this.hasQuotedIdentifiers = false;
    this.host = null;
    this.indexToCustomMysqlCharset = null;
    this.mysqlCharsetToCustomMblen = null;
    this.io = null;
    this.isClientTzUTC = false;
    this.isClosed = true;
    this.isInGlobalTx = false;
    this.isRunningOnJDK13 = false;
    this.isolationLevel = 2;
    this.isServerTzUTC = false;
    this.lastQueryFinishedTime = 0L;
    this.log = NULL_LOGGER;
    this.longestQueryTimeMs = 0L;
    this.lowerCaseTableNames = false;
    this.maximumNumberTablesAccessed = 0L;
    this.sessionMaxRows = -1;
    this.minimumNumberTablesAccessed = Long.MAX_VALUE;
    this.myURL = null;
    this.needsPing = false;
    this.netBufferLength = 16384;
    this.noBackslashEscapes = false;
    this.serverTruncatesFracSecs = false;
    this.numberOfPreparedExecutes = 0L;
    this.numberOfPrepares = 0L;
    this.numberOfQueriesIssued = 0L;
    this.numberOfResultSetsCreated = 0L;
    this.oldHistBreakpoints = null;
    this.oldHistCounts = null;
    this.openStatements = new CopyOnWriteArrayList<Statement>();
    this.parserKnowsUnicode = false;
    this.password = null;
    this.port = 3306;
    this.props = null;
    this.readInfoMsg = false;
    this.readOnly = false;
    this.serverTimezoneTZ = null;
    this.serverVariables = null;
    this.shortestQueryTimeMs = Long.MAX_VALUE;
    this.totalQueryTimeMs = 0.0D;
    this.transactionsSupported = false;
    this.useAnsiQuotes = false;
    this.user = null;
    this.useServerPreparedStmts = false;
    this.errorMessageEncoding = "Cp1252";
    this.hasTriedMasterFlag = false;
    this.statementComment = null;
    this.autoIncrementIncrement = 0;
  }
  
  public ConnectionImpl(String paramString1, int paramInt, Properties paramProperties, String paramString2, String paramString3) throws SQLException {
    Enumeration<?> enumeration;
    this.proxy = null;
    this.realProxy = null;
    boolean bool = true;
    this.autoCommit = true;
    this.characterSetMetadata = null;
    this.characterSetResultsOnServer = null;
    this.charsetConverterMap = new HashMap<String, Object>(CharsetMapping.getNumberOfCharsetsConfigured());
    this.connectionCreationTimeMillis = 0L;
    this.database = null;
    this.dbmd = null;
    this.hasIsolationLevels = false;
    this.hasQuotedIdentifiers = false;
    this.host = null;
    this.indexToCustomMysqlCharset = null;
    this.mysqlCharsetToCustomMblen = null;
    this.io = null;
    this.isClientTzUTC = false;
    this.isClosed = true;
    this.isInGlobalTx = false;
    this.isRunningOnJDK13 = false;
    this.isolationLevel = 2;
    this.isServerTzUTC = false;
    this.lastQueryFinishedTime = 0L;
    this.log = NULL_LOGGER;
    this.longestQueryTimeMs = 0L;
    this.lowerCaseTableNames = false;
    this.maximumNumberTablesAccessed = 0L;
    this.sessionMaxRows = -1;
    this.minimumNumberTablesAccessed = Long.MAX_VALUE;
    this.myURL = null;
    this.needsPing = false;
    this.netBufferLength = 16384;
    this.noBackslashEscapes = false;
    this.serverTruncatesFracSecs = false;
    this.numberOfPreparedExecutes = 0L;
    this.numberOfPrepares = 0L;
    this.numberOfQueriesIssued = 0L;
    this.numberOfResultSetsCreated = 0L;
    this.oldHistBreakpoints = null;
    this.oldHistCounts = null;
    this.openStatements = new CopyOnWriteArrayList<Statement>();
    this.parserKnowsUnicode = false;
    this.password = null;
    this.port = 3306;
    this.props = null;
    this.readInfoMsg = false;
    this.readOnly = false;
    this.serverTimezoneTZ = null;
    this.serverVariables = null;
    this.shortestQueryTimeMs = Long.MAX_VALUE;
    this.totalQueryTimeMs = 0.0D;
    this.transactionsSupported = false;
    this.useAnsiQuotes = false;
    this.user = null;
    this.useServerPreparedStmts = false;
    this.errorMessageEncoding = "Cp1252";
    this.hasTriedMasterFlag = false;
    this.statementComment = null;
    this.autoIncrementIncrement = 0;
    this.connectionCreationTimeMillis = System.currentTimeMillis();
    String str2 = paramString2;
    if (paramString2 == null)
      str2 = ""; 
    this.origHostToConnectTo = paramString1;
    this.origPortToConnectTo = paramInt;
    this.origDatabaseToConnectTo = str2;
    try {
      Blob.class.getMethod("truncate", new Class[] { long.class });
      this.isRunningOnJDK13 = false;
    } catch (NoSuchMethodException noSuchMethodException) {
      this.isRunningOnJDK13 = true;
    } 
    this.sessionCalendar = new GregorianCalendar();
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    this.utcCalendar = gregorianCalendar;
    gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());
    if (NonRegisteringDriver.isHostPropertiesList(paramString1)) {
      Properties properties = NonRegisteringDriver.expandHostKeyValues(paramString1);
      enumeration = properties.propertyNames();
      while (enumeration.hasMoreElements()) {
        String str = enumeration.nextElement().toString();
        paramProperties.setProperty(str, properties.getProperty(str));
      } 
    } else {
      StringBuilder stringBuilder;
      if (enumeration == null) {
        this.host = "localhost";
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.host);
        stringBuilder.append(":");
        stringBuilder.append(paramInt);
        this.hostPortPair = stringBuilder.toString();
      } else {
        this.host = (String)stringBuilder;
        if (stringBuilder.indexOf(":") == -1) {
          stringBuilder = new StringBuilder();
          stringBuilder.append(this.host);
          stringBuilder.append(":");
          stringBuilder.append(paramInt);
          this.hostPortPair = stringBuilder.toString();
        } else {
          this.hostPortPair = this.host;
        } 
      } 
    } 
    this.port = paramInt;
    this.database = str2;
    this.myURL = paramString3;
    this.user = paramProperties.getProperty("user");
    this.password = paramProperties.getProperty("password");
    String str1 = this.user;
    if (str1 == null || str1.equals(""))
      this.user = ""; 
    if (this.password == null)
      this.password = ""; 
    this.props = paramProperties;
    initializeDriverProperties(paramProperties);
    TimeZone timeZone = TimeUtil.getDefaultTimeZone(getCacheDefaultTimezone());
    this.defaultTimeZone = timeZone;
    if (timeZone.useDaylightTime() || this.defaultTimeZone.getRawOffset() != 0)
      bool = false; 
    this.isClientTzUTC = bool;
    if (getUseUsageAdvisor()) {
      this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
    } else {
      this.pointOfOrigin = "";
    } 
    try {
      this.dbmd = getMetaData(false, false);
      initializeSafeStatementInterceptors();
      createNewIO(false);
      unSafeStatementInterceptors();
      NonRegisteringDriver.trackConnection(this);
      return;
    } catch (SQLException sQLException) {
      cleanup(sQLException);
      throw sQLException;
    } catch (Exception exception) {
      cleanup(exception);
      StringBuilder stringBuilder = new StringBuilder(128);
      if (!getParanoid()) {
        stringBuilder.append("Cannot connect to MySQL server on ");
        stringBuilder.append(this.host);
        stringBuilder.append(":");
        stringBuilder.append(this.port);
        stringBuilder.append(".\n\n");
        stringBuilder.append("Make sure that there is a MySQL server ");
        stringBuilder.append("running on the machine/port you are trying ");
        stringBuilder.append("to connect to and that the machine this software is running on ");
        stringBuilder.append("is able to connect to this host/port (i.e. not firewalled). ");
        stringBuilder.append("Also make sure that the server has not been started with the --skip-networking ");
        stringBuilder.append("flag.\n\n");
      } else {
        stringBuilder.append("Unable to connect to database.");
      } 
      SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "08S01", getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void addToHistogram(int[] paramArrayOfint, long[] paramArrayOflong, long paramLong1, int paramInt, long paramLong2, long paramLong3) {
    if (paramArrayOfint == null) {
      createInitialHistogram(paramArrayOflong, paramLong2, paramLong3);
    } else {
      for (byte b = 0; b < 20; b++) {
        if (paramArrayOflong[b] >= paramLong1) {
          paramArrayOfint[b] = paramArrayOfint[b] + paramInt;
          break;
        } 
      } 
    } 
  }
  
  private void addToPerformanceHistogram(long paramLong, int paramInt) {
    checkAndCreatePerformanceHistogram();
    int[] arrayOfInt = this.perfMetricsHistCounts;
    long[] arrayOfLong = this.perfMetricsHistBreakpoints;
    long l2 = this.shortestQueryTimeMs;
    long l1 = l2;
    if (l2 == Long.MAX_VALUE)
      l1 = 0L; 
    addToHistogram(arrayOfInt, arrayOfLong, paramLong, paramInt, l1, this.longestQueryTimeMs);
  }
  
  private void addToTablesAccessedHistogram(long paramLong, int paramInt) {
    checkAndCreateTablesAccessedHistogram();
    int[] arrayOfInt = this.numTablesMetricsHistCounts;
    long[] arrayOfLong = this.numTablesMetricsHistBreakpoints;
    long l2 = this.minimumNumberTablesAccessed;
    long l1 = l2;
    if (l2 == Long.MAX_VALUE)
      l1 = 0L; 
    addToHistogram(arrayOfInt, arrayOfLong, paramLong, paramInt, l1, this.maximumNumberTablesAccessed);
  }
  
  public static SQLException appendMessageToException(SQLException paramSQLException, String paramString, ExceptionInterceptor paramExceptionInterceptor) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getMessage : ()Ljava/lang/String;
    //   4: astore #4
    //   6: aload_0
    //   7: invokevirtual getSQLState : ()Ljava/lang/String;
    //   10: astore #5
    //   12: aload_0
    //   13: invokevirtual getErrorCode : ()I
    //   16: istore_3
    //   17: new java/lang/StringBuilder
    //   20: dup
    //   21: aload #4
    //   23: invokevirtual length : ()I
    //   26: aload_1
    //   27: invokevirtual length : ()I
    //   30: iadd
    //   31: invokespecial <init> : (I)V
    //   34: astore #6
    //   36: aload #6
    //   38: aload #4
    //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload #6
    //   46: aload_1
    //   47: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload #6
    //   53: invokevirtual toString : ()Ljava/lang/String;
    //   56: aload #5
    //   58: iload_3
    //   59: aload_2
    //   60: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;ILcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   63: astore_1
    //   64: ldc_w 'java.lang.StackTraceElement'
    //   67: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   70: iconst_1
    //   71: newarray int
    //   73: dup
    //   74: iconst_0
    //   75: iconst_0
    //   76: iastore
    //   77: invokestatic newInstance : (Ljava/lang/Class;[I)Ljava/lang/Object;
    //   80: invokevirtual getClass : ()Ljava/lang/Class;
    //   83: astore #4
    //   85: ldc_w java/lang/Throwable
    //   88: ldc_w 'getStackTrace'
    //   91: iconst_0
    //   92: anewarray java/lang/Class
    //   95: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   98: astore_2
    //   99: ldc_w java/lang/Throwable
    //   102: ldc_w 'setStackTrace'
    //   105: iconst_1
    //   106: anewarray java/lang/Class
    //   109: dup
    //   110: iconst_0
    //   111: aload #4
    //   113: aastore
    //   114: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   117: astore #4
    //   119: aload_2
    //   120: ifnull -> 151
    //   123: aload #4
    //   125: ifnull -> 151
    //   128: aload #4
    //   130: aload_1
    //   131: iconst_1
    //   132: anewarray java/lang/Object
    //   135: dup
    //   136: iconst_0
    //   137: aload_2
    //   138: aload_0
    //   139: iconst_0
    //   140: anewarray java/lang/Object
    //   143: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   146: aastore
    //   147: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   150: pop
    //   151: aload_1
    //   152: areturn
    //   153: astore_0
    //   154: goto -> 151
    // Exception table:
    //   from	to	target	type
    //   64	119	153	java/lang/NoClassDefFoundError
    //   64	119	153	java/lang/NoSuchMethodException
    //   64	119	153	finally
    //   128	151	153	java/lang/NoClassDefFoundError
    //   128	151	153	java/lang/NoSuchMethodException
    //   128	151	153	finally
  }
  
  private void buildCollationMapping() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getCacheServerConfiguration : ()Z
    //   4: ifeq -> 58
    //   7: getstatic com/mysql/jdbc/ConnectionImpl.customIndexToCharsetMapByUrl : Ljava/util/Map;
    //   10: astore #4
    //   12: aload #4
    //   14: monitorenter
    //   15: aload #4
    //   17: aload_0
    //   18: invokevirtual getURL : ()Ljava/lang/String;
    //   21: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   26: checkcast java/util/Map
    //   29: astore_3
    //   30: getstatic com/mysql/jdbc/ConnectionImpl.customCharsetToMblenMapByUrl : Ljava/util/Map;
    //   33: aload_0
    //   34: invokevirtual getURL : ()Ljava/lang/String;
    //   37: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   42: checkcast java/util/Map
    //   45: astore_2
    //   46: aload #4
    //   48: monitorexit
    //   49: goto -> 62
    //   52: astore_2
    //   53: aload #4
    //   55: monitorexit
    //   56: aload_2
    //   57: athrow
    //   58: aconst_null
    //   59: astore_3
    //   60: aconst_null
    //   61: astore_2
    //   62: aload_3
    //   63: astore #4
    //   65: aload_2
    //   66: astore #5
    //   68: aload_3
    //   69: ifnonnull -> 946
    //   72: aload_3
    //   73: astore #4
    //   75: aload_2
    //   76: astore #5
    //   78: aload_0
    //   79: invokevirtual getDetectCustomCollations : ()Z
    //   82: ifeq -> 946
    //   85: aload_3
    //   86: astore #4
    //   88: aload_2
    //   89: astore #5
    //   91: aload_0
    //   92: iconst_4
    //   93: iconst_1
    //   94: iconst_0
    //   95: invokevirtual versionMeetsMinimum : (III)Z
    //   98: ifeq -> 946
    //   101: new java/util/HashMap
    //   104: astore #10
    //   106: aload #10
    //   108: invokespecial <init> : ()V
    //   111: new java/util/HashMap
    //   114: astore #11
    //   116: aload #11
    //   118: invokespecial <init> : ()V
    //   121: aload_0
    //   122: invokevirtual getMetadataSafeStatement : ()Ljava/sql/Statement;
    //   125: astore #8
    //   127: aload #8
    //   129: ldc_w 'SHOW COLLATION'
    //   132: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   137: astore #5
    //   139: aload #5
    //   141: astore #4
    //   143: aload #5
    //   145: astore_3
    //   146: aload #8
    //   148: astore #9
    //   150: aload #5
    //   152: astore_2
    //   153: aload #5
    //   155: invokeinterface next : ()Z
    //   160: ifeq -> 386
    //   163: aload #5
    //   165: astore_3
    //   166: aload #8
    //   168: astore #9
    //   170: aload #5
    //   172: astore_2
    //   173: aload #5
    //   175: iconst_3
    //   176: invokeinterface getObject : (I)Ljava/lang/Object;
    //   181: checkcast java/lang/Number
    //   184: invokevirtual intValue : ()I
    //   187: istore_1
    //   188: aload #5
    //   190: astore_3
    //   191: aload #8
    //   193: astore #9
    //   195: aload #5
    //   197: astore_2
    //   198: aload #5
    //   200: iconst_2
    //   201: invokeinterface getString : (I)Ljava/lang/String;
    //   206: astore #4
    //   208: iload_1
    //   209: sipush #2048
    //   212: if_icmpge -> 240
    //   215: aload #5
    //   217: astore_3
    //   218: aload #8
    //   220: astore #9
    //   222: aload #5
    //   224: astore_2
    //   225: aload #4
    //   227: iload_1
    //   228: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   231: invokestatic getMysqlCharsetNameForCollationIndex : (Ljava/lang/Integer;)Ljava/lang/String;
    //   234: invokevirtual equals : (Ljava/lang/Object;)Z
    //   237: ifne -> 264
    //   240: aload #5
    //   242: astore_3
    //   243: aload #8
    //   245: astore #9
    //   247: aload #5
    //   249: astore_2
    //   250: aload #10
    //   252: iload_1
    //   253: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   256: aload #4
    //   258: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   263: pop
    //   264: aload #5
    //   266: astore_3
    //   267: aload #8
    //   269: astore #9
    //   271: aload #5
    //   273: astore_2
    //   274: getstatic com/mysql/jdbc/CharsetMapping.CHARSET_NAME_TO_CHARSET : Ljava/util/Map;
    //   277: aload #4
    //   279: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   284: ifne -> 139
    //   287: aload #5
    //   289: astore_3
    //   290: aload #8
    //   292: astore #9
    //   294: aload #5
    //   296: astore_2
    //   297: aload #11
    //   299: aload #4
    //   301: aconst_null
    //   302: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   307: pop
    //   308: goto -> 139
    //   311: astore_2
    //   312: aload #5
    //   314: astore #4
    //   316: aload_2
    //   317: astore #5
    //   319: goto -> 340
    //   322: astore_2
    //   323: aconst_null
    //   324: astore_3
    //   325: goto -> 918
    //   328: astore #4
    //   330: aconst_null
    //   331: astore_3
    //   332: goto -> 852
    //   335: astore #5
    //   337: aconst_null
    //   338: astore #4
    //   340: aload #4
    //   342: astore_3
    //   343: aload #4
    //   345: astore #7
    //   347: aload #8
    //   349: astore #9
    //   351: aload #4
    //   353: astore_2
    //   354: aload #5
    //   356: invokevirtual getErrorCode : ()I
    //   359: sipush #1820
    //   362: if_icmpne -> 807
    //   365: aload #4
    //   367: astore_3
    //   368: aload #4
    //   370: astore #7
    //   372: aload #8
    //   374: astore #9
    //   376: aload #4
    //   378: astore_2
    //   379: aload_0
    //   380: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   383: ifne -> 807
    //   386: aload #4
    //   388: astore_3
    //   389: aload #4
    //   391: astore #7
    //   393: aload #8
    //   395: astore #9
    //   397: aload #4
    //   399: astore_2
    //   400: aload #11
    //   402: invokeinterface size : ()I
    //   407: istore_1
    //   408: aload #4
    //   410: astore #6
    //   412: iload_1
    //   413: ifle -> 634
    //   416: aload #4
    //   418: astore #5
    //   420: aload #4
    //   422: astore_3
    //   423: aload #8
    //   425: astore #9
    //   427: aload #4
    //   429: astore_2
    //   430: aload #8
    //   432: ldc_w 'SHOW CHARACTER SET'
    //   435: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   440: astore #4
    //   442: aload #4
    //   444: astore #5
    //   446: aload #4
    //   448: astore #6
    //   450: aload #4
    //   452: astore_3
    //   453: aload #8
    //   455: astore #9
    //   457: aload #4
    //   459: astore_2
    //   460: aload #4
    //   462: invokeinterface next : ()Z
    //   467: ifeq -> 634
    //   470: aload #4
    //   472: astore #5
    //   474: aload #4
    //   476: astore_3
    //   477: aload #8
    //   479: astore #9
    //   481: aload #4
    //   483: astore_2
    //   484: aload #4
    //   486: ldc_w 'Charset'
    //   489: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   494: astore #6
    //   496: aload #4
    //   498: astore #5
    //   500: aload #4
    //   502: astore_3
    //   503: aload #8
    //   505: astore #9
    //   507: aload #4
    //   509: astore_2
    //   510: aload #11
    //   512: aload #6
    //   514: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   519: ifeq -> 442
    //   522: aload #4
    //   524: astore #5
    //   526: aload #4
    //   528: astore_3
    //   529: aload #8
    //   531: astore #9
    //   533: aload #4
    //   535: astore_2
    //   536: aload #11
    //   538: aload #6
    //   540: aload #4
    //   542: ldc_w 'Maxlen'
    //   545: invokeinterface getInt : (Ljava/lang/String;)I
    //   550: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   553: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   558: pop
    //   559: goto -> 442
    //   562: astore #4
    //   564: aload #5
    //   566: astore_3
    //   567: aload #5
    //   569: astore #7
    //   571: aload #8
    //   573: astore #9
    //   575: aload #5
    //   577: astore_2
    //   578: aload #4
    //   580: invokevirtual getErrorCode : ()I
    //   583: sipush #1820
    //   586: if_icmpne -> 617
    //   589: aload #5
    //   591: astore_3
    //   592: aload #5
    //   594: astore #7
    //   596: aload #8
    //   598: astore #9
    //   600: aload #5
    //   602: astore_2
    //   603: aload_0
    //   604: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   607: ifne -> 617
    //   610: aload #5
    //   612: astore #6
    //   614: goto -> 634
    //   617: aload #5
    //   619: astore_3
    //   620: aload #5
    //   622: astore #7
    //   624: aload #8
    //   626: astore #9
    //   628: aload #5
    //   630: astore_2
    //   631: aload #4
    //   633: athrow
    //   634: aload #6
    //   636: astore_3
    //   637: aload #6
    //   639: astore #7
    //   641: aload #8
    //   643: astore #9
    //   645: aload #6
    //   647: astore_2
    //   648: aload_0
    //   649: invokevirtual getCacheServerConfiguration : ()Z
    //   652: ifeq -> 748
    //   655: aload #6
    //   657: astore_3
    //   658: aload #6
    //   660: astore #7
    //   662: aload #8
    //   664: astore #9
    //   666: aload #6
    //   668: astore_2
    //   669: getstatic com/mysql/jdbc/ConnectionImpl.customIndexToCharsetMapByUrl : Ljava/util/Map;
    //   672: astore #4
    //   674: aload #6
    //   676: astore_3
    //   677: aload #6
    //   679: astore #7
    //   681: aload #8
    //   683: astore #9
    //   685: aload #6
    //   687: astore_2
    //   688: aload #4
    //   690: monitorenter
    //   691: aload #4
    //   693: aload_0
    //   694: invokevirtual getURL : ()Ljava/lang/String;
    //   697: aload #10
    //   699: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   704: pop
    //   705: getstatic com/mysql/jdbc/ConnectionImpl.customCharsetToMblenMapByUrl : Ljava/util/Map;
    //   708: aload_0
    //   709: invokevirtual getURL : ()Ljava/lang/String;
    //   712: aload #11
    //   714: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   719: pop
    //   720: aload #4
    //   722: monitorexit
    //   723: goto -> 748
    //   726: astore #5
    //   728: aload #4
    //   730: monitorexit
    //   731: aload #6
    //   733: astore_3
    //   734: aload #6
    //   736: astore #7
    //   738: aload #8
    //   740: astore #9
    //   742: aload #6
    //   744: astore_2
    //   745: aload #5
    //   747: athrow
    //   748: aload #6
    //   750: ifnull -> 764
    //   753: aload #6
    //   755: invokeinterface close : ()V
    //   760: goto -> 764
    //   763: astore_2
    //   764: aload #10
    //   766: astore #4
    //   768: aload #11
    //   770: astore #5
    //   772: aload #8
    //   774: ifnull -> 946
    //   777: aload #8
    //   779: invokeinterface close : ()V
    //   784: aload #10
    //   786: astore #4
    //   788: aload #11
    //   790: astore #5
    //   792: goto -> 946
    //   795: astore_2
    //   796: aload #10
    //   798: astore #4
    //   800: aload #11
    //   802: astore #5
    //   804: goto -> 946
    //   807: aload #4
    //   809: astore_3
    //   810: aload #4
    //   812: astore #7
    //   814: aload #8
    //   816: astore #9
    //   818: aload #4
    //   820: astore_2
    //   821: aload #5
    //   823: athrow
    //   824: astore #4
    //   826: goto -> 852
    //   829: astore_3
    //   830: aload #7
    //   832: astore_2
    //   833: goto -> 901
    //   836: astore_2
    //   837: aconst_null
    //   838: astore_3
    //   839: aconst_null
    //   840: astore #8
    //   842: goto -> 918
    //   845: astore #4
    //   847: aconst_null
    //   848: astore #8
    //   850: aconst_null
    //   851: astore_3
    //   852: aload #8
    //   854: astore #9
    //   856: aload_3
    //   857: astore_2
    //   858: aload #4
    //   860: invokevirtual toString : ()Ljava/lang/String;
    //   863: ldc_w 'S1009'
    //   866: aconst_null
    //   867: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   870: astore #5
    //   872: aload #8
    //   874: astore #9
    //   876: aload_3
    //   877: astore_2
    //   878: aload #5
    //   880: aload #4
    //   882: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   885: pop
    //   886: aload #8
    //   888: astore #9
    //   890: aload_3
    //   891: astore_2
    //   892: aload #5
    //   894: athrow
    //   895: astore_3
    //   896: aconst_null
    //   897: astore_2
    //   898: aconst_null
    //   899: astore #8
    //   901: aload #8
    //   903: astore #9
    //   905: aload_3
    //   906: athrow
    //   907: astore #4
    //   909: aload_2
    //   910: astore_3
    //   911: aload #4
    //   913: astore_2
    //   914: aload #9
    //   916: astore #8
    //   918: aload_3
    //   919: ifnull -> 932
    //   922: aload_3
    //   923: invokeinterface close : ()V
    //   928: goto -> 932
    //   931: astore_3
    //   932: aload #8
    //   934: ifnull -> 944
    //   937: aload #8
    //   939: invokeinterface close : ()V
    //   944: aload_2
    //   945: athrow
    //   946: aload #4
    //   948: ifnull -> 960
    //   951: aload_0
    //   952: aload #4
    //   954: invokestatic unmodifiableMap : (Ljava/util/Map;)Ljava/util/Map;
    //   957: putfield indexToCustomMysqlCharset : Ljava/util/Map;
    //   960: aload #5
    //   962: ifnull -> 974
    //   965: aload_0
    //   966: aload #5
    //   968: invokestatic unmodifiableMap : (Ljava/util/Map;)Ljava/util/Map;
    //   971: putfield mysqlCharsetToCustomMblen : Ljava/util/Map;
    //   974: return
    //   975: astore_3
    //   976: goto -> 944
    // Exception table:
    //   from	to	target	type
    //   15	49	52	finally
    //   53	56	52	finally
    //   101	127	895	java/sql/SQLException
    //   101	127	845	java/lang/RuntimeException
    //   101	127	836	finally
    //   127	139	335	java/sql/SQLException
    //   127	139	328	java/lang/RuntimeException
    //   127	139	322	finally
    //   153	163	311	java/sql/SQLException
    //   153	163	824	java/lang/RuntimeException
    //   153	163	907	finally
    //   173	188	311	java/sql/SQLException
    //   173	188	824	java/lang/RuntimeException
    //   173	188	907	finally
    //   198	208	311	java/sql/SQLException
    //   198	208	824	java/lang/RuntimeException
    //   198	208	907	finally
    //   225	240	311	java/sql/SQLException
    //   225	240	824	java/lang/RuntimeException
    //   225	240	907	finally
    //   250	264	311	java/sql/SQLException
    //   250	264	824	java/lang/RuntimeException
    //   250	264	907	finally
    //   274	287	311	java/sql/SQLException
    //   274	287	824	java/lang/RuntimeException
    //   274	287	907	finally
    //   297	308	311	java/sql/SQLException
    //   297	308	824	java/lang/RuntimeException
    //   297	308	907	finally
    //   354	365	829	java/sql/SQLException
    //   354	365	824	java/lang/RuntimeException
    //   354	365	907	finally
    //   379	386	829	java/sql/SQLException
    //   379	386	824	java/lang/RuntimeException
    //   379	386	907	finally
    //   400	408	829	java/sql/SQLException
    //   400	408	824	java/lang/RuntimeException
    //   400	408	907	finally
    //   430	442	562	java/sql/SQLException
    //   430	442	824	java/lang/RuntimeException
    //   430	442	907	finally
    //   460	470	562	java/sql/SQLException
    //   460	470	824	java/lang/RuntimeException
    //   460	470	907	finally
    //   484	496	562	java/sql/SQLException
    //   484	496	824	java/lang/RuntimeException
    //   484	496	907	finally
    //   510	522	562	java/sql/SQLException
    //   510	522	824	java/lang/RuntimeException
    //   510	522	907	finally
    //   536	559	562	java/sql/SQLException
    //   536	559	824	java/lang/RuntimeException
    //   536	559	907	finally
    //   578	589	829	java/sql/SQLException
    //   578	589	824	java/lang/RuntimeException
    //   578	589	907	finally
    //   603	610	829	java/sql/SQLException
    //   603	610	824	java/lang/RuntimeException
    //   603	610	907	finally
    //   631	634	829	java/sql/SQLException
    //   631	634	824	java/lang/RuntimeException
    //   631	634	907	finally
    //   648	655	829	java/sql/SQLException
    //   648	655	824	java/lang/RuntimeException
    //   648	655	907	finally
    //   669	674	829	java/sql/SQLException
    //   669	674	824	java/lang/RuntimeException
    //   669	674	907	finally
    //   688	691	829	java/sql/SQLException
    //   688	691	824	java/lang/RuntimeException
    //   688	691	907	finally
    //   691	723	726	finally
    //   728	731	726	finally
    //   745	748	829	java/sql/SQLException
    //   745	748	824	java/lang/RuntimeException
    //   745	748	907	finally
    //   753	760	763	java/sql/SQLException
    //   777	784	795	java/sql/SQLException
    //   821	824	829	java/sql/SQLException
    //   821	824	824	java/lang/RuntimeException
    //   821	824	907	finally
    //   858	872	907	finally
    //   878	886	907	finally
    //   892	895	907	finally
    //   905	907	907	finally
    //   922	928	931	java/sql/SQLException
    //   937	944	975	java/sql/SQLException
  }
  
  private boolean canHandleAsServerPreparedStatement(String paramString) throws SQLException {
    if (paramString == null || paramString.length() == 0)
      return true; 
    if (!this.useServerPreparedStmts)
      return false; 
    if (getCachePreparedStatements())
      synchronized (this.serverSideStatementCheckCache) {
        Boolean bool1 = this.serverSideStatementCheckCache.get(paramString);
        if (bool1 != null)
          return bool1.booleanValue(); 
        boolean bool = canHandleAsServerPreparedStatementNoCache(paramString);
        if (paramString.length() < getPreparedStatementCacheSqlLimit()) {
          LRUCache<String, Boolean> lRUCache = this.serverSideStatementCheckCache;
          if (bool) {
            bool1 = Boolean.TRUE;
          } else {
            bool1 = Boolean.FALSE;
          } 
          lRUCache.put(paramString, bool1);
        } 
        return bool;
      }  
    return canHandleAsServerPreparedStatementNoCache(paramString);
  }
  
  private boolean canHandleAsServerPreparedStatementNoCache(String paramString) throws SQLException {
    String str;
    boolean bool1 = StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(paramString, "CALL");
    int j = 0;
    boolean bool = false;
    if (bool1)
      return false; 
    int i = this.noBackslashEscapes ^ true;
    if (this.useAnsiQuotes) {
      str = "\"";
    } else {
      str = "'";
    } 
    if (getAllowMultiQueries()) {
      Set<StringUtils.SearchMode> set;
      if (i != 0) {
        set = StringUtils.SEARCH_MODE__ALL;
      } else {
        set = StringUtils.SEARCH_MODE__MRK_COM_WS;
      } 
      if (StringUtils.indexOfIgnoreCase(0, paramString, ";", str, str, set) != -1)
        return j; 
    } else {
      if (!versionMeetsMinimum(5, 0, 7) && (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(paramString, "SELECT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(paramString, "DELETE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(paramString, "INSERT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(paramString, "UPDATE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(paramString, "REPLACE"))) {
        int m = paramString.length();
        int k = 0;
        label67: while (k < m - 7) {
          Set<StringUtils.SearchMode> set;
          if (i != 0) {
            set = StringUtils.SEARCH_MODE__ALL;
          } else {
            set = StringUtils.SEARCH_MODE__MRK_COM_WS;
          } 
          int n = StringUtils.indexOfIgnoreCase(k, paramString, "LIMIT ", str, str, set);
          if (n == -1)
            break; 
          n += 7;
          while (true) {
            k = n;
            if (n < m) {
              char c = paramString.charAt(n);
              if (!Character.isDigit(c) && !Character.isWhitespace(c) && c != ',' && c != '?') {
                k = n;
                continue label67;
              } 
              if (c == '?') {
                bool = true;
                k = n;
                continue label67;
              } 
              n++;
              continue;
            } 
            continue label67;
          } 
        } 
        j = bool ^ true;
      } else {
        if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "XA ") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "CREATE TABLE") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "DO") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "SET") || (StringUtils.startsWithIgnoreCaseAndWs(paramString, "SHOW WARNINGS") && versionMeetsMinimum(5, 7, 2)) || paramString.startsWith("/* ping */"))
          return j; 
        j = 1;
      } 
      return j;
    } 
    j = 1;
  }
  
  private boolean characterSetNamesMatches(String paramString) {
    boolean bool;
    if (paramString != null && paramString.equalsIgnoreCase(this.serverVariables.get("character_set_client")) && paramString.equalsIgnoreCase(this.serverVariables.get("character_set_connection"))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void checkAndCreatePerformanceHistogram() {
    if (this.perfMetricsHistCounts == null)
      this.perfMetricsHistCounts = new int[20]; 
    if (this.perfMetricsHistBreakpoints == null)
      this.perfMetricsHistBreakpoints = new long[20]; 
  }
  
  private void checkAndCreateTablesAccessedHistogram() {
    if (this.numTablesMetricsHistCounts == null)
      this.numTablesMetricsHistCounts = new int[20]; 
    if (this.numTablesMetricsHistBreakpoints == null)
      this.numTablesMetricsHistBreakpoints = new long[20]; 
  }
  
  private void checkServerEncoding() throws SQLException {
    SQLException sQLException;
    if (getUseUnicode() && getEncoding() != null)
      return; 
    String str2 = this.serverVariables.get("character_set");
    String str1 = str2;
    if (str2 == null)
      str1 = this.serverVariables.get("character_set_server"); 
    str2 = null;
    if (str1 != null)
      try {
        str2 = CharsetMapping.getJavaEncodingForMysqlCharset(str1);
      } catch (RuntimeException runtimeException) {
        sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
        sQLException.initCause(runtimeException);
        throw sQLException;
      }  
    if (!getUseUnicode() && runtimeException != null && getCharsetConverter((String)runtimeException) != null) {
      setUseUnicode(true);
      setEncoding((String)runtimeException);
      return;
    } 
    if (sQLException != null) {
      StringBuilder stringBuilder;
      if (runtimeException == null && Character.isLowerCase(sQLException.charAt(0))) {
        char[] arrayOfChar = sQLException.toCharArray();
        arrayOfChar[0] = Character.toUpperCase(sQLException.charAt(0));
        setEncoding(new String(arrayOfChar));
      } 
      if (runtimeException != null) {
        try {
          StringUtils.getBytes("abc", (String)runtimeException);
          setEncoding((String)runtimeException);
          setUseUnicode(true);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("The driver can not map the character encoding '");
          stringBuilder.append(getEncoding());
          stringBuilder.append("' that your server is using ");
          stringBuilder.append("to a character encoding your JVM understands. You can specify this mapping manually by adding \"useUnicode=true\" ");
          stringBuilder.append("as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" to your JDBC URL.");
          throw SQLError.createSQLException(stringBuilder.toString(), "0S100", getExceptionInterceptor());
        } 
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Unknown character encoding on server '");
        stringBuilder1.append((String)stringBuilder);
        stringBuilder1.append("', use 'characterEncoding=' property ");
        stringBuilder1.append(" to provide correct mapping");
        throw SQLError.createSQLException(stringBuilder1.toString(), "01S00", getExceptionInterceptor());
      } 
    } 
  }
  
  private void checkTransactionIsolationLevel() throws SQLException {
    String str2 = this.serverVariables.get("transaction_isolation");
    String str1 = str2;
    if (str2 == null)
      str1 = this.serverVariables.get("tx_isolation"); 
    if (str1 != null) {
      Integer integer = mapTransIsolationNameToValue.get(str1);
      if (integer != null)
        this.isolationLevel = integer.intValue(); 
    } 
  }
  
  private void cleanup(Throwable paramThrowable) {
    try {
      if (this.io != null)
        if (isClosed()) {
          this.io.forceClose();
        } else {
          realClose(false, false, false, paramThrowable);
        }  
    } catch (SQLException sQLException) {}
    this.isClosed = true;
  }
  
  private void closeAllOpenStatements() throws SQLException {
    Iterator<Statement> iterator = this.openStatements.iterator();
    sQLException = null;
    while (iterator.hasNext()) {
      Statement statement = iterator.next();
      try {
        ((StatementImpl)statement).realClose(false, true);
      } catch (SQLException sQLException) {}
    } 
    if (sQLException == null)
      return; 
    throw sQLException;
  }
  
  private void closeStatement(Statement paramStatement) {
    if (paramStatement != null)
      try {
        paramStatement.close();
      } catch (SQLException sQLException) {} 
  }
  
  private void configureCharsetProperties() throws SQLException {
    if (getEncoding() != null)
      try {
        StringUtils.getBytes("abc", getEncoding());
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        String str = getEncoding();
        try {
          StringBuilder stringBuilder;
          setEncoding(CharsetMapping.getJavaEncodingForMysqlCharset(str));
          if (getEncoding() != null) {
            try {
              StringUtils.getBytes("abc", getEncoding());
            } catch (UnsupportedEncodingException unsupportedEncodingException1) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("Unsupported character encoding '");
              stringBuilder.append(getEncoding());
              stringBuilder.append("'.");
              throw SQLError.createSQLException(stringBuilder.toString(), "01S00", getExceptionInterceptor());
            } 
          } else {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("Java does not support the MySQL character encoding '");
            stringBuilder1.append((String)stringBuilder);
            stringBuilder1.append("'.");
            throw SQLError.createSQLException(stringBuilder1.toString(), "01S00", getExceptionInterceptor());
          } 
        } catch (RuntimeException runtimeException) {
          SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
          sQLException.initCause(runtimeException);
          throw sQLException;
        } 
      }  
  }
  
  private boolean configureClientCharacterSet(boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getEncoding : ()Ljava/lang/String;
    //   4: astore #5
    //   6: aload #5
    //   8: astore #6
    //   10: aload_0
    //   11: iconst_4
    //   12: iconst_1
    //   13: iconst_0
    //   14: invokevirtual versionMeetsMinimum : (III)Z
    //   17: istore_3
    //   18: iload_3
    //   19: ifeq -> 2486
    //   22: aload #5
    //   24: astore #6
    //   26: aload_0
    //   27: iconst_1
    //   28: invokevirtual setUseUnicode : (Z)V
    //   31: aload #5
    //   33: astore #6
    //   35: aload_0
    //   36: invokespecial configureCharsetProperties : ()V
    //   39: aload #5
    //   41: astore #6
    //   43: aload_0
    //   44: invokevirtual getEncoding : ()Ljava/lang/String;
    //   47: astore #5
    //   49: aload #5
    //   51: astore #6
    //   53: aload_0
    //   54: invokevirtual getUseOldUTF8Behavior : ()Z
    //   57: istore_3
    //   58: ldc_w ''
    //   61: astore #7
    //   63: iload_3
    //   64: ifne -> 234
    //   67: aload #5
    //   69: astore #6
    //   71: aload_0
    //   72: invokevirtual getConnectionCollation : ()Ljava/lang/String;
    //   75: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   78: ifne -> 234
    //   81: ldc_w ''
    //   84: astore #8
    //   86: iconst_1
    //   87: istore_2
    //   88: aload #5
    //   90: astore #6
    //   92: getstatic com/mysql/jdbc/CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME : [Ljava/lang/String;
    //   95: astore #10
    //   97: aload #5
    //   99: astore #6
    //   101: iload_2
    //   102: aload #10
    //   104: arraylength
    //   105: if_icmpge -> 223
    //   108: aload #5
    //   110: astore #9
    //   112: aload #5
    //   114: astore #6
    //   116: aload #10
    //   118: iload_2
    //   119: aaload
    //   120: aload_0
    //   121: invokevirtual getConnectionCollation : ()Ljava/lang/String;
    //   124: invokevirtual equals : (Ljava/lang/Object;)Z
    //   127: ifeq -> 213
    //   130: aload #5
    //   132: astore #6
    //   134: new java/lang/StringBuilder
    //   137: astore #7
    //   139: aload #5
    //   141: astore #6
    //   143: aload #7
    //   145: invokespecial <init> : ()V
    //   148: aload #5
    //   150: astore #6
    //   152: aload #7
    //   154: ldc_w ' COLLATE '
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: aload #5
    //   163: astore #6
    //   165: aload #7
    //   167: aload #10
    //   169: iload_2
    //   170: aaload
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload #5
    //   177: astore #6
    //   179: aload #7
    //   181: invokevirtual toString : ()Ljava/lang/String;
    //   184: astore #7
    //   186: aload #5
    //   188: astore #6
    //   190: getstatic com/mysql/jdbc/CharsetMapping.COLLATION_INDEX_TO_CHARSET : [Lcom/mysql/jdbc/MysqlCharset;
    //   193: iload_2
    //   194: aaload
    //   195: getfield charsetName : Ljava/lang/String;
    //   198: astore #8
    //   200: aload #5
    //   202: astore #6
    //   204: iload_2
    //   205: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   208: invokestatic getJavaEncodingForCollationIndex : (Ljava/lang/Integer;)Ljava/lang/String;
    //   211: astore #9
    //   213: iinc #2, 1
    //   216: aload #9
    //   218: astore #5
    //   220: goto -> 88
    //   223: aload #8
    //   225: astore #6
    //   227: aload #7
    //   229: astore #9
    //   231: goto -> 243
    //   234: ldc_w ''
    //   237: astore #6
    //   239: aload #7
    //   241: astore #9
    //   243: aload #5
    //   245: astore #7
    //   247: aload_0
    //   248: getfield props : Ljava/util/Properties;
    //   251: astore #8
    //   253: aload #8
    //   255: ifnull -> 297
    //   258: aload #5
    //   260: astore #7
    //   262: aload #8
    //   264: ldc_w 'com.mysql.jdbc.faultInjection.serverCharsetIndex'
    //   267: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   270: ifnull -> 297
    //   273: aload #5
    //   275: astore #7
    //   277: aload_0
    //   278: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   281: aload_0
    //   282: getfield props : Ljava/util/Properties;
    //   285: ldc_w 'com.mysql.jdbc.faultInjection.serverCharsetIndex'
    //   288: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   291: invokestatic parseInt : (Ljava/lang/String;)I
    //   294: putfield serverCharsetIndex : I
    //   297: aload #5
    //   299: astore #7
    //   301: aload_0
    //   302: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   305: getfield serverCharsetIndex : I
    //   308: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   311: invokestatic getJavaEncodingForCollationIndex : (Ljava/lang/Integer;)Ljava/lang/String;
    //   314: astore #10
    //   316: aload #10
    //   318: ifnull -> 339
    //   321: aload #5
    //   323: astore #7
    //   325: aload #10
    //   327: invokevirtual length : ()I
    //   330: ifne -> 336
    //   333: goto -> 339
    //   336: goto -> 357
    //   339: aload #5
    //   341: ifnull -> 481
    //   344: aload #5
    //   346: astore #7
    //   348: aload_0
    //   349: aload #5
    //   351: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   354: goto -> 336
    //   357: aload #10
    //   359: astore #8
    //   361: aload #5
    //   363: astore #7
    //   365: aload_0
    //   366: iconst_4
    //   367: iconst_1
    //   368: iconst_0
    //   369: invokevirtual versionMeetsMinimum : (III)Z
    //   372: ifeq -> 399
    //   375: aload #10
    //   377: astore #8
    //   379: aload #5
    //   381: astore #7
    //   383: ldc_w 'ISO8859_1'
    //   386: aload #10
    //   388: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   391: ifeq -> 399
    //   394: ldc_w 'Cp1252'
    //   397: astore #8
    //   399: aload #5
    //   401: astore #7
    //   403: ldc_w 'UnicodeBig'
    //   406: aload #8
    //   408: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   411: ifne -> 463
    //   414: aload #5
    //   416: astore #7
    //   418: ldc_w 'UTF-16'
    //   421: aload #8
    //   423: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   426: ifne -> 463
    //   429: aload #5
    //   431: astore #7
    //   433: ldc_w 'UTF-16LE'
    //   436: aload #8
    //   438: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   441: ifne -> 463
    //   444: aload #8
    //   446: astore #10
    //   448: aload #5
    //   450: astore #7
    //   452: ldc_w 'UTF-32'
    //   455: aload #8
    //   457: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   460: ifeq -> 468
    //   463: ldc_w 'UTF-8'
    //   466: astore #10
    //   468: aload #5
    //   470: astore #7
    //   472: aload_0
    //   473: aload #10
    //   475: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   478: goto -> 637
    //   481: aload #5
    //   483: astore #7
    //   485: new java/lang/StringBuilder
    //   488: astore #8
    //   490: aload #5
    //   492: astore #7
    //   494: aload #8
    //   496: invokespecial <init> : ()V
    //   499: aload #5
    //   501: astore #7
    //   503: aload #8
    //   505: ldc_w 'Unknown initial character set index ''
    //   508: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   511: pop
    //   512: aload #5
    //   514: astore #7
    //   516: aload #8
    //   518: aload_0
    //   519: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   522: getfield serverCharsetIndex : I
    //   525: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   528: pop
    //   529: aload #5
    //   531: astore #7
    //   533: aload #8
    //   535: ldc_w '' received from server. Initial client character set can be forced via the 'characterEncoding' property.'
    //   538: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   541: pop
    //   542: aload #5
    //   544: astore #7
    //   546: aload #8
    //   548: invokevirtual toString : ()Ljava/lang/String;
    //   551: ldc_w 'S1000'
    //   554: aload_0
    //   555: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   558: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   561: athrow
    //   562: astore #7
    //   564: goto -> 626
    //   567: astore #5
    //   569: aload #7
    //   571: astore #6
    //   573: goto -> 2743
    //   576: astore #8
    //   578: aload #5
    //   580: astore #7
    //   582: aload #8
    //   584: invokevirtual toString : ()Ljava/lang/String;
    //   587: ldc_w 'S1009'
    //   590: aconst_null
    //   591: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   594: astore #6
    //   596: aload #5
    //   598: astore #7
    //   600: aload #6
    //   602: aload #8
    //   604: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   607: pop
    //   608: aload #5
    //   610: astore #7
    //   612: aload #6
    //   614: athrow
    //   615: astore #6
    //   617: aload #5
    //   619: astore #7
    //   621: aload #6
    //   623: athrow
    //   624: astore #7
    //   626: aload #5
    //   628: ifnull -> 2400
    //   631: aload_0
    //   632: aload #5
    //   634: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   637: aload_0
    //   638: invokevirtual getEncoding : ()Ljava/lang/String;
    //   641: astore #7
    //   643: aload #7
    //   645: ifnonnull -> 659
    //   648: aload #5
    //   650: astore #7
    //   652: aload_0
    //   653: ldc_w 'ISO8859_1'
    //   656: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   659: aload_0
    //   660: invokevirtual getUseUnicode : ()Z
    //   663: istore_3
    //   664: iload_3
    //   665: ifeq -> 1634
    //   668: aload #5
    //   670: ifnull -> 1279
    //   673: aload #5
    //   675: astore #7
    //   677: aload #5
    //   679: ldc_w 'UTF-8'
    //   682: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   685: istore_3
    //   686: iload_3
    //   687: ifne -> 929
    //   690: aload #5
    //   692: ldc_w 'UTF8'
    //   695: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   698: ifeq -> 704
    //   701: goto -> 929
    //   704: aload #9
    //   706: invokevirtual length : ()I
    //   709: ifle -> 715
    //   712: goto -> 729
    //   715: aload #5
    //   717: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   720: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   723: aload_0
    //   724: invokestatic getMysqlCharsetForJavaEncoding : (Ljava/lang/String;Lcom/mysql/jdbc/Connection;)Ljava/lang/String;
    //   727: astore #6
    //   729: aload #6
    //   731: ifnull -> 887
    //   734: iload_1
    //   735: ifne -> 763
    //   738: aload #5
    //   740: astore #7
    //   742: aload_0
    //   743: aload #6
    //   745: invokespecial characterSetNamesMatches : (Ljava/lang/String;)Z
    //   748: istore_1
    //   749: iload_1
    //   750: ifne -> 756
    //   753: goto -> 763
    //   756: aload #5
    //   758: astore #6
    //   760: goto -> 891
    //   763: new java/lang/StringBuilder
    //   766: astore #7
    //   768: aload #7
    //   770: invokespecial <init> : ()V
    //   773: aload #7
    //   775: ldc_w 'SET NAMES '
    //   778: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   781: pop
    //   782: aload #7
    //   784: aload #6
    //   786: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   789: pop
    //   790: aload #7
    //   792: aload #9
    //   794: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   797: pop
    //   798: aload #7
    //   800: invokevirtual toString : ()Ljava/lang/String;
    //   803: astore #7
    //   805: aload_0
    //   806: getfield database : Ljava/lang/String;
    //   809: astore #8
    //   811: aload_0
    //   812: aconst_null
    //   813: aload #7
    //   815: iconst_m1
    //   816: aconst_null
    //   817: sipush #1003
    //   820: sipush #1007
    //   823: iconst_0
    //   824: aload #8
    //   826: aconst_null
    //   827: iconst_0
    //   828: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   831: pop
    //   832: aload_0
    //   833: getfield serverVariables : Ljava/util/Map;
    //   836: ldc_w 'character_set_client'
    //   839: aload #6
    //   841: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   846: pop
    //   847: aload_0
    //   848: getfield serverVariables : Ljava/util/Map;
    //   851: ldc_w 'character_set_connection'
    //   854: aload #6
    //   856: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   861: pop
    //   862: goto -> 887
    //   865: astore #6
    //   867: goto -> 872
    //   870: astore #6
    //   872: aload #5
    //   874: astore #7
    //   876: aload #6
    //   878: astore #5
    //   880: aload #7
    //   882: astore #6
    //   884: goto -> 2743
    //   887: aload #5
    //   889: astore #6
    //   891: aload #5
    //   893: astore #6
    //   895: aload_0
    //   896: aload #6
    //   898: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   901: aload #6
    //   903: astore #5
    //   905: goto -> 1631
    //   908: astore #6
    //   910: aload #5
    //   912: astore #7
    //   914: aload #5
    //   916: astore #7
    //   918: aload #6
    //   920: astore #5
    //   922: aload #7
    //   924: astore #6
    //   926: goto -> 2743
    //   929: aload #5
    //   931: astore #7
    //   933: aload_0
    //   934: iconst_5
    //   935: iconst_5
    //   936: iconst_2
    //   937: invokevirtual versionMeetsMinimum : (III)Z
    //   940: istore_3
    //   941: aload #9
    //   943: invokevirtual length : ()I
    //   946: istore_2
    //   947: iload_2
    //   948: ifle -> 954
    //   951: goto -> 980
    //   954: iload_3
    //   955: ifeq -> 975
    //   958: ldc_w 'utf8mb4'
    //   961: astore #6
    //   963: goto -> 951
    //   966: aload #5
    //   968: astore #6
    //   970: astore #6
    //   972: goto -> 914
    //   975: ldc_w 'utf8'
    //   978: astore #6
    //   980: aload_0
    //   981: invokevirtual getUseOldUTF8Behavior : ()Z
    //   984: istore #4
    //   986: iload #4
    //   988: ifne -> 1184
    //   991: iload_1
    //   992: ifne -> 1060
    //   995: aload_0
    //   996: ldc_w 'utf8'
    //   999: invokespecial characterSetNamesMatches : (Ljava/lang/String;)Z
    //   1002: ifeq -> 1060
    //   1005: iload_3
    //   1006: ifeq -> 1019
    //   1009: aload_0
    //   1010: ldc_w 'utf8mb4'
    //   1013: invokespecial characterSetNamesMatches : (Ljava/lang/String;)Z
    //   1016: ifeq -> 1060
    //   1019: aload #9
    //   1021: invokevirtual length : ()I
    //   1024: ifle -> 1057
    //   1027: aload_0
    //   1028: invokevirtual getConnectionCollation : ()Ljava/lang/String;
    //   1031: aload_0
    //   1032: getfield serverVariables : Ljava/util/Map;
    //   1035: ldc_w 'collation_server'
    //   1038: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1043: checkcast java/lang/String
    //   1046: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1049: istore_1
    //   1050: iload_1
    //   1051: ifne -> 1057
    //   1054: goto -> 1060
    //   1057: goto -> 1244
    //   1060: new java/lang/StringBuilder
    //   1063: astore #7
    //   1065: aload #7
    //   1067: invokespecial <init> : ()V
    //   1070: aload #7
    //   1072: ldc_w 'SET NAMES '
    //   1075: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1078: pop
    //   1079: aload #7
    //   1081: aload #6
    //   1083: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1086: pop
    //   1087: aload #7
    //   1089: aload #9
    //   1091: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1094: pop
    //   1095: aload #7
    //   1097: invokevirtual toString : ()Ljava/lang/String;
    //   1100: astore #8
    //   1102: aload_0
    //   1103: getfield database : Ljava/lang/String;
    //   1106: astore #7
    //   1108: aload_0
    //   1109: aconst_null
    //   1110: aload #8
    //   1112: iconst_m1
    //   1113: aconst_null
    //   1114: sipush #1003
    //   1117: sipush #1007
    //   1120: iconst_0
    //   1121: aload #7
    //   1123: aconst_null
    //   1124: iconst_0
    //   1125: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1128: pop
    //   1129: aload_0
    //   1130: getfield serverVariables : Ljava/util/Map;
    //   1133: ldc_w 'character_set_client'
    //   1136: aload #6
    //   1138: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1143: pop
    //   1144: aload_0
    //   1145: getfield serverVariables : Ljava/util/Map;
    //   1148: ldc_w 'character_set_connection'
    //   1151: aload #6
    //   1153: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1158: pop
    //   1159: goto -> 1244
    //   1162: astore #6
    //   1164: goto -> 1169
    //   1167: astore #6
    //   1169: aload #5
    //   1171: astore #7
    //   1173: aload #6
    //   1175: astore #5
    //   1177: aload #7
    //   1179: astore #6
    //   1181: goto -> 2743
    //   1184: aload_0
    //   1185: aconst_null
    //   1186: ldc_w 'SET NAMES latin1'
    //   1189: iconst_m1
    //   1190: aconst_null
    //   1191: sipush #1003
    //   1194: sipush #1007
    //   1197: iconst_0
    //   1198: aload_0
    //   1199: getfield database : Ljava/lang/String;
    //   1202: aconst_null
    //   1203: iconst_0
    //   1204: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1207: pop
    //   1208: aload_0
    //   1209: getfield serverVariables : Ljava/util/Map;
    //   1212: astore #6
    //   1214: aload #6
    //   1216: ldc_w 'character_set_client'
    //   1219: ldc_w 'latin1'
    //   1222: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1227: pop
    //   1228: aload_0
    //   1229: getfield serverVariables : Ljava/util/Map;
    //   1232: ldc_w 'character_set_connection'
    //   1235: ldc_w 'latin1'
    //   1238: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1243: pop
    //   1244: aload #5
    //   1246: astore #7
    //   1248: aload_0
    //   1249: aload #5
    //   1251: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   1254: aload #5
    //   1256: astore #7
    //   1258: goto -> 1627
    //   1261: astore #5
    //   1263: aload #7
    //   1265: astore #6
    //   1267: goto -> 573
    //   1270: astore #5
    //   1272: aload #7
    //   1274: astore #6
    //   1276: goto -> 573
    //   1279: aload #5
    //   1281: astore #7
    //   1283: aload_0
    //   1284: invokevirtual getEncoding : ()Ljava/lang/String;
    //   1287: ifnull -> 1627
    //   1290: aload #9
    //   1292: invokevirtual length : ()I
    //   1295: ifle -> 1301
    //   1298: goto -> 1322
    //   1301: aload_0
    //   1302: invokevirtual getUseOldUTF8Behavior : ()Z
    //   1305: ifeq -> 1316
    //   1308: ldc_w 'latin1'
    //   1311: astore #6
    //   1313: goto -> 1322
    //   1316: aload_0
    //   1317: invokevirtual getServerCharset : ()Ljava/lang/String;
    //   1320: astore #6
    //   1322: ldc_w 'ucs2'
    //   1325: aload #6
    //   1327: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1330: istore_3
    //   1331: iload_3
    //   1332: ifne -> 1394
    //   1335: aload #5
    //   1337: astore #7
    //   1339: ldc_w 'utf16'
    //   1342: aload #6
    //   1344: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1347: ifne -> 1394
    //   1350: aload #5
    //   1352: astore #7
    //   1354: ldc_w 'utf16le'
    //   1357: aload #6
    //   1359: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1362: ifne -> 1394
    //   1365: aload #5
    //   1367: astore #7
    //   1369: ldc_w 'utf32'
    //   1372: aload #6
    //   1374: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1377: istore_3
    //   1378: iload_3
    //   1379: ifeq -> 1385
    //   1382: goto -> 1394
    //   1385: iconst_0
    //   1386: istore_2
    //   1387: aload #6
    //   1389: astore #8
    //   1391: goto -> 1423
    //   1394: aload_0
    //   1395: invokevirtual getCharacterSetResults : ()Ljava/lang/String;
    //   1398: astore #6
    //   1400: aload #6
    //   1402: ifnonnull -> 1416
    //   1405: aload #5
    //   1407: astore #7
    //   1409: aload_0
    //   1410: ldc_w 'UTF-8'
    //   1413: invokevirtual setCharacterSetResults : (Ljava/lang/String;)V
    //   1416: ldc_w 'utf8'
    //   1419: astore #8
    //   1421: iconst_1
    //   1422: istore_2
    //   1423: iload_1
    //   1424: ifne -> 1452
    //   1427: aload #5
    //   1429: astore #7
    //   1431: aload_0
    //   1432: aload #8
    //   1434: invokespecial characterSetNamesMatches : (Ljava/lang/String;)Z
    //   1437: istore_1
    //   1438: iload_1
    //   1439: ifeq -> 1452
    //   1442: iload_2
    //   1443: ifeq -> 1449
    //   1446: goto -> 1452
    //   1449: goto -> 1607
    //   1452: new java/lang/StringBuilder
    //   1455: astore #6
    //   1457: aload #6
    //   1459: invokespecial <init> : ()V
    //   1462: aload #6
    //   1464: ldc_w 'SET NAMES '
    //   1467: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1470: pop
    //   1471: aload #6
    //   1473: aload #8
    //   1475: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1478: pop
    //   1479: aload #6
    //   1481: aload #9
    //   1483: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1486: pop
    //   1487: aload #6
    //   1489: invokevirtual toString : ()Ljava/lang/String;
    //   1492: astore #9
    //   1494: aload_0
    //   1495: getfield database : Ljava/lang/String;
    //   1498: astore #10
    //   1500: aload #5
    //   1502: astore #7
    //   1504: aload #7
    //   1506: astore #6
    //   1508: aload_0
    //   1509: aconst_null
    //   1510: aload #9
    //   1512: iconst_m1
    //   1513: aconst_null
    //   1514: sipush #1003
    //   1517: sipush #1007
    //   1520: iconst_0
    //   1521: aload #10
    //   1523: aconst_null
    //   1524: iconst_0
    //   1525: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1528: pop
    //   1529: aload #7
    //   1531: astore #6
    //   1533: aload_0
    //   1534: getfield serverVariables : Ljava/util/Map;
    //   1537: ldc_w 'character_set_client'
    //   1540: aload #8
    //   1542: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1547: pop
    //   1548: aload #7
    //   1550: astore #6
    //   1552: aload_0
    //   1553: getfield serverVariables : Ljava/util/Map;
    //   1556: ldc_w 'character_set_connection'
    //   1559: aload #8
    //   1561: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1566: pop
    //   1567: goto -> 1449
    //   1570: astore #8
    //   1572: goto -> 1577
    //   1575: astore #8
    //   1577: aload #5
    //   1579: astore #7
    //   1581: aload #7
    //   1583: astore #6
    //   1585: aload #8
    //   1587: invokevirtual getErrorCode : ()I
    //   1590: sipush #1820
    //   1593: if_icmpne -> 1620
    //   1596: aload #7
    //   1598: astore #6
    //   1600: aload_0
    //   1601: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   1604: ifne -> 1620
    //   1607: aload #5
    //   1609: astore #6
    //   1611: aload_0
    //   1612: invokevirtual getEncoding : ()Ljava/lang/String;
    //   1615: astore #5
    //   1617: goto -> 1634
    //   1620: aload #7
    //   1622: astore #6
    //   1624: aload #8
    //   1626: athrow
    //   1627: aload #7
    //   1629: astore #5
    //   1631: goto -> 1634
    //   1634: aload #5
    //   1636: astore #6
    //   1638: aload_0
    //   1639: getfield serverVariables : Ljava/util/Map;
    //   1642: astore #7
    //   1644: aload #7
    //   1646: ifnull -> 1713
    //   1649: aload #5
    //   1651: astore #6
    //   1653: aload #7
    //   1655: ldc_w 'character_set_results'
    //   1658: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1663: checkcast java/lang/String
    //   1666: astore #7
    //   1668: aload #7
    //   1670: ifnull -> 1708
    //   1673: aload #5
    //   1675: astore #6
    //   1677: ldc_w 'NULL'
    //   1680: aload #7
    //   1682: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1685: ifne -> 1708
    //   1688: aload #5
    //   1690: astore #6
    //   1692: aload #7
    //   1694: invokevirtual length : ()I
    //   1697: ifne -> 1703
    //   1700: goto -> 1708
    //   1703: iconst_0
    //   1704: istore_2
    //   1705: goto -> 1710
    //   1708: iconst_1
    //   1709: istore_2
    //   1710: goto -> 1718
    //   1713: iconst_0
    //   1714: istore_2
    //   1715: aconst_null
    //   1716: astore #7
    //   1718: aload #5
    //   1720: astore #6
    //   1722: aload_0
    //   1723: invokevirtual getCharacterSetResults : ()Ljava/lang/String;
    //   1726: astore #8
    //   1728: aload #8
    //   1730: ifnonnull -> 1857
    //   1733: iload_2
    //   1734: ifne -> 1836
    //   1737: aload #5
    //   1739: astore #6
    //   1741: aload_0
    //   1742: getfield database : Ljava/lang/String;
    //   1745: astore #7
    //   1747: aload #5
    //   1749: astore #6
    //   1751: aload_0
    //   1752: aconst_null
    //   1753: ldc_w 'SET character_set_results = NULL'
    //   1756: iconst_m1
    //   1757: aconst_null
    //   1758: sipush #1003
    //   1761: sipush #1007
    //   1764: iconst_0
    //   1765: aload #7
    //   1767: aconst_null
    //   1768: iconst_0
    //   1769: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1772: pop
    //   1773: goto -> 1809
    //   1776: astore #7
    //   1778: goto -> 1783
    //   1781: astore #7
    //   1783: aload #5
    //   1785: astore #6
    //   1787: aload #7
    //   1789: invokevirtual getErrorCode : ()I
    //   1792: sipush #1820
    //   1795: if_icmpne -> 1829
    //   1798: aload #5
    //   1800: astore #6
    //   1802: aload_0
    //   1803: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   1806: ifne -> 1829
    //   1809: aload #5
    //   1811: astore #6
    //   1813: aload_0
    //   1814: getfield serverVariables : Ljava/util/Map;
    //   1817: ldc 'jdbc.local.character_set_results'
    //   1819: aconst_null
    //   1820: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1825: pop
    //   1826: goto -> 1854
    //   1829: aload #5
    //   1831: astore #6
    //   1833: aload #7
    //   1835: athrow
    //   1836: aload #5
    //   1838: astore #6
    //   1840: aload_0
    //   1841: getfield serverVariables : Ljava/util/Map;
    //   1844: ldc 'jdbc.local.character_set_results'
    //   1846: aload #7
    //   1848: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1853: pop
    //   1854: goto -> 2306
    //   1857: aload #5
    //   1859: astore #6
    //   1861: aload_0
    //   1862: invokevirtual getUseOldUTF8Behavior : ()Z
    //   1865: istore_1
    //   1866: iload_1
    //   1867: ifeq -> 1992
    //   1870: aload #5
    //   1872: astore #6
    //   1874: aload_0
    //   1875: getfield database : Ljava/lang/String;
    //   1878: astore #8
    //   1880: aload #5
    //   1882: astore #6
    //   1884: aload_0
    //   1885: aconst_null
    //   1886: ldc_w 'SET NAMES latin1'
    //   1889: iconst_m1
    //   1890: aconst_null
    //   1891: sipush #1003
    //   1894: sipush #1007
    //   1897: iconst_0
    //   1898: aload #8
    //   1900: aconst_null
    //   1901: iconst_0
    //   1902: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1905: pop
    //   1906: aload #5
    //   1908: astore #6
    //   1910: aload_0
    //   1911: getfield serverVariables : Ljava/util/Map;
    //   1914: ldc_w 'character_set_client'
    //   1917: ldc_w 'latin1'
    //   1920: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1925: pop
    //   1926: aload #5
    //   1928: astore #6
    //   1930: aload_0
    //   1931: getfield serverVariables : Ljava/util/Map;
    //   1934: ldc_w 'character_set_connection'
    //   1937: ldc_w 'latin1'
    //   1940: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1945: pop
    //   1946: goto -> 1992
    //   1949: astore #8
    //   1951: goto -> 1956
    //   1954: astore #8
    //   1956: aload #5
    //   1958: astore #6
    //   1960: aload #8
    //   1962: invokevirtual getErrorCode : ()I
    //   1965: sipush #1820
    //   1968: if_icmpne -> 1985
    //   1971: aload #5
    //   1973: astore #6
    //   1975: aload_0
    //   1976: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   1979: ifne -> 1985
    //   1982: goto -> 1992
    //   1985: aload #5
    //   1987: astore #6
    //   1989: aload #8
    //   1991: athrow
    //   1992: aload #5
    //   1994: astore #6
    //   1996: aload_0
    //   1997: invokevirtual getCharacterSetResults : ()Ljava/lang/String;
    //   2000: astore #9
    //   2002: aload #5
    //   2004: astore #6
    //   2006: ldc_w 'UTF-8'
    //   2009: aload #9
    //   2011: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   2014: ifne -> 2087
    //   2017: aload #5
    //   2019: astore #6
    //   2021: ldc_w 'UTF8'
    //   2024: aload #9
    //   2026: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   2029: ifeq -> 2035
    //   2032: goto -> 2087
    //   2035: aload #5
    //   2037: astore #6
    //   2039: ldc_w 'null'
    //   2042: aload #9
    //   2044: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   2047: ifeq -> 2062
    //   2050: ldc_w 'NULL'
    //   2053: astore #6
    //   2055: aload #6
    //   2057: astore #8
    //   2059: goto -> 2092
    //   2062: aload #5
    //   2064: astore #6
    //   2066: aload #9
    //   2068: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   2071: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   2074: aload_0
    //   2075: invokestatic getMysqlCharsetForJavaEncoding : (Ljava/lang/String;Lcom/mysql/jdbc/Connection;)Ljava/lang/String;
    //   2078: astore #8
    //   2080: aload #8
    //   2082: astore #6
    //   2084: goto -> 2055
    //   2087: ldc_w 'utf8'
    //   2090: astore #8
    //   2092: aload #8
    //   2094: ifnull -> 2311
    //   2097: aload #5
    //   2099: astore #6
    //   2101: aload #8
    //   2103: aload_0
    //   2104: getfield serverVariables : Ljava/util/Map;
    //   2107: ldc_w 'character_set_results'
    //   2110: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   2115: checkcast java/lang/String
    //   2118: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   2121: ifne -> 2288
    //   2124: aload #5
    //   2126: astore #6
    //   2128: new java/lang/StringBuilder
    //   2131: astore #7
    //   2133: aload #5
    //   2135: astore #6
    //   2137: aload #7
    //   2139: bipush #28
    //   2141: aload #8
    //   2143: invokevirtual length : ()I
    //   2146: iadd
    //   2147: invokespecial <init> : (I)V
    //   2150: aload #5
    //   2152: astore #6
    //   2154: aload #7
    //   2156: ldc_w 'SET character_set_results = '
    //   2159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2162: pop
    //   2163: aload #5
    //   2165: astore #6
    //   2167: aload #7
    //   2169: aload #8
    //   2171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2174: pop
    //   2175: aload #5
    //   2177: astore #6
    //   2179: aload_0
    //   2180: aconst_null
    //   2181: aload #7
    //   2183: invokevirtual toString : ()Ljava/lang/String;
    //   2186: iconst_m1
    //   2187: aconst_null
    //   2188: sipush #1003
    //   2191: sipush #1007
    //   2194: iconst_0
    //   2195: aload_0
    //   2196: getfield database : Ljava/lang/String;
    //   2199: aconst_null
    //   2200: iconst_0
    //   2201: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   2204: pop
    //   2205: goto -> 2236
    //   2208: astore #7
    //   2210: aload #5
    //   2212: astore #6
    //   2214: aload #7
    //   2216: invokevirtual getErrorCode : ()I
    //   2219: sipush #1820
    //   2222: if_icmpne -> 2281
    //   2225: aload #5
    //   2227: astore #6
    //   2229: aload_0
    //   2230: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   2233: ifne -> 2281
    //   2236: aload #5
    //   2238: astore #6
    //   2240: aload_0
    //   2241: getfield serverVariables : Ljava/util/Map;
    //   2244: ldc 'jdbc.local.character_set_results'
    //   2246: aload #8
    //   2248: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2253: pop
    //   2254: aload #5
    //   2256: astore #6
    //   2258: aload_0
    //   2259: iconst_5
    //   2260: iconst_5
    //   2261: iconst_0
    //   2262: invokevirtual versionMeetsMinimum : (III)Z
    //   2265: ifeq -> 2306
    //   2268: aload #5
    //   2270: astore #6
    //   2272: aload_0
    //   2273: aload #9
    //   2275: putfield errorMessageEncoding : Ljava/lang/String;
    //   2278: goto -> 2306
    //   2281: aload #5
    //   2283: astore #6
    //   2285: aload #7
    //   2287: athrow
    //   2288: aload #5
    //   2290: astore #6
    //   2292: aload_0
    //   2293: getfield serverVariables : Ljava/util/Map;
    //   2296: ldc 'jdbc.local.character_set_results'
    //   2298: aload #7
    //   2300: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2305: pop
    //   2306: iconst_1
    //   2307: istore_1
    //   2308: goto -> 2498
    //   2311: aload #5
    //   2313: astore #6
    //   2315: new java/lang/StringBuilder
    //   2318: astore #7
    //   2320: aload #5
    //   2322: astore #6
    //   2324: aload #7
    //   2326: invokespecial <init> : ()V
    //   2329: aload #5
    //   2331: astore #6
    //   2333: aload #7
    //   2335: ldc_w 'Can't map '
    //   2338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2341: pop
    //   2342: aload #5
    //   2344: astore #6
    //   2346: aload #7
    //   2348: aload #9
    //   2350: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2353: pop
    //   2354: aload #5
    //   2356: astore #6
    //   2358: aload #7
    //   2360: ldc_w ' given for characterSetResults to a supported MySQL encoding.'
    //   2363: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2366: pop
    //   2367: aload #5
    //   2369: astore #6
    //   2371: aload #7
    //   2373: invokevirtual toString : ()Ljava/lang/String;
    //   2376: ldc_w 'S1009'
    //   2379: aload_0
    //   2380: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2383: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2386: athrow
    //   2387: astore #7
    //   2389: aload #5
    //   2391: astore #6
    //   2393: aload #7
    //   2395: astore #5
    //   2397: goto -> 2483
    //   2400: aload #5
    //   2402: astore #6
    //   2404: new java/lang/StringBuilder
    //   2407: astore #7
    //   2409: aload #5
    //   2411: astore #6
    //   2413: aload #7
    //   2415: invokespecial <init> : ()V
    //   2418: aload #5
    //   2420: astore #6
    //   2422: aload #7
    //   2424: ldc_w 'Unknown initial character set index ''
    //   2427: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2430: pop
    //   2431: aload #5
    //   2433: astore #6
    //   2435: aload #7
    //   2437: aload_0
    //   2438: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   2441: getfield serverCharsetIndex : I
    //   2444: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2447: pop
    //   2448: aload #5
    //   2450: astore #6
    //   2452: aload #7
    //   2454: ldc_w '' received from server. Initial client character set can be forced via the 'characterEncoding' property.'
    //   2457: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2460: pop
    //   2461: aload #5
    //   2463: astore #6
    //   2465: aload #7
    //   2467: invokevirtual toString : ()Ljava/lang/String;
    //   2470: ldc_w 'S1000'
    //   2473: aload_0
    //   2474: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2477: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2480: athrow
    //   2481: astore #5
    //   2483: goto -> 2743
    //   2486: aload #5
    //   2488: astore #6
    //   2490: aload_0
    //   2491: invokevirtual getEncoding : ()Ljava/lang/String;
    //   2494: astore #5
    //   2496: iconst_0
    //   2497: istore_1
    //   2498: aload_0
    //   2499: aload #5
    //   2501: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   2504: aload_0
    //   2505: invokevirtual getEncoding : ()Ljava/lang/String;
    //   2508: invokestatic forName : (Ljava/lang/String;)Ljava/nio/charset/Charset;
    //   2511: invokevirtual newEncoder : ()Ljava/nio/charset/CharsetEncoder;
    //   2514: astore #6
    //   2516: iconst_1
    //   2517: invokestatic allocate : (I)Ljava/nio/CharBuffer;
    //   2520: astore #7
    //   2522: iconst_1
    //   2523: invokestatic allocate : (I)Ljava/nio/ByteBuffer;
    //   2526: astore #5
    //   2528: aload #7
    //   2530: ldc_w '¥'
    //   2533: invokevirtual put : (Ljava/lang/String;)Ljava/nio/CharBuffer;
    //   2536: pop
    //   2537: aload #7
    //   2539: iconst_0
    //   2540: invokevirtual position : (I)Ljava/nio/Buffer;
    //   2543: pop
    //   2544: aload #6
    //   2546: aload #7
    //   2548: aload #5
    //   2550: iconst_1
    //   2551: invokevirtual encode : (Ljava/nio/CharBuffer;Ljava/nio/ByteBuffer;Z)Ljava/nio/charset/CoderResult;
    //   2554: pop
    //   2555: aload #5
    //   2557: iconst_0
    //   2558: invokevirtual get : (I)B
    //   2561: bipush #92
    //   2563: if_icmpne -> 2574
    //   2566: aload_0
    //   2567: iconst_1
    //   2568: putfield requiresEscapingEncoder : Z
    //   2571: goto -> 2691
    //   2574: aload #7
    //   2576: invokevirtual clear : ()Ljava/nio/Buffer;
    //   2579: pop
    //   2580: aload #5
    //   2582: invokevirtual clear : ()Ljava/nio/Buffer;
    //   2585: pop
    //   2586: aload #7
    //   2588: ldc_w '₩'
    //   2591: invokevirtual put : (Ljava/lang/String;)Ljava/nio/CharBuffer;
    //   2594: pop
    //   2595: aload #7
    //   2597: iconst_0
    //   2598: invokevirtual position : (I)Ljava/nio/Buffer;
    //   2601: pop
    //   2602: aload #6
    //   2604: aload #7
    //   2606: aload #5
    //   2608: iconst_1
    //   2609: invokevirtual encode : (Ljava/nio/CharBuffer;Ljava/nio/ByteBuffer;Z)Ljava/nio/charset/CoderResult;
    //   2612: pop
    //   2613: aload #5
    //   2615: iconst_0
    //   2616: invokevirtual get : (I)B
    //   2619: bipush #92
    //   2621: if_icmpne -> 2691
    //   2624: aload_0
    //   2625: iconst_1
    //   2626: putfield requiresEscapingEncoder : Z
    //   2629: goto -> 2691
    //   2632: astore #5
    //   2634: goto -> 2644
    //   2637: astore #5
    //   2639: goto -> 2644
    //   2642: astore #5
    //   2644: ldc_w '¥'
    //   2647: aload_0
    //   2648: invokevirtual getEncoding : ()Ljava/lang/String;
    //   2651: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   2654: iconst_0
    //   2655: baload
    //   2656: bipush #92
    //   2658: if_icmpne -> 2669
    //   2661: aload_0
    //   2662: iconst_1
    //   2663: putfield requiresEscapingEncoder : Z
    //   2666: goto -> 2691
    //   2669: ldc_w '₩'
    //   2672: aload_0
    //   2673: invokevirtual getEncoding : ()Ljava/lang/String;
    //   2676: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;)[B
    //   2679: iconst_0
    //   2680: baload
    //   2681: bipush #92
    //   2683: if_icmpne -> 2691
    //   2686: aload_0
    //   2687: iconst_1
    //   2688: putfield requiresEscapingEncoder : Z
    //   2691: iload_1
    //   2692: ireturn
    //   2693: astore #6
    //   2695: new java/lang/StringBuilder
    //   2698: dup
    //   2699: invokespecial <init> : ()V
    //   2702: astore #5
    //   2704: aload #5
    //   2706: ldc_w 'Unable to use encoding: '
    //   2709: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2712: pop
    //   2713: aload #5
    //   2715: aload_0
    //   2716: invokevirtual getEncoding : ()Ljava/lang/String;
    //   2719: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2722: pop
    //   2723: aload #5
    //   2725: invokevirtual toString : ()Ljava/lang/String;
    //   2728: ldc_w 'S1000'
    //   2731: aload #6
    //   2733: aload_0
    //   2734: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2737: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2740: athrow
    //   2741: astore #5
    //   2743: aload_0
    //   2744: aload #6
    //   2746: invokevirtual setEncoding : (Ljava/lang/String;)V
    //   2749: aload #5
    //   2751: athrow
    //   2752: astore #5
    //   2754: goto -> 2644
    // Exception table:
    //   from	to	target	type
    //   10	18	2741	finally
    //   26	31	2741	finally
    //   35	39	2741	finally
    //   43	49	2741	finally
    //   53	58	2741	finally
    //   71	81	2741	finally
    //   92	97	2741	finally
    //   101	108	2741	finally
    //   116	130	2741	finally
    //   134	139	2741	finally
    //   143	148	2741	finally
    //   152	161	2741	finally
    //   165	175	2741	finally
    //   179	186	2741	finally
    //   190	200	2741	finally
    //   204	213	2741	finally
    //   247	253	624	java/lang/ArrayIndexOutOfBoundsException
    //   247	253	615	java/sql/SQLException
    //   247	253	576	java/lang/RuntimeException
    //   247	253	567	finally
    //   262	273	624	java/lang/ArrayIndexOutOfBoundsException
    //   262	273	615	java/sql/SQLException
    //   262	273	576	java/lang/RuntimeException
    //   262	273	567	finally
    //   277	297	624	java/lang/ArrayIndexOutOfBoundsException
    //   277	297	615	java/sql/SQLException
    //   277	297	576	java/lang/RuntimeException
    //   277	297	567	finally
    //   301	316	624	java/lang/ArrayIndexOutOfBoundsException
    //   301	316	615	java/sql/SQLException
    //   301	316	576	java/lang/RuntimeException
    //   301	316	567	finally
    //   325	333	624	java/lang/ArrayIndexOutOfBoundsException
    //   325	333	615	java/sql/SQLException
    //   325	333	576	java/lang/RuntimeException
    //   325	333	567	finally
    //   348	354	624	java/lang/ArrayIndexOutOfBoundsException
    //   348	354	615	java/sql/SQLException
    //   348	354	576	java/lang/RuntimeException
    //   348	354	567	finally
    //   365	375	562	java/lang/ArrayIndexOutOfBoundsException
    //   365	375	615	java/sql/SQLException
    //   365	375	576	java/lang/RuntimeException
    //   365	375	567	finally
    //   383	394	562	java/lang/ArrayIndexOutOfBoundsException
    //   383	394	615	java/sql/SQLException
    //   383	394	576	java/lang/RuntimeException
    //   383	394	567	finally
    //   403	414	562	java/lang/ArrayIndexOutOfBoundsException
    //   403	414	615	java/sql/SQLException
    //   403	414	576	java/lang/RuntimeException
    //   403	414	567	finally
    //   418	429	562	java/lang/ArrayIndexOutOfBoundsException
    //   418	429	615	java/sql/SQLException
    //   418	429	576	java/lang/RuntimeException
    //   418	429	567	finally
    //   433	444	562	java/lang/ArrayIndexOutOfBoundsException
    //   433	444	615	java/sql/SQLException
    //   433	444	576	java/lang/RuntimeException
    //   433	444	567	finally
    //   452	463	562	java/lang/ArrayIndexOutOfBoundsException
    //   452	463	615	java/sql/SQLException
    //   452	463	576	java/lang/RuntimeException
    //   452	463	567	finally
    //   472	478	562	java/lang/ArrayIndexOutOfBoundsException
    //   472	478	615	java/sql/SQLException
    //   472	478	576	java/lang/RuntimeException
    //   472	478	567	finally
    //   485	490	562	java/lang/ArrayIndexOutOfBoundsException
    //   485	490	615	java/sql/SQLException
    //   485	490	576	java/lang/RuntimeException
    //   485	490	567	finally
    //   494	499	562	java/lang/ArrayIndexOutOfBoundsException
    //   494	499	615	java/sql/SQLException
    //   494	499	576	java/lang/RuntimeException
    //   494	499	567	finally
    //   503	512	562	java/lang/ArrayIndexOutOfBoundsException
    //   503	512	615	java/sql/SQLException
    //   503	512	576	java/lang/RuntimeException
    //   503	512	567	finally
    //   516	529	562	java/lang/ArrayIndexOutOfBoundsException
    //   516	529	615	java/sql/SQLException
    //   516	529	576	java/lang/RuntimeException
    //   516	529	567	finally
    //   533	542	562	java/lang/ArrayIndexOutOfBoundsException
    //   533	542	615	java/sql/SQLException
    //   533	542	576	java/lang/RuntimeException
    //   533	542	567	finally
    //   546	562	562	java/lang/ArrayIndexOutOfBoundsException
    //   546	562	615	java/sql/SQLException
    //   546	562	576	java/lang/RuntimeException
    //   546	562	567	finally
    //   582	596	567	finally
    //   600	608	567	finally
    //   612	615	567	finally
    //   621	624	567	finally
    //   631	637	2387	finally
    //   637	643	2387	finally
    //   652	659	567	finally
    //   659	664	2387	finally
    //   677	686	567	finally
    //   690	701	908	finally
    //   704	712	908	finally
    //   715	729	908	finally
    //   742	749	567	finally
    //   763	811	870	finally
    //   811	862	865	finally
    //   895	901	966	finally
    //   933	947	1270	finally
    //   980	986	1270	finally
    //   995	1005	966	finally
    //   1009	1019	966	finally
    //   1019	1050	966	finally
    //   1060	1108	1167	finally
    //   1108	1159	1162	finally
    //   1184	1244	1261	finally
    //   1248	1254	567	finally
    //   1283	1298	2387	finally
    //   1301	1308	2387	finally
    //   1316	1322	2387	finally
    //   1322	1331	2387	finally
    //   1339	1350	567	finally
    //   1354	1365	567	finally
    //   1369	1378	567	finally
    //   1394	1400	2387	finally
    //   1409	1416	567	finally
    //   1431	1438	567	finally
    //   1452	1500	1575	java/sql/SQLException
    //   1452	1500	2387	finally
    //   1508	1529	1570	java/sql/SQLException
    //   1508	1529	2481	finally
    //   1533	1548	1570	java/sql/SQLException
    //   1533	1548	2481	finally
    //   1552	1567	1570	java/sql/SQLException
    //   1552	1567	2481	finally
    //   1585	1596	2481	finally
    //   1600	1607	2481	finally
    //   1611	1617	2481	finally
    //   1624	1627	2481	finally
    //   1638	1644	2481	finally
    //   1653	1668	2481	finally
    //   1677	1688	2481	finally
    //   1692	1700	2481	finally
    //   1722	1728	2481	finally
    //   1741	1747	1781	java/sql/SQLException
    //   1741	1747	2481	finally
    //   1751	1773	1776	java/sql/SQLException
    //   1751	1773	2481	finally
    //   1787	1798	2481	finally
    //   1802	1809	2481	finally
    //   1813	1826	2481	finally
    //   1833	1836	2481	finally
    //   1840	1854	2481	finally
    //   1861	1866	2481	finally
    //   1874	1880	1954	java/sql/SQLException
    //   1874	1880	2481	finally
    //   1884	1906	1949	java/sql/SQLException
    //   1884	1906	2481	finally
    //   1910	1926	1949	java/sql/SQLException
    //   1910	1926	2481	finally
    //   1930	1946	1949	java/sql/SQLException
    //   1930	1946	2481	finally
    //   1960	1971	2481	finally
    //   1975	1982	2481	finally
    //   1989	1992	2481	finally
    //   1996	2002	2481	finally
    //   2006	2017	2481	finally
    //   2021	2032	2481	finally
    //   2039	2050	2481	finally
    //   2066	2080	2481	finally
    //   2101	2124	2481	finally
    //   2128	2133	2481	finally
    //   2137	2150	2481	finally
    //   2154	2163	2481	finally
    //   2167	2175	2481	finally
    //   2179	2205	2208	java/sql/SQLException
    //   2179	2205	2481	finally
    //   2214	2225	2481	finally
    //   2229	2236	2481	finally
    //   2240	2254	2481	finally
    //   2258	2268	2481	finally
    //   2272	2278	2481	finally
    //   2285	2288	2481	finally
    //   2292	2306	2481	finally
    //   2315	2320	2481	finally
    //   2324	2329	2481	finally
    //   2333	2342	2481	finally
    //   2346	2354	2481	finally
    //   2358	2367	2481	finally
    //   2371	2387	2481	finally
    //   2404	2409	2481	finally
    //   2413	2418	2481	finally
    //   2422	2431	2481	finally
    //   2435	2448	2481	finally
    //   2452	2461	2481	finally
    //   2465	2481	2481	finally
    //   2490	2496	2741	finally
    //   2504	2528	2642	java/nio/charset/UnsupportedCharsetException
    //   2528	2544	2637	java/nio/charset/UnsupportedCharsetException
    //   2544	2571	2632	java/nio/charset/UnsupportedCharsetException
    //   2574	2586	2632	java/nio/charset/UnsupportedCharsetException
    //   2586	2629	2752	java/nio/charset/UnsupportedCharsetException
    //   2644	2666	2693	java/io/UnsupportedEncodingException
    //   2669	2691	2693	java/io/UnsupportedEncodingException
  }
  
  private void configureTimezone() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield serverVariables : Ljava/util/Map;
    //   4: ldc_w 'timezone'
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast java/lang/String
    //   15: astore_3
    //   16: aload_3
    //   17: astore_2
    //   18: aload_3
    //   19: ifnonnull -> 66
    //   22: aload_0
    //   23: getfield serverVariables : Ljava/util/Map;
    //   26: ldc_w 'time_zone'
    //   29: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   34: checkcast java/lang/String
    //   37: astore_3
    //   38: aload_3
    //   39: astore_2
    //   40: ldc_w 'SYSTEM'
    //   43: aload_3
    //   44: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   47: ifeq -> 66
    //   50: aload_0
    //   51: getfield serverVariables : Ljava/util/Map;
    //   54: ldc_w 'system_time_zone'
    //   57: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   62: checkcast java/lang/String
    //   65: astore_2
    //   66: aload_0
    //   67: invokevirtual getServerTimezone : ()Ljava/lang/String;
    //   70: astore #4
    //   72: aload_0
    //   73: invokevirtual getUseTimezone : ()Z
    //   76: ifne -> 89
    //   79: aload #4
    //   81: astore_3
    //   82: aload_0
    //   83: invokevirtual getUseLegacyDatetimeCode : ()Z
    //   86: ifne -> 140
    //   89: aload #4
    //   91: astore_3
    //   92: aload_2
    //   93: ifnull -> 140
    //   96: aload #4
    //   98: ifnull -> 112
    //   101: aload #4
    //   103: astore_3
    //   104: aload #4
    //   106: invokestatic isEmptyOrWhitespaceOnly : (Ljava/lang/String;)Z
    //   109: ifeq -> 140
    //   112: aload_2
    //   113: aload_0
    //   114: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   117: invokestatic getCanonicalTimezone : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/lang/String;
    //   120: astore_3
    //   121: goto -> 140
    //   124: astore_2
    //   125: aload_2
    //   126: invokevirtual getMessage : ()Ljava/lang/String;
    //   129: ldc_w 'S1000'
    //   132: aload_0
    //   133: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   136: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   139: athrow
    //   140: aload_3
    //   141: ifnull -> 265
    //   144: aload_3
    //   145: invokevirtual length : ()I
    //   148: ifle -> 265
    //   151: aload_0
    //   152: aload_3
    //   153: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   156: putfield serverTimezoneTZ : Ljava/util/TimeZone;
    //   159: aload_3
    //   160: ldc_w 'GMT'
    //   163: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   166: ifne -> 233
    //   169: aload_0
    //   170: getfield serverTimezoneTZ : Ljava/util/TimeZone;
    //   173: invokevirtual getID : ()Ljava/lang/String;
    //   176: ldc_w 'GMT'
    //   179: invokevirtual equals : (Ljava/lang/Object;)Z
    //   182: ifne -> 188
    //   185: goto -> 233
    //   188: new java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial <init> : ()V
    //   195: astore_2
    //   196: aload_2
    //   197: ldc_w 'No timezone mapping entry for ''
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_2
    //   205: aload_3
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_2
    //   211: ldc_w '''
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload_2
    //   219: invokevirtual toString : ()Ljava/lang/String;
    //   222: ldc_w 'S1009'
    //   225: aload_0
    //   226: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   229: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   232: athrow
    //   233: aload_0
    //   234: getfield serverTimezoneTZ : Ljava/util/TimeZone;
    //   237: invokevirtual useDaylightTime : ()Z
    //   240: ifne -> 258
    //   243: aload_0
    //   244: getfield serverTimezoneTZ : Ljava/util/TimeZone;
    //   247: invokevirtual getRawOffset : ()I
    //   250: ifne -> 258
    //   253: iconst_1
    //   254: istore_1
    //   255: goto -> 260
    //   258: iconst_0
    //   259: istore_1
    //   260: aload_0
    //   261: iload_1
    //   262: putfield isServerTzUTC : Z
    //   265: return
    // Exception table:
    //   from	to	target	type
    //   112	121	124	java/lang/IllegalArgumentException
  }
  
  private void connectOneTryOnly(boolean paramBoolean, Properties paramProperties) throws SQLException {
    try {
      coreConnect(paramProperties);
      this.connectionId = this.io.getThreadId();
      this.isClosed = false;
      boolean bool2 = getAutoCommit();
      int i = this.isolationLevel;
      boolean bool1 = isReadOnly(false);
      String str = getCatalog();
      this.io.setStatementInterceptors(this.statementInterceptors);
      initializePropsFromServer();
      if (paramBoolean) {
        setAutoCommit(bool2);
        if (this.hasIsolationLevels)
          setTransactionIsolation(i); 
        setCatalog(str);
        setReadOnly(bool1);
      } 
      return;
    } catch (Exception exception) {
      paramBoolean = exception instanceof SQLException;
      if (paramBoolean && ((SQLException)exception).getErrorCode() == 1820 && !getDisconnectOnExpiredPasswords())
        return; 
      MysqlIO mysqlIO = this.io;
      if (mysqlIO != null)
        mysqlIO.forceClose(); 
      if (paramBoolean)
        throw (SQLException)exception; 
      SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.UnableToConnect"), "08001", getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void connectWithRetries(boolean paramBoolean, Properties paramProperties) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getInitialTimeout : ()I
    //   4: i2d
    //   5: dstore_3
    //   6: aconst_null
    //   7: astore #12
    //   9: aconst_null
    //   10: astore #11
    //   12: iconst_0
    //   13: istore #5
    //   15: iload #5
    //   17: aload_0
    //   18: invokevirtual getMaxReconnects : ()I
    //   21: if_icmpge -> 199
    //   24: aload_0
    //   25: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   28: astore #13
    //   30: aload #13
    //   32: ifnull -> 40
    //   35: aload #13
    //   37: invokevirtual forceClose : ()V
    //   40: aload_0
    //   41: aload_2
    //   42: invokespecial coreConnect : (Ljava/util/Properties;)V
    //   45: aload_0
    //   46: iconst_0
    //   47: iconst_0
    //   48: invokevirtual pingInternal : (ZI)V
    //   51: aload_0
    //   52: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   55: astore #13
    //   57: aload #13
    //   59: monitorenter
    //   60: aload_0
    //   61: aload_0
    //   62: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   65: invokevirtual getThreadId : ()J
    //   68: putfield connectionId : J
    //   71: aload_0
    //   72: iconst_0
    //   73: putfield isClosed : Z
    //   76: aload_0
    //   77: invokevirtual getAutoCommit : ()Z
    //   80: istore #9
    //   82: aload_0
    //   83: getfield isolationLevel : I
    //   86: istore #6
    //   88: aload_0
    //   89: iconst_0
    //   90: invokevirtual isReadOnly : (Z)Z
    //   93: istore #10
    //   95: aload_0
    //   96: invokevirtual getCatalog : ()Ljava/lang/String;
    //   99: astore #14
    //   101: aload_0
    //   102: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   105: aload_0
    //   106: getfield statementInterceptors : Ljava/util/List;
    //   109: invokevirtual setStatementInterceptors : (Ljava/util/List;)V
    //   112: aload #13
    //   114: monitorexit
    //   115: aload_0
    //   116: invokespecial initializePropsFromServer : ()V
    //   119: iload_1
    //   120: ifeq -> 154
    //   123: aload_0
    //   124: iload #9
    //   126: invokevirtual setAutoCommit : (Z)V
    //   129: aload_0
    //   130: getfield hasIsolationLevels : Z
    //   133: ifeq -> 142
    //   136: aload_0
    //   137: iload #6
    //   139: invokevirtual setTransactionIsolation : (I)V
    //   142: aload_0
    //   143: aload #14
    //   145: invokevirtual setCatalog : (Ljava/lang/String;)V
    //   148: aload_0
    //   149: iload #10
    //   151: invokevirtual setReadOnly : (Z)V
    //   154: iconst_1
    //   155: istore #5
    //   157: goto -> 202
    //   160: astore #11
    //   162: aload #13
    //   164: monitorexit
    //   165: aload #11
    //   167: athrow
    //   168: astore #11
    //   170: goto -> 175
    //   173: astore #11
    //   175: iload #5
    //   177: ifle -> 193
    //   180: dload_3
    //   181: d2l
    //   182: lstore #7
    //   184: lload #7
    //   186: ldc2_w 1000
    //   189: lmul
    //   190: invokestatic sleep : (J)V
    //   193: iinc #5, 1
    //   196: goto -> 15
    //   199: iconst_0
    //   200: istore #5
    //   202: iload #5
    //   204: ifeq -> 335
    //   207: aload_0
    //   208: invokevirtual getParanoid : ()Z
    //   211: ifeq -> 231
    //   214: aload_0
    //   215: invokevirtual getHighAvailability : ()Z
    //   218: ifne -> 231
    //   221: aload_0
    //   222: aconst_null
    //   223: putfield password : Ljava/lang/String;
    //   226: aload_0
    //   227: aconst_null
    //   228: putfield user : Ljava/lang/String;
    //   231: iload_1
    //   232: ifeq -> 334
    //   235: aload_0
    //   236: getfield openStatements : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   239: invokevirtual iterator : ()Ljava/util/Iterator;
    //   242: astore #13
    //   244: aload #12
    //   246: astore #11
    //   248: aload #13
    //   250: invokeinterface hasNext : ()Z
    //   255: ifeq -> 307
    //   258: aload #13
    //   260: invokeinterface next : ()Ljava/lang/Object;
    //   265: checkcast com/mysql/jdbc/Statement
    //   268: astore #12
    //   270: aload #12
    //   272: instanceof com/mysql/jdbc/ServerPreparedStatement
    //   275: ifeq -> 248
    //   278: aload #11
    //   280: astore_2
    //   281: aload #11
    //   283: ifnonnull -> 294
    //   286: new java/util/Stack
    //   289: dup
    //   290: invokespecial <init> : ()V
    //   293: astore_2
    //   294: aload_2
    //   295: aload #12
    //   297: invokevirtual add : (Ljava/lang/Object;)Z
    //   300: pop
    //   301: aload_2
    //   302: astore #11
    //   304: goto -> 248
    //   307: aload #11
    //   309: ifnull -> 334
    //   312: aload #11
    //   314: invokevirtual isEmpty : ()Z
    //   317: ifne -> 334
    //   320: aload #11
    //   322: invokevirtual pop : ()Ljava/lang/Object;
    //   325: checkcast com/mysql/jdbc/ServerPreparedStatement
    //   328: invokevirtual rePrepare : ()V
    //   331: goto -> 312
    //   334: return
    //   335: ldc_w 'Connection.UnableToConnectWithRetries'
    //   338: iconst_1
    //   339: anewarray java/lang/Object
    //   342: dup
    //   343: iconst_0
    //   344: aload_0
    //   345: invokevirtual getMaxReconnects : ()I
    //   348: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   351: aastore
    //   352: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   355: ldc_w '08001'
    //   358: aload_0
    //   359: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   362: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   365: astore_2
    //   366: aload_2
    //   367: aload #11
    //   369: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   372: pop
    //   373: aload_2
    //   374: athrow
    //   375: astore #13
    //   377: goto -> 193
    // Exception table:
    //   from	to	target	type
    //   24	30	173	java/lang/Exception
    //   35	40	173	java/lang/Exception
    //   40	60	168	java/lang/Exception
    //   60	115	160	finally
    //   115	119	168	java/lang/Exception
    //   123	142	168	java/lang/Exception
    //   142	154	168	java/lang/Exception
    //   162	165	160	finally
    //   165	168	168	java/lang/Exception
    //   184	193	375	java/lang/InterruptedException
  }
  
  private void coreConnect(Properties paramProperties) throws SQLException, IOException {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 'PROTOCOL'
    //   4: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   7: astore_3
    //   8: aload_3
    //   9: ifnull -> 130
    //   12: ldc_w 'tcp'
    //   15: aload_3
    //   16: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   19: ifeq -> 52
    //   22: aload_0
    //   23: aload_1
    //   24: ldc_w 'HOST'
    //   27: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   30: invokespecial normalizeHost : (Ljava/lang/String;)Ljava/lang/String;
    //   33: astore_3
    //   34: aload_0
    //   35: aload_1
    //   36: ldc_w 'PORT'
    //   39: ldc_w '3306'
    //   42: invokevirtual getProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   45: invokespecial parsePortNumber : (Ljava/lang/String;)I
    //   48: istore_2
    //   49: goto -> 171
    //   52: ldc_w 'pipe'
    //   55: aload_3
    //   56: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   59: ifeq -> 100
    //   62: aload_0
    //   63: ldc_w com/mysql/jdbc/NamedPipeSocketFactory
    //   66: invokevirtual getName : ()Ljava/lang/String;
    //   69: invokevirtual setSocketFactoryClassName : (Ljava/lang/String;)V
    //   72: aload_1
    //   73: ldc_w 'PATH'
    //   76: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   79: astore_3
    //   80: aload_3
    //   81: ifnull -> 93
    //   84: aload_1
    //   85: ldc_w 'namedPipePath'
    //   88: aload_3
    //   89: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   92: pop
    //   93: ldc_w 'localhost'
    //   96: astore_3
    //   97: goto -> 167
    //   100: aload_0
    //   101: aload_1
    //   102: ldc_w 'HOST'
    //   105: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   108: invokespecial normalizeHost : (Ljava/lang/String;)Ljava/lang/String;
    //   111: astore_3
    //   112: aload_0
    //   113: aload_1
    //   114: ldc_w 'PORT'
    //   117: ldc_w '3306'
    //   120: invokevirtual getProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   123: invokespecial parsePortNumber : (Ljava/lang/String;)I
    //   126: istore_2
    //   127: goto -> 49
    //   130: aload_0
    //   131: getfield hostPortPair : Ljava/lang/String;
    //   134: invokestatic parseHostPortPair : (Ljava/lang/String;)[Ljava/lang/String;
    //   137: astore #4
    //   139: aload_0
    //   140: aload #4
    //   142: iconst_0
    //   143: aaload
    //   144: invokespecial normalizeHost : (Ljava/lang/String;)Ljava/lang/String;
    //   147: astore_3
    //   148: aload #4
    //   150: iconst_1
    //   151: aaload
    //   152: ifnull -> 167
    //   155: aload_0
    //   156: aload #4
    //   158: iconst_1
    //   159: aaload
    //   160: invokespecial parsePortNumber : (Ljava/lang/String;)I
    //   163: istore_2
    //   164: goto -> 171
    //   167: sipush #3306
    //   170: istore_2
    //   171: aload_0
    //   172: iload_2
    //   173: putfield port : I
    //   176: aload_0
    //   177: aload_3
    //   178: putfield host : Ljava/lang/String;
    //   181: aload_0
    //   182: iconst_m1
    //   183: putfield sessionMaxRows : I
    //   186: new java/util/HashMap
    //   189: dup
    //   190: invokespecial <init> : ()V
    //   193: astore #4
    //   195: aload_0
    //   196: aload #4
    //   198: putfield serverVariables : Ljava/util/Map;
    //   201: aload #4
    //   203: ldc_w 'character_set_server'
    //   206: ldc_w 'utf8'
    //   209: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   214: pop
    //   215: new com/mysql/jdbc/MysqlIO
    //   218: dup
    //   219: aload_3
    //   220: iload_2
    //   221: aload_1
    //   222: aload_0
    //   223: invokevirtual getSocketFactoryClassName : ()Ljava/lang/String;
    //   226: aload_0
    //   227: invokespecial getProxy : ()Lcom/mysql/jdbc/MySQLConnection;
    //   230: aload_0
    //   231: invokevirtual getSocketTimeout : ()I
    //   234: aload_0
    //   235: getfield largeRowSizeThreshold : Lcom/mysql/jdbc/ConnectionPropertiesImpl$MemorySizeConnectionProperty;
    //   238: invokevirtual getValueAsInt : ()I
    //   241: invokespecial <init> : (Ljava/lang/String;ILjava/util/Properties;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;II)V
    //   244: astore_1
    //   245: aload_0
    //   246: aload_1
    //   247: putfield io : Lcom/mysql/jdbc/MysqlIO;
    //   250: aload_1
    //   251: aload_0
    //   252: getfield user : Ljava/lang/String;
    //   255: aload_0
    //   256: getfield password : Ljava/lang/String;
    //   259: aload_0
    //   260: getfield database : Ljava/lang/String;
    //   263: invokevirtual doHandshake : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   266: aload_0
    //   267: iconst_5
    //   268: iconst_5
    //   269: iconst_0
    //   270: invokevirtual versionMeetsMinimum : (III)Z
    //   273: ifeq -> 287
    //   276: aload_0
    //   277: aload_0
    //   278: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   281: invokevirtual getEncodingForHandshake : ()Ljava/lang/String;
    //   284: putfield errorMessageEncoding : Ljava/lang/String;
    //   287: return
  }
  
  private void createConfigCacheIfNeeded() throws SQLException {
    synchronized (getConnectionMutex()) {
      if (this.serverConfigCache != null)
        return; 
      try {
        this.serverConfigCache = ((CacheAdapterFactory<String, Map<String, String>>)Class.forName(getServerConfigCacheFactory()).newInstance()).getInstance(this, this.myURL, 2147483647, 2147483647, this.props);
        ExceptionInterceptor exceptionInterceptor2 = new ExceptionInterceptor() {
            public final ConnectionImpl this$0;
            
            public void destroy() {}
            
            public void init(Connection param1Connection, Properties param1Properties) throws SQLException {}
            
            public SQLException interceptException(SQLException param1SQLException, Connection param1Connection) {
              if (param1SQLException.getSQLState() != null && param1SQLException.getSQLState().startsWith("08"))
                ConnectionImpl.this.serverConfigCache.invalidate(ConnectionImpl.this.getURL()); 
              return null;
            }
          };
        super(this);
        ExceptionInterceptor exceptionInterceptor1 = this.exceptionInterceptor;
        if (exceptionInterceptor1 == null) {
          this.exceptionInterceptor = exceptionInterceptor2;
        } else {
          ((ExceptionInterceptorChain)exceptionInterceptor1).addRingZero(exceptionInterceptor2);
        } 
        return;
      } catch (ClassNotFoundException classNotFoundException) {
        SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
        sQLException.initCause(classNotFoundException);
        throw sQLException;
      } catch (InstantiationException instantiationException) {
        SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
        sQLException.initCause(instantiationException);
        throw sQLException;
      } catch (IllegalAccessException illegalAccessException) {
        SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
        sQLException.initCause(illegalAccessException);
        throw sQLException;
      } 
    } 
  }
  
  private void createInitialHistogram(long[] paramArrayOflong, long paramLong1, long paramLong2) {
    double d2 = (paramLong2 - paramLong1) / 20.0D * 1.25D;
    double d1 = d2;
    if (d2 < 1.0D)
      d1 = 1.0D; 
    for (byte b = 0; b < 20; b++) {
      paramArrayOflong[b] = paramLong1;
      paramLong1 = (long)(paramLong1 + d1);
    } 
  }
  
  private void createPreparedStatementCaches() throws SQLException {
    synchronized (getConnectionMutex()) {
      int i = getPreparedStatementCacheSize();
      try {
        this.cachedPreparedStatementParams = ((CacheAdapterFactory<String, PreparedStatement.ParseInfo>)Class.forName(getParseInfoCacheFactory()).newInstance()).getInstance(this, this.myURL, getPreparedStatementCacheSize(), getPreparedStatementCacheSqlLimit(), this.props);
        if (getUseServerPreparedStmts()) {
          LRUCache<Object, Object> lRUCache1 = new LRUCache<Object, Object>();
          this(i);
          this.serverSideStatementCheckCache = (LRUCache)lRUCache1;
          LRUCache<CompoundCacheKey, ServerPreparedStatement> lRUCache = new LRUCache<CompoundCacheKey, ServerPreparedStatement>() {
              private static final long serialVersionUID = 7692318650375988114L;
              
              public final ConnectionImpl this$0;
              
              public boolean removeEldestEntry(Map.Entry<ConnectionImpl.CompoundCacheKey, ServerPreparedStatement> param1Entry) {
                if (this.maxElements <= 1)
                  return false; 
                boolean bool = super.removeEldestEntry(param1Entry);
                if (bool) {
                  ServerPreparedStatement serverPreparedStatement = param1Entry.getValue();
                  serverPreparedStatement.isCached = false;
                  serverPreparedStatement.setClosed(false);
                  try {
                    serverPreparedStatement.close();
                  } catch (SQLException sQLException) {}
                } 
                return bool;
              }
            };
          super(this, i);
          this.serverSideStatementCache = lRUCache;
        } 
        return;
      } catch (ClassNotFoundException classNotFoundException) {
        SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
        sQLException.initCause(classNotFoundException);
        throw sQLException;
      } catch (InstantiationException instantiationException) {
        SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
        sQLException.initCause(instantiationException);
        throw sQLException;
      } catch (IllegalAccessException illegalAccessException) {
        SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
        sQLException.initCause(illegalAccessException);
        throw sQLException;
      } 
    } 
  }
  
  public static Connection getInstance(String paramString1, int paramInt, Properties paramProperties, String paramString2, String paramString3) throws SQLException {
    return !Util.isJdbc4() ? new ConnectionImpl(paramString1, paramInt, paramProperties, paramString2, paramString3) : (Connection)Util.handleNewInstance(JDBC_4_CONNECTION_CTOR, new Object[] { paramString1, Integer.valueOf(paramInt), paramProperties, paramString2, paramString3 }, null);
  }
  
  private DatabaseMetaData getMetaData(boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    if (paramBoolean1)
      checkClosed(); 
    return DatabaseMetaData.getInstance(getMultiHostSafeProxy(), this.database, paramBoolean2);
  }
  
  public static int getNextRoundRobinHostIndex(String paramString, List<?> paramList) {
    // Byte code:
    //   0: ldc com/mysql/jdbc/ConnectionImpl
    //   2: monitorenter
    //   3: aload_1
    //   4: invokeinterface size : ()I
    //   9: istore_2
    //   10: getstatic com/mysql/jdbc/ConnectionImpl.random : Ljava/util/Random;
    //   13: iload_2
    //   14: invokevirtual nextInt : (I)I
    //   17: istore_2
    //   18: ldc com/mysql/jdbc/ConnectionImpl
    //   20: monitorexit
    //   21: iload_2
    //   22: ireturn
    //   23: astore_0
    //   24: ldc com/mysql/jdbc/ConnectionImpl
    //   26: monitorexit
    //   27: aload_0
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   3	18	23	finally
  }
  
  private MySQLConnection getProxy() {
    MySQLConnection mySQLConnection = this.proxy;
    if (mySQLConnection == null)
      mySQLConnection = this; 
    return mySQLConnection;
  }
  
  private int getServerVariableAsInt(String paramString, int paramInt) throws SQLException {
    try {
      return Integer.parseInt(this.serverVariables.get(paramString));
    } catch (NumberFormatException numberFormatException) {
      getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[] { paramString, this.serverVariables.get(paramString), Integer.valueOf(paramInt) }));
      return paramInt;
    } 
  }
  
  private void handleAutoCommitDefaults() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getElideSetAutoCommits : ()Z
    //   4: istore_3
    //   5: iconst_0
    //   6: istore_2
    //   7: iconst_0
    //   8: istore_1
    //   9: iload_3
    //   10: ifne -> 214
    //   13: aload_0
    //   14: getfield serverVariables : Ljava/util/Map;
    //   17: ldc_w 'init_connect'
    //   20: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   25: checkcast java/lang/String
    //   28: astore #4
    //   30: aload_0
    //   31: iconst_4
    //   32: iconst_1
    //   33: iconst_2
    //   34: invokevirtual versionMeetsMinimum : (III)Z
    //   37: ifeq -> 230
    //   40: aload #4
    //   42: ifnull -> 230
    //   45: aload #4
    //   47: invokevirtual length : ()I
    //   50: ifle -> 230
    //   53: aconst_null
    //   54: astore #6
    //   56: aconst_null
    //   57: astore #4
    //   59: aload_0
    //   60: invokevirtual getMetadataSafeStatement : ()Ljava/sql/Statement;
    //   63: astore #7
    //   65: aload #7
    //   67: ldc_w 'SELECT @@session.autocommit'
    //   70: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   75: astore #5
    //   77: aload #5
    //   79: astore #4
    //   81: aload #5
    //   83: invokeinterface next : ()Z
    //   88: ifeq -> 117
    //   91: aload #5
    //   93: astore #4
    //   95: aload #5
    //   97: iconst_1
    //   98: invokeinterface getBoolean : (I)Z
    //   103: istore_3
    //   104: aload #5
    //   106: astore #4
    //   108: aload_0
    //   109: iload_3
    //   110: putfield autoCommit : Z
    //   113: iload_3
    //   114: iconst_1
    //   115: ixor
    //   116: istore_1
    //   117: aload #5
    //   119: ifnull -> 134
    //   122: aload #5
    //   124: invokeinterface close : ()V
    //   129: goto -> 134
    //   132: astore #4
    //   134: iload_1
    //   135: istore_2
    //   136: aload #7
    //   138: ifnull -> 232
    //   141: aload #7
    //   143: invokeinterface close : ()V
    //   148: iload_1
    //   149: istore_2
    //   150: goto -> 232
    //   153: astore #4
    //   155: iload_1
    //   156: istore_2
    //   157: goto -> 232
    //   160: astore #5
    //   162: aload #4
    //   164: astore #6
    //   166: aload #5
    //   168: astore #4
    //   170: aload #7
    //   172: astore #5
    //   174: goto -> 182
    //   177: astore #4
    //   179: aconst_null
    //   180: astore #5
    //   182: aload #6
    //   184: ifnull -> 199
    //   187: aload #6
    //   189: invokeinterface close : ()V
    //   194: goto -> 199
    //   197: astore #6
    //   199: aload #5
    //   201: ifnull -> 211
    //   204: aload #5
    //   206: invokeinterface close : ()V
    //   211: aload #4
    //   213: athrow
    //   214: aload_0
    //   215: invokevirtual getIO : ()Lcom/mysql/jdbc/MysqlIO;
    //   218: iconst_1
    //   219: invokevirtual isSetNeededForAutoCommitMode : (Z)Z
    //   222: ifeq -> 232
    //   225: aload_0
    //   226: iconst_0
    //   227: putfield autoCommit : Z
    //   230: iconst_1
    //   231: istore_2
    //   232: iload_2
    //   233: ifeq -> 270
    //   236: aload_0
    //   237: iconst_1
    //   238: invokevirtual setAutoCommit : (Z)V
    //   241: goto -> 270
    //   244: astore #4
    //   246: aload #4
    //   248: invokevirtual getErrorCode : ()I
    //   251: sipush #1820
    //   254: if_icmpne -> 267
    //   257: aload_0
    //   258: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   261: ifne -> 267
    //   264: goto -> 270
    //   267: aload #4
    //   269: athrow
    //   270: return
    //   271: astore #5
    //   273: goto -> 211
    // Exception table:
    //   from	to	target	type
    //   59	65	177	finally
    //   65	77	160	finally
    //   81	91	160	finally
    //   95	104	160	finally
    //   108	113	160	finally
    //   122	129	132	java/sql/SQLException
    //   141	148	153	java/sql/SQLException
    //   187	194	197	java/sql/SQLException
    //   204	211	271	java/sql/SQLException
    //   236	241	244	java/sql/SQLException
  }
  
  private void initializeDriverProperties(Properties paramProperties) throws SQLException {
    initializeProperties(paramProperties);
    String str = getExceptionInterceptors();
    if (str != null && !"".equals(str))
      this.exceptionInterceptor = new ExceptionInterceptorChain(str); 
    this.usePlatformCharsetConverters = getUseJvmCharsetConverters();
    this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());
    if (getProfileSql() || getUseUsageAdvisor())
      this.eventSink = ProfilerEventHandlerFactory.getInstance(getMultiHostSafeProxy()); 
    if (getCachePreparedStatements())
      createPreparedStatementCaches(); 
    if (!getNoDatetimeStringSync() || !getUseTimezone()) {
      if (getCacheCallableStatements())
        this.parsedCallableStatementCache = new LRUCache<CompoundCacheKey, CallableStatement.CallableStatementParamInfo>(getCallableStatementCacheSize()); 
      if (getAllowMultiQueries())
        setCacheResultSetMetadata(false); 
      if (getCacheResultSetMetadata())
        this.resultSetMetadataCache = new LRUCache<String, CachedResultSetMetaData>(getMetadataCacheSize()); 
      if (getSocksProxyHost() != null)
        setSocketFactoryClassName("com.mysql.jdbc.SocksProxySocketFactory"); 
      return;
    } 
    throw SQLError.createSQLException("Can't enable noDatetimeStringSync and useTimezone configuration properties at the same time", "01S00", getExceptionInterceptor());
  }
  
  private void initializePropsFromServer() throws SQLException {
    String str = getConnectionLifecycleInterceptors();
    this.connectionLifecycleInterceptors = null;
    if (str != null)
      this.connectionLifecycleInterceptors = Util.loadExtensions(this, this.props, str, "Connection.badLifecycleInterceptor", getExceptionInterceptor()); 
    setSessionVariables();
    if (!versionMeetsMinimum(4, 1, 0))
      setTransformedBitIsBoolean(false); 
    this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);
    if (getUseServerPreparedStmts() && versionMeetsMinimum(4, 1, 0)) {
      this.useServerPreparedStmts = true;
      if (versionMeetsMinimum(5, 0, 0) && !versionMeetsMinimum(5, 0, 3))
        this.useServerPreparedStmts = false; 
    } 
    if (versionMeetsMinimum(3, 21, 22)) {
      boolean bool;
      loadServerVariables();
      if (versionMeetsMinimum(5, 0, 2)) {
        this.autoIncrementIncrement = getServerVariableAsInt("auto_increment_increment", 1);
      } else {
        this.autoIncrementIncrement = 1;
      } 
      buildCollationMapping();
      if (this.io.serverCharsetIndex == 0) {
        String str1 = this.serverVariables.get("collation_server");
        if (str1 != null) {
          byte b = 1;
          while (true) {
            String[] arrayOfString = CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME;
            if (b < arrayOfString.length) {
              if (arrayOfString[b].equals(str1)) {
                this.io.serverCharsetIndex = b;
                break;
              } 
              b++;
              continue;
            } 
            break;
          } 
        } else {
          this.io.serverCharsetIndex = 45;
        } 
      } 
      LicenseConfiguration.checkLicenseType(this.serverVariables);
      str = this.serverVariables.get("lower_case_table_names");
      if ("on".equalsIgnoreCase(str) || "1".equalsIgnoreCase(str) || "2".equalsIgnoreCase(str)) {
        bool = true;
      } else {
        bool = false;
      } 
      this.lowerCaseTableNames = bool;
      if ("1".equalsIgnoreCase(str) || "on".equalsIgnoreCase(str)) {
        bool = true;
      } else {
        bool = false;
      } 
      this.storesLowerCaseTableName = bool;
      configureTimezone();
      if (this.serverVariables.containsKey("max_allowed_packet")) {
        int i = getServerVariableAsInt("max_allowed_packet", -1);
        if (i != -1 && (i < getMaxAllowedPacket() || getMaxAllowedPacket() <= 0)) {
          setMaxAllowedPacket(i);
        } else if (i == -1 && getMaxAllowedPacket() == -1) {
          setMaxAllowedPacket(65535);
        } 
        if (getUseServerPrepStmts()) {
          i = Math.min(getBlobSendChunkSize(), getMaxAllowedPacket()) - 8203;
          if (i > 0) {
            setBlobSendChunkSize(String.valueOf(i));
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Connection setting too low for 'maxAllowedPacket'. When 'useServerPrepStmts=true', 'maxAllowedPacket' must be higher than ");
            stringBuilder.append(8203);
            stringBuilder.append(". Check also 'max_allowed_packet' in MySQL configuration files.");
            throw SQLError.createSQLException(stringBuilder.toString(), "01S00", getExceptionInterceptor());
          } 
        } 
      } 
      if (this.serverVariables.containsKey("net_buffer_length"))
        this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16384); 
      checkTransactionIsolationLevel();
      if (!versionMeetsMinimum(4, 1, 0))
        checkServerEncoding(); 
      this.io.checkForCharsetMismatch();
      if (this.serverVariables.containsKey("sql_mode")) {
        str = this.serverVariables.get("sql_mode");
        if (StringUtils.isStrictlyNumeric(str)) {
          if ((Integer.parseInt(str) & 0x4) > 0) {
            bool = true;
          } else {
            bool = false;
          } 
          this.useAnsiQuotes = bool;
        } else if (str != null) {
          if (str.indexOf("ANSI_QUOTES") != -1) {
            bool = true;
          } else {
            bool = false;
          } 
          this.useAnsiQuotes = bool;
          if (str.indexOf("NO_BACKSLASH_ESCAPES") != -1) {
            bool = true;
          } else {
            bool = false;
          } 
          this.noBackslashEscapes = bool;
          if (str.indexOf("TIME_TRUNCATE_FRACTIONAL") != -1) {
            bool = true;
          } else {
            bool = false;
          } 
          this.serverTruncatesFracSecs = bool;
        } 
      } 
    } 
    configureClientCharacterSet(false);
    try {
      this.errorMessageEncoding = CharsetMapping.getCharacterEncodingForErrorMessages(this);
      if (versionMeetsMinimum(3, 23, 15)) {
        this.transactionsSupported = true;
        handleAutoCommitDefaults();
      } else {
        this.transactionsSupported = false;
      } 
      if (versionMeetsMinimum(3, 23, 36)) {
        this.hasIsolationLevels = true;
      } else {
        this.hasIsolationLevels = false;
      } 
      this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);
      this.io.resetMaxBuf();
      if (this.io.versionMeetsMinimum(4, 1, 0)) {
        str = this.serverVariables.get("jdbc.local.character_set_results");
        if (str == null || StringUtils.startsWithIgnoreCaseAndWs(str, "NULL") || str.length() == 0) {
          str = this.serverVariables.get("character_set_system");
          if (str != null) {
            str = CharsetMapping.getJavaEncodingForMysqlCharset(str);
          } else {
            str = "UTF-8";
          } 
          this.characterSetMetadata = str;
        } else {
          str = CharsetMapping.getJavaEncodingForMysqlCharset(str);
          this.characterSetResultsOnServer = str;
          this.characterSetMetadata = str;
        } 
      } else {
        this.characterSetMetadata = getEncoding();
      } 
      if (versionMeetsMinimum(4, 1, 0) && !versionMeetsMinimum(4, 1, 10) && getAllowMultiQueries() && isQueryCacheEnabled())
        setAllowMultiQueries(false); 
      if (versionMeetsMinimum(5, 0, 0) && (getUseLocalTransactionState() || getElideSetAutoCommits()) && isQueryCacheEnabled() && !versionMeetsMinimum(5, 1, 32)) {
        setUseLocalTransactionState(false);
        setElideSetAutoCommits(false);
      } 
      setupServerForTruncationChecks();
      return;
    } catch (SQLException sQLException) {
      throw sQLException;
    } catch (RuntimeException runtimeException) {
      SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
      sQLException.initCause(runtimeException);
      throw sQLException;
    } 
  }
  
  private void loadServerVariables() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getCacheServerConfiguration : ()Z
    //   4: ifeq -> 98
    //   7: aload_0
    //   8: invokespecial createConfigCacheIfNeeded : ()V
    //   11: aload_0
    //   12: getfield serverConfigCache : Lcom/mysql/jdbc/CacheAdapter;
    //   15: aload_0
    //   16: invokevirtual getURL : ()Ljava/lang/String;
    //   19: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   24: checkcast java/util/Map
    //   27: astore #6
    //   29: aload #6
    //   31: ifnull -> 98
    //   34: aload #6
    //   36: ldc 'server_version_string'
    //   38: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   43: checkcast java/lang/String
    //   46: astore #5
    //   48: aload #5
    //   50: ifnull -> 85
    //   53: aload_0
    //   54: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   57: invokevirtual getServerVersion : ()Ljava/lang/String;
    //   60: ifnull -> 85
    //   63: aload #5
    //   65: aload_0
    //   66: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   69: invokevirtual getServerVersion : ()Ljava/lang/String;
    //   72: invokevirtual equals : (Ljava/lang/Object;)Z
    //   75: ifeq -> 85
    //   78: aload_0
    //   79: aload #6
    //   81: putfield serverVariables : Ljava/util/Map;
    //   84: return
    //   85: aload_0
    //   86: getfield serverConfigCache : Lcom/mysql/jdbc/CacheAdapter;
    //   89: aload_0
    //   90: invokevirtual getURL : ()Ljava/lang/String;
    //   93: invokeinterface invalidate : (Ljava/lang/Object;)V
    //   98: aconst_null
    //   99: astore #12
    //   101: aconst_null
    //   102: astore #11
    //   104: aconst_null
    //   105: astore #13
    //   107: aconst_null
    //   108: astore #14
    //   110: aconst_null
    //   111: astore #6
    //   113: aconst_null
    //   114: astore #7
    //   116: aconst_null
    //   117: astore #10
    //   119: aload_0
    //   120: invokevirtual getMetadataSafeStatement : ()Ljava/sql/Statement;
    //   123: astore #9
    //   125: aload #13
    //   127: astore #5
    //   129: aload #14
    //   131: astore #6
    //   133: aload_0
    //   134: getfield dbmd : Ljava/sql/DatabaseMetaData;
    //   137: invokeinterface getDriverVersion : ()Ljava/lang/String;
    //   142: astore #8
    //   144: aload #8
    //   146: astore #7
    //   148: aload #8
    //   150: ifnull -> 306
    //   153: aload #8
    //   155: astore #7
    //   157: aload #13
    //   159: astore #5
    //   161: aload #14
    //   163: astore #6
    //   165: aload #8
    //   167: bipush #42
    //   169: invokevirtual indexOf : (I)I
    //   172: iconst_m1
    //   173: if_icmpeq -> 306
    //   176: aload #13
    //   178: astore #5
    //   180: aload #14
    //   182: astore #6
    //   184: new java/lang/StringBuilder
    //   187: astore #7
    //   189: aload #13
    //   191: astore #5
    //   193: aload #14
    //   195: astore #6
    //   197: aload #7
    //   199: aload #8
    //   201: invokevirtual length : ()I
    //   204: bipush #10
    //   206: iadd
    //   207: invokespecial <init> : (I)V
    //   210: iconst_0
    //   211: istore_2
    //   212: aload #13
    //   214: astore #5
    //   216: aload #14
    //   218: astore #6
    //   220: iload_2
    //   221: aload #8
    //   223: invokevirtual length : ()I
    //   226: if_icmpge -> 291
    //   229: aload #13
    //   231: astore #5
    //   233: aload #14
    //   235: astore #6
    //   237: aload #8
    //   239: iload_2
    //   240: invokevirtual charAt : (I)C
    //   243: istore_1
    //   244: iload_1
    //   245: bipush #42
    //   247: if_icmpne -> 270
    //   250: aload #13
    //   252: astore #5
    //   254: aload #14
    //   256: astore #6
    //   258: aload #7
    //   260: ldc_w '[star]'
    //   263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   266: pop
    //   267: goto -> 285
    //   270: aload #13
    //   272: astore #5
    //   274: aload #14
    //   276: astore #6
    //   278: aload #7
    //   280: iload_1
    //   281: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   284: pop
    //   285: iinc #2, 1
    //   288: goto -> 212
    //   291: aload #13
    //   293: astore #5
    //   295: aload #14
    //   297: astore #6
    //   299: aload #7
    //   301: invokevirtual toString : ()Ljava/lang/String;
    //   304: astore #7
    //   306: aload #13
    //   308: astore #5
    //   310: aload #14
    //   312: astore #6
    //   314: aload_0
    //   315: invokevirtual getParanoid : ()Z
    //   318: ifne -> 423
    //   321: aload #7
    //   323: ifnonnull -> 329
    //   326: goto -> 423
    //   329: aload #13
    //   331: astore #5
    //   333: aload #14
    //   335: astore #6
    //   337: new java/lang/StringBuilder
    //   340: astore #8
    //   342: aload #13
    //   344: astore #5
    //   346: aload #14
    //   348: astore #6
    //   350: aload #8
    //   352: invokespecial <init> : ()V
    //   355: aload #13
    //   357: astore #5
    //   359: aload #14
    //   361: astore #6
    //   363: aload #8
    //   365: ldc_w '/* '
    //   368: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   371: pop
    //   372: aload #13
    //   374: astore #5
    //   376: aload #14
    //   378: astore #6
    //   380: aload #8
    //   382: aload #7
    //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   387: pop
    //   388: aload #13
    //   390: astore #5
    //   392: aload #14
    //   394: astore #6
    //   396: aload #8
    //   398: ldc_w ' */'
    //   401: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   404: pop
    //   405: aload #13
    //   407: astore #5
    //   409: aload #14
    //   411: astore #6
    //   413: aload #8
    //   415: invokevirtual toString : ()Ljava/lang/String;
    //   418: astore #8
    //   420: goto -> 428
    //   423: ldc_w ''
    //   426: astore #8
    //   428: aload #13
    //   430: astore #5
    //   432: aload #14
    //   434: astore #6
    //   436: new java/util/HashMap
    //   439: astore #7
    //   441: aload #13
    //   443: astore #5
    //   445: aload #14
    //   447: astore #6
    //   449: aload #7
    //   451: invokespecial <init> : ()V
    //   454: aload #13
    //   456: astore #5
    //   458: aload #14
    //   460: astore #6
    //   462: aload_0
    //   463: aload #7
    //   465: putfield serverVariables : Ljava/util/Map;
    //   468: aload #13
    //   470: astore #5
    //   472: aload #14
    //   474: astore #6
    //   476: aload_0
    //   477: invokevirtual getJdbcCompliantTruncation : ()Z
    //   480: istore #4
    //   482: aload #13
    //   484: astore #5
    //   486: aload #14
    //   488: astore #6
    //   490: aload_0
    //   491: iconst_0
    //   492: invokevirtual setJdbcCompliantTruncation : (Z)V
    //   495: iconst_1
    //   496: istore_2
    //   497: aload #12
    //   499: astore #7
    //   501: aload_0
    //   502: iconst_5
    //   503: iconst_1
    //   504: iconst_0
    //   505: invokevirtual versionMeetsMinimum : (III)Z
    //   508: ifeq -> 1044
    //   511: aload #12
    //   513: astore #7
    //   515: new java/lang/StringBuilder
    //   518: astore #5
    //   520: aload #12
    //   522: astore #7
    //   524: aload #5
    //   526: aload #8
    //   528: invokespecial <init> : (Ljava/lang/String;)V
    //   531: aload #12
    //   533: astore #7
    //   535: aload #5
    //   537: ldc_w 'SELECT'
    //   540: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   543: pop
    //   544: aload #12
    //   546: astore #7
    //   548: aload #5
    //   550: ldc_w '  @@session.auto_increment_increment AS auto_increment_increment'
    //   553: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   556: pop
    //   557: aload #12
    //   559: astore #7
    //   561: aload #5
    //   563: ldc_w ', @@character_set_client AS character_set_client'
    //   566: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   569: pop
    //   570: aload #12
    //   572: astore #7
    //   574: aload #5
    //   576: ldc_w ', @@character_set_connection AS character_set_connection'
    //   579: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   582: pop
    //   583: aload #12
    //   585: astore #7
    //   587: aload #5
    //   589: ldc_w ', @@character_set_results AS character_set_results'
    //   592: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   595: pop
    //   596: aload #12
    //   598: astore #7
    //   600: aload #5
    //   602: ldc_w ', @@character_set_server AS character_set_server'
    //   605: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   608: pop
    //   609: aload #12
    //   611: astore #7
    //   613: aload #5
    //   615: ldc_w ', @@collation_server AS collation_server'
    //   618: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   621: pop
    //   622: aload #12
    //   624: astore #7
    //   626: aload #5
    //   628: ldc_w ', @@collation_connection AS collation_connection'
    //   631: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   634: pop
    //   635: aload #12
    //   637: astore #7
    //   639: aload #5
    //   641: ldc_w ', @@init_connect AS init_connect'
    //   644: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   647: pop
    //   648: aload #12
    //   650: astore #7
    //   652: aload #5
    //   654: ldc_w ', @@interactive_timeout AS interactive_timeout'
    //   657: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   660: pop
    //   661: aload #12
    //   663: astore #7
    //   665: aload_0
    //   666: iconst_5
    //   667: iconst_5
    //   668: iconst_0
    //   669: invokevirtual versionMeetsMinimum : (III)Z
    //   672: ifne -> 688
    //   675: aload #12
    //   677: astore #7
    //   679: aload #5
    //   681: ldc_w ', @@language AS language'
    //   684: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   687: pop
    //   688: aload #12
    //   690: astore #7
    //   692: aload #5
    //   694: ldc_w ', @@license AS license'
    //   697: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   700: pop
    //   701: aload #12
    //   703: astore #7
    //   705: aload #5
    //   707: ldc_w ', @@lower_case_table_names AS lower_case_table_names'
    //   710: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   713: pop
    //   714: aload #12
    //   716: astore #7
    //   718: aload #5
    //   720: ldc_w ', @@max_allowed_packet AS max_allowed_packet'
    //   723: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   726: pop
    //   727: aload #12
    //   729: astore #7
    //   731: aload #5
    //   733: ldc_w ', @@net_buffer_length AS net_buffer_length'
    //   736: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   739: pop
    //   740: aload #12
    //   742: astore #7
    //   744: aload #5
    //   746: ldc_w ', @@net_write_timeout AS net_write_timeout'
    //   749: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   752: pop
    //   753: aload #12
    //   755: astore #7
    //   757: aload_0
    //   758: bipush #8
    //   760: iconst_0
    //   761: iconst_3
    //   762: invokevirtual versionMeetsMinimum : (III)Z
    //   765: ifne -> 794
    //   768: aload #12
    //   770: astore #7
    //   772: aload #5
    //   774: ldc_w ', @@query_cache_size AS query_cache_size'
    //   777: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   780: pop
    //   781: aload #12
    //   783: astore #7
    //   785: aload #5
    //   787: ldc_w ', @@query_cache_type AS query_cache_type'
    //   790: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   793: pop
    //   794: aload #12
    //   796: astore #7
    //   798: aload #5
    //   800: ldc_w ', @@sql_mode AS sql_mode'
    //   803: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   806: pop
    //   807: aload #12
    //   809: astore #7
    //   811: aload #5
    //   813: ldc_w ', @@system_time_zone AS system_time_zone'
    //   816: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   819: pop
    //   820: aload #12
    //   822: astore #7
    //   824: aload #5
    //   826: ldc_w ', @@time_zone AS time_zone'
    //   829: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   832: pop
    //   833: aload #12
    //   835: astore #7
    //   837: aload_0
    //   838: bipush #8
    //   840: iconst_0
    //   841: iconst_3
    //   842: invokevirtual versionMeetsMinimum : (III)Z
    //   845: ifne -> 898
    //   848: aload #12
    //   850: astore #7
    //   852: aload_0
    //   853: iconst_5
    //   854: bipush #7
    //   856: bipush #20
    //   858: invokevirtual versionMeetsMinimum : (III)Z
    //   861: ifeq -> 882
    //   864: aload #12
    //   866: astore #7
    //   868: aload_0
    //   869: bipush #8
    //   871: iconst_0
    //   872: iconst_0
    //   873: invokevirtual versionMeetsMinimum : (III)Z
    //   876: ifne -> 882
    //   879: goto -> 898
    //   882: aload #12
    //   884: astore #7
    //   886: aload #5
    //   888: ldc_w ', @@tx_isolation AS transaction_isolation'
    //   891: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   894: pop
    //   895: goto -> 911
    //   898: aload #12
    //   900: astore #7
    //   902: aload #5
    //   904: ldc_w ', @@transaction_isolation AS transaction_isolation'
    //   907: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   910: pop
    //   911: aload #12
    //   913: astore #7
    //   915: aload #5
    //   917: ldc_w ', @@wait_timeout AS wait_timeout'
    //   920: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   923: pop
    //   924: aload #12
    //   926: astore #7
    //   928: aload #9
    //   930: aload #5
    //   932: invokevirtual toString : ()Ljava/lang/String;
    //   935: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   940: astore #8
    //   942: aload #8
    //   944: astore #7
    //   946: aload #8
    //   948: astore #6
    //   950: aload #8
    //   952: astore #5
    //   954: aload #8
    //   956: invokeinterface next : ()Z
    //   961: ifeq -> 1164
    //   964: aload #8
    //   966: astore #6
    //   968: aload #8
    //   970: astore #5
    //   972: aload #8
    //   974: invokeinterface getMetaData : ()Ljava/sql/ResultSetMetaData;
    //   979: astore #11
    //   981: aload #8
    //   983: astore #7
    //   985: aload #8
    //   987: astore #6
    //   989: aload #8
    //   991: astore #5
    //   993: iload_2
    //   994: aload #11
    //   996: invokeinterface getColumnCount : ()I
    //   1001: if_icmpgt -> 1164
    //   1004: aload #8
    //   1006: astore #6
    //   1008: aload #8
    //   1010: astore #5
    //   1012: aload_0
    //   1013: getfield serverVariables : Ljava/util/Map;
    //   1016: aload #11
    //   1018: iload_2
    //   1019: invokeinterface getColumnLabel : (I)Ljava/lang/String;
    //   1024: aload #8
    //   1026: iload_2
    //   1027: invokeinterface getString : (I)Ljava/lang/String;
    //   1032: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1037: pop
    //   1038: iinc #2, 1
    //   1041: goto -> 981
    //   1044: aload #12
    //   1046: astore #7
    //   1048: new java/lang/StringBuilder
    //   1051: astore #5
    //   1053: aload #12
    //   1055: astore #7
    //   1057: aload #5
    //   1059: invokespecial <init> : ()V
    //   1062: aload #12
    //   1064: astore #7
    //   1066: aload #5
    //   1068: aload #8
    //   1070: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1073: pop
    //   1074: aload #12
    //   1076: astore #7
    //   1078: aload #5
    //   1080: ldc_w 'SHOW VARIABLES'
    //   1083: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1086: pop
    //   1087: aload #12
    //   1089: astore #7
    //   1091: aload #9
    //   1093: aload #5
    //   1095: invokevirtual toString : ()Ljava/lang/String;
    //   1098: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   1103: astore #8
    //   1105: aload #8
    //   1107: astore #7
    //   1109: aload #8
    //   1111: astore #6
    //   1113: aload #8
    //   1115: astore #5
    //   1117: aload #8
    //   1119: invokeinterface next : ()Z
    //   1124: ifeq -> 1164
    //   1127: aload #8
    //   1129: astore #6
    //   1131: aload #8
    //   1133: astore #5
    //   1135: aload_0
    //   1136: getfield serverVariables : Ljava/util/Map;
    //   1139: aload #8
    //   1141: iconst_1
    //   1142: invokeinterface getString : (I)Ljava/lang/String;
    //   1147: aload #8
    //   1149: iconst_2
    //   1150: invokeinterface getString : (I)Ljava/lang/String;
    //   1155: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1160: pop
    //   1161: goto -> 1105
    //   1164: aload #7
    //   1166: astore #6
    //   1168: aload #7
    //   1170: astore #5
    //   1172: aload #7
    //   1174: invokeinterface close : ()V
    //   1179: aload #10
    //   1181: astore #7
    //   1183: aload #7
    //   1185: astore #5
    //   1187: aload #7
    //   1189: astore #6
    //   1191: aload_0
    //   1192: iload #4
    //   1194: invokevirtual setJdbcCompliantTruncation : (Z)V
    //   1197: goto -> 1260
    //   1200: astore #8
    //   1202: aload #6
    //   1204: astore #7
    //   1206: goto -> 1364
    //   1209: astore #6
    //   1211: goto -> 1225
    //   1214: astore #8
    //   1216: goto -> 1364
    //   1219: astore #6
    //   1221: aload #11
    //   1223: astore #5
    //   1225: aload #5
    //   1227: astore #7
    //   1229: aload #6
    //   1231: invokevirtual getErrorCode : ()I
    //   1234: sipush #1820
    //   1237: if_icmpne -> 1357
    //   1240: aload #5
    //   1242: astore #7
    //   1244: aload_0
    //   1245: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   1248: istore_3
    //   1249: iload_3
    //   1250: ifne -> 1357
    //   1253: aload #5
    //   1255: astore #7
    //   1257: goto -> 1183
    //   1260: aload #7
    //   1262: astore #5
    //   1264: aload #7
    //   1266: astore #6
    //   1268: aload_0
    //   1269: invokevirtual getCacheServerConfiguration : ()Z
    //   1272: ifeq -> 1327
    //   1275: aload #7
    //   1277: astore #5
    //   1279: aload #7
    //   1281: astore #6
    //   1283: aload_0
    //   1284: getfield serverVariables : Ljava/util/Map;
    //   1287: ldc 'server_version_string'
    //   1289: aload_0
    //   1290: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   1293: invokevirtual getServerVersion : ()Ljava/lang/String;
    //   1296: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1301: pop
    //   1302: aload #7
    //   1304: astore #5
    //   1306: aload #7
    //   1308: astore #6
    //   1310: aload_0
    //   1311: getfield serverConfigCache : Lcom/mysql/jdbc/CacheAdapter;
    //   1314: aload_0
    //   1315: invokevirtual getURL : ()Ljava/lang/String;
    //   1318: aload_0
    //   1319: getfield serverVariables : Ljava/util/Map;
    //   1322: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   1327: aload #7
    //   1329: ifnull -> 1344
    //   1332: aload #7
    //   1334: invokeinterface close : ()V
    //   1339: goto -> 1344
    //   1342: astore #5
    //   1344: aload #9
    //   1346: ifnull -> 1356
    //   1349: aload #9
    //   1351: invokeinterface close : ()V
    //   1356: return
    //   1357: aload #5
    //   1359: astore #7
    //   1361: aload #6
    //   1363: athrow
    //   1364: aload #7
    //   1366: astore #5
    //   1368: aload #7
    //   1370: astore #6
    //   1372: aload_0
    //   1373: iload #4
    //   1375: invokevirtual setJdbcCompliantTruncation : (Z)V
    //   1378: aload #7
    //   1380: astore #5
    //   1382: aload #7
    //   1384: astore #6
    //   1386: aload #8
    //   1388: athrow
    //   1389: astore #7
    //   1391: aload #5
    //   1393: astore #6
    //   1395: aload #7
    //   1397: astore #5
    //   1399: aload #9
    //   1401: astore #7
    //   1403: goto -> 1457
    //   1406: astore #7
    //   1408: aload #6
    //   1410: astore #5
    //   1412: aload #9
    //   1414: astore #6
    //   1416: goto -> 1440
    //   1419: astore #5
    //   1421: aconst_null
    //   1422: astore #8
    //   1424: aload #7
    //   1426: astore #6
    //   1428: aload #8
    //   1430: astore #7
    //   1432: goto -> 1457
    //   1435: astore #7
    //   1437: aconst_null
    //   1438: astore #5
    //   1440: aload #7
    //   1442: athrow
    //   1443: astore #8
    //   1445: aload #6
    //   1447: astore #7
    //   1449: aload #5
    //   1451: astore #6
    //   1453: aload #8
    //   1455: astore #5
    //   1457: aload #6
    //   1459: ifnull -> 1474
    //   1462: aload #6
    //   1464: invokeinterface close : ()V
    //   1469: goto -> 1474
    //   1472: astore #6
    //   1474: aload #7
    //   1476: ifnull -> 1486
    //   1479: aload #7
    //   1481: invokeinterface close : ()V
    //   1486: aload #5
    //   1488: athrow
    //   1489: astore #5
    //   1491: goto -> 1356
    //   1494: astore #6
    //   1496: goto -> 1486
    // Exception table:
    //   from	to	target	type
    //   119	125	1435	java/sql/SQLException
    //   119	125	1419	finally
    //   133	144	1406	java/sql/SQLException
    //   133	144	1389	finally
    //   165	176	1406	java/sql/SQLException
    //   165	176	1389	finally
    //   184	189	1406	java/sql/SQLException
    //   184	189	1389	finally
    //   197	210	1406	java/sql/SQLException
    //   197	210	1389	finally
    //   220	229	1406	java/sql/SQLException
    //   220	229	1389	finally
    //   237	244	1406	java/sql/SQLException
    //   237	244	1389	finally
    //   258	267	1406	java/sql/SQLException
    //   258	267	1389	finally
    //   278	285	1406	java/sql/SQLException
    //   278	285	1389	finally
    //   299	306	1406	java/sql/SQLException
    //   299	306	1389	finally
    //   314	321	1406	java/sql/SQLException
    //   314	321	1389	finally
    //   337	342	1406	java/sql/SQLException
    //   337	342	1389	finally
    //   350	355	1406	java/sql/SQLException
    //   350	355	1389	finally
    //   363	372	1406	java/sql/SQLException
    //   363	372	1389	finally
    //   380	388	1406	java/sql/SQLException
    //   380	388	1389	finally
    //   396	405	1406	java/sql/SQLException
    //   396	405	1389	finally
    //   413	420	1406	java/sql/SQLException
    //   413	420	1389	finally
    //   436	441	1406	java/sql/SQLException
    //   436	441	1389	finally
    //   449	454	1406	java/sql/SQLException
    //   449	454	1389	finally
    //   462	468	1406	java/sql/SQLException
    //   462	468	1389	finally
    //   476	482	1406	java/sql/SQLException
    //   476	482	1389	finally
    //   490	495	1406	java/sql/SQLException
    //   490	495	1389	finally
    //   501	511	1219	java/sql/SQLException
    //   501	511	1214	finally
    //   515	520	1219	java/sql/SQLException
    //   515	520	1214	finally
    //   524	531	1219	java/sql/SQLException
    //   524	531	1214	finally
    //   535	544	1219	java/sql/SQLException
    //   535	544	1214	finally
    //   548	557	1219	java/sql/SQLException
    //   548	557	1214	finally
    //   561	570	1219	java/sql/SQLException
    //   561	570	1214	finally
    //   574	583	1219	java/sql/SQLException
    //   574	583	1214	finally
    //   587	596	1219	java/sql/SQLException
    //   587	596	1214	finally
    //   600	609	1219	java/sql/SQLException
    //   600	609	1214	finally
    //   613	622	1219	java/sql/SQLException
    //   613	622	1214	finally
    //   626	635	1219	java/sql/SQLException
    //   626	635	1214	finally
    //   639	648	1219	java/sql/SQLException
    //   639	648	1214	finally
    //   652	661	1219	java/sql/SQLException
    //   652	661	1214	finally
    //   665	675	1219	java/sql/SQLException
    //   665	675	1214	finally
    //   679	688	1219	java/sql/SQLException
    //   679	688	1214	finally
    //   692	701	1219	java/sql/SQLException
    //   692	701	1214	finally
    //   705	714	1219	java/sql/SQLException
    //   705	714	1214	finally
    //   718	727	1219	java/sql/SQLException
    //   718	727	1214	finally
    //   731	740	1219	java/sql/SQLException
    //   731	740	1214	finally
    //   744	753	1219	java/sql/SQLException
    //   744	753	1214	finally
    //   757	768	1219	java/sql/SQLException
    //   757	768	1214	finally
    //   772	781	1219	java/sql/SQLException
    //   772	781	1214	finally
    //   785	794	1219	java/sql/SQLException
    //   785	794	1214	finally
    //   798	807	1219	java/sql/SQLException
    //   798	807	1214	finally
    //   811	820	1219	java/sql/SQLException
    //   811	820	1214	finally
    //   824	833	1219	java/sql/SQLException
    //   824	833	1214	finally
    //   837	848	1219	java/sql/SQLException
    //   837	848	1214	finally
    //   852	864	1219	java/sql/SQLException
    //   852	864	1214	finally
    //   868	879	1219	java/sql/SQLException
    //   868	879	1214	finally
    //   886	895	1219	java/sql/SQLException
    //   886	895	1214	finally
    //   902	911	1219	java/sql/SQLException
    //   902	911	1214	finally
    //   915	924	1219	java/sql/SQLException
    //   915	924	1214	finally
    //   928	942	1219	java/sql/SQLException
    //   928	942	1214	finally
    //   954	964	1209	java/sql/SQLException
    //   954	964	1200	finally
    //   972	981	1209	java/sql/SQLException
    //   972	981	1200	finally
    //   993	1004	1209	java/sql/SQLException
    //   993	1004	1200	finally
    //   1012	1038	1209	java/sql/SQLException
    //   1012	1038	1200	finally
    //   1048	1053	1219	java/sql/SQLException
    //   1048	1053	1214	finally
    //   1057	1062	1219	java/sql/SQLException
    //   1057	1062	1214	finally
    //   1066	1074	1219	java/sql/SQLException
    //   1066	1074	1214	finally
    //   1078	1087	1219	java/sql/SQLException
    //   1078	1087	1214	finally
    //   1091	1105	1219	java/sql/SQLException
    //   1091	1105	1214	finally
    //   1117	1127	1209	java/sql/SQLException
    //   1117	1127	1200	finally
    //   1135	1161	1209	java/sql/SQLException
    //   1135	1161	1200	finally
    //   1172	1179	1209	java/sql/SQLException
    //   1172	1179	1200	finally
    //   1191	1197	1406	java/sql/SQLException
    //   1191	1197	1389	finally
    //   1229	1240	1214	finally
    //   1244	1249	1214	finally
    //   1268	1275	1406	java/sql/SQLException
    //   1268	1275	1389	finally
    //   1283	1302	1406	java/sql/SQLException
    //   1283	1302	1389	finally
    //   1310	1327	1406	java/sql/SQLException
    //   1310	1327	1389	finally
    //   1332	1339	1342	java/sql/SQLException
    //   1349	1356	1489	java/sql/SQLException
    //   1361	1364	1214	finally
    //   1372	1378	1406	java/sql/SQLException
    //   1372	1378	1389	finally
    //   1386	1389	1406	java/sql/SQLException
    //   1386	1389	1389	finally
    //   1440	1443	1443	finally
    //   1462	1469	1472	java/sql/SQLException
    //   1479	1486	1494	java/sql/SQLException
  }
  
  private String normalizeHost(String paramString) {
    return (paramString == null || StringUtils.isEmptyOrWhitespaceOnly(paramString)) ? "localhost" : paramString;
  }
  
  private static boolean nullSafeCompare(String paramString1, String paramString2) {
    boolean bool = true;
    if (paramString1 == null && paramString2 == null)
      return true; 
    if (paramString1 == null && paramString2 != null)
      return false; 
    if (paramString1 == null || !paramString1.equals(paramString2))
      bool = false; 
    return bool;
  }
  
  private CallableStatement parseCallableStatement(String paramString) throws SQLException {
    boolean bool;
    Object object = EscapeProcessor.escapeSQL(paramString, serverSupportsConvertFn(), getMultiHostSafeProxy());
    if (object instanceof EscapeProcessorResult) {
      EscapeProcessorResult escapeProcessorResult = (EscapeProcessorResult)object;
      object = escapeProcessorResult.escapedSql;
      bool = escapeProcessorResult.callingStoredFunction;
    } else {
      object = object;
      bool = false;
    } 
    return CallableStatement.getInstance(getMultiHostSafeProxy(), (String)object, this.database, bool);
  }
  
  private int parsePortNumber(String paramString) throws SQLException {
    try {
      return Integer.parseInt(paramString);
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Illegal connection port value '");
      stringBuilder.append(paramString);
      stringBuilder.append("'");
      throw SQLError.createSQLException(stringBuilder.toString(), "01S00", getExceptionInterceptor());
    } 
  }
  
  private void repartitionHistogram(int[] paramArrayOfint, long[] paramArrayOflong, long paramLong1, long paramLong2) {
    if (this.oldHistCounts == null) {
      this.oldHistCounts = new int[paramArrayOfint.length];
      this.oldHistBreakpoints = new long[paramArrayOflong.length];
    } 
    System.arraycopy(paramArrayOfint, 0, this.oldHistCounts, 0, paramArrayOfint.length);
    System.arraycopy(paramArrayOflong, 0, this.oldHistBreakpoints, 0, paramArrayOflong.length);
    createInitialHistogram(paramArrayOflong, paramLong1, paramLong2);
    for (byte b = 0; b < 20; b++)
      addToHistogram(paramArrayOfint, paramArrayOflong, this.oldHistBreakpoints[b], this.oldHistCounts[b], paramLong1, paramLong2); 
  }
  
  private void repartitionPerformanceHistogram() {
    checkAndCreatePerformanceHistogram();
    int[] arrayOfInt = this.perfMetricsHistCounts;
    long[] arrayOfLong = this.perfMetricsHistBreakpoints;
    long l2 = this.shortestQueryTimeMs;
    long l1 = l2;
    if (l2 == Long.MAX_VALUE)
      l1 = 0L; 
    repartitionHistogram(arrayOfInt, arrayOfLong, l1, this.longestQueryTimeMs);
  }
  
  private void repartitionTablesAccessedHistogram() {
    checkAndCreateTablesAccessedHistogram();
    int[] arrayOfInt = this.numTablesMetricsHistCounts;
    long[] arrayOfLong = this.numTablesMetricsHistBreakpoints;
    long l2 = this.minimumNumberTablesAccessed;
    long l1 = l2;
    if (l2 == Long.MAX_VALUE)
      l1 = 0L; 
    repartitionHistogram(arrayOfInt, arrayOfLong, l1, this.maximumNumberTablesAccessed);
  }
  
  private void reportMetrics() {
    if (getGatherPerformanceMetrics()) {
      StringBuilder stringBuilder2 = new StringBuilder(256);
      stringBuilder2.append("** Performance Metrics Report **\n");
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nLongest reported query: ");
      stringBuilder1.append(this.longestQueryTimeMs);
      stringBuilder1.append(" ms");
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nShortest reported query: ");
      stringBuilder1.append(this.shortestQueryTimeMs);
      stringBuilder1.append(" ms");
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nAverage query execution time: ");
      stringBuilder1.append(this.totalQueryTimeMs / this.numberOfQueriesIssued);
      stringBuilder1.append(" ms");
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nNumber of statements executed: ");
      stringBuilder1.append(this.numberOfQueriesIssued);
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nNumber of result sets created: ");
      stringBuilder1.append(this.numberOfResultSetsCreated);
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nNumber of statements prepared: ");
      stringBuilder1.append(this.numberOfPrepares);
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\nNumber of prepared statement executions: ");
      stringBuilder1.append(this.numberOfPreparedExecutes);
      stringBuilder2.append(stringBuilder1.toString());
      long[] arrayOfLong = this.perfMetricsHistBreakpoints;
      String str2 = "\t";
      String str1 = "\n\tbetween ";
      if (arrayOfLong != null) {
        stringBuilder2.append("\n\n\tTiming Histogram:\n");
        byte b = 0;
        int i;
        for (i = Integer.MIN_VALUE; b < 20; i = k) {
          int[] arrayOfInt = this.perfMetricsHistCounts;
          int k = i;
          if (arrayOfInt[b] > i)
            k = arrayOfInt[b]; 
          b++;
        } 
        int j = i;
        if (i == 0)
          j = 1; 
        i = 0;
        while (i < 19) {
          if (i == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n\tless than ");
            stringBuilder.append(this.perfMetricsHistBreakpoints[i + 1]);
            stringBuilder.append(" ms: \t");
            stringBuilder.append(this.perfMetricsHistCounts[i]);
            stringBuilder2.append(stringBuilder.toString());
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str1);
            stringBuilder.append(this.perfMetricsHistBreakpoints[i]);
            stringBuilder.append(" and ");
            stringBuilder.append(this.perfMetricsHistBreakpoints[i + 1]);
            stringBuilder.append(" ms: \t");
            stringBuilder.append(this.perfMetricsHistCounts[i]);
            stringBuilder2.append(stringBuilder.toString());
          } 
          stringBuilder2.append(str2);
          int k = (int)(20 * this.perfMetricsHistCounts[i] / j);
          for (b = 0; b < k; b++)
            stringBuilder2.append("*"); 
          long l = this.longestQueryTimeMs;
          int[] arrayOfInt = this.perfMetricsHistCounts;
          if (l < arrayOfInt[++i])
            break; 
        } 
        String str3 = str2;
        String str4 = str1;
        str2 = str4;
        str1 = str3;
        if (this.perfMetricsHistBreakpoints[18] < this.longestQueryTimeMs) {
          stringBuilder2.append(str4);
          stringBuilder2.append(this.perfMetricsHistBreakpoints[18]);
          stringBuilder2.append(" and ");
          stringBuilder2.append(this.perfMetricsHistBreakpoints[19]);
          stringBuilder2.append(" ms: \t");
          stringBuilder2.append(this.perfMetricsHistCounts[19]);
          str2 = str4;
          str1 = str3;
        } 
      } else {
        str1 = "\t";
        str2 = "\n\tbetween ";
      } 
      if (this.numTablesMetricsHistBreakpoints != null) {
        stringBuilder2.append("\n\n\tTable Join Histogram:\n");
        int j = 0;
        int i;
        for (i = Integer.MIN_VALUE; j < 20; i = k) {
          int[] arrayOfInt = this.numTablesMetricsHistCounts;
          int k = i;
          if (arrayOfInt[j] > i)
            k = arrayOfInt[j]; 
          j++;
        } 
        j = i;
        if (i == 0)
          j = 1; 
        i = 0;
        while (i < 19) {
          if (i == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n\t");
            stringBuilder.append(this.numTablesMetricsHistBreakpoints[i + 1]);
            stringBuilder.append(" tables or less: \t\t");
            stringBuilder.append(this.numTablesMetricsHistCounts[i]);
            stringBuilder2.append(stringBuilder.toString());
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(this.numTablesMetricsHistBreakpoints[i]);
            stringBuilder.append(" and ");
            stringBuilder.append(this.numTablesMetricsHistBreakpoints[i + 1]);
            stringBuilder.append(" tables: \t");
            stringBuilder.append(this.numTablesMetricsHistCounts[i]);
            stringBuilder2.append(stringBuilder.toString());
          } 
          stringBuilder2.append(str1);
          int k = (int)(20 * this.numTablesMetricsHistCounts[i] / j);
          for (byte b = 0; b < k; b++)
            stringBuilder2.append("*"); 
          long l = this.maximumNumberTablesAccessed;
          arrayOfLong = this.numTablesMetricsHistBreakpoints;
          if (l < arrayOfLong[++i])
            break; 
        } 
        if (this.numTablesMetricsHistBreakpoints[18] < this.maximumNumberTablesAccessed) {
          stringBuilder2.append(str2);
          stringBuilder2.append(this.numTablesMetricsHistBreakpoints[18]);
          stringBuilder2.append(" and ");
          stringBuilder2.append(this.numTablesMetricsHistBreakpoints[19]);
          stringBuilder2.append(" tables: ");
          stringBuilder2.append(this.numTablesMetricsHistCounts[19]);
        } 
      } 
      this.log.logInfo(stringBuilder2);
      this.metricsLastReportedMs = System.currentTimeMillis();
    } 
  }
  
  private void rollbackNoChecks() throws SQLException {
    if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) && !this.io.inTransactionOnServer())
      return; 
    execSQL(null, "rollback", -1, null, 1003, 1007, false, this.database, null, false);
  }
  
  private void setSavepoint(MysqlSavepoint paramMysqlSavepoint) throws SQLException {
    synchronized (getConnectionMutex()) {
      if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
        Statement statement;
        checkClosed();
        StringBuilder stringBuilder = new StringBuilder();
        this("SAVEPOINT ");
        stringBuilder.append('`');
        stringBuilder.append(paramMysqlSavepoint.getSavepointName());
        stringBuilder.append('`');
        paramMysqlSavepoint = null;
        try {
          Statement statement1 = getMetadataSafeStatement();
          statement = statement1;
          statement1.executeUpdate(stringBuilder.toString());
          return;
        } finally {
          closeStatement(statement);
        } 
      } 
      throw SQLError.createSQLFeatureNotSupportedException();
    } 
  }
  
  private void setSessionVariables() throws SQLException {
    if (versionMeetsMinimum(4, 0, 0) && getSessionVariables() != null) {
      ArrayList<String> arrayList = new ArrayList();
      Iterator<String> iterator = StringUtils.split(getSessionVariables(), ",", "\"'(", "\"')", "\"'", true).iterator();
      while (iterator.hasNext())
        arrayList.addAll(StringUtils.split(iterator.next(), ";", "\"'(", "\"')", "\"'", true)); 
      if (!arrayList.isEmpty()) {
        Statement statement;
        iterator = null;
        try {
          Statement statement1 = getMetadataSafeStatement();
          statement = statement1;
          StringBuilder stringBuilder = new StringBuilder();
          statement = statement1;
          this("SET ");
          String str = "";
          statement = statement1;
          Iterator<String> iterator1 = arrayList.iterator();
          while (true) {
            statement = statement1;
            if (iterator1.hasNext()) {
              statement = statement1;
              String str1 = iterator1.next();
              statement = statement1;
              if (str1.length() > 0) {
                statement = statement1;
                stringBuilder.append(str);
                statement = statement1;
                if (!str1.startsWith("@")) {
                  statement = statement1;
                  stringBuilder.append("SESSION ");
                } 
                statement = statement1;
                stringBuilder.append(str1);
                str = ",";
              } 
              continue;
            } 
            statement = statement1;
            statement1.executeUpdate(stringBuilder.toString());
            if (statement1 != null)
              statement1.close(); 
            break;
          } 
        } finally {
          if (statement != null)
            statement.close(); 
        } 
      } 
    } 
  }
  
  private void setupServerForTruncationChecks() throws SQLException {
    if (getJdbcCompliantTruncation() && versionMeetsMinimum(5, 0, 2)) {
      boolean bool;
      String str = this.serverVariables.get("sql_mode");
      if (StringUtils.indexOfIgnoreCase(str, "STRICT_TRANS_TABLES") != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      if (str == null || str.length() == 0 || !bool) {
        StringBuilder stringBuilder = new StringBuilder("SET sql_mode='");
        if (str != null && str.length() > 0) {
          stringBuilder.append(str);
          stringBuilder.append(",");
        } 
        stringBuilder.append("STRICT_TRANS_TABLES'");
        execSQL(null, stringBuilder.toString(), -1, null, 1003, 1007, false, this.database, null, false);
        setJdbcCompliantTruncation(false);
        return;
      } 
      if (bool)
        setJdbcCompliantTruncation(false); 
    } 
  }
  
  public void abort(Executor paramExecutor) throws SQLException {
    SecurityManager securityManager = System.getSecurityManager();
    if (securityManager != null)
      securityManager.checkPermission(ABORT_PERM); 
    if (paramExecutor != null) {
      paramExecutor.execute(new Runnable() {
            public final ConnectionImpl this$0;
            
            public void run() {
              try {
                ConnectionImpl.this.abortInternal();
                return;
              } catch (SQLException sQLException) {
                throw new RuntimeException(sQLException);
              } 
            }
          });
      return;
    } 
    throw SQLError.createSQLException("Executor can not be null", "S1009", getExceptionInterceptor());
  }
  
  public void abortInternal() throws SQLException {
    MysqlIO mysqlIO = this.io;
    if (mysqlIO != null) {
      try {
        mysqlIO.forceClose();
        this.io.releaseResources();
      } finally {}
      this.io = null;
    } 
    this.isClosed = true;
  }
  
  public void changeUser(String paramString1, String paramString2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: aload_0
    //   10: invokevirtual checkClosed : ()V
    //   13: aload_1
    //   14: ifnull -> 29
    //   17: aload_1
    //   18: astore_3
    //   19: aload_1
    //   20: ldc_w ''
    //   23: invokevirtual equals : (Ljava/lang/Object;)Z
    //   26: ifeq -> 33
    //   29: ldc_w ''
    //   32: astore_3
    //   33: aload_2
    //   34: astore_1
    //   35: aload_2
    //   36: ifnonnull -> 43
    //   39: ldc_w ''
    //   42: astore_1
    //   43: aload_0
    //   44: iconst_m1
    //   45: putfield sessionMaxRows : I
    //   48: aload_0
    //   49: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   52: aload_3
    //   53: aload_1
    //   54: aload_0
    //   55: getfield database : Ljava/lang/String;
    //   58: invokevirtual changeUser : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   61: aload_0
    //   62: aload_3
    //   63: putfield user : Ljava/lang/String;
    //   66: aload_0
    //   67: aload_1
    //   68: putfield password : Ljava/lang/String;
    //   71: aload_0
    //   72: iconst_4
    //   73: iconst_1
    //   74: iconst_0
    //   75: invokevirtual versionMeetsMinimum : (III)Z
    //   78: ifeq -> 87
    //   81: aload_0
    //   82: iconst_1
    //   83: invokespecial configureClientCharacterSet : (Z)Z
    //   86: pop
    //   87: aload_0
    //   88: invokespecial setSessionVariables : ()V
    //   91: aload_0
    //   92: invokespecial setupServerForTruncationChecks : ()V
    //   95: aload #4
    //   97: monitorexit
    //   98: return
    //   99: astore_1
    //   100: aload_0
    //   101: iconst_5
    //   102: bipush #6
    //   104: bipush #13
    //   106: invokevirtual versionMeetsMinimum : (III)Z
    //   109: ifeq -> 130
    //   112: ldc_w '28000'
    //   115: aload_1
    //   116: invokevirtual getSQLState : ()Ljava/lang/String;
    //   119: invokevirtual equals : (Ljava/lang/Object;)Z
    //   122: ifeq -> 130
    //   125: aload_0
    //   126: aload_1
    //   127: invokespecial cleanup : (Ljava/lang/Throwable;)V
    //   130: aload_1
    //   131: athrow
    //   132: astore_1
    //   133: aload #4
    //   135: monitorexit
    //   136: aload_1
    //   137: athrow
    // Exception table:
    //   from	to	target	type
    //   9	13	132	finally
    //   19	29	132	finally
    //   43	48	132	finally
    //   48	61	99	java/sql/SQLException
    //   48	61	132	finally
    //   61	87	132	finally
    //   87	98	132	finally
    //   100	130	132	finally
    //   130	132	132	finally
    //   133	136	132	finally
  }
  
  public void checkClosed() throws SQLException {
    if (this.isClosed)
      throwConnectionClosedException(); 
  }
  
  @Deprecated
  public void clearHasTriedMaster() {
    this.hasTriedMasterFlag = false;
  }
  
  public void clearWarnings() throws SQLException {}
  
  public PreparedStatement clientPrepareStatement(String paramString) throws SQLException {
    return clientPrepareStatement(paramString, 1003, 1007);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt) throws SQLException {
    PreparedStatement preparedStatement1 = clientPrepareStatement(paramString);
    PreparedStatement preparedStatement = (PreparedStatement)preparedStatement1;
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    preparedStatement.setRetrieveGeneratedKeys(bool);
    return preparedStatement1;
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    return clientPrepareStatement(paramString, paramInt1, paramInt2, true);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return clientPrepareStatement(paramString, paramInt1, paramInt2, true);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) throws SQLException {
    PreparedStatement preparedStatement;
    checkClosed();
    String str = paramString;
    if (paramBoolean) {
      str = paramString;
      if (getProcessEscapeCodesForPrepStmts())
        str = nativeSQL(paramString); 
    } 
    if (getCachePreparedStatements()) {
      PreparedStatement.ParseInfo parseInfo = this.cachedPreparedStatementParams.get(str);
      if (parseInfo == null) {
        preparedStatement = PreparedStatement.getInstance(getMultiHostSafeProxy(), str, this.database);
        this.cachedPreparedStatementParams.put(str, preparedStatement.getParseInfo());
      } else {
        preparedStatement = PreparedStatement.getInstance(getMultiHostSafeProxy(), str, this.database, (PreparedStatement.ParseInfo)preparedStatement);
      } 
    } else {
      preparedStatement = PreparedStatement.getInstance(getMultiHostSafeProxy(), str, this.database);
    } 
    preparedStatement.setResultSetType(paramInt1);
    preparedStatement.setResultSetConcurrency(paramInt2);
    return preparedStatement;
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    boolean bool;
    PreparedStatement preparedStatement = (PreparedStatement)clientPrepareStatement(paramString);
    if (paramArrayOfint != null && paramArrayOfint.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    preparedStatement.setRetrieveGeneratedKeys(bool);
    return preparedStatement;
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    boolean bool;
    PreparedStatement preparedStatement = (PreparedStatement)clientPrepareStatement(paramString);
    if (paramArrayOfString != null && paramArrayOfString.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    preparedStatement.setRetrieveGeneratedKeys(bool);
    return preparedStatement;
  }
  
  public void close() throws SQLException {
    synchronized (getConnectionMutex()) {
      List<Extension> list = this.connectionLifecycleInterceptors;
      if (list != null) {
        IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
            public final ConnectionImpl this$0;
            
            public void forEach(Extension param1Extension) throws SQLException {
              ((ConnectionLifecycleInterceptor)param1Extension).close();
            }
          };
        super(this, list.iterator());
        iterateBlock.doForAll();
      } 
      realClose(true, true, false, null);
      return;
    } 
  }
  
  public void commit() throws SQLException {
    synchronized (getConnectionMutex()) {
      Exception exception;
      checkClosed();
      try {
        List<Extension> list = this.connectionLifecycleInterceptors;
        if (list != null) {
          IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
              public final ConnectionImpl this$0;
              
              public void forEach(Extension param1Extension) throws SQLException {
                if (!((ConnectionLifecycleInterceptor)param1Extension).commit())
                  this.stopIterating = true; 
              }
            };
          super(this, list.iterator());
          iterateBlock.doForAll();
          boolean bool = iterateBlock.fullIteration();
          if (!bool) {
            this.needsPing = getReconnectAtTxEnd();
            return;
          } 
        } 
        if (!this.autoCommit || getRelaxAutoCommit()) {
          if (this.transactionsSupported) {
            if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0)) {
              boolean bool = this.io.inTransactionOnServer();
              if (!bool) {
                this.needsPing = getReconnectAtTxEnd();
                return;
              } 
            } 
            execSQL(null, "commit", -1, null, 1003, 1007, false, this.database, null, false);
          } 
          this.needsPing = getReconnectAtTxEnd();
          return;
        } 
        throw SQLError.createSQLException("Can't call commit when autocommit=true", getExceptionInterceptor());
      } catch (SQLException null) {
        if ("08S01".equals(exception.getSQLState()))
          throw SQLError.createSQLException("Communications link failure during commit(). Transaction resolution unknown.", "08007", getExceptionInterceptor()); 
        throw exception;
      } finally {}
      this.needsPing = getReconnectAtTxEnd();
      throw exception;
    } 
  }
  
  public void createNewIO(boolean paramBoolean) throws SQLException {
    synchronized (getConnectionMutex()) {
      Properties properties = exposeAsProperties(this.props);
      if (!getHighAvailability()) {
        connectOneTryOnly(paramBoolean, properties);
        return;
      } 
      connectWithRetries(paramBoolean, properties);
      return;
    } 
  }
  
  public Statement createStatement() throws SQLException {
    return createStatement(1003, 1007);
  }
  
  public Statement createStatement(int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    StatementImpl statementImpl = new StatementImpl(getMultiHostSafeProxy(), this.database);
    statementImpl.setResultSetType(paramInt1);
    statementImpl.setResultSetConcurrency(paramInt2);
    return statementImpl;
  }
  
  public Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    if (!getPedantic() || paramInt3 == 1)
      return createStatement(paramInt1, paramInt2); 
    throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
  }
  
  public void decachePreparedStatement(ServerPreparedStatement paramServerPreparedStatement) throws SQLException {
    synchronized (getConnectionMutex()) {
      if (getCachePreparedStatements() && paramServerPreparedStatement.isPoolable())
        synchronized (this.serverSideStatementCache) {
          LRUCache<CompoundCacheKey, ServerPreparedStatement> lRUCache = this.serverSideStatementCache;
          CompoundCacheKey compoundCacheKey = new CompoundCacheKey();
          this(paramServerPreparedStatement.currentCatalog, paramServerPreparedStatement.originalSql);
          lRUCache.remove(compoundCacheKey);
        }  
      return;
    } 
  }
  
  public void dumpTestcaseQuery(String paramString) {
    System.err.println(paramString);
  }
  
  public Connection duplicate() throws SQLException {
    return new ConnectionImpl(this.origHostToConnectTo, this.origPortToConnectTo, this.props, this.origDatabaseToConnectTo, this.myURL);
  }
  
  public ResultSetInternalMethods execSQL(StatementImpl paramStatementImpl, String paramString1, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3, boolean paramBoolean, String paramString2, Field[] paramArrayOfField) throws SQLException {
    return execSQL(paramStatementImpl, paramString1, paramInt1, paramBuffer, paramInt2, paramInt3, paramBoolean, paramString2, paramArrayOfField, false);
  }
  
  public ResultSetInternalMethods execSQL(StatementImpl paramStatementImpl, String paramString1, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3, boolean paramBoolean1, String paramString2, Field[] paramArrayOfField, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   4: astore #16
    //   6: aload #16
    //   8: monitorenter
    //   9: aload #4
    //   11: ifnull -> 24
    //   14: aload #4
    //   16: invokevirtual getPosition : ()I
    //   19: istore #11
    //   21: goto -> 27
    //   24: iconst_0
    //   25: istore #11
    //   27: aload_0
    //   28: invokevirtual getGatherPerformanceMetrics : ()Z
    //   31: ifeq -> 42
    //   34: invokestatic currentTimeMillis : ()J
    //   37: lstore #12
    //   39: goto -> 45
    //   42: lconst_0
    //   43: lstore #12
    //   45: aload_0
    //   46: lconst_0
    //   47: putfield lastQueryFinishedTime : J
    //   50: aload_0
    //   51: invokevirtual getHighAvailability : ()Z
    //   54: ifeq -> 108
    //   57: aload_0
    //   58: getfield autoCommit : Z
    //   61: ifne -> 71
    //   64: aload_0
    //   65: invokevirtual getAutoReconnectForPools : ()Z
    //   68: ifeq -> 108
    //   71: aload_0
    //   72: getfield needsPing : Z
    //   75: istore #14
    //   77: iload #14
    //   79: ifeq -> 108
    //   82: iload #10
    //   84: ifne -> 108
    //   87: aload_0
    //   88: iconst_0
    //   89: iconst_0
    //   90: invokevirtual pingInternal : (ZI)V
    //   93: aload_0
    //   94: iconst_0
    //   95: putfield needsPing : Z
    //   98: goto -> 108
    //   101: astore #15
    //   103: aload_0
    //   104: iconst_1
    //   105: invokevirtual createNewIO : (Z)V
    //   108: aload #4
    //   110: ifnonnull -> 189
    //   113: aconst_null
    //   114: astore #15
    //   116: aload_0
    //   117: invokevirtual getUseUnicode : ()Z
    //   120: ifeq -> 129
    //   123: aload_0
    //   124: invokevirtual getEncoding : ()Ljava/lang/String;
    //   127: astore #15
    //   129: aload_0
    //   130: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   133: aload_1
    //   134: aload_2
    //   135: aload #15
    //   137: aconst_null
    //   138: iload_3
    //   139: iload #5
    //   141: iload #6
    //   143: iload #7
    //   145: aload #8
    //   147: aload #9
    //   149: invokevirtual sqlQueryDirect : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/Buffer;IIIZLjava/lang/String;[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   152: astore_1
    //   153: aload_0
    //   154: invokevirtual getMaintainTimeStats : ()Z
    //   157: ifeq -> 167
    //   160: aload_0
    //   161: invokestatic currentTimeMillis : ()J
    //   164: putfield lastQueryFinishedTime : J
    //   167: aload_0
    //   168: invokevirtual getGatherPerformanceMetrics : ()Z
    //   171: ifeq -> 184
    //   174: aload_0
    //   175: invokestatic currentTimeMillis : ()J
    //   178: lload #12
    //   180: lsub
    //   181: invokevirtual registerQueryExecutionTime : (J)V
    //   184: aload #16
    //   186: monitorexit
    //   187: aload_1
    //   188: areturn
    //   189: aload_0
    //   190: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   193: astore #15
    //   195: aload #15
    //   197: aload_1
    //   198: aconst_null
    //   199: aconst_null
    //   200: aload #4
    //   202: iload_3
    //   203: iload #5
    //   205: iload #6
    //   207: iload #7
    //   209: aload #8
    //   211: aload #9
    //   213: invokevirtual sqlQueryDirect : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/Buffer;IIIZLjava/lang/String;[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   216: astore_1
    //   217: aload_0
    //   218: invokevirtual getMaintainTimeStats : ()Z
    //   221: ifeq -> 231
    //   224: aload_0
    //   225: invokestatic currentTimeMillis : ()J
    //   228: putfield lastQueryFinishedTime : J
    //   231: aload_0
    //   232: invokevirtual getGatherPerformanceMetrics : ()Z
    //   235: ifeq -> 248
    //   238: aload_0
    //   239: invokestatic currentTimeMillis : ()J
    //   242: lload #12
    //   244: lsub
    //   245: invokevirtual registerQueryExecutionTime : (J)V
    //   248: aload #16
    //   250: monitorexit
    //   251: aload_1
    //   252: areturn
    //   253: astore_1
    //   254: goto -> 266
    //   257: astore_1
    //   258: goto -> 333
    //   261: astore_1
    //   262: goto -> 461
    //   265: astore_1
    //   266: aload_0
    //   267: invokevirtual getHighAvailability : ()Z
    //   270: ifeq -> 295
    //   273: aload_1
    //   274: instanceof java/io/IOException
    //   277: ifeq -> 287
    //   280: aload_0
    //   281: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   284: invokevirtual forceClose : ()V
    //   287: aload_0
    //   288: iconst_1
    //   289: putfield needsPing : Z
    //   292: goto -> 307
    //   295: aload_1
    //   296: instanceof java/io/IOException
    //   299: ifeq -> 307
    //   302: aload_0
    //   303: aload_1
    //   304: invokespecial cleanup : (Ljava/lang/Throwable;)V
    //   307: ldc_w 'Connection.UnexpectedException'
    //   310: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   313: ldc_w 'S1000'
    //   316: aload_0
    //   317: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   320: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   323: astore_2
    //   324: aload_2
    //   325: aload_1
    //   326: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   329: pop
    //   330: aload_2
    //   331: athrow
    //   332: astore_1
    //   333: aload_0
    //   334: invokevirtual getDumpQueriesOnException : ()Z
    //   337: ifeq -> 406
    //   340: aload_0
    //   341: aload_2
    //   342: aload #4
    //   344: iload #11
    //   346: invokevirtual extractSqlFromPacket : (Ljava/lang/String;Lcom/mysql/jdbc/Buffer;I)Ljava/lang/String;
    //   349: astore #4
    //   351: new java/lang/StringBuilder
    //   354: astore_2
    //   355: aload_2
    //   356: aload #4
    //   358: invokevirtual length : ()I
    //   361: bipush #32
    //   363: iadd
    //   364: invokespecial <init> : (I)V
    //   367: aload_2
    //   368: ldc_w '\\n\\nQuery being executed when exception was thrown:\\n'
    //   371: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: aload_2
    //   376: aload #4
    //   378: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   381: pop
    //   382: aload_2
    //   383: ldc_w '\\n\\n'
    //   386: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   389: pop
    //   390: aload_1
    //   391: aload_2
    //   392: invokevirtual toString : ()Ljava/lang/String;
    //   395: aload_0
    //   396: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   399: invokestatic appendMessageToException : (Ljava/sql/SQLException;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   402: astore_1
    //   403: goto -> 406
    //   406: aload_0
    //   407: invokevirtual getHighAvailability : ()Z
    //   410: ifeq -> 441
    //   413: ldc_w '08S01'
    //   416: aload_1
    //   417: invokevirtual getSQLState : ()Ljava/lang/String;
    //   420: invokevirtual equals : (Ljava/lang/Object;)Z
    //   423: ifeq -> 433
    //   426: aload_0
    //   427: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   430: invokevirtual forceClose : ()V
    //   433: aload_0
    //   434: iconst_1
    //   435: putfield needsPing : Z
    //   438: goto -> 459
    //   441: ldc_w '08S01'
    //   444: aload_1
    //   445: invokevirtual getSQLState : ()Ljava/lang/String;
    //   448: invokevirtual equals : (Ljava/lang/Object;)Z
    //   451: ifeq -> 459
    //   454: aload_0
    //   455: aload_1
    //   456: invokespecial cleanup : (Ljava/lang/Throwable;)V
    //   459: aload_1
    //   460: athrow
    //   461: aload_0
    //   462: invokevirtual getMaintainTimeStats : ()Z
    //   465: ifeq -> 475
    //   468: aload_0
    //   469: invokestatic currentTimeMillis : ()J
    //   472: putfield lastQueryFinishedTime : J
    //   475: aload_0
    //   476: invokevirtual getGatherPerformanceMetrics : ()Z
    //   479: ifeq -> 492
    //   482: aload_0
    //   483: invokestatic currentTimeMillis : ()J
    //   486: lload #12
    //   488: lsub
    //   489: invokevirtual registerQueryExecutionTime : (J)V
    //   492: aload_1
    //   493: athrow
    //   494: astore_1
    //   495: aload #16
    //   497: monitorexit
    //   498: aload_1
    //   499: athrow
    // Exception table:
    //   from	to	target	type
    //   14	21	494	finally
    //   27	39	494	finally
    //   45	71	494	finally
    //   71	77	494	finally
    //   87	98	101	java/lang/Exception
    //   87	98	494	finally
    //   103	108	494	finally
    //   116	129	332	java/sql/SQLException
    //   116	129	265	java/lang/Exception
    //   116	129	261	finally
    //   129	153	332	java/sql/SQLException
    //   129	153	265	java/lang/Exception
    //   129	153	261	finally
    //   153	167	494	finally
    //   167	184	494	finally
    //   184	187	494	finally
    //   189	195	332	java/sql/SQLException
    //   189	195	265	java/lang/Exception
    //   189	195	261	finally
    //   195	217	257	java/sql/SQLException
    //   195	217	253	java/lang/Exception
    //   195	217	261	finally
    //   217	231	494	finally
    //   231	248	494	finally
    //   248	251	494	finally
    //   266	287	261	finally
    //   287	292	261	finally
    //   295	307	261	finally
    //   307	332	261	finally
    //   333	403	261	finally
    //   406	433	261	finally
    //   433	438	261	finally
    //   441	459	261	finally
    //   459	461	261	finally
    //   461	475	494	finally
    //   475	492	494	finally
    //   492	494	494	finally
    //   495	498	494	finally
  }
  
  public String extractSqlFromPacket(String paramString, Buffer paramBuffer, int paramInt) throws SQLException {
    String str1;
    String str2;
    boolean bool = false;
    if (paramString != null) {
      str2 = paramString;
      if (paramString.length() > getMaxQuerySizeToLog()) {
        StringBuilder stringBuilder = new StringBuilder(paramString.substring(0, getMaxQuerySizeToLog()));
        stringBuilder.append(Messages.getString("MysqlIO.25"));
        str2 = stringBuilder.toString();
      } 
    } else {
      str2 = null;
    } 
    paramString = str2;
    if (str2 == null) {
      int i = paramInt;
      if (paramInt > getMaxQuerySizeToLog()) {
        i = getMaxQuerySizeToLog();
        bool = true;
      } 
      String str = StringUtils.toString(paramBuffer.getByteBuffer(), 5, i - 5);
      paramString = str;
      if (bool) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(Messages.getString("MysqlIO.25"));
        str1 = stringBuilder.toString();
      } 
    } 
    return str1;
  }
  
  public StringBuilder generateConnectionCommentBlock(StringBuilder paramStringBuilder) {
    paramStringBuilder.append("/* conn id ");
    paramStringBuilder.append(getId());
    paramStringBuilder.append(" clock: ");
    paramStringBuilder.append(System.currentTimeMillis());
    paramStringBuilder.append(" */ ");
    return paramStringBuilder;
  }
  
  public MySQLConnection getActiveMySQLConnection() {
    return this;
  }
  
  public int getActiveStatementCount() {
    return this.openStatements.size();
  }
  
  public boolean getAutoCommit() throws SQLException {
    synchronized (getConnectionMutex()) {
      return this.autoCommit;
    } 
  }
  
  public int getAutoIncrementIncrement() {
    return this.autoIncrementIncrement;
  }
  
  public CachedResultSetMetaData getCachedMetaData(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield resultSetMetadataCache : Lcom/mysql/jdbc/util/LRUCache;
    //   4: astore_2
    //   5: aload_2
    //   6: ifnull -> 32
    //   9: aload_2
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield resultSetMetadataCache : Lcom/mysql/jdbc/util/LRUCache;
    //   15: aload_1
    //   16: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast com/mysql/jdbc/CachedResultSetMetaData
    //   22: astore_1
    //   23: aload_2
    //   24: monitorexit
    //   25: aload_1
    //   26: areturn
    //   27: astore_1
    //   28: aload_2
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    //   32: aconst_null
    //   33: areturn
    // Exception table:
    //   from	to	target	type
    //   11	25	27	finally
    //   28	30	27	finally
  }
  
  public Calendar getCalendarInstanceForSessionOrNew() {
    return getDynamicCalendars() ? Calendar.getInstance() : getSessionLockedCalendar();
  }
  
  public Timer getCancelTimer() {
    synchronized (getConnectionMutex()) {
      if (this.cancelTimer == null) {
        Timer timer = new Timer();
        this("MySQL Statement Cancellation Timer", true);
        this.cancelTimer = timer;
      } 
      return this.cancelTimer;
    } 
  }
  
  public String getCatalog() throws SQLException {
    synchronized (getConnectionMutex()) {
      return this.database;
    } 
  }
  
  public String getCharacterSetMetadata() {
    synchronized (getConnectionMutex()) {
      return this.characterSetMetadata;
    } 
  }
  
  public SingleByteCharsetConverter getCharsetConverter(String paramString) throws SQLException {
    String str = null;
    if (paramString == null)
      return null; 
    if (this.usePlatformCharsetConverters)
      return null; 
    synchronized (this.charsetConverterMap) {
      object1 = this.charsetConverterMap.get(paramString);
      Object object2 = CHARSET_CONVERTER_NOT_AVAILABLE_MARKER;
      if (object1 == object2)
        return null; 
      SingleByteCharsetConverter singleByteCharsetConverter = (SingleByteCharsetConverter)object1;
      object1 = singleByteCharsetConverter;
      if (singleByteCharsetConverter == null)
        try {
          object1 = SingleByteCharsetConverter.getInstance(paramString, this);
          if (object1 == null) {
            this.charsetConverterMap.put(paramString, object2);
          } else {
            this.charsetConverterMap.put(paramString, object1);
          } 
        } catch (UnsupportedEncodingException object1) {
          this.charsetConverterMap.put(paramString, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
          paramString = str;
        }  
      return (SingleByteCharsetConverter)object1;
    } 
  }
  
  @Deprecated
  public String getCharsetNameForIndex(int paramInt) throws SQLException {
    return getEncodingForIndex(paramInt);
  }
  
  public Object getConnectionMutex() {
    MySQLConnection mySQLConnection;
    InvocationHandler invocationHandler = this.realProxy;
    if (invocationHandler == null)
      mySQLConnection = getProxy(); 
    return mySQLConnection;
  }
  
  public TimeZone getDefaultTimeZone() {
    TimeZone timeZone;
    if (getCacheDefaultTimezone()) {
      timeZone = this.defaultTimeZone;
    } else {
      timeZone = TimeUtil.getDefaultTimeZone(false);
    } 
    return timeZone;
  }
  
  public String getEncodingForIndex(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getUseOldUTF8Behavior : ()Z
    //   4: ifeq -> 12
    //   7: aload_0
    //   8: invokevirtual getEncoding : ()Ljava/lang/String;
    //   11: areturn
    //   12: iload_1
    //   13: iconst_m1
    //   14: if_icmpeq -> 157
    //   17: aload_0
    //   18: getfield indexToCustomMysqlCharset : Ljava/util/Map;
    //   21: astore_2
    //   22: aload_2
    //   23: ifnull -> 56
    //   26: aload_2
    //   27: iload_1
    //   28: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   31: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   36: checkcast java/lang/String
    //   39: astore_2
    //   40: aload_2
    //   41: ifnull -> 56
    //   44: aload_2
    //   45: aload_0
    //   46: invokevirtual getEncoding : ()Ljava/lang/String;
    //   49: invokestatic getJavaEncodingForMysqlCharset : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   52: astore_3
    //   53: goto -> 58
    //   56: aconst_null
    //   57: astore_3
    //   58: aload_3
    //   59: astore_2
    //   60: aload_3
    //   61: ifnonnull -> 76
    //   64: iload_1
    //   65: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   68: aload_0
    //   69: invokevirtual getEncoding : ()Ljava/lang/String;
    //   72: invokestatic getJavaEncodingForCollationIndex : (Ljava/lang/Integer;Ljava/lang/String;)Ljava/lang/String;
    //   75: astore_2
    //   76: aload_2
    //   77: astore_3
    //   78: aload_2
    //   79: ifnonnull -> 162
    //   82: aload_0
    //   83: invokevirtual getEncoding : ()Ljava/lang/String;
    //   86: astore_3
    //   87: goto -> 162
    //   90: astore_2
    //   91: aload_2
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: ldc_w 'S1009'
    //   98: aconst_null
    //   99: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   102: astore_3
    //   103: aload_3
    //   104: aload_2
    //   105: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   108: pop
    //   109: aload_3
    //   110: athrow
    //   111: astore_2
    //   112: new java/lang/StringBuilder
    //   115: dup
    //   116: invokespecial <init> : ()V
    //   119: astore_2
    //   120: aload_2
    //   121: ldc_w 'Unknown character set index for field ''
    //   124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: aload_2
    //   129: iload_1
    //   130: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload_2
    //   135: ldc_w '' received from server.'
    //   138: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_2
    //   143: invokevirtual toString : ()Ljava/lang/String;
    //   146: ldc_w 'S1000'
    //   149: aload_0
    //   150: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   153: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   156: athrow
    //   157: aload_0
    //   158: invokevirtual getEncoding : ()Ljava/lang/String;
    //   161: astore_3
    //   162: aload_3
    //   163: areturn
    // Exception table:
    //   from	to	target	type
    //   17	22	111	java/lang/ArrayIndexOutOfBoundsException
    //   17	22	90	java/lang/RuntimeException
    //   26	40	111	java/lang/ArrayIndexOutOfBoundsException
    //   26	40	90	java/lang/RuntimeException
    //   44	53	111	java/lang/ArrayIndexOutOfBoundsException
    //   44	53	90	java/lang/RuntimeException
    //   64	76	111	java/lang/ArrayIndexOutOfBoundsException
    //   64	76	90	java/lang/RuntimeException
  }
  
  public String getErrorMessageEncoding() {
    return this.errorMessageEncoding;
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  public int getHoldability() throws SQLException {
    return 2;
  }
  
  public String getHost() {
    return this.host;
  }
  
  public String getHostPortPair() {
    String str = this.hostPortPair;
    if (str == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.host);
      stringBuilder.append(":");
      stringBuilder.append(this.port);
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  public MysqlIO getIO() throws SQLException {
    MysqlIO mysqlIO = this.io;
    if (mysqlIO != null && !this.isClosed)
      return mysqlIO; 
    throw SQLError.createSQLException("Operation not allowed on closed connection", "08003", getExceptionInterceptor());
  }
  
  public long getId() {
    return this.connectionId;
  }
  
  public long getIdleFor() {
    synchronized (getConnectionMutex()) {
      if (this.lastQueryFinishedTime == 0L)
        return 0L; 
      long l2 = System.currentTimeMillis();
      long l1 = this.lastQueryFinishedTime;
      return l2 - l1;
    } 
  }
  
  @Deprecated
  public MySQLConnection getLoadBalanceSafeProxy() {
    return getMultiHostSafeProxy();
  }
  
  public Log getLog() throws SQLException {
    return this.log;
  }
  
  public int getMaxBytesPerChar(Integer paramInteger, String paramString) throws SQLException {
    try {
      Integer integer;
      boolean bool;
      String str1;
      Map map3;
      Map<Integer, String> map = this.indexToCustomMysqlCharset;
      if (map != null) {
        map3 = (Map)map.get(paramInteger);
      } else {
        map3 = null;
      } 
      map = map3;
      if (map3 == null)
        str1 = CharsetMapping.getMysqlCharsetNameForCollationIndex(paramInteger); 
      String str2 = str1;
      if (str1 == null)
        str2 = CharsetMapping.getMysqlCharsetForJavaEncoding(paramString, this); 
      Map<String, Integer> map1 = this.mysqlCharsetToCustomMblen;
      if (map1 != null) {
        Integer integer1 = map1.get(str2);
      } else {
        map1 = null;
      } 
      Map<String, Integer> map2 = map1;
      if (map1 == null)
        integer = Integer.valueOf(CharsetMapping.getMblen(str2)); 
      if (integer != null) {
        bool = integer.intValue();
      } else {
        bool = true;
      } 
      return bool;
    } catch (SQLException sQLException) {
      throw sQLException;
    } catch (RuntimeException runtimeException) {
      SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
      sQLException.initCause(runtimeException);
      throw sQLException;
    } 
  }
  
  public int getMaxBytesPerChar(String paramString) throws SQLException {
    return getMaxBytesPerChar(null, paramString);
  }
  
  public DatabaseMetaData getMetaData() throws SQLException {
    return getMetaData(true, true);
  }
  
  public Statement getMetadataSafeStatement() throws SQLException {
    return getMetadataSafeStatement(0);
  }
  
  public Statement getMetadataSafeStatement(int paramInt) throws SQLException {
    Statement statement = createStatement();
    int i = paramInt;
    if (paramInt == -1)
      i = 0; 
    statement.setMaxRows(i);
    statement.setEscapeProcessing(false);
    if (statement.getFetchSize() != 0)
      statement.setFetchSize(0); 
    return statement;
  }
  
  public MySQLConnection getMultiHostSafeProxy() {
    return getProxy();
  }
  
  public int getNetBufferLength() {
    return this.netBufferLength;
  }
  
  public int getNetworkTimeout() throws SQLException {
    synchronized (getConnectionMutex()) {
      checkClosed();
      return getSocketTimeout();
    } 
  }
  
  public ProfilerEventHandler getProfilerEventHandlerInstance() {
    return this.eventSink;
  }
  
  public Properties getProperties() {
    return this.props;
  }
  
  public boolean getRequiresEscapingEncoder() {
    return this.requiresEscapingEncoder;
  }
  
  public String getSchema() throws SQLException {
    synchronized (getConnectionMutex()) {
      checkClosed();
      return null;
    } 
  }
  
  @Deprecated
  public String getServerCharacterEncoding() {
    return getServerCharset();
  }
  
  public String getServerCharset() {
    if (this.io.versionMeetsMinimum(4, 1, 0)) {
      String str1 = null;
      Map<Integer, String> map = this.indexToCustomMysqlCharset;
      if (map != null)
        str1 = map.get(Integer.valueOf(this.io.serverCharsetIndex)); 
      String str2 = str1;
      if (str1 == null)
        str2 = CharsetMapping.getMysqlCharsetNameForCollationIndex(Integer.valueOf(this.io.serverCharsetIndex)); 
      if (str2 == null)
        str2 = this.serverVariables.get("character_set_server"); 
      return str2;
    } 
    return this.serverVariables.get("character_set");
  }
  
  public int getServerMajorVersion() {
    return this.io.getServerMajorVersion();
  }
  
  public int getServerMinorVersion() {
    return this.io.getServerMinorVersion();
  }
  
  public int getServerSubMinorVersion() {
    return this.io.getServerSubMinorVersion();
  }
  
  public TimeZone getServerTimezoneTZ() {
    return this.serverTimezoneTZ;
  }
  
  public String getServerVariable(String paramString) {
    Map<String, String> map = this.serverVariables;
    return (map != null) ? map.get(paramString) : null;
  }
  
  public String getServerVersion() {
    return this.io.getServerVersion();
  }
  
  public Calendar getSessionLockedCalendar() {
    return this.sessionCalendar;
  }
  
  public int getSessionMaxRows() {
    synchronized (getConnectionMutex()) {
      return this.sessionMaxRows;
    } 
  }
  
  public String getStatementComment() {
    return this.statementComment;
  }
  
  public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
    return this.statementInterceptors;
  }
  
  public int getTransactionIsolation() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   4: astore #8
    //   6: aload #8
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield hasIsolationLevels : Z
    //   13: ifeq -> 355
    //   16: aload_0
    //   17: invokevirtual getUseLocalSessionState : ()Z
    //   20: istore_2
    //   21: iload_2
    //   22: ifne -> 355
    //   25: aconst_null
    //   26: astore #6
    //   28: aconst_null
    //   29: astore #5
    //   31: aload_0
    //   32: aload_0
    //   33: getfield sessionMaxRows : I
    //   36: invokevirtual getMetadataSafeStatement : (I)Ljava/sql/Statement;
    //   39: astore #7
    //   41: aload #5
    //   43: astore_3
    //   44: aload_0
    //   45: bipush #8
    //   47: iconst_0
    //   48: iconst_3
    //   49: invokevirtual versionMeetsMinimum : (III)Z
    //   52: ifne -> 95
    //   55: aload #5
    //   57: astore_3
    //   58: aload_0
    //   59: iconst_5
    //   60: bipush #7
    //   62: bipush #20
    //   64: invokevirtual versionMeetsMinimum : (III)Z
    //   67: ifeq -> 87
    //   70: aload #5
    //   72: astore_3
    //   73: aload_0
    //   74: bipush #8
    //   76: iconst_0
    //   77: iconst_0
    //   78: invokevirtual versionMeetsMinimum : (III)Z
    //   81: ifne -> 87
    //   84: goto -> 95
    //   87: ldc_w 'SELECT @@session.tx_isolation'
    //   90: astore #4
    //   92: goto -> 100
    //   95: ldc_w 'SELECT @@session.transaction_isolation'
    //   98: astore #4
    //   100: aload #5
    //   102: astore_3
    //   103: aload #7
    //   105: aload #4
    //   107: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   112: astore #4
    //   114: aload #4
    //   116: astore_3
    //   117: aload #4
    //   119: invokeinterface next : ()Z
    //   124: ifeq -> 288
    //   127: aload #4
    //   129: astore_3
    //   130: aload #4
    //   132: iconst_1
    //   133: invokeinterface getString : (I)Ljava/lang/String;
    //   138: astore #5
    //   140: aload #5
    //   142: ifnull -> 218
    //   145: aload #4
    //   147: astore_3
    //   148: getstatic com/mysql/jdbc/ConnectionImpl.mapTransIsolationNameToValue : Ljava/util/Map;
    //   151: aload #5
    //   153: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   158: checkcast java/lang/Integer
    //   161: astore #6
    //   163: aload #6
    //   165: ifnull -> 218
    //   168: aload #4
    //   170: astore_3
    //   171: aload #6
    //   173: invokevirtual intValue : ()I
    //   176: istore_1
    //   177: aload #4
    //   179: astore_3
    //   180: aload_0
    //   181: iload_1
    //   182: putfield isolationLevel : I
    //   185: aload #4
    //   187: ifnull -> 201
    //   190: aload #4
    //   192: invokeinterface close : ()V
    //   197: goto -> 201
    //   200: astore_3
    //   201: aload #7
    //   203: ifnull -> 213
    //   206: aload #7
    //   208: invokeinterface close : ()V
    //   213: aload #8
    //   215: monitorexit
    //   216: iload_1
    //   217: ireturn
    //   218: aload #4
    //   220: astore_3
    //   221: new java/lang/StringBuilder
    //   224: astore #6
    //   226: aload #4
    //   228: astore_3
    //   229: aload #6
    //   231: invokespecial <init> : ()V
    //   234: aload #4
    //   236: astore_3
    //   237: aload #6
    //   239: ldc_w 'Could not map transaction isolation ''
    //   242: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: pop
    //   246: aload #4
    //   248: astore_3
    //   249: aload #6
    //   251: aload #5
    //   253: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   256: pop
    //   257: aload #4
    //   259: astore_3
    //   260: aload #6
    //   262: ldc_w ' to a valid JDBC level.'
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload #4
    //   271: astore_3
    //   272: aload #6
    //   274: invokevirtual toString : ()Ljava/lang/String;
    //   277: ldc_w 'S1000'
    //   280: aload_0
    //   281: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   284: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   287: athrow
    //   288: aload #4
    //   290: astore_3
    //   291: ldc_w 'Could not retrieve transaction isolation level from server'
    //   294: ldc_w 'S1000'
    //   297: aload_0
    //   298: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   301: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   304: athrow
    //   305: astore #5
    //   307: aload_3
    //   308: astore #6
    //   310: aload #7
    //   312: astore #4
    //   314: aload #5
    //   316: astore_3
    //   317: goto -> 324
    //   320: astore_3
    //   321: aconst_null
    //   322: astore #4
    //   324: aload #6
    //   326: ifnull -> 341
    //   329: aload #6
    //   331: invokeinterface close : ()V
    //   336: goto -> 341
    //   339: astore #5
    //   341: aload #4
    //   343: ifnull -> 353
    //   346: aload #4
    //   348: invokeinterface close : ()V
    //   353: aload_3
    //   354: athrow
    //   355: aload_0
    //   356: getfield isolationLevel : I
    //   359: istore_1
    //   360: aload #8
    //   362: monitorexit
    //   363: iload_1
    //   364: ireturn
    //   365: astore_3
    //   366: aload #8
    //   368: monitorexit
    //   369: aload_3
    //   370: athrow
    //   371: astore_3
    //   372: goto -> 213
    //   375: astore #4
    //   377: goto -> 353
    // Exception table:
    //   from	to	target	type
    //   9	21	365	finally
    //   31	41	320	finally
    //   44	55	305	finally
    //   58	70	305	finally
    //   73	84	305	finally
    //   103	114	305	finally
    //   117	127	305	finally
    //   130	140	305	finally
    //   148	163	305	finally
    //   171	177	305	finally
    //   180	185	305	finally
    //   190	197	200	java/lang/Exception
    //   190	197	365	finally
    //   206	213	371	java/lang/Exception
    //   206	213	365	finally
    //   213	216	365	finally
    //   221	226	305	finally
    //   229	234	305	finally
    //   237	246	305	finally
    //   249	257	305	finally
    //   260	269	305	finally
    //   272	288	305	finally
    //   291	305	305	finally
    //   329	336	339	java/lang/Exception
    //   329	336	365	finally
    //   346	353	375	java/lang/Exception
    //   346	353	365	finally
    //   353	355	365	finally
    //   355	363	365	finally
    //   366	369	365	finally
  }
  
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    synchronized (getConnectionMutex()) {
      if (this.typeMap == null) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        this();
        this.typeMap = (Map)hashMap;
      } 
      return this.typeMap;
    } 
  }
  
  public String getURL() {
    return this.myURL;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public Calendar getUtcCalendar() {
    return this.utcCalendar;
  }
  
  public SQLWarning getWarnings() throws SQLException {
    return null;
  }
  
  public boolean hasSameProperties(Connection paramConnection) {
    return this.props.equals(paramConnection.getProperties());
  }
  
  @Deprecated
  public boolean hasTriedMaster() {
    return this.hasTriedMasterFlag;
  }
  
  public void incrementNumberOfPreparedExecutes() {
    if (getGatherPerformanceMetrics()) {
      this.numberOfPreparedExecutes++;
      this.numberOfQueriesIssued++;
    } 
  }
  
  public void incrementNumberOfPrepares() {
    if (getGatherPerformanceMetrics())
      this.numberOfPrepares++; 
  }
  
  public void incrementNumberOfResultSetsCreated() {
    if (getGatherPerformanceMetrics())
      this.numberOfResultSetsCreated++; 
  }
  
  public void initializeExtension(Extension paramExtension) throws SQLException {
    paramExtension.init(this, this.props);
  }
  
  public void initializeResultsMetadataFromCache(String paramString, CachedResultSetMetaData paramCachedResultSetMetaData, ResultSetInternalMethods paramResultSetInternalMethods) throws SQLException {
    if (paramCachedResultSetMetaData == null) {
      paramCachedResultSetMetaData = new CachedResultSetMetaData();
      paramResultSetInternalMethods.buildIndexMapping();
      paramResultSetInternalMethods.initializeWithMetadata();
      if (paramResultSetInternalMethods instanceof UpdatableResultSet)
        ((UpdatableResultSet)paramResultSetInternalMethods).checkUpdatability(); 
      paramResultSetInternalMethods.populateCachedMetaData(paramCachedResultSetMetaData);
      this.resultSetMetadataCache.put(paramString, paramCachedResultSetMetaData);
    } else {
      paramResultSetInternalMethods.initializeFromCachedMetaData(paramCachedResultSetMetaData);
      paramResultSetInternalMethods.initializeWithMetadata();
      if (paramResultSetInternalMethods instanceof UpdatableResultSet)
        ((UpdatableResultSet)paramResultSetInternalMethods).checkUpdatability(); 
    } 
  }
  
  public void initializeSafeStatementInterceptors() throws SQLException {
    byte b = 0;
    this.isClosed = false;
    List<Extension> list = Util.loadExtensions(this, this.props, getStatementInterceptors(), "MysqlIo.BadStatementInterceptor", getExceptionInterceptor());
    this.statementInterceptors = new ArrayList<StatementInterceptorV2>(list.size());
    while (b < list.size()) {
      Extension extension = list.get(b);
      if (extension instanceof StatementInterceptor) {
        if (ReflectiveStatementInterceptorAdapter.getV2PostProcessMethod(extension.getClass()) != null) {
          this.statementInterceptors.add(new NoSubInterceptorWrapper(new ReflectiveStatementInterceptorAdapter((StatementInterceptor)extension)));
        } else {
          this.statementInterceptors.add(new NoSubInterceptorWrapper(new V1toV2StatementInterceptorAdapter((StatementInterceptor)extension)));
        } 
      } else {
        this.statementInterceptors.add(new NoSubInterceptorWrapper((StatementInterceptorV2)extension));
      } 
      b++;
    } 
  }
  
  public boolean isAbonormallyLongQuery(long paramLong) {
    synchronized (getConnectionMutex()) {
      long l = this.queryTimeCount;
      boolean bool = false;
      if (l < 15L)
        return false; 
      double d2 = this.queryTimeSumSquares;
      double d1 = this.queryTimeSum;
      d1 = Math.sqrt((d2 - d1 * d1 / l) / (l - 1L));
      if (paramLong > this.queryTimeMean + d1 * 5.0D)
        bool = true; 
      return bool;
    } 
  }
  
  public boolean isClientTzUTC() {
    return this.isClientTzUTC;
  }
  
  public boolean isClosed() {
    return this.isClosed;
  }
  
  public boolean isCursorFetchEnabled() throws SQLException {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (versionMeetsMinimum(5, 0, 2)) {
      bool1 = bool2;
      if (getUseCursorFetch())
        bool1 = true; 
    } 
    return bool1;
  }
  
  public boolean isInGlobalTx() {
    return this.isInGlobalTx;
  }
  
  public boolean isMasterConnection() {
    return false;
  }
  
  public boolean isNoBackslashEscapesSet() {
    return this.noBackslashEscapes;
  }
  
  public boolean isProxySet() {
    boolean bool;
    if (this.proxy != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isQueryCacheEnabled() {
    boolean bool;
    if ("ON".equalsIgnoreCase(this.serverVariables.get("query_cache_type")) && !"0".equalsIgnoreCase(this.serverVariables.get("query_cache_size"))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isReadInfoMsgEnabled() {
    return this.readInfoMsg;
  }
  
  public boolean isReadOnly() throws SQLException {
    return isReadOnly(true);
  }
  
  public boolean isReadOnly(boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: iload_1
    //   1: ifeq -> 408
    //   4: aload_0
    //   5: getfield isClosed : Z
    //   8: ifne -> 408
    //   11: aload_0
    //   12: iconst_5
    //   13: bipush #6
    //   15: iconst_5
    //   16: invokevirtual versionMeetsMinimum : (III)Z
    //   19: ifeq -> 408
    //   22: aload_0
    //   23: invokevirtual getUseLocalSessionState : ()Z
    //   26: ifne -> 408
    //   29: aload_0
    //   30: invokevirtual getReadOnlyPropagatesToServer : ()Z
    //   33: ifeq -> 408
    //   36: aconst_null
    //   37: astore_3
    //   38: aconst_null
    //   39: astore #8
    //   41: aconst_null
    //   42: astore #5
    //   44: aconst_null
    //   45: astore #9
    //   47: aload_0
    //   48: aload_0
    //   49: getfield sessionMaxRows : I
    //   52: invokevirtual getMetadataSafeStatement : (I)Ljava/sql/Statement;
    //   55: astore #4
    //   57: iconst_0
    //   58: istore_1
    //   59: aload #9
    //   61: astore #5
    //   63: aload #4
    //   65: astore #6
    //   67: aload #8
    //   69: astore_3
    //   70: aload_0
    //   71: bipush #8
    //   73: iconst_0
    //   74: iconst_3
    //   75: invokevirtual versionMeetsMinimum : (III)Z
    //   78: ifne -> 137
    //   81: aload #9
    //   83: astore #5
    //   85: aload #4
    //   87: astore #6
    //   89: aload #8
    //   91: astore_3
    //   92: aload_0
    //   93: iconst_5
    //   94: bipush #7
    //   96: bipush #20
    //   98: invokevirtual versionMeetsMinimum : (III)Z
    //   101: ifeq -> 129
    //   104: aload #9
    //   106: astore #5
    //   108: aload #4
    //   110: astore #6
    //   112: aload #8
    //   114: astore_3
    //   115: aload_0
    //   116: bipush #8
    //   118: iconst_0
    //   119: iconst_0
    //   120: invokevirtual versionMeetsMinimum : (III)Z
    //   123: ifne -> 129
    //   126: goto -> 137
    //   129: ldc_w 'select @@session.tx_read_only'
    //   132: astore #7
    //   134: goto -> 142
    //   137: ldc_w 'select @@session.transaction_read_only'
    //   140: astore #7
    //   142: aload #9
    //   144: astore #5
    //   146: aload #4
    //   148: astore #6
    //   150: aload #8
    //   152: astore_3
    //   153: aload #4
    //   155: aload #7
    //   157: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
    //   162: astore #7
    //   164: aload #7
    //   166: astore #5
    //   168: aload #4
    //   170: astore #9
    //   172: aload #7
    //   174: astore #8
    //   176: aload #4
    //   178: astore #6
    //   180: aload #7
    //   182: astore_3
    //   183: aload #7
    //   185: invokeinterface next : ()Z
    //   190: ifeq -> 315
    //   193: aload #7
    //   195: astore #5
    //   197: aload #4
    //   199: astore #6
    //   201: aload #7
    //   203: astore_3
    //   204: aload #7
    //   206: iconst_1
    //   207: invokeinterface getInt : (I)I
    //   212: istore_2
    //   213: iload_2
    //   214: ifeq -> 219
    //   217: iconst_1
    //   218: istore_1
    //   219: aload #7
    //   221: ifnull -> 235
    //   224: aload #7
    //   226: invokeinterface close : ()V
    //   231: goto -> 235
    //   234: astore_3
    //   235: aload #4
    //   237: ifnull -> 247
    //   240: aload #4
    //   242: invokeinterface close : ()V
    //   247: iload_1
    //   248: ireturn
    //   249: astore #7
    //   251: goto -> 273
    //   254: astore_3
    //   255: aconst_null
    //   256: astore #6
    //   258: aload #5
    //   260: astore #4
    //   262: goto -> 377
    //   265: astore #7
    //   267: aconst_null
    //   268: astore #4
    //   270: aload_3
    //   271: astore #5
    //   273: aload #4
    //   275: astore #6
    //   277: aload #5
    //   279: astore_3
    //   280: aload #7
    //   282: invokevirtual getErrorCode : ()I
    //   285: sipush #1820
    //   288: if_icmpne -> 346
    //   291: aload #4
    //   293: astore #6
    //   295: aload #5
    //   297: astore_3
    //   298: aload_0
    //   299: invokevirtual getDisconnectOnExpiredPasswords : ()Z
    //   302: istore_1
    //   303: iload_1
    //   304: ifne -> 346
    //   307: aload #5
    //   309: astore #8
    //   311: aload #4
    //   313: astore #9
    //   315: aload #8
    //   317: ifnull -> 331
    //   320: aload #8
    //   322: invokeinterface close : ()V
    //   327: goto -> 331
    //   330: astore_3
    //   331: aload #9
    //   333: ifnull -> 408
    //   336: aload #9
    //   338: invokeinterface close : ()V
    //   343: goto -> 408
    //   346: aload #4
    //   348: astore #6
    //   350: aload #5
    //   352: astore_3
    //   353: ldc_w 'Could not retrieve transaction read-only status from server'
    //   356: ldc_w 'S1000'
    //   359: aload #7
    //   361: aload_0
    //   362: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   365: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   368: athrow
    //   369: astore #5
    //   371: aload_3
    //   372: astore #4
    //   374: aload #5
    //   376: astore_3
    //   377: aload #4
    //   379: ifnull -> 394
    //   382: aload #4
    //   384: invokeinterface close : ()V
    //   389: goto -> 394
    //   392: astore #4
    //   394: aload #6
    //   396: ifnull -> 406
    //   399: aload #6
    //   401: invokeinterface close : ()V
    //   406: aload_3
    //   407: athrow
    //   408: aload_0
    //   409: getfield readOnly : Z
    //   412: ireturn
    //   413: astore_3
    //   414: goto -> 247
    //   417: astore_3
    //   418: goto -> 408
    //   421: astore #4
    //   423: goto -> 406
    // Exception table:
    //   from	to	target	type
    //   47	57	265	java/sql/SQLException
    //   47	57	254	finally
    //   70	81	249	java/sql/SQLException
    //   70	81	369	finally
    //   92	104	249	java/sql/SQLException
    //   92	104	369	finally
    //   115	126	249	java/sql/SQLException
    //   115	126	369	finally
    //   153	164	249	java/sql/SQLException
    //   153	164	369	finally
    //   183	193	249	java/sql/SQLException
    //   183	193	369	finally
    //   204	213	249	java/sql/SQLException
    //   204	213	369	finally
    //   224	231	234	java/lang/Exception
    //   240	247	413	java/lang/Exception
    //   280	291	369	finally
    //   298	303	369	finally
    //   320	327	330	java/lang/Exception
    //   336	343	417	java/lang/Exception
    //   353	369	369	finally
    //   382	389	392	java/lang/Exception
    //   399	406	421	java/lang/Exception
  }
  
  public boolean isRunningOnJDK13() {
    return this.isRunningOnJDK13;
  }
  
  public boolean isSameResource(Connection paramConnection) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: aload_1
    //   10: ifnonnull -> 18
    //   13: aload #4
    //   15: monitorexit
    //   16: iconst_0
    //   17: ireturn
    //   18: aload_1
    //   19: checkcast com/mysql/jdbc/ConnectionImpl
    //   22: getfield origHostToConnectTo : Ljava/lang/String;
    //   25: astore #6
    //   27: aload_1
    //   28: checkcast com/mysql/jdbc/ConnectionImpl
    //   31: getfield origDatabaseToConnectTo : Ljava/lang/String;
    //   34: astore #7
    //   36: aload_1
    //   37: checkcast com/mysql/jdbc/ConnectionImpl
    //   40: getfield database : Ljava/lang/String;
    //   43: astore #5
    //   45: aload #6
    //   47: aload_0
    //   48: getfield origHostToConnectTo : Ljava/lang/String;
    //   51: invokestatic nullSafeCompare : (Ljava/lang/String;Ljava/lang/String;)Z
    //   54: ifne -> 62
    //   57: iconst_0
    //   58: istore_2
    //   59: goto -> 105
    //   62: aload #6
    //   64: ifnull -> 103
    //   67: aload #6
    //   69: bipush #44
    //   71: invokevirtual indexOf : (I)I
    //   74: iconst_m1
    //   75: if_icmpne -> 103
    //   78: aload #6
    //   80: bipush #58
    //   82: invokevirtual indexOf : (I)I
    //   85: iconst_m1
    //   86: if_icmpne -> 103
    //   89: aload_1
    //   90: checkcast com/mysql/jdbc/ConnectionImpl
    //   93: getfield origPortToConnectTo : I
    //   96: aload_0
    //   97: getfield origPortToConnectTo : I
    //   100: if_icmpne -> 57
    //   103: iconst_1
    //   104: istore_2
    //   105: iload_2
    //   106: istore_3
    //   107: iload_2
    //   108: ifeq -> 139
    //   111: aload #7
    //   113: aload_0
    //   114: getfield origDatabaseToConnectTo : Ljava/lang/String;
    //   117: invokestatic nullSafeCompare : (Ljava/lang/String;Ljava/lang/String;)Z
    //   120: ifeq -> 137
    //   123: iload_2
    //   124: istore_3
    //   125: aload #5
    //   127: aload_0
    //   128: getfield database : Ljava/lang/String;
    //   131: invokestatic nullSafeCompare : (Ljava/lang/String;Ljava/lang/String;)Z
    //   134: ifne -> 139
    //   137: iconst_0
    //   138: istore_3
    //   139: iload_3
    //   140: ifeq -> 148
    //   143: aload #4
    //   145: monitorexit
    //   146: iconst_1
    //   147: ireturn
    //   148: aload_1
    //   149: checkcast com/mysql/jdbc/ConnectionImpl
    //   152: invokevirtual getResourceId : ()Ljava/lang/String;
    //   155: astore_1
    //   156: aload_0
    //   157: invokevirtual getResourceId : ()Ljava/lang/String;
    //   160: astore #5
    //   162: aload_1
    //   163: ifnonnull -> 171
    //   166: aload #5
    //   168: ifnull -> 185
    //   171: aload_1
    //   172: aload #5
    //   174: invokestatic nullSafeCompare : (Ljava/lang/String;Ljava/lang/String;)Z
    //   177: ifeq -> 185
    //   180: aload #4
    //   182: monitorexit
    //   183: iconst_1
    //   184: ireturn
    //   185: aload #4
    //   187: monitorexit
    //   188: iconst_0
    //   189: ireturn
    //   190: astore_1
    //   191: aload #4
    //   193: monitorexit
    //   194: aload_1
    //   195: athrow
    // Exception table:
    //   from	to	target	type
    //   13	16	190	finally
    //   18	57	190	finally
    //   67	103	190	finally
    //   111	123	190	finally
    //   125	137	190	finally
    //   143	146	190	finally
    //   148	162	190	finally
    //   171	183	190	finally
    //   185	188	190	finally
    //   191	194	190	finally
  }
  
  public boolean isServerLocal() throws SQLException {
    synchronized (getConnectionMutex()) {
      SocketFactory socketFactory = (getIO()).socketFactory;
      if (socketFactory instanceof SocketMetadata)
        return ((SocketMetadata)socketFactory).isLocallyConnected(this); 
      getLog().logWarn(Messages.getString("Connection.NoMetadataOnSocketFactory"));
      return false;
    } 
  }
  
  public boolean isServerTruncatesFracSecs() {
    return this.serverTruncatesFracSecs;
  }
  
  public boolean isServerTzUTC() {
    return this.isServerTzUTC;
  }
  
  public boolean lowerCaseTableNames() {
    return this.lowerCaseTableNames;
  }
  
  public String nativeSQL(String paramString) throws SQLException {
    if (paramString == null)
      return null; 
    Object object = EscapeProcessor.escapeSQL(paramString, serverSupportsConvertFn(), getMultiHostSafeProxy());
    return (object instanceof String) ? (String)object : ((EscapeProcessorResult)object).escapedSql;
  }
  
  public boolean parserKnowsUnicode() {
    return this.parserKnowsUnicode;
  }
  
  public void ping() throws SQLException {
    pingInternal(true, 0);
  }
  
  public void pingInternal(boolean paramBoolean, int paramInt) throws SQLException {
    if (paramBoolean)
      checkClosed(); 
    long l = getSelfDestructOnPingSecondsLifetime();
    int i = getSelfDestructOnPingMaxOperations();
    if ((l <= 0L || System.currentTimeMillis() - this.connectionCreationTimeMillis <= l) && (i <= 0 || i > this.io.getCommandCount())) {
      this.io.sendCommand(14, null, null, false, null, paramInt);
      return;
    } 
    close();
    throw SQLError.createSQLException(Messages.getString("Connection.exceededConnectionLifetime"), "08S01", getExceptionInterceptor());
  }
  
  public CallableStatement prepareCall(String paramString) throws SQLException {
    return prepareCall(paramString, 1003, 1007);
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) throws SQLException {
    if (versionMeetsMinimum(5, 0, 0)) {
      CallableStatement callableStatement;
      if (!getCacheCallableStatements()) {
        callableStatement = parseCallableStatement(paramString);
      } else {
        synchronized (this.parsedCallableStatementCache) {
          CompoundCacheKey compoundCacheKey = new CompoundCacheKey();
          this(getCatalog(), (String)callableStatement);
          CallableStatement.CallableStatementParamInfo callableStatementParamInfo = this.parsedCallableStatementCache.get(compoundCacheKey);
          if (callableStatementParamInfo != null) {
            callableStatement = CallableStatement.getInstance(getMultiHostSafeProxy(), callableStatementParamInfo);
          } else {
            synchronized (parseCallableStatement((String)callableStatement)) {
              callableStatementParamInfo = callableStatement.paramInfo;
              this.parsedCallableStatementCache.put(compoundCacheKey, callableStatementParamInfo);
              callableStatement.setResultSetType(paramInt1);
              callableStatement.setResultSetConcurrency(paramInt2);
              return callableStatement;
            } 
          } 
          callableStatement.setResultSetType(paramInt1);
          callableStatement.setResultSetConcurrency(paramInt2);
          return callableStatement;
        } 
      } 
      callableStatement.setResultSetType(paramInt1);
      callableStatement.setResultSetConcurrency(paramInt2);
      return callableStatement;
    } 
    throw SQLError.createSQLException("Callable statements not supported.", "S1C00", getExceptionInterceptor());
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    if (!getPedantic() || paramInt3 == 1)
      return prepareCall(paramString, paramInt1, paramInt2); 
    throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
  }
  
  public PreparedStatement prepareStatement(String paramString) throws SQLException {
    return prepareStatement(paramString, 1003, 1007);
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt) throws SQLException {
    PreparedStatement preparedStatement = prepareStatement(paramString);
    PreparedStatement preparedStatement1 = (PreparedStatement)preparedStatement;
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    preparedStatement1.setRetrieveGeneratedKeys(bool);
    return preparedStatement;
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    synchronized (getConnectionMutex()) {
      boolean bool;
      PreparedStatement preparedStatement1;
      String str;
      PreparedStatement preparedStatement2;
      checkClosed();
      if (getProcessEscapeCodesForPrepStmts()) {
        str = nativeSQL(paramString);
      } else {
        str = paramString;
      } 
      if (this.useServerPreparedStmts && getEmulateUnsupportedPstmts()) {
        bool = canHandleAsServerPreparedStatement(str);
      } else {
        bool = true;
      } 
      if (this.useServerPreparedStmts && bool) {
        if (getCachePreparedStatements()) {
          synchronized (this.serverSideStatementCache) {
            LRUCache<CompoundCacheKey, ServerPreparedStatement> lRUCache = this.serverSideStatementCache;
            CompoundCacheKey compoundCacheKey = new CompoundCacheKey();
            this(this.database, paramString);
            PreparedStatement preparedStatement = lRUCache.remove(compoundCacheKey);
            if (preparedStatement != null) {
              ((ServerPreparedStatement)preparedStatement).setClosed(false);
              preparedStatement.clearParameters();
            } 
            preparedStatement1 = preparedStatement;
            if (preparedStatement == null)
              try {
                preparedStatement1 = ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), str, this.database, paramInt1, paramInt2);
                if (paramString.length() < getPreparedStatementCacheSqlLimit())
                  ((ServerPreparedStatement)preparedStatement1).isCached = true; 
                preparedStatement1.setResultSetType(paramInt1);
                preparedStatement1.setResultSetConcurrency(paramInt2);
              } catch (SQLException sQLException) {
                PreparedStatement preparedStatement3;
                if (getEmulateUnsupportedPstmts()) {
                  preparedStatement2 = (PreparedStatement)clientPrepareStatement(str, paramInt1, paramInt2, false);
                  preparedStatement3 = preparedStatement2;
                  if (paramString.length() < getPreparedStatementCacheSqlLimit()) {
                    this.serverSideStatementCheckCache.put(paramString, Boolean.FALSE);
                    preparedStatement3 = preparedStatement2;
                  } 
                } else {
                  throw preparedStatement3;
                } 
              }  
          } 
        } else {
          try {
            preparedStatement1 = ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), (String)preparedStatement2, this.database, paramInt1, paramInt2);
            preparedStatement1.setResultSetType(paramInt1);
            preparedStatement1.setResultSetConcurrency(paramInt2);
          } catch (SQLException sQLException) {
            if (getEmulateUnsupportedPstmts()) {
              preparedStatement1 = (PreparedStatement)clientPrepareStatement((String)preparedStatement2, paramInt1, paramInt2, false);
            } else {
              throw sQLException;
            } 
          } 
        } 
      } else {
        preparedStatement1 = (PreparedStatement)clientPrepareStatement((String)preparedStatement2, paramInt1, paramInt2, false);
      } 
      return preparedStatement1;
    } 
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    if (!getPedantic() || paramInt3 == 1)
      return prepareStatement(paramString, paramInt1, paramInt2); 
    throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
  }
  
  public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    boolean bool;
    PreparedStatement preparedStatement = prepareStatement(paramString);
    PreparedStatement preparedStatement1 = (PreparedStatement)preparedStatement;
    if (paramArrayOfint != null && paramArrayOfint.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    preparedStatement1.setRetrieveGeneratedKeys(bool);
    return preparedStatement;
  }
  
  public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    boolean bool;
    PreparedStatement preparedStatement1 = prepareStatement(paramString);
    PreparedStatement preparedStatement = (PreparedStatement)preparedStatement1;
    if (paramArrayOfString != null && paramArrayOfString.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    preparedStatement.setRetrieveGeneratedKeys(bool);
    return preparedStatement1;
  }
  
  public void realClose(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Throwable paramThrowable) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isClosed : ()Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_0
    //   9: aload #4
    //   11: putfield forceClosedReason : Ljava/lang/Throwable;
    //   14: iload_3
    //   15: ifne -> 220
    //   18: aload_0
    //   19: invokevirtual getAutoCommit : ()Z
    //   22: istore_3
    //   23: iload_3
    //   24: ifne -> 43
    //   27: iload_2
    //   28: ifeq -> 43
    //   31: aload_0
    //   32: invokevirtual rollback : ()V
    //   35: goto -> 43
    //   38: astore #4
    //   40: goto -> 46
    //   43: aconst_null
    //   44: astore #4
    //   46: aload_0
    //   47: invokespecial reportMetrics : ()V
    //   50: aload_0
    //   51: invokevirtual getUseUsageAdvisor : ()Z
    //   54: ifeq -> 184
    //   57: iload_1
    //   58: ifne -> 115
    //   61: aload_0
    //   62: getfield eventSink : Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   65: astore #6
    //   67: new com/mysql/jdbc/profiler/ProfilerEvent
    //   70: astore #7
    //   72: aload #7
    //   74: iconst_0
    //   75: ldc_w ''
    //   78: aload_0
    //   79: invokevirtual getCatalog : ()Ljava/lang/String;
    //   82: aload_0
    //   83: invokevirtual getId : ()J
    //   86: iconst_m1
    //   87: iconst_m1
    //   88: invokestatic currentTimeMillis : ()J
    //   91: lconst_0
    //   92: getstatic com/mysql/jdbc/Constants.MILLIS_I18N : Ljava/lang/String;
    //   95: aconst_null
    //   96: aload_0
    //   97: getfield pointOfOrigin : Ljava/lang/String;
    //   100: ldc_w 'Connection implicitly closed by Driver. You should call Connection.close() from your code to free resources more efficiently and avoid resource leaks.'
    //   103: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   106: aload #6
    //   108: aload #7
    //   110: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   115: invokestatic currentTimeMillis : ()J
    //   118: aload_0
    //   119: getfield connectionCreationTimeMillis : J
    //   122: lsub
    //   123: ldc2_w 500
    //   126: lcmp
    //   127: ifge -> 184
    //   130: aload_0
    //   131: getfield eventSink : Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   134: astore #7
    //   136: new com/mysql/jdbc/profiler/ProfilerEvent
    //   139: astore #6
    //   141: aload #6
    //   143: iconst_0
    //   144: ldc_w ''
    //   147: aload_0
    //   148: invokevirtual getCatalog : ()Ljava/lang/String;
    //   151: aload_0
    //   152: invokevirtual getId : ()J
    //   155: iconst_m1
    //   156: iconst_m1
    //   157: invokestatic currentTimeMillis : ()J
    //   160: lconst_0
    //   161: getstatic com/mysql/jdbc/Constants.MILLIS_I18N : Ljava/lang/String;
    //   164: aconst_null
    //   165: aload_0
    //   166: getfield pointOfOrigin : Ljava/lang/String;
    //   169: ldc_w 'Connection lifetime of < .5 seconds. You might be un-necessarily creating short-lived connections and should investigate connection pooling to be more efficient.'
    //   172: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   175: aload #7
    //   177: aload #6
    //   179: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   184: aload_0
    //   185: invokespecial closeAllOpenStatements : ()V
    //   188: goto -> 193
    //   191: astore #4
    //   193: aload_0
    //   194: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   197: astore #7
    //   199: aload #4
    //   201: astore #6
    //   203: aload #7
    //   205: ifnull -> 230
    //   208: aload #7
    //   210: invokevirtual quit : ()V
    //   213: aload #4
    //   215: astore #6
    //   217: goto -> 230
    //   220: aload_0
    //   221: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   224: invokevirtual forceClose : ()V
    //   227: aconst_null
    //   228: astore #6
    //   230: aload_0
    //   231: getfield statementInterceptors : Ljava/util/List;
    //   234: ifnull -> 279
    //   237: iconst_0
    //   238: istore #5
    //   240: iload #5
    //   242: aload_0
    //   243: getfield statementInterceptors : Ljava/util/List;
    //   246: invokeinterface size : ()I
    //   251: if_icmpge -> 279
    //   254: aload_0
    //   255: getfield statementInterceptors : Ljava/util/List;
    //   258: iload #5
    //   260: invokeinterface get : (I)Ljava/lang/Object;
    //   265: checkcast com/mysql/jdbc/StatementInterceptorV2
    //   268: invokeinterface destroy : ()V
    //   273: iinc #5, 1
    //   276: goto -> 240
    //   279: aload_0
    //   280: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   283: astore #4
    //   285: aload #4
    //   287: ifnull -> 297
    //   290: aload #4
    //   292: invokeinterface destroy : ()V
    //   297: aload_0
    //   298: getfield openStatements : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   301: invokevirtual clear : ()V
    //   304: aload_0
    //   305: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   308: astore #4
    //   310: aload #4
    //   312: ifnull -> 328
    //   315: aload #4
    //   317: invokevirtual releaseResources : ()V
    //   320: aload_0
    //   321: aconst_null
    //   322: putfield io : Lcom/mysql/jdbc/MysqlIO;
    //   325: goto -> 328
    //   328: aload_0
    //   329: aconst_null
    //   330: putfield statementInterceptors : Ljava/util/List;
    //   333: aload_0
    //   334: aconst_null
    //   335: putfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   338: aload_0
    //   339: invokestatic removeInstance : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   342: aload_0
    //   343: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   346: astore #4
    //   348: aload #4
    //   350: monitorenter
    //   351: aload_0
    //   352: getfield cancelTimer : Ljava/util/Timer;
    //   355: astore #7
    //   357: aload #7
    //   359: ifnull -> 367
    //   362: aload #7
    //   364: invokevirtual cancel : ()V
    //   367: aload #4
    //   369: monitorexit
    //   370: aload_0
    //   371: iconst_1
    //   372: putfield isClosed : Z
    //   375: aload #6
    //   377: ifnonnull -> 381
    //   380: return
    //   381: aload #6
    //   383: athrow
    //   384: astore #6
    //   386: aload #4
    //   388: monitorexit
    //   389: aload #6
    //   391: athrow
    //   392: astore #6
    //   394: aload_0
    //   395: getfield openStatements : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   398: invokevirtual clear : ()V
    //   401: aload_0
    //   402: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   405: astore #4
    //   407: aload #4
    //   409: ifnull -> 425
    //   412: aload #4
    //   414: invokevirtual releaseResources : ()V
    //   417: aload_0
    //   418: aconst_null
    //   419: putfield io : Lcom/mysql/jdbc/MysqlIO;
    //   422: goto -> 425
    //   425: aload_0
    //   426: aconst_null
    //   427: putfield statementInterceptors : Ljava/util/List;
    //   430: aload_0
    //   431: aconst_null
    //   432: putfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   435: aload_0
    //   436: invokestatic removeInstance : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   439: aload_0
    //   440: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   443: astore #4
    //   445: aload #4
    //   447: monitorenter
    //   448: aload_0
    //   449: getfield cancelTimer : Ljava/util/Timer;
    //   452: astore #7
    //   454: aload #7
    //   456: ifnull -> 464
    //   459: aload #7
    //   461: invokevirtual cancel : ()V
    //   464: aload #4
    //   466: monitorexit
    //   467: aload_0
    //   468: iconst_1
    //   469: putfield isClosed : Z
    //   472: aload #6
    //   474: athrow
    //   475: astore #6
    //   477: aload #4
    //   479: monitorexit
    //   480: aload #6
    //   482: athrow
    //   483: astore #6
    //   485: aload #4
    //   487: astore #6
    //   489: goto -> 230
    // Exception table:
    //   from	to	target	type
    //   18	23	392	finally
    //   31	35	38	java/sql/SQLException
    //   31	35	392	finally
    //   46	57	392	finally
    //   61	115	392	finally
    //   115	184	392	finally
    //   184	188	191	java/sql/SQLException
    //   184	188	392	finally
    //   193	199	392	finally
    //   208	213	483	java/lang/Exception
    //   208	213	392	finally
    //   220	227	392	finally
    //   230	237	392	finally
    //   240	273	392	finally
    //   279	285	392	finally
    //   290	297	392	finally
    //   351	357	384	finally
    //   362	367	384	finally
    //   367	370	384	finally
    //   386	389	384	finally
    //   448	454	475	finally
    //   459	464	475	finally
    //   464	467	475	finally
    //   477	480	475	finally
  }
  
  public void recachePreparedStatement(ServerPreparedStatement paramServerPreparedStatement) throws SQLException {
    synchronized (getConnectionMutex()) {
      if (getCachePreparedStatements() && paramServerPreparedStatement.isPoolable())
        synchronized (this.serverSideStatementCache) {
          LRUCache<CompoundCacheKey, ServerPreparedStatement> lRUCache = this.serverSideStatementCache;
          CompoundCacheKey compoundCacheKey = new CompoundCacheKey();
          this(paramServerPreparedStatement.currentCatalog, paramServerPreparedStatement.originalSql);
          compoundCacheKey = (CompoundCacheKey)lRUCache.put(compoundCacheKey, paramServerPreparedStatement);
          if (compoundCacheKey != null && compoundCacheKey != paramServerPreparedStatement) {
            ((ServerPreparedStatement)compoundCacheKey).isCached = false;
            ((ServerPreparedStatement)compoundCacheKey).setClosed(false);
            ((ServerPreparedStatement)compoundCacheKey).realClose(true, true);
          } 
        }  
      return;
    } 
  }
  
  public void registerQueryExecutionTime(long paramLong) {
    if (paramLong > this.longestQueryTimeMs) {
      this.longestQueryTimeMs = paramLong;
      repartitionPerformanceHistogram();
    } 
    addToPerformanceHistogram(paramLong, 1);
    if (paramLong < this.shortestQueryTimeMs) {
      long l;
      if (paramLong == 0L) {
        l = 1L;
      } else {
        l = paramLong;
      } 
      this.shortestQueryTimeMs = l;
    } 
    this.numberOfQueriesIssued++;
    this.totalQueryTimeMs += paramLong;
  }
  
  public void registerStatement(Statement paramStatement) {
    this.openStatements.addIfAbsent(paramStatement);
  }
  
  public void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {}
  
  public void reportMetricsIfNeeded() {
    if (getGatherPerformanceMetrics() && System.currentTimeMillis() - this.metricsLastReportedMs > getReportMetricsIntervalMillis())
      reportMetrics(); 
  }
  
  public void reportNumberOfTablesAccessed(int paramInt) {
    long l = paramInt;
    if (l < this.minimumNumberTablesAccessed)
      this.minimumNumberTablesAccessed = l; 
    if (l > this.maximumNumberTablesAccessed) {
      this.maximumNumberTablesAccessed = l;
      repartitionTablesAccessedHistogram();
    } 
    addToTablesAccessedHistogram(l, 1);
  }
  
  public void reportQueryTime(long paramLong) {
    synchronized (getConnectionMutex()) {
      long l = this.queryTimeCount + 1L;
      this.queryTimeCount = l;
      double d1 = this.queryTimeSum;
      double d2 = paramLong;
      this.queryTimeSum = d1 + d2;
      this.queryTimeSumSquares += (paramLong * paramLong);
      this.queryTimeMean = (this.queryTimeMean * (l - 1L) + d2) / l;
      return;
    } 
  }
  
  public void resetServerState() throws SQLException {
    if (!getParanoid() && this.io != null && versionMeetsMinimum(4, 0, 6))
      changeUser(this.user, this.password); 
  }
  
  public void rollback() throws SQLException {
    synchronized (getConnectionMutex()) {
      Exception exception;
      checkClosed();
      try {
        List<Extension> list = this.connectionLifecycleInterceptors;
        if (list != null) {
          IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
              public final ConnectionImpl this$0;
              
              public void forEach(Extension param1Extension) throws SQLException {
                if (!((ConnectionLifecycleInterceptor)param1Extension).rollback())
                  this.stopIterating = true; 
              }
            };
          super(this, list.iterator());
          iterateBlock.doForAll();
          boolean bool = iterateBlock.fullIteration();
          if (!bool) {
            this.needsPing = getReconnectAtTxEnd();
            return;
          } 
        } 
        if (!this.autoCommit || getRelaxAutoCommit()) {
          boolean bool = this.transactionsSupported;
          if (bool)
            try {
              rollbackNoChecks();
            } catch (SQLException null) {
              if (getIgnoreNonTxTables()) {
                int i = exception.getErrorCode();
                if (i == 1196) {
                  this.needsPing = getReconnectAtTxEnd();
                  return;
                } 
              } 
              throw exception;
            }  
          this.needsPing = getReconnectAtTxEnd();
          return;
        } 
        throw SQLError.createSQLException("Can't call rollback when autocommit=true", "08003", getExceptionInterceptor());
      } catch (SQLException null) {
        if ("08S01".equals(exception.getSQLState()))
          throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor()); 
        throw exception;
      } finally {}
      this.needsPing = getReconnectAtTxEnd();
      throw exception;
    } 
  }
  
  public void rollback(Savepoint paramSavepoint) throws SQLException {
    synchronized (getConnectionMutex()) {
      if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
        checkClosed();
        try {
          Statement statement2;
          List<Extension> list = this.connectionLifecycleInterceptors;
          if (list != null) {
            IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
                public final ConnectionImpl this$0;
                
                public final Savepoint val$savepoint;
                
                public void forEach(Extension param1Extension) throws SQLException {
                  if (!((ConnectionLifecycleInterceptor)param1Extension).rollback(savepoint))
                    this.stopIterating = true; 
                }
              };
            super(this, list.iterator(), paramSavepoint);
            iterateBlock.doForAll();
            boolean bool = iterateBlock.fullIteration();
            if (!bool)
              return; 
          } 
          StringBuilder stringBuilder = new StringBuilder();
          this("ROLLBACK TO SAVEPOINT ");
          stringBuilder.append('`');
          stringBuilder.append(paramSavepoint.getSavepointName());
          stringBuilder.append('`');
          list = null;
          Statement statement1 = null;
          try {
            Statement statement = getMetadataSafeStatement();
            statement1 = statement;
            statement2 = statement;
            statement.executeUpdate(stringBuilder.toString());
            closeStatement(statement);
            return;
          } catch (SQLException sQLException) {
            statement1 = statement2;
            int i = sQLException.getErrorCode();
            if (i == 1181) {
              statement1 = statement2;
              String str = sQLException.getMessage();
              if (str != null) {
                statement1 = statement2;
                if (str.indexOf("153") != -1) {
                  statement1 = statement2;
                  StringBuilder stringBuilder1 = new StringBuilder();
                  statement1 = statement2;
                  this();
                  statement1 = statement2;
                  stringBuilder1.append("Savepoint '");
                  statement1 = statement2;
                  stringBuilder1.append(paramSavepoint.getSavepointName());
                  statement1 = statement2;
                  stringBuilder1.append("' does not exist");
                  statement1 = statement2;
                  throw SQLError.createSQLException(stringBuilder1.toString(), "S1009", i, getExceptionInterceptor());
                } 
              } 
            } 
            statement1 = statement2;
            if (getIgnoreNonTxTables()) {
              statement1 = statement2;
              if (sQLException.getErrorCode() != 1196) {
                statement1 = statement2;
                throw sQLException;
              } 
            } 
            statement1 = statement2;
            if ("08S01".equals(sQLException.getSQLState())) {
              statement1 = statement2;
              throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
            } 
            statement1 = statement2;
            throw sQLException;
          } finally {}
          closeStatement(statement1);
          throw paramSavepoint;
        } finally {
          this.needsPing = getReconnectAtTxEnd();
        } 
      } 
      throw SQLError.createSQLFeatureNotSupportedException();
    } 
  }
  
  public PreparedStatement serverPrepareStatement(String paramString) throws SQLException {
    String str = paramString;
    if (getProcessEscapeCodesForPrepStmts())
      str = nativeSQL(paramString); 
    return ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), str, getCatalog(), 1003, 1007);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt) throws SQLException {
    String str = paramString;
    if (getProcessEscapeCodesForPrepStmts())
      str = nativeSQL(paramString); 
    ServerPreparedStatement serverPreparedStatement = ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), str, getCatalog(), 1003, 1007);
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    serverPreparedStatement.setRetrieveGeneratedKeys(bool);
    return serverPreparedStatement;
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    String str = paramString;
    if (getProcessEscapeCodesForPrepStmts())
      str = nativeSQL(paramString); 
    return ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), str, getCatalog(), paramInt1, paramInt2);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    if (!getPedantic() || paramInt3 == 1)
      return serverPrepareStatement(paramString, paramInt1, paramInt2); 
    throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    boolean bool;
    PreparedStatement preparedStatement = (PreparedStatement)serverPrepareStatement(paramString);
    if (paramArrayOfint != null && paramArrayOfint.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    preparedStatement.setRetrieveGeneratedKeys(bool);
    return preparedStatement;
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    boolean bool;
    PreparedStatement preparedStatement = (PreparedStatement)serverPrepareStatement(paramString);
    if (paramArrayOfString != null && paramArrayOfString.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    preparedStatement.setRetrieveGeneratedKeys(bool);
    return preparedStatement;
  }
  
  public boolean serverSupportsConvertFn() throws SQLException {
    return versionMeetsMinimum(4, 0, 2);
  }
  
  public void setAutoCommit(boolean paramBoolean) throws SQLException {
    synchronized (getConnectionMutex()) {
      checkClosed();
      null = this.connectionLifecycleInterceptors;
      if (null != null) {
        IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
            public final ConnectionImpl this$0;
            
            public final boolean val$autoCommitFlag;
            
            public void forEach(Extension param1Extension) throws SQLException {
              if (!((ConnectionLifecycleInterceptor)param1Extension).setAutoCommit(autoCommitFlag))
                this.stopIterating = true; 
            }
          };
        super(this, null.iterator(), paramBoolean);
        iterateBlock.doForAll();
        if (!iterateBlock.fullIteration())
          return; 
      } 
      boolean bool2 = getAutoReconnectForPools();
      boolean bool1 = true;
      if (bool2)
        setHighAvailability(true); 
      try {
        if (this.transactionsSupported) {
          if (getUseLocalSessionState() && this.autoCommit == paramBoolean) {
            bool1 = false;
          } else if (!getHighAvailability()) {
            bool1 = getIO().isSetNeededForAutoCommitMode(paramBoolean);
          } 
          this.autoCommit = paramBoolean;
          if (bool1) {
            String str;
            if (paramBoolean) {
              str = "SET autocommit=1";
            } else {
              str = "SET autocommit=0";
            } 
            execSQL(null, str, -1, null, 1003, 1007, false, this.database, null, false);
          } 
        } else if (paramBoolean || getRelaxAutoCommit()) {
          this.autoCommit = paramBoolean;
        } else {
          throw SQLError.createSQLException("MySQL Versions Older than 3.23.15 do not support transactions", "08003", getExceptionInterceptor());
        } 
        return;
      } finally {
        if (getAutoReconnectForPools())
          setHighAvailability(false); 
      } 
    } 
  }
  
  public void setCatalog(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getConnectionMutex : ()Ljava/lang/Object;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: aload_0
    //   10: invokevirtual checkClosed : ()V
    //   13: aload_1
    //   14: ifnull -> 190
    //   17: aload_0
    //   18: getfield connectionLifecycleInterceptors : Ljava/util/List;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull -> 57
    //   26: new com/mysql/jdbc/ConnectionImpl$8
    //   29: astore_2
    //   30: aload_2
    //   31: aload_0
    //   32: aload_3
    //   33: invokeinterface iterator : ()Ljava/util/Iterator;
    //   38: aload_1
    //   39: invokespecial <init> : (Lcom/mysql/jdbc/ConnectionImpl;Ljava/util/Iterator;Ljava/lang/String;)V
    //   42: aload_2
    //   43: invokevirtual doForAll : ()V
    //   46: aload_2
    //   47: invokevirtual fullIteration : ()Z
    //   50: ifne -> 57
    //   53: aload #4
    //   55: monitorexit
    //   56: return
    //   57: aload_0
    //   58: invokevirtual getUseLocalSessionState : ()Z
    //   61: ifeq -> 101
    //   64: aload_0
    //   65: getfield lowerCaseTableNames : Z
    //   68: ifeq -> 86
    //   71: aload_0
    //   72: getfield database : Ljava/lang/String;
    //   75: aload_1
    //   76: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   79: ifeq -> 101
    //   82: aload #4
    //   84: monitorexit
    //   85: return
    //   86: aload_0
    //   87: getfield database : Ljava/lang/String;
    //   90: aload_1
    //   91: invokevirtual equals : (Ljava/lang/Object;)Z
    //   94: ifeq -> 101
    //   97: aload #4
    //   99: monitorexit
    //   100: return
    //   101: aload_0
    //   102: getfield dbmd : Ljava/sql/DatabaseMetaData;
    //   105: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
    //   110: astore_3
    //   111: aload_3
    //   112: ifnull -> 127
    //   115: aload_3
    //   116: astore_2
    //   117: aload_3
    //   118: ldc_w ' '
    //   121: invokevirtual equals : (Ljava/lang/Object;)Z
    //   124: ifeq -> 131
    //   127: ldc_w ''
    //   130: astore_2
    //   131: new java/lang/StringBuilder
    //   134: astore_3
    //   135: aload_3
    //   136: ldc_w 'USE '
    //   139: invokespecial <init> : (Ljava/lang/String;)V
    //   142: aload_3
    //   143: aload_1
    //   144: aload_2
    //   145: aload_0
    //   146: invokevirtual getPedantic : ()Z
    //   149: invokestatic quoteIdentifier : (Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload_0
    //   157: aconst_null
    //   158: aload_3
    //   159: invokevirtual toString : ()Ljava/lang/String;
    //   162: iconst_m1
    //   163: aconst_null
    //   164: sipush #1003
    //   167: sipush #1007
    //   170: iconst_0
    //   171: aload_0
    //   172: getfield database : Ljava/lang/String;
    //   175: aconst_null
    //   176: iconst_0
    //   177: invokevirtual execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   180: pop
    //   181: aload_0
    //   182: aload_1
    //   183: putfield database : Ljava/lang/String;
    //   186: aload #4
    //   188: monitorexit
    //   189: return
    //   190: ldc_w 'Catalog can not be null'
    //   193: ldc_w 'S1009'
    //   196: aload_0
    //   197: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   200: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   203: athrow
    //   204: astore_1
    //   205: aload #4
    //   207: monitorexit
    //   208: aload_1
    //   209: athrow
    // Exception table:
    //   from	to	target	type
    //   9	13	204	finally
    //   17	22	204	finally
    //   26	56	204	finally
    //   57	85	204	finally
    //   86	100	204	finally
    //   101	111	204	finally
    //   117	127	204	finally
    //   131	189	204	finally
    //   190	204	204	finally
    //   205	208	204	finally
  }
  
  public void setFailedOver(boolean paramBoolean) {}
  
  public void setHoldability(int paramInt) throws SQLException {}
  
  public void setInGlobalTx(boolean paramBoolean) {
    this.isInGlobalTx = paramBoolean;
  }
  
  public void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException {
    synchronized (getConnectionMutex()) {
      SecurityManager securityManager = System.getSecurityManager();
      if (securityManager != null)
        securityManager.checkPermission(SET_NETWORK_TIMEOUT_PERM); 
      if (paramExecutor != null) {
        checkClosed();
        NetworkTimeoutSetter networkTimeoutSetter = new NetworkTimeoutSetter();
        this(this, this.io, paramInt);
        paramExecutor.execute(networkTimeoutSetter);
        return;
      } 
      throw SQLError.createSQLException("Executor can not be null", "S1009", getExceptionInterceptor());
    } 
  }
  
  @Deprecated
  public void setPreferSlaveDuringFailover(boolean paramBoolean) {}
  
  public void setProfilerEventHandlerInstance(ProfilerEventHandler paramProfilerEventHandler) {
    this.eventSink = paramProfilerEventHandler;
  }
  
  public void setProxy(MySQLConnection paramMySQLConnection) {
    this.proxy = paramMySQLConnection;
    if (paramMySQLConnection instanceof MultiHostMySQLConnection) {
      MultiHostConnectionProxy multiHostConnectionProxy = ((MultiHostMySQLConnection)paramMySQLConnection).getThisAsProxy();
    } else {
      paramMySQLConnection = null;
    } 
    this.realProxy = (InvocationHandler)paramMySQLConnection;
  }
  
  public void setReadInfoMsgEnabled(boolean paramBoolean) {
    this.readInfoMsg = paramBoolean;
  }
  
  public void setReadOnly(boolean paramBoolean) throws SQLException {
    checkClosed();
    setReadOnlyInternal(paramBoolean);
  }
  
  public void setReadOnlyInternal(boolean paramBoolean) throws SQLException {
    if (getReadOnlyPropagatesToServer() && versionMeetsMinimum(5, 6, 5) && (!getUseLocalSessionState() || paramBoolean != this.readOnly)) {
      String str;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("set session transaction ");
      if (paramBoolean) {
        str = "read only";
      } else {
        str = "read write";
      } 
      stringBuilder.append(str);
      execSQL(null, stringBuilder.toString(), -1, null, 1003, 1007, false, this.database, null, false);
    } 
    this.readOnly = paramBoolean;
  }
  
  public Savepoint setSavepoint() throws SQLException {
    MysqlSavepoint mysqlSavepoint = new MysqlSavepoint(getExceptionInterceptor());
    setSavepoint(mysqlSavepoint);
    return mysqlSavepoint;
  }
  
  public Savepoint setSavepoint(String paramString) throws SQLException {
    synchronized (getConnectionMutex()) {
      MysqlSavepoint mysqlSavepoint = new MysqlSavepoint();
      this(paramString, getExceptionInterceptor());
      setSavepoint(mysqlSavepoint);
      return mysqlSavepoint;
    } 
  }
  
  public void setSchema(String paramString) throws SQLException {
    synchronized (getConnectionMutex()) {
      checkClosed();
      return;
    } 
  }
  
  public void setSessionMaxRows(int paramInt) throws SQLException {
    synchronized (getConnectionMutex()) {
      if (this.sessionMaxRows != paramInt) {
        Integer integer;
        this.sessionMaxRows = paramInt;
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("SET SQL_SELECT_LIMIT=");
        paramInt = this.sessionMaxRows;
        if (paramInt == -1) {
          String str = "DEFAULT";
        } else {
          integer = Integer.valueOf(paramInt);
        } 
        stringBuilder.append(integer);
        execSQL(null, stringBuilder.toString(), -1, null, 1003, 1007, false, this.database, null, false);
      } 
      return;
    } 
  }
  
  public void setStatementComment(String paramString) {
    this.statementComment = paramString;
  }
  
  public void setTransactionIsolation(int paramInt) throws SQLException {
    synchronized (getConnectionMutex()) {
      checkClosed();
      if (this.hasIsolationLevels) {
        boolean bool1;
        boolean bool = getAlwaysSendSetIsolation();
        boolean bool2 = false;
        if (bool || paramInt != this.isolationLevel) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (getUseLocalSessionState()) {
          bool1 = bool2;
          if (this.isolationLevel != paramInt)
            bool1 = true; 
        } 
        if (bool1)
          if (paramInt != 0) {
            String str;
            if (paramInt != 1) {
              if (paramInt != 2) {
                if (paramInt != 4) {
                  if (paramInt == 8) {
                    str = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
                  } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    this();
                    stringBuilder.append("Unsupported transaction isolation level '");
                    stringBuilder.append(paramInt);
                    stringBuilder.append("'");
                    throw SQLError.createSQLException(stringBuilder.toString(), "S1C00", getExceptionInterceptor());
                  } 
                } else {
                  str = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
                } 
              } else {
                str = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
              } 
            } else {
              str = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
            } 
            execSQL(null, str, -1, null, 1003, 1007, false, this.database, null, false);
            this.isolationLevel = paramInt;
          } else {
            throw SQLError.createSQLException("Transaction isolation level NONE not supported by MySQL", getExceptionInterceptor());
          }  
        return;
      } 
      throw SQLError.createSQLException("Transaction Isolation Levels are not supported on MySQL versions older than 3.23.36.", "S1C00", getExceptionInterceptor());
    } 
  }
  
  public void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
    synchronized (getConnectionMutex()) {
      this.typeMap = paramMap;
      return;
    } 
  }
  
  public void shutdownServer() throws SQLException {
    try {
      if (versionMeetsMinimum(5, 7, 9)) {
        execSQL(null, "SHUTDOWN", -1, null, 1003, 1007, false, this.database, null, false);
      } else {
        this.io.sendCommand(8, null, null, false, null, 0);
      } 
      return;
    } catch (Exception exception) {
      SQLException sQLException = SQLError.createSQLException(Messages.getString("Connection.UnhandledExceptionDuringShutdown"), "S1000", getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  public boolean storesLowerCaseTableName() {
    return this.storesLowerCaseTableName;
  }
  
  public boolean supportsIsolationLevel() {
    return this.hasIsolationLevels;
  }
  
  public boolean supportsQuotedIdentifiers() {
    return this.hasQuotedIdentifiers;
  }
  
  public boolean supportsTransactions() {
    return this.transactionsSupported;
  }
  
  public void throwConnectionClosedException() throws SQLException {
    SQLException sQLException = SQLError.createSQLException("No operations allowed after connection closed.", "08003", getExceptionInterceptor());
    Throwable throwable = this.forceClosedReason;
    if (throwable != null)
      sQLException.initCause(throwable); 
    throw sQLException;
  }
  
  public void transactionBegun() throws SQLException {
    synchronized (getConnectionMutex()) {
      List<Extension> list = this.connectionLifecycleInterceptors;
      if (list != null) {
        IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
            public final ConnectionImpl this$0;
            
            public void forEach(Extension param1Extension) throws SQLException {
              ((ConnectionLifecycleInterceptor)param1Extension).transactionBegun();
            }
          };
        super(this, list.iterator());
        iterateBlock.doForAll();
      } 
      return;
    } 
  }
  
  public void transactionCompleted() throws SQLException {
    synchronized (getConnectionMutex()) {
      List<Extension> list = this.connectionLifecycleInterceptors;
      if (list != null) {
        IterateBlock<Extension> iterateBlock = new IterateBlock<Extension>() {
            public final ConnectionImpl this$0;
            
            public void forEach(Extension param1Extension) throws SQLException {
              ((ConnectionLifecycleInterceptor)param1Extension).transactionCompleted();
            }
          };
        super(this, list.iterator());
        iterateBlock.doForAll();
      } 
      return;
    } 
  }
  
  public void unSafeStatementInterceptors() throws SQLException {
    ArrayList<StatementInterceptorV2> arrayList = new ArrayList(this.statementInterceptors.size());
    for (byte b = 0; b < this.statementInterceptors.size(); b++)
      arrayList.add(((NoSubInterceptorWrapper)this.statementInterceptors.get(b)).getUnderlyingInterceptor()); 
    this.statementInterceptors = arrayList;
    MysqlIO mysqlIO = this.io;
    if (mysqlIO != null)
      mysqlIO.setStatementInterceptors(arrayList); 
  }
  
  public void unregisterStatement(Statement paramStatement) {
    this.openStatements.remove(paramStatement);
  }
  
  public boolean useAnsiQuotedIdentifiers() {
    synchronized (getConnectionMutex()) {
      return this.useAnsiQuotes;
    } 
  }
  
  public boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    checkClosed();
    return this.io.versionMeetsMinimum(paramInt1, paramInt2, paramInt3);
  }
  
  public static class CompoundCacheKey {
    public final String componentOne;
    
    public final String componentTwo;
    
    public final int hashCode;
    
    public CompoundCacheKey(String param1String1, String param1String2) {
      byte b;
      this.componentOne = param1String1;
      this.componentTwo = param1String2;
      int i = 0;
      if (param1String1 != null) {
        b = param1String1.hashCode();
      } else {
        b = 0;
      } 
      if (param1String2 != null)
        i = param1String2.hashCode(); 
      this.hashCode = (527 + b) * 31 + i;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this == param1Object)
        return true; 
      if (param1Object != null && CompoundCacheKey.class.isAssignableFrom(param1Object.getClass())) {
        CompoundCacheKey compoundCacheKey = (CompoundCacheKey)param1Object;
        param1Object = this.componentOne;
        if ((param1Object == null) ? (compoundCacheKey.componentOne == null) : param1Object.equals(compoundCacheKey.componentOne)) {
          param1Object = this.componentTwo;
          String str = compoundCacheKey.componentTwo;
          if (param1Object == null) {
            if (str != null)
              bool = false; 
          } else {
            bool = param1Object.equals(str);
          } 
          return bool;
        } 
      } 
      return false;
    }
    
    public int hashCode() {
      return this.hashCode;
    }
  }
  
  public class ExceptionInterceptorChain implements ExceptionInterceptor {
    private List<Extension> interceptors;
    
    public final ConnectionImpl this$0;
    
    public ExceptionInterceptorChain(String param1String) throws SQLException {
      this.interceptors = Util.loadExtensions(ConnectionImpl.this, ConnectionImpl.this.props, param1String, "Connection.BadExceptionInterceptor", this);
    }
    
    public void addRingZero(ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      this.interceptors.add(0, param1ExceptionInterceptor);
    }
    
    public void destroy() {
      List<Extension> list = this.interceptors;
      if (list != null) {
        Iterator<Extension> iterator = list.iterator();
        while (iterator.hasNext())
          ((ExceptionInterceptor)iterator.next()).destroy(); 
      } 
    }
    
    public List<Extension> getInterceptors() {
      return this.interceptors;
    }
    
    public void init(Connection param1Connection, Properties param1Properties) throws SQLException {
      List<Extension> list = this.interceptors;
      if (list != null) {
        Iterator<Extension> iterator = list.iterator();
        while (iterator.hasNext())
          ((ExceptionInterceptor)iterator.next()).init(param1Connection, param1Properties); 
      } 
    }
    
    public SQLException interceptException(SQLException param1SQLException, Connection param1Connection) {
      List<Extension> list = this.interceptors;
      SQLException sQLException = param1SQLException;
      if (list != null) {
        Iterator<Extension> iterator = list.iterator();
        while (true) {
          sQLException = param1SQLException;
          if (iterator.hasNext()) {
            param1SQLException = ((ExceptionInterceptor)iterator.next()).interceptException(param1SQLException, ConnectionImpl.this);
            continue;
          } 
          break;
        } 
      } 
      return sQLException;
    }
  }
  
  public static class NetworkTimeoutSetter implements Runnable {
    private final WeakReference<ConnectionImpl> connImplRef;
    
    private final int milliseconds;
    
    private final WeakReference<MysqlIO> mysqlIoRef;
    
    public NetworkTimeoutSetter(ConnectionImpl param1ConnectionImpl, MysqlIO param1MysqlIO, int param1Int) {
      this.connImplRef = new WeakReference<ConnectionImpl>(param1ConnectionImpl);
      this.mysqlIoRef = new WeakReference<MysqlIO>(param1MysqlIO);
      this.milliseconds = param1Int;
    }
    
    public void run() {
      try {
        ConnectionImpl connectionImpl = this.connImplRef.get();
        if (connectionImpl != null)
          synchronized (connectionImpl.getConnectionMutex()) {
            connectionImpl.setSocketTimeout(this.milliseconds);
            MysqlIO mysqlIO = this.mysqlIoRef.get();
            if (mysqlIO != null)
              mysqlIO.setSocketTimeout(this.milliseconds); 
          }  
        return;
      } catch (SQLException sQLException) {
        throw new RuntimeException(sQLException);
      } 
    }
  }
}
