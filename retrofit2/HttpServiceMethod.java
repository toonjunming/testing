package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import kotlin.coroutines.Continuation;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class HttpServiceMethod<ResponseT, ReturnT> extends ServiceMethod<ReturnT> {
  private final Call.Factory callFactory;
  
  private final RequestFactory requestFactory;
  
  private final Converter<ResponseBody, ResponseT> responseConverter;
  
  public HttpServiceMethod(RequestFactory paramRequestFactory, Call.Factory paramFactory, Converter<ResponseBody, ResponseT> paramConverter) {
    this.requestFactory = paramRequestFactory;
    this.callFactory = paramFactory;
    this.responseConverter = paramConverter;
  }
  
  private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(Retrofit paramRetrofit, Method paramMethod, Type paramType, Annotation[] paramArrayOfAnnotation) {
    try {
      return (CallAdapter)paramRetrofit.callAdapter(paramType, paramArrayOfAnnotation);
    } catch (RuntimeException runtimeException) {
      throw Utils.methodError(paramMethod, runtimeException, "Unable to create call adapter for %s", new Object[] { paramType });
    } 
  }
  
  private static <ResponseT> Converter<ResponseBody, ResponseT> createResponseConverter(Retrofit paramRetrofit, Method paramMethod, Type paramType) {
    Annotation[] arrayOfAnnotation = paramMethod.getAnnotations();
    try {
      return (Converter)paramRetrofit.responseBodyConverter(paramType, arrayOfAnnotation);
    } catch (RuntimeException runtimeException) {
      throw Utils.methodError(paramMethod, runtimeException, "Unable to create converter for %s", new Object[] { paramType });
    } 
  }
  
  public static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(Retrofit paramRetrofit, Method paramMethod, RequestFactory paramRequestFactory) {
    Converter<ResponseBody, ?> converter;
    boolean bool;
    Type type1;
    boolean bool1 = paramRequestFactory.isKotlinSuspendFunction;
    Annotation[] arrayOfAnnotation = paramMethod.getAnnotations();
    if (bool1) {
      Type[] arrayOfType = paramMethod.getGenericParameterTypes();
      type1 = Utils.getParameterLowerBound(0, (ParameterizedType)arrayOfType[arrayOfType.length - 1]);
      if (Utils.getRawType(type1) == Response.class && type1 instanceof ParameterizedType) {
        type1 = Utils.getParameterUpperBound(0, (ParameterizedType)type1);
        bool = true;
      } else {
        bool = false;
      } 
      type1 = new Utils.ParameterizedTypeImpl(null, Call.class, new Type[] { type1 });
      arrayOfAnnotation = SkipCallbackExecutorImpl.ensurePresent(arrayOfAnnotation);
    } else {
      type1 = paramMethod.getGenericReturnType();
      bool = false;
    } 
    CallAdapter<?, ?> callAdapter = createCallAdapter(paramRetrofit, paramMethod, type1, arrayOfAnnotation);
    Type type2 = callAdapter.responseType();
    if (type2 != Response.class) {
      if (type2 != Response.class) {
        if (!paramRequestFactory.httpMethod.equals("HEAD") || Void.class.equals(type2)) {
          converter = createResponseConverter(paramRetrofit, paramMethod, type2);
          Call.Factory factory = paramRetrofit.callFactory;
          return (HttpServiceMethod<ResponseT, ReturnT>)(!bool1 ? new CallAdapted<ResponseT, ReturnT>(paramRequestFactory, factory, (Converter)converter, (CallAdapter)callAdapter) : (bool ? new SuspendForResponse<ResponseT>(paramRequestFactory, factory, (Converter)converter, (CallAdapter)callAdapter) : new SuspendForBody<ResponseT>(paramRequestFactory, factory, (Converter)converter, (CallAdapter)callAdapter, false)));
        } 
        throw Utils.methodError(converter, "HEAD method must use Void as response type.", new Object[0]);
      } 
      throw Utils.methodError(converter, "Response must include generic type (e.g., Response<String>)", new Object[0]);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("'");
    stringBuilder.append(Utils.getRawType(type2).getName());
    stringBuilder.append("' is not a valid response body type. Did you mean ResponseBody?");
    throw Utils.methodError(converter, stringBuilder.toString(), new Object[0]);
  }
  
  @Nullable
  public abstract ReturnT adapt(Call<ResponseT> paramCall, Object[] paramArrayOfObject);
  
  @Nullable
  public final ReturnT invoke(Object[] paramArrayOfObject) {
    return adapt(new OkHttpCall<ResponseT>(this.requestFactory, paramArrayOfObject, this.callFactory, this.responseConverter), paramArrayOfObject);
  }
  
  public static final class CallAdapted<ResponseT, ReturnT> extends HttpServiceMethod<ResponseT, ReturnT> {
    private final CallAdapter<ResponseT, ReturnT> callAdapter;
    
    public CallAdapted(RequestFactory param1RequestFactory, Call.Factory param1Factory, Converter<ResponseBody, ResponseT> param1Converter, CallAdapter<ResponseT, ReturnT> param1CallAdapter) {
      super(param1RequestFactory, param1Factory, param1Converter);
      this.callAdapter = param1CallAdapter;
    }
    
    public ReturnT adapt(Call<ResponseT> param1Call, Object[] param1ArrayOfObject) {
      return this.callAdapter.adapt(param1Call);
    }
  }
  
  public static final class SuspendForBody<ResponseT> extends HttpServiceMethod<ResponseT, Object> {
    private final CallAdapter<ResponseT, Call<ResponseT>> callAdapter;
    
    private final boolean isNullable;
    
    public SuspendForBody(RequestFactory param1RequestFactory, Call.Factory param1Factory, Converter<ResponseBody, ResponseT> param1Converter, CallAdapter<ResponseT, Call<ResponseT>> param1CallAdapter, boolean param1Boolean) {
      super(param1RequestFactory, param1Factory, param1Converter);
      this.callAdapter = param1CallAdapter;
      this.isNullable = param1Boolean;
    }
    
    public Object adapt(Call<ResponseT> param1Call, Object[] param1ArrayOfObject) {
      param1Call = this.callAdapter.adapt(param1Call);
      Continuation<? super ResponseT> continuation = (Continuation)param1ArrayOfObject[param1ArrayOfObject.length - 1];
      try {
        Object object;
        if (this.isNullable) {
          object = KotlinExtensions.awaitNullable(param1Call, continuation);
        } else {
          object = KotlinExtensions.await((Call<ResponseT>)object, continuation);
        } 
        return object;
      } catch (Exception exception) {
        return KotlinExtensions.suspendAndThrow(exception, continuation);
      } 
    }
  }
  
  public static final class SuspendForResponse<ResponseT> extends HttpServiceMethod<ResponseT, Object> {
    private final CallAdapter<ResponseT, Call<ResponseT>> callAdapter;
    
    public SuspendForResponse(RequestFactory param1RequestFactory, Call.Factory param1Factory, Converter<ResponseBody, ResponseT> param1Converter, CallAdapter<ResponseT, Call<ResponseT>> param1CallAdapter) {
      super(param1RequestFactory, param1Factory, param1Converter);
      this.callAdapter = param1CallAdapter;
    }
    
    public Object adapt(Call<ResponseT> param1Call, Object[] param1ArrayOfObject) {
      Call<?> call = this.callAdapter.adapt(param1Call);
      Continuation<? super Response<?>> continuation = (Continuation)param1ArrayOfObject[param1ArrayOfObject.length - 1];
      try {
        return KotlinExtensions.awaitResponse(call, continuation);
      } catch (Exception exception) {
        return KotlinExtensions.suspendAndThrow(exception, continuation);
      } 
    }
  }
}
