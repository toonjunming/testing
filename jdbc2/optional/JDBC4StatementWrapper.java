package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBC4StatementWrapper extends StatementWrapper {
  public JDBC4StatementWrapper(ConnectionWrapper paramConnectionWrapper, MysqlPooledConnection paramMysqlPooledConnection, Statement paramStatement) {
    super(paramConnectionWrapper, paramMysqlPooledConnection, paramStatement);
  }
  
  public void close() throws SQLException {
    try {
      super.close();
      return;
    } finally {
      this.unwrappedInterfaces = null;
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
      if (!str.equals("java.sql.Statement"))
        if (str.equals("java.sql.Wrapper")) {
          bool = bool1;
        } else {
          bool = false;
        }  
    } 
    return bool;
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
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4StatementWrapper}} */
    try {
      if ("java.sql.Statement".equals(paramClass.getName()) || "java.sql.Wrapper.class".equals(paramClass.getName())) {
        T t = paramClass.cast(this);
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4StatementWrapper}} */
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
        object1 = this.wrappedStmt.getClass().getClassLoader();
        object2 = new WrapperBase.ConnectionErrorFiringInvocationHandler();
        super(this, this.wrappedStmt);
        object1 = Proxy.newProxyInstance((ClassLoader)object1, new Class[] { paramClass }, (InvocationHandler)object2);
        this.unwrappedInterfaces.put(paramClass, object1);
      } 
      object1 = paramClass.cast(object1);
      /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4StatementWrapper}} */
      return (T)object1;
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/JDBC4StatementWrapper}} */
    throw paramClass;
  }
}
