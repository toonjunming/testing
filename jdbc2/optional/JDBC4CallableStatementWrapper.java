package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBC4CallableStatementWrapper extends CallableStatementWrapper {
  public JDBC4CallableStatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, CallableStatement paramCallableStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramCallableStatement);
  }
  
  public void close() throws SQLException {
    try {
      super.close();
      return;
    } finally {
      this.unwrappedInterfaces = null;
    } 
  }
  
  public Reader getCharacterStream(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getCharacterStream(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Reader getCharacterStream(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getCharacterStream(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Reader getNCharacterStream(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getNCharacterStream(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Reader getNCharacterStream(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getNCharacterStream(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public NClob getNClob(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getNClob(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public NClob getNClob(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getNClob(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public String getNString(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getNString(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public String getNString(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getNString(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public RowId getRowId(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getRowId(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public RowId getRowId(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getRowId(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public SQLXML getSQLXML(int paramInt) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getSQLXML(paramInt); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public SQLXML getSQLXML(String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return ((CallableStatement)statement).getSQLXML(paramString); 
      throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public boolean isClosed() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.isClosed(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean isPoolable() throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null)
        return statement.isPoolable(); 
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return false;
    } 
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    boolean bool = paramClass.isInstance(this);
    boolean bool1 = true;
    if (bool)
      return true; 
    String str = paramClass.getName();
    bool = bool1;
    if (!str.equals("com.mysql.jdbc.Statement")) {
      bool = bool1;
      if (!str.equals("java.sql.Statement")) {
        bool = bool1;
        if (!str.equals("java.sql.PreparedStatement"))
          if (str.equals("java.sql.Wrapper")) {
            bool = bool1;
          } else {
            bool = false;
          }  
      } 
    } 
    return bool;
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setAsciiStream(paramInt, paramInputStream);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setAsciiStream(paramInt, paramInputStream, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setAsciiStream(paramString, paramInputStream);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setAsciiStream(paramString, paramInputStream, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBinaryStream(paramInt, paramInputStream);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBinaryStream(paramInt, paramInputStream, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBinaryStream(paramString, paramInputStream);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBinaryStream(paramString, paramInputStream, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBlob(paramInt, paramInputStream);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setBlob(paramInt, paramInputStream, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBlob(String paramString, InputStream paramInputStream) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBlob(paramString, paramInputStream);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBlob(paramString, paramInputStream, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setBlob(String paramString, Blob paramBlob) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setBlob(paramString, paramBlob);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setCharacterStream(paramInt, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setCharacterStream(paramInt, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterStream(String paramString, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setCharacterStream(paramString, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setCharacterStream(paramString, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setClob(int paramInt, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setClob(paramInt, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setClob(paramInt, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setClob(String paramString, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setClob(paramString, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setClob(paramString, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setClob(String paramString, Clob paramClob) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setClob(paramString, paramClob);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNCharacterStream(paramInt, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNCharacterStream(paramInt, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNCharacterStream(String paramString, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNCharacterStream(paramString, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNCharacterStream(paramString, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNClob(int paramInt, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNClob(paramInt, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNClob(paramInt, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNClob(paramInt, paramNClob);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNClob(String paramString, Reader paramReader) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNClob(paramString, paramReader);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNClob(paramString, paramReader, paramLong);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNClob(String paramString, NClob paramNClob) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNClob(paramString, paramNClob);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNString(int paramInt, String paramString) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setNString(paramInt, paramString);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setNString(String paramString1, String paramString2) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setNString(paramString1, paramString2);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setPoolable(boolean paramBoolean) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        statement.setPoolable(paramBoolean);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setRowId(paramInt, paramRowId);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setRowId(String paramString, RowId paramRowId) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setRowId(paramString, paramRowId);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((PreparedStatement)statement).setSQLXML(paramInt, paramSQLXML);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public void setSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
    try {
      Statement statement = this.wrappedStmt;
      if (statement != null) {
        ((CallableStatement)statement).setSQLXML(paramString, paramSQLXML);
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      } 
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
    } 
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4CallableStatementWrapper}} */
    try {
      if ("java.sql.Statement".equals(paramClass.getName()) || "java.sql.PreparedStatement".equals(paramClass.getName()) || "java.sql.Wrapper.class".equals(paramClass.getName())) {
        T t = paramClass.cast(this);
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4CallableStatementWrapper}} */
        return t;
      } 
      if (this.unwrappedInterfaces == null) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        this();
        this.unwrappedInterfaces = (Map)hashMap;
      } 
      Object object2 = this.unwrappedInterfaces.get(paramClass);
      Object object1 = object2;
      if (object2 == null) {
        object1 = object2;
        if (object2 == null) {
          object2 = this.wrappedStmt.getClass().getClassLoader();
          object1 = new WrapperBase.ConnectionErrorFiringInvocationHandler();
          super(this, this.wrappedStmt);
          object1 = Proxy.newProxyInstance((ClassLoader)object2, new Class[] { paramClass }, (InvocationHandler)object1);
          this.unwrappedInterfaces.put(paramClass, object1);
        } 
        this.unwrappedInterfaces.put(paramClass, object1);
      } 
      object1 = paramClass.cast(object1);
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4CallableStatementWrapper}} */
      return (T)object1;
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4CallableStatementWrapper}} */
    throw paramClass;
  }
}
