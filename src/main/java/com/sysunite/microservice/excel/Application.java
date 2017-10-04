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
    get("/",                      ApplicationController.index);
    get("/application/version",   ApplicationController.version);
    post("/template/add",         TemplateController.add);
    get("/template/get",          TemplateController.get);
    post("/excel/create",         ExcelController.create);
    post("/excel/inject",         ExcelController.inject);
    get("*",                      ApplicationController.notfound);


    // Catch exceptions
    exception(Exception.class, (e, request, response) -> {
      logger.error(e.getLocalizedMessage());
      response.status(503);
      response.body("Server Error");
    });


    // Wait for server to be initialized
    awaitInitialization();

    // Catch exceptions
    exception(Exception.class, (e, request, response) -> {
      e.printStackTrace();
      response.status(503);
      response.body("Server Error");
    });

    // Running
    final String VERSION = Props.getInstance().getProperty("application.version");
    logger.info("Excel Microservice version " + VERSION  + " running on port " + PORT);
  }

  public static void main(String[] args) throws IOException {
    new Application();
  }
}
