package dev.shingi.utils;

import org.apache.poi.ss.usermodel.*;

import dev.shingi.models.FileConfig;

import java.util.Map;
import java.util.HashMap;

public class FileConfigAutoDetector {

    public FileConfigAutoDetector() {
        // Constructor, if needed
    }

    public FileConfig suggestFileConfig(Workbook workbook) {
        // Identify the sheet to analyze
        Sheet sheet = workbook.getSheetAt(0);
    
        // Find the row that is likely the header
        Row headerRow = ExcelSheetUtils.findLikelyHeaderRow(sheet);
    
        // Detect columns and their likely roles (date, amount, description, etc.)
        Map<String, Integer> headerIndices = ExcelSheetUtils.detectHeaderIndices(headerRow);

        // Create another map to get header names from indices
        Map<Integer, String> indexToHeaderNames = new HashMap<Integer, String>();
        for (Map.Entry<String, Integer> entry : headerIndices.entrySet()) {
            indexToHeaderNames.put(entry.getValue(), entry.getKey());
        }

        // Check if credit and debit amounts are separate
        boolean hasSeparateCreditDebit = ExcelSheetUtils.detectSeparateCreditDebit(headerRow);
    
        // Extract header names
        String dateHeaderName = indexToHeaderNames.get(headerIndices.get("Date"));
        String amountHeaderName = indexToHeaderNames.get(headerIndices.get("Amount"));
        String descriptionHeaderName = indexToHeaderNames.get(headerIndices.get("Description"));
        String debetOrCreditHeaderName = hasSeparateCreditDebit ? indexToHeaderNames.get(headerIndices.get("Af Bij")) : null;
        String creditFormat = hasSeparateCreditDebit ? ExcelSheetUtils.findLikelyCreditFormat() : null;
        String debetFormat = hasSeparateCreditDebit ? ExcelSheetUtils.findLikelyDebetFormat() : null;

        // Detect date format
        String dateFormat = DateUtils.detectDateFormat(sheet, headerIndices.get(dateHeaderName), headerRow.getRowNum());

        // Create the suggested FileConfig using the constructor
        FileConfig suggestedConfig = new FileConfig(
            "XXX",
            dateFormat,
            dateHeaderName,
            amountHeaderName,
            descriptionHeaderName,
            debetOrCreditHeaderName,
            hasSeparateCreditDebit,
            creditFormat,
            debetFormat,
            headerRow.getRowNum(),
            (headerRow.getRowNum() + 1),
            headerIndices
        );
        
        return suggestedConfig;
    }
}
