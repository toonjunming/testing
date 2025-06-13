package com.mysql.jdbc;

import com.mysql.jdbc.log.LogUtils;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class StatementImpl implements Statement {
  public static final String[] ON_DUPLICATE_KEY_UPDATE_CLAUSE = new String[] { "ON", "DUPLICATE", "KEY", "UPDATE" };
  
  public static final String PING_MARKER = "/* ping */";
  
  public static final byte USES_VARIABLES_FALSE = 0;
  
  public static final byte USES_VARIABLES_TRUE = 1;
  
  public static final byte USES_VARIABLES_UNKNOWN = -1;
  
  public static int statementCounter = 1;
  
  public List<Object> batchedArgs;
  
  public ArrayList<ResultSetRow> batchedGeneratedKeys;
  
  public Object cancelTimeoutMutex = new Object();
  
  public SingleByteCharsetConverter charConverter = null;
  
  public String charEncoding = null;
  
  public boolean clearWarningsCalled;
  
  private boolean closeOnCompletion;
  
  public volatile MySQLConnection connection = null;
  
  public long connectionId = 0L;
  
  public boolean continueBatchOnError;
  
  public String currentCatalog = null;
  
  public boolean doEscapeProcessing;
  
  public ProfilerEventHandler eventSink;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private int fetchSize;
  
  public ResultSetInternalMethods generatedKeysResults;
  
  public boolean holdResultsOpenOverClose;
  
  public boolean isClosed;
  
  private boolean isImplicitlyClosingResults;
  
  private boolean isPoolable;
  
  public long lastInsertId;
  
  public boolean lastQueryIsOnDupKeyUpdate;
  
  private InputStream localInfileInputStream;
  
  public int maxFieldSize;
  
  public int maxRows;
  
  public Set<ResultSetInternalMethods> openResults;
  
  private int originalFetchSize;
  
  private int originalResultSetType;
  
  public boolean pedantic;
  
  public Reference<MySQLConnection> physicalConnection = null;
  
  public PingTarget pingTarget;
  
  public String pointOfOrigin;
  
  public boolean profileSQL;
  
  public int resultSetConcurrency;
  
  public int resultSetType;
  
  public ResultSetInternalMethods results;
  
  public boolean retrieveGeneratedKeys;
  
  public boolean sendFractionalSeconds;
  
  public final AtomicBoolean statementExecuting;
  
  public int statementId;
  
  public int timeoutInMillis;
  
  public long updateCount;
  
  public boolean useLegacyDatetimeCode;
  
  public boolean useUsageAdvisor;
  
  public final boolean version5013OrNewer;
  
  public SQLWarning warningChain;
  
  public boolean wasCancelled = false;
  
  public boolean wasCancelledByTimeout = false;
  
  public StatementImpl(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    int i = 1;
    this.doEscapeProcessing = true;
    this.eventSink = null;
    this.fetchSize = 0;
    this.isClosed = false;
    this.lastInsertId = -1L;
    this.maxFieldSize = MysqlIO.getMaxBuf();
    this.maxRows = -1;
    this.openResults = new HashSet<ResultSetInternalMethods>();
    this.pedantic = false;
    this.profileSQL = false;
    this.results = null;
    this.generatedKeysResults = null;
    this.resultSetConcurrency = 0;
    this.resultSetType = 0;
    this.timeoutInMillis = 0;
    this.updateCount = -1L;
    this.useUsageAdvisor = false;
    this.warningChain = null;
    this.clearWarningsCalled = false;
    this.holdResultsOpenOverClose = false;
    this.batchedGeneratedKeys = null;
    this.retrieveGeneratedKeys = false;
    this.continueBatchOnError = false;
    this.pingTarget = null;
    this.lastQueryIsOnDupKeyUpdate = false;
    this.statementExecuting = new AtomicBoolean(false);
    this.isImplicitlyClosingResults = false;
    this.originalResultSetType = 0;
    this.originalFetchSize = 0;
    this.isPoolable = true;
    this.closeOnCompletion = false;
    if (paramMySQLConnection != null && !paramMySQLConnection.isClosed()) {
      this.connection = paramMySQLConnection;
      this.connectionId = this.connection.getId();
      this.exceptionInterceptor = this.connection.getExceptionInterceptor();
      this.currentCatalog = paramString;
      this.pedantic = this.connection.getPedantic();
      this.continueBatchOnError = this.connection.getContinueBatchOnError();
      this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
      this.sendFractionalSeconds = this.connection.getSendFractionalSeconds();
      this.doEscapeProcessing = this.connection.getEnableEscapeProcessing();
      if (!this.connection.getDontTrackOpenResources())
        this.connection.registerStatement(this); 
      this.maxFieldSize = this.connection.getMaxAllowedPacket();
      int j = this.connection.getDefaultFetchSize();
      if (j != 0)
        setFetchSize(j); 
      if (this.connection.getUseUnicode()) {
        this.charEncoding = this.connection.getEncoding();
        this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
      } 
      j = i;
      if (!this.connection.getProfileSql()) {
        j = i;
        if (!this.connection.getUseUsageAdvisor())
          if (this.connection.getLogSlowQueries()) {
            j = i;
          } else {
            j = 0;
          }  
      } 
      if (this.connection.getAutoGenerateTestcaseScript() || j != 0) {
        i = statementCounter;
        statementCounter = i + 1;
        this.statementId = i;
      } 
      if (j != 0) {
        this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
        this.profileSQL = this.connection.getProfileSql();
        this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
        this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
      } 
      j = this.connection.getMaxRows();
      if (j != -1)
        setMaxRows(j); 
      this.holdResultsOpenOverClose = this.connection.getHoldResultsOpenOverStatementClose();
      this.version5013OrNewer = this.connection.versionMeetsMinimum(5, 0, 13);
      return;
    } 
    throw SQLError.createSQLException(Messages.getString("Statement.0"), "08003", null);
  }
  
  private void checkAndPerformCloseOnCompletionAction() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (isCloseOnCompletion() && !this.connection.getDontTrackOpenResources() && getOpenResultSetCount() == 0) {
          ResultSetInternalMethods resultSetInternalMethods = this.results;
          if (resultSetInternalMethods == null || !resultSetInternalMethods.reallyResult() || this.results.isClosed()) {
            resultSetInternalMethods = this.generatedKeysResults;
            if (resultSetInternalMethods == null || !resultSetInternalMethods.reallyResult() || this.generatedKeysResults.isClosed())
              realClose(false, false); 
          } 
        } 
      } 
    } catch (SQLException sQLException) {}
  }
  
  private ResultSetInternalMethods createResultSetUsingServerFetch(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      PreparedStatement preparedStatement = this.connection.prepareStatement(paramString, this.resultSetType, this.resultSetConcurrency);
      preparedStatement.setFetchSize(this.fetchSize);
      int i = this.maxRows;
      if (i > -1)
        preparedStatement.setMaxRows(i); 
      statementBegins();
      preparedStatement.execute();
      ResultSetInternalMethods resultSetInternalMethods = ((StatementImpl)preparedStatement).getResultSetInternal();
      resultSetInternalMethods.setStatementUsedForFetchingRows((PreparedStatement)preparedStatement);
      this.results = resultSetInternalMethods;
      return resultSetInternalMethods;
    } 
  }
  
  private long[] executeBatchUsingMultiQueries(boolean paramBoolean, int paramInt1, int paramInt2) throws SQLException {
    TimerTask timerTask;
    Statement statement;
    MySQLConnection mySQLConnection = checkClosed();
    Object object = mySQLConnection.getConnectionMutex();
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    if (!paramBoolean)
      try {
        mySQLConnection.getIO().enableMultiQueries();
      } finally {
        Exception exception;
      }  
    try {
      Statement statement1;
      long[] arrayOfLong = new long[paramInt1];
      for (byte b = 0; b < paramInt1; b++)
        arrayOfLong[b] = -3L; 
      StringBuilder stringBuilder = new StringBuilder();
      this();
    } finally {
      Exception exception = null;
      timerTask = null;
    } 
    if (timerTask != null) {
      timerTask.cancel();
      mySQLConnection.getCancelTimer().purge();
    } 
    resetCancelledState();
    if (statement != null)
      try {
        statement.close();
      } finally {
        if (!paramBoolean)
          mySQLConnection.getIO().disableMultiQueries(); 
      }  
    if (!paramBoolean)
      mySQLConnection.getIO().disableMultiQueries(); 
    throw SYNTHETIC_LOCAL_VARIABLE_12;
  }
  
  private boolean executeInternal(String paramString, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_1
    //   1: astore #8
    //   3: aload_0
    //   4: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   7: astore #14
    //   9: aload #14
    //   11: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   16: astore #13
    //   18: aload #13
    //   20: monitorenter
    //   21: aload_0
    //   22: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   25: pop
    //   26: aload_0
    //   27: aload_1
    //   28: invokevirtual checkNullOrEmptyQuery : (Ljava/lang/String;)V
    //   31: aload_0
    //   32: invokevirtual resetCancelledState : ()V
    //   35: aload_0
    //   36: invokevirtual implicitlyCloseAllOpenResults : ()V
    //   39: aload #8
    //   41: iconst_0
    //   42: invokevirtual charAt : (I)C
    //   45: bipush #47
    //   47: if_icmpne -> 69
    //   50: aload #8
    //   52: ldc '/* ping */'
    //   54: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   57: ifeq -> 69
    //   60: aload_0
    //   61: invokevirtual doPingInstead : ()V
    //   64: aload #13
    //   66: monitorexit
    //   67: iconst_1
    //   68: ireturn
    //   69: aload #8
    //   71: aload_1
    //   72: invokestatic findStartOfStatement : (Ljava/lang/String;)I
    //   75: invokestatic firstAlphaCharUc : (Ljava/lang/String;I)C
    //   78: istore_3
    //   79: iload_3
    //   80: bipush #83
    //   82: if_icmpne -> 91
    //   85: iconst_1
    //   86: istore #4
    //   88: goto -> 94
    //   91: iconst_0
    //   92: istore #4
    //   94: aload_0
    //   95: iload_2
    //   96: putfield retrieveGeneratedKeys : Z
    //   99: iload_2
    //   100: ifeq -> 123
    //   103: iload_3
    //   104: bipush #73
    //   106: if_icmpne -> 123
    //   109: aload_0
    //   110: aload_1
    //   111: invokevirtual containsOnDuplicateKeyInString : (Ljava/lang/String;)Z
    //   114: ifeq -> 123
    //   117: iconst_1
    //   118: istore #7
    //   120: goto -> 126
    //   123: iconst_0
    //   124: istore #7
    //   126: aload_0
    //   127: iload #7
    //   129: putfield lastQueryIsOnDupKeyUpdate : Z
    //   132: iload #4
    //   134: ifne -> 195
    //   137: aload #14
    //   139: invokeinterface isReadOnly : ()Z
    //   144: ifne -> 150
    //   147: goto -> 195
    //   150: new java/lang/StringBuilder
    //   153: astore_1
    //   154: aload_1
    //   155: invokespecial <init> : ()V
    //   158: aload_1
    //   159: ldc_w 'Statement.27'
    //   162: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: aload_1
    //   170: ldc_w 'Statement.28'
    //   173: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload_1
    //   181: invokevirtual toString : ()Ljava/lang/String;
    //   184: ldc_w 'S1009'
    //   187: aload_0
    //   188: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   191: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   194: athrow
    //   195: aload #14
    //   197: invokeinterface isReadInfoMsgEnabled : ()Z
    //   202: istore #7
    //   204: iload_2
    //   205: ifeq -> 222
    //   208: iload_3
    //   209: bipush #82
    //   211: if_icmpne -> 222
    //   214: aload #14
    //   216: iconst_1
    //   217: invokeinterface setReadInfoMsgEnabled : (Z)V
    //   222: aload_0
    //   223: aload #14
    //   225: invokevirtual setupStreamingTimeout : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   228: aload #8
    //   230: astore #10
    //   232: aload_0
    //   233: getfield doEscapeProcessing : Z
    //   236: ifeq -> 279
    //   239: aload #8
    //   241: aload #14
    //   243: invokeinterface serverSupportsConvertFn : ()Z
    //   248: aload #14
    //   250: invokestatic escapeSQL : (Ljava/lang/String;ZLcom/mysql/jdbc/MySQLConnection;)Ljava/lang/Object;
    //   253: astore_1
    //   254: aload_1
    //   255: instanceof java/lang/String
    //   258: ifeq -> 270
    //   261: aload_1
    //   262: checkcast java/lang/String
    //   265: astore #10
    //   267: goto -> 279
    //   270: aload_1
    //   271: checkcast com/mysql/jdbc/EscapeProcessorResult
    //   274: getfield escapedSql : Ljava/lang/String;
    //   277: astore #10
    //   279: aconst_null
    //   280: astore_1
    //   281: aconst_null
    //   282: astore #11
    //   284: aconst_null
    //   285: astore #8
    //   287: aload_0
    //   288: aconst_null
    //   289: putfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   292: aload_0
    //   293: invokespecial useServerFetch : ()Z
    //   296: ifeq -> 310
    //   299: aload_0
    //   300: aload #10
    //   302: invokespecial createResultSetUsingServerFetch : (Ljava/lang/String;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   305: astore #8
    //   307: goto -> 779
    //   310: aload #14
    //   312: invokeinterface getEnableQueryTimeouts : ()Z
    //   317: istore_2
    //   318: iload_2
    //   319: ifeq -> 400
    //   322: aload_0
    //   323: getfield timeoutInMillis : I
    //   326: ifeq -> 400
    //   329: aload #14
    //   331: iconst_5
    //   332: iconst_0
    //   333: iconst_0
    //   334: invokeinterface versionMeetsMinimum : (III)Z
    //   339: ifeq -> 400
    //   342: new com/mysql/jdbc/StatementImpl$CancelTask
    //   345: astore_1
    //   346: aload_1
    //   347: aload_0
    //   348: aload_0
    //   349: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   352: aload #14
    //   354: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   359: aload_1
    //   360: aload_0
    //   361: getfield timeoutInMillis : I
    //   364: i2l
    //   365: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   368: goto -> 402
    //   371: astore #8
    //   373: aconst_null
    //   374: astore #10
    //   376: aload_1
    //   377: astore #9
    //   379: aload #8
    //   381: astore_1
    //   382: aload #9
    //   384: astore #8
    //   386: aload #10
    //   388: astore #9
    //   390: goto -> 397
    //   393: astore_1
    //   394: aconst_null
    //   395: astore #9
    //   397: goto -> 1000
    //   400: aconst_null
    //   401: astore_1
    //   402: aload #14
    //   404: invokeinterface getCatalog : ()Ljava/lang/String;
    //   409: aload_0
    //   410: getfield currentCatalog : Ljava/lang/String;
    //   413: invokevirtual equals : (Ljava/lang/Object;)Z
    //   416: istore_2
    //   417: iload_2
    //   418: ifne -> 485
    //   421: aload #14
    //   423: invokeinterface getCatalog : ()Ljava/lang/String;
    //   428: astore #9
    //   430: aload #14
    //   432: aload_0
    //   433: getfield currentCatalog : Ljava/lang/String;
    //   436: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   441: aload #9
    //   443: astore #8
    //   445: goto -> 488
    //   448: astore #8
    //   450: aload_1
    //   451: astore #10
    //   453: aload #8
    //   455: astore_1
    //   456: aload #10
    //   458: astore #8
    //   460: goto -> 397
    //   463: astore #8
    //   465: aconst_null
    //   466: astore #10
    //   468: aload_1
    //   469: astore #9
    //   471: aload #8
    //   473: astore_1
    //   474: aload #9
    //   476: astore #8
    //   478: aload #10
    //   480: astore #9
    //   482: goto -> 1000
    //   485: aconst_null
    //   486: astore #8
    //   488: aload #14
    //   490: invokeinterface getCacheResultSetMetadata : ()Z
    //   495: istore_2
    //   496: iload_2
    //   497: ifeq -> 545
    //   500: aload #14
    //   502: aload #10
    //   504: invokeinterface getCachedMetaData : (Ljava/lang/String;)Lcom/mysql/jdbc/CachedResultSetMetaData;
    //   509: astore #9
    //   511: aload #9
    //   513: ifnull -> 526
    //   516: aload #9
    //   518: getfield fields : [Lcom/mysql/jdbc/Field;
    //   521: astore #12
    //   523: goto -> 551
    //   526: aconst_null
    //   527: astore #12
    //   529: goto -> 551
    //   532: astore #10
    //   534: aload #8
    //   536: astore #9
    //   538: aload #10
    //   540: astore #8
    //   542: goto -> 450
    //   545: aconst_null
    //   546: astore #9
    //   548: aconst_null
    //   549: astore #12
    //   551: iload #4
    //   553: ifeq -> 565
    //   556: aload_0
    //   557: getfield maxRows : I
    //   560: istore #4
    //   562: goto -> 568
    //   565: iconst_m1
    //   566: istore #4
    //   568: aload #14
    //   570: iload #4
    //   572: invokeinterface setSessionMaxRows : (I)V
    //   577: aload_0
    //   578: invokevirtual statementBegins : ()V
    //   581: aload_0
    //   582: getfield maxRows : I
    //   585: istore #6
    //   587: aload_0
    //   588: getfield resultSetType : I
    //   591: istore #5
    //   593: aload_0
    //   594: getfield resultSetConcurrency : I
    //   597: istore #4
    //   599: aload_0
    //   600: invokevirtual createStreamingResultSet : ()Z
    //   603: istore_2
    //   604: aload_0
    //   605: getfield currentCatalog : Ljava/lang/String;
    //   608: astore #15
    //   610: aload #8
    //   612: astore #11
    //   614: aload #14
    //   616: aload_0
    //   617: aload #10
    //   619: iload #6
    //   621: aconst_null
    //   622: iload #5
    //   624: iload #4
    //   626: iload_2
    //   627: aload #15
    //   629: aload #12
    //   631: invokeinterface execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   636: astore #12
    //   638: aload_1
    //   639: ifnull -> 682
    //   642: aload_1
    //   643: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   646: astore #8
    //   648: aload #8
    //   650: ifnonnull -> 664
    //   653: aload_1
    //   654: invokevirtual cancel : ()Z
    //   657: pop
    //   658: aconst_null
    //   659: astore #8
    //   661: goto -> 685
    //   664: aload #8
    //   666: athrow
    //   667: astore #8
    //   669: aload_1
    //   670: astore #9
    //   672: aload #8
    //   674: astore_1
    //   675: aload #9
    //   677: astore #8
    //   679: goto -> 941
    //   682: aload_1
    //   683: astore #8
    //   685: aload_0
    //   686: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   689: astore #15
    //   691: aload #15
    //   693: monitorenter
    //   694: aload_0
    //   695: getfield wasCancelled : Z
    //   698: ifeq -> 733
    //   701: aload_0
    //   702: getfield wasCancelledByTimeout : Z
    //   705: ifeq -> 719
    //   708: new com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   711: astore_1
    //   712: aload_1
    //   713: invokespecial <init> : ()V
    //   716: goto -> 727
    //   719: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   722: dup
    //   723: invokespecial <init> : ()V
    //   726: astore_1
    //   727: aload_0
    //   728: invokevirtual resetCancelledState : ()V
    //   731: aload_1
    //   732: athrow
    //   733: aload #15
    //   735: monitorexit
    //   736: aload #8
    //   738: ifnull -> 758
    //   741: aload #8
    //   743: invokevirtual cancel : ()Z
    //   746: pop
    //   747: aload #14
    //   749: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   754: invokevirtual purge : ()I
    //   757: pop
    //   758: aload #11
    //   760: ifnull -> 772
    //   763: aload #14
    //   765: aload #11
    //   767: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   772: aload #9
    //   774: astore_1
    //   775: aload #12
    //   777: astore #8
    //   779: aload #8
    //   781: ifnull -> 876
    //   784: aload_0
    //   785: aload #8
    //   787: invokeinterface getUpdateID : ()J
    //   792: putfield lastInsertId : J
    //   795: aload_0
    //   796: aload #8
    //   798: putfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   801: aload #8
    //   803: iload_3
    //   804: invokeinterface setFirstCharOfQuery : (C)V
    //   809: aload #8
    //   811: invokeinterface reallyResult : ()Z
    //   816: ifeq -> 876
    //   819: aload_1
    //   820: ifnull -> 840
    //   823: aload #14
    //   825: aload #10
    //   827: aload_1
    //   828: aload_0
    //   829: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   832: invokeinterface initializeResultsMetadataFromCache : (Ljava/lang/String;Lcom/mysql/jdbc/CachedResultSetMetaData;Lcom/mysql/jdbc/ResultSetInternalMethods;)V
    //   837: goto -> 876
    //   840: aload_0
    //   841: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   844: invokeinterface getCacheResultSetMetadata : ()Z
    //   849: ifeq -> 876
    //   852: aload #14
    //   854: aload #10
    //   856: aconst_null
    //   857: aload_0
    //   858: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   861: invokeinterface initializeResultsMetadataFromCache : (Ljava/lang/String;Lcom/mysql/jdbc/CachedResultSetMetaData;Lcom/mysql/jdbc/ResultSetInternalMethods;)V
    //   866: goto -> 876
    //   869: astore_1
    //   870: iload #7
    //   872: istore_2
    //   873: goto -> 1052
    //   876: aload #8
    //   878: ifnull -> 898
    //   881: aload #8
    //   883: invokeinterface reallyResult : ()Z
    //   888: istore_2
    //   889: iload_2
    //   890: ifeq -> 898
    //   893: iconst_1
    //   894: istore_2
    //   895: goto -> 900
    //   898: iconst_0
    //   899: istore_2
    //   900: aload #14
    //   902: iload #7
    //   904: invokeinterface setReadInfoMsgEnabled : (Z)V
    //   909: aload_0
    //   910: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   913: iconst_0
    //   914: invokevirtual set : (Z)V
    //   917: aload #13
    //   919: monitorexit
    //   920: iload_2
    //   921: ireturn
    //   922: astore_1
    //   923: aload #15
    //   925: monitorexit
    //   926: aload_1
    //   927: athrow
    //   928: astore_1
    //   929: aload #11
    //   931: astore #9
    //   933: goto -> 1000
    //   936: astore_1
    //   937: goto -> 923
    //   940: astore_1
    //   941: aload #11
    //   943: astore #9
    //   945: goto -> 1000
    //   948: astore #8
    //   950: aload #11
    //   952: astore #9
    //   954: goto -> 979
    //   957: astore #9
    //   959: aload #8
    //   961: astore #10
    //   963: aload #9
    //   965: astore #8
    //   967: aload #10
    //   969: astore #9
    //   971: goto -> 979
    //   974: astore #8
    //   976: aconst_null
    //   977: astore #9
    //   979: aload_1
    //   980: astore #10
    //   982: aload #8
    //   984: astore_1
    //   985: aload #10
    //   987: astore #8
    //   989: goto -> 1000
    //   992: astore_1
    //   993: aconst_null
    //   994: astore #9
    //   996: aload #11
    //   998: astore #8
    //   1000: iload #7
    //   1002: istore_2
    //   1003: aload #8
    //   1005: ifnull -> 1032
    //   1008: aload #8
    //   1010: invokevirtual cancel : ()Z
    //   1013: pop
    //   1014: aload #14
    //   1016: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   1021: invokevirtual purge : ()I
    //   1024: pop
    //   1025: goto -> 1032
    //   1028: astore_1
    //   1029: goto -> 1052
    //   1032: aload #9
    //   1034: ifnull -> 1046
    //   1037: aload #14
    //   1039: aload #9
    //   1041: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   1046: aload_1
    //   1047: athrow
    //   1048: astore_1
    //   1049: iload #7
    //   1051: istore_2
    //   1052: aload #14
    //   1054: iload #7
    //   1056: invokeinterface setReadInfoMsgEnabled : (Z)V
    //   1061: aload_0
    //   1062: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   1065: iconst_0
    //   1066: invokevirtual set : (Z)V
    //   1069: aload_1
    //   1070: athrow
    //   1071: astore_1
    //   1072: aload #13
    //   1074: monitorexit
    //   1075: aload_1
    //   1076: athrow
    // Exception table:
    //   from	to	target	type
    //   21	67	1071	finally
    //   69	79	1071	finally
    //   94	99	1071	finally
    //   109	117	1071	finally
    //   126	132	1071	finally
    //   137	147	1071	finally
    //   150	195	1071	finally
    //   195	204	1071	finally
    //   214	222	1071	finally
    //   222	228	1048	finally
    //   232	267	1048	finally
    //   270	279	1048	finally
    //   287	307	1048	finally
    //   310	318	992	finally
    //   322	352	393	finally
    //   352	368	371	finally
    //   402	417	974	finally
    //   421	430	463	finally
    //   430	441	448	finally
    //   488	496	957	finally
    //   500	511	532	finally
    //   516	523	532	finally
    //   556	562	532	finally
    //   568	610	957	finally
    //   614	638	948	finally
    //   642	648	667	finally
    //   653	658	667	finally
    //   664	667	667	finally
    //   685	694	940	finally
    //   694	716	922	finally
    //   719	727	922	finally
    //   727	733	922	finally
    //   733	736	922	finally
    //   741	758	869	finally
    //   763	772	869	finally
    //   784	819	869	finally
    //   823	837	869	finally
    //   840	866	869	finally
    //   881	889	869	finally
    //   900	920	1071	finally
    //   923	926	936	finally
    //   926	928	928	finally
    //   1008	1025	1028	finally
    //   1037	1046	1028	finally
    //   1046	1048	1028	finally
    //   1052	1071	1071	finally
    //   1072	1075	1071	finally
  }
  
  public static int findStartOfStatement(String paramString) {
    int i;
    boolean bool = StringUtils.startsWithIgnoreCaseAndWs(paramString, "/*");
    byte b = 0;
    if (bool) {
      i = paramString.indexOf("*/");
      if (i == -1) {
        i = b;
      } else {
        i += 2;
      } 
    } else {
      if (!StringUtils.startsWithIgnoreCaseAndWs(paramString, "--")) {
        int j = b;
        if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "#"))
          j = paramString.indexOf('\n'); 
        return j;
      } 
      i = paramString.indexOf('\n');
    } 
    return i;
  }
  
  public static int getOnDuplicateKeyLocation(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    int i;
    if (paramBoolean1 && !paramBoolean2) {
      i = -1;
    } else {
      Set<StringUtils.SearchMode> set;
      String[] arrayOfString = ON_DUPLICATE_KEY_UPDATE_CLAUSE;
      if (paramBoolean3) {
        set = StringUtils.SEARCH_MODE__MRK_COM_WS;
      } else {
        set = StringUtils.SEARCH_MODE__ALL;
      } 
      i = StringUtils.indexOfIgnoreCase(0, paramString, arrayOfString, "\"'`", "\"'`", set);
    } 
    return i;
  }
  
  private long getRecordCountFromInfo(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramString.length();
    byte b = 0;
    char c = Character.MIN_VALUE;
    while (b < i) {
      c = paramString.charAt(b);
      if (Character.isDigit(c))
        break; 
      b++;
    } 
    stringBuilder.append(c);
    while (++b < i) {
      c = paramString.charAt(b);
      if (!Character.isDigit(c))
        break; 
      stringBuilder.append(c);
    } 
    long l = Long.parseLong(stringBuilder.toString());
    stringBuilder = new StringBuilder();
    while (b < i) {
      c = paramString.charAt(b);
      if (Character.isDigit(c))
        break; 
      b++;
    } 
    stringBuilder.append(c);
    while (++b < i) {
      c = paramString.charAt(b);
      if (!Character.isDigit(c))
        break; 
      stringBuilder.append(c);
    } 
    return l - Long.parseLong(stringBuilder.toString());
  }
  
  private boolean useServerFetch() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool;
      if (this.connection.isCursorFetchEnabled() && this.fetchSize > 0 && this.resultSetConcurrency == 1007 && this.resultSetType == 1003) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public void addBatch(String paramString) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.batchedArgs == null) {
        ArrayList<Object> arrayList = new ArrayList();
        this();
        this.batchedArgs = arrayList;
      } 
      if (paramString != null)
        this.batchedArgs.add(paramString); 
      return;
    } 
  }
  
  public void cancel() throws SQLException {
    if (!this.statementExecuting.get())
      return; 
    if (!this.isClosed && this.connection != null && this.connection.versionMeetsMinimum(5, 0, 0)) {
      Exception exception;
      Statement statement2;
      StringBuilder stringBuilder = null;
      Statement statement1 = null;
      try {
        Connection connection = this.connection.duplicate();
      } finally {
        exception = null;
      } 
      if (statement2 != null)
        statement2.close(); 
      if (statement1 != null)
        statement1.close(); 
      throw exception;
    } 
  }
  
  public MySQLConnection checkClosed() throws SQLException {
    MySQLConnection mySQLConnection = this.connection;
    if (mySQLConnection != null)
      return mySQLConnection; 
    throw SQLError.createSQLException(Messages.getString("Statement.49"), "S1009", getExceptionInterceptor());
  }
  
  public void checkForDml(String paramString, char paramChar) throws SQLException {
    if (paramChar == 'I' || paramChar == 'U' || paramChar == 'D' || paramChar == 'A' || paramChar == 'C' || paramChar == 'T' || paramChar == 'R') {
      paramString = StringUtils.stripComments(paramString, "'\"", "'\"", true, false, true, true);
      if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "INSERT") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "UPDATE") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "DELETE") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "DROP") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "CREATE") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "ALTER") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "TRUNCATE") || StringUtils.startsWithIgnoreCaseAndWs(paramString, "RENAME"))
        throw SQLError.createSQLException(Messages.getString("Statement.57"), "S1009", getExceptionInterceptor()); 
    } 
  }
  
  public void checkNullOrEmptyQuery(String paramString) throws SQLException {
    if (paramString != null) {
      if (paramString.length() != 0)
        return; 
      throw SQLError.createSQLException(Messages.getString("Statement.61"), "S1009", getExceptionInterceptor());
    } 
    throw SQLError.createSQLException(Messages.getString("Statement.59"), "S1009", getExceptionInterceptor());
  }
  
  public void clearBatch() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      List<Object> list = this.batchedArgs;
      if (list != null)
        list.clear(); 
      return;
    } 
  }
  
  public void clearWarnings() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.clearWarningsCalled = true;
      this.warningChain = null;
      return;
    } 
  }
  
  public void close() throws SQLException {
    realClose(true, true);
  }
  
  public void closeAllOpenResults() throws SQLException {
    MySQLConnection mySQLConnection = this.connection;
    if (mySQLConnection == null)
      return; 
    synchronized (mySQLConnection.getConnectionMutex()) {
      Set<ResultSetInternalMethods> set = this.openResults;
      if (set != null) {
        for (ResultSetInternalMethods resultSetInternalMethods : set) {
          try {
            resultSetInternalMethods.realClose(false);
          } catch (SQLException sQLException) {
            AssertionFailedException.shouldNotHappen(sQLException);
          } 
        } 
        this.openResults.clear();
      } 
      return;
    } 
  }
  
  public void closeOnCompletion() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.closeOnCompletion = true;
      return;
    } 
  }
  
  public boolean containsOnDuplicateKeyInString(String paramString) {
    boolean bool;
    if (getOnDuplicateKeyLocation(paramString, this.connection.getDontCheckOnDuplicateKeyUpdateInSQL(), this.connection.getRewriteBatchedStatements(), this.connection.isNoBackslashEscapesSet()) != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean createStreamingResultSet() {
    boolean bool;
    if (this.resultSetType == 1003 && this.resultSetConcurrency == 1007 && this.fetchSize == Integer.MIN_VALUE) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void disableStreamingResults() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.fetchSize == Integer.MIN_VALUE && this.resultSetType == 1003) {
        setFetchSize(this.originalFetchSize);
        setResultSetType(this.originalResultSetType);
      } 
      return;
    } 
  }
  
  public void doPingInstead() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      PingTarget pingTarget = this.pingTarget;
      if (pingTarget != null) {
        pingTarget.doPing();
      } else {
        this.connection.ping();
      } 
      this.results = generatePingResultSet();
      return;
    } 
  }
  
  public void enableStreamingResults() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.originalResultSetType = this.resultSetType;
      this.originalFetchSize = this.fetchSize;
      setFetchSize(-2147483648);
      setResultSetType(1003);
      return;
    } 
  }
  
  public boolean execute(String paramString) throws SQLException {
    return executeInternal(paramString, false);
  }
  
  public boolean execute(String paramString, int paramInt) throws SQLException {
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    return executeInternal(paramString, bool);
  }
  
  public boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
    boolean bool;
    if (paramArrayOfint != null && paramArrayOfint.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return executeInternal(paramString, bool);
  }
  
  public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
    boolean bool;
    if (paramArrayOfString != null && paramArrayOfString.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return executeInternal(paramString, bool);
  }
  
  public int[] executeBatch() throws SQLException {
    return Util.truncateAndConvertToInt(executeBatchInternal());
  }
  
  public long[] executeBatchInternal() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: astore #11
    //   6: aload #11
    //   8: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   13: astore #10
    //   15: aload #10
    //   17: monitorenter
    //   18: aload #11
    //   20: invokeinterface isReadOnly : ()Z
    //   25: ifne -> 781
    //   28: aload_0
    //   29: invokevirtual implicitlyCloseAllOpenResults : ()V
    //   32: aload_0
    //   33: getfield batchedArgs : Ljava/util/List;
    //   36: astore #6
    //   38: aload #6
    //   40: ifnull -> 774
    //   43: aload #6
    //   45: invokeinterface size : ()I
    //   50: ifne -> 56
    //   53: goto -> 774
    //   56: aload_0
    //   57: getfield timeoutInMillis : I
    //   60: istore_3
    //   61: aload_0
    //   62: iconst_0
    //   63: putfield timeoutInMillis : I
    //   66: aload_0
    //   67: invokevirtual resetCancelledState : ()V
    //   70: aload_0
    //   71: invokevirtual statementBegins : ()V
    //   74: aload_0
    //   75: iconst_1
    //   76: putfield retrieveGeneratedKeys : Z
    //   79: aload_0
    //   80: getfield batchedArgs : Ljava/util/List;
    //   83: astore #6
    //   85: aload #6
    //   87: ifnull -> 557
    //   90: aload #6
    //   92: invokeinterface size : ()I
    //   97: istore #4
    //   99: new java/util/ArrayList
    //   102: astore #6
    //   104: aload #6
    //   106: aload_0
    //   107: getfield batchedArgs : Ljava/util/List;
    //   110: invokeinterface size : ()I
    //   115: invokespecial <init> : (I)V
    //   118: aload_0
    //   119: aload #6
    //   121: putfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   124: aload #11
    //   126: invokeinterface getAllowMultiQueries : ()Z
    //   131: istore #5
    //   133: aload #11
    //   135: iconst_4
    //   136: iconst_1
    //   137: iconst_1
    //   138: invokeinterface versionMeetsMinimum : (III)Z
    //   143: ifeq -> 205
    //   146: iload #5
    //   148: ifne -> 167
    //   151: aload #11
    //   153: invokeinterface getRewriteBatchedStatements : ()Z
    //   158: ifeq -> 205
    //   161: iload #4
    //   163: iconst_4
    //   164: if_icmple -> 205
    //   167: aload_0
    //   168: iload #5
    //   170: iload #4
    //   172: iload_3
    //   173: invokespecial executeBatchUsingMultiQueries : (ZII)[J
    //   176: astore #6
    //   178: aload_0
    //   179: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   182: iconst_0
    //   183: invokevirtual set : (Z)V
    //   186: aload_0
    //   187: invokevirtual resetCancelledState : ()V
    //   190: aload_0
    //   191: iload_3
    //   192: putfield timeoutInMillis : I
    //   195: aload_0
    //   196: invokevirtual clearBatch : ()V
    //   199: aload #10
    //   201: monitorexit
    //   202: aload #6
    //   204: areturn
    //   205: aload #11
    //   207: invokeinterface getEnableQueryTimeouts : ()Z
    //   212: ifeq -> 264
    //   215: iload_3
    //   216: ifeq -> 264
    //   219: aload #11
    //   221: iconst_5
    //   222: iconst_0
    //   223: iconst_0
    //   224: invokeinterface versionMeetsMinimum : (III)Z
    //   229: ifeq -> 264
    //   232: new com/mysql/jdbc/StatementImpl$CancelTask
    //   235: dup
    //   236: aload_0
    //   237: aload_0
    //   238: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   241: astore #7
    //   243: aload #7
    //   245: astore #6
    //   247: aload #11
    //   249: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   254: aload #7
    //   256: iload_3
    //   257: i2l
    //   258: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   261: goto -> 267
    //   264: aconst_null
    //   265: astore #7
    //   267: aload #7
    //   269: astore #6
    //   271: iload #4
    //   273: newarray long
    //   275: astore #9
    //   277: iconst_0
    //   278: istore_1
    //   279: iload_1
    //   280: iload #4
    //   282: if_icmpge -> 298
    //   285: aload #9
    //   287: iload_1
    //   288: ldc2_w -3
    //   291: lastore
    //   292: iinc #1, 1
    //   295: goto -> 279
    //   298: aconst_null
    //   299: astore #8
    //   301: iconst_0
    //   302: istore_1
    //   303: iload_1
    //   304: iload #4
    //   306: if_icmpge -> 529
    //   309: aload #7
    //   311: astore #6
    //   313: aload_0
    //   314: getfield batchedArgs : Ljava/util/List;
    //   317: iload_1
    //   318: invokeinterface get : (I)Ljava/lang/Object;
    //   323: checkcast java/lang/String
    //   326: astore #12
    //   328: aload #7
    //   330: astore #6
    //   332: aload #9
    //   334: iload_1
    //   335: aload_0
    //   336: aload #12
    //   338: iconst_1
    //   339: iconst_1
    //   340: invokevirtual executeUpdateInternal : (Ljava/lang/String;ZZ)J
    //   343: lastore
    //   344: aload #7
    //   346: astore #6
    //   348: aload_0
    //   349: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   352: invokeinterface getFirstCharOfQuery : ()C
    //   357: bipush #73
    //   359: if_icmpne -> 380
    //   362: aload #7
    //   364: astore #6
    //   366: aload_0
    //   367: aload #12
    //   369: invokevirtual containsOnDuplicateKeyInString : (Ljava/lang/String;)Z
    //   372: ifeq -> 380
    //   375: iconst_1
    //   376: istore_2
    //   377: goto -> 382
    //   380: iconst_0
    //   381: istore_2
    //   382: aload #7
    //   384: astore #6
    //   386: aload_0
    //   387: iload_2
    //   388: invokevirtual getBatchedGeneratedKeys : (I)V
    //   391: goto -> 451
    //   394: astore #8
    //   396: aload #9
    //   398: iload_1
    //   399: ldc2_w -3
    //   402: lastore
    //   403: aload #7
    //   405: astore #6
    //   407: aload_0
    //   408: getfield continueBatchOnError : Z
    //   411: ifeq -> 457
    //   414: aload #7
    //   416: astore #6
    //   418: aload #8
    //   420: instanceof com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   423: ifne -> 457
    //   426: aload #7
    //   428: astore #6
    //   430: aload #8
    //   432: instanceof com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   435: ifne -> 457
    //   438: aload #7
    //   440: astore #6
    //   442: aload_0
    //   443: aload #8
    //   445: invokevirtual hasDeadlockOrTimeoutRolledBackTx : (Ljava/sql/SQLException;)Z
    //   448: ifne -> 457
    //   451: iinc #1, 1
    //   454: goto -> 303
    //   457: aload #7
    //   459: astore #6
    //   461: iload_1
    //   462: newarray long
    //   464: astore #12
    //   466: aload #7
    //   468: astore #6
    //   470: aload_0
    //   471: aload #8
    //   473: invokevirtual hasDeadlockOrTimeoutRolledBackTx : (Ljava/sql/SQLException;)Z
    //   476: ifeq -> 499
    //   479: iconst_0
    //   480: istore_2
    //   481: iload_2
    //   482: iload_1
    //   483: if_icmpge -> 513
    //   486: aload #12
    //   488: iload_2
    //   489: ldc2_w -3
    //   492: lastore
    //   493: iinc #2, 1
    //   496: goto -> 481
    //   499: aload #7
    //   501: astore #6
    //   503: aload #9
    //   505: iconst_0
    //   506: aload #12
    //   508: iconst_0
    //   509: iload_1
    //   510: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   513: aload #7
    //   515: astore #6
    //   517: aload #8
    //   519: aload #12
    //   521: aload_0
    //   522: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   525: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   528: athrow
    //   529: aload #8
    //   531: ifnonnull -> 541
    //   534: aload #9
    //   536: astore #8
    //   538: goto -> 563
    //   541: aload #7
    //   543: astore #6
    //   545: aload #8
    //   547: aload #9
    //   549: aload_0
    //   550: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   553: invokestatic createBatchUpdateException : (Ljava/sql/SQLException;[JLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   556: athrow
    //   557: aconst_null
    //   558: astore #7
    //   560: aconst_null
    //   561: astore #8
    //   563: aload #7
    //   565: ifnull -> 627
    //   568: aload #7
    //   570: astore #6
    //   572: aload #7
    //   574: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   577: astore #9
    //   579: aload #9
    //   581: ifnonnull -> 615
    //   584: aload #7
    //   586: astore #6
    //   588: aload #7
    //   590: invokevirtual cancel : ()Z
    //   593: pop
    //   594: aload #7
    //   596: astore #6
    //   598: aload #11
    //   600: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   605: invokevirtual purge : ()I
    //   608: pop
    //   609: aconst_null
    //   610: astore #6
    //   612: goto -> 631
    //   615: aload #7
    //   617: astore #6
    //   619: aload #9
    //   621: athrow
    //   622: astore #8
    //   624: goto -> 707
    //   627: aload #7
    //   629: astore #6
    //   631: aload #8
    //   633: ifnull -> 639
    //   636: goto -> 644
    //   639: iconst_0
    //   640: newarray long
    //   642: astore #8
    //   644: aload #6
    //   646: astore #7
    //   648: aload_0
    //   649: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   652: iconst_0
    //   653: invokevirtual set : (Z)V
    //   656: aload #6
    //   658: ifnull -> 678
    //   661: aload #6
    //   663: invokevirtual cancel : ()Z
    //   666: pop
    //   667: aload #11
    //   669: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   674: invokevirtual purge : ()I
    //   677: pop
    //   678: aload_0
    //   679: invokevirtual resetCancelledState : ()V
    //   682: aload_0
    //   683: iload_3
    //   684: putfield timeoutInMillis : I
    //   687: aload_0
    //   688: invokevirtual clearBatch : ()V
    //   691: aload #10
    //   693: monitorexit
    //   694: aload #8
    //   696: areturn
    //   697: astore #8
    //   699: goto -> 707
    //   702: astore #8
    //   704: aconst_null
    //   705: astore #6
    //   707: aload #6
    //   709: astore #7
    //   711: aload_0
    //   712: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   715: iconst_0
    //   716: invokevirtual set : (Z)V
    //   719: aload #6
    //   721: astore #7
    //   723: aload #8
    //   725: athrow
    //   726: astore #6
    //   728: goto -> 736
    //   731: astore #6
    //   733: aconst_null
    //   734: astore #7
    //   736: aload #7
    //   738: ifnull -> 758
    //   741: aload #7
    //   743: invokevirtual cancel : ()Z
    //   746: pop
    //   747: aload #11
    //   749: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   754: invokevirtual purge : ()I
    //   757: pop
    //   758: aload_0
    //   759: invokevirtual resetCancelledState : ()V
    //   762: aload_0
    //   763: iload_3
    //   764: putfield timeoutInMillis : I
    //   767: aload_0
    //   768: invokevirtual clearBatch : ()V
    //   771: aload #6
    //   773: athrow
    //   774: aload #10
    //   776: monitorexit
    //   777: iconst_0
    //   778: newarray long
    //   780: areturn
    //   781: new java/lang/StringBuilder
    //   784: astore #6
    //   786: aload #6
    //   788: invokespecial <init> : ()V
    //   791: aload #6
    //   793: ldc_w 'Statement.34'
    //   796: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   799: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   802: pop
    //   803: aload #6
    //   805: ldc_w 'Statement.35'
    //   808: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   811: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   814: pop
    //   815: aload #6
    //   817: invokevirtual toString : ()Ljava/lang/String;
    //   820: ldc_w 'S1009'
    //   823: aload_0
    //   824: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   827: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   830: athrow
    //   831: astore #6
    //   833: aload #10
    //   835: monitorexit
    //   836: aload #6
    //   838: athrow
    // Exception table:
    //   from	to	target	type
    //   18	38	831	finally
    //   43	53	831	finally
    //   56	66	831	finally
    //   66	74	731	finally
    //   74	85	702	finally
    //   90	146	702	finally
    //   151	161	702	finally
    //   167	178	702	finally
    //   178	186	731	finally
    //   186	202	831	finally
    //   205	215	702	finally
    //   219	243	702	finally
    //   247	261	622	finally
    //   271	277	622	finally
    //   313	328	394	java/sql/SQLException
    //   313	328	622	finally
    //   332	344	394	java/sql/SQLException
    //   332	344	622	finally
    //   348	362	394	java/sql/SQLException
    //   348	362	622	finally
    //   366	375	394	java/sql/SQLException
    //   366	375	622	finally
    //   386	391	394	java/sql/SQLException
    //   386	391	622	finally
    //   407	414	622	finally
    //   418	426	622	finally
    //   430	438	622	finally
    //   442	451	622	finally
    //   461	466	622	finally
    //   470	479	622	finally
    //   503	513	622	finally
    //   517	529	622	finally
    //   545	557	622	finally
    //   572	579	622	finally
    //   588	594	622	finally
    //   598	609	622	finally
    //   619	622	622	finally
    //   639	644	697	finally
    //   648	656	726	finally
    //   661	678	831	finally
    //   678	694	831	finally
    //   711	719	726	finally
    //   723	726	726	finally
    //   741	758	831	finally
    //   758	774	831	finally
    //   774	781	831	finally
    //   781	831	831	finally
    //   833	836	831	finally
  }
  
  public long[] executeLargeBatch() throws SQLException {
    return executeBatchInternal();
  }
  
  public long executeLargeUpdate(String paramString) throws SQLException {
    return executeUpdateInternal(paramString, false, false);
  }
  
  public long executeLargeUpdate(String paramString, int paramInt) throws SQLException {
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    return executeUpdateInternal(paramString, false, bool);
  }
  
  public long executeLargeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
    boolean bool;
    if (paramArrayOfint != null && paramArrayOfint.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return executeUpdateInternal(paramString, false, bool);
  }
  
  public long executeLargeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
    boolean bool;
    if (paramArrayOfString != null && paramArrayOfString.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return executeUpdateInternal(paramString, false, bool);
  }
  
  public ResultSet executeQuery(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_1
    //   1: astore #6
    //   3: aload_0
    //   4: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   7: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   12: astore #10
    //   14: aload #10
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   21: astore #11
    //   23: aload_0
    //   24: iconst_0
    //   25: putfield retrieveGeneratedKeys : Z
    //   28: aload_0
    //   29: aload_1
    //   30: invokevirtual checkNullOrEmptyQuery : (Ljava/lang/String;)V
    //   33: aload_0
    //   34: invokevirtual resetCancelledState : ()V
    //   37: aload_0
    //   38: invokevirtual implicitlyCloseAllOpenResults : ()V
    //   41: aload #6
    //   43: iconst_0
    //   44: invokevirtual charAt : (I)C
    //   47: bipush #47
    //   49: if_icmpne -> 76
    //   52: aload #6
    //   54: ldc '/* ping */'
    //   56: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   59: ifeq -> 76
    //   62: aload_0
    //   63: invokevirtual doPingInstead : ()V
    //   66: aload_0
    //   67: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   70: astore_1
    //   71: aload #10
    //   73: monitorexit
    //   74: aload_1
    //   75: areturn
    //   76: aload_0
    //   77: aload #11
    //   79: invokevirtual setupStreamingTimeout : (Lcom/mysql/jdbc/MySQLConnection;)V
    //   82: aload #6
    //   84: astore #7
    //   86: aload_0
    //   87: getfield doEscapeProcessing : Z
    //   90: ifeq -> 135
    //   93: aload #6
    //   95: aload #11
    //   97: invokeinterface serverSupportsConvertFn : ()Z
    //   102: aload_0
    //   103: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   106: invokestatic escapeSQL : (Ljava/lang/String;ZLcom/mysql/jdbc/MySQLConnection;)Ljava/lang/Object;
    //   109: astore_1
    //   110: aload_1
    //   111: instanceof java/lang/String
    //   114: ifeq -> 126
    //   117: aload_1
    //   118: checkcast java/lang/String
    //   121: astore #7
    //   123: goto -> 135
    //   126: aload_1
    //   127: checkcast com/mysql/jdbc/EscapeProcessorResult
    //   130: getfield escapedSql : Ljava/lang/String;
    //   133: astore #7
    //   135: aload_0
    //   136: aload #7
    //   138: aload #7
    //   140: aload #7
    //   142: invokestatic findStartOfStatement : (Ljava/lang/String;)I
    //   145: invokestatic firstAlphaCharUc : (Ljava/lang/String;I)C
    //   148: invokevirtual checkForDml : (Ljava/lang/String;C)V
    //   151: aload_0
    //   152: invokespecial useServerFetch : ()Z
    //   155: ifeq -> 175
    //   158: aload_0
    //   159: aload #7
    //   161: invokespecial createResultSetUsingServerFetch : (Ljava/lang/String;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   164: astore_1
    //   165: aload_0
    //   166: aload_1
    //   167: putfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   170: aload #10
    //   172: monitorexit
    //   173: aload_1
    //   174: areturn
    //   175: aload #11
    //   177: invokeinterface getEnableQueryTimeouts : ()Z
    //   182: istore #5
    //   184: iload #5
    //   186: ifeq -> 262
    //   189: aload_0
    //   190: getfield timeoutInMillis : I
    //   193: ifeq -> 262
    //   196: aload #11
    //   198: iconst_5
    //   199: iconst_0
    //   200: iconst_0
    //   201: invokeinterface versionMeetsMinimum : (III)Z
    //   206: ifeq -> 262
    //   209: new com/mysql/jdbc/StatementImpl$CancelTask
    //   212: astore_1
    //   213: aload_1
    //   214: aload_0
    //   215: aload_0
    //   216: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   219: aload #11
    //   221: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   226: aload_1
    //   227: aload_0
    //   228: getfield timeoutInMillis : I
    //   231: i2l
    //   232: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   235: aload_1
    //   236: astore #6
    //   238: goto -> 265
    //   241: astore #6
    //   243: aload_1
    //   244: astore #7
    //   246: aload #6
    //   248: astore_1
    //   249: goto -> 337
    //   252: astore_1
    //   253: aconst_null
    //   254: astore #6
    //   256: aconst_null
    //   257: astore #7
    //   259: goto -> 787
    //   262: aconst_null
    //   263: astore #6
    //   265: aload #11
    //   267: invokeinterface getCatalog : ()Ljava/lang/String;
    //   272: aload_0
    //   273: getfield currentCatalog : Ljava/lang/String;
    //   276: invokevirtual equals : (Ljava/lang/Object;)Z
    //   279: istore #5
    //   281: iload #5
    //   283: ifne -> 343
    //   286: aload #11
    //   288: invokeinterface getCatalog : ()Ljava/lang/String;
    //   293: astore #8
    //   295: aload #11
    //   297: aload_0
    //   298: getfield currentCatalog : Ljava/lang/String;
    //   301: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   306: aload #8
    //   308: astore_1
    //   309: goto -> 345
    //   312: astore_1
    //   313: aload #8
    //   315: astore #7
    //   317: aload #6
    //   319: astore #8
    //   321: aload #7
    //   323: astore #6
    //   325: aload #8
    //   327: astore #7
    //   329: goto -> 787
    //   332: astore_1
    //   333: aload #6
    //   335: astore #7
    //   337: aconst_null
    //   338: astore #6
    //   340: goto -> 787
    //   343: aconst_null
    //   344: astore_1
    //   345: aload #11
    //   347: invokeinterface getCacheResultSetMetadata : ()Z
    //   352: istore #5
    //   354: iload #5
    //   356: ifeq -> 399
    //   359: aload #11
    //   361: aload #7
    //   363: invokeinterface getCachedMetaData : (Ljava/lang/String;)Lcom/mysql/jdbc/CachedResultSetMetaData;
    //   368: astore #8
    //   370: aload #8
    //   372: ifnull -> 385
    //   375: aload #8
    //   377: getfield fields : [Lcom/mysql/jdbc/Field;
    //   380: astore #9
    //   382: goto -> 405
    //   385: goto -> 402
    //   388: astore #8
    //   390: aload_1
    //   391: astore #7
    //   393: aload #8
    //   395: astore_1
    //   396: goto -> 317
    //   399: aconst_null
    //   400: astore #8
    //   402: aconst_null
    //   403: astore #9
    //   405: aload #11
    //   407: aload_0
    //   408: getfield maxRows : I
    //   411: invokeinterface setSessionMaxRows : (I)V
    //   416: aload_0
    //   417: invokevirtual statementBegins : ()V
    //   420: aload_0
    //   421: getfield maxRows : I
    //   424: istore #4
    //   426: aload_0
    //   427: getfield resultSetType : I
    //   430: istore_3
    //   431: aload_0
    //   432: getfield resultSetConcurrency : I
    //   435: istore_2
    //   436: aload_0
    //   437: invokevirtual createStreamingResultSet : ()Z
    //   440: istore #5
    //   442: aload_0
    //   443: getfield currentCatalog : Ljava/lang/String;
    //   446: astore #12
    //   448: aload_0
    //   449: aload #11
    //   451: aload_0
    //   452: aload #7
    //   454: iload #4
    //   456: aconst_null
    //   457: iload_3
    //   458: iload_2
    //   459: iload #5
    //   461: aload #12
    //   463: aload #9
    //   465: invokeinterface execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   470: putfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   473: aload #6
    //   475: ifnull -> 531
    //   478: aload #6
    //   480: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   483: astore #9
    //   485: aload #9
    //   487: ifnonnull -> 513
    //   490: aload #6
    //   492: invokevirtual cancel : ()Z
    //   495: pop
    //   496: aload #11
    //   498: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   503: invokevirtual purge : ()I
    //   506: pop
    //   507: aconst_null
    //   508: astore #6
    //   510: goto -> 531
    //   513: aload #9
    //   515: athrow
    //   516: astore #8
    //   518: aload #6
    //   520: astore #7
    //   522: aload_1
    //   523: astore #6
    //   525: aload #8
    //   527: astore_1
    //   528: goto -> 787
    //   531: aload_0
    //   532: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   535: astore #9
    //   537: aload #9
    //   539: monitorenter
    //   540: aload_0
    //   541: getfield wasCancelled : Z
    //   544: ifeq -> 583
    //   547: aload_0
    //   548: getfield wasCancelledByTimeout : Z
    //   551: ifeq -> 567
    //   554: new com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   557: astore #7
    //   559: aload #7
    //   561: invokespecial <init> : ()V
    //   564: goto -> 576
    //   567: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   570: dup
    //   571: invokespecial <init> : ()V
    //   574: astore #7
    //   576: aload_0
    //   577: invokevirtual resetCancelledState : ()V
    //   580: aload #7
    //   582: athrow
    //   583: aload #9
    //   585: monitorexit
    //   586: aload_0
    //   587: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   590: iconst_0
    //   591: invokevirtual set : (Z)V
    //   594: aload #6
    //   596: ifnull -> 616
    //   599: aload #6
    //   601: invokevirtual cancel : ()Z
    //   604: pop
    //   605: aload #11
    //   607: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   612: invokevirtual purge : ()I
    //   615: pop
    //   616: aload_1
    //   617: ifnull -> 628
    //   620: aload #11
    //   622: aload_1
    //   623: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   628: aload_0
    //   629: aload_0
    //   630: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   633: invokeinterface getUpdateID : ()J
    //   638: putfield lastInsertId : J
    //   641: aload #8
    //   643: ifnull -> 664
    //   646: aload #11
    //   648: aload #7
    //   650: aload #8
    //   652: aload_0
    //   653: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   656: invokeinterface initializeResultsMetadataFromCache : (Ljava/lang/String;Lcom/mysql/jdbc/CachedResultSetMetaData;Lcom/mysql/jdbc/ResultSetInternalMethods;)V
    //   661: goto -> 690
    //   664: aload_0
    //   665: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   668: invokeinterface getCacheResultSetMetadata : ()Z
    //   673: ifeq -> 690
    //   676: aload #11
    //   678: aload #7
    //   680: aconst_null
    //   681: aload_0
    //   682: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   685: invokeinterface initializeResultsMetadataFromCache : (Ljava/lang/String;Lcom/mysql/jdbc/CachedResultSetMetaData;Lcom/mysql/jdbc/ResultSetInternalMethods;)V
    //   690: aload_0
    //   691: getfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   694: astore_1
    //   695: aload #10
    //   697: monitorexit
    //   698: aload_1
    //   699: areturn
    //   700: astore #8
    //   702: aload_1
    //   703: astore #7
    //   705: aload #9
    //   707: monitorexit
    //   708: aload #8
    //   710: athrow
    //   711: astore_1
    //   712: goto -> 317
    //   715: astore #8
    //   717: goto -> 705
    //   720: astore #7
    //   722: aload_1
    //   723: astore #8
    //   725: aload #7
    //   727: astore_1
    //   728: aload #8
    //   730: astore #7
    //   732: goto -> 317
    //   735: astore #7
    //   737: aload_1
    //   738: astore #8
    //   740: aload #7
    //   742: astore_1
    //   743: aload #8
    //   745: astore #7
    //   747: goto -> 765
    //   750: astore #8
    //   752: aload_1
    //   753: astore #7
    //   755: aload #8
    //   757: astore_1
    //   758: goto -> 765
    //   761: astore_1
    //   762: aconst_null
    //   763: astore #7
    //   765: aload #6
    //   767: astore #8
    //   769: aload #7
    //   771: astore #6
    //   773: aload #8
    //   775: astore #7
    //   777: goto -> 787
    //   780: astore_1
    //   781: aconst_null
    //   782: astore #6
    //   784: aconst_null
    //   785: astore #7
    //   787: aload_0
    //   788: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   791: iconst_0
    //   792: invokevirtual set : (Z)V
    //   795: aload #7
    //   797: ifnull -> 817
    //   800: aload #7
    //   802: invokevirtual cancel : ()Z
    //   805: pop
    //   806: aload #11
    //   808: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   813: invokevirtual purge : ()I
    //   816: pop
    //   817: aload #6
    //   819: ifnull -> 831
    //   822: aload #11
    //   824: aload #6
    //   826: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   831: aload_1
    //   832: athrow
    //   833: astore_1
    //   834: aload #10
    //   836: monitorexit
    //   837: aload_1
    //   838: athrow
    // Exception table:
    //   from	to	target	type
    //   17	74	833	finally
    //   76	82	833	finally
    //   86	123	833	finally
    //   126	135	833	finally
    //   135	173	833	finally
    //   175	184	780	finally
    //   189	219	252	finally
    //   219	235	241	finally
    //   265	281	761	finally
    //   286	295	332	finally
    //   295	306	312	finally
    //   345	354	750	finally
    //   359	370	388	finally
    //   375	382	388	finally
    //   405	448	750	finally
    //   448	473	735	finally
    //   478	485	516	finally
    //   490	507	516	finally
    //   513	516	516	finally
    //   531	540	720	finally
    //   540	564	700	finally
    //   567	576	700	finally
    //   576	583	700	finally
    //   583	586	700	finally
    //   586	594	833	finally
    //   599	616	833	finally
    //   620	628	833	finally
    //   628	641	833	finally
    //   646	661	833	finally
    //   664	690	833	finally
    //   690	698	833	finally
    //   705	708	715	finally
    //   708	711	711	finally
    //   787	795	833	finally
    //   800	817	833	finally
    //   822	831	833	finally
    //   831	833	833	finally
    //   834	837	833	finally
  }
  
  public void executeSimpleNonQuery(MySQLConnection paramMySQLConnection, String paramString) throws SQLException {
    paramMySQLConnection.execSQL(this, paramString, -1, null, 1003, 1007, false, this.currentCatalog, null, false).close();
  }
  
  public int executeUpdate(String paramString) throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeUpdate(paramString));
  }
  
  public int executeUpdate(String paramString, int paramInt) throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeUpdate(paramString, paramInt));
  }
  
  public int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeUpdate(paramString, paramArrayOfint));
  }
  
  public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
    return Util.truncateAndConvertToInt(executeLargeUpdate(paramString, paramArrayOfString));
  }
  
  public long executeUpdateInternal(String paramString, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    // Byte code:
    //   0: aload_1
    //   1: astore #8
    //   3: aload_0
    //   4: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   7: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   12: astore #12
    //   14: aload #12
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   21: astore #13
    //   23: aload_0
    //   24: aload_1
    //   25: invokevirtual checkNullOrEmptyQuery : (Ljava/lang/String;)V
    //   28: aload_0
    //   29: invokevirtual resetCancelledState : ()V
    //   32: aload #8
    //   34: aload_1
    //   35: invokestatic findStartOfStatement : (Ljava/lang/String;)I
    //   38: invokestatic firstAlphaCharUc : (Ljava/lang/String;I)C
    //   41: istore #4
    //   43: aload_0
    //   44: iload_3
    //   45: putfield retrieveGeneratedKeys : Z
    //   48: iload_3
    //   49: ifeq -> 73
    //   52: iload #4
    //   54: bipush #73
    //   56: if_icmpne -> 73
    //   59: aload_0
    //   60: aload_1
    //   61: invokevirtual containsOnDuplicateKeyInString : (Ljava/lang/String;)Z
    //   64: ifeq -> 73
    //   67: iconst_1
    //   68: istore #5
    //   70: goto -> 76
    //   73: iconst_0
    //   74: istore #5
    //   76: aload_0
    //   77: iload #5
    //   79: putfield lastQueryIsOnDupKeyUpdate : Z
    //   82: aload #8
    //   84: astore #9
    //   86: aload_0
    //   87: getfield doEscapeProcessing : Z
    //   90: ifeq -> 137
    //   93: aload #8
    //   95: aload_0
    //   96: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   99: invokeinterface serverSupportsConvertFn : ()Z
    //   104: aload_0
    //   105: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   108: invokestatic escapeSQL : (Ljava/lang/String;ZLcom/mysql/jdbc/MySQLConnection;)Ljava/lang/Object;
    //   111: astore_1
    //   112: aload_1
    //   113: instanceof java/lang/String
    //   116: ifeq -> 128
    //   119: aload_1
    //   120: checkcast java/lang/String
    //   123: astore #9
    //   125: goto -> 137
    //   128: aload_1
    //   129: checkcast com/mysql/jdbc/EscapeProcessorResult
    //   132: getfield escapedSql : Ljava/lang/String;
    //   135: astore #9
    //   137: aload #13
    //   139: iconst_0
    //   140: invokeinterface isReadOnly : (Z)Z
    //   145: ifne -> 779
    //   148: aload #9
    //   150: ldc_w 'select'
    //   153: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
    //   156: ifne -> 762
    //   159: aload_0
    //   160: invokevirtual implicitlyCloseAllOpenResults : ()V
    //   163: aload #13
    //   165: invokeinterface isReadInfoMsgEnabled : ()Z
    //   170: istore #5
    //   172: iload_3
    //   173: ifeq -> 191
    //   176: iload #4
    //   178: bipush #82
    //   180: if_icmpne -> 191
    //   183: aload #13
    //   185: iconst_1
    //   186: invokeinterface setReadInfoMsgEnabled : (Z)V
    //   191: aconst_null
    //   192: astore #10
    //   194: aconst_null
    //   195: astore #11
    //   197: aconst_null
    //   198: astore #8
    //   200: aload #13
    //   202: invokeinterface getEnableQueryTimeouts : ()Z
    //   207: istore_3
    //   208: iload_3
    //   209: ifeq -> 294
    //   212: aload_0
    //   213: getfield timeoutInMillis : I
    //   216: ifeq -> 294
    //   219: aload #13
    //   221: iconst_5
    //   222: iconst_0
    //   223: iconst_0
    //   224: invokeinterface versionMeetsMinimum : (III)Z
    //   229: ifeq -> 294
    //   232: new com/mysql/jdbc/StatementImpl$CancelTask
    //   235: astore_1
    //   236: aload_1
    //   237: aload_0
    //   238: aload_0
    //   239: invokespecial <init> : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   242: aload #13
    //   244: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   249: aload_1
    //   250: aload_0
    //   251: getfield timeoutInMillis : I
    //   254: i2l
    //   255: invokevirtual schedule : (Ljava/util/TimerTask;J)V
    //   258: goto -> 296
    //   261: astore #8
    //   263: aconst_null
    //   264: astore #10
    //   266: aload_1
    //   267: astore #9
    //   269: aload #8
    //   271: astore_1
    //   272: aload #10
    //   274: astore #8
    //   276: goto -> 703
    //   279: astore_1
    //   280: aconst_null
    //   281: astore #10
    //   283: aload #8
    //   285: astore #9
    //   287: aload #10
    //   289: astore #8
    //   291: goto -> 703
    //   294: aconst_null
    //   295: astore_1
    //   296: aload #13
    //   298: invokeinterface getCatalog : ()Ljava/lang/String;
    //   303: aload_0
    //   304: getfield currentCatalog : Ljava/lang/String;
    //   307: invokevirtual equals : (Ljava/lang/Object;)Z
    //   310: istore_3
    //   311: iload_3
    //   312: ifne -> 367
    //   315: aload #13
    //   317: invokeinterface getCatalog : ()Ljava/lang/String;
    //   322: astore #8
    //   324: aload #13
    //   326: aload_0
    //   327: getfield currentCatalog : Ljava/lang/String;
    //   330: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   335: goto -> 370
    //   338: astore #10
    //   340: aload_1
    //   341: astore #9
    //   343: aload #10
    //   345: astore_1
    //   346: goto -> 291
    //   349: astore #8
    //   351: aconst_null
    //   352: astore #10
    //   354: aload_1
    //   355: astore #9
    //   357: aload #8
    //   359: astore_1
    //   360: aload #10
    //   362: astore #8
    //   364: goto -> 703
    //   367: aconst_null
    //   368: astore #8
    //   370: aload #13
    //   372: iconst_m1
    //   373: invokeinterface setSessionMaxRows : (I)V
    //   378: aload_0
    //   379: invokevirtual statementBegins : ()V
    //   382: aload_0
    //   383: getfield currentCatalog : Ljava/lang/String;
    //   386: astore #11
    //   388: aload #13
    //   390: aload_0
    //   391: aload #9
    //   393: iconst_m1
    //   394: aconst_null
    //   395: sipush #1003
    //   398: sipush #1007
    //   401: iconst_0
    //   402: aload #11
    //   404: aconst_null
    //   405: iload_2
    //   406: invokeinterface execSQL : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   411: astore #11
    //   413: aload_1
    //   414: ifnull -> 465
    //   417: aload_1
    //   418: getfield caughtWhileCancelling : Ljava/sql/SQLException;
    //   421: astore #9
    //   423: aload #9
    //   425: ifnonnull -> 451
    //   428: aload_1
    //   429: invokevirtual cancel : ()Z
    //   432: pop
    //   433: aload #13
    //   435: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   440: invokevirtual purge : ()I
    //   443: pop
    //   444: aload #10
    //   446: astore #9
    //   448: goto -> 468
    //   451: aload #9
    //   453: athrow
    //   454: astore #10
    //   456: aload_1
    //   457: astore #9
    //   459: aload #10
    //   461: astore_1
    //   462: goto -> 640
    //   465: aload_1
    //   466: astore #9
    //   468: aload_0
    //   469: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   472: astore #10
    //   474: aload #10
    //   476: monitorenter
    //   477: aload_0
    //   478: getfield wasCancelled : Z
    //   481: ifeq -> 516
    //   484: aload_0
    //   485: getfield wasCancelledByTimeout : Z
    //   488: ifeq -> 502
    //   491: new com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   494: astore_1
    //   495: aload_1
    //   496: invokespecial <init> : ()V
    //   499: goto -> 510
    //   502: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   505: dup
    //   506: invokespecial <init> : ()V
    //   509: astore_1
    //   510: aload_0
    //   511: invokevirtual resetCancelledState : ()V
    //   514: aload_1
    //   515: athrow
    //   516: aload #10
    //   518: monitorexit
    //   519: aload #13
    //   521: iload #5
    //   523: invokeinterface setReadInfoMsgEnabled : (Z)V
    //   528: aload #9
    //   530: ifnull -> 550
    //   533: aload #9
    //   535: invokevirtual cancel : ()Z
    //   538: pop
    //   539: aload #13
    //   541: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   546: invokevirtual purge : ()I
    //   549: pop
    //   550: aload #8
    //   552: ifnull -> 564
    //   555: aload #13
    //   557: aload #8
    //   559: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   564: iload_2
    //   565: ifne -> 576
    //   568: aload_0
    //   569: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   572: iconst_0
    //   573: invokevirtual set : (Z)V
    //   576: aload_0
    //   577: aload #11
    //   579: putfield results : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   582: aload #11
    //   584: iload #4
    //   586: invokeinterface setFirstCharOfQuery : (C)V
    //   591: aload_0
    //   592: aload #11
    //   594: invokeinterface getUpdateCount : ()J
    //   599: putfield updateCount : J
    //   602: aload_0
    //   603: aload #11
    //   605: invokeinterface getUpdateID : ()J
    //   610: putfield lastInsertId : J
    //   613: aload_0
    //   614: getfield updateCount : J
    //   617: lstore #6
    //   619: aload #12
    //   621: monitorexit
    //   622: lload #6
    //   624: lreturn
    //   625: astore_1
    //   626: aload #10
    //   628: monitorexit
    //   629: aload_1
    //   630: athrow
    //   631: astore_1
    //   632: goto -> 703
    //   635: astore_1
    //   636: goto -> 626
    //   639: astore_1
    //   640: goto -> 703
    //   643: astore #9
    //   645: aload #8
    //   647: astore #10
    //   649: aload #9
    //   651: astore #8
    //   653: aload #10
    //   655: astore #9
    //   657: goto -> 678
    //   660: astore #10
    //   662: aload #8
    //   664: astore #9
    //   666: aload #10
    //   668: astore #8
    //   670: goto -> 678
    //   673: astore #8
    //   675: aconst_null
    //   676: astore #9
    //   678: aload_1
    //   679: astore #10
    //   681: aload #8
    //   683: astore_1
    //   684: aload #9
    //   686: astore #8
    //   688: aload #10
    //   690: astore #9
    //   692: goto -> 703
    //   695: astore_1
    //   696: aconst_null
    //   697: astore #8
    //   699: aload #11
    //   701: astore #9
    //   703: aload #13
    //   705: iload #5
    //   707: invokeinterface setReadInfoMsgEnabled : (Z)V
    //   712: aload #9
    //   714: ifnull -> 734
    //   717: aload #9
    //   719: invokevirtual cancel : ()Z
    //   722: pop
    //   723: aload #13
    //   725: invokeinterface getCancelTimer : ()Ljava/util/Timer;
    //   730: invokevirtual purge : ()I
    //   733: pop
    //   734: aload #8
    //   736: ifnull -> 748
    //   739: aload #13
    //   741: aload #8
    //   743: invokeinterface setCatalog : (Ljava/lang/String;)V
    //   748: iload_2
    //   749: ifne -> 760
    //   752: aload_0
    //   753: getfield statementExecuting : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   756: iconst_0
    //   757: invokevirtual set : (Z)V
    //   760: aload_1
    //   761: athrow
    //   762: ldc_w 'Statement.46'
    //   765: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   768: ldc_w '01S03'
    //   771: aload_0
    //   772: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   775: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   778: athrow
    //   779: new java/lang/StringBuilder
    //   782: astore_1
    //   783: aload_1
    //   784: invokespecial <init> : ()V
    //   787: aload_1
    //   788: ldc_w 'Statement.42'
    //   791: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   794: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   797: pop
    //   798: aload_1
    //   799: ldc_w 'Statement.43'
    //   802: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   805: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   808: pop
    //   809: aload_1
    //   810: invokevirtual toString : ()Ljava/lang/String;
    //   813: ldc_w 'S1009'
    //   816: aload_0
    //   817: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   820: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   823: athrow
    //   824: astore_1
    //   825: aload #12
    //   827: monitorexit
    //   828: aload_1
    //   829: athrow
    // Exception table:
    //   from	to	target	type
    //   17	48	824	finally
    //   59	67	824	finally
    //   76	82	824	finally
    //   86	125	824	finally
    //   128	137	824	finally
    //   137	172	824	finally
    //   183	191	824	finally
    //   200	208	695	finally
    //   212	242	279	finally
    //   242	258	261	finally
    //   296	311	673	finally
    //   315	324	349	finally
    //   324	335	338	finally
    //   370	388	660	finally
    //   388	413	643	finally
    //   417	423	454	finally
    //   428	444	454	finally
    //   451	454	454	finally
    //   468	477	639	finally
    //   477	499	625	finally
    //   502	510	625	finally
    //   510	516	625	finally
    //   516	519	625	finally
    //   519	528	824	finally
    //   533	550	824	finally
    //   555	564	824	finally
    //   568	576	824	finally
    //   576	622	824	finally
    //   626	629	635	finally
    //   629	631	631	finally
    //   703	712	824	finally
    //   717	734	824	finally
    //   739	748	824	finally
    //   752	760	824	finally
    //   760	762	824	finally
    //   762	779	824	finally
    //   779	824	824	finally
    //   825	828	824	finally
  }
  
  public ResultSetInternalMethods generatePingResultSet() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      Field field = new Field();
      this(null, "1", -5, 1);
      ArrayList<ByteArrayRow> arrayList = new ArrayList();
      this();
      ByteArrayRow byteArrayRow = new ByteArrayRow();
      byte[] arrayOfByte = { 49 };
      ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
      this(new byte[][] { arrayOfByte }, exceptionInterceptor);
      arrayList.add(byteArrayRow);
      MySQLConnection mySQLConnection = this.connection;
      return (ResultSetInternalMethods)DatabaseMetaData.buildResultSet(new Field[] { field }, (ArrayList)arrayList, mySQLConnection);
    } 
  }
  
  public List<Object> getBatchedArgs() {
    List<Object> list = this.batchedArgs;
    if (list == null) {
      list = null;
    } else {
      list = Collections.unmodifiableList(list);
    } 
    return list;
  }
  
  public void getBatchedGeneratedKeys(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool = this.retrieveGeneratedKeys;
      if (bool) {
        ResultSetInternalMethods resultSetInternalMethods = null;
        if (paramInt == 0) {
          try {
          
          } finally {
            this.isImplicitlyClosingResults = true;
            if (resultSetInternalMethods != null)
              try {
                resultSetInternalMethods.close();
              } finally {
                this.isImplicitlyClosingResults = false;
              }  
            this.isImplicitlyClosingResults = false;
          } 
        } else {
          ResultSetInternalMethods resultSetInternalMethods1 = getGeneratedKeysInternal(paramInt);
          resultSetInternalMethods = resultSetInternalMethods1;
          resultSetInternalMethods1 = resultSetInternalMethods;
        } 
      } 
      return;
    } 
  }
  
  public void getBatchedGeneratedKeys(Statement paramStatement) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      boolean bool = this.retrieveGeneratedKeys;
      if (bool) {
        ResultSet resultSet = null;
        try {
          ResultSet resultSet1 = paramStatement.getGeneratedKeys();
          while (true) {
            resultSet = resultSet1;
            if (resultSet1.next()) {
              resultSet = resultSet1;
              ArrayList<ResultSetRow> arrayList = this.batchedGeneratedKeys;
              resultSet = resultSet1;
              ByteArrayRow byteArrayRow = new ByteArrayRow();
              resultSet = resultSet1;
              byte[] arrayOfByte = resultSet1.getBytes(1);
              resultSet = resultSet1;
              ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
              resultSet = resultSet1;
              this(new byte[][] { arrayOfByte }, exceptionInterceptor);
              resultSet = resultSet1;
              arrayList.add(byteArrayRow);
              continue;
            } 
            if (resultSet1 != null)
              resultSet1.close(); 
            break;
          } 
        } finally {
          if (resultSet != null)
            resultSet.close(); 
        } 
      } 
      return;
    } 
  }
  
  public Calendar getCalendarInstanceForSessionOrNew() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.connection != null)
        return this.connection.getCalendarInstanceForSessionOrNew(); 
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      this();
      return gregorianCalendar;
    } 
  }
  
  public Connection getConnection() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.connection;
    } 
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  public int getFetchDirection() throws SQLException {
    return 1000;
  }
  
  public int getFetchSize() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.fetchSize;
    } 
  }
  
  public ResultSet getGeneratedKeys() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.retrieveGeneratedKeys) {
        if (this.batchedGeneratedKeys == null) {
          if (this.lastQueryIsOnDupKeyUpdate) {
            ResultSetInternalMethods resultSetInternalMethods1 = getGeneratedKeysInternal(1L);
            this.generatedKeysResults = resultSetInternalMethods1;
            return resultSetInternalMethods1;
          } 
          ResultSetInternalMethods resultSetInternalMethods = getGeneratedKeysInternal();
          this.generatedKeysResults = resultSetInternalMethods;
          return resultSetInternalMethods;
        } 
        Field[] arrayOfField = new Field[1];
        Field field = new Field();
        this("", "GENERATED_KEY", -5, 20);
        arrayOfField[0] = field;
        arrayOfField[0].setConnection(this.connection);
        String str = this.currentCatalog;
        RowDataStatic rowDataStatic = new RowDataStatic();
        this(this.batchedGeneratedKeys);
        ResultSetImpl resultSetImpl = ResultSetImpl.getInstance(str, arrayOfField, rowDataStatic, this.connection, this, false);
        this.generatedKeysResults = resultSetImpl;
        return resultSetImpl;
      } 
      throw SQLError.createSQLException(Messages.getString("Statement.GeneratedKeysNotRequested"), "S1009", getExceptionInterceptor());
    } 
  }
  
  public ResultSetInternalMethods getGeneratedKeysInternal() throws SQLException {
    return getGeneratedKeysInternal(getLargeUpdateCount());
  }
  
  public ResultSetInternalMethods getGeneratedKeysInternal(long paramLong) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      byte[][] arrayOfByte;
      Field[] arrayOfField = new Field[1];
      Field field = new Field();
      this("", "GENERATED_KEY", -5, 20);
      arrayOfField[0] = field;
      arrayOfField[0].setConnection(this.connection);
      arrayOfField[0].setUseOldNameMetadata(true);
      ArrayList<ByteArrayRow> arrayList1 = new ArrayList();
      this();
      long l = getLastInsertID();
      int i = l cmp 0L;
      if (i < 0)
        arrayOfField[0].setUnsigned(); 
      ResultSetInternalMethods resultSetInternalMethods = this.results;
      ArrayList<ByteArrayRow> arrayList2 = arrayList1;
      if (resultSetInternalMethods != null) {
        String str1 = resultSetInternalMethods.getServerInfo();
        if (paramLong > 0L && this.results.getFirstCharOfQuery() == 'R' && str1 != null && str1.length() > 0)
          paramLong = getRecordCountFromInfo(str1); 
        ArrayList<ByteArrayRow> arrayList = arrayList1;
        if (i != 0) {
          arrayList = arrayList1;
          if (paramLong > 0L) {
            i = 0;
            while (true) {
              arrayList = arrayList1;
              if (i < paramLong) {
                arrayOfByte = new byte[1][];
                if (l > 0L) {
                  arrayOfByte[0] = StringUtils.getBytes(Long.toString(l));
                } else {
                  byte b3 = (byte)(int)(l & 0xFFL);
                  byte b1 = (byte)(int)(l >>> 8L);
                  byte b2 = (byte)(int)(l >>> 16L);
                  byte b5 = (byte)(int)(l >>> 24L);
                  byte b6 = (byte)(int)(l >>> 32L);
                  byte b8 = (byte)(int)(l >>> 40L);
                  byte b4 = (byte)(int)(l >>> 48L);
                  byte b7 = (byte)(int)(l >>> 56L);
                  BigInteger bigInteger = new BigInteger();
                  this(1, new byte[] { b7, b4, b8, b6, b5, b2, b1, b3 });
                  arrayOfByte[0] = bigInteger.toString().getBytes();
                } 
                ByteArrayRow byteArrayRow = new ByteArrayRow();
                this(arrayOfByte, getExceptionInterceptor());
                arrayList1.add(byteArrayRow);
                l += this.connection.getAutoIncrementIncrement();
                i++;
                continue;
              } 
              break;
            } 
          } 
        } 
      } 
      String str = this.currentCatalog;
      RowDataStatic rowDataStatic = new RowDataStatic();
      this((List<ResultSetRow>)arrayOfByte);
      return ResultSetImpl.getInstance(str, arrayOfField, rowDataStatic, this.connection, this, false);
    } 
  }
  
  public int getId() {
    return this.statementId;
  }
  
  public long getLargeMaxRows() throws SQLException {
    return getMaxRows();
  }
  
  public long getLargeUpdateCount() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = this.results;
      if (resultSetInternalMethods == null)
        return -1L; 
      if (resultSetInternalMethods.reallyResult())
        return -1L; 
      return this.results.getUpdateCount();
    } 
  }
  
  public long getLastInsertID() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        return this.lastInsertId;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public InputStream getLocalInfileInputStream() {
    return this.localInfileInputStream;
  }
  
  public long getLongUpdateCount() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        ResultSetInternalMethods resultSetInternalMethods = this.results;
        if (resultSetInternalMethods == null)
          return -1L; 
        if (resultSetInternalMethods.reallyResult())
          return -1L; 
        return this.updateCount;
      } 
    } catch (SQLException sQLException) {
      throw new RuntimeException(sQLException);
    } 
  }
  
  public int getMaxFieldSize() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.maxFieldSize;
    } 
  }
  
  public int getMaxRows() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      int i = this.maxRows;
      if (i <= 0)
        return 0; 
      return i;
    } 
  }
  
  public boolean getMoreResults() throws SQLException {
    return getMoreResults(1);
  }
  
  public boolean getMoreResults(int paramInt) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = this.results;
      boolean bool1 = false;
      if (resultSetInternalMethods == null)
        return false; 
      boolean bool = createStreamingResultSet();
      if (bool && this.results.reallyResult())
        while (this.results.next()); 
      resultSetInternalMethods = this.results.getNextResultSet();
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt == 3) {
            if (this.results != null) {
              if (!bool && !this.connection.getDontTrackOpenResources())
                this.results.realClose(false); 
              this.results.clearNextResult();
            } 
            closeAllOpenResults();
          } else {
            throw SQLError.createSQLException(Messages.getString("Statement.19"), "S1009", getExceptionInterceptor());
          } 
        } else {
          if (!this.connection.getDontTrackOpenResources())
            this.openResults.add(this.results); 
          this.results.clearNextResult();
        } 
      } else if (this.results != null) {
        if (!bool && !this.connection.getDontTrackOpenResources())
          this.results.realClose(false); 
        this.results.clearNextResult();
      } 
      this.results = resultSetInternalMethods;
      if (resultSetInternalMethods == null) {
        this.updateCount = -1L;
        this.lastInsertId = -1L;
      } else if (resultSetInternalMethods.reallyResult()) {
        this.updateCount = -1L;
        this.lastInsertId = -1L;
      } else {
        this.updateCount = this.results.getUpdateCount();
        this.lastInsertId = this.results.getUpdateID();
      } 
      resultSetInternalMethods = this.results;
      bool = bool1;
      if (resultSetInternalMethods != null) {
        bool = bool1;
        if (resultSetInternalMethods.reallyResult())
          bool = true; 
      } 
      if (!bool)
        checkAndPerformCloseOnCompletionAction(); 
      return bool;
    } 
  }
  
  public int getOpenResultSetCount() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        Set<ResultSetInternalMethods> set = this.openResults;
        if (set != null)
          return set.size(); 
        return 0;
      } 
    } catch (SQLException sQLException) {
      return 0;
    } 
  }
  
  public int getQueryTimeout() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.timeoutInMillis / 1000;
    } 
  }
  
  public ResultSet getResultSet() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      ResultSetInternalMethods resultSetInternalMethods = this.results;
      if (resultSetInternalMethods != null && resultSetInternalMethods.reallyResult()) {
        resultSetInternalMethods = this.results;
      } else {
        resultSetInternalMethods = null;
      } 
      return resultSetInternalMethods;
    } 
  }
  
  public int getResultSetConcurrency() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.resultSetConcurrency;
    } 
  }
  
  public int getResultSetHoldability() throws SQLException {
    return 1;
  }
  
  public ResultSetInternalMethods getResultSetInternal() {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        return this.results;
      } 
    } catch (SQLException sQLException) {
      return this.results;
    } 
  }
  
  public int getResultSetType() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.resultSetType;
    } 
  }
  
  public int getUpdateCount() throws SQLException {
    return Util.truncateAndConvertToInt(getLargeUpdateCount());
  }
  
  public SQLWarning getWarnings() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (this.clearWarningsCalled)
        return null; 
      if (this.connection.versionMeetsMinimum(4, 1, 0)) {
        SQLWarning sQLWarning2 = SQLError.convertShowWarningsToSQLWarnings(this.connection);
        SQLWarning sQLWarning1 = this.warningChain;
        if (sQLWarning1 != null) {
          sQLWarning1.setNextWarning(sQLWarning2);
        } else {
          this.warningChain = sQLWarning2;
        } 
        sQLWarning1 = this.warningChain;
        return sQLWarning1;
      } 
      return this.warningChain;
    } 
  }
  
  public SQLException handleExceptionForBatch(int paramInt1, int paramInt2, long[] paramArrayOflong, SQLException paramSQLException) throws BatchUpdateException, SQLException {
    for (int i = paramInt1; i > paramInt1 - paramInt2; i--)
      paramArrayOflong[i] = -3L; 
    if (this.continueBatchOnError && !(paramSQLException instanceof com.mysql.jdbc.exceptions.MySQLTimeoutException) && !(paramSQLException instanceof com.mysql.jdbc.exceptions.MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(paramSQLException))
      return paramSQLException; 
    long[] arrayOfLong = new long[paramInt1];
    System.arraycopy(paramArrayOflong, 0, arrayOfLong, 0, paramInt1);
    throw SQLError.createBatchUpdateException(paramSQLException, arrayOfLong, getExceptionInterceptor());
  }
  
  public final boolean hasDeadlockOrTimeoutRolledBackTx(SQLException paramSQLException) {
    int i = paramSQLException.getErrorCode();
    return (i != 1205) ? (!(i != 1206 && i != 1213)) : (this.version5013OrNewer ^ true);
  }
  
  public void implicitlyCloseAllOpenResults() throws SQLException {
    this.isImplicitlyClosingResults = true;
    try {
      if (!this.connection.getHoldResultsOpenOverStatementClose() && !this.connection.getDontTrackOpenResources() && !this.holdResultsOpenOverClose) {
        ResultSetInternalMethods resultSetInternalMethods = this.results;
        if (resultSetInternalMethods != null)
          resultSetInternalMethods.realClose(false); 
        resultSetInternalMethods = this.generatedKeysResults;
        if (resultSetInternalMethods != null)
          resultSetInternalMethods.realClose(false); 
        closeAllOpenResults();
      } 
      return;
    } finally {
      this.isImplicitlyClosingResults = false;
    } 
  }
  
  public boolean isCloseOnCompletion() throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      return this.closeOnCompletion;
    } 
  }
  
  public boolean isClosed() throws SQLException {
    null = this.connection;
    if (null == null)
      return true; 
    synchronized (null.getConnectionMutex()) {
      return this.isClosed;
    } 
  }
  
  public boolean isCursorRequired() throws SQLException {
    return false;
  }
  
  public boolean isPoolable() throws SQLException {
    return this.isPoolable;
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    checkClosed();
    return paramClass.isInstance(this);
  }
  
  public int processMultiCountsAndKeys(StatementImpl paramStatementImpl, int paramInt, long[] paramArrayOflong) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #6
    //   11: aload #6
    //   13: monitorenter
    //   14: iload_2
    //   15: iconst_1
    //   16: iadd
    //   17: istore #5
    //   19: aload_3
    //   20: iload_2
    //   21: aload_1
    //   22: invokevirtual getLargeUpdateCount : ()J
    //   25: lastore
    //   26: aload_0
    //   27: getfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   30: ifnull -> 38
    //   33: iconst_1
    //   34: istore_2
    //   35: goto -> 40
    //   38: iconst_0
    //   39: istore_2
    //   40: iload #5
    //   42: istore #4
    //   44: iload_2
    //   45: ifeq -> 105
    //   48: aload_1
    //   49: invokevirtual getLastInsertID : ()J
    //   52: invokestatic toString : (J)Ljava/lang/String;
    //   55: invokestatic getBytes : (Ljava/lang/String;)[B
    //   58: astore #10
    //   60: aload_0
    //   61: getfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   64: astore #9
    //   66: new com/mysql/jdbc/ByteArrayRow
    //   69: astore #8
    //   71: aload_0
    //   72: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   75: astore #7
    //   77: aload #8
    //   79: iconst_1
    //   80: anewarray [B
    //   83: dup
    //   84: iconst_0
    //   85: aload #10
    //   87: aastore
    //   88: aload #7
    //   90: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
    //   93: aload #9
    //   95: aload #8
    //   97: invokevirtual add : (Ljava/lang/Object;)Z
    //   100: pop
    //   101: iload #5
    //   103: istore #4
    //   105: aload_1
    //   106: invokevirtual getMoreResults : ()Z
    //   109: ifne -> 132
    //   112: aload_1
    //   113: invokevirtual getLargeUpdateCount : ()J
    //   116: ldc2_w -1
    //   119: lcmp
    //   120: ifeq -> 126
    //   123: goto -> 132
    //   126: aload #6
    //   128: monitorexit
    //   129: iload #4
    //   131: ireturn
    //   132: aload_3
    //   133: iload #4
    //   135: aload_1
    //   136: invokevirtual getLargeUpdateCount : ()J
    //   139: lastore
    //   140: iload_2
    //   141: ifeq -> 197
    //   144: aload_1
    //   145: invokevirtual getLastInsertID : ()J
    //   148: invokestatic toString : (J)Ljava/lang/String;
    //   151: invokestatic getBytes : (Ljava/lang/String;)[B
    //   154: astore #9
    //   156: aload_0
    //   157: getfield batchedGeneratedKeys : Ljava/util/ArrayList;
    //   160: astore #7
    //   162: new com/mysql/jdbc/ByteArrayRow
    //   165: astore #10
    //   167: aload_0
    //   168: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   171: astore #8
    //   173: aload #10
    //   175: iconst_1
    //   176: anewarray [B
    //   179: dup
    //   180: iconst_0
    //   181: aload #9
    //   183: aastore
    //   184: aload #8
    //   186: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
    //   189: aload #7
    //   191: aload #10
    //   193: invokevirtual add : (Ljava/lang/Object;)Z
    //   196: pop
    //   197: iinc #4, 1
    //   200: goto -> 105
    //   203: astore_1
    //   204: aload #6
    //   206: monitorexit
    //   207: aload_1
    //   208: athrow
    // Exception table:
    //   from	to	target	type
    //   19	33	203	finally
    //   48	101	203	finally
    //   105	123	203	finally
    //   126	129	203	finally
    //   132	140	203	finally
    //   144	197	203	finally
    //   204	207	203	finally
  }
  
  public void realClose(boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    MySQLConnection mySQLConnection = this.connection;
    if (mySQLConnection != null && !this.isClosed) {
      if (!mySQLConnection.getDontTrackOpenResources())
        mySQLConnection.unregisterStatement(this); 
      if (this.useUsageAdvisor && !paramBoolean1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("Statement.63"));
        stringBuilder.append(Messages.getString("Statement.64"));
        String str = stringBuilder.toString();
        this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, str));
      } 
      if (paramBoolean2) {
        if (!this.holdResultsOpenOverClose && !this.connection.getDontTrackOpenResources()) {
          paramBoolean1 = true;
        } else {
          paramBoolean1 = false;
        } 
      } else {
        paramBoolean1 = paramBoolean2;
      } 
      if (paramBoolean1) {
        ResultSetInternalMethods resultSetInternalMethods = this.results;
        if (resultSetInternalMethods != null)
          try {
            resultSetInternalMethods.close();
          } catch (Exception exception) {} 
        resultSetInternalMethods = this.generatedKeysResults;
        if (resultSetInternalMethods != null)
          try {
            resultSetInternalMethods.close();
          } catch (Exception exception) {} 
        closeAllOpenResults();
      } 
      this.isClosed = true;
      this.results = null;
      this.generatedKeysResults = null;
      this.connection = null;
      this.warningChain = null;
      this.openResults = null;
      this.batchedGeneratedKeys = null;
      this.localInfileInputStream = null;
      this.pingTarget = null;
    } 
  }
  
  public void removeOpenResultSet(ResultSetInternalMethods paramResultSetInternalMethods) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        boolean bool;
        Set<ResultSetInternalMethods> set = this.openResults;
        if (set != null)
          set.remove(paramResultSetInternalMethods); 
        if (paramResultSetInternalMethods.getNextResultSet() != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (this.results == paramResultSetInternalMethods && !bool)
          this.results = null; 
        if (this.generatedKeysResults == paramResultSetInternalMethods)
          this.generatedKeysResults = null; 
        if (!this.isImplicitlyClosingResults && !bool)
          checkAndPerformCloseOnCompletionAction(); 
      } 
    } catch (SQLException sQLException) {}
  }
  
  public void resetCancelledState() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_1
    //   10: aload_1
    //   11: monitorenter
    //   12: aload_0
    //   13: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   16: astore_3
    //   17: aload_3
    //   18: ifnonnull -> 24
    //   21: aload_1
    //   22: monitorexit
    //   23: return
    //   24: aload_3
    //   25: monitorenter
    //   26: aload_0
    //   27: iconst_0
    //   28: putfield wasCancelled : Z
    //   31: aload_0
    //   32: iconst_0
    //   33: putfield wasCancelledByTimeout : Z
    //   36: aload_3
    //   37: monitorexit
    //   38: aload_1
    //   39: monitorexit
    //   40: return
    //   41: astore_2
    //   42: aload_3
    //   43: monitorexit
    //   44: aload_2
    //   45: athrow
    //   46: astore_2
    //   47: aload_1
    //   48: monitorexit
    //   49: aload_2
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   12	17	46	finally
    //   21	23	46	finally
    //   24	26	46	finally
    //   26	38	41	finally
    //   38	40	46	finally
    //   42	44	41	finally
    //   44	46	46	finally
    //   47	49	46	finally
  }
  
  public void setCursorName(String paramString) throws SQLException {}
  
  public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      this.doEscapeProcessing = paramBoolean;
      return;
    } 
  }
  
  public void setFetchDirection(int paramInt) throws SQLException {
    switch (paramInt) {
      default:
        throw SQLError.createSQLException(Messages.getString("Statement.5"), "S1009", getExceptionInterceptor());
      case 1000:
      case 1001:
      case 1002:
        break;
    } 
  }
  
  public void setFetchSize(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: ifge -> 23
    //   16: iload_1
    //   17: ldc_w -2147483648
    //   20: if_icmpne -> 41
    //   23: aload_0
    //   24: getfield maxRows : I
    //   27: ifle -> 58
    //   30: iload_1
    //   31: aload_0
    //   32: invokevirtual getMaxRows : ()I
    //   35: if_icmpgt -> 41
    //   38: goto -> 58
    //   41: ldc_w 'Statement.7'
    //   44: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   47: ldc_w 'S1009'
    //   50: aload_0
    //   51: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   54: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   57: athrow
    //   58: aload_0
    //   59: iload_1
    //   60: putfield fetchSize : I
    //   63: aload_3
    //   64: monitorexit
    //   65: return
    //   66: astore_2
    //   67: aload_3
    //   68: monitorexit
    //   69: aload_2
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   23	38	66	finally
    //   41	58	66	finally
    //   58	65	66	finally
    //   67	69	66	finally
  }
  
  public void setHoldResultsOpenOverClose(boolean paramBoolean) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.holdResultsOpenOverClose = paramBoolean;
      } 
    } catch (SQLException sQLException) {}
  }
  
  public void setLargeMaxRows(long paramLong) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore #4
    //   11: aload #4
    //   13: monitorenter
    //   14: lload_1
    //   15: ldc2_w 50000000
    //   18: lcmp
    //   19: ifgt -> 50
    //   22: lload_1
    //   23: lconst_0
    //   24: lcmp
    //   25: istore_3
    //   26: iload_3
    //   27: iflt -> 50
    //   30: iload_3
    //   31: ifne -> 38
    //   34: ldc2_w -1
    //   37: lstore_1
    //   38: lload_1
    //   39: l2i
    //   40: istore_3
    //   41: aload_0
    //   42: iload_3
    //   43: putfield maxRows : I
    //   46: aload #4
    //   48: monitorexit
    //   49: return
    //   50: new java/lang/StringBuilder
    //   53: astore #5
    //   55: aload #5
    //   57: invokespecial <init> : ()V
    //   60: aload #5
    //   62: ldc_w 'Statement.15'
    //   65: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload #5
    //   74: lload_1
    //   75: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   78: pop
    //   79: aload #5
    //   81: ldc_w ' > '
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload #5
    //   90: ldc_w 50000000
    //   93: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload #5
    //   99: ldc_w '.'
    //   102: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload #5
    //   108: invokevirtual toString : ()Ljava/lang/String;
    //   111: ldc_w 'S1009'
    //   114: aload_0
    //   115: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   118: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   121: athrow
    //   122: astore #5
    //   124: aload #4
    //   126: monitorexit
    //   127: aload #5
    //   129: athrow
    // Exception table:
    //   from	to	target	type
    //   41	49	122	finally
    //   50	122	122	finally
    //   124	127	122	finally
  }
  
  public void setLocalInfileInputStream(InputStream paramInputStream) {
    this.localInfileInputStream = paramInputStream;
  }
  
  public void setMaxFieldSize(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: iflt -> 87
    //   16: aload_0
    //   17: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   20: ifnull -> 36
    //   23: aload_0
    //   24: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   27: invokeinterface getMaxAllowedPacket : ()I
    //   32: istore_2
    //   33: goto -> 40
    //   36: invokestatic getMaxBuf : ()I
    //   39: istore_2
    //   40: iload_1
    //   41: iload_2
    //   42: if_icmpgt -> 53
    //   45: aload_0
    //   46: iload_1
    //   47: putfield maxFieldSize : I
    //   50: aload_3
    //   51: monitorexit
    //   52: return
    //   53: ldc_w 'Statement.13'
    //   56: iconst_1
    //   57: anewarray java/lang/Object
    //   60: dup
    //   61: iconst_0
    //   62: iload_2
    //   63: i2l
    //   64: invokestatic valueOf : (J)Ljava/lang/Long;
    //   67: aastore
    //   68: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   71: ldc_w 'S1009'
    //   74: aload_0
    //   75: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   78: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   81: athrow
    //   82: astore #4
    //   84: goto -> 104
    //   87: ldc_w 'Statement.11'
    //   90: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   93: ldc_w 'S1009'
    //   96: aload_0
    //   97: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   100: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   103: athrow
    //   104: aload_3
    //   105: monitorexit
    //   106: aload #4
    //   108: athrow
    // Exception table:
    //   from	to	target	type
    //   16	33	82	finally
    //   36	40	82	finally
    //   45	52	82	finally
    //   53	82	82	finally
    //   87	104	82	finally
    //   104	106	82	finally
  }
  
  public void setMaxRows(int paramInt) throws SQLException {
    setLargeMaxRows(paramInt);
  }
  
  public void setPingTarget(PingTarget paramPingTarget) {
    this.pingTarget = paramPingTarget;
  }
  
  public void setPoolable(boolean paramBoolean) throws SQLException {
    this.isPoolable = paramBoolean;
  }
  
  public void setQueryTimeout(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual checkClosed : ()Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getConnectionMutex : ()Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iload_1
    //   13: iflt -> 32
    //   16: aload_0
    //   17: iload_1
    //   18: sipush #1000
    //   21: imul
    //   22: putfield timeoutInMillis : I
    //   25: aload_2
    //   26: monitorexit
    //   27: return
    //   28: astore_3
    //   29: goto -> 49
    //   32: ldc_w 'Statement.21'
    //   35: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   38: ldc_w 'S1009'
    //   41: aload_0
    //   42: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   45: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   48: athrow
    //   49: aload_2
    //   50: monitorexit
    //   51: aload_3
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   16	27	28	finally
    //   32	49	28	finally
    //   49	51	28	finally
  }
  
  public void setResultSetConcurrency(int paramInt) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.resultSetConcurrency = paramInt;
      } 
    } catch (SQLException sQLException) {}
  }
  
  public void setResultSetType(int paramInt) {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        this.resultSetType = paramInt;
      } 
    } catch (SQLException sQLException) {}
  }
  
  public void setupStreamingTimeout(MySQLConnection paramMySQLConnection) throws SQLException {
    if (createStreamingResultSet() && paramMySQLConnection.getNetTimeoutForStreamingResults() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("SET net_write_timeout=");
      stringBuilder.append(paramMySQLConnection.getNetTimeoutForStreamingResults());
      executeSimpleNonQuery(paramMySQLConnection, stringBuilder.toString());
    } 
  }
  
  public void statementBegins() {
    this.clearWarningsCalled = false;
    this.statementExecuting.set(true);
    MySQLConnection mySQLConnection;
    for (mySQLConnection = this.connection.getMultiHostSafeProxy().getActiveMySQLConnection(); !(mySQLConnection instanceof ConnectionImpl); mySQLConnection = mySQLConnection.getMultiHostSafeProxy().getActiveMySQLConnection());
    this.physicalConnection = new WeakReference<MySQLConnection>(mySQLConnection);
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
  
  public class CancelTask extends TimerTask {
    public SQLException caughtWhileCancelling = null;
    
    public long origConnId = 0L;
    
    public Properties origConnProps = null;
    
    public String origConnURL = "";
    
    public final StatementImpl this$0;
    
    public StatementImpl toCancel;
    
    public CancelTask(StatementImpl param1StatementImpl1) throws SQLException {
      this.toCancel = param1StatementImpl1;
      this.origConnProps = new Properties();
      Properties properties = StatementImpl.this.connection.getProperties();
      Enumeration<?> enumeration = properties.propertyNames();
      while (enumeration.hasMoreElements()) {
        String str = enumeration.nextElement().toString();
        this.origConnProps.setProperty(str, properties.getProperty(str));
      } 
      this.origConnURL = StatementImpl.this.connection.getURL();
      this.origConnId = StatementImpl.this.connection.getId();
    }
    
    public void run() {
      (new Thread() {
          public final StatementImpl.CancelTask this$1;
          
          public void run() {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   4: getfield this$0 : Lcom/mysql/jdbc/StatementImpl;
            //   7: getfield physicalConnection : Ljava/lang/ref/Reference;
            //   10: invokevirtual get : ()Ljava/lang/Object;
            //   13: checkcast com/mysql/jdbc/MySQLConnection
            //   16: astore #7
            //   18: aload #7
            //   20: ifnull -> 421
            //   23: aload #7
            //   25: invokeinterface getQueryTimeoutKillsConnection : ()Z
            //   30: ifeq -> 78
            //   33: aload_0
            //   34: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   37: getfield toCancel : Lcom/mysql/jdbc/StatementImpl;
            //   40: astore_1
            //   41: aload_1
            //   42: iconst_1
            //   43: putfield wasCancelled : Z
            //   46: aload_1
            //   47: iconst_1
            //   48: putfield wasCancelledByTimeout : Z
            //   51: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
            //   54: astore_1
            //   55: aload_1
            //   56: ldc 'Statement.ConnectionKilledDueToTimeout'
            //   58: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
            //   61: invokespecial <init> : (Ljava/lang/String;)V
            //   64: aload #7
            //   66: iconst_0
            //   67: iconst_0
            //   68: iconst_1
            //   69: aload_1
            //   70: invokeinterface realClose : (ZZZLjava/lang/Throwable;)V
            //   75: goto -> 421
            //   78: aload_0
            //   79: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   82: getfield this$0 : Lcom/mysql/jdbc/StatementImpl;
            //   85: getfield cancelTimeoutMutex : Ljava/lang/Object;
            //   88: astore #6
            //   90: aload #6
            //   92: monitorenter
            //   93: aload_0
            //   94: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   97: getfield origConnURL : Ljava/lang/String;
            //   100: aload #7
            //   102: invokeinterface getURL : ()Ljava/lang/String;
            //   107: invokevirtual equals : (Ljava/lang/Object;)Z
            //   110: ifeq -> 215
            //   113: aload #7
            //   115: invokeinterface duplicate : ()Lcom/mysql/jdbc/Connection;
            //   120: astore_1
            //   121: aload_1
            //   122: astore_2
            //   123: aload_1
            //   124: invokeinterface createStatement : ()Ljava/sql/Statement;
            //   129: astore #5
            //   131: aload_1
            //   132: astore #4
            //   134: aload #5
            //   136: astore_3
            //   137: new java/lang/StringBuilder
            //   140: astore_2
            //   141: aload_1
            //   142: astore #4
            //   144: aload #5
            //   146: astore_3
            //   147: aload_2
            //   148: invokespecial <init> : ()V
            //   151: aload_1
            //   152: astore #4
            //   154: aload #5
            //   156: astore_3
            //   157: aload_2
            //   158: ldc 'KILL QUERY '
            //   160: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   163: pop
            //   164: aload_1
            //   165: astore #4
            //   167: aload #5
            //   169: astore_3
            //   170: aload_2
            //   171: aload #7
            //   173: invokeinterface getId : ()J
            //   178: invokevirtual append : (J)Ljava/lang/StringBuilder;
            //   181: pop
            //   182: aload_1
            //   183: astore #4
            //   185: aload #5
            //   187: astore_3
            //   188: aload #5
            //   190: aload_2
            //   191: invokevirtual toString : ()Ljava/lang/String;
            //   194: invokeinterface execute : (Ljava/lang/String;)Z
            //   199: pop
            //   200: aload_1
            //   201: astore_2
            //   202: aload #5
            //   204: astore_1
            //   205: goto -> 337
            //   208: astore #5
            //   210: aconst_null
            //   211: astore_1
            //   212: goto -> 390
            //   215: aload_0
            //   216: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   219: astore_1
            //   220: aload_1
            //   221: getfield origConnURL : Ljava/lang/String;
            //   224: aload_1
            //   225: getfield origConnProps : Ljava/util/Properties;
            //   228: invokestatic getConnection : (Ljava/lang/String;Ljava/util/Properties;)Ljava/sql/Connection;
            //   231: checkcast com/mysql/jdbc/Connection
            //   234: astore_1
            //   235: aload_1
            //   236: astore_2
            //   237: aload_1
            //   238: invokeinterface createStatement : ()Ljava/sql/Statement;
            //   243: astore #5
            //   245: aload_1
            //   246: astore #4
            //   248: aload #5
            //   250: astore_3
            //   251: new java/lang/StringBuilder
            //   254: astore_2
            //   255: aload_1
            //   256: astore #4
            //   258: aload #5
            //   260: astore_3
            //   261: aload_2
            //   262: invokespecial <init> : ()V
            //   265: aload_1
            //   266: astore #4
            //   268: aload #5
            //   270: astore_3
            //   271: aload_2
            //   272: ldc 'KILL QUERY '
            //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   277: pop
            //   278: aload_1
            //   279: astore #4
            //   281: aload #5
            //   283: astore_3
            //   284: aload_2
            //   285: aload_0
            //   286: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   289: getfield origConnId : J
            //   292: invokevirtual append : (J)Ljava/lang/StringBuilder;
            //   295: pop
            //   296: aload_1
            //   297: astore #4
            //   299: aload #5
            //   301: astore_3
            //   302: aload #5
            //   304: aload_2
            //   305: invokevirtual toString : ()Ljava/lang/String;
            //   308: invokeinterface execute : (Ljava/lang/String;)Z
            //   313: pop
            //   314: aload_1
            //   315: astore_2
            //   316: aload #5
            //   318: astore_1
            //   319: goto -> 337
            //   322: astore_2
            //   323: aconst_null
            //   324: astore_3
            //   325: aload_1
            //   326: astore_2
            //   327: aload_3
            //   328: astore_1
            //   329: goto -> 337
            //   332: astore_1
            //   333: aconst_null
            //   334: astore_2
            //   335: aconst_null
            //   336: astore_1
            //   337: aload_2
            //   338: astore #4
            //   340: aload_1
            //   341: astore_3
            //   342: aload_0
            //   343: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   346: getfield toCancel : Lcom/mysql/jdbc/StatementImpl;
            //   349: astore #5
            //   351: aload_2
            //   352: astore #4
            //   354: aload_1
            //   355: astore_3
            //   356: aload #5
            //   358: iconst_1
            //   359: putfield wasCancelled : Z
            //   362: aload_2
            //   363: astore #4
            //   365: aload_1
            //   366: astore_3
            //   367: aload #5
            //   369: iconst_1
            //   370: putfield wasCancelledByTimeout : Z
            //   373: aload_2
            //   374: astore #4
            //   376: aload_1
            //   377: astore_3
            //   378: aload #6
            //   380: monitorexit
            //   381: goto -> 425
            //   384: astore #5
            //   386: aconst_null
            //   387: astore_2
            //   388: aconst_null
            //   389: astore_1
            //   390: aload_2
            //   391: astore #4
            //   393: aload_1
            //   394: astore_3
            //   395: aload #6
            //   397: monitorexit
            //   398: aload_2
            //   399: astore_3
            //   400: aload_1
            //   401: astore #4
            //   403: aload #5
            //   405: athrow
            //   406: astore #5
            //   408: goto -> 572
            //   411: astore #5
            //   413: aload #4
            //   415: astore_2
            //   416: aload_3
            //   417: astore_1
            //   418: goto -> 390
            //   421: aconst_null
            //   422: astore_2
            //   423: aconst_null
            //   424: astore_1
            //   425: aload_1
            //   426: ifnull -> 451
            //   429: aload_1
            //   430: invokeinterface close : ()V
            //   435: goto -> 451
            //   438: astore_1
            //   439: new java/lang/RuntimeException
            //   442: dup
            //   443: aload_1
            //   444: invokevirtual toString : ()Ljava/lang/String;
            //   447: invokespecial <init> : (Ljava/lang/String;)V
            //   450: athrow
            //   451: aload_2
            //   452: ifnull -> 477
            //   455: aload_2
            //   456: invokeinterface close : ()V
            //   461: goto -> 477
            //   464: astore_1
            //   465: new java/lang/RuntimeException
            //   468: dup
            //   469: aload_1
            //   470: invokevirtual toString : ()Ljava/lang/String;
            //   473: invokespecial <init> : (Ljava/lang/String;)V
            //   476: athrow
            //   477: aload_0
            //   478: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   481: astore_1
            //   482: aload_1
            //   483: aconst_null
            //   484: putfield toCancel : Lcom/mysql/jdbc/StatementImpl;
            //   487: aload_1
            //   488: aconst_null
            //   489: putfield origConnProps : Ljava/util/Properties;
            //   492: aload_1
            //   493: aconst_null
            //   494: putfield origConnURL : Ljava/lang/String;
            //   497: goto -> 638
            //   500: astore_1
            //   501: aconst_null
            //   502: astore_3
            //   503: aconst_null
            //   504: astore #4
            //   506: goto -> 640
            //   509: astore_1
            //   510: aconst_null
            //   511: astore_2
            //   512: aconst_null
            //   513: astore_1
            //   514: aload_1
            //   515: ifnull -> 540
            //   518: aload_1
            //   519: invokeinterface close : ()V
            //   524: goto -> 540
            //   527: astore_1
            //   528: new java/lang/RuntimeException
            //   531: dup
            //   532: aload_1
            //   533: invokevirtual toString : ()Ljava/lang/String;
            //   536: invokespecial <init> : (Ljava/lang/String;)V
            //   539: athrow
            //   540: aload_2
            //   541: ifnull -> 477
            //   544: aload_2
            //   545: invokeinterface close : ()V
            //   550: goto -> 477
            //   553: astore_1
            //   554: new java/lang/RuntimeException
            //   557: dup
            //   558: aload_1
            //   559: invokevirtual toString : ()Ljava/lang/String;
            //   562: invokespecial <init> : (Ljava/lang/String;)V
            //   565: athrow
            //   566: astore #5
            //   568: aconst_null
            //   569: astore_2
            //   570: aconst_null
            //   571: astore_1
            //   572: aload_2
            //   573: astore_3
            //   574: aload_1
            //   575: astore #4
            //   577: aload_0
            //   578: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   581: aload #5
            //   583: putfield caughtWhileCancelling : Ljava/sql/SQLException;
            //   586: aload_1
            //   587: ifnull -> 612
            //   590: aload_1
            //   591: invokeinterface close : ()V
            //   596: goto -> 612
            //   599: astore_1
            //   600: new java/lang/RuntimeException
            //   603: dup
            //   604: aload_1
            //   605: invokevirtual toString : ()Ljava/lang/String;
            //   608: invokespecial <init> : (Ljava/lang/String;)V
            //   611: athrow
            //   612: aload_2
            //   613: ifnull -> 477
            //   616: aload_2
            //   617: invokeinterface close : ()V
            //   622: goto -> 477
            //   625: astore_1
            //   626: new java/lang/RuntimeException
            //   629: dup
            //   630: aload_1
            //   631: invokevirtual toString : ()Ljava/lang/String;
            //   634: invokespecial <init> : (Ljava/lang/String;)V
            //   637: athrow
            //   638: return
            //   639: astore_1
            //   640: aload #4
            //   642: ifnull -> 668
            //   645: aload #4
            //   647: invokeinterface close : ()V
            //   652: goto -> 668
            //   655: astore_1
            //   656: new java/lang/RuntimeException
            //   659: dup
            //   660: aload_1
            //   661: invokevirtual toString : ()Ljava/lang/String;
            //   664: invokespecial <init> : (Ljava/lang/String;)V
            //   667: athrow
            //   668: aload_3
            //   669: ifnull -> 694
            //   672: aload_3
            //   673: invokeinterface close : ()V
            //   678: goto -> 694
            //   681: astore_1
            //   682: new java/lang/RuntimeException
            //   685: dup
            //   686: aload_1
            //   687: invokevirtual toString : ()Ljava/lang/String;
            //   690: invokespecial <init> : (Ljava/lang/String;)V
            //   693: athrow
            //   694: aload_0
            //   695: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
            //   698: astore_2
            //   699: aload_2
            //   700: aconst_null
            //   701: putfield toCancel : Lcom/mysql/jdbc/StatementImpl;
            //   704: aload_2
            //   705: aconst_null
            //   706: putfield origConnProps : Ljava/util/Properties;
            //   709: aload_2
            //   710: aconst_null
            //   711: putfield origConnURL : Ljava/lang/String;
            //   714: aload_1
            //   715: athrow
            //   716: astore_2
            //   717: aload_1
            //   718: astore_2
            //   719: aload #5
            //   721: astore_1
            //   722: goto -> 337
            //   725: astore_3
            //   726: goto -> 514
            // Exception table:
            //   from	to	target	type
            //   0	18	566	java/sql/SQLException
            //   0	18	509	java/lang/NullPointerException
            //   0	18	500	finally
            //   23	75	566	java/sql/SQLException
            //   23	75	509	java/lang/NullPointerException
            //   23	75	500	finally
            //   78	93	566	java/sql/SQLException
            //   78	93	509	java/lang/NullPointerException
            //   78	93	500	finally
            //   93	121	384	finally
            //   123	131	208	finally
            //   137	141	411	finally
            //   147	151	411	finally
            //   157	164	411	finally
            //   170	182	411	finally
            //   188	200	411	finally
            //   215	235	332	java/lang/NullPointerException
            //   215	235	384	finally
            //   237	245	322	java/lang/NullPointerException
            //   237	245	208	finally
            //   251	255	716	java/lang/NullPointerException
            //   251	255	411	finally
            //   261	265	716	java/lang/NullPointerException
            //   261	265	411	finally
            //   271	278	716	java/lang/NullPointerException
            //   271	278	411	finally
            //   284	296	716	java/lang/NullPointerException
            //   284	296	411	finally
            //   302	314	716	java/lang/NullPointerException
            //   302	314	411	finally
            //   342	351	411	finally
            //   356	362	411	finally
            //   367	373	411	finally
            //   378	381	411	finally
            //   395	398	411	finally
            //   403	406	406	java/sql/SQLException
            //   403	406	725	java/lang/NullPointerException
            //   403	406	639	finally
            //   429	435	438	java/sql/SQLException
            //   455	461	464	java/sql/SQLException
            //   518	524	527	java/sql/SQLException
            //   544	550	553	java/sql/SQLException
            //   577	586	639	finally
            //   590	596	599	java/sql/SQLException
            //   616	622	625	java/sql/SQLException
            //   645	652	655	java/sql/SQLException
            //   672	678	681	java/sql/SQLException
          }
        }).start();
    }
  }
  
  public class null extends Thread {
    public final StatementImpl.CancelTask this$1;
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   4: getfield this$0 : Lcom/mysql/jdbc/StatementImpl;
      //   7: getfield physicalConnection : Ljava/lang/ref/Reference;
      //   10: invokevirtual get : ()Ljava/lang/Object;
      //   13: checkcast com/mysql/jdbc/MySQLConnection
      //   16: astore #7
      //   18: aload #7
      //   20: ifnull -> 421
      //   23: aload #7
      //   25: invokeinterface getQueryTimeoutKillsConnection : ()Z
      //   30: ifeq -> 78
      //   33: aload_0
      //   34: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   37: getfield toCancel : Lcom/mysql/jdbc/StatementImpl;
      //   40: astore_1
      //   41: aload_1
      //   42: iconst_1
      //   43: putfield wasCancelled : Z
      //   46: aload_1
      //   47: iconst_1
      //   48: putfield wasCancelledByTimeout : Z
      //   51: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
      //   54: astore_1
      //   55: aload_1
      //   56: ldc 'Statement.ConnectionKilledDueToTimeout'
      //   58: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
      //   61: invokespecial <init> : (Ljava/lang/String;)V
      //   64: aload #7
      //   66: iconst_0
      //   67: iconst_0
      //   68: iconst_1
      //   69: aload_1
      //   70: invokeinterface realClose : (ZZZLjava/lang/Throwable;)V
      //   75: goto -> 421
      //   78: aload_0
      //   79: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   82: getfield this$0 : Lcom/mysql/jdbc/StatementImpl;
      //   85: getfield cancelTimeoutMutex : Ljava/lang/Object;
      //   88: astore #6
      //   90: aload #6
      //   92: monitorenter
      //   93: aload_0
      //   94: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   97: getfield origConnURL : Ljava/lang/String;
      //   100: aload #7
      //   102: invokeinterface getURL : ()Ljava/lang/String;
      //   107: invokevirtual equals : (Ljava/lang/Object;)Z
      //   110: ifeq -> 215
      //   113: aload #7
      //   115: invokeinterface duplicate : ()Lcom/mysql/jdbc/Connection;
      //   120: astore_1
      //   121: aload_1
      //   122: astore_2
      //   123: aload_1
      //   124: invokeinterface createStatement : ()Ljava/sql/Statement;
      //   129: astore #5
      //   131: aload_1
      //   132: astore #4
      //   134: aload #5
      //   136: astore_3
      //   137: new java/lang/StringBuilder
      //   140: astore_2
      //   141: aload_1
      //   142: astore #4
      //   144: aload #5
      //   146: astore_3
      //   147: aload_2
      //   148: invokespecial <init> : ()V
      //   151: aload_1
      //   152: astore #4
      //   154: aload #5
      //   156: astore_3
      //   157: aload_2
      //   158: ldc 'KILL QUERY '
      //   160: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   163: pop
      //   164: aload_1
      //   165: astore #4
      //   167: aload #5
      //   169: astore_3
      //   170: aload_2
      //   171: aload #7
      //   173: invokeinterface getId : ()J
      //   178: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   181: pop
      //   182: aload_1
      //   183: astore #4
      //   185: aload #5
      //   187: astore_3
      //   188: aload #5
      //   190: aload_2
      //   191: invokevirtual toString : ()Ljava/lang/String;
      //   194: invokeinterface execute : (Ljava/lang/String;)Z
      //   199: pop
      //   200: aload_1
      //   201: astore_2
      //   202: aload #5
      //   204: astore_1
      //   205: goto -> 337
      //   208: astore #5
      //   210: aconst_null
      //   211: astore_1
      //   212: goto -> 390
      //   215: aload_0
      //   216: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   219: astore_1
      //   220: aload_1
      //   221: getfield origConnURL : Ljava/lang/String;
      //   224: aload_1
      //   225: getfield origConnProps : Ljava/util/Properties;
      //   228: invokestatic getConnection : (Ljava/lang/String;Ljava/util/Properties;)Ljava/sql/Connection;
      //   231: checkcast com/mysql/jdbc/Connection
      //   234: astore_1
      //   235: aload_1
      //   236: astore_2
      //   237: aload_1
      //   238: invokeinterface createStatement : ()Ljava/sql/Statement;
      //   243: astore #5
      //   245: aload_1
      //   246: astore #4
      //   248: aload #5
      //   250: astore_3
      //   251: new java/lang/StringBuilder
      //   254: astore_2
      //   255: aload_1
      //   256: astore #4
      //   258: aload #5
      //   260: astore_3
      //   261: aload_2
      //   262: invokespecial <init> : ()V
      //   265: aload_1
      //   266: astore #4
      //   268: aload #5
      //   270: astore_3
      //   271: aload_2
      //   272: ldc 'KILL QUERY '
      //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   277: pop
      //   278: aload_1
      //   279: astore #4
      //   281: aload #5
      //   283: astore_3
      //   284: aload_2
      //   285: aload_0
      //   286: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   289: getfield origConnId : J
      //   292: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   295: pop
      //   296: aload_1
      //   297: astore #4
      //   299: aload #5
      //   301: astore_3
      //   302: aload #5
      //   304: aload_2
      //   305: invokevirtual toString : ()Ljava/lang/String;
      //   308: invokeinterface execute : (Ljava/lang/String;)Z
      //   313: pop
      //   314: aload_1
      //   315: astore_2
      //   316: aload #5
      //   318: astore_1
      //   319: goto -> 337
      //   322: astore_2
      //   323: aconst_null
      //   324: astore_3
      //   325: aload_1
      //   326: astore_2
      //   327: aload_3
      //   328: astore_1
      //   329: goto -> 337
      //   332: astore_1
      //   333: aconst_null
      //   334: astore_2
      //   335: aconst_null
      //   336: astore_1
      //   337: aload_2
      //   338: astore #4
      //   340: aload_1
      //   341: astore_3
      //   342: aload_0
      //   343: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   346: getfield toCancel : Lcom/mysql/jdbc/StatementImpl;
      //   349: astore #5
      //   351: aload_2
      //   352: astore #4
      //   354: aload_1
      //   355: astore_3
      //   356: aload #5
      //   358: iconst_1
      //   359: putfield wasCancelled : Z
      //   362: aload_2
      //   363: astore #4
      //   365: aload_1
      //   366: astore_3
      //   367: aload #5
      //   369: iconst_1
      //   370: putfield wasCancelledByTimeout : Z
      //   373: aload_2
      //   374: astore #4
      //   376: aload_1
      //   377: astore_3
      //   378: aload #6
      //   380: monitorexit
      //   381: goto -> 425
      //   384: astore #5
      //   386: aconst_null
      //   387: astore_2
      //   388: aconst_null
      //   389: astore_1
      //   390: aload_2
      //   391: astore #4
      //   393: aload_1
      //   394: astore_3
      //   395: aload #6
      //   397: monitorexit
      //   398: aload_2
      //   399: astore_3
      //   400: aload_1
      //   401: astore #4
      //   403: aload #5
      //   405: athrow
      //   406: astore #5
      //   408: goto -> 572
      //   411: astore #5
      //   413: aload #4
      //   415: astore_2
      //   416: aload_3
      //   417: astore_1
      //   418: goto -> 390
      //   421: aconst_null
      //   422: astore_2
      //   423: aconst_null
      //   424: astore_1
      //   425: aload_1
      //   426: ifnull -> 451
      //   429: aload_1
      //   430: invokeinterface close : ()V
      //   435: goto -> 451
      //   438: astore_1
      //   439: new java/lang/RuntimeException
      //   442: dup
      //   443: aload_1
      //   444: invokevirtual toString : ()Ljava/lang/String;
      //   447: invokespecial <init> : (Ljava/lang/String;)V
      //   450: athrow
      //   451: aload_2
      //   452: ifnull -> 477
      //   455: aload_2
      //   456: invokeinterface close : ()V
      //   461: goto -> 477
      //   464: astore_1
      //   465: new java/lang/RuntimeException
      //   468: dup
      //   469: aload_1
      //   470: invokevirtual toString : ()Ljava/lang/String;
      //   473: invokespecial <init> : (Ljava/lang/String;)V
      //   476: athrow
      //   477: aload_0
      //   478: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   481: astore_1
      //   482: aload_1
      //   483: aconst_null
      //   484: putfield toCancel : Lcom/mysql/jdbc/StatementImpl;
      //   487: aload_1
      //   488: aconst_null
      //   489: putfield origConnProps : Ljava/util/Properties;
      //   492: aload_1
      //   493: aconst_null
      //   494: putfield origConnURL : Ljava/lang/String;
      //   497: goto -> 638
      //   500: astore_1
      //   501: aconst_null
      //   502: astore_3
      //   503: aconst_null
      //   504: astore #4
      //   506: goto -> 640
      //   509: astore_1
      //   510: aconst_null
      //   511: astore_2
      //   512: aconst_null
      //   513: astore_1
      //   514: aload_1
      //   515: ifnull -> 540
      //   518: aload_1
      //   519: invokeinterface close : ()V
      //   524: goto -> 540
      //   527: astore_1
      //   528: new java/lang/RuntimeException
      //   531: dup
      //   532: aload_1
      //   533: invokevirtual toString : ()Ljava/lang/String;
      //   536: invokespecial <init> : (Ljava/lang/String;)V
      //   539: athrow
      //   540: aload_2
      //   541: ifnull -> 477
      //   544: aload_2
      //   545: invokeinterface close : ()V
      //   550: goto -> 477
      //   553: astore_1
      //   554: new java/lang/RuntimeException
      //   557: dup
      //   558: aload_1
      //   559: invokevirtual toString : ()Ljava/lang/String;
      //   562: invokespecial <init> : (Ljava/lang/String;)V
      //   565: athrow
      //   566: astore #5
      //   568: aconst_null
      //   569: astore_2
      //   570: aconst_null
      //   571: astore_1
      //   572: aload_2
      //   573: astore_3
      //   574: aload_1
      //   575: astore #4
      //   577: aload_0
      //   578: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   581: aload #5
      //   583: putfield caughtWhileCancelling : Ljava/sql/SQLException;
      //   586: aload_1
      //   587: ifnull -> 612
      //   590: aload_1
      //   591: invokeinterface close : ()V
      //   596: goto -> 612
      //   599: astore_1
      //   600: new java/lang/RuntimeException
      //   603: dup
      //   604: aload_1
      //   605: invokevirtual toString : ()Ljava/lang/String;
      //   608: invokespecial <init> : (Ljava/lang/String;)V
      //   611: athrow
      //   612: aload_2
      //   613: ifnull -> 477
      //   616: aload_2
      //   617: invokeinterface close : ()V
      //   622: goto -> 477
      //   625: astore_1
      //   626: new java/lang/RuntimeException
      //   629: dup
      //   630: aload_1
      //   631: invokevirtual toString : ()Ljava/lang/String;
      //   634: invokespecial <init> : (Ljava/lang/String;)V
      //   637: athrow
      //   638: return
      //   639: astore_1
      //   640: aload #4
      //   642: ifnull -> 668
      //   645: aload #4
      //   647: invokeinterface close : ()V
      //   652: goto -> 668
      //   655: astore_1
      //   656: new java/lang/RuntimeException
      //   659: dup
      //   660: aload_1
      //   661: invokevirtual toString : ()Ljava/lang/String;
      //   664: invokespecial <init> : (Ljava/lang/String;)V
      //   667: athrow
      //   668: aload_3
      //   669: ifnull -> 694
      //   672: aload_3
      //   673: invokeinterface close : ()V
      //   678: goto -> 694
      //   681: astore_1
      //   682: new java/lang/RuntimeException
      //   685: dup
      //   686: aload_1
      //   687: invokevirtual toString : ()Ljava/lang/String;
      //   690: invokespecial <init> : (Ljava/lang/String;)V
      //   693: athrow
      //   694: aload_0
      //   695: getfield this$1 : Lcom/mysql/jdbc/StatementImpl$CancelTask;
      //   698: astore_2
      //   699: aload_2
      //   700: aconst_null
      //   701: putfield toCancel : Lcom/mysql/jdbc/StatementImpl;
      //   704: aload_2
      //   705: aconst_null
      //   706: putfield origConnProps : Ljava/util/Properties;
      //   709: aload_2
      //   710: aconst_null
      //   711: putfield origConnURL : Ljava/lang/String;
      //   714: aload_1
      //   715: athrow
      //   716: astore_2
      //   717: aload_1
      //   718: astore_2
      //   719: aload #5
      //   721: astore_1
      //   722: goto -> 337
      //   725: astore_3
      //   726: goto -> 514
      // Exception table:
      //   from	to	target	type
      //   0	18	566	java/sql/SQLException
      //   0	18	509	java/lang/NullPointerException
      //   0	18	500	finally
      //   23	75	566	java/sql/SQLException
      //   23	75	509	java/lang/NullPointerException
      //   23	75	500	finally
      //   78	93	566	java/sql/SQLException
      //   78	93	509	java/lang/NullPointerException
      //   78	93	500	finally
      //   93	121	384	finally
      //   123	131	208	finally
      //   137	141	411	finally
      //   147	151	411	finally
      //   157	164	411	finally
      //   170	182	411	finally
      //   188	200	411	finally
      //   215	235	332	java/lang/NullPointerException
      //   215	235	384	finally
      //   237	245	322	java/lang/NullPointerException
      //   237	245	208	finally
      //   251	255	716	java/lang/NullPointerException
      //   251	255	411	finally
      //   261	265	716	java/lang/NullPointerException
      //   261	265	411	finally
      //   271	278	716	java/lang/NullPointerException
      //   271	278	411	finally
      //   284	296	716	java/lang/NullPointerException
      //   284	296	411	finally
      //   302	314	716	java/lang/NullPointerException
      //   302	314	411	finally
      //   342	351	411	finally
      //   356	362	411	finally
      //   367	373	411	finally
      //   378	381	411	finally
      //   395	398	411	finally
      //   403	406	406	java/sql/SQLException
      //   403	406	725	java/lang/NullPointerException
      //   403	406	639	finally
      //   429	435	438	java/sql/SQLException
      //   455	461	464	java/sql/SQLException
      //   518	524	527	java/sql/SQLException
      //   544	550	553	java/sql/SQLException
      //   577	586	639	finally
      //   590	596	599	java/sql/SQLException
      //   616	622	625	java/sql/SQLException
      //   645	652	655	java/sql/SQLException
      //   672	678	681	java/sql/SQLException
    }
  }
}
