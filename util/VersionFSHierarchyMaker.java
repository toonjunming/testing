package com.mysql.jdbc.util;

import com.mysql.jdbc.NonRegisteringDriver;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.Properties;

public class VersionFSHierarchyMaker {
  public static void main(String[] paramArrayOfString) throws Exception {
    String str2;
    StringBuilder stringBuilder3;
    if (paramArrayOfString.length < 3) {
      usage();
      System.exit(1);
    } 
    String str7 = removeWhitespaceChars(System.getProperty("java.version"));
    String str6 = removeWhitespaceChars(System.getProperty("java.vendor"));
    String str5 = removeWhitespaceChars(System.getProperty("os.name"));
    String str4 = removeWhitespaceChars(System.getProperty("os.arch"));
    String str3 = removeWhitespaceChars(System.getProperty("os.version"));
    String str8 = System.getProperty("com.mysql.jdbc.testsuite.url");
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("MySQL");
    stringBuilder1.append(paramArrayOfString[2]);
    stringBuilder1.append("_");
    String str9 = stringBuilder1.toString();
    try {
      Properties properties = new Properties();
      this();
      properties.setProperty("allowPublicKeyRetrieval", "true");
      NonRegisteringDriver nonRegisteringDriver = new NonRegisteringDriver();
      this();
      ResultSet resultSet = nonRegisteringDriver.connect(str8, properties).createStatement().executeQuery("SELECT VERSION()");
      resultSet.next();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(str9);
      stringBuilder.append(removeWhitespaceChars(resultSet.getString(1)));
      str2 = stringBuilder.toString();
    } finally {
      stringBuilder1 = null;
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str9);
      stringBuilder1.append("no-server-running-on-");
      stringBuilder1.append(removeWhitespaceChars(str8));
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(str5);
    stringBuilder2.append("-");
    stringBuilder2.append(str4);
    stringBuilder2.append("-");
    stringBuilder2.append(str3);
    str3 = stringBuilder2.toString();
    File file1 = new File(paramArrayOfString[0]);
    File file2 = new File(new File(new File(file1, str2), str3), str6);
    file2.mkdirs();
    str3 = null;
    String str1 = paramArrayOfString[1];
    try {
      FileOutputStream fileOutputStream = new FileOutputStream();
      this(str1);
    } finally {
      str1 = null;
    } 
    if (str2 != null) {
      str2.flush();
      str2.close();
    } 
    throw str1;
  }
  
  public static String removeWhitespaceChars(String paramString) {
    if (paramString == null)
      return paramString; 
    int i = paramString.length();
    StringBuilder stringBuilder = new StringBuilder(i);
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (!Character.isDigit(c) && !Character.isLetter(c)) {
        if (Character.isWhitespace(c)) {
          stringBuilder.append("_");
        } else {
          stringBuilder.append(".");
        } 
      } else {
        stringBuilder.append(c);
      } 
    } 
    return stringBuilder.toString();
  }
  
  private static void usage() {
    System.err.println("Creates a fs hierarchy representing MySQL version, OS version and JVM version.");
    System.err.println("Stores the full path as 'outputDirectory' property in file 'directoryPropPath'");
    System.err.println();
    System.err.println("Usage: java VersionFSHierarchyMaker baseDirectory directoryPropPath jdbcUrlIter");
  }
}
