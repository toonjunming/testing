package com.mysql.jdbc;

import java.io.Reader;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

public class JDBC4CallableStatement extends CallableStatement {
  public JDBC4CallableStatement(MySQLConnection paramMySQLConnection, CallableStatement.CallableStatementParamInfo paramCallableStatementParamInfo) throws SQLException {
    super(paramMySQLConnection, paramCallableStatementParamInfo);
  }
  
  public JDBC4CallableStatement(MySQLConnection paramMySQLConnection, String paramString1, String paramString2, boolean paramBoolean) throws SQLException {
    super(paramMySQLConnection, paramString1, paramString2, paramBoolean);
  }
  
  public Reader getCharacterStream(int paramInt) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
    Reader reader = resultSetInternalMethods.getCharacterStream(mapOutputParameterIndexToRsIndex(paramInt));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return reader;
  }
  
  public Reader getCharacterStream(String paramString) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
    Reader reader = resultSetInternalMethods.getCharacterStream(fixParameterName(paramString));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return reader;
  }
  
  public Reader getNCharacterStream(int paramInt) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
    Reader reader = ((JDBC4ResultSet)resultSetInternalMethods).getNCharacterStream(mapOutputParameterIndexToRsIndex(paramInt));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return reader;
  }
  
  public Reader getNCharacterStream(String paramString) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
    Reader reader = ((JDBC4ResultSet)resultSetInternalMethods).getNCharacterStream(fixParameterName(paramString));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return reader;
  }
  
  public NClob getNClob(int paramInt) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
    NClob nClob = ((JDBC4ResultSet)resultSetInternalMethods).getNClob(mapOutputParameterIndexToRsIndex(paramInt));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return nClob;
  }
  
  public NClob getNClob(String paramString) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
    NClob nClob = ((JDBC4ResultSet)resultSetInternalMethods).getNClob(fixParameterName(paramString));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return nClob;
  }
  
  public String getNString(int paramInt) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
    String str = ((JDBC4ResultSet)resultSetInternalMethods).getNString(mapOutputParameterIndexToRsIndex(paramInt));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return str;
  }
  
  public String getNString(String paramString) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
    paramString = ((JDBC4ResultSet)resultSetInternalMethods).getNString(fixParameterName(paramString));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return paramString;
  }
  
  public RowId getRowId(int paramInt) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
    RowId rowId = ((JDBC4ResultSet)resultSetInternalMethods).getRowId(mapOutputParameterIndexToRsIndex(paramInt));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return rowId;
  }
  
  public RowId getRowId(String paramString) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
    RowId rowId = ((JDBC4ResultSet)resultSetInternalMethods).getRowId(fixParameterName(paramString));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return rowId;
  }
  
  public SQLXML getSQLXML(int paramInt) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(paramInt);
    SQLXML sQLXML = ((JDBC4ResultSet)resultSetInternalMethods).getSQLXML(mapOutputParameterIndexToRsIndex(paramInt));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return sQLXML;
  }
  
  public SQLXML getSQLXML(String paramString) throws SQLException {
    ResultSetInternalMethods resultSetInternalMethods = getOutputParameters(0);
    SQLXML sQLXML = ((JDBC4ResultSet)resultSetInternalMethods).getSQLXML(fixParameterName(paramString));
    this.outputParamWasNull = resultSetInternalMethods.wasNull();
    return sQLXML;
  }
  
  public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
    JDBC4PreparedStatementHelper.setNClob(this, paramInt, paramNClob);
  }
  
  public void setNClob(String paramString, Reader paramReader) throws SQLException {
    setNClob(getNamedParamIndex(paramString, false), paramReader);
  }
  
  public void setNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
    setNClob(getNamedParamIndex(paramString, false), paramReader, paramLong);
  }
  
  public void setNClob(String paramString, NClob paramNClob) throws SQLException {
    JDBC4PreparedStatementHelper.setNClob(this, getNamedParamIndex(paramString, false), paramNClob);
  }
  
  public void setNString(String paramString1, String paramString2) throws SQLException {
    setNString(getNamedParamIndex(paramString1, false), paramString2);
  }
  
  public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
    JDBC4PreparedStatementHelper.setRowId(this, paramInt, paramRowId);
  }
  
  public void setRowId(String paramString, RowId paramRowId) throws SQLException {
    JDBC4PreparedStatementHelper.setRowId(this, getNamedParamIndex(paramString, false), paramRowId);
  }
  
  public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
    JDBC4PreparedStatementHelper.setSQLXML(this, paramInt, paramSQLXML);
  }
  
  public void setSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
    JDBC4PreparedStatementHelper.setSQLXML(this, getNamedParamIndex(paramString, false), paramSQLXML);
  }
}
