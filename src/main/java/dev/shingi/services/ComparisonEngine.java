package dev.shingi.services;

import java.util.ArrayList;
import java.util.List;

import dev.shingi.models.*;

public class ComparisonEngine {

    // 4 steps:
    /*
     * 1. Sort the transaction by date first, second by amount.
     * 2. Compare every transaction
     * 2.1 If the transactions are not the same, see if either list contains the other's transaction (amount and date).
     *     Usually, one of the transactions being compared will be present in the other list. That means it needs to be incremented to its equal.
     *     So, add an empty transaction to that list, so that an empty transaction now matches the missing transaction.
     *     If neither transactions have an equal in the other list, add an empty transaction to the first list and continue the comparison. The empty transaction
     *     for the second list will be added automatically on the next step of the loop.
     * 3. Add possible explanation
     * 3.1 Different dates: for each missing transaction, see if the other list contains a missing transaction of the same amount but different date. 
     *                      Add an explanation to that transaction (create a model: MissingTransaction containing a Transaction and an explanation) and remove it from the missings.
     * 3.2 Doubles: for the missing transactions that are left, see if the same date in each list contains multiples of the same amount. These may be potential double bookings. Also remove these.
     * 3.3 Other: for the missing transactions with no explanation, add the explanation that they do not appear in the other list.
     * 4. 
     */

    private ArrayList<MissingTransaction> missingTransactionsList1, missingTransactionsList2, finalMissingTransactions;
    
    public List<MissingTransaction> findMissingTransactions(TransactionFile file1, TransactionFile file2, int dateToleranceInDays) {
        
        
        return finalMissingTransactions;
    }
}
