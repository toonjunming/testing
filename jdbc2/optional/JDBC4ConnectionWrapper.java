package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.SQLError;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JDBC4ConnectionWrapper extends ConnectionWrapper {
  public JDBC4ConnectionWrapper(MysqlPooledConnection paramMysqlPooledConnection, Connection paramConnection, boolean paramBoolean) throws SQLException {
    super(paramMysqlPooledConnection, paramConnection, paramBoolean);
  }
  
  public void close() throws SQLException {
    try {
      super.close();
      return;
    } finally {
      this.unwrappedInterfaces = null;
    } 
  }
  
  public Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException {
    checkClosed();
    try {
      return this.mc.createArrayOf(paramString, paramArrayOfObject);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Blob createBlob() throws SQLException {
    checkClosed();
    try {
      return this.mc.createBlob();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Clob createClob() throws SQLException {
    checkClosed();
    try {
      return this.mc.createClob();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public NClob createNClob() throws SQLException {
    checkClosed();
    try {
      return this.mc.createNClob();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public SQLXML createSQLXML() throws SQLException {
    checkClosed();
    try {
      return this.mc.createSQLXML();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException {
    checkClosed();
    try {
      return this.mc.createStruct(paramString, paramArrayOfObject);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public String getClientInfo(String paramString) throws SQLException {
    checkClosed();
    try {
      return this.mc.getClientInfo(paramString);
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public Properties getClientInfo() throws SQLException {
    checkClosed();
    try {
      return this.mc.getClientInfo();
    } catch (SQLException sQLException) {
      checkAndFireConnectionError(sQLException);
      return null;
    } 
  }
  
  public boolean isValid(int paramInt) throws SQLException {
    Exception exception;
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
    try {
      boolean bool = this.mc.isValid(paramInt);
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
      return bool;
    } catch (SQLException null) {
      checkAndFireConnectionError((SQLException)exception);
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
      return false;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
    throw exception;
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    checkClosed();
    boolean bool = paramClass.isInstance(this);
    boolean bool1 = true;
    if (bool)
      return true; 
    bool = bool1;
    if (!paramClass.getName().equals("com.mysql.jdbc.Connection"))
      if (paramClass.getName().equals("com.mysql.jdbc.ConnectionProperties")) {
        bool = bool1;
      } else {
        bool = false;
      }  
    return bool;
  }
  
  public void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException {
    try {
      checkClosed();
      this.mc.setClientInfo(paramString1, paramString2);
    } catch (SQLException sQLException) {
      try {
        checkAndFireConnectionError(sQLException);
        return;
      } catch (SQLException sQLException1) {
        sQLException = new SQLClientInfoException();
        sQLException.initCause(sQLException1);
        throw sQLException;
      } 
    } 
  }
  
  public void setClientInfo(Properties paramProperties) throws SQLClientInfoException {
    try {
      checkClosed();
      this.mc.setClientInfo(paramProperties);
    } catch (SQLException sQLException) {
      try {
        checkAndFireConnectionError(sQLException);
        return;
      } catch (SQLException sQLException1) {
        SQLClientInfoException sQLClientInfoException = new SQLClientInfoException();
        sQLClientInfoException.initCause(sQLException1);
        throw sQLClientInfoException;
      } 
    } 
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
    try {
      if ("java.sql.Connection".equals(paramClass.getName()) || "java.sql.Wrapper.class".equals(paramClass.getName())) {
        T t = paramClass.cast(this);
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
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
        object1 = this.mc.getClass().getClassLoader();
        object2 = new WrapperBase.ConnectionErrorFiringInvocationHandler();
        super(this, this.mc);
        object1 = Proxy.newProxyInstance((ClassLoader)object1, new Class[] { paramClass }, (InvocationHandler)object2);
        this.unwrappedInterfaces.put(paramClass, object1);
      } 
      object1 = paramClass.cast(object1);
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
      return (T)object1;
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper}} */
    throw paramClass;
  }
}
