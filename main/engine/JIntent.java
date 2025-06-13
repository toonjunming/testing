package com.main.engine;

import java.util.Map;

public class JIntent {
  private String action;
  
  private String classname;
  
  private String data;
  
  private String datapart;
  
  private Map<String, Object> extra;
  
  private String packageName;
  
  private String uri;
  
  public String getAction() {
    return this.action;
  }
  
  public String getClassname() {
    return this.classname;
  }
  
  public String getData() {
    return this.data;
  }
  
  public String getDatapart() {
    return this.datapart;
  }
  
  public Map<String, Object> getExtra() {
    return this.extra;
  }
  
  public String getPackageName() {
    return this.packageName;
  }
  
  public String getUri() {
    return this.uri;
  }
  
  public void setAction(String paramString) {
    this.action = paramString;
  }
  
  public void setClassname(String paramString) {
    this.classname = paramString;
  }
  
  public void setData(String paramString) {
    this.data = paramString;
  }
  
  public void setDatapart(String paramString) {
    this.datapart = paramString;
  }
  
  public void setExtra(Map<String, Object> paramMap) {
    this.extra = paramMap;
  }
  
  public void setPackageName(String paramString) {
    this.packageName = paramString;
  }
  
  public void setUri(String paramString) {
    this.uri = paramString;
  }
}
