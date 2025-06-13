package com.mysql.jdbc.interceptors;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Pattern;

public class ResultSetScannerInterceptor implements StatementInterceptor {
  public Pattern regexP;
  
  public void destroy() {}
  
  public boolean executeTopLevelOnly() {
    return false;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    String str = paramProperties.getProperty("resultSetScannerRegex");
    if (str != null && str.length() != 0)
      try {
        return;
      } finally {
        str = null;
        SQLException sQLException = new SQLException("Can't use configured regex due to underlying exception.");
        sQLException.initCause((Throwable)str);
      }  
    throw new SQLException("resultSetScannerRegex must be configured, and must be > 0 characters");
  }
  
  public ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, final ResultSetInternalMethods finalResultSet, Connection paramConnection) throws SQLException {
    ClassLoader classLoader = finalResultSet.getClass().getClassLoader();
    InvocationHandler invocationHandler = new InvocationHandler() {
        public final ResultSetScannerInterceptor this$0;
        
        public final ResultSetInternalMethods val$finalResultSet;
        
        public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
          if ("equals".equals(param1Method.getName()))
            return Boolean.valueOf(param1ArrayOfObject[0].equals(this)); 
          param1Object = param1Method.invoke(finalResultSet, param1ArrayOfObject);
          String str = param1Method.getName();
          if (((param1Object == null || !(param1Object instanceof String)) && !"getString".equals(str) && !"getObject".equals(str) && !"getObjectStoredProc".equals(str)) || !ResultSetScannerInterceptor.this.regexP.matcher(param1Object.toString()).matches())
            return param1Object; 
          throw new SQLException("value disallowed by filter");
        }
      };
    return (ResultSetInternalMethods)Proxy.newProxyInstance(classLoader, new Class[] { ResultSetInternalMethods.class }, invocationHandler);
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    return null;
  }
}
