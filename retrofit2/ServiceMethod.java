package retrofit2;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

public abstract class ServiceMethod<T> {
  public static <T> ServiceMethod<T> parseAnnotations(Retrofit paramRetrofit, Method paramMethod) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(paramRetrofit, paramMethod);
    Type type = paramMethod.getGenericReturnType();
    if (!Utils.hasUnresolvableType(type)) {
      if (type != void.class)
        return HttpServiceMethod.parseAnnotations(paramRetrofit, paramMethod, requestFactory); 
      throw Utils.methodError(paramMethod, "Service methods cannot return void.", new Object[0]);
    } 
    throw Utils.methodError(paramMethod, "Method return type must not include a type variable or wildcard: %s", new Object[] { type });
  }
  
  @Nullable
  public abstract T invoke(Object[] paramArrayOfObject);
}
