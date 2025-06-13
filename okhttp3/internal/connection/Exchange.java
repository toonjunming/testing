package okhttp3.internal.connection;

import I丨L.I11li1;
import I丨L.I1I;
import I丨L.I丨iL;
import I丨L.L丨1丨1丨I;
import I丨L.lIi丨I;
import I丨L.丨lL;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketException;
import javax.annotation.Nullable;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.ws.RealWebSocket;

public final class Exchange {
  public final Call call;
  
  public final ExchangeCodec codec;
  
  private boolean duplex;
  
  public final EventListener eventListener;
  
  public final ExchangeFinder finder;
  
  public final Transmitter transmitter;
  
  public Exchange(Transmitter paramTransmitter, Call paramCall, EventListener paramEventListener, ExchangeFinder paramExchangeFinder, ExchangeCodec paramExchangeCodec) {
    this.transmitter = paramTransmitter;
    this.call = paramCall;
    this.eventListener = paramEventListener;
    this.finder = paramExchangeFinder;
    this.codec = paramExchangeCodec;
  }
  
  @Nullable
  public IOException bodyComplete(long paramLong, boolean paramBoolean1, boolean paramBoolean2, @Nullable IOException paramIOException) {
    if (paramIOException != null)
      trackFailure(paramIOException); 
    if (paramBoolean2)
      if (paramIOException != null) {
        this.eventListener.requestFailed(this.call, paramIOException);
      } else {
        this.eventListener.requestBodyEnd(this.call, paramLong);
      }  
    if (paramBoolean1)
      if (paramIOException != null) {
        this.eventListener.responseFailed(this.call, paramIOException);
      } else {
        this.eventListener.responseBodyEnd(this.call, paramLong);
      }  
    return this.transmitter.exchangeMessageDone(this, paramBoolean2, paramBoolean1, paramIOException);
  }
  
  public void cancel() {
    this.codec.cancel();
  }
  
  public RealConnection connection() {
    return this.codec.connection();
  }
  
  public I11li1 createRequestBody(Request paramRequest, boolean paramBoolean) throws IOException {
    this.duplex = paramBoolean;
    long l = paramRequest.body().contentLength();
    this.eventListener.requestBodyStart(this.call);
    return new RequestBodySink(this.codec.createRequestBody(paramRequest, l), l);
  }
  
  public void detachWithViolence() {
    this.codec.cancel();
    this.transmitter.exchangeMessageDone(this, true, true, null);
  }
  
  public void finishRequest() throws IOException {
    try {
      this.codec.finishRequest();
      return;
    } catch (IOException iOException) {
      this.eventListener.requestFailed(this.call, iOException);
      trackFailure(iOException);
      throw iOException;
    } 
  }
  
  public void flushRequest() throws IOException {
    try {
      this.codec.flushRequest();
      return;
    } catch (IOException iOException) {
      this.eventListener.requestFailed(this.call, iOException);
      trackFailure(iOException);
      throw iOException;
    } 
  }
  
  public boolean isDuplex() {
    return this.duplex;
  }
  
  public RealWebSocket.Streams newWebSocketStreams() throws SocketException {
    this.transmitter.timeoutEarlyExit();
    return this.codec.connection().newWebSocketStreams(this);
  }
  
  public void noNewExchangesOnConnection() {
    this.codec.connection().noNewExchanges();
  }
  
  public void noRequestBody() {
    this.transmitter.exchangeMessageDone(this, true, false, null);
  }
  
  public ResponseBody openResponseBody(Response paramResponse) throws IOException {
    try {
      this.eventListener.responseBodyStart(this.call);
      String str = paramResponse.header("Content-Type");
      long l = this.codec.reportedContentLength(paramResponse);
      丨lL 丨lL = this.codec.openResponseBodySource(paramResponse);
      ResponseBodySource responseBodySource = new ResponseBodySource();
      this(this, 丨lL, l);
      return new RealResponseBody(str, l, lIi丨I.I丨L(responseBodySource));
    } catch (IOException iOException) {
      this.eventListener.responseFailed(this.call, iOException);
      trackFailure(iOException);
      throw iOException;
    } 
  }
  
  @Nullable
  public Response.Builder readResponseHeaders(boolean paramBoolean) throws IOException {
    try {
      Response.Builder builder = this.codec.readResponseHeaders(paramBoolean);
      if (builder != null)
        Internal.instance.initExchange(builder, this); 
      return builder;
    } catch (IOException iOException) {
      this.eventListener.responseFailed(this.call, iOException);
      trackFailure(iOException);
      throw iOException;
    } 
  }
  
  public void responseHeadersEnd(Response paramResponse) {
    this.eventListener.responseHeadersEnd(this.call, paramResponse);
  }
  
  public void responseHeadersStart() {
    this.eventListener.responseHeadersStart(this.call);
  }
  
  public void timeoutEarlyExit() {
    this.transmitter.timeoutEarlyExit();
  }
  
  public void trackFailure(IOException paramIOException) {
    this.finder.trackFailure();
    this.codec.connection().trackFailure(paramIOException);
  }
  
  public Headers trailers() throws IOException {
    return this.codec.trailers();
  }
  
  public void webSocketUpgradeFailed() {
    bodyComplete(-1L, true, true, null);
  }
  
  public void writeRequestHeaders(Request paramRequest) throws IOException {
    try {
      this.eventListener.requestHeadersStart(this.call);
      this.codec.writeRequestHeaders(paramRequest);
      this.eventListener.requestHeadersEnd(this.call, paramRequest);
      return;
    } catch (IOException iOException) {
      this.eventListener.requestFailed(this.call, iOException);
      trackFailure(iOException);
      throw iOException;
    } 
  }
  
  public final class RequestBodySink extends I丨iL {
    private long bytesReceived;
    
    private boolean closed;
    
    private boolean completed;
    
    private long contentLength;
    
    public final Exchange this$0;
    
    public RequestBodySink(I11li1 param1I11li1, long param1Long) {
      super(param1I11li1);
      this.contentLength = param1Long;
    }
    
    @Nullable
    private IOException complete(@Nullable IOException param1IOException) {
      if (this.completed)
        return param1IOException; 
      this.completed = true;
      return Exchange.this.bodyComplete(this.bytesReceived, false, true, param1IOException);
    }
    
    public void close() throws IOException {
      if (this.closed)
        return; 
      this.closed = true;
      long l = this.contentLength;
      if (l == -1L || this.bytesReceived == l)
        try {
          super.close();
          complete(null);
          return;
        } catch (IOException iOException) {
          throw complete(iOException);
        }  
      throw new ProtocolException("unexpected end of stream");
    }
    
    public void flush() throws IOException {
      try {
        super.flush();
        return;
      } catch (IOException iOException) {
        throw complete(iOException);
      } 
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      if (!this.closed) {
        long l = this.contentLength;
        if (l == -1L || this.bytesReceived + param1Long <= l)
          try {
            super.write(param1I1I, param1Long);
            this.bytesReceived += param1Long;
            return;
          } catch (IOException iOException) {
            throw complete(iOException);
          }  
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("expected ");
        stringBuilder.append(this.contentLength);
        stringBuilder.append(" bytes but received ");
        stringBuilder.append(this.bytesReceived + param1Long);
        throw new ProtocolException(stringBuilder.toString());
      } 
      throw new IllegalStateException("closed");
    }
  }
  
  public final class ResponseBodySource extends L丨1丨1丨I {
    private long bytesReceived;
    
    private boolean closed;
    
    private boolean completed;
    
    private final long contentLength;
    
    public final Exchange this$0;
    
    public ResponseBodySource(丨lL param1丨lL, long param1Long) {
      super(param1丨lL);
      this.contentLength = param1Long;
      if (param1Long == 0L)
        complete(null); 
    }
    
    public void close() throws IOException {
      if (this.closed)
        return; 
      this.closed = true;
      try {
        super.close();
        complete(null);
        return;
      } catch (IOException iOException) {
        throw complete(iOException);
      } 
    }
    
    @Nullable
    public IOException complete(@Nullable IOException param1IOException) {
      if (this.completed)
        return param1IOException; 
      this.completed = true;
      return Exchange.this.bodyComplete(this.bytesReceived, true, false, param1IOException);
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      if (!this.closed)
        try {
          long l2 = delegate().read(param1I1I, param1Long);
          if (l2 == -1L) {
            complete(null);
            return -1L;
          } 
          param1Long = this.bytesReceived + l2;
          long l1 = this.contentLength;
          if (l1 == -1L || param1Long <= l1) {
            this.bytesReceived = param1Long;
            if (param1Long == l1)
              complete(null); 
            return l2;
          } 
          ProtocolException protocolException = new ProtocolException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("expected ");
          stringBuilder.append(this.contentLength);
          stringBuilder.append(" bytes but received ");
          stringBuilder.append(param1Long);
          this(stringBuilder.toString());
          throw protocolException;
        } catch (IOException iOException) {
          throw complete(iOException);
        }  
      throw new IllegalStateException("closed");
    }
  }
}
