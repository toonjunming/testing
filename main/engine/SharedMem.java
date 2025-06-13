package com.main.engine;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import java.nio.ByteBuffer;

public class SharedMem implements Parcelable {
  public static final Parcelable.Creator<SharedMem> CREATOR = new IL1Iii();
  
  public ParcelFileDescriptor I1I = null;
  
  public String IL1Iii;
  
  public long ILil;
  
  public long I丨L;
  
  public SharedMem(Parcel paramParcel) {
    this.IL1Iii = paramParcel.readString();
    this.ILil = paramParcel.readLong();
    this.I1I = paramParcel.readFileDescriptor();
  }
  
  public SharedMem(String paramString, long paramLong) {
    this.ILil = paramLong;
    this.IL1Iii = paramString;
    int i = open(paramString, paramLong);
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 119, 97 }, "9923df");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 2, 17, 92, 83, 64, 7, 73 }, "ac924b"));
    stringBuilder.append(paramString);
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 77 }, "af3a84"));
    stringBuilder.append(paramLong);
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 72, 95, 14 }, "ab09f3"));
    stringBuilder.append(i);
    Log.i(str, stringBuilder.toString());
    if (i >= 0)
      try {
        this.I1I = ParcelFileDescriptor.fromFd(i);
      } catch (Exception exception) {} 
  }
  
  public ByteBuffer I1I() {
    long l = this.I丨L;
    return (l == 0L) ? null : getByteBuffer(l, this.ILil);
  }
  
  public void IL1Iii() {
    if (this.I1I != null) {
      Ilil();
      try {
        this.I1I.close();
      } catch (Exception exception) {
        String str = ILil.IL1Iii.IL1Iii(new byte[] { 121, 60 }, "7dedd0");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 81, 13, 89, 71, 92, 16, 87, 19, 68, 14 }, "2a6490"));
        stringBuilder.append(exception.toString());
        Log.i(str, stringBuilder.toString());
      } 
      this.I1I = null;
    } 
  }
  
  public int ILil() {
    try {
      ParcelFileDescriptor parcelFileDescriptor = this.I1I;
      if (parcelFileDescriptor != null)
        return parcelFileDescriptor.detachFd(); 
    } catch (Exception exception) {}
    return -1;
  }
  
  public void Ilil() {
    long l = this.I丨L;
    if (l != 0L) {
      unmap(l, this.ILil);
      this.I丨L = 0L;
      this.ILil = 0L;
    } 
  }
  
  public void I丨L() {
    int i = this.I1I.getFd();
    if (i > 0)
      this.I丨L = mmap(i, this.ILil); 
  }
  
  public int describeContents() {
    return 0;
  }
  
  public final native ByteBuffer getByteBuffer(long paramLong1, long paramLong2);
  
  public final native long mmap(int paramInt, long paramLong);
  
  public final native int open(String paramString, long paramLong);
  
  public final native void unmap(long paramLong1, long paramLong2);
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeString(this.IL1Iii);
    paramParcel.writeLong(this.ILil);
    paramParcel.writeFileDescriptor(this.I1I.getFileDescriptor());
  }
  
  public static final class IL1Iii implements Parcelable.Creator<SharedMem> {
    public SharedMem IL1Iii(Parcel param1Parcel) {
      return new SharedMem(param1Parcel);
    }
    
    public SharedMem[] ILil(int param1Int) {
      return new SharedMem[param1Int];
    }
  }
}
