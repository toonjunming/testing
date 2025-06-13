package com.mysql.jdbc.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = 1L;
  
  public int maxElements;
  
  public LRUCache(int paramInt) {
    super(paramInt, 0.75F, true);
    this.maxElements = paramInt;
  }
  
  public boolean removeEldestEntry(Map.Entry<K, V> paramEntry) {
    boolean bool;
    if (size() > this.maxElements) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}
