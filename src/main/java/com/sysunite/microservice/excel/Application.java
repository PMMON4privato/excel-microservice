package com.sysunite.microservice.excel; 

import com.sysunite.microservice.excel.controllers.ApplicationController;
import com.sysunite.microservice.excel.controllers.ExcelController;
import com.sysunite.microservice.excel.controllers.TemplateController;
import com.sysunite.microservice.excel.util.CORS;
import com.sysunite.microservice.excel.util.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static spark.Spark.*;

/**
 * @author Mohamad Alamili
 */
public class Application {

  static Logger logger = LoggerFactory.getLogger(Application.class);

  public Application() {
    // HTTP port to listen on
    final int PORT = 9267;
    port(PORT);

    // Enable CORS on all hosts
    CORS.enable();

    // Wire routes
    get("/",                     ApplicationController.index);
    get("/application/version",  ApplicationController.version);
    get("/template/add",         TemplateController.add);
    get("/template/get",         TemplateController.get);
    get("/excel/inject",         ExcelController.inject);
    get("*",                     ApplicationController.notfound);

    // Wait for server to be initialized
    awaitInitialization();

    // Running
    final String VERSION = Props.getInstance().getProperty("application.version");
    logger.info("Excel Microservice version " + VERSION  + " running on port " + PORT);
  }
  
  public void stop() {
    stop();
  }

  public static void main(String[] args) throws IOException {
    new Application();
  }
}