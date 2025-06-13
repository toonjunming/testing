package com.main.app;

import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.ILil.LlLI1;
import ILil.I丨L.ILil.L丨1l;
import ILil.I丨L.ILil.i1;
import ILil.I丨L.I丨L.I11li1;
import ILil.I丨L.I丨L.ll丨L1ii;
import ILil.I丨L.I丨L.l丨Li1LL;
import Ilil.ILil.IL1Iii.L丨1丨1丨I;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.nx.assist.UIMgr;
import com.nx.assist.ui.UI;
import com.nx.assist.ui.Window;
import java.io.File;
import org.json.JSONObject;

public class ScriptActivity extends AppCompatActivity {
  public Window I1I = null;
  
  public String IL1Iii = "";
  
  public ViewGroup ILil;
  
  public boolean Ilil = false;
  
  public ILil.I丨L.IL1Iii.ILil I丨L;
  
  public String l丨Li1LL = null;
  
  public final void I丨iL(Intent paramIntent) {
    this.l丨Li1LL = paramIntent.getStringExtra(ILil.IL1Iii.IL1Iii(new byte[] { 22, 8, 83, 89, 89, 85 }, "ca5050"));
    this.Ilil = false;
    ILil.Ilil.IL1Iii.IL1Iii.I丨iL((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
            -41, -17, -110, -33, -120, -114, -42, -35, -97, 25, 
            27, 29 }, "2e2753"));
    (new Thread(new I1I(this))).start();
  }
  
  public void finish() {
    super.finish();
    this.I丨L.L丨1丨1丨I();
  }
  
  public final void iI丨LLL1() {
    File file = L丨1l.I1I().I丨iL();
    try {
      File file1 = new File();
      this(file, ILil.IL1Iii.IL1Iii(new byte[] { 0, 88, 71, 16, 29, 26, 15, 69, 92, 12 }, "e63bd4"));
      String str = I11li1.Ili(file1);
      JSONObject jSONObject = new JSONObject();
      this(str);
      str = jSONObject.getString(ILil.IL1Iii.IL1Iii(new byte[] { 17, 88, 59, 0, 87, 69, 22, 72 }, "d1de91"));
      if (!TextUtils.isEmpty(this.l丨Li1LL)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { -44, -16, -70, -39, -8, -102, 28 }, "3e60e8"));
        stringBuilder.append(this.l丨Li1LL);
        str = stringBuilder.toString();
      } 
      File file2 = new File();
      this(file, str);
      str = I11li1.Ili(file2);
      UI uI = UIMgr.getInstance().parseUI(str, -1);
      if (uI != null) {
        Window window = (Window)uI.inflateView(I11li1.l1IIi1丨());
        window.hideBottom();
        window.hideTitle();
        if (window != null) {
          this.ILil.removeAllViews();
          this.ILil.addView((View)window);
          this.I1I = window;
        } 
      } 
    } catch (Exception exception) {
      String str = ILil.IL1Iii.IL1Iii(new byte[] { 126, 111 }, "07e103");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 
              16, 10, 89, 65, 76, 80, 67, 7, 68, 68, 
              4, 4, 93 }, "cb6699"));
      stringBuilder.append(exception.toString());
      Log.i(str, stringBuilder.toString());
    } 
  }
  
  public void onCreate(Bundle paramBundle) {
    boolean bool;
    super.onCreate(paramBundle);
    setContentView(2131492897);
    new Handler();
    this.I丨L = new ILil.I丨L.IL1Iii.ILil(this);
    l丨Li1LL.IL1Iii(this, ILil.IL1Iii.IL1Iii(new byte[] { 
            -48, -96, -22, -40, -87, -60, -46, -65, -58, -39, 
            -119, -37, -45, -115, -53, -39, -101, -47, -48, -99, 
            -21, -42, -103, -17 }, "75f14f"));
    this.IL1Iii = I11li1.L1iI1((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 22, 5, 74, 13, 70, 21, 75, 10, 74, 14 }, "ef8d6a"), false);
    this.ILil = findViewById(2131296365);
    File file = new File(this.IL1Iii);
    if (ILil.I丨L.I丨L.ILil.IL1Iii((Context)this).I丨L() || !file.exists()) {
      Log.i(ILil.IL1Iii.IL1Iii(new byte[] { 47, 96 }, "a80454"), ILil.IL1Iii.IL1Iii(new byte[] { 
              66, 68, 6, 5, 16, 6, 23, 71, 1, 22, 
              13, 19, 67, 5, 67, 69, 69 }, "74bddc"));
      File file1 = new File(getFilesDir(), ILil.IL1Iii.IL1Iii(new byte[] { 16, 82, 16, 13, 73, 70, 77, 93, 16, 14 }, "c1bd92"));
      if (file1.exists())
        file1.delete(); 
      I11li1.I11L((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 17, 91, 69, 92, 64, 71, 76, 84, 69, 95 }, "b87503"), ILil.IL1Iii.IL1Iii(new byte[] { 64, 1, 23, 94, 67, 65, 29, 14, 23, 93 }, "3be735"));
      if (file.exists()) {
        long l = file.lastModified();
        LlLI1.IL1Iii().Ilil(l, (Context)this);
      } 
    } 
    findViewById(2131296687).setOnClickListener(new IL1Iii(this));
    this.I丨L.I1I();
    findViewById(2131296338).setOnClickListener(new ILil(this));
    if (getResources().getInteger(2131361807) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    I11li1.I1LilL(I11li1.LIlLi((Context)this, 2131296624, bool));
    I11li1.il1L丨(I11li1.LIlLi((Context)this, 2131296623, false));
    I丨iL(getIntent());
    ll丨L1ii.I丨L((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
            68, 1, 66, 67, 89, 90, 80, 23, 105, 82, 
            94, 66 }, "7d6704"), true);
    ILil.I丨L.I丨L.I1I.Ilil().I丨L();
    I11li1.LIIIiI((Context)this);
    App.ILil().I1I();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4) {
      if (i1.iI().丨i1丨1i()) {
        l丨Li1LL.ILil(this, ILil.IL1Iii.IL1Iii(new byte[] { 
                -124, -7, -55, -127, -94, -108, -123, -38, -11, -126, 
                -81, -83, -118, -27, -4, -126, -82, -98 }, "bafd22"), ILil.IL1Iii.IL1Iii(new byte[] { -41, -71, -96, -124, -100, -125 }, "160c89"), new I丨L(this));
        return true;
      } 
      L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(100, "", ""));
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    I丨iL(paramIntent);
  }
  
  public void onResume() {
    super.onResume();
  }
  
  public class I1I implements Runnable {
    public final ScriptActivity IL1Iii;
    
    public I1I(ScriptActivity this$0) {}
    
    public void run() {
      if ((new File(ScriptActivity.Ilil(this.IL1Iii))).exists()) {
        i1 i1 = i1.iI();
        ScriptActivity scriptActivity = this.IL1Iii;
        i1.iI丨Li丨lI((Context)scriptActivity, ScriptActivity.Ilil(scriptActivity));
      } 
      this.IL1Iii.runOnUiThread(new IL1Iii(this));
    }
    
    public class IL1Iii implements Runnable {
      public final ScriptActivity.I1I IL1Iii;
      
      public IL1Iii(ScriptActivity.I1I this$0) {}
      
      public void run() {
        ScriptActivity.l丨Li1LL(this.IL1Iii.IL1Iii);
        ScriptActivity.ILil(this.IL1Iii.IL1Iii, true);
        ILil.Ilil.IL1Iii.IL1Iii.Ilil();
      }
    }
  }
  
  public class IL1Iii implements Runnable {
    public final ScriptActivity.I1I IL1Iii;
    
    public IL1Iii(ScriptActivity this$0) {}
    
    public void run() {
      ScriptActivity.l丨Li1LL(this.IL1Iii.IL1Iii);
      ScriptActivity.ILil(this.IL1Iii.IL1Iii, true);
      ILil.Ilil.IL1Iii.IL1Iii.Ilil();
    }
  }
  
  public class IL1Iii implements View.OnClickListener {
    public final ScriptActivity IL1Iii;
    
    public IL1Iii(ScriptActivity this$0) {}
    
    public void onClick(View param1View) {
      Intent intent = new Intent((Context)this.IL1Iii, SettingsActivity.class);
      this.IL1Iii.startActivity(intent);
    }
  }
  
  public class ILil implements View.OnClickListener {
    public final ScriptActivity IL1Iii;
    
    public ILil(ScriptActivity this$0) {}
    
    public void onClick(View param1View) {
      if (ScriptActivity.IL1Iii(this.IL1Iii)) {
        if (ScriptActivity.I1I(this.IL1Iii) != null)
          ScriptActivity.I1I(this.IL1Iii).saveCfg(); 
        ScriptActivity.I丨L(this.IL1Iii).l丨Li1LL();
        this.IL1Iii.moveTaskToBack(true);
      } else {
        I11li1.LlLl111l((Context)this.IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
                -46, -100, -90, -43, -22, -101, -35, -119, -87, -42, 
                -41, -103, -48, -68, -111, -40, -40, -123, -48, -104, 
                -67, -42, -19, -88 }, "5610e8"));
      } 
    }
  }
  
  public class I丨L implements l丨Li1LL.I1I {
    public final ScriptActivity IL1Iii;
    
    public I丨L(ScriptActivity this$0) {}
    
    public void IL1Iii(l丨Li1LL.I丨L param1I丨L) {
      if (param1I丨L == l丨Li1LL.I丨L.Ok) {
        L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(100, "", ""));
        this.IL1Iii.finish();
      } 
    }
  }
}
