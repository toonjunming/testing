package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.SQLType;

public class JDBC42UpdatableResultSet extends JDBC4UpdatableResultSet {
  public JDBC42UpdatableResultSet(String paramString, Field[] paramArrayOfField, RowData paramRowData, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) throws SQLException {
    super(paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl);
  }
  
  private int translateAndCheckSqlType(SQLType paramSQLType) throws SQLException {
    return JDBC42Helper.translateAndCheckSqlType(paramSQLType, getExceptionInterceptor());
  }
  
  public <T> T getObject(int paramInt, Class<T> paramClass) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: aload_2
    //   15: ifnull -> 177
    //   18: aload_2
    //   19: ldc java/time/LocalDate
    //   21: invokevirtual equals : (Ljava/lang/Object;)Z
    //   24: ifeq -> 45
    //   27: aload_2
    //   28: aload_0
    //   29: iload_1
    //   30: invokevirtual getDate : (I)Ljava/sql/Date;
    //   33: invokevirtual toLocalDate : ()Ljava/time/LocalDate;
    //   36: invokevirtual cast : (Ljava/lang/Object;)Ljava/lang/Object;
    //   39: astore_2
    //   40: aload #4
    //   42: monitorexit
    //   43: aload_2
    //   44: areturn
    //   45: aload_2
    //   46: ldc java/time/LocalDateTime
    //   48: invokevirtual equals : (Ljava/lang/Object;)Z
    //   51: ifeq -> 72
    //   54: aload_2
    //   55: aload_0
    //   56: iload_1
    //   57: invokevirtual getTimestamp : (I)Ljava/sql/Timestamp;
    //   60: invokevirtual toLocalDateTime : ()Ljava/time/LocalDateTime;
    //   63: invokevirtual cast : (Ljava/lang/Object;)Ljava/lang/Object;
    //   66: astore_2
    //   67: aload #4
    //   69: monitorexit
    //   70: aload_2
    //   71: areturn
    //   72: aload_2
    //   73: ldc java/time/LocalTime
    //   75: invokevirtual equals : (Ljava/lang/Object;)Z
    //   78: ifeq -> 99
    //   81: aload_2
    //   82: aload_0
    //   83: iload_1
    //   84: invokevirtual getTime : (I)Ljava/sql/Time;
    //   87: invokevirtual toLocalTime : ()Ljava/time/LocalTime;
    //   90: invokevirtual cast : (Ljava/lang/Object;)Ljava/lang/Object;
    //   93: astore_2
    //   94: aload #4
    //   96: monitorexit
    //   97: aload_2
    //   98: areturn
    //   99: aload_2
    //   100: ldc java/time/OffsetDateTime
    //   102: invokevirtual equals : (Ljava/lang/Object;)Z
    //   105: istore_3
    //   106: iload_3
    //   107: ifeq -> 130
    //   110: aload_2
    //   111: aload_0
    //   112: iload_1
    //   113: invokevirtual getString : (I)Ljava/lang/String;
    //   116: invokestatic parse : (Ljava/lang/CharSequence;)Ljava/time/OffsetDateTime;
    //   119: invokevirtual cast : (Ljava/lang/Object;)Ljava/lang/Object;
    //   122: astore #5
    //   124: aload #4
    //   126: monitorexit
    //   127: aload #5
    //   129: areturn
    //   130: aload_2
    //   131: ldc java/time/OffsetTime
    //   133: invokevirtual equals : (Ljava/lang/Object;)Z
    //   136: istore_3
    //   137: iload_3
    //   138: ifeq -> 161
    //   141: aload_2
    //   142: aload_0
    //   143: iload_1
    //   144: invokevirtual getString : (I)Ljava/lang/String;
    //   147: invokestatic parse : (Ljava/lang/CharSequence;)Ljava/time/OffsetTime;
    //   150: invokevirtual cast : (Ljava/lang/Object;)Ljava/lang/Object;
    //   153: astore #5
    //   155: aload #4
    //   157: monitorexit
    //   158: aload #5
    //   160: areturn
    //   161: aload_0
    //   162: iload_1
    //   163: aload_2
    //   164: invokespecial getObject : (ILjava/lang/Class;)Ljava/lang/Object;
    //   167: astore_2
    //   168: aload #4
    //   170: monitorexit
    //   171: aload_2
    //   172: areturn
    //   173: astore_2
    //   174: goto -> 189
    //   177: ldc 'Type parameter can not be null'
    //   179: ldc 'S1009'
    //   181: aload_0
    //   182: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   185: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   188: athrow
    //   189: aload #4
    //   191: monitorexit
    //   192: aload_2
    //   193: athrow
    //   194: astore #5
    //   196: goto -> 161
    // Exception table:
    //   from	to	target	type
    //   18	43	173	finally
    //   45	70	173	finally
    //   72	97	173	finally
    //   99	106	173	finally
    //   110	124	194	java/time/format/DateTimeParseException
    //   110	124	173	finally
    //   124	127	173	finally
    //   130	137	173	finally
    //   141	155	194	java/time/format/DateTimeParseException
    //   141	155	173	finally
    //   155	158	173	finally
    //   161	171	173	finally
    //   177	189	173	finally
    //   189	192	173	finally
  }
  
  public void updateObject(int paramInt, Object paramObject) throws SQLException {
    super.updateObject(paramInt, JDBC42Helper.convertJavaTimeToJavaSql(paramObject));
  }
  
  public void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
    super.updateObject(paramInt1, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), paramInt2);
  }
  
  public void updateObject(int paramInt, Object paramObject, SQLType paramSQLType) throws SQLException {
    updateObjectInternal(paramInt, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), Integer.valueOf(translateAndCheckSqlType(paramSQLType)), 0);
  }
  
  public void updateObject(int paramInt1, Object paramObject, SQLType paramSQLType, int paramInt2) throws SQLException {
    updateObjectInternal(paramInt1, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), Integer.valueOf(translateAndCheckSqlType(paramSQLType)), paramInt2);
  }
  
  public void updateObject(String paramString, Object paramObject) throws SQLException {
    super.updateObject(paramString, JDBC42Helper.convertJavaTimeToJavaSql(paramObject));
  }
  
  public void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
    super.updateObject(paramString, JDBC42Helper.convertJavaTimeToJavaSql(paramObject), paramInt);
  }
  
  public void updateObject(String paramString, Object paramObject, SQLType paramSQLType) throws SQLException {
    updateObjectInternal(findColumn(paramString), JDBC42Helper.convertJavaTimeToJavaSql(paramObject), Integer.valueOf(translateAndCheckSqlType(paramSQLType)), 0);
  }
  
  public void updateObject(String paramString, Object paramObject, SQLType paramSQLType, int paramInt) throws SQLException {
    updateObjectInternal(findColumn(paramString), JDBC42Helper.convertJavaTimeToJavaSql(paramObject), Integer.valueOf(translateAndCheckSqlType(paramSQLType)), paramInt);
  }
}
