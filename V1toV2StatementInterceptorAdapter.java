package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public class V1toV2StatementInterceptorAdapter implements StatementInterceptorV2 {
  private final StatementInterceptor toProxy;
  
  public V1toV2StatementInterceptorAdapter(StatementInterceptor paramStatementInterceptor) {
    this.toProxy = paramStatementInterceptor;
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
    return this.toProxy.postProcess(paramString, paramStatement, paramResultSetInternalMethods, paramConnection);
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    return this.toProxy.preProcess(paramString, paramStatement, paramConnection);
  }
}
