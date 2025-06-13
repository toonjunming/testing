package com.main.engine;

import ILil.I丨L.ILil.ili丨11;
import ILil.I丨L.ILil.lIIiIlL丨;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.IRotationWatcher;

public class RotateMgr extends IRotationWatcher.Stub {
  public static final String I1I = ILil.IL1Iii.IL1Iii(new byte[] { 
        7, 91, 92, 67, 94, 12, 2, 27, 78, 88, 
        84, 18, 72, 124, 106, 94, 69, 4, 18, 92, 
        87, 95, 102, 4, 18, 86, 80, 84, 67 }, "f5811e");
  
  public static RotateMgr I丨L = new RotateMgr();
  
  public int IL1Iii = 0;
  
  public IL1Iii ILil = null;
  
  public static RotateMgr Ilil() {
    return I丨L;
  }
  
  public int ILil() {
    return this.IL1Iii;
  }
  
  public void iI丨LLL1(IL1Iii paramIL1Iii) {
    try {
      this.ILil = paramIL1Iii;
      lIIiIlL丨 lIIiIlL丨 = ili丨11.Ilil().L丨1丨1丨I();
      lIIiIlL丨.IL1Iii(this);
      int i = lIIiIlL丨.I1I();
      this.IL1Iii = i;
      this.ILil.IL1Iii(i);
    } catch (Exception exception) {
      String str = ILil.IL1Iii.IL1Iii(new byte[] { 121, 109 }, "758d59");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 83, 16, 68, 92 }, "6b6f5d"));
      stringBuilder.append(exception.toString());
      Log.i(str, stringBuilder.toString());
    } 
  }
  
  public void onRotationChanged(int paramInt) throws RemoteException {}
  
  public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2) throws RemoteException {
    String str = I1I;
    if (paramInt1 != 1) {
      if (paramInt1 != 1598968902)
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2); 
      paramParcel2.writeString(str);
      return true;
    } 
    paramParcel1.enforceInterface(str);
    paramInt1 = paramParcel1.readInt();
    this.IL1Iii = paramInt1;
    this.ILil.IL1Iii(paramInt1);
    paramParcel2.writeNoException();
    return true;
  }
  
  public static interface IL1Iii {
    void IL1Iii(int param1Int);
  }
}
