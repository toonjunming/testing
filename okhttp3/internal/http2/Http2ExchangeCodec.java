package okhttp3.internal.http2;

import I丨L.I11L;
import I丨L.I11li1;
import I丨L.丨lL;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;

public final class Http2ExchangeCodec implements ExchangeCodec {
  private static final String CONNECTION = "connection";
  
  private static final String ENCODING = "encoding";
  
  private static final String HOST = "host";
  
  private static final List<String> HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList(new String[] { 
        "connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade", ":method", ":path", 
        ":scheme", ":authority" });
  
  private static final List<String> HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList(new String[] { "connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade" });
  
  private static final String KEEP_ALIVE = "keep-alive";
  
  private static final String PROXY_CONNECTION = "proxy-connection";
  
  private static final String TE = "te";
  
  private static final String TRANSFER_ENCODING = "transfer-encoding";
  
  private static final String UPGRADE = "upgrade";
  
  private volatile boolean canceled;
  
  private final Interceptor.Chain chain;
  
  private final Http2Connection connection;
  
  private final Protocol protocol;
  
  private final RealConnection realConnection;
  
  private volatile Http2Stream stream;
  
  public Http2ExchangeCodec(OkHttpClient paramOkHttpClient, RealConnection paramRealConnection, Interceptor.Chain paramChain, Http2Connection paramHttp2Connection) {
    this.realConnection = paramRealConnection;
    this.chain = paramChain;
    this.connection = paramHttp2Connection;
    List<Protocol> list = paramOkHttpClient.protocols();
    Protocol protocol = Protocol.H2_PRIOR_KNOWLEDGE;
    if (!list.contains(protocol))
      protocol = Protocol.HTTP_2; 
    this.protocol = protocol;
  }
  
  public static List<Header> http2HeadersList(Request paramRequest) {
    Headers headers = paramRequest.headers();
    ArrayList<Header> arrayList = new ArrayList(headers.size() + 4);
    arrayList.add(new Header(Header.TARGET_METHOD, paramRequest.method()));
    arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(paramRequest.url())));
    String str = paramRequest.header("Host");
    if (str != null)
      arrayList.add(new Header(Header.TARGET_AUTHORITY, str)); 
    arrayList.add(new Header(Header.TARGET_SCHEME, paramRequest.url().scheme()));
    byte b = 0;
    int i = headers.size();
    while (b < i) {
      String str1 = headers.name(b).toLowerCase(Locale.US);
      if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(str1) || (str1.equals("te") && headers.value(b).equals("trailers")))
        arrayList.add(new Header(str1, headers.value(b))); 
      b++;
    } 
    return arrayList;
  }
  
  public static Response.Builder readHttp2HeadersList(Headers paramHeaders, Protocol paramProtocol) throws IOException {
    Headers.Builder builder = new Headers.Builder();
    int i = paramHeaders.size();
    Object object = null;
    byte b = 0;
    while (b < i) {
      Object object1;
      String str2 = paramHeaders.name(b);
      String str1 = paramHeaders.value(b);
      if (str2.equals(":status")) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 ");
        stringBuilder.append(str1);
        object1 = StatusLine.parse(stringBuilder.toString());
      } else {
        object1 = object;
        if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(str2)) {
          Internal.instance.addLenient(builder, str2, str1);
          object1 = object;
        } 
      } 
      b++;
      object = object1;
    } 
    if (object != null)
      return (new Response.Builder()).protocol(paramProtocol).code(((StatusLine)object).code).message(((StatusLine)object).message).headers(builder.build()); 
    throw new ProtocolException("Expected ':status' header not present");
  }
  
  public void cancel() {
    this.canceled = true;
    if (this.stream != null)
      this.stream.closeLater(ErrorCode.CANCEL); 
  }
  
  public RealConnection connection() {
    return this.realConnection;
  }
  
  public I11li1 createRequestBody(Request paramRequest, long paramLong) {
    return this.stream.getSink();
  }
  
  public void finishRequest() throws IOException {
    this.stream.getSink().close();
  }
  
  public void flushRequest() throws IOException {
    this.connection.flush();
  }
  
  public 丨lL openResponseBodySource(Response paramResponse) {
    return this.stream.getSource();
  }
  
  public Response.Builder readResponseHeaders(boolean paramBoolean) throws IOException {
    Response.Builder builder = readHttp2HeadersList(this.stream.takeHeaders(), this.protocol);
    return (paramBoolean && Internal.instance.code(builder) == 100) ? null : builder;
  }
  
  public long reportedContentLength(Response paramResponse) {
    return HttpHeaders.contentLength(paramResponse);
  }
  
  public Headers trailers() throws IOException {
    return this.stream.trailers();
  }
  
  public void writeRequestHeaders(Request paramRequest) throws IOException {
    boolean bool;
    if (this.stream != null)
      return; 
    if (paramRequest.body() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    List<Header> list = http2HeadersList(paramRequest);
    this.stream = this.connection.newStream(list, bool);
    if (!this.canceled) {
      I11L i11L = this.stream.readTimeout();
      long l = this.chain.readTimeoutMillis();
      TimeUnit timeUnit = TimeUnit.MILLISECONDS;
      i11L.timeout(l, timeUnit);
      this.stream.writeTimeout().timeout(this.chain.writeTimeoutMillis(), timeUnit);
      return;
    } 
    this.stream.closeLater(ErrorCode.CANCEL);
    throw new IOException("Canceled");
  }
}
