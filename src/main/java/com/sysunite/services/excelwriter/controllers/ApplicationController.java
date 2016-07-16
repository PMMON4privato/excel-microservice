package com.sysunite.services.excelwriter.controllers;

import com.sysunite.services.excelwriter.util.Props;

import static spark.Spark.get;

/**
 * @author Mohamad Alamili
 */
public class ApplicationController {
  public ApplicationController() {
  }
  
  public void wire(){
    
    final String VERSION = Props.getInstance().getProperty("application.version");
    
    // Index
    get("/", (req, res) -> "Excel Writer v" +VERSION);
    
    // Application version
    get("/application/version", (req, res) -> VERSION);
  }
}