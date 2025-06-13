package com.mysql.jdbc.jdbc2.optional;

import javax.transaction.xa.XAException;

public class MysqlXAException extends XAException {
  private static final long serialVersionUID = -9075817535836563004L;
  
  private String message;
  
  public String xidAsString;
  
  public MysqlXAException(int paramInt, String paramString1, String paramString2) {
    super(paramInt);
    this.message = paramString1;
    this.xidAsString = paramString2;
  }
  
  public MysqlXAException(String paramString1, String paramString2) {
    this.message = paramString1;
    this.xidAsString = paramString2;
  }
  
  public String getMessage() {
    String str = super.getMessage();
    StringBuilder stringBuilder = new StringBuilder();
    if (str != null) {
      stringBuilder.append(str);
      stringBuilder.append(":");
    } 
    str = this.message;
    if (str != null)
      stringBuilder.append(str); 
    return stringBuilder.toString();
  }
}
