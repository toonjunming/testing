package net.lingala.zip4j.model.enums;

public enum EncryptionMethod {
  AES, NONE, ZIP_STANDARD, ZIP_STANDARD_VARIANT_STRONG;
  
  private static final EncryptionMethod[] $VALUES;
  
  static {
    EncryptionMethod encryptionMethod3 = new EncryptionMethod("NONE", 0);
    NONE = encryptionMethod3;
    EncryptionMethod encryptionMethod1 = new EncryptionMethod("ZIP_STANDARD", 1);
    ZIP_STANDARD = encryptionMethod1;
    EncryptionMethod encryptionMethod4 = new EncryptionMethod("ZIP_STANDARD_VARIANT_STRONG", 2);
    ZIP_STANDARD_VARIANT_STRONG = encryptionMethod4;
    EncryptionMethod encryptionMethod2 = new EncryptionMethod("AES", 3);
    AES = encryptionMethod2;
    $VALUES = new EncryptionMethod[] { encryptionMethod3, encryptionMethod1, encryptionMethod4, encryptionMethod2 };
  }
}
