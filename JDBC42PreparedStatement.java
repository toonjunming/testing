package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.SQLType;

public class JDBC42PreparedStatement extends JDBC4PreparedStatement {
  public JDBC42PreparedStatement(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    super(paramMySQLConnection, paramString);
  }
  
  public JDBC42PreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2);
  }
  
  public JDBC42PreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, PreparedStatement.ParseInfo paramParseInfo) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2, paramParseInfo);
  }
  
  private int checkSqlType(int paramInt) throws SQLException {
    return JDBC42Helper.checkSqlType(paramInt, getExceptionInterceptor());
  }
  
  private int translateAndCheckSqlType(SQLType paramSQLType) throws SQLException {
    return JDBC42Helper.translateAndCheckSqlType(paramSQLType, getExceptionInterceptor());
  }
  
  public void setObject(int paramInt, Object paramObject) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      super.setObject(paramInt, JDBC42Helper.convertJavaTimeToJavaSql(paramObject));
      return;
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      super.setObject(paramInt1, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), checkSqlType(paramInt2));
      return;
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      super.setObject(paramInt1, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), checkSqlType(paramInt2), paramInt3);
      return;
    } 
  }
  
  public void setObject(int paramInt, Object paramObject, SQLType paramSQLType) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      super.setObject(paramInt, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), translateAndCheckSqlType(paramSQLType));
      return;
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, SQLType paramSQLType, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      super.setObject(paramInt1, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), translateAndCheckSqlType(paramSQLType), paramInt2);
      return;
    } 
  }
}
