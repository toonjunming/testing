package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;
import javax.annotation.Nullable;

public final class Route {
  public final Address address;
  
  public final InetSocketAddress inetSocketAddress;
  
  public final Proxy proxy;
  
  public Route(Address paramAddress, Proxy paramProxy, InetSocketAddress paramInetSocketAddress) {
    Objects.requireNonNull(paramAddress, "address == null");
    Objects.requireNonNull(paramProxy, "proxy == null");
    Objects.requireNonNull(paramInetSocketAddress, "inetSocketAddress == null");
    this.address = paramAddress;
    this.proxy = paramProxy;
    this.inetSocketAddress = paramInetSocketAddress;
  }
  
  public Address address() {
    return this.address;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    if (paramObject instanceof Route) {
      paramObject = paramObject;
      if (((Route)paramObject).address.equals(this.address) && ((Route)paramObject).proxy.equals(this.proxy) && ((Route)paramObject).inetSocketAddress.equals(this.inetSocketAddress))
        return true; 
    } 
    return false;
  }
  
  public int hashCode() {
    return ((527 + this.address.hashCode()) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
  }
  
  public Proxy proxy() {
    return this.proxy;
  }
  
  public boolean requiresTunnel() {
    boolean bool;
    if (this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public InetSocketAddress socketAddress() {
    return this.inetSocketAddress;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Route{");
    stringBuilder.append(this.inetSocketAddress);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
}
