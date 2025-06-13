package com.mysql.jdbc.integration.c3p0;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.c3p0.QueryConnectionTester;
import com.mysql.jdbc.Connection;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class MysqlConnectionTester implements QueryConnectionTester {
  private static final Object[] NO_ARGS_ARRAY = new Object[0];
  
  private static final long serialVersionUID = 3256444690067896368L;
  
  private transient Method pingMethod;
  
  public MysqlConnectionTester() {
    try {
      this.pingMethod = Connection.class.getMethod("ping", null);
    } catch (Exception exception) {}
  }
  
  public int activeCheckConnection(Connection paramConnection) {
    try {
      Method method = this.pingMethod;
      if (method != null) {
        if (paramConnection instanceof Connection) {
          ((Connection)paramConnection).ping();
        } else {
          ((C3P0ProxyConnection)paramConnection).rawConnectionOperation(method, C3P0ProxyConnection.RAW_CONNECTION, NO_ARGS_ARRAY);
        } 
      } else {
        Statement statement;
        method = null;
        try {
          Statement statement1 = paramConnection.createStatement();
          statement = statement1;
          statement1.executeQuery("SELECT 1").close();
          return 0;
        } finally {
          if (statement != null)
            statement.close(); 
        } 
      } 
      return 0;
    } catch (Exception exception) {
      return -1;
    } 
  }
  
  public int activeCheckConnection(Connection paramConnection, String paramString) {
    return 0;
  }
  
  public int statusOnException(Connection paramConnection, Throwable paramThrowable) {
    if (!(paramThrowable instanceof com.mysql.jdbc.CommunicationsException) && !"com.mysql.jdbc.exceptions.jdbc4.CommunicationsException".equals(paramThrowable.getClass().getName()) && paramThrowable instanceof SQLException) {
      String str = ((SQLException)paramThrowable).getSQLState();
      return (str != null && str.startsWith("08")) ? -1 : 0;
    } 
    return -1;
  }
}
