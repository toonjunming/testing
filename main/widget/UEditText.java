package com.main.widget;

import ILil.I丨L.I丨L.I11li1;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class UEditText extends EditText {
  private TextView mCopy;
  
  private TextView mPaste;
  
  private PopupWindow popupWindow;
  
  public UEditText(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public UEditText(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public UEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  public String getClibboardText() {
    String str;
    if (Build.VERSION.SDK_INT >= 11) {
      ClipData clipData = ((ClipboardManager)getContext().getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 87, 84, 11, 70, 91, 92, 85, 74, 6 }, "48b693"))).getPrimaryClip();
      if (clipData != null && clipData.getItemCount() > 0) {
        str = clipData.getItemAt(0).getText().toString();
      } else {
        str = "";
      } 
    } else {
      str = ((ClipboardManager)getContext().getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 90, 91, 92, 65, 0, 95, 88, 69, 81 }, "9751b0"))).getText().toString();
    } 
    return str;
  }
  
  public void init() {
    setOnLongClickListener(new IL1Iii(this));
  }
  
  public class IL1Iii implements View.OnLongClickListener {
    public final UEditText IL1Iii;
    
    public IL1Iii(UEditText this$0) {}
    
    public boolean onLongClick(View param1View) {
      String str1;
      try {
        str1 = this.IL1Iii.getClibboardText();
      } catch (Exception exception) {
        str1 = "";
      } 
      String str2 = this.IL1Iii.getText().toString();
      if (TextUtils.isEmpty(str1) && TextUtils.isEmpty(str2))
        return false; 
      I11li1.I1I(100L);
      LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(this.IL1Iii.getContext()).inflate(2131492919, null);
      int i = param1View.getWidth() / 2;
      i = (int)(param1View.getHeight() * 1.5D);
      UEditText.access$002(this.IL1Iii, (TextView)linearLayout.findViewById(2131296370));
      UEditText.access$102(this.IL1Iii, (TextView)linearLayout.findViewById(2131296539));
      int j = (int)(this.IL1Iii.mCopy.getPaint().measureText(ILil.IL1Iii.IL1Iii(new byte[] { -44, -108, -70, -121, -79, -41 }, "107b9a")) * 2.0F * 2.0F);
      if (TextUtils.isEmpty(str2)) {
        this.IL1Iii.mCopy.setVisibility(8);
        UEditText.access$202(this.IL1Iii, new PopupWindow((View)linearLayout, -2, i));
      } else if (TextUtils.isEmpty(str1)) {
        this.IL1Iii.mPaste.setVisibility(8);
        UEditText.access$202(this.IL1Iii, new PopupWindow((View)linearLayout, -2, i));
      } else {
        UEditText.access$202(this.IL1Iii, new PopupWindow((View)linearLayout, -2, i));
      } 
      this.IL1Iii.popupWindow.setOutsideTouchable(true);
      this.IL1Iii.popupWindow.setBackgroundDrawable((Drawable)new ColorDrawable(0));
      this.IL1Iii.popupWindow.setOnDismissListener(new IL1Iii(this));
      ILil iLil = new ILil(this, str1, str2);
      if (this.IL1Iii.mCopy != null)
        this.IL1Iii.mCopy.setOnClickListener(iLil); 
      if (this.IL1Iii.mPaste != null)
        this.IL1Iii.mPaste.setOnClickListener(iLil); 
      int k = this.IL1Iii.getSelectionStart();
      Layout layout = this.IL1Iii.getLayout();
      if (k >= 0 && k <= this.IL1Iii.getText().length()) {
        float f3 = layout.getPrimaryHorizontal(k);
        float f1 = layout.getLineBaseline(layout.getLineForOffset(k));
        int[] arrayOfInt1 = new int[2];
        param1View.getLocationInWindow(arrayOfInt1);
        float f2 = arrayOfInt1[0];
        float f4 = arrayOfInt1[1];
        this.IL1Iii.popupWindow.showAtLocation((View)this.IL1Iii, 0, (int)(f3 + f2), (int)(f1 + f4));
        return false;
      } 
      int[] arrayOfInt = new int[2];
      param1View.getLocationInWindow(arrayOfInt);
      this.IL1Iii.popupWindow.showAtLocation((View)this.IL1Iii, 0, arrayOfInt[0] + j / 4, arrayOfInt[1] + i);
      return false;
    }
    
    public class IL1Iii implements PopupWindow.OnDismissListener {
      public IL1Iii(UEditText.IL1Iii this$0) {}
      
      public void onDismiss() {}
    }
    
    public class ILil implements View.OnClickListener {
      public final UEditText.IL1Iii I1I;
      
      public final String IL1Iii;
      
      public final String ILil;
      
      public ILil(UEditText.IL1Iii this$0, String param2String1, String param2String2) {}
      
      public void onClick(View param2View) {
        if (param2View.getId() == 2131296539) {
          int i = this.I1I.IL1Iii.getSelectionStart();
          try {
            Editable editable = this.I1I.IL1Iii.getText();
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("");
            stringBuilder.append(this.IL1Iii);
            editable.insert(i, stringBuilder.toString());
            this.I1I.IL1Iii.setSelection(i + this.IL1Iii.length());
          } catch (Exception exception) {}
        } else if (exception.getId() == 2131296370) {
          I11li1.Ll丨1(this.I1I.IL1Iii.getContext(), this.ILil);
        } 
        this.I1I.IL1Iii.popupWindow.dismiss();
      }
    }
  }
  
  public class IL1Iii implements PopupWindow.OnDismissListener {
    public IL1Iii(UEditText this$0) {}
    
    public void onDismiss() {}
  }
  
  public class ILil implements View.OnClickListener {
    public final UEditText.IL1Iii I1I;
    
    public final String IL1Iii;
    
    public final String ILil;
    
    public ILil(UEditText this$0, String param1String1, String param1String2) {}
    
    public void onClick(View param1View) {
      if (param1View.getId() == 2131296539) {
        int i = this.I1I.IL1Iii.getSelectionStart();
        try {
          Editable editable = this.I1I.IL1Iii.getText();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("");
          stringBuilder.append(this.IL1Iii);
          editable.insert(i, stringBuilder.toString());
          this.I1I.IL1Iii.setSelection(i + this.IL1Iii.length());
        } catch (Exception exception) {}
      } else if (exception.getId() == 2131296370) {
        I11li1.Ll丨1(this.I1I.IL1Iii.getContext(), this.ILil);
      } 
      this.I1I.IL1Iii.popupWindow.dismiss();
    }
  }
}
