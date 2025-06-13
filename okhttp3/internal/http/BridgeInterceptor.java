package okhttp3.internal.http;

import I丨L.ILL;
import I丨L.lIi丨I;
import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;

public final class BridgeInterceptor implements Interceptor {
  private final CookieJar cookieJar;
  
  public BridgeInterceptor(CookieJar paramCookieJar) {
    this.cookieJar = paramCookieJar;
  }
  
  private String cookieHeader(List<Cookie> paramList) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      if (b > 0)
        stringBuilder.append("; "); 
      Cookie cookie = paramList.get(b);
      stringBuilder.append(cookie.name());
      stringBuilder.append('=');
      stringBuilder.append(cookie.value());
    } 
    return stringBuilder.toString();
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    Request request = paramChain.request();
    Request.Builder builder1 = request.newBuilder();
    RequestBody requestBody = request.body();
    if (requestBody != null) {
      MediaType mediaType = requestBody.contentType();
      if (mediaType != null)
        builder1.header("Content-Type", mediaType.toString()); 
      long l = requestBody.contentLength();
      if (l != -1L) {
        builder1.header("Content-Length", Long.toString(l));
        builder1.removeHeader("Transfer-Encoding");
      } else {
        builder1.header("Transfer-Encoding", "chunked");
        builder1.removeHeader("Content-Length");
      } 
    } 
    String str = request.header("Host");
    boolean bool2 = false;
    if (str == null)
      builder1.header("Host", Util.hostHeader(request.url(), false)); 
    if (request.header("Connection") == null)
      builder1.header("Connection", "Keep-Alive"); 
    boolean bool1 = bool2;
    if (request.header("Accept-Encoding") == null) {
      bool1 = bool2;
      if (request.header("Range") == null) {
        bool1 = true;
        builder1.header("Accept-Encoding", "gzip");
      } 
    } 
    List<Cookie> list = this.cookieJar.loadForRequest(request.url());
    if (!list.isEmpty())
      builder1.header("Cookie", cookieHeader(list)); 
    if (request.header("User-Agent") == null)
      builder1.header("User-Agent", Version.userAgent()); 
    Response response = paramChain.proceed(builder1.build());
    HttpHeaders.receiveHeaders(this.cookieJar, request.url(), response.headers());
    Response.Builder builder = response.newBuilder().request(request);
    if (bool1 && "gzip".equalsIgnoreCase(response.header("Content-Encoding")) && HttpHeaders.hasBody(response)) {
      ILL iLL = new ILL(response.body().source());
      builder.headers(response.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build());
      builder.body(new RealResponseBody(response.header("Content-Type"), -1L, lIi丨I.I丨L(iLL)));
    } 
    return builder.build();
  }
}
