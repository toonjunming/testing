package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class SuspendableXAConnection extends MysqlPooledConnection implements XAConnection, XAResource {
  private static final Constructor<?> JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
  
  private static final Map<Xid, XAConnection> XIDS_TO_PHYSICAL_CONNECTIONS = new HashMap<Xid, XAConnection>();
  
  private XAConnection currentXAConnection;
  
  private XAResource currentXAResource;
  
  private Xid currentXid;
  
  private Connection underlyingConnection;
  
  public SuspendableXAConnection(Connection paramConnection) {
    super(paramConnection);
    this.underlyingConnection = paramConnection;
  }
  
  private static XAConnection findConnectionForXid(Connection paramConnection, Xid paramXid) throws SQLException {
    throw null;
  }
  
  public static SuspendableXAConnection getInstance(Connection paramConnection) throws SQLException {
    if (!Util.isJdbc4())
      return new SuspendableXAConnection(paramConnection); 
    Constructor<?> constructor = JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
    ExceptionInterceptor exceptionInterceptor = paramConnection.getExceptionInterceptor();
    return (SuspendableXAConnection)Util.handleNewInstance(constructor, new Object[] { paramConnection }, exceptionInterceptor);
  }
  
  private static void removeXAConnectionMapping(Xid paramXid) {
    // Byte code:
    //   0: ldc com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection
    //   2: monitorenter
    //   3: getstatic com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection.XIDS_TO_PHYSICAL_CONNECTIONS : Ljava/util/Map;
    //   6: aload_0
    //   7: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: pop
    //   13: ldc com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection
    //   15: monitorexit
    //   16: return
    //   17: astore_0
    //   18: ldc com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection
    //   20: monitorexit
    //   21: aload_0
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   3	13	17	finally
  }
  
  private void switchToXid(Xid paramXid) throws XAException {
    XAException xAException;
    /* monitor enter ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection}} */
    if (paramXid != null) {
      try {
        if (!paramXid.equals(this.currentXid)) {
          XAConnection xAConnection = findConnectionForXid(this.underlyingConnection, paramXid);
          this.currentXAConnection = xAConnection;
          this.currentXid = paramXid;
          this.currentXAResource = xAConnection.getXAResource();
        } 
        /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection}} */
        return;
      } catch (SQLException sQLException) {
        xAException = new XAException();
        this();
        throw xAException;
      } finally {}
    } else {
      xAException = new XAException();
      this();
      throw xAException;
    } 
    /* monitor exit ThisExpression{ObjectType{com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection}} */
    throw xAException;
  }
  
  public void close() throws SQLException {
    if (this.currentXAConnection == null) {
      super.close();
    } else {
      removeXAConnectionMapping(this.currentXid);
      this.currentXAConnection.close();
    } 
  }
  
  public void commit(Xid paramXid, boolean paramBoolean) throws XAException {
    switchToXid(paramXid);
    this.currentXAResource.commit(paramXid, paramBoolean);
    removeXAConnectionMapping(paramXid);
  }
  
  public void end(Xid paramXid, int paramInt) throws XAException {
    switchToXid(paramXid);
    this.currentXAResource.end(paramXid, paramInt);
  }
  
  public void forget(Xid paramXid) throws XAException {
    switchToXid(paramXid);
    this.currentXAResource.forget(paramXid);
    removeXAConnectionMapping(paramXid);
  }
  
  public Connection getConnection() throws SQLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentXAConnection : Ljavax/sql/XAConnection;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 22
    //   11: aload_0
    //   12: iconst_0
    //   13: iconst_1
    //   14: invokevirtual getConnection : (ZZ)Ljava/sql/Connection;
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: areturn
    //   22: aload_1
    //   23: invokeinterface getConnection : ()Ljava/sql/Connection;
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: areturn
    //   33: astore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	33	finally
    //   11	18	33	finally
    //   22	29	33	finally
  }
  
  public int getTransactionTimeout() throws XAException {
    return 0;
  }
  
  public XAResource getXAResource() throws SQLException {
    return this;
  }
  
  public boolean isSameRM(XAResource paramXAResource) throws XAException {
    boolean bool;
    if (paramXAResource == this) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int prepare(Xid paramXid) throws XAException {
    switchToXid(paramXid);
    return this.currentXAResource.prepare(paramXid);
  }
  
  public Xid[] recover(int paramInt) throws XAException {
    return MysqlXAConnection.recover(this.underlyingConnection, paramInt);
  }
  
  public void rollback(Xid paramXid) throws XAException {
    switchToXid(paramXid);
    this.currentXAResource.rollback(paramXid);
    removeXAConnectionMapping(paramXid);
  }
  
  public boolean setTransactionTimeout(int paramInt) throws XAException {
    return false;
  }
  
  public void start(Xid paramXid, int paramInt) throws XAException {
    switchToXid(paramXid);
    if (paramInt != 2097152) {
      this.currentXAResource.start(paramXid, paramInt);
      return;
    } 
    this.currentXAResource.start(paramXid, 134217728);
  }
  
  static {
    if (Util.isJdbc4()) {
      try {
        JDBC_4_XA_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4SuspendableXAConnection").getConstructor(new Class[] { Connection.class });
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
}
