package com.main.widget;

import android.content.Context;
import android.util.AttributeSet;

public class IPEditText extends AbsEditText {
  public IPEditText(Context paramContext) {
    this(paramContext, null, 0);
  }
  
  public IPEditText(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public IPEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public int getEditTextInputType() {
    return 2;
  }
  
  public char[] getInputFilterAcceptedChars() {
    return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
  }
  
  public int getMaxLength() {
    return 3;
  }
}
