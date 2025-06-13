package com.mysql.jdbc.exceptions;

public class MySQLStatementCancelledException extends MySQLNonTransientException {
  public static final long serialVersionUID = -8762717748377197378L;
  
  public MySQLStatementCancelledException() {
    super("Statement cancelled due to client request");
  }
  
  public MySQLStatementCancelledException(String paramString) {
    super(paramString);
  }
  
  public MySQLStatementCancelledException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLStatementCancelledException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
