package I丨L;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class I11L {
  public static final I11L NONE = new IL1Iii();
  
  private long deadlineNanoTime;
  
  private boolean hasDeadline;
  
  private long timeoutNanos;
  
  public static long minTimeout(long paramLong1, long paramLong2) {
    return (paramLong1 == 0L) ? paramLong2 : ((paramLong2 == 0L) ? paramLong1 : ((paramLong1 < paramLong2) ? paramLong1 : paramLong2));
  }
  
  public I11L clearDeadline() {
    this.hasDeadline = false;
    return this;
  }
  
  public I11L clearTimeout() {
    this.timeoutNanos = 0L;
    return this;
  }
  
  public final I11L deadline(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong > 0L) {
      if (paramTimeUnit != null)
        return deadlineNanoTime(System.nanoTime() + paramTimeUnit.toNanos(paramLong)); 
      throw new IllegalArgumentException("unit == null");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("duration <= 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public long deadlineNanoTime() {
    if (this.hasDeadline)
      return this.deadlineNanoTime; 
    throw new IllegalStateException("No deadline");
  }
  
  public I11L deadlineNanoTime(long paramLong) {
    this.hasDeadline = true;
    this.deadlineNanoTime = paramLong;
    return this;
  }
  
  public boolean hasDeadline() {
    return this.hasDeadline;
  }
  
  public void throwIfReached() throws IOException {
    if (!Thread.interrupted()) {
      if (!this.hasDeadline || this.deadlineNanoTime - System.nanoTime() > 0L)
        return; 
      throw new InterruptedIOException("deadline reached");
    } 
    Thread.currentThread().interrupt();
    throw new InterruptedIOException("interrupted");
  }
  
  public I11L timeout(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong >= 0L) {
      if (paramTimeUnit != null) {
        this.timeoutNanos = paramTimeUnit.toNanos(paramLong);
        return this;
      } 
      throw new IllegalArgumentException("unit == null");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("timeout < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public long timeoutNanos() {
    return this.timeoutNanos;
  }
  
  public final void waitUntilNotified(Object paramObject) throws InterruptedIOException {
    try {
      boolean bool = hasDeadline();
      long l1 = timeoutNanos();
      long l2 = 0L;
      if (!bool && l1 == 0L) {
        paramObject.wait();
        return;
      } 
      long l3 = System.nanoTime();
      if (bool && l1 != 0L) {
        l1 = Math.min(l1, deadlineNanoTime() - l3);
      } else if (bool) {
        l1 = deadlineNanoTime() - l3;
      } 
      if (l1 > 0L) {
        l2 = l1 / 1000000L;
        Long.signum(l2);
        int i = (int)(l1 - 1000000L * l2);
        paramObject.wait(l2, i);
        l2 = System.nanoTime() - l3;
      } 
      if (l2 < l1)
        return; 
      paramObject = new InterruptedIOException();
      super("timeout");
      throw paramObject;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new InterruptedIOException("interrupted");
    } 
  }
  
  public final class IL1Iii extends I11L {
    public I11L deadlineNanoTime(long param1Long) {
      return this;
    }
    
    public void throwIfReached() throws IOException {}
    
    public I11L timeout(long param1Long, TimeUnit param1TimeUnit) {
      return this;
    }
  }
}
