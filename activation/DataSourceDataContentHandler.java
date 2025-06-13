package javax.activation;

import java.io.IOException;
import java.io.OutputStream;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.UnsupportedFlavorException;

public class DataSourceDataContentHandler implements DataContentHandler {
  private DataContentHandler dch = null;
  
  private DataSource ds = null;
  
  private DataFlavor[] transferFlavors = null;
  
  public DataSourceDataContentHandler(DataContentHandler paramDataContentHandler, DataSource paramDataSource) {
    this.ds = paramDataSource;
    this.dch = paramDataContentHandler;
  }
  
  public Object getContent(DataSource paramDataSource) throws IOException {
    DataContentHandler dataContentHandler = this.dch;
    return (dataContentHandler != null) ? dataContentHandler.getContent(paramDataSource) : paramDataSource.getInputStream();
  }
  
  public Object getTransferData(DataFlavor paramDataFlavor, DataSource paramDataSource) throws UnsupportedFlavorException, IOException {
    DataContentHandler dataContentHandler = this.dch;
    if (dataContentHandler != null)
      return dataContentHandler.getTransferData(paramDataFlavor, paramDataSource); 
    if (paramDataFlavor.equals(getTransferDataFlavors()[0]))
      return paramDataSource.getInputStream(); 
    throw new UnsupportedFlavorException(paramDataFlavor);
  }
  
  public DataFlavor[] getTransferDataFlavors() {
    if (this.transferFlavors == null) {
      DataContentHandler dataContentHandler = this.dch;
      if (dataContentHandler != null) {
        this.transferFlavors = dataContentHandler.getTransferDataFlavors();
      } else {
        DataFlavor[] arrayOfDataFlavor = new DataFlavor[1];
        this.transferFlavors = arrayOfDataFlavor;
        arrayOfDataFlavor[0] = new ActivationDataFlavor(this.ds.getContentType(), this.ds.getContentType());
      } 
    } 
    return this.transferFlavors;
  }
  
  public void writeTo(Object paramObject, String paramString, OutputStream paramOutputStream) throws IOException {
    DataContentHandler dataContentHandler = this.dch;
    if (dataContentHandler != null) {
      dataContentHandler.writeTo(paramObject, paramString, paramOutputStream);
      return;
    } 
    paramObject = new StringBuilder("no DCH for content type ");
    paramObject.append(this.ds.getContentType());
    throw new UnsupportedDataTypeException(paramObject.toString());
  }
}
