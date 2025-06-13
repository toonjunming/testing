package retrofit2;

import java.lang.reflect.Method;
import kotlin.KotlinNullPointerException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\000,\n\000\n\002\030\002\n\002\b\002\n\002\020\000\n\002\030\002\n\002\b\003\n\002\030\002\n\000\n\002\030\002\n\002\030\002\n\002\020\001\n\002\b\003\032\034\020\002\032\0028\000\"\006\b\000\020\000\030\001*\0020\001H\b¢\006\004\b\002\020\003\032'\020\006\032\0028\000\"\b\b\000\020\000*\0020\004*\b\022\004\022\0028\0000\005H@ø\001\000¢\006\004\b\006\020\007\032+\020\006\032\004\030\0018\000\"\b\b\000\020\000*\0020\004*\n\022\006\022\004\030\0018\0000\005H@ø\001\000¢\006\004\b\b\020\007\032)\020\n\032\b\022\004\022\0028\0000\t\"\004\b\000\020\000*\b\022\004\022\0028\0000\005H@ø\001\000¢\006\004\b\n\020\007\032\033\020\016\032\0020\r*\0060\013j\002`\fH@ø\001\000¢\006\004\b\016\020\017\002\004\n\002\b\031¨\006\020"}, d2 = {"T", "Lretrofit2/Retrofit;", "create", "(Lretrofit2/Retrofit;)Ljava/lang/Object;", "", "Lretrofit2/Call;", "await", "(Lretrofit2/Call;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitNullable", "Lretrofit2/Response;", "awaitResponse", "Ljava/lang/Exception;", "Lkotlin/Exception;", "", "suspendAndThrow", "(Ljava/lang/Exception;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "retrofit"}, k = 2, mv = {1, 4, 0})
@JvmName(name = "KotlinExtensions")
public final class KotlinExtensions {
  @Nullable
  public static final <T> Object await(@NotNull Call<T> paramCall, @NotNull Continuation<? super T> paramContinuation) {
    CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(paramContinuation), 1);
    CancellableContinuation cancellableContinuation = (CancellableContinuation)cancellableContinuationImpl;
    cancellableContinuation.invokeOnCancellation(new KotlinExtensions$await$$inlined$suspendCancellableCoroutine$lambda$1(paramCall));
    paramCall.enqueue(new KotlinExtensions$await$2$2(cancellableContinuation));
    Object object = cancellableContinuationImpl.getResult();
    if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED())
      DebugProbesKt.probeCoroutineSuspended(paramContinuation); 
    return object;
  }
  
  @JvmName(name = "awaitNullable")
  @Nullable
  public static final <T> Object awaitNullable(@NotNull Call<T> paramCall, @NotNull Continuation<? super T> paramContinuation) {
    CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(paramContinuation), 1);
    CancellableContinuation cancellableContinuation = (CancellableContinuation)cancellableContinuationImpl;
    cancellableContinuation.invokeOnCancellation(new KotlinExtensions$await$$inlined$suspendCancellableCoroutine$lambda$2(paramCall));
    paramCall.enqueue(new KotlinExtensions$await$4$2(cancellableContinuation));
    Object object = cancellableContinuationImpl.getResult();
    if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED())
      DebugProbesKt.probeCoroutineSuspended(paramContinuation); 
    return object;
  }
  
  @Nullable
  public static final <T> Object awaitResponse(@NotNull Call<T> paramCall, @NotNull Continuation<? super Response<T>> paramContinuation) {
    CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(paramContinuation), 1);
    CancellableContinuation cancellableContinuation = (CancellableContinuation)cancellableContinuationImpl;
    cancellableContinuation.invokeOnCancellation(new KotlinExtensions$awaitResponse$$inlined$suspendCancellableCoroutine$lambda$1(paramCall));
    paramCall.enqueue(new KotlinExtensions$awaitResponse$2$2(cancellableContinuation));
    Object object = cancellableContinuationImpl.getResult();
    if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED())
      DebugProbesKt.probeCoroutineSuspended(paramContinuation); 
    return object;
  }
  
  @Nullable
  public static final Object suspendAndThrow(@NotNull Exception paramException, @NotNull Continuation<?> paramContinuation) {
    // Byte code:
    //   0: aload_1
    //   1: instanceof retrofit2/KotlinExtensions$suspendAndThrow$1
    //   4: ifeq -> 37
    //   7: aload_1
    //   8: checkcast retrofit2/KotlinExtensions$suspendAndThrow$1
    //   11: astore_3
    //   12: aload_3
    //   13: getfield label : I
    //   16: istore_2
    //   17: iload_2
    //   18: ldc -2147483648
    //   20: iand
    //   21: ifeq -> 37
    //   24: aload_3
    //   25: iload_2
    //   26: ldc -2147483648
    //   28: iadd
    //   29: putfield label : I
    //   32: aload_3
    //   33: astore_1
    //   34: goto -> 46
    //   37: new retrofit2/KotlinExtensions$suspendAndThrow$1
    //   40: dup
    //   41: aload_1
    //   42: invokespecial <init> : (Lkotlin/coroutines/Continuation;)V
    //   45: astore_1
    //   46: aload_1
    //   47: getfield result : Ljava/lang/Object;
    //   50: astore #4
    //   52: invokestatic getCOROUTINE_SUSPENDED : ()Ljava/lang/Object;
    //   55: astore_3
    //   56: aload_1
    //   57: getfield label : I
    //   60: istore_2
    //   61: iload_2
    //   62: ifeq -> 96
    //   65: iload_2
    //   66: iconst_1
    //   67: if_icmpne -> 86
    //   70: aload_1
    //   71: getfield L$0 : Ljava/lang/Object;
    //   74: checkcast java/lang/Exception
    //   77: astore_0
    //   78: aload #4
    //   80: invokestatic throwOnFailure : (Ljava/lang/Object;)V
    //   83: goto -> 159
    //   86: new java/lang/IllegalStateException
    //   89: dup
    //   90: ldc 'call to 'resume' before 'invoke' with coroutine'
    //   92: invokespecial <init> : (Ljava/lang/String;)V
    //   95: athrow
    //   96: aload #4
    //   98: invokestatic throwOnFailure : (Ljava/lang/Object;)V
    //   101: aload_1
    //   102: aload_0
    //   103: putfield L$0 : Ljava/lang/Object;
    //   106: aload_1
    //   107: iconst_1
    //   108: putfield label : I
    //   111: aload_1
    //   112: checkcast kotlin/coroutines/Continuation
    //   115: astore_1
    //   116: invokestatic getDefault : ()Lkotlinx/coroutines/CoroutineDispatcher;
    //   119: aload_1
    //   120: invokeinterface getContext : ()Lkotlin/coroutines/CoroutineContext;
    //   125: new retrofit2/KotlinExtensions$suspendAndThrow$$inlined$suspendCoroutineUninterceptedOrReturn$lambda$1
    //   128: dup
    //   129: aload_1
    //   130: aload_0
    //   131: invokespecial <init> : (Lkotlin/coroutines/Continuation;Ljava/lang/Exception;)V
    //   134: invokevirtual dispatch : (Lkotlin/coroutines/CoroutineContext;Ljava/lang/Runnable;)V
    //   137: invokestatic getCOROUTINE_SUSPENDED : ()Ljava/lang/Object;
    //   140: astore_0
    //   141: aload_0
    //   142: invokestatic getCOROUTINE_SUSPENDED : ()Ljava/lang/Object;
    //   145: if_acmpne -> 152
    //   148: aload_1
    //   149: invokestatic probeCoroutineSuspended : (Lkotlin/coroutines/Continuation;)V
    //   152: aload_0
    //   153: aload_3
    //   154: if_acmpne -> 159
    //   157: aload_3
    //   158: areturn
    //   159: getstatic kotlin/Unit.INSTANCE : Lkotlin/Unit;
    //   162: areturn
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000\024\n\002\020\000\n\000\n\002\020\003\n\000\n\002\020\002\n\002\b\004\020\b\032\0020\004\"\b\b\000\020\001*\0020\0002\b\020\003\032\004\030\0010\002H\n¢\006\004\b\005\020\006¨\006\007"}, d2 = {"", "T", "", "it", "", "invoke", "(Ljava/lang/Throwable;)V", "retrofit2/KotlinExtensions$await$2$1", "<anonymous>"}, k = 3, mv = {1, 4, 0})
  public static final class KotlinExtensions$await$$inlined$suspendCancellableCoroutine$lambda$1 extends Lambda implements Function1<Throwable, Unit> {
    public final Call $this_await$inlined;
    
    public KotlinExtensions$await$$inlined$suspendCancellableCoroutine$lambda$1(Call param1Call) {
      super(1);
    }
    
    public final void invoke(@Nullable Throwable param1Throwable) {
      this.$this_await$inlined.cancel();
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000\024\n\002\020\000\n\000\n\002\020\003\n\000\n\002\020\002\n\002\b\004\020\b\032\0020\004\"\b\b\000\020\001*\0020\0002\b\020\003\032\004\030\0010\002H\n¢\006\004\b\005\020\006¨\006\007"}, d2 = {"", "T", "", "it", "", "invoke", "(Ljava/lang/Throwable;)V", "retrofit2/KotlinExtensions$await$4$1", "<anonymous>"}, k = 3, mv = {1, 4, 0})
  public static final class KotlinExtensions$await$$inlined$suspendCancellableCoroutine$lambda$2 extends Lambda implements Function1<Throwable, Unit> {
    public final Call $this_await$inlined;
    
    public KotlinExtensions$await$$inlined$suspendCancellableCoroutine$lambda$2(Call param1Call) {
      super(1);
    }
    
    public final void invoke(@Nullable Throwable param1Throwable) {
      this.$this_await$inlined.cancel();
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000%\n\000\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\002\n\002\b\002\n\002\020\003\n\002\b\004*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001J+\020\007\032\0020\0062\f\020\003\032\b\022\004\022\0028\0000\0022\f\020\005\032\b\022\004\022\0028\0000\004H\026¢\006\004\b\007\020\bJ%\020\013\032\0020\0062\f\020\003\032\b\022\004\022\0028\0000\0022\006\020\n\032\0020\tH\026¢\006\004\b\013\020\f¨\006\r"}, d2 = {"retrofit2/KotlinExtensions$await$2$2", "Lretrofit2/Callback;", "Lretrofit2/Call;", "call", "Lretrofit2/Response;", "response", "", "onResponse", "(Lretrofit2/Call;Lretrofit2/Response;)V", "", "t", "onFailure", "(Lretrofit2/Call;Ljava/lang/Throwable;)V", "retrofit"}, k = 1, mv = {1, 4, 0})
  public static final class KotlinExtensions$await$2$2 implements Callback<T> {
    public final CancellableContinuation $continuation;
    
    public KotlinExtensions$await$2$2(CancellableContinuation param1CancellableContinuation) {}
    
    public void onFailure(@NotNull Call<T> param1Call, @NotNull Throwable param1Throwable) {
      Intrinsics.checkParameterIsNotNull(param1Call, "call");
      Intrinsics.checkParameterIsNotNull(param1Throwable, "t");
      Continuation continuation = (Continuation)this.$continuation;
      Result.Companion companion = Result.Companion;
      continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(param1Throwable)));
    }
    
    public void onResponse(@NotNull Call<T> param1Call, @NotNull Response<T> param1Response) {
      Result.Companion companion;
      Intrinsics.checkParameterIsNotNull(param1Call, "call");
      Intrinsics.checkParameterIsNotNull(param1Response, "response");
      if (param1Response.isSuccessful()) {
        param1Response = (Response<T>)param1Response.body();
        if (param1Response == null) {
          param1Call = param1Call.request().tag(Invocation.class);
          if (param1Call == null)
            Intrinsics.throwNpe(); 
          Intrinsics.checkExpressionValueIsNotNull(param1Call, "call.request().tag(Invocation::class.java)!!");
          Method method = ((Invocation)param1Call).method();
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Response from ");
          Intrinsics.checkExpressionValueIsNotNull(method, "method");
          Class<?> clazz = method.getDeclaringClass();
          Intrinsics.checkExpressionValueIsNotNull(clazz, "method.declaringClass");
          stringBuilder.append(clazz.getName());
          stringBuilder.append('.');
          stringBuilder.append(method.getName());
          stringBuilder.append(" was null but response body type was declared as non-null");
          KotlinNullPointerException kotlinNullPointerException = new KotlinNullPointerException(stringBuilder.toString());
          Continuation continuation = (Continuation)this.$continuation;
          companion = Result.Companion;
          continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure((Throwable)kotlinNullPointerException)));
        } else {
          Continuation continuation = (Continuation)this.$continuation;
          Result.Companion companion1 = Result.Companion;
          continuation.resumeWith(Result.constructor-impl(companion));
        } 
      } else {
        Continuation continuation = (Continuation)this.$continuation;
        HttpException httpException = new HttpException((Response<?>)companion);
        Result.Companion companion1 = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(httpException)));
      } 
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000%\n\000\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\002\n\002\b\002\n\002\020\003\n\002\b\004*\001\000\b\n\030\0002\n\022\006\022\004\030\0018\0000\001J/\020\007\032\0020\0062\016\020\003\032\n\022\006\022\004\030\0018\0000\0022\016\020\005\032\n\022\006\022\004\030\0018\0000\004H\026¢\006\004\b\007\020\bJ'\020\013\032\0020\0062\016\020\003\032\n\022\006\022\004\030\0018\0000\0022\006\020\n\032\0020\tH\026¢\006\004\b\013\020\f¨\006\r"}, d2 = {"retrofit2/KotlinExtensions$await$4$2", "Lretrofit2/Callback;", "Lretrofit2/Call;", "call", "Lretrofit2/Response;", "response", "", "onResponse", "(Lretrofit2/Call;Lretrofit2/Response;)V", "", "t", "onFailure", "(Lretrofit2/Call;Ljava/lang/Throwable;)V", "retrofit"}, k = 1, mv = {1, 4, 0})
  public static final class KotlinExtensions$await$4$2 implements Callback<T> {
    public final CancellableContinuation $continuation;
    
    public KotlinExtensions$await$4$2(CancellableContinuation param1CancellableContinuation) {}
    
    public void onFailure(@NotNull Call<T> param1Call, @NotNull Throwable param1Throwable) {
      Intrinsics.checkParameterIsNotNull(param1Call, "call");
      Intrinsics.checkParameterIsNotNull(param1Throwable, "t");
      Continuation continuation = (Continuation)this.$continuation;
      Result.Companion companion = Result.Companion;
      continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(param1Throwable)));
    }
    
    public void onResponse(@NotNull Call<T> param1Call, @NotNull Response<T> param1Response) {
      Result.Companion companion;
      Intrinsics.checkParameterIsNotNull(param1Call, "call");
      Intrinsics.checkParameterIsNotNull(param1Response, "response");
      if (param1Response.isSuccessful()) {
        Continuation continuation = (Continuation)this.$continuation;
        T t = param1Response.body();
        companion = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(t));
      } else {
        Continuation continuation = (Continuation)this.$continuation;
        HttpException httpException = new HttpException((Response<?>)companion);
        Result.Companion companion1 = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(httpException)));
      } 
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000\020\n\000\n\002\030\002\n\000\n\002\020\002\n\002\b\004\020\007\032\0020\003\"\004\b\000\020\0002\b\020\002\032\004\030\0010\001H\n¢\006\004\b\004\020\005¨\006\006"}, d2 = {"T", "L;", "it", "", "invoke", "(L;)V", "kotlin/Throwable", "<anonymous>"}, k = 3, mv = {1, 4, 0})
  public static final class KotlinExtensions$awaitResponse$$inlined$suspendCancellableCoroutine$lambda$1 extends Lambda implements Function1<Throwable, Unit> {
    public final Call $this_awaitResponse$inlined;
    
    public KotlinExtensions$awaitResponse$$inlined$suspendCancellableCoroutine$lambda$1(Call param1Call) {
      super(1);
    }
    
    public final void invoke(@Nullable Throwable param1Throwable) {
      this.$this_awaitResponse$inlined.cancel();
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000%\n\000\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\002\n\002\b\002\n\002\020\003\n\002\b\004*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001J+\020\007\032\0020\0062\f\020\003\032\b\022\004\022\0028\0000\0022\f\020\005\032\b\022\004\022\0028\0000\004H\026¢\006\004\b\007\020\bJ%\020\013\032\0020\0062\f\020\003\032\b\022\004\022\0028\0000\0022\006\020\n\032\0020\tH\026¢\006\004\b\013\020\f¨\006\r"}, d2 = {"retrofit2/KotlinExtensions$awaitResponse$2$2", "Lretrofit2/Callback;", "Lretrofit2/Call;", "call", "Lretrofit2/Response;", "response", "", "onResponse", "(Lretrofit2/Call;Lretrofit2/Response;)V", "", "t", "onFailure", "(Lretrofit2/Call;Ljava/lang/Throwable;)V", "retrofit"}, k = 1, mv = {1, 4, 0})
  public static final class KotlinExtensions$awaitResponse$2$2 implements Callback<T> {
    public final CancellableContinuation $continuation;
    
    public KotlinExtensions$awaitResponse$2$2(CancellableContinuation param1CancellableContinuation) {}
    
    public void onFailure(@NotNull Call<T> param1Call, @NotNull Throwable param1Throwable) {
      Intrinsics.checkParameterIsNotNull(param1Call, "call");
      Intrinsics.checkParameterIsNotNull(param1Throwable, "t");
      Continuation continuation = (Continuation)this.$continuation;
      Result.Companion companion = Result.Companion;
      continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(param1Throwable)));
    }
    
    public void onResponse(@NotNull Call<T> param1Call, @NotNull Response<T> param1Response) {
      Intrinsics.checkParameterIsNotNull(param1Call, "call");
      Intrinsics.checkParameterIsNotNull(param1Response, "response");
      Continuation continuation = (Continuation)this.$continuation;
      Result.Companion companion = Result.Companion;
      continuation.resumeWith(Result.constructor-impl(param1Response));
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000\b\n\002\020\002\n\002\b\004\020\004\032\0020\000H\n¢\006\004\b\001\020\002¨\006\003"}, d2 = {"", "run", "()V", "retrofit2/KotlinExtensions$suspendAndThrow$2$1", "<anonymous>"}, k = 3, mv = {1, 4, 0})
  public static final class KotlinExtensions$suspendAndThrow$$inlined$suspendCoroutineUninterceptedOrReturn$lambda$1 implements Runnable {
    public final Continuation $continuation;
    
    public final Exception $this_suspendAndThrow$inlined;
    
    public KotlinExtensions$suspendAndThrow$$inlined$suspendCoroutineUninterceptedOrReturn$lambda$1(Continuation param1Continuation, Exception param1Exception) {}
    
    public final void run() {
      Continuation continuation = IntrinsicsKt.intercepted(this.$continuation);
      Exception exception = this.$this_suspendAndThrow$inlined;
      Result.Companion companion = Result.Companion;
      continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(exception)));
    }
  }
  
  @Metadata(bv = {1, 0, 3}, d1 = {"\000\032\n\002\030\002\n\002\030\002\n\002\030\002\n\002\020\001\n\000\n\002\020\000\n\002\b\002\020\006\032\004\030\0010\005*\0060\000j\002`\0012\f\020\004\032\b\022\004\022\0020\0030\002H@¢\006\004\b\006\020\007"}, d2 = {"Ljava/lang/Exception;", "Lkotlin/Exception;", "Lkotlin/coroutines/Continuation;", "", "continuation", "", "suspendAndThrow", "(Ljava/lang/Exception;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 4, 0})
  @DebugMetadata(c = "retrofit2.KotlinExtensions", f = "KotlinExtensions.kt", i = {0}, l = {113}, m = "suspendAndThrow", n = {"$this$suspendAndThrow"}, s = {"L$0"})
  public static final class KotlinExtensions$suspendAndThrow$1 extends ContinuationImpl {
    public Object L$0;
    
    public int label;
    
    public Object result;
    
    public KotlinExtensions$suspendAndThrow$1(Continuation param1Continuation) {
      super(param1Continuation);
    }
    
    @Nullable
    public final Object invokeSuspend(@NotNull Object param1Object) {
      this.result = param1Object;
      this.label |= Integer.MIN_VALUE;
      return KotlinExtensions.suspendAndThrow(null, (Continuation<?>)this);
    }
  }
}
