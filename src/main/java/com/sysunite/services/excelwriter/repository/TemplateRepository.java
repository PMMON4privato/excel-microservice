package com.sysunite.services.excelwriter.repository;

/**
 * @author Mohamad Alamili
 */
public class TemplateRepository {
  
  private static TemplateRepository instance;
  
  public static TemplateRepository getInstance() {
    if(instance == null) {
      instance = new TemplateRepository();
    }
    
    return instance;
  }

  protected TemplateRepository() {
  }
}