package retrofit2;

import java.util.Objects;
import javax.annotation.Nullable;

public class HttpException extends RuntimeException {
  private final int code;
  
  private final String message;
  
  private final transient Response<?> response;
  
  public HttpException(Response<?> paramResponse) {
    super(getMessage(paramResponse));
    this.code = paramResponse.code();
    this.message = paramResponse.message();
    this.response = paramResponse;
  }
  
  private static String getMessage(Response<?> paramResponse) {
    Objects.requireNonNull(paramResponse, "response == null");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("HTTP ");
    stringBuilder.append(paramResponse.code());
    stringBuilder.append(" ");
    stringBuilder.append(paramResponse.message());
    return stringBuilder.toString();
  }
  
  public int code() {
    return this.code;
  }
  
  public String message() {
    return this.message;
  }
  
  @Nullable
  public Response<?> response() {
    return this.response;
  }
}
