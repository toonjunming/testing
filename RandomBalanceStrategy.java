package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RandomBalanceStrategy implements BalanceStrategy {
  private Map<String, Integer> getArrayIndexMap(List<String> paramList) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>(paramList.size());
    for (byte b = 0; b < paramList.size(); b++)
      hashMap.put(paramList.get(b), Integer.valueOf(b)); 
    return (Map)hashMap;
  }
  
  public void destroy() {}
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {}
  
  public ConnectionImpl pickConnection(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOflong, int paramInt) throws SQLException {
    int j = paramList.size();
    ArrayList<String> arrayList = new ArrayList(j);
    arrayList.addAll(paramList);
    arrayList.removeAll(paramLoadBalancedConnectionProxy.getGlobalBlacklist().keySet());
    Map<String, Integer> map = getArrayIndexMap(arrayList);
    int i = 0;
    ConnectionImpl connectionImpl = null;
    while (true) {
      if (i < paramInt) {
        int k = (int)Math.floor(Math.random() * arrayList.size());
        if (arrayList.size() != 0) {
          String str = arrayList.get(k);
          ConnectionImpl connectionImpl1 = paramMap.get(str);
          connectionImpl = connectionImpl1;
          if (connectionImpl1 == null)
            try {
              connectionImpl = paramLoadBalancedConnectionProxy.createConnectionForHost(str);
            } catch (SQLException sQLException) {
              if (paramLoadBalancedConnectionProxy.shouldExceptionTriggerConnectionSwitch(sQLException)) {
                Integer integer = map.get(str);
                if (integer != null) {
                  arrayList.remove(integer.intValue());
                  map = getArrayIndexMap(arrayList);
                } 
                paramLoadBalancedConnectionProxy.addToGlobalBlacklist(str);
                k = i;
                if (arrayList.size() == 0) {
                  k = i + 1;
                  try {
                    Thread.sleep(250L);
                  } catch (InterruptedException interruptedException) {}
                  new HashMap<Object, Object>(j);
                  arrayList.addAll(paramList);
                  arrayList.removeAll(paramLoadBalancedConnectionProxy.getGlobalBlacklist().keySet());
                  map = getArrayIndexMap(arrayList);
                } 
                i = k;
                continue;
              } 
              throw sQLException;
            }  
          return (ConnectionImpl)sQLException;
        } 
        throw SQLError.createSQLException("No hosts configured", null);
      } 
      if (sQLException == null)
        return null; 
      throw sQLException;
    } 
  }
}
