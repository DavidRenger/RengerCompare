import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.shingi.models.FileConfig;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileConfigTest {

    private FileConfig mockFileConfig;

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
    }

    @Test
    public void testGetAllHeaderNames() {
        List<String> headerNames = mockFileConfig.getAllHeaderNames();

        // Test for the existence of mandatory headers
        assertTrue(headerNames.contains("Date"));
        assertTrue(headerNames.contains("Amount"));
        assertTrue(headerNames.contains("Description"));

        // Test for the existence of optional headers (since hasSeparateCreditDebit is set to true)
        assertTrue(headerNames.contains("Credit"));
        assertTrue(headerNames.contains("Debit"));
    }

    @Test
    public void testGettersAndSetters() {
        // Test getSourceName
        assertEquals("TestBank", mockFileConfig.getSourceName());

        // Test getDateHeaderName
        assertEquals("Date", mockFileConfig.getDateHeaderName());

        // Test getAmountHeaderName
        assertEquals("Amount", mockFileConfig.getAmountHeaderName());

        // Test getDescriptionHeaderName
        assertEquals("Description", mockFileConfig.getDescriptionHeaderName());

        // Test getDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        assertEquals(sdf, mockFileConfig.getDateFormat());

        // Test hasSeparateCreditDebit
        assertEquals(true, mockFileConfig.hasSeparateCreditDebit());

        // Test getCreditHeaderName
        assertEquals("Credit", mockFileConfig.getDebetOrCreditHeaderName());

    }

    @Test
    public void testDateFormatSetter() {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mockFileConfig.setDateFormat(newDateFormat);
        assertEquals(newDateFormat, mockFileConfig.getDateFormat());
    }
}
