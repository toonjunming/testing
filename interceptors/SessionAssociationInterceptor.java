package com.mysql.jdbc.interceptors;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class SessionAssociationInterceptor implements StatementInterceptor {
  public static final ThreadLocal<String> sessionLocal = new ThreadLocal<String>();
  
  public String currentSessionKey;
  
  public static final String getSessionKey() {
    return sessionLocal.get();
  }
  
  public static final void resetSessionKey() {
    sessionLocal.set(null);
  }
  
  public static final void setSessionKey(String paramString) {
    sessionLocal.set(paramString);
  }
  
  public void destroy() {}
  
  public boolean executeTopLevelOnly() {
    return true;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {}
  
  public ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection) throws SQLException {
    return null;
  }
  
  public ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException {
    paramString = getSessionKey();
    if (paramString != null && !paramString.equals(this.currentSessionKey)) {
      PreparedStatement preparedStatement = paramConnection.clientPrepareStatement("SET @mysql_proxy_session=?");
      try {
        preparedStatement.setString(1, paramString);
        preparedStatement.execute();
        preparedStatement.close();
      } finally {
        preparedStatement.close();
      } 
    } 
    return null;
  }
}
