package com.mysql.jdbc;

import java.sql.SQLException;

public class OperationNotSupportedException extends SQLException {
  public static final long serialVersionUID = 474918612056813430L;
  
  public OperationNotSupportedException() {
    super(Messages.getString("RowDataDynamic.10"), "S1009");
  }
}
