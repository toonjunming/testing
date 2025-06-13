package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.CRC32;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.FileHeaderFactory;
import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.RawIO;

public class ZipOutputStream extends OutputStream {
  private CompressedOutputStream compressedOutputStream;
  
  private CountingOutputStream countingOutputStream;
  
  private CRC32 crc32 = new CRC32();
  
  private boolean entryClosed = true;
  
  private FileHeader fileHeader;
  
  private FileHeaderFactory fileHeaderFactory = new FileHeaderFactory();
  
  private HeaderWriter headerWriter = new HeaderWriter();
  
  private LocalFileHeader localFileHeader;
  
  private char[] password;
  
  private RawIO rawIO = new RawIO();
  
  private boolean streamClosed;
  
  private long uncompressedSizeForThisEntry = 0L;
  
  private Zip4jConfig zip4jConfig;
  
  private ZipModel zipModel;
  
  public ZipOutputStream(OutputStream paramOutputStream) throws IOException {
    this(paramOutputStream, null, null);
  }
  
  public ZipOutputStream(OutputStream paramOutputStream, Charset paramCharset) throws IOException {
    this(paramOutputStream, null, paramCharset);
  }
  
  public ZipOutputStream(OutputStream paramOutputStream, char[] paramArrayOfchar) throws IOException {
    this(paramOutputStream, paramArrayOfchar, null);
  }
  
  public ZipOutputStream(OutputStream paramOutputStream, char[] paramArrayOfchar, Charset paramCharset) throws IOException {
    this(paramOutputStream, paramArrayOfchar, new Zip4jConfig(paramCharset, 4096), new ZipModel());
  }
  
  public ZipOutputStream(OutputStream paramOutputStream, char[] paramArrayOfchar, Zip4jConfig paramZip4jConfig, ZipModel paramZipModel) throws IOException {
    if (paramZip4jConfig.getBufferSize() >= 512) {
      paramOutputStream = new CountingOutputStream(paramOutputStream);
      this.countingOutputStream = (CountingOutputStream)paramOutputStream;
      this.password = paramArrayOfchar;
      this.zip4jConfig = paramZip4jConfig;
      this.zipModel = initializeZipModel(paramZipModel, (CountingOutputStream)paramOutputStream);
      this.streamClosed = false;
      writeSplitZipHeaderIfApplicable();
      return;
    } 
    throw new IllegalArgumentException("Buffer size cannot be less than 512 bytes");
  }
  
  private void ensureStreamOpen() throws IOException {
    if (!this.streamClosed)
      return; 
    throw new IOException("Stream is closed");
  }
  
  private void initializeAndWriteFileHeader(ZipParameters paramZipParameters) throws IOException {
    FileHeader fileHeader = this.fileHeaderFactory.generateFileHeader(paramZipParameters, this.countingOutputStream.isSplitZipFile(), this.countingOutputStream.getCurrentSplitFileCounter(), this.zip4jConfig.getCharset(), this.rawIO);
    this.fileHeader = fileHeader;
    fileHeader.setOffsetLocalHeader(this.countingOutputStream.getOffsetForNextEntry());
    LocalFileHeader localFileHeader = this.fileHeaderFactory.generateLocalFileHeader(this.fileHeader);
    this.localFileHeader = localFileHeader;
    this.headerWriter.writeLocalFileHeader(this.zipModel, localFileHeader, this.countingOutputStream, this.zip4jConfig.getCharset());
  }
  
  private CipherOutputStream initializeCipherOutputStream(ZipEntryOutputStream paramZipEntryOutputStream, ZipParameters paramZipParameters) throws IOException {
    if (!paramZipParameters.isEncryptFiles())
      return new NoCipherOutputStream(paramZipEntryOutputStream, paramZipParameters, null); 
    char[] arrayOfChar = this.password;
    if (arrayOfChar != null && arrayOfChar.length != 0) {
      if (paramZipParameters.getEncryptionMethod() == EncryptionMethod.AES)
        return new AesCipherOutputStream(paramZipEntryOutputStream, paramZipParameters, this.password); 
      if (paramZipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD)
        return new ZipStandardCipherOutputStream(paramZipEntryOutputStream, paramZipParameters, this.password); 
      EncryptionMethod encryptionMethod2 = paramZipParameters.getEncryptionMethod();
      EncryptionMethod encryptionMethod1 = EncryptionMethod.ZIP_STANDARD_VARIANT_STRONG;
      if (encryptionMethod2 == encryptionMethod1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(encryptionMethod1);
        stringBuilder.append(" encryption method is not supported");
        throw new ZipException(stringBuilder.toString());
      } 
      throw new ZipException("Invalid encryption method");
    } 
    throw new ZipException("password not set");
  }
  
  private CompressedOutputStream initializeCompressedOutputStream(CipherOutputStream paramCipherOutputStream, ZipParameters paramZipParameters) {
    return (CompressedOutputStream)((paramZipParameters.getCompressionMethod() == CompressionMethod.DEFLATE) ? new DeflaterOutputStream(paramCipherOutputStream, paramZipParameters.getCompressionLevel(), this.zip4jConfig.getBufferSize()) : new StoreOutputStream(paramCipherOutputStream));
  }
  
  private CompressedOutputStream initializeCompressedOutputStream(ZipParameters paramZipParameters) throws IOException {
    return initializeCompressedOutputStream(initializeCipherOutputStream(new ZipEntryOutputStream(this.countingOutputStream), paramZipParameters), paramZipParameters);
  }
  
  private ZipModel initializeZipModel(ZipModel paramZipModel, CountingOutputStream paramCountingOutputStream) {
    ZipModel zipModel = paramZipModel;
    if (paramZipModel == null)
      zipModel = new ZipModel(); 
    if (paramCountingOutputStream.isSplitZipFile()) {
      zipModel.setSplitArchive(true);
      zipModel.setSplitLength(paramCountingOutputStream.getSplitLength());
    } 
    return zipModel;
  }
  
  private boolean isEntryDirectory(String paramString) {
    return (paramString.endsWith("/") || paramString.endsWith("\\"));
  }
  
  private void reset() throws IOException {
    this.uncompressedSizeForThisEntry = 0L;
    this.crc32.reset();
    this.compressedOutputStream.close();
  }
  
  private void verifyZipParameters(ZipParameters paramZipParameters) {
    if (paramZipParameters.getCompressionMethod() != CompressionMethod.STORE || paramZipParameters.getEntrySize() >= 0L || isEntryDirectory(paramZipParameters.getFileNameInZip()) || !paramZipParameters.isWriteExtendedLocalFileHeader())
      return; 
    throw new IllegalArgumentException("uncompressed size should be set for zip entries of compression type store");
  }
  
  private boolean writeCrc(FileHeader paramFileHeader) {
    boolean bool;
    if (paramFileHeader.isEncrypted() && paramFileHeader.getEncryptionMethod().equals(EncryptionMethod.AES)) {
      bool = true;
    } else {
      bool = false;
    } 
    return !bool ? true : paramFileHeader.getAesExtraDataRecord().getAesVersion().equals(AesVersion.ONE);
  }
  
  private void writeSplitZipHeaderIfApplicable() throws IOException {
    if (!this.countingOutputStream.isSplitZipFile())
      return; 
    this.rawIO.writeIntLittleEndian(this.countingOutputStream, (int)HeaderSignature.SPLIT_ZIP.getValue());
  }
  
  public void close() throws IOException {
    if (!this.entryClosed)
      closeEntry(); 
    this.zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(this.countingOutputStream.getNumberOfBytesWritten());
    this.headerWriter.finalizeZipFile(this.zipModel, this.countingOutputStream, this.zip4jConfig.getCharset());
    this.countingOutputStream.close();
    this.streamClosed = true;
  }
  
  public FileHeader closeEntry() throws IOException {
    this.compressedOutputStream.closeEntry();
    long l = this.compressedOutputStream.getCompressedSize();
    this.fileHeader.setCompressedSize(l);
    this.localFileHeader.setCompressedSize(l);
    this.fileHeader.setUncompressedSize(this.uncompressedSizeForThisEntry);
    this.localFileHeader.setUncompressedSize(this.uncompressedSizeForThisEntry);
    if (writeCrc(this.fileHeader)) {
      this.fileHeader.setCrc(this.crc32.getValue());
      this.localFileHeader.setCrc(this.crc32.getValue());
    } 
    this.zipModel.getLocalFileHeaders().add(this.localFileHeader);
    this.zipModel.getCentralDirectory().getFileHeaders().add(this.fileHeader);
    if (this.localFileHeader.isDataDescriptorExists())
      this.headerWriter.writeExtendedLocalHeader(this.localFileHeader, this.countingOutputStream); 
    reset();
    this.entryClosed = true;
    return this.fileHeader;
  }
  
  public void putNextEntry(ZipParameters paramZipParameters) throws IOException {
    verifyZipParameters(paramZipParameters);
    initializeAndWriteFileHeader(paramZipParameters);
    this.compressedOutputStream = initializeCompressedOutputStream(paramZipParameters);
    this.entryClosed = false;
  }
  
  public void setComment(String paramString) throws IOException {
    ensureStreamOpen();
    this.zipModel.getEndOfCentralDirectoryRecord().setComment(paramString);
  }
  
  public void write(int paramInt) throws IOException {
    write(new byte[] { (byte)paramInt });
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    ensureStreamOpen();
    this.crc32.update(paramArrayOfbyte, paramInt1, paramInt2);
    this.compressedOutputStream.write(paramArrayOfbyte, paramInt1, paramInt2);
    this.uncompressedSizeForThisEntry += paramInt2;
  }
}
