package javax.activation;

import java.io.IOException;

public interface CommandObject {
  void setCommandContext(String paramString, DataHandler paramDataHandler) throws IOException;
}
