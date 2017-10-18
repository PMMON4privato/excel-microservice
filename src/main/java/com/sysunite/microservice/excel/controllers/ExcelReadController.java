package com.sysunite.microservice.excel.controllers;

import com.sysunite.microservice.excel.model.ExcelCell;
import com.sysunite.microservice.excel.model.ExcelContent;
import com.sysunite.microservice.excel.model.ExcelRow;
import com.sysunite.microservice.excel.model.ExcelSheet;
import com.sysunite.microservice.excel.util.UploadedFile;
import org.apache.poi.ss.usermodel.*;
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
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author Mohamad Alamili
 */
public class ExcelReadController {

  /**
   * Read Excel file and convert to JSON object
   */
  public static Route read = (Request req, Response res) -> {
    // Get stream of uploaded file
    InputStream stream = UploadedFile.get(req).getInputStream();

    // Construct Apache POI workbook
    Workbook workbook = new XSSFWorkbook(stream);
    workbook.setForceFormulaRecalculation(true);

    // Read workbook and store content in here
    ExcelContent content = new ExcelContent();

    // For each sheet
    for(int i=0; i < workbook.getNumberOfSheets(); i++){
      Sheet sheet = workbook.getSheetAt(i);

      // For each row
      Iterator<Row> rowIterator = sheet.rowIterator();
      while(rowIterator.hasNext()){
        Row row = rowIterator.next();

        // For each cell
        Iterator<Cell> cellIterator = row.cellIterator();
        while(cellIterator.hasNext()){
          Cell cell = cellIterator.next();

          // Save cell to content

          content
            .getOrInit(sheet.getSheetName())
            .getOrInit(row.getRowNum())
            .putIfDefined(cell.getAddress().getColumn(), getCell(cell, cell.getCellTypeEnum()));
        }
      }
    }

    stream.close();
    content.cleanEmpty();
    return content.toJson();
  };

  private static ExcelCell getCell(Cell cell, CellType cellType) {

    switch (cellType) {
      case _NONE:
        return null;
      case NUMERIC:
        return new ExcelCell(cell.getNumericCellValue(), "number");
      case STRING:
        return new ExcelCell(cell.getStringCellValue(), "string");
      case FORMULA:
        return getCell(cell, cell.getCachedFormulaResultTypeEnum());
      case BLANK:
        return null;
      case BOOLEAN:
        return new ExcelCell(cell.getBooleanCellValue(), "boolean");
      case ERROR:
        return new ExcelCell(cell.getErrorCellValue(), "error");
      default:
        return null;
    }
  }
}
