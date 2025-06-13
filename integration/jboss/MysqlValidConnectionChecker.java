package com.mysql.jdbc.integration.jboss;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import org.jboss.resource.adapter.jdbc.ValidConnectionChecker;

public final class MysqlValidConnectionChecker implements ValidConnectionChecker, Serializable {
  private static final long serialVersionUID = 8909421133577519177L;
  
  public SQLException isValidConnection(Connection paramConnection) {
    try {
    
    } catch (SQLException sQLException1) {
    
    } finally {
      paramConnection = null;
      if (paramConnection != null)
        try {
          paramConnection.close();
        } catch (SQLException sQLException) {} 
    } 
    if (sQLException != null)
      try {
        sQLException.close();
      } catch (SQLException sQLException1) {} 
    return (SQLException)SYNTHETIC_LOCAL_VARIABLE_2;
  }
}
