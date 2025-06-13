package okhttp3;

import I丨L.I1I;
import I丨L.iI丨LLL1;
import I丨L.l丨Li1LL;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.internal.Util;

public abstract class ResponseBody implements Closeable {
  @Nullable
  private Reader reader;
  
  private Charset charset() {
    Charset charset;
    MediaType mediaType = contentType();
    if (mediaType != null) {
      charset = mediaType.charset(StandardCharsets.UTF_8);
    } else {
      charset = StandardCharsets.UTF_8;
    } 
    return charset;
  }
  
  public static ResponseBody create(@Nullable final MediaType contentType, final long contentLength, final l丨Li1LL content) {
    Objects.requireNonNull(content, "source == null");
    return new ResponseBody() {
        public final l丨Li1LL val$content;
        
        public final long val$contentLength;
        
        public final MediaType val$contentType;
        
        public long contentLength() {
          return contentLength;
        }
        
        @Nullable
        public MediaType contentType() {
          return contentType;
        }
        
        public l丨Li1LL source() {
          return content;
        }
      };
  }
  
  public static ResponseBody create(@Nullable MediaType paramMediaType, iI丨LLL1 paramiI丨LLL1) {
    I1I i1I = new I1I();
    i1I.iIilII1(paramiI丨LLL1);
    return create(paramMediaType, paramiI丨LLL1.size(), i1I);
  }
  
  public static ResponseBody create(@Nullable MediaType paramMediaType, String paramString) {
    Charset charset = StandardCharsets.UTF_8;
    MediaType mediaType = paramMediaType;
    if (paramMediaType != null) {
      Charset charset1 = paramMediaType.charset();
      charset = charset1;
      mediaType = paramMediaType;
      if (charset1 == null) {
        charset = StandardCharsets.UTF_8;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramMediaType);
        stringBuilder.append("; charset=utf-8");
        mediaType = MediaType.parse(stringBuilder.toString());
      } 
    } 
    I1I i1I = new I1I();
    i1I.L1iI1(paramString, charset);
    return create(mediaType, i1I.iI1i丨I(), i1I);
  }
  
  public static ResponseBody create(@Nullable MediaType paramMediaType, byte[] paramArrayOfbyte) {
    I1I i1I = new I1I();
    i1I.i1(paramArrayOfbyte);
    return create(paramMediaType, paramArrayOfbyte.length, i1I);
  }
  
  public final InputStream byteStream() {
    return source().LL1IL();
  }
  
  public final byte[] bytes() throws IOException {
    long l = contentLength();
    if (l <= 2147483647L) {
      StringBuilder stringBuilder1;
      l丨Li1LL l丨Li1LL = source();
      try {
        byte[] arrayOfByte = l丨Li1LL.I丨L();
        if (l丨Li1LL != null)
          $closeResource(null, l丨Li1LL); 
        if (l == -1L || l == arrayOfByte.length)
          return arrayOfByte; 
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Content-Length (");
        stringBuilder1.append(l);
        stringBuilder1.append(") and stream length (");
        stringBuilder1.append(arrayOfByte.length);
        stringBuilder1.append(") disagree");
        throw new IOException(stringBuilder1.toString());
      } finally {
        Exception exception = null;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Cannot buffer entire body for content length: ");
    stringBuilder.append(l);
    throw new IOException(stringBuilder.toString());
  }
  
  public final Reader charStream() {
    Reader reader = this.reader;
    if (reader == null) {
      reader = new BomAwareReader(source(), charset());
      this.reader = reader;
    } 
    return reader;
  }
  
  public void close() {
    Util.closeQuietly(source());
  }
  
  public abstract long contentLength();
  
  @Nullable
  public abstract MediaType contentType();
  
  public abstract l丨Li1LL source();
  
  public final String string() throws IOException {
    l丨Li1LL l丨Li1LL = source();
    try {
      return l丨Li1LL.Ll丨1(Util.bomAwareCharset(l丨Li1LL, charset()));
    } finally {
      Exception exception = null;
    } 
  }
  
  public static final class BomAwareReader extends Reader {
    private final Charset charset;
    
    private boolean closed;
    
    @Nullable
    private Reader delegate;
    
    private final l丨Li1LL source;
    
    public BomAwareReader(l丨Li1LL param1l丨Li1LL, Charset param1Charset) {
      this.source = param1l丨Li1LL;
      this.charset = param1Charset;
    }
    
    public void close() throws IOException {
      this.closed = true;
      Reader reader = this.delegate;
      if (reader != null) {
        reader.close();
      } else {
        this.source.close();
      } 
    }
    
    public int read(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws IOException {
      if (!this.closed) {
        Reader reader2 = this.delegate;
        Reader reader1 = reader2;
        if (reader2 == null) {
          Charset charset = Util.bomAwareCharset(this.source, this.charset);
          reader1 = new InputStreamReader(this.source.LL1IL(), charset);
          this.delegate = reader1;
        } 
        return reader1.read(param1ArrayOfchar, param1Int1, param1Int2);
      } 
      throw new IOException("Stream closed");
    }
  }
}
