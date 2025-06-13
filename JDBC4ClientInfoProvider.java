package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public interface JDBC4ClientInfoProvider {
  void destroy() throws SQLException;
  
  String getClientInfo(Connection paramConnection, String paramString) throws SQLException;
  
  Properties getClientInfo(Connection paramConnection) throws SQLException;
  
  void initialize(Connection paramConnection, Properties paramProperties) throws SQLException;
  
  void setClientInfo(Connection paramConnection, String paramString1, String paramString2) throws SQLClientInfoException;
  
  void setClientInfo(Connection paramConnection, Properties paramProperties) throws SQLClientInfoException;
}
