package com.mysql.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Struct;

public class JDBC4ResultSet extends ResultSetImpl {
  public JDBC4ResultSet(long paramLong1, long paramLong2, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) {
    super(paramLong1, paramLong2, paramMySQLConnection, paramStatementImpl);
  }
  
  public JDBC4ResultSet(String paramString, Field[] paramArrayOfField, RowData paramRowData, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) throws SQLException {
    super(paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl);
  }
  
  private final NClob getNClobFromString(String paramString, int paramInt) throws SQLException {
    return new JDBC4NClob(paramString, getExceptionInterceptor());
  }
  
  private String getStringForNClob(int paramInt) throws SQLException {
    try {
      byte[] arrayOfByte;
      if (!this.isBinaryEncoded) {
        arrayOfByte = getBytes(paramInt);
      } else {
        arrayOfByte = getNativeBytes(paramInt, true);
      } 
      if (arrayOfByte != null) {
        String str = new String(arrayOfByte, "UTF-8");
      } else {
        arrayOfByte = null;
      } 
      return (String)arrayOfByte;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unsupported character encoding ");
      stringBuilder.append("UTF-8");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public int getHoldability() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public Reader getNCharacterStream(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    String str = this.fields[paramInt - 1].getEncoding();
    if (str != null && str.equals("UTF-8"))
      return getCharacterStream(paramInt); 
    throw new SQLException("Can not call getNCharacterStream() when field's charset isn't UTF-8");
  }
  
  public Reader getNCharacterStream(String paramString) throws SQLException {
    return getNCharacterStream(findColumn(paramString));
  }
  
  public NClob getNClob(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    String str = this.fields[paramInt - 1].getEncoding();
    if (str != null && str.equals("UTF-8")) {
      if (!this.isBinaryEncoded) {
        str = getStringForNClob(paramInt);
        return (str == null) ? null : new JDBC4NClob(str, getExceptionInterceptor());
      } 
      return getNativeNClob(paramInt);
    } 
    throw new SQLException("Can not call getNClob() when field's charset isn't UTF-8");
  }
  
  public NClob getNClob(String paramString) throws SQLException {
    return getNClob(findColumn(paramString));
  }
  
  public String getNString(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    String str = this.fields[paramInt - 1].getEncoding();
    if (str != null && str.equals("UTF-8"))
      return getString(paramInt); 
    throw new SQLException("Can not call getNString() when field's charset isn't UTF-8");
  }
  
  public String getNString(String paramString) throws SQLException {
    return getNString(findColumn(paramString));
  }
  
  public NClob getNativeNClob(int paramInt) throws SQLException {
    String str = getStringForNClob(paramInt);
    return (str == null) ? null : getNClobFromString(str, paramInt);
  }
  
  public <T> T getObject(int paramInt, Class<T> paramClass) throws SQLException {
    if (paramClass != null) {
      if (!paramClass.equals(Struct.class))
        return (T)(paramClass.equals(RowId.class) ? getRowId(paramInt) : (paramClass.equals(NClob.class) ? getNClob(paramInt) : (paramClass.equals(SQLXML.class) ? getSQLXML(paramInt) : (Object)super.getObject(paramInt, paramClass)))); 
      throw new SQLFeatureNotSupportedException();
    } 
    throw SQLError.createSQLException("Type parameter can not be null", "S1009", getExceptionInterceptor());
  }
  
  public RowId getRowId(int paramInt) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public RowId getRowId(String paramString) throws SQLException {
    return getRowId(findColumn(paramString));
  }
  
  public SQLXML getSQLXML(int paramInt) throws SQLException {
    checkColumnBounds(paramInt);
    return new JDBC4MysqlSQLXML(this, paramInt, getExceptionInterceptor());
  }
  
  public SQLXML getSQLXML(String paramString) throws SQLException {
    return getSQLXML(findColumn(paramString));
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    checkClosed();
    return paramClass.isInstance(this);
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
    updateAsciiStream(findColumn(paramString), paramInputStream);
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    updateAsciiStream(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
    updateBinaryStream(findColumn(paramString), paramInputStream);
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    updateBinaryStream(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateBlob(int paramInt, InputStream paramInputStream) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateBlob(String paramString, InputStream paramInputStream) throws SQLException {
    updateBlob(findColumn(paramString), paramInputStream);
  }
  
  public void updateBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
    updateBlob(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateCharacterStream(int paramInt, Reader paramReader) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader) throws SQLException {
    updateCharacterStream(findColumn(paramString), paramReader);
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
    updateCharacterStream(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateClob(int paramInt, Reader paramReader) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateClob(String paramString, Reader paramReader) throws SQLException {
    updateClob(findColumn(paramString), paramReader);
  }
  
  public void updateClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
    updateClob(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader) throws SQLException {
    updateNCharacterStream(findColumn(paramString), paramReader);
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
    updateNCharacterStream(findColumn(paramString), paramReader, paramInt);
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
    updateNCharacterStream(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNClob(int paramInt, Reader paramReader) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNClob(int paramInt, NClob paramNClob) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNClob(String paramString, Reader paramReader) throws SQLException {
    updateNClob(findColumn(paramString), paramReader);
  }
  
  public void updateNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
    updateNClob(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNClob(String paramString, NClob paramNClob) throws SQLException {
    updateNClob(findColumn(paramString), paramNClob);
  }
  
  public void updateNString(int paramInt, String paramString) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateNString(String paramString1, String paramString2) throws SQLException {
    updateNString(findColumn(paramString1), paramString2);
  }
  
  public void updateRowId(int paramInt, RowId paramRowId) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateRowId(String paramString, RowId paramRowId) throws SQLException {
    updateRowId(findColumn(paramString), paramRowId);
  }
  
  public void updateSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
    throw new NotUpdatable();
  }
  
  public void updateSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
    updateSQLXML(findColumn(paramString), paramSQLXML);
  }
}
