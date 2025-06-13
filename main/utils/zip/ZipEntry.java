package com.main.utils.zip;

import ILil.IL1Iii;
import java.util.Objects;

public class ZipEntry implements Cloneable {
  public long I1I = -1L;
  
  public String IL1Iii;
  
  public long ILil;
  
  public long Ilil = -1L;
  
  public long I丨L = -1L;
  
  public byte[] iI丨LLL1;
  
  public int l丨Li1LL = -1;
  
  public ZipEntry(String paramString) {
    Objects.requireNonNull(paramString);
    if (paramString.length() <= 65535) {
      this.IL1Iii = paramString;
      return;
    } 
    throw new IllegalArgumentException(IL1Iii.IL1Iii(new byte[] { 
            3, 15, 70, 23, 75, 69, 8, 0, 95, 0, 
            18, 17, 9, 14, 18, 9, 93, 11, 1 }, "fa2e2e"));
  }
  
  public void I1I(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length <= 65535) {
      this.iI丨LLL1 = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException(IL1Iii.IL1Iii(new byte[] { 
            8, 8, 69, 82, 13, 89, 5, 70, 86, 75, 
            21, 66, 0, 70, 85, 90, 4, 92, 5, 70, 
            95, 86, 15, 87, 21, 14 }, "af33a0"));
  }
  
  public String IL1Iii() {
    return this.IL1Iii;
  }
  
  public boolean ILil() {
    return this.IL1Iii.endsWith(IL1Iii.IL1Iii(new byte[] { 24 }, "793ba3"));
  }
  
  public Object clone() {
    try {
      ZipEntry zipEntry = (ZipEntry)super.clone();
      byte[] arrayOfByte = this.iI丨LLL1;
      if (arrayOfByte == null) {
        arrayOfByte = null;
      } else {
        arrayOfByte = (byte[])arrayOfByte.clone();
      } 
      zipEntry.iI丨LLL1 = arrayOfByte;
      return zipEntry;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new InternalError();
    } 
  }
  
  public int hashCode() {
    return this.IL1Iii.hashCode();
  }
  
  public String toString() {
    return IL1Iii();
  }
}
