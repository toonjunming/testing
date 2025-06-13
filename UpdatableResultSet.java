package com.mysql.jdbc;

import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UpdatableResultSet extends ResultSetImpl {
  public static final byte[] STREAM_DATA_MARKER = StringUtils.getBytes("** STREAM DATA **");
  
  public SingleByteCharsetConverter charConverter;
  
  private String charEncoding;
  
  private Map<String, Map<String, Map<String, Integer>>> databasesUsedToTablesUsed = null;
  
  private byte[][] defaultColumnValue;
  
  private String deleteSQL = null;
  
  private PreparedStatement deleter = null;
  
  private boolean initializedCharConverter = false;
  
  private String insertSQL = null;
  
  public PreparedStatement inserter = null;
  
  private boolean isUpdatable = false;
  
  private String notUpdatableReason = null;
  
  private boolean populateInserterWithDefaultValues = false;
  
  private List<Integer> primaryKeyIndicies = null;
  
  private String qualifiedAndQuotedTableName;
  
  private String quotedIdChar = null;
  
  private String refreshSQL = null;
  
  private PreparedStatement refresher;
  
  private ResultSetRow savedCurrentRow;
  
  private String updateSQL = null;
  
  public PreparedStatement updater = null;
  
  public UpdatableResultSet(String paramString, Field[] paramArrayOfField, RowData paramRowData, MySQLConnection paramMySQLConnection, StatementImpl paramStatementImpl) throws SQLException {
    super(paramString, paramArrayOfField, paramRowData, paramMySQLConnection, paramStatementImpl);
    checkUpdatability();
    this.populateInserterWithDefaultValues = this.connection.getPopulateInsertRowWithDefaultValues();
  }
  
  private void extractDefaultValues() throws SQLException {
    DatabaseMetaData databaseMetaData = this.connection.getMetaData();
    this.defaultColumnValue = new byte[this.fields.length][];
    Iterator<Map.Entry> iterator = this.databasesUsedToTablesUsed.entrySet().iterator();
    String str = null;
    label31: while (iterator.hasNext()) {
      Iterator<Map.Entry> iterator1 = ((Map)((Map.Entry)iterator.next()).getValue()).entrySet().iterator();
      String str1 = str;
      while (true) {
        str = str1;
        if (iterator1.hasNext()) {
          Map.Entry entry = iterator1.next();
          str = (String)entry.getKey();
          Map map = (Map)entry.getValue();
          try {
            ResultSet resultSet = databaseMetaData.getColumns(this.catalog, null, str, "%");
            while (true) {
              ResultSet resultSet1 = resultSet;
              if (resultSet.next()) {
                resultSet1 = resultSet;
                String str2 = resultSet.getString("COLUMN_NAME");
                resultSet1 = resultSet;
                byte[] arrayOfByte = resultSet.getBytes("COLUMN_DEF");
                resultSet1 = resultSet;
                if (map.containsKey(str2)) {
                  resultSet1 = resultSet;
                  int i = ((Integer)map.get(str2)).intValue();
                  resultSet1 = resultSet;
                  this.defaultColumnValue[i] = arrayOfByte;
                } 
                continue;
              } 
              resultSet1 = resultSet;
              break;
            } 
            if (resultSet != null) {
              resultSet.close();
              str1 = null;
            } 
          } finally {
            if (str1 != null)
              str1.close(); 
          } 
          continue;
        } 
        continue label31;
      } 
    } 
  }
  
  private SingleByteCharsetConverter getCharConverter() throws SQLException {
    if (!this.initializedCharConverter) {
      this.initializedCharConverter = true;
      if (this.connection.getUseUnicode()) {
        this.charEncoding = this.connection.getEncoding();
        this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
      } 
    } 
    return this.charConverter;
  }
  
  private Map<String, Integer> getColumnsToIndexMapForTableAndDB(String paramString1, String paramString2) {
    Map<String, Object> map3 = (Map)this.databasesUsedToTablesUsed.get(paramString1);
    Map<String, Object> map2 = map3;
    if (map3 == null) {
      if (this.connection.lowerCaseTableNames()) {
        map2 = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
      } else {
        map2 = new TreeMap<String, Object>();
      } 
      this.databasesUsedToTablesUsed.put(paramString1, map2);
    } 
    map3 = (Map<String, Object>)map2.get(paramString2);
    Map<String, Object> map1 = map3;
    if (map3 == null) {
      map1 = new HashMap<String, Object>();
      map2.put(paramString2, map1);
    } 
    return (Map)map1;
  }
  
  private String getQuotedIdChar() throws SQLException {
    if (this.quotedIdChar == null)
      if (this.connection.supportsQuotedIdentifiers()) {
        this.quotedIdChar = this.connection.getMetaData().getIdentifierQuoteString();
      } else {
        this.quotedIdChar = "";
      }  
    return this.quotedIdChar;
  }
  
  private void refreshRow(PreparedStatement paramPreparedStatement, ResultSetRow paramResultSetRow) throws SQLException {
    byte[] arrayOfByte;
    if (this.refresher == null) {
      if (this.refreshSQL == null)
        generateStatements(); 
      PreparedStatement preparedStatement = (PreparedStatement)this.connection.clientPrepareStatement(this.refreshSQL);
      this.refresher = preparedStatement;
      Field[] arrayOfField = this.fields;
      preparedStatement.parameterMetaData = new MysqlParameterMetadata(arrayOfField, arrayOfField.length, getExceptionInterceptor());
    } 
    this.refresher.clearParameters();
    int j = this.primaryKeyIndicies.size();
    int i = 0;
    if (j == 1) {
      int k = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
      if (!this.doingUpdates && !this.onInsertRow) {
        arrayOfByte = paramResultSetRow.getColumnValue(k);
      } else {
        byte[] arrayOfByte1 = arrayOfByte.getBytesRepresentation(k);
        if (arrayOfByte.isNull(k) || arrayOfByte1.length == 0) {
          arrayOfByte = paramResultSetRow.getColumnValue(k);
        } else {
          arrayOfByte = stripBinaryPrefix(arrayOfByte1);
        } 
      } 
      if (this.fields[k].getvalueNeedsQuoting()) {
        this.refresher.setBytesNoEscape(1, arrayOfByte);
      } else {
        this.refresher.setBytesNoEscapeNoQuotes(1, arrayOfByte);
      } 
    } else {
      byte b = 0;
      while (b < j) {
        byte[] arrayOfByte1;
        int k = ((Integer)this.primaryKeyIndicies.get(b)).intValue();
        if (!this.doingUpdates && !this.onInsertRow) {
          arrayOfByte1 = paramResultSetRow.getColumnValue(k);
        } else {
          arrayOfByte1 = arrayOfByte.getBytesRepresentation(k);
          if (arrayOfByte.isNull(k) || arrayOfByte1.length == 0) {
            arrayOfByte1 = paramResultSetRow.getColumnValue(k);
          } else {
            arrayOfByte1 = stripBinaryPrefix(arrayOfByte1);
          } 
        } 
        PreparedStatement preparedStatement = this.refresher;
        preparedStatement.setBytesNoEscape(++b, arrayOfByte1);
      } 
    } 
    try {
      ResultSet resultSet = this.refresher.executeQuery();
    } finally {
      arrayOfByte = null;
    } 
    if (paramResultSetRow != null)
      try {
        paramResultSetRow.close();
      } catch (SQLException sQLException) {} 
    throw arrayOfByte;
  }
  
  private void resetInserter() throws SQLException {
    this.inserter.clearParameters();
    byte b = 0;
    while (b < this.fields.length) {
      PreparedStatement preparedStatement = this.inserter;
      preparedStatement.setNull(++b, 0);
    } 
  }
  
  private void setParamValue(PreparedStatement paramPreparedStatement, int paramInt1, ResultSetRow paramResultSetRow, int paramInt2, Field paramField) throws SQLException {
    byte[] arrayOfByte = paramResultSetRow.getColumnValue(paramInt2);
    if (arrayOfByte == null) {
      paramPreparedStatement.setNull(paramInt1, 0);
      return;
    } 
    int i = paramField.getSQLType();
    if (i != -6)
      if (i != -5) {
        if (i != 12) {
          if (i != 16) {
            switch (i) {
              default:
                switch (i) {
                  default:
                    paramPreparedStatement.setBytes(paramInt1, arrayOfByte);
                    return;
                  case 93:
                    paramPreparedStatement.setTimestampInternal(paramInt1, paramResultSetRow.getTimestampFast(paramInt2, this.fastDefaultCal, this.connection.getServerTimezoneTZ(), false, this.connection, this, false, false), null, this.connection.getDefaultTimeZone(), false, paramField.getDecimals(), false);
                    return;
                  case 92:
                    paramPreparedStatement.setTime(paramInt1, paramResultSetRow.getTimeFast(paramInt2, this.fastDefaultCal, this.connection.getServerTimezoneTZ(), false, this.connection, this));
                    return;
                  case 91:
                    break;
                } 
                paramPreparedStatement.setDate(paramInt1, paramResultSetRow.getDateFast(paramInt2, this.connection, this, this.fastDefaultCal), this.fastDefaultCal);
                return;
              case 0:
                paramPreparedStatement.setNull(paramInt1, 0);
                return;
              case 6:
              case 7:
              case 8:
                paramPreparedStatement.setBytesNoEscapeNoQuotes(paramInt1, arrayOfByte);
                return;
              case -1:
              case 1:
              case 2:
              case 3:
                paramPreparedStatement.setString(paramInt1, paramResultSetRow.getString(paramInt2, this.fields[paramInt2].getEncoding(), this.connection));
                return;
              case 4:
              case 5:
                break;
            } 
          } else {
          
          } 
        } else {
        
        } 
      } else {
        paramPreparedStatement.setLong(paramInt1, paramResultSetRow.getLong(paramInt2));
        return;
      }  
    paramPreparedStatement.setInt(paramInt1, paramResultSetRow.getInt(paramInt2));
  }
  
  private byte[] stripBinaryPrefix(byte[] paramArrayOfbyte) {
    return StringUtils.stripEnclosure(paramArrayOfbyte, "_binary'", "'");
  }
  
  public boolean absolute(int paramInt) throws SQLException {
    return super.absolute(paramInt);
  }
  
  public void afterLast() throws SQLException {
    super.afterLast();
  }
  
  public void beforeFirst() throws SQLException {
    super.beforeFirst();
  }
  
  public void cancelRowUpdates() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.doingUpdates) {
        this.doingUpdates = false;
        this.updater.clearParameters();
      } 
      return;
    } 
  }
  
  public void checkRowPos() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow)
        super.checkRowPos(); 
      return;
    } 
  }
  
  public void checkUpdatability() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield fields : [Lcom/mysql/jdbc/Field;
    //   4: ifnonnull -> 8
    //   7: return
    //   8: aload_0
    //   9: getfield catalog : Ljava/lang/String;
    //   12: astore #5
    //   14: aload #5
    //   16: ifnull -> 27
    //   19: aload #5
    //   21: invokevirtual length : ()I
    //   24: ifne -> 57
    //   27: aload_0
    //   28: getfield fields : [Lcom/mysql/jdbc/Field;
    //   31: iconst_0
    //   32: aaload
    //   33: invokevirtual getDatabaseName : ()Ljava/lang/String;
    //   36: astore #5
    //   38: aload_0
    //   39: aload #5
    //   41: putfield catalog : Ljava/lang/String;
    //   44: aload #5
    //   46: ifnull -> 745
    //   49: aload #5
    //   51: invokevirtual length : ()I
    //   54: ifeq -> 745
    //   57: aload_0
    //   58: getfield fields : [Lcom/mysql/jdbc/Field;
    //   61: astore #5
    //   63: aload #5
    //   65: arraylength
    //   66: istore_1
    //   67: iload_1
    //   68: ifle -> 729
    //   71: aload #5
    //   73: iconst_0
    //   74: aaload
    //   75: invokevirtual getOriginalTableName : ()Ljava/lang/String;
    //   78: astore #7
    //   80: aload_0
    //   81: getfield fields : [Lcom/mysql/jdbc/Field;
    //   84: iconst_0
    //   85: aaload
    //   86: invokevirtual getDatabaseName : ()Ljava/lang/String;
    //   89: astore #5
    //   91: aload #7
    //   93: astore #6
    //   95: aload #7
    //   97: ifnonnull -> 117
    //   100: aload_0
    //   101: getfield fields : [Lcom/mysql/jdbc/Field;
    //   104: iconst_0
    //   105: aaload
    //   106: invokevirtual getTableName : ()Ljava/lang/String;
    //   109: astore #6
    //   111: aload_0
    //   112: getfield catalog : Ljava/lang/String;
    //   115: astore #5
    //   117: aload #6
    //   119: ifnull -> 146
    //   122: aload #6
    //   124: invokevirtual length : ()I
    //   127: ifne -> 146
    //   130: aload_0
    //   131: iconst_0
    //   132: putfield isUpdatable : Z
    //   135: aload_0
    //   136: ldc_w 'NotUpdatableReason.3'
    //   139: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   142: putfield notUpdatableReason : Ljava/lang/String;
    //   145: return
    //   146: aload_0
    //   147: getfield fields : [Lcom/mysql/jdbc/Field;
    //   150: iconst_0
    //   151: aaload
    //   152: invokevirtual isPrimaryKey : ()Z
    //   155: istore_1
    //   156: iconst_1
    //   157: istore_2
    //   158: aload_0
    //   159: getfield fields : [Lcom/mysql/jdbc/Field;
    //   162: astore #7
    //   164: iload_2
    //   165: aload #7
    //   167: arraylength
    //   168: if_icmpge -> 340
    //   171: aload #7
    //   173: iload_2
    //   174: aaload
    //   175: invokevirtual getOriginalTableName : ()Ljava/lang/String;
    //   178: astore #9
    //   180: aload_0
    //   181: getfield fields : [Lcom/mysql/jdbc/Field;
    //   184: iload_2
    //   185: aaload
    //   186: invokevirtual getDatabaseName : ()Ljava/lang/String;
    //   189: astore #8
    //   191: aload #9
    //   193: astore #7
    //   195: aload #9
    //   197: ifnonnull -> 217
    //   200: aload_0
    //   201: getfield fields : [Lcom/mysql/jdbc/Field;
    //   204: iload_2
    //   205: aaload
    //   206: invokevirtual getTableName : ()Ljava/lang/String;
    //   209: astore #7
    //   211: aload_0
    //   212: getfield catalog : Ljava/lang/String;
    //   215: astore #8
    //   217: aload #7
    //   219: ifnull -> 246
    //   222: aload #7
    //   224: invokevirtual length : ()I
    //   227: ifne -> 246
    //   230: aload_0
    //   231: iconst_0
    //   232: putfield isUpdatable : Z
    //   235: aload_0
    //   236: ldc_w 'NotUpdatableReason.3'
    //   239: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   242: putfield notUpdatableReason : Ljava/lang/String;
    //   245: return
    //   246: aload #6
    //   248: ifnull -> 324
    //   251: aload #7
    //   253: aload #6
    //   255: invokevirtual equals : (Ljava/lang/Object;)Z
    //   258: ifne -> 264
    //   261: goto -> 324
    //   264: aload #5
    //   266: ifnull -> 308
    //   269: aload #8
    //   271: aload #5
    //   273: invokevirtual equals : (Ljava/lang/Object;)Z
    //   276: ifne -> 282
    //   279: goto -> 308
    //   282: iload_1
    //   283: istore_3
    //   284: aload_0
    //   285: getfield fields : [Lcom/mysql/jdbc/Field;
    //   288: iload_2
    //   289: aaload
    //   290: invokevirtual isPrimaryKey : ()Z
    //   293: ifeq -> 300
    //   296: iload_1
    //   297: iconst_1
    //   298: iadd
    //   299: istore_3
    //   300: iinc #2, 1
    //   303: iload_3
    //   304: istore_1
    //   305: goto -> 158
    //   308: aload_0
    //   309: iconst_0
    //   310: putfield isUpdatable : Z
    //   313: aload_0
    //   314: ldc_w 'NotUpdatableReason.1'
    //   317: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   320: putfield notUpdatableReason : Ljava/lang/String;
    //   323: return
    //   324: aload_0
    //   325: iconst_0
    //   326: putfield isUpdatable : Z
    //   329: aload_0
    //   330: ldc_w 'NotUpdatableReason.0'
    //   333: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   336: putfield notUpdatableReason : Ljava/lang/String;
    //   339: return
    //   340: aload #6
    //   342: ifnull -> 713
    //   345: aload #6
    //   347: invokevirtual length : ()I
    //   350: ifne -> 356
    //   353: goto -> 713
    //   356: aload_0
    //   357: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   360: invokeinterface getStrictUpdates : ()Z
    //   365: istore #4
    //   367: aconst_null
    //   368: astore #7
    //   370: iload #4
    //   372: ifeq -> 682
    //   375: aload_0
    //   376: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   379: invokeinterface getMetaData : ()Ljava/sql/DatabaseMetaData;
    //   384: astore #9
    //   386: new java/util/HashMap
    //   389: astore #8
    //   391: aload #8
    //   393: invokespecial <init> : ()V
    //   396: aload #9
    //   398: aload #5
    //   400: aconst_null
    //   401: aload #6
    //   403: invokeinterface getPrimaryKeys : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
    //   408: astore #6
    //   410: aload #6
    //   412: invokeinterface next : ()Z
    //   417: ifeq -> 446
    //   420: aload #6
    //   422: iconst_4
    //   423: invokeinterface getString : (I)Ljava/lang/String;
    //   428: invokevirtual toUpperCase : ()Ljava/lang/String;
    //   431: astore #5
    //   433: aload #8
    //   435: aload #5
    //   437: aload #5
    //   439: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   442: pop
    //   443: goto -> 410
    //   446: aload #6
    //   448: ifnull -> 468
    //   451: aload #6
    //   453: invokeinterface close : ()V
    //   458: goto -> 468
    //   461: astore #5
    //   463: aload #5
    //   465: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   468: aload #8
    //   470: invokevirtual size : ()I
    //   473: istore_3
    //   474: iload_3
    //   475: ifne -> 494
    //   478: aload_0
    //   479: iconst_0
    //   480: putfield isUpdatable : Z
    //   483: aload_0
    //   484: ldc_w 'NotUpdatableReason.5'
    //   487: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   490: putfield notUpdatableReason : Ljava/lang/String;
    //   493: return
    //   494: iconst_0
    //   495: istore_2
    //   496: aload_0
    //   497: getfield fields : [Lcom/mysql/jdbc/Field;
    //   500: astore #5
    //   502: iload_2
    //   503: aload #5
    //   505: arraylength
    //   506: if_icmpge -> 599
    //   509: aload #5
    //   511: iload_2
    //   512: aaload
    //   513: invokevirtual isPrimaryKey : ()Z
    //   516: ifeq -> 593
    //   519: aload #8
    //   521: aload_0
    //   522: getfield fields : [Lcom/mysql/jdbc/Field;
    //   525: iload_2
    //   526: aaload
    //   527: invokevirtual getName : ()Ljava/lang/String;
    //   530: invokevirtual toUpperCase : ()Ljava/lang/String;
    //   533: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   536: ifnonnull -> 593
    //   539: aload_0
    //   540: getfield fields : [Lcom/mysql/jdbc/Field;
    //   543: iload_2
    //   544: aaload
    //   545: invokevirtual getOriginalName : ()Ljava/lang/String;
    //   548: astore #5
    //   550: aload #5
    //   552: ifnull -> 593
    //   555: aload #8
    //   557: aload #5
    //   559: invokevirtual toUpperCase : ()Ljava/lang/String;
    //   562: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   565: ifnonnull -> 593
    //   568: aload_0
    //   569: iconst_0
    //   570: putfield isUpdatable : Z
    //   573: aload_0
    //   574: ldc_w 'NotUpdatableReason.6'
    //   577: iconst_1
    //   578: anewarray java/lang/Object
    //   581: dup
    //   582: iconst_0
    //   583: aload #5
    //   585: aastore
    //   586: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   589: putfield notUpdatableReason : Ljava/lang/String;
    //   592: return
    //   593: iinc #2, 1
    //   596: goto -> 496
    //   599: aload #8
    //   601: invokevirtual isEmpty : ()Z
    //   604: istore #4
    //   606: aload_0
    //   607: iload #4
    //   609: putfield isUpdatable : Z
    //   612: iload #4
    //   614: ifne -> 682
    //   617: iload_3
    //   618: iconst_1
    //   619: if_icmple -> 635
    //   622: aload_0
    //   623: ldc_w 'NotUpdatableReason.7'
    //   626: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   629: putfield notUpdatableReason : Ljava/lang/String;
    //   632: goto -> 645
    //   635: aload_0
    //   636: ldc_w 'NotUpdatableReason.4'
    //   639: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   642: putfield notUpdatableReason : Ljava/lang/String;
    //   645: return
    //   646: astore #5
    //   648: goto -> 657
    //   651: astore #5
    //   653: aload #7
    //   655: astore #6
    //   657: aload #6
    //   659: ifnull -> 679
    //   662: aload #6
    //   664: invokeinterface close : ()V
    //   669: goto -> 679
    //   672: astore #6
    //   674: aload #6
    //   676: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
    //   679: aload #5
    //   681: athrow
    //   682: iload_1
    //   683: ifne -> 702
    //   686: aload_0
    //   687: iconst_0
    //   688: putfield isUpdatable : Z
    //   691: aload_0
    //   692: ldc_w 'NotUpdatableReason.4'
    //   695: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   698: putfield notUpdatableReason : Ljava/lang/String;
    //   701: return
    //   702: aload_0
    //   703: iconst_1
    //   704: putfield isUpdatable : Z
    //   707: aload_0
    //   708: aconst_null
    //   709: putfield notUpdatableReason : Ljava/lang/String;
    //   712: return
    //   713: aload_0
    //   714: iconst_0
    //   715: putfield isUpdatable : Z
    //   718: aload_0
    //   719: ldc_w 'NotUpdatableReason.2'
    //   722: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   725: putfield notUpdatableReason : Ljava/lang/String;
    //   728: return
    //   729: aload_0
    //   730: iconst_0
    //   731: putfield isUpdatable : Z
    //   734: aload_0
    //   735: ldc_w 'NotUpdatableReason.3'
    //   738: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   741: putfield notUpdatableReason : Ljava/lang/String;
    //   744: return
    //   745: ldc_w 'UpdatableResultSet.43'
    //   748: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   751: ldc_w 'S1009'
    //   754: aload_0
    //   755: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   758: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   761: athrow
    //   762: astore #5
    //   764: aload_0
    //   765: iconst_0
    //   766: putfield isUpdatable : Z
    //   769: aload_0
    //   770: aload #5
    //   772: invokevirtual getMessage : ()Ljava/lang/String;
    //   775: putfield notUpdatableReason : Ljava/lang/String;
    //   778: return
    // Exception table:
    //   from	to	target	type
    //   0	7	762	java/sql/SQLException
    //   8	14	762	java/sql/SQLException
    //   19	27	762	java/sql/SQLException
    //   27	44	762	java/sql/SQLException
    //   49	57	762	java/sql/SQLException
    //   57	67	762	java/sql/SQLException
    //   71	91	762	java/sql/SQLException
    //   100	117	762	java/sql/SQLException
    //   122	145	762	java/sql/SQLException
    //   146	156	762	java/sql/SQLException
    //   158	191	762	java/sql/SQLException
    //   200	217	762	java/sql/SQLException
    //   222	245	762	java/sql/SQLException
    //   251	261	762	java/sql/SQLException
    //   269	279	762	java/sql/SQLException
    //   284	296	762	java/sql/SQLException
    //   308	323	762	java/sql/SQLException
    //   324	339	762	java/sql/SQLException
    //   345	353	762	java/sql/SQLException
    //   356	367	762	java/sql/SQLException
    //   375	396	762	java/sql/SQLException
    //   396	410	651	finally
    //   410	443	646	finally
    //   451	458	461	java/lang/Exception
    //   463	468	762	java/sql/SQLException
    //   468	474	762	java/sql/SQLException
    //   478	493	762	java/sql/SQLException
    //   496	550	762	java/sql/SQLException
    //   555	592	762	java/sql/SQLException
    //   599	612	762	java/sql/SQLException
    //   622	632	762	java/sql/SQLException
    //   635	645	762	java/sql/SQLException
    //   662	669	672	java/lang/Exception
    //   674	679	762	java/sql/SQLException
    //   679	682	762	java/sql/SQLException
    //   686	701	762	java/sql/SQLException
    //   702	712	762	java/sql/SQLException
    //   713	728	762	java/sql/SQLException
    //   729	744	762	java/sql/SQLException
    //   745	762	762	java/sql/SQLException
  }
  
  public void deleteRow() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.isUpdatable) {
        if (!this.onInsertRow) {
          if (this.rowData.size() != 0) {
            if (!isBeforeFirst()) {
              if (!isAfterLast()) {
                if (this.deleter == null) {
                  if (this.deleteSQL == null)
                    generateStatements(); 
                  this.deleter = (PreparedStatement)this.connection.clientPrepareStatement(this.deleteSQL);
                } 
                this.deleter.clearParameters();
                int i = this.primaryKeyIndicies.size();
                byte b = 0;
                while (b < i) {
                  int j = ((Integer)this.primaryKeyIndicies.get(b)).intValue();
                  PreparedStatement preparedStatement = this.deleter;
                  setParamValue(preparedStatement, ++b, this.thisRow, j, this.fields[j]);
                } 
                this.deleter.executeUpdate();
                RowData rowData = this.rowData;
                rowData.removeRow(rowData.getCurrentRowNumber());
                previous();
                return;
              } 
              throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.4"), getExceptionInterceptor());
            } 
            throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.3"), getExceptionInterceptor());
          } 
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.2"), getExceptionInterceptor());
        } 
        throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.1"), getExceptionInterceptor());
      } 
      NotUpdatable notUpdatable = new NotUpdatable();
      this(this.notUpdatableReason);
      throw notUpdatable;
    } 
  }
  
  public boolean first() throws SQLException {
    return super.first();
  }
  
  public void generateStatements() throws SQLException {
    if (this.isUpdatable) {
      TreeMap<Object, Object> treeMap;
      String str1;
      String str2 = getQuotedIdChar();
      if (this.connection.lowerCaseTableNames()) {
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        TreeMap<String, Object> treeMap1 = new TreeMap<String, Object>(comparator);
        this.databasesUsedToTablesUsed = new TreeMap<String, Map<String, Map<String, Integer>>>(comparator);
      } else {
        treeMap = new TreeMap<Object, Object>();
        this.databasesUsedToTablesUsed = new TreeMap<String, Map<String, Map<String, Integer>>>();
      } 
      this.primaryKeyIndicies = new ArrayList<Integer>();
      StringBuilder stringBuilder5 = new StringBuilder();
      StringBuilder stringBuilder3 = new StringBuilder();
      StringBuilder stringBuilder4 = new StringBuilder();
      StringBuilder stringBuilder2 = new StringBuilder();
      StringBuilder stringBuilder6 = new StringBuilder();
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      if (this.connection.versionMeetsMinimum(3, 23, 0)) {
        str1 = "<=>";
      } else {
        str1 = "=";
      } 
      boolean bool1 = true;
      boolean bool2 = true;
      byte b = 0;
      while (b < this.fields.length) {
        StringBuilder stringBuilder7 = new StringBuilder();
        if (this.fields[b].getOriginalTableName() != null) {
          String str7 = this.fields[b].getDatabaseName();
          if (str7 != null && str7.length() > 0) {
            stringBuilder7.append(str2);
            stringBuilder7.append(str7);
            stringBuilder7.append(str2);
            stringBuilder7.append('.');
          } 
          String str6 = this.fields[b].getOriginalTableName();
          stringBuilder7.append(str2);
          stringBuilder7.append(str6);
          stringBuilder7.append(str2);
          str4 = stringBuilder7.toString();
          if (!treeMap.containsKey(str4)) {
            if (!treeMap.isEmpty())
              stringBuilder6.append(','); 
            stringBuilder6.append(str4);
            treeMap.put(str4, str4);
          } 
          hashMap.put(Integer.valueOf(b), str4);
          Map<String, Integer> map = getColumnsToIndexMapForTableAndDB(str7, str6);
        } else {
          str3 = this.fields[b].getTableName();
          if (str3 != null) {
            str4.append(str2);
            str4.append(str3);
            str4.append(str2);
            str4 = str4.toString();
            if (!treeMap.containsKey(str4)) {
              if (!treeMap.isEmpty())
                stringBuilder6.append(','); 
              stringBuilder6.append(str4);
              treeMap.put(str4, str4);
            } 
            hashMap.put(Integer.valueOf(b), str4);
            Map<String, Integer> map = getColumnsToIndexMapForTableAndDB(this.catalog, str3);
          } else {
            str3 = null;
          } 
        } 
        String str4 = this.fields[b].getOriginalName();
        if (!this.connection.getIO().hasLongColumnInfo() || str4 == null || str4.length() <= 0)
          str4 = this.fields[b].getName(); 
        if (str3 != null && str4 != null)
          str3.put(str4, Integer.valueOf(b)); 
        String str3 = this.fields[b].getOriginalTableName();
        if (!this.connection.getIO().hasLongColumnInfo() || str3 == null || str3.length() <= 0)
          str3 = this.fields[b].getTableName(); 
        StringBuilder stringBuilder8 = new StringBuilder();
        String str5 = this.fields[b].getDatabaseName();
        if (str5 != null && str5.length() > 0) {
          stringBuilder8.append(str2);
          stringBuilder8.append(str5);
          stringBuilder8.append(str2);
          stringBuilder8.append('.');
        } 
        stringBuilder8.append(str2);
        stringBuilder8.append(str3);
        stringBuilder8.append(str2);
        stringBuilder8.append('.');
        stringBuilder8.append(str2);
        stringBuilder8.append(str4);
        stringBuilder8.append(str2);
        str3 = stringBuilder8.toString();
        boolean bool = bool1;
        if (this.fields[b].isPrimaryKey()) {
          this.primaryKeyIndicies.add(Integer.valueOf(b));
          if (!bool1) {
            stringBuilder3.append(" AND ");
          } else {
            bool1 = false;
          } 
          stringBuilder3.append(str3);
          stringBuilder3.append(str1);
          stringBuilder3.append("?");
          bool = bool1;
        } 
        if (bool2) {
          stringBuilder5.append("SET ");
          bool2 = false;
        } else {
          stringBuilder5.append(",");
          stringBuilder4.append(",");
          stringBuilder2.append(",");
        } 
        stringBuilder2.append("?");
        stringBuilder4.append(str3);
        stringBuilder5.append(str3);
        stringBuilder5.append("=?");
        b++;
        bool1 = bool;
      } 
      this.qualifiedAndQuotedTableName = stringBuilder6.toString();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("UPDATE ");
      stringBuilder1.append(this.qualifiedAndQuotedTableName);
      stringBuilder1.append(" ");
      stringBuilder1.append(stringBuilder5.toString());
      stringBuilder1.append(" WHERE ");
      stringBuilder1.append(stringBuilder3.toString());
      this.updateSQL = stringBuilder1.toString();
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("INSERT INTO ");
      stringBuilder1.append(this.qualifiedAndQuotedTableName);
      stringBuilder1.append(" (");
      stringBuilder1.append(stringBuilder4.toString());
      stringBuilder1.append(") VALUES (");
      stringBuilder1.append(stringBuilder2.toString());
      stringBuilder1.append(")");
      this.insertSQL = stringBuilder1.toString();
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("SELECT ");
      stringBuilder1.append(stringBuilder4.toString());
      stringBuilder1.append(" FROM ");
      stringBuilder1.append(this.qualifiedAndQuotedTableName);
      stringBuilder1.append(" WHERE ");
      stringBuilder1.append(stringBuilder3.toString());
      this.refreshSQL = stringBuilder1.toString();
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("DELETE FROM ");
      stringBuilder1.append(this.qualifiedAndQuotedTableName);
      stringBuilder1.append(" WHERE ");
      stringBuilder1.append(stringBuilder3.toString());
      this.deleteSQL = stringBuilder1.toString();
      return;
    } 
    this.doingUpdates = false;
    this.onInsertRow = false;
    throw new NotUpdatable(this.notUpdatableReason);
  }
  
  public int getConcurrency() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      char c;
      if (this.isUpdatable) {
        c = 'ϰ';
      } else {
        c = 'ϯ';
      } 
      return c;
    } 
  }
  
  public void insertRow() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.onInsertRow) {
        this.inserter.executeUpdate();
        long l = this.inserter.getLastInsertID();
        int i = this.fields.length;
        byte[][] arrayOfByte = new byte[i][];
        for (byte b = 0; b < i; b++) {
          if (this.inserter.isNull(b)) {
            arrayOfByte[b] = null;
          } else {
            arrayOfByte[b] = this.inserter.getBytesRepresentation(b);
          } 
          if (this.fields[b].isAutoIncrement() && l > 0L) {
            arrayOfByte[b] = StringUtils.getBytes(String.valueOf(l));
            this.inserter.setBytesNoEscapeNoQuotes(b + 1, arrayOfByte[b]);
          } 
        } 
        ByteArrayRow byteArrayRow = new ByteArrayRow();
        this(arrayOfByte, getExceptionInterceptor());
        refreshRow(this.inserter, byteArrayRow);
        this.rowData.addRow(byteArrayRow);
        resetInserter();
        return;
      } 
      throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.7"), getExceptionInterceptor());
    } 
  }
  
  public boolean isAfterLast() throws SQLException {
    return super.isAfterLast();
  }
  
  public boolean isBeforeFirst() throws SQLException {
    return super.isBeforeFirst();
  }
  
  public boolean isFirst() throws SQLException {
    return super.isFirst();
  }
  
  public boolean isLast() throws SQLException {
    return super.isLast();
  }
  
  public boolean isUpdatable() {
    return this.isUpdatable;
  }
  
  public boolean last() throws SQLException {
    return super.last();
  }
  
  public void moveToCurrentRow() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.isUpdatable) {
        if (this.onInsertRow) {
          this.onInsertRow = false;
          this.thisRow = this.savedCurrentRow;
        } 
        return;
      } 
      NotUpdatable notUpdatable = new NotUpdatable();
      this(this.notUpdatableReason);
      throw notUpdatable;
    } 
  }
  
  public void moveToInsertRow() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.isUpdatable) {
        if (this.inserter == null) {
          if (this.insertSQL == null)
            generateStatements(); 
          PreparedStatement preparedStatement = (PreparedStatement)this.connection.clientPrepareStatement(this.insertSQL);
          this.inserter = preparedStatement;
          MysqlParameterMetadata mysqlParameterMetadata = new MysqlParameterMetadata();
          Field[] arrayOfField = this.fields;
          this(arrayOfField, arrayOfField.length, getExceptionInterceptor());
          preparedStatement.parameterMetaData = mysqlParameterMetadata;
          if (this.populateInserterWithDefaultValues)
            extractDefaultValues(); 
          resetInserter();
        } else {
          resetInserter();
        } 
        int i = this.fields.length;
        this.onInsertRow = true;
        this.doingUpdates = false;
        this.savedCurrentRow = this.thisRow;
        byte[][] arrayOfByte = new byte[i][];
        ByteArrayRow byteArrayRow = new ByteArrayRow();
        this(arrayOfByte, getExceptionInterceptor());
        this.thisRow = byteArrayRow;
        byteArrayRow.setMetadata(this.fields);
        for (byte b = 0; b < i; b++) {
          if (!this.populateInserterWithDefaultValues) {
            this.inserter.setBytesNoEscapeNoQuotes(b + 1, StringUtils.getBytes("DEFAULT"));
            arrayOfByte = null;
          } else if (this.defaultColumnValue[b] != null) {
            int j = this.fields[b].getMysqlType();
            if (j != 7 && j != 14) {
              switch (j) {
                default:
                  this.inserter.setBytes(b + 1, this.defaultColumnValue[b], false, false);
                  break;
                case 10:
                case 11:
                case 12:
                  arrayOfByte1 = this.defaultColumnValue;
                  if ((arrayOfByte1[b]).length > 7 && arrayOfByte1[b][0] == 67 && arrayOfByte1[b][1] == 85 && arrayOfByte1[b][2] == 82 && arrayOfByte1[b][3] == 82 && arrayOfByte1[b][4] == 69 && arrayOfByte1[b][5] == 78 && arrayOfByte1[b][6] == 84 && arrayOfByte1[b][7] == 95) {
                    this.inserter.setBytesNoEscapeNoQuotes(b + 1, arrayOfByte1[b]);
                    break;
                  } 
                  this.inserter.setBytes(b + 1, arrayOfByte1[b], false, false);
                  break;
              } 
              byte[][] arrayOfByte1 = this.defaultColumnValue;
              j = (arrayOfByte1[b]).length;
              byte[] arrayOfByte2 = new byte[j];
              System.arraycopy(arrayOfByte1[b], 0, arrayOfByte2, 0, j);
              arrayOfByte[b] = arrayOfByte2;
            } else {
            
            } 
          } else {
            this.inserter.setNull(b + 1, 0);
            arrayOfByte[b] = null;
          } 
        } 
        return;
      } 
      NotUpdatable notUpdatable = new NotUpdatable();
      this(this.notUpdatableReason);
      throw notUpdatable;
    } 
  }
  
  public boolean next() throws SQLException {
    return super.next();
  }
  
  public boolean prev() throws SQLException {
    return super.prev();
  }
  
  public boolean previous() throws SQLException {
    return super.previous();
  }
  
  public void realClose(boolean paramBoolean) throws SQLException {
    if (this.connection == null)
      return; 
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.useUsageAdvisor && this.deleter == null && this.inserter == null && this.refresher == null && this.updater == null) {
        int i;
        String str1;
        this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
        String str2 = Messages.getString("UpdatableResultSet.34");
        ProfilerEventHandler profilerEventHandler = this.eventSink;
        ProfilerEvent profilerEvent = new ProfilerEvent();
        StatementImpl statementImpl = this.owningStatement;
        if (statementImpl == null) {
          str1 = "N/A";
        } else {
          str1 = statementImpl.currentCatalog;
        } 
        long l = this.connectionId;
        if (statementImpl == null) {
          i = -1;
        } else {
          i = statementImpl.getId();
        } 
        this((byte)0, "", str1, l, i, this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, str2);
        profilerEventHandler.consumeEvent(profilerEvent);
      } 
      try {
        PreparedStatement preparedStatement = this.deleter;
        if (preparedStatement != null)
          preparedStatement.close(); 
        preparedStatement = null;
      } catch (SQLException null) {}
      try {
        PreparedStatement preparedStatement = this.inserter;
        sQLException2 = sQLException1;
        if (preparedStatement != null) {
          preparedStatement.close();
          sQLException2 = sQLException1;
        } 
      } catch (SQLException null) {}
      try {
        PreparedStatement preparedStatement = this.refresher;
        sQLException1 = sQLException2;
        if (preparedStatement != null) {
          preparedStatement.close();
          sQLException1 = sQLException2;
        } 
      } catch (SQLException sQLException1) {}
      try {
        PreparedStatement preparedStatement = this.updater;
        sQLException2 = sQLException1;
        if (preparedStatement != null) {
          preparedStatement.close();
          sQLException2 = sQLException1;
        } 
      } catch (SQLException sQLException2) {}
      super.realClose(paramBoolean);
      if (sQLException2 == null)
        return; 
      throw sQLException2;
    } 
  }
  
  public void refreshRow() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.isUpdatable) {
        if (!this.onInsertRow) {
          if (this.rowData.size() != 0) {
            if (!isBeforeFirst()) {
              if (!isAfterLast()) {
                refreshRow(this.updater, this.thisRow);
                return;
              } 
              throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.11"), getExceptionInterceptor());
            } 
            throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.10"), getExceptionInterceptor());
          } 
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.9"), getExceptionInterceptor());
        } 
        throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.8"), getExceptionInterceptor());
      } 
      NotUpdatable notUpdatable = new NotUpdatable();
      this();
      throw notUpdatable;
    } 
  }
  
  public boolean relative(int paramInt) throws SQLException {
    return super.relative(paramInt);
  }
  
  public boolean rowDeleted() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public boolean rowInserted() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public boolean rowUpdated() throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public void setResultSetConcurrency(int paramInt) {
    super.setResultSetConcurrency(paramInt);
  }
  
  public void syncUpdate() throws SQLException {
    if (this.updater == null) {
      if (this.updateSQL == null)
        generateStatements(); 
      PreparedStatement preparedStatement = (PreparedStatement)this.connection.clientPrepareStatement(this.updateSQL);
      this.updater = preparedStatement;
      Field[] arrayOfField = this.fields;
      preparedStatement.parameterMetaData = new MysqlParameterMetadata(arrayOfField, arrayOfField.length, getExceptionInterceptor());
    } 
    int k = this.fields.length;
    this.updater.clearParameters();
    int j = 0;
    int i;
    for (i = 0; i < k; i++) {
      if (this.thisRow.getColumnValue(i) != null) {
        if (this.fields[i].getvalueNeedsQuoting()) {
          if (this.fields[i].isCharsetApplicableType() && !this.fields[i].getEncoding().equals(this.connection.getEncoding())) {
            this.updater.setString(i + 1, this.thisRow.getString(i, this.fields[i].getEncoding(), this.connection));
          } else {
            this.updater.setBytes(i + 1, this.thisRow.getColumnValue(i), this.fields[i].isBinary(), false);
          } 
        } else {
          this.updater.setBytesNoEscapeNoQuotes(i + 1, this.thisRow.getColumnValue(i));
        } 
      } else {
        this.updater.setNull(i + 1, 0);
      } 
    } 
    int m = this.primaryKeyIndicies.size();
    for (i = j; i < m; i++) {
      j = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
      setParamValue(this.updater, k + i + 1, this.thisRow, j, this.fields[j]);
    } 
  }
  
  public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setAsciiStream(paramInt1, paramInputStream, paramInt2);
      } else {
        this.inserter.setAsciiStream(paramInt1, paramInputStream, paramInt2);
        this.thisRow.setColumnValue(paramInt1 - 1, STREAM_DATA_MARKER);
      } 
      return;
    } 
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    updateAsciiStream(findColumn(paramString), paramInputStream, paramInt);
  }
  
  public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBigDecimal(paramInt, paramBigDecimal);
      } else {
        this.inserter.setBigDecimal(paramInt, paramBigDecimal);
        if (paramBigDecimal == null) {
          this.thisRow.setColumnValue(paramInt - 1, null);
        } else {
          this.thisRow.setColumnValue(paramInt - 1, StringUtils.getBytes(paramBigDecimal.toString()));
        } 
      } 
      return;
    } 
  }
  
  public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
    updateBigDecimal(findColumn(paramString), paramBigDecimal);
  }
  
  public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBinaryStream(paramInt1, paramInputStream, paramInt2);
      } else {
        this.inserter.setBinaryStream(paramInt1, paramInputStream, paramInt2);
        if (paramInputStream == null) {
          this.thisRow.setColumnValue(paramInt1 - 1, null);
        } else {
          this.thisRow.setColumnValue(paramInt1 - 1, STREAM_DATA_MARKER);
        } 
      } 
      return;
    } 
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
    updateBinaryStream(findColumn(paramString), paramInputStream, paramInt);
  }
  
  public void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBlob(paramInt, paramBlob);
      } else {
        this.inserter.setBlob(paramInt, paramBlob);
        if (paramBlob == null) {
          this.thisRow.setColumnValue(paramInt - 1, null);
        } else {
          this.thisRow.setColumnValue(paramInt - 1, STREAM_DATA_MARKER);
        } 
      } 
      return;
    } 
  }
  
  public void updateBlob(String paramString, Blob paramBlob) throws SQLException {
    updateBlob(findColumn(paramString), paramBlob);
  }
  
  public void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBoolean(paramInt, paramBoolean);
      } else {
        this.inserter.setBoolean(paramInt, paramBoolean);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateBoolean(String paramString, boolean paramBoolean) throws SQLException {
    updateBoolean(findColumn(paramString), paramBoolean);
  }
  
  public void updateByte(int paramInt, byte paramByte) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setByte(paramInt, paramByte);
      } else {
        this.inserter.setByte(paramInt, paramByte);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateByte(String paramString, byte paramByte) throws SQLException {
    updateByte(findColumn(paramString), paramByte);
  }
  
  public void updateBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBytes(paramInt, paramArrayOfbyte);
      } else {
        this.inserter.setBytes(paramInt, paramArrayOfbyte);
        this.thisRow.setColumnValue(paramInt - 1, paramArrayOfbyte);
      } 
      return;
    } 
  }
  
  public void updateBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
    updateBytes(findColumn(paramString), paramArrayOfbyte);
  }
  
  public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setCharacterStream(paramInt1, paramReader, paramInt2);
      } else {
        this.inserter.setCharacterStream(paramInt1, paramReader, paramInt2);
        if (paramReader == null) {
          this.thisRow.setColumnValue(paramInt1 - 1, null);
        } else {
          this.thisRow.setColumnValue(paramInt1 - 1, STREAM_DATA_MARKER);
        } 
      } 
      return;
    } 
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
    updateCharacterStream(findColumn(paramString), paramReader, paramInt);
  }
  
  public void updateClob(int paramInt, Clob paramClob) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: aload_2
    //   13: ifnonnull -> 24
    //   16: aload_0
    //   17: iload_1
    //   18: invokevirtual updateNull : (I)V
    //   21: goto -> 42
    //   24: aload_0
    //   25: iload_1
    //   26: aload_2
    //   27: invokeinterface getCharacterStream : ()Ljava/io/Reader;
    //   32: aload_2
    //   33: invokeinterface length : ()J
    //   38: l2i
    //   39: invokevirtual updateCharacterStream : (ILjava/io/Reader;I)V
    //   42: aload_3
    //   43: monitorexit
    //   44: return
    //   45: astore_2
    //   46: aload_3
    //   47: monitorexit
    //   48: aload_2
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   16	21	45	finally
    //   24	42	45	finally
    //   42	44	45	finally
    //   46	48	45	finally
  }
  
  public void updateDate(int paramInt, Date paramDate) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setDate(paramInt, paramDate);
      } else {
        this.inserter.setDate(paramInt, paramDate);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateDate(String paramString, Date paramDate) throws SQLException {
    updateDate(findColumn(paramString), paramDate);
  }
  
  public void updateDouble(int paramInt, double paramDouble) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setDouble(paramInt, paramDouble);
      } else {
        this.inserter.setDouble(paramInt, paramDouble);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateDouble(String paramString, double paramDouble) throws SQLException {
    updateDouble(findColumn(paramString), paramDouble);
  }
  
  public void updateFloat(int paramInt, float paramFloat) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setFloat(paramInt, paramFloat);
      } else {
        this.inserter.setFloat(paramInt, paramFloat);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateFloat(String paramString, float paramFloat) throws SQLException {
    updateFloat(findColumn(paramString), paramFloat);
  }
  
  public void updateInt(int paramInt1, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setInt(paramInt1, paramInt2);
      } else {
        this.inserter.setInt(paramInt1, paramInt2);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt1, this.inserter.getBytesRepresentation(paramInt1));
      } 
      return;
    } 
  }
  
  public void updateInt(String paramString, int paramInt) throws SQLException {
    updateInt(findColumn(paramString), paramInt);
  }
  
  public void updateLong(int paramInt, long paramLong) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setLong(paramInt, paramLong);
      } else {
        this.inserter.setLong(paramInt, paramLong);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateLong(String paramString, long paramLong) throws SQLException {
    updateLong(findColumn(paramString), paramLong);
  }
  
  public void updateNull(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setNull(paramInt, 0);
      } else {
        this.inserter.setNull(paramInt, 0);
        this.thisRow.setColumnValue(paramInt - 1, null);
      } 
      return;
    } 
  }
  
  public void updateNull(String paramString) throws SQLException {
    updateNull(findColumn(paramString));
  }
  
  public void updateObject(int paramInt, Object paramObject) throws SQLException {
    updateObjectInternal(paramInt, paramObject, null, 0);
  }
  
  public void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
    updateObjectInternal(paramInt1, paramObject, null, paramInt2);
  }
  
  public void updateObject(String paramString, Object paramObject) throws SQLException {
    updateObject(findColumn(paramString), paramObject);
  }
  
  public void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
    updateObject(findColumn(paramString), paramObject);
  }
  
  public void updateObjectInternal(int paramInt1, Object paramObject, Integer paramInteger, int paramInt2) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        if (paramInteger == null) {
          this.updater.setObject(paramInt1, paramObject);
        } else {
          this.updater.setObject(paramInt1, paramObject, paramInteger.intValue());
        } 
      } else {
        if (paramInteger == null) {
          this.inserter.setObject(paramInt1, paramObject);
        } else {
          this.inserter.setObject(paramInt1, paramObject, paramInteger.intValue());
        } 
        paramObject = this.thisRow;
        paramObject.setColumnValue(--paramInt1, this.inserter.getBytesRepresentation(paramInt1));
      } 
      return;
    } 
  }
  
  public void updateRow() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.isUpdatable) {
        if (this.doingUpdates) {
          this.updater.executeUpdate();
          refreshRow();
          this.doingUpdates = false;
        } else if (this.onInsertRow) {
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.44"), getExceptionInterceptor());
        } 
        syncUpdate();
        return;
      } 
      NotUpdatable notUpdatable = new NotUpdatable();
      this(this.notUpdatableReason);
      throw notUpdatable;
    } 
  }
  
  public void updateShort(int paramInt, short paramShort) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setShort(paramInt, paramShort);
      } else {
        this.inserter.setShort(paramInt, paramShort);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateShort(String paramString, short paramShort) throws SQLException {
    updateShort(findColumn(paramString), paramShort);
  }
  
  public void updateString(int paramInt, String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setString(paramInt, paramString);
      } else {
        this.inserter.setString(paramInt, paramString);
        if (paramString == null) {
          this.thisRow.setColumnValue(paramInt - 1, null);
        } else if (getCharConverter() != null) {
          this.thisRow.setColumnValue(paramInt - 1, StringUtils.getBytes(paramString, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
        } else {
          this.thisRow.setColumnValue(paramInt - 1, StringUtils.getBytes(paramString));
        } 
      } 
      return;
    } 
  }
  
  public void updateString(String paramString1, String paramString2) throws SQLException {
    updateString(findColumn(paramString1), paramString2);
  }
  
  public void updateTime(int paramInt, Time paramTime) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setTime(paramInt, paramTime);
      } else {
        this.inserter.setTime(paramInt, paramTime);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateTime(String paramString, Time paramTime) throws SQLException {
    updateTime(findColumn(paramString), paramTime);
  }
  
  public void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setTimestamp(paramInt, paramTimestamp);
      } else {
        this.inserter.setTimestamp(paramInt, paramTimestamp);
        ResultSetRow resultSetRow = this.thisRow;
        resultSetRow.setColumnValue(--paramInt, this.inserter.getBytesRepresentation(paramInt));
      } 
      return;
    } 
  }
  
  public void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
    updateTimestamp(findColumn(paramString), paramTimestamp);
  }
}
