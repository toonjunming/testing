package com.mysql.jdbc.exceptions.jdbc4;

public class MySQLQueryInterruptedException extends MySQLNonTransientException {
  private static final long serialVersionUID = -8714521137662613517L;
  
  public MySQLQueryInterruptedException() {}
  
  public MySQLQueryInterruptedException(String paramString) {
    super(paramString);
  }
  
  public MySQLQueryInterruptedException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLQueryInterruptedException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
