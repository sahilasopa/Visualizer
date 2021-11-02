package com.example.visualizer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XlsxToCSV {

    public String xlsx(File inputFile, File outputFile) {
        StringBuilder data = new StringBuilder();

        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            FileInputStream fis = new FileInputStream(inputFile);
            Workbook workbook;
            workbook = new XSSFWorkbook(fis);

            int rowStart = Math.min(0, workbook.getSheetAt(0).getFirstRowNum());
            int rowEnd = Math.max(0, workbook.getSheetAt(0).getLastRowNum());
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = workbook.getSheetAt(0).getRow(rowNum);
                if (r == null) {
                    continue;
                }
                int lastColumn = Math.max(r.getLastCellNum(), 0);
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell c = r.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (c == null) {
                        data.append("0" + ",");
                    } else {
                        switch (c.getCellType()) {
                            case BOOLEAN -> data.append(c.getBooleanCellValue()).append(",");
                            case NUMERIC -> data.append(c.getNumericCellValue()).append(",");
                            case STRING -> data.append(c.getStringCellValue()).append(",");
                            case BLANK -> data.append("0" + ",");
                            case FORMULA -> {
                                try {
                                    data.append(c.getStringCellValue()).append(",");
                                } catch (IllegalStateException e) {
                                    data.append(c.getNumericCellValue()).append(",");
                                }
                            }
                            default -> data.append(c).append(",");
                        }
                    }
                }
                data.append('\n');
            }
            fos.write(data.toString().getBytes());
            fos.close();
            return outputFile.getPath();

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}