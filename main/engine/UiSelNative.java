package com.main.engine;

import ILil.IL1Iii;
import ILil.I丨L.ILil.L丨1丨1丨I;
import ILil.I丨L.ILil.丨丨;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class UiSelNative implements 丨丨 {
  private boolean bNeedSetNodeMode = false;
  
  private int mNodeMode = -1;
  
  private 丨丨 mProxy;
  
  public UiSelNative(丨丨 param丨丨) {
    this.mProxy = param丨丨;
  }
  
  private boolean checkProxyLive() {
    try {
      丨丨 丨丨1 = this.mProxy;
      if (丨丨1 != null && 丨丨1.asBinder().isBinderAlive()) {
        if (this.bNeedSetNodeMode) {
          int i = this.mNodeMode;
          if (i != -1) {
            this.bNeedSetNodeMode = false;
            setMode(i);
          } 
        } 
        return true;
      } 
      L丨1丨1丨I.LL1IL().IIi丨丨I();
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean accessibilityFocus(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.accessibilityFocus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public IBinder asBinder() {
    return null;
  }
  
  public void bounds(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.bounds(paramLong, paramInt1, paramInt2, paramInt3, paramInt4); 
    } catch (Exception exception) {}
  }
  
  public void boundsContains(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.boundsContains(paramLong, paramInt1, paramInt2, paramInt3, paramInt4); 
    } catch (Exception exception) {}
  }
  
  public void boundsInside(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.boundsInside(paramLong, paramInt1, paramInt2, paramInt3, paramInt4); 
    } catch (Exception exception) {}
  }
  
  public void checkable(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.checkable(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public void checked(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.checked(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public void className(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.className(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void classNameContains(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.classNameContains(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void classNameEndsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.classNameEndsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void classNameMatches(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.classNameMatches(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void classNameStartsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.classNameStartsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public boolean clearAccessibilityFocus(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.clearAccessibilityFocus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean clearFocus(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.clearFocus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean click(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.click(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void clickable(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.clickable(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public boolean collapse(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.collapse(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean contextClick(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.contextClick(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean copy(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.copy(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean cut(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.cut(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void depth(long paramLong, int paramInt) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.depth(paramLong, paramInt); 
    } catch (Exception exception) {}
  }
  
  public void desc(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.desc(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void descContains(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.descContains(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void descEndsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.descEndsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void descMatches(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.descMatches(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void descStartsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.descStartsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public boolean dismiss(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.dismiss(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void drawingOrder(long paramLong, int paramInt) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.drawingOrder(paramLong, paramInt); 
    } catch (Exception exception) {}
  }
  
  public void enabled(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.enabled(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public boolean expand(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.expand(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public long[] findAll(long paramLong1, long paramLong2) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.findAll(paramLong1, paramLong2); 
    } catch (Exception exception) {}
    return null;
  }
  
  public long findOnce(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.findOnce(paramLong); 
    } catch (Exception exception) {}
    return 0L;
  }
  
  public long findOnceEx(long paramLong, int paramInt) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.findOnceEx(paramLong, paramInt); 
    } catch (Exception exception) {}
    return 0L;
  }
  
  public long findOne(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.findOne(paramLong); 
    } catch (Exception exception) {}
    return 0L;
  }
  
  public long findOneEx(long paramLong1, long paramLong2) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.findOneEx(paramLong1, paramLong2); 
    } catch (Exception exception) {}
    return 0L;
  }
  
  public boolean focus(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.focus(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void focusable(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.focusable(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public void focused(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.focused(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public 丨丨 getProxy() {
    return this.mProxy;
  }
  
  public void id(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.id(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void idContains(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.idContains(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void idEndsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.idEndsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void idMatches(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.idMatches(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void idStartsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.idStartsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void index(long paramLong, int paramInt) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.index(paramLong, paramInt); 
    } catch (Exception exception) {}
  }
  
  public boolean longClick(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.longClick(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void longClickable(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.longClickable(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public long obtain() throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.obtain(); 
    } catch (Exception exception) {}
    return 0L;
  }
  
  public Bundle obtainXml() {
    try {
      if (checkProxyLive())
        return this.mProxy.obtainXml(); 
    } catch (Exception exception) {}
    return null;
  }
  
  public void packageName(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.packageName(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void packageNameContains(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.packageNameContains(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void packageNameEndsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.packageNameEndsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void packageNameMatches(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.packageNameMatches(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void packageNameStartsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.packageNameStartsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void password(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.password(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public boolean paste(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.paste(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void release(long paramLong) throws RemoteException {
    String str = IL1Iii.IL1Iii(new byte[] { 47, 104 }, "a00e05");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 16, 87, 13, 87, 86, 71, 7, 26 }, "b2a274"));
    stringBuilder.append(paramLong);
    stringBuilder.append(IL1Iii.IL1Iii(new byte[] { 25 }, "0f9bad"));
    Log.i(str, stringBuilder.toString());
    try {
      if (checkProxyLive())
        this.mProxy.release(paramLong); 
    } catch (Exception exception) {}
  }
  
  public boolean scrollBackward(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollBackward(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollDown(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollDown(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollForward(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollForward(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollLeft(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollLeft(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollRight(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollRight(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollTo(long paramLong, int paramInt1, int paramInt2) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollTo(paramLong, paramInt1, paramInt2); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean scrollUp(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.scrollUp(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void scrollable(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.scrollable(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public boolean select(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.select(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void selected(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.selected(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
  
  public int setMode(int paramInt) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.setMode(paramInt); 
      this.mNodeMode = paramInt;
      this.bNeedSetNodeMode = true;
    } catch (Exception exception) {}
    return 0;
  }
  
  public boolean setProgress(long paramLong, float paramFloat) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.setProgress(paramLong, paramFloat); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void setProxy(丨丨 param丨丨) {
    this.mProxy = param丨丨;
  }
  
  public boolean setSelection(long paramLong, int paramInt1, int paramInt2) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.setSelection(paramLong, paramInt1, paramInt2); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean setText(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.setText(paramLong, paramString); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean show(long paramLong) throws RemoteException {
    try {
      if (checkProxyLive())
        return this.mProxy.show(paramLong); 
    } catch (Exception exception) {}
    return false;
  }
  
  public void text(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.text(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void textContains(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.textContains(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void textEndsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.textEndsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void textMatches(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.textMatches(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void textStartsWith(long paramLong, String paramString) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.textStartsWith(paramLong, paramString); 
    } catch (Exception exception) {}
  }
  
  public void visibleToUser(long paramLong, boolean paramBoolean) throws RemoteException {
    try {
      if (checkProxyLive())
        this.mProxy.visibleToUser(paramLong, paramBoolean); 
    } catch (Exception exception) {}
  }
}
