package com.mysql.jdbc.integration.jboss;

import java.sql.SQLException;
import org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter;

public final class ExtendedMysqlExceptionSorter extends MySQLExceptionSorter {
  public static final long serialVersionUID = -2454582336945931069L;
  
  public boolean isExceptionFatal(SQLException paramSQLException) {
    String str = paramSQLException.getSQLState();
    return (str != null && str.startsWith("08")) ? true : super.isExceptionFatal(paramSQLException);
  }
}
