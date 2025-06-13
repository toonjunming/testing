package com.mysql.jdbc.log;

import com.mysql.jdbc.Util;
import com.mysql.jdbc.profiler.ProfilerEvent;

public class LogUtils {
  public static final String CALLER_INFORMATION_NOT_AVAILABLE = "Caller information not available";
  
  private static final String LINE_SEPARATOR;
  
  private static final int LINE_SEPARATOR_LENGTH;
  
  static {
    String str = System.getProperty("line.separator");
    LINE_SEPARATOR = str;
    LINE_SEPARATOR_LENGTH = str.length();
  }
  
  public static Object expandProfilerEventIfNecessary(Object paramObject) {
    if (paramObject instanceof ProfilerEvent) {
      StringBuilder stringBuilder = new StringBuilder();
      ProfilerEvent profilerEvent = (ProfilerEvent)paramObject;
      String str = profilerEvent.getEventCreationPointAsString();
      paramObject = str;
      if (str == null)
        paramObject = Util.stackTraceToString(new Throwable()); 
      stringBuilder.append("Profiler Event: [");
      switch (profilerEvent.getEventType()) {
        default:
          stringBuilder.append("UNKNOWN");
          break;
        case 6:
          stringBuilder.append("SLOW QUERY");
          break;
        case 5:
          stringBuilder.append("FETCH");
          break;
        case 4:
          stringBuilder.append("EXECUTE");
          break;
        case 3:
          stringBuilder.append("QUERY");
          break;
        case 2:
          stringBuilder.append("PREPARE");
          break;
        case 1:
          stringBuilder.append("CONSTRUCT");
          break;
        case 0:
          stringBuilder.append("WARN");
          break;
      } 
      stringBuilder.append("] ");
      stringBuilder.append((String)paramObject);
      stringBuilder.append(" duration: ");
      stringBuilder.append(profilerEvent.getEventDuration());
      stringBuilder.append(" ");
      stringBuilder.append(profilerEvent.getDurationUnits());
      stringBuilder.append(", connection-id: ");
      stringBuilder.append(profilerEvent.getConnectionId());
      stringBuilder.append(", statement-id: ");
      stringBuilder.append(profilerEvent.getStatementId());
      stringBuilder.append(", resultset-id: ");
      stringBuilder.append(profilerEvent.getResultSetId());
      paramObject = profilerEvent.getMessage();
      if (paramObject != null) {
        stringBuilder.append(", message: ");
        stringBuilder.append((String)paramObject);
      } 
      return stringBuilder;
    } 
    return paramObject;
  }
  
  public static String findCallingClassAndMethod(Throwable paramThrowable) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic stackTraceToString : (Ljava/lang/Throwable;)Ljava/lang/String;
    //   4: astore #4
    //   6: aload #4
    //   8: ldc 'com.mysql.jdbc'
    //   10: invokevirtual lastIndexOf : (Ljava/lang/String;)I
    //   13: istore_2
    //   14: iload_2
    //   15: iconst_m1
    //   16: if_icmpeq -> 105
    //   19: aload #4
    //   21: ldc 'com.mysql.jdbc.compliance'
    //   23: iload_2
    //   24: invokevirtual indexOf : (Ljava/lang/String;I)I
    //   27: istore_1
    //   28: iload_1
    //   29: iconst_m1
    //   30: if_icmpeq -> 42
    //   33: iload_1
    //   34: getstatic com/mysql/jdbc/log/LogUtils.LINE_SEPARATOR_LENGTH : I
    //   37: isub
    //   38: istore_1
    //   39: goto -> 52
    //   42: aload #4
    //   44: getstatic com/mysql/jdbc/log/LogUtils.LINE_SEPARATOR : Ljava/lang/String;
    //   47: iload_2
    //   48: invokevirtual indexOf : (Ljava/lang/String;I)I
    //   51: istore_1
    //   52: iload_1
    //   53: iconst_m1
    //   54: if_icmpeq -> 105
    //   57: getstatic com/mysql/jdbc/log/LogUtils.LINE_SEPARATOR : Ljava/lang/String;
    //   60: astore_0
    //   61: getstatic com/mysql/jdbc/log/LogUtils.LINE_SEPARATOR_LENGTH : I
    //   64: istore_3
    //   65: aload #4
    //   67: aload_0
    //   68: iload_1
    //   69: iload_3
    //   70: iadd
    //   71: invokevirtual indexOf : (Ljava/lang/String;I)I
    //   74: istore_2
    //   75: iload_2
    //   76: iconst_m1
    //   77: if_icmpeq -> 93
    //   80: aload #4
    //   82: iload_1
    //   83: iload_3
    //   84: iadd
    //   85: iload_2
    //   86: invokevirtual substring : (II)Ljava/lang/String;
    //   89: astore_0
    //   90: goto -> 108
    //   93: aload #4
    //   95: iload_1
    //   96: iload_3
    //   97: iadd
    //   98: invokevirtual substring : (I)Ljava/lang/String;
    //   101: astore_0
    //   102: goto -> 108
    //   105: ldc 'Caller information not available'
    //   107: astore_0
    //   108: aload_0
    //   109: astore #4
    //   111: aload_0
    //   112: ldc '\\tat '
    //   114: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   117: ifne -> 163
    //   120: aload_0
    //   121: astore #4
    //   123: aload_0
    //   124: ldc 'at '
    //   126: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   129: ifne -> 163
    //   132: new java/lang/StringBuilder
    //   135: dup
    //   136: invokespecial <init> : ()V
    //   139: astore #4
    //   141: aload #4
    //   143: ldc 'at '
    //   145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: aload #4
    //   151: aload_0
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload #4
    //   158: invokevirtual toString : ()Ljava/lang/String;
    //   161: astore #4
    //   163: aload #4
    //   165: areturn
  }
}
