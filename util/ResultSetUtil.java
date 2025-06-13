package com.mysql.jdbc.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetUtil {
  public static StringBuilder appendResultSetSlashGStyle(StringBuilder paramStringBuilder, ResultSet paramResultSet) throws SQLException {
    ResultSetMetaData resultSetMetaData = paramResultSet.getMetaData();
    int k = resultSetMetaData.getColumnCount();
    String[] arrayOfString = new String[k];
    int j = 0;
    int i;
    for (i = 0; j < k; i = m) {
      int n = j + 1;
      arrayOfString[j] = resultSetMetaData.getColumnLabel(n);
      int m = i;
      if (arrayOfString[j].length() > i)
        m = arrayOfString[j].length(); 
      j = n;
    } 
    for (byte b = 1; paramResultSet.next(); b++) {
      paramStringBuilder.append("*************************** ");
      paramStringBuilder.append(b);
      paramStringBuilder.append(". row ***************************\n");
      j = 0;
      while (j < k) {
        int m = arrayOfString[j].length();
        for (byte b1 = 0; b1 < i - m; b1++)
          paramStringBuilder.append(" "); 
        paramStringBuilder.append(arrayOfString[j]);
        paramStringBuilder.append(": ");
        String str = paramResultSet.getString(++j);
        if (str != null) {
          paramStringBuilder.append(str);
        } else {
          paramStringBuilder.append("NULL");
        } 
        paramStringBuilder.append("\n");
      } 
      paramStringBuilder.append("\n");
    } 
    return paramStringBuilder;
  }
}
