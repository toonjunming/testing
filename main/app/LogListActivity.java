package com.main.app;

import ILil.I丨L.I丨L.I11li1;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LogListActivity extends AppCompatActivity {
  public static final String L丨1丨1丨I = ILil.IL1Iii.IL1Iii(new byte[] { 103, 89, 20, 69, 100, 20, 81, 80, 21 }, "46f14f");
  
  public static final String 丨il = ILil.IL1Iii.IL1Iii(new byte[] { 69, 86, 19, 71, 123, 64, 82, 92, 19 }, "69a342");
  
  public TextView I1I;
  
  public ListView IL1Iii;
  
  public List<String> ILil = new ArrayList<String>();
  
  public FileObserver Ilil;
  
  public View I丨L = null;
  
  public BaseAdapter I丨iL = new I1I(this);
  
  public SharedPreferences iI丨LLL1;
  
  public Spinner l丨Li1LL;
  
  public final void ILL(String paramString) {
    runOnUiThread(new Ll丨1(this, paramString));
  }
  
  public final void IL丨丨l(int paramInt) {
    if (paramInt == 0) {
      Collections.sort(this.ILil, new L丨1丨1丨I(this));
    } else {
      Collections.sort(this.ILil, new 丨il(this));
    } 
    this.I丨iL.notifyDataSetChanged();
  }
  
  public final void Lil() {
    ILL iLL = new ILL(this, I11li1.Liil1L1l((Context)this).getAbsolutePath());
    this.Ilil = iLL;
    if (iLL != null)
      try {
        iLL.startWatching();
      } catch (Exception exception) {
        Log.e(ILil.IL1Iii.IL1Iii(new byte[] { 
                42, 95, 2, 121, 91, 18, 18, 113, 6, 65, 
                91, 23, 15, 68, 28 }, "f0e52a"), ILil.IL1Iii.IL1Iii(new byte[] { 
                117, 11, 84, 80, 44, 90, 64, 7, 74, 67, 
                6, 74, 19, 17, 76, 84, 17, 76, 19, 7, 
                74, 71, 12, 74 }, "3b85c8"), exception);
      }  
  }
  
  public final void Ll丨1() {
    this.I丨L.setVisibility(0);
    (new Thread(new ILil(this))).start();
  }
  
  public void finish() {
    super.finish();
    try {
      FileObserver fileObserver = this.Ilil;
      if (fileObserver != null)
        fileObserver.stopWatching(); 
    } catch (Exception exception) {
      Log.e(ILil.IL1Iii.IL1Iii(new byte[] { 
              47, 93, 81, 47, 88, 67, 23, 115, 85, 23, 
              88, 70, 10, 70, 79 }, "c26c10"), ILil.IL1Iii.IL1Iii(new byte[] { 
              126, 93, 91, 84, 121, 85, 75, 81, 69, 71, 
              83, 69, 24, 71, 67, 94, 70, 23, 93, 70, 
              69, 94, 68 }, "847167"), exception);
    } 
  }
  
  public final void lIi丨I(String paramString) {
    runOnUiThread(new IL1Iii(this, paramString));
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492928);
    this.IL1Iii = findViewById(2131296466);
    this.I丨L = findViewById(2131296398);
    this.I1I = findViewById(2131296379);
    this.l丨Li1LL = findViewById(2131296606);
    this.iI丨LLL1 = getSharedPreferences(L丨1丨1丨I, 0);
    ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource((Context)this, 2130903040, 17367048);
    arrayAdapter.setDropDownViewResource(17367049);
    this.l丨Li1LL.setAdapter((SpinnerAdapter)arrayAdapter);
    int i = this.iI丨LLL1.getInt(丨il, 0);
    this.l丨Li1LL.setSelection(i);
    this.l丨Li1LL.setOnItemSelectedListener(new I丨L(this));
    this.I1I.setOnClickListener(new l丨Li1LL(this));
    this.I丨L.setVisibility(0);
    (new Thread(new iI丨LLL1(this, i))).start();
    this.IL1Iii.setOnItemClickListener(new I丨iL(this));
  }
  
  public class I1I extends BaseAdapter {
    public final LogListActivity IL1Iii;
    
    public I1I(LogListActivity this$0) {}
    
    public int getCount() {
      return LogListActivity.IL1Iii(this.IL1Iii).size();
    }
    
    public Object getItem(int param1Int) {
      return LogListActivity.IL1Iii(this.IL1Iii).get(param1Int);
    }
    
    public long getItemId(int param1Int) {
      return 0L;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = param1View;
      if (param1View == null) {
        view = View.inflate((Context)this.IL1Iii, 2131492926, null);
        LogListActivity.lIi丨I lIi丨I1 = new LogListActivity.lIi丨I(null);
        lIi丨I1.IL1Iii = (TextView)view.findViewById(2131296516);
        view.setTag(lIi丨I1);
      } 
      LogListActivity.lIi丨I lIi丨I = (LogListActivity.lIi丨I)view.getTag();
      File file = new File(LogListActivity.IL1Iii(this.IL1Iii).get(param1Int));
      lIi丨I.IL1Iii.setText(file.getName());
      return view;
    }
  }
  
  public class IL1Iii implements Runnable {
    public final String IL1Iii;
    
    public final LogListActivity ILil;
    
    public IL1Iii(LogListActivity this$0, String param1String) {}
    
    public void run() {
      LogListActivity.IL1Iii(this.ILil).remove(this.IL1Iii);
      LogListActivity.iI丨LLL1(this.ILil).notifyDataSetChanged();
    }
  }
  
  public class ILL extends FileObserver {
    public final LogListActivity IL1Iii;
    
    public ILL(LogListActivity this$0, String param1String) {
      super(param1String);
    }
    
    public void onEvent(int param1Int, @Nullable String param1String) {
      if (param1String == null)
        return; 
      File file = new File(I11li1.Liil1L1l((Context)this.IL1Iii), param1String);
      if (param1Int != 256) {
        if (param1Int == 512)
          LogListActivity.ILil(this.IL1Iii, file.getAbsolutePath()); 
      } else {
        LogListActivity.丨il(this.IL1Iii, file.getAbsolutePath());
      } 
    }
  }
  
  public class ILil implements Runnable {
    public final LogListActivity IL1Iii;
    
    public ILil(LogListActivity this$0) {}
    
    public void run() {
      for (File file : I11li1.Liil1L1l((Context)this.IL1Iii).listFiles()) {
        if (file.exists())
          file.delete(); 
      } 
      this.IL1Iii.runOnUiThread(new IL1Iii(this));
    }
    
    public class IL1Iii implements Runnable {
      public final LogListActivity.ILil IL1Iii;
      
      public IL1Iii(LogListActivity.ILil this$0) {}
      
      public void run() {
        LogListActivity.IL1Iii(this.IL1Iii.IL1Iii).clear();
        LogListActivity.l丨Li1LL(this.IL1Iii.IL1Iii).setVisibility(8);
        LogListActivity.iI丨LLL1(this.IL1Iii.IL1Iii).notifyDataSetChanged();
      }
    }
  }
  
  public class IL1Iii implements Runnable {
    public final LogListActivity.ILil IL1Iii;
    
    public IL1Iii(LogListActivity this$0) {}
    
    public void run() {
      LogListActivity.IL1Iii(this.IL1Iii.IL1Iii).clear();
      LogListActivity.l丨Li1LL(this.IL1Iii.IL1Iii).setVisibility(8);
      LogListActivity.iI丨LLL1(this.IL1Iii.IL1Iii).notifyDataSetChanged();
    }
  }
  
  public class I丨L implements AdapterView.OnItemSelectedListener {
    public final LogListActivity IL1Iii;
    
    public I丨L(LogListActivity this$0) {}
    
    public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      SharedPreferences.Editor editor = LogListActivity.I1I(this.IL1Iii).edit();
      editor.putInt(ILil.IL1Iii.IL1Iii(new byte[] { 65, 86, 75, 76, 125, 22, 86, 92, 75 }, "29982d"), param1Int);
      editor.apply();
      LogListActivity.I丨L(this.IL1Iii, param1Int);
    }
    
    public void onNothingSelected(AdapterView<?> param1AdapterView) {}
  }
  
  public class I丨iL implements AdapterView.OnItemClickListener {
    public final LogListActivity IL1Iii;
    
    public I丨iL(LogListActivity this$0) {}
    
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      String str = LogListActivity.IL1Iii(this.IL1Iii).get(param1Int);
      Intent intent = new Intent((Context)this.IL1Iii, LogActivity.class);
      intent.putExtra(ILil.IL1Iii.IL1Iii(new byte[] { 94, 81, 9, 93 }, "88e8aa"), str);
      this.IL1Iii.startActivity(intent);
    }
  }
  
  public class Ll丨1 implements Runnable {
    public final String IL1Iii;
    
    public final LogListActivity ILil;
    
    public Ll丨1(LogListActivity this$0, String param1String) {}
    
    public void run() {
      LogListActivity.IL1Iii(this.ILil).add(this.IL1Iii);
      LogListActivity.iI丨LLL1(this.ILil).notifyDataSetChanged();
    }
  }
  
  public class L丨1丨1丨I implements Comparator<String> {
    public L丨1丨1丨I(LogListActivity this$0) {}
    
    public int IL1Iii(String param1String1, String param1String2) {
      return Long.compare((new File(param1String1)).lastModified(), (new File(param1String2)).lastModified());
    }
  }
  
  public class iI丨LLL1 implements Runnable {
    public final int IL1Iii;
    
    public final LogListActivity ILil;
    
    public iI丨LLL1(LogListActivity this$0, int param1Int) {}
    
    public void run() {
      file = I11li1.Liil1L1l((Context)this.ILil);
      if (file != null)
        for (File file : file.listFiles())
          LogListActivity.IL1Iii(this.ILil).add(file.getAbsolutePath());  
      this.ILil.runOnUiThread(new IL1Iii(this));
    }
    
    public class IL1Iii implements Runnable {
      public final LogListActivity.iI丨LLL1 IL1Iii;
      
      public IL1Iii(LogListActivity.iI丨LLL1 this$0) {}
      
      public void run() {
        LogListActivity.iI丨LLL1 iI丨LLL11 = this.IL1Iii;
        LogListActivity.I丨L(iI丨LLL11.ILil, iI丨LLL11.IL1Iii);
        LogListActivity.l丨Li1LL(this.IL1Iii.ILil).setVisibility(8);
        LogListActivity.I丨iL(this.IL1Iii.ILil).setAdapter((ListAdapter)LogListActivity.iI丨LLL1(this.IL1Iii.ILil));
        LogListActivity.L丨1丨1丨I(this.IL1Iii.ILil);
      }
    }
  }
  
  public class IL1Iii implements Runnable {
    public final LogListActivity.iI丨LLL1 IL1Iii;
    
    public IL1Iii(LogListActivity this$0) {}
    
    public void run() {
      LogListActivity.iI丨LLL1 iI丨LLL11 = this.IL1Iii;
      LogListActivity.I丨L(iI丨LLL11.ILil, iI丨LLL11.IL1Iii);
      LogListActivity.l丨Li1LL(this.IL1Iii.ILil).setVisibility(8);
      LogListActivity.I丨iL(this.IL1Iii.ILil).setAdapter((ListAdapter)LogListActivity.iI丨LLL1(this.IL1Iii.ILil));
      LogListActivity.L丨1丨1丨I(this.IL1Iii.ILil);
    }
  }
  
  public class lIi丨I {
    public TextView IL1Iii;
    
    public lIi丨I(LogListActivity this$0) {}
  }
  
  public class l丨Li1LL implements View.OnClickListener {
    public final LogListActivity IL1Iii;
    
    public l丨Li1LL(LogListActivity this$0) {}
    
    public void onClick(View param1View) {
      LogListActivity.Ilil(this.IL1Iii);
    }
  }
  
  public class 丨il implements Comparator<String> {
    public 丨il(LogListActivity this$0) {}
    
    public int IL1Iii(String param1String1, String param1String2) {
      long l = (new File(param1String1)).lastModified();
      return Long.compare((new File(param1String2)).lastModified(), l);
    }
  }
}
