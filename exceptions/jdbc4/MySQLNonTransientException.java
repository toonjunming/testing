package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLNonTransientException;

public class MySQLNonTransientException extends SQLNonTransientException {
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
