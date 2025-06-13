package javax.activation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class MimeTypeParameterList {
  private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
  
  private Hashtable parameters = new Hashtable<Object, Object>();
  
  public MimeTypeParameterList() {}
  
  public MimeTypeParameterList(String paramString) throws MimeTypeParseException {
    parse(paramString);
  }
  
  private static boolean isTokenChar(char paramChar) {
    return (paramChar > ' ' && paramChar < '' && "()<>@,;:/[]?=\\\"".indexOf(paramChar) < 0);
  }
  
  private static String quote(String paramString) {
    int j = paramString.length();
    boolean bool = false;
    byte b = 0;
    int i = 0;
    while (b < j && !i) {
      i = isTokenChar(paramString.charAt(b)) ^ true;
      b++;
    } 
    if (i != 0) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.ensureCapacity((int)(j * 1.5D));
      stringBuffer.append('"');
      for (b = bool;; b++) {
        if (b >= j) {
          stringBuffer.append('"');
          return stringBuffer.toString();
        } 
        char c = paramString.charAt(b);
        if (c == '\\' || c == '"')
          stringBuffer.append('\\'); 
        stringBuffer.append(c);
      } 
    } 
    return paramString;
  }
  
  private static int skipWhiteSpace(String paramString, int paramInt) {
    int i = paramString.length();
    while (paramInt < i && Character.isWhitespace(paramString.charAt(paramInt)))
      paramInt++; 
    return paramInt;
  }
  
  private static String unquote(String paramString) {
    int i = paramString.length();
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.ensureCapacity(i);
    byte b = 0;
    boolean bool = false;
    while (true) {
      if (b >= i)
        return stringBuffer.toString(); 
      char c = paramString.charAt(b);
      if (!bool && c != '\\') {
        stringBuffer.append(c);
      } else if (bool) {
        stringBuffer.append(c);
        bool = false;
      } else {
        bool = true;
      } 
      b++;
    } 
  }
  
  public String get(String paramString) {
    return (String)this.parameters.get(paramString.trim().toLowerCase(Locale.ENGLISH));
  }
  
  public Enumeration getNames() {
    return this.parameters.keys();
  }
  
  public boolean isEmpty() {
    return this.parameters.isEmpty();
  }
  
  public void parse(String paramString) throws MimeTypeParseException {
    if (paramString == null)
      return; 
    int j = paramString.length();
    if (j <= 0)
      return; 
    int i = skipWhiteSpace(paramString, 0);
    while (i < j && paramString.charAt(i) == ';') {
      int k = skipWhiteSpace(paramString, i + 1);
      if (k >= j)
        return; 
      for (i = k; i < j && isTokenChar(paramString.charAt(i)); i++);
      String str = paramString.substring(k, i).toLowerCase(Locale.ENGLISH);
      i = skipWhiteSpace(paramString, i);
      if (i < j && paramString.charAt(i) == '=') {
        k = skipWhiteSpace(paramString, i + 1);
        if (k < j) {
          StringBuilder stringBuilder1;
          String str1;
          char c = paramString.charAt(k);
          if (c == '"') {
            int m = k + 1;
            if (m < j) {
              i = m;
              k = c;
              while (true) {
                if (i < j) {
                  k = paramString.charAt(i);
                  if (k != 34) {
                    int n = i;
                    if (k == 92)
                      n = i + 1; 
                    i = n + 1;
                    continue;
                  } 
                } 
                if (k == 34) {
                  str1 = unquote(paramString.substring(m, i));
                  i++;
                  break;
                } 
                throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
              } 
            } else {
              throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
            } 
          } else if (isTokenChar(c)) {
            for (i = k; i < j && isTokenChar(paramString.charAt(i)); i++);
            str1 = paramString.substring(k, i);
          } else {
            stringBuilder1 = new StringBuilder("Unexpected character encountered at index ");
            stringBuilder1.append(k);
            throw new MimeTypeParseException(stringBuilder1.toString());
          } 
          this.parameters.put(str, str1);
          i = skipWhiteSpace((String)stringBuilder1, i);
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder("Couldn't find a value for parameter named ");
        stringBuilder.append(str);
        throw new MimeTypeParseException(stringBuilder.toString());
      } 
      throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
    } 
    if (i >= j)
      return; 
    throw new MimeTypeParseException("More characters encountered in input than expected.");
  }
  
  public void remove(String paramString) {
    this.parameters.remove(paramString.trim().toLowerCase(Locale.ENGLISH));
  }
  
  public void set(String paramString1, String paramString2) {
    this.parameters.put(paramString1.trim().toLowerCase(Locale.ENGLISH), paramString2);
  }
  
  public int size() {
    return this.parameters.size();
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.ensureCapacity(this.parameters.size() * 16);
    Enumeration<String> enumeration = this.parameters.keys();
    while (true) {
      if (!enumeration.hasMoreElements())
        return stringBuffer.toString(); 
      String str = enumeration.nextElement();
      stringBuffer.append("; ");
      stringBuffer.append(str);
      stringBuffer.append('=');
      stringBuffer.append(quote((String)this.parameters.get(str)));
    } 
  }
}
