package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

public class JDBC4DatabaseMetaDataUsingInfoSchema extends DatabaseMetaDataUsingInfoSchema {
  public JDBC4DatabaseMetaDataUsingInfoSchema(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    super(paramMySQLConnection, paramString);
  }
  
  public int getColumnType(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    return JDBC4DatabaseMetaData.getProcedureOrFunctionColumnType(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }
  
  public int getJDBC4FunctionConstant(DatabaseMetaDataUsingInfoSchema.JDBC4FunctionConstant paramJDBC4FunctionConstant) {
    switch (paramJDBC4FunctionConstant) {
      default:
        return -1;
      case null:
        return 2;
      case null:
        return 1;
      case null:
      case null:
        return 0;
      case null:
        return 5;
      case null:
        return 4;
      case null:
        return 3;
      case null:
        return 2;
      case null:
        break;
    } 
    return 1;
  }
  
  public int getJDBC4FunctionNoTableConstant() {
    return 1;
  }
  
  public ResultSet getProcedureColumnsNoISParametersView(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    return getProcedureOrFunctionColumns(createProcedureColumnsFields(), paramString1, paramString2, paramString3, paramString4, true, this.conn.getGetProceduresReturnsFunctions());
  }
  
  public String getRoutineTypeConditionForGetProcedureColumns() {
    String str;
    if (this.conn.getGetProceduresReturnsFunctions()) {
      str = "";
    } else {
      str = "ROUTINE_TYPE = 'PROCEDURE' AND ";
    } 
    return str;
  }
  
  public String getRoutineTypeConditionForGetProcedures() {
    String str;
    if (this.conn.getGetProceduresReturnsFunctions()) {
      str = "";
    } else {
      str = "ROUTINE_TYPE = 'PROCEDURE' AND ";
    } 
    return str;
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
