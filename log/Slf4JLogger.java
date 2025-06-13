package com.mysql.jdbc.log;

import Ilil.I丨L.I1I;
import Ilil.I丨L.ILil;

public class Slf4JLogger implements Log {
  private ILil log;
  
  public Slf4JLogger(String paramString) {
    this.log = I1I.丨il(paramString);
  }
  
  public boolean isDebugEnabled() {
    return this.log.isDebugEnabled();
  }
  
  public boolean isErrorEnabled() {
    return this.log.isErrorEnabled();
  }
  
  public boolean isFatalEnabled() {
    return this.log.isErrorEnabled();
  }
  
  public boolean isInfoEnabled() {
    return this.log.isInfoEnabled();
  }
  
  public boolean isTraceEnabled() {
    return this.log.isTraceEnabled();
  }
  
  public boolean isWarnEnabled() {
    return this.log.isWarnEnabled();
  }
  
  public void logDebug(Object paramObject) {
    this.log.debug(paramObject.toString());
  }
  
  public void logDebug(Object paramObject, Throwable paramThrowable) {
    this.log.debug(paramObject.toString(), paramThrowable);
  }
  
  public void logError(Object paramObject) {
    this.log.error(paramObject.toString());
  }
  
  public void logError(Object paramObject, Throwable paramThrowable) {
    this.log.error(paramObject.toString(), paramThrowable);
  }
  
  public void logFatal(Object paramObject) {
    this.log.error(paramObject.toString());
  }
  
  public void logFatal(Object paramObject, Throwable paramThrowable) {
    this.log.error(paramObject.toString(), paramThrowable);
  }
  
  public void logInfo(Object paramObject) {
    this.log.info(paramObject.toString());
  }
  
  public void logInfo(Object paramObject, Throwable paramThrowable) {
    this.log.info(paramObject.toString(), paramThrowable);
  }
  
  public void logTrace(Object paramObject) {
    this.log.trace(paramObject.toString());
  }
  
  public void logTrace(Object paramObject, Throwable paramThrowable) {
    this.log.trace(paramObject.toString(), paramThrowable);
  }
  
  public void logWarn(Object paramObject) {
    this.log.warn(paramObject.toString());
  }
  
  public void logWarn(Object paramObject, Throwable paramThrowable) {
    this.log.warn(paramObject.toString(), paramThrowable);
  }
}
