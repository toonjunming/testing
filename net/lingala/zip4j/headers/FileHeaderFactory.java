package net.lingala.zip4j.headers;

import java.nio.charset.Charset;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.BitUtils;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.RawIO;
import net.lingala.zip4j.util.Zip4jUtil;
import net.lingala.zip4j.util.ZipVersionUtils;

public class FileHeaderFactory {
  private int determineFileNameLength(String paramString, Charset paramCharset) {
    return (HeaderUtil.getBytesFromString(paramString, paramCharset)).length;
  }
  
  private byte[] determineGeneralPurposeBitFlag(boolean paramBoolean, ZipParameters paramZipParameters, Charset paramCharset) {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = generateFirstGeneralPurposeByte(paramBoolean, paramZipParameters);
    if (paramCharset == null || InternalZipConstants.CHARSET_UTF_8.equals(paramCharset))
      arrayOfByte[1] = BitUtils.setBit(arrayOfByte[1], 3); 
    return arrayOfByte;
  }
  
  private AESExtraDataRecord generateAESExtraDataRecord(ZipParameters paramZipParameters) throws ZipException {
    AESExtraDataRecord aESExtraDataRecord = new AESExtraDataRecord();
    if (paramZipParameters.getAesVersion() != null)
      aESExtraDataRecord.setAesVersion(paramZipParameters.getAesVersion()); 
    AesKeyStrength aesKeyStrength2 = paramZipParameters.getAesKeyStrength();
    AesKeyStrength aesKeyStrength1 = AesKeyStrength.KEY_STRENGTH_128;
    if (aesKeyStrength2 == aesKeyStrength1) {
      aESExtraDataRecord.setAesKeyStrength(aesKeyStrength1);
    } else {
      aesKeyStrength2 = paramZipParameters.getAesKeyStrength();
      aesKeyStrength1 = AesKeyStrength.KEY_STRENGTH_192;
      if (aesKeyStrength2 == aesKeyStrength1) {
        aESExtraDataRecord.setAesKeyStrength(aesKeyStrength1);
      } else {
        aesKeyStrength2 = paramZipParameters.getAesKeyStrength();
        aesKeyStrength1 = AesKeyStrength.KEY_STRENGTH_256;
        if (aesKeyStrength2 == aesKeyStrength1) {
          aESExtraDataRecord.setAesKeyStrength(aesKeyStrength1);
          aESExtraDataRecord.setCompressionMethod(paramZipParameters.getCompressionMethod());
          return aESExtraDataRecord;
        } 
        throw new ZipException("invalid AES key strength");
      } 
    } 
    aESExtraDataRecord.setCompressionMethod(paramZipParameters.getCompressionMethod());
    return aESExtraDataRecord;
  }
  
  private byte generateFirstGeneralPurposeByte(boolean paramBoolean, ZipParameters paramZipParameters) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iload_1
    //   4: ifeq -> 14
    //   7: iconst_0
    //   8: iconst_0
    //   9: invokestatic setBit : (BI)B
    //   12: istore #4
    //   14: iload #4
    //   16: istore_3
    //   17: getstatic net/lingala/zip4j/model/enums/CompressionMethod.DEFLATE : Lnet/lingala/zip4j/model/enums/CompressionMethod;
    //   20: aload_2
    //   21: invokevirtual getCompressionMethod : ()Lnet/lingala/zip4j/model/enums/CompressionMethod;
    //   24: invokevirtual equals : (Ljava/lang/Object;)Z
    //   27: ifeq -> 151
    //   30: getstatic net/lingala/zip4j/model/enums/CompressionLevel.NORMAL : Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   33: aload_2
    //   34: invokevirtual getCompressionLevel : ()Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   37: invokevirtual equals : (Ljava/lang/Object;)Z
    //   40: ifeq -> 57
    //   43: iload #4
    //   45: iconst_1
    //   46: invokestatic unsetBit : (BI)B
    //   49: iconst_2
    //   50: invokestatic unsetBit : (BI)B
    //   53: istore_3
    //   54: goto -> 151
    //   57: getstatic net/lingala/zip4j/model/enums/CompressionLevel.MAXIMUM : Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   60: aload_2
    //   61: invokevirtual getCompressionLevel : ()Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   64: invokevirtual equals : (Ljava/lang/Object;)Z
    //   67: ifeq -> 84
    //   70: iload #4
    //   72: iconst_1
    //   73: invokestatic setBit : (BI)B
    //   76: iconst_2
    //   77: invokestatic unsetBit : (BI)B
    //   80: istore_3
    //   81: goto -> 151
    //   84: getstatic net/lingala/zip4j/model/enums/CompressionLevel.FAST : Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   87: aload_2
    //   88: invokevirtual getCompressionLevel : ()Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   91: invokevirtual equals : (Ljava/lang/Object;)Z
    //   94: ifeq -> 111
    //   97: iload #4
    //   99: iconst_1
    //   100: invokestatic unsetBit : (BI)B
    //   103: iconst_2
    //   104: invokestatic setBit : (BI)B
    //   107: istore_3
    //   108: goto -> 151
    //   111: getstatic net/lingala/zip4j/model/enums/CompressionLevel.FASTEST : Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   114: aload_2
    //   115: invokevirtual getCompressionLevel : ()Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   118: invokevirtual equals : (Ljava/lang/Object;)Z
    //   121: ifne -> 140
    //   124: iload #4
    //   126: istore_3
    //   127: getstatic net/lingala/zip4j/model/enums/CompressionLevel.ULTRA : Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   130: aload_2
    //   131: invokevirtual getCompressionLevel : ()Lnet/lingala/zip4j/model/enums/CompressionLevel;
    //   134: invokevirtual equals : (Ljava/lang/Object;)Z
    //   137: ifeq -> 151
    //   140: iload #4
    //   142: iconst_1
    //   143: invokestatic setBit : (BI)B
    //   146: iconst_2
    //   147: invokestatic setBit : (BI)B
    //   150: istore_3
    //   151: iload_3
    //   152: istore #4
    //   154: aload_2
    //   155: invokevirtual isWriteExtendedLocalFileHeader : ()Z
    //   158: ifeq -> 168
    //   161: iload_3
    //   162: iconst_3
    //   163: invokestatic setBit : (BI)B
    //   166: istore #4
    //   168: iload #4
    //   170: ireturn
  }
  
  private String validateAndGetFileName(String paramString) throws ZipException {
    if (Zip4jUtil.isStringNotNullAndNotEmpty(paramString))
      return paramString; 
    throw new ZipException("fileNameInZip is null or empty");
  }
  
  public FileHeader generateFileHeader(ZipParameters paramZipParameters, boolean paramBoolean, int paramInt, Charset paramCharset, RawIO paramRawIO) throws ZipException {
    FileHeader fileHeader = new FileHeader();
    fileHeader.setSignature(HeaderSignature.CENTRAL_DIRECTORY);
    fileHeader.setVersionMadeBy(ZipVersionUtils.determineVersionMadeBy(paramZipParameters, paramRawIO));
    fileHeader.setVersionNeededToExtract(ZipVersionUtils.determineVersionNeededToExtract(paramZipParameters).getCode());
    if (paramZipParameters.isEncryptFiles() && paramZipParameters.getEncryptionMethod() == EncryptionMethod.AES) {
      fileHeader.setCompressionMethod(CompressionMethod.AES_INTERNAL_ONLY);
      fileHeader.setAesExtraDataRecord(generateAESExtraDataRecord(paramZipParameters));
      fileHeader.setExtraFieldLength(fileHeader.getExtraFieldLength() + 11);
    } else {
      fileHeader.setCompressionMethod(paramZipParameters.getCompressionMethod());
    } 
    if (paramZipParameters.isEncryptFiles())
      if (paramZipParameters.getEncryptionMethod() != null && paramZipParameters.getEncryptionMethod() != EncryptionMethod.NONE) {
        fileHeader.setEncrypted(true);
        fileHeader.setEncryptionMethod(paramZipParameters.getEncryptionMethod());
      } else {
        throw new ZipException("Encryption method has to be set when encryptFiles flag is set in zip parameters");
      }  
    String str = validateAndGetFileName(paramZipParameters.getFileNameInZip());
    fileHeader.setFileName(str);
    fileHeader.setFileNameLength(determineFileNameLength(str, paramCharset));
    if (!paramBoolean)
      paramInt = 0; 
    fileHeader.setDiskNumberStart(paramInt);
    if (paramZipParameters.getLastModifiedFileTime() > 0L) {
      fileHeader.setLastModifiedTime(Zip4jUtil.epochToExtendedDosTime(paramZipParameters.getLastModifiedFileTime()));
    } else {
      fileHeader.setLastModifiedTime(Zip4jUtil.epochToExtendedDosTime(System.currentTimeMillis()));
    } 
    paramBoolean = FileUtils.isZipEntryDirectory(str);
    fileHeader.setDirectory(paramBoolean);
    fileHeader.setExternalFileAttributes(FileUtils.getDefaultFileAttributes(paramBoolean));
    if (paramZipParameters.isWriteExtendedLocalFileHeader() && paramZipParameters.getEntrySize() == -1L) {
      fileHeader.setUncompressedSize(0L);
    } else {
      fileHeader.setUncompressedSize(paramZipParameters.getEntrySize());
    } 
    if (paramZipParameters.isEncryptFiles() && paramZipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD)
      fileHeader.setCrc(paramZipParameters.getEntryCRC()); 
    fileHeader.setGeneralPurposeFlag(determineGeneralPurposeBitFlag(fileHeader.isEncrypted(), paramZipParameters, paramCharset));
    fileHeader.setDataDescriptorExists(paramZipParameters.isWriteExtendedLocalFileHeader());
    fileHeader.setFileComment(paramZipParameters.getFileComment());
    return fileHeader;
  }
  
  public LocalFileHeader generateLocalFileHeader(FileHeader paramFileHeader) {
    LocalFileHeader localFileHeader = new LocalFileHeader();
    localFileHeader.setSignature(HeaderSignature.LOCAL_FILE_HEADER);
    localFileHeader.setVersionNeededToExtract(paramFileHeader.getVersionNeededToExtract());
    localFileHeader.setCompressionMethod(paramFileHeader.getCompressionMethod());
    localFileHeader.setLastModifiedTime(paramFileHeader.getLastModifiedTime());
    localFileHeader.setUncompressedSize(paramFileHeader.getUncompressedSize());
    localFileHeader.setFileNameLength(paramFileHeader.getFileNameLength());
    localFileHeader.setFileName(paramFileHeader.getFileName());
    localFileHeader.setEncrypted(paramFileHeader.isEncrypted());
    localFileHeader.setEncryptionMethod(paramFileHeader.getEncryptionMethod());
    localFileHeader.setAesExtraDataRecord(paramFileHeader.getAesExtraDataRecord());
    localFileHeader.setCrc(paramFileHeader.getCrc());
    localFileHeader.setCompressedSize(paramFileHeader.getCompressedSize());
    localFileHeader.setGeneralPurposeFlag((byte[])paramFileHeader.getGeneralPurposeFlag().clone());
    localFileHeader.setDataDescriptorExists(paramFileHeader.isDataDescriptorExists());
    localFileHeader.setExtraFieldLength(paramFileHeader.getExtraFieldLength());
    return localFileHeader;
  }
}
