package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public class LoadBalancedAutoCommitInterceptor implements StatementInterceptorV2 {
  private ConnectionImpl conn;
  
  private boolean countStatements = false;
  
  private int matchingAfterStatementCount = 0;
  
  private String matchingAfterStatementRegex;
  
  private int matchingAfterStatementThreshold = 0;
  
  private LoadBalancedConnectionProxy proxy = null;
  
  public void destroy() {}
  
  public boolean executeTopLevelOnly() {
    return false;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.conn = (ConnectionImpl)paramConnection;
    String str = paramProperties.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
    try {
      this.matchingAfterStatementThreshold = Integer.parseInt(str);
    } catch (NumberFormatException numberFormatException) {}
    str = paramProperties.getProperty("loadBalanceAutoCommitStatementRegex", "");
    if ("".equals(str))
      return; 
    this.matchingAfterStatementRegex = str;
  }
  
  public void pauseCounters() {
    this.countStatements = false;
  }
  
  public ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, SQLException paramSQLException) throws SQLException {
    if (this.countStatements && !StringUtils.startsWithIgnoreCase(paramString, "SET") && !StringUtils.startsWithIgnoreCase(paramString, "SHOW")) {
      if (!this.conn.getAutoCommit()) {
        this.matchingAfterStatementCount = 0;
        return paramResultSetInternalMethods;
      } 
      if (this.proxy == null && this.conn.isProxySet()) {
        MySQLConnection mySQLConnection;
        for (mySQLConnection = this.conn.getMultiHostSafeProxy(); mySQLConnection != null && !(mySQLConnection instanceof LoadBalancedMySQLConnection); mySQLConnection = mySQLConnection.getMultiHostSafeProxy());
        if (mySQLConnection != null)
          this.proxy = ((LoadBalancedMySQLConnection)mySQLConnection).getThisAsProxy(); 
      } 
      if (this.proxy == null)
        return paramResultSetInternalMethods; 
      String str = this.matchingAfterStatementRegex;
      if (str == null || paramString.matches(str))
        this.matchingAfterStatementCount++; 
      if (this.matchingAfterStatementCount >= this.matchingAfterStatementThreshold) {
        this.matchingAfterStatementCount = 0;
        try {
          this.proxy.pickNewConnection();
        } catch (SQLException sQLException) {}
      } 
    } 
    return paramResultSetInternalMethods;
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    return null;
  }
  
  public void resumeCounters() {
    this.countStatements = true;
  }
}
