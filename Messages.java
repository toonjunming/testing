package com.mysql.jdbc;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
  private static final String BUNDLE_NAME = "com.mysql.jdbc.LocalizedErrorMessages";
  
  private static final ResourceBundle RESOURCE_BUNDLE;
  
  static {
    try {
      ResourceBundle resourceBundle = ResourceBundle.getBundle("com.mysql.jdbc.LocalizedErrorMessages", Locale.getDefault(), Messages.class.getClassLoader());
    } finally {
      Exception exception = null;
    } 
  }
  
  public static String getString(String paramString) {
    ResourceBundle resourceBundle = RESOURCE_BUNDLE;
    if (resourceBundle != null) {
      if (paramString != null)
        try {
          String str2 = resourceBundle.getString(paramString);
          String str1 = str2;
          if (str2 == null) {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("Missing error message for key '");
            stringBuilder.append(paramString);
            stringBuilder.append("'");
            str1 = stringBuilder.toString();
          } 
          return str1;
        } catch (MissingResourceException missingResourceException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append('!');
          stringBuilder.append(paramString);
          stringBuilder.append('!');
          return stringBuilder.toString();
        }  
      IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
      this("Message key can not be null");
      throw illegalArgumentException;
    } 
    throw new RuntimeException("Localized messages from resource bundle 'com.mysql.jdbc.LocalizedErrorMessages' not loaded during initialization of driver.");
  }
  
  public static String getString(String paramString, Object[] paramArrayOfObject) {
    return MessageFormat.format(getString(paramString), paramArrayOfObject);
  }
}
