package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Security;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MysqlNativePasswordPlugin implements AuthenticationPlugin {
  private Connection connection;
  
  private String password = null;
  
  public void destroy() {
    this.password = null;
  }
  
  public String getProtocolPluginName() {
    return "mysql_native_password";
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.connection = paramConnection;
  }
  
  public boolean isReusable() {
    return true;
  }
  
  public boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException {
    try {
      paramList.clear();
      String str = this.password;
      if (paramBuffer == null || str == null || str.length() == 0) {
        paramBuffer = new Buffer();
        this(new byte[0]);
        paramList.add(paramBuffer);
        return true;
      } 
      paramBuffer = new Buffer(Security.scramble411(str, paramBuffer.readString(), this.connection.getPasswordCharacterEncoding()));
      paramList.add(paramBuffer);
      return true;
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("MysqlIO.91"));
      stringBuilder.append(Messages.getString("MysqlIO.92"));
      throw SQLError.createSQLException(stringBuilder.toString(), "S1000", null);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw SQLError.createSQLException(Messages.getString("MysqlNativePasswordPlugin.1", new Object[] { this.connection.getPasswordCharacterEncoding() }), "S1000", null);
    } 
  }
  
  public boolean requiresConfidentiality() {
    return false;
  }
  
  public void reset() {}
  
  public void setAuthenticationParameters(String paramString1, String paramString2) {
    this.password = paramString2;
  }
}
