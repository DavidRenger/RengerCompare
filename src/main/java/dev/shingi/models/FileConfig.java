package dev.shingi.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import dev.shingi.utils.ExcelSheetUtils;

public class FileConfig {
    // Name and date format
    private String sourceName;
    private SimpleDateFormat dateFormat;

    // Amount format and potential debetCredit header name
    private AmountInfo amountInfo;
    private String debetCreditHeaderName;
    private String debetHeaderName;
    private String creditHeaderName;

    // Basic read information
    private int headerRowNumber;
    private int firstTransactionRowNumber;
    
    // Basic header names
    private String dateHeaderName;
    private String descriptionHeaderName;

    // List of all headers and map connecting header names and column indices
    private List<String> allHeaderNames;
    // private Map<String, Integer> allHeaderIndices;

    public FileConfig(String sourceName, String dateFormat, AmountInfo amountInfo,  int headerRowNumber, int firstTransactionRowNumber, String dateHeaderName, String descriptionHeaderName, String debetCreditHeaderName) {
        this.sourceName = sourceName;

        this.dateFormat = new SimpleDateFormat(dateFormat);

        this.amountInfo = amountInfo;
        this.debetCreditHeaderName = debetCreditHeaderName;

        this.dateHeaderName = dateHeaderName;
        this.descriptionHeaderName = descriptionHeaderName;

        this.headerRowNumber = headerRowNumber;
        this.firstTransactionRowNumber = firstTransactionRowNumber;

        // this.allHeaderIndices = allHeaderIndices;
        this.allHeaderNames = addAllHeaderNames();
    }

    // Used in EditFileConfigController in the handleAddFileConfigButton method to initialize an new, empty file configuration.
    public FileConfig(String sourceName) {
        this.sourceName = sourceName;
    }

    private List<String> addAllHeaderNames() {
        List<String> headerNames = new ArrayList<String>();

        // Add mandatory headers
        if (dateHeaderName != null) headerNames.add(dateHeaderName);
        // TO DO: if (amountHeaderName != null) headerNames.add(amountHeaderName);
        if (descriptionHeaderName != null) headerNames.add(descriptionHeaderName);

        // Add optional headers for credit and debit if applicable
        if (amountInfo.isInSeparateColumn()) {
            if (debetCreditHeaderName != null) headerNames.add(debetCreditHeaderName);
        } else if (amountInfo.isInSeparateColumns()) {
            if (debetHeaderName != null) headerNames.add(debetHeaderName);
            if (creditHeaderName != null) headerNames.add(creditHeaderName);
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

    public String getDebetHeaderName() {
        return debetHeaderName;
    }

    public void setDebetHeaderName(String debetHeaderName) {
        this.debetHeaderName = debetHeaderName;
    }

    public String getCreditHeaderName() {
        return creditHeaderName;
    }

    public void setCreditHeaderName(String creditHeaderName) {
        this.creditHeaderName = creditHeaderName;
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

    public AmountInfo getAmountFormat() {
        return amountInfo;
    }

    public void setAmountFormat(AmountInfo amountInfo) {
        this.amountInfo = amountInfo;
    }

    public String getDebetCreditHeaderName() {
        return debetCreditHeaderName;
    }

    public void setDebetCreditHeaderName(String debetCreditHeaderName) {
        this.debetCreditHeaderName = debetCreditHeaderName;
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

    // public Map<String, Integer> getAllHeaderIndices() {
    //     return allHeaderIndices;
    // }

    // public void setAllHeaderIndices(Map<String, Integer> allHeaderIndices) {
    //     this.allHeaderIndices = allHeaderIndices;
    // }
}
