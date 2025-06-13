package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.Util;

public final class Address {
  @Nullable
  public final CertificatePinner certificatePinner;
  
  public final List<ConnectionSpec> connectionSpecs;
  
  public final Dns dns;
  
  @Nullable
  public final HostnameVerifier hostnameVerifier;
  
  public final List<Protocol> protocols;
  
  @Nullable
  public final Proxy proxy;
  
  public final Authenticator proxyAuthenticator;
  
  public final ProxySelector proxySelector;
  
  public final SocketFactory socketFactory;
  
  @Nullable
  public final SSLSocketFactory sslSocketFactory;
  
  public final HttpUrl url;
  
  public Address(String paramString, int paramInt, Dns paramDns, SocketFactory paramSocketFactory, @Nullable SSLSocketFactory paramSSLSocketFactory, @Nullable HostnameVerifier paramHostnameVerifier, @Nullable CertificatePinner paramCertificatePinner, Authenticator paramAuthenticator, @Nullable Proxy paramProxy, List<Protocol> paramList, List<ConnectionSpec> paramList1, ProxySelector paramProxySelector) {
    String str;
    HttpUrl.Builder builder = new HttpUrl.Builder();
    if (paramSSLSocketFactory != null) {
      str = "https";
    } else {
      str = "http";
    } 
    this.url = builder.scheme(str).host(paramString).port(paramInt).build();
    Objects.requireNonNull(paramDns, "dns == null");
    this.dns = paramDns;
    Objects.requireNonNull(paramSocketFactory, "socketFactory == null");
    this.socketFactory = paramSocketFactory;
    Objects.requireNonNull(paramAuthenticator, "proxyAuthenticator == null");
    this.proxyAuthenticator = paramAuthenticator;
    Objects.requireNonNull(paramList, "protocols == null");
    this.protocols = Util.immutableList(paramList);
    Objects.requireNonNull(paramList1, "connectionSpecs == null");
    this.connectionSpecs = Util.immutableList(paramList1);
    Objects.requireNonNull(paramProxySelector, "proxySelector == null");
    this.proxySelector = paramProxySelector;
    this.proxy = paramProxy;
    this.sslSocketFactory = paramSSLSocketFactory;
    this.hostnameVerifier = paramHostnameVerifier;
    this.certificatePinner = paramCertificatePinner;
  }
  
  @Nullable
  public CertificatePinner certificatePinner() {
    return this.certificatePinner;
  }
  
  public List<ConnectionSpec> connectionSpecs() {
    return this.connectionSpecs;
  }
  
  public Dns dns() {
    return this.dns;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    if (paramObject instanceof Address) {
      HttpUrl httpUrl = this.url;
      paramObject = paramObject;
      if (httpUrl.equals(((Address)paramObject).url) && equalsNonHost((Address)paramObject))
        return true; 
    } 
    return false;
  }
  
  public boolean equalsNonHost(Address paramAddress) {
    boolean bool;
    if (this.dns.equals(paramAddress.dns) && this.proxyAuthenticator.equals(paramAddress.proxyAuthenticator) && this.protocols.equals(paramAddress.protocols) && this.connectionSpecs.equals(paramAddress.connectionSpecs) && this.proxySelector.equals(paramAddress.proxySelector) && Objects.equals(this.proxy, paramAddress.proxy) && Objects.equals(this.sslSocketFactory, paramAddress.sslSocketFactory) && Objects.equals(this.hostnameVerifier, paramAddress.hostnameVerifier) && Objects.equals(this.certificatePinner, paramAddress.certificatePinner) && url().port() == paramAddress.url().port()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int hashCode() {
    return (((((((((527 + this.url.hashCode()) * 31 + this.dns.hashCode()) * 31 + this.proxyAuthenticator.hashCode()) * 31 + this.protocols.hashCode()) * 31 + this.connectionSpecs.hashCode()) * 31 + this.proxySelector.hashCode()) * 31 + Objects.hashCode(this.proxy)) * 31 + Objects.hashCode(this.sslSocketFactory)) * 31 + Objects.hashCode(this.hostnameVerifier)) * 31 + Objects.hashCode(this.certificatePinner);
  }
  
  @Nullable
  public HostnameVerifier hostnameVerifier() {
    return this.hostnameVerifier;
  }
  
  public List<Protocol> protocols() {
    return this.protocols;
  }
  
  @Nullable
  public Proxy proxy() {
    return this.proxy;
  }
  
  public Authenticator proxyAuthenticator() {
    return this.proxyAuthenticator;
  }
  
  public ProxySelector proxySelector() {
    return this.proxySelector;
  }
  
  public SocketFactory socketFactory() {
    return this.socketFactory;
  }
  
  @Nullable
  public SSLSocketFactory sslSocketFactory() {
    return this.sslSocketFactory;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Address{");
    stringBuilder.append(this.url.host());
    stringBuilder.append(":");
    stringBuilder.append(this.url.port());
    if (this.proxy != null) {
      stringBuilder.append(", proxy=");
      stringBuilder.append(this.proxy);
    } else {
      stringBuilder.append(", proxySelector=");
      stringBuilder.append(this.proxySelector);
    } 
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public HttpUrl url() {
    return this.url;
  }
}
