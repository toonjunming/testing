package com.mysql.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

public class JDBC4Connection extends ConnectionImpl implements JDBC4MySQLConnection {
  private static final long serialVersionUID = 2877471301981509475L;
  
  private JDBC4ClientInfoProvider infoProvider;
  
  public JDBC4Connection(String paramString1, int paramInt, Properties paramProperties, String paramString2, String paramString3) throws SQLException {
    super(paramString1, paramInt, paramProperties, paramString2, paramString3);
  }
  
  public Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public Blob createBlob() {
    return new Blob(getExceptionInterceptor());
  }
  
  public Clob createClob() {
    return new Clob(getExceptionInterceptor());
  }
  
  public NClob createNClob() {
    return new JDBC4NClob(getExceptionInterceptor());
  }
  
  public SQLXML createSQLXML() throws SQLException {
    return new JDBC4MysqlSQLXML(getExceptionInterceptor());
  }
  
  public Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException {
    throw SQLError.createSQLFeatureNotSupportedException();
  }
  
  public String getClientInfo(String paramString) throws SQLException {
    return getClientInfoProviderImpl().getClientInfo(this, paramString);
  }
  
  public Properties getClientInfo() throws SQLException {
    return getClientInfoProviderImpl().getClientInfo(this);
  }
  
  public JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
    synchronized (getConnectionMutex()) {
      JDBC4ClientInfoProvider jDBC4ClientInfoProvider = this.infoProvider;
      if (jDBC4ClientInfoProvider == null)
        try {
          String str = getClientInfoProvider();
          ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
          this.infoProvider = (JDBC4ClientInfoProvider)Util.getInstance(str, new Class[0], new Object[0], exceptionInterceptor);
          this.infoProvider.initialize(this, this.props);
        } catch (SQLException sQLException) {
          if (sQLException.getCause() instanceof ClassCastException) {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("com.mysql.jdbc.");
            stringBuilder.append(getClientInfoProvider());
            String str = stringBuilder.toString();
            ExceptionInterceptor exceptionInterceptor = getExceptionInterceptor();
            this.infoProvider = (JDBC4ClientInfoProvider)Util.getInstance(str, new Class[0], new Object[0], exceptionInterceptor);
          } 
          this.infoProvider.initialize(this, this.props);
        } catch (ClassCastException classCastException) {
          throw SQLError.createSQLException(Messages.getString("JDBC4Connection.ClientInfoNotImplemented", new Object[] { getClientInfoProvider() }), "S1009", getExceptionInterceptor());
        }  
      jDBC4ClientInfoProvider = this.infoProvider;
      return jDBC4ClientInfoProvider;
    } 
  }
  
  public boolean isValid(int paramInt) throws SQLException {
    synchronized (getConnectionMutex()) {
      if (isClosed())
        return false; 
      try {
        return true;
      } finally {
        Exception exception = null;
        try {
          abortInternal();
        } finally {}
      } 
    } 
  }
  
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    checkClosed();
    return paramClass.isInstance(this);
  }
  
  public void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException {
    try {
      getClientInfoProviderImpl().setClientInfo(this, paramString1, paramString2);
      return;
    } catch (SQLClientInfoException sQLClientInfoException) {
      throw sQLClientInfoException;
    } catch (SQLException sQLException) {
      SQLClientInfoException sQLClientInfoException = new SQLClientInfoException();
      sQLClientInfoException.initCause(sQLException);
      throw sQLClientInfoException;
    } 
  }
  
  public void setClientInfo(Properties paramProperties) throws SQLClientInfoException {
    try {
      getClientInfoProviderImpl().setClientInfo(this, paramProperties);
      return;
    } catch (SQLClientInfoException sQLClientInfoException) {
      throw sQLClientInfoException;
    } catch (SQLException sQLException) {
      SQLClientInfoException sQLClientInfoException = new SQLClientInfoException();
      sQLClientInfoException.initCause(sQLException);
      throw sQLClientInfoException;
    } 
  }
  
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    try {
      return paramClass.cast(this);
    } catch (ClassCastException classCastException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to unwrap to ");
      stringBuilder.append(paramClass.toString());
      throw SQLError.createSQLException(stringBuilder.toString(), "S1009", getExceptionInterceptor());
    } 
  }
}
