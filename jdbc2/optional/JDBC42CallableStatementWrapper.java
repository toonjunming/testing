package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;

public class JDBC42CallableStatementWrapper extends JDBC4CallableStatementWrapper {
  public JDBC42CallableStatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, CallableStatement paramCallableStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramCallableStatement);
  }
  
  public void registerOutParameter(int paramInt, SQLType paramSQLType) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramInt, paramSQLType);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(int paramInt1, SQLType paramSQLType, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramInt1, paramSQLType, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(int paramInt, SQLType paramSQLType, String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramInt, paramSQLType, paramString);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(String paramString, SQLType paramSQLType) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramString, paramSQLType);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(String paramString, SQLType paramSQLType, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramString, paramSQLType, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(String paramString1, SQLType paramSQLType, String paramString2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramString1, paramSQLType, paramString2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(int paramInt, Object paramObject, SQLType paramSQLType) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramInt, paramObject, paramSQLType);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, SQLType paramSQLType, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramInt1, paramObject, paramSQLType, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(String paramString, Object paramObject, SQLType paramSQLType) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramString, paramObject, paramSQLType);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(String paramString, Object paramObject, SQLType paramSQLType, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramString, paramObject, paramSQLType, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
}
