package dev.shingi.services;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import dev.shingi.models.FileConfig;
import dev.shingi.models.Transaction;
import dev.shingi.models.TransactionFile;
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
        for (Cell cell : headerRow) {
            String cellValue = cell.toString().trim();
            int columnIndex = cell.getColumnIndex();
            if (cellValue.equals(config.getDateHeaderName()) || 
                cellValue.equals(config.getAmountFormat().getAmountHeaderName()) || 
                cellValue.equals(config.getDescriptionHeaderName()) ||
                (config.getAmountFormat().isInSeparateColumn() && (
                    cellValue.equals(config.getDebetCreditHeaderName())))) {
                headerIndexMap.put(cellValue, columnIndex);
            }
        }
        return headerIndexMap;
    }
    

    // Create a Transaction object based on a Row and header indices
    private Transaction createTransactionFromRow(Row row, Map<String, Integer> headerIndexMap) {
        // Create the Transaction object
        try {
            Transaction transaction = new Transaction(
                row.getCell(headerIndexMap.get(config.getDateHeaderName())).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), // Assuming cell type is Date
                row.getCell(headerIndexMap.get(config.getAmountFormat().getAmountHeaderName())).getNumericCellValue(), // Assuming cell type is Numeric
                row.getCell(headerIndexMap.get(config.getDescriptionHeaderName())).getStringCellValue().trim(), // Assuming cell type is String
                ExcelSheetUtils.generateRowMap(row),
                row.getRowNum()
            );
            
            // Additional logic to handle credit and debit separately, if applicable
            if (config.getAmountFormat().isInSeparateColumn()) {
                Cell debetOrCreditCell = row.getCell(headerIndexMap.get(config.getDebetCreditHeaderName()));
                if (debetOrCreditCell.getStringCellValue().equals(config.getAmountFormat().getDebetFormat())) {
                    transaction.setAmount(transaction.getAmount() * -1);
                }
            }

        return transaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
