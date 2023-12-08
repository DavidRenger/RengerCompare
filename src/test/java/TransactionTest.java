import dev.shingi.models.FileConfigManager;
import dev.shingi.models.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TransactionTest {
    private FileConfigManager mockFileConfigManager;

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;
    private Transaction transaction4;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mock objects

        when(mockFileConfigManager.DATE_FORMAT).thenReturn("NUMERIC");
        when(mockFileConfigManager.AMOUNT_FORMAT).thenReturn("NUMERIC");
        when(mockFileConfigManager.DESCRIPTION_FORMAT).thenReturn("STRING");
        
        Map<Object, String> entireRow1 = new HashMap<>();
        entireRow1.put(LocalDate.of(2021, 1, 1), FileConfigManager.getInstance().DATE_FORMAT);
        entireRow1.put(100.0, FileConfigManager.getInstance().AMOUNT_FORMAT);
        entireRow1.put("Description1", FileConfigManager.getInstance().DESCRIPTION_FORMAT);

        Map<Object, String> entireRow2 = new HashMap<>();
        entireRow2.put(LocalDate.of(2021, 1, 3), FileConfigManager.getInstance().DATE_FORMAT);
        entireRow2.put(100.0, FileConfigManager.getInstance().AMOUNT_FORMAT);
        entireRow2.put("Description2", FileConfigManager.getInstance().DESCRIPTION_FORMAT);

        Map<Object, String> entireRow3 = new HashMap<>();
        entireRow3.put(LocalDate.of(2021, 2, 1), FileConfigManager.getInstance().DATE_FORMAT);
        entireRow3.put(200.0, FileConfigManager.getInstance().AMOUNT_FORMAT);
        entireRow3.put("Description3", FileConfigManager.getInstance().DESCRIPTION_FORMAT);

        transaction1 = new Transaction(LocalDate.of(2021, 1, 1), 100.0, "Description1", entireRow1, 1);
        transaction2 = new Transaction(LocalDate.of(2021, 1, 3), 100.0, "Description2", entireRow2, 2);
        transaction3 = new Transaction(LocalDate.of(2021, 2, 1), 200.0, "Description3", entireRow3, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(LocalDate.of(2021, 1, 1), transaction1.getDate());
        assertEquals(100.0, transaction1.getAmount());
        assertEquals("Description1", transaction1.getDescription());
    }

    @Test
    public void testEquals() {
        // Test for equality
        assertEquals(transaction1, transaction2);

        // Test for inequality
        assertNotEquals(transaction1, transaction3);

        // Test for inequality beyond 3 days
        assertNotEquals(transaction1, transaction4);
    }

    @Test
    public void testHashCode() {
        // Hash codes should be equal for equal objects
        assertEquals(transaction1.hashCode(), transaction2.hashCode());

        // Hash codes should be different for different objects
        assertNotEquals(transaction1.hashCode(), transaction3.hashCode());
    }
}