package com.mysql.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ReplicationDriver extends NonRegisteringReplicationDriver implements Driver {
  static {
    try {
      NonRegisteringReplicationDriver nonRegisteringReplicationDriver = new NonRegisteringReplicationDriver();
      this();
      DriverManager.registerDriver(nonRegisteringReplicationDriver);
      return;
    } catch (SQLException sQLException) {
      throw new RuntimeException("Can't register driver!");
    } 
  }
}
