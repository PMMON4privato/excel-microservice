package com.sysunite.microservice.excel.enum_type;

public enum CellDataTypeEnum {

  NUMBER("number"),
  STRING("string"),
  BOOLEAN("boolean"),
  DATETIME("datetime"),
  ERROR("error");

  private String dataType;

  CellDataTypeEnum(String dataType) {
    this.dataType = dataType;
  }

  public String dataType() {
    return this.dataType;
  }
}
