import dev.shingi.models.FileConfig;
import dev.shingi.models.Transaction;
import dev.shingi.models.TransactionFile;
import dev.shingi.services.DynamicFileReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DynamicFileReaderTest {

    private DynamicFileReader dynamicFileReader;
    private Workbook mockWorkbook;
    private Sheet mockSheet;
    private Row mockHeaderRow;
    private Row mockDataRow;
    private Cell mockDateCell;
    private Cell mockAmountCell;
    private Cell mockDescriptionCell;
    private FileConfig mockConfig;

    @BeforeEach
    public void setUp() {
        mockConfig = mock(FileConfig.class);
        when(mockConfig.getDateHeaderName()).thenReturn("Date");
        when(mockConfig.getAmountHeaderName()).thenReturn("Amount");
        when(mockConfig.getDescriptionHeaderName()).thenReturn("Description");
        
        dynamicFileReader = new DynamicFileReader(mockConfig);

        mockWorkbook = mock(Workbook.class);
        mockSheet = mock(Sheet.class);
        mockHeaderRow = mock(Row.class);
        mockDataRow = mock(Row.class);
        mockDateCell = mock(Cell.class);
        mockAmountCell = mock(Cell.class);
        mockDescriptionCell = mock(Cell.class);

        when(mockWorkbook.getSheetAt(0)).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(2);
        when(mockSheet.getRow(0)).thenReturn(mockHeaderRow);
        when(mockSheet.getRow(1)).thenReturn(mockDataRow);

        when(mockHeaderRow.getCell(0)).thenReturn(mockDateCell);
        when(mockHeaderRow.getCell(1)).thenReturn(mockAmountCell);
        when(mockHeaderRow.getCell(2)).thenReturn(mockDescriptionCell);
        
        when(mockDateCell.toString()).thenReturn("Date");
        when(mockAmountCell.toString()).thenReturn("Amount");
        when(mockDescriptionCell.toString()).thenReturn("Description");
    }

    @Test
    public void testReadExcelFile() {
        // Prepare mock data cells
        Cell dataDateCell = mock(Cell.class);
        Cell dataAmountCell = mock(Cell.class);
        Cell dataDescriptionCell = mock(Cell.class);

        when(mockDataRow.getCell(0)).thenReturn(dataDateCell);
        when(mockDataRow.getCell(1)).thenReturn(dataAmountCell);
        when(mockDataRow.getCell(2)).thenReturn(dataDescriptionCell);

        when(dataDateCell.getDateCellValue()).thenReturn(java.sql.Date.valueOf(LocalDate.of(2021, 1, 1)));
        when(dataAmountCell.getNumericCellValue()).thenReturn(100.0);
        when(dataDescriptionCell.getStringCellValue()).thenReturn("Groceries");

        // Call the method under test
        TransactionFile transactionFile = dynamicFileReader.readExcelFile(mockWorkbook);

        // Assertions
        assertEquals(1, transactionFile.getTransactions().size());
        Transaction transaction = transactionFile.getTransactions().get(0);
        assertEquals(LocalDate.of(2021, 1, 1), transaction.getDate());
        assertEquals(100.0, transaction.getAmount());
        assertEquals("Groceries", transaction.getDescription());
    }
}
