package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLDataException;

public class MySQLDataException extends SQLDataException {
  public static final long serialVersionUID = 4317904269797988676L;
  
  public MySQLDataException() {}
  
  public MySQLDataException(String paramString) {
    super(paramString);
  }
  
  public MySQLDataException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLDataException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
