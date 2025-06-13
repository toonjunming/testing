package com.mysql.jdbc;

import com.mysql.jdbc.util.Base64Decoder;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class ExportControlled {
  private static final String SQL_STATE_BAD_SSL_PARAMS = "08000";
  
  private static final String[] TLS_PROTOCOLS = new String[] { "TLSv1.2", "TLSv1.1", "TLSv1" };
  
  private static final String TLSv1 = "TLSv1";
  
  private static final String TLSv1_1 = "TLSv1.1";
  
  private static final String TLSv1_2 = "TLSv1.2";
  
  public static RSAPublicKey decodeRSAPublicKey(String paramString, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    if (paramString != null)
      try {
        int i = paramString.indexOf("\n") + 1;
        int j = paramString.indexOf("-----END PUBLIC KEY-----");
        byte[] arrayOfByte = Base64Decoder.decode(paramString.getBytes(), i, j - i);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec();
        this(arrayOfByte);
        return (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec);
      } catch (Exception exception) {
        throw SQLError.createSQLException("Unable to decode public key", "S1009", exception, paramExceptionInterceptor);
      }  
    SQLException sQLException = new SQLException();
    this("key parameter is null");
    throw sQLException;
  }
  
  public static boolean enabled() {
    return true;
  }
  
  public static byte[] encryptWithRSAPublicKey(byte[] paramArrayOfbyte, RSAPublicKey paramRSAPublicKey, String paramString, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    try {
      Cipher cipher = Cipher.getInstance(paramString);
      cipher.init(1, paramRSAPublicKey);
      return cipher.doFinal(paramArrayOfbyte);
    } catch (Exception exception) {
      throw SQLError.createSQLException(exception.getMessage(), "S1009", exception, paramExceptionInterceptor);
    } 
  }
  
  private static SSLSocketFactory getSSLSocketFactoryDefaultOrConfigured(MysqlIO paramMysqlIO) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface getClientCertificateKeyStoreUrl : ()Ljava/lang/String;
    //   9: astore #4
    //   11: aload_0
    //   12: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   15: invokeinterface getClientCertificateKeyStorePassword : ()Ljava/lang/String;
    //   20: astore #8
    //   22: aload_0
    //   23: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   26: invokeinterface getClientCertificateKeyStoreType : ()Ljava/lang/String;
    //   31: astore #7
    //   33: aload_0
    //   34: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   37: invokeinterface getTrustCertificateKeyStoreUrl : ()Ljava/lang/String;
    //   42: astore #9
    //   44: aload_0
    //   45: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   48: invokeinterface getTrustCertificateKeyStorePassword : ()Ljava/lang/String;
    //   53: astore #14
    //   55: aload_0
    //   56: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   59: invokeinterface getTrustCertificateKeyStoreType : ()Ljava/lang/String;
    //   64: astore #12
    //   66: aload #4
    //   68: astore #5
    //   70: aload #4
    //   72: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   75: ifeq -> 200
    //   78: ldc 'javax.net.ssl.keyStore'
    //   80: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   83: astore #10
    //   85: ldc 'javax.net.ssl.keyStorePassword'
    //   87: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   90: astore #6
    //   92: ldc 'javax.net.ssl.keyStoreType'
    //   94: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   97: astore #5
    //   99: aload #5
    //   101: astore #4
    //   103: aload #5
    //   105: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   108: ifeq -> 115
    //   111: ldc 'JKS'
    //   113: astore #4
    //   115: aload #10
    //   117: astore #5
    //   119: aload #6
    //   121: astore #8
    //   123: aload #4
    //   125: astore #7
    //   127: aload #10
    //   129: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   132: ifne -> 200
    //   135: new java/net/URL
    //   138: aload #10
    //   140: invokespecial <init> : (Ljava/lang/String;)V
    //   143: aload #10
    //   145: astore #5
    //   147: aload #6
    //   149: astore #8
    //   151: aload #4
    //   153: astore #7
    //   155: goto -> 200
    //   158: astore #5
    //   160: new java/lang/StringBuilder
    //   163: dup
    //   164: invokespecial <init> : ()V
    //   167: astore #5
    //   169: aload #5
    //   171: ldc 'file:'
    //   173: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: pop
    //   177: aload #5
    //   179: aload #10
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload #5
    //   187: invokevirtual toString : ()Ljava/lang/String;
    //   190: astore #5
    //   192: aload #4
    //   194: astore #7
    //   196: aload #6
    //   198: astore #8
    //   200: aload #9
    //   202: astore #6
    //   204: aload #9
    //   206: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   209: ifeq -> 334
    //   212: ldc 'javax.net.ssl.trustStore'
    //   214: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   217: astore #10
    //   219: ldc 'javax.net.ssl.trustStorePassword'
    //   221: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   224: astore #9
    //   226: ldc 'javax.net.ssl.trustStoreType'
    //   228: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   231: astore #6
    //   233: aload #6
    //   235: astore #4
    //   237: aload #6
    //   239: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   242: ifeq -> 249
    //   245: ldc 'JKS'
    //   247: astore #4
    //   249: aload #10
    //   251: astore #6
    //   253: aload #9
    //   255: astore #14
    //   257: aload #4
    //   259: astore #12
    //   261: aload #10
    //   263: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   266: ifne -> 334
    //   269: new java/net/URL
    //   272: aload #10
    //   274: invokespecial <init> : (Ljava/lang/String;)V
    //   277: aload #10
    //   279: astore #6
    //   281: aload #9
    //   283: astore #14
    //   285: aload #4
    //   287: astore #12
    //   289: goto -> 334
    //   292: astore #6
    //   294: new java/lang/StringBuilder
    //   297: dup
    //   298: invokespecial <init> : ()V
    //   301: astore #6
    //   303: aload #6
    //   305: ldc 'file:'
    //   307: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: pop
    //   311: aload #6
    //   313: aload #10
    //   315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   318: pop
    //   319: aload #6
    //   321: invokevirtual toString : ()Ljava/lang/String;
    //   324: astore #6
    //   326: aload #4
    //   328: astore #12
    //   330: aload #9
    //   332: astore #14
    //   334: new java/util/ArrayList
    //   337: dup
    //   338: invokespecial <init> : ()V
    //   341: astore #13
    //   343: invokestatic getDefaultAlgorithm : ()Ljava/lang/String;
    //   346: invokestatic getInstance : (Ljava/lang/String;)Ljavax/net/ssl/TrustManagerFactory;
    //   349: astore #17
    //   351: invokestatic getDefaultAlgorithm : ()Ljava/lang/String;
    //   354: invokestatic getInstance : (Ljava/lang/String;)Ljavax/net/ssl/KeyManagerFactory;
    //   357: astore #9
    //   359: aload #5
    //   361: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   364: istore_3
    //   365: iload_3
    //   366: ifne -> 1031
    //   369: aload #7
    //   371: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   374: ifne -> 509
    //   377: aload #7
    //   379: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/KeyStore;
    //   382: astore #10
    //   384: new java/net/URL
    //   387: astore #4
    //   389: aload #4
    //   391: aload #5
    //   393: invokespecial <init> : (Ljava/lang/String;)V
    //   396: aload #8
    //   398: ifnonnull -> 409
    //   401: iconst_0
    //   402: newarray char
    //   404: astore #8
    //   406: goto -> 416
    //   409: aload #8
    //   411: invokevirtual toCharArray : ()[C
    //   414: astore #8
    //   416: aload #4
    //   418: invokevirtual openStream : ()Ljava/io/InputStream;
    //   421: astore #4
    //   423: aload #10
    //   425: aload #4
    //   427: aload #8
    //   429: invokevirtual load : (Ljava/io/InputStream;[C)V
    //   432: aload #9
    //   434: aload #10
    //   436: aload #8
    //   438: invokevirtual init : (Ljava/security/KeyStore;[C)V
    //   441: aload #9
    //   443: invokevirtual getKeyManagers : ()[Ljavax/net/ssl/KeyManager;
    //   446: astore #8
    //   448: aload #8
    //   450: astore #5
    //   452: goto -> 515
    //   455: astore_0
    //   456: goto -> 1019
    //   459: astore #7
    //   461: aload #4
    //   463: astore #6
    //   465: goto -> 548
    //   468: astore #6
    //   470: aload #4
    //   472: astore #6
    //   474: goto -> 672
    //   477: astore #6
    //   479: aload #4
    //   481: astore #6
    //   483: goto -> 741
    //   486: astore #6
    //   488: aload #4
    //   490: astore #5
    //   492: goto -> 831
    //   495: astore #6
    //   497: aload #4
    //   499: astore #5
    //   501: goto -> 915
    //   504: astore #5
    //   506: goto -> 1003
    //   509: aconst_null
    //   510: astore #5
    //   512: aconst_null
    //   513: astore #4
    //   515: aload #5
    //   517: astore #15
    //   519: aload #4
    //   521: ifnull -> 1034
    //   524: aload #4
    //   526: invokevirtual close : ()V
    //   529: aload #5
    //   531: astore #15
    //   533: goto -> 1034
    //   536: astore_0
    //   537: aconst_null
    //   538: astore #4
    //   540: goto -> 1019
    //   543: astore #7
    //   545: aconst_null
    //   546: astore #6
    //   548: aload #6
    //   550: astore #4
    //   552: new java/lang/StringBuilder
    //   555: astore #8
    //   557: aload #6
    //   559: astore #4
    //   561: aload #8
    //   563: invokespecial <init> : ()V
    //   566: aload #6
    //   568: astore #4
    //   570: aload #8
    //   572: ldc 'Cannot open '
    //   574: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   577: pop
    //   578: aload #6
    //   580: astore #4
    //   582: aload #8
    //   584: aload #5
    //   586: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   589: pop
    //   590: aload #6
    //   592: astore #4
    //   594: aload #8
    //   596: ldc ' ['
    //   598: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   601: pop
    //   602: aload #6
    //   604: astore #4
    //   606: aload #8
    //   608: aload #7
    //   610: invokevirtual getMessage : ()Ljava/lang/String;
    //   613: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   616: pop
    //   617: aload #6
    //   619: astore #4
    //   621: aload #8
    //   623: ldc ']'
    //   625: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   628: pop
    //   629: aload #6
    //   631: astore #4
    //   633: aload #8
    //   635: invokevirtual toString : ()Ljava/lang/String;
    //   638: ldc '08000'
    //   640: iconst_0
    //   641: iconst_0
    //   642: aload_0
    //   643: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   646: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   649: astore_0
    //   650: aload #6
    //   652: astore #4
    //   654: aload_0
    //   655: aload #7
    //   657: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   660: pop
    //   661: aload #6
    //   663: astore #4
    //   665: aload_0
    //   666: athrow
    //   667: astore #4
    //   669: aconst_null
    //   670: astore #6
    //   672: aload #6
    //   674: astore #4
    //   676: new java/lang/StringBuilder
    //   679: astore #7
    //   681: aload #6
    //   683: astore #4
    //   685: aload #7
    //   687: invokespecial <init> : ()V
    //   690: aload #6
    //   692: astore #4
    //   694: aload #7
    //   696: aload #5
    //   698: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   701: pop
    //   702: aload #6
    //   704: astore #4
    //   706: aload #7
    //   708: ldc_w ' does not appear to be a valid URL.'
    //   711: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   714: pop
    //   715: aload #6
    //   717: astore #4
    //   719: aload #7
    //   721: invokevirtual toString : ()Ljava/lang/String;
    //   724: ldc '08000'
    //   726: iconst_0
    //   727: iconst_0
    //   728: aload_0
    //   729: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   732: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   735: athrow
    //   736: astore #4
    //   738: aconst_null
    //   739: astore #6
    //   741: aload #6
    //   743: astore #4
    //   745: new java/lang/StringBuilder
    //   748: astore #8
    //   750: aload #6
    //   752: astore #4
    //   754: aload #8
    //   756: invokespecial <init> : ()V
    //   759: aload #6
    //   761: astore #4
    //   763: aload #8
    //   765: ldc_w 'Could not load client'
    //   768: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   771: pop
    //   772: aload #6
    //   774: astore #4
    //   776: aload #8
    //   778: aload #7
    //   780: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   783: pop
    //   784: aload #6
    //   786: astore #4
    //   788: aload #8
    //   790: ldc_w ' keystore from '
    //   793: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   796: pop
    //   797: aload #6
    //   799: astore #4
    //   801: aload #8
    //   803: aload #5
    //   805: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   808: pop
    //   809: aload #6
    //   811: astore #4
    //   813: aload #8
    //   815: invokevirtual toString : ()Ljava/lang/String;
    //   818: aload_0
    //   819: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   822: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   825: athrow
    //   826: astore #6
    //   828: aconst_null
    //   829: astore #5
    //   831: aload #5
    //   833: astore #4
    //   835: new java/lang/StringBuilder
    //   838: astore #7
    //   840: aload #5
    //   842: astore #4
    //   844: aload #7
    //   846: invokespecial <init> : ()V
    //   849: aload #5
    //   851: astore #4
    //   853: aload #7
    //   855: ldc_w 'Could not create KeyStore instance ['
    //   858: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   861: pop
    //   862: aload #5
    //   864: astore #4
    //   866: aload #7
    //   868: aload #6
    //   870: invokevirtual getMessage : ()Ljava/lang/String;
    //   873: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   876: pop
    //   877: aload #5
    //   879: astore #4
    //   881: aload #7
    //   883: ldc ']'
    //   885: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   888: pop
    //   889: aload #5
    //   891: astore #4
    //   893: aload #7
    //   895: invokevirtual toString : ()Ljava/lang/String;
    //   898: ldc '08000'
    //   900: iconst_0
    //   901: iconst_0
    //   902: aload_0
    //   903: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   906: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   909: athrow
    //   910: astore #6
    //   912: aconst_null
    //   913: astore #5
    //   915: aload #5
    //   917: astore #4
    //   919: new java/lang/StringBuilder
    //   922: astore #7
    //   924: aload #5
    //   926: astore #4
    //   928: aload #7
    //   930: invokespecial <init> : ()V
    //   933: aload #5
    //   935: astore #4
    //   937: aload #7
    //   939: ldc_w 'Unsupported keystore algorithm ['
    //   942: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   945: pop
    //   946: aload #5
    //   948: astore #4
    //   950: aload #7
    //   952: aload #6
    //   954: invokevirtual getMessage : ()Ljava/lang/String;
    //   957: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   960: pop
    //   961: aload #5
    //   963: astore #4
    //   965: aload #7
    //   967: ldc ']'
    //   969: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   972: pop
    //   973: aload #5
    //   975: astore #4
    //   977: aload #7
    //   979: invokevirtual toString : ()Ljava/lang/String;
    //   982: ldc '08000'
    //   984: iconst_0
    //   985: iconst_0
    //   986: aload_0
    //   987: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   990: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   993: athrow
    //   994: astore_0
    //   995: goto -> 1019
    //   998: astore #4
    //   1000: aconst_null
    //   1001: astore #4
    //   1003: ldc_w 'Could not recover keys from client keystore.  Check password?'
    //   1006: ldc '08000'
    //   1008: iconst_0
    //   1009: iconst_0
    //   1010: aload_0
    //   1011: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1014: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1017: athrow
    //   1018: astore_0
    //   1019: aload #4
    //   1021: ifnull -> 1029
    //   1024: aload #4
    //   1026: invokevirtual close : ()V
    //   1029: aload_0
    //   1030: athrow
    //   1031: aconst_null
    //   1032: astore #15
    //   1034: aload #6
    //   1036: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   1039: istore_3
    //   1040: iload_3
    //   1041: ifne -> 1257
    //   1044: aload #12
    //   1046: invokestatic isNullOrEmpty : (Ljava/lang/String;)Z
    //   1049: ifne -> 1257
    //   1052: new java/net/URL
    //   1055: astore #4
    //   1057: aload #4
    //   1059: aload #6
    //   1061: invokespecial <init> : (Ljava/lang/String;)V
    //   1064: aload #4
    //   1066: invokevirtual openStream : ()Ljava/io/InputStream;
    //   1069: astore #4
    //   1071: aload #14
    //   1073: ifnonnull -> 1108
    //   1076: aload #4
    //   1078: astore #7
    //   1080: aload #4
    //   1082: astore #10
    //   1084: aload #4
    //   1086: astore #8
    //   1088: aload #4
    //   1090: astore #9
    //   1092: aload #4
    //   1094: astore #11
    //   1096: aload #4
    //   1098: astore #5
    //   1100: iconst_0
    //   1101: newarray char
    //   1103: astore #14
    //   1105: goto -> 1139
    //   1108: aload #4
    //   1110: astore #7
    //   1112: aload #4
    //   1114: astore #10
    //   1116: aload #4
    //   1118: astore #8
    //   1120: aload #4
    //   1122: astore #9
    //   1124: aload #4
    //   1126: astore #11
    //   1128: aload #4
    //   1130: astore #5
    //   1132: aload #14
    //   1134: invokevirtual toCharArray : ()[C
    //   1137: astore #14
    //   1139: aload #4
    //   1141: astore #7
    //   1143: aload #4
    //   1145: astore #10
    //   1147: aload #4
    //   1149: astore #8
    //   1151: aload #4
    //   1153: astore #9
    //   1155: aload #4
    //   1157: astore #11
    //   1159: aload #4
    //   1161: astore #5
    //   1163: aload #12
    //   1165: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/KeyStore;
    //   1168: astore #16
    //   1170: aload #4
    //   1172: astore #7
    //   1174: aload #4
    //   1176: astore #10
    //   1178: aload #4
    //   1180: astore #8
    //   1182: aload #4
    //   1184: astore #9
    //   1186: aload #4
    //   1188: astore #11
    //   1190: aload #4
    //   1192: astore #5
    //   1194: aload #16
    //   1196: aload #4
    //   1198: aload #14
    //   1200: invokevirtual load : (Ljava/io/InputStream;[C)V
    //   1203: aload #16
    //   1205: astore #14
    //   1207: goto -> 1263
    //   1210: astore_0
    //   1211: aconst_null
    //   1212: astore #4
    //   1214: goto -> 2206
    //   1217: astore #6
    //   1219: aconst_null
    //   1220: astore #4
    //   1222: goto -> 1743
    //   1225: astore #4
    //   1227: aconst_null
    //   1228: astore #4
    //   1230: goto -> 1867
    //   1233: astore #6
    //   1235: aconst_null
    //   1236: astore #4
    //   1238: goto -> 1961
    //   1241: astore #6
    //   1243: aconst_null
    //   1244: astore #4
    //   1246: goto -> 2045
    //   1249: astore #4
    //   1251: aconst_null
    //   1252: astore #4
    //   1254: goto -> 2137
    //   1257: aconst_null
    //   1258: astore #4
    //   1260: aconst_null
    //   1261: astore #14
    //   1263: aload #4
    //   1265: astore #7
    //   1267: aload #4
    //   1269: astore #10
    //   1271: aload #4
    //   1273: astore #8
    //   1275: aload #4
    //   1277: astore #9
    //   1279: aload #4
    //   1281: astore #11
    //   1283: aload #4
    //   1285: astore #5
    //   1287: aload #17
    //   1289: aload #14
    //   1291: invokevirtual init : (Ljava/security/KeyStore;)V
    //   1294: aload #4
    //   1296: astore #7
    //   1298: aload #4
    //   1300: astore #10
    //   1302: aload #4
    //   1304: astore #8
    //   1306: aload #4
    //   1308: astore #9
    //   1310: aload #4
    //   1312: astore #11
    //   1314: aload #4
    //   1316: astore #5
    //   1318: aload #17
    //   1320: invokevirtual getTrustManagers : ()[Ljavax/net/ssl/TrustManager;
    //   1323: astore #14
    //   1325: aload #4
    //   1327: astore #7
    //   1329: aload #4
    //   1331: astore #10
    //   1333: aload #4
    //   1335: astore #8
    //   1337: aload #4
    //   1339: astore #9
    //   1341: aload #4
    //   1343: astore #11
    //   1345: aload #4
    //   1347: astore #5
    //   1349: aload_0
    //   1350: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   1353: invokeinterface getVerifyServerCertificate : ()Z
    //   1358: istore_3
    //   1359: aload #4
    //   1361: astore #7
    //   1363: aload #4
    //   1365: astore #10
    //   1367: aload #4
    //   1369: astore #8
    //   1371: aload #4
    //   1373: astore #9
    //   1375: aload #4
    //   1377: astore #11
    //   1379: aload #4
    //   1381: astore #5
    //   1383: aload #14
    //   1385: arraylength
    //   1386: istore_2
    //   1387: iconst_0
    //   1388: istore_1
    //   1389: iload_1
    //   1390: iload_2
    //   1391: if_icmpge -> 1540
    //   1394: aload #14
    //   1396: iload_1
    //   1397: aaload
    //   1398: astore #17
    //   1400: aload #17
    //   1402: astore #16
    //   1404: aload #4
    //   1406: astore #7
    //   1408: aload #4
    //   1410: astore #10
    //   1412: aload #4
    //   1414: astore #8
    //   1416: aload #4
    //   1418: astore #9
    //   1420: aload #4
    //   1422: astore #11
    //   1424: aload #4
    //   1426: astore #5
    //   1428: aload #17
    //   1430: instanceof javax/net/ssl/X509TrustManager
    //   1433: ifeq -> 1500
    //   1436: aload #4
    //   1438: astore #7
    //   1440: aload #4
    //   1442: astore #10
    //   1444: aload #4
    //   1446: astore #8
    //   1448: aload #4
    //   1450: astore #9
    //   1452: aload #4
    //   1454: astore #11
    //   1456: aload #4
    //   1458: astore #5
    //   1460: new com/mysql/jdbc/ExportControlled$X509TrustManagerWrapper
    //   1463: astore #16
    //   1465: aload #4
    //   1467: astore #7
    //   1469: aload #4
    //   1471: astore #10
    //   1473: aload #4
    //   1475: astore #8
    //   1477: aload #4
    //   1479: astore #9
    //   1481: aload #4
    //   1483: astore #11
    //   1485: aload #4
    //   1487: astore #5
    //   1489: aload #16
    //   1491: aload #17
    //   1493: checkcast javax/net/ssl/X509TrustManager
    //   1496: iload_3
    //   1497: invokespecial <init> : (Ljavax/net/ssl/X509TrustManager;Z)V
    //   1500: aload #4
    //   1502: astore #7
    //   1504: aload #4
    //   1506: astore #10
    //   1508: aload #4
    //   1510: astore #8
    //   1512: aload #4
    //   1514: astore #9
    //   1516: aload #4
    //   1518: astore #11
    //   1520: aload #4
    //   1522: astore #5
    //   1524: aload #13
    //   1526: aload #16
    //   1528: invokeinterface add : (Ljava/lang/Object;)Z
    //   1533: pop
    //   1534: iinc #1, 1
    //   1537: goto -> 1389
    //   1540: aload #4
    //   1542: ifnull -> 1550
    //   1545: aload #4
    //   1547: invokevirtual close : ()V
    //   1550: aload #13
    //   1552: invokeinterface size : ()I
    //   1557: ifne -> 1575
    //   1560: aload #13
    //   1562: new com/mysql/jdbc/ExportControlled$X509TrustManagerWrapper
    //   1565: dup
    //   1566: invokespecial <init> : ()V
    //   1569: invokeinterface add : (Ljava/lang/Object;)Z
    //   1574: pop
    //   1575: ldc_w 'TLS'
    //   1578: invokestatic getInstance : (Ljava/lang/String;)Ljavax/net/ssl/SSLContext;
    //   1581: astore #4
    //   1583: aload #4
    //   1585: aload #15
    //   1587: aload #13
    //   1589: aload #13
    //   1591: invokeinterface size : ()I
    //   1596: anewarray javax/net/ssl/TrustManager
    //   1599: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   1604: checkcast [Ljavax/net/ssl/TrustManager;
    //   1607: aconst_null
    //   1608: invokevirtual init : ([Ljavax/net/ssl/KeyManager;[Ljavax/net/ssl/TrustManager;Ljava/security/SecureRandom;)V
    //   1611: aload #4
    //   1613: invokevirtual getSocketFactory : ()Ljavax/net/ssl/SSLSocketFactory;
    //   1616: astore #4
    //   1618: aload #4
    //   1620: areturn
    //   1621: astore #4
    //   1623: new java/lang/StringBuilder
    //   1626: dup
    //   1627: invokespecial <init> : ()V
    //   1630: astore #5
    //   1632: aload #5
    //   1634: ldc_w 'KeyManagementException: '
    //   1637: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1640: pop
    //   1641: aload #5
    //   1643: aload #4
    //   1645: invokevirtual getMessage : ()Ljava/lang/String;
    //   1648: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1651: pop
    //   1652: aload #5
    //   1654: invokevirtual toString : ()Ljava/lang/String;
    //   1657: ldc '08000'
    //   1659: iconst_0
    //   1660: iconst_0
    //   1661: aload_0
    //   1662: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1665: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1668: athrow
    //   1669: astore #4
    //   1671: ldc_w 'TLS is not a valid SSL protocol.'
    //   1674: ldc '08000'
    //   1676: iconst_0
    //   1677: iconst_0
    //   1678: aload_0
    //   1679: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1682: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1685: athrow
    //   1686: astore #6
    //   1688: aload #7
    //   1690: astore #4
    //   1692: goto -> 1743
    //   1695: astore #4
    //   1697: aload #10
    //   1699: astore #4
    //   1701: goto -> 1867
    //   1704: astore #6
    //   1706: aload #8
    //   1708: astore #4
    //   1710: goto -> 1961
    //   1713: astore #6
    //   1715: aload #9
    //   1717: astore #4
    //   1719: goto -> 2045
    //   1722: astore #4
    //   1724: aload #11
    //   1726: astore #4
    //   1728: goto -> 2137
    //   1731: astore_0
    //   1732: aconst_null
    //   1733: astore #4
    //   1735: goto -> 2206
    //   1738: astore #6
    //   1740: aconst_null
    //   1741: astore #4
    //   1743: aload #4
    //   1745: astore #5
    //   1747: new java/lang/StringBuilder
    //   1750: astore #7
    //   1752: aload #4
    //   1754: astore #5
    //   1756: aload #7
    //   1758: invokespecial <init> : ()V
    //   1761: aload #4
    //   1763: astore #5
    //   1765: aload #7
    //   1767: ldc 'Cannot open '
    //   1769: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1772: pop
    //   1773: aload #4
    //   1775: astore #5
    //   1777: aload #7
    //   1779: aload #12
    //   1781: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1784: pop
    //   1785: aload #4
    //   1787: astore #5
    //   1789: aload #7
    //   1791: ldc ' ['
    //   1793: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1796: pop
    //   1797: aload #4
    //   1799: astore #5
    //   1801: aload #7
    //   1803: aload #6
    //   1805: invokevirtual getMessage : ()Ljava/lang/String;
    //   1808: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1811: pop
    //   1812: aload #4
    //   1814: astore #5
    //   1816: aload #7
    //   1818: ldc ']'
    //   1820: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1823: pop
    //   1824: aload #4
    //   1826: astore #5
    //   1828: aload #7
    //   1830: invokevirtual toString : ()Ljava/lang/String;
    //   1833: ldc '08000'
    //   1835: iconst_0
    //   1836: iconst_0
    //   1837: aload_0
    //   1838: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1841: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1844: astore_0
    //   1845: aload #4
    //   1847: astore #5
    //   1849: aload_0
    //   1850: aload #6
    //   1852: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1855: pop
    //   1856: aload #4
    //   1858: astore #5
    //   1860: aload_0
    //   1861: athrow
    //   1862: astore #4
    //   1864: aconst_null
    //   1865: astore #4
    //   1867: aload #4
    //   1869: astore #5
    //   1871: new java/lang/StringBuilder
    //   1874: astore #7
    //   1876: aload #4
    //   1878: astore #5
    //   1880: aload #7
    //   1882: invokespecial <init> : ()V
    //   1885: aload #4
    //   1887: astore #5
    //   1889: aload #7
    //   1891: ldc_w 'Could not load trust'
    //   1894: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1897: pop
    //   1898: aload #4
    //   1900: astore #5
    //   1902: aload #7
    //   1904: aload #12
    //   1906: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1909: pop
    //   1910: aload #4
    //   1912: astore #5
    //   1914: aload #7
    //   1916: ldc_w ' keystore from '
    //   1919: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1922: pop
    //   1923: aload #4
    //   1925: astore #5
    //   1927: aload #7
    //   1929: aload #6
    //   1931: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1934: pop
    //   1935: aload #4
    //   1937: astore #5
    //   1939: aload #7
    //   1941: invokevirtual toString : ()Ljava/lang/String;
    //   1944: ldc '08000'
    //   1946: iconst_0
    //   1947: iconst_0
    //   1948: aload_0
    //   1949: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1952: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1955: athrow
    //   1956: astore #6
    //   1958: aconst_null
    //   1959: astore #4
    //   1961: aload #4
    //   1963: astore #5
    //   1965: new java/lang/StringBuilder
    //   1968: astore #7
    //   1970: aload #4
    //   1972: astore #5
    //   1974: aload #7
    //   1976: invokespecial <init> : ()V
    //   1979: aload #4
    //   1981: astore #5
    //   1983: aload #7
    //   1985: ldc_w 'Unsupported keystore algorithm ['
    //   1988: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1991: pop
    //   1992: aload #4
    //   1994: astore #5
    //   1996: aload #7
    //   1998: aload #6
    //   2000: invokevirtual getMessage : ()Ljava/lang/String;
    //   2003: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2006: pop
    //   2007: aload #4
    //   2009: astore #5
    //   2011: aload #7
    //   2013: ldc ']'
    //   2015: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2018: pop
    //   2019: aload #4
    //   2021: astore #5
    //   2023: aload #7
    //   2025: invokevirtual toString : ()Ljava/lang/String;
    //   2028: ldc '08000'
    //   2030: iconst_0
    //   2031: iconst_0
    //   2032: aload_0
    //   2033: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2036: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2039: athrow
    //   2040: astore #6
    //   2042: aconst_null
    //   2043: astore #4
    //   2045: aload #4
    //   2047: astore #5
    //   2049: new java/lang/StringBuilder
    //   2052: astore #7
    //   2054: aload #4
    //   2056: astore #5
    //   2058: aload #7
    //   2060: invokespecial <init> : ()V
    //   2063: aload #4
    //   2065: astore #5
    //   2067: aload #7
    //   2069: ldc_w 'Could not create KeyStore instance ['
    //   2072: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2075: pop
    //   2076: aload #4
    //   2078: astore #5
    //   2080: aload #7
    //   2082: aload #6
    //   2084: invokevirtual getMessage : ()Ljava/lang/String;
    //   2087: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2090: pop
    //   2091: aload #4
    //   2093: astore #5
    //   2095: aload #7
    //   2097: ldc ']'
    //   2099: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2102: pop
    //   2103: aload #4
    //   2105: astore #5
    //   2107: aload #7
    //   2109: invokevirtual toString : ()Ljava/lang/String;
    //   2112: ldc '08000'
    //   2114: iconst_0
    //   2115: iconst_0
    //   2116: aload_0
    //   2117: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2120: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2123: athrow
    //   2124: astore_0
    //   2125: aload #5
    //   2127: astore #4
    //   2129: goto -> 2206
    //   2132: astore #4
    //   2134: aconst_null
    //   2135: astore #4
    //   2137: aload #4
    //   2139: astore #5
    //   2141: new java/lang/StringBuilder
    //   2144: astore #7
    //   2146: aload #4
    //   2148: astore #5
    //   2150: aload #7
    //   2152: invokespecial <init> : ()V
    //   2155: aload #4
    //   2157: astore #5
    //   2159: aload #7
    //   2161: aload #6
    //   2163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2166: pop
    //   2167: aload #4
    //   2169: astore #5
    //   2171: aload #7
    //   2173: ldc_w ' does not appear to be a valid URL.'
    //   2176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2179: pop
    //   2180: aload #4
    //   2182: astore #5
    //   2184: aload #7
    //   2186: invokevirtual toString : ()Ljava/lang/String;
    //   2189: ldc '08000'
    //   2191: iconst_0
    //   2192: iconst_0
    //   2193: aload_0
    //   2194: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2197: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2200: athrow
    //   2201: astore_0
    //   2202: aload #5
    //   2204: astore #4
    //   2206: aload #4
    //   2208: ifnull -> 2216
    //   2211: aload #4
    //   2213: invokevirtual close : ()V
    //   2216: aload_0
    //   2217: athrow
    //   2218: astore #4
    //   2220: ldc_w 'Default algorithm definitions for TrustManager and/or KeyManager are invalid.  Check java security properties file.'
    //   2223: ldc '08000'
    //   2225: iconst_0
    //   2226: iconst_0
    //   2227: aload_0
    //   2228: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   2231: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;IZLcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   2234: athrow
    //   2235: astore #4
    //   2237: aload #5
    //   2239: astore #15
    //   2241: goto -> 1034
    //   2244: astore #4
    //   2246: goto -> 1029
    //   2249: astore #4
    //   2251: goto -> 1550
    //   2254: astore #4
    //   2256: goto -> 2216
    // Exception table:
    //   from	to	target	type
    //   135	143	158	java/net/MalformedURLException
    //   269	277	292	java/net/MalformedURLException
    //   343	359	2218	java/security/NoSuchAlgorithmException
    //   369	396	998	java/security/UnrecoverableKeyException
    //   369	396	910	java/security/NoSuchAlgorithmException
    //   369	396	826	java/security/KeyStoreException
    //   369	396	736	java/security/cert/CertificateException
    //   369	396	667	java/net/MalformedURLException
    //   369	396	543	java/io/IOException
    //   369	396	536	finally
    //   401	406	998	java/security/UnrecoverableKeyException
    //   401	406	910	java/security/NoSuchAlgorithmException
    //   401	406	826	java/security/KeyStoreException
    //   401	406	736	java/security/cert/CertificateException
    //   401	406	667	java/net/MalformedURLException
    //   401	406	543	java/io/IOException
    //   401	406	536	finally
    //   409	416	998	java/security/UnrecoverableKeyException
    //   409	416	910	java/security/NoSuchAlgorithmException
    //   409	416	826	java/security/KeyStoreException
    //   409	416	736	java/security/cert/CertificateException
    //   409	416	667	java/net/MalformedURLException
    //   409	416	543	java/io/IOException
    //   409	416	536	finally
    //   416	423	998	java/security/UnrecoverableKeyException
    //   416	423	910	java/security/NoSuchAlgorithmException
    //   416	423	826	java/security/KeyStoreException
    //   416	423	736	java/security/cert/CertificateException
    //   416	423	667	java/net/MalformedURLException
    //   416	423	543	java/io/IOException
    //   416	423	536	finally
    //   423	448	504	java/security/UnrecoverableKeyException
    //   423	448	495	java/security/NoSuchAlgorithmException
    //   423	448	486	java/security/KeyStoreException
    //   423	448	477	java/security/cert/CertificateException
    //   423	448	468	java/net/MalformedURLException
    //   423	448	459	java/io/IOException
    //   423	448	455	finally
    //   524	529	2235	java/io/IOException
    //   552	557	994	finally
    //   561	566	994	finally
    //   570	578	994	finally
    //   582	590	994	finally
    //   594	602	994	finally
    //   606	617	994	finally
    //   621	629	994	finally
    //   633	650	994	finally
    //   654	661	994	finally
    //   665	667	994	finally
    //   676	681	1018	finally
    //   685	690	1018	finally
    //   694	702	1018	finally
    //   706	715	1018	finally
    //   719	736	1018	finally
    //   745	750	1018	finally
    //   754	759	1018	finally
    //   763	772	1018	finally
    //   776	784	1018	finally
    //   788	797	1018	finally
    //   801	809	1018	finally
    //   813	826	1018	finally
    //   835	840	994	finally
    //   844	849	994	finally
    //   853	862	994	finally
    //   866	877	994	finally
    //   881	889	994	finally
    //   893	910	994	finally
    //   919	924	994	finally
    //   928	933	994	finally
    //   937	946	994	finally
    //   950	961	994	finally
    //   965	973	994	finally
    //   977	994	994	finally
    //   1003	1018	1018	finally
    //   1024	1029	2244	java/io/IOException
    //   1034	1040	2132	java/net/MalformedURLException
    //   1034	1040	2040	java/security/KeyStoreException
    //   1034	1040	1956	java/security/NoSuchAlgorithmException
    //   1034	1040	1862	java/security/cert/CertificateException
    //   1034	1040	1738	java/io/IOException
    //   1034	1040	1731	finally
    //   1044	1071	1249	java/net/MalformedURLException
    //   1044	1071	1241	java/security/KeyStoreException
    //   1044	1071	1233	java/security/NoSuchAlgorithmException
    //   1044	1071	1225	java/security/cert/CertificateException
    //   1044	1071	1217	java/io/IOException
    //   1044	1071	1210	finally
    //   1100	1105	1722	java/net/MalformedURLException
    //   1100	1105	1713	java/security/KeyStoreException
    //   1100	1105	1704	java/security/NoSuchAlgorithmException
    //   1100	1105	1695	java/security/cert/CertificateException
    //   1100	1105	1686	java/io/IOException
    //   1100	1105	2124	finally
    //   1132	1139	1722	java/net/MalformedURLException
    //   1132	1139	1713	java/security/KeyStoreException
    //   1132	1139	1704	java/security/NoSuchAlgorithmException
    //   1132	1139	1695	java/security/cert/CertificateException
    //   1132	1139	1686	java/io/IOException
    //   1132	1139	2124	finally
    //   1163	1170	1722	java/net/MalformedURLException
    //   1163	1170	1713	java/security/KeyStoreException
    //   1163	1170	1704	java/security/NoSuchAlgorithmException
    //   1163	1170	1695	java/security/cert/CertificateException
    //   1163	1170	1686	java/io/IOException
    //   1163	1170	2124	finally
    //   1194	1203	1722	java/net/MalformedURLException
    //   1194	1203	1713	java/security/KeyStoreException
    //   1194	1203	1704	java/security/NoSuchAlgorithmException
    //   1194	1203	1695	java/security/cert/CertificateException
    //   1194	1203	1686	java/io/IOException
    //   1194	1203	2124	finally
    //   1287	1294	1722	java/net/MalformedURLException
    //   1287	1294	1713	java/security/KeyStoreException
    //   1287	1294	1704	java/security/NoSuchAlgorithmException
    //   1287	1294	1695	java/security/cert/CertificateException
    //   1287	1294	1686	java/io/IOException
    //   1287	1294	2124	finally
    //   1318	1325	1722	java/net/MalformedURLException
    //   1318	1325	1713	java/security/KeyStoreException
    //   1318	1325	1704	java/security/NoSuchAlgorithmException
    //   1318	1325	1695	java/security/cert/CertificateException
    //   1318	1325	1686	java/io/IOException
    //   1318	1325	2124	finally
    //   1349	1359	1722	java/net/MalformedURLException
    //   1349	1359	1713	java/security/KeyStoreException
    //   1349	1359	1704	java/security/NoSuchAlgorithmException
    //   1349	1359	1695	java/security/cert/CertificateException
    //   1349	1359	1686	java/io/IOException
    //   1349	1359	2124	finally
    //   1383	1387	1722	java/net/MalformedURLException
    //   1383	1387	1713	java/security/KeyStoreException
    //   1383	1387	1704	java/security/NoSuchAlgorithmException
    //   1383	1387	1695	java/security/cert/CertificateException
    //   1383	1387	1686	java/io/IOException
    //   1383	1387	2124	finally
    //   1428	1436	1722	java/net/MalformedURLException
    //   1428	1436	1713	java/security/KeyStoreException
    //   1428	1436	1704	java/security/NoSuchAlgorithmException
    //   1428	1436	1695	java/security/cert/CertificateException
    //   1428	1436	1686	java/io/IOException
    //   1428	1436	2124	finally
    //   1460	1465	1722	java/net/MalformedURLException
    //   1460	1465	1713	java/security/KeyStoreException
    //   1460	1465	1704	java/security/NoSuchAlgorithmException
    //   1460	1465	1695	java/security/cert/CertificateException
    //   1460	1465	1686	java/io/IOException
    //   1460	1465	2124	finally
    //   1489	1500	1722	java/net/MalformedURLException
    //   1489	1500	1713	java/security/KeyStoreException
    //   1489	1500	1704	java/security/NoSuchAlgorithmException
    //   1489	1500	1695	java/security/cert/CertificateException
    //   1489	1500	1686	java/io/IOException
    //   1489	1500	2124	finally
    //   1524	1534	1722	java/net/MalformedURLException
    //   1524	1534	1713	java/security/KeyStoreException
    //   1524	1534	1704	java/security/NoSuchAlgorithmException
    //   1524	1534	1695	java/security/cert/CertificateException
    //   1524	1534	1686	java/io/IOException
    //   1524	1534	2124	finally
    //   1545	1550	2249	java/io/IOException
    //   1575	1618	1669	java/security/NoSuchAlgorithmException
    //   1575	1618	1621	java/security/KeyManagementException
    //   1747	1752	2124	finally
    //   1756	1761	2124	finally
    //   1765	1773	2124	finally
    //   1777	1785	2124	finally
    //   1789	1797	2124	finally
    //   1801	1812	2124	finally
    //   1816	1824	2124	finally
    //   1828	1845	2124	finally
    //   1849	1856	2124	finally
    //   1860	1862	2124	finally
    //   1871	1876	2201	finally
    //   1880	1885	2201	finally
    //   1889	1898	2201	finally
    //   1902	1910	2201	finally
    //   1914	1923	2201	finally
    //   1927	1935	2201	finally
    //   1939	1956	2201	finally
    //   1965	1970	2124	finally
    //   1974	1979	2124	finally
    //   1983	1992	2124	finally
    //   1996	2007	2124	finally
    //   2011	2019	2124	finally
    //   2023	2040	2124	finally
    //   2049	2054	2124	finally
    //   2058	2063	2124	finally
    //   2067	2076	2124	finally
    //   2080	2091	2124	finally
    //   2095	2103	2124	finally
    //   2107	2124	2124	finally
    //   2141	2146	2201	finally
    //   2150	2155	2201	finally
    //   2159	2167	2201	finally
    //   2171	2180	2201	finally
    //   2184	2201	2201	finally
    //   2211	2216	2254	java/io/IOException
  }
  
  public static boolean isSSLEstablished(MysqlIO paramMysqlIO) {
    return SSLSocket.class.isAssignableFrom(paramMysqlIO.mysqlConnection.getClass());
  }
  
  public static void transformSocketToSSLSocket(MysqlIO paramMysqlIO) throws SQLException {
    // Byte code:
    //   0: new com/mysql/jdbc/ExportControlled$StandardSSLSocketFactory
    //   3: dup
    //   4: aload_0
    //   5: invokestatic getSSLSocketFactoryDefaultOrConfigured : (Lcom/mysql/jdbc/MysqlIO;)Ljavax/net/ssl/SSLSocketFactory;
    //   8: aload_0
    //   9: getfield socketFactory : Lcom/mysql/jdbc/SocketFactory;
    //   12: aload_0
    //   13: getfield mysqlConnection : Ljava/net/Socket;
    //   16: invokespecial <init> : (Ljavax/net/ssl/SSLSocketFactory;Lcom/mysql/jdbc/SocketFactory;Ljava/net/Socket;)V
    //   19: astore #6
    //   21: aload_0
    //   22: getfield host : Ljava/lang/String;
    //   25: astore #4
    //   27: aload_0
    //   28: getfield port : I
    //   31: istore_1
    //   32: aconst_null
    //   33: astore #5
    //   35: aload_0
    //   36: aload #6
    //   38: aload #4
    //   40: iload_1
    //   41: aconst_null
    //   42: invokeinterface connect : (Ljava/lang/String;ILjava/util/Properties;)Ljava/net/Socket;
    //   47: putfield mysqlConnection : Ljava/net/Socket;
    //   50: aload_0
    //   51: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   54: invokeinterface getEnabledTLSProtocols : ()Ljava/lang/String;
    //   59: astore #4
    //   61: aload #4
    //   63: ifnull -> 87
    //   66: aload #4
    //   68: invokevirtual length : ()I
    //   71: ifle -> 87
    //   74: aload #4
    //   76: ldc_w '\s*,\s*'
    //   79: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   82: astore #4
    //   84: goto -> 146
    //   87: aload_0
    //   88: bipush #8
    //   90: iconst_0
    //   91: iconst_4
    //   92: invokevirtual versionMeetsMinimum : (III)Z
    //   95: ifne -> 141
    //   98: aload_0
    //   99: iconst_5
    //   100: bipush #6
    //   102: iconst_0
    //   103: invokevirtual versionMeetsMinimum : (III)Z
    //   106: ifeq -> 122
    //   109: aload_0
    //   110: invokevirtual getServerVersion : ()Ljava/lang/String;
    //   113: invokestatic isEnterpriseEdition : (Ljava/lang/String;)Z
    //   116: ifeq -> 122
    //   119: goto -> 141
    //   122: iconst_2
    //   123: anewarray java/lang/String
    //   126: dup
    //   127: iconst_0
    //   128: ldc 'TLSv1.1'
    //   130: aastore
    //   131: dup
    //   132: iconst_1
    //   133: ldc 'TLSv1'
    //   135: aastore
    //   136: astore #4
    //   138: goto -> 146
    //   141: getstatic com/mysql/jdbc/ExportControlled.TLS_PROTOCOLS : [Ljava/lang/String;
    //   144: astore #4
    //   146: new java/util/ArrayList
    //   149: astore #7
    //   151: aload #7
    //   153: aload #4
    //   155: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   158: invokespecial <init> : (Ljava/util/Collection;)V
    //   161: aload_0
    //   162: getfield mysqlConnection : Ljava/net/Socket;
    //   165: checkcast javax/net/ssl/SSLSocket
    //   168: invokevirtual getSupportedProtocols : ()[Ljava/lang/String;
    //   171: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   174: astore #10
    //   176: new java/util/ArrayList
    //   179: astore #9
    //   181: aload #9
    //   183: invokespecial <init> : ()V
    //   186: getstatic com/mysql/jdbc/ExportControlled.TLS_PROTOCOLS : [Ljava/lang/String;
    //   189: astore #4
    //   191: aload #4
    //   193: arraylength
    //   194: istore_2
    //   195: iconst_0
    //   196: istore_1
    //   197: iload_1
    //   198: iload_2
    //   199: if_icmpge -> 248
    //   202: aload #4
    //   204: iload_1
    //   205: aaload
    //   206: astore #8
    //   208: aload #10
    //   210: aload #8
    //   212: invokeinterface contains : (Ljava/lang/Object;)Z
    //   217: ifeq -> 242
    //   220: aload #7
    //   222: aload #8
    //   224: invokeinterface contains : (Ljava/lang/Object;)Z
    //   229: ifeq -> 242
    //   232: aload #9
    //   234: aload #8
    //   236: invokeinterface add : (Ljava/lang/Object;)Z
    //   241: pop
    //   242: iinc #1, 1
    //   245: goto -> 197
    //   248: aload_0
    //   249: getfield mysqlConnection : Ljava/net/Socket;
    //   252: checkcast javax/net/ssl/SSLSocket
    //   255: aload #9
    //   257: iconst_0
    //   258: anewarray java/lang/String
    //   261: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   266: checkcast [Ljava/lang/String;
    //   269: invokevirtual setEnabledProtocols : ([Ljava/lang/String;)V
    //   272: aload_0
    //   273: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   276: invokeinterface getEnabledSSLCipherSuites : ()Ljava/lang/String;
    //   281: astore #4
    //   283: iconst_1
    //   284: istore_2
    //   285: aload #4
    //   287: ifnull -> 303
    //   290: aload #4
    //   292: invokevirtual length : ()I
    //   295: ifle -> 303
    //   298: iconst_1
    //   299: istore_1
    //   300: goto -> 305
    //   303: iconst_0
    //   304: istore_1
    //   305: iload_1
    //   306: ifeq -> 393
    //   309: new java/util/ArrayList
    //   312: astore #5
    //   314: aload #5
    //   316: invokespecial <init> : ()V
    //   319: aload_0
    //   320: getfield mysqlConnection : Ljava/net/Socket;
    //   323: checkcast javax/net/ssl/SSLSocket
    //   326: invokevirtual getEnabledCipherSuites : ()[Ljava/lang/String;
    //   329: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   332: astore #7
    //   334: aload #4
    //   336: ldc_w '\s*,\s*'
    //   339: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   342: astore #8
    //   344: aload #8
    //   346: arraylength
    //   347: istore_2
    //   348: iconst_0
    //   349: istore_1
    //   350: aload #5
    //   352: astore #4
    //   354: iload_1
    //   355: iload_2
    //   356: if_icmpge -> 573
    //   359: aload #8
    //   361: iload_1
    //   362: aaload
    //   363: astore #4
    //   365: aload #7
    //   367: aload #4
    //   369: invokeinterface contains : (Ljava/lang/Object;)Z
    //   374: ifeq -> 387
    //   377: aload #5
    //   379: aload #4
    //   381: invokeinterface add : (Ljava/lang/Object;)Z
    //   386: pop
    //   387: iinc #1, 1
    //   390: goto -> 350
    //   393: aload_0
    //   394: iconst_5
    //   395: iconst_5
    //   396: bipush #45
    //   398: invokevirtual versionMeetsMinimum : (III)Z
    //   401: ifeq -> 415
    //   404: aload_0
    //   405: iconst_5
    //   406: bipush #6
    //   408: iconst_0
    //   409: invokevirtual versionMeetsMinimum : (III)Z
    //   412: ifeq -> 450
    //   415: aload_0
    //   416: iconst_5
    //   417: bipush #6
    //   419: bipush #26
    //   421: invokevirtual versionMeetsMinimum : (III)Z
    //   424: ifeq -> 438
    //   427: aload_0
    //   428: iconst_5
    //   429: bipush #7
    //   431: iconst_0
    //   432: invokevirtual versionMeetsMinimum : (III)Z
    //   435: ifeq -> 450
    //   438: aload_0
    //   439: iconst_5
    //   440: bipush #7
    //   442: bipush #6
    //   444: invokevirtual versionMeetsMinimum : (III)Z
    //   447: ifeq -> 463
    //   450: invokestatic getJVMVersion : ()I
    //   453: bipush #8
    //   455: if_icmpge -> 476
    //   458: iload_2
    //   459: istore_1
    //   460: goto -> 478
    //   463: invokestatic getJVMVersion : ()I
    //   466: bipush #8
    //   468: if_icmplt -> 476
    //   471: iload_2
    //   472: istore_1
    //   473: goto -> 478
    //   476: iconst_0
    //   477: istore_1
    //   478: aload #5
    //   480: astore #4
    //   482: iload_1
    //   483: ifeq -> 573
    //   486: new java/util/ArrayList
    //   489: astore #5
    //   491: aload #5
    //   493: invokespecial <init> : ()V
    //   496: aload_0
    //   497: getfield mysqlConnection : Ljava/net/Socket;
    //   500: checkcast javax/net/ssl/SSLSocket
    //   503: invokevirtual getEnabledCipherSuites : ()[Ljava/lang/String;
    //   506: astore #7
    //   508: aload #7
    //   510: arraylength
    //   511: istore_3
    //   512: iconst_0
    //   513: istore_2
    //   514: aload #5
    //   516: astore #4
    //   518: iload_2
    //   519: iload_3
    //   520: if_icmpge -> 573
    //   523: aload #7
    //   525: iload_2
    //   526: aaload
    //   527: astore #4
    //   529: iload_1
    //   530: ifeq -> 557
    //   533: aload #4
    //   535: ldc_w '_DHE_'
    //   538: invokevirtual indexOf : (Ljava/lang/String;)I
    //   541: iconst_m1
    //   542: if_icmpgt -> 567
    //   545: aload #4
    //   547: ldc_w '_DH_'
    //   550: invokevirtual indexOf : (Ljava/lang/String;)I
    //   553: iconst_m1
    //   554: if_icmpgt -> 567
    //   557: aload #5
    //   559: aload #4
    //   561: invokeinterface add : (Ljava/lang/Object;)Z
    //   566: pop
    //   567: iinc #2, 1
    //   570: goto -> 514
    //   573: aload #4
    //   575: ifnull -> 602
    //   578: aload_0
    //   579: getfield mysqlConnection : Ljava/net/Socket;
    //   582: checkcast javax/net/ssl/SSLSocket
    //   585: aload #4
    //   587: iconst_0
    //   588: anewarray java/lang/String
    //   591: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   596: checkcast [Ljava/lang/String;
    //   599: invokevirtual setEnabledCipherSuites : ([Ljava/lang/String;)V
    //   602: aload_0
    //   603: getfield mysqlConnection : Ljava/net/Socket;
    //   606: checkcast javax/net/ssl/SSLSocket
    //   609: invokevirtual startHandshake : ()V
    //   612: aload_0
    //   613: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   616: invokeinterface getUseUnbufferedInput : ()Z
    //   621: ifeq -> 638
    //   624: aload_0
    //   625: aload_0
    //   626: getfield mysqlConnection : Ljava/net/Socket;
    //   629: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   632: putfield mysqlInput : Ljava/io/InputStream;
    //   635: goto -> 664
    //   638: new java/io/BufferedInputStream
    //   641: astore #4
    //   643: aload #4
    //   645: aload_0
    //   646: getfield mysqlConnection : Ljava/net/Socket;
    //   649: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   652: sipush #16384
    //   655: invokespecial <init> : (Ljava/io/InputStream;I)V
    //   658: aload_0
    //   659: aload #4
    //   661: putfield mysqlInput : Ljava/io/InputStream;
    //   664: new java/io/BufferedOutputStream
    //   667: astore #4
    //   669: aload #4
    //   671: aload_0
    //   672: getfield mysqlConnection : Ljava/net/Socket;
    //   675: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   678: sipush #16384
    //   681: invokespecial <init> : (Ljava/io/OutputStream;I)V
    //   684: aload_0
    //   685: aload #4
    //   687: putfield mysqlOutput : Ljava/io/BufferedOutputStream;
    //   690: aload #4
    //   692: invokevirtual flush : ()V
    //   695: aload_0
    //   696: aload #6
    //   698: putfield socketFactory : Lcom/mysql/jdbc/SocketFactory;
    //   701: return
    //   702: astore #4
    //   704: aload_0
    //   705: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
    //   708: aload_0
    //   709: invokevirtual getLastPacketSentTimeMs : ()J
    //   712: aload_0
    //   713: invokevirtual getLastPacketReceivedTimeMs : ()J
    //   716: aload #4
    //   718: aload_0
    //   719: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
    //   722: invokestatic createCommunicationsException : (Lcom/mysql/jdbc/MySQLConnection;JJLjava/lang/Exception;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   725: athrow
    // Exception table:
    //   from	to	target	type
    //   21	32	702	java/io/IOException
    //   35	61	702	java/io/IOException
    //   66	84	702	java/io/IOException
    //   87	119	702	java/io/IOException
    //   122	138	702	java/io/IOException
    //   141	146	702	java/io/IOException
    //   146	195	702	java/io/IOException
    //   208	242	702	java/io/IOException
    //   248	283	702	java/io/IOException
    //   290	298	702	java/io/IOException
    //   309	348	702	java/io/IOException
    //   365	387	702	java/io/IOException
    //   393	415	702	java/io/IOException
    //   415	438	702	java/io/IOException
    //   438	450	702	java/io/IOException
    //   450	458	702	java/io/IOException
    //   463	471	702	java/io/IOException
    //   486	512	702	java/io/IOException
    //   533	557	702	java/io/IOException
    //   557	567	702	java/io/IOException
    //   578	602	702	java/io/IOException
    //   602	635	702	java/io/IOException
    //   638	664	702	java/io/IOException
    //   664	701	702	java/io/IOException
  }
  
  public static class StandardSSLSocketFactory implements SocketFactory, SocketMetadata {
    private final Socket existingSocket;
    
    private final SocketFactory existingSocketFactory;
    
    private SSLSocket rawSocket = null;
    
    private final SSLSocketFactory sslFact;
    
    public StandardSSLSocketFactory(SSLSocketFactory param1SSLSocketFactory, SocketFactory param1SocketFactory, Socket param1Socket) {
      this.sslFact = param1SSLSocketFactory;
      this.existingSocketFactory = param1SocketFactory;
      this.existingSocket = param1Socket;
    }
    
    public Socket afterHandshake() throws SocketException, IOException {
      this.existingSocketFactory.afterHandshake();
      return this.rawSocket;
    }
    
    public Socket beforeHandshake() throws SocketException, IOException {
      return this.rawSocket;
    }
    
    public Socket connect(String param1String, int param1Int, Properties param1Properties) throws SocketException, IOException {
      SSLSocket sSLSocket = (SSLSocket)this.sslFact.createSocket(this.existingSocket, param1String, param1Int, true);
      this.rawSocket = sSLSocket;
      return sSLSocket;
    }
    
    public boolean isLocallyConnected(ConnectionImpl param1ConnectionImpl) throws SQLException {
      return SocketMetadata.Helper.isLocallyConnected(param1ConnectionImpl);
    }
  }
  
  public static class X509TrustManagerWrapper implements X509TrustManager {
    private CertificateFactory certFactory = null;
    
    private X509TrustManager origTm = null;
    
    private CertPathValidator validator = null;
    
    private PKIXParameters validatorParams = null;
    
    private boolean verifyServerCert = false;
    
    public X509TrustManagerWrapper() {}
    
    public X509TrustManagerWrapper(X509TrustManager param1X509TrustManager, boolean param1Boolean) throws CertificateException {
      this.origTm = param1X509TrustManager;
      this.verifyServerCert = param1Boolean;
      if (param1Boolean)
        try {
          HashSet<TrustAnchor> hashSet = new HashSet();
          this();
          for (X509Certificate x509Certificate : param1X509TrustManager.getAcceptedIssuers()) {
            TrustAnchor trustAnchor = new TrustAnchor();
            this(x509Certificate, null);
            hashSet.add(trustAnchor);
          } 
          PKIXParameters pKIXParameters = new PKIXParameters();
          this(hashSet);
          this.validatorParams = pKIXParameters;
          pKIXParameters.setRevocationEnabled(false);
          this.validator = CertPathValidator.getInstance("PKIX");
          this.certFactory = CertificateFactory.getInstance("X.509");
        } catch (Exception exception) {
          throw new CertificateException(exception);
        }  
    }
    
    public void checkClientTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) throws CertificateException {
      this.origTm.checkClientTrusted(param1ArrayOfX509Certificate, param1String);
    }
    
    public void checkServerTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) throws CertificateException {
      for (byte b = 0; b < param1ArrayOfX509Certificate.length; b++)
        param1ArrayOfX509Certificate[b].checkValidity(); 
      if (this.validatorParams != null) {
        (new X509CertSelector()).setSerialNumber(param1ArrayOfX509Certificate[0].getSerialNumber());
        try {
          CertPath certPath = this.certFactory.generateCertPath(Arrays.asList((Certificate[])param1ArrayOfX509Certificate));
          ((PKIXCertPathValidatorResult)this.validator.validate(certPath, this.validatorParams)).getTrustAnchor().getTrustedCert().checkValidity();
        } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
          throw new CertificateException(invalidAlgorithmParameterException);
        } catch (CertPathValidatorException certPathValidatorException) {
          throw new CertificateException(certPathValidatorException);
        } 
      } 
      if (this.verifyServerCert)
        this.origTm.checkServerTrusted((X509Certificate[])certPathValidatorException, param1String); 
    }
    
    public X509Certificate[] getAcceptedIssuers() {
      X509Certificate[] arrayOfX509Certificate;
      X509TrustManager x509TrustManager = this.origTm;
      if (x509TrustManager != null) {
        arrayOfX509Certificate = x509TrustManager.getAcceptedIssuers();
      } else {
        arrayOfX509Certificate = new X509Certificate[0];
      } 
      return arrayOfX509Certificate;
    }
  }
}
