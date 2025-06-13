package com.mysql.jdbc.exceptions;

import java.sql.SQLException;

public class MySQLTransientException extends SQLException {
  public static final long serialVersionUID = -1885878228558607563L;
  
  public MySQLTransientException() {}
  
  public MySQLTransientException(String paramString) {
    super(paramString);
  }
  
  public MySQLTransientException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLTransientException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
