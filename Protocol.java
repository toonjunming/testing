package okhttp3;

import java.io.IOException;

public enum Protocol {
  H2_PRIOR_KNOWLEDGE, HTTP_1_0, HTTP_1_1, HTTP_2, QUIC, SPDY_3;
  
  private static final Protocol[] $VALUES;
  
  private final String protocol;
  
  static {
    Protocol protocol4 = new Protocol("HTTP_1_0", 0, "http/1.0");
    HTTP_1_0 = protocol4;
    Protocol protocol1 = new Protocol("HTTP_1_1", 1, "http/1.1");
    HTTP_1_1 = protocol1;
    Protocol protocol2 = new Protocol("SPDY_3", 2, "spdy/3.1");
    SPDY_3 = protocol2;
    Protocol protocol3 = new Protocol("HTTP_2", 3, "h2");
    HTTP_2 = protocol3;
    Protocol protocol5 = new Protocol("H2_PRIOR_KNOWLEDGE", 4, "h2_prior_knowledge");
    H2_PRIOR_KNOWLEDGE = protocol5;
    Protocol protocol6 = new Protocol("QUIC", 5, "quic");
    QUIC = protocol6;
    $VALUES = new Protocol[] { protocol4, protocol1, protocol2, protocol3, protocol5, protocol6 };
  }
  
  Protocol(String paramString1) {
    this.protocol = paramString1;
  }
  
  public static Protocol get(String paramString) throws IOException {
    Protocol protocol = HTTP_1_0;
    if (paramString.equals(protocol.protocol))
      return protocol; 
    protocol = HTTP_1_1;
    if (paramString.equals(protocol.protocol))
      return protocol; 
    protocol = H2_PRIOR_KNOWLEDGE;
    if (paramString.equals(protocol.protocol))
      return protocol; 
    protocol = HTTP_2;
    if (paramString.equals(protocol.protocol))
      return protocol; 
    protocol = SPDY_3;
    if (paramString.equals(protocol.protocol))
      return protocol; 
    protocol = QUIC;
    if (paramString.equals(protocol.protocol))
      return protocol; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unexpected protocol: ");
    stringBuilder.append(paramString);
    throw new IOException(stringBuilder.toString());
  }
  
  public String toString() {
    return this.protocol;
  }
}
