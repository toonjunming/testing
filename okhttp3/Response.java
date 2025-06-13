package okhttp3;

import I丨L.I1I;
import I丨L.l丨Li1LL;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.http.HttpHeaders;

public final class Response implements Closeable {
  @Nullable
  public final ResponseBody body;
  
  @Nullable
  private volatile CacheControl cacheControl;
  
  @Nullable
  public final Response cacheResponse;
  
  public final int code;
  
  @Nullable
  public final Exchange exchange;
  
  @Nullable
  public final Handshake handshake;
  
  public final Headers headers;
  
  public final String message;
  
  @Nullable
  public final Response networkResponse;
  
  @Nullable
  public final Response priorResponse;
  
  public final Protocol protocol;
  
  public final long receivedResponseAtMillis;
  
  public final Request request;
  
  public final long sentRequestAtMillis;
  
  public Response(Builder paramBuilder) {
    this.request = paramBuilder.request;
    this.protocol = paramBuilder.protocol;
    this.code = paramBuilder.code;
    this.message = paramBuilder.message;
    this.handshake = paramBuilder.handshake;
    this.headers = paramBuilder.headers.build();
    this.body = paramBuilder.body;
    this.networkResponse = paramBuilder.networkResponse;
    this.cacheResponse = paramBuilder.cacheResponse;
    this.priorResponse = paramBuilder.priorResponse;
    this.sentRequestAtMillis = paramBuilder.sentRequestAtMillis;
    this.receivedResponseAtMillis = paramBuilder.receivedResponseAtMillis;
    this.exchange = paramBuilder.exchange;
  }
  
  @Nullable
  public ResponseBody body() {
    return this.body;
  }
  
  public CacheControl cacheControl() {
    CacheControl cacheControl = this.cacheControl;
    if (cacheControl == null) {
      cacheControl = CacheControl.parse(this.headers);
      this.cacheControl = cacheControl;
    } 
    return cacheControl;
  }
  
  @Nullable
  public Response cacheResponse() {
    return this.cacheResponse;
  }
  
  public List<Challenge> challenges() {
    String str;
    int i = this.code;
    if (i == 401) {
      str = "WWW-Authenticate";
    } else {
      if (i == 407) {
        str = "Proxy-Authenticate";
        return HttpHeaders.parseChallenges(headers(), str);
      } 
      return Collections.emptyList();
    } 
    return HttpHeaders.parseChallenges(headers(), str);
  }
  
  public void close() {
    ResponseBody responseBody = this.body;
    if (responseBody != null) {
      responseBody.close();
      return;
    } 
    throw new IllegalStateException("response is not eligible for a body and must not be closed");
  }
  
  public int code() {
    return this.code;
  }
  
  @Nullable
  public Handshake handshake() {
    return this.handshake;
  }
  
  @Nullable
  public String header(String paramString) {
    return header(paramString, null);
  }
  
  @Nullable
  public String header(String paramString1, @Nullable String paramString2) {
    paramString1 = this.headers.get(paramString1);
    if (paramString1 != null)
      paramString2 = paramString1; 
    return paramString2;
  }
  
  public List<String> headers(String paramString) {
    return this.headers.values(paramString);
  }
  
  public Headers headers() {
    return this.headers;
  }
  
  public boolean isRedirect() {
    int i = this.code;
    if (i != 307 && i != 308)
      switch (i) {
        default:
          return false;
        case 300:
        case 301:
        case 302:
        case 303:
          break;
      }  
    return true;
  }
  
  public boolean isSuccessful() {
    boolean bool;
    int i = this.code;
    if (i >= 200 && i < 300) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String message() {
    return this.message;
  }
  
  @Nullable
  public Response networkResponse() {
    return this.networkResponse;
  }
  
  public Builder newBuilder() {
    return new Builder(this);
  }
  
  public ResponseBody peekBody(long paramLong) throws IOException {
    l丨Li1LL l丨Li1LL = this.body.source().I11L();
    I1I i1I = new I1I();
    l丨Li1LL.Lil(paramLong);
    i1I.LI丨l(l丨Li1LL, Math.min(paramLong, l丨Li1LL.Ilil().iI1i丨I()));
    return ResponseBody.create(this.body.contentType(), i1I.iI1i丨I(), i1I);
  }
  
  @Nullable
  public Response priorResponse() {
    return this.priorResponse;
  }
  
  public Protocol protocol() {
    return this.protocol;
  }
  
  public long receivedResponseAtMillis() {
    return this.receivedResponseAtMillis;
  }
  
  public Request request() {
    return this.request;
  }
  
  public long sentRequestAtMillis() {
    return this.sentRequestAtMillis;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Response{protocol=");
    stringBuilder.append(this.protocol);
    stringBuilder.append(", code=");
    stringBuilder.append(this.code);
    stringBuilder.append(", message=");
    stringBuilder.append(this.message);
    stringBuilder.append(", url=");
    stringBuilder.append(this.request.url());
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public Headers trailers() throws IOException {
    Exchange exchange = this.exchange;
    if (exchange != null)
      return exchange.trailers(); 
    throw new IllegalStateException("trailers not available");
  }
  
  public static class Builder {
    @Nullable
    public ResponseBody body;
    
    @Nullable
    public Response cacheResponse;
    
    public int code = -1;
    
    @Nullable
    public Exchange exchange;
    
    @Nullable
    public Handshake handshake;
    
    public Headers.Builder headers;
    
    public String message;
    
    @Nullable
    public Response networkResponse;
    
    @Nullable
    public Response priorResponse;
    
    @Nullable
    public Protocol protocol;
    
    public long receivedResponseAtMillis;
    
    @Nullable
    public Request request;
    
    public long sentRequestAtMillis;
    
    public Builder() {
      this.headers = new Headers.Builder();
    }
    
    public Builder(Response param1Response) {
      this.request = param1Response.request;
      this.protocol = param1Response.protocol;
      this.code = param1Response.code;
      this.message = param1Response.message;
      this.handshake = param1Response.handshake;
      this.headers = param1Response.headers.newBuilder();
      this.body = param1Response.body;
      this.networkResponse = param1Response.networkResponse;
      this.cacheResponse = param1Response.cacheResponse;
      this.priorResponse = param1Response.priorResponse;
      this.sentRequestAtMillis = param1Response.sentRequestAtMillis;
      this.receivedResponseAtMillis = param1Response.receivedResponseAtMillis;
      this.exchange = param1Response.exchange;
    }
    
    private void checkPriorResponse(Response param1Response) {
      if (param1Response.body == null)
        return; 
      throw new IllegalArgumentException("priorResponse.body != null");
    }
    
    private void checkSupportResponse(String param1String, Response param1Response) {
      if (param1Response.body == null) {
        if (param1Response.networkResponse == null) {
          if (param1Response.cacheResponse == null) {
            if (param1Response.priorResponse == null)
              return; 
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(param1String);
            stringBuilder3.append(".priorResponse != null");
            throw new IllegalArgumentException(stringBuilder3.toString());
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append(param1String);
          stringBuilder2.append(".cacheResponse != null");
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(param1String);
        stringBuilder1.append(".networkResponse != null");
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1String);
      stringBuilder.append(".body != null");
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder addHeader(String param1String1, String param1String2) {
      this.headers.add(param1String1, param1String2);
      return this;
    }
    
    public Builder body(@Nullable ResponseBody param1ResponseBody) {
      this.body = param1ResponseBody;
      return this;
    }
    
    public Response build() {
      if (this.request != null) {
        if (this.protocol != null) {
          if (this.code >= 0) {
            if (this.message != null)
              return new Response(this); 
            throw new IllegalStateException("message == null");
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("code < 0: ");
          stringBuilder.append(this.code);
          throw new IllegalStateException(stringBuilder.toString());
        } 
        throw new IllegalStateException("protocol == null");
      } 
      throw new IllegalStateException("request == null");
    }
    
    public Builder cacheResponse(@Nullable Response param1Response) {
      if (param1Response != null)
        checkSupportResponse("cacheResponse", param1Response); 
      this.cacheResponse = param1Response;
      return this;
    }
    
    public Builder code(int param1Int) {
      this.code = param1Int;
      return this;
    }
    
    public Builder handshake(@Nullable Handshake param1Handshake) {
      this.handshake = param1Handshake;
      return this;
    }
    
    public Builder header(String param1String1, String param1String2) {
      this.headers.set(param1String1, param1String2);
      return this;
    }
    
    public Builder headers(Headers param1Headers) {
      this.headers = param1Headers.newBuilder();
      return this;
    }
    
    public void initExchange(Exchange param1Exchange) {
      this.exchange = param1Exchange;
    }
    
    public Builder message(String param1String) {
      this.message = param1String;
      return this;
    }
    
    public Builder networkResponse(@Nullable Response param1Response) {
      if (param1Response != null)
        checkSupportResponse("networkResponse", param1Response); 
      this.networkResponse = param1Response;
      return this;
    }
    
    public Builder priorResponse(@Nullable Response param1Response) {
      if (param1Response != null)
        checkPriorResponse(param1Response); 
      this.priorResponse = param1Response;
      return this;
    }
    
    public Builder protocol(Protocol param1Protocol) {
      this.protocol = param1Protocol;
      return this;
    }
    
    public Builder receivedResponseAtMillis(long param1Long) {
      this.receivedResponseAtMillis = param1Long;
      return this;
    }
    
    public Builder removeHeader(String param1String) {
      this.headers.removeAll(param1String);
      return this;
    }
    
    public Builder request(Request param1Request) {
      this.request = param1Request;
      return this;
    }
    
    public Builder sentRequestAtMillis(long param1Long) {
      this.sentRequestAtMillis = param1Long;
      return this;
    }
  }
}
