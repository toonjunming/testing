package com.mysql.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

public interface JDBC4MySQLConnection extends MySQLConnection {
  Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException;
  
  Blob createBlob();
  
  Clob createClob();
  
  NClob createNClob();
  
  SQLXML createSQLXML() throws SQLException;
  
  Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException;
  
  String getClientInfo(String paramString) throws SQLException;
  
  Properties getClientInfo() throws SQLException;
  
  JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException;
  
  boolean isValid(int paramInt) throws SQLException;
  
  boolean isWrapperFor(Class<?> paramClass) throws SQLException;
  
  void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException;
  
  void setClientInfo(Properties paramProperties) throws SQLClientInfoException;
  
  <T> T unwrap(Class<T> paramClass) throws SQLException;
}
