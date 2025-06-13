package com.mysql.jdbc;

import java.sql.Date;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JDBC42Helper {
  public static int checkSqlType(int paramInt, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    if (isSqlTypeSupported(paramInt))
      return paramInt; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Messages.getString("UnsupportedSQLType.0"));
    stringBuilder.append(JDBCType.valueOf(paramInt));
    throw SQLError.createSQLFeatureNotSupportedException(stringBuilder.toString(), "S1C00", paramExceptionInterceptor);
  }
  
  public static Object convertJavaTimeToJavaSql(Object paramObject) {
    if (paramObject instanceof LocalDate)
      return Date.valueOf((LocalDate)paramObject); 
    if (paramObject instanceof LocalDateTime)
      return Timestamp.valueOf((LocalDateTime)paramObject); 
    Object object = paramObject;
    if (paramObject instanceof LocalTime)
      object = Time.valueOf((LocalTime)paramObject); 
    return object;
  }
  
  public static boolean isSqlTypeSupported(int paramInt) {
    boolean bool;
    if (paramInt != 2012 && paramInt != 2013 && paramInt != 2014) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static int translateAndCheckSqlType(SQLType paramSQLType, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    return checkSqlType(paramSQLType.getVendorTypeNumber().intValue(), paramExceptionInterceptor);
  }
}
