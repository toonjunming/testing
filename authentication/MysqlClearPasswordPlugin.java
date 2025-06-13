package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.StringUtils;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MysqlClearPasswordPlugin implements AuthenticationPlugin {
  private Connection connection;
  
  private String password = null;
  
  public void destroy() {
    this.password = null;
  }
  
  public String getProtocolPluginName() {
    return "mysql_clear_password";
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.connection = paramConnection;
  }
  
  public boolean isReusable() {
    return true;
  }
  
  public boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException {
    paramList.clear();
    try {
      String str1;
      if (this.connection.versionMeetsMinimum(5, 7, 6)) {
        str1 = this.connection.getPasswordCharacterEncoding();
      } else {
        str1 = "UTF-8";
      } 
      String str2 = this.password;
      if (str2 == null)
        str2 = ""; 
      Buffer buffer = new Buffer(StringUtils.getBytes(str2, str1));
      buffer.setPosition(buffer.getBufLength());
      int i = buffer.getBufLength();
      buffer.writeByte((byte)0);
      buffer.setBufLength(i + 1);
      buffer.setPosition(0);
      paramList.add(buffer);
      return true;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw SQLError.createSQLException(Messages.getString("MysqlClearPasswordPlugin.1", new Object[] { this.connection.getPasswordCharacterEncoding() }), "S1000", null);
    } 
  }
  
  public boolean requiresConfidentiality() {
    return true;
  }
  
  public void reset() {}
  
  public void setAuthenticationParameters(String paramString1, String paramString2) {
    this.password = paramString2;
  }
}
