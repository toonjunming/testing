package okhttp3;

import I丨L.I丨L;
import I丨L.iI丨LLL1;
import I丨L.lIi丨I;
import I丨L.丨lL;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.internal.Util;

public abstract class RequestBody {
  public static RequestBody create(@Nullable final MediaType contentType, final iI丨LLL1 content) {
    return new RequestBody() {
        public final iI丨LLL1 val$content;
        
        public final MediaType val$contentType;
        
        public long contentLength() throws IOException {
          return content.size();
        }
        
        @Nullable
        public MediaType contentType() {
          return contentType;
        }
        
        public void writeTo(I丨L param1I丨L) throws IOException {
          param1I丨L.iIlLiL(content);
        }
      };
  }
  
  public static RequestBody create(@Nullable final MediaType contentType, final File file) {
    Objects.requireNonNull(file, "file == null");
    return new RequestBody() {
        public final MediaType val$contentType;
        
        public final File val$file;
        
        public long contentLength() {
          return file.length();
        }
        
        @Nullable
        public MediaType contentType() {
          return contentType;
        }
        
        public void writeTo(I丨L param1I丨L) throws IOException {
          丨lL 丨lL = lIi丨I.丨il(file);
          try {
            param1I丨L.lIi丨I(丨lL);
            return;
          } finally {
            param1I丨L = null;
          } 
        }
      };
  }
  
  public static RequestBody create(@Nullable MediaType paramMediaType, String paramString) {
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
    return create(mediaType, paramString.getBytes(charset));
  }
  
  public static RequestBody create(@Nullable MediaType paramMediaType, byte[] paramArrayOfbyte) {
    return create(paramMediaType, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static RequestBody create(@Nullable final MediaType contentType, final byte[] content, final int offset, final int byteCount) {
    Objects.requireNonNull(content, "content == null");
    Util.checkOffsetAndCount(content.length, offset, byteCount);
    return new RequestBody() {
        public final int val$byteCount;
        
        public final byte[] val$content;
        
        public final MediaType val$contentType;
        
        public final int val$offset;
        
        public long contentLength() {
          return byteCount;
        }
        
        @Nullable
        public MediaType contentType() {
          return contentType;
        }
        
        public void writeTo(I丨L param1I丨L) throws IOException {
          param1I丨L.write(content, offset, byteCount);
        }
      };
  }
  
  public long contentLength() throws IOException {
    return -1L;
  }
  
  @Nullable
  public abstract MediaType contentType();
  
  public boolean isDuplex() {
    return false;
  }
  
  public boolean isOneShot() {
    return false;
  }
  
  public abstract void writeTo(I丨L paramI丨L) throws IOException;
}
