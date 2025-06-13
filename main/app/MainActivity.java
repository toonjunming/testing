package com.main.app;

import ILil.I丨L.IL1Iii.ILil;
import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.ILil.i1;
import ILil.I丨L.I丨L.I11li1;
import ILil.I丨L.I丨L.I1I;
import ILil.I丨L.I丨L.ll丨L1ii;
import ILil.I丨L.I丨L.l丨Li1LL;
import Ilil.ILil.IL1Iii.L丨1丨1丨I;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.main.widget.CircleButton;

public class MainActivity extends BaseSettingActivity implements View.OnClickListener {
  public CircleButton 丨l丨;
  
  public ILil 丨丨丨1丨;
  
  public final void IL丨丨l() {
    Ll丨1(false);
    this.丨丨丨1丨 = new ILil(this);
    l丨Li1LL.IL1Iii(this, ILil.IL1Iii.IL1Iii(new byte[] { 
            -43, -92, -127, -46, -75, -99, -42, -69, -111, -36, 
            -123, -114, -41, -119, -100, -36, -105, -124, -44, -103, 
            -68, -45, -107, -70 }, "311483"));
    CircleButton circleButton = findViewById(2131296338);
    this.丨l丨 = circleButton;
    circleButton.set_text(ILil.IL1Iii.IL1Iii(new byte[] { 
            -46, -117, -79, -125, -108, -68, -33, -120, -95, -114, 
            -110, -69 }, "771f37"));
    this.丨l丨.setOnClickListener(this);
    ll丨L1ii.I丨L((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
            68, 80, 71, 67, 12, 11, 80, 70, 108, 82, 
            11, 19 }, "7537ee"), true);
    this.丨丨丨1丨.I1I();
  }
  
  public void finish() {
    super.finish();
    this.丨丨丨1丨.L丨1丨1丨I();
  }
  
  public void onClick(View paramView) {
    this.丨丨丨1丨.l丨Li1LL();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492894);
    I11li1.LIIIiI((Context)this);
    IL丨丨l();
    I1I.Ilil().I丨L();
    App.ILil().I1I();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4) {
      if (i1.iI().丨i1丨1i()) {
        l丨Li1LL.ILil(this, ILil.IL1Iii.IL1Iii(new byte[] { 
                -34, -84, -105, -35, -89, -97, -33, -113, -85, -34, 
                -86, -90, -48, -80, -94, -34, -85, -107 }, "848879"), ILil.IL1Iii.IL1Iii(new byte[] { -126, -66, -13, -41, -105, -118 }, "d1c030"), new IL1Iii(this));
        return true;
      } 
      L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(100, "", ""));
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public void onResume() {
    super.onResume();
  }
  
  public class IL1Iii implements l丨Li1LL.I1I {
    public final MainActivity IL1Iii;
    
    public IL1Iii(MainActivity this$0) {}
    
    public void IL1Iii(l丨Li1LL.I丨L param1I丨L) {
      if (param1I丨L == l丨Li1LL.I丨L.Ok) {
        L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(100, "", ""));
        this.IL1Iii.finish();
      } 
    }
  }
}
