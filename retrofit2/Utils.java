package retrofit2;

import I丨L.I1I;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.annotation.Nullable;
import okhttp3.ResponseBody;

public final class Utils {
  public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
  
  public static ResponseBody buffer(ResponseBody paramResponseBody) throws IOException {
    I1I i1I = new I1I();
    paramResponseBody.source().丨lL(i1I);
    return ResponseBody.create(paramResponseBody.contentType(), paramResponseBody.contentLength(), i1I);
  }
  
  public static void checkNotPrimitive(Type paramType) {
    if (!(paramType instanceof Class) || !((Class)paramType).isPrimitive())
      return; 
    throw new IllegalArgumentException();
  }
  
  @Nullable
  private static Class<?> declaringClassOf(TypeVariable<?> paramTypeVariable) {
    paramTypeVariable = (TypeVariable<?>)paramTypeVariable.getGenericDeclaration();
    if (paramTypeVariable instanceof Class) {
      Class clazz = (Class)paramTypeVariable;
    } else {
      paramTypeVariable = null;
    } 
    return (Class<?>)paramTypeVariable;
  }
  
  public static boolean equals(Type paramType1, Type paramType2) {
    boolean bool2 = true;
    boolean bool1 = true;
    boolean bool3 = true;
    if (paramType1 == paramType2)
      return true; 
    if (paramType1 instanceof Class)
      return paramType1.equals(paramType2); 
    if (paramType1 instanceof ParameterizedType) {
      if (!(paramType2 instanceof ParameterizedType))
        return false; 
      paramType1 = paramType1;
      ParameterizedType parameterizedType = (ParameterizedType)paramType2;
      paramType2 = paramType1.getOwnerType();
      Type type = parameterizedType.getOwnerType();
      if ((paramType2 == type || (paramType2 != null && paramType2.equals(type))) && paramType1.getRawType().equals(parameterizedType.getRawType()) && Arrays.equals((Object[])paramType1.getActualTypeArguments(), (Object[])parameterizedType.getActualTypeArguments())) {
        bool1 = bool3;
      } else {
        bool1 = false;
      } 
      return bool1;
    } 
    if (paramType1 instanceof GenericArrayType) {
      if (!(paramType2 instanceof GenericArrayType))
        return false; 
      paramType1 = paramType1;
      paramType2 = paramType2;
      return equals(paramType1.getGenericComponentType(), paramType2.getGenericComponentType());
    } 
    if (paramType1 instanceof WildcardType) {
      if (!(paramType2 instanceof WildcardType))
        return false; 
      paramType1 = paramType1;
      paramType2 = paramType2;
      if (Arrays.equals((Object[])paramType1.getUpperBounds(), (Object[])paramType2.getUpperBounds()) && Arrays.equals((Object[])paramType1.getLowerBounds(), (Object[])paramType2.getLowerBounds())) {
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      return bool1;
    } 
    if (paramType1 instanceof TypeVariable) {
      if (!(paramType2 instanceof TypeVariable))
        return false; 
      paramType1 = paramType1;
      paramType2 = paramType2;
      if (paramType1.getGenericDeclaration() != paramType2.getGenericDeclaration() || !paramType1.getName().equals(paramType2.getName()))
        bool1 = false; 
      return bool1;
    } 
    return false;
  }
  
  public static Type getGenericSupertype(Type<?> paramType, Class<?> paramClass1, Class<?> paramClass2) {
    if (paramClass2 == paramClass1)
      return paramType; 
    if (paramClass2.isInterface()) {
      Class[] arrayOfClass = paramClass1.getInterfaces();
      byte b = 0;
      int i = arrayOfClass.length;
      while (b < i) {
        if (arrayOfClass[b] == paramClass2)
          return paramClass1.getGenericInterfaces()[b]; 
        if (paramClass2.isAssignableFrom(arrayOfClass[b]))
          return getGenericSupertype(paramClass1.getGenericInterfaces()[b], arrayOfClass[b], paramClass2); 
        b++;
      } 
    } 
    if (!paramClass1.isInterface())
      while (paramClass1 != Object.class) {
        paramType = paramClass1.getSuperclass();
        if (paramType == paramClass2)
          return paramClass1.getGenericSuperclass(); 
        if (paramClass2.isAssignableFrom((Class<?>)paramType))
          return getGenericSupertype(paramClass1.getGenericSuperclass(), (Class<?>)paramType, paramClass2); 
        Type<?> type = paramType;
      }  
    return paramClass2;
  }
  
  public static Type getParameterLowerBound(int paramInt, ParameterizedType paramParameterizedType) {
    Type type2 = paramParameterizedType.getActualTypeArguments()[paramInt];
    Type type1 = type2;
    if (type2 instanceof WildcardType)
      type1 = ((WildcardType)type2).getLowerBounds()[0]; 
    return type1;
  }
  
  public static Type getParameterUpperBound(int paramInt, ParameterizedType paramParameterizedType) {
    Type type;
    Type[] arrayOfType = paramParameterizedType.getActualTypeArguments();
    if (paramInt >= 0 && paramInt < arrayOfType.length) {
      Type type1 = arrayOfType[paramInt];
      type = type1;
      if (type1 instanceof WildcardType)
        type = ((WildcardType)type1).getUpperBounds()[0]; 
      return type;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Index ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" not in range [0,");
    stringBuilder.append(arrayOfType.length);
    stringBuilder.append(") for ");
    stringBuilder.append(type);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static Class<?> getRawType(Type paramType) {
    Objects.requireNonNull(paramType, "type == null");
    if (paramType instanceof Class)
      return (Class)paramType; 
    if (paramType instanceof ParameterizedType) {
      paramType = ((ParameterizedType)paramType).getRawType();
      if (paramType instanceof Class)
        return (Class)paramType; 
      throw new IllegalArgumentException();
    } 
    if (paramType instanceof GenericArrayType)
      return Array.newInstance(getRawType(((GenericArrayType)paramType).getGenericComponentType()), 0).getClass(); 
    if (paramType instanceof TypeVariable)
      return Object.class; 
    if (paramType instanceof WildcardType)
      return getRawType(((WildcardType)paramType).getUpperBounds()[0]); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected a Class, ParameterizedType, or GenericArrayType, but <");
    stringBuilder.append(paramType);
    stringBuilder.append("> is of type ");
    stringBuilder.append(paramType.getClass().getName());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static Type getSupertype(Type paramType, Class<?> paramClass1, Class<?> paramClass2) {
    if (paramClass2.isAssignableFrom(paramClass1))
      return resolve(paramType, paramClass1, getGenericSupertype(paramType, paramClass1, paramClass2)); 
    throw new IllegalArgumentException();
  }
  
  public static boolean hasUnresolvableType(@Nullable Type paramType) {
    Type[] arrayOfType;
    String str;
    if (paramType instanceof Class)
      return false; 
    if (paramType instanceof ParameterizedType) {
      arrayOfType = ((ParameterizedType)paramType).getActualTypeArguments();
      int i = arrayOfType.length;
      for (byte b = 0; b < i; b++) {
        if (hasUnresolvableType(arrayOfType[b]))
          return true; 
      } 
      return false;
    } 
    if (arrayOfType instanceof GenericArrayType)
      return hasUnresolvableType(((GenericArrayType)arrayOfType).getGenericComponentType()); 
    if (arrayOfType instanceof TypeVariable)
      return true; 
    if (arrayOfType instanceof WildcardType)
      return true; 
    if (arrayOfType == null) {
      str = "null";
    } else {
      str = arrayOfType.getClass().getName();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected a Class, ParameterizedType, or GenericArrayType, but <");
    stringBuilder.append(arrayOfType);
    stringBuilder.append("> is of type ");
    stringBuilder.append(str);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static int indexOf(Object[] paramArrayOfObject, Object paramObject) {
    for (byte b = 0; b < paramArrayOfObject.length; b++) {
      if (paramObject.equals(paramArrayOfObject[b]))
        return b; 
    } 
    throw new NoSuchElementException();
  }
  
  public static boolean isAnnotationPresent(Annotation[] paramArrayOfAnnotation, Class<? extends Annotation> paramClass) {
    int i = paramArrayOfAnnotation.length;
    for (byte b = 0; b < i; b++) {
      if (paramClass.isInstance(paramArrayOfAnnotation[b]))
        return true; 
    } 
    return false;
  }
  
  public static RuntimeException methodError(Method paramMethod, String paramString, Object... paramVarArgs) {
    return methodError(paramMethod, null, paramString, paramVarArgs);
  }
  
  public static RuntimeException methodError(Method paramMethod, @Nullable Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    String str = String.format(paramString, paramVarArgs);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append("\n    for method ");
    stringBuilder.append(paramMethod.getDeclaringClass().getSimpleName());
    stringBuilder.append(".");
    stringBuilder.append(paramMethod.getName());
    return new IllegalArgumentException(stringBuilder.toString(), paramThrowable);
  }
  
  public static RuntimeException parameterError(Method paramMethod, int paramInt, String paramString, Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" (parameter #");
    stringBuilder.append(paramInt + 1);
    stringBuilder.append(")");
    return methodError(paramMethod, stringBuilder.toString(), paramVarArgs);
  }
  
  public static RuntimeException parameterError(Method paramMethod, Throwable paramThrowable, int paramInt, String paramString, Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" (parameter #");
    stringBuilder.append(paramInt + 1);
    stringBuilder.append(")");
    return methodError(paramMethod, paramThrowable, stringBuilder.toString(), paramVarArgs);
  }
  
  public static Type resolve(Type<?> paramType1, Class<?> paramClass, Type<?> paramType2) {
    Type[] arrayOfType1;
    Type type;
    while (paramType2 instanceof TypeVariable) {
      type = paramType2;
      paramType2 = resolveTypeVariable(paramType1, paramClass, (TypeVariable<?>)type);
      if (paramType2 == type)
        return paramType2; 
    } 
    if (paramType2 instanceof Class) {
      type = paramType2;
      if (type.isArray()) {
        paramType2 = type.getComponentType();
        paramType1 = resolve(paramType1, paramClass, paramType2);
        if (paramType2 == paramType1) {
          paramType1 = type;
        } else {
          paramType1 = new GenericArrayTypeImpl(paramType1);
        } 
        return paramType1;
      } 
    } 
    if (paramType2 instanceof GenericArrayType) {
      paramType2 = paramType2;
      type = paramType2.getGenericComponentType();
      paramType1 = resolve(paramType1, paramClass, type);
      if (type == paramType1) {
        paramType1 = paramType2;
      } else {
        paramType1 = new GenericArrayTypeImpl(paramType1);
      } 
      return paramType1;
    } 
    boolean bool = paramType2 instanceof ParameterizedType;
    byte b = 0;
    if (bool) {
      boolean bool1;
      ParameterizedType parameterizedType = (ParameterizedType)paramType2;
      paramType2 = parameterizedType.getOwnerType();
      Type type1 = resolve(paramType1, paramClass, paramType2);
      if (type1 != paramType2) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      arrayOfType1 = parameterizedType.getActualTypeArguments();
      int i = arrayOfType1.length;
      while (b < i) {
        Type type2 = resolve(paramType1, paramClass, arrayOfType1[b]);
        boolean bool2 = bool1;
        Type[] arrayOfType = arrayOfType1;
        if (type2 != arrayOfType1[b]) {
          bool2 = bool1;
          arrayOfType = arrayOfType1;
          if (!bool1) {
            arrayOfType = (Type[])arrayOfType1.clone();
            bool2 = true;
          } 
          arrayOfType[b] = type2;
        } 
        b++;
        bool1 = bool2;
        arrayOfType1 = arrayOfType;
      } 
      paramType1 = parameterizedType;
      if (bool1)
        paramType1 = new ParameterizedTypeImpl(type1, parameterizedType.getRawType(), arrayOfType1); 
      return paramType1;
    } 
    Type[] arrayOfType2 = arrayOfType1;
    if (arrayOfType1 instanceof WildcardType) {
      WildcardType wildcardType = (WildcardType)arrayOfType1;
      Type[] arrayOfType4 = wildcardType.getLowerBounds();
      Type[] arrayOfType3 = wildcardType.getUpperBounds();
      if (arrayOfType4.length == 1) {
        paramType1 = resolve(paramType1, paramClass, arrayOfType4[0]);
        type = wildcardType;
        if (paramType1 != arrayOfType4[0])
          return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { paramType1 }); 
      } else {
        type = wildcardType;
        if (arrayOfType3.length == 1) {
          type = arrayOfType3[0];
          try {
            paramType1 = resolve(paramType1, paramClass, type);
            type = wildcardType;
            if (paramType1 != arrayOfType3[0]) {
              Type[] arrayOfType = EMPTY_TYPE_ARRAY;
              return new WildcardTypeImpl(new Type[] { paramType1 }, arrayOfType);
            } 
            return type;
          } finally {}
        } 
      } 
    } 
    return type;
  }
  
  private static Type resolveTypeVariable(Type paramType, Class<?> paramClass, TypeVariable<?> paramTypeVariable) {
    Class<?> clazz = declaringClassOf(paramTypeVariable);
    if (clazz == null)
      return paramTypeVariable; 
    paramType = getGenericSupertype(paramType, paramClass, clazz);
    if (paramType instanceof ParameterizedType) {
      int i = indexOf((Object[])clazz.getTypeParameters(), paramTypeVariable);
      return ((ParameterizedType)paramType).getActualTypeArguments()[i];
    } 
    return paramTypeVariable;
  }
  
  public static void throwIfFatal(Throwable paramThrowable) {
    if (!(paramThrowable instanceof VirtualMachineError)) {
      if (!(paramThrowable instanceof ThreadDeath)) {
        if (!(paramThrowable instanceof LinkageError))
          return; 
        throw (LinkageError)paramThrowable;
      } 
      throw (ThreadDeath)paramThrowable;
    } 
    throw (VirtualMachineError)paramThrowable;
  }
  
  public static String typeToString(Type paramType) {
    String str;
    if (paramType instanceof Class) {
      str = ((Class)paramType).getName();
    } else {
      str = str.toString();
    } 
    return str;
  }
  
  public static final class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;
    
    public GenericArrayTypeImpl(Type param1Type) {
      this.componentType = param1Type;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool;
      if (param1Object instanceof GenericArrayType && Utils.equals(this, (GenericArrayType)param1Object)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Type getGenericComponentType() {
      return this.componentType;
    }
    
    public int hashCode() {
      return this.componentType.hashCode();
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Utils.typeToString(this.componentType));
      stringBuilder.append("[]");
      return stringBuilder.toString();
    }
  }
  
  public static final class ParameterizedTypeImpl implements ParameterizedType {
    @Nullable
    private final Type ownerType;
    
    private final Type rawType;
    
    private final Type[] typeArguments;
    
    public ParameterizedTypeImpl(@Nullable Type param1Type1, Type param1Type2, Type... param1VarArgs) {
      boolean bool = param1Type2 instanceof Class;
      byte b2 = 0;
      if (bool) {
        boolean bool1;
        boolean bool2 = true;
        if (param1Type1 == null) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (((Class)param1Type2).getEnclosingClass() != null)
          bool2 = false; 
        if (bool1 != bool2)
          throw new IllegalArgumentException(); 
      } 
      int i = param1VarArgs.length;
      for (byte b1 = b2; b1 < i; b1++) {
        Type type = param1VarArgs[b1];
        Objects.requireNonNull(type, "typeArgument == null");
        Utils.checkNotPrimitive(type);
      } 
      this.ownerType = param1Type1;
      this.rawType = param1Type2;
      this.typeArguments = (Type[])param1VarArgs.clone();
    }
    
    public boolean equals(Object param1Object) {
      boolean bool;
      if (param1Object instanceof ParameterizedType && Utils.equals(this, (ParameterizedType)param1Object)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Type[] getActualTypeArguments() {
      return (Type[])this.typeArguments.clone();
    }
    
    @Nullable
    public Type getOwnerType() {
      return this.ownerType;
    }
    
    public Type getRawType() {
      return this.rawType;
    }
    
    public int hashCode() {
      boolean bool;
      int i = Arrays.hashCode((Object[])this.typeArguments);
      int j = this.rawType.hashCode();
      Type type = this.ownerType;
      if (type != null) {
        bool = type.hashCode();
      } else {
        bool = false;
      } 
      return i ^ j ^ bool;
    }
    
    public String toString() {
      Type[] arrayOfType = this.typeArguments;
      if (arrayOfType.length == 0)
        return Utils.typeToString(this.rawType); 
      int i = arrayOfType.length;
      byte b = 1;
      StringBuilder stringBuilder = new StringBuilder((i + 1) * 30);
      stringBuilder.append(Utils.typeToString(this.rawType));
      stringBuilder.append("<");
      stringBuilder.append(Utils.typeToString(this.typeArguments[0]));
      while (b < this.typeArguments.length) {
        stringBuilder.append(", ");
        stringBuilder.append(Utils.typeToString(this.typeArguments[b]));
        b++;
      } 
      stringBuilder.append(">");
      return stringBuilder.toString();
    }
  }
  
  public static final class WildcardTypeImpl implements WildcardType {
    @Nullable
    private final Type lowerBound;
    
    private final Type upperBound;
    
    public WildcardTypeImpl(Type[] param1ArrayOfType1, Type[] param1ArrayOfType2) {
      if (param1ArrayOfType2.length <= 1) {
        if (param1ArrayOfType1.length == 1) {
          if (param1ArrayOfType2.length == 1) {
            Objects.requireNonNull(param1ArrayOfType2[0]);
            Utils.checkNotPrimitive(param1ArrayOfType2[0]);
            if (param1ArrayOfType1[0] == Object.class) {
              this.lowerBound = param1ArrayOfType2[0];
              this.upperBound = Object.class;
            } else {
              throw new IllegalArgumentException();
            } 
          } else {
            Objects.requireNonNull(param1ArrayOfType1[0]);
            Utils.checkNotPrimitive(param1ArrayOfType1[0]);
            this.lowerBound = null;
            this.upperBound = param1ArrayOfType1[0];
          } 
          return;
        } 
        throw new IllegalArgumentException();
      } 
      throw new IllegalArgumentException();
    }
    
    public boolean equals(Object param1Object) {
      boolean bool;
      if (param1Object instanceof WildcardType && Utils.equals(this, (WildcardType)param1Object)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Type[] getLowerBounds() {
      Type[] arrayOfType;
      Type type = this.lowerBound;
      if (type != null) {
        arrayOfType = new Type[1];
        arrayOfType[0] = type;
      } else {
        arrayOfType = Utils.EMPTY_TYPE_ARRAY;
      } 
      return arrayOfType;
    }
    
    public Type[] getUpperBounds() {
      return new Type[] { this.upperBound };
    }
    
    public int hashCode() {
      boolean bool;
      Type type = this.lowerBound;
      if (type != null) {
        bool = type.hashCode() + 31;
      } else {
        bool = true;
      } 
      return bool ^ this.upperBound.hashCode() + 31;
    }
    
    public String toString() {
      if (this.lowerBound != null) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("? super ");
        stringBuilder1.append(Utils.typeToString(this.lowerBound));
        return stringBuilder1.toString();
      } 
      if (this.upperBound == Object.class)
        return "?"; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("? extends ");
      stringBuilder.append(Utils.typeToString(this.upperBound));
      return stringBuilder.toString();
    }
  }
}
