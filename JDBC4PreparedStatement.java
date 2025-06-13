package com.mysql.jdbc;

import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

public class JDBC4PreparedStatement extends PreparedStatement {
  public JDBC4PreparedStatement(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    super(paramMySQLConnection, paramString);
  }
  
  public JDBC4PreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2);
  }
  
  public JDBC4PreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, PreparedStatement.ParseInfo paramParseInfo) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2, paramParseInfo);
  }
  
  public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
    JDBC4PreparedStatementHelper.setNClob(this, paramInt, paramNClob);
  }
  
  public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
    JDBC4PreparedStatementHelper.setRowId(this, paramInt, paramRowId);
  }
  
  public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
    JDBC4PreparedStatementHelper.setSQLXML(this, paramInt, paramSQLXML);
  }
}
