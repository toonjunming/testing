package com.mysql.jdbc;

import com.mysql.jdbc.exceptions.MySQLDataException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
import com.mysql.jdbc.exceptions.MySQLQueryInterruptedException;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;
import com.mysql.jdbc.exceptions.MySQLTransientConnectionException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

public class SQLError {
  private static final long DEFAULT_WAIT_TIMEOUT_SECONDS = 28800L;
  
  private static final int DUE_TO_TIMEOUT_FALSE = 0;
  
  private static final int DUE_TO_TIMEOUT_MAYBE = 2;
  
  private static final int DUE_TO_TIMEOUT_TRUE = 1;
  
  public static final int ER_WARNING_NOT_COMPLETE_ROLLBACK = 1196;
  
  private static final Constructor<?> JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR;
  
  public static final String SQL_STATE_ACTIVE_SQL_TRANSACTION = "25001";
  
  public static final String SQL_STATE_BASE_TABLE_NOT_FOUND = "S0002";
  
  public static final String SQL_STATE_BASE_TABLE_OR_VIEW_ALREADY_EXISTS = "S0001";
  
  public static final String SQL_STATE_BASE_TABLE_OR_VIEW_NOT_FOUND = "42S02";
  
  public static final String SQL_STATE_CARDINALITY_VIOLATION = "21000";
  
  public static final String SQL_STATE_CASE_NOT_FOUND_FOR_CASE_STATEMENT = "20000";
  
  public static final String SQL_STATE_CLI_SPECIFIC_CONDITION = "HY000";
  
  public static final String SQL_STATE_COLUMN_ALREADY_EXISTS = "S0021";
  
  public static final String SQL_STATE_COLUMN_NOT_FOUND = "S0022";
  
  public static final String SQL_STATE_COMMUNICATION_LINK_FAILURE = "08S01";
  
  public static final String SQL_STATE_CONNECTION_FAILURE = "08006";
  
  public static final String SQL_STATE_CONNECTION_IN_USE = "08002";
  
  public static final String SQL_STATE_CONNECTION_NOT_OPEN = "08003";
  
  public static final String SQL_STATE_CONNECTION_REJECTED = "08004";
  
  public static final String SQL_STATE_DATA_TRUNCATED = "01004";
  
  public static final String SQL_STATE_DATETIME_FIELD_OVERFLOW = "22008";
  
  public static final String SQL_STATE_DISCONNECT_ERROR = "01002";
  
  public static final String SQL_STATE_DIVISION_BY_ZERO = "22012";
  
  public static final String SQL_STATE_DRIVER_NOT_CAPABLE = "S1C00";
  
  public static final String SQL_STATE_ERROR_IN_ROW = "01S01";
  
  public static final String SQL_STATE_ER_BAD_FIELD_ERROR = "42S22";
  
  public static final String SQL_STATE_ER_DUP_FIELDNAME = "42S21";
  
  public static final String SQL_STATE_ER_NO_SUCH_INDEX = "42S12";
  
  public static final String SQL_STATE_ER_QUERY_INTERRUPTED = "70100";
  
  public static final String SQL_STATE_ER_TABLE_EXISTS_ERROR = "42S01";
  
  public static final String SQL_STATE_FEATURE_NOT_SUPPORTED = "0A000";
  
  public static final String SQL_STATE_GENERAL_ERROR = "S1000";
  
  public static final String SQL_STATE_ILLEGAL_ARGUMENT = "S1009";
  
  public static final String SQL_STATE_INDEX_ALREADY_EXISTS = "S0011";
  
  public static final String SQL_STATE_INDEX_NOT_FOUND = "S0012";
  
  public static final String SQL_STATE_INSERT_VALUE_LIST_NO_MATCH_COL_LIST = "21S01";
  
  public static final String SQL_STATE_INTEGRITY_CONSTRAINT_VIOLATION = "23000";
  
  public static final String SQL_STATE_INVALID_AUTH_SPEC = "28000";
  
  public static final String SQL_STATE_INVALID_CATALOG_NAME = "3D000";
  
  public static final String SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST = "22018";
  
  public static final String SQL_STATE_INVALID_COLUMN_NUMBER = "S1002";
  
  public static final String SQL_STATE_INVALID_CONDITION_NUMBER = "35000";
  
  public static final String SQL_STATE_INVALID_CONNECTION_ATTRIBUTE = "01S00";
  
  public static final String SQL_STATE_INVALID_CURSOR_STATE = "24000";
  
  public static final String SQL_STATE_INVALID_DATETIME_FORMAT = "22007";
  
  public static final String SQL_STATE_INVALID_LOGARITHM_ARGUMENT = "2201E";
  
  public static final String SQL_STATE_INVALID_TRANSACTION_STATE = "25000";
  
  public static final String SQL_STATE_INVALID_TRANSACTION_TERMINATION = "2D000";
  
  public static final String SQL_STATE_MEMORY_ALLOCATION_ERROR = "HY001";
  
  public static final String SQL_STATE_MEMORY_ALLOCATION_FAILURE = "S1001";
  
  public static final String SQL_STATE_MORE_THAN_ONE_ROW_UPDATED_OR_DELETED = "01S04";
  
  public static final String SQL_STATE_NO_DATA = "02000";
  
  public static final String SQL_STATE_NO_DEFAULT_FOR_COLUMN = "S0023";
  
  public static final String SQL_STATE_NO_ROWS_UPDATED_OR_DELETED = "01S03";
  
  public static final String SQL_STATE_NULL_VALUE_NOT_ALLOWED = "22004";
  
  public static final String SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE = "22003";
  
  public static final String SQL_STATE_PRIVILEGE_NOT_REVOKED = "01006";
  
  public static final String SQL_STATE_READ_ONLY_SQL_TRANSACTION = "25006";
  
  public static final String SQL_STATE_RESIGNAL_WHEN_HANDLER_NOT_ACTIVE = "0K000";
  
  public static final String SQL_STATE_ROLLBACK_SERIALIZATION_FAILURE = "40001";
  
  public static final String SQL_STATE_SRE_FUNCTION_EXECUTED_NO_RETURN_STATEMENT = "2F005";
  
  public static final String SQL_STATE_SRE_PROHIBITED_SQL_STATEMENT_ATTEMPTED = "2F003";
  
  public static final String SQL_STATE_STACKED_DIAGNOSTICS_ACCESSED_WITHOUT_ACTIVE_HANDLER = "0Z002";
  
  public static final String SQL_STATE_STRING_DATA_RIGHT_TRUNCATION = "22001";
  
  public static final String SQL_STATE_SYNTAX_ERROR = "42000";
  
  public static final String SQL_STATE_TIMEOUT_EXPIRED = "S1T00";
  
  public static final String SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN = "08007";
  
  public static final String SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE = "08001";
  
  public static final String SQL_STATE_WARNING = "01000";
  
  public static final String SQL_STATE_WRONG_NO_OF_PARAMETERS = "07001";
  
  public static final String SQL_STATE_XAER_DUPID = "XAE08";
  
  public static final String SQL_STATE_XAER_INVAL = "XAE05";
  
  public static final String SQL_STATE_XAER_NOTA = "XAE04";
  
  public static final String SQL_STATE_XAER_OUTSIDE = "XAE09";
  
  public static final String SQL_STATE_XAER_RMFAIL = "XAE07";
  
  public static final String SQL_STATE_XA_RBDEADLOCK = "XA102";
  
  public static final String SQL_STATE_XA_RBROLLBACK = "XA100";
  
  public static final String SQL_STATE_XA_RBTIMEOUT = "XA106";
  
  public static final String SQL_STATE_XA_RMERR = "XAE03";
  
  private static Map<Integer, String> mysqlToSql99State;
  
  private static Map<Integer, String> mysqlToSqlState;
  
  private static Map<String, String> sqlStateMessages;
  
  static {
    if (Util.isJdbc4()) {
      try {
        Class<?> clazz = Class.forName("com.mysql.jdbc.exceptions.jdbc4.CommunicationsException");
        Class<long> clazz1 = long.class;
        JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR = clazz.getConstructor(new Class[] { MySQLConnection.class, clazz1, clazz1, Exception.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR = null;
    } 
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
    sqlStateMessages = (Map)hashMap2;
    hashMap2.put("01002", Messages.getString("SQLError.35"));
    sqlStateMessages.put("01004", Messages.getString("SQLError.36"));
    sqlStateMessages.put("01006", Messages.getString("SQLError.37"));
    sqlStateMessages.put("01S00", Messages.getString("SQLError.38"));
    sqlStateMessages.put("01S01", Messages.getString("SQLError.39"));
    sqlStateMessages.put("01S03", Messages.getString("SQLError.40"));
    sqlStateMessages.put("01S04", Messages.getString("SQLError.41"));
    sqlStateMessages.put("07001", Messages.getString("SQLError.42"));
    sqlStateMessages.put("08001", Messages.getString("SQLError.43"));
    sqlStateMessages.put("08002", Messages.getString("SQLError.44"));
    sqlStateMessages.put("08003", Messages.getString("SQLError.45"));
    sqlStateMessages.put("08004", Messages.getString("SQLError.46"));
    sqlStateMessages.put("08007", Messages.getString("SQLError.47"));
    sqlStateMessages.put("08S01", Messages.getString("SQLError.48"));
    sqlStateMessages.put("21S01", Messages.getString("SQLError.49"));
    sqlStateMessages.put("22003", Messages.getString("SQLError.50"));
    sqlStateMessages.put("22008", Messages.getString("SQLError.51"));
    sqlStateMessages.put("22012", Messages.getString("SQLError.52"));
    sqlStateMessages.put("40001", Messages.getString("SQLError.53"));
    sqlStateMessages.put("28000", Messages.getString("SQLError.54"));
    sqlStateMessages.put("42000", Messages.getString("SQLError.55"));
    sqlStateMessages.put("42S02", Messages.getString("SQLError.56"));
    sqlStateMessages.put("S0001", Messages.getString("SQLError.57"));
    sqlStateMessages.put("S0002", Messages.getString("SQLError.58"));
    sqlStateMessages.put("S0011", Messages.getString("SQLError.59"));
    sqlStateMessages.put("S0012", Messages.getString("SQLError.60"));
    sqlStateMessages.put("S0021", Messages.getString("SQLError.61"));
    sqlStateMessages.put("S0022", Messages.getString("SQLError.62"));
    sqlStateMessages.put("S0023", Messages.getString("SQLError.63"));
    sqlStateMessages.put("S1000", Messages.getString("SQLError.64"));
    sqlStateMessages.put("S1001", Messages.getString("SQLError.65"));
    sqlStateMessages.put("S1002", Messages.getString("SQLError.66"));
    sqlStateMessages.put("S1009", Messages.getString("SQLError.67"));
    sqlStateMessages.put("S1C00", Messages.getString("SQLError.68"));
    sqlStateMessages.put("S1T00", Messages.getString("SQLError.69"));
    Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>();
    mysqlToSqlState = (Map)hashtable;
    hashtable.put(Integer.valueOf(1249), "01000");
    mysqlToSqlState.put(Integer.valueOf(1261), "01000");
    mysqlToSqlState.put(Integer.valueOf(1262), "01000");
    mysqlToSqlState.put(Integer.valueOf(1265), "01000");
    mysqlToSqlState.put(Integer.valueOf(1311), "01000");
    mysqlToSqlState.put(Integer.valueOf(1642), "01000");
    mysqlToSqlState.put(Integer.valueOf(1040), "08004");
    mysqlToSqlState.put(Integer.valueOf(1251), "08004");
    mysqlToSqlState.put(Integer.valueOf(1042), "08004");
    mysqlToSqlState.put(Integer.valueOf(1043), "08004");
    mysqlToSqlState.put(Integer.valueOf(1129), "08004");
    mysqlToSqlState.put(Integer.valueOf(1130), "08004");
    mysqlToSqlState.put(Integer.valueOf(1047), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1053), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1080), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1081), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1152), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1153), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1154), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1155), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1156), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1157), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1158), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1159), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1160), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1161), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1184), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1189), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1190), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1218), "08S01");
    mysqlToSqlState.put(Integer.valueOf(1312), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1314), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1335), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1336), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1415), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1845), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1846), "0A000");
    mysqlToSqlState.put(Integer.valueOf(1044), "42000");
    mysqlToSqlState.put(Integer.valueOf(1049), "42000");
    mysqlToSqlState.put(Integer.valueOf(1055), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1056), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1057), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1059), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1060), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1061), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1062), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1063), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1064), "42000");
    mysqlToSqlState.put(Integer.valueOf(1065), "42000");
    mysqlToSqlState.put(Integer.valueOf(1066), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1067), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1068), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1069), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1070), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1071), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1072), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1073), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1074), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1075), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1082), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1083), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1084), "S1009");
    mysqlToSqlState.put(Integer.valueOf(1090), "42000");
    mysqlToSqlState.put(Integer.valueOf(1091), "42000");
    mysqlToSqlState.put(Integer.valueOf(1101), "42000");
    mysqlToSqlState.put(Integer.valueOf(1102), "42000");
    mysqlToSqlState.put(Integer.valueOf(1103), "42000");
    mysqlToSqlState.put(Integer.valueOf(1104), "42000");
    mysqlToSqlState.put(Integer.valueOf(1106), "42000");
    mysqlToSqlState.put(Integer.valueOf(1107), "42000");
    mysqlToSqlState.put(Integer.valueOf(1110), "42000");
    mysqlToSqlState.put(Integer.valueOf(1112), "42000");
    mysqlToSqlState.put(Integer.valueOf(1113), "42000");
    mysqlToSqlState.put(Integer.valueOf(1115), "42000");
    mysqlToSqlState.put(Integer.valueOf(1118), "42000");
    mysqlToSqlState.put(Integer.valueOf(1120), "42000");
    mysqlToSqlState.put(Integer.valueOf(1121), "42000");
    mysqlToSqlState.put(Integer.valueOf(1131), "42000");
    mysqlToSqlState.put(Integer.valueOf(1132), "42000");
    mysqlToSqlState.put(Integer.valueOf(1133), "42000");
    mysqlToSqlState.put(Integer.valueOf(1139), "42000");
    mysqlToSqlState.put(Integer.valueOf(1140), "42000");
    mysqlToSqlState.put(Integer.valueOf(1141), "42000");
    mysqlToSqlState.put(Integer.valueOf(1142), "42000");
    mysqlToSqlState.put(Integer.valueOf(1143), "42000");
    mysqlToSqlState.put(Integer.valueOf(1144), "42000");
    mysqlToSqlState.put(Integer.valueOf(1145), "42000");
    mysqlToSqlState.put(Integer.valueOf(1147), "42000");
    mysqlToSqlState.put(Integer.valueOf(1148), "42000");
    mysqlToSqlState.put(Integer.valueOf(1149), "42000");
    mysqlToSqlState.put(Integer.valueOf(1162), "42000");
    mysqlToSqlState.put(Integer.valueOf(1163), "42000");
    mysqlToSqlState.put(Integer.valueOf(1164), "42000");
    mysqlToSqlState.put(Integer.valueOf(1166), "42000");
    mysqlToSqlState.put(Integer.valueOf(1167), "42000");
    mysqlToSqlState.put(Integer.valueOf(1170), "42000");
    mysqlToSqlState.put(Integer.valueOf(1171), "42000");
    mysqlToSqlState.put(Integer.valueOf(1172), "42000");
    mysqlToSqlState.put(Integer.valueOf(1173), "42000");
    mysqlToSqlState.put(Integer.valueOf(1176), "42000");
    mysqlToSqlState.put(Integer.valueOf(1177), "42000");
    mysqlToSqlState.put(Integer.valueOf(1178), "42000");
    mysqlToSqlState.put(Integer.valueOf(1203), "42000");
    mysqlToSqlState.put(Integer.valueOf(1211), "42000");
    mysqlToSqlState.put(Integer.valueOf(1226), "42000");
    mysqlToSqlState.put(Integer.valueOf(1227), "42000");
    mysqlToSqlState.put(Integer.valueOf(1230), "42000");
    mysqlToSqlState.put(Integer.valueOf(1231), "42000");
    mysqlToSqlState.put(Integer.valueOf(1232), "42000");
    mysqlToSqlState.put(Integer.valueOf(1234), "42000");
    mysqlToSqlState.put(Integer.valueOf(1235), "42000");
    mysqlToSqlState.put(Integer.valueOf(1239), "42000");
    mysqlToSqlState.put(Integer.valueOf(1248), "42000");
    mysqlToSqlState.put(Integer.valueOf(1250), "42000");
    mysqlToSqlState.put(Integer.valueOf(1252), "42000");
    mysqlToSqlState.put(Integer.valueOf(1253), "42000");
    mysqlToSqlState.put(Integer.valueOf(1280), "42000");
    mysqlToSqlState.put(Integer.valueOf(1281), "42000");
    mysqlToSqlState.put(Integer.valueOf(1286), "42000");
    mysqlToSqlState.put(Integer.valueOf(1304), "42000");
    mysqlToSqlState.put(Integer.valueOf(1305), "42000");
    mysqlToSqlState.put(Integer.valueOf(1308), "42000");
    mysqlToSqlState.put(Integer.valueOf(1309), "42000");
    mysqlToSqlState.put(Integer.valueOf(1310), "42000");
    mysqlToSqlState.put(Integer.valueOf(1313), "42000");
    mysqlToSqlState.put(Integer.valueOf(1315), "42000");
    mysqlToSqlState.put(Integer.valueOf(1316), "42000");
    mysqlToSqlState.put(Integer.valueOf(1318), "42000");
    mysqlToSqlState.put(Integer.valueOf(1319), "42000");
    mysqlToSqlState.put(Integer.valueOf(1320), "42000");
    mysqlToSqlState.put(Integer.valueOf(1322), "42000");
    mysqlToSqlState.put(Integer.valueOf(1323), "42000");
    mysqlToSqlState.put(Integer.valueOf(1324), "42000");
    mysqlToSqlState.put(Integer.valueOf(1327), "42000");
    mysqlToSqlState.put(Integer.valueOf(1330), "42000");
    mysqlToSqlState.put(Integer.valueOf(1331), "42000");
    mysqlToSqlState.put(Integer.valueOf(1332), "42000");
    mysqlToSqlState.put(Integer.valueOf(1333), "42000");
    mysqlToSqlState.put(Integer.valueOf(1337), "42000");
    mysqlToSqlState.put(Integer.valueOf(1338), "42000");
    mysqlToSqlState.put(Integer.valueOf(1370), "42000");
    mysqlToSqlState.put(Integer.valueOf(1403), "42000");
    mysqlToSqlState.put(Integer.valueOf(1407), "42000");
    mysqlToSqlState.put(Integer.valueOf(1410), "42000");
    mysqlToSqlState.put(Integer.valueOf(1413), "42000");
    mysqlToSqlState.put(Integer.valueOf(1414), "42000");
    mysqlToSqlState.put(Integer.valueOf(1425), "42000");
    mysqlToSqlState.put(Integer.valueOf(1426), "42000");
    mysqlToSqlState.put(Integer.valueOf(1427), "42000");
    mysqlToSqlState.put(Integer.valueOf(1437), "42000");
    mysqlToSqlState.put(Integer.valueOf(1439), "42000");
    mysqlToSqlState.put(Integer.valueOf(1453), "42000");
    mysqlToSqlState.put(Integer.valueOf(1458), "42000");
    mysqlToSqlState.put(Integer.valueOf(1460), "42000");
    mysqlToSqlState.put(Integer.valueOf(1461), "42000");
    mysqlToSqlState.put(Integer.valueOf(1463), "42000");
    mysqlToSqlState.put(Integer.valueOf(1582), "42000");
    mysqlToSqlState.put(Integer.valueOf(1583), "42000");
    mysqlToSqlState.put(Integer.valueOf(1584), "42000");
    mysqlToSqlState.put(Integer.valueOf(1630), "42000");
    mysqlToSqlState.put(Integer.valueOf(1641), "42000");
    mysqlToSqlState.put(Integer.valueOf(1687), "42000");
    mysqlToSqlState.put(Integer.valueOf(1701), "42000");
    mysqlToSqlState.put(Integer.valueOf(1222), "21000");
    mysqlToSqlState.put(Integer.valueOf(1241), "21000");
    mysqlToSqlState.put(Integer.valueOf(1242), "21000");
    mysqlToSqlState.put(Integer.valueOf(1022), "23000");
    mysqlToSqlState.put(Integer.valueOf(1048), "23000");
    mysqlToSqlState.put(Integer.valueOf(1052), "23000");
    mysqlToSqlState.put(Integer.valueOf(1169), "23000");
    mysqlToSqlState.put(Integer.valueOf(1216), "23000");
    mysqlToSqlState.put(Integer.valueOf(1217), "23000");
    mysqlToSqlState.put(Integer.valueOf(1451), "23000");
    mysqlToSqlState.put(Integer.valueOf(1452), "23000");
    mysqlToSqlState.put(Integer.valueOf(1557), "23000");
    mysqlToSqlState.put(Integer.valueOf(1586), "23000");
    mysqlToSqlState.put(Integer.valueOf(1761), "23000");
    mysqlToSqlState.put(Integer.valueOf(1762), "23000");
    mysqlToSqlState.put(Integer.valueOf(1859), "23000");
    mysqlToSqlState.put(Integer.valueOf(1406), "22001");
    mysqlToSqlState.put(Integer.valueOf(1264), "01000");
    mysqlToSqlState.put(Integer.valueOf(1416), "22003");
    mysqlToSqlState.put(Integer.valueOf(1690), "22003");
    mysqlToSqlState.put(Integer.valueOf(1292), "22007");
    mysqlToSqlState.put(Integer.valueOf(1367), "22007");
    mysqlToSqlState.put(Integer.valueOf(1441), "22008");
    mysqlToSqlState.put(Integer.valueOf(1365), "22012");
    mysqlToSqlState.put(Integer.valueOf(1325), "24000");
    mysqlToSqlState.put(Integer.valueOf(1326), "24000");
    mysqlToSqlState.put(Integer.valueOf(1179), "25000");
    mysqlToSqlState.put(Integer.valueOf(1207), "25000");
    mysqlToSqlState.put(Integer.valueOf(1045), "28000");
    mysqlToSqlState.put(Integer.valueOf(1698), "28000");
    mysqlToSqlState.put(Integer.valueOf(1873), "28000");
    mysqlToSqlState.put(Integer.valueOf(1758), "35000");
    mysqlToSqlState.put(Integer.valueOf(1046), "3D000");
    mysqlToSqlState.put(Integer.valueOf(1058), "21S01");
    mysqlToSqlState.put(Integer.valueOf(1136), "21S01");
    mysqlToSqlState.put(Integer.valueOf(1050), "42S01");
    mysqlToSqlState.put(Integer.valueOf(1051), "42S02");
    mysqlToSqlState.put(Integer.valueOf(1109), "42S02");
    mysqlToSqlState.put(Integer.valueOf(1146), "42S02");
    mysqlToSqlState.put(Integer.valueOf(1054), "S0022");
    mysqlToSqlState.put(Integer.valueOf(1247), "42S22");
    mysqlToSqlState.put(Integer.valueOf(1037), "S1001");
    mysqlToSqlState.put(Integer.valueOf(1038), "S1001");
    mysqlToSqlState.put(Integer.valueOf(1205), "40001");
    mysqlToSqlState.put(Integer.valueOf(1213), "40001");
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
    mysqlToSql99State = (Map)hashMap1;
    hashMap1.put(Integer.valueOf(1249), "01000");
    mysqlToSql99State.put(Integer.valueOf(1261), "01000");
    mysqlToSql99State.put(Integer.valueOf(1262), "01000");
    mysqlToSql99State.put(Integer.valueOf(1265), "01000");
    mysqlToSql99State.put(Integer.valueOf(1263), "01000");
    mysqlToSql99State.put(Integer.valueOf(1264), "01000");
    mysqlToSql99State.put(Integer.valueOf(1311), "01000");
    mysqlToSql99State.put(Integer.valueOf(1642), "01000");
    mysqlToSql99State.put(Integer.valueOf(1329), "02000");
    mysqlToSql99State.put(Integer.valueOf(1643), "02000");
    mysqlToSql99State.put(Integer.valueOf(1040), "08004");
    mysqlToSql99State.put(Integer.valueOf(1251), "08004");
    mysqlToSql99State.put(Integer.valueOf(1042), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1043), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1047), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1053), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1080), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1081), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1152), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1153), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1154), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1155), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1156), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1157), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1158), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1159), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1160), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1161), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1184), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1189), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1190), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1218), "08S01");
    mysqlToSql99State.put(Integer.valueOf(1312), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1314), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1335), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1336), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1415), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1845), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1846), "0A000");
    mysqlToSql99State.put(Integer.valueOf(1044), "42000");
    mysqlToSql99State.put(Integer.valueOf(1049), "42000");
    mysqlToSql99State.put(Integer.valueOf(1055), "42000");
    mysqlToSql99State.put(Integer.valueOf(1056), "42000");
    mysqlToSql99State.put(Integer.valueOf(1057), "42000");
    mysqlToSql99State.put(Integer.valueOf(1059), "42000");
    mysqlToSql99State.put(Integer.valueOf(1061), "42000");
    mysqlToSql99State.put(Integer.valueOf(1063), "42000");
    mysqlToSql99State.put(Integer.valueOf(1064), "42000");
    mysqlToSql99State.put(Integer.valueOf(1065), "42000");
    mysqlToSql99State.put(Integer.valueOf(1066), "42000");
    mysqlToSql99State.put(Integer.valueOf(1067), "42000");
    mysqlToSql99State.put(Integer.valueOf(1068), "42000");
    mysqlToSql99State.put(Integer.valueOf(1069), "42000");
    mysqlToSql99State.put(Integer.valueOf(1070), "42000");
    mysqlToSql99State.put(Integer.valueOf(1071), "42000");
    mysqlToSql99State.put(Integer.valueOf(1072), "42000");
    mysqlToSql99State.put(Integer.valueOf(1073), "42000");
    mysqlToSql99State.put(Integer.valueOf(1074), "42000");
    mysqlToSql99State.put(Integer.valueOf(1075), "42000");
    mysqlToSql99State.put(Integer.valueOf(1083), "42000");
    mysqlToSql99State.put(Integer.valueOf(1084), "42000");
    mysqlToSql99State.put(Integer.valueOf(1090), "42000");
    mysqlToSql99State.put(Integer.valueOf(1091), "42000");
    mysqlToSql99State.put(Integer.valueOf(1101), "42000");
    mysqlToSql99State.put(Integer.valueOf(1102), "42000");
    mysqlToSql99State.put(Integer.valueOf(1103), "42000");
    mysqlToSql99State.put(Integer.valueOf(1104), "42000");
    mysqlToSql99State.put(Integer.valueOf(1106), "42000");
    mysqlToSql99State.put(Integer.valueOf(1107), "42000");
    mysqlToSql99State.put(Integer.valueOf(1110), "42000");
    mysqlToSql99State.put(Integer.valueOf(1112), "42000");
    mysqlToSql99State.put(Integer.valueOf(1113), "42000");
    mysqlToSql99State.put(Integer.valueOf(1115), "42000");
    mysqlToSql99State.put(Integer.valueOf(1118), "42000");
    mysqlToSql99State.put(Integer.valueOf(1120), "42000");
    mysqlToSql99State.put(Integer.valueOf(1121), "42000");
    mysqlToSql99State.put(Integer.valueOf(1131), "42000");
    mysqlToSql99State.put(Integer.valueOf(1132), "42000");
    mysqlToSql99State.put(Integer.valueOf(1133), "42000");
    mysqlToSql99State.put(Integer.valueOf(1139), "42000");
    mysqlToSql99State.put(Integer.valueOf(1140), "42000");
    mysqlToSql99State.put(Integer.valueOf(1141), "42000");
    mysqlToSql99State.put(Integer.valueOf(1142), "42000");
    mysqlToSql99State.put(Integer.valueOf(1143), "42000");
    mysqlToSql99State.put(Integer.valueOf(1144), "42000");
    mysqlToSql99State.put(Integer.valueOf(1145), "42000");
    mysqlToSql99State.put(Integer.valueOf(1147), "42000");
    mysqlToSql99State.put(Integer.valueOf(1148), "42000");
    mysqlToSql99State.put(Integer.valueOf(1149), "42000");
    mysqlToSql99State.put(Integer.valueOf(1162), "42000");
    mysqlToSql99State.put(Integer.valueOf(1163), "42000");
    mysqlToSql99State.put(Integer.valueOf(1164), "42000");
    mysqlToSql99State.put(Integer.valueOf(1166), "42000");
    mysqlToSql99State.put(Integer.valueOf(1167), "42000");
    mysqlToSql99State.put(Integer.valueOf(1170), "42000");
    mysqlToSql99State.put(Integer.valueOf(1171), "42000");
    mysqlToSql99State.put(Integer.valueOf(1172), "42000");
    mysqlToSql99State.put(Integer.valueOf(1173), "42000");
    mysqlToSql99State.put(Integer.valueOf(1176), "42000");
    mysqlToSql99State.put(Integer.valueOf(1177), "42000");
    mysqlToSql99State.put(Integer.valueOf(1178), "42000");
    mysqlToSql99State.put(Integer.valueOf(1203), "42000");
    mysqlToSql99State.put(Integer.valueOf(1211), "42000");
    mysqlToSql99State.put(Integer.valueOf(1226), "42000");
    mysqlToSql99State.put(Integer.valueOf(1227), "42000");
    mysqlToSql99State.put(Integer.valueOf(1230), "42000");
    mysqlToSql99State.put(Integer.valueOf(1231), "42000");
    mysqlToSql99State.put(Integer.valueOf(1232), "42000");
    mysqlToSql99State.put(Integer.valueOf(1234), "42000");
    mysqlToSql99State.put(Integer.valueOf(1235), "42000");
    mysqlToSql99State.put(Integer.valueOf(1239), "42000");
    mysqlToSql99State.put(Integer.valueOf(1248), "42000");
    mysqlToSql99State.put(Integer.valueOf(1250), "42000");
    mysqlToSql99State.put(Integer.valueOf(1252), "42000");
    mysqlToSql99State.put(Integer.valueOf(1253), "42000");
    mysqlToSql99State.put(Integer.valueOf(1280), "42000");
    mysqlToSql99State.put(Integer.valueOf(1281), "42000");
    mysqlToSql99State.put(Integer.valueOf(1286), "42000");
    mysqlToSql99State.put(Integer.valueOf(1304), "42000");
    mysqlToSql99State.put(Integer.valueOf(1305), "42000");
    mysqlToSql99State.put(Integer.valueOf(1308), "42000");
    mysqlToSql99State.put(Integer.valueOf(1309), "42000");
    mysqlToSql99State.put(Integer.valueOf(1310), "42000");
    mysqlToSql99State.put(Integer.valueOf(1313), "42000");
    mysqlToSql99State.put(Integer.valueOf(1315), "42000");
    mysqlToSql99State.put(Integer.valueOf(1316), "42000");
    mysqlToSql99State.put(Integer.valueOf(1318), "42000");
    mysqlToSql99State.put(Integer.valueOf(1319), "42000");
    mysqlToSql99State.put(Integer.valueOf(1320), "42000");
    mysqlToSql99State.put(Integer.valueOf(1322), "42000");
    mysqlToSql99State.put(Integer.valueOf(1323), "42000");
    mysqlToSql99State.put(Integer.valueOf(1324), "42000");
    mysqlToSql99State.put(Integer.valueOf(1327), "42000");
    mysqlToSql99State.put(Integer.valueOf(1330), "42000");
    mysqlToSql99State.put(Integer.valueOf(1331), "42000");
    mysqlToSql99State.put(Integer.valueOf(1332), "42000");
    mysqlToSql99State.put(Integer.valueOf(1333), "42000");
    mysqlToSql99State.put(Integer.valueOf(1337), "42000");
    mysqlToSql99State.put(Integer.valueOf(1338), "42000");
    mysqlToSql99State.put(Integer.valueOf(1370), "42000");
    mysqlToSql99State.put(Integer.valueOf(1403), "42000");
    mysqlToSql99State.put(Integer.valueOf(1407), "42000");
    mysqlToSql99State.put(Integer.valueOf(1410), "42000");
    mysqlToSql99State.put(Integer.valueOf(1413), "42000");
    mysqlToSql99State.put(Integer.valueOf(1414), "42000");
    mysqlToSql99State.put(Integer.valueOf(1425), "42000");
    mysqlToSql99State.put(Integer.valueOf(1426), "42000");
    mysqlToSql99State.put(Integer.valueOf(1427), "42000");
    mysqlToSql99State.put(Integer.valueOf(1437), "42000");
    mysqlToSql99State.put(Integer.valueOf(1439), "42000");
    mysqlToSql99State.put(Integer.valueOf(1453), "42000");
    mysqlToSql99State.put(Integer.valueOf(1458), "42000");
    mysqlToSql99State.put(Integer.valueOf(1460), "42000");
    mysqlToSql99State.put(Integer.valueOf(1461), "42000");
    mysqlToSql99State.put(Integer.valueOf(1463), "42000");
    mysqlToSql99State.put(Integer.valueOf(1582), "42000");
    mysqlToSql99State.put(Integer.valueOf(1583), "42000");
    mysqlToSql99State.put(Integer.valueOf(1584), "42000");
    mysqlToSql99State.put(Integer.valueOf(1630), "42000");
    mysqlToSql99State.put(Integer.valueOf(1641), "42000");
    mysqlToSql99State.put(Integer.valueOf(1687), "42000");
    mysqlToSql99State.put(Integer.valueOf(1701), "42000");
    mysqlToSql99State.put(Integer.valueOf(1222), "21000");
    mysqlToSql99State.put(Integer.valueOf(1241), "21000");
    mysqlToSql99State.put(Integer.valueOf(1242), "21000");
    mysqlToSql99State.put(Integer.valueOf(1022), "23000");
    mysqlToSql99State.put(Integer.valueOf(1048), "23000");
    mysqlToSql99State.put(Integer.valueOf(1052), "23000");
    mysqlToSql99State.put(Integer.valueOf(1062), "23000");
    mysqlToSql99State.put(Integer.valueOf(1169), "23000");
    mysqlToSql99State.put(Integer.valueOf(1216), "23000");
    mysqlToSql99State.put(Integer.valueOf(1217), "23000");
    mysqlToSql99State.put(Integer.valueOf(1451), "23000");
    mysqlToSql99State.put(Integer.valueOf(1452), "23000");
    mysqlToSql99State.put(Integer.valueOf(1557), "23000");
    mysqlToSql99State.put(Integer.valueOf(1586), "23000");
    mysqlToSql99State.put(Integer.valueOf(1761), "23000");
    mysqlToSql99State.put(Integer.valueOf(1762), "23000");
    mysqlToSql99State.put(Integer.valueOf(1859), "23000");
    mysqlToSql99State.put(Integer.valueOf(1406), "22001");
    mysqlToSql99State.put(Integer.valueOf(1416), "22003");
    mysqlToSql99State.put(Integer.valueOf(1690), "22003");
    mysqlToSql99State.put(Integer.valueOf(1292), "22007");
    mysqlToSql99State.put(Integer.valueOf(1367), "22007");
    mysqlToSql99State.put(Integer.valueOf(1441), "22008");
    mysqlToSql99State.put(Integer.valueOf(1365), "22012");
    mysqlToSql99State.put(Integer.valueOf(1325), "24000");
    mysqlToSql99State.put(Integer.valueOf(1326), "24000");
    mysqlToSql99State.put(Integer.valueOf(1179), "25000");
    mysqlToSql99State.put(Integer.valueOf(1207), "25000");
    mysqlToSql99State.put(Integer.valueOf(1045), "28000");
    mysqlToSql99State.put(Integer.valueOf(1698), "28000");
    mysqlToSql99State.put(Integer.valueOf(1873), "28000");
    mysqlToSql99State.put(Integer.valueOf(1758), "35000");
    mysqlToSql99State.put(Integer.valueOf(1046), "3D000");
    mysqlToSql99State.put(Integer.valueOf(1645), "0K000");
    mysqlToSql99State.put(Integer.valueOf(1887), "0Z002");
    mysqlToSql99State.put(Integer.valueOf(1339), "20000");
    mysqlToSql99State.put(Integer.valueOf(1058), "21S01");
    mysqlToSql99State.put(Integer.valueOf(1136), "21S01");
    mysqlToSql99State.put(Integer.valueOf(1138), "42000");
    mysqlToSql99State.put(Integer.valueOf(1903), "2201E");
    mysqlToSql99State.put(Integer.valueOf(1568), "25001");
    mysqlToSql99State.put(Integer.valueOf(1792), "25006");
    mysqlToSql99State.put(Integer.valueOf(1303), "2F003");
    mysqlToSql99State.put(Integer.valueOf(1321), "2F005");
    mysqlToSql99State.put(Integer.valueOf(1050), "42S01");
    mysqlToSql99State.put(Integer.valueOf(1051), "42S02");
    mysqlToSql99State.put(Integer.valueOf(1109), "42S02");
    mysqlToSql99State.put(Integer.valueOf(1146), "42S02");
    mysqlToSql99State.put(Integer.valueOf(1082), "42S12");
    mysqlToSql99State.put(Integer.valueOf(1060), "42S21");
    mysqlToSql99State.put(Integer.valueOf(1054), "42S22");
    mysqlToSql99State.put(Integer.valueOf(1247), "42S22");
    mysqlToSql99State.put(Integer.valueOf(1317), "70100");
    mysqlToSql99State.put(Integer.valueOf(1037), "HY001");
    mysqlToSql99State.put(Integer.valueOf(1038), "HY001");
    mysqlToSql99State.put(Integer.valueOf(1402), "XA100");
    mysqlToSql99State.put(Integer.valueOf(1614), "XA102");
    mysqlToSql99State.put(Integer.valueOf(1613), "XA106");
    mysqlToSql99State.put(Integer.valueOf(1401), "XAE03");
    mysqlToSql99State.put(Integer.valueOf(1397), "XAE04");
    mysqlToSql99State.put(Integer.valueOf(1398), "XAE05");
    mysqlToSql99State.put(Integer.valueOf(1399), "XAE07");
    mysqlToSql99State.put(Integer.valueOf(1440), "XAE08");
    mysqlToSql99State.put(Integer.valueOf(1400), "XAE09");
    mysqlToSql99State.put(Integer.valueOf(1205), "40001");
    mysqlToSql99State.put(Integer.valueOf(1213), "40001");
  }
  
  public static SQLWarning convertShowWarningsToSQLWarnings(Connection paramConnection) throws SQLException {
    return convertShowWarningsToSQLWarnings(paramConnection, 0, false);
  }
  
  public static SQLWarning convertShowWarningsToSQLWarnings(Connection paramConnection, int paramInt, boolean paramBoolean) throws SQLException {
    Statement statement;
    ResultSet resultSet;
    Object object1 = null;
    Object object2 = null;
    if (paramInt < 100) {
      try {
        Statement statement1;
        resultSet = (ResultSet)paramConnection.createStatement();
      } finally {
        paramConnection = null;
        statement = null;
      } 
    } else {
      statement = paramConnection.createStatement(1003, 1007);
      Statement statement1 = statement;
      statement.setFetchSize(-2147483648);
      statement1 = statement;
    } 
    sQLException1 = (SQLException)object1;
    if (resultSet != null)
      try {
        resultSet.close();
        sQLException1 = (SQLException)object1;
      } catch (SQLException sQLException1) {} 
    sQLException2 = sQLException1;
    if (statement != null)
      try {
        statement.close();
        sQLException2 = sQLException1;
      } catch (SQLException sQLException2) {} 
    if (sQLException2 != null)
      throw sQLException2; 
    throw paramConnection;
  }
  
  public static SQLException createBatchUpdateException(SQLException paramSQLException, long[] paramArrayOflong, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    if (Util.isJdbc42()) {
      Class<int> clazz = int.class;
      String str2 = paramSQLException.getMessage();
      String str1 = paramSQLException.getSQLState();
      int i = paramSQLException.getErrorCode();
      paramSQLException = (SQLException)Util.getInstance("java.sql.BatchUpdateException", new Class[] { String.class, String.class, clazz, long[].class, Throwable.class }, new Object[] { str2, str1, Integer.valueOf(i), paramArrayOflong, paramSQLException }, paramExceptionInterceptor);
    } else {
      BatchUpdateException batchUpdateException = new BatchUpdateException(paramSQLException.getMessage(), paramSQLException.getSQLState(), paramSQLException.getErrorCode(), Util.truncateAndConvertToInt(paramArrayOflong));
      batchUpdateException.initCause(paramSQLException);
      paramSQLException = batchUpdateException;
    } 
    return runThroughExceptionInterceptor(paramExceptionInterceptor, paramSQLException, null);
  }
  
  public static SQLException createCommunicationsException(MySQLConnection paramMySQLConnection, long paramLong1, long paramLong2, Exception paramException, ExceptionInterceptor paramExceptionInterceptor) {
    if (!Util.isJdbc4()) {
      paramException = new CommunicationsException(paramMySQLConnection, paramLong1, paramLong2, paramException);
    } else {
      try {
        paramException = (SQLException)Util.handleNewInstance(JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR, new Object[] { paramMySQLConnection, Long.valueOf(paramLong1), Long.valueOf(paramLong2), paramException }, paramExceptionInterceptor);
        return runThroughExceptionInterceptor(paramExceptionInterceptor, (SQLException)paramException, paramMySQLConnection);
      } catch (SQLException null) {
        return null;
      } 
    } 
    return runThroughExceptionInterceptor(paramExceptionInterceptor, (SQLException)paramException, paramMySQLConnection);
  }
  
  public static String createLinkFailureMessageBasedOnHeuristics(MySQLConnection paramMySQLConnection, long paramLong1, long paramLong2, Exception paramException) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 63
    //   4: aload_0
    //   5: invokeinterface getInteractiveClient : ()Z
    //   10: istore #13
    //   12: iload #13
    //   14: ifeq -> 31
    //   17: aload_0
    //   18: ldc_w 'interactive_timeout'
    //   21: invokeinterface getServerVariable : (Ljava/lang/String;)Ljava/lang/String;
    //   26: astore #14
    //   28: goto -> 42
    //   31: aload_0
    //   32: ldc_w 'wait_timeout'
    //   35: invokeinterface getServerVariable : (Ljava/lang/String;)Ljava/lang/String;
    //   40: astore #14
    //   42: aload #14
    //   44: ifnull -> 57
    //   47: aload #14
    //   49: invokestatic parseLong : (Ljava/lang/String;)J
    //   52: lstore #7
    //   54: goto -> 69
    //   57: lconst_0
    //   58: lstore #7
    //   60: goto -> 69
    //   63: lconst_0
    //   64: lstore #7
    //   66: iconst_0
    //   67: istore #13
    //   69: new java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial <init> : ()V
    //   76: astore #15
    //   78: invokestatic currentTimeMillis : ()J
    //   81: lstore #9
    //   83: lload_1
    //   84: lconst_0
    //   85: lcmp
    //   86: ifne -> 95
    //   89: lload #9
    //   91: lstore_1
    //   92: goto -> 95
    //   95: lload #9
    //   97: lload_1
    //   98: lsub
    //   99: lstore_1
    //   100: lload_1
    //   101: ldc2_w 1000
    //   104: ldiv
    //   105: lstore #11
    //   107: lload #9
    //   109: lload_3
    //   110: lsub
    //   111: lstore #9
    //   113: lload #7
    //   115: lconst_0
    //   116: lcmp
    //   117: ifeq -> 187
    //   120: lload #11
    //   122: lload #7
    //   124: lcmp
    //   125: ifle -> 259
    //   128: new java/lang/StringBuilder
    //   131: dup
    //   132: invokespecial <init> : ()V
    //   135: astore #14
    //   137: aload #14
    //   139: ldc_w 'CommunicationsException.2'
    //   142: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: iload #13
    //   151: ifne -> 169
    //   154: aload #14
    //   156: ldc_w 'CommunicationsException.3'
    //   159: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: goto -> 181
    //   169: aload #14
    //   171: ldc_w 'CommunicationsException.4'
    //   174: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: iconst_1
    //   182: istore #6
    //   184: goto -> 265
    //   187: lload #11
    //   189: ldc2_w 28800
    //   192: lcmp
    //   193: ifle -> 259
    //   196: new java/lang/StringBuilder
    //   199: dup
    //   200: invokespecial <init> : ()V
    //   203: astore #14
    //   205: aload #14
    //   207: ldc_w 'CommunicationsException.5'
    //   210: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload #14
    //   219: ldc_w 'CommunicationsException.6'
    //   222: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   225: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   228: pop
    //   229: aload #14
    //   231: ldc_w 'CommunicationsException.7'
    //   234: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   237: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   240: pop
    //   241: aload #14
    //   243: ldc_w 'CommunicationsException.8'
    //   246: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: iconst_2
    //   254: istore #6
    //   256: goto -> 265
    //   259: aconst_null
    //   260: astore #14
    //   262: iconst_0
    //   263: istore #6
    //   265: iload #6
    //   267: iconst_1
    //   268: if_icmpeq -> 339
    //   271: iload #6
    //   273: iconst_2
    //   274: if_icmpne -> 280
    //   277: goto -> 339
    //   280: aload #5
    //   282: instanceof java/net/BindException
    //   285: ifeq -> 451
    //   288: aload_0
    //   289: invokeinterface getLocalSocketAddress : ()Ljava/lang/String;
    //   294: ifnull -> 324
    //   297: aload_0
    //   298: invokeinterface getLocalSocketAddress : ()Ljava/lang/String;
    //   303: invokestatic interfaceExists : (Ljava/lang/String;)Z
    //   306: ifne -> 324
    //   309: aload #15
    //   311: ldc_w 'CommunicationsException.LocalSocketAddressNotAvailable'
    //   314: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   317: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: goto -> 451
    //   324: aload #15
    //   326: ldc_w 'CommunicationsException.TooManyClientConnections'
    //   329: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: pop
    //   336: goto -> 451
    //   339: lload_3
    //   340: lconst_0
    //   341: lcmp
    //   342: ifeq -> 379
    //   345: aload #15
    //   347: ldc_w 'CommunicationsException.ServerPacketTimingInfo'
    //   350: iconst_2
    //   351: anewarray java/lang/Object
    //   354: dup
    //   355: iconst_0
    //   356: lload #9
    //   358: invokestatic valueOf : (J)Ljava/lang/Long;
    //   361: aastore
    //   362: dup
    //   363: iconst_1
    //   364: lload_1
    //   365: invokestatic valueOf : (J)Ljava/lang/Long;
    //   368: aastore
    //   369: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   372: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   375: pop
    //   376: goto -> 402
    //   379: aload #15
    //   381: ldc_w 'CommunicationsException.ServerPacketTimingInfoNoRecv'
    //   384: iconst_1
    //   385: anewarray java/lang/Object
    //   388: dup
    //   389: iconst_0
    //   390: lload_1
    //   391: invokestatic valueOf : (J)Ljava/lang/Long;
    //   394: aastore
    //   395: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   398: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   401: pop
    //   402: aload #14
    //   404: ifnull -> 415
    //   407: aload #15
    //   409: aload #14
    //   411: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;
    //   414: pop
    //   415: aload #15
    //   417: ldc_w 'CommunicationsException.11'
    //   420: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   423: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   426: pop
    //   427: aload #15
    //   429: ldc_w 'CommunicationsException.12'
    //   432: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   435: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   438: pop
    //   439: aload #15
    //   441: ldc_w 'CommunicationsException.13'
    //   444: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   447: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   450: pop
    //   451: aload #15
    //   453: invokevirtual length : ()I
    //   456: ifne -> 565
    //   459: aload #15
    //   461: ldc_w 'CommunicationsException.20'
    //   464: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   467: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   470: pop
    //   471: aload_0
    //   472: ifnull -> 565
    //   475: aload_0
    //   476: invokeinterface getMaintainTimeStats : ()Z
    //   481: ifeq -> 565
    //   484: aload_0
    //   485: invokeinterface getParanoid : ()Z
    //   490: ifne -> 565
    //   493: aload #15
    //   495: ldc_w '\\n\\n'
    //   498: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   501: pop
    //   502: lload_3
    //   503: lconst_0
    //   504: lcmp
    //   505: ifeq -> 542
    //   508: aload #15
    //   510: ldc_w 'CommunicationsException.ServerPacketTimingInfo'
    //   513: iconst_2
    //   514: anewarray java/lang/Object
    //   517: dup
    //   518: iconst_0
    //   519: lload #9
    //   521: invokestatic valueOf : (J)Ljava/lang/Long;
    //   524: aastore
    //   525: dup
    //   526: iconst_1
    //   527: lload_1
    //   528: invokestatic valueOf : (J)Ljava/lang/Long;
    //   531: aastore
    //   532: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   535: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   538: pop
    //   539: goto -> 565
    //   542: aload #15
    //   544: ldc_w 'CommunicationsException.ServerPacketTimingInfoNoRecv'
    //   547: iconst_1
    //   548: anewarray java/lang/Object
    //   551: dup
    //   552: iconst_0
    //   553: lload_1
    //   554: invokestatic valueOf : (J)Ljava/lang/Long;
    //   557: aastore
    //   558: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   561: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   564: pop
    //   565: aload #15
    //   567: invokevirtual toString : ()Ljava/lang/String;
    //   570: areturn
    //   571: astore #14
    //   573: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   47	54	571	java/lang/NumberFormatException
  }
  
  public static SQLException createSQLException(String paramString, ExceptionInterceptor paramExceptionInterceptor) {
    return createSQLException(paramString, paramExceptionInterceptor, (Connection)null);
  }
  
  public static SQLException createSQLException(String paramString, ExceptionInterceptor paramExceptionInterceptor, Connection paramConnection) {
    return runThroughExceptionInterceptor(paramExceptionInterceptor, new SQLException(paramString), paramConnection);
  }
  
  public static SQLException createSQLException(String paramString1, String paramString2, int paramInt, ExceptionInterceptor paramExceptionInterceptor) {
    return createSQLException(paramString1, paramString2, paramInt, false, paramExceptionInterceptor);
  }
  
  public static SQLException createSQLException(String paramString1, String paramString2, int paramInt, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) {
    return createSQLException(paramString1, paramString2, paramInt, paramBoolean, paramExceptionInterceptor, null);
  }
  
  public static SQLException createSQLException(String paramString1, String paramString2, int paramInt, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor, Connection paramConnection) {
    StringBuilder stringBuilder;
    if (paramString2 != null)
      try {
        if (paramString2.startsWith("08")) {
          if (paramBoolean) {
            if (!Util.isJdbc4()) {
              MySQLTransientConnectionException mySQLTransientConnectionException = new MySQLTransientConnectionException();
              this(paramString1, paramString2, paramInt);
              null = mySQLTransientConnectionException;
            } else {
              null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLTransientConnectionException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
            } 
          } else if (!Util.isJdbc4()) {
            null = new MySQLNonTransientConnectionException((String)null, paramString2, paramInt);
          } else {
            null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
          } 
        } else if (paramString2.startsWith("22")) {
          if (!Util.isJdbc4()) {
            null = new MySQLDataException((String)null, paramString2, paramInt);
          } else {
            null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLDataException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
          } 
        } else if (paramString2.startsWith("23")) {
          if (!Util.isJdbc4()) {
            null = new MySQLIntegrityConstraintViolationException((String)null, paramString2, paramInt);
          } else {
            null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
          } 
        } else if (paramString2.startsWith("42")) {
          if (!Util.isJdbc4()) {
            null = new MySQLSyntaxErrorException((String)null, paramString2, paramInt);
          } else {
            null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
          } 
        } else if (paramString2.startsWith("40")) {
          if (!Util.isJdbc4()) {
            null = new MySQLTransactionRollbackException((String)null, paramString2, paramInt);
          } else {
            null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
          } 
        } else if (paramString2.startsWith("70100")) {
          if (!Util.isJdbc4()) {
            null = new MySQLQueryInterruptedException((String)null, paramString2, paramInt);
          } else {
            null = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLQueryInterruptedException", new Class[] { String.class, String.class, int.class }, new Object[] { null, paramString2, Integer.valueOf(paramInt) }, paramExceptionInterceptor);
          } 
        } else {
          null = new SQLException((String)null, paramString2, paramInt);
        } 
        return runThroughExceptionInterceptor(paramExceptionInterceptor, null, paramConnection);
      } catch (SQLException null) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to create correct SQLException class instance, error class/codes may be incorrect. Reason: ");
        stringBuilder.append(Util.stackTraceToString(null));
        return runThroughExceptionInterceptor(paramExceptionInterceptor, new SQLException(stringBuilder.toString(), "S1000"), paramConnection);
      }  
    null = new SQLException((String)null, (String)stringBuilder, paramInt);
    return runThroughExceptionInterceptor(paramExceptionInterceptor, null, paramConnection);
  }
  
  public static SQLException createSQLException(String paramString1, String paramString2, ExceptionInterceptor paramExceptionInterceptor) {
    return createSQLException(paramString1, paramString2, 0, paramExceptionInterceptor);
  }
  
  public static SQLException createSQLException(String paramString1, String paramString2, Throwable paramThrowable, ExceptionInterceptor paramExceptionInterceptor) {
    return createSQLException(paramString1, paramString2, paramThrowable, paramExceptionInterceptor, (Connection)null);
  }
  
  public static SQLException createSQLException(String paramString1, String paramString2, Throwable paramThrowable, ExceptionInterceptor paramExceptionInterceptor, Connection paramConnection) {
    SQLException sQLException = createSQLException(paramString1, paramString2, (ExceptionInterceptor)null);
    if (sQLException.getCause() == null)
      sQLException.initCause(paramThrowable); 
    return runThroughExceptionInterceptor(paramExceptionInterceptor, sQLException, paramConnection);
  }
  
  public static SQLException createSQLFeatureNotSupportedException() throws SQLException {
    SQLException sQLException;
    if (Util.isJdbc4()) {
      sQLException = (SQLException)Util.getInstance("java.sql.SQLFeatureNotSupportedException", null, null, null);
    } else {
      sQLException = new NotImplemented();
    } 
    return sQLException;
  }
  
  public static SQLException createSQLFeatureNotSupportedException(String paramString1, String paramString2, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    SQLException sQLException;
    if (Util.isJdbc4()) {
      sQLException = (SQLException)Util.getInstance("java.sql.SQLFeatureNotSupportedException", new Class[] { String.class, String.class }, new Object[] { paramString1, paramString2 }, paramExceptionInterceptor);
    } else {
      sQLException = new NotImplemented();
    } 
    return runThroughExceptionInterceptor(paramExceptionInterceptor, sQLException, null);
  }
  
  public static void dumpSqlStatesMappingsAsXml() throws Exception {
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    for (Integer integer : mysqlToSql99State.keySet())
      treeMap.put(integer, integer); 
    for (Integer integer : mysqlToSqlState.keySet())
      treeMap.put(integer, integer); 
    Field[] arrayOfField = MysqlErrorNumbers.class.getDeclaredFields();
    for (byte b = 0; b < arrayOfField.length; b++) {
      String str = arrayOfField[b].getName();
      if (str.startsWith("ER_"))
        hashMap.put(arrayOfField[b].get(null), str); 
    } 
    System.out.println("<ErrorMappings>");
    for (Integer integer : treeMap.keySet()) {
      String str2 = mysqlToSql99(integer.intValue());
      String str3 = mysqlToXOpen(integer.intValue());
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("   <ErrorMapping mysqlErrorNumber=\"");
      stringBuilder.append(integer);
      stringBuilder.append("\" mysqlErrorName=\"");
      stringBuilder.append((String)hashMap.get(integer));
      stringBuilder.append("\" legacySqlState=\"");
      String str1 = str3;
      if (str3 == null)
        str1 = ""; 
      stringBuilder.append(str1);
      stringBuilder.append("\" sql92SqlState=\"");
      str1 = str2;
      if (str2 == null)
        str1 = ""; 
      stringBuilder.append(str1);
      stringBuilder.append("\"/>");
      printStream.println(stringBuilder.toString());
    } 
    System.out.println("</ErrorMappings>");
  }
  
  public static String get(String paramString) {
    return sqlStateMessages.get(paramString);
  }
  
  private static String mysqlToSql99(int paramInt) {
    Integer integer = Integer.valueOf(paramInt);
    return mysqlToSql99State.containsKey(integer) ? mysqlToSql99State.get(integer) : "HY000";
  }
  
  public static String mysqlToSqlState(int paramInt, boolean paramBoolean) {
    return paramBoolean ? mysqlToSql99(paramInt) : mysqlToXOpen(paramInt);
  }
  
  private static String mysqlToXOpen(int paramInt) {
    Integer integer = Integer.valueOf(paramInt);
    return mysqlToSqlState.containsKey(integer) ? mysqlToSqlState.get(integer) : "S1000";
  }
  
  private static SQLException runThroughExceptionInterceptor(ExceptionInterceptor paramExceptionInterceptor, SQLException paramSQLException, Connection paramConnection) {
    if (paramExceptionInterceptor != null) {
      SQLException sQLException = paramExceptionInterceptor.interceptException(paramSQLException, paramConnection);
      if (sQLException != null)
        return sQLException; 
    } 
    return paramSQLException;
  }
}
