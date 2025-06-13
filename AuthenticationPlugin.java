package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;

public interface AuthenticationPlugin extends Extension {
  String getProtocolPluginName();
  
  boolean isReusable();
  
  boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException;
  
  boolean requiresConfidentiality();
  
  void reset();
  
  void setAuthenticationParameters(String paramString1, String paramString2);
}
