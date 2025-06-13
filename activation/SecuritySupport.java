package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;

public class SecuritySupport {
  public static ClassLoader getContextClassLoader() {
    return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>() {
          public Object run() {
            try {
              ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            } catch (SecurityException securityException) {
              securityException = null;
            } 
            return securityException;
          }
        });
  }
  
  public static InputStream getResourceAsStream(Class paramClass, String paramString) throws IOException {
    try {
      PrivilegedExceptionAction<InputStream> privilegedExceptionAction = new PrivilegedExceptionAction() {
          private final Class val$c;
          
          private final String val$name;
          
          public Object run() throws IOException {
            return c.getResourceAsStream(name);
          }
        };
      super(paramClass, paramString);
      return AccessController.<InputStream>doPrivileged(privilegedExceptionAction);
    } catch (PrivilegedActionException privilegedActionException) {
      throw (IOException)privilegedActionException.getException();
    } 
  }
  
  public static URL[] getResources(final ClassLoader cl, final String name) {
    return AccessController.<URL[]>doPrivileged(new PrivilegedAction<URL>() {
          private final ClassLoader val$cl;
          
          private final String val$name;
          
          public Object run() {
            URL[] arrayOfURL3 = null;
            URL[] arrayOfURL2 = null;
            URL[] arrayOfURL1 = arrayOfURL3;
            try {
              ArrayList<URL> arrayList = new ArrayList();
              arrayOfURL1 = arrayOfURL3;
              this();
              arrayOfURL1 = arrayOfURL3;
              Enumeration<URL> enumeration = cl.getResources(name);
              while (enumeration != null) {
                arrayOfURL1 = arrayOfURL3;
                if (!enumeration.hasMoreElements())
                  break; 
                arrayOfURL1 = arrayOfURL3;
                URL uRL = enumeration.nextElement();
                if (uRL != null) {
                  arrayOfURL1 = arrayOfURL3;
                  arrayList.add(uRL);
                } 
              } 
              arrayOfURL1 = arrayOfURL3;
              if (arrayList.size() > 0) {
                arrayOfURL1 = arrayOfURL3;
                arrayOfURL2 = new URL[arrayList.size()];
                arrayOfURL1 = arrayOfURL2;
                arrayOfURL2 = arrayList.<URL>toArray(arrayOfURL2);
              } 
            } catch (IOException|SecurityException iOException) {
              arrayOfURL2 = arrayOfURL1;
            } 
            return arrayOfURL2;
          }
        });
  }
  
  public static URL[] getSystemResources(final String name) {
    return AccessController.<URL[]>doPrivileged(new PrivilegedAction<URL>() {
          private final String val$name;
          
          public Object run() {
            URL[] arrayOfURL3 = null;
            URL[] arrayOfURL2 = null;
            URL[] arrayOfURL1 = arrayOfURL3;
            try {
              ArrayList<URL> arrayList = new ArrayList();
              arrayOfURL1 = arrayOfURL3;
              this();
              arrayOfURL1 = arrayOfURL3;
              Enumeration<URL> enumeration = ClassLoader.getSystemResources(name);
              while (enumeration != null) {
                arrayOfURL1 = arrayOfURL3;
                if (!enumeration.hasMoreElements())
                  break; 
                arrayOfURL1 = arrayOfURL3;
                URL uRL = enumeration.nextElement();
                if (uRL != null) {
                  arrayOfURL1 = arrayOfURL3;
                  arrayList.add(uRL);
                } 
              } 
              arrayOfURL1 = arrayOfURL3;
              if (arrayList.size() > 0) {
                arrayOfURL1 = arrayOfURL3;
                arrayOfURL2 = new URL[arrayList.size()];
                arrayOfURL1 = arrayOfURL2;
                arrayOfURL2 = arrayList.<URL>toArray(arrayOfURL2);
              } 
            } catch (IOException|SecurityException iOException) {
              arrayOfURL2 = arrayOfURL1;
            } 
            return arrayOfURL2;
          }
        });
  }
  
  public static InputStream openStream(URL paramURL) throws IOException {
    try {
      PrivilegedExceptionAction<InputStream> privilegedExceptionAction = new PrivilegedExceptionAction() {
          private final URL val$url;
          
          public Object run() throws IOException {
            return url.openStream();
          }
        };
      super(paramURL);
      return AccessController.<InputStream>doPrivileged(privilegedExceptionAction);
    } catch (PrivilegedActionException privilegedActionException) {
      throw (IOException)privilegedActionException.getException();
    } 
  }
}
