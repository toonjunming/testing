package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Proxy;
import javax.annotation.Nullable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.Transmitter;

public final class RetryAndFollowUpInterceptor implements Interceptor {
  private static final int MAX_FOLLOW_UPS = 20;
  
  private final OkHttpClient client;
  
  public RetryAndFollowUpInterceptor(OkHttpClient paramOkHttpClient) {
    this.client = paramOkHttpClient;
  }
  
  private Request followUpRequest(Response paramResponse, @Nullable Route paramRoute) throws IOException {
    if (paramResponse != null) {
      int i = paramResponse.code();
      String str2 = paramResponse.request().method();
      Proxy proxy = null;
      if (i != 307 && i != 308) {
        RequestBody requestBody;
        if (i != 401) {
          if (i != 503) {
            if (i != 407) {
              if (i != 408) {
                switch (i) {
                  default:
                    return null;
                  case 300:
                  case 301:
                  case 302:
                  case 303:
                    break;
                } 
              } else {
                if (!this.client.retryOnConnectionFailure())
                  return null; 
                requestBody = paramResponse.request().body();
                return (requestBody != null && requestBody.isOneShot()) ? null : ((paramResponse.priorResponse() != null && paramResponse.priorResponse().code() == 408) ? null : ((retryAfter(paramResponse, 0) > 0) ? null : paramResponse.request()));
              } 
            } else {
              if (requestBody != null) {
                proxy = requestBody.proxy();
              } else {
                proxy = this.client.proxy();
              } 
              if (proxy.type() == Proxy.Type.HTTP)
                return this.client.proxyAuthenticator().authenticate((Route)requestBody, paramResponse); 
              throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            } 
          } else {
            return (paramResponse.priorResponse() != null && paramResponse.priorResponse().code() == 503) ? null : ((retryAfter(paramResponse, 2147483647) == 0) ? paramResponse.request() : null);
          } 
        } else {
          return this.client.authenticator().authenticate((Route)requestBody, paramResponse);
        } 
      } else if (!str2.equals("GET") && !str2.equals("HEAD")) {
        return null;
      } 
      if (!this.client.followRedirects())
        return null; 
      String str1 = paramResponse.header("Location");
      if (str1 == null)
        return null; 
      HttpUrl httpUrl = paramResponse.request().url().resolve(str1);
      if (httpUrl == null)
        return null; 
      if (!httpUrl.scheme().equals(paramResponse.request().url().scheme()) && !this.client.followSslRedirects())
        return null; 
      Request.Builder builder = paramResponse.request().newBuilder();
      if (HttpMethod.permitsRequestBody(str2)) {
        boolean bool = HttpMethod.redirectsWithBody(str2);
        if (HttpMethod.redirectsToGet(str2)) {
          builder.method("GET", null);
        } else {
          RequestBody requestBody;
          Proxy proxy1 = proxy;
          if (bool)
            requestBody = paramResponse.request().body(); 
          builder.method(str2, requestBody);
        } 
        if (!bool) {
          builder.removeHeader("Transfer-Encoding");
          builder.removeHeader("Content-Length");
          builder.removeHeader("Content-Type");
        } 
      } 
      if (!Util.sameConnection(paramResponse.request().url(), httpUrl))
        builder.removeHeader("Authorization"); 
      return builder.url(httpUrl).build();
    } 
    throw new IllegalStateException();
  }
  
  private boolean isRecoverable(IOException paramIOException, boolean paramBoolean) {
    boolean bool = paramIOException instanceof ProtocolException;
    boolean bool1 = false;
    if (bool)
      return false; 
    if (paramIOException instanceof java.io.InterruptedIOException) {
      bool = bool1;
      if (paramIOException instanceof java.net.SocketTimeoutException) {
        bool = bool1;
        if (!paramBoolean)
          bool = true; 
      } 
      return bool;
    } 
    return (paramIOException instanceof javax.net.ssl.SSLHandshakeException && paramIOException.getCause() instanceof java.security.cert.CertificateException) ? false : (!(paramIOException instanceof javax.net.ssl.SSLPeerUnverifiedException));
  }
  
  private boolean recover(IOException paramIOException, Transmitter paramTransmitter, boolean paramBoolean, Request paramRequest) {
    return !this.client.retryOnConnectionFailure() ? false : ((paramBoolean && requestIsOneShot(paramIOException, paramRequest)) ? false : (!isRecoverable(paramIOException, paramBoolean) ? false : (!!paramTransmitter.canRetry())));
  }
  
  private boolean requestIsOneShot(IOException paramIOException, Request paramRequest) {
    boolean bool;
    RequestBody requestBody = paramRequest.body();
    if ((requestBody != null && requestBody.isOneShot()) || paramIOException instanceof java.io.FileNotFoundException) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private int retryAfter(Response paramResponse, int paramInt) {
    String str = paramResponse.header("Retry-After");
    return (str == null) ? paramInt : (str.matches("\\d+") ? Integer.valueOf(str).intValue() : Integer.MAX_VALUE);
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    Request request = paramChain.request();
    RealInterceptorChain realInterceptorChain = (RealInterceptorChain)paramChain;
    Transmitter transmitter = realInterceptorChain.transmitter();
    Response response = null;
    byte b = 0;
    while (true) {
      transmitter.prepareToConnect(request);
      if (!transmitter.isCanceled()) {
        try {
          Response response2 = realInterceptorChain.proceed(request, transmitter, null);
          Response response1 = response2;
          if (response != null)
            response1 = response2.newBuilder().priorResponse(response.newBuilder().body(null).build()).build(); 
          Exchange exchange = Internal.instance.exchange(response1);
          if (exchange != null) {
            Route route = exchange.connection().route();
          } else {
            response = null;
          } 
          request = followUpRequest(response1, (Route)response);
          if (request == null) {
            if (exchange != null && exchange.isDuplex())
              transmitter.timeoutEarlyExit(); 
            return response1;
          } 
          RequestBody requestBody = request.body();
          if (requestBody != null && requestBody.isOneShot())
            return response1; 
          Util.closeQuietly(response1.body());
          if (transmitter.hasExchange())
            exchange.detachWithViolence(); 
          if (++b <= 20) {
            Response response3 = response1;
            continue;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Too many follow-up requests: ");
          stringBuilder.append(b);
          throw new ProtocolException(stringBuilder.toString());
        } catch (RouteException routeException) {
          boolean bool = recover(routeException.getLastConnectException(), transmitter, false, request);
          if (!bool)
            throw routeException.getFirstConnectException(); 
        } catch (IOException iOException) {
          boolean bool;
          if (!(iOException instanceof okhttp3.internal.http2.ConnectionShutdownException)) {
            bool = true;
          } else {
            bool = false;
          } 
          if (!recover(iOException, transmitter, bool, request))
            throw iOException; 
        } finally {}
        transmitter.exchangeDoneDueToException();
        continue;
      } 
      throw new IOException("Canceled");
    } 
  }
}
