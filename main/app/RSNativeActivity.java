package com.main.app;

import ILil.IL1Iii;
import ILil.I丨L.ILil.i1;
import ILil.I丨L.ILil.丨lL;
import ILil.I丨L.I丨L.IL丨丨l;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;

public class RSNativeActivity extends Activity {
  public long IL1Iii = 0L;
  
  public static void ILil(Context paramContext, 丨lL param丨lL) {
    Intent intent = (new Intent(paramContext, RSNativeActivity.class)).addFlags(268959744);
    long l = IL丨丨l.ILil().IL1Iii(param丨lL);
    intent.putExtra(IL1Iii.IL1Iii(new byte[] { 10, 88, 93, 6, 89, 84 }, "b93b51"), l);
    paramContext.startActivity(intent);
  }
  
  public final void IL1Iii(Intent paramIntent) {
    this.IL1Iii = paramIntent.getLongExtra(IL1Iii.IL1Iii(new byte[] { 9, 5, 95, 85, 13, 93 }, "ad11a8"), 0L);
    丨lL 丨lL = (丨lL)IL丨丨l.ILil().I1I(this.IL1Iii);
    if (丨lL != null)
      丨lL.ILil(); 
    startActivityForResult(((MediaProjectionManager)getSystemService(IL1Iii.IL1Iii(new byte[] { 
              91, 3, 7, 11, 85, 58, 70, 20, 12, 8, 
              81, 6, 66, 15, 12, 12 }, "6fcb4e"))).createScreenCaptureIntent(), 256);
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 256 && paramInt2 == -1)
      i1.iI().LII(paramIntent); 
    丨lL 丨lL = (丨lL)IL丨丨l.ILil().I1I(this.IL1Iii);
    if (丨lL != null) {
      丨lL.IL1Iii();
      IL丨丨l.ILil().I丨L(this.IL1Iii);
    } 
    finish();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    IL1Iii(getIntent());
  }
  
  public void onDestroy() {
    super.onDestroy();
  }
  
  public void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    IL1Iii(paramIntent);
  }
}
