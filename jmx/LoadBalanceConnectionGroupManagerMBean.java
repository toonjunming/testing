package com.mysql.jdbc.jmx;

import java.sql.SQLException;

public interface LoadBalanceConnectionGroupManagerMBean {
  void addHost(String paramString1, String paramString2, boolean paramBoolean);
  
  int getActiveHostCount(String paramString);
  
  String getActiveHostsList(String paramString);
  
  long getActiveLogicalConnectionCount(String paramString);
  
  long getActivePhysicalConnectionCount(String paramString);
  
  String getRegisteredConnectionGroups();
  
  int getTotalHostCount(String paramString);
  
  long getTotalLogicalConnectionCount(String paramString);
  
  long getTotalPhysicalConnectionCount(String paramString);
  
  long getTotalTransactionCount(String paramString);
  
  void removeHost(String paramString1, String paramString2) throws SQLException;
  
  void stopNewConnectionsToHost(String paramString1, String paramString2) throws SQLException;
}
