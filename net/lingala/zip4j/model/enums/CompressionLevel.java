package net.lingala.zip4j.model.enums;

public enum CompressionLevel {
  FAST, FASTER, FASTEST, HIGHER, MAXIMUM, MEDIUM_FAST, NORMAL, PRE_ULTRA, ULTRA;
  
  private static final CompressionLevel[] $VALUES;
  
  private int level;
  
  static {
    CompressionLevel compressionLevel6 = new CompressionLevel("FASTEST", 0, 1);
    FASTEST = compressionLevel6;
    CompressionLevel compressionLevel1 = new CompressionLevel("FASTER", 1, 2);
    FASTER = compressionLevel1;
    CompressionLevel compressionLevel2 = new CompressionLevel("FAST", 2, 3);
    FAST = compressionLevel2;
    CompressionLevel compressionLevel4 = new CompressionLevel("MEDIUM_FAST", 3, 4);
    MEDIUM_FAST = compressionLevel4;
    CompressionLevel compressionLevel5 = new CompressionLevel("NORMAL", 4, 5);
    NORMAL = compressionLevel5;
    CompressionLevel compressionLevel8 = new CompressionLevel("HIGHER", 5, 6);
    HIGHER = compressionLevel8;
    CompressionLevel compressionLevel3 = new CompressionLevel("MAXIMUM", 6, 7);
    MAXIMUM = compressionLevel3;
    CompressionLevel compressionLevel7 = new CompressionLevel("PRE_ULTRA", 7, 8);
    PRE_ULTRA = compressionLevel7;
    CompressionLevel compressionLevel9 = new CompressionLevel("ULTRA", 8, 9);
    ULTRA = compressionLevel9;
    $VALUES = new CompressionLevel[] { compressionLevel6, compressionLevel1, compressionLevel2, compressionLevel4, compressionLevel5, compressionLevel8, compressionLevel3, compressionLevel7, compressionLevel9 };
  }
  
  CompressionLevel(int paramInt1) {
    this.level = paramInt1;
  }
  
  public int getLevel() {
    return this.level;
  }
}
