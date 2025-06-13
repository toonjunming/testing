package com.mysql.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WatchableOutputStream extends ByteArrayOutputStream {
  private OutputStreamWatcher watcher;
  
  public void close() throws IOException {
    super.close();
    OutputStreamWatcher outputStreamWatcher = this.watcher;
    if (outputStreamWatcher != null)
      outputStreamWatcher.streamClosed(this); 
  }
  
  public void setWatcher(OutputStreamWatcher paramOutputStreamWatcher) {
    this.watcher = paramOutputStreamWatcher;
  }
}
