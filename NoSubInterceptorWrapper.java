package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public class NoSubInterceptorWrapper implements StatementInterceptorV2 {
  private final StatementInterceptorV2 underlyingInterceptor;
  
  public NoSubInterceptorWrapper(StatementInterceptorV2 paramStatementInterceptorV2) {
    if (paramStatementInterceptorV2 != null) {
      this.underlyingInterceptor = paramStatementInterceptorV2;
      return;
    } 
    throw new RuntimeException("Interceptor to be wrapped can not be NULL");
  }
  
  public void destroy() {
    this.underlyingInterceptor.destroy();
  }
  
  public boolean executeTopLevelOnly() {
    return this.underlyingInterceptor.executeTopLevelOnly();
  }
  
  public StatementInterceptorV2 getUnderlyingInterceptor() {
    return this.underlyingInterceptor;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.underlyingInterceptor.init(paramConnection, paramProperties);
  }
  
  public ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, SQLException paramSQLException) throws SQLException {
    this.underlyingInterceptor.postProcess(paramString, paramStatement, paramResultSetInternalMethods, paramConnection, paramInt, paramBoolean1, paramBoolean2, paramSQLException);
    return null;
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    this.underlyingInterceptor.preProcess(paramString, paramStatement, paramConnection);
    return null;
  }
}
