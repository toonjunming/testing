package com.mysql.jdbc;

import java.util.HashMap;
import java.util.Map;

public final class MysqlDefs {
  public static final int COM_BINLOG_DUMP = 18;
  
  public static final int COM_CHANGE_USER = 17;
  
  public static final int COM_CLOSE_STATEMENT = 25;
  
  public static final int COM_CONNECT_OUT = 20;
  
  public static final int COM_END = 29;
  
  public static final int COM_EXECUTE = 23;
  
  public static final int COM_FETCH = 28;
  
  public static final int COM_LONG_DATA = 24;
  
  public static final int COM_PREPARE = 22;
  
  public static final int COM_REGISTER_SLAVE = 21;
  
  public static final int COM_RESET_STMT = 26;
  
  public static final int COM_SET_OPTION = 27;
  
  public static final int COM_TABLE_DUMP = 19;
  
  public static final int CONNECT = 11;
  
  public static final int CREATE_DB = 5;
  
  public static final int DEBUG = 13;
  
  public static final int DELAYED_INSERT = 16;
  
  public static final int DROP_DB = 6;
  
  public static final int FIELD_LIST = 4;
  
  public static final int FIELD_TYPE_BIT = 16;
  
  public static final int FIELD_TYPE_BLOB = 252;
  
  public static final int FIELD_TYPE_DATE = 10;
  
  public static final int FIELD_TYPE_DATETIME = 12;
  
  public static final int FIELD_TYPE_DECIMAL = 0;
  
  public static final int FIELD_TYPE_DOUBLE = 5;
  
  public static final int FIELD_TYPE_ENUM = 247;
  
  public static final int FIELD_TYPE_FLOAT = 4;
  
  public static final int FIELD_TYPE_GEOMETRY = 255;
  
  public static final int FIELD_TYPE_INT24 = 9;
  
  public static final int FIELD_TYPE_JSON = 245;
  
  public static final int FIELD_TYPE_LONG = 3;
  
  public static final int FIELD_TYPE_LONGLONG = 8;
  
  public static final int FIELD_TYPE_LONG_BLOB = 251;
  
  public static final int FIELD_TYPE_MEDIUM_BLOB = 250;
  
  public static final int FIELD_TYPE_NEWDATE = 14;
  
  public static final int FIELD_TYPE_NEW_DECIMAL = 246;
  
  public static final int FIELD_TYPE_NULL = 6;
  
  public static final int FIELD_TYPE_SET = 248;
  
  public static final int FIELD_TYPE_SHORT = 2;
  
  public static final int FIELD_TYPE_STRING = 254;
  
  public static final int FIELD_TYPE_TIME = 11;
  
  public static final int FIELD_TYPE_TIMESTAMP = 7;
  
  public static final int FIELD_TYPE_TINY = 1;
  
  public static final int FIELD_TYPE_TINY_BLOB = 249;
  
  public static final int FIELD_TYPE_VARCHAR = 15;
  
  public static final int FIELD_TYPE_VAR_STRING = 253;
  
  public static final int FIELD_TYPE_YEAR = 13;
  
  public static final int INIT_DB = 2;
  
  public static final long LENGTH_BLOB = 65535L;
  
  public static final long LENGTH_LONGBLOB = 4294967295L;
  
  public static final long LENGTH_MEDIUMBLOB = 16777215L;
  
  public static final long LENGTH_TINYBLOB = 255L;
  
  public static final int MAX_ROWS = 50000000;
  
  public static final int NO_CHARSET_INFO = -1;
  
  public static final byte OPEN_CURSOR_FLAG = 1;
  
  public static final int PING = 14;
  
  public static final int PROCESS_INFO = 10;
  
  public static final int PROCESS_KILL = 12;
  
  public static final int QUERY = 3;
  
  public static final int QUIT = 1;
  
  public static final int RELOAD = 7;
  
  public static final int SHUTDOWN = 8;
  
  public static final int SLEEP = 0;
  
  public static final int STATISTICS = 9;
  
  public static final int TIME = 15;
  
  private static Map<String, Integer> mysqlToJdbcTypesMap;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    mysqlToJdbcTypesMap = (Map)hashMap;
    hashMap.put("BIT", Integer.valueOf(mysqlToJavaType(16)));
    mysqlToJdbcTypesMap.put("TINYINT", Integer.valueOf(mysqlToJavaType(1)));
    mysqlToJdbcTypesMap.put("SMALLINT", Integer.valueOf(mysqlToJavaType(2)));
    mysqlToJdbcTypesMap.put("MEDIUMINT", Integer.valueOf(mysqlToJavaType(9)));
    mysqlToJdbcTypesMap.put("INT", Integer.valueOf(mysqlToJavaType(3)));
    mysqlToJdbcTypesMap.put("INTEGER", Integer.valueOf(mysqlToJavaType(3)));
    mysqlToJdbcTypesMap.put("BIGINT", Integer.valueOf(mysqlToJavaType(8)));
    mysqlToJdbcTypesMap.put("INT24", Integer.valueOf(mysqlToJavaType(9)));
    mysqlToJdbcTypesMap.put("REAL", Integer.valueOf(mysqlToJavaType(5)));
    mysqlToJdbcTypesMap.put("FLOAT", Integer.valueOf(mysqlToJavaType(4)));
    mysqlToJdbcTypesMap.put("DECIMAL", Integer.valueOf(mysqlToJavaType(0)));
    mysqlToJdbcTypesMap.put("NUMERIC", Integer.valueOf(mysqlToJavaType(0)));
    mysqlToJdbcTypesMap.put("DOUBLE", Integer.valueOf(mysqlToJavaType(5)));
    mysqlToJdbcTypesMap.put("CHAR", Integer.valueOf(mysqlToJavaType(254)));
    mysqlToJdbcTypesMap.put("VARCHAR", Integer.valueOf(mysqlToJavaType(253)));
    mysqlToJdbcTypesMap.put("DATE", Integer.valueOf(mysqlToJavaType(10)));
    mysqlToJdbcTypesMap.put("TIME", Integer.valueOf(mysqlToJavaType(11)));
    mysqlToJdbcTypesMap.put("YEAR", Integer.valueOf(mysqlToJavaType(13)));
    mysqlToJdbcTypesMap.put("TIMESTAMP", Integer.valueOf(mysqlToJavaType(7)));
    mysqlToJdbcTypesMap.put("DATETIME", Integer.valueOf(mysqlToJavaType(12)));
    mysqlToJdbcTypesMap.put("TINYBLOB", Integer.valueOf(-2));
    Map<String, Integer> map2 = mysqlToJdbcTypesMap;
    Integer integer1 = Integer.valueOf(-4);
    map2.put("BLOB", integer1);
    mysqlToJdbcTypesMap.put("MEDIUMBLOB", integer1);
    mysqlToJdbcTypesMap.put("LONGBLOB", integer1);
    mysqlToJdbcTypesMap.put("TINYTEXT", Integer.valueOf(12));
    Map<String, Integer> map1 = mysqlToJdbcTypesMap;
    Integer integer2 = Integer.valueOf(-1);
    map1.put("TEXT", integer2);
    mysqlToJdbcTypesMap.put("MEDIUMTEXT", integer2);
    mysqlToJdbcTypesMap.put("LONGTEXT", integer2);
    mysqlToJdbcTypesMap.put("ENUM", Integer.valueOf(mysqlToJavaType(247)));
    mysqlToJdbcTypesMap.put("SET", Integer.valueOf(mysqlToJavaType(248)));
    mysqlToJdbcTypesMap.put("GEOMETRY", Integer.valueOf(mysqlToJavaType(255)));
    mysqlToJdbcTypesMap.put("JSON", Integer.valueOf(mysqlToJavaType(245)));
  }
  
  public static final void appendJdbcTypeMappingQuery(StringBuilder paramStringBuilder, String paramString) {
    paramStringBuilder.append("CASE ");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.putAll(mysqlToJdbcTypesMap);
    hashMap.put("BINARY", Integer.valueOf(-2));
    hashMap.put("VARBINARY", Integer.valueOf(-3));
    for (String str : hashMap.keySet()) {
      paramStringBuilder.append(" WHEN UPPER(");
      paramStringBuilder.append(paramString);
      paramStringBuilder.append(")='");
      paramStringBuilder.append(str);
      paramStringBuilder.append("' THEN ");
      paramStringBuilder.append(hashMap.get(str));
      if (str.equalsIgnoreCase("DOUBLE") || str.equalsIgnoreCase("FLOAT") || str.equalsIgnoreCase("DECIMAL") || str.equalsIgnoreCase("NUMERIC")) {
        paramStringBuilder.append(" WHEN ");
        paramStringBuilder.append(paramString);
        paramStringBuilder.append("='");
        paramStringBuilder.append(str);
        paramStringBuilder.append(" UNSIGNED' THEN ");
        paramStringBuilder.append(hashMap.get(str));
      } 
    } 
    paramStringBuilder.append(" ELSE ");
    paramStringBuilder.append(1111);
    paramStringBuilder.append(" END ");
  }
  
  public static int mysqlToJavaType(int paramInt) {
    byte b2 = 12;
    byte b1 = b2;
    switch (paramInt) {
      default:
        b1 = b2;
        switch (paramInt) {
          default:
            b1 = b2;
            break;
          case 255:
            b1 = -2;
            break;
          case 250:
          case 251:
          case 252:
            b1 = -4;
            break;
          case 249:
            b1 = -3;
            break;
          case 245:
          case 247:
          case 248:
          case 254:
            b1 = 1;
            break;
          case 246:
            b1 = 3;
            break;
          case 253:
            break;
        } 
        break;
      case 16:
        b1 = -7;
        break;
      case 11:
        b1 = 92;
        break;
      case 10:
      case 13:
      case 14:
        b1 = 91;
        break;
      case 8:
        b1 = -5;
        break;
      case 7:
      case 12:
        b1 = 93;
        break;
      case 6:
        b1 = 0;
        break;
      case 5:
        b1 = 8;
        break;
      case 4:
        b1 = 7;
        break;
      case 3:
      case 9:
        b1 = 4;
        break;
      case 2:
        b1 = 5;
        break;
      case 1:
        b1 = -6;
        break;
      case 0:
      
      case 15:
        break;
    } 
    return b1;
  }
  
  public static int mysqlToJavaType(String paramString) {
    return paramString.equalsIgnoreCase("BIT") ? mysqlToJavaType(16) : (paramString.equalsIgnoreCase("TINYINT") ? mysqlToJavaType(1) : (paramString.equalsIgnoreCase("SMALLINT") ? mysqlToJavaType(2) : (paramString.equalsIgnoreCase("MEDIUMINT") ? mysqlToJavaType(9) : ((paramString.equalsIgnoreCase("INT") || paramString.equalsIgnoreCase("INTEGER")) ? mysqlToJavaType(3) : (paramString.equalsIgnoreCase("BIGINT") ? mysqlToJavaType(8) : (paramString.equalsIgnoreCase("INT24") ? mysqlToJavaType(9) : (paramString.equalsIgnoreCase("REAL") ? mysqlToJavaType(5) : (paramString.equalsIgnoreCase("FLOAT") ? mysqlToJavaType(4) : (paramString.equalsIgnoreCase("DECIMAL") ? mysqlToJavaType(0) : (paramString.equalsIgnoreCase("NUMERIC") ? mysqlToJavaType(0) : (paramString.equalsIgnoreCase("DOUBLE") ? mysqlToJavaType(5) : (paramString.equalsIgnoreCase("CHAR") ? mysqlToJavaType(254) : (paramString.equalsIgnoreCase("VARCHAR") ? mysqlToJavaType(253) : (paramString.equalsIgnoreCase("DATE") ? mysqlToJavaType(10) : (paramString.equalsIgnoreCase("TIME") ? mysqlToJavaType(11) : (paramString.equalsIgnoreCase("YEAR") ? mysqlToJavaType(13) : (paramString.equalsIgnoreCase("TIMESTAMP") ? mysqlToJavaType(7) : (paramString.equalsIgnoreCase("DATETIME") ? mysqlToJavaType(12) : (paramString.equalsIgnoreCase("TINYBLOB") ? -2 : (paramString.equalsIgnoreCase("BLOB") ? -4 : (paramString.equalsIgnoreCase("MEDIUMBLOB") ? -4 : (paramString.equalsIgnoreCase("LONGBLOB") ? -4 : (paramString.equalsIgnoreCase("TINYTEXT") ? 12 : (paramString.equalsIgnoreCase("TEXT") ? -1 : (paramString.equalsIgnoreCase("MEDIUMTEXT") ? -1 : (paramString.equalsIgnoreCase("LONGTEXT") ? -1 : (paramString.equalsIgnoreCase("ENUM") ? mysqlToJavaType(247) : (paramString.equalsIgnoreCase("SET") ? mysqlToJavaType(248) : (paramString.equalsIgnoreCase("GEOMETRY") ? mysqlToJavaType(255) : (paramString.equalsIgnoreCase("BINARY") ? -2 : (paramString.equalsIgnoreCase("VARBINARY") ? -3 : (paramString.equalsIgnoreCase("BIT") ? mysqlToJavaType(16) : (paramString.equalsIgnoreCase("JSON") ? mysqlToJavaType(245) : 1111)))))))))))))))))))))))))))))))));
  }
  
  public static String typeToName(int paramInt) {
    if (paramInt != 245) {
      StringBuilder stringBuilder;
      switch (paramInt) {
        default:
          switch (paramInt) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append(" Unknown MySQL Type # ");
              stringBuilder.append(paramInt);
              return stringBuilder.toString();
            case 255:
              return "FIELD_TYPE_GEOMETRY";
            case 254:
              return "FIELD_TYPE_STRING";
            case 253:
              return "FIELD_TYPE_VAR_STRING";
            case 252:
              return "FIELD_TYPE_BLOB";
            case 251:
              return "FIELD_TYPE_LONG_BLOB";
            case 250:
              return "FIELD_TYPE_MEDIUM_BLOB";
            case 249:
              return "FIELD_TYPE_TINY_BLOB";
            case 248:
              return "FIELD_TYPE_SET";
            case 247:
              break;
          } 
          return "FIELD_TYPE_ENUM";
        case 16:
          return "FIELD_TYPE_BIT";
        case 15:
          return "FIELD_TYPE_VARCHAR";
        case 14:
          return "FIELD_TYPE_NEWDATE";
        case 13:
          return "FIELD_TYPE_YEAR";
        case 12:
          return "FIELD_TYPE_DATETIME";
        case 11:
          return "FIELD_TYPE_TIME";
        case 10:
          return "FIELD_TYPE_DATE";
        case 9:
          return "FIELD_TYPE_INT24";
        case 8:
          return "FIELD_TYPE_LONGLONG";
        case 7:
          return "FIELD_TYPE_TIMESTAMP";
        case 6:
          return "FIELD_TYPE_NULL";
        case 5:
          return "FIELD_TYPE_DOUBLE";
        case 4:
          return "FIELD_TYPE_FLOAT";
        case 3:
          return "FIELD_TYPE_LONG";
        case 2:
          return "FIELD_TYPE_SHORT";
        case 1:
          return "FIELD_TYPE_TINY";
        case 0:
          break;
      } 
      return "FIELD_TYPE_DECIMAL";
    } 
    return "FIELD_TYPE_JSON";
  }
}
