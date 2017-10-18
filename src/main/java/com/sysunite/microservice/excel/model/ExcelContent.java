package com.sysunite.microservice.excel.model;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

/**
 * @author Mohamad Alamili
 */
public class ExcelContent extends LinkedHashMap<String, ExcelSheet> {

  public void cleanNullCells(){
    for(String sheet: keySet()){
      for(Integer rowIndex: get(sheet).keySet()){
        Object[] cells = get(sheet).get(rowIndex).keySet().toArray();

        for (int i=0; i < cells.length; i++) {
          ExcelCell cell = get(sheet).get(rowIndex).get(cells[i]);
          if (cell.getValue() == null){
            get(sheet).get(rowIndex).remove(cells[i]);
          }
        }
      }
    }
  }

  public void cleanEmpty(){
    Object[] sheets = keySet().toArray();

    for (int i=0; i < sheets.length; i++){
      if(get(sheets[i]).isEmpty()){
        remove(sheets[i]);
      }
      else {
        get(sheets[i]).cleanEmpty();
      }
    }
  }

  public String toJson(){
    return new Gson().toJson(this);
  }

  public ExcelSheet getOrInit(String name){
    if (!containsKey(name)){
      put(name, new ExcelSheet());
    }

    return get(name);
  }

  public static ExcelContent fromJson(String data) {
    return new Gson().fromJson(data, ExcelContent.class);
  }
}
