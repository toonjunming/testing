package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.SQLType;

public class JDBC42CallableStatement extends JDBC4CallableStatement {
  public JDBC42CallableStatement(MySQLConnection paramMySQLConnection, CallableStatement.CallableStatementParamInfo paramCallableStatementParamInfo) throws SQLException {
    super(paramMySQLConnection, paramCallableStatementParamInfo);
  }
  
  public JDBC42CallableStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2, paramBoolean);
  }
  
  private int checkSqlType(int paramInt) throws SQLException {
    return JDBC42Helper.checkSqlType(paramInt, getExceptionInterceptor());
  }
  
  private int translateAndCheckSqlType(SQLType paramSQLType) throws SQLException {
    return JDBC42Helper.translateAndCheckSqlType(paramSQLType, getExceptionInterceptor());
  }
  
  public void registerOutParameter(int paramInt, SQLType paramSQLType) throws SQLException {
    registerOutParameter(paramInt, translateAndCheckSqlType(paramSQLType));
  }
  
  public void registerOutParameter(int paramInt1, SQLType paramSQLType, int paramInt2) throws SQLException {
    registerOutParameter(paramInt1, translateAndCheckSqlType(paramSQLType), paramInt2);
  }
  
  public void registerOutParameter(int paramInt, SQLType paramSQLType, String paramString) throws SQLException {
    registerOutParameter(paramInt, translateAndCheckSqlType(paramSQLType), paramString);
  }
  
  public void registerOutParameter(String paramString, SQLType paramSQLType) throws SQLException {
    registerOutParameter(paramString, translateAndCheckSqlType(paramSQLType));
  }
  
  public void registerOutParameter(String paramString, SQLType paramSQLType, int paramInt) throws SQLException {
    registerOutParameter(paramString, translateAndCheckSqlType(paramSQLType), paramInt);
  }
  
  public void registerOutParameter(String paramString1, SQLType paramSQLType, String paramString2) throws SQLException {
    registerOutParameter(paramString1, translateAndCheckSqlType(paramSQLType), paramString2);
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
  
  public void setObject(String paramString, Object paramObject, SQLType paramSQLType) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setObject(paramString, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), translateAndCheckSqlType(paramSQLType));
      return;
    } 
  }
  
  public void setObject(String paramString, Object paramObject, SQLType paramSQLType, int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      setObject(paramString, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), translateAndCheckSqlType(paramSQLType), paramInt);
      return;
    } 
  }
}
