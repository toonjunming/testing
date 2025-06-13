package retrofit2;

import java.lang.annotation.Annotation;

public final class SkipCallbackExecutorImpl implements SkipCallbackExecutor {
  private static final SkipCallbackExecutor INSTANCE = new SkipCallbackExecutorImpl();
  
  public static Annotation[] ensurePresent(Annotation[] paramArrayOfAnnotation) {
    if (Utils.isAnnotationPresent(paramArrayOfAnnotation, (Class)SkipCallbackExecutor.class))
      return paramArrayOfAnnotation; 
    Annotation[] arrayOfAnnotation = new Annotation[paramArrayOfAnnotation.length + 1];
    arrayOfAnnotation[0] = INSTANCE;
    System.arraycopy(paramArrayOfAnnotation, 0, arrayOfAnnotation, 1, paramArrayOfAnnotation.length);
    return arrayOfAnnotation;
  }
  
  public Class<? extends Annotation> annotationType() {
    return (Class)SkipCallbackExecutor.class;
  }
  
  public boolean equals(Object paramObject) {
    return paramObject instanceof SkipCallbackExecutor;
  }
  
  public int hashCode() {
    return 0;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("@");
    stringBuilder.append(SkipCallbackExecutor.class.getName());
    stringBuilder.append("()");
    return stringBuilder.toString();
  }
}
