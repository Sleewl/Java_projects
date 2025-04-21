package Annotation;

import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SaveData {

    private static final Logger logger = Logger.getLogger(SaveData.class.getName());
    private static final Random random = new Random();

    static {
        try {
            FileHandler fileHandler = new FileHandler("logfile.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(Data data, String filePath, String mode) throws IOException, IllegalAccessException {
        boolean append = mode.equals("-a");
        try (FileWriter writer = new FileWriter(filePath, append)) {

            Field[] fields = data.getClass().getFields();

            writer.write("{\n");

            boolean firstField = true;

            for (Field field : fields) {
                if (field.isAnnotationPresent(Ann.Ok.class)) {
                    if (!firstField) writer.write(",\n");
                    writer.write("  \"" + field.getName() + "\": " + field.get(data));
                    firstField = false;
                } else if (field.isAnnotationPresent(Ann.Ugly.class)) {
                    Ann.Ugly ugly = field.getAnnotation(Ann.Ugly.class);
                    int k = ugly.k();
                    int randomValue = random.nextInt(2 * k + 1) - k;
                    if (!firstField) writer.write(",\n");
                    writer.write("  \"" + field.getName() + "\": " + randomValue);
                    firstField = false;
                } else {
                    logger.info("Не аннотированное поле: " + field.getName() + ", значение: " + field.get(data));
                }
            }

            writer.write("\n}");
        }
    }
}


