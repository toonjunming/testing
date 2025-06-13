package com.main.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

public abstract class AbsEditText extends AppCompatEditText {
  public AbsEditText(Context paramContext) {
    this(paramContext, null, 0);
  }
  
  public AbsEditText(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AbsEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ILil();
    IL1Iii();
  }
  
  public void IL1Iii() {
    setKeyListener((KeyListener)new IL1Iii(this));
  }
  
  public void ILil() {
    setFilters(new InputFilter[] { (InputFilter)new InputFilter.LengthFilter(getMaxLength()) });
  }
  
  public abstract int getEditTextInputType();
  
  public abstract char[] getInputFilterAcceptedChars();
  
  public abstract int getMaxLength();
  
  public class IL1Iii extends NumberKeyListener {
    public final AbsEditText IL1Iii;
    
    public IL1Iii(AbsEditText this$0) {}
    
    public char[] getAcceptedChars() {
      return this.IL1Iii.getInputFilterAcceptedChars();
    }
    
    public int getInputType() {
      return this.IL1Iii.getEditTextInputType();
    }
  }
}
