import dev.shingi.models.FileConfig;
import dev.shingi.models.FileConfigManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileConfigManagerTest {
    private FileConfigManager fileConfigManager;
    private FileConfig mockFileConfig;
    private FileSystem jimfs;
    private Path testPath;

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
        // Initialize Jimfs
        jimfs = Jimfs.newFileSystem(Configuration.unix());
        testPath = jimfs.getPath("/testConfigs.json");
    }

    @Test
    public void testAddAndRemoveConfig() {
        fileConfigManager.addConfig(mockFileConfig);
        assertEquals(mockFileConfig.getSourceName(), fileConfigManager.getConfig("TestSource").getSourceName());

        fileConfigManager.removeConfig("TestSource");
        assertNull(fileConfigManager.getConfig("TestSource"));
    }

    @Test
    public void testSaveAndLoadConfigs() throws IOException {
        fileConfigManager.addConfig(mockFileConfig);

        // Save to Jimfs

        // Check file was created
        assertTrue(Files.exists(testPath));

        // Clear the manager and load from Jimfs
        fileConfigManager.removeConfig("TestSource");
        fileConfigManager.loadConfigs(testPath.toString());

        // Check the loaded config
        assertEquals(mockFileConfig.getSourceName(), fileConfigManager.getConfig("TestSource").getSourceName());
    }
}

