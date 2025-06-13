package com.main.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.AppCompatEditText;

public class UPassEditText extends AppCompatEditText {
  private boolean isMasked;
  
  private String maskedText;
  
  public UPassEditText(Context paramContext) {
    super(paramContext);
    setup();
  }
  
  public UPassEditText(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setup();
  }
  
  public UPassEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setup();
  }
  
  private String getMaskedText(CharSequence paramCharSequence) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramCharSequence.length(); b++) {
      stringBuilder.append(ILil.IL1Iii.IL1Iii(new byte[] { 72 }, "b13940"));
    } 
    return stringBuilder.toString();
  }
  
  private void setup() {
    this.isMasked = true;
    this.maskedText = "";
    setFocusableInTouchMode(true);
    setFocusable(true);
    requestFocus();
    setCursorVisible(false);
    (new Handler()).postDelayed(new IL1Iii(this), 500L);
  }
  
  public boolean isMasked() {
    return this.isMasked;
  }
  
  public void onDraw(Canvas paramCanvas) {
    String str = getMaskedText(this.maskedText);
    if (!this.isMasked)
      str = getText().toString(); 
    paramCanvas.drawText(str, getCompoundPaddingLeft(), getBaseline(), (Paint)getPaint());
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    this.maskedText = paramCharSequence.toString();
  }
  
  public void setMasked(boolean paramBoolean) {
    this.isMasked = paramBoolean;
    invalidate();
  }
  
  public class IL1Iii implements Runnable {
    public final UPassEditText IL1Iii;
    
    public IL1Iii(UPassEditText this$0) {}
    
    public void run() {
      ((InputMethodManager)this.IL1Iii.getContext().getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
              11, 13, 22, 68, 21, 105, 15, 6, 18, 89, 
              14, 82 }, "bcf1a6"))).toggleSoftInput(0, 2);
    }
  }
}
