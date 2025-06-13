package okhttp3.internal.ws;

import I丨L.I11L;
import I丨L.I11li1;
import I丨L.I1I;
import I丨L.I丨L;
import I丨L.iI丨LLL1;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public final class WebSocketWriter {
  public boolean activeWriter;
  
  public final I1I buffer;
  
  public final FrameSink frameSink;
  
  public final boolean isClient;
  
  private final I1I.I1I maskCursor;
  
  private final byte[] maskKey;
  
  public final Random random;
  
  public final I丨L sink;
  
  public final I1I sinkBuffer;
  
  public boolean writerClosed;
  
  public WebSocketWriter(boolean paramBoolean, I丨L paramI丨L, Random paramRandom) {
    I1I.I1I i1I;
    this.buffer = new I1I();
    this.frameSink = new FrameSink();
    Objects.requireNonNull(paramI丨L, "sink == null");
    Objects.requireNonNull(paramRandom, "random == null");
    this.isClient = paramBoolean;
    this.sink = paramI丨L;
    this.sinkBuffer = paramI丨L.IL1Iii();
    this.random = paramRandom;
    paramRandom = null;
    if (paramBoolean) {
      byte[] arrayOfByte = new byte[4];
    } else {
      paramI丨L = null;
    } 
    this.maskKey = (byte[])paramI丨L;
    Random random = paramRandom;
    if (paramBoolean)
      i1I = new I1I.I1I(); 
    this.maskCursor = i1I;
  }
  
  private void writeControlFrame(int paramInt, iI丨LLL1 paramiI丨LLL1) throws IOException {
    if (!this.writerClosed) {
      int i = paramiI丨LLL1.size();
      if (i <= 125L) {
        this.sinkBuffer.iI(paramInt | 0x80);
        if (this.isClient) {
          this.sinkBuffer.iI(i | 0x80);
          this.random.nextBytes(this.maskKey);
          this.sinkBuffer.i1(this.maskKey);
          if (i > 0) {
            long l = this.sinkBuffer.iI1i丨I();
            this.sinkBuffer.iIilII1(paramiI丨LLL1);
            this.sinkBuffer.丨丨丨丨(this.maskCursor);
            this.maskCursor.丨丨(l);
            WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
            this.maskCursor.close();
          } 
        } else {
          this.sinkBuffer.iI(i);
          this.sinkBuffer.iIilII1(paramiI丨LLL1);
        } 
        this.sink.flush();
        return;
      } 
      throw new IllegalArgumentException("Payload size must be less than or equal to 125");
    } 
    throw new IOException("closed");
  }
  
  public I11li1 newMessageSink(int paramInt, long paramLong) {
    if (!this.activeWriter) {
      this.activeWriter = true;
      FrameSink frameSink = this.frameSink;
      frameSink.formatOpcode = paramInt;
      frameSink.contentLength = paramLong;
      frameSink.isFirstFrame = true;
      frameSink.closed = false;
      return frameSink;
    } 
    throw new IllegalStateException("Another message writer is active. Did you call close()?");
  }
  
  public void writeClose(int paramInt, iI丨LLL1 paramiI丨LLL1) throws IOException {
    iI丨LLL1 iI丨LLL11 = iI丨LLL1.EMPTY;
    if (paramInt != 0 || paramiI丨LLL1 != null) {
      if (paramInt != 0)
        WebSocketProtocol.validateCloseCode(paramInt); 
      I1I i1I = new I1I();
      i1I.I1(paramInt);
      if (paramiI丨LLL1 != null)
        i1I.iIilII1(paramiI丨LLL1); 
      iI丨LLL11 = i1I.L11丨();
    } 
    try {
      writeControlFrame(8, iI丨LLL11);
      return;
    } finally {
      this.writerClosed = true;
    } 
  }
  
  public void writeMessageFrame(int paramInt, long paramLong, boolean paramBoolean1, boolean paramBoolean2) throws IOException {
    if (!this.writerClosed) {
      boolean bool = false;
      if (!paramBoolean1)
        paramInt = 0; 
      int i = paramInt;
      if (paramBoolean2)
        i = paramInt | 0x80; 
      this.sinkBuffer.iI(i);
      paramInt = bool;
      if (this.isClient)
        paramInt = 128; 
      if (paramLong <= 125L) {
        i = (int)paramLong;
        this.sinkBuffer.iI(i | paramInt);
      } else if (paramLong <= 65535L) {
        this.sinkBuffer.iI(paramInt | 0x7E);
        this.sinkBuffer.I1((int)paramLong);
      } else {
        this.sinkBuffer.iI(paramInt | 0x7F);
        this.sinkBuffer.iI丨Li丨lI(paramLong);
      } 
      if (this.isClient) {
        this.random.nextBytes(this.maskKey);
        this.sinkBuffer.i1(this.maskKey);
        if (paramLong > 0L) {
          long l = this.sinkBuffer.iI1i丨I();
          this.sinkBuffer.write(this.buffer, paramLong);
          this.sinkBuffer.丨丨丨丨(this.maskCursor);
          this.maskCursor.丨丨(l);
          WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
          this.maskCursor.close();
        } 
      } else {
        this.sinkBuffer.write(this.buffer, paramLong);
      } 
      this.sink.I1I();
      return;
    } 
    throw new IOException("closed");
  }
  
  public void writePing(iI丨LLL1 paramiI丨LLL1) throws IOException {
    writeControlFrame(9, paramiI丨LLL1);
  }
  
  public void writePong(iI丨LLL1 paramiI丨LLL1) throws IOException {
    writeControlFrame(10, paramiI丨LLL1);
  }
  
  public final class FrameSink implements I11li1 {
    public boolean closed;
    
    public long contentLength;
    
    public int formatOpcode;
    
    public boolean isFirstFrame;
    
    public final WebSocketWriter this$0;
    
    public void close() throws IOException {
      if (!this.closed) {
        WebSocketWriter webSocketWriter = WebSocketWriter.this;
        webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.buffer.iI1i丨I(), this.isFirstFrame, true);
        this.closed = true;
        WebSocketWriter.this.activeWriter = false;
        return;
      } 
      throw new IOException("closed");
    }
    
    public void flush() throws IOException {
      if (!this.closed) {
        WebSocketWriter webSocketWriter = WebSocketWriter.this;
        webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.buffer.iI1i丨I(), this.isFirstFrame, false);
        this.isFirstFrame = false;
        return;
      } 
      throw new IOException("closed");
    }
    
    public I11L timeout() {
      return WebSocketWriter.this.sink.timeout();
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      if (!this.closed) {
        boolean bool;
        WebSocketWriter.this.buffer.write(param1I1I, param1Long);
        if (this.isFirstFrame && this.contentLength != -1L && WebSocketWriter.this.buffer.iI1i丨I() > this.contentLength - 8192L) {
          bool = true;
        } else {
          bool = false;
        } 
        param1Long = WebSocketWriter.this.buffer.llI();
        if (param1Long > 0L && !bool) {
          WebSocketWriter.this.writeMessageFrame(this.formatOpcode, param1Long, this.isFirstFrame, false);
          this.isFirstFrame = false;
        } 
        return;
      } 
      throw new IOException("closed");
    }
  }
}
