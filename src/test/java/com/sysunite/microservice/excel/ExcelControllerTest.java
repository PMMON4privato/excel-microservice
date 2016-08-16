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
public class ExcelControllerTest extends BaseTest {
  
  @Test
  public void create() throws IOException, NoSuchAlgorithmException {
    ExcelContent data = new ExcelContent();
    data.put(0, new ExcelSheet());
    data.get(0).put(1, new ExcelRow());
    data.get(0).get(1).put(2, new ExcelCell("Hello World", "string"));
    
    String fileName = "hello.xlsx";
    
    InputStream excelContent = 
    given().
      contentType(ContentType.JSON).
      queryParam("data", data.toJson()).
      queryParam("fileName", fileName).
    when().
      post("/excel/create").
    then().
      assertThat().
      contentType("application/octet-stream").
      header("Content-Disposition", "attachment; filename=" + fileName).
      statusCode(200).
    extract().
      response().asInputStream();
    
    // Check content
    List<String> receivedContent = getHashesFromZipFile(writeTempFile(excelContent));
    List<String> shouldBeContent = getHashesFromZipFile(new File(Resources.getResource("test-create-1.xlsx").getPath()));
    
    assertEquals(shouldBeContent, receivedContent);
  }


  @Test
  public void inject() throws IOException, NoSuchAlgorithmException {
    // First upload template
    File template = new File(Resources.getResource("test-inject-1-template.xlsx").getPath());

    String templateId =
      given().
        multiPart("uploaded_file", template).
        when().
        post("/template/add").
        then().
        assertThat().
        statusCode(200).
        extract().
        response().
        asString();
    
    ExcelContent data = new ExcelContent();
    data.put(0, new ExcelSheet());
    data.get(0).put(1, new ExcelRow());
    data.get(0).get(1).put(2, new ExcelCell("Hello World", "string"));

    String fileName = "hello.xlsx";

    InputStream excelContent =
      given().
        contentType(ContentType.JSON).
        body(data.toJson()).
        queryParam("fileName", fileName).
        queryParam("templateId", templateId).
        when().
        post("/excel/inject").
        then().
        assertThat().
        contentType("application/octet-stream").
        header("Content-Disposition", "attachment; filename=" + fileName).
        statusCode(200).
        extract().
        response().asInputStream();

    // Check content
    List<String> receivedContent = getHashesFromZipFile(writeTempFile(excelContent));
    List<String> shouldBeContent = getHashesFromZipFile(new File(Resources.getResource("test-inject-1.xlsx").getPath()));

    assertEquals(shouldBeContent, receivedContent);
  }
  
  
  public File writeTempFile(InputStream stream) throws IOException {
    File tempFile = File.createTempFile("test_temp_","");
    FileUtils.writeByteArrayToFile(tempFile, IOUtils.toByteArray(stream));
    return tempFile;
  }
  
  public List<String> getHashesFromZipFile(File file) throws IOException {
    List<String> hashes = new ArrayList<>();
    ZipFile zipFile = new ZipFile(file);

    byte[] buf = new byte[65536];
    Enumeration<?> entries = zipFile.getEntries();
    while (entries.hasMoreElements()) {
      ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) entries.nextElement();
      int n;
      InputStream is = zipFile.getInputStream(zipArchiveEntry);
      ZipArchiveInputStream zis = new ZipArchiveInputStream(is);

      if (zis.canReadEntryData(zipArchiveEntry)) {
        while ((n = is.read(buf)) != -1) {
          if (n > 0) {
            hashes.add(DigestUtils.md5Hex(buf));
          }
        }
      }
      zis.close();
    }
    
    return hashes;
  }
}