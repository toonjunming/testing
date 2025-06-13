package okhttp3;

import java.io.IOException;

public interface Callback {
  void onFailure(Call paramCall, IOException paramIOException);
  
  void onResponse(Call paramCall, Response paramResponse) throws IOException;
}
