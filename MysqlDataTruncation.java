package com.mysql.jdbc;

import java.sql.DataTruncation;

public class MysqlDataTruncation extends DataTruncation {
  public static final long serialVersionUID = 3263928195256986226L;
  
  private String message;
  
  private int vendorErrorCode;
  
  public MysqlDataTruncation(String paramString, int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramBoolean1, paramBoolean2, paramInt2, paramInt3);
    this.message = paramString;
    this.vendorErrorCode = paramInt4;
  }
  
  public int getErrorCode() {
    return this.vendorErrorCode;
  }
  
  public String getMessage() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.getMessage());
    stringBuilder.append(": ");
    stringBuilder.append(this.message);
    return stringBuilder.toString();
  }
}
