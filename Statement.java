package com.mysql.jdbc;

import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;

public interface Statement extends Statement, Wrapper {
  void disableStreamingResults() throws SQLException;
  
  void enableStreamingResults() throws SQLException;
  
  ExceptionInterceptor getExceptionInterceptor();
  
  InputStream getLocalInfileInputStream();
  
  int getOpenResultSetCount();
  
  void removeOpenResultSet(ResultSetInternalMethods paramResultSetInternalMethods);
  
  void setHoldResultsOpenOverClose(boolean paramBoolean);
  
  void setLocalInfileInputStream(InputStream paramInputStream);
  
  void setPingTarget(PingTarget paramPingTarget);
}
