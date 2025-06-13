package okhttp3.internal.http2;

import java.util.Arrays;

public final class Settings {
  public static final int COUNT = 10;
  
  public static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
  
  public static final int ENABLE_PUSH = 2;
  
  public static final int HEADER_TABLE_SIZE = 1;
  
  public static final int INITIAL_WINDOW_SIZE = 7;
  
  public static final int MAX_CONCURRENT_STREAMS = 4;
  
  public static final int MAX_FRAME_SIZE = 5;
  
  public static final int MAX_HEADER_LIST_SIZE = 6;
  
  private int set;
  
  private final int[] values = new int[10];
  
  public void clear() {
    this.set = 0;
    Arrays.fill(this.values, 0);
  }
  
  public int get(int paramInt) {
    return this.values[paramInt];
  }
  
  public boolean getEnablePush(boolean paramBoolean) {
    int i = this.set;
    boolean bool = false;
    if ((i & 0x4) != 0) {
      i = this.values[2];
    } else if (paramBoolean) {
      i = 1;
    } else {
      i = 0;
    } 
    paramBoolean = bool;
    if (i == 1)
      paramBoolean = true; 
    return paramBoolean;
  }
  
  public int getHeaderTableSize() {
    byte b;
    if ((this.set & 0x2) != 0) {
      b = this.values[1];
    } else {
      b = -1;
    } 
    return b;
  }
  
  public int getInitialWindowSize() {
    char c;
    if ((this.set & 0x80) != 0) {
      c = this.values[7];
    } else {
      c = '￿';
    } 
    return c;
  }
  
  public int getMaxConcurrentStreams(int paramInt) {
    if ((this.set & 0x10) != 0)
      paramInt = this.values[4]; 
    return paramInt;
  }
  
  public int getMaxFrameSize(int paramInt) {
    if ((this.set & 0x20) != 0)
      paramInt = this.values[5]; 
    return paramInt;
  }
  
  public int getMaxHeaderListSize(int paramInt) {
    if ((this.set & 0x40) != 0)
      paramInt = this.values[6]; 
    return paramInt;
  }
  
  public boolean isSet(int paramInt) {
    boolean bool = true;
    if ((1 << paramInt & this.set) == 0)
      bool = false; 
    return bool;
  }
  
  public void merge(Settings paramSettings) {
    for (byte b = 0; b < 10; b++) {
      if (paramSettings.isSet(b))
        set(b, paramSettings.get(b)); 
    } 
  }
  
  public Settings set(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      int[] arrayOfInt = this.values;
      if (paramInt1 < arrayOfInt.length) {
        this.set = 1 << paramInt1 | this.set;
        arrayOfInt[paramInt1] = paramInt2;
      } 
    } 
    return this;
  }
  
  public int size() {
    return Integer.bitCount(this.set);
  }
}
