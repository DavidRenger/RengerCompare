package dev.shingi.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

public class Transaction {
    private LocalDate date;
    private double amount;  // Positive or negative based on credit/debit
    private String description;
    private Map<Object, String> entireRow;
    private int rowNumber;

    public Transaction(LocalDate date, double amount, String description, Map<Object, String> entireRow, int rowNumber) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.entireRow = entireRow;
        this.rowNumber = rowNumber;
    }

    @Override
    public String toString() {
        return "Transaction [Date=" + date + ", Amount=" + amount + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;

        // Compute the difference in days between the two dates
        long differenceInDays = ChronoUnit.DAYS.between(this.date, that.date);

        // Check if dates are within 3 days of each other and if amounts are the same
        return Math.abs(differenceInDays) <= 3 && Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount);
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Object, String> getEntireRow() {
        return entireRow;
    }
    
    public void setEntireRow(Map<Object, String> entireRow) {
        this.entireRow = entireRow;
    }    

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}
