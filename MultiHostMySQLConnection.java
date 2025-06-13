package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executor;

public class MultiHostMySQLConnection implements MySQLConnection {
  public MultiHostConnectionProxy thisAsProxy;
  
  public MultiHostMySQLConnection(MultiHostConnectionProxy paramMultiHostConnectionProxy) {
    this.thisAsProxy = paramMultiHostConnectionProxy;
  }
  
  public void abort(Executor paramExecutor) throws SQLException {
    getActiveMySQLConnection().abort(paramExecutor);
  }
  
  public void abortInternal() throws SQLException {
    getActiveMySQLConnection().abortInternal();
  }
  
  public void changeUser(String paramString1, String paramString2) throws SQLException {
    getActiveMySQLConnection().changeUser(paramString1, paramString2);
  }
  
  public void checkClosed() throws SQLException {
    getActiveMySQLConnection().checkClosed();
  }
  
  @Deprecated
  public void clearHasTriedMaster() {
    getActiveMySQLConnection().clearHasTriedMaster();
  }
  
  public void clearWarnings() throws SQLException {
    getActiveMySQLConnection().clearWarnings();
  }
  
  public PreparedStatement clientPrepareStatement(String paramString) throws SQLException {
    return getActiveMySQLConnection().clientPrepareStatement(paramString);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt) throws SQLException {
    return getActiveMySQLConnection().clientPrepareStatement(paramString, paramInt);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    return getActiveMySQLConnection().clientPrepareStatement(paramString, paramInt1, paramInt2);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return getActiveMySQLConnection().clientPrepareStatement(paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    return getActiveMySQLConnection().clientPrepareStatement(paramString, paramArrayOfint);
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    return getActiveMySQLConnection().clientPrepareStatement(paramString, paramArrayOfString);
  }
  
  public void close() throws SQLException {
    getActiveMySQLConnection().close();
  }
  
  public void commit() throws SQLException {
    getActiveMySQLConnection().commit();
  }
  
  public void createNewIO(boolean paramBoolean) throws SQLException {
    getActiveMySQLConnection().createNewIO(paramBoolean);
  }
  
  public Statement createStatement() throws SQLException {
    return getActiveMySQLConnection().createStatement();
  }
  
  public Statement createStatement(int paramInt1, int paramInt2) throws SQLException {
    return getActiveMySQLConnection().createStatement(paramInt1, paramInt2);
  }
  
  public Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return getActiveMySQLConnection().createStatement(paramInt1, paramInt2, paramInt3);
  }
  
  public void decachePreparedStatement(ServerPreparedStatement paramServerPreparedStatement) throws SQLException {
    getActiveMySQLConnection().decachePreparedStatement(paramServerPreparedStatement);
  }
  
  public void dumpTestcaseQuery(String paramString) {
    getActiveMySQLConnection().dumpTestcaseQuery(paramString);
  }
  
  public Connection duplicate() throws SQLException {
    return getActiveMySQLConnection().duplicate();
  }
  
  public ResultSetInternalMethods execSQL(StatementImpl paramStatementImpl, String paramString1, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3, boolean paramBoolean, String paramString2, Field[] paramArrayOfField) throws SQLException {
    return getActiveMySQLConnection().execSQL(paramStatementImpl, paramString1, paramInt1, paramBuffer, paramInt2, paramInt3, paramBoolean, paramString2, paramArrayOfField);
  }
  
  public ResultSetInternalMethods execSQL(StatementImpl paramStatementImpl, String paramString1, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3, boolean paramBoolean1, String paramString2, Field[] paramArrayOfField, boolean paramBoolean2) throws SQLException {
    return getActiveMySQLConnection().execSQL(paramStatementImpl, paramString1, paramInt1, paramBuffer, paramInt2, paramInt3, paramBoolean1, paramString2, paramArrayOfField, paramBoolean2);
  }
  
  public String exposeAsXml() throws SQLException {
    return getActiveMySQLConnection().exposeAsXml();
  }
  
  public String extractSqlFromPacket(String paramString, Buffer paramBuffer, int paramInt) throws SQLException {
    return getActiveMySQLConnection().extractSqlFromPacket(paramString, paramBuffer, paramInt);
  }
  
  public StringBuilder generateConnectionCommentBlock(StringBuilder paramStringBuilder) {
    return getActiveMySQLConnection().generateConnectionCommentBlock(paramStringBuilder);
  }
  
  public MySQLConnection getActiveMySQLConnection() {
    synchronized (this.thisAsProxy) {
      return this.thisAsProxy.currentConnection;
    } 
  }
  
  public int getActiveStatementCount() {
    return getActiveMySQLConnection().getActiveStatementCount();
  }
  
  public boolean getAllowLoadLocalInfile() {
    return getActiveMySQLConnection().getAllowLoadLocalInfile();
  }
  
  public boolean getAllowMasterDownConnections() {
    return getActiveMySQLConnection().getAllowMasterDownConnections();
  }
  
  public boolean getAllowMultiQueries() {
    return getActiveMySQLConnection().getAllowMultiQueries();
  }
  
  public boolean getAllowNanAndInf() {
    return getActiveMySQLConnection().getAllowNanAndInf();
  }
  
  public boolean getAllowPublicKeyRetrieval() {
    return getActiveMySQLConnection().getAllowPublicKeyRetrieval();
  }
  
  public boolean getAllowSlaveDownConnections() {
    return getActiveMySQLConnection().getAllowSlaveDownConnections();
  }
  
  public boolean getAllowUrlInLocalInfile() {
    return getActiveMySQLConnection().getAllowUrlInLocalInfile();
  }
  
  public boolean getAlwaysSendSetIsolation() {
    return getActiveMySQLConnection().getAlwaysSendSetIsolation();
  }
  
  public String getAuthenticationPlugins() {
    return getActiveMySQLConnection().getAuthenticationPlugins();
  }
  
  public boolean getAutoClosePStmtStreams() {
    return getActiveMySQLConnection().getAutoClosePStmtStreams();
  }
  
  public boolean getAutoCommit() throws SQLException {
    return getActiveMySQLConnection().getAutoCommit();
  }
  
  public boolean getAutoDeserialize() {
    return getActiveMySQLConnection().getAutoDeserialize();
  }
  
  public boolean getAutoGenerateTestcaseScript() {
    return getActiveMySQLConnection().getAutoGenerateTestcaseScript();
  }
  
  public int getAutoIncrementIncrement() {
    return getActiveMySQLConnection().getAutoIncrementIncrement();
  }
  
  public boolean getAutoReconnectForPools() {
    return getActiveMySQLConnection().getAutoReconnectForPools();
  }
  
  public boolean getAutoSlowLog() {
    return getActiveMySQLConnection().getAutoSlowLog();
  }
  
  public int getBlobSendChunkSize() {
    return getActiveMySQLConnection().getBlobSendChunkSize();
  }
  
  public boolean getBlobsAreStrings() {
    return getActiveMySQLConnection().getBlobsAreStrings();
  }
  
  public boolean getCacheCallableStatements() {
    return getActiveMySQLConnection().getCacheCallableStatements();
  }
  
  public boolean getCacheCallableStmts() {
    return getActiveMySQLConnection().getCacheCallableStmts();
  }
  
  public boolean getCacheDefaultTimezone() {
    return getActiveMySQLConnection().getCacheDefaultTimezone();
  }
  
  public boolean getCachePrepStmts() {
    return getActiveMySQLConnection().getCachePrepStmts();
  }
  
  public boolean getCachePreparedStatements() {
    return getActiveMySQLConnection().getCachePreparedStatements();
  }
  
  public boolean getCacheResultSetMetadata() {
    return getActiveMySQLConnection().getCacheResultSetMetadata();
  }
  
  public boolean getCacheServerConfiguration() {
    return getActiveMySQLConnection().getCacheServerConfiguration();
  }
  
  public CachedResultSetMetaData getCachedMetaData(String paramString) {
    return getActiveMySQLConnection().getCachedMetaData(paramString);
  }
  
  public Calendar getCalendarInstanceForSessionOrNew() {
    return getActiveMySQLConnection().getCalendarInstanceForSessionOrNew();
  }
  
  public int getCallableStatementCacheSize() {
    return getActiveMySQLConnection().getCallableStatementCacheSize();
  }
  
  public int getCallableStmtCacheSize() {
    return getActiveMySQLConnection().getCallableStmtCacheSize();
  }
  
  public Timer getCancelTimer() {
    return getActiveMySQLConnection().getCancelTimer();
  }
  
  public boolean getCapitalizeTypeNames() {
    return getActiveMySQLConnection().getCapitalizeTypeNames();
  }
  
  public String getCatalog() throws SQLException {
    return getActiveMySQLConnection().getCatalog();
  }
  
  public String getCharacterSetMetadata() {
    return getActiveMySQLConnection().getCharacterSetMetadata();
  }
  
  public String getCharacterSetResults() {
    return getActiveMySQLConnection().getCharacterSetResults();
  }
  
  public SingleByteCharsetConverter getCharsetConverter(String paramString) throws SQLException {
    return getActiveMySQLConnection().getCharsetConverter(paramString);
  }
  
  @Deprecated
  public String getCharsetNameForIndex(int paramInt) throws SQLException {
    return getEncodingForIndex(paramInt);
  }
  
  public String getClientCertificateKeyStorePassword() {
    return getActiveMySQLConnection().getClientCertificateKeyStorePassword();
  }
  
  public String getClientCertificateKeyStoreType() {
    return getActiveMySQLConnection().getClientCertificateKeyStoreType();
  }
  
  public String getClientCertificateKeyStoreUrl() {
    return getActiveMySQLConnection().getClientCertificateKeyStoreUrl();
  }
  
  public String getClientInfoProvider() {
    return getActiveMySQLConnection().getClientInfoProvider();
  }
  
  public String getClobCharacterEncoding() {
    return getActiveMySQLConnection().getClobCharacterEncoding();
  }
  
  public boolean getClobberStreamingResults() {
    return getActiveMySQLConnection().getClobberStreamingResults();
  }
  
  public boolean getCompensateOnDuplicateKeyUpdateCounts() {
    return getActiveMySQLConnection().getCompensateOnDuplicateKeyUpdateCounts();
  }
  
  public int getConnectTimeout() {
    return getActiveMySQLConnection().getConnectTimeout();
  }
  
  public String getConnectionAttributes() throws SQLException {
    return getActiveMySQLConnection().getConnectionAttributes();
  }
  
  public String getConnectionCollation() {
    return getActiveMySQLConnection().getConnectionCollation();
  }
  
  public String getConnectionLifecycleInterceptors() {
    return getActiveMySQLConnection().getConnectionLifecycleInterceptors();
  }
  
  public Object getConnectionMutex() {
    return getActiveMySQLConnection().getConnectionMutex();
  }
  
  public boolean getContinueBatchOnError() {
    return getActiveMySQLConnection().getContinueBatchOnError();
  }
  
  public boolean getCreateDatabaseIfNotExist() {
    return getActiveMySQLConnection().getCreateDatabaseIfNotExist();
  }
  
  public String getDefaultAuthenticationPlugin() {
    return getActiveMySQLConnection().getDefaultAuthenticationPlugin();
  }
  
  public int getDefaultFetchSize() {
    return getActiveMySQLConnection().getDefaultFetchSize();
  }
  
  public TimeZone getDefaultTimeZone() {
    return getActiveMySQLConnection().getDefaultTimeZone();
  }
  
  public boolean getDetectCustomCollations() {
    return getActiveMySQLConnection().getDetectCustomCollations();
  }
  
  public String getDisabledAuthenticationPlugins() {
    return getActiveMySQLConnection().getDisabledAuthenticationPlugins();
  }
  
  public boolean getDisconnectOnExpiredPasswords() {
    return getActiveMySQLConnection().getDisconnectOnExpiredPasswords();
  }
  
  public boolean getDontCheckOnDuplicateKeyUpdateInSQL() {
    return getActiveMySQLConnection().getDontCheckOnDuplicateKeyUpdateInSQL();
  }
  
  public boolean getDontTrackOpenResources() {
    return getActiveMySQLConnection().getDontTrackOpenResources();
  }
  
  public boolean getDumpMetadataOnColumnNotFound() {
    return getActiveMySQLConnection().getDumpMetadataOnColumnNotFound();
  }
  
  public boolean getDumpQueriesOnException() {
    return getActiveMySQLConnection().getDumpQueriesOnException();
  }
  
  public boolean getDynamicCalendars() {
    return getActiveMySQLConnection().getDynamicCalendars();
  }
  
  public boolean getElideSetAutoCommits() {
    return getActiveMySQLConnection().getElideSetAutoCommits();
  }
  
  public boolean getEmptyStringsConvertToZero() {
    return getActiveMySQLConnection().getEmptyStringsConvertToZero();
  }
  
  public boolean getEmulateLocators() {
    return getActiveMySQLConnection().getEmulateLocators();
  }
  
  public boolean getEmulateUnsupportedPstmts() {
    return getActiveMySQLConnection().getEmulateUnsupportedPstmts();
  }
  
  public boolean getEnableEscapeProcessing() {
    return getActiveMySQLConnection().getEnableEscapeProcessing();
  }
  
  public boolean getEnablePacketDebug() {
    return getActiveMySQLConnection().getEnablePacketDebug();
  }
  
  public boolean getEnableQueryTimeouts() {
    return getActiveMySQLConnection().getEnableQueryTimeouts();
  }
  
  public String getEnabledSSLCipherSuites() {
    return getActiveMySQLConnection().getEnabledSSLCipherSuites();
  }
  
  public String getEnabledTLSProtocols() {
    return getActiveMySQLConnection().getEnabledTLSProtocols();
  }
  
  public String getEncoding() {
    return getActiveMySQLConnection().getEncoding();
  }
  
  public String getEncodingForIndex(int paramInt) throws SQLException {
    return getActiveMySQLConnection().getEncodingForIndex(paramInt);
  }
  
  public String getErrorMessageEncoding() {
    return getActiveMySQLConnection().getErrorMessageEncoding();
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return getActiveMySQLConnection().getExceptionInterceptor();
  }
  
  public String getExceptionInterceptors() {
    return getActiveMySQLConnection().getExceptionInterceptors();
  }
  
  public boolean getExplainSlowQueries() {
    return getActiveMySQLConnection().getExplainSlowQueries();
  }
  
  public boolean getFailOverReadOnly() {
    return getActiveMySQLConnection().getFailOverReadOnly();
  }
  
  public boolean getFunctionsNeverReturnBlobs() {
    return getActiveMySQLConnection().getFunctionsNeverReturnBlobs();
  }
  
  public boolean getGatherPerfMetrics() {
    return getActiveMySQLConnection().getGatherPerfMetrics();
  }
  
  public boolean getGatherPerformanceMetrics() {
    return getActiveMySQLConnection().getGatherPerformanceMetrics();
  }
  
  public boolean getGenerateSimpleParameterMetadata() {
    return getActiveMySQLConnection().getGenerateSimpleParameterMetadata();
  }
  
  public boolean getGetProceduresReturnsFunctions() {
    return getActiveMySQLConnection().getGetProceduresReturnsFunctions();
  }
  
  public boolean getHoldResultsOpenOverStatementClose() {
    return getActiveMySQLConnection().getHoldResultsOpenOverStatementClose();
  }
  
  public int getHoldability() throws SQLException {
    return getActiveMySQLConnection().getHoldability();
  }
  
  public String getHost() {
    return getActiveMySQLConnection().getHost();
  }
  
  public String getHostPortPair() {
    return getActiveMySQLConnection().getHostPortPair();
  }
  
  public MysqlIO getIO() throws SQLException {
    return getActiveMySQLConnection().getIO();
  }
  
  public long getId() {
    return getActiveMySQLConnection().getId();
  }
  
  public long getIdleFor() {
    return getActiveMySQLConnection().getIdleFor();
  }
  
  public boolean getIgnoreNonTxTables() {
    return getActiveMySQLConnection().getIgnoreNonTxTables();
  }
  
  public boolean getIncludeInnodbStatusInDeadlockExceptions() {
    return getActiveMySQLConnection().getIncludeInnodbStatusInDeadlockExceptions();
  }
  
  public boolean getIncludeThreadDumpInDeadlockExceptions() {
    return getActiveMySQLConnection().getIncludeThreadDumpInDeadlockExceptions();
  }
  
  public boolean getIncludeThreadNamesAsStatementComment() {
    return getActiveMySQLConnection().getIncludeThreadNamesAsStatementComment();
  }
  
  public int getInitialTimeout() {
    return getActiveMySQLConnection().getInitialTimeout();
  }
  
  public boolean getInteractiveClient() {
    return getActiveMySQLConnection().getInteractiveClient();
  }
  
  public boolean getIsInteractiveClient() {
    return getActiveMySQLConnection().getIsInteractiveClient();
  }
  
  public boolean getJdbcCompliantTruncation() {
    return getActiveMySQLConnection().getJdbcCompliantTruncation();
  }
  
  public boolean getJdbcCompliantTruncationForReads() {
    return getActiveMySQLConnection().getJdbcCompliantTruncationForReads();
  }
  
  public String getLargeRowSizeThreshold() {
    return getActiveMySQLConnection().getLargeRowSizeThreshold();
  }
  
  public String getLoadBalanceAutoCommitStatementRegex() {
    return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementRegex();
  }
  
  public int getLoadBalanceAutoCommitStatementThreshold() {
    return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementThreshold();
  }
  
  public int getLoadBalanceBlacklistTimeout() {
    return getActiveMySQLConnection().getLoadBalanceBlacklistTimeout();
  }
  
  public String getLoadBalanceConnectionGroup() {
    return getActiveMySQLConnection().getLoadBalanceConnectionGroup();
  }
  
  public boolean getLoadBalanceEnableJMX() {
    return getActiveMySQLConnection().getLoadBalanceEnableJMX();
  }
  
  public String getLoadBalanceExceptionChecker() {
    return getActiveMySQLConnection().getLoadBalanceExceptionChecker();
  }
  
  public int getLoadBalanceHostRemovalGracePeriod() {
    return getActiveMySQLConnection().getLoadBalanceHostRemovalGracePeriod();
  }
  
  public int getLoadBalancePingTimeout() {
    return getActiveMySQLConnection().getLoadBalancePingTimeout();
  }
  
  public String getLoadBalanceSQLExceptionSubclassFailover() {
    return getActiveMySQLConnection().getLoadBalanceSQLExceptionSubclassFailover();
  }
  
  public String getLoadBalanceSQLStateFailover() {
    return getActiveMySQLConnection().getLoadBalanceSQLStateFailover();
  }
  
  @Deprecated
  public MySQLConnection getLoadBalanceSafeProxy() {
    return getMultiHostSafeProxy();
  }
  
  public String getLoadBalanceStrategy() {
    return getActiveMySQLConnection().getLoadBalanceStrategy();
  }
  
  public boolean getLoadBalanceValidateConnectionOnSwapServer() {
    return getActiveMySQLConnection().getLoadBalanceValidateConnectionOnSwapServer();
  }
  
  public String getLocalSocketAddress() {
    return getActiveMySQLConnection().getLocalSocketAddress();
  }
  
  public int getLocatorFetchBufferSize() {
    return getActiveMySQLConnection().getLocatorFetchBufferSize();
  }
  
  public Log getLog() throws SQLException {
    return getActiveMySQLConnection().getLog();
  }
  
  public boolean getLogSlowQueries() {
    return getActiveMySQLConnection().getLogSlowQueries();
  }
  
  public boolean getLogXaCommands() {
    return getActiveMySQLConnection().getLogXaCommands();
  }
  
  public String getLogger() {
    return getActiveMySQLConnection().getLogger();
  }
  
  public String getLoggerClassName() {
    return getActiveMySQLConnection().getLoggerClassName();
  }
  
  public boolean getMaintainTimeStats() {
    return getActiveMySQLConnection().getMaintainTimeStats();
  }
  
  public int getMaxAllowedPacket() {
    return getActiveMySQLConnection().getMaxAllowedPacket();
  }
  
  public int getMaxBytesPerChar(Integer paramInteger, String paramString) throws SQLException {
    return getActiveMySQLConnection().getMaxBytesPerChar(paramInteger, paramString);
  }
  
  public int getMaxBytesPerChar(String paramString) throws SQLException {
    return getActiveMySQLConnection().getMaxBytesPerChar(paramString);
  }
  
  public int getMaxQuerySizeToLog() {
    return getActiveMySQLConnection().getMaxQuerySizeToLog();
  }
  
  public int getMaxReconnects() {
    return getActiveMySQLConnection().getMaxReconnects();
  }
  
  public int getMaxRows() {
    return getActiveMySQLConnection().getMaxRows();
  }
  
  public DatabaseMetaData getMetaData() throws SQLException {
    return getActiveMySQLConnection().getMetaData();
  }
  
  public int getMetadataCacheSize() {
    return getActiveMySQLConnection().getMetadataCacheSize();
  }
  
  public Statement getMetadataSafeStatement() throws SQLException {
    return getActiveMySQLConnection().getMetadataSafeStatement();
  }
  
  public MySQLConnection getMultiHostSafeProxy() {
    return getThisAsProxy().getProxy();
  }
  
  public int getNetBufferLength() {
    return getActiveMySQLConnection().getNetBufferLength();
  }
  
  public int getNetTimeoutForStreamingResults() {
    return getActiveMySQLConnection().getNetTimeoutForStreamingResults();
  }
  
  public int getNetworkTimeout() throws SQLException {
    return getActiveMySQLConnection().getNetworkTimeout();
  }
  
  public boolean getNoAccessToProcedureBodies() {
    return getActiveMySQLConnection().getNoAccessToProcedureBodies();
  }
  
  public boolean getNoDatetimeStringSync() {
    return getActiveMySQLConnection().getNoDatetimeStringSync();
  }
  
  public boolean getNoTimezoneConversionForDateType() {
    return getActiveMySQLConnection().getNoTimezoneConversionForDateType();
  }
  
  public boolean getNoTimezoneConversionForTimeType() {
    return getActiveMySQLConnection().getNoTimezoneConversionForTimeType();
  }
  
  public boolean getNullCatalogMeansCurrent() {
    return getActiveMySQLConnection().getNullCatalogMeansCurrent();
  }
  
  public boolean getNullNamePatternMatchesAll() {
    return getActiveMySQLConnection().getNullNamePatternMatchesAll();
  }
  
  public boolean getOverrideSupportsIntegrityEnhancementFacility() {
    return getActiveMySQLConnection().getOverrideSupportsIntegrityEnhancementFacility();
  }
  
  public int getPacketDebugBufferSize() {
    return getActiveMySQLConnection().getPacketDebugBufferSize();
  }
  
  public boolean getPadCharsWithSpace() {
    return getActiveMySQLConnection().getPadCharsWithSpace();
  }
  
  public boolean getParanoid() {
    return getActiveMySQLConnection().getParanoid();
  }
  
  public String getParseInfoCacheFactory() {
    return getActiveMySQLConnection().getParseInfoCacheFactory();
  }
  
  public String getPasswordCharacterEncoding() {
    return getActiveMySQLConnection().getPasswordCharacterEncoding();
  }
  
  public boolean getPedantic() {
    return getActiveMySQLConnection().getPedantic();
  }
  
  public boolean getPinGlobalTxToPhysicalConnection() {
    return getActiveMySQLConnection().getPinGlobalTxToPhysicalConnection();
  }
  
  public boolean getPopulateInsertRowWithDefaultValues() {
    return getActiveMySQLConnection().getPopulateInsertRowWithDefaultValues();
  }
  
  public int getPrepStmtCacheSize() {
    return getActiveMySQLConnection().getPrepStmtCacheSize();
  }
  
  public int getPrepStmtCacheSqlLimit() {
    return getActiveMySQLConnection().getPrepStmtCacheSqlLimit();
  }
  
  public int getPreparedStatementCacheSize() {
    return getActiveMySQLConnection().getPreparedStatementCacheSize();
  }
  
  public int getPreparedStatementCacheSqlLimit() {
    return getActiveMySQLConnection().getPreparedStatementCacheSqlLimit();
  }
  
  public boolean getProcessEscapeCodesForPrepStmts() {
    return getActiveMySQLConnection().getProcessEscapeCodesForPrepStmts();
  }
  
  public boolean getProfileSQL() {
    return getActiveMySQLConnection().getProfileSQL();
  }
  
  public boolean getProfileSql() {
    return getActiveMySQLConnection().getProfileSql();
  }
  
  public String getProfilerEventHandler() {
    return getActiveMySQLConnection().getProfilerEventHandler();
  }
  
  public ProfilerEventHandler getProfilerEventHandlerInstance() {
    return getActiveMySQLConnection().getProfilerEventHandlerInstance();
  }
  
  public Properties getProperties() {
    return getActiveMySQLConnection().getProperties();
  }
  
  public String getPropertiesTransform() {
    return getActiveMySQLConnection().getPropertiesTransform();
  }
  
  public int getQueriesBeforeRetryMaster() {
    return getActiveMySQLConnection().getQueriesBeforeRetryMaster();
  }
  
  public boolean getQueryTimeoutKillsConnection() {
    return getActiveMySQLConnection().getQueryTimeoutKillsConnection();
  }
  
  public boolean getReadFromMasterWhenNoSlaves() {
    return getActiveMySQLConnection().getReadFromMasterWhenNoSlaves();
  }
  
  public boolean getReadOnlyPropagatesToServer() {
    return getActiveMySQLConnection().getReadOnlyPropagatesToServer();
  }
  
  public boolean getReconnectAtTxEnd() {
    return getActiveMySQLConnection().getReconnectAtTxEnd();
  }
  
  public boolean getRelaxAutoCommit() {
    return getActiveMySQLConnection().getRelaxAutoCommit();
  }
  
  public boolean getReplicationEnableJMX() {
    return getActiveMySQLConnection().getReplicationEnableJMX();
  }
  
  public int getReportMetricsIntervalMillis() {
    return getActiveMySQLConnection().getReportMetricsIntervalMillis();
  }
  
  public boolean getRequireSSL() {
    return getActiveMySQLConnection().getRequireSSL();
  }
  
  public boolean getRequiresEscapingEncoder() {
    return getActiveMySQLConnection().getRequiresEscapingEncoder();
  }
  
  public String getResourceId() {
    return getActiveMySQLConnection().getResourceId();
  }
  
  public int getResultSetSizeThreshold() {
    return getActiveMySQLConnection().getResultSetSizeThreshold();
  }
  
  public boolean getRetainStatementAfterResultSetClose() {
    return getActiveMySQLConnection().getRetainStatementAfterResultSetClose();
  }
  
  public int getRetriesAllDown() {
    return getActiveMySQLConnection().getRetriesAllDown();
  }
  
  public boolean getRewriteBatchedStatements() {
    return getActiveMySQLConnection().getRewriteBatchedStatements();
  }
  
  public boolean getRollbackOnPooledClose() {
    return getActiveMySQLConnection().getRollbackOnPooledClose();
  }
  
  public boolean getRoundRobinLoadBalance() {
    return getActiveMySQLConnection().getRoundRobinLoadBalance();
  }
  
  public boolean getRunningCTS13() {
    return getActiveMySQLConnection().getRunningCTS13();
  }
  
  public String getSchema() throws SQLException {
    return getActiveMySQLConnection().getSchema();
  }
  
  public int getSecondsBeforeRetryMaster() {
    return getActiveMySQLConnection().getSecondsBeforeRetryMaster();
  }
  
  public int getSelfDestructOnPingMaxOperations() {
    return getActiveMySQLConnection().getSelfDestructOnPingMaxOperations();
  }
  
  public int getSelfDestructOnPingSecondsLifetime() {
    return getActiveMySQLConnection().getSelfDestructOnPingSecondsLifetime();
  }
  
  public boolean getSendFractionalSeconds() {
    return getActiveMySQLConnection().getSendFractionalSeconds();
  }
  
  public String getServerAffinityOrder() {
    return getActiveMySQLConnection().getServerAffinityOrder();
  }
  
  @Deprecated
  public String getServerCharacterEncoding() {
    return getServerCharset();
  }
  
  public String getServerCharset() {
    return getActiveMySQLConnection().getServerCharset();
  }
  
  public String getServerConfigCacheFactory() {
    return getActiveMySQLConnection().getServerConfigCacheFactory();
  }
  
  public int getServerMajorVersion() {
    return getActiveMySQLConnection().getServerMajorVersion();
  }
  
  public int getServerMinorVersion() {
    return getActiveMySQLConnection().getServerMinorVersion();
  }
  
  public String getServerRSAPublicKeyFile() {
    return getActiveMySQLConnection().getServerRSAPublicKeyFile();
  }
  
  public int getServerSubMinorVersion() {
    return getActiveMySQLConnection().getServerSubMinorVersion();
  }
  
  public String getServerTimezone() {
    return getActiveMySQLConnection().getServerTimezone();
  }
  
  public TimeZone getServerTimezoneTZ() {
    return getActiveMySQLConnection().getServerTimezoneTZ();
  }
  
  public String getServerVariable(String paramString) {
    return getActiveMySQLConnection().getServerVariable(paramString);
  }
  
  public String getServerVersion() {
    return getActiveMySQLConnection().getServerVersion();
  }
  
  public Calendar getSessionLockedCalendar() {
    return getActiveMySQLConnection().getSessionLockedCalendar();
  }
  
  public int getSessionMaxRows() {
    return getActiveMySQLConnection().getSessionMaxRows();
  }
  
  public String getSessionVariables() {
    return getActiveMySQLConnection().getSessionVariables();
  }
  
  public int getSlowQueryThresholdMillis() {
    return getActiveMySQLConnection().getSlowQueryThresholdMillis();
  }
  
  public long getSlowQueryThresholdNanos() {
    return getActiveMySQLConnection().getSlowQueryThresholdNanos();
  }
  
  public String getSocketFactory() {
    return getActiveMySQLConnection().getSocketFactory();
  }
  
  public String getSocketFactoryClassName() {
    return getActiveMySQLConnection().getSocketFactoryClassName();
  }
  
  public int getSocketTimeout() {
    return getActiveMySQLConnection().getSocketTimeout();
  }
  
  public String getSocksProxyHost() {
    return getActiveMySQLConnection().getSocksProxyHost();
  }
  
  public int getSocksProxyPort() {
    return getActiveMySQLConnection().getSocksProxyPort();
  }
  
  public String getStatementComment() {
    return getActiveMySQLConnection().getStatementComment();
  }
  
  public String getStatementInterceptors() {
    return getActiveMySQLConnection().getStatementInterceptors();
  }
  
  public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
    return getActiveMySQLConnection().getStatementInterceptorsInstances();
  }
  
  public boolean getStrictFloatingPoint() {
    return getActiveMySQLConnection().getStrictFloatingPoint();
  }
  
  public boolean getStrictUpdates() {
    return getActiveMySQLConnection().getStrictUpdates();
  }
  
  public boolean getTcpKeepAlive() {
    return getActiveMySQLConnection().getTcpKeepAlive();
  }
  
  public boolean getTcpNoDelay() {
    return getActiveMySQLConnection().getTcpNoDelay();
  }
  
  public int getTcpRcvBuf() {
    return getActiveMySQLConnection().getTcpRcvBuf();
  }
  
  public int getTcpSndBuf() {
    return getActiveMySQLConnection().getTcpSndBuf();
  }
  
  public int getTcpTrafficClass() {
    return getActiveMySQLConnection().getTcpTrafficClass();
  }
  
  public MultiHostConnectionProxy getThisAsProxy() {
    return this.thisAsProxy;
  }
  
  public boolean getTinyInt1isBit() {
    return getActiveMySQLConnection().getTinyInt1isBit();
  }
  
  public boolean getTraceProtocol() {
    return getActiveMySQLConnection().getTraceProtocol();
  }
  
  public int getTransactionIsolation() throws SQLException {
    return getActiveMySQLConnection().getTransactionIsolation();
  }
  
  public boolean getTransformedBitIsBoolean() {
    return getActiveMySQLConnection().getTransformedBitIsBoolean();
  }
  
  public boolean getTreatUtilDateAsTimestamp() {
    return getActiveMySQLConnection().getTreatUtilDateAsTimestamp();
  }
  
  public String getTrustCertificateKeyStorePassword() {
    return getActiveMySQLConnection().getTrustCertificateKeyStorePassword();
  }
  
  public String getTrustCertificateKeyStoreType() {
    return getActiveMySQLConnection().getTrustCertificateKeyStoreType();
  }
  
  public String getTrustCertificateKeyStoreUrl() {
    return getActiveMySQLConnection().getTrustCertificateKeyStoreUrl();
  }
  
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return getActiveMySQLConnection().getTypeMap();
  }
  
  public String getURL() {
    return getActiveMySQLConnection().getURL();
  }
  
  public boolean getUltraDevHack() {
    return getActiveMySQLConnection().getUltraDevHack();
  }
  
  public boolean getUseAffectedRows() {
    return getActiveMySQLConnection().getUseAffectedRows();
  }
  
  public boolean getUseBlobToStoreUTF8OutsideBMP() {
    return getActiveMySQLConnection().getUseBlobToStoreUTF8OutsideBMP();
  }
  
  public boolean getUseColumnNamesInFindColumn() {
    return getActiveMySQLConnection().getUseColumnNamesInFindColumn();
  }
  
  public boolean getUseCompression() {
    return getActiveMySQLConnection().getUseCompression();
  }
  
  public String getUseConfigs() {
    return getActiveMySQLConnection().getUseConfigs();
  }
  
  public boolean getUseCursorFetch() {
    return getActiveMySQLConnection().getUseCursorFetch();
  }
  
  public boolean getUseDirectRowUnpack() {
    return getActiveMySQLConnection().getUseDirectRowUnpack();
  }
  
  public boolean getUseDynamicCharsetInfo() {
    return getActiveMySQLConnection().getUseDynamicCharsetInfo();
  }
  
  public boolean getUseFastDateParsing() {
    return getActiveMySQLConnection().getUseFastDateParsing();
  }
  
  public boolean getUseFastIntParsing() {
    return getActiveMySQLConnection().getUseFastIntParsing();
  }
  
  public boolean getUseGmtMillisForDatetimes() {
    return getActiveMySQLConnection().getUseGmtMillisForDatetimes();
  }
  
  public boolean getUseHostsInPrivileges() {
    return getActiveMySQLConnection().getUseHostsInPrivileges();
  }
  
  public boolean getUseInformationSchema() {
    return getActiveMySQLConnection().getUseInformationSchema();
  }
  
  public boolean getUseJDBCCompliantTimezoneShift() {
    return getActiveMySQLConnection().getUseJDBCCompliantTimezoneShift();
  }
  
  public boolean getUseJvmCharsetConverters() {
    return getActiveMySQLConnection().getUseJvmCharsetConverters();
  }
  
  public boolean getUseLegacyDatetimeCode() {
    return getActiveMySQLConnection().getUseLegacyDatetimeCode();
  }
  
  public boolean getUseLocalSessionState() {
    return getActiveMySQLConnection().getUseLocalSessionState();
  }
  
  public boolean getUseLocalTransactionState() {
    return getActiveMySQLConnection().getUseLocalTransactionState();
  }
  
  public boolean getUseNanosForElapsedTime() {
    return getActiveMySQLConnection().getUseNanosForElapsedTime();
  }
  
  public boolean getUseOldAliasMetadataBehavior() {
    return getActiveMySQLConnection().getUseOldAliasMetadataBehavior();
  }
  
  public boolean getUseOldUTF8Behavior() {
    return getActiveMySQLConnection().getUseOldUTF8Behavior();
  }
  
  public boolean getUseOnlyServerErrorMessages() {
    return getActiveMySQLConnection().getUseOnlyServerErrorMessages();
  }
  
  public boolean getUseReadAheadInput() {
    return getActiveMySQLConnection().getUseReadAheadInput();
  }
  
  public boolean getUseSSL() {
    return getActiveMySQLConnection().getUseSSL();
  }
  
  public boolean getUseSSPSCompatibleTimezoneShift() {
    return getActiveMySQLConnection().getUseSSPSCompatibleTimezoneShift();
  }
  
  public boolean getUseServerPrepStmts() {
    return getActiveMySQLConnection().getUseServerPrepStmts();
  }
  
  public boolean getUseServerPreparedStmts() {
    return getActiveMySQLConnection().getUseServerPreparedStmts();
  }
  
  public boolean getUseSqlStateCodes() {
    return getActiveMySQLConnection().getUseSqlStateCodes();
  }
  
  public boolean getUseStreamLengthsInPrepStmts() {
    return getActiveMySQLConnection().getUseStreamLengthsInPrepStmts();
  }
  
  public boolean getUseTimezone() {
    return getActiveMySQLConnection().getUseTimezone();
  }
  
  public boolean getUseUltraDevWorkAround() {
    return getActiveMySQLConnection().getUseUltraDevWorkAround();
  }
  
  public boolean getUseUnbufferedInput() {
    return getActiveMySQLConnection().getUseUnbufferedInput();
  }
  
  public boolean getUseUnicode() {
    return getActiveMySQLConnection().getUseUnicode();
  }
  
  public boolean getUseUsageAdvisor() {
    return getActiveMySQLConnection().getUseUsageAdvisor();
  }
  
  public String getUser() {
    return getActiveMySQLConnection().getUser();
  }
  
  public Calendar getUtcCalendar() {
    return getActiveMySQLConnection().getUtcCalendar();
  }
  
  public String getUtf8OutsideBmpExcludedColumnNamePattern() {
    return getActiveMySQLConnection().getUtf8OutsideBmpExcludedColumnNamePattern();
  }
  
  public String getUtf8OutsideBmpIncludedColumnNamePattern() {
    return getActiveMySQLConnection().getUtf8OutsideBmpIncludedColumnNamePattern();
  }
  
  public boolean getVerifyServerCertificate() {
    return getActiveMySQLConnection().getVerifyServerCertificate();
  }
  
  public SQLWarning getWarnings() throws SQLException {
    return getActiveMySQLConnection().getWarnings();
  }
  
  public boolean getYearIsDateType() {
    return getActiveMySQLConnection().getYearIsDateType();
  }
  
  public String getZeroDateTimeBehavior() {
    return getActiveMySQLConnection().getZeroDateTimeBehavior();
  }
  
  public boolean hasSameProperties(Connection paramConnection) {
    return getActiveMySQLConnection().hasSameProperties(paramConnection);
  }
  
  @Deprecated
  public boolean hasTriedMaster() {
    return getActiveMySQLConnection().hasTriedMaster();
  }
  
  public void incrementNumberOfPreparedExecutes() {
    getActiveMySQLConnection().incrementNumberOfPreparedExecutes();
  }
  
  public void incrementNumberOfPrepares() {
    getActiveMySQLConnection().incrementNumberOfPrepares();
  }
  
  public void incrementNumberOfResultSetsCreated() {
    getActiveMySQLConnection().incrementNumberOfResultSetsCreated();
  }
  
  public void initializeExtension(Extension paramExtension) throws SQLException {
    getActiveMySQLConnection().initializeExtension(paramExtension);
  }
  
  public void initializeResultsMetadataFromCache(String paramString, CachedResultSetMetaData paramCachedResultSetMetaData, ResultSetInternalMethods paramResultSetInternalMethods) throws SQLException {
    getActiveMySQLConnection().initializeResultsMetadataFromCache(paramString, paramCachedResultSetMetaData, paramResultSetInternalMethods);
  }
  
  public void initializeSafeStatementInterceptors() throws SQLException {
    getActiveMySQLConnection().initializeSafeStatementInterceptors();
  }
  
  public boolean isAbonormallyLongQuery(long paramLong) {
    return getActiveMySQLConnection().isAbonormallyLongQuery(paramLong);
  }
  
  public boolean isClientTzUTC() {
    return getActiveMySQLConnection().isClientTzUTC();
  }
  
  public boolean isClosed() throws SQLException {
    return (getThisAsProxy()).isClosed;
  }
  
  public boolean isCursorFetchEnabled() throws SQLException {
    return getActiveMySQLConnection().isCursorFetchEnabled();
  }
  
  public boolean isInGlobalTx() {
    return getActiveMySQLConnection().isInGlobalTx();
  }
  
  public boolean isMasterConnection() {
    return getThisAsProxy().isMasterConnection();
  }
  
  public boolean isNoBackslashEscapesSet() {
    return getActiveMySQLConnection().isNoBackslashEscapesSet();
  }
  
  public boolean isProxySet() {
    return getActiveMySQLConnection().isProxySet();
  }
  
  public boolean isReadInfoMsgEnabled() {
    return getActiveMySQLConnection().isReadInfoMsgEnabled();
  }
  
  public boolean isReadOnly() throws SQLException {
    return getActiveMySQLConnection().isReadOnly();
  }
  
  public boolean isReadOnly(boolean paramBoolean) throws SQLException {
    return getActiveMySQLConnection().isReadOnly(paramBoolean);
  }
  
  public boolean isRunningOnJDK13() {
    return getActiveMySQLConnection().isRunningOnJDK13();
  }
  
  public boolean isSameResource(Connection paramConnection) {
    return getActiveMySQLConnection().isSameResource(paramConnection);
  }
  
  public boolean isServerLocal() throws SQLException {
    return getActiveMySQLConnection().isServerLocal();
  }
  
  public boolean isServerTruncatesFracSecs() {
    return getActiveMySQLConnection().isServerTruncatesFracSecs();
  }
  
  public boolean isServerTzUTC() {
    return getActiveMySQLConnection().isServerTzUTC();
  }
  
  public boolean isUseSSLExplicit() {
    return getActiveMySQLConnection().isUseSSLExplicit();
  }
  
  public boolean lowerCaseTableNames() {
    return getActiveMySQLConnection().lowerCaseTableNames();
  }
  
  public String nativeSQL(String paramString) throws SQLException {
    return getActiveMySQLConnection().nativeSQL(paramString);
  }
  
  public boolean parserKnowsUnicode() {
    return getActiveMySQLConnection().parserKnowsUnicode();
  }
  
  public void ping() throws SQLException {
    getActiveMySQLConnection().ping();
  }
  
  public void pingInternal(boolean paramBoolean, int paramInt) throws SQLException {
    getActiveMySQLConnection().pingInternal(paramBoolean, paramInt);
  }
  
  public CallableStatement prepareCall(String paramString) throws SQLException {
    return getActiveMySQLConnection().prepareCall(paramString);
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) throws SQLException {
    return getActiveMySQLConnection().prepareCall(paramString, paramInt1, paramInt2);
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return getActiveMySQLConnection().prepareCall(paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public PreparedStatement prepareStatement(String paramString) throws SQLException {
    return getActiveMySQLConnection().prepareStatement(paramString);
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt) throws SQLException {
    return getActiveMySQLConnection().prepareStatement(paramString, paramInt);
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    return getActiveMySQLConnection().prepareStatement(paramString, paramInt1, paramInt2);
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return getActiveMySQLConnection().prepareStatement(paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    return getActiveMySQLConnection().prepareStatement(paramString, paramArrayOfint);
  }
  
  public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    return getActiveMySQLConnection().prepareStatement(paramString, paramArrayOfString);
  }
  
  public void realClose(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Throwable paramThrowable) throws SQLException {
    getActiveMySQLConnection().realClose(paramBoolean1, paramBoolean2, paramBoolean3, paramThrowable);
  }
  
  public void recachePreparedStatement(ServerPreparedStatement paramServerPreparedStatement) throws SQLException {
    getActiveMySQLConnection().recachePreparedStatement(paramServerPreparedStatement);
  }
  
  public void registerQueryExecutionTime(long paramLong) {
    getActiveMySQLConnection().registerQueryExecutionTime(paramLong);
  }
  
  public void registerStatement(Statement paramStatement) {
    getActiveMySQLConnection().registerStatement(paramStatement);
  }
  
  public void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
    getActiveMySQLConnection().releaseSavepoint(paramSavepoint);
  }
  
  public void reportNumberOfTablesAccessed(int paramInt) {
    getActiveMySQLConnection().reportNumberOfTablesAccessed(paramInt);
  }
  
  public void reportQueryTime(long paramLong) {
    getActiveMySQLConnection().reportQueryTime(paramLong);
  }
  
  public void resetServerState() throws SQLException {
    getActiveMySQLConnection().resetServerState();
  }
  
  public void rollback() throws SQLException {
    getActiveMySQLConnection().rollback();
  }
  
  public void rollback(Savepoint paramSavepoint) throws SQLException {
    getActiveMySQLConnection().rollback(paramSavepoint);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString) throws SQLException {
    return getActiveMySQLConnection().serverPrepareStatement(paramString);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt) throws SQLException {
    return getActiveMySQLConnection().serverPrepareStatement(paramString, paramInt);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    return getActiveMySQLConnection().serverPrepareStatement(paramString, paramInt1, paramInt2);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return getActiveMySQLConnection().serverPrepareStatement(paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    return getActiveMySQLConnection().serverPrepareStatement(paramString, paramArrayOfint);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    return getActiveMySQLConnection().serverPrepareStatement(paramString, paramArrayOfString);
  }
  
  public boolean serverSupportsConvertFn() throws SQLException {
    return getActiveMySQLConnection().serverSupportsConvertFn();
  }
  
  public void setAllowLoadLocalInfile(boolean paramBoolean) {
    getActiveMySQLConnection().setAllowLoadLocalInfile(paramBoolean);
  }
  
  public void setAllowMasterDownConnections(boolean paramBoolean) {
    getActiveMySQLConnection().setAllowMasterDownConnections(paramBoolean);
  }
  
  public void setAllowMultiQueries(boolean paramBoolean) {
    getActiveMySQLConnection().setAllowMultiQueries(paramBoolean);
  }
  
  public void setAllowNanAndInf(boolean paramBoolean) {
    getActiveMySQLConnection().setAllowNanAndInf(paramBoolean);
  }
  
  public void setAllowPublicKeyRetrieval(boolean paramBoolean) throws SQLException {
    getActiveMySQLConnection().setAllowPublicKeyRetrieval(paramBoolean);
  }
  
  public void setAllowSlaveDownConnections(boolean paramBoolean) {
    getActiveMySQLConnection().setAllowSlaveDownConnections(paramBoolean);
  }
  
  public void setAllowUrlInLocalInfile(boolean paramBoolean) {
    getActiveMySQLConnection().setAllowUrlInLocalInfile(paramBoolean);
  }
  
  public void setAlwaysSendSetIsolation(boolean paramBoolean) {
    getActiveMySQLConnection().setAlwaysSendSetIsolation(paramBoolean);
  }
  
  public void setAuthenticationPlugins(String paramString) {
    getActiveMySQLConnection().setAuthenticationPlugins(paramString);
  }
  
  public void setAutoClosePStmtStreams(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoClosePStmtStreams(paramBoolean);
  }
  
  public void setAutoCommit(boolean paramBoolean) throws SQLException {
    getActiveMySQLConnection().setAutoCommit(paramBoolean);
  }
  
  public void setAutoDeserialize(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoDeserialize(paramBoolean);
  }
  
  public void setAutoGenerateTestcaseScript(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoGenerateTestcaseScript(paramBoolean);
  }
  
  public void setAutoReconnect(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoReconnect(paramBoolean);
  }
  
  public void setAutoReconnectForConnectionPools(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoReconnectForConnectionPools(paramBoolean);
  }
  
  public void setAutoReconnectForPools(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoReconnectForPools(paramBoolean);
  }
  
  public void setAutoSlowLog(boolean paramBoolean) {
    getActiveMySQLConnection().setAutoSlowLog(paramBoolean);
  }
  
  public void setBlobSendChunkSize(String paramString) throws SQLException {
    getActiveMySQLConnection().setBlobSendChunkSize(paramString);
  }
  
  public void setBlobsAreStrings(boolean paramBoolean) {
    getActiveMySQLConnection().setBlobsAreStrings(paramBoolean);
  }
  
  public void setCacheCallableStatements(boolean paramBoolean) {
    getActiveMySQLConnection().setCacheCallableStatements(paramBoolean);
  }
  
  public void setCacheCallableStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setCacheCallableStmts(paramBoolean);
  }
  
  public void setCacheDefaultTimezone(boolean paramBoolean) {
    getActiveMySQLConnection().setCacheDefaultTimezone(paramBoolean);
  }
  
  public void setCachePrepStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setCachePrepStmts(paramBoolean);
  }
  
  public void setCachePreparedStatements(boolean paramBoolean) {
    getActiveMySQLConnection().setCachePreparedStatements(paramBoolean);
  }
  
  public void setCacheResultSetMetadata(boolean paramBoolean) {
    getActiveMySQLConnection().setCacheResultSetMetadata(paramBoolean);
  }
  
  public void setCacheServerConfiguration(boolean paramBoolean) {
    getActiveMySQLConnection().setCacheServerConfiguration(paramBoolean);
  }
  
  public void setCallableStatementCacheSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setCallableStatementCacheSize(paramInt);
  }
  
  public void setCallableStmtCacheSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setCallableStmtCacheSize(paramInt);
  }
  
  public void setCapitalizeDBMDTypes(boolean paramBoolean) {
    getActiveMySQLConnection().setCapitalizeDBMDTypes(paramBoolean);
  }
  
  public void setCapitalizeTypeNames(boolean paramBoolean) {
    getActiveMySQLConnection().setCapitalizeTypeNames(paramBoolean);
  }
  
  public void setCatalog(String paramString) throws SQLException {
    getActiveMySQLConnection().setCatalog(paramString);
  }
  
  public void setCharacterEncoding(String paramString) {
    getActiveMySQLConnection().setCharacterEncoding(paramString);
  }
  
  public void setCharacterSetResults(String paramString) {
    getActiveMySQLConnection().setCharacterSetResults(paramString);
  }
  
  public void setClientCertificateKeyStorePassword(String paramString) {
    getActiveMySQLConnection().setClientCertificateKeyStorePassword(paramString);
  }
  
  public void setClientCertificateKeyStoreType(String paramString) {
    getActiveMySQLConnection().setClientCertificateKeyStoreType(paramString);
  }
  
  public void setClientCertificateKeyStoreUrl(String paramString) {
    getActiveMySQLConnection().setClientCertificateKeyStoreUrl(paramString);
  }
  
  public void setClientInfoProvider(String paramString) {
    getActiveMySQLConnection().setClientInfoProvider(paramString);
  }
  
  public void setClobCharacterEncoding(String paramString) {
    getActiveMySQLConnection().setClobCharacterEncoding(paramString);
  }
  
  public void setClobberStreamingResults(boolean paramBoolean) {
    getActiveMySQLConnection().setClobberStreamingResults(paramBoolean);
  }
  
  public void setCompensateOnDuplicateKeyUpdateCounts(boolean paramBoolean) {
    getActiveMySQLConnection().setCompensateOnDuplicateKeyUpdateCounts(paramBoolean);
  }
  
  public void setConnectTimeout(int paramInt) throws SQLException {
    getActiveMySQLConnection().setConnectTimeout(paramInt);
  }
  
  public void setConnectionCollation(String paramString) {
    getActiveMySQLConnection().setConnectionCollation(paramString);
  }
  
  public void setConnectionLifecycleInterceptors(String paramString) {
    getActiveMySQLConnection().setConnectionLifecycleInterceptors(paramString);
  }
  
  public void setContinueBatchOnError(boolean paramBoolean) {
    getActiveMySQLConnection().setContinueBatchOnError(paramBoolean);
  }
  
  public void setCreateDatabaseIfNotExist(boolean paramBoolean) {
    getActiveMySQLConnection().setCreateDatabaseIfNotExist(paramBoolean);
  }
  
  public void setDefaultAuthenticationPlugin(String paramString) {
    getActiveMySQLConnection().setDefaultAuthenticationPlugin(paramString);
  }
  
  public void setDefaultFetchSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setDefaultFetchSize(paramInt);
  }
  
  public void setDetectCustomCollations(boolean paramBoolean) {
    getActiveMySQLConnection().setDetectCustomCollations(paramBoolean);
  }
  
  public void setDetectServerPreparedStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setDetectServerPreparedStmts(paramBoolean);
  }
  
  public void setDisabledAuthenticationPlugins(String paramString) {
    getActiveMySQLConnection().setDisabledAuthenticationPlugins(paramString);
  }
  
  public void setDisconnectOnExpiredPasswords(boolean paramBoolean) {
    getActiveMySQLConnection().setDisconnectOnExpiredPasswords(paramBoolean);
  }
  
  public void setDontCheckOnDuplicateKeyUpdateInSQL(boolean paramBoolean) {
    getActiveMySQLConnection().setDontCheckOnDuplicateKeyUpdateInSQL(paramBoolean);
  }
  
  public void setDontTrackOpenResources(boolean paramBoolean) {
    getActiveMySQLConnection().setDontTrackOpenResources(paramBoolean);
  }
  
  public void setDumpMetadataOnColumnNotFound(boolean paramBoolean) {
    getActiveMySQLConnection().setDumpMetadataOnColumnNotFound(paramBoolean);
  }
  
  public void setDumpQueriesOnException(boolean paramBoolean) {
    getActiveMySQLConnection().setDumpQueriesOnException(paramBoolean);
  }
  
  public void setDynamicCalendars(boolean paramBoolean) {
    getActiveMySQLConnection().setDynamicCalendars(paramBoolean);
  }
  
  public void setElideSetAutoCommits(boolean paramBoolean) {
    getActiveMySQLConnection().setElideSetAutoCommits(paramBoolean);
  }
  
  public void setEmptyStringsConvertToZero(boolean paramBoolean) {
    getActiveMySQLConnection().setEmptyStringsConvertToZero(paramBoolean);
  }
  
  public void setEmulateLocators(boolean paramBoolean) {
    getActiveMySQLConnection().setEmulateLocators(paramBoolean);
  }
  
  public void setEmulateUnsupportedPstmts(boolean paramBoolean) {
    getActiveMySQLConnection().setEmulateUnsupportedPstmts(paramBoolean);
  }
  
  public void setEnableEscapeProcessing(boolean paramBoolean) {
    getActiveMySQLConnection().setEnableEscapeProcessing(paramBoolean);
  }
  
  public void setEnablePacketDebug(boolean paramBoolean) {
    getActiveMySQLConnection().setEnablePacketDebug(paramBoolean);
  }
  
  public void setEnableQueryTimeouts(boolean paramBoolean) {
    getActiveMySQLConnection().setEnableQueryTimeouts(paramBoolean);
  }
  
  public void setEnabledSSLCipherSuites(String paramString) {
    getActiveMySQLConnection().setEnabledSSLCipherSuites(paramString);
  }
  
  public void setEnabledTLSProtocols(String paramString) {
    getActiveMySQLConnection().setEnabledTLSProtocols(paramString);
  }
  
  public void setEncoding(String paramString) {
    getActiveMySQLConnection().setEncoding(paramString);
  }
  
  public void setExceptionInterceptors(String paramString) {
    getActiveMySQLConnection().setExceptionInterceptors(paramString);
  }
  
  public void setExplainSlowQueries(boolean paramBoolean) {
    getActiveMySQLConnection().setExplainSlowQueries(paramBoolean);
  }
  
  public void setFailOverReadOnly(boolean paramBoolean) {
    getActiveMySQLConnection().setFailOverReadOnly(paramBoolean);
  }
  
  public void setFailedOver(boolean paramBoolean) {
    getActiveMySQLConnection().setFailedOver(paramBoolean);
  }
  
  public void setFunctionsNeverReturnBlobs(boolean paramBoolean) {
    getActiveMySQLConnection().setFunctionsNeverReturnBlobs(paramBoolean);
  }
  
  public void setGatherPerfMetrics(boolean paramBoolean) {
    getActiveMySQLConnection().setGatherPerfMetrics(paramBoolean);
  }
  
  public void setGatherPerformanceMetrics(boolean paramBoolean) {
    getActiveMySQLConnection().setGatherPerformanceMetrics(paramBoolean);
  }
  
  public void setGenerateSimpleParameterMetadata(boolean paramBoolean) {
    getActiveMySQLConnection().setGenerateSimpleParameterMetadata(paramBoolean);
  }
  
  public void setGetProceduresReturnsFunctions(boolean paramBoolean) {
    getActiveMySQLConnection().setGetProceduresReturnsFunctions(paramBoolean);
  }
  
  public void setHoldResultsOpenOverStatementClose(boolean paramBoolean) {
    getActiveMySQLConnection().setHoldResultsOpenOverStatementClose(paramBoolean);
  }
  
  public void setHoldability(int paramInt) throws SQLException {
    getActiveMySQLConnection().setHoldability(paramInt);
  }
  
  public void setIgnoreNonTxTables(boolean paramBoolean) {
    getActiveMySQLConnection().setIgnoreNonTxTables(paramBoolean);
  }
  
  public void setInGlobalTx(boolean paramBoolean) {
    getActiveMySQLConnection().setInGlobalTx(paramBoolean);
  }
  
  public void setIncludeInnodbStatusInDeadlockExceptions(boolean paramBoolean) {
    getActiveMySQLConnection().setIncludeInnodbStatusInDeadlockExceptions(paramBoolean);
  }
  
  public void setIncludeThreadDumpInDeadlockExceptions(boolean paramBoolean) {
    getActiveMySQLConnection().setIncludeThreadDumpInDeadlockExceptions(paramBoolean);
  }
  
  public void setIncludeThreadNamesAsStatementComment(boolean paramBoolean) {
    getActiveMySQLConnection().setIncludeThreadNamesAsStatementComment(paramBoolean);
  }
  
  public void setInitialTimeout(int paramInt) throws SQLException {
    getActiveMySQLConnection().setInitialTimeout(paramInt);
  }
  
  public void setInteractiveClient(boolean paramBoolean) {
    getActiveMySQLConnection().setInteractiveClient(paramBoolean);
  }
  
  public void setIsInteractiveClient(boolean paramBoolean) {
    getActiveMySQLConnection().setIsInteractiveClient(paramBoolean);
  }
  
  public void setJdbcCompliantTruncation(boolean paramBoolean) {
    getActiveMySQLConnection().setJdbcCompliantTruncation(paramBoolean);
  }
  
  public void setJdbcCompliantTruncationForReads(boolean paramBoolean) {
    getActiveMySQLConnection().setJdbcCompliantTruncationForReads(paramBoolean);
  }
  
  public void setLargeRowSizeThreshold(String paramString) throws SQLException {
    getActiveMySQLConnection().setLargeRowSizeThreshold(paramString);
  }
  
  public void setLoadBalanceAutoCommitStatementRegex(String paramString) {
    getActiveMySQLConnection().setLoadBalanceAutoCommitStatementRegex(paramString);
  }
  
  public void setLoadBalanceAutoCommitStatementThreshold(int paramInt) throws SQLException {
    getActiveMySQLConnection().setLoadBalanceAutoCommitStatementThreshold(paramInt);
  }
  
  public void setLoadBalanceBlacklistTimeout(int paramInt) throws SQLException {
    getActiveMySQLConnection().setLoadBalanceBlacklistTimeout(paramInt);
  }
  
  public void setLoadBalanceConnectionGroup(String paramString) {
    getActiveMySQLConnection().setLoadBalanceConnectionGroup(paramString);
  }
  
  public void setLoadBalanceEnableJMX(boolean paramBoolean) {
    getActiveMySQLConnection().setLoadBalanceEnableJMX(paramBoolean);
  }
  
  public void setLoadBalanceExceptionChecker(String paramString) {
    getActiveMySQLConnection().setLoadBalanceExceptionChecker(paramString);
  }
  
  public void setLoadBalanceHostRemovalGracePeriod(int paramInt) throws SQLException {
    getActiveMySQLConnection().setLoadBalanceHostRemovalGracePeriod(paramInt);
  }
  
  public void setLoadBalancePingTimeout(int paramInt) throws SQLException {
    getActiveMySQLConnection().setLoadBalancePingTimeout(paramInt);
  }
  
  public void setLoadBalanceSQLExceptionSubclassFailover(String paramString) {
    getActiveMySQLConnection().setLoadBalanceSQLExceptionSubclassFailover(paramString);
  }
  
  public void setLoadBalanceSQLStateFailover(String paramString) {
    getActiveMySQLConnection().setLoadBalanceSQLStateFailover(paramString);
  }
  
  public void setLoadBalanceStrategy(String paramString) {
    getActiveMySQLConnection().setLoadBalanceStrategy(paramString);
  }
  
  public void setLoadBalanceValidateConnectionOnSwapServer(boolean paramBoolean) {
    getActiveMySQLConnection().setLoadBalanceValidateConnectionOnSwapServer(paramBoolean);
  }
  
  public void setLocalSocketAddress(String paramString) {
    getActiveMySQLConnection().setLocalSocketAddress(paramString);
  }
  
  public void setLocatorFetchBufferSize(String paramString) throws SQLException {
    getActiveMySQLConnection().setLocatorFetchBufferSize(paramString);
  }
  
  public void setLogSlowQueries(boolean paramBoolean) {
    getActiveMySQLConnection().setLogSlowQueries(paramBoolean);
  }
  
  public void setLogXaCommands(boolean paramBoolean) {
    getActiveMySQLConnection().setLogXaCommands(paramBoolean);
  }
  
  public void setLogger(String paramString) {
    getActiveMySQLConnection().setLogger(paramString);
  }
  
  public void setLoggerClassName(String paramString) {
    getActiveMySQLConnection().setLoggerClassName(paramString);
  }
  
  public void setMaintainTimeStats(boolean paramBoolean) {
    getActiveMySQLConnection().setMaintainTimeStats(paramBoolean);
  }
  
  public void setMaxQuerySizeToLog(int paramInt) throws SQLException {
    getActiveMySQLConnection().setMaxQuerySizeToLog(paramInt);
  }
  
  public void setMaxReconnects(int paramInt) throws SQLException {
    getActiveMySQLConnection().setMaxReconnects(paramInt);
  }
  
  public void setMaxRows(int paramInt) throws SQLException {
    getActiveMySQLConnection().setMaxRows(paramInt);
  }
  
  public void setMetadataCacheSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setMetadataCacheSize(paramInt);
  }
  
  public void setNetTimeoutForStreamingResults(int paramInt) throws SQLException {
    getActiveMySQLConnection().setNetTimeoutForStreamingResults(paramInt);
  }
  
  public void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException {
    getActiveMySQLConnection().setNetworkTimeout(paramExecutor, paramInt);
  }
  
  public void setNoAccessToProcedureBodies(boolean paramBoolean) {
    getActiveMySQLConnection().setNoAccessToProcedureBodies(paramBoolean);
  }
  
  public void setNoDatetimeStringSync(boolean paramBoolean) {
    getActiveMySQLConnection().setNoDatetimeStringSync(paramBoolean);
  }
  
  public void setNoTimezoneConversionForDateType(boolean paramBoolean) {
    getActiveMySQLConnection().setNoTimezoneConversionForDateType(paramBoolean);
  }
  
  public void setNoTimezoneConversionForTimeType(boolean paramBoolean) {
    getActiveMySQLConnection().setNoTimezoneConversionForTimeType(paramBoolean);
  }
  
  public void setNullCatalogMeansCurrent(boolean paramBoolean) {
    getActiveMySQLConnection().setNullCatalogMeansCurrent(paramBoolean);
  }
  
  public void setNullNamePatternMatchesAll(boolean paramBoolean) {
    getActiveMySQLConnection().setNullNamePatternMatchesAll(paramBoolean);
  }
  
  public void setOverrideSupportsIntegrityEnhancementFacility(boolean paramBoolean) {
    getActiveMySQLConnection().setOverrideSupportsIntegrityEnhancementFacility(paramBoolean);
  }
  
  public void setPacketDebugBufferSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setPacketDebugBufferSize(paramInt);
  }
  
  public void setPadCharsWithSpace(boolean paramBoolean) {
    getActiveMySQLConnection().setPadCharsWithSpace(paramBoolean);
  }
  
  public void setParanoid(boolean paramBoolean) {
    getActiveMySQLConnection().setParanoid(paramBoolean);
  }
  
  public void setParseInfoCacheFactory(String paramString) {
    getActiveMySQLConnection().setParseInfoCacheFactory(paramString);
  }
  
  public void setPasswordCharacterEncoding(String paramString) {
    getActiveMySQLConnection().setPasswordCharacterEncoding(paramString);
  }
  
  public void setPedantic(boolean paramBoolean) {
    getActiveMySQLConnection().setPedantic(paramBoolean);
  }
  
  public void setPinGlobalTxToPhysicalConnection(boolean paramBoolean) {
    getActiveMySQLConnection().setPinGlobalTxToPhysicalConnection(paramBoolean);
  }
  
  public void setPopulateInsertRowWithDefaultValues(boolean paramBoolean) {
    getActiveMySQLConnection().setPopulateInsertRowWithDefaultValues(paramBoolean);
  }
  
  @Deprecated
  public void setPreferSlaveDuringFailover(boolean paramBoolean) {
    getActiveMySQLConnection().setPreferSlaveDuringFailover(paramBoolean);
  }
  
  public void setPrepStmtCacheSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setPrepStmtCacheSize(paramInt);
  }
  
  public void setPrepStmtCacheSqlLimit(int paramInt) throws SQLException {
    getActiveMySQLConnection().setPrepStmtCacheSqlLimit(paramInt);
  }
  
  public void setPreparedStatementCacheSize(int paramInt) throws SQLException {
    getActiveMySQLConnection().setPreparedStatementCacheSize(paramInt);
  }
  
  public void setPreparedStatementCacheSqlLimit(int paramInt) throws SQLException {
    getActiveMySQLConnection().setPreparedStatementCacheSqlLimit(paramInt);
  }
  
  public void setProcessEscapeCodesForPrepStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setProcessEscapeCodesForPrepStmts(paramBoolean);
  }
  
  public void setProfileSQL(boolean paramBoolean) {
    getActiveMySQLConnection().setProfileSQL(paramBoolean);
  }
  
  public void setProfileSql(boolean paramBoolean) {
    getActiveMySQLConnection().setProfileSql(paramBoolean);
  }
  
  public void setProfilerEventHandler(String paramString) {
    getActiveMySQLConnection().setProfilerEventHandler(paramString);
  }
  
  public void setProfilerEventHandlerInstance(ProfilerEventHandler paramProfilerEventHandler) {
    getActiveMySQLConnection().setProfilerEventHandlerInstance(paramProfilerEventHandler);
  }
  
  public void setPropertiesTransform(String paramString) {
    getActiveMySQLConnection().setPropertiesTransform(paramString);
  }
  
  public void setProxy(MySQLConnection paramMySQLConnection) {
    getThisAsProxy().setProxy(paramMySQLConnection);
  }
  
  public void setQueriesBeforeRetryMaster(int paramInt) throws SQLException {
    getActiveMySQLConnection().setQueriesBeforeRetryMaster(paramInt);
  }
  
  public void setQueryTimeoutKillsConnection(boolean paramBoolean) {
    getActiveMySQLConnection().setQueryTimeoutKillsConnection(paramBoolean);
  }
  
  public void setReadFromMasterWhenNoSlaves(boolean paramBoolean) {
    getActiveMySQLConnection().setReadFromMasterWhenNoSlaves(paramBoolean);
  }
  
  public void setReadInfoMsgEnabled(boolean paramBoolean) {
    getActiveMySQLConnection().setReadInfoMsgEnabled(paramBoolean);
  }
  
  public void setReadOnly(boolean paramBoolean) throws SQLException {
    getActiveMySQLConnection().setReadOnly(paramBoolean);
  }
  
  public void setReadOnlyInternal(boolean paramBoolean) throws SQLException {
    getActiveMySQLConnection().setReadOnlyInternal(paramBoolean);
  }
  
  public void setReadOnlyPropagatesToServer(boolean paramBoolean) {
    getActiveMySQLConnection().setReadOnlyPropagatesToServer(paramBoolean);
  }
  
  public void setReconnectAtTxEnd(boolean paramBoolean) {
    getActiveMySQLConnection().setReconnectAtTxEnd(paramBoolean);
  }
  
  public void setRelaxAutoCommit(boolean paramBoolean) {
    getActiveMySQLConnection().setRelaxAutoCommit(paramBoolean);
  }
  
  public void setReplicationEnableJMX(boolean paramBoolean) {
    getActiveMySQLConnection().setReplicationEnableJMX(paramBoolean);
  }
  
  public void setReportMetricsIntervalMillis(int paramInt) throws SQLException {
    getActiveMySQLConnection().setReportMetricsIntervalMillis(paramInt);
  }
  
  public void setRequireSSL(boolean paramBoolean) {
    getActiveMySQLConnection().setRequireSSL(paramBoolean);
  }
  
  public void setResourceId(String paramString) {
    getActiveMySQLConnection().setResourceId(paramString);
  }
  
  public void setResultSetSizeThreshold(int paramInt) throws SQLException {
    getActiveMySQLConnection().setResultSetSizeThreshold(paramInt);
  }
  
  public void setRetainStatementAfterResultSetClose(boolean paramBoolean) {
    getActiveMySQLConnection().setRetainStatementAfterResultSetClose(paramBoolean);
  }
  
  public void setRetriesAllDown(int paramInt) throws SQLException {
    getActiveMySQLConnection().setRetriesAllDown(paramInt);
  }
  
  public void setRewriteBatchedStatements(boolean paramBoolean) {
    getActiveMySQLConnection().setRewriteBatchedStatements(paramBoolean);
  }
  
  public void setRollbackOnPooledClose(boolean paramBoolean) {
    getActiveMySQLConnection().setRollbackOnPooledClose(paramBoolean);
  }
  
  public void setRoundRobinLoadBalance(boolean paramBoolean) {
    getActiveMySQLConnection().setRoundRobinLoadBalance(paramBoolean);
  }
  
  public void setRunningCTS13(boolean paramBoolean) {
    getActiveMySQLConnection().setRunningCTS13(paramBoolean);
  }
  
  public Savepoint setSavepoint() throws SQLException {
    return getActiveMySQLConnection().setSavepoint();
  }
  
  public Savepoint setSavepoint(String paramString) throws SQLException {
    return getActiveMySQLConnection().setSavepoint(paramString);
  }
  
  public void setSchema(String paramString) throws SQLException {
    getActiveMySQLConnection().setSchema(paramString);
  }
  
  public void setSecondsBeforeRetryMaster(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSecondsBeforeRetryMaster(paramInt);
  }
  
  public void setSelfDestructOnPingMaxOperations(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSelfDestructOnPingMaxOperations(paramInt);
  }
  
  public void setSelfDestructOnPingSecondsLifetime(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSelfDestructOnPingSecondsLifetime(paramInt);
  }
  
  public void setSendFractionalSeconds(boolean paramBoolean) {
    getActiveMySQLConnection().setSendFractionalSeconds(paramBoolean);
  }
  
  public void setServerAffinityOrder(String paramString) {
    getActiveMySQLConnection().setServerAffinityOrder(paramString);
  }
  
  public void setServerConfigCacheFactory(String paramString) {
    getActiveMySQLConnection().setServerConfigCacheFactory(paramString);
  }
  
  public void setServerRSAPublicKeyFile(String paramString) throws SQLException {
    getActiveMySQLConnection().setServerRSAPublicKeyFile(paramString);
  }
  
  public void setServerTimezone(String paramString) {
    getActiveMySQLConnection().setServerTimezone(paramString);
  }
  
  public void setSessionMaxRows(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSessionMaxRows(paramInt);
  }
  
  public void setSessionVariables(String paramString) {
    getActiveMySQLConnection().setSessionVariables(paramString);
  }
  
  public void setSlowQueryThresholdMillis(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSlowQueryThresholdMillis(paramInt);
  }
  
  public void setSlowQueryThresholdNanos(long paramLong) throws SQLException {
    getActiveMySQLConnection().setSlowQueryThresholdNanos(paramLong);
  }
  
  public void setSocketFactory(String paramString) {
    getActiveMySQLConnection().setSocketFactory(paramString);
  }
  
  public void setSocketFactoryClassName(String paramString) {
    getActiveMySQLConnection().setSocketFactoryClassName(paramString);
  }
  
  public void setSocketTimeout(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSocketTimeout(paramInt);
  }
  
  public void setSocksProxyHost(String paramString) {
    getActiveMySQLConnection().setSocksProxyHost(paramString);
  }
  
  public void setSocksProxyPort(int paramInt) throws SQLException {
    getActiveMySQLConnection().setSocksProxyPort(paramInt);
  }
  
  public void setStatementComment(String paramString) {
    getActiveMySQLConnection().setStatementComment(paramString);
  }
  
  public void setStatementInterceptors(String paramString) {
    getActiveMySQLConnection().setStatementInterceptors(paramString);
  }
  
  public void setStrictFloatingPoint(boolean paramBoolean) {
    getActiveMySQLConnection().setStrictFloatingPoint(paramBoolean);
  }
  
  public void setStrictUpdates(boolean paramBoolean) {
    getActiveMySQLConnection().setStrictUpdates(paramBoolean);
  }
  
  public void setTcpKeepAlive(boolean paramBoolean) {
    getActiveMySQLConnection().setTcpKeepAlive(paramBoolean);
  }
  
  public void setTcpNoDelay(boolean paramBoolean) {
    getActiveMySQLConnection().setTcpNoDelay(paramBoolean);
  }
  
  public void setTcpRcvBuf(int paramInt) throws SQLException {
    getActiveMySQLConnection().setTcpRcvBuf(paramInt);
  }
  
  public void setTcpSndBuf(int paramInt) throws SQLException {
    getActiveMySQLConnection().setTcpSndBuf(paramInt);
  }
  
  public void setTcpTrafficClass(int paramInt) throws SQLException {
    getActiveMySQLConnection().setTcpTrafficClass(paramInt);
  }
  
  public void setTinyInt1isBit(boolean paramBoolean) {
    getActiveMySQLConnection().setTinyInt1isBit(paramBoolean);
  }
  
  public void setTraceProtocol(boolean paramBoolean) {
    getActiveMySQLConnection().setTraceProtocol(paramBoolean);
  }
  
  public void setTransactionIsolation(int paramInt) throws SQLException {
    getActiveMySQLConnection().setTransactionIsolation(paramInt);
  }
  
  public void setTransformedBitIsBoolean(boolean paramBoolean) {
    getActiveMySQLConnection().setTransformedBitIsBoolean(paramBoolean);
  }
  
  public void setTreatUtilDateAsTimestamp(boolean paramBoolean) {
    getActiveMySQLConnection().setTreatUtilDateAsTimestamp(paramBoolean);
  }
  
  public void setTrustCertificateKeyStorePassword(String paramString) {
    getActiveMySQLConnection().setTrustCertificateKeyStorePassword(paramString);
  }
  
  public void setTrustCertificateKeyStoreType(String paramString) {
    getActiveMySQLConnection().setTrustCertificateKeyStoreType(paramString);
  }
  
  public void setTrustCertificateKeyStoreUrl(String paramString) {
    getActiveMySQLConnection().setTrustCertificateKeyStoreUrl(paramString);
  }
  
  public void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
    getActiveMySQLConnection().setTypeMap(paramMap);
  }
  
  public void setUltraDevHack(boolean paramBoolean) {
    getActiveMySQLConnection().setUltraDevHack(paramBoolean);
  }
  
  public void setUseAffectedRows(boolean paramBoolean) {
    getActiveMySQLConnection().setUseAffectedRows(paramBoolean);
  }
  
  public void setUseBlobToStoreUTF8OutsideBMP(boolean paramBoolean) {
    getActiveMySQLConnection().setUseBlobToStoreUTF8OutsideBMP(paramBoolean);
  }
  
  public void setUseColumnNamesInFindColumn(boolean paramBoolean) {
    getActiveMySQLConnection().setUseColumnNamesInFindColumn(paramBoolean);
  }
  
  public void setUseCompression(boolean paramBoolean) {
    getActiveMySQLConnection().setUseCompression(paramBoolean);
  }
  
  public void setUseConfigs(String paramString) {
    getActiveMySQLConnection().setUseConfigs(paramString);
  }
  
  public void setUseCursorFetch(boolean paramBoolean) {
    getActiveMySQLConnection().setUseCursorFetch(paramBoolean);
  }
  
  public void setUseDirectRowUnpack(boolean paramBoolean) {
    getActiveMySQLConnection().setUseDirectRowUnpack(paramBoolean);
  }
  
  public void setUseDynamicCharsetInfo(boolean paramBoolean) {
    getActiveMySQLConnection().setUseDynamicCharsetInfo(paramBoolean);
  }
  
  public void setUseFastDateParsing(boolean paramBoolean) {
    getActiveMySQLConnection().setUseFastDateParsing(paramBoolean);
  }
  
  public void setUseFastIntParsing(boolean paramBoolean) {
    getActiveMySQLConnection().setUseFastIntParsing(paramBoolean);
  }
  
  public void setUseGmtMillisForDatetimes(boolean paramBoolean) {
    getActiveMySQLConnection().setUseGmtMillisForDatetimes(paramBoolean);
  }
  
  public void setUseHostsInPrivileges(boolean paramBoolean) {
    getActiveMySQLConnection().setUseHostsInPrivileges(paramBoolean);
  }
  
  public void setUseInformationSchema(boolean paramBoolean) {
    getActiveMySQLConnection().setUseInformationSchema(paramBoolean);
  }
  
  public void setUseJDBCCompliantTimezoneShift(boolean paramBoolean) {
    getActiveMySQLConnection().setUseJDBCCompliantTimezoneShift(paramBoolean);
  }
  
  public void setUseJvmCharsetConverters(boolean paramBoolean) {
    getActiveMySQLConnection().setUseJvmCharsetConverters(paramBoolean);
  }
  
  public void setUseLegacyDatetimeCode(boolean paramBoolean) {
    getActiveMySQLConnection().setUseLegacyDatetimeCode(paramBoolean);
  }
  
  public void setUseLocalSessionState(boolean paramBoolean) {
    getActiveMySQLConnection().setUseLocalSessionState(paramBoolean);
  }
  
  public void setUseLocalTransactionState(boolean paramBoolean) {
    getActiveMySQLConnection().setUseLocalTransactionState(paramBoolean);
  }
  
  public void setUseNanosForElapsedTime(boolean paramBoolean) {
    getActiveMySQLConnection().setUseNanosForElapsedTime(paramBoolean);
  }
  
  public void setUseOldAliasMetadataBehavior(boolean paramBoolean) {
    getActiveMySQLConnection().setUseOldAliasMetadataBehavior(paramBoolean);
  }
  
  public void setUseOldUTF8Behavior(boolean paramBoolean) {
    getActiveMySQLConnection().setUseOldUTF8Behavior(paramBoolean);
  }
  
  public void setUseOnlyServerErrorMessages(boolean paramBoolean) {
    getActiveMySQLConnection().setUseOnlyServerErrorMessages(paramBoolean);
  }
  
  public void setUseReadAheadInput(boolean paramBoolean) {
    getActiveMySQLConnection().setUseReadAheadInput(paramBoolean);
  }
  
  public void setUseSSL(boolean paramBoolean) {
    getActiveMySQLConnection().setUseSSL(paramBoolean);
  }
  
  public void setUseSSPSCompatibleTimezoneShift(boolean paramBoolean) {
    getActiveMySQLConnection().setUseSSPSCompatibleTimezoneShift(paramBoolean);
  }
  
  public void setUseServerPrepStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setUseServerPrepStmts(paramBoolean);
  }
  
  public void setUseServerPreparedStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setUseServerPreparedStmts(paramBoolean);
  }
  
  public void setUseSqlStateCodes(boolean paramBoolean) {
    getActiveMySQLConnection().setUseSqlStateCodes(paramBoolean);
  }
  
  public void setUseStreamLengthsInPrepStmts(boolean paramBoolean) {
    getActiveMySQLConnection().setUseStreamLengthsInPrepStmts(paramBoolean);
  }
  
  public void setUseTimezone(boolean paramBoolean) {
    getActiveMySQLConnection().setUseTimezone(paramBoolean);
  }
  
  public void setUseUltraDevWorkAround(boolean paramBoolean) {
    getActiveMySQLConnection().setUseUltraDevWorkAround(paramBoolean);
  }
  
  public void setUseUnbufferedInput(boolean paramBoolean) {
    getActiveMySQLConnection().setUseUnbufferedInput(paramBoolean);
  }
  
  public void setUseUnicode(boolean paramBoolean) {
    getActiveMySQLConnection().setUseUnicode(paramBoolean);
  }
  
  public void setUseUsageAdvisor(boolean paramBoolean) {
    getActiveMySQLConnection().setUseUsageAdvisor(paramBoolean);
  }
  
  public void setUtf8OutsideBmpExcludedColumnNamePattern(String paramString) {
    getActiveMySQLConnection().setUtf8OutsideBmpExcludedColumnNamePattern(paramString);
  }
  
  public void setUtf8OutsideBmpIncludedColumnNamePattern(String paramString) {
    getActiveMySQLConnection().setUtf8OutsideBmpIncludedColumnNamePattern(paramString);
  }
  
  public void setVerifyServerCertificate(boolean paramBoolean) {
    getActiveMySQLConnection().setVerifyServerCertificate(paramBoolean);
  }
  
  public void setYearIsDateType(boolean paramBoolean) {
    getActiveMySQLConnection().setYearIsDateType(paramBoolean);
  }
  
  public void setZeroDateTimeBehavior(String paramString) {
    getActiveMySQLConnection().setZeroDateTimeBehavior(paramString);
  }
  
  public void shutdownServer() throws SQLException {
    getActiveMySQLConnection().shutdownServer();
  }
  
  public boolean storesLowerCaseTableName() {
    return getActiveMySQLConnection().storesLowerCaseTableName();
  }
  
  public boolean supportsIsolationLevel() {
    return getActiveMySQLConnection().supportsIsolationLevel();
  }
  
  public boolean supportsQuotedIdentifiers() {
    return getActiveMySQLConnection().supportsQuotedIdentifiers();
  }
  
  public boolean supportsTransactions() {
    return getActiveMySQLConnection().supportsTransactions();
  }
  
  public void throwConnectionClosedException() throws SQLException {
    getActiveMySQLConnection().throwConnectionClosedException();
  }
  
  public void transactionBegun() throws SQLException {
    getActiveMySQLConnection().transactionBegun();
  }
  
  public void transactionCompleted() throws SQLException {
    getActiveMySQLConnection().transactionCompleted();
  }
  
  public void unSafeStatementInterceptors() throws SQLException {
    getActiveMySQLConnection().unSafeStatementInterceptors();
  }
  
  public void unregisterStatement(Statement paramStatement) {
    getActiveMySQLConnection().unregisterStatement(paramStatement);
  }
  
  public boolean useAnsiQuotedIdentifiers() {
    return getActiveMySQLConnection().useAnsiQuotedIdentifiers();
  }
  
  public boolean useUnbufferedInput() {
    return getActiveMySQLConnection().useUnbufferedInput();
  }
  
  public boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    return getActiveMySQLConnection().versionMeetsMinimum(paramInt1, paramInt2, paramInt3);
  }
}
