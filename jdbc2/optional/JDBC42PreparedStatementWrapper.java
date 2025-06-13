package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;

public class JDBC42PreparedStatementWrapper extends JDBC4PreparedStatementWrapper {
  public JDBC42PreparedStatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, PreparedStatement paramPreparedStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramPreparedStatement);
  }
  
  public void setObject(int paramInt, Object paramObject, SQLType paramSQLType) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setObject(paramInt, paramObject, paramSQLType);
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
        ((PreparedStatement)statement).setObject(paramInt1, paramObject, paramSQLType, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
}
