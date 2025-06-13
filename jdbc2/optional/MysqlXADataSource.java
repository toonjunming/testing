package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

public class MysqlXADataSource extends MysqlDataSource implements XADataSource {
  public static final long serialVersionUID = 7911390333152247455L;
  
  private XAConnection wrapConnection(Connection paramConnection) throws SQLException {
    if (!getPinGlobalTxToPhysicalConnection()) {
      Connection connection = (Connection)paramConnection;
      if (!connection.getPinGlobalTxToPhysicalConnection())
        return MysqlXAConnection.getInstance(connection, getLogXaCommands()); 
    } 
    return SuspendableXAConnection.getInstance((Connection)paramConnection);
  }
  
  public XAConnection getXAConnection() throws SQLException {
    return wrapConnection(getConnection());
  }
  
  public XAConnection getXAConnection(String paramString1, String paramString2) throws SQLException {
    return wrapConnection(getConnection(paramString1, paramString2));
  }
}
