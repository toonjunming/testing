package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Util;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Map;

public abstract class WrapperBase {
  public ExceptionInterceptor exceptionInterceptor;
  
  public MysqlPooledConnection pooledConnection;
  
  public Map<Class<?>, Object> unwrappedInterfaces = null;
  
  public WrapperBase(MysqlPooledConnection paramMysqlPooledConnection) {
    this.pooledConnection = paramMysqlPooledConnection;
    this.exceptionInterceptor = paramMysqlPooledConnection.getExceptionInterceptor();
  }
  
  public void checkAndFireConnectionError(SQLException paramSQLException) throws SQLException {
    if (this.pooledConnection != null && "08S01".equals(paramSQLException.getSQLState()))
      this.pooledConnection.callConnectionEventListeners(1, paramSQLException); 
    throw paramSQLException;
  }
  
  public class ConnectionErrorFiringInvocationHandler implements InvocationHandler {
    public Object invokeOn = null;
    
    public final WrapperBase this$0;
    
    public ConnectionErrorFiringInvocationHandler(Object param1Object) {
      this.invokeOn = param1Object;
    }
    
    private Object proxyIfInterfaceIsJdbc(Object param1Object, Class<?> param1Class) {
      Class[] arrayOfClass = param1Class.getInterfaces();
      Object object = param1Object;
      if (arrayOfClass.length > 0) {
        Class<?> clazz = arrayOfClass[0];
        object = Util.getPackageName(clazz);
        return ("java.sql".equals(object) || "javax.sql".equals(object)) ? Proxy.newProxyInstance(param1Object.getClass().getClassLoader(), arrayOfClass, new ConnectionErrorFiringInvocationHandler(param1Object)) : proxyIfInterfaceIsJdbc(param1Object, clazz);
      } 
      return object;
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      if ("equals".equals(param1Method.getName()))
        return Boolean.valueOf(param1ArrayOfObject[0].equals(this)); 
      param1Object = null;
      try {
        Object object = param1Method.invoke(this.invokeOn, param1ArrayOfObject);
        param1Object = object;
        if (object != null) {
          param1Object = object;
          object = proxyIfInterfaceIsJdbc(object, object.getClass());
          param1Object = object;
        } 
      } catch (InvocationTargetException invocationTargetException) {
        if (invocationTargetException.getTargetException() instanceof SQLException) {
          WrapperBase.this.checkAndFireConnectionError((SQLException)invocationTargetException.getTargetException());
          return param1Object;
        } 
      } 
      return param1Object;
    }
  }
}
