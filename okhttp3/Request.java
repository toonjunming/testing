package okhttp3;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;

public final class Request {
  @Nullable
  public final RequestBody body;
  
  @Nullable
  private volatile CacheControl cacheControl;
  
  public final Headers headers;
  
  public final String method;
  
  public final Map<Class<?>, Object> tags;
  
  public final HttpUrl url;
  
  public Request(Builder paramBuilder) {
    this.url = paramBuilder.url;
    this.method = paramBuilder.method;
    this.headers = paramBuilder.headers.build();
    this.body = paramBuilder.body;
    this.tags = Util.immutableMap(paramBuilder.tags);
  }
  
  @Nullable
  public RequestBody body() {
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
  public String header(String paramString) {
    return this.headers.get(paramString);
  }
  
  public List<String> headers(String paramString) {
    return this.headers.values(paramString);
  }
  
  public Headers headers() {
    return this.headers;
  }
  
  public boolean isHttps() {
    return this.url.isHttps();
  }
  
  public String method() {
    return this.method;
  }
  
  public Builder newBuilder() {
    return new Builder(this);
  }
  
  @Nullable
  public Object tag() {
    return tag(Object.class);
  }
  
  @Nullable
  public <T> T tag(Class<? extends T> paramClass) {
    return paramClass.cast(this.tags.get(paramClass));
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Request{method=");
    stringBuilder.append(this.method);
    stringBuilder.append(", url=");
    stringBuilder.append(this.url);
    stringBuilder.append(", tags=");
    stringBuilder.append(this.tags);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public HttpUrl url() {
    return this.url;
  }
  
  public static class Builder {
    @Nullable
    public RequestBody body;
    
    public Headers.Builder headers;
    
    public String method;
    
    public Map<Class<?>, Object> tags;
    
    @Nullable
    public HttpUrl url;
    
    public Builder() {
      this.tags = Collections.emptyMap();
      this.method = "GET";
      this.headers = new Headers.Builder();
    }
    
    public Builder(Request param1Request) {
      Map<?, ?> map;
      this.tags = Collections.emptyMap();
      this.url = param1Request.url;
      this.method = param1Request.method;
      this.body = param1Request.body;
      if (param1Request.tags.isEmpty()) {
        map = Collections.emptyMap();
      } else {
        map = new LinkedHashMap<Class<?>, Object>(param1Request.tags);
      } 
      this.tags = (Map)map;
      this.headers = param1Request.headers.newBuilder();
    }
    
    public Builder addHeader(String param1String1, String param1String2) {
      this.headers.add(param1String1, param1String2);
      return this;
    }
    
    public Request build() {
      if (this.url != null)
        return new Request(this); 
      throw new IllegalStateException("url == null");
    }
    
    public Builder cacheControl(CacheControl param1CacheControl) {
      String str = param1CacheControl.toString();
      return str.isEmpty() ? removeHeader("Cache-Control") : header("Cache-Control", str);
    }
    
    public Builder delete() {
      return delete(Util.EMPTY_REQUEST);
    }
    
    public Builder delete(@Nullable RequestBody param1RequestBody) {
      return method("DELETE", param1RequestBody);
    }
    
    public Builder get() {
      return method("GET", null);
    }
    
    public Builder head() {
      return method("HEAD", null);
    }
    
    public Builder header(String param1String1, String param1String2) {
      this.headers.set(param1String1, param1String2);
      return this;
    }
    
    public Builder headers(Headers param1Headers) {
      this.headers = param1Headers.newBuilder();
      return this;
    }
    
    public Builder method(String param1String, @Nullable RequestBody param1RequestBody) {
      Objects.requireNonNull(param1String, "method == null");
      if (param1String.length() != 0) {
        if (param1RequestBody == null || HttpMethod.permitsRequestBody(param1String)) {
          if (param1RequestBody != null || !HttpMethod.requiresRequestBody(param1String)) {
            this.method = param1String;
            this.body = param1RequestBody;
            return this;
          } 
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("method ");
          stringBuilder1.append(param1String);
          stringBuilder1.append(" must have a request body.");
          throw new IllegalArgumentException(stringBuilder1.toString());
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("method ");
        stringBuilder.append(param1String);
        stringBuilder.append(" must not have a request body.");
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IllegalArgumentException("method.length() == 0");
    }
    
    public Builder patch(RequestBody param1RequestBody) {
      return method("PATCH", param1RequestBody);
    }
    
    public Builder post(RequestBody param1RequestBody) {
      return method("POST", param1RequestBody);
    }
    
    public Builder put(RequestBody param1RequestBody) {
      return method("PUT", param1RequestBody);
    }
    
    public Builder removeHeader(String param1String) {
      this.headers.removeAll(param1String);
      return this;
    }
    
    public <T> Builder tag(Class<? super T> param1Class, @Nullable T param1T) {
      Objects.requireNonNull(param1Class, "type == null");
      if (param1T == null) {
        this.tags.remove(param1Class);
      } else {
        if (this.tags.isEmpty())
          this.tags = new LinkedHashMap<Class<?>, Object>(); 
        this.tags.put(param1Class, param1Class.cast(param1T));
      } 
      return this;
    }
    
    public Builder tag(@Nullable Object param1Object) {
      return tag(Object.class, param1Object);
    }
    
    public Builder url(String param1String) {
      String str;
      Objects.requireNonNull(param1String, "url == null");
      if (param1String.regionMatches(true, 0, "ws:", 0, 3)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http:");
        stringBuilder.append(param1String.substring(3));
        str = stringBuilder.toString();
      } else {
        str = param1String;
        if (param1String.regionMatches(true, 0, "wss:", 0, 4)) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("https:");
          stringBuilder.append(param1String.substring(4));
          str = stringBuilder.toString();
        } 
      } 
      return url(HttpUrl.get(str));
    }
    
    public Builder url(URL param1URL) {
      Objects.requireNonNull(param1URL, "url == null");
      return url(HttpUrl.get(param1URL.toString()));
    }
    
    public Builder url(HttpUrl param1HttpUrl) {
      Objects.requireNonNull(param1HttpUrl, "url == null");
      this.url = param1HttpUrl;
      return this;
    }
  }
}
