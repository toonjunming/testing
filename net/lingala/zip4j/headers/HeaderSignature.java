package net.lingala.zip4j.headers;

public enum HeaderSignature {
  AES_EXTRA_DATA_RECORD, ARCEXTDATREC, CENTRAL_DIRECTORY, DIGITAL_SIGNATURE, END_OF_CENTRAL_DIRECTORY, EXTRA_DATA_RECORD, LOCAL_FILE_HEADER, SPLIT_ZIP, TEMPORARY_SPANNING_MARKER, ZIP64_END_CENTRAL_DIRECTORY_LOCATOR, ZIP64_END_CENTRAL_DIRECTORY_RECORD, ZIP64_EXTRA_FIELD_SIGNATURE;
  
  private static final HeaderSignature[] $VALUES;
  
  private long value;
  
  static {
    HeaderSignature headerSignature10 = new HeaderSignature("LOCAL_FILE_HEADER", 0, 67324752L);
    LOCAL_FILE_HEADER = headerSignature10;
    HeaderSignature headerSignature8 = new HeaderSignature("EXTRA_DATA_RECORD", 1, 134695760L);
    EXTRA_DATA_RECORD = headerSignature8;
    HeaderSignature headerSignature4 = new HeaderSignature("CENTRAL_DIRECTORY", 2, 33639248L);
    CENTRAL_DIRECTORY = headerSignature4;
    HeaderSignature headerSignature6 = new HeaderSignature("END_OF_CENTRAL_DIRECTORY", 3, 101010256L);
    END_OF_CENTRAL_DIRECTORY = headerSignature6;
    HeaderSignature headerSignature1 = new HeaderSignature("TEMPORARY_SPANNING_MARKER", 4, 808471376L);
    TEMPORARY_SPANNING_MARKER = headerSignature1;
    HeaderSignature headerSignature12 = new HeaderSignature("DIGITAL_SIGNATURE", 5, 84233040L);
    DIGITAL_SIGNATURE = headerSignature12;
    HeaderSignature headerSignature7 = new HeaderSignature("ARCEXTDATREC", 6, 134630224L);
    ARCEXTDATREC = headerSignature7;
    HeaderSignature headerSignature2 = new HeaderSignature("SPLIT_ZIP", 7, 134695760L);
    SPLIT_ZIP = headerSignature2;
    HeaderSignature headerSignature11 = new HeaderSignature("ZIP64_END_CENTRAL_DIRECTORY_LOCATOR", 8, 117853008L);
    ZIP64_END_CENTRAL_DIRECTORY_LOCATOR = headerSignature11;
    HeaderSignature headerSignature3 = new HeaderSignature("ZIP64_END_CENTRAL_DIRECTORY_RECORD", 9, 101075792L);
    ZIP64_END_CENTRAL_DIRECTORY_RECORD = headerSignature3;
    HeaderSignature headerSignature5 = new HeaderSignature("ZIP64_EXTRA_FIELD_SIGNATURE", 10, 1L);
    ZIP64_EXTRA_FIELD_SIGNATURE = headerSignature5;
    HeaderSignature headerSignature9 = new HeaderSignature("AES_EXTRA_DATA_RECORD", 11, 39169L);
    AES_EXTRA_DATA_RECORD = headerSignature9;
    $VALUES = new HeaderSignature[] { 
        headerSignature10, headerSignature8, headerSignature4, headerSignature6, headerSignature1, headerSignature12, headerSignature7, headerSignature2, headerSignature11, headerSignature3, 
        headerSignature5, headerSignature9 };
  }
  
  HeaderSignature(long paramLong) {
    this.value = paramLong;
  }
  
  public long getValue() {
    return this.value;
  }
}
