package com.mysql.jdbc.util;

import com.mysql.jdbc.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ServerController {
  public static final String BASEDIR_KEY = "basedir";
  
  public static final String DATADIR_KEY = "datadir";
  
  public static final String DEFAULTS_FILE_KEY = "defaults-file";
  
  public static final String EXECUTABLE_NAME_KEY = "executable";
  
  public static final String EXECUTABLE_PATH_KEY = "executablePath";
  
  private Process serverProcess = null;
  
  private Properties serverProps = null;
  
  private Properties systemProps = null;
  
  public ServerController(String paramString) {
    setBaseDir(paramString);
  }
  
  public ServerController(String paramString1, String paramString2) {}
  
  private String buildOptionalCommandLine() {
    StringBuilder stringBuilder = new StringBuilder();
    Properties properties = this.serverProps;
    if (properties != null)
      for (String str1 : properties.keySet()) {
        String str2 = this.serverProps.getProperty(str1);
        if (!isNonCommandLineArgument(str1)) {
          if (str2 != null && str2.length() > 0) {
            stringBuilder.append(" \"");
            stringBuilder.append("--");
            stringBuilder.append(str1);
            stringBuilder.append("=");
            stringBuilder.append(str2);
            stringBuilder.append("\"");
            continue;
          } 
          stringBuilder.append(" --");
          stringBuilder.append(str1);
        } 
      }  
    return stringBuilder.toString();
  }
  
  private String getCommandLine() {
    StringBuilder stringBuilder = new StringBuilder(getFullExecutablePath());
    stringBuilder.append(buildOptionalCommandLine());
    return stringBuilder.toString();
  }
  
  private String getFullExecutablePath() {
    StringBuilder stringBuilder = new StringBuilder();
    String str = getServerProps().getProperty("executablePath");
    if (str == null) {
      str = getServerProps().getProperty("basedir");
      stringBuilder.append(str);
      if (!str.endsWith(File.separator))
        stringBuilder.append(File.separatorChar); 
      if (runningOnWindows()) {
        stringBuilder.append("bin");
      } else {
        stringBuilder.append("libexec");
      } 
      stringBuilder.append(File.separatorChar);
    } else {
      stringBuilder.append(str);
      if (!str.endsWith(File.separator))
        stringBuilder.append(File.separatorChar); 
    } 
    stringBuilder.append(getServerProps().getProperty("executable", "mysqld"));
    return stringBuilder.toString();
  }
  
  private Properties getSystemProperties() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield systemProps : Ljava/util/Properties;
    //   6: ifnonnull -> 16
    //   9: aload_0
    //   10: invokestatic getProperties : ()Ljava/util/Properties;
    //   13: putfield systemProps : Ljava/util/Properties;
    //   16: aload_0
    //   17: getfield systemProps : Ljava/util/Properties;
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: areturn
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	25	finally
    //   16	21	25	finally
  }
  
  private boolean isNonCommandLineArgument(String paramString) {
    return (paramString.equals("executable") || paramString.equals("executablePath"));
  }
  
  private boolean runningOnWindows() {
    boolean bool;
    if (StringUtils.indexOfIgnoreCase(getSystemProperties().getProperty("os.name"), "WINDOWS") != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void forceStop() {
    Process process = this.serverProcess;
    if (process != null) {
      process.destroy();
      this.serverProcess = null;
    } 
  }
  
  public Properties getServerProps() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield serverProps : Ljava/util/Properties;
    //   6: ifnonnull -> 22
    //   9: new java/util/Properties
    //   12: astore_1
    //   13: aload_1
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: aload_1
    //   19: putfield serverProps : Ljava/util/Properties;
    //   22: aload_0
    //   23: getfield serverProps : Ljava/util/Properties;
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: areturn
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	31	finally
    //   22	27	31	finally
  }
  
  public void setBaseDir(String paramString) {
    getServerProps().setProperty("basedir", paramString);
  }
  
  public void setDataDir(String paramString) {
    getServerProps().setProperty("datadir", paramString);
  }
  
  public Process start() throws IOException {
    if (this.serverProcess == null) {
      Process process = Runtime.getRuntime().exec(getCommandLine());
      this.serverProcess = process;
      return process;
    } 
    throw new IllegalArgumentException("Server already started");
  }
  
  public void stop(boolean paramBoolean) throws IOException {
    if (this.serverProcess != null) {
      String str1 = getServerProps().getProperty("basedir");
      StringBuilder stringBuilder = new StringBuilder(str1);
      String str2 = File.separator;
      if (!str1.endsWith(str2))
        stringBuilder.append(str2); 
      stringBuilder.append("bin");
      stringBuilder.append(str2);
      stringBuilder.append("mysqladmin shutdown");
      System.out.println(stringBuilder.toString());
      Process process = Runtime.getRuntime().exec(stringBuilder.toString());
      int i = -1;
      try {
        int j = process.waitFor();
        i = j;
      } catch (InterruptedException interruptedException) {}
      if (i != 0 && paramBoolean)
        forceStop(); 
    } 
  }
}
