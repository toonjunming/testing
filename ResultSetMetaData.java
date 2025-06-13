package com.mysql.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetMetaData implements ResultSetMetaData {
  private ExceptionInterceptor exceptionInterceptor;
  
  public Field[] fields;
  
  public boolean treatYearAsDate = true;
  
  public boolean useOldAliasBehavior = false;
  
  public ResultSetMetaData(Field[] paramArrayOfField, boolean paramBoolean1, boolean paramBoolean2, ExceptionInterceptor paramExceptionInterceptor) {
    this.fields = paramArrayOfField;
    this.useOldAliasBehavior = paramBoolean1;
    this.treatYearAsDate = paramBoolean2;
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  private static int clampedGetLength(Field paramField) {
    long l2 = paramField.getLength();
    long l1 = l2;
    if (l2 > 2147483647L)
      l1 = 2147483647L; 
    return (int)l1;
  }
  
  public static String getClassNameForJavaType(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (paramInt1 != 12)
      if (paramInt1 != 16) {
        switch (paramInt1) {
          default:
            switch (paramInt1) {
              default:
                switch (paramInt1) {
                  default:
                    return "java.lang.Object";
                  case 93:
                    return "java.sql.Timestamp";
                  case 92:
                    return "java.sql.Time";
                  case 91:
                    break;
                } 
                return (paramBoolean4 || paramInt2 != 13) ? "java.sql.Date" : "java.lang.Short";
              case 7:
                return "java.lang.Float";
              case 6:
              case 8:
                return "java.lang.Double";
              case 5:
                if (paramBoolean1);
                return "java.lang.Integer";
              case 4:
                return (!paramBoolean1 || paramInt2 == 9) ? "java.lang.Integer" : "java.lang.Long";
              case 2:
              case 3:
                return "java.math.BigDecimal";
              case 1:
                break;
            } 
            break;
          case -4:
          case -3:
          case -2:
            return (paramInt2 == 255) ? "[B" : (paramBoolean2 ? "[B" : "java.lang.String");
          case -5:
            return !paramBoolean1 ? "java.lang.Long" : "java.math.BigInteger";
          case -6:
            if (paramBoolean1);
            return "java.lang.Integer";
          case -7:
            return "java.lang.Boolean";
          case -1:
            break;
        } 
      } else {
      
      }  
    return !paramBoolean3 ? "java.lang.String" : "[B";
  }
  
  private static final boolean isDecimalType(int paramInt) {
    if (paramInt != -7 && paramInt != -6 && paramInt != -5)
      switch (paramInt) {
        default:
          return false;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
          break;
      }  
    return true;
  }
  
  public String getCatalogName(int paramInt) throws SQLException {
    String str2 = getField(paramInt).getDatabaseName();
    String str1 = str2;
    if (str2 == null)
      str1 = ""; 
    return str1;
  }
  
  public String getColumnCharacterEncoding(int paramInt) throws SQLException {
    SQLException sQLException;
    String str2 = getColumnCharacterSet(paramInt);
    String str1 = null;
    if (str2 != null)
      try {
        str1 = CharsetMapping.getJavaEncodingForMysqlCharset(str2);
      } catch (RuntimeException runtimeException) {
        sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
        sQLException.initCause(runtimeException);
        throw sQLException;
      }  
    return (String)sQLException;
  }
  
  public String getColumnCharacterSet(int paramInt) throws SQLException {
    return getField(paramInt).getEncoding();
  }
  
  public String getColumnClassName(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    int i = field.getSQLType();
    boolean bool1 = field.isUnsigned();
    paramInt = field.getMysqlType();
    if (field.isBinary() || field.isBlob()) {
      boolean bool2 = true;
      return getClassNameForJavaType(i, bool1, paramInt, bool2, field.isOpaqueBinary(), this.treatYearAsDate);
    } 
    boolean bool = false;
    return getClassNameForJavaType(i, bool1, paramInt, bool, field.isOpaqueBinary(), this.treatYearAsDate);
  }
  
  public int getColumnCount() throws SQLException {
    return this.fields.length;
  }
  
  public int getColumnDisplaySize(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    return clampedGetLength(field) / field.getMaxBytesPerCharacter();
  }
  
  public String getColumnLabel(int paramInt) throws SQLException {
    return this.useOldAliasBehavior ? getColumnName(paramInt) : getField(paramInt).getColumnLabel();
  }
  
  public String getColumnName(int paramInt) throws SQLException {
    if (this.useOldAliasBehavior)
      return getField(paramInt).getName(); 
    String str = getField(paramInt).getNameNoAliases();
    return (str != null && str.length() == 0) ? getField(paramInt).getName() : str;
  }
  
  public int getColumnType(int paramInt) throws SQLException {
    return getField(paramInt).getSQLType();
  }
  
  public String getColumnTypeName(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    int j = field.getMysqlType();
    int i = field.getSQLType();
    if (j != 15) {
      if (j != 16) {
        String str;
        switch (j) {
          default:
            switch (j) {
              default:
                return "UNKNOWN";
              case 255:
                return "GEOMETRY";
              case 254:
                return (i == -2) ? "BINARY" : "CHAR";
              case 253:
                return (i == -3) ? "VARBINARY" : "VARCHAR";
              case 252:
                return getField(paramInt).isBinary() ? "BLOB" : "TEXT";
              case 251:
                return "LONGBLOB";
              case 250:
                return "MEDIUMBLOB";
              case 249:
                return "TINYBLOB";
              case 248:
                return "SET";
              case 247:
                return "ENUM";
              case 245:
                return "JSON";
              case 246:
                break;
            } 
            break;
          case 13:
            return "YEAR";
          case 12:
            return "DATETIME";
          case 11:
            return "TIME";
          case 10:
            return "DATE";
          case 9:
            if (field.isUnsigned()) {
              str = "MEDIUMINT UNSIGNED";
            } else {
              str = "MEDIUMINT";
            } 
            return str;
          case 8:
            if (str.isUnsigned()) {
              str = "BIGINT UNSIGNED";
            } else {
              str = "BIGINT";
            } 
            return str;
          case 7:
            return "TIMESTAMP";
          case 6:
            return "NULL";
          case 5:
            if (str.isUnsigned()) {
              str = "DOUBLE UNSIGNED";
            } else {
              str = "DOUBLE";
            } 
            return str;
          case 4:
            if (str.isUnsigned()) {
              str = "FLOAT UNSIGNED";
            } else {
              str = "FLOAT";
            } 
            return str;
          case 3:
            if (str.isUnsigned()) {
              str = "INT UNSIGNED";
            } else {
              str = "INT";
            } 
            return str;
          case 2:
            if (str.isUnsigned()) {
              str = "SMALLINT UNSIGNED";
            } else {
              str = "SMALLINT";
            } 
            return str;
          case 1:
            if (str.isUnsigned()) {
              str = "TINYINT UNSIGNED";
            } else {
              str = "TINYINT";
            } 
            return str;
          case 0:
            break;
        } 
        if (str.isUnsigned()) {
          str = "DECIMAL UNSIGNED";
        } else {
          str = "DECIMAL";
        } 
        return str;
      } 
      return "BIT";
    } 
    return "VARCHAR";
  }
  
  public Field getField(int paramInt) throws SQLException {
    if (paramInt >= 1) {
      Field[] arrayOfField = this.fields;
      if (paramInt <= arrayOfField.length)
        return arrayOfField[paramInt - 1]; 
    } 
    throw SQLError.createSQLException(Messages.getString("ResultSetMetaData.46"), "S1002", this.exceptionInterceptor);
  }
  
  public int getPrecision(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    if (isDecimalType(field.getSQLType()))
      return (field.getDecimals() > 0) ? (clampedGetLength(field) - 1 + field.getPrecisionAdjustFactor()) : (clampedGetLength(field) + field.getPrecisionAdjustFactor()); 
    switch (field.getMysqlType()) {
      default:
        return clampedGetLength(field) / field.getMaxBytesPerCharacter();
      case 249:
      case 250:
      case 251:
      case 252:
        break;
    } 
    return clampedGetLength(field);
  }
  
  public int getScale(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    return isDecimalType(field.getSQLType()) ? field.getDecimals() : 0;
  }
  
  public String getSchemaName(int paramInt) throws SQLException {
    return "";
  }
  
  public String getTableName(int paramInt) throws SQLException {
    return this.useOldAliasBehavior ? getField(paramInt).getTableName() : getField(paramInt).getTableNameNoAliases();
  }
  
  public boolean isAutoIncrement(int paramInt) throws SQLException {
    return getField(paramInt).isAutoIncrement();
  }
  
  public boolean isCaseSensitive(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    paramInt = field.getSQLType();
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramInt != -7) {
      bool1 = bool2;
      if (paramInt != -6) {
        bool1 = bool2;
        if (paramInt != -5)
          if (paramInt != -1 && paramInt != 1 && paramInt != 12) {
            bool1 = bool2;
            switch (paramInt) {
              default:
                bool1 = bool2;
                switch (paramInt) {
                  default:
                    return true;
                  case 91:
                  case 92:
                  case 93:
                    break;
                } 
                break;
              case 4:
              case 5:
              case 6:
              case 7:
              case 8:
                break;
            } 
          } else {
            if (field.isBinary())
              return true; 
            String str = field.getCollation();
            bool1 = bool2;
            if (str != null) {
              bool1 = bool2;
              if (!str.endsWith("_ci"))
                bool1 = true; 
            } 
          }  
      } 
    } 
    return bool1;
  }
  
  public boolean isCurrency(int paramInt) throws SQLException {
    return false;
  }
  
  public boolean isDefinitelyWritable(int paramInt) throws SQLException {
    return isWritable(paramInt);
  }
  
  public int isNullable(int paramInt) throws SQLException {
    return !getField(paramInt).isNotNull() ? 1 : 0;
  }
  
  public boolean isReadOnly(int paramInt) throws SQLException {
    return getField(paramInt).isReadOnly();
  }
  
  public boolean isSearchable(int paramInt) throws SQLException {
    return true;
  }
  
  public boolean isSigned(int paramInt) throws SQLException {
    Field field = getField(paramInt);
    paramInt = field.getSQLType();
    if (paramInt != -6 && paramInt != -5)
      switch (paramInt) {
        default:
          return false;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
          break;
      }  
    return field.isUnsigned() ^ true;
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    return paramClass.isInstance(this);
  }
  
  public boolean isWritable(int paramInt) throws SQLException {
    return isReadOnly(paramInt) ^ true;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append(" - Field level information: ");
    for (byte b = 0; b < this.fields.length; b++) {
      stringBuilder.append("\n\t");
      stringBuilder.append(this.fields[b].toString());
    } 
    return stringBuilder.toString();
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", this.exceptionInterceptor);
    } 
  }
}
