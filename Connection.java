package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public interface Connection extends Connection, ConnectionProperties {
  void abort(Executor paramExecutor) throws SQLException;
  
  void abortInternal() throws SQLException;
  
  void changeUser(String paramString1, String paramString2) throws SQLException;
  
  void checkClosed() throws SQLException;
  
  @Deprecated
  void clearHasTriedMaster();
  
  PreparedStatement clientPrepareStatement(String paramString) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int paramInt) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException;
  
  int getActiveStatementCount();
  
  int getAutoIncrementIncrement();
  
  Object getConnectionMutex();
  
  String getHost();
  
  long getIdleFor();
  
  Log getLog() throws SQLException;
  
  int getNetworkTimeout() throws SQLException;
  
  Properties getProperties();
  
  String getSchema() throws SQLException;
  
  @Deprecated
  String getServerCharacterEncoding();
  
  String getServerCharset();
  
  TimeZone getServerTimezoneTZ();
  
  int getSessionMaxRows();
  
  String getStatementComment();
  
  boolean hasSameProperties(Connection paramConnection);
  
  @Deprecated
  boolean hasTriedMaster();
  
  void initializeExtension(Extension paramExtension) throws SQLException;
  
  boolean isAbonormallyLongQuery(long paramLong);
  
  boolean isInGlobalTx();
  
  boolean isMasterConnection();
  
  boolean isNoBackslashEscapesSet();
  
  boolean isSameResource(Connection paramConnection);
  
  boolean isServerLocal() throws SQLException;
  
  boolean lowerCaseTableNames();
  
  boolean parserKnowsUnicode();
  
  void ping() throws SQLException;
  
  void reportQueryTime(long paramLong);
  
  void resetServerState() throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int paramInt) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException;
  
  void setFailedOver(boolean paramBoolean);
  
  void setInGlobalTx(boolean paramBoolean);
  
  void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException;
  
  @Deprecated
  void setPreferSlaveDuringFailover(boolean paramBoolean);
  
  void setProxy(MySQLConnection paramMySQLConnection);
  
  void setSchema(String paramString) throws SQLException;
  
  void setSessionMaxRows(int paramInt) throws SQLException;
  
  void setStatementComment(String paramString);
  
  void shutdownServer() throws SQLException;
  
  boolean supportsIsolationLevel();
  
  boolean supportsQuotedIdentifiers();
  
  boolean supportsTransactions();
  
  boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3) throws SQLException;
}
