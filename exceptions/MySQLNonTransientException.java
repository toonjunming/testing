package com.mysql.jdbc.exceptions;

import java.sql.SQLException;

public class MySQLNonTransientException extends SQLException {
  public static final long serialVersionUID = -8714521137552613517L;
  
  public MySQLNonTransientException() {}
  
  public MySQLNonTransientException(String paramString) {
    super(paramString);
  }
  
  public MySQLNonTransientException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLNonTransientException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
