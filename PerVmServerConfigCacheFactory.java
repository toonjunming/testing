package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PerVmServerConfigCacheFactory implements CacheAdapterFactory<String, Map<String, String>> {
  public static final ConcurrentHashMap<String, Map<String, String>> serverConfigByUrl = new ConcurrentHashMap<String, Map<String, String>>();
  
  private static final CacheAdapter<String, Map<String, String>> serverConfigCache = new CacheAdapter<String, Map<String, String>>() {
      public Map<String, String> get(String param1String) {
        return PerVmServerConfigCacheFactory.serverConfigByUrl.get(param1String);
      }
      
      public void invalidate(String param1String) {
        PerVmServerConfigCacheFactory.serverConfigByUrl.remove(param1String);
      }
      
      public void invalidateAll() {
        PerVmServerConfigCacheFactory.serverConfigByUrl.clear();
      }
      
      public void invalidateAll(Set<String> param1Set) {
        for (String str : param1Set)
          PerVmServerConfigCacheFactory.serverConfigByUrl.remove(str); 
      }
      
      public void put(String param1String, Map<String, String> param1Map) {
        PerVmServerConfigCacheFactory.serverConfigByUrl.putIfAbsent(param1String, param1Map);
      }
    };
  
  public CacheAdapter<String, Map<String, String>> getInstance(Connection paramConnection, String paramString, int paramInt1, int paramInt2, Properties paramProperties) throws SQLException {
    return serverConfigCache;
  }
}
