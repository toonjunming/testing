package com.mysql.jdbc;

import java.sql.SQLException;

public interface ReplicationConnection extends MySQLConnection {
  void addSlaveHost(String paramString) throws SQLException;
  
  long getConnectionGroupId();
  
  Connection getCurrentConnection();
  
  Connection getMasterConnection();
  
  Connection getSlavesConnection();
  
  boolean isHostMaster(String paramString);
  
  boolean isHostSlave(String paramString);
  
  void promoteSlaveToMaster(String paramString) throws SQLException;
  
  void removeMasterHost(String paramString) throws SQLException;
  
  void removeMasterHost(String paramString, boolean paramBoolean) throws SQLException;
  
  void removeSlave(String paramString) throws SQLException;
  
  void removeSlave(String paramString, boolean paramBoolean) throws SQLException;
}
