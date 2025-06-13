package okhttp3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class Headers {
  private final String[] namesAndValues;
  
  public Headers(Builder paramBuilder) {
    List<String> list = paramBuilder.namesAndValues;
    this.namesAndValues = list.<String>toArray(new String[list.size()]);
  }
  
  private Headers(String[] paramArrayOfString) {
    this.namesAndValues = paramArrayOfString;
  }
  
  public static void checkName(String paramString) {
    Objects.requireNonNull(paramString, "name == null");
    if (!paramString.isEmpty()) {
      int i = paramString.length();
      byte b = 0;
      while (b < i) {
        char c = paramString.charAt(b);
        if (c > ' ' && c < '') {
          b++;
          continue;
        } 
        throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", new Object[] { Integer.valueOf(c), Integer.valueOf(b), paramString }));
      } 
      return;
    } 
    throw new IllegalArgumentException("name is empty");
  }
  
  public static void checkValue(String paramString1, String paramString2) {
    if (paramString1 != null) {
      int i = paramString1.length();
      byte b = 0;
      while (b < i) {
        char c = paramString1.charAt(b);
        if ((c > '\037' || c == '\t') && c < '') {
          b++;
          continue;
        } 
        throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", new Object[] { Integer.valueOf(c), Integer.valueOf(b), paramString2, paramString1 }));
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("value for name ");
    stringBuilder.append(paramString2);
    stringBuilder.append(" == null");
    throw new NullPointerException(stringBuilder.toString());
  }
  
  @Nullable
  private static String get(String[] paramArrayOfString, String paramString) {
    for (int i = paramArrayOfString.length - 2; i >= 0; i -= 2) {
      if (paramString.equalsIgnoreCase(paramArrayOfString[i]))
        return paramArrayOfString[i + 1]; 
    } 
    return null;
  }
  
  public static Headers of(Map<String, String> paramMap) {
    Objects.requireNonNull(paramMap, "headers == null");
    String[] arrayOfString = new String[paramMap.size() * 2];
    byte b = 0;
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      if (entry.getKey() != null && entry.getValue() != null) {
        String str1 = ((String)entry.getKey()).trim();
        String str2 = ((String)entry.getValue()).trim();
        checkName(str1);
        checkValue(str2, str1);
        arrayOfString[b] = str1;
        arrayOfString[b + 1] = str2;
        b += 2;
        continue;
      } 
      throw new IllegalArgumentException("Headers cannot be null");
    } 
    return new Headers(arrayOfString);
  }
  
  public static Headers of(String... paramVarArgs) {
    Objects.requireNonNull(paramVarArgs, "namesAndValues == null");
    if (paramVarArgs.length % 2 == 0) {
      byte b2;
      String[] arrayOfString = (String[])paramVarArgs.clone();
      byte b3 = 0;
      byte b1 = 0;
      while (true) {
        b2 = b3;
        if (b1 < arrayOfString.length) {
          if (arrayOfString[b1] != null) {
            arrayOfString[b1] = arrayOfString[b1].trim();
            b1++;
            continue;
          } 
          throw new IllegalArgumentException("Headers cannot be null");
        } 
        break;
      } 
      while (b2 < arrayOfString.length) {
        String str1 = arrayOfString[b2];
        String str2 = arrayOfString[b2 + 1];
        checkName(str1);
        checkValue(str2, str1);
        b2 += 2;
      } 
      return new Headers(arrayOfString);
    } 
    throw new IllegalArgumentException("Expected alternating header names and values");
  }
  
  public long byteCount() {
    String[] arrayOfString = this.namesAndValues;
    long l = (arrayOfString.length * 2);
    int i = arrayOfString.length;
    for (byte b = 0; b < i; b++)
      l += this.namesAndValues[b].length(); 
    return l;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool;
    if (paramObject instanceof Headers && Arrays.equals((Object[])((Headers)paramObject).namesAndValues, (Object[])this.namesAndValues)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Nullable
  public String get(String paramString) {
    return get(this.namesAndValues, paramString);
  }
  
  @Nullable
  public Date getDate(String paramString) {
    paramString = get(paramString);
    if (paramString != null) {
      Date date = HttpDate.parse(paramString);
    } else {
      paramString = null;
    } 
    return (Date)paramString;
  }
  
  @Nullable
  @IgnoreJRERequirement
  public Instant getInstant(String paramString) {
    Date date = getDate(paramString);
    if (date != null) {
      Instant instant = date.toInstant();
    } else {
      date = null;
    } 
    return (Instant)date;
  }
  
  public int hashCode() {
    return Arrays.hashCode((Object[])this.namesAndValues);
  }
  
  public String name(int paramInt) {
    return this.namesAndValues[paramInt * 2];
  }
  
  public Set<String> names() {
    TreeSet<String> treeSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    int i = size();
    for (byte b = 0; b < i; b++)
      treeSet.add(name(b)); 
    return Collections.unmodifiableSet(treeSet);
  }
  
  public Builder newBuilder() {
    Builder builder = new Builder();
    Collections.addAll(builder.namesAndValues, this.namesAndValues);
    return builder;
  }
  
  public int size() {
    return this.namesAndValues.length / 2;
  }
  
  public Map<String, List<String>> toMultimap() {
    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
    int i = size();
    for (byte b = 0; b < i; b++) {
      String str = name(b).toLowerCase(Locale.US);
      List<String> list2 = (List)treeMap.get(str);
      List<String> list1 = list2;
      if (list2 == null) {
        list1 = new ArrayList(2);
        treeMap.put(str, list1);
      } 
      list1.add(value(b));
    } 
    return (Map)treeMap;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    int i = size();
    for (byte b = 0; b < i; b++) {
      stringBuilder.append(name(b));
      stringBuilder.append(": ");
      stringBuilder.append(value(b));
      stringBuilder.append("\n");
    } 
    return stringBuilder.toString();
  }
  
  public String value(int paramInt) {
    return this.namesAndValues[paramInt * 2 + 1];
  }
  
  public List<String> values(String paramString) {
    List<?> list;
    int i = size();
    ArrayList<String> arrayList = null;
    byte b = 0;
    while (b < i) {
      ArrayList<String> arrayList1 = arrayList;
      if (paramString.equalsIgnoreCase(name(b))) {
        arrayList1 = arrayList;
        if (arrayList == null)
          arrayList1 = new ArrayList(2); 
        arrayList1.add(value(b));
      } 
      b++;
      arrayList = arrayList1;
    } 
    if (arrayList != null) {
      list = Collections.unmodifiableList(arrayList);
    } else {
      list = Collections.emptyList();
    } 
    return (List)list;
  }
  
  public static final class Builder {
    public final List<String> namesAndValues = new ArrayList<String>(20);
    
    public Builder add(String param1String) {
      int i = param1String.indexOf(":");
      if (i != -1)
        return add(param1String.substring(0, i).trim(), param1String.substring(i + 1)); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected header: ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder add(String param1String1, String param1String2) {
      Headers.checkName(param1String1);
      Headers.checkValue(param1String2, param1String1);
      return addLenient(param1String1, param1String2);
    }
    
    @IgnoreJRERequirement
    public Builder add(String param1String, Instant param1Instant) {
      if (param1Instant != null)
        return add(param1String, new Date(param1Instant.toEpochMilli())); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("value for name ");
      stringBuilder.append(param1String);
      stringBuilder.append(" == null");
      throw new NullPointerException(stringBuilder.toString());
    }
    
    public Builder add(String param1String, Date param1Date) {
      if (param1Date != null) {
        add(param1String, HttpDate.format(param1Date));
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("value for name ");
      stringBuilder.append(param1String);
      stringBuilder.append(" == null");
      throw new NullPointerException(stringBuilder.toString());
    }
    
    public Builder addAll(Headers param1Headers) {
      int i = param1Headers.size();
      for (byte b = 0; b < i; b++)
        addLenient(param1Headers.name(b), param1Headers.value(b)); 
      return this;
    }
    
    public Builder addLenient(String param1String) {
      int i = param1String.indexOf(":", 1);
      return (i != -1) ? addLenient(param1String.substring(0, i), param1String.substring(i + 1)) : (param1String.startsWith(":") ? addLenient("", param1String.substring(1)) : addLenient("", param1String));
    }
    
    public Builder addLenient(String param1String1, String param1String2) {
      this.namesAndValues.add(param1String1);
      this.namesAndValues.add(param1String2.trim());
      return this;
    }
    
    public Builder addUnsafeNonAscii(String param1String1, String param1String2) {
      Headers.checkName(param1String1);
      return addLenient(param1String1, param1String2);
    }
    
    public Headers build() {
      return new Headers(this);
    }
    
    @Nullable
    public String get(String param1String) {
      for (int i = this.namesAndValues.size() - 2; i >= 0; i -= 2) {
        if (param1String.equalsIgnoreCase(this.namesAndValues.get(i)))
          return this.namesAndValues.get(i + 1); 
      } 
      return null;
    }
    
    public Builder removeAll(String param1String) {
      for (int i = 0; i < this.namesAndValues.size(); i = j + 2) {
        int j = i;
        if (param1String.equalsIgnoreCase(this.namesAndValues.get(i))) {
          this.namesAndValues.remove(i);
          this.namesAndValues.remove(i);
          j = i - 2;
        } 
      } 
      return this;
    }
    
    public Builder set(String param1String1, String param1String2) {
      Headers.checkName(param1String1);
      Headers.checkValue(param1String2, param1String1);
      removeAll(param1String1);
      addLenient(param1String1, param1String2);
      return this;
    }
    
    @IgnoreJRERequirement
    public Builder set(String param1String, Instant param1Instant) {
      if (param1Instant != null)
        return set(param1String, new Date(param1Instant.toEpochMilli())); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("value for name ");
      stringBuilder.append(param1String);
      stringBuilder.append(" == null");
      throw new NullPointerException(stringBuilder.toString());
    }
    
    public Builder set(String param1String, Date param1Date) {
      if (param1Date != null) {
        set(param1String, HttpDate.format(param1Date));
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("value for name ");
      stringBuilder.append(param1String);
      stringBuilder.append(" == null");
      throw new NullPointerException(stringBuilder.toString());
    }
  }
}
