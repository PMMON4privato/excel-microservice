package com.sysunite.microservice.excel.model;

import java.util.LinkedHashMap;

/**
 * @author Mohamad Alamili
 */
public class ExcelSheet extends LinkedHashMap<Integer, ExcelRow> {

  public ExcelRow getOrInit(int key){
    if (!containsKey(key)){
      put(key, new ExcelRow());
    }

    return get(key);
  }

  public void cleanEmpty() {
   Object[] rows = keySet().toArray();

   for (int i=0; i < rows.length; i++){
      if(get(rows[i]).isEmpty()){
        remove(rows[i]);
      }
    }
  }
}
