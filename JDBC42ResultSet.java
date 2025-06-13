package com.mysql.jdbc;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;

public class JDBC42ResultSet extends JDBC4ResultSet {
  public JDBC42ResultSet(long paramLong1, long paramLong2, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) {
    super(paramLong1, paramLong2, paramMySQLConnection, paramStatementImpl);
  }
  
  public JDBC42ResultSet(String paramString, Field[] paramArrayOfField, RowData paramRowData, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) throws SQLException {
    super(paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl);
  }
  
  public <T> T getObject(int paramInt, Class<T> paramClass) throws SQLException {
    if (paramClass != null) {
      String str;
      DateTimeParseException dateTimeParseException1;
      boolean bool = paramClass.equals(LocalDate.class);
      Class clazz4 = null;
      Class clazz1 = null;
      Class clazz2 = null;
      Date date = null;
      Class clazz3 = null;
      if (bool) {
        date = getDate(paramInt);
        if (date == null) {
          paramClass = clazz3;
        } else {
          paramClass = (Class<T>)paramClass.cast(date.toLocalDate());
        } 
        return (T)paramClass;
      } 
      if (paramClass.equals(LocalDateTime.class)) {
        Timestamp timestamp = getTimestamp(paramInt);
        if (timestamp == null) {
          paramClass = clazz4;
        } else {
          paramClass = (Class<T>)paramClass.cast(timestamp.toLocalDateTime());
        } 
        return (T)paramClass;
      } 
      if (paramClass.equals(LocalTime.class)) {
        Time time = getTime(paramInt);
        if (time == null) {
          paramClass = clazz1;
        } else {
          paramClass = (Class<T>)paramClass.cast(time.toLocalTime());
        } 
        return (T)paramClass;
      } 
      if (paramClass.equals(OffsetDateTime.class)) {
        try {
          String str1 = getString(paramInt);
          if (str1 == null) {
            paramClass = clazz2;
          } else {
            str1 = (String)paramClass.cast(OffsetDateTime.parse(str1));
            str = str1;
          } 
          return (T)str;
        } catch (DateTimeParseException dateTimeParseException2) {}
      } else if (str.equals(OffsetTime.class)) {
        String str1 = getString(paramInt);
        if (str1 == null) {
          dateTimeParseException1 = dateTimeParseException2;
        } else {
          dateTimeParseException2 = dateTimeParseException1.cast(OffsetTime.parse(str1));
          dateTimeParseException1 = dateTimeParseException2;
        } 
        return (T)dateTimeParseException1;
      } 
      return super.getObject(paramInt, (Class<T>)dateTimeParseException1);
    } 
    throw SQLError.createSQLException("Type parameter can not be null", "S1009", getExceptionInterceptor());
  }
  
  public void updateObject(int paramInt, Object paramObject, SQLType paramSQLType) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateObject(int paramInt1, Object paramObject, SQLType paramSQLType, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateObject(String paramString, Object paramObject, SQLType paramSQLType) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateObject(String paramString, Object paramObject, SQLType paramSQLType, int paramInt) throws SQLException {
    throw new NotUpdatable();
  }
}
