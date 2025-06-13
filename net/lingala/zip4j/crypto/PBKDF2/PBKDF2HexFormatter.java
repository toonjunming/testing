package net.lingala.zip4j.crypto.PBKDF2;

public class PBKDF2HexFormatter {
  public boolean fromString(PBKDF2Parameters paramPBKDF2Parameters, String paramString) {
    if (paramPBKDF2Parameters == null || paramString == null)
      return true; 
    String[] arrayOfString = paramString.split(":");
    if (arrayOfString.length != 3)
      return true; 
    byte[] arrayOfByte1 = BinTools.hex2bin(arrayOfString[0]);
    int i = Integer.parseInt(arrayOfString[1]);
    byte[] arrayOfByte2 = BinTools.hex2bin(arrayOfString[2]);
    paramPBKDF2Parameters.setSalt(arrayOfByte1);
    paramPBKDF2Parameters.setIterationCount(i);
    paramPBKDF2Parameters.setDerivedKey(arrayOfByte2);
    return false;
  }
  
  public String toString(PBKDF2Parameters paramPBKDF2Parameters) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(BinTools.bin2hex(paramPBKDF2Parameters.getSalt()));
    stringBuilder.append(":");
    stringBuilder.append(String.valueOf(paramPBKDF2Parameters.getIterationCount()));
    stringBuilder.append(":");
    stringBuilder.append(BinTools.bin2hex(paramPBKDF2Parameters.getDerivedKey()));
    return stringBuilder.toString();
  }
}
