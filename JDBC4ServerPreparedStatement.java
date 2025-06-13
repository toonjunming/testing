package com.mysql.jdbc;

import java.io.Reader;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

public class JDBC4ServerPreparedStatement extends ServerPreparedStatement {
  public JDBC4ServerPreparedStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, int paramInt1, int paramInt2) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2, paramInt1, paramInt2);
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
      checkClosed();
      if (paramReader == null) {
        setNull(paramInt, -2);
      } else {
        ServerPreparedStatement.BindValue bindValue = getBinding(paramInt, true);
        resetToType(bindValue, 252);
        bindValue.value = paramReader;
        bindValue.isLongData = true;
        if (this.connection.getUseStreamLengthsInPrepStmts()) {
          bindValue.bindLength = paramLong;
        } else {
          bindValue.bindLength = -1L;
        } 
      } 
      return;
    } 
    throw SQLError.createSQLException("Can not call setNCharacterStream() when connection character set isn't UTF-8", getExceptionInterceptor());
  }
  
  public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
    if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
      checkClosed();
      if (paramReader == null) {
        setNull(paramInt, 2011);
      } else {
        ServerPreparedStatement.BindValue bindValue = getBinding(paramInt, true);
        resetToType(bindValue, 252);
        bindValue.value = paramReader;
        bindValue.isLongData = true;
        if (this.connection.getUseStreamLengthsInPrepStmts()) {
          bindValue.bindLength = paramLong;
        } else {
          bindValue.bindLength = -1L;
        } 
      } 
      return;
    } 
    throw SQLError.createSQLException("Can not call setNClob() when connection character set isn't UTF-8", getExceptionInterceptor());
  }
  
  public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
    long l;
    Reader reader = paramNClob.getCharacterStream();
    if (this.connection.getUseStreamLengthsInPrepStmts()) {
      l = paramNClob.length();
    } else {
      l = -1L;
    } 
    setNClob(paramInt, reader, l);
  }
  
  public void setNString(int paramInt, String paramString) throws SQLException {
    if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
      setString(paramInt, paramString);
      return;
    } 
    throw SQLError.createSQLException("Can not call setNString() when connection character set isn't UTF-8", getExceptionInterceptor());
  }
  
  public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
    JDBC4PreparedStatementHelper.setRowId(this, paramInt, paramRowId);
  }
  
  public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
    JDBC4PreparedStatementHelper.setSQLXML(this, paramInt, paramSQLXML);
  }
}
