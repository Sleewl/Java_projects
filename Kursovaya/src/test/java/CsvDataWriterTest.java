import org.example.CsvDataWriter;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CsvDataWriterTest {
    private static final String TEST_FILE = "test_output.csv";
    private final CsvDataWriter writer = new CsvDataWriter(TEST_FILE);

    @AfterEach
    void cleanup() {
        new File(TEST_FILE).delete();
    }

    @Test
    void appendData_shouldCreateValidFile() throws IOException {
        writer.appendData(Map.of("test", "value"));
        assertTrue(new File(TEST_FILE).exists());
    }
}