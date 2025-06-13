package com.mysql.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Properties;

public class NamedPipeSocketFactory implements SocketFactory, SocketMetadata {
  public static final String NAMED_PIPE_PROP_NAME = "namedPipePath";
  
  private Socket namedPipeSocket;
  
  public Socket afterHandshake() throws SocketException, IOException {
    return this.namedPipeSocket;
  }
  
  public Socket beforeHandshake() throws SocketException, IOException {
    return this.namedPipeSocket;
  }
  
  public Socket connect(String paramString, int paramInt, Properties paramProperties) throws SocketException, IOException {
    StringBuilder stringBuilder;
    paramString = paramProperties.getProperty("namedPipePath");
    if (paramString == null) {
      paramString = "\\\\.\\pipe\\MySQL";
    } else if (paramString.length() == 0) {
      stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("NamedPipeSocketFactory.2"));
      stringBuilder.append("namedPipePath");
      stringBuilder.append(Messages.getString("NamedPipeSocketFactory.3"));
      throw new SocketException(stringBuilder.toString());
    } 
    NamedPipeSocket namedPipeSocket = new NamedPipeSocket((String)stringBuilder);
    this.namedPipeSocket = namedPipeSocket;
    return namedPipeSocket;
  }
  
  public boolean isLocallyConnected(ConnectionImpl paramConnectionImpl) throws SQLException {
    return true;
  }
  
  public class NamedPipeSocket extends Socket {
    private boolean isClosed = false;
    
    private RandomAccessFile namedPipeFile;
    
    public final NamedPipeSocketFactory this$0;
    
    public NamedPipeSocket(String param1String) throws IOException {
      if (param1String != null && param1String.length() != 0) {
        this.namedPipeFile = new RandomAccessFile(param1String, "rw");
        return;
      } 
      throw new IOException(Messages.getString("NamedPipeSocketFactory.4"));
    }
    
    public void close() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield namedPipeFile : Ljava/io/RandomAccessFile;
      //   6: invokevirtual close : ()V
      //   9: aload_0
      //   10: iconst_1
      //   11: putfield isClosed : Z
      //   14: aload_0
      //   15: monitorexit
      //   16: return
      //   17: astore_1
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_1
      //   21: athrow
      // Exception table:
      //   from	to	target	type
      //   2	14	17	finally
    }
    
    public InputStream getInputStream() throws IOException {
      return new NamedPipeSocketFactory.RandomAccessFileInputStream(this.namedPipeFile);
    }
    
    public OutputStream getOutputStream() throws IOException {
      return new NamedPipeSocketFactory.RandomAccessFileOutputStream(this.namedPipeFile);
    }
    
    public boolean isClosed() {
      return this.isClosed;
    }
  }
  
  public class RandomAccessFileInputStream extends InputStream {
    public RandomAccessFile raFile;
    
    public final NamedPipeSocketFactory this$0;
    
    public RandomAccessFileInputStream(RandomAccessFile param1RandomAccessFile) {
      this.raFile = param1RandomAccessFile;
    }
    
    public int available() throws IOException {
      return -1;
    }
    
    public void close() throws IOException {
      this.raFile.close();
    }
    
    public int read() throws IOException {
      return this.raFile.read();
    }
    
    public int read(byte[] param1ArrayOfbyte) throws IOException {
      return this.raFile.read(param1ArrayOfbyte);
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      return this.raFile.read(param1ArrayOfbyte, param1Int1, param1Int2);
    }
  }
  
  public class RandomAccessFileOutputStream extends OutputStream {
    public RandomAccessFile raFile;
    
    public final NamedPipeSocketFactory this$0;
    
    public RandomAccessFileOutputStream(RandomAccessFile param1RandomAccessFile) {
      this.raFile = param1RandomAccessFile;
    }
    
    public void close() throws IOException {
      this.raFile.close();
    }
    
    public void write(int param1Int) throws IOException {}
    
    public void write(byte[] param1ArrayOfbyte) throws IOException {
      this.raFile.write(param1ArrayOfbyte);
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      this.raFile.write(param1ArrayOfbyte, param1Int1, param1Int2);
    }
  }
}
