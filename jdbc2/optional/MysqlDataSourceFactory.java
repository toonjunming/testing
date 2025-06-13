package com.mysql.jdbc.jdbc2.optional;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class MysqlDataSourceFactory implements ObjectFactory {
  public static final String DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
  
  public static final String POOL_DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";
  
  public static final String XA_DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
  
  private String nullSafeRefAddrStringGet(String paramString, Reference paramReference) {
    RefAddr refAddr = paramReference.get(paramString);
    if (refAddr != null) {
      String str = (String)refAddr.getContent();
    } else {
      refAddr = null;
    } 
    return (String)refAddr;
  }
  
  public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable) throws Exception {
    Reference reference = (Reference)paramObject;
    String str = reference.getClassName();
    if (str != null && (str.equals("com.mysql.jdbc.jdbc2.optional.MysqlDataSource") || str.equals("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource") || str.equals("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource")))
      try {
        paramObject = Class.forName(str).newInstance();
        int i = 3306;
        str = nullSafeRefAddrStringGet("port", reference);
        if (str != null)
          i = Integer.parseInt(str); 
        paramObject.setPort(i);
        str = nullSafeRefAddrStringGet("user", reference);
        if (str != null)
          paramObject.setUser(str); 
        str = nullSafeRefAddrStringGet("password", reference);
        if (str != null)
          paramObject.setPassword(str); 
        str = nullSafeRefAddrStringGet("serverName", reference);
        if (str != null)
          paramObject.setServerName(str); 
        str = nullSafeRefAddrStringGet("databaseName", reference);
        if (str != null)
          paramObject.setDatabaseName(str); 
        str = nullSafeRefAddrStringGet("explicitUrl", reference);
        if (str != null && Boolean.valueOf(str).booleanValue())
          paramObject.setUrl(nullSafeRefAddrStringGet("url", reference)); 
        paramObject.setPropertiesViaRef(reference);
        return paramObject;
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to create DataSource of class '");
        stringBuilder.append(str);
        stringBuilder.append("', reason: ");
        stringBuilder.append(exception.toString());
        throw new RuntimeException(stringBuilder.toString());
      }  
    return null;
  }
}
