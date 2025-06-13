package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExportControlled;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Security;
import com.mysql.jdbc.StringUtils;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Sha256PasswordPlugin implements AuthenticationPlugin {
  public static String PLUGIN_NAME = "sha256_password";
  
  public Connection connection;
  
  public String password = null;
  
  public boolean publicKeyRequested = false;
  
  public String publicKeyString = null;
  
  public String seed = null;
  
  private static String readRSAKey(Connection paramConnection, String paramString) throws SQLException {
    // Byte code:
    //   0: sipush #2048
    //   3: newarray byte
    //   5: astore #8
    //   7: aconst_null
    //   8: astore #7
    //   10: aconst_null
    //   11: astore #6
    //   13: aload #6
    //   15: astore #4
    //   17: new java/io/File
    //   20: astore #5
    //   22: aload #6
    //   24: astore #4
    //   26: aload #5
    //   28: aload_1
    //   29: invokespecial <init> : (Ljava/lang/String;)V
    //   32: aload #6
    //   34: astore #4
    //   36: aload #5
    //   38: invokevirtual getCanonicalPath : ()Ljava/lang/String;
    //   41: astore #10
    //   43: aload #6
    //   45: astore #4
    //   47: new java/io/BufferedInputStream
    //   50: astore #5
    //   52: aload #6
    //   54: astore #4
    //   56: new java/io/FileInputStream
    //   59: astore #9
    //   61: aload #6
    //   63: astore #4
    //   65: aload #9
    //   67: aload #10
    //   69: invokespecial <init> : (Ljava/lang/String;)V
    //   72: aload #6
    //   74: astore #4
    //   76: aload #5
    //   78: aload #9
    //   80: invokespecial <init> : (Ljava/io/InputStream;)V
    //   83: new java/lang/StringBuilder
    //   86: astore #4
    //   88: aload #4
    //   90: invokespecial <init> : ()V
    //   93: aload #5
    //   95: aload #8
    //   97: invokevirtual read : ([B)I
    //   100: istore_2
    //   101: iload_2
    //   102: iconst_m1
    //   103: if_icmpeq -> 122
    //   106: aload #4
    //   108: aload #8
    //   110: iconst_0
    //   111: iload_2
    //   112: invokestatic toAsciiString : ([BII)Ljava/lang/String;
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: goto -> 93
    //   122: aload #4
    //   124: invokevirtual toString : ()Ljava/lang/String;
    //   127: astore #4
    //   129: aload #5
    //   131: invokevirtual close : ()V
    //   134: aload #4
    //   136: areturn
    //   137: astore_1
    //   138: ldc 'Sha256PasswordPlugin.1'
    //   140: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   143: ldc 'S1000'
    //   145: aload_1
    //   146: aload_0
    //   147: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   152: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   155: athrow
    //   156: astore_1
    //   157: aload #5
    //   159: astore #4
    //   161: goto -> 312
    //   164: astore #6
    //   166: goto -> 179
    //   169: astore_1
    //   170: goto -> 312
    //   173: astore #6
    //   175: aload #7
    //   177: astore #5
    //   179: aload #5
    //   181: astore #4
    //   183: aload_0
    //   184: invokeinterface getParanoid : ()Z
    //   189: istore_3
    //   190: iload_3
    //   191: ifeq -> 224
    //   194: aload #5
    //   196: astore #4
    //   198: ldc 'Sha256PasswordPlugin.0'
    //   200: iconst_1
    //   201: anewarray java/lang/Object
    //   204: dup
    //   205: iconst_0
    //   206: ldc ''
    //   208: aastore
    //   209: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   212: ldc 'S1009'
    //   214: aload_0
    //   215: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   220: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   223: athrow
    //   224: aload #5
    //   226: astore #4
    //   228: new java/lang/StringBuilder
    //   231: astore #7
    //   233: aload #5
    //   235: astore #4
    //   237: aload #7
    //   239: invokespecial <init> : ()V
    //   242: aload #5
    //   244: astore #4
    //   246: aload #7
    //   248: ldc '''
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload #5
    //   256: astore #4
    //   258: aload #7
    //   260: aload_1
    //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: pop
    //   265: aload #5
    //   267: astore #4
    //   269: aload #7
    //   271: ldc '''
    //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: pop
    //   277: aload #5
    //   279: astore #4
    //   281: ldc 'Sha256PasswordPlugin.0'
    //   283: iconst_1
    //   284: anewarray java/lang/Object
    //   287: dup
    //   288: iconst_0
    //   289: aload #7
    //   291: invokevirtual toString : ()Ljava/lang/String;
    //   294: aastore
    //   295: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   298: ldc 'S1009'
    //   300: aload #6
    //   302: aload_0
    //   303: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   308: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   311: athrow
    //   312: aload #4
    //   314: ifnull -> 344
    //   317: aload #4
    //   319: invokevirtual close : ()V
    //   322: goto -> 344
    //   325: astore_1
    //   326: ldc 'Sha256PasswordPlugin.1'
    //   328: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   331: ldc 'S1000'
    //   333: aload_1
    //   334: aload_0
    //   335: invokeinterface getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   340: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   343: athrow
    //   344: aload_1
    //   345: athrow
    // Exception table:
    //   from	to	target	type
    //   17	22	173	java/io/IOException
    //   17	22	169	finally
    //   26	32	173	java/io/IOException
    //   26	32	169	finally
    //   36	43	173	java/io/IOException
    //   36	43	169	finally
    //   47	52	173	java/io/IOException
    //   47	52	169	finally
    //   56	61	173	java/io/IOException
    //   56	61	169	finally
    //   65	72	173	java/io/IOException
    //   65	72	169	finally
    //   76	83	173	java/io/IOException
    //   76	83	169	finally
    //   83	93	164	java/io/IOException
    //   83	93	156	finally
    //   93	101	164	java/io/IOException
    //   93	101	156	finally
    //   106	119	164	java/io/IOException
    //   106	119	156	finally
    //   122	129	164	java/io/IOException
    //   122	129	156	finally
    //   129	134	137	java/lang/Exception
    //   183	190	169	finally
    //   198	224	169	finally
    //   228	233	169	finally
    //   237	242	169	finally
    //   246	254	169	finally
    //   258	265	169	finally
    //   269	277	169	finally
    //   281	312	169	finally
    //   317	322	325	java/lang/Exception
  }
  
  public void destroy() {
    this.password = null;
    this.seed = null;
    this.publicKeyRequested = false;
  }
  
  public byte[] encryptPassword() throws SQLException {
    return encryptPassword("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
  }
  
  public byte[] encryptPassword(String paramString) throws SQLException {
    try {
      byte[] arrayOfByte1;
      String str = this.password;
      if (str != null) {
        arrayOfByte1 = StringUtils.getBytesNullTerminated(str, this.connection.getPasswordCharacterEncoding());
      } else {
        arrayOfByte1 = new byte[] { 0 };
      } 
      byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
      Security.xorString(arrayOfByte1, arrayOfByte2, this.seed.getBytes(), arrayOfByte1.length);
      return ExportControlled.encryptWithRSAPublicKey(arrayOfByte2, ExportControlled.decodeRSAPublicKey(this.publicKeyString, this.connection.getExceptionInterceptor()), paramString, this.connection.getExceptionInterceptor());
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.3", new Object[] { this.connection.getPasswordCharacterEncoding() }), "S1000", null);
    } 
  }
  
  public String getProtocolPluginName() {
    return PLUGIN_NAME;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    this.connection = paramConnection;
    String str = paramConnection.getServerRSAPublicKeyFile();
    if (str != null)
      this.publicKeyString = readRSAKey(this.connection, str); 
  }
  
  public boolean isReusable() {
    return true;
  }
  
  public boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException {
    paramList.clear();
    String str = this.password;
    if (str == null || str.length() == 0 || paramBuffer == null) {
      paramList.add(new Buffer(new byte[] { 0 }));
      return true;
    } 
    if (((MySQLConnection)this.connection).getIO().isSSLEstablished()) {
      try {
        paramBuffer = new Buffer(StringUtils.getBytes(this.password, this.connection.getPasswordCharacterEncoding()));
        paramBuffer.setPosition(paramBuffer.getBufLength());
        int i = paramBuffer.getBufLength();
        paramBuffer.writeByte((byte)0);
        paramBuffer.setBufLength(i + 1);
        paramBuffer.setPosition(0);
        paramList.add(paramBuffer);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.3", new Object[] { this.connection.getPasswordCharacterEncoding() }), "S1000", null);
      } 
    } else if (this.connection.getServerRSAPublicKeyFile() != null) {
      this.seed = unsupportedEncodingException.readString();
      paramList.add(new Buffer(encryptPassword()));
    } else if (this.connection.getAllowPublicKeyRetrieval()) {
      if (this.publicKeyRequested && unsupportedEncodingException.getBufLength() > 20) {
        this.publicKeyString = unsupportedEncodingException.readString();
        paramList.add(new Buffer(encryptPassword()));
        this.publicKeyRequested = false;
      } else {
        this.seed = unsupportedEncodingException.readString();
        paramList.add(new Buffer(new byte[] { 1 }));
        this.publicKeyRequested = true;
      } 
    } else {
      throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.2"), "08001", this.connection.getExceptionInterceptor());
    } 
    return true;
  }
  
  public boolean requiresConfidentiality() {
    return false;
  }
  
  public void reset() {}
  
  public void setAuthenticationParameters(String paramString1, String paramString2) {
    this.password = paramString2;
  }
}
