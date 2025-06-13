package com.mysql.jdbc;

import com.mysql.jdbc.util.LRUCache;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

public class PerConnectionLRUFactory implements CacheAdapterFactory<String, PreparedStatement.ParseInfo> {
  public CacheAdapter<String, PreparedStatement.ParseInfo> getInstance(Connection paramConnection, String paramString, int paramInt1, int paramInt2, Properties paramProperties) throws SQLException {
    return new PerConnectionLRU(paramConnection, paramInt1, paramInt2);
  }
  
  public class PerConnectionLRU implements CacheAdapter<String, PreparedStatement.ParseInfo> {
    private final LRUCache<String, PreparedStatement.ParseInfo> cache;
    
    private final int cacheSqlLimit;
    
    private final Connection conn;
    
    public final PerConnectionLRUFactory this$0;
    
    public PerConnectionLRU(Connection param1Connection, int param1Int1, int param1Int2) {
      this.cacheSqlLimit = param1Int2;
      this.cache = new LRUCache<String, PreparedStatement.ParseInfo>(param1Int1);
      this.conn = param1Connection;
    }
    
    public PreparedStatement.ParseInfo get(String param1String) {
      if (param1String == null || param1String.length() > this.cacheSqlLimit)
        return null; 
      synchronized (this.conn.getConnectionMutex()) {
        return this.cache.get(param1String);
      } 
    }
    
    public void invalidate(String param1String) {
      synchronized (this.conn.getConnectionMutex()) {
        this.cache.remove(param1String);
        return;
      } 
    }
    
    public void invalidateAll() {
      synchronized (this.conn.getConnectionMutex()) {
        this.cache.clear();
        return;
      } 
    }
    
    public void invalidateAll(Set<String> param1Set) {
      synchronized (this.conn.getConnectionMutex()) {
        for (String str : param1Set)
          this.cache.remove(str); 
        return;
      } 
    }
    
    public void put(String param1String, PreparedStatement.ParseInfo param1ParseInfo) {
      if (param1String == null || param1String.length() > this.cacheSqlLimit)
        return; 
      synchronized (this.conn.getConnectionMutex()) {
        this.cache.put(param1String, param1ParseInfo);
        return;
      } 
    }
  }
}
