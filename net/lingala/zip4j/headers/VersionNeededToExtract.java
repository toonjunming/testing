package net.lingala.zip4j.headers;

public enum VersionNeededToExtract {
  AES_ENCRYPTED, DEFAULT, DEFLATE_COMPRESSED, ZIP_64_FORMAT;
  
  private static final VersionNeededToExtract[] $VALUES;
  
  private final int code;
  
  static {
    VersionNeededToExtract versionNeededToExtract3 = new VersionNeededToExtract("DEFAULT", 0, 10);
    DEFAULT = versionNeededToExtract3;
    VersionNeededToExtract versionNeededToExtract1 = new VersionNeededToExtract("DEFLATE_COMPRESSED", 1, 20);
    DEFLATE_COMPRESSED = versionNeededToExtract1;
    VersionNeededToExtract versionNeededToExtract4 = new VersionNeededToExtract("ZIP_64_FORMAT", 2, 45);
    ZIP_64_FORMAT = versionNeededToExtract4;
    VersionNeededToExtract versionNeededToExtract2 = new VersionNeededToExtract("AES_ENCRYPTED", 3, 51);
    AES_ENCRYPTED = versionNeededToExtract2;
    $VALUES = new VersionNeededToExtract[] { versionNeededToExtract3, versionNeededToExtract1, versionNeededToExtract4, versionNeededToExtract2 };
  }
  
  VersionNeededToExtract(int paramInt1) {
    this.code = paramInt1;
  }
  
  public int getCode() {
    return this.code;
  }
}
