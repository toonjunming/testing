package okhttp3.internal.cache;

import I丨L.I11L;
import I丨L.I11li1;
import I丨L.I1I;
import I丨L.I丨L;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.RealResponseBody;

public final class CacheInterceptor implements Interceptor {
  @Nullable
  public final InternalCache cache;
  
  public CacheInterceptor(@Nullable InternalCache paramInternalCache) {
    this.cache = paramInternalCache;
  }
  
  private Response cacheWritingResponse(final CacheRequest cacheRequest, Response paramResponse) throws IOException {
    if (cacheRequest == null)
      return paramResponse; 
    I11li1 i11li1 = cacheRequest.body();
    if (i11li1 == null)
      return paramResponse; 
    丨lL 丨lL = new 丨lL() {
        public boolean cacheRequestClosed;
        
        public final CacheInterceptor this$0;
        
        public final I丨L val$cacheBody;
        
        public final CacheRequest val$cacheRequest;
        
        public final l丨Li1LL val$source;
        
        public void close() throws IOException {
          if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
            this.cacheRequestClosed = true;
            cacheRequest.abort();
          } 
          source.close();
        }
        
        public long read(I1I param1I1I, long param1Long) throws IOException {
          try {
            param1Long = source.read(param1I1I, param1Long);
            if (param1Long == -1L) {
              if (!this.cacheRequestClosed) {
                this.cacheRequestClosed = true;
                cacheBody.close();
              } 
              return -1L;
            } 
            param1I1I.l1IIi1丨(cacheBody.IL1Iii(), param1I1I.iI1i丨I() - param1Long, param1Long);
            cacheBody.I丨iL();
            return param1Long;
          } catch (IOException iOException) {
            if (!this.cacheRequestClosed) {
              this.cacheRequestClosed = true;
              cacheRequest.abort();
            } 
            throw iOException;
          } 
        }
        
        public I11L timeout() {
          return source.timeout();
        }
      };
    String str = paramResponse.header("Content-Type");
    long l = paramResponse.body().contentLength();
    return paramResponse.newBuilder().body(new RealResponseBody(str, l, lIi丨I.I丨L(丨lL))).build();
  }
  
  private static Headers combine(Headers paramHeaders1, Headers paramHeaders2) {
    Headers.Builder builder = new Headers.Builder();
    int i = paramHeaders1.size();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      String str1 = paramHeaders1.name(b);
      String str2 = paramHeaders1.value(b);
      if ((!"Warning".equalsIgnoreCase(str1) || !str2.startsWith("1")) && (isContentSpecificHeader(str1) || !isEndToEnd(str1) || paramHeaders2.get(str1) == null))
        Internal.instance.addLenient(builder, str1, str2); 
    } 
    i = paramHeaders2.size();
    for (b = bool; b < i; b++) {
      String str = paramHeaders2.name(b);
      if (!isContentSpecificHeader(str) && isEndToEnd(str))
        Internal.instance.addLenient(builder, str, paramHeaders2.value(b)); 
    } 
    return builder.build();
  }
  
  public static boolean isContentSpecificHeader(String paramString) {
    return ("Content-Length".equalsIgnoreCase(paramString) || "Content-Encoding".equalsIgnoreCase(paramString) || "Content-Type".equalsIgnoreCase(paramString));
  }
  
  public static boolean isEndToEnd(String paramString) {
    boolean bool;
    if (!"Connection".equalsIgnoreCase(paramString) && !"Keep-Alive".equalsIgnoreCase(paramString) && !"Proxy-Authenticate".equalsIgnoreCase(paramString) && !"Proxy-Authorization".equalsIgnoreCase(paramString) && !"TE".equalsIgnoreCase(paramString) && !"Trailers".equalsIgnoreCase(paramString) && !"Transfer-Encoding".equalsIgnoreCase(paramString) && !"Upgrade".equalsIgnoreCase(paramString)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static Response stripBody(Response paramResponse) {
    Response response = paramResponse;
    if (paramResponse != null) {
      response = paramResponse;
      if (paramResponse.body() != null)
        response = paramResponse.newBuilder().body(null).build(); 
    } 
    return response;
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    InternalCache internalCache1 = this.cache;
    if (internalCache1 != null) {
      Response response1 = internalCache1.get(paramChain.request());
    } else {
      internalCache1 = null;
    } 
    CacheStrategy cacheStrategy = (new CacheStrategy.Factory(System.currentTimeMillis(), paramChain.request(), (Response)internalCache1)).get();
    Request request = cacheStrategy.networkRequest;
    Response response = cacheStrategy.cacheResponse;
    InternalCache internalCache2 = this.cache;
    if (internalCache2 != null)
      internalCache2.trackResponse(cacheStrategy); 
    if (internalCache1 != null && response == null)
      Util.closeQuietly(internalCache1.body()); 
    if (request == null && response == null)
      return (new Response.Builder()).request(paramChain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build(); 
    if (request == null)
      return response.newBuilder().cacheResponse(stripBody(response)).build(); 
    try {
      Response response1 = paramChain.proceed(request);
      if (response1 == null && internalCache1 != null)
        Util.closeQuietly(internalCache1.body()); 
      if (response != null) {
        if (response1.code() == 304) {
          Response response2 = response.newBuilder().headers(combine(response.headers(), response1.headers())).sentRequestAtMillis(response1.sentRequestAtMillis()).receivedResponseAtMillis(response1.receivedResponseAtMillis()).cacheResponse(stripBody(response)).networkResponse(stripBody(response1)).build();
          response1.body().close();
          this.cache.trackConditionalCacheHit();
          return response2;
        } 
        Util.closeQuietly(response.body());
      } 
      response1 = response1.newBuilder().cacheResponse(stripBody(response)).networkResponse(stripBody(response1)).build();
      return response1;
    } finally {
      if (iOException != null)
        Util.closeQuietly(iOException.body()); 
    } 
  }
}
