package net.lingala.zip4j.headers;

public enum VersionMadeBy {
  SPECIFICATION_VERSION, UNIX, WINDOWS;
  
  private static final VersionMadeBy[] $VALUES;
  
  private final byte code;
  
  static {
    VersionMadeBy versionMadeBy1 = new VersionMadeBy("SPECIFICATION_VERSION", 0, (byte)51);
    SPECIFICATION_VERSION = versionMadeBy1;
    VersionMadeBy versionMadeBy2 = new VersionMadeBy("WINDOWS", 1, (byte)0);
    WINDOWS = versionMadeBy2;
    VersionMadeBy versionMadeBy3 = new VersionMadeBy("UNIX", 2, (byte)3);
    UNIX = versionMadeBy3;
    $VALUES = new VersionMadeBy[] { versionMadeBy1, versionMadeBy2, versionMadeBy3 };
  }
  
  VersionMadeBy(byte paramByte) {
    this.code = paramByte;
  }
  
  public byte getCode() {
    return this.code;
  }
}
