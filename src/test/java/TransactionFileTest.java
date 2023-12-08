import dev.shingi.models.Transaction;
import dev.shingi.models.TransactionFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionFileTest {

    private TransactionFile transactionFile;
    private Transaction mockTransaction1;
    private Transaction mockTransaction2;
    private List<Transaction> mockTransactionList;

    @BeforeEach
    public void setUp() {
        // Initialize TransactionFile
        transactionFile = new TransactionFile("TestBank");

        // Create mock Transaction 1
        Map<String, Object> entireRow1 = new HashMap<>();
        entireRow1.put("STRING_Date", "2021-01-01");
        entireRow1.put("NUMERIC_Amount", 50.0);
        entireRow1.put("STRING_Description", "Groceries");
        mockTransaction1 = Mockito.mock(Transaction.class);
        Mockito.when(mockTransaction1.getDate()).thenReturn(LocalDate.of(2021, 1, 1));
        Mockito.when(mockTransaction1.getAmount()).thenReturn(50.0);
        Mockito.when(mockTransaction1.getDescription()).thenReturn("Groceries");

        // Create mock Transaction 2
        Map<String, Object> entireRow2 = new HashMap<>();
        entireRow2.put("STRING_Date", "2021-01-02");
        entireRow2.put("NUMERIC_Amount", 100.0);
        entireRow2.put("STRING_Description", "Rent");
        mockTransaction2 = Mockito.mock(Transaction.class);
        Mockito.when(mockTransaction2.getDate()).thenReturn(LocalDate.of(2021, 1, 2));
        Mockito.when(mockTransaction2.getAmount()).thenReturn(100.0);
        Mockito.when(mockTransaction2.getDescription()).thenReturn("Rent");

        // Create a mock List of Transactions
        mockTransactionList = new ArrayList<>();
        mockTransactionList.add(mockTransaction1);
        mockTransactionList.add(mockTransaction2);

        // Add transactions to the initial TransactionFile
        transactionFile.addTransactions(mockTransactionList);
    }

    @Test
    public void testAddTransaction() {
        // Create a new TransactionFile and add a single mock transaction
        TransactionFile newTransactionFile = new TransactionFile("TestBank");
        newTransactionFile.addTransaction(mockTransaction1);
        assertEquals(1, newTransactionFile.getTransactions().size());
        assertEquals(mockTransaction1, newTransactionFile.getTransactions().get(0));
    }

    @Test
    public void testAddTransactions() {
        // Create a new TransactionFile and add multiple mock transactions
        TransactionFile newTransactionFile = new TransactionFile("TestBank");
        newTransactionFile.addTransactions(mockTransactionList);
        assertEquals(2, newTransactionFile.getTransactions().size());
        assertEquals(mockTransactionList, newTransactionFile.getTransactions());
    }

    @Test
    public void testRemoveTransaction() {
        // Remove a single mock transaction
        transactionFile.removeTransaction(mockTransaction1);
        assertEquals(1, transactionFile.getTransactions().size());
        assertEquals(mockTransaction2, transactionFile.getTransactions().get(0));
    }

    @Test
    public void testEquals() {
        // Create another TransactionFile and compare
        TransactionFile anotherTransactionFile = new TransactionFile("TestBank");
        anotherTransactionFile.addTransactions(mockTransactionList);

        assertEquals(transactionFile, anotherTransactionFile);
    }
}
