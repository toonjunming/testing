package retrofit2;

import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

public final class Response<T> {
  @Nullable
  private final T body;
  
  @Nullable
  private final ResponseBody errorBody;
  
  private final okhttp3.Response rawResponse;
  
  private Response(okhttp3.Response paramResponse, @Nullable T paramT, @Nullable ResponseBody paramResponseBody) {
    this.rawResponse = paramResponse;
    this.body = paramT;
    this.errorBody = paramResponseBody;
  }
  
  public static <T> Response<T> error(int paramInt, ResponseBody paramResponseBody) {
    Objects.requireNonNull(paramResponseBody, "body == null");
    if (paramInt >= 400)
      return error(paramResponseBody, (new okhttp3.Response.Builder()).body(new OkHttpCall.NoContentResponseBody(paramResponseBody.contentType(), paramResponseBody.contentLength())).code(paramInt).message("Response.error()").protocol(Protocol.HTTP_1_1).request((new Request.Builder()).url("http://localhost/").build()).build()); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("code < 400: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static <T> Response<T> error(ResponseBody paramResponseBody, okhttp3.Response paramResponse) {
    Objects.requireNonNull(paramResponseBody, "body == null");
    Objects.requireNonNull(paramResponse, "rawResponse == null");
    if (!paramResponse.isSuccessful())
      return new Response<T>(paramResponse, null, paramResponseBody); 
    throw new IllegalArgumentException("rawResponse should not be successful response");
  }
  
  public static <T> Response<T> success(int paramInt, @Nullable T paramT) {
    if (paramInt >= 200 && paramInt < 300)
      return success(paramT, (new okhttp3.Response.Builder()).code(paramInt).message("Response.success()").protocol(Protocol.HTTP_1_1).request((new Request.Builder()).url("http://localhost/").build()).build()); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("code < 200 or >= 300: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static <T> Response<T> success(@Nullable T paramT) {
    return success(paramT, (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1).request((new Request.Builder()).url("http://localhost/").build()).build());
  }
  
  public static <T> Response<T> success(@Nullable T paramT, Headers paramHeaders) {
    Objects.requireNonNull(paramHeaders, "headers == null");
    return success(paramT, (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1).headers(paramHeaders).request((new Request.Builder()).url("http://localhost/").build()).build());
  }
  
  public static <T> Response<T> success(@Nullable T paramT, okhttp3.Response paramResponse) {
    Objects.requireNonNull(paramResponse, "rawResponse == null");
    if (paramResponse.isSuccessful())
      return new Response<T>(paramResponse, paramT, null); 
    throw new IllegalArgumentException("rawResponse must be successful response");
  }
  
  @Nullable
  public T body() {
    return this.body;
  }
  
  public int code() {
    return this.rawResponse.code();
  }
  
  @Nullable
  public ResponseBody errorBody() {
    return this.errorBody;
  }
  
  public Headers headers() {
    return this.rawResponse.headers();
  }
  
  public boolean isSuccessful() {
    return this.rawResponse.isSuccessful();
  }
  
  public String message() {
    return this.rawResponse.message();
  }
  
  public okhttp3.Response raw() {
    return this.rawResponse;
  }
  
  public String toString() {
    return this.rawResponse.toString();
  }
}
