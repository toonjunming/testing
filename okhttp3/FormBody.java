package okhttp3;

import I丨L.I1I;
import I丨L.I丨L;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.internal.Util;

public final class FormBody extends RequestBody {
  private static final MediaType CONTENT_TYPE = MediaType.get("application/x-www-form-urlencoded");
  
  private final List<String> encodedNames;
  
  private final List<String> encodedValues;
  
  public FormBody(List<String> paramList1, List<String> paramList2) {
    this.encodedNames = Util.immutableList(paramList1);
    this.encodedValues = Util.immutableList(paramList2);
  }
  
  private long writeOrCountBytes(@Nullable I丨L paramI丨L, boolean paramBoolean) {
    long l;
    if (paramBoolean) {
      paramI丨L = new I1I();
    } else {
      paramI丨L = paramI丨L.IL1Iii();
    } 
    byte b = 0;
    int i = this.encodedNames.size();
    while (b < i) {
      if (b > 0)
        paramI丨L.iI(38); 
      paramI丨L.lL(this.encodedNames.get(b));
      paramI丨L.iI(61);
      paramI丨L.lL(this.encodedValues.get(b));
      b++;
    } 
    if (paramBoolean) {
      l = paramI丨L.iI1i丨I();
      paramI丨L.丨丨LLlI1();
    } else {
      l = 0L;
    } 
    return l;
  }
  
  public long contentLength() {
    return writeOrCountBytes(null, true);
  }
  
  public MediaType contentType() {
    return CONTENT_TYPE;
  }
  
  public String encodedName(int paramInt) {
    return this.encodedNames.get(paramInt);
  }
  
  public String encodedValue(int paramInt) {
    return this.encodedValues.get(paramInt);
  }
  
  public String name(int paramInt) {
    return HttpUrl.percentDecode(encodedName(paramInt), true);
  }
  
  public int size() {
    return this.encodedNames.size();
  }
  
  public String value(int paramInt) {
    return HttpUrl.percentDecode(encodedValue(paramInt), true);
  }
  
  public void writeTo(I丨L paramI丨L) throws IOException {
    writeOrCountBytes(paramI丨L, false);
  }
  
  public static final class Builder {
    @Nullable
    private final Charset charset;
    
    private final List<String> names = new ArrayList<String>();
    
    private final List<String> values = new ArrayList<String>();
    
    public Builder() {
      this(null);
    }
    
    public Builder(@Nullable Charset param1Charset) {
      this.charset = param1Charset;
    }
    
    public Builder add(String param1String1, String param1String2) {
      Objects.requireNonNull(param1String1, "name == null");
      Objects.requireNonNull(param1String2, "value == null");
      this.names.add(HttpUrl.canonicalize(param1String1, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true, this.charset));
      this.values.add(HttpUrl.canonicalize(param1String2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true, this.charset));
      return this;
    }
    
    public Builder addEncoded(String param1String1, String param1String2) {
      Objects.requireNonNull(param1String1, "name == null");
      Objects.requireNonNull(param1String2, "value == null");
      this.names.add(HttpUrl.canonicalize(param1String1, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true, this.charset));
      this.values.add(HttpUrl.canonicalize(param1String2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true, this.charset));
      return this;
    }
    
    public FormBody build() {
      return new FormBody(this.names, this.values);
    }
  }
}
