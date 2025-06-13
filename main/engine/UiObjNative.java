package com.main.engine;

import ILil.IL1Iii;
import ILil.I丨L.ILil.丨丨LLlI1;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class UiObjNative implements 丨丨LLlI1 {
  private 丨丨LLlI1 mProxy;
  
  public UiObjNative(丨丨LLlI1 param丨丨LLlI1) {
    this.mProxy = param丨丨LLlI1;
  }
  
  public boolean accessibilityFocus(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.accessibilityFocus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public IBinder asBinder() {
    return null;
  }
  
  public Rect bounds(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.bounds(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public Rect boundsInParent(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.boundsInParent(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public int childCount(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.childCount(paramLong); 
    } catch (Exception exception) {}
    return 0;
  }
  
  public long[] childs(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.childs(paramLong); 
    } catch (Exception exception) {}
    return new long[0];
  }
  
  public String className(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.className(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public boolean clearAccessibilityFocus(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.clearAccessibilityFocus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean clearFocus(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.clearFocus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean click(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.click(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean collapse(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.collapse(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean contextClick(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.contextClick(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean copy(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.copy(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean cut(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.cut(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public int depth(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.depth(paramLong); 
    } catch (Exception exception) {}
    return -1;
  }
  
  public String desc(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.desc(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public boolean dismiss(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.dismiss(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public int drawingOrder(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.drawingOrder(paramLong); 
    } catch (Exception exception) {}
    return 0;
  }
  
  public boolean expand(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.expand(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean focus(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.focus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean gc(long paramLong) throws RemoteException {
    String str = IL1Iii.IL1Iii(new byte[] { 123, 111 }, "57ce67");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 12, 86, 88, 94, 86, 78 }, "c4295f"));
    stringBuilder.append(paramLong);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 25 }, "06c096"));
    Log.i(str, stringBuilder.toString());
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.gc(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public String getText(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.getText(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public String id(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.id(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public int index(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.index(paramLong); 
    } catch (Exception exception) {}
    return -1;
  }
  
  public boolean longClick(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.longClick(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public String packageName(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.packageName(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
  
  public long parent(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.parent(paramLong); 
    } catch (Exception exception) {}
    return 0L;
  }
  
  public boolean paste(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.paste(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean performAction(long paramLong, int paramInt) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.performAction(paramLong, paramInt); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean performActionEx(long paramLong, int paramInt, Bundle paramBundle) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.performActionEx(paramLong, paramInt, paramBundle); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollBackward(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollBackward(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollDown(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollDown(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollForward(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollForward(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollLeft(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollLeft(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollRight(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollRight(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollTo(long paramLong, int paramInt1, int paramInt2) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollTo(paramLong, paramInt1, paramInt2); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollUp(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.scrollUp(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean select(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.select(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean setProgress(long paramLong, float paramFloat) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.setProgress(paramLong, paramFloat); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void setProxy(丨丨LLlI1 param丨丨LLlI1) {
    this.mProxy = param丨丨LLlI1;
  }
  
  public boolean setSelection(long paramLong, int paramInt1, int paramInt2) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.setSelection(paramLong, paramInt1, paramInt2); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean setText(long paramLong, String paramString) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.setText(paramLong, paramString); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean show(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.show(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public String text(long paramLong) throws RemoteException {
    try {
      if (this.mProxy.asBinder().isBinderAlive())
        return this.mProxy.text(paramLong); 
    } catch (Exception exception) {}
    return null;
  }
}
