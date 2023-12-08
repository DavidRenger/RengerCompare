package dev.shingi.models;

public class AmountInfo {
    private boolean inAmount;
    private boolean inSeparateColumn;
    private boolean inSeparateColumns;

    private String amountHeaderName;
    private String debetHeaderName;
    private String creditHeaderName;
    private String debetCreditHeaderName;

    private String creditFormat;
    private String debetFormat;

    public AmountInfo() {
        inAmount = true;
        inSeparateColumn = false;
        inSeparateColumns = false;
    }

    public boolean isInAmount() {
        return inAmount;
    }

    public void setInAmount(boolean inAmount) {
        this.inAmount = inAmount;
    }

    public boolean isInSeparateColumn() {
        return inSeparateColumn;
    }

    public void setInSeparateColumn(boolean inSeparateColumn) {
        this.inSeparateColumn = inSeparateColumn;
    }

    public boolean isInSeparateColumns() {
        return inSeparateColumns;
    }

    public void setInSeparateColumns(boolean inSeparateColumns) {
        this.inSeparateColumns = inSeparateColumns;
    }

    public String getAmountHeaderName() {
        return amountHeaderName;
    }

    public void setAmountHeaderName(String amountHeaderName) {
        this.amountHeaderName = amountHeaderName;
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

    public String getDebetCreditHeaderName() {
        return debetCreditHeaderName;
    }

    public void setDebetCreditHeaderName(String debetCreditHeaderName) {
        this.debetCreditHeaderName = debetCreditHeaderName;
    }

    public String getCreditFormat() {
        return creditFormat;
    }

    public void setCreditFormat(String creditFormat) {
        this.creditFormat = creditFormat;
    }

    public String getDebetFormat() {
        return debetFormat;
    }
    
    public void setDebetFormat(String debetFormat) {
        this.debetFormat = debetFormat;
    }
}
