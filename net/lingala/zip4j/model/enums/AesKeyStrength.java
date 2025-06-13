package net.lingala.zip4j.model.enums;

public enum AesKeyStrength {
  KEY_STRENGTH_128, KEY_STRENGTH_192, KEY_STRENGTH_256;
  
  private static final AesKeyStrength[] $VALUES;
  
  private int keyLength;
  
  private int macLength;
  
  private int rawCode;
  
  private int saltLength;
  
  static {
    AesKeyStrength aesKeyStrength1 = new AesKeyStrength("KEY_STRENGTH_128", 0, 1, 8, 16, 16);
    KEY_STRENGTH_128 = aesKeyStrength1;
    AesKeyStrength aesKeyStrength2 = new AesKeyStrength("KEY_STRENGTH_192", 1, 2, 12, 24, 24);
    KEY_STRENGTH_192 = aesKeyStrength2;
    AesKeyStrength aesKeyStrength3 = new AesKeyStrength("KEY_STRENGTH_256", 2, 3, 16, 32, 32);
    KEY_STRENGTH_256 = aesKeyStrength3;
    $VALUES = new AesKeyStrength[] { aesKeyStrength1, aesKeyStrength2, aesKeyStrength3 };
  }
  
  AesKeyStrength(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.rawCode = paramInt1;
    this.saltLength = paramInt2;
    this.macLength = paramInt3;
    this.keyLength = paramInt4;
  }
  
  public static AesKeyStrength getAesKeyStrengthFromRawCode(int paramInt) {
    for (AesKeyStrength aesKeyStrength : values()) {
      if (aesKeyStrength.getRawCode() == paramInt)
        return aesKeyStrength; 
    } 
    return null;
  }
  
  public int getKeyLength() {
    return this.keyLength;
  }
  
  public int getMacLength() {
    return this.macLength;
  }
  
  public int getRawCode() {
    return this.rawCode;
  }
  
  public int getSaltLength() {
    return this.saltLength;
  }
}
