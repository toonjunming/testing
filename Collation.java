package com.mysql.jdbc;

public class Collation {
  public final String collationName;
  
  public final int index;
  
  public final MysqlCharset mysqlCharset;
  
  public final int priority;
  
  public Collation(int paramInt1, String paramString1, int paramInt2, String paramString2) {
    this.index = paramInt1;
    this.collationName = paramString1;
    this.priority = paramInt2;
    this.mysqlCharset = CharsetMapping.CHARSET_NAME_TO_CHARSET.get(paramString2);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    stringBuilder.append("index=");
    stringBuilder.append(this.index);
    stringBuilder.append(",collationName=");
    stringBuilder.append(this.collationName);
    stringBuilder.append(",charsetName=");
    stringBuilder.append(this.mysqlCharset.charsetName);
    stringBuilder.append(",javaCharsetName=");
    stringBuilder.append(this.mysqlCharset.getMatchingJavaEncoding(null));
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}
