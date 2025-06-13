package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLTransientConnectionException;

public class MySQLTransientConnectionException extends SQLTransientConnectionException {
  public static final long serialVersionUID = 8699144578759941201L;
  
  public MySQLTransientConnectionException() {}
  
  public MySQLTransientConnectionException(String paramString) {
    super(paramString);
  }
  
  public MySQLTransientConnectionException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLTransientConnectionException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
