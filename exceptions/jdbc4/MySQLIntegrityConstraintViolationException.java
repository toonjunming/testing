package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLIntegrityConstraintViolationException;

public class MySQLIntegrityConstraintViolationException extends SQLIntegrityConstraintViolationException {
  public static final long serialVersionUID = -5528363270635808904L;
  
  public MySQLIntegrityConstraintViolationException() {}
  
  public MySQLIntegrityConstraintViolationException(String paramString) {
    super(paramString);
  }
  
  public MySQLIntegrityConstraintViolationException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLIntegrityConstraintViolationException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
