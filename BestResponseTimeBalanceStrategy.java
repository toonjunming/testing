package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class BestResponseTimeBalanceStrategy implements BalanceStrategy {
  public void destroy() {}
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {}
  
  public ConnectionImpl pickConnection(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOflong, int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getGlobalBlacklist : ()Ljava/util/Map;
    //   4: astore #17
    //   6: aconst_null
    //   7: astore #16
    //   9: iconst_0
    //   10: istore #8
    //   12: iload #8
    //   14: iload #5
    //   16: if_icmpge -> 274
    //   19: ldc2_w 9223372036854775807
    //   22: lstore #12
    //   24: aload #17
    //   26: astore #16
    //   28: aload #17
    //   30: invokeinterface size : ()I
    //   35: aload_2
    //   36: invokeinterface size : ()I
    //   41: if_icmpne -> 50
    //   44: aload_1
    //   45: invokevirtual getGlobalBlacklist : ()Ljava/util/Map;
    //   48: astore #16
    //   50: iconst_0
    //   51: istore #6
    //   53: iconst_0
    //   54: istore #7
    //   56: iload #6
    //   58: aload #4
    //   60: arraylength
    //   61: if_icmpge -> 145
    //   64: aload #4
    //   66: iload #6
    //   68: laload
    //   69: lstore #14
    //   71: lload #12
    //   73: lstore #10
    //   75: iload #7
    //   77: istore #9
    //   79: lload #14
    //   81: lload #12
    //   83: lcmp
    //   84: ifge -> 131
    //   87: lload #12
    //   89: lstore #10
    //   91: iload #7
    //   93: istore #9
    //   95: aload #16
    //   97: aload_2
    //   98: iload #6
    //   100: invokeinterface get : (I)Ljava/lang/Object;
    //   105: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   110: ifne -> 131
    //   113: lload #14
    //   115: lconst_0
    //   116: lcmp
    //   117: ifne -> 123
    //   120: goto -> 149
    //   123: iload #6
    //   125: istore #9
    //   127: lload #14
    //   129: lstore #10
    //   131: iinc #6, 1
    //   134: lload #10
    //   136: lstore #12
    //   138: iload #9
    //   140: istore #7
    //   142: goto -> 56
    //   145: iload #7
    //   147: istore #6
    //   149: aload_2
    //   150: iload #6
    //   152: invokeinterface get : (I)Ljava/lang/Object;
    //   157: checkcast java/lang/String
    //   160: astore #19
    //   162: aload_3
    //   163: aload #19
    //   165: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   170: checkcast com/mysql/jdbc/ConnectionImpl
    //   173: astore #18
    //   175: aload #18
    //   177: astore #17
    //   179: aload #18
    //   181: ifnonnull -> 271
    //   184: aload_1
    //   185: aload #19
    //   187: invokevirtual createConnectionForHost : (Ljava/lang/String;)Lcom/mysql/jdbc/ConnectionImpl;
    //   190: astore #17
    //   192: goto -> 271
    //   195: astore #18
    //   197: aload_1
    //   198: aload #18
    //   200: invokevirtual shouldExceptionTriggerConnectionSwitch : (Ljava/lang/Throwable;)Z
    //   203: ifeq -> 268
    //   206: aload_1
    //   207: aload #19
    //   209: invokevirtual addToGlobalBlacklist : (Ljava/lang/String;)V
    //   212: aload #16
    //   214: aload #19
    //   216: aconst_null
    //   217: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   222: pop
    //   223: aload #16
    //   225: invokeinterface size : ()I
    //   230: aload_2
    //   231: invokeinterface size : ()I
    //   236: if_icmpne -> 257
    //   239: iinc #8, 1
    //   242: ldc2_w 250
    //   245: invokestatic sleep : (J)V
    //   248: aload_1
    //   249: invokevirtual getGlobalBlacklist : ()Ljava/util/Map;
    //   252: astore #17
    //   254: goto -> 261
    //   257: aload #16
    //   259: astore #17
    //   261: aload #18
    //   263: astore #16
    //   265: goto -> 12
    //   268: aload #18
    //   270: athrow
    //   271: aload #17
    //   273: areturn
    //   274: aload #16
    //   276: ifnonnull -> 281
    //   279: aconst_null
    //   280: areturn
    //   281: aload #16
    //   283: athrow
    //   284: astore #16
    //   286: goto -> 248
    // Exception table:
    //   from	to	target	type
    //   184	192	195	java/sql/SQLException
    //   242	248	284	java/lang/InterruptedException
  }
}
