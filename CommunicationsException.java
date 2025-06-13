package com.mysql.jdbc;

import java.sql.SQLException;

public class CommunicationsException extends SQLException implements StreamingNotifiable {
  public static final long serialVersionUID = 3193864990663398317L;
  
  private String exceptionMessage = null;
  
  public CommunicationsException(MySQLConnection paramMySQLConnection, long paramLong1, long paramLong2, Exception paramException) {
    this.exceptionMessage = SQLError.createLinkFailureMessageBasedOnHeuristics(paramMySQLConnection, paramLong1, paramLong2, paramException);
    if (paramException != null)
      initCause(paramException); 
  }
  
  public String getMessage() {
    return this.exceptionMessage;
  }
  
  public String getSQLState() {
    return "08S01";
  }
  
  public void setWasStreamingResults() {
    this.exceptionMessage = Messages.getString("CommunicationsException.ClientWasStreaming");
  }
}
