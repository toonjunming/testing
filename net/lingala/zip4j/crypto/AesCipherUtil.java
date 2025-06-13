package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.enums.AesKeyStrength;

public class AesCipherUtil {
  private static final int START_INDEX = 0;
  
  public static byte[] derivePasswordBasedKey(byte[] paramArrayOfbyte, char[] paramArrayOfchar, AesKeyStrength paramAesKeyStrength) throws ZipException {
    PBKDF2Engine pBKDF2Engine = new PBKDF2Engine(new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", paramArrayOfbyte, 1000));
    int i = paramAesKeyStrength.getKeyLength();
    int k = paramAesKeyStrength.getMacLength();
    int j = i + k + 2;
    byte[] arrayOfByte = pBKDF2Engine.deriveKey(paramArrayOfchar, j);
    if (arrayOfByte != null && arrayOfByte.length == j)
      return arrayOfByte; 
    throw new ZipException(String.format("Derived Key invalid for Key Length [%d] MAC Length [%d]", new Object[] { Integer.valueOf(i), Integer.valueOf(k) }));
  }
  
  public static byte[] derivePasswordVerifier(byte[] paramArrayOfbyte, AesKeyStrength paramAesKeyStrength) {
    byte[] arrayOfByte = new byte[2];
    System.arraycopy(paramArrayOfbyte, paramAesKeyStrength.getKeyLength() + paramAesKeyStrength.getMacLength(), arrayOfByte, 0, 2);
    return arrayOfByte;
  }
  
  public static AESEngine getAESEngine(byte[] paramArrayOfbyte, AesKeyStrength paramAesKeyStrength) throws ZipException {
    int i = paramAesKeyStrength.getKeyLength();
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, i);
    return new AESEngine(arrayOfByte);
  }
  
  public static MacBasedPRF getMacBasedPRF(byte[] paramArrayOfbyte, AesKeyStrength paramAesKeyStrength) {
    int i = paramAesKeyStrength.getMacLength();
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfbyte, paramAesKeyStrength.getKeyLength(), arrayOfByte, 0, i);
    MacBasedPRF macBasedPRF = new MacBasedPRF("HmacSHA1");
    macBasedPRF.init(arrayOfByte);
    return macBasedPRF;
  }
  
  public static void prepareBuffAESIVBytes(byte[] paramArrayOfbyte, int paramInt) {
    paramArrayOfbyte[0] = (byte)paramInt;
    paramArrayOfbyte[1] = (byte)(paramInt >> 8);
    paramArrayOfbyte[2] = (byte)(paramInt >> 16);
    paramArrayOfbyte[3] = (byte)(paramInt >> 24);
    for (paramInt = 4; paramInt <= 15; paramInt++)
      paramArrayOfbyte[paramInt] = 0; 
  }
}
