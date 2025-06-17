
import org.example.JsonDataWriter;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class JsonDataWriterTest {
    private static final String TEST_FILE = "test_output.json";
    private final JsonDataWriter writer = new JsonDataWriter(TEST_FILE);

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