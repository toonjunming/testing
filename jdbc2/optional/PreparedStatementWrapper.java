package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {
  private static final Constructor<?> JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR;
  
  static {
    if (Util.isJdbc4()) {
      try {
        String str;
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.jdbc2.optional.JDBC42PreparedStatementWrapper";
        } else {
          str = "com.mysql.jdbc.jdbc2.optional.JDBC4PreparedStatementWrapper";
        } 
        JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR = Class.forName(str).getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, PreparedStatement.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR = null;
    } 
  }
  
  public PreparedStatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, PreparedStatement paramPreparedStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramPreparedStatement);
  }
  
  public static PreparedStatementWrapper getInstance(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, PreparedStatement paramPreparedStatement) throws SQLException {
    if (!Util.isJdbc4())
      return new PreparedStatementWrapper(paramConnectionWrapper, paramMysqlPooledConnection, paramPreparedStatement); 
    Constructor<?> constructor = JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMysqlPooledConnection.getExceptionInterceptor();
    return (PreparedStatementWrapper)Util.handleNewInstance(constructor, new Object[] { paramConnectionWrapper, paramMysqlPooledConnection, paramPreparedStatement }, exceptionInterceptor);
  }
  
  public void addBatch() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).addBatch();
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void clearParameters() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).clearParameters();
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public boolean execute() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((PreparedStatement)statement).execute(); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public long executeLargeUpdate() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((PreparedStatement)statement).executeLargeUpdate(); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1L;
    } 
  }
  
  public ResultSet executeQuery() throws SQLException {
    ResultSet resultSet2 = null;
    ResultSet resultSet1 = resultSet2;
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        resultSet1 = resultSet2;
        resultSet2 = ((PreparedStatement)statement).executeQuery();
        resultSet1 = resultSet2;
        ((ResultSetInternalMethods)resultSet2).setWrapperStatement(this);
        resultSet1 = resultSet2;
      } else {
        resultSet1 = resultSet2;
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
    return resultSet1;
  }
  
  public int executeUpdate() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((PreparedStatement)statement).executeUpdate(); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return -1;
    } 
  }
  
  public ResultSetMetaData getMetaData() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((PreparedStatement)statement).getMetaData(); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public ParameterMetaData getParameterMetaData() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((PreparedStatement)statement).getParameterMetaData(); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public void setArray(int paramInt, Array paramArray) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setArray(paramInt, paramArray);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setAsciiStream(paramInt1, paramInputStream, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBigDecimal(paramInt, paramBigDecimal);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBinaryStream(paramInt1, paramInputStream, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBlob(paramInt, paramBlob);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBoolean(paramInt, paramBoolean);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setByte(int paramInt, byte paramByte) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setByte(paramInt, paramByte);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBytes(paramInt, paramArrayOfbyte);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setCharacterStream(paramInt1, paramReader, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setClob(int paramInt, Clob paramClob) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setClob(paramInt, paramClob);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setDate(int paramInt, Date paramDate) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setDate(paramInt, paramDate);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setDate(paramInt, paramDate, paramCalendar);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setDouble(int paramInt, double paramDouble) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setDouble(paramInt, paramDouble);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setFloat(int paramInt, float paramFloat) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setFloat(paramInt, paramFloat);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setInt(int paramInt1, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setInt(paramInt1, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setLong(int paramInt, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setLong(paramInt, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNull(int paramInt1, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNull(paramInt1, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNull(paramInt1, paramInt2, paramString);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(int paramInt, Object paramObject) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setObject(paramInt, paramObject);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setObject(paramInt1, paramObject, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setObject(paramInt1, paramObject, paramInt2, paramInt3);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setRef(int paramInt, Ref paramRef) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setRef(paramInt, paramRef);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setShort(int paramInt, short paramShort) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setShort(paramInt, paramShort);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setString(int paramInt, String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setString(paramInt, paramString);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTime(int paramInt, Time paramTime) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setTime(paramInt, paramTime);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setTime(paramInt, paramTime, paramCalendar);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setTimestamp(paramInt, paramTimestamp);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setTimestamp(paramInt, paramTimestamp, paramCalendar);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setURL(int paramInt, URL paramURL) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setURL(paramInt, paramURL);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  @Deprecated
  public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setUnicodeStream(paramInt1, paramInputStream, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(super.toString());
    if (this.wrappedStmt != null) {
      stringBuilder.append(": ");
      try {
        stringBuilder.append(((PreparedStatement)this.wrappedStmt).asSql());
      } catch (SQLException sQLException) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("EXCEPTION: ");
        stringBuilder1.append(sQLException.toString());
        stringBuilder.append(stringBuilder1.toString());
      } 
    } 
    return stringBuilder.toString();
  }
}
