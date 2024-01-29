package dev.shingi.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import dev.shingi.utils.ExcelSheetUtils;

public class FileConfig {
    // Name
    private String sourceName;

    // Date format
    private SimpleDateFormat dateFormat;

    // Basic header names
    private String dateHeaderName;
    private String descriptionHeaderName;

    // Basic read information
    private int headerRowNumber;
    private int firstTransactionRowNumber;

    // Amount format settings
    private int amountFormatType; // this can only take the value 1, 2, or 3, indicating which type of amount formatting this fileConfig uses
    private AmountFormat1 amountFormat1;
    private AmountFormat2 amountFormat2;
    private AmountFormat3 amountFormat3;

    // List of all header names
    private List<String> allHeaderNames;

    public FileConfig(String sourceName, String dateFormat, int amountFormatType, int headerRowNumber, int firstTransactionRowNumber, String dateHeaderName, String descriptionHeaderName) {
        this.sourceName = sourceName;

        this.dateFormat = new SimpleDateFormat(dateFormat);

        this.amountFormatType = amountFormatType;

        this.dateHeaderName = dateHeaderName;
        this.descriptionHeaderName = descriptionHeaderName;

        this.headerRowNumber = headerRowNumber;
        this.firstTransactionRowNumber = firstTransactionRowNumber;

        this.allHeaderNames = addAllHeaderNames();
    }

    private List<String> addAllHeaderNames() {
        List<String> headerNames = new ArrayList<String>();

        // Add mandatory headers
        if (this.dateHeaderName != null) headerNames.add(dateHeaderName);
        // TO DO: if (amountHeaderName != null) headerNames.add(amountHeaderName);
        if (this.descriptionHeaderName != null) headerNames.add(this.descriptionHeaderName);

        // Add optional headers for credit and debit if applicable
        if (this.amountFormatType == 1) {
            headerNames.add(this.amountFormat1.getAmountHeaderName());
        } else if (this.amountFormatType == 2) {
            headerNames.add(this.amountFormat2.getAmountHeaderName());
            headerNames.add(this.amountFormat2.getAmountFormatHeaderName());
        } else if (this.amountFormatType == 3) {
            headerNames.add(this.amountFormat3.getDebetHeaderName());
            headerNames.add(this.amountFormat3.getCreditHeaderName());
        }

        return headerNames;
    }

    public Map<String, Integer> getAllHeaderIndices(Row row) {
        return ExcelSheetUtils.detectHeaderIndices(row, this);
    }

    @Override
    public String toString() {
        return sourceName;
    }

    // Getters and Setters
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDateHeaderName() {
        return dateHeaderName;
    }

    public void setDateHeaderName(String dateHeaderName) {
        this.dateHeaderName = dateHeaderName;
    }

    public String getDescriptionHeaderName() {
        return descriptionHeaderName;
    }

    public void setDescriptionHeaderName(String descriptionHeaderName) {
        this.descriptionHeaderName = descriptionHeaderName;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getAmountFormatType() {
        return this.amountFormatType;
    }

    public AmountFormat getAmountFormat() {
        if (this.amountFormatType == 1) {
            return this.amountFormat1;
        } else if (this.amountFormatType == 2) {
            return this.amountFormat2;
        } else if (this.amountFormatType == 3) {
            return this.amountFormat3;
        } else {
            return null;
        }
    }

    public int getHeaderRowNumber() {
        return headerRowNumber;
    }

    public void setHeaderRowNumber(int headerRowNumber) {
        this.headerRowNumber = headerRowNumber;
    }

    public int getFirstTransactionRowNumber() {
        return firstTransactionRowNumber;
    }

    public void setFirstTransactionRowNumber(int firstTransactionRowNumber) {
        this.firstTransactionRowNumber = firstTransactionRowNumber;
    }

    public List<String> getAllHeaderNames() {
        return allHeaderNames;
    }

    public void setAllHeaderNames(List<String> allHeaderNames) {
        this.allHeaderNames = allHeaderNames;
    }
}
