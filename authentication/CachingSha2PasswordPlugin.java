package com.mysql.jdbc.authentication;

import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Security;
import com.mysql.jdbc.StringUtils;
import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class CachingSha2PasswordPlugin extends Sha256PasswordPlugin {
  public static String PLUGIN_NAME = "caching_sha2_password";
  
  private AuthStage stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
  
  public void destroy() {
    this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
    super.destroy();
  }
  
  public byte[] encryptPassword() throws SQLException {
    return this.connection.versionMeetsMinimum(8, 0, 5) ? super.encryptPassword() : encryptPassword("RSA/ECB/PKCS1Padding");
  }
  
  public String getProtocolPluginName() {
    return PLUGIN_NAME;
  }
  
  public void init(Connection paramConnection, Properties paramProperties) throws SQLException {
    super.init(paramConnection, paramProperties);
    this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
  }
  
  public boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException {
    paramList.clear();
    String str = this.password;
    if (str == null || str.length() == 0 || paramBuffer == null) {
      paramList.add(new Buffer(new byte[] { 0 }));
      return true;
    } 
    AuthStage authStage = this.stage;
    if (authStage == AuthStage.FAST_AUTH_SEND_SCRAMBLE) {
      this.seed = paramBuffer.readString();
      try {
        paramBuffer = new Buffer();
        this(Security.scrambleCachingSha2(StringUtils.getBytes(this.password, this.connection.getPasswordCharacterEncoding()), this.seed.getBytes()));
        paramList.add(paramBuffer);
        this.stage = AuthStage.FAST_AUTH_READ_RESULT;
        return true;
      } catch (DigestException digestException) {
        throw SQLError.createSQLException(digestException.getMessage(), "S1000", digestException, null);
      } catch (UnsupportedEncodingException null) {
        throw SQLError.createSQLException(unsupportedEncodingException.getMessage(), "S1000", unsupportedEncodingException, null);
      } 
    } 
    if (authStage == AuthStage.FAST_AUTH_READ_RESULT) {
      byte b = unsupportedEncodingException.getByteBuffer()[0];
      if (b != 3) {
        if (b == 4) {
          this.stage = AuthStage.FULL_AUTH;
        } else {
          throw SQLError.createSQLException("Unknown server response after fast auth.", "08001", this.connection.getExceptionInterceptor());
        } 
      } else {
        this.stage = AuthStage.FAST_AUTH_COMPLETE;
        return true;
      } 
    } 
    if (((MySQLConnection)this.connection).getIO().isSSLEstablished()) {
      try {
        Buffer buffer = new Buffer(StringUtils.getBytes(this.password, this.connection.getPasswordCharacterEncoding()));
        buffer.setPosition(buffer.getBufLength());
        int i = buffer.getBufLength();
        buffer.writeByte((byte)0);
        buffer.setBufLength(i + 1);
        buffer.setPosition(0);
        paramList.add(buffer);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.3", new Object[] { this.connection.getPasswordCharacterEncoding() }), "S1000", null);
      } 
    } else if (this.connection.getServerRSAPublicKeyFile() != null) {
      paramList.add(new Buffer(encryptPassword()));
    } else if (this.connection.getAllowPublicKeyRetrieval()) {
      if (this.publicKeyRequested && unsupportedEncodingException.getBufLength() > 20) {
        this.publicKeyString = unsupportedEncodingException.readString();
        paramList.add(new Buffer(encryptPassword()));
        this.publicKeyRequested = false;
      } else {
        paramList.add(new Buffer(new byte[] { 2 }));
        this.publicKeyRequested = true;
      } 
    } else {
      throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.2"), "08001", this.connection.getExceptionInterceptor());
    } 
    return true;
  }
  
  public void reset() {
    this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
  }
  
  public enum AuthStage {
    FAST_AUTH_COMPLETE, FAST_AUTH_READ_RESULT, FAST_AUTH_SEND_SCRAMBLE, FULL_AUTH;
    
    private static final AuthStage[] $VALUES;
    
    static {
      AuthStage authStage2 = new AuthStage("FAST_AUTH_SEND_SCRAMBLE", 0);
      FAST_AUTH_SEND_SCRAMBLE = authStage2;
      AuthStage authStage4 = new AuthStage("FAST_AUTH_READ_RESULT", 1);
      FAST_AUTH_READ_RESULT = authStage4;
      AuthStage authStage3 = new AuthStage("FAST_AUTH_COMPLETE", 2);
      FAST_AUTH_COMPLETE = authStage3;
      AuthStage authStage1 = new AuthStage("FULL_AUTH", 3);
      FULL_AUTH = authStage1;
      $VALUES = new AuthStage[] { authStage2, authStage4, authStage3, authStage1 };
    }
  }
}
