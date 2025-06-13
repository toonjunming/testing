package okhttp3;

import I1I.IL1Iii;
import java.io.IOException;
import javax.annotation.Nullable;

public interface Authenticator {
  public static final Authenticator NONE = IL1Iii.IL1Iii;
  
  @Nullable
  Request authenticate(@Nullable Route paramRoute, Response paramResponse) throws IOException;
}
