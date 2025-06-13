package com.mysql.jdbc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlobFromLocator implements Blob {
  private String blobColumnName = null;
  
  private ResultSetImpl creatorResultSet;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private int numColsInResultSet = 0;
  
  private int numPrimaryKeys = 0;
  
  private List<String> primaryKeyColumns = null;
  
  private List<String> primaryKeyValues = null;
  
  private String quotedId;
  
  private String tableName = null;
  
  public BlobFromLocator(ResultSetImpl paramResultSetImpl, int paramInt, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    this.exceptionInterceptor = paramExceptionInterceptor;
    this.creatorResultSet = paramResultSetImpl;
    this.numColsInResultSet = paramResultSetImpl.fields.length;
    this.quotedId = paramResultSetImpl.connection.getMetaData().getIdentifierQuoteString();
    if (this.numColsInResultSet > 1) {
      this.primaryKeyColumns = new ArrayList<String>();
      this.primaryKeyValues = new ArrayList<String>();
      for (byte b = 0; b < this.numColsInResultSet; b++) {
        if (this.creatorResultSet.fields[b].isPrimaryKey()) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(this.quotedId);
          String str = this.creatorResultSet.fields[b].getOriginalName();
          if (str != null && str.length() > 0) {
            stringBuilder1.append(str);
          } else {
            stringBuilder1.append(this.creatorResultSet.fields[b].getName());
          } 
          stringBuilder1.append(this.quotedId);
          this.primaryKeyColumns.add(stringBuilder1.toString());
          this.primaryKeyValues.add(this.creatorResultSet.getString(b + 1));
        } 
      } 
    } else {
      notEnoughInformationInQuery();
    } 
    int i = this.primaryKeyColumns.size();
    this.numPrimaryKeys = i;
    if (i == 0)
      notEnoughInformationInQuery(); 
    if (this.creatorResultSet.fields[0].getOriginalTableName() != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      String str = this.creatorResultSet.fields[0].getDatabaseName();
      if (str != null && str.length() > 0) {
        stringBuilder1.append(this.quotedId);
        stringBuilder1.append(str);
        stringBuilder1.append(this.quotedId);
        stringBuilder1.append('.');
      } 
      stringBuilder1.append(this.quotedId);
      stringBuilder1.append(this.creatorResultSet.fields[0].getOriginalTableName());
      stringBuilder1.append(this.quotedId);
      this.tableName = stringBuilder1.toString();
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(this.quotedId);
      stringBuilder1.append(this.creatorResultSet.fields[0].getTableName());
      stringBuilder1.append(this.quotedId);
      this.tableName = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.quotedId);
    stringBuilder.append(this.creatorResultSet.getString(paramInt));
    stringBuilder.append(this.quotedId);
    this.blobColumnName = stringBuilder.toString();
  }
  
  private void notEnoughInformationInQuery() throws SQLException {
    throw SQLError.createSQLException("Emulated BLOB locators must come from a ResultSet with only one table selected, and all primary keys selected", "S1000", this.exceptionInterceptor);
  }
  
  public PreparedStatement createGetBytesStatement() throws SQLException {
    StringBuilder stringBuilder = new StringBuilder("SELECT SUBSTRING(");
    stringBuilder.append(this.blobColumnName);
    stringBuilder.append(", ");
    stringBuilder.append("?");
    stringBuilder.append(", ");
    stringBuilder.append("?");
    stringBuilder.append(") FROM ");
    stringBuilder.append(this.tableName);
    stringBuilder.append(" WHERE ");
    stringBuilder.append(this.primaryKeyColumns.get(0));
    stringBuilder.append(" = ?");
    for (byte b = 1; b < this.numPrimaryKeys; b++) {
      stringBuilder.append(" AND ");
      stringBuilder.append(this.primaryKeyColumns.get(b));
      stringBuilder.append(" = ?");
    } 
    return this.creatorResultSet.connection.prepareStatement(stringBuilder.toString());
  }
  
  public void free() throws SQLException {
    this.creatorResultSet = null;
    this.primaryKeyColumns = null;
    this.primaryKeyValues = null;
  }
  
  public InputStream getBinaryStream() throws SQLException {
    return new BufferedInputStream(new LocatorInputStream(), this.creatorResultSet.connection.getLocatorFetchBufferSize());
  }
  
  public InputStream getBinaryStream(long paramLong1, long paramLong2) throws SQLException {
    return new LocatorInputStream(paramLong1, paramLong2);
  }
  
  public byte[] getBytes(long paramLong, int paramInt) throws SQLException {
    Exception exception;
    PreparedStatement preparedStatement;
    try {
      preparedStatement = createGetBytesStatement();
    } finally {
      exception = null;
    } 
    if (preparedStatement != null)
      try {
        preparedStatement.close();
      } catch (SQLException sQLException) {} 
    throw exception;
  }
  
  public byte[] getBytesInternal(PreparedStatement paramPreparedStatement, long paramLong, int paramInt) throws SQLException {
    // Byte code:
    //   0: aconst_null
    //   1: astore #6
    //   3: aload #6
    //   5: astore #5
    //   7: aload_1
    //   8: iconst_1
    //   9: lload_2
    //   10: invokeinterface setLong : (IJ)V
    //   15: aload #6
    //   17: astore #5
    //   19: aload_1
    //   20: iconst_2
    //   21: iload #4
    //   23: invokeinterface setInt : (II)V
    //   28: iconst_0
    //   29: istore #4
    //   31: aload #6
    //   33: astore #5
    //   35: iload #4
    //   37: aload_0
    //   38: getfield numPrimaryKeys : I
    //   41: if_icmpge -> 78
    //   44: aload #6
    //   46: astore #5
    //   48: aload_1
    //   49: iload #4
    //   51: iconst_3
    //   52: iadd
    //   53: aload_0
    //   54: getfield primaryKeyValues : Ljava/util/List;
    //   57: iload #4
    //   59: invokeinterface get : (I)Ljava/lang/Object;
    //   64: checkcast java/lang/String
    //   67: invokeinterface setString : (ILjava/lang/String;)V
    //   72: iinc #4, 1
    //   75: goto -> 31
    //   78: aload #6
    //   80: astore #5
    //   82: aload_1
    //   83: invokeinterface executeQuery : ()Ljava/sql/ResultSet;
    //   88: astore_1
    //   89: aload_1
    //   90: astore #5
    //   92: aload_1
    //   93: invokeinterface next : ()Z
    //   98: ifeq -> 128
    //   101: aload_1
    //   102: astore #5
    //   104: aload_1
    //   105: checkcast com/mysql/jdbc/ResultSetImpl
    //   108: iconst_1
    //   109: iconst_1
    //   110: invokevirtual getBytes : (IZ)[B
    //   113: astore #6
    //   115: aload_1
    //   116: ifnull -> 125
    //   119: aload_1
    //   120: invokeinterface close : ()V
    //   125: aload #6
    //   127: areturn
    //   128: aload_1
    //   129: astore #5
    //   131: ldc 'BLOB data not found! Did primary keys change?'
    //   133: ldc 'S1000'
    //   135: aload_0
    //   136: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   139: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   142: athrow
    //   143: astore_1
    //   144: aload #5
    //   146: ifnull -> 156
    //   149: aload #5
    //   151: invokeinterface close : ()V
    //   156: aload_1
    //   157: athrow
    //   158: astore_1
    //   159: goto -> 125
    //   162: astore #5
    //   164: goto -> 156
    // Exception table:
    //   from	to	target	type
    //   7	15	143	finally
    //   19	28	143	finally
    //   35	44	143	finally
    //   48	72	143	finally
    //   82	89	143	finally
    //   92	101	143	finally
    //   104	115	143	finally
    //   119	125	158	java/sql/SQLException
    //   131	143	143	finally
    //   149	156	162	java/sql/SQLException
  }
  
  public long length() throws SQLException {
    StringBuilder stringBuilder1 = new StringBuilder("SELECT LENGTH(");
    stringBuilder1.append(this.blobColumnName);
    stringBuilder1.append(") FROM ");
    stringBuilder1.append(this.tableName);
    stringBuilder1.append(" WHERE ");
    List<String> list = this.primaryKeyColumns;
    int j = 0;
    stringBuilder1.append(list.get(0));
    stringBuilder1.append(" = ?");
    int i;
    for (i = 1; i < this.numPrimaryKeys; i++) {
      stringBuilder1.append(" AND ");
      stringBuilder1.append(this.primaryKeyColumns.get(i));
      stringBuilder1.append(" = ?");
    } 
    ResultSet resultSet = null;
    StringBuilder stringBuilder2 = null;
    try {
      PreparedStatement preparedStatement = this.creatorResultSet.connection.prepareStatement(stringBuilder1.toString());
      i = j;
      while (true) {
        ResultSet resultSet1;
        StringBuilder stringBuilder;
        stringBuilder1 = stringBuilder2;
        try {
          if (i < this.numPrimaryKeys) {
            j = i + 1;
            stringBuilder1 = stringBuilder2;
            preparedStatement.setString(j, this.primaryKeyValues.get(i));
            i = j;
            continue;
          } 
          stringBuilder1 = stringBuilder2;
          resultSet = preparedStatement.executeQuery();
          resultSet1 = resultSet;
          if (resultSet.next()) {
            resultSet1 = resultSet;
            return l;
          } 
          resultSet1 = resultSet;
          throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000", this.exceptionInterceptor);
        } finally {
          stringBuilder2 = null;
          resultSet = resultSet1;
        } 
        if (resultSet != null)
          try {
            resultSet.close();
          } catch (SQLException sQLException) {} 
        if (preparedStatement != null)
          try {
            preparedStatement.close();
          } catch (SQLException sQLException1) {} 
        throw stringBuilder;
      } 
    } finally {
      stringBuilder1 = null;
    } 
    if (sQLException != null)
      try {
        sQLException.close();
      } catch (SQLException sQLException1) {} 
    if (list != null)
      try {
        list.close();
      } catch (SQLException sQLException1) {} 
    throw stringBuilder1;
  }
  
  public long position(Blob paramBlob, long paramLong) throws SQLException {
    return position(paramBlob.getBytes(0L, (int)paramBlob.length()), paramLong);
  }
  
  public long position(byte[] paramArrayOfbyte, long paramLong) throws SQLException {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: ldc 'SELECT LOCATE('
    //   6: invokespecial <init> : (Ljava/lang/String;)V
    //   9: astore #7
    //   11: aload #7
    //   13: ldc '?, '
    //   15: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: pop
    //   19: aload #7
    //   21: aload_0
    //   22: getfield blobColumnName : Ljava/lang/String;
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload #7
    //   31: ldc ', '
    //   33: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: pop
    //   37: aload #7
    //   39: lload_2
    //   40: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload #7
    //   46: ldc ') FROM '
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload #7
    //   54: aload_0
    //   55: getfield tableName : Ljava/lang/String;
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: aload #7
    //   64: ldc ' WHERE '
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_0
    //   71: getfield primaryKeyColumns : Ljava/util/List;
    //   74: astore #6
    //   76: iconst_0
    //   77: istore #5
    //   79: aload #7
    //   81: aload #6
    //   83: iconst_0
    //   84: invokeinterface get : (I)Ljava/lang/Object;
    //   89: checkcast java/lang/String
    //   92: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: pop
    //   96: aload #7
    //   98: ldc ' = ?'
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: iconst_1
    //   105: istore #4
    //   107: iload #4
    //   109: aload_0
    //   110: getfield numPrimaryKeys : I
    //   113: if_icmpge -> 158
    //   116: aload #7
    //   118: ldc ' AND '
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload #7
    //   126: aload_0
    //   127: getfield primaryKeyColumns : Ljava/util/List;
    //   130: iload #4
    //   132: invokeinterface get : (I)Ljava/lang/Object;
    //   137: checkcast java/lang/String
    //   140: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: aload #7
    //   146: ldc ' = ?'
    //   148: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: iinc #4, 1
    //   155: goto -> 107
    //   158: aconst_null
    //   159: astore #6
    //   161: aconst_null
    //   162: astore #8
    //   164: aload_0
    //   165: getfield creatorResultSet : Lcom/mysql/jdbc/ResultSetImpl;
    //   168: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   171: aload #7
    //   173: invokevirtual toString : ()Ljava/lang/String;
    //   176: invokeinterface prepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   181: astore #7
    //   183: aload #8
    //   185: astore #6
    //   187: aload #7
    //   189: iconst_1
    //   190: aload_1
    //   191: invokeinterface setBytes : (I[B)V
    //   196: iload #5
    //   198: istore #4
    //   200: aload #8
    //   202: astore #6
    //   204: iload #4
    //   206: aload_0
    //   207: getfield numPrimaryKeys : I
    //   210: if_icmpge -> 248
    //   213: aload #8
    //   215: astore #6
    //   217: aload #7
    //   219: iload #4
    //   221: iconst_2
    //   222: iadd
    //   223: aload_0
    //   224: getfield primaryKeyValues : Ljava/util/List;
    //   227: iload #4
    //   229: invokeinterface get : (I)Ljava/lang/Object;
    //   234: checkcast java/lang/String
    //   237: invokeinterface setString : (ILjava/lang/String;)V
    //   242: iinc #4, 1
    //   245: goto -> 200
    //   248: aload #8
    //   250: astore #6
    //   252: aload #7
    //   254: invokeinterface executeQuery : ()Ljava/sql/ResultSet;
    //   259: astore_1
    //   260: aload_1
    //   261: astore #6
    //   263: aload_1
    //   264: invokeinterface next : ()Z
    //   269: ifeq -> 311
    //   272: aload_1
    //   273: astore #6
    //   275: aload_1
    //   276: iconst_1
    //   277: invokeinterface getLong : (I)J
    //   282: lstore_2
    //   283: aload_1
    //   284: ifnull -> 297
    //   287: aload_1
    //   288: invokeinterface close : ()V
    //   293: goto -> 297
    //   296: astore_1
    //   297: aload #7
    //   299: ifnull -> 309
    //   302: aload #7
    //   304: invokeinterface close : ()V
    //   309: lload_2
    //   310: lreturn
    //   311: aload_1
    //   312: astore #6
    //   314: ldc 'BLOB data not found! Did primary keys change?'
    //   316: ldc 'S1000'
    //   318: aload_0
    //   319: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   322: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   325: athrow
    //   326: astore_1
    //   327: goto -> 334
    //   330: astore_1
    //   331: aconst_null
    //   332: astore #7
    //   334: aload #6
    //   336: ifnull -> 351
    //   339: aload #6
    //   341: invokeinterface close : ()V
    //   346: goto -> 351
    //   349: astore #6
    //   351: aload #7
    //   353: ifnull -> 363
    //   356: aload #7
    //   358: invokeinterface close : ()V
    //   363: aload_1
    //   364: athrow
    //   365: astore_1
    //   366: goto -> 309
    //   369: astore #6
    //   371: goto -> 363
    // Exception table:
    //   from	to	target	type
    //   164	183	330	finally
    //   187	196	326	finally
    //   204	213	326	finally
    //   217	242	326	finally
    //   252	260	326	finally
    //   263	272	326	finally
    //   275	283	326	finally
    //   287	293	296	java/sql/SQLException
    //   302	309	365	java/sql/SQLException
    //   314	326	326	finally
    //   339	346	349	java/sql/SQLException
    //   356	363	369	java/sql/SQLException
  }
  
  public OutputStream setBinaryStream(long paramLong) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public int setBytes(long paramLong, byte[] paramArrayOfbyte) throws SQLException {
    return setBytes(paramLong, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int setBytes(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws SQLException {
    // Byte code:
    //   0: iload #5
    //   2: istore #6
    //   4: iload #4
    //   6: iload #5
    //   8: iadd
    //   9: aload_3
    //   10: arraylength
    //   11: if_icmple -> 21
    //   14: aload_3
    //   15: arraylength
    //   16: iload #4
    //   18: isub
    //   19: istore #6
    //   21: iload #6
    //   23: newarray byte
    //   25: astore #8
    //   27: iconst_0
    //   28: istore #5
    //   30: aload_3
    //   31: iload #4
    //   33: aload #8
    //   35: iconst_0
    //   36: iload #6
    //   38: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   41: new java/lang/StringBuilder
    //   44: dup
    //   45: ldc_w 'UPDATE '
    //   48: invokespecial <init> : (Ljava/lang/String;)V
    //   51: astore #7
    //   53: aload #7
    //   55: aload_0
    //   56: getfield tableName : Ljava/lang/String;
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload #7
    //   65: ldc_w ' SET '
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload #7
    //   74: aload_0
    //   75: getfield blobColumnName : Ljava/lang/String;
    //   78: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: pop
    //   82: aload #7
    //   84: ldc_w ' = INSERT('
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: aload #7
    //   93: aload_0
    //   94: getfield blobColumnName : Ljava/lang/String;
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload #7
    //   103: ldc ', '
    //   105: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload #7
    //   111: lload_1
    //   112: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload #7
    //   118: ldc ', '
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload #7
    //   126: iload #6
    //   128: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload #7
    //   134: ldc_w ', ?) WHERE '
    //   137: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: pop
    //   141: aload #7
    //   143: aload_0
    //   144: getfield primaryKeyColumns : Ljava/util/List;
    //   147: iconst_0
    //   148: invokeinterface get : (I)Ljava/lang/Object;
    //   153: checkcast java/lang/String
    //   156: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: aload #7
    //   162: ldc ' = ?'
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: pop
    //   168: iconst_1
    //   169: istore #4
    //   171: iload #4
    //   173: aload_0
    //   174: getfield numPrimaryKeys : I
    //   177: if_icmpge -> 222
    //   180: aload #7
    //   182: ldc ' AND '
    //   184: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: pop
    //   188: aload #7
    //   190: aload_0
    //   191: getfield primaryKeyColumns : Ljava/util/List;
    //   194: iload #4
    //   196: invokeinterface get : (I)Ljava/lang/Object;
    //   201: checkcast java/lang/String
    //   204: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: aload #7
    //   210: ldc ' = ?'
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: pop
    //   216: iinc #4, 1
    //   219: goto -> 171
    //   222: aconst_null
    //   223: astore_3
    //   224: aload_0
    //   225: getfield creatorResultSet : Lcom/mysql/jdbc/ResultSetImpl;
    //   228: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   231: aload #7
    //   233: invokevirtual toString : ()Ljava/lang/String;
    //   236: invokeinterface prepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   241: astore #7
    //   243: aload #7
    //   245: astore_3
    //   246: aload #7
    //   248: iconst_1
    //   249: aload #8
    //   251: invokeinterface setBytes : (I[B)V
    //   256: iload #5
    //   258: istore #4
    //   260: aload #7
    //   262: astore_3
    //   263: iload #4
    //   265: aload_0
    //   266: getfield numPrimaryKeys : I
    //   269: if_icmpge -> 306
    //   272: aload #7
    //   274: astore_3
    //   275: aload #7
    //   277: iload #4
    //   279: iconst_2
    //   280: iadd
    //   281: aload_0
    //   282: getfield primaryKeyValues : Ljava/util/List;
    //   285: iload #4
    //   287: invokeinterface get : (I)Ljava/lang/Object;
    //   292: checkcast java/lang/String
    //   295: invokeinterface setString : (ILjava/lang/String;)V
    //   300: iinc #4, 1
    //   303: goto -> 260
    //   306: aload #7
    //   308: astore_3
    //   309: aload #7
    //   311: invokeinterface executeUpdate : ()I
    //   316: istore #4
    //   318: iload #4
    //   320: iconst_1
    //   321: if_icmpne -> 342
    //   324: aload #7
    //   326: ifnull -> 336
    //   329: aload #7
    //   331: invokeinterface close : ()V
    //   336: aload_0
    //   337: invokevirtual length : ()J
    //   340: l2i
    //   341: ireturn
    //   342: aload #7
    //   344: astore_3
    //   345: ldc 'BLOB data not found! Did primary keys change?'
    //   347: ldc 'S1000'
    //   349: aload_0
    //   350: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   353: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   356: athrow
    //   357: astore #7
    //   359: aload_3
    //   360: ifnull -> 369
    //   363: aload_3
    //   364: invokeinterface close : ()V
    //   369: aload #7
    //   371: athrow
    //   372: astore_3
    //   373: goto -> 336
    //   376: astore_3
    //   377: goto -> 369
    // Exception table:
    //   from	to	target	type
    //   224	243	357	finally
    //   246	256	357	finally
    //   263	272	357	finally
    //   275	300	357	finally
    //   309	318	357	finally
    //   329	336	372	java/sql/SQLException
    //   345	357	357	finally
    //   363	369	376	java/sql/SQLException
  }
  
  public void truncate(long paramLong) throws SQLException {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: ldc_w 'UPDATE '
    //   7: invokespecial <init> : (Ljava/lang/String;)V
    //   10: astore #6
    //   12: aload #6
    //   14: aload_0
    //   15: getfield tableName : Ljava/lang/String;
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload #6
    //   24: ldc_w ' SET '
    //   27: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: pop
    //   31: aload #6
    //   33: aload_0
    //   34: getfield blobColumnName : Ljava/lang/String;
    //   37: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: pop
    //   41: aload #6
    //   43: ldc_w ' = LEFT('
    //   46: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: pop
    //   50: aload #6
    //   52: aload_0
    //   53: getfield blobColumnName : Ljava/lang/String;
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload #6
    //   62: ldc ', '
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload #6
    //   70: lload_1
    //   71: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: aload #6
    //   77: ldc_w ') WHERE '
    //   80: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_0
    //   85: getfield primaryKeyColumns : Ljava/util/List;
    //   88: astore #5
    //   90: iconst_0
    //   91: istore #4
    //   93: aload #6
    //   95: aload #5
    //   97: iconst_0
    //   98: invokeinterface get : (I)Ljava/lang/Object;
    //   103: checkcast java/lang/String
    //   106: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: aload #6
    //   112: ldc ' = ?'
    //   114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: iconst_1
    //   119: istore_3
    //   120: iload_3
    //   121: aload_0
    //   122: getfield numPrimaryKeys : I
    //   125: if_icmpge -> 169
    //   128: aload #6
    //   130: ldc ' AND '
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload #6
    //   138: aload_0
    //   139: getfield primaryKeyColumns : Ljava/util/List;
    //   142: iload_3
    //   143: invokeinterface get : (I)Ljava/lang/Object;
    //   148: checkcast java/lang/String
    //   151: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: pop
    //   155: aload #6
    //   157: ldc ' = ?'
    //   159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: iinc #3, 1
    //   166: goto -> 120
    //   169: aconst_null
    //   170: astore #5
    //   172: aload_0
    //   173: getfield creatorResultSet : Lcom/mysql/jdbc/ResultSetImpl;
    //   176: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   179: aload #6
    //   181: invokevirtual toString : ()Ljava/lang/String;
    //   184: invokeinterface prepareStatement : (Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   189: astore #6
    //   191: iload #4
    //   193: istore_3
    //   194: aload #6
    //   196: astore #5
    //   198: iload_3
    //   199: aload_0
    //   200: getfield numPrimaryKeys : I
    //   203: if_icmpge -> 243
    //   206: iload_3
    //   207: iconst_1
    //   208: iadd
    //   209: istore #4
    //   211: aload #6
    //   213: astore #5
    //   215: aload #6
    //   217: iload #4
    //   219: aload_0
    //   220: getfield primaryKeyValues : Ljava/util/List;
    //   223: iload_3
    //   224: invokeinterface get : (I)Ljava/lang/Object;
    //   229: checkcast java/lang/String
    //   232: invokeinterface setString : (ILjava/lang/String;)V
    //   237: iload #4
    //   239: istore_3
    //   240: goto -> 194
    //   243: aload #6
    //   245: astore #5
    //   247: aload #6
    //   249: invokeinterface executeUpdate : ()I
    //   254: istore_3
    //   255: iload_3
    //   256: iconst_1
    //   257: if_icmpne -> 273
    //   260: aload #6
    //   262: ifnull -> 272
    //   265: aload #6
    //   267: invokeinterface close : ()V
    //   272: return
    //   273: aload #6
    //   275: astore #5
    //   277: ldc 'BLOB data not found! Did primary keys change?'
    //   279: ldc 'S1000'
    //   281: aload_0
    //   282: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   285: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   288: athrow
    //   289: astore #6
    //   291: aload #5
    //   293: ifnull -> 303
    //   296: aload #5
    //   298: invokeinterface close : ()V
    //   303: aload #6
    //   305: athrow
    //   306: astore #5
    //   308: goto -> 272
    //   311: astore #5
    //   313: goto -> 303
    // Exception table:
    //   from	to	target	type
    //   172	191	289	finally
    //   198	206	289	finally
    //   215	237	289	finally
    //   247	255	289	finally
    //   265	272	306	java/sql/SQLException
    //   277	289	289	finally
    //   296	303	311	java/sql/SQLException
  }
  
  public class LocatorInputStream extends InputStream {
    public long currentPositionInBlob = 0L;
    
    public long length = 0L;
    
    public PreparedStatement pStmt = null;
    
    public final BlobFromLocator this$0;
    
    public LocatorInputStream() throws SQLException {
      this.length = BlobFromLocator.this.length();
      this.pStmt = BlobFromLocator.this.createGetBytesStatement();
    }
    
    public LocatorInputStream(long param1Long1, long param1Long2) throws SQLException {
      long l2 = param1Long1 + param1Long2;
      this.length = l2;
      this.currentPositionInBlob = param1Long1;
      long l1 = BlobFromLocator.this.length();
      if (l2 <= l1) {
        if (param1Long1 >= 1L) {
          if (param1Long1 <= l1)
            return; 
          throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamPos"), "S1009", BlobFromLocator.this.exceptionInterceptor);
        } 
        throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamPos"), "S1009", BlobFromLocator.this.exceptionInterceptor);
      } 
      throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamLength", new Object[] { Long.valueOf(l1), Long.valueOf(param1Long1), Long.valueOf(param1Long2) }), "S1009", BlobFromLocator.this.exceptionInterceptor);
    }
    
    public void close() throws IOException {
      PreparedStatement preparedStatement = this.pStmt;
      if (preparedStatement != null)
        try {
          preparedStatement.close();
        } catch (SQLException sQLException) {
          throw new IOException(sQLException.toString());
        }  
      super.close();
    }
    
    public int read() throws IOException {
      long l = this.currentPositionInBlob;
      if (l + 1L > this.length)
        return -1; 
      try {
        BlobFromLocator blobFromLocator = BlobFromLocator.this;
        PreparedStatement preparedStatement = this.pStmt;
        this.currentPositionInBlob = ++l;
        byte[] arrayOfByte = blobFromLocator.getBytesInternal(preparedStatement, l, 1);
        return (arrayOfByte == null) ? -1 : arrayOfByte[0];
      } catch (SQLException sQLException) {
        throw new IOException(sQLException.toString());
      } 
    }
    
    public int read(byte[] param1ArrayOfbyte) throws IOException {
      long l = this.currentPositionInBlob;
      if (l + 1L > this.length)
        return -1; 
      try {
        byte[] arrayOfByte = BlobFromLocator.this.getBytesInternal(this.pStmt, l + 1L, param1ArrayOfbyte.length);
        if (arrayOfByte == null)
          return -1; 
        System.arraycopy(arrayOfByte, 0, param1ArrayOfbyte, 0, arrayOfByte.length);
        this.currentPositionInBlob += arrayOfByte.length;
        return arrayOfByte.length;
      } catch (SQLException sQLException) {
        throw new IOException(sQLException.toString());
      } 
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      long l = this.currentPositionInBlob;
      if (l + 1L > this.length)
        return -1; 
      try {
        byte[] arrayOfByte = BlobFromLocator.this.getBytesInternal(this.pStmt, l + 1L, param1Int2);
        if (arrayOfByte == null)
          return -1; 
        System.arraycopy(arrayOfByte, 0, param1ArrayOfbyte, param1Int1, arrayOfByte.length);
        this.currentPositionInBlob += arrayOfByte.length;
        return arrayOfByte.length;
      } catch (SQLException sQLException) {
        throw new IOException(sQLException.toString());
      } 
    }
  }
}
