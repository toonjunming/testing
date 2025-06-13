package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderReader;
import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.model.DataDescriptor;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipInputStream extends InputStream {
  private boolean canSkipExtendedLocalFileHeader = false;
  
  private CRC32 crc32 = new CRC32();
  
  private DecompressedInputStream decompressedInputStream;
  
  private byte[] endOfEntryBuffer;
  
  private boolean entryEOFReached = false;
  
  private HeaderReader headerReader = new HeaderReader();
  
  private PushbackInputStream inputStream;
  
  private LocalFileHeader localFileHeader;
  
  private char[] password;
  
  private boolean streamClosed = false;
  
  private Zip4jConfig zip4jConfig;
  
  public ZipInputStream(InputStream paramInputStream) {
    this(paramInputStream, (char[])null, (Charset)null);
  }
  
  public ZipInputStream(InputStream paramInputStream, Charset paramCharset) {
    this(paramInputStream, (char[])null, paramCharset);
  }
  
  public ZipInputStream(InputStream paramInputStream, char[] paramArrayOfchar) {
    this(paramInputStream, paramArrayOfchar, (Charset)null);
  }
  
  public ZipInputStream(InputStream paramInputStream, char[] paramArrayOfchar, Charset paramCharset) {
    this(paramInputStream, paramArrayOfchar, new Zip4jConfig(paramCharset, 4096));
  }
  
  public ZipInputStream(InputStream paramInputStream, char[] paramArrayOfchar, Zip4jConfig paramZip4jConfig) {
    if (paramZip4jConfig.getBufferSize() >= 512) {
      this.inputStream = new PushbackInputStream(paramInputStream, paramZip4jConfig.getBufferSize());
      this.password = paramArrayOfchar;
      this.zip4jConfig = paramZip4jConfig;
      return;
    } 
    throw new IllegalArgumentException("Buffer size cannot be less than 512 bytes");
  }
  
  private void assertStreamOpen() throws IOException {
    if (!this.streamClosed)
      return; 
    throw new IOException("Stream closed");
  }
  
  private boolean checkIfZip64ExtraDataRecordPresentInLFH(List<ExtraDataRecord> paramList) {
    if (paramList == null)
      return false; 
    Iterator<ExtraDataRecord> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      if (((ExtraDataRecord)iterator.next()).getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue())
        return true; 
    } 
    return false;
  }
  
  private void endOfCompressedDataReached() throws IOException {
    this.decompressedInputStream.pushBackInputStreamIfNecessary(this.inputStream);
    this.decompressedInputStream.endOfEntryReached(this.inputStream);
    readExtendedLocalFileHeaderIfPresent();
    verifyCrc();
    resetFields();
    this.entryEOFReached = true;
  }
  
  private long getCompressedSize(LocalFileHeader paramLocalFileHeader) {
    return Zip4jUtil.getCompressionMethod(paramLocalFileHeader).equals(CompressionMethod.STORE) ? paramLocalFileHeader.getUncompressedSize() : ((paramLocalFileHeader.isDataDescriptorExists() && !this.canSkipExtendedLocalFileHeader) ? -1L : (paramLocalFileHeader.getCompressedSize() - getEncryptionHeaderSize(paramLocalFileHeader)));
  }
  
  private int getEncryptionHeaderSize(LocalFileHeader paramLocalFileHeader) {
    return !paramLocalFileHeader.isEncrypted() ? 0 : (paramLocalFileHeader.getEncryptionMethod().equals(EncryptionMethod.AES) ? (paramLocalFileHeader.getAesExtraDataRecord().getAesKeyStrength().getSaltLength() + 12) : (paramLocalFileHeader.getEncryptionMethod().equals(EncryptionMethod.ZIP_STANDARD) ? 12 : 0));
  }
  
  private CipherInputStream initializeCipherInputStream(ZipEntryInputStream paramZipEntryInputStream, LocalFileHeader paramLocalFileHeader) throws IOException {
    if (!paramLocalFileHeader.isEncrypted())
      return new NoCipherInputStream(paramZipEntryInputStream, paramLocalFileHeader, this.password, this.zip4jConfig.getBufferSize()); 
    if (paramLocalFileHeader.getEncryptionMethod() == EncryptionMethod.AES)
      return new AesCipherInputStream(paramZipEntryInputStream, paramLocalFileHeader, this.password, this.zip4jConfig.getBufferSize()); 
    if (paramLocalFileHeader.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD)
      return new ZipStandardCipherInputStream(paramZipEntryInputStream, paramLocalFileHeader, this.password, this.zip4jConfig.getBufferSize()); 
    throw new ZipException(String.format("Entry [%s] Strong Encryption not supported", new Object[] { paramLocalFileHeader.getFileName() }), ZipException.Type.UNSUPPORTED_ENCRYPTION);
  }
  
  private DecompressedInputStream initializeDecompressorForThisEntry(CipherInputStream paramCipherInputStream, LocalFileHeader paramLocalFileHeader) {
    return (DecompressedInputStream)((Zip4jUtil.getCompressionMethod(paramLocalFileHeader) == CompressionMethod.DEFLATE) ? new InflaterInputStream(paramCipherInputStream, this.zip4jConfig.getBufferSize()) : new StoreInputStream(paramCipherInputStream));
  }
  
  private DecompressedInputStream initializeEntryInputStream(LocalFileHeader paramLocalFileHeader) throws IOException {
    return initializeDecompressorForThisEntry(initializeCipherInputStream(new ZipEntryInputStream(this.inputStream, getCompressedSize(paramLocalFileHeader)), paramLocalFileHeader), paramLocalFileHeader);
  }
  
  private boolean isEncryptionMethodZipStandard(LocalFileHeader paramLocalFileHeader) {
    boolean bool;
    if (paramLocalFileHeader.isEncrypted() && EncryptionMethod.ZIP_STANDARD.equals(paramLocalFileHeader.getEncryptionMethod())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isEntryDirectory(String paramString) {
    return (paramString.endsWith("/") || paramString.endsWith("\\"));
  }
  
  private void readExtendedLocalFileHeaderIfPresent() throws IOException {
    if (this.localFileHeader.isDataDescriptorExists() && !this.canSkipExtendedLocalFileHeader) {
      DataDescriptor dataDescriptor = this.headerReader.readDataDescriptor(this.inputStream, checkIfZip64ExtraDataRecordPresentInLFH(this.localFileHeader.getExtraDataRecords()));
      this.localFileHeader.setCompressedSize(dataDescriptor.getCompressedSize());
      this.localFileHeader.setUncompressedSize(dataDescriptor.getUncompressedSize());
      this.localFileHeader.setCrc(dataDescriptor.getCrc());
    } 
  }
  
  private void readUntilEndOfEntry() throws IOException {
    if ((this.localFileHeader.isDirectory() || this.localFileHeader.getCompressedSize() == 0L) && !this.localFileHeader.isDataDescriptorExists())
      return; 
    if (this.endOfEntryBuffer == null)
      this.endOfEntryBuffer = new byte[512]; 
    while (read(this.endOfEntryBuffer) != -1);
    this.entryEOFReached = true;
  }
  
  private void resetFields() {
    this.localFileHeader = null;
    this.crc32.reset();
  }
  
  private void verifyCrc() throws IOException {
    if (this.localFileHeader.getEncryptionMethod() == EncryptionMethod.AES && this.localFileHeader.getAesExtraDataRecord().getAesVersion().equals(AesVersion.TWO))
      return; 
    if (this.localFileHeader.getCrc() != this.crc32.getValue()) {
      ZipException.Type type = ZipException.Type.CHECKSUM_MISMATCH;
      if (isEncryptionMethodZipStandard(this.localFileHeader))
        type = ZipException.Type.WRONG_PASSWORD; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Reached end of entry, but crc verification failed for ");
      stringBuilder.append(this.localFileHeader.getFileName());
      throw new ZipException(stringBuilder.toString(), type);
    } 
  }
  
  private void verifyLocalFileHeader(LocalFileHeader paramLocalFileHeader) throws IOException {
    if (isEntryDirectory(paramLocalFileHeader.getFileName()) || paramLocalFileHeader.getCompressionMethod() != CompressionMethod.STORE || paramLocalFileHeader.getUncompressedSize() >= 0L)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid local file header for: ");
    stringBuilder.append(paramLocalFileHeader.getFileName());
    stringBuilder.append(". Uncompressed size has to be set for entry of compression type store which is not a directory");
    throw new IOException(stringBuilder.toString());
  }
  
  public int available() throws IOException {
    assertStreamOpen();
    return this.entryEOFReached ^ true;
  }
  
  public void close() throws IOException {
    if (this.streamClosed)
      return; 
    DecompressedInputStream decompressedInputStream = this.decompressedInputStream;
    if (decompressedInputStream != null)
      decompressedInputStream.close(); 
    this.streamClosed = true;
  }
  
  public LocalFileHeader getNextEntry() throws IOException {
    return getNextEntry(null);
  }
  
  public LocalFileHeader getNextEntry(FileHeader paramFileHeader) throws IOException {
    if (this.localFileHeader != null)
      readUntilEndOfEntry(); 
    LocalFileHeader localFileHeader = this.headerReader.readLocalFileHeader(this.inputStream, this.zip4jConfig.getCharset());
    this.localFileHeader = localFileHeader;
    if (localFileHeader == null)
      return null; 
    verifyLocalFileHeader(localFileHeader);
    this.crc32.reset();
    if (paramFileHeader != null) {
      this.localFileHeader.setCrc(paramFileHeader.getCrc());
      this.localFileHeader.setCompressedSize(paramFileHeader.getCompressedSize());
      this.localFileHeader.setUncompressedSize(paramFileHeader.getUncompressedSize());
      this.localFileHeader.setDirectory(paramFileHeader.isDirectory());
      this.canSkipExtendedLocalFileHeader = true;
    } else {
      this.canSkipExtendedLocalFileHeader = false;
    } 
    this.decompressedInputStream = initializeEntryInputStream(this.localFileHeader);
    this.entryEOFReached = false;
    return this.localFileHeader;
  }
  
  public int read() throws IOException {
    byte[] arrayOfByte = new byte[1];
    return (read(arrayOfByte) == -1) ? -1 : (arrayOfByte[0] & 0xFF);
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (!this.streamClosed) {
      if (paramInt2 >= 0) {
        if (paramInt2 == 0)
          return 0; 
        LocalFileHeader localFileHeader = this.localFileHeader;
        if (localFileHeader == null)
          return -1; 
        if (localFileHeader.isDirectory())
          return -1; 
        try {
          paramInt2 = this.decompressedInputStream.read(paramArrayOfbyte, paramInt1, paramInt2);
          if (paramInt2 == -1) {
            endOfCompressedDataReached();
          } else {
            this.crc32.update(paramArrayOfbyte, paramInt1, paramInt2);
          } 
          return paramInt2;
        } catch (IOException iOException) {
          if (isEncryptionMethodZipStandard(this.localFileHeader))
            throw new ZipException(iOException.getMessage(), iOException.getCause(), ZipException.Type.WRONG_PASSWORD); 
          throw iOException;
        } 
      } 
      throw new IllegalArgumentException("Negative read length");
    } 
    throw new IOException("Stream closed");
  }
  
  public void setPassword(char[] paramArrayOfchar) {
    this.password = paramArrayOfchar;
  }
}
