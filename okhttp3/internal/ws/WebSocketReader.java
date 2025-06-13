package okhttp3.internal.ws;

import I丨L.I1I;
import I丨L.iI丨LLL1;
import I丨L.l丨Li1LL;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class WebSocketReader {
  public boolean closed;
  
  private final I1I controlFrameBuffer;
  
  public final FrameCallback frameCallback;
  
  public long frameLength;
  
  public final boolean isClient;
  
  public boolean isControlFrame;
  
  public boolean isFinalFrame;
  
  private final I1I.I1I maskCursor;
  
  private final byte[] maskKey;
  
  private final I1I messageFrameBuffer;
  
  public int opcode;
  
  public final l丨Li1LL source;
  
  public WebSocketReader(boolean paramBoolean, l丨Li1LL paraml丨Li1LL, FrameCallback paramFrameCallback) {
    byte[] arrayOfByte;
    I1I.I1I i1I;
    this.controlFrameBuffer = new I1I();
    this.messageFrameBuffer = new I1I();
    Objects.requireNonNull(paraml丨Li1LL, "source == null");
    Objects.requireNonNull(paramFrameCallback, "frameCallback == null");
    this.isClient = paramBoolean;
    this.source = paraml丨Li1LL;
    this.frameCallback = paramFrameCallback;
    paramFrameCallback = null;
    if (paramBoolean) {
      paraml丨Li1LL = null;
    } else {
      arrayOfByte = new byte[4];
    } 
    this.maskKey = arrayOfByte;
    if (paramBoolean) {
      FrameCallback frameCallback = paramFrameCallback;
    } else {
      i1I = new I1I.I1I();
    } 
    this.maskCursor = i1I;
  }
  
  private void readControlFrame() throws IOException {
    StringBuilder stringBuilder;
    long l = this.frameLength;
    if (l > 0L) {
      this.source.iI丨LLL1(this.controlFrameBuffer, l);
      if (!this.isClient) {
        this.controlFrameBuffer.丨丨丨丨(this.maskCursor);
        this.maskCursor.丨丨(0L);
        WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
        this.maskCursor.close();
      } 
    } 
    switch (this.opcode) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown control opcode: ");
        stringBuilder.append(Integer.toHexString(this.opcode));
        throw new ProtocolException(stringBuilder.toString());
      case 10:
        this.frameCallback.onReadPong(this.controlFrameBuffer.L11丨());
        return;
      case 9:
        this.frameCallback.onReadPing(this.controlFrameBuffer.L11丨());
        return;
      case 8:
        break;
    } 
    short s = 1005;
    l = this.controlFrameBuffer.iI1i丨I();
    if (l != 1L) {
      String str;
      if (l != 0L) {
        s = this.controlFrameBuffer.readShort();
        str = this.controlFrameBuffer.iIi1();
        String str1 = WebSocketProtocol.closeCodeExceptionMessage(s);
        if (str1 != null)
          throw new ProtocolException(str1); 
      } else {
        str = "";
      } 
      this.frameCallback.onReadClose(s, str);
      this.closed = true;
      return;
    } 
    throw new ProtocolException("Malformed close payload length of 1.");
  }
  
  private void readHeader() throws IOException {
    if (!this.closed) {
      long l = this.source.timeout().timeoutNanos();
      this.source.timeout().clearTimeout();
      try {
        boolean bool1;
        boolean bool;
        byte b = this.source.readByte();
        int i = b & 0xFF;
        this.source.timeout().timeout(l, TimeUnit.NANOSECONDS);
        this.opcode = i & 0xF;
        boolean bool2 = true;
        if ((i & 0x80) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.isFinalFrame = bool1;
        if ((i & 0x8) != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.isControlFrame = bool;
        if (!bool || bool1) {
          boolean bool3;
          if ((i & 0x40) != 0) {
            b = 1;
          } else {
            b = 0;
          } 
          if ((i & 0x20) != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          } 
          if ((i & 0x10) != 0) {
            i = 1;
          } else {
            i = 0;
          } 
          if (b == 0 && !bool3 && i == 0) {
            int j = this.source.readByte() & 0xFF;
            if ((j & 0x80) != 0) {
              bool1 = bool2;
            } else {
              bool1 = false;
            } 
            if (bool1 == this.isClient) {
              String str;
              if (this.isClient) {
                str = "Server-sent frames must not be masked.";
              } else {
                str = "Client-sent frames must be masked.";
              } 
              throw new ProtocolException(str);
            } 
            l = (j & 0x7F);
            this.frameLength = l;
            if (l == 126L) {
              this.frameLength = this.source.readShort() & 0xFFFFL;
            } else if (l == 127L) {
              l = this.source.readLong();
              this.frameLength = l;
              if (l < 0L) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Frame length 0x");
                stringBuilder.append(Long.toHexString(this.frameLength));
                stringBuilder.append(" > 0x7FFFFFFFFFFFFFFF");
                throw new ProtocolException(stringBuilder.toString());
              } 
            } 
            if (!this.isControlFrame || this.frameLength <= 125L)
              return; 
            throw new ProtocolException("Control frame must be less than 125B.");
          } 
          throw new ProtocolException("Reserved flags are unsupported.");
        } 
        throw new ProtocolException("Control frames must be final.");
      } finally {
        this.source.timeout().timeout(l, TimeUnit.NANOSECONDS);
      } 
    } 
    throw new IOException("closed");
  }
  
  private void readMessage() throws IOException {
    while (!this.closed) {
      long l = this.frameLength;
      if (l > 0L) {
        this.source.iI丨LLL1(this.messageFrameBuffer, l);
        if (!this.isClient) {
          this.messageFrameBuffer.丨丨丨丨(this.maskCursor);
          this.maskCursor.丨丨(this.messageFrameBuffer.iI1i丨I() - this.frameLength);
          WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
          this.maskCursor.close();
        } 
      } 
      if (this.isFinalFrame)
        return; 
      readUntilNonControlFrame();
      if (this.opcode == 0)
        continue; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Expected continuation opcode. Got: ");
      stringBuilder.append(Integer.toHexString(this.opcode));
      throw new ProtocolException(stringBuilder.toString());
    } 
    throw new IOException("closed");
  }
  
  private void readMessageFrame() throws IOException {
    int i = this.opcode;
    if (i == 1 || i == 2) {
      readMessage();
      if (i == 1) {
        this.frameCallback.onReadMessage(this.messageFrameBuffer.iIi1());
      } else {
        this.frameCallback.onReadMessage(this.messageFrameBuffer.L11丨());
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unknown opcode: ");
    stringBuilder.append(Integer.toHexString(i));
    throw new ProtocolException(stringBuilder.toString());
  }
  
  private void readUntilNonControlFrame() throws IOException {
    while (!this.closed) {
      readHeader();
      if (!this.isControlFrame)
        break; 
      readControlFrame();
    } 
  }
  
  public void processNextFrame() throws IOException {
    readHeader();
    if (this.isControlFrame) {
      readControlFrame();
    } else {
      readMessageFrame();
    } 
  }
  
  public static interface FrameCallback {
    void onReadClose(int param1Int, String param1String);
    
    void onReadMessage(iI丨LLL1 param1iI丨LLL1) throws IOException;
    
    void onReadMessage(String param1String) throws IOException;
    
    void onReadPing(iI丨LLL1 param1iI丨LLL1);
    
    void onReadPong(iI丨LLL1 param1iI丨LLL1);
  }
}
