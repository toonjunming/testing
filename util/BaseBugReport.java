package com.mysql.jdbc.util;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class BaseBugReport {
  private Connection conn;
  
  private Driver driver;
  
  public BaseBugReport() {
    try {
      Driver driver = new Driver();
      this();
      this.driver = driver;
      return;
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException.toString());
    } 
  }
  
  public final void assertTrue(String paramString, boolean paramBoolean) throws Exception {
    if (paramBoolean)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Assertion failed: ");
    stringBuilder.append(paramString);
    throw new Exception(stringBuilder.toString());
  }
  
  public final void assertTrue(boolean paramBoolean) throws Exception {
    assertTrue("(no message given)", paramBoolean);
  }
  
  public final Connection getConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield conn : Ljava/sql/Connection;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 20
    //   11: aload_1
    //   12: invokeinterface isClosed : ()Z
    //   17: ifeq -> 28
    //   20: aload_0
    //   21: aload_0
    //   22: invokevirtual getNewConnection : ()Ljava/sql/Connection;
    //   25: putfield conn : Ljava/sql/Connection;
    //   28: aload_0
    //   29: getfield conn : Ljava/sql/Connection;
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: areturn
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   11	20	37	finally
    //   20	28	37	finally
    //   28	33	37	finally
  }
  
  public final Connection getConnection(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aconst_null
    //   5: invokevirtual getConnection : (Ljava/lang/String;Ljava/util/Properties;)Ljava/sql/Connection;
    //   8: astore_1
    //   9: aload_0
    //   10: monitorexit
    //   11: aload_1
    //   12: areturn
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	13	finally
  }
  
  public final Connection getConnection(String paramString, Properties paramProperties) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield driver : Lcom/mysql/jdbc/Driver;
    //   6: aload_1
    //   7: aload_2
    //   8: invokevirtual connect : (Ljava/lang/String;Ljava/util/Properties;)Ljava/sql/Connection;
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: areturn
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public final Connection getNewConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: invokevirtual getUrl : ()Ljava/lang/String;
    //   7: invokevirtual getConnection : (Ljava/lang/String;)Ljava/sql/Connection;
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: areturn
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public String getUrl() {
    return "jdbc:mysql:///test";
  }
  
  public final void run() throws Exception {
    try {
      setUp();
      runTest();
      return;
    } finally {
      tearDown();
    } 
  }
  
  public abstract void runTest() throws Exception;
  
  public abstract void setUp() throws Exception;
  
  public abstract void tearDown() throws Exception;
}
