package okhttp3;

import I丨L.iI丨LLL1;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.tls.CertificateChainCleaner;

public final class CertificatePinner {
  public static final CertificatePinner DEFAULT = (new Builder()).build();
  
  @Nullable
  private final CertificateChainCleaner certificateChainCleaner;
  
  private final Set<Pin> pins;
  
  public CertificatePinner(Set<Pin> paramSet, @Nullable CertificateChainCleaner paramCertificateChainCleaner) {
    this.pins = paramSet;
    this.certificateChainCleaner = paramCertificateChainCleaner;
  }
  
  public static String pin(Certificate paramCertificate) {
    if (paramCertificate instanceof X509Certificate) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("sha256/");
      stringBuilder.append(sha256((X509Certificate)paramCertificate).base64());
      return stringBuilder.toString();
    } 
    throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
  }
  
  public static iI丨LLL1 sha1(X509Certificate paramX509Certificate) {
    return iI丨LLL1.of(paramX509Certificate.getPublicKey().getEncoded()).sha1();
  }
  
  public static iI丨LLL1 sha256(X509Certificate paramX509Certificate) {
    return iI丨LLL1.of(paramX509Certificate.getPublicKey().getEncoded()).sha256();
  }
  
  public void check(String paramString, List<Certificate> paramList) throws SSLPeerUnverifiedException {
    StringBuilder stringBuilder1;
    List<Pin> list1 = findMatchingPins(paramString);
    if (list1.isEmpty())
      return; 
    CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
    List<Certificate> list = paramList;
    if (certificateChainCleaner != null)
      list = certificateChainCleaner.clean(paramList, paramString); 
    int j = list.size();
    boolean bool = false;
    byte b;
    for (b = 0; b < j; b++) {
      X509Certificate x509Certificate = (X509Certificate)list.get(b);
      int k = list1.size();
      certificateChainCleaner = null;
      paramList = null;
      for (byte b1 = 0; b1 < k; b1++) {
        Pin pin = list1.get(b1);
        if (pin.hashAlgorithm.equals("sha256/")) {
          iI丨LLL1 iI丨LLL12;
          CertificateChainCleaner certificateChainCleaner1 = certificateChainCleaner;
          if (certificateChainCleaner == null)
            iI丨LLL12 = sha256(x509Certificate); 
          iI丨LLL1 iI丨LLL11 = iI丨LLL12;
          if (pin.hash.equals(iI丨LLL12))
            return; 
        } else if (pin.hashAlgorithm.equals("sha1/")) {
          iI丨LLL1 iI丨LLL12;
          List<Certificate> list2 = paramList;
          if (paramList == null)
            iI丨LLL12 = sha1(x509Certificate); 
          iI丨LLL1 iI丨LLL11 = iI丨LLL12;
          if (pin.hash.equals(iI丨LLL12))
            return; 
        } else {
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("unsupported hashAlgorithm: ");
          stringBuilder1.append(pin.hashAlgorithm);
          throw new AssertionError(stringBuilder1.toString());
        } 
      } 
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Certificate pinning failure!");
    stringBuilder2.append("\n  Peer certificate chain:");
    int i = list.size();
    for (b = 0; b < i; b++) {
      X509Certificate x509Certificate = (X509Certificate)list.get(b);
      stringBuilder2.append("\n    ");
      stringBuilder2.append(pin(x509Certificate));
      stringBuilder2.append(": ");
      stringBuilder2.append(x509Certificate.getSubjectDN().getName());
    } 
    stringBuilder2.append("\n  Pinned certificates for ");
    stringBuilder2.append((String)stringBuilder1);
    stringBuilder2.append(":");
    i = list1.size();
    for (b = bool; b < i; b++) {
      Pin pin = list1.get(b);
      stringBuilder2.append("\n    ");
      stringBuilder2.append(pin);
    } 
    throw new SSLPeerUnverifiedException(stringBuilder2.toString());
  }
  
  public void check(String paramString, Certificate... paramVarArgs) throws SSLPeerUnverifiedException {
    check(paramString, Arrays.asList(paramVarArgs));
  }
  
  public boolean equals(@Nullable Object paramObject) {
    null = true;
    if (paramObject == this)
      return true; 
    if (paramObject instanceof CertificatePinner) {
      CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
      paramObject = paramObject;
      if (Objects.equals(certificateChainCleaner, ((CertificatePinner)paramObject).certificateChainCleaner) && this.pins.equals(((CertificatePinner)paramObject).pins))
        return null; 
    } 
    return false;
  }
  
  public List<Pin> findMatchingPins(String paramString) {
    List<?> list = Collections.emptyList();
    for (Pin pin : this.pins) {
      if (pin.matches(paramString)) {
        List<?> list1 = list;
        if (list.isEmpty())
          list1 = new ArrayList(); 
        list1.add(pin);
        list = list1;
      } 
    } 
    return (List)list;
  }
  
  public int hashCode() {
    return Objects.hashCode(this.certificateChainCleaner) * 31 + this.pins.hashCode();
  }
  
  public CertificatePinner withCertificateChainCleaner(@Nullable CertificateChainCleaner paramCertificateChainCleaner) {
    CertificatePinner certificatePinner;
    if (Objects.equals(this.certificateChainCleaner, paramCertificateChainCleaner)) {
      certificatePinner = this;
    } else {
      certificatePinner = new CertificatePinner(this.pins, (CertificateChainCleaner)certificatePinner);
    } 
    return certificatePinner;
  }
  
  public static final class Builder {
    private final List<CertificatePinner.Pin> pins = new ArrayList<CertificatePinner.Pin>();
    
    public Builder add(String param1String, String... param1VarArgs) {
      Objects.requireNonNull(param1String, "pattern == null");
      int i = param1VarArgs.length;
      for (byte b = 0; b < i; b++) {
        String str = param1VarArgs[b];
        this.pins.add(new CertificatePinner.Pin(param1String, str));
      } 
      return this;
    }
    
    public CertificatePinner build() {
      return new CertificatePinner(new LinkedHashSet<CertificatePinner.Pin>(this.pins), null);
    }
  }
  
  public static final class Pin {
    private static final String WILDCARD = "*.";
    
    public final String canonicalHostname;
    
    public final iI丨LLL1 hash;
    
    public final String hashAlgorithm;
    
    public final String pattern;
    
    public Pin(String param1String1, String param1String2) {
      this.pattern = param1String1;
      if (param1String1.startsWith("*.")) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("http://");
        stringBuilder1.append(param1String1.substring(2));
        param1String1 = HttpUrl.get(stringBuilder1.toString()).host();
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("http://");
        stringBuilder1.append(param1String1);
        param1String1 = HttpUrl.get(stringBuilder1.toString()).host();
      } 
      this.canonicalHostname = param1String1;
      if (param1String2.startsWith("sha1/")) {
        this.hashAlgorithm = "sha1/";
        this.hash = iI丨LLL1.decodeBase64(param1String2.substring(5));
      } else if (param1String2.startsWith("sha256/")) {
        this.hashAlgorithm = "sha256/";
        this.hash = iI丨LLL1.decodeBase64(param1String2.substring(7));
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("pins must start with 'sha256/' or 'sha1/': ");
        stringBuilder1.append(param1String2);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      if (this.hash != null)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("pins must be base64: ");
      stringBuilder.append(param1String2);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof Pin) {
        String str = this.pattern;
        param1Object = param1Object;
        if (str.equals(((Pin)param1Object).pattern) && this.hashAlgorithm.equals(((Pin)param1Object).hashAlgorithm) && this.hash.equals(((Pin)param1Object).hash))
          return true; 
      } 
      return false;
    }
    
    public int hashCode() {
      return ((527 + this.pattern.hashCode()) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
    }
    
    public boolean matches(String param1String) {
      if (this.pattern.startsWith("*.")) {
        int i = param1String.indexOf('.');
        int j = param1String.length();
        null = true;
        if (j - i - 1 == this.canonicalHostname.length()) {
          String str = this.canonicalHostname;
          if (param1String.regionMatches(false, i + 1, str, 0, str.length()))
            return null; 
        } 
        return false;
      } 
      return param1String.equals(this.canonicalHostname);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.hashAlgorithm);
      stringBuilder.append(this.hash.base64());
      return stringBuilder.toString();
    }
  }
}
