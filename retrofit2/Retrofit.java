package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public final class Retrofit {
  public final HttpUrl baseUrl;
  
  public final List<CallAdapter.Factory> callAdapterFactories;
  
  public final Call.Factory callFactory;
  
  @Nullable
  public final Executor callbackExecutor;
  
  public final List<Converter.Factory> converterFactories;
  
  private final Map<Method, ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<Method, ServiceMethod<?>>();
  
  public final boolean validateEagerly;
  
  public Retrofit(Call.Factory paramFactory, HttpUrl paramHttpUrl, List<Converter.Factory> paramList, List<CallAdapter.Factory> paramList1, @Nullable Executor paramExecutor, boolean paramBoolean) {
    this.callFactory = paramFactory;
    this.baseUrl = paramHttpUrl;
    this.converterFactories = paramList;
    this.callAdapterFactories = paramList1;
    this.callbackExecutor = paramExecutor;
    this.validateEagerly = paramBoolean;
  }
  
  private void validateServiceInterface(Class<?> paramClass) {
    if (paramClass.isInterface()) {
      ArrayDeque<Class<?>> arrayDeque = new ArrayDeque(1);
      arrayDeque.add(paramClass);
      while (!arrayDeque.isEmpty()) {
        StringBuilder stringBuilder;
        Class<?> clazz = arrayDeque.removeFirst();
        if ((clazz.getTypeParameters()).length != 0) {
          stringBuilder = new StringBuilder("Type parameters are unsupported on ");
          stringBuilder.append(clazz.getName());
          if (clazz != paramClass) {
            stringBuilder.append(" which is an interface of ");
            stringBuilder.append(paramClass.getName());
          } 
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
        Collections.addAll((Collection<? super Class<?>>)stringBuilder, clazz.getInterfaces());
      } 
      if (this.validateEagerly) {
        Platform platform = Platform.get();
        for (Method method : paramClass.getDeclaredMethods()) {
          if (!platform.isDefaultMethod(method) && !Modifier.isStatic(method.getModifiers()))
            loadServiceMethod(method); 
        } 
      } 
      return;
    } 
    throw new IllegalArgumentException("API declarations must be interfaces.");
  }
  
  public HttpUrl baseUrl() {
    return this.baseUrl;
  }
  
  public CallAdapter<?, ?> callAdapter(Type paramType, Annotation[] paramArrayOfAnnotation) {
    return nextCallAdapter(null, paramType, paramArrayOfAnnotation);
  }
  
  public List<CallAdapter.Factory> callAdapterFactories() {
    return this.callAdapterFactories;
  }
  
  public Call.Factory callFactory() {
    return this.callFactory;
  }
  
  @Nullable
  public Executor callbackExecutor() {
    return this.callbackExecutor;
  }
  
  public List<Converter.Factory> converterFactories() {
    return this.converterFactories;
  }
  
  public <T> T create(final Class<T> service) {
    validateServiceInterface(service);
    ClassLoader classLoader = service.getClassLoader();
    InvocationHandler invocationHandler = new InvocationHandler() {
        private final Object[] emptyArgs = new Object[0];
        
        private final Platform platform = Platform.get();
        
        public final Retrofit this$0;
        
        public final Class val$service;
        
        @Nullable
        public Object invoke(Object param1Object, Method param1Method, @Nullable Object[] param1ArrayOfObject) throws Throwable {
          if (param1Method.getDeclaringClass() == Object.class)
            return param1Method.invoke(this, param1ArrayOfObject); 
          if (param1ArrayOfObject == null)
            param1ArrayOfObject = this.emptyArgs; 
          if (this.platform.isDefaultMethod(param1Method)) {
            param1Object = this.platform.invokeDefaultMethod(param1Method, service, param1Object, param1ArrayOfObject);
          } else {
            param1Object = Retrofit.this.loadServiceMethod(param1Method).invoke(param1ArrayOfObject);
          } 
          return param1Object;
        }
      };
    return (T)Proxy.newProxyInstance(classLoader, new Class[] { service }, invocationHandler);
  }
  
  public ServiceMethod<?> loadServiceMethod(Method paramMethod) {
    ServiceMethod<?> serviceMethod = this.serviceMethodCache.get(paramMethod);
    if (serviceMethod != null)
      return serviceMethod; 
    synchronized (this.serviceMethodCache) {
      ServiceMethod<?> serviceMethod1 = this.serviceMethodCache.get(paramMethod);
      serviceMethod = serviceMethod1;
      if (serviceMethod1 == null) {
        serviceMethod = ServiceMethod.parseAnnotations(this, paramMethod);
        this.serviceMethodCache.put(paramMethod, serviceMethod);
      } 
      return serviceMethod;
    } 
  }
  
  public Builder newBuilder() {
    return new Builder(this);
  }
  
  public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter.Factory paramFactory, Type paramType, Annotation[] paramArrayOfAnnotation) {
    Objects.requireNonNull(paramType, "returnType == null");
    Objects.requireNonNull(paramArrayOfAnnotation, "annotations == null");
    int i = this.callAdapterFactories.indexOf(paramFactory) + 1;
    int k = this.callAdapterFactories.size();
    int j;
    for (j = i; j < k; j++) {
      CallAdapter<?, ?> callAdapter = ((CallAdapter.Factory)this.callAdapterFactories.get(j)).get(paramType, paramArrayOfAnnotation, this);
      if (callAdapter != null)
        return callAdapter; 
    } 
    StringBuilder stringBuilder = new StringBuilder("Could not locate call adapter for ");
    stringBuilder.append(paramType);
    stringBuilder.append(".\n");
    if (paramFactory != null) {
      stringBuilder.append("  Skipped:");
      for (j = 0; j < i; j++) {
        stringBuilder.append("\n   * ");
        stringBuilder.append(((CallAdapter.Factory)this.callAdapterFactories.get(j)).getClass().getName());
      } 
      stringBuilder.append('\n');
    } 
    stringBuilder.append("  Tried:");
    j = this.callAdapterFactories.size();
    while (i < j) {
      stringBuilder.append("\n   * ");
      stringBuilder.append(((CallAdapter.Factory)this.callAdapterFactories.get(i)).getClass().getName());
      i++;
    } 
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public <T> Converter<T, RequestBody> nextRequestBodyConverter(@Nullable Converter.Factory paramFactory, Type paramType, Annotation[] paramArrayOfAnnotation1, Annotation[] paramArrayOfAnnotation2) {
    Objects.requireNonNull(paramType, "type == null");
    Objects.requireNonNull(paramArrayOfAnnotation1, "parameterAnnotations == null");
    Objects.requireNonNull(paramArrayOfAnnotation2, "methodAnnotations == null");
    int i = this.converterFactories.indexOf(paramFactory) + 1;
    int k = this.converterFactories.size();
    int j;
    for (j = i; j < k; j++) {
      Converter<?, RequestBody> converter = ((Converter.Factory)this.converterFactories.get(j)).requestBodyConverter(paramType, paramArrayOfAnnotation1, paramArrayOfAnnotation2, this);
      if (converter != null)
        return (Converter)converter; 
    } 
    StringBuilder stringBuilder = new StringBuilder("Could not locate RequestBody converter for ");
    stringBuilder.append(paramType);
    stringBuilder.append(".\n");
    if (paramFactory != null) {
      stringBuilder.append("  Skipped:");
      for (j = 0; j < i; j++) {
        stringBuilder.append("\n   * ");
        stringBuilder.append(((Converter.Factory)this.converterFactories.get(j)).getClass().getName());
      } 
      stringBuilder.append('\n');
    } 
    stringBuilder.append("  Tried:");
    j = this.converterFactories.size();
    while (i < j) {
      stringBuilder.append("\n   * ");
      stringBuilder.append(((Converter.Factory)this.converterFactories.get(i)).getClass().getName());
      i++;
    } 
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public <T> Converter<ResponseBody, T> nextResponseBodyConverter(@Nullable Converter.Factory paramFactory, Type paramType, Annotation[] paramArrayOfAnnotation) {
    Objects.requireNonNull(paramType, "type == null");
    Objects.requireNonNull(paramArrayOfAnnotation, "annotations == null");
    int i = this.converterFactories.indexOf(paramFactory) + 1;
    int k = this.converterFactories.size();
    int j;
    for (j = i; j < k; j++) {
      Converter<ResponseBody, ?> converter = ((Converter.Factory)this.converterFactories.get(j)).responseBodyConverter(paramType, paramArrayOfAnnotation, this);
      if (converter != null)
        return (Converter)converter; 
    } 
    StringBuilder stringBuilder = new StringBuilder("Could not locate ResponseBody converter for ");
    stringBuilder.append(paramType);
    stringBuilder.append(".\n");
    if (paramFactory != null) {
      stringBuilder.append("  Skipped:");
      for (j = 0; j < i; j++) {
        stringBuilder.append("\n   * ");
        stringBuilder.append(((Converter.Factory)this.converterFactories.get(j)).getClass().getName());
      } 
      stringBuilder.append('\n');
    } 
    stringBuilder.append("  Tried:");
    j = this.converterFactories.size();
    while (i < j) {
      stringBuilder.append("\n   * ");
      stringBuilder.append(((Converter.Factory)this.converterFactories.get(i)).getClass().getName());
      i++;
    } 
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public <T> Converter<T, RequestBody> requestBodyConverter(Type paramType, Annotation[] paramArrayOfAnnotation1, Annotation[] paramArrayOfAnnotation2) {
    return nextRequestBodyConverter(null, paramType, paramArrayOfAnnotation1, paramArrayOfAnnotation2);
  }
  
  public <T> Converter<ResponseBody, T> responseBodyConverter(Type paramType, Annotation[] paramArrayOfAnnotation) {
    return nextResponseBodyConverter(null, paramType, paramArrayOfAnnotation);
  }
  
  public <T> Converter<T, String> stringConverter(Type paramType, Annotation[] paramArrayOfAnnotation) {
    Objects.requireNonNull(paramType, "type == null");
    Objects.requireNonNull(paramArrayOfAnnotation, "annotations == null");
    int i = this.converterFactories.size();
    for (byte b = 0; b < i; b++) {
      Converter<?, String> converter = ((Converter.Factory)this.converterFactories.get(b)).stringConverter(paramType, paramArrayOfAnnotation, this);
      if (converter != null)
        return (Converter)converter; 
    } 
    return BuiltInConverters.ToStringConverter.INSTANCE;
  }
  
  public static final class Builder {
    @Nullable
    private HttpUrl baseUrl;
    
    private final List<CallAdapter.Factory> callAdapterFactories = new ArrayList<CallAdapter.Factory>();
    
    @Nullable
    private Call.Factory callFactory;
    
    @Nullable
    private Executor callbackExecutor;
    
    private final List<Converter.Factory> converterFactories = new ArrayList<Converter.Factory>();
    
    private final Platform platform;
    
    private boolean validateEagerly;
    
    public Builder() {
      this(Platform.get());
    }
    
    public Builder(Platform param1Platform) {
      this.platform = param1Platform;
    }
    
    public Builder(Retrofit param1Retrofit) {
      Platform platform = Platform.get();
      this.platform = platform;
      this.callFactory = param1Retrofit.callFactory;
      this.baseUrl = param1Retrofit.baseUrl;
      int j = param1Retrofit.converterFactories.size();
      int i = platform.defaultConverterFactoriesSize();
      byte b;
      for (b = 1; b < j - i; b++)
        this.converterFactories.add(param1Retrofit.converterFactories.get(b)); 
      b = 0;
      j = param1Retrofit.callAdapterFactories.size();
      i = this.platform.defaultCallAdapterFactoriesSize();
      while (b < j - i) {
        this.callAdapterFactories.add(param1Retrofit.callAdapterFactories.get(b));
        b++;
      } 
      this.callbackExecutor = param1Retrofit.callbackExecutor;
      this.validateEagerly = param1Retrofit.validateEagerly;
    }
    
    public Builder addCallAdapterFactory(CallAdapter.Factory param1Factory) {
      List<CallAdapter.Factory> list = this.callAdapterFactories;
      Objects.requireNonNull(param1Factory, "factory == null");
      list.add(param1Factory);
      return this;
    }
    
    public Builder addConverterFactory(Converter.Factory param1Factory) {
      List<Converter.Factory> list = this.converterFactories;
      Objects.requireNonNull(param1Factory, "factory == null");
      list.add(param1Factory);
      return this;
    }
    
    public Builder baseUrl(String param1String) {
      Objects.requireNonNull(param1String, "baseUrl == null");
      return baseUrl(HttpUrl.get(param1String));
    }
    
    public Builder baseUrl(URL param1URL) {
      Objects.requireNonNull(param1URL, "baseUrl == null");
      return baseUrl(HttpUrl.get(param1URL.toString()));
    }
    
    public Builder baseUrl(HttpUrl param1HttpUrl) {
      Objects.requireNonNull(param1HttpUrl, "baseUrl == null");
      List<String> list = param1HttpUrl.pathSegments();
      if ("".equals(list.get(list.size() - 1))) {
        this.baseUrl = param1HttpUrl;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("baseUrl must end in /: ");
      stringBuilder.append(param1HttpUrl);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Retrofit build() {
      if (this.baseUrl != null) {
        Call.Factory factory2 = this.callFactory;
        Call.Factory factory1 = factory2;
        if (factory2 == null)
          factory1 = new OkHttpClient(); 
        Executor executor2 = this.callbackExecutor;
        Executor executor1 = executor2;
        if (executor2 == null)
          executor1 = this.platform.defaultCallbackExecutor(); 
        ArrayList<CallAdapter.Factory> arrayList1 = new ArrayList<CallAdapter.Factory>(this.callAdapterFactories);
        arrayList1.addAll(this.platform.defaultCallAdapterFactories(executor1));
        ArrayList<BuiltInConverters> arrayList = new ArrayList(this.converterFactories.size() + 1 + this.platform.defaultConverterFactoriesSize());
        arrayList.add(new BuiltInConverters());
        arrayList.addAll(this.converterFactories);
        arrayList.addAll(this.platform.defaultConverterFactories());
        return new Retrofit(factory1, this.baseUrl, Collections.unmodifiableList((List)arrayList), Collections.unmodifiableList(arrayList1), executor1, this.validateEagerly);
      } 
      throw new IllegalStateException("Base URL required.");
    }
    
    public List<CallAdapter.Factory> callAdapterFactories() {
      return this.callAdapterFactories;
    }
    
    public Builder callFactory(Call.Factory param1Factory) {
      Objects.requireNonNull(param1Factory, "factory == null");
      this.callFactory = param1Factory;
      return this;
    }
    
    public Builder callbackExecutor(Executor param1Executor) {
      Objects.requireNonNull(param1Executor, "executor == null");
      this.callbackExecutor = param1Executor;
      return this;
    }
    
    public Builder client(OkHttpClient param1OkHttpClient) {
      Objects.requireNonNull(param1OkHttpClient, "client == null");
      return callFactory(param1OkHttpClient);
    }
    
    public List<Converter.Factory> converterFactories() {
      return this.converterFactories;
    }
    
    public Builder validateEagerly(boolean param1Boolean) {
      this.validateEagerly = param1Boolean;
      return this;
    }
  }
}
