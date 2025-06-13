package net.lingala.zip4j.crypto.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import net.lingala.zip4j.exception.ZipException;

public class AESEngine {
  private static final byte[] S = new byte[] { 
      99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 
      103, 43, -2, -41, -85, 118, -54, -126, -55, 125, 
      -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 
      114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 
      52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 
      35, -61, 24, -106, 5, -102, 7, 18, Byte.MIN_VALUE, -30, 
      -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 
      90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 
      83, -47, 0, -19, 32, -4, -79, 91, 106, -53, 
      -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 
      67, 77, 51, -123, 69, -7, 2, Byte.MAX_VALUE, 80, 60, 
      -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, 
      -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 
      19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 
      100, 93, 25, 115, 96, -127, 79, -36, 34, 42, 
      -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, 
      -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, 
      -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, 
      -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, 
      -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, 
      -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, 
      -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, 
      -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, 
      -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, 
      -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 
      45, 15, -80, 84, -69, 22 };
  
  private static final int[] T0;
  
  private static final int[] rcon = new int[] { 
      1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 
      108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 
      151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
  
  private int C0;
  
  private int C1;
  
  private int C2;
  
  private int C3;
  
  private int rounds;
  
  private int[][] workingKey = null;
  
  static {
    T0 = $d2j$hex$011f8405$decode_I("c66363a5f87c7c84ee777799f67b7b8dfff2f20dd66b6bbdde6f6fb191c5c5546030305002010103ce6767a9562b2b7de7fefe19b5d7d7624dababe6ec76769a8fcaca451f82829d89c9c940fa7d7d87effafa15b25959eb8e4747c9fbf0f00b41adadecb3d4d4675fa2a2fd45afafea239c9cbf53a4a4f7e47272969bc0c05b75b7b7c2e1fdfd1c3d9393ae4c26266a6c36365a7e3f3f41f5f7f70283cccc4f6834345c51a5a5f4d1e5e534f9f1f108e2717193abd8d873623131532a15153f0804040c95c7c752462323659dc3c35e30181828379696a10a05050f2f9a9ab50e070709241212361b80809bdfe2e23dcdebeb264e2727697fb2b2cdea75759f1209091b1d83839e582c2c74341a1a2e361b1b2ddc6e6eb2b45a5aee5ba0a0fba45252f6763b3b4db7d6d6617db3b3ce5229297bdde3e33e5e2f2f7113848497a65353f5b9d1d16800000000c1eded2c40202060e3fcfc1f79b1b1c8b65b5bedd46a6abe8dcbcb4667bebed97239394b944a4ade984c4cd4b05858e885cfcf4abbd0d06bc5efef2a4faaaae5edfbfb16864343c59a4d4dd766333355118585948a4545cfe9f9f91004020206fe7f7f81a05050f0783c3c44259f9fba4ba8a8e3a25151f35da3a3fe804040c0058f8f8a3f9292ad219d9dbc70383848f1f5f50463bcbcdf77b6b6c1afdada754221216320101030e5ffff1afdf3f30ebfd2d26d81cdcd4c180c0c1426131335c3ecec2fbe5f5fe1359797a2884444cc2e17173993c4c45755a7a7f2fc7e7e827a3d3d47c86464acba5d5de73219192be6737395c06060a0198181989e4f4fd1a3dcdc7f44222266542a2a7e3b9090ab0b8888838c4646cac7eeee296bb8b8d32814143ca7dede79bc5e5ee2160b0b1daddbdb76dbe0e03b64323256743a3a4e140a0a1e924949db0c06060a4824246cb85c5ce49fc2c25dbdd3d36e43acacefc46262a6399191a8319595a4d3e4e437f279798bd5e7e7328bc8c8436e373759da6d6db7018d8d8cb1d5d5649c4e4ed249a9a9e0d86c6cb4ac5656faf3f4f407cfeaea25ca6565aff47a7a8e47aeaee9100808186fbabad5f07878884a25256f5c2e2e72381c1c2457a6a6f173b4b4c797c6c651cbe8e823a1dddd7ce874749c3e1f1f21964b4bdd61bdbddc0d8b8b860f8a8a85e07070907c3e3e4271b5b5c4cc6666aa904848d806030305f7f6f6011c0e0e12c26161a36a35355fae5757f969b9b9d01786869199c1c1583a1d1d27279e9eb9d9e1e138ebf8f8132b9898b322111133d26969bba9d9d970078e8e89339494a72d9b9bb63c1e1e2215878792c9e9e92087cece49aa5555ff50282878a5dfdf7a038c8c8f59a1a1f8098989801a0d0d1765bfbfdad7e6e631844242c6d06868b8824141c3299999b05a2d2d771e0f0f117bb0b0cba85454fc6dbbbbd62c16163a");
  }
  
  public AESEngine(byte[] paramArrayOfbyte) throws ZipException {
    init(paramArrayOfbyte);
  }
  
  private void encryptBlock(int[][] paramArrayOfint) {
    this.C0 ^= paramArrayOfint[0][0];
    this.C1 ^= paramArrayOfint[0][1];
    this.C2 ^= paramArrayOfint[0][2];
    this.C3 ^= paramArrayOfint[0][3];
    int i;
    for (i = 1; i < this.rounds - 1; i = i5 + 1) {
      int[] arrayOfInt1 = T0;
      int i6 = arrayOfInt1[this.C0 & 0xFF] ^ shift(arrayOfInt1[this.C1 >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[this.C2 >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[this.C3 >> 24 & 0xFF], 8) ^ paramArrayOfint[i][0];
      int i8 = arrayOfInt1[this.C1 & 0xFF] ^ shift(arrayOfInt1[this.C2 >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[this.C3 >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[this.C0 >> 24 & 0xFF], 8) ^ paramArrayOfint[i][1];
      int i7 = arrayOfInt1[this.C2 & 0xFF] ^ shift(arrayOfInt1[this.C3 >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[this.C0 >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[this.C1 >> 24 & 0xFF], 8) ^ paramArrayOfint[i][2];
      int i12 = arrayOfInt1[this.C3 & 0xFF];
      int i10 = shift(arrayOfInt1[this.C0 >> 8 & 0xFF], 24);
      int i9 = shift(arrayOfInt1[this.C1 >> 16 & 0xFF], 16);
      int i11 = shift(arrayOfInt1[this.C2 >> 24 & 0xFF], 8);
      int i5 = i + 1;
      i = paramArrayOfint[i][3] ^ i12 ^ i10 ^ i9 ^ i11;
      this.C0 = arrayOfInt1[i6 & 0xFF] ^ shift(arrayOfInt1[i8 >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[i7 >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[i >> 24 & 0xFF], 8) ^ paramArrayOfint[i5][0];
      this.C1 = arrayOfInt1[i8 & 0xFF] ^ shift(arrayOfInt1[i7 >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[i >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[i6 >> 24 & 0xFF], 8) ^ paramArrayOfint[i5][1];
      this.C2 = arrayOfInt1[i7 & 0xFF] ^ shift(arrayOfInt1[i >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[i6 >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[i8 >> 24 & 0xFF], 8) ^ paramArrayOfint[i5][2];
      this.C3 = arrayOfInt1[i & 0xFF] ^ shift(arrayOfInt1[i6 >> 8 & 0xFF], 24) ^ shift(arrayOfInt1[i8 >> 16 & 0xFF], 16) ^ shift(arrayOfInt1[i7 >> 24 & 0xFF], 8) ^ paramArrayOfint[i5][3];
    } 
    int[] arrayOfInt = T0;
    int n = arrayOfInt[this.C0 & 0xFF] ^ shift(arrayOfInt[this.C1 >> 8 & 0xFF], 24) ^ shift(arrayOfInt[this.C2 >> 16 & 0xFF], 16) ^ shift(arrayOfInt[this.C3 >> 24 & 0xFF], 8) ^ paramArrayOfint[i][0];
    int k = arrayOfInt[this.C1 & 0xFF] ^ shift(arrayOfInt[this.C2 >> 8 & 0xFF], 24) ^ shift(arrayOfInt[this.C3 >> 16 & 0xFF], 16) ^ shift(arrayOfInt[this.C0 >> 24 & 0xFF], 8) ^ paramArrayOfint[i][1];
    int j = arrayOfInt[this.C2 & 0xFF] ^ shift(arrayOfInt[this.C3 >> 8 & 0xFF], 24) ^ shift(arrayOfInt[this.C0 >> 16 & 0xFF], 16) ^ shift(arrayOfInt[this.C1 >> 24 & 0xFF], 8) ^ paramArrayOfint[i][2];
    int i4 = arrayOfInt[this.C3 & 0xFF];
    int i1 = shift(arrayOfInt[this.C0 >> 8 & 0xFF], 24);
    int i2 = shift(arrayOfInt[this.C1 >> 16 & 0xFF], 16);
    int i3 = shift(arrayOfInt[this.C2 >> 24 & 0xFF], 8);
    int m = i + 1;
    i = paramArrayOfint[i][3] ^ i3 ^ i4 ^ i1 ^ i2;
    byte[] arrayOfByte = S;
    i3 = arrayOfByte[n & 0xFF];
    i1 = arrayOfByte[k >> 8 & 0xFF];
    i2 = arrayOfByte[j >> 16 & 0xFF];
    i4 = arrayOfByte[i >> 24 & 0xFF];
    this.C0 = paramArrayOfint[m][0] ^ i3 & 0xFF ^ (i1 & 0xFF) << 8 ^ (i2 & 0xFF) << 16 ^ i4 << 24;
    this.C1 = arrayOfByte[k & 0xFF] & 0xFF ^ (arrayOfByte[j >> 8 & 0xFF] & 0xFF) << 8 ^ (arrayOfByte[i >> 16 & 0xFF] & 0xFF) << 16 ^ arrayOfByte[n >> 24 & 0xFF] << 24 ^ paramArrayOfint[m][1];
    this.C2 = arrayOfByte[j & 0xFF] & 0xFF ^ (arrayOfByte[i >> 8 & 0xFF] & 0xFF) << 8 ^ (arrayOfByte[n >> 16 & 0xFF] & 0xFF) << 16 ^ arrayOfByte[k >> 24 & 0xFF] << 24 ^ paramArrayOfint[m][2];
    this.C3 = arrayOfByte[i & 0xFF] & 0xFF ^ (arrayOfByte[n >> 8 & 0xFF] & 0xFF) << 8 ^ (arrayOfByte[k >> 16 & 0xFF] & 0xFF) << 16 ^ arrayOfByte[j >> 24 & 0xFF] << 24 ^ paramArrayOfint[m][3];
  }
  
  private int[][] generateWorkingKey(byte[] paramArrayOfbyte) throws ZipException {
    int i = paramArrayOfbyte.length / 4;
    if ((i == 4 || i == 6 || i == 8) && i * 4 == paramArrayOfbyte.length) {
      int k = i + 6;
      this.rounds = k;
      int j = 0;
      int[][] arrayOfInt = new int[k + 1][4];
      for (k = 0; j < paramArrayOfbyte.length; k++) {
        arrayOfInt[k >> 2][k & 0x3] = paramArrayOfbyte[j] & 0xFF | (paramArrayOfbyte[j + 1] & 0xFF) << 8 | (paramArrayOfbyte[j + 2] & 0xFF) << 16 | paramArrayOfbyte[j + 3] << 24;
        j += 4;
      } 
      int m = this.rounds;
      for (k = i; k < m + 1 << 2; k++) {
        j = k - 1;
        int n = arrayOfInt[j >> 2][j & 0x3];
        int i1 = k % i;
        if (i1 == 0) {
          j = subWord(shift(n, 8)) ^ rcon[k / i - 1];
        } else {
          j = n;
          if (i > 6) {
            j = n;
            if (i1 == 4)
              j = subWord(n); 
          } 
        } 
        int[] arrayOfInt1 = arrayOfInt[k >> 2];
        n = k - i;
        arrayOfInt1[k & 0x3] = j ^ arrayOfInt[n >> 2][n & 0x3];
      } 
      return arrayOfInt;
    } 
    throw new ZipException("invalid key length (not 128/192/256)");
  }
  
  private void init(byte[] paramArrayOfbyte) throws ZipException {
    this.workingKey = generateWorkingKey(paramArrayOfbyte);
  }
  
  private int shift(int paramInt1, int paramInt2) {
    return paramInt1 << -paramInt2 | paramInt1 >>> paramInt2;
  }
  
  private void stateIn(byte[] paramArrayOfbyte, int paramInt) {
    int i = paramInt + 1;
    int j = paramArrayOfbyte[paramInt] & 0xFF;
    this.C0 = j;
    paramInt = i + 1;
    j |= (paramArrayOfbyte[i] & 0xFF) << 8;
    this.C0 = j;
    i = paramInt + 1;
    paramInt = j | (paramArrayOfbyte[paramInt] & 0xFF) << 16;
    this.C0 = paramInt;
    j = i + 1;
    this.C0 = paramInt | paramArrayOfbyte[i] << 24;
    paramInt = j + 1;
    j = paramArrayOfbyte[j] & 0xFF;
    this.C1 = j;
    i = paramInt + 1;
    j = (paramArrayOfbyte[paramInt] & 0xFF) << 8 | j;
    this.C1 = j;
    paramInt = i + 1;
    i = j | (paramArrayOfbyte[i] & 0xFF) << 16;
    this.C1 = i;
    j = paramInt + 1;
    this.C1 = i | paramArrayOfbyte[paramInt] << 24;
    i = j + 1;
    j = paramArrayOfbyte[j] & 0xFF;
    this.C2 = j;
    paramInt = i + 1;
    j = (paramArrayOfbyte[i] & 0xFF) << 8 | j;
    this.C2 = j;
    i = paramInt + 1;
    j |= (paramArrayOfbyte[paramInt] & 0xFF) << 16;
    this.C2 = j;
    paramInt = i + 1;
    this.C2 = j | paramArrayOfbyte[i] << 24;
    i = paramInt + 1;
    j = paramArrayOfbyte[paramInt] & 0xFF;
    this.C3 = j;
    paramInt = i + 1;
    i = (paramArrayOfbyte[i] & 0xFF) << 8 | j;
    this.C3 = i;
    i |= (paramArrayOfbyte[paramInt] & 0xFF) << 16;
    this.C3 = i;
    this.C3 = paramArrayOfbyte[paramInt + 1] << 24 | i;
  }
  
  private void stateOut(byte[] paramArrayOfbyte, int paramInt) {
    int j = paramInt + 1;
    int i = this.C0;
    paramArrayOfbyte[paramInt] = (byte)i;
    int k = j + 1;
    paramArrayOfbyte[j] = (byte)(i >> 8);
    paramInt = k + 1;
    paramArrayOfbyte[k] = (byte)(i >> 16);
    j = paramInt + 1;
    paramArrayOfbyte[paramInt] = (byte)(i >> 24);
    k = j + 1;
    paramInt = this.C1;
    paramArrayOfbyte[j] = (byte)paramInt;
    i = k + 1;
    paramArrayOfbyte[k] = (byte)(paramInt >> 8);
    j = i + 1;
    paramArrayOfbyte[i] = (byte)(paramInt >> 16);
    i = j + 1;
    paramArrayOfbyte[j] = (byte)(paramInt >> 24);
    j = i + 1;
    paramInt = this.C2;
    paramArrayOfbyte[i] = (byte)paramInt;
    i = j + 1;
    paramArrayOfbyte[j] = (byte)(paramInt >> 8);
    j = i + 1;
    paramArrayOfbyte[i] = (byte)(paramInt >> 16);
    i = j + 1;
    paramArrayOfbyte[j] = (byte)(paramInt >> 24);
    j = i + 1;
    paramInt = this.C3;
    paramArrayOfbyte[i] = (byte)paramInt;
    i = j + 1;
    paramArrayOfbyte[j] = (byte)(paramInt >> 8);
    paramArrayOfbyte[i] = (byte)(paramInt >> 16);
    paramArrayOfbyte[i + 1] = (byte)(paramInt >> 24);
  }
  
  private int subWord(int paramInt) {
    byte[] arrayOfByte = S;
    byte b3 = arrayOfByte[paramInt & 0xFF];
    byte b1 = arrayOfByte[paramInt >> 8 & 0xFF];
    byte b2 = arrayOfByte[paramInt >> 16 & 0xFF];
    return arrayOfByte[paramInt >> 24 & 0xFF] << 24 | b3 & 0xFF | (b1 & 0xFF) << 8 | (b2 & 0xFF) << 16;
  }
  
  public int processBlock(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2) throws ZipException {
    if (this.workingKey != null) {
      if (paramInt1 + 16 <= paramArrayOfbyte1.length) {
        if (paramInt2 + 16 <= paramArrayOfbyte2.length) {
          stateIn(paramArrayOfbyte1, paramInt1);
          encryptBlock(this.workingKey);
          stateOut(paramArrayOfbyte2, paramInt2);
          return 16;
        } 
        throw new ZipException("output buffer too short");
      } 
      throw new ZipException("input buffer too short");
    } 
    throw new ZipException("AES engine not initialised");
  }
  
  public int processBlock(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws ZipException {
    return processBlock(paramArrayOfbyte1, 0, paramArrayOfbyte2, 0);
  }
  
  private static long[] $d2j$hex$011f8405$decode_J(String src) {
    byte[] d = $d2j$hex$011f8405$decode_B(src);
    ByteBuffer b = ByteBuffer.wrap(d);
    b.order(ByteOrder.LITTLE_ENDIAN);
    LongBuffer s = b.asLongBuffer();
    long[] data = new long[d.length / 8];
    s.get(data);
    return data;
  }
  
  private static int[] $d2j$hex$011f8405$decode_I(String src) {
    byte[] d = $d2j$hex$011f8405$decode_B(src);
    ByteBuffer b = ByteBuffer.wrap(d);
    b.order(ByteOrder.LITTLE_ENDIAN);
    IntBuffer s = b.asIntBuffer();
    int[] data = new int[d.length / 4];
    s.get(data);
    return data;
  }
  
  private static short[] $d2j$hex$011f8405$decode_S(String src) {
    byte[] d = $d2j$hex$011f8405$decode_B(src);
    ByteBuffer b = ByteBuffer.wrap(d);
    b.order(ByteOrder.LITTLE_ENDIAN);
    ShortBuffer s = b.asShortBuffer();
    short[] data = new short[d.length / 2];
    s.get(data);
    return data;
  }
  
  private static byte[] $d2j$hex$011f8405$decode_B(String src) {
    char[] d = src.toCharArray();
    byte[] ret = new byte[src.length() / 2];
    for (int i = 0; i < ret.length; i++) {
      int hh, ll;
      char h = d[2 * i];
      char l = d[2 * i + 1];
      if (h >= '0' && h <= '9') {
        hh = h - 48;
      } else if (h >= 'a' && h <= 'f') {
        hh = h - 97 + 10;
      } else if (h >= 'A' && h <= 'F') {
        hh = h - 65 + 10;
      } else {
        throw new RuntimeException();
      } 
      if (l >= '0' && l <= '9') {
        ll = l - 48;
      } else if (l >= 'a' && l <= 'f') {
        ll = l - 97 + 10;
      } else if (l >= 'A' && l <= 'F') {
        ll = l - 65 + 10;
      } else {
        throw new RuntimeException();
      } 
      ret[i] = (byte)(hh << 4 | ll);
    } 
    return ret;
  }
}
