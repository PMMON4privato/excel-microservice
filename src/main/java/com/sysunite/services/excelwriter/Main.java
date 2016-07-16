package com.sysunite.services.excelwriter; 

import com.sysunite.services.excelwriter.controllers.ApplicationController;
import com.sysunite.services.excelwriter.controllers.DataController;
import com.sysunite.services.excelwriter.controllers.TemplateController;
import com.sysunite.services.excelwriter.util.CORS;
import com.sysunite.services.excelwriter.util.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static spark.Spark.awaitInitialization;
import static spark.Spark.port;

/**
 * @author Mohamad Alamili
 */
public class Main {

  static Logger logger = LoggerFactory.getLogger(Main.class);
  
  public static void main(String[] args) throws IOException {
    
    // HTTP port to listen on
    final int PORT = 9267;
    port(PORT);
    
    // Enable CORS on all hosts
    CORS.enable();
    
    // Wire controllers
    new ApplicationController().wire();
    new TemplateController().wire();
    new DataController().wire();

    // Wait for server to be initialized
    awaitInitialization(); 

    // Running
    final String VERSION = Props.getInstance().getProperty("application.version");
    logger.info("Excel Writer version " + VERSION  + " running on port " + PORT);
  }
}