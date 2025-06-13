package com.mysql.jdbc;

import java.sql.SQLException;

public class NdbLoadBalanceExceptionChecker extends StandardLoadBalanceExceptionChecker {
  private boolean checkNdbException(SQLException paramSQLException) {
    return (paramSQLException.getMessage().startsWith("Lock wait timeout exceeded") || (paramSQLException.getMessage().startsWith("Got temporary error") && paramSQLException.getMessage().endsWith("from NDB")));
  }
  
  public boolean shouldExceptionTriggerFailover(SQLException paramSQLException) {
    return (super.shouldExceptionTriggerFailover(paramSQLException) || checkNdbException(paramSQLException));
  }
}
