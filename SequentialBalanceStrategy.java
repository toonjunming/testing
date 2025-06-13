package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SequentialBalanceStrategy implements BalanceStrategy {
  private int currentHostIndex = -1;
  
  public void destroy() {}
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {}
  
  public ConnectionImpl pickConnection(LoadBalancedConnectionProxy paramLoadBalancedConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOflong, int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_2
    //   1: invokeinterface size : ()I
    //   6: istore #11
    //   8: aload_1
    //   9: invokevirtual getGlobalBlacklist : ()Ljava/util/Map;
    //   12: astore #4
    //   14: aconst_null
    //   15: astore #12
    //   17: iload #5
    //   19: ifle -> 419
    //   22: iconst_0
    //   23: istore #9
    //   25: iconst_0
    //   26: istore #7
    //   28: iconst_1
    //   29: istore #8
    //   31: iload #11
    //   33: iconst_1
    //   34: if_icmpne -> 45
    //   37: aload_0
    //   38: iconst_0
    //   39: putfield currentHostIndex : I
    //   42: goto -> 339
    //   45: aload_0
    //   46: getfield currentHostIndex : I
    //   49: istore #10
    //   51: iload #10
    //   53: istore #6
    //   55: iload #10
    //   57: iconst_m1
    //   58: if_icmpne -> 206
    //   61: invokestatic random : ()D
    //   64: iload #11
    //   66: i2d
    //   67: dmul
    //   68: invokestatic floor : (D)D
    //   71: d2i
    //   72: istore #8
    //   74: iload #8
    //   76: istore #6
    //   78: iload #6
    //   80: iload #11
    //   82: if_icmpge -> 118
    //   85: aload #4
    //   87: aload_2
    //   88: iload #6
    //   90: invokeinterface get : (I)Ljava/lang/Object;
    //   95: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   100: ifne -> 112
    //   103: aload_0
    //   104: iload #6
    //   106: putfield currentHostIndex : I
    //   109: goto -> 118
    //   112: iinc #6, 1
    //   115: goto -> 78
    //   118: aload_0
    //   119: getfield currentHostIndex : I
    //   122: iconst_m1
    //   123: if_icmpne -> 170
    //   126: iload #7
    //   128: istore #6
    //   130: iload #6
    //   132: iload #8
    //   134: if_icmpge -> 170
    //   137: aload #4
    //   139: aload_2
    //   140: iload #6
    //   142: invokeinterface get : (I)Ljava/lang/Object;
    //   147: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   152: ifne -> 164
    //   155: aload_0
    //   156: iload #6
    //   158: putfield currentHostIndex : I
    //   161: goto -> 170
    //   164: iinc #6, 1
    //   167: goto -> 130
    //   170: aload_0
    //   171: getfield currentHostIndex : I
    //   174: iconst_m1
    //   175: if_icmpne -> 339
    //   178: aload_1
    //   179: invokevirtual getGlobalBlacklist : ()Ljava/util/Map;
    //   182: astore #13
    //   184: aload #13
    //   186: astore #4
    //   188: ldc2_w 250
    //   191: invokestatic sleep : (J)V
    //   194: aload #13
    //   196: astore #4
    //   198: goto -> 17
    //   201: astore #13
    //   203: goto -> 17
    //   206: iload #6
    //   208: iconst_1
    //   209: iadd
    //   210: istore #7
    //   212: iload #7
    //   214: iload #11
    //   216: if_icmpge -> 253
    //   219: iload #7
    //   221: istore #6
    //   223: aload #4
    //   225: aload_2
    //   226: iload #7
    //   228: invokeinterface get : (I)Ljava/lang/Object;
    //   233: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   238: ifne -> 206
    //   241: aload_0
    //   242: iload #7
    //   244: putfield currentHostIndex : I
    //   247: iconst_1
    //   248: istore #6
    //   250: goto -> 256
    //   253: iconst_0
    //   254: istore #6
    //   256: iload #6
    //   258: ifne -> 311
    //   261: iload #9
    //   263: istore #7
    //   265: iload #7
    //   267: aload_0
    //   268: getfield currentHostIndex : I
    //   271: if_icmpge -> 311
    //   274: aload #4
    //   276: aload_2
    //   277: iload #7
    //   279: invokeinterface get : (I)Ljava/lang/Object;
    //   284: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   289: ifne -> 305
    //   292: aload_0
    //   293: iload #7
    //   295: putfield currentHostIndex : I
    //   298: iload #8
    //   300: istore #6
    //   302: goto -> 311
    //   305: iinc #7, 1
    //   308: goto -> 265
    //   311: iload #6
    //   313: ifne -> 339
    //   316: aload_1
    //   317: invokevirtual getGlobalBlacklist : ()Ljava/util/Map;
    //   320: astore #13
    //   322: aload #13
    //   324: astore #4
    //   326: ldc2_w 250
    //   329: invokestatic sleep : (J)V
    //   332: aload #13
    //   334: astore #4
    //   336: goto -> 17
    //   339: aload_2
    //   340: aload_0
    //   341: getfield currentHostIndex : I
    //   344: invokeinterface get : (I)Ljava/lang/Object;
    //   349: checkcast java/lang/String
    //   352: astore #14
    //   354: aload_3
    //   355: aload #14
    //   357: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   362: checkcast com/mysql/jdbc/ConnectionImpl
    //   365: astore #13
    //   367: aload #13
    //   369: astore #12
    //   371: aload #13
    //   373: ifnonnull -> 416
    //   376: aload_1
    //   377: aload #14
    //   379: invokevirtual createConnectionForHost : (Ljava/lang/String;)Lcom/mysql/jdbc/ConnectionImpl;
    //   382: astore #12
    //   384: goto -> 416
    //   387: astore #12
    //   389: aload_1
    //   390: aload #12
    //   392: invokevirtual shouldExceptionTriggerConnectionSwitch : (Ljava/lang/Throwable;)Z
    //   395: ifeq -> 413
    //   398: aload_1
    //   399: aload #14
    //   401: invokevirtual addToGlobalBlacklist : (Ljava/lang/String;)V
    //   404: ldc2_w 250
    //   407: invokestatic sleep : (J)V
    //   410: goto -> 17
    //   413: aload #12
    //   415: athrow
    //   416: aload #12
    //   418: areturn
    //   419: aload #12
    //   421: ifnonnull -> 426
    //   424: aconst_null
    //   425: areturn
    //   426: aload #12
    //   428: athrow
    //   429: astore #13
    //   431: goto -> 410
    // Exception table:
    //   from	to	target	type
    //   188	194	201	java/lang/InterruptedException
    //   326	332	201	java/lang/InterruptedException
    //   376	384	387	java/sql/SQLException
    //   404	410	429	java/lang/InterruptedException
  }
}
