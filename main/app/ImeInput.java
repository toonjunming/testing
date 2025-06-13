package com.main.app;

import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.I丨L.ILL;
import Ilil.ILil.IL1Iii.L丨1丨1丨I;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import java.util.concurrent.Callable;

public class ImeInput extends InputMethodService {
  public static ImeInput ILil = null;
  
  public KeyboardView IL1Iii;
  
  public static ImeInput I1I() {
    return ILil;
  }
  
  public boolean IL1Iii() {
    return ((Boolean)(new ILL()).<Boolean>ILil(new I丨L(this))).booleanValue();
  }
  
  public boolean ILil() {
    return ((Boolean)(new ILL()).<Boolean>ILil(new l丨Li1LL(this))).booleanValue();
  }
  
  public boolean Ilil() {
    return ((Boolean)(new ILL()).<Boolean>ILil(new ILil(this))).booleanValue();
  }
  
  public boolean I丨L(String paramString) {
    return ((Boolean)(new ILL()).<Boolean>ILil(new IL1Iii(this, paramString))).booleanValue();
  }
  
  public boolean l丨Li1LL(int paramInt1, int paramInt2) {
    return ((Boolean)(new ILL()).<Boolean>ILil(new I1I(this, paramInt1, paramInt2))).booleanValue();
  }
  
  public void onCreate() {
    super.onCreate();
    ILil = this;
    L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(104, ILil.IL1Iii.IL1Iii(new byte[] { 7 }, "6566f1"), ILil.IL1Iii.IL1Iii(new byte[] { 5 }, "5e272b")));
    Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 42, 96 }, "d8e01a"), ILil.IL1Iii.IL1Iii(new byte[] { 
            86, 71, 0, 88, 25, 9, 75, 23, 12, 91, 
            92, 68, 24, 22 }, "97e69e"));
  }
  
  public View onCreateInputView() {
    return (View)this.IL1Iii;
  }
  
  public void onDestroy() {
    super.onDestroy();
    Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 120, 58 }, "6b31aa"), ILil.IL1Iii.IL1Iii(new byte[] { 
            42, 89, 69, 22, 23, 96, 6, 79, 65, 67, 
            39, 81, 16, 67, 90, 17, 26, 21, 66, 22 }, "c75cc4"));
    L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(104, ILil.IL1Iii.IL1Iii(new byte[] { 83 }, "c85422"), ILil.IL1Iii.IL1Iii(new byte[] { 8 }, "8bfbad")));
    ILil = null;
  }
  
  public boolean onEvaluateFullscreenMode() {
    return false;
  }
  
  public void onFinishInputView(boolean paramBoolean) {
    super.onFinishInputView(paramBoolean);
  }
  
  public class I1I implements Callable<Boolean> {
    public final ImeInput I1I;
    
    public final int IL1Iii;
    
    public final int ILil;
    
    public I1I(ImeInput this$0, int param1Int1, int param1Int2) {}
    
    public Boolean IL1Iii() throws Exception {
      try {
        KeyEvent keyEvent = new KeyEvent();
        this(this.IL1Iii, this.ILil);
        boolean bool = this.I1I.getCurrentInputConnection().sendKeyEvent(keyEvent);
        return Boolean.valueOf(bool);
      } catch (Exception exception) {
        return Boolean.FALSE;
      } 
    }
  }
  
  public class IL1Iii implements Callable<Boolean> {
    public final String IL1Iii;
    
    public final ImeInput ILil;
    
    public IL1Iii(ImeInput this$0, String param1String) {}
    
    public Boolean IL1Iii() throws Exception {
      try {
        InputConnection inputConnection = this.ILil.getCurrentInputConnection();
        String str = this.IL1Iii;
        boolean bool = inputConnection.commitText(str, str.length());
        return Boolean.valueOf(bool);
      } catch (Exception exception) {
        exception.printStackTrace();
        return Boolean.FALSE;
      } 
    }
  }
  
  public class ILil implements Callable<Boolean> {
    public final ImeInput IL1Iii;
    
    public ILil(ImeInput this$0) {}
    
    public Boolean IL1Iii() throws Exception {
      try {
        boolean bool;
        if (this.IL1Iii.getCurrentInputConnection() != null) {
          bool = true;
        } else {
          bool = false;
        } 
        return Boolean.valueOf(bool);
      } catch (Exception exception) {
        return Boolean.FALSE;
      } 
    }
  }
  
  public class I丨L implements Callable<Boolean> {
    public final ImeInput IL1Iii;
    
    public I丨L(ImeInput this$0) {}
    
    public Boolean IL1Iii() throws Exception {
      try {
        boolean bool = this.IL1Iii.getCurrentInputConnection().deleteSurroundingText(1, 0);
        return Boolean.valueOf(bool);
      } catch (Exception exception) {
        return Boolean.FALSE;
      } 
    }
  }
  
  public class l丨Li1LL implements Callable<Boolean> {
    public final ImeInput IL1Iii;
    
    public l丨Li1LL(ImeInput this$0) {}
    
    public Boolean IL1Iii() throws Exception {
      try {
        boolean bool = this.IL1Iii.getCurrentInputConnection().finishComposingText();
        return Boolean.valueOf(bool);
      } catch (Exception exception) {
        return Boolean.FALSE;
      } 
    }
  }
}
