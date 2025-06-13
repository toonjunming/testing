package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.StandardLogger;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

public class ConnectionPropertiesImpl implements Serializable, ConnectionProperties {
  private static final String CONNECTION_AND_AUTH_CATEGORY;
  
  private static final String DEBUGING_PROFILING_CATEGORY;
  
  private static final String HA_CATEGORY;
  
  private static final String MISC_CATEGORY;
  
  private static final String NETWORK_CATEGORY;
  
  private static final String PERFORMANCE_CATEGORY;
  
  private static final String[] PROPERTY_CATEGORIES;
  
  private static final ArrayList<Field> PROPERTY_LIST;
  
  private static final String SECURITY_CATEGORY;
  
  private static final String STANDARD_LOGGER_NAME;
  
  public static final String ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL = "convertToNull";
  
  public static final String ZERO_DATETIME_BEHAVIOR_EXCEPTION = "exception";
  
  public static final String ZERO_DATETIME_BEHAVIOR_ROUND = "round";
  
  private static final long serialVersionUID = 4257801713007640580L;
  
  private BooleanConnectionProperty allowLoadLocalInfile;
  
  private BooleanConnectionProperty allowMasterDownConnections;
  
  private BooleanConnectionProperty allowMultiQueries;
  
  private BooleanConnectionProperty allowNanAndInf;
  
  private BooleanConnectionProperty allowPublicKeyRetrieval;
  
  private BooleanConnectionProperty allowSlaveDownConnections;
  
  private BooleanConnectionProperty allowUrlInLocalInfile;
  
  private BooleanConnectionProperty alwaysSendSetIsolation;
  
  private StringConnectionProperty authenticationPlugins;
  
  private BooleanConnectionProperty autoClosePStmtStreams;
  
  private BooleanConnectionProperty autoDeserialize;
  
  private BooleanConnectionProperty autoGenerateTestcaseScript;
  
  private boolean autoGenerateTestcaseScriptAsBoolean;
  
  private BooleanConnectionProperty autoReconnect;
  
  private BooleanConnectionProperty autoReconnectForPools;
  
  private boolean autoReconnectForPoolsAsBoolean;
  
  private BooleanConnectionProperty autoSlowLog;
  
  private MemorySizeConnectionProperty blobSendChunkSize;
  
  private BooleanConnectionProperty blobsAreStrings;
  
  private BooleanConnectionProperty cacheCallableStatements;
  
  private BooleanConnectionProperty cacheDefaultTimezone;
  
  private BooleanConnectionProperty cachePreparedStatements;
  
  private boolean cacheResultSetMetaDataAsBoolean;
  
  private BooleanConnectionProperty cacheResultSetMetadata;
  
  private BooleanConnectionProperty cacheServerConfiguration;
  
  private IntegerConnectionProperty callableStatementCacheSize;
  
  private BooleanConnectionProperty capitalizeTypeNames;
  
  private StringConnectionProperty characterEncoding;
  
  private String characterEncodingAsString;
  
  public boolean characterEncodingIsAliasForSjis;
  
  private StringConnectionProperty characterSetResults;
  
  private StringConnectionProperty clientCertificateKeyStorePassword;
  
  private StringConnectionProperty clientCertificateKeyStoreType;
  
  private StringConnectionProperty clientCertificateKeyStoreUrl;
  
  private StringConnectionProperty clientInfoProvider;
  
  private StringConnectionProperty clobCharacterEncoding;
  
  private BooleanConnectionProperty clobberStreamingResults;
  
  private BooleanConnectionProperty compensateOnDuplicateKeyUpdateCounts;
  
  private IntegerConnectionProperty connectTimeout;
  
  private StringConnectionProperty connectionAttributes;
  
  private StringConnectionProperty connectionCollation;
  
  private StringConnectionProperty connectionLifecycleInterceptors;
  
  private BooleanConnectionProperty continueBatchOnError;
  
  private BooleanConnectionProperty createDatabaseIfNotExist;
  
  private StringConnectionProperty defaultAuthenticationPlugin;
  
  private IntegerConnectionProperty defaultFetchSize;
  
  private BooleanConnectionProperty detectCustomCollations;
  
  private BooleanConnectionProperty detectServerPreparedStmts;
  
  private StringConnectionProperty disabledAuthenticationPlugins;
  
  private BooleanConnectionProperty disconnectOnExpiredPasswords;
  
  private BooleanConnectionProperty dontCheckOnDuplicateKeyUpdateInSQL;
  
  private BooleanConnectionProperty dontTrackOpenResources;
  
  private BooleanConnectionProperty dumpMetadataOnColumnNotFound;
  
  private BooleanConnectionProperty dumpQueriesOnException;
  
  private BooleanConnectionProperty dynamicCalendars;
  
  private BooleanConnectionProperty elideSetAutoCommits;
  
  private BooleanConnectionProperty emptyStringsConvertToZero;
  
  private BooleanConnectionProperty emulateLocators;
  
  private BooleanConnectionProperty emulateUnsupportedPstmts;
  
  private BooleanConnectionProperty enableEscapeProcessing;
  
  private BooleanConnectionProperty enablePacketDebug;
  
  private BooleanConnectionProperty enableQueryTimeouts;
  
  private StringConnectionProperty enabledSSLCipherSuites;
  
  private StringConnectionProperty enabledTLSProtocols;
  
  private StringConnectionProperty exceptionInterceptors;
  
  private BooleanConnectionProperty explainSlowQueries;
  
  private BooleanConnectionProperty failOverReadOnly;
  
  private BooleanConnectionProperty functionsNeverReturnBlobs;
  
  private BooleanConnectionProperty gatherPerformanceMetrics;
  
  private BooleanConnectionProperty generateSimpleParameterMetadata;
  
  private BooleanConnectionProperty getProceduresReturnsFunctions;
  
  private boolean highAvailabilityAsBoolean;
  
  private BooleanConnectionProperty holdResultsOpenOverStatementClose;
  
  private BooleanConnectionProperty ignoreNonTxTables;
  
  private BooleanConnectionProperty includeInnodbStatusInDeadlockExceptions;
  
  private BooleanConnectionProperty includeThreadDumpInDeadlockExceptions;
  
  private BooleanConnectionProperty includeThreadNamesAsStatementComment;
  
  private IntegerConnectionProperty initialTimeout;
  
  private BooleanConnectionProperty isInteractiveClient;
  
  private BooleanConnectionProperty jdbcCompliantTruncation;
  
  private boolean jdbcCompliantTruncationForReads;
  
  public MemorySizeConnectionProperty largeRowSizeThreshold;
  
  private StringConnectionProperty loadBalanceAutoCommitStatementRegex;
  
  private IntegerConnectionProperty loadBalanceAutoCommitStatementThreshold;
  
  private IntegerConnectionProperty loadBalanceBlacklistTimeout;
  
  private StringConnectionProperty loadBalanceConnectionGroup;
  
  private BooleanConnectionProperty loadBalanceEnableJMX;
  
  private StringConnectionProperty loadBalanceExceptionChecker;
  
  private IntegerConnectionProperty loadBalanceHostRemovalGracePeriod;
  
  private IntegerConnectionProperty loadBalancePingTimeout;
  
  private StringConnectionProperty loadBalanceSQLExceptionSubclassFailover;
  
  private StringConnectionProperty loadBalanceSQLStateFailover;
  
  private StringConnectionProperty loadBalanceStrategy;
  
  private BooleanConnectionProperty loadBalanceValidateConnectionOnSwapServer;
  
  private StringConnectionProperty localSocketAddress;
  
  private MemorySizeConnectionProperty locatorFetchBufferSize;
  
  private BooleanConnectionProperty logSlowQueries;
  
  private BooleanConnectionProperty logXaCommands;
  
  private StringConnectionProperty loggerClassName;
  
  private BooleanConnectionProperty maintainTimeStats;
  
  private boolean maintainTimeStatsAsBoolean;
  
  private IntegerConnectionProperty maxAllowedPacket;
  
  private IntegerConnectionProperty maxQuerySizeToLog;
  
  private IntegerConnectionProperty maxReconnects;
  
  private IntegerConnectionProperty maxRows;
  
  private int maxRowsAsInt;
  
  private IntegerConnectionProperty metadataCacheSize;
  
  private IntegerConnectionProperty netTimeoutForStreamingResults;
  
  private BooleanConnectionProperty noAccessToProcedureBodies;
  
  private BooleanConnectionProperty noDatetimeStringSync;
  
  private BooleanConnectionProperty noTimezoneConversionForDateType;
  
  private BooleanConnectionProperty noTimezoneConversionForTimeType;
  
  private BooleanConnectionProperty nullCatalogMeansCurrent;
  
  private BooleanConnectionProperty nullNamePatternMatchesAll;
  
  private BooleanConnectionProperty overrideSupportsIntegrityEnhancementFacility;
  
  private IntegerConnectionProperty packetDebugBufferSize;
  
  private BooleanConnectionProperty padCharsWithSpace;
  
  private BooleanConnectionProperty paranoid;
  
  private StringConnectionProperty parseInfoCacheFactory;
  
  private StringConnectionProperty passwordCharacterEncoding;
  
  private BooleanConnectionProperty pedantic;
  
  private BooleanConnectionProperty pinGlobalTxToPhysicalConnection;
  
  private BooleanConnectionProperty populateInsertRowWithDefaultValues;
  
  private IntegerConnectionProperty preparedStatementCacheSize;
  
  private IntegerConnectionProperty preparedStatementCacheSqlLimit;
  
  private BooleanConnectionProperty processEscapeCodesForPrepStmts;
  
  private BooleanConnectionProperty profileSQL;
  
  private boolean profileSQLAsBoolean;
  
  private StringConnectionProperty profileSql;
  
  private StringConnectionProperty profilerEventHandler;
  
  private StringConnectionProperty propertiesTransform;
  
  private IntegerConnectionProperty queriesBeforeRetryMaster;
  
  private BooleanConnectionProperty queryTimeoutKillsConnection;
  
  private BooleanConnectionProperty readFromMasterWhenNoSlaves;
  
  private BooleanConnectionProperty readOnlyPropagatesToServer;
  
  private BooleanConnectionProperty reconnectAtTxEnd;
  
  private boolean reconnectTxAtEndAsBoolean;
  
  private BooleanConnectionProperty relaxAutoCommit;
  
  private StringConnectionProperty replicationConnectionGroup;
  
  private BooleanConnectionProperty replicationEnableJMX;
  
  private IntegerConnectionProperty reportMetricsIntervalMillis;
  
  private BooleanConnectionProperty requireSSL;
  
  private StringConnectionProperty resourceId;
  
  private IntegerConnectionProperty resultSetSizeThreshold;
  
  private BooleanConnectionProperty retainStatementAfterResultSetClose;
  
  private IntegerConnectionProperty retriesAllDown;
  
  private BooleanConnectionProperty rewriteBatchedStatements;
  
  private BooleanConnectionProperty rollbackOnPooledClose;
  
  private BooleanConnectionProperty roundRobinLoadBalance;
  
  private BooleanConnectionProperty runningCTS13;
  
  private IntegerConnectionProperty secondsBeforeRetryMaster;
  
  private IntegerConnectionProperty selfDestructOnPingMaxOperations;
  
  private IntegerConnectionProperty selfDestructOnPingSecondsLifetime;
  
  private BooleanConnectionProperty sendFractionalSeconds;
  
  private StringConnectionProperty serverAffinityOrder;
  
  private StringConnectionProperty serverConfigCacheFactory;
  
  private StringConnectionProperty serverRSAPublicKeyFile;
  
  private StringConnectionProperty serverTimezone;
  
  private StringConnectionProperty sessionVariables;
  
  private IntegerConnectionProperty slowQueryThresholdMillis;
  
  private LongConnectionProperty slowQueryThresholdNanos;
  
  private StringConnectionProperty socketFactoryClassName;
  
  private IntegerConnectionProperty socketTimeout;
  
  private StringConnectionProperty socksProxyHost;
  
  private IntegerConnectionProperty socksProxyPort;
  
  private StringConnectionProperty statementInterceptors;
  
  private BooleanConnectionProperty strictFloatingPoint;
  
  private BooleanConnectionProperty strictUpdates;
  
  private BooleanConnectionProperty tcpKeepAlive;
  
  private BooleanConnectionProperty tcpNoDelay;
  
  private IntegerConnectionProperty tcpRcvBuf;
  
  private IntegerConnectionProperty tcpSndBuf;
  
  private IntegerConnectionProperty tcpTrafficClass;
  
  private BooleanConnectionProperty tinyInt1isBit;
  
  public BooleanConnectionProperty traceProtocol;
  
  private BooleanConnectionProperty transformedBitIsBoolean;
  
  private BooleanConnectionProperty treatUtilDateAsTimestamp;
  
  private StringConnectionProperty trustCertificateKeyStorePassword;
  
  private StringConnectionProperty trustCertificateKeyStoreType;
  
  private StringConnectionProperty trustCertificateKeyStoreUrl;
  
  private BooleanConnectionProperty useAffectedRows;
  
  private BooleanConnectionProperty useBlobToStoreUTF8OutsideBMP;
  
  private BooleanConnectionProperty useColumnNamesInFindColumn;
  
  private BooleanConnectionProperty useCompression;
  
  private StringConnectionProperty useConfigs;
  
  private BooleanConnectionProperty useCursorFetch;
  
  private BooleanConnectionProperty useDirectRowUnpack;
  
  private BooleanConnectionProperty useDynamicCharsetInfo;
  
  private BooleanConnectionProperty useFastDateParsing;
  
  private BooleanConnectionProperty useFastIntParsing;
  
  private BooleanConnectionProperty useGmtMillisForDatetimes;
  
  private BooleanConnectionProperty useHostsInPrivileges;
  
  private BooleanConnectionProperty useInformationSchema;
  
  private BooleanConnectionProperty useJDBCCompliantTimezoneShift;
  
  private BooleanConnectionProperty useJvmCharsetConverters;
  
  private BooleanConnectionProperty useLegacyDatetimeCode;
  
  private BooleanConnectionProperty useLocalSessionState;
  
  private BooleanConnectionProperty useLocalTransactionState;
  
  private BooleanConnectionProperty useNanosForElapsedTime;
  
  private BooleanConnectionProperty useOldAliasMetadataBehavior;
  
  private BooleanConnectionProperty useOldUTF8Behavior;
  
  private boolean useOldUTF8BehaviorAsBoolean;
  
  private BooleanConnectionProperty useOnlyServerErrorMessages;
  
  private BooleanConnectionProperty useReadAheadInput;
  
  private BooleanConnectionProperty useSSL;
  
  private BooleanConnectionProperty useSSPSCompatibleTimezoneShift;
  
  private BooleanConnectionProperty useSqlStateCodes;
  
  private BooleanConnectionProperty useStreamLengthsInPrepStmts;
  
  private BooleanConnectionProperty useTimezone;
  
  private BooleanConnectionProperty useUltraDevWorkAround;
  
  private BooleanConnectionProperty useUnbufferedInput;
  
  private BooleanConnectionProperty useUnicode;
  
  private boolean useUnicodeAsBoolean;
  
  private BooleanConnectionProperty useUsageAdvisor;
  
  private boolean useUsageAdvisorAsBoolean;
  
  private StringConnectionProperty utf8OutsideBmpExcludedColumnNamePattern;
  
  private StringConnectionProperty utf8OutsideBmpIncludedColumnNamePattern;
  
  private BooleanConnectionProperty verifyServerCertificate;
  
  private BooleanConnectionProperty yearIsDateType;
  
  private StringConnectionProperty zeroDateTimeBehavior;
  
  static {
    String str1 = Messages.getString("ConnectionProperties.categoryConnectionAuthentication");
    CONNECTION_AND_AUTH_CATEGORY = str1;
    String str6 = Messages.getString("ConnectionProperties.categoryNetworking");
    NETWORK_CATEGORY = str6;
    String str3 = Messages.getString("ConnectionProperties.categoryDebuggingProfiling");
    DEBUGING_PROFILING_CATEGORY = str3;
    String str4 = Messages.getString("ConnectionProperties.categorryHA");
    HA_CATEGORY = str4;
    String str7 = Messages.getString("ConnectionProperties.categoryMisc");
    MISC_CATEGORY = str7;
    String str5 = Messages.getString("ConnectionProperties.categoryPerformance");
    PERFORMANCE_CATEGORY = str5;
    String str2 = Messages.getString("ConnectionProperties.categorySecurity");
    SECURITY_CATEGORY = str2;
    byte b = 0;
    PROPERTY_CATEGORIES = new String[] { str1, str6, str4, str2, str5, str3, str7 };
    PROPERTY_LIST = new ArrayList<Field>();
    STANDARD_LOGGER_NAME = StandardLogger.class.getName();
    try {
      Field[] arrayOfField = ConnectionPropertiesImpl.class.getDeclaredFields();
      while (b < arrayOfField.length) {
        if (ConnectionProperty.class.isAssignableFrom(arrayOfField[b].getType()))
          PROPERTY_LIST.add(arrayOfField[b]); 
        b++;
      } 
      return;
    } catch (Exception exception) {
      RuntimeException runtimeException = new RuntimeException();
      runtimeException.initCause(exception);
      throw runtimeException;
    } 
  }
  
  public ConnectionPropertiesImpl() {
    String str2 = Messages.getString("ConnectionProperties.loadDataLocal");
    String str1 = SECURITY_CATEGORY;
    this.allowLoadLocalInfile = new BooleanConnectionProperty("allowLoadLocalInfile", true, str2, "3.0.3", str1, 2147483647);
    this.allowMultiQueries = new BooleanConnectionProperty("allowMultiQueries", false, Messages.getString("ConnectionProperties.allowMultiQueries"), "3.1.1", str1, 1);
    String str3 = Messages.getString("ConnectionProperties.allowNANandINF");
    str2 = MISC_CATEGORY;
    this.allowNanAndInf = new BooleanConnectionProperty("allowNanAndInf", false, str3, "3.1.5", str2, -2147483648);
    this.allowUrlInLocalInfile = new BooleanConnectionProperty("allowUrlInLocalInfile", false, Messages.getString("ConnectionProperties.allowUrlInLoadLocal"), "3.1.4", str1, 2147483647);
    String str4 = Messages.getString("ConnectionProperties.alwaysSendSetIsolation");
    str3 = PERFORMANCE_CATEGORY;
    this.alwaysSendSetIsolation = new BooleanConnectionProperty("alwaysSendSetIsolation", true, str4, "3.1.7", str3, 2147483647);
    this.autoClosePStmtStreams = new BooleanConnectionProperty("autoClosePStmtStreams", false, Messages.getString("ConnectionProperties.autoClosePstmtStreams"), "3.1.12", str2, -2147483648);
    str4 = Messages.getString("ConnectionProperties.replicationConnectionGroup");
    String str6 = HA_CATEGORY;
    this.replicationConnectionGroup = new StringConnectionProperty("replicationConnectionGroup", null, str4, "5.1.27", str6, -2147483648);
    this.allowMasterDownConnections = new BooleanConnectionProperty("allowMasterDownConnections", false, Messages.getString("ConnectionProperties.allowMasterDownConnections"), "5.1.27", str6, 2147483647);
    this.allowSlaveDownConnections = new BooleanConnectionProperty("allowSlaveDownConnections", false, Messages.getString("ConnectionProperties.allowSlaveDownConnections"), "5.1.38", str6, 2147483647);
    this.readFromMasterWhenNoSlaves = new BooleanConnectionProperty("readFromMasterWhenNoSlaves", false, Messages.getString("ConnectionProperties.readFromMasterWhenNoSlaves"), "5.1.38", str6, 2147483647);
    this.autoDeserialize = new BooleanConnectionProperty("autoDeserialize", false, Messages.getString("ConnectionProperties.autoDeserialize"), "3.1.5", str2, -2147483648);
    String str5 = Messages.getString("ConnectionProperties.autoGenerateTestcaseScript");
    str4 = DEBUGING_PROFILING_CATEGORY;
    this.autoGenerateTestcaseScript = new BooleanConnectionProperty("autoGenerateTestcaseScript", false, str5, "3.1.9", str4, -2147483648);
    this.autoGenerateTestcaseScriptAsBoolean = false;
    this.autoReconnect = new BooleanConnectionProperty("autoReconnect", false, Messages.getString("ConnectionProperties.autoReconnect"), "1.1", str6, 0);
    this.autoReconnectForPools = new BooleanConnectionProperty("autoReconnectForPools", false, Messages.getString("ConnectionProperties.autoReconnectForPools"), "3.1.3", str6, 1);
    this.autoReconnectForPoolsAsBoolean = false;
    this.blobSendChunkSize = new MemorySizeConnectionProperty("blobSendChunkSize", 1048576, 0, 0, Messages.getString("ConnectionProperties.blobSendChunkSize"), "3.1.9", str3, -2147483648);
    this.autoSlowLog = new BooleanConnectionProperty("autoSlowLog", true, Messages.getString("ConnectionProperties.autoSlowLog"), "5.1.4", str4, -2147483648);
    this.blobsAreStrings = new BooleanConnectionProperty("blobsAreStrings", false, "Should the driver always treat BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", str2, -2147483648);
    this.functionsNeverReturnBlobs = new BooleanConnectionProperty("functionsNeverReturnBlobs", false, "Should the driver always treat data from functions returning BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", str2, -2147483648);
    this.cacheCallableStatements = new BooleanConnectionProperty("cacheCallableStmts", false, Messages.getString("ConnectionProperties.cacheCallableStatements"), "3.1.2", str3, -2147483648);
    this.cachePreparedStatements = new BooleanConnectionProperty("cachePrepStmts", false, Messages.getString("ConnectionProperties.cachePrepStmts"), "3.0.10", str3, -2147483648);
    this.cacheResultSetMetadata = new BooleanConnectionProperty("cacheResultSetMetadata", false, Messages.getString("ConnectionProperties.cacheRSMetadata"), "3.1.1", str3, -2147483648);
    this.serverConfigCacheFactory = new StringConnectionProperty("serverConfigCacheFactory", PerVmServerConfigCacheFactory.class.getName(), Messages.getString("ConnectionProperties.serverConfigCacheFactory"), "5.1.1", str3, 12);
    this.cacheServerConfiguration = new BooleanConnectionProperty("cacheServerConfiguration", false, Messages.getString("ConnectionProperties.cacheServerConfiguration"), "3.1.5", str3, -2147483648);
    this.callableStatementCacheSize = new IntegerConnectionProperty("callableStmtCacheSize", 100, 0, 2147483647, Messages.getString("ConnectionProperties.callableStmtCacheSize"), "3.1.2", str3, 5);
    this.capitalizeTypeNames = new BooleanConnectionProperty("capitalizeTypeNames", true, Messages.getString("ConnectionProperties.capitalizeTypeNames"), "2.0.7", str2, -2147483648);
    this.characterEncoding = new StringConnectionProperty("characterEncoding", null, Messages.getString("ConnectionProperties.characterEncoding"), "1.1g", str2, 5);
    this.characterEncodingAsString = null;
    this.characterEncodingIsAliasForSjis = false;
    this.characterSetResults = new StringConnectionProperty("characterSetResults", null, Messages.getString("ConnectionProperties.characterSetResults"), "3.0.13", str2, 6);
    this.connectionAttributes = new StringConnectionProperty("connectionAttributes", null, Messages.getString("ConnectionProperties.connectionAttributes"), "5.1.25", str2, 7);
    this.clientInfoProvider = new StringConnectionProperty("clientInfoProvider", "com.mysql.jdbc.JDBC4CommentClientInfoProvider", Messages.getString("ConnectionProperties.clientInfoProvider"), "5.1.0", str4, -2147483648);
    this.clobberStreamingResults = new BooleanConnectionProperty("clobberStreamingResults", false, Messages.getString("ConnectionProperties.clobberStreamingResults"), "3.0.9", str2, -2147483648);
    this.clobCharacterEncoding = new StringConnectionProperty("clobCharacterEncoding", null, Messages.getString("ConnectionProperties.clobCharacterEncoding"), "5.0.0", str2, -2147483648);
    this.compensateOnDuplicateKeyUpdateCounts = new BooleanConnectionProperty("compensateOnDuplicateKeyUpdateCounts", false, Messages.getString("ConnectionProperties.compensateOnDuplicateKeyUpdateCounts"), "5.1.7", str2, -2147483648);
    this.connectionCollation = new StringConnectionProperty("connectionCollation", null, Messages.getString("ConnectionProperties.connectionCollation"), "3.0.13", str2, 7);
    String str8 = Messages.getString("ConnectionProperties.connectionLifecycleInterceptors");
    str5 = CONNECTION_AND_AUTH_CATEGORY;
    this.connectionLifecycleInterceptors = new StringConnectionProperty("connectionLifecycleInterceptors", null, str8, "5.1.4", str5, 2147483647);
    this.connectTimeout = new IntegerConnectionProperty("connectTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.connectTimeout"), "3.0.1", str5, 9);
    this.continueBatchOnError = new BooleanConnectionProperty("continueBatchOnError", true, Messages.getString("ConnectionProperties.continueBatchOnError"), "3.0.3", str2, -2147483648);
    this.createDatabaseIfNotExist = new BooleanConnectionProperty("createDatabaseIfNotExist", false, Messages.getString("ConnectionProperties.createDatabaseIfNotExist"), "3.1.9", str2, -2147483648);
    this.defaultFetchSize = new IntegerConnectionProperty("defaultFetchSize", 0, Messages.getString("ConnectionProperties.defaultFetchSize"), "3.1.9", str3, -2147483648);
    this.detectServerPreparedStmts = new BooleanConnectionProperty("useServerPrepStmts", false, Messages.getString("ConnectionProperties.useServerPrepStmts"), "3.1.0", str2, -2147483648);
    this.dontTrackOpenResources = new BooleanConnectionProperty("dontTrackOpenResources", false, Messages.getString("ConnectionProperties.dontTrackOpenResources"), "3.1.7", str3, -2147483648);
    this.dumpQueriesOnException = new BooleanConnectionProperty("dumpQueriesOnException", false, Messages.getString("ConnectionProperties.dumpQueriesOnException"), "3.1.3", str4, -2147483648);
    this.dynamicCalendars = new BooleanConnectionProperty("dynamicCalendars", false, Messages.getString("ConnectionProperties.dynamicCalendars"), "3.1.5", str3, -2147483648);
    this.elideSetAutoCommits = new BooleanConnectionProperty("elideSetAutoCommits", false, Messages.getString("ConnectionProperties.eliseSetAutoCommit"), "3.1.3", str3, -2147483648);
    this.emptyStringsConvertToZero = new BooleanConnectionProperty("emptyStringsConvertToZero", true, Messages.getString("ConnectionProperties.emptyStringsConvertToZero"), "3.1.8", str2, -2147483648);
    this.emulateLocators = new BooleanConnectionProperty("emulateLocators", false, Messages.getString("ConnectionProperties.emulateLocators"), "3.1.0", str2, -2147483648);
    this.emulateUnsupportedPstmts = new BooleanConnectionProperty("emulateUnsupportedPstmts", true, Messages.getString("ConnectionProperties.emulateUnsupportedPstmts"), "3.1.7", str2, -2147483648);
    this.enablePacketDebug = new BooleanConnectionProperty("enablePacketDebug", false, Messages.getString("ConnectionProperties.enablePacketDebug"), "3.1.3", str4, -2147483648);
    this.enableQueryTimeouts = new BooleanConnectionProperty("enableQueryTimeouts", true, Messages.getString("ConnectionProperties.enableQueryTimeouts"), "5.0.6", str3, -2147483648);
    this.explainSlowQueries = new BooleanConnectionProperty("explainSlowQueries", false, Messages.getString("ConnectionProperties.explainSlowQueries"), "3.1.2", str4, -2147483648);
    this.exceptionInterceptors = new StringConnectionProperty("exceptionInterceptors", null, Messages.getString("ConnectionProperties.exceptionInterceptors"), "5.1.8", str2, -2147483648);
    this.failOverReadOnly = new BooleanConnectionProperty("failOverReadOnly", true, Messages.getString("ConnectionProperties.failoverReadOnly"), "3.0.12", str6, 2);
    this.gatherPerformanceMetrics = new BooleanConnectionProperty("gatherPerfMetrics", false, Messages.getString("ConnectionProperties.gatherPerfMetrics"), "3.1.2", str4, 1);
    this.generateSimpleParameterMetadata = new BooleanConnectionProperty("generateSimpleParameterMetadata", false, Messages.getString("ConnectionProperties.generateSimpleParameterMetadata"), "5.0.5", str2, -2147483648);
    this.highAvailabilityAsBoolean = false;
    this.holdResultsOpenOverStatementClose = new BooleanConnectionProperty("holdResultsOpenOverStatementClose", false, Messages.getString("ConnectionProperties.holdRSOpenOverStmtClose"), "3.1.7", str3, -2147483648);
    this.includeInnodbStatusInDeadlockExceptions = new BooleanConnectionProperty("includeInnodbStatusInDeadlockExceptions", false, Messages.getString("ConnectionProperties.includeInnodbStatusInDeadlockExceptions"), "5.0.7", str4, -2147483648);
    this.includeThreadDumpInDeadlockExceptions = new BooleanConnectionProperty("includeThreadDumpInDeadlockExceptions", false, Messages.getString("ConnectionProperties.includeThreadDumpInDeadlockExceptions"), "5.1.15", str4, -2147483648);
    this.includeThreadNamesAsStatementComment = new BooleanConnectionProperty("includeThreadNamesAsStatementComment", false, Messages.getString("ConnectionProperties.includeThreadNamesAsStatementComment"), "5.1.15", str4, -2147483648);
    this.ignoreNonTxTables = new BooleanConnectionProperty("ignoreNonTxTables", false, Messages.getString("ConnectionProperties.ignoreNonTxTables"), "3.0.9", str2, -2147483648);
    this.initialTimeout = new IntegerConnectionProperty("initialTimeout", 2, 1, 2147483647, Messages.getString("ConnectionProperties.initialTimeout"), "1.1", str6, 5);
    this.isInteractiveClient = new BooleanConnectionProperty("interactiveClient", false, Messages.getString("ConnectionProperties.interactiveClient"), "3.1.0", str5, -2147483648);
    BooleanConnectionProperty booleanConnectionProperty = new BooleanConnectionProperty("jdbcCompliantTruncation", true, Messages.getString("ConnectionProperties.jdbcCompliantTruncation"), "3.1.2", str2, -2147483648);
    this.jdbcCompliantTruncation = booleanConnectionProperty;
    this.jdbcCompliantTruncationForReads = booleanConnectionProperty.getValueAsBoolean();
    this.largeRowSizeThreshold = new MemorySizeConnectionProperty("largeRowSizeThreshold", 2048, 0, 2147483647, Messages.getString("ConnectionProperties.largeRowSizeThreshold"), "5.1.1", str3, -2147483648);
    this.loadBalanceStrategy = new StringConnectionProperty("loadBalanceStrategy", "random", null, Messages.getString("ConnectionProperties.loadBalanceStrategy"), "5.0.6", str3, -2147483648);
    this.serverAffinityOrder = new StringConnectionProperty("serverAffinityOrder", "", null, Messages.getString("ConnectionProperties.serverAffinityOrder"), "5.1.43", str3, -2147483648);
    this.loadBalanceBlacklistTimeout = new IntegerConnectionProperty("loadBalanceBlacklistTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalanceBlacklistTimeout"), "5.1.0", str2, -2147483648);
    this.loadBalancePingTimeout = new IntegerConnectionProperty("loadBalancePingTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalancePingTimeout"), "5.1.13", str2, -2147483648);
    this.loadBalanceValidateConnectionOnSwapServer = new BooleanConnectionProperty("loadBalanceValidateConnectionOnSwapServer", false, Messages.getString("ConnectionProperties.loadBalanceValidateConnectionOnSwapServer"), "5.1.13", str2, -2147483648);
    this.loadBalanceConnectionGroup = new StringConnectionProperty("loadBalanceConnectionGroup", null, Messages.getString("ConnectionProperties.loadBalanceConnectionGroup"), "5.1.13", str2, -2147483648);
    this.loadBalanceExceptionChecker = new StringConnectionProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker", null, Messages.getString("ConnectionProperties.loadBalanceExceptionChecker"), "5.1.13", str2, -2147483648);
    this.loadBalanceSQLStateFailover = new StringConnectionProperty("loadBalanceSQLStateFailover", null, Messages.getString("ConnectionProperties.loadBalanceSQLStateFailover"), "5.1.13", str2, -2147483648);
    this.loadBalanceSQLExceptionSubclassFailover = new StringConnectionProperty("loadBalanceSQLExceptionSubclassFailover", null, Messages.getString("ConnectionProperties.loadBalanceSQLExceptionSubclassFailover"), "5.1.13", str2, -2147483648);
    this.loadBalanceEnableJMX = new BooleanConnectionProperty("loadBalanceEnableJMX", false, Messages.getString("ConnectionProperties.loadBalanceEnableJMX"), "5.1.13", str2, 2147483647);
    this.loadBalanceHostRemovalGracePeriod = new IntegerConnectionProperty("loadBalanceHostRemovalGracePeriod", 15000, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalanceHostRemovalGracePeriod"), "5.1.39", str2, 2147483647);
    this.loadBalanceAutoCommitStatementRegex = new StringConnectionProperty("loadBalanceAutoCommitStatementRegex", null, Messages.getString("ConnectionProperties.loadBalanceAutoCommitStatementRegex"), "5.1.15", str2, -2147483648);
    this.loadBalanceAutoCommitStatementThreshold = new IntegerConnectionProperty("loadBalanceAutoCommitStatementThreshold", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalanceAutoCommitStatementThreshold"), "5.1.15", str2, -2147483648);
    this.localSocketAddress = new StringConnectionProperty("localSocketAddress", null, Messages.getString("ConnectionProperties.localSocketAddress"), "5.0.5", str5, -2147483648);
    this.locatorFetchBufferSize = new MemorySizeConnectionProperty("locatorFetchBufferSize", 1048576, 0, 2147483647, Messages.getString("ConnectionProperties.locatorFetchBufferSize"), "3.2.1", str3, -2147483648);
    String str7 = STANDARD_LOGGER_NAME;
    this.loggerClassName = new StringConnectionProperty("logger", str7, Messages.getString("ConnectionProperties.logger", new Object[] { Log.class.getName(), str7 }), "3.1.1", str4, 0);
    this.logSlowQueries = new BooleanConnectionProperty("logSlowQueries", false, Messages.getString("ConnectionProperties.logSlowQueries"), "3.1.2", str4, -2147483648);
    this.logXaCommands = new BooleanConnectionProperty("logXaCommands", false, Messages.getString("ConnectionProperties.logXaCommands"), "5.0.5", str4, -2147483648);
    this.maintainTimeStats = new BooleanConnectionProperty("maintainTimeStats", true, Messages.getString("ConnectionProperties.maintainTimeStats"), "3.1.9", str3, 2147483647);
    this.maintainTimeStatsAsBoolean = true;
    this.maxQuerySizeToLog = new IntegerConnectionProperty("maxQuerySizeToLog", 2048, 0, 2147483647, Messages.getString("ConnectionProperties.maxQuerySizeToLog"), "3.1.3", str4, 4);
    this.maxReconnects = new IntegerConnectionProperty("maxReconnects", 3, 1, 2147483647, Messages.getString("ConnectionProperties.maxReconnects"), "1.1", str6, 4);
    this.retriesAllDown = new IntegerConnectionProperty("retriesAllDown", 120, 0, 2147483647, Messages.getString("ConnectionProperties.retriesAllDown"), "5.1.6", str6, 4);
    this.maxRows = new IntegerConnectionProperty("maxRows", -1, -1, 2147483647, Messages.getString("ConnectionProperties.maxRows"), Messages.getString("ConnectionProperties.allVersions"), str2, -2147483648);
    this.maxRowsAsInt = -1;
    this.metadataCacheSize = new IntegerConnectionProperty("metadataCacheSize", 50, 1, 2147483647, Messages.getString("ConnectionProperties.metadataCacheSize"), "3.1.1", str3, 5);
    this.netTimeoutForStreamingResults = new IntegerConnectionProperty("netTimeoutForStreamingResults", 600, 0, 2147483647, Messages.getString("ConnectionProperties.netTimeoutForStreamingResults"), "5.1.0", str2, -2147483648);
    this.noAccessToProcedureBodies = new BooleanConnectionProperty("noAccessToProcedureBodies", false, "When determining procedure parameter types for CallableStatements, and the connected user  can't access procedure bodies through \"SHOW CREATE PROCEDURE\" or select on mysql.proc  should the driver instead create basic metadata (all parameters reported as IN VARCHARs, but allowing registerOutParameter() to be called on them anyway) instead of throwing an exception?", "5.0.3", str2, -2147483648);
    this.noDatetimeStringSync = new BooleanConnectionProperty("noDatetimeStringSync", false, Messages.getString("ConnectionProperties.noDatetimeStringSync"), "3.1.7", str2, -2147483648);
    this.noTimezoneConversionForTimeType = new BooleanConnectionProperty("noTimezoneConversionForTimeType", false, Messages.getString("ConnectionProperties.noTzConversionForTimeType"), "5.0.0", str2, -2147483648);
    this.noTimezoneConversionForDateType = new BooleanConnectionProperty("noTimezoneConversionForDateType", true, Messages.getString("ConnectionProperties.noTzConversionForDateType"), "5.1.35", str2, -2147483648);
    this.cacheDefaultTimezone = new BooleanConnectionProperty("cacheDefaultTimezone", true, Messages.getString("ConnectionProperties.cacheDefaultTimezone"), "5.1.35", str2, -2147483648);
    this.nullCatalogMeansCurrent = new BooleanConnectionProperty("nullCatalogMeansCurrent", true, Messages.getString("ConnectionProperties.nullCatalogMeansCurrent"), "3.1.8", str2, -2147483648);
    this.nullNamePatternMatchesAll = new BooleanConnectionProperty("nullNamePatternMatchesAll", true, Messages.getString("ConnectionProperties.nullNamePatternMatchesAll"), "3.1.8", str2, -2147483648);
    this.packetDebugBufferSize = new IntegerConnectionProperty("packetDebugBufferSize", 20, 1, 2147483647, Messages.getString("ConnectionProperties.packetDebugBufferSize"), "3.1.3", str4, 7);
    this.padCharsWithSpace = new BooleanConnectionProperty("padCharsWithSpace", false, Messages.getString("ConnectionProperties.padCharsWithSpace"), "5.0.6", str2, -2147483648);
    this.paranoid = new BooleanConnectionProperty("paranoid", false, Messages.getString("ConnectionProperties.paranoid"), "3.0.1", str1, -2147483648);
    this.pedantic = new BooleanConnectionProperty("pedantic", false, Messages.getString("ConnectionProperties.pedantic"), "3.0.0", str2, -2147483648);
    this.pinGlobalTxToPhysicalConnection = new BooleanConnectionProperty("pinGlobalTxToPhysicalConnection", false, Messages.getString("ConnectionProperties.pinGlobalTxToPhysicalConnection"), "5.0.1", str2, -2147483648);
    this.populateInsertRowWithDefaultValues = new BooleanConnectionProperty("populateInsertRowWithDefaultValues", false, Messages.getString("ConnectionProperties.populateInsertRowWithDefaultValues"), "5.0.5", str2, -2147483648);
    this.preparedStatementCacheSize = new IntegerConnectionProperty("prepStmtCacheSize", 25, 0, 2147483647, Messages.getString("ConnectionProperties.prepStmtCacheSize"), "3.0.10", str3, 10);
    this.preparedStatementCacheSqlLimit = new IntegerConnectionProperty("prepStmtCacheSqlLimit", 256, 1, 2147483647, Messages.getString("ConnectionProperties.prepStmtCacheSqlLimit"), "3.0.10", str3, 11);
    this.parseInfoCacheFactory = new StringConnectionProperty("parseInfoCacheFactory", PerConnectionLRUFactory.class.getName(), Messages.getString("ConnectionProperties.parseInfoCacheFactory"), "5.1.1", str3, 12);
    this.processEscapeCodesForPrepStmts = new BooleanConnectionProperty("processEscapeCodesForPrepStmts", true, Messages.getString("ConnectionProperties.processEscapeCodesForPrepStmts"), "3.1.12", str2, -2147483648);
    this.profilerEventHandler = new StringConnectionProperty("profilerEventHandler", "com.mysql.jdbc.profiler.LoggingProfilerEventHandler", Messages.getString("ConnectionProperties.profilerEventHandler"), "5.1.6", str4, -2147483648);
    this.profileSql = new StringConnectionProperty("profileSql", null, Messages.getString("ConnectionProperties.profileSqlDeprecated"), "2.0.14", str4, 3);
    this.profileSQL = new BooleanConnectionProperty("profileSQL", false, Messages.getString("ConnectionProperties.profileSQL"), "3.1.0", str4, 1);
    this.profileSQLAsBoolean = false;
    this.propertiesTransform = new StringConnectionProperty("propertiesTransform", null, Messages.getString("ConnectionProperties.connectionPropertiesTransform"), "3.1.4", str5, -2147483648);
    this.queriesBeforeRetryMaster = new IntegerConnectionProperty("queriesBeforeRetryMaster", 50, 0, 2147483647, Messages.getString("ConnectionProperties.queriesBeforeRetryMaster"), "3.0.2", str6, 7);
    this.queryTimeoutKillsConnection = new BooleanConnectionProperty("queryTimeoutKillsConnection", false, Messages.getString("ConnectionProperties.queryTimeoutKillsConnection"), "5.1.9", str2, -2147483648);
    this.reconnectAtTxEnd = new BooleanConnectionProperty("reconnectAtTxEnd", false, Messages.getString("ConnectionProperties.reconnectAtTxEnd"), "3.0.10", str6, 4);
    this.reconnectTxAtEndAsBoolean = false;
    this.relaxAutoCommit = new BooleanConnectionProperty("relaxAutoCommit", false, Messages.getString("ConnectionProperties.relaxAutoCommit"), "2.0.13", str2, -2147483648);
    this.reportMetricsIntervalMillis = new IntegerConnectionProperty("reportMetricsIntervalMillis", 30000, 0, 2147483647, Messages.getString("ConnectionProperties.reportMetricsIntervalMillis"), "3.1.2", str4, 3);
    this.requireSSL = new BooleanConnectionProperty("requireSSL", false, Messages.getString("ConnectionProperties.requireSSL"), "3.1.0", str1, 3);
    this.resourceId = new StringConnectionProperty("resourceId", null, Messages.getString("ConnectionProperties.resourceId"), "5.0.1", str6, -2147483648);
    this.resultSetSizeThreshold = new IntegerConnectionProperty("resultSetSizeThreshold", 100, Messages.getString("ConnectionProperties.resultSetSizeThreshold"), "5.0.5", str4, -2147483648);
    this.retainStatementAfterResultSetClose = new BooleanConnectionProperty("retainStatementAfterResultSetClose", false, Messages.getString("ConnectionProperties.retainStatementAfterResultSetClose"), "3.1.11", str2, -2147483648);
    this.rewriteBatchedStatements = new BooleanConnectionProperty("rewriteBatchedStatements", false, Messages.getString("ConnectionProperties.rewriteBatchedStatements"), "3.1.13", str3, -2147483648);
    this.rollbackOnPooledClose = new BooleanConnectionProperty("rollbackOnPooledClose", true, Messages.getString("ConnectionProperties.rollbackOnPooledClose"), "3.0.15", str2, -2147483648);
    this.roundRobinLoadBalance = new BooleanConnectionProperty("roundRobinLoadBalance", false, Messages.getString("ConnectionProperties.roundRobinLoadBalance"), "3.1.2", str6, 5);
    this.runningCTS13 = new BooleanConnectionProperty("runningCTS13", false, Messages.getString("ConnectionProperties.runningCTS13"), "3.1.7", str2, -2147483648);
    this.secondsBeforeRetryMaster = new IntegerConnectionProperty("secondsBeforeRetryMaster", 30, 0, 2147483647, Messages.getString("ConnectionProperties.secondsBeforeRetryMaster"), "3.0.2", str6, 8);
    this.selfDestructOnPingSecondsLifetime = new IntegerConnectionProperty("selfDestructOnPingSecondsLifetime", 0, 0, 2147483647, Messages.getString("ConnectionProperties.selfDestructOnPingSecondsLifetime"), "5.1.6", str6, 2147483647);
    this.selfDestructOnPingMaxOperations = new IntegerConnectionProperty("selfDestructOnPingMaxOperations", 0, 0, 2147483647, Messages.getString("ConnectionProperties.selfDestructOnPingMaxOperations"), "5.1.6", str6, 2147483647);
    this.replicationEnableJMX = new BooleanConnectionProperty("replicationEnableJMX", false, Messages.getString("ConnectionProperties.loadBalanceEnableJMX"), "5.1.27", str6, 2147483647);
    this.serverTimezone = new StringConnectionProperty("serverTimezone", null, Messages.getString("ConnectionProperties.serverTimezone"), "3.0.2", str2, -2147483648);
    this.sessionVariables = new StringConnectionProperty("sessionVariables", null, Messages.getString("ConnectionProperties.sessionVariables"), "3.1.8", str2, 2147483647);
    this.slowQueryThresholdMillis = new IntegerConnectionProperty("slowQueryThresholdMillis", 2000, 0, 2147483647, Messages.getString("ConnectionProperties.slowQueryThresholdMillis"), "3.1.2", str4, 9);
    this.slowQueryThresholdNanos = new LongConnectionProperty("slowQueryThresholdNanos", 0L, Messages.getString("ConnectionProperties.slowQueryThresholdNanos"), "5.0.7", str4, 10);
    this.socketFactoryClassName = new StringConnectionProperty("socketFactory", StandardSocketFactory.class.getName(), Messages.getString("ConnectionProperties.socketFactory"), "3.0.3", str5, 4);
    str7 = Messages.getString("ConnectionProperties.socksProxyHost");
    str6 = NETWORK_CATEGORY;
    this.socksProxyHost = new StringConnectionProperty("socksProxyHost", null, str7, "5.1.34", str6, 1);
    this.socksProxyPort = new IntegerConnectionProperty("socksProxyPort", SocksProxySocketFactory.SOCKS_DEFAULT_PORT, 0, 65535, Messages.getString("ConnectionProperties.socksProxyPort"), "5.1.34", str6, 2);
    this.socketTimeout = new IntegerConnectionProperty("socketTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.socketTimeout"), "3.0.1", str5, 10);
    this.statementInterceptors = new StringConnectionProperty("statementInterceptors", null, Messages.getString("ConnectionProperties.statementInterceptors"), "5.1.1", str2, -2147483648);
    this.strictFloatingPoint = new BooleanConnectionProperty("strictFloatingPoint", false, Messages.getString("ConnectionProperties.strictFloatingPoint"), "3.0.0", str2, -2147483648);
    this.strictUpdates = new BooleanConnectionProperty("strictUpdates", true, Messages.getString("ConnectionProperties.strictUpdates"), "3.0.4", str2, -2147483648);
    this.overrideSupportsIntegrityEnhancementFacility = new BooleanConnectionProperty("overrideSupportsIntegrityEnhancementFacility", false, Messages.getString("ConnectionProperties.overrideSupportsIEF"), "3.1.12", str2, -2147483648);
    this.tcpNoDelay = new BooleanConnectionProperty("tcpNoDelay", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpNoDelay"), "5.0.7", str6, -2147483648);
    this.tcpKeepAlive = new BooleanConnectionProperty("tcpKeepAlive", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpKeepAlive"), "5.0.7", str6, -2147483648);
    this.tcpRcvBuf = new IntegerConnectionProperty("tcpRcvBuf", Integer.parseInt("0"), 0, 2147483647, Messages.getString("ConnectionProperties.tcpSoRcvBuf"), "5.0.7", str6, -2147483648);
    this.tcpSndBuf = new IntegerConnectionProperty("tcpSndBuf", Integer.parseInt("0"), 0, 2147483647, Messages.getString("ConnectionProperties.tcpSoSndBuf"), "5.0.7", str6, -2147483648);
    this.tcpTrafficClass = new IntegerConnectionProperty("tcpTrafficClass", Integer.parseInt("0"), 0, 255, Messages.getString("ConnectionProperties.tcpTrafficClass"), "5.0.7", str6, -2147483648);
    this.tinyInt1isBit = new BooleanConnectionProperty("tinyInt1isBit", true, Messages.getString("ConnectionProperties.tinyInt1isBit"), "3.0.16", str2, -2147483648);
    this.traceProtocol = new BooleanConnectionProperty("traceProtocol", false, Messages.getString("ConnectionProperties.traceProtocol"), "3.1.2", str4, -2147483648);
    this.treatUtilDateAsTimestamp = new BooleanConnectionProperty("treatUtilDateAsTimestamp", true, Messages.getString("ConnectionProperties.treatUtilDateAsTimestamp"), "5.0.5", str2, -2147483648);
    this.transformedBitIsBoolean = new BooleanConnectionProperty("transformedBitIsBoolean", false, Messages.getString("ConnectionProperties.transformedBitIsBoolean"), "3.1.9", str2, -2147483648);
    this.useBlobToStoreUTF8OutsideBMP = new BooleanConnectionProperty("useBlobToStoreUTF8OutsideBMP", false, Messages.getString("ConnectionProperties.useBlobToStoreUTF8OutsideBMP"), "5.1.3", str2, 128);
    this.utf8OutsideBmpExcludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpExcludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpExcludedColumnNamePattern"), "5.1.3", str2, 129);
    this.utf8OutsideBmpIncludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpIncludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpIncludedColumnNamePattern"), "5.1.3", str2, 129);
    this.useCompression = new BooleanConnectionProperty("useCompression", false, Messages.getString("ConnectionProperties.useCompression"), "3.0.17", str5, -2147483648);
    this.useColumnNamesInFindColumn = new BooleanConnectionProperty("useColumnNamesInFindColumn", false, Messages.getString("ConnectionProperties.useColumnNamesInFindColumn"), "5.1.7", str2, 2147483647);
    this.useConfigs = new StringConnectionProperty("useConfigs", null, Messages.getString("ConnectionProperties.useConfigs"), "3.1.5", str5, 2147483647);
    this.useCursorFetch = new BooleanConnectionProperty("useCursorFetch", false, Messages.getString("ConnectionProperties.useCursorFetch"), "5.0.0", str3, 2147483647);
    this.useDynamicCharsetInfo = new BooleanConnectionProperty("useDynamicCharsetInfo", true, Messages.getString("ConnectionProperties.useDynamicCharsetInfo"), "5.0.6", str3, -2147483648);
    this.useDirectRowUnpack = new BooleanConnectionProperty("useDirectRowUnpack", true, "Use newer result set row unpacking code that skips a copy from network buffers  to a MySQL packet instance and instead reads directly into the result set row data buffers.", "5.1.1", str3, -2147483648);
    this.useFastIntParsing = new BooleanConnectionProperty("useFastIntParsing", true, Messages.getString("ConnectionProperties.useFastIntParsing"), "3.1.4", str3, -2147483648);
    this.useFastDateParsing = new BooleanConnectionProperty("useFastDateParsing", true, Messages.getString("ConnectionProperties.useFastDateParsing"), "5.0.5", str3, -2147483648);
    this.useHostsInPrivileges = new BooleanConnectionProperty("useHostsInPrivileges", true, Messages.getString("ConnectionProperties.useHostsInPrivileges"), "3.0.2", str2, -2147483648);
    this.useInformationSchema = new BooleanConnectionProperty("useInformationSchema", false, Messages.getString("ConnectionProperties.useInformationSchema"), "5.0.0", str2, -2147483648);
    this.useJDBCCompliantTimezoneShift = new BooleanConnectionProperty("useJDBCCompliantTimezoneShift", false, Messages.getString("ConnectionProperties.useJDBCCompliantTimezoneShift"), "5.0.0", str2, -2147483648);
    this.useLocalSessionState = new BooleanConnectionProperty("useLocalSessionState", false, Messages.getString("ConnectionProperties.useLocalSessionState"), "3.1.7", str3, 5);
    this.useLocalTransactionState = new BooleanConnectionProperty("useLocalTransactionState", false, Messages.getString("ConnectionProperties.useLocalTransactionState"), "5.1.7", str3, 6);
    this.useLegacyDatetimeCode = new BooleanConnectionProperty("useLegacyDatetimeCode", true, Messages.getString("ConnectionProperties.useLegacyDatetimeCode"), "5.1.6", str2, -2147483648);
    this.sendFractionalSeconds = new BooleanConnectionProperty("sendFractionalSeconds", true, Messages.getString("ConnectionProperties.sendFractionalSeconds"), "5.1.37", str2, -2147483648);
    this.useNanosForElapsedTime = new BooleanConnectionProperty("useNanosForElapsedTime", false, Messages.getString("ConnectionProperties.useNanosForElapsedTime"), "5.0.7", str4, -2147483648);
    this.useOldAliasMetadataBehavior = new BooleanConnectionProperty("useOldAliasMetadataBehavior", false, Messages.getString("ConnectionProperties.useOldAliasMetadataBehavior"), "5.0.4", str2, -2147483648);
    this.useOldUTF8Behavior = new BooleanConnectionProperty("useOldUTF8Behavior", false, Messages.getString("ConnectionProperties.useOldUtf8Behavior"), "3.1.6", str2, -2147483648);
    this.useOldUTF8BehaviorAsBoolean = false;
    this.useOnlyServerErrorMessages = new BooleanConnectionProperty("useOnlyServerErrorMessages", true, Messages.getString("ConnectionProperties.useOnlyServerErrorMessages"), "3.0.15", str2, -2147483648);
    this.useReadAheadInput = new BooleanConnectionProperty("useReadAheadInput", true, Messages.getString("ConnectionProperties.useReadAheadInput"), "3.1.5", str3, -2147483648);
    this.useSqlStateCodes = new BooleanConnectionProperty("useSqlStateCodes", true, Messages.getString("ConnectionProperties.useSqlStateCodes"), "3.1.3", str2, -2147483648);
    this.useSSL = new BooleanConnectionProperty("useSSL", false, Messages.getString("ConnectionProperties.useSSL"), "3.0.2", str1, 2);
    this.useSSPSCompatibleTimezoneShift = new BooleanConnectionProperty("useSSPSCompatibleTimezoneShift", false, Messages.getString("ConnectionProperties.useSSPSCompatibleTimezoneShift"), "5.0.5", str2, -2147483648);
    this.useStreamLengthsInPrepStmts = new BooleanConnectionProperty("useStreamLengthsInPrepStmts", true, Messages.getString("ConnectionProperties.useStreamLengthsInPrepStmts"), "3.0.2", str2, -2147483648);
    this.useTimezone = new BooleanConnectionProperty("useTimezone", false, Messages.getString("ConnectionProperties.useTimezone"), "3.0.2", str2, -2147483648);
    this.useUltraDevWorkAround = new BooleanConnectionProperty("ultraDevHack", false, Messages.getString("ConnectionProperties.ultraDevHack"), "2.0.3", str2, -2147483648);
    this.useUnbufferedInput = new BooleanConnectionProperty("useUnbufferedInput", true, Messages.getString("ConnectionProperties.useUnbufferedInput"), "3.0.11", str2, -2147483648);
    this.useUnicode = new BooleanConnectionProperty("useUnicode", true, Messages.getString("ConnectionProperties.useUnicode"), "1.1g", str2, 0);
    this.useUnicodeAsBoolean = true;
    this.useUsageAdvisor = new BooleanConnectionProperty("useUsageAdvisor", false, Messages.getString("ConnectionProperties.useUsageAdvisor"), "3.1.1", str4, 10);
    this.useUsageAdvisorAsBoolean = false;
    this.yearIsDateType = new BooleanConnectionProperty("yearIsDateType", true, Messages.getString("ConnectionProperties.yearIsDateType"), "3.1.9", str2, -2147483648);
    str7 = Messages.getString("ConnectionProperties.zeroDateTimeBehavior", new Object[] { "exception", "round", "convertToNull" });
    this.zeroDateTimeBehavior = new StringConnectionProperty("zeroDateTimeBehavior", "exception", new String[] { "exception", "round", "convertToNull" }, str7, "3.1.4", str2, -2147483648);
    this.useJvmCharsetConverters = new BooleanConnectionProperty("useJvmCharsetConverters", false, Messages.getString("ConnectionProperties.useJvmCharsetConverters"), "5.0.1", str3, -2147483648);
    this.useGmtMillisForDatetimes = new BooleanConnectionProperty("useGmtMillisForDatetimes", false, Messages.getString("ConnectionProperties.useGmtMillisForDatetimes"), "3.1.12", str2, -2147483648);
    this.dumpMetadataOnColumnNotFound = new BooleanConnectionProperty("dumpMetadataOnColumnNotFound", false, Messages.getString("ConnectionProperties.dumpMetadataOnColumnNotFound"), "3.1.13", str4, -2147483648);
    this.clientCertificateKeyStoreUrl = new StringConnectionProperty("clientCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.clientCertificateKeyStoreUrl"), "5.1.0", str1, 5);
    this.trustCertificateKeyStoreUrl = new StringConnectionProperty("trustCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.trustCertificateKeyStoreUrl"), "5.1.0", str1, 8);
    this.clientCertificateKeyStoreType = new StringConnectionProperty("clientCertificateKeyStoreType", "JKS", Messages.getString("ConnectionProperties.clientCertificateKeyStoreType"), "5.1.0", str1, 6);
    this.clientCertificateKeyStorePassword = new StringConnectionProperty("clientCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.clientCertificateKeyStorePassword"), "5.1.0", str1, 7);
    this.trustCertificateKeyStoreType = new StringConnectionProperty("trustCertificateKeyStoreType", "JKS", Messages.getString("ConnectionProperties.trustCertificateKeyStoreType"), "5.1.0", str1, 9);
    this.trustCertificateKeyStorePassword = new StringConnectionProperty("trustCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.trustCertificateKeyStorePassword"), "5.1.0", str1, 10);
    this.verifyServerCertificate = new BooleanConnectionProperty("verifyServerCertificate", true, Messages.getString("ConnectionProperties.verifyServerCertificate"), "5.1.6", str1, 4);
    this.useAffectedRows = new BooleanConnectionProperty("useAffectedRows", false, Messages.getString("ConnectionProperties.useAffectedRows"), "5.1.7", str2, -2147483648);
    this.passwordCharacterEncoding = new StringConnectionProperty("passwordCharacterEncoding", null, Messages.getString("ConnectionProperties.passwordCharacterEncoding"), "5.1.7", str1, -2147483648);
    this.maxAllowedPacket = new IntegerConnectionProperty("maxAllowedPacket", -1, Messages.getString("ConnectionProperties.maxAllowedPacket"), "5.1.8", str6, -2147483648);
    this.authenticationPlugins = new StringConnectionProperty("authenticationPlugins", null, Messages.getString("ConnectionProperties.authenticationPlugins"), "5.1.19", str5, -2147483648);
    this.disabledAuthenticationPlugins = new StringConnectionProperty("disabledAuthenticationPlugins", null, Messages.getString("ConnectionProperties.disabledAuthenticationPlugins"), "5.1.19", str5, -2147483648);
    this.defaultAuthenticationPlugin = new StringConnectionProperty("defaultAuthenticationPlugin", "com.mysql.jdbc.authentication.MysqlNativePasswordPlugin", Messages.getString("ConnectionProperties.defaultAuthenticationPlugin"), "5.1.19", str5, -2147483648);
    this.disconnectOnExpiredPasswords = new BooleanConnectionProperty("disconnectOnExpiredPasswords", true, Messages.getString("ConnectionProperties.disconnectOnExpiredPasswords"), "5.1.23", str5, -2147483648);
    this.getProceduresReturnsFunctions = new BooleanConnectionProperty("getProceduresReturnsFunctions", true, Messages.getString("ConnectionProperties.getProceduresReturnsFunctions"), "5.1.26", str2, -2147483648);
    this.detectCustomCollations = new BooleanConnectionProperty("detectCustomCollations", false, Messages.getString("ConnectionProperties.detectCustomCollations"), "5.1.29", str2, -2147483648);
    this.serverRSAPublicKeyFile = new StringConnectionProperty("serverRSAPublicKeyFile", null, Messages.getString("ConnectionProperties.serverRSAPublicKeyFile"), "5.1.31", str1, -2147483648);
    this.allowPublicKeyRetrieval = new BooleanConnectionProperty("allowPublicKeyRetrieval", false, Messages.getString("ConnectionProperties.allowPublicKeyRetrieval"), "5.1.31", str1, -2147483648);
    this.dontCheckOnDuplicateKeyUpdateInSQL = new BooleanConnectionProperty("dontCheckOnDuplicateKeyUpdateInSQL", false, Messages.getString("ConnectionProperties.dontCheckOnDuplicateKeyUpdateInSQL"), "5.1.32", str3, -2147483648);
    this.readOnlyPropagatesToServer = new BooleanConnectionProperty("readOnlyPropagatesToServer", true, Messages.getString("ConnectionProperties.readOnlyPropagatesToServer"), "5.1.35", str3, -2147483648);
    this.enabledSSLCipherSuites = new StringConnectionProperty("enabledSSLCipherSuites", null, Messages.getString("ConnectionProperties.enabledSSLCipherSuites"), "5.1.35", str1, 11);
    this.enabledTLSProtocols = new StringConnectionProperty("enabledTLSProtocols", null, Messages.getString("ConnectionProperties.enabledTLSProtocols"), "5.1.44", str1, 12);
    this.enableEscapeProcessing = new BooleanConnectionProperty("enableEscapeProcessing", true, Messages.getString("ConnectionProperties.enableEscapeProcessing"), "5.1.37", str3, -2147483648);
  }
  
  public static DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties paramProperties, int paramInt) throws SQLException {
    return (new ConnectionPropertiesImpl() {
        private static final long serialVersionUID = 4257801713007640581L;
      }).exposeAsDriverPropertyInfoInternal(paramProperties, paramInt);
  }
  
  public DriverPropertyInfo[] exposeAsDriverPropertyInfoInternal(Properties paramProperties, int paramInt) throws SQLException {
    initializeProperties(paramProperties);
    int j = PROPERTY_LIST.size() + paramInt;
    DriverPropertyInfo[] arrayOfDriverPropertyInfo = new DriverPropertyInfo[j];
    int i = paramInt;
    while (i < j) {
      Field field = PROPERTY_LIST.get(i - paramInt);
      try {
        ConnectionProperty connectionProperty = (ConnectionProperty)field.get(this);
        if (paramProperties != null)
          connectionProperty.initializeFrom(paramProperties, getExceptionInterceptor()); 
        arrayOfDriverPropertyInfo[i] = connectionProperty.getAsDriverPropertyInfo();
        i++;
      } catch (IllegalAccessException illegalAccessException) {
        throw SQLError.createSQLException(Messages.getString("ConnectionProperties.InternalPropertiesFailure"), "S1000", getExceptionInterceptor());
      } 
    } 
    return arrayOfDriverPropertyInfo;
  }
  
  public Properties exposeAsProperties(Properties paramProperties) throws SQLException {
    Properties properties = paramProperties;
    if (paramProperties == null)
      properties = new Properties(); 
    int i = PROPERTY_LIST.size();
    byte b = 0;
    while (b < i) {
      Field field = PROPERTY_LIST.get(b);
      try {
        ConnectionProperty connectionProperty = (ConnectionProperty)field.get(this);
        Object object = connectionProperty.getValueAsObject();
        if (object != null)
          properties.setProperty(connectionProperty.getPropertyName(), object.toString()); 
        b++;
      } catch (IllegalAccessException illegalAccessException) {
        throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
      } 
    } 
    return properties;
  }
  
  public Properties exposeAsProperties(Properties paramProperties, boolean paramBoolean) throws SQLException {
    Properties properties = paramProperties;
    if (paramProperties == null)
      properties = new Properties(); 
    int i = PROPERTY_LIST.size();
    byte b = 0;
    while (b < i) {
      Field field = PROPERTY_LIST.get(b);
      try {
        ConnectionProperty connectionProperty = (ConnectionProperty)field.get(this);
        Object object = connectionProperty.getValueAsObject();
        if (object != null && (!paramBoolean || connectionProperty.isExplicitlySet()))
          properties.setProperty(connectionProperty.getPropertyName(), object.toString()); 
        b++;
      } catch (IllegalAccessException illegalAccessException) {
        throw SQLError.createSQLException("Internal properties failure", "S1000", illegalAccessException, getExceptionInterceptor());
      } 
    } 
    return properties;
  }
  
  public String exposeAsXml() throws SQLException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<ConnectionProperties>");
    int k = PROPERTY_LIST.size();
    int j = PROPERTY_CATEGORIES.length;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    byte b2 = 0;
    int i;
    for (i = 0; i < j; i++)
      hashMap.put(PROPERTY_CATEGORIES[i], new XmlMap()); 
    String str2 = Messages.getString("ConnectionProperties.Username");
    String str1 = Messages.getString("ConnectionProperties.allVersions");
    String str3 = CONNECTION_AND_AUTH_CATEGORY;
    StringConnectionProperty stringConnectionProperty2 = new StringConnectionProperty("user", null, str2, str1, str3, -2147483647);
    StringConnectionProperty stringConnectionProperty1 = new StringConnectionProperty("password", null, Messages.getString("ConnectionProperties.Password"), Messages.getString("ConnectionProperties.allVersions"), str3, -2147483646);
    XmlMap xmlMap = (XmlMap)hashMap.get(str3);
    TreeMap<Object, Object> treeMap2 = new TreeMap<Object, Object>();
    treeMap2.put(stringConnectionProperty2.getPropertyName(), stringConnectionProperty2);
    xmlMap.ordered.put(Integer.valueOf(stringConnectionProperty2.getOrder()), treeMap2);
    TreeMap<Object, Object> treeMap1 = new TreeMap<Object, Object>();
    treeMap1.put(stringConnectionProperty1.getPropertyName(), stringConnectionProperty1);
    xmlMap.ordered.put(new Integer(stringConnectionProperty1.getOrder()), treeMap1);
    byte b1 = 0;
    while (true) {
      i = j;
      byte b = b2;
      if (b1 < k) {
        try {
          ConnectionProperty connectionProperty = (ConnectionProperty)((Field)PROPERTY_LIST.get(b1)).get(this);
          xmlMap = (XmlMap)hashMap.get(connectionProperty.getCategoryName());
          i = connectionProperty.getOrder();
          if (i == Integer.MIN_VALUE) {
            xmlMap.alpha.put(connectionProperty.getPropertyName(), connectionProperty);
          } else {
            Integer integer = Integer.valueOf(i);
            Map<Object, Object> map2 = (Map)xmlMap.ordered.get(integer);
            Map<Object, Object> map1 = map2;
            if (map2 == null) {
              map1 = new TreeMap<Object, Object>();
              super();
              xmlMap.ordered.put(integer, map1);
            } 
            map1.put(connectionProperty.getPropertyName(), connectionProperty);
          } 
          b1++;
        } catch (IllegalAccessException illegalAccessException) {
          throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
        } 
        continue;
      } 
      while (b < i) {
        String[] arrayOfString = PROPERTY_CATEGORIES;
        xmlMap = (XmlMap)hashMap.get(arrayOfString[b]);
        stringBuilder.append("\n <PropertyCategory name=\"");
        stringBuilder.append(arrayOfString[b]);
        stringBuilder.append("\">");
        Iterator<Map> iterator = xmlMap.ordered.values().iterator();
        while (true) {
          boolean bool = iterator.hasNext();
          if (bool) {
            for (ConnectionProperty connectionProperty : ((Map)iterator.next()).values()) {
              String str;
              stringBuilder.append("\n  <Property name=\"");
              stringBuilder.append(connectionProperty.getPropertyName());
              stringBuilder.append("\" required=\"");
              if (connectionProperty.required) {
                str = "Yes";
              } else {
                str = "No";
              } 
              stringBuilder.append(str);
              stringBuilder.append("\" default=\"");
              if (connectionProperty.getDefaultValue() != null)
                stringBuilder.append(connectionProperty.getDefaultValue()); 
              stringBuilder.append("\" sortOrder=\"");
              stringBuilder.append(connectionProperty.getOrder());
              stringBuilder.append("\" since=\"");
              stringBuilder.append(connectionProperty.sinceVersion);
              stringBuilder.append("\">\n");
              stringBuilder.append("    ");
              stringBuilder.append(connectionProperty.description.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
              stringBuilder.append("\n  </Property>");
            } 
            continue;
          } 
          for (ConnectionProperty connectionProperty : xmlMap.alpha.values()) {
            String str;
            stringBuilder.append("\n  <Property name=\"");
            stringBuilder.append(connectionProperty.getPropertyName());
            stringBuilder.append("\" required=\"");
            if (connectionProperty.required) {
              str = "Yes";
            } else {
              str = "No";
            } 
            stringBuilder.append(str);
            stringBuilder.append("\" default=\"");
            if (connectionProperty.getDefaultValue() != null)
              stringBuilder.append(connectionProperty.getDefaultValue()); 
            stringBuilder.append("\" sortOrder=\"alpha\" since=\"");
            stringBuilder.append(connectionProperty.sinceVersion);
            stringBuilder.append("\">\n");
            stringBuilder.append("    ");
            stringBuilder.append(connectionProperty.description);
            stringBuilder.append("\n  </Property>");
          } 
          break;
        } 
        stringBuilder.append("\n </PropertyCategory>");
        b++;
      } 
      stringBuilder.append("\n</ConnectionProperties>");
      return stringBuilder.toString();
    } 
  }
  
  public boolean getAllowLoadLocalInfile() {
    return this.allowLoadLocalInfile.getValueAsBoolean();
  }
  
  public boolean getAllowMasterDownConnections() {
    return this.allowMasterDownConnections.getValueAsBoolean();
  }
  
  public boolean getAllowMultiQueries() {
    return this.allowMultiQueries.getValueAsBoolean();
  }
  
  public boolean getAllowNanAndInf() {
    return this.allowNanAndInf.getValueAsBoolean();
  }
  
  public boolean getAllowPublicKeyRetrieval() {
    return this.allowPublicKeyRetrieval.getValueAsBoolean();
  }
  
  public boolean getAllowSlaveDownConnections() {
    return this.allowSlaveDownConnections.getValueAsBoolean();
  }
  
  public boolean getAllowUrlInLocalInfile() {
    return this.allowUrlInLocalInfile.getValueAsBoolean();
  }
  
  public boolean getAlwaysSendSetIsolation() {
    return this.alwaysSendSetIsolation.getValueAsBoolean();
  }
  
  public String getAuthenticationPlugins() {
    return this.authenticationPlugins.getValueAsString();
  }
  
  public boolean getAutoClosePStmtStreams() {
    return this.autoClosePStmtStreams.getValueAsBoolean();
  }
  
  public boolean getAutoDeserialize() {
    return this.autoDeserialize.getValueAsBoolean();
  }
  
  public boolean getAutoGenerateTestcaseScript() {
    return this.autoGenerateTestcaseScriptAsBoolean;
  }
  
  public boolean getAutoReconnectForPools() {
    return this.autoReconnectForPoolsAsBoolean;
  }
  
  public boolean getAutoSlowLog() {
    return this.autoSlowLog.getValueAsBoolean();
  }
  
  public int getBlobSendChunkSize() {
    return this.blobSendChunkSize.getValueAsInt();
  }
  
  public boolean getBlobsAreStrings() {
    return this.blobsAreStrings.getValueAsBoolean();
  }
  
  public boolean getCacheCallableStatements() {
    return this.cacheCallableStatements.getValueAsBoolean();
  }
  
  public boolean getCacheCallableStmts() {
    return getCacheCallableStatements();
  }
  
  public boolean getCacheDefaultTimezone() {
    return this.cacheDefaultTimezone.getValueAsBoolean();
  }
  
  public boolean getCachePrepStmts() {
    return getCachePreparedStatements();
  }
  
  public boolean getCachePreparedStatements() {
    return ((Boolean)this.cachePreparedStatements.getValueAsObject()).booleanValue();
  }
  
  public boolean getCacheResultSetMetadata() {
    return this.cacheResultSetMetaDataAsBoolean;
  }
  
  public boolean getCacheServerConfiguration() {
    return this.cacheServerConfiguration.getValueAsBoolean();
  }
  
  public int getCallableStatementCacheSize() {
    return this.callableStatementCacheSize.getValueAsInt();
  }
  
  public int getCallableStmtCacheSize() {
    return getCallableStatementCacheSize();
  }
  
  public boolean getCapitalizeTypeNames() {
    return this.capitalizeTypeNames.getValueAsBoolean();
  }
  
  public String getCharacterSetResults() {
    return this.characterSetResults.getValueAsString();
  }
  
  public String getClientCertificateKeyStorePassword() {
    return this.clientCertificateKeyStorePassword.getValueAsString();
  }
  
  public String getClientCertificateKeyStoreType() {
    return this.clientCertificateKeyStoreType.getValueAsString();
  }
  
  public String getClientCertificateKeyStoreUrl() {
    return this.clientCertificateKeyStoreUrl.getValueAsString();
  }
  
  public String getClientInfoProvider() {
    return this.clientInfoProvider.getValueAsString();
  }
  
  public String getClobCharacterEncoding() {
    return this.clobCharacterEncoding.getValueAsString();
  }
  
  public boolean getClobberStreamingResults() {
    return this.clobberStreamingResults.getValueAsBoolean();
  }
  
  public boolean getCompensateOnDuplicateKeyUpdateCounts() {
    return this.compensateOnDuplicateKeyUpdateCounts.getValueAsBoolean();
  }
  
  public int getConnectTimeout() {
    return this.connectTimeout.getValueAsInt();
  }
  
  public String getConnectionAttributes() {
    return this.connectionAttributes.getValueAsString();
  }
  
  public String getConnectionCollation() {
    return this.connectionCollation.getValueAsString();
  }
  
  public String getConnectionLifecycleInterceptors() {
    return this.connectionLifecycleInterceptors.getValueAsString();
  }
  
  public boolean getContinueBatchOnError() {
    return this.continueBatchOnError.getValueAsBoolean();
  }
  
  public boolean getCreateDatabaseIfNotExist() {
    return this.createDatabaseIfNotExist.getValueAsBoolean();
  }
  
  public String getDefaultAuthenticationPlugin() {
    return this.defaultAuthenticationPlugin.getValueAsString();
  }
  
  public int getDefaultFetchSize() {
    return this.defaultFetchSize.getValueAsInt();
  }
  
  public boolean getDetectCustomCollations() {
    return this.detectCustomCollations.getValueAsBoolean();
  }
  
  public String getDisabledAuthenticationPlugins() {
    return this.disabledAuthenticationPlugins.getValueAsString();
  }
  
  public boolean getDisconnectOnExpiredPasswords() {
    return this.disconnectOnExpiredPasswords.getValueAsBoolean();
  }
  
  public boolean getDontCheckOnDuplicateKeyUpdateInSQL() {
    return this.dontCheckOnDuplicateKeyUpdateInSQL.getValueAsBoolean();
  }
  
  public boolean getDontTrackOpenResources() {
    return this.dontTrackOpenResources.getValueAsBoolean();
  }
  
  public boolean getDumpMetadataOnColumnNotFound() {
    return this.dumpMetadataOnColumnNotFound.getValueAsBoolean();
  }
  
  public boolean getDumpQueriesOnException() {
    return this.dumpQueriesOnException.getValueAsBoolean();
  }
  
  public boolean getDynamicCalendars() {
    return this.dynamicCalendars.getValueAsBoolean();
  }
  
  public boolean getElideSetAutoCommits() {
    return false;
  }
  
  public boolean getEmptyStringsConvertToZero() {
    return this.emptyStringsConvertToZero.getValueAsBoolean();
  }
  
  public boolean getEmulateLocators() {
    return this.emulateLocators.getValueAsBoolean();
  }
  
  public boolean getEmulateUnsupportedPstmts() {
    return this.emulateUnsupportedPstmts.getValueAsBoolean();
  }
  
  public boolean getEnableEscapeProcessing() {
    return this.enableEscapeProcessing.getValueAsBoolean();
  }
  
  public boolean getEnablePacketDebug() {
    return this.enablePacketDebug.getValueAsBoolean();
  }
  
  public boolean getEnableQueryTimeouts() {
    return this.enableQueryTimeouts.getValueAsBoolean();
  }
  
  public String getEnabledSSLCipherSuites() {
    return this.enabledSSLCipherSuites.getValueAsString();
  }
  
  public String getEnabledTLSProtocols() {
    return this.enabledTLSProtocols.getValueAsString();
  }
  
  public String getEncoding() {
    return this.characterEncodingAsString;
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return null;
  }
  
  public String getExceptionInterceptors() {
    return this.exceptionInterceptors.getValueAsString();
  }
  
  public boolean getExplainSlowQueries() {
    return this.explainSlowQueries.getValueAsBoolean();
  }
  
  public boolean getFailOverReadOnly() {
    return this.failOverReadOnly.getValueAsBoolean();
  }
  
  public boolean getFunctionsNeverReturnBlobs() {
    return this.functionsNeverReturnBlobs.getValueAsBoolean();
  }
  
  public boolean getGatherPerfMetrics() {
    return getGatherPerformanceMetrics();
  }
  
  public boolean getGatherPerformanceMetrics() {
    return this.gatherPerformanceMetrics.getValueAsBoolean();
  }
  
  public boolean getGenerateSimpleParameterMetadata() {
    return this.generateSimpleParameterMetadata.getValueAsBoolean();
  }
  
  public boolean getGetProceduresReturnsFunctions() {
    return this.getProceduresReturnsFunctions.getValueAsBoolean();
  }
  
  public boolean getHighAvailability() {
    return this.highAvailabilityAsBoolean;
  }
  
  public boolean getHoldResultsOpenOverStatementClose() {
    return this.holdResultsOpenOverStatementClose.getValueAsBoolean();
  }
  
  public boolean getIgnoreNonTxTables() {
    return this.ignoreNonTxTables.getValueAsBoolean();
  }
  
  public boolean getIncludeInnodbStatusInDeadlockExceptions() {
    return this.includeInnodbStatusInDeadlockExceptions.getValueAsBoolean();
  }
  
  public boolean getIncludeThreadDumpInDeadlockExceptions() {
    return this.includeThreadDumpInDeadlockExceptions.getValueAsBoolean();
  }
  
  public boolean getIncludeThreadNamesAsStatementComment() {
    return this.includeThreadNamesAsStatementComment.getValueAsBoolean();
  }
  
  public int getInitialTimeout() {
    return this.initialTimeout.getValueAsInt();
  }
  
  public boolean getInteractiveClient() {
    return this.isInteractiveClient.getValueAsBoolean();
  }
  
  public boolean getIsInteractiveClient() {
    return this.isInteractiveClient.getValueAsBoolean();
  }
  
  public boolean getJdbcCompliantTruncation() {
    return this.jdbcCompliantTruncation.getValueAsBoolean();
  }
  
  public boolean getJdbcCompliantTruncationForReads() {
    return this.jdbcCompliantTruncationForReads;
  }
  
  public String getLargeRowSizeThreshold() {
    return this.largeRowSizeThreshold.getValueAsString();
  }
  
  public String getLoadBalanceAutoCommitStatementRegex() {
    return this.loadBalanceAutoCommitStatementRegex.getValueAsString();
  }
  
  public int getLoadBalanceAutoCommitStatementThreshold() {
    return this.loadBalanceAutoCommitStatementThreshold.getValueAsInt();
  }
  
  public int getLoadBalanceBlacklistTimeout() {
    return this.loadBalanceBlacklistTimeout.getValueAsInt();
  }
  
  public String getLoadBalanceConnectionGroup() {
    return this.loadBalanceConnectionGroup.getValueAsString();
  }
  
  public boolean getLoadBalanceEnableJMX() {
    return this.loadBalanceEnableJMX.getValueAsBoolean();
  }
  
  public String getLoadBalanceExceptionChecker() {
    return this.loadBalanceExceptionChecker.getValueAsString();
  }
  
  public int getLoadBalanceHostRemovalGracePeriod() {
    return this.loadBalanceHostRemovalGracePeriod.getValueAsInt();
  }
  
  public int getLoadBalancePingTimeout() {
    return this.loadBalancePingTimeout.getValueAsInt();
  }
  
  public String getLoadBalanceSQLExceptionSubclassFailover() {
    return this.loadBalanceSQLExceptionSubclassFailover.getValueAsString();
  }
  
  public String getLoadBalanceSQLStateFailover() {
    return this.loadBalanceSQLStateFailover.getValueAsString();
  }
  
  public String getLoadBalanceStrategy() {
    return this.loadBalanceStrategy.getValueAsString();
  }
  
  public boolean getLoadBalanceValidateConnectionOnSwapServer() {
    return this.loadBalanceValidateConnectionOnSwapServer.getValueAsBoolean();
  }
  
  public String getLocalSocketAddress() {
    return this.localSocketAddress.getValueAsString();
  }
  
  public int getLocatorFetchBufferSize() {
    return this.locatorFetchBufferSize.getValueAsInt();
  }
  
  public boolean getLogSlowQueries() {
    return this.logSlowQueries.getValueAsBoolean();
  }
  
  public boolean getLogXaCommands() {
    return this.logXaCommands.getValueAsBoolean();
  }
  
  public String getLogger() {
    return this.loggerClassName.getValueAsString();
  }
  
  public String getLoggerClassName() {
    return this.loggerClassName.getValueAsString();
  }
  
  public boolean getMaintainTimeStats() {
    return this.maintainTimeStatsAsBoolean;
  }
  
  public int getMaxAllowedPacket() {
    return this.maxAllowedPacket.getValueAsInt();
  }
  
  public int getMaxQuerySizeToLog() {
    return this.maxQuerySizeToLog.getValueAsInt();
  }
  
  public int getMaxReconnects() {
    return this.maxReconnects.getValueAsInt();
  }
  
  public int getMaxRows() {
    return this.maxRowsAsInt;
  }
  
  public int getMetadataCacheSize() {
    return this.metadataCacheSize.getValueAsInt();
  }
  
  public int getNetTimeoutForStreamingResults() {
    return this.netTimeoutForStreamingResults.getValueAsInt();
  }
  
  public boolean getNoAccessToProcedureBodies() {
    return this.noAccessToProcedureBodies.getValueAsBoolean();
  }
  
  public boolean getNoDatetimeStringSync() {
    return this.noDatetimeStringSync.getValueAsBoolean();
  }
  
  public boolean getNoTimezoneConversionForDateType() {
    return this.noTimezoneConversionForDateType.getValueAsBoolean();
  }
  
  public boolean getNoTimezoneConversionForTimeType() {
    return this.noTimezoneConversionForTimeType.getValueAsBoolean();
  }
  
  public boolean getNullCatalogMeansCurrent() {
    return this.nullCatalogMeansCurrent.getValueAsBoolean();
  }
  
  public boolean getNullNamePatternMatchesAll() {
    return this.nullNamePatternMatchesAll.getValueAsBoolean();
  }
  
  public boolean getOverrideSupportsIntegrityEnhancementFacility() {
    return this.overrideSupportsIntegrityEnhancementFacility.getValueAsBoolean();
  }
  
  public int getPacketDebugBufferSize() {
    return this.packetDebugBufferSize.getValueAsInt();
  }
  
  public boolean getPadCharsWithSpace() {
    return this.padCharsWithSpace.getValueAsBoolean();
  }
  
  public boolean getParanoid() {
    return this.paranoid.getValueAsBoolean();
  }
  
  public String getParseInfoCacheFactory() {
    return this.parseInfoCacheFactory.getValueAsString();
  }
  
  public String getPasswordCharacterEncoding() {
    String str = this.passwordCharacterEncoding.getValueAsString();
    if (str != null)
      return str; 
    if (getUseUnicode()) {
      str = getEncoding();
      if (str != null)
        return str; 
    } 
    return "UTF-8";
  }
  
  public boolean getPedantic() {
    return this.pedantic.getValueAsBoolean();
  }
  
  public boolean getPinGlobalTxToPhysicalConnection() {
    return this.pinGlobalTxToPhysicalConnection.getValueAsBoolean();
  }
  
  public boolean getPopulateInsertRowWithDefaultValues() {
    return this.populateInsertRowWithDefaultValues.getValueAsBoolean();
  }
  
  public int getPrepStmtCacheSize() {
    return getPreparedStatementCacheSize();
  }
  
  public int getPrepStmtCacheSqlLimit() {
    return getPreparedStatementCacheSqlLimit();
  }
  
  public int getPreparedStatementCacheSize() {
    return ((Integer)this.preparedStatementCacheSize.getValueAsObject()).intValue();
  }
  
  public int getPreparedStatementCacheSqlLimit() {
    return ((Integer)this.preparedStatementCacheSqlLimit.getValueAsObject()).intValue();
  }
  
  public boolean getProcessEscapeCodesForPrepStmts() {
    return this.processEscapeCodesForPrepStmts.getValueAsBoolean();
  }
  
  public boolean getProfileSQL() {
    return this.profileSQL.getValueAsBoolean();
  }
  
  public boolean getProfileSql() {
    return this.profileSQLAsBoolean;
  }
  
  public String getProfilerEventHandler() {
    return this.profilerEventHandler.getValueAsString();
  }
  
  public String getPropertiesTransform() {
    return this.propertiesTransform.getValueAsString();
  }
  
  public int getQueriesBeforeRetryMaster() {
    return this.queriesBeforeRetryMaster.getValueAsInt();
  }
  
  public boolean getQueryTimeoutKillsConnection() {
    return this.queryTimeoutKillsConnection.getValueAsBoolean();
  }
  
  public boolean getReadFromMasterWhenNoSlaves() {
    return this.readFromMasterWhenNoSlaves.getValueAsBoolean();
  }
  
  public boolean getReadOnlyPropagatesToServer() {
    return this.readOnlyPropagatesToServer.getValueAsBoolean();
  }
  
  public boolean getReconnectAtTxEnd() {
    return this.reconnectTxAtEndAsBoolean;
  }
  
  public boolean getRelaxAutoCommit() {
    return this.relaxAutoCommit.getValueAsBoolean();
  }
  
  public String getReplicationConnectionGroup() {
    return this.replicationConnectionGroup.getValueAsString();
  }
  
  public boolean getReplicationEnableJMX() {
    return this.replicationEnableJMX.getValueAsBoolean();
  }
  
  public int getReportMetricsIntervalMillis() {
    return this.reportMetricsIntervalMillis.getValueAsInt();
  }
  
  public boolean getRequireSSL() {
    return this.requireSSL.getValueAsBoolean();
  }
  
  public String getResourceId() {
    return this.resourceId.getValueAsString();
  }
  
  public int getResultSetSizeThreshold() {
    return this.resultSetSizeThreshold.getValueAsInt();
  }
  
  public boolean getRetainStatementAfterResultSetClose() {
    return this.retainStatementAfterResultSetClose.getValueAsBoolean();
  }
  
  public int getRetriesAllDown() {
    return this.retriesAllDown.getValueAsInt();
  }
  
  public boolean getRewriteBatchedStatements() {
    return this.rewriteBatchedStatements.getValueAsBoolean();
  }
  
  public boolean getRollbackOnPooledClose() {
    return this.rollbackOnPooledClose.getValueAsBoolean();
  }
  
  public boolean getRoundRobinLoadBalance() {
    return this.roundRobinLoadBalance.getValueAsBoolean();
  }
  
  public boolean getRunningCTS13() {
    return this.runningCTS13.getValueAsBoolean();
  }
  
  public int getSecondsBeforeRetryMaster() {
    return this.secondsBeforeRetryMaster.getValueAsInt();
  }
  
  public int getSelfDestructOnPingMaxOperations() {
    return this.selfDestructOnPingMaxOperations.getValueAsInt();
  }
  
  public int getSelfDestructOnPingSecondsLifetime() {
    return this.selfDestructOnPingSecondsLifetime.getValueAsInt();
  }
  
  public boolean getSendFractionalSeconds() {
    return this.sendFractionalSeconds.getValueAsBoolean();
  }
  
  public String getServerAffinityOrder() {
    return this.serverAffinityOrder.getValueAsString();
  }
  
  public String getServerConfigCacheFactory() {
    return this.serverConfigCacheFactory.getValueAsString();
  }
  
  public String getServerRSAPublicKeyFile() {
    return this.serverRSAPublicKeyFile.getValueAsString();
  }
  
  public String getServerTimezone() {
    return this.serverTimezone.getValueAsString();
  }
  
  public String getSessionVariables() {
    return this.sessionVariables.getValueAsString();
  }
  
  public int getSlowQueryThresholdMillis() {
    return this.slowQueryThresholdMillis.getValueAsInt();
  }
  
  public long getSlowQueryThresholdNanos() {
    return this.slowQueryThresholdNanos.getValueAsLong();
  }
  
  public String getSocketFactory() {
    return getSocketFactoryClassName();
  }
  
  public String getSocketFactoryClassName() {
    return this.socketFactoryClassName.getValueAsString();
  }
  
  public int getSocketTimeout() {
    return this.socketTimeout.getValueAsInt();
  }
  
  public String getSocksProxyHost() {
    return this.socksProxyHost.getValueAsString();
  }
  
  public int getSocksProxyPort() {
    return this.socksProxyPort.getValueAsInt();
  }
  
  public String getStatementInterceptors() {
    return this.statementInterceptors.getValueAsString();
  }
  
  public boolean getStrictFloatingPoint() {
    return this.strictFloatingPoint.getValueAsBoolean();
  }
  
  public boolean getStrictUpdates() {
    return this.strictUpdates.getValueAsBoolean();
  }
  
  public boolean getTcpKeepAlive() {
    return this.tcpKeepAlive.getValueAsBoolean();
  }
  
  public boolean getTcpNoDelay() {
    return this.tcpNoDelay.getValueAsBoolean();
  }
  
  public int getTcpRcvBuf() {
    return this.tcpRcvBuf.getValueAsInt();
  }
  
  public int getTcpSndBuf() {
    return this.tcpSndBuf.getValueAsInt();
  }
  
  public int getTcpTrafficClass() {
    return this.tcpTrafficClass.getValueAsInt();
  }
  
  public boolean getTinyInt1isBit() {
    return this.tinyInt1isBit.getValueAsBoolean();
  }
  
  public boolean getTraceProtocol() {
    return this.traceProtocol.getValueAsBoolean();
  }
  
  public boolean getTransformedBitIsBoolean() {
    return this.transformedBitIsBoolean.getValueAsBoolean();
  }
  
  public boolean getTreatUtilDateAsTimestamp() {
    return this.treatUtilDateAsTimestamp.getValueAsBoolean();
  }
  
  public String getTrustCertificateKeyStorePassword() {
    return this.trustCertificateKeyStorePassword.getValueAsString();
  }
  
  public String getTrustCertificateKeyStoreType() {
    return this.trustCertificateKeyStoreType.getValueAsString();
  }
  
  public String getTrustCertificateKeyStoreUrl() {
    return this.trustCertificateKeyStoreUrl.getValueAsString();
  }
  
  public boolean getUltraDevHack() {
    return getUseUltraDevWorkAround();
  }
  
  public boolean getUseAffectedRows() {
    return this.useAffectedRows.getValueAsBoolean();
  }
  
  public boolean getUseBlobToStoreUTF8OutsideBMP() {
    return this.useBlobToStoreUTF8OutsideBMP.getValueAsBoolean();
  }
  
  public boolean getUseColumnNamesInFindColumn() {
    return this.useColumnNamesInFindColumn.getValueAsBoolean();
  }
  
  public boolean getUseCompression() {
    return this.useCompression.getValueAsBoolean();
  }
  
  public String getUseConfigs() {
    return this.useConfigs.getValueAsString();
  }
  
  public boolean getUseCursorFetch() {
    return this.useCursorFetch.getValueAsBoolean();
  }
  
  public boolean getUseDirectRowUnpack() {
    return this.useDirectRowUnpack.getValueAsBoolean();
  }
  
  public boolean getUseDynamicCharsetInfo() {
    return this.useDynamicCharsetInfo.getValueAsBoolean();
  }
  
  public boolean getUseFastDateParsing() {
    return this.useFastDateParsing.getValueAsBoolean();
  }
  
  public boolean getUseFastIntParsing() {
    return this.useFastIntParsing.getValueAsBoolean();
  }
  
  public boolean getUseGmtMillisForDatetimes() {
    return this.useGmtMillisForDatetimes.getValueAsBoolean();
  }
  
  public boolean getUseHostsInPrivileges() {
    return this.useHostsInPrivileges.getValueAsBoolean();
  }
  
  public boolean getUseInformationSchema() {
    return this.useInformationSchema.getValueAsBoolean();
  }
  
  public boolean getUseJDBCCompliantTimezoneShift() {
    return this.useJDBCCompliantTimezoneShift.getValueAsBoolean();
  }
  
  public boolean getUseJvmCharsetConverters() {
    return this.useJvmCharsetConverters.getValueAsBoolean();
  }
  
  public boolean getUseLegacyDatetimeCode() {
    return this.useLegacyDatetimeCode.getValueAsBoolean();
  }
  
  public boolean getUseLocalSessionState() {
    return this.useLocalSessionState.getValueAsBoolean();
  }
  
  public boolean getUseLocalTransactionState() {
    return this.useLocalTransactionState.getValueAsBoolean();
  }
  
  public boolean getUseNanosForElapsedTime() {
    return this.useNanosForElapsedTime.getValueAsBoolean();
  }
  
  public boolean getUseOldAliasMetadataBehavior() {
    return this.useOldAliasMetadataBehavior.getValueAsBoolean();
  }
  
  public boolean getUseOldUTF8Behavior() {
    return this.useOldUTF8BehaviorAsBoolean;
  }
  
  public boolean getUseOnlyServerErrorMessages() {
    return this.useOnlyServerErrorMessages.getValueAsBoolean();
  }
  
  public boolean getUseReadAheadInput() {
    return this.useReadAheadInput.getValueAsBoolean();
  }
  
  public boolean getUseSSL() {
    return this.useSSL.getValueAsBoolean();
  }
  
  public boolean getUseSSPSCompatibleTimezoneShift() {
    return this.useSSPSCompatibleTimezoneShift.getValueAsBoolean();
  }
  
  public boolean getUseServerPrepStmts() {
    return getUseServerPreparedStmts();
  }
  
  public boolean getUseServerPreparedStmts() {
    return this.detectServerPreparedStmts.getValueAsBoolean();
  }
  
  public boolean getUseSqlStateCodes() {
    return this.useSqlStateCodes.getValueAsBoolean();
  }
  
  public boolean getUseStreamLengthsInPrepStmts() {
    return this.useStreamLengthsInPrepStmts.getValueAsBoolean();
  }
  
  public boolean getUseTimezone() {
    return this.useTimezone.getValueAsBoolean();
  }
  
  public boolean getUseUltraDevWorkAround() {
    return this.useUltraDevWorkAround.getValueAsBoolean();
  }
  
  public boolean getUseUnbufferedInput() {
    return this.useUnbufferedInput.getValueAsBoolean();
  }
  
  public boolean getUseUnicode() {
    return this.useUnicodeAsBoolean;
  }
  
  public boolean getUseUsageAdvisor() {
    return this.useUsageAdvisorAsBoolean;
  }
  
  public String getUtf8OutsideBmpExcludedColumnNamePattern() {
    return this.utf8OutsideBmpExcludedColumnNamePattern.getValueAsString();
  }
  
  public String getUtf8OutsideBmpIncludedColumnNamePattern() {
    return this.utf8OutsideBmpIncludedColumnNamePattern.getValueAsString();
  }
  
  public boolean getVerifyServerCertificate() {
    return this.verifyServerCertificate.getValueAsBoolean();
  }
  
  public boolean getYearIsDateType() {
    return this.yearIsDateType.getValueAsBoolean();
  }
  
  public String getZeroDateTimeBehavior() {
    return this.zeroDateTimeBehavior.getValueAsString();
  }
  
  public void initializeFromRef(Reference paramReference) throws SQLException {
    int i = PROPERTY_LIST.size();
    byte b = 0;
    while (b < i) {
      Field field = PROPERTY_LIST.get(b);
      try {
        ConnectionProperty connectionProperty = (ConnectionProperty)field.get(this);
        if (paramReference != null)
          connectionProperty.initializeFrom(paramReference, getExceptionInterceptor()); 
        b++;
      } catch (IllegalAccessException illegalAccessException) {
        throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
      } 
    } 
    postInitialization();
  }
  
  public void initializeProperties(Properties paramProperties) throws SQLException {
    if (paramProperties != null) {
      String str = paramProperties.getProperty("profileSql");
      if (str != null)
        paramProperties.put("profileSQL", str); 
      paramProperties = (Properties)paramProperties.clone();
      paramProperties.remove("HOST");
      paramProperties.remove("user");
      paramProperties.remove("password");
      paramProperties.remove("DBNAME");
      paramProperties.remove("PORT");
      paramProperties.remove("profileSql");
      int i = PROPERTY_LIST.size();
      byte b = 0;
      while (b < i) {
        Field field = PROPERTY_LIST.get(b);
        try {
          ((ConnectionProperty)field.get(this)).initializeFrom(paramProperties, getExceptionInterceptor());
          b++;
        } catch (IllegalAccessException illegalAccessException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("ConnectionProperties.unableToInitDriverProperties"));
          stringBuilder.append(illegalAccessException.toString());
          throw SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
        } 
      } 
      postInitialization();
    } 
  }
  
  public boolean isUseSSLExplicit() {
    return this.useSSL.wasExplicitlySet;
  }
  
  public void postInitialization() throws SQLException {
    if (this.profileSql.getValueAsObject() != null)
      this.profileSQL.initializeFrom(this.profileSql.getValueAsObject().toString(), getExceptionInterceptor()); 
    this.reconnectTxAtEndAsBoolean = ((Boolean)this.reconnectAtTxEnd.getValueAsObject()).booleanValue();
    if (getMaxRows() == 0)
      this.maxRows.setValueAsObject(Integer.valueOf(-1)); 
    String str = (String)this.characterEncoding.getValueAsObject();
    if (str != null)
      try {
        StringUtils.getBytes("abc", str);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unsupportedCharacterEncoding", new Object[] { str }), "0S100", getExceptionInterceptor());
      }  
    if (((Boolean)this.cacheResultSetMetadata.getValueAsObject()).booleanValue())
      try {
        Class.forName("java.util.LinkedHashMap");
      } catch (ClassNotFoundException classNotFoundException) {
        this.cacheResultSetMetadata.setValue(false);
      }  
    this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
    this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
    this.characterEncodingAsString = (String)this.characterEncoding.getValueAsObject();
    this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
    this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
    this.maxRowsAsInt = ((Integer)this.maxRows.getValueAsObject()).intValue();
    this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
    this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
    this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
    this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
    this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
    this.jdbcCompliantTruncationForReads = getJdbcCompliantTruncation();
    if (getUseCursorFetch())
      setDetectServerPreparedStmts(true); 
  }
  
  public void setAllowLoadLocalInfile(boolean paramBoolean) {
    this.allowLoadLocalInfile.setValue(paramBoolean);
  }
  
  public void setAllowMasterDownConnections(boolean paramBoolean) {
    this.allowMasterDownConnections.setValue(paramBoolean);
  }
  
  public void setAllowMultiQueries(boolean paramBoolean) {
    this.allowMultiQueries.setValue(paramBoolean);
  }
  
  public void setAllowNanAndInf(boolean paramBoolean) {
    this.allowNanAndInf.setValue(paramBoolean);
  }
  
  public void setAllowPublicKeyRetrieval(boolean paramBoolean) throws SQLException {
    if (this.allowPublicKeyRetrieval.getUpdateCount() <= 0) {
      this.allowPublicKeyRetrieval.setValue(paramBoolean);
      return;
    } 
    throw SQLError.createSQLException(Messages.getString("ConnectionProperties.dynamicChangeIsNotAllowed", new Object[] { "'allowPublicKeyRetrieval'" }), "S1009", null);
  }
  
  public void setAllowSlaveDownConnections(boolean paramBoolean) {
    this.allowSlaveDownConnections.setValue(paramBoolean);
  }
  
  public void setAllowUrlInLocalInfile(boolean paramBoolean) {
    this.allowUrlInLocalInfile.setValue(paramBoolean);
  }
  
  public void setAlwaysSendSetIsolation(boolean paramBoolean) {
    this.alwaysSendSetIsolation.setValue(paramBoolean);
  }
  
  public void setAuthenticationPlugins(String paramString) {
    this.authenticationPlugins.setValue(paramString);
  }
  
  public void setAutoClosePStmtStreams(boolean paramBoolean) {
    this.autoClosePStmtStreams.setValue(paramBoolean);
  }
  
  public void setAutoDeserialize(boolean paramBoolean) {
    this.autoDeserialize.setValue(paramBoolean);
  }
  
  public void setAutoGenerateTestcaseScript(boolean paramBoolean) {
    this.autoGenerateTestcaseScript.setValue(paramBoolean);
    this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
  }
  
  public void setAutoReconnect(boolean paramBoolean) {
    this.autoReconnect.setValue(paramBoolean);
  }
  
  public void setAutoReconnectForConnectionPools(boolean paramBoolean) {
    this.autoReconnectForPools.setValue(paramBoolean);
    this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
  }
  
  public void setAutoReconnectForPools(boolean paramBoolean) {
    this.autoReconnectForPools.setValue(paramBoolean);
  }
  
  public void setAutoSlowLog(boolean paramBoolean) {
    this.autoSlowLog.setValue(paramBoolean);
  }
  
  public void setBlobSendChunkSize(String paramString) throws SQLException {
    this.blobSendChunkSize.setValue(paramString, getExceptionInterceptor());
  }
  
  public void setBlobsAreStrings(boolean paramBoolean) {
    this.blobsAreStrings.setValue(paramBoolean);
  }
  
  public void setCacheCallableStatements(boolean paramBoolean) {
    this.cacheCallableStatements.setValue(paramBoolean);
  }
  
  public void setCacheCallableStmts(boolean paramBoolean) {
    setCacheCallableStatements(paramBoolean);
  }
  
  public void setCacheDefaultTimezone(boolean paramBoolean) {
    this.cacheDefaultTimezone.setValue(paramBoolean);
  }
  
  public void setCachePrepStmts(boolean paramBoolean) {
    setCachePreparedStatements(paramBoolean);
  }
  
  public void setCachePreparedStatements(boolean paramBoolean) {
    this.cachePreparedStatements.setValue(paramBoolean);
  }
  
  public void setCacheResultSetMetadata(boolean paramBoolean) {
    this.cacheResultSetMetadata.setValue(paramBoolean);
    this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
  }
  
  public void setCacheServerConfiguration(boolean paramBoolean) {
    this.cacheServerConfiguration.setValue(paramBoolean);
  }
  
  public void setCallableStatementCacheSize(int paramInt) throws SQLException {
    this.callableStatementCacheSize.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setCallableStmtCacheSize(int paramInt) throws SQLException {
    setCallableStatementCacheSize(paramInt);
  }
  
  public void setCapitalizeDBMDTypes(boolean paramBoolean) {
    this.capitalizeTypeNames.setValue(paramBoolean);
  }
  
  public void setCapitalizeTypeNames(boolean paramBoolean) {
    this.capitalizeTypeNames.setValue(paramBoolean);
  }
  
  public void setCharacterEncoding(String paramString) {
    this.characterEncoding.setValue(paramString);
  }
  
  public void setCharacterSetResults(String paramString) {
    this.characterSetResults.setValue(paramString);
  }
  
  public void setClientCertificateKeyStorePassword(String paramString) {
    this.clientCertificateKeyStorePassword.setValue(paramString);
  }
  
  public void setClientCertificateKeyStoreType(String paramString) {
    this.clientCertificateKeyStoreType.setValue(paramString);
  }
  
  public void setClientCertificateKeyStoreUrl(String paramString) {
    this.clientCertificateKeyStoreUrl.setValue(paramString);
  }
  
  public void setClientInfoProvider(String paramString) {
    this.clientInfoProvider.setValue(paramString);
  }
  
  public void setClobCharacterEncoding(String paramString) {
    this.clobCharacterEncoding.setValue(paramString);
  }
  
  public void setClobberStreamingResults(boolean paramBoolean) {
    this.clobberStreamingResults.setValue(paramBoolean);
  }
  
  public void setCompensateOnDuplicateKeyUpdateCounts(boolean paramBoolean) {
    this.compensateOnDuplicateKeyUpdateCounts.setValue(paramBoolean);
  }
  
  public void setConnectTimeout(int paramInt) throws SQLException {
    this.connectTimeout.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setConnectionAttributes(String paramString) {
    this.connectionAttributes.setValue(paramString);
  }
  
  public void setConnectionCollation(String paramString) {
    this.connectionCollation.setValue(paramString);
  }
  
  public void setConnectionLifecycleInterceptors(String paramString) {
    this.connectionLifecycleInterceptors.setValue(paramString);
  }
  
  public void setContinueBatchOnError(boolean paramBoolean) {
    this.continueBatchOnError.setValue(paramBoolean);
  }
  
  public void setCreateDatabaseIfNotExist(boolean paramBoolean) {
    this.createDatabaseIfNotExist.setValue(paramBoolean);
  }
  
  public void setDefaultAuthenticationPlugin(String paramString) {
    this.defaultAuthenticationPlugin.setValue(paramString);
  }
  
  public void setDefaultFetchSize(int paramInt) throws SQLException {
    this.defaultFetchSize.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setDetectCustomCollations(boolean paramBoolean) {
    this.detectCustomCollations.setValue(paramBoolean);
  }
  
  public void setDetectServerPreparedStmts(boolean paramBoolean) {
    this.detectServerPreparedStmts.setValue(paramBoolean);
  }
  
  public void setDisabledAuthenticationPlugins(String paramString) {
    this.disabledAuthenticationPlugins.setValue(paramString);
  }
  
  public void setDisconnectOnExpiredPasswords(boolean paramBoolean) {
    this.disconnectOnExpiredPasswords.setValue(paramBoolean);
  }
  
  public void setDontCheckOnDuplicateKeyUpdateInSQL(boolean paramBoolean) {
    this.dontCheckOnDuplicateKeyUpdateInSQL.setValue(paramBoolean);
  }
  
  public void setDontTrackOpenResources(boolean paramBoolean) {
    this.dontTrackOpenResources.setValue(paramBoolean);
  }
  
  public void setDumpMetadataOnColumnNotFound(boolean paramBoolean) {
    this.dumpMetadataOnColumnNotFound.setValue(paramBoolean);
  }
  
  public void setDumpQueriesOnException(boolean paramBoolean) {
    this.dumpQueriesOnException.setValue(paramBoolean);
  }
  
  public void setDynamicCalendars(boolean paramBoolean) {
    this.dynamicCalendars.setValue(paramBoolean);
  }
  
  public void setElideSetAutoCommits(boolean paramBoolean) {
    this.elideSetAutoCommits.setValue(paramBoolean);
  }
  
  public void setEmptyStringsConvertToZero(boolean paramBoolean) {
    this.emptyStringsConvertToZero.setValue(paramBoolean);
  }
  
  public void setEmulateLocators(boolean paramBoolean) {
    this.emulateLocators.setValue(paramBoolean);
  }
  
  public void setEmulateUnsupportedPstmts(boolean paramBoolean) {
    this.emulateUnsupportedPstmts.setValue(paramBoolean);
  }
  
  public void setEnableEscapeProcessing(boolean paramBoolean) {
    this.enableEscapeProcessing.setValue(paramBoolean);
  }
  
  public void setEnablePacketDebug(boolean paramBoolean) {
    this.enablePacketDebug.setValue(paramBoolean);
  }
  
  public void setEnableQueryTimeouts(boolean paramBoolean) {
    this.enableQueryTimeouts.setValue(paramBoolean);
  }
  
  public void setEnabledSSLCipherSuites(String paramString) {
    this.enabledSSLCipherSuites.setValue(paramString);
  }
  
  public void setEnabledTLSProtocols(String paramString) {
    this.enabledTLSProtocols.setValue(paramString);
  }
  
  public void setEncoding(String paramString) {
    this.characterEncoding.setValue(paramString);
    this.characterEncodingAsString = this.characterEncoding.getValueAsString();
  }
  
  public void setExceptionInterceptors(String paramString) {
    this.exceptionInterceptors.setValue(paramString);
  }
  
  public void setExplainSlowQueries(boolean paramBoolean) {
    this.explainSlowQueries.setValue(paramBoolean);
  }
  
  public void setFailOverReadOnly(boolean paramBoolean) {
    this.failOverReadOnly.setValue(paramBoolean);
  }
  
  public void setFunctionsNeverReturnBlobs(boolean paramBoolean) {
    this.functionsNeverReturnBlobs.setValue(paramBoolean);
  }
  
  public void setGatherPerfMetrics(boolean paramBoolean) {
    setGatherPerformanceMetrics(paramBoolean);
  }
  
  public void setGatherPerformanceMetrics(boolean paramBoolean) {
    this.gatherPerformanceMetrics.setValue(paramBoolean);
  }
  
  public void setGenerateSimpleParameterMetadata(boolean paramBoolean) {
    this.generateSimpleParameterMetadata.setValue(paramBoolean);
  }
  
  public void setGetProceduresReturnsFunctions(boolean paramBoolean) {
    this.getProceduresReturnsFunctions.setValue(paramBoolean);
  }
  
  public void setHighAvailability(boolean paramBoolean) {
    this.autoReconnect.setValue(paramBoolean);
    this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
  }
  
  public void setHoldResultsOpenOverStatementClose(boolean paramBoolean) {
    this.holdResultsOpenOverStatementClose.setValue(paramBoolean);
  }
  
  public void setIgnoreNonTxTables(boolean paramBoolean) {
    this.ignoreNonTxTables.setValue(paramBoolean);
  }
  
  public void setIncludeInnodbStatusInDeadlockExceptions(boolean paramBoolean) {
    this.includeInnodbStatusInDeadlockExceptions.setValue(paramBoolean);
  }
  
  public void setIncludeThreadDumpInDeadlockExceptions(boolean paramBoolean) {
    this.includeThreadDumpInDeadlockExceptions.setValue(paramBoolean);
  }
  
  public void setIncludeThreadNamesAsStatementComment(boolean paramBoolean) {
    this.includeThreadNamesAsStatementComment.setValue(paramBoolean);
  }
  
  public void setInitialTimeout(int paramInt) throws SQLException {
    this.initialTimeout.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setInteractiveClient(boolean paramBoolean) {
    setIsInteractiveClient(paramBoolean);
  }
  
  public void setIsInteractiveClient(boolean paramBoolean) {
    this.isInteractiveClient.setValue(paramBoolean);
  }
  
  public void setJdbcCompliantTruncation(boolean paramBoolean) {
    this.jdbcCompliantTruncation.setValue(paramBoolean);
  }
  
  public void setJdbcCompliantTruncationForReads(boolean paramBoolean) {
    this.jdbcCompliantTruncationForReads = paramBoolean;
  }
  
  public void setLargeRowSizeThreshold(String paramString) throws SQLException {
    this.largeRowSizeThreshold.setValue(paramString, getExceptionInterceptor());
  }
  
  public void setLoadBalanceAutoCommitStatementRegex(String paramString) {
    this.loadBalanceAutoCommitStatementRegex.setValue(paramString);
  }
  
  public void setLoadBalanceAutoCommitStatementThreshold(int paramInt) throws SQLException {
    this.loadBalanceAutoCommitStatementThreshold.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setLoadBalanceBlacklistTimeout(int paramInt) throws SQLException {
    this.loadBalanceBlacklistTimeout.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setLoadBalanceConnectionGroup(String paramString) {
    this.loadBalanceConnectionGroup.setValue(paramString);
  }
  
  public void setLoadBalanceEnableJMX(boolean paramBoolean) {
    this.loadBalanceEnableJMX.setValue(paramBoolean);
  }
  
  public void setLoadBalanceExceptionChecker(String paramString) {
    this.loadBalanceExceptionChecker.setValue(paramString);
  }
  
  public void setLoadBalanceHostRemovalGracePeriod(int paramInt) throws SQLException {
    this.loadBalanceHostRemovalGracePeriod.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setLoadBalancePingTimeout(int paramInt) throws SQLException {
    this.loadBalancePingTimeout.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setLoadBalanceSQLExceptionSubclassFailover(String paramString) {
    this.loadBalanceSQLExceptionSubclassFailover.setValue(paramString);
  }
  
  public void setLoadBalanceSQLStateFailover(String paramString) {
    this.loadBalanceSQLStateFailover.setValue(paramString);
  }
  
  public void setLoadBalanceStrategy(String paramString) {
    this.loadBalanceStrategy.setValue(paramString);
  }
  
  public void setLoadBalanceValidateConnectionOnSwapServer(boolean paramBoolean) {
    this.loadBalanceValidateConnectionOnSwapServer.setValue(paramBoolean);
  }
  
  public void setLocalSocketAddress(String paramString) {
    this.localSocketAddress.setValue(paramString);
  }
  
  public void setLocatorFetchBufferSize(String paramString) throws SQLException {
    this.locatorFetchBufferSize.setValue(paramString, getExceptionInterceptor());
  }
  
  public void setLogSlowQueries(boolean paramBoolean) {
    this.logSlowQueries.setValue(paramBoolean);
  }
  
  public void setLogXaCommands(boolean paramBoolean) {
    this.logXaCommands.setValue(paramBoolean);
  }
  
  public void setLogger(String paramString) {
    this.loggerClassName.setValueAsObject(paramString);
  }
  
  public void setLoggerClassName(String paramString) {
    this.loggerClassName.setValue(paramString);
  }
  
  public void setMaintainTimeStats(boolean paramBoolean) {
    this.maintainTimeStats.setValue(paramBoolean);
    this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
  }
  
  public void setMaxAllowedPacket(int paramInt) throws SQLException {
    this.maxAllowedPacket.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setMaxQuerySizeToLog(int paramInt) throws SQLException {
    this.maxQuerySizeToLog.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setMaxReconnects(int paramInt) throws SQLException {
    this.maxReconnects.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setMaxRows(int paramInt) throws SQLException {
    this.maxRows.setValue(paramInt, getExceptionInterceptor());
    this.maxRowsAsInt = this.maxRows.getValueAsInt();
  }
  
  public void setMetadataCacheSize(int paramInt) throws SQLException {
    this.metadataCacheSize.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setNetTimeoutForStreamingResults(int paramInt) throws SQLException {
    this.netTimeoutForStreamingResults.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setNoAccessToProcedureBodies(boolean paramBoolean) {
    this.noAccessToProcedureBodies.setValue(paramBoolean);
  }
  
  public void setNoDatetimeStringSync(boolean paramBoolean) {
    this.noDatetimeStringSync.setValue(paramBoolean);
  }
  
  public void setNoTimezoneConversionForDateType(boolean paramBoolean) {
    this.noTimezoneConversionForDateType.setValue(paramBoolean);
  }
  
  public void setNoTimezoneConversionForTimeType(boolean paramBoolean) {
    this.noTimezoneConversionForTimeType.setValue(paramBoolean);
  }
  
  public void setNullCatalogMeansCurrent(boolean paramBoolean) {
    this.nullCatalogMeansCurrent.setValue(paramBoolean);
  }
  
  public void setNullNamePatternMatchesAll(boolean paramBoolean) {
    this.nullNamePatternMatchesAll.setValue(paramBoolean);
  }
  
  public void setOverrideSupportsIntegrityEnhancementFacility(boolean paramBoolean) {
    this.overrideSupportsIntegrityEnhancementFacility.setValue(paramBoolean);
  }
  
  public void setPacketDebugBufferSize(int paramInt) throws SQLException {
    this.packetDebugBufferSize.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setPadCharsWithSpace(boolean paramBoolean) {
    this.padCharsWithSpace.setValue(paramBoolean);
  }
  
  public void setParanoid(boolean paramBoolean) {
    this.paranoid.setValue(paramBoolean);
  }
  
  public void setParseInfoCacheFactory(String paramString) {
    this.parseInfoCacheFactory.setValue(paramString);
  }
  
  public void setPasswordCharacterEncoding(String paramString) {
    this.passwordCharacterEncoding.setValue(paramString);
  }
  
  public void setPedantic(boolean paramBoolean) {
    this.pedantic.setValue(paramBoolean);
  }
  
  public void setPinGlobalTxToPhysicalConnection(boolean paramBoolean) {
    this.pinGlobalTxToPhysicalConnection.setValue(paramBoolean);
  }
  
  public void setPopulateInsertRowWithDefaultValues(boolean paramBoolean) {
    this.populateInsertRowWithDefaultValues.setValue(paramBoolean);
  }
  
  public void setPrepStmtCacheSize(int paramInt) throws SQLException {
    setPreparedStatementCacheSize(paramInt);
  }
  
  public void setPrepStmtCacheSqlLimit(int paramInt) throws SQLException {
    setPreparedStatementCacheSqlLimit(paramInt);
  }
  
  public void setPreparedStatementCacheSize(int paramInt) throws SQLException {
    this.preparedStatementCacheSize.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setPreparedStatementCacheSqlLimit(int paramInt) throws SQLException {
    this.preparedStatementCacheSqlLimit.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setProcessEscapeCodesForPrepStmts(boolean paramBoolean) {
    this.processEscapeCodesForPrepStmts.setValue(paramBoolean);
  }
  
  public void setProfileSQL(boolean paramBoolean) {
    this.profileSQL.setValue(paramBoolean);
  }
  
  public void setProfileSql(boolean paramBoolean) {
    this.profileSQL.setValue(paramBoolean);
    this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
  }
  
  public void setProfilerEventHandler(String paramString) {
    this.profilerEventHandler.setValue(paramString);
  }
  
  public void setPropertiesTransform(String paramString) {
    this.propertiesTransform.setValue(paramString);
  }
  
  public void setQueriesBeforeRetryMaster(int paramInt) throws SQLException {
    this.queriesBeforeRetryMaster.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setQueryTimeoutKillsConnection(boolean paramBoolean) {
    this.queryTimeoutKillsConnection.setValue(paramBoolean);
  }
  
  public void setReadFromMasterWhenNoSlaves(boolean paramBoolean) {
    this.readFromMasterWhenNoSlaves.setValue(paramBoolean);
  }
  
  public void setReadOnlyPropagatesToServer(boolean paramBoolean) {
    this.readOnlyPropagatesToServer.setValue(paramBoolean);
  }
  
  public void setReconnectAtTxEnd(boolean paramBoolean) {
    this.reconnectAtTxEnd.setValue(paramBoolean);
    this.reconnectTxAtEndAsBoolean = this.reconnectAtTxEnd.getValueAsBoolean();
  }
  
  public void setRelaxAutoCommit(boolean paramBoolean) {
    this.relaxAutoCommit.setValue(paramBoolean);
  }
  
  public void setReplicationConnectionGroup(String paramString) {
    this.replicationConnectionGroup.setValue(paramString);
  }
  
  public void setReplicationEnableJMX(boolean paramBoolean) {
    this.replicationEnableJMX.setValue(paramBoolean);
  }
  
  public void setReportMetricsIntervalMillis(int paramInt) throws SQLException {
    this.reportMetricsIntervalMillis.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setRequireSSL(boolean paramBoolean) {
    this.requireSSL.setValue(paramBoolean);
  }
  
  public void setResourceId(String paramString) {
    this.resourceId.setValue(paramString);
  }
  
  public void setResultSetSizeThreshold(int paramInt) throws SQLException {
    this.resultSetSizeThreshold.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setRetainStatementAfterResultSetClose(boolean paramBoolean) {
    this.retainStatementAfterResultSetClose.setValue(paramBoolean);
  }
  
  public void setRetriesAllDown(int paramInt) throws SQLException {
    this.retriesAllDown.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setRewriteBatchedStatements(boolean paramBoolean) {
    this.rewriteBatchedStatements.setValue(paramBoolean);
  }
  
  public void setRollbackOnPooledClose(boolean paramBoolean) {
    this.rollbackOnPooledClose.setValue(paramBoolean);
  }
  
  public void setRoundRobinLoadBalance(boolean paramBoolean) {
    this.roundRobinLoadBalance.setValue(paramBoolean);
  }
  
  public void setRunningCTS13(boolean paramBoolean) {
    this.runningCTS13.setValue(paramBoolean);
  }
  
  public void setSecondsBeforeRetryMaster(int paramInt) throws SQLException {
    this.secondsBeforeRetryMaster.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setSelfDestructOnPingMaxOperations(int paramInt) throws SQLException {
    this.selfDestructOnPingMaxOperations.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setSelfDestructOnPingSecondsLifetime(int paramInt) throws SQLException {
    this.selfDestructOnPingSecondsLifetime.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setSendFractionalSeconds(boolean paramBoolean) {
    this.sendFractionalSeconds.setValue(paramBoolean);
  }
  
  public void setServerAffinityOrder(String paramString) {
    this.serverAffinityOrder.setValue(paramString);
  }
  
  public void setServerConfigCacheFactory(String paramString) {
    this.serverConfigCacheFactory.setValue(paramString);
  }
  
  public void setServerRSAPublicKeyFile(String paramString) throws SQLException {
    if (this.serverRSAPublicKeyFile.getUpdateCount() <= 0) {
      this.serverRSAPublicKeyFile.setValue(paramString);
      return;
    } 
    throw SQLError.createSQLException(Messages.getString("ConnectionProperties.dynamicChangeIsNotAllowed", new Object[] { "'serverRSAPublicKeyFile'" }), "S1009", null);
  }
  
  public void setServerTimezone(String paramString) {
    this.serverTimezone.setValue(paramString);
  }
  
  public void setSessionVariables(String paramString) {
    this.sessionVariables.setValue(paramString);
  }
  
  public void setSlowQueryThresholdMillis(int paramInt) throws SQLException {
    this.slowQueryThresholdMillis.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setSlowQueryThresholdNanos(long paramLong) throws SQLException {
    this.slowQueryThresholdNanos.setValue(paramLong, getExceptionInterceptor());
  }
  
  public void setSocketFactory(String paramString) {
    setSocketFactoryClassName(paramString);
  }
  
  public void setSocketFactoryClassName(String paramString) {
    this.socketFactoryClassName.setValue(paramString);
  }
  
  public void setSocketTimeout(int paramInt) throws SQLException {
    this.socketTimeout.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setSocksProxyHost(String paramString) {
    this.socksProxyHost.setValue(paramString);
  }
  
  public void setSocksProxyPort(int paramInt) throws SQLException {
    this.socksProxyPort.setValue(paramInt, null);
  }
  
  public void setStatementInterceptors(String paramString) {
    this.statementInterceptors.setValue(paramString);
  }
  
  public void setStrictFloatingPoint(boolean paramBoolean) {
    this.strictFloatingPoint.setValue(paramBoolean);
  }
  
  public void setStrictUpdates(boolean paramBoolean) {
    this.strictUpdates.setValue(paramBoolean);
  }
  
  public void setTcpKeepAlive(boolean paramBoolean) {
    this.tcpKeepAlive.setValue(paramBoolean);
  }
  
  public void setTcpNoDelay(boolean paramBoolean) {
    this.tcpNoDelay.setValue(paramBoolean);
  }
  
  public void setTcpRcvBuf(int paramInt) throws SQLException {
    this.tcpRcvBuf.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setTcpSndBuf(int paramInt) throws SQLException {
    this.tcpSndBuf.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setTcpTrafficClass(int paramInt) throws SQLException {
    this.tcpTrafficClass.setValue(paramInt, getExceptionInterceptor());
  }
  
  public void setTinyInt1isBit(boolean paramBoolean) {
    this.tinyInt1isBit.setValue(paramBoolean);
  }
  
  public void setTraceProtocol(boolean paramBoolean) {
    this.traceProtocol.setValue(paramBoolean);
  }
  
  public void setTransformedBitIsBoolean(boolean paramBoolean) {
    this.transformedBitIsBoolean.setValue(paramBoolean);
  }
  
  public void setTreatUtilDateAsTimestamp(boolean paramBoolean) {
    this.treatUtilDateAsTimestamp.setValue(paramBoolean);
  }
  
  public void setTrustCertificateKeyStorePassword(String paramString) {
    this.trustCertificateKeyStorePassword.setValue(paramString);
  }
  
  public void setTrustCertificateKeyStoreType(String paramString) {
    this.trustCertificateKeyStoreType.setValue(paramString);
  }
  
  public void setTrustCertificateKeyStoreUrl(String paramString) {
    this.trustCertificateKeyStoreUrl.setValue(paramString);
  }
  
  public void setUltraDevHack(boolean paramBoolean) {
    setUseUltraDevWorkAround(paramBoolean);
  }
  
  public void setUseAffectedRows(boolean paramBoolean) {
    this.useAffectedRows.setValue(paramBoolean);
  }
  
  public void setUseBlobToStoreUTF8OutsideBMP(boolean paramBoolean) {
    this.useBlobToStoreUTF8OutsideBMP.setValue(paramBoolean);
  }
  
  public void setUseColumnNamesInFindColumn(boolean paramBoolean) {
    this.useColumnNamesInFindColumn.setValue(paramBoolean);
  }
  
  public void setUseCompression(boolean paramBoolean) {
    this.useCompression.setValue(paramBoolean);
  }
  
  public void setUseConfigs(String paramString) {
    this.useConfigs.setValue(paramString);
  }
  
  public void setUseCursorFetch(boolean paramBoolean) {
    this.useCursorFetch.setValue(paramBoolean);
  }
  
  public void setUseDirectRowUnpack(boolean paramBoolean) {
    this.useDirectRowUnpack.setValue(paramBoolean);
  }
  
  public void setUseDynamicCharsetInfo(boolean paramBoolean) {
    this.useDynamicCharsetInfo.setValue(paramBoolean);
  }
  
  public void setUseFastDateParsing(boolean paramBoolean) {
    this.useFastDateParsing.setValue(paramBoolean);
  }
  
  public void setUseFastIntParsing(boolean paramBoolean) {
    this.useFastIntParsing.setValue(paramBoolean);
  }
  
  public void setUseGmtMillisForDatetimes(boolean paramBoolean) {
    this.useGmtMillisForDatetimes.setValue(paramBoolean);
  }
  
  public void setUseHostsInPrivileges(boolean paramBoolean) {
    this.useHostsInPrivileges.setValue(paramBoolean);
  }
  
  public void setUseInformationSchema(boolean paramBoolean) {
    this.useInformationSchema.setValue(paramBoolean);
  }
  
  public void setUseJDBCCompliantTimezoneShift(boolean paramBoolean) {
    this.useJDBCCompliantTimezoneShift.setValue(paramBoolean);
  }
  
  public void setUseJvmCharsetConverters(boolean paramBoolean) {
    this.useJvmCharsetConverters.setValue(paramBoolean);
  }
  
  public void setUseLegacyDatetimeCode(boolean paramBoolean) {
    this.useLegacyDatetimeCode.setValue(paramBoolean);
  }
  
  public void setUseLocalSessionState(boolean paramBoolean) {
    this.useLocalSessionState.setValue(paramBoolean);
  }
  
  public void setUseLocalTransactionState(boolean paramBoolean) {
    this.useLocalTransactionState.setValue(paramBoolean);
  }
  
  public void setUseNanosForElapsedTime(boolean paramBoolean) {
    this.useNanosForElapsedTime.setValue(paramBoolean);
  }
  
  public void setUseOldAliasMetadataBehavior(boolean paramBoolean) {
    this.useOldAliasMetadataBehavior.setValue(paramBoolean);
  }
  
  public void setUseOldUTF8Behavior(boolean paramBoolean) {
    this.useOldUTF8Behavior.setValue(paramBoolean);
    this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
  }
  
  public void setUseOnlyServerErrorMessages(boolean paramBoolean) {
    this.useOnlyServerErrorMessages.setValue(paramBoolean);
  }
  
  public void setUseReadAheadInput(boolean paramBoolean) {
    this.useReadAheadInput.setValue(paramBoolean);
  }
  
  public void setUseSSL(boolean paramBoolean) {
    this.useSSL.setValue(paramBoolean);
  }
  
  public void setUseSSPSCompatibleTimezoneShift(boolean paramBoolean) {
    this.useSSPSCompatibleTimezoneShift.setValue(paramBoolean);
  }
  
  public void setUseServerPrepStmts(boolean paramBoolean) {
    setUseServerPreparedStmts(paramBoolean);
  }
  
  public void setUseServerPreparedStmts(boolean paramBoolean) {
    this.detectServerPreparedStmts.setValue(paramBoolean);
  }
  
  public void setUseSqlStateCodes(boolean paramBoolean) {
    this.useSqlStateCodes.setValue(paramBoolean);
  }
  
  public void setUseStreamLengthsInPrepStmts(boolean paramBoolean) {
    this.useStreamLengthsInPrepStmts.setValue(paramBoolean);
  }
  
  public void setUseTimezone(boolean paramBoolean) {
    this.useTimezone.setValue(paramBoolean);
  }
  
  public void setUseUltraDevWorkAround(boolean paramBoolean) {
    this.useUltraDevWorkAround.setValue(paramBoolean);
  }
  
  public void setUseUnbufferedInput(boolean paramBoolean) {
    this.useUnbufferedInput.setValue(paramBoolean);
  }
  
  public void setUseUnicode(boolean paramBoolean) {
    this.useUnicode.setValue(paramBoolean);
    this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
  }
  
  public void setUseUsageAdvisor(boolean paramBoolean) {
    this.useUsageAdvisor.setValue(paramBoolean);
    this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
  }
  
  public void setUtf8OutsideBmpExcludedColumnNamePattern(String paramString) {
    this.utf8OutsideBmpExcludedColumnNamePattern.setValue(paramString);
  }
  
  public void setUtf8OutsideBmpIncludedColumnNamePattern(String paramString) {
    this.utf8OutsideBmpIncludedColumnNamePattern.setValue(paramString);
  }
  
  public void setVerifyServerCertificate(boolean paramBoolean) {
    this.verifyServerCertificate.setValue(paramBoolean);
  }
  
  public void setYearIsDateType(boolean paramBoolean) {
    this.yearIsDateType.setValue(paramBoolean);
  }
  
  public void setZeroDateTimeBehavior(String paramString) {
    this.zeroDateTimeBehavior.setValue(paramString);
  }
  
  public void storeToRef(Reference paramReference) throws SQLException {
    int i = PROPERTY_LIST.size();
    byte b = 0;
    while (b < i) {
      Field field = PROPERTY_LIST.get(b);
      try {
        ConnectionProperty connectionProperty = (ConnectionProperty)field.get(this);
        if (paramReference != null)
          connectionProperty.storeTo(paramReference); 
        b++;
      } catch (IllegalAccessException illegalAccessException) {
        throw SQLError.createSQLException(Messages.getString("ConnectionProperties.errorNotExpected"), getExceptionInterceptor());
      } 
    } 
  }
  
  public boolean useUnbufferedInput() {
    return this.useUnbufferedInput.getValueAsBoolean();
  }
  
  public static class BooleanConnectionProperty extends ConnectionProperty implements Serializable {
    private static final long serialVersionUID = 2540132501709159404L;
    
    public BooleanConnectionProperty(String param1String1, boolean param1Boolean, String param1String2, String param1String3, String param1String4, int param1Int) {
      super(param1String1, Boolean.valueOf(param1Boolean), null, 0, 0, param1String2, param1String3, param1String4, param1Int);
    }
    
    public String[] getAllowableValues() {
      return new String[] { "true", "false", "yes", "no" };
    }
    
    public boolean getValueAsBoolean() {
      return ((Boolean)this.valueAsObject).booleanValue();
    }
    
    public boolean hasValueConstraints() {
      return true;
    }
    
    public void initializeFrom(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      if (param1String != null) {
        boolean bool;
        validateStringValues(param1String, param1ExceptionInterceptor);
        if (param1String.equalsIgnoreCase("TRUE") || param1String.equalsIgnoreCase("YES")) {
          bool = true;
        } else {
          bool = false;
        } 
        this.valueAsObject = Boolean.valueOf(bool);
        this.wasExplicitlySet = true;
      } else {
        this.valueAsObject = this.defaultValue;
      } 
      this.updateCount++;
    }
    
    public boolean isRangeBased() {
      return false;
    }
    
    public void setValue(boolean param1Boolean) {
      this.valueAsObject = Boolean.valueOf(param1Boolean);
      this.wasExplicitlySet = true;
      this.updateCount++;
    }
  }
  
  public static abstract class ConnectionProperty implements Serializable {
    public static final long serialVersionUID = -6644853639584478367L;
    
    public String[] allowableValues;
    
    public String categoryName;
    
    public Object defaultValue;
    
    public String description;
    
    public int lowerBound;
    
    public int order;
    
    public String propertyName;
    
    public boolean required;
    
    public String sinceVersion;
    
    public int updateCount = 0;
    
    public int upperBound;
    
    public Object valueAsObject;
    
    public boolean wasExplicitlySet = false;
    
    public ConnectionProperty() {}
    
    public ConnectionProperty(String param1String1, Object param1Object, String[] param1ArrayOfString, int param1Int1, int param1Int2, String param1String2, String param1String3, String param1String4, int param1Int3) {
      this.description = param1String2;
      this.propertyName = param1String1;
      this.defaultValue = param1Object;
      this.valueAsObject = param1Object;
      this.allowableValues = param1ArrayOfString;
      this.lowerBound = param1Int1;
      this.upperBound = param1Int2;
      this.required = false;
      this.sinceVersion = param1String3;
      this.categoryName = param1String4;
      this.order = param1Int3;
    }
    
    public String[] getAllowableValues() {
      return this.allowableValues;
    }
    
    public DriverPropertyInfo getAsDriverPropertyInfo() {
      String str2 = this.propertyName;
      String str1 = null;
      DriverPropertyInfo driverPropertyInfo = new DriverPropertyInfo(str2, null);
      driverPropertyInfo.choices = getAllowableValues();
      Object object = this.valueAsObject;
      if (object != null)
        str1 = object.toString(); 
      driverPropertyInfo.value = str1;
      driverPropertyInfo.required = this.required;
      driverPropertyInfo.description = this.description;
      return driverPropertyInfo;
    }
    
    public String getCategoryName() {
      return this.categoryName;
    }
    
    public Object getDefaultValue() {
      return this.defaultValue;
    }
    
    public int getLowerBound() {
      return this.lowerBound;
    }
    
    public int getOrder() {
      return this.order;
    }
    
    public String getPropertyName() {
      return this.propertyName;
    }
    
    public int getUpdateCount() {
      return this.updateCount;
    }
    
    public int getUpperBound() {
      return this.upperBound;
    }
    
    public Object getValueAsObject() {
      return this.valueAsObject;
    }
    
    public abstract boolean hasValueConstraints();
    
    public abstract void initializeFrom(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException;
    
    public void initializeFrom(Properties param1Properties, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      String str = param1Properties.getProperty(getPropertyName());
      param1Properties.remove(getPropertyName());
      initializeFrom(str, param1ExceptionInterceptor);
    }
    
    public void initializeFrom(Reference param1Reference, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      RefAddr refAddr = param1Reference.get(getPropertyName());
      if (refAddr != null)
        initializeFrom((String)refAddr.getContent(), param1ExceptionInterceptor); 
    }
    
    public boolean isExplicitlySet() {
      return this.wasExplicitlySet;
    }
    
    public abstract boolean isRangeBased();
    
    public void setCategoryName(String param1String) {
      this.categoryName = param1String;
    }
    
    public void setOrder(int param1Int) {
      this.order = param1Int;
    }
    
    public void setValueAsObject(Object param1Object) {
      this.valueAsObject = param1Object;
      this.updateCount++;
    }
    
    public void storeTo(Reference param1Reference) {
      if (getValueAsObject() != null)
        param1Reference.add(new StringRefAddr(getPropertyName(), getValueAsObject().toString())); 
    }
    
    public void validateStringValues(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      String[] arrayOfString = getAllowableValues();
      if (param1String == null)
        return; 
      if (arrayOfString == null || arrayOfString.length == 0)
        return; 
      byte b;
      for (b = 0; b < arrayOfString.length; b++) {
        if (arrayOfString[b] != null && arrayOfString[b].equalsIgnoreCase(param1String))
          return; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The connection property '");
      stringBuilder.append(getPropertyName());
      stringBuilder.append("' only accepts values of the form: ");
      if (arrayOfString.length != 0) {
        stringBuilder.append("'");
        stringBuilder.append(arrayOfString[0]);
        stringBuilder.append("'");
        for (b = 1; b < arrayOfString.length - 1; b++) {
          stringBuilder.append(", ");
          stringBuilder.append("'");
          stringBuilder.append(arrayOfString[b]);
          stringBuilder.append("'");
        } 
        stringBuilder.append(" or '");
        stringBuilder.append(arrayOfString[arrayOfString.length - 1]);
        stringBuilder.append("'");
      } 
      stringBuilder.append(". The value '");
      stringBuilder.append(param1String);
      stringBuilder.append("' is not in this set.");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", param1ExceptionInterceptor);
    }
  }
  
  public static class IntegerConnectionProperty extends ConnectionProperty implements Serializable {
    private static final long serialVersionUID = -3004305481796850832L;
    
    public int multiplier = 1;
    
    public IntegerConnectionProperty(String param1String1, int param1Int1, int param1Int2, int param1Int3, String param1String2, String param1String3, String param1String4, int param1Int4) {
      super(param1String1, Integer.valueOf(param1Int1), null, param1Int2, param1Int3, param1String2, param1String3, param1String4, param1Int4);
    }
    
    public IntegerConnectionProperty(String param1String1, int param1Int1, String param1String2, String param1String3, String param1String4, int param1Int2) {
      this(param1String1, param1Int1, 0, 0, param1String2, param1String3, param1String4, param1Int2);
    }
    
    public IntegerConnectionProperty(String param1String1, Object param1Object, String[] param1ArrayOfString, int param1Int1, int param1Int2, String param1String2, String param1String3, String param1String4, int param1Int3) {
      super(param1String1, param1Object, param1ArrayOfString, param1Int1, param1Int2, param1String2, param1String3, param1String4, param1Int3);
    }
    
    public String[] getAllowableValues() {
      return null;
    }
    
    public int getLowerBound() {
      return this.lowerBound;
    }
    
    public int getUpperBound() {
      return this.upperBound;
    }
    
    public int getValueAsInt() {
      return ((Integer)this.valueAsObject).intValue();
    }
    
    public boolean hasValueConstraints() {
      return false;
    }
    
    public void initializeFrom(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      if (param1String != null) {
        try {
          setValue((int)(Double.valueOf(param1String).doubleValue() * this.multiplier), param1String, param1ExceptionInterceptor);
        } catch (NumberFormatException numberFormatException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("The connection property '");
          stringBuilder.append(getPropertyName());
          stringBuilder.append("' only accepts integer values. The value '");
          stringBuilder.append(param1String);
          stringBuilder.append("' can not be converted to an integer.");
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", param1ExceptionInterceptor);
        } 
      } else {
        this.valueAsObject = this.defaultValue;
      } 
      this.updateCount++;
    }
    
    public boolean isRangeBased() {
      boolean bool;
      if (getUpperBound() != getLowerBound()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setValue(int param1Int, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      setValue(param1Int, null, param1ExceptionInterceptor);
    }
    
    public void setValue(int param1Int, String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      if (isRangeBased() && (param1Int < getLowerBound() || param1Int > getUpperBound())) {
        Integer integer;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The connection property '");
        stringBuilder.append(getPropertyName());
        stringBuilder.append("' only accepts integer values in the range of ");
        stringBuilder.append(getLowerBound());
        stringBuilder.append(" - ");
        stringBuilder.append(getUpperBound());
        stringBuilder.append(", the value '");
        String str = param1String;
        if (param1String == null)
          integer = Integer.valueOf(param1Int); 
        stringBuilder.append(integer);
        stringBuilder.append("' exceeds this range.");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", param1ExceptionInterceptor);
      } 
      this.valueAsObject = Integer.valueOf(param1Int);
      this.wasExplicitlySet = true;
      this.updateCount++;
    }
  }
  
  public static class LongConnectionProperty extends IntegerConnectionProperty {
    private static final long serialVersionUID = 6068572984340480895L;
    
    public LongConnectionProperty(String param1String1, long param1Long1, long param1Long2, long param1Long3, String param1String2, String param1String3, String param1String4, int param1Int) {
      super(param1String1, Long.valueOf(param1Long1), null, (int)param1Long2, (int)param1Long3, param1String2, param1String3, param1String4, param1Int);
    }
    
    public LongConnectionProperty(String param1String1, long param1Long, String param1String2, String param1String3, String param1String4, int param1Int) {
      this(param1String1, param1Long, 0L, 0L, param1String2, param1String3, param1String4, param1Int);
    }
    
    public long getValueAsLong() {
      return ((Long)this.valueAsObject).longValue();
    }
    
    public void initializeFrom(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      if (param1String != null) {
        try {
          setValue(Double.valueOf(param1String).longValue(), param1String, param1ExceptionInterceptor);
        } catch (NumberFormatException numberFormatException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("The connection property '");
          stringBuilder.append(getPropertyName());
          stringBuilder.append("' only accepts long integer values. The value '");
          stringBuilder.append(param1String);
          stringBuilder.append("' can not be converted to a long integer.");
          throw SQLError.createSQLException(stringBuilder.toString(), "S1009", param1ExceptionInterceptor);
        } 
      } else {
        this.valueAsObject = this.defaultValue;
      } 
      this.updateCount++;
    }
    
    public void setValue(long param1Long, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      setValue(param1Long, null, param1ExceptionInterceptor);
    }
    
    public void setValue(long param1Long, String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      if (isRangeBased() && (param1Long < getLowerBound() || param1Long > getUpperBound())) {
        Long long_;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The connection property '");
        stringBuilder.append(getPropertyName());
        stringBuilder.append("' only accepts long integer values in the range of ");
        stringBuilder.append(getLowerBound());
        stringBuilder.append(" - ");
        stringBuilder.append(getUpperBound());
        stringBuilder.append(", the value '");
        String str = param1String;
        if (param1String == null)
          long_ = Long.valueOf(param1Long); 
        stringBuilder.append(long_);
        stringBuilder.append("' exceeds this range.");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", param1ExceptionInterceptor);
      } 
      this.valueAsObject = Long.valueOf(param1Long);
      this.wasExplicitlySet = true;
      this.updateCount++;
    }
  }
  
  public static class MemorySizeConnectionProperty extends IntegerConnectionProperty implements Serializable {
    private static final long serialVersionUID = 7351065128998572656L;
    
    private String valueAsString;
    
    public MemorySizeConnectionProperty(String param1String1, int param1Int1, int param1Int2, int param1Int3, String param1String2, String param1String3, String param1String4, int param1Int4) {
      super(param1String1, param1Int1, param1Int2, param1Int3, param1String2, param1String3, param1String4, param1Int4);
    }
    
    public String getValueAsString() {
      return this.valueAsString;
    }
    
    public void initializeFrom(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: putfield valueAsString : Ljava/lang/String;
      //   5: aload_0
      //   6: iconst_1
      //   7: putfield multiplier : I
      //   10: aload_1
      //   11: astore_3
      //   12: aload_1
      //   13: ifnull -> 247
      //   16: aload_1
      //   17: ldc 'k'
      //   19: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   22: ifne -> 228
      //   25: aload_1
      //   26: ldc 'K'
      //   28: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   31: ifne -> 228
      //   34: aload_1
      //   35: ldc 'kb'
      //   37: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   40: ifne -> 228
      //   43: aload_1
      //   44: ldc 'Kb'
      //   46: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   49: ifne -> 228
      //   52: aload_1
      //   53: ldc 'kB'
      //   55: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   58: ifne -> 228
      //   61: aload_1
      //   62: ldc 'KB'
      //   64: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   67: ifeq -> 73
      //   70: goto -> 228
      //   73: aload_1
      //   74: ldc 'm'
      //   76: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   79: ifne -> 207
      //   82: aload_1
      //   83: ldc 'M'
      //   85: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   88: ifne -> 207
      //   91: aload_1
      //   92: ldc 'mb'
      //   94: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   97: ifne -> 207
      //   100: aload_1
      //   101: ldc 'Mb'
      //   103: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   106: ifne -> 207
      //   109: aload_1
      //   110: ldc 'mB'
      //   112: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   115: ifne -> 207
      //   118: aload_1
      //   119: ldc 'MB'
      //   121: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   124: ifeq -> 130
      //   127: goto -> 207
      //   130: aload_1
      //   131: ldc 'g'
      //   133: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   136: ifne -> 186
      //   139: aload_1
      //   140: ldc 'G'
      //   142: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   145: ifne -> 186
      //   148: aload_1
      //   149: ldc 'gb'
      //   151: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   154: ifne -> 186
      //   157: aload_1
      //   158: ldc 'Gb'
      //   160: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   163: ifne -> 186
      //   166: aload_1
      //   167: ldc 'gB'
      //   169: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   172: ifne -> 186
      //   175: aload_1
      //   176: astore_3
      //   177: aload_1
      //   178: ldc 'GB'
      //   180: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   183: ifeq -> 247
      //   186: aload_0
      //   187: ldc 1073741824
      //   189: putfield multiplier : I
      //   192: aload_1
      //   193: iconst_0
      //   194: aload_1
      //   195: ldc 'g'
      //   197: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
      //   200: invokevirtual substring : (II)Ljava/lang/String;
      //   203: astore_3
      //   204: goto -> 247
      //   207: aload_0
      //   208: ldc 1048576
      //   210: putfield multiplier : I
      //   213: aload_1
      //   214: iconst_0
      //   215: aload_1
      //   216: ldc 'm'
      //   218: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
      //   221: invokevirtual substring : (II)Ljava/lang/String;
      //   224: astore_3
      //   225: goto -> 247
      //   228: aload_0
      //   229: sipush #1024
      //   232: putfield multiplier : I
      //   235: aload_1
      //   236: iconst_0
      //   237: aload_1
      //   238: ldc 'k'
      //   240: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
      //   243: invokevirtual substring : (II)Ljava/lang/String;
      //   246: astore_3
      //   247: aload_0
      //   248: aload_3
      //   249: aload_2
      //   250: invokespecial initializeFrom : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)V
      //   253: return
    }
    
    public void setValue(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      initializeFrom(param1String, param1ExceptionInterceptor);
    }
  }
  
  public static class StringConnectionProperty extends ConnectionProperty implements Serializable {
    private static final long serialVersionUID = 5432127962785948272L;
    
    public StringConnectionProperty(String param1String1, String param1String2, String param1String3, String param1String4, String param1String5, int param1Int) {
      this(param1String1, param1String2, null, param1String3, param1String4, param1String5, param1Int);
    }
    
    public StringConnectionProperty(String param1String1, String param1String2, String[] param1ArrayOfString, String param1String3, String param1String4, String param1String5, int param1Int) {
      super(param1String1, param1String2, param1ArrayOfString, 0, 0, param1String3, param1String4, param1String5, param1Int);
    }
    
    public String getValueAsString() {
      return (String)this.valueAsObject;
    }
    
    public boolean hasValueConstraints() {
      boolean bool;
      String[] arrayOfString = this.allowableValues;
      if (arrayOfString != null && arrayOfString.length > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void initializeFrom(String param1String, ExceptionInterceptor param1ExceptionInterceptor) throws SQLException {
      if (param1String != null) {
        validateStringValues(param1String, param1ExceptionInterceptor);
        this.valueAsObject = param1String;
        this.wasExplicitlySet = true;
      } else {
        this.valueAsObject = this.defaultValue;
      } 
      this.updateCount++;
    }
    
    public boolean isRangeBased() {
      return false;
    }
    
    public void setValue(String param1String) {
      this.valueAsObject = param1String;
      this.wasExplicitlySet = true;
      this.updateCount++;
    }
  }
  
  public class XmlMap {
    public Map<String, ConnectionPropertiesImpl.ConnectionProperty> alpha = new TreeMap<String, ConnectionPropertiesImpl.ConnectionProperty>();
    
    public Map<Integer, Map<String, ConnectionPropertiesImpl.ConnectionProperty>> ordered = new TreeMap<Integer, Map<String, ConnectionPropertiesImpl.ConnectionProperty>>();
    
    public final ConnectionPropertiesImpl this$0;
  }
}
