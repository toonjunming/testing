package com.mysql.jdbc;

public class ConnectionFeatureNotAvailableException extends CommunicationsException {
  public static final long serialVersionUID = -5065030488729238287L;
  
  public ConnectionFeatureNotAvailableException(MySQLConnection paramMySQLConnection, long paramLong, Exception paramException) {
    super(paramMySQLConnection, paramLong, 0L, paramException);
  }
  
  public String getMessage() {
    return "Feature not available in this distribution of Connector/J";
  }
  
  public String getSQLState() {
    return "01S00";
  }
}
