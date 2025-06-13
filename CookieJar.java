package okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
  public static final CookieJar NO_COOKIES = new CookieJar() {
      public List<Cookie> loadForRequest(HttpUrl param1HttpUrl) {
        return Collections.emptyList();
      }
      
      public void saveFromResponse(HttpUrl param1HttpUrl, List<Cookie> param1List) {}
    };
  
  List<Cookie> loadForRequest(HttpUrl paramHttpUrl);
  
  void saveFromResponse(HttpUrl paramHttpUrl, List<Cookie> paramList);
}
