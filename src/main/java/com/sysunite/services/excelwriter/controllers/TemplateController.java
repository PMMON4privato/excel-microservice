package com.sysunite.services.excelwriter.controllers;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * @author Mohamad Alamili
 */
public class TemplateController {
  public TemplateController() {
  }
  
  public void wire(){
    uploadTemplate();
    downloadTemplate();
  }
  
  private void uploadTemplate() {
    
    // Create the templates directory if it doesn't exist
    File templatesDir = new File("templates");
    templatesDir.mkdir();
    
    // Generate templateId for file name
    String templateId = UUID.randomUUID().toString();

    post("/template/upload", (req, res) -> {

      File newFile = new File(templatesDir, templateId);
      newFile.createNewFile();

      req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
      
      // getPart needs to use same "name" as input field in form
      try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { 
        Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      return templateId;
    });
  }

  private void downloadTemplate() {
    get("/template/download", (req, res) -> {
      String templateId = req.queryParams("templateId");
      String fileName   = req.queryParams("fileName");

      res.raw().setContentType("application/octet-stream");
      res.raw().setHeader("Content-Disposition","attachment; filename=" + fileName);

      byte[] bytes = Files.readAllBytes(Paths.get("templates", templateId));
      HttpServletResponse raw = res.raw();

      raw.getOutputStream().write(bytes);
      raw.getOutputStream().flush();
      raw.getOutputStream().close();

      return res.raw();
    });
  }
}
