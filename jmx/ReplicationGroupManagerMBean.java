package com.mysql.jdbc.jmx;

import java.sql.SQLException;

public interface ReplicationGroupManagerMBean {
  void addSlaveHost(String paramString1, String paramString2) throws SQLException;
  
  long getActiveLogicalConnectionCount(String paramString);
  
  int getActiveMasterHostCount(String paramString);
  
  int getActiveSlaveHostCount(String paramString);
  
  String getMasterHostsList(String paramString);
  
  String getRegisteredConnectionGroups();
  
  String getSlaveHostsList(String paramString);
  
  int getSlavePromotionCount(String paramString);
  
  long getTotalLogicalConnectionCount(String paramString);
  
  void promoteSlaveToMaster(String paramString1, String paramString2) throws SQLException;
  
  void removeMasterHost(String paramString1, String paramString2) throws SQLException;
  
  void removeSlaveHost(String paramString1, String paramString2) throws SQLException;
}
