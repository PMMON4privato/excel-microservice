package com.sysunite.microservice.excel.model;

import java.util.LinkedHashMap;

/**
 * @author Mohamad Alamili
 */
public class ExcelRow extends LinkedHashMap<Integer, ExcelCell> {

  public void putIfDefined(int key, ExcelCell cell) {
    if(cell != null){
      put(key, cell);
    }
  }
}
