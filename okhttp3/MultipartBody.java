package okhttp3;

import I丨L.I1I;
import I丨L.I丨L;
import I丨L.iI丨LLL1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import okhttp3.internal.Util;

public final class MultipartBody extends RequestBody {
  public static final MediaType ALTERNATIVE;
  
  private static final byte[] COLONSPACE;
  
  private static final byte[] CRLF;
  
  private static final byte[] DASHDASH;
  
  public static final MediaType DIGEST;
  
  public static final MediaType FORM;
  
  public static final MediaType MIXED = MediaType.get("multipart/mixed");
  
  public static final MediaType PARALLEL;
  
  private final iI丨LLL1 boundary;
  
  private long contentLength = -1L;
  
  private final MediaType contentType;
  
  private final MediaType originalType;
  
  private final List<Part> parts;
  
  static {
    ALTERNATIVE = MediaType.get("multipart/alternative");
    DIGEST = MediaType.get("multipart/digest");
    PARALLEL = MediaType.get("multipart/parallel");
    FORM = MediaType.get("multipart/form-data");
    COLONSPACE = new byte[] { 58, 32 };
    CRLF = new byte[] { 13, 10 };
    DASHDASH = new byte[] { 45, 45 };
  }
  
  public MultipartBody(iI丨LLL1 paramiI丨LLL1, MediaType paramMediaType, List<Part> paramList) {
    this.boundary = paramiI丨LLL1;
    this.originalType = paramMediaType;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramMediaType);
    stringBuilder.append("; boundary=");
    stringBuilder.append(paramiI丨LLL1.utf8());
    this.contentType = MediaType.get(stringBuilder.toString());
    this.parts = Util.immutableList(paramList);
  }
  
  public static void appendQuotedString(StringBuilder paramStringBuilder, String paramString) {
    paramStringBuilder.append('"');
    int i = paramString.length();
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (c != '\n') {
        if (c != '\r') {
          if (c != '"') {
            paramStringBuilder.append(c);
          } else {
            paramStringBuilder.append("%22");
          } 
        } else {
          paramStringBuilder.append("%0D");
        } 
      } else {
        paramStringBuilder.append("%0A");
      } 
    } 
    paramStringBuilder.append('"');
  }
  
  private long writeOrCountBytes(@Nullable I丨L paramI丨L, boolean paramBoolean) throws IOException {
    I1I i1I;
    if (paramBoolean) {
      paramI丨L = new I1I();
      i1I = (I1I)paramI丨L;
    } else {
      i1I = null;
    } 
    int i = this.parts.size();
    long l1 = 0L;
    for (byte b = 0; b < i; b++) {
      Part part = this.parts.get(b);
      Headers headers = part.headers;
      RequestBody requestBody = part.body;
      paramI丨L.write(DASHDASH);
      paramI丨L.iIlLiL(this.boundary);
      paramI丨L.write(CRLF);
      if (headers != null) {
        int j = headers.size();
        for (byte b1 = 0; b1 < j; b1++)
          paramI丨L.ILL(headers.name(b1)).write(COLONSPACE).ILL(headers.value(b1)).write(CRLF); 
      } 
      MediaType mediaType = requestBody.contentType();
      if (mediaType != null)
        paramI丨L.ILL("Content-Type: ").ILL(mediaType.toString()).write(CRLF); 
      long l = requestBody.contentLength();
      if (l != -1L) {
        paramI丨L.ILL("Content-Length: ").丨l丨(l).write(CRLF);
      } else if (paramBoolean) {
        i1I.丨丨LLlI1();
        return -1L;
      } 
      byte[] arrayOfByte1 = CRLF;
      paramI丨L.write(arrayOfByte1);
      if (paramBoolean) {
        l1 += l;
      } else {
        requestBody.writeTo(paramI丨L);
      } 
      paramI丨L.write(arrayOfByte1);
    } 
    byte[] arrayOfByte = DASHDASH;
    paramI丨L.write(arrayOfByte);
    paramI丨L.iIlLiL(this.boundary);
    paramI丨L.write(arrayOfByte);
    paramI丨L.write(CRLF);
    long l2 = l1;
    if (paramBoolean) {
      l2 = l1 + i1I.iI1i丨I();
      i1I.丨丨LLlI1();
    } 
    return l2;
  }
  
  public String boundary() {
    return this.boundary.utf8();
  }
  
  public long contentLength() throws IOException {
    long l = this.contentLength;
    if (l != -1L)
      return l; 
    l = writeOrCountBytes(null, true);
    this.contentLength = l;
    return l;
  }
  
  public MediaType contentType() {
    return this.contentType;
  }
  
  public Part part(int paramInt) {
    return this.parts.get(paramInt);
  }
  
  public List<Part> parts() {
    return this.parts;
  }
  
  public int size() {
    return this.parts.size();
  }
  
  public MediaType type() {
    return this.originalType;
  }
  
  public void writeTo(I丨L paramI丨L) throws IOException {
    writeOrCountBytes(paramI丨L, false);
  }
  
  public static final class Builder {
    private final iI丨LLL1 boundary;
    
    private final List<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
    
    private MediaType type = MultipartBody.MIXED;
    
    public Builder() {
      this(UUID.randomUUID().toString());
    }
    
    public Builder(String param1String) {
      this.boundary = iI丨LLL1.encodeUtf8(param1String);
    }
    
    public Builder addFormDataPart(String param1String1, String param1String2) {
      return addPart(MultipartBody.Part.createFormData(param1String1, param1String2));
    }
    
    public Builder addFormDataPart(String param1String1, @Nullable String param1String2, RequestBody param1RequestBody) {
      return addPart(MultipartBody.Part.createFormData(param1String1, param1String2, param1RequestBody));
    }
    
    public Builder addPart(@Nullable Headers param1Headers, RequestBody param1RequestBody) {
      return addPart(MultipartBody.Part.create(param1Headers, param1RequestBody));
    }
    
    public Builder addPart(MultipartBody.Part param1Part) {
      Objects.requireNonNull(param1Part, "part == null");
      this.parts.add(param1Part);
      return this;
    }
    
    public Builder addPart(RequestBody param1RequestBody) {
      return addPart(MultipartBody.Part.create(param1RequestBody));
    }
    
    public MultipartBody build() {
      if (!this.parts.isEmpty())
        return new MultipartBody(this.boundary, this.type, this.parts); 
      throw new IllegalStateException("Multipart body must have at least one part.");
    }
    
    public Builder setType(MediaType param1MediaType) {
      Objects.requireNonNull(param1MediaType, "type == null");
      if (param1MediaType.type().equals("multipart")) {
        this.type = param1MediaType;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("multipart != ");
      stringBuilder.append(param1MediaType);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  public static final class Part {
    public final RequestBody body;
    
    @Nullable
    public final Headers headers;
    
    private Part(@Nullable Headers param1Headers, RequestBody param1RequestBody) {
      this.headers = param1Headers;
      this.body = param1RequestBody;
    }
    
    public static Part create(@Nullable Headers param1Headers, RequestBody param1RequestBody) {
      Objects.requireNonNull(param1RequestBody, "body == null");
      if (param1Headers == null || param1Headers.get("Content-Type") == null) {
        if (param1Headers == null || param1Headers.get("Content-Length") == null)
          return new Part(param1Headers, param1RequestBody); 
        throw new IllegalArgumentException("Unexpected header: Content-Length");
      } 
      throw new IllegalArgumentException("Unexpected header: Content-Type");
    }
    
    public static Part create(RequestBody param1RequestBody) {
      return create(null, param1RequestBody);
    }
    
    public static Part createFormData(String param1String1, String param1String2) {
      return createFormData(param1String1, null, RequestBody.create((MediaType)null, param1String2));
    }
    
    public static Part createFormData(String param1String1, @Nullable String param1String2, RequestBody param1RequestBody) {
      Objects.requireNonNull(param1String1, "name == null");
      StringBuilder stringBuilder = new StringBuilder("form-data; name=");
      MultipartBody.appendQuotedString(stringBuilder, param1String1);
      if (param1String2 != null) {
        stringBuilder.append("; filename=");
        MultipartBody.appendQuotedString(stringBuilder, param1String2);
      } 
      return create((new Headers.Builder()).addUnsafeNonAscii("Content-Disposition", stringBuilder.toString()).build(), param1RequestBody);
    }
    
    public RequestBody body() {
      return this.body;
    }
    
    @Nullable
    public Headers headers() {
      return this.headers;
    }
  }
}
