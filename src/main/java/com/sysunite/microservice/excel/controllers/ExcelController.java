package com.sysunite.microservice.excel.controllers;

import com.google.common.io.Resources;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author Mohamad Alamili
 */
public class ExcelController {
  
  
  public static Route create = (Request req, Response res) -> {

    try {
      // Get params
      req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));

      String data = req.queryParams("data");
      String fileName = req.queryParams("fileName");

      res.raw().setContentType("application/octet-stream");
      res.raw().setHeader("Content-Disposition", "attachment; filename=" + fileName);

      InputStream stream = Resources.getResource("base.xlsx").openStream();
      HttpServletResponse raw = res.raw();

      int nRead;
      byte[] byteSize = new byte[1024];
      while ((nRead = stream.read(byteSize, 0, byteSize.length)) != -1) {
        raw.getOutputStream().write(byteSize, 0, nRead);
      }

      raw.getOutputStream().flush();
      raw.getOutputStream().close();

      return res.raw();
    }
    catch(Exception e ){
      e.printStackTrace();
      return null;

    }
  };
  
  
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