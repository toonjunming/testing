package net.lingala.zip4j.headers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.outputstream.CountingOutputStream;
import net.lingala.zip4j.io.outputstream.OutputStreamWithSplitZipSupport;
import net.lingala.zip4j.io.outputstream.SplitOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.util.RawIO;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderWriter {
  private static final short AES_EXTRA_DATA_RECORD_SIZE = 11;
  
  private static final short ZIP64_EXTRA_DATA_RECORD_SIZE_FH = 28;
  
  private static final short ZIP64_EXTRA_DATA_RECORD_SIZE_LFH = 16;
  
  private final byte[] intBuff = new byte[4];
  
  private final byte[] longBuff = new byte[8];
  
  private final RawIO rawIO = new RawIO();
  
  private Zip64EndOfCentralDirectoryRecord buildZip64EndOfCentralDirectoryRecord(ZipModel paramZipModel, int paramInt, long paramLong) throws ZipException {
    long l1;
    Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = new Zip64EndOfCentralDirectoryRecord();
    zip64EndOfCentralDirectoryRecord.setSignature(HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_RECORD);
    zip64EndOfCentralDirectoryRecord.setSizeOfZip64EndCentralDirectoryRecord(44L);
    if (paramZipModel.getCentralDirectory() != null && paramZipModel.getCentralDirectory().getFileHeaders() != null && paramZipModel.getCentralDirectory().getFileHeaders().size() > 0) {
      FileHeader fileHeader = paramZipModel.getCentralDirectory().getFileHeaders().get(0);
      zip64EndOfCentralDirectoryRecord.setVersionMadeBy(fileHeader.getVersionMadeBy());
      zip64EndOfCentralDirectoryRecord.setVersionNeededToExtract(fileHeader.getVersionNeededToExtract());
    } 
    zip64EndOfCentralDirectoryRecord.setNumberOfThisDisk(paramZipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
    zip64EndOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDirectory(paramZipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDiskStartOfCentralDir());
    long l2 = paramZipModel.getCentralDirectory().getFileHeaders().size();
    if (paramZipModel.isSplitArchive()) {
      l1 = countNumberOfFileHeaderEntriesOnDisk(paramZipModel.getCentralDirectory().getFileHeaders(), paramZipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
    } else {
      l1 = l2;
    } 
    zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(l1);
    zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(l2);
    zip64EndOfCentralDirectoryRecord.setSizeOfCentralDirectory(paramInt);
    zip64EndOfCentralDirectoryRecord.setOffsetStartCentralDirectoryWRTStartDiskNumber(paramLong);
    return zip64EndOfCentralDirectoryRecord;
  }
  
  private int calculateExtraDataRecordsSize(FileHeader paramFileHeader, boolean paramBoolean) {
    if (paramBoolean) {
      j = 32;
    } else {
      j = 0;
    } 
    int i = j;
    if (paramFileHeader.getAesExtraDataRecord() != null)
      i = j + 11; 
    int j = i;
    if (paramFileHeader.getExtraDataRecords() != null) {
      Iterator<ExtraDataRecord> iterator = paramFileHeader.getExtraDataRecords().iterator();
      while (true) {
        j = i;
        if (iterator.hasNext()) {
          ExtraDataRecord extraDataRecord = iterator.next();
          if (extraDataRecord.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue() || extraDataRecord.getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue())
            continue; 
          i += extraDataRecord.getSizeOfData() + 4;
          continue;
        } 
        break;
      } 
    } 
    return j;
  }
  
  private long countNumberOfFileHeaderEntriesOnDisk(List<FileHeader> paramList, int paramInt) throws ZipException {
    if (paramList != null) {
      byte b = 0;
      Iterator<FileHeader> iterator = paramList.iterator();
      while (iterator.hasNext()) {
        if (((FileHeader)iterator.next()).getDiskNumberStart() == paramInt)
          b++; 
      } 
      return b;
    } 
    throw new ZipException("file headers are null, cannot calculate number of entries on this disk");
  }
  
  private int getCurrentSplitFileCounter(OutputStream paramOutputStream) {
    return (paramOutputStream instanceof SplitOutputStream) ? ((SplitOutputStream)paramOutputStream).getCurrentSplitFileCounter() : ((CountingOutputStream)paramOutputStream).getCurrentSplitFileCounter();
  }
  
  private long getOffsetOfCentralDirectory(ZipModel paramZipModel) {
    return (paramZipModel.isZip64Format() && paramZipModel.getZip64EndOfCentralDirectoryRecord() != null && paramZipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() != -1L) ? paramZipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() : paramZipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
  }
  
  private boolean isSplitZipFile(OutputStream paramOutputStream) {
    return (paramOutputStream instanceof SplitOutputStream) ? ((SplitOutputStream)paramOutputStream).isSplitZipFile() : ((paramOutputStream instanceof CountingOutputStream) ? ((CountingOutputStream)paramOutputStream).isSplitZipFile() : false);
  }
  
  private boolean isZip64Entry(FileHeader paramFileHeader) {
    return (paramFileHeader.getCompressedSize() >= 4294967295L || paramFileHeader.getUncompressedSize() >= 4294967295L || paramFileHeader.getOffsetLocalHeader() >= 4294967295L || paramFileHeader.getDiskNumberStart() >= 65535);
  }
  
  private void processHeaderData(ZipModel paramZipModel, OutputStream paramOutputStream) throws IOException {
    byte b;
    if (paramOutputStream instanceof OutputStreamWithSplitZipSupport) {
      EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = paramZipModel.getEndOfCentralDirectoryRecord();
      OutputStreamWithSplitZipSupport outputStreamWithSplitZipSupport = (OutputStreamWithSplitZipSupport)paramOutputStream;
      endOfCentralDirectoryRecord.setOffsetOfStartOfCentralDirectory(outputStreamWithSplitZipSupport.getFilePointer());
      b = outputStreamWithSplitZipSupport.getCurrentSplitFileCounter();
    } else {
      b = 0;
    } 
    if (paramZipModel.isZip64Format()) {
      if (paramZipModel.getZip64EndOfCentralDirectoryRecord() == null)
        paramZipModel.setZip64EndOfCentralDirectoryRecord(new Zip64EndOfCentralDirectoryRecord()); 
      if (paramZipModel.getZip64EndOfCentralDirectoryLocator() == null)
        paramZipModel.setZip64EndOfCentralDirectoryLocator(new Zip64EndOfCentralDirectoryLocator()); 
      paramZipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(paramZipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory());
      paramZipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(b);
      paramZipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(b + 1);
    } 
    paramZipModel.getEndOfCentralDirectoryRecord().setNumberOfThisDisk(b);
    paramZipModel.getEndOfCentralDirectoryRecord().setNumberOfThisDiskStartOfCentralDir(b);
  }
  
  private void updateFileSizesInLocalFileHeader(SplitOutputStream paramSplitOutputStream, FileHeader paramFileHeader) throws IOException {
    StringBuilder stringBuilder;
    if (paramFileHeader.getUncompressedSize() >= 4294967295L) {
      this.rawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
      paramSplitOutputStream.write(this.longBuff, 0, 4);
      paramSplitOutputStream.write(this.longBuff, 0, 4);
      int i = paramFileHeader.getFileNameLength() + 4 + 2 + 2;
      if (paramSplitOutputStream.skipBytes(i) == i) {
        this.rawIO.writeLongLittleEndian(paramSplitOutputStream, paramFileHeader.getUncompressedSize());
        this.rawIO.writeLongLittleEndian(paramSplitOutputStream, paramFileHeader.getCompressedSize());
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to skip ");
        stringBuilder.append(i);
        stringBuilder.append(" bytes to update LFH");
        throw new ZipException(stringBuilder.toString());
      } 
    } else {
      this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getCompressedSize());
      stringBuilder.write(this.longBuff, 0, 4);
      this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getUncompressedSize());
      stringBuilder.write(this.longBuff, 0, 4);
    } 
  }
  
  private void writeCentralDirectory(ZipModel paramZipModel, ByteArrayOutputStream paramByteArrayOutputStream, RawIO paramRawIO, Charset paramCharset) throws ZipException {
    if (paramZipModel.getCentralDirectory() != null && paramZipModel.getCentralDirectory().getFileHeaders() != null && paramZipModel.getCentralDirectory().getFileHeaders().size() > 0) {
      Iterator<FileHeader> iterator = paramZipModel.getCentralDirectory().getFileHeaders().iterator();
      while (iterator.hasNext())
        writeFileHeader(paramZipModel, iterator.next(), paramByteArrayOutputStream, paramRawIO, paramCharset); 
    } 
  }
  
  private void writeEndOfCentralDirectoryRecord(ZipModel paramZipModel, int paramInt, long paramLong, ByteArrayOutputStream paramByteArrayOutputStream, RawIO paramRawIO, Charset paramCharset) throws IOException {
    byte[] arrayOfByte = new byte[8];
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, (int)HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue());
    paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramZipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
    paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramZipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDiskStartOfCentralDir());
    long l2 = paramZipModel.getCentralDirectory().getFileHeaders().size();
    if (paramZipModel.isSplitArchive()) {
      l1 = countNumberOfFileHeaderEntriesOnDisk(paramZipModel.getCentralDirectory().getFileHeaders(), paramZipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
    } else {
      l1 = l2;
    } 
    long l3 = l1;
    if (l1 > 65535L)
      l3 = 65535L; 
    paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, (int)l3);
    long l1 = l2;
    if (l2 > 65535L)
      l1 = 65535L; 
    paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, (int)l1);
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, paramInt);
    if (paramLong > 4294967295L) {
      paramRawIO.writeLongLittleEndian(arrayOfByte, 0, 4294967295L);
      paramByteArrayOutputStream.write(arrayOfByte, 0, 4);
    } else {
      paramRawIO.writeLongLittleEndian(arrayOfByte, 0, paramLong);
      paramByteArrayOutputStream.write(arrayOfByte, 0, 4);
    } 
    String str = paramZipModel.getEndOfCentralDirectoryRecord().getComment();
    if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
      byte[] arrayOfByte1 = HeaderUtil.getBytesFromString(str, paramCharset);
      paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, arrayOfByte1.length);
      paramByteArrayOutputStream.write(arrayOfByte1);
    } else {
      paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, 0);
    } 
  }
  
  private void writeFileHeader(ZipModel paramZipModel, FileHeader paramFileHeader, ByteArrayOutputStream paramByteArrayOutputStream, RawIO paramRawIO, Charset paramCharset) throws ZipException {
    if (paramFileHeader != null)
      try {
        byte[] arrayOfByte3 = new byte[2];
        arrayOfByte3[0] = 0;
        arrayOfByte3[1] = 0;
        boolean bool = isZip64Entry(paramFileHeader);
        paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, (int)paramFileHeader.getSignature().getValue());
        paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramFileHeader.getVersionMadeBy());
        paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramFileHeader.getVersionNeededToExtract());
        paramByteArrayOutputStream.write(paramFileHeader.getGeneralPurposeFlag());
        paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramFileHeader.getCompressionMethod().getCode());
        paramRawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getLastModifiedTime());
        paramByteArrayOutputStream.write(this.longBuff, 0, 4);
        paramRawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getCrc());
        paramByteArrayOutputStream.write(this.longBuff, 0, 4);
        if (bool) {
          paramRawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
          paramByteArrayOutputStream.write(this.longBuff, 0, 4);
          paramByteArrayOutputStream.write(this.longBuff, 0, 4);
          paramZipModel.setZip64Format(true);
        } else {
          paramRawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getCompressedSize());
          paramByteArrayOutputStream.write(this.longBuff, 0, 4);
          paramRawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getUncompressedSize());
          paramByteArrayOutputStream.write(this.longBuff, 0, 4);
        } 
        byte[] arrayOfByte1 = new byte[0];
        if (Zip4jUtil.isStringNotNullAndNotEmpty(paramFileHeader.getFileName()))
          arrayOfByte1 = HeaderUtil.getBytesFromString(paramFileHeader.getFileName(), paramCharset); 
        paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, arrayOfByte1.length);
        byte[] arrayOfByte4 = new byte[4];
        if (bool) {
          paramRawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
          System.arraycopy(this.longBuff, 0, arrayOfByte4, 0, 4);
        } else {
          paramRawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getOffsetLocalHeader());
          System.arraycopy(this.longBuff, 0, arrayOfByte4, 0, 4);
        } 
        paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, calculateExtraDataRecordsSize(paramFileHeader, bool));
        String str = paramFileHeader.getFileComment();
        byte[] arrayOfByte2 = new byte[0];
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str))
          arrayOfByte2 = HeaderUtil.getBytesFromString(str, paramCharset); 
        paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, arrayOfByte2.length);
        if (bool) {
          paramRawIO.writeIntLittleEndian(this.intBuff, 0, 65535);
          paramByteArrayOutputStream.write(this.intBuff, 0, 2);
        } else {
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramFileHeader.getDiskNumberStart());
        } 
        paramByteArrayOutputStream.write(arrayOfByte3);
        paramByteArrayOutputStream.write(paramFileHeader.getExternalFileAttributes());
        paramByteArrayOutputStream.write(arrayOfByte4);
        if (arrayOfByte1.length > 0)
          paramByteArrayOutputStream.write(arrayOfByte1); 
        if (bool) {
          paramZipModel.setZip64Format(true);
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, (int)HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue());
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, 28);
          paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramFileHeader.getUncompressedSize());
          paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramFileHeader.getCompressedSize());
          paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramFileHeader.getOffsetLocalHeader());
          paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, paramFileHeader.getDiskNumberStart());
        } 
        if (paramFileHeader.getAesExtraDataRecord() != null) {
          AESExtraDataRecord aESExtraDataRecord = paramFileHeader.getAesExtraDataRecord();
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, (int)aESExtraDataRecord.getSignature().getValue());
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, aESExtraDataRecord.getDataSize());
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, aESExtraDataRecord.getAesVersion().getVersionNumber());
          paramByteArrayOutputStream.write(aESExtraDataRecord.getVendorID().getBytes());
          paramByteArrayOutputStream.write(new byte[] { (byte)aESExtraDataRecord.getAesKeyStrength().getRawCode() });
          paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, aESExtraDataRecord.getCompressionMethod().getCode());
        } 
        writeRemainingExtraDataRecordsIfPresent(paramFileHeader, paramByteArrayOutputStream);
        if (arrayOfByte2.length > 0)
          paramByteArrayOutputStream.write(arrayOfByte2); 
        return;
      } catch (Exception exception) {
        throw new ZipException(exception);
      }  
    throw new ZipException("input parameters is null, cannot write local file header");
  }
  
  private void writeRemainingExtraDataRecordsIfPresent(FileHeader paramFileHeader, OutputStream paramOutputStream) throws IOException {
    if (paramFileHeader.getExtraDataRecords() != null && paramFileHeader.getExtraDataRecords().size() != 0)
      for (ExtraDataRecord extraDataRecord : paramFileHeader.getExtraDataRecords()) {
        if (extraDataRecord.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue() || extraDataRecord.getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue())
          continue; 
        this.rawIO.writeShortLittleEndian(paramOutputStream, (int)extraDataRecord.getHeader());
        this.rawIO.writeShortLittleEndian(paramOutputStream, extraDataRecord.getSizeOfData());
        if (extraDataRecord.getSizeOfData() > 0 && extraDataRecord.getData() != null)
          paramOutputStream.write(extraDataRecord.getData()); 
      }  
  }
  
  private void writeZip64EndOfCentralDirectoryLocator(Zip64EndOfCentralDirectoryLocator paramZip64EndOfCentralDirectoryLocator, ByteArrayOutputStream paramByteArrayOutputStream, RawIO paramRawIO) throws IOException {
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, (int)HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_LOCATOR.getValue());
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryLocator.getNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord());
    paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryLocator.getOffsetZip64EndOfCentralDirectoryRecord());
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryLocator.getTotalNumberOfDiscs());
  }
  
  private void writeZip64EndOfCentralDirectoryRecord(Zip64EndOfCentralDirectoryRecord paramZip64EndOfCentralDirectoryRecord, ByteArrayOutputStream paramByteArrayOutputStream, RawIO paramRawIO) throws IOException {
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, (int)paramZip64EndOfCentralDirectoryRecord.getSignature().getValue());
    paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getSizeOfZip64EndCentralDirectoryRecord());
    paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getVersionMadeBy());
    paramRawIO.writeShortLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getVersionNeededToExtract());
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getNumberOfThisDisk());
    paramRawIO.writeIntLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getNumberOfThisDiskStartOfCentralDirectory());
    paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectoryOnThisDisk());
    paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectory());
    paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getSizeOfCentralDirectory());
    paramRawIO.writeLongLittleEndian(paramByteArrayOutputStream, paramZip64EndOfCentralDirectoryRecord.getOffsetStartCentralDirectoryWRTStartDiskNumber());
  }
  
  private void writeZipHeaderBytes(ZipModel paramZipModel, OutputStream paramOutputStream, byte[] paramArrayOfbyte, Charset paramCharset) throws IOException {
    if (paramArrayOfbyte != null) {
      if (paramOutputStream instanceof CountingOutputStream && ((CountingOutputStream)paramOutputStream).checkBuffSizeAndStartNextSplitFile(paramArrayOfbyte.length)) {
        finalizeZipFile(paramZipModel, paramOutputStream, paramCharset);
        return;
      } 
      paramOutputStream.write(paramArrayOfbyte);
      return;
    } 
    throw new ZipException("invalid buff to write as zip headers");
  }
  
  public void finalizeZipFile(ZipModel paramZipModel, OutputStream paramOutputStream, Charset paramCharset) throws IOException {
    if (paramZipModel != null && paramOutputStream != null) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      try {
        processHeaderData(paramZipModel, paramOutputStream);
        long l = getOffsetOfCentralDirectory(paramZipModel);
        writeCentralDirectory(paramZipModel, byteArrayOutputStream, this.rawIO, paramCharset);
        int i = byteArrayOutputStream.size();
        if (paramZipModel.isZip64Format() || l >= 4294967295L || paramZipModel.getCentralDirectory().getFileHeaders().size() >= 65535) {
          if (paramZipModel.getZip64EndOfCentralDirectoryRecord() == null) {
            Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord1 = new Zip64EndOfCentralDirectoryRecord();
            this();
            paramZipModel.setZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryRecord1);
          } 
          if (paramZipModel.getZip64EndOfCentralDirectoryLocator() == null) {
            Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = new Zip64EndOfCentralDirectoryLocator();
            this();
            paramZipModel.setZip64EndOfCentralDirectoryLocator(zip64EndOfCentralDirectoryLocator);
          } 
          paramZipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(i + l);
          if (isSplitZipFile(paramOutputStream)) {
            int j = getCurrentSplitFileCounter(paramOutputStream);
            paramZipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(j);
            paramZipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(j + 1);
          } else {
            paramZipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(0);
            paramZipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(1);
          } 
          Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = buildZip64EndOfCentralDirectoryRecord(paramZipModel, i, l);
          paramZipModel.setZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryRecord);
          writeZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryRecord, byteArrayOutputStream, this.rawIO);
          writeZip64EndOfCentralDirectoryLocator(paramZipModel.getZip64EndOfCentralDirectoryLocator(), byteArrayOutputStream, this.rawIO);
        } 
        writeEndOfCentralDirectoryRecord(paramZipModel, i, l, byteArrayOutputStream, this.rawIO, paramCharset);
        writeZipHeaderBytes(paramZipModel, paramOutputStream, byteArrayOutputStream.toByteArray(), paramCharset);
        return;
      } finally {
        paramOutputStream = null;
      } 
    } 
    throw new ZipException("input parameters is null, cannot finalize zip file");
  }
  
  public void finalizeZipFileWithoutValidations(ZipModel paramZipModel, OutputStream paramOutputStream, Charset paramCharset) throws IOException {
    if (paramZipModel != null && paramOutputStream != null) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      try {
        long l = paramZipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
        writeCentralDirectory(paramZipModel, byteArrayOutputStream, this.rawIO, paramCharset);
        int i = byteArrayOutputStream.size();
        if (paramZipModel.isZip64Format() || l >= 4294967295L || paramZipModel.getCentralDirectory().getFileHeaders().size() >= 65535) {
          if (paramZipModel.getZip64EndOfCentralDirectoryRecord() == null) {
            Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord1 = new Zip64EndOfCentralDirectoryRecord();
            this();
            paramZipModel.setZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryRecord1);
          } 
          if (paramZipModel.getZip64EndOfCentralDirectoryLocator() == null) {
            Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = new Zip64EndOfCentralDirectoryLocator();
            this();
            paramZipModel.setZip64EndOfCentralDirectoryLocator(zip64EndOfCentralDirectoryLocator);
          } 
          paramZipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(i + l);
          Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = buildZip64EndOfCentralDirectoryRecord(paramZipModel, i, l);
          paramZipModel.setZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryRecord);
          writeZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryRecord, byteArrayOutputStream, this.rawIO);
          writeZip64EndOfCentralDirectoryLocator(paramZipModel.getZip64EndOfCentralDirectoryLocator(), byteArrayOutputStream, this.rawIO);
        } 
        writeEndOfCentralDirectoryRecord(paramZipModel, i, l, byteArrayOutputStream, this.rawIO, paramCharset);
        writeZipHeaderBytes(paramZipModel, paramOutputStream, byteArrayOutputStream.toByteArray(), paramCharset);
        return;
      } finally {
        paramZipModel = null;
      } 
    } 
    throw new ZipException("input parameters is null, cannot finalize zip file without validations");
  }
  
  public void updateLocalFileHeader(FileHeader paramFileHeader, ZipModel paramZipModel, SplitOutputStream paramSplitOutputStream) throws IOException {
    if (paramFileHeader != null && paramZipModel != null) {
      SplitOutputStream splitOutputStream;
      int j = paramFileHeader.getDiskNumberStart();
      int i = paramSplitOutputStream.getCurrentSplitFileCounter();
      boolean bool = true;
      if (j != i) {
        String str1;
        String str3 = paramZipModel.getZipFile().getParent();
        String str2 = FileUtils.getZipFileNameWithoutExtension(paramZipModel.getZipFile().getName());
        if (str3 != null) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str3);
          stringBuilder.append(System.getProperty("file.separator"));
          str1 = stringBuilder.toString();
        } else {
          str1 = "";
        } 
        if (paramFileHeader.getDiskNumberStart() < 9) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append(str2);
          stringBuilder.append(".z0");
          stringBuilder.append(paramFileHeader.getDiskNumberStart() + 1);
          str1 = stringBuilder.toString();
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append(str2);
          stringBuilder.append(".z");
          stringBuilder.append(paramFileHeader.getDiskNumberStart() + 1);
          str1 = stringBuilder.toString();
        } 
        splitOutputStream = new SplitOutputStream(new File(str1));
      } else {
        splitOutputStream = paramSplitOutputStream;
        bool = false;
      } 
      long l = splitOutputStream.getFilePointer();
      splitOutputStream.seek(paramFileHeader.getOffsetLocalHeader() + 14L);
      this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramFileHeader.getCrc());
      splitOutputStream.write(this.longBuff, 0, 4);
      updateFileSizesInLocalFileHeader(splitOutputStream, paramFileHeader);
      if (bool) {
        splitOutputStream.close();
      } else {
        paramSplitOutputStream.seek(l);
      } 
      return;
    } 
    throw new ZipException("invalid input parameters, cannot update local file header");
  }
  
  public void writeExtendedLocalHeader(LocalFileHeader paramLocalFileHeader, OutputStream paramOutputStream) throws IOException {
    if (paramLocalFileHeader != null && paramOutputStream != null) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      try {
        this.rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)HeaderSignature.EXTRA_DATA_RECORD.getValue());
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getCrc());
        byteArrayOutputStream.write(this.longBuff, 0, 4);
        if (paramLocalFileHeader.isWriteCompressedSizeInZip64ExtraRecord()) {
          this.rawIO.writeLongLittleEndian(byteArrayOutputStream, paramLocalFileHeader.getCompressedSize());
          this.rawIO.writeLongLittleEndian(byteArrayOutputStream, paramLocalFileHeader.getUncompressedSize());
        } else {
          this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getCompressedSize());
          byteArrayOutputStream.write(this.longBuff, 0, 4);
          this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getUncompressedSize());
          byteArrayOutputStream.write(this.longBuff, 0, 4);
        } 
        paramOutputStream.write(byteArrayOutputStream.toByteArray());
        return;
      } finally {
        paramLocalFileHeader = null;
      } 
    } 
    throw new ZipException("input parameters is null, cannot write extended local header");
  }
  
  public void writeLocalFileHeader(ZipModel paramZipModel, LocalFileHeader paramLocalFileHeader, OutputStream paramOutputStream, Charset paramCharset) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      byte b;
      boolean bool;
      this.rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)paramLocalFileHeader.getSignature().getValue());
      this.rawIO.writeShortLittleEndian(byteArrayOutputStream, paramLocalFileHeader.getVersionNeededToExtract());
      byteArrayOutputStream.write(paramLocalFileHeader.getGeneralPurposeFlag());
      this.rawIO.writeShortLittleEndian(byteArrayOutputStream, paramLocalFileHeader.getCompressionMethod().getCode());
      this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getLastModifiedTime());
      byteArrayOutputStream.write(this.longBuff, 0, 4);
      this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getCrc());
      byteArrayOutputStream.write(this.longBuff, 0, 4);
      if (paramLocalFileHeader.getCompressedSize() >= 4294967295L || paramLocalFileHeader.getUncompressedSize() >= 4294967295L) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
        byteArrayOutputStream.write(this.longBuff, 0, 4);
        byteArrayOutputStream.write(this.longBuff, 0, 4);
        paramZipModel.setZip64Format(true);
        paramLocalFileHeader.setWriteCompressedSizeInZip64ExtraRecord(true);
      } else {
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getCompressedSize());
        byteArrayOutputStream.write(this.longBuff, 0, 4);
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, paramLocalFileHeader.getUncompressedSize());
        byteArrayOutputStream.write(this.longBuff, 0, 4);
        paramLocalFileHeader.setWriteCompressedSizeInZip64ExtraRecord(false);
      } 
      byte[] arrayOfByte = new byte[0];
      if (Zip4jUtil.isStringNotNullAndNotEmpty(paramLocalFileHeader.getFileName()))
        arrayOfByte = HeaderUtil.getBytesFromString(paramLocalFileHeader.getFileName(), paramCharset); 
      this.rawIO.writeShortLittleEndian(byteArrayOutputStream, arrayOfByte.length);
      if (bool) {
        b = 20;
      } else {
        b = 0;
      } 
      int i = b;
      if (paramLocalFileHeader.getAesExtraDataRecord() != null)
        i = b + 11; 
      this.rawIO.writeShortLittleEndian(byteArrayOutputStream, i);
      if (arrayOfByte.length > 0)
        byteArrayOutputStream.write(arrayOfByte); 
      if (bool) {
        this.rawIO.writeShortLittleEndian(byteArrayOutputStream, (int)HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue());
        this.rawIO.writeShortLittleEndian(byteArrayOutputStream, 16);
        this.rawIO.writeLongLittleEndian(byteArrayOutputStream, paramLocalFileHeader.getUncompressedSize());
        this.rawIO.writeLongLittleEndian(byteArrayOutputStream, paramLocalFileHeader.getCompressedSize());
      } 
      if (paramLocalFileHeader.getAesExtraDataRecord() != null) {
        AESExtraDataRecord aESExtraDataRecord = paramLocalFileHeader.getAesExtraDataRecord();
        this.rawIO.writeShortLittleEndian(byteArrayOutputStream, (int)aESExtraDataRecord.getSignature().getValue());
        this.rawIO.writeShortLittleEndian(byteArrayOutputStream, aESExtraDataRecord.getDataSize());
        this.rawIO.writeShortLittleEndian(byteArrayOutputStream, aESExtraDataRecord.getAesVersion().getVersionNumber());
        byteArrayOutputStream.write(aESExtraDataRecord.getVendorID().getBytes());
        byteArrayOutputStream.write(new byte[] { (byte)aESExtraDataRecord.getAesKeyStrength().getRawCode() });
        this.rawIO.writeShortLittleEndian(byteArrayOutputStream, aESExtraDataRecord.getCompressionMethod().getCode());
      } 
      paramOutputStream.write(byteArrayOutputStream.toByteArray());
      return;
    } finally {
      paramZipModel = null;
    } 
  }
}
