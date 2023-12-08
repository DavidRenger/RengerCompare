package dev.shingi.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionFile {
    private String sourceName;  // Name of the bank or software
    private List<Transaction> transactions;  // List of transactions

    public TransactionFile(String sourceName) {
        this.sourceName = sourceName;
        this.transactions = new ArrayList<Transaction>();
    }

    // Method to add a single transaction
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        try {
            transactions.remove(transactions.indexOf(transaction));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such transaction exists in this transaction file.");
        }
    }

    // Method to add multiple transactions
    public void addTransactions(List<Transaction> transactions) {
        this.transactions.addAll(transactions);
    }

    // Getters and Setters
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionFile that = (TransactionFile) o;

        // Compare the size of the transactions list
        if (this.transactions.size() != that.transactions.size()) return false;

        // Use sets for an efficient comparison
        Set<Transaction> thisTransactionsSet = new HashSet<>(this.transactions);
        Set<Transaction> thatTransactionsSet = new HashSet<>(that.transactions);

        return thisTransactionsSet.equals(thatTransactionsSet);
    }

    // Additional utility methods can be added as needed
}
