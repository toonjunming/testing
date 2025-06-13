package com.mysql.jdbc;

import java.sql.SQLException;

public class RowDataDynamic implements RowData {
  private int columnCount;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private int index = -1;
  
  private MysqlIO io;
  
  private boolean isAfterEnd = false;
  
  private boolean isBinaryEncoded = false;
  
  private Field[] metadata;
  
  private boolean moreResultsExisted;
  
  private ResultSetRow nextRow;
  
  private boolean noMoreRows = false;
  
  private ResultSetImpl owner;
  
  private boolean streamerClosed = false;
  
  private boolean useBufferRowExplicit;
  
  private boolean wasEmpty = false;
  
  public RowDataDynamic(MysqlIO paramMysqlIO, int paramInt, Field[] paramArrayOfField, boolean paramBoolean) throws SQLException {
    this.io = paramMysqlIO;
    this.columnCount = paramInt;
    this.isBinaryEncoded = paramBoolean;
    this.metadata = paramArrayOfField;
    this.exceptionInterceptor = paramMysqlIO.getExceptionInterceptor();
    this.useBufferRowExplicit = MysqlIO.useBufferRowExplicit(this.metadata);
  }
  
  private void nextRecord() throws SQLException {
    try {
      if (!this.noMoreRows) {
        ResultSetRow resultSetRow = this.io.nextRow(this.metadata, this.columnCount, this.isBinaryEncoded, 1007, true, this.useBufferRowExplicit, true, null);
        this.nextRow = resultSetRow;
        if (resultSetRow == null) {
          this.noMoreRows = true;
          this.isAfterEnd = true;
          this.moreResultsExisted = this.io.tackOnMoreStreamingResults(this.owner);
          if (this.index == -1)
            this.wasEmpty = true; 
        } 
      } else {
        this.nextRow = null;
        this.isAfterEnd = true;
      } 
      return;
    } catch (SQLException sQLException) {
      if (sQLException instanceof StreamingNotifiable)
        ((StreamingNotifiable)sQLException).setWasStreamingResults(); 
      this.noMoreRows = true;
      throw sQLException;
    } catch (Exception exception) {
      String str1 = exception.getClass().getName();
      String str3 = exception.getMessage();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str3);
      stringBuilder1.append(Messages.getString("RowDataDynamic.7"));
      String str2 = stringBuilder1.toString();
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str2);
      stringBuilder2.append(Util.stackTraceToString(exception));
      str2 = stringBuilder2.toString();
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(Messages.getString("RowDataDynamic.8"));
      stringBuilder2.append(str1);
      stringBuilder2.append(Messages.getString("RowDataDynamic.9"));
      stringBuilder2.append(str2);
      SQLException sQLException = SQLError.createSQLException(stringBuilder2.toString(), "S1000", this.exceptionInterceptor);
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void notSupported() throws SQLException {
    throw new OperationNotSupportedException();
  }
  
  public void addRow(ResultSetRow paramResultSetRow) throws SQLException {
    notSupported();
  }
  
  public void afterLast() throws SQLException {
    notSupported();
  }
  
  public void beforeFirst() throws SQLException {
    notSupported();
  }
  
  public void beforeLast() throws SQLException {
    notSupported();
  }
  
  public void close() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield owner : Lcom/mysql/jdbc/ResultSetImpl;
    //   4: astore #7
    //   6: aload #7
    //   8: ifnull -> 41
    //   11: aload #7
    //   13: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   16: astore #8
    //   18: aload #8
    //   20: ifnull -> 35
    //   23: aload #8
    //   25: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   30: astore #7
    //   32: goto -> 47
    //   35: aload_0
    //   36: astore #7
    //   38: goto -> 47
    //   41: aload_0
    //   42: astore #7
    //   44: aconst_null
    //   45: astore #8
    //   47: aload #7
    //   49: monitorenter
    //   50: iconst_0
    //   51: istore_1
    //   52: iconst_0
    //   53: istore_2
    //   54: aload_0
    //   55: invokevirtual next : ()Lcom/mysql/jdbc/ResultSetRow;
    //   58: ifnull -> 79
    //   61: iinc #1, 1
    //   64: iload_1
    //   65: bipush #100
    //   67: irem
    //   68: ifne -> 74
    //   71: invokestatic yield : ()V
    //   74: iconst_1
    //   75: istore_2
    //   76: goto -> 54
    //   79: aload #8
    //   81: ifnull -> 456
    //   84: aload #8
    //   86: invokeinterface getClobberStreamingResults : ()Z
    //   91: ifne -> 241
    //   94: aload #8
    //   96: invokeinterface getNetTimeoutForStreamingResults : ()I
    //   101: ifle -> 241
    //   104: aload #8
    //   106: ldc 'net_write_timeout'
    //   108: invokeinterface getServerVariable : (Ljava/lang/String;)Ljava/lang/String;
    //   113: astore #10
    //   115: aload #10
    //   117: ifnull -> 132
    //   120: aload #10
    //   122: astore #9
    //   124: aload #10
    //   126: invokevirtual length : ()I
    //   129: ifne -> 136
    //   132: ldc '60'
    //   134: astore #9
    //   136: aload_0
    //   137: getfield io : Lcom/mysql/jdbc/MysqlIO;
    //   140: invokevirtual clearInputStream : ()V
    //   143: aload #8
    //   145: invokeinterface createStatement : ()Ljava/sql/Statement;
    //   150: astore #10
    //   152: aload #10
    //   154: checkcast com/mysql/jdbc/StatementImpl
    //   157: astore #12
    //   159: new java/lang/StringBuilder
    //   162: astore #11
    //   164: aload #11
    //   166: invokespecial <init> : ()V
    //   169: aload #11
    //   171: ldc 'SET net_write_timeout='
    //   173: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: pop
    //   177: aload #11
    //   179: aload #9
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload #12
    //   187: aload #8
    //   189: aload #11
    //   191: invokevirtual toString : ()Ljava/lang/String;
    //   194: invokevirtual executeSimpleNonQuery : (Lcom/mysql/jdbc/MySQLConnection;Ljava/lang/String;)V
    //   197: aload #10
    //   199: ifnull -> 241
    //   202: aload #10
    //   204: invokeinterface close : ()V
    //   209: goto -> 241
    //   212: astore #8
    //   214: aload #10
    //   216: astore #9
    //   218: goto -> 226
    //   221: astore #8
    //   223: aconst_null
    //   224: astore #9
    //   226: aload #9
    //   228: ifnull -> 238
    //   231: aload #9
    //   233: invokeinterface close : ()V
    //   238: aload #8
    //   240: athrow
    //   241: aload #8
    //   243: invokeinterface getUseUsageAdvisor : ()Z
    //   248: ifeq -> 456
    //   251: iload_2
    //   252: ifeq -> 456
    //   255: aload #8
    //   257: invokestatic getInstance : (Lcom/mysql/jdbc/MySQLConnection;)Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   260: astore #9
    //   262: new com/mysql/jdbc/profiler/ProfilerEvent
    //   265: astore #10
    //   267: aload_0
    //   268: getfield owner : Lcom/mysql/jdbc/ResultSetImpl;
    //   271: astore #11
    //   273: aload #11
    //   275: getfield owningStatement : Lcom/mysql/jdbc/StatementImpl;
    //   278: astore #12
    //   280: aload #12
    //   282: ifnonnull -> 292
    //   285: ldc 'N/A'
    //   287: astore #8
    //   289: goto -> 299
    //   292: aload #12
    //   294: getfield currentCatalog : Ljava/lang/String;
    //   297: astore #8
    //   299: aload #11
    //   301: getfield connectionId : J
    //   304: lstore #5
    //   306: aload #12
    //   308: ifnonnull -> 316
    //   311: iconst_m1
    //   312: istore_2
    //   313: goto -> 322
    //   316: aload #12
    //   318: invokevirtual getId : ()I
    //   321: istore_2
    //   322: invokestatic currentTimeMillis : ()J
    //   325: lstore_3
    //   326: getstatic com/mysql/jdbc/Constants.MILLIS_I18N : Ljava/lang/String;
    //   329: astore #12
    //   331: new java/lang/StringBuilder
    //   334: astore #11
    //   336: aload #11
    //   338: invokespecial <init> : ()V
    //   341: aload #11
    //   343: ldc_w 'RowDataDynamic.2'
    //   346: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   349: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: pop
    //   353: aload #11
    //   355: iload_1
    //   356: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   359: pop
    //   360: aload #11
    //   362: ldc_w 'RowDataDynamic.3'
    //   365: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   368: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   371: pop
    //   372: aload #11
    //   374: ldc_w 'RowDataDynamic.4'
    //   377: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   380: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   383: pop
    //   384: aload #11
    //   386: ldc_w 'RowDataDynamic.5'
    //   389: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   392: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   395: pop
    //   396: aload #11
    //   398: ldc_w 'RowDataDynamic.6'
    //   401: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   404: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   407: pop
    //   408: aload #11
    //   410: aload_0
    //   411: getfield owner : Lcom/mysql/jdbc/ResultSetImpl;
    //   414: getfield pointOfOrigin : Ljava/lang/String;
    //   417: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   420: pop
    //   421: aload #10
    //   423: iconst_0
    //   424: ldc_w ''
    //   427: aload #8
    //   429: lload #5
    //   431: iload_2
    //   432: iconst_m1
    //   433: lload_3
    //   434: lconst_0
    //   435: aload #12
    //   437: aconst_null
    //   438: aconst_null
    //   439: aload #11
    //   441: invokevirtual toString : ()Ljava/lang/String;
    //   444: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   447: aload #9
    //   449: aload #10
    //   451: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   456: aload #7
    //   458: monitorexit
    //   459: aload_0
    //   460: aconst_null
    //   461: putfield metadata : [Lcom/mysql/jdbc/Field;
    //   464: aload_0
    //   465: aconst_null
    //   466: putfield owner : Lcom/mysql/jdbc/ResultSetImpl;
    //   469: return
    //   470: astore #8
    //   472: aload #7
    //   474: monitorexit
    //   475: aload #8
    //   477: athrow
    // Exception table:
    //   from	to	target	type
    //   54	61	470	finally
    //   71	74	470	finally
    //   84	115	470	finally
    //   124	132	470	finally
    //   136	143	470	finally
    //   143	152	221	finally
    //   152	197	212	finally
    //   202	209	470	finally
    //   231	238	470	finally
    //   238	241	470	finally
    //   241	251	470	finally
    //   255	280	470	finally
    //   292	299	470	finally
    //   299	306	470	finally
    //   316	322	470	finally
    //   322	456	470	finally
    //   456	459	470	finally
    //   472	475	470	finally
  }
  
  public ResultSetRow getAt(int paramInt) throws SQLException {
    notSupported();
    return null;
  }
  
  public int getCurrentRowNumber() throws SQLException {
    notSupported();
    return -1;
  }
  
  public ResultSetInternalMethods getOwner() {
    return this.owner;
  }
  
  public boolean hasNext() throws SQLException {
    boolean bool;
    if (this.nextRow != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!bool && !this.streamerClosed) {
      this.io.closeStreamer(this);
      this.streamerClosed = true;
    } 
    return bool;
  }
  
  public boolean isAfterLast() throws SQLException {
    return this.isAfterEnd;
  }
  
  public boolean isBeforeFirst() throws SQLException {
    boolean bool;
    if (this.index < 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDynamic() {
    return true;
  }
  
  public boolean isEmpty() throws SQLException {
    notSupported();
    return false;
  }
  
  public boolean isFirst() throws SQLException {
    notSupported();
    return false;
  }
  
  public boolean isLast() throws SQLException {
    notSupported();
    return false;
  }
  
  public void moveRowRelative(int paramInt) throws SQLException {
    notSupported();
  }
  
  public ResultSetRow next() throws SQLException {
    nextRecord();
    if (this.nextRow == null && !this.streamerClosed && !this.moreResultsExisted) {
      this.io.closeStreamer(this);
      this.streamerClosed = true;
    } 
    ResultSetRow resultSetRow = this.nextRow;
    if (resultSetRow != null) {
      int i = this.index;
      if (i != Integer.MAX_VALUE)
        this.index = i + 1; 
    } 
    return resultSetRow;
  }
  
  public void removeRow(int paramInt) throws SQLException {
    notSupported();
  }
  
  public void setCurrentRow(int paramInt) throws SQLException {
    notSupported();
  }
  
  public void setMetadata(Field[] paramArrayOfField) {
    this.metadata = paramArrayOfField;
  }
  
  public void setOwner(ResultSetImpl paramResultSetImpl) {
    this.owner = paramResultSetImpl;
  }
  
  public int size() {
    return -1;
  }
  
  public boolean wasEmpty() {
    return this.wasEmpty;
  }
}
