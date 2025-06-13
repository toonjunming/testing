package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

@IgnoreJRERequirement
public final class CompletableFutureCallAdapterFactory extends CallAdapter.Factory {
  public static final CallAdapter.Factory INSTANCE = new CompletableFutureCallAdapterFactory();
  
  @Nullable
  public CallAdapter<?, ?> get(Type paramType, Annotation[] paramArrayOfAnnotation, Retrofit paramRetrofit) {
    if (CallAdapter.Factory.getRawType(paramType) != CompletableFuture.class)
      return null; 
    if (paramType instanceof ParameterizedType) {
      paramType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType)paramType);
      if (CallAdapter.Factory.getRawType(paramType) != Response.class)
        return new BodyCallAdapter(paramType); 
      if (paramType instanceof ParameterizedType)
        return new ResponseCallAdapter(CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType)paramType)); 
      throw new IllegalStateException("Response must be parameterized as Response<Foo> or Response<? extends Foo>");
    } 
    throw new IllegalStateException("CompletableFuture return type must be parameterized as CompletableFuture<Foo> or CompletableFuture<? extends Foo>");
  }
  
  @IgnoreJRERequirement
  public static final class BodyCallAdapter<R> implements CallAdapter<R, CompletableFuture<R>> {
    private final Type responseType;
    
    public BodyCallAdapter(Type param1Type) {
      this.responseType = param1Type;
    }
    
    public CompletableFuture<R> adapt(Call<R> param1Call) {
      CompletableFutureCallAdapterFactory.CallCancelCompletableFuture<R> callCancelCompletableFuture = new CompletableFutureCallAdapterFactory.CallCancelCompletableFuture(param1Call);
      param1Call.enqueue(new BodyCallback(callCancelCompletableFuture));
      return callCancelCompletableFuture;
    }
    
    public Type responseType() {
      return this.responseType;
    }
    
    @IgnoreJRERequirement
    public class BodyCallback implements Callback<R> {
      private final CompletableFuture<R> future;
      
      public final CompletableFutureCallAdapterFactory.BodyCallAdapter this$0;
      
      public BodyCallback(CompletableFuture<R> param2CompletableFuture) {
        this.future = param2CompletableFuture;
      }
      
      public void onFailure(Call<R> param2Call, Throwable param2Throwable) {
        this.future.completeExceptionally(param2Throwable);
      }
      
      public void onResponse(Call<R> param2Call, Response<R> param2Response) {
        if (param2Response.isSuccessful()) {
          this.future.complete(param2Response.body());
        } else {
          this.future.completeExceptionally(new HttpException(param2Response));
        } 
      }
    }
  }
  
  @IgnoreJRERequirement
  public class BodyCallback implements Callback<R> {
    private final CompletableFuture<R> future;
    
    public final CompletableFutureCallAdapterFactory.BodyCallAdapter this$0;
    
    public BodyCallback(CompletableFuture<R> param1CompletableFuture) {
      this.future = param1CompletableFuture;
    }
    
    public void onFailure(Call<R> param1Call, Throwable param1Throwable) {
      this.future.completeExceptionally(param1Throwable);
    }
    
    public void onResponse(Call<R> param1Call, Response<R> param1Response) {
      if (param1Response.isSuccessful()) {
        this.future.complete(param1Response.body());
      } else {
        this.future.completeExceptionally(new HttpException(param1Response));
      } 
    }
  }
  
  @IgnoreJRERequirement
  public static final class CallCancelCompletableFuture<T> extends CompletableFuture<T> {
    private final Call<?> call;
    
    public CallCancelCompletableFuture(Call<?> param1Call) {
      this.call = param1Call;
    }
    
    public boolean cancel(boolean param1Boolean) {
      if (param1Boolean)
        this.call.cancel(); 
      return super.cancel(param1Boolean);
    }
  }
  
  @IgnoreJRERequirement
  public static final class ResponseCallAdapter<R> implements CallAdapter<R, CompletableFuture<Response<R>>> {
    private final Type responseType;
    
    public ResponseCallAdapter(Type param1Type) {
      this.responseType = param1Type;
    }
    
    public CompletableFuture<Response<R>> adapt(Call<R> param1Call) {
      CompletableFutureCallAdapterFactory.CallCancelCompletableFuture<Response<R>> callCancelCompletableFuture = new CompletableFutureCallAdapterFactory.CallCancelCompletableFuture(param1Call);
      param1Call.enqueue(new ResponseCallback(callCancelCompletableFuture));
      return callCancelCompletableFuture;
    }
    
    public Type responseType() {
      return this.responseType;
    }
    
    @IgnoreJRERequirement
    public class ResponseCallback implements Callback<R> {
      private final CompletableFuture<Response<R>> future;
      
      public final CompletableFutureCallAdapterFactory.ResponseCallAdapter this$0;
      
      public ResponseCallback(CompletableFuture<Response<R>> param2CompletableFuture) {
        this.future = param2CompletableFuture;
      }
      
      public void onFailure(Call<R> param2Call, Throwable param2Throwable) {
        this.future.completeExceptionally(param2Throwable);
      }
      
      public void onResponse(Call<R> param2Call, Response<R> param2Response) {
        this.future.complete(param2Response);
      }
    }
  }
  
  @IgnoreJRERequirement
  public class ResponseCallback implements Callback<R> {
    private final CompletableFuture<Response<R>> future;
    
    public final CompletableFutureCallAdapterFactory.ResponseCallAdapter this$0;
    
    public ResponseCallback(CompletableFuture<Response<R>> param1CompletableFuture) {
      this.future = param1CompletableFuture;
    }
    
    public void onFailure(Call<R> param1Call, Throwable param1Throwable) {
      this.future.completeExceptionally(param1Throwable);
    }
    
    public void onResponse(Call<R> param1Call, Response<R> param1Response) {
      this.future.complete(param1Response);
    }
  }
}
