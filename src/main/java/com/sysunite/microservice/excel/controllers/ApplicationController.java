package com.sysunite.microservice.excel.controllers;

import com.sysunite.microservice.excel.util.Props;
import com.sysunite.microservice.excel.util.ResourceLoader;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Mohamad Alamili
 */
public class ApplicationController {

  public static Route index = (Request req, Response res) ->
    "Excel Microservice v" + Props.get("application.version");

  public static Route version = (Request req, Response res) ->
    Props.get("application.version");

  public static Route notfound = (Request req, Response res) ->
    "not found";

  public static Route swagger = (Request req, Response res) ->
    new ResourceLoader().getFile("public/swagger.yml");
}
