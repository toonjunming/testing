package com.mysql.jdbc;

import java.util.Set;

public interface CacheAdapter<K, V> {
  V get(K paramK);
  
  void invalidate(K paramK);
  
  void invalidateAll();
  
  void invalidateAll(Set<K> paramSet);
  
  void put(K paramK, V paramV);
}
