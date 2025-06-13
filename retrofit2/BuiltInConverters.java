package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import kotlin.Unit;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Streaming;

public final class BuiltInConverters extends Converter.Factory {
  private boolean checkForKotlinUnit = true;
  
  @Nullable
  public Converter<?, RequestBody> requestBodyConverter(Type paramType, Annotation[] paramArrayOfAnnotation1, Annotation[] paramArrayOfAnnotation2, Retrofit paramRetrofit) {
    return RequestBody.class.isAssignableFrom(Utils.getRawType(paramType)) ? RequestBodyConverter.INSTANCE : null;
  }
  
  @Nullable
  public Converter<ResponseBody, ?> responseBodyConverter(Type paramType, Annotation[] paramArrayOfAnnotation, Retrofit paramRetrofit) {
    BufferingResponseBodyConverter bufferingResponseBodyConverter;
    if (paramType == ResponseBody.class) {
      if (Utils.isAnnotationPresent(paramArrayOfAnnotation, (Class)Streaming.class)) {
        StreamingResponseBodyConverter streamingResponseBodyConverter = StreamingResponseBodyConverter.INSTANCE;
      } else {
        bufferingResponseBodyConverter = BufferingResponseBodyConverter.INSTANCE;
      } 
      return bufferingResponseBodyConverter;
    } 
    if (bufferingResponseBodyConverter == Void.class)
      return VoidResponseBodyConverter.INSTANCE; 
    if (this.checkForKotlinUnit && bufferingResponseBodyConverter == Unit.class)
      try {
        return UnitResponseBodyConverter.INSTANCE;
      } catch (NoClassDefFoundError noClassDefFoundError) {
        this.checkForKotlinUnit = false;
      }  
    return null;
  }
  
  public static final class BufferingResponseBodyConverter implements Converter<ResponseBody, ResponseBody> {
    public static final BufferingResponseBodyConverter INSTANCE = new BufferingResponseBodyConverter();
    
    public ResponseBody convert(ResponseBody param1ResponseBody) throws IOException {
      try {
        return Utils.buffer(param1ResponseBody);
      } finally {
        param1ResponseBody.close();
      } 
    }
  }
  
  public static final class RequestBodyConverter implements Converter<RequestBody, RequestBody> {
    public static final RequestBodyConverter INSTANCE = new RequestBodyConverter();
    
    public RequestBody convert(RequestBody param1RequestBody) {
      return param1RequestBody;
    }
  }
  
  public static final class StreamingResponseBodyConverter implements Converter<ResponseBody, ResponseBody> {
    public static final StreamingResponseBodyConverter INSTANCE = new StreamingResponseBodyConverter();
    
    public ResponseBody convert(ResponseBody param1ResponseBody) {
      return param1ResponseBody;
    }
  }
  
  public static final class ToStringConverter implements Converter<Object, String> {
    public static final ToStringConverter INSTANCE = new ToStringConverter();
    
    public String convert(Object param1Object) {
      return param1Object.toString();
    }
  }
  
  public static final class UnitResponseBodyConverter implements Converter<ResponseBody, Unit> {
    public static final UnitResponseBodyConverter INSTANCE = new UnitResponseBodyConverter();
    
    public Unit convert(ResponseBody param1ResponseBody) {
      param1ResponseBody.close();
      return Unit.INSTANCE;
    }
  }
  
  public static final class VoidResponseBodyConverter implements Converter<ResponseBody, Void> {
    public static final VoidResponseBodyConverter INSTANCE = new VoidResponseBodyConverter();
    
    public Void convert(ResponseBody param1ResponseBody) {
      param1ResponseBody.close();
      return null;
    }
  }
}
