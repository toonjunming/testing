package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLTransientException;

public class MySQLTransientException extends SQLTransientException {
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
