package com.mysql.jdbc;

import java.sql.SQLException;

public class PacketTooBigException extends SQLException {
  public static final long serialVersionUID = 7248633977685452174L;
  
  public PacketTooBigException(long paramLong1, long paramLong2) {
    super(stringBuilder.toString(), "S1000");
  }
}
