package com.mysql.jdbc.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Jdk14Logger implements Log {
  private static final Level DEBUG = Level.FINE;
  
  private static final Level ERROR;
  
  private static final Level FATAL;
  
  private static final Level INFO = Level.INFO;
  
  private static final Level TRACE = Level.FINEST;
  
  private static final Level WARN = Level.WARNING;
  
  public Logger jdkLogger = null;
  
  public Jdk14Logger(String paramString) {
    this.jdkLogger = Logger.getLogger(paramString);
  }
  
  private static final int findCallerStackDepth(StackTraceElement[] paramArrayOfStackTraceElement) {
    int i = paramArrayOfStackTraceElement.length;
    for (byte b = 0; b < i; b++) {
      String str = paramArrayOfStackTraceElement[b].getClassName();
      if (!str.startsWith("com.mysql.jdbc") || str.startsWith("com.mysql.jdbc.compliance"))
        return b; 
    } 
    return 0;
  }
  
  private void logInternal(Level paramLevel, Object paramObject, Throwable paramThrowable) {
    if (this.jdkLogger.isLoggable(paramLevel)) {
      String str2;
      boolean bool = paramObject instanceof com.mysql.jdbc.profiler.ProfilerEvent;
      String str1 = "N/A";
      if (bool) {
        str1 = LogUtils.expandProfilerEventIfNecessary(paramObject).toString();
        paramObject = "N/A";
        str2 = "N/A";
      } else {
        StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
        int i = findCallerStackDepth(arrayOfStackTraceElement);
        if (i != 0) {
          str1 = arrayOfStackTraceElement[i].getClassName();
          str2 = arrayOfStackTraceElement[i].getMethodName();
        } else {
          str2 = "N/A";
        } 
        String str = String.valueOf(paramObject);
        paramObject = str1;
        str1 = str;
      } 
      if (paramThrowable == null) {
        this.jdkLogger.logp(paramLevel, (String)paramObject, str2, str1);
      } else {
        this.jdkLogger.logp(paramLevel, (String)paramObject, str2, str1, paramThrowable);
      } 
    } 
  }
  
  public boolean isDebugEnabled() {
    return this.jdkLogger.isLoggable(Level.FINE);
  }
  
  public boolean isErrorEnabled() {
    return this.jdkLogger.isLoggable(Level.SEVERE);
  }
  
  public boolean isFatalEnabled() {
    return this.jdkLogger.isLoggable(Level.SEVERE);
  }
  
  public boolean isInfoEnabled() {
    return this.jdkLogger.isLoggable(Level.INFO);
  }
  
  public boolean isTraceEnabled() {
    return this.jdkLogger.isLoggable(Level.FINEST);
  }
  
  public boolean isWarnEnabled() {
    return this.jdkLogger.isLoggable(Level.WARNING);
  }
  
  public void logDebug(Object paramObject) {
    logInternal(DEBUG, paramObject, null);
  }
  
  public void logDebug(Object paramObject, Throwable paramThrowable) {
    logInternal(DEBUG, paramObject, paramThrowable);
  }
  
  public void logError(Object paramObject) {
    logInternal(ERROR, paramObject, null);
  }
  
  public void logError(Object paramObject, Throwable paramThrowable) {
    logInternal(ERROR, paramObject, paramThrowable);
  }
  
  public void logFatal(Object paramObject) {
    logInternal(FATAL, paramObject, null);
  }
  
  public void logFatal(Object paramObject, Throwable paramThrowable) {
    logInternal(FATAL, paramObject, paramThrowable);
  }
  
  public void logInfo(Object paramObject) {
    logInternal(INFO, paramObject, null);
  }
  
  public void logInfo(Object paramObject, Throwable paramThrowable) {
    logInternal(INFO, paramObject, paramThrowable);
  }
  
  public void logTrace(Object paramObject) {
    logInternal(TRACE, paramObject, null);
  }
  
  public void logTrace(Object paramObject, Throwable paramThrowable) {
    logInternal(TRACE, paramObject, paramThrowable);
  }
  
  public void logWarn(Object paramObject) {
    logInternal(WARN, paramObject, null);
  }
  
  public void logWarn(Object paramObject, Throwable paramThrowable) {
    logInternal(WARN, paramObject, paramThrowable);
  }
  
  static {
    Level level = Level.SEVERE;
    ERROR = level;
    FATAL = level;
  }
}
