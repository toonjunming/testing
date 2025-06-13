package com.mysql.jdbc.exceptions.jdbc4;

import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.StreamingNotifiable;
import java.sql.SQLRecoverableException;

public class CommunicationsException extends SQLRecoverableException implements StreamingNotifiable {
  public static final long serialVersionUID = 4317904269797988677L;
  
  private String exceptionMessage;
  
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
