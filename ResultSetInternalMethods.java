package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public interface ResultSetInternalMethods extends ResultSet {
  void buildIndexMapping() throws SQLException;
  
  void clearNextResult();
  
  ResultSetInternalMethods copy() throws SQLException;
  
  int getBytesSize() throws SQLException;
  
  char getFirstCharOfQuery();
  
  ResultSetInternalMethods getNextResultSet();
  
  Object getObjectStoredProc(int paramInt1, int paramInt2) throws SQLException;
  
  Object getObjectStoredProc(int paramInt1, Map<Object, Object> paramMap, int paramInt2) throws SQLException;
  
  Object getObjectStoredProc(String paramString, int paramInt) throws SQLException;
  
  Object getObjectStoredProc(String paramString, Map<Object, Object> paramMap, int paramInt) throws SQLException;
  
  String getServerInfo();
  
  long getUpdateCount();
  
  long getUpdateID();
  
  void initializeFromCachedMetaData(CachedResultSetMetaData paramCachedResultSetMetaData);
  
  void initializeWithMetadata() throws SQLException;
  
  boolean isClosed() throws SQLException;
  
  void populateCachedMetaData(CachedResultSetMetaData paramCachedResultSetMetaData) throws SQLException;
  
  void realClose(boolean paramBoolean) throws SQLException;
  
  boolean reallyResult();
  
  void redefineFieldsForDBMD(Field[] paramArrayOfField);
  
  void setFirstCharOfQuery(char paramChar);
  
  void setOwningStatement(StatementImpl paramStatementImpl);
  
  void setStatementUsedForFetchingRows(PreparedStatement paramPreparedStatement);
  
  void setWrapperStatement(Statement paramStatement);
}
