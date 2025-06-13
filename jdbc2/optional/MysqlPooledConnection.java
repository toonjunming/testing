package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

public class MysqlPooledConnection implements PooledConnection {
  public static final int CONNECTION_CLOSED_EVENT = 2;
  
  public static final int CONNECTION_ERROR_EVENT = 1;
  
  private static final Constructor<?> JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR;
  
  private Map<ConnectionEventListener, ConnectionEventListener> connectionEventListeners;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private Connection logicalHandle = null;
  
  private Connection physicalConn;
  
  static {
    if (Util.isJdbc4()) {
      try {
        JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlPooledConnection").getConstructor(new Class[] { Connection.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = null;
    } 
  }
  
  public MysqlPooledConnection(Connection paramConnection) {
    this.physicalConn = paramConnection;
    this.connectionEventListeners = new HashMap<ConnectionEventListener, ConnectionEventListener>();
    this.exceptionInterceptor = this.physicalConn.getExceptionInterceptor();
  }
  
  public static MysqlPooledConnection getInstance(Connection paramConnection) throws SQLException {
    if (!Util.isJdbc4())
      return new MysqlPooledConnection(paramConnection); 
    Constructor<?> constructor = JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramConnection.getExceptionInterceptor();
    return (MysqlPooledConnection)Util.handleNewInstance(constructor, new Object[] { paramConnection }, exceptionInterceptor);
  }
  
  public void addConnectionEventListener(ConnectionEventListener paramConnectionEventListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connectionEventListeners : Ljava/util/Map;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 20
    //   11: aload_2
    //   12: aload_1
    //   13: aload_1
    //   14: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   19: pop
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	23	finally
    //   11	20	23	finally
  }
  
  public void callConnectionEventListeners(int paramInt, SQLException paramSQLException) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connectionEventListeners : Ljava/util/Map;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnonnull -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_3
    //   15: invokeinterface entrySet : ()Ljava/util/Set;
    //   20: invokeinterface iterator : ()Ljava/util/Iterator;
    //   25: astore_3
    //   26: new javax/sql/ConnectionEvent
    //   29: astore #4
    //   31: aload #4
    //   33: aload_0
    //   34: aload_2
    //   35: invokespecial <init> : (Ljavax/sql/PooledConnection;Ljava/sql/SQLException;)V
    //   38: aload_3
    //   39: invokeinterface hasNext : ()Z
    //   44: ifeq -> 97
    //   47: aload_3
    //   48: invokeinterface next : ()Ljava/lang/Object;
    //   53: checkcast java/util/Map$Entry
    //   56: invokeinterface getValue : ()Ljava/lang/Object;
    //   61: checkcast javax/sql/ConnectionEventListener
    //   64: astore_2
    //   65: iload_1
    //   66: iconst_2
    //   67: if_icmpne -> 81
    //   70: aload_2
    //   71: aload #4
    //   73: invokeinterface connectionClosed : (Ljavax/sql/ConnectionEvent;)V
    //   78: goto -> 38
    //   81: iload_1
    //   82: iconst_1
    //   83: if_icmpne -> 38
    //   86: aload_2
    //   87: aload #4
    //   89: invokeinterface connectionErrorOccurred : (Ljavax/sql/ConnectionEvent;)V
    //   94: goto -> 38
    //   97: aload_0
    //   98: monitorexit
    //   99: return
    //   100: astore_2
    //   101: aload_0
    //   102: monitorexit
    //   103: aload_2
    //   104: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	100	finally
    //   14	38	100	finally
    //   38	65	100	finally
    //   70	78	100	finally
    //   86	94	100	finally
  }
  
  public void close() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield physicalConn : Lcom/mysql/jdbc/Connection;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 22
    //   11: aload_1
    //   12: invokeinterface close : ()V
    //   17: aload_0
    //   18: aconst_null
    //   19: putfield physicalConn : Lcom/mysql/jdbc/Connection;
    //   22: aload_0
    //   23: getfield connectionEventListeners : Ljava/util/Map;
    //   26: astore_1
    //   27: aload_1
    //   28: ifnull -> 42
    //   31: aload_1
    //   32: invokeinterface clear : ()V
    //   37: aload_0
    //   38: aconst_null
    //   39: putfield connectionEventListeners : Ljava/util/Map;
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	45	finally
    //   11	22	45	finally
    //   22	27	45	finally
    //   31	42	45	finally
  }
  
  public Connection getConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: iconst_0
    //   5: invokevirtual getConnection : (ZZ)Ljava/sql/Connection;
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
  
  public Connection getConnection(boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield physicalConn : Lcom/mysql/jdbc/Connection;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnull -> 69
    //   11: aload_0
    //   12: getfield logicalHandle : Ljava/sql/Connection;
    //   15: astore_3
    //   16: aload_3
    //   17: ifnull -> 28
    //   20: aload_3
    //   21: checkcast com/mysql/jdbc/jdbc2/optional/ConnectionWrapper
    //   24: iconst_0
    //   25: invokevirtual close : (Z)V
    //   28: iload_1
    //   29: ifeq -> 41
    //   32: aload_0
    //   33: getfield physicalConn : Lcom/mysql/jdbc/Connection;
    //   36: invokeinterface resetServerState : ()V
    //   41: aload_0
    //   42: aload_0
    //   43: getfield physicalConn : Lcom/mysql/jdbc/Connection;
    //   46: iload_2
    //   47: invokestatic getInstance : (Lcom/mysql/jdbc/jdbc2/optional/MysqlPooledConnection;Lcom/mysql/jdbc/Connection;Z)Lcom/mysql/jdbc/jdbc2/optional/ConnectionWrapper;
    //   50: astore_3
    //   51: aload_0
    //   52: aload_3
    //   53: putfield logicalHandle : Ljava/sql/Connection;
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_3
    //   59: areturn
    //   60: astore_3
    //   61: aload_0
    //   62: iconst_1
    //   63: aload_3
    //   64: invokevirtual callConnectionEventListeners : (ILjava/sql/SQLException;)V
    //   67: aload_3
    //   68: athrow
    //   69: ldc 'Physical Connection doesn't exist'
    //   71: aload_0
    //   72: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   75: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   78: astore_3
    //   79: aload_0
    //   80: iconst_1
    //   81: aload_3
    //   82: invokevirtual callConnectionEventListeners : (ILjava/sql/SQLException;)V
    //   85: aload_3
    //   86: athrow
    //   87: astore_3
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_3
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	87	finally
    //   11	16	60	java/sql/SQLException
    //   11	16	87	finally
    //   20	28	60	java/sql/SQLException
    //   20	28	87	finally
    //   32	41	60	java/sql/SQLException
    //   32	41	87	finally
    //   41	56	60	java/sql/SQLException
    //   41	56	87	finally
    //   61	69	87	finally
    //   69	87	87	finally
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  public void removeConnectionEventListener(ConnectionEventListener paramConnectionEventListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connectionEventListeners : Ljava/util/Map;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 19
    //   11: aload_2
    //   12: aload_1
    //   13: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   18: pop
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	22	finally
    //   11	19	22	finally
  }
}
