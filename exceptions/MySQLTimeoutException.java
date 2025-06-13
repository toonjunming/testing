package com.mysql.jdbc.exceptions;

public class MySQLTimeoutException extends MySQLTransientException {
  public static final long serialVersionUID = -789621240523230339L;
  
  public MySQLTimeoutException() {
    super("Statement cancelled due to timeout or client request");
  }
  
  public MySQLTimeoutException(String paramString) {
    super(paramString);
  }
  
  public MySQLTimeoutException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLTimeoutException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
