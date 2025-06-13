package com.mysql.jdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JDBC4MysqlSQLXML implements SQLXML {
  private ByteArrayOutputStream asByteArrayOutputStream;
  
  private DOMResult asDOMResult;
  
  private SAXResult asSAXResult;
  
  private StringWriter asStringWriter;
  
  private int columnIndexOfXml;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private boolean fromResultSet;
  
  private XMLInputFactory inputFactory;
  
  private boolean isClosed = false;
  
  private XMLOutputFactory outputFactory;
  
  private ResultSetInternalMethods owningResultSet;
  
  private SimpleSaxToReader saxToReaderConverter;
  
  private String stringRep;
  
  private boolean workingWithResult;
  
  public JDBC4MysqlSQLXML(ExceptionInterceptor paramExceptionInterceptor) {
    this.fromResultSet = false;
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  public JDBC4MysqlSQLXML(ResultSetInternalMethods paramResultSetInternalMethods, int paramInt, ExceptionInterceptor paramExceptionInterceptor) {
    this.owningResultSet = paramResultSetInternalMethods;
    this.columnIndexOfXml = paramInt;
    this.fromResultSet = true;
    this.exceptionInterceptor = paramExceptionInterceptor;
  }
  
  private Reader binaryInputStreamStreamToReader(ByteArrayOutputStream paramByteArrayOutputStream) {
    String str1;
    String str2 = "UTF-8";
    try {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
      this(paramByteArrayOutputStream.toByteArray());
      XMLStreamReader xMLStreamReader = this.inputFactory.createXMLStreamReader(byteArrayInputStream);
      while (true) {
        int i = xMLStreamReader.next();
        str1 = str2;
        if (i != 8) {
          if (i == 7) {
            String str = xMLStreamReader.getEncoding();
            str1 = str2;
            if (str != null)
              str1 = str; 
            break;
          } 
          continue;
        } 
        break;
      } 
    } finally {
      Exception exception = null;
    } 
    try {
      str2 = new String();
      this(paramByteArrayOutputStream.toByteArray(), str1);
      return new StringReader(str2);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new RuntimeException(unsupportedEncodingException);
    } 
  }
  
  private void checkClosed() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isClosed : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: ldc 'SQLXMLInstance has been free()d'
    //   16: aload_0
    //   17: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   20: invokestatic createSQLException : (Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   23: athrow
    //   24: astore_2
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_2
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	24	finally
    //   14	24	24	finally
  }
  
  private void checkWorkingWithResult() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield workingWithResult : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: ldc 'Can't perform requested operation after getResult() has been called to write XML data'
    //   16: ldc 'S1009'
    //   18: aload_0
    //   19: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   22: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   25: athrow
    //   26: astore_2
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_2
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	26	finally
    //   14	26	26	finally
  }
  
  private OutputStream setBinaryStreamInternal() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/io/ByteArrayOutputStream
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: putfield asByteArrayOutputStream : Ljava/io/ByteArrayOutputStream;
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: areturn
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  private Writer setCharacterStreamInternal() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/io/StringWriter
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: putfield asStringWriter : Ljava/io/StringWriter;
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: areturn
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  public String domSourceToString() throws SQLException {
    try {
      StreamResult streamResult;
      DOMSource dOMSource = new DOMSource();
      this(this.asDOMResult.getNode());
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StringWriter stringWriter = new StringWriter();
      this();
      return stringWriter.toString();
    } finally {
      Exception exception = null;
      SQLException sQLException = SQLError.createSQLException(exception.getMessage(), "S1009", this.exceptionInterceptor);
      sQLException.initCause(exception);
    } 
  }
  
  public void free() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield stringRep : Ljava/lang/String;
    //   7: aload_0
    //   8: aconst_null
    //   9: putfield asDOMResult : Ljavax/xml/transform/dom/DOMResult;
    //   12: aload_0
    //   13: aconst_null
    //   14: putfield asSAXResult : Ljavax/xml/transform/sax/SAXResult;
    //   17: aload_0
    //   18: aconst_null
    //   19: putfield inputFactory : Ljavax/xml/stream/XMLInputFactory;
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield outputFactory : Ljavax/xml/stream/XMLOutputFactory;
    //   27: aload_0
    //   28: aconst_null
    //   29: putfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   32: aload_0
    //   33: iconst_0
    //   34: putfield workingWithResult : Z
    //   37: aload_0
    //   38: iconst_1
    //   39: putfield isClosed : Z
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	45	finally
  }
  
  public InputStream getBinaryStream() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   14: aload_0
    //   15: getfield columnIndexOfXml : I
    //   18: invokeinterface getBinaryStream : (I)Ljava/io/InputStream;
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: areturn
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	28	finally
  }
  
  public Reader getCharacterStream() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   14: aload_0
    //   15: getfield columnIndexOfXml : I
    //   18: invokeinterface getCharacterStream : (I)Ljava/io/Reader;
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: areturn
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	28	finally
  }
  
  public <T extends javax.xml.transform.Source> T getSource(Class<T> paramClass) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_1
    //   11: ifnull -> 338
    //   14: aload_1
    //   15: ldc javax/xml/transform/sax/SAXSource
    //   17: invokevirtual equals : (Ljava/lang/Object;)Z
    //   20: ifeq -> 26
    //   23: goto -> 338
    //   26: aload_1
    //   27: ldc javax/xml/transform/dom/DOMSource
    //   29: invokevirtual equals : (Ljava/lang/Object;)Z
    //   32: istore_2
    //   33: iload_2
    //   34: ifeq -> 143
    //   37: invokestatic newInstance : ()Ljavax/xml/parsers/DocumentBuilderFactory;
    //   40: astore_1
    //   41: aload_1
    //   42: iconst_1
    //   43: invokevirtual setNamespaceAware : (Z)V
    //   46: aload_1
    //   47: invokevirtual newDocumentBuilder : ()Ljavax/xml/parsers/DocumentBuilder;
    //   50: astore_3
    //   51: aload_0
    //   52: getfield fromResultSet : Z
    //   55: ifeq -> 82
    //   58: new org/xml/sax/InputSource
    //   61: astore_1
    //   62: aload_1
    //   63: aload_0
    //   64: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   67: aload_0
    //   68: getfield columnIndexOfXml : I
    //   71: invokeinterface getCharacterStream : (I)Ljava/io/Reader;
    //   76: invokespecial <init> : (Ljava/io/Reader;)V
    //   79: goto -> 103
    //   82: new java/io/StringReader
    //   85: astore_1
    //   86: aload_1
    //   87: aload_0
    //   88: getfield stringRep : Ljava/lang/String;
    //   91: invokespecial <init> : (Ljava/lang/String;)V
    //   94: new org/xml/sax/InputSource
    //   97: dup
    //   98: aload_1
    //   99: invokespecial <init> : (Ljava/io/Reader;)V
    //   102: astore_1
    //   103: new javax/xml/transform/dom/DOMSource
    //   106: dup
    //   107: aload_3
    //   108: aload_1
    //   109: invokevirtual parse : (Lorg/xml/sax/InputSource;)Lorg/w3c/dom/Document;
    //   112: invokespecial <init> : (Lorg/w3c/dom/Node;)V
    //   115: astore_1
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_1
    //   119: areturn
    //   120: astore_1
    //   121: aload_1
    //   122: invokevirtual getMessage : ()Ljava/lang/String;
    //   125: ldc 'S1009'
    //   127: aload_0
    //   128: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   131: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   134: astore_3
    //   135: aload_3
    //   136: aload_1
    //   137: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   140: pop
    //   141: aload_3
    //   142: athrow
    //   143: aload_1
    //   144: ldc javax/xml/transform/stream/StreamSource
    //   146: invokevirtual equals : (Ljava/lang/Object;)Z
    //   149: ifeq -> 201
    //   152: aload_0
    //   153: getfield fromResultSet : Z
    //   156: ifeq -> 176
    //   159: aload_0
    //   160: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   163: aload_0
    //   164: getfield columnIndexOfXml : I
    //   167: invokeinterface getCharacterStream : (I)Ljava/io/Reader;
    //   172: astore_1
    //   173: goto -> 188
    //   176: new java/io/StringReader
    //   179: dup
    //   180: aload_0
    //   181: getfield stringRep : Ljava/lang/String;
    //   184: invokespecial <init> : (Ljava/lang/String;)V
    //   187: astore_1
    //   188: new javax/xml/transform/stream/StreamSource
    //   191: dup
    //   192: aload_1
    //   193: invokespecial <init> : (Ljava/io/Reader;)V
    //   196: astore_1
    //   197: aload_0
    //   198: monitorexit
    //   199: aload_1
    //   200: areturn
    //   201: aload_1
    //   202: ldc javax/xml/transform/stax/StAXSource
    //   204: invokevirtual equals : (Ljava/lang/Object;)Z
    //   207: istore_2
    //   208: iload_2
    //   209: ifeq -> 291
    //   212: aload_0
    //   213: getfield fromResultSet : Z
    //   216: ifeq -> 236
    //   219: aload_0
    //   220: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   223: aload_0
    //   224: getfield columnIndexOfXml : I
    //   227: invokeinterface getCharacterStream : (I)Ljava/io/Reader;
    //   232: astore_1
    //   233: goto -> 248
    //   236: new java/io/StringReader
    //   239: dup
    //   240: aload_0
    //   241: getfield stringRep : Ljava/lang/String;
    //   244: invokespecial <init> : (Ljava/lang/String;)V
    //   247: astore_1
    //   248: new javax/xml/transform/stax/StAXSource
    //   251: dup
    //   252: aload_0
    //   253: getfield inputFactory : Ljavax/xml/stream/XMLInputFactory;
    //   256: aload_1
    //   257: invokevirtual createXMLStreamReader : (Ljava/io/Reader;)Ljavax/xml/stream/XMLStreamReader;
    //   260: invokespecial <init> : (Ljavax/xml/stream/XMLStreamReader;)V
    //   263: astore_1
    //   264: aload_0
    //   265: monitorexit
    //   266: aload_1
    //   267: areturn
    //   268: astore_3
    //   269: aload_3
    //   270: invokevirtual getMessage : ()Ljava/lang/String;
    //   273: ldc 'S1009'
    //   275: aload_0
    //   276: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   279: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   282: astore_1
    //   283: aload_1
    //   284: aload_3
    //   285: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   288: pop
    //   289: aload_1
    //   290: athrow
    //   291: new java/lang/StringBuilder
    //   294: astore_3
    //   295: aload_3
    //   296: invokespecial <init> : ()V
    //   299: aload_3
    //   300: ldc_w 'XML Source of type "'
    //   303: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   306: pop
    //   307: aload_3
    //   308: aload_1
    //   309: invokevirtual toString : ()Ljava/lang/String;
    //   312: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: aload_3
    //   317: ldc_w '" Not supported.'
    //   320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: pop
    //   324: aload_3
    //   325: invokevirtual toString : ()Ljava/lang/String;
    //   328: ldc 'S1009'
    //   330: aload_0
    //   331: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   334: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   337: athrow
    //   338: aload_0
    //   339: getfield fromResultSet : Z
    //   342: ifeq -> 369
    //   345: new org/xml/sax/InputSource
    //   348: astore_1
    //   349: aload_1
    //   350: aload_0
    //   351: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   354: aload_0
    //   355: getfield columnIndexOfXml : I
    //   358: invokeinterface getCharacterStream : (I)Ljava/io/Reader;
    //   363: invokespecial <init> : (Ljava/io/Reader;)V
    //   366: goto -> 390
    //   369: new java/io/StringReader
    //   372: astore_1
    //   373: aload_1
    //   374: aload_0
    //   375: getfield stringRep : Ljava/lang/String;
    //   378: invokespecial <init> : (Ljava/lang/String;)V
    //   381: new org/xml/sax/InputSource
    //   384: dup
    //   385: aload_1
    //   386: invokespecial <init> : (Ljava/io/Reader;)V
    //   389: astore_1
    //   390: new javax/xml/transform/sax/SAXSource
    //   393: dup
    //   394: aload_1
    //   395: invokespecial <init> : (Lorg/xml/sax/InputSource;)V
    //   398: astore_1
    //   399: aload_0
    //   400: monitorexit
    //   401: aload_1
    //   402: areturn
    //   403: astore_1
    //   404: aload_0
    //   405: monitorexit
    //   406: aload_1
    //   407: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	403	finally
    //   14	23	403	finally
    //   26	33	403	finally
    //   37	79	120	finally
    //   82	103	120	finally
    //   103	116	120	finally
    //   121	143	403	finally
    //   143	173	403	finally
    //   176	188	403	finally
    //   188	197	403	finally
    //   201	208	403	finally
    //   212	233	268	javax/xml/stream/XMLStreamException
    //   212	233	403	finally
    //   236	248	268	javax/xml/stream/XMLStreamException
    //   236	248	403	finally
    //   248	264	268	javax/xml/stream/XMLStreamException
    //   248	264	403	finally
    //   269	291	403	finally
    //   291	338	403	finally
    //   338	366	403	finally
    //   369	390	403	finally
    //   390	399	403	finally
  }
  
  public String getString() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: getfield fromResultSet : Z
    //   14: ifeq -> 35
    //   17: aload_0
    //   18: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   21: aload_0
    //   22: getfield columnIndexOfXml : I
    //   25: invokeinterface getString : (I)Ljava/lang/String;
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: areturn
    //   35: aload_0
    //   36: getfield stringRep : Ljava/lang/String;
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: areturn
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	44	finally
    //   35	40	44	finally
  }
  
  public boolean isEmpty() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: getfield fromResultSet : Z
    //   14: istore_3
    //   15: iconst_0
    //   16: istore_2
    //   17: iload_3
    //   18: ifne -> 48
    //   21: aload_0
    //   22: getfield stringRep : Ljava/lang/String;
    //   25: astore #4
    //   27: aload #4
    //   29: ifnull -> 42
    //   32: aload #4
    //   34: invokevirtual length : ()I
    //   37: istore_1
    //   38: iload_1
    //   39: ifne -> 44
    //   42: iconst_1
    //   43: istore_2
    //   44: aload_0
    //   45: monitorexit
    //   46: iload_2
    //   47: ireturn
    //   48: aload_0
    //   49: monitorexit
    //   50: iconst_0
    //   51: ireturn
    //   52: astore #4
    //   54: aload_0
    //   55: monitorexit
    //   56: aload #4
    //   58: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	52	finally
    //   21	27	52	finally
    //   32	38	52	finally
  }
  
  public String readerToString(Reader paramReader) throws SQLException {
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = new char[512];
    try {
      while (true) {
        int i = paramReader.read(arrayOfChar);
        if (i != -1) {
          stringBuilder.append(arrayOfChar, 0, i);
          continue;
        } 
        return stringBuilder.toString();
      } 
    } catch (IOException iOException) {
      SQLException sQLException = SQLError.createSQLException(iOException.getMessage(), "S1009", this.exceptionInterceptor);
      sQLException.initCause(iOException);
      throw sQLException;
    } 
  }
  
  public Reader serializeAsCharacterStream() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: getfield workingWithResult : Z
    //   10: ifeq -> 123
    //   13: aload_0
    //   14: getfield stringRep : Ljava/lang/String;
    //   17: ifnull -> 36
    //   20: new java/io/StringReader
    //   23: dup
    //   24: aload_0
    //   25: getfield stringRep : Ljava/lang/String;
    //   28: invokespecial <init> : (Ljava/lang/String;)V
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: areturn
    //   36: aload_0
    //   37: getfield asDOMResult : Ljavax/xml/transform/dom/DOMResult;
    //   40: ifnull -> 59
    //   43: new java/io/StringReader
    //   46: dup
    //   47: aload_0
    //   48: invokevirtual domSourceToString : ()Ljava/lang/String;
    //   51: invokespecial <init> : (Ljava/lang/String;)V
    //   54: astore_1
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_1
    //   58: areturn
    //   59: aload_0
    //   60: getfield asStringWriter : Ljava/io/StringWriter;
    //   63: ifnull -> 85
    //   66: new java/io/StringReader
    //   69: dup
    //   70: aload_0
    //   71: getfield asStringWriter : Ljava/io/StringWriter;
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: astore_1
    //   81: aload_0
    //   82: monitorexit
    //   83: aload_1
    //   84: areturn
    //   85: aload_0
    //   86: getfield asSAXResult : Ljavax/xml/transform/sax/SAXResult;
    //   89: ifnull -> 104
    //   92: aload_0
    //   93: getfield saxToReaderConverter : Lcom/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader;
    //   96: invokevirtual toReader : ()Ljava/io/Reader;
    //   99: astore_1
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_1
    //   103: areturn
    //   104: aload_0
    //   105: getfield asByteArrayOutputStream : Ljava/io/ByteArrayOutputStream;
    //   108: astore_1
    //   109: aload_1
    //   110: ifnull -> 123
    //   113: aload_0
    //   114: aload_1
    //   115: invokespecial binaryInputStreamStreamToReader : (Ljava/io/ByteArrayOutputStream;)Ljava/io/Reader;
    //   118: astore_1
    //   119: aload_0
    //   120: monitorexit
    //   121: aload_1
    //   122: areturn
    //   123: aload_0
    //   124: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   127: aload_0
    //   128: getfield columnIndexOfXml : I
    //   131: invokeinterface getCharacterStream : (I)Ljava/io/Reader;
    //   136: astore_1
    //   137: aload_0
    //   138: monitorexit
    //   139: aload_1
    //   140: areturn
    //   141: astore_1
    //   142: aload_0
    //   143: monitorexit
    //   144: aload_1
    //   145: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	141	finally
    //   36	55	141	finally
    //   59	81	141	finally
    //   85	100	141	finally
    //   104	109	141	finally
    //   113	119	141	finally
    //   123	137	141	finally
  }
  
  public String serializeAsString() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: getfield workingWithResult : Z
    //   10: ifeq -> 106
    //   13: aload_0
    //   14: getfield stringRep : Ljava/lang/String;
    //   17: astore_1
    //   18: aload_1
    //   19: ifnull -> 26
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: areturn
    //   26: aload_0
    //   27: getfield asDOMResult : Ljavax/xml/transform/dom/DOMResult;
    //   30: ifnull -> 42
    //   33: aload_0
    //   34: invokevirtual domSourceToString : ()Ljava/lang/String;
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: areturn
    //   42: aload_0
    //   43: getfield asStringWriter : Ljava/io/StringWriter;
    //   46: astore_1
    //   47: aload_1
    //   48: ifnull -> 60
    //   51: aload_1
    //   52: invokevirtual toString : ()Ljava/lang/String;
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: areturn
    //   60: aload_0
    //   61: getfield asSAXResult : Ljavax/xml/transform/sax/SAXResult;
    //   64: ifnull -> 83
    //   67: aload_0
    //   68: aload_0
    //   69: getfield saxToReaderConverter : Lcom/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader;
    //   72: invokevirtual toReader : ()Ljava/io/Reader;
    //   75: invokevirtual readerToString : (Ljava/io/Reader;)Ljava/lang/String;
    //   78: astore_1
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_1
    //   82: areturn
    //   83: aload_0
    //   84: getfield asByteArrayOutputStream : Ljava/io/ByteArrayOutputStream;
    //   87: astore_1
    //   88: aload_1
    //   89: ifnull -> 106
    //   92: aload_0
    //   93: aload_0
    //   94: aload_1
    //   95: invokespecial binaryInputStreamStreamToReader : (Ljava/io/ByteArrayOutputStream;)Ljava/io/Reader;
    //   98: invokevirtual readerToString : (Ljava/io/Reader;)Ljava/lang/String;
    //   101: astore_1
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_1
    //   105: areturn
    //   106: aload_0
    //   107: getfield owningResultSet : Lcom/mysql/jdbc/ResultSetInternalMethods;
    //   110: aload_0
    //   111: getfield columnIndexOfXml : I
    //   114: invokeinterface getString : (I)Ljava/lang/String;
    //   119: astore_1
    //   120: aload_0
    //   121: monitorexit
    //   122: aload_1
    //   123: areturn
    //   124: astore_1
    //   125: aload_0
    //   126: monitorexit
    //   127: aload_1
    //   128: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	124	finally
    //   26	38	124	finally
    //   42	47	124	finally
    //   51	56	124	finally
    //   60	79	124	finally
    //   83	88	124	finally
    //   92	102	124	finally
    //   106	120	124	finally
  }
  
  public OutputStream setBinaryStream() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: iconst_1
    //   12: putfield workingWithResult : Z
    //   15: aload_0
    //   16: invokespecial setBinaryStreamInternal : ()Ljava/io/OutputStream;
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: areturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public Writer setCharacterStream() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: iconst_1
    //   12: putfield workingWithResult : Z
    //   15: aload_0
    //   16: invokespecial setCharacterStreamInternal : ()Ljava/io/Writer;
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: areturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public <T extends javax.xml.transform.Result> T setResult(Class<T> paramClass) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: iconst_1
    //   12: putfield workingWithResult : Z
    //   15: aload_0
    //   16: aconst_null
    //   17: putfield asDOMResult : Ljavax/xml/transform/dom/DOMResult;
    //   20: aload_0
    //   21: aconst_null
    //   22: putfield asSAXResult : Ljavax/xml/transform/sax/SAXResult;
    //   25: aload_0
    //   26: aconst_null
    //   27: putfield saxToReaderConverter : Lcom/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader;
    //   30: aload_0
    //   31: aconst_null
    //   32: putfield stringRep : Ljava/lang/String;
    //   35: aload_0
    //   36: aconst_null
    //   37: putfield asStringWriter : Ljava/io/StringWriter;
    //   40: aload_0
    //   41: aconst_null
    //   42: putfield asByteArrayOutputStream : Ljava/io/ByteArrayOutputStream;
    //   45: aload_1
    //   46: ifnull -> 232
    //   49: aload_1
    //   50: ldc_w javax/xml/transform/sax/SAXResult
    //   53: invokevirtual equals : (Ljava/lang/Object;)Z
    //   56: ifeq -> 62
    //   59: goto -> 232
    //   62: aload_1
    //   63: ldc javax/xml/transform/dom/DOMResult
    //   65: invokevirtual equals : (Ljava/lang/Object;)Z
    //   68: ifeq -> 88
    //   71: new javax/xml/transform/dom/DOMResult
    //   74: astore_1
    //   75: aload_1
    //   76: invokespecial <init> : ()V
    //   79: aload_0
    //   80: aload_1
    //   81: putfield asDOMResult : Ljavax/xml/transform/dom/DOMResult;
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_1
    //   87: areturn
    //   88: aload_1
    //   89: ldc javax/xml/transform/stream/StreamResult
    //   91: invokevirtual equals : (Ljava/lang/Object;)Z
    //   94: ifeq -> 113
    //   97: new javax/xml/transform/stream/StreamResult
    //   100: dup
    //   101: aload_0
    //   102: invokespecial setCharacterStreamInternal : ()Ljava/io/Writer;
    //   105: invokespecial <init> : (Ljava/io/Writer;)V
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: areturn
    //   113: aload_1
    //   114: ldc_w javax/xml/transform/stax/StAXResult
    //   117: invokevirtual equals : (Ljava/lang/Object;)Z
    //   120: istore_2
    //   121: iload_2
    //   122: ifeq -> 185
    //   125: aload_0
    //   126: getfield outputFactory : Ljavax/xml/stream/XMLOutputFactory;
    //   129: ifnonnull -> 139
    //   132: aload_0
    //   133: invokestatic newInstance : ()Ljavax/xml/stream/XMLOutputFactory;
    //   136: putfield outputFactory : Ljavax/xml/stream/XMLOutputFactory;
    //   139: new javax/xml/transform/stax/StAXResult
    //   142: dup
    //   143: aload_0
    //   144: getfield outputFactory : Ljavax/xml/stream/XMLOutputFactory;
    //   147: aload_0
    //   148: invokespecial setCharacterStreamInternal : ()Ljava/io/Writer;
    //   151: invokevirtual createXMLEventWriter : (Ljava/io/Writer;)Ljavax/xml/stream/XMLEventWriter;
    //   154: invokespecial <init> : (Ljavax/xml/stream/XMLEventWriter;)V
    //   157: astore_1
    //   158: aload_0
    //   159: monitorexit
    //   160: aload_1
    //   161: areturn
    //   162: astore_1
    //   163: aload_1
    //   164: invokevirtual getMessage : ()Ljava/lang/String;
    //   167: ldc 'S1009'
    //   169: aload_0
    //   170: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   173: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   176: astore_3
    //   177: aload_3
    //   178: aload_1
    //   179: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   182: pop
    //   183: aload_3
    //   184: athrow
    //   185: new java/lang/StringBuilder
    //   188: astore_3
    //   189: aload_3
    //   190: invokespecial <init> : ()V
    //   193: aload_3
    //   194: ldc_w 'XML Result of type "'
    //   197: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: pop
    //   201: aload_3
    //   202: aload_1
    //   203: invokevirtual toString : ()Ljava/lang/String;
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_3
    //   211: ldc_w '" Not supported.'
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload_3
    //   219: invokevirtual toString : ()Ljava/lang/String;
    //   222: ldc 'S1009'
    //   224: aload_0
    //   225: getfield exceptionInterceptor : Lcom/mysql/jdbc/ExceptionInterceptor;
    //   228: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   231: athrow
    //   232: new com/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader
    //   235: astore_1
    //   236: aload_1
    //   237: aload_0
    //   238: invokespecial <init> : (Lcom/mysql/jdbc/JDBC4MysqlSQLXML;)V
    //   241: aload_0
    //   242: aload_1
    //   243: putfield saxToReaderConverter : Lcom/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader;
    //   246: new javax/xml/transform/sax/SAXResult
    //   249: astore_1
    //   250: aload_1
    //   251: aload_0
    //   252: getfield saxToReaderConverter : Lcom/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader;
    //   255: invokespecial <init> : (Lorg/xml/sax/ContentHandler;)V
    //   258: aload_0
    //   259: aload_1
    //   260: putfield asSAXResult : Ljavax/xml/transform/sax/SAXResult;
    //   263: aload_0
    //   264: monitorexit
    //   265: aload_1
    //   266: areturn
    //   267: astore_1
    //   268: aload_0
    //   269: monitorexit
    //   270: aload_1
    //   271: athrow
    // Exception table:
    //   from	to	target	type
    //   2	45	267	finally
    //   49	59	267	finally
    //   62	84	267	finally
    //   88	109	267	finally
    //   113	121	267	finally
    //   125	139	162	javax/xml/stream/XMLStreamException
    //   125	139	267	finally
    //   139	158	162	javax/xml/stream/XMLStreamException
    //   139	158	267	finally
    //   163	185	267	finally
    //   185	232	267	finally
    //   232	263	267	finally
  }
  
  public void setString(String paramString) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkClosed : ()V
    //   6: aload_0
    //   7: invokespecial checkWorkingWithResult : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: putfield stringRep : Ljava/lang/String;
    //   15: aload_0
    //   16: iconst_0
    //   17: putfield fromResultSet : Z
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  public class SimpleSaxToReader extends DefaultHandler {
    public StringBuilder buf = new StringBuilder();
    
    private boolean inCDATA = false;
    
    public final JDBC4MysqlSQLXML this$0;
    
    private void escapeCharsForXml(char param1Char, boolean param1Boolean) {
      if (param1Char != '\r') {
        if (param1Char != '"') {
          if (param1Char != '&') {
            if (param1Char != '<') {
              if (param1Char != '>') {
                if ((param1Char >= '\001' && param1Char <= '\037' && param1Char != '\t' && param1Char != '\n') || (param1Char >= '' && param1Char <= '') || param1Char == ' ' || (param1Boolean && (param1Char == '\t' || param1Char == '\n'))) {
                  this.buf.append("&#x");
                  this.buf.append(Integer.toHexString(param1Char).toUpperCase());
                  this.buf.append(";");
                  return;
                } 
                this.buf.append(param1Char);
              } else {
                this.buf.append("&gt;");
              } 
            } else {
              this.buf.append("&lt;");
            } 
          } else {
            this.buf.append("&amp;");
          } 
        } else if (!param1Boolean) {
          this.buf.append("\"");
        } else {
          this.buf.append("&quot;");
        } 
      } else {
        this.buf.append("&#xD;");
      } 
    }
    
    private void escapeCharsForXml(String param1String, boolean param1Boolean) {
      if (param1String == null)
        return; 
      int i = param1String.length();
      for (byte b = 0; b < i; b++)
        escapeCharsForXml(param1String.charAt(b), param1Boolean); 
    }
    
    private void escapeCharsForXml(char[] param1ArrayOfchar, int param1Int1, int param1Int2, boolean param1Boolean) {
      if (param1ArrayOfchar == null)
        return; 
      for (byte b = 0; b < param1Int2; b++)
        escapeCharsForXml(param1ArrayOfchar[param1Int1 + b], param1Boolean); 
    }
    
    public void characters(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
      if (!this.inCDATA) {
        escapeCharsForXml(param1ArrayOfchar, param1Int1, param1Int2, false);
      } else {
        this.buf.append(param1ArrayOfchar, param1Int1, param1Int2);
      } 
    }
    
    public void comment(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
      this.buf.append("<!--");
      for (byte b = 0; b < param1Int2; b++)
        this.buf.append(param1ArrayOfchar[param1Int1 + b]); 
      this.buf.append("-->");
    }
    
    public void endCDATA() throws SAXException {
      this.inCDATA = false;
      this.buf.append("]]>");
    }
    
    public void endDocument() throws SAXException {}
    
    public void ignorableWhitespace(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
      characters(param1ArrayOfchar, param1Int1, param1Int2);
    }
    
    public void startCDATA() throws SAXException {
      this.buf.append("<![CDATA[");
      this.inCDATA = true;
    }
    
    public void startDocument() throws SAXException {
      this.buf.append("<?xml version='1.0' encoding='UTF-8'?>");
    }
    
    public void startElement(String param1String1, String param1String2, String param1String3, Attributes param1Attributes) throws SAXException {
      this.buf.append("<");
      this.buf.append(param1String3);
      if (param1Attributes != null)
        for (byte b = 0; b < param1Attributes.getLength(); b++) {
          this.buf.append(" ");
          StringBuilder stringBuilder = this.buf;
          stringBuilder.append(param1Attributes.getQName(b));
          stringBuilder.append("=\"");
          escapeCharsForXml(param1Attributes.getValue(b), true);
          this.buf.append("\"");
        }  
      this.buf.append(">");
    }
    
    public Reader toReader() {
      return new StringReader(this.buf.toString());
    }
  }
}
