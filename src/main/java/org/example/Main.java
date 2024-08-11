package org.example;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Weight Tracker");

        String filePath = "weight.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = removeLbs(line);

                String[] tokens = line.split(" ");
                if(tokens.length == 1){
                    String year = tokens[0];
                    year = removeColon(year);
                } else if (tokens.length == 2){
                    String month = tokens[0];
                    month = removeColon(month);

                    String weight = tokens[1];
                } else if (tokens.length == 3) {
                    String month = tokens[0];
                    month = removeColon(month);

                    String day = tokens[1];
                    day = removeColon(day);
                    day = removeOrdinal(day);

                    String weight = tokens[1];
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
        if (str.length() > 2) {
            return str.substring(0, str.length() - 2);
        }
        return str;
    }

    private static String removeOrdinal(String day) {
        if (day.length() > 2) {
            return day.substring(0, day.length() - 2);
        }
        return day;
    }
}