package okhttp3;

import I丨L.I11li1;
import I丨L.I1I;
import I丨L.I丨L;
import I丨L.I丨iL;
import I丨L.L丨1丨1丨I;
import I丨L.iI丨LLL1;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;

public final class Cache implements Closeable, Flushable {
  private static final int ENTRY_BODY = 1;
  
  private static final int ENTRY_COUNT = 2;
  
  private static final int ENTRY_METADATA = 0;
  
  private static final int VERSION = 201105;
  
  public final DiskLruCache cache;
  
  private int hitCount;
  
  public final InternalCache internalCache = new InternalCache() {
      public final Cache this$0;
      
      @Nullable
      public Response get(Request param1Request) throws IOException {
        return Cache.this.get(param1Request);
      }
      
      @Nullable
      public CacheRequest put(Response param1Response) throws IOException {
        return Cache.this.put(param1Response);
      }
      
      public void remove(Request param1Request) throws IOException {
        Cache.this.remove(param1Request);
      }
      
      public void trackConditionalCacheHit() {
        Cache.this.trackConditionalCacheHit();
      }
      
      public void trackResponse(CacheStrategy param1CacheStrategy) {
        Cache.this.trackResponse(param1CacheStrategy);
      }
      
      public void update(Response param1Response1, Response param1Response2) {
        Cache.this.update(param1Response1, param1Response2);
      }
    };
  
  private int networkCount;
  
  private int requestCount;
  
  public int writeAbortCount;
  
  public int writeSuccessCount;
  
  public Cache(File paramFile, long paramLong) {
    this(paramFile, paramLong, FileSystem.SYSTEM);
  }
  
  public Cache(File paramFile, long paramLong, FileSystem paramFileSystem) {
    this.cache = DiskLruCache.create(paramFileSystem, paramFile, 201105, 2, paramLong);
  }
  
  private void abortQuietly(@Nullable DiskLruCache.Editor paramEditor) {
    if (paramEditor != null)
      try {
        paramEditor.abort();
      } catch (IOException iOException) {} 
  }
  
  public static String key(HttpUrl paramHttpUrl) {
    return iI丨LLL1.encodeUtf8(paramHttpUrl.toString()).md5().hex();
  }
  
  public static int readInt(l丨Li1LL paraml丨Li1LL) throws IOException {
    try {
      long l = paraml丨Li1LL.L丨1丨1丨I();
      String str = paraml丨Li1LL.LlLI1();
      if (l >= 0L && l <= 2147483647L && str.isEmpty())
        return (int)l; 
      IOException iOException = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("expected an int but was \"");
      stringBuilder.append(l);
      stringBuilder.append(str);
      stringBuilder.append("\"");
      this(stringBuilder.toString());
      throw iOException;
    } catch (NumberFormatException numberFormatException) {
      throw new IOException(numberFormatException.getMessage());
    } 
  }
  
  public void close() throws IOException {
    this.cache.close();
  }
  
  public void delete() throws IOException {
    this.cache.delete();
  }
  
  public File directory() {
    return this.cache.getDirectory();
  }
  
  public void evictAll() throws IOException {
    this.cache.evictAll();
  }
  
  public void flush() throws IOException {
    this.cache.flush();
  }
  
  @Nullable
  public Response get(Request paramRequest) {
    String str = key(paramRequest.url());
    try {
      Response response;
      DiskLruCache.Snapshot snapshot = this.cache.get(str);
      if (snapshot == null)
        return null; 
      try {
        Entry entry = new Entry(snapshot.getSource(0));
        response = entry.response(snapshot);
        if (!entry.matches(paramRequest, response)) {
          Util.closeQuietly(response.body());
          return null;
        } 
        return response;
      } catch (IOException iOException) {
        Util.closeQuietly(response);
      } 
    } catch (IOException iOException) {}
    return null;
  }
  
  public int hitCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hitCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void initialize() throws IOException {
    this.cache.initialize();
  }
  
  public boolean isClosed() {
    return this.cache.isClosed();
  }
  
  public long maxSize() {
    return this.cache.getMaxSize();
  }
  
  public int networkCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield networkCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  @Nullable
  public CacheRequest put(Response paramResponse) {
    String str = paramResponse.request().method();
    if (HttpMethod.invalidatesCache(paramResponse.request().method())) {
      try {
        remove(paramResponse.request());
      } catch (IOException null) {}
      return null;
    } 
    if (!str.equals("GET"))
      return null; 
    if (HttpHeaders.hasVaryAll((Response)iOException))
      return null; 
    Entry entry = new Entry((Response)iOException);
    try {
      DiskLruCache.Editor editor = this.cache.edit(key(iOException.request().url()));
      if (editor == null)
        return null; 
      try {
        entry.writeTo(editor);
        return new CacheRequestImpl(editor);
      } catch (IOException iOException1) {}
    } catch (IOException iOException) {
      iOException = null;
    } 
    abortQuietly((DiskLruCache.Editor)iOException);
    return null;
  }
  
  public void remove(Request paramRequest) throws IOException {
    this.cache.remove(key(paramRequest.url()));
  }
  
  public int requestCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public long size() throws IOException {
    return this.cache.size();
  }
  
  public void trackConditionalCacheHit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield hitCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield hitCount : I
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  public void trackResponse(CacheStrategy paramCacheStrategy) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield requestCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield requestCount : I
    //   12: aload_1
    //   13: getfield networkRequest : Lokhttp3/Request;
    //   16: ifnull -> 32
    //   19: aload_0
    //   20: aload_0
    //   21: getfield networkCount : I
    //   24: iconst_1
    //   25: iadd
    //   26: putfield networkCount : I
    //   29: goto -> 49
    //   32: aload_1
    //   33: getfield cacheResponse : Lokhttp3/Response;
    //   36: ifnull -> 49
    //   39: aload_0
    //   40: aload_0
    //   41: getfield hitCount : I
    //   44: iconst_1
    //   45: iadd
    //   46: putfield hitCount : I
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	52	finally
    //   32	49	52	finally
  }
  
  public void update(Response paramResponse1, Response paramResponse2) {
    Entry entry = new Entry(paramResponse2);
    DiskLruCache.Snapshot snapshot = ((CacheResponseBody)paramResponse1.body()).snapshot;
    try {
      DiskLruCache.Editor editor = snapshot.edit();
      if (editor != null) {
        try {
          entry.writeTo(editor);
          editor.commit();
          return;
        } catch (IOException iOException1) {}
      } else {
        return;
      } 
    } catch (IOException iOException) {
      iOException = null;
    } 
    abortQuietly((DiskLruCache.Editor)iOException);
  }
  
  public Iterator<String> urls() throws IOException {
    return new Iterator<String>() {
        public boolean canRemove;
        
        public final Iterator<DiskLruCache.Snapshot> delegate = Cache.this.cache.snapshots();
        
        @Nullable
        public String nextUrl;
        
        public final Cache this$0;
        
        public boolean hasNext() {
          if (this.nextUrl != null)
            return true; 
          this.canRemove = false;
          while (this.delegate.hasNext()) {
            try {
              DiskLruCache.Snapshot snapshot = this.delegate.next();
              try {
                this.nextUrl = lIi丨I.I丨L(snapshot.getSource(0)).LlLI1();
                return true;
              } finally {
                Exception exception = null;
              } 
            } catch (IOException iOException) {}
          } 
          return false;
        }
        
        public String next() {
          if (hasNext()) {
            String str = this.nextUrl;
            this.nextUrl = null;
            this.canRemove = true;
            return str;
          } 
          throw new NoSuchElementException();
        }
        
        public void remove() {
          if (this.canRemove) {
            this.delegate.remove();
            return;
          } 
          throw new IllegalStateException("remove() before next()");
        }
      };
  }
  
  public int writeAbortCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield writeAbortCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int writeSuccessCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield writeSuccessCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final class CacheRequestImpl implements CacheRequest {
    private I11li1 body;
    
    private I11li1 cacheOut;
    
    public boolean done;
    
    private final DiskLruCache.Editor editor;
    
    public final Cache this$0;
    
    public CacheRequestImpl(final DiskLruCache.Editor editor) {
      this.editor = editor;
      I11li1 i11li1 = editor.newSink(1);
      this.cacheOut = i11li1;
      this.body = new I丨iL(i11li1) {
          public final Cache.CacheRequestImpl this$1;
          
          public final DiskLruCache.Editor val$editor;
          
          public final Cache val$this$0;
          
          public void close() throws IOException {
            synchronized (Cache.this) {
              Cache.CacheRequestImpl cacheRequestImpl = Cache.CacheRequestImpl.this;
              if (cacheRequestImpl.done)
                return; 
              cacheRequestImpl.done = true;
              Cache cache = Cache.this;
              cache.writeSuccessCount++;
              super.close();
              editor.commit();
              return;
            } 
          }
        };
    }
    
    public void abort() {
      Cache cache;
      synchronized (Cache.this) {
        if (this.done)
          return; 
        this.done = true;
        Cache cache1 = Cache.this;
        cache1.writeAbortCount++;
        Util.closeQuietly(this.cacheOut);
        try {
          this.editor.abort();
        } catch (IOException iOException) {}
      } 
    }
    
    public I11li1 body() {
      return this.body;
    }
  }
  
  public class null extends I丨iL {
    public final Cache.CacheRequestImpl this$1;
    
    public final DiskLruCache.Editor val$editor;
    
    public final Cache val$this$0;
    
    public null(I11li1 param1I11li1) {
      super(param1I11li1);
    }
    
    public void close() throws IOException {
      synchronized (Cache.this) {
        Cache.CacheRequestImpl cacheRequestImpl = this.this$1;
        if (cacheRequestImpl.done)
          return; 
        cacheRequestImpl.done = true;
        Cache cache = Cache.this;
        cache.writeSuccessCount++;
        super.close();
        editor.commit();
        return;
      } 
    }
  }
  
  public static class CacheResponseBody extends ResponseBody {
    private final l丨Li1LL bodySource;
    
    @Nullable
    private final String contentLength;
    
    @Nullable
    private final String contentType;
    
    public final DiskLruCache.Snapshot snapshot;
    
    public CacheResponseBody(final DiskLruCache.Snapshot snapshot, String param1String1, String param1String2) {
      this.snapshot = snapshot;
      this.contentType = param1String1;
      this.contentLength = param1String2;
      this.bodySource = lIi丨I.I丨L(new L丨1丨1丨I(snapshot.getSource(1)) {
            public final Cache.CacheResponseBody this$0;
            
            public final DiskLruCache.Snapshot val$snapshot;
            
            public void close() throws IOException {
              snapshot.close();
              super.close();
            }
          });
    }
    
    public long contentLength() {
      long l1;
      long l2 = -1L;
      try {
        String str = this.contentLength;
        l1 = l2;
        if (str != null)
          l1 = Long.parseLong(str); 
      } catch (NumberFormatException numberFormatException) {
        l1 = l2;
      } 
      return l1;
    }
    
    public MediaType contentType() {
      String str = this.contentType;
      if (str != null) {
        MediaType mediaType = MediaType.parse(str);
      } else {
        str = null;
      } 
      return (MediaType)str;
    }
    
    public l丨Li1LL source() {
      return this.bodySource;
    }
  }
  
  public class null extends L丨1丨1丨I {
    public final Cache.CacheResponseBody this$0;
    
    public final DiskLruCache.Snapshot val$snapshot;
    
    public null(丨lL param1丨lL) {
      super(param1丨lL);
    }
    
    public void close() throws IOException {
      snapshot.close();
      super.close();
    }
  }
  
  public static final class Entry {
    private static final String RECEIVED_MILLIS;
    
    private static final String SENT_MILLIS;
    
    private final int code;
    
    @Nullable
    private final Handshake handshake;
    
    private final String message;
    
    private final Protocol protocol;
    
    private final long receivedResponseMillis;
    
    private final String requestMethod;
    
    private final Headers responseHeaders;
    
    private final long sentRequestMillis;
    
    private final String url;
    
    private final Headers varyHeaders;
    
    static {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Platform.get().getPrefix());
      stringBuilder.append("-Sent-Millis");
      SENT_MILLIS = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(Platform.get().getPrefix());
      stringBuilder.append("-Received-Millis");
      RECEIVED_MILLIS = stringBuilder.toString();
    }
    
    public Entry(丨lL param1丨lL) throws IOException {
      try {
        l丨Li1LL l丨Li1LL = lIi丨I.I丨L(param1丨lL);
        this.url = l丨Li1LL.LlLI1();
        this.requestMethod = l丨Li1LL.LlLI1();
        Headers.Builder builder1 = new Headers.Builder();
        this();
        int i = Cache.readInt(l丨Li1LL);
        boolean bool = false;
        byte b;
        for (b = 0; b < i; b++)
          builder1.addLenient(l丨Li1LL.LlLI1()); 
        this.varyHeaders = builder1.build();
        StatusLine statusLine = StatusLine.parse(l丨Li1LL.LlLI1());
        this.protocol = statusLine.protocol;
        this.code = statusLine.code;
        this.message = statusLine.message;
        Headers.Builder builder2 = new Headers.Builder();
        this();
        i = Cache.readInt(l丨Li1LL);
        for (b = bool; b < i; b++)
          builder2.addLenient(l丨Li1LL.LlLI1()); 
        String str3 = SENT_MILLIS;
        String str2 = builder2.get(str3);
        String str1 = RECEIVED_MILLIS;
        String str4 = builder2.get(str1);
        builder2.removeAll(str3);
        builder2.removeAll(str1);
        long l2 = 0L;
        if (str2 != null) {
          l1 = Long.parseLong(str2);
        } else {
          l1 = 0L;
        } 
        this.sentRequestMillis = l1;
        long l1 = l2;
        if (str4 != null)
          l1 = Long.parseLong(str4); 
        this.receivedResponseMillis = l1;
        this.responseHeaders = builder2.build();
        if (isHttps()) {
          List<Certificate> list;
          str1 = l丨Li1LL.LlLI1();
          if (str1.length() <= 0) {
            TlsVersion tlsVersion;
            CipherSuite cipherSuite = CipherSuite.forJavaName(l丨Li1LL.LlLI1());
            list = readCertificateList(l丨Li1LL);
            List<Certificate> list1 = readCertificateList(l丨Li1LL);
            if (!l丨Li1LL.l丨Li1LL()) {
              tlsVersion = TlsVersion.forJavaName(l丨Li1LL.LlLI1());
            } else {
              tlsVersion = TlsVersion.SSL_3_0;
            } 
            this.handshake = Handshake.get(tlsVersion, cipherSuite, list, list1);
          } else {
            IOException iOException = new IOException();
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("expected \"\" but was \"");
            stringBuilder.append((String)list);
            stringBuilder.append("\"");
            this(stringBuilder.toString());
            throw iOException;
          } 
        } else {
          this.handshake = null;
        } 
        return;
      } finally {
        param1丨lL.close();
      } 
    }
    
    public Entry(Response param1Response) {
      this.url = param1Response.request().url().toString();
      this.varyHeaders = HttpHeaders.varyHeaders(param1Response);
      this.requestMethod = param1Response.request().method();
      this.protocol = param1Response.protocol();
      this.code = param1Response.code();
      this.message = param1Response.message();
      this.responseHeaders = param1Response.headers();
      this.handshake = param1Response.handshake();
      this.sentRequestMillis = param1Response.sentRequestAtMillis();
      this.receivedResponseMillis = param1Response.receivedResponseAtMillis();
    }
    
    private boolean isHttps() {
      return this.url.startsWith("https://");
    }
    
    private List<Certificate> readCertificateList(l丨Li1LL param1l丨Li1LL) throws IOException {
      int i = Cache.readInt(param1l丨Li1LL);
      if (i == -1)
        return Collections.emptyList(); 
      try {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ArrayList<Certificate> arrayList = new ArrayList();
        this(i);
        for (byte b = 0; b < i; b++) {
          String str = param1l丨Li1LL.LlLI1();
          I1I i1I = new I1I();
          this();
          i1I.iIilII1(iI丨LLL1.decodeBase64(str));
          arrayList.add(certificateFactory.generateCertificate(i1I.LL1IL()));
        } 
        return arrayList;
      } catch (CertificateException certificateException) {
        throw new IOException(certificateException.getMessage());
      } 
    }
    
    private void writeCertList(I丨L param1I丨L, List<Certificate> param1List) throws IOException {
      try {
        param1I丨L.丨l丨(param1List.size()).writeByte(10);
        byte b = 0;
        int i = param1List.size();
        while (b < i) {
          param1I丨L.ILL(iI丨LLL1.of(((Certificate)param1List.get(b)).getEncoded()).base64()).writeByte(10);
          b++;
        } 
        return;
      } catch (CertificateEncodingException certificateEncodingException) {
        throw new IOException(certificateEncodingException.getMessage());
      } 
    }
    
    public boolean matches(Request param1Request, Response param1Response) {
      boolean bool;
      if (this.url.equals(param1Request.url().toString()) && this.requestMethod.equals(param1Request.method()) && HttpHeaders.varyMatches(param1Response, this.varyHeaders, param1Request)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Response response(DiskLruCache.Snapshot param1Snapshot) {
      String str1 = this.responseHeaders.get("Content-Type");
      String str2 = this.responseHeaders.get("Content-Length");
      Request request = (new Request.Builder()).url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
      return (new Response.Builder()).request(request).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new Cache.CacheResponseBody(param1Snapshot, str1, str2)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
    }
    
    public void writeTo(DiskLruCache.Editor param1Editor) throws IOException {
      boolean bool = false;
      I丨L i丨L = lIi丨I.I1I(param1Editor.newSink(0));
      i丨L.ILL(this.url).writeByte(10);
      i丨L.ILL(this.requestMethod).writeByte(10);
      i丨L.丨l丨(this.varyHeaders.size()).writeByte(10);
      int i = this.varyHeaders.size();
      byte b;
      for (b = 0; b < i; b++)
        i丨L.ILL(this.varyHeaders.name(b)).ILL(": ").ILL(this.varyHeaders.value(b)).writeByte(10); 
      i丨L.ILL((new StatusLine(this.protocol, this.code, this.message)).toString()).writeByte(10);
      i丨L.丨l丨((this.responseHeaders.size() + 2)).writeByte(10);
      i = this.responseHeaders.size();
      for (b = bool; b < i; b++)
        i丨L.ILL(this.responseHeaders.name(b)).ILL(": ").ILL(this.responseHeaders.value(b)).writeByte(10); 
      i丨L.ILL(SENT_MILLIS).ILL(": ").丨l丨(this.sentRequestMillis).writeByte(10);
      i丨L.ILL(RECEIVED_MILLIS).ILL(": ").丨l丨(this.receivedResponseMillis).writeByte(10);
      if (isHttps()) {
        i丨L.writeByte(10);
        i丨L.ILL(this.handshake.cipherSuite().javaName()).writeByte(10);
        writeCertList(i丨L, this.handshake.peerCertificates());
        writeCertList(i丨L, this.handshake.localCertificates());
        i丨L.ILL(this.handshake.tlsVersion().javaName()).writeByte(10);
      } 
      i丨L.close();
    }
  }
}
