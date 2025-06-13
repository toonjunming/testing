package com.mysql.jdbc;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.SQLException;

public class Buffer {
  public static final int MAX_BYTES_TO_DUMP = 512;
  
  public static final int NO_LENGTH_LIMIT = -1;
  
  public static final long NULL_LENGTH = -1L;
  
  public static final short TYPE_ID_AUTH_SWITCH = 254;
  
  public static final short TYPE_ID_EOF = 254;
  
  public static final short TYPE_ID_ERROR = 255;
  
  public static final short TYPE_ID_LOCAL_INFILE = 251;
  
  public static final short TYPE_ID_OK = 0;
  
  private int bufLength = 0;
  
  private byte[] byteBuffer;
  
  private int position = 0;
  
  public boolean wasMultiPacket = false;
  
  public Buffer(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    this.byteBuffer = arrayOfByte;
    setBufLength(arrayOfByte.length);
    this.position = 4;
  }
  
  public Buffer(byte[] paramArrayOfbyte) {
    this.byteBuffer = paramArrayOfbyte;
    setBufLength(paramArrayOfbyte.length);
  }
  
  public final void clear() {
    this.position = 4;
  }
  
  public final String dump(int paramInt) {
    if (paramInt > getBufLength()) {
      i = getBufLength();
    } else {
      i = paramInt;
    } 
    byte[] arrayOfByte = getBytes(0, i);
    int i = paramInt;
    if (paramInt > getBufLength())
      i = getBufLength(); 
    return StringUtils.dumpAsHex(arrayOfByte, i);
  }
  
  public final void dump() {
    dump(getBufLength());
  }
  
  public final String dumpClampedBytes(int paramInt) {
    int j;
    int i = 512;
    if (paramInt < 512)
      i = paramInt; 
    if (i > getBufLength()) {
      j = getBufLength();
    } else {
      j = i;
    } 
    byte[] arrayOfByte = getBytes(0, j);
    if (i > getBufLength()) {
      j = getBufLength();
    } else {
      j = i;
    } 
    String str = StringUtils.dumpAsHex(arrayOfByte, j);
    if (i < paramInt) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(" ....(packet exceeds max. dump length)");
      return stringBuilder.toString();
    } 
    return str;
  }
  
  public final void dumpHeader() {
    for (byte b = 0; b < 4; b++) {
      String str2 = Integer.toHexString(readByte(b) & 0xFF);
      String str1 = str2;
      if (str2.length() == 1) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("0");
        stringBuilder1.append(str2);
        str1 = stringBuilder1.toString();
      } 
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(" ");
      printStream.print(stringBuilder.toString());
    } 
  }
  
  public final void dumpNBytes(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder2 = new StringBuilder();
    for (int i = paramInt1; i < paramInt1 + paramInt2 && i < getBufLength(); i++) {
      String str2 = Integer.toHexString(readByte(i) & 0xFF);
      String str1 = str2;
      if (str2.length() == 1) {
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("0");
        stringBuilder3.append(str2);
        str1 = stringBuilder3.toString();
      } 
      PrintStream printStream1 = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(" ");
      printStream1.print(stringBuilder.toString());
      if (readByte(i) > 32 && readByte(i) < Byte.MAX_VALUE) {
        stringBuilder2.append((char)readByte(i));
      } else {
        stringBuilder2.append(".");
      } 
      stringBuilder2.append(" ");
    } 
    PrintStream printStream = System.out;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("    ");
    stringBuilder1.append(stringBuilder2.toString());
    printStream.println(stringBuilder1.toString());
  }
  
  public final void ensureCapacity(int paramInt) throws SQLException {
    if (this.position + paramInt > getBufLength()) {
      int i = this.position;
      byte[] arrayOfByte = this.byteBuffer;
      if (i + paramInt < arrayOfByte.length) {
        setBufLength(arrayOfByte.length);
      } else {
        int j = (int)(arrayOfByte.length * 1.25D);
        i = j;
        if (j < arrayOfByte.length + paramInt)
          i = arrayOfByte.length + (int)(paramInt * 1.25D); 
        j = i;
        if (i < arrayOfByte.length)
          j = arrayOfByte.length + paramInt; 
        byte[] arrayOfByte1 = new byte[j];
        System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, arrayOfByte.length);
        this.byteBuffer = arrayOfByte1;
        setBufLength(arrayOfByte1.length);
      } 
    } 
  }
  
  public void fastSkipLenByteArray() {
    long l = readFieldLength();
    if (l != -1L && l != 0L)
      this.position = (int)(this.position + l); 
  }
  
  public int fastSkipLenString() {
    long l = readFieldLength();
    this.position = (int)(this.position + l);
    return (int)l;
  }
  
  public int getBufLength() {
    return this.bufLength;
  }
  
  public final byte[] getBufferSource() {
    return this.byteBuffer;
  }
  
  public byte[] getByteBuffer() {
    return this.byteBuffer;
  }
  
  public final byte[] getBytes(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(this.byteBuffer, this.position, arrayOfByte, 0, paramInt);
    this.position += paramInt;
    return arrayOfByte;
  }
  
  public byte[] getBytes(int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(this.byteBuffer, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public int getCapacity() {
    return this.byteBuffer.length;
  }
  
  public ByteBuffer getNioBuffer() {
    throw new IllegalArgumentException(Messages.getString("ByteArrayBuffer.0"));
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public final boolean isAuthMethodSwitchRequestPacket() {
    byte[] arrayOfByte = this.byteBuffer;
    boolean bool = false;
    if ((arrayOfByte[0] & 0xFF) == 254)
      bool = true; 
    return bool;
  }
  
  public final boolean isEOFPacket() {
    byte[] arrayOfByte = this.byteBuffer;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if ((arrayOfByte[0] & 0xFF) == 254) {
      bool1 = bool2;
      if (getBufLength() <= 5)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public final boolean isOKPacket() {
    byte[] arrayOfByte = this.byteBuffer;
    boolean bool = false;
    if ((arrayOfByte[0] & 0xFF) == 0)
      bool = true; 
    return bool;
  }
  
  public final boolean isRawPacket() {
    byte[] arrayOfByte = this.byteBuffer;
    boolean bool = false;
    if ((arrayOfByte[0] & 0xFF) == 1)
      bool = true; 
    return bool;
  }
  
  public final boolean isResultSetOKPacket() {
    byte[] arrayOfByte = this.byteBuffer;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if ((arrayOfByte[0] & 0xFF) == 254) {
      bool1 = bool2;
      if (getBufLength() < 16777215)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public final long newReadLength() {
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    this.position = i + 1;
    i = arrayOfByte[i] & 0xFF;
    switch (i) {
      default:
        return i;
      case 254:
        return readLongLong();
      case 253:
        i = readLongInt();
        return i;
      case 252:
        i = readInt();
        return i;
      case 251:
        break;
    } 
    return 0L;
  }
  
  public final byte readByte() {
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    this.position = i + 1;
    return arrayOfByte[i];
  }
  
  public final byte readByte(int paramInt) {
    return this.byteBuffer[paramInt];
  }
  
  public final long readFieldLength() {
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    this.position = i + 1;
    i = arrayOfByte[i] & 0xFF;
    switch (i) {
      default:
        return i;
      case 254:
        return readLongLong();
      case 253:
        i = readLongInt();
        return i;
      case 252:
        i = readInt();
        return i;
      case 251:
        break;
    } 
    return -1L;
  }
  
  public final int readInt() {
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    j = arrayOfByte[j];
    this.position = i + 1;
    return (arrayOfByte[i] & 0xFF) << 8 | j & 0xFF;
  }
  
  public final int readIntAsLong() {
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    int j = i + 1;
    this.position = j;
    i = arrayOfByte[i];
    int m = j + 1;
    this.position = m;
    j = arrayOfByte[j];
    int k = m + 1;
    this.position = k;
    m = arrayOfByte[m];
    this.position = k + 1;
    return (arrayOfByte[k] & 0xFF) << 24 | i & 0xFF | (j & 0xFF) << 8 | (m & 0xFF) << 16;
  }
  
  public final byte[] readLenByteArray(int paramInt) {
    long l = readFieldLength();
    if (l == -1L)
      return null; 
    if (l == 0L)
      return Constants.EMPTY_BYTE_ARRAY; 
    this.position += paramInt;
    return getBytes((int)l);
  }
  
  public final long readLength() {
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    this.position = i + 1;
    i = arrayOfByte[i] & 0xFF;
    switch (i) {
      default:
        return i;
      case 254:
        return readLong();
      case 253:
        i = readLongInt();
        return i;
      case 252:
        i = readInt();
        return i;
      case 251:
        break;
    } 
    return 0L;
  }
  
  public final long readLong() {
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    long l1 = arrayOfByte[j];
    j = i + 1;
    this.position = j;
    long l2 = arrayOfByte[i];
    i = j + 1;
    this.position = i;
    long l3 = (arrayOfByte[j] & 0xFF);
    this.position = i + 1;
    return l1 & 0xFFL | (0xFFL & l2) << 8L | l3 << 16L | (arrayOfByte[i] & 0xFF) << 24L;
  }
  
  public final int readLongInt() {
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    int k = i + 1;
    this.position = k;
    i = arrayOfByte[i];
    int j = k + 1;
    this.position = j;
    k = arrayOfByte[k];
    this.position = j + 1;
    return (arrayOfByte[j] & 0xFF) << 16 | i & 0xFF | (k & 0xFF) << 8;
  }
  
  public final long readLongLong() {
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    long l3 = (arrayOfByte[j] & 0xFF);
    j = i + 1;
    this.position = j;
    long l4 = (arrayOfByte[i] & 0xFF);
    i = j + 1;
    this.position = i;
    long l6 = (arrayOfByte[j] & 0xFF);
    j = i + 1;
    this.position = j;
    long l7 = (arrayOfByte[i] & 0xFF);
    int k = j + 1;
    this.position = k;
    long l1 = (arrayOfByte[j] & 0xFF);
    i = k + 1;
    this.position = i;
    long l5 = (arrayOfByte[k] & 0xFF);
    j = i + 1;
    this.position = j;
    long l2 = (arrayOfByte[i] & 0xFF);
    this.position = j + 1;
    return l3 | l4 << 8L | l6 << 16L | l7 << 24L | l1 << 32L | l5 << 40L | l2 << 48L | (arrayOfByte[j] & 0xFF) << 56L;
  }
  
  public final String readString() {
    int i = this.position;
    int j = getBufLength();
    byte b = 0;
    while (i < j && this.byteBuffer[i] != 0) {
      b++;
      i++;
    } 
    String str = StringUtils.toString(this.byteBuffer, this.position, b);
    this.position += b + 1;
    return str;
  }
  
  public final String readString(String paramString, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    int i = this.position;
    int j = getBufLength();
    byte b = 0;
    while (i < j && this.byteBuffer[i] != 0) {
      b++;
      i++;
    } 
    try {
      String str = StringUtils.toString(this.byteBuffer, this.position, b, paramString);
      this.position += b + 1;
      return str;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(Messages.getString("ByteArrayBuffer.1"));
      stringBuilder.append(paramString);
      stringBuilder.append("'");
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
    } finally {}
    this.position += b + 1;
    throw paramString;
  }
  
  public final String readString(String paramString, ExceptionInterceptor paramExceptionInterceptor, int paramInt) throws SQLException {
    if (this.position + paramInt <= getBufLength()) {
      try {
        String str = StringUtils.toString(this.byteBuffer, this.position, paramInt, paramString);
        this.position += paramInt;
        return str;
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(Messages.getString("ByteArrayBuffer.1"));
        stringBuilder.append(paramString);
        stringBuilder.append("'");
        throw SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
      } finally {}
      this.position += paramInt;
      throw paramString;
    } 
    throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.2"), "S1009", paramExceptionInterceptor);
  }
  
  public final int readnBytes() {
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    j = arrayOfByte[j] & 0xFF;
    if (j != 1)
      return (j != 2) ? ((j != 3) ? ((j != 4) ? 255 : (int)readLong()) : readLongInt()) : readInt(); 
    this.position = i + 1;
    return arrayOfByte[i] & 0xFF;
  }
  
  public void setBufLength(int paramInt) {
    this.bufLength = paramInt;
  }
  
  public void setByteBuffer(byte[] paramArrayOfbyte) {
    this.byteBuffer = paramArrayOfbyte;
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
  }
  
  public void setWasMultiPacket(boolean paramBoolean) {
    this.wasMultiPacket = paramBoolean;
  }
  
  public String toString() {
    return dumpClampedBytes(getPosition());
  }
  
  public String toSuperString() {
    return super.toString();
  }
  
  public boolean wasMultiPacket() {
    return this.wasMultiPacket;
  }
  
  public final void writeByte(byte paramByte) throws SQLException {
    ensureCapacity(1);
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    this.position = i + 1;
    arrayOfByte[i] = paramByte;
  }
  
  public final void writeBytesNoNull(byte[] paramArrayOfbyte) throws SQLException {
    int i = paramArrayOfbyte.length;
    ensureCapacity(i);
    System.arraycopy(paramArrayOfbyte, 0, this.byteBuffer, this.position, i);
    this.position += i;
  }
  
  public final void writeBytesNoNull(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws SQLException {
    ensureCapacity(paramInt2);
    System.arraycopy(paramArrayOfbyte, paramInt1, this.byteBuffer, this.position, paramInt2);
    this.position += paramInt2;
  }
  
  public final void writeDouble(double paramDouble) throws SQLException {
    writeLongLong(Double.doubleToLongBits(paramDouble));
  }
  
  public final void writeFieldLength(long paramLong) throws SQLException {
    if (paramLong < 251L) {
      writeByte((byte)(int)paramLong);
    } else if (paramLong < 65536L) {
      ensureCapacity(3);
      writeByte((byte)-4);
      writeInt((int)paramLong);
    } else if (paramLong < 16777216L) {
      ensureCapacity(4);
      writeByte((byte)-3);
      writeLongInt((int)paramLong);
    } else {
      ensureCapacity(9);
      writeByte((byte)-2);
      writeLongLong(paramLong);
    } 
  }
  
  public final void writeFloat(float paramFloat) throws SQLException {
    ensureCapacity(4);
    int i = Float.floatToIntBits(paramFloat);
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int k = j + 1;
    this.position = k;
    arrayOfByte[j] = (byte)(i & 0xFF);
    j = k + 1;
    this.position = j;
    arrayOfByte[k] = (byte)(i >>> 8);
    k = j + 1;
    this.position = k;
    arrayOfByte[j] = (byte)(i >>> 16);
    this.position = k + 1;
    arrayOfByte[k] = (byte)(i >>> 24);
  }
  
  public final void writeInt(int paramInt) throws SQLException {
    ensureCapacity(2);
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    int j = i + 1;
    this.position = j;
    arrayOfByte[i] = (byte)(paramInt & 0xFF);
    this.position = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 8);
  }
  
  public final void writeLenBytes(byte[] paramArrayOfbyte) throws SQLException {
    int i = paramArrayOfbyte.length;
    ensureCapacity(i + 9);
    writeFieldLength(i);
    System.arraycopy(paramArrayOfbyte, 0, this.byteBuffer, this.position, i);
    this.position += i;
  }
  
  public final void writeLenString(String paramString1, String paramString2, String paramString3, SingleByteCharsetConverter paramSingleByteCharsetConverter, boolean paramBoolean, MySQLConnection paramMySQLConnection) throws UnsupportedEncodingException, SQLException {
    byte[] arrayOfByte;
    if (paramSingleByteCharsetConverter != null) {
      arrayOfByte = paramSingleByteCharsetConverter.toBytes(paramString1);
    } else {
      arrayOfByte = StringUtils.getBytes((String)arrayOfByte, paramString2, paramString3, paramBoolean, paramMySQLConnection, paramMySQLConnection.getExceptionInterceptor());
    } 
    int i = arrayOfByte.length;
    ensureCapacity(i + 9);
    writeFieldLength(i);
    System.arraycopy(arrayOfByte, 0, this.byteBuffer, this.position, i);
    this.position += i;
  }
  
  public final void writeLong(long paramLong) throws SQLException {
    ensureCapacity(4);
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    arrayOfByte[j] = (byte)(int)(0xFFL & paramLong);
    j = i + 1;
    this.position = j;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 8L);
    i = j + 1;
    this.position = i;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 16L);
    this.position = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 24L);
  }
  
  public final void writeLongInt(int paramInt) throws SQLException {
    ensureCapacity(3);
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    arrayOfByte[j] = (byte)(paramInt & 0xFF);
    j = i + 1;
    this.position = j;
    arrayOfByte[i] = (byte)(paramInt >>> 8);
    this.position = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 16);
  }
  
  public final void writeLongLong(long paramLong) throws SQLException {
    ensureCapacity(8);
    byte[] arrayOfByte = this.byteBuffer;
    int j = this.position;
    int i = j + 1;
    this.position = i;
    arrayOfByte[j] = (byte)(int)(0xFFL & paramLong);
    int k = i + 1;
    this.position = k;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 8L);
    j = k + 1;
    this.position = j;
    arrayOfByte[k] = (byte)(int)(paramLong >>> 16L);
    i = j + 1;
    this.position = i;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 24L);
    j = i + 1;
    this.position = j;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 32L);
    i = j + 1;
    this.position = i;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 40L);
    j = i + 1;
    this.position = j;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 48L);
    this.position = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 56L);
  }
  
  public final void writeString(String paramString) throws SQLException {
    ensureCapacity(paramString.length() * 3 + 1);
    writeStringNoNull(paramString);
    byte[] arrayOfByte = this.byteBuffer;
    int i = this.position;
    this.position = i + 1;
    arrayOfByte[i] = 0;
  }
  
  public final void writeString(String paramString1, String paramString2, MySQLConnection paramMySQLConnection) throws SQLException {
    ensureCapacity(paramString1.length() * 3 + 1);
    try {
      writeStringNoNull(paramString1, paramString2, paramString2, false, paramMySQLConnection);
      byte[] arrayOfByte = this.byteBuffer;
      int i = this.position;
      this.position = i + 1;
      arrayOfByte[i] = 0;
      return;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new SQLException(unsupportedEncodingException.toString(), "S1000");
    } 
  }
  
  public final void writeStringNoNull(String paramString) throws SQLException {
    int i = paramString.length();
    ensureCapacity(i * 3);
    System.arraycopy(StringUtils.getBytes(paramString), 0, this.byteBuffer, this.position, i);
    this.position += i;
  }
  
  public final void writeStringNoNull(String paramString1, String paramString2, String paramString3, boolean paramBoolean, MySQLConnection paramMySQLConnection) throws UnsupportedEncodingException, SQLException {
    byte[] arrayOfByte = StringUtils.getBytes(paramString1, paramString2, paramString3, paramBoolean, paramMySQLConnection, paramMySQLConnection.getExceptionInterceptor());
    int i = arrayOfByte.length;
    ensureCapacity(i);
    System.arraycopy(arrayOfByte, 0, this.byteBuffer, this.position, i);
    this.position += i;
  }
}
