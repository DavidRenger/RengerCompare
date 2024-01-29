package dev.shingi.models;

public class AmountFormat3 extends AmountFormat {

    private String debetHeaderName;
    private String creditHeaderName;

    /*
     * A FileConfig has this format if:
     *   - the debet amount is in one column;
     *   - the credit amount is in another column.
     */
    public AmountFormat3(String debetHeaderName, String creditHeaderName) {
        this.debetHeaderName = debetHeaderName;
        this.creditHeaderName = creditHeaderName;
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
}
