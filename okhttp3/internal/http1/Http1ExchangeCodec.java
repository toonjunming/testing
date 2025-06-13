package okhttp3.internal.http1;

import I丨L.I11L;
import I丨L.I11li1;
import I丨L.I1I;
import I丨L.I丨L;
import I丨L.l丨Li1LL;
import I丨L.丨il;
import I丨L.丨lL;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;

public final class Http1ExchangeCodec implements ExchangeCodec {
  private static final int HEADER_LIMIT = 262144;
  
  private static final int STATE_CLOSED = 6;
  
  private static final int STATE_IDLE = 0;
  
  private static final int STATE_OPEN_REQUEST_BODY = 1;
  
  private static final int STATE_OPEN_RESPONSE_BODY = 4;
  
  private static final int STATE_READING_RESPONSE_BODY = 5;
  
  private static final int STATE_READ_RESPONSE_HEADERS = 3;
  
  private static final int STATE_WRITING_REQUEST_BODY = 2;
  
  private final OkHttpClient client;
  
  private long headerLimit = 262144L;
  
  private final RealConnection realConnection;
  
  private final I丨L sink;
  
  private final l丨Li1LL source;
  
  private int state = 0;
  
  private Headers trailers;
  
  public Http1ExchangeCodec(OkHttpClient paramOkHttpClient, RealConnection paramRealConnection, l丨Li1LL paraml丨Li1LL, I丨L paramI丨L) {
    this.client = paramOkHttpClient;
    this.realConnection = paramRealConnection;
    this.source = paraml丨Li1LL;
    this.sink = paramI丨L;
  }
  
  private void detachTimeout(丨il param丨il) {
    I11L i11L = param丨il.IL1Iii();
    param丨il.ILil(I11L.NONE);
    i11L.clearDeadline();
    i11L.clearTimeout();
  }
  
  private I11li1 newChunkedSink() {
    if (this.state == 1) {
      this.state = 2;
      return new ChunkedSink();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private 丨lL newChunkedSource(HttpUrl paramHttpUrl) {
    if (this.state == 4) {
      this.state = 5;
      return new ChunkedSource(paramHttpUrl);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private 丨lL newFixedLengthSource(long paramLong) {
    if (this.state == 4) {
      this.state = 5;
      return new FixedLengthSource(paramLong);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private I11li1 newKnownLengthSink() {
    if (this.state == 1) {
      this.state = 2;
      return new KnownLengthSink();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private 丨lL newUnknownLengthSource() {
    if (this.state == 4) {
      this.state = 5;
      this.realConnection.noNewExchanges();
      return new UnknownLengthSource();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private String readHeaderLine() throws IOException {
    String str = this.source.丨il(this.headerLimit);
    this.headerLimit -= str.length();
    return str;
  }
  
  private Headers readHeaders() throws IOException {
    Headers.Builder builder = new Headers.Builder();
    while (true) {
      String str = readHeaderLine();
      if (str.length() != 0) {
        Internal.instance.addLenient(builder, str);
        continue;
      } 
      return builder.build();
    } 
  }
  
  public void cancel() {
    RealConnection realConnection = this.realConnection;
    if (realConnection != null)
      realConnection.cancel(); 
  }
  
  public RealConnection connection() {
    return this.realConnection;
  }
  
  public I11li1 createRequestBody(Request paramRequest, long paramLong) throws IOException {
    if (paramRequest.body() == null || !paramRequest.body().isDuplex()) {
      if ("chunked".equalsIgnoreCase(paramRequest.header("Transfer-Encoding")))
        return newChunkedSink(); 
      if (paramLong != -1L)
        return newKnownLengthSink(); 
      throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    } 
    throw new ProtocolException("Duplex connections are not supported for HTTP/1");
  }
  
  public void finishRequest() throws IOException {
    this.sink.flush();
  }
  
  public void flushRequest() throws IOException {
    this.sink.flush();
  }
  
  public boolean isClosed() {
    boolean bool;
    if (this.state == 6) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public 丨lL openResponseBodySource(Response paramResponse) {
    if (!HttpHeaders.hasBody(paramResponse))
      return newFixedLengthSource(0L); 
    if ("chunked".equalsIgnoreCase(paramResponse.header("Transfer-Encoding")))
      return newChunkedSource(paramResponse.request().url()); 
    long l = HttpHeaders.contentLength(paramResponse);
    return (l != -1L) ? newFixedLengthSource(l) : newUnknownLengthSource();
  }
  
  public Response.Builder readResponseHeaders(boolean paramBoolean) throws IOException {
    int i = this.state;
    if (i == 1 || i == 3)
      try {
        StatusLine statusLine = StatusLine.parse(readHeaderLine());
        Response.Builder builder = new Response.Builder();
        this();
        builder = builder.protocol(statusLine.protocol).code(statusLine.code).message(statusLine.message).headers(readHeaders());
        if (paramBoolean && statusLine.code == 100)
          return null; 
        if (statusLine.code == 100) {
          this.state = 3;
          return builder;
        } 
        this.state = 4;
        return builder;
      } catch (EOFException eOFException) {
        String str;
        RealConnection realConnection = this.realConnection;
        if (realConnection != null) {
          str = realConnection.route().address().url().redact();
        } else {
          str = "unknown";
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("unexpected end of stream on ");
        stringBuilder1.append(str);
        throw new IOException(stringBuilder1.toString(), eOFException);
      }  
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public long reportedContentLength(Response paramResponse) {
    return !HttpHeaders.hasBody(paramResponse) ? 0L : ("chunked".equalsIgnoreCase(paramResponse.header("Transfer-Encoding")) ? -1L : HttpHeaders.contentLength(paramResponse));
  }
  
  public void skipConnectBody(Response paramResponse) throws IOException {
    long l = HttpHeaders.contentLength(paramResponse);
    if (l == -1L)
      return; 
    丨lL 丨lL = newFixedLengthSource(l);
    Util.skipAll(丨lL, 2147483647, TimeUnit.MILLISECONDS);
    丨lL.close();
  }
  
  public Headers trailers() {
    if (this.state == 6) {
      Headers headers = this.trailers;
      if (headers == null)
        headers = Util.EMPTY_HEADERS; 
      return headers;
    } 
    throw new IllegalStateException("too early; can't read the trailers yet");
  }
  
  public void writeRequest(Headers paramHeaders, String paramString) throws IOException {
    if (this.state == 0) {
      this.sink.ILL(paramString).ILL("\r\n");
      byte b = 0;
      int i = paramHeaders.size();
      while (b < i) {
        this.sink.ILL(paramHeaders.name(b)).ILL(": ").ILL(paramHeaders.value(b)).ILL("\r\n");
        b++;
      } 
      this.sink.ILL("\r\n");
      this.state = 1;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state: ");
    stringBuilder.append(this.state);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void writeRequestHeaders(Request paramRequest) throws IOException {
    String str = RequestLine.get(paramRequest, this.realConnection.route().proxy().type());
    writeRequest(paramRequest.headers(), str);
  }
  
  public abstract class AbstractSource implements 丨lL {
    public boolean closed;
    
    public final Http1ExchangeCodec this$0;
    
    public final 丨il timeout = new 丨il(Http1ExchangeCodec.this.source.timeout());
    
    private AbstractSource() {}
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      try {
        return Http1ExchangeCodec.this.source.read(param1I1I, param1Long);
      } catch (IOException iOException) {
        Http1ExchangeCodec.this.realConnection.noNewExchanges();
        responseBodyComplete();
        throw iOException;
      } 
    }
    
    public final void responseBodyComplete() {
      if (Http1ExchangeCodec.this.state == 6)
        return; 
      if (Http1ExchangeCodec.this.state == 5) {
        Http1ExchangeCodec.this.detachTimeout(this.timeout);
        Http1ExchangeCodec.access$402(Http1ExchangeCodec.this, 6);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("state: ");
      stringBuilder.append(Http1ExchangeCodec.this.state);
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    public I11L timeout() {
      return this.timeout;
    }
  }
  
  public final class ChunkedSink implements I11li1 {
    private boolean closed;
    
    public final Http1ExchangeCodec this$0;
    
    private final 丨il timeout = new 丨il(Http1ExchangeCodec.this.sink.timeout());
    
    public void close() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: istore_1
      //   7: iload_1
      //   8: ifeq -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_0
      //   15: iconst_1
      //   16: putfield closed : Z
      //   19: aload_0
      //   20: getfield this$0 : Lokhttp3/internal/http1/Http1ExchangeCodec;
      //   23: invokestatic access$200 : (Lokhttp3/internal/http1/Http1ExchangeCodec;)LI丨L/I丨L;
      //   26: ldc '0\\r\\n\\r\\n'
      //   28: invokeinterface ILL : (Ljava/lang/String;)LI丨L/I丨L;
      //   33: pop
      //   34: aload_0
      //   35: getfield this$0 : Lokhttp3/internal/http1/Http1ExchangeCodec;
      //   38: aload_0
      //   39: getfield timeout : LI丨L/丨il;
      //   42: invokestatic access$300 : (Lokhttp3/internal/http1/Http1ExchangeCodec;LI丨L/丨il;)V
      //   45: aload_0
      //   46: getfield this$0 : Lokhttp3/internal/http1/Http1ExchangeCodec;
      //   49: iconst_3
      //   50: invokestatic access$402 : (Lokhttp3/internal/http1/Http1ExchangeCodec;I)I
      //   53: pop
      //   54: aload_0
      //   55: monitorexit
      //   56: return
      //   57: astore_2
      //   58: aload_0
      //   59: monitorexit
      //   60: aload_2
      //   61: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	57	finally
      //   14	54	57	finally
    }
    
    public void flush() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: istore_1
      //   7: iload_1
      //   8: ifeq -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_0
      //   15: getfield this$0 : Lokhttp3/internal/http1/Http1ExchangeCodec;
      //   18: invokestatic access$200 : (Lokhttp3/internal/http1/Http1ExchangeCodec;)LI丨L/I丨L;
      //   21: invokeinterface flush : ()V
      //   26: aload_0
      //   27: monitorexit
      //   28: return
      //   29: astore_2
      //   30: aload_0
      //   31: monitorexit
      //   32: aload_2
      //   33: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	29	finally
      //   14	26	29	finally
    }
    
    public I11L timeout() {
      return this.timeout;
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      if (!this.closed) {
        if (param1Long == 0L)
          return; 
        Http1ExchangeCodec.this.sink.IL丨丨l(param1Long);
        Http1ExchangeCodec.this.sink.ILL("\r\n");
        Http1ExchangeCodec.this.sink.write(param1I1I, param1Long);
        Http1ExchangeCodec.this.sink.ILL("\r\n");
        return;
      } 
      throw new IllegalStateException("closed");
    }
  }
  
  public class ChunkedSource extends AbstractSource {
    private static final long NO_CHUNK_YET = -1L;
    
    private long bytesRemainingInChunk = -1L;
    
    private boolean hasMoreChunks = true;
    
    public final Http1ExchangeCodec this$0;
    
    private final HttpUrl url;
    
    public ChunkedSource(HttpUrl param1HttpUrl) {
      this.url = param1HttpUrl;
    }
    
    private void readChunkSize() throws IOException {
      if (this.bytesRemainingInChunk != -1L)
        Http1ExchangeCodec.this.source.LlLI1(); 
      try {
        this.bytesRemainingInChunk = Http1ExchangeCodec.this.source.lI丨II();
        String str = Http1ExchangeCodec.this.source.LlLI1().trim();
        if (this.bytesRemainingInChunk >= 0L) {
          if (!str.isEmpty()) {
            boolean bool = str.startsWith(";");
            if (bool) {
              if (this.bytesRemainingInChunk == 0L) {
                this.hasMoreChunks = false;
                Http1ExchangeCodec http1ExchangeCodec = Http1ExchangeCodec.this;
                Http1ExchangeCodec.access$802(http1ExchangeCodec, http1ExchangeCodec.readHeaders());
                HttpHeaders.receiveHeaders(Http1ExchangeCodec.this.client.cookieJar(), this.url, Http1ExchangeCodec.this.trailers);
                responseBodyComplete();
              } 
              return;
            } 
            ProtocolException protocolException = new ProtocolException();
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("expected chunk size and optional extensions but was \"");
            stringBuilder.append(this.bytesRemainingInChunk);
            stringBuilder.append(str);
            stringBuilder.append("\"");
            this(stringBuilder.toString());
            throw protocolException;
          } 
        } else {
          ProtocolException protocolException = new ProtocolException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("expected chunk size and optional extensions but was \"");
          stringBuilder.append(this.bytesRemainingInChunk);
          stringBuilder.append(str);
          stringBuilder.append("\"");
          this(stringBuilder.toString());
          throw protocolException;
        } 
        if (this.bytesRemainingInChunk == 0L) {
          this.hasMoreChunks = false;
          Http1ExchangeCodec http1ExchangeCodec = Http1ExchangeCodec.this;
          Http1ExchangeCodec.access$802(http1ExchangeCodec, http1ExchangeCodec.readHeaders());
          HttpHeaders.receiveHeaders(Http1ExchangeCodec.this.client.cookieJar(), this.url, Http1ExchangeCodec.this.trailers);
          responseBodyComplete();
        } 
        return;
      } catch (NumberFormatException numberFormatException) {
        throw new ProtocolException(numberFormatException.getMessage());
      } 
    }
    
    public void close() throws IOException {
      if (this.closed)
        return; 
      if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
        Http1ExchangeCodec.this.realConnection.noNewExchanges();
        responseBodyComplete();
      } 
      this.closed = true;
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      if (param1Long >= 0L) {
        if (!this.closed) {
          if (!this.hasMoreChunks)
            return -1L; 
          long l = this.bytesRemainingInChunk;
          if (l == 0L || l == -1L) {
            readChunkSize();
            if (!this.hasMoreChunks)
              return -1L; 
          } 
          param1Long = super.read(param1I1I, Math.min(param1Long, this.bytesRemainingInChunk));
          if (param1Long != -1L) {
            this.bytesRemainingInChunk -= param1Long;
            return param1Long;
          } 
          Http1ExchangeCodec.this.realConnection.noNewExchanges();
          ProtocolException protocolException = new ProtocolException("unexpected end of stream");
          responseBodyComplete();
          throw protocolException;
        } 
        throw new IllegalStateException("closed");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(param1Long);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  public class FixedLengthSource extends AbstractSource {
    private long bytesRemaining;
    
    public final Http1ExchangeCodec this$0;
    
    public FixedLengthSource(long param1Long) {
      this.bytesRemaining = param1Long;
      if (param1Long == 0L)
        responseBodyComplete(); 
    }
    
    public void close() throws IOException {
      if (this.closed)
        return; 
      if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
        Http1ExchangeCodec.this.realConnection.noNewExchanges();
        responseBodyComplete();
      } 
      this.closed = true;
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      if (param1Long >= 0L) {
        if (!this.closed) {
          long l = this.bytesRemaining;
          if (l == 0L)
            return -1L; 
          param1Long = super.read(param1I1I, Math.min(l, param1Long));
          if (param1Long != -1L) {
            l = this.bytesRemaining - param1Long;
            this.bytesRemaining = l;
            if (l == 0L)
              responseBodyComplete(); 
            return param1Long;
          } 
          Http1ExchangeCodec.this.realConnection.noNewExchanges();
          ProtocolException protocolException = new ProtocolException("unexpected end of stream");
          responseBodyComplete();
          throw protocolException;
        } 
        throw new IllegalStateException("closed");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(param1Long);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  public final class KnownLengthSink implements I11li1 {
    private boolean closed;
    
    public final Http1ExchangeCodec this$0;
    
    private final 丨il timeout = new 丨il(Http1ExchangeCodec.this.sink.timeout());
    
    private KnownLengthSink() {}
    
    public void close() throws IOException {
      if (this.closed)
        return; 
      this.closed = true;
      Http1ExchangeCodec.this.detachTimeout(this.timeout);
      Http1ExchangeCodec.access$402(Http1ExchangeCodec.this, 3);
    }
    
    public void flush() throws IOException {
      if (this.closed)
        return; 
      Http1ExchangeCodec.this.sink.flush();
    }
    
    public I11L timeout() {
      return this.timeout;
    }
    
    public void write(I1I param1I1I, long param1Long) throws IOException {
      if (!this.closed) {
        Util.checkOffsetAndCount(param1I1I.iI1i丨I(), 0L, param1Long);
        Http1ExchangeCodec.this.sink.write(param1I1I, param1Long);
        return;
      } 
      throw new IllegalStateException("closed");
    }
  }
  
  public class UnknownLengthSource extends AbstractSource {
    private boolean inputExhausted;
    
    public final Http1ExchangeCodec this$0;
    
    private UnknownLengthSource() {}
    
    public void close() throws IOException {
      if (this.closed)
        return; 
      if (!this.inputExhausted)
        responseBodyComplete(); 
      this.closed = true;
    }
    
    public long read(I1I param1I1I, long param1Long) throws IOException {
      if (param1Long >= 0L) {
        if (!this.closed) {
          if (this.inputExhausted)
            return -1L; 
          param1Long = super.read(param1I1I, param1Long);
          if (param1Long == -1L) {
            this.inputExhausted = true;
            responseBodyComplete();
            return -1L;
          } 
          return param1Long;
        } 
        throw new IllegalStateException("closed");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(param1Long);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
}
