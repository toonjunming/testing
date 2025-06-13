package com.main.engine;

import ILil.I丨L.ILil.I11L;
import android.os.IBinder;
import android.os.RemoteException;

public class UiAccActionNative implements I11L {
  private I11L mProxy;
  
  public UiAccActionNative(I11L paramI11L) {
    this.mProxy = paramI11L;
  }
  
  public IBinder asBinder() {
    return null;
  }
  
  public boolean click(int paramInt1, int paramInt2, int paramInt3) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.click(paramInt1, paramInt2, paramInt3); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean dispatchGesture(String paramString) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.dispatchGesture(paramString); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean keyPress(int paramInt) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.keyPress(paramInt); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean longClick(int paramInt1, int paramInt2) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.longClick(paramInt1, paramInt2); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void setProxy(I11L paramI11L) {
    this.mProxy = paramI11L;
  }
  
  public boolean swipe(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.swipe(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5); 
    } catch (Exception exception) {}
    return false;
  }
}
