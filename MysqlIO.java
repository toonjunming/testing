package com.mysql.jdbc;

import com.mysql.jdbc.authentication.CachingSha2PasswordPlugin;
import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;
import com.mysql.jdbc.authentication.MysqlNativePasswordPlugin;
import com.mysql.jdbc.authentication.MysqlOldPasswordPlugin;
import com.mysql.jdbc.authentication.Sha256PasswordPlugin;
import com.mysql.jdbc.util.ReadAheadInputStream;
import com.mysql.jdbc.util.ResultSetUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.Deflater;

public class MysqlIO {
  static {
    try {
      outputStreamWriter = new OutputStreamWriter();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this();
      this(byteArrayOutputStream);
    } finally {
      exception = null;
    } 
    if (outputStreamWriter != null)
      try {
        outputStreamWriter.close();
      } catch (IOException iOException) {} 
    throw exception;
  }
  
  public MysqlIO(String paramString1, int paramInt1, Properties paramProperties, String paramString2, MySQLConnection paramMySQLConnection, int paramInt2, int paramInt3) throws IOException, SQLException {
    boolean bool = false;
    this.packetSequenceReset = false;
    this.reusablePacket = null;
    this.sendPacket = null;
    this.sharedSendPacket = null;
    this.mysqlOutput = null;
    this.deflater = null;
    this.mysqlInput = null;
    this.packetDebugRingBuffer = null;
    this.streamingData = null;
    this.mysqlConnection = null;
    this.socketFactory = null;
    this.host = null;
    this.serverVersion = null;
    this.socketFactoryClassName = null;
    this.packetHeaderBuf = new byte[4];
    this.colDecimalNeedsBump = false;
    this.hadWarnings = false;
    this.has41NewNewProt = false;
    this.hasLongColumnInfo = false;
    this.isInteractiveClient = false;
    this.logSlowQueries = false;
    this.platformDbCharsetMatches = true;
    this.profileSql = false;
    this.queryBadIndexUsed = false;
    this.queryNoIndexUsed = false;
    this.serverQueryWasSlow = false;
    this.use41Extensions = false;
    this.useCompression = false;
    this.useNewLargePackets = false;
    this.useNewUpdateCounts = false;
    this.packetSequence = 0;
    this.compressedPacketSequence = 0;
    this.readPacketSequence = -1;
    this.checkPacketSequence = false;
    this.protocolVersion = 0;
    this.maxAllowedPacket = 1048576;
    this.maxThreeBytes = 16581375;
    this.port = 3306;
    this.serverMajorVersion = 0;
    this.serverMinorVersion = 0;
    this.oldServerStatus = 0;
    this.serverStatus = 0;
    this.serverSubMinorVersion = 0;
    this.warningCount = 0;
    this.clientParam = 0L;
    this.lastPacketSentTimeMs = 0L;
    this.lastPacketReceivedTimeMs = 0L;
    this.traceProtocol = false;
    this.enablePacketDebug = false;
    this.useDirectRowUnpack = true;
    this.commandCount = 0;
    this.authPluginDataLength = 0;
    this.authenticationPlugins = null;
    this.disabledAuthenticationPlugins = null;
    this.clientDefaultAuthenticationPlugin = null;
    this.clientDefaultAuthenticationPluginName = null;
    this.serverDefaultAuthenticationPluginName = null;
    this.statementExecutionDepth = 0;
    this.connection = paramMySQLConnection;
    if (paramMySQLConnection.getEnablePacketDebug())
      this.packetDebugRingBuffer = new LinkedList<StringBuilder>(); 
    this.traceProtocol = this.connection.getTraceProtocol();
    this.useAutoSlowLog = this.connection.getAutoSlowLog();
    this.useBufferRowSizeThreshold = paramInt3;
    this.useDirectRowUnpack = this.connection.getUseDirectRowUnpack();
    this.logSlowQueries = this.connection.getLogSlowQueries();
    this.reusablePacket = new Buffer(1024);
    this.sendPacket = new Buffer(1024);
    this.port = paramInt1;
    this.host = paramString1;
    this.socketFactoryClassName = paramString2;
    this.socketFactory = createSocketFactory();
    this.exceptionInterceptor = this.connection.getExceptionInterceptor();
    try {
      Socket socket = this.socketFactory.connect(this.host, this.port, paramProperties);
      this.mysqlConnection = socket;
      if (paramInt2 != 0)
        try {
          socket.setSoTimeout(paramInt2);
        } catch (Exception exception) {} 
      this.mysqlConnection = this.socketFactory.beforeHandshake();
      if (this.connection.getUseReadAheadInput()) {
        ReadAheadInputStream readAheadInputStream = new ReadAheadInputStream();
        this(this.mysqlConnection.getInputStream(), 16384, this.connection.getTraceProtocol(), this.connection.getLog());
        this.mysqlInput = readAheadInputStream;
      } else if (this.connection.useUnbufferedInput()) {
        this.mysqlInput = this.mysqlConnection.getInputStream();
      } else {
        BufferedInputStream bufferedInputStream = new BufferedInputStream();
        this(this.mysqlConnection.getInputStream(), 16384);
        this.mysqlInput = bufferedInputStream;
      } 
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      this(this.mysqlConnection.getOutputStream(), 16384);
      this.mysqlOutput = bufferedOutputStream;
      this.isInteractiveClient = this.connection.getInteractiveClient();
      this.profileSql = this.connection.getProfileSql();
      boolean bool1 = this.connection.getAutoGenerateTestcaseScript();
      this.autoGenerateTestcaseScript = bool1;
      if (this.profileSql || this.logSlowQueries || bool1)
        bool = true; 
      this.needToGrabQueryFromPacket = bool;
      if (this.connection.getUseNanosForElapsedTime() && TimeUtil.nanoTimeAvailable()) {
        this.useNanosForElapsedTime = true;
        this.queryTimingUnits = Messages.getString("Nanoseconds");
      } else {
        this.queryTimingUnits = Messages.getString("Milliseconds");
      } 
      if (this.connection.getLogSlowQueries())
        calculateSlowQueryThreshold(); 
      return;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, 0L, 0L, iOException, getExceptionInterceptor());
    } 
  }
  
  private boolean addAuthenticationPlugin(AuthenticationPlugin paramAuthenticationPlugin) throws SQLException {
    String str1;
    boolean bool1;
    boolean bool2;
    String str2 = paramAuthenticationPlugin.getClass().getName();
    String str3 = paramAuthenticationPlugin.getProtocolPluginName();
    List<String> list = this.disabledAuthenticationPlugins;
    null = true;
    if (list != null && list.contains(str2)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    list = this.disabledAuthenticationPlugins;
    if (list != null && list.contains(str3)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool1 || bool2) {
      if (this.clientDefaultAuthenticationPlugin.equals(str2)) {
        if (bool1) {
          str1 = str2;
        } else {
          str1 = str3;
        } 
        throw SQLError.createSQLException(Messages.getString("Connection.BadDisabledAuthenticationPlugin", new Object[] { str1 }), getExceptionInterceptor());
      } 
    } else {
      this.authenticationPlugins.put(str3, str1);
      if (this.clientDefaultAuthenticationPlugin.equals(str2)) {
        this.clientDefaultAuthenticationPluginName = str3;
        return null;
      } 
    } 
    return false;
  }
  
  private int adjustStartForFieldLength(int paramInt1, int paramInt2) {
    return (paramInt2 < 251) ? paramInt1 : ((paramInt2 >= 251 && paramInt2 < 65536) ? (paramInt1 + 2) : ((paramInt2 >= 65536 && paramInt2 < 16777216) ? (paramInt1 + 3) : (paramInt1 + 8)));
  }
  
  private int alignPacketSize(int paramInt1, int paramInt2) {
    return paramInt1 + paramInt2 - 1 & (paramInt2 - 1 ^ 0xFFFFFFFF);
  }
  
  private void appendCharsetByteForHandshake(Buffer paramBuffer, String paramString) throws SQLException {
    byte b1;
    if (paramString != null) {
      b1 = CharsetMapping.getCollationIndexForJavaEncoding(paramString, this.connection);
    } else {
      b1 = 0;
    } 
    byte b2 = b1;
    if (!b1)
      b2 = 33; 
    if (b2 <= 'ÿ') {
      paramBuffer.writeByte((byte)b2);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid character set index for encoding: ");
    stringBuilder.append(paramString);
    throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
  }
  
  private void appendDeadlockStatusInformation(String paramString, StringBuilder paramStringBuilder) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getIncludeInnodbStatusInDeadlockExceptions : ()Z
    //   9: ifeq -> 269
    //   12: aload_1
    //   13: ifnull -> 269
    //   16: aload_1
    //   17: ldc_w '40'
    //   20: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   23: ifne -> 36
    //   26: aload_1
    //   27: ldc_w '41'
    //   30: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   33: ifeq -> 269
    //   36: aload_0
    //   37: getfield streamingData : Lcom/mysql/jdbc/RowData;
    //   40: ifnonnull -> 269
    //   43: aconst_null
    //   44: astore_1
    //   45: aconst_null
    //   46: astore #5
    //   48: aload_0
    //   49: aconst_null
    //   50: ldc_w 'SHOW ENGINE INNODB STATUS'
    //   53: aload_0
    //   54: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   57: invokeinterface getEncoding : ()Ljava/lang/String;
    //   62: aconst_null
    //   63: iconst_m1
    //   64: sipush #1003
    //   67: sipush #1007
    //   70: iconst_0
    //   71: aload_0
    //   72: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   75: invokeinterface getCatalog : ()Ljava/lang/String;
    //   80: aconst_null
    //   81: invokevirtual sqlQueryDirect : (Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/Buffer;IIIZLjava/lang/String;[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   84: astore #6
    //   86: aload #6
    //   88: astore #5
    //   90: aload #6
    //   92: astore_1
    //   93: aload #6
    //   95: invokeinterface next : ()Z
    //   100: ifeq -> 143
    //   103: aload #6
    //   105: astore #5
    //   107: aload #6
    //   109: astore_1
    //   110: aload_2
    //   111: ldc_w '\\n\\n'
    //   114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: aload #6
    //   120: astore #5
    //   122: aload #6
    //   124: astore_1
    //   125: aload_2
    //   126: aload #6
    //   128: ldc_w 'Status'
    //   131: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: goto -> 176
    //   143: aload #6
    //   145: astore #5
    //   147: aload #6
    //   149: astore_1
    //   150: aload_2
    //   151: ldc_w '\\n\\n'
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: aload #6
    //   160: astore #5
    //   162: aload #6
    //   164: astore_1
    //   165: aload_2
    //   166: ldc_w 'MysqlIO.NoInnoDBStatusFound'
    //   169: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   172: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: pop
    //   176: aload #6
    //   178: ifnull -> 269
    //   181: aload #6
    //   183: astore_1
    //   184: aload_1
    //   185: invokeinterface close : ()V
    //   190: goto -> 269
    //   193: astore_1
    //   194: goto -> 255
    //   197: astore #6
    //   199: aload_1
    //   200: astore #5
    //   202: aload_2
    //   203: ldc_w '\\n\\n'
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_1
    //   211: astore #5
    //   213: aload_2
    //   214: ldc_w 'MysqlIO.InnoDBStatusFailed'
    //   217: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: pop
    //   224: aload_1
    //   225: astore #5
    //   227: aload_2
    //   228: ldc_w '\\n\\n'
    //   231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload_1
    //   236: astore #5
    //   238: aload_2
    //   239: aload #6
    //   241: invokestatic stackTraceToString : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: pop
    //   248: aload_1
    //   249: ifnull -> 269
    //   252: goto -> 184
    //   255: aload #5
    //   257: ifnull -> 267
    //   260: aload #5
    //   262: invokeinterface close : ()V
    //   267: aload_1
    //   268: athrow
    //   269: aload_0
    //   270: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   273: invokeinterface getIncludeThreadDumpInDeadlockExceptions : ()Z
    //   278: ifeq -> 728
    //   281: aload_2
    //   282: ldc_w '\\n\\n*** Java threads running at time of deadlock ***\\n\\n'
    //   285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   288: pop
    //   289: invokestatic getThreadMXBean : ()Ljava/lang/management/ThreadMXBean;
    //   292: astore_1
    //   293: aload_1
    //   294: aload_1
    //   295: invokeinterface getAllThreadIds : ()[J
    //   300: ldc_w 2147483647
    //   303: invokeinterface getThreadInfo : ([JI)[Ljava/lang/management/ThreadInfo;
    //   308: astore #5
    //   310: new java/util/ArrayList
    //   313: dup
    //   314: invokespecial <init> : ()V
    //   317: astore_1
    //   318: aload #5
    //   320: arraylength
    //   321: istore #4
    //   323: iconst_0
    //   324: istore_3
    //   325: iload_3
    //   326: iload #4
    //   328: if_icmpge -> 357
    //   331: aload #5
    //   333: iload_3
    //   334: aaload
    //   335: astore #6
    //   337: aload #6
    //   339: ifnull -> 351
    //   342: aload_1
    //   343: aload #6
    //   345: invokeinterface add : (Ljava/lang/Object;)Z
    //   350: pop
    //   351: iinc #3, 1
    //   354: goto -> 325
    //   357: aload_1
    //   358: invokeinterface iterator : ()Ljava/util/Iterator;
    //   363: astore_1
    //   364: aload_1
    //   365: invokeinterface hasNext : ()Z
    //   370: ifeq -> 728
    //   373: aload_1
    //   374: invokeinterface next : ()Ljava/lang/Object;
    //   379: checkcast java/lang/management/ThreadInfo
    //   382: astore #6
    //   384: aload_2
    //   385: bipush #34
    //   387: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   390: pop
    //   391: aload_2
    //   392: aload #6
    //   394: invokevirtual getThreadName : ()Ljava/lang/String;
    //   397: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: pop
    //   401: aload_2
    //   402: ldc_w '" tid='
    //   405: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   408: pop
    //   409: aload_2
    //   410: aload #6
    //   412: invokevirtual getThreadId : ()J
    //   415: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   418: pop
    //   419: aload_2
    //   420: ldc_w ' '
    //   423: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   426: pop
    //   427: aload_2
    //   428: aload #6
    //   430: invokevirtual getThreadState : ()Ljava/lang/Thread$State;
    //   433: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   436: pop
    //   437: aload #6
    //   439: invokevirtual getLockName : ()Ljava/lang/String;
    //   442: ifnull -> 484
    //   445: new java/lang/StringBuilder
    //   448: dup
    //   449: invokespecial <init> : ()V
    //   452: astore #5
    //   454: aload #5
    //   456: ldc_w ' on lock='
    //   459: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   462: pop
    //   463: aload #5
    //   465: aload #6
    //   467: invokevirtual getLockName : ()Ljava/lang/String;
    //   470: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   473: pop
    //   474: aload_2
    //   475: aload #5
    //   477: invokevirtual toString : ()Ljava/lang/String;
    //   480: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   483: pop
    //   484: aload #6
    //   486: invokevirtual isSuspended : ()Z
    //   489: ifeq -> 500
    //   492: aload_2
    //   493: ldc_w ' (suspended)'
    //   496: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   499: pop
    //   500: aload #6
    //   502: invokevirtual isInNative : ()Z
    //   505: ifeq -> 516
    //   508: aload_2
    //   509: ldc_w ' (running in native)'
    //   512: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   515: pop
    //   516: aload #6
    //   518: invokevirtual getStackTrace : ()[Ljava/lang/StackTraceElement;
    //   521: astore #5
    //   523: aload #5
    //   525: arraylength
    //   526: ifle -> 577
    //   529: aload_2
    //   530: ldc_w ' in '
    //   533: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   536: pop
    //   537: aload_2
    //   538: aload #5
    //   540: iconst_0
    //   541: aaload
    //   542: invokevirtual getClassName : ()Ljava/lang/String;
    //   545: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   548: pop
    //   549: aload_2
    //   550: ldc_w '.'
    //   553: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   556: pop
    //   557: aload_2
    //   558: aload #5
    //   560: iconst_0
    //   561: aaload
    //   562: invokevirtual getMethodName : ()Ljava/lang/String;
    //   565: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   568: pop
    //   569: aload_2
    //   570: ldc_w '()'
    //   573: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   576: pop
    //   577: aload_2
    //   578: ldc_w '\\n'
    //   581: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   584: pop
    //   585: aload #6
    //   587: invokevirtual getLockOwnerName : ()Ljava/lang/String;
    //   590: ifnull -> 660
    //   593: new java/lang/StringBuilder
    //   596: dup
    //   597: invokespecial <init> : ()V
    //   600: astore #7
    //   602: aload #7
    //   604: ldc_w '\\t owned by '
    //   607: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   610: pop
    //   611: aload #7
    //   613: aload #6
    //   615: invokevirtual getLockOwnerName : ()Ljava/lang/String;
    //   618: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   621: pop
    //   622: aload #7
    //   624: ldc_w ' Id='
    //   627: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   630: pop
    //   631: aload #7
    //   633: aload #6
    //   635: invokevirtual getLockOwnerId : ()J
    //   638: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   641: pop
    //   642: aload_2
    //   643: aload #7
    //   645: invokevirtual toString : ()Ljava/lang/String;
    //   648: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   651: pop
    //   652: aload_2
    //   653: ldc_w '\\n'
    //   656: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   659: pop
    //   660: iconst_0
    //   661: istore_3
    //   662: iload_3
    //   663: aload #5
    //   665: arraylength
    //   666: if_icmpge -> 364
    //   669: aload #5
    //   671: iload_3
    //   672: aaload
    //   673: astore #7
    //   675: new java/lang/StringBuilder
    //   678: dup
    //   679: invokespecial <init> : ()V
    //   682: astore #6
    //   684: aload #6
    //   686: ldc_w '\\tat '
    //   689: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   692: pop
    //   693: aload #6
    //   695: aload #7
    //   697: invokevirtual toString : ()Ljava/lang/String;
    //   700: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   703: pop
    //   704: aload_2
    //   705: aload #6
    //   707: invokevirtual toString : ()Ljava/lang/String;
    //   710: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   713: pop
    //   714: aload_2
    //   715: ldc_w '\\n'
    //   718: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   721: pop
    //   722: iinc #3, 1
    //   725: goto -> 662
    //   728: return
    // Exception table:
    //   from	to	target	type
    //   48	86	197	java/lang/Exception
    //   48	86	193	finally
    //   93	103	197	java/lang/Exception
    //   93	103	193	finally
    //   110	118	197	java/lang/Exception
    //   110	118	193	finally
    //   125	140	197	java/lang/Exception
    //   125	140	193	finally
    //   150	158	197	java/lang/Exception
    //   150	158	193	finally
    //   165	176	197	java/lang/Exception
    //   165	176	193	finally
    //   202	210	193	finally
    //   213	224	193	finally
    //   227	235	193	finally
    //   238	248	193	finally
  }
  
  private ResultSetImpl buildResultSetWithRows(StatementImpl paramStatementImpl, String paramString, Field[] paramArrayOfField, RowData paramRowData, int paramInt1, int paramInt2, boolean paramBoolean) throws SQLException {
    ResultSetImpl resultSetImpl;
    if (paramInt2 != 1007) {
      if (paramInt2 != 1008)
        return ResultSetImpl.getInstance(paramString, paramArrayOfField, paramRowData, this.connection, paramStatementImpl, false); 
      resultSetImpl = ResultSetImpl.getInstance(paramString, paramArrayOfField, paramRowData, this.connection, paramStatementImpl, true);
    } else {
      ResultSetImpl resultSetImpl1 = ResultSetImpl.getInstance(paramString, paramArrayOfField, paramRowData, this.connection, (StatementImpl)resultSetImpl, false);
      resultSetImpl = resultSetImpl1;
      if (paramBoolean) {
        resultSetImpl1.setBinaryEncoded();
        resultSetImpl = resultSetImpl1;
      } 
    } 
    resultSetImpl.setResultSetType(paramInt1);
    resultSetImpl.setResultSetConcurrency(paramInt2);
    return resultSetImpl;
  }
  
  private ResultSetImpl buildResultSetWithUpdates(StatementImpl paramStatementImpl, Buffer paramBuffer) throws SQLException {
    try {
      long l1;
      long l2;
      if (this.useNewUpdateCounts) {
        l1 = paramBuffer.newReadLength();
        l2 = paramBuffer.newReadLength();
      } else {
        l1 = paramBuffer.readLength();
        l2 = paramBuffer.readLength();
      } 
      if (this.use41Extensions) {
        this.serverStatus = paramBuffer.readInt();
        checkTransactionState(this.oldServerStatus);
        int i = paramBuffer.readInt();
        this.warningCount = i;
        if (i > 0)
          this.hadWarnings = true; 
        paramBuffer.readByte();
        setServerSlowQueryFlags();
      } 
      if (this.connection.isReadInfoMsgEnabled()) {
        String str = paramBuffer.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
      } else {
        paramBuffer = null;
      } 
      ResultSetImpl resultSetImpl = ResultSetImpl.getInstance(l1, l2, this.connection, paramStatementImpl);
      if (paramBuffer != null)
        resultSetImpl.setServerInfo((String)paramBuffer); 
      return resultSetImpl;
    } catch (Exception exception) {
      SQLException sQLException = SQLError.createSQLException(SQLError.get("S1000"), "S1000", -1, getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void calculateSlowQueryThreshold() {
    this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();
    if (this.connection.getUseNanosForElapsedTime()) {
      long l = this.connection.getSlowQueryThresholdNanos();
      if (l != 0L) {
        this.slowQueryThreshold = l;
      } else {
        this.slowQueryThreshold *= 1000000L;
      } 
    } 
  }
  
  private void changeDatabaseTo(String paramString) throws SQLException {
    if (paramString == null || paramString.length() == 0)
      return; 
    try {
      sendCommand(2, paramString, null, false, null, 0);
    } catch (Exception exception) {
      if (this.connection.getCreateDatabaseIfNotExist()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE DATABASE IF NOT EXISTS ");
        stringBuilder.append(paramString);
        sendCommand(3, stringBuilder.toString(), null, false, null, 0);
        sendCommand(2, paramString, null, false, null, 0);
        return;
      } 
    } 
  }
  
  private void checkConfidentiality(AuthenticationPlugin paramAuthenticationPlugin) throws SQLException {
    if (!paramAuthenticationPlugin.requiresConfidentiality() || isSSLEstablished())
      return; 
    throw SQLError.createSQLException(Messages.getString("Connection.AuthenticationPluginRequiresSSL", new Object[] { paramAuthenticationPlugin.getProtocolPluginName() }), getExceptionInterceptor());
  }
  
  private Buffer checkErrorPacket(int paramInt) throws SQLException {
    this.serverStatus = 0;
    try {
      Buffer buffer = reuseAndReadPacket(this.reusablePacket);
      checkErrorPacket(buffer);
      return buffer;
    } catch (SQLException sQLException) {
      throw sQLException;
    } catch (Exception exception) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, exception, getExceptionInterceptor());
    } 
  }
  
  private void checkErrorPacket(Buffer paramBuffer) throws SQLException {
    if (paramBuffer.readByte() == -1) {
      String str1;
      if (this.protocolVersion > 9) {
        int i = paramBuffer.readInt();
        String str3 = paramBuffer.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
        if (str3.charAt(0) == '#') {
          if (str3.length() > 6) {
            String str6 = str3.substring(1, 6);
            String str5 = str3.substring(6);
            str1 = str6;
            str3 = str5;
            if (str6.equals("HY000")) {
              str1 = SQLError.mysqlToSqlState(i, this.connection.getUseSqlStateCodes());
              str3 = str5;
            } 
          } else {
            str1 = SQLError.mysqlToSqlState(i, this.connection.getUseSqlStateCodes());
          } 
        } else {
          str1 = SQLError.mysqlToSqlState(i, this.connection.getUseSqlStateCodes());
        } 
        clearInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        String str4 = SQLError.get(str1);
        if (!this.connection.getUseOnlyServerErrorMessages() && str4 != null) {
          stringBuilder.append(str4);
          stringBuilder.append(Messages.getString("MysqlIO.68"));
        } 
        stringBuilder.append(str3);
        if (!this.connection.getUseOnlyServerErrorMessages() && str4 != null)
          stringBuilder.append("\""); 
        appendDeadlockStatusInformation(str1, stringBuilder);
        if (str1 != null && str1.startsWith("22"))
          throw new MysqlDataTruncation(stringBuilder.toString(), 0, true, false, 0, 0, i); 
        throw SQLError.createSQLException(stringBuilder.toString(), str1, i, false, getExceptionInterceptor(), this.connection);
      } 
      String str2 = str1.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
      clearInputStream();
      if (str2.indexOf(Messages.getString("MysqlIO.70")) != -1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SQLError.get("S0022"));
        stringBuilder.append(", ");
        stringBuilder.append(str2);
        throw SQLError.createSQLException(stringBuilder.toString(), "S0022", -1, false, getExceptionInterceptor(), this.connection);
      } 
      StringBuilder stringBuilder1 = new StringBuilder(Messages.getString("MysqlIO.72"));
      stringBuilder1.append(str2);
      stringBuilder1.append("\"");
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(SQLError.get("S1000"));
      stringBuilder2.append(", ");
      stringBuilder2.append(stringBuilder1.toString());
      throw SQLError.createSQLException(stringBuilder2.toString(), "S1000", -1, false, getExceptionInterceptor(), this.connection);
    } 
  }
  
  private void checkForOutstandingStreamingData() throws SQLException {
    if (this.streamingData != null)
      if (this.connection.getClobberStreamingResults()) {
        this.streamingData.getOwner().realClose(false);
        clearInputStream();
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("MysqlIO.39"));
        stringBuilder.append(this.streamingData);
        stringBuilder.append(Messages.getString("MysqlIO.40"));
        stringBuilder.append(Messages.getString("MysqlIO.41"));
        stringBuilder.append(Messages.getString("MysqlIO.42"));
        throw SQLError.createSQLException(stringBuilder.toString(), getExceptionInterceptor());
      }  
  }
  
  private void checkPacketSequencing(byte paramByte) throws SQLException {
    if (paramByte != Byte.MIN_VALUE || this.readPacketSequence == Byte.MAX_VALUE) {
      byte b = this.readPacketSequence;
      if (b != -1 || paramByte == 0) {
        if (paramByte == Byte.MIN_VALUE || b == -1 || paramByte == b + 1)
          return; 
        MySQLConnection mySQLConnection2 = this.connection;
        long l5 = this.lastPacketSentTimeMs;
        long l6 = this.lastPacketReceivedTimeMs;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Packets out of order, expected packet # ");
        stringBuilder2.append(this.readPacketSequence + 1);
        stringBuilder2.append(", but received packet # ");
        stringBuilder2.append(paramByte);
        throw SQLError.createCommunicationsException(mySQLConnection2, l5, l6, new IOException(stringBuilder2.toString()), getExceptionInterceptor());
      } 
      MySQLConnection mySQLConnection1 = this.connection;
      long l4 = this.lastPacketSentTimeMs;
      long l3 = this.lastPacketReceivedTimeMs;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Packets out of order, expected packet # -1, but received packet # ");
      stringBuilder1.append(paramByte);
      throw SQLError.createCommunicationsException(mySQLConnection1, l4, l3, new IOException(stringBuilder1.toString()), getExceptionInterceptor());
    } 
    MySQLConnection mySQLConnection = this.connection;
    long l1 = this.lastPacketSentTimeMs;
    long l2 = this.lastPacketReceivedTimeMs;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Packets out of order, expected packet # -128, but received packet # ");
    stringBuilder.append(paramByte);
    throw SQLError.createCommunicationsException(mySQLConnection, l1, l2, new IOException(stringBuilder.toString()), getExceptionInterceptor());
  }
  
  private void checkTransactionState(int paramInt) throws SQLException {
    boolean bool = true;
    if ((paramInt & 0x1) != 0) {
      paramInt = bool;
    } else {
      paramInt = 0;
    } 
    boolean bool1 = inTransactionOnServer();
    if (paramInt != 0 && !bool1) {
      this.connection.transactionCompleted();
    } else if (paramInt == 0 && bool1) {
      this.connection.transactionBegun();
    } 
  }
  
  private Buffer compressPacket(Buffer paramBuffer, int paramInt1, int paramInt2) throws SQLException {
    byte[] arrayOfByte;
    if (paramInt2 < 50) {
      arrayOfByte = paramBuffer.getByteBuffer();
    } else {
      byte[] arrayOfByte2 = arrayOfByte.getByteBuffer();
      byte[] arrayOfByte1 = new byte[arrayOfByte2.length * 2];
      if (this.deflater == null)
        this.deflater = new Deflater(); 
      this.deflater.reset();
      this.deflater.setInput(arrayOfByte2, paramInt1, paramInt2);
      this.deflater.finish();
      int j = this.deflater.deflate(arrayOfByte1);
      if (j > paramInt2) {
        arrayOfByte = arrayOfByte.getByteBuffer();
      } else {
        arrayOfByte = arrayOfByte1;
        boolean bool1 = false;
        paramInt1 = j;
        j = paramInt2;
        Buffer buffer1 = new Buffer(paramInt1 + 7);
        buffer1.setPosition(0);
        buffer1.writeLongInt(paramInt1);
        buffer1.writeByte(this.compressedPacketSequence);
        buffer1.writeLongInt(j);
        buffer1.writeBytesNoNull(arrayOfByte, bool1, paramInt1);
        return buffer1;
      } 
    } 
    boolean bool = false;
    int i = paramInt1;
    paramInt1 = paramInt2;
    Buffer buffer = new Buffer(paramInt1 + 7);
    buffer.setPosition(0);
    buffer.writeLongInt(paramInt1);
    buffer.writeByte(this.compressedPacketSequence);
    buffer.writeLongInt(bool);
    buffer.writeBytesNoNull(arrayOfByte, i, paramInt1);
    return buffer;
  }
  
  private SocketFactory createSocketFactory() throws SQLException {
    try {
      String str = this.socketFactoryClassName;
      if (str != null)
        return (SocketFactory)Class.forName(str).newInstance(); 
      throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), "08001", getExceptionInterceptor());
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Messages.getString("MysqlIO.76"));
      stringBuilder.append(this.socketFactoryClassName);
      stringBuilder.append(Messages.getString("MysqlIO.77"));
      SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "08001", getExceptionInterceptor());
      sQLException.initCause(exception);
      throw sQLException;
    } 
  }
  
  private void enqueuePacketForDebugging(boolean paramBoolean1, boolean paramBoolean2, int paramInt, byte[] paramArrayOfbyte, Buffer paramBuffer) throws SQLException {
    StringBuilder stringBuilder;
    if (this.packetDebugRingBuffer.size() + 1 > this.connection.getPacketDebugBufferSize())
      this.packetDebugRingBuffer.removeFirst(); 
    if (!paramBoolean1) {
      String str1;
      paramInt = Math.min(1024, paramBuffer.getBufLength());
      Buffer buffer = new Buffer(paramInt + 4);
      buffer.setPosition(0);
      buffer.writeBytesNoNull(paramArrayOfbyte);
      buffer.writeBytesNoNull(paramBuffer.getBytes(0, paramInt));
      String str2 = buffer.dump(paramInt);
      StringBuilder stringBuilder1 = new StringBuilder(str2.length() + 96);
      stringBuilder1.append("Server ");
      if (paramBoolean2) {
        str1 = "(re-used) ";
      } else {
        str1 = "(new) ";
      } 
      stringBuilder1.append(str1);
      stringBuilder1.append(paramBuffer.toSuperString());
      stringBuilder1.append(" --------------------> Client\n");
      stringBuilder1.append("\nPacket payload:\n\n");
      stringBuilder1.append(str2);
      stringBuilder = stringBuilder1;
      if (paramInt == 1024) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nNote: Packet of ");
        stringBuilder.append(paramBuffer.getBufLength());
        stringBuilder.append(" bytes truncated to ");
        stringBuilder.append(1024);
        stringBuilder.append(" bytes.\n");
        stringBuilder1.append(stringBuilder.toString());
        stringBuilder = stringBuilder1;
      } 
    } else {
      int i = Math.min(1024, paramInt);
      String str = paramBuffer.dump(i);
      StringBuilder stringBuilder1 = new StringBuilder(str.length() + 68);
      stringBuilder1.append("Client ");
      stringBuilder1.append(paramBuffer.toSuperString());
      stringBuilder1.append("--------------------> Server\n");
      stringBuilder1.append("\nPacket payload:\n\n");
      stringBuilder1.append(str);
      stringBuilder = stringBuilder1;
      if (i == 1024) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nNote: Packet of ");
        stringBuilder.append(paramInt);
        stringBuilder.append(" bytes truncated to ");
        stringBuilder.append(1024);
        stringBuilder.append(" bytes.\n");
        stringBuilder1.append(stringBuilder.toString());
        stringBuilder = stringBuilder1;
      } 
    } 
    this.packetDebugRingBuffer.addLast(stringBuilder);
  }
  
  private final void extractNativeEncodedColumn(Buffer paramBuffer, Field[] paramArrayOfField, int paramInt, byte[][] paramArrayOfbyte) throws SQLException {
    Field field = paramArrayOfField[paramInt];
    int i = field.getMysqlType();
    if (i != 15 && i != 16 && i != 245 && i != 246) {
      StringBuilder stringBuilder;
      switch (i) {
        default:
          switch (i) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append(Messages.getString("MysqlIO.97"));
              stringBuilder.append(field.getMysqlType());
              stringBuilder.append(Messages.getString("MysqlIO.98"));
              stringBuilder.append(paramInt);
              stringBuilder.append(Messages.getString("MysqlIO.99"));
              stringBuilder.append(paramArrayOfField.length);
              stringBuilder.append(Messages.getString("MysqlIO.100"));
              throw SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
            case 249:
            case 250:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
              break;
          } 
        case 11:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes((int)stringBuilder.readFieldLength());
          break;
        case 10:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes((int)stringBuilder.readFieldLength());
          break;
        case 8:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes(8);
          break;
        case 7:
        case 12:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes((int)stringBuilder.readFieldLength());
          break;
        case 5:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes(8);
          break;
        case 4:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes(4);
          break;
        case 3:
        case 9:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes(4);
          break;
        case 2:
        case 13:
          paramArrayOfbyte[paramInt] = stringBuilder.getBytes(2);
          break;
        case 1:
          (new byte[1])[0] = stringBuilder.readByte();
          paramArrayOfbyte[paramInt] = new byte[1];
          break;
        case 0:
          paramArrayOfbyte[paramInt] = stringBuilder.readLenByteArray(0);
          break;
        case 6:
          break;
      } 
      return;
    } 
  }
  
  private AuthenticationPlugin getAuthenticationPlugin(String paramString) throws SQLException {
    SQLException sQLException;
    AuthenticationPlugin authenticationPlugin2 = this.authenticationPlugins.get(paramString);
    AuthenticationPlugin authenticationPlugin1 = authenticationPlugin2;
    if (authenticationPlugin2 != null) {
      authenticationPlugin1 = authenticationPlugin2;
      if (!authenticationPlugin2.isReusable())
        try {
        
        } finally {
          Exception exception2 = null;
          authenticationPlugin1 = authenticationPlugin2;
          Exception exception1 = exception2;
          sQLException = SQLError.createSQLException(Messages.getString("Connection.BadAuthenticationPlugin", new Object[] { authenticationPlugin1.getClass().getName() }), getExceptionInterceptor());
          sQLException.initCause(exception1);
        }  
    } 
    return (AuthenticationPlugin)sQLException;
  }
  
  private Properties getConnectionAttributesAsProperties(String paramString) throws SQLException {
    Properties properties = new Properties();
    if (paramString != null)
      for (String paramString : paramString.split(",")) {
        int i = paramString.indexOf(":");
        if (i > 0) {
          int j = i + 1;
          if (j < paramString.length())
            properties.setProperty(paramString.substring(0, i), paramString.substring(j)); 
        } 
      }  
    properties.setProperty("_client_name", "MySQL Connector Java");
    properties.setProperty("_client_version", "5.1.47");
    properties.setProperty("_runtime_vendor", NonRegisteringDriver.RUNTIME_VENDOR);
    properties.setProperty("_runtime_version", NonRegisteringDriver.RUNTIME_VERSION);
    properties.setProperty("_client_license", "GPL");
    return properties;
  }
  
  public static int getMaxBuf() {
    return maxBufferSize;
  }
  
  private static final String getPacketDumpToLog(Buffer paramBuffer, int paramInt) {
    if (paramInt < 1024)
      return paramBuffer.dump(paramInt); 
    StringBuilder stringBuilder = new StringBuilder(4096);
    stringBuilder.append(paramBuffer.dump(1024));
    stringBuilder.append(Messages.getString("MysqlIO.36"));
    stringBuilder.append(1024);
    stringBuilder.append(Messages.getString("MysqlIO.37"));
    return stringBuilder.toString();
  }
  
  private void loadAuthenticationPlugins() throws SQLException {
    String str = this.connection.getDefaultAuthenticationPlugin();
    this.clientDefaultAuthenticationPlugin = str;
    if (str != null && !"".equals(str.trim())) {
      str = this.connection.getDisabledAuthenticationPlugins();
      if (str != null && !"".equals(str)) {
        this.disabledAuthenticationPlugins = new ArrayList<String>();
        Iterator<String> iterator = StringUtils.split(str, ",", true).iterator();
        while (iterator.hasNext())
          this.disabledAuthenticationPlugins.add(iterator.next()); 
      } 
      this.authenticationPlugins = new HashMap<String, AuthenticationPlugin>();
      MysqlOldPasswordPlugin mysqlOldPasswordPlugin = new MysqlOldPasswordPlugin();
      MySQLConnection mySQLConnection4 = this.connection;
      mysqlOldPasswordPlugin.init(mySQLConnection4, mySQLConnection4.getProperties());
      boolean bool1 = addAuthenticationPlugin(mysqlOldPasswordPlugin);
      MysqlNativePasswordPlugin mysqlNativePasswordPlugin = new MysqlNativePasswordPlugin();
      mySQLConnection4 = this.connection;
      mysqlNativePasswordPlugin.init(mySQLConnection4, mySQLConnection4.getProperties());
      if (addAuthenticationPlugin(mysqlNativePasswordPlugin))
        bool1 = true; 
      MysqlClearPasswordPlugin mysqlClearPasswordPlugin = new MysqlClearPasswordPlugin();
      MySQLConnection mySQLConnection2 = this.connection;
      mysqlClearPasswordPlugin.init(mySQLConnection2, mySQLConnection2.getProperties());
      if (addAuthenticationPlugin(mysqlClearPasswordPlugin))
        bool1 = true; 
      Sha256PasswordPlugin sha256PasswordPlugin = new Sha256PasswordPlugin();
      MySQLConnection mySQLConnection3 = this.connection;
      sha256PasswordPlugin.init(mySQLConnection3, mySQLConnection3.getProperties());
      if (addAuthenticationPlugin(sha256PasswordPlugin))
        bool1 = true; 
      CachingSha2PasswordPlugin cachingSha2PasswordPlugin = new CachingSha2PasswordPlugin();
      MySQLConnection mySQLConnection1 = this.connection;
      cachingSha2PasswordPlugin.init(mySQLConnection1, mySQLConnection1.getProperties());
      if (addAuthenticationPlugin(cachingSha2PasswordPlugin))
        bool1 = true; 
      String str1 = this.connection.getAuthenticationPlugins();
      boolean bool2 = bool1;
      if (str1 != null) {
        bool2 = bool1;
        if (!"".equals(str1)) {
          mySQLConnection1 = this.connection;
          Iterator<Extension> iterator = Util.loadExtensions(mySQLConnection1, mySQLConnection1.getProperties(), str1, "Connection.BadAuthenticationPlugin", getExceptionInterceptor()).iterator();
          while (true) {
            bool2 = bool1;
            if (iterator.hasNext()) {
              if (addAuthenticationPlugin((AuthenticationPlugin)iterator.next()))
                bool1 = true; 
              continue;
            } 
            break;
          } 
        } 
      } 
      if (bool2)
        return; 
      throw SQLError.createSQLException(Messages.getString("Connection.DefaultAuthenticationPluginIsNotListed", new Object[] { this.clientDefaultAuthenticationPlugin }), getExceptionInterceptor());
    } 
    throw SQLError.createSQLException(Messages.getString("Connection.BadDefaultAuthenticationPlugin", new Object[] { this.clientDefaultAuthenticationPlugin }), getExceptionInterceptor());
  }
  
  private void negotiateSSLConnection(String paramString1, String paramString2, String paramString3, int paramInt) throws SQLException {
    if (ExportControlled.enabled()) {
      if ((this.serverCapabilities & 0x8000) != 0)
        this.clientParam |= 0x8000L; 
      this.clientParam |= 0x800L;
      Buffer buffer = new Buffer(paramInt);
      if (this.use41Extensions) {
        buffer.writeLong(this.clientParam);
        buffer.writeLong(this.maxThreeBytes);
        appendCharsetByteForHandshake(buffer, getEncodingForHandshake());
        buffer.writeBytesNoNull(new byte[23]);
      } else {
        buffer.writeInt((int)this.clientParam);
      } 
      send(buffer, buffer.getPosition());
      ExportControlled.transformSocketToSSLSocket(this);
      return;
    } 
    throw new ConnectionFeatureNotAvailableException(this.connection, this.lastPacketSentTimeMs, null);
  }
  
  private void preserveOldTransactionState() {
    this.serverStatus |= this.oldServerStatus & 0x1;
  }
  
  private void proceedHandshakeWithPluggableAuthentication(String paramString1, String paramString2, String paramString3, Buffer paramBuffer) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield authenticationPlugins : Ljava/util/Map;
    //   4: ifnonnull -> 11
    //   7: aload_0
    //   8: invokespecial loadAuthenticationPlugins : ()V
    //   11: aload_1
    //   12: ifnull -> 24
    //   15: aload_1
    //   16: invokevirtual length : ()I
    //   19: istore #5
    //   21: goto -> 27
    //   24: iconst_0
    //   25: istore #5
    //   27: aload_3
    //   28: ifnull -> 40
    //   31: aload_3
    //   32: invokevirtual length : ()I
    //   35: istore #6
    //   37: goto -> 43
    //   40: iconst_0
    //   41: istore #6
    //   43: iload #5
    //   45: bipush #16
    //   47: iadd
    //   48: iload #6
    //   50: iadd
    //   51: iconst_3
    //   52: imul
    //   53: bipush #7
    //   55: iadd
    //   56: iconst_4
    //   57: iadd
    //   58: bipush #33
    //   60: iadd
    //   61: istore #7
    //   63: new java/util/ArrayList
    //   66: dup
    //   67: invokespecial <init> : ()V
    //   70: astore #15
    //   72: bipush #100
    //   74: istore #9
    //   76: iconst_0
    //   77: istore #11
    //   79: aconst_null
    //   80: astore #12
    //   82: iconst_0
    //   83: istore #5
    //   85: iconst_0
    //   86: istore #8
    //   88: aload #4
    //   90: astore #13
    //   92: iload #9
    //   94: iconst_1
    //   95: isub
    //   96: istore #10
    //   98: iload #9
    //   100: ifle -> 1676
    //   103: iload #11
    //   105: ifne -> 626
    //   108: aload #13
    //   110: ifnull -> 568
    //   113: aload #13
    //   115: invokevirtual isOKPacket : ()Z
    //   118: ifne -> 540
    //   121: aload_0
    //   122: aload_0
    //   123: getfield clientParam : J
    //   126: ldc2_w 696833
    //   129: lor
    //   130: putfield clientParam : J
    //   133: aload_0
    //   134: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   137: invokeinterface getAllowMultiQueries : ()Z
    //   142: ifeq -> 157
    //   145: aload_0
    //   146: aload_0
    //   147: getfield clientParam : J
    //   150: ldc2_w 65536
    //   153: lor
    //   154: putfield clientParam : J
    //   157: aload_0
    //   158: getfield serverCapabilities : I
    //   161: ldc 4194304
    //   163: iand
    //   164: ifeq -> 191
    //   167: aload_0
    //   168: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   171: invokeinterface getDisconnectOnExpiredPasswords : ()Z
    //   176: ifne -> 191
    //   179: aload_0
    //   180: aload_0
    //   181: getfield clientParam : J
    //   184: ldc2_w 4194304
    //   187: lor
    //   188: putfield clientParam : J
    //   191: aload_0
    //   192: getfield serverCapabilities : I
    //   195: ldc 1048576
    //   197: iand
    //   198: ifeq -> 230
    //   201: ldc 'none'
    //   203: aload_0
    //   204: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   207: invokeinterface getConnectionAttributes : ()Ljava/lang/String;
    //   212: invokevirtual equals : (Ljava/lang/Object;)Z
    //   215: ifne -> 230
    //   218: aload_0
    //   219: aload_0
    //   220: getfield clientParam : J
    //   223: ldc2_w 1048576
    //   226: lor
    //   227: putfield clientParam : J
    //   230: aload_0
    //   231: getfield serverCapabilities : I
    //   234: ldc 2097152
    //   236: iand
    //   237: ifeq -> 252
    //   240: aload_0
    //   241: aload_0
    //   242: getfield clientParam : J
    //   245: ldc2_w 2097152
    //   248: lor
    //   249: putfield clientParam : J
    //   252: aload_0
    //   253: iconst_1
    //   254: putfield has41NewNewProt : Z
    //   257: aload_0
    //   258: iconst_1
    //   259: putfield use41Extensions : Z
    //   262: aload_0
    //   263: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   266: invokeinterface getUseSSL : ()Z
    //   271: ifeq -> 286
    //   274: aload_0
    //   275: aload_1
    //   276: aload_2
    //   277: aload_3
    //   278: iload #7
    //   280: invokespecial negotiateSSLConnection : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
    //   283: goto -> 286
    //   286: aload_0
    //   287: getfield serverCapabilities : I
    //   290: ldc 524288
    //   292: iand
    //   293: ifeq -> 370
    //   296: aload_0
    //   297: iconst_5
    //   298: iconst_5
    //   299: bipush #10
    //   301: invokevirtual versionMeetsMinimum : (III)Z
    //   304: ifeq -> 349
    //   307: aload_0
    //   308: iconst_5
    //   309: bipush #6
    //   311: iconst_0
    //   312: invokevirtual versionMeetsMinimum : (III)Z
    //   315: ifeq -> 332
    //   318: aload_0
    //   319: iconst_5
    //   320: bipush #6
    //   322: iconst_2
    //   323: invokevirtual versionMeetsMinimum : (III)Z
    //   326: ifne -> 332
    //   329: goto -> 349
    //   332: aload #13
    //   334: ldc_w 'ASCII'
    //   337: aload_0
    //   338: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   341: invokevirtual readString : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/lang/String;
    //   344: astore #12
    //   346: goto -> 373
    //   349: aload #13
    //   351: ldc_w 'ASCII'
    //   354: aload_0
    //   355: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   358: aload_0
    //   359: getfield authPluginDataLength : I
    //   362: invokevirtual readString : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;I)Ljava/lang/String;
    //   365: astore #12
    //   367: goto -> 373
    //   370: aconst_null
    //   371: astore #12
    //   373: aload_0
    //   374: aload #12
    //   376: invokespecial getAuthenticationPlugin : (Ljava/lang/String;)Lcom/mysql/jdbc/AuthenticationPlugin;
    //   379: astore #14
    //   381: aload #14
    //   383: ifnonnull -> 403
    //   386: aload_0
    //   387: aload_0
    //   388: getfield clientDefaultAuthenticationPluginName : Ljava/lang/String;
    //   391: invokespecial getAuthenticationPlugin : (Ljava/lang/String;)Lcom/mysql/jdbc/AuthenticationPlugin;
    //   394: astore #4
    //   396: iload #5
    //   398: istore #6
    //   400: goto -> 500
    //   403: aload #14
    //   405: astore #4
    //   407: iload #5
    //   409: istore #6
    //   411: aload #12
    //   413: getstatic com/mysql/jdbc/authentication/Sha256PasswordPlugin.PLUGIN_NAME : Ljava/lang/String;
    //   416: invokevirtual equals : (Ljava/lang/Object;)Z
    //   419: ifeq -> 500
    //   422: aload #14
    //   424: astore #4
    //   426: iload #5
    //   428: istore #6
    //   430: aload_0
    //   431: invokevirtual isSSLEstablished : ()Z
    //   434: ifne -> 500
    //   437: aload #14
    //   439: astore #4
    //   441: iload #5
    //   443: istore #6
    //   445: aload_0
    //   446: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   449: invokeinterface getServerRSAPublicKeyFile : ()Ljava/lang/String;
    //   454: ifnonnull -> 500
    //   457: aload #14
    //   459: astore #4
    //   461: iload #5
    //   463: istore #6
    //   465: aload_0
    //   466: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   469: invokeinterface getAllowPublicKeyRetrieval : ()Z
    //   474: ifne -> 500
    //   477: aload_0
    //   478: aload_0
    //   479: getfield clientDefaultAuthenticationPluginName : Ljava/lang/String;
    //   482: invokespecial getAuthenticationPlugin : (Ljava/lang/String;)Lcom/mysql/jdbc/AuthenticationPlugin;
    //   485: astore #4
    //   487: aload_0
    //   488: getfield clientDefaultAuthenticationPluginName : Ljava/lang/String;
    //   491: aload #12
    //   493: invokevirtual equals : (Ljava/lang/Object;)Z
    //   496: iconst_1
    //   497: ixor
    //   498: istore #6
    //   500: aload_0
    //   501: aload #4
    //   503: invokeinterface getProtocolPluginName : ()Ljava/lang/String;
    //   508: putfield serverDefaultAuthenticationPluginName : Ljava/lang/String;
    //   511: aload_0
    //   512: aload #4
    //   514: invokespecial checkConfidentiality : (Lcom/mysql/jdbc/AuthenticationPlugin;)V
    //   517: new com/mysql/jdbc/Buffer
    //   520: dup
    //   521: aload_0
    //   522: getfield seed : Ljava/lang/String;
    //   525: invokestatic getBytes : (Ljava/lang/String;)[B
    //   528: invokespecial <init> : ([B)V
    //   531: astore #12
    //   533: iload #6
    //   535: istore #5
    //   537: goto -> 619
    //   540: ldc_w 'Connection.UnexpectedAuthenticationApproval'
    //   543: iconst_1
    //   544: anewarray java/lang/Object
    //   547: dup
    //   548: iconst_0
    //   549: aload #12
    //   551: invokeinterface getProtocolPluginName : ()Ljava/lang/String;
    //   556: aastore
    //   557: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   560: aload_0
    //   561: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   564: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   567: athrow
    //   568: aload_0
    //   569: getfield serverDefaultAuthenticationPluginName : Ljava/lang/String;
    //   572: astore #12
    //   574: aload #12
    //   576: astore #4
    //   578: aload #12
    //   580: ifnonnull -> 589
    //   583: aload_0
    //   584: getfield clientDefaultAuthenticationPluginName : Ljava/lang/String;
    //   587: astore #4
    //   589: aload_0
    //   590: aload #4
    //   592: invokespecial getAuthenticationPlugin : (Ljava/lang/String;)Lcom/mysql/jdbc/AuthenticationPlugin;
    //   595: astore #4
    //   597: aload_0
    //   598: aload #4
    //   600: invokespecial checkConfidentiality : (Lcom/mysql/jdbc/AuthenticationPlugin;)V
    //   603: new com/mysql/jdbc/Buffer
    //   606: dup
    //   607: aload_0
    //   608: getfield seed : Ljava/lang/String;
    //   611: invokestatic getBytes : (Ljava/lang/String;)[B
    //   614: invokespecial <init> : ([B)V
    //   617: astore #12
    //   619: iload #8
    //   621: istore #6
    //   623: goto -> 948
    //   626: aload_0
    //   627: invokevirtual checkErrorPacket : ()Lcom/mysql/jdbc/Buffer;
    //   630: astore #13
    //   632: aload_0
    //   633: aload_0
    //   634: getfield packetSequence : B
    //   637: iconst_1
    //   638: iadd
    //   639: i2b
    //   640: putfield packetSequence : B
    //   643: aload_0
    //   644: aload_0
    //   645: getfield compressedPacketSequence : B
    //   648: iconst_1
    //   649: iadd
    //   650: i2b
    //   651: putfield compressedPacketSequence : B
    //   654: aload #12
    //   656: astore #4
    //   658: aload #12
    //   660: ifnonnull -> 691
    //   663: aload_0
    //   664: getfield serverDefaultAuthenticationPluginName : Ljava/lang/String;
    //   667: astore #4
    //   669: aload #4
    //   671: ifnull -> 677
    //   674: goto -> 683
    //   677: aload_0
    //   678: getfield clientDefaultAuthenticationPluginName : Ljava/lang/String;
    //   681: astore #4
    //   683: aload_0
    //   684: aload #4
    //   686: invokespecial getAuthenticationPlugin : (Ljava/lang/String;)Lcom/mysql/jdbc/AuthenticationPlugin;
    //   689: astore #4
    //   691: aload #13
    //   693: invokevirtual isOKPacket : ()Z
    //   696: ifeq -> 738
    //   699: aload #13
    //   701: invokevirtual newReadLength : ()J
    //   704: pop2
    //   705: aload #13
    //   707: invokevirtual newReadLength : ()J
    //   710: pop2
    //   711: aload_0
    //   712: aload_0
    //   713: getfield serverStatus : I
    //   716: putfield oldServerStatus : I
    //   719: aload_0
    //   720: aload #13
    //   722: invokevirtual readInt : ()I
    //   725: putfield serverStatus : I
    //   728: aload #4
    //   730: invokeinterface destroy : ()V
    //   735: goto -> 1676
    //   738: aload #13
    //   740: invokevirtual isAuthMethodSwitchRequestPacket : ()Z
    //   743: ifeq -> 864
    //   746: aload #13
    //   748: ldc_w 'ASCII'
    //   751: aload_0
    //   752: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   755: invokevirtual readString : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/lang/String;
    //   758: astore #12
    //   760: aload #4
    //   762: invokeinterface getProtocolPluginName : ()Ljava/lang/String;
    //   767: aload #12
    //   769: invokevirtual equals : (Ljava/lang/Object;)Z
    //   772: ifne -> 821
    //   775: aload #4
    //   777: invokeinterface destroy : ()V
    //   782: aload_0
    //   783: aload #12
    //   785: invokespecial getAuthenticationPlugin : (Ljava/lang/String;)Lcom/mysql/jdbc/AuthenticationPlugin;
    //   788: astore #4
    //   790: aload #4
    //   792: ifnull -> 798
    //   795: goto -> 828
    //   798: ldc_w 'Connection.BadAuthenticationPlugin'
    //   801: iconst_1
    //   802: anewarray java/lang/Object
    //   805: dup
    //   806: iconst_0
    //   807: aload #12
    //   809: aastore
    //   810: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   813: aload_0
    //   814: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   817: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   820: athrow
    //   821: aload #4
    //   823: invokeinterface reset : ()V
    //   828: aload_0
    //   829: aload #4
    //   831: invokespecial checkConfidentiality : (Lcom/mysql/jdbc/AuthenticationPlugin;)V
    //   834: new com/mysql/jdbc/Buffer
    //   837: dup
    //   838: aload #13
    //   840: ldc_w 'ASCII'
    //   843: aload_0
    //   844: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   847: invokevirtual readString : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/lang/String;
    //   850: invokestatic getBytes : (Ljava/lang/String;)[B
    //   853: invokespecial <init> : ([B)V
    //   856: astore #12
    //   858: iconst_0
    //   859: istore #5
    //   861: goto -> 905
    //   864: aload_0
    //   865: iconst_5
    //   866: iconst_5
    //   867: bipush #16
    //   869: invokevirtual versionMeetsMinimum : (III)Z
    //   872: ifeq -> 911
    //   875: new com/mysql/jdbc/Buffer
    //   878: dup
    //   879: aload #13
    //   881: aload #13
    //   883: invokevirtual getPosition : ()I
    //   886: aload #13
    //   888: invokevirtual getBufLength : ()I
    //   891: aload #13
    //   893: invokevirtual getPosition : ()I
    //   896: isub
    //   897: invokevirtual getBytes : (II)[B
    //   900: invokespecial <init> : ([B)V
    //   903: astore #12
    //   905: iconst_0
    //   906: istore #6
    //   908: goto -> 948
    //   911: new com/mysql/jdbc/Buffer
    //   914: dup
    //   915: aload #13
    //   917: aload #13
    //   919: invokevirtual getPosition : ()I
    //   922: iconst_1
    //   923: isub
    //   924: aload #13
    //   926: invokevirtual getBufLength : ()I
    //   929: aload #13
    //   931: invokevirtual getPosition : ()I
    //   934: isub
    //   935: iconst_1
    //   936: iadd
    //   937: invokevirtual getBytes : (II)[B
    //   940: invokespecial <init> : ([B)V
    //   943: astore #12
    //   945: iconst_1
    //   946: istore #6
    //   948: iload #5
    //   950: ifeq -> 959
    //   953: aconst_null
    //   954: astore #14
    //   956: goto -> 962
    //   959: aload_2
    //   960: astore #14
    //   962: aload #4
    //   964: aload_1
    //   965: aload #14
    //   967: invokeinterface setAuthenticationParameters : (Ljava/lang/String;Ljava/lang/String;)V
    //   972: aload #4
    //   974: aload #12
    //   976: aload #15
    //   978: invokeinterface nextAuthenticationStep : (Lcom/mysql/jdbc/Buffer;Ljava/util/List;)Z
    //   983: istore #11
    //   985: aload #15
    //   987: invokevirtual size : ()I
    //   990: ifle -> 1643
    //   993: aload #13
    //   995: ifnonnull -> 1229
    //   998: aload_0
    //   999: invokevirtual getEncodingForHandshake : ()Ljava/lang/String;
    //   1002: astore #14
    //   1004: new com/mysql/jdbc/Buffer
    //   1007: dup
    //   1008: iload #7
    //   1010: iconst_1
    //   1011: iadd
    //   1012: invokespecial <init> : (I)V
    //   1015: astore #12
    //   1017: aload #12
    //   1019: bipush #17
    //   1021: invokevirtual writeByte : (B)V
    //   1024: aload #12
    //   1026: aload_1
    //   1027: aload #14
    //   1029: aload_0
    //   1030: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1033: invokevirtual writeString : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1036: aload #15
    //   1038: iconst_0
    //   1039: invokevirtual get : (I)Ljava/lang/Object;
    //   1042: checkcast com/mysql/jdbc/Buffer
    //   1045: invokevirtual getBufLength : ()I
    //   1048: sipush #256
    //   1051: if_icmpge -> 1105
    //   1054: aload #12
    //   1056: aload #15
    //   1058: iconst_0
    //   1059: invokevirtual get : (I)Ljava/lang/Object;
    //   1062: checkcast com/mysql/jdbc/Buffer
    //   1065: invokevirtual getBufLength : ()I
    //   1068: i2b
    //   1069: invokevirtual writeByte : (B)V
    //   1072: aload #12
    //   1074: aload #15
    //   1076: iconst_0
    //   1077: invokevirtual get : (I)Ljava/lang/Object;
    //   1080: checkcast com/mysql/jdbc/Buffer
    //   1083: invokevirtual getByteBuffer : ()[B
    //   1086: iconst_0
    //   1087: aload #15
    //   1089: iconst_0
    //   1090: invokevirtual get : (I)Ljava/lang/Object;
    //   1093: checkcast com/mysql/jdbc/Buffer
    //   1096: invokevirtual getBufLength : ()I
    //   1099: invokevirtual writeBytesNoNull : ([BII)V
    //   1102: goto -> 1111
    //   1105: aload #12
    //   1107: iconst_0
    //   1108: invokevirtual writeByte : (B)V
    //   1111: aload_0
    //   1112: getfield useConnectWithDb : Z
    //   1115: ifeq -> 1133
    //   1118: aload #12
    //   1120: aload_3
    //   1121: aload #14
    //   1123: aload_0
    //   1124: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1127: invokevirtual writeString : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1130: goto -> 1139
    //   1133: aload #12
    //   1135: iconst_0
    //   1136: invokevirtual writeByte : (B)V
    //   1139: aload_0
    //   1140: aload #12
    //   1142: aload #14
    //   1144: invokespecial appendCharsetByteForHandshake : (Lcom/mysql/jdbc/Buffer;Ljava/lang/String;)V
    //   1147: aload #12
    //   1149: iconst_0
    //   1150: invokevirtual writeByte : (B)V
    //   1153: aload_0
    //   1154: getfield serverCapabilities : I
    //   1157: ldc 524288
    //   1159: iand
    //   1160: ifeq -> 1181
    //   1163: aload #12
    //   1165: aload #4
    //   1167: invokeinterface getProtocolPluginName : ()Ljava/lang/String;
    //   1172: aload #14
    //   1174: aload_0
    //   1175: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1178: invokevirtual writeString : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1181: aload_0
    //   1182: getfield clientParam : J
    //   1185: ldc2_w 1048576
    //   1188: land
    //   1189: lconst_0
    //   1190: lcmp
    //   1191: ifeq -> 1215
    //   1194: aload_0
    //   1195: aload #12
    //   1197: aload #14
    //   1199: aload_0
    //   1200: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1203: invokespecial sendConnectionAttributes : (Lcom/mysql/jdbc/Buffer;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1206: aload #12
    //   1208: iconst_0
    //   1209: invokevirtual writeByte : (B)V
    //   1212: goto -> 1215
    //   1215: aload_0
    //   1216: aload #12
    //   1218: aload #12
    //   1220: invokevirtual getPosition : ()I
    //   1223: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   1226: goto -> 1301
    //   1229: aload #13
    //   1231: invokevirtual isAuthMethodSwitchRequestPacket : ()Z
    //   1234: ifeq -> 1304
    //   1237: new com/mysql/jdbc/Buffer
    //   1240: dup
    //   1241: aload #15
    //   1243: iconst_0
    //   1244: invokevirtual get : (I)Ljava/lang/Object;
    //   1247: checkcast com/mysql/jdbc/Buffer
    //   1250: invokevirtual getBufLength : ()I
    //   1253: iconst_4
    //   1254: iadd
    //   1255: invokespecial <init> : (I)V
    //   1258: astore #12
    //   1260: aload #12
    //   1262: aload #15
    //   1264: iconst_0
    //   1265: invokevirtual get : (I)Ljava/lang/Object;
    //   1268: checkcast com/mysql/jdbc/Buffer
    //   1271: invokevirtual getByteBuffer : ()[B
    //   1274: iconst_0
    //   1275: aload #15
    //   1277: iconst_0
    //   1278: invokevirtual get : (I)Ljava/lang/Object;
    //   1281: checkcast com/mysql/jdbc/Buffer
    //   1284: invokevirtual getBufLength : ()I
    //   1287: invokevirtual writeBytesNoNull : ([BII)V
    //   1290: aload_0
    //   1291: aload #12
    //   1293: aload #12
    //   1295: invokevirtual getPosition : ()I
    //   1298: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   1301: goto -> 1643
    //   1304: aload #13
    //   1306: invokevirtual isRawPacket : ()Z
    //   1309: ifne -> 1561
    //   1312: iload #6
    //   1314: ifeq -> 1320
    //   1317: goto -> 1561
    //   1320: aload_0
    //   1321: invokevirtual getEncodingForHandshake : ()Ljava/lang/String;
    //   1324: astore #12
    //   1326: new com/mysql/jdbc/Buffer
    //   1329: dup
    //   1330: iload #7
    //   1332: invokespecial <init> : (I)V
    //   1335: astore #14
    //   1337: aload #14
    //   1339: aload_0
    //   1340: getfield clientParam : J
    //   1343: invokevirtual writeLong : (J)V
    //   1346: aload #14
    //   1348: aload_0
    //   1349: getfield maxThreeBytes : I
    //   1352: i2l
    //   1353: invokevirtual writeLong : (J)V
    //   1356: aload_0
    //   1357: aload #14
    //   1359: aload #12
    //   1361: invokespecial appendCharsetByteForHandshake : (Lcom/mysql/jdbc/Buffer;Ljava/lang/String;)V
    //   1364: aload #14
    //   1366: bipush #23
    //   1368: newarray byte
    //   1370: invokevirtual writeBytesNoNull : ([B)V
    //   1373: aload #14
    //   1375: aload_1
    //   1376: aload #12
    //   1378: aload_0
    //   1379: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1382: invokevirtual writeString : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1385: aload_0
    //   1386: getfield serverCapabilities : I
    //   1389: ldc 2097152
    //   1391: iand
    //   1392: ifeq -> 1427
    //   1395: aload #14
    //   1397: aload #15
    //   1399: iconst_0
    //   1400: invokevirtual get : (I)Ljava/lang/Object;
    //   1403: checkcast com/mysql/jdbc/Buffer
    //   1406: aload #15
    //   1408: iconst_0
    //   1409: invokevirtual get : (I)Ljava/lang/Object;
    //   1412: checkcast com/mysql/jdbc/Buffer
    //   1415: invokevirtual getBufLength : ()I
    //   1418: invokevirtual getBytes : (I)[B
    //   1421: invokevirtual writeLenBytes : ([B)V
    //   1424: goto -> 1475
    //   1427: aload #14
    //   1429: aload #15
    //   1431: iconst_0
    //   1432: invokevirtual get : (I)Ljava/lang/Object;
    //   1435: checkcast com/mysql/jdbc/Buffer
    //   1438: invokevirtual getBufLength : ()I
    //   1441: i2b
    //   1442: invokevirtual writeByte : (B)V
    //   1445: aload #14
    //   1447: aload #15
    //   1449: iconst_0
    //   1450: invokevirtual get : (I)Ljava/lang/Object;
    //   1453: checkcast com/mysql/jdbc/Buffer
    //   1456: invokevirtual getByteBuffer : ()[B
    //   1459: iconst_0
    //   1460: aload #15
    //   1462: iconst_0
    //   1463: invokevirtual get : (I)Ljava/lang/Object;
    //   1466: checkcast com/mysql/jdbc/Buffer
    //   1469: invokevirtual getBufLength : ()I
    //   1472: invokevirtual writeBytesNoNull : ([BII)V
    //   1475: aload_0
    //   1476: getfield useConnectWithDb : Z
    //   1479: ifeq -> 1494
    //   1482: aload #14
    //   1484: aload_3
    //   1485: aload #12
    //   1487: aload_0
    //   1488: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1491: invokevirtual writeString : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1494: aload_0
    //   1495: getfield serverCapabilities : I
    //   1498: ldc 524288
    //   1500: iand
    //   1501: ifeq -> 1522
    //   1504: aload #14
    //   1506: aload #4
    //   1508: invokeinterface getProtocolPluginName : ()Ljava/lang/String;
    //   1513: aload #12
    //   1515: aload_0
    //   1516: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1519: invokevirtual writeString : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1522: aload_0
    //   1523: getfield clientParam : J
    //   1526: ldc2_w 1048576
    //   1529: land
    //   1530: lconst_0
    //   1531: lcmp
    //   1532: ifeq -> 1547
    //   1535: aload_0
    //   1536: aload #14
    //   1538: aload #12
    //   1540: aload_0
    //   1541: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1544: invokespecial sendConnectionAttributes : (Lcom/mysql/jdbc/Buffer;Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)V
    //   1547: aload_0
    //   1548: aload #14
    //   1550: aload #14
    //   1552: invokevirtual getPosition : ()I
    //   1555: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   1558: goto -> 1301
    //   1561: aload #15
    //   1563: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1566: astore #14
    //   1568: aload #14
    //   1570: invokeinterface hasNext : ()Z
    //   1575: ifeq -> 1301
    //   1578: aload #14
    //   1580: invokeinterface next : ()Ljava/lang/Object;
    //   1585: checkcast com/mysql/jdbc/Buffer
    //   1588: astore #12
    //   1590: new com/mysql/jdbc/Buffer
    //   1593: dup
    //   1594: aload #12
    //   1596: invokevirtual getBufLength : ()I
    //   1599: iconst_4
    //   1600: iadd
    //   1601: invokespecial <init> : (I)V
    //   1604: astore #16
    //   1606: aload #16
    //   1608: aload #12
    //   1610: invokevirtual getByteBuffer : ()[B
    //   1613: iconst_0
    //   1614: aload #15
    //   1616: iconst_0
    //   1617: invokevirtual get : (I)Ljava/lang/Object;
    //   1620: checkcast com/mysql/jdbc/Buffer
    //   1623: invokevirtual getBufLength : ()I
    //   1626: invokevirtual writeBytesNoNull : ([BII)V
    //   1629: aload_0
    //   1630: aload #16
    //   1632: aload #16
    //   1634: invokevirtual getPosition : ()I
    //   1637: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   1640: goto -> 1568
    //   1643: iload #10
    //   1645: istore #9
    //   1647: aload #4
    //   1649: astore #12
    //   1651: iload #6
    //   1653: istore #8
    //   1655: goto -> 92
    //   1658: astore_1
    //   1659: aload_1
    //   1660: invokevirtual getMessage : ()Ljava/lang/String;
    //   1663: aload_1
    //   1664: invokevirtual getSQLState : ()Ljava/lang/String;
    //   1667: aload_1
    //   1668: aload_0
    //   1669: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1672: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1675: athrow
    //   1676: iload #10
    //   1678: ifeq -> 1796
    //   1681: aload_0
    //   1682: getfield serverCapabilities : I
    //   1685: bipush #32
    //   1687: iand
    //   1688: ifeq -> 1748
    //   1691: aload_0
    //   1692: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1695: invokeinterface getUseCompression : ()Z
    //   1700: ifeq -> 1748
    //   1703: aload_0
    //   1704: getfield mysqlInput : Ljava/io/InputStream;
    //   1707: instanceof com/mysql/jdbc/CompressedInputStream
    //   1710: ifne -> 1748
    //   1713: aload_0
    //   1714: new java/util/zip/Deflater
    //   1717: dup
    //   1718: invokespecial <init> : ()V
    //   1721: putfield deflater : Ljava/util/zip/Deflater;
    //   1724: aload_0
    //   1725: iconst_1
    //   1726: putfield useCompression : Z
    //   1729: aload_0
    //   1730: new com/mysql/jdbc/CompressedInputStream
    //   1733: dup
    //   1734: aload_0
    //   1735: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1738: aload_0
    //   1739: getfield mysqlInput : Ljava/io/InputStream;
    //   1742: invokespecial <init> : (Lcom/mysql/jdbc/Connection;Ljava/io/InputStream;)V
    //   1745: putfield mysqlInput : Ljava/io/InputStream;
    //   1748: aload_0
    //   1749: getfield useConnectWithDb : Z
    //   1752: ifne -> 1760
    //   1755: aload_0
    //   1756: aload_3
    //   1757: invokespecial changeDatabaseTo : (Ljava/lang/String;)V
    //   1760: aload_0
    //   1761: aload_0
    //   1762: getfield socketFactory : Lcom/mysql/jdbc/SocketFactory;
    //   1765: invokeinterface afterHandshake : ()Ljava/net/Socket;
    //   1770: putfield mysqlConnection : Ljava/net/Socket;
    //   1773: return
    //   1774: astore_1
    //   1775: aload_0
    //   1776: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1779: aload_0
    //   1780: getfield lastPacketSentTimeMs : J
    //   1783: aload_0
    //   1784: getfield lastPacketReceivedTimeMs : J
    //   1787: aload_1
    //   1788: aload_0
    //   1789: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1792: invokestatic createCommunicationsException : (Lcom/mysql/jdbc/MySQLConnection;JJLjava/lang/Exception;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1795: athrow
    //   1796: ldc_w 'CommunicationsException.TooManyAuthenticationPluginNegotiations'
    //   1799: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1802: aload_0
    //   1803: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1806: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1809: athrow
    // Exception table:
    //   from	to	target	type
    //   962	985	1658	java/sql/SQLException
    //   1760	1773	1774	java/io/IOException
  }
  
  private final int readFully(InputStream paramInputStream, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 >= 0) {
      int i = 0;
      while (i < paramInt2) {
        int j = paramInputStream.read(paramArrayOfbyte, paramInt1 + i, paramInt2 - i);
        if (j >= 0) {
          i += j;
          continue;
        } 
        throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { Integer.valueOf(paramInt2), Integer.valueOf(i) }));
      } 
      return i;
    } 
    throw new IndexOutOfBoundsException();
  }
  
  private int readRemainingMultiPackets(Buffer paramBuffer, byte paramByte) throws IOException, SQLException {
    byte[] arrayOfByte = null;
    while (readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4) >= 4) {
      MySQLConnection mySQLConnection;
      Buffer buffer;
      byte[] arrayOfByte1 = this.packetHeaderBuf;
      int i = (arrayOfByte1[0] & 0xFF) + ((arrayOfByte1[1] & 0xFF) << 8) + ((arrayOfByte1[2] & 0xFF) << 16);
      arrayOfByte1 = arrayOfByte;
      if (arrayOfByte == null)
        buffer = new Buffer(i); 
      if (!this.useNewLargePackets && i == 1) {
        clearInputStream();
      } else {
        paramByte = (byte)(paramByte + 1);
        if (paramByte == this.packetHeaderBuf[3]) {
          buffer.setPosition(0);
          buffer.setBufLength(i);
          arrayOfByte = buffer.getByteBuffer();
          int j = readFully(this.mysqlInput, arrayOfByte, 0, i);
          if (j == i) {
            paramBuffer.writeBytesNoNull(arrayOfByte, 0, i);
            Buffer buffer1 = buffer;
            if (i != this.maxThreeBytes) {
              paramBuffer.setPosition(0);
              paramBuffer.setWasMultiPacket(true);
              return i;
            } 
            continue;
          } 
          mySQLConnection = this.connection;
          long l1 = this.lastPacketSentTimeMs;
          long l2 = this.lastPacketReceivedTimeMs;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Messages.getString("MysqlIO.50"));
          stringBuilder.append(i);
          stringBuilder.append(Messages.getString("MysqlIO.51"));
          stringBuilder.append(j);
          stringBuilder.append(".");
          throw SQLError.createCommunicationsException(mySQLConnection, l1, l2, SQLError.createSQLException(stringBuilder.toString(), getExceptionInterceptor()), getExceptionInterceptor());
        } 
        throw new IOException(Messages.getString("MysqlIO.49"));
      } 
      mySQLConnection.setPosition(0);
      mySQLConnection.setWasMultiPacket(true);
      return i;
    } 
    forceClose();
    throw new IOException(Messages.getString("MysqlIO.47"));
  }
  
  private final void readServerStatusForResultSets(Buffer paramBuffer) throws SQLException {
    if (this.use41Extensions) {
      paramBuffer.readByte();
      if (isEOFDeprecated()) {
        paramBuffer.newReadLength();
        paramBuffer.newReadLength();
        this.oldServerStatus = this.serverStatus;
        this.serverStatus = paramBuffer.readInt();
        checkTransactionState(this.oldServerStatus);
        int i = paramBuffer.readInt();
        this.warningCount = i;
        if (i > 0)
          this.hadWarnings = true; 
        paramBuffer.readByte();
        if (this.connection.isReadInfoMsgEnabled())
          paramBuffer.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor()); 
      } else {
        int i = paramBuffer.readInt();
        this.warningCount = i;
        if (i > 0)
          this.hadWarnings = true; 
        this.oldServerStatus = this.serverStatus;
        this.serverStatus = paramBuffer.readInt();
        checkTransactionState(this.oldServerStatus);
      } 
      setServerSlowQueryFlags();
    } 
  }
  
  private RowData readSingleRowSet(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean, Field[] paramArrayOfField) throws SQLException {
    byte b;
    ArrayList<ResultSetRow> arrayList = new ArrayList();
    boolean bool = useBufferRowExplicit(paramArrayOfField);
    int i = (int)paramLong;
    Object object = nextRow(paramArrayOfField, i, paramBoolean, paramInt2, false, bool, false, null);
    if (object != null) {
      arrayList.add((ResultSetRow)object);
      b = 1;
    } else {
      b = 0;
    } 
    while (object != null) {
      ResultSetRow resultSetRow = nextRow(paramArrayOfField, i, paramBoolean, paramInt2, false, bool, false, null);
      object = resultSetRow;
      if (resultSetRow != null) {
        if (paramInt1 != -1) {
          object = resultSetRow;
          if (b < paramInt1)
            continue; 
          continue;
        } 
        continue;
      } 
      continue;
      arrayList.add(SYNTHETIC_LOCAL_VARIABLE_11);
      b++;
      object = SYNTHETIC_LOCAL_VARIABLE_11;
    } 
    return new RowDataStatic(arrayList);
  }
  
  private void reclaimLargeReusablePacket() {
    Buffer buffer = this.reusablePacket;
    if (buffer != null && buffer.getCapacity() > 1048576)
      this.reusablePacket = new Buffer(1024); 
  }
  
  private void reclaimLargeSharedSendPacket() {
    Buffer buffer = this.sharedSendPacket;
    if (buffer != null && buffer.getCapacity() > 1048576)
      this.sharedSendPacket = new Buffer(1024); 
  }
  
  private final Buffer reuseAndReadPacket(Buffer paramBuffer) throws SQLException {
    return reuseAndReadPacket(paramBuffer, -1);
  }
  
  private final Buffer reuseAndReadPacket(Buffer paramBuffer, int paramInt) throws SQLException {
    try {
      IOException iOException1;
      paramBuffer.setWasMultiPacket(false);
      int i = paramInt;
      if (paramInt == -1)
        if (readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4) >= 4) {
          byte[] arrayOfByte = this.packetHeaderBuf;
          paramInt = arrayOfByte[0];
          i = arrayOfByte[1];
          i = ((arrayOfByte[2] & 0xFF) << 16) + (paramInt & 0xFF) + ((i & 0xFF) << 8);
        } else {
          forceClose();
          iOException1 = new IOException();
          this(Messages.getString("MysqlIO.43"));
          throw iOException1;
        }  
      if (this.traceProtocol) {
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        stringBuilder1.append(Messages.getString("MysqlIO.44"));
        stringBuilder1.append(i);
        stringBuilder1.append(Messages.getString("MysqlIO.45"));
        stringBuilder1.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
        this.connection.getLog().logTrace(stringBuilder1.toString());
      } 
      byte b = this.packetHeaderBuf[3];
      if (!this.packetSequenceReset) {
        if (this.enablePacketDebug && this.checkPacketSequence)
          checkPacketSequencing(b); 
      } else {
        this.packetSequenceReset = false;
      } 
      this.readPacketSequence = b;
      iOException1.setPosition(0);
      if ((iOException1.getByteBuffer()).length <= i)
        iOException1.setByteBuffer(new byte[i + 1]); 
      iOException1.setBufLength(i);
      paramInt = readFully(this.mysqlInput, iOException1.getByteBuffer(), 0, i);
      if (paramInt == i) {
        if (this.traceProtocol) {
          StringBuilder stringBuilder1 = new StringBuilder();
          this();
          stringBuilder1.append(Messages.getString("MysqlIO.46"));
          stringBuilder1.append(getPacketDumpToLog((Buffer)iOException1, i));
          this.connection.getLog().logTrace(stringBuilder1.toString());
        } 
        if (this.enablePacketDebug)
          enqueuePacketForDebugging(false, true, 0, this.packetHeaderBuf, (Buffer)iOException1); 
        paramInt = this.maxThreeBytes;
        if (i == paramInt) {
          iOException1.setPosition(paramInt);
          i = readRemainingMultiPackets((Buffer)iOException1, b);
          paramInt = 1;
        } else {
          paramInt = 0;
        } 
        if (paramInt == 0)
          iOException1.getByteBuffer()[i] = 0; 
        if (this.connection.getMaintainTimeStats())
          this.lastPacketReceivedTimeMs = System.currentTimeMillis(); 
        return (Buffer)iOException1;
      } 
      IOException iOException2 = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Short read, expected ");
      stringBuilder.append(i);
      stringBuilder.append(" bytes, only read ");
      stringBuilder.append(paramInt);
      this(stringBuilder.toString());
      throw iOException2;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } catch (OutOfMemoryError outOfMemoryError) {
      try {
        clearInputStream();
      } catch (Exception exception) {}
      try {
        this.connection.realClose(false, false, true, outOfMemoryError);
      } catch (Exception exception) {}
    } 
  }
  
  private void secureAuth(Buffer paramBuffer, int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean) throws SQLException {
    Buffer buffer = paramBuffer;
    if (paramBuffer == null)
      buffer = new Buffer(paramInt); 
    if (paramBoolean)
      if (this.use41Extensions) {
        if (versionMeetsMinimum(4, 1, 1)) {
          buffer.writeLong(this.clientParam);
          buffer.writeLong(this.maxThreeBytes);
          buffer.writeByte((byte)8);
          buffer.writeBytesNoNull(new byte[23]);
        } else {
          buffer.writeLong(this.clientParam);
          buffer.writeLong(this.maxThreeBytes);
        } 
      } else {
        buffer.writeInt((int)this.clientParam);
        buffer.writeLongInt(this.maxThreeBytes);
      }  
    buffer.writeString(paramString1, "Cp1252", this.connection);
    if (paramString2.length() != 0) {
      buffer.writeString("xxxxxxxx", "Cp1252", this.connection);
    } else {
      buffer.writeString("", "Cp1252", this.connection);
    } 
    if (this.useConnectWithDb)
      buffer.writeString(paramString3, "Cp1252", this.connection); 
    send(buffer, buffer.getPosition());
    if (paramString2.length() > 0) {
      paramBuffer = readPacket();
      paramBuffer.setPosition(0);
      byte[] arrayOfByte = paramBuffer.getByteBuffer();
      if (arrayOfByte.length == 24 && arrayOfByte[0] != 0) {
        StringBuilder stringBuilder;
        byte[] arrayOfByte1;
        if (arrayOfByte[0] != 42) {
          try {
            byte[] arrayOfByte2 = Security.passwordHashStage1(paramString2);
            arrayOfByte1 = new byte[arrayOfByte2.length];
            System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, arrayOfByte2.length);
            arrayOfByte1 = Security.passwordHashStage2(arrayOfByte1, arrayOfByte);
            byte[] arrayOfByte3 = new byte[arrayOfByte.length - 4];
            System.arraycopy(arrayOfByte, 4, arrayOfByte3, 0, arrayOfByte.length - 4);
            arrayOfByte = new byte[20];
            Security.xorString(arrayOfByte3, arrayOfByte, arrayOfByte1, 20);
            Security.xorString(arrayOfByte, arrayOfByte2, arrayOfByte2, 20);
            Buffer buffer1 = new Buffer();
            this(25);
            buffer1.writeBytesNoNull(arrayOfByte2);
            this.packetSequence = (byte)(this.packetSequence + 1);
            send(buffer1, 24);
          } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(Messages.getString("MysqlIO.91"));
            stringBuilder.append(Messages.getString("MysqlIO.92"));
            throw SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
          } 
        } else {
          try {
            byte[] arrayOfByte3 = Security.createKeyFromOldPassword((String)arrayOfByte1);
            byte[] arrayOfByte4 = new byte[stringBuilder.length - 4];
            System.arraycopy(stringBuilder, 4, arrayOfByte4, 0, stringBuilder.length - 4);
            byte[] arrayOfByte2 = new byte[20];
            Security.xorString(arrayOfByte4, arrayOfByte2, arrayOfByte3, 20);
            String str = Util.scramble(StringUtils.toString(arrayOfByte2), (String)arrayOfByte1);
            Buffer buffer1 = new Buffer();
            this(paramInt);
            buffer1.writeString(str, "Cp1252", this.connection);
            this.packetSequence = (byte)(this.packetSequence + 1);
            send(buffer1, 24);
          } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(Messages.getString("MysqlIO.91"));
            stringBuilder1.append(Messages.getString("MysqlIO.92"));
            throw SQLError.createSQLException(stringBuilder1.toString(), "S1000", getExceptionInterceptor());
          } 
        } 
      } 
    } 
  }
  
  private final void send(Buffer paramBuffer, int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield maxAllowedPacket : I
    //   4: istore_3
    //   5: iload_3
    //   6: ifle -> 34
    //   9: iload_2
    //   10: iload_3
    //   11: if_icmpgt -> 17
    //   14: goto -> 34
    //   17: new com/mysql/jdbc/PacketTooBigException
    //   20: astore_1
    //   21: aload_1
    //   22: iload_2
    //   23: i2l
    //   24: aload_0
    //   25: getfield maxAllowedPacket : I
    //   28: i2l
    //   29: invokespecial <init> : (JJ)V
    //   32: aload_1
    //   33: athrow
    //   34: aload_0
    //   35: getfield serverMajorVersion : I
    //   38: iconst_4
    //   39: if_icmplt -> 82
    //   42: iload_2
    //   43: iconst_4
    //   44: isub
    //   45: istore_3
    //   46: aload_0
    //   47: getfield maxThreeBytes : I
    //   50: istore #4
    //   52: iload_3
    //   53: iload #4
    //   55: if_icmpge -> 73
    //   58: aload_0
    //   59: getfield useCompression : Z
    //   62: ifeq -> 82
    //   65: iload_3
    //   66: iload #4
    //   68: iconst_3
    //   69: isub
    //   70: if_icmplt -> 82
    //   73: aload_0
    //   74: aload_1
    //   75: iload_2
    //   76: invokespecial sendSplitPackets : (Lcom/mysql/jdbc/Buffer;I)V
    //   79: goto -> 364
    //   82: aload_0
    //   83: aload_0
    //   84: getfield packetSequence : B
    //   87: iconst_1
    //   88: iadd
    //   89: i2b
    //   90: putfield packetSequence : B
    //   93: aload_1
    //   94: iconst_0
    //   95: invokevirtual setPosition : (I)V
    //   98: aload_1
    //   99: iload_2
    //   100: iconst_4
    //   101: isub
    //   102: invokevirtual writeLongInt : (I)V
    //   105: aload_1
    //   106: aload_0
    //   107: getfield packetSequence : B
    //   110: invokevirtual writeByte : (B)V
    //   113: aload_0
    //   114: getfield useCompression : Z
    //   117: ifeq -> 234
    //   120: aload_0
    //   121: aload_0
    //   122: getfield compressedPacketSequence : B
    //   125: iconst_1
    //   126: iadd
    //   127: i2b
    //   128: putfield compressedPacketSequence : B
    //   131: aload_0
    //   132: aload_1
    //   133: iconst_0
    //   134: iload_2
    //   135: invokespecial compressPacket : (Lcom/mysql/jdbc/Buffer;II)Lcom/mysql/jdbc/Buffer;
    //   138: astore #5
    //   140: aload #5
    //   142: invokevirtual getPosition : ()I
    //   145: istore_3
    //   146: aload_0
    //   147: getfield traceProtocol : Z
    //   150: ifeq -> 229
    //   153: new java/lang/StringBuilder
    //   156: astore #6
    //   158: aload #6
    //   160: invokespecial <init> : ()V
    //   163: aload #6
    //   165: ldc_w 'MysqlIO.57'
    //   168: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload #6
    //   177: aload #5
    //   179: iload_3
    //   180: invokestatic getPacketDumpToLog : (Lcom/mysql/jdbc/Buffer;I)Ljava/lang/String;
    //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload #6
    //   189: ldc_w 'MysqlIO.58'
    //   192: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   195: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: pop
    //   199: aload #6
    //   201: aload_1
    //   202: iload_2
    //   203: invokestatic getPacketDumpToLog : (Lcom/mysql/jdbc/Buffer;I)Ljava/lang/String;
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_0
    //   211: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   214: invokeinterface getLog : ()Lcom/mysql/jdbc/log/Log;
    //   219: aload #6
    //   221: invokevirtual toString : ()Ljava/lang/String;
    //   224: invokeinterface logTrace : (Ljava/lang/Object;)V
    //   229: iload_3
    //   230: istore_2
    //   231: goto -> 343
    //   234: aload_0
    //   235: getfield traceProtocol : Z
    //   238: ifeq -> 340
    //   241: new java/lang/StringBuilder
    //   244: astore #5
    //   246: aload #5
    //   248: invokespecial <init> : ()V
    //   251: aload #5
    //   253: ldc_w 'MysqlIO.59'
    //   256: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   259: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: pop
    //   263: aload #5
    //   265: ldc_w 'host: ''
    //   268: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: pop
    //   272: aload #5
    //   274: aload_0
    //   275: getfield host : Ljava/lang/String;
    //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload #5
    //   284: ldc_w '' threadId: ''
    //   287: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   290: pop
    //   291: aload #5
    //   293: aload_0
    //   294: getfield threadId : J
    //   297: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   300: pop
    //   301: aload #5
    //   303: ldc_w ''\\n'
    //   306: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   309: pop
    //   310: aload #5
    //   312: aload_1
    //   313: iload_2
    //   314: invokevirtual dump : (I)Ljava/lang/String;
    //   317: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: aload_0
    //   322: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   325: invokeinterface getLog : ()Lcom/mysql/jdbc/log/Log;
    //   330: aload #5
    //   332: invokevirtual toString : ()Ljava/lang/String;
    //   335: invokeinterface logTrace : (Ljava/lang/Object;)V
    //   340: aload_1
    //   341: astore #5
    //   343: aload_0
    //   344: getfield mysqlOutput : Ljava/io/BufferedOutputStream;
    //   347: aload #5
    //   349: invokevirtual getByteBuffer : ()[B
    //   352: iconst_0
    //   353: iload_2
    //   354: invokevirtual write : ([BII)V
    //   357: aload_0
    //   358: getfield mysqlOutput : Ljava/io/BufferedOutputStream;
    //   361: invokevirtual flush : ()V
    //   364: aload_0
    //   365: getfield enablePacketDebug : Z
    //   368: ifeq -> 385
    //   371: aload_0
    //   372: iconst_1
    //   373: iconst_0
    //   374: iload_2
    //   375: iconst_5
    //   376: iadd
    //   377: aload_0
    //   378: getfield packetHeaderBuf : [B
    //   381: aload_1
    //   382: invokespecial enqueuePacketForDebugging : (ZZI[BLcom/mysql/jdbc/Buffer;)V
    //   385: aload_1
    //   386: aload_0
    //   387: getfield sharedSendPacket : Lcom/mysql/jdbc/Buffer;
    //   390: if_acmpne -> 397
    //   393: aload_0
    //   394: invokespecial reclaimLargeSharedSendPacket : ()V
    //   397: aload_0
    //   398: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   401: invokeinterface getMaintainTimeStats : ()Z
    //   406: ifeq -> 416
    //   409: aload_0
    //   410: invokestatic currentTimeMillis : ()J
    //   413: putfield lastPacketSentTimeMs : J
    //   416: return
    //   417: astore_1
    //   418: aload_0
    //   419: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   422: aload_0
    //   423: getfield lastPacketSentTimeMs : J
    //   426: aload_0
    //   427: getfield lastPacketReceivedTimeMs : J
    //   430: aload_1
    //   431: aload_0
    //   432: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   435: invokestatic createCommunicationsException : (Lcom/mysql/jdbc/MySQLConnection;JJLjava/lang/Exception;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   438: athrow
    // Exception table:
    //   from	to	target	type
    //   0	5	417	java/io/IOException
    //   17	34	417	java/io/IOException
    //   34	42	417	java/io/IOException
    //   46	52	417	java/io/IOException
    //   58	65	417	java/io/IOException
    //   73	79	417	java/io/IOException
    //   82	229	417	java/io/IOException
    //   234	340	417	java/io/IOException
    //   343	364	417	java/io/IOException
    //   364	385	417	java/io/IOException
    //   385	397	417	java/io/IOException
    //   397	416	417	java/io/IOException
  }
  
  private void sendConnectionAttributes(Buffer paramBuffer, String paramString, MySQLConnection paramMySQLConnection) throws SQLException {
    String str = paramMySQLConnection.getConnectionAttributes();
    Buffer buffer = new Buffer(100);
    try {
      Properties properties = getConnectionAttributesAsProperties(str);
      for (String str1 : properties.keySet()) {
        buffer.writeLenString(str1, paramString, paramMySQLConnection.getServerCharset(), null, paramMySQLConnection.parserKnowsUnicode(), paramMySQLConnection);
        buffer.writeLenString(properties.getProperty(str1), paramString, paramMySQLConnection.getServerCharset(), null, paramMySQLConnection.parserKnowsUnicode(), paramMySQLConnection);
      } 
    } catch (UnsupportedEncodingException unsupportedEncodingException) {}
    paramBuffer.writeByte((byte)(buffer.getPosition() - 4));
    paramBuffer.writeBytesNoNull(buffer.getByteBuffer(), 4, buffer.getBufLength() - 4);
  }
  
  private final ResultSetImpl sendFileToServer(StatementImpl paramStatementImpl, String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield useCompression : Z
    //   4: ifeq -> 18
    //   7: aload_0
    //   8: aload_0
    //   9: getfield compressedPacketSequence : B
    //   12: iconst_1
    //   13: iadd
    //   14: i2b
    //   15: putfield compressedPacketSequence : B
    //   18: aload_0
    //   19: getfield loadFileBufRef : Ljava/lang/ref/SoftReference;
    //   22: astore #4
    //   24: aconst_null
    //   25: astore #9
    //   27: aconst_null
    //   28: astore #8
    //   30: aload #4
    //   32: ifnonnull -> 41
    //   35: aconst_null
    //   36: astore #4
    //   38: goto -> 51
    //   41: aload #4
    //   43: invokevirtual get : ()Ljava/lang/Object;
    //   46: checkcast com/mysql/jdbc/Buffer
    //   49: astore #4
    //   51: aload_0
    //   52: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   55: invokeinterface getMaxAllowedPacket : ()I
    //   60: bipush #12
    //   62: isub
    //   63: aload_0
    //   64: aload_0
    //   65: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   68: invokeinterface getMaxAllowedPacket : ()I
    //   73: bipush #16
    //   75: isub
    //   76: sipush #4096
    //   79: invokespecial alignPacketSize : (II)I
    //   82: bipush #12
    //   84: isub
    //   85: invokestatic min : (II)I
    //   88: istore_3
    //   89: ldc_w 1048564
    //   92: aload_0
    //   93: ldc_w 1048560
    //   96: sipush #4096
    //   99: invokespecial alignPacketSize : (II)I
    //   102: bipush #12
    //   104: isub
    //   105: invokestatic min : (II)I
    //   108: iload_3
    //   109: invokestatic min : (II)I
    //   112: istore_3
    //   113: aload #4
    //   115: astore #7
    //   117: aload #4
    //   119: ifnonnull -> 210
    //   122: new com/mysql/jdbc/Buffer
    //   125: astore #7
    //   127: aload #7
    //   129: iload_3
    //   130: iconst_4
    //   131: iadd
    //   132: invokespecial <init> : (I)V
    //   135: new java/lang/ref/SoftReference
    //   138: astore #4
    //   140: aload #4
    //   142: aload #7
    //   144: invokespecial <init> : (Ljava/lang/Object;)V
    //   147: aload_0
    //   148: aload #4
    //   150: putfield loadFileBufRef : Ljava/lang/ref/SoftReference;
    //   153: goto -> 210
    //   156: astore_1
    //   157: new java/lang/StringBuilder
    //   160: dup
    //   161: invokespecial <init> : ()V
    //   164: astore_1
    //   165: aload_1
    //   166: ldc_w 'Could not allocate packet of '
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload_1
    //   174: iload_3
    //   175: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: aload_1
    //   180: ldc_w ' bytes required for LOAD DATA LOCAL INFILE operation.'
    //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_1
    //   188: ldc_w ' Try increasing max heap allocation for JVM or decreasing server variable 'max_allowed_packet''
    //   191: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: aload_1
    //   196: invokevirtual toString : ()Ljava/lang/String;
    //   199: ldc_w 'S1001'
    //   202: aload_0
    //   203: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   206: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   209: athrow
    //   210: aload #7
    //   212: invokevirtual clear : ()V
    //   215: aload_0
    //   216: aload #7
    //   218: iconst_0
    //   219: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   222: iload_3
    //   223: newarray byte
    //   225: astore #11
    //   227: aload #8
    //   229: astore #5
    //   231: aload #9
    //   233: astore #6
    //   235: aload_0
    //   236: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   239: invokeinterface getAllowLoadLocalInfile : ()Z
    //   244: ifeq -> 675
    //   247: aload_1
    //   248: ifnull -> 268
    //   251: aload #8
    //   253: astore #5
    //   255: aload #9
    //   257: astore #6
    //   259: aload_1
    //   260: invokevirtual getLocalInfileInputStream : ()Ljava/io/InputStream;
    //   263: astore #4
    //   265: goto -> 271
    //   268: aconst_null
    //   269: astore #4
    //   271: aload #4
    //   273: ifnull -> 311
    //   276: aload #8
    //   278: astore #5
    //   280: aload #9
    //   282: astore #6
    //   284: new java/io/BufferedInputStream
    //   287: astore #10
    //   289: aload #8
    //   291: astore #5
    //   293: aload #9
    //   295: astore #6
    //   297: aload #10
    //   299: aload #4
    //   301: invokespecial <init> : (Ljava/io/InputStream;)V
    //   304: aload #10
    //   306: astore #4
    //   308: goto -> 552
    //   311: aload #8
    //   313: astore #5
    //   315: aload #9
    //   317: astore #6
    //   319: aload_0
    //   320: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   323: invokeinterface getAllowUrlInLocalInfile : ()Z
    //   328: ifne -> 380
    //   331: aload #8
    //   333: astore #5
    //   335: aload #9
    //   337: astore #6
    //   339: new java/io/FileInputStream
    //   342: astore #4
    //   344: aload #8
    //   346: astore #5
    //   348: aload #9
    //   350: astore #6
    //   352: aload #4
    //   354: aload_2
    //   355: invokespecial <init> : (Ljava/lang/String;)V
    //   358: aload #8
    //   360: astore #5
    //   362: aload #9
    //   364: astore #6
    //   366: new java/io/BufferedInputStream
    //   369: dup
    //   370: aload #4
    //   372: invokespecial <init> : (Ljava/io/InputStream;)V
    //   375: astore #4
    //   377: goto -> 552
    //   380: aload #8
    //   382: astore #5
    //   384: aload #9
    //   386: astore #6
    //   388: aload_2
    //   389: bipush #58
    //   391: invokevirtual indexOf : (I)I
    //   394: istore_3
    //   395: iload_3
    //   396: iconst_m1
    //   397: if_icmpeq -> 503
    //   400: aload #8
    //   402: astore #5
    //   404: aload #9
    //   406: astore #6
    //   408: new java/net/URL
    //   411: astore #4
    //   413: aload #8
    //   415: astore #5
    //   417: aload #9
    //   419: astore #6
    //   421: aload #4
    //   423: aload_2
    //   424: invokespecial <init> : (Ljava/lang/String;)V
    //   427: aload #8
    //   429: astore #5
    //   431: aload #9
    //   433: astore #6
    //   435: new java/io/BufferedInputStream
    //   438: dup
    //   439: aload #4
    //   441: invokevirtual openStream : ()Ljava/io/InputStream;
    //   444: invokespecial <init> : (Ljava/io/InputStream;)V
    //   447: astore #4
    //   449: goto -> 308
    //   452: astore #4
    //   454: aload #8
    //   456: astore #5
    //   458: aload #9
    //   460: astore #6
    //   462: new java/io/FileInputStream
    //   465: astore #4
    //   467: aload #8
    //   469: astore #5
    //   471: aload #9
    //   473: astore #6
    //   475: aload #4
    //   477: aload_2
    //   478: invokespecial <init> : (Ljava/lang/String;)V
    //   481: aload #8
    //   483: astore #5
    //   485: aload #9
    //   487: astore #6
    //   489: new java/io/BufferedInputStream
    //   492: dup
    //   493: aload #4
    //   495: invokespecial <init> : (Ljava/io/InputStream;)V
    //   498: astore #4
    //   500: goto -> 377
    //   503: aload #8
    //   505: astore #5
    //   507: aload #9
    //   509: astore #6
    //   511: new java/io/FileInputStream
    //   514: astore #4
    //   516: aload #8
    //   518: astore #5
    //   520: aload #9
    //   522: astore #6
    //   524: aload #4
    //   526: aload_2
    //   527: invokespecial <init> : (Ljava/lang/String;)V
    //   530: aload #8
    //   532: astore #5
    //   534: aload #9
    //   536: astore #6
    //   538: new java/io/BufferedInputStream
    //   541: dup
    //   542: aload #4
    //   544: invokespecial <init> : (Ljava/io/InputStream;)V
    //   547: astore #4
    //   549: goto -> 377
    //   552: aload #4
    //   554: astore #5
    //   556: aload #4
    //   558: astore #6
    //   560: aload #4
    //   562: aload #11
    //   564: invokevirtual read : ([B)I
    //   567: istore_3
    //   568: iload_3
    //   569: iconst_m1
    //   570: if_icmpeq -> 625
    //   573: aload #4
    //   575: astore #5
    //   577: aload #4
    //   579: astore #6
    //   581: aload #7
    //   583: invokevirtual clear : ()V
    //   586: aload #4
    //   588: astore #5
    //   590: aload #4
    //   592: astore #6
    //   594: aload #7
    //   596: aload #11
    //   598: iconst_0
    //   599: iload_3
    //   600: invokevirtual writeBytesNoNull : ([BII)V
    //   603: aload #4
    //   605: astore #5
    //   607: aload #4
    //   609: astore #6
    //   611: aload_0
    //   612: aload #7
    //   614: aload #7
    //   616: invokevirtual getPosition : ()I
    //   619: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   622: goto -> 552
    //   625: aload #4
    //   627: invokevirtual close : ()V
    //   630: aload #7
    //   632: invokevirtual clear : ()V
    //   635: aload_0
    //   636: aload #7
    //   638: aload #7
    //   640: invokevirtual getPosition : ()I
    //   643: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   646: aload_0
    //   647: aload_1
    //   648: aload_0
    //   649: invokevirtual checkErrorPacket : ()Lcom/mysql/jdbc/Buffer;
    //   652: invokespecial buildResultSetWithUpdates : (Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/Buffer;)Lcom/mysql/jdbc/ResultSetImpl;
    //   655: areturn
    //   656: astore_1
    //   657: ldc_w 'MysqlIO.65'
    //   660: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   663: ldc_w 'S1000'
    //   666: aload_1
    //   667: aload_0
    //   668: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   671: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   674: athrow
    //   675: aload #8
    //   677: astore #5
    //   679: aload #9
    //   681: astore #6
    //   683: ldc_w 'MysqlIO.LoadDataLocalNotAllowed'
    //   686: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   689: ldc_w 'S1000'
    //   692: aload_0
    //   693: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   696: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   699: athrow
    //   700: astore_1
    //   701: goto -> 861
    //   704: astore #4
    //   706: aload #6
    //   708: astore #5
    //   710: new java/lang/StringBuilder
    //   713: astore_1
    //   714: aload #6
    //   716: astore #5
    //   718: aload_1
    //   719: ldc_w 'MysqlIO.60'
    //   722: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   725: invokespecial <init> : (Ljava/lang/String;)V
    //   728: aload_2
    //   729: ifnull -> 782
    //   732: aload #6
    //   734: astore #5
    //   736: aload_0
    //   737: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   740: invokeinterface getParanoid : ()Z
    //   745: ifne -> 782
    //   748: aload #6
    //   750: astore #5
    //   752: aload_1
    //   753: ldc_w '''
    //   756: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   759: pop
    //   760: aload #6
    //   762: astore #5
    //   764: aload_1
    //   765: aload_2
    //   766: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   769: pop
    //   770: aload #6
    //   772: astore #5
    //   774: aload_1
    //   775: ldc_w '''
    //   778: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   781: pop
    //   782: aload #6
    //   784: astore #5
    //   786: aload_1
    //   787: ldc_w 'MysqlIO.63'
    //   790: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   793: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   796: pop
    //   797: aload #6
    //   799: astore #5
    //   801: aload_0
    //   802: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   805: invokeinterface getParanoid : ()Z
    //   810: ifne -> 842
    //   813: aload #6
    //   815: astore #5
    //   817: aload_1
    //   818: ldc_w 'MysqlIO.64'
    //   821: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   824: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   827: pop
    //   828: aload #6
    //   830: astore #5
    //   832: aload_1
    //   833: aload #4
    //   835: invokestatic stackTraceToString : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   838: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   841: pop
    //   842: aload #6
    //   844: astore #5
    //   846: aload_1
    //   847: invokevirtual toString : ()Ljava/lang/String;
    //   850: ldc_w 'S1009'
    //   853: aload_0
    //   854: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   857: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   860: athrow
    //   861: aload #5
    //   863: ifnull -> 893
    //   866: aload #5
    //   868: invokevirtual close : ()V
    //   871: goto -> 914
    //   874: astore_1
    //   875: ldc_w 'MysqlIO.65'
    //   878: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   881: ldc_w 'S1000'
    //   884: aload_1
    //   885: aload_0
    //   886: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   889: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   892: athrow
    //   893: aload #7
    //   895: invokevirtual clear : ()V
    //   898: aload_0
    //   899: aload #7
    //   901: aload #7
    //   903: invokevirtual getPosition : ()I
    //   906: invokespecial send : (Lcom/mysql/jdbc/Buffer;I)V
    //   909: aload_0
    //   910: invokevirtual checkErrorPacket : ()Lcom/mysql/jdbc/Buffer;
    //   913: pop
    //   914: aload_1
    //   915: athrow
    // Exception table:
    //   from	to	target	type
    //   122	153	156	java/lang/OutOfMemoryError
    //   235	247	704	java/io/IOException
    //   235	247	700	finally
    //   259	265	704	java/io/IOException
    //   259	265	700	finally
    //   284	289	704	java/io/IOException
    //   284	289	700	finally
    //   297	304	704	java/io/IOException
    //   297	304	700	finally
    //   319	331	704	java/io/IOException
    //   319	331	700	finally
    //   339	344	704	java/io/IOException
    //   339	344	700	finally
    //   352	358	704	java/io/IOException
    //   352	358	700	finally
    //   366	377	704	java/io/IOException
    //   366	377	700	finally
    //   388	395	704	java/io/IOException
    //   388	395	700	finally
    //   408	413	452	java/net/MalformedURLException
    //   408	413	704	java/io/IOException
    //   408	413	700	finally
    //   421	427	452	java/net/MalformedURLException
    //   421	427	704	java/io/IOException
    //   421	427	700	finally
    //   435	449	452	java/net/MalformedURLException
    //   435	449	704	java/io/IOException
    //   435	449	700	finally
    //   462	467	704	java/io/IOException
    //   462	467	700	finally
    //   475	481	704	java/io/IOException
    //   475	481	700	finally
    //   489	500	704	java/io/IOException
    //   489	500	700	finally
    //   511	516	704	java/io/IOException
    //   511	516	700	finally
    //   524	530	704	java/io/IOException
    //   524	530	700	finally
    //   538	549	704	java/io/IOException
    //   538	549	700	finally
    //   560	568	704	java/io/IOException
    //   560	568	700	finally
    //   581	586	704	java/io/IOException
    //   581	586	700	finally
    //   594	603	704	java/io/IOException
    //   594	603	700	finally
    //   611	622	704	java/io/IOException
    //   611	622	700	finally
    //   625	630	656	java/lang/Exception
    //   683	700	704	java/io/IOException
    //   683	700	700	finally
    //   710	714	700	finally
    //   718	728	700	finally
    //   736	748	700	finally
    //   752	760	700	finally
    //   764	770	700	finally
    //   774	782	700	finally
    //   786	797	700	finally
    //   801	813	700	finally
    //   817	828	700	finally
    //   832	842	700	finally
    //   846	861	700	finally
    //   866	871	874	java/lang/Exception
  }
  
  private final void sendSplitPackets(Buffer paramBuffer, int paramInt) throws SQLException {
    try {
      Buffer buffer1;
      SoftReference<Buffer> softReference2;
      SoftReference<Buffer> softReference1 = this.splitBufRef;
      SoftReference<Buffer> softReference3 = null;
      if (softReference1 == null) {
        buffer2 = null;
      } else {
        buffer2 = softReference1.get();
      } 
      softReference1 = softReference3;
      if (this.useCompression) {
        softReference1 = this.compressBufRef;
        if (softReference1 == null) {
          softReference1 = softReference3;
        } else {
          buffer1 = softReference1.get();
        } 
      } 
      Buffer buffer3 = buffer2;
      if (buffer2 == null) {
        buffer3 = new Buffer();
        this(this.maxThreeBytes + 4);
        softReference2 = new SoftReference();
        this((T)buffer3);
        this.splitBufRef = softReference2;
      } 
      Buffer buffer2 = buffer1;
      if (this.useCompression) {
        SoftReference<Buffer> softReference;
        int m = (paramInt / this.maxThreeBytes + 1) * 4 + paramInt;
        if (buffer1 == null) {
          buffer2 = new Buffer();
          this(m);
          softReference = new SoftReference();
          this((T)buffer2);
          this.compressBufRef = softReference;
        } else {
          softReference2 = softReference;
          if (softReference.getBufLength() < m) {
            softReference.setPosition(softReference.getBufLength());
            softReference.ensureCapacity(m - softReference.getBufLength());
            softReference2 = softReference;
          } 
        } 
      } 
      int i = paramInt - 4;
      int k = this.maxThreeBytes;
      byte[] arrayOfByte = paramBuffer.getByteBuffer();
      paramInt = 0;
      int j = 4;
      while (i >= 0) {
        this.packetSequence = (byte)(this.packetSequence + 1);
        int m = k;
        if (i < k)
          m = i; 
        buffer3.setPosition(0);
        buffer3.writeLongInt(m);
        buffer3.writeByte(this.packetSequence);
        if (i > 0)
          System.arraycopy(arrayOfByte, j, buffer3.getByteBuffer(), 4, m); 
        if (this.useCompression) {
          byte[] arrayOfByte1 = buffer3.getByteBuffer();
          byte[] arrayOfByte2 = softReference2.getByteBuffer();
          k = m + 4;
          System.arraycopy(arrayOfByte1, 0, arrayOfByte2, paramInt, k);
          paramInt += k;
        } else {
          this.mysqlOutput.write(buffer3.getByteBuffer(), 0, m + 4);
          this.mysqlOutput.flush();
        } 
        j += m;
        i -= this.maxThreeBytes;
        k = m;
      } 
      if (this.useCompression) {
        int m = this.maxThreeBytes - 3;
        i = 0;
        while (paramInt >= 0) {
          this.compressedPacketSequence = (byte)(this.compressedPacketSequence + 1);
          j = m;
          if (paramInt < m)
            j = paramInt; 
          Buffer buffer = compressPacket((Buffer)softReference2, i, j);
          m = buffer.getPosition();
          this.mysqlOutput.write(buffer.getByteBuffer(), 0, m);
          this.mysqlOutput.flush();
          i += j;
          m = this.maxThreeBytes;
          paramInt -= m - 3;
          m = j;
        } 
      } 
      return;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } 
  }
  
  private void setServerSlowQueryFlags() {
    boolean bool1;
    int i = this.serverStatus;
    boolean bool2 = true;
    if ((i & 0x10) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.queryBadIndexUsed = bool1;
    if ((i & 0x20) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.queryNoIndexUsed = bool1;
    if ((i & 0x800) != 0) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    this.serverQueryWasSlow = bool1;
  }
  
  private final long skipFully(InputStream paramInputStream, long paramLong) throws IOException {
    if (paramLong >= 0L) {
      long l = 0L;
      while (l < paramLong) {
        long l1 = paramInputStream.skip(paramLong - l);
        if (l1 >= 0L) {
          l += l1;
          continue;
        } 
        throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { Long.valueOf(paramLong), Long.valueOf(l) }));
      } 
      return l;
    } 
    throw new IOException("Negative skip length not allowed");
  }
  
  private final int skipLengthEncodedInteger(InputStream paramInputStream) throws IOException {
    switch (paramInputStream.read() & 0xFF) {
      default:
        return 1;
      case 254:
        l = skipFully(paramInputStream, 8L);
        return (int)l + 1;
      case 253:
        l = skipFully(paramInputStream, 3L);
        return (int)l + 1;
      case 252:
        break;
    } 
    long l = skipFully(paramInputStream, 2L);
    return (int)l + 1;
  }
  
  private final ResultSetRow unpackBinaryResultSetRow(Field[] paramArrayOfField, Buffer paramBuffer, int paramInt) throws SQLException {
    int k = paramArrayOfField.length;
    byte[][] arrayOfByte = new byte[k][];
    int i = (k + 9) / 8;
    int j = paramBuffer.getPosition();
    paramBuffer.setPosition(i + j);
    i = 4;
    byte b = 0;
    while (b < k) {
      if ((paramBuffer.readByte(j) & i) != 0) {
        arrayOfByte[b] = null;
      } else if (paramInt != 1008) {
        extractNativeEncodedColumn(paramBuffer, paramArrayOfField, b, arrayOfByte);
      } else {
        unpackNativeEncodedColumn(paramBuffer, paramArrayOfField, b, arrayOfByte);
      } 
      int n = i << 1;
      i = n;
      int m = j;
      if ((n & 0xFF) == 0) {
        m = j + 1;
        i = 1;
      } 
      b++;
      j = m;
    } 
    return new ByteArrayRow(arrayOfByte, getExceptionInterceptor());
  }
  
  private final void unpackNativeEncodedColumn(Buffer paramBuffer, Field[] paramArrayOfField, int paramInt, byte[][] paramArrayOfbyte) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: iload_3
    //   2: aaload
    //   3: astore #18
    //   5: aload #18
    //   7: invokevirtual getMysqlType : ()I
    //   10: istore #7
    //   12: iload #7
    //   14: bipush #15
    //   16: if_icmpeq -> 246
    //   19: iload #7
    //   21: bipush #16
    //   23: if_icmpeq -> 246
    //   26: iload #7
    //   28: sipush #245
    //   31: if_icmpeq -> 246
    //   34: iload #7
    //   36: sipush #246
    //   39: if_icmpeq -> 246
    //   42: iload #7
    //   44: tableswitch default -> 116, 0 -> 246, 1 -> 1440, 2 -> 1392, 3 -> 1342, 4 -> 1322, 5 -> 1302, 6 -> 1498, 7 -> 811, 8 -> 764, 9 -> 1342, 10 -> 444, 11 -> 249, 12 -> 811, 13 -> 1392
    //   116: iload #7
    //   118: tableswitch default -> 156, 249 -> 246, 250 -> 246, 251 -> 246, 252 -> 246, 253 -> 246, 254 -> 246
    //   156: new java/lang/StringBuilder
    //   159: dup
    //   160: invokespecial <init> : ()V
    //   163: astore_1
    //   164: aload_1
    //   165: ldc_w 'MysqlIO.97'
    //   168: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_1
    //   176: aload #18
    //   178: invokevirtual getMysqlType : ()I
    //   181: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload_1
    //   186: ldc_w 'MysqlIO.98'
    //   189: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: pop
    //   196: aload_1
    //   197: iload_3
    //   198: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload_1
    //   203: ldc_w 'MysqlIO.99'
    //   206: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   209: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload_1
    //   214: aload_2
    //   215: arraylength
    //   216: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   219: pop
    //   220: aload_1
    //   221: ldc_w 'MysqlIO.100'
    //   224: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   227: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: pop
    //   231: aload_1
    //   232: invokevirtual toString : ()Ljava/lang/String;
    //   235: ldc_w 'S1000'
    //   238: aload_0
    //   239: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   242: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   245: athrow
    //   246: goto -> 1489
    //   249: aload_1
    //   250: invokevirtual readFieldLength : ()J
    //   253: l2i
    //   254: istore #13
    //   256: iload #13
    //   258: ifeq -> 328
    //   261: aload_1
    //   262: invokevirtual readByte : ()B
    //   265: pop
    //   266: aload_1
    //   267: invokevirtual readLong : ()J
    //   270: pop2
    //   271: aload_1
    //   272: invokevirtual readByte : ()B
    //   275: istore #12
    //   277: aload_1
    //   278: invokevirtual readByte : ()B
    //   281: istore #10
    //   283: aload_1
    //   284: invokevirtual readByte : ()B
    //   287: istore #11
    //   289: iload #12
    //   291: istore #9
    //   293: iload #10
    //   295: istore #8
    //   297: iload #11
    //   299: istore #7
    //   301: iload #13
    //   303: bipush #8
    //   305: if_icmple -> 337
    //   308: aload_1
    //   309: invokevirtual readLong : ()J
    //   312: pop2
    //   313: iload #12
    //   315: istore #9
    //   317: iload #10
    //   319: istore #8
    //   321: iload #11
    //   323: istore #7
    //   325: goto -> 337
    //   328: iconst_0
    //   329: istore #9
    //   331: iconst_0
    //   332: istore #8
    //   334: iconst_0
    //   335: istore #7
    //   337: aload #4
    //   339: iload_3
    //   340: bipush #8
    //   342: newarray byte
    //   344: dup
    //   345: iconst_0
    //   346: iload #9
    //   348: bipush #10
    //   350: idiv
    //   351: bipush #10
    //   353: invokestatic forDigit : (II)C
    //   356: i2b
    //   357: bastore
    //   358: dup
    //   359: iconst_1
    //   360: iload #9
    //   362: bipush #10
    //   364: irem
    //   365: bipush #10
    //   367: invokestatic forDigit : (II)C
    //   370: i2b
    //   371: bastore
    //   372: dup
    //   373: iconst_2
    //   374: bipush #58
    //   376: bastore
    //   377: dup
    //   378: iconst_3
    //   379: iload #8
    //   381: bipush #10
    //   383: idiv
    //   384: bipush #10
    //   386: invokestatic forDigit : (II)C
    //   389: i2b
    //   390: bastore
    //   391: dup
    //   392: iconst_4
    //   393: iload #8
    //   395: bipush #10
    //   397: irem
    //   398: bipush #10
    //   400: invokestatic forDigit : (II)C
    //   403: i2b
    //   404: bastore
    //   405: dup
    //   406: iconst_5
    //   407: bipush #58
    //   409: bastore
    //   410: dup
    //   411: bipush #6
    //   413: iload #7
    //   415: bipush #10
    //   417: idiv
    //   418: bipush #10
    //   420: invokestatic forDigit : (II)C
    //   423: i2b
    //   424: bastore
    //   425: dup
    //   426: bipush #7
    //   428: iload #7
    //   430: bipush #10
    //   432: irem
    //   433: bipush #10
    //   435: invokestatic forDigit : (II)C
    //   438: i2b
    //   439: bastore
    //   440: aastore
    //   441: goto -> 1498
    //   444: aload_1
    //   445: invokevirtual readFieldLength : ()J
    //   448: l2i
    //   449: ifeq -> 473
    //   452: aload_1
    //   453: invokevirtual readInt : ()I
    //   456: istore #8
    //   458: aload_1
    //   459: invokevirtual readByte : ()B
    //   462: istore #9
    //   464: aload_1
    //   465: invokevirtual readByte : ()B
    //   468: istore #7
    //   470: goto -> 482
    //   473: iconst_0
    //   474: istore #8
    //   476: iconst_0
    //   477: istore #9
    //   479: iconst_0
    //   480: istore #7
    //   482: iload #8
    //   484: istore #11
    //   486: iload #9
    //   488: istore #10
    //   490: iload #7
    //   492: istore #12
    //   494: iload #8
    //   496: ifne -> 603
    //   499: iload #8
    //   501: istore #11
    //   503: iload #9
    //   505: istore #10
    //   507: iload #7
    //   509: istore #12
    //   511: iload #9
    //   513: ifne -> 603
    //   516: iload #8
    //   518: istore #11
    //   520: iload #9
    //   522: istore #10
    //   524: iload #7
    //   526: istore #12
    //   528: iload #7
    //   530: ifne -> 603
    //   533: ldc_w 'convertToNull'
    //   536: aload_0
    //   537: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   540: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   545: invokevirtual equals : (Ljava/lang/Object;)Z
    //   548: ifeq -> 559
    //   551: aload #4
    //   553: iload_3
    //   554: aconst_null
    //   555: aastore
    //   556: goto -> 1498
    //   559: ldc_w 'exception'
    //   562: aload_0
    //   563: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   566: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   571: invokevirtual equals : (Ljava/lang/Object;)Z
    //   574: ifne -> 589
    //   577: iconst_1
    //   578: istore #11
    //   580: iconst_1
    //   581: istore #10
    //   583: iconst_1
    //   584: istore #12
    //   586: goto -> 603
    //   589: ldc_w 'Value '0000-00-00' can not be represented as java.sql.Date'
    //   592: ldc_w 'S1009'
    //   595: aload_0
    //   596: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   599: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   602: athrow
    //   603: iload #11
    //   605: sipush #1000
    //   608: idiv
    //   609: bipush #10
    //   611: invokestatic forDigit : (II)C
    //   614: i2b
    //   615: istore #6
    //   617: iload #11
    //   619: sipush #1000
    //   622: irem
    //   623: istore #7
    //   625: iload #7
    //   627: bipush #100
    //   629: idiv
    //   630: bipush #10
    //   632: invokestatic forDigit : (II)C
    //   635: i2b
    //   636: istore #5
    //   638: iload #7
    //   640: bipush #100
    //   642: irem
    //   643: istore #7
    //   645: aload #4
    //   647: iload_3
    //   648: bipush #10
    //   650: newarray byte
    //   652: dup
    //   653: iconst_0
    //   654: iload #6
    //   656: bastore
    //   657: dup
    //   658: iconst_1
    //   659: iload #5
    //   661: bastore
    //   662: dup
    //   663: iconst_2
    //   664: iload #7
    //   666: bipush #10
    //   668: idiv
    //   669: bipush #10
    //   671: invokestatic forDigit : (II)C
    //   674: i2b
    //   675: bastore
    //   676: dup
    //   677: iconst_3
    //   678: iload #7
    //   680: bipush #10
    //   682: irem
    //   683: bipush #10
    //   685: invokestatic forDigit : (II)C
    //   688: i2b
    //   689: bastore
    //   690: dup
    //   691: iconst_4
    //   692: bipush #45
    //   694: bastore
    //   695: dup
    //   696: iconst_5
    //   697: iload #10
    //   699: bipush #10
    //   701: idiv
    //   702: bipush #10
    //   704: invokestatic forDigit : (II)C
    //   707: i2b
    //   708: bastore
    //   709: dup
    //   710: bipush #6
    //   712: iload #10
    //   714: bipush #10
    //   716: irem
    //   717: bipush #10
    //   719: invokestatic forDigit : (II)C
    //   722: i2b
    //   723: bastore
    //   724: dup
    //   725: bipush #7
    //   727: bipush #45
    //   729: bastore
    //   730: dup
    //   731: bipush #8
    //   733: iload #12
    //   735: bipush #10
    //   737: idiv
    //   738: bipush #10
    //   740: invokestatic forDigit : (II)C
    //   743: i2b
    //   744: bastore
    //   745: dup
    //   746: bipush #9
    //   748: iload #12
    //   750: bipush #10
    //   752: irem
    //   753: bipush #10
    //   755: invokestatic forDigit : (II)C
    //   758: i2b
    //   759: bastore
    //   760: aastore
    //   761: goto -> 1498
    //   764: aload_1
    //   765: invokevirtual readLongLong : ()J
    //   768: lstore #16
    //   770: aload #18
    //   772: invokevirtual isUnsigned : ()Z
    //   775: ifne -> 793
    //   778: aload #4
    //   780: iload_3
    //   781: lload #16
    //   783: invokestatic valueOf : (J)Ljava/lang/String;
    //   786: invokestatic getBytes : (Ljava/lang/String;)[B
    //   789: aastore
    //   790: goto -> 1498
    //   793: aload #4
    //   795: iload_3
    //   796: lload #16
    //   798: invokestatic convertLongToUlong : (J)Ljava/math/BigInteger;
    //   801: invokevirtual toString : ()Ljava/lang/String;
    //   804: invokestatic getBytes : (Ljava/lang/String;)[B
    //   807: aastore
    //   808: goto -> 1498
    //   811: aload_1
    //   812: invokevirtual readFieldLength : ()J
    //   815: l2i
    //   816: istore #10
    //   818: iload #10
    //   820: ifeq -> 871
    //   823: aload_1
    //   824: invokevirtual readInt : ()I
    //   827: istore #7
    //   829: aload_1
    //   830: invokevirtual readByte : ()B
    //   833: istore #9
    //   835: aload_1
    //   836: invokevirtual readByte : ()B
    //   839: istore #8
    //   841: iload #10
    //   843: iconst_4
    //   844: if_icmple -> 868
    //   847: aload_1
    //   848: invokevirtual readByte : ()B
    //   851: istore #10
    //   853: aload_1
    //   854: invokevirtual readByte : ()B
    //   857: istore #11
    //   859: aload_1
    //   860: invokevirtual readByte : ()B
    //   863: istore #12
    //   865: goto -> 889
    //   868: goto -> 880
    //   871: iconst_0
    //   872: istore #7
    //   874: iconst_0
    //   875: istore #9
    //   877: iconst_0
    //   878: istore #8
    //   880: iconst_0
    //   881: istore #10
    //   883: iconst_0
    //   884: istore #11
    //   886: iconst_0
    //   887: istore #12
    //   889: iload #7
    //   891: istore #14
    //   893: iload #9
    //   895: istore #13
    //   897: iload #8
    //   899: istore #15
    //   901: iload #7
    //   903: ifne -> 1010
    //   906: iload #7
    //   908: istore #14
    //   910: iload #9
    //   912: istore #13
    //   914: iload #8
    //   916: istore #15
    //   918: iload #9
    //   920: ifne -> 1010
    //   923: iload #7
    //   925: istore #14
    //   927: iload #9
    //   929: istore #13
    //   931: iload #8
    //   933: istore #15
    //   935: iload #8
    //   937: ifne -> 1010
    //   940: ldc_w 'convertToNull'
    //   943: aload_0
    //   944: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   947: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   952: invokevirtual equals : (Ljava/lang/Object;)Z
    //   955: ifeq -> 966
    //   958: aload #4
    //   960: iload_3
    //   961: aconst_null
    //   962: aastore
    //   963: goto -> 1498
    //   966: ldc_w 'exception'
    //   969: aload_0
    //   970: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   973: invokeinterface getZeroDateTimeBehavior : ()Ljava/lang/String;
    //   978: invokevirtual equals : (Ljava/lang/Object;)Z
    //   981: ifne -> 996
    //   984: iconst_1
    //   985: istore #14
    //   987: iconst_1
    //   988: istore #13
    //   990: iconst_1
    //   991: istore #15
    //   993: goto -> 1010
    //   996: ldc_w 'Value '0000-00-00' can not be represented as java.sql.Timestamp'
    //   999: ldc_w 'S1009'
    //   1002: aload_0
    //   1003: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1006: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1009: athrow
    //   1010: iconst_0
    //   1011: invokestatic toString : (I)Ljava/lang/String;
    //   1014: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1017: astore_2
    //   1018: aload_2
    //   1019: arraylength
    //   1020: iconst_1
    //   1021: iadd
    //   1022: bipush #19
    //   1024: iadd
    //   1025: newarray byte
    //   1027: astore_1
    //   1028: aload_1
    //   1029: iconst_0
    //   1030: iload #14
    //   1032: sipush #1000
    //   1035: idiv
    //   1036: bipush #10
    //   1038: invokestatic forDigit : (II)C
    //   1041: i2b
    //   1042: bastore
    //   1043: iload #14
    //   1045: sipush #1000
    //   1048: irem
    //   1049: istore #7
    //   1051: aload_1
    //   1052: iconst_1
    //   1053: iload #7
    //   1055: bipush #100
    //   1057: idiv
    //   1058: bipush #10
    //   1060: invokestatic forDigit : (II)C
    //   1063: i2b
    //   1064: bastore
    //   1065: iload #7
    //   1067: bipush #100
    //   1069: irem
    //   1070: istore #7
    //   1072: aload_1
    //   1073: iconst_2
    //   1074: iload #7
    //   1076: bipush #10
    //   1078: idiv
    //   1079: bipush #10
    //   1081: invokestatic forDigit : (II)C
    //   1084: i2b
    //   1085: bastore
    //   1086: aload_1
    //   1087: iconst_3
    //   1088: iload #7
    //   1090: bipush #10
    //   1092: irem
    //   1093: bipush #10
    //   1095: invokestatic forDigit : (II)C
    //   1098: i2b
    //   1099: bastore
    //   1100: aload_1
    //   1101: iconst_4
    //   1102: bipush #45
    //   1104: bastore
    //   1105: aload_1
    //   1106: iconst_5
    //   1107: iload #13
    //   1109: bipush #10
    //   1111: idiv
    //   1112: bipush #10
    //   1114: invokestatic forDigit : (II)C
    //   1117: i2b
    //   1118: bastore
    //   1119: aload_1
    //   1120: bipush #6
    //   1122: iload #13
    //   1124: bipush #10
    //   1126: irem
    //   1127: bipush #10
    //   1129: invokestatic forDigit : (II)C
    //   1132: i2b
    //   1133: bastore
    //   1134: aload_1
    //   1135: bipush #7
    //   1137: bipush #45
    //   1139: bastore
    //   1140: aload_1
    //   1141: bipush #8
    //   1143: iload #15
    //   1145: bipush #10
    //   1147: idiv
    //   1148: bipush #10
    //   1150: invokestatic forDigit : (II)C
    //   1153: i2b
    //   1154: bastore
    //   1155: aload_1
    //   1156: bipush #9
    //   1158: iload #15
    //   1160: bipush #10
    //   1162: irem
    //   1163: bipush #10
    //   1165: invokestatic forDigit : (II)C
    //   1168: i2b
    //   1169: bastore
    //   1170: aload_1
    //   1171: bipush #10
    //   1173: bipush #32
    //   1175: bastore
    //   1176: aload_1
    //   1177: bipush #11
    //   1179: iload #10
    //   1181: bipush #10
    //   1183: idiv
    //   1184: bipush #10
    //   1186: invokestatic forDigit : (II)C
    //   1189: i2b
    //   1190: bastore
    //   1191: aload_1
    //   1192: bipush #12
    //   1194: iload #10
    //   1196: bipush #10
    //   1198: irem
    //   1199: bipush #10
    //   1201: invokestatic forDigit : (II)C
    //   1204: i2b
    //   1205: bastore
    //   1206: aload_1
    //   1207: bipush #13
    //   1209: bipush #58
    //   1211: bastore
    //   1212: aload_1
    //   1213: bipush #14
    //   1215: iload #11
    //   1217: bipush #10
    //   1219: idiv
    //   1220: bipush #10
    //   1222: invokestatic forDigit : (II)C
    //   1225: i2b
    //   1226: bastore
    //   1227: aload_1
    //   1228: bipush #15
    //   1230: iload #11
    //   1232: bipush #10
    //   1234: irem
    //   1235: bipush #10
    //   1237: invokestatic forDigit : (II)C
    //   1240: i2b
    //   1241: bastore
    //   1242: aload_1
    //   1243: bipush #16
    //   1245: bipush #58
    //   1247: bastore
    //   1248: aload_1
    //   1249: bipush #17
    //   1251: iload #12
    //   1253: bipush #10
    //   1255: idiv
    //   1256: bipush #10
    //   1258: invokestatic forDigit : (II)C
    //   1261: i2b
    //   1262: bastore
    //   1263: aload_1
    //   1264: bipush #18
    //   1266: iload #12
    //   1268: bipush #10
    //   1270: irem
    //   1271: bipush #10
    //   1273: invokestatic forDigit : (II)C
    //   1276: i2b
    //   1277: bastore
    //   1278: aload_1
    //   1279: bipush #19
    //   1281: bipush #46
    //   1283: bastore
    //   1284: aload_2
    //   1285: iconst_0
    //   1286: aload_1
    //   1287: bipush #20
    //   1289: aload_2
    //   1290: arraylength
    //   1291: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   1294: aload #4
    //   1296: iload_3
    //   1297: aload_1
    //   1298: aastore
    //   1299: goto -> 1498
    //   1302: aload #4
    //   1304: iload_3
    //   1305: aload_1
    //   1306: invokevirtual readLongLong : ()J
    //   1309: invokestatic longBitsToDouble : (J)D
    //   1312: invokestatic valueOf : (D)Ljava/lang/String;
    //   1315: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1318: aastore
    //   1319: goto -> 1498
    //   1322: aload #4
    //   1324: iload_3
    //   1325: aload_1
    //   1326: invokevirtual readIntAsLong : ()I
    //   1329: invokestatic intBitsToFloat : (I)F
    //   1332: invokestatic valueOf : (F)Ljava/lang/String;
    //   1335: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1338: aastore
    //   1339: goto -> 1498
    //   1342: aload_1
    //   1343: invokevirtual readLong : ()J
    //   1346: l2i
    //   1347: istore #7
    //   1349: aload #18
    //   1351: invokevirtual isUnsigned : ()Z
    //   1354: ifne -> 1372
    //   1357: aload #4
    //   1359: iload_3
    //   1360: iload #7
    //   1362: invokestatic valueOf : (I)Ljava/lang/String;
    //   1365: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1368: aastore
    //   1369: goto -> 1498
    //   1372: aload #4
    //   1374: iload_3
    //   1375: iload #7
    //   1377: i2l
    //   1378: ldc2_w 4294967295
    //   1381: land
    //   1382: invokestatic valueOf : (J)Ljava/lang/String;
    //   1385: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1388: aastore
    //   1389: goto -> 1498
    //   1392: aload_1
    //   1393: invokevirtual readInt : ()I
    //   1396: i2s
    //   1397: istore #7
    //   1399: aload #18
    //   1401: invokevirtual isUnsigned : ()Z
    //   1404: ifne -> 1422
    //   1407: aload #4
    //   1409: iload_3
    //   1410: iload #7
    //   1412: invokestatic valueOf : (I)Ljava/lang/String;
    //   1415: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1418: aastore
    //   1419: goto -> 1498
    //   1422: aload #4
    //   1424: iload_3
    //   1425: iload #7
    //   1427: ldc 65535
    //   1429: iand
    //   1430: invokestatic valueOf : (I)Ljava/lang/String;
    //   1433: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1436: aastore
    //   1437: goto -> 1498
    //   1440: aload_1
    //   1441: invokevirtual readByte : ()B
    //   1444: istore #7
    //   1446: aload #18
    //   1448: invokevirtual isUnsigned : ()Z
    //   1451: ifne -> 1469
    //   1454: aload #4
    //   1456: iload_3
    //   1457: iload #7
    //   1459: invokestatic valueOf : (I)Ljava/lang/String;
    //   1462: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1465: aastore
    //   1466: goto -> 1498
    //   1469: aload #4
    //   1471: iload_3
    //   1472: iload #7
    //   1474: sipush #255
    //   1477: iand
    //   1478: i2s
    //   1479: invokestatic valueOf : (I)Ljava/lang/String;
    //   1482: invokestatic getBytes : (Ljava/lang/String;)[B
    //   1485: aastore
    //   1486: goto -> 1498
    //   1489: aload #4
    //   1491: iload_3
    //   1492: aload_1
    //   1493: iconst_0
    //   1494: invokevirtual readLenByteArray : (I)[B
    //   1497: aastore
    //   1498: return
  }
  
  public static boolean useBufferRowExplicit(Field[] paramArrayOfField) {
    if (paramArrayOfField == null)
      return false; 
    byte b = 0;
    while (b < paramArrayOfField.length) {
      int i = paramArrayOfField[b].getSQLType();
      if (i != -4 && i != -1 && i != 2004 && i != 2005) {
        b++;
        continue;
      } 
      return true;
    } 
    return false;
  }
  
  public void changeUser(String paramString1, String paramString2, String paramString3) throws SQLException {
    this.packetSequence = -1;
    this.compressedPacketSequence = -1;
    boolean bool = false;
    if (paramString1 != null) {
      i = paramString1.length();
    } else {
      i = 0;
    } 
    if (paramString3 != null) {
      j = paramString3.length();
    } else {
      j = 0;
    } 
    int i = (i + 16 + j) * 3 + 7 + 4 + 33;
    int j = this.serverCapabilities;
    if ((0x80000 & j) != 0) {
      proceedHandshakeWithPluggableAuthentication(paramString1, paramString2, paramString3, null);
    } else if ((j & 0x8000) != 0) {
      Buffer buffer = new Buffer(i + 1);
      buffer.writeByte((byte)17);
      if (versionMeetsMinimum(4, 1, 1)) {
        secureAuth411(buffer, i, paramString1, paramString2, paramString3, false, true);
      } else {
        secureAuth(buffer, i, paramString1, paramString2, paramString3, false);
      } 
    } else {
      Buffer buffer = new Buffer(i);
      buffer.writeByte((byte)17);
      buffer.writeString(paramString1);
      if (this.protocolVersion > 9) {
        buffer.writeString(Util.newCrypt(paramString2, this.seed, this.connection.getPasswordCharacterEncoding()));
      } else {
        buffer.writeString(Util.oldCrypt(paramString2, this.seed));
      } 
      i = bool;
      if (this.useConnectWithDb) {
        i = bool;
        if (paramString3 != null) {
          i = bool;
          if (paramString3.length() > 0)
            i = 1; 
        } 
      } 
      if (i != 0)
        buffer.writeString(paramString3); 
      send(buffer, buffer.getPosition());
      checkErrorPacket();
      if (i == 0)
        changeDatabaseTo(paramString3); 
    } 
  }
  
  public Buffer checkErrorPacket() throws SQLException {
    return checkErrorPacket(-1);
  }
  
  public void checkForCharsetMismatch() {
    if (this.connection.getUseUnicode() && this.connection.getEncoding() != null) {
      String str2 = jvmPlatformCharset;
      String str1 = str2;
      if (str2 == null)
        str1 = System.getProperty("file.encoding"); 
      if (str1 == null) {
        this.platformDbCharsetMatches = false;
      } else {
        this.platformDbCharsetMatches = str1.equals(this.connection.getEncoding());
      } 
    } 
  }
  
  public void clearInputStream() throws SQLException {
    try {
      while (true) {
        int i = this.mysqlInput.available();
        if (i > 0) {
          long l = this.mysqlInput.skip(i);
          if (l > 0L)
            continue; 
        } 
        break;
      } 
      return;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } 
  }
  
  public void closeStreamer(RowData paramRowData) throws SQLException {
    RowData rowData = this.streamingData;
    if (rowData != null) {
      if (paramRowData == rowData) {
        this.streamingData = null;
        return;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(Messages.getString("MysqlIO.19"));
      stringBuilder1.append(paramRowData);
      stringBuilder1.append(Messages.getString("MysqlIO.20"));
      stringBuilder1.append(Messages.getString("MysqlIO.21"));
      stringBuilder1.append(Messages.getString("MysqlIO.22"));
      throw SQLError.createSQLException(stringBuilder1.toString(), getExceptionInterceptor());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Messages.getString("MysqlIO.17"));
    stringBuilder.append(paramRowData);
    stringBuilder.append(Messages.getString("MysqlIO.18"));
    throw SQLError.createSQLException(stringBuilder.toString(), getExceptionInterceptor());
  }
  
  public void disableMultiQueries() throws SQLException {
    Buffer buffer = getSharedSendPacket();
    buffer.clear();
    buffer.writeByte((byte)27);
    buffer.writeInt(1);
    sendCommand(27, null, buffer, false, null, 0);
    preserveOldTransactionState();
  }
  
  public void doHandshake(String paramString1, String paramString2, String paramString3) throws SQLException {
    int j = 0;
    this.checkPacketSequence = false;
    this.readPacketSequence = 0;
    Buffer buffer = readPacket();
    byte b = buffer.readByte();
    this.protocolVersion = b;
    if (b != -1) {
      boolean bool;
      String str = buffer.readString("ASCII", getExceptionInterceptor());
      this.serverVersion = str;
      int k = str.indexOf('.');
      if (k != -1) {
        try {
          this.serverMajorVersion = Integer.parseInt(this.serverVersion.substring(0, k));
        } catch (NumberFormatException numberFormatException) {}
        str = this.serverVersion;
        String str1 = str.substring(k + 1, str.length());
        k = str1.indexOf('.');
        if (k != -1) {
          try {
            this.serverMinorVersion = Integer.parseInt(str1.substring(0, k));
          } catch (NumberFormatException numberFormatException) {}
          str = str1.substring(k + 1, str1.length());
          for (k = 0; k < str.length() && str.charAt(k) >= '0' && str.charAt(k) <= '9'; k++);
          try {
            this.serverSubMinorVersion = Integer.parseInt(str.substring(0, k));
          } catch (NumberFormatException numberFormatException) {}
        } 
      } 
      if (versionMeetsMinimum(4, 0, 8)) {
        this.maxThreeBytes = 16777215;
        this.useNewLargePackets = true;
      } else {
        this.maxThreeBytes = 16581375;
        this.useNewLargePackets = false;
      } 
      this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
      this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 15) ^ true;
      this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);
      this.threadId = buffer.readLong();
      if (this.protocolVersion > 9) {
        this.seed = buffer.readString("ASCII", getExceptionInterceptor(), 8);
        buffer.readByte();
      } else {
        this.seed = buffer.readString("ASCII", getExceptionInterceptor());
      } 
      this.serverCapabilities = 0;
      if (buffer.getPosition() < buffer.getBufLength())
        this.serverCapabilities = buffer.readInt(); 
      if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
        this.serverCharsetIndex = buffer.readByte() & 0xFF;
        this.serverStatus = buffer.readInt();
        checkTransactionState(0);
        k = this.serverCapabilities | buffer.readInt() << 16;
        this.serverCapabilities = k;
        if ((k & 0x80000) != 0) {
          this.authPluginDataLength = buffer.readByte() & 0xFF;
        } else {
          buffer.readByte();
        } 
        buffer.setPosition(buffer.getPosition() + 10);
        if ((this.serverCapabilities & 0x8000) != 0) {
          StringBuilder stringBuilder;
          if (this.authPluginDataLength > 0) {
            str = buffer.readString("ASCII", getExceptionInterceptor(), this.authPluginDataLength - 8);
            stringBuilder = new StringBuilder(this.authPluginDataLength);
          } else {
            str = buffer.readString("ASCII", getExceptionInterceptor());
            stringBuilder = new StringBuilder(20);
          } 
          stringBuilder.append(this.seed);
          stringBuilder.append(str);
          this.seed = stringBuilder.toString();
        } 
      } 
      if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression())
        this.clientParam |= 0x20L; 
      if (paramString3 != null && paramString3.length() > 0 && !this.connection.getCreateDatabaseIfNotExist()) {
        bool = true;
      } else {
        bool = false;
      } 
      this.useConnectWithDb = bool;
      if (bool)
        this.clientParam |= 0x8L; 
      if (versionMeetsMinimum(5, 7, 0) && !this.connection.getUseSSL() && !this.connection.isUseSSLExplicit()) {
        this.connection.setUseSSL(true);
        this.connection.setVerifyServerCertificate(false);
        this.connection.getLog().logWarn(Messages.getString("MysqlIO.SSLWarning"));
      } 
      if ((this.serverCapabilities & 0x800) == 0 && this.connection.getUseSSL())
        if (!this.connection.getRequireSSL()) {
          this.connection.setUseSSL(false);
        } else {
          this.connection.close();
          forceClose();
          throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), "08001", getExceptionInterceptor());
        }  
      if ((this.serverCapabilities & 0x4) != 0) {
        this.clientParam |= 0x4L;
        this.hasLongColumnInfo = true;
      } 
      if (!this.connection.getUseAffectedRows())
        this.clientParam |= 0x2L; 
      if (this.connection.getAllowLoadLocalInfile())
        this.clientParam |= 0x80L; 
      if (this.isInteractiveClient)
        this.clientParam |= 0x400L; 
      k = this.serverCapabilities;
      if ((0x1000000 & k) != 0)
        this.clientParam |= 0x1000000L; 
      if ((k & 0x80000) != 0) {
        proceedHandshakeWithPluggableAuthentication(paramString1, paramString2, paramString3, buffer);
        return;
      } 
      if (this.protocolVersion > 9) {
        this.clientParam |= 0x1L;
      } else {
        this.clientParam &= 0xFFFFFFFFFFFFFFFEL;
      } 
      if (versionMeetsMinimum(4, 1, 0) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x4000) != 0)) {
        if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
          long l = this.clientParam | 0x200L;
          this.clientParam = l;
          this.has41NewNewProt = true;
          l |= 0x2000L;
          this.clientParam = l;
          this.clientParam = l | 0x20000L;
          if (this.connection.getAllowMultiQueries())
            this.clientParam |= 0x10000L; 
        } else {
          this.clientParam |= 0x4000L;
          this.has41NewNewProt = false;
        } 
        this.use41Extensions = true;
      } 
      if (paramString1 != null) {
        k = paramString1.length();
      } else {
        k = 0;
      } 
      if (paramString3 != null)
        j = paramString3.length(); 
      k = (k + 16 + j) * 3 + 7 + 4 + 33;
      if (!this.connection.getUseSSL()) {
        if ((this.serverCapabilities & 0x8000) != 0) {
          this.clientParam |= 0x8000L;
          if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
            secureAuth411(null, k, paramString1, paramString2, paramString3, true, false);
          } else {
            secureAuth(null, k, paramString1, paramString2, paramString3, true);
          } 
        } else {
          Buffer buffer1 = new Buffer(k);
          long l = this.clientParam;
          if ((l & 0x4000L) != 0L) {
            if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
              buffer1.writeLong(this.clientParam);
              buffer1.writeLong(this.maxThreeBytes);
              buffer1.writeByte((byte)8);
              buffer1.writeBytesNoNull(new byte[23]);
            } else {
              buffer1.writeLong(this.clientParam);
              buffer1.writeLong(this.maxThreeBytes);
            } 
          } else {
            buffer1.writeInt((int)l);
            buffer1.writeLongInt(this.maxThreeBytes);
          } 
          buffer1.writeString(paramString1, "Cp1252", this.connection);
          if (this.protocolVersion > 9) {
            buffer1.writeString(Util.newCrypt(paramString2, this.seed, this.connection.getPasswordCharacterEncoding()), "Cp1252", this.connection);
          } else {
            buffer1.writeString(Util.oldCrypt(paramString2, this.seed), "Cp1252", this.connection);
          } 
          if (this.useConnectWithDb)
            buffer1.writeString(paramString3, "Cp1252", this.connection); 
          send(buffer1, buffer1.getPosition());
        } 
      } else {
        negotiateSSLConnection(paramString1, paramString2, paramString3, k);
        if ((this.serverCapabilities & 0x8000) != 0) {
          if (versionMeetsMinimum(4, 1, 1)) {
            secureAuth411(null, k, paramString1, paramString2, paramString3, true, false);
          } else {
            secureAuth411(null, k, paramString1, paramString2, paramString3, true, false);
          } 
        } else {
          Buffer buffer1 = new Buffer(k);
          if (this.use41Extensions) {
            buffer1.writeLong(this.clientParam);
            buffer1.writeLong(this.maxThreeBytes);
          } else {
            buffer1.writeInt((int)this.clientParam);
            buffer1.writeLongInt(this.maxThreeBytes);
          } 
          buffer1.writeString(paramString1);
          if (this.protocolVersion > 9) {
            buffer1.writeString(Util.newCrypt(paramString2, this.seed, this.connection.getPasswordCharacterEncoding()));
          } else {
            buffer1.writeString(Util.oldCrypt(paramString2, this.seed));
          } 
          if ((this.serverCapabilities & 0x8) != 0 && paramString3 != null && paramString3.length() > 0)
            buffer1.writeString(paramString3); 
          send(buffer1, buffer1.getPosition());
        } 
      } 
      if (!versionMeetsMinimum(4, 1, 1) || this.protocolVersion <= 9 || (this.serverCapabilities & 0x200) == 0)
        checkErrorPacket(); 
      if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression() && !(this.mysqlInput instanceof CompressedInputStream)) {
        this.deflater = new Deflater();
        this.useCompression = true;
        this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
      } 
      if (!this.useConnectWithDb)
        changeDatabaseTo(paramString3); 
      try {
        this.mysqlConnection = this.socketFactory.afterHandshake();
        return;
      } catch (IOException iOException) {
        throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
      } 
    } 
    try {
      this.mysqlConnection.close();
    } catch (Exception exception) {}
    int i = buffer.readInt();
    paramString2 = buffer.readString("ASCII", getExceptionInterceptor());
    StringBuilder stringBuilder1 = new StringBuilder(Messages.getString("MysqlIO.10"));
    stringBuilder1.append(paramString2);
    stringBuilder1.append("\"");
    paramString2 = SQLError.mysqlToSqlState(i, this.connection.getUseSqlStateCodes());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(SQLError.get(paramString2));
    stringBuilder2.append(", ");
    stringBuilder2.append(stringBuilder1.toString());
    throw SQLError.createSQLException(stringBuilder2.toString(), paramString2, i, getExceptionInterceptor());
  }
  
  public void dumpPacketRingBuffer() throws SQLException {
    if (this.packetDebugRingBuffer != null && this.connection.getEnablePacketDebug()) {
      StringBuilder stringBuilder1 = new StringBuilder();
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Last ");
      stringBuilder2.append(this.packetDebugRingBuffer.size());
      stringBuilder2.append(" packets received from server, from oldest->newest:\n");
      stringBuilder1.append(stringBuilder2.toString());
      stringBuilder1.append("\n");
      Iterator<StringBuilder> iterator = this.packetDebugRingBuffer.iterator();
      while (iterator.hasNext()) {
        stringBuilder1.append(iterator.next());
        stringBuilder1.append("\n");
      } 
      this.connection.getLog().logTrace(stringBuilder1.toString());
    } 
  }
  
  public void enableMultiQueries() throws SQLException {
    Buffer buffer = getSharedSendPacket();
    buffer.clear();
    buffer.writeByte((byte)27);
    buffer.writeInt(0);
    sendCommand(27, null, buffer, false, null, 0);
    preserveOldTransactionState();
  }
  
  public void explainSlowQuery(byte[] paramArrayOfbyte, String paramString) throws SQLException {
    StatementImpl statementImpl;
    ResultSet resultSet;
    if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "SELECT") || (versionMeetsMinimum(5, 6, 3) && StringUtils.startsWithIgnoreCaseAndWs(paramString, EXPLAINABLE_STATEMENT_EXTENSION) != -1)) {
      ResultSet resultSet1 = null;
      resultSet = null;
      ResultSet resultSet2 = null;
      ResultSet resultSet3 = null;
      try {
        statementImpl = (PreparedStatement)this.connection.clientPrepareStatement("EXPLAIN ?");
        resultSet1 = resultSet3;
        resultSet = resultSet2;
        try {
          statementImpl.setBytesNoEscapeNoQuotes(1, paramArrayOfbyte);
          resultSet1 = resultSet3;
          resultSet = resultSet2;
          ResultSet resultSet4 = statementImpl.executeQuery();
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          StringBuilder stringBuilder2 = new StringBuilder();
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          StringBuilder stringBuilder1 = new StringBuilder();
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          this();
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          stringBuilder1.append(Messages.getString("MysqlIO.8"));
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          stringBuilder1.append(paramString);
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          stringBuilder1.append(Messages.getString("MysqlIO.9"));
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          this(stringBuilder1.toString());
          resultSet1 = resultSet4;
          resultSet = resultSet4;
          ResultSetUtil.appendResultSetSlashGStyle(stringBuilder2, resultSet4);
          return;
        } catch (SQLException sQLException) {
        
        } finally {
          paramArrayOfbyte = null;
          if (resultSet1 != null)
            resultSet1.close(); 
        } 
        throw paramArrayOfbyte;
      } catch (SQLException sQLException) {
      
      } finally {
        paramArrayOfbyte = null;
        statementImpl = null;
        if (resultSet1 != null)
          resultSet1.close(); 
      } 
    } else {
      return;
    } 
    if (resultSet != null)
      resultSet.close(); 
    if (statementImpl != null) {
      statementImpl.close();
      return;
    } 
  }
  
  public List<ResultSetRow> fetchRowsViaCursor(List<ResultSetRow> paramList, long paramLong, Field[] paramArrayOfField, int paramInt, boolean paramBoolean) throws SQLException {
    if (paramList == null) {
      paramList = new ArrayList<ResultSetRow>(paramInt);
    } else {
      paramList.clear();
    } 
    this.sharedSendPacket.clear();
    this.sharedSendPacket.writeByte((byte)28);
    this.sharedSendPacket.writeLong(paramLong);
    this.sharedSendPacket.writeLong(paramInt);
    sendCommand(28, null, this.sharedSendPacket, true, null, 0);
    while (true) {
      ResultSetRow resultSetRow = nextRow(paramArrayOfField, paramArrayOfField.length, true, 1007, false, paramBoolean, false, null);
      if (resultSetRow != null) {
        paramList.add(resultSetRow);
        continue;
      } 
      return paramList;
    } 
  }
  
  public final void forceClose() {
    try {
      getNetworkResources().forceClose();
      return;
    } finally {
      this.mysqlConnection = null;
      this.mysqlInput = null;
      this.mysqlOutput = null;
    } 
  }
  
  public int getCommandCount() {
    return this.commandCount;
  }
  
  public long getCurrentTimeNanosOrMillis() {
    return this.useNanosForElapsedTime ? TimeUtil.getCurrentTimeNanosOrMillis() : System.currentTimeMillis();
  }
  
  public String getEncodingForHandshake() {
    String str2 = this.connection.getEncoding();
    String str1 = str2;
    if (str2 == null)
      str1 = "UTF-8"; 
    return str1;
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  public String getHost() {
    return this.host;
  }
  
  public long getLastPacketReceivedTimeMs() {
    return this.lastPacketReceivedTimeMs;
  }
  
  public long getLastPacketSentTimeMs() {
    return this.lastPacketSentTimeMs;
  }
  
  public NetworkResources getNetworkResources() {
    return new NetworkResources(this.mysqlConnection, this.mysqlInput, this.mysqlOutput);
  }
  
  public String getQueryTimingUnits() {
    return this.queryTimingUnits;
  }
  
  public ResultSetImpl getResultSet(StatementImpl paramStatementImpl, long paramLong, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, String paramString, boolean paramBoolean2, Field[] paramArrayOfField) throws SQLException {
    ResultSetImpl resultSetImpl;
    Field[] arrayOfField;
    RowData rowData;
    boolean bool = false;
    if (paramArrayOfField == null) {
      Field[] arrayOfField1 = new Field[(int)paramLong];
      byte b = 0;
      while (true) {
        arrayOfField = arrayOfField1;
        if (b < paramLong) {
          arrayOfField1[b] = unpackField(readPacket(), false);
          b++;
          continue;
        } 
        break;
      } 
    } else {
      for (byte b = 0; b < paramLong; b++)
        skipPacket(); 
      arrayOfField = null;
    } 
    if (!isEOFDeprecated() || (this.connection.versionMeetsMinimum(5, 0, 2) && paramStatementImpl != null && paramBoolean2 && paramStatementImpl.isCursorRequired()))
      readServerStatusForResultSets(reuseAndReadPacket(this.reusablePacket)); 
    if (this.connection.versionMeetsMinimum(5, 0, 2) && this.connection.getUseCursorFetch() && paramBoolean2 && paramStatementImpl != null && paramStatementImpl.getFetchSize() != 0 && paramStatementImpl.getResultSetType() == 1003) {
      boolean bool1;
      ServerPreparedStatement serverPreparedStatement = (ServerPreparedStatement)paramStatementImpl;
      if (this.connection.versionMeetsMinimum(5, 0, 5)) {
        bool1 = bool;
        if ((this.serverStatus & 0x40) != 0)
          bool1 = true; 
      } else {
        bool1 = true;
      } 
      if (bool1) {
        resultSetImpl = buildResultSetWithRows(paramStatementImpl, paramString, arrayOfField, new RowDataCursor(this, serverPreparedStatement, arrayOfField), paramInt2, paramInt3, paramBoolean2);
        if (bool1)
          resultSetImpl.setFetchSize(paramStatementImpl.getFetchSize()); 
        return resultSetImpl;
      } 
    } 
    if (!paramBoolean1) {
      Field[] arrayOfField1;
      if (paramArrayOfField == null) {
        arrayOfField1 = arrayOfField;
      } else {
        arrayOfField1 = paramArrayOfField;
      } 
      rowData = readSingleRowSet(paramLong, paramInt1, paramInt3, paramBoolean2, arrayOfField1);
    } else {
      Field[] arrayOfField1;
      paramInt1 = (int)paramLong;
      if (paramArrayOfField == null) {
        arrayOfField1 = arrayOfField;
      } else {
        arrayOfField1 = paramArrayOfField;
      } 
      rowData = new RowDataDynamic(this, paramInt1, arrayOfField1, paramBoolean2);
      this.streamingData = rowData;
    } 
    if (paramArrayOfField == null)
      paramArrayOfField = arrayOfField; 
    return buildResultSetWithRows(paramStatementImpl, (String)resultSetImpl, paramArrayOfField, rowData, paramInt2, paramInt3, paramBoolean2);
  }
  
  public final int getServerMajorVersion() {
    return this.serverMajorVersion;
  }
  
  public final int getServerMinorVersion() {
    return this.serverMinorVersion;
  }
  
  public int getServerStatus() {
    return this.serverStatus;
  }
  
  public final int getServerSubMinorVersion() {
    return this.serverSubMinorVersion;
  }
  
  public String getServerVersion() {
    return this.serverVersion;
  }
  
  public Buffer getSharedSendPacket() {
    if (this.sharedSendPacket == null)
      this.sharedSendPacket = new Buffer(1024); 
    return this.sharedSendPacket;
  }
  
  public long getSlowQueryThreshold() {
    return this.slowQueryThreshold;
  }
  
  public long getThreadId() {
    return this.threadId;
  }
  
  public boolean hadWarnings() {
    return this.hadWarnings;
  }
  
  public boolean hasLongColumnInfo() {
    return this.hasLongColumnInfo;
  }
  
  public boolean inTransactionOnServer() {
    int i = this.serverStatus;
    boolean bool = true;
    if ((i & 0x1) == 0)
      bool = false; 
    return bool;
  }
  
  public ResultSetInternalMethods invokeStatementInterceptorsPost(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, boolean paramBoolean, SQLException paramSQLException) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield statementInterceptors : Ljava/util/List;
    //   4: invokeinterface size : ()I
    //   9: istore #9
    //   11: iconst_0
    //   12: istore #6
    //   14: aload_3
    //   15: astore #11
    //   17: iload #6
    //   19: iload #9
    //   21: if_icmpge -> 152
    //   24: aload_0
    //   25: getfield statementInterceptors : Ljava/util/List;
    //   28: iload #6
    //   30: invokeinterface get : (I)Ljava/lang/Object;
    //   35: checkcast com/mysql/jdbc/StatementInterceptorV2
    //   38: astore #12
    //   40: aload #12
    //   42: invokeinterface executeTopLevelOnly : ()Z
    //   47: istore #10
    //   49: iconst_1
    //   50: istore #8
    //   52: iload #10
    //   54: ifeq -> 78
    //   57: iload #8
    //   59: istore #7
    //   61: aload_0
    //   62: getfield statementExecutionDepth : I
    //   65: iconst_1
    //   66: if_icmpeq -> 93
    //   69: iload #8
    //   71: istore #7
    //   73: iload #4
    //   75: ifne -> 93
    //   78: iload #10
    //   80: ifne -> 90
    //   83: iload #8
    //   85: istore #7
    //   87: goto -> 93
    //   90: iconst_0
    //   91: istore #7
    //   93: aload #11
    //   95: astore_3
    //   96: iload #7
    //   98: ifeq -> 143
    //   101: aload #12
    //   103: aload_1
    //   104: aload_2
    //   105: aload #11
    //   107: aload_0
    //   108: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   111: aload_0
    //   112: getfield warningCount : I
    //   115: aload_0
    //   116: getfield queryNoIndexUsed : Z
    //   119: aload_0
    //   120: getfield queryBadIndexUsed : Z
    //   123: aload #5
    //   125: invokeinterface postProcess : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Lcom/mysql/jdbc/ResultSetInternalMethods;Lcom/mysql/jdbc/Connection;IZZLjava/sql/SQLException;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   130: astore #12
    //   132: aload #11
    //   134: astore_3
    //   135: aload #12
    //   137: ifnull -> 143
    //   140: aload #12
    //   142: astore_3
    //   143: iinc #6, 1
    //   146: aload_3
    //   147: astore #11
    //   149: goto -> 17
    //   152: aload #11
    //   154: areturn
  }
  
  public ResultSetInternalMethods invokeStatementInterceptorsPre(String paramString, Statement paramStatement, boolean paramBoolean) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield statementInterceptors : Ljava/util/List;
    //   4: invokeinterface size : ()I
    //   9: istore #7
    //   11: aconst_null
    //   12: astore #10
    //   14: iconst_0
    //   15: istore #4
    //   17: iload #4
    //   19: iload #7
    //   21: if_icmpge -> 139
    //   24: aload_0
    //   25: getfield statementInterceptors : Ljava/util/List;
    //   28: iload #4
    //   30: invokeinterface get : (I)Ljava/lang/Object;
    //   35: checkcast com/mysql/jdbc/StatementInterceptorV2
    //   38: astore #11
    //   40: aload #11
    //   42: invokeinterface executeTopLevelOnly : ()Z
    //   47: istore #8
    //   49: iconst_1
    //   50: istore #6
    //   52: iload #8
    //   54: ifeq -> 77
    //   57: iload #6
    //   59: istore #5
    //   61: aload_0
    //   62: getfield statementExecutionDepth : I
    //   65: iconst_1
    //   66: if_icmpeq -> 92
    //   69: iload #6
    //   71: istore #5
    //   73: iload_3
    //   74: ifne -> 92
    //   77: iload #8
    //   79: ifne -> 89
    //   82: iload #6
    //   84: istore #5
    //   86: goto -> 92
    //   89: iconst_0
    //   90: istore #5
    //   92: aload #10
    //   94: astore #9
    //   96: iload #5
    //   98: ifeq -> 129
    //   101: aload #11
    //   103: aload_1
    //   104: aload_2
    //   105: aload_0
    //   106: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   109: invokeinterface preProcess : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Lcom/mysql/jdbc/Connection;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   114: astore #11
    //   116: aload #10
    //   118: astore #9
    //   120: aload #11
    //   122: ifnull -> 129
    //   125: aload #11
    //   127: astore #9
    //   129: iinc #4, 1
    //   132: aload #9
    //   134: astore #10
    //   136: goto -> 17
    //   139: aload #10
    //   141: areturn
  }
  
  public boolean isDataAvailable() throws SQLException {
    try {
      boolean bool;
      int i = this.mysqlInput.available();
      if (i > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } 
  }
  
  public boolean isEOFDeprecated() {
    boolean bool;
    if ((this.clientParam & 0x1000000L) != 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isSSLEstablished() {
    boolean bool;
    if (ExportControlled.enabled() && ExportControlled.isSSLEstablished(this)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isSetNeededForAutoCommitMode(boolean paramBoolean) {
    boolean bool3 = this.use41Extensions;
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (bool3) {
      bool1 = bool2;
      if (this.connection.getElideSetAutoCommits()) {
        if ((this.serverStatus & 0x2) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (!paramBoolean && versionMeetsMinimum(5, 0, 0))
          return inTransactionOnServer() ^ true; 
        if (bool1 != paramBoolean) {
          bool1 = bool2;
        } else {
          bool1 = false;
        } 
      } 
    } 
    return bool1;
  }
  
  public boolean isVersion(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool;
    if (paramInt1 == getServerMajorVersion() && paramInt2 == getServerMinorVersion() && paramInt3 == getServerSubMinorVersion()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final ResultSetRow nextRow(Field[] paramArrayOfField, int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, Buffer paramBuffer) throws SQLException {
    byte[][] arrayOfByte;
    boolean bool;
    if (this.useDirectRowUnpack && paramBuffer == null && !paramBoolean1 && !paramBoolean2 && !paramBoolean3)
      return nextRowFast(paramArrayOfField, paramInt1, paramBoolean1, paramInt2, paramBoolean2, paramBoolean3, paramBoolean4); 
    if (paramBuffer == null) {
      Buffer buffer = checkErrorPacket();
      bool = paramBoolean3;
      paramBuffer = buffer;
      if (!paramBoolean3) {
        bool = paramBoolean3;
        paramBuffer = buffer;
        if (paramBoolean2) {
          bool = paramBoolean3;
          paramBuffer = buffer;
          if (buffer.getBufLength() > this.useBufferRowSizeThreshold) {
            bool = true;
            paramBuffer = buffer;
          } 
        } 
      } 
    } else {
      checkErrorPacket(paramBuffer);
      bool = paramBoolean3;
    } 
    if (!paramBoolean1) {
      paramBuffer.setPosition(paramBuffer.getPosition() - 1);
      if ((isEOFDeprecated() || !paramBuffer.isEOFPacket()) && (!isEOFDeprecated() || !paramBuffer.isResultSetOKPacket())) {
        if (paramInt2 == 1008 || (!paramBoolean2 && !bool)) {
          arrayOfByte = new byte[paramInt1][];
          for (paramInt2 = 0; paramInt2 < paramInt1; paramInt2++)
            arrayOfByte[paramInt2] = paramBuffer.readLenByteArray(0); 
          return new ByteArrayRow(arrayOfByte, getExceptionInterceptor());
        } 
        if (!paramBoolean4)
          this.reusablePacket = new Buffer(paramBuffer.getBufLength()); 
        return new BufferRow(paramBuffer, (Field[])arrayOfByte, false, getExceptionInterceptor());
      } 
      readServerStatusForResultSets(paramBuffer);
      return null;
    } 
    if ((isEOFDeprecated() || !paramBuffer.isEOFPacket()) && (!isEOFDeprecated() || !paramBuffer.isResultSetOKPacket())) {
      if (paramInt2 == 1008 || (!paramBoolean2 && !bool))
        return unpackBinaryResultSetRow((Field[])arrayOfByte, paramBuffer, paramInt2); 
      if (!paramBoolean4)
        this.reusablePacket = new Buffer(paramBuffer.getBufLength()); 
      return new BufferRow(paramBuffer, (Field[])arrayOfByte, true, getExceptionInterceptor());
    } 
    paramBuffer.setPosition(paramBuffer.getPosition() - 1);
    readServerStatusForResultSets(paramBuffer);
    return null;
  }
  
  public final ResultSetRow nextRowFast(Field[] paramArrayOfField, int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) throws SQLException {
    try {
      int i = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
      if (i >= 4) {
        IOException iOException;
        byte[] arrayOfByte = this.packetHeaderBuf;
        int k = (arrayOfByte[0] & 0xFF) + ((arrayOfByte[1] & 0xFF) << 8) + ((arrayOfByte[2] & 0xFF) << 16);
        if (k == this.maxThreeBytes) {
          reuseAndReadPacket(this.reusablePacket, k);
          return nextRow(paramArrayOfField, paramInt1, paramBoolean1, paramInt2, paramBoolean2, paramBoolean3, paramBoolean4, this.reusablePacket);
        } 
        if (k > this.useBufferRowSizeThreshold) {
          reuseAndReadPacket(this.reusablePacket, k);
          return nextRow(paramArrayOfField, paramInt1, paramBoolean1, paramInt2, true, true, false, this.reusablePacket);
        } 
        paramInt2 = k;
        int j = 0;
        i = 1;
        paramArrayOfField = null;
        while (j < paramInt1) {
          byte[][] arrayOfByte1;
          int n = this.mysqlInput.read() & 0xFF;
          paramInt2--;
          int m = i;
          if (i != 0) {
            if (n == 255) {
              Buffer buffer = new Buffer();
              this(k + 4);
              buffer.setPosition(0);
              buffer.writeByte(this.packetHeaderBuf[0]);
              buffer.writeByte(this.packetHeaderBuf[1]);
              buffer.writeByte(this.packetHeaderBuf[2]);
              buffer.writeByte((byte)1);
              buffer.writeByte((byte)n);
              readFully(this.mysqlInput, buffer.getByteBuffer(), 5, k - 1);
              buffer.setPosition(4);
              checkErrorPacket(buffer);
            } 
            if (n == 254 && k < 16777215) {
              if (this.use41Extensions) {
                if (isEOFDeprecated()) {
                  paramInt1 = skipLengthEncodedInteger(this.mysqlInput);
                  j = skipLengthEncodedInteger(this.mysqlInput);
                  this.oldServerStatus = this.serverStatus;
                  this.serverStatus = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                  checkTransactionState(this.oldServerStatus);
                  i = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                  this.warningCount = i;
                  paramInt2 = paramInt2 - paramInt1 - j - 2 - 2;
                  paramInt1 = paramInt2;
                  if (i > 0) {
                    this.hadWarnings = true;
                    paramInt1 = paramInt2;
                  } 
                } else {
                  paramInt1 = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                  this.warningCount = paramInt1;
                  if (paramInt1 > 0)
                    this.hadWarnings = true; 
                  this.oldServerStatus = this.serverStatus;
                  this.serverStatus = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                  checkTransactionState(this.oldServerStatus);
                  paramInt1 = paramInt2 - 2 - 2;
                } 
                setServerSlowQueryFlags();
                if (paramInt1 > 0)
                  skipFully(this.mysqlInput, paramInt1); 
              } 
              return null;
            } 
            arrayOfByte1 = new byte[paramInt1][];
            m = 0;
          } 
          switch (n) {
            default:
              i = n;
              break;
            case 254:
              i = (int)((this.mysqlInput.read() & 0xFF) | (this.mysqlInput.read() & 0xFF) << 8L | (this.mysqlInput.read() & 0xFF) << 16L | (this.mysqlInput.read() & 0xFF) << 24L | (this.mysqlInput.read() & 0xFF) << 32L | (this.mysqlInput.read() & 0xFF) << 40L | (this.mysqlInput.read() & 0xFF) << 48L | (this.mysqlInput.read() & 0xFF) << 56L);
              paramInt2 -= 8;
              break;
            case 253:
              i = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8 | (this.mysqlInput.read() & 0xFF) << 16;
              paramInt2 -= 3;
              break;
            case 252:
              i = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
              paramInt2 -= 2;
              break;
            case 251:
              i = -1;
              break;
          } 
          if (i == -1) {
            arrayOfByte1[j] = null;
          } else if (i == 0) {
            arrayOfByte1[j] = Constants.EMPTY_BYTE_ARRAY;
          } else {
            arrayOfByte1[j] = new byte[i];
            n = readFully(this.mysqlInput, arrayOfByte1[j], 0, i);
            if (n == i) {
              paramInt2 -= n;
            } else {
              MySQLConnection mySQLConnection = this.connection;
              long l2 = this.lastPacketSentTimeMs;
              long l1 = this.lastPacketReceivedTimeMs;
              iOException = new IOException();
              this(Messages.getString("MysqlIO.43"));
              throw SQLError.createCommunicationsException(mySQLConnection, l2, l1, iOException, getExceptionInterceptor());
            } 
          } 
          j++;
          i = m;
        } 
        if (paramInt2 > 0)
          skipFully(this.mysqlInput, paramInt2); 
        return new ByteArrayRow((byte[][])iOException, getExceptionInterceptor());
      } 
      forceClose();
      RuntimeException runtimeException = new RuntimeException();
      this(Messages.getString("MysqlIO.43"));
      throw runtimeException;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } 
  }
  
  public final void quit() throws SQLException {
    try {
      boolean bool = this.mysqlConnection.isClosed();
      if (!bool)
        try {
          this.mysqlConnection.shutdownInput();
        } catch (UnsupportedOperationException unsupportedOperationException) {} 
    } catch (IOException iOException) {
      this.connection.getLog().logWarn("Caught while disconnecting...", iOException);
    } finally {
      Exception exception;
    } 
    Buffer buffer = new Buffer();
    this(6);
    this.packetSequence = -1;
    this.compressedPacketSequence = -1;
    buffer.writeByte((byte)1);
    send(buffer, buffer.getPosition());
    forceClose();
  }
  
  public ResultSetImpl readAllResults(StatementImpl paramStatementImpl, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, String paramString, Buffer paramBuffer, boolean paramBoolean2, long paramLong, Field[] paramArrayOfField) throws SQLException {
    int i;
    byte b;
    paramBuffer.setPosition(paramBuffer.getPosition() - 1);
    ResultSetImpl resultSetImpl2 = readResultsForQueryOrUpdate(paramStatementImpl, paramInt1, paramInt2, paramInt3, paramBoolean1, paramString, paramBuffer, paramBoolean2, paramLong, paramArrayOfField);
    if ((this.clientParam & 0x20000L) != 0L) {
      i = 1;
    } else {
      i = 0;
    } 
    if ((this.serverStatus & 0x8) != 0) {
      b = 1;
    } else {
      b = 0;
    } 
    if (b && paramBoolean1) {
      if (resultSetImpl2.getUpdateCount() != -1L)
        tackOnMoreStreamingResults(resultSetImpl2); 
      reclaimLargeReusablePacket();
      return resultSetImpl2;
    } 
    i &= b;
    for (ResultSetImpl resultSetImpl1 = resultSetImpl2; i != 0; resultSetImpl1 = resultSetImpl) {
      Buffer buffer = checkErrorPacket();
      buffer.setPosition(0);
      ResultSetImpl resultSetImpl = readResultsForQueryOrUpdate(paramStatementImpl, paramInt1, paramInt2, paramInt3, paramBoolean1, paramString, buffer, paramBoolean2, paramLong, paramArrayOfField);
      resultSetImpl1.setNextResultSet(resultSetImpl);
      if ((this.serverStatus & 0x8) != 0) {
        i = 1;
      } else {
        i = 0;
      } 
    } 
    if (!paramBoolean1)
      clearInputStream(); 
    reclaimLargeReusablePacket();
    return resultSetImpl2;
  }
  
  public final Buffer readPacket() throws SQLException {
    try {
      if (readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4) >= 4) {
        byte[] arrayOfByte = this.packetHeaderBuf;
        int i = (arrayOfByte[0] & 0xFF) + ((arrayOfByte[1] & 0xFF) << 8) + ((arrayOfByte[2] & 0xFF) << 16);
        if (i <= this.maxAllowedPacket) {
          if (this.traceProtocol) {
            StringBuilder stringBuilder1 = new StringBuilder();
            this();
            stringBuilder1.append(Messages.getString("MysqlIO.2"));
            stringBuilder1.append(i);
            stringBuilder1.append(Messages.getString("MysqlIO.3"));
            stringBuilder1.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
            this.connection.getLog().logTrace(stringBuilder1.toString());
          } 
          byte b = this.packetHeaderBuf[3];
          if (!this.packetSequenceReset) {
            if (this.enablePacketDebug && this.checkPacketSequence)
              checkPacketSequencing(b); 
          } else {
            this.packetSequenceReset = false;
          } 
          this.readPacketSequence = b;
          byte[] arrayOfByte1 = new byte[i];
          int j = readFully(this.mysqlInput, arrayOfByte1, 0, i);
          if (j == i) {
            Buffer buffer = new Buffer();
            this(arrayOfByte1);
            if (this.traceProtocol) {
              StringBuilder stringBuilder1 = new StringBuilder();
              this();
              stringBuilder1.append(Messages.getString("MysqlIO.4"));
              stringBuilder1.append(getPacketDumpToLog(buffer, i));
              this.connection.getLog().logTrace(stringBuilder1.toString());
            } 
            if (this.enablePacketDebug)
              enqueuePacketForDebugging(false, false, 0, this.packetHeaderBuf, buffer); 
            if (this.connection.getMaintainTimeStats())
              this.lastPacketReceivedTimeMs = System.currentTimeMillis(); 
            return buffer;
          } 
          IOException iOException1 = new IOException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Short read, expected ");
          stringBuilder.append(i);
          stringBuilder.append(" bytes, only read ");
          stringBuilder.append(j);
          this(stringBuilder.toString());
          throw iOException1;
        } 
        PacketTooBigException packetTooBigException = new PacketTooBigException();
        this(i, this.maxAllowedPacket);
        throw packetTooBigException;
      } 
      forceClose();
      IOException iOException = new IOException();
      this(Messages.getString("MysqlIO.1"));
      throw iOException;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } catch (OutOfMemoryError outOfMemoryError) {
      try {
        this.connection.realClose(false, false, true, outOfMemoryError);
      } catch (Exception exception) {}
    } 
  }
  
  public final ResultSetImpl readResultsForQueryOrUpdate(StatementImpl paramStatementImpl, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, String paramString, Buffer paramBuffer, boolean paramBoolean2, long paramLong, Field[] paramArrayOfField) throws SQLException {
    paramLong = paramBuffer.readFieldLength();
    if (paramLong == 0L)
      return buildResultSetWithUpdates(paramStatementImpl, paramBuffer); 
    if (paramLong == -1L) {
      paramString = null;
      if (this.connection.getUseUnicode())
        paramString = this.connection.getEncoding(); 
      if (this.platformDbCharsetMatches) {
        if (paramString != null) {
          paramString = paramBuffer.readString(paramString, getExceptionInterceptor());
        } else {
          paramString = paramBuffer.readString();
        } 
      } else {
        paramString = paramBuffer.readString();
      } 
      return sendFileToServer(paramStatementImpl, paramString);
    } 
    return getResultSet(paramStatementImpl, paramLong, paramInt1, paramInt2, paramInt3, paramBoolean1, paramString, paramBoolean2, paramArrayOfField);
  }
  
  public void releaseResources() {
    Deflater deflater = this.deflater;
    if (deflater != null) {
      deflater.end();
      this.deflater = null;
    } 
  }
  
  public void resetMaxBuf() {
    this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
  }
  
  public void resetReadPacketSequence() {
    this.readPacketSequence = 0;
  }
  
  public void scanForAndThrowDataTruncation() throws SQLException {
    if (this.streamingData == null && versionMeetsMinimum(4, 1, 0) && this.connection.getJdbcCompliantTruncation()) {
      int i = this.warningCount;
      if (i > 0) {
        SQLError.convertShowWarningsToSQLWarnings(this.connection, i, true);
        this.warningCount = i;
      } 
    } 
  }
  
  public void secureAuth411(Buffer paramBuffer, int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
    String str = getEncodingForHandshake();
    Buffer buffer = paramBuffer;
    if (paramBuffer == null)
      buffer = new Buffer(paramInt); 
    if (paramBoolean1)
      if (this.use41Extensions) {
        if (versionMeetsMinimum(4, 1, 1)) {
          buffer.writeLong(this.clientParam);
          buffer.writeLong(this.maxThreeBytes);
          appendCharsetByteForHandshake(buffer, str);
          buffer.writeBytesNoNull(new byte[23]);
        } else {
          buffer.writeLong(this.clientParam);
          buffer.writeLong(this.maxThreeBytes);
        } 
      } else {
        buffer.writeInt((int)this.clientParam);
        buffer.writeLongInt(this.maxThreeBytes);
      }  
    if (paramString1 != null)
      buffer.writeString(paramString1, str, this.connection); 
    if (paramString2.length() != 0) {
      buffer.writeByte((byte)20);
      try {
        buffer.writeBytesNoNull(Security.scramble411(paramString2, this.seed, this.connection.getPasswordCharacterEncoding()));
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("MysqlIO.91"));
        stringBuilder.append(Messages.getString("MysqlIO.92"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("MysqlIO.91"));
        stringBuilder.append(Messages.getString("MysqlIO.92"));
        throw SQLError.createSQLException(stringBuilder.toString(), "S1000", getExceptionInterceptor());
      } 
    } else {
      buffer.writeByte((byte)0);
    } 
    if (this.useConnectWithDb) {
      buffer.writeString(paramString3, str, this.connection);
    } else if (paramBoolean2) {
      buffer.writeByte((byte)0);
    } 
    if ((this.serverCapabilities & 0x100000) != 0)
      sendConnectionAttributes(buffer, str, this.connection); 
    send(buffer, buffer.getPosition());
    byte b = (byte)(this.packetSequence + 1);
    this.packetSequence = b;
    if (checkErrorPacket().isAuthMethodSwitchRequestPacket()) {
      this.packetSequence = b;
      buffer.clear();
      buffer.writeString(Util.newCrypt(paramString2, this.seed.substring(0, 8), this.connection.getPasswordCharacterEncoding()));
      send(buffer, buffer.getPosition());
      checkErrorPacket();
    } 
    if (!this.useConnectWithDb)
      changeDatabaseTo(paramString3); 
  }
  
  public final Buffer sendCommand(int paramInt1, String paramString1, Buffer paramBuffer, boolean paramBoolean, String paramString2, int paramInt2) throws SQLException {
    boolean bool;
    this.commandCount++;
    this.enablePacketDebug = this.connection.getEnablePacketDebug();
    this.readPacketSequence = 0;
    if (paramInt2 != 0) {
      try {
        bool = this.mysqlConnection.getSoTimeout();
        this.mysqlConnection.setSoTimeout(paramInt2);
      } catch (SocketException null) {
        throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, socketException, getExceptionInterceptor());
      } 
    } else {
      bool = false;
    } 
    try {
      checkForOutstandingStreamingData();
      this.oldServerStatus = this.serverStatus;
      this.serverStatus = 0;
      this.hadWarnings = false;
      this.warningCount = 0;
      this.queryNoIndexUsed = false;
      this.queryBadIndexUsed = false;
      this.serverQueryWasSlow = false;
      if (this.useCompression) {
        int i = this.mysqlInput.available();
        if (i > 0)
          this.mysqlInput.skip(i); 
      } 
      try {
        clearInputStream();
        if (paramBuffer == null) {
          byte b;
          if (socketException != null) {
            b = socketException.length();
          } else {
            b = 0;
          } 
          if (this.sendPacket == null) {
            paramBuffer = new Buffer();
            this(8 + b + 2);
            this.sendPacket = paramBuffer;
          } 
          this.packetSequence = -1;
          this.compressedPacketSequence = -1;
          this.readPacketSequence = 0;
          this.checkPacketSequence = true;
          this.sendPacket.clear();
          this.sendPacket.writeByte((byte)paramInt1);
          if (paramInt1 == 2 || paramInt1 == 3 || paramInt1 == 22)
            if (paramString2 == null) {
              this.sendPacket.writeStringNoNull((String)socketException);
            } else {
              this.sendPacket.writeStringNoNull((String)socketException, paramString2, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), this.connection);
            }  
          Buffer buffer = this.sendPacket;
          send(buffer, buffer.getPosition());
        } else {
          this.packetSequence = -1;
          this.compressedPacketSequence = -1;
          send(paramBuffer, paramBuffer.getPosition());
        } 
        socketException = null;
        if (!paramBoolean) {
          if (paramInt1 == 23 || paramInt1 == 26) {
            this.readPacketSequence = 0;
            this.packetSequenceReset = true;
          } 
          Buffer buffer = checkErrorPacket(paramInt1);
        } 
        if (paramInt2 != 0)
          try {
            this.mysqlConnection.setSoTimeout(bool);
          } catch (SocketException socketException1) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, socketException1, getExceptionInterceptor());
          }  
        return (Buffer)socketException1;
      } catch (SQLException sQLException) {
        throw sQLException;
      } catch (Exception exception) {
        throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, exception, getExceptionInterceptor());
      } 
    } catch (IOException iOException) {
      preserveOldTransactionState();
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } catch (SQLException sQLException) {
      preserveOldTransactionState();
      throw sQLException;
    } finally {}
    if (paramInt2 != 0)
      try {
        this.mysqlConnection.setSoTimeout(bool);
      } catch (SocketException socketException) {
        throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, socketException, getExceptionInterceptor());
      }  
    throw socketException;
  }
  
  public void setSocketTimeout(int paramInt) throws SQLException {
    try {
      Socket socket = this.mysqlConnection;
      if (socket != null)
        socket.setSoTimeout(paramInt); 
      return;
    } catch (SocketException socketException) {
      SQLException sQLException = SQLError.createSQLException("Invalid socket timeout value or state", "S1009", getExceptionInterceptor());
      sQLException.initCause(socketException);
      throw sQLException;
    } 
  }
  
  public void setStatementInterceptors(List<StatementInterceptorV2> paramList) {
    List<StatementInterceptorV2> list = paramList;
    if (paramList.isEmpty())
      list = null; 
    this.statementInterceptors = list;
  }
  
  public boolean shouldIntercept() {
    boolean bool;
    if (this.statementInterceptors != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final void skipPacket() throws SQLException {
    try {
      if (readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4) >= 4) {
        byte[] arrayOfByte = this.packetHeaderBuf;
        int i = (arrayOfByte[0] & 0xFF) + ((arrayOfByte[1] & 0xFF) << 8) + ((arrayOfByte[2] & 0xFF) << 16);
        if (this.traceProtocol) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append(Messages.getString("MysqlIO.2"));
          stringBuilder.append(i);
          stringBuilder.append(Messages.getString("MysqlIO.3"));
          stringBuilder.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
          this.connection.getLog().logTrace(stringBuilder.toString());
        } 
        byte b = this.packetHeaderBuf[3];
        if (!this.packetSequenceReset) {
          if (this.enablePacketDebug && this.checkPacketSequence)
            checkPacketSequencing(b); 
        } else {
          this.packetSequenceReset = false;
        } 
        this.readPacketSequence = b;
        skipFully(this.mysqlInput, i);
        return;
      } 
      forceClose();
      IOException iOException = new IOException();
      this(Messages.getString("MysqlIO.1"));
      throw iOException;
    } catch (IOException iOException) {
      throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, iOException, getExceptionInterceptor());
    } catch (OutOfMemoryError outOfMemoryError) {
      try {
        this.connection.realClose(false, false, true, outOfMemoryError);
      } catch (Exception exception) {}
    } 
  }
  
  public final ResultSetInternalMethods sqlQueryDirect(StatementImpl paramStatementImpl, String paramString1, String paramString2, Buffer paramBuffer, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, String paramString3, Field[] paramArrayOfField) throws Exception {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield statementExecutionDepth : I
    //   5: iconst_1
    //   6: iadd
    //   7: putfield statementExecutionDepth : I
    //   10: aload_0
    //   11: getfield statementInterceptors : Ljava/util/List;
    //   14: ifnull -> 44
    //   17: aload_0
    //   18: aload_2
    //   19: aload_1
    //   20: iconst_0
    //   21: invokevirtual invokeStatementInterceptorsPre : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   24: astore #28
    //   26: aload #28
    //   28: ifnull -> 44
    //   31: aload_0
    //   32: aload_0
    //   33: getfield statementExecutionDepth : I
    //   36: iconst_1
    //   37: isub
    //   38: putfield statementExecutionDepth : I
    //   41: aload #28
    //   43: areturn
    //   44: aload_0
    //   45: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   48: invokeinterface getStatementComment : ()Ljava/lang/String;
    //   53: astore #29
    //   55: aload #29
    //   57: astore #28
    //   59: aload_0
    //   60: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   63: invokeinterface getIncludeThreadNamesAsStatementComment : ()Z
    //   68: ifeq -> 164
    //   71: new java/lang/StringBuilder
    //   74: astore #30
    //   76: aload #30
    //   78: invokespecial <init> : ()V
    //   81: aload #29
    //   83: ifnull -> 123
    //   86: new java/lang/StringBuilder
    //   89: astore #28
    //   91: aload #28
    //   93: invokespecial <init> : ()V
    //   96: aload #28
    //   98: aload #29
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload #28
    //   106: ldc_w ', '
    //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload #28
    //   115: invokevirtual toString : ()Ljava/lang/String;
    //   118: astore #28
    //   120: goto -> 128
    //   123: ldc_w ''
    //   126: astore #28
    //   128: aload #30
    //   130: aload #28
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload #30
    //   138: ldc_w 'java thread: '
    //   141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: pop
    //   145: aload #30
    //   147: invokestatic currentThread : ()Ljava/lang/Thread;
    //   150: invokevirtual getName : ()Ljava/lang/String;
    //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: aload #30
    //   159: invokevirtual toString : ()Ljava/lang/String;
    //   162: astore #28
    //   164: aconst_null
    //   165: astore #31
    //   167: aconst_null
    //   168: astore #30
    //   170: aload_2
    //   171: ifnull -> 438
    //   174: aload_2
    //   175: invokevirtual length : ()I
    //   178: iconst_3
    //   179: imul
    //   180: iconst_5
    //   181: iadd
    //   182: iconst_2
    //   183: iadd
    //   184: istore #11
    //   186: aload #28
    //   188: ifnull -> 236
    //   191: aload #28
    //   193: aconst_null
    //   194: aload_3
    //   195: aload_0
    //   196: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   199: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   204: aload_0
    //   205: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   208: invokeinterface parserKnowsUnicode : ()Z
    //   213: aload_0
    //   214: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   217: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ExceptionInterceptor;)[B
    //   220: astore #4
    //   222: iload #11
    //   224: aload #4
    //   226: arraylength
    //   227: iadd
    //   228: bipush #6
    //   230: iadd
    //   231: istore #11
    //   233: goto -> 239
    //   236: aconst_null
    //   237: astore #4
    //   239: aload_0
    //   240: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   243: astore #29
    //   245: aload #29
    //   247: ifnonnull -> 271
    //   250: new com/mysql/jdbc/Buffer
    //   253: astore #29
    //   255: aload #29
    //   257: iload #11
    //   259: invokespecial <init> : (I)V
    //   262: aload_0
    //   263: aload #29
    //   265: putfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   268: goto -> 276
    //   271: aload #29
    //   273: invokevirtual clear : ()V
    //   276: aload_0
    //   277: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   280: iconst_3
    //   281: invokevirtual writeByte : (B)V
    //   284: aload #4
    //   286: ifnull -> 318
    //   289: aload_0
    //   290: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   293: getstatic com/mysql/jdbc/Constants.SLASH_STAR_SPACE_AS_BYTES : [B
    //   296: invokevirtual writeBytesNoNull : ([B)V
    //   299: aload_0
    //   300: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   303: aload #4
    //   305: invokevirtual writeBytesNoNull : ([B)V
    //   308: aload_0
    //   309: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   312: getstatic com/mysql/jdbc/Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES : [B
    //   315: invokevirtual writeBytesNoNull : ([B)V
    //   318: aload_3
    //   319: ifnull -> 421
    //   322: aload_0
    //   323: getfield platformDbCharsetMatches : Z
    //   326: ifeq -> 363
    //   329: aload_0
    //   330: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   333: aload_2
    //   334: aload_3
    //   335: aload_0
    //   336: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   339: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   344: aload_0
    //   345: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   348: invokeinterface parserKnowsUnicode : ()Z
    //   353: aload_0
    //   354: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   357: invokevirtual writeStringNoNull : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/MySQLConnection;)V
    //   360: goto -> 429
    //   363: aload_2
    //   364: ldc_w 'LOAD DATA'
    //   367: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
    //   370: ifeq -> 387
    //   373: aload_0
    //   374: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   377: aload_2
    //   378: invokestatic getBytes : (Ljava/lang/String;)[B
    //   381: invokevirtual writeBytesNoNull : ([B)V
    //   384: goto -> 429
    //   387: aload_0
    //   388: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   391: aload_2
    //   392: aload_3
    //   393: aload_0
    //   394: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   397: invokeinterface getServerCharset : ()Ljava/lang/String;
    //   402: aload_0
    //   403: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   406: invokeinterface parserKnowsUnicode : ()Z
    //   411: aload_0
    //   412: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   415: invokevirtual writeStringNoNull : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/MySQLConnection;)V
    //   418: goto -> 429
    //   421: aload_0
    //   422: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   425: aload_2
    //   426: invokevirtual writeStringNoNull : (Ljava/lang/String;)V
    //   429: aload_0
    //   430: getfield sendPacket : Lcom/mysql/jdbc/Buffer;
    //   433: astore #4
    //   435: goto -> 438
    //   438: aload_0
    //   439: getfield needToGrabQueryFromPacket : Z
    //   442: ifeq -> 468
    //   445: aload #4
    //   447: invokevirtual getByteBuffer : ()[B
    //   450: astore #29
    //   452: aload #4
    //   454: invokevirtual getPosition : ()I
    //   457: istore #11
    //   459: aload_0
    //   460: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   463: lstore #17
    //   465: goto -> 477
    //   468: aconst_null
    //   469: astore #29
    //   471: lconst_0
    //   472: lstore #17
    //   474: iconst_0
    //   475: istore #11
    //   477: aload_0
    //   478: getfield autoGenerateTestcaseScript : Z
    //   481: ifeq -> 612
    //   484: aload_2
    //   485: ifnull -> 543
    //   488: aload #28
    //   490: ifnull -> 538
    //   493: new java/lang/StringBuilder
    //   496: astore_3
    //   497: aload_3
    //   498: invokespecial <init> : ()V
    //   501: aload_3
    //   502: ldc_w '/* '
    //   505: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   508: pop
    //   509: aload_3
    //   510: aload #28
    //   512: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   515: pop
    //   516: aload_3
    //   517: ldc_w ' */ '
    //   520: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   523: pop
    //   524: aload_3
    //   525: aload_2
    //   526: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   529: pop
    //   530: aload_3
    //   531: invokevirtual toString : ()Ljava/lang/String;
    //   534: astore_3
    //   535: goto -> 554
    //   538: aload_2
    //   539: astore_3
    //   540: goto -> 554
    //   543: aload #29
    //   545: iconst_5
    //   546: iload #11
    //   548: iconst_5
    //   549: isub
    //   550: invokestatic toString : ([BII)Ljava/lang/String;
    //   553: astore_3
    //   554: new java/lang/StringBuilder
    //   557: astore #28
    //   559: aload #28
    //   561: aload_3
    //   562: invokevirtual length : ()I
    //   565: bipush #32
    //   567: iadd
    //   568: invokespecial <init> : (I)V
    //   571: aload_0
    //   572: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   575: aload #28
    //   577: invokeinterface generateConnectionCommentBlock : (Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
    //   582: pop
    //   583: aload #28
    //   585: aload_3
    //   586: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   589: pop
    //   590: aload #28
    //   592: bipush #59
    //   594: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   597: pop
    //   598: aload_0
    //   599: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   602: aload #28
    //   604: invokevirtual toString : ()Ljava/lang/String;
    //   607: invokeinterface dumpTestcaseQuery : (Ljava/lang/String;)V
    //   612: aload_0
    //   613: iconst_3
    //   614: aconst_null
    //   615: aload #4
    //   617: iconst_0
    //   618: aconst_null
    //   619: iconst_0
    //   620: invokevirtual sendCommand : (ILjava/lang/String;Lcom/mysql/jdbc/Buffer;ZLjava/lang/String;I)Lcom/mysql/jdbc/Buffer;
    //   623: astore #28
    //   625: aload_0
    //   626: getfield profileSql : Z
    //   629: ifne -> 657
    //   632: aload_0
    //   633: getfield logSlowQueries : Z
    //   636: ifeq -> 642
    //   639: goto -> 657
    //   642: lconst_0
    //   643: lstore #15
    //   645: lconst_0
    //   646: lstore #19
    //   648: iconst_0
    //   649: istore #12
    //   651: aload #31
    //   653: astore_3
    //   654: goto -> 886
    //   657: aload_0
    //   658: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   661: lstore #15
    //   663: aload_0
    //   664: getfield profileSql : Z
    //   667: ifeq -> 687
    //   670: iconst_1
    //   671: istore #12
    //   673: iconst_0
    //   674: istore #14
    //   676: iload #12
    //   678: istore #13
    //   680: iload #14
    //   682: istore #12
    //   684: goto -> 780
    //   687: aload_0
    //   688: getfield logSlowQueries : Z
    //   691: ifeq -> 774
    //   694: lload #15
    //   696: lload #17
    //   698: lsub
    //   699: lstore #19
    //   701: aload_0
    //   702: getfield useAutoSlowLog : Z
    //   705: ifne -> 736
    //   708: lload #19
    //   710: aload_0
    //   711: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   714: invokeinterface getSlowQueryThresholdMillis : ()I
    //   719: i2l
    //   720: lcmp
    //   721: ifle -> 730
    //   724: iconst_1
    //   725: istore #27
    //   727: goto -> 760
    //   730: iconst_0
    //   731: istore #27
    //   733: goto -> 760
    //   736: aload_0
    //   737: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   740: lload #19
    //   742: invokeinterface isAbonormallyLongQuery : (J)Z
    //   747: istore #27
    //   749: aload_0
    //   750: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   753: lload #19
    //   755: invokeinterface reportQueryTime : (J)V
    //   760: iload #27
    //   762: ifeq -> 774
    //   765: iconst_1
    //   766: istore #13
    //   768: iconst_1
    //   769: istore #12
    //   771: goto -> 780
    //   774: iconst_0
    //   775: istore #12
    //   777: goto -> 673
    //   780: aload #30
    //   782: astore_3
    //   783: iload #13
    //   785: ifeq -> 882
    //   788: iload #11
    //   790: aload_0
    //   791: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   794: invokeinterface getMaxQuerySizeToLog : ()I
    //   799: if_icmple -> 821
    //   802: aload_0
    //   803: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   806: invokeinterface getMaxQuerySizeToLog : ()I
    //   811: iconst_5
    //   812: iadd
    //   813: istore #13
    //   815: iconst_1
    //   816: istore #14
    //   818: goto -> 828
    //   821: iload #11
    //   823: istore #13
    //   825: iconst_0
    //   826: istore #14
    //   828: aload #29
    //   830: iconst_5
    //   831: iload #13
    //   833: iconst_5
    //   834: isub
    //   835: invokestatic toString : ([BII)Ljava/lang/String;
    //   838: astore_3
    //   839: iload #14
    //   841: ifeq -> 882
    //   844: new java/lang/StringBuilder
    //   847: astore #29
    //   849: aload #29
    //   851: invokespecial <init> : ()V
    //   854: aload #29
    //   856: aload_3
    //   857: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   860: pop
    //   861: aload #29
    //   863: ldc_w 'MysqlIO.25'
    //   866: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   869: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   872: pop
    //   873: aload #29
    //   875: invokevirtual toString : ()Ljava/lang/String;
    //   878: astore_3
    //   879: goto -> 882
    //   882: lload #15
    //   884: lstore #19
    //   886: aload_0
    //   887: aload_1
    //   888: iload #5
    //   890: iload #6
    //   892: iload #7
    //   894: iload #8
    //   896: aload #9
    //   898: aload #28
    //   900: iconst_0
    //   901: ldc2_w -1
    //   904: aload #10
    //   906: invokevirtual readAllResults : (Lcom/mysql/jdbc/StatementImpl;IIIZLjava/lang/String;Lcom/mysql/jdbc/Buffer;ZJ[Lcom/mysql/jdbc/Field;)Lcom/mysql/jdbc/ResultSetImpl;
    //   909: astore #28
    //   911: iload #12
    //   913: ifeq -> 1251
    //   916: aload_0
    //   917: getfield serverQueryWasSlow : Z
    //   920: ifne -> 1251
    //   923: new java/lang/StringBuilder
    //   926: astore #29
    //   928: aload #29
    //   930: aload_3
    //   931: invokevirtual length : ()I
    //   934: bipush #48
    //   936: iadd
    //   937: invokespecial <init> : (I)V
    //   940: aload_0
    //   941: getfield useAutoSlowLog : Z
    //   944: ifeq -> 955
    //   947: ldc_w ' 95% of all queries '
    //   950: astore #10
    //   952: goto -> 964
    //   955: aload_0
    //   956: getfield slowQueryThreshold : J
    //   959: invokestatic valueOf : (J)Ljava/lang/Long;
    //   962: astore #10
    //   964: aload #10
    //   966: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   969: astore #10
    //   971: aload_0
    //   972: getfield queryTimingUnits : Ljava/lang/String;
    //   975: astore #30
    //   977: lload #15
    //   979: lload #17
    //   981: lsub
    //   982: lstore #25
    //   984: aload #29
    //   986: ldc_w 'MysqlIO.SlowQuery'
    //   989: iconst_3
    //   990: anewarray java/lang/Object
    //   993: dup
    //   994: iconst_0
    //   995: aload #10
    //   997: aastore
    //   998: dup
    //   999: iconst_1
    //   1000: aload #30
    //   1002: aastore
    //   1003: dup
    //   1004: iconst_2
    //   1005: lload #25
    //   1007: invokestatic valueOf : (J)Ljava/lang/Long;
    //   1010: aastore
    //   1011: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1014: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1017: pop
    //   1018: aload #29
    //   1020: aload_3
    //   1021: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1024: pop
    //   1025: aload_0
    //   1026: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1029: invokestatic getInstance : (Lcom/mysql/jdbc/MySQLConnection;)Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1032: astore #30
    //   1034: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1037: astore #31
    //   1039: aload_0
    //   1040: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1043: invokeinterface getId : ()J
    //   1048: lstore #23
    //   1050: aload_1
    //   1051: ifnull -> 1063
    //   1054: aload_1
    //   1055: invokevirtual getId : ()I
    //   1058: istore #5
    //   1060: goto -> 1068
    //   1063: sipush #999
    //   1066: istore #5
    //   1068: aload #28
    //   1070: getfield resultId : I
    //   1073: istore #6
    //   1075: invokestatic currentTimeMillis : ()J
    //   1078: lstore #21
    //   1080: lload #25
    //   1082: l2i
    //   1083: i2l
    //   1084: lstore #25
    //   1086: aload_0
    //   1087: getfield queryTimingUnits : Ljava/lang/String;
    //   1090: astore #10
    //   1092: new java/lang/Throwable
    //   1095: astore #32
    //   1097: aload #32
    //   1099: invokespecial <init> : ()V
    //   1102: aload #31
    //   1104: bipush #6
    //   1106: ldc_w ''
    //   1109: aload #9
    //   1111: lload #23
    //   1113: iload #5
    //   1115: iload #6
    //   1117: lload #21
    //   1119: lload #25
    //   1121: aload #10
    //   1123: aconst_null
    //   1124: aload #32
    //   1126: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1129: aload #29
    //   1131: invokevirtual toString : ()Ljava/lang/String;
    //   1134: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1137: aload #30
    //   1139: aload #31
    //   1141: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1146: aload_0
    //   1147: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1150: invokeinterface getExplainSlowQueries : ()Z
    //   1155: ifeq -> 1251
    //   1158: iload #11
    //   1160: ldc 1048576
    //   1162: if_icmpge -> 1183
    //   1165: aload_0
    //   1166: aload #4
    //   1168: iconst_5
    //   1169: iload #11
    //   1171: iconst_5
    //   1172: isub
    //   1173: invokevirtual getBytes : (II)[B
    //   1176: aload_3
    //   1177: invokevirtual explainSlowQuery : ([BLjava/lang/String;)V
    //   1180: goto -> 1251
    //   1183: aload_0
    //   1184: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1187: invokeinterface getLog : ()Lcom/mysql/jdbc/log/Log;
    //   1192: astore #10
    //   1194: new java/lang/StringBuilder
    //   1197: astore #4
    //   1199: aload #4
    //   1201: invokespecial <init> : ()V
    //   1204: aload #4
    //   1206: ldc_w 'MysqlIO.28'
    //   1209: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1215: pop
    //   1216: aload #4
    //   1218: ldc 1048576
    //   1220: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1223: pop
    //   1224: aload #4
    //   1226: ldc_w 'MysqlIO.29'
    //   1229: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1232: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1235: pop
    //   1236: aload #10
    //   1238: aload #4
    //   1240: invokevirtual toString : ()Ljava/lang/String;
    //   1243: invokeinterface logWarn : (Ljava/lang/Object;)V
    //   1248: goto -> 1251
    //   1251: aload_0
    //   1252: getfield logSlowQueries : Z
    //   1255: ifeq -> 1735
    //   1258: aload_0
    //   1259: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1262: invokestatic getInstance : (Lcom/mysql/jdbc/MySQLConnection;)Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1265: astore #4
    //   1267: aload_0
    //   1268: getfield queryBadIndexUsed : Z
    //   1271: ifeq -> 1423
    //   1274: aload_0
    //   1275: getfield profileSql : Z
    //   1278: ifeq -> 1423
    //   1281: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1284: astore #10
    //   1286: aload_0
    //   1287: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1290: invokeinterface getId : ()J
    //   1295: lstore #23
    //   1297: aload_1
    //   1298: ifnull -> 1310
    //   1301: aload_1
    //   1302: invokevirtual getId : ()I
    //   1305: istore #5
    //   1307: goto -> 1315
    //   1310: sipush #999
    //   1313: istore #5
    //   1315: aload #28
    //   1317: getfield resultId : I
    //   1320: istore #6
    //   1322: invokestatic currentTimeMillis : ()J
    //   1325: lstore #21
    //   1327: aload_0
    //   1328: getfield queryTimingUnits : Ljava/lang/String;
    //   1331: astore #29
    //   1333: new java/lang/Throwable
    //   1336: astore #30
    //   1338: aload #30
    //   1340: invokespecial <init> : ()V
    //   1343: aload #30
    //   1345: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1348: astore #30
    //   1350: new java/lang/StringBuilder
    //   1353: astore #31
    //   1355: aload #31
    //   1357: invokespecial <init> : ()V
    //   1360: aload #31
    //   1362: ldc_w 'MysqlIO.33'
    //   1365: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1368: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1371: pop
    //   1372: aload #31
    //   1374: aload_3
    //   1375: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1378: pop
    //   1379: aload #10
    //   1381: bipush #6
    //   1383: ldc_w ''
    //   1386: aload #9
    //   1388: lload #23
    //   1390: iload #5
    //   1392: iload #6
    //   1394: lload #21
    //   1396: lload #15
    //   1398: lload #17
    //   1400: lsub
    //   1401: aload #29
    //   1403: aconst_null
    //   1404: aload #30
    //   1406: aload #31
    //   1408: invokevirtual toString : ()Ljava/lang/String;
    //   1411: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1414: aload #4
    //   1416: aload #10
    //   1418: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1423: aload_0
    //   1424: getfield queryNoIndexUsed : Z
    //   1427: ifeq -> 1579
    //   1430: aload_0
    //   1431: getfield profileSql : Z
    //   1434: ifeq -> 1579
    //   1437: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1440: astore #10
    //   1442: aload_0
    //   1443: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1446: invokeinterface getId : ()J
    //   1451: lstore #23
    //   1453: aload_1
    //   1454: ifnull -> 1466
    //   1457: aload_1
    //   1458: invokevirtual getId : ()I
    //   1461: istore #5
    //   1463: goto -> 1471
    //   1466: sipush #999
    //   1469: istore #5
    //   1471: aload #28
    //   1473: getfield resultId : I
    //   1476: istore #6
    //   1478: invokestatic currentTimeMillis : ()J
    //   1481: lstore #21
    //   1483: aload_0
    //   1484: getfield queryTimingUnits : Ljava/lang/String;
    //   1487: astore #29
    //   1489: new java/lang/Throwable
    //   1492: astore #30
    //   1494: aload #30
    //   1496: invokespecial <init> : ()V
    //   1499: aload #30
    //   1501: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1504: astore #31
    //   1506: new java/lang/StringBuilder
    //   1509: astore #30
    //   1511: aload #30
    //   1513: invokespecial <init> : ()V
    //   1516: aload #30
    //   1518: ldc_w 'MysqlIO.35'
    //   1521: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1524: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1527: pop
    //   1528: aload #30
    //   1530: aload_3
    //   1531: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1534: pop
    //   1535: aload #10
    //   1537: bipush #6
    //   1539: ldc_w ''
    //   1542: aload #9
    //   1544: lload #23
    //   1546: iload #5
    //   1548: iload #6
    //   1550: lload #21
    //   1552: lload #15
    //   1554: lload #17
    //   1556: lsub
    //   1557: aload #29
    //   1559: aconst_null
    //   1560: aload #31
    //   1562: aload #30
    //   1564: invokevirtual toString : ()Ljava/lang/String;
    //   1567: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1570: aload #4
    //   1572: aload #10
    //   1574: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1579: aload_0
    //   1580: getfield serverQueryWasSlow : Z
    //   1583: ifeq -> 1735
    //   1586: aload_0
    //   1587: getfield profileSql : Z
    //   1590: ifeq -> 1735
    //   1593: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1596: astore #29
    //   1598: aload_0
    //   1599: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1602: invokeinterface getId : ()J
    //   1607: lstore #23
    //   1609: aload_1
    //   1610: ifnull -> 1622
    //   1613: aload_1
    //   1614: invokevirtual getId : ()I
    //   1617: istore #5
    //   1619: goto -> 1627
    //   1622: sipush #999
    //   1625: istore #5
    //   1627: aload #28
    //   1629: getfield resultId : I
    //   1632: istore #6
    //   1634: invokestatic currentTimeMillis : ()J
    //   1637: lstore #21
    //   1639: aload_0
    //   1640: getfield queryTimingUnits : Ljava/lang/String;
    //   1643: astore #10
    //   1645: new java/lang/Throwable
    //   1648: astore #30
    //   1650: aload #30
    //   1652: invokespecial <init> : ()V
    //   1655: aload #30
    //   1657: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1660: astore #30
    //   1662: new java/lang/StringBuilder
    //   1665: astore #31
    //   1667: aload #31
    //   1669: invokespecial <init> : ()V
    //   1672: aload #31
    //   1674: ldc_w 'MysqlIO.ServerSlowQuery'
    //   1677: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   1680: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1683: pop
    //   1684: aload #31
    //   1686: aload_3
    //   1687: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1690: pop
    //   1691: aload #29
    //   1693: bipush #6
    //   1695: ldc_w ''
    //   1698: aload #9
    //   1700: lload #23
    //   1702: iload #5
    //   1704: iload #6
    //   1706: lload #21
    //   1708: lload #15
    //   1710: lload #17
    //   1712: lsub
    //   1713: aload #10
    //   1715: aconst_null
    //   1716: aload #30
    //   1718: aload #31
    //   1720: invokevirtual toString : ()Ljava/lang/String;
    //   1723: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1726: aload #4
    //   1728: aload #29
    //   1730: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1735: aload_0
    //   1736: getfield profileSql : Z
    //   1739: ifeq -> 1962
    //   1742: aload_0
    //   1743: invokevirtual getCurrentTimeNanosOrMillis : ()J
    //   1746: lstore #21
    //   1748: aload_0
    //   1749: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1752: invokestatic getInstance : (Lcom/mysql/jdbc/MySQLConnection;)Lcom/mysql/jdbc/profiler/ProfilerEventHandler;
    //   1755: astore #4
    //   1757: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1760: astore #30
    //   1762: aload_0
    //   1763: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1766: invokeinterface getId : ()J
    //   1771: lstore #23
    //   1773: aload_1
    //   1774: ifnull -> 1786
    //   1777: aload_1
    //   1778: invokevirtual getId : ()I
    //   1781: istore #5
    //   1783: goto -> 1791
    //   1786: sipush #999
    //   1789: istore #5
    //   1791: aload #28
    //   1793: getfield resultId : I
    //   1796: istore #6
    //   1798: invokestatic currentTimeMillis : ()J
    //   1801: lstore #25
    //   1803: aload_0
    //   1804: getfield queryTimingUnits : Ljava/lang/String;
    //   1807: astore #29
    //   1809: new java/lang/Throwable
    //   1812: astore #10
    //   1814: aload #10
    //   1816: invokespecial <init> : ()V
    //   1819: aload #30
    //   1821: iconst_3
    //   1822: ldc_w ''
    //   1825: aload #9
    //   1827: lload #23
    //   1829: iload #5
    //   1831: iload #6
    //   1833: lload #25
    //   1835: lload #15
    //   1837: lload #17
    //   1839: lsub
    //   1840: aload #29
    //   1842: aconst_null
    //   1843: aload #10
    //   1845: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1848: aload_3
    //   1849: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1852: aload #4
    //   1854: aload #30
    //   1856: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1861: new com/mysql/jdbc/profiler/ProfilerEvent
    //   1864: astore #10
    //   1866: aload_0
    //   1867: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1870: invokeinterface getId : ()J
    //   1875: lstore #15
    //   1877: aload_1
    //   1878: ifnull -> 1890
    //   1881: aload_1
    //   1882: invokevirtual getId : ()I
    //   1885: istore #5
    //   1887: goto -> 1895
    //   1890: sipush #999
    //   1893: istore #5
    //   1895: aload #28
    //   1897: getfield resultId : I
    //   1900: istore #6
    //   1902: invokestatic currentTimeMillis : ()J
    //   1905: lstore #17
    //   1907: aload_0
    //   1908: getfield queryTimingUnits : Ljava/lang/String;
    //   1911: astore #29
    //   1913: new java/lang/Throwable
    //   1916: astore_3
    //   1917: aload_3
    //   1918: invokespecial <init> : ()V
    //   1921: aload #10
    //   1923: iconst_5
    //   1924: ldc_w ''
    //   1927: aload #9
    //   1929: lload #15
    //   1931: iload #5
    //   1933: iload #6
    //   1935: lload #17
    //   1937: lload #21
    //   1939: lload #19
    //   1941: lsub
    //   1942: aload #29
    //   1944: aconst_null
    //   1945: aload_3
    //   1946: invokestatic findCallingClassAndMethod : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   1949: aconst_null
    //   1950: invokespecial <init> : (BLjava/lang/String;Ljava/lang/String;JIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   1953: aload #4
    //   1955: aload #10
    //   1957: invokeinterface consumeEvent : (Lcom/mysql/jdbc/profiler/ProfilerEvent;)V
    //   1962: aload_0
    //   1963: getfield hadWarnings : Z
    //   1966: ifeq -> 1973
    //   1969: aload_0
    //   1970: invokevirtual scanForAndThrowDataTruncation : ()V
    //   1973: aload #28
    //   1975: astore_3
    //   1976: aload_0
    //   1977: getfield statementInterceptors : Ljava/util/List;
    //   1980: ifnull -> 2006
    //   1983: aload_0
    //   1984: aload_2
    //   1985: aload_1
    //   1986: aload #28
    //   1988: iconst_0
    //   1989: aconst_null
    //   1990: invokevirtual invokeStatementInterceptorsPost : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Lcom/mysql/jdbc/ResultSetInternalMethods;ZLjava/sql/SQLException;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   1993: astore #4
    //   1995: aload #28
    //   1997: astore_3
    //   1998: aload #4
    //   2000: ifnull -> 2006
    //   2003: aload #4
    //   2005: astore_3
    //   2006: aload_0
    //   2007: aload_0
    //   2008: getfield statementExecutionDepth : I
    //   2011: iconst_1
    //   2012: isub
    //   2013: putfield statementExecutionDepth : I
    //   2016: aload_3
    //   2017: areturn
    //   2018: astore_1
    //   2019: goto -> 2105
    //   2022: astore #4
    //   2024: aload_0
    //   2025: getfield statementInterceptors : Ljava/util/List;
    //   2028: ifnull -> 2042
    //   2031: aload_0
    //   2032: aload_2
    //   2033: aload_1
    //   2034: aconst_null
    //   2035: iconst_0
    //   2036: aload #4
    //   2038: invokevirtual invokeStatementInterceptorsPost : (Ljava/lang/String;Lcom/mysql/jdbc/Statement;Lcom/mysql/jdbc/ResultSetInternalMethods;ZLjava/sql/SQLException;)Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   2041: pop
    //   2042: aload_1
    //   2043: ifnull -> 2102
    //   2046: aload_1
    //   2047: getfield cancelTimeoutMutex : Ljava/lang/Object;
    //   2050: astore_3
    //   2051: aload_3
    //   2052: monitorenter
    //   2053: aload_1
    //   2054: getfield wasCancelled : Z
    //   2057: ifeq -> 2092
    //   2060: aload_1
    //   2061: getfield wasCancelledByTimeout : Z
    //   2064: ifeq -> 2078
    //   2067: new com/mysql/jdbc/exceptions/MySQLTimeoutException
    //   2070: astore_2
    //   2071: aload_2
    //   2072: invokespecial <init> : ()V
    //   2075: goto -> 2086
    //   2078: new com/mysql/jdbc/exceptions/MySQLStatementCancelledException
    //   2081: dup
    //   2082: invokespecial <init> : ()V
    //   2085: astore_2
    //   2086: aload_1
    //   2087: invokevirtual resetCancelledState : ()V
    //   2090: aload_2
    //   2091: athrow
    //   2092: aload_3
    //   2093: monitorexit
    //   2094: goto -> 2102
    //   2097: astore_1
    //   2098: aload_3
    //   2099: monitorexit
    //   2100: aload_1
    //   2101: athrow
    //   2102: aload #4
    //   2104: athrow
    //   2105: aload_0
    //   2106: aload_0
    //   2107: getfield statementExecutionDepth : I
    //   2110: iconst_1
    //   2111: isub
    //   2112: putfield statementExecutionDepth : I
    //   2115: aload_1
    //   2116: athrow
    // Exception table:
    //   from	to	target	type
    //   10	26	2022	java/sql/SQLException
    //   10	26	2018	finally
    //   44	55	2022	java/sql/SQLException
    //   44	55	2018	finally
    //   59	81	2022	java/sql/SQLException
    //   59	81	2018	finally
    //   86	120	2022	java/sql/SQLException
    //   86	120	2018	finally
    //   128	164	2022	java/sql/SQLException
    //   128	164	2018	finally
    //   174	186	2022	java/sql/SQLException
    //   174	186	2018	finally
    //   191	233	2022	java/sql/SQLException
    //   191	233	2018	finally
    //   239	245	2022	java/sql/SQLException
    //   239	245	2018	finally
    //   250	268	2022	java/sql/SQLException
    //   250	268	2018	finally
    //   271	276	2022	java/sql/SQLException
    //   271	276	2018	finally
    //   276	284	2022	java/sql/SQLException
    //   276	284	2018	finally
    //   289	318	2022	java/sql/SQLException
    //   289	318	2018	finally
    //   322	360	2022	java/sql/SQLException
    //   322	360	2018	finally
    //   363	384	2022	java/sql/SQLException
    //   363	384	2018	finally
    //   387	418	2022	java/sql/SQLException
    //   387	418	2018	finally
    //   421	429	2022	java/sql/SQLException
    //   421	429	2018	finally
    //   429	435	2022	java/sql/SQLException
    //   429	435	2018	finally
    //   438	465	2022	java/sql/SQLException
    //   438	465	2018	finally
    //   477	484	2022	java/sql/SQLException
    //   477	484	2018	finally
    //   493	535	2022	java/sql/SQLException
    //   493	535	2018	finally
    //   543	554	2022	java/sql/SQLException
    //   543	554	2018	finally
    //   554	612	2022	java/sql/SQLException
    //   554	612	2018	finally
    //   612	639	2022	java/sql/SQLException
    //   612	639	2018	finally
    //   657	670	2022	java/sql/SQLException
    //   657	670	2018	finally
    //   687	694	2022	java/sql/SQLException
    //   687	694	2018	finally
    //   701	724	2022	java/sql/SQLException
    //   701	724	2018	finally
    //   736	760	2022	java/sql/SQLException
    //   736	760	2018	finally
    //   788	815	2022	java/sql/SQLException
    //   788	815	2018	finally
    //   828	839	2022	java/sql/SQLException
    //   828	839	2018	finally
    //   844	879	2022	java/sql/SQLException
    //   844	879	2018	finally
    //   886	911	2022	java/sql/SQLException
    //   886	911	2018	finally
    //   916	947	2022	java/sql/SQLException
    //   916	947	2018	finally
    //   955	964	2022	java/sql/SQLException
    //   955	964	2018	finally
    //   964	977	2022	java/sql/SQLException
    //   964	977	2018	finally
    //   984	1050	2022	java/sql/SQLException
    //   984	1050	2018	finally
    //   1054	1060	2022	java/sql/SQLException
    //   1054	1060	2018	finally
    //   1068	1080	2022	java/sql/SQLException
    //   1068	1080	2018	finally
    //   1086	1158	2022	java/sql/SQLException
    //   1086	1158	2018	finally
    //   1165	1180	2022	java/sql/SQLException
    //   1165	1180	2018	finally
    //   1183	1248	2022	java/sql/SQLException
    //   1183	1248	2018	finally
    //   1251	1297	2022	java/sql/SQLException
    //   1251	1297	2018	finally
    //   1301	1307	2022	java/sql/SQLException
    //   1301	1307	2018	finally
    //   1315	1423	2022	java/sql/SQLException
    //   1315	1423	2018	finally
    //   1423	1453	2022	java/sql/SQLException
    //   1423	1453	2018	finally
    //   1457	1463	2022	java/sql/SQLException
    //   1457	1463	2018	finally
    //   1471	1579	2022	java/sql/SQLException
    //   1471	1579	2018	finally
    //   1579	1609	2022	java/sql/SQLException
    //   1579	1609	2018	finally
    //   1613	1619	2022	java/sql/SQLException
    //   1613	1619	2018	finally
    //   1627	1735	2022	java/sql/SQLException
    //   1627	1735	2018	finally
    //   1735	1773	2022	java/sql/SQLException
    //   1735	1773	2018	finally
    //   1777	1783	2022	java/sql/SQLException
    //   1777	1783	2018	finally
    //   1791	1877	2022	java/sql/SQLException
    //   1791	1877	2018	finally
    //   1881	1887	2022	java/sql/SQLException
    //   1881	1887	2018	finally
    //   1895	1962	2022	java/sql/SQLException
    //   1895	1962	2018	finally
    //   1962	1973	2022	java/sql/SQLException
    //   1962	1973	2018	finally
    //   1976	1995	2022	java/sql/SQLException
    //   1976	1995	2018	finally
    //   2024	2042	2018	finally
    //   2046	2053	2018	finally
    //   2053	2075	2097	finally
    //   2078	2086	2097	finally
    //   2086	2092	2097	finally
    //   2092	2094	2097	finally
    //   2098	2100	2097	finally
    //   2100	2102	2018	finally
    //   2102	2105	2018	finally
  }
  
  public boolean tackOnMoreStreamingResults(ResultSetImpl paramResultSetImpl) throws SQLException {
    if ((this.serverStatus & 0x8) != 0) {
      ResultSetImpl resultSetImpl = paramResultSetImpl;
      int i = 1;
      for (boolean bool = true; i && (bool || !resultSetImpl.reallyResult()); bool = false) {
        Buffer buffer = checkErrorPacket();
        buffer.setPosition(0);
        Statement statement = paramResultSetImpl.getStatement();
        i = statement.getMaxRows();
        ResultSetImpl resultSetImpl1 = readResultsForQueryOrUpdate((StatementImpl)statement, i, statement.getResultSetType(), statement.getResultSetConcurrency(), true, statement.getConnection().getCatalog(), buffer, paramResultSetImpl.isBinaryEncoded, -1L, null);
        resultSetImpl.setNextResultSet(resultSetImpl1);
        if ((this.serverStatus & 0x8) != 0) {
          i = 1;
        } else {
          i = 0;
        } 
        if (!resultSetImpl1.reallyResult() && i == 0)
          return false; 
        resultSetImpl = resultSetImpl1;
      } 
      return true;
    } 
    return false;
  }
  
  public final Field unpackField(Buffer paramBuffer, boolean paramBoolean) throws SQLException {
    if (this.use41Extensions) {
      byte b;
      long l;
      if (this.has41NewNewProt)
        adjustStartForFieldLength(paramBuffer.getPosition() + 1, paramBuffer.fastSkipLenString()); 
      int i4 = paramBuffer.getPosition();
      int i5 = paramBuffer.fastSkipLenString();
      int i7 = adjustStartForFieldLength(i4 + 1, i5);
      i4 = paramBuffer.getPosition();
      int i6 = paramBuffer.fastSkipLenString();
      int i8 = adjustStartForFieldLength(i4 + 1, i6);
      i4 = paramBuffer.getPosition();
      int i9 = paramBuffer.fastSkipLenString();
      int i10 = adjustStartForFieldLength(i4 + 1, i9);
      i4 = paramBuffer.getPosition();
      int i11 = paramBuffer.fastSkipLenString();
      int i13 = adjustStartForFieldLength(i4 + 1, i11);
      i4 = paramBuffer.getPosition();
      int i12 = paramBuffer.fastSkipLenString();
      int i14 = adjustStartForFieldLength(i4 + 1, i12);
      paramBuffer.readByte();
      short s2 = (short)paramBuffer.readInt();
      if (this.has41NewNewProt) {
        l = paramBuffer.readLong();
      } else {
        l = paramBuffer.readLongInt();
      } 
      byte b1 = paramBuffer.readByte();
      if (this.hasLongColumnInfo) {
        i4 = paramBuffer.readInt();
      } else {
        i4 = paramBuffer.readByte() & 0xFF;
      } 
      short s1 = (short)i4;
      byte b2 = paramBuffer.readByte();
      if (paramBoolean) {
        b = paramBuffer.getPosition();
        i4 = paramBuffer.fastSkipLenString();
        b++;
      } else {
        b = -1;
        i4 = -1;
      } 
      return new Field(this.connection, paramBuffer.getByteBuffer(), i7, i5, i8, i6, i10, i9, i13, i11, i14, i12, l, b1 & 0xFF, s1, b2 & 0xFF, b, i4, s2);
    } 
    int i = paramBuffer.getPosition();
    int k = paramBuffer.fastSkipLenString();
    int n = adjustStartForFieldLength(i + 1, k);
    i = paramBuffer.getPosition();
    int m = paramBuffer.fastSkipLenString();
    int i3 = adjustStartForFieldLength(i + 1, m);
    int i1 = paramBuffer.readnBytes();
    int i2 = paramBuffer.readnBytes();
    paramBuffer.readByte();
    if (this.hasLongColumnInfo) {
      i = paramBuffer.readInt();
    } else {
      i = paramBuffer.readByte() & 0xFF;
    } 
    short s = (short)i;
    int j = paramBuffer.readByte() & 0xFF;
    i = j;
    if (this.colDecimalNeedsBump)
      i = j + 1; 
    return new Field(this.connection, paramBuffer.getByteBuffer(), i3, m, n, k, i1, i2, s, i);
  }
  
  public boolean useNanosForElapsedTime() {
    return this.useNanosForElapsedTime;
  }
  
  public boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3) {
    int i = getServerMajorVersion();
    boolean bool = false;
    if (i >= paramInt1) {
      if (getServerMajorVersion() == paramInt1) {
        if (getServerMinorVersion() >= paramInt2) {
          if (getServerMinorVersion() == paramInt2) {
            if (getServerSubMinorVersion() >= paramInt3)
              bool = true; 
            return bool;
          } 
          return true;
        } 
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  static {
    Exception exception;
    OutputStreamWriter outputStreamWriter;
  }
  
  public static final int AUTH_411_OVERHEAD = 33;
  
  private static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORD = 4194304;
  
  private static final int CLIENT_COMPRESS = 32;
  
  private static final int CLIENT_CONNECT_ATTRS = 1048576;
  
  public static final int CLIENT_CONNECT_WITH_DB = 8;
  
  private static final int CLIENT_DEPRECATE_EOF = 16777216;
  
  private static final int CLIENT_FOUND_ROWS = 2;
  
  private static final int CLIENT_INTERACTIVE = 1024;
  
  private static final int CLIENT_LOCAL_FILES = 128;
  
  private static final int CLIENT_LONG_FLAG = 4;
  
  private static final int CLIENT_LONG_PASSWORD = 1;
  
  private static final int CLIENT_MULTI_RESULTS = 131072;
  
  private static final int CLIENT_MULTI_STATEMENTS = 65536;
  
  private static final int CLIENT_PLUGIN_AUTH = 524288;
  
  private static final int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 2097152;
  
  private static final int CLIENT_PROTOCOL_41 = 512;
  
  public static final int CLIENT_RESERVED = 16384;
  
  public static final int CLIENT_SECURE_CONNECTION = 32768;
  
  private static final int CLIENT_SESSION_TRACK = 8388608;
  
  public static final int CLIENT_SSL = 2048;
  
  private static final int CLIENT_TRANSACTIONS = 8192;
  
  private static final String CODE_PAGE_1252 = "Cp1252";
  
  public static final int COMP_HEADER_LENGTH = 3;
  
  private static final String EXPLAINABLE_STATEMENT = "SELECT";
  
  private static final String[] EXPLAINABLE_STATEMENT_EXTENSION = new String[] { "INSERT", "UPDATE", "REPLACE", "DELETE" };
  
  private static final String FALSE_SCRAMBLE = "xxxxxxxx";
  
  public static final int HEADER_LENGTH = 4;
  
  public static final int INITIAL_PACKET_SIZE = 1024;
  
  private static final int MAX_PACKET_DUMP_LENGTH = 1024;
  
  public static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
  
  public static final int MAX_QUERY_SIZE_TO_LOG = 1024;
  
  public static final int MIN_COMPRESS_LEN = 50;
  
  private static final String NONE = "none";
  
  public static final int NULL_LENGTH = -1;
  
  public static final int SEED_LENGTH = 20;
  
  public static final int SERVER_MORE_RESULTS_EXISTS = 8;
  
  private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
  
  private static final int SERVER_QUERY_NO_INDEX_USED = 32;
  
  private static final int SERVER_QUERY_WAS_SLOW = 2048;
  
  private static final int SERVER_STATUS_AUTOCOMMIT = 2;
  
  private static final int SERVER_STATUS_CURSOR_EXISTS = 64;
  
  private static final int SERVER_STATUS_IN_TRANS = 1;
  
  public static final String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";
  
  public static final String ZERO_DATE_VALUE_MARKER = "0000-00-00";
  
  private static String jvmPlatformCharset;
  
  private static int maxBufferSize = 65535;
  
  private int authPluginDataLength;
  
  private Map<String, AuthenticationPlugin> authenticationPlugins;
  
  private boolean autoGenerateTestcaseScript;
  
  private boolean checkPacketSequence;
  
  private String clientDefaultAuthenticationPlugin;
  
  private String clientDefaultAuthenticationPluginName;
  
  public long clientParam;
  
  private boolean colDecimalNeedsBump;
  
  private int commandCount;
  
  private SoftReference<Buffer> compressBufRef;
  
  private byte compressedPacketSequence;
  
  public MySQLConnection connection;
  
  private Deflater deflater;
  
  private List<String> disabledAuthenticationPlugins;
  
  private boolean enablePacketDebug;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private boolean hadWarnings;
  
  private boolean has41NewNewProt;
  
  private boolean hasLongColumnInfo;
  
  public String host;
  
  private boolean isInteractiveClient;
  
  public long lastPacketReceivedTimeMs;
  
  public long lastPacketSentTimeMs;
  
  private SoftReference<Buffer> loadFileBufRef;
  
  private boolean logSlowQueries;
  
  private int maxAllowedPacket;
  
  public int maxThreeBytes;
  
  public Socket mysqlConnection;
  
  public InputStream mysqlInput;
  
  public BufferedOutputStream mysqlOutput;
  
  private boolean needToGrabQueryFromPacket;
  
  private int oldServerStatus;
  
  private LinkedList<StringBuilder> packetDebugRingBuffer;
  
  private byte[] packetHeaderBuf;
  
  private byte packetSequence;
  
  private boolean packetSequenceReset;
  
  private boolean platformDbCharsetMatches;
  
  public int port;
  
  private boolean profileSql;
  
  private byte protocolVersion;
  
  private boolean queryBadIndexUsed;
  
  private boolean queryNoIndexUsed;
  
  private String queryTimingUnits;
  
  private byte readPacketSequence;
  
  private Buffer reusablePacket;
  
  public String seed;
  
  private Buffer sendPacket;
  
  public int serverCapabilities;
  
  public int serverCharsetIndex;
  
  private String serverDefaultAuthenticationPluginName;
  
  private int serverMajorVersion;
  
  private int serverMinorVersion;
  
  private boolean serverQueryWasSlow;
  
  private int serverStatus;
  
  private int serverSubMinorVersion;
  
  private String serverVersion;
  
  private Buffer sharedSendPacket;
  
  private long slowQueryThreshold;
  
  public SocketFactory socketFactory;
  
  private String socketFactoryClassName;
  
  private SoftReference<Buffer> splitBufRef;
  
  private int statementExecutionDepth;
  
  private List<StatementInterceptorV2> statementInterceptors;
  
  private RowData streamingData;
  
  private long threadId;
  
  private boolean traceProtocol;
  
  private boolean use41Extensions;
  
  private boolean useAutoSlowLog;
  
  private int useBufferRowSizeThreshold;
  
  private boolean useCompression;
  
  private boolean useConnectWithDb;
  
  private boolean useDirectRowUnpack;
  
  private boolean useNanosForElapsedTime;
  
  private boolean useNewLargePackets;
  
  private boolean useNewUpdateCounts;
  
  private int warningCount;
}
