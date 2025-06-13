package I丨L;

import java.io.IOException;

public abstract class I丨iL implements I11li1 {
  private final I11li1 delegate;
  
  public I丨iL(I11li1 paramI11li1) {
    if (paramI11li1 != null) {
      this.delegate = paramI11li1;
      return;
    } 
    throw new IllegalArgumentException("delegate == null");
  }
  
  public void close() throws IOException {
    this.delegate.close();
  }
  
  public final I11li1 delegate() {
    return this.delegate;
  }
  
  public void flush() throws IOException {
    this.delegate.flush();
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
  
  public void write(I1I paramI1I, long paramLong) throws IOException {
    this.delegate.write(paramI1I, paramLong);
  }
}
