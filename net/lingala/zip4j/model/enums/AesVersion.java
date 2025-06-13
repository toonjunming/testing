package net.lingala.zip4j.model.enums;

public enum AesVersion {
  ONE, TWO;
  
  private static final AesVersion[] $VALUES;
  
  private int versionNumber;
  
  static {
    AesVersion aesVersion2 = new AesVersion("ONE", 0, 1);
    ONE = aesVersion2;
    AesVersion aesVersion1 = new AesVersion("TWO", 1, 2);
    TWO = aesVersion1;
    $VALUES = new AesVersion[] { aesVersion2, aesVersion1 };
  }
  
  AesVersion(int paramInt1) {
    this.versionNumber = paramInt1;
  }
  
  public static AesVersion getFromVersionNumber(int paramInt) {
    for (AesVersion aesVersion : values()) {
      if (aesVersion.versionNumber == paramInt)
        return aesVersion; 
    } 
    throw new IllegalArgumentException("Unsupported Aes version");
  }
  
  public int getVersionNumber() {
    return this.versionNumber;
  }
}
