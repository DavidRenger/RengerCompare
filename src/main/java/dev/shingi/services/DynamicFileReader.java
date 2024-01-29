package dev.shingi.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import dev.shingi.models.*;
import dev.shingi.utils.ExcelSheetUtils;

public class DynamicFileReader {
    private FileConfig config;

    public DynamicFileReader(FileConfig config) {
        this.config = config;
    }

    // Main method to read an Excel file and return a TransactionFile object
    public TransactionFile readExcelFile(Workbook workbook) {
        TransactionFile transactionFile = new TransactionFile(config.getSourceName());

        // Assuming transactions are in the first sheet
        Sheet sheet = workbook.getSheetAt(0);

        Row headerRow = ExcelSheetUtils.findLikelyHeaderRow(sheet); // Find the header row dynamically
        
        // Find header index based on header names from config
        Map<String, Integer> headerIndexMap = findHeaderIndices(headerRow);

        // Read transactions
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {  // Start from 1 to skip header row
            Row row = sheet.getRow(i);
            Transaction transaction = createTransactionFromRow(row, headerIndexMap);
            transactionFile.addTransaction(transaction);
        }

        return transactionFile;
    }

    // Find the index of headers based on header names
    private Map<String, Integer> findHeaderIndices(Row headerRow) {
        Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();
        List<String> allHeaderNames = config.getAllHeaderNames();

        for (Cell cell : headerRow) {
            String cellValue = cell.toString().trim();
            int columnIndex = cell.getColumnIndex();

            if (allHeaderNames.contains(cellValue)) {
                headerIndexMap.put(cellValue, columnIndex);
            }
        }
        return headerIndexMap;
    }

    // Create a Transaction object based on a Row and header indices
    private Transaction createTransactionFromRow(Row row, Map<String, Integer> headerIndexMap) {
        // Create the Transaction object
        try {
            // Get the date
            LocalDate date = row.getCell(headerIndexMap.get(config.getDateHeaderName())).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // Assuming cell type is Date
            
            // Get the description
            String description = row.getCell(headerIndexMap.get(config.getDescriptionHeaderName())).getStringCellValue().trim(); // Assuming cell type is String
            
            // Get the row map
            Map<Object, String> entireRow = ExcelSheetUtils.generateRowMap(row);

            // Get the row number
            int rowNum = row.getRowNum();

            // Get the amount
            double amount = readAmount(headerIndexMap, row);
            
            // Create the transaction and return it
            Transaction transaction = new Transaction(date, amount, description, entireRow, rowNum);
            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private double readAmount(Map<String, Integer> headerIndexMap, Row row) {
        double amount = 0.0;
        switch(this.config.getAmountFormatType()) {
            case 1: 
                AmountFormat1 amountFormat1 = (AmountFormat1) config.getAmountFormat();
                amount = row.getCell(headerIndexMap.get(amountFormat1.getAmountHeaderName())).getNumericCellValue();
                break;
            case 2:
                AmountFormat2 amountFormat2 = (AmountFormat2) config.getAmountFormat();
                amount = row.getCell(headerIndexMap.get(amountFormat2.getAmountHeaderName())).getNumericCellValue();

                String amountType = row.getCell(headerIndexMap.get(amountFormat2.getAmountFormatHeaderName())).getStringCellValue();

                if (amountType.equals(amountFormat2.getDebetFormat())) {
                    amount *= -1;
                }
                break;
            case 3:
                AmountFormat3 amountFormat3 = (AmountFormat3) config.getAmountFormat();
                double debetAmount = (double) row.getCell(headerIndexMap.get(amountFormat3.getDebetHeaderName())).getNumericCellValue();
                double creditAmount = (double) row.getCell(headerIndexMap.get(amountFormat3.getCreditHeaderName())).getNumericCellValue();
                amount = creditAmount - debetAmount;
        }
        return amount;
    }
}
