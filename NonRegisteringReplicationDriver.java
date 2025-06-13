package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class NonRegisteringReplicationDriver extends NonRegisteringDriver {
  public Connection connect(String paramString, Properties paramProperties) throws SQLException {
    return connectReplicationConnection(paramString, paramProperties);
  }
}
