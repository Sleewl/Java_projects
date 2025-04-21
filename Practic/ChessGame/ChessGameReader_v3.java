import java.io.*;
import java.util.logging.*;

public class ChessGameReader {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\User\\Desktop\\chess_game.txt";
        String logFilePath = "error_log.txt";

        setupLogger(logFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder player1Moves = new StringBuilder();
            StringBuilder player2Moves = new StringBuilder();
            String result = null;

            for (int i = 0; i < 2; i++) {
                if ((line = reader.readLine()) != null) {
                    String[] moves = line.split(" ");
                    if (moves.length == 3) {
                        player1Moves.append(moves[1]).append(" ");
                        player2Moves.append(moves[2]).append(" ");
                    } else {
                        logWarning("Некорректный формат строки: " + line);
                    }
                } else {
                    logWarning("Файл содержит менее двух строк с ходами.");
                }
            }

            if ((line = reader.readLine()) != null) {
                result = line.trim();
            } else {
                logWarning("Файл не содержит результата.");
            }

            System.out.println("Игрок 1: " + player1Moves.toString().trim() +
                    " - Игрок 2: " + player2Moves.toString().trim());
            System.out.println("Результат: " + (result != null ? result : "Результат не указан"));

        } catch (IOException e) {
            logSevere("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static final Logger logger = Logger.getLogger(ChessGameReader.class.getName());

    private static void setupLogger(String logFilePath) {
        try {
            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Ошибка при настройке логгера: " + e.getMessage());
        }
    }

    private static void logWarning(String message) {
        logger.warning(message);
    }

    private static void logSevere(String message) {
        logger.severe(message);
    }
}
