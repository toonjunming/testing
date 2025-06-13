package javax.activation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.UnsupportedFlavorException;

public class ObjectDataContentHandler implements DataContentHandler {
  private DataContentHandler dch = null;
  
  private String mimeType;
  
  private Object obj;
  
  private DataFlavor[] transferFlavors = null;
  
  public ObjectDataContentHandler(DataContentHandler paramDataContentHandler, Object paramObject, String paramString) {
    this.obj = paramObject;
    this.mimeType = paramString;
    this.dch = paramDataContentHandler;
  }
  
  public Object getContent(DataSource paramDataSource) {
    return this.obj;
  }
  
  public DataContentHandler getDCH() {
    return this.dch;
  }
  
  public Object getTransferData(DataFlavor paramDataFlavor, DataSource paramDataSource) throws UnsupportedFlavorException, IOException {
    DataContentHandler dataContentHandler = this.dch;
    if (dataContentHandler != null)
      return dataContentHandler.getTransferData(paramDataFlavor, paramDataSource); 
    if (paramDataFlavor.equals(getTransferDataFlavors()[0]))
      return this.obj; 
    throw new UnsupportedFlavorException(paramDataFlavor);
  }
  
  public DataFlavor[] getTransferDataFlavors() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   6: ifnonnull -> 67
    //   9: aload_0
    //   10: getfield dch : Ljavax/activation/DataContentHandler;
    //   13: astore_1
    //   14: aload_1
    //   15: ifnull -> 31
    //   18: aload_0
    //   19: aload_1
    //   20: invokeinterface getTransferDataFlavors : ()[Lmyjava/awt/datatransfer/DataFlavor;
    //   25: putfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   28: goto -> 67
    //   31: iconst_1
    //   32: anewarray myjava/awt/datatransfer/DataFlavor
    //   35: astore_2
    //   36: aload_0
    //   37: aload_2
    //   38: putfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   41: aload_0
    //   42: getfield obj : Ljava/lang/Object;
    //   45: invokevirtual getClass : ()Ljava/lang/Class;
    //   48: astore_3
    //   49: aload_0
    //   50: getfield mimeType : Ljava/lang/String;
    //   53: astore_1
    //   54: aload_2
    //   55: iconst_0
    //   56: new javax/activation/ActivationDataFlavor
    //   59: dup
    //   60: aload_3
    //   61: aload_1
    //   62: aload_1
    //   63: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;)V
    //   66: aastore
    //   67: aload_0
    //   68: getfield transferFlavors : [Lmyjava/awt/datatransfer/DataFlavor;
    //   71: astore_1
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_1
    //   75: areturn
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	76	finally
    //   18	28	76	finally
    //   31	67	76	finally
    //   67	72	76	finally
  }
  
  public void writeTo(Object paramObject, String paramString, OutputStream paramOutputStream) throws IOException {
    DataContentHandler dataContentHandler = this.dch;
    if (dataContentHandler != null) {
      dataContentHandler.writeTo(paramObject, paramString, paramOutputStream);
    } else if (paramObject instanceof byte[]) {
      paramOutputStream.write((byte[])paramObject);
    } else {
      if (paramObject instanceof String) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(paramOutputStream);
        outputStreamWriter.write((String)paramObject);
        outputStreamWriter.flush();
        return;
      } 
      paramObject = new StringBuilder("no object DCH for MIME type ");
      paramObject.append(this.mimeType);
      throw new UnsupportedDataTypeException(paramObject.toString());
    } 
  }
}
