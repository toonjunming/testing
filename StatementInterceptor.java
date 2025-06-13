package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public interface StatementInterceptor extends Extension {
  void destroy();
  
  boolean executeTopLevelOnly();
  
  void init(Connection paramConnection, Properties paramProperties) throws SQLException;
  
  ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection) throws SQLException;
  
  ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException;
}
