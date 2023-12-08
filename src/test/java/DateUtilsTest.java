import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.shingi.utils.DateUtils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateUtilsTest {

    private Sheet mockSheet;
    private Row mockRow;
    private Cell mockCell;

    @BeforeEach
    public void setUp() {
        mockSheet = mock(Sheet.class);
        mockRow = mock(Row.class);
        mockCell = mock(Cell.class);
    }

    @Test
    public void testToLocalDate() {
        // Create a sample Date object (January 1, 2023)
        Calendar calendar = new GregorianCalendar(2023, 0, 1);
        Date testDate = calendar.getTime();

        // Convert it to LocalDate
        LocalDate localDate = DateUtils.toLocalDate(testDate);

        // Assert that the converted LocalDate is as expected
        assertEquals(LocalDate.of(2023, 1, 1), localDate);
    }

    @Test
    public void testToDate() {
        // Create a sample Date object (January 1, 2023)
        Calendar calendar = new GregorianCalendar(2023, Calendar.JANUARY, 1);
        Date date = calendar.getTime();

        // Create a sample LocalDate object (January 1, 2023)
        LocalDate localDate = LocalDate.of(2023, 1, 1);

        // Convert it to Date
        Date testDate = DateUtils.toDate(localDate);

        // Assert that the converted Date is as expected
        assertEquals(date, testDate);
    }

    @Test
    public void testDetectDateFormat() {
        // Arrange
        when(mockSheet.getRow(anyInt())).thenReturn(mockRow);
        when(mockRow.getCell(anyInt())).thenReturn(mockCell);

        // Assuming the first date format in the list of COMMON_DATE_FORMATS is "yyyy-MM-dd"
        when(mockCell.toString()).thenReturn("2022-09-25");

        // Act
        String detectedFormat = DateUtils.detectDateFormat(mockSheet, 0, 0);

        // Assert
        assertEquals("yyyy-MM-dd", detectedFormat);
    }

    @Test
    public void testNoValidDateFormat() {
        // Arrange
        when(mockSheet.getRow(anyInt())).thenReturn(mockRow);
        when(mockRow.getCell(anyInt())).thenReturn(mockCell);

        // Set an unlikely date format
        when(mockCell.toString()).thenReturn("25*09*2022");

        // Act
        String detectedFormat = DateUtils.detectDateFormat(mockSheet, 0, 0);

        // Assert
        assertEquals(null, detectedFormat);
    }
}
