package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.util.zip.Deflater;
import net.lingala.zip4j.model.enums.CompressionLevel;

public class DeflaterOutputStream extends CompressedOutputStream {
  private byte[] buff;
  
  public Deflater deflater;
  
  public DeflaterOutputStream(CipherOutputStream paramCipherOutputStream, CompressionLevel paramCompressionLevel, int paramInt) {
    super(paramCipherOutputStream);
    this.deflater = new Deflater(paramCompressionLevel.getLevel(), true);
    this.buff = new byte[paramInt];
  }
  
  private void deflate() throws IOException {
    Deflater deflater = this.deflater;
    byte[] arrayOfByte = this.buff;
    int i = deflater.deflate(arrayOfByte, 0, arrayOfByte.length);
    if (i > 0)
      super.write(this.buff, 0, i); 
  }
  
  public void closeEntry() throws IOException {
    if (!this.deflater.finished()) {
      this.deflater.finish();
      while (!this.deflater.finished())
        deflate(); 
    } 
    this.deflater.end();
    super.closeEntry();
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt }, 0, 1);
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.deflater.setInput(paramArrayOfbyte, paramInt1, paramInt2);
    while (!this.deflater.needsInput())
      deflate(); 
  }
}
