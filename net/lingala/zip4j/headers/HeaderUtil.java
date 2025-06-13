package net.lingala.zip4j.headers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderUtil {
  public static String decodeStringWithCharset(byte[] paramArrayOfbyte, boolean paramBoolean, Charset paramCharset) {
    if (paramCharset != null)
      return new String(paramArrayOfbyte, paramCharset); 
    if (paramBoolean)
      return new String(paramArrayOfbyte, InternalZipConstants.CHARSET_UTF_8); 
    try {
      return new String(paramArrayOfbyte, "Cp437");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return new String(paramArrayOfbyte);
    } 
  }
  
  public static byte[] getBytesFromString(String paramString, Charset paramCharset) {
    return (paramCharset == null) ? paramString.getBytes(InternalZipConstants.ZIP4J_DEFAULT_CHARSET) : paramString.getBytes(paramCharset);
  }
  
  public static FileHeader getFileHeader(ZipModel paramZipModel, String paramString) throws ZipException {
    FileHeader fileHeader2 = getFileHeaderWithExactMatch(paramZipModel, paramString);
    FileHeader fileHeader1 = fileHeader2;
    if (fileHeader2 == null) {
      paramString = paramString.replaceAll("\\\\", "/");
      fileHeader1 = getFileHeaderWithExactMatch(paramZipModel, paramString);
      if (fileHeader1 == null)
        fileHeader1 = getFileHeaderWithExactMatch(paramZipModel, paramString.replaceAll("/", "\\\\")); 
    } 
    return fileHeader1;
  }
  
  private static FileHeader getFileHeaderWithExactMatch(ZipModel paramZipModel, String paramString) throws ZipException {
    if (paramZipModel != null) {
      if (Zip4jUtil.isStringNotNullAndNotEmpty(paramString)) {
        if (paramZipModel.getCentralDirectory() != null) {
          if (paramZipModel.getCentralDirectory().getFileHeaders() != null) {
            if (paramZipModel.getCentralDirectory().getFileHeaders().size() == 0)
              return null; 
            for (FileHeader fileHeader : paramZipModel.getCentralDirectory().getFileHeaders()) {
              String str = fileHeader.getFileName();
              if (Zip4jUtil.isStringNotNullAndNotEmpty(str) && paramString.equalsIgnoreCase(str))
                return fileHeader; 
            } 
            return null;
          } 
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append("file Headers are null, cannot determine file header with exact match for fileName: ");
          stringBuilder3.append(paramString);
          throw new ZipException(stringBuilder3.toString());
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("central directory is null, cannot determine file header with exact match for fileName: ");
        stringBuilder2.append(paramString);
        throw new ZipException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("file name is null, cannot determine file header with exact match for fileName: ");
      stringBuilder1.append(paramString);
      throw new ZipException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("zip model is null, cannot determine file header with exact match for fileName: ");
    stringBuilder.append(paramString);
    throw new ZipException(stringBuilder.toString());
  }
  
  public static List<FileHeader> getFileHeadersUnderDirectory(List<FileHeader> paramList, FileHeader paramFileHeader) {
    if (!paramFileHeader.isDirectory())
      return Collections.emptyList(); 
    ArrayList<FileHeader> arrayList = new ArrayList();
    for (FileHeader fileHeader : paramList) {
      if (fileHeader.getFileName().startsWith(paramFileHeader.getFileName()))
        arrayList.add(fileHeader); 
    } 
    return arrayList;
  }
  
  public static long getOffsetStartOfCentralDirectory(ZipModel paramZipModel) {
    return paramZipModel.isZip64Format() ? paramZipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() : paramZipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
  }
  
  public static long getTotalUncompressedSizeOfAllFileHeaders(List<FileHeader> paramList) {
    Iterator<FileHeader> iterator = paramList.iterator();
    long l;
    for (l = 0L; iterator.hasNext(); l += l1) {
      long l1;
      FileHeader fileHeader = iterator.next();
      if (fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getUncompressedSize() > 0L) {
        l1 = fileHeader.getZip64ExtendedInfo().getUncompressedSize();
      } else {
        l1 = fileHeader.getUncompressedSize();
      } 
    } 
    return l;
  }
}
