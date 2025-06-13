package com.mysql.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkResources {
  private final Socket mysqlConnection;
  
  private final InputStream mysqlInput;
  
  private final OutputStream mysqlOutput;
  
  public NetworkResources(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream) {
    this.mysqlConnection = paramSocket;
    this.mysqlInput = paramInputStream;
    this.mysqlOutput = paramOutputStream;
  }
  
  public final void forceClose() {
    try {
      InputStream inputStream = this.mysqlInput;
    } finally {
      Socket socket = this.mysqlConnection;
      if (socket != null && !socket.isClosed()) {
        boolean bool = this.mysqlConnection.isInputShutdown();
        if (!bool)
          try {
            this.mysqlConnection.shutdownInput();
          } catch (UnsupportedOperationException unsupportedOperationException) {} 
      } 
    } 
  }
}
