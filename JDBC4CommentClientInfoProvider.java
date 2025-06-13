package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public class JDBC4CommentClientInfoProvider implements JDBC4ClientInfoProvider {
  private Properties clientInfo;
  
  private void setComment(Connection paramConnection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield clientInfo : Ljava/util/Properties;
    //   14: invokevirtual entrySet : ()Ljava/util/Set;
    //   17: invokeinterface iterator : ()Ljava/util/Iterator;
    //   22: astore_3
    //   23: aload_3
    //   24: invokeinterface hasNext : ()Z
    //   29: ifeq -> 149
    //   32: aload_2
    //   33: invokevirtual length : ()I
    //   36: ifle -> 46
    //   39: aload_2
    //   40: ldc ', '
    //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: aload_3
    //   47: invokeinterface next : ()Ljava/lang/Object;
    //   52: checkcast java/util/Map$Entry
    //   55: astore #4
    //   57: new java/lang/StringBuilder
    //   60: astore #5
    //   62: aload #5
    //   64: invokespecial <init> : ()V
    //   67: aload #5
    //   69: ldc ''
    //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: aload #5
    //   77: aload #4
    //   79: invokeinterface getKey : ()Ljava/lang/Object;
    //   84: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload_2
    //   89: aload #5
    //   91: invokevirtual toString : ()Ljava/lang/String;
    //   94: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: aload_2
    //   99: ldc '='
    //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: new java/lang/StringBuilder
    //   108: astore #5
    //   110: aload #5
    //   112: invokespecial <init> : ()V
    //   115: aload #5
    //   117: ldc ''
    //   119: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: pop
    //   123: aload #5
    //   125: aload #4
    //   127: invokeinterface getValue : ()Ljava/lang/Object;
    //   132: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_2
    //   137: aload #5
    //   139: invokevirtual toString : ()Ljava/lang/String;
    //   142: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: pop
    //   146: goto -> 23
    //   149: aload_1
    //   150: checkcast com/mysql/jdbc/Connection
    //   153: aload_2
    //   154: invokevirtual toString : ()Ljava/lang/String;
    //   157: invokeinterface setStatementComment : (Ljava/lang/String;)V
    //   162: aload_0
    //   163: monitorexit
    //   164: return
    //   165: astore_1
    //   166: aload_0
    //   167: monitorexit
    //   168: aload_1
    //   169: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	165	finally
    //   23	46	165	finally
    //   46	146	165	finally
    //   149	162	165	finally
  }
  
  public void destroy() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield clientInfo : Ljava/util/Properties;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public String getClientInfo(Connection paramConnection, String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield clientInfo : Ljava/util/Properties;
    //   6: aload_2
    //   7: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
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
  
  public Properties getClientInfo(Connection paramConnection) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield clientInfo : Ljava/util/Properties;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void initialize(Connection paramConnection, Properties paramProperties) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/Properties
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: putfield clientInfo : Ljava/util/Properties;
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
  
  public void setClientInfo(Connection paramConnection, String paramString1, String paramString2) throws SQLClientInfoException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield clientInfo : Ljava/util/Properties;
    //   6: aload_2
    //   7: aload_3
    //   8: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: aload_1
    //   14: invokespecial setComment : (Ljava/sql/Connection;)V
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public void setClientInfo(Connection paramConnection, Properties paramProperties) throws SQLClientInfoException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/Properties
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: aload_3
    //   12: putfield clientInfo : Ljava/util/Properties;
    //   15: aload_2
    //   16: invokevirtual propertyNames : ()Ljava/util/Enumeration;
    //   19: astore_3
    //   20: aload_3
    //   21: invokeinterface hasMoreElements : ()Z
    //   26: ifeq -> 59
    //   29: aload_3
    //   30: invokeinterface nextElement : ()Ljava/lang/Object;
    //   35: checkcast java/lang/String
    //   38: astore #4
    //   40: aload_0
    //   41: getfield clientInfo : Ljava/util/Properties;
    //   44: aload #4
    //   46: aload_2
    //   47: aload #4
    //   49: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   52: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   55: pop
    //   56: goto -> 20
    //   59: aload_0
    //   60: aload_1
    //   61: invokespecial setComment : (Ljava/sql/Connection;)V
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	67	finally
    //   20	56	67	finally
    //   59	64	67	finally
  }
}
