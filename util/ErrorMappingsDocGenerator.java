package com.mysql.jdbc.util;

import com.mysql.jdbc.SQLError;

public class ErrorMappingsDocGenerator {
  public static void main(String[] paramArrayOfString) throws Exception {
    SQLError.dumpSqlStatesMappingsAsXml();
  }
}
