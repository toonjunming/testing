package com.mysql.jdbc.exceptions;

public class MySQLInvalidAuthorizationSpecException extends MySQLNonTransientException {
  public static final long serialVersionUID = 6878889837492500030L;
  
  public MySQLInvalidAuthorizationSpecException() {}
  
  public MySQLInvalidAuthorizationSpecException(String paramString) {
    super(paramString);
  }
  
  public MySQLInvalidAuthorizationSpecException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLInvalidAuthorizationSpecException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
