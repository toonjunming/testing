package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Iterator;

public abstract class IterateBlock<T> {
  public DatabaseMetaData.IteratorWithCleanup<T> iteratorWithCleanup;
  
  public Iterator<T> javaIterator;
  
  public boolean stopIterating = false;
  
  public IterateBlock(DatabaseMetaData.IteratorWithCleanup<T> paramIteratorWithCleanup) {
    this.iteratorWithCleanup = paramIteratorWithCleanup;
    this.javaIterator = null;
  }
  
  public IterateBlock(Iterator<T> paramIterator) {
    this.javaIterator = paramIterator;
    this.iteratorWithCleanup = null;
  }
  
  public void doForAll() throws SQLException {
    if (this.iteratorWithCleanup != null) {
      try {
        while (this.iteratorWithCleanup.hasNext()) {
          forEach(this.iteratorWithCleanup.next());
          boolean bool = this.stopIterating;
          if (bool)
            break; 
        } 
      } finally {
        this.iteratorWithCleanup.close();
      } 
    } else {
      while (this.javaIterator.hasNext()) {
        forEach(this.javaIterator.next());
        if (this.stopIterating)
          break; 
      } 
    } 
  }
  
  public abstract void forEach(T paramT) throws SQLException;
  
  public final boolean fullIteration() {
    return this.stopIterating ^ true;
  }
}
