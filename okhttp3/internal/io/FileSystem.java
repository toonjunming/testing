package okhttp3.internal.io;

import I丨L.I11li1;
import I丨L.lIi丨I;
import I丨L.丨lL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileSystem {
  public static final FileSystem SYSTEM = new FileSystem() {
      public I11li1 appendingSink(File param1File) throws FileNotFoundException {
        try {
          return lIi丨I.IL1Iii(param1File);
        } catch (FileNotFoundException fileNotFoundException) {
          param1File.getParentFile().mkdirs();
          return lIi丨I.IL1Iii(param1File);
        } 
      }
      
      public void delete(File param1File) throws IOException {
        if (param1File.delete() || !param1File.exists())
          return; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("failed to delete ");
        stringBuilder.append(param1File);
        throw new IOException(stringBuilder.toString());
      }
      
      public void deleteContents(File param1File) throws IOException {
        File[] arrayOfFile = param1File.listFiles();
        if (arrayOfFile != null) {
          int i = arrayOfFile.length;
          byte b = 0;
          while (b < i) {
            param1File = arrayOfFile[b];
            if (param1File.isDirectory())
              deleteContents(param1File); 
            if (param1File.delete()) {
              b++;
              continue;
            } 
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("failed to delete ");
            stringBuilder1.append(param1File);
            throw new IOException(stringBuilder1.toString());
          } 
          return;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("not a readable directory: ");
        stringBuilder.append(param1File);
        throw new IOException(stringBuilder.toString());
      }
      
      public boolean exists(File param1File) {
        return param1File.exists();
      }
      
      public void rename(File param1File1, File param1File2) throws IOException {
        delete(param1File2);
        if (param1File1.renameTo(param1File2))
          return; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("failed to rename ");
        stringBuilder.append(param1File1);
        stringBuilder.append(" to ");
        stringBuilder.append(param1File2);
        throw new IOException(stringBuilder.toString());
      }
      
      public I11li1 sink(File param1File) throws FileNotFoundException {
        try {
          return lIi丨I.l丨Li1LL(param1File);
        } catch (FileNotFoundException fileNotFoundException) {
          param1File.getParentFile().mkdirs();
          return lIi丨I.l丨Li1LL(param1File);
        } 
      }
      
      public long size(File param1File) {
        return param1File.length();
      }
      
      public 丨lL source(File param1File) throws FileNotFoundException {
        return lIi丨I.丨il(param1File);
      }
    };
  
  I11li1 appendingSink(File paramFile) throws FileNotFoundException;
  
  void delete(File paramFile) throws IOException;
  
  void deleteContents(File paramFile) throws IOException;
  
  boolean exists(File paramFile);
  
  void rename(File paramFile1, File paramFile2) throws IOException;
  
  I11li1 sink(File paramFile) throws FileNotFoundException;
  
  long size(File paramFile);
  
  丨lL source(File paramFile) throws FileNotFoundException;
}
