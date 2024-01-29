package dev.shingi.models;

public class AmountFormat2 extends AmountFormat {
    
    private String amountHeaderName;
    private String amountFormatHeaderName;
    private String debetFormat;
    private String creditFormat;

    /*
     * A FileConfig has this format if:
     *   - the amount is in one column;
     *   - the amount format is in another column.
     */
     public AmountFormat2(String amountHeaderName, String amountFormatHeaderName, String debetFormat, String creditFormat) {
        this.amountHeaderName = amountHeaderName;
        this.amountFormatHeaderName = amountFormatHeaderName;
        this.debetFormat = debetFormat;
        this.creditFormat = creditFormat;
    }

    public String getAmountHeaderName() {
        return amountHeaderName;
    }

    public void setAmountHeaderName(String amountHeaderName) {
        this.amountHeaderName = amountHeaderName;
    }
    
    public String getAmountFormatHeaderName() {
        return amountFormatHeaderName;
    }

    public void setAmountFormatHeaderName(String amountFormatHeaderName) {
        this.amountFormatHeaderName = amountFormatHeaderName;
    }

    public String getDebetFormat() {
        return debetFormat;
    }

    public void setDebetFormat(String debetFormat) {
        this.debetFormat = debetFormat;
    }

    public String getCreditFormat() {
        return creditFormat;
    }

    public void setCreditFormat(String creditFormat) {
        this.creditFormat = creditFormat;
    }
}
