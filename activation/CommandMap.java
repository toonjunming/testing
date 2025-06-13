package javax.activation;

public abstract class CommandMap {
  private static CommandMap defaultCommandMap;
  
  public static CommandMap getDefaultCommandMap() {
    if (defaultCommandMap == null)
      defaultCommandMap = new MailcapCommandMap(); 
    return defaultCommandMap;
  }
  
  public static void setDefaultCommandMap(CommandMap paramCommandMap) {
    SecurityManager securityManager = System.getSecurityManager();
    if (securityManager != null)
      try {
        securityManager.checkSetFactory();
      } catch (SecurityException securityException) {
        if (CommandMap.class.getClassLoader() != paramCommandMap.getClass().getClassLoader())
          throw securityException; 
      }  
    defaultCommandMap = paramCommandMap;
  }
  
  public abstract DataContentHandler createDataContentHandler(String paramString);
  
  public DataContentHandler createDataContentHandler(String paramString, DataSource paramDataSource) {
    return createDataContentHandler(paramString);
  }
  
  public abstract CommandInfo[] getAllCommands(String paramString);
  
  public CommandInfo[] getAllCommands(String paramString, DataSource paramDataSource) {
    return getAllCommands(paramString);
  }
  
  public abstract CommandInfo getCommand(String paramString1, String paramString2);
  
  public CommandInfo getCommand(String paramString1, String paramString2, DataSource paramDataSource) {
    return getCommand(paramString1, paramString2);
  }
  
  public String[] getMimeTypes() {
    return null;
  }
  
  public abstract CommandInfo[] getPreferredCommands(String paramString);
  
  public CommandInfo[] getPreferredCommands(String paramString, DataSource paramDataSource) {
    return getPreferredCommands(paramString);
  }
}
