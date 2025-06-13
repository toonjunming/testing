package com.mysql.jdbc.exceptions;

public class MySQLTransactionRollbackException extends MySQLTransientException implements DeadlockTimeoutRollbackMarker {
  public static final long serialVersionUID = 6034999468737801730L;
  
  public MySQLTransactionRollbackException() {}
  
  public MySQLTransactionRollbackException(String paramString) {
    super(paramString);
  }
  
  public MySQLTransactionRollbackException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLTransactionRollbackException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
