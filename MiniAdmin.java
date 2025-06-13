package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MiniAdmin {
  private Connection conn;
  
  public MiniAdmin(String paramString) throws SQLException {
    this(paramString, new Properties());
  }
  
  public MiniAdmin(String paramString, Properties paramProperties) throws SQLException {
    this.conn = (Connection)(new Driver()).connect(paramString, paramProperties);
  }
  
  public MiniAdmin(Connection paramConnection) throws SQLException {
    if (paramConnection != null) {
      if (paramConnection instanceof Connection) {
        this.conn = (Connection)paramConnection;
        return;
      } 
      throw SQLError.createSQLException(Messages.getString("MiniAdmin.1"), "S1000", ((ConnectionImpl)paramConnection).getExceptionInterceptor());
    } 
    throw SQLError.createSQLException(Messages.getString("MiniAdmin.0"), "S1000", null);
  }
  
  public void shutdown() throws SQLException {
    this.conn.shutdownServer();
  }
}
