package javax.activation;

import myjava.awt.datatransfer.DataFlavor;

public class ActivationDataFlavor extends DataFlavor {
  private String humanPresentableName = null;
  
  private MimeType mimeObject = null;
  
  private String mimeType = null;
  
  private Class representationClass = null;
  
  public ActivationDataFlavor(Class<?> paramClass, String paramString) {
    super(paramClass, paramString);
    this.mimeType = super.getMimeType();
    this.representationClass = paramClass;
    this.humanPresentableName = paramString;
  }
  
  public ActivationDataFlavor(Class paramClass, String paramString1, String paramString2) {
    super(paramString1, paramString2);
    this.mimeType = paramString1;
    this.humanPresentableName = paramString2;
    this.representationClass = paramClass;
  }
  
  public ActivationDataFlavor(String paramString1, String paramString2) {
    super(paramString1, paramString2);
    this.mimeType = paramString1;
    try {
      this.representationClass = Class.forName("java.io.InputStream");
    } catch (ClassNotFoundException classNotFoundException) {}
    this.humanPresentableName = paramString2;
  }
  
  public boolean equals(DataFlavor paramDataFlavor) {
    return (isMimeTypeEqual(paramDataFlavor) && paramDataFlavor.getRepresentationClass() == this.representationClass);
  }
  
  public String getHumanPresentableName() {
    return this.humanPresentableName;
  }
  
  public String getMimeType() {
    return this.mimeType;
  }
  
  public Class getRepresentationClass() {
    return this.representationClass;
  }
  
  public boolean isMimeTypeEqual(String paramString) {
    try {
      if (this.mimeObject == null) {
        MimeType mimeType1 = new MimeType();
        this(this.mimeType);
        this.mimeObject = mimeType1;
      } 
      MimeType mimeType = new MimeType(paramString);
      return this.mimeObject.match(mimeType);
    } catch (MimeTypeParseException mimeTypeParseException) {
      return this.mimeType.equalsIgnoreCase(paramString);
    } 
  }
  
  public String normalizeMimeType(String paramString) {
    return paramString;
  }
  
  public String normalizeMimeTypeParameter(String paramString1, String paramString2) {
    return paramString2;
  }
  
  public void setHumanPresentableName(String paramString) {
    this.humanPresentableName = paramString;
  }
}
