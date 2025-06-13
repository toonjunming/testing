package I丨L;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class LlLI1 implements I丨L {
  public boolean I1I;
  
  public final I1I IL1Iii = new I1I();
  
  public final I11li1 ILil;
  
  public LlLI1(I11li1 paramI11li1) {
    Objects.requireNonNull(paramI11li1, "sink == null");
    this.ILil = paramI11li1;
  }
  
  public I丨L I1I() throws IOException {
    if (!this.I1I) {
      long l = this.IL1Iii.iI1i丨I();
      if (l > 0L)
        this.ILil.write(this.IL1Iii, l); 
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I1I IL1Iii() {
    return this.IL1Iii;
  }
  
  public I丨L ILL(String paramString) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.lL(paramString);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L IL丨丨l(long paramLong) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.I丨(paramLong);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L I丨iL() throws IOException {
    if (!this.I1I) {
      long l = this.IL1Iii.llI();
      if (l > 0L)
        this.ILil.write(this.IL1Iii, l); 
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public void close() throws IOException {
    Exception exception1;
    Exception exception2;
    if (this.I1I)
      return; 
    try {
      I1I i1I = this.IL1Iii;
      long l = i1I.ILil;
      if (l > 0L)
        this.ILil.write(i1I, l); 
      i1I = null;
    } finally {}
    try {
    
    } finally {
      Exception exception = null;
      exception2 = exception1;
    } 
    this.I1I = true;
    if (exception2 == null)
      return; 
    llliI.Ilil(exception2);
    throw null;
  }
  
  public void flush() throws IOException {
    if (!this.I1I) {
      I1I i1I = this.IL1Iii;
      long l = i1I.ILil;
      if (l > 0L)
        this.ILil.write(i1I, l); 
      this.ILil.flush();
      return;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L iIlLiL(iI丨LLL1 paramiI丨LLL1) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.iIilII1(paramiI丨LLL1);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public boolean isOpen() {
    return this.I1I ^ true;
  }
  
  public long lIi丨I(丨lL param丨lL) throws IOException {
    if (param丨lL != null) {
      long l = 0L;
      while (true) {
        long l1 = param丨lL.read(this.IL1Iii, 8192L);
        if (l1 != -1L) {
          l += l1;
          I丨iL();
          continue;
        } 
        return l;
      } 
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public I11L timeout() {
    return this.ILil.timeout();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("buffer(");
    stringBuilder.append(this.ILil);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public int write(ByteBuffer paramByteBuffer) throws IOException {
    if (!this.I1I) {
      int i = this.IL1Iii.write(paramByteBuffer);
      I丨iL();
      return i;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L write(byte[] paramArrayOfbyte) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.i1(paramArrayOfbyte);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.ili丨11(paramArrayOfbyte, paramInt1, paramInt2);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public void write(I1I paramI1I, long paramLong) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.write(paramI1I, paramLong);
      I丨iL();
      return;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L writeByte(int paramInt) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.iI(paramInt);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L writeInt(int paramInt) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.iiIIi丨11(paramInt);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L writeShort(int paramInt) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.I1(paramInt);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public I丨L 丨l丨(long paramLong) throws IOException {
    if (!this.I1I) {
      this.IL1Iii.LlLiL丨L丨(paramLong);
      I丨iL();
      return this;
    } 
    throw new IllegalStateException("closed");
  }
  
  public OutputStream 丨丨丨1丨() {
    return new IL1Iii(this);
  }
  
  public class IL1Iii extends OutputStream {
    public final LlLI1 IL1Iii;
    
    public IL1Iii(LlLI1 this$0) {}
    
    public void close() throws IOException {
      this.IL1Iii.close();
    }
    
    public void flush() throws IOException {
      LlLI1 llLI1 = this.IL1Iii;
      if (!llLI1.I1I)
        llLI1.flush(); 
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.IL1Iii);
      stringBuilder.append(".outputStream()");
      return stringBuilder.toString();
    }
    
    public void write(int param1Int) throws IOException {
      LlLI1 llLI1 = this.IL1Iii;
      if (!llLI1.I1I) {
        llLI1.IL1Iii.iI((byte)param1Int);
        this.IL1Iii.I丨iL();
        return;
      } 
      throw new IOException("closed");
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      LlLI1 llLI1 = this.IL1Iii;
      if (!llLI1.I1I) {
        llLI1.IL1Iii.ili丨11(param1ArrayOfbyte, param1Int1, param1Int2);
        this.IL1Iii.I丨iL();
        return;
      } 
      throw new IOException("closed");
    }
  }
}
