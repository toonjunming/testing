package okhttp3;

import I丨L.iI丨LLL1;
import javax.annotation.Nullable;

public abstract class WebSocketListener {
  public void onClosed(WebSocket paramWebSocket, int paramInt, String paramString) {}
  
  public void onClosing(WebSocket paramWebSocket, int paramInt, String paramString) {}
  
  public void onFailure(WebSocket paramWebSocket, Throwable paramThrowable, @Nullable Response paramResponse) {}
  
  public void onMessage(WebSocket paramWebSocket, iI丨LLL1 paramiI丨LLL1) {}
  
  public void onMessage(WebSocket paramWebSocket, String paramString) {}
  
  public void onOpen(WebSocket paramWebSocket, Response paramResponse) {}
}
