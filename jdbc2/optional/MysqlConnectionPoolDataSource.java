package com.mysql.jdbc.jdbc2.optional;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public class MysqlConnectionPoolDataSource extends MysqlDataSource implements ConnectionPoolDataSource {
  public static final long serialVersionUID = -7767325445592304961L;
  
  public PooledConnection getPooledConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getConnection : ()Ljava/sql/Connection;
    //   6: checkcast com/mysql/jdbc/Connection
    //   9: invokestatic getInstance : (Lcom/mysql/jdbc/Connection;)Lcom/mysql/jdbc/jdbc2/optional/MysqlPooledConnection;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	17	finally
  }
  
  public PooledConnection getPooledConnection(String paramString1, String paramString2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: invokevirtual getConnection : (Ljava/lang/String;Ljava/lang/String;)Ljava/sql/Connection;
    //   8: checkcast com/mysql/jdbc/Connection
    //   11: invokestatic getInstance : (Lcom/mysql/jdbc/Connection;)Lcom/mysql/jdbc/jdbc2/optional/MysqlPooledConnection;
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: areturn
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
}
