package com.sysunite.microservice.excel.controllers;

import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;

/**
 * @author Mohamad Alamili
 */
public class ExcelController {
  
  public static Route inject = (Request req, Response res) -> {
      
      // Get params
      req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
      
      System.out.println(req.queryParams("data"));
      System.out.println(req.queryParams("templateId"));
      
      // Here is where the magic happens
      // JSON object of CELL to Excel
      
      // Should download the file
      res.type("text/xml");
      return "OK";
  };
}