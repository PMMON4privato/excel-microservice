package com.sysunite.microservice.excel.controllers;

import com.sysunite.microservice.excel.model.ExcelCell;
import com.sysunite.microservice.excel.model.ExcelContent;
import com.sysunite.microservice.excel.model.ExcelRow;
import com.sysunite.microservice.excel.model.ExcelSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Mohamad Alamili
 */
public class ExcelWriteController {

  static Logger logger = LoggerFactory.getLogger(ExcelWriteController.class);

  public static Route create = (Request req, Response res) -> {
    // Get params
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));

    String data = req.body();
    String fileName = req.queryParams("fileName");

    File baseFile = new File("base.xlsx");

    return process(res, data, fileName, baseFile, true);
  };


  public static Route inject = (Request req, Response res) -> {
    // Get params
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));

    String data       = req.body();
    String fileName   = req.queryParams("fileName");
    String templateId = req.queryParams("templateId");

    File templateFile = new File("templates", templateId);

    return process(res, data, fileName, templateFile, false);
  };


  private static HttpServletResponse process(Response res, String data, String fileName, File templateFile, boolean isBaseFile) throws IOException {
    ExcelContent excelContent = ExcelContent.fromJson(data);
    excelContent.cleanNullCells();
    excelContent.cleanEmpty();

    FileInputStream templateFileStream = new FileInputStream(templateFile);

    Workbook workbook = new XSSFWorkbook(templateFileStream);
    if (isBaseFile) {
      workbook.removeSheetAt(0); // Remove default sheet
    }

    workbook.setForceFormulaRecalculation(true);


    // Parse values
    for(String sheetName: excelContent.keySet()){
      ExcelSheet excelSheet = excelContent.get(sheetName);

      // Create sheets
      if (isBaseFile) {
        workbook.createSheet(sheetName);
      }
      
      for(Integer rowIndex : excelSheet.keySet()){
        ExcelRow excelRow = excelSheet.get(rowIndex);

        for(Integer cellIndex : excelRow.keySet()){
          ExcelCell excelCell = excelRow.get(cellIndex);

          // Create row
          Sheet sheet = workbook.getSheet(sheetName);
          if(sheet.getRow(rowIndex) == null){
            sheet.createRow(rowIndex);
          }

          // Create cell
          Row row = sheet.getRow(rowIndex);
          if(row.getCell(cellIndex) == null){
            row.createCell(cellIndex);
          }

          // Set value
          Cell cell = row.getCell(cellIndex);

          if (excelCell.getType().equalsIgnoreCase("string"))
            cell.setCellValue(excelCell.getValue().toString());
          else if(excelCell.getType().equalsIgnoreCase("number"))
            cell.setCellValue(Double.valueOf(excelCell.getValue().toString()));
        }
      }
    }

    templateFileStream.close();

    res.raw().setContentType("application/octet-stream");
    res.raw().setHeader("Content-Disposition", "attachment; filename=" + fileName);

    HttpServletResponse raw = res.raw();

    workbook.write(raw.getOutputStream());

    raw.getOutputStream().flush();
    raw.getOutputStream().close();

    return res.raw();
  }
}
