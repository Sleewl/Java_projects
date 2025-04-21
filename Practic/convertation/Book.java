package convertation;

public class Book {
    private String id;
    private String author;
    private String name;
    private String year;
    private double price;

    public Book(String id, String author, String name, String year, int price) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.year = year;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public int getPrice() {
        return (int) price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", price=" + price +
                '}';
    }
}
