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

public class JDBC4MultiHostMySQLConnection extends MultiHostMySQLConnection implements JDBC4MySQLConnection {
  public JDBC4MultiHostMySQLConnection(MultiHostConnectionProxy paramMultiHostConnectionProxy) throws SQLException {
    super(paramMultiHostConnectionProxy);
  }
  
  private JDBC4Connection getJDBC4Connection() {
    return (JDBC4Connection)(getThisAsProxy()).currentConnection;
  }
  
  public Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException {
    return getJDBC4Connection().createArrayOf(paramString, paramArrayOfObject);
  }
  
  public Blob createBlob() {
    return getJDBC4Connection().createBlob();
  }
  
  public Clob createClob() {
    return getJDBC4Connection().createClob();
  }
  
  public NClob createNClob() {
    return getJDBC4Connection().createNClob();
  }
  
  public SQLXML createSQLXML() throws SQLException {
    return getJDBC4Connection().createSQLXML();
  }
  
  public Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException {
    return getJDBC4Connection().createStruct(paramString, paramArrayOfObject);
  }
  
  public String getClientInfo(String paramString) throws SQLException {
    return getJDBC4Connection().getClientInfo(paramString);
  }
  
  public Properties getClientInfo() throws SQLException {
    return getJDBC4Connection().getClientInfo();
  }
  
  public JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
    synchronized (getThisAsProxy()) {
      return getJDBC4Connection().getClientInfoProviderImpl();
    } 
  }
  
  public boolean isValid(int paramInt) throws SQLException {
    synchronized (getThisAsProxy()) {
      return getJDBC4Connection().isValid(paramInt);
    } 
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    checkClosed();
    return paramClass.isInstance(this);
  }
  
  public void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException {
    getJDBC4Connection().setClientInfo(paramString1, paramString2);
  }
  
  public void setClientInfo(Properties paramProperties) throws SQLClientInfoException {
    getJDBC4Connection().setClientInfo(paramProperties);
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
}
