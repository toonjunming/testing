package com.main.app;

import ILil.I丨L.ILil.LI丨丨l丨l;
import ILil.I丨L.ILil.LLL;
import ILil.I丨L.ILil.i1;
import ILil.I丨L.ILil.iIlLiL;
import ILil.I丨L.ILil.丨l丨;
import ILil.I丨L.ILil.丨丨丨1丨;
import ILil.I丨L.I丨L.I11li1;
import Ilil.ILil.IL1Iii.iIi1;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import org.greenrobot.eventbus.ThreadMode;

public class BaseSettingActivity extends AppCompatActivity {
  public Button I11L;
  
  public Button I11li1;
  
  public RelativeLayout I1I;
  
  public Button IL1Iii;
  
  public CheckBox ILL;
  
  public LinearLayout ILil;
  
  public EditText IL丨丨l;
  
  public RadioGroup Ilil;
  
  public LinearLayout I丨L;
  
  public CheckBox I丨iL;
  
  public EditText Lil;
  
  public TextView LlLI1;
  
  public CheckBox Ll丨1;
  
  public CheckBox L丨1丨1丨I;
  
  public boolean iIi1 = false;
  
  public View iIlLiL;
  
  public CheckBox iI丨LLL1;
  
  public EditText lIi丨I;
  
  public boolean lI丨lii = false;
  
  public Button llliI;
  
  public Handler ll丨L1ii;
  
  public CheckBox l丨Li1LL;
  
  public CheckBox 丨il;
  
  public Button 丨lL;
  
  public final void ILL() {
    try {
      if (((LLL)ILil.I丨L.ILil.Lil.I丨L().Ilil()).I11L()) {
        this.丨il.setChecked(true);
      } else {
        this.丨il.setChecked(false);
      } 
    } catch (Exception exception) {}
  }
  
  public final CheckBox I丨iL(int paramInt, boolean paramBoolean) {
    CheckBox checkBox = findViewById(paramInt);
    if (checkBox != null) {
      checkBox.setChecked(I11li1.LIlLi((Context)this, paramInt, paramBoolean));
      checkBox.setOnCheckedChangeListener(new I丨iL(this, paramInt));
    } 
    return checkBox;
  }
  
  public void Ll丨1(boolean paramBoolean) {
    this.ll丨L1ii = new Handler();
    this.ILil = findViewById(2131296400);
    this.I1I = findViewById(2131296571);
    this.I丨L = findViewById(2131296475);
    if (!I11li1.LIIl丨丨L((Context)this) || paramBoolean)
      this.I丨L.setVisibility(8); 
    if (I11li1.ii丨1((Context)this) || I11li1.丨iI丨Ii丨1((Context)this) || I11li1.丨IiIi((Context)this)) {
      this.ILil.setVisibility(8);
      if (I11li1.ii丨1((Context)this)) {
        I11li1.丨丨1((Context)this, ILil.I丨L.ILil.Lil.IL1Iii.ROOT);
      } else if (I11li1.丨iI丨Ii丨1((Context)this)) {
        I11li1.丨丨1((Context)this, ILil.I丨L.ILil.Lil.IL1Iii.ACC);
      } else {
        I11li1.丨丨1((Context)this, ILil.I丨L.ILil.Lil.IL1Iii.HANDLE);
      } 
    } else {
      this.ILil.setVisibility(0);
    } 
    this.Ilil = findViewById(2131296401);
    if (I11li1.丨IiIi((Context)this)) {
      this.I1I.setVisibility(8);
    } else if (I11li1.ii丨1((Context)this)) {
      if (I11li1.丨L((Context)this)) {
        this.I1I.setVisibility(8);
        I11li1.丨丨I11((Context)this, 2131296632, true);
      } 
    } else if (I11li1.丨iI丨Ii丨1((Context)this)) {
      this.I1I.setVisibility(8);
    } 
    this.iIlLiL = findViewById(2131296545);
    if (I11li1.LIIl丨丨L((Context)this)) {
      this.iIlLiL.setVisibility(0);
      this.I11li1 = (Button)this.iIlLiL.findViewById(2131296473);
      this.丨lL = (Button)this.iIlLiL.findViewById(2131296530);
      this.I11li1.setOnClickListener(new L丨1丨1丨I(this));
      this.丨lL.setOnClickListener(new 丨il(this));
      this.Lil = (EditText)this.iIlLiL.findViewById(2131296325);
      this.I11L = (Button)this.iIlLiL.findViewById(2131296474);
      this.llliI = (Button)this.iIlLiL.findViewById(2131296531);
      this.I11L.setOnClickListener(new ILL(this));
      this.llliI.setOnClickListener(new Ll丨1(this));
    } else {
      this.iIlLiL.setVisibility(8);
    } 
    ((TextView)findViewById(2131296702)).setText(String.format(ILil.IL1Iii.IL1Iii(new byte[] { 68, 70 }, "a59e0b"), new Object[] { I11li1.丨i((Context)this) }));
    this.IL1Iii = findViewById(2131296364);
    this.I丨iL = findViewById(2131296622);
    this.L丨1丨1丨I = findViewById(2131296630);
    RadioButton radioButton = findViewById(2131296631);
    radioButton = findViewById(2131296637);
    radioButton = findViewById(2131296626);
    this.丨il = findViewById(2131296629);
    I丨iL(2131296625, I11li1.LLL((Context)this));
    I丨iL(2131296636, I11li1.l丨丨i11((Context)this));
    this.Ll丨1 = I丨iL(2131296634, I11li1.l11((Context)this));
    iI丨LLL1(2131296635);
    this.l丨Li1LL = findViewById(2131296627);
    this.LlLI1 = findViewById(2131296478);
    I丨iL(2131296632, I11li1.iI1i丨I((Context)this));
    I丨iL(2131296698, I11li1.ILL丨Ii((Context)this));
    CheckBox checkBox = findViewById(2131296700);
    this.ILL = checkBox;
    checkBox.setChecked(I11li1.I1L丨11L((Context)this));
    if (!I11li1.LIIl丨丨L((Context)this)) {
      this.ILL.setEnabled(false);
      this.Ll丨1.setEnabled(false);
    } 
    if (getResources().getInteger(2131361807) == 1) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    I丨iL(2131296624, paramBoolean);
    I丨iL(2131296623, false);
    this.iI丨LLL1 = I丨iL(2131296628, false);
    I丨iL(2131296633, true);
    this.IL丨丨l = findViewById(2131296448);
    EditText editText = findViewById(2131296546);
    this.lIi丨I = editText;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("");
    stringBuilder1.append(I11li1.lI1I1((Context)this, 2131296546, 10192));
    editText.setText(stringBuilder1.toString());
    TextView textView = findViewById(2131296476);
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("");
    stringBuilder2.append(I11li1.l丨liiI1((Context)this));
    textView.setText(stringBuilder2.toString());
    String str1 = I11li1.li((Context)this, 2131296448, ILil.IL1Iii.IL1Iii(new byte[] { 83, 24, 6, 23, 2, 74, 83 }, "c6692d"));
    if (!TextUtils.isEmpty(str1))
      this.IL丨丨l.setText(str1); 
    String str2 = I11li1.LI11();
    str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = ILil.IL1Iii.IL1Iii(new byte[] { 4, 31, 9, 25, 5, 27, 4 }, "419755"); 
    this.LlLI1.setText(str1);
    ILil.I丨L.ILil.Lil.IL1Iii iL1Iii = I11li1.丨i1丨1i((Context)this);
    if (iL1Iii == ILil.I丨L.ILil.Lil.IL1Iii.ROOT) {
      this.Ilil.check(2131296631);
    } else {
      ILil.I丨L.ILil.Lil.IL1Iii iL1Iii1 = ILil.I丨L.ILil.Lil.IL1Iii.ACC;
      if (iL1Iii == iL1Iii1) {
        this.Ilil.check(2131296637);
        I11li1.丨丨1((Context)this, iL1Iii1);
      } else {
        this.Ilil.check(2131296626);
      } 
    } 
    this.ILL.setOnCheckedChangeListener(new lIi丨I(this));
    this.Ilil.setOnCheckedChangeListener(new IL丨丨l(this));
    this.l丨Li1LL.setOnCheckedChangeListener(new Lil(this));
    if (i1.iI().I丨())
      this.L丨1丨1丨I.setChecked(true); 
    this.L丨1丨1丨I.setOnCheckedChangeListener(new LlLI1(this));
    this.I丨iL.setOnCheckedChangeListener(new IL1Iii(this));
    this.iI丨LLL1.setOnCheckedChangeListener(new ILil(this));
    this.丨il.setOnCheckedChangeListener(new I1I(this));
    this.IL1Iii.setOnClickListener(new I丨L(this));
    I11li1.I1LilL(I11li1.LIlLi((Context)this, 2131296624, paramBoolean));
    I11li1.il1L丨(I11li1.LIlLi((Context)this, 2131296623, false));
    Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().Lil(this);
    ILL();
  }
  
  public final boolean L丨1丨1丨I(int paramInt) {
    File file = new File(ILil.IL1Iii.IL1Iii(new byte[] { 
            75, 18, 84, 82, 81, 65, 0, 78, 116, 94, 
            71, 93, 8, 14, 81, 85, 31, 95, 22, 17, 
            92, 68, 87, 90, 10, 79, 81, 65, 91 }, "da0103"));
    String str = ILil.IL1Iii.IL1Iii(new byte[] { 40, 60 }, "fd8b26");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 64, 89, 66, 14, 13 }, "086f74"));
    stringBuilder.append(file.getAbsolutePath());
    Log.i(str, stringBuilder.toString());
    if (file.exists()) {
      str = file.getAbsolutePath();
      Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(paramInt, ILil.IL1Iii.IL1Iii(new byte[] { 22, 20, 17, 86 }, "bfd34f"), str));
      return true;
    } 
    I11li1.LlLl111l((Context)this, getResources().getString(2131689583));
    return false;
  }
  
  public void finish() {
    super.finish();
    Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().ll丨L1ii(this);
  }
  
  public final CheckBox iI丨LLL1(int paramInt) {
    return I丨iL(paramInt, false);
  }
  
  public final void lIi丨I(View paramView) {
    if (paramView == this.IL1Iii) {
      String str2 = this.lIi丨I.getText().toString();
      String str1 = this.IL丨丨l.getText().toString();
      if (!TextUtils.isEmpty(str2)) {
        I11li1.LI丨I1((Context)this, 2131296546, Integer.parseInt(str2));
        if (!TextUtils.isEmpty(str1)) {
          I11li1.I1i((Context)this, 2131296448, str1);
          this.IL1Iii.setEnabled(false);
          (new Thread(new l丨Li1LL(this, str1))).start();
        } else {
          I11li1.LlLl111l((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
                  -41, Byte.MIN_VALUE, -114, -124, -84, -34, -42, -92, -123, -121, 
                  -83, -28, -43, -118, -108, -124, -84, -19, -37, -106, 
                  -117, -123, -115, -54 }, "385b0d"));
          return;
        } 
      } else {
        I11li1.LlLl111l((Context)this, ILil.IL1Iii.IL1Iii(new byte[] { 
                -47, -97, -97, -46, -18, -109, -48, -122, -111, -47, 
                -3, -71, -34, -102, -114, -48, -36, -98 }, "6407a0"));
      } 
    } 
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
  }
  
  @iIi1(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LI丨丨l丨l paramLI丨丨l丨l) {
    String str;
    if (paramLI丨丨l丨l.I1I() == 103) {
      str = paramLI丨丨l丨l.IL1Iii();
      if (str != null)
        if (str.equals(ILil.IL1Iii.IL1Iii(new byte[] { 85 }, "db2381"))) {
          this.I丨iL.setChecked(true);
        } else if (str.equals(ILil.IL1Iii.IL1Iii(new byte[] { 83 }, "cf7a5d"))) {
          this.I丨iL.setChecked(false);
        }  
    } else if (str.I1I() == 104) {
      str = str.IL1Iii();
      if (str != null)
        if (str.equals(ILil.IL1Iii.IL1Iii(new byte[] { 8 }, "93676f"))) {
          this.l丨Li1LL.setChecked(true);
        } else if (str.equals(ILil.IL1Iii.IL1Iii(new byte[] { 84 }, "de78ce"))) {
          this.l丨Li1LL.setChecked(false);
        }  
    } else if (str.I1I() == 206) {
      str = str.IL1Iii();
      if (str != null)
        try {
          boolean bool = Boolean.valueOf(str).booleanValue();
          CheckBox checkBox = this.ILL;
          if (checkBox != null)
            checkBox.setChecked(bool); 
          I11li1.LI1丨l((Context)this, bool);
        } catch (Exception exception) {} 
    } 
  }
  
  public void onResume() {
    super.onResume();
    if (I11li1.Ilil()) {
      this.I丨iL.setChecked(true);
    } else {
      this.I丨iL.setChecked(false);
    } 
    if (i1.iI().I丨()) {
      this.L丨1丨1丨I.setChecked(true);
    } else {
      this.L丨1丨1丨I.setChecked(false);
    } 
    if (ImeInput.I1I() != null) {
      this.l丨Li1LL.setChecked(true);
    } else {
      this.l丨Li1LL.setChecked(false);
    } 
    if (this.lI丨lii) {
      this.lI丨lii = false;
      this.ll丨L1ii.postDelayed(new iI丨LLL1(this), 1000L);
    } 
    ILL();
  }
  
  public final boolean 丨il(int paramInt) {
    String str = this.Lil.getText().toString();
    File file = new File(I11li1.L丨1l(I11li1.l1IIi1丨(), str));
    if (file.exists()) {
      String str1 = file.getAbsolutePath();
      Ilil.ILil.IL1Iii.L丨1丨1丨I.I1I().ILL(new LI丨丨l丨l(paramInt, ILil.IL1Iii.IL1Iii(new byte[] { 76, 66, 22, 84 }, "80c1ef"), str1));
      return true;
    } 
    I11li1.LlLl111l((Context)this, getResources().getString(2131689583));
    return false;
  }
  
  public class I1I implements CompoundButton.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public I1I(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (I11li1.丨i1丨1i((Context)this.IL1Iii) == ILil.I丨L.ILil.Lil.IL1Iii.ROOT && this.IL1Iii.丨il.isPressed()) {
        if (BaseSettingActivity.I1I(this.IL1Iii))
          return; 
        BaseSettingActivity.I丨L(this.IL1Iii, true);
        ILil.I丨L.ILil.Lil.I丨L().Ilil().LlLI1((Context)this.IL1Iii, new IL1Iii(this));
      } 
    }
    
    public class IL1Iii implements 丨丨丨1丨 {
      public final BaseSettingActivity.I1I IL1Iii;
      
      public IL1Iii(BaseSettingActivity.I1I this$0) {}
      
      public void IL1Iii() {
        I11li1.LlLl111l((Context)this.IL1Iii.IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
                Byte.MIN_VALUE, -34, -92, -46, -11, -21, Byte.MIN_VALUE, -14, -98, -47, 
                -20, -51, Byte.MIN_VALUE, -58, Byte.MIN_VALUE, -36, -46, -64, 73, -118, 
                -98, -125, -125, -32, -19, -123, -112, -102, -126, -38, 
                -8, -121, -115, -76, -125, -11, -54, 16, 94, 91, 
                18, -127, -33, -28 }, "eb14fe"));
        BaseSettingActivity.I丨L(this.IL1Iii.IL1Iii, false);
      }
      
      public void ILil() {
        I11li1.LlLl111l((Context)this.IL1Iii.IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
                98, 46, 119, 53, -35, -38, -91, -121, -85, -17, 
                -35, -10, -97, -124, -78, -55, -35, -56, -68, -121, 
                -80, -15 }, "0a8a8f"));
        BaseSettingActivity.Ilil(this.IL1Iii.IL1Iii);
        BaseSettingActivity.I丨L(this.IL1Iii.IL1Iii, false);
      }
    }
  }
  
  public class IL1Iii implements 丨丨丨1丨 {
    public final BaseSettingActivity.I1I IL1Iii;
    
    public IL1Iii(BaseSettingActivity this$0) {}
    
    public void IL1Iii() {
      I11li1.LlLl111l((Context)this.IL1Iii.IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
              Byte.MIN_VALUE, -34, -92, -46, -11, -21, Byte.MIN_VALUE, -14, -98, -47, 
              -20, -51, Byte.MIN_VALUE, -58, Byte.MIN_VALUE, -36, -46, -64, 73, -118, 
              -98, -125, -125, -32, -19, -123, -112, -102, -126, -38, 
              -8, -121, -115, -76, -125, -11, -54, 16, 94, 91, 
              18, -127, -33, -28 }, "eb14fe"));
      BaseSettingActivity.I丨L(this.IL1Iii.IL1Iii, false);
    }
    
    public void ILil() {
      I11li1.LlLl111l((Context)this.IL1Iii.IL1Iii, ILil.IL1Iii.IL1Iii(new byte[] { 
              98, 46, 119, 53, -35, -38, -91, -121, -85, -17, 
              -35, -10, -97, -124, -78, -55, -35, -56, -68, -121, 
              -80, -15 }, "0a8a8f"));
      BaseSettingActivity.Ilil(this.IL1Iii.IL1Iii);
      BaseSettingActivity.I丨L(this.IL1Iii.IL1Iii, false);
    }
  }
  
  public class IL1Iii implements CompoundButton.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public IL1Iii(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (this.IL1Iii.I丨iL.isPressed())
        I11li1.LI1((Context)this.IL1Iii); 
    }
  }
  
  public class ILL implements View.OnClickListener {
    public final BaseSettingActivity IL1Iii;
    
    public ILL(BaseSettingActivity this$0) {}
    
    public void onClick(View param1View) {
      BaseSettingActivity.ILil(this.IL1Iii, 200);
    }
  }
  
  public class ILil implements CompoundButton.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public ILil(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (this.IL1Iii.iI丨LLL1.isPressed()) {
        this.IL1Iii.iI丨LLL1.setChecked(param1Boolean ^ true);
        I11li1.IlIL丨il((Context)this.IL1Iii);
      } 
    }
  }
  
  public class IL丨丨l implements RadioGroup.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public IL丨丨l(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
      if (param1Int == 2131296631) {
        I11li1.丨丨1((Context)this.IL1Iii, ILil.I丨L.ILil.Lil.IL1Iii.ROOT);
      } else if (param1Int == 2131296637) {
        I11li1.丨丨1((Context)this.IL1Iii, ILil.I丨L.ILil.Lil.IL1Iii.ACC);
      } else if (param1Int == 2131296626) {
        I11li1.丨丨1((Context)this.IL1Iii, ILil.I丨L.ILil.Lil.IL1Iii.HANDLE);
      } 
    }
  }
  
  public class I丨L implements View.OnClickListener {
    public final BaseSettingActivity IL1Iii;
    
    public I丨L(BaseSettingActivity this$0) {}
    
    public void onClick(View param1View) {
      BaseSettingActivity.l丨Li1LL(this.IL1Iii, param1View);
    }
  }
  
  public class I丨iL implements CompoundButton.OnCheckedChangeListener {
    public final int IL1Iii;
    
    public final BaseSettingActivity ILil;
    
    public I丨iL(BaseSettingActivity this$0, int param1Int) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      int i = this.IL1Iii;
      if (i == 2131296624) {
        I11li1.I1LilL(param1Boolean);
      } else if (i == 2131296623) {
        I11li1.il1L丨(param1Boolean);
      } else {
        I11li1.丨丨I11((Context)this.ILil, i, param1Boolean);
      } 
    }
  }
  
  public class Lil implements CompoundButton.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public Lil(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (this.IL1Iii.l丨Li1LL.isPressed())
        if (I11li1.i丨1lL1l((Context)this.IL1Iii)) {
          this.IL1Iii.l丨Li1LL.setChecked(param1Boolean ^ true);
          ((InputMethodManager)this.IL1Iii.getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
                  92, 87, 18, 70, 68, 107, 88, 92, 22, 91, 
                  95, 80 }, "59b304"))).showInputMethodPicker();
        } else {
          BaseSettingActivity baseSettingActivity = this.IL1Iii;
          baseSettingActivity.lI丨lii = true;
          I11li1.丨丨Ii丨L((Context)baseSettingActivity);
        }  
    }
  }
  
  public class LlLI1 implements CompoundButton.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public LlLI1(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (this.IL1Iii.L丨1丨1丨I.isPressed())
        ((iIlLiL)ILil.I丨L.ILil.Lil.I丨L().ILil()).iIi1((Context)this.IL1Iii, false); 
    }
  }
  
  public class Ll丨1 implements View.OnClickListener {
    public final BaseSettingActivity IL1Iii;
    
    public Ll丨1(BaseSettingActivity this$0) {}
    
    public void onClick(View param1View) {
      BaseSettingActivity.ILil(this.IL1Iii, 201);
    }
  }
  
  public class L丨1丨1丨I implements View.OnClickListener {
    public final BaseSettingActivity IL1Iii;
    
    public L丨1丨1丨I(BaseSettingActivity this$0) {}
    
    public void onClick(View param1View) {
      BaseSettingActivity.IL1Iii(this.IL1Iii, 200);
    }
  }
  
  public class iI丨LLL1 implements Runnable {
    public final BaseSettingActivity IL1Iii;
    
    public iI丨LLL1(BaseSettingActivity this$0) {}
    
    public void run() {
      if (I11li1.i丨1lL1l((Context)this.IL1Iii))
        ((InputMethodManager)this.IL1Iii.getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
                91, 13, 17, 67, 21, 104, 95, 6, 21, 94, 
                14, 83 }, "2ca6a7"))).showInputMethodPicker(); 
    }
  }
  
  public class lIi丨I implements CompoundButton.OnCheckedChangeListener {
    public final BaseSettingActivity IL1Iii;
    
    public lIi丨I(BaseSettingActivity this$0) {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (this.IL1Iii.ILL.isPressed() && I11li1.I1L丨11L((Context)this.IL1Iii) != param1Boolean) {
        I11li1.LI1丨l((Context)this.IL1Iii, param1Boolean);
        丨l丨 丨l丨 = ILil.I丨L.ILil.Lil.I丨L().Ilil();
        if (丨l丨 != null) {
          丨l丨 = 丨l丨;
          if (丨l丨.I11L()) {
            丨l丨.iI丨LLL1(1, 0, 0, "");
            BaseSettingActivity baseSettingActivity = this.IL1Iii;
            I11li1.LlLl111l((Context)baseSettingActivity, baseSettingActivity.getString(2131689592));
          } 
        } 
      } 
    }
  }
  
  public class l丨Li1LL implements Runnable {
    public final String IL1Iii;
    
    public final BaseSettingActivity ILil;
    
    public l丨Li1LL(BaseSettingActivity this$0, String param1String) {}
    
    public void run() {
      if (Native.connectService(this.IL1Iii, I11li1.lI1I1((Context)this.ILil, 2131296546, 10192), I11li1.LIIl丨丨L((Context)this.ILil))) {
        I11li1.LlLl111l((Context)this.ILil, ILil.IL1Iii.IL1Iii(new byte[] { 
                -119, -35, -84, -126, -74, -100, -121, -22, -94, -127, 
                -78, -90 }, "ab2d89"));
      } else {
        I11li1.LlLl111l((Context)this.ILil, ILil.IL1Iii.IL1Iii(new byte[] { 
                -117, -36, -6, -124, -67, -63, -122, -57, -43, -118, 
                -121, -63 }, "ccdb3d"));
      } 
      this.ILil.runOnUiThread(new IL1Iii(this));
    }
    
    public class IL1Iii implements Runnable {
      public final BaseSettingActivity.l丨Li1LL IL1Iii;
      
      public IL1Iii(BaseSettingActivity.l丨Li1LL this$0) {}
      
      public void run() {
        this.IL1Iii.ILil.IL1Iii.setEnabled(true);
      }
    }
  }
  
  public class IL1Iii implements Runnable {
    public final BaseSettingActivity.l丨Li1LL IL1Iii;
    
    public IL1Iii(BaseSettingActivity this$0) {}
    
    public void run() {
      this.IL1Iii.ILil.IL1Iii.setEnabled(true);
    }
  }
  
  public class 丨il implements View.OnClickListener {
    public final BaseSettingActivity IL1Iii;
    
    public 丨il(BaseSettingActivity this$0) {}
    
    public void onClick(View param1View) {
      BaseSettingActivity.IL1Iii(this.IL1Iii, 201);
    }
  }
}
