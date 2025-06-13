package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLNonTransientConnectionException;

public class MySQLNonTransientConnectionException extends SQLNonTransientConnectionException {
  public static final long serialVersionUID = -3050543822763367670L;
  
  public MySQLNonTransientConnectionException() {}
  
  public MySQLNonTransientConnectionException(String paramString) {
    super(paramString);
  }
  
  public MySQLNonTransientConnectionException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLNonTransientConnectionException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
