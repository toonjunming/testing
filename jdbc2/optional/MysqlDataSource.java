package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ConnectionPropertiesImpl;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.NonRegisteringDriver;
import com.mysql.jdbc.SQLError;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;

public class MysqlDataSource extends ConnectionPropertiesImpl implements DataSource, Referenceable, Serializable {
  public static final NonRegisteringDriver mysqlDriver;
  
  public static final long serialVersionUID = -5515846944416881264L;
  
  public String databaseName = null;
  
  public String encoding = null;
  
  public boolean explicitUrl = false;
  
  public String hostName = null;
  
  public transient PrintWriter logWriter = null;
  
  public String password = null;
  
  public int port = 3306;
  
  public String profileSql = "false";
  
  public String url = null;
  
  public String user = null;
  
  static {
    try {
      NonRegisteringDriver nonRegisteringDriver = new NonRegisteringDriver();
      this();
      mysqlDriver = nonRegisteringDriver;
      return;
    } catch (Exception exception) {
      throw new RuntimeException("Can not load Driver class com.mysql.jdbc.Driver");
    } 
  }
  
  public Properties exposeAsProperties(Properties paramProperties) throws SQLException {
    return exposeAsProperties(paramProperties, true);
  }
  
  public Connection getConnection() throws SQLException {
    return getConnection(this.user, this.password);
  }
  
  public Connection getConnection(String paramString1, String paramString2) throws SQLException {
    Properties properties = new Properties();
    if (paramString1 != null)
      properties.setProperty("user", paramString1); 
    if (paramString2 != null)
      properties.setProperty("password", paramString2); 
    exposeAsProperties(properties);
    return getConnection(properties);
  }
  
  public Connection getConnection(Properties paramProperties) throws SQLException {
    String str;
    if (!this.explicitUrl) {
      StringBuilder stringBuilder = new StringBuilder("jdbc:mysql://");
      String str1 = this.hostName;
      if (str1 != null)
        stringBuilder.append(str1); 
      stringBuilder.append(":");
      stringBuilder.append(this.port);
      stringBuilder.append("/");
      str1 = this.databaseName;
      if (str1 != null)
        stringBuilder.append(str1); 
      str = stringBuilder.toString();
    } else {
      str = this.url;
    } 
    Properties properties = mysqlDriver.parseURL(str, null);
    if (properties != null) {
      properties.remove("DBNAME");
      properties.remove("HOST");
      properties.remove("PORT");
      for (String str1 : properties.keySet())
        paramProperties.setProperty(str1, properties.getProperty(str1)); 
      return mysqlDriver.connect(str, paramProperties);
    } 
    throw SQLError.createSQLException(Messages.getString("MysqlDataSource.BadUrl", new Object[] { str }), "08006", null);
  }
  
  public String getDatabaseName() {
    String str = this.databaseName;
    if (str == null)
      str = ""; 
    return str;
  }
  
  public PrintWriter getLogWriter() {
    return this.logWriter;
  }
  
  public int getLoginTimeout() {
    return 0;
  }
  
  public int getPort() {
    return this.port;
  }
  
  public int getPortNumber() {
    return getPort();
  }
  
  public Reference getReference() throws NamingException {
    Reference reference = new Reference(getClass().getName(), "com.mysql.jdbc.jdbc2.optional.MysqlDataSourceFactory", null);
    reference.add(new StringRefAddr("user", getUser()));
    reference.add(new StringRefAddr("password", this.password));
    reference.add(new StringRefAddr("serverName", getServerName()));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(getPort());
    reference.add(new StringRefAddr("port", stringBuilder.toString()));
    reference.add(new StringRefAddr("databaseName", getDatabaseName()));
    reference.add(new StringRefAddr("url", getUrl()));
    reference.add(new StringRefAddr("explicitUrl", String.valueOf(this.explicitUrl)));
    try {
      storeToRef(reference);
      return reference;
    } catch (SQLException sQLException) {
      throw new NamingException(sQLException.getMessage());
    } 
  }
  
  public String getServerName() {
    String str = this.hostName;
    if (str == null)
      str = ""; 
    return str;
  }
  
  public String getURL() {
    return getUrl();
  }
  
  public String getUrl() {
    if (!this.explicitUrl) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("jdbc:mysql://");
      stringBuilder.append(getServerName());
      stringBuilder.append(":");
      stringBuilder.append(getPort());
      stringBuilder.append("/");
      stringBuilder.append(getDatabaseName());
      return stringBuilder.toString();
    } 
    return this.url;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public void setDatabaseName(String paramString) {
    this.databaseName = paramString;
  }
  
  public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
    this.logWriter = paramPrintWriter;
  }
  
  public void setLoginTimeout(int paramInt) throws SQLException {}
  
  public void setPassword(String paramString) {
    this.password = paramString;
  }
  
  public void setPort(int paramInt) {
    this.port = paramInt;
  }
  
  public void setPortNumber(int paramInt) {
    setPort(paramInt);
  }
  
  public void setPropertiesViaRef(Reference paramReference) throws SQLException {
    initializeFromRef(paramReference);
  }
  
  public void setServerName(String paramString) {
    this.hostName = paramString;
  }
  
  public void setURL(String paramString) {
    setUrl(paramString);
  }
  
  public void setUrl(String paramString) {
    this.url = paramString;
    this.explicitUrl = true;
  }
  
  public void setUser(String paramString) {
    this.user = paramString;
  }
}
