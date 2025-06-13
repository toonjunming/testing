package net.lingala.zip4j.model;

import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionMethod;

public class AESExtraDataRecord extends ZipHeader {
  private AesKeyStrength aesKeyStrength;
  
  private AesVersion aesVersion;
  
  private CompressionMethod compressionMethod;
  
  private int dataSize;
  
  private String vendorID;
  
  public AESExtraDataRecord() {
    setSignature(HeaderSignature.AES_EXTRA_DATA_RECORD);
    this.dataSize = 7;
    this.aesVersion = AesVersion.TWO;
    this.vendorID = "AE";
    this.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256;
    this.compressionMethod = CompressionMethod.DEFLATE;
  }
  
  public AesKeyStrength getAesKeyStrength() {
    return this.aesKeyStrength;
  }
  
  public AesVersion getAesVersion() {
    return this.aesVersion;
  }
  
  public CompressionMethod getCompressionMethod() {
    return this.compressionMethod;
  }
  
  public int getDataSize() {
    return this.dataSize;
  }
  
  public String getVendorID() {
    return this.vendorID;
  }
  
  public void setAesKeyStrength(AesKeyStrength paramAesKeyStrength) {
    this.aesKeyStrength = paramAesKeyStrength;
  }
  
  public void setAesVersion(AesVersion paramAesVersion) {
    this.aesVersion = paramAesVersion;
  }
  
  public void setCompressionMethod(CompressionMethod paramCompressionMethod) {
    this.compressionMethod = paramCompressionMethod;
  }
  
  public void setDataSize(int paramInt) {
    this.dataSize = paramInt;
  }
  
  public void setVendorID(String paramString) {
    this.vendorID = paramString;
  }
}
