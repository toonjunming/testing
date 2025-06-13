package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.Transferable;
import myjava.awt.datatransfer.UnsupportedFlavorException;

public class DataHandler implements Transferable {
  private static final DataFlavor[] emptyFlavors = new DataFlavor[0];
  
  private static DataContentHandlerFactory factory;
  
  private CommandMap currentCommandMap = null;
  
  private DataContentHandler dataContentHandler = null;
  
  private DataSource dataSource = null;
  
  private DataContentHandler factoryDCH = null;
  
  private DataSource objDataSource = null;
  
  private Object object = null;
  
  private String objectMimeType = null;
  
  private DataContentHandlerFactory oldFactory = null;
  
  private String shortType = null;
  
  private DataFlavor[] transferFlavors = emptyFlavors;
  
  public DataHandler(Object paramObject, String paramString) {
    this.object = paramObject;
    this.objectMimeType = paramString;
    this.oldFactory = factory;
  }
  
  public DataHandler(URL paramURL) {
    this.dataSource = new URLDataSource(paramURL);
    this.oldFactory = factory;
  }
  
  public DataHandler(DataSource paramDataSource) {
    this.dataSource = paramDataSource;
    this.oldFactory = factory;
  }
  
  private String getBaseType() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield shortType : Ljava/lang/String;
    //   6: ifnonnull -> 40
    //   9: aload_0
    //   10: invokevirtual getContentType : ()Ljava/lang/String;
    //   13: astore_1
    //   14: new javax/activation/MimeType
    //   17: astore_2
    //   18: aload_2
    //   19: aload_1
    //   20: invokespecial <init> : (Ljava/lang/String;)V
    //   23: aload_0
    //   24: aload_2
    //   25: invokevirtual getBaseType : ()Ljava/lang/String;
    //   28: putfield shortType : Ljava/lang/String;
    //   31: goto -> 40
    //   34: astore_2
    //   35: aload_0
    //   36: aload_1
    //   37: putfield shortType : Ljava/lang/String;
    //   40: aload_0
    //   41: getfield shortType : Ljava/lang/String;
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: areturn
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	49	finally
    //   14	31	34	javax/activation/MimeTypeParseException
    //   14	31	49	finally
    //   35	40	49	finally
    //   40	45	49	finally
  }
  
  private CommandMap getCommandMap() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentCommandMap : Ljavax/activation/CommandMap;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: areturn
    //   15: invokestatic getDefaultCommandMap : ()Ljavax/activation/CommandMap;
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: areturn
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	23	finally
    //   15	19	23	finally
  }
  
  private DataContentHandler getDataContentHandler() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic javax/activation/DataHandler.factory : Ljavax/activation/DataContentHandlerFactory;
    //   5: astore_1
    //   6: aload_1
    //   7: aload_0
    //   8: getfield oldFactory : Ljavax/activation/DataContentHandlerFactory;
    //   11: if_acmpeq -> 36
    //   14: aload_0
    //   15: aload_1
    //   16: putfield oldFactory : Ljavax/activation/DataContentHandlerFactory;
    //   19: aload_0
    //   20: aconst_null
    //   21: putfield factoryDCH : Ljavax/activation/DataContentHandler;
    //   24: aload_0
    //   25: aconst_null
    //   26: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   29: aload_0
    //   30: getstatic javax/activation/DataHandler.emptyFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   33: putfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   36: aload_0
    //   37: getfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   40: astore_1
    //   41: aload_1
    //   42: ifnull -> 49
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: areturn
    //   49: aload_0
    //   50: invokespecial getBaseType : ()Ljava/lang/String;
    //   53: astore_1
    //   54: aload_0
    //   55: getfield factoryDCH : Ljavax/activation/DataContentHandler;
    //   58: ifnonnull -> 80
    //   61: getstatic javax/activation/DataHandler.factory : Ljavax/activation/DataContentHandlerFactory;
    //   64: astore_2
    //   65: aload_2
    //   66: ifnull -> 80
    //   69: aload_0
    //   70: aload_2
    //   71: aload_1
    //   72: invokeinterface createDataContentHandler : (Ljava/lang/String;)Ljavax/activation/DataContentHandler;
    //   77: putfield factoryDCH : Ljavax/activation/DataContentHandler;
    //   80: aload_0
    //   81: getfield factoryDCH : Ljavax/activation/DataContentHandler;
    //   84: astore_2
    //   85: aload_2
    //   86: ifnull -> 94
    //   89: aload_0
    //   90: aload_2
    //   91: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   94: aload_0
    //   95: getfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   98: ifnonnull -> 139
    //   101: aload_0
    //   102: getfield dataSource : Ljavax/activation/DataSource;
    //   105: ifnull -> 127
    //   108: aload_0
    //   109: aload_0
    //   110: invokespecial getCommandMap : ()Ljavax/activation/CommandMap;
    //   113: aload_1
    //   114: aload_0
    //   115: getfield dataSource : Ljavax/activation/DataSource;
    //   118: invokevirtual createDataContentHandler : (Ljava/lang/String;Ljavax/activation/DataSource;)Ljavax/activation/DataContentHandler;
    //   121: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   124: goto -> 139
    //   127: aload_0
    //   128: aload_0
    //   129: invokespecial getCommandMap : ()Ljavax/activation/CommandMap;
    //   132: aload_1
    //   133: invokevirtual createDataContentHandler : (Ljava/lang/String;)Ljavax/activation/DataContentHandler;
    //   136: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   139: aload_0
    //   140: getfield dataSource : Ljavax/activation/DataSource;
    //   143: astore_1
    //   144: aload_1
    //   145: ifnull -> 169
    //   148: new javax/activation/DataSourceDataContentHandler
    //   151: astore_2
    //   152: aload_2
    //   153: aload_0
    //   154: getfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   157: aload_1
    //   158: invokespecial <init> : (Ljavax/activation/DataContentHandler;Ljavax/activation/DataSource;)V
    //   161: aload_0
    //   162: aload_2
    //   163: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   166: goto -> 194
    //   169: new javax/activation/ObjectDataContentHandler
    //   172: astore_1
    //   173: aload_1
    //   174: aload_0
    //   175: getfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   178: aload_0
    //   179: getfield object : Ljava/lang/Object;
    //   182: aload_0
    //   183: getfield objectMimeType : Ljava/lang/String;
    //   186: invokespecial <init> : (Ljavax/activation/DataContentHandler;Ljava/lang/Object;Ljava/lang/String;)V
    //   189: aload_0
    //   190: aload_1
    //   191: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   194: aload_0
    //   195: getfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   198: astore_1
    //   199: aload_0
    //   200: monitorexit
    //   201: aload_1
    //   202: areturn
    //   203: astore_1
    //   204: aload_0
    //   205: monitorexit
    //   206: aload_1
    //   207: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	203	finally
    //   36	41	203	finally
    //   49	65	203	finally
    //   69	80	203	finally
    //   80	85	203	finally
    //   89	94	203	finally
    //   94	124	203	finally
    //   127	139	203	finally
    //   139	144	203	finally
    //   148	166	203	finally
    //   169	194	203	finally
    //   194	199	203	finally
  }
  
  public static void setDataContentHandlerFactory(DataContentHandlerFactory paramDataContentHandlerFactory) {
    // Byte code:
    //   0: ldc javax/activation/DataHandler
    //   2: monitorenter
    //   3: getstatic javax/activation/DataHandler.factory : Ljavax/activation/DataContentHandlerFactory;
    //   6: ifnonnull -> 53
    //   9: invokestatic getSecurityManager : ()Ljava/lang/SecurityManager;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull -> 45
    //   17: aload_1
    //   18: invokevirtual checkSetFactory : ()V
    //   21: goto -> 45
    //   24: astore_1
    //   25: ldc javax/activation/DataHandler
    //   27: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   30: aload_0
    //   31: invokevirtual getClass : ()Ljava/lang/Class;
    //   34: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   37: if_acmpne -> 43
    //   40: goto -> 45
    //   43: aload_1
    //   44: athrow
    //   45: aload_0
    //   46: putstatic javax/activation/DataHandler.factory : Ljavax/activation/DataContentHandlerFactory;
    //   49: ldc javax/activation/DataHandler
    //   51: monitorexit
    //   52: return
    //   53: new java/lang/Error
    //   56: astore_0
    //   57: aload_0
    //   58: ldc 'DataContentHandlerFactory already defined'
    //   60: invokespecial <init> : (Ljava/lang/String;)V
    //   63: aload_0
    //   64: athrow
    //   65: astore_0
    //   66: ldc javax/activation/DataHandler
    //   68: monitorexit
    //   69: aload_0
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   3	13	65	finally
    //   17	21	24	java/lang/SecurityException
    //   17	21	65	finally
    //   25	40	65	finally
    //   43	45	65	finally
    //   45	49	65	finally
    //   53	65	65	finally
  }
  
  public CommandInfo[] getAllCommands() {
    return (this.dataSource != null) ? getCommandMap().getAllCommands(getBaseType(), this.dataSource) : getCommandMap().getAllCommands(getBaseType());
  }
  
  public Object getBean(CommandInfo paramCommandInfo) {
    try {
      ClassLoader classLoader2 = SecuritySupport.getContextClassLoader();
      ClassLoader classLoader1 = classLoader2;
      if (classLoader2 == null)
        classLoader1 = getClass().getClassLoader(); 
      object = paramCommandInfo.getCommandObject(this, classLoader1);
    } catch (IOException|ClassNotFoundException object) {
      object = null;
    } 
    return object;
  }
  
  public CommandInfo getCommand(String paramString) {
    return (this.dataSource != null) ? getCommandMap().getCommand(getBaseType(), paramString, this.dataSource) : getCommandMap().getCommand(getBaseType(), paramString);
  }
  
  public Object getContent() throws IOException {
    Object object = this.object;
    return (object != null) ? object : getDataContentHandler().getContent(getDataSource());
  }
  
  public String getContentType() {
    DataSource dataSource = this.dataSource;
    return (dataSource != null) ? dataSource.getContentType() : this.objectMimeType;
  }
  
  public DataSource getDataSource() {
    DataSource dataSource2 = this.dataSource;
    DataSource dataSource1 = dataSource2;
    if (dataSource2 == null) {
      if (this.objDataSource == null)
        this.objDataSource = new DataHandlerDataSource(this); 
      dataSource1 = this.objDataSource;
    } 
    return dataSource1;
  }
  
  public InputStream getInputStream() throws IOException {
    StringBuilder stringBuilder;
    DataSource dataSource = this.dataSource;
    if (dataSource != null) {
      InputStream inputStream = dataSource.getInputStream();
    } else {
      final DataContentHandler fdch = getDataContentHandler();
      if (dataContentHandler != null) {
        if (!(dataContentHandler instanceof ObjectDataContentHandler) || ((ObjectDataContentHandler)dataContentHandler).getDCH() != null) {
          final PipedOutputStream pos = new PipedOutputStream();
          PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
          (new Thread(new Runnable() {
                public final DataHandler this$0;
                
                private final DataContentHandler val$fdch;
                
                private final PipedOutputStream val$pos;
                
                public void run() {
                  try {
                  
                  } catch (IOException iOException) {
                  
                  } finally {
                    try {
                      pos.close();
                    } catch (IOException iOException) {}
                  } 
                  try {
                    pos.close();
                  } catch (IOException iOException) {}
                }
              },  "DataHandler.getInputStream")).start();
          return pipedInputStream;
        } 
        StringBuilder stringBuilder1 = new StringBuilder("no object DCH for MIME type ");
        stringBuilder1.append(getBaseType());
        throw new UnsupportedDataTypeException(stringBuilder1.toString());
      } 
      stringBuilder = new StringBuilder("no DCH for MIME type ");
      stringBuilder.append(getBaseType());
      throw new UnsupportedDataTypeException(stringBuilder.toString());
    } 
    return (InputStream)stringBuilder;
  }
  
  public String getName() {
    DataSource dataSource = this.dataSource;
    return (dataSource != null) ? dataSource.getName() : null;
  }
  
  public OutputStream getOutputStream() throws IOException {
    DataSource dataSource = this.dataSource;
    return (dataSource != null) ? dataSource.getOutputStream() : null;
  }
  
  public CommandInfo[] getPreferredCommands() {
    return (this.dataSource != null) ? getCommandMap().getPreferredCommands(getBaseType(), this.dataSource) : getCommandMap().getPreferredCommands(getBaseType());
  }
  
  public Object getTransferData(DataFlavor paramDataFlavor) throws UnsupportedFlavorException, IOException {
    return getDataContentHandler().getTransferData(paramDataFlavor, this.dataSource);
  }
  
  public DataFlavor[] getTransferDataFlavors() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic javax/activation/DataHandler.factory : Ljavax/activation/DataContentHandlerFactory;
    //   5: aload_0
    //   6: getfield oldFactory : Ljavax/activation/DataContentHandlerFactory;
    //   9: if_acmpeq -> 19
    //   12: aload_0
    //   13: getstatic javax/activation/DataHandler.emptyFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   16: putfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   19: aload_0
    //   20: getfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   23: getstatic javax/activation/DataHandler.emptyFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   26: if_acmpne -> 42
    //   29: aload_0
    //   30: aload_0
    //   31: invokespecial getDataContentHandler : ()Ljavax/activation/DataContentHandler;
    //   34: invokeinterface getTransferDataFlavors : ()[Lmyjava/awt/datatransfer/DataFlavor;
    //   39: putfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   42: aload_0
    //   43: getfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: areturn
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	51	finally
    //   19	42	51	finally
    //   42	47	51	finally
  }
  
  public boolean isDataFlavorSupported(DataFlavor paramDataFlavor) {
    DataFlavor[] arrayOfDataFlavor = getTransferDataFlavors();
    for (byte b = 0;; b++) {
      if (b >= arrayOfDataFlavor.length)
        return false; 
      if (arrayOfDataFlavor[b].equals(paramDataFlavor))
        return true; 
    } 
  }
  
  public void setCommandMap(CommandMap paramCommandMap) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: getfield currentCommandMap : Ljavax/activation/CommandMap;
    //   7: if_acmpne -> 14
    //   10: aload_1
    //   11: ifnonnull -> 31
    //   14: aload_0
    //   15: getstatic javax/activation/DataHandler.emptyFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   18: putfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield dataContentHandler : Ljavax/activation/DataContentHandler;
    //   26: aload_0
    //   27: aload_1
    //   28: putfield currentCommandMap : Ljavax/activation/CommandMap;
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	34	finally
    //   14	31	34	finally
  }
  
  public void writeTo(OutputStream paramOutputStream) throws IOException {
    DataSource dataSource = this.dataSource;
    if (dataSource != null) {
      byte[] arrayOfByte = new byte[8192];
      InputStream inputStream = dataSource.getInputStream();
      try {
        while (true) {
          int i = inputStream.read(arrayOfByte);
          if (i <= 0) {
            inputStream.close();
            break;
          } 
          paramOutputStream.write(arrayOfByte, 0, i);
        } 
      } finally {
        inputStream.close();
      } 
    } else {
      getDataContentHandler().writeTo(this.object, this.objectMimeType, paramOutputStream);
    } 
  }
}
