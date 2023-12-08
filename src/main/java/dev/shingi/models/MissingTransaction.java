package dev.shingi.models;

public class MissingTransaction {

    private Transaction transaction;
    private String explanation;

    public MissingTransaction(Transaction transaction, String explanation) {
        this.transaction = transaction;
        this.explanation = explanation;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
