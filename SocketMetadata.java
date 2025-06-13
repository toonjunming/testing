package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.Statement;

public interface SocketMetadata {
  boolean isLocallyConnected(ConnectionImpl paramConnectionImpl) throws SQLException;
  
  public static class Helper {
    public static final String IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME = "com.mysql.jdbc.test.isLocalHostnameReplacement";
    
    private static String findProcessHost(long param1Long, Statement param1Statement) throws SQLException {
      // Byte code:
      //   0: aload_2
      //   1: ldc 'SHOW PROCESSLIST'
      //   3: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
      //   8: astore_2
      //   9: aload_2
      //   10: invokeinterface next : ()Z
      //   15: ifeq -> 41
      //   18: lload_0
      //   19: aload_2
      //   20: iconst_1
      //   21: invokeinterface getLong : (I)J
      //   26: lcmp
      //   27: ifne -> 9
      //   30: aload_2
      //   31: iconst_3
      //   32: invokeinterface getString : (I)Ljava/lang/String;
      //   37: astore_2
      //   38: goto -> 43
      //   41: aconst_null
      //   42: astore_2
      //   43: aload_2
      //   44: areturn
    }
    
    public static boolean isLocallyConnected(ConnectionImpl param1ConnectionImpl) throws SQLException {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual getId : ()J
      //   4: lstore #7
      //   6: aload_0
      //   7: invokevirtual getMetadataSafeStatement : ()Ljava/sql/Statement;
      //   10: astore #11
      //   12: ldc 'com.mysql.jdbc.test.isLocalHostnameReplacement'
      //   14: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
      //   17: astore #9
      //   19: iconst_1
      //   20: istore_3
      //   21: iconst_0
      //   22: istore #4
      //   24: aload #9
      //   26: ifnull -> 43
      //   29: ldc 'com.mysql.jdbc.test.isLocalHostnameReplacement'
      //   31: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
      //   34: astore #9
      //   36: lload #7
      //   38: lstore #5
      //   40: goto -> 190
      //   43: aload_0
      //   44: invokevirtual getProperties : ()Ljava/util/Properties;
      //   47: ldc 'com.mysql.jdbc.test.isLocalHostnameReplacement'
      //   49: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
      //   52: ifnull -> 73
      //   55: aload_0
      //   56: invokevirtual getProperties : ()Ljava/util/Properties;
      //   59: ldc 'com.mysql.jdbc.test.isLocalHostnameReplacement'
      //   61: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
      //   64: astore #9
      //   66: lload #7
      //   68: lstore #5
      //   70: goto -> 190
      //   73: lload #7
      //   75: aload #11
      //   77: invokestatic findProcessHost : (JLjava/sql/Statement;)Ljava/lang/String;
      //   80: astore #10
      //   82: lload #7
      //   84: lstore #5
      //   86: aload #10
      //   88: astore #9
      //   90: aload #10
      //   92: ifnonnull -> 183
      //   95: aload_0
      //   96: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   99: ldc 'Connection id %d not found in "SHOW PROCESSLIST", assuming 32-bit overflow, using SELECT CONNECTION_ID() instead'
      //   101: iconst_1
      //   102: anewarray java/lang/Object
      //   105: dup
      //   106: iconst_0
      //   107: lload #7
      //   109: invokestatic valueOf : (J)Ljava/lang/Long;
      //   112: aastore
      //   113: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   116: invokeinterface logWarn : (Ljava/lang/Object;)V
      //   121: aload #11
      //   123: ldc 'SELECT CONNECTION_ID()'
      //   125: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
      //   130: astore #9
      //   132: aload #9
      //   134: invokeinterface next : ()Z
      //   139: ifeq -> 164
      //   142: aload #9
      //   144: iconst_1
      //   145: invokeinterface getLong : (I)J
      //   150: lstore #5
      //   152: lload #5
      //   154: aload #11
      //   156: invokestatic findProcessHost : (JLjava/sql/Statement;)Ljava/lang/String;
      //   159: astore #9
      //   161: goto -> 183
      //   164: aload_0
      //   165: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   168: ldc 'No rows returned for statement "SELECT CONNECTION_ID()", local connection check will most likely be incorrect'
      //   170: invokeinterface logError : (Ljava/lang/Object;)V
      //   175: aload #10
      //   177: astore #9
      //   179: lload #7
      //   181: lstore #5
      //   183: aload #11
      //   185: invokeinterface close : ()V
      //   190: aload #9
      //   192: ifnull -> 460
      //   195: aload_0
      //   196: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   199: ldc 'Using 'host' value of '%s' to determine locality of connection'
      //   201: iconst_1
      //   202: anewarray java/lang/Object
      //   205: dup
      //   206: iconst_0
      //   207: aload #9
      //   209: aastore
      //   210: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   213: invokeinterface logDebug : (Ljava/lang/Object;)V
      //   218: aload #9
      //   220: ldc ':'
      //   222: invokevirtual lastIndexOf : (Ljava/lang/String;)I
      //   225: istore_1
      //   226: iload_1
      //   227: iconst_m1
      //   228: if_icmpeq -> 435
      //   231: aload #9
      //   233: iconst_0
      //   234: iload_1
      //   235: invokevirtual substring : (II)Ljava/lang/String;
      //   238: astore #9
      //   240: aload #9
      //   242: invokestatic getAllByName : (Ljava/lang/String;)[Ljava/net/InetAddress;
      //   245: astore #10
      //   247: aload_0
      //   248: invokevirtual getIO : ()Lcom/mysql/jdbc/MysqlIO;
      //   251: getfield mysqlConnection : Ljava/net/Socket;
      //   254: invokevirtual getRemoteSocketAddress : ()Ljava/net/SocketAddress;
      //   257: astore #11
      //   259: aload #11
      //   261: instanceof java/net/InetSocketAddress
      //   264: ifeq -> 374
      //   267: aload #11
      //   269: checkcast java/net/InetSocketAddress
      //   272: invokevirtual getAddress : ()Ljava/net/InetAddress;
      //   275: astore #11
      //   277: aload #10
      //   279: arraylength
      //   280: istore_2
      //   281: iconst_0
      //   282: istore_1
      //   283: iload_1
      //   284: iload_2
      //   285: if_icmpge -> 369
      //   288: aload #10
      //   290: iload_1
      //   291: aaload
      //   292: astore #12
      //   294: aload #12
      //   296: aload #11
      //   298: invokevirtual equals : (Ljava/lang/Object;)Z
      //   301: ifeq -> 335
      //   304: aload_0
      //   305: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   308: ldc 'Locally connected - HostAddress(%s).equals(whereIconnectedTo({%s})'
      //   310: iconst_2
      //   311: anewarray java/lang/Object
      //   314: dup
      //   315: iconst_0
      //   316: aload #12
      //   318: aastore
      //   319: dup
      //   320: iconst_1
      //   321: aload #11
      //   323: aastore
      //   324: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   327: invokeinterface logDebug : (Ljava/lang/Object;)V
      //   332: goto -> 371
      //   335: aload_0
      //   336: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   339: ldc 'Attempted locally connected check failed - ! HostAddress(%s).equals(whereIconnectedTo(%s)'
      //   341: iconst_2
      //   342: anewarray java/lang/Object
      //   345: dup
      //   346: iconst_0
      //   347: aload #12
      //   349: aastore
      //   350: dup
      //   351: iconst_1
      //   352: aload #11
      //   354: aastore
      //   355: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   358: invokeinterface logDebug : (Ljava/lang/Object;)V
      //   363: iinc #1, 1
      //   366: goto -> 283
      //   369: iconst_0
      //   370: istore_3
      //   371: goto -> 404
      //   374: ldc 'Remote socket address %s is not an inet socket address'
      //   376: iconst_1
      //   377: anewarray java/lang/Object
      //   380: dup
      //   381: iconst_0
      //   382: aload #11
      //   384: aastore
      //   385: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   388: astore #10
      //   390: aload_0
      //   391: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   394: aload #10
      //   396: invokeinterface logDebug : (Ljava/lang/Object;)V
      //   401: iload #4
      //   403: istore_3
      //   404: iload_3
      //   405: ireturn
      //   406: astore #10
      //   408: aload_0
      //   409: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   412: ldc 'Connection.CantDetectLocalConnect'
      //   414: iconst_1
      //   415: anewarray java/lang/Object
      //   418: dup
      //   419: iconst_0
      //   420: aload #9
      //   422: aastore
      //   423: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   426: aload #10
      //   428: invokeinterface logWarn : (Ljava/lang/Object;Ljava/lang/Throwable;)V
      //   433: iconst_0
      //   434: ireturn
      //   435: aload_0
      //   436: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   439: ldc 'No port number present in 'host' from SHOW PROCESSLIST '%s', unable to determine whether locally connected'
      //   441: iconst_1
      //   442: anewarray java/lang/Object
      //   445: dup
      //   446: iconst_0
      //   447: aload #9
      //   449: aastore
      //   450: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   453: invokeinterface logWarn : (Ljava/lang/Object;)V
      //   458: iconst_0
      //   459: ireturn
      //   460: aload_0
      //   461: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
      //   464: ldc 'Cannot find process listing for connection %d in SHOW PROCESSLIST output, unable to determine if locally connected'
      //   466: iconst_1
      //   467: anewarray java/lang/Object
      //   470: dup
      //   471: iconst_0
      //   472: lload #5
      //   474: invokestatic valueOf : (J)Ljava/lang/Long;
      //   477: aastore
      //   478: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   481: invokeinterface logWarn : (Ljava/lang/Object;)V
      //   486: iconst_0
      //   487: ireturn
      //   488: astore_0
      //   489: aload #11
      //   491: invokeinterface close : ()V
      //   496: aload_0
      //   497: athrow
      // Exception table:
      //   from	to	target	type
      //   73	82	488	finally
      //   95	161	488	finally
      //   164	175	488	finally
      //   240	281	406	java/net/UnknownHostException
      //   294	332	406	java/net/UnknownHostException
      //   335	363	406	java/net/UnknownHostException
      //   374	401	406	java/net/UnknownHostException
    }
  }
}
