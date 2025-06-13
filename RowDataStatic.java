package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;

public class RowDataStatic implements RowData {
  private int index = -1;
  
  private Field[] metadata;
  
  public ResultSetImpl owner;
  
  private List<ResultSetRow> rows;
  
  public RowDataStatic(List<ResultSetRow> paramList) {
    this.rows = paramList;
  }
  
  public void addRow(ResultSetRow paramResultSetRow) {
    this.rows.add(paramResultSetRow);
  }
  
  public void afterLast() {
    if (this.rows.size() > 0)
      this.index = this.rows.size(); 
  }
  
  public void beforeFirst() {
    if (this.rows.size() > 0)
      this.index = -1; 
  }
  
  public void beforeLast() {
    if (this.rows.size() > 0)
      this.index = this.rows.size() - 2; 
  }
  
  public void close() {}
  
  public ResultSetRow getAt(int paramInt) throws SQLException {
    return (paramInt < 0 || paramInt >= this.rows.size()) ? null : ((ResultSetRow)this.rows.get(paramInt)).setMetadata(this.metadata);
  }
  
  public int getCurrentRowNumber() {
    return this.index;
  }
  
  public ResultSetInternalMethods getOwner() {
    return this.owner;
  }
  
  public boolean hasNext() {
    int i = this.index;
    boolean bool = true;
    if (i + 1 >= this.rows.size())
      bool = false; 
    return bool;
  }
  
  public boolean isAfterLast() {
    boolean bool;
    if (this.index >= this.rows.size() && this.rows.size() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isBeforeFirst() {
    boolean bool;
    if (this.index == -1 && this.rows.size() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDynamic() {
    return false;
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.rows.size() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isFirst() {
    boolean bool;
    if (this.index == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isLast() {
    int i = this.rows.size();
    boolean bool = false;
    if (i == 0)
      return false; 
    if (this.index == this.rows.size() - 1)
      bool = true; 
    return bool;
  }
  
  public void moveRowRelative(int paramInt) {
    if (this.rows.size() > 0) {
      paramInt = this.index + paramInt;
      this.index = paramInt;
      if (paramInt < -1) {
        beforeFirst();
      } else if (paramInt > this.rows.size()) {
        afterLast();
      } 
    } 
  }
  
  public ResultSetRow next() throws SQLException {
    int i = this.index + 1;
    this.index = i;
    if (i > this.rows.size()) {
      afterLast();
    } else if (this.index < this.rows.size()) {
      return ((ResultSetRow)this.rows.get(this.index)).setMetadata(this.metadata);
    } 
    return null;
  }
  
  public void removeRow(int paramInt) {
    this.rows.remove(paramInt);
  }
  
  public void setCurrentRow(int paramInt) {
    this.index = paramInt;
  }
  
  public void setMetadata(Field[] paramArrayOfField) {
    this.metadata = paramArrayOfField;
  }
  
  public void setOwner(ResultSetImpl paramResultSetImpl) {
    this.owner = paramResultSetImpl;
  }
  
  public int size() {
    return this.rows.size();
  }
  
  public boolean wasEmpty() {
    boolean bool;
    List<ResultSetRow> list = this.rows;
    if (list != null && list.size() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}
