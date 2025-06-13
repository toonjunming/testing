package com.mysql.jdbc;

import java.sql.NClob;

public class JDBC4NClob extends Clob implements NClob {
  public JDBC4NClob(ExceptionInterceptor paramExceptionInterceptor) {
    super(paramExceptionInterceptor);
  }
  
  public JDBC4NClob(String paramString, ExceptionInterceptor paramExceptionInterceptor) {
    super(paramString, paramExceptionInterceptor);
  }
}
