package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.Util;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MysqlOldPasswordPlugin implements AuthenticationPlugin {
  private Connection connection;
  
  private String password = null;
  
  public void destroy() {
    this.password = null;
  }
  
  public String getProtocolPluginName() {
    return "mysql_old_password";
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.connection = paramConnection;
  }
  
  public boolean isReusable() {
    return true;
  }
  
  public boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException {
    paramList.clear();
    String str = this.password;
    if (paramBuffer == null || str == null || str.length() == 0) {
      paramBuffer = new Buffer(new byte[0]);
      paramList.add(paramBuffer);
      return true;
    } 
    paramBuffer = new Buffer(StringUtils.getBytes(Util.newCrypt(str, paramBuffer.readString().substring(0, 8), this.connection.getPasswordCharacterEncoding())));
    paramBuffer.setPosition(paramBuffer.getBufLength());
    int i = paramBuffer.getBufLength();
    paramBuffer.writeByte((byte)0);
    paramBuffer.setBufLength(i + 1);
    paramBuffer.setPosition(0);
    paramList.add(paramBuffer);
    return true;
  }
  
  public boolean requiresConfidentiality() {
    return false;
  }
  
  public void reset() {}
  
  public void setAuthenticationParameters(String paramString1, String paramString2) {
    this.password = paramString2;
  }
}
