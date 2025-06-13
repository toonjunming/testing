package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

public interface CallAdapter<R, T> {
  T adapt(Call<R> paramCall);
  
  Type responseType();
  
  public static abstract class Factory {
    public static Type getParameterUpperBound(int param1Int, ParameterizedType param1ParameterizedType) {
      return Utils.getParameterUpperBound(param1Int, param1ParameterizedType);
    }
    
    public static Class<?> getRawType(Type param1Type) {
      return Utils.getRawType(param1Type);
    }
    
    @Nullable
    public abstract CallAdapter<?, ?> get(Type param1Type, Annotation[] param1ArrayOfAnnotation, Retrofit param1Retrofit);
  }
}
