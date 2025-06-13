package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBC4PreparedStatementWrapper extends PreparedStatementWrapper {
  public JDBC4PreparedStatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, PreparedStatement paramPreparedStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramPreparedStatement);
  }
  
  public void close() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield pooledConnection : Lcom/mysql/jdbc/jdbc2/optional/MysqlPooledConnection;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: invokespecial close : ()V
    //   18: new javax/sql/StatementEvent
    //   21: astore_2
    //   22: aload_2
    //   23: aload_1
    //   24: aload_0
    //   25: invokespecial <init> : (Ljavax/sql/PooledConnection;Ljava/sql/PreparedStatement;)V
    //   28: aload_1
    //   29: instanceof com/mysql/jdbc/jdbc2/optional/JDBC4MysqlPooledConnection
    //   32: ifeq -> 46
    //   35: aload_1
    //   36: checkcast com/mysql/jdbc/jdbc2/optional/JDBC4MysqlPooledConnection
    //   39: aload_2
    //   40: invokevirtual fireStatementEvent : (Ljavax/sql/StatementEvent;)V
    //   43: goto -> 79
    //   46: aload_1
    //   47: instanceof com/mysql/jdbc/jdbc2/optional/JDBC4MysqlXAConnection
    //   50: ifeq -> 64
    //   53: aload_1
    //   54: checkcast com/mysql/jdbc/jdbc2/optional/JDBC4MysqlXAConnection
    //   57: aload_2
    //   58: invokevirtual fireStatementEvent : (Ljavax/sql/StatementEvent;)V
    //   61: goto -> 79
    //   64: aload_1
    //   65: instanceof com/mysql/jdbc/jdbc2/optional/JDBC4SuspendableXAConnection
    //   68: ifeq -> 79
    //   71: aload_1
    //   72: checkcast com/mysql/jdbc/jdbc2/optional/JDBC4SuspendableXAConnection
    //   75: aload_2
    //   76: invokevirtual fireStatementEvent : (Ljavax/sql/StatementEvent;)V
    //   79: aload_0
    //   80: aconst_null
    //   81: putfield unwrappedInterfaces : Ljava/util/Map;
    //   84: aload_0
    //   85: monitorexit
    //   86: return
    //   87: astore_1
    //   88: aload_0
    //   89: aconst_null
    //   90: putfield unwrappedInterfaces : Ljava/util/Map;
    //   93: aload_1
    //   94: athrow
    //   95: astore_2
    //   96: new javax/sql/StatementEvent
    //   99: astore_3
    //   100: aload_3
    //   101: aload_1
    //   102: aload_0
    //   103: invokespecial <init> : (Ljavax/sql/PooledConnection;Ljava/sql/PreparedStatement;)V
    //   106: aload_1
    //   107: instanceof com/mysql/jdbc/jdbc2/optional/JDBC4MysqlPooledConnection
    //   110: ifne -> 149
    //   113: aload_1
    //   114: instanceof com/mysql/jdbc/jdbc2/optional/JDBC4MysqlXAConnection
    //   117: ifne -> 138
    //   120: aload_1
    //   121: instanceof com/mysql/jdbc/jdbc2/optional/JDBC4SuspendableXAConnection
    //   124: ifeq -> 157
    //   127: aload_1
    //   128: checkcast com/mysql/jdbc/jdbc2/optional/JDBC4SuspendableXAConnection
    //   131: aload_3
    //   132: invokevirtual fireStatementEvent : (Ljavax/sql/StatementEvent;)V
    //   135: goto -> 157
    //   138: aload_1
    //   139: checkcast com/mysql/jdbc/jdbc2/optional/JDBC4MysqlXAConnection
    //   142: aload_3
    //   143: invokevirtual fireStatementEvent : (Ljavax/sql/StatementEvent;)V
    //   146: goto -> 157
    //   149: aload_1
    //   150: checkcast com/mysql/jdbc/jdbc2/optional/JDBC4MysqlPooledConnection
    //   153: aload_3
    //   154: invokevirtual fireStatementEvent : (Ljavax/sql/StatementEvent;)V
    //   157: aload_0
    //   158: aconst_null
    //   159: putfield unwrappedInterfaces : Ljava/util/Map;
    //   162: aload_2
    //   163: athrow
    //   164: astore_1
    //   165: aload_0
    //   166: aconst_null
    //   167: putfield unwrappedInterfaces : Ljava/util/Map;
    //   170: aload_1
    //   171: athrow
    //   172: astore_1
    //   173: aload_0
    //   174: monitorexit
    //   175: aload_1
    //   176: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	172	finally
    //   14	18	95	finally
    //   18	43	87	finally
    //   46	61	87	finally
    //   64	79	87	finally
    //   79	84	172	finally
    //   88	95	172	finally
    //   96	135	164	finally
    //   138	146	164	finally
    //   149	157	164	finally
    //   157	164	172	finally
    //   165	172	172	finally
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
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4PreparedStatementWrapper}} */
    try {
      if ("java.sql.Statement".equals(paramClass.getName()) || "java.sql.PreparedStatement".equals(paramClass.getName()) || "java.sql.Wrapper.class".equals(paramClass.getName())) {
        T t = paramClass.cast(this);
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4PreparedStatementWrapper}} */
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
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4PreparedStatementWrapper}} */
      return (T)object1;
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4PreparedStatementWrapper}} */
    throw paramClass;
  }
}
