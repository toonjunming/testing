package com.mysql.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Properties;

public class ReflectiveStatementInterceptorAdapter implements StatementInterceptorV2 {
  private final StatementInterceptor toProxy;
  
  public final Method v2PostProcessMethod;
  
  public ReflectiveStatementInterceptorAdapter(StatementInterceptor paramStatementInterceptor) {
    this.toProxy = paramStatementInterceptor;
    this.v2PostProcessMethod = getV2PostProcessMethod(paramStatementInterceptor.getClass());
  }
  
  public static final Method getV2PostProcessMethod(Class<?> paramClass) {
    try {
      Class<int> clazz1 = int.class;
      Class<boolean> clazz = boolean.class;
      return paramClass.getMethod("postProcess", new Class[] { String.class, Statement.class, ResultSetInternalMethods.class, Connection.class, clazz1, clazz, clazz, SQLException.class });
    } catch (SecurityException|NoSuchMethodException securityException) {
      return null;
    } 
  }
  
  public void destroy() {
    this.toProxy.destroy();
  }
  
  public boolean executeTopLevelOnly() {
    return this.toProxy.executeTopLevelOnly();
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.toProxy.init(paramConnection, paramProperties);
  }
  
  public ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, SQLException paramSQLException) throws SQLException {
    try {
      Boolean bool1;
      Boolean bool2;
      Method method = this.v2PostProcessMethod;
      StatementInterceptor statementInterceptor = this.toProxy;
      if (paramBoolean1) {
        bool1 = Boolean.TRUE;
      } else {
        bool1 = Boolean.FALSE;
      } 
      if (paramBoolean2) {
        bool2 = Boolean.TRUE;
      } else {
        bool2 = Boolean.FALSE;
      } 
      return (ResultSetInternalMethods)method.invoke(statementInterceptor, new Object[] { paramString, paramStatement, paramResultSetInternalMethods, paramConnection, Integer.valueOf(paramInt), bool1, bool2, paramSQLException });
    } catch (IllegalArgumentException illegalArgumentException) {
      SQLException sQLException = new SQLException("Unable to reflectively invoke interceptor");
      sQLException.initCause(illegalArgumentException);
      throw sQLException;
    } catch (IllegalAccessException illegalAccessException) {
      SQLException sQLException = new SQLException("Unable to reflectively invoke interceptor");
      sQLException.initCause(illegalAccessException);
      throw sQLException;
    } catch (InvocationTargetException invocationTargetException) {
      SQLException sQLException = new SQLException("Unable to reflectively invoke interceptor");
      sQLException.initCause(invocationTargetException);
      throw sQLException;
    } 
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    return this.toProxy.preProcess(paramString, paramStatement, paramConnection);
  }
}
