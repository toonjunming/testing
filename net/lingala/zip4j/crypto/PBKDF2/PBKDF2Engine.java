package net.lingala.zip4j.crypto.PBKDF2;

import java.util.Objects;
import net.lingala.zip4j.util.Zip4jUtil;

public class PBKDF2Engine {
  private PBKDF2Parameters parameters;
  
  private PRF prf;
  
  public PBKDF2Engine(PBKDF2Parameters paramPBKDF2Parameters) {
    this(paramPBKDF2Parameters, null);
  }
  
  public PBKDF2Engine(PBKDF2Parameters paramPBKDF2Parameters, PRF paramPRF) {
    this.parameters = paramPBKDF2Parameters;
    this.prf = paramPRF;
  }
  
  private byte[] PBKDF2(PRF paramPRF, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte == null)
      paramArrayOfbyte = new byte[0]; 
    int j = paramPRF.getHLen();
    int k = ceil(paramInt2, j);
    byte[] arrayOfByte = new byte[k * j];
    byte b = 1;
    int i = 0;
    while (b <= k) {
      _F(arrayOfByte, i, paramPRF, paramArrayOfbyte, paramInt1, b);
      i += j;
      b++;
    } 
    if (paramInt2 - (k - 1) * j < j) {
      byte[] arrayOfByte1 = new byte[paramInt2];
      System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, paramInt2);
      return arrayOfByte1;
    } 
    return arrayOfByte;
  }
  
  private void _F(byte[] paramArrayOfbyte1, int paramInt1, PRF paramPRF, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
    int i = paramPRF.getHLen();
    byte[] arrayOfByte2 = new byte[i];
    byte[] arrayOfByte1 = new byte[paramArrayOfbyte2.length + 4];
    System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte1, 0, paramArrayOfbyte2.length);
    INT(arrayOfByte1, paramArrayOfbyte2.length, paramInt3);
    paramInt3 = 0;
    paramArrayOfbyte2 = arrayOfByte1;
    while (paramInt3 < paramInt2) {
      paramArrayOfbyte2 = paramPRF.doFinal(paramArrayOfbyte2);
      xor(arrayOfByte2, paramArrayOfbyte2);
      paramInt3++;
    } 
    System.arraycopy(arrayOfByte2, 0, paramArrayOfbyte1, paramInt1, i);
  }
  
  private void assertPRF(byte[] paramArrayOfbyte) {
    if (this.prf == null)
      this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm()); 
    this.prf.init(paramArrayOfbyte);
  }
  
  private int ceil(int paramInt1, int paramInt2) {
    byte b;
    if (paramInt1 % paramInt2 > 0) {
      b = 1;
    } else {
      b = 0;
    } 
    return paramInt1 / paramInt2 + b;
  }
  
  private void xor(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    for (byte b = 0; b < paramArrayOfbyte1.length; b++)
      paramArrayOfbyte1[b] = (byte)(paramArrayOfbyte1[b] ^ paramArrayOfbyte2[b]); 
  }
  
  public void INT(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    paramArrayOfbyte[paramInt1] = (byte)(paramInt2 / 16777216);
    paramArrayOfbyte[paramInt1 + 1] = (byte)(paramInt2 / 65536);
    paramArrayOfbyte[paramInt1 + 2] = (byte)(paramInt2 / 256);
    paramArrayOfbyte[paramInt1 + 3] = (byte)paramInt2;
  }
  
  public byte[] deriveKey(char[] paramArrayOfchar) {
    return deriveKey(paramArrayOfchar, 0);
  }
  
  public byte[] deriveKey(char[] paramArrayOfchar, int paramInt) {
    Objects.requireNonNull(paramArrayOfchar);
    assertPRF(Zip4jUtil.convertCharArrayToByteArray(paramArrayOfchar));
    int i = paramInt;
    if (paramInt == 0)
      i = this.prf.getHLen(); 
    return PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), i);
  }
  
  public PBKDF2Parameters getParameters() {
    return this.parameters;
  }
  
  public PRF getPseudoRandomFunction() {
    return this.prf;
  }
  
  public void setParameters(PBKDF2Parameters paramPBKDF2Parameters) {
    this.parameters = paramPBKDF2Parameters;
  }
  
  public void setPseudoRandomFunction(PRF paramPRF) {
    this.prf = paramPRF;
  }
  
  public boolean verifyKey(char[] paramArrayOfchar) {
    byte[] arrayOfByte = getParameters().getDerivedKey();
    if (arrayOfByte != null && arrayOfByte.length != 0) {
      byte[] arrayOfByte1 = deriveKey(paramArrayOfchar, arrayOfByte.length);
      if (arrayOfByte1 != null && arrayOfByte1.length == arrayOfByte.length) {
        for (byte b = 0; b < arrayOfByte1.length; b++) {
          if (arrayOfByte1[b] != arrayOfByte[b])
            return false; 
        } 
        return true;
      } 
    } 
    return false;
  }
}
