package net.lingala.zip4j.model.enums;

public enum RandomAccessFileMode {
  READ, WRITE;
  
  private static final RandomAccessFileMode[] $VALUES;
  
  private String value;
  
  static {
    RandomAccessFileMode randomAccessFileMode2 = new RandomAccessFileMode("READ", 0, "r");
    READ = randomAccessFileMode2;
    RandomAccessFileMode randomAccessFileMode1 = new RandomAccessFileMode("WRITE", 1, "rw");
    WRITE = randomAccessFileMode1;
    $VALUES = new RandomAccessFileMode[] { randomAccessFileMode2, randomAccessFileMode1 };
  }
  
  RandomAccessFileMode(String paramString1) {
    this.value = paramString1;
  }
  
  public String getValue() {
    return this.value;
  }
}
