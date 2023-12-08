import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.poi.ss.usermodel.*;

import dev.shingi.models.FileConfig;
import dev.shingi.models.Transaction;
import dev.shingi.models.TransactionFile;
import dev.shingi.services.DynamicFileWriter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class DynamicFileWriterTest {

    private DynamicFileWriter dynamicFileWriter;
    private FileConfig mockFileConfig;
    private Sheet mockSheet;
    private Row mockRow;
    private Cell mockCell;
    private Transaction mockTransaction;
    private List<Transaction> mockTransactions;
    private TransactionFile mockTransactionFile;

    @BeforeEach
    public void setUp() {
        // Initialize or mock FileConfig based on your actual class
        mockFileConfig = mock(FileConfig.class);

        // Stub methods to return specific values
        when(mockFileConfig.getSourceName()).thenReturn("TestSource");
        when(mockFileConfig.getDateHeaderName()).thenReturn("Date");
        when(mockFileConfig.getAmountHeaderName()).thenReturn("Amount");
        when(mockFileConfig.getDescriptionHeaderName()).thenReturn("Description");
        when(mockFileConfig.getDateFormat()).thenReturn(new SimpleDateFormat("yyyy-MM-dd"));
        when(mockFileConfig.hasSeparateCreditDebit()).thenReturn(true);
        when(mockFileConfig.getDebetOrCreditHeaderName()).thenReturn("Credit");

        // Create and stub the allHeaderIndices map
        Map<String, Integer> allHeaderIndices = new HashMap<>();
        allHeaderIndices.put("Date", 0);
        allHeaderIndices.put("Amount", 1);
        allHeaderIndices.put("Description", 2);
        allHeaderIndices.put("Credit", 3);
        allHeaderIndices.put("Debit", 4);
        when(mockFileConfig.getAllHeaderIndices()).thenReturn(allHeaderIndices);
        
        // Initialize DynamicFileWriter
        dynamicFileWriter = new DynamicFileWriter(mockFileConfig);

        // Create mock Transaction and add it to mockTransactionFile
        Map<Object, String> entireRow = new HashMap<>();
        entireRow.put("2021-01-01", "DATE");
        entireRow.put(50.0, "NUMERIC");
        entireRow.put("Groceries", "STRING");
        mockTransaction = new Transaction(LocalDate.of(2021, 1, 1), 50.0, "Groceries", entireRow, 1);
        
        // Create a mock List of Transactions
        mockTransactions = new ArrayList<>();
        mockTransactions.add(mockTransaction);

        // Create a mock TransactionFile with the mocktransaction
        mockTransactionFile = new TransactionFile("TestBank");
        mockTransactionFile.addTransaction(mockTransaction);

        // Mocking the Sheet, Row, and Cell classes
        mockSheet = mock(Sheet.class);
        mockRow = mock(Row.class);
        mockCell = mock(Cell.class);
        when(mockSheet.createRow(anyInt())).thenReturn(mockRow);
        when(mockRow.createCell(anyInt())).thenReturn(mockCell);
    }

    @Test
    public void testWriteHeader() {
        // Call the method under test
        dynamicFileWriter.writeHeader(mockSheet, mockFileConfig);

        // Verify that cells are created and set with correct values
        verify(mockRow, times(3)).createCell(anyInt());  // 3 headers, so 3 cells should be created

        // Assuming setCellValue is called on mockCell to set the header names
        verify(mockCell).setCellValue("Date");
        verify(mockCell).setCellValue("Amount");
        verify(mockCell).setCellValue("Description");
    }

    @Test
    public void testWriteTransactions() {
        // Call the writeTransactions method
        dynamicFileWriter.writeTransactions(mockSheet, mockTransactions, mockFileConfig);

        // Verify that the correct number of rows are created (in this case, 1 for 1 transaction)
        verify(mockSheet, times(1)).createRow(anyInt());

        // Verify that the correct number of cells are created (in this case, 3 for Date, Amount, and Description)
        verify(mockRow, times(3)).createCell(anyInt());

        // Verify that the cell value setters are called correctly
        verify(mockCell).setCellValue(eq(LocalDate.of(2021, 1, 1)));
        verify(mockCell).setCellValue(eq(50.0));
        verify(mockCell).setCellValue(eq("Groceries"));
    }

    @Test
    public void testFullWrite() {
        // Call the writeToWorkbook method
        dynamicFileWriter.writeToWorkbook(mockTransactionFile);

        // Verify that writeHeader and writeTransactions methods were called
        verify(mockSheet, times(1)).createRow(0);  // Header row
        verify(mockSheet, times(1)).createRow(1);  // First transaction row

        // Verify that cells in the header row are correctly populated
        verify(mockCell).setCellValue("Date");
        verify(mockCell).setCellValue("Amount");
        verify(mockCell).setCellValue("Description");

        // Verify that cells in the transaction row are correctly populated
        verify(mockCell).setCellValue(LocalDate.of(2021, 1, 1));
        verify(mockCell).setCellValue(50.0);
        verify(mockCell).setCellValue("Groceries");
    }
}