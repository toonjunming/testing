package okhttp3.internal.cache;

import I丨L.I11li1;
import I丨L.I1I;
import I丨L.I丨iL;
import java.io.IOException;

public class FaultHidingSink extends I丨iL {
  private boolean hasErrors;
  
  public FaultHidingSink(I11li1 paramI11li1) {
    super(paramI11li1);
  }
  
  public void close() throws IOException {
    if (this.hasErrors)
      return; 
    try {
      super.close();
    } catch (IOException iOException) {
      this.hasErrors = true;
      onException(iOException);
    } 
  }
  
  public void flush() throws IOException {
    if (this.hasErrors)
      return; 
    try {
      super.flush();
    } catch (IOException iOException) {
      this.hasErrors = true;
      onException(iOException);
    } 
  }
  
  public void onException(IOException paramIOException) {}
  
  public void write(I1I paramI1I, long paramLong) throws IOException {
    if (this.hasErrors) {
      paramI1I.skip(paramLong);
      return;
    } 
    try {
      super.write(paramI1I, paramLong);
    } catch (IOException iOException) {
      this.hasErrors = true;
      onException(iOException);
    } 
  }
}
