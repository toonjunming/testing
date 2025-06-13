package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class URLDataSource implements DataSource {
  private URL url = null;
  
  private URLConnection url_conn = null;
  
  public URLDataSource(URL paramURL) {
    this.url = paramURL;
  }
  
  public String getContentType() {
    String str;
    try {
      if (this.url_conn == null)
        this.url_conn = this.url.openConnection(); 
    } catch (IOException iOException) {}
    URLConnection uRLConnection1 = this.url_conn;
    if (uRLConnection1 != null) {
      String str1 = uRLConnection1.getContentType();
    } else {
      uRLConnection1 = null;
    } 
    URLConnection uRLConnection2 = uRLConnection1;
    if (uRLConnection1 == null)
      str = "application/octet-stream"; 
    return str;
  }
  
  public InputStream getInputStream() throws IOException {
    return this.url.openStream();
  }
  
  public String getName() {
    return this.url.getFile();
  }
  
  public OutputStream getOutputStream() throws IOException {
    URLConnection uRLConnection = this.url.openConnection();
    this.url_conn = uRLConnection;
    if (uRLConnection != null) {
      uRLConnection.setDoOutput(true);
      return this.url_conn.getOutputStream();
    } 
    return null;
  }
  
  public URL getURL() {
    return this.url;
  }
}
