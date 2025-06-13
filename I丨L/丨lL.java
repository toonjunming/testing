package I丨L;

import java.io.Closeable;
import java.io.IOException;

public interface 丨lL extends Closeable {
  void close() throws IOException;
  
  long read(I1I paramI1I, long paramLong) throws IOException;
  
  I11L timeout();
}
