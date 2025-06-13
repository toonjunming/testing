package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.http.ExchangeCodec;

public final class ExchangeFinder {
  public static final boolean $assertionsDisabled = false;
  
  private final Address address;
  
  private final Call call;
  
  private RealConnection connectingConnection;
  
  private final RealConnectionPool connectionPool;
  
  private final EventListener eventListener;
  
  private boolean hasStreamFailure;
  
  private Route nextRouteToTry;
  
  private RouteSelector.Selection routeSelection;
  
  private final RouteSelector routeSelector;
  
  private final Transmitter transmitter;
  
  public ExchangeFinder(Transmitter paramTransmitter, RealConnectionPool paramRealConnectionPool, Address paramAddress, Call paramCall, EventListener paramEventListener) {
    this.transmitter = paramTransmitter;
    this.connectionPool = paramRealConnectionPool;
    this.address = paramAddress;
    this.call = paramCall;
    this.eventListener = paramEventListener;
    this.routeSelector = new RouteSelector(paramAddress, paramRealConnectionPool.routeDatabase, paramCall, paramEventListener);
  }
  
  private RealConnection findConnection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   4: astore #13
    //   6: aload #13
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   13: invokevirtual isCanceled : ()Z
    //   16: ifne -> 608
    //   19: aload_0
    //   20: iconst_0
    //   21: putfield hasStreamFailure : Z
    //   24: aload_0
    //   25: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   28: astore #8
    //   30: aload #8
    //   32: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   35: astore #11
    //   37: aconst_null
    //   38: astore #12
    //   40: aload #11
    //   42: ifnull -> 63
    //   45: aload #11
    //   47: getfield noNewExchanges : Z
    //   50: ifeq -> 63
    //   53: aload #8
    //   55: invokevirtual releaseConnectionNoEvents : ()Ljava/net/Socket;
    //   58: astore #10
    //   60: goto -> 66
    //   63: aconst_null
    //   64: astore #10
    //   66: aload_0
    //   67: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   70: astore #8
    //   72: aload #8
    //   74: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   77: astore #9
    //   79: aload #9
    //   81: ifnull -> 90
    //   84: aconst_null
    //   85: astore #11
    //   87: goto -> 93
    //   90: aconst_null
    //   91: astore #9
    //   93: aload #9
    //   95: ifnonnull -> 175
    //   98: aload_0
    //   99: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   102: aload_0
    //   103: getfield address : Lokhttp3/Address;
    //   106: aload #8
    //   108: aconst_null
    //   109: iconst_0
    //   110: invokevirtual transmitterAcquirePooledConnection : (Lokhttp3/Address;Lokhttp3/internal/connection/Transmitter;Ljava/util/List;Z)Z
    //   113: ifeq -> 134
    //   116: aload_0
    //   117: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   120: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   123: astore #9
    //   125: aconst_null
    //   126: astore #8
    //   128: iconst_1
    //   129: istore #6
    //   131: goto -> 181
    //   134: aload_0
    //   135: getfield nextRouteToTry : Lokhttp3/Route;
    //   138: astore #8
    //   140: aload #8
    //   142: ifnull -> 153
    //   145: aload_0
    //   146: aconst_null
    //   147: putfield nextRouteToTry : Lokhttp3/Route;
    //   150: goto -> 178
    //   153: aload_0
    //   154: invokespecial retryCurrentRoute : ()Z
    //   157: ifeq -> 175
    //   160: aload_0
    //   161: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   164: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   167: invokevirtual route : ()Lokhttp3/Route;
    //   170: astore #8
    //   172: goto -> 178
    //   175: aconst_null
    //   176: astore #8
    //   178: iconst_0
    //   179: istore #6
    //   181: aload #13
    //   183: monitorexit
    //   184: aload #10
    //   186: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   189: aload #11
    //   191: ifnull -> 207
    //   194: aload_0
    //   195: getfield eventListener : Lokhttp3/EventListener;
    //   198: aload_0
    //   199: getfield call : Lokhttp3/Call;
    //   202: aload #11
    //   204: invokevirtual connectionReleased : (Lokhttp3/Call;Lokhttp3/Connection;)V
    //   207: iload #6
    //   209: ifeq -> 225
    //   212: aload_0
    //   213: getfield eventListener : Lokhttp3/EventListener;
    //   216: aload_0
    //   217: getfield call : Lokhttp3/Call;
    //   220: aload #9
    //   222: invokevirtual connectionAcquired : (Lokhttp3/Call;Lokhttp3/Connection;)V
    //   225: aload #9
    //   227: ifnull -> 233
    //   230: aload #9
    //   232: areturn
    //   233: aload #8
    //   235: ifnonnull -> 274
    //   238: aload_0
    //   239: getfield routeSelection : Lokhttp3/internal/connection/RouteSelector$Selection;
    //   242: astore #10
    //   244: aload #10
    //   246: ifnull -> 257
    //   249: aload #10
    //   251: invokevirtual hasNext : ()Z
    //   254: ifne -> 274
    //   257: aload_0
    //   258: aload_0
    //   259: getfield routeSelector : Lokhttp3/internal/connection/RouteSelector;
    //   262: invokevirtual next : ()Lokhttp3/internal/connection/RouteSelector$Selection;
    //   265: putfield routeSelection : Lokhttp3/internal/connection/RouteSelector$Selection;
    //   268: iconst_1
    //   269: istore #7
    //   271: goto -> 277
    //   274: iconst_0
    //   275: istore #7
    //   277: aload_0
    //   278: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   281: astore #13
    //   283: aload #13
    //   285: monitorenter
    //   286: aload_0
    //   287: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   290: invokevirtual isCanceled : ()Z
    //   293: ifne -> 585
    //   296: iload #7
    //   298: ifeq -> 354
    //   301: aload_0
    //   302: getfield routeSelection : Lokhttp3/internal/connection/RouteSelector$Selection;
    //   305: invokevirtual getAll : ()Ljava/util/List;
    //   308: astore #10
    //   310: aload #10
    //   312: astore #11
    //   314: aload_0
    //   315: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   318: aload_0
    //   319: getfield address : Lokhttp3/Address;
    //   322: aload_0
    //   323: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   326: aload #10
    //   328: iconst_0
    //   329: invokevirtual transmitterAcquirePooledConnection : (Lokhttp3/Address;Lokhttp3/internal/connection/Transmitter;Ljava/util/List;Z)Z
    //   332: ifeq -> 357
    //   335: aload_0
    //   336: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   339: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   342: astore #9
    //   344: iconst_1
    //   345: istore #6
    //   347: aload #10
    //   349: astore #11
    //   351: goto -> 357
    //   354: aconst_null
    //   355: astore #11
    //   357: aload #8
    //   359: astore #10
    //   361: iload #6
    //   363: ifne -> 406
    //   366: aload #8
    //   368: astore #10
    //   370: aload #8
    //   372: ifnonnull -> 384
    //   375: aload_0
    //   376: getfield routeSelection : Lokhttp3/internal/connection/RouteSelector$Selection;
    //   379: invokevirtual next : ()Lokhttp3/Route;
    //   382: astore #10
    //   384: new okhttp3/internal/connection/RealConnection
    //   387: astore #9
    //   389: aload #9
    //   391: aload_0
    //   392: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   395: aload #10
    //   397: invokespecial <init> : (Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Route;)V
    //   400: aload_0
    //   401: aload #9
    //   403: putfield connectingConnection : Lokhttp3/internal/connection/RealConnection;
    //   406: aload #13
    //   408: monitorexit
    //   409: iload #6
    //   411: ifeq -> 430
    //   414: aload_0
    //   415: getfield eventListener : Lokhttp3/EventListener;
    //   418: aload_0
    //   419: getfield call : Lokhttp3/Call;
    //   422: aload #9
    //   424: invokevirtual connectionAcquired : (Lokhttp3/Call;Lokhttp3/Connection;)V
    //   427: aload #9
    //   429: areturn
    //   430: aload #9
    //   432: iload_1
    //   433: iload_2
    //   434: iload_3
    //   435: iload #4
    //   437: iload #5
    //   439: aload_0
    //   440: getfield call : Lokhttp3/Call;
    //   443: aload_0
    //   444: getfield eventListener : Lokhttp3/EventListener;
    //   447: invokevirtual connect : (IIIIZLokhttp3/Call;Lokhttp3/EventListener;)V
    //   450: aload_0
    //   451: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   454: getfield routeDatabase : Lokhttp3/internal/connection/RouteDatabase;
    //   457: aload #9
    //   459: invokevirtual route : ()Lokhttp3/Route;
    //   462: invokevirtual connected : (Lokhttp3/Route;)V
    //   465: aload_0
    //   466: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   469: astore #13
    //   471: aload #13
    //   473: monitorenter
    //   474: aload_0
    //   475: aconst_null
    //   476: putfield connectingConnection : Lokhttp3/internal/connection/RealConnection;
    //   479: aload_0
    //   480: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   483: aload_0
    //   484: getfield address : Lokhttp3/Address;
    //   487: aload_0
    //   488: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   491: aload #11
    //   493: iconst_1
    //   494: invokevirtual transmitterAcquirePooledConnection : (Lokhttp3/Address;Lokhttp3/internal/connection/Transmitter;Ljava/util/List;Z)Z
    //   497: ifeq -> 531
    //   500: aload #9
    //   502: iconst_1
    //   503: putfield noNewExchanges : Z
    //   506: aload #9
    //   508: invokevirtual socket : ()Ljava/net/Socket;
    //   511: astore #8
    //   513: aload_0
    //   514: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   517: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   520: astore #9
    //   522: aload_0
    //   523: aload #10
    //   525: putfield nextRouteToTry : Lokhttp3/Route;
    //   528: goto -> 553
    //   531: aload_0
    //   532: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   535: aload #9
    //   537: invokevirtual put : (Lokhttp3/internal/connection/RealConnection;)V
    //   540: aload_0
    //   541: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   544: aload #9
    //   546: invokevirtual acquireConnectionNoEvents : (Lokhttp3/internal/connection/RealConnection;)V
    //   549: aload #12
    //   551: astore #8
    //   553: aload #13
    //   555: monitorexit
    //   556: aload #8
    //   558: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   561: aload_0
    //   562: getfield eventListener : Lokhttp3/EventListener;
    //   565: aload_0
    //   566: getfield call : Lokhttp3/Call;
    //   569: aload #9
    //   571: invokevirtual connectionAcquired : (Lokhttp3/Call;Lokhttp3/Connection;)V
    //   574: aload #9
    //   576: areturn
    //   577: astore #8
    //   579: aload #13
    //   581: monitorexit
    //   582: aload #8
    //   584: athrow
    //   585: new java/io/IOException
    //   588: astore #8
    //   590: aload #8
    //   592: ldc 'Canceled'
    //   594: invokespecial <init> : (Ljava/lang/String;)V
    //   597: aload #8
    //   599: athrow
    //   600: astore #8
    //   602: aload #13
    //   604: monitorexit
    //   605: aload #8
    //   607: athrow
    //   608: new java/io/IOException
    //   611: astore #8
    //   613: aload #8
    //   615: ldc 'Canceled'
    //   617: invokespecial <init> : (Ljava/lang/String;)V
    //   620: aload #8
    //   622: athrow
    //   623: astore #8
    //   625: aload #13
    //   627: monitorexit
    //   628: aload #8
    //   630: athrow
    // Exception table:
    //   from	to	target	type
    //   9	37	623	finally
    //   45	60	623	finally
    //   66	79	623	finally
    //   98	125	623	finally
    //   134	140	623	finally
    //   145	150	623	finally
    //   153	172	623	finally
    //   181	184	623	finally
    //   286	296	600	finally
    //   301	310	600	finally
    //   314	344	600	finally
    //   375	384	600	finally
    //   384	406	600	finally
    //   406	409	600	finally
    //   474	528	577	finally
    //   531	549	577	finally
    //   553	556	577	finally
    //   579	582	577	finally
    //   585	600	600	finally
    //   602	605	600	finally
    //   608	623	623	finally
    //   625	628	623	finally
  }
  
  private RealConnection findHealthyConnection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2) throws IOException {
    while (true) {
      null = findConnection(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean1);
      synchronized (this.connectionPool) {
        if (null.successCount == 0 && !null.isMultiplexed())
          return null; 
        if (!null.isHealthy(paramBoolean2)) {
          null.noNewExchanges();
          continue;
        } 
        return null;
      } 
    } 
  }
  
  private boolean retryCurrentRoute() {
    boolean bool;
    RealConnection realConnection = this.transmitter.connection;
    if (realConnection != null && realConnection.routeFailureCount == 0 && Util.sameConnection(realConnection.route().address().url(), this.address.url())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public RealConnection connectingConnection() {
    return this.connectingConnection;
  }
  
  public ExchangeCodec find(OkHttpClient paramOkHttpClient, Interceptor.Chain paramChain, boolean paramBoolean) {
    int k = paramChain.connectTimeoutMillis();
    int i = paramChain.readTimeoutMillis();
    int m = paramChain.writeTimeoutMillis();
    int j = paramOkHttpClient.pingIntervalMillis();
    boolean bool = paramOkHttpClient.retryOnConnectionFailure();
    try {
      return findHealthyConnection(k, i, m, j, bool, paramBoolean).newCodec(paramOkHttpClient, paramChain);
    } catch (RouteException routeException) {
      trackFailure();
      throw routeException;
    } catch (IOException iOException) {
      trackFailure();
      throw new RouteException(iOException);
    } 
  }
  
  public boolean hasRouteToTry() {
    // Byte code:
    //   0: aload_0
    //   1: getfield connectionPool : Lokhttp3/internal/connection/RealConnectionPool;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield nextRouteToTry : Lokhttp3/Route;
    //   11: astore #4
    //   13: iconst_1
    //   14: istore_2
    //   15: aload #4
    //   17: ifnull -> 24
    //   20: aload_3
    //   21: monitorexit
    //   22: iconst_1
    //   23: ireturn
    //   24: aload_0
    //   25: invokespecial retryCurrentRoute : ()Z
    //   28: ifeq -> 49
    //   31: aload_0
    //   32: aload_0
    //   33: getfield transmitter : Lokhttp3/internal/connection/Transmitter;
    //   36: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   39: invokevirtual route : ()Lokhttp3/Route;
    //   42: putfield nextRouteToTry : Lokhttp3/Route;
    //   45: aload_3
    //   46: monitorexit
    //   47: iconst_1
    //   48: ireturn
    //   49: aload_0
    //   50: getfield routeSelection : Lokhttp3/internal/connection/RouteSelector$Selection;
    //   53: astore #4
    //   55: aload #4
    //   57: ifnull -> 70
    //   60: iload_2
    //   61: istore_1
    //   62: aload #4
    //   64: invokevirtual hasNext : ()Z
    //   67: ifne -> 87
    //   70: aload_0
    //   71: getfield routeSelector : Lokhttp3/internal/connection/RouteSelector;
    //   74: invokevirtual hasNext : ()Z
    //   77: ifeq -> 85
    //   80: iload_2
    //   81: istore_1
    //   82: goto -> 87
    //   85: iconst_0
    //   86: istore_1
    //   87: aload_3
    //   88: monitorexit
    //   89: iload_1
    //   90: ireturn
    //   91: astore #4
    //   93: aload_3
    //   94: monitorexit
    //   95: aload #4
    //   97: athrow
    // Exception table:
    //   from	to	target	type
    //   7	13	91	finally
    //   20	22	91	finally
    //   24	47	91	finally
    //   49	55	91	finally
    //   62	70	91	finally
    //   70	80	91	finally
    //   87	89	91	finally
    //   93	95	91	finally
  }
  
  public boolean hasStreamFailure() {
    synchronized (this.connectionPool) {
      return this.hasStreamFailure;
    } 
  }
  
  public void trackFailure() {
    synchronized (this.connectionPool) {
      this.hasStreamFailure = true;
      return;
    } 
  }
}
