package com.sysunite.microservice.excel.model;

/**
 * @author Mohamad Alamili
 */
public class ExcelCell {
  private Object value;
  private String type;

  public ExcelCell() {
  }

  public ExcelCell(Object value, String type) {
    this.value = value;
    this.type = type;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
