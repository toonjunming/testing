package com.mysql.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseMetaDataUsingInfoSchema extends DatabaseMetaData {
  private final boolean hasParametersView;
  
  private boolean hasReferentialConstraintsView;
  
  public DatabaseMetaDataUsingInfoSchema(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    super(paramMySQLConnection, paramString);
    ResultSet resultSet;
    this.hasReferentialConstraintsView = this.conn.versionMeetsMinimum(5, 1, 10);
    paramMySQLConnection = null;
    try {
      ResultSet resultSet1 = super.getTables("INFORMATION_SCHEMA", null, "PARAMETERS", new String[0]);
      resultSet = resultSet1;
      this.hasParametersView = resultSet1.next();
      return;
    } finally {
      if (resultSet != null)
        resultSet.close(); 
    } 
  }
  
  private String generateDeleteRuleClause() {
    String str;
    if (this.hasReferentialConstraintsView) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("CASE WHEN R.DELETE_RULE='CASCADE' THEN ");
      stringBuilder.append(String.valueOf(0));
      stringBuilder.append(" WHEN R.DELETE_RULE='SET NULL' THEN ");
      stringBuilder.append(String.valueOf(2));
      stringBuilder.append(" WHEN R.DELETE_RULE='SET DEFAULT' THEN ");
      stringBuilder.append(String.valueOf(4));
      stringBuilder.append(" WHEN R.DELETE_RULE='RESTRICT' THEN ");
      stringBuilder.append(String.valueOf(1));
      stringBuilder.append(" WHEN R.DELETE_RULE='NO ACTION' THEN ");
      stringBuilder.append(String.valueOf(3));
      stringBuilder.append(" ELSE ");
      stringBuilder.append(String.valueOf(3));
      stringBuilder.append(" END ");
      str = stringBuilder.toString();
    } else {
      str = String.valueOf(1);
    } 
    return str;
  }
  
  private String generateOptionalRefContraintsJoin() {
    String str;
    if (this.hasReferentialConstraintsView) {
      str = "JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R ON (R.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND R.TABLE_NAME = B.TABLE_NAME AND R.CONSTRAINT_SCHEMA = B.TABLE_SCHEMA) ";
    } else {
      str = "";
    } 
    return str;
  }
  
  private String generateUpdateRuleClause() {
    String str;
    if (this.hasReferentialConstraintsView) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("CASE WHEN R.UPDATE_RULE='CASCADE' THEN ");
      stringBuilder.append(String.valueOf(0));
      stringBuilder.append(" WHEN R.UPDATE_RULE='SET NULL' THEN ");
      stringBuilder.append(String.valueOf(2));
      stringBuilder.append(" WHEN R.UPDATE_RULE='SET DEFAULT' THEN ");
      stringBuilder.append(String.valueOf(4));
      stringBuilder.append(" WHEN R.UPDATE_RULE='RESTRICT' THEN ");
      stringBuilder.append(String.valueOf(1));
      stringBuilder.append(" WHEN R.UPDATE_RULE='NO ACTION' THEN ");
      stringBuilder.append(String.valueOf(3));
      stringBuilder.append(" ELSE ");
      stringBuilder.append(String.valueOf(3));
      stringBuilder.append(" END ");
      str = stringBuilder.toString();
    } else {
      str = String.valueOf(1);
    } 
    return str;
  }
  
  public ResultSet executeMetadataQuery(PreparedStatement paramPreparedStatement) throws SQLException {
    ResultSet resultSet = paramPreparedStatement.executeQuery();
    ((ResultSetInternalMethods)resultSet).setOwningStatement(null);
    return resultSet;
  }
  
  public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    PreparedStatement preparedStatement;
    paramString2 = paramString4;
    if (paramString4 == null)
      if (this.conn.getNullNamePatternMatchesAll()) {
        paramString2 = "%";
      } else {
        throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }  
    paramString4 = paramString1;
    if (paramString1 == null) {
      paramString4 = paramString1;
      if (this.conn.getNullCatalogMeansCurrent())
        paramString4 = this.database; 
    } 
    paramString1 = null;
    try {
      PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME,COLUMN_NAME, NULL AS GRANTOR, GRANTEE, PRIVILEGE_TYPE AS PRIVILEGE, IS_GRANTABLE FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME =? AND COLUMN_NAME LIKE ? ORDER BY COLUMN_NAME, PRIVILEGE_TYPE");
      if (paramString4 != null) {
        PreparedStatement preparedStatement2 = preparedStatement1;
        preparedStatement1.setString(1, paramString4);
      } else {
        PreparedStatement preparedStatement2 = preparedStatement1;
        preparedStatement1.setString(1, "%");
      } 
      preparedStatement = preparedStatement1;
      preparedStatement1.setString(2, paramString3);
      preparedStatement = preparedStatement1;
      preparedStatement1.setString(3, paramString2);
      preparedStatement = preparedStatement1;
      ResultSet resultSet = executeMetadataQuery(preparedStatement1);
      preparedStatement = preparedStatement1;
      ResultSetInternalMethods resultSetInternalMethods = (ResultSetInternalMethods)resultSet;
      preparedStatement = preparedStatement1;
      Field field6 = new Field();
      preparedStatement = preparedStatement1;
      this("", "TABLE_CAT", 1, 64);
      preparedStatement = preparedStatement1;
      Field field4 = new Field();
      preparedStatement = preparedStatement1;
      this("", "TABLE_SCHEM", 1, 1);
      preparedStatement = preparedStatement1;
      Field field1 = new Field();
      preparedStatement = preparedStatement1;
      this("", "TABLE_NAME", 1, 64);
      preparedStatement = preparedStatement1;
      Field field3 = new Field();
      preparedStatement = preparedStatement1;
      this("", "COLUMN_NAME", 1, 64);
      preparedStatement = preparedStatement1;
      Field field5 = new Field();
      preparedStatement = preparedStatement1;
      this("", "GRANTOR", 1, 77);
      preparedStatement = preparedStatement1;
      Field field7 = new Field();
      preparedStatement = preparedStatement1;
      this("", "GRANTEE", 1, 77);
      preparedStatement = preparedStatement1;
      Field field8 = new Field();
      preparedStatement = preparedStatement1;
      this("", "PRIVILEGE", 1, 64);
      preparedStatement = preparedStatement1;
      Field field2 = new Field();
      preparedStatement = preparedStatement1;
      this("", "IS_GRANTABLE", 1, 3);
      preparedStatement = preparedStatement1;
      resultSetInternalMethods.redefineFieldsForDBMD(new Field[] { field6, field4, field1, field3, field5, field7, field8, field2 });
      return resultSet;
    } finally {
      if (preparedStatement != null)
        preparedStatement.close(); 
    } 
  }
  
  public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    PreparedStatement preparedStatement;
    paramString2 = paramString4;
    if (paramString4 == null)
      if (this.conn.getNullNamePatternMatchesAll()) {
        paramString2 = "%";
      } else {
        throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }  
    paramString4 = paramString1;
    if (paramString1 == null) {
      paramString4 = paramString1;
      if (this.conn.getNullCatalogMeansCurrent())
        paramString4 = this.database; 
    } 
    StringBuilder stringBuilder2 = new StringBuilder("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME,");
    MysqlDefs.appendJdbcTypeMappingQuery(stringBuilder2, "DATA_TYPE");
    stringBuilder2.append(" AS DATA_TYPE, ");
    if (this.conn.getCapitalizeTypeNames()) {
      stringBuilder2.append("UPPER(CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 AND LOCATE('set', DATA_TYPE) <> 1 AND LOCATE('enum', DATA_TYPE) <> 1 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS TYPE_NAME,");
    } else {
      stringBuilder2.append("CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 AND LOCATE('set', DATA_TYPE) <> 1 AND LOCATE('enum', DATA_TYPE) <> 1 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS TYPE_NAME,");
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS COLUMN_SIZE, ");
    stringBuilder1.append(MysqlIO.getMaxBuf());
    stringBuilder1.append(" AS BUFFER_LENGTH,");
    stringBuilder1.append("NUMERIC_SCALE AS DECIMAL_DIGITS,");
    stringBuilder1.append("10 AS NUM_PREC_RADIX,");
    stringBuilder1.append("CASE WHEN IS_NULLABLE='NO' THEN ");
    stringBuilder1.append(0);
    stringBuilder1.append(" ELSE CASE WHEN IS_NULLABLE='YES' THEN ");
    stringBuilder1.append(1);
    stringBuilder1.append(" ELSE ");
    stringBuilder1.append(2);
    stringBuilder1.append(" END END AS NULLABLE,");
    stringBuilder1.append("COLUMN_COMMENT AS REMARKS,");
    stringBuilder1.append("COLUMN_DEFAULT AS COLUMN_DEF,");
    stringBuilder1.append("0 AS SQL_DATA_TYPE,");
    stringBuilder1.append("0 AS SQL_DATETIME_SUB,");
    stringBuilder1.append("CASE WHEN CHARACTER_OCTET_LENGTH > ");
    stringBuilder1.append(2147483647);
    stringBuilder1.append(" THEN ");
    stringBuilder1.append(2147483647);
    stringBuilder1.append(" ELSE CHARACTER_OCTET_LENGTH END AS CHAR_OCTET_LENGTH,");
    stringBuilder1.append("ORDINAL_POSITION,");
    stringBuilder1.append("IS_NULLABLE,");
    stringBuilder1.append("NULL AS SCOPE_CATALOG,");
    stringBuilder1.append("NULL AS SCOPE_SCHEMA,");
    stringBuilder1.append("NULL AS SCOPE_TABLE,");
    stringBuilder1.append("NULL AS SOURCE_DATA_TYPE,");
    stringBuilder1.append("IF (EXTRA LIKE '%auto_increment%','YES','NO') AS IS_AUTOINCREMENT, ");
    stringBuilder1.append("IF (EXTRA LIKE '%GENERATED%','YES','NO') AS IS_GENERATEDCOLUMN FROM INFORMATION_SCHEMA.COLUMNS WHERE ");
    stringBuilder2.append(stringBuilder1.toString());
    boolean bool = "information_schema".equalsIgnoreCase(paramString4);
    if (paramString4 != null) {
      if (bool || (StringUtils.indexOfIgnoreCase(0, paramString4, "%") == -1 && StringUtils.indexOfIgnoreCase(0, paramString4, "_") == -1)) {
        stringBuilder2.append("TABLE_SCHEMA = ? AND ");
      } else {
        stringBuilder2.append("TABLE_SCHEMA LIKE ? AND ");
      } 
    } else {
      stringBuilder2.append("TABLE_SCHEMA LIKE ? AND ");
    } 
    if (paramString3 != null) {
      if (StringUtils.indexOfIgnoreCase(0, paramString3, "%") == -1 && StringUtils.indexOfIgnoreCase(0, paramString3, "_") == -1) {
        stringBuilder2.append("TABLE_NAME = ? AND ");
      } else {
        stringBuilder2.append("TABLE_NAME LIKE ? AND ");
      } 
    } else {
      stringBuilder2.append("TABLE_NAME LIKE ? AND ");
    } 
    if (StringUtils.indexOfIgnoreCase(0, paramString2, "%") == -1 && StringUtils.indexOfIgnoreCase(0, paramString2, "_") == -1) {
      stringBuilder2.append("COLUMN_NAME = ? ");
    } else {
      stringBuilder2.append("COLUMN_NAME LIKE ? ");
    } 
    stringBuilder2.append("ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION");
    stringBuilder1 = null;
    try {
      PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement(stringBuilder2.toString());
      if (paramString4 != null) {
        PreparedStatement preparedStatement2 = preparedStatement1;
        preparedStatement1.setString(1, paramString4);
      } else {
        PreparedStatement preparedStatement2 = preparedStatement1;
        preparedStatement1.setString(1, "%");
      } 
      preparedStatement = preparedStatement1;
      preparedStatement1.setString(2, paramString3);
      preparedStatement = preparedStatement1;
      preparedStatement1.setString(3, paramString2);
      preparedStatement = preparedStatement1;
      ResultSet resultSet = executeMetadataQuery(preparedStatement1);
      preparedStatement = preparedStatement1;
      ((ResultSetInternalMethods)resultSet).redefineFieldsForDBMD(createColumnsFields());
      return resultSet;
    } finally {
      if (preparedStatement != null)
        preparedStatement.close(); 
    } 
  }
  
  public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws SQLException {
    if (paramString3 != null) {
      PreparedStatement preparedStatement;
      paramString2 = paramString1;
      if (paramString1 == null) {
        paramString2 = paramString1;
        if (this.conn.getNullCatalogMeansCurrent())
          paramString2 = this.database; 
      } 
      paramString1 = paramString4;
      if (paramString4 == null) {
        paramString1 = paramString4;
        if (this.conn.getNullCatalogMeansCurrent())
          paramString1 = this.database; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM, A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, A.TABLE_SCHEMA AS FKTABLE_CAT, NULL AS FKTABLE_SCHEM, A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ,");
      stringBuilder.append(generateUpdateRuleClause());
      stringBuilder.append(" AS UPDATE_RULE,");
      stringBuilder.append(generateDeleteRuleClause());
      stringBuilder.append(" AS DELETE_RULE,");
      stringBuilder.append("A.CONSTRAINT_NAME AS FK_NAME,");
      stringBuilder.append("(SELECT CONSTRAINT_NAME FROM");
      stringBuilder.append(" INFORMATION_SCHEMA.TABLE_CONSTRAINTS");
      stringBuilder.append(" WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND");
      stringBuilder.append(" TABLE_NAME = A.REFERENCED_TABLE_NAME AND");
      stringBuilder.append(" CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)");
      stringBuilder.append(" AS PK_NAME,");
      stringBuilder.append(7);
      stringBuilder.append(" AS DEFERRABILITY ");
      stringBuilder.append("FROM ");
      stringBuilder.append("INFORMATION_SCHEMA.KEY_COLUMN_USAGE A JOIN ");
      stringBuilder.append("INFORMATION_SCHEMA.TABLE_CONSTRAINTS B ");
      stringBuilder.append("USING (TABLE_SCHEMA, TABLE_NAME, CONSTRAINT_NAME) ");
      stringBuilder.append(generateOptionalRefContraintsJoin());
      stringBuilder.append("WHERE ");
      stringBuilder.append("B.CONSTRAINT_TYPE = 'FOREIGN KEY' ");
      stringBuilder.append("AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? ");
      stringBuilder.append("AND A.TABLE_SCHEMA LIKE ? AND A.TABLE_NAME=? ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION");
      paramString5 = stringBuilder.toString();
      stringBuilder = null;
      try {
        PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement(paramString5);
        if (paramString2 != null) {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, paramString2);
        } else {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, "%");
        } 
        preparedStatement = preparedStatement1;
        preparedStatement1.setString(2, paramString3);
        if (paramString1 != null) {
          preparedStatement = preparedStatement1;
          preparedStatement1.setString(3, paramString1);
        } else {
          preparedStatement = preparedStatement1;
          preparedStatement1.setString(3, "%");
        } 
        preparedStatement = preparedStatement1;
        preparedStatement1.setString(4, paramString6);
        preparedStatement = preparedStatement1;
        ResultSet resultSet = executeMetadataQuery(preparedStatement1);
        preparedStatement = preparedStatement1;
        ((ResultSetInternalMethods)resultSet).redefineFieldsForDBMD(createFkMetadataFields());
        return resultSet;
      } finally {
        if (preparedStatement != null)
          preparedStatement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
    if (paramString3 != null) {
      PreparedStatement preparedStatement;
      paramString2 = paramString1;
      if (paramString1 == null) {
        paramString2 = paramString1;
        if (this.conn.getNullCatalogMeansCurrent())
          paramString2 = this.database; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT, NULL AS PKTABLE_SCHEM, A.REFERENCED_TABLE_NAME AS PKTABLE_NAME, A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, A.TABLE_SCHEMA AS FKTABLE_CAT, NULL AS FKTABLE_SCHEM, A.TABLE_NAME AS FKTABLE_NAME,A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ,");
      stringBuilder.append(generateUpdateRuleClause());
      stringBuilder.append(" AS UPDATE_RULE,");
      stringBuilder.append(generateDeleteRuleClause());
      stringBuilder.append(" AS DELETE_RULE,");
      stringBuilder.append("A.CONSTRAINT_NAME AS FK_NAME,");
      stringBuilder.append("(SELECT CONSTRAINT_NAME FROM");
      stringBuilder.append(" INFORMATION_SCHEMA.TABLE_CONSTRAINTS");
      stringBuilder.append(" WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND");
      stringBuilder.append(" TABLE_NAME = A.REFERENCED_TABLE_NAME AND");
      stringBuilder.append(" CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)");
      stringBuilder.append(" AS PK_NAME,");
      stringBuilder.append(7);
      stringBuilder.append(" AS DEFERRABILITY ");
      stringBuilder.append("FROM ");
      stringBuilder.append("INFORMATION_SCHEMA.KEY_COLUMN_USAGE A JOIN ");
      stringBuilder.append("INFORMATION_SCHEMA.TABLE_CONSTRAINTS B ");
      stringBuilder.append("USING (TABLE_SCHEMA, TABLE_NAME, CONSTRAINT_NAME) ");
      stringBuilder.append(generateOptionalRefContraintsJoin());
      stringBuilder.append("WHERE ");
      stringBuilder.append("B.CONSTRAINT_TYPE = 'FOREIGN KEY' ");
      stringBuilder.append("AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? ");
      stringBuilder.append("ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION");
      String str = stringBuilder.toString();
      stringBuilder = null;
      try {
        PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement(str);
        if (paramString2 != null) {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, paramString2);
        } else {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, "%");
        } 
        preparedStatement = preparedStatement1;
        preparedStatement1.setString(2, paramString3);
        preparedStatement = preparedStatement1;
        ResultSet resultSet = executeMetadataQuery(preparedStatement1);
        preparedStatement = preparedStatement1;
        ((ResultSetInternalMethods)resultSet).redefineFieldsForDBMD(createFkMetadataFields());
        return resultSet;
      } finally {
        if (preparedStatement != null)
          preparedStatement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield hasParametersView : Z
    //   4: ifne -> 17
    //   7: aload_0
    //   8: aload_1
    //   9: aload_2
    //   10: aload_3
    //   11: aload #4
    //   13: invokespecial getFunctionColumns : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
    //   16: areturn
    //   17: aload_3
    //   18: ifnull -> 31
    //   21: aload_3
    //   22: astore #5
    //   24: aload_3
    //   25: invokevirtual length : ()I
    //   28: ifne -> 47
    //   31: aload_0
    //   32: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   35: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   40: ifeq -> 472
    //   43: ldc '%'
    //   45: astore #5
    //   47: aconst_null
    //   48: astore_3
    //   49: aload_1
    //   50: astore_2
    //   51: aload_1
    //   52: ifnonnull -> 77
    //   55: aload_0
    //   56: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   59: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   64: ifeq -> 75
    //   67: aload_0
    //   68: getfield database : Ljava/lang/String;
    //   71: astore_2
    //   72: goto -> 77
    //   75: aconst_null
    //   76: astore_2
    //   77: new java/lang/StringBuilder
    //   80: dup
    //   81: ldc_w 'SELECT SPECIFIC_SCHEMA AS FUNCTION_CAT, NULL AS `FUNCTION_SCHEM`, SPECIFIC_NAME AS `FUNCTION_NAME`, '
    //   84: invokespecial <init> : (Ljava/lang/String;)V
    //   87: astore #6
    //   89: aload #6
    //   91: ldc_w 'IFNULL(PARAMETER_NAME, '') AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN '
    //   94: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: aload #6
    //   100: aload_0
    //   101: getstatic com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.FUNCTION_COLUMN_IN : Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;
    //   104: invokevirtual getJDBC4FunctionConstant : (Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;)I
    //   107: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload #6
    //   113: ldc_w ' WHEN PARAMETER_MODE = 'OUT' THEN '
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: aload #6
    //   122: aload_0
    //   123: getstatic com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.FUNCTION_COLUMN_OUT : Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;
    //   126: invokevirtual getJDBC4FunctionConstant : (Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;)I
    //   129: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   132: pop
    //   133: aload #6
    //   135: ldc_w ' WHEN PARAMETER_MODE = 'INOUT' THEN '
    //   138: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload #6
    //   144: aload_0
    //   145: getstatic com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.FUNCTION_COLUMN_INOUT : Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;
    //   148: invokevirtual getJDBC4FunctionConstant : (Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;)I
    //   151: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   154: pop
    //   155: aload #6
    //   157: ldc_w ' WHEN ORDINAL_POSITION = 0 THEN '
    //   160: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload #6
    //   166: aload_0
    //   167: getstatic com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.FUNCTION_COLUMN_RETURN : Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;
    //   170: invokevirtual getJDBC4FunctionConstant : (Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;)I
    //   173: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   176: pop
    //   177: aload #6
    //   179: ldc ' ELSE '
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload #6
    //   187: aload_0
    //   188: getstatic com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.FUNCTION_COLUMN_UNKNOWN : Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;
    //   191: invokevirtual getJDBC4FunctionConstant : (Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;)I
    //   194: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   197: pop
    //   198: aload #6
    //   200: ldc_w ' END AS `COLUMN_TYPE`, '
    //   203: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: pop
    //   207: aload #6
    //   209: ldc 'DATA_TYPE'
    //   211: invokestatic appendJdbcTypeMappingQuery : (Ljava/lang/StringBuilder;Ljava/lang/String;)V
    //   214: aload #6
    //   216: ldc_w ' AS `DATA_TYPE`, '
    //   219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: aload_0
    //   224: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   227: invokeinterface getCapitalizeTypeNames : ()Z
    //   232: ifeq -> 247
    //   235: aload #6
    //   237: ldc_w 'UPPER(CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS `TYPE_NAME`,'
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: pop
    //   244: goto -> 256
    //   247: aload #6
    //   249: ldc_w 'CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS `TYPE_NAME`,'
    //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: aload #6
    //   258: ldc_w 'NUMERIC_PRECISION AS `PRECISION`, '
    //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: pop
    //   265: aload #6
    //   267: ldc_w 'CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH, '
    //   270: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: pop
    //   274: aload #6
    //   276: ldc_w 'NUMERIC_SCALE AS `SCALE`, '
    //   279: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: pop
    //   283: aload #6
    //   285: ldc_w '10 AS RADIX,'
    //   288: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   291: pop
    //   292: new java/lang/StringBuilder
    //   295: dup
    //   296: invokespecial <init> : ()V
    //   299: astore_1
    //   300: aload_1
    //   301: aload_0
    //   302: getstatic com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.FUNCTION_NULLABLE : Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;
    //   305: invokevirtual getJDBC4FunctionConstant : (Lcom/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant;)I
    //   308: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   311: pop
    //   312: aload_1
    //   313: ldc_w ' AS `NULLABLE`,  NULL AS `REMARKS`, '
    //   316: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: pop
    //   320: aload_1
    //   321: ldc_w 'CHARACTER_OCTET_LENGTH AS `CHAR_OCTET_LENGTH`,  ORDINAL_POSITION, 'YES' AS `IS_NULLABLE`, SPECIFIC_NAME '
    //   324: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   327: pop
    //   328: aload_1
    //   329: ldc_w 'FROM INFORMATION_SCHEMA.PARAMETERS WHERE '
    //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: pop
    //   336: aload_1
    //   337: ldc_w 'SPECIFIC_SCHEMA LIKE ? AND SPECIFIC_NAME LIKE ? AND (PARAMETER_NAME LIKE ? OR PARAMETER_NAME IS NULL) '
    //   340: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   343: pop
    //   344: aload_1
    //   345: ldc_w 'AND ROUTINE_TYPE='FUNCTION' ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ORDINAL_POSITION'
    //   348: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   351: pop
    //   352: aload #6
    //   354: aload_1
    //   355: invokevirtual toString : ()Ljava/lang/String;
    //   358: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: pop
    //   362: aload_3
    //   363: astore_1
    //   364: aload_0
    //   365: aload #6
    //   367: invokevirtual toString : ()Ljava/lang/String;
    //   370: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   373: astore_3
    //   374: aload_2
    //   375: ifnull -> 391
    //   378: aload_3
    //   379: astore_1
    //   380: aload_3
    //   381: iconst_1
    //   382: aload_2
    //   383: invokeinterface setString : (ILjava/lang/String;)V
    //   388: goto -> 402
    //   391: aload_3
    //   392: astore_1
    //   393: aload_3
    //   394: iconst_1
    //   395: ldc '%'
    //   397: invokeinterface setString : (ILjava/lang/String;)V
    //   402: aload_3
    //   403: astore_1
    //   404: aload_3
    //   405: iconst_2
    //   406: aload #5
    //   408: invokeinterface setString : (ILjava/lang/String;)V
    //   413: aload_3
    //   414: astore_1
    //   415: aload_3
    //   416: iconst_3
    //   417: aload #4
    //   419: invokeinterface setString : (ILjava/lang/String;)V
    //   424: aload_3
    //   425: astore_1
    //   426: aload_0
    //   427: aload_3
    //   428: invokevirtual executeMetadataQuery : (Ljava/sql/PreparedStatement;)Ljava/sql/ResultSet;
    //   431: astore_2
    //   432: aload_3
    //   433: astore_1
    //   434: aload_2
    //   435: checkcast com/mysql/jdbc/ResultSetInternalMethods
    //   438: aload_0
    //   439: invokevirtual createFunctionColumnsFields : ()[Lcom/mysql/jdbc/Field;
    //   442: invokeinterface redefineFieldsForDBMD : ([Lcom/mysql/jdbc/Field;)V
    //   447: aload_3
    //   448: ifnull -> 457
    //   451: aload_3
    //   452: invokeinterface close : ()V
    //   457: aload_2
    //   458: areturn
    //   459: astore_2
    //   460: aload_1
    //   461: ifnull -> 470
    //   464: aload_1
    //   465: invokeinterface close : ()V
    //   470: aload_2
    //   471: athrow
    //   472: ldc_w 'Procedure name pattern can not be NULL or empty.'
    //   475: ldc 'S1009'
    //   477: aload_0
    //   478: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   481: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   484: athrow
    // Exception table:
    //   from	to	target	type
    //   364	374	459	finally
    //   380	388	459	finally
    //   393	402	459	finally
    //   404	413	459	finally
    //   415	424	459	finally
    //   426	432	459	finally
    //   434	447	459	finally
  }
  
  public ResultSet getFunctions(String paramString1, String paramString2, String paramString3) throws SQLException {
    // Byte code:
    //   0: ldc '%'
    //   2: astore #6
    //   4: aload_3
    //   5: ifnull -> 18
    //   8: aload_3
    //   9: astore #4
    //   11: aload_3
    //   12: invokevirtual length : ()I
    //   15: ifne -> 34
    //   18: aload_0
    //   19: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   22: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   27: ifeq -> 400
    //   30: ldc '%'
    //   32: astore #4
    //   34: aconst_null
    //   35: astore_3
    //   36: aload_1
    //   37: astore_2
    //   38: aload_1
    //   39: ifnonnull -> 64
    //   42: aload_0
    //   43: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   46: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   51: ifeq -> 62
    //   54: aload_0
    //   55: getfield database : Ljava/lang/String;
    //   58: astore_2
    //   59: goto -> 64
    //   62: aconst_null
    //   63: astore_2
    //   64: new java/lang/StringBuilder
    //   67: dup
    //   68: invokespecial <init> : ()V
    //   71: astore_1
    //   72: aload_1
    //   73: ldc_w 'SELECT ROUTINE_SCHEMA AS FUNCTION_CAT, NULL AS FUNCTION_SCHEM, ROUTINE_NAME AS FUNCTION_NAME, ROUTINE_COMMENT AS REMARKS, '
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_1
    //   81: aload_0
    //   82: invokevirtual getJDBC4FunctionNoTableConstant : ()I
    //   85: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   88: pop
    //   89: aload_1
    //   90: ldc_w ' AS FUNCTION_TYPE, ROUTINE_NAME AS SPECIFIC_NAME FROM INFORMATION_SCHEMA.ROUTINES '
    //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload_1
    //   98: ldc_w 'WHERE ROUTINE_TYPE LIKE 'FUNCTION' AND ROUTINE_SCHEMA LIKE ? AND '
    //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload_1
    //   106: ldc_w 'ROUTINE_NAME LIKE ? ORDER BY FUNCTION_CAT, FUNCTION_SCHEM, FUNCTION_NAME, SPECIFIC_NAME'
    //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload_1
    //   114: invokevirtual toString : ()Ljava/lang/String;
    //   117: astore #5
    //   119: aload_3
    //   120: astore_1
    //   121: aload_0
    //   122: aload #5
    //   124: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   127: astore #5
    //   129: aload #6
    //   131: astore_3
    //   132: aload_2
    //   133: ifnull -> 138
    //   136: aload_2
    //   137: astore_3
    //   138: aload #5
    //   140: astore_1
    //   141: aload #5
    //   143: iconst_1
    //   144: aload_3
    //   145: invokeinterface setString : (ILjava/lang/String;)V
    //   150: aload #5
    //   152: astore_1
    //   153: aload #5
    //   155: iconst_2
    //   156: aload #4
    //   158: invokeinterface setString : (ILjava/lang/String;)V
    //   163: aload #5
    //   165: astore_1
    //   166: aload_0
    //   167: aload #5
    //   169: invokevirtual executeMetadataQuery : (Ljava/sql/PreparedStatement;)Ljava/sql/ResultSet;
    //   172: astore_3
    //   173: aload #5
    //   175: astore_1
    //   176: aload_3
    //   177: checkcast com/mysql/jdbc/ResultSetInternalMethods
    //   180: astore #8
    //   182: aload #5
    //   184: astore_1
    //   185: new com/mysql/jdbc/Field
    //   188: astore #9
    //   190: aload #5
    //   192: astore_1
    //   193: aload #9
    //   195: ldc ''
    //   197: ldc_w 'FUNCTION_CAT'
    //   200: iconst_1
    //   201: sipush #255
    //   204: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   207: aload #5
    //   209: astore_1
    //   210: new com/mysql/jdbc/Field
    //   213: astore #6
    //   215: aload #5
    //   217: astore_1
    //   218: aload #6
    //   220: ldc ''
    //   222: ldc_w 'FUNCTION_SCHEM'
    //   225: iconst_1
    //   226: sipush #255
    //   229: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   232: aload #5
    //   234: astore_1
    //   235: new com/mysql/jdbc/Field
    //   238: astore #10
    //   240: aload #5
    //   242: astore_1
    //   243: aload #10
    //   245: ldc ''
    //   247: ldc_w 'FUNCTION_NAME'
    //   250: iconst_1
    //   251: sipush #255
    //   254: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   257: aload #5
    //   259: astore_1
    //   260: new com/mysql/jdbc/Field
    //   263: astore #4
    //   265: aload #5
    //   267: astore_1
    //   268: aload #4
    //   270: ldc ''
    //   272: ldc_w 'REMARKS'
    //   275: iconst_1
    //   276: sipush #255
    //   279: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   282: aload #5
    //   284: astore_1
    //   285: new com/mysql/jdbc/Field
    //   288: astore_2
    //   289: aload #5
    //   291: astore_1
    //   292: aload_2
    //   293: ldc ''
    //   295: ldc_w 'FUNCTION_TYPE'
    //   298: iconst_5
    //   299: bipush #6
    //   301: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   304: aload #5
    //   306: astore_1
    //   307: new com/mysql/jdbc/Field
    //   310: astore #7
    //   312: aload #5
    //   314: astore_1
    //   315: aload #7
    //   317: ldc ''
    //   319: ldc_w 'SPECIFIC_NAME'
    //   322: iconst_1
    //   323: sipush #255
    //   326: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
    //   329: aload #5
    //   331: astore_1
    //   332: aload #8
    //   334: bipush #6
    //   336: anewarray com/mysql/jdbc/Field
    //   339: dup
    //   340: iconst_0
    //   341: aload #9
    //   343: aastore
    //   344: dup
    //   345: iconst_1
    //   346: aload #6
    //   348: aastore
    //   349: dup
    //   350: iconst_2
    //   351: aload #10
    //   353: aastore
    //   354: dup
    //   355: iconst_3
    //   356: aload #4
    //   358: aastore
    //   359: dup
    //   360: iconst_4
    //   361: aload_2
    //   362: aastore
    //   363: dup
    //   364: iconst_5
    //   365: aload #7
    //   367: aastore
    //   368: invokeinterface redefineFieldsForDBMD : ([Lcom/mysql/jdbc/Field;)V
    //   373: aload #5
    //   375: ifnull -> 385
    //   378: aload #5
    //   380: invokeinterface close : ()V
    //   385: aload_3
    //   386: areturn
    //   387: astore_2
    //   388: aload_1
    //   389: ifnull -> 398
    //   392: aload_1
    //   393: invokeinterface close : ()V
    //   398: aload_2
    //   399: athrow
    //   400: ldc_w 'Function name pattern can not be NULL or empty.'
    //   403: ldc 'S1009'
    //   405: aload_0
    //   406: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   409: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   412: athrow
    // Exception table:
    //   from	to	target	type
    //   121	129	387	finally
    //   141	150	387	finally
    //   153	163	387	finally
    //   166	173	387	finally
    //   176	182	387	finally
    //   185	190	387	finally
    //   193	207	387	finally
    //   210	215	387	finally
    //   218	232	387	finally
    //   235	240	387	finally
    //   243	257	387	finally
    //   260	265	387	finally
    //   268	282	387	finally
    //   285	289	387	finally
    //   292	304	387	finally
    //   307	312	387	finally
    //   315	329	387	finally
    //   332	373	387	finally
  }
  
  public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
    if (paramString3 != null) {
      PreparedStatement preparedStatement;
      paramString2 = paramString1;
      if (paramString1 == null) {
        paramString2 = paramString1;
        if (this.conn.getNullCatalogMeansCurrent())
          paramString2 = this.database; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT, NULL AS PKTABLE_SCHEM, A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, A.TABLE_SCHEMA AS FKTABLE_CAT, NULL AS FKTABLE_SCHEM, A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ,");
      stringBuilder.append(generateUpdateRuleClause());
      stringBuilder.append(" AS UPDATE_RULE,");
      stringBuilder.append(generateDeleteRuleClause());
      stringBuilder.append(" AS DELETE_RULE,");
      stringBuilder.append("A.CONSTRAINT_NAME AS FK_NAME,");
      stringBuilder.append("(SELECT CONSTRAINT_NAME FROM");
      stringBuilder.append(" INFORMATION_SCHEMA.TABLE_CONSTRAINTS");
      stringBuilder.append(" WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND");
      stringBuilder.append(" TABLE_NAME = A.REFERENCED_TABLE_NAME AND");
      stringBuilder.append(" CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)");
      stringBuilder.append(" AS PK_NAME,");
      stringBuilder.append(7);
      stringBuilder.append(" AS DEFERRABILITY ");
      stringBuilder.append("FROM ");
      stringBuilder.append("INFORMATION_SCHEMA.KEY_COLUMN_USAGE A ");
      stringBuilder.append("JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS B USING ");
      stringBuilder.append("(CONSTRAINT_NAME, TABLE_NAME) ");
      stringBuilder.append(generateOptionalRefContraintsJoin());
      stringBuilder.append("WHERE ");
      stringBuilder.append("B.CONSTRAINT_TYPE = 'FOREIGN KEY' ");
      stringBuilder.append("AND A.TABLE_SCHEMA LIKE ? ");
      stringBuilder.append("AND A.TABLE_NAME=? ");
      stringBuilder.append("AND A.REFERENCED_TABLE_SCHEMA IS NOT NULL ");
      stringBuilder.append("ORDER BY A.REFERENCED_TABLE_SCHEMA, A.REFERENCED_TABLE_NAME, A.ORDINAL_POSITION");
      String str = stringBuilder.toString();
      stringBuilder = null;
      try {
        PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement(str);
        if (paramString2 != null) {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, paramString2);
        } else {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, "%");
        } 
        preparedStatement = preparedStatement1;
        preparedStatement1.setString(2, paramString3);
        preparedStatement = preparedStatement1;
        ResultSet resultSet = executeMetadataQuery(preparedStatement1);
        preparedStatement = preparedStatement1;
        ((ResultSetInternalMethods)resultSet).redefineFieldsForDBMD(createFkMetadataFields());
        return resultSet;
      } finally {
        if (preparedStatement != null)
          preparedStatement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: ldc_w 'SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, NON_UNIQUE,'
    //   7: invokespecial <init> : (Ljava/lang/String;)V
    //   10: astore #7
    //   12: aload #7
    //   14: ldc_w 'TABLE_SCHEMA AS INDEX_QUALIFIER, INDEX_NAME,3 AS TYPE, SEQ_IN_INDEX AS ORDINAL_POSITION, COLUMN_NAME,'
    //   17: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   20: pop
    //   21: aload #7
    //   23: ldc_w 'COLLATION AS ASC_OR_DESC, CARDINALITY, NULL AS PAGES, NULL AS FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS WHERE '
    //   26: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: pop
    //   30: aload #7
    //   32: ldc_w 'TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?'
    //   35: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: iload #4
    //   41: ifeq -> 53
    //   44: aload #7
    //   46: ldc_w ' AND NON_UNIQUE=0 '
    //   49: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: pop
    //   53: aload #7
    //   55: ldc_w 'ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX'
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: aconst_null
    //   63: astore #6
    //   65: aload_1
    //   66: astore_2
    //   67: aload_1
    //   68: ifnonnull -> 96
    //   71: aload_1
    //   72: astore_2
    //   73: aload #6
    //   75: astore_1
    //   76: aload_0
    //   77: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   80: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   85: ifeq -> 96
    //   88: aload #6
    //   90: astore_1
    //   91: aload_0
    //   92: getfield database : Ljava/lang/String;
    //   95: astore_2
    //   96: aload #6
    //   98: astore_1
    //   99: aload_0
    //   100: aload #7
    //   102: invokevirtual toString : ()Ljava/lang/String;
    //   105: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   108: astore #6
    //   110: aload_2
    //   111: ifnull -> 129
    //   114: aload #6
    //   116: astore_1
    //   117: aload #6
    //   119: iconst_1
    //   120: aload_2
    //   121: invokeinterface setString : (ILjava/lang/String;)V
    //   126: goto -> 142
    //   129: aload #6
    //   131: astore_1
    //   132: aload #6
    //   134: iconst_1
    //   135: ldc '%'
    //   137: invokeinterface setString : (ILjava/lang/String;)V
    //   142: aload #6
    //   144: astore_1
    //   145: aload #6
    //   147: iconst_2
    //   148: aload_3
    //   149: invokeinterface setString : (ILjava/lang/String;)V
    //   154: aload #6
    //   156: astore_1
    //   157: aload_0
    //   158: aload #6
    //   160: invokevirtual executeMetadataQuery : (Ljava/sql/PreparedStatement;)Ljava/sql/ResultSet;
    //   163: astore_2
    //   164: aload #6
    //   166: astore_1
    //   167: aload_2
    //   168: checkcast com/mysql/jdbc/ResultSetInternalMethods
    //   171: aload_0
    //   172: invokevirtual createIndexInfoFields : ()[Lcom/mysql/jdbc/Field;
    //   175: invokeinterface redefineFieldsForDBMD : ([Lcom/mysql/jdbc/Field;)V
    //   180: aload #6
    //   182: ifnull -> 192
    //   185: aload #6
    //   187: invokeinterface close : ()V
    //   192: aload_2
    //   193: areturn
    //   194: astore_2
    //   195: aload_1
    //   196: ifnull -> 205
    //   199: aload_1
    //   200: invokeinterface close : ()V
    //   205: aload_2
    //   206: athrow
    // Exception table:
    //   from	to	target	type
    //   76	88	194	finally
    //   91	96	194	finally
    //   99	110	194	finally
    //   117	126	194	finally
    //   132	142	194	finally
    //   145	154	194	finally
    //   157	164	194	finally
    //   167	180	194	finally
  }
  
  public int getJDBC4FunctionConstant(JDBC4FunctionConstant paramJDBC4FunctionConstant) {
    return 0;
  }
  
  public int getJDBC4FunctionNoTableConstant() {
    return 0;
  }
  
  public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
    paramString2 = paramString1;
    if (paramString1 == null) {
      paramString2 = paramString1;
      if (this.conn.getNullCatalogMeansCurrent())
        paramString2 = this.database; 
    } 
    if (paramString3 != null) {
      PreparedStatement preparedStatement;
      paramString1 = null;
      try {
        PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, SEQ_IN_INDEX AS KEY_SEQ, 'PRIMARY' AS PK_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND INDEX_NAME='PRIMARY' ORDER BY TABLE_SCHEMA, TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX");
        if (paramString2 != null) {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, paramString2);
        } else {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, "%");
        } 
        preparedStatement = preparedStatement1;
        preparedStatement1.setString(2, paramString3);
        preparedStatement = preparedStatement1;
        ResultSet resultSet = executeMetadataQuery(preparedStatement1);
        preparedStatement = preparedStatement1;
        ResultSetInternalMethods resultSetInternalMethods = (ResultSetInternalMethods)resultSet;
        preparedStatement = preparedStatement1;
        Field field4 = new Field();
        preparedStatement = preparedStatement1;
        this("", "TABLE_CAT", 1, 255);
        preparedStatement = preparedStatement1;
        Field field2 = new Field();
        preparedStatement = preparedStatement1;
        this("", "TABLE_SCHEM", 1, 0);
        preparedStatement = preparedStatement1;
        Field field3 = new Field();
        preparedStatement = preparedStatement1;
        this("", "TABLE_NAME", 1, 255);
        preparedStatement = preparedStatement1;
        Field field6 = new Field();
        preparedStatement = preparedStatement1;
        this("", "COLUMN_NAME", 1, 32);
        preparedStatement = preparedStatement1;
        Field field1 = new Field();
        preparedStatement = preparedStatement1;
        this("", "KEY_SEQ", 5, 5);
        preparedStatement = preparedStatement1;
        Field field5 = new Field();
        preparedStatement = preparedStatement1;
        this("", "PK_NAME", 1, 32);
        preparedStatement = preparedStatement1;
        resultSetInternalMethods.redefineFieldsForDBMD(new Field[] { field4, field2, field3, field6, field1, field5 });
        return resultSet;
      } finally {
        if (preparedStatement != null)
          preparedStatement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield hasParametersView : Z
    //   4: ifne -> 17
    //   7: aload_0
    //   8: aload_1
    //   9: aload_2
    //   10: aload_3
    //   11: aload #4
    //   13: invokevirtual getProcedureColumnsNoISParametersView : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
    //   16: areturn
    //   17: aload_3
    //   18: ifnull -> 31
    //   21: aload_3
    //   22: astore #5
    //   24: aload_3
    //   25: invokevirtual length : ()I
    //   28: ifne -> 47
    //   31: aload_0
    //   32: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   35: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   40: ifeq -> 335
    //   43: ldc '%'
    //   45: astore #5
    //   47: aconst_null
    //   48: astore_3
    //   49: aload_1
    //   50: astore_2
    //   51: aload_1
    //   52: ifnonnull -> 77
    //   55: aload_0
    //   56: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   59: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   64: ifeq -> 75
    //   67: aload_0
    //   68: getfield database : Ljava/lang/String;
    //   71: astore_2
    //   72: goto -> 77
    //   75: aconst_null
    //   76: astore_2
    //   77: new java/lang/StringBuilder
    //   80: dup
    //   81: ldc_w 'SELECT SPECIFIC_SCHEMA AS PROCEDURE_CAT, NULL AS `PROCEDURE_SCHEM`, SPECIFIC_NAME AS `PROCEDURE_NAME`, IFNULL(PARAMETER_NAME, '') AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN 1 WHEN PARAMETER_MODE = 'OUT' THEN 4 WHEN PARAMETER_MODE = 'INOUT' THEN 2 WHEN ORDINAL_POSITION = 0 THEN 5 ELSE 0 END AS `COLUMN_TYPE`, '
    //   84: invokespecial <init> : (Ljava/lang/String;)V
    //   87: astore #6
    //   89: aload #6
    //   91: ldc 'DATA_TYPE'
    //   93: invokestatic appendJdbcTypeMappingQuery : (Ljava/lang/StringBuilder;Ljava/lang/String;)V
    //   96: aload #6
    //   98: ldc_w ' AS `DATA_TYPE`, '
    //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload_0
    //   106: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   109: invokeinterface getCapitalizeTypeNames : ()Z
    //   114: ifeq -> 129
    //   117: aload #6
    //   119: ldc_w 'UPPER(CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS `TYPE_NAME`,'
    //   122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: pop
    //   126: goto -> 138
    //   129: aload #6
    //   131: ldc_w 'CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS `TYPE_NAME`,'
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: aload #6
    //   140: ldc_w 'NUMERIC_PRECISION AS `PRECISION`, '
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload #6
    //   149: ldc_w 'CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH, '
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload #6
    //   158: ldc_w 'NUMERIC_SCALE AS `SCALE`, '
    //   161: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload #6
    //   167: ldc_w '10 AS RADIX,'
    //   170: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: new java/lang/StringBuilder
    //   177: dup
    //   178: invokespecial <init> : ()V
    //   181: astore_1
    //   182: aload_1
    //   183: ldc_w '1 AS `NULLABLE`, NULL AS `REMARKS`, NULL AS `COLUMN_DEF`, NULL AS `SQL_DATA_TYPE`, NULL AS `SQL_DATETIME_SUB`, CHARACTER_OCTET_LENGTH AS `CHAR_OCTET_LENGTH`, ORDINAL_POSITION, 'YES' AS `IS_NULLABLE`, SPECIFIC_NAME FROM INFORMATION_SCHEMA.PARAMETERS WHERE '
    //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: pop
    //   190: aload_1
    //   191: aload_0
    //   192: invokevirtual getRoutineTypeConditionForGetProcedureColumns : ()Ljava/lang/String;
    //   195: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: pop
    //   199: aload_1
    //   200: ldc_w 'SPECIFIC_SCHEMA LIKE ? AND SPECIFIC_NAME LIKE ? AND (PARAMETER_NAME LIKE ? OR PARAMETER_NAME IS NULL) '
    //   203: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: pop
    //   207: aload_1
    //   208: ldc_w 'ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ROUTINE_TYPE, ORDINAL_POSITION'
    //   211: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: pop
    //   215: aload #6
    //   217: aload_1
    //   218: invokevirtual toString : ()Ljava/lang/String;
    //   221: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload_3
    //   226: astore_1
    //   227: aload_0
    //   228: aload #6
    //   230: invokevirtual toString : ()Ljava/lang/String;
    //   233: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   236: astore_3
    //   237: aload_2
    //   238: ifnull -> 254
    //   241: aload_3
    //   242: astore_1
    //   243: aload_3
    //   244: iconst_1
    //   245: aload_2
    //   246: invokeinterface setString : (ILjava/lang/String;)V
    //   251: goto -> 265
    //   254: aload_3
    //   255: astore_1
    //   256: aload_3
    //   257: iconst_1
    //   258: ldc '%'
    //   260: invokeinterface setString : (ILjava/lang/String;)V
    //   265: aload_3
    //   266: astore_1
    //   267: aload_3
    //   268: iconst_2
    //   269: aload #5
    //   271: invokeinterface setString : (ILjava/lang/String;)V
    //   276: aload_3
    //   277: astore_1
    //   278: aload_3
    //   279: iconst_3
    //   280: aload #4
    //   282: invokeinterface setString : (ILjava/lang/String;)V
    //   287: aload_3
    //   288: astore_1
    //   289: aload_0
    //   290: aload_3
    //   291: invokevirtual executeMetadataQuery : (Ljava/sql/PreparedStatement;)Ljava/sql/ResultSet;
    //   294: astore_2
    //   295: aload_3
    //   296: astore_1
    //   297: aload_2
    //   298: checkcast com/mysql/jdbc/ResultSetInternalMethods
    //   301: aload_0
    //   302: invokevirtual createProcedureColumnsFields : ()[Lcom/mysql/jdbc/Field;
    //   305: invokeinterface redefineFieldsForDBMD : ([Lcom/mysql/jdbc/Field;)V
    //   310: aload_3
    //   311: ifnull -> 320
    //   314: aload_3
    //   315: invokeinterface close : ()V
    //   320: aload_2
    //   321: areturn
    //   322: astore_2
    //   323: aload_1
    //   324: ifnull -> 333
    //   327: aload_1
    //   328: invokeinterface close : ()V
    //   333: aload_2
    //   334: athrow
    //   335: ldc_w 'Procedure name pattern can not be NULL or empty.'
    //   338: ldc 'S1009'
    //   340: aload_0
    //   341: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   344: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   347: athrow
    // Exception table:
    //   from	to	target	type
    //   227	237	322	finally
    //   243	251	322	finally
    //   256	265	322	finally
    //   267	276	322	finally
    //   278	287	322	finally
    //   289	295	322	finally
    //   297	310	322	finally
  }
  
  public ResultSet getProcedureColumnsNoISParametersView(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
    return super.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
  }
  
  public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
    // Byte code:
    //   0: aload_3
    //   1: ifnull -> 14
    //   4: aload_3
    //   5: astore #4
    //   7: aload_3
    //   8: invokevirtual length : ()I
    //   11: ifne -> 30
    //   14: aload_0
    //   15: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   18: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   23: ifeq -> 195
    //   26: ldc '%'
    //   28: astore #4
    //   30: aconst_null
    //   31: astore_3
    //   32: aload_1
    //   33: astore_2
    //   34: aload_1
    //   35: ifnonnull -> 60
    //   38: aload_0
    //   39: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   42: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   47: ifeq -> 58
    //   50: aload_0
    //   51: getfield database : Ljava/lang/String;
    //   54: astore_2
    //   55: goto -> 60
    //   58: aconst_null
    //   59: astore_2
    //   60: new java/lang/StringBuilder
    //   63: dup
    //   64: invokespecial <init> : ()V
    //   67: astore_1
    //   68: aload_1
    //   69: ldc_w 'SELECT ROUTINE_SCHEMA AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, ROUTINE_NAME AS PROCEDURE_NAME, NULL AS RESERVED_1, NULL AS RESERVED_2, NULL AS RESERVED_3, ROUTINE_COMMENT AS REMARKS, CASE WHEN ROUTINE_TYPE = 'PROCEDURE' THEN 1 WHEN ROUTINE_TYPE='FUNCTION' THEN 2 ELSE 0 END AS PROCEDURE_TYPE, ROUTINE_NAME AS SPECIFIC_NAME FROM INFORMATION_SCHEMA.ROUTINES WHERE '
    //   72: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: pop
    //   76: aload_1
    //   77: aload_0
    //   78: invokevirtual getRoutineTypeConditionForGetProcedures : ()Ljava/lang/String;
    //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: aload_1
    //   86: ldc_w 'ROUTINE_SCHEMA LIKE ? AND ROUTINE_NAME LIKE ? ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME, ROUTINE_TYPE'
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: aload_1
    //   94: invokevirtual toString : ()Ljava/lang/String;
    //   97: astore #5
    //   99: aload_3
    //   100: astore_1
    //   101: aload_0
    //   102: aload #5
    //   104: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   107: astore_3
    //   108: aload_2
    //   109: ifnull -> 125
    //   112: aload_3
    //   113: astore_1
    //   114: aload_3
    //   115: iconst_1
    //   116: aload_2
    //   117: invokeinterface setString : (ILjava/lang/String;)V
    //   122: goto -> 136
    //   125: aload_3
    //   126: astore_1
    //   127: aload_3
    //   128: iconst_1
    //   129: ldc '%'
    //   131: invokeinterface setString : (ILjava/lang/String;)V
    //   136: aload_3
    //   137: astore_1
    //   138: aload_3
    //   139: iconst_2
    //   140: aload #4
    //   142: invokeinterface setString : (ILjava/lang/String;)V
    //   147: aload_3
    //   148: astore_1
    //   149: aload_0
    //   150: aload_3
    //   151: invokevirtual executeMetadataQuery : (Ljava/sql/PreparedStatement;)Ljava/sql/ResultSet;
    //   154: astore_2
    //   155: aload_3
    //   156: astore_1
    //   157: aload_2
    //   158: checkcast com/mysql/jdbc/ResultSetInternalMethods
    //   161: aload_0
    //   162: invokevirtual createFieldMetadataForGetProcedures : ()[Lcom/mysql/jdbc/Field;
    //   165: invokeinterface redefineFieldsForDBMD : ([Lcom/mysql/jdbc/Field;)V
    //   170: aload_3
    //   171: ifnull -> 180
    //   174: aload_3
    //   175: invokeinterface close : ()V
    //   180: aload_2
    //   181: areturn
    //   182: astore_2
    //   183: aload_1
    //   184: ifnull -> 193
    //   187: aload_1
    //   188: invokeinterface close : ()V
    //   193: aload_2
    //   194: athrow
    //   195: ldc_w 'Procedure name pattern can not be NULL or empty.'
    //   198: ldc 'S1009'
    //   200: aload_0
    //   201: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   204: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   207: athrow
    // Exception table:
    //   from	to	target	type
    //   101	108	182	finally
    //   114	122	182	finally
    //   127	136	182	finally
    //   138	147	182	finally
    //   149	155	182	finally
    //   157	170	182	finally
  }
  
  public String getRoutineTypeConditionForGetProcedureColumns() {
    return "";
  }
  
  public String getRoutineTypeConditionForGetProcedures() {
    return "";
  }
  
  public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) throws SQLException {
    // Byte code:
    //   0: aload_1
    //   1: astore_2
    //   2: aload_1
    //   3: ifnonnull -> 25
    //   6: aload_1
    //   7: astore_2
    //   8: aload_0
    //   9: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   12: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   17: ifeq -> 25
    //   20: aload_0
    //   21: getfield database : Ljava/lang/String;
    //   24: astore_2
    //   25: aload_3
    //   26: astore_1
    //   27: aload_3
    //   28: ifnonnull -> 62
    //   31: aload_0
    //   32: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   35: invokeinterface getNullNamePatternMatchesAll : ()Z
    //   40: ifeq -> 49
    //   43: ldc '%'
    //   45: astore_1
    //   46: goto -> 62
    //   49: ldc_w 'Table name pattern can not be NULL or empty.'
    //   52: ldc 'S1009'
    //   54: aload_0
    //   55: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   58: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   61: athrow
    //   62: aload_2
    //   63: ifnull -> 81
    //   66: aload_2
    //   67: invokevirtual length : ()I
    //   70: ifne -> 76
    //   73: goto -> 81
    //   76: aload_2
    //   77: astore_3
    //   78: goto -> 104
    //   81: aload_0
    //   82: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   85: invokeinterface getNullCatalogMeansCurrent : ()Z
    //   90: ifeq -> 101
    //   93: aload_0
    //   94: getfield database : Ljava/lang/String;
    //   97: astore_3
    //   98: goto -> 104
    //   101: ldc ''
    //   103: astore_3
    //   104: aload_1
    //   105: aload_3
    //   106: aload_0
    //   107: getfield quotedId : Ljava/lang/String;
    //   110: aload_0
    //   111: getfield conn : Lcom/mysql/jdbc/MySQLConnection;
    //   114: invokeinterface isNoBackslashEscapesSet : ()Z
    //   119: invokestatic splitDBdotName : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
    //   122: astore #9
    //   124: aload_1
    //   125: astore_3
    //   126: aload #9
    //   128: invokeinterface size : ()I
    //   133: iconst_2
    //   134: if_icmpne -> 149
    //   137: aload #9
    //   139: iconst_1
    //   140: invokeinterface get : (I)Ljava/lang/Object;
    //   145: checkcast java/lang/String
    //   148: astore_3
    //   149: aconst_null
    //   150: astore #9
    //   152: ldc_w 'information_schema'
    //   155: aload_2
    //   156: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   159: istore #8
    //   161: iconst_0
    //   162: istore #5
    //   164: iconst_0
    //   165: istore #6
    //   167: aload_2
    //   168: ifnull -> 266
    //   171: iload #8
    //   173: ifne -> 234
    //   176: iconst_0
    //   177: aload_2
    //   178: ldc '%'
    //   180: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;)I
    //   183: iconst_m1
    //   184: if_icmpne -> 202
    //   187: iconst_0
    //   188: aload_2
    //   189: ldc_w '_'
    //   192: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;)I
    //   195: iconst_m1
    //   196: if_icmpne -> 202
    //   199: goto -> 234
    //   202: new java/lang/StringBuilder
    //   205: dup
    //   206: invokespecial <init> : ()V
    //   209: astore_1
    //   210: aload_1
    //   211: ldc_w 'SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, CASE WHEN TABLE_TYPE='BASE TABLE' THEN CASE WHEN TABLE_SCHEMA = 'mysql' OR TABLE_SCHEMA = 'performance_schema' THEN 'SYSTEM TABLE' ELSE 'TABLE' END WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, TABLE_COMMENT AS REMARKS, NULL AS TYPE_CAT, NULL AS TYPE_SCHEM, NULL AS TYPE_NAME, NULL AS SELF_REFERENCING_COL_NAME, NULL AS REF_GENERATION FROM INFORMATION_SCHEMA.TABLES WHERE '
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload_1
    //   219: ldc_w 'TABLE_SCHEMA LIKE ? '
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: aload_1
    //   227: invokevirtual toString : ()Ljava/lang/String;
    //   230: astore_1
    //   231: goto -> 295
    //   234: new java/lang/StringBuilder
    //   237: dup
    //   238: invokespecial <init> : ()V
    //   241: astore_1
    //   242: aload_1
    //   243: ldc_w 'SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, CASE WHEN TABLE_TYPE='BASE TABLE' THEN CASE WHEN TABLE_SCHEMA = 'mysql' OR TABLE_SCHEMA = 'performance_schema' THEN 'SYSTEM TABLE' ELSE 'TABLE' END WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, TABLE_COMMENT AS REMARKS, NULL AS TYPE_CAT, NULL AS TYPE_SCHEM, NULL AS TYPE_NAME, NULL AS SELF_REFERENCING_COL_NAME, NULL AS REF_GENERATION FROM INFORMATION_SCHEMA.TABLES WHERE '
    //   246: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   249: pop
    //   250: aload_1
    //   251: ldc_w 'TABLE_SCHEMA = ? '
    //   254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: pop
    //   258: aload_1
    //   259: invokevirtual toString : ()Ljava/lang/String;
    //   262: astore_1
    //   263: goto -> 295
    //   266: new java/lang/StringBuilder
    //   269: dup
    //   270: invokespecial <init> : ()V
    //   273: astore_1
    //   274: aload_1
    //   275: ldc_w 'SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, CASE WHEN TABLE_TYPE='BASE TABLE' THEN CASE WHEN TABLE_SCHEMA = 'mysql' OR TABLE_SCHEMA = 'performance_schema' THEN 'SYSTEM TABLE' ELSE 'TABLE' END WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, TABLE_COMMENT AS REMARKS, NULL AS TYPE_CAT, NULL AS TYPE_SCHEM, NULL AS TYPE_NAME, NULL AS SELF_REFERENCING_COL_NAME, NULL AS REF_GENERATION FROM INFORMATION_SCHEMA.TABLES WHERE '
    //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload_1
    //   283: ldc_w 'TABLE_SCHEMA LIKE ? '
    //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: aload_1
    //   291: invokevirtual toString : ()Ljava/lang/String;
    //   294: astore_1
    //   295: aload_3
    //   296: ifnull -> 390
    //   299: iconst_0
    //   300: aload_3
    //   301: ldc '%'
    //   303: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;)I
    //   306: iconst_m1
    //   307: if_icmpne -> 356
    //   310: iconst_0
    //   311: aload_3
    //   312: ldc_w '_'
    //   315: invokestatic indexOfIgnoreCase : (ILjava/lang/String;Ljava/lang/String;)I
    //   318: iconst_m1
    //   319: if_icmpne -> 356
    //   322: new java/lang/StringBuilder
    //   325: dup
    //   326: invokespecial <init> : ()V
    //   329: astore #10
    //   331: aload #10
    //   333: aload_1
    //   334: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   337: pop
    //   338: aload #10
    //   340: ldc_w 'AND TABLE_NAME = ? '
    //   343: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: pop
    //   347: aload #10
    //   349: invokevirtual toString : ()Ljava/lang/String;
    //   352: astore_1
    //   353: goto -> 421
    //   356: new java/lang/StringBuilder
    //   359: dup
    //   360: invokespecial <init> : ()V
    //   363: astore #10
    //   365: aload #10
    //   367: aload_1
    //   368: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   371: pop
    //   372: aload #10
    //   374: ldc_w 'AND TABLE_NAME LIKE ? '
    //   377: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   380: pop
    //   381: aload #10
    //   383: invokevirtual toString : ()Ljava/lang/String;
    //   386: astore_1
    //   387: goto -> 421
    //   390: new java/lang/StringBuilder
    //   393: dup
    //   394: invokespecial <init> : ()V
    //   397: astore #10
    //   399: aload #10
    //   401: aload_1
    //   402: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   405: pop
    //   406: aload #10
    //   408: ldc_w 'AND TABLE_NAME LIKE ? '
    //   411: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   414: pop
    //   415: aload #10
    //   417: invokevirtual toString : ()Ljava/lang/String;
    //   420: astore_1
    //   421: new java/lang/StringBuilder
    //   424: dup
    //   425: invokespecial <init> : ()V
    //   428: astore #10
    //   430: aload #10
    //   432: aload_1
    //   433: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   436: pop
    //   437: aload #10
    //   439: ldc_w 'HAVING TABLE_TYPE IN (?,?,?,?,?) '
    //   442: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   445: pop
    //   446: aload #10
    //   448: invokevirtual toString : ()Ljava/lang/String;
    //   451: astore_1
    //   452: new java/lang/StringBuilder
    //   455: dup
    //   456: invokespecial <init> : ()V
    //   459: astore #10
    //   461: aload #10
    //   463: aload_1
    //   464: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   467: pop
    //   468: aload #10
    //   470: ldc_w 'ORDER BY TABLE_TYPE, TABLE_SCHEMA, TABLE_NAME'
    //   473: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   476: pop
    //   477: aload #10
    //   479: invokevirtual toString : ()Ljava/lang/String;
    //   482: astore #10
    //   484: aload #9
    //   486: astore_1
    //   487: aload_0
    //   488: aload #10
    //   490: invokevirtual prepareMetaDataSafeStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   493: astore #9
    //   495: aload_2
    //   496: ifnull -> 514
    //   499: aload #9
    //   501: astore_1
    //   502: aload #9
    //   504: iconst_1
    //   505: aload_2
    //   506: invokeinterface setString : (ILjava/lang/String;)V
    //   511: goto -> 527
    //   514: aload #9
    //   516: astore_1
    //   517: aload #9
    //   519: iconst_1
    //   520: ldc '%'
    //   522: invokeinterface setString : (ILjava/lang/String;)V
    //   527: aload #9
    //   529: astore_1
    //   530: aload #9
    //   532: iconst_2
    //   533: aload_3
    //   534: invokeinterface setString : (ILjava/lang/String;)V
    //   539: aload #4
    //   541: ifnull -> 659
    //   544: aload #9
    //   546: astore_1
    //   547: aload #4
    //   549: arraylength
    //   550: ifne -> 556
    //   553: goto -> 659
    //   556: iconst_0
    //   557: istore #5
    //   559: iload #5
    //   561: iconst_5
    //   562: if_icmpge -> 587
    //   565: aload #9
    //   567: astore_1
    //   568: aload #9
    //   570: iload #5
    //   572: iconst_3
    //   573: iadd
    //   574: bipush #12
    //   576: invokeinterface setNull : (II)V
    //   581: iinc #5, 1
    //   584: goto -> 559
    //   587: iconst_3
    //   588: istore #5
    //   590: aload #9
    //   592: astore_1
    //   593: iload #6
    //   595: aload #4
    //   597: arraylength
    //   598: if_icmpge -> 699
    //   601: aload #9
    //   603: astore_1
    //   604: aload #4
    //   606: iload #6
    //   608: aaload
    //   609: invokestatic getTableTypeEqualTo : (Ljava/lang/String;)Lcom/mysql/jdbc/DatabaseMetaData$TableType;
    //   612: astore_2
    //   613: iload #5
    //   615: istore #7
    //   617: aload #9
    //   619: astore_1
    //   620: aload_2
    //   621: getstatic com/mysql/jdbc/DatabaseMetaData$TableType.UNKNOWN : Lcom/mysql/jdbc/DatabaseMetaData$TableType;
    //   624: if_acmpeq -> 649
    //   627: aload #9
    //   629: astore_1
    //   630: aload #9
    //   632: iload #5
    //   634: aload_2
    //   635: invokevirtual getName : ()Ljava/lang/String;
    //   638: invokeinterface setString : (ILjava/lang/String;)V
    //   643: iload #5
    //   645: iconst_1
    //   646: iadd
    //   647: istore #7
    //   649: iinc #6, 1
    //   652: iload #7
    //   654: istore #5
    //   656: goto -> 590
    //   659: aload #9
    //   661: astore_1
    //   662: invokestatic values : ()[Lcom/mysql/jdbc/DatabaseMetaData$TableType;
    //   665: astore_2
    //   666: iload #5
    //   668: iconst_5
    //   669: if_icmpge -> 699
    //   672: aload #9
    //   674: astore_1
    //   675: aload #9
    //   677: iload #5
    //   679: iconst_3
    //   680: iadd
    //   681: aload_2
    //   682: iload #5
    //   684: aaload
    //   685: invokevirtual getName : ()Ljava/lang/String;
    //   688: invokeinterface setString : (ILjava/lang/String;)V
    //   693: iinc #5, 1
    //   696: goto -> 666
    //   699: aload #9
    //   701: astore_1
    //   702: aload_0
    //   703: aload #9
    //   705: invokevirtual executeMetadataQuery : (Ljava/sql/PreparedStatement;)Ljava/sql/ResultSet;
    //   708: astore_2
    //   709: aload #9
    //   711: astore_1
    //   712: aload_2
    //   713: checkcast com/mysql/jdbc/ResultSetInternalMethods
    //   716: aload_0
    //   717: invokevirtual createTablesFields : ()[Lcom/mysql/jdbc/Field;
    //   720: invokeinterface redefineFieldsForDBMD : ([Lcom/mysql/jdbc/Field;)V
    //   725: aload #9
    //   727: ifnull -> 737
    //   730: aload #9
    //   732: invokeinterface close : ()V
    //   737: aload_2
    //   738: areturn
    //   739: astore_2
    //   740: aload_1
    //   741: ifnull -> 750
    //   744: aload_1
    //   745: invokeinterface close : ()V
    //   750: aload_2
    //   751: athrow
    // Exception table:
    //   from	to	target	type
    //   487	495	739	finally
    //   502	511	739	finally
    //   517	527	739	finally
    //   530	539	739	finally
    //   547	553	739	finally
    //   568	581	739	finally
    //   593	601	739	finally
    //   604	613	739	finally
    //   620	627	739	finally
    //   630	643	739	finally
    //   662	666	739	finally
    //   675	693	739	finally
    //   702	709	739	finally
    //   712	725	739	finally
  }
  
  public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) throws SQLException {
    paramString2 = paramString1;
    if (paramString1 == null) {
      paramString2 = paramString1;
      if (this.conn.getNullCatalogMeansCurrent())
        paramString2 = this.database; 
    } 
    if (paramString3 != null) {
      PreparedStatement preparedStatement;
      StringBuilder stringBuilder2 = new StringBuilder("SELECT NULL AS SCOPE, COLUMN_NAME, ");
      MysqlDefs.appendJdbcTypeMappingQuery(stringBuilder2, "DATA_TYPE");
      stringBuilder2.append(" AS DATA_TYPE, ");
      stringBuilder2.append("COLUMN_TYPE AS TYPE_NAME, ");
      stringBuilder2.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS COLUMN_SIZE, ");
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(MysqlIO.getMaxBuf());
      stringBuilder1.append(" AS BUFFER_LENGTH,NUMERIC_SCALE AS DECIMAL_DIGITS, ");
      stringBuilder1.append(Integer.toString(1));
      stringBuilder1.append(" AS PSEUDO_COLUMN FROM INFORMATION_SCHEMA.COLUMNS ");
      stringBuilder1.append("WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND EXTRA LIKE '%on update CURRENT_TIMESTAMP%'");
      stringBuilder2.append(stringBuilder1.toString());
      stringBuilder1 = null;
      try {
        PreparedStatement preparedStatement1 = prepareMetaDataSafeStatement(stringBuilder2.toString());
        if (paramString2 != null) {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, paramString2);
        } else {
          PreparedStatement preparedStatement2 = preparedStatement1;
          preparedStatement1.setString(1, "%");
        } 
        preparedStatement = preparedStatement1;
        preparedStatement1.setString(2, paramString3);
        preparedStatement = preparedStatement1;
        ResultSet resultSet = executeMetadataQuery(preparedStatement1);
        preparedStatement = preparedStatement1;
        ResultSetInternalMethods resultSetInternalMethods = (ResultSetInternalMethods)resultSet;
        preparedStatement = preparedStatement1;
        Field field5 = new Field();
        preparedStatement = preparedStatement1;
        this("", "SCOPE", 5, 5);
        preparedStatement = preparedStatement1;
        Field field2 = new Field();
        preparedStatement = preparedStatement1;
        this("", "COLUMN_NAME", 1, 32);
        preparedStatement = preparedStatement1;
        Field field7 = new Field();
        preparedStatement = preparedStatement1;
        this("", "DATA_TYPE", 4, 5);
        preparedStatement = preparedStatement1;
        Field field8 = new Field();
        preparedStatement = preparedStatement1;
        this("", "TYPE_NAME", 1, 16);
        preparedStatement = preparedStatement1;
        Field field3 = new Field();
        preparedStatement = preparedStatement1;
        this("", "COLUMN_SIZE", 4, 16);
        preparedStatement = preparedStatement1;
        Field field6 = new Field();
        preparedStatement = preparedStatement1;
        this("", "BUFFER_LENGTH", 4, 16);
        preparedStatement = preparedStatement1;
        Field field1 = new Field();
        preparedStatement = preparedStatement1;
        this("", "DECIMAL_DIGITS", 5, 16);
        preparedStatement = preparedStatement1;
        Field field4 = new Field();
        preparedStatement = preparedStatement1;
        this("", "PSEUDO_COLUMN", 5, 5);
        preparedStatement = preparedStatement1;
        resultSetInternalMethods.redefineFieldsForDBMD(new Field[] { field5, field2, field7, field8, field3, field6, field1, field4 });
        return resultSet;
      } finally {
        if (preparedStatement != null)
          preparedStatement.close(); 
      } 
    } 
    throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
  }
  
  public boolean gethasParametersView() {
    return this.hasParametersView;
  }
  
  public enum JDBC4FunctionConstant {
    FUNCTION_COLUMN_IN, FUNCTION_COLUMN_INOUT, FUNCTION_COLUMN_OUT, FUNCTION_COLUMN_RESULT, FUNCTION_COLUMN_RETURN, FUNCTION_COLUMN_UNKNOWN, FUNCTION_NO_NULLS, FUNCTION_NULLABLE, FUNCTION_NULLABLE_UNKNOWN;
    
    private static final JDBC4FunctionConstant[] $VALUES;
    
    static {
      JDBC4FunctionConstant jDBC4FunctionConstant9 = new JDBC4FunctionConstant("FUNCTION_COLUMN_UNKNOWN", 0);
      FUNCTION_COLUMN_UNKNOWN = jDBC4FunctionConstant9;
      JDBC4FunctionConstant jDBC4FunctionConstant5 = new JDBC4FunctionConstant("FUNCTION_COLUMN_IN", 1);
      FUNCTION_COLUMN_IN = jDBC4FunctionConstant5;
      JDBC4FunctionConstant jDBC4FunctionConstant1 = new JDBC4FunctionConstant("FUNCTION_COLUMN_INOUT", 2);
      FUNCTION_COLUMN_INOUT = jDBC4FunctionConstant1;
      JDBC4FunctionConstant jDBC4FunctionConstant3 = new JDBC4FunctionConstant("FUNCTION_COLUMN_OUT", 3);
      FUNCTION_COLUMN_OUT = jDBC4FunctionConstant3;
      JDBC4FunctionConstant jDBC4FunctionConstant6 = new JDBC4FunctionConstant("FUNCTION_COLUMN_RETURN", 4);
      FUNCTION_COLUMN_RETURN = jDBC4FunctionConstant6;
      JDBC4FunctionConstant jDBC4FunctionConstant4 = new JDBC4FunctionConstant("FUNCTION_COLUMN_RESULT", 5);
      FUNCTION_COLUMN_RESULT = jDBC4FunctionConstant4;
      JDBC4FunctionConstant jDBC4FunctionConstant2 = new JDBC4FunctionConstant("FUNCTION_NO_NULLS", 6);
      FUNCTION_NO_NULLS = jDBC4FunctionConstant2;
      JDBC4FunctionConstant jDBC4FunctionConstant7 = new JDBC4FunctionConstant("FUNCTION_NULLABLE", 7);
      FUNCTION_NULLABLE = jDBC4FunctionConstant7;
      JDBC4FunctionConstant jDBC4FunctionConstant8 = new JDBC4FunctionConstant("FUNCTION_NULLABLE_UNKNOWN", 8);
      FUNCTION_NULLABLE_UNKNOWN = jDBC4FunctionConstant8;
      $VALUES = new JDBC4FunctionConstant[] { jDBC4FunctionConstant9, jDBC4FunctionConstant5, jDBC4FunctionConstant1, jDBC4FunctionConstant3, jDBC4FunctionConstant6, jDBC4FunctionConstant4, jDBC4FunctionConstant2, jDBC4FunctionConstant7, jDBC4FunctionConstant8 };
    }
  }
}
