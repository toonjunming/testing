package com.main.widget;

import ILil.IL1Iii;
import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.ArrayList;

public class IPView extends AbsEditTextGroup {
  public IPView(Context paramContext) {
    this(paramContext, null, 0);
  }
  
  public IPView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public IPView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void I1I(TextView paramTextView) {
    paramTextView.setPadding(0, 0, 0, 5);
    paramTextView.getPaint().setFakeBoldText(true);
    paramTextView.setBackgroundColor(-1);
    paramTextView.setGravity(80);
  }
  
  public void ILil(AbsEditText paramAbsEditText) {}
  
  public void afterTextChanged(Editable paramEditable) {
    if (paramEditable.toString().length() == getDelMaxLength())
      for (byte b = 0; b < this.I1I.size() - 1; b++) {
        if (((AbsEditText)this.I1I.get(b)).hasFocus()) {
          ((AbsEditText)this.I1I.get(b)).clearFocus();
          ArrayList<AbsEditText> arrayList = this.I1I;
          int i = b + 1;
          ((AbsEditText)arrayList.get(i)).requestFocus();
          String str = ((AbsEditText)this.I1I.get(b)).getText().toString();
          if (Integer.parseInt(str) > 255) {
            ((AbsEditText)this.I1I.get(b)).setText(str.substring(0, 2));
            ((AbsEditText)this.I1I.get(i)).setText(str.substring(2, 3));
            ((AbsEditText)this.I1I.get(i)).setSelection(1);
          } 
          break;
        } 
      }  
  }
  
  public AbsEditText getAbsEditText() {
    return new IPEditText(getContext());
  }
  
  public int getChildCount() {
    return 7;
  }
  
  public int getDelMaxLength() {
    return 3;
  }
  
  public String getSemicolomText() {
    return IL1Iii.IL1Iii(new byte[] { 72 }, "f246c7");
  }
  
  public void setGatewayText(String[] paramArrayOfString) {
    byte b2 = 0;
    byte b1 = b2;
    String[] arrayOfString = paramArrayOfString;
    if (paramArrayOfString == null) {
      arrayOfString = new String[4];
      arrayOfString[0] = IL1Iii.IL1Iii(new byte[] { 83, 3, 87 }, "a6bd13");
      arrayOfString[1] = IL1Iii.IL1Iii(new byte[] { 4, 81, 12 }, "6d9108");
      arrayOfString[2] = IL1Iii.IL1Iii(new byte[] { 3, 87, 6 }, "1b363d");
      arrayOfString[3] = IL1Iii.IL1Iii(new byte[] { 0 }, "0a6637");
      b1 = b2;
    } 
    while (b1 < this.I1I.size()) {
      ((AbsEditText)this.I1I.get(b1)).setText(arrayOfString[b1]);
      b1++;
    } 
  }
}
