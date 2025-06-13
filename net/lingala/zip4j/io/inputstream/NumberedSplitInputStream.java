package net.lingala.zip4j.io.inputstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.lingala.zip4j.util.FileUtils;

public class NumberedSplitInputStream extends SplitInputStream {
  public NumberedSplitInputStream(File paramFile, boolean paramBoolean, int paramInt) throws FileNotFoundException {
    super(paramFile, paramBoolean, paramInt);
  }
  
  public File getNextSplitFile(int paramInt) throws IOException {
    String str1 = this.zipFile.getCanonicalPath();
    String str2 = str1.substring(0, str1.lastIndexOf("."));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str2);
    stringBuilder.append(FileUtils.getNextNumberedSplitFileCounterAsExtension(paramInt));
    return new File(stringBuilder.toString());
  }
}
