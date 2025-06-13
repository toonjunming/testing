package retrofit2;

import I丨L.I11L;
import I丨L.I1I;
import I丨L.L丨1丨1丨I;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class OkHttpCall<T> implements Call<T> {
  private final Object[] args;
  
  private final Call.Factory callFactory;
  
  private volatile boolean canceled;
  
  @Nullable
  @GuardedBy("this")
  private Throwable creationFailure;
  
  @GuardedBy("this")
  private boolean executed;
  
  @Nullable
  @GuardedBy("this")
  private Call rawCall;
  
  private final RequestFactory requestFactory;
  
  private final Converter<ResponseBody, T> responseConverter;
  
  public OkHttpCall(RequestFactory paramRequestFactory, Object[] paramArrayOfObject, Call.Factory paramFactory, Converter<ResponseBody, T> paramConverter) {
    this.requestFactory = paramRequestFactory;
    this.args = paramArrayOfObject;
    this.callFactory = paramFactory;
    this.responseConverter = paramConverter;
  }
  
  private Call createRawCall() throws IOException {
    Call call = this.callFactory.newCall(this.requestFactory.create(this.args));
    Objects.requireNonNull(call, "Call.Factory returned null.");
    return call;
  }
  
  @GuardedBy("this")
  private Call getRawCall() throws IOException {
    Call call = this.rawCall;
    if (call != null)
      return call; 
    throwable = this.creationFailure;
    if (throwable != null) {
      if (!(throwable instanceof IOException)) {
        if (throwable instanceof RuntimeException)
          throw (RuntimeException)throwable; 
        throw (Error)throwable;
      } 
      throw (IOException)throwable;
    } 
    try {
      Call call1 = createRawCall();
      this.rawCall = call1;
      return call1;
    } catch (RuntimeException runtimeException) {
    
    } catch (Error error) {
    
    } catch (IOException throwable) {}
    Utils.throwIfFatal(throwable);
    this.creationFailure = throwable;
    throw throwable;
  }
  
  public void cancel() {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: putfield canceled : Z
    //   5: aload_0
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield rawCall : Lokhttp3/Call;
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: ifnull -> 24
    //   18: aload_1
    //   19: invokeinterface cancel : ()V
    //   24: return
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   7	14	25	finally
    //   26	28	25	finally
  }
  
  public OkHttpCall<T> clone() {
    return new OkHttpCall(this.requestFactory, this.args, this.callFactory, this.responseConverter);
  }
  
  public void enqueue(Callback<T> paramCallback) {
    // Byte code:
    //   0: aload_1
    //   1: ldc 'callback == null'
    //   3: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield executed : Z
    //   13: ifne -> 128
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield executed : Z
    //   21: aload_0
    //   22: getfield rawCall : Lokhttp3/Call;
    //   25: astore #4
    //   27: aload_0
    //   28: getfield creationFailure : Ljava/lang/Throwable;
    //   31: astore #5
    //   33: aload #4
    //   35: astore_2
    //   36: aload #5
    //   38: astore_3
    //   39: aload #4
    //   41: ifnonnull -> 84
    //   44: aload #4
    //   46: astore_2
    //   47: aload #5
    //   49: astore_3
    //   50: aload #5
    //   52: ifnonnull -> 84
    //   55: aload_0
    //   56: invokespecial createRawCall : ()Lokhttp3/Call;
    //   59: astore_2
    //   60: aload_0
    //   61: aload_2
    //   62: putfield rawCall : Lokhttp3/Call;
    //   65: aload #5
    //   67: astore_3
    //   68: goto -> 84
    //   71: astore_3
    //   72: aload_3
    //   73: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   76: aload_0
    //   77: aload_3
    //   78: putfield creationFailure : Ljava/lang/Throwable;
    //   81: aload #4
    //   83: astore_2
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_3
    //   87: ifnull -> 99
    //   90: aload_1
    //   91: aload_0
    //   92: aload_3
    //   93: invokeinterface onFailure : (Lretrofit2/Call;Ljava/lang/Throwable;)V
    //   98: return
    //   99: aload_0
    //   100: getfield canceled : Z
    //   103: ifeq -> 112
    //   106: aload_2
    //   107: invokeinterface cancel : ()V
    //   112: aload_2
    //   113: new retrofit2/OkHttpCall$1
    //   116: dup
    //   117: aload_0
    //   118: aload_1
    //   119: invokespecial <init> : (Lretrofit2/OkHttpCall;Lretrofit2/Callback;)V
    //   122: invokeinterface enqueue : (Lokhttp3/Callback;)V
    //   127: return
    //   128: new java/lang/IllegalStateException
    //   131: astore_1
    //   132: aload_1
    //   133: ldc 'Already executed.'
    //   135: invokespecial <init> : (Ljava/lang/String;)V
    //   138: aload_1
    //   139: athrow
    //   140: astore_1
    //   141: aload_0
    //   142: monitorexit
    //   143: aload_1
    //   144: athrow
    // Exception table:
    //   from	to	target	type
    //   9	33	140	finally
    //   55	65	71	finally
    //   72	81	140	finally
    //   84	86	140	finally
    //   128	140	140	finally
    //   141	143	140	finally
  }
  
  public Response<T> execute() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifne -> 45
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield executed : Z
    //   14: aload_0
    //   15: invokespecial getRawCall : ()Lokhttp3/Call;
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_0
    //   22: getfield canceled : Z
    //   25: ifeq -> 34
    //   28: aload_1
    //   29: invokeinterface cancel : ()V
    //   34: aload_0
    //   35: aload_1
    //   36: invokeinterface execute : ()Lokhttp3/Response;
    //   41: invokevirtual parseResponse : (Lokhttp3/Response;)Lretrofit2/Response;
    //   44: areturn
    //   45: new java/lang/IllegalStateException
    //   48: astore_1
    //   49: aload_1
    //   50: ldc 'Already executed.'
    //   52: invokespecial <init> : (Ljava/lang/String;)V
    //   55: aload_1
    //   56: athrow
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	57	finally
    //   45	57	57	finally
    //   58	60	57	finally
  }
  
  public boolean isCanceled() {
    // Byte code:
    //   0: aload_0
    //   1: getfield canceled : Z
    //   4: istore_2
    //   5: iconst_1
    //   6: istore_1
    //   7: iload_2
    //   8: ifeq -> 13
    //   11: iconst_1
    //   12: ireturn
    //   13: aload_0
    //   14: monitorenter
    //   15: aload_0
    //   16: getfield rawCall : Lokhttp3/Call;
    //   19: astore_3
    //   20: aload_3
    //   21: ifnull -> 36
    //   24: aload_3
    //   25: invokeinterface isCanceled : ()Z
    //   30: ifeq -> 36
    //   33: goto -> 38
    //   36: iconst_0
    //   37: istore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: iload_1
    //   41: ireturn
    //   42: astore_3
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_3
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   15	20	42	finally
    //   24	33	42	finally
    //   38	40	42	finally
    //   43	45	42	finally
  }
  
  public boolean isExecuted() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Response<T> parseResponse(Response paramResponse) throws IOException {
    ResponseBody responseBody = paramResponse.body();
    Response response = paramResponse.newBuilder().body(new NoContentResponseBody(responseBody.contentType(), responseBody.contentLength())).build();
    int i = response.code();
    if (i < 200 || i >= 300)
      try {
        return (Response)Response.error(Utils.buffer(responseBody), response);
      } finally {
        responseBody.close();
      }  
    if (i == 204 || i == 205) {
      responseBody.close();
      return Response.success((T)null, response);
    } 
    ExceptionCatchingResponseBody exceptionCatchingResponseBody = new ExceptionCatchingResponseBody(responseBody);
    try {
      return (Response)Response.success(this.responseConverter.convert(exceptionCatchingResponseBody), response);
    } catch (RuntimeException runtimeException) {
      exceptionCatchingResponseBody.throwIfCaught();
      throw runtimeException;
    } 
  }
  
  public Request request() {
    Exception exception;
    /* monitor enter ThisExpression{ObjectType{retrofit2/OkHttpCall}} */
    try {
      Request request = getRawCall().request();
      /* monitor exit ThisExpression{ObjectType{retrofit2/OkHttpCall}} */
      return request;
    } catch (IOException iOException) {
      exception = new RuntimeException();
      this("Unable to create request.", iOException);
      throw exception;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{retrofit2/OkHttpCall}} */
    throw exception;
  }
  
  public I11L timeout() {
    Exception exception;
    /* monitor enter ThisExpression{ObjectType{retrofit2/OkHttpCall}} */
    try {
      I11L i11L = getRawCall().timeout();
      /* monitor exit ThisExpression{ObjectType{retrofit2/OkHttpCall}} */
      return i11L;
    } catch (IOException null) {
      RuntimeException runtimeException = new RuntimeException();
      this("Unable to create call.", exception);
      throw runtimeException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{retrofit2/OkHttpCall}} */
    throw exception;
  }
  
  public static final class ExceptionCatchingResponseBody extends ResponseBody {
    private final ResponseBody delegate;
    
    private final l丨Li1LL delegateSource;
    
    @Nullable
    public IOException thrownException;
    
    public ExceptionCatchingResponseBody(ResponseBody param1ResponseBody) {
      this.delegate = param1ResponseBody;
      this.delegateSource = lIi丨I.I丨L(new L丨1丨1丨I(param1ResponseBody.source()) {
            public final OkHttpCall.ExceptionCatchingResponseBody this$0;
            
            public long read(I1I param2I1I, long param2Long) throws IOException {
              try {
                return super.read(param2I1I, param2Long);
              } catch (IOException iOException) {
                OkHttpCall.ExceptionCatchingResponseBody.this.thrownException = iOException;
                throw iOException;
              } 
            }
          });
    }
    
    public void close() {
      this.delegate.close();
    }
    
    public long contentLength() {
      return this.delegate.contentLength();
    }
    
    public MediaType contentType() {
      return this.delegate.contentType();
    }
    
    public l丨Li1LL source() {
      return this.delegateSource;
    }
    
    public void throwIfCaught() throws IOException {
      IOException iOException = this.thrownException;
      if (iOException == null)
        return; 
      throw iOException;
    }
  }
  
  public class null extends L丨1丨1丨I {
    public final OkHttpCall.ExceptionCatchingResponseBody this$0;
    
    public null(丨lL param1丨lL) {
      super(param1丨lL);
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      try {
        return super.read(param1I1I, param1Long);
      } catch (IOException iOException) {
        this.this$0.thrownException = iOException;
        throw iOException;
      } 
    }
  }
  
  public static final class NoContentResponseBody extends ResponseBody {
    private final long contentLength;
    
    @Nullable
    private final MediaType contentType;
    
    public NoContentResponseBody(@Nullable MediaType param1MediaType, long param1Long) {
      this.contentType = param1MediaType;
      this.contentLength = param1Long;
    }
    
    public long contentLength() {
      return this.contentLength;
    }
    
    public MediaType contentType() {
      return this.contentType;
    }
    
    public l丨Li1LL source() {
      throw new IllegalStateException("Cannot read raw response body of a converted body.");
    }
  }
}
