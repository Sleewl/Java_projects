package Reflection;
import java.lang.reflect.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main{
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        MyClass myObject = new MyClass();
        try {
            Method[] methods = MyClass.class.getDeclaredMethods();
            for(Method method : methods){
                if (Modifier.isPrivate(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
                    method.setAccessible(true);
                    method.invoke(myObject);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.severe(e.getMessage());
        }
    }
    static {
        try {
            FileHandler fileHandler = new FileHandler("error_log.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.severe("Ошибка при настройке логгера: " + e.getMessage());
        }
    }
}
