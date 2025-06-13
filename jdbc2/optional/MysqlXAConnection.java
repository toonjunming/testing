package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.Util;
import com.mysql.jdbc.log.Log;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class MysqlXAConnection extends MysqlPooledConnection implements XAConnection, XAResource {
  private static final Constructor<?> JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
  
  private static final int MAX_COMMAND_LENGTH = 300;
  
  private static final Map<Integer, Integer> MYSQL_ERROR_CODES_TO_XA_ERROR_CODES;
  
  private Log log;
  
  public boolean logXaCommands;
  
  private Connection underlyingConnection;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put(Integer.valueOf(1397), Integer.valueOf(-4));
    hashMap.put(Integer.valueOf(1398), Integer.valueOf(-5));
    hashMap.put(Integer.valueOf(1399), Integer.valueOf(-7));
    hashMap.put(Integer.valueOf(1400), Integer.valueOf(-9));
    hashMap.put(Integer.valueOf(1401), Integer.valueOf(-3));
    hashMap.put(Integer.valueOf(1402), Integer.valueOf(100));
    hashMap.put(Integer.valueOf(1440), Integer.valueOf(-8));
    hashMap.put(Integer.valueOf(1613), Integer.valueOf(106));
    hashMap.put(Integer.valueOf(1614), Integer.valueOf(102));
    MYSQL_ERROR_CODES_TO_XA_ERROR_CODES = Collections.unmodifiableMap(hashMap);
    if (Util.isJdbc4()) {
      try {
        JDBC_4_XA_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlXAConnection").getConstructor(new Class[] { Connection.class, boolean.class });
      } catch (SecurityException securityException) {
        throw new RuntimeException(securityException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } catch (ClassNotFoundException classNotFoundException) {
        throw new RuntimeException(classNotFoundException);
      } 
    } else {
      JDBC_4_XA_CONNECTION_WRAPPER_CTOR = null;
    } 
  }
  
  public MysqlXAConnection(Connection paramConnection, boolean paramBoolean) throws SQLException {
    super(paramConnection);
    this.underlyingConnection = paramConnection;
    this.log = paramConnection.getLog();
    this.logXaCommands = paramBoolean;
  }
  
  private static void appendXid(StringBuilder paramStringBuilder, Xid paramXid) {
    byte[] arrayOfByte1 = paramXid.getGlobalTransactionId();
    byte[] arrayOfByte2 = paramXid.getBranchQualifier();
    if (arrayOfByte1 != null)
      StringUtils.appendAsHex(paramStringBuilder, arrayOfByte1); 
    paramStringBuilder.append(',');
    if (arrayOfByte2 != null)
      StringUtils.appendAsHex(paramStringBuilder, arrayOfByte2); 
    paramStringBuilder.append(',');
    StringUtils.appendAsHex(paramStringBuilder, paramXid.getFormatId());
  }
  
  private ResultSet dispatchCommand(String paramString) throws XAException {
    Statement statement4 = null;
    Statement statement3 = null;
    Statement statement1 = statement3;
    Statement statement2 = statement4;
    try {
      if (this.logXaCommands) {
        statement1 = statement3;
        statement2 = statement4;
        Log log = this.log;
        statement1 = statement3;
        statement2 = statement4;
        StringBuilder stringBuilder = new StringBuilder();
        statement1 = statement3;
        statement2 = statement4;
        this();
        statement1 = statement3;
        statement2 = statement4;
        stringBuilder.append("Executing XA statement: ");
        statement1 = statement3;
        statement2 = statement4;
        stringBuilder.append(paramString);
        statement1 = statement3;
        statement2 = statement4;
        log.logDebug(stringBuilder.toString());
      } 
      statement1 = statement3;
      statement2 = statement4;
      statement3 = this.underlyingConnection.createStatement();
      statement1 = statement3;
      statement2 = statement3;
      statement3.execute(paramString);
      statement1 = statement3;
      statement2 = statement3;
      ResultSet resultSet = statement3.getResultSet();
      if (statement3 != null)
        try {
          statement3.close();
        } catch (SQLException sQLException) {} 
      return resultSet;
    } catch (SQLException sQLException) {
      statement1 = statement2;
      throw mapXAExceptionFromSQLException(sQLException);
    } finally {}
    if (statement1 != null)
      try {
        statement1.close();
      } catch (SQLException sQLException) {} 
    throw paramString;
  }
  
  public static MysqlXAConnection getInstance(Connection paramConnection, boolean paramBoolean) throws SQLException {
    if (!Util.isJdbc4())
      return new MysqlXAConnection(paramConnection, paramBoolean); 
    Constructor<?> constructor = JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramConnection.getExceptionInterceptor();
    return (MysqlXAConnection)Util.handleNewInstance(constructor, new Object[] { paramConnection, Boolean.valueOf(paramBoolean) }, exceptionInterceptor);
  }
  
  public static XAException mapXAExceptionFromSQLException(SQLException paramSQLException) {
    Integer integer = MYSQL_ERROR_CODES_TO_XA_ERROR_CODES.get(Integer.valueOf(paramSQLException.getErrorCode()));
    return (integer != null) ? (XAException)(new MysqlXAException(integer.intValue(), paramSQLException.getMessage(), null)).initCause(paramSQLException) : (XAException)(new MysqlXAException(-7, Messages.getString("MysqlXAConnection.003"), null)).initCause(paramSQLException);
  }
  
  public static Xid[] recover(Connection paramConnection, int paramInt) throws XAException {
    int i;
    int j;
    boolean bool = false;
    if ((0x1000000 & paramInt) > 0) {
      i = 1;
    } else {
      i = 0;
    } 
    if ((0x800000 & paramInt) > 0) {
      j = 1;
    } else {
      j = 0;
    } 
    byte[] arrayOfByte = null;
    Statement statement1 = null;
    Statement statement2 = null;
    if (i || j || paramInt == 0) {
      MysqlXid mysqlXid;
      if (!i)
        return new Xid[0]; 
      ArrayList<MysqlXid> arrayList = new ArrayList();
      try {
        Statement statement = paramConnection.createStatement();
        statement1 = statement2;
        statement2 = statement;
      } catch (SQLException exception) {
        paramConnection = null;
      } finally {
        statement2 = null;
      } 
      Connection connection = paramConnection;
      throw mapXAExceptionFromSQLException(exception);
    } 
    throw new MysqlXAException(-5, Messages.getString("MysqlXAConnection.001"), null);
  }
  
  public void commit(Xid paramXid, boolean paramBoolean) throws XAException {
    StringBuilder stringBuilder = new StringBuilder(300);
    stringBuilder.append("XA COMMIT ");
    appendXid(stringBuilder, paramXid);
    if (paramBoolean)
      stringBuilder.append(" ONE PHASE"); 
    try {
      dispatchCommand(stringBuilder.toString());
      return;
    } finally {
      this.underlyingConnection.setInGlobalTx(false);
    } 
  }
  
  public void end(Xid paramXid, int paramInt) throws XAException {
    StringBuilder stringBuilder = new StringBuilder(300);
    stringBuilder.append("XA END ");
    appendXid(stringBuilder, paramXid);
    if (paramInt != 33554432) {
      if (paramInt != 67108864 && paramInt != 536870912)
        throw new XAException(-5); 
    } else {
      stringBuilder.append(" SUSPEND");
    } 
    dispatchCommand(stringBuilder.toString());
  }
  
  public void forget(Xid paramXid) throws XAException {}
  
  public Connection getConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: iconst_1
    //   5: invokevirtual getConnection : (ZZ)Ljava/sql/Connection;
    //   8: astore_1
    //   9: aload_0
    //   10: monitorexit
    //   11: aload_1
    //   12: areturn
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	13	finally
  }
  
  public int getTransactionTimeout() throws XAException {
    return 0;
  }
  
  public XAResource getXAResource() throws SQLException {
    return this;
  }
  
  public boolean isSameRM(XAResource paramXAResource) throws XAException {
    return (paramXAResource instanceof MysqlXAConnection) ? this.underlyingConnection.isSameResource(((MysqlXAConnection)paramXAResource).underlyingConnection) : false;
  }
  
  public int prepare(Xid paramXid) throws XAException {
    StringBuilder stringBuilder = new StringBuilder(300);
    stringBuilder.append("XA PREPARE ");
    appendXid(stringBuilder, paramXid);
    dispatchCommand(stringBuilder.toString());
    return 0;
  }
  
  public Xid[] recover(int paramInt) throws XAException {
    return recover(this.underlyingConnection, paramInt);
  }
  
  public void rollback(Xid paramXid) throws XAException {
    StringBuilder stringBuilder = new StringBuilder(300);
    stringBuilder.append("XA ROLLBACK ");
    appendXid(stringBuilder, paramXid);
    try {
      dispatchCommand(stringBuilder.toString());
      return;
    } finally {
      this.underlyingConnection.setInGlobalTx(false);
    } 
  }
  
  public boolean setTransactionTimeout(int paramInt) throws XAException {
    return false;
  }
  
  public void start(Xid paramXid, int paramInt) throws XAException {
    StringBuilder stringBuilder = new StringBuilder(300);
    stringBuilder.append("XA START ");
    appendXid(stringBuilder, paramXid);
    if (paramInt != 0)
      if (paramInt != 2097152) {
        if (paramInt == 134217728) {
          stringBuilder.append(" RESUME");
        } else {
          throw new XAException(-5);
        } 
      } else {
        stringBuilder.append(" JOIN");
      }  
    dispatchCommand(stringBuilder.toString());
    this.underlyingConnection.setInGlobalTx(true);
  }
}
