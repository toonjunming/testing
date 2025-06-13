package com.mysql.jdbc.interceptors;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import com.mysql.jdbc.Util;
import com.mysql.jdbc.log.Log;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServerStatusDiffInterceptor implements StatementInterceptor {
  private Map<String, String> postExecuteValues = new HashMap<String, String>();
  
  private Map<String, String> preExecuteValues = new HashMap<String, String>();
  
  private void populateMapWithSessionStatusValues(Connection paramConnection, Map<String, String> paramMap) throws SQLException {
    ResultSet resultSet1;
    Statement statement;
    ResultSet resultSet2 = null;
    Connection connection = null;
    try {
      ResultSet resultSet;
      paramMap.clear();
      statement = paramConnection.createStatement();
    } finally {
      paramConnection = null;
      statement = null;
    } 
    if (resultSet1 != null)
      resultSet1.close(); 
    if (statement != null)
      statement.close(); 
    throw paramConnection;
  }
  
  public void destroy() {}
  
  public boolean executeTopLevelOnly() {
    return true;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {}
  
  public ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection) throws SQLException {
    if (paramConnection.versionMeetsMinimum(5, 0, 2)) {
      populateMapWithSessionStatusValues(paramConnection, this.postExecuteValues);
      Log log = paramConnection.getLog();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Server status change for statement:\n");
      stringBuilder.append(Util.calculateDifferences(this.preExecuteValues, this.postExecuteValues));
      log.logInfo(stringBuilder.toString());
    } 
    return null;
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    if (paramConnection.versionMeetsMinimum(5, 0, 2))
      populateMapWithSessionStatusValues(paramConnection, this.preExecuteValues); 
    return null;
  }
}
