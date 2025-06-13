package okhttp3;

import I1I.I1I;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public interface Dns {
  public static final Dns SYSTEM = I1I.IL1Iii;
  
  List<InetAddress> lookup(String paramString) throws UnknownHostException;
}
