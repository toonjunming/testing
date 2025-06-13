package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import javax.annotation.Nullable;
import okhttp3.ResponseBody;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

@IgnoreJRERequirement
public final class OptionalConverterFactory extends Converter.Factory {
  public static final Converter.Factory INSTANCE = new OptionalConverterFactory();
  
  @Nullable
  public Converter<ResponseBody, ?> responseBodyConverter(Type paramType, Annotation[] paramArrayOfAnnotation, Retrofit paramRetrofit) {
    return (Converter.Factory.getRawType(paramType) != Optional.class) ? null : new OptionalConverter(paramRetrofit.responseBodyConverter(Converter.Factory.getParameterUpperBound(0, (ParameterizedType)paramType), paramArrayOfAnnotation));
  }
  
  @IgnoreJRERequirement
  public static final class OptionalConverter<T> implements Converter<ResponseBody, Optional<T>> {
    public final Converter<ResponseBody, T> delegate;
    
    public OptionalConverter(Converter<ResponseBody, T> param1Converter) {
      this.delegate = param1Converter;
    }
    
    public Optional<T> convert(ResponseBody param1ResponseBody) throws IOException {
      return Optional.ofNullable(this.delegate.convert(param1ResponseBody));
    }
  }
}
