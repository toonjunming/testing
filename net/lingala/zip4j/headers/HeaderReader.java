package net.lingala.zip4j.headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.NumberedSplitRandomAccessFile;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.DataDescriptor;
import net.lingala.zip4j.model.DigitalSignature;
import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.Zip64ExtendedInfo;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.BitUtils;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.RawIO;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderReader {
  private final byte[] intBuff = new byte[4];
  
  private final RawIO rawIO = new RawIO();
  
  private ZipModel zipModel;
  
  private long getNumberOfEntriesInCentralDirectory(ZipModel paramZipModel) {
    return paramZipModel.isZip64Format() ? paramZipModel.getZip64EndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory() : paramZipModel.getEndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory();
  }
  
  private long locateOffsetOfEndOfCentralDirectory(RandomAccessFile paramRandomAccessFile) throws IOException {
    long l = paramRandomAccessFile.length();
    if (l >= 22L) {
      l -= 22L;
      seekInCurrentPart(paramRandomAccessFile, l);
      return (this.rawIO.readIntLittleEndian(paramRandomAccessFile) == HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue()) ? l : locateOffsetOfEndOfCentralDirectoryByReverseSeek(paramRandomAccessFile);
    } 
    throw new ZipException("Zip file size less than size of zip headers. Probably not a zip file.");
  }
  
  private long locateOffsetOfEndOfCentralDirectoryByReverseSeek(RandomAccessFile paramRandomAccessFile) throws IOException {
    long l3 = paramRandomAccessFile.length() - 22L;
    long l4 = paramRandomAccessFile.length();
    long l1 = 65536L;
    long l2 = l3;
    if (l4 < 65536L) {
      l1 = paramRandomAccessFile.length();
      l2 = l3;
    } 
    while (l1 > 0L && l2 > 0L) {
      l2--;
      seekInCurrentPart(paramRandomAccessFile, l2);
      if (this.rawIO.readIntLittleEndian(paramRandomAccessFile) == HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue())
        return l2; 
      l1--;
    } 
    throw new ZipException("Zip headers not found. Probably not a zip file");
  }
  
  private List<ExtraDataRecord> parseExtraDataRecords(byte[] paramArrayOfbyte, int paramInt) {
    ArrayList<ExtraDataRecord> arrayList = new ArrayList();
    int i = 0;
    while (i < paramInt) {
      ExtraDataRecord extraDataRecord = new ExtraDataRecord();
      extraDataRecord.setHeader(this.rawIO.readShortLittleEndian(paramArrayOfbyte, i));
      int j = i + 2;
      i = this.rawIO.readShortLittleEndian(paramArrayOfbyte, j);
      extraDataRecord.setSizeOfData(i);
      j += 2;
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(paramArrayOfbyte, j, arrayOfByte, 0, i);
        extraDataRecord.setData(arrayOfByte);
      } 
      i = j + i;
      arrayList.add(extraDataRecord);
    } 
    if (arrayList.size() > 0) {
      ArrayList<ExtraDataRecord> arrayList1 = arrayList;
    } else {
      paramArrayOfbyte = null;
    } 
    return (List<ExtraDataRecord>)paramArrayOfbyte;
  }
  
  private AESExtraDataRecord readAesExtraDataRecord(List<ExtraDataRecord> paramList, RawIO paramRawIO) throws ZipException {
    if (paramList == null)
      return null; 
    Iterator<ExtraDataRecord> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      ExtraDataRecord extraDataRecord = iterator.next();
      if (extraDataRecord == null)
        continue; 
      long l = extraDataRecord.getHeader();
      HeaderSignature headerSignature = HeaderSignature.AES_EXTRA_DATA_RECORD;
      if (l == headerSignature.getValue()) {
        if (extraDataRecord.getData() != null) {
          AESExtraDataRecord aESExtraDataRecord = new AESExtraDataRecord();
          aESExtraDataRecord.setSignature(headerSignature);
          aESExtraDataRecord.setDataSize(extraDataRecord.getSizeOfData());
          byte[] arrayOfByte1 = extraDataRecord.getData();
          aESExtraDataRecord.setAesVersion(AesVersion.getFromVersionNumber(paramRawIO.readShortLittleEndian(arrayOfByte1, 0)));
          byte[] arrayOfByte2 = new byte[2];
          System.arraycopy(arrayOfByte1, 2, arrayOfByte2, 0, 2);
          aESExtraDataRecord.setVendorID(new String(arrayOfByte2));
          aESExtraDataRecord.setAesKeyStrength(AesKeyStrength.getAesKeyStrengthFromRawCode(arrayOfByte1[4] & 0xFF));
          aESExtraDataRecord.setCompressionMethod(CompressionMethod.getCompressionMethodFromCode(paramRawIO.readShortLittleEndian(arrayOfByte1, 5)));
          return aESExtraDataRecord;
        } 
        throw new ZipException("corrupt AES extra data records");
      } 
    } 
    return null;
  }
  
  private void readAesExtraDataRecord(FileHeader paramFileHeader, RawIO paramRawIO) throws ZipException {
    if (paramFileHeader.getExtraDataRecords() != null && paramFileHeader.getExtraDataRecords().size() > 0) {
      AESExtraDataRecord aESExtraDataRecord = readAesExtraDataRecord(paramFileHeader.getExtraDataRecords(), paramRawIO);
      if (aESExtraDataRecord != null) {
        paramFileHeader.setAesExtraDataRecord(aESExtraDataRecord);
        paramFileHeader.setEncryptionMethod(EncryptionMethod.AES);
      } 
    } 
  }
  
  private void readAesExtraDataRecord(LocalFileHeader paramLocalFileHeader, RawIO paramRawIO) throws ZipException {
    if (paramLocalFileHeader.getExtraDataRecords() != null && paramLocalFileHeader.getExtraDataRecords().size() > 0) {
      AESExtraDataRecord aESExtraDataRecord = readAesExtraDataRecord(paramLocalFileHeader.getExtraDataRecords(), paramRawIO);
      if (aESExtraDataRecord != null) {
        paramLocalFileHeader.setAesExtraDataRecord(aESExtraDataRecord);
        paramLocalFileHeader.setEncryptionMethod(EncryptionMethod.AES);
      } 
    } 
  }
  
  private CentralDirectory readCentralDirectory(RandomAccessFile paramRandomAccessFile, RawIO paramRawIO, Charset paramCharset) throws IOException {
    StringBuilder stringBuilder;
    CentralDirectory centralDirectory = new CentralDirectory();
    ArrayList<FileHeader> arrayList = new ArrayList();
    long l2 = HeaderUtil.getOffsetStartOfCentralDirectory(this.zipModel);
    long l1 = getNumberOfEntriesInCentralDirectory(this.zipModel);
    paramRandomAccessFile.seek(l2);
    byte[] arrayOfByte2 = new byte[2];
    byte[] arrayOfByte1 = new byte[4];
    byte b = 0;
    while (b < l1) {
      FileHeader fileHeader = new FileHeader();
      l2 = paramRawIO.readIntLittleEndian(paramRandomAccessFile);
      HeaderSignature headerSignature1 = HeaderSignature.CENTRAL_DIRECTORY;
      if (l2 == headerSignature1.getValue()) {
        fileHeader.setSignature(headerSignature1);
        fileHeader.setVersionMadeBy(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
        fileHeader.setVersionNeededToExtract(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
        byte[] arrayOfByte = new byte[2];
        paramRandomAccessFile.readFully(arrayOfByte);
        fileHeader.setEncrypted(BitUtils.isBitSet(arrayOfByte[0], 0));
        fileHeader.setDataDescriptorExists(BitUtils.isBitSet(arrayOfByte[0], 3));
        fileHeader.setFileNameUTF8Encoded(BitUtils.isBitSet(arrayOfByte[1], 3));
        fileHeader.setGeneralPurposeFlag((byte[])arrayOfByte.clone());
        fileHeader.setCompressionMethod(CompressionMethod.getCompressionMethodFromCode(paramRawIO.readShortLittleEndian(paramRandomAccessFile)));
        fileHeader.setLastModifiedTime(paramRawIO.readIntLittleEndian(paramRandomAccessFile));
        paramRandomAccessFile.readFully(arrayOfByte1);
        fileHeader.setCrc(paramRawIO.readLongLittleEndian(arrayOfByte1, 0));
        fileHeader.setCompressedSize(paramRawIO.readLongLittleEndian(paramRandomAccessFile, 4));
        fileHeader.setUncompressedSize(paramRawIO.readLongLittleEndian(paramRandomAccessFile, 4));
        int j = paramRawIO.readShortLittleEndian(paramRandomAccessFile);
        fileHeader.setFileNameLength(j);
        fileHeader.setExtraFieldLength(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
        int i = paramRawIO.readShortLittleEndian(paramRandomAccessFile);
        fileHeader.setFileCommentLength(i);
        fileHeader.setDiskNumberStart(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
        paramRandomAccessFile.readFully(arrayOfByte2);
        fileHeader.setInternalFileAttributes((byte[])arrayOfByte2.clone());
        paramRandomAccessFile.readFully(arrayOfByte1);
        fileHeader.setExternalFileAttributes((byte[])arrayOfByte1.clone());
        paramRandomAccessFile.readFully(arrayOfByte1);
        fileHeader.setOffsetLocalHeader(paramRawIO.readLongLittleEndian(arrayOfByte1, 0));
        if (j > 0) {
          arrayOfByte = new byte[j];
          paramRandomAccessFile.readFully(arrayOfByte);
          String str = HeaderUtil.decodeStringWithCharset(arrayOfByte, fileHeader.isFileNameUTF8Encoded(), paramCharset);
          if (str.contains(":\\"))
            str = str.substring(str.indexOf(":\\") + 2); 
          fileHeader.setFileName(str);
        } else {
          fileHeader.setFileName(null);
        } 
        fileHeader.setDirectory(isDirectory(fileHeader.getExternalFileAttributes(), fileHeader.getFileName()));
        readExtraDataRecords(paramRandomAccessFile, fileHeader);
        readZip64ExtendedInfo(fileHeader, paramRawIO);
        readAesExtraDataRecord(fileHeader, paramRawIO);
        if (i > 0) {
          arrayOfByte = new byte[i];
          paramRandomAccessFile.readFully(arrayOfByte);
          fileHeader.setFileComment(HeaderUtil.decodeStringWithCharset(arrayOfByte, fileHeader.isFileNameUTF8Encoded(), paramCharset));
        } 
        if (fileHeader.isEncrypted())
          if (fileHeader.getAesExtraDataRecord() != null) {
            fileHeader.setEncryptionMethod(EncryptionMethod.AES);
          } else {
            fileHeader.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
          }  
        arrayList.add(fileHeader);
        b++;
        continue;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Expected central directory entry not found (#");
      stringBuilder.append(b + 1);
      stringBuilder.append(")");
      throw new ZipException(stringBuilder.toString());
    } 
    centralDirectory.setFileHeaders(arrayList);
    DigitalSignature digitalSignature = new DigitalSignature();
    l1 = paramRawIO.readIntLittleEndian((RandomAccessFile)stringBuilder);
    HeaderSignature headerSignature = HeaderSignature.DIGITAL_SIGNATURE;
    if (l1 == headerSignature.getValue()) {
      digitalSignature.setSignature(headerSignature);
      digitalSignature.setSizeOfData(paramRawIO.readShortLittleEndian((RandomAccessFile)stringBuilder));
      if (digitalSignature.getSizeOfData() > 0) {
        byte[] arrayOfByte = new byte[digitalSignature.getSizeOfData()];
        stringBuilder.readFully(arrayOfByte);
        digitalSignature.setSignatureData(new String(arrayOfByte));
      } 
    } 
    return centralDirectory;
  }
  
  private EndOfCentralDirectoryRecord readEndOfCentralDirectoryRecord(RandomAccessFile paramRandomAccessFile, RawIO paramRawIO, Zip4jConfig paramZip4jConfig) throws IOException {
    long l = locateOffsetOfEndOfCentralDirectory(paramRandomAccessFile);
    seekInCurrentPart(paramRandomAccessFile, 4L + l);
    EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = new EndOfCentralDirectoryRecord();
    endOfCentralDirectoryRecord.setSignature(HeaderSignature.END_OF_CENTRAL_DIRECTORY);
    endOfCentralDirectoryRecord.setNumberOfThisDisk(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
    endOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDir(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
    endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
    endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
    endOfCentralDirectoryRecord.setSizeOfCentralDirectory(paramRawIO.readIntLittleEndian(paramRandomAccessFile));
    endOfCentralDirectoryRecord.setOffsetOfEndOfCentralDirectory(l);
    paramRandomAccessFile.readFully(this.intBuff);
    byte[] arrayOfByte = this.intBuff;
    boolean bool = false;
    endOfCentralDirectoryRecord.setOffsetOfStartOfCentralDirectory(paramRawIO.readLongLittleEndian(arrayOfByte, 0));
    endOfCentralDirectoryRecord.setComment(readZipComment(paramRandomAccessFile, paramRawIO.readShortLittleEndian(paramRandomAccessFile), paramZip4jConfig.getCharset()));
    ZipModel zipModel = this.zipModel;
    if (endOfCentralDirectoryRecord.getNumberOfThisDisk() > 0)
      bool = true; 
    zipModel.setSplitArchive(bool);
    return endOfCentralDirectoryRecord;
  }
  
  private List<ExtraDataRecord> readExtraDataRecords(InputStream paramInputStream, int paramInt) throws IOException {
    if (paramInt < 4) {
      if (paramInt > 0)
        paramInputStream.skip(paramInt); 
      return null;
    } 
    byte[] arrayOfByte = new byte[paramInt];
    Zip4jUtil.readFully(paramInputStream, arrayOfByte);
    try {
      return parseExtraDataRecords(arrayOfByte, paramInt);
    } catch (Exception exception) {
      return Collections.emptyList();
    } 
  }
  
  private List<ExtraDataRecord> readExtraDataRecords(RandomAccessFile paramRandomAccessFile, int paramInt) throws IOException {
    if (paramInt < 4) {
      if (paramInt > 0)
        paramRandomAccessFile.skipBytes(paramInt); 
      return null;
    } 
    byte[] arrayOfByte = new byte[paramInt];
    paramRandomAccessFile.read(arrayOfByte);
    try {
      return parseExtraDataRecords(arrayOfByte, paramInt);
    } catch (Exception exception) {
      return Collections.emptyList();
    } 
  }
  
  private void readExtraDataRecords(InputStream paramInputStream, LocalFileHeader paramLocalFileHeader) throws IOException {
    int i = paramLocalFileHeader.getExtraFieldLength();
    if (i <= 0)
      return; 
    paramLocalFileHeader.setExtraDataRecords(readExtraDataRecords(paramInputStream, i));
  }
  
  private void readExtraDataRecords(RandomAccessFile paramRandomAccessFile, FileHeader paramFileHeader) throws IOException {
    int i = paramFileHeader.getExtraFieldLength();
    if (i <= 0)
      return; 
    paramFileHeader.setExtraDataRecords(readExtraDataRecords(paramRandomAccessFile, i));
  }
  
  private Zip64EndOfCentralDirectoryRecord readZip64EndCentralDirRec(RandomAccessFile paramRandomAccessFile, RawIO paramRawIO) throws IOException {
    if (this.zipModel.getZip64EndOfCentralDirectoryLocator() != null) {
      long l = this.zipModel.getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord();
      if (l >= 0L) {
        paramRandomAccessFile.seek(l);
        Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = new Zip64EndOfCentralDirectoryRecord();
        l = paramRawIO.readIntLittleEndian(paramRandomAccessFile);
        HeaderSignature headerSignature = HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_RECORD;
        if (l == headerSignature.getValue()) {
          zip64EndOfCentralDirectoryRecord.setSignature(headerSignature);
          zip64EndOfCentralDirectoryRecord.setSizeOfZip64EndCentralDirectoryRecord(paramRawIO.readLongLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setVersionMadeBy(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setVersionNeededToExtract(paramRawIO.readShortLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setNumberOfThisDisk(paramRawIO.readIntLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDirectory(paramRawIO.readIntLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(paramRawIO.readLongLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(paramRawIO.readLongLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setSizeOfCentralDirectory(paramRawIO.readLongLittleEndian(paramRandomAccessFile));
          zip64EndOfCentralDirectoryRecord.setOffsetStartCentralDirectoryWRTStartDiskNumber(paramRawIO.readLongLittleEndian(paramRandomAccessFile));
          l = zip64EndOfCentralDirectoryRecord.getSizeOfZip64EndCentralDirectoryRecord() - 44L;
          if (l > 0L) {
            byte[] arrayOfByte = new byte[(int)l];
            paramRandomAccessFile.readFully(arrayOfByte);
            zip64EndOfCentralDirectoryRecord.setExtensibleDataSector(arrayOfByte);
          } 
          return zip64EndOfCentralDirectoryRecord;
        } 
        throw new ZipException("invalid signature for zip64 end of central directory record");
      } 
      throw new ZipException("invalid offset for start of end of central directory record");
    } 
    throw new ZipException("invalid zip64 end of central directory locator");
  }
  
  private Zip64EndOfCentralDirectoryLocator readZip64EndOfCentralDirectoryLocator(RandomAccessFile paramRandomAccessFile, RawIO paramRawIO, long paramLong) throws IOException {
    Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = new Zip64EndOfCentralDirectoryLocator();
    setFilePointerToReadZip64EndCentralDirLoc(paramRandomAccessFile, paramLong);
    paramLong = paramRawIO.readIntLittleEndian(paramRandomAccessFile);
    HeaderSignature headerSignature = HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_LOCATOR;
    if (paramLong == headerSignature.getValue()) {
      this.zipModel.setZip64Format(true);
      zip64EndOfCentralDirectoryLocator.setSignature(headerSignature);
      zip64EndOfCentralDirectoryLocator.setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(paramRawIO.readIntLittleEndian(paramRandomAccessFile));
      zip64EndOfCentralDirectoryLocator.setOffsetZip64EndOfCentralDirectoryRecord(paramRawIO.readLongLittleEndian(paramRandomAccessFile));
      zip64EndOfCentralDirectoryLocator.setTotalNumberOfDiscs(paramRawIO.readIntLittleEndian(paramRandomAccessFile));
      return zip64EndOfCentralDirectoryLocator;
    } 
    this.zipModel.setZip64Format(false);
    return null;
  }
  
  private Zip64ExtendedInfo readZip64ExtendedInfo(List<ExtraDataRecord> paramList, RawIO paramRawIO, long paramLong1, long paramLong2, long paramLong3, int paramInt) {
    Iterator<ExtraDataRecord> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      ExtraDataRecord extraDataRecord = iterator.next();
      if (extraDataRecord != null && HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue() == extraDataRecord.getHeader()) {
        Zip64ExtendedInfo zip64ExtendedInfo = new Zip64ExtendedInfo();
        byte[] arrayOfByte = extraDataRecord.getData();
        if (extraDataRecord.getSizeOfData() <= 0)
          return null; 
        int j = 0;
        int i = j;
        if (extraDataRecord.getSizeOfData() > 0) {
          i = j;
          if (paramLong1 == 4294967295L) {
            zip64ExtendedInfo.setUncompressedSize(paramRawIO.readLongLittleEndian(arrayOfByte, 0));
            i = 8;
          } 
        } 
        j = i;
        if (i < extraDataRecord.getSizeOfData()) {
          j = i;
          if (paramLong2 == 4294967295L) {
            zip64ExtendedInfo.setCompressedSize(paramRawIO.readLongLittleEndian(arrayOfByte, i));
            j = i + 8;
          } 
        } 
        i = j;
        if (j < extraDataRecord.getSizeOfData()) {
          i = j;
          if (paramLong3 == 4294967295L) {
            zip64ExtendedInfo.setOffsetLocalHeader(paramRawIO.readLongLittleEndian(arrayOfByte, j));
            i = j + 8;
          } 
        } 
        if (i < extraDataRecord.getSizeOfData() && paramInt == 65535)
          zip64ExtendedInfo.setDiskNumberStart(paramRawIO.readIntLittleEndian(arrayOfByte, i)); 
        return zip64ExtendedInfo;
      } 
    } 
    return null;
  }
  
  private void readZip64ExtendedInfo(FileHeader paramFileHeader, RawIO paramRawIO) {
    if (paramFileHeader.getExtraDataRecords() != null && paramFileHeader.getExtraDataRecords().size() > 0) {
      Zip64ExtendedInfo zip64ExtendedInfo = readZip64ExtendedInfo(paramFileHeader.getExtraDataRecords(), paramRawIO, paramFileHeader.getUncompressedSize(), paramFileHeader.getCompressedSize(), paramFileHeader.getOffsetLocalHeader(), paramFileHeader.getDiskNumberStart());
      if (zip64ExtendedInfo == null)
        return; 
      paramFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
      if (zip64ExtendedInfo.getUncompressedSize() != -1L)
        paramFileHeader.setUncompressedSize(zip64ExtendedInfo.getUncompressedSize()); 
      if (zip64ExtendedInfo.getCompressedSize() != -1L)
        paramFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize()); 
      if (zip64ExtendedInfo.getOffsetLocalHeader() != -1L)
        paramFileHeader.setOffsetLocalHeader(zip64ExtendedInfo.getOffsetLocalHeader()); 
      if (zip64ExtendedInfo.getDiskNumberStart() != -1)
        paramFileHeader.setDiskNumberStart(zip64ExtendedInfo.getDiskNumberStart()); 
    } 
  }
  
  private void readZip64ExtendedInfo(LocalFileHeader paramLocalFileHeader, RawIO paramRawIO) throws ZipException {
    if (paramLocalFileHeader != null) {
      if (paramLocalFileHeader.getExtraDataRecords() != null && paramLocalFileHeader.getExtraDataRecords().size() > 0) {
        Zip64ExtendedInfo zip64ExtendedInfo = readZip64ExtendedInfo(paramLocalFileHeader.getExtraDataRecords(), paramRawIO, paramLocalFileHeader.getUncompressedSize(), paramLocalFileHeader.getCompressedSize(), 0L, 0);
        if (zip64ExtendedInfo == null)
          return; 
        paramLocalFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
        if (zip64ExtendedInfo.getUncompressedSize() != -1L)
          paramLocalFileHeader.setUncompressedSize(zip64ExtendedInfo.getUncompressedSize()); 
        if (zip64ExtendedInfo.getCompressedSize() != -1L)
          paramLocalFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize()); 
      } 
      return;
    } 
    throw new ZipException("file header is null in reading Zip64 Extended Info");
  }
  
  private String readZipComment(RandomAccessFile paramRandomAccessFile, int paramInt, Charset paramCharset) {
    if (paramInt <= 0)
      return null; 
    try {
      byte[] arrayOfByte = new byte[paramInt];
      paramRandomAccessFile.readFully(arrayOfByte);
      if (paramCharset == null)
        paramCharset = InternalZipConstants.ZIP4J_DEFAULT_CHARSET; 
      return HeaderUtil.decodeStringWithCharset(arrayOfByte, false, paramCharset);
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  private void seekInCurrentPart(RandomAccessFile paramRandomAccessFile, long paramLong) throws IOException {
    if (paramRandomAccessFile instanceof NumberedSplitRandomAccessFile) {
      ((NumberedSplitRandomAccessFile)paramRandomAccessFile).seekInCurrentPart(paramLong);
    } else {
      paramRandomAccessFile.seek(paramLong);
    } 
  }
  
  private void setFilePointerToReadZip64EndCentralDirLoc(RandomAccessFile paramRandomAccessFile, long paramLong) throws IOException {
    seekInCurrentPart(paramRandomAccessFile, paramLong - 4L - 8L - 4L - 4L);
  }
  
  public boolean isDirectory(byte[] paramArrayOfbyte, String paramString) {
    boolean bool = false;
    if (paramArrayOfbyte[0] != 0 && BitUtils.isBitSet(paramArrayOfbyte[0], 4))
      return true; 
    if (paramArrayOfbyte[3] != 0 && BitUtils.isBitSet(paramArrayOfbyte[3], 6))
      return true; 
    null = bool;
    if (paramString != null) {
      if (!paramString.endsWith("/")) {
        null = bool;
        return paramString.endsWith("\\") ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public ZipModel readAllHeaders(RandomAccessFile paramRandomAccessFile, Zip4jConfig paramZip4jConfig) throws IOException {
    if (paramRandomAccessFile.length() >= 22L) {
      ZipModel zipModel = new ZipModel();
      this.zipModel = zipModel;
      try {
        zipModel.setEndOfCentralDirectoryRecord(readEndOfCentralDirectoryRecord(paramRandomAccessFile, this.rawIO, paramZip4jConfig));
        if (this.zipModel.getEndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory() == 0)
          return this.zipModel; 
        zipModel = this.zipModel;
        zipModel.setZip64EndOfCentralDirectoryLocator(readZip64EndOfCentralDirectoryLocator(paramRandomAccessFile, this.rawIO, zipModel.getEndOfCentralDirectoryRecord().getOffsetOfEndOfCentralDirectory()));
        if (this.zipModel.isZip64Format()) {
          this.zipModel.setZip64EndOfCentralDirectoryRecord(readZip64EndCentralDirRec(paramRandomAccessFile, this.rawIO));
          if (this.zipModel.getZip64EndOfCentralDirectoryRecord() != null && this.zipModel.getZip64EndOfCentralDirectoryRecord().getNumberOfThisDisk() > 0) {
            this.zipModel.setSplitArchive(true);
          } else {
            this.zipModel.setSplitArchive(false);
          } 
        } 
        this.zipModel.setCentralDirectory(readCentralDirectory(paramRandomAccessFile, this.rawIO, paramZip4jConfig.getCharset()));
        return this.zipModel;
      } catch (ZipException zipException) {
        throw zipException;
      } catch (IOException iOException) {
        iOException.printStackTrace();
        throw new ZipException("Zip headers not found. Probably not a zip file or a corrupted zip file", iOException);
      } 
    } 
    throw new ZipException("Zip file size less than minimum expected zip file size. Probably not a zip file or a corrupted zip file");
  }
  
  public DataDescriptor readDataDescriptor(InputStream paramInputStream, boolean paramBoolean) throws IOException {
    DataDescriptor dataDescriptor = new DataDescriptor();
    byte[] arrayOfByte = new byte[4];
    Zip4jUtil.readFully(paramInputStream, arrayOfByte);
    long l = this.rawIO.readLongLittleEndian(arrayOfByte, 0);
    HeaderSignature headerSignature = HeaderSignature.EXTRA_DATA_RECORD;
    if (l == headerSignature.getValue()) {
      dataDescriptor.setSignature(headerSignature);
      Zip4jUtil.readFully(paramInputStream, arrayOfByte);
      dataDescriptor.setCrc(this.rawIO.readLongLittleEndian(arrayOfByte, 0));
    } else {
      dataDescriptor.setCrc(l);
    } 
    if (paramBoolean) {
      dataDescriptor.setCompressedSize(this.rawIO.readLongLittleEndian(paramInputStream));
      dataDescriptor.setUncompressedSize(this.rawIO.readLongLittleEndian(paramInputStream));
    } else {
      dataDescriptor.setCompressedSize(this.rawIO.readIntLittleEndian(paramInputStream));
      dataDescriptor.setUncompressedSize(this.rawIO.readIntLittleEndian(paramInputStream));
    } 
    return dataDescriptor;
  }
  
  public LocalFileHeader readLocalFileHeader(InputStream paramInputStream, Charset paramCharset) throws IOException {
    LocalFileHeader localFileHeader = new LocalFileHeader();
    byte[] arrayOfByte1 = new byte[4];
    int j = this.rawIO.readIntLittleEndian(paramInputStream);
    int i = j;
    if (j == HeaderSignature.TEMPORARY_SPANNING_MARKER.getValue())
      i = this.rawIO.readIntLittleEndian(paramInputStream); 
    long l = i;
    HeaderSignature headerSignature = HeaderSignature.LOCAL_FILE_HEADER;
    if (l != headerSignature.getValue())
      return null; 
    localFileHeader.setSignature(headerSignature);
    localFileHeader.setVersionNeededToExtract(this.rawIO.readShortLittleEndian(paramInputStream));
    byte[] arrayOfByte2 = new byte[2];
    if (Zip4jUtil.readFully(paramInputStream, arrayOfByte2) == 2) {
      localFileHeader.setEncrypted(BitUtils.isBitSet(arrayOfByte2[0], 0));
      localFileHeader.setDataDescriptorExists(BitUtils.isBitSet(arrayOfByte2[0], 3));
      boolean bool = true;
      localFileHeader.setFileNameUTF8Encoded(BitUtils.isBitSet(arrayOfByte2[1], 3));
      localFileHeader.setGeneralPurposeFlag((byte[])arrayOfByte2.clone());
      localFileHeader.setCompressionMethod(CompressionMethod.getCompressionMethodFromCode(this.rawIO.readShortLittleEndian(paramInputStream)));
      localFileHeader.setLastModifiedTime(this.rawIO.readIntLittleEndian(paramInputStream));
      Zip4jUtil.readFully(paramInputStream, arrayOfByte1);
      localFileHeader.setCrc(this.rawIO.readLongLittleEndian(arrayOfByte1, 0));
      localFileHeader.setCompressedSize(this.rawIO.readLongLittleEndian(paramInputStream, 4));
      localFileHeader.setUncompressedSize(this.rawIO.readLongLittleEndian(paramInputStream, 4));
      i = this.rawIO.readShortLittleEndian(paramInputStream);
      localFileHeader.setFileNameLength(i);
      localFileHeader.setExtraFieldLength(this.rawIO.readShortLittleEndian(paramInputStream));
      if (i > 0) {
        arrayOfByte1 = new byte[i];
        Zip4jUtil.readFully(paramInputStream, arrayOfByte1);
        String str2 = HeaderUtil.decodeStringWithCharset(arrayOfByte1, localFileHeader.isFileNameUTF8Encoded(), paramCharset);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(":");
        stringBuilder.append(System.getProperty("file.separator"));
        String str1 = str2;
        if (str2.contains(stringBuilder.toString())) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(":");
          stringBuilder1.append(System.getProperty("file.separator"));
          str1 = str2.substring(str2.indexOf(stringBuilder1.toString()) + 2);
        } 
        localFileHeader.setFileName(str1);
        boolean bool1 = bool;
        if (!str1.endsWith("/"))
          if (str1.endsWith("\\")) {
            bool1 = bool;
          } else {
            bool1 = false;
          }  
        localFileHeader.setDirectory(bool1);
      } else {
        localFileHeader.setFileName(null);
      } 
      readExtraDataRecords(paramInputStream, localFileHeader);
      readZip64ExtendedInfo(localFileHeader, this.rawIO);
      readAesExtraDataRecord(localFileHeader, this.rawIO);
      if (localFileHeader.isEncrypted() && localFileHeader.getEncryptionMethod() != EncryptionMethod.AES)
        if (BitUtils.isBitSet(localFileHeader.getGeneralPurposeFlag()[0], 6)) {
          localFileHeader.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD_VARIANT_STRONG);
        } else {
          localFileHeader.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        }  
      return localFileHeader;
    } 
    throw new ZipException("Could not read enough bytes for generalPurposeFlags");
  }
}
