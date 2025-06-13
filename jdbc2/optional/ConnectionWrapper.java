package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Extension;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import com.mysql.jdbc.log.Log;
import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public class ConnectionWrapper extends WrapperBase implements Connection {
  private static final Constructor<?> JDBC_4_CONNECTION_WRAPPER_CTOR;
  
  private boolean closed;
  
  private String invalidHandleStr = "Logical handle no longer valid";
  
  private boolean isForXa;
  
  public Connection mc = null;
  
  static {
    if (Util.isJdbc4()) {
      try {
        JDBC_4_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4ConnectionWrapper").getConstructor(new Class[] { MysqlPooledConnection.class, Connection.class, boolean.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_CONNECTION_WRAPPER_CTOR = null;
    } 
  }
  
  public ConnectionWrapper(MysqlPooledConnection paramMysqlPooledConnection, Connection paramConnection, boolean paramBoolean) throws SQLException {
    super(paramMysqlPooledConnection);
    this.mc = paramConnection;
    this.closed = false;
    this.isForXa = paramBoolean;
    if (paramBoolean)
      setInGlobalTx(false); 
  }
  
  public static ConnectionWrapper getInstance(MysqlPooledConnection paramMysqlPooledConnection, Connection paramConnection, boolean paramBoolean) throws SQLException {
    if (!Util.isJdbc4())
      return new ConnectionWrapper(paramMysqlPooledConnection, paramConnection, paramBoolean); 
    Constructor<?> constructor = JDBC_4_CONNECTION_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMysqlPooledConnection.getExceptionInterceptor();
    return (ConnectionWrapper)Util.handleNewInstance(constructor, new Object[] { paramMysqlPooledConnection, paramConnection, Boolean.valueOf(paramBoolean) }, exceptionInterceptor);
  }
  
  public void abort(Executor paramExecutor) throws SQLException {
    this.mc.abort(paramExecutor);
  }
  
  public void abortInternal() throws SQLException {
    this.mc.abortInternal();
  }
  
  public void changeUser(String paramString1, String paramString2) throws SQLException {
    checkClosed();
    try {
      this.mc.changeUser(paramString1, paramString2);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void checkClosed() throws SQLException {
    if (!this.closed)
      return; 
    throw SQLError.createSQLException(this.invalidHandleStr, this.exceptionInterceptor);
  }
  
  @Deprecated
  public void clearHasTriedMaster() {
    this.mc.clearHasTriedMaster();
  }
  
  public void clearWarnings() throws SQLException {
    checkClosed();
    try {
      this.mc.clearWarnings();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public PreparedStatement clientPrepare(String paramString) throws SQLException {
    checkClosed();
    try {
      return new PreparedStatementWrapper(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepare(String paramString, int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    try {
      return new PreparedStatementWrapper(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString, paramInt1, paramInt2));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepareStatement(String paramString) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString, paramInt));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString, paramInt1, paramInt2));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString, paramInt1, paramInt2, paramInt3));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString, paramArrayOfint));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement clientPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(paramString, paramArrayOfString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public void close() throws SQLException {
    close(true);
  }
  
  public void close(boolean paramBoolean) throws SQLException {
    synchronized (this.pooledConnection) {
      if (this.closed)
        return; 
      if (!isInGlobalTx() && this.mc.getRollbackOnPooledClose() && !getAutoCommit())
        rollback(); 
      if (paramBoolean)
        this.pooledConnection.callConnectionEventListeners(2, null); 
      this.closed = true;
      return;
    } 
  }
  
  public void commit() throws SQLException {
    checkClosed();
    if (!isInGlobalTx()) {
      try {
        this.mc.commit();
      } catch (SQLException sQLException) {
        checkAndFireConnectionError(sQLException);
      } 
      return;
    } 
    throw SQLError.createSQLException("Can't call commit() on an XAConnection associated with a global transaction", "2D000", 1401, this.exceptionInterceptor);
  }
  
  public Statement createStatement() throws SQLException {
    checkClosed();
    try {
      return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement());
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Statement createStatement(int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    try {
      return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement(paramInt1, paramInt2));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    checkClosed();
    try {
      return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement(paramInt1, paramInt2, paramInt3));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public String exposeAsXml() throws SQLException {
    checkClosed();
    try {
      return this.mc.exposeAsXml();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public int getActiveStatementCount() {
    return this.mc.getActiveStatementCount();
  }
  
  public boolean getAllowLoadLocalInfile() {
    return this.mc.getAllowLoadLocalInfile();
  }
  
  public boolean getAllowMasterDownConnections() {
    return this.mc.getAllowMasterDownConnections();
  }
  
  public boolean getAllowMultiQueries() {
    return this.mc.getAllowMultiQueries();
  }
  
  public boolean getAllowNanAndInf() {
    return this.mc.getAllowNanAndInf();
  }
  
  public boolean getAllowPublicKeyRetrieval() {
    return this.mc.getAllowPublicKeyRetrieval();
  }
  
  public boolean getAllowSlaveDownConnections() {
    return this.mc.getAllowSlaveDownConnections();
  }
  
  public boolean getAllowUrlInLocalInfile() {
    return this.mc.getAllowUrlInLocalInfile();
  }
  
  public boolean getAlwaysSendSetIsolation() {
    return this.mc.getAlwaysSendSetIsolation();
  }
  
  public String getAuthenticationPlugins() {
    return this.mc.getAuthenticationPlugins();
  }
  
  public boolean getAutoClosePStmtStreams() {
    return this.mc.getAutoClosePStmtStreams();
  }
  
  public boolean getAutoCommit() throws SQLException {
    checkClosed();
    try {
      return this.mc.getAutoCommit();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean getAutoDeserialize() {
    return this.mc.getAutoDeserialize();
  }
  
  public boolean getAutoGenerateTestcaseScript() {
    return this.mc.getAutoGenerateTestcaseScript();
  }
  
  public int getAutoIncrementIncrement() {
    return this.mc.getAutoIncrementIncrement();
  }
  
  public boolean getAutoReconnectForPools() {
    return this.mc.getAutoReconnectForPools();
  }
  
  public boolean getAutoSlowLog() {
    return this.mc.getAutoSlowLog();
  }
  
  public int getBlobSendChunkSize() {
    return this.mc.getBlobSendChunkSize();
  }
  
  public boolean getBlobsAreStrings() {
    return this.mc.getBlobsAreStrings();
  }
  
  public boolean getCacheCallableStatements() {
    return this.mc.getCacheCallableStatements();
  }
  
  public boolean getCacheCallableStmts() {
    return this.mc.getCacheCallableStmts();
  }
  
  public boolean getCacheDefaultTimezone() {
    return this.mc.getCacheDefaultTimezone();
  }
  
  public boolean getCachePrepStmts() {
    return this.mc.getCachePrepStmts();
  }
  
  public boolean getCachePreparedStatements() {
    return this.mc.getCachePreparedStatements();
  }
  
  public boolean getCacheResultSetMetadata() {
    return this.mc.getCacheResultSetMetadata();
  }
  
  public boolean getCacheServerConfiguration() {
    return this.mc.getCacheServerConfiguration();
  }
  
  public int getCallableStatementCacheSize() {
    return this.mc.getCallableStatementCacheSize();
  }
  
  public int getCallableStmtCacheSize() {
    return this.mc.getCallableStmtCacheSize();
  }
  
  public boolean getCapitalizeTypeNames() {
    return this.mc.getCapitalizeTypeNames();
  }
  
  public String getCatalog() throws SQLException {
    checkClosed();
    try {
      return this.mc.getCatalog();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public String getCharacterSetResults() {
    return this.mc.getCharacterSetResults();
  }
  
  public String getClientCertificateKeyStorePassword() {
    return this.mc.getClientCertificateKeyStorePassword();
  }
  
  public String getClientCertificateKeyStoreType() {
    return this.mc.getClientCertificateKeyStoreType();
  }
  
  public String getClientCertificateKeyStoreUrl() {
    return this.mc.getClientCertificateKeyStoreUrl();
  }
  
  public String getClientInfoProvider() {
    return this.mc.getClientInfoProvider();
  }
  
  public String getClobCharacterEncoding() {
    return this.mc.getClobCharacterEncoding();
  }
  
  public boolean getClobberStreamingResults() {
    return this.mc.getClobberStreamingResults();
  }
  
  public boolean getCompensateOnDuplicateKeyUpdateCounts() {
    return this.mc.getCompensateOnDuplicateKeyUpdateCounts();
  }
  
  public int getConnectTimeout() {
    return this.mc.getConnectTimeout();
  }
  
  public String getConnectionAttributes() throws SQLException {
    return this.mc.getConnectionAttributes();
  }
  
  public String getConnectionCollation() {
    return this.mc.getConnectionCollation();
  }
  
  public String getConnectionLifecycleInterceptors() {
    return this.mc.getConnectionLifecycleInterceptors();
  }
  
  public Object getConnectionMutex() {
    return this.mc.getConnectionMutex();
  }
  
  public boolean getContinueBatchOnError() {
    return this.mc.getContinueBatchOnError();
  }
  
  public boolean getCreateDatabaseIfNotExist() {
    return this.mc.getCreateDatabaseIfNotExist();
  }
  
  public String getDefaultAuthenticationPlugin() {
    return this.mc.getDefaultAuthenticationPlugin();
  }
  
  public int getDefaultFetchSize() {
    return this.mc.getDefaultFetchSize();
  }
  
  public boolean getDetectCustomCollations() {
    return this.mc.getDetectCustomCollations();
  }
  
  public String getDisabledAuthenticationPlugins() {
    return this.mc.getDisabledAuthenticationPlugins();
  }
  
  public boolean getDisconnectOnExpiredPasswords() {
    return this.mc.getDisconnectOnExpiredPasswords();
  }
  
  public boolean getDontCheckOnDuplicateKeyUpdateInSQL() {
    return this.mc.getDontCheckOnDuplicateKeyUpdateInSQL();
  }
  
  public boolean getDontTrackOpenResources() {
    return this.mc.getDontTrackOpenResources();
  }
  
  public boolean getDumpMetadataOnColumnNotFound() {
    return this.mc.getDumpMetadataOnColumnNotFound();
  }
  
  public boolean getDumpQueriesOnException() {
    return this.mc.getDumpQueriesOnException();
  }
  
  public boolean getDynamicCalendars() {
    return this.mc.getDynamicCalendars();
  }
  
  public boolean getElideSetAutoCommits() {
    return this.mc.getElideSetAutoCommits();
  }
  
  public boolean getEmptyStringsConvertToZero() {
    return this.mc.getEmptyStringsConvertToZero();
  }
  
  public boolean getEmulateLocators() {
    return this.mc.getEmulateLocators();
  }
  
  public boolean getEmulateUnsupportedPstmts() {
    return this.mc.getEmulateUnsupportedPstmts();
  }
  
  public boolean getEnableEscapeProcessing() {
    return this.mc.getEnableEscapeProcessing();
  }
  
  public boolean getEnablePacketDebug() {
    return this.mc.getEnablePacketDebug();
  }
  
  public boolean getEnableQueryTimeouts() {
    return this.mc.getEnableQueryTimeouts();
  }
  
  public String getEnabledSSLCipherSuites() {
    return this.mc.getEnabledSSLCipherSuites();
  }
  
  public String getEnabledTLSProtocols() {
    return this.mc.getEnabledTLSProtocols();
  }
  
  public String getEncoding() {
    return this.mc.getEncoding();
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.pooledConnection.getExceptionInterceptor();
  }
  
  public String getExceptionInterceptors() {
    return this.mc.getExceptionInterceptors();
  }
  
  public boolean getExplainSlowQueries() {
    return this.mc.getExplainSlowQueries();
  }
  
  public boolean getFailOverReadOnly() {
    return this.mc.getFailOverReadOnly();
  }
  
  public boolean getFunctionsNeverReturnBlobs() {
    return this.mc.getFunctionsNeverReturnBlobs();
  }
  
  public boolean getGatherPerfMetrics() {
    return this.mc.getGatherPerfMetrics();
  }
  
  public boolean getGatherPerformanceMetrics() {
    return this.mc.getGatherPerformanceMetrics();
  }
  
  public boolean getGenerateSimpleParameterMetadata() {
    return this.mc.getGenerateSimpleParameterMetadata();
  }
  
  public boolean getGetProceduresReturnsFunctions() {
    return this.mc.getGetProceduresReturnsFunctions();
  }
  
  public boolean getHoldResultsOpenOverStatementClose() {
    return this.mc.getHoldResultsOpenOverStatementClose();
  }
  
  public int getHoldability() throws SQLException {
    checkClosed();
    try {
      return this.mc.getHoldability();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 1;
    } 
  }
  
  public String getHost() {
    return this.mc.getHost();
  }
  
  public long getIdleFor() {
    return this.mc.getIdleFor();
  }
  
  public boolean getIgnoreNonTxTables() {
    return this.mc.getIgnoreNonTxTables();
  }
  
  public boolean getIncludeInnodbStatusInDeadlockExceptions() {
    return this.mc.getIncludeInnodbStatusInDeadlockExceptions();
  }
  
  public boolean getIncludeThreadDumpInDeadlockExceptions() {
    return this.mc.getIncludeThreadDumpInDeadlockExceptions();
  }
  
  public boolean getIncludeThreadNamesAsStatementComment() {
    return this.mc.getIncludeThreadNamesAsStatementComment();
  }
  
  public int getInitialTimeout() {
    return this.mc.getInitialTimeout();
  }
  
  public boolean getInteractiveClient() {
    return this.mc.getInteractiveClient();
  }
  
  public boolean getIsInteractiveClient() {
    return this.mc.getIsInteractiveClient();
  }
  
  public boolean getJdbcCompliantTruncation() {
    return this.mc.getJdbcCompliantTruncation();
  }
  
  public boolean getJdbcCompliantTruncationForReads() {
    return this.mc.getJdbcCompliantTruncationForReads();
  }
  
  public String getLargeRowSizeThreshold() {
    return this.mc.getLargeRowSizeThreshold();
  }
  
  public String getLoadBalanceAutoCommitStatementRegex() {
    return this.mc.getLoadBalanceAutoCommitStatementRegex();
  }
  
  public int getLoadBalanceAutoCommitStatementThreshold() {
    return this.mc.getLoadBalanceAutoCommitStatementThreshold();
  }
  
  public int getLoadBalanceBlacklistTimeout() {
    return this.mc.getLoadBalanceBlacklistTimeout();
  }
  
  public String getLoadBalanceConnectionGroup() {
    return this.mc.getLoadBalanceConnectionGroup();
  }
  
  public boolean getLoadBalanceEnableJMX() {
    return this.mc.getLoadBalanceEnableJMX();
  }
  
  public String getLoadBalanceExceptionChecker() {
    return this.mc.getLoadBalanceExceptionChecker();
  }
  
  public int getLoadBalanceHostRemovalGracePeriod() {
    return this.mc.getLoadBalanceHostRemovalGracePeriod();
  }
  
  public int getLoadBalancePingTimeout() {
    return this.mc.getLoadBalancePingTimeout();
  }
  
  public String getLoadBalanceSQLExceptionSubclassFailover() {
    return this.mc.getLoadBalanceSQLExceptionSubclassFailover();
  }
  
  public String getLoadBalanceSQLStateFailover() {
    return this.mc.getLoadBalanceSQLStateFailover();
  }
  
  public String getLoadBalanceStrategy() {
    return this.mc.getLoadBalanceStrategy();
  }
  
  public boolean getLoadBalanceValidateConnectionOnSwapServer() {
    return this.mc.getLoadBalanceValidateConnectionOnSwapServer();
  }
  
  public String getLocalSocketAddress() {
    return this.mc.getLocalSocketAddress();
  }
  
  public int getLocatorFetchBufferSize() {
    return this.mc.getLocatorFetchBufferSize();
  }
  
  public Log getLog() throws SQLException {
    return this.mc.getLog();
  }
  
  public boolean getLogSlowQueries() {
    return this.mc.getLogSlowQueries();
  }
  
  public boolean getLogXaCommands() {
    return this.mc.getLogXaCommands();
  }
  
  public String getLogger() {
    return this.mc.getLogger();
  }
  
  public String getLoggerClassName() {
    return this.mc.getLoggerClassName();
  }
  
  public boolean getMaintainTimeStats() {
    return this.mc.getMaintainTimeStats();
  }
  
  public int getMaxAllowedPacket() {
    return this.mc.getMaxAllowedPacket();
  }
  
  public int getMaxQuerySizeToLog() {
    return this.mc.getMaxQuerySizeToLog();
  }
  
  public int getMaxReconnects() {
    return this.mc.getMaxReconnects();
  }
  
  public int getMaxRows() {
    return this.mc.getMaxRows();
  }
  
  public DatabaseMetaData getMetaData() throws SQLException {
    checkClosed();
    try {
      return this.mc.getMetaData();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public int getMetadataCacheSize() {
    return this.mc.getMetadataCacheSize();
  }
  
  public int getNetTimeoutForStreamingResults() {
    return this.mc.getNetTimeoutForStreamingResults();
  }
  
  public int getNetworkTimeout() throws SQLException {
    return this.mc.getNetworkTimeout();
  }
  
  public boolean getNoAccessToProcedureBodies() {
    return this.mc.getNoAccessToProcedureBodies();
  }
  
  public boolean getNoDatetimeStringSync() {
    return this.mc.getNoDatetimeStringSync();
  }
  
  public boolean getNoTimezoneConversionForDateType() {
    return this.mc.getNoTimezoneConversionForDateType();
  }
  
  public boolean getNoTimezoneConversionForTimeType() {
    return this.mc.getNoTimezoneConversionForTimeType();
  }
  
  public boolean getNullCatalogMeansCurrent() {
    return this.mc.getNullCatalogMeansCurrent();
  }
  
  public boolean getNullNamePatternMatchesAll() {
    return this.mc.getNullNamePatternMatchesAll();
  }
  
  public boolean getOverrideSupportsIntegrityEnhancementFacility() {
    return this.mc.getOverrideSupportsIntegrityEnhancementFacility();
  }
  
  public int getPacketDebugBufferSize() {
    return this.mc.getPacketDebugBufferSize();
  }
  
  public boolean getPadCharsWithSpace() {
    return this.mc.getPadCharsWithSpace();
  }
  
  public boolean getParanoid() {
    return this.mc.getParanoid();
  }
  
  public String getParseInfoCacheFactory() {
    return this.mc.getParseInfoCacheFactory();
  }
  
  public String getPasswordCharacterEncoding() {
    return this.mc.getPasswordCharacterEncoding();
  }
  
  public boolean getPedantic() {
    return this.mc.getPedantic();
  }
  
  public boolean getPinGlobalTxToPhysicalConnection() {
    return this.mc.getPinGlobalTxToPhysicalConnection();
  }
  
  public boolean getPopulateInsertRowWithDefaultValues() {
    return this.mc.getPopulateInsertRowWithDefaultValues();
  }
  
  public int getPrepStmtCacheSize() {
    return this.mc.getPrepStmtCacheSize();
  }
  
  public int getPrepStmtCacheSqlLimit() {
    return this.mc.getPrepStmtCacheSqlLimit();
  }
  
  public int getPreparedStatementCacheSize() {
    return this.mc.getPreparedStatementCacheSize();
  }
  
  public int getPreparedStatementCacheSqlLimit() {
    return this.mc.getPreparedStatementCacheSqlLimit();
  }
  
  public boolean getProcessEscapeCodesForPrepStmts() {
    return this.mc.getProcessEscapeCodesForPrepStmts();
  }
  
  public boolean getProfileSQL() {
    return this.mc.getProfileSQL();
  }
  
  public boolean getProfileSql() {
    return this.mc.getProfileSql();
  }
  
  public String getProfilerEventHandler() {
    return this.mc.getProfilerEventHandler();
  }
  
  public Properties getProperties() {
    return this.mc.getProperties();
  }
  
  public String getPropertiesTransform() {
    return this.mc.getPropertiesTransform();
  }
  
  public int getQueriesBeforeRetryMaster() {
    return this.mc.getQueriesBeforeRetryMaster();
  }
  
  public boolean getQueryTimeoutKillsConnection() {
    return this.mc.getQueryTimeoutKillsConnection();
  }
  
  public boolean getReadFromMasterWhenNoSlaves() {
    return this.mc.getReadFromMasterWhenNoSlaves();
  }
  
  public boolean getReadOnlyPropagatesToServer() {
    return this.mc.getReadOnlyPropagatesToServer();
  }
  
  public boolean getReconnectAtTxEnd() {
    return this.mc.getReconnectAtTxEnd();
  }
  
  public boolean getRelaxAutoCommit() {
    return this.mc.getRelaxAutoCommit();
  }
  
  public boolean getReplicationEnableJMX() {
    return this.mc.getReplicationEnableJMX();
  }
  
  public int getReportMetricsIntervalMillis() {
    return this.mc.getReportMetricsIntervalMillis();
  }
  
  public boolean getRequireSSL() {
    return this.mc.getRequireSSL();
  }
  
  public String getResourceId() {
    return this.mc.getResourceId();
  }
  
  public int getResultSetSizeThreshold() {
    return this.mc.getResultSetSizeThreshold();
  }
  
  public boolean getRetainStatementAfterResultSetClose() {
    return this.mc.getRetainStatementAfterResultSetClose();
  }
  
  public int getRetriesAllDown() {
    return this.mc.getRetriesAllDown();
  }
  
  public boolean getRewriteBatchedStatements() {
    return this.mc.getRewriteBatchedStatements();
  }
  
  public boolean getRollbackOnPooledClose() {
    return this.mc.getRollbackOnPooledClose();
  }
  
  public boolean getRoundRobinLoadBalance() {
    return this.mc.getRoundRobinLoadBalance();
  }
  
  public boolean getRunningCTS13() {
    return this.mc.getRunningCTS13();
  }
  
  public String getSchema() throws SQLException {
    return this.mc.getSchema();
  }
  
  public int getSecondsBeforeRetryMaster() {
    return this.mc.getSecondsBeforeRetryMaster();
  }
  
  public int getSelfDestructOnPingMaxOperations() {
    return this.mc.getSelfDestructOnPingMaxOperations();
  }
  
  public int getSelfDestructOnPingSecondsLifetime() {
    return this.mc.getSelfDestructOnPingSecondsLifetime();
  }
  
  public boolean getSendFractionalSeconds() {
    return this.mc.getSendFractionalSeconds();
  }
  
  public String getServerAffinityOrder() {
    return this.mc.getServerAffinityOrder();
  }
  
  @Deprecated
  public String getServerCharacterEncoding() {
    return getServerCharset();
  }
  
  public String getServerCharset() {
    return this.mc.getServerCharset();
  }
  
  public String getServerConfigCacheFactory() {
    return this.mc.getServerConfigCacheFactory();
  }
  
  public String getServerRSAPublicKeyFile() {
    return this.mc.getServerRSAPublicKeyFile();
  }
  
  public String getServerTimezone() {
    return this.mc.getServerTimezone();
  }
  
  public TimeZone getServerTimezoneTZ() {
    return this.mc.getServerTimezoneTZ();
  }
  
  public int getSessionMaxRows() {
    return this.mc.getSessionMaxRows();
  }
  
  public String getSessionVariables() {
    return this.mc.getSessionVariables();
  }
  
  public int getSlowQueryThresholdMillis() {
    return this.mc.getSlowQueryThresholdMillis();
  }
  
  public long getSlowQueryThresholdNanos() {
    return this.mc.getSlowQueryThresholdNanos();
  }
  
  public String getSocketFactory() {
    return this.mc.getSocketFactory();
  }
  
  public String getSocketFactoryClassName() {
    return this.mc.getSocketFactoryClassName();
  }
  
  public int getSocketTimeout() {
    return this.mc.getSocketTimeout();
  }
  
  public String getSocksProxyHost() {
    return this.mc.getSocksProxyHost();
  }
  
  public int getSocksProxyPort() {
    return this.mc.getSocksProxyPort();
  }
  
  public String getStatementComment() {
    return this.mc.getStatementComment();
  }
  
  public String getStatementInterceptors() {
    return this.mc.getStatementInterceptors();
  }
  
  public boolean getStrictFloatingPoint() {
    return this.mc.getStrictFloatingPoint();
  }
  
  public boolean getStrictUpdates() {
    return this.mc.getStrictUpdates();
  }
  
  public boolean getTcpKeepAlive() {
    return this.mc.getTcpKeepAlive();
  }
  
  public boolean getTcpNoDelay() {
    return this.mc.getTcpNoDelay();
  }
  
  public int getTcpRcvBuf() {
    return this.mc.getTcpRcvBuf();
  }
  
  public int getTcpSndBuf() {
    return this.mc.getTcpSndBuf();
  }
  
  public int getTcpTrafficClass() {
    return this.mc.getTcpTrafficClass();
  }
  
  public boolean getTinyInt1isBit() {
    return this.mc.getTinyInt1isBit();
  }
  
  public boolean getTraceProtocol() {
    return this.mc.getTraceProtocol();
  }
  
  public int getTransactionIsolation() throws SQLException {
    checkClosed();
    try {
      return this.mc.getTransactionIsolation();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 4;
    } 
  }
  
  public boolean getTransformedBitIsBoolean() {
    return this.mc.getTransformedBitIsBoolean();
  }
  
  public boolean getTreatUtilDateAsTimestamp() {
    return this.mc.getTreatUtilDateAsTimestamp();
  }
  
  public String getTrustCertificateKeyStorePassword() {
    return this.mc.getTrustCertificateKeyStorePassword();
  }
  
  public String getTrustCertificateKeyStoreType() {
    return this.mc.getTrustCertificateKeyStoreType();
  }
  
  public String getTrustCertificateKeyStoreUrl() {
    return this.mc.getTrustCertificateKeyStoreUrl();
  }
  
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    checkClosed();
    try {
      return this.mc.getTypeMap();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public boolean getUltraDevHack() {
    return this.mc.getUltraDevHack();
  }
  
  public boolean getUseAffectedRows() {
    return this.mc.getUseAffectedRows();
  }
  
  public boolean getUseBlobToStoreUTF8OutsideBMP() {
    return this.mc.getUseBlobToStoreUTF8OutsideBMP();
  }
  
  public boolean getUseColumnNamesInFindColumn() {
    return this.mc.getUseColumnNamesInFindColumn();
  }
  
  public boolean getUseCompression() {
    return this.mc.getUseCompression();
  }
  
  public String getUseConfigs() {
    return this.mc.getUseConfigs();
  }
  
  public boolean getUseCursorFetch() {
    return this.mc.getUseCursorFetch();
  }
  
  public boolean getUseDirectRowUnpack() {
    return this.mc.getUseDirectRowUnpack();
  }
  
  public boolean getUseDynamicCharsetInfo() {
    return this.mc.getUseDynamicCharsetInfo();
  }
  
  public boolean getUseFastDateParsing() {
    return this.mc.getUseFastDateParsing();
  }
  
  public boolean getUseFastIntParsing() {
    return this.mc.getUseFastIntParsing();
  }
  
  public boolean getUseGmtMillisForDatetimes() {
    return this.mc.getUseGmtMillisForDatetimes();
  }
  
  public boolean getUseHostsInPrivileges() {
    return this.mc.getUseHostsInPrivileges();
  }
  
  public boolean getUseInformationSchema() {
    return this.mc.getUseInformationSchema();
  }
  
  public boolean getUseJDBCCompliantTimezoneShift() {
    return this.mc.getUseJDBCCompliantTimezoneShift();
  }
  
  public boolean getUseJvmCharsetConverters() {
    return this.mc.getUseJvmCharsetConverters();
  }
  
  public boolean getUseLegacyDatetimeCode() {
    return this.mc.getUseLegacyDatetimeCode();
  }
  
  public boolean getUseLocalSessionState() {
    return this.mc.getUseLocalSessionState();
  }
  
  public boolean getUseLocalTransactionState() {
    return this.mc.getUseLocalTransactionState();
  }
  
  public boolean getUseNanosForElapsedTime() {
    return this.mc.getUseNanosForElapsedTime();
  }
  
  public boolean getUseOldAliasMetadataBehavior() {
    return this.mc.getUseOldAliasMetadataBehavior();
  }
  
  public boolean getUseOldUTF8Behavior() {
    return this.mc.getUseOldUTF8Behavior();
  }
  
  public boolean getUseOnlyServerErrorMessages() {
    return this.mc.getUseOnlyServerErrorMessages();
  }
  
  public boolean getUseReadAheadInput() {
    return this.mc.getUseReadAheadInput();
  }
  
  public boolean getUseSSL() {
    return this.mc.getUseSSL();
  }
  
  public boolean getUseSSPSCompatibleTimezoneShift() {
    return this.mc.getUseSSPSCompatibleTimezoneShift();
  }
  
  public boolean getUseServerPrepStmts() {
    return this.mc.getUseServerPrepStmts();
  }
  
  public boolean getUseServerPreparedStmts() {
    return this.mc.getUseServerPreparedStmts();
  }
  
  public boolean getUseSqlStateCodes() {
    return this.mc.getUseSqlStateCodes();
  }
  
  public boolean getUseStreamLengthsInPrepStmts() {
    return this.mc.getUseStreamLengthsInPrepStmts();
  }
  
  public boolean getUseTimezone() {
    return this.mc.getUseTimezone();
  }
  
  public boolean getUseUltraDevWorkAround() {
    return this.mc.getUseUltraDevWorkAround();
  }
  
  public boolean getUseUnbufferedInput() {
    return this.mc.getUseUnbufferedInput();
  }
  
  public boolean getUseUnicode() {
    return this.mc.getUseUnicode();
  }
  
  public boolean getUseUsageAdvisor() {
    return this.mc.getUseUsageAdvisor();
  }
  
  public String getUtf8OutsideBmpExcludedColumnNamePattern() {
    return this.mc.getUtf8OutsideBmpExcludedColumnNamePattern();
  }
  
  public String getUtf8OutsideBmpIncludedColumnNamePattern() {
    return this.mc.getUtf8OutsideBmpIncludedColumnNamePattern();
  }
  
  public boolean getVerifyServerCertificate() {
    return this.mc.getVerifyServerCertificate();
  }
  
  public SQLWarning getWarnings() throws SQLException {
    checkClosed();
    try {
      return this.mc.getWarnings();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public boolean getYearIsDateType() {
    return this.mc.getYearIsDateType();
  }
  
  public String getZeroDateTimeBehavior() {
    return this.mc.getZeroDateTimeBehavior();
  }
  
  public boolean hasSameProperties(Connection paramConnection) {
    return this.mc.hasSameProperties(paramConnection);
  }
  
  @Deprecated
  public boolean hasTriedMaster() {
    return this.mc.hasTriedMaster();
  }
  
  public void initializeExtension(Extension paramExtension) throws SQLException {
    this.mc.initializeExtension(paramExtension);
  }
  
  public boolean isAbonormallyLongQuery(long paramLong) {
    return this.mc.isAbonormallyLongQuery(paramLong);
  }
  
  public boolean isClosed() throws SQLException {
    return (this.closed || this.mc.isClosed());
  }
  
  public boolean isInGlobalTx() {
    return this.mc.isInGlobalTx();
  }
  
  public boolean isMasterConnection() {
    return this.mc.isMasterConnection();
  }
  
  public boolean isNoBackslashEscapesSet() {
    return this.mc.isNoBackslashEscapesSet();
  }
  
  public boolean isReadOnly() throws SQLException {
    checkClosed();
    try {
      return this.mc.isReadOnly();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean isSameResource(Connection paramConnection) {
    return (paramConnection instanceof ConnectionWrapper) ? this.mc.isSameResource(((ConnectionWrapper)paramConnection).mc) : this.mc.isSameResource(paramConnection);
  }
  
  public boolean isServerLocal() throws SQLException {
    return this.mc.isServerLocal();
  }
  
  public boolean isUseSSLExplicit() {
    return this.mc.isUseSSLExplicit();
  }
  
  public boolean lowerCaseTableNames() {
    return this.mc.lowerCaseTableNames();
  }
  
  public String nativeSQL(String paramString) throws SQLException {
    checkClosed();
    try {
      return this.mc.nativeSQL(paramString);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public boolean parserKnowsUnicode() {
    return this.mc.parserKnowsUnicode();
  }
  
  public void ping() throws SQLException {
    Connection connection = this.mc;
    if (connection != null)
      connection.ping(); 
  }
  
  public CallableStatement prepareCall(String paramString) throws SQLException {
    checkClosed();
    try {
      return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(paramString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    try {
      return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(paramString, paramInt1, paramInt2));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    checkClosed();
    try {
      return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(paramString, paramInt1, paramInt2, paramInt3));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement prepareStatement(String paramString) throws SQLException {
    checkClosed();
    try {
      PreparedStatementWrapper preparedStatementWrapper = PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(paramString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      sQLException = null;
    } 
    return (PreparedStatement)sQLException;
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(paramString, paramInt));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(paramString, paramInt1, paramInt2));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(paramString, paramInt1, paramInt2, paramInt3));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(paramString, paramArrayOfint));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(paramString, paramArrayOfString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
    checkClosed();
    try {
      this.mc.releaseSavepoint(paramSavepoint);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void reportQueryTime(long paramLong) {
    this.mc.reportQueryTime(paramLong);
  }
  
  public void resetServerState() throws SQLException {
    checkClosed();
    try {
      this.mc.resetServerState();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void rollback() throws SQLException {
    checkClosed();
    if (!isInGlobalTx()) {
      try {
        this.mc.rollback();
      } catch (SQLException sQLException) {
        checkAndFireConnectionError(sQLException);
      } 
      return;
    } 
    throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", "2D000", 1401, this.exceptionInterceptor);
  }
  
  public void rollback(Savepoint paramSavepoint) throws SQLException {
    checkClosed();
    if (!isInGlobalTx()) {
      try {
        this.mc.rollback(paramSavepoint);
      } catch (SQLException sQLException) {
        checkAndFireConnectionError(sQLException);
      } 
      return;
    } 
    throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", "2D000", 1401, this.exceptionInterceptor);
  }
  
  public PreparedStatement serverPrepareStatement(String paramString) throws SQLException {
    checkClosed();
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(paramString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(paramString, paramInt));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(paramString, paramInt1, paramInt2));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(paramString, paramInt1, paramInt2, paramInt3));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(paramString, paramArrayOfint));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public PreparedStatement serverPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
    try {
      return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(paramString, paramArrayOfString));
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public void setAllowLoadLocalInfile(boolean paramBoolean) {
    this.mc.setAllowLoadLocalInfile(paramBoolean);
  }
  
  public void setAllowMasterDownConnections(boolean paramBoolean) {
    this.mc.setAllowMasterDownConnections(paramBoolean);
  }
  
  public void setAllowMultiQueries(boolean paramBoolean) {
    this.mc.setAllowMultiQueries(paramBoolean);
  }
  
  public void setAllowNanAndInf(boolean paramBoolean) {
    this.mc.setAllowNanAndInf(paramBoolean);
  }
  
  public void setAllowPublicKeyRetrieval(boolean paramBoolean) throws SQLException {
    this.mc.setAllowPublicKeyRetrieval(paramBoolean);
  }
  
  public void setAllowSlaveDownConnections(boolean paramBoolean) {
    this.mc.setAllowSlaveDownConnections(paramBoolean);
  }
  
  public void setAllowUrlInLocalInfile(boolean paramBoolean) {
    this.mc.setAllowUrlInLocalInfile(paramBoolean);
  }
  
  public void setAlwaysSendSetIsolation(boolean paramBoolean) {
    this.mc.setAlwaysSendSetIsolation(paramBoolean);
  }
  
  public void setAuthenticationPlugins(String paramString) {
    this.mc.setAuthenticationPlugins(paramString);
  }
  
  public void setAutoClosePStmtStreams(boolean paramBoolean) {
    this.mc.setAutoClosePStmtStreams(paramBoolean);
  }
  
  public void setAutoCommit(boolean paramBoolean) throws SQLException {
    checkClosed();
    if (!paramBoolean || !isInGlobalTx()) {
      try {
        this.mc.setAutoCommit(paramBoolean);
      } catch (SQLException sQLException) {
        checkAndFireConnectionError(sQLException);
      } 
      return;
    } 
    throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401, this.exceptionInterceptor);
  }
  
  public void setAutoDeserialize(boolean paramBoolean) {
    this.mc.setAutoDeserialize(paramBoolean);
  }
  
  public void setAutoGenerateTestcaseScript(boolean paramBoolean) {
    this.mc.setAutoGenerateTestcaseScript(paramBoolean);
  }
  
  public void setAutoReconnect(boolean paramBoolean) {
    this.mc.setAutoReconnect(paramBoolean);
  }
  
  public void setAutoReconnectForConnectionPools(boolean paramBoolean) {
    this.mc.setAutoReconnectForConnectionPools(paramBoolean);
  }
  
  public void setAutoReconnectForPools(boolean paramBoolean) {
    this.mc.setAutoReconnectForPools(paramBoolean);
  }
  
  public void setAutoSlowLog(boolean paramBoolean) {
    this.mc.setAutoSlowLog(paramBoolean);
  }
  
  public void setBlobSendChunkSize(String paramString) throws SQLException {
    this.mc.setBlobSendChunkSize(paramString);
  }
  
  public void setBlobsAreStrings(boolean paramBoolean) {
    this.mc.setBlobsAreStrings(paramBoolean);
  }
  
  public void setCacheCallableStatements(boolean paramBoolean) {
    this.mc.setCacheCallableStatements(paramBoolean);
  }
  
  public void setCacheCallableStmts(boolean paramBoolean) {
    this.mc.setCacheCallableStmts(paramBoolean);
  }
  
  public void setCacheDefaultTimezone(boolean paramBoolean) {
    this.mc.setCacheDefaultTimezone(paramBoolean);
  }
  
  public void setCachePrepStmts(boolean paramBoolean) {
    this.mc.setCachePrepStmts(paramBoolean);
  }
  
  public void setCachePreparedStatements(boolean paramBoolean) {
    this.mc.setCachePreparedStatements(paramBoolean);
  }
  
  public void setCacheResultSetMetadata(boolean paramBoolean) {
    this.mc.setCacheResultSetMetadata(paramBoolean);
  }
  
  public void setCacheServerConfiguration(boolean paramBoolean) {
    this.mc.setCacheServerConfiguration(paramBoolean);
  }
  
  public void setCallableStatementCacheSize(int paramInt) throws SQLException {
    this.mc.setCallableStatementCacheSize(paramInt);
  }
  
  public void setCallableStmtCacheSize(int paramInt) throws SQLException {
    this.mc.setCallableStmtCacheSize(paramInt);
  }
  
  public void setCapitalizeDBMDTypes(boolean paramBoolean) {
    this.mc.setCapitalizeDBMDTypes(paramBoolean);
  }
  
  public void setCapitalizeTypeNames(boolean paramBoolean) {
    this.mc.setCapitalizeTypeNames(paramBoolean);
  }
  
  public void setCatalog(String paramString) throws SQLException {
    checkClosed();
    try {
      this.mc.setCatalog(paramString);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterEncoding(String paramString) {
    this.mc.setCharacterEncoding(paramString);
  }
  
  public void setCharacterSetResults(String paramString) {
    this.mc.setCharacterSetResults(paramString);
  }
  
  public void setClientCertificateKeyStorePassword(String paramString) {
    this.mc.setClientCertificateKeyStorePassword(paramString);
  }
  
  public void setClientCertificateKeyStoreType(String paramString) {
    this.mc.setClientCertificateKeyStoreType(paramString);
  }
  
  public void setClientCertificateKeyStoreUrl(String paramString) {
    this.mc.setClientCertificateKeyStoreUrl(paramString);
  }
  
  public void setClientInfoProvider(String paramString) {
    this.mc.setClientInfoProvider(paramString);
  }
  
  public void setClobCharacterEncoding(String paramString) {
    this.mc.setClobCharacterEncoding(paramString);
  }
  
  public void setClobberStreamingResults(boolean paramBoolean) {
    this.mc.setClobberStreamingResults(paramBoolean);
  }
  
  public void setCompensateOnDuplicateKeyUpdateCounts(boolean paramBoolean) {
    this.mc.setCompensateOnDuplicateKeyUpdateCounts(paramBoolean);
  }
  
  public void setConnectTimeout(int paramInt) throws SQLException {
    this.mc.setConnectTimeout(paramInt);
  }
  
  public void setConnectionCollation(String paramString) {
    this.mc.setConnectionCollation(paramString);
  }
  
  public void setConnectionLifecycleInterceptors(String paramString) {
    this.mc.setConnectionLifecycleInterceptors(paramString);
  }
  
  public void setContinueBatchOnError(boolean paramBoolean) {
    this.mc.setContinueBatchOnError(paramBoolean);
  }
  
  public void setCreateDatabaseIfNotExist(boolean paramBoolean) {
    this.mc.setCreateDatabaseIfNotExist(paramBoolean);
  }
  
  public void setDefaultAuthenticationPlugin(String paramString) {
    this.mc.setDefaultAuthenticationPlugin(paramString);
  }
  
  public void setDefaultFetchSize(int paramInt) throws SQLException {
    this.mc.setDefaultFetchSize(paramInt);
  }
  
  public void setDetectCustomCollations(boolean paramBoolean) {
    this.mc.setDetectCustomCollations(paramBoolean);
  }
  
  public void setDetectServerPreparedStmts(boolean paramBoolean) {
    this.mc.setDetectServerPreparedStmts(paramBoolean);
  }
  
  public void setDisabledAuthenticationPlugins(String paramString) {
    this.mc.setDisabledAuthenticationPlugins(paramString);
  }
  
  public void setDisconnectOnExpiredPasswords(boolean paramBoolean) {
    this.mc.setDisconnectOnExpiredPasswords(paramBoolean);
  }
  
  public void setDontCheckOnDuplicateKeyUpdateInSQL(boolean paramBoolean) {
    this.mc.setDontCheckOnDuplicateKeyUpdateInSQL(paramBoolean);
  }
  
  public void setDontTrackOpenResources(boolean paramBoolean) {
    this.mc.setDontTrackOpenResources(paramBoolean);
  }
  
  public void setDumpMetadataOnColumnNotFound(boolean paramBoolean) {
    this.mc.setDumpMetadataOnColumnNotFound(paramBoolean);
  }
  
  public void setDumpQueriesOnException(boolean paramBoolean) {
    this.mc.setDumpQueriesOnException(paramBoolean);
  }
  
  public void setDynamicCalendars(boolean paramBoolean) {
    this.mc.setDynamicCalendars(paramBoolean);
  }
  
  public void setElideSetAutoCommits(boolean paramBoolean) {
    this.mc.setElideSetAutoCommits(paramBoolean);
  }
  
  public void setEmptyStringsConvertToZero(boolean paramBoolean) {
    this.mc.setEmptyStringsConvertToZero(paramBoolean);
  }
  
  public void setEmulateLocators(boolean paramBoolean) {
    this.mc.setEmulateLocators(paramBoolean);
  }
  
  public void setEmulateUnsupportedPstmts(boolean paramBoolean) {
    this.mc.setEmulateUnsupportedPstmts(paramBoolean);
  }
  
  public void setEnableEscapeProcessing(boolean paramBoolean) {
    this.mc.setEnableEscapeProcessing(paramBoolean);
  }
  
  public void setEnablePacketDebug(boolean paramBoolean) {
    this.mc.setEnablePacketDebug(paramBoolean);
  }
  
  public void setEnableQueryTimeouts(boolean paramBoolean) {
    this.mc.setEnableQueryTimeouts(paramBoolean);
  }
  
  public void setEnabledSSLCipherSuites(String paramString) {
    this.mc.setEnabledSSLCipherSuites(paramString);
  }
  
  public void setEnabledTLSProtocols(String paramString) {
    this.mc.setEnabledTLSProtocols(paramString);
  }
  
  public void setEncoding(String paramString) {
    this.mc.setEncoding(paramString);
  }
  
  public void setExceptionInterceptors(String paramString) {
    this.mc.setExceptionInterceptors(paramString);
  }
  
  public void setExplainSlowQueries(boolean paramBoolean) {
    this.mc.setExplainSlowQueries(paramBoolean);
  }
  
  public void setFailOverReadOnly(boolean paramBoolean) {
    this.mc.setFailOverReadOnly(paramBoolean);
  }
  
  public void setFailedOver(boolean paramBoolean) {
    this.mc.setFailedOver(paramBoolean);
  }
  
  public void setFunctionsNeverReturnBlobs(boolean paramBoolean) {
    this.mc.setFunctionsNeverReturnBlobs(paramBoolean);
  }
  
  public void setGatherPerfMetrics(boolean paramBoolean) {
    this.mc.setGatherPerfMetrics(paramBoolean);
  }
  
  public void setGatherPerformanceMetrics(boolean paramBoolean) {
    this.mc.setGatherPerformanceMetrics(paramBoolean);
  }
  
  public void setGenerateSimpleParameterMetadata(boolean paramBoolean) {
    this.mc.setGenerateSimpleParameterMetadata(paramBoolean);
  }
  
  public void setGetProceduresReturnsFunctions(boolean paramBoolean) {
    this.mc.setGetProceduresReturnsFunctions(paramBoolean);
  }
  
  public void setHoldResultsOpenOverStatementClose(boolean paramBoolean) {
    this.mc.setHoldResultsOpenOverStatementClose(paramBoolean);
  }
  
  public void setHoldability(int paramInt) throws SQLException {
    checkClosed();
    try {
      this.mc.setHoldability(paramInt);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setIgnoreNonTxTables(boolean paramBoolean) {
    this.mc.setIgnoreNonTxTables(paramBoolean);
  }
  
  public void setInGlobalTx(boolean paramBoolean) {
    this.mc.setInGlobalTx(paramBoolean);
  }
  
  public void setIncludeInnodbStatusInDeadlockExceptions(boolean paramBoolean) {
    this.mc.setIncludeInnodbStatusInDeadlockExceptions(paramBoolean);
  }
  
  public void setIncludeThreadDumpInDeadlockExceptions(boolean paramBoolean) {
    this.mc.setIncludeThreadDumpInDeadlockExceptions(paramBoolean);
  }
  
  public void setIncludeThreadNamesAsStatementComment(boolean paramBoolean) {
    this.mc.setIncludeThreadNamesAsStatementComment(paramBoolean);
  }
  
  public void setInitialTimeout(int paramInt) throws SQLException {
    this.mc.setInitialTimeout(paramInt);
  }
  
  public void setInteractiveClient(boolean paramBoolean) {
    this.mc.setInteractiveClient(paramBoolean);
  }
  
  public void setIsInteractiveClient(boolean paramBoolean) {
    this.mc.setIsInteractiveClient(paramBoolean);
  }
  
  public void setJdbcCompliantTruncation(boolean paramBoolean) {
    this.mc.setJdbcCompliantTruncation(paramBoolean);
  }
  
  public void setJdbcCompliantTruncationForReads(boolean paramBoolean) {
    this.mc.setJdbcCompliantTruncationForReads(paramBoolean);
  }
  
  public void setLargeRowSizeThreshold(String paramString) throws SQLException {
    this.mc.setLargeRowSizeThreshold(paramString);
  }
  
  public void setLoadBalanceAutoCommitStatementRegex(String paramString) {
    this.mc.setLoadBalanceAutoCommitStatementRegex(paramString);
  }
  
  public void setLoadBalanceAutoCommitStatementThreshold(int paramInt) throws SQLException {
    this.mc.setLoadBalanceAutoCommitStatementThreshold(paramInt);
  }
  
  public void setLoadBalanceBlacklistTimeout(int paramInt) throws SQLException {
    this.mc.setLoadBalanceBlacklistTimeout(paramInt);
  }
  
  public void setLoadBalanceConnectionGroup(String paramString) {
    this.mc.setLoadBalanceConnectionGroup(paramString);
  }
  
  public void setLoadBalanceEnableJMX(boolean paramBoolean) {
    this.mc.setLoadBalanceEnableJMX(paramBoolean);
  }
  
  public void setLoadBalanceExceptionChecker(String paramString) {
    this.mc.setLoadBalanceExceptionChecker(paramString);
  }
  
  public void setLoadBalanceHostRemovalGracePeriod(int paramInt) throws SQLException {
    this.mc.setLoadBalanceHostRemovalGracePeriod(paramInt);
  }
  
  public void setLoadBalancePingTimeout(int paramInt) throws SQLException {
    this.mc.setLoadBalancePingTimeout(paramInt);
  }
  
  public void setLoadBalanceSQLExceptionSubclassFailover(String paramString) {
    this.mc.setLoadBalanceSQLExceptionSubclassFailover(paramString);
  }
  
  public void setLoadBalanceSQLStateFailover(String paramString) {
    this.mc.setLoadBalanceSQLStateFailover(paramString);
  }
  
  public void setLoadBalanceStrategy(String paramString) {
    this.mc.setLoadBalanceStrategy(paramString);
  }
  
  public void setLoadBalanceValidateConnectionOnSwapServer(boolean paramBoolean) {
    this.mc.setLoadBalanceValidateConnectionOnSwapServer(paramBoolean);
  }
  
  public void setLocalSocketAddress(String paramString) {
    this.mc.setLocalSocketAddress(paramString);
  }
  
  public void setLocatorFetchBufferSize(String paramString) throws SQLException {
    this.mc.setLocatorFetchBufferSize(paramString);
  }
  
  public void setLogSlowQueries(boolean paramBoolean) {
    this.mc.setLogSlowQueries(paramBoolean);
  }
  
  public void setLogXaCommands(boolean paramBoolean) {
    this.mc.setLogXaCommands(paramBoolean);
  }
  
  public void setLogger(String paramString) {
    this.mc.setLogger(paramString);
  }
  
  public void setLoggerClassName(String paramString) {
    this.mc.setLoggerClassName(paramString);
  }
  
  public void setMaintainTimeStats(boolean paramBoolean) {
    this.mc.setMaintainTimeStats(paramBoolean);
  }
  
  public void setMaxQuerySizeToLog(int paramInt) throws SQLException {
    this.mc.setMaxQuerySizeToLog(paramInt);
  }
  
  public void setMaxReconnects(int paramInt) throws SQLException {
    this.mc.setMaxReconnects(paramInt);
  }
  
  public void setMaxRows(int paramInt) throws SQLException {
    this.mc.setMaxRows(paramInt);
  }
  
  public void setMetadataCacheSize(int paramInt) throws SQLException {
    this.mc.setMetadataCacheSize(paramInt);
  }
  
  public void setNetTimeoutForStreamingResults(int paramInt) throws SQLException {
    this.mc.setNetTimeoutForStreamingResults(paramInt);
  }
  
  public void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException {
    this.mc.setNetworkTimeout(paramExecutor, paramInt);
  }
  
  public void setNoAccessToProcedureBodies(boolean paramBoolean) {
    this.mc.setNoAccessToProcedureBodies(paramBoolean);
  }
  
  public void setNoDatetimeStringSync(boolean paramBoolean) {
    this.mc.setNoDatetimeStringSync(paramBoolean);
  }
  
  public void setNoTimezoneConversionForDateType(boolean paramBoolean) {
    this.mc.setNoTimezoneConversionForDateType(paramBoolean);
  }
  
  public void setNoTimezoneConversionForTimeType(boolean paramBoolean) {
    this.mc.setNoTimezoneConversionForTimeType(paramBoolean);
  }
  
  public void setNullCatalogMeansCurrent(boolean paramBoolean) {
    this.mc.setNullCatalogMeansCurrent(paramBoolean);
  }
  
  public void setNullNamePatternMatchesAll(boolean paramBoolean) {
    this.mc.setNullNamePatternMatchesAll(paramBoolean);
  }
  
  public void setOverrideSupportsIntegrityEnhancementFacility(boolean paramBoolean) {
    this.mc.setOverrideSupportsIntegrityEnhancementFacility(paramBoolean);
  }
  
  public void setPacketDebugBufferSize(int paramInt) throws SQLException {
    this.mc.setPacketDebugBufferSize(paramInt);
  }
  
  public void setPadCharsWithSpace(boolean paramBoolean) {
    this.mc.setPadCharsWithSpace(paramBoolean);
  }
  
  public void setParanoid(boolean paramBoolean) {
    this.mc.setParanoid(paramBoolean);
  }
  
  public void setParseInfoCacheFactory(String paramString) {
    this.mc.setParseInfoCacheFactory(paramString);
  }
  
  public void setPasswordCharacterEncoding(String paramString) {
    this.mc.setPasswordCharacterEncoding(paramString);
  }
  
  public void setPedantic(boolean paramBoolean) {
    this.mc.setPedantic(paramBoolean);
  }
  
  public void setPinGlobalTxToPhysicalConnection(boolean paramBoolean) {
    this.mc.setPinGlobalTxToPhysicalConnection(paramBoolean);
  }
  
  public void setPopulateInsertRowWithDefaultValues(boolean paramBoolean) {
    this.mc.setPopulateInsertRowWithDefaultValues(paramBoolean);
  }
  
  @Deprecated
  public void setPreferSlaveDuringFailover(boolean paramBoolean) {
    this.mc.setPreferSlaveDuringFailover(paramBoolean);
  }
  
  public void setPrepStmtCacheSize(int paramInt) throws SQLException {
    this.mc.setPrepStmtCacheSize(paramInt);
  }
  
  public void setPrepStmtCacheSqlLimit(int paramInt) throws SQLException {
    this.mc.setPrepStmtCacheSqlLimit(paramInt);
  }
  
  public void setPreparedStatementCacheSize(int paramInt) throws SQLException {
    this.mc.setPreparedStatementCacheSize(paramInt);
  }
  
  public void setPreparedStatementCacheSqlLimit(int paramInt) throws SQLException {
    this.mc.setPreparedStatementCacheSqlLimit(paramInt);
  }
  
  public void setProcessEscapeCodesForPrepStmts(boolean paramBoolean) {
    this.mc.setProcessEscapeCodesForPrepStmts(paramBoolean);
  }
  
  public void setProfileSQL(boolean paramBoolean) {
    this.mc.setProfileSQL(paramBoolean);
  }
  
  public void setProfileSql(boolean paramBoolean) {
    this.mc.setProfileSql(paramBoolean);
  }
  
  public void setProfilerEventHandler(String paramString) {
    this.mc.setProfilerEventHandler(paramString);
  }
  
  public void setPropertiesTransform(String paramString) {
    this.mc.setPropertiesTransform(paramString);
  }
  
  public void setProxy(MySQLConnection paramMySQLConnection) {
    this.mc.setProxy(paramMySQLConnection);
  }
  
  public void setQueriesBeforeRetryMaster(int paramInt) throws SQLException {
    this.mc.setQueriesBeforeRetryMaster(paramInt);
  }
  
  public void setQueryTimeoutKillsConnection(boolean paramBoolean) {
    this.mc.setQueryTimeoutKillsConnection(paramBoolean);
  }
  
  public void setReadFromMasterWhenNoSlaves(boolean paramBoolean) {
    this.mc.setReadFromMasterWhenNoSlaves(paramBoolean);
  }
  
  public void setReadOnly(boolean paramBoolean) throws SQLException {
    checkClosed();
    try {
      this.mc.setReadOnly(paramBoolean);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setReadOnlyPropagatesToServer(boolean paramBoolean) {
    this.mc.setReadOnlyPropagatesToServer(paramBoolean);
  }
  
  public void setReconnectAtTxEnd(boolean paramBoolean) {
    this.mc.setReconnectAtTxEnd(paramBoolean);
  }
  
  public void setRelaxAutoCommit(boolean paramBoolean) {
    this.mc.setRelaxAutoCommit(paramBoolean);
  }
  
  public void setReplicationEnableJMX(boolean paramBoolean) {
    this.mc.setReplicationEnableJMX(paramBoolean);
  }
  
  public void setReportMetricsIntervalMillis(int paramInt) throws SQLException {
    this.mc.setReportMetricsIntervalMillis(paramInt);
  }
  
  public void setRequireSSL(boolean paramBoolean) {
    this.mc.setRequireSSL(paramBoolean);
  }
  
  public void setResourceId(String paramString) {
    this.mc.setResourceId(paramString);
  }
  
  public void setResultSetSizeThreshold(int paramInt) throws SQLException {
    this.mc.setResultSetSizeThreshold(paramInt);
  }
  
  public void setRetainStatementAfterResultSetClose(boolean paramBoolean) {
    this.mc.setRetainStatementAfterResultSetClose(paramBoolean);
  }
  
  public void setRetriesAllDown(int paramInt) throws SQLException {
    this.mc.setRetriesAllDown(paramInt);
  }
  
  public void setRewriteBatchedStatements(boolean paramBoolean) {
    this.mc.setRewriteBatchedStatements(paramBoolean);
  }
  
  public void setRollbackOnPooledClose(boolean paramBoolean) {
    this.mc.setRollbackOnPooledClose(paramBoolean);
  }
  
  public void setRoundRobinLoadBalance(boolean paramBoolean) {
    this.mc.setRoundRobinLoadBalance(paramBoolean);
  }
  
  public void setRunningCTS13(boolean paramBoolean) {
    this.mc.setRunningCTS13(paramBoolean);
  }
  
  public Savepoint setSavepoint() throws SQLException {
    checkClosed();
    if (!isInGlobalTx())
      try {
        return this.mc.setSavepoint();
      } catch (SQLException sQLException) {
        checkAndFireConnectionError(sQLException);
        return null;
      }  
    throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401, this.exceptionInterceptor);
  }
  
  public Savepoint setSavepoint(String paramString) throws SQLException {
    checkClosed();
    if (!isInGlobalTx())
      try {
        return this.mc.setSavepoint(paramString);
      } catch (SQLException sQLException) {
        checkAndFireConnectionError(sQLException);
        return null;
      }  
    throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401, this.exceptionInterceptor);
  }
  
  public void setSchema(String paramString) throws SQLException {
    this.mc.setSchema(paramString);
  }
  
  public void setSecondsBeforeRetryMaster(int paramInt) throws SQLException {
    this.mc.setSecondsBeforeRetryMaster(paramInt);
  }
  
  public void setSelfDestructOnPingMaxOperations(int paramInt) throws SQLException {
    this.mc.setSelfDestructOnPingMaxOperations(paramInt);
  }
  
  public void setSelfDestructOnPingSecondsLifetime(int paramInt) throws SQLException {
    this.mc.setSelfDestructOnPingSecondsLifetime(paramInt);
  }
  
  public void setSendFractionalSeconds(boolean paramBoolean) {
    this.mc.setSendFractionalSeconds(paramBoolean);
  }
  
  public void setServerAffinityOrder(String paramString) {
    this.mc.setServerAffinityOrder(paramString);
  }
  
  public void setServerConfigCacheFactory(String paramString) {
    this.mc.setServerConfigCacheFactory(paramString);
  }
  
  public void setServerRSAPublicKeyFile(String paramString) throws SQLException {
    this.mc.setServerRSAPublicKeyFile(paramString);
  }
  
  public void setServerTimezone(String paramString) {
    this.mc.setServerTimezone(paramString);
  }
  
  public void setSessionMaxRows(int paramInt) throws SQLException {
    this.mc.setSessionMaxRows(paramInt);
  }
  
  public void setSessionVariables(String paramString) {
    this.mc.setSessionVariables(paramString);
  }
  
  public void setSlowQueryThresholdMillis(int paramInt) throws SQLException {
    this.mc.setSlowQueryThresholdMillis(paramInt);
  }
  
  public void setSlowQueryThresholdNanos(long paramLong) throws SQLException {
    this.mc.setSlowQueryThresholdNanos(paramLong);
  }
  
  public void setSocketFactory(String paramString) {
    this.mc.setSocketFactory(paramString);
  }
  
  public void setSocketFactoryClassName(String paramString) {
    this.mc.setSocketFactoryClassName(paramString);
  }
  
  public void setSocketTimeout(int paramInt) throws SQLException {
    this.mc.setSocketTimeout(paramInt);
  }
  
  public void setSocksProxyHost(String paramString) {
    this.mc.setSocksProxyHost(paramString);
  }
  
  public void setSocksProxyPort(int paramInt) throws SQLException {
    this.mc.setSocksProxyPort(paramInt);
  }
  
  public void setStatementComment(String paramString) {
    this.mc.setStatementComment(paramString);
  }
  
  public void setStatementInterceptors(String paramString) {
    this.mc.setStatementInterceptors(paramString);
  }
  
  public void setStrictFloatingPoint(boolean paramBoolean) {
    this.mc.setStrictFloatingPoint(paramBoolean);
  }
  
  public void setStrictUpdates(boolean paramBoolean) {
    this.mc.setStrictUpdates(paramBoolean);
  }
  
  public void setTcpKeepAlive(boolean paramBoolean) {
    this.mc.setTcpKeepAlive(paramBoolean);
  }
  
  public void setTcpNoDelay(boolean paramBoolean) {
    this.mc.setTcpNoDelay(paramBoolean);
  }
  
  public void setTcpRcvBuf(int paramInt) throws SQLException {
    this.mc.setTcpRcvBuf(paramInt);
  }
  
  public void setTcpSndBuf(int paramInt) throws SQLException {
    this.mc.setTcpSndBuf(paramInt);
  }
  
  public void setTcpTrafficClass(int paramInt) throws SQLException {
    this.mc.setTcpTrafficClass(paramInt);
  }
  
  public void setTinyInt1isBit(boolean paramBoolean) {
    this.mc.setTinyInt1isBit(paramBoolean);
  }
  
  public void setTraceProtocol(boolean paramBoolean) {
    this.mc.setTraceProtocol(paramBoolean);
  }
  
  public void setTransactionIsolation(int paramInt) throws SQLException {
    checkClosed();
    try {
      this.mc.setTransactionIsolation(paramInt);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTransformedBitIsBoolean(boolean paramBoolean) {
    this.mc.setTransformedBitIsBoolean(paramBoolean);
  }
  
  public void setTreatUtilDateAsTimestamp(boolean paramBoolean) {
    this.mc.setTreatUtilDateAsTimestamp(paramBoolean);
  }
  
  public void setTrustCertificateKeyStorePassword(String paramString) {
    this.mc.setTrustCertificateKeyStorePassword(paramString);
  }
  
  public void setTrustCertificateKeyStoreType(String paramString) {
    this.mc.setTrustCertificateKeyStoreType(paramString);
  }
  
  public void setTrustCertificateKeyStoreUrl(String paramString) {
    this.mc.setTrustCertificateKeyStoreUrl(paramString);
  }
  
  public void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
    checkClosed();
    try {
      this.mc.setTypeMap(paramMap);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setUltraDevHack(boolean paramBoolean) {
    this.mc.setUltraDevHack(paramBoolean);
  }
  
  public void setUseAffectedRows(boolean paramBoolean) {
    this.mc.setUseAffectedRows(paramBoolean);
  }
  
  public void setUseBlobToStoreUTF8OutsideBMP(boolean paramBoolean) {
    this.mc.setUseBlobToStoreUTF8OutsideBMP(paramBoolean);
  }
  
  public void setUseColumnNamesInFindColumn(boolean paramBoolean) {
    this.mc.setUseColumnNamesInFindColumn(paramBoolean);
  }
  
  public void setUseCompression(boolean paramBoolean) {
    this.mc.setUseCompression(paramBoolean);
  }
  
  public void setUseConfigs(String paramString) {
    this.mc.setUseConfigs(paramString);
  }
  
  public void setUseCursorFetch(boolean paramBoolean) {
    this.mc.setUseCursorFetch(paramBoolean);
  }
  
  public void setUseDirectRowUnpack(boolean paramBoolean) {
    this.mc.setUseDirectRowUnpack(paramBoolean);
  }
  
  public void setUseDynamicCharsetInfo(boolean paramBoolean) {
    this.mc.setUseDynamicCharsetInfo(paramBoolean);
  }
  
  public void setUseFastDateParsing(boolean paramBoolean) {
    this.mc.setUseFastDateParsing(paramBoolean);
  }
  
  public void setUseFastIntParsing(boolean paramBoolean) {
    this.mc.setUseFastIntParsing(paramBoolean);
  }
  
  public void setUseGmtMillisForDatetimes(boolean paramBoolean) {
    this.mc.setUseGmtMillisForDatetimes(paramBoolean);
  }
  
  public void setUseHostsInPrivileges(boolean paramBoolean) {
    this.mc.setUseHostsInPrivileges(paramBoolean);
  }
  
  public void setUseInformationSchema(boolean paramBoolean) {
    this.mc.setUseInformationSchema(paramBoolean);
  }
  
  public void setUseJDBCCompliantTimezoneShift(boolean paramBoolean) {
    this.mc.setUseJDBCCompliantTimezoneShift(paramBoolean);
  }
  
  public void setUseJvmCharsetConverters(boolean paramBoolean) {
    this.mc.setUseJvmCharsetConverters(paramBoolean);
  }
  
  public void setUseLegacyDatetimeCode(boolean paramBoolean) {
    this.mc.setUseLegacyDatetimeCode(paramBoolean);
  }
  
  public void setUseLocalSessionState(boolean paramBoolean) {
    this.mc.setUseLocalSessionState(paramBoolean);
  }
  
  public void setUseLocalTransactionState(boolean paramBoolean) {
    this.mc.setUseLocalTransactionState(paramBoolean);
  }
  
  public void setUseNanosForElapsedTime(boolean paramBoolean) {
    this.mc.setUseNanosForElapsedTime(paramBoolean);
  }
  
  public void setUseOldAliasMetadataBehavior(boolean paramBoolean) {
    this.mc.setUseOldAliasMetadataBehavior(paramBoolean);
  }
  
  public void setUseOldUTF8Behavior(boolean paramBoolean) {
    this.mc.setUseOldUTF8Behavior(paramBoolean);
  }
  
  public void setUseOnlyServerErrorMessages(boolean paramBoolean) {
    this.mc.setUseOnlyServerErrorMessages(paramBoolean);
  }
  
  public void setUseReadAheadInput(boolean paramBoolean) {
    this.mc.setUseReadAheadInput(paramBoolean);
  }
  
  public void setUseSSL(boolean paramBoolean) {
    this.mc.setUseSSL(paramBoolean);
  }
  
  public void setUseSSPSCompatibleTimezoneShift(boolean paramBoolean) {
    this.mc.setUseSSPSCompatibleTimezoneShift(paramBoolean);
  }
  
  public void setUseServerPrepStmts(boolean paramBoolean) {
    this.mc.setUseServerPrepStmts(paramBoolean);
  }
  
  public void setUseServerPreparedStmts(boolean paramBoolean) {
    this.mc.setUseServerPreparedStmts(paramBoolean);
  }
  
  public void setUseSqlStateCodes(boolean paramBoolean) {
    this.mc.setUseSqlStateCodes(paramBoolean);
  }
  
  public void setUseStreamLengthsInPrepStmts(boolean paramBoolean) {
    this.mc.setUseStreamLengthsInPrepStmts(paramBoolean);
  }
  
  public void setUseTimezone(boolean paramBoolean) {
    this.mc.setUseTimezone(paramBoolean);
  }
  
  public void setUseUltraDevWorkAround(boolean paramBoolean) {
    this.mc.setUseUltraDevWorkAround(paramBoolean);
  }
  
  public void setUseUnbufferedInput(boolean paramBoolean) {
    this.mc.setUseUnbufferedInput(paramBoolean);
  }
  
  public void setUseUnicode(boolean paramBoolean) {
    this.mc.setUseUnicode(paramBoolean);
  }
  
  public void setUseUsageAdvisor(boolean paramBoolean) {
    this.mc.setUseUsageAdvisor(paramBoolean);
  }
  
  public void setUtf8OutsideBmpExcludedColumnNamePattern(String paramString) {
    this.mc.setUtf8OutsideBmpExcludedColumnNamePattern(paramString);
  }
  
  public void setUtf8OutsideBmpIncludedColumnNamePattern(String paramString) {
    this.mc.setUtf8OutsideBmpIncludedColumnNamePattern(paramString);
  }
  
  public void setVerifyServerCertificate(boolean paramBoolean) {
    this.mc.setVerifyServerCertificate(paramBoolean);
  }
  
  public void setYearIsDateType(boolean paramBoolean) {
    this.mc.setYearIsDateType(paramBoolean);
  }
  
  public void setZeroDateTimeBehavior(String paramString) {
    this.mc.setZeroDateTimeBehavior(paramString);
  }
  
  public void shutdownServer() throws SQLException {
    checkClosed();
    try {
      this.mc.shutdownServer();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public boolean supportsIsolationLevel() {
    return this.mc.supportsIsolationLevel();
  }
  
  public boolean supportsQuotedIdentifiers() {
    return this.mc.supportsQuotedIdentifiers();
  }
  
  public boolean supportsTransactions() {
    return this.mc.supportsTransactions();
  }
  
  public boolean useUnbufferedInput() {
    return this.mc.useUnbufferedInput();
  }
  
  public boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    checkClosed();
    try {
      return this.mc.versionMeetsMinimum(paramInt1, paramInt2, paramInt3);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
}
