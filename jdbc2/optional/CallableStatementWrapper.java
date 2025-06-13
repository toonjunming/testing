package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class CallableStatementWrapper extends PreparedStatementWrapper implements CallableStatement {
  private static final Constructor<?> JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR;
  
  static {
    if (Util.isJdbc4()) {
      try {
        String str;
        if (Util.isJdbc42()) {
          str = "com.mysql.jdbc.jdbc2.optional.JDBC42CallableStatementWrapper";
        } else {
          str = "com.mysql.jdbc.jdbc2.optional.JDBC4CallableStatementWrapper";
        } 
        JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR = Class.forName(str).getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, CallableStatement.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR = null;
    } 
  }
  
  public CallableStatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, CallableStatement paramCallableStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramCallableStatement);
  }
  
  public static CallableStatementWrapper getInstance(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, CallableStatement paramCallableStatement) throws SQLException {
    if (!Util.isJdbc4())
      return new CallableStatementWrapper(paramConnectionWrapper, paramMysqlPooledConnection, paramCallableStatement); 
    Constructor<?> constructor = JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramMysqlPooledConnection.getExceptionInterceptor();
    return (CallableStatementWrapper)Util.handleNewInstance(constructor, new Object[] { paramConnectionWrapper, paramMysqlPooledConnection, paramCallableStatement }, exceptionInterceptor);
  }
  
  public Array getArray(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getArray(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Array getArray(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getArray(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public BigDecimal getBigDecimal(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBigDecimal(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  @Deprecated
  public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBigDecimal(paramInt1, paramInt2); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public BigDecimal getBigDecimal(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBigDecimal(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Blob getBlob(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBlob(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Blob getBlob(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBlob(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public boolean getBoolean(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBoolean(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean getBoolean(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBoolean(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public byte getByte(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getByte(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public byte getByte(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getByte(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public byte[] getBytes(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBytes(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public byte[] getBytes(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getBytes(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Clob getClob(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getClob(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Clob getClob(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getClob(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Date getDate(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getDate(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getDate(paramInt, paramCalendar); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Date getDate(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getDate(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getDate(paramString, paramCalendar); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public double getDouble(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getDouble(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0.0D;
    } 
  }
  
  public double getDouble(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getDouble(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0.0D;
    } 
  }
  
  public float getFloat(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getFloat(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0.0F;
    } 
  }
  
  public float getFloat(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getFloat(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0.0F;
    } 
  }
  
  public int getInt(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getInt(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public int getInt(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getInt(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public long getLong(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getLong(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0L;
    } 
  }
  
  public long getLong(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getLong(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0L;
    } 
  }
  
  public Object getObject(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getObject(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getObject(paramInt, paramMap); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Object getObject(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getObject(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getObject(paramString, paramMap); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Ref getRef(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getRef(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Ref getRef(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getRef(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public short getShort(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getShort(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public short getShort(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getShort(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return 0;
    } 
  }
  
  public String getString(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getString(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public String getString(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getString(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Time getTime(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTime(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTime(paramInt, paramCalendar); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Time getTime(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTime(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTime(paramString, paramCalendar); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Timestamp getTimestamp(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTimestamp(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTimestamp(paramInt, paramCalendar); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Timestamp getTimestamp(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTimestamp(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getTimestamp(paramString, paramCalendar); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public URL getURL(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getURL(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public URL getURL(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getURL(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public void registerOutParameter(int paramInt1, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramInt1, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramInt1, paramInt2, paramInt3);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(int paramInt1, int paramInt2, String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramInt1, paramInt2, paramString);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(String paramString, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramString, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramString, paramInt1, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).registerOutParameter(paramString1, paramInt, paramString2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setAsciiStream(paramString, paramInputStream, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBigDecimal(paramString, paramBigDecimal);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBinaryStream(paramString, paramInputStream, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBoolean(paramString, paramBoolean);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setByte(String paramString, byte paramByte) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setByte(paramString, paramByte);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBytes(paramString, paramArrayOfbyte);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setCharacterStream(paramString, paramReader, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setDate(String paramString, Date paramDate) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setDate(paramString, paramDate);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setDate(String paramString, Date paramDate, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setDate(paramString, paramDate, paramCalendar);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setDouble(String paramString, double paramDouble) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setDouble(paramString, paramDouble);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setFloat(String paramString, float paramFloat) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setFloat(paramString, paramFloat);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setInt(String paramString, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setInt(paramString, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setLong(String paramString, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setLong(paramString, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNull(String paramString, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNull(paramString, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNull(String paramString1, int paramInt, String paramString2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNull(paramString1, paramInt, paramString2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(String paramString, Object paramObject) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramString, paramObject);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(String paramString, Object paramObject, int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramString, paramObject, paramInt);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setObject(paramString, paramObject, paramInt1, paramInt2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setShort(String paramString, short paramShort) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setShort(paramString, paramShort);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setString(String paramString1, String paramString2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setString(paramString1, paramString2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTime(String paramString, Time paramTime) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setTime(paramString, paramTime);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setTime(paramString, paramTime, paramCalendar);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setTimestamp(paramString, paramTimestamp);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setTimestamp(paramString, paramTimestamp, paramCalendar);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setURL(String paramString, URL paramURL) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setURL(paramString, paramURL);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public boolean wasNull() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).wasNull(); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
}
