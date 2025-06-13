package com.mysql.jdbc.profiler;

import com.mysql.jdbc.StringUtils;
import java.util.Date;

public class ProfilerEvent {
  public static final byte TYPE_EXECUTE = 4;
  
  public static final byte TYPE_FETCH = 5;
  
  public static final byte TYPE_OBJECT_CREATION = 1;
  
  public static final byte TYPE_PREPARE = 2;
  
  public static final byte TYPE_QUERY = 3;
  
  public static final byte TYPE_SLOW_QUERY = 6;
  
  public static final byte TYPE_WARN = 0;
  
  public String catalog;
  
  public int catalogIndex;
  
  public long connectionId;
  
  public String durationUnits;
  
  public String eventCreationPointDesc;
  
  public int eventCreationPointIndex;
  
  public long eventCreationTime;
  
  public long eventDuration;
  
  public byte eventType;
  
  public String hostName;
  
  public int hostNameIndex;
  
  public String message;
  
  public int resultSetId;
  
  public int statementId;
  
  public ProfilerEvent(byte paramByte, String paramString1, String paramString2, long paramLong1, int paramInt1, int paramInt2, long paramLong2, long paramLong3, String paramString3, String paramString4, String paramString5, String paramString6) {
    this.eventType = paramByte;
    this.connectionId = paramLong1;
    this.statementId = paramInt1;
    this.resultSetId = paramInt2;
    this.eventCreationTime = paramLong2;
    this.eventDuration = paramLong3;
    this.durationUnits = paramString3;
    this.eventCreationPointDesc = paramString4;
    this.message = paramString6;
  }
  
  private static byte[] readBytes(byte[] paramArrayOfbyte, int paramInt) {
    int i = readInt(paramArrayOfbyte, paramInt);
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfbyte, paramInt + 4, arrayOfByte, 0, i);
    return arrayOfByte;
  }
  
  private static int readInt(byte[] paramArrayOfbyte, int paramInt) {
    int j = paramInt + 1;
    paramInt = paramArrayOfbyte[paramInt];
    int i = j + 1;
    j = paramArrayOfbyte[j];
    byte b = paramArrayOfbyte[i];
    return (paramArrayOfbyte[i + 1] & 0xFF) << 24 | paramInt & 0xFF | (j & 0xFF) << 8 | (b & 0xFF) << 16;
  }
  
  private static long readLong(byte[] paramArrayOfbyte, int paramInt) {
    int i = paramInt + 1;
    long l6 = (paramArrayOfbyte[paramInt] & 0xFF);
    paramInt = i + 1;
    long l2 = (paramArrayOfbyte[i] & 0xFF);
    i = paramInt + 1;
    long l3 = (paramArrayOfbyte[paramInt] & 0xFF);
    int j = i + 1;
    long l5 = (paramArrayOfbyte[i] & 0xFF);
    paramInt = j + 1;
    long l4 = (paramArrayOfbyte[j] & 0xFF);
    i = paramInt + 1;
    long l1 = (paramArrayOfbyte[paramInt] & 0xFF);
    long l7 = (paramArrayOfbyte[i] & 0xFF);
    return (paramArrayOfbyte[i + 1] & 0xFF) << 56L | l6 | l2 << 8L | l3 << 16L | l5 << 24L | l4 << 32L | l1 << 40L | l7 << 48L;
  }
  
  public static ProfilerEvent unpack(byte[] paramArrayOfbyte) throws Exception {
    byte b = paramArrayOfbyte[0];
    long l2 = readInt(paramArrayOfbyte, 1);
    int m = readInt(paramArrayOfbyte, 9);
    int k = readInt(paramArrayOfbyte, 13);
    long l3 = readLong(paramArrayOfbyte, 17);
    long l1 = readLong(paramArrayOfbyte, 25);
    byte[] arrayOfByte2 = readBytes(paramArrayOfbyte, 29);
    int i = 33;
    if (arrayOfByte2 != null)
      i = 33 + arrayOfByte2.length; 
    readInt(paramArrayOfbyte, i);
    i += 4;
    byte[] arrayOfByte1 = readBytes(paramArrayOfbyte, i);
    int j = i + 4;
    i = j;
    if (arrayOfByte1 != null)
      i = j + arrayOfByte1.length; 
    paramArrayOfbyte = readBytes(paramArrayOfbyte, i);
    if (paramArrayOfbyte != null)
      i = paramArrayOfbyte.length; 
    return new ProfilerEvent(b, "", "", l2, m, k, l3, l1, StringUtils.toString(arrayOfByte2, "ISO8859_1"), StringUtils.toString(arrayOfByte1, "ISO8859_1"), null, StringUtils.toString(paramArrayOfbyte, "ISO8859_1"));
  }
  
  private static int writeBytes(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt) {
    paramInt = writeInt(paramArrayOfbyte1.length, paramArrayOfbyte2, paramInt);
    System.arraycopy(paramArrayOfbyte1, 0, paramArrayOfbyte2, paramInt, paramArrayOfbyte1.length);
    return paramInt + paramArrayOfbyte1.length;
  }
  
  private static int writeInt(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    int i = paramInt2 + 1;
    paramArrayOfbyte[paramInt2] = (byte)(paramInt1 & 0xFF);
    paramInt2 = i + 1;
    paramArrayOfbyte[i] = (byte)(paramInt1 >>> 8);
    i = paramInt2 + 1;
    paramArrayOfbyte[paramInt2] = (byte)(paramInt1 >>> 16);
    paramArrayOfbyte[i] = (byte)(paramInt1 >>> 24);
    return i + 1;
  }
  
  private static int writeLong(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
    int i = paramInt + 1;
    paramArrayOfbyte[paramInt] = (byte)(int)(0xFFL & paramLong);
    int j = i + 1;
    paramArrayOfbyte[i] = (byte)(int)(paramLong >>> 8L);
    paramInt = j + 1;
    paramArrayOfbyte[j] = (byte)(int)(paramLong >>> 16L);
    i = paramInt + 1;
    paramArrayOfbyte[paramInt] = (byte)(int)(paramLong >>> 24L);
    paramInt = i + 1;
    paramArrayOfbyte[i] = (byte)(int)(paramLong >>> 32L);
    i = paramInt + 1;
    paramArrayOfbyte[paramInt] = (byte)(int)(paramLong >>> 40L);
    paramInt = i + 1;
    paramArrayOfbyte[i] = (byte)(int)(paramLong >>> 48L);
    paramArrayOfbyte[paramInt] = (byte)(int)(paramLong >>> 56L);
    return paramInt + 1;
  }
  
  public String getCatalog() {
    return this.catalog;
  }
  
  public long getConnectionId() {
    return this.connectionId;
  }
  
  public String getDurationUnits() {
    return this.durationUnits;
  }
  
  public String getEventCreationPointAsString() {
    return this.eventCreationPointDesc;
  }
  
  public long getEventCreationTime() {
    return this.eventCreationTime;
  }
  
  public long getEventDuration() {
    return this.eventDuration;
  }
  
  public byte getEventType() {
    return this.eventType;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public int getResultSetId() {
    return this.resultSetId;
  }
  
  public int getStatementId() {
    return this.statementId;
  }
  
  public byte[] pack() throws Exception {
    byte[] arrayOfByte2;
    getEventCreationPointAsString();
    String str1 = this.eventCreationPointDesc;
    byte[] arrayOfByte1 = null;
    if (str1 != null) {
      byte[] arrayOfByte = StringUtils.getBytes(str1, "ISO8859_1");
      i = arrayOfByte.length + 4 + 29;
    } else {
      i = 33;
      str1 = null;
    } 
    String str2 = this.message;
    if (str2 != null) {
      arrayOfByte1 = StringUtils.getBytes(str2, "ISO8859_1");
      i += arrayOfByte1.length + 4;
    } else {
      i += 4;
    } 
    str2 = this.durationUnits;
    if (str2 != null) {
      arrayOfByte2 = StringUtils.getBytes(str2, "ISO8859_1");
      i += arrayOfByte2.length + 4;
    } else {
      i += 4;
      arrayOfByte2 = StringUtils.getBytes("", "ISO8859_1");
    } 
    byte[] arrayOfByte3 = new byte[i];
    arrayOfByte3[0] = this.eventType;
    int i = writeLong(this.connectionId, arrayOfByte3, 1);
    i = writeInt(this.statementId, arrayOfByte3, i);
    i = writeInt(this.resultSetId, arrayOfByte3, i);
    i = writeLong(this.eventCreationTime, arrayOfByte3, i);
    i = writeBytes(arrayOfByte2, arrayOfByte3, writeLong(this.eventDuration, arrayOfByte3, i));
    i = writeInt(this.eventCreationPointIndex, arrayOfByte3, i);
    if (str1 != null) {
      i = writeBytes((byte[])str1, arrayOfByte3, i);
    } else {
      i = writeInt(0, arrayOfByte3, i);
    } 
    if (arrayOfByte1 != null) {
      writeBytes(arrayOfByte1, arrayOfByte3, i);
    } else {
      writeInt(0, arrayOfByte3, i);
    } 
    return arrayOfByte3;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(32);
    switch (this.eventType) {
      default:
        stringBuilder.append("UNKNOWN");
        break;
      case 6:
        stringBuilder.append("SLOW QUERY");
        break;
      case 5:
        stringBuilder.append("FETCH");
        break;
      case 4:
        stringBuilder.append("EXECUTE");
        break;
      case 3:
        stringBuilder.append("QUERY");
        break;
      case 2:
        stringBuilder.append("PREPARE");
        break;
      case 1:
        stringBuilder.append("CONSTRUCT");
        break;
      case 0:
        stringBuilder.append("WARN");
        break;
    } 
    stringBuilder.append(" created: ");
    stringBuilder.append(new Date(this.eventCreationTime));
    stringBuilder.append(" duration: ");
    stringBuilder.append(this.eventDuration);
    stringBuilder.append(" connection: ");
    stringBuilder.append(this.connectionId);
    stringBuilder.append(" statement: ");
    stringBuilder.append(this.statementId);
    stringBuilder.append(" resultset: ");
    stringBuilder.append(this.resultSetId);
    if (this.message != null) {
      stringBuilder.append(" message: ");
      stringBuilder.append(this.message);
    } 
    if (this.eventCreationPointDesc != null) {
      stringBuilder.append("\n\nEvent Created at:\n");
      stringBuilder.append(this.eventCreationPointDesc);
    } 
    return stringBuilder.toString();
  }
}
