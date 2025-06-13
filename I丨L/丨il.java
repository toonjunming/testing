package I丨L;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class 丨il extends I11L {
  public I11L IL1Iii;
  
  public 丨il(I11L paramI11L) {
    if (paramI11L != null) {
      this.IL1Iii = paramI11L;
      return;
    } 
    throw new IllegalArgumentException("delegate == null");
  }
  
  public final I11L IL1Iii() {
    return this.IL1Iii;
  }
  
  public final 丨il ILil(I11L paramI11L) {
    if (paramI11L != null) {
      this.IL1Iii = paramI11L;
      return this;
    } 
    throw new IllegalArgumentException("delegate == null");
  }
  
  public I11L clearDeadline() {
    return this.IL1Iii.clearDeadline();
  }
  
  public I11L clearTimeout() {
    return this.IL1Iii.clearTimeout();
  }
  
  public long deadlineNanoTime() {
    return this.IL1Iii.deadlineNanoTime();
  }
  
  public I11L deadlineNanoTime(long paramLong) {
    return this.IL1Iii.deadlineNanoTime(paramLong);
  }
  
  public boolean hasDeadline() {
    return this.IL1Iii.hasDeadline();
  }
  
  public void throwIfReached() throws IOException {
    this.IL1Iii.throwIfReached();
  }
  
  public I11L timeout(long paramLong, TimeUnit paramTimeUnit) {
    return this.IL1Iii.timeout(paramLong, paramTimeUnit);
  }
  
  public long timeoutNanos() {
    return this.IL1Iii.timeoutNanos();
  }
}
