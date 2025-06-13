package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowDataCursor implements RowData {
  private static final int BEFORE_START_OF_ROWS = -1;
  
  private static final int SERVER_STATUS_LAST_ROW_SENT = 128;
  
  private int currentPositionInEntireResult = -1;
  
  private int currentPositionInFetchedRows = -1;
  
  private List<ResultSetRow> fetchedRows;
  
  private boolean firstFetchCompleted = false;
  
  private boolean lastRowFetched = false;
  
  private Field[] metadata;
  
  private MysqlIO mysql;
  
  private ResultSetImpl owner;
  
  private ServerPreparedStatement prepStmt;
  
  private long statementIdOnServer;
  
  private boolean useBufferRowExplicit = false;
  
  private boolean wasEmpty = false;
  
  public RowDataCursor(MysqlIO paramMysqlIO, ServerPreparedStatement paramServerPreparedStatement, Field[] paramArrayOfField) {
    this.currentPositionInEntireResult = -1;
    this.metadata = paramArrayOfField;
    this.mysql = paramMysqlIO;
    this.statementIdOnServer = paramServerPreparedStatement.getServerStatementId();
    this.prepStmt = paramServerPreparedStatement;
    this.useBufferRowExplicit = MysqlIO.useBufferRowExplicit(this.metadata);
  }
  
  private void fetchMoreRows() throws SQLException {
    if (this.lastRowFetched) {
      this.fetchedRows = new ArrayList<ResultSetRow>(0);
      return;
    } 
    synchronized (this.owner.connection.getConnectionMutex()) {
      boolean bool = this.firstFetchCompleted;
      if (!bool)
        this.firstFetchCompleted = true; 
      int j = this.owner.getFetchSize();
      int i = j;
      if (j == 0)
        i = this.prepStmt.getFetchSize(); 
      if (i == Integer.MIN_VALUE)
        i = 1; 
      this.fetchedRows = this.mysql.fetchRowsViaCursor(this.fetchedRows, this.statementIdOnServer, this.metadata, i, this.useBufferRowExplicit);
      this.currentPositionInFetchedRows = -1;
      if ((this.mysql.getServerStatus() & 0x80) != 0) {
        this.lastRowFetched = true;
        if (!bool && this.fetchedRows.size() == 0)
          this.wasEmpty = true; 
      } 
      return;
    } 
  }
  
  private void notSupported() throws SQLException {
    throw new OperationNotSupportedException();
  }
  
  public void addRow(ResultSetRow paramResultSetRow) throws SQLException {
    notSupported();
  }
  
  public void afterLast() throws SQLException {
    notSupported();
  }
  
  public void beforeFirst() throws SQLException {
    notSupported();
  }
  
  public void beforeLast() throws SQLException {
    notSupported();
  }
  
  public void close() throws SQLException {
    this.metadata = null;
    this.owner = null;
  }
  
  public ResultSetRow getAt(int paramInt) throws SQLException {
    notSupported();
    return null;
  }
  
  public int getCurrentRowNumber() throws SQLException {
    return this.currentPositionInEntireResult + 1;
  }
  
  public ResultSetInternalMethods getOwner() {
    return this.owner;
  }
  
  public boolean hasNext() throws SQLException {
    List<ResultSetRow> list = this.fetchedRows;
    boolean bool1 = false;
    boolean bool2 = false;
    if (list != null && list.size() == 0)
      return false; 
    ResultSetImpl resultSetImpl = this.owner;
    if (resultSetImpl != null) {
      StatementImpl statementImpl = resultSetImpl.owningStatement;
      if (statementImpl != null) {
        int i = statementImpl.maxRows;
        if (i != -1 && this.currentPositionInEntireResult + 1 > i)
          return false; 
      } 
    } 
    if (this.currentPositionInEntireResult != -1) {
      if (this.currentPositionInFetchedRows < this.fetchedRows.size() - 1)
        return true; 
      if (this.currentPositionInFetchedRows == this.fetchedRows.size() && this.lastRowFetched)
        return false; 
      fetchMoreRows();
      bool1 = bool2;
      if (this.fetchedRows.size() > 0)
        bool1 = true; 
      return bool1;
    } 
    fetchMoreRows();
    if (this.fetchedRows.size() > 0)
      bool1 = true; 
    return bool1;
  }
  
  public boolean isAfterLast() {
    boolean bool;
    if (this.lastRowFetched && this.currentPositionInFetchedRows > this.fetchedRows.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isBeforeFirst() throws SQLException {
    boolean bool;
    if (this.currentPositionInEntireResult < 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDynamic() {
    return true;
  }
  
  public boolean isEmpty() throws SQLException {
    boolean bool;
    if (isBeforeFirst() && isAfterLast()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isFirst() throws SQLException {
    boolean bool;
    if (this.currentPositionInEntireResult == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isLast() throws SQLException {
    boolean bool1 = this.lastRowFetched;
    boolean bool = true;
    if (!bool1 || this.currentPositionInFetchedRows != this.fetchedRows.size() - 1)
      bool = false; 
    return bool;
  }
  
  public void moveRowRelative(int paramInt) throws SQLException {
    notSupported();
  }
  
  public ResultSetRow next() throws SQLException {
    if (this.fetchedRows != null || this.currentPositionInEntireResult == -1) {
      if (!hasNext())
        return null; 
      this.currentPositionInEntireResult++;
      this.currentPositionInFetchedRows++;
      List<ResultSetRow> list = this.fetchedRows;
      if (list != null && list.size() == 0)
        return null; 
      list = this.fetchedRows;
      if (list == null || this.currentPositionInFetchedRows > list.size() - 1) {
        fetchMoreRows();
        this.currentPositionInFetchedRows = 0;
      } 
      ResultSetRow resultSetRow = this.fetchedRows.get(this.currentPositionInFetchedRows);
      resultSetRow.setMetadata(this.metadata);
      return resultSetRow;
    } 
    throw SQLError.createSQLException(Messages.getString("ResultSet.Operation_not_allowed_after_ResultSet_closed_144"), "S1000", this.mysql.getExceptionInterceptor());
  }
  
  public void nextRecord() throws SQLException {}
  
  public void removeRow(int paramInt) throws SQLException {
    notSupported();
  }
  
  public void setCurrentRow(int paramInt) throws SQLException {
    notSupported();
  }
  
  public void setMetadata(Field[] paramArrayOfField) {
    this.metadata = paramArrayOfField;
  }
  
  public void setOwner(ResultSetImpl paramResultSetImpl) {
    this.owner = paramResultSetImpl;
  }
  
  public int size() {
    return -1;
  }
  
  public boolean wasEmpty() {
    return this.wasEmpty;
  }
}
