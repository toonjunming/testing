package com.mysql.jdbc;

public class AssertionFailedException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public AssertionFailedException(Exception paramException) {
    super(stringBuilder.toString());
  }
  
  public static void shouldNotHappen(Exception paramException) throws AssertionFailedException {
    throw new AssertionFailedException(paramException);
  }
}
