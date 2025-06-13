package I丨L;

import java.io.IOException;

public abstract class L丨1丨1丨I implements 丨lL {
  private final 丨lL delegate;
  
  public L丨1丨1丨I(丨lL param丨lL) {
    if (param丨lL != null) {
      this.delegate = param丨lL;
      return;
    } 
    throw new IllegalArgumentException("delegate == null");
  }
  
  public void close() throws IOException {
    this.delegate.close();
  }
  
  public final 丨lL delegate() {
    return this.delegate;
  }
  
  public long read(I1I paramI1I, long paramLong) throws IOException {
    return this.delegate.read(paramI1I, paramLong);
  }
  
  public I11L timeout() {
    return this.delegate.timeout();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append("(");
    stringBuilder.append(this.delegate.toString());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}
