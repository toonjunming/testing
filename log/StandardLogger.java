package com.mysql.jdbc.log;

import com.mysql.jdbc.Util;
import java.util.Date;

public class StandardLogger implements Log {
  private static final int DEBUG = 4;
  
  private static final int ERROR = 1;
  
  private static final int FATAL = 0;
  
  private static final int INFO = 3;
  
  private static final int TRACE = 5;
  
  private static final int WARN = 2;
  
  private static StringBuffer bufferedLog;
  
  private boolean logLocationInfo = true;
  
  public StandardLogger(String paramString) {
    this(paramString, false);
  }
  
  public StandardLogger(String paramString, boolean paramBoolean) {
    this.logLocationInfo = paramBoolean;
  }
  
  public static void dropBuffer() {
    bufferedLog = null;
  }
  
  public static Appendable getBuffer() {
    return bufferedLog;
  }
  
  public static void startLoggingToBuffer() {
    bufferedLog = new StringBuffer();
  }
  
  public boolean isDebugEnabled() {
    return true;
  }
  
  public boolean isErrorEnabled() {
    return true;
  }
  
  public boolean isFatalEnabled() {
    return true;
  }
  
  public boolean isInfoEnabled() {
    return true;
  }
  
  public boolean isTraceEnabled() {
    return true;
  }
  
  public boolean isWarnEnabled() {
    return true;
  }
  
  public void logDebug(Object paramObject) {
    logInternal(4, paramObject, null);
  }
  
  public void logDebug(Object paramObject, Throwable paramThrowable) {
    logInternal(4, paramObject, paramThrowable);
  }
  
  public void logError(Object paramObject) {
    logInternal(1, paramObject, null);
  }
  
  public void logError(Object paramObject, Throwable paramThrowable) {
    logInternal(1, paramObject, paramThrowable);
  }
  
  public void logFatal(Object paramObject) {
    logInternal(0, paramObject, null);
  }
  
  public void logFatal(Object paramObject, Throwable paramThrowable) {
    logInternal(0, paramObject, paramThrowable);
  }
  
  public void logInfo(Object paramObject) {
    logInternal(3, paramObject, null);
  }
  
  public void logInfo(Object paramObject, Throwable paramThrowable) {
    logInternal(3, paramObject, paramThrowable);
  }
  
  public void logInternal(int paramInt, Object paramObject, Throwable paramThrowable) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append((new Date()).toString());
    stringBuilder.append(" ");
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt != 4) {
              if (paramInt == 5)
                stringBuilder.append("TRACE: "); 
            } else {
              stringBuilder.append("DEBUG: ");
            } 
          } else {
            stringBuilder.append("INFO: ");
          } 
        } else {
          stringBuilder.append("WARN: ");
        } 
      } else {
        stringBuilder.append("ERROR: ");
      } 
    } else {
      stringBuilder.append("FATAL: ");
    } 
    if (paramObject instanceof com.mysql.jdbc.profiler.ProfilerEvent) {
      stringBuilder.append(LogUtils.expandProfilerEventIfNecessary(paramObject));
    } else {
      if (this.logLocationInfo && paramInt != 5) {
        stringBuilder.append(LogUtils.findCallingClassAndMethod(new Throwable()));
        stringBuilder.append(" ");
      } 
      if (paramObject != null)
        stringBuilder.append(String.valueOf(paramObject)); 
    } 
    if (paramThrowable != null) {
      stringBuilder.append("\n");
      stringBuilder.append("\n");
      stringBuilder.append("EXCEPTION STACK TRACE:");
      stringBuilder.append("\n");
      stringBuilder.append("\n");
      stringBuilder.append(Util.stackTraceToString(paramThrowable));
    } 
    paramObject = stringBuilder.toString();
    System.err.println((String)paramObject);
    StringBuffer stringBuffer = bufferedLog;
    if (stringBuffer != null)
      stringBuffer.append((String)paramObject); 
  }
  
  public void logTrace(Object paramObject) {
    logInternal(5, paramObject, null);
  }
  
  public void logTrace(Object paramObject, Throwable paramThrowable) {
    logInternal(5, paramObject, paramThrowable);
  }
  
  public void logWarn(Object paramObject) {
    logInternal(2, paramObject, null);
  }
  
  public void logWarn(Object paramObject, Throwable paramThrowable) {
    logInternal(2, paramObject, paramThrowable);
  }
}
