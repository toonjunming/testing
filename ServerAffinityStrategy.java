package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ServerAffinityStrategy extends RandomBalanceStrategy {
  public static final String AFFINITY_ORDER = "serverAffinityOrder";
  
  public String[] affinityOrderedServers = null;
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    super.init(paramConnection, paramProperties);
    String str = paramProperties.getProperty("serverAffinityOrder");
    if (!StringUtils.isNullOrEmpty(str))
      this.affinityOrderedServers = str.split(","); 
  }
  
  public ConnectionImpl pickConnection(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOflong, int paramInt) throws SQLException {
    if (this.affinityOrderedServers == null)
      return super.pickConnection(paramLoadBalancedConnectionProxy, paramList, paramMap, paramArrayOflong, paramInt); 
    Map<String, Long> map = paramLoadBalancedConnectionProxy.getGlobalBlacklist();
    for (String str : this.affinityOrderedServers) {
      if (paramList.contains(str) && !map.containsKey(str)) {
        ConnectionImpl connectionImpl = paramMap.get(str);
        if (connectionImpl != null)
          return connectionImpl; 
        try {
          return paramLoadBalancedConnectionProxy.createConnectionForHost(str);
        } catch (SQLException sQLException) {
          if (paramLoadBalancedConnectionProxy.shouldExceptionTriggerConnectionSwitch(sQLException))
            paramLoadBalancedConnectionProxy.addToGlobalBlacklist(str); 
        } 
      } 
    } 
    return super.pickConnection(paramLoadBalancedConnectionProxy, paramList, paramMap, paramArrayOflong, paramInt);
  }
}
