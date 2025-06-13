package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.NullLogger;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class CompressedInputStream extends InputStream {
  private byte[] buffer;
  
  private InputStream in;
  
  private Inflater inflater;
  
  private Log log;
  
  private byte[] packetHeaderBuffer = new byte[7];
  
  private int pos = 0;
  
  private ConnectionPropertiesImpl.BooleanConnectionProperty traceProtocol;
  
  public CompressedInputStream(Connection paramConnection, InputStream paramInputStream) {
    this.traceProtocol = ((ConnectionPropertiesImpl)paramConnection).traceProtocol;
    try {
      this.log = paramConnection.getLog();
    } catch (SQLException sQLException) {
      this.log = new NullLogger(null);
    } 
    this.in = paramInputStream;
    this.inflater = new Inflater();
  }
  
  private void getNextPacketFromServer() throws IOException {
    if (readFully(this.packetHeaderBuffer, 0, 7) >= 7) {
      byte[] arrayOfByte1 = this.packetHeaderBuffer;
      int i = (arrayOfByte1[0] & 0xFF) + ((arrayOfByte1[1] & 0xFF) << 8) + ((arrayOfByte1[2] & 0xFF) << 16);
      int j = (arrayOfByte1[4] & 0xFF) + ((arrayOfByte1[5] & 0xFF) << 8) + ((arrayOfByte1[6] & 0xFF) << 16);
      boolean bool = this.traceProtocol.getValueAsBoolean();
      if (bool) {
        Log log = this.log;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Reading compressed packet of length ");
        stringBuilder.append(i);
        stringBuilder.append(" uncompressed to ");
        stringBuilder.append(j);
        log.logTrace(stringBuilder.toString());
      } 
      if (j > 0) {
        arrayOfByte1 = new byte[j];
        byte[] arrayOfByte = new byte[i];
        readFully(arrayOfByte, 0, i);
        this.inflater.reset();
        this.inflater.setInput(arrayOfByte);
        try {
          this.inflater.inflate(arrayOfByte1);
          i = j;
        } catch (DataFormatException dataFormatException) {
          throw new IOException("Error while uncompressing packet from server.");
        } 
      } else {
        if (bool)
          this.log.logTrace("Packet didn't meet compression threshold, not uncompressing..."); 
        arrayOfByte1 = new byte[i];
        readFully(arrayOfByte1, 0, i);
      } 
      if (bool)
        if (i > 1024) {
          Log log1 = this.log;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Uncompressed packet: \n");
          stringBuilder1.append(StringUtils.dumpAsHex(arrayOfByte1, 256));
          log1.logTrace(stringBuilder1.toString());
          byte[] arrayOfByte = new byte[256];
          System.arraycopy(arrayOfByte1, i - 256, arrayOfByte, 0, 256);
          Log log2 = this.log;
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Uncompressed packet: \n");
          stringBuilder2.append(StringUtils.dumpAsHex(arrayOfByte, 256));
          log2.logTrace(stringBuilder2.toString());
          this.log.logTrace("Large packet dump truncated. Showing first and last 256 bytes.");
        } else {
          Log log = this.log;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Uncompressed packet: \n");
          stringBuilder.append(StringUtils.dumpAsHex(arrayOfByte1, i));
          log.logTrace(stringBuilder.toString());
        }  
      byte[] arrayOfByte3 = this.buffer;
      byte[] arrayOfByte2 = arrayOfByte1;
      if (arrayOfByte3 != null) {
        arrayOfByte2 = arrayOfByte1;
        if (this.pos < arrayOfByte3.length) {
          if (bool)
            this.log.logTrace("Combining remaining packet with new: "); 
          arrayOfByte3 = this.buffer;
          j = arrayOfByte3.length;
          i = this.pos;
          j -= i;
          arrayOfByte2 = new byte[arrayOfByte1.length + j];
          System.arraycopy(arrayOfByte3, i, arrayOfByte2, 0, j);
          System.arraycopy(arrayOfByte1, 0, arrayOfByte2, j, arrayOfByte1.length);
        } 
      } 
      this.pos = 0;
      this.buffer = arrayOfByte2;
      return;
    } 
    throw new IOException("Unexpected end of input stream");
  }
  
  private void getNextPacketIfRequired(int paramInt) throws IOException {
    byte[] arrayOfByte = this.buffer;
    if (arrayOfByte == null || this.pos + paramInt > arrayOfByte.length)
      getNextPacketFromServer(); 
  }
  
  private final int readFully(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 >= 0) {
      int i = 0;
      while (i < paramInt2) {
        int j = this.in.read(paramArrayOfbyte, paramInt1 + i, paramInt2 - i);
        if (j >= 0) {
          i += j;
          continue;
        } 
        throw new EOFException();
      } 
      return i;
    } 
    throw new IndexOutOfBoundsException();
  }
  
  public int available() throws IOException {
    byte[] arrayOfByte = this.buffer;
    return (arrayOfByte == null) ? this.in.available() : (arrayOfByte.length - this.pos + this.in.available());
  }
  
  public void close() throws IOException {
    this.in.close();
    this.buffer = null;
    this.inflater.end();
    this.inflater = null;
    this.traceProtocol = null;
    this.log = null;
  }
  
  public int read() throws IOException {
    try {
      getNextPacketIfRequired(1);
      byte[] arrayOfByte = this.buffer;
      int i = this.pos;
      this.pos = i + 1;
      return arrayOfByte[i] & 0xFF;
    } catch (IOException iOException) {
      return -1;
    } 
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    Objects.requireNonNull(paramArrayOfbyte);
    if (paramInt1 >= 0 && paramInt1 <= paramArrayOfbyte.length && paramInt2 >= 0) {
      int i = paramInt1 + paramInt2;
      if (i <= paramArrayOfbyte.length && i >= 0) {
        if (paramInt2 <= 0)
          return 0; 
        try {
          getNextPacketIfRequired(paramInt2);
          paramInt2 = Math.min(this.buffer.length - this.pos, paramInt2);
          System.arraycopy(this.buffer, this.pos, paramArrayOfbyte, paramInt1, paramInt2);
          this.pos += paramInt2;
          return paramInt2;
        } catch (IOException iOException) {
          return -1;
        } 
      } 
    } 
    throw new IndexOutOfBoundsException();
  }
  
  public long skip(long paramLong) throws IOException {
    long l1 = 0L;
    long l2 = 0L;
    while (l1 < paramLong && read() != -1) {
      l2++;
      l1++;
    } 
    return l2;
  }
}
