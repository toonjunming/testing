package com.main.request;

import com.google.gson.annotations.SerializedName;

public class CheckRequest {
  @SerializedName("imei")
  private String imei;
  
  @SerializedName("packageName")
  private String packageName;
  
  @SerializedName("uphash")
  private String uphash;
  
  public CheckRequest(String paramString1, String paramString2, String paramString3) {
    this.packageName = paramString1;
    this.uphash = paramString2;
    this.imei = paramString3;
  }
}
