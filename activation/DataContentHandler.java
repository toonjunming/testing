package javax.activation;

import java.io.IOException;
import java.io.OutputStream;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.UnsupportedFlavorException;

public interface DataContentHandler {
  Object getContent(DataSource paramDataSource) throws IOException;
  
  Object getTransferData(DataFlavor paramDataFlavor, DataSource paramDataSource) throws UnsupportedFlavorException, IOException;
  
  DataFlavor[] getTransferDataFlavors();
  
  void writeTo(Object paramObject, String paramString, OutputStream paramOutputStream) throws IOException;
}
