package com.mysql.jdbc;

import java.io.CharArrayWriter;

public class WatchableWriter extends CharArrayWriter {
  private WriterWatcher watcher;
  
  public void close() {
    super.close();
    WriterWatcher writerWatcher = this.watcher;
    if (writerWatcher != null)
      writerWatcher.writerClosed(this); 
  }
  
  public void setWatcher(WriterWatcher paramWriterWatcher) {
    this.watcher = paramWriterWatcher;
  }
}
