package okhttp3;

import I丨L.iI丨LLL1;
import javax.annotation.Nullable;

public interface WebSocket {
  void cancel();
  
  boolean close(int paramInt, @Nullable String paramString);
  
  long queueSize();
  
  Request request();
  
  boolean send(iI丨LLL1 paramiI丨LLL1);
  
  boolean send(String paramString);
  
  public static interface Factory {
    WebSocket newWebSocket(Request param1Request, WebSocketListener param1WebSocketListener);
  }
}
