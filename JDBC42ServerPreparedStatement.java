package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.SQLType;

public class JDBC42ServerPreparedStatement extends JDBC4ServerPreparedStatement {
  public JDBC42ServerPreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, int paramInt1, int paramInt2) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2, paramInt1, paramInt2);
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
