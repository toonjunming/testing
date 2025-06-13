package com.mysql.jdbc;

import java.sql.SQLException;

public interface Wrapper {
  boolean isWrapperFor(Class<?> paramClass) throws SQLException;
  
  <T> T unwrap(Class<T> paramClass) throws SQLException;
}
