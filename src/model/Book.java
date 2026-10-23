package model;

public class Book extends LibraryItem {
    private final String author;
    private final int pages;
    private static final int LOAN_DAYS = 14;

    public Book(String name, String author, int pages) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Имя автора не может быть пустым. ");
        }
        if (pages <= 0) {
            throw new IllegalArgumentException("Количество страниц должно быть больше 0, получено: " + pages);
        }
        super(name);
        this.author = author;
        this.pages = pages;
    }
    public Book(int id, String name, String author, int pages) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Имя автора не может быть пустым. ");
        }
        if (pages <= 0) {
            throw new IllegalArgumentException("Количество страниц должно быть больше 0, получено: " + pages);
        }
        super(id, name);
        this.author = author;
        this.pages = pages;
    }

    @Override
    public String convertToCSV() {
        return "Book;" + super.convertToCSV() + ";" + author + ";" + pages;
    }

    @Override
    public int getLoanDays() {
        return LOAN_DAYS;
    }

    @Override
    public String toString() {
        return super.toString() + " - книга под авторством: " + author + ", количество страниц: " + pages;
    }

    @Override
    public String getTypeName() {
        return "Книга";
    }
}
