package com.sysunite.microservice.excel.controllers;

import com.sysunite.microservice.excel.enum_type.CellDataTypeEnum;
import com.sysunite.microservice.excel.model.ExcelCell;
import com.sysunite.microservice.excel.model.ExcelContent;
import com.sysunite.microservice.excel.util.UploadedFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import spark.Request;
import spark.Response;
import spark.Route;

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
        if(DateUtil.isCellDateFormatted(cell)) {
          return new ExcelCell(cell.getDateCellValue().toInstant().toString(), CellDataTypeEnum.DATETIME.dataType());
        } else {
          return new ExcelCell(cell.getNumericCellValue(), CellDataTypeEnum.NUMBER.dataType());
        }
      case STRING:
        return new ExcelCell(cell.getStringCellValue(), CellDataTypeEnum.STRING.dataType());
      case FORMULA:
        return getCell(cell, cell.getCachedFormulaResultTypeEnum());
      case BLANK:
        return null;
      case BOOLEAN:
        return new ExcelCell(cell.getBooleanCellValue(), CellDataTypeEnum.BOOLEAN.dataType());
      case ERROR:
        return new ExcelCell(cell.getErrorCellValue(), CellDataTypeEnum.ERROR.dataType());
      default:
        return null;
    }
  }
}
