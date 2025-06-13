package retrofit2;

import I丨L.I11L;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import l丨Li1LL.IL1Iii;
import l丨Li1LL.ILil;
import okhttp3.Request;

public final class DefaultCallAdapterFactory extends CallAdapter.Factory {
  @Nullable
  private final Executor callbackExecutor;
  
  public DefaultCallAdapterFactory(@Nullable Executor paramExecutor) {
    this.callbackExecutor = paramExecutor;
  }
  
  @Nullable
  public CallAdapter<?, ?> get(Type paramType, Annotation[] paramArrayOfAnnotation, Retrofit paramRetrofit) {
    Class<?> clazz = CallAdapter.Factory.getRawType(paramType);
    paramRetrofit = null;
    if (clazz != Call.class)
      return null; 
    if (paramType instanceof ParameterizedType) {
      final Executor executor;
      final Type responseType = Utils.getParameterUpperBound(0, (ParameterizedType)paramType);
      if (Utils.isAnnotationPresent(paramArrayOfAnnotation, (Class)SkipCallbackExecutor.class)) {
        Retrofit retrofit = paramRetrofit;
      } else {
        executor = this.callbackExecutor;
      } 
      return new CallAdapter<Object, Call<?>>() {
          public final DefaultCallAdapterFactory this$0;
          
          public final Executor val$executor;
          
          public final Type val$responseType;
          
          public Call<Object> adapt(Call<Object> param1Call) {
            Executor executor = executor;
            if (executor != null)
              param1Call = new DefaultCallAdapterFactory.ExecutorCallbackCall(executor, param1Call); 
            return param1Call;
          }
          
          public Type responseType() {
            return responseType;
          }
        };
    } 
    throw new IllegalArgumentException("Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
  }
  
  public static final class ExecutorCallbackCall<T> implements Call<T> {
    public final Executor callbackExecutor;
    
    public final Call<T> delegate;
    
    public ExecutorCallbackCall(Executor param1Executor, Call<T> param1Call) {
      this.callbackExecutor = param1Executor;
      this.delegate = param1Call;
    }
    
    public void cancel() {
      this.delegate.cancel();
    }
    
    public Call<T> clone() {
      return new ExecutorCallbackCall(this.callbackExecutor, this.delegate.clone());
    }
    
    public void enqueue(final Callback<T> callback) {
      Objects.requireNonNull(callback, "callback == null");
      this.delegate.enqueue(new Callback<T>() {
            public final DefaultCallAdapterFactory.ExecutorCallbackCall this$0;
            
            public final Callback val$callback;
            
            public void onFailure(Call<T> param2Call, Throwable param2Throwable) {
              DefaultCallAdapterFactory.ExecutorCallbackCall.this.callbackExecutor.execute(new IL1Iii(this, callback, param2Throwable));
            }
            
            public void onResponse(Call<T> param2Call, Response<T> param2Response) {
              DefaultCallAdapterFactory.ExecutorCallbackCall.this.callbackExecutor.execute(new ILil(this, callback, param2Response));
            }
          });
    }
    
    public Response<T> execute() throws IOException {
      return this.delegate.execute();
    }
    
    public boolean isCanceled() {
      return this.delegate.isCanceled();
    }
    
    public boolean isExecuted() {
      return this.delegate.isExecuted();
    }
    
    public Request request() {
      return this.delegate.request();
    }
    
    public I11L timeout() {
      return this.delegate.timeout();
    }
  }
  
  public class null implements Callback<T> {
    public final DefaultCallAdapterFactory.ExecutorCallbackCall this$0;
    
    public final Callback val$callback;
    
    public void onFailure(Call<T> param1Call, Throwable param1Throwable) {
      this.this$0.callbackExecutor.execute(new IL1Iii(this, callback, param1Throwable));
    }
    
    public void onResponse(Call<T> param1Call, Response<T> param1Response) {
      this.this$0.callbackExecutor.execute(new ILil(this, callback, param1Response));
    }
  }
}
