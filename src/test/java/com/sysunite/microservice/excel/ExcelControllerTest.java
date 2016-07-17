package com.sysunite.microservice.excel;

import com.google.common.io.Resources;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

/**
 * @author Mohamad Alamili
 */

// The order is important because test1 will upload a template that test2 will check for downloading
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExcelControllerTest extends BaseTest {
  

  @Test
  public void test2_create() throws IOException {
    String data = "{}";
    String fileName = "hello";
    
    byte[] excelContent = 
    given().
      queryParam("data", data).
      queryParam("fileName", fileName).
    when().
      post("/excel/create").
    then().
      assertThat().
      contentType("application/octet-stream").
      header("Content-Disposition", "attachment; filename=" + fileName).
      statusCode(200).
    extract().
      response().asByteArray();
    
    String digest = DigestUtils.md5Hex(excelContent);
    String actualDigest = DigestUtils.md5Hex(Resources.getResource("base.xlsx").openStream());

    // Check content
    assertEquals(actualDigest, digest);
  }
}