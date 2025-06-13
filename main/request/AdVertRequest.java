package com.main.request;

import com.google.gson.annotations.SerializedName;

public class AdVertRequest {
  @SerializedName("isapp")
  private int isapp;
  
  @SerializedName("packageName")
  private String packageName;
  
  @SerializedName("uphash")
  private String uphash;
  
  public AdVertRequest(String paramString1, String paramString2, int paramInt) {
    this.packageName = paramString1;
    this.uphash = paramString2;
    this.isapp = paramInt;
  }
}
