package dev.shingi.models;

public class AmountFormat1 extends AmountFormat {
    
    private String amountHeaderName;

    /*
     * A FileConfig has this format if:
     *   - the amount is in 1 column;
     *   - negative amounts are debet;
     *   - positive amounts are credit.
     */
    public AmountFormat1(String amountHeaderName) {
        this.amountHeaderName = amountHeaderName;
    }

    public String getAmountHeaderName() {
        return amountHeaderName;
    }

    public void setAmountHeaderName(String amountHeaderName) {
        this.amountHeaderName = amountHeaderName;
    }
}
