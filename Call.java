package okhttp3;

import I丨L.I11L;
import java.io.IOException;

public interface Call extends Cloneable {
  void cancel();
  
  Call clone();
  
  void enqueue(Callback paramCallback);
  
  Response execute() throws IOException;
  
  boolean isCanceled();
  
  boolean isExecuted();
  
  Request request();
  
  I11L timeout();
  
  public static interface Factory {
    Call newCall(Request param1Request);
  }
}
