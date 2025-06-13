package com.main.engine;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class BinderContainer implements Parcelable {
  public static final Parcelable.Creator<BinderContainer> CREATOR = new IL1Iii();
  
  public IBinder binder;
  
  public BinderContainer(IBinder paramIBinder) {
    this.binder = paramIBinder;
  }
  
  public BinderContainer(Parcel paramParcel) {
    this.binder = paramParcel.readStrongBinder();
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeStrongBinder(this.binder);
  }
  
  public static final class IL1Iii implements Parcelable.Creator<BinderContainer> {
    public BinderContainer IL1Iii(Parcel param1Parcel) {
      return new BinderContainer(param1Parcel);
    }
    
    public BinderContainer[] ILil(int param1Int) {
      return new BinderContainer[param1Int];
    }
  }
}
