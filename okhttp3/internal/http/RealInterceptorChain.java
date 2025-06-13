package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.Transmitter;

public final class RealInterceptorChain implements Interceptor.Chain {
  private final Call call;
  
  private int calls;
  
  private final int connectTimeout;
  
  @Nullable
  private final Exchange exchange;
  
  private final int index;
  
  private final List<Interceptor> interceptors;
  
  private final int readTimeout;
  
  private final Request request;
  
  private final Transmitter transmitter;
  
  private final int writeTimeout;
  
  public RealInterceptorChain(List<Interceptor> paramList, Transmitter paramTransmitter, @Nullable Exchange paramExchange, int paramInt1, Request paramRequest, Call paramCall, int paramInt2, int paramInt3, int paramInt4) {
    this.interceptors = paramList;
    this.transmitter = paramTransmitter;
    this.exchange = paramExchange;
    this.index = paramInt1;
    this.request = paramRequest;
    this.call = paramCall;
    this.connectTimeout = paramInt2;
    this.readTimeout = paramInt3;
    this.writeTimeout = paramInt4;
  }
  
  public Call call() {
    return this.call;
  }
  
  public int connectTimeoutMillis() {
    return this.connectTimeout;
  }
  
  @Nullable
  public Connection connection() {
    Exchange exchange = this.exchange;
    if (exchange != null) {
      RealConnection realConnection = exchange.connection();
    } else {
      exchange = null;
    } 
    return (Connection)exchange;
  }
  
  public Exchange exchange() {
    Exchange exchange = this.exchange;
    if (exchange != null)
      return exchange; 
    throw new IllegalStateException();
  }
  
  public Response proceed(Request paramRequest) throws IOException {
    return proceed(paramRequest, this.transmitter, this.exchange);
  }
  
  public Response proceed(Request paramRequest, Transmitter paramTransmitter, @Nullable Exchange paramExchange) throws IOException {
    if (this.index < this.interceptors.size()) {
      this.calls++;
      Exchange exchange = this.exchange;
      if (exchange == null || exchange.connection().supportsUrl(paramRequest.url())) {
        if (this.exchange == null || this.calls <= 1) {
          RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, paramTransmitter, paramExchange, this.index + 1, paramRequest, this.call, this.connectTimeout, this.readTimeout, this.writeTimeout);
          Interceptor interceptor = this.interceptors.get(this.index);
          Response response = interceptor.intercept(realInterceptorChain);
          if (paramExchange == null || this.index + 1 >= this.interceptors.size() || realInterceptorChain.calls == 1) {
            if (response != null) {
              if (response.body() != null)
                return response; 
              StringBuilder stringBuilder4 = new StringBuilder();
              stringBuilder4.append("interceptor ");
              stringBuilder4.append(interceptor);
              stringBuilder4.append(" returned a response with no body");
              throw new IllegalStateException(stringBuilder4.toString());
            } 
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("interceptor ");
            stringBuilder3.append(interceptor);
            stringBuilder3.append(" returned null");
            throw new NullPointerException(stringBuilder3.toString());
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("network interceptor ");
          stringBuilder2.append(interceptor);
          stringBuilder2.append(" must call proceed() exactly once");
          throw new IllegalStateException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("network interceptor ");
        stringBuilder1.append(this.interceptors.get(this.index - 1));
        stringBuilder1.append(" must call proceed() exactly once");
        throw new IllegalStateException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("network interceptor ");
      stringBuilder.append(this.interceptors.get(this.index - 1));
      stringBuilder.append(" must retain the same host and port");
      throw new IllegalStateException(stringBuilder.toString());
    } 
    throw new AssertionError();
  }
  
  public int readTimeoutMillis() {
    return this.readTimeout;
  }
  
  public Request request() {
    return this.request;
  }
  
  public Transmitter transmitter() {
    return this.transmitter;
  }
  
  public Interceptor.Chain withConnectTimeout(int paramInt, TimeUnit paramTimeUnit) {
    paramInt = Util.checkDuration("timeout", paramInt, paramTimeUnit);
    return new RealInterceptorChain(this.interceptors, this.transmitter, this.exchange, this.index, this.request, this.call, paramInt, this.readTimeout, this.writeTimeout);
  }
  
  public Interceptor.Chain withReadTimeout(int paramInt, TimeUnit paramTimeUnit) {
    paramInt = Util.checkDuration("timeout", paramInt, paramTimeUnit);
    return new RealInterceptorChain(this.interceptors, this.transmitter, this.exchange, this.index, this.request, this.call, this.connectTimeout, paramInt, this.writeTimeout);
  }
  
  public Interceptor.Chain withWriteTimeout(int paramInt, TimeUnit paramTimeUnit) {
    paramInt = Util.checkDuration("timeout", paramInt, paramTimeUnit);
    return new RealInterceptorChain(this.interceptors, this.transmitter, this.exchange, this.index, this.request, this.call, this.connectTimeout, this.readTimeout, paramInt);
  }
  
  public int writeTimeoutMillis() {
    return this.writeTimeout;
  }
}
