package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealInterceptorChain;

public final class ConnectInterceptor implements Interceptor {
  public final OkHttpClient client;
  
  public ConnectInterceptor(OkHttpClient paramOkHttpClient) {
    this.client = paramOkHttpClient;
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    RealInterceptorChain realInterceptorChain = (RealInterceptorChain)paramChain;
    Request request = realInterceptorChain.request();
    Transmitter transmitter = realInterceptorChain.transmitter();
    return realInterceptorChain.proceed(request, transmitter, transmitter.newExchange(paramChain, request.method().equals("GET") ^ true));
  }
}
