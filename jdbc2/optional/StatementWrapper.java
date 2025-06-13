package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementImpl;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class StatementWrapper extends WrapperBase implements Statement {
  private static final Constructor<?> JDBC_4_STATEMENT_WRAPPER_CTOR;
  
  public ConnectionWrapper wrappedConn;
  
  public Statement wrappedStmt;
  
  static {
    if (Util.isJdbc4()) {
      try {
        JDBC_4_STATEMENT_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4StatementWrapper").getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, Statement.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_STATEMENT_WRAPPER_CTOR = null;
    } 
  }
  
  public StatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, Statement paramStatement) {
    super(paramMysqlPooledConnection);
    this.wrappedStmt = paramStatement;
    this.wrappedConn = paramConnectionWrapper;
  }
  
  public static StatementWrapper getInstance(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, Statement paramStatement) throws SQLException {
    if (!Util.isJdbc4())
      return new StatementWrapper(paramConnectionWrapper, paramMysqlPooledConnection, paramStatement); 
    Constructor<?> constructor = JDBC_4_STATEMENT_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMysqlPooledConnection.getExceptionInterceptor();
    return (StatementWrapper)Util.handleNewInstance(constructor, new Object[] { paramConnectionWrapper, paramMysqlPooledConnection, paramStatement }, exceptionInterceptor);
  }
  
  public void addBatch(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        statement.addBatch(paramString); 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void cancel() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        statement.cancel(); 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void clearBatch() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        statement.clearBatch(); 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void clearWarnings() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        statement.clearWarnings(); 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void close() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        statement.close(); 
      this.wrappedStmt = null;
      this.pooledConnection = null;
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      this.wrappedStmt = null;
      this.pooledConnection = null;
    } finally {
      Exception exception;
    } 
  }
  
  public void enableStreamingResults() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((Statement)statement).enableStreamingResults();
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public boolean execute(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.execute(paramString); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean execute(String paramString, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.execute(paramString, paramInt); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.execute(paramString, paramArrayOfint); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.execute(paramString, paramArrayOfString); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public int[] executeBatch() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.executeBatch(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public long[] executeLargeBatch() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).executeLargeBatch(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public long executeLargeUpdate(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).executeLargeUpdate(paramString); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1L;
    } 
  }
  
  public long executeLargeUpdate(String paramString, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).executeLargeUpdate(paramString, paramInt); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1L;
    } 
  }
  
  public long executeLargeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).executeLargeUpdate(paramString, paramArrayOfint); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1L;
    } 
  }
  
  public long executeLargeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).executeLargeUpdate(paramString, paramArrayOfString); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1L;
    } 
  }
  
  public ResultSet executeQuery(String paramString) throws SQLException {
    ResultSet resultSet1;
    ResultSet resultSet3 = null;
    ResultSet resultSet2 = resultSet3;
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        resultSet2 = resultSet3;
        resultSet1 = statement.executeQuery(paramString);
        resultSet2 = resultSet1;
        ((ResultSetInternalMethods)resultSet1).setWrapperStatement(this);
      } else {
        resultSet2 = resultSet3;
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      resultSet1 = resultSet2;
    } 
    return resultSet1;
  }
  
  public int executeUpdate(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.executeUpdate(paramString); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1;
    } 
  }
  
  public int executeUpdate(String paramString, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.executeUpdate(paramString, paramInt); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1;
    } 
  }
  
  public int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.executeUpdate(paramString, paramArrayOfint); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1;
    } 
  }
  
  public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.executeUpdate(paramString, paramArrayOfString); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1;
    } 
  }
  
  public Connection getConnection() throws SQLException {
    try {
      if (this.wrappedStmt != null)
        return this.wrappedConn; 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public int getFetchDirection() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getFetchDirection(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 1000;
    } 
  }
  
  public int getFetchSize() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getFetchSize(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public ResultSet getGeneratedKeys() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getGeneratedKeys(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public long getLargeMaxRows() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).getLargeMaxRows(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0L;
    } 
  }
  
  public long getLargeUpdateCount() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((StatementImpl)statement).getLargeUpdateCount(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1L;
    } 
  }
  
  public int getMaxFieldSize() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getMaxFieldSize(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public int getMaxRows() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getMaxRows(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public boolean getMoreResults() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getMoreResults(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean getMoreResults(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getMoreResults(paramInt); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public int getQueryTimeout() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getQueryTimeout(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public ResultSet getResultSet() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet != null)
          ((ResultSetInternalMethods)resultSet).setWrapperStatement(this); 
        return resultSet;
      } 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public int getResultSetConcurrency() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getResultSetConcurrency(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public int getResultSetHoldability() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getResultSetHoldability(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 1;
    } 
  }
  
  public int getResultSetType() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getResultSetType(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 1003;
    } 
  }
  
  public int getUpdateCount() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getUpdateCount(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1;
    } 
  }
  
  public SQLWarning getWarnings() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.getWarnings(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public void setCursorName(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setCursorName(paramString);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setEscapeProcessing(paramBoolean);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setFetchDirection(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setFetchDirection(paramInt);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setFetchSize(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setFetchSize(paramInt);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setLargeMaxRows(long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((StatementImpl)statement).setLargeMaxRows(paramLong);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setMaxFieldSize(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setMaxFieldSize(paramInt);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setMaxRows(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setMaxRows(paramInt);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setQueryTimeout(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setQueryTimeout(paramInt);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
}
