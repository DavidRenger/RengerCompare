package dev.shingi.utils;

import org.apache.poi.ss.usermodel.*;

import dev.shingi.models.FileConfig;
import dev.shingi.models.FileConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ExcelSheetUtils {

    public static String rowToCSV(Row row) {
        StringBuilder sb = new StringBuilder();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch (cell.getCellType()) {
                case STRING:
                    sb.append(cell.getStringCellValue());
                    break;
                case NUMERIC:
                    sb.append(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    sb.append(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    sb.append(cell.getCellFormula());
                    break;
                // Add more cases for other cell types, if needed
                default:
                    sb.append(cell.toString());
            }
            if (cellIterator.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static Map<Object, String> generateRowMap(Row row) {
        Map<Object, String> rowMap = new HashMap<>();
        Map<Object, Integer> duplicateCountMap = new HashMap<>();
        
        for (Cell cell : row) {
            CellType cellType = cell.getCellType();
            Object cellValue = null;
            String cellTypeString = cellType.name();

            switch (cellType) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    cellValue = cell.getNumericCellValue();
                    break;
                case BOOLEAN:
                    cellValue = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    cellValue = cell.getCellFormula();
                    break;
                case BLANK:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString();
            }

            // Check for duplicates
            if (rowMap.containsKey(cellValue)) {
                int count = duplicateCountMap.getOrDefault(cellValue, 1);
                duplicateCountMap.put(cellValue, count + 1);
                cellValue = cellValue.toString() + "_" + count;
            }
            
            rowMap.put(cellValue, cellTypeString);
        }
        
        return rowMap;
    }
    
    public static Row createRowFromRowMap(Sheet sheet, int rowNumber, Map<Object, String> entireRow, FileConfig fileConfig) {
        Row row = sheet.createRow(rowNumber);
        int cellIndex = 0;
        for (Map.Entry<Object, String> entry : entireRow.entrySet()) {
            Object key = entry.getKey();
            String value = entry.getValue();
            Cell cell = row.createCell(cellIndex);

            CellType cellType = CellType.valueOf(value);
            switch (cellType) {
            case STRING:
                cell.setCellValue((String) key);
                break;
            case NUMERIC:
                cell.setCellValue((Double) key);
                break;
            case BOOLEAN:
                cell.setCellValue((Boolean) key);
                break;
            case FORMULA:
                cell.setCellFormula((String) key);
                break;
            // Add cases for other cell types as needed
            default:
                cell.setCellValue(key.toString());
            }

            cellIndex++;
        }
        return row;
    }

    public static Row findLikelyHeaderRow(Sheet sheet) {
        List<FileConfig> existingConfigs = new ArrayList<FileConfig>(FileConfigManager.getInstance().getConfigsMap().values());

        for (Row row : sheet) {
            int matchCount = 0;

            for (Cell cell : row) {
                String cellValue = cell.toString().trim().toLowerCase();

                for (FileConfig config : existingConfigs) {
                    if (config.getAllHeaderNames().contains(cellValue)) {
                        matchCount++;
                        break; // Skip to next cell once a match is found for this cell
                    }
                }

                if (matchCount >= 3) {
                    return row;
                }
            }
        }
        return null;
    }

    // Loops through the headernames of all existing fileconfigs to see if the headerrow in this new fileconfig can be automatically recognized
    public static Map<String, Integer> detectHeaderIndices(Row headerRow) {
        List<FileConfig> existingConfigs = new ArrayList<FileConfig>(FileConfigManager.getInstance().getConfigsMap().values());

        Map<String, Integer> headerIndices = new HashMap<String, Integer>();

        // Loop through all cells in the header row
        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue();
            int columnIndex = cell.getColumnIndex();

            // Loop through all existing configurations to match headers
            for (FileConfig config : existingConfigs) {
                List<String> allHeaderNames = config.getAllHeaderNames();
                
                // Loop through all headers in the current config
                for (String headerName : allHeaderNames) {
                    
                    // Use a string similarity function to find approximate matches
                    if (stringSimilarity(cellValue, headerName) > 0.8) {  // Threshold can be adjusted
                        String standardizedKey = getStandardizedKey(headerName, config);
                        headerIndices.put(standardizedKey, columnIndex);
                    }
                }
            }
        }

        return headerIndices;
    }

    public static Map<String, Integer> detectHeaderIndices(Row headerRow, FileConfig fileConfig) {
        Map<String, Integer> headerIndices = new HashMap<String, Integer>();
        List<String> allHeaderNames = fileConfig.getAllHeaderNames();

        // Loop through all cells in the header row
        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue();
                
            // Loop through all headers in the current config
            for (String headerName : allHeaderNames) {
                
                if (cellValue.equals(headerName)) {
                    String standardizedKey = getStandardizedKey(headerName, fileConfig);
                    headerIndices.put(standardizedKey, cell.getColumnIndex());
                }
            }
        }
        return headerIndices;
    }

    public static boolean detectSeparateCreditDebit(Row headerRow) {
        boolean hasCredit = false;
        boolean hasDebit = false;

        // Loop through each cell in the header row
        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue();

            // Check for approximate match with "credit"
            if (stringSimilarity(cellValue, "credit") > 0.8) {
                hasCredit = true;
            }

            // Check for approximate match with "debit"
            if (stringSimilarity(cellValue, "debit") > 0.8) {
                hasDebit = true;
            }
        }

        return hasCredit && hasDebit;
    }

    private static String getStandardizedKey(String headerName, FileConfig config) {
        if (headerName.equalsIgnoreCase(config.getDateHeaderName())) {
            return FileConfigManager.getInstance().getDATE_FORMAT();
        } else if (headerName.equalsIgnoreCase(config.getAmountHeaderName())) {
            return FileConfigManager.getInstance().getAMOUNT_FORMAT();
        } else if (headerName.equalsIgnoreCase(config.getDescriptionHeaderName())) {
            return FileConfigManager.getInstance().getDESCRIPTION_FORMAT();
        } else if (headerName.equalsIgnoreCase(config.getDebetOrCreditHeaderName())) {
            return FileConfigManager.getInstance().getDEBET_OR_CREDIT_FORMAT();
        }
        
        return null;  // or throw an exception if you prefer
    }    

    private static double stringSimilarity(String str1, String str2) {
        Set<Character> set1 = new HashSet<Character>();
        Set<Character> set2 = new HashSet<Character>();
        
        for (char c : str1.toLowerCase().toCharArray()) {
            set1.add(c);
        }
        
        for (char c : str2.toLowerCase().toCharArray()) {
            set2.add(c);
        }
        
        Set<Character> intersection = new HashSet<Character>(set1);
        intersection.retainAll(set2);
        
        double union = set1.size() + set2.size() - intersection.size();
        
        return union != 0 ? (double) intersection.size() / union : 0;
    }

    // TO BE IMPLEMENTED: CALLED IN FILECONFIGAUTODETECTOR, SHOULD TAKE THE SHEET AND THE INDEX
    // OF THE SEPARATE CREDITORDEBET COLUMN AND FIND HOW DEBET OR CREDIT IS INDICATED.
    public static String findLikelyCreditFormat() {
        return null;
    }

    public static String findLikelyDebetFormat() {
        return null;
    }
}