package com.mysql.jdbc;

import java.sql.SQLException;

public class NotImplemented extends SQLException {
  public static final long serialVersionUID = 7768433826547599990L;
  
  public NotImplemented() {
    super(Messages.getString("NotImplemented.0"), "S1C00");
  }
}
