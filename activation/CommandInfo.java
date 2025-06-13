package javax.activation;

import java.beans.Beans;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class CommandInfo {
  private String className;
  
  private String verb;
  
  public CommandInfo(String paramString1, String paramString2) {
    this.verb = paramString1;
    this.className = paramString2;
  }
  
  public String getCommandClass() {
    return this.className;
  }
  
  public String getCommandName() {
    return this.verb;
  }
  
  public Object getCommandObject(DataHandler paramDataHandler, ClassLoader paramClassLoader) throws IOException, ClassNotFoundException {
    Object object = Beans.instantiate(paramClassLoader, this.className);
    if (object != null)
      if (object instanceof CommandObject) {
        ((CommandObject)object).setCommandContext(this.verb, paramDataHandler);
      } else if (object instanceof Externalizable && paramDataHandler != null) {
        InputStream inputStream = paramDataHandler.getInputStream();
        if (inputStream != null)
          ((Externalizable)object).readExternal(new ObjectInputStream(inputStream)); 
      }  
    return object;
  }
}
