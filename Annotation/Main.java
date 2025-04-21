package Annotation;

public class Main {
    public static void main(String[] args) {
        Data data = new Data(10, 20, 30, 40, 52);

        try {
            String filePath = "data.json";
            String mode = args.length > 0 ? args[0] : "-r";

            SaveData.save(data, filePath, mode);

            System.out.println("Данные успешно сохранены в файл.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
