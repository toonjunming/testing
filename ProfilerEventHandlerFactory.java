package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.sql.SQLException;

public class ProfilerEventHandlerFactory {
  public Log log = null;
  
  private Connection ownerConnection = null;
  
  private ProfilerEventHandlerFactory(Connection paramConnection) {
    this.ownerConnection = paramConnection;
    try {
      this.log = paramConnection.getLog();
      return;
    } catch (SQLException sQLException) {
      throw new RuntimeException("Unable to get logger from connection");
    } 
  }
  
  public static ProfilerEventHandler getInstance(MySQLConnection paramMySQLConnection) throws SQLException {
    // Byte code:
    //   0: ldc com/mysql/jdbc/ProfilerEventHandlerFactory
    //   2: monitorenter
    //   3: aload_0
    //   4: invokeinterface getProfilerEventHandlerInstance : ()Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   9: astore_2
    //   10: aload_2
    //   11: astore_1
    //   12: aload_2
    //   13: ifnonnull -> 61
    //   16: aload_0
    //   17: invokeinterface getProfilerEventHandler : ()Ljava/lang/String;
    //   22: astore_2
    //   23: aload_0
    //   24: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   29: astore_1
    //   30: aload_2
    //   31: iconst_0
    //   32: anewarray java/lang/Class
    //   35: iconst_0
    //   36: anewarray java/lang/Object
    //   39: aload_1
    //   40: invokestatic getInstance : (Ljava/lang/String;[Ljava/lang/Class;[Ljava/lang/Object;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/lang/Object;
    //   43: checkcast com/mysql/jdbc/profiler/ProfilerEventHandler
    //   46: astore_1
    //   47: aload_0
    //   48: aload_1
    //   49: invokeinterface initializeExtension : (Lcom/mysql/jdbc/Extension;)V
    //   54: aload_0
    //   55: aload_1
    //   56: invokeinterface setProfilerEventHandlerInstance : (Lcom/mysql/jdbc/profiler/ProfilerEventHandler;)V
    //   61: ldc com/mysql/jdbc/ProfilerEventHandlerFactory
    //   63: monitorexit
    //   64: aload_1
    //   65: areturn
    //   66: astore_0
    //   67: ldc com/mysql/jdbc/ProfilerEventHandlerFactory
    //   69: monitorexit
    //   70: aload_0
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   3	10	66	finally
    //   16	61	66	finally
  }
  
  public static void removeInstance(MySQLConnection paramMySQLConnection) {
    // Byte code:
    //   0: ldc com/mysql/jdbc/ProfilerEventHandlerFactory
    //   2: monitorenter
    //   3: aload_0
    //   4: invokeinterface getProfilerEventHandlerInstance : ()Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   9: astore_0
    //   10: aload_0
    //   11: ifnull -> 20
    //   14: aload_0
    //   15: invokeinterface destroy : ()V
    //   20: ldc com/mysql/jdbc/ProfilerEventHandlerFactory
    //   22: monitorexit
    //   23: return
    //   24: astore_0
    //   25: ldc com/mysql/jdbc/ProfilerEventHandlerFactory
    //   27: monitorexit
    //   28: aload_0
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   3	10	24	finally
    //   14	20	24	finally
  }
}
