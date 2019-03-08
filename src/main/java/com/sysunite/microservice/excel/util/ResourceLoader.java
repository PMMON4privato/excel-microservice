package com.sysunite.microservice.excel.util;

import java.io.InputStream;

public class ResourceLoader {

  public InputStream getFile(String fileName) {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream file = classLoader.getResourceAsStream(fileName);
    return file;
  }
}
