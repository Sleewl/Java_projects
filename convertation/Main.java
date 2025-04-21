package convertation;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;


public class Main {
    private static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        try {
            loadBooksFromXml("src/books.xml");
            System.out.println("Данные успешно загружены из XML.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        displayMenu();
    }

    private static void loadBooksFromXml(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filePath));
        doc.getDocumentElement().normalize();

        NodeList bookNodes = doc.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);

            String id = bookElement.getAttribute("id");
            String author = getTextContent(bookElement, "author");
            String name = getTextContent(bookElement, "name");
            String year = getTextContent(bookElement, "year");
            String priceStr = getTextContent(bookElement, "price");
            int price = priceStr.isEmpty() ? 0 : Integer.parseInt(priceStr);

            books.add(new Book(id, author, name, year, price));
        }
    }

    private static String getTextContent(Element element, String tagName) {
        Node node = element.getElementsByTagName(tagName).item(0);
        return node != null ? node.getTextContent().trim() : "";
    }

    private static void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Вывести всех авторов");
            System.out.println("2. Вывести все книги");
            System.out.println("3. Найти книги по автору");
            System.out.println("4. Найти книгу по названию");
            System.out.println("5. Найти книги по году издания");
            System.out.println("6. Создать новую книгу");
            System.out.println("7. Удалить книгу");
            System.out.println("8. Редактировать книгу");
            System.out.println("9. Экспортировать в JSON");
            System.out.println("10. Экспортировать в XML");
            System.out.println("11. Экспортировать в CSV");
            System.out.println("12. Выход");


            int choice = -1;
            boolean validChoice = false;
            while (!validChoice) {
                try {
                    String input = scanner.nextLine();
                    choice = Integer.parseInt(input);
                    if (choice < 1 || choice > 12) {
                        System.out.println("Ошибка: Введите число от 1 до 12!");
                    } else {
                        validChoice = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: Введите число от 1 до 12!");
                }
            }
            switch (choice) {
                case 1 -> displayAuthors();
                case 2 -> displayAllBooks();
                case 3 -> {
                    System.out.println("Введите имя автора:");
                    String authorName = scanner.nextLine();
                    displayBooksByAuthor(authorName);
                }
                case 4 -> {
                    System.out.println("Введите название книги:");
                    String bookName = scanner.nextLine();
                    displayBookByName(bookName);
                }
                case 5 -> {
                    System.out.println("Введите год издания:");
                    String year = scanner.nextLine();
                    displayBooksByYear(year);
                }
                case 6 -> createBook();
                case 7 -> deleteBook();
                case 8 -> editBook();
                case 9 -> exportToJson();
                case 10 -> exportToXml();
                case 11 -> exportToCsv();
                case 12 -> {
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                }
            }
//                default -> System.out.println("Неверный выбор. Пожалуйста, выберите от 1 до 12.");
        }

    }


    private static void displayAuthors() {
        System.out.println("Авторы:");
        books.stream()
                .map(Book::getAuthor)
                .distinct()
                .forEach(System.out::println);
    }

    private static void displayAllBooks() {
        System.out.println("Список всех книг:");
        books.forEach(System.out::println);
    }

    private static void displayBooksByAuthor(String authorName) {
        books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(authorName))
                .forEach(System.out::println);
    }

    private static void displayBookByName(String bookName) {
        books.stream()
                .filter(book -> book.getName().equalsIgnoreCase(bookName))
                .findFirst()
                .ifPresent(System.out::println);
    }

    private static void displayBooksByYear(String year) {
        books.stream()
                .filter(book -> book.getYear().equals(year))
                .forEach(System.out::println);
    }

    private static void createBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ID книги:");
        String id = scanner.nextLine();

        System.out.println("Введите автора:");
        String author = scanner.nextLine();

        System.out.println("Введите название:");
        String name = scanner.nextLine();

        System.out.println("Введите год издания:");
        String year = scanner.nextLine();

        System.out.println("Введите цену:");
        int price = scanner.nextInt();

        Book newBook = new Book(id, author, name, year, price);
        books.add(newBook);
        System.out.println("Книга успешно добавлена: " + newBook);
    }

    private static void deleteBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ID книги для удаления:");
        String id = scanner.nextLine();

        Book bookToRemove = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (bookToRemove == null) {
            System.out.println("Книга с таким ID не найдена.");
        } else {
            books.remove(bookToRemove);
            System.out.println("Книга успешно удалена: " + bookToRemove);
        }
    }

    private static void editBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ID книги для редактирования:");
        String id = scanner.nextLine();

        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (book == null) {
            System.out.println("Книга с таким ID не найдена.");
            return;
        }

        System.out.println("Что вы хотите изменить?");
        System.out.println("1. Автор");
        System.out.println("2. Название");
        System.out.println("3. Год издания");
        System.out.println("4. Цена");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Введите нового автора:");
                book.setAuthor(scanner.nextLine());
            }
            case 2 -> {
                System.out.println("Введите новое название:");
                book.setName(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("Введите новый год издания:");
                book.setYear(scanner.nextLine());
            }
            case 4 -> {
                System.out.println("Введите новую цену:");
                book.setPrice(scanner.nextInt());
            }
            default -> System.out.println("Неверный выбор.");
        }

        System.out.println("Книга успешно обновлена: " + book);
    }

    private static void exportToJson() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.json"))) {
            writer.write("{\n  \"books\": [\n");
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                writer.write("    {\n");
                writer.write("      \"id\": \"" + book.getId() + "\",\n");
                writer.write("      \"author\": \"" + book.getAuthor() + "\",\n");
                writer.write("      \"name\": \"" + book.getName() + "\",\n");
                writer.write("      \"year\": \"" + book.getYear() + "\",\n");
                writer.write("      \"price\": " + book.getPrice() + "\n");
                writer.write("    }" + (i < books.size() - 1 ? "," : "") + "\n");
            }
            writer.write("  ]\n}");
            System.out.println("Данные успешно экспортированы в books.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportToXml() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("books");
            doc.appendChild(rootElement);

            for (Book book : books) {
                Element bookElement = doc.createElement("book");
                bookElement.setAttribute("id", book.getId());

                appendChildElement(doc, bookElement, "author", book.getAuthor());
                appendChildElement(doc, bookElement, "name", book.getName());
                appendChildElement(doc, bookElement, "year", book.getYear());
                appendChildElement(doc, bookElement, "price", String.valueOf(book.getPrice()));

                rootElement.appendChild(bookElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("books.xml"));
            transformer.transform(source, result);

            System.out.println("Данные успешно экспортированы в books.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exportToCsv() {
        try (PrintWriter writer = new PrintWriter("books.csv")) {
            writer.println("id,author,name,year,price");
            for (Book book : books) {
                writer.printf("%s,%s,%s,%s,%.2f%n",
                        book.getId(),
                        book.getAuthor(),
                        book.getName(),
                        book.getYear(),
                        book.getPrice());
            }
            System.out.println("Данные успешно экспортированы в books.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendChildElement(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent));
        parent.appendChild(element);
    }
}
