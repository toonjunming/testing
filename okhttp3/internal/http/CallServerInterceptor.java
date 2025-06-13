package okhttp3.internal.http;

import I丨L.I丨L;
import I丨L.lIi丨I;
import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;

public final class CallServerInterceptor implements Interceptor {
  private final boolean forWebSocket;
  
  public CallServerInterceptor(boolean paramBoolean) {
    this.forWebSocket = paramBoolean;
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    Response.Builder builder2;
    paramChain = paramChain;
    Exchange exchange = paramChain.exchange();
    Request request = paramChain.request();
    long l = System.currentTimeMillis();
    exchange.writeRequestHeaders(request);
    boolean bool = HttpMethod.permitsRequestBody(request.method());
    I丨L i丨L = null;
    paramChain = null;
    if (bool && request.body() != null) {
      Response.Builder builder;
      boolean bool1;
      if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
        exchange.flushRequest();
        exchange.responseHeadersStart();
        builder = exchange.readResponseHeaders(true);
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (builder == null) {
        if (request.body().isDuplex()) {
          exchange.flushRequest();
          i丨L = lIi丨I.I1I(exchange.createRequestBody(request, true));
          request.body().writeTo(i丨L);
          j = bool1;
          builder2 = builder;
        } else {
          i丨L = lIi丨I.I1I(exchange.createRequestBody(request, false));
          request.body().writeTo(i丨L);
          i丨L.close();
          j = bool1;
          builder2 = builder;
        } 
      } else {
        exchange.noRequestBody();
        j = bool1;
        builder2 = builder;
        if (!exchange.connection().isMultiplexed()) {
          exchange.noNewExchangesOnConnection();
          j = bool1;
          builder2 = builder;
        } 
      } 
    } else {
      exchange.noRequestBody();
      j = 0;
    } 
    if (request.body() == null || !request.body().isDuplex())
      exchange.finishRequest(); 
    if (!j)
      exchange.responseHeadersStart(); 
    Response.Builder builder1 = builder2;
    if (builder2 == null)
      builder1 = exchange.readResponseHeaders(false); 
    Response response = builder1.request(request).handshake(exchange.connection().handshake()).sentRequestAtMillis(l).receivedResponseAtMillis(System.currentTimeMillis()).build();
    int j = response.code();
    int i = j;
    if (j == 100) {
      response = exchange.readResponseHeaders(false).request(request).handshake(exchange.connection().handshake()).sentRequestAtMillis(l).receivedResponseAtMillis(System.currentTimeMillis()).build();
      i = response.code();
    } 
    exchange.responseHeadersEnd(response);
    if (this.forWebSocket && i == 101) {
      response = response.newBuilder().body(Util.EMPTY_RESPONSE).build();
    } else {
      response = response.newBuilder().body(exchange.openResponseBody(response)).build();
    } 
    if ("close".equalsIgnoreCase(response.request().header("Connection")) || "close".equalsIgnoreCase(response.header("Connection")))
      exchange.noNewExchangesOnConnection(); 
    if ((i != 204 && i != 205) || response.body().contentLength() <= 0L)
      return response; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("HTTP ");
    stringBuilder.append(i);
    stringBuilder.append(" had non-zero Content-Length: ");
    stringBuilder.append(response.body().contentLength());
    throw new ProtocolException(stringBuilder.toString());
  }
}
