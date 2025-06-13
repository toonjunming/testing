package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataHandlerDataSource implements DataSource {
  public DataHandler dataHandler = null;
  
  public DataHandlerDataSource(DataHandler paramDataHandler) {
    this.dataHandler = paramDataHandler;
  }
  
  public String getContentType() {
    return this.dataHandler.getContentType();
  }
  
  public InputStream getInputStream() throws IOException {
    return this.dataHandler.getInputStream();
  }
  
  public String getName() {
    return this.dataHandler.getName();
  }
  
  public OutputStream getOutputStream() throws IOException {
    return this.dataHandler.getOutputStream();
  }
}
