package com.mysql.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver extends NonRegisteringDriver implements Driver {
  static {
    try {
      Driver driver = new Driver();
      this();
      DriverManager.registerDriver(driver);
      return;
    } catch (SQLException sQLException) {
      throw new RuntimeException("Can't register driver!");
    } 
  }
}
