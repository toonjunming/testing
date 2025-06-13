package com.main.app;

import ILil.I丨L.ILil.丨lL;
import ILil.I丨L.I丨L.I11li1;
import ILil.I丨L.I丨L.IL丨丨l;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AccRequestActivity extends Activity {
  public long IL1Iii = 0L;
  
  public boolean ILil = false;
  
  public static void ILil(Context paramContext, 丨lL param丨lL) {
    I11li1.lILIlI(new IL1Iii(paramContext, param丨lL));
  }
  
  public final void IL1Iii(Intent paramIntent) {
    long l = paramIntent.getLongExtra(ILil.IL1Iii.IL1Iii(new byte[] { 13, 85, 94, 85, 88, 6 }, "e4014c"), 0L);
    if (l != 0L) {
      this.IL1Iii = l;
      丨lL 丨lL = (丨lL)IL丨丨l.ILil().I1I(this.IL1Iii);
      if (丨lL != null)
        丨lL.ILil(); 
      I11li1.LI1((Context)this);
    } else {
      finish();
    } 
  }
  
  public void finish() {
    super.finish();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    IL1Iii(getIntent());
  }
  
  public void onDestroy() {
    super.onDestroy();
    if (this.IL1Iii != 0L) {
      丨lL 丨lL = (丨lL)IL丨丨l.ILil().I1I(this.IL1Iii);
      if (丨lL != null)
        丨lL.IL1Iii(); 
      IL丨丨l.ILil().I丨L(this.IL1Iii);
    } 
  }
  
  public void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    IL1Iii(paramIntent);
  }
  
  public void onPause() {
    super.onPause();
  }
  
  public void onResume() {
    super.onResume();
    if (!this.ILil) {
      this.ILil = true;
    } else {
      finish();
    } 
  }
  
  public static final class IL1Iii implements Runnable {
    public final Context IL1Iii;
    
    public final 丨lL ILil;
    
    public IL1Iii(Context param1Context, 丨lL param1丨lL) {}
    
    public void run() {
      Intent intent = (new Intent(this.IL1Iii, AccRequestActivity.class)).addFlags(268959744);
      long l = IL丨丨l.ILil().IL1Iii(this.ILil);
      intent.putExtra(ILil.IL1Iii.IL1Iii(new byte[] { 93, 89, 90, 87, 95, 81 }, "584334"), l);
      this.IL1Iii.startActivity(intent);
    }
  }
}
