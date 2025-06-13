package com.main.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public abstract class AbsEditTextGroup extends LinearLayout implements TextWatcher {
  public ArrayList<AbsEditText> I1I = new ArrayList<AbsEditText>();
  
  public float IL1Iii = 16.0F;
  
  public int ILil = 4;
  
  public AbsEditTextGroup(Context paramContext) {
    this(paramContext, null, 0);
  }
  
  public AbsEditTextGroup(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AbsEditTextGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    IL1Iii();
    I丨L();
  }
  
  public abstract void I1I(TextView paramTextView);
  
  public void IL1Iii() {
    for (byte b = 0; b < getChildCount(); b++) {
      if (b % 2 == 0) {
        AbsEditText absEditText = Ilil();
        this.I1I.add(absEditText);
        addView((View)absEditText);
      } else {
        addView((View)l丨Li1LL());
      } 
    } 
  }
  
  public abstract void ILil(AbsEditText paramAbsEditText);
  
  public AbsEditText Ilil() {
    AbsEditText absEditText = getAbsEditText();
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -1);
    layoutParams.weight = 1.0F;
    absEditText.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    absEditText.setTextSize(this.IL1Iii);
    absEditText.setTextColor(-7829368);
    absEditText.setGravity(17);
    int i = this.ILil;
    absEditText.setPadding(i, i, i, i);
    absEditText.setSingleLine();
    absEditText.setHint(ILil.IL1Iii.IL1Iii(new byte[] { 86 }, "f849ed"));
    absEditText.setFocusableInTouchMode(true);
    absEditText.setBackgroundDrawable((Drawable)new ColorDrawable(-1));
    ILil(absEditText);
    return absEditText;
  }
  
  public void I丨L() {
    for (byte b = 0; b < this.I1I.size(); b++) {
      ((AbsEditText)this.I1I.get(b)).addTextChangedListener(this);
      if (b != 0)
        ((AbsEditText)this.I1I.get(b)).setOnKeyListener(new IL1Iii(this, this.I1I.get(b - 1), this.I1I.get(b))); 
    } 
  }
  
  public void afterTextChanged(Editable paramEditable) {
    if (paramEditable.toString().length() == getDelMaxLength())
      for (byte b = 0; b < this.I1I.size() - 1; b++) {
        if (((AbsEditText)this.I1I.get(b)).hasFocus()) {
          ((AbsEditText)this.I1I.get(b)).clearFocus();
          ((AbsEditText)this.I1I.get(b + 1)).requestFocus();
          break;
        } 
      }  
  }
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public abstract AbsEditText getAbsEditText();
  
  public byte[] getBytesWithIP() {
    byte[] arrayOfByte = new byte[4];
    for (byte b = 0; b < this.I1I.size(); b++) {
      boolean bool;
      try {
        bool = Integer.valueOf(((AbsEditText)this.I1I.get(b)).getText().toString()).intValue();
      } catch (Exception exception) {
        bool = false;
      } 
      arrayOfByte[b] = (byte)bool;
    } 
    return arrayOfByte;
  }
  
  public byte[] getBytesWithMac() {
    byte[] arrayOfByte = new byte[6];
    for (byte b = 0; b < this.I1I.size(); b++)
      arrayOfByte[b] = Integer.valueOf(((AbsEditText)this.I1I.get(b)).getText().toString(), 16).byteValue(); 
    return arrayOfByte;
  }
  
  public abstract int getChildCount();
  
  public ArrayList<AbsEditText> getChildEditTextViews() {
    return this.I1I;
  }
  
  public abstract int getDelMaxLength();
  
  public abstract String getSemicolomText();
  
  public TextView l丨Li1LL() {
    TextView textView = new TextView(getContext());
    textView.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -1));
    textView.setTextSize(this.IL1Iii);
    textView.setTextColor(-12303292);
    textView.setText(getSemicolomText());
    I1I(textView);
    return textView;
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public class IL1Iii implements View.OnKeyListener {
    public AbsEditText IL1Iii;
    
    public AbsEditText ILil;
    
    public IL1Iii(AbsEditTextGroup this$0, AbsEditText param1AbsEditText1, AbsEditText param1AbsEditText2) {
      this.ILil = param1AbsEditText1;
      this.IL1Iii = param1AbsEditText2;
    }
    
    public boolean onKey(View param1View, int param1Int, KeyEvent param1KeyEvent) {
      if (param1Int == 67 && param1KeyEvent.getAction() == 0 && this.IL1Iii.getSelectionStart() == 0) {
        this.IL1Iii.clearFocus();
        this.ILil.requestFocus();
        AbsEditText absEditText = this.ILil;
        absEditText.setSelection(absEditText.getText().toString().trim().length());
        return true;
      } 
      return false;
    }
  }
}
