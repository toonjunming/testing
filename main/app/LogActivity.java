package com.main.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.nx.baseui.X5WebView;
import java.io.File;

public class LogActivity extends AppCompatActivity {
  public X5WebView IL1Iii = null;
  
  public File ILil = null;
  
  public void I1I(File paramFile) {
    Intent intent = new Intent(ILil.IL1Iii.IL1Iii(new byte[] { 
            89, 92, 85, 19, 86, 11, 92, 28, 88, 15, 
            77, 7, 86, 70, 31, 0, 90, 22, 81, 93, 
            95, 79, 106, 39, 118, 118 }, "821a9b"));
    intent.setType(ILil.IL1Iii.IL1Iii(new byte[] { 77, 6, 27, 76, 73, 67, 85, 2, 10, 86 }, "9cc8f3"));
    intent.putExtra(ILil.IL1Iii.IL1Iii(new byte[] { 
            3, 93, 80, 75, 9, 92, 6, 29, 93, 87, 
            18, 80, 12, 71, 26, 92, 30, 65, 16, 82, 
            26, 109, 35, 109, 54 }, "b349f5"), paramFile.getAbsolutePath());
    startActivity(Intent.createChooser(intent, ILil.IL1Iii.IL1Iii(new byte[] { 
              -42, -72, -78, -47, -115, -100, -43, -90, -77, -47, 
              -116, -127 }, "304577")));
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492893);
    File file = new File(getIntent().getStringExtra(ILil.IL1Iii.IL1Iii(new byte[] { 4, 15, 15, 81 }, "bfc46f")));
    file.getName();
    this.ILil = file;
    ((TextView)findViewById(2131296386)).setText(file.getName());
    this.IL1Iii = findViewById(2131296482);
    findViewById(2131296378).setOnClickListener(new IL1Iii(this));
    findViewById(2131296593).setOnClickListener(new ILil(this));
    (new Thread(new I1I(this, Uri.fromFile(file).toString()))).start();
  }
  
  public class I1I implements Runnable {
    public final String IL1Iii;
    
    public final LogActivity ILil;
    
    public I1I(LogActivity this$0, String param1String) {}
    
    public void run() {
      LogActivity.ILil(this.ILil).post(new IL1Iii(this));
    }
    
    public class IL1Iii implements Runnable {
      public final LogActivity.I1I IL1Iii;
      
      public IL1Iii(LogActivity.I1I this$0) {}
      
      public void run() {
        LogActivity.ILil(this.IL1Iii.ILil).loadUrl(this.IL1Iii.IL1Iii);
      }
    }
  }
  
  public class IL1Iii implements Runnable {
    public final LogActivity.I1I IL1Iii;
    
    public IL1Iii(LogActivity this$0) {}
    
    public void run() {
      LogActivity.ILil(this.IL1Iii.ILil).loadUrl(this.IL1Iii.IL1Iii);
    }
  }
  
  public class IL1Iii implements View.OnClickListener {
    public final LogActivity IL1Iii;
    
    public IL1Iii(LogActivity this$0) {}
    
    public void onClick(View param1View) {
      if (LogActivity.IL1Iii(this.IL1Iii).exists()) {
        LogActivity.IL1Iii(this.IL1Iii).delete();
        this.IL1Iii.finish();
      } 
    }
  }
  
  public class ILil implements View.OnClickListener {
    public final LogActivity IL1Iii;
    
    public ILil(LogActivity this$0) {}
    
    public void onClick(View param1View) {
      LogActivity logActivity = this.IL1Iii;
      logActivity.I1I(LogActivity.IL1Iii(logActivity));
    }
  }
}
