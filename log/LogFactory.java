package com.mysql.jdbc.log;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class LogFactory {
  public static Log getLogger(String paramString1, String paramString2, ExceptionInterceptor paramExceptionInterceptor) throws SQLException {
    if (paramString1 != null) {
      if (paramString2 != null) {
        StringBuilder stringBuilder;
        try {
          Class<?> clazz = Class.forName(paramString1);
        } catch (ClassNotFoundException classNotFoundException) {
          try {
            StringBuilder stringBuilder1 = new StringBuilder();
            this();
            stringBuilder1.append(Util.getPackageName(Log.class));
            stringBuilder1.append(".");
            stringBuilder1.append(paramString1);
            Class<?> clazz = Class.forName(stringBuilder1.toString());
            return clazz.getConstructor(new Class[] { String.class }).newInstance(new Object[] { paramString2 });
          } catch (ClassNotFoundException classNotFoundException1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to load class for logger '");
            stringBuilder.append(paramString1);
            stringBuilder.append("'");
            SQLException sQLException = SQLError.createSQLException(stringBuilder.toString(), "S1009", paramExceptionInterceptor);
            sQLException.initCause(classNotFoundException1);
            throw sQLException;
          } 
        } catch (NoSuchMethodException noSuchMethodException) {
        
        } catch (InstantiationException instantiationException) {
        
        } catch (InvocationTargetException invocationTargetException) {
        
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (ClassCastException classCastException) {}
        return stringBuilder.getConstructor(new Class[] { String.class }).newInstance(new Object[] { classCastException });
      } 
      throw SQLError.createSQLException("Logger instance name can not be NULL", "S1009", paramExceptionInterceptor);
    } 
    throw SQLError.createSQLException("Logger class can not be NULL", "S1009", paramExceptionInterceptor);
  }
}
