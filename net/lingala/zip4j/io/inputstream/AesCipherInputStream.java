package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.Decrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

public class AesCipherInputStream extends CipherInputStream<AESDecrypter> {
  private byte[] aes16ByteBlock = new byte[16];
  
  private int aes16ByteBlockPointer = 0;
  
  private int aes16ByteBlockReadLength = 0;
  
  private int bytesCopiedInThisIteration = 0;
  
  private int lengthToCopyInThisIteration = 0;
  
  private int lengthToRead = 0;
  
  private int offsetWithAesBlock = 0;
  
  private int remainingAes16ByteBlockLength = 0;
  
  private byte[] singleByteBuffer = new byte[1];
  
  public AesCipherInputStream(ZipEntryInputStream paramZipEntryInputStream, LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar, int paramInt) throws IOException {
    super(paramZipEntryInputStream, paramLocalFileHeader, paramArrayOfchar, paramInt);
  }
  
  private void copyBytesFromBuffer(byte[] paramArrayOfbyte, int paramInt) {
    int j = this.lengthToRead;
    int i = this.remainingAes16ByteBlockLength;
    if (j < i)
      i = j; 
    this.lengthToCopyInThisIteration = i;
    System.arraycopy(this.aes16ByteBlock, this.aes16ByteBlockPointer, paramArrayOfbyte, paramInt, i);
    incrementAesByteBlockPointer(this.lengthToCopyInThisIteration);
    decrementRemainingAesBytesLength(this.lengthToCopyInThisIteration);
    paramInt = this.bytesCopiedInThisIteration;
    i = this.lengthToCopyInThisIteration;
    this.bytesCopiedInThisIteration = paramInt + i;
    this.lengthToRead -= i;
    this.offsetWithAesBlock += i;
  }
  
  private void decrementRemainingAesBytesLength(int paramInt) {
    paramInt = this.remainingAes16ByteBlockLength - paramInt;
    this.remainingAes16ByteBlockLength = paramInt;
    if (paramInt <= 0)
      this.remainingAes16ByteBlockLength = 0; 
  }
  
  private byte[] getPasswordVerifier() throws IOException {
    byte[] arrayOfByte = new byte[2];
    readRaw(arrayOfByte);
    return arrayOfByte;
  }
  
  private byte[] getSalt(LocalFileHeader paramLocalFileHeader) throws IOException {
    if (paramLocalFileHeader.getAesExtraDataRecord() != null) {
      byte[] arrayOfByte = new byte[paramLocalFileHeader.getAesExtraDataRecord().getAesKeyStrength().getSaltLength()];
      readRaw(arrayOfByte);
      return arrayOfByte;
    } 
    throw new IOException("invalid aes extra data record");
  }
  
  private void incrementAesByteBlockPointer(int paramInt) {
    paramInt = this.aes16ByteBlockPointer + paramInt;
    this.aes16ByteBlockPointer = paramInt;
    if (paramInt >= 15)
      this.aes16ByteBlockPointer = 15; 
  }
  
  private void verifyContent(byte[] paramArrayOfbyte) throws IOException {
    if (getLocalFileHeader().isDataDescriptorExists() && CompressionMethod.DEFLATE.equals(Zip4jUtil.getCompressionMethod(getLocalFileHeader())))
      return; 
    byte[] arrayOfByte2 = ((AESDecrypter)getDecrypter()).getCalculatedAuthenticationBytes();
    byte[] arrayOfByte1 = new byte[10];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 10);
    if (Arrays.equals(paramArrayOfbyte, arrayOfByte1))
      return; 
    throw new IOException("Reached end of data for this entry, but aes verification failed");
  }
  
  public void endOfEntryReached(InputStream paramInputStream) throws IOException {
    verifyContent(readStoredMac(paramInputStream));
  }
  
  public AESDecrypter initializeDecrypter(LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar) throws IOException {
    return new AESDecrypter(paramLocalFileHeader.getAesExtraDataRecord(), paramArrayOfchar, getSalt(paramLocalFileHeader), getPasswordVerifier());
  }
  
  public int read() throws IOException {
    return (read(this.singleByteBuffer) == -1) ? -1 : this.singleByteBuffer[0];
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.lengthToRead = paramInt2;
    this.offsetWithAesBlock = paramInt1;
    this.bytesCopiedInThisIteration = 0;
    if (this.remainingAes16ByteBlockLength != 0) {
      copyBytesFromBuffer(paramArrayOfbyte, paramInt1);
      paramInt1 = this.bytesCopiedInThisIteration;
      if (paramInt1 == paramInt2)
        return paramInt1; 
    } 
    if (this.lengthToRead < 16) {
      byte[] arrayOfByte = this.aes16ByteBlock;
      paramInt1 = super.read(arrayOfByte, 0, arrayOfByte.length);
      this.aes16ByteBlockReadLength = paramInt1;
      this.aes16ByteBlockPointer = 0;
      if (paramInt1 == -1) {
        this.remainingAes16ByteBlockLength = 0;
        paramInt1 = this.bytesCopiedInThisIteration;
        return (paramInt1 > 0) ? paramInt1 : -1;
      } 
      this.remainingAes16ByteBlockLength = paramInt1;
      copyBytesFromBuffer(paramArrayOfbyte, this.offsetWithAesBlock);
      paramInt1 = this.bytesCopiedInThisIteration;
      if (paramInt1 == paramInt2)
        return paramInt1; 
    } 
    paramInt2 = this.offsetWithAesBlock;
    paramInt1 = this.lengthToRead;
    paramInt1 = super.read(paramArrayOfbyte, paramInt2, paramInt1 - paramInt1 % 16);
    if (paramInt1 == -1) {
      paramInt1 = this.bytesCopiedInThisIteration;
      return (paramInt1 > 0) ? paramInt1 : -1;
    } 
    return paramInt1 + this.bytesCopiedInThisIteration;
  }
  
  public byte[] readStoredMac(InputStream paramInputStream) throws IOException {
    byte[] arrayOfByte = new byte[10];
    if (Zip4jUtil.readFully(paramInputStream, arrayOfByte) == 10)
      return arrayOfByte; 
    throw new ZipException("Invalid AES Mac bytes. Could not read sufficient data");
  }
}
