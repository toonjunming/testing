package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLSyntaxErrorException;

public class MySQLSyntaxErrorException extends SQLSyntaxErrorException {
  public static final long serialVersionUID = 6919059513432113764L;
  
  public MySQLSyntaxErrorException() {}
  
  public MySQLSyntaxErrorException(String paramString) {
    super(paramString);
  }
  
  public MySQLSyntaxErrorException(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public MySQLSyntaxErrorException(String paramString1, String paramString2, int paramInt) {
    super(paramString1, paramString2, paramInt);
  }
}
