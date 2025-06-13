package retrofit2.converter.gson;

import I丨L.I1I;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
  private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
  
  private static final Charset UTF_8 = Charset.forName("UTF-8");
  
  private final TypeAdapter<T> adapter;
  
  private final Gson gson;
  
  public GsonRequestBodyConverter(Gson paramGson, TypeAdapter<T> paramTypeAdapter) {
    this.gson = paramGson;
    this.adapter = paramTypeAdapter;
  }
  
  public RequestBody convert(T paramT) throws IOException {
    I1I i1I = new I1I();
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(i1I.丨丨丨1丨(), UTF_8);
    JsonWriter jsonWriter = this.gson.newJsonWriter(outputStreamWriter);
    this.adapter.write(jsonWriter, paramT);
    jsonWriter.close();
    return RequestBody.create(MEDIA_TYPE, i1I.L11丨());
  }
}
