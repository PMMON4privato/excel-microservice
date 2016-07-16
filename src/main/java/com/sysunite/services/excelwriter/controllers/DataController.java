package com.sysunite.services.excelwriter.controllers;

import javax.servlet.MultipartConfigElement;

import static spark.Spark.post;

/**
 * @author Mohamad Alamili
 */
public class DataController {
  public DataController() {
  }
  
  public void wire(){
    excelInject();
  }

  private void excelInject() {
    post("/data/excel/inject", (req, res) -> {
      
      // Get params
      req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
      
      System.out.println(req.queryParams("data"));
      System.out.println(req.queryParams("templateId"));
      
      // Should download the file
      res.type("text/xml");
      return "OK";
    });
  }
}