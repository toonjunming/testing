package myjava.awt.datatransfer;

import java.io.IOException;

public interface Transferable {
  Object getTransferData(DataFlavor paramDataFlavor) throws UnsupportedFlavorException, IOException;
  
  DataFlavor[] getTransferDataFlavors();
  
  boolean isDataFlavorSupported(DataFlavor paramDataFlavor);
}
