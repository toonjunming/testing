package retrofit2;

import I丨L.I11L;
import java.io.IOException;
import okhttp3.Request;

public interface Call<T> extends Cloneable {
  void cancel();
  
  Call<T> clone();
  
  void enqueue(Callback<T> paramCallback);
  
  Response<T> execute() throws IOException;
  
  boolean isCanceled();
  
  boolean isExecuted();
  
  Request request();
  
  I11L timeout();
}
