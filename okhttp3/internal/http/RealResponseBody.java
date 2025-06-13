package okhttp3.internal.http;

import I丨L.l丨Li1LL;
import javax.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public final class RealResponseBody extends ResponseBody {
  private final long contentLength;
  
  @Nullable
  private final String contentTypeString;
  
  private final l丨Li1LL source;
  
  public RealResponseBody(@Nullable String paramString, long paramLong, l丨Li1LL paraml丨Li1LL) {
    this.contentTypeString = paramString;
    this.contentLength = paramLong;
    this.source = paraml丨Li1LL;
  }
  
  public long contentLength() {
    return this.contentLength;
  }
  
  public MediaType contentType() {
    String str = this.contentTypeString;
    if (str != null) {
      MediaType mediaType = MediaType.parse(str);
    } else {
      str = null;
    } 
    return (MediaType)str;
  }
  
  public l丨Li1LL source() {
    return this.source;
  }
}
