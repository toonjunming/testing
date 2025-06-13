package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

public class JDBC4SuspendableXAConnection extends SuspendableXAConnection {
  private final Map<StatementEventListener, StatementEventListener> statementEventListeners = new HashMap<StatementEventListener, StatementEventListener>();
  
  public JDBC4SuspendableXAConnection(Connection paramConnection) throws SQLException {
    super(paramConnection);
  }
  
  public void addStatementEventListener(StatementEventListener paramStatementEventListener) {
    synchronized (this.statementEventListeners) {
      this.statementEventListeners.put(paramStatementEventListener, paramStatementEventListener);
      return;
    } 
  }
  
  public void close() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial close : ()V
    //   6: aload_0
    //   7: getfield statementEventListeners : Ljava/util/Map;
    //   10: invokeinterface clear : ()V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  public void fireStatementEvent(StatementEvent paramStatementEvent) throws SQLException {
    synchronized (this.statementEventListeners) {
      Iterator<StatementEventListener> iterator = this.statementEventListeners.keySet().iterator();
      while (iterator.hasNext())
        ((StatementEventListener)iterator.next()).statementClosed(paramStatementEvent); 
      return;
    } 
  }
  
  public void removeStatementEventListener(StatementEventListener paramStatementEventListener) {
    synchronized (this.statementEventListeners) {
      this.statementEventListeners.remove(paramStatementEventListener);
      return;
    } 
  }
}
