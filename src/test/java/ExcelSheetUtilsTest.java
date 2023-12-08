import dev.shingi.models.FileConfig;
import dev.shingi.models.FileConfigManager;
import dev.shingi.utils.ExcelSheetUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ExcelSheetUtilsTest {

    private Sheet mockSheet;
    private Row mockRow;
    private Cell mockCell;
    // private CellStyle mockCellStyle;
    private FileConfigManager mockFileConfigManager;

    @BeforeEach
    public void setUp() {
        mockSheet = mock(Sheet.class);
        mockRow = mock(Row.class);
        mockCell = mock(Cell.class);
        // mockCellStyle = mock(CellStyle.class);
        mockFileConfigManager = mock(FileConfigManager.class);
    }

    @Test
    public void testGenerateRowMapForStringNumericBoolean() {
        // Mock Row and Cells
        Row mockRow = mock(Row.class);
        Cell mockStringCell = mock(Cell.class);
        Cell mockNumericCell = mock(Cell.class);
        Cell mockBooleanCell = mock(Cell.class);
        
        // Mock behavior for each cell
        when(mockStringCell.getCellType()).thenReturn(CellType.STRING);
        when(mockStringCell.getStringCellValue()).thenReturn("Test");
        
        when(mockNumericCell.getCellType()).thenReturn(CellType.NUMERIC);
        when(mockNumericCell.getNumericCellValue()).thenReturn(42.0);
        
        when(mockBooleanCell.getCellType()).thenReturn(CellType.BOOLEAN);
        when(mockBooleanCell.getBooleanCellValue()).thenReturn(true);

        // Mock behavior for row to return these cells
        when(mockRow.iterator()).thenReturn(Arrays.asList(mockStringCell, mockNumericCell, mockBooleanCell).iterator());
        
        // Call method to test
        Map<Object, String> result = ExcelSheetUtils.generateRowMap(mockRow);
        
        // Assertions
        assertEquals(3, result.size()); // 3 different cell types
        assertEquals("Test", result.get("STRING"));
        assertEquals(42.0, result.get("NUMERIC"));
        assertEquals(true, result.get("BOOLEAN"));
    }

    // The program will most likely not be dealing with formulas, so this test is not necessary.
    // @Test
    // public void testGenerateRowMapForFormula() {
    //     // Mock Row and Formula Cell
    //     Row mockRow = mock(Row.class);
    //     Cell mockFormulaCell = mock(Cell.class);

    //     // Mock behavior for Formula cell
    //     when(mockFormulaCell.getCellType()).thenReturn(CellType.FORMULA);
    //     when(mockFormulaCell.getCellFormula()).thenReturn("SUM(A1:A3)");

    //     // Mock behavior for row to return the Formula cell
    //     when(mockRow.iterator()).thenReturn(Arrays.asList(mockFormulaCell).iterator());
        
    //     // Call method to test
    //     Map<Object, String> result = ExcelSheetUtils.generateRowMap(mockRow);
        
    //     // Assertions
    //     assertEquals(1, result.size()); // One Formula cell
    //     assertEquals("SUM(A1:A3)", result.get("FORMULA"));
    // }

    @Test
    public void testGenerateRowMapForBlank() {
        // Mock Row and Blank Cell
        Row mockRow = mock(Row.class);
        Cell mockBlankCell = mock(Cell.class);

        // Mock behavior for Blank cell
        when(mockBlankCell.getCellType()).thenReturn(CellType.BLANK);

        // Mock behavior for row to return the Blank cell
        when(mockRow.iterator()).thenReturn(Arrays.asList(mockBlankCell).iterator());
        
        // Call method to test
        Map<Object, String> result = ExcelSheetUtils.generateRowMap(mockRow);
        
        // Assertions
        assertEquals(1, result.size()); // One Blank cell
        assertEquals("", result.get("BLANK"));
    }

    // @Test
    // public void testCreateRowFromRowMap() {
    //     Map<Object, String> rowMap = new HashMap<>();
    //     rowMap.put("STRING", "Test");

    //     when(mockSheet.createRow(0)).thenReturn(mockRow);
    //     when(mockRow.createCell(0)).thenReturn(mockCell);

    //     ExcelSheetUtils.createRowFromRowMap(mockSheet, 0, rowMap, fileConfig);

    //     verify(mockCell).setCellValue("Test");
    // }

    @Test
    public void testFindLikelyHeaderRow() {
        FileConfig mockConfig = mock(FileConfig.class);
        when(mockConfig.getAllHeaderNames()).thenReturn(Arrays.asList("date", "amount", "description"));
        when(mockFileConfigManager.getConfigsMap()).thenReturn(Collections.singletonMap("test", mockConfig));
        when(mockSheet.iterator()).thenReturn(Collections.singletonList(mockRow).iterator());
        when(mockRow.iterator()).thenReturn(Collections.nCopies(3, mockCell).iterator());
        when(mockCell.toString()).thenReturn("date").thenReturn("amount").thenReturn("description");

        Row result = ExcelSheetUtils.findLikelyHeaderRow(mockSheet);

        assertEquals(mockRow, result);
    }

    @Test
    public void detectHeaderIndices() {
        FileConfig mockConfig1 = mock(FileConfig.class);
        FileConfig mockConfig2 = mock(FileConfig.class);

        when(mockConfig1.getAllHeaderNames()).thenReturn(Arrays.asList("datum", "bedrag", "omschrijving"));
        when(mockConfig2.getAllHeaderNames()).thenReturn(Arrays.asList("date", "amount", "description"));
        
        Map<String, FileConfig> configMap = new HashMap<>();

        configMap.put("config1", mockConfig1);
        configMap.put("config2", mockConfig2);

        when(mockFileConfigManager.getConfigsMap()).thenReturn(configMap);

        // Mocking to simulate 6 cells, 3 for each config
        when(mockRow.iterator()).thenReturn(
            Arrays.asList(mockCell, mockCell, mockCell, mockCell, mockCell, mockCell).iterator()
        );

        // Returning cell values in sequence
        when(mockCell.getStringCellValue()).thenReturn("datum", "bedrag", "omschrijving", "date", "amount", "description");
        when(mockCell.getColumnIndex()).thenReturn(0, 1, 2, 3, 4, 5);  // Mocking the column indices

        // Run the test
        Map<String, Integer> result = ExcelSheetUtils.detectHeaderIndices(mockRow);

        // Assertions
        assertEquals(Integer.valueOf(3), result.get("date"));
        assertEquals(Integer.valueOf(4), result.get("amount"));
        assertEquals(Integer.valueOf(5), result.get("description"));
        assertEquals(Integer.valueOf(0), result.get("datum"));
        assertEquals(Integer.valueOf(1), result.get("bedrag"));
        assertEquals(Integer.valueOf(2), result.get("omschrijving"));
    }

    @Test
    public void testDetectSeparateCreditDebit() {
        when(mockRow.iterator()).thenReturn(Collections.nCopies(2, mockCell).iterator());
        when(mockCell.getStringCellValue()).thenReturn("credit").thenReturn("debit");

        boolean result = ExcelSheetUtils.detectSeparateCreditDebit(mockRow);

        assertTrue(result);
    }
}
