package I丨L;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public interface I11li1 extends Closeable, Flushable {
  void close() throws IOException;
  
  void flush() throws IOException;
  
  I11L timeout();
  
  void write(I1I paramI1I, long paramLong) throws IOException;
}
