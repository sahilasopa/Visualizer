package com.example.visualizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

public class XlsxtoCSV {

    public String xlsx(File inputFile, File outputFile) {
        // For storing data into CSV files
        StringBuilder data = new StringBuilder();

        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            // Get the workbook object for XLSX file
            FileInputStream fis = new FileInputStream(inputFile);
            Workbook workbook;

            String ext = FilenameUtils.getExtension(inputFile.toString());

            workbook = new XSSFWorkbook(fis);
            // Get first sheet from the workbook

            int numberOfSheets = workbook.getNumberOfSheets();
            Row row;
            Cell cell;
            // Iterate through each rows from first sheet

            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row cells : sheet) {
                    row = cells;
                    // For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {

                        cell = cellIterator.next();

                        switch (cell.getCellType()) {
                            case BOOLEAN -> data.append(cell.getBooleanCellValue()).append(",");
                            case NUMERIC -> data.append(cell.getNumericCellValue()).append(",");
                            case STRING -> data.append(cell.getStringCellValue()).append(",");
                            case BLANK -> data.append("" + ",");
                            default -> {
                                data.append(cell).append(",");
                            }
                        }
                    }
                    data.append('\n'); // appending new line after each row
                }

            }
            fos.write(data.toString().getBytes());
            fos.close();
            return outputFile.getPath();

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return "";
    }
}
