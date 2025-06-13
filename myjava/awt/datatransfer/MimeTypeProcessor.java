package myjava.awt.datatransfer;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public final class MimeTypeProcessor {
  private static MimeTypeProcessor instance;
  
  public static String assemble(MimeType paramMimeType) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramMimeType.getFullType());
    Enumeration<String> enumeration = paramMimeType.parameters.keys();
    while (true) {
      if (!enumeration.hasMoreElements())
        return stringBuilder.toString(); 
      String str2 = enumeration.nextElement();
      String str1 = (String)paramMimeType.parameters.get(str2);
      stringBuilder.append("; ");
      stringBuilder.append(str2);
      stringBuilder.append("=\"");
      stringBuilder.append(str1);
      stringBuilder.append('"');
    } 
  }
  
  private static int getNextMeaningfulIndex(String paramString, int paramInt) {
    while (paramInt < paramString.length() && !isMeaningfulChar(paramString.charAt(paramInt)))
      paramInt++; 
    return paramInt;
  }
  
  private static boolean isMeaningfulChar(char paramChar) {
    return (paramChar >= '!' && paramChar <= '~');
  }
  
  private static boolean isTSpecialChar(char paramChar) {
    return !(paramChar != '(' && paramChar != ')' && paramChar != '[' && paramChar != ']' && paramChar != '<' && paramChar != '>' && paramChar != '@' && paramChar != ',' && paramChar != ';' && paramChar != ':' && paramChar != '\\' && paramChar != '"' && paramChar != '/' && paramChar != '?' && paramChar != '=');
  }
  
  public static MimeType parse(String paramString) {
    if (instance == null)
      instance = new MimeTypeProcessor(); 
    MimeType mimeType = new MimeType();
    if (paramString != null) {
      StringPosition stringPosition = new StringPosition(null);
      retrieveType(paramString, mimeType, stringPosition);
      retrieveParams(paramString, mimeType, stringPosition);
    } 
    return mimeType;
  }
  
  private static void retrieveParam(String paramString, MimeType paramMimeType, StringPosition paramStringPosition) {
    String str = retrieveToken(paramString, paramStringPosition).toLowerCase();
    int i = getNextMeaningfulIndex(paramString, paramStringPosition.i);
    paramStringPosition.i = i;
    if (i < paramString.length() && paramString.charAt(paramStringPosition.i) == '=') {
      i = paramStringPosition.i + 1;
      paramStringPosition.i = i;
      i = getNextMeaningfulIndex(paramString, i);
      paramStringPosition.i = i;
      if (i < paramString.length()) {
        if (paramString.charAt(paramStringPosition.i) == '"') {
          paramString = retrieveQuoted(paramString, paramStringPosition);
        } else {
          paramString = retrieveToken(paramString, paramStringPosition);
        } 
        paramMimeType.parameters.put(str, paramString);
        return;
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  private static void retrieveParams(String paramString, MimeType paramMimeType, StringPosition paramStringPosition) {
    paramMimeType.parameters = (Hashtable)new Hashtable<Object, Object>();
    paramMimeType.systemParameters = (Hashtable)new Hashtable<Object, Object>();
    while (true) {
      int i = getNextMeaningfulIndex(paramString, paramStringPosition.i);
      paramStringPosition.i = i;
      if (i >= paramString.length())
        return; 
      if (paramString.charAt(paramStringPosition.i) == ';') {
        paramStringPosition.i++;
        retrieveParam(paramString, paramMimeType, paramStringPosition);
        continue;
      } 
      throw new IllegalArgumentException();
    } 
  }
  
  private static String retrieveQuoted(String paramString, StringPosition paramStringPosition) {
    StringBuilder stringBuilder = new StringBuilder();
    paramStringPosition.i++;
    boolean bool = true;
    while (true) {
      if (paramString.charAt(paramStringPosition.i) == '"' && bool) {
        paramStringPosition.i++;
        return stringBuilder.toString();
      } 
      int i = paramStringPosition.i;
      paramStringPosition.i = i + 1;
      char c = paramString.charAt(i);
      if (!bool) {
        bool = true;
      } else if (c == '\\') {
        bool = false;
      } 
      if (bool)
        stringBuilder.append(c); 
      if (paramStringPosition.i != paramString.length())
        continue; 
      throw new IllegalArgumentException();
    } 
  }
  
  private static String retrieveToken(String paramString, StringPosition paramStringPosition) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = getNextMeaningfulIndex(paramString, paramStringPosition.i);
    paramStringPosition.i = i;
    if (i < paramString.length() && !isTSpecialChar(paramString.charAt(paramStringPosition.i))) {
      do {
        i = paramStringPosition.i;
        paramStringPosition.i = i + 1;
        stringBuilder.append(paramString.charAt(i));
      } while (paramStringPosition.i < paramString.length() && isMeaningfulChar(paramString.charAt(paramStringPosition.i)) && !isTSpecialChar(paramString.charAt(paramStringPosition.i)));
      return stringBuilder.toString();
    } 
    throw new IllegalArgumentException();
  }
  
  private static void retrieveType(String paramString, MimeType paramMimeType, StringPosition paramStringPosition) {
    paramMimeType.primaryType = retrieveToken(paramString, paramStringPosition).toLowerCase();
    int i = getNextMeaningfulIndex(paramString, paramStringPosition.i);
    paramStringPosition.i = i;
    if (i < paramString.length() && paramString.charAt(paramStringPosition.i) == '/') {
      paramStringPosition.i++;
      paramMimeType.subType = retrieveToken(paramString, paramStringPosition).toLowerCase();
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public static final class MimeType implements Cloneable, Serializable {
    private static final long serialVersionUID = -6693571907475992044L;
    
    private Hashtable<String, String> parameters = null;
    
    private String primaryType = null;
    
    private String subType = null;
    
    private Hashtable<String, Object> systemParameters = null;
    
    public MimeType() {}
    
    public MimeType(String param1String1, String param1String2) {}
    
    public void addParameter(String param1String1, String param1String2) {
      if (param1String2 == null)
        return; 
      String str = param1String2;
      if (param1String2.charAt(0) == '"') {
        str = param1String2;
        if (param1String2.charAt(param1String2.length() - 1) == '"')
          str = param1String2.substring(1, param1String2.length() - 2); 
      } 
      if (str.length() == 0)
        return; 
      this.parameters.put(param1String1, str);
    }
    
    public void addSystemParameter(String param1String, Object param1Object) {
      this.systemParameters.put(param1String, param1Object);
    }
    
    public Object clone() {
      MimeType mimeType = new MimeType(this.primaryType, this.subType);
      mimeType.parameters = (Hashtable<String, String>)this.parameters.clone();
      mimeType.systemParameters = (Hashtable<String, Object>)this.systemParameters.clone();
      return mimeType;
    }
    
    public boolean equals(MimeType param1MimeType) {
      return (param1MimeType == null) ? false : getFullType().equals(param1MimeType.getFullType());
    }
    
    public String getFullType() {
      StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.primaryType));
      stringBuilder.append("/");
      stringBuilder.append(this.subType);
      return stringBuilder.toString();
    }
    
    public String getParameter(String param1String) {
      return this.parameters.get(param1String);
    }
    
    public String getPrimaryType() {
      return this.primaryType;
    }
    
    public String getSubType() {
      return this.subType;
    }
    
    public Object getSystemParameter(String param1String) {
      return this.systemParameters.get(param1String);
    }
    
    public void removeParameter(String param1String) {
      this.parameters.remove(param1String);
    }
  }
  
  public static final class StringPosition {
    public int i = 0;
    
    private StringPosition() {}
  }
}
