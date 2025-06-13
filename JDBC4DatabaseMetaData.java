package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

public class JDBC4DatabaseMetaData extends DatabaseMetaData {
  public JDBC4DatabaseMetaData(MySQLConnection paramMySQLConnection, String paramString) {
    super(paramMySQLConnection, paramString);
  }
  
  public static int getProcedureOrFunctionColumnType(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (paramBoolean2 && paramBoolean1)
      return 2; 
    if (paramBoolean2)
      return 1; 
    byte b = 4;
    if (paramBoolean1) {
      if (paramBoolean4)
        b = 3; 
      return b;
    } 
    if (paramBoolean3) {
      if (!paramBoolean4)
        b = 5; 
      return b;
    } 
    return 0;
  }
  
  public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
    return false;
  }
  
  public int getColumnType(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    return getProcedureOrFunctionColumnType(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }
  
  public int getJDBC4FunctionNoTableConstant() {
    return 1;
  }
  
  public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    return getProcedureOrFunctionColumns(createProcedureColumnsFields(), paramString1, paramString2, paramString3, paramString4, true, this.conn.getGetProceduresReturnsFunctions());
  }
  
  public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
    return getProceduresAndOrFunctions(createFieldMetadataForGetProcedures(), paramString1, paramString2, paramString3, true, this.conn.getGetProceduresReturnsFunctions());
  }
  
  public RowIdLifetime getRowIdLifetime() throws SQLException {
    return RowIdLifetime.ROWID_UNSUPPORTED;
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    return paramClass.isInstance(this);
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.conn.getExceptionInterceptor());
    } 
  }
}
