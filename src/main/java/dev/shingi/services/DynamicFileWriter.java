package dev.shingi.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dev.shingi.models.FileConfig;
import dev.shingi.models.FileConfigManager;
import dev.shingi.models.Transaction;
import dev.shingi.models.TransactionFile;
import dev.shingi.utils.ExcelSheetUtils;

public class DynamicFileWriter {
    private FileConfig fileConfig;

    public DynamicFileWriter(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    // Write a TransactionFile to an Excel Workbook
    public Workbook writeToWorkbook(TransactionFile transactionFile) {
        Workbook workbook = new XSSFWorkbook(); // or new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        // Retrieve the FileConfig for the given source name
        if (fileConfig == null) {
            throw new IllegalArgumentException("No FileConfig found for source: " + transactionFile.getSourceName());
        }

        // Write the header
        writeHeader(sheet, fileConfig);

        // Write the transactions
        writeTransactions(sheet, transactionFile.getTransactions(), fileConfig);

        return workbook;
    }

    public void writeHeader(Sheet sheet, FileConfig fileConfig) {
        Map<Object, String> headerMap = new LinkedHashMap<>();  // Using LinkedHashMap to preserve insertion order

        // Populate the header map
        headerMap.put(fileConfig.getDateHeaderName(), FileConfigManager.getInstance().DATE_FORMAT);
        headerMap.put(fileConfig.getAmountFormat().getAmountHeaderName(), FileConfigManager.getInstance().AMOUNT_FORMAT);
        headerMap.put(fileConfig.getDescriptionHeaderName(), FileConfigManager.getInstance().DESCRIPTION_FORMAT);

        // TO DO:
        // if (fileConfig.hasSeparateCreditDebit()) {
        //     headerMap.put(fileConfig.getDebetOrCreditHeaderName(), FileConfigManager.getInstance().DEBET_OR_CREDIT_FORMAT);
        // }

        // Use ExcelSheetUtils to write the header row
        ExcelSheetUtils.createRowFromRowMap(sheet, 0, headerMap, fileConfig);  // Assuming header will be at row 0
    }


    public void writeTransactions(Sheet sheet, List<Transaction> transactions, FileConfig fileConfig) {
        int rowIndex = 1; // Assuming header is at row 0

        for (Transaction transaction : transactions) {
            Map<Object, String> entireRow = transaction.getEntireRow();

            // Use the utility method to create a new row in the sheet
            ExcelSheetUtils.createRowFromRowMap(sheet, rowIndex, entireRow, fileConfig);

            rowIndex++;
        }
    }
}