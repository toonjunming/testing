package com.mysql.jdbc;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Properties;

public class SocksProxySocketFactory extends StandardSocketFactory {
  public static int SOCKS_DEFAULT_PORT = 1080;
  
  public Socket createSocket(Properties paramProperties) {
    int i;
    String str2 = paramProperties.getProperty("socksProxyHost");
    String str1 = paramProperties.getProperty("socksProxyPort", String.valueOf(SOCKS_DEFAULT_PORT));
    int j = SOCKS_DEFAULT_PORT;
    try {
      i = Integer.valueOf(str1).intValue();
    } catch (NumberFormatException numberFormatException) {
      i = j;
    } 
    return new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(str2, i)));
  }
}
