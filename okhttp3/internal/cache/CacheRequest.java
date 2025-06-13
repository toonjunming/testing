package okhttp3.internal.cache;

import I丨L.I11li1;
import java.io.IOException;

public interface CacheRequest {
  void abort();
  
  I11li1 body() throws IOException;
}
