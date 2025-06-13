package okhttp3;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public interface Interceptor {
  Response intercept(Chain paramChain) throws IOException;
  
  public static interface Chain {
    Call call();
    
    int connectTimeoutMillis();
    
    @Nullable
    Connection connection();
    
    Response proceed(Request param1Request) throws IOException;
    
    int readTimeoutMillis();
    
    Request request();
    
    Chain withConnectTimeout(int param1Int, TimeUnit param1TimeUnit);
    
    Chain withReadTimeout(int param1Int, TimeUnit param1TimeUnit);
    
    Chain withWriteTimeout(int param1Int, TimeUnit param1TimeUnit);
    
    int writeTimeoutMillis();
  }
}
