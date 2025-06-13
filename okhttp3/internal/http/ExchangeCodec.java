package okhttp3.internal.http;

import I丨L.I11li1;
import I丨L.丨lL;
import java.io.IOException;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;

public interface ExchangeCodec {
  public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;
  
  void cancel();
  
  RealConnection connection();
  
  I11li1 createRequestBody(Request paramRequest, long paramLong) throws IOException;
  
  void finishRequest() throws IOException;
  
  void flushRequest() throws IOException;
  
  丨lL openResponseBodySource(Response paramResponse) throws IOException;
  
  @Nullable
  Response.Builder readResponseHeaders(boolean paramBoolean) throws IOException;
  
  long reportedContentLength(Response paramResponse) throws IOException;
  
  Headers trailers() throws IOException;
  
  void writeRequestHeaders(Request paramRequest) throws IOException;
}
