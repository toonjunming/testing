package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final TypeAdapter<T> adapter;
  
  private final Gson gson;
  
  public GsonResponseBodyConverter(Gson paramGson, TypeAdapter<T> paramTypeAdapter) {
    this.gson = paramGson;
    this.adapter = paramTypeAdapter;
  }
  
  public T convert(ResponseBody paramResponseBody) throws IOException {
    JsonReader jsonReader = this.gson.newJsonReader(paramResponseBody.charStream());
    try {
      T t = this.adapter.read(jsonReader);
      JsonToken jsonToken2 = jsonReader.peek();
      JsonToken jsonToken1 = JsonToken.END_DOCUMENT;
      if (jsonToken2 == jsonToken1)
        return t; 
      JsonIOException jsonIOException = new JsonIOException();
      this("JSON document was not fully consumed.");
      throw jsonIOException;
    } finally {
      paramResponseBody.close();
    } 
  }
}
