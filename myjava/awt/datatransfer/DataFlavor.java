package myjava.awt.datatransfer;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.harmony.awt.datatransfer.DTK;
import org.apache.harmony.awt.internal.nls.Messages;

public class DataFlavor implements Externalizable, Cloneable {
  public static final DataFlavor javaFileListFlavor;
  
  public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";
  
  public static final String javaRemoteObjectMimeType = "application/x-java-remote-object";
  
  public static final String javaSerializedObjectMimeType = "application/x-java-serialized-object";
  
  @Deprecated
  public static final DataFlavor plainTextFlavor = new DataFlavor("text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");
  
  private static DataFlavor plainUnicodeFlavor;
  
  private static final long serialVersionUID = 8367026044764648243L;
  
  private static final String[] sortedTextFlavors;
  
  public static final DataFlavor stringFlavor = new DataFlavor("application/x-java-serialized-object; class=java.lang.String", "Unicode String");
  
  private String humanPresentableName;
  
  private MimeTypeProcessor.MimeType mimeInfo;
  
  private Class<?> representationClass;
  
  static {
    javaFileListFlavor = new DataFlavor("application/x-java-file-list; class=java.util.List", "application/x-java-file-list");
    sortedTextFlavors = new String[] { 
        "text/sgml", "text/xml", "text/html", "text/rtf", "text/enriched", "text/richtext", "text/uri-list", "text/tab-separated-values", "text/t140", "text/rfc822-headers", 
        "text/parityfec", "text/directory", "text/css", "text/calendar", "application/x-java-serialized-object", "text/plain" };
    plainUnicodeFlavor = null;
  }
  
  public DataFlavor() {
    this.mimeInfo = null;
    this.humanPresentableName = null;
    this.representationClass = null;
  }
  
  public DataFlavor(Class<?> paramClass, String paramString) {
    MimeTypeProcessor.MimeType mimeType = new MimeTypeProcessor.MimeType("application", "x-java-serialized-object");
    this.mimeInfo = mimeType;
    if (paramString != null) {
      this.humanPresentableName = paramString;
    } else {
      this.humanPresentableName = "application/x-java-serialized-object";
    } 
    mimeType.addParameter("class", paramClass.getName());
    this.representationClass = paramClass;
  }
  
  public DataFlavor(String paramString) throws ClassNotFoundException {
    init(paramString, null, null);
  }
  
  public DataFlavor(String paramString1, String paramString2) {
    try {
      init(paramString1, paramString2, null);
      return;
    } catch (ClassNotFoundException classNotFoundException) {
      throw new IllegalArgumentException(Messages.getString("awt.16C", this.mimeInfo.getParameter("class")), classNotFoundException);
    } 
  }
  
  public DataFlavor(String paramString1, String paramString2, ClassLoader paramClassLoader) throws ClassNotFoundException {
    init(paramString1, paramString2, paramClassLoader);
  }
  
  private static List<DataFlavor> fetchTextFlavors(List<DataFlavor> paramList, String paramString) {
    LinkedList<DataFlavor> linkedList = new LinkedList();
    Iterator<DataFlavor> iterator = paramList.iterator();
    while (true) {
      LinkedList<DataFlavor> linkedList1;
      if (!iterator.hasNext()) {
        linkedList1 = linkedList;
        if (linkedList.isEmpty())
          linkedList1 = null; 
        return linkedList1;
      } 
      DataFlavor dataFlavor = linkedList1.next();
      if (dataFlavor.isFlavorTextType()) {
        if (dataFlavor.mimeInfo.getFullType().equals(paramString)) {
          if (!linkedList.contains(dataFlavor))
            linkedList.add(dataFlavor); 
          linkedList1.remove();
        } 
        continue;
      } 
      linkedList1.remove();
    } 
  }
  
  private String getCharset() {
    if (this.mimeInfo == null || isCharsetRedundant())
      return ""; 
    String str = this.mimeInfo.getParameter("charset");
    return (isCharsetRequired() && (str == null || str.length() == 0)) ? DTK.getDTK().getDefaultCharset() : ((str == null) ? "" : str);
  }
  
  private static List<DataFlavor> getFlavors(List<DataFlavor> paramList, Class<?> paramClass) {
    LinkedList<DataFlavor> linkedList = new LinkedList();
    Iterator<DataFlavor> iterator = paramList.iterator();
    while (true) {
      if (!iterator.hasNext()) {
        if (linkedList.isEmpty())
          paramList = null; 
        return paramList;
      } 
      DataFlavor dataFlavor = iterator.next();
      if (dataFlavor.representationClass.equals(paramClass))
        linkedList.add(dataFlavor); 
    } 
  }
  
  private static List<DataFlavor> getFlavors(List<DataFlavor> paramList, String[] paramArrayOfString) {
    LinkedList<DataFlavor> linkedList = new LinkedList();
    Iterator<DataFlavor> iterator = paramList.iterator();
    while (true) {
      if (!iterator.hasNext()) {
        if (linkedList.isEmpty())
          paramList = null; 
        return paramList;
      } 
      DataFlavor dataFlavor = iterator.next();
      if (isCharsetSupported(dataFlavor.getCharset())) {
        int i = paramArrayOfString.length;
        for (byte b = 0; b < i; b++) {
          if (Charset.forName(paramArrayOfString[b]).equals(Charset.forName(dataFlavor.getCharset())))
            linkedList.add(dataFlavor); 
        } 
        continue;
      } 
      iterator.remove();
    } 
  }
  
  private String getKeyInfo() {
    StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.mimeInfo.getFullType()));
    stringBuilder.append(";class=");
    stringBuilder.append(this.representationClass.getName());
    String str2 = stringBuilder.toString();
    String str1 = str2;
    if (this.mimeInfo.getPrimaryType().equals("text"))
      if (isUnicodeFlavor()) {
        str1 = str2;
      } else {
        StringBuilder stringBuilder1 = new StringBuilder(String.valueOf(str2));
        stringBuilder1.append(";charset=");
        stringBuilder1.append(getCharset().toLowerCase());
        str1 = stringBuilder1.toString();
      }  
    return str1;
  }
  
  public static final DataFlavor getTextPlainUnicodeFlavor() {
    if (plainUnicodeFlavor == null) {
      StringBuilder stringBuilder = new StringBuilder("text/plain; charset=");
      stringBuilder.append(DTK.getDTK().getDefaultCharset());
      stringBuilder.append("; class=java.io.InputStream");
      plainUnicodeFlavor = new DataFlavor(stringBuilder.toString(), "Plain Text");
    } 
    return plainUnicodeFlavor;
  }
  
  private void init(String paramString1, String paramString2, ClassLoader paramClassLoader) throws ClassNotFoundException {
    Class<?> clazz;
    try {
      MimeTypeProcessor.MimeType mimeType = MimeTypeProcessor.parse(paramString1);
      this.mimeInfo = mimeType;
      if (paramString2 != null) {
        this.humanPresentableName = paramString2;
      } else {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(mimeType.getPrimaryType()));
        stringBuilder.append('/');
        stringBuilder.append(this.mimeInfo.getSubType());
        this.humanPresentableName = stringBuilder.toString();
      } 
      paramString2 = this.mimeInfo.getParameter("class");
      paramString1 = paramString2;
      if (paramString2 == null) {
        paramString1 = "java.io.InputStream";
        this.mimeInfo.addParameter("class", "java.io.InputStream");
      } 
      if (paramClassLoader == null) {
        clazz = Class.forName(paramString1);
      } else {
        clazz = paramClassLoader.loadClass((String)clazz);
      } 
      this.representationClass = clazz;
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new IllegalArgumentException(Messages.getString("awt.16D", clazz));
    } 
  }
  
  private boolean isByteCodeFlavor() {
    Class<?> clazz = this.representationClass;
    return (clazz != null && (clazz.equals(InputStream.class) || this.representationClass.equals(ByteBuffer.class) || this.representationClass.equals(byte[].class)));
  }
  
  private boolean isCharsetRedundant() {
    String str = this.mimeInfo.getFullType();
    return !(!str.equals("text/rtf") && !str.equals("text/tab-separated-values") && !str.equals("text/t140") && !str.equals("text/rfc822-headers") && !str.equals("text/parityfec"));
  }
  
  private boolean isCharsetRequired() {
    String str = this.mimeInfo.getFullType();
    return !(!str.equals("text/sgml") && !str.equals("text/xml") && !str.equals("text/html") && !str.equals("text/enriched") && !str.equals("text/richtext") && !str.equals("text/uri-list") && !str.equals("text/directory") && !str.equals("text/css") && !str.equals("text/calendar") && !str.equals("application/x-java-serialized-object") && !str.equals("text/plain"));
  }
  
  private static boolean isCharsetSupported(String paramString) {
    try {
      return Charset.isSupported(paramString);
    } catch (IllegalCharsetNameException illegalCharsetNameException) {
      return false;
    } 
  }
  
  private boolean isUnicodeFlavor() {
    Class<?> clazz = this.representationClass;
    return (clazz != null && (clazz.equals(Reader.class) || this.representationClass.equals(String.class) || this.representationClass.equals(CharBuffer.class) || this.representationClass.equals(char[].class)));
  }
  
  private static List<DataFlavor> selectBestByAlphabet(List<DataFlavor> paramList) {
    int i = paramList.size();
    String[] arrayOfString = new String[i];
    LinkedList<DataFlavor> linkedList = new LinkedList();
    for (byte b = 0;; b++) {
      LinkedList<DataFlavor> linkedList1;
      if (b >= i) {
        Arrays.sort(arrayOfString, String.CASE_INSENSITIVE_ORDER);
        Iterator<DataFlavor> iterator = paramList.iterator();
        while (true) {
          if (!iterator.hasNext()) {
            linkedList1 = linkedList;
            if (linkedList.isEmpty())
              linkedList1 = null; 
            return linkedList1;
          } 
          DataFlavor dataFlavor = linkedList1.next();
          if (arrayOfString[0].equalsIgnoreCase(dataFlavor.getCharset()))
            linkedList.add(dataFlavor); 
        } 
        break;
      } 
      arrayOfString[b] = ((DataFlavor)linkedList1.get(b)).getCharset();
    } 
  }
  
  private static DataFlavor selectBestByCharset(List<DataFlavor> paramList) {
    List<DataFlavor> list2 = getFlavors(paramList, new String[] { "UTF-16", "UTF-8", "UTF-16BE", "UTF-16LE" });
    List<DataFlavor> list1 = list2;
    if (list2 == null) {
      list2 = getFlavors(paramList, new String[] { DTK.getDTK().getDefaultCharset() });
      list1 = list2;
      if (list2 == null) {
        list2 = getFlavors(paramList, new String[] { "US-ASCII" });
        list1 = list2;
        if (list2 == null)
          list1 = selectBestByAlphabet(paramList); 
      } 
    } 
    return (list1 != null) ? ((list1.size() == 1) ? list1.get(0) : selectBestFlavorWOCharset(list1)) : null;
  }
  
  private static DataFlavor selectBestFlavorWCharset(List<DataFlavor> paramList) {
    List<DataFlavor> list = getFlavors(paramList, Reader.class);
    if (list != null)
      return list.get(0); 
    list = getFlavors(paramList, String.class);
    if (list != null)
      return list.get(0); 
    list = getFlavors(paramList, CharBuffer.class);
    if (list != null)
      return list.get(0); 
    list = getFlavors(paramList, char[].class);
    return (list != null) ? list.get(0) : selectBestByCharset(paramList);
  }
  
  private static DataFlavor selectBestFlavorWOCharset(List<DataFlavor> paramList) {
    List<DataFlavor> list = getFlavors(paramList, InputStream.class);
    if (list != null)
      return list.get(0); 
    list = getFlavors(paramList, ByteBuffer.class);
    if (list != null)
      return list.get(0); 
    list = getFlavors(paramList, byte[].class);
    return (list != null) ? list.get(0) : paramList.get(0);
  }
  
  public static final DataFlavor selectBestTextFlavor(DataFlavor[] paramArrayOfDataFlavor) {
    if (paramArrayOfDataFlavor == null)
      return null; 
    List<List<DataFlavor>> list = sortTextFlavorsByType(new LinkedList<DataFlavor>(Arrays.asList(paramArrayOfDataFlavor)));
    if (list.isEmpty())
      return null; 
    list = (List<List<DataFlavor>>)list.get(0);
    return (list.size() == 1) ? (DataFlavor)list.get(0) : ((((DataFlavor)list.get(0)).getCharset().length() == 0) ? selectBestFlavorWOCharset((List)list) : selectBestFlavorWCharset((List)list));
  }
  
  private static List<List<DataFlavor>> sortTextFlavorsByType(List<DataFlavor> paramList) {
    LinkedList<List<DataFlavor>> linkedList = new LinkedList();
    String[] arrayOfString = sortedTextFlavors;
    int i = arrayOfString.length;
    for (byte b = 0;; b++) {
      if (b >= i) {
        if (!paramList.isEmpty())
          linkedList.addLast(paramList); 
        return linkedList;
      } 
      List<DataFlavor> list = fetchTextFlavors(paramList, arrayOfString[b]);
      if (list != null)
        linkedList.addLast(list); 
    } 
  }
  
  public static final Class<?> tryToLoadClass(String paramString, ClassLoader paramClassLoader) throws ClassNotFoundException {
    try {
      return Class.forName(paramString);
    } catch (ClassNotFoundException classNotFoundException) {
      try {
        return ClassLoader.getSystemClassLoader().loadClass(paramString);
      } catch (ClassNotFoundException classNotFoundException1) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null)
          try {
            return classLoader.loadClass(paramString);
          } catch (ClassNotFoundException classNotFoundException2) {} 
        return paramClassLoader.loadClass(paramString);
      } 
    } 
  }
  
  public Object clone() throws CloneNotSupportedException {
    DataFlavor dataFlavor = new DataFlavor();
    dataFlavor.humanPresentableName = this.humanPresentableName;
    dataFlavor.representationClass = this.representationClass;
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null) {
      mimeType = (MimeTypeProcessor.MimeType)mimeType.clone();
    } else {
      mimeType = null;
    } 
    dataFlavor.mimeInfo = mimeType;
    return dataFlavor;
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == null || !(paramObject instanceof DataFlavor)) ? false : equals((DataFlavor)paramObject);
  }
  
  @Deprecated
  public boolean equals(String paramString) {
    return (paramString == null) ? false : isMimeTypeEqual(paramString);
  }
  
  public boolean equals(DataFlavor paramDataFlavor) {
    if (paramDataFlavor == this)
      return true; 
    if (paramDataFlavor == null)
      return false; 
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType == null)
      return (paramDataFlavor.mimeInfo == null); 
    if (!mimeType.equals(paramDataFlavor.mimeInfo) || !this.representationClass.equals(paramDataFlavor.representationClass))
      return false; 
    if (!this.mimeInfo.getPrimaryType().equals("text") || isUnicodeFlavor())
      return true; 
    String str2 = getCharset();
    String str1 = paramDataFlavor.getCharset();
    return (!isCharsetSupported(str2) || !isCharsetSupported(str1)) ? str2.equalsIgnoreCase(str1) : Charset.forName(str2).equals(Charset.forName(str1));
  }
  
  public final Class<?> getDefaultRepresentationClass() {
    return InputStream.class;
  }
  
  public final String getDefaultRepresentationClassAsString() {
    return getDefaultRepresentationClass().getName();
  }
  
  public String getHumanPresentableName() {
    return this.humanPresentableName;
  }
  
  public MimeTypeProcessor.MimeType getMimeInfo() {
    return this.mimeInfo;
  }
  
  public String getMimeType() {
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null) {
      String str = MimeTypeProcessor.assemble(mimeType);
    } else {
      mimeType = null;
    } 
    return (String)mimeType;
  }
  
  public String getParameter(String paramString) {
    String str = paramString.toLowerCase();
    if (str.equals("humanpresentablename"))
      return this.humanPresentableName; 
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null) {
      String str1 = mimeType.getParameter(str);
    } else {
      mimeType = null;
    } 
    return (String)mimeType;
  }
  
  public String getPrimaryType() {
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null) {
      String str = mimeType.getPrimaryType();
    } else {
      mimeType = null;
    } 
    return (String)mimeType;
  }
  
  public Reader getReaderForText(Transferable paramTransferable) throws UnsupportedFlavorException, IOException {
    Object object = paramTransferable.getTransferData(this);
    if (object != null) {
      if (object instanceof Reader) {
        object = object;
        object.reset();
        return (Reader)object;
      } 
      if (object instanceof String)
        return new StringReader((String)object); 
      if (object instanceof CharBuffer)
        return new CharArrayReader(((CharBuffer)object).array()); 
      if (object instanceof char[])
        return new CharArrayReader((char[])object); 
      String str = getCharset();
      if (object instanceof InputStream) {
        object = object;
        object.reset();
      } else if (object instanceof ByteBuffer) {
        object = new ByteArrayInputStream(((ByteBuffer)object).array());
      } else if (object instanceof byte[]) {
        object = new ByteArrayInputStream((byte[])object);
      } else {
        throw new IllegalArgumentException(Messages.getString("awt.16F"));
      } 
      return (str.length() == 0) ? new InputStreamReader((InputStream)object) : new InputStreamReader((InputStream)object, str);
    } 
    throw new IllegalArgumentException(Messages.getString("awt.16E"));
  }
  
  public Class<?> getRepresentationClass() {
    return this.representationClass;
  }
  
  public String getSubType() {
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null) {
      String str = mimeType.getSubType();
    } else {
      mimeType = null;
    } 
    return (String)mimeType;
  }
  
  public int hashCode() {
    return getKeyInfo().hashCode();
  }
  
  public boolean isFlavorJavaFileListType() {
    return (List.class.isAssignableFrom(this.representationClass) && isMimeTypeEqual(javaFileListFlavor));
  }
  
  public boolean isFlavorRemoteObjectType() {
    return (isMimeTypeEqual("application/x-java-remote-object") && isRepresentationClassRemote());
  }
  
  public boolean isFlavorSerializedObjectType() {
    return (isMimeTypeSerializedObject() && isRepresentationClassSerializable());
  }
  
  public boolean isFlavorTextType() {
    if (equals(stringFlavor) || equals(plainTextFlavor))
      return true; 
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null && !mimeType.getPrimaryType().equals("text"))
      return false; 
    String str = getCharset();
    return isByteCodeFlavor() ? ((str.length() != 0) ? isCharsetSupported(str) : true) : isUnicodeFlavor();
  }
  
  public boolean isMimeTypeEqual(String paramString) {
    try {
      return this.mimeInfo.equals(MimeTypeProcessor.parse(paramString));
    } catch (IllegalArgumentException illegalArgumentException) {
      return false;
    } 
  }
  
  public final boolean isMimeTypeEqual(DataFlavor paramDataFlavor) {
    boolean bool;
    MimeTypeProcessor.MimeType mimeType = this.mimeInfo;
    if (mimeType != null) {
      bool = mimeType.equals(paramDataFlavor.mimeInfo);
    } else if (paramDataFlavor.mimeInfo == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isMimeTypeSerializedObject() {
    return isMimeTypeEqual("application/x-java-serialized-object");
  }
  
  public boolean isRepresentationClassByteBuffer() {
    return ByteBuffer.class.isAssignableFrom(this.representationClass);
  }
  
  public boolean isRepresentationClassCharBuffer() {
    return CharBuffer.class.isAssignableFrom(this.representationClass);
  }
  
  public boolean isRepresentationClassInputStream() {
    return InputStream.class.isAssignableFrom(this.representationClass);
  }
  
  public boolean isRepresentationClassReader() {
    return Reader.class.isAssignableFrom(this.representationClass);
  }
  
  public boolean isRepresentationClassRemote() {
    return false;
  }
  
  public boolean isRepresentationClassSerializable() {
    return Serializable.class.isAssignableFrom(this.representationClass);
  }
  
  public boolean match(DataFlavor paramDataFlavor) {
    return equals(paramDataFlavor);
  }
  
  @Deprecated
  public String normalizeMimeType(String paramString) {
    return paramString;
  }
  
  @Deprecated
  public String normalizeMimeTypeParameter(String paramString1, String paramString2) {
    return paramString2;
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokeinterface readObject : ()Ljava/lang/Object;
    //   9: checkcast java/lang/String
    //   12: putfield humanPresentableName : Ljava/lang/String;
    //   15: aload_1
    //   16: invokeinterface readObject : ()Ljava/lang/Object;
    //   21: checkcast myjava/awt/datatransfer/MimeTypeProcessor$MimeType
    //   24: astore_1
    //   25: aload_0
    //   26: aload_1
    //   27: putfield mimeInfo : Lmyjava/awt/datatransfer/MimeTypeProcessor$MimeType;
    //   30: aload_1
    //   31: ifnull -> 47
    //   34: aload_1
    //   35: ldc 'class'
    //   37: invokevirtual getParameter : (Ljava/lang/String;)Ljava/lang/String;
    //   40: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   43: astore_1
    //   44: goto -> 49
    //   47: aconst_null
    //   48: astore_1
    //   49: aload_0
    //   50: aload_1
    //   51: putfield representationClass : Ljava/lang/Class;
    //   54: aload_0
    //   55: monitorexit
    //   56: return
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	57	finally
    //   34	44	57	finally
    //   49	54	57	finally
  }
  
  public void setHumanPresentableName(String paramString) {
    this.humanPresentableName = paramString;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(String.valueOf(getClass().getName()));
    stringBuilder.append("[MimeType=(");
    stringBuilder.append(getMimeType());
    stringBuilder.append(");humanPresentableName=");
    stringBuilder.append(this.humanPresentableName);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: getfield humanPresentableName : Ljava/lang/String;
    //   7: invokeinterface writeObject : (Ljava/lang/Object;)V
    //   12: aload_1
    //   13: aload_0
    //   14: getfield mimeInfo : Lmyjava/awt/datatransfer/MimeTypeProcessor$MimeType;
    //   17: invokeinterface writeObject : (Ljava/lang/Object;)V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
}
