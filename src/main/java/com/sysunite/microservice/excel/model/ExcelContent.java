package com.sysunite.microservice.excel.model;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * @author Mohamad Alamili
 */
public class ExcelContent extends HashMap<Integer, ExcelSheet> {

  public String toJson(){
    return new Gson().toJson(this);
  }

  public static ExcelContent fromJson(String data) {
    return new Gson().fromJson(data, ExcelContent.class);
  }
}