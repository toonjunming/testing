package com.mysql.jdbc.log;

public class NullLogger implements Log {
  public NullLogger(String paramString) {}
  
  public boolean isDebugEnabled() {
    return false;
  }
  
  public boolean isErrorEnabled() {
    return false;
  }
  
  public boolean isFatalEnabled() {
    return false;
  }
  
  public boolean isInfoEnabled() {
    return false;
  }
  
  public boolean isTraceEnabled() {
    return false;
  }
  
  public boolean isWarnEnabled() {
    return false;
  }
  
  public void logDebug(Object paramObject) {}
  
  public void logDebug(Object paramObject, Throwable paramThrowable) {}
  
  public void logError(Object paramObject) {}
  
  public void logError(Object paramObject, Throwable paramThrowable) {}
  
  public void logFatal(Object paramObject) {}
  
  public void logFatal(Object paramObject, Throwable paramThrowable) {}
  
  public void logInfo(Object paramObject) {}
  
  public void logInfo(Object paramObject, Throwable paramThrowable) {}
  
  public void logTrace(Object paramObject) {}
  
  public void logTrace(Object paramObject, Throwable paramThrowable) {}
  
  public void logWarn(Object paramObject) {}
  
  public void logWarn(Object paramObject, Throwable paramThrowable) {}
}
