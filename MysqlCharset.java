package com.mysql.jdbc;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MysqlCharset {
  public final String charsetName;
  
  public final List<String> javaEncodingsUc = new ArrayList<String>();
  
  public int major = 4;
  
  public final int mblen;
  
  public int minor = 1;
  
  public final int priority;
  
  public int subminor;
  
  public MysqlCharset(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString) {
    boolean bool = false;
    this.subminor = 0;
    this.charsetName = paramString;
    this.mblen = paramInt1;
    this.priority = paramInt2;
    for (paramInt2 = bool; paramInt2 < paramArrayOfString.length; paramInt2++) {
      paramString = paramArrayOfString[paramInt2];
      try {
        Charset charset = Charset.forName(paramString);
        addEncodingMapping(charset.name());
        Iterator<String> iterator = charset.aliases().iterator();
        while (iterator.hasNext())
          addEncodingMapping(iterator.next()); 
      } catch (Exception exception) {
        if (paramInt1 == 1)
          addEncodingMapping(paramString); 
      } 
    } 
    if (this.javaEncodingsUc.size() == 0)
      if (paramInt1 > 1) {
        addEncodingMapping("UTF-8");
      } else {
        addEncodingMapping("Cp1252");
      }  
  }
  
  public MysqlCharset(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString, int paramInt3, int paramInt4) {
    this(paramString, paramInt1, paramInt2, paramArrayOfString);
    this.major = paramInt3;
    this.minor = paramInt4;
  }
  
  public MysqlCharset(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString, int paramInt3, int paramInt4, int paramInt5) {
    this(paramString, paramInt1, paramInt2, paramArrayOfString);
    this.major = paramInt3;
    this.minor = paramInt4;
    this.subminor = paramInt5;
  }
  
  private void addEncodingMapping(String paramString) {
    paramString = paramString.toUpperCase(Locale.ENGLISH);
    if (!this.javaEncodingsUc.contains(paramString))
      this.javaEncodingsUc.add(paramString); 
  }
  
  public String getMatchingJavaEncoding(String paramString) {
    return (paramString != null && this.javaEncodingsUc.contains(paramString.toUpperCase(Locale.ENGLISH))) ? paramString : this.javaEncodingsUc.get(0);
  }
  
  public boolean isOkayForVersion(Connection paramConnection) throws SQLException {
    return paramConnection.versionMeetsMinimum(this.major, this.minor, this.subminor);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    stringBuilder.append("charsetName=");
    stringBuilder.append(this.charsetName);
    stringBuilder.append(",mblen=");
    stringBuilder.append(this.mblen);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}
