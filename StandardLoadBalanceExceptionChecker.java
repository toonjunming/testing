package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class StandardLoadBalanceExceptionChecker implements LoadBalanceExceptionChecker {
  private List<Class<?>> sqlExClassList;
  
  private List<String> sqlStateList;
  
  private void configureSQLExceptionSubclassList(String paramString) {
    if (paramString != null && !"".equals(paramString)) {
      List<String> list = StringUtils.split(paramString, ",", true);
      ArrayList<Class<?>> arrayList = new ArrayList();
      for (String str : list) {
        try {
          arrayList.add(Class.forName(str));
        } catch (Exception exception) {}
      } 
      if (arrayList.size() > 0)
        this.sqlExClassList = arrayList; 
    } 
  }
  
  private void configureSQLStateList(String paramString) {
    if (paramString != null && !"".equals(paramString)) {
      List<String> list = StringUtils.split(paramString, ",", true);
      ArrayList<String> arrayList = new ArrayList();
      for (String str : list) {
        if (str.length() > 0)
          arrayList.add(str); 
      } 
      if (arrayList.size() > 0)
        this.sqlStateList = arrayList; 
    } 
  }
  
  public void destroy() {}
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    configureSQLStateList(paramProperties.getProperty("loadBalanceSQLStateFailover", null));
    configureSQLExceptionSubclassList(paramProperties.getProperty("loadBalanceSQLExceptionSubclassFailover", null));
  }
  
  public boolean shouldExceptionTriggerFailover(SQLException paramSQLException) {
    String str = paramSQLException.getSQLState();
    if (str != null) {
      if (str.startsWith("08"))
        return true; 
      List<String> list1 = this.sqlStateList;
      if (list1 != null) {
        Iterator<String> iterator = list1.iterator();
        while (iterator.hasNext()) {
          if (str.startsWith(((String)iterator.next()).toString()))
            return true; 
        } 
      } 
    } 
    if (paramSQLException instanceof CommunicationsException)
      return true; 
    List<Class<?>> list = this.sqlExClassList;
    if (list != null) {
      Iterator<Class<?>> iterator = list.iterator();
      while (iterator.hasNext()) {
        if (((Class)iterator.next()).isInstance(paramSQLException))
          return true; 
      } 
    } 
    return false;
  }
}
