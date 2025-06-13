package com.mysql.jdbc;

import java.sql.SQLException;

public interface ConnectionProperties {
  String exposeAsXml() throws SQLException;
  
  boolean getAllowLoadLocalInfile();
  
  boolean getAllowMasterDownConnections();
  
  boolean getAllowMultiQueries();
  
  boolean getAllowNanAndInf();
  
  boolean getAllowPublicKeyRetrieval();
  
  boolean getAllowSlaveDownConnections();
  
  boolean getAllowUrlInLocalInfile();
  
  boolean getAlwaysSendSetIsolation();
  
  String getAuthenticationPlugins();
  
  boolean getAutoClosePStmtStreams();
  
  boolean getAutoDeserialize();
  
  boolean getAutoGenerateTestcaseScript();
  
  boolean getAutoReconnectForPools();
  
  boolean getAutoSlowLog();
  
  int getBlobSendChunkSize();
  
  boolean getBlobsAreStrings();
  
  boolean getCacheCallableStatements();
  
  boolean getCacheCallableStmts();
  
  boolean getCacheDefaultTimezone();
  
  boolean getCachePrepStmts();
  
  boolean getCachePreparedStatements();
  
  boolean getCacheResultSetMetadata();
  
  boolean getCacheServerConfiguration();
  
  int getCallableStatementCacheSize();
  
  int getCallableStmtCacheSize();
  
  boolean getCapitalizeTypeNames();
  
  String getCharacterSetResults();
  
  String getClientCertificateKeyStorePassword();
  
  String getClientCertificateKeyStoreType();
  
  String getClientCertificateKeyStoreUrl();
  
  String getClientInfoProvider();
  
  String getClobCharacterEncoding();
  
  boolean getClobberStreamingResults();
  
  boolean getCompensateOnDuplicateKeyUpdateCounts();
  
  int getConnectTimeout();
  
  String getConnectionAttributes() throws SQLException;
  
  String getConnectionCollation();
  
  String getConnectionLifecycleInterceptors();
  
  boolean getContinueBatchOnError();
  
  boolean getCreateDatabaseIfNotExist();
  
  String getDefaultAuthenticationPlugin();
  
  int getDefaultFetchSize();
  
  boolean getDetectCustomCollations();
  
  String getDisabledAuthenticationPlugins();
  
  boolean getDisconnectOnExpiredPasswords();
  
  boolean getDontCheckOnDuplicateKeyUpdateInSQL();
  
  boolean getDontTrackOpenResources();
  
  boolean getDumpMetadataOnColumnNotFound();
  
  boolean getDumpQueriesOnException();
  
  boolean getDynamicCalendars();
  
  boolean getElideSetAutoCommits();
  
  boolean getEmptyStringsConvertToZero();
  
  boolean getEmulateLocators();
  
  boolean getEmulateUnsupportedPstmts();
  
  boolean getEnableEscapeProcessing();
  
  boolean getEnablePacketDebug();
  
  boolean getEnableQueryTimeouts();
  
  String getEnabledSSLCipherSuites();
  
  String getEnabledTLSProtocols();
  
  String getEncoding();
  
  ExceptionInterceptor getExceptionInterceptor();
  
  String getExceptionInterceptors();
  
  boolean getExplainSlowQueries();
  
  boolean getFailOverReadOnly();
  
  boolean getFunctionsNeverReturnBlobs();
  
  boolean getGatherPerfMetrics();
  
  boolean getGatherPerformanceMetrics();
  
  boolean getGenerateSimpleParameterMetadata();
  
  boolean getGetProceduresReturnsFunctions();
  
  boolean getHoldResultsOpenOverStatementClose();
  
  boolean getIgnoreNonTxTables();
  
  boolean getIncludeInnodbStatusInDeadlockExceptions();
  
  boolean getIncludeThreadDumpInDeadlockExceptions();
  
  boolean getIncludeThreadNamesAsStatementComment();
  
  int getInitialTimeout();
  
  boolean getInteractiveClient();
  
  boolean getIsInteractiveClient();
  
  boolean getJdbcCompliantTruncation();
  
  boolean getJdbcCompliantTruncationForReads();
  
  String getLargeRowSizeThreshold();
  
  String getLoadBalanceAutoCommitStatementRegex();
  
  int getLoadBalanceAutoCommitStatementThreshold();
  
  int getLoadBalanceBlacklistTimeout();
  
  String getLoadBalanceConnectionGroup();
  
  boolean getLoadBalanceEnableJMX();
  
  String getLoadBalanceExceptionChecker();
  
  int getLoadBalanceHostRemovalGracePeriod();
  
  int getLoadBalancePingTimeout();
  
  String getLoadBalanceSQLExceptionSubclassFailover();
  
  String getLoadBalanceSQLStateFailover();
  
  String getLoadBalanceStrategy();
  
  boolean getLoadBalanceValidateConnectionOnSwapServer();
  
  String getLocalSocketAddress();
  
  int getLocatorFetchBufferSize();
  
  boolean getLogSlowQueries();
  
  boolean getLogXaCommands();
  
  String getLogger();
  
  String getLoggerClassName();
  
  boolean getMaintainTimeStats();
  
  int getMaxAllowedPacket();
  
  int getMaxQuerySizeToLog();
  
  int getMaxReconnects();
  
  int getMaxRows();
  
  int getMetadataCacheSize();
  
  int getNetTimeoutForStreamingResults();
  
  boolean getNoAccessToProcedureBodies();
  
  boolean getNoDatetimeStringSync();
  
  boolean getNoTimezoneConversionForDateType();
  
  boolean getNoTimezoneConversionForTimeType();
  
  boolean getNullCatalogMeansCurrent();
  
  boolean getNullNamePatternMatchesAll();
  
  boolean getOverrideSupportsIntegrityEnhancementFacility();
  
  int getPacketDebugBufferSize();
  
  boolean getPadCharsWithSpace();
  
  boolean getParanoid();
  
  String getParseInfoCacheFactory();
  
  String getPasswordCharacterEncoding();
  
  boolean getPedantic();
  
  boolean getPinGlobalTxToPhysicalConnection();
  
  boolean getPopulateInsertRowWithDefaultValues();
  
  int getPrepStmtCacheSize();
  
  int getPrepStmtCacheSqlLimit();
  
  int getPreparedStatementCacheSize();
  
  int getPreparedStatementCacheSqlLimit();
  
  boolean getProcessEscapeCodesForPrepStmts();
  
  boolean getProfileSQL();
  
  boolean getProfileSql();
  
  String getProfilerEventHandler();
  
  String getPropertiesTransform();
  
  int getQueriesBeforeRetryMaster();
  
  boolean getQueryTimeoutKillsConnection();
  
  boolean getReadFromMasterWhenNoSlaves();
  
  boolean getReadOnlyPropagatesToServer();
  
  boolean getReconnectAtTxEnd();
  
  boolean getRelaxAutoCommit();
  
  boolean getReplicationEnableJMX();
  
  int getReportMetricsIntervalMillis();
  
  boolean getRequireSSL();
  
  String getResourceId();
  
  int getResultSetSizeThreshold();
  
  boolean getRetainStatementAfterResultSetClose();
  
  int getRetriesAllDown();
  
  boolean getRewriteBatchedStatements();
  
  boolean getRollbackOnPooledClose();
  
  boolean getRoundRobinLoadBalance();
  
  boolean getRunningCTS13();
  
  int getSecondsBeforeRetryMaster();
  
  int getSelfDestructOnPingMaxOperations();
  
  int getSelfDestructOnPingSecondsLifetime();
  
  boolean getSendFractionalSeconds();
  
  String getServerAffinityOrder();
  
  String getServerConfigCacheFactory();
  
  String getServerRSAPublicKeyFile();
  
  String getServerTimezone();
  
  String getSessionVariables();
  
  int getSlowQueryThresholdMillis();
  
  long getSlowQueryThresholdNanos();
  
  String getSocketFactory();
  
  String getSocketFactoryClassName();
  
  int getSocketTimeout();
  
  String getSocksProxyHost();
  
  int getSocksProxyPort();
  
  String getStatementInterceptors();
  
  boolean getStrictFloatingPoint();
  
  boolean getStrictUpdates();
  
  boolean getTcpKeepAlive();
  
  boolean getTcpNoDelay();
  
  int getTcpRcvBuf();
  
  int getTcpSndBuf();
  
  int getTcpTrafficClass();
  
  boolean getTinyInt1isBit();
  
  boolean getTraceProtocol();
  
  boolean getTransformedBitIsBoolean();
  
  boolean getTreatUtilDateAsTimestamp();
  
  String getTrustCertificateKeyStorePassword();
  
  String getTrustCertificateKeyStoreType();
  
  String getTrustCertificateKeyStoreUrl();
  
  boolean getUltraDevHack();
  
  boolean getUseAffectedRows();
  
  boolean getUseBlobToStoreUTF8OutsideBMP();
  
  boolean getUseColumnNamesInFindColumn();
  
  boolean getUseCompression();
  
  String getUseConfigs();
  
  boolean getUseCursorFetch();
  
  boolean getUseDirectRowUnpack();
  
  boolean getUseDynamicCharsetInfo();
  
  boolean getUseFastDateParsing();
  
  boolean getUseFastIntParsing();
  
  boolean getUseGmtMillisForDatetimes();
  
  boolean getUseHostsInPrivileges();
  
  boolean getUseInformationSchema();
  
  boolean getUseJDBCCompliantTimezoneShift();
  
  boolean getUseJvmCharsetConverters();
  
  boolean getUseLegacyDatetimeCode();
  
  boolean getUseLocalSessionState();
  
  boolean getUseLocalTransactionState();
  
  boolean getUseNanosForElapsedTime();
  
  boolean getUseOldAliasMetadataBehavior();
  
  boolean getUseOldUTF8Behavior();
  
  boolean getUseOnlyServerErrorMessages();
  
  boolean getUseReadAheadInput();
  
  boolean getUseSSL();
  
  boolean getUseSSPSCompatibleTimezoneShift();
  
  boolean getUseServerPrepStmts();
  
  boolean getUseServerPreparedStmts();
  
  boolean getUseSqlStateCodes();
  
  boolean getUseStreamLengthsInPrepStmts();
  
  boolean getUseTimezone();
  
  boolean getUseUltraDevWorkAround();
  
  boolean getUseUnbufferedInput();
  
  boolean getUseUnicode();
  
  boolean getUseUsageAdvisor();
  
  String getUtf8OutsideBmpExcludedColumnNamePattern();
  
  String getUtf8OutsideBmpIncludedColumnNamePattern();
  
  boolean getVerifyServerCertificate();
  
  boolean getYearIsDateType();
  
  String getZeroDateTimeBehavior();
  
  boolean isUseSSLExplicit();
  
  void setAllowLoadLocalInfile(boolean paramBoolean);
  
  void setAllowMasterDownConnections(boolean paramBoolean);
  
  void setAllowMultiQueries(boolean paramBoolean);
  
  void setAllowNanAndInf(boolean paramBoolean);
  
  void setAllowPublicKeyRetrieval(boolean paramBoolean) throws SQLException;
  
  void setAllowSlaveDownConnections(boolean paramBoolean);
  
  void setAllowUrlInLocalInfile(boolean paramBoolean);
  
  void setAlwaysSendSetIsolation(boolean paramBoolean);
  
  void setAuthenticationPlugins(String paramString);
  
  void setAutoClosePStmtStreams(boolean paramBoolean);
  
  void setAutoDeserialize(boolean paramBoolean);
  
  void setAutoGenerateTestcaseScript(boolean paramBoolean);
  
  void setAutoReconnect(boolean paramBoolean);
  
  void setAutoReconnectForConnectionPools(boolean paramBoolean);
  
  void setAutoReconnectForPools(boolean paramBoolean);
  
  void setAutoSlowLog(boolean paramBoolean);
  
  void setBlobSendChunkSize(String paramString) throws SQLException;
  
  void setBlobsAreStrings(boolean paramBoolean);
  
  void setCacheCallableStatements(boolean paramBoolean);
  
  void setCacheCallableStmts(boolean paramBoolean);
  
  void setCacheDefaultTimezone(boolean paramBoolean);
  
  void setCachePrepStmts(boolean paramBoolean);
  
  void setCachePreparedStatements(boolean paramBoolean);
  
  void setCacheResultSetMetadata(boolean paramBoolean);
  
  void setCacheServerConfiguration(boolean paramBoolean);
  
  void setCallableStatementCacheSize(int paramInt) throws SQLException;
  
  void setCallableStmtCacheSize(int paramInt) throws SQLException;
  
  void setCapitalizeDBMDTypes(boolean paramBoolean);
  
  void setCapitalizeTypeNames(boolean paramBoolean);
  
  void setCharacterEncoding(String paramString);
  
  void setCharacterSetResults(String paramString);
  
  void setClientCertificateKeyStorePassword(String paramString);
  
  void setClientCertificateKeyStoreType(String paramString);
  
  void setClientCertificateKeyStoreUrl(String paramString);
  
  void setClientInfoProvider(String paramString);
  
  void setClobCharacterEncoding(String paramString);
  
  void setClobberStreamingResults(boolean paramBoolean);
  
  void setCompensateOnDuplicateKeyUpdateCounts(boolean paramBoolean);
  
  void setConnectTimeout(int paramInt) throws SQLException;
  
  void setConnectionCollation(String paramString);
  
  void setConnectionLifecycleInterceptors(String paramString);
  
  void setContinueBatchOnError(boolean paramBoolean);
  
  void setCreateDatabaseIfNotExist(boolean paramBoolean);
  
  void setDefaultAuthenticationPlugin(String paramString);
  
  void setDefaultFetchSize(int paramInt) throws SQLException;
  
  void setDetectCustomCollations(boolean paramBoolean);
  
  void setDetectServerPreparedStmts(boolean paramBoolean);
  
  void setDisabledAuthenticationPlugins(String paramString);
  
  void setDisconnectOnExpiredPasswords(boolean paramBoolean);
  
  void setDontCheckOnDuplicateKeyUpdateInSQL(boolean paramBoolean);
  
  void setDontTrackOpenResources(boolean paramBoolean);
  
  void setDumpMetadataOnColumnNotFound(boolean paramBoolean);
  
  void setDumpQueriesOnException(boolean paramBoolean);
  
  void setDynamicCalendars(boolean paramBoolean);
  
  void setElideSetAutoCommits(boolean paramBoolean);
  
  void setEmptyStringsConvertToZero(boolean paramBoolean);
  
  void setEmulateLocators(boolean paramBoolean);
  
  void setEmulateUnsupportedPstmts(boolean paramBoolean);
  
  void setEnableEscapeProcessing(boolean paramBoolean);
  
  void setEnablePacketDebug(boolean paramBoolean);
  
  void setEnableQueryTimeouts(boolean paramBoolean);
  
  void setEnabledSSLCipherSuites(String paramString);
  
  void setEnabledTLSProtocols(String paramString);
  
  void setEncoding(String paramString);
  
  void setExceptionInterceptors(String paramString);
  
  void setExplainSlowQueries(boolean paramBoolean);
  
  void setFailOverReadOnly(boolean paramBoolean);
  
  void setFunctionsNeverReturnBlobs(boolean paramBoolean);
  
  void setGatherPerfMetrics(boolean paramBoolean);
  
  void setGatherPerformanceMetrics(boolean paramBoolean);
  
  void setGenerateSimpleParameterMetadata(boolean paramBoolean);
  
  void setGetProceduresReturnsFunctions(boolean paramBoolean);
  
  void setHoldResultsOpenOverStatementClose(boolean paramBoolean);
  
  void setIgnoreNonTxTables(boolean paramBoolean);
  
  void setIncludeInnodbStatusInDeadlockExceptions(boolean paramBoolean);
  
  void setIncludeThreadDumpInDeadlockExceptions(boolean paramBoolean);
  
  void setIncludeThreadNamesAsStatementComment(boolean paramBoolean);
  
  void setInitialTimeout(int paramInt) throws SQLException;
  
  void setInteractiveClient(boolean paramBoolean);
  
  void setIsInteractiveClient(boolean paramBoolean);
  
  void setJdbcCompliantTruncation(boolean paramBoolean);
  
  void setJdbcCompliantTruncationForReads(boolean paramBoolean);
  
  void setLargeRowSizeThreshold(String paramString) throws SQLException;
  
  void setLoadBalanceAutoCommitStatementRegex(String paramString);
  
  void setLoadBalanceAutoCommitStatementThreshold(int paramInt) throws SQLException;
  
  void setLoadBalanceBlacklistTimeout(int paramInt) throws SQLException;
  
  void setLoadBalanceConnectionGroup(String paramString);
  
  void setLoadBalanceEnableJMX(boolean paramBoolean);
  
  void setLoadBalanceExceptionChecker(String paramString);
  
  void setLoadBalanceHostRemovalGracePeriod(int paramInt) throws SQLException;
  
  void setLoadBalancePingTimeout(int paramInt) throws SQLException;
  
  void setLoadBalanceSQLExceptionSubclassFailover(String paramString);
  
  void setLoadBalanceSQLStateFailover(String paramString);
  
  void setLoadBalanceStrategy(String paramString);
  
  void setLoadBalanceValidateConnectionOnSwapServer(boolean paramBoolean);
  
  void setLocalSocketAddress(String paramString);
  
  void setLocatorFetchBufferSize(String paramString) throws SQLException;
  
  void setLogSlowQueries(boolean paramBoolean);
  
  void setLogXaCommands(boolean paramBoolean);
  
  void setLogger(String paramString);
  
  void setLoggerClassName(String paramString);
  
  void setMaintainTimeStats(boolean paramBoolean);
  
  void setMaxQuerySizeToLog(int paramInt) throws SQLException;
  
  void setMaxReconnects(int paramInt) throws SQLException;
  
  void setMaxRows(int paramInt) throws SQLException;
  
  void setMetadataCacheSize(int paramInt) throws SQLException;
  
  void setNetTimeoutForStreamingResults(int paramInt) throws SQLException;
  
  void setNoAccessToProcedureBodies(boolean paramBoolean);
  
  void setNoDatetimeStringSync(boolean paramBoolean);
  
  void setNoTimezoneConversionForDateType(boolean paramBoolean);
  
  void setNoTimezoneConversionForTimeType(boolean paramBoolean);
  
  void setNullCatalogMeansCurrent(boolean paramBoolean);
  
  void setNullNamePatternMatchesAll(boolean paramBoolean);
  
  void setOverrideSupportsIntegrityEnhancementFacility(boolean paramBoolean);
  
  void setPacketDebugBufferSize(int paramInt) throws SQLException;
  
  void setPadCharsWithSpace(boolean paramBoolean);
  
  void setParanoid(boolean paramBoolean);
  
  void setParseInfoCacheFactory(String paramString);
  
  void setPasswordCharacterEncoding(String paramString);
  
  void setPedantic(boolean paramBoolean);
  
  void setPinGlobalTxToPhysicalConnection(boolean paramBoolean);
  
  void setPopulateInsertRowWithDefaultValues(boolean paramBoolean);
  
  void setPrepStmtCacheSize(int paramInt) throws SQLException;
  
  void setPrepStmtCacheSqlLimit(int paramInt) throws SQLException;
  
  void setPreparedStatementCacheSize(int paramInt) throws SQLException;
  
  void setPreparedStatementCacheSqlLimit(int paramInt) throws SQLException;
  
  void setProcessEscapeCodesForPrepStmts(boolean paramBoolean);
  
  void setProfileSQL(boolean paramBoolean);
  
  void setProfileSql(boolean paramBoolean);
  
  void setProfilerEventHandler(String paramString);
  
  void setPropertiesTransform(String paramString);
  
  void setQueriesBeforeRetryMaster(int paramInt) throws SQLException;
  
  void setQueryTimeoutKillsConnection(boolean paramBoolean);
  
  void setReadFromMasterWhenNoSlaves(boolean paramBoolean);
  
  void setReadOnlyPropagatesToServer(boolean paramBoolean);
  
  void setReconnectAtTxEnd(boolean paramBoolean);
  
  void setRelaxAutoCommit(boolean paramBoolean);
  
  void setReplicationEnableJMX(boolean paramBoolean);
  
  void setReportMetricsIntervalMillis(int paramInt) throws SQLException;
  
  void setRequireSSL(boolean paramBoolean);
  
  void setResourceId(String paramString);
  
  void setResultSetSizeThreshold(int paramInt) throws SQLException;
  
  void setRetainStatementAfterResultSetClose(boolean paramBoolean);
  
  void setRetriesAllDown(int paramInt) throws SQLException;
  
  void setRewriteBatchedStatements(boolean paramBoolean);
  
  void setRollbackOnPooledClose(boolean paramBoolean);
  
  void setRoundRobinLoadBalance(boolean paramBoolean);
  
  void setRunningCTS13(boolean paramBoolean);
  
  void setSecondsBeforeRetryMaster(int paramInt) throws SQLException;
  
  void setSelfDestructOnPingMaxOperations(int paramInt) throws SQLException;
  
  void setSelfDestructOnPingSecondsLifetime(int paramInt) throws SQLException;
  
  void setSendFractionalSeconds(boolean paramBoolean);
  
  void setServerAffinityOrder(String paramString);
  
  void setServerConfigCacheFactory(String paramString);
  
  void setServerRSAPublicKeyFile(String paramString) throws SQLException;
  
  void setServerTimezone(String paramString);
  
  void setSessionVariables(String paramString);
  
  void setSlowQueryThresholdMillis(int paramInt) throws SQLException;
  
  void setSlowQueryThresholdNanos(long paramLong) throws SQLException;
  
  void setSocketFactory(String paramString);
  
  void setSocketFactoryClassName(String paramString);
  
  void setSocketTimeout(int paramInt) throws SQLException;
  
  void setSocksProxyHost(String paramString);
  
  void setSocksProxyPort(int paramInt) throws SQLException;
  
  void setStatementInterceptors(String paramString);
  
  void setStrictFloatingPoint(boolean paramBoolean);
  
  void setStrictUpdates(boolean paramBoolean);
  
  void setTcpKeepAlive(boolean paramBoolean);
  
  void setTcpNoDelay(boolean paramBoolean);
  
  void setTcpRcvBuf(int paramInt) throws SQLException;
  
  void setTcpSndBuf(int paramInt) throws SQLException;
  
  void setTcpTrafficClass(int paramInt) throws SQLException;
  
  void setTinyInt1isBit(boolean paramBoolean);
  
  void setTraceProtocol(boolean paramBoolean);
  
  void setTransformedBitIsBoolean(boolean paramBoolean);
  
  void setTreatUtilDateAsTimestamp(boolean paramBoolean);
  
  void setTrustCertificateKeyStorePassword(String paramString);
  
  void setTrustCertificateKeyStoreType(String paramString);
  
  void setTrustCertificateKeyStoreUrl(String paramString);
  
  void setUltraDevHack(boolean paramBoolean);
  
  void setUseAffectedRows(boolean paramBoolean);
  
  void setUseBlobToStoreUTF8OutsideBMP(boolean paramBoolean);
  
  void setUseColumnNamesInFindColumn(boolean paramBoolean);
  
  void setUseCompression(boolean paramBoolean);
  
  void setUseConfigs(String paramString);
  
  void setUseCursorFetch(boolean paramBoolean);
  
  void setUseDirectRowUnpack(boolean paramBoolean);
  
  void setUseDynamicCharsetInfo(boolean paramBoolean);
  
  void setUseFastDateParsing(boolean paramBoolean);
  
  void setUseFastIntParsing(boolean paramBoolean);
  
  void setUseGmtMillisForDatetimes(boolean paramBoolean);
  
  void setUseHostsInPrivileges(boolean paramBoolean);
  
  void setUseInformationSchema(boolean paramBoolean);
  
  void setUseJDBCCompliantTimezoneShift(boolean paramBoolean);
  
  void setUseJvmCharsetConverters(boolean paramBoolean);
  
  void setUseLegacyDatetimeCode(boolean paramBoolean);
  
  void setUseLocalSessionState(boolean paramBoolean);
  
  void setUseLocalTransactionState(boolean paramBoolean);
  
  void setUseNanosForElapsedTime(boolean paramBoolean);
  
  void setUseOldAliasMetadataBehavior(boolean paramBoolean);
  
  void setUseOldUTF8Behavior(boolean paramBoolean);
  
  void setUseOnlyServerErrorMessages(boolean paramBoolean);
  
  void setUseReadAheadInput(boolean paramBoolean);
  
  void setUseSSL(boolean paramBoolean);
  
  void setUseSSPSCompatibleTimezoneShift(boolean paramBoolean);
  
  void setUseServerPrepStmts(boolean paramBoolean);
  
  void setUseServerPreparedStmts(boolean paramBoolean);
  
  void setUseSqlStateCodes(boolean paramBoolean);
  
  void setUseStreamLengthsInPrepStmts(boolean paramBoolean);
  
  void setUseTimezone(boolean paramBoolean);
  
  void setUseUltraDevWorkAround(boolean paramBoolean);
  
  void setUseUnbufferedInput(boolean paramBoolean);
  
  void setUseUnicode(boolean paramBoolean);
  
  void setUseUsageAdvisor(boolean paramBoolean);
  
  void setUtf8OutsideBmpExcludedColumnNamePattern(String paramString);
  
  void setUtf8OutsideBmpIncludedColumnNamePattern(String paramString);
  
  void setVerifyServerCertificate(boolean paramBoolean);
  
  void setYearIsDateType(boolean paramBoolean);
  
  void setZeroDateTimeBehavior(String paramString);
  
  boolean useUnbufferedInput();
}
