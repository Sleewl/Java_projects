import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.read.ListAppender;
import org.example.Main;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> listAppender;
    private Logger mainLogger;

    @BeforeEach
    void setup() {
        // Захватываем Logback-логгер класса Main
        mainLogger = (Logger) LoggerFactory.getLogger(Main.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        mainLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        mainLogger.detachAppender(listAppender);
        listAppender.stop();
    }

    @Test
    void insufficientArguments_shouldLogErrorAndExit() {
        Main.main(new String[] { "1", "1", "json" });  // меньше 4 аргументов

        List<ch.qos.logback.classic.spi.ILoggingEvent> logs = listAppender.list;
        assertFalse(logs.isEmpty(), "Ожидаем хотя бы одну запись в логах");

        boolean hasError = logs.stream()
                .anyMatch(evt ->
                        evt.getLevel() == Level.ERROR
                                && evt.getFormattedMessage().contains("Invalid arguments"));
        assertTrue(hasError, "Должно быть сообщение ERROR с текстом 'Invalid arguments'");
    }

    @Test
    void invalidFormat_shouldLogErrorAndExit() {
        Main.main(new String[] { "1", "1", "xml", "news" });  // формат 'xml' не поддерживается

        List<ch.qos.logback.classic.spi.ILoggingEvent> logs = listAppender.list;
        assertFalse(logs.isEmpty(), "Ожидаем хотя бы одну запись в логах");

        boolean hasError = logs.stream()
                .anyMatch(evt ->
                        evt.getLevel() == Level.ERROR
                                && evt.getFormattedMessage().contains("Invalid output format"));
        assertTrue(hasError, "Должно быть сообщение ERROR с текстом 'Invalid output format'");
    }

    @Test
    void unknownApi_shouldLogWarningAndExit() {
        Main.main(new String[] { "1", "1", "json", "foobar" });

        List<ch.qos.logback.classic.spi.ILoggingEvent> logs = listAppender.list;
        assertFalse(logs.isEmpty(), "Ожидаем хотя бы одну запись в логах");

        boolean hasWarn = logs.stream()
                .anyMatch(evt ->
                        evt.getLevel() == Level.WARN
                                && evt.getFormattedMessage().contains("Unknown API"));
        assertTrue(hasWarn, "Должно быть сообщение WARN с текстом 'Unknown API'");
    }
}
