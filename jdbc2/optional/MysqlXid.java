package com.mysql.jdbc.jdbc2.optional;

import javax.transaction.xa.Xid;

public class MysqlXid implements Xid {
  public int hash = 0;
  
  public byte[] myBqual;
  
  public int myFormatId;
  
  public byte[] myGtrid;
  
  public MysqlXid(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt) {
    this.myGtrid = paramArrayOfbyte1;
    this.myBqual = paramArrayOfbyte2;
    this.myFormatId = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Xid) {
      Xid xid = (Xid)paramObject;
      if (this.myFormatId != xid.getFormatId())
        return false; 
      paramObject = xid.getBranchQualifier();
      byte[] arrayOfByte = xid.getGlobalTransactionId();
      if (arrayOfByte != null && arrayOfByte.length == this.myGtrid.length) {
        int i = arrayOfByte.length;
        byte b;
        for (b = 0; b < i; b++) {
          if (arrayOfByte[b] != this.myGtrid[b])
            return false; 
        } 
        if (paramObject != null && paramObject.length == this.myBqual.length) {
          i = paramObject.length;
          for (b = 0; b < i; b++) {
            if (paramObject[b] != this.myBqual[b])
              return false; 
          } 
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public byte[] getBranchQualifier() {
    return this.myBqual;
  }
  
  public int getFormatId() {
    return this.myFormatId;
  }
  
  public byte[] getGlobalTransactionId() {
    return this.myGtrid;
  }
  
  public int hashCode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hash : I
    //   6: ifne -> 43
    //   9: iconst_0
    //   10: istore_1
    //   11: aload_0
    //   12: getfield myGtrid : [B
    //   15: astore_2
    //   16: iload_1
    //   17: aload_2
    //   18: arraylength
    //   19: if_icmpge -> 43
    //   22: aload_0
    //   23: aload_0
    //   24: getfield hash : I
    //   27: bipush #33
    //   29: imul
    //   30: aload_2
    //   31: iload_1
    //   32: baload
    //   33: iadd
    //   34: putfield hash : I
    //   37: iinc #1, 1
    //   40: goto -> 11
    //   43: aload_0
    //   44: getfield hash : I
    //   47: istore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: iload_1
    //   51: ireturn
    //   52: astore_2
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_2
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	52	finally
    //   11	37	52	finally
    //   43	48	52	finally
  }
}
