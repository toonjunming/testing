package com.mysql.jdbc;

import java.sql.SQLException;

public class LoadBalancedMySQLConnection extends MultiHostMySQLConnection implements LoadBalancedConnection {
  public LoadBalancedMySQLConnection(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy) {
    super(paramLoadBalancedConnectionProxy);
  }
  
  public boolean addHost(String paramString) throws SQLException {
    return getThisAsProxy().addHost(paramString);
  }
  
  public void close() throws SQLException {
    getThisAsProxy().doClose();
  }
  
  public LoadBalancedConnectionProxy getThisAsProxy() {
    return (LoadBalancedConnectionProxy)super.getThisAsProxy();
  }
  
  public void ping() throws SQLException {
    ping(true);
  }
  
  public void ping(boolean paramBoolean) throws SQLException {
    if (paramBoolean) {
      getThisAsProxy().doPing();
    } else {
      getActiveMySQLConnection().ping();
    } 
  }
  
  public void removeHost(String paramString) throws SQLException {
    getThisAsProxy().removeHost(paramString);
  }
  
  public void removeHostWhenNotInUse(String paramString) throws SQLException {
    getThisAsProxy().removeHostWhenNotInUse(paramString);
  }
}
