package okhttp3.internal.http2;

import I丨L.l丨Li1LL;
import java.io.IOException;
import java.util.List;

public interface PushObserver {
  public static final PushObserver CANCEL = new PushObserver() {
      public boolean onData(int param1Int1, l丨Li1LL param1l丨Li1LL, int param1Int2, boolean param1Boolean) throws IOException {
        param1l丨Li1LL.skip(param1Int2);
        return true;
      }
      
      public boolean onHeaders(int param1Int, List<Header> param1List, boolean param1Boolean) {
        return true;
      }
      
      public boolean onRequest(int param1Int, List<Header> param1List) {
        return true;
      }
      
      public void onReset(int param1Int, ErrorCode param1ErrorCode) {}
    };
  
  boolean onData(int paramInt1, l丨Li1LL paraml丨Li1LL, int paramInt2, boolean paramBoolean) throws IOException;
  
  boolean onHeaders(int paramInt, List<Header> paramList, boolean paramBoolean);
  
  boolean onRequest(int paramInt, List<Header> paramList);
  
  void onReset(int paramInt, ErrorCode paramErrorCode);
}
