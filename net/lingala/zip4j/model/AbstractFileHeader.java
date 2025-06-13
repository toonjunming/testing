package net.lingala.zip4j.model;

import java.util.List;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

public abstract class AbstractFileHeader extends ZipHeader {
  private AESExtraDataRecord aesExtraDataRecord;
  
  private long compressedSize = 0L;
  
  private CompressionMethod compressionMethod;
  
  private long crc = 0L;
  
  private boolean dataDescriptorExists;
  
  private EncryptionMethod encryptionMethod = EncryptionMethod.NONE;
  
  private List<ExtraDataRecord> extraDataRecords;
  
  private int extraFieldLength;
  
  private String fileName;
  
  private int fileNameLength;
  
  private boolean fileNameUTF8Encoded;
  
  private byte[] generalPurposeFlag;
  
  private boolean isDirectory;
  
  private boolean isEncrypted;
  
  private long lastModifiedTime;
  
  private long uncompressedSize = 0L;
  
  private int versionNeededToExtract;
  
  private Zip64ExtendedInfo zip64ExtendedInfo;
  
  public boolean equals(Object paramObject) {
    return (paramObject == null) ? false : (!(paramObject instanceof AbstractFileHeader) ? false : getFileName().equals(((AbstractFileHeader)paramObject).getFileName()));
  }
  
  public AESExtraDataRecord getAesExtraDataRecord() {
    return this.aesExtraDataRecord;
  }
  
  public long getCompressedSize() {
    return this.compressedSize;
  }
  
  public CompressionMethod getCompressionMethod() {
    return this.compressionMethod;
  }
  
  public long getCrc() {
    return this.crc;
  }
  
  public EncryptionMethod getEncryptionMethod() {
    return this.encryptionMethod;
  }
  
  public List<ExtraDataRecord> getExtraDataRecords() {
    return this.extraDataRecords;
  }
  
  public int getExtraFieldLength() {
    return this.extraFieldLength;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public int getFileNameLength() {
    return this.fileNameLength;
  }
  
  public byte[] getGeneralPurposeFlag() {
    return this.generalPurposeFlag;
  }
  
  public long getLastModifiedTime() {
    return this.lastModifiedTime;
  }
  
  public long getLastModifiedTimeEpoch() {
    return Zip4jUtil.dosToExtendedEpochTme(this.lastModifiedTime);
  }
  
  public long getUncompressedSize() {
    return this.uncompressedSize;
  }
  
  public int getVersionNeededToExtract() {
    return this.versionNeededToExtract;
  }
  
  public Zip64ExtendedInfo getZip64ExtendedInfo() {
    return this.zip64ExtendedInfo;
  }
  
  public boolean isDataDescriptorExists() {
    return this.dataDescriptorExists;
  }
  
  public boolean isDirectory() {
    return this.isDirectory;
  }
  
  public boolean isEncrypted() {
    return this.isEncrypted;
  }
  
  public boolean isFileNameUTF8Encoded() {
    return this.fileNameUTF8Encoded;
  }
  
  public void setAesExtraDataRecord(AESExtraDataRecord paramAESExtraDataRecord) {
    this.aesExtraDataRecord = paramAESExtraDataRecord;
  }
  
  public void setCompressedSize(long paramLong) {
    this.compressedSize = paramLong;
  }
  
  public void setCompressionMethod(CompressionMethod paramCompressionMethod) {
    this.compressionMethod = paramCompressionMethod;
  }
  
  public void setCrc(long paramLong) {
    this.crc = paramLong;
  }
  
  public void setDataDescriptorExists(boolean paramBoolean) {
    this.dataDescriptorExists = paramBoolean;
  }
  
  public void setDirectory(boolean paramBoolean) {
    this.isDirectory = paramBoolean;
  }
  
  public void setEncrypted(boolean paramBoolean) {
    this.isEncrypted = paramBoolean;
  }
  
  public void setEncryptionMethod(EncryptionMethod paramEncryptionMethod) {
    this.encryptionMethod = paramEncryptionMethod;
  }
  
  public void setExtraDataRecords(List<ExtraDataRecord> paramList) {
    this.extraDataRecords = paramList;
  }
  
  public void setExtraFieldLength(int paramInt) {
    this.extraFieldLength = paramInt;
  }
  
  public void setFileName(String paramString) {
    this.fileName = paramString;
  }
  
  public void setFileNameLength(int paramInt) {
    this.fileNameLength = paramInt;
  }
  
  public void setFileNameUTF8Encoded(boolean paramBoolean) {
    this.fileNameUTF8Encoded = paramBoolean;
  }
  
  public void setGeneralPurposeFlag(byte[] paramArrayOfbyte) {
    this.generalPurposeFlag = paramArrayOfbyte;
  }
  
  public void setLastModifiedTime(long paramLong) {
    this.lastModifiedTime = paramLong;
  }
  
  public void setUncompressedSize(long paramLong) {
    this.uncompressedSize = paramLong;
  }
  
  public void setVersionNeededToExtract(int paramInt) {
    this.versionNeededToExtract = paramInt;
  }
  
  public void setZip64ExtendedInfo(Zip64ExtendedInfo paramZip64ExtendedInfo) {
    this.zip64ExtendedInfo = paramZip64ExtendedInfo;
  }
}
