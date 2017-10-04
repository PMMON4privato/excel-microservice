package com.sysunite.microservice.excel.model;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

/**
 * @author Mohamad Alamili
 */
public class ExcelContent extends LinkedHashMap<Integer, ExcelSheet> {

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

  public ExcelSheet getOrInit(int key){
    if (!containsKey(key)){
      put(key, new ExcelSheet());
    }

    return get(key);
  }

  public static ExcelContent fromJson(String data) {
    return new Gson().fromJson(data, ExcelContent.class);
  }
}
