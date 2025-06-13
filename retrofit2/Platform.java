package retrofit2;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public class Platform {
  private static final Platform PLATFORM = findPlatform();
  
  private final boolean hasJava8Types;
  
  @Nullable
  private final Constructor<MethodHandles.Lookup> lookupConstructor;
  
  public Platform(boolean paramBoolean) {
    this.hasJava8Types = paramBoolean;
    Constructor<MethodHandles.Lookup> constructor2 = null;
    Constructor<MethodHandles.Lookup> constructor1 = null;
    if (paramBoolean) {
      constructor1 = constructor2;
      try {
        constructor2 = MethodHandles.Lookup.class.getDeclaredConstructor(new Class[] { Class.class, int.class });
        constructor1 = constructor2;
        constructor2.setAccessible(true);
        constructor1 = constructor2;
      } catch (NoClassDefFoundError|NoSuchMethodException noClassDefFoundError) {}
    } 
    this.lookupConstructor = constructor1;
  }
  
  private static Platform findPlatform() {
    Platform platform;
    if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
      platform = new Android();
    } else {
      platform = new Platform(true);
    } 
    return platform;
  }
  
  public static Platform get() {
    return PLATFORM;
  }
  
  public List<? extends CallAdapter.Factory> defaultCallAdapterFactories(@Nullable Executor paramExecutor) {
    List<CallAdapter.Factory> list;
    DefaultCallAdapterFactory defaultCallAdapterFactory = new DefaultCallAdapterFactory(paramExecutor);
    if (this.hasJava8Types) {
      list = Arrays.asList(new CallAdapter.Factory[] { CompletableFutureCallAdapterFactory.INSTANCE, defaultCallAdapterFactory });
    } else {
      list = Collections.singletonList(list);
    } 
    return list;
  }
  
  public int defaultCallAdapterFactoriesSize() {
    boolean bool;
    if (this.hasJava8Types) {
      bool = true;
    } else {
      bool = true;
    } 
    return bool;
  }
  
  @Nullable
  public Executor defaultCallbackExecutor() {
    return null;
  }
  
  public List<? extends Converter.Factory> defaultConverterFactories() {
    List<?> list;
    if (this.hasJava8Types) {
      list = Collections.singletonList(OptionalConverterFactory.INSTANCE);
    } else {
      list = Collections.emptyList();
    } 
    return (List)list;
  }
  
  public int defaultConverterFactoriesSize() {
    return this.hasJava8Types;
  }
  
  @Nullable
  @IgnoreJRERequirement
  public Object invokeDefaultMethod(Method paramMethod, Class<?> paramClass, Object paramObject, Object... paramVarArgs) throws Throwable {
    MethodHandles.Lookup lookup;
    Constructor<MethodHandles.Lookup> constructor = this.lookupConstructor;
    if (constructor != null) {
      lookup = constructor.newInstance(new Object[] { paramClass, Integer.valueOf(-1) });
    } else {
      lookup = MethodHandles.lookup();
    } 
    return lookup.unreflectSpecial(paramMethod, paramClass).bindTo(paramObject).invokeWithArguments(paramVarArgs);
  }
  
  @IgnoreJRERequirement
  public boolean isDefaultMethod(Method paramMethod) {
    boolean bool;
    if (this.hasJava8Types && paramMethod.isDefault()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class Android extends Platform {
    public Android() {
      super(bool);
    }
    
    public Executor defaultCallbackExecutor() {
      return new MainThreadExecutor();
    }
    
    @Nullable
    public Object invokeDefaultMethod(Method param1Method, Class<?> param1Class, Object param1Object, Object... param1VarArgs) throws Throwable {
      if (Build.VERSION.SDK_INT >= 26)
        return super.invokeDefaultMethod(param1Method, param1Class, param1Object, param1VarArgs); 
      throw new UnsupportedOperationException("Calling default methods on API 24 and 25 is not supported");
    }
    
    public static final class MainThreadExecutor implements Executor {
      private final Handler handler = new Handler(Looper.getMainLooper());
      
      public void execute(Runnable param2Runnable) {
        this.handler.post(param2Runnable);
      }
    }
  }
  
  public static final class MainThreadExecutor implements Executor {
    private final Handler handler = new Handler(Looper.getMainLooper());
    
    public void execute(Runnable param1Runnable) {
      this.handler.post(param1Runnable);
    }
  }
}
