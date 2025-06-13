package okhttp3;

import I1I.ILil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CipherSuite {
  private static final Map<String, CipherSuite> INSTANCES;
  
  public static final Comparator<String> ORDER_BY_NAME = ILil.IL1Iii;
  
  public static final CipherSuite TLS_AES_128_CCM_8_SHA256;
  
  public static final CipherSuite TLS_AES_128_CCM_SHA256;
  
  public static final CipherSuite TLS_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_CHACHA20_POLY1305_SHA256;
  
  public static final CipherSuite TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_AES_256_CBC_SHA256;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_DSS_WITH_DES_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_AES_256_CBC_SHA256;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256;
  
  public static final CipherSuite TLS_DHE_RSA_WITH_DES_CBC_SHA;
  
  public static final CipherSuite TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA;
  
  public static final CipherSuite TLS_DH_anon_EXPORT_WITH_RC4_40_MD5;
  
  public static final CipherSuite TLS_DH_anon_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_DH_anon_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_DH_anon_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_DH_anon_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_DH_anon_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_DH_anon_WITH_AES_256_CBC_SHA256;
  
  public static final CipherSuite TLS_DH_anon_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_DH_anon_WITH_DES_CBC_SHA;
  
  public static final CipherSuite TLS_DH_anon_WITH_RC4_128_MD5;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_NULL_SHA;
  
  public static final CipherSuite TLS_ECDHE_ECDSA_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_PSK_WITH_CHACHA20_POLY1305_SHA256;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_NULL_SHA;
  
  public static final CipherSuite TLS_ECDHE_RSA_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_NULL_SHA;
  
  public static final CipherSuite TLS_ECDH_ECDSA_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_NULL_SHA;
  
  public static final CipherSuite TLS_ECDH_RSA_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_anon_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_anon_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_ECDH_anon_WITH_NULL_SHA;
  
  public static final CipherSuite TLS_ECDH_anon_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_EMPTY_RENEGOTIATION_INFO_SCSV;
  
  public static final CipherSuite TLS_FALLBACK_SCSV;
  
  public static final CipherSuite TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5;
  
  public static final CipherSuite TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA;
  
  public static final CipherSuite TLS_KRB5_EXPORT_WITH_RC4_40_MD5;
  
  public static final CipherSuite TLS_KRB5_EXPORT_WITH_RC4_40_SHA;
  
  public static final CipherSuite TLS_KRB5_WITH_3DES_EDE_CBC_MD5;
  
  public static final CipherSuite TLS_KRB5_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_KRB5_WITH_DES_CBC_MD5;
  
  public static final CipherSuite TLS_KRB5_WITH_DES_CBC_SHA;
  
  public static final CipherSuite TLS_KRB5_WITH_RC4_128_MD5;
  
  public static final CipherSuite TLS_KRB5_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_PSK_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_PSK_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_PSK_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_PSK_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_RSA_EXPORT_WITH_DES40_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_EXPORT_WITH_RC4_40_MD5;
  
  public static final CipherSuite TLS_RSA_WITH_3DES_EDE_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_AES_128_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_AES_128_CBC_SHA256;
  
  public static final CipherSuite TLS_RSA_WITH_AES_128_GCM_SHA256;
  
  public static final CipherSuite TLS_RSA_WITH_AES_256_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_AES_256_CBC_SHA256;
  
  public static final CipherSuite TLS_RSA_WITH_AES_256_GCM_SHA384;
  
  public static final CipherSuite TLS_RSA_WITH_CAMELLIA_128_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_CAMELLIA_256_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_DES_CBC_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_NULL_MD5;
  
  public static final CipherSuite TLS_RSA_WITH_NULL_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_NULL_SHA256;
  
  public static final CipherSuite TLS_RSA_WITH_RC4_128_MD5;
  
  public static final CipherSuite TLS_RSA_WITH_RC4_128_SHA;
  
  public static final CipherSuite TLS_RSA_WITH_SEED_CBC_SHA;
  
  public final String javaName;
  
  static {
    INSTANCES = new LinkedHashMap<String, CipherSuite>();
    TLS_RSA_WITH_NULL_MD5 = init("SSL_RSA_WITH_NULL_MD5", 1);
    TLS_RSA_WITH_NULL_SHA = init("SSL_RSA_WITH_NULL_SHA", 2);
    TLS_RSA_EXPORT_WITH_RC4_40_MD5 = init("SSL_RSA_EXPORT_WITH_RC4_40_MD5", 3);
    TLS_RSA_WITH_RC4_128_MD5 = init("SSL_RSA_WITH_RC4_128_MD5", 4);
    TLS_RSA_WITH_RC4_128_SHA = init("SSL_RSA_WITH_RC4_128_SHA", 5);
    TLS_RSA_EXPORT_WITH_DES40_CBC_SHA = init("SSL_RSA_EXPORT_WITH_DES40_CBC_SHA", 8);
    TLS_RSA_WITH_DES_CBC_SHA = init("SSL_RSA_WITH_DES_CBC_SHA", 9);
    TLS_RSA_WITH_3DES_EDE_CBC_SHA = init("SSL_RSA_WITH_3DES_EDE_CBC_SHA", 10);
    TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA = init("SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", 17);
    TLS_DHE_DSS_WITH_DES_CBC_SHA = init("SSL_DHE_DSS_WITH_DES_CBC_SHA", 18);
    TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = init("SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 19);
    TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA = init("SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", 20);
    TLS_DHE_RSA_WITH_DES_CBC_SHA = init("SSL_DHE_RSA_WITH_DES_CBC_SHA", 21);
    TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = init("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA", 22);
    TLS_DH_anon_EXPORT_WITH_RC4_40_MD5 = init("SSL_DH_anon_EXPORT_WITH_RC4_40_MD5", 23);
    TLS_DH_anon_WITH_RC4_128_MD5 = init("SSL_DH_anon_WITH_RC4_128_MD5", 24);
    TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA = init("SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA", 25);
    TLS_DH_anon_WITH_DES_CBC_SHA = init("SSL_DH_anon_WITH_DES_CBC_SHA", 26);
    TLS_DH_anon_WITH_3DES_EDE_CBC_SHA = init("SSL_DH_anon_WITH_3DES_EDE_CBC_SHA", 27);
    TLS_KRB5_WITH_DES_CBC_SHA = init("TLS_KRB5_WITH_DES_CBC_SHA", 30);
    TLS_KRB5_WITH_3DES_EDE_CBC_SHA = init("TLS_KRB5_WITH_3DES_EDE_CBC_SHA", 31);
    TLS_KRB5_WITH_RC4_128_SHA = init("TLS_KRB5_WITH_RC4_128_SHA", 32);
    TLS_KRB5_WITH_DES_CBC_MD5 = init("TLS_KRB5_WITH_DES_CBC_MD5", 34);
    TLS_KRB5_WITH_3DES_EDE_CBC_MD5 = init("TLS_KRB5_WITH_3DES_EDE_CBC_MD5", 35);
    TLS_KRB5_WITH_RC4_128_MD5 = init("TLS_KRB5_WITH_RC4_128_MD5", 36);
    TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA = init("TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA", 38);
    TLS_KRB5_EXPORT_WITH_RC4_40_SHA = init("TLS_KRB5_EXPORT_WITH_RC4_40_SHA", 40);
    TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5 = init("TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5", 41);
    TLS_KRB5_EXPORT_WITH_RC4_40_MD5 = init("TLS_KRB5_EXPORT_WITH_RC4_40_MD5", 43);
    TLS_RSA_WITH_AES_128_CBC_SHA = init("TLS_RSA_WITH_AES_128_CBC_SHA", 47);
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA = init("TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 50);
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA = init("TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 51);
    TLS_DH_anon_WITH_AES_128_CBC_SHA = init("TLS_DH_anon_WITH_AES_128_CBC_SHA", 52);
    TLS_RSA_WITH_AES_256_CBC_SHA = init("TLS_RSA_WITH_AES_256_CBC_SHA", 53);
    TLS_DHE_DSS_WITH_AES_256_CBC_SHA = init("TLS_DHE_DSS_WITH_AES_256_CBC_SHA", 56);
    TLS_DHE_RSA_WITH_AES_256_CBC_SHA = init("TLS_DHE_RSA_WITH_AES_256_CBC_SHA", 57);
    TLS_DH_anon_WITH_AES_256_CBC_SHA = init("TLS_DH_anon_WITH_AES_256_CBC_SHA", 58);
    TLS_RSA_WITH_NULL_SHA256 = init("TLS_RSA_WITH_NULL_SHA256", 59);
    TLS_RSA_WITH_AES_128_CBC_SHA256 = init("TLS_RSA_WITH_AES_128_CBC_SHA256", 60);
    TLS_RSA_WITH_AES_256_CBC_SHA256 = init("TLS_RSA_WITH_AES_256_CBC_SHA256", 61);
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA256 = init("TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", 64);
    TLS_RSA_WITH_CAMELLIA_128_CBC_SHA = init("TLS_RSA_WITH_CAMELLIA_128_CBC_SHA", 65);
    TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA = init("TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA", 68);
    TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA = init("TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA", 69);
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 = init("TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", 103);
    TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 = init("TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", 106);
    TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 = init("TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", 107);
    TLS_DH_anon_WITH_AES_128_CBC_SHA256 = init("TLS_DH_anon_WITH_AES_128_CBC_SHA256", 108);
    TLS_DH_anon_WITH_AES_256_CBC_SHA256 = init("TLS_DH_anon_WITH_AES_256_CBC_SHA256", 109);
    TLS_RSA_WITH_CAMELLIA_256_CBC_SHA = init("TLS_RSA_WITH_CAMELLIA_256_CBC_SHA", 132);
    TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA = init("TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA", 135);
    TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA = init("TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA", 136);
    TLS_PSK_WITH_RC4_128_SHA = init("TLS_PSK_WITH_RC4_128_SHA", 138);
    TLS_PSK_WITH_3DES_EDE_CBC_SHA = init("TLS_PSK_WITH_3DES_EDE_CBC_SHA", 139);
    TLS_PSK_WITH_AES_128_CBC_SHA = init("TLS_PSK_WITH_AES_128_CBC_SHA", 140);
    TLS_PSK_WITH_AES_256_CBC_SHA = init("TLS_PSK_WITH_AES_256_CBC_SHA", 141);
    TLS_RSA_WITH_SEED_CBC_SHA = init("TLS_RSA_WITH_SEED_CBC_SHA", 150);
    TLS_RSA_WITH_AES_128_GCM_SHA256 = init("TLS_RSA_WITH_AES_128_GCM_SHA256", 156);
    TLS_RSA_WITH_AES_256_GCM_SHA384 = init("TLS_RSA_WITH_AES_256_GCM_SHA384", 157);
    TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 = init("TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", 158);
    TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 = init("TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", 159);
    TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 = init("TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", 162);
    TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 = init("TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", 163);
    TLS_DH_anon_WITH_AES_128_GCM_SHA256 = init("TLS_DH_anon_WITH_AES_128_GCM_SHA256", 166);
    TLS_DH_anon_WITH_AES_256_GCM_SHA384 = init("TLS_DH_anon_WITH_AES_256_GCM_SHA384", 167);
    TLS_EMPTY_RENEGOTIATION_INFO_SCSV = init("TLS_EMPTY_RENEGOTIATION_INFO_SCSV", 255);
    TLS_FALLBACK_SCSV = init("TLS_FALLBACK_SCSV", 22016);
    TLS_ECDH_ECDSA_WITH_NULL_SHA = init("TLS_ECDH_ECDSA_WITH_NULL_SHA", 49153);
    TLS_ECDH_ECDSA_WITH_RC4_128_SHA = init("TLS_ECDH_ECDSA_WITH_RC4_128_SHA", 49154);
    TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA = init("TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA", 49155);
    TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA = init("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA", 49156);
    TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA = init("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", 49157);
    TLS_ECDHE_ECDSA_WITH_NULL_SHA = init("TLS_ECDHE_ECDSA_WITH_NULL_SHA", 49158);
    TLS_ECDHE_ECDSA_WITH_RC4_128_SHA = init("TLS_ECDHE_ECDSA_WITH_RC4_128_SHA", 49159);
    TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA = init("TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA", 49160);
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA = init("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", 49161);
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA = init("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", 49162);
    TLS_ECDH_RSA_WITH_NULL_SHA = init("TLS_ECDH_RSA_WITH_NULL_SHA", 49163);
    TLS_ECDH_RSA_WITH_RC4_128_SHA = init("TLS_ECDH_RSA_WITH_RC4_128_SHA", 49164);
    TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA = init("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", 49165);
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA = init("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", 49166);
    TLS_ECDH_RSA_WITH_AES_256_CBC_SHA = init("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", 49167);
    TLS_ECDHE_RSA_WITH_NULL_SHA = init("TLS_ECDHE_RSA_WITH_NULL_SHA", 49168);
    TLS_ECDHE_RSA_WITH_RC4_128_SHA = init("TLS_ECDHE_RSA_WITH_RC4_128_SHA", 49169);
    TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA = init("TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA", 49170);
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA = init("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", 49171);
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA = init("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", 49172);
    TLS_ECDH_anon_WITH_NULL_SHA = init("TLS_ECDH_anon_WITH_NULL_SHA", 49173);
    TLS_ECDH_anon_WITH_RC4_128_SHA = init("TLS_ECDH_anon_WITH_RC4_128_SHA", 49174);
    TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA = init("TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA", 49175);
    TLS_ECDH_anon_WITH_AES_128_CBC_SHA = init("TLS_ECDH_anon_WITH_AES_128_CBC_SHA", 49176);
    TLS_ECDH_anon_WITH_AES_256_CBC_SHA = init("TLS_ECDH_anon_WITH_AES_256_CBC_SHA", 49177);
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 = init("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", 49187);
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 = init("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", 49188);
    TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 = init("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", 49189);
    TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 = init("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", 49190);
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 = init("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", 49191);
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 = init("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", 49192);
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 = init("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", 49193);
    TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 = init("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", 49194);
    TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 = init("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", 49195);
    TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 = init("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", 49196);
    TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 = init("TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", 49197);
    TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 = init("TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", 49198);
    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 = init("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", 49199);
    TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 = init("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", 49200);
    TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 = init("TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", 49201);
    TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 = init("TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", 49202);
    TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA = init("TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA", 49205);
    TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA = init("TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA", 49206);
    TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 = init("TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256", 52392);
    TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 = init("TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256", 52393);
    TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 = init("TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256", 52394);
    TLS_ECDHE_PSK_WITH_CHACHA20_POLY1305_SHA256 = init("TLS_ECDHE_PSK_WITH_CHACHA20_POLY1305_SHA256", 52396);
    TLS_AES_128_GCM_SHA256 = init("TLS_AES_128_GCM_SHA256", 4865);
    TLS_AES_256_GCM_SHA384 = init("TLS_AES_256_GCM_SHA384", 4866);
    TLS_CHACHA20_POLY1305_SHA256 = init("TLS_CHACHA20_POLY1305_SHA256", 4867);
    TLS_AES_128_CCM_SHA256 = init("TLS_AES_128_CCM_SHA256", 4868);
    TLS_AES_128_CCM_8_SHA256 = init("TLS_AES_128_CCM_8_SHA256", 4869);
  }
  
  private CipherSuite(String paramString) {
    Objects.requireNonNull(paramString);
    this.javaName = paramString;
  }
  
  public static CipherSuite forJavaName(String paramString) {
    // Byte code:
    //   0: ldc okhttp3/CipherSuite
    //   2: monitorenter
    //   3: getstatic okhttp3/CipherSuite.INSTANCES : Ljava/util/Map;
    //   6: astore_3
    //   7: aload_3
    //   8: aload_0
    //   9: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast okhttp3/CipherSuite
    //   17: astore_2
    //   18: aload_2
    //   19: astore_1
    //   20: aload_2
    //   21: ifnonnull -> 62
    //   24: aload_3
    //   25: aload_0
    //   26: invokestatic secondaryName : (Ljava/lang/String;)Ljava/lang/String;
    //   29: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   34: checkcast okhttp3/CipherSuite
    //   37: astore_2
    //   38: aload_2
    //   39: astore_1
    //   40: aload_2
    //   41: ifnonnull -> 53
    //   44: new okhttp3/CipherSuite
    //   47: astore_1
    //   48: aload_1
    //   49: aload_0
    //   50: invokespecial <init> : (Ljava/lang/String;)V
    //   53: aload_3
    //   54: aload_0
    //   55: aload_1
    //   56: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   61: pop
    //   62: ldc okhttp3/CipherSuite
    //   64: monitorexit
    //   65: aload_1
    //   66: areturn
    //   67: astore_0
    //   68: ldc okhttp3/CipherSuite
    //   70: monitorexit
    //   71: aload_0
    //   72: athrow
    // Exception table:
    //   from	to	target	type
    //   3	18	67	finally
    //   24	38	67	finally
    //   44	53	67	finally
    //   53	62	67	finally
  }
  
  public static List<CipherSuite> forJavaNames(String... paramVarArgs) {
    ArrayList<CipherSuite> arrayList = new ArrayList(paramVarArgs.length);
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      arrayList.add(forJavaName(paramVarArgs[b])); 
    return Collections.unmodifiableList(arrayList);
  }
  
  private static CipherSuite init(String paramString, int paramInt) {
    CipherSuite cipherSuite = new CipherSuite(paramString);
    INSTANCES.put(paramString, cipherSuite);
    return cipherSuite;
  }
  
  private static String secondaryName(String paramString) {
    if (paramString.startsWith("TLS_")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("SSL_");
      stringBuilder.append(paramString.substring(4));
      return stringBuilder.toString();
    } 
    String str = paramString;
    if (paramString.startsWith("SSL_")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("TLS_");
      stringBuilder.append(paramString.substring(4));
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  public String javaName() {
    return this.javaName;
  }
  
  public String toString() {
    return this.javaName;
  }
}
