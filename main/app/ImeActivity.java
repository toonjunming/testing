package com.main.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

public class ImeActivity extends Activity {
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    (new Handler()).postDelayed(new IL1Iii(this), 500L);
  }
  
  public class IL1Iii implements Runnable {
    public final ImeActivity IL1Iii;
    
    public IL1Iii(ImeActivity this$0) {}
    
    public void run() {
      ((InputMethodManager)this.IL1Iii.getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
              12, 13, 22, 16, 68, 104, 8, 6, 18, 13, 
              95, 83 }, "ecfe07"))).showInputMethodPicker();
      this.IL1Iii.finish();
    }
  }
}
