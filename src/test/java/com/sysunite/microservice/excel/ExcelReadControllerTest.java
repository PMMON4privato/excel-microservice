package com.sysunite.microservice.excel;

import com.google.common.io.Resources;
import com.sysunite.microservice.excel.model.ExcelCell;
import com.sysunite.microservice.excel.model.ExcelContent;
import com.sysunite.microservice.excel.model.ExcelRow;
import com.sysunite.microservice.excel.model.ExcelSheet;
import io.restassured.http.ContentType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

/**
 * @author Mohamad Alamili
 */
public class ExcelReadControllerTest extends BaseTest {


  @Test
  public void read() throws IOException {
    File file = new File(Resources.getResource("test-read.xlsx").getPath());

    String data =
      given()
      .multiPart(file)
      .expect()
      .statusCode(200)
      .when()
      .post("/excel/read")
      .then()
      .extract()
      .response()
      .asString();

    ExcelContent content = ExcelContent.fromJson(data);

    assertEquals("Header 1",  content.get(0).get(0).get(0).getValue());
    assertEquals("Header 2",  content.get(0).get(0).get(1).getValue());
    assertEquals("Header 3",  content.get(0).get(0).get(2).getValue());
    assertEquals("Header E",  content.get(0).get(0).get(4).getValue());
    assertEquals("Cell A2",   content.get(0).get(1).get(0).getValue());
    assertEquals("Cell B2",   content.get(0).get(1).get(1).getValue());
    assertEquals("Cell C4",   content.get(0).get(3).get(2).getValue());
    assertEquals("Cell D4",   content.get(0).get(3).get(3).getValue());
    assertEquals("Block A8",  content.get(0).get(7).get(0).getValue());
    assertEquals("Val B8",    content.get(0).get(7).get(1).getValue());
    assertEquals("Lonely D9", content.get(1).get(8).get(3).getValue());
  }
}
