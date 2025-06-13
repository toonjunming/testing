package okhttp3.internal.http2;

import I丨L.I1I;
import I丨L.iI丨LLL1;
import I丨L.lIi丨I;
import I丨L.l丨Li1LL;
import I丨L.丨lL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Hpack {
  public static final Map<iI丨LLL1, Integer> NAME_TO_FIRST_INDEX;
  
  private static final int PREFIX_4_BITS = 15;
  
  private static final int PREFIX_5_BITS = 31;
  
  private static final int PREFIX_6_BITS = 63;
  
  private static final int PREFIX_7_BITS = 127;
  
  public static final Header[] STATIC_HEADER_TABLE;
  
  static {
    Header header1 = new Header(Header.TARGET_AUTHORITY, "");
    iI丨LLL1 iI丨LLL11 = Header.TARGET_METHOD;
    Header header2 = new Header(iI丨LLL11, "GET");
    Header header4 = new Header(iI丨LLL11, "POST");
    iI丨LLL1 iI丨LLL12 = Header.TARGET_PATH;
    Header header3 = new Header(iI丨LLL12, "/");
    Header header5 = new Header(iI丨LLL12, "/index.html");
    iI丨LLL1 iI丨LLL13 = Header.TARGET_SCHEME;
    Header header6 = new Header(iI丨LLL13, "http");
    Header header7 = new Header(iI丨LLL13, "https");
    iI丨LLL1 iI丨LLL14 = Header.RESPONSE_STATUS;
    STATIC_HEADER_TABLE = new Header[] { 
        header1, header2, header4, header3, header5, header6, header7, new Header(iI丨LLL14, "200"), new Header(iI丨LLL14, "204"), new Header(iI丨LLL14, "206"), 
        new Header(iI丨LLL14, "304"), new Header(iI丨LLL14, "400"), new Header(iI丨LLL14, "404"), new Header(iI丨LLL14, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), 
        new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), 
        new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), 
        new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), 
        new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), 
        new Header("www-authenticate", "") };
    NAME_TO_FIRST_INDEX = nameToFirstIndex();
  }
  
  public static iI丨LLL1 checkLowercase(iI丨LLL1 paramiI丨LLL1) throws IOException {
    int i = paramiI丨LLL1.size();
    byte b = 0;
    while (b < i) {
      byte b1 = paramiI丨LLL1.getByte(b);
      if (b1 < 65 || b1 > 90) {
        b++;
        continue;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("PROTOCOL_ERROR response malformed: mixed case name: ");
      stringBuilder.append(paramiI丨LLL1.utf8());
      throw new IOException(stringBuilder.toString());
    } 
    return paramiI丨LLL1;
  }
  
  private static Map<iI丨LLL1, Integer> nameToFirstIndex() {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>(STATIC_HEADER_TABLE.length);
    byte b = 0;
    while (true) {
      Header[] arrayOfHeader = STATIC_HEADER_TABLE;
      if (b < arrayOfHeader.length) {
        if (!linkedHashMap.containsKey((arrayOfHeader[b]).name))
          linkedHashMap.put((arrayOfHeader[b]).name, Integer.valueOf(b)); 
        b++;
        continue;
      } 
      return (Map)Collections.unmodifiableMap(linkedHashMap);
    } 
  }
  
  public static final class Reader {
    public Header[] dynamicTable;
    
    public int dynamicTableByteCount;
    
    public int headerCount;
    
    private final List<Header> headerList = new ArrayList<Header>();
    
    private final int headerTableSizeSetting;
    
    private int maxDynamicTableByteCount;
    
    public int nextHeaderIndex;
    
    private final l丨Li1LL source;
    
    public Reader(int param1Int1, int param1Int2, 丨lL param1丨lL) {
      Header[] arrayOfHeader = new Header[8];
      this.dynamicTable = arrayOfHeader;
      this.nextHeaderIndex = arrayOfHeader.length - 1;
      this.headerCount = 0;
      this.dynamicTableByteCount = 0;
      this.headerTableSizeSetting = param1Int1;
      this.maxDynamicTableByteCount = param1Int2;
      this.source = lIi丨I.I丨L(param1丨lL);
    }
    
    public Reader(int param1Int, 丨lL param1丨lL) {
      this(param1Int, param1Int, param1丨lL);
    }
    
    private void adjustDynamicTableByteCount() {
      int j = this.maxDynamicTableByteCount;
      int i = this.dynamicTableByteCount;
      if (j < i)
        if (j == 0) {
          clearDynamicTable();
        } else {
          evictToRecoverBytes(i - j);
        }  
    }
    
    private void clearDynamicTable() {
      Arrays.fill((Object[])this.dynamicTable, (Object)null);
      this.nextHeaderIndex = this.dynamicTable.length - 1;
      this.headerCount = 0;
      this.dynamicTableByteCount = 0;
    }
    
    private int dynamicTableIndex(int param1Int) {
      return this.nextHeaderIndex + 1 + param1Int;
    }
    
    private int evictToRecoverBytes(int param1Int) {
      int i = 0;
      int j = 0;
      if (param1Int > 0) {
        i = this.dynamicTable.length - 1;
        int k = param1Int;
        param1Int = j;
        while (true) {
          j = this.nextHeaderIndex;
          if (i >= j && k > 0) {
            Header[] arrayOfHeader1 = this.dynamicTable;
            k -= (arrayOfHeader1[i]).hpackSize;
            this.dynamicTableByteCount -= (arrayOfHeader1[i]).hpackSize;
            this.headerCount--;
            param1Int++;
            i--;
            continue;
          } 
          break;
        } 
        Header[] arrayOfHeader = this.dynamicTable;
        System.arraycopy(arrayOfHeader, j + 1, arrayOfHeader, j + 1 + param1Int, this.headerCount);
        this.nextHeaderIndex += param1Int;
        i = param1Int;
      } 
      return i;
    }
    
    private iI丨LLL1 getName(int param1Int) throws IOException {
      if (isStaticHeader(param1Int))
        return (Hpack.STATIC_HEADER_TABLE[param1Int]).name; 
      int i = dynamicTableIndex(param1Int - Hpack.STATIC_HEADER_TABLE.length);
      if (i >= 0) {
        Header[] arrayOfHeader = this.dynamicTable;
        if (i < arrayOfHeader.length)
          return (arrayOfHeader[i]).name; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Header index too large ");
      stringBuilder.append(param1Int + 1);
      throw new IOException(stringBuilder.toString());
    }
    
    private void insertIntoDynamicTable(int param1Int, Header param1Header) {
      this.headerList.add(param1Header);
      int j = param1Header.hpackSize;
      int i = j;
      if (param1Int != -1)
        i = j - (this.dynamicTable[dynamicTableIndex(param1Int)]).hpackSize; 
      j = this.maxDynamicTableByteCount;
      if (i > j) {
        clearDynamicTable();
        return;
      } 
      j = evictToRecoverBytes(this.dynamicTableByteCount + i - j);
      if (param1Int == -1) {
        param1Int = this.headerCount;
        Header[] arrayOfHeader = this.dynamicTable;
        if (param1Int + 1 > arrayOfHeader.length) {
          Header[] arrayOfHeader1 = new Header[arrayOfHeader.length * 2];
          System.arraycopy(arrayOfHeader, 0, arrayOfHeader1, arrayOfHeader.length, arrayOfHeader.length);
          this.nextHeaderIndex = this.dynamicTable.length - 1;
          this.dynamicTable = arrayOfHeader1;
        } 
        param1Int = this.nextHeaderIndex;
        this.nextHeaderIndex = param1Int - 1;
        this.dynamicTable[param1Int] = param1Header;
        this.headerCount++;
      } else {
        int k = dynamicTableIndex(param1Int);
        this.dynamicTable[param1Int + k + j] = param1Header;
      } 
      this.dynamicTableByteCount += i;
    }
    
    private boolean isStaticHeader(int param1Int) {
      boolean bool = true;
      if (param1Int < 0 || param1Int > Hpack.STATIC_HEADER_TABLE.length - 1)
        bool = false; 
      return bool;
    }
    
    private int readByte() throws IOException {
      return this.source.readByte() & 0xFF;
    }
    
    private void readIndexedHeader(int param1Int) throws IOException {
      if (isStaticHeader(param1Int)) {
        Header header = Hpack.STATIC_HEADER_TABLE[param1Int];
        this.headerList.add(header);
      } else {
        int i = dynamicTableIndex(param1Int - Hpack.STATIC_HEADER_TABLE.length);
        if (i >= 0) {
          Header[] arrayOfHeader = this.dynamicTable;
          if (i < arrayOfHeader.length) {
            this.headerList.add(arrayOfHeader[i]);
            return;
          } 
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Header index too large ");
        stringBuilder.append(param1Int + 1);
        throw new IOException(stringBuilder.toString());
      } 
    }
    
    private void readLiteralHeaderWithIncrementalIndexingIndexedName(int param1Int) throws IOException {
      insertIntoDynamicTable(-1, new Header(getName(param1Int), readByteString()));
    }
    
    private void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
      insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(readByteString()), readByteString()));
    }
    
    private void readLiteralHeaderWithoutIndexingIndexedName(int param1Int) throws IOException {
      iI丨LLL1 iI丨LLL11 = getName(param1Int);
      iI丨LLL1 iI丨LLL12 = readByteString();
      this.headerList.add(new Header(iI丨LLL11, iI丨LLL12));
    }
    
    private void readLiteralHeaderWithoutIndexingNewName() throws IOException {
      iI丨LLL1 iI丨LLL11 = Hpack.checkLowercase(readByteString());
      iI丨LLL1 iI丨LLL12 = readByteString();
      this.headerList.add(new Header(iI丨LLL11, iI丨LLL12));
    }
    
    public List<Header> getAndResetHeaderList() {
      ArrayList<Header> arrayList = new ArrayList<Header>(this.headerList);
      this.headerList.clear();
      return arrayList;
    }
    
    public int maxDynamicTableByteCount() {
      return this.maxDynamicTableByteCount;
    }
    
    public iI丨LLL1 readByteString() throws IOException {
      boolean bool;
      int i = readByte();
      if ((i & 0x80) == 128) {
        bool = true;
      } else {
        bool = false;
      } 
      i = readInt(i, 127);
      return bool ? iI丨LLL1.of(Huffman.get().decode(this.source.lI丨lii(i))) : this.source.ILil(i);
    }
    
    public void readHeaders() throws IOException {
      while (!this.source.l丨Li1LL()) {
        int i = this.source.readByte() & 0xFF;
        if (i != 128) {
          if ((i & 0x80) == 128) {
            readIndexedHeader(readInt(i, 127) - 1);
            continue;
          } 
          if (i == 64) {
            readLiteralHeaderWithIncrementalIndexingNewName();
            continue;
          } 
          if ((i & 0x40) == 64) {
            readLiteralHeaderWithIncrementalIndexingIndexedName(readInt(i, 63) - 1);
            continue;
          } 
          if ((i & 0x20) == 32) {
            i = readInt(i, 31);
            this.maxDynamicTableByteCount = i;
            if (i >= 0 && i <= this.headerTableSizeSetting) {
              adjustDynamicTableByteCount();
              continue;
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid dynamic table size update ");
            stringBuilder.append(this.maxDynamicTableByteCount);
            throw new IOException(stringBuilder.toString());
          } 
          if (i == 16 || i == 0) {
            readLiteralHeaderWithoutIndexingNewName();
            continue;
          } 
          readLiteralHeaderWithoutIndexingIndexedName(readInt(i, 15) - 1);
          continue;
        } 
        throw new IOException("index == 0");
      } 
    }
    
    public int readInt(int param1Int1, int param1Int2) throws IOException {
      param1Int1 &= param1Int2;
      if (param1Int1 < param1Int2)
        return param1Int1; 
      param1Int1 = 0;
      while (true) {
        int i = readByte();
        if ((i & 0x80) != 0) {
          param1Int2 += (i & 0x7F) << param1Int1;
          param1Int1 += 7;
          continue;
        } 
        return param1Int2 + (i << param1Int1);
      } 
    }
  }
  
  public static final class Writer {
    private static final int SETTINGS_HEADER_TABLE_SIZE = 4096;
    
    private static final int SETTINGS_HEADER_TABLE_SIZE_LIMIT = 16384;
    
    public Header[] dynamicTable;
    
    public int dynamicTableByteCount;
    
    private boolean emitDynamicTableSizeUpdate;
    
    public int headerCount;
    
    public int headerTableSizeSetting;
    
    public int maxDynamicTableByteCount;
    
    public int nextHeaderIndex;
    
    private final I1I out;
    
    private int smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
    
    private final boolean useCompression;
    
    public Writer(int param1Int, boolean param1Boolean, I1I param1I1I) {
      Header[] arrayOfHeader = new Header[8];
      this.dynamicTable = arrayOfHeader;
      this.nextHeaderIndex = arrayOfHeader.length - 1;
      this.headerCount = 0;
      this.dynamicTableByteCount = 0;
      this.headerTableSizeSetting = param1Int;
      this.maxDynamicTableByteCount = param1Int;
      this.useCompression = param1Boolean;
      this.out = param1I1I;
    }
    
    public Writer(I1I param1I1I) {
      this(4096, true, param1I1I);
    }
    
    private void adjustDynamicTableByteCount() {
      int j = this.maxDynamicTableByteCount;
      int i = this.dynamicTableByteCount;
      if (j < i)
        if (j == 0) {
          clearDynamicTable();
        } else {
          evictToRecoverBytes(i - j);
        }  
    }
    
    private void clearDynamicTable() {
      Arrays.fill((Object[])this.dynamicTable, (Object)null);
      this.nextHeaderIndex = this.dynamicTable.length - 1;
      this.headerCount = 0;
      this.dynamicTableByteCount = 0;
    }
    
    private int evictToRecoverBytes(int param1Int) {
      int i = 0;
      int j = 0;
      if (param1Int > 0) {
        i = this.dynamicTable.length - 1;
        int k = param1Int;
        param1Int = j;
        while (true) {
          j = this.nextHeaderIndex;
          if (i >= j && k > 0) {
            Header[] arrayOfHeader1 = this.dynamicTable;
            k -= (arrayOfHeader1[i]).hpackSize;
            this.dynamicTableByteCount -= (arrayOfHeader1[i]).hpackSize;
            this.headerCount--;
            param1Int++;
            i--;
            continue;
          } 
          break;
        } 
        Header[] arrayOfHeader = this.dynamicTable;
        System.arraycopy(arrayOfHeader, j + 1, arrayOfHeader, j + 1 + param1Int, this.headerCount);
        arrayOfHeader = this.dynamicTable;
        i = this.nextHeaderIndex;
        Arrays.fill((Object[])arrayOfHeader, i + 1, i + 1 + param1Int, (Object)null);
        this.nextHeaderIndex += param1Int;
        i = param1Int;
      } 
      return i;
    }
    
    private void insertIntoDynamicTable(Header param1Header) {
      int i = param1Header.hpackSize;
      int j = this.maxDynamicTableByteCount;
      if (i > j) {
        clearDynamicTable();
        return;
      } 
      evictToRecoverBytes(this.dynamicTableByteCount + i - j);
      j = this.headerCount;
      Header[] arrayOfHeader = this.dynamicTable;
      if (j + 1 > arrayOfHeader.length) {
        Header[] arrayOfHeader1 = new Header[arrayOfHeader.length * 2];
        System.arraycopy(arrayOfHeader, 0, arrayOfHeader1, arrayOfHeader.length, arrayOfHeader.length);
        this.nextHeaderIndex = this.dynamicTable.length - 1;
        this.dynamicTable = arrayOfHeader1;
      } 
      j = this.nextHeaderIndex;
      this.nextHeaderIndex = j - 1;
      this.dynamicTable[j] = param1Header;
      this.headerCount++;
      this.dynamicTableByteCount += i;
    }
    
    public void setHeaderTableSizeSetting(int param1Int) {
      this.headerTableSizeSetting = param1Int;
      param1Int = Math.min(param1Int, 16384);
      int i = this.maxDynamicTableByteCount;
      if (i == param1Int)
        return; 
      if (param1Int < i)
        this.smallestHeaderTableSizeSetting = Math.min(this.smallestHeaderTableSizeSetting, param1Int); 
      this.emitDynamicTableSizeUpdate = true;
      this.maxDynamicTableByteCount = param1Int;
      adjustDynamicTableByteCount();
    }
    
    public void writeByteString(iI丨LLL1 param1iI丨LLL1) throws IOException {
      if (this.useCompression && Huffman.get().encodedLength(param1iI丨LLL1) < param1iI丨LLL1.size()) {
        I1I i1I = new I1I();
        Huffman.get().encode(param1iI丨LLL1, i1I);
        param1iI丨LLL1 = i1I.L11丨();
        writeInt(param1iI丨LLL1.size(), 127, 128);
        this.out.iIilII1(param1iI丨LLL1);
      } else {
        writeInt(param1iI丨LLL1.size(), 127, 0);
        this.out.iIilII1(param1iI丨LLL1);
      } 
    }
    
    public void writeHeaders(List<Header> param1List) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: getfield emitDynamicTableSizeUpdate : Z
      //   4: ifeq -> 52
      //   7: aload_0
      //   8: getfield smallestHeaderTableSizeSetting : I
      //   11: istore_2
      //   12: iload_2
      //   13: aload_0
      //   14: getfield maxDynamicTableByteCount : I
      //   17: if_icmpge -> 29
      //   20: aload_0
      //   21: iload_2
      //   22: bipush #31
      //   24: bipush #32
      //   26: invokevirtual writeInt : (III)V
      //   29: aload_0
      //   30: iconst_0
      //   31: putfield emitDynamicTableSizeUpdate : Z
      //   34: aload_0
      //   35: ldc 2147483647
      //   37: putfield smallestHeaderTableSizeSetting : I
      //   40: aload_0
      //   41: aload_0
      //   42: getfield maxDynamicTableByteCount : I
      //   45: bipush #31
      //   47: bipush #32
      //   49: invokevirtual writeInt : (III)V
      //   52: aload_1
      //   53: invokeinterface size : ()I
      //   58: istore #8
      //   60: iconst_0
      //   61: istore #4
      //   63: iload #4
      //   65: iload #8
      //   67: if_icmpge -> 456
      //   70: aload_1
      //   71: iload #4
      //   73: invokeinterface get : (I)Ljava/lang/Object;
      //   78: checkcast okhttp3/internal/http2/Header
      //   81: astore #12
      //   83: aload #12
      //   85: getfield name : LI丨L/iI丨LLL1;
      //   88: invokevirtual toAsciiLowercase : ()LI丨L/iI丨LLL1;
      //   91: astore #10
      //   93: aload #12
      //   95: getfield value : LI丨L/iI丨LLL1;
      //   98: astore #11
      //   100: getstatic okhttp3/internal/http2/Hpack.NAME_TO_FIRST_INDEX : Ljava/util/Map;
      //   103: aload #10
      //   105: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   110: checkcast java/lang/Integer
      //   113: astore #13
      //   115: aload #13
      //   117: ifnull -> 196
      //   120: aload #13
      //   122: invokevirtual intValue : ()I
      //   125: iconst_1
      //   126: iadd
      //   127: istore_3
      //   128: iload_3
      //   129: iconst_1
      //   130: if_icmple -> 189
      //   133: iload_3
      //   134: bipush #8
      //   136: if_icmpge -> 189
      //   139: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   142: astore #13
      //   144: aload #13
      //   146: iload_3
      //   147: iconst_1
      //   148: isub
      //   149: aaload
      //   150: getfield value : LI丨L/iI丨LLL1;
      //   153: aload #11
      //   155: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   158: ifeq -> 166
      //   161: iload_3
      //   162: istore_2
      //   163: goto -> 200
      //   166: aload #13
      //   168: iload_3
      //   169: aaload
      //   170: getfield value : LI丨L/iI丨LLL1;
      //   173: aload #11
      //   175: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   178: ifeq -> 189
      //   181: iload_3
      //   182: istore_2
      //   183: iinc #3, 1
      //   186: goto -> 200
      //   189: iload_3
      //   190: istore_2
      //   191: iconst_m1
      //   192: istore_3
      //   193: goto -> 200
      //   196: iconst_m1
      //   197: istore_3
      //   198: iconst_m1
      //   199: istore_2
      //   200: iload_3
      //   201: istore #6
      //   203: iload_2
      //   204: istore #7
      //   206: iload_3
      //   207: iconst_m1
      //   208: if_icmpne -> 331
      //   211: aload_0
      //   212: getfield nextHeaderIndex : I
      //   215: iconst_1
      //   216: iadd
      //   217: istore #5
      //   219: aload_0
      //   220: getfield dynamicTable : [Lokhttp3/internal/http2/Header;
      //   223: arraylength
      //   224: istore #9
      //   226: iload_3
      //   227: istore #6
      //   229: iload_2
      //   230: istore #7
      //   232: iload #5
      //   234: iload #9
      //   236: if_icmpge -> 331
      //   239: iload_2
      //   240: istore #6
      //   242: aload_0
      //   243: getfield dynamicTable : [Lokhttp3/internal/http2/Header;
      //   246: iload #5
      //   248: aaload
      //   249: getfield name : LI丨L/iI丨LLL1;
      //   252: aload #10
      //   254: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   257: ifeq -> 322
      //   260: aload_0
      //   261: getfield dynamicTable : [Lokhttp3/internal/http2/Header;
      //   264: iload #5
      //   266: aaload
      //   267: getfield value : LI丨L/iI丨LLL1;
      //   270: aload #11
      //   272: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   275: ifeq -> 300
      //   278: aload_0
      //   279: getfield nextHeaderIndex : I
      //   282: istore_3
      //   283: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   286: arraylength
      //   287: iload #5
      //   289: iload_3
      //   290: isub
      //   291: iadd
      //   292: istore #6
      //   294: iload_2
      //   295: istore #7
      //   297: goto -> 331
      //   300: iload_2
      //   301: istore #6
      //   303: iload_2
      //   304: iconst_m1
      //   305: if_icmpne -> 322
      //   308: iload #5
      //   310: aload_0
      //   311: getfield nextHeaderIndex : I
      //   314: isub
      //   315: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   318: arraylength
      //   319: iadd
      //   320: istore #6
      //   322: iinc #5, 1
      //   325: iload #6
      //   327: istore_2
      //   328: goto -> 226
      //   331: iload #6
      //   333: iconst_m1
      //   334: if_icmpeq -> 351
      //   337: aload_0
      //   338: iload #6
      //   340: bipush #127
      //   342: sipush #128
      //   345: invokevirtual writeInt : (III)V
      //   348: goto -> 450
      //   351: iload #7
      //   353: iconst_m1
      //   354: if_icmpne -> 388
      //   357: aload_0
      //   358: getfield out : LI丨L/I1I;
      //   361: bipush #64
      //   363: invokevirtual iI : (I)LI丨L/I1I;
      //   366: pop
      //   367: aload_0
      //   368: aload #10
      //   370: invokevirtual writeByteString : (LI丨L/iI丨LLL1;)V
      //   373: aload_0
      //   374: aload #11
      //   376: invokevirtual writeByteString : (LI丨L/iI丨LLL1;)V
      //   379: aload_0
      //   380: aload #12
      //   382: invokespecial insertIntoDynamicTable : (Lokhttp3/internal/http2/Header;)V
      //   385: goto -> 450
      //   388: aload #10
      //   390: getstatic okhttp3/internal/http2/Header.PSEUDO_PREFIX : LI丨L/iI丨LLL1;
      //   393: invokevirtual startsWith : (LI丨L/iI丨LLL1;)Z
      //   396: ifeq -> 428
      //   399: getstatic okhttp3/internal/http2/Header.TARGET_AUTHORITY : LI丨L/iI丨LLL1;
      //   402: aload #10
      //   404: invokevirtual equals : (Ljava/lang/Object;)Z
      //   407: ifne -> 428
      //   410: aload_0
      //   411: iload #7
      //   413: bipush #15
      //   415: iconst_0
      //   416: invokevirtual writeInt : (III)V
      //   419: aload_0
      //   420: aload #11
      //   422: invokevirtual writeByteString : (LI丨L/iI丨LLL1;)V
      //   425: goto -> 450
      //   428: aload_0
      //   429: iload #7
      //   431: bipush #63
      //   433: bipush #64
      //   435: invokevirtual writeInt : (III)V
      //   438: aload_0
      //   439: aload #11
      //   441: invokevirtual writeByteString : (LI丨L/iI丨LLL1;)V
      //   444: aload_0
      //   445: aload #12
      //   447: invokespecial insertIntoDynamicTable : (Lokhttp3/internal/http2/Header;)V
      //   450: iinc #4, 1
      //   453: goto -> 63
      //   456: return
    }
    
    public void writeInt(int param1Int1, int param1Int2, int param1Int3) {
      if (param1Int1 < param1Int2) {
        this.out.iI(param1Int1 | param1Int3);
        return;
      } 
      this.out.iI(param1Int3 | param1Int2);
      for (param1Int1 -= param1Int2; param1Int1 >= 128; param1Int1 >>>= 7)
        this.out.iI(0x80 | param1Int1 & 0x7F); 
      this.out.iI(param1Int1);
    }
  }
}
