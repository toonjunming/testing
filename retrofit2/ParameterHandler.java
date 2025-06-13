package retrofit2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class ParameterHandler<T> {
  public abstract void apply(RequestBuilder paramRequestBuilder, @Nullable T paramT) throws IOException;
  
  public final ParameterHandler<Object> array() {
    return new ParameterHandler<Object>() {
        public final ParameterHandler this$0;
        
        public void apply(RequestBuilder param1RequestBuilder, @Nullable Object param1Object) throws IOException {
          if (param1Object == null)
            return; 
          byte b = 0;
          int i = Array.getLength(param1Object);
          while (b < i) {
            ParameterHandler.this.apply(param1RequestBuilder, Array.get(param1Object, b));
            b++;
          } 
        }
      };
  }
  
  public final ParameterHandler<Iterable<T>> iterable() {
    return new ParameterHandler<Iterable<T>>() {
        public final ParameterHandler this$0;
        
        public void apply(RequestBuilder param1RequestBuilder, @Nullable Iterable<T> param1Iterable) throws IOException {
          if (param1Iterable == null)
            return; 
          for (Iterable<T> param1Iterable : param1Iterable)
            ParameterHandler.this.apply(param1RequestBuilder, param1Iterable); 
        }
      };
  }
  
  public static final class Body<T> extends ParameterHandler<T> {
    private final Converter<T, RequestBody> converter;
    
    private final Method method;
    
    private final int p;
    
    public Body(Method param1Method, int param1Int, Converter<T, RequestBody> param1Converter) {
      this.method = param1Method;
      this.p = param1Int;
      this.converter = param1Converter;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) {
      if (param1T != null)
        try {
          RequestBody requestBody = this.converter.convert(param1T);
          param1RequestBuilder.setBody(requestBody);
          return;
        } catch (IOException iOException) {
          Method method = this.method;
          int i = this.p;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unable to convert ");
          stringBuilder.append(param1T);
          stringBuilder.append(" to RequestBody");
          throw Utils.parameterError(method, iOException, i, stringBuilder.toString(), new Object[0]);
        }  
      throw Utils.parameterError(this.method, this.p, "Body parameter value must not be null.", new Object[0]);
    }
  }
  
  public static final class Field<T> extends ParameterHandler<T> {
    private final boolean encoded;
    
    private final String name;
    
    private final Converter<T, String> valueConverter;
    
    public Field(String param1String, Converter<T, String> param1Converter, boolean param1Boolean) {
      Objects.requireNonNull(param1String, "name == null");
      this.name = param1String;
      this.valueConverter = param1Converter;
      this.encoded = param1Boolean;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) throws IOException {
      if (param1T == null)
        return; 
      String str = this.valueConverter.convert(param1T);
      if (str == null)
        return; 
      param1RequestBuilder.addFormField(this.name, str, this.encoded);
    }
  }
  
  public static final class FieldMap<T> extends ParameterHandler<Map<String, T>> {
    private final boolean encoded;
    
    private final Method method;
    
    private final int p;
    
    private final Converter<T, String> valueConverter;
    
    public FieldMap(Method param1Method, int param1Int, Converter<T, String> param1Converter, boolean param1Boolean) {
      this.method = param1Method;
      this.p = param1Int;
      this.valueConverter = param1Converter;
      this.encoded = param1Boolean;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable Map<String, T> param1Map) throws IOException {
      if (param1Map != null) {
        Iterator<Map.Entry> iterator = param1Map.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = iterator.next();
          String str = (String)entry.getKey();
          if (str != null) {
            entry = (Map.Entry)entry.getValue();
            if (entry != null) {
              String str1 = this.valueConverter.convert((T)entry);
              if (str1 != null) {
                param1RequestBuilder.addFormField(str, str1, this.encoded);
                continue;
              } 
              Method method1 = this.method;
              int j = this.p;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Field map value '");
              stringBuilder1.append(entry);
              stringBuilder1.append("' converted to null by ");
              stringBuilder1.append(this.valueConverter.getClass().getName());
              stringBuilder1.append(" for key '");
              stringBuilder1.append(str);
              stringBuilder1.append("'.");
              throw Utils.parameterError(method1, j, stringBuilder1.toString(), new Object[0]);
            } 
            Method method = this.method;
            int i = this.p;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field map contained null value for key '");
            stringBuilder.append(str);
            stringBuilder.append("'.");
            throw Utils.parameterError(method, i, stringBuilder.toString(), new Object[0]);
          } 
          throw Utils.parameterError(this.method, this.p, "Field map contained null key.", new Object[0]);
        } 
        return;
      } 
      throw Utils.parameterError(this.method, this.p, "Field map was null.", new Object[0]);
    }
  }
  
  public static final class Header<T> extends ParameterHandler<T> {
    private final String name;
    
    private final Converter<T, String> valueConverter;
    
    public Header(String param1String, Converter<T, String> param1Converter) {
      Objects.requireNonNull(param1String, "name == null");
      this.name = param1String;
      this.valueConverter = param1Converter;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) throws IOException {
      if (param1T == null)
        return; 
      String str = this.valueConverter.convert(param1T);
      if (str == null)
        return; 
      param1RequestBuilder.addHeader(this.name, str);
    }
  }
  
  public static final class HeaderMap<T> extends ParameterHandler<Map<String, T>> {
    private final Method method;
    
    private final int p;
    
    private final Converter<T, String> valueConverter;
    
    public HeaderMap(Method param1Method, int param1Int, Converter<T, String> param1Converter) {
      this.method = param1Method;
      this.p = param1Int;
      this.valueConverter = param1Converter;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable Map<String, T> param1Map) throws IOException {
      if (param1Map != null) {
        Iterator<Map.Entry> iterator = param1Map.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = iterator.next();
          String str = (String)entry.getKey();
          if (str != null) {
            entry = (Map.Entry)entry.getValue();
            if (entry != null) {
              param1RequestBuilder.addHeader(str, this.valueConverter.convert((T)entry));
              continue;
            } 
            Method method = this.method;
            int i = this.p;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Header map contained null value for key '");
            stringBuilder.append(str);
            stringBuilder.append("'.");
            throw Utils.parameterError(method, i, stringBuilder.toString(), new Object[0]);
          } 
          throw Utils.parameterError(this.method, this.p, "Header map contained null key.", new Object[0]);
        } 
        return;
      } 
      throw Utils.parameterError(this.method, this.p, "Header map was null.", new Object[0]);
    }
  }
  
  public static final class Headers extends ParameterHandler<okhttp3.Headers> {
    private final Method method;
    
    private final int p;
    
    public Headers(Method param1Method, int param1Int) {
      this.method = param1Method;
      this.p = param1Int;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable okhttp3.Headers param1Headers) {
      if (param1Headers != null) {
        param1RequestBuilder.addHeaders(param1Headers);
        return;
      } 
      throw Utils.parameterError(this.method, this.p, "Headers parameter must not be null.", new Object[0]);
    }
  }
  
  public static final class Part<T> extends ParameterHandler<T> {
    private final Converter<T, RequestBody> converter;
    
    private final okhttp3.Headers headers;
    
    private final Method method;
    
    private final int p;
    
    public Part(Method param1Method, int param1Int, okhttp3.Headers param1Headers, Converter<T, RequestBody> param1Converter) {
      this.method = param1Method;
      this.p = param1Int;
      this.headers = param1Headers;
      this.converter = param1Converter;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) {
      if (param1T == null)
        return; 
      try {
        RequestBody requestBody = this.converter.convert(param1T);
        param1RequestBuilder.addPart(this.headers, requestBody);
        return;
      } catch (IOException iOException) {
        Method method = this.method;
        int i = this.p;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to convert ");
        stringBuilder.append(param1T);
        stringBuilder.append(" to RequestBody");
        throw Utils.parameterError(method, i, stringBuilder.toString(), new Object[] { iOException });
      } 
    }
  }
  
  public static final class PartMap<T> extends ParameterHandler<Map<String, T>> {
    private final Method method;
    
    private final int p;
    
    private final String transferEncoding;
    
    private final Converter<T, RequestBody> valueConverter;
    
    public PartMap(Method param1Method, int param1Int, Converter<T, RequestBody> param1Converter, String param1String) {
      this.method = param1Method;
      this.p = param1Int;
      this.valueConverter = param1Converter;
      this.transferEncoding = param1String;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable Map<String, T> param1Map) throws IOException {
      if (param1Map != null) {
        Iterator<Map.Entry> iterator = param1Map.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = iterator.next();
          String str = (String)entry.getKey();
          if (str != null) {
            entry = (Map.Entry)entry.getValue();
            if (entry != null) {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("form-data; name=\"");
              stringBuilder1.append(str);
              stringBuilder1.append("\"");
              param1RequestBuilder.addPart(okhttp3.Headers.of(new String[] { "Content-Disposition", stringBuilder1.toString(), "Content-Transfer-Encoding", this.transferEncoding }, ), this.valueConverter.convert((T)entry));
              continue;
            } 
            Method method = this.method;
            int i = this.p;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Part map contained null value for key '");
            stringBuilder.append(str);
            stringBuilder.append("'.");
            throw Utils.parameterError(method, i, stringBuilder.toString(), new Object[0]);
          } 
          throw Utils.parameterError(this.method, this.p, "Part map contained null key.", new Object[0]);
        } 
        return;
      } 
      throw Utils.parameterError(this.method, this.p, "Part map was null.", new Object[0]);
    }
  }
  
  public static final class Path<T> extends ParameterHandler<T> {
    private final boolean encoded;
    
    private final Method method;
    
    private final String name;
    
    private final int p;
    
    private final Converter<T, String> valueConverter;
    
    public Path(Method param1Method, int param1Int, String param1String, Converter<T, String> param1Converter, boolean param1Boolean) {
      this.method = param1Method;
      this.p = param1Int;
      Objects.requireNonNull(param1String, "name == null");
      this.name = param1String;
      this.valueConverter = param1Converter;
      this.encoded = param1Boolean;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) throws IOException {
      if (param1T != null) {
        param1RequestBuilder.addPathParam(this.name, this.valueConverter.convert(param1T), this.encoded);
        return;
      } 
      Method method = this.method;
      int i = this.p;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Path parameter \"");
      stringBuilder.append(this.name);
      stringBuilder.append("\" value must not be null.");
      throw Utils.parameterError(method, i, stringBuilder.toString(), new Object[0]);
    }
  }
  
  public static final class Query<T> extends ParameterHandler<T> {
    private final boolean encoded;
    
    private final String name;
    
    private final Converter<T, String> valueConverter;
    
    public Query(String param1String, Converter<T, String> param1Converter, boolean param1Boolean) {
      Objects.requireNonNull(param1String, "name == null");
      this.name = param1String;
      this.valueConverter = param1Converter;
      this.encoded = param1Boolean;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) throws IOException {
      if (param1T == null)
        return; 
      String str = this.valueConverter.convert(param1T);
      if (str == null)
        return; 
      param1RequestBuilder.addQueryParam(this.name, str, this.encoded);
    }
  }
  
  public static final class QueryMap<T> extends ParameterHandler<Map<String, T>> {
    private final boolean encoded;
    
    private final Method method;
    
    private final int p;
    
    private final Converter<T, String> valueConverter;
    
    public QueryMap(Method param1Method, int param1Int, Converter<T, String> param1Converter, boolean param1Boolean) {
      this.method = param1Method;
      this.p = param1Int;
      this.valueConverter = param1Converter;
      this.encoded = param1Boolean;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable Map<String, T> param1Map) throws IOException {
      if (param1Map != null) {
        Iterator<Map.Entry> iterator = param1Map.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = iterator.next();
          String str = (String)entry.getKey();
          if (str != null) {
            entry = (Map.Entry)entry.getValue();
            if (entry != null) {
              String str1 = this.valueConverter.convert((T)entry);
              if (str1 != null) {
                param1RequestBuilder.addQueryParam(str, str1, this.encoded);
                continue;
              } 
              Method method1 = this.method;
              int j = this.p;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Query map value '");
              stringBuilder1.append(entry);
              stringBuilder1.append("' converted to null by ");
              stringBuilder1.append(this.valueConverter.getClass().getName());
              stringBuilder1.append(" for key '");
              stringBuilder1.append(str);
              stringBuilder1.append("'.");
              throw Utils.parameterError(method1, j, stringBuilder1.toString(), new Object[0]);
            } 
            Method method = this.method;
            int i = this.p;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Query map contained null value for key '");
            stringBuilder.append(str);
            stringBuilder.append("'.");
            throw Utils.parameterError(method, i, stringBuilder.toString(), new Object[0]);
          } 
          throw Utils.parameterError(this.method, this.p, "Query map contained null key.", new Object[0]);
        } 
        return;
      } 
      throw Utils.parameterError(this.method, this.p, "Query map was null", new Object[0]);
    }
  }
  
  public static final class QueryName<T> extends ParameterHandler<T> {
    private final boolean encoded;
    
    private final Converter<T, String> nameConverter;
    
    public QueryName(Converter<T, String> param1Converter, boolean param1Boolean) {
      this.nameConverter = param1Converter;
      this.encoded = param1Boolean;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) throws IOException {
      if (param1T == null)
        return; 
      param1RequestBuilder.addQueryParam(this.nameConverter.convert(param1T), null, this.encoded);
    }
  }
  
  public static final class RawPart extends ParameterHandler<MultipartBody.Part> {
    public static final RawPart INSTANCE = new RawPart();
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable MultipartBody.Part param1Part) {
      if (param1Part != null)
        param1RequestBuilder.addPart(param1Part); 
    }
  }
  
  public static final class RelativeUrl extends ParameterHandler<Object> {
    private final Method method;
    
    private final int p;
    
    public RelativeUrl(Method param1Method, int param1Int) {
      this.method = param1Method;
      this.p = param1Int;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable Object param1Object) {
      if (param1Object != null) {
        param1RequestBuilder.setRelativeUrl(param1Object);
        return;
      } 
      throw Utils.parameterError(this.method, this.p, "@Url parameter is null.", new Object[0]);
    }
  }
  
  public static final class Tag<T> extends ParameterHandler<T> {
    public final Class<T> cls;
    
    public Tag(Class<T> param1Class) {
      this.cls = param1Class;
    }
    
    public void apply(RequestBuilder param1RequestBuilder, @Nullable T param1T) {
      param1RequestBuilder.addTag(this.cls, param1T);
    }
  }
}
