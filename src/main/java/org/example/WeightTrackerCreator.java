package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class WeightTrackerCreator {
    public static void main(String[] args) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Weight Tracker");
        String filePath = "weight.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int rowIdx = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isEmpty()){
                    rowIdx = createSpacerRow(sheet, rowIdx);
                } else{
                    line = removeLbs(line);
                    String[] tokens = line.split(" ");
                    if(tokens.length == 1){
                        String year = tokens[0];
                        year = removeColon(year);

                        rowIdx = createRowWithYear(sheet, rowIdx, year);
                        rowIdx = createMonthDayWeightHeaderRow(sheet, rowIdx);

                    } else if (tokens.length == 2){
                        String month = tokens[0];
                        month = removeColon(month);

                        String day = "15";

                        String weight = tokens[1];

                        rowIdx = createDataRow(sheet, rowIdx, month, day, weight);
                    } else if (tokens.length == 3) {
                        String month = tokens[0];
                        month = removeColon(month);

                        String day = tokens[1];
                        day = removeColon(day);
                        day = removeOrdinal(day);

                        String weight = tokens[2];

                        rowIdx = createDataRow(sheet, rowIdx, month, day, weight);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("WeightTracker.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String removeLbs(String line) {
        return line.replace(" lbs", "");
    }

    private static String removeColon(String str) {
        return str.replace(":", "");
    }

    private static String removeOrdinal(String day) {
        if (day.length() > 2) {
            return day.substring(0, day.length() - 2);
        }
        return day;
    }

    private static int createSpacerRow(Sheet sheet, int rowIdx) {
        sheet.createRow(rowIdx++);
        return rowIdx;
    }

    private static int createRowWithYear(Sheet sheet, int rowIdx, String year) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue(Integer.valueOf(year));
        return rowIdx;
    }

    private static int createMonthDayWeightHeaderRow(Sheet sheet, int rowIdx) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue("MONTH/DAY");
        row.createCell(1).setCellValue("WEIGHT (LBS)");
        return rowIdx;
    }

    private static int createDataRow(Sheet sheet, int rowIdx, String month, String day, String weight) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue(month + " " + day);
        row.createCell(1).setCellValue(Double.valueOf(weight));
        return rowIdx;
    }
}