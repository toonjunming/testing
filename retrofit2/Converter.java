package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface Converter<F, T> {
  @Nullable
  T convert(F paramF) throws IOException;
  
  public static abstract class Factory {
    public static Type getParameterUpperBound(int param1Int, ParameterizedType param1ParameterizedType) {
      return Utils.getParameterUpperBound(param1Int, param1ParameterizedType);
    }
    
    public static Class<?> getRawType(Type param1Type) {
      return Utils.getRawType(param1Type);
    }
    
    @Nullable
    public Converter<?, RequestBody> requestBodyConverter(Type param1Type, Annotation[] param1ArrayOfAnnotation1, Annotation[] param1ArrayOfAnnotation2, Retrofit param1Retrofit) {
      return null;
    }
    
    @Nullable
    public Converter<ResponseBody, ?> responseBodyConverter(Type param1Type, Annotation[] param1ArrayOfAnnotation, Retrofit param1Retrofit) {
      return null;
    }
    
    @Nullable
    public Converter<?, String> stringConverter(Type param1Type, Annotation[] param1ArrayOfAnnotation, Retrofit param1Retrofit) {
      return null;
    }
  }
}
