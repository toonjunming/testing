package okhttp3.internal.cache2;

import I丨L.I1I;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class FileOperator {
  private final FileChannel fileChannel;
  
  public FileOperator(FileChannel paramFileChannel) {
    this.fileChannel = paramFileChannel;
  }
  
  public void read(long paramLong1, I1I paramI1I, long paramLong2) throws IOException {
    if (paramLong2 >= 0L) {
      while (paramLong2 > 0L) {
        long l = this.fileChannel.transferTo(paramLong1, paramLong2, paramI1I);
        paramLong1 += l;
        paramLong2 -= l;
      } 
      return;
    } 
    throw new IndexOutOfBoundsException();
  }
  
  public void write(long paramLong1, I1I paramI1I, long paramLong2) throws IOException {
    if (paramLong2 >= 0L && paramLong2 <= paramI1I.iI1i丨I()) {
      long l = paramLong1;
      for (paramLong1 = paramLong2; paramLong1 > 0L; paramLong1 -= paramLong2) {
        paramLong2 = this.fileChannel.transferFrom(paramI1I, l, paramLong1);
        l += paramLong2;
      } 
      return;
    } 
    throw new IndexOutOfBoundsException();
  }
}
