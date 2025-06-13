package com.mysql.jdbc;

import java.rmi.server.UID;
import java.sql.SQLException;
import java.sql.Savepoint;

public class MysqlSavepoint implements Savepoint {
  private ExceptionInterceptor exceptionInterceptor;
  
  private String savepointName;
  
  public MysqlSavepoint(ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    this(getUniqueId(), paramExceptionInterceptor);
  }
  
  public MysqlSavepoint(String paramString, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    if (paramString != null && paramString.length() != 0) {
      this.savepointName = paramString;
      this.exceptionInterceptor = paramExceptionInterceptor;
      return;
    } 
    throw SQLError.createSQLException("Savepoint name can not be NULL or empty", "S1009", paramExceptionInterceptor);
  }
  
  private static String getUniqueId() {
    String str = (new UID()).toString();
    int i = str.length();
    StringBuilder stringBuilder = new StringBuilder(i + 1);
    stringBuilder.append('_');
    for (byte b = 0; b < i; b++) {
      char c = str.charAt(b);
      if (Character.isLetter(c) || Character.isDigit(c)) {
        stringBuilder.append(c);
      } else {
        stringBuilder.append('_');
      } 
    } 
    return stringBuilder.toString();
  }
  
  public int getSavepointId() throws SQLException {
    throw SQLError.createSQLException("Only named savepoints are supported.", "S1C00", this.exceptionInterceptor);
  }
  
  public String getSavepointName() throws SQLException {
    return this.savepointName;
  }
}
