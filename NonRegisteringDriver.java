package com.mysql.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class NonRegisteringDriver implements Driver {
  private static final String ALLOWED_QUOTES = "\"'";
  
  public static final String DBNAME_PROPERTY_KEY = "DBNAME";
  
  public static final boolean DEBUG = false;
  
  public static final int HOST_NAME_INDEX = 0;
  
  public static final String HOST_PROPERTY_KEY = "HOST";
  
  public static final String LICENSE = "GPL";
  
  public static final String LOADBALANCE_URL_PREFIX = "jdbc:mysql:loadbalance://";
  
  private static final String MXJ_URL_PREFIX = "jdbc:mysql:mxj://";
  
  public static final String NAME = "MySQL Connector Java";
  
  public static final String NUM_HOSTS_PROPERTY_KEY = "NUM_HOSTS";
  
  public static final String OS;
  
  public static final String PASSWORD_PROPERTY_KEY = "password";
  
  public static final String PATH_PROPERTY_KEY = "PATH";
  
  public static final String PLATFORM;
  
  public static final int PORT_NUMBER_INDEX = 1;
  
  public static final String PORT_PROPERTY_KEY = "PORT";
  
  public static final String PROPERTIES_TRANSFORM_KEY = "propertiesTransform";
  
  public static final String PROTOCOL_PROPERTY_KEY = "PROTOCOL";
  
  private static final String REPLICATION_URL_PREFIX = "jdbc:mysql:replication://";
  
  public static final String RUNTIME_VENDOR;
  
  public static final String RUNTIME_VERSION;
  
  public static final boolean TRACE = false;
  
  private static final String URL_PREFIX = "jdbc:mysql://";
  
  public static final String USER_PROPERTY_KEY = "user";
  
  public static final String USE_CONFIG_PROPERTY_KEY = "useConfigs";
  
  public static final String VERSION = "5.1.47";
  
  public static final ConcurrentHashMap<ConnectionPhantomReference, ConnectionPhantomReference> connectionPhantomRefs = new ConcurrentHashMap<ConnectionPhantomReference, ConnectionPhantomReference>();
  
  public static final ReferenceQueue<ConnectionImpl> refQueue = new ReferenceQueue<ConnectionImpl>();
  
  static {
    OS = getOSName();
    PLATFORM = getPlatform();
    RUNTIME_VENDOR = System.getProperty("java.vendor");
    RUNTIME_VERSION = System.getProperty("java.version");
    try {
      Class.forName(AbandonedConnectionCleanupThread.class.getName());
    } catch (ClassNotFoundException classNotFoundException) {}
  }
  
  private Connection connectFailover(String paramString, Properties paramProperties) throws SQLException {
    Properties properties = parseURL(paramString, paramProperties);
    if (properties == null)
      return null; 
    properties.remove("roundRobinLoadBalance");
    int i = Integer.parseInt(properties.getProperty("NUM_HOSTS"));
    ArrayList<String> arrayList = new ArrayList();
    byte b = 0;
    while (b < i) {
      b++;
      StringBuilder stringBuilder1 = new StringBuilder();
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("HOST.");
      stringBuilder2.append(b);
      stringBuilder1.append(properties.getProperty(stringBuilder2.toString()));
      stringBuilder1.append(":");
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("PORT.");
      stringBuilder2.append(b);
      stringBuilder1.append(properties.getProperty(stringBuilder2.toString()));
      arrayList.add(stringBuilder1.toString());
    } 
    return FailoverConnectionProxy.createProxyInstance(arrayList, properties);
  }
  
  private Connection connectLoadBalanced(String paramString, Properties paramProperties) throws SQLException {
    Properties properties = parseURL(paramString, paramProperties);
    if (properties == null)
      return null; 
    properties.remove("roundRobinLoadBalance");
    int i = Integer.parseInt(properties.getProperty("NUM_HOSTS"));
    ArrayList<String> arrayList = new ArrayList();
    byte b = 0;
    while (b < i) {
      b++;
      StringBuilder stringBuilder1 = new StringBuilder();
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("HOST.");
      stringBuilder2.append(b);
      stringBuilder1.append(properties.getProperty(stringBuilder2.toString()));
      stringBuilder1.append(":");
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("PORT.");
      stringBuilder2.append(b);
      stringBuilder1.append(properties.getProperty(stringBuilder2.toString()));
      arrayList.add(stringBuilder1.toString());
    } 
    return LoadBalancedConnectionProxy.createProxyInstance(arrayList, properties);
  }
  
  public static Properties expandHostKeyValues(String paramString) {
    // Byte code:
    //   0: new java/util/Properties
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aload_0
    //   9: invokestatic isHostPropertiesList : (Ljava/lang/String;)Z
    //   12: ifeq -> 286
    //   15: aload_0
    //   16: bipush #9
    //   18: invokevirtual substring : (I)Ljava/lang/String;
    //   21: ldc ')'
    //   23: ldc ''"'
    //   25: ldc ''"'
    //   27: iconst_1
    //   28: invokestatic split : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
    //   31: invokeinterface iterator : ()Ljava/util/Iterator;
    //   36: astore #4
    //   38: aload #4
    //   40: invokeinterface hasNext : ()Z
    //   45: ifeq -> 286
    //   48: aload #4
    //   50: invokeinterface next : ()Ljava/lang/Object;
    //   55: checkcast java/lang/String
    //   58: astore_1
    //   59: aload_1
    //   60: astore_0
    //   61: aload_1
    //   62: ldc '('
    //   64: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   67: ifeq -> 76
    //   70: aload_1
    //   71: iconst_1
    //   72: invokevirtual substring : (I)Ljava/lang/String;
    //   75: astore_0
    //   76: aload_0
    //   77: ldc '='
    //   79: ldc ''"'
    //   81: ldc ''"'
    //   83: iconst_1
    //   84: invokestatic split : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
    //   87: astore_0
    //   88: aload_0
    //   89: iconst_0
    //   90: invokeinterface get : (I)Ljava/lang/Object;
    //   95: checkcast java/lang/String
    //   98: astore_2
    //   99: aload_0
    //   100: invokeinterface size : ()I
    //   105: iconst_1
    //   106: if_icmple -> 123
    //   109: aload_0
    //   110: iconst_1
    //   111: invokeinterface get : (I)Ljava/lang/Object;
    //   116: checkcast java/lang/String
    //   119: astore_0
    //   120: goto -> 125
    //   123: aconst_null
    //   124: astore_0
    //   125: aload_0
    //   126: astore_1
    //   127: aload_0
    //   128: ifnull -> 185
    //   131: aload_0
    //   132: ldc '"'
    //   134: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   137: ifeq -> 149
    //   140: aload_0
    //   141: ldc '"'
    //   143: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   146: ifne -> 173
    //   149: aload_0
    //   150: astore_1
    //   151: aload_0
    //   152: ldc_w '''
    //   155: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   158: ifeq -> 185
    //   161: aload_0
    //   162: astore_1
    //   163: aload_0
    //   164: ldc_w '''
    //   167: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   170: ifeq -> 185
    //   173: aload_0
    //   174: iconst_1
    //   175: aload_0
    //   176: invokevirtual length : ()I
    //   179: iconst_1
    //   180: isub
    //   181: invokevirtual substring : (II)Ljava/lang/String;
    //   184: astore_1
    //   185: aload_1
    //   186: ifnull -> 38
    //   189: ldc 'HOST'
    //   191: aload_2
    //   192: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   195: ifne -> 268
    //   198: ldc 'DBNAME'
    //   200: aload_2
    //   201: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   204: ifne -> 268
    //   207: ldc 'PORT'
    //   209: aload_2
    //   210: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   213: ifne -> 268
    //   216: ldc 'PROTOCOL'
    //   218: aload_2
    //   219: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   222: ifne -> 268
    //   225: ldc 'PATH'
    //   227: aload_2
    //   228: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   231: ifeq -> 237
    //   234: goto -> 268
    //   237: ldc 'user'
    //   239: aload_2
    //   240: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   243: ifne -> 257
    //   246: aload_2
    //   247: astore_0
    //   248: ldc 'password'
    //   250: aload_2
    //   251: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   254: ifeq -> 276
    //   257: aload_2
    //   258: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   261: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   264: astore_0
    //   265: goto -> 276
    //   268: aload_2
    //   269: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   272: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   275: astore_0
    //   276: aload_3
    //   277: aload_0
    //   278: aload_1
    //   279: invokevirtual setProperty : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
    //   282: pop
    //   283: goto -> 38
    //   286: aload_3
    //   287: areturn
  }
  
  public static int getMajorVersionInternal() {
    return safeIntParse("5");
  }
  
  public static int getMinorVersionInternal() {
    return safeIntParse("1");
  }
  
  public static String getOSName() {
    return System.getProperty("os.name");
  }
  
  public static String getPlatform() {
    return System.getProperty("os.arch");
  }
  
  private boolean isHostMaster(String paramString) {
    if (isHostPropertiesList(paramString)) {
      Properties properties = expandHostKeyValues(paramString);
      if (properties.containsKey("type") && "master".equalsIgnoreCase(properties.get("type").toString()))
        return true; 
    } 
    return false;
  }
  
  public static boolean isHostPropertiesList(String paramString) {
    boolean bool;
    if (paramString != null && StringUtils.startsWithIgnoreCase(paramString, "address=")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static String[] parseHostPortPair(String paramString) throws SQLException {
    String[] arrayOfString = new String[2];
    if (StringUtils.startsWithIgnoreCaseAndWs(paramString, "address=")) {
      arrayOfString[0] = paramString.trim();
      arrayOfString[1] = null;
      return arrayOfString;
    } 
    int i = paramString.indexOf(":");
    if (i != -1) {
      int j = i + 1;
      if (j < paramString.length()) {
        String str = paramString.substring(j);
        arrayOfString[0] = paramString.substring(0, i);
        arrayOfString[1] = str;
      } else {
        throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.37"), "01S00", null);
      } 
    } else {
      arrayOfString[0] = paramString;
      arrayOfString[1] = null;
    } 
    return arrayOfString;
  }
  
  private static int safeIntParse(String paramString) {
    try {
      return Integer.parseInt(paramString);
    } catch (NumberFormatException numberFormatException) {
      return 0;
    } 
  }
  
  public static void trackConnection(Connection paramConnection) {
    ConnectionPhantomReference connectionPhantomReference = new ConnectionPhantomReference((ConnectionImpl)paramConnection, refQueue);
    connectionPhantomRefs.put(connectionPhantomReference, connectionPhantomReference);
  }
  
  public boolean acceptsURL(String paramString) throws SQLException {
    if (paramString != null) {
      boolean bool;
      if (parseURL(paramString, null) != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.1"), "08001", null);
  }
  
  public Connection connect(String paramString, Properties paramProperties) throws SQLException {
    if (paramString != null) {
      if (StringUtils.startsWithIgnoreCase(paramString, "jdbc:mysql:loadbalance://"))
        return connectLoadBalanced(paramString, paramProperties); 
      if (StringUtils.startsWithIgnoreCase(paramString, "jdbc:mysql:replication://"))
        return connectReplicationConnection(paramString, paramProperties); 
      Properties properties = parseURL(paramString, paramProperties);
      if (properties == null)
        return null; 
      if (!"1".equals(properties.getProperty("NUM_HOSTS")))
        return connectFailover(paramString, paramProperties); 
      try {
        return ConnectionImpl.getInstance(host(properties), port(properties), properties, database(properties), paramString);
      } catch (SQLException sQLException) {
        throw sQLException;
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Messages.getString("NonRegisteringDriver.17"));
        stringBuilder.append(exception.toString());
        stringBuilder.append(Messages.getString("NonRegisteringDriver.18"));
        SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "08001", (ExceptionInterceptor)null);
        sQLException.initCause(exception);
        throw sQLException;
      } 
    } 
    throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.1"), "08001", null);
  }
  
  public Connection connectReplicationConnection(String paramString, Properties paramProperties) throws SQLException {
    Properties properties2 = parseURL(paramString, paramProperties);
    if (properties2 == null)
      return null; 
    Properties properties1 = (Properties)properties2.clone();
    paramProperties = (Properties)properties2.clone();
    paramProperties.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
    int i = Integer.parseInt(properties2.getProperty("NUM_HOSTS"));
    if (i >= 2) {
      ArrayList<String> arrayList2 = new ArrayList();
      ArrayList<String> arrayList1 = new ArrayList();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(properties1.getProperty("HOST.1"));
      stringBuilder.append(":");
      stringBuilder.append(properties1.getProperty("PORT.1"));
      boolean bool = isHostPropertiesList(stringBuilder.toString());
      for (int j = 0; j < i; j = k) {
        int k = j + 1;
        stringBuilder = new StringBuilder();
        stringBuilder.append("HOST.");
        stringBuilder.append(k);
        properties1.remove(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("PORT.");
        stringBuilder.append(k);
        properties1.remove(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("HOST.");
        stringBuilder.append(k);
        paramProperties.remove(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("PORT.");
        stringBuilder.append(k);
        paramProperties.remove(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("HOST.");
        stringBuilder.append(k);
        String str1 = properties2.getProperty(stringBuilder.toString());
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("PORT.");
        stringBuilder1.append(k);
        String str2 = properties2.getProperty(stringBuilder1.toString());
        if (bool) {
          if (isHostMaster(str1)) {
            arrayList1.add(str1);
          } else {
            arrayList2.add(str1);
          } 
        } else if (j == 0) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append(str1);
          stringBuilder2.append(":");
          stringBuilder2.append(str2);
          arrayList1.add(stringBuilder2.toString());
        } else {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append(str1);
          stringBuilder2.append(":");
          stringBuilder2.append(str2);
          arrayList2.add(stringBuilder2.toString());
        } 
      } 
      paramProperties.remove("NUM_HOSTS");
      properties1.remove("NUM_HOSTS");
      properties1.remove("HOST");
      properties1.remove("PORT");
      paramProperties.remove("HOST");
      paramProperties.remove("PORT");
      return ReplicationConnectionProxy.createProxyInstance(arrayList1, properties1, arrayList2, paramProperties);
    } 
    throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", "01S00", null);
  }
  
  public String database(Properties paramProperties) {
    return paramProperties.getProperty("DBNAME");
  }
  
  public int getMajorVersion() {
    return getMajorVersionInternal();
  }
  
  public int getMinorVersion() {
    return getMinorVersionInternal();
  }
  
  public DriverPropertyInfo[] getPropertyInfo(String paramString, Properties paramProperties) throws SQLException {
    Properties properties = paramProperties;
    if (paramProperties == null)
      properties = new Properties(); 
    paramProperties = properties;
    if (paramString != null) {
      paramProperties = properties;
      if (paramString.startsWith("jdbc:mysql://"))
        paramProperties = parseURL(paramString, properties); 
    } 
    DriverPropertyInfo driverPropertyInfo2 = new DriverPropertyInfo("HOST", paramProperties.getProperty("HOST"));
    driverPropertyInfo2.required = true;
    driverPropertyInfo2.description = Messages.getString("NonRegisteringDriver.3");
    DriverPropertyInfo driverPropertyInfo3 = new DriverPropertyInfo("PORT", paramProperties.getProperty("PORT", "3306"));
    driverPropertyInfo3.required = false;
    driverPropertyInfo3.description = Messages.getString("NonRegisteringDriver.7");
    DriverPropertyInfo driverPropertyInfo4 = new DriverPropertyInfo("DBNAME", paramProperties.getProperty("DBNAME"));
    driverPropertyInfo4.required = false;
    driverPropertyInfo4.description = "Database name";
    DriverPropertyInfo driverPropertyInfo5 = new DriverPropertyInfo("user", paramProperties.getProperty("user"));
    driverPropertyInfo5.required = true;
    driverPropertyInfo5.description = Messages.getString("NonRegisteringDriver.13");
    DriverPropertyInfo driverPropertyInfo1 = new DriverPropertyInfo("password", paramProperties.getProperty("password"));
    driverPropertyInfo1.required = true;
    driverPropertyInfo1.description = Messages.getString("NonRegisteringDriver.16");
    DriverPropertyInfo[] arrayOfDriverPropertyInfo = ConnectionPropertiesImpl.exposeAsDriverPropertyInfo(paramProperties, 5);
    arrayOfDriverPropertyInfo[0] = driverPropertyInfo2;
    arrayOfDriverPropertyInfo[1] = driverPropertyInfo3;
    arrayOfDriverPropertyInfo[2] = driverPropertyInfo4;
    arrayOfDriverPropertyInfo[3] = driverPropertyInfo5;
    arrayOfDriverPropertyInfo[4] = driverPropertyInfo1;
    return arrayOfDriverPropertyInfo;
  }
  
  public String host(Properties paramProperties) {
    return paramProperties.getProperty("HOST", "localhost");
  }
  
  public boolean jdbcCompliant() {
    return false;
  }
  
  public Properties parseURL(String paramString, Properties paramProperties) throws SQLException {
    StringBuilder stringBuilder1;
    SQLException sQLException;
    Properties properties2;
    String str2 = paramString;
    Properties properties3 = new Properties();
    if (paramProperties != null) {
      this(paramProperties);
    } else {
      this();
    } 
    if (str2 == null)
      return null; 
    if (!StringUtils.startsWithIgnoreCase(str2, "jdbc:mysql://") && !StringUtils.startsWithIgnoreCase(str2, "jdbc:mysql:mxj://") && !StringUtils.startsWithIgnoreCase(str2, "jdbc:mysql:loadbalance://") && !StringUtils.startsWithIgnoreCase(str2, "jdbc:mysql:replication://"))
      return null; 
    int i = str2.indexOf("//");
    if (StringUtils.startsWithIgnoreCase(str2, "jdbc:mysql:mxj://"))
      properties3.setProperty("socketFactory", "com.mysql.management.driverlaunched.ServerLauncherSocketFactory"); 
    int j = str2.indexOf("?");
    String str1 = str2;
    if (j != -1) {
      paramString = str2.substring(j + 1, paramString.length());
      str2 = str2.substring(0, j);
      StringTokenizer stringTokenizer = new StringTokenizer(paramString, "&");
      while (true) {
        str1 = str2;
        if (stringTokenizer.hasMoreTokens()) {
          paramString = stringTokenizer.nextToken();
          j = StringUtils.indexOfIgnoreCase(0, paramString, "=");
          if (j != -1) {
            str1 = paramString.substring(0, j);
            if (++j < paramString.length()) {
              paramString = paramString.substring(j);
            } else {
              paramString = null;
            } 
          } else {
            paramString = null;
            str1 = null;
          } 
          if (paramString != null && paramString.length() > 0 && str1 != null && str1.length() > 0)
            try {
              properties3.setProperty(str1, URLDecoder.decode(paramString, "UTF-8"));
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
              properties3.setProperty(str1, URLDecoder.decode(paramString));
            } catch (NoSuchMethodError noSuchMethodError) {
              properties3.setProperty(str1, URLDecoder.decode(paramString));
            }  
          continue;
        } 
        break;
      } 
    } 
    str1 = str1.substring(i + 2);
    i = StringUtils.indexOfIgnoreCase(0, str1, "/", "\"'", "\"'", StringUtils.SEARCH_MODE__ALL);
    paramString = str1;
    if (i != -1) {
      paramString = str1.substring(0, i);
      if (++i < str1.length())
        properties3.put("DBNAME", str1.substring(i, str1.length())); 
    } 
    if (paramString != null && paramString.trim().length() > 0) {
      Iterator<String> iterator = StringUtils.split(paramString, ",", "\"'", "\"'", false).iterator();
      i = 0;
      while (true) {
        j = i;
        if (iterator.hasNext()) {
          str1 = iterator.next();
          i++;
          String[] arrayOfString = parseHostPortPair(str1);
          if (arrayOfString[0] != null && arrayOfString[0].trim().length() > 0) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("HOST.");
            stringBuilder3.append(i);
            properties3.setProperty(stringBuilder3.toString(), arrayOfString[0]);
          } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("HOST.");
            stringBuilder3.append(i);
            properties3.setProperty(stringBuilder3.toString(), "localhost");
          } 
          if (arrayOfString[1] != null) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("PORT.");
            stringBuilder3.append(i);
            properties3.setProperty(stringBuilder3.toString(), arrayOfString[1]);
            continue;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("PORT.");
          stringBuilder.append(i);
          properties3.setProperty(stringBuilder.toString(), "3306");
          continue;
        } 
        break;
      } 
    } else {
      properties3.setProperty("HOST.1", "localhost");
      properties3.setProperty("PORT.1", "3306");
      j = 1;
    } 
    properties3.setProperty("NUM_HOSTS", String.valueOf(j));
    properties3.setProperty("HOST", properties3.getProperty("HOST.1"));
    properties3.setProperty("PORT", properties3.getProperty("PORT.1"));
    str1 = properties3.getProperty("propertiesTransform");
    Properties properties1 = properties3;
    if (str1 != null)
      try {
        properties1 = ((ConnectionPropertiesTransform)Class.forName(str1).newInstance()).transformProperties(properties3);
      } catch (InstantiationException instantiationException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to create properties transform instance '");
        stringBuilder.append(str1);
        stringBuilder.append("' due to underlying exception: ");
        stringBuilder.append(instantiationException.toString());
        throw SQLError.createSQLException(stringBuilder.toString(), "01S00", null);
      } catch (IllegalAccessException illegalAccessException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to create properties transform instance '");
        stringBuilder.append(str1);
        stringBuilder.append("' due to underlying exception: ");
        stringBuilder.append(illegalAccessException.toString());
        throw SQLError.createSQLException(stringBuilder.toString(), "01S00", null);
      } catch (ClassNotFoundException classNotFoundException) {
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Unable to create properties transform instance '");
        stringBuilder1.append(str1);
        stringBuilder1.append("' due to underlying exception: ");
        stringBuilder1.append(classNotFoundException.toString());
        throw SQLError.createSQLException(stringBuilder1.toString(), "01S00", null);
      }  
    if (Util.isColdFusion() && stringBuilder1.getProperty("autoConfigureForColdFusion", "true").equalsIgnoreCase("true")) {
      str1 = stringBuilder1.getProperty("useConfigs");
      StringBuilder stringBuilder = new StringBuilder();
      if (str1 != null) {
        stringBuilder.append(str1);
        stringBuilder.append(",");
      } 
      stringBuilder.append("coldFusion");
      stringBuilder1.setProperty("useConfigs", stringBuilder.toString());
    } 
    if (classNotFoundException != null) {
      str1 = classNotFoundException.getProperty("useConfigs");
    } else {
      str1 = null;
    } 
    str2 = str1;
    if (str1 == null)
      str2 = stringBuilder1.getProperty("useConfigs"); 
    StringBuilder stringBuilder2 = stringBuilder1;
    if (str2 != null) {
      List<String> list = StringUtils.split(str2, ",", true);
      properties2 = new Properties();
      for (String str : list) {
        try {
          Class<?> clazz = getClass();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("configs/");
          stringBuilder.append(str);
          stringBuilder.append(".properties");
          InputStream inputStream = clazz.getResourceAsStream(stringBuilder.toString());
          if (inputStream != null) {
            properties2.load(inputStream);
            continue;
          } 
          stringBuilder1 = new StringBuilder();
          this();
          stringBuilder1.append("Can't find configuration template named '");
          stringBuilder1.append(str);
          stringBuilder1.append("'");
          throw SQLError.createSQLException(stringBuilder1.toString(), "01S00", null);
        } catch (IOException iOException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unable to load configuration template '");
          stringBuilder.append(str);
          stringBuilder.append("' due to underlying IOException: ");
          stringBuilder.append(iOException);
          sQLException = SQLError.createSQLException(stringBuilder.toString(), "01S00", (ExceptionInterceptor)null);
          sQLException.initCause(iOException);
          throw sQLException;
        } 
      } 
      Iterator<E> iterator = iOException.keySet().iterator();
      while (iterator.hasNext()) {
        String str = iterator.next().toString();
        properties2.setProperty(str, iOException.getProperty(str));
      } 
    } 
    if (sQLException != null) {
      Iterator<E> iterator = sQLException.keySet().iterator();
      while (iterator.hasNext()) {
        String str = iterator.next().toString();
        if (!str.equals("NUM_HOSTS"))
          properties2.setProperty(str, sQLException.getProperty(str)); 
      } 
    } 
    return properties2;
  }
  
  public int port(Properties paramProperties) {
    return Integer.parseInt(paramProperties.getProperty("PORT", "3306"));
  }
  
  public String property(String paramString, Properties paramProperties) {
    return paramProperties.getProperty(paramString);
  }
  
  public static class ConnectionPhantomReference extends PhantomReference<ConnectionImpl> {
    private NetworkResources io;
    
    public ConnectionPhantomReference(ConnectionImpl param1ConnectionImpl, ReferenceQueue<ConnectionImpl> param1ReferenceQueue) {
      super(param1ConnectionImpl, param1ReferenceQueue);
      try {
        this.io = param1ConnectionImpl.getIO().getNetworkResources();
      } catch (SQLException sQLException) {}
    }
    
    public void cleanup() {
      NetworkResources networkResources = this.io;
      if (networkResources != null)
        try {
          networkResources.forceClose();
        } finally {
          this.io = null;
        }  
    }
  }
}
