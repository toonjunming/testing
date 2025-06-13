package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import kotlin.coroutines.Continuation;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

public final class RequestFactory {
  private final HttpUrl baseUrl;
  
  @Nullable
  private final MediaType contentType;
  
  private final boolean hasBody;
  
  @Nullable
  private final Headers headers;
  
  public final String httpMethod;
  
  private final boolean isFormEncoded;
  
  public final boolean isKotlinSuspendFunction;
  
  private final boolean isMultipart;
  
  private final Method method;
  
  private final ParameterHandler<?>[] parameterHandlers;
  
  @Nullable
  private final String relativeUrl;
  
  public RequestFactory(Builder paramBuilder) {
    this.method = paramBuilder.method;
    this.baseUrl = paramBuilder.retrofit.baseUrl;
    this.httpMethod = paramBuilder.httpMethod;
    this.relativeUrl = paramBuilder.relativeUrl;
    this.headers = paramBuilder.headers;
    this.contentType = paramBuilder.contentType;
    this.hasBody = paramBuilder.hasBody;
    this.isFormEncoded = paramBuilder.isFormEncoded;
    this.isMultipart = paramBuilder.isMultipart;
    this.parameterHandlers = paramBuilder.parameterHandlers;
    this.isKotlinSuspendFunction = paramBuilder.isKotlinSuspendFunction;
  }
  
  public static RequestFactory parseAnnotations(Retrofit paramRetrofit, Method paramMethod) {
    return (new Builder(paramRetrofit, paramMethod)).build();
  }
  
  public Request create(Object[] paramArrayOfObject) throws IOException {
    ParameterHandler<?>[] arrayOfParameterHandler = this.parameterHandlers;
    int i = paramArrayOfObject.length;
    if (i == arrayOfParameterHandler.length) {
      RequestBuilder requestBuilder = new RequestBuilder(this.httpMethod, this.baseUrl, this.relativeUrl, this.headers, this.contentType, this.hasBody, this.isFormEncoded, this.isMultipart);
      int j = i;
      if (this.isKotlinSuspendFunction)
        j = i - 1; 
      ArrayList<Object> arrayList = new ArrayList(j);
      for (i = 0; i < j; i++) {
        arrayList.add(paramArrayOfObject[i]);
        arrayOfParameterHandler[i].apply(requestBuilder, paramArrayOfObject[i]);
      } 
      return requestBuilder.get().<Invocation>tag(Invocation.class, new Invocation(this.method, arrayList)).build();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Argument count (");
    stringBuilder.append(i);
    stringBuilder.append(") doesn't match expected count (");
    stringBuilder.append(arrayOfParameterHandler.length);
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static final class Builder {
    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
    
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
    
    @Nullable
    public MediaType contentType;
    
    public boolean gotBody;
    
    public boolean gotField;
    
    public boolean gotPart;
    
    public boolean gotPath;
    
    public boolean gotQuery;
    
    public boolean gotQueryMap;
    
    public boolean gotQueryName;
    
    public boolean gotUrl;
    
    public boolean hasBody;
    
    @Nullable
    public Headers headers;
    
    @Nullable
    public String httpMethod;
    
    public boolean isFormEncoded;
    
    public boolean isKotlinSuspendFunction;
    
    public boolean isMultipart;
    
    public final Method method;
    
    public final Annotation[] methodAnnotations;
    
    public final Annotation[][] parameterAnnotationsArray;
    
    @Nullable
    public ParameterHandler<?>[] parameterHandlers;
    
    public final Type[] parameterTypes;
    
    @Nullable
    public String relativeUrl;
    
    @Nullable
    public Set<String> relativeUrlParamNames;
    
    public final Retrofit retrofit;
    
    static {
    
    }
    
    public Builder(Retrofit param1Retrofit, Method param1Method) {
      this.retrofit = param1Retrofit;
      this.method = param1Method;
      this.methodAnnotations = param1Method.getAnnotations();
      this.parameterTypes = param1Method.getGenericParameterTypes();
      this.parameterAnnotationsArray = param1Method.getParameterAnnotations();
    }
    
    private static Class<?> boxIfPrimitive(Class<?> param1Class) {
      if (boolean.class == param1Class)
        return Boolean.class; 
      if (byte.class == param1Class)
        return Byte.class; 
      if (char.class == param1Class)
        return Character.class; 
      if (double.class == param1Class)
        return Double.class; 
      if (float.class == param1Class)
        return Float.class; 
      if (int.class == param1Class)
        return Integer.class; 
      if (long.class == param1Class)
        return Long.class; 
      Class<?> clazz = param1Class;
      if (short.class == param1Class)
        clazz = Short.class; 
      return clazz;
    }
    
    private Headers parseHeaders(String[] param1ArrayOfString) {
      Headers.Builder builder = new Headers.Builder();
      int i = param1ArrayOfString.length;
      byte b = 0;
      while (b < i) {
        String str = param1ArrayOfString[b];
        int j = str.indexOf(':');
        if (j != -1 && j != 0 && j != str.length() - 1) {
          String str1 = str.substring(0, j);
          str = str.substring(j + 1).trim();
          if ("Content-Type".equalsIgnoreCase(str1)) {
            try {
              this.contentType = MediaType.get(str);
            } catch (IllegalArgumentException illegalArgumentException) {
              throw Utils.methodError(this.method, illegalArgumentException, "Malformed content type: %s", new Object[] { str });
            } 
          } else {
            builder.add(str1, str);
          } 
          b++;
          continue;
        } 
        throw Utils.methodError(this.method, "@Headers value must be in the form \"Name: Value\". Found: \"%s\"", new Object[] { str });
      } 
      return builder.build();
    }
    
    private void parseHttpMethodAndPath(String param1String1, String param1String2, boolean param1Boolean) {
      String str = this.httpMethod;
      if (str == null) {
        this.httpMethod = param1String1;
        this.hasBody = param1Boolean;
        if (param1String2.isEmpty())
          return; 
        int i = param1String2.indexOf('?');
        if (i != -1 && i < param1String2.length() - 1) {
          param1String1 = param1String2.substring(i + 1);
          if (PARAM_URL_REGEX.matcher(param1String1).find())
            throw Utils.methodError(this.method, "URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", new Object[] { param1String1 }); 
        } 
        this.relativeUrl = param1String2;
        this.relativeUrlParamNames = parsePathParameters(param1String2);
        return;
      } 
      throw Utils.methodError(this.method, "Only one HTTP method is allowed. Found: %s and %s.", new Object[] { str, param1String1 });
    }
    
    private void parseMethodAnnotation(Annotation param1Annotation) {
      if (param1Annotation instanceof DELETE) {
        parseHttpMethodAndPath("DELETE", ((DELETE)param1Annotation).value(), false);
      } else if (param1Annotation instanceof GET) {
        parseHttpMethodAndPath("GET", ((GET)param1Annotation).value(), false);
      } else if (param1Annotation instanceof HEAD) {
        parseHttpMethodAndPath("HEAD", ((HEAD)param1Annotation).value(), false);
      } else if (param1Annotation instanceof PATCH) {
        parseHttpMethodAndPath("PATCH", ((PATCH)param1Annotation).value(), true);
      } else if (param1Annotation instanceof POST) {
        parseHttpMethodAndPath("POST", ((POST)param1Annotation).value(), true);
      } else if (param1Annotation instanceof PUT) {
        parseHttpMethodAndPath("PUT", ((PUT)param1Annotation).value(), true);
      } else if (param1Annotation instanceof OPTIONS) {
        parseHttpMethodAndPath("OPTIONS", ((OPTIONS)param1Annotation).value(), false);
      } else if (param1Annotation instanceof retrofit2.http.HTTP) {
        param1Annotation = param1Annotation;
        parseHttpMethodAndPath(param1Annotation.method(), param1Annotation.path(), param1Annotation.hasBody());
      } else {
        String[] arrayOfString;
        if (param1Annotation instanceof Headers) {
          arrayOfString = ((Headers)param1Annotation).value();
          if (arrayOfString.length != 0) {
            this.headers = parseHeaders(arrayOfString);
          } else {
            throw Utils.methodError(this.method, "@Headers annotation is empty.", new Object[0]);
          } 
        } else if (arrayOfString instanceof retrofit2.http.Multipart) {
          if (!this.isFormEncoded) {
            this.isMultipart = true;
          } else {
            throw Utils.methodError(this.method, "Only one encoding annotation is allowed.", new Object[0]);
          } 
        } else if (arrayOfString instanceof retrofit2.http.FormUrlEncoded) {
          if (!this.isMultipart) {
            this.isFormEncoded = true;
          } else {
            throw Utils.methodError(this.method, "Only one encoding annotation is allowed.", new Object[0]);
          } 
        } 
      } 
    }
    
    @Nullable
    private ParameterHandler<?> parseParameter(int param1Int, Type param1Type, @Nullable Annotation[] param1ArrayOfAnnotation, boolean param1Boolean) {
      ParameterHandler parameterHandler;
      if (param1ArrayOfAnnotation != null) {
        int i = param1ArrayOfAnnotation.length;
        ParameterHandler<?> parameterHandler1 = null;
        byte b = 0;
        while (true) {
          parameterHandler = parameterHandler1;
          if (b < i) {
            parameterHandler = parseParameterAnnotation(param1Int, param1Type, param1ArrayOfAnnotation, param1ArrayOfAnnotation[b]);
            if (parameterHandler != null)
              if (parameterHandler1 == null) {
                parameterHandler1 = parameterHandler;
              } else {
                throw Utils.parameterError(this.method, param1Int, "Multiple Retrofit annotations found, only one allowed.", new Object[0]);
              }  
            b++;
            continue;
          } 
          break;
        } 
      } else {
        parameterHandler = null;
      } 
      if (parameterHandler == null) {
        if (param1Boolean)
          try {
            if (Utils.getRawType(param1Type) == Continuation.class) {
              this.isKotlinSuspendFunction = true;
              return null;
            } 
          } catch (NoClassDefFoundError noClassDefFoundError) {} 
        throw Utils.parameterError(this.method, param1Int, "No Retrofit annotation found.", new Object[0]);
      } 
      return parameterHandler;
    }
    
    @Nullable
    private ParameterHandler<?> parseParameterAnnotation(int param1Int, Type param1Type, Annotation[] param1ArrayOfAnnotation, Annotation param1Annotation) {
      Converter<?, String> converter4;
      Type<?> type;
      Converter<?, String> converter3;
      Type type2;
      Converter<?, String> converter2;
      Type type1;
      Converter<?, String> converter1;
      Converter<?, RequestBody> converter;
      Method method2;
      StringBuilder stringBuilder2;
      Method method1;
      StringBuilder stringBuilder1;
      String str2;
      Class<?> clazz3;
      Method method6;
      Class<?> clazz2;
      Method method5;
      String str1;
      Method method4;
      Class<?> clazz1;
      Method method3;
      if (param1Annotation instanceof retrofit2.http.Url) {
        validateResolvableType(param1Int, param1Type);
        if (!this.gotUrl) {
          if (!this.gotPath) {
            if (!this.gotQuery) {
              if (!this.gotQueryName) {
                if (!this.gotQueryMap) {
                  if (this.relativeUrl == null) {
                    this.gotUrl = true;
                    if (param1Type == HttpUrl.class || param1Type == String.class || param1Type == URI.class || (param1Type instanceof Class && "android.net.Uri".equals(((Class)param1Type).getName())))
                      return new ParameterHandler.RelativeUrl(this.method, param1Int); 
                    throw Utils.parameterError(this.method, param1Int, "@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type.", new Object[0]);
                  } 
                  throw Utils.parameterError(this.method, param1Int, "@Url cannot be used with @%s URL", new Object[] { this.httpMethod });
                } 
                throw Utils.parameterError(this.method, param1Int, "A @Url parameter must not come after a @QueryMap.", new Object[0]);
              } 
              throw Utils.parameterError(this.method, param1Int, "A @Url parameter must not come after a @QueryName.", new Object[0]);
            } 
            throw Utils.parameterError(this.method, param1Int, "A @Url parameter must not come after a @Query.", new Object[0]);
          } 
          throw Utils.parameterError(this.method, param1Int, "@Path parameters may not be used with @Url.", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "Multiple @Url method annotations found.", new Object[0]);
      } 
      if (param1Annotation instanceof Path) {
        validateResolvableType(param1Int, param1Type);
        if (!this.gotQuery) {
          if (!this.gotQueryName) {
            if (!this.gotQueryMap) {
              if (!this.gotUrl) {
                if (this.relativeUrl != null) {
                  this.gotPath = true;
                  Path path = (Path)param1Annotation;
                  str2 = path.value();
                  validatePathName(param1Int, str2);
                  converter4 = this.retrofit.stringConverter(param1Type, param1ArrayOfAnnotation);
                  return new ParameterHandler.Path(this.method, param1Int, str2, converter4, path.encoded());
                } 
                throw Utils.parameterError(this.method, param1Int, "@Path can only be used with relative url on @%s", new Object[] { this.httpMethod });
              } 
              throw Utils.parameterError(this.method, param1Int, "@Path parameters may not be used with @Url.", new Object[0]);
            } 
            throw Utils.parameterError(this.method, param1Int, "A @Path parameter must not come after a @QueryMap.", new Object[0]);
          } 
          throw Utils.parameterError(this.method, param1Int, "A @Path parameter must not come after a @QueryName.", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "A @Path parameter must not come after a @Query.", new Object[0]);
      } 
      if (str2 instanceof Query) {
        validateResolvableType(param1Int, (Type)converter4);
        Query query = (Query)str2;
        str2 = query.value();
        boolean bool = query.encoded();
        Class<?> clazz = Utils.getRawType((Type)converter4);
        this.gotQuery = true;
        if (Iterable.class.isAssignableFrom(clazz)) {
          if (converter4 instanceof ParameterizedType) {
            type = Utils.getParameterUpperBound(0, (ParameterizedType)converter4);
            return (new ParameterHandler.Query(str2, this.retrofit.stringConverter(type, param1ArrayOfAnnotation), bool)).iterable();
          } 
          method2 = this.method;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(clazz.getSimpleName());
          stringBuilder.append(" must include generic type (e.g., ");
          stringBuilder.append(clazz.getSimpleName());
          stringBuilder.append("<String>)");
          throw Utils.parameterError(method2, param1Int, stringBuilder.toString(), new Object[0]);
        } 
        if (clazz.isArray()) {
          type = boxIfPrimitive(clazz.getComponentType());
          return (new ParameterHandler.Query(str2, this.retrofit.stringConverter(type, (Annotation[])method2), bool)).array();
        } 
        return new ParameterHandler.Query(str2, this.retrofit.stringConverter(type, (Annotation[])method2), bool);
      } 
      if (str2 instanceof QueryName) {
        validateResolvableType(param1Int, type);
        boolean bool = ((QueryName)str2).encoded();
        clazz3 = Utils.getRawType(type);
        this.gotQueryName = true;
        if (Iterable.class.isAssignableFrom(clazz3)) {
          if (type instanceof ParameterizedType) {
            type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
            return (new ParameterHandler.QueryName(this.retrofit.stringConverter(type, (Annotation[])method2), bool)).iterable();
          } 
          method2 = this.method;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(clazz3.getSimpleName());
          stringBuilder.append(" must include generic type (e.g., ");
          stringBuilder.append(clazz3.getSimpleName());
          stringBuilder.append("<String>)");
          throw Utils.parameterError(method2, param1Int, stringBuilder.toString(), new Object[0]);
        } 
        if (clazz3.isArray()) {
          type = boxIfPrimitive(clazz3.getComponentType());
          return (new ParameterHandler.QueryName(this.retrofit.stringConverter(type, (Annotation[])method2), bool)).array();
        } 
        return new ParameterHandler.QueryName(this.retrofit.stringConverter(type, (Annotation[])method2), bool);
      } 
      if (clazz3 instanceof QueryMap) {
        validateResolvableType(param1Int, type);
        Class<?> clazz = Utils.getRawType(type);
        this.gotQueryMap = true;
        if (Map.class.isAssignableFrom(clazz)) {
          type = Utils.getSupertype(type, clazz, Map.class);
          if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            type = Utils.getParameterUpperBound(0, parameterizedType);
            if (String.class == type) {
              type = Utils.getParameterUpperBound(1, parameterizedType);
              converter3 = this.retrofit.stringConverter(type, (Annotation[])method2);
              return new ParameterHandler.QueryMap(this.method, param1Int, converter3, ((QueryMap)clazz3).encoded());
            } 
            method6 = this.method;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("@QueryMap keys must be of type String: ");
            stringBuilder2.append(converter3);
            throw Utils.parameterError(method6, param1Int, stringBuilder2.toString(), new Object[0]);
          } 
          throw Utils.parameterError(this.method, param1Int, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "@QueryMap parameter type must be Map.", new Object[0]);
      } 
      if (method6 instanceof Header) {
        validateResolvableType(param1Int, (Type)converter3);
        String str = ((Header)method6).value();
        clazz2 = Utils.getRawType((Type)converter3);
        if (Iterable.class.isAssignableFrom(clazz2)) {
          if (converter3 instanceof ParameterizedType) {
            type2 = Utils.getParameterUpperBound(0, (ParameterizedType)converter3);
            return (new ParameterHandler.Header(str, this.retrofit.stringConverter(type2, (Annotation[])stringBuilder2))).iterable();
          } 
          Method method = this.method;
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append(clazz2.getSimpleName());
          stringBuilder2.append(" must include generic type (e.g., ");
          stringBuilder2.append(clazz2.getSimpleName());
          stringBuilder2.append("<String>)");
          throw Utils.parameterError(method, param1Int, stringBuilder2.toString(), new Object[0]);
        } 
        if (clazz2.isArray()) {
          type2 = boxIfPrimitive(clazz2.getComponentType());
          return (new ParameterHandler.Header(str, this.retrofit.stringConverter(type2, (Annotation[])stringBuilder2))).array();
        } 
        return new ParameterHandler.Header(str, this.retrofit.stringConverter(type2, (Annotation[])stringBuilder2));
      } 
      if (clazz2 instanceof retrofit2.http.HeaderMap) {
        if (type2 == Headers.class)
          return new ParameterHandler.Headers(this.method, param1Int); 
        validateResolvableType(param1Int, type2);
        clazz2 = Utils.getRawType(type2);
        if (Map.class.isAssignableFrom(clazz2)) {
          type2 = Utils.getSupertype(type2, clazz2, Map.class);
          if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type2;
            type2 = Utils.getParameterUpperBound(0, parameterizedType);
            if (String.class == type2) {
              type2 = Utils.getParameterUpperBound(1, parameterizedType);
              converter2 = this.retrofit.stringConverter(type2, (Annotation[])stringBuilder2);
              return new ParameterHandler.HeaderMap(this.method, param1Int, converter2);
            } 
            method5 = this.method;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("@HeaderMap keys must be of type String: ");
            stringBuilder2.append(converter2);
            throw Utils.parameterError(method5, param1Int, stringBuilder2.toString(), new Object[0]);
          } 
          throw Utils.parameterError(this.method, param1Int, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "@HeaderMap parameter type must be Map.", new Object[0]);
      } 
      if (method5 instanceof Field) {
        validateResolvableType(param1Int, (Type)converter2);
        if (this.isFormEncoded) {
          Field field = (Field)method5;
          str1 = field.value();
          boolean bool = field.encoded();
          this.gotField = true;
          Class<?> clazz = Utils.getRawType((Type)converter2);
          if (Iterable.class.isAssignableFrom(clazz)) {
            if (converter2 instanceof ParameterizedType) {
              type1 = Utils.getParameterUpperBound(0, (ParameterizedType)converter2);
              return (new ParameterHandler.Field(str1, this.retrofit.stringConverter(type1, (Annotation[])stringBuilder2), bool)).iterable();
            } 
            Method method = this.method;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(clazz.getSimpleName());
            stringBuilder2.append(" must include generic type (e.g., ");
            stringBuilder2.append(clazz.getSimpleName());
            stringBuilder2.append("<String>)");
            throw Utils.parameterError(method, param1Int, stringBuilder2.toString(), new Object[0]);
          } 
          if (clazz.isArray()) {
            type1 = boxIfPrimitive(clazz.getComponentType());
            return (new ParameterHandler.Field(str1, this.retrofit.stringConverter(type1, (Annotation[])stringBuilder2), bool)).array();
          } 
          return new ParameterHandler.Field(str1, this.retrofit.stringConverter(type1, (Annotation[])stringBuilder2), bool);
        } 
        throw Utils.parameterError(this.method, param1Int, "@Field parameters can only be used with form encoding.", new Object[0]);
      } 
      if (str1 instanceof FieldMap) {
        validateResolvableType(param1Int, type1);
        if (this.isFormEncoded) {
          Class<?> clazz = Utils.getRawType(type1);
          if (Map.class.isAssignableFrom(clazz)) {
            type1 = Utils.getSupertype(type1, clazz, Map.class);
            if (type1 instanceof ParameterizedType) {
              ParameterizedType parameterizedType = (ParameterizedType)type1;
              type1 = Utils.getParameterUpperBound(0, parameterizedType);
              if (String.class == type1) {
                type1 = Utils.getParameterUpperBound(1, parameterizedType);
                converter1 = this.retrofit.stringConverter(type1, (Annotation[])stringBuilder2);
                this.gotField = true;
                return new ParameterHandler.FieldMap(this.method, param1Int, converter1, ((FieldMap)str1).encoded());
              } 
              method4 = this.method;
              stringBuilder2 = new StringBuilder();
              stringBuilder2.append("@FieldMap keys must be of type String: ");
              stringBuilder2.append(converter1);
              throw Utils.parameterError(method4, param1Int, stringBuilder2.toString(), new Object[0]);
            } 
            throw Utils.parameterError(this.method, param1Int, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
          } 
          throw Utils.parameterError(this.method, param1Int, "@FieldMap parameter type must be Map.", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
      } 
      if (method4 instanceof Part) {
        validateResolvableType(param1Int, (Type)converter1);
        if (this.isMultipart) {
          StringBuilder stringBuilder3;
          Part part = (Part)method4;
          this.gotPart = true;
          String str = part.value();
          clazz1 = Utils.getRawType((Type)converter1);
          if (str.isEmpty()) {
            if (Iterable.class.isAssignableFrom(clazz1)) {
              if (converter1 instanceof ParameterizedType) {
                if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(Utils.getParameterUpperBound(0, (ParameterizedType)converter1))))
                  return ParameterHandler.RawPart.INSTANCE.iterable(); 
                throw Utils.parameterError(this.method, param1Int, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
              } 
              method1 = this.method;
              stringBuilder3 = new StringBuilder();
              stringBuilder3.append(clazz1.getSimpleName());
              stringBuilder3.append(" must include generic type (e.g., ");
              stringBuilder3.append(clazz1.getSimpleName());
              stringBuilder3.append("<String>)");
              throw Utils.parameterError(method1, param1Int, stringBuilder3.toString(), new Object[0]);
            } 
            if (clazz1.isArray()) {
              if (MultipartBody.Part.class.isAssignableFrom(clazz1.getComponentType()))
                return ParameterHandler.RawPart.INSTANCE.array(); 
              throw Utils.parameterError(this.method, param1Int, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
            } 
            if (MultipartBody.Part.class.isAssignableFrom(clazz1))
              return ParameterHandler.RawPart.INSTANCE; 
            throw Utils.parameterError(this.method, param1Int, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
          } 
          StringBuilder stringBuilder4 = new StringBuilder();
          stringBuilder4.append("form-data; name=\"");
          stringBuilder4.append(str);
          stringBuilder4.append("\"");
          Headers headers = Headers.of(new String[] { "Content-Disposition", stringBuilder4.toString(), "Content-Transfer-Encoding", part.encoding() });
          if (Iterable.class.isAssignableFrom(clazz1)) {
            if (stringBuilder3 instanceof ParameterizedType) {
              Type type3 = Utils.getParameterUpperBound(0, (ParameterizedType)stringBuilder3);
              if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(type3))) {
                converter = this.retrofit.requestBodyConverter(type3, (Annotation[])method1, this.methodAnnotations);
                return (new ParameterHandler.Part(this.method, param1Int, headers, converter)).iterable();
              } 
              throw Utils.parameterError(this.method, param1Int, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
            } 
            method1 = this.method;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(clazz1.getSimpleName());
            stringBuilder3.append(" must include generic type (e.g., ");
            stringBuilder3.append(clazz1.getSimpleName());
            stringBuilder3.append("<String>)");
            throw Utils.parameterError(method1, param1Int, stringBuilder3.toString(), new Object[0]);
          } 
          if (clazz1.isArray()) {
            Class<?> clazz = boxIfPrimitive(clazz1.getComponentType());
            if (!MultipartBody.Part.class.isAssignableFrom(clazz)) {
              converter = this.retrofit.requestBodyConverter(clazz, (Annotation[])method1, this.methodAnnotations);
              return (new ParameterHandler.Part(this.method, param1Int, headers, converter)).array();
            } 
            throw Utils.parameterError(this.method, param1Int, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
          } 
          if (!MultipartBody.Part.class.isAssignableFrom(clazz1)) {
            converter = this.retrofit.requestBodyConverter((Type)converter, (Annotation[])method1, this.methodAnnotations);
            return new ParameterHandler.Part(this.method, param1Int, headers, converter);
          } 
          throw Utils.parameterError(this.method, param1Int, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "@Part parameters can only be used with multipart encoding.", new Object[0]);
      } 
      if (clazz1 instanceof PartMap) {
        validateResolvableType(param1Int, (Type)converter);
        if (this.isMultipart) {
          this.gotPart = true;
          Class<?> clazz = Utils.getRawType((Type)converter);
          if (Map.class.isAssignableFrom(clazz)) {
            Type type3 = Utils.getSupertype((Type)converter, clazz, Map.class);
            if (type3 instanceof ParameterizedType) {
              ParameterizedType parameterizedType = (ParameterizedType)type3;
              type3 = Utils.getParameterUpperBound(0, parameterizedType);
              if (String.class == type3) {
                type3 = Utils.getParameterUpperBound(1, parameterizedType);
                if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(type3))) {
                  converter = this.retrofit.requestBodyConverter(type3, (Annotation[])method1, this.methodAnnotations);
                  PartMap partMap = (PartMap)clazz1;
                  return new ParameterHandler.PartMap(this.method, param1Int, converter, partMap.encoding());
                } 
                throw Utils.parameterError(this.method, param1Int, "@PartMap values cannot be MultipartBody.Part. Use @Part List<Part> or a different value type instead.", new Object[0]);
              } 
              method3 = this.method;
              stringBuilder1 = new StringBuilder();
              stringBuilder1.append("@PartMap keys must be of type String: ");
              stringBuilder1.append(converter);
              throw Utils.parameterError(method3, param1Int, stringBuilder1.toString(), new Object[0]);
            } 
            throw Utils.parameterError(this.method, param1Int, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
          } 
          throw Utils.parameterError(this.method, param1Int, "@PartMap parameter type must be Map.", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
      } 
      if (method3 instanceof retrofit2.http.Body) {
        validateResolvableType(param1Int, (Type)converter);
        if (!this.isFormEncoded && !this.isMultipart) {
          if (!this.gotBody)
            try {
              Converter<?, RequestBody> converter5 = this.retrofit.requestBodyConverter((Type)converter, (Annotation[])stringBuilder1, this.methodAnnotations);
              this.gotBody = true;
              return new ParameterHandler.Body(this.method, param1Int, converter5);
            } catch (RuntimeException runtimeException) {
              throw Utils.parameterError(this.method, runtimeException, param1Int, "Unable to create @Body converter for %s", new Object[] { converter });
            }  
          throw Utils.parameterError(this.method, param1Int, "Multiple @Body method annotations found.", new Object[0]);
        } 
        throw Utils.parameterError(this.method, param1Int, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
      } 
      if (method3 instanceof retrofit2.http.Tag) {
        validateResolvableType(param1Int, (Type)converter);
        Class<?> clazz = Utils.getRawType((Type)converter);
        int i = param1Int - 1;
        while (i >= 0) {
          ParameterHandler<?> parameterHandler = this.parameterHandlers[i];
          if (!(parameterHandler instanceof ParameterHandler.Tag) || !((ParameterHandler.Tag)parameterHandler).cls.equals(clazz)) {
            i--;
            continue;
          } 
          Method method = this.method;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("@Tag type ");
          stringBuilder.append(clazz.getName());
          stringBuilder.append(" is duplicate of parameter #");
          stringBuilder.append(i + 1);
          stringBuilder.append(" and would always overwrite its value.");
          throw Utils.parameterError(method, param1Int, stringBuilder.toString(), new Object[0]);
        } 
        return new ParameterHandler.Tag(clazz);
      } 
      return null;
    }
    
    public static Set<String> parsePathParameters(String param1String) {
      Matcher matcher = PARAM_URL_REGEX.matcher(param1String);
      LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
      while (matcher.find())
        linkedHashSet.add(matcher.group(1)); 
      return linkedHashSet;
    }
    
    private void validatePathName(int param1Int, String param1String) {
      if (PARAM_NAME_REGEX.matcher(param1String).matches()) {
        if (this.relativeUrlParamNames.contains(param1String))
          return; 
        throw Utils.parameterError(this.method, param1Int, "URL \"%s\" does not contain \"{%s}\".", new Object[] { this.relativeUrl, param1String });
      } 
      throw Utils.parameterError(this.method, param1Int, "@Path parameter name must match %s. Found: %s", new Object[] { PARAM_URL_REGEX.pattern(), param1String });
    }
    
    private void validateResolvableType(int param1Int, Type param1Type) {
      if (!Utils.hasUnresolvableType(param1Type))
        return; 
      throw Utils.parameterError(this.method, param1Int, "Parameter type must not include a type variable or wildcard: %s", new Object[] { param1Type });
    }
    
    public RequestFactory build() {
      Annotation[] arrayOfAnnotation = this.methodAnnotations;
      int i = arrayOfAnnotation.length;
      byte b;
      for (b = 0; b < i; b++)
        parseMethodAnnotation(arrayOfAnnotation[b]); 
      if (this.httpMethod != null) {
        if (!this.hasBody)
          if (!this.isMultipart) {
            if (this.isFormEncoded)
              throw Utils.methodError(this.method, "FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]); 
          } else {
            throw Utils.methodError(this.method, "Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
          }  
        i = this.parameterAnnotationsArray.length;
        this.parameterHandlers = (ParameterHandler<?>[])new ParameterHandler[i];
        b = 0;
        while (true) {
          boolean bool = true;
          if (b < i) {
            ParameterHandler<?>[] arrayOfParameterHandler = this.parameterHandlers;
            Type type = this.parameterTypes[b];
            Annotation[] arrayOfAnnotation1 = this.parameterAnnotationsArray[b];
            if (b != i - 1)
              bool = false; 
            arrayOfParameterHandler[b] = parseParameter(b, type, arrayOfAnnotation1, bool);
            b++;
            continue;
          } 
          if (this.relativeUrl != null || this.gotUrl) {
            bool = this.isFormEncoded;
            if (bool || this.isMultipart || this.hasBody || !this.gotBody) {
              if (!bool || this.gotField) {
                if (!this.isMultipart || this.gotPart)
                  return new RequestFactory(this); 
                throw Utils.methodError(this.method, "Multipart method must contain at least one @Part.", new Object[0]);
              } 
              throw Utils.methodError(this.method, "Form-encoded method must contain at least one @Field.", new Object[0]);
            } 
            throw Utils.methodError(this.method, "Non-body HTTP method cannot contain @Body.", new Object[0]);
          } 
          throw Utils.methodError(this.method, "Missing either @%s URL or @Url parameter.", new Object[] { this.httpMethod });
        } 
      } 
      throw Utils.methodError(this.method, "HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
    }
  }
}
