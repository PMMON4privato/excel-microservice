package com.sysunite.microservice.excel;

import com.sysunite.microservice.excel.util.Props;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohamad Alamili
 */
public class ApplicationControllerTest extends BaseTest {

  @Test
  public void get_version() {
    final String VERSION = Props.get("application.version");
    get("/application/version").then().body(is(VERSION));
  }
}
