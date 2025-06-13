package I丨L;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public final class lIi丨I {
  public static final Logger IL1Iii = Logger.getLogger(lIi丨I.class.getName());
  
  public static I丨L I1I(I11li1 paramI11li1) {
    return new LlLI1(paramI11li1);
  }
  
  public static I11li1 IL1Iii(File paramFile) throws FileNotFoundException {
    if (paramFile != null)
      return iI丨LLL1(new FileOutputStream(paramFile, true)); 
    throw new IllegalArgumentException("file == null");
  }
  
  public static 丨lL ILL(InputStream paramInputStream) {
    return Ll丨1(paramInputStream, new I11L());
  }
  
  public static I11li1 ILil() {
    return new I1I();
  }
  
  public static IL1Iii IL丨丨l(Socket paramSocket) {
    return new I丨L(paramSocket);
  }
  
  public static boolean Ilil(AssertionError paramAssertionError) {
    boolean bool;
    if (paramAssertionError.getCause() != null && paramAssertionError.getMessage() != null && paramAssertionError.getMessage().contains("getsockname failed")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static l丨Li1LL I丨L(丨lL param丨lL) {
    return new ll丨L1ii(param丨lL);
  }
  
  public static I11li1 I丨iL(OutputStream paramOutputStream, I11L paramI11L) {
    if (paramOutputStream != null) {
      if (paramI11L != null)
        return new IL1Iii(paramI11L, paramOutputStream); 
      throw new IllegalArgumentException("timeout == null");
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  public static 丨lL Ll丨1(InputStream paramInputStream, I11L paramI11L) {
    if (paramInputStream != null) {
      if (paramI11L != null)
        return new ILil(paramI11L, paramInputStream); 
      throw new IllegalArgumentException("timeout == null");
    } 
    throw new IllegalArgumentException("in == null");
  }
  
  public static I11li1 L丨1丨1丨I(Socket paramSocket) throws IOException {
    if (paramSocket != null) {
      if (paramSocket.getOutputStream() != null) {
        IL1Iii iL1Iii = IL丨丨l(paramSocket);
        return iL1Iii.sink(I丨iL(paramSocket.getOutputStream(), iL1Iii));
      } 
      throw new IOException("socket's output stream == null");
    } 
    throw new IllegalArgumentException("socket == null");
  }
  
  public static I11li1 iI丨LLL1(OutputStream paramOutputStream) {
    return I丨iL(paramOutputStream, new I11L());
  }
  
  public static 丨lL lIi丨I(Socket paramSocket) throws IOException {
    if (paramSocket != null) {
      if (paramSocket.getInputStream() != null) {
        IL1Iii iL1Iii = IL丨丨l(paramSocket);
        return iL1Iii.source(Ll丨1(paramSocket.getInputStream(), iL1Iii));
      } 
      throw new IOException("socket's input stream == null");
    } 
    throw new IllegalArgumentException("socket == null");
  }
  
  public static I11li1 l丨Li1LL(File paramFile) throws FileNotFoundException {
    if (paramFile != null)
      return iI丨LLL1(new FileOutputStream(paramFile)); 
    throw new IllegalArgumentException("file == null");
  }
  
  public static 丨lL 丨il(File paramFile) throws FileNotFoundException {
    if (paramFile != null)
      return ILL(new FileInputStream(paramFile)); 
    throw new IllegalArgumentException("file == null");
  }
  
  public final class I1I implements I11li1 {
    public void close() throws IOException {}
    
    public void flush() throws IOException {}
    
    public I11L timeout() {
      return I11L.NONE;
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      param1I1I.skip(param1Long);
    }
  }
  
  public final class IL1Iii implements I11li1 {
    public final I11L IL1Iii;
    
    public final OutputStream ILil;
    
    public IL1Iii(lIi丨I this$0, OutputStream param1OutputStream) {}
    
    public void close() throws IOException {
      this.ILil.close();
    }
    
    public void flush() throws IOException {
      this.ILil.flush();
    }
    
    public I11L timeout() {
      return this.IL1Iii;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("sink(");
      stringBuilder.append(this.ILil);
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      llliI.ILil(param1I1I.ILil, 0L, param1Long);
      while (param1Long > 0L) {
        this.IL1Iii.throwIfReached();
        lI丨lii lI丨lii = param1I1I.IL1Iii;
        int i = (int)Math.min(param1Long, (lI丨lii.I1I - lI丨lii.ILil));
        this.ILil.write(lI丨lii.IL1Iii, lI丨lii.ILil, i);
        int j = lI丨lii.ILil + i;
        lI丨lii.ILil = j;
        long l2 = i;
        long l1 = param1Long - l2;
        param1I1I.ILil -= l2;
        param1Long = l1;
        if (j == lI丨lii.I1I) {
          param1I1I.IL1Iii = lI丨lii.ILil();
          iIi1.IL1Iii(lI丨lii);
          param1Long = l1;
        } 
      } 
    }
  }
  
  public final class ILil implements 丨lL {
    public final I11L IL1Iii;
    
    public final InputStream ILil;
    
    public ILil(lIi丨I this$0, InputStream param1InputStream) {}
    
    public void close() throws IOException {
      this.ILil.close();
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      int i = param1Long cmp 0L;
      if (i >= 0) {
        if (i == 0)
          return 0L; 
        try {
          this.IL1Iii.throwIfReached();
          lI丨lii lI丨lii = param1I1I.l丨丨i11(1);
          i = (int)Math.min(param1Long, (8192 - lI丨lii.I1I));
          i = this.ILil.read(lI丨lii.IL1Iii, lI丨lii.I1I, i);
          if (i == -1)
            return -1L; 
          lI丨lii.I1I += i;
          param1Long = param1I1I.ILil;
          long l = i;
          param1I1I.ILil = param1Long + l;
          return l;
        } catch (AssertionError assertionError) {
          if (lIi丨I.Ilil(assertionError))
            throw new IOException(assertionError); 
          throw assertionError;
        } 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(param1Long);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public I11L timeout() {
      return this.IL1Iii;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("source(");
      stringBuilder.append(this.ILil);
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
  }
  
  public final class I丨L extends IL1Iii {
    public final Socket IL1Iii;
    
    public I丨L(lIi丨I this$0) {}
    
    public IOException newTimeoutException(@Nullable IOException param1IOException) {
      SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
      if (param1IOException != null)
        socketTimeoutException.initCause(param1IOException); 
      return socketTimeoutException;
    }
    
    public void timedOut() {
      try {
        this.IL1Iii.close();
      } catch (Exception exception) {
        Logger logger = lIi丨I.IL1Iii;
        Level level = Level.WARNING;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to close timed out socket ");
        stringBuilder.append(this.IL1Iii);
        logger.log(level, stringBuilder.toString(), exception);
      } catch (AssertionError assertionError) {
        if (lIi丨I.Ilil(assertionError)) {
          Logger logger = lIi丨I.IL1Iii;
          Level level = Level.WARNING;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Failed to close timed out socket ");
          stringBuilder.append(this.IL1Iii);
          logger.log(level, stringBuilder.toString(), assertionError);
        } else {
          throw assertionError;
        } 
      } 
    }
  }
}
