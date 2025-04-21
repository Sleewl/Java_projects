import java.io.*;
import java.nio.file.*;
import java.util.logging.*;

public class ChessGameReader {

    private static final Logger logger = Logger.getLogger(ChessGameReader.class.getName());

    public static void main(String[] args) {
        String filePath = "chess_game.txt";
        String logFilePath = "error_log.txt";

        setupLogger(logFilePath);

        try {
            readChessGameFromFile(filePath);
        } catch (IOException e) {
            logger.severe("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static void setupLogger(String logFilePath) {
        try {
            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Ошибка при настройке логгера: " + e.getMessage());
        }
    }

    private static void readChessGameFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Файл не найден: " + filePath);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            StringBuilder player1Moves = new StringBuilder();
            StringBuilder player2Moves = new StringBuilder();
            String result = null;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.matches("^[0-9]-[0-9]$")) {
                    result = line;
                    break;
                }

                String[] moves = line.split("\\s+");
                if (moves.length >= 3 && moves[0].matches("\\d+\\.")) {
                    player1Moves.append(moves[1]).append(" ");
                    player2Moves.append(moves[2]).append(" ");
                } else {
                    logger.warning("Некорректная строка в файле: " + line);
                }
            }

            if (result == null) {
                logger.warning("Результат партии не найден.");
            }
            
            System.out.println("Игрок 1: " + player1Moves.toString().trim() +
                    " - Игрок 2: " + player2Moves.toString().trim());
            System.out.println("Результат: " + (result != null ? result : "Результат не указан"));

        } catch (IOException e) {
            logger.severe("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
