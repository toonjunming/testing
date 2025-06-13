package com.mysql.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;

public class MysqlParameterMetadata implements ParameterMetaData {
  private ExceptionInterceptor exceptionInterceptor;
  
  public ResultSetMetaData metadata = null;
  
  public int parameterCount = 0;
  
  public boolean returnSimpleMetadata = false;
  
  public MysqlParameterMetadata(int paramInt) {
    this.parameterCount = paramInt;
    this.returnSimpleMetadata = true;
  }
  
  public MysqlParameterMetadata(Field[] paramArrayOfField, int paramInt, ExceptionInterceptor paramExceptionInterceptor) {
    this.metadata = new ResultSetMetaData(paramArrayOfField, false, true, paramExceptionInterceptor);
    this.parameterCount = paramInt;
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  private void checkAvailable() throws SQLException {
    ResultSetMetaData resultSetMetaData = this.metadata;
    if (resultSetMetaData != null && resultSetMetaData.fields != null)
      return; 
    throw SQLError.createSQLException("Parameter metadata not available for the given statement", "S1C00", this.exceptionInterceptor);
  }
  
  private void checkBounds(int paramInt) throws SQLException {
    if (paramInt >= 1) {
      if (paramInt <= this.parameterCount)
        return; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Parameter index of '");
      stringBuilder1.append(paramInt);
      stringBuilder1.append("' is greater than number of parameters, which is '");
      stringBuilder1.append(this.parameterCount);
      stringBuilder1.append("'.");
      throw SQLError.createSQLException(stringBuilder1.toString(), "S1009", this.exceptionInterceptor);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Parameter index of '");
    stringBuilder.append(paramInt);
    stringBuilder.append("' is invalid.");
    throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
  }
  
  public String getParameterClassName(int paramInt) throws SQLException {
    if (this.returnSimpleMetadata) {
      checkBounds(paramInt);
      return "java.lang.String";
    } 
    checkAvailable();
    return this.metadata.getColumnClassName(paramInt);
  }
  
  public int getParameterCount() throws SQLException {
    return this.parameterCount;
  }
  
  public int getParameterMode(int paramInt) throws SQLException {
    return 1;
  }
  
  public int getParameterType(int paramInt) throws SQLException {
    if (this.returnSimpleMetadata) {
      checkBounds(paramInt);
      return 12;
    } 
    checkAvailable();
    return this.metadata.getColumnType(paramInt);
  }
  
  public String getParameterTypeName(int paramInt) throws SQLException {
    if (this.returnSimpleMetadata) {
      checkBounds(paramInt);
      return "VARCHAR";
    } 
    checkAvailable();
    return this.metadata.getColumnTypeName(paramInt);
  }
  
  public int getPrecision(int paramInt) throws SQLException {
    if (this.returnSimpleMetadata) {
      checkBounds(paramInt);
      return 0;
    } 
    checkAvailable();
    return this.metadata.getPrecision(paramInt);
  }
  
  public int getScale(int paramInt) throws SQLException {
    if (this.returnSimpleMetadata) {
      checkBounds(paramInt);
      return 0;
    } 
    checkAvailable();
    return this.metadata.getScale(paramInt);
  }
  
  public int isNullable(int paramInt) throws SQLException {
    checkAvailable();
    return this.metadata.isNullable(paramInt);
  }
  
  public boolean isSigned(int paramInt) throws SQLException {
    if (this.returnSimpleMetadata) {
      checkBounds(paramInt);
      return false;
    } 
    checkAvailable();
    return this.metadata.isSigned(paramInt);
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    return paramClass.isInstance(this);
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
    } 
  }
}
