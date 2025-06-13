package com.mysql.jdbc;

import java.sql.SQLException;

public interface LoadBalancedConnection extends MySQLConnection {
  boolean addHost(String paramString) throws SQLException;
  
  void ping(boolean paramBoolean) throws SQLException;
  
  void removeHost(String paramString) throws SQLException;
  
  void removeHostWhenNotInUse(String paramString) throws SQLException;
}
