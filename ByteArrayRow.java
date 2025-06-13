package com.mysql.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class ByteArrayRow extends ResultSetRow {
  public byte[][] internalRowData;
  
  public ByteArrayRow(byte[][] paramArrayOfbyte, ExceptionInterceptor paramExceptionInterceptor) {
    super(paramExceptionInterceptor);
    this.internalRowData = paramArrayOfbyte;
  }
  
  public void closeOpenStreams() {}
  
  public InputStream getBinaryInputStream(int paramInt) throws SQLException {
    return (this.internalRowData[paramInt] == null) ? null : new ByteArrayInputStream(this.internalRowData[paramInt]);
  }
  
  public int getBytesSize() {
    byte[][] arrayOfByte = this.internalRowData;
    byte b = 0;
    if (arrayOfByte == null)
      return 0; 
    int i = 0;
    while (true) {
      arrayOfByte = this.internalRowData;
      if (b < arrayOfByte.length) {
        int j = i;
        if (arrayOfByte[b] != null)
          j = i + (arrayOfByte[b]).length; 
        b++;
        i = j;
        continue;
      } 
      return i;
    } 
  }
  
  public byte[] getColumnValue(int paramInt) throws SQLException {
    return this.internalRowData[paramInt];
  }
  
  public Date getDateFast(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    byte[] arrayOfByte1 = arrayOfByte[paramInt];
    return (arrayOfByte1 == null) ? null : getDateFast(paramInt, arrayOfByte[paramInt], 0, arrayOfByte1.length, paramMySQLConnection, paramResultSetImpl, paramCalendar);
  }
  
  public int getInt(int paramInt) {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0 : StringUtils.getInt(arrayOfByte[paramInt]);
  }
  
  public long getLong(int paramInt) {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0L : StringUtils.getLong(arrayOfByte[paramInt]);
  }
  
  public Date getNativeDate(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException {
    byte[] arrayOfByte = this.internalRowData[paramInt];
    return (arrayOfByte == null) ? null : getNativeDate(paramInt, arrayOfByte, 0, arrayOfByte.length, paramMySQLConnection, paramResultSetImpl, paramCalendar);
  }
  
  public Object getNativeDateTimeValue(int paramInt1, Calendar paramCalendar, int paramInt2, int paramInt3, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    byte[] arrayOfByte = this.internalRowData[paramInt1];
    return (arrayOfByte == null) ? null : getNativeDateTimeValue(paramInt1, arrayOfByte, 0, arrayOfByte.length, paramCalendar, paramInt2, paramInt3, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public double getNativeDouble(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0.0D : getNativeDouble(arrayOfByte[paramInt], 0);
  }
  
  public float getNativeFloat(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0.0F : getNativeFloat(arrayOfByte[paramInt], 0);
  }
  
  public int getNativeInt(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0 : getNativeInt(arrayOfByte[paramInt], 0);
  }
  
  public long getNativeLong(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0L : getNativeLong(arrayOfByte[paramInt], 0);
  }
  
  public short getNativeShort(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0 : getNativeShort(arrayOfByte[paramInt], 0);
  }
  
  public Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    byte[] arrayOfByte = this.internalRowData[paramInt];
    return (arrayOfByte == null) ? null : getNativeTime(paramInt, arrayOfByte, 0, arrayOfByte.length, paramCalendar, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    byte[] arrayOfByte = this.internalRowData[paramInt];
    return (arrayOfByte == null) ? null : getNativeTimestamp(arrayOfByte, 0, arrayOfByte.length, paramCalendar, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public Reader getReader(int paramInt) throws SQLException {
    InputStream inputStream = getBinaryInputStream(paramInt);
    if (inputStream == null)
      return null; 
    try {
      return new InputStreamReader(inputStream, this.metadata[paramInt].getEncoding());
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      SQLException sQLException = SQLError.createSQLException("", this.exceptionInterceptor);
      sQLException.initCause(unsupportedEncodingException);
      throw sQLException;
    } 
  }
  
  public String getString(int paramInt, String paramString, MySQLConnection paramMySQLConnection) throws SQLException {
    byte[] arrayOfByte = this.internalRowData[paramInt];
    return (arrayOfByte == null) ? null : getString(paramString, paramMySQLConnection, arrayOfByte, 0, arrayOfByte.length);
  }
  
  public Time getTimeFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    byte[] arrayOfByte1 = arrayOfByte[paramInt];
    return (arrayOfByte1 == null) ? null : getTimeFast(paramInt, arrayOfByte[paramInt], 0, arrayOfByte1.length, paramCalendar, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public Timestamp getTimestampFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean1, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, boolean paramBoolean2, boolean paramBoolean3) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    byte[] arrayOfByte1 = arrayOfByte[paramInt];
    return (arrayOfByte1 == null) ? null : getTimestampFast(paramInt, arrayOfByte[paramInt], 0, arrayOfByte1.length, paramCalendar, paramTimeZone, paramBoolean1, paramMySQLConnection, paramResultSetImpl, paramBoolean2, paramBoolean3);
  }
  
  public boolean isFloatingPointNumber(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    byte[] arrayOfByte1 = arrayOfByte[paramInt];
    if (arrayOfByte[paramInt] != null && (arrayOfByte[paramInt]).length != 0)
      for (paramInt = 0; paramInt < arrayOfByte1.length; paramInt++) {
        if ((char)arrayOfByte1[paramInt] == 'e' || (char)arrayOfByte1[paramInt] == 'E')
          return true; 
      }  
    return false;
  }
  
  public boolean isNull(int paramInt) throws SQLException {
    boolean bool;
    if (this.internalRowData[paramInt] == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public long length(int paramInt) throws SQLException {
    byte[][] arrayOfByte = this.internalRowData;
    return (arrayOfByte[paramInt] == null) ? 0L : (arrayOfByte[paramInt]).length;
  }
  
  public void setColumnValue(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    this.internalRowData[paramInt] = paramArrayOfbyte;
  }
}
