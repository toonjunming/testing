package com.mysql.jdbc.profiler;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.log.Log;
import java.sql.SQLException;
import java.util.Properties;

public class LoggingProfilerEventHandler implements ProfilerEventHandler {
  private Log log;
  
  public void consumeEvent(ProfilerEvent paramProfilerEvent) {
    if (paramProfilerEvent.eventType == 0) {
      this.log.logWarn(paramProfilerEvent);
    } else {
      this.log.logInfo(paramProfilerEvent);
    } 
  }
  
  public void destroy() {
    this.log = null;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.log = paramConnection.getLog();
  }
}
