package com.mysql.jdbc;

import java.io.Reader;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

public class JDBC4PreparedStatementHelper {
  public static void setNClob(PreparedStatement paramPreparedStatement, int paramInt, Reader paramReader) throws SQLException {
    paramPreparedStatement.setNCharacterStream(paramInt, paramReader);
  }
  
  public static void setNClob(PreparedStatement paramPreparedStatement, int paramInt, Reader paramReader, long paramLong) throws SQLException {
    if (paramReader == null) {
      paramPreparedStatement.setNull(paramInt, 2011);
    } else {
      paramPreparedStatement.setNCharacterStream(paramInt, paramReader, paramLong);
    } 
  }
  
  public static void setNClob(PreparedStatement paramPreparedStatement, int paramInt, NClob paramNClob) throws SQLException {
    if (paramNClob == null) {
      paramPreparedStatement.setNull(paramInt, 2011);
    } else {
      paramPreparedStatement.setNCharacterStream(paramInt, paramNClob.getCharacterStream(), paramNClob.length());
    } 
  }
  
  public static void setRowId(PreparedStatement paramPreparedStatement, int paramInt, RowId paramRowId) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public static void setSQLXML(PreparedStatement paramPreparedStatement, int paramInt, SQLXML paramSQLXML) throws SQLException {
    if (paramSQLXML == null) {
      paramPreparedStatement.setNull(paramInt, 2009);
    } else {
      paramPreparedStatement.setCharacterStream(paramInt, ((JDBC4MysqlSQLXML)paramSQLXML).serializeAsCharacterStream());
    } 
  }
}
