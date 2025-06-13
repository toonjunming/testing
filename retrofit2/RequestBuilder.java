package retrofit2;

import I丨L.I1I;
import I丨L.I丨L;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class RequestBuilder {
  private static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  private static final String PATH_SEGMENT_ALWAYS_ENCODE_SET = " \"<>^`{}|\\?#";
  
  private static final Pattern PATH_TRAVERSAL = Pattern.compile("(.*/)?(\\.|%2e|%2E){1,2}(/.*)?");
  
  private final HttpUrl baseUrl;
  
  @Nullable
  private RequestBody body;
  
  @Nullable
  private MediaType contentType;
  
  @Nullable
  private FormBody.Builder formBuilder;
  
  private final boolean hasBody;
  
  private final Headers.Builder headersBuilder;
  
  private final String method;
  
  @Nullable
  private MultipartBody.Builder multipartBuilder;
  
  @Nullable
  private String relativeUrl;
  
  private final Request.Builder requestBuilder;
  
  @Nullable
  private HttpUrl.Builder urlBuilder;
  
  public RequestBuilder(String paramString1, HttpUrl paramHttpUrl, @Nullable String paramString2, @Nullable Headers paramHeaders, @Nullable MediaType paramMediaType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    this.method = paramString1;
    this.baseUrl = paramHttpUrl;
    this.relativeUrl = paramString2;
    this.requestBuilder = new Request.Builder();
    this.contentType = paramMediaType;
    this.hasBody = paramBoolean1;
    if (paramHeaders != null) {
      this.headersBuilder = paramHeaders.newBuilder();
    } else {
      this.headersBuilder = new Headers.Builder();
    } 
    if (paramBoolean2) {
      this.formBuilder = new FormBody.Builder();
    } else if (paramBoolean3) {
      MultipartBody.Builder builder = new MultipartBody.Builder();
      this.multipartBuilder = builder;
      builder.setType(MultipartBody.FORM);
    } 
  }
  
  private static String canonicalizeForPath(String paramString, boolean paramBoolean) {
    String str;
    int j = paramString.length();
    int i = 0;
    while (true) {
      str = paramString;
      if (i < j) {
        int k = paramString.codePointAt(i);
        if (k < 32 || k >= 127 || " \"<>^`{}|\\?#".indexOf(k) != -1 || (!paramBoolean && (k == 47 || k == 37))) {
          I1I i1I = new I1I();
          i1I.l1Lll(paramString, 0, i);
          canonicalizeForPath(i1I, paramString, i, j, paramBoolean);
          str = i1I.iIi1();
          break;
        } 
        i += Character.charCount(k);
        continue;
      } 
      break;
    } 
    return str;
  }
  
  private static void canonicalizeForPath(I1I paramI1I, String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    for (Object object = null; paramInt1 < paramInt2; object = SYNTHETIC_LOCAL_VARIABLE_9) {
      int i = paramString.codePointAt(paramInt1);
      if (paramBoolean) {
        Object object1 = object;
        if (i != 9) {
          object1 = object;
          if (i != 10) {
            object1 = object;
            if (i != 12) {
              if (i == 13) {
                object1 = object;
                continue;
              } 
            } else {
              continue;
            } 
          } else {
            continue;
          } 
        } else {
          continue;
        } 
      } 
      if (i < 32 || i >= 127 || " \"<>^`{}|\\?#".indexOf(i) != -1 || (!paramBoolean && (i == 47 || i == 37))) {
        Object object1 = object;
        if (object == null)
          object1 = new I1I(); 
        object1.I1IILIIL(i);
        while (true) {
          Object object2 = object1;
          if (!object1.l丨Li1LL()) {
            int j = object1.readByte() & 0xFF;
            paramI1I.iI(37);
            object = HEX_DIGITS;
            paramI1I.iI(object[j >> 4 & 0xF]);
            paramI1I.iI(object[j & 0xF]);
            continue;
          } 
          break;
        } 
      } else {
        paramI1I.I1IILIIL(i);
        Object object1 = object;
      } 
      continue;
      paramInt1 += Character.charCount(SYNTHETIC_LOCAL_VARIABLE_6);
    } 
  }
  
  public void addFormField(String paramString1, String paramString2, boolean paramBoolean) {
    if (paramBoolean) {
      this.formBuilder.addEncoded(paramString1, paramString2);
    } else {
      this.formBuilder.add(paramString1, paramString2);
    } 
  }
  
  public void addHeader(String paramString1, String paramString2) {
    StringBuilder stringBuilder;
    if ("Content-Type".equalsIgnoreCase(paramString1)) {
      try {
        this.contentType = MediaType.get(paramString2);
      } catch (IllegalArgumentException illegalArgumentException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Malformed content type: ");
        stringBuilder.append(paramString2);
        throw new IllegalArgumentException(stringBuilder.toString(), illegalArgumentException);
      } 
    } else {
      this.headersBuilder.add((String)stringBuilder, paramString2);
    } 
  }
  
  public void addHeaders(Headers paramHeaders) {
    this.headersBuilder.addAll(paramHeaders);
  }
  
  public void addPart(Headers paramHeaders, RequestBody paramRequestBody) {
    this.multipartBuilder.addPart(paramHeaders, paramRequestBody);
  }
  
  public void addPart(MultipartBody.Part paramPart) {
    this.multipartBuilder.addPart(paramPart);
  }
  
  public void addPathParam(String paramString1, String paramString2, boolean paramBoolean) {
    if (this.relativeUrl != null) {
      String str2 = canonicalizeForPath(paramString2, paramBoolean);
      String str1 = this.relativeUrl;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("{");
      stringBuilder2.append(paramString1);
      stringBuilder2.append("}");
      paramString1 = str1.replace(stringBuilder2.toString(), str2);
      if (!PATH_TRAVERSAL.matcher(paramString1).matches()) {
        this.relativeUrl = paramString1;
        return;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("@Path parameters shouldn't perform path traversal ('.' or '..'): ");
      stringBuilder1.append(paramString2);
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    throw new AssertionError();
  }
  
  public void addQueryParam(String paramString1, @Nullable String paramString2, boolean paramBoolean) {
    StringBuilder stringBuilder;
    String str = this.relativeUrl;
    if (str != null) {
      HttpUrl.Builder builder = this.baseUrl.newBuilder(str);
      this.urlBuilder = builder;
      if (builder != null) {
        this.relativeUrl = null;
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Malformed URL. Base: ");
        stringBuilder.append(this.baseUrl);
        stringBuilder.append(", Relative: ");
        stringBuilder.append(this.relativeUrl);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    if (paramBoolean) {
      this.urlBuilder.addEncodedQueryParameter((String)stringBuilder, paramString2);
    } else {
      this.urlBuilder.addQueryParameter((String)stringBuilder, paramString2);
    } 
  }
  
  public <T> void addTag(Class<T> paramClass, @Nullable T paramT) {
    this.requestBuilder.tag(paramClass, paramT);
  }
  
  public Request.Builder get() {
    HttpUrl httpUrl;
    HttpUrl.Builder builder = this.urlBuilder;
    if (builder != null) {
      httpUrl = builder.build();
    } else {
      httpUrl = this.baseUrl.resolve(this.relativeUrl);
      if (httpUrl == null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Malformed URL. Base: ");
        stringBuilder.append(this.baseUrl);
        stringBuilder.append(", Relative: ");
        stringBuilder.append(this.relativeUrl);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    RequestBody requestBody2 = this.body;
    RequestBody requestBody1 = requestBody2;
    if (requestBody2 == null) {
      FormBody.Builder builder1 = this.formBuilder;
      if (builder1 != null) {
        FormBody formBody = builder1.build();
      } else {
        MultipartBody.Builder builder2 = this.multipartBuilder;
        if (builder2 != null) {
          MultipartBody multipartBody = builder2.build();
        } else {
          requestBody1 = requestBody2;
          if (this.hasBody)
            requestBody1 = RequestBody.create((MediaType)null, new byte[0]); 
        } 
      } 
    } 
    MediaType mediaType = this.contentType;
    requestBody2 = requestBody1;
    if (mediaType != null)
      if (requestBody1 != null) {
        requestBody2 = new ContentTypeOverridingRequestBody(requestBody1, mediaType);
      } else {
        this.headersBuilder.add("Content-Type", mediaType.toString());
        requestBody2 = requestBody1;
      }  
    return this.requestBuilder.url(httpUrl).headers(this.headersBuilder.build()).method(this.method, requestBody2);
  }
  
  public void setBody(RequestBody paramRequestBody) {
    this.body = paramRequestBody;
  }
  
  public void setRelativeUrl(Object paramObject) {
    this.relativeUrl = paramObject.toString();
  }
  
  public static class ContentTypeOverridingRequestBody extends RequestBody {
    private final MediaType contentType;
    
    private final RequestBody delegate;
    
    public ContentTypeOverridingRequestBody(RequestBody param1RequestBody, MediaType param1MediaType) {
      this.delegate = param1RequestBody;
      this.contentType = param1MediaType;
    }
    
    public long contentLength() throws IOException {
      return this.delegate.contentLength();
    }
    
    public MediaType contentType() {
      return this.contentType;
    }
    
    public void writeTo(I丨L param1I丨L) throws IOException {
      this.delegate.writeTo(param1I丨L);
    }
  }
}
