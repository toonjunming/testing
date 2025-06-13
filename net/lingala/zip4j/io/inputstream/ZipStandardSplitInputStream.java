package net.lingala.zip4j.io.inputstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ZipStandardSplitInputStream extends SplitInputStream {
  private int lastSplitZipFileNumber;
  
  public ZipStandardSplitInputStream(File paramFile, boolean paramBoolean, int paramInt) throws FileNotFoundException {
    super(paramFile, paramBoolean, paramInt);
    this.lastSplitZipFileNumber = paramInt;
  }
  
  public File getNextSplitFile(int paramInt) throws IOException {
    String str1;
    if (paramInt == this.lastSplitZipFileNumber)
      return this.zipFile; 
    String str2 = this.zipFile.getCanonicalPath();
    if (paramInt >= 9) {
      str1 = ".z";
    } else {
      str1 = ".z0";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str2.substring(0, str2.lastIndexOf(".")));
    stringBuilder.append(str1);
    stringBuilder.append(paramInt + 1);
    return new File(stringBuilder.toString());
  }
}
