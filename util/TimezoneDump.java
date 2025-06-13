package com.mysql.jdbc.util;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class TimezoneDump {
  private static final String DEFAULT_URL = "jdbc:mysql:///test";
  
  public static void main(String[] paramArrayOfString) throws Exception {
    String str;
    ResultSet resultSet;
    if (paramArrayOfString.length == 1 && paramArrayOfString[0] != null) {
      str = paramArrayOfString[0];
    } else {
      str = "jdbc:mysql:///test";
    } 
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    try {
      resultSet = DriverManager.getConnection(str).createStatement().executeQuery("SHOW VARIABLES LIKE 'timezone'");
    } finally {
      str = null;
    } 
    if (resultSet != null)
      resultSet.close(); 
    throw str;
  }
}
