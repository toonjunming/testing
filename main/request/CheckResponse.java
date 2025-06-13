package com.main.request;

import com.google.gson.annotations.SerializedName;

public class CheckResponse {
  @SerializedName("code")
  private int code;
  
  @SerializedName("data")
  private IL1Iii data;
  
  @SerializedName("msg")
  private String msg;
  
  public int getCode() {
    return this.code;
  }
  
  public IL1Iii getData() {
    return this.data;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public class IL1Iii {
    @SerializedName("pkid")
    private String I1I;
    
    @SerializedName("timestamp")
    private long IL1Iii;
    
    @SerializedName("sign")
    private String ILil;
    
    @SerializedName("encryptBizString")
    private String Ilil;
    
    @SerializedName("nonce")
    private String I丨L;
    
    @SerializedName("key")
    private String l丨Li1LL;
    
    public String I1I() {
      return this.I1I;
    }
    
    public String IL1Iii() {
      return this.Ilil;
    }
    
    public String ILil() {
      return this.l丨Li1LL;
    }
  }
}
