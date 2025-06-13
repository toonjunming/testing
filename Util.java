package com.mysql.jdbc;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Util {
  private static final String MYSQL_JDBC_PACKAGE_ROOT;
  
  private static Util enclosingInstance = new Util();
  
  private static final ConcurrentMap<Class<?>, Class<?>[]> implementedInterfacesCache;
  
  private static boolean isColdFusion;
  
  private static boolean isJdbc4;
  
  private static boolean isJdbc42;
  
  private static final ConcurrentMap<Class<?>, Boolean> isJdbcInterfaceCache;
  
  private static int jvmUpdateNumber;
  
  private static int jvmVersion = -1;
  
  static {
    jvmUpdateNumber = -1;
    isColdFusion = false;
    boolean bool = true;
    try {
      Class.forName("java.sql.NClob");
      isJdbc4 = true;
    } catch (ClassNotFoundException classNotFoundException) {
      isJdbc4 = false;
    } 
    try {
      Class.forName("java.sql.JDBCType");
      isJdbc42 = true;
    } finally {
      Exception exception = null;
    } 
    int i = str.indexOf('.');
    int j = i + 1;
    if (i != -1) {
      int k = j;
      while (true) {
        i = k;
        if (Character.isDigit(str.charAt(k))) {
          i = ++k;
          if (k < str.length())
            continue; 
        } 
        break;
      } 
    } else {
      i = j;
    } 
    if (i > j) {
      jvmVersion = Integer.parseInt(str.substring(j, i));
    } else {
      if (isJdbc42) {
        i = 8;
      } else if (isJdbc4) {
        i = 6;
      } else {
        i = 5;
      } 
      jvmVersion = i;
    } 
    i = str.indexOf("_");
    j = i + 1;
    if (i != -1) {
      int k = j;
      while (true) {
        i = k;
        if (Character.isDigit(str.charAt(k))) {
          i = ++k;
          if (k < str.length())
            continue; 
        } 
        break;
      } 
    } else {
      i = j;
    } 
    if (i > j)
      jvmUpdateNumber = Integer.parseInt(str.substring(j, i)); 
    String str = stackTraceToString(new Throwable());
    if (str != null) {
      if (str.indexOf("coldfusion") == -1)
        bool = false; 
      isColdFusion = bool;
    } else {
      isColdFusion = false;
    } 
    isJdbcInterfaceCache = new ConcurrentHashMap<Class<?>, Boolean>();
    str = getPackageName(MultiHostConnectionProxy.class);
    MYSQL_JDBC_PACKAGE_ROOT = str.substring(0, str.indexOf("jdbc") + 4);
    implementedInterfacesCache = (ConcurrentMap)new ConcurrentHashMap<Class<?>, Class<?>>();
  }
  
  public static Map<Object, Object> calculateDifferences(Map<?, ?> paramMap1, Map<?, ?> paramMap2) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    Iterator<Map.Entry> iterator = paramMap1.entrySet().iterator();
    while (true) {
      Number number;
      Object object;
      if (iterator.hasNext()) {
        Number number1;
        Map.Entry entry = iterator.next();
        object = entry.getKey();
        if (entry.getValue() instanceof Number) {
          number = (Number)entry.getValue();
          number1 = (Number)paramMap2.get(object);
        } else {
          try {
            number = new Double();
            this(number1.getValue().toString());
            number1 = new Double(paramMap2.get(object).toString());
            if (number.equals(number1))
              continue; 
            if (number instanceof Byte) {
              hashMap.put(object, Byte.valueOf((byte)(((Byte)number1).byteValue() - ((Byte)number).byteValue())));
              continue;
            } 
            if (number instanceof Short) {
              hashMap.put(object, Short.valueOf((short)(((Short)number1).shortValue() - ((Short)number).shortValue())));
              continue;
            } 
            if (number instanceof Integer) {
              hashMap.put(object, Integer.valueOf(((Integer)number1).intValue() - ((Integer)number).intValue()));
              continue;
            } 
            if (number instanceof Long) {
              hashMap.put(object, Long.valueOf(((Long)number1).longValue() - ((Long)number).longValue()));
              continue;
            } 
            if (number instanceof Float) {
              hashMap.put(object, Float.valueOf(((Float)number1).floatValue() - ((Float)number).floatValue()));
              continue;
            } 
            if (number instanceof Double) {
              hashMap.put(object, Double.valueOf((((Double)number1).shortValue() - number.shortValue())));
              continue;
            } 
            if (number instanceof BigDecimal) {
              hashMap.put(object, ((BigDecimal)number1).subtract((BigDecimal)number));
              continue;
            } 
            if (number instanceof BigInteger)
              hashMap.put(object, ((BigInteger)number1).subtract((BigInteger)number)); 
          } catch (NumberFormatException numberFormatException) {}
          continue;
        } 
      } else {
        break;
      } 
      if (number.equals(numberFormatException))
        continue; 
      if (number instanceof Byte) {
        hashMap.put(object, Byte.valueOf((byte)(((Byte)numberFormatException).byteValue() - ((Byte)number).byteValue())));
        continue;
      } 
      if (number instanceof Short) {
        hashMap.put(object, Short.valueOf((short)(((Short)numberFormatException).shortValue() - ((Short)number).shortValue())));
        continue;
      } 
      if (number instanceof Integer) {
        hashMap.put(object, Integer.valueOf(((Integer)numberFormatException).intValue() - ((Integer)number).intValue()));
        continue;
      } 
      if (number instanceof Long) {
        hashMap.put(object, Long.valueOf(((Long)numberFormatException).longValue() - ((Long)number).longValue()));
        continue;
      } 
      if (number instanceof Float) {
        hashMap.put(object, Float.valueOf(((Float)numberFormatException).floatValue() - ((Float)number).floatValue()));
        continue;
      } 
      if (number instanceof Double) {
        hashMap.put(object, Double.valueOf((((Double)numberFormatException).shortValue() - ((Double)number).shortValue())));
        continue;
      } 
      if (number instanceof BigDecimal) {
        hashMap.put(object, ((BigDecimal)numberFormatException).subtract((BigDecimal)number));
        continue;
      } 
      if (number instanceof BigInteger)
        hashMap.put(object, ((BigInteger)numberFormatException).subtract((BigInteger)number)); 
    } 
    return hashMap;
  }
  
  public static Class<?>[] getImplementedInterfaces(Class<?> paramClass) {
    Class[] arrayOfClass = implementedInterfacesCache.get(paramClass);
    if (arrayOfClass != null)
      return arrayOfClass; 
    LinkedHashSet<? super Class<?>> linkedHashSet = new LinkedHashSet();
    Class<?> clazz = paramClass;
    while (true) {
      Collections.addAll(linkedHashSet, clazz.getInterfaces());
      Class<?> clazz1 = clazz.getSuperclass();
      clazz = clazz1;
      if (clazz1 == null) {
        Class[] arrayOfClass2 = (Class[])linkedHashSet.<Class<?>[]>toArray((Class<?>[][])new Class[linkedHashSet.size()]);
        Class[] arrayOfClass3 = implementedInterfacesCache.putIfAbsent(paramClass, arrayOfClass2);
        Class[] arrayOfClass1 = arrayOfClass2;
        if (arrayOfClass3 != null)
          arrayOfClass1 = arrayOfClass3; 
        return arrayOfClass1;
      } 
    } 
  }
  
  public static Object getInstance(String paramString, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    try {
      return handleNewInstance(Class.forName(paramString).getConstructor(paramArrayOfClass), paramArrayOfObject, paramExceptionInterceptor);
    } catch (SecurityException securityException) {
      throw SQLError.createSQLException("Can't instantiate required class", "S1000", securityException, paramExceptionInterceptor);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw SQLError.createSQLException("Can't instantiate required class", "S1000", noSuchMethodException, paramExceptionInterceptor);
    } catch (ClassNotFoundException classNotFoundException) {
      throw SQLError.createSQLException("Can't instantiate required class", "S1000", classNotFoundException, paramExceptionInterceptor);
    } 
  }
  
  public static int getJVMUpdateNumber() {
    return jvmUpdateNumber;
  }
  
  public static int getJVMVersion() {
    return jvmVersion;
  }
  
  public static String getPackageName(Class<?> paramClass) {
    String str = paramClass.getName();
    int i = str.lastIndexOf('.');
    return (i > 0) ? str.substring(0, i) : "";
  }
  
  public static final Object handleNewInstance(Constructor<?> paramConstructor, Object[] paramArrayOfObject, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    try {
      return paramConstructor.newInstance(paramArrayOfObject);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw SQLError.createSQLException("Can't instantiate required class", "S1000", illegalArgumentException, paramExceptionInterceptor);
    } catch (InstantiationException instantiationException) {
      throw SQLError.createSQLException("Can't instantiate required class", "S1000", instantiationException, paramExceptionInterceptor);
    } catch (IllegalAccessException illegalAccessException) {
      throw SQLError.createSQLException("Can't instantiate required class", "S1000", illegalAccessException, paramExceptionInterceptor);
    } catch (InvocationTargetException invocationTargetException) {
      Throwable throwable = invocationTargetException.getTargetException();
      if (!(throwable instanceof SQLException)) {
        Throwable throwable1 = throwable;
        if (throwable instanceof ExceptionInInitializerError)
          throwable1 = ((ExceptionInInitializerError)throwable).getException(); 
        throw SQLError.createSQLException(throwable1.toString(), "S1000", throwable1, paramExceptionInterceptor);
      } 
      throw (SQLException)throwable;
    } 
  }
  
  public static long[] hashPre41Password(String paramString) {
    return hashPre41Password(paramString, Charset.defaultCharset().name());
  }
  
  public static long[] hashPre41Password(String paramString1, String paramString2) {
    try {
      return newHash(paramString1.replaceAll("\\s", "").getBytes(paramString2));
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return new long[0];
    } 
  }
  
  public static boolean interfaceExists(String paramString) {
    boolean bool = false;
    try {
      Class<?> clazz = Class.forName("java.net.NetworkInterface");
      Object object = clazz.getMethod("getByName", null).invoke(clazz, new Object[] { paramString });
      if (object != null)
        bool = true; 
    } finally {}
    return bool;
  }
  
  public static boolean isColdFusion() {
    return isColdFusion;
  }
  
  public static boolean isCommunityEdition(String paramString) {
    return isEnterpriseEdition(paramString) ^ true;
  }
  
  public static boolean isEnterpriseEdition(String paramString) {
    return (paramString.contains("enterprise") || paramString.contains("commercial") || paramString.contains("advanced"));
  }
  
  public static boolean isJdbc4() {
    return isJdbc4;
  }
  
  public static boolean isJdbc42() {
    return isJdbc42;
  }
  
  public static boolean isJdbcInterface(Class<?> paramClass) {
    ConcurrentMap<Class<?>, Boolean> concurrentMap = isJdbcInterfaceCache;
    if (concurrentMap.containsKey(paramClass))
      return ((Boolean)concurrentMap.get(paramClass)).booleanValue(); 
    if (paramClass.isInterface())
      try {
        if (isJdbcPackage(getPackageName(paramClass))) {
          concurrentMap.putIfAbsent(paramClass, Boolean.TRUE);
          return true;
        } 
      } catch (Exception exception) {} 
    Class[] arrayOfClass = paramClass.getInterfaces();
    int i = arrayOfClass.length;
    for (byte b = 0; b < i; b++) {
      if (isJdbcInterface(arrayOfClass[b])) {
        isJdbcInterfaceCache.putIfAbsent(paramClass, Boolean.TRUE);
        return true;
      } 
    } 
    if (paramClass.getSuperclass() != null && isJdbcInterface(paramClass.getSuperclass())) {
      isJdbcInterfaceCache.putIfAbsent(paramClass, Boolean.TRUE);
      return true;
    } 
    isJdbcInterfaceCache.putIfAbsent(paramClass, Boolean.FALSE);
    return false;
  }
  
  public static boolean isJdbcPackage(String paramString) {
    boolean bool;
    if (paramString != null && (paramString.startsWith("java.sql") || paramString.startsWith("javax.sql") || paramString.startsWith(MYSQL_JDBC_PACKAGE_ROOT))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean jvmMeetsMinimum(int paramInt1, int paramInt2) {
    return (getJVMVersion() > paramInt1 || (getJVMVersion() == paramInt1 && getJVMUpdateNumber() >= paramInt2));
  }
  
  public static List<Extension> loadExtensions(Connection paramConnection, Properties paramProperties, String paramString1, String paramString2, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    LinkedList<Extension> linkedList = new LinkedList();
    List<String> list = StringUtils.split(paramString1, ",", true);
    paramString1 = null;
    String str = null;
    try {
      int i = list.size();
      byte b = 0;
      paramString1 = str;
      while (true) {
        if (b < i) {
          str = list.get(b);
          try {
            Extension extension = (Extension)Class.forName(str).newInstance();
            extension.init(paramConnection, paramProperties);
            linkedList.add(extension);
            b++;
            continue;
          } finally {
            paramConnection = null;
          } 
        } else {
          return linkedList;
        } 
        SQLException sQLException1 = SQLError.createSQLException(Messages.getString(paramString2, new Object[] { paramString1 }), paramExceptionInterceptor);
        sQLException1.initCause((Throwable)paramConnection);
        throw sQLException1;
      } 
    } finally {}
    SQLException sQLException = SQLError.createSQLException(Messages.getString(paramString2, new Object[] { paramString1 }), paramExceptionInterceptor);
    sQLException.initCause((Throwable)paramConnection);
    throw sQLException;
  }
  
  public static String newCrypt(String paramString1, String paramString2, String paramString3) {
    String str1 = paramString1;
    String str2 = str1;
    if (str1 != null)
      if (paramString1.length() == 0) {
        str2 = str1;
      } else {
        long[] arrayOfLong1 = newHash(paramString2.getBytes());
        long[] arrayOfLong2 = hashPre41Password(str1, paramString3);
        boolean bool = false;
        long l2 = (arrayOfLong1[0] ^ arrayOfLong2[0]) % 1073741823L;
        long l1 = (arrayOfLong1[1] ^ arrayOfLong2[1]) % 1073741823L;
        char[] arrayOfChar = new char[paramString2.length()];
        byte b;
        for (b = 0; b < paramString2.length(); b++) {
          l2 = (l2 * 3L + l1) % 1073741823L;
          l1 = (l1 + l2 + 33L) % 1073741823L;
          arrayOfChar[b] = (char)(byte)(int)Math.floor(l2 / 1073741823L * 31.0D + 64.0D);
        } 
        Long.signum(l2);
        l1 = (l2 * 3L + l1) % 1073741823L;
        byte b1 = (byte)(int)Math.floor(l1 / 1073741823L * 31.0D);
        for (b = bool; b < paramString2.length(); b++)
          arrayOfChar[b] = (char)(arrayOfChar[b] ^ (char)b1); 
        str2 = new String(arrayOfChar);
      }  
    return str2;
  }
  
  public static long[] newHash(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    long l3 = 1345345333L;
    long l2 = 7L;
    long l1 = 305419889L;
    for (byte b = 0; b < i; b++) {
      long l = (paramArrayOfbyte[b] & 0xFF);
      l3 ^= ((0x3FL & l3) + l2) * l + (l3 << 8L);
      l1 += l1 << 8L ^ l3;
      l2 += l;
    } 
    return new long[] { l3 & 0x7FFFFFFFL, l1 & 0x7FFFFFFFL };
  }
  
  public static String oldCrypt(String paramString1, String paramString2) {
    if (paramString1 == null || paramString1.length() == 0)
      return paramString1; 
    long l2 = (oldHash(paramString2) ^ oldHash(paramString1)) % 33554431L;
    long l1 = l2 / 2L;
    char[] arrayOfChar = new char[paramString2.length()];
    for (byte b = 0; b < paramString2.length(); b++) {
      l2 = (l2 * 3L + l1) % 33554431L;
      l1 = (l1 + l2 + 33L) % 33554431L;
      arrayOfChar[b] = (char)(byte)(int)Math.floor(l2 / 33554431L * 31.0D + 64.0D);
    } 
    return new String(arrayOfChar);
  }
  
  public static long oldHash(String paramString) {
    long l2 = 1345345333L;
    long l1 = 7L;
    byte b = 0;
    while (b < paramString.length()) {
      long l4 = l2;
      long l3 = l1;
      if (paramString.charAt(b) != ' ')
        if (paramString.charAt(b) == '\t') {
          l4 = l2;
          l3 = l1;
        } else {
          l3 = paramString.charAt(b);
          l4 = l2 ^ ((0x3FL & l2) + l1) * l3 + (l2 << 8L);
          l3 = l1 + l3;
        }  
      b++;
      l2 = l4;
      l1 = l3;
    } 
    return l2 & 0x7FFFFFFFL;
  }
  
  private static RandStructcture randomInit(long paramLong1, long paramLong2) {
    Util util = enclosingInstance;
    util.getClass();
    RandStructcture randStructcture = new RandStructcture();
    randStructcture.maxValue = 1073741823L;
    randStructcture.maxValueDbl = 1073741823L;
    randStructcture.seed1 = paramLong1 % 1073741823L;
    randStructcture.seed2 = paramLong2 % 1073741823L;
    return randStructcture;
  }
  
  public static Object readObject(ResultSet paramResultSet, int paramInt) throws Exception {
    ObjectInputStream objectInputStream = new ObjectInputStream(paramResultSet.getBinaryStream(paramInt));
    Object object = objectInputStream.readObject();
    objectInputStream.close();
    return object;
  }
  
  public static void resultSetToMap(Map<Object, Object> paramMap, ResultSet paramResultSet) throws SQLException {
    while (paramResultSet.next())
      paramMap.put(paramResultSet.getObject(1), paramResultSet.getObject(2)); 
  }
  
  public static void resultSetToMap(Map<Object, Object> paramMap, ResultSet paramResultSet, int paramInt1, int paramInt2) throws SQLException {
    while (paramResultSet.next())
      paramMap.put(paramResultSet.getObject(paramInt1), paramResultSet.getObject(paramInt2)); 
  }
  
  public static void resultSetToMap(Map<Object, Object> paramMap, ResultSet paramResultSet, String paramString1, String paramString2) throws SQLException {
    while (paramResultSet.next())
      paramMap.put(paramResultSet.getObject(paramString1), paramResultSet.getObject(paramString2)); 
  }
  
  private static double rnd(RandStructcture paramRandStructcture) {
    long l3 = paramRandStructcture.seed1;
    long l2 = paramRandStructcture.seed2;
    long l1 = paramRandStructcture.maxValue;
    l3 = (l3 * 3L + l2) % l1;
    paramRandStructcture.seed1 = l3;
    paramRandStructcture.seed2 = (l2 + l3 + 33L) % l1;
    return l3 / paramRandStructcture.maxValueDbl;
  }
  
  public static String scramble(String paramString1, String paramString2) {
    byte[] arrayOfByte = new byte[8];
    boolean bool = false;
    paramString1 = paramString1.substring(0, 8);
    if (paramString2 != null && paramString2.length() > 0) {
      long[] arrayOfLong2 = hashPre41Password(paramString2);
      long[] arrayOfLong1 = newHash(paramString1.getBytes());
      RandStructcture randStructcture = randomInit(arrayOfLong2[0] ^ arrayOfLong1[0], arrayOfLong2[1] ^ arrayOfLong1[1]);
      int i = paramString1.length();
      byte b = 0;
      byte b1 = 0;
      while (b < i) {
        arrayOfByte[b1] = (byte)(int)(Math.floor(rnd(randStructcture) * 31.0D) + 64.0D);
        b1++;
        b++;
      } 
      b1 = (byte)(int)Math.floor(rnd(randStructcture) * 31.0D);
      for (b = bool; b < 8; b++)
        arrayOfByte[b] = (byte)(arrayOfByte[b] ^ b1); 
      paramString1 = StringUtils.toString(arrayOfByte);
    } else {
      paramString1 = "";
    } 
    return paramString1;
  }
  
  public static long secondsSinceMillis(long paramLong) {
    return (System.currentTimeMillis() - paramLong) / 1000L;
  }
  
  public static String stackTraceToString(Throwable paramThrowable) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Messages.getString("Util.1"));
    if (paramThrowable != null) {
      stringBuilder.append(paramThrowable.getClass().getName());
      String str = paramThrowable.getMessage();
      if (str != null) {
        stringBuilder.append(Messages.getString("Util.2"));
        stringBuilder.append(str);
      } 
      StringWriter stringWriter = new StringWriter();
      paramThrowable.printStackTrace(new PrintWriter(stringWriter));
      stringBuilder.append(Messages.getString("Util.3"));
      stringBuilder.append(stringWriter.toString());
    } 
    stringBuilder.append(Messages.getString("Util.4"));
    return stringBuilder.toString();
  }
  
  public static int truncateAndConvertToInt(long paramLong) {
    int i;
    if (paramLong > 2147483647L) {
      i = Integer.MAX_VALUE;
    } else if (paramLong < -2147483648L) {
      i = Integer.MIN_VALUE;
    } else {
      i = (int)paramLong;
    } 
    return i;
  }
  
  public static int[] truncateAndConvertToInt(long[] paramArrayOflong) {
    int[] arrayOfInt = new int[paramArrayOflong.length];
    for (byte b = 0; b < paramArrayOflong.length; b++) {
      int i;
      if (paramArrayOflong[b] > 2147483647L) {
        i = Integer.MAX_VALUE;
      } else if (paramArrayOflong[b] < -2147483648L) {
        i = Integer.MIN_VALUE;
      } else {
        i = (int)paramArrayOflong[b];
      } 
      arrayOfInt[b] = i;
    } 
    return arrayOfInt;
  }
  
  public class RandStructcture {
    public long maxValue;
    
    public double maxValueDbl;
    
    public long seed1;
    
    public long seed2;
    
    public final Util this$0;
  }
}
