package javax.activation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;

public class MimeType implements Externalizable {
  private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
  
  private MimeTypeParameterList parameters;
  
  private String primaryType;
  
  private String subType;
  
  public MimeType() {
    this.primaryType = "application";
    this.subType = "*";
    this.parameters = new MimeTypeParameterList();
  }
  
  public MimeType(String paramString) throws MimeTypeParseException {
    parse(paramString);
  }
  
  public MimeType(String paramString1, String paramString2) throws MimeTypeParseException {
    if (isValidToken(paramString1)) {
      Locale locale = Locale.ENGLISH;
      this.primaryType = paramString1.toLowerCase(locale);
      if (isValidToken(paramString2)) {
        this.subType = paramString2.toLowerCase(locale);
        this.parameters = new MimeTypeParameterList();
        return;
      } 
      throw new MimeTypeParseException("Sub type is invalid.");
    } 
    throw new MimeTypeParseException("Primary type is invalid.");
  }
  
  private static boolean isTokenChar(char paramChar) {
    return (paramChar > ' ' && paramChar < '' && "()<>@,;:/[]?=\\\"".indexOf(paramChar) < 0);
  }
  
  private boolean isValidToken(String paramString) {
    int i = paramString.length();
    if (i > 0)
      for (byte b = 0;; b++) {
        if (b >= i)
          return true; 
        if (!isTokenChar(paramString.charAt(b)))
          return false; 
      }  
    return false;
  }
  
  private void parse(String paramString) throws MimeTypeParseException {
    int j = paramString.indexOf('/');
    int i = paramString.indexOf(';');
    if (j >= 0 || i >= 0) {
      if (j >= 0 || i < 0) {
        if (j >= 0 && i < 0) {
          String str = paramString.substring(0, j).trim();
          Locale locale = Locale.ENGLISH;
          this.primaryType = str.toLowerCase(locale);
          this.subType = paramString.substring(j + 1).trim().toLowerCase(locale);
          this.parameters = new MimeTypeParameterList();
        } else if (j < i) {
          String str = paramString.substring(0, j).trim();
          Locale locale = Locale.ENGLISH;
          this.primaryType = str.toLowerCase(locale);
          this.subType = paramString.substring(j + 1, i).trim().toLowerCase(locale);
          this.parameters = new MimeTypeParameterList(paramString.substring(i));
        } else {
          throw new MimeTypeParseException("Unable to find a sub type.");
        } 
        if (isValidToken(this.primaryType)) {
          if (isValidToken(this.subType))
            return; 
          throw new MimeTypeParseException("Sub type is invalid.");
        } 
        throw new MimeTypeParseException("Primary type is invalid.");
      } 
      throw new MimeTypeParseException("Unable to find a sub type.");
    } 
    throw new MimeTypeParseException("Unable to find a sub type.");
  }
  
  public String getBaseType() {
    StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.primaryType));
    stringBuilder.append("/");
    stringBuilder.append(this.subType);
    return stringBuilder.toString();
  }
  
  public String getParameter(String paramString) {
    return this.parameters.get(paramString);
  }
  
  public MimeTypeParameterList getParameters() {
    return this.parameters;
  }
  
  public String getPrimaryType() {
    return this.primaryType;
  }
  
  public String getSubType() {
    return this.subType;
  }
  
  public boolean match(String paramString) throws MimeTypeParseException {
    return match(new MimeType(paramString));
  }
  
  public boolean match(MimeType paramMimeType) {
    return (this.primaryType.equals(paramMimeType.getPrimaryType()) && (this.subType.equals("*") || paramMimeType.getSubType().equals("*") || this.subType.equals(paramMimeType.getSubType())));
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    try {
      parse(paramObjectInput.readUTF());
      return;
    } catch (MimeTypeParseException mimeTypeParseException) {
      throw new IOException(mimeTypeParseException.toString());
    } 
  }
  
  public void removeParameter(String paramString) {
    this.parameters.remove(paramString);
  }
  
  public void setParameter(String paramString1, String paramString2) {
    this.parameters.set(paramString1, paramString2);
  }
  
  public void setPrimaryType(String paramString) throws MimeTypeParseException {
    if (isValidToken(this.primaryType)) {
      this.primaryType = paramString.toLowerCase(Locale.ENGLISH);
      return;
    } 
    throw new MimeTypeParseException("Primary type is invalid.");
  }
  
  public void setSubType(String paramString) throws MimeTypeParseException {
    if (isValidToken(this.subType)) {
      this.subType = paramString.toLowerCase(Locale.ENGLISH);
      return;
    } 
    throw new MimeTypeParseException("Sub type is invalid.");
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(String.valueOf(getBaseType()));
    stringBuilder.append(this.parameters.toString());
    return stringBuilder.toString();
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeUTF(toString());
    paramObjectOutput.flush();
  }
}
