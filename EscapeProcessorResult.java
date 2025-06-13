package com.mysql.jdbc;

public class EscapeProcessorResult {
  public boolean callingStoredFunction = false;
  
  public String escapedSql;
  
  public byte usesVariables = 0;
}
