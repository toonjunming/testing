package com.mysql.jdbc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BufferRow extends ResultSetRow {
  private int homePosition = 0;
  
  private boolean isBinaryEncoded;
  
  private boolean[] isNull;
  
  private int lastRequestedIndex = -1;
  
  private int lastRequestedPos;
  
  private Field[] metadata;
  
  private List<InputStream> openStreams;
  
  private int preNullBitmaskHomePosition = 0;
  
  private Buffer rowFromServer;
  
  public BufferRow(Buffer paramBuffer, Field[] paramArrayOfField, boolean paramBoolean, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    super(paramExceptionInterceptor);
    this.rowFromServer = paramBuffer;
    this.metadata = paramArrayOfField;
    this.isBinaryEncoded = paramBoolean;
    int i = paramBuffer.getPosition();
    this.homePosition = i;
    this.preNullBitmaskHomePosition = i;
    if (paramArrayOfField != null)
      setMetadata(paramArrayOfField); 
  }
  
  private int findAndSeekToOffset(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      boolean bool = false;
      int i = 0;
      if (paramInt == 0) {
        this.lastRequestedIndex = 0;
        paramInt = this.homePosition;
        this.lastRequestedPos = paramInt;
        this.rowFromServer.setPosition(paramInt);
        return 0;
      } 
      int j = this.lastRequestedIndex;
      if (paramInt == j) {
        this.rowFromServer.setPosition(this.lastRequestedPos);
        return this.lastRequestedPos;
      } 
      if (paramInt > j) {
        if (j >= 0)
          i = j; 
        this.rowFromServer.setPosition(this.lastRequestedPos);
      } else {
        this.rowFromServer.setPosition(this.homePosition);
        i = bool;
      } 
      while (i < paramInt) {
        this.rowFromServer.fastSkipLenByteArray();
        i++;
      } 
      this.lastRequestedIndex = paramInt;
      paramInt = this.rowFromServer.getPosition();
      this.lastRequestedPos = paramInt;
      return paramInt;
    } 
    return findAndSeekToOffsetForBinaryEncoding(paramInt);
  }
  
  private int findAndSeekToOffsetForBinaryEncoding(int paramInt) throws SQLException {
    int j = 0;
    int k = 0;
    if (paramInt == 0) {
      this.lastRequestedIndex = 0;
      paramInt = this.homePosition;
      this.lastRequestedPos = paramInt;
      this.rowFromServer.setPosition(paramInt);
      return 0;
    } 
    int i = this.lastRequestedIndex;
    if (paramInt == i) {
      this.rowFromServer.setPosition(this.lastRequestedPos);
      return this.lastRequestedPos;
    } 
    if (paramInt > i) {
      if (i < 0) {
        this.lastRequestedPos = this.homePosition;
        i = k;
      } 
      this.rowFromServer.setPosition(this.lastRequestedPos);
    } else {
      this.rowFromServer.setPosition(this.homePosition);
      i = j;
    } 
    while (i < paramInt) {
      if (!this.isNull[i]) {
        j = this.rowFromServer.getPosition();
        k = this.metadata[i].getMysqlType();
        if (k != 15 && k != 16 && k != 245 && k != 246) {
          StringBuilder stringBuilder;
          switch (k) {
            default:
              switch (k) {
                default:
                  stringBuilder = new StringBuilder();
                  stringBuilder.append(Messages.getString("MysqlIO.97"));
                  stringBuilder.append(this.metadata[i].getMysqlType());
                  stringBuilder.append(Messages.getString("MysqlIO.98"));
                  stringBuilder.append(i + 1);
                  stringBuilder.append(Messages.getString("MysqlIO.99"));
                  stringBuilder.append(this.metadata.length);
                  stringBuilder.append(Messages.getString("MysqlIO.100"));
                  throw SQLError.createSQLException(stringBuilder.toString(), "S1000", this.exceptionInterceptor);
                case 249:
                case 250:
                case 251:
                case 252:
                case 253:
                case 254:
                case 255:
                  break;
              } 
            case 11:
              this.rowFromServer.fastSkipLenByteArray();
              break;
            case 10:
              this.rowFromServer.fastSkipLenByteArray();
              break;
            case 8:
              this.rowFromServer.setPosition(j + 8);
              break;
            case 7:
            case 12:
              this.rowFromServer.fastSkipLenByteArray();
              break;
            case 5:
              this.rowFromServer.setPosition(j + 8);
              break;
            case 4:
              this.rowFromServer.setPosition(j + 4);
              break;
            case 3:
            case 9:
              this.rowFromServer.setPosition(j + 4);
              break;
            case 2:
            case 13:
              this.rowFromServer.setPosition(j + 2);
              break;
            case 1:
              this.rowFromServer.setPosition(j + 1);
              break;
            case 0:
              this.rowFromServer.fastSkipLenByteArray();
              break;
            case 6:
              break;
          } 
        } else {
        
        } 
      } 
      i++;
    } 
    this.lastRequestedIndex = paramInt;
    paramInt = this.rowFromServer.getPosition();
    this.lastRequestedPos = paramInt;
    return paramInt;
  }
  
  private void setupIsNullBitmask() throws SQLException {
    if (this.isNull != null)
      return; 
    this.rowFromServer.setPosition(this.preNullBitmaskHomePosition);
    int j = (this.metadata.length + 9) / 8;
    byte[] arrayOfByte = new byte[j];
    int i;
    for (i = 0; i < j; i++)
      arrayOfByte[i] = this.rowFromServer.readByte(); 
    this.homePosition = this.rowFromServer.getPosition();
    this.isNull = new boolean[this.metadata.length];
    i = 4;
    j = 0;
    for (int k = 0; j < this.metadata.length; k = m) {
      boolean bool;
      boolean[] arrayOfBoolean = this.isNull;
      if ((arrayOfByte[k] & i) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      arrayOfBoolean[j] = bool;
      int n = i << 1;
      i = n;
      int m = k;
      if ((n & 0xFF) == 0) {
        m = k + 1;
        i = 1;
      } 
      j++;
    } 
  }
  
  public void closeOpenStreams() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield openStreams : Ljava/util/List;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 53
    //   11: aload_2
    //   12: invokeinterface iterator : ()Ljava/util/Iterator;
    //   17: astore_3
    //   18: aload_3
    //   19: invokeinterface hasNext : ()Z
    //   24: istore_1
    //   25: iload_1
    //   26: ifeq -> 44
    //   29: aload_3
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast java/io/InputStream
    //   38: invokevirtual close : ()V
    //   41: goto -> 18
    //   44: aload_0
    //   45: getfield openStreams : Ljava/util/List;
    //   48: invokeinterface clear : ()V
    //   53: aload_0
    //   54: monitorexit
    //   55: return
    //   56: astore_2
    //   57: aload_0
    //   58: monitorexit
    //   59: aload_2
    //   60: athrow
    //   61: astore_2
    //   62: goto -> 18
    // Exception table:
    //   from	to	target	type
    //   2	7	56	finally
    //   11	18	56	finally
    //   18	25	56	finally
    //   29	41	61	java/io/IOException
    //   29	41	56	finally
    //   44	53	56	finally
  }
  
  public InputStream getBinaryInputStream(int paramInt) throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isBinaryEncoded : Z
    //   6: ifeq -> 23
    //   9: aload_0
    //   10: iload_1
    //   11: invokevirtual isNull : (I)Z
    //   14: istore_2
    //   15: iload_2
    //   16: ifeq -> 23
    //   19: aload_0
    //   20: monitorexit
    //   21: aconst_null
    //   22: areturn
    //   23: aload_0
    //   24: iload_1
    //   25: invokespecial findAndSeekToOffset : (I)I
    //   28: pop
    //   29: aload_0
    //   30: getfield rowFromServer : Lcom/mysql/jdbc/Buffer;
    //   33: invokevirtual readFieldLength : ()J
    //   36: lstore_3
    //   37: aload_0
    //   38: getfield rowFromServer : Lcom/mysql/jdbc/Buffer;
    //   41: invokevirtual getPosition : ()I
    //   44: istore_1
    //   45: lload_3
    //   46: ldc2_w -1
    //   49: lcmp
    //   50: ifne -> 57
    //   53: aload_0
    //   54: monitorexit
    //   55: aconst_null
    //   56: areturn
    //   57: new java/io/ByteArrayInputStream
    //   60: astore #5
    //   62: aload #5
    //   64: aload_0
    //   65: getfield rowFromServer : Lcom/mysql/jdbc/Buffer;
    //   68: invokevirtual getByteBuffer : ()[B
    //   71: iload_1
    //   72: lload_3
    //   73: l2i
    //   74: invokespecial <init> : ([BII)V
    //   77: aload_0
    //   78: getfield openStreams : Ljava/util/List;
    //   81: ifnonnull -> 100
    //   84: new java/util/LinkedList
    //   87: astore #6
    //   89: aload #6
    //   91: invokespecial <init> : ()V
    //   94: aload_0
    //   95: aload #6
    //   97: putfield openStreams : Ljava/util/List;
    //   100: aload_0
    //   101: monitorexit
    //   102: aload #5
    //   104: areturn
    //   105: astore #5
    //   107: aload_0
    //   108: monitorexit
    //   109: aload #5
    //   111: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	105	finally
    //   23	45	105	finally
    //   57	100	105	finally
  }
  
  public int getBytesSize() {
    return this.rowFromServer.getBufLength();
  }
  
  public byte[] getColumnValue(int paramInt) throws SQLException {
    findAndSeekToOffset(paramInt);
    if (!this.isBinaryEncoded)
      return this.rowFromServer.readLenByteArray(0); 
    if (this.isNull[paramInt])
      return null; 
    int i = this.metadata[paramInt].getMysqlType();
    if (i != 15 && i != 16 && i != 245 && i != 246) {
      StringBuilder stringBuilder;
      switch (i) {
        default:
          switch (i) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append(Messages.getString("MysqlIO.97"));
              stringBuilder.append(this.metadata[paramInt].getMysqlType());
              stringBuilder.append(Messages.getString("MysqlIO.98"));
              stringBuilder.append(paramInt + 1);
              stringBuilder.append(Messages.getString("MysqlIO.99"));
              stringBuilder.append(this.metadata.length);
              stringBuilder.append(Messages.getString("MysqlIO.100"));
              throw SQLError.createSQLException(stringBuilder.toString(), "S1000", this.exceptionInterceptor);
            case 249:
            case 250:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
              break;
          } 
          break;
        case 8:
          return this.rowFromServer.getBytes(8);
        case 6:
          return null;
        case 5:
          return this.rowFromServer.getBytes(8);
        case 4:
          return this.rowFromServer.getBytes(4);
        case 3:
        case 9:
          return this.rowFromServer.getBytes(4);
        case 2:
        case 13:
          return this.rowFromServer.getBytes(2);
        case 1:
          return new byte[] { this.rowFromServer.readByte() };
        case 0:
        case 7:
        case 10:
        case 11:
        case 12:
          break;
      } 
    } 
    return this.rowFromServer.readLenByteArray(0);
  }
  
  public Date getDateFast(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException {
    if (isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    int i = this.rowFromServer.getPosition();
    return getDateFast(paramInt, this.rowFromServer.getByteBuffer(), i, (int)l, paramMySQLConnection, paramResultSetImpl, paramCalendar);
  }
  
  public int getInt(int paramInt) throws SQLException {
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    paramInt = this.rowFromServer.getPosition();
    return (l == -1L) ? 0 : StringUtils.getInt(this.rowFromServer.getByteBuffer(), paramInt, (int)l + paramInt);
  }
  
  public long getLong(int paramInt) throws SQLException {
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    paramInt = this.rowFromServer.getPosition();
    return (l == -1L) ? 0L : StringUtils.getLong(this.rowFromServer.getByteBuffer(), paramInt, (int)l + paramInt);
  }
  
  public Date getNativeDate(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException {
    if (isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    int i = this.rowFromServer.getPosition();
    return getNativeDate(paramInt, this.rowFromServer.getByteBuffer(), i, (int)l, paramMySQLConnection, paramResultSetImpl, paramCalendar);
  }
  
  public Object getNativeDateTimeValue(int paramInt1, Calendar paramCalendar, int paramInt2, int paramInt3, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    if (isNull(paramInt1))
      return null; 
    findAndSeekToOffset(paramInt1);
    long l = this.rowFromServer.readFieldLength();
    int i = this.rowFromServer.getPosition();
    return getNativeDateTimeValue(paramInt1, this.rowFromServer.getByteBuffer(), i, (int)l, paramCalendar, paramInt2, paramInt3, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public double getNativeDouble(int paramInt) throws SQLException {
    if (isNull(paramInt))
      return 0.0D; 
    findAndSeekToOffset(paramInt);
    paramInt = this.rowFromServer.getPosition();
    return getNativeDouble(this.rowFromServer.getByteBuffer(), paramInt);
  }
  
  public float getNativeFloat(int paramInt) throws SQLException {
    if (isNull(paramInt))
      return 0.0F; 
    findAndSeekToOffset(paramInt);
    paramInt = this.rowFromServer.getPosition();
    return getNativeFloat(this.rowFromServer.getByteBuffer(), paramInt);
  }
  
  public int getNativeInt(int paramInt) throws SQLException {
    if (isNull(paramInt))
      return 0; 
    findAndSeekToOffset(paramInt);
    paramInt = this.rowFromServer.getPosition();
    return getNativeInt(this.rowFromServer.getByteBuffer(), paramInt);
  }
  
  public long getNativeLong(int paramInt) throws SQLException {
    if (isNull(paramInt))
      return 0L; 
    findAndSeekToOffset(paramInt);
    paramInt = this.rowFromServer.getPosition();
    return getNativeLong(this.rowFromServer.getByteBuffer(), paramInt);
  }
  
  public short getNativeShort(int paramInt) throws SQLException {
    if (isNull(paramInt))
      return 0; 
    findAndSeekToOffset(paramInt);
    paramInt = this.rowFromServer.getPosition();
    return getNativeShort(this.rowFromServer.getByteBuffer(), paramInt);
  }
  
  public Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    if (isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    int i = this.rowFromServer.getPosition();
    return getNativeTime(paramInt, this.rowFromServer.getByteBuffer(), i, (int)l, paramCalendar, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    if (isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    paramInt = this.rowFromServer.getPosition();
    return getNativeTimestamp(this.rowFromServer.getByteBuffer(), paramInt, (int)l, paramCalendar, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public Reader getReader(int paramInt) throws SQLException {
    InputStream inputStream = getBinaryInputStream(paramInt);
    if (inputStream == null)
      return null; 
    try {
      return new InputStreamReader(inputStream, this.metadata[paramInt].getEncoding());
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      SQLException sQLException = SQLError.createSQLException("", this.exceptionInterceptor);
      sQLException.initCause(unsupportedEncodingException);
      throw sQLException;
    } 
  }
  
  public String getString(int paramInt, String paramString, MySQLConnection paramMySQLConnection) throws SQLException {
    if (this.isBinaryEncoded && isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    if (l == -1L)
      return null; 
    if (l == 0L)
      return ""; 
    paramInt = this.rowFromServer.getPosition();
    return getString(paramString, paramMySQLConnection, this.rowFromServer.getByteBuffer(), paramInt, (int)l);
  }
  
  public Time getTimeFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl) throws SQLException {
    if (isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    int i = this.rowFromServer.getPosition();
    return getTimeFast(paramInt, this.rowFromServer.getByteBuffer(), i, (int)l, paramCalendar, paramTimeZone, paramBoolean, paramMySQLConnection, paramResultSetImpl);
  }
  
  public Timestamp getTimestampFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean1, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, boolean paramBoolean2, boolean paramBoolean3) throws SQLException {
    if (isNull(paramInt))
      return null; 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    int i = this.rowFromServer.getPosition();
    return getTimestampFast(paramInt, this.rowFromServer.getByteBuffer(), i, (int)l, paramCalendar, paramTimeZone, paramBoolean1, paramMySQLConnection, paramResultSetImpl, paramBoolean2, paramBoolean3);
  }
  
  public boolean isFloatingPointNumber(int paramInt) throws SQLException {
    if (this.isBinaryEncoded) {
      paramInt = this.metadata[paramInt].getSQLType();
      return !(paramInt != 2 && paramInt != 3 && paramInt != 6 && paramInt != 8);
    } 
    findAndSeekToOffset(paramInt);
    long l = this.rowFromServer.readFieldLength();
    if (l == -1L)
      return false; 
    if (l == 0L)
      return false; 
    int i = this.rowFromServer.getPosition();
    byte[] arrayOfByte = this.rowFromServer.getByteBuffer();
    for (paramInt = 0; paramInt < (int)l; paramInt++) {
      char c = (char)arrayOfByte[i + paramInt];
      if (c == 'e' || c == 'E')
        return true; 
    } 
    return false;
  }
  
  public boolean isNull(int paramInt) throws SQLException {
    if (!this.isBinaryEncoded) {
      boolean bool;
      findAndSeekToOffset(paramInt);
      if (this.rowFromServer.readFieldLength() == -1L) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    return this.isNull[paramInt];
  }
  
  public long length(int paramInt) throws SQLException {
    findAndSeekToOffset(paramInt);
    long l2 = this.rowFromServer.readFieldLength();
    long l1 = l2;
    if (l2 == -1L)
      l1 = 0L; 
    return l1;
  }
  
  public void setColumnValue(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
    throw new OperationNotSupportedException();
  }
  
  public ResultSetRow setMetadata(Field[] paramArrayOfField) throws SQLException {
    super.setMetadata(paramArrayOfField);
    if (this.isBinaryEncoded)
      setupIsNullBitmask(); 
    return this;
  }
}
