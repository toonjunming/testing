package com.mysql.jdbc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class TimeUtil {
  private static final TimeZone DEFAULT_TIMEZONE;
  
  public static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
  
  private static final String TIME_ZONE_MAPPINGS_RESOURCE = "/com/mysql/jdbc/TimeZoneMapping.properties";
  
  public static final Method systemNanoTimeMethod;
  
  private static Properties timeZoneMappings;
  
  static {
    DEFAULT_TIMEZONE = TimeZone.getDefault();
    Method method = null;
    timeZoneMappings = null;
    try {
      Method method1 = System.class.getMethod("nanoTime", null);
      method = method1;
    } catch (SecurityException|NoSuchMethodException securityException) {}
    systemNanoTimeMethod = method;
  }
  
  public static Timestamp adjustTimestampNanosPrecision(Timestamp paramTimestamp, int paramInt, boolean paramBoolean) throws SQLException {
    if (paramInt >= 0 && paramInt <= 6) {
      paramTimestamp = (Timestamp)paramTimestamp.clone();
      int i = paramTimestamp.getNanos();
      double d = Math.pow(10.0D, (9 - paramInt));
      if (paramBoolean) {
        i = (int)Math.round(i / d) * (int)d;
        paramInt = i;
        if (i > 999999999) {
          paramInt = i % 1000000000;
          paramTimestamp.setTime(paramTimestamp.getTime() + 1000L);
        } 
      } else {
        paramInt = (int)(i / d);
        paramInt = (int)d * paramInt;
      } 
      paramTimestamp.setNanos(paramInt);
      return paramTimestamp;
    } 
    throw SQLError.createSQLException("fsp value must be in 0 to 6 range.", "S1009", null);
  }
  
  public static Time changeTimezone(MySQLConnection paramMySQLConnection, Calendar paramCalendar1, Calendar paramCalendar2, Time paramTime, TimeZone paramTimeZone1, TimeZone paramTimeZone2, boolean paramBoolean) {
    if (paramMySQLConnection != null) {
      Calendar calendar;
      if (paramMySQLConnection.getUseTimezone() && !paramMySQLConnection.getNoTimezoneConversionForTimeType()) {
        calendar = Calendar.getInstance(paramTimeZone1);
        calendar.setTime(paramTime);
        int j = calendar.get(15);
        int i = calendar.get(16);
        calendar = Calendar.getInstance(paramTimeZone2);
        calendar.setTime(paramTime);
        i = j + i - calendar.get(15) + calendar.get(16);
        long l = calendar.getTime().getTime();
        if (paramBoolean) {
          l += i;
        } else {
          l -= i;
        } 
        return new Time(l);
      } 
      if (calendar.getUseJDBCCompliantTimezoneShift() && paramCalendar2 != null)
        return new Time(jdbcCompliantZoneShift(paramCalendar1, paramCalendar2, paramTime)); 
    } 
    return paramTime;
  }
  
  public static Timestamp changeTimezone(MySQLConnection paramMySQLConnection, Calendar paramCalendar1, Calendar paramCalendar2, Timestamp paramTimestamp, TimeZone paramTimeZone1, TimeZone paramTimeZone2, boolean paramBoolean) {
    if (paramMySQLConnection != null) {
      Calendar calendar;
      if (paramMySQLConnection.getUseTimezone()) {
        calendar = Calendar.getInstance(paramTimeZone1);
        calendar.setTime(paramTimestamp);
        int i = calendar.get(15);
        int j = calendar.get(16);
        calendar = Calendar.getInstance(paramTimeZone2);
        calendar.setTime(paramTimestamp);
        i = i + j - calendar.get(15) + calendar.get(16);
        long l = calendar.getTime().getTime();
        if (paramBoolean) {
          l += i;
        } else {
          l -= i;
        } 
        return new Timestamp(l);
      } 
      if (calendar.getUseJDBCCompliantTimezoneShift() && paramCalendar2 != null) {
        Timestamp timestamp = new Timestamp(jdbcCompliantZoneShift(paramCalendar1, paramCalendar2, paramTimestamp));
        timestamp.setNanos(paramTimestamp.getNanos());
        return timestamp;
      } 
    } 
    return paramTimestamp;
  }
  
  public static final Date fastDateCreate(int paramInt1, int paramInt2, int paramInt3, Calendar paramCalendar) {
    // Byte code:
    //   0: aload_3
    //   1: astore #6
    //   3: aload_3
    //   4: ifnonnull -> 16
    //   7: new java/util/GregorianCalendar
    //   10: dup
    //   11: invokespecial <init> : ()V
    //   14: astore #6
    //   16: aload #6
    //   18: monitorenter
    //   19: aload #6
    //   21: invokevirtual getTime : ()Ljava/util/Date;
    //   24: astore_3
    //   25: aload #6
    //   27: invokevirtual clear : ()V
    //   30: aload #6
    //   32: iload_0
    //   33: iload_1
    //   34: iconst_1
    //   35: isub
    //   36: iload_2
    //   37: iconst_0
    //   38: iconst_0
    //   39: iconst_0
    //   40: invokevirtual set : (IIIIII)V
    //   43: aload #6
    //   45: bipush #14
    //   47: iconst_0
    //   48: invokevirtual set : (II)V
    //   51: aload #6
    //   53: invokevirtual getTimeInMillis : ()J
    //   56: lstore #4
    //   58: new java/sql/Date
    //   61: astore #7
    //   63: aload #7
    //   65: lload #4
    //   67: invokespecial <init> : (J)V
    //   70: aload #6
    //   72: aload_3
    //   73: invokevirtual setTime : (Ljava/util/Date;)V
    //   76: aload #6
    //   78: monitorexit
    //   79: aload #7
    //   81: areturn
    //   82: astore #7
    //   84: aload #6
    //   86: aload_3
    //   87: invokevirtual setTime : (Ljava/util/Date;)V
    //   90: aload #7
    //   92: athrow
    //   93: astore_3
    //   94: aload #6
    //   96: monitorexit
    //   97: aload_3
    //   98: athrow
    // Exception table:
    //   from	to	target	type
    //   19	25	93	finally
    //   25	70	82	finally
    //   70	79	93	finally
    //   84	93	93	finally
    //   94	97	93	finally
  }
  
  public static final Date fastDateCreate(boolean paramBoolean, Calendar paramCalendar1, Calendar paramCalendar2, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: iload_0
    //   1: ifeq -> 19
    //   4: aload_1
    //   5: astore_2
    //   6: aload_1
    //   7: ifnonnull -> 19
    //   10: ldc 'GMT'
    //   12: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   15: invokestatic getInstance : (Ljava/util/TimeZone;)Ljava/util/Calendar;
    //   18: astore_2
    //   19: aload_2
    //   20: monitorenter
    //   21: aload_2
    //   22: invokevirtual getTime : ()Ljava/util/Date;
    //   25: astore_1
    //   26: aload_2
    //   27: invokevirtual clear : ()V
    //   30: aload_2
    //   31: bipush #14
    //   33: iconst_0
    //   34: invokevirtual set : (II)V
    //   37: aload_2
    //   38: iload_3
    //   39: iload #4
    //   41: iconst_1
    //   42: isub
    //   43: iload #5
    //   45: iconst_0
    //   46: iconst_0
    //   47: iconst_0
    //   48: invokevirtual set : (IIIIII)V
    //   51: aload_2
    //   52: invokevirtual getTimeInMillis : ()J
    //   55: lstore #6
    //   57: new java/sql/Date
    //   60: astore #8
    //   62: aload #8
    //   64: lload #6
    //   66: invokespecial <init> : (J)V
    //   69: aload_2
    //   70: aload_1
    //   71: invokevirtual setTime : (Ljava/util/Date;)V
    //   74: aload_2
    //   75: monitorexit
    //   76: aload #8
    //   78: areturn
    //   79: astore #8
    //   81: aload_2
    //   82: aload_1
    //   83: invokevirtual setTime : (Ljava/util/Date;)V
    //   86: aload #8
    //   88: athrow
    //   89: astore_1
    //   90: aload_2
    //   91: monitorexit
    //   92: aload_1
    //   93: athrow
    // Exception table:
    //   from	to	target	type
    //   21	26	89	finally
    //   26	69	79	finally
    //   69	76	89	finally
    //   81	89	89	finally
    //   90	92	89	finally
  }
  
  public static final Time fastTimeCreate(int paramInt1, int paramInt2, int paramInt3, Calendar paramCalendar, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    // Byte code:
    //   0: iload_0
    //   1: iflt -> 237
    //   4: iload_0
    //   5: bipush #23
    //   7: if_icmpgt -> 237
    //   10: iload_1
    //   11: iflt -> 179
    //   14: iload_1
    //   15: bipush #59
    //   17: if_icmpgt -> 179
    //   20: iload_2
    //   21: iflt -> 121
    //   24: iload_2
    //   25: bipush #59
    //   27: if_icmpgt -> 121
    //   30: aload_3
    //   31: astore #4
    //   33: aload_3
    //   34: ifnonnull -> 46
    //   37: new java/util/GregorianCalendar
    //   40: dup
    //   41: invokespecial <init> : ()V
    //   44: astore #4
    //   46: aload #4
    //   48: monitorenter
    //   49: aload #4
    //   51: invokevirtual getTime : ()Ljava/util/Date;
    //   54: astore_3
    //   55: aload #4
    //   57: invokevirtual clear : ()V
    //   60: aload #4
    //   62: sipush #1970
    //   65: iconst_0
    //   66: iconst_1
    //   67: iload_0
    //   68: iload_1
    //   69: iload_2
    //   70: invokevirtual set : (IIIIII)V
    //   73: aload #4
    //   75: invokevirtual getTimeInMillis : ()J
    //   78: lstore #5
    //   80: new java/sql/Time
    //   83: astore #7
    //   85: aload #7
    //   87: lload #5
    //   89: invokespecial <init> : (J)V
    //   92: aload #4
    //   94: aload_3
    //   95: invokevirtual setTime : (Ljava/util/Date;)V
    //   98: aload #4
    //   100: monitorexit
    //   101: aload #7
    //   103: areturn
    //   104: astore #7
    //   106: aload #4
    //   108: aload_3
    //   109: invokevirtual setTime : (Ljava/util/Date;)V
    //   112: aload #7
    //   114: athrow
    //   115: astore_3
    //   116: aload #4
    //   118: monitorexit
    //   119: aload_3
    //   120: athrow
    //   121: new java/lang/StringBuilder
    //   124: dup
    //   125: invokespecial <init> : ()V
    //   128: astore_3
    //   129: aload_3
    //   130: ldc 'Illegal minute value ''
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_3
    //   137: iload_2
    //   138: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_3
    //   143: ldc '' for java.sql.Time type in value ''
    //   145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: aload_3
    //   150: iload_0
    //   151: iload_1
    //   152: iload_2
    //   153: invokestatic timeFormattedString : (III)Ljava/lang/String;
    //   156: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: aload_3
    //   161: ldc '.'
    //   163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: pop
    //   167: aload_3
    //   168: invokevirtual toString : ()Ljava/lang/String;
    //   171: ldc 'S1009'
    //   173: aload #4
    //   175: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   178: athrow
    //   179: new java/lang/StringBuilder
    //   182: dup
    //   183: invokespecial <init> : ()V
    //   186: astore_3
    //   187: aload_3
    //   188: ldc 'Illegal minute value ''
    //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload_3
    //   195: iload_1
    //   196: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   199: pop
    //   200: aload_3
    //   201: ldc '' for java.sql.Time type in value ''
    //   203: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: pop
    //   207: aload_3
    //   208: iload_0
    //   209: iload_1
    //   210: iload_2
    //   211: invokestatic timeFormattedString : (III)Ljava/lang/String;
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload_3
    //   219: ldc '.'
    //   221: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload_3
    //   226: invokevirtual toString : ()Ljava/lang/String;
    //   229: ldc 'S1009'
    //   231: aload #4
    //   233: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   236: athrow
    //   237: new java/lang/StringBuilder
    //   240: dup
    //   241: invokespecial <init> : ()V
    //   244: astore_3
    //   245: aload_3
    //   246: ldc 'Illegal hour value ''
    //   248: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: aload_3
    //   253: iload_0
    //   254: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   257: pop
    //   258: aload_3
    //   259: ldc '' for java.sql.Time type in value ''
    //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: pop
    //   265: aload_3
    //   266: iload_0
    //   267: iload_1
    //   268: iload_2
    //   269: invokestatic timeFormattedString : (III)Ljava/lang/String;
    //   272: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   275: pop
    //   276: aload_3
    //   277: ldc '.'
    //   279: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: pop
    //   283: aload_3
    //   284: invokevirtual toString : ()Ljava/lang/String;
    //   287: ldc 'S1009'
    //   289: aload #4
    //   291: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   294: athrow
    // Exception table:
    //   from	to	target	type
    //   49	55	115	finally
    //   55	92	104	finally
    //   92	101	115	finally
    //   106	115	115	finally
    //   116	119	115	finally
  }
  
  public static final Time fastTimeCreate(Calendar paramCalendar, int paramInt1, int paramInt2, int paramInt3, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    // Byte code:
    //   0: iload_1
    //   1: iflt -> 217
    //   4: iload_1
    //   5: bipush #24
    //   7: if_icmpgt -> 217
    //   10: iload_2
    //   11: iflt -> 159
    //   14: iload_2
    //   15: bipush #59
    //   17: if_icmpgt -> 159
    //   20: iload_3
    //   21: iflt -> 101
    //   24: iload_3
    //   25: bipush #59
    //   27: if_icmpgt -> 101
    //   30: aload_0
    //   31: monitorenter
    //   32: aload_0
    //   33: invokevirtual getTime : ()Ljava/util/Date;
    //   36: astore #4
    //   38: aload_0
    //   39: invokevirtual clear : ()V
    //   42: aload_0
    //   43: sipush #1970
    //   46: iconst_0
    //   47: iconst_1
    //   48: iload_1
    //   49: iload_2
    //   50: iload_3
    //   51: invokevirtual set : (IIIIII)V
    //   54: aload_0
    //   55: invokevirtual getTimeInMillis : ()J
    //   58: lstore #5
    //   60: new java/sql/Time
    //   63: astore #7
    //   65: aload #7
    //   67: lload #5
    //   69: invokespecial <init> : (J)V
    //   72: aload_0
    //   73: aload #4
    //   75: invokevirtual setTime : (Ljava/util/Date;)V
    //   78: aload_0
    //   79: monitorexit
    //   80: aload #7
    //   82: areturn
    //   83: astore #7
    //   85: aload_0
    //   86: aload #4
    //   88: invokevirtual setTime : (Ljava/util/Date;)V
    //   91: aload #7
    //   93: athrow
    //   94: astore #4
    //   96: aload_0
    //   97: monitorexit
    //   98: aload #4
    //   100: athrow
    //   101: new java/lang/StringBuilder
    //   104: dup
    //   105: invokespecial <init> : ()V
    //   108: astore_0
    //   109: aload_0
    //   110: ldc 'Illegal minute value ''
    //   112: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload_0
    //   117: iload_3
    //   118: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload_0
    //   123: ldc '' for java.sql.Time type in value ''
    //   125: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_0
    //   130: iload_1
    //   131: iload_2
    //   132: iload_3
    //   133: invokestatic timeFormattedString : (III)Ljava/lang/String;
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: aload_0
    //   141: ldc '.'
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload_0
    //   148: invokevirtual toString : ()Ljava/lang/String;
    //   151: ldc 'S1009'
    //   153: aload #4
    //   155: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   158: athrow
    //   159: new java/lang/StringBuilder
    //   162: dup
    //   163: invokespecial <init> : ()V
    //   166: astore_0
    //   167: aload_0
    //   168: ldc 'Illegal minute value ''
    //   170: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: aload_0
    //   175: iload_2
    //   176: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload_0
    //   181: ldc '' for java.sql.Time type in value ''
    //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_0
    //   188: iload_1
    //   189: iload_2
    //   190: iload_3
    //   191: invokestatic timeFormattedString : (III)Ljava/lang/String;
    //   194: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: pop
    //   198: aload_0
    //   199: ldc '.'
    //   201: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: pop
    //   205: aload_0
    //   206: invokevirtual toString : ()Ljava/lang/String;
    //   209: ldc 'S1009'
    //   211: aload #4
    //   213: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   216: athrow
    //   217: new java/lang/StringBuilder
    //   220: dup
    //   221: invokespecial <init> : ()V
    //   224: astore_0
    //   225: aload_0
    //   226: ldc 'Illegal hour value ''
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload_0
    //   233: iload_1
    //   234: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   237: pop
    //   238: aload_0
    //   239: ldc '' for java.sql.Time type in value ''
    //   241: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload_0
    //   246: iload_1
    //   247: iload_2
    //   248: iload_3
    //   249: invokestatic timeFormattedString : (III)Ljava/lang/String;
    //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: aload_0
    //   257: ldc '.'
    //   259: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: pop
    //   263: aload_0
    //   264: invokevirtual toString : ()Ljava/lang/String;
    //   267: ldc 'S1009'
    //   269: aload #4
    //   271: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   274: athrow
    // Exception table:
    //   from	to	target	type
    //   32	38	94	finally
    //   38	72	83	finally
    //   72	80	94	finally
    //   85	94	94	finally
    //   96	98	94	finally
  }
  
  public static final Timestamp fastTimestampCreate(TimeZone paramTimeZone, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
    GregorianCalendar gregorianCalendar;
    if (paramTimeZone == null) {
      gregorianCalendar = new GregorianCalendar();
    } else {
      gregorianCalendar = new GregorianCalendar((TimeZone)gregorianCalendar);
    } 
    gregorianCalendar.clear();
    gregorianCalendar.set(paramInt1, paramInt2 - 1, paramInt3, paramInt4, paramInt5, paramInt6);
    Timestamp timestamp = new Timestamp(gregorianCalendar.getTimeInMillis());
    timestamp.setNanos(paramInt7);
    return timestamp;
  }
  
  public static final Timestamp fastTimestampCreate(boolean paramBoolean, Calendar paramCalendar1, Calendar paramCalendar2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
    // Byte code:
    //   0: aload_2
    //   1: monitorenter
    //   2: aload_2
    //   3: invokevirtual getTime : ()Ljava/util/Date;
    //   6: astore #12
    //   8: aload_2
    //   9: invokevirtual clear : ()V
    //   12: aload_2
    //   13: iload_3
    //   14: iload #4
    //   16: iconst_1
    //   17: isub
    //   18: iload #5
    //   20: iload #6
    //   22: iload #7
    //   24: iload #8
    //   26: invokevirtual set : (IIIIII)V
    //   29: iconst_0
    //   30: istore_3
    //   31: iload_0
    //   32: ifeq -> 97
    //   35: aload_2
    //   36: bipush #15
    //   38: invokevirtual get : (I)I
    //   41: istore #4
    //   43: aload_2
    //   44: bipush #16
    //   46: invokevirtual get : (I)I
    //   49: istore_3
    //   50: aload_1
    //   51: ifnonnull -> 66
    //   54: ldc 'GMT'
    //   56: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   59: invokestatic getInstance : (Ljava/util/TimeZone;)Ljava/util/Calendar;
    //   62: astore_1
    //   63: goto -> 66
    //   66: aload_1
    //   67: invokevirtual clear : ()V
    //   70: aload_1
    //   71: aload_2
    //   72: invokevirtual getTimeInMillis : ()J
    //   75: invokevirtual setTimeInMillis : (J)V
    //   78: iload #4
    //   80: iload_3
    //   81: iadd
    //   82: aload_1
    //   83: bipush #15
    //   85: invokevirtual get : (I)I
    //   88: aload_1
    //   89: bipush #16
    //   91: invokevirtual get : (I)I
    //   94: iadd
    //   95: isub
    //   96: istore_3
    //   97: iload #9
    //   99: ifeq -> 113
    //   102: aload_2
    //   103: bipush #14
    //   105: iload #9
    //   107: ldc 1000000
    //   109: idiv
    //   110: invokevirtual set : (II)V
    //   113: aload_2
    //   114: invokevirtual getTimeInMillis : ()J
    //   117: lstore #10
    //   119: new java/sql/Timestamp
    //   122: astore_1
    //   123: aload_1
    //   124: lload #10
    //   126: iload_3
    //   127: i2l
    //   128: ladd
    //   129: invokespecial <init> : (J)V
    //   132: aload_1
    //   133: iload #9
    //   135: invokevirtual setNanos : (I)V
    //   138: aload_2
    //   139: aload #12
    //   141: invokevirtual setTime : (Ljava/util/Date;)V
    //   144: aload_2
    //   145: monitorexit
    //   146: aload_1
    //   147: areturn
    //   148: astore_1
    //   149: aload_2
    //   150: aload #12
    //   152: invokevirtual setTime : (Ljava/util/Date;)V
    //   155: aload_1
    //   156: athrow
    //   157: astore_1
    //   158: aload_2
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	157	finally
    //   8	29	148	finally
    //   35	50	148	finally
    //   54	63	148	finally
    //   66	97	148	finally
    //   102	113	148	finally
    //   113	138	148	finally
    //   138	146	157	finally
    //   149	157	157	finally
    //   158	160	157	finally
  }
  
  public static String formatNanos(int paramInt1, boolean paramBoolean, int paramInt2) throws SQLException {
    if (paramInt1 >= 0 && paramInt1 <= 999999999) {
      if (paramInt2 >= 0 && paramInt2 <= 6) {
        if (!paramBoolean || paramInt2 == 0 || paramInt1 == 0)
          return "0"; 
        paramInt1 = (int)(paramInt1 / Math.pow(10.0D, (9 - paramInt2)));
        if (paramInt1 == 0)
          return "0"; 
        String str = Integer.toString(paramInt1);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("000000000".substring(0, paramInt2 - str.length()));
        stringBuilder2.append(str);
        str = stringBuilder2.toString();
        for (paramInt1 = paramInt2 - 1; str.charAt(paramInt1) == '0'; paramInt1--);
        return str.substring(0, paramInt1 + 1);
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("fsp value must be in 0 to 6 range but was ");
      stringBuilder1.append(paramInt2);
      throw SQLError.createSQLException(stringBuilder1.toString(), "S1009", null);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("nanos value must be in 0 to 999999999 range but was ");
    stringBuilder.append(paramInt1);
    throw SQLError.createSQLException(stringBuilder.toString(), "S1009", null);
  }
  
  public static String getCanonicalTimezone(String paramString, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: aload_0
    //   7: invokevirtual trim : ()Ljava/lang/String;
    //   10: astore_0
    //   11: aload_0
    //   12: invokevirtual length : ()I
    //   15: iconst_2
    //   16: if_icmple -> 76
    //   19: aload_0
    //   20: iconst_0
    //   21: invokevirtual charAt : (I)C
    //   24: bipush #43
    //   26: if_icmpeq -> 39
    //   29: aload_0
    //   30: iconst_0
    //   31: invokevirtual charAt : (I)C
    //   34: bipush #45
    //   36: if_icmpne -> 76
    //   39: aload_0
    //   40: iconst_1
    //   41: invokevirtual charAt : (I)C
    //   44: invokestatic isDigit : (C)Z
    //   47: ifeq -> 76
    //   50: new java/lang/StringBuilder
    //   53: dup
    //   54: invokespecial <init> : ()V
    //   57: astore_1
    //   58: aload_1
    //   59: ldc 'GMT'
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: pop
    //   65: aload_1
    //   66: aload_0
    //   67: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: pop
    //   71: aload_1
    //   72: invokevirtual toString : ()Ljava/lang/String;
    //   75: areturn
    //   76: ldc com/mysql/jdbc/TimeUtil
    //   78: monitorenter
    //   79: getstatic com/mysql/jdbc/TimeUtil.timeZoneMappings : Ljava/util/Properties;
    //   82: ifnonnull -> 89
    //   85: aload_1
    //   86: invokestatic loadTimeZoneMappings : (Lcom/mysql/jdbc/ExceptionInterceptor;)V
    //   89: ldc com/mysql/jdbc/TimeUtil
    //   91: monitorexit
    //   92: getstatic com/mysql/jdbc/TimeUtil.timeZoneMappings : Ljava/util/Properties;
    //   95: aload_0
    //   96: invokevirtual getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   99: astore_2
    //   100: aload_2
    //   101: ifnull -> 106
    //   104: aload_2
    //   105: areturn
    //   106: ldc_w 'TimeUtil.UnrecognizedTimezoneId'
    //   109: iconst_1
    //   110: anewarray java/lang/Object
    //   113: dup
    //   114: iconst_0
    //   115: aload_0
    //   116: aastore
    //   117: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   120: ldc_w '01S00'
    //   123: aload_1
    //   124: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   127: athrow
    //   128: astore_0
    //   129: ldc com/mysql/jdbc/TimeUtil
    //   131: monitorexit
    //   132: aload_0
    //   133: athrow
    // Exception table:
    //   from	to	target	type
    //   79	89	128	finally
    //   89	92	128	finally
    //   129	132	128	finally
  }
  
  public static long getCurrentTimeNanosOrMillis() {
    Method method = systemNanoTimeMethod;
    if (method != null)
      try {
        return ((Long)method.invoke(null, null)).longValue();
      } catch (IllegalArgumentException|IllegalAccessException|java.lang.reflect.InvocationTargetException illegalArgumentException) {} 
    return System.currentTimeMillis();
  }
  
  public static final TimeZone getDefaultTimeZone(boolean paramBoolean) {
    TimeZone timeZone;
    if (paramBoolean) {
      timeZone = DEFAULT_TIMEZONE;
    } else {
      timeZone = TimeZone.getDefault();
    } 
    return (TimeZone)timeZone.clone();
  }
  
  public static SimpleDateFormat getSimpleDateFormat(SimpleDateFormat paramSimpleDateFormat, String paramString, Calendar paramCalendar, TimeZone paramTimeZone) {
    if (paramSimpleDateFormat == null)
      paramSimpleDateFormat = new SimpleDateFormat(paramString, Locale.US); 
    if (paramCalendar != null)
      paramSimpleDateFormat.setCalendar((Calendar)paramCalendar.clone()); 
    if (paramTimeZone != null)
      paramSimpleDateFormat.setTimeZone(paramTimeZone); 
    return paramSimpleDateFormat;
  }
  
  private static long jdbcCompliantZoneShift(Calendar paramCalendar1, Calendar paramCalendar2, Date paramDate) {
    // Byte code:
    //   0: aload_0
    //   1: astore #5
    //   3: aload_0
    //   4: ifnonnull -> 16
    //   7: new java/util/GregorianCalendar
    //   10: dup
    //   11: invokespecial <init> : ()V
    //   14: astore #5
    //   16: aload #5
    //   18: monitorenter
    //   19: aload_1
    //   20: invokevirtual getTime : ()Ljava/util/Date;
    //   23: astore #6
    //   25: aload #5
    //   27: invokevirtual getTime : ()Ljava/util/Date;
    //   30: astore_0
    //   31: aload #5
    //   33: aload_2
    //   34: invokevirtual setTime : (Ljava/util/Date;)V
    //   37: aload_1
    //   38: iconst_1
    //   39: aload #5
    //   41: iconst_1
    //   42: invokevirtual get : (I)I
    //   45: invokevirtual set : (II)V
    //   48: aload_1
    //   49: iconst_2
    //   50: aload #5
    //   52: iconst_2
    //   53: invokevirtual get : (I)I
    //   56: invokevirtual set : (II)V
    //   59: aload_1
    //   60: iconst_5
    //   61: aload #5
    //   63: iconst_5
    //   64: invokevirtual get : (I)I
    //   67: invokevirtual set : (II)V
    //   70: aload_1
    //   71: bipush #11
    //   73: aload #5
    //   75: bipush #11
    //   77: invokevirtual get : (I)I
    //   80: invokevirtual set : (II)V
    //   83: aload_1
    //   84: bipush #12
    //   86: aload #5
    //   88: bipush #12
    //   90: invokevirtual get : (I)I
    //   93: invokevirtual set : (II)V
    //   96: aload_1
    //   97: bipush #13
    //   99: aload #5
    //   101: bipush #13
    //   103: invokevirtual get : (I)I
    //   106: invokevirtual set : (II)V
    //   109: aload_1
    //   110: bipush #14
    //   112: aload #5
    //   114: bipush #14
    //   116: invokevirtual get : (I)I
    //   119: invokevirtual set : (II)V
    //   122: aload_1
    //   123: invokevirtual getTime : ()Ljava/util/Date;
    //   126: invokevirtual getTime : ()J
    //   129: lstore_3
    //   130: aload #5
    //   132: aload_0
    //   133: invokevirtual setTime : (Ljava/util/Date;)V
    //   136: aload_1
    //   137: aload #6
    //   139: invokevirtual setTime : (Ljava/util/Date;)V
    //   142: aload #5
    //   144: monitorexit
    //   145: lload_3
    //   146: lreturn
    //   147: astore_2
    //   148: aload #5
    //   150: aload_0
    //   151: invokevirtual setTime : (Ljava/util/Date;)V
    //   154: aload_1
    //   155: aload #6
    //   157: invokevirtual setTime : (Ljava/util/Date;)V
    //   160: aload_2
    //   161: athrow
    //   162: astore_0
    //   163: aload #5
    //   165: monitorexit
    //   166: aload_0
    //   167: athrow
    // Exception table:
    //   from	to	target	type
    //   19	31	162	finally
    //   31	130	147	finally
    //   130	145	162	finally
    //   148	162	162	finally
    //   163	166	162	finally
  }
  
  private static void loadTimeZoneMappings(ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    Properties properties = new Properties();
    timeZoneMappings = properties;
    try {
      properties.load(TimeUtil.class.getResourceAsStream("/com/mysql/jdbc/TimeZoneMapping.properties"));
      for (String str : TimeZone.getAvailableIDs()) {
        if (!timeZoneMappings.containsKey(str))
          timeZoneMappings.put(str, str); 
      } 
      return;
    } catch (IOException iOException) {
      throw SQLError.createSQLException(Messages.getString("TimeUtil.LoadTimeZoneMappingError"), "01S00", paramExceptionInterceptor);
    } 
  }
  
  public static boolean nanoTimeAvailable() {
    boolean bool;
    if (systemNanoTimeMethod != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static Calendar setProlepticIfNeeded(Calendar paramCalendar1, Calendar paramCalendar2) {
    Calendar calendar = paramCalendar1;
    if (paramCalendar1 != null) {
      calendar = paramCalendar1;
      if (paramCalendar2 != null) {
        calendar = paramCalendar1;
        if (paramCalendar1 instanceof GregorianCalendar) {
          calendar = paramCalendar1;
          if (paramCalendar2 instanceof GregorianCalendar) {
            calendar = paramCalendar1;
            if (((GregorianCalendar)paramCalendar2).getGregorianChange().getTime() == Long.MIN_VALUE) {
              calendar = (GregorianCalendar)paramCalendar1.clone();
              calendar.setGregorianChange(new Date(Long.MIN_VALUE));
              calendar.clear();
            } 
          } 
        } 
      } 
    } 
    return calendar;
  }
  
  private static String timeFormattedString(int paramInt1, int paramInt2, int paramInt3) {
    StringBuilder stringBuilder = new StringBuilder(8);
    if (paramInt1 < 10)
      stringBuilder.append("0"); 
    stringBuilder.append(paramInt1);
    stringBuilder.append(":");
    if (paramInt2 < 10)
      stringBuilder.append("0"); 
    stringBuilder.append(paramInt2);
    stringBuilder.append(":");
    if (paramInt3 < 10)
      stringBuilder.append("0"); 
    stringBuilder.append(paramInt3);
    return stringBuilder.toString();
  }
  
  public static Timestamp truncateFractionalSeconds(Timestamp paramTimestamp) {
    paramTimestamp = new Timestamp(paramTimestamp.getTime());
    paramTimestamp.setNanos(0);
    return paramTimestamp;
  }
}
