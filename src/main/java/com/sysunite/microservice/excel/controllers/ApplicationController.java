package com.sysunite.microservice.excel.controllers;

import com.sysunite.microservice.excel.util.Props;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Mohamad Alamili
 */
public class ApplicationController {
  
  public static Route index = (Request req, Response res) -> 
    "Excel Microservice v" + Props.getInstance().getProperty("application.version");  
    
  public static Route version = (Request req, Response res) -> 
    Props.getInstance().getProperty("application.version");

  public static Route notfound = (Request req, Response res) ->
    "not found";
}
