package Test;

import model.Book;
import model.LibraryItem;
import model.Magazine;


import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Task4 {

    TreeSet<LibraryItem> set;

    public Task4(TreeSet<LibraryItem> set) {
        this.set = set;

    }


    public void addItems() {
        set.add(new Book("Война и мир", "Лев Толстой", 1300));
        set.add(new Book("Преступление и наказание", "Фёдор Достоевский", 672));
        set.add(new Book("Мастер и Маргарита", "Михаил Булгаков", 480));
        set.add(new Book("Мастер и Маргарита", "Михаил Булгаков", 480));
        set.add(new Book("Евгений Онегин", "Александр Пушкин", 288));
        set.add(new Book("Евгений Онегин", "Александр Пушкин", 288));
        set.add(new Book("Чистый код", "Роберт Мартин", 464));
        set.add(new Book("Head First Java", "Кэти Сьерра, Берт Бейтс", 720));
        set.add(new Magazine("Наука и жизнь", 5));
        set.add(new Magazine("Наука и жизнь", 6));
        set.add(new Magazine("Хакер", 12));
        set.add(new Magazine("Вокруг света", 3));

        //3, 9, 5
    }
}
