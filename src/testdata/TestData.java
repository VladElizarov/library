package testdata;

import model.Book;
import model.Magazine;
import model.Reader;
import service.Library;

public class TestData {


    public static Library createLibrary() {
        Library library = new Library();

        library.addLibraryItem(new Book("Война и мир", "Лев Толстой", 1300));
        library.addLibraryItem(new Book("Преступление и наказание", "Фёдор Достоевский", 672));
        library.addLibraryItem(new Book("Мастер и Маргарита", "Михаил Булгаков", 480));
        library.addLibraryItem(new Book("Мастер и Маргарита", "Михаил Булгаков", 480));
        library.addLibraryItem(new Book("Евгений Онегин", "Александр Пушкин", 288));
        library.addLibraryItem(new Book("Евгений Онегин", "Александр Пушкин", 288));
        library.addLibraryItem(new Book("Чистый код", "Роберт Мартин", 464));
        library.addLibraryItem(new Book("Head First Java", "Кэти Сьерра, Берт Бейтс", 720));
        library.addLibraryItem(new Magazine("Наука и жизнь", 5));
        library.addLibraryItem(new Magazine("Наука и жизнь", 6));
        library.addLibraryItem(new Magazine("Хакер", 12));
        library.addLibraryItem(new Magazine("Вокруг света", 3));

        library.registerReader(new Reader("Иван Петров"));
        library.registerReader(new Reader("Мария Сидорова"));
        library.registerReader(new Reader("Иван Петров"));
        library.registerReader(new Reader("Алексей Смирнов"));

        library.issueItem(library.findReaderById(0), library.findItemById(0));
        library.issueItem(library.findReaderById(1), library.findItemById(2));
        library.issueItem(library.findReaderById(1), library.findItemById(10));
        library.issueItem(library.findReaderById(1), library.findItemById(6));

        return library;
    }
}
