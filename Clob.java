package com.mysql.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

public class Clob implements Clob, OutputStreamWatcher, WriterWatcher {
  private String charData = "";
  
  private ExceptionInterceptor exceptionInterceptor;
  
  public Clob(ExceptionInterceptor paramExceptionInterceptor) {
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  public Clob(String paramString, ExceptionInterceptor paramExceptionInterceptor) {
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  public void free() throws SQLException {
    this.charData = null;
  }
  
  public InputStream getAsciiStream() throws SQLException {
    return (this.charData != null) ? new ByteArrayInputStream(StringUtils.getBytes(this.charData)) : null;
  }
  
  public Reader getCharacterStream() throws SQLException {
    return (this.charData != null) ? new StringReader(this.charData) : null;
  }
  
  public Reader getCharacterStream(long paramLong1, long paramLong2) throws SQLException {
    return new StringReader(getSubString(paramLong1, (int)paramLong2));
  }
  
  public String getSubString(long paramLong, int paramInt) throws SQLException {
    if (paramLong >= 1L) {
      int i = (int)paramLong - 1;
      paramInt += i;
      String str = this.charData;
      if (str != null) {
        if (paramInt <= str.length())
          return this.charData.substring(i, paramInt); 
        throw SQLError.createSQLException(Messages.getString("Clob.7"), "S1009", this.exceptionInterceptor);
      } 
      return null;
    } 
    throw SQLError.createSQLException(Messages.getString("Clob.6"), "S1009", this.exceptionInterceptor);
  }
  
  public long length() throws SQLException {
    String str = this.charData;
    return (str != null) ? str.length() : 0L;
  }
  
  public long position(String paramString, long paramLong) throws SQLException {
    if (paramLong >= 1L) {
      String str = this.charData;
      long l = -1L;
      if (str != null) {
        paramLong--;
        if (paramLong <= str.length()) {
          int i = this.charData.indexOf(paramString, (int)paramLong);
          if (i == -1) {
            paramLong = l;
          } else {
            paramLong = (i + 1);
          } 
          return paramLong;
        } 
        throw SQLError.createSQLException(Messages.getString("Clob.10"), "S1009", this.exceptionInterceptor);
      } 
      return -1L;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Messages.getString("Clob.8"));
    stringBuilder.append(paramLong);
    stringBuilder.append(Messages.getString("Clob.9"));
    throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
  }
  
  public long position(Clob paramClob, long paramLong) throws SQLException {
    return position(paramClob.getSubString(1L, (int)paramClob.length()), paramLong);
  }
  
  public OutputStream setAsciiStream(long paramLong) throws SQLException {
    if (paramLong >= 1L) {
      WatchableOutputStream watchableOutputStream = new WatchableOutputStream();
      watchableOutputStream.setWatcher(this);
      if (paramLong > 0L)
        watchableOutputStream.write(StringUtils.getBytes(this.charData), 0, (int)(paramLong - 1L)); 
      return watchableOutputStream;
    } 
    throw SQLError.createSQLException(Messages.getString("Clob.0"), "S1009", this.exceptionInterceptor);
  }
  
  public Writer setCharacterStream(long paramLong) throws SQLException {
    int i = paramLong cmp 1L;
    if (i >= 0) {
      WatchableWriter watchableWriter = new WatchableWriter();
      watchableWriter.setWatcher(this);
      if (i > 0)
        watchableWriter.write(this.charData, 0, (int)(paramLong - 1L)); 
      return watchableWriter;
    } 
    throw SQLError.createSQLException(Messages.getString("Clob.1"), "S1009", this.exceptionInterceptor);
  }
  
  public int setString(long paramLong, String paramString) throws SQLException {
    if (paramLong >= 1L) {
      if (paramString != null) {
        StringBuilder stringBuilder = new StringBuilder(this.charData);
        paramLong--;
        int i = paramString.length();
        stringBuilder.replace((int)paramLong, (int)(paramLong + i), paramString);
        this.charData = stringBuilder.toString();
        return i;
      } 
      throw SQLError.createSQLException(Messages.getString("Clob.3"), "S1009", this.exceptionInterceptor);
    } 
    throw SQLError.createSQLException(Messages.getString("Clob.2"), "S1009", this.exceptionInterceptor);
  }
  
  public int setString(long paramLong, String paramString, int paramInt1, int paramInt2) throws SQLException {
    if (paramLong >= 1L) {
      if (paramString != null) {
        StringBuilder stringBuilder = new StringBuilder(this.charData);
        paramLong--;
        try {
          paramString = paramString.substring(paramInt1, paramInt1 + paramInt2);
          stringBuilder.replace((int)paramLong, (int)(paramLong + paramString.length()), paramString);
          this.charData = stringBuilder.toString();
          return paramInt2;
        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
          throw SQLError.createSQLException(stringIndexOutOfBoundsException.getMessage(), "S1009", stringIndexOutOfBoundsException, this.exceptionInterceptor);
        } 
      } 
      throw SQLError.createSQLException(Messages.getString("Clob.5"), "S1009", this.exceptionInterceptor);
    } 
    throw SQLError.createSQLException(Messages.getString("Clob.4"), "S1009", this.exceptionInterceptor);
  }
  
  public void streamClosed(WatchableOutputStream paramWatchableOutputStream) {
    int i = paramWatchableOutputStream.size();
    if (i < this.charData.length())
      try {
        paramWatchableOutputStream.write(StringUtils.getBytes(this.charData, (String)null, (String)null, false, (MySQLConnection)null, this.exceptionInterceptor), i, this.charData.length() - i);
      } catch (SQLException sQLException) {} 
    this.charData = StringUtils.toAsciiString(paramWatchableOutputStream.toByteArray());
  }
  
  public void truncate(long paramLong) throws SQLException {
    if (paramLong <= this.charData.length()) {
      this.charData = this.charData.substring(0, (int)paramLong);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Messages.getString("Clob.11"));
    stringBuilder.append(this.charData.length());
    stringBuilder.append(Messages.getString("Clob.12"));
    stringBuilder.append(paramLong);
    stringBuilder.append(Messages.getString("Clob.13"));
    throw SQLError.createSQLException(stringBuilder.toString(), this.exceptionInterceptor);
  }
  
  public void writerClosed(WatchableWriter paramWatchableWriter) {
    int i = paramWatchableWriter.size();
    if (i < this.charData.length()) {
      String str = this.charData;
      paramWatchableWriter.write(str, i, str.length() - i);
    } 
    this.charData = paramWatchableWriter.toString();
  }
  
  public void writerClosed(char[] paramArrayOfchar) {
    this.charData = new String(paramArrayOfchar);
  }
}
