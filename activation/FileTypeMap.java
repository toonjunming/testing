package javax.activation;

import java.io.File;

public abstract class FileTypeMap {
  private static FileTypeMap defaultMap;
  
  public static FileTypeMap getDefaultFileTypeMap() {
    if (defaultMap == null)
      defaultMap = new MimetypesFileTypeMap(); 
    return defaultMap;
  }
  
  public static void setDefaultFileTypeMap(FileTypeMap paramFileTypeMap) {
    SecurityManager securityManager = System.getSecurityManager();
    if (securityManager != null)
      try {
        securityManager.checkSetFactory();
      } catch (SecurityException securityException) {
        if (FileTypeMap.class.getClassLoader() != paramFileTypeMap.getClass().getClassLoader())
          throw securityException; 
      }  
    defaultMap = paramFileTypeMap;
  }
  
  public abstract String getContentType(File paramFile);
  
  public abstract String getContentType(String paramString);
}
