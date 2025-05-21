package com.example.task.Alephys;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DailyExpenseTracker {
    public static void main(String[] args) throws IOException {
        String filePath = "./DailyExpensetracker.xlsx";
        Workbook workbook;
        Sheet sheet;
        File file = new File(filePath);
        int rownumber;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = null;
        
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            rownumber = sheet.getLastRowNum(); 
            fis.close();
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("DailyExpense");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Income");
            header.createCell(2).setCellValue("Income Type");
            header.createCell(3).setCellValue("Expense");
            header.createCell(4).setCellValue("Expense Type");
            rownumber = 0;
        }

        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("Do you want to enter data? (Yes/No): ");
            if (!s.nextLine().equalsIgnoreCase("Yes")) {
                break;
            }
            rownumber++;
            Row nextrow = sheet.createRow(rownumber);

            System.out.println("Enter date (dd-mm-yyyy): ");
            String dateenter = s.nextLine();
            dateenter.trim();
          
            try {
            date = LocalDate.parse(dateenter, dtf);
            nextrow.createCell(0).setCellValue(date.toString());
            }
            catch(Exception e) {
            	System.out.println("Invalid Format");
            }
            System.out.println("Is it expense or income?: ");
            String type = s.nextLine().trim();

            if (type.equalsIgnoreCase("Income")) {
                try {
                    System.out.println("Enter your income: ");
                    int income = s.nextInt();
                    nextrow.createCell(1).setCellValue(income);
                    s.nextLine();
                    System.out.println("Enter income type: ");
                    nextrow.createCell(2).setCellValue(s.nextLine());
                } catch (Exception e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    s.nextLine(); 
                }
            } else if (type.equalsIgnoreCase("Expense")) {
                try {
                    System.out.println("Enter your expense: ");
                    int expense = s.nextInt();
                    nextrow.createCell(3).setCellValue(expense);
                    s.nextLine();
                    System.out.println("Enter expense type: ");
                    nextrow.createCell(4).setCellValue(s.nextLine());
                } catch (Exception e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    s.nextLine(); 
                }
            } else {
                System.out.println("Invalid Input");
            }
        }

        
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
        s.close();
        System.out.println("Data saved successfully.");
        
        
    }
}
