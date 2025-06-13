package com.mysql.jdbc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class StandardSocketFactory implements SocketFactory, SocketMetadata {
  public static final String TCP_KEEP_ALIVE_DEFAULT_VALUE = "true";
  
  public static final String TCP_KEEP_ALIVE_PROPERTY_NAME = "tcpKeepAlive";
  
  public static final String TCP_NO_DELAY_DEFAULT_VALUE = "true";
  
  public static final String TCP_NO_DELAY_PROPERTY_NAME = "tcpNoDelay";
  
  public static final String TCP_RCV_BUF_DEFAULT_VALUE = "0";
  
  public static final String TCP_RCV_BUF_PROPERTY_NAME = "tcpRcvBuf";
  
  public static final String TCP_SND_BUF_DEFAULT_VALUE = "0";
  
  public static final String TCP_SND_BUF_PROPERTY_NAME = "tcpSndBuf";
  
  public static final String TCP_TRAFFIC_CLASS_DEFAULT_VALUE = "0";
  
  public static final String TCP_TRAFFIC_CLASS_PROPERTY_NAME = "tcpTrafficClass";
  
  public String host = null;
  
  public long loginTimeoutCheckTimestamp = System.currentTimeMillis();
  
  public int loginTimeoutCountdown = DriverManager.getLoginTimeout() * 1000;
  
  public int port = 3306;
  
  public Socket rawSocket = null;
  
  public int socketTimeoutBackup = 0;
  
  private void configureSocket(Socket paramSocket, Properties paramProperties) throws SocketException, IOException {
    paramSocket.setTcpNoDelay(Boolean.valueOf(paramProperties.getProperty("tcpNoDelay", "true")).booleanValue());
    String str = paramProperties.getProperty("tcpKeepAlive", "true");
    if (str != null && str.length() > 0)
      paramSocket.setKeepAlive(Boolean.valueOf(str).booleanValue()); 
    int i = Integer.parseInt(paramProperties.getProperty("tcpRcvBuf", "0"));
    if (i > 0)
      paramSocket.setReceiveBufferSize(i); 
    i = Integer.parseInt(paramProperties.getProperty("tcpSndBuf", "0"));
    if (i > 0)
      paramSocket.setSendBufferSize(i); 
    i = Integer.parseInt(paramProperties.getProperty("tcpTrafficClass", "0"));
    if (i > 0)
      paramSocket.setTrafficClass(i); 
  }
  
  public Socket afterHandshake() throws SocketException, IOException {
    resetLoginTimeCountdown();
    this.rawSocket.setSoTimeout(this.socketTimeoutBackup);
    return this.rawSocket;
  }
  
  public Socket beforeHandshake() throws SocketException, IOException {
    resetLoginTimeCountdown();
    int i = this.rawSocket.getSoTimeout();
    this.socketTimeoutBackup = i;
    this.rawSocket.setSoTimeout(getRealTimeout(i));
    return this.rawSocket;
  }
  
  public Socket connect(String paramString, int paramInt, Properties paramProperties) throws SocketException, IOException {
    if (paramProperties != null) {
      StringBuilder stringBuilder;
      this.host = paramString;
      this.port = paramInt;
      paramString = paramProperties.getProperty("localSocketAddress");
      byte b = 0;
      if (paramString != null && paramString.length() > 0) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(paramString), 0);
      } else {
        paramString = null;
      } 
      String str = paramProperties.getProperty("connectTimeout");
      if (str != null) {
        try {
          paramInt = Integer.parseInt(str);
        } catch (NumberFormatException numberFormatException) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Illegal value '");
          stringBuilder.append(str);
          stringBuilder.append("' for connectTimeout");
          throw new SocketException(stringBuilder.toString());
        } 
      } else {
        paramInt = 0;
      } 
      str = this.host;
      if (str != null) {
        InetAddress[] arrayOfInetAddress = InetAddress.getAllByName(str);
        if (arrayOfInetAddress.length != 0) {
          str = null;
          while (b < arrayOfInetAddress.length) {
            try {
              Socket socket = createSocket(paramProperties);
              this.rawSocket = socket;
              configureSocket(socket, paramProperties);
              InetSocketAddress inetSocketAddress = new InetSocketAddress();
              this(arrayOfInetAddress[b], this.port);
              if (stringBuilder != null)
                this.rawSocket.bind((SocketAddress)stringBuilder); 
              this.rawSocket.connect(inetSocketAddress, getRealTimeout(paramInt));
              break;
            } catch (SocketException socketException) {
              resetLoginTimeCountdown();
              this.rawSocket = null;
              b++;
            } 
          } 
          if (this.rawSocket != null || socketException == null) {
            resetLoginTimeCountdown();
            return this.rawSocket;
          } 
          throw socketException;
        } 
        throw new SocketException("No addresses for host");
      } 
    } 
    throw new SocketException("Unable to create socket");
  }
  
  public Socket createSocket(Properties paramProperties) {
    return new Socket();
  }
  
  public int getRealTimeout(int paramInt) {
    int i = this.loginTimeoutCountdown;
    return (i > 0 && (paramInt == 0 || paramInt > i)) ? i : paramInt;
  }
  
  public boolean isLocallyConnected(ConnectionImpl paramConnectionImpl) throws SQLException {
    return SocketMetadata.Helper.isLocallyConnected(paramConnectionImpl);
  }
  
  public void resetLoginTimeCountdown() throws SocketException {
    if (this.loginTimeoutCountdown > 0) {
      long l = System.currentTimeMillis();
      int i = (int)(this.loginTimeoutCountdown - l - this.loginTimeoutCheckTimestamp);
      this.loginTimeoutCountdown = i;
      if (i > 0) {
        this.loginTimeoutCheckTimestamp = l;
      } else {
        throw new SocketException(Messages.getString("Connection.LoginTimeout"));
      } 
    } 
  }
}
