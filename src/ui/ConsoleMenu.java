package ui;


import exceptions.LibraryException;
import model.Book;
import model.LibraryItem;
import model.Magazine;
import model.Reader;
import service.Library;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ConsoleMenu {

    private final Library library;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(Library library) {
        this.library = library;
    }


    public void run() {
        showMenu();
        while (true) {
            int choice = readInt(scanner, "Введите число, какой пункт хотите выбрать: ");
            try {
                switch (choice) {
                    case 0 -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    case 1 -> showAllItems();
                    case 2 -> addBook();
                    case 3 -> addMagazine();
                    case 4 -> showItemByName();
                    case 5 -> registerReader();
                    case 6 -> showAllReaders();
                    case 7 -> issueItem();
                    case 8 -> returnItem();
                    case 9 -> showReadersItems();
                    case 10 -> groupItemsByName();
                    case 11 -> sortItems();
                    case 12 -> showAvailableItems();
                    case 13 -> partlySearch();
                    case 14 -> showStatistics();
                    case 15 -> deleteReader();
                    default -> throw new IllegalStateException("Такого пункта не существует: " + choice);
                }
            } catch (LibraryException | IllegalStateException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showAllItems() {
        List<LibraryItem> libraryItems = library.getLibraryItems();
        show(libraryItems, "библиотека пуста");
    }

    private void partlySearch() {
        String name = readLine(scanner, "Введите полное название или часть: ");
        List<LibraryItem> items = library.findItems(p -> p.getName().toLowerCase().contains(name.toLowerCase()));
        show(items, "Ничего не найдено. ");
    }

    private void sortItems() {
        int option = readInt(scanner, "Как вы хотите отсортировать издания? \n" +
                "1 - по  id \n" +
                "2 - по названию \n" +
                "3 - сначала свободные \n" +
                "4 - сначала свободные, внутри по названию ");
        Comparator<LibraryItem> comparator = switch (option) {
            case 1 -> Comparator.comparing(LibraryItem::getId);
            case 2 -> Comparator.comparing(LibraryItem::getName);
            case 3 -> Comparator.comparing(LibraryItem::isAvailable).reversed();
            case 4 -> Comparator.comparing(LibraryItem::isAvailable).reversed().thenComparing(LibraryItem::getName);
            default -> throw new IllegalStateException("Такого пункта не существует: " + option);
        };
        List<LibraryItem> items = library.getItemsSorted(comparator);
        show(items, "В библиотеке нет изданий.");
    }

    private void showAvailableItems() {
        List<LibraryItem> availableItems = library.findItems(LibraryItem::isAvailable);
        show(availableItems, "Свободных изданий нет.");

    }

    private void addBook() {
        String name = readLine(scanner, "Введите название книги: ");
        String author = readLine(scanner, "Введите имя автора: ");
        int pages = readInt(scanner, "Введите количество страниц: ");
        Book book = new Book(name, author, pages);
        library.addLibraryItem(book);
        System.out.println("Вы добавили книгу - " + book);
    }

    private void addMagazine() {
        String name = readLine(scanner, "Введите название журнала: ");
        int numIssue = readInt(scanner, "Введите номер выпуска: ");
        Magazine magazine = new Magazine(name, numIssue);
        library.addLibraryItem(magazine);
        System.out.println("Вы добавили журнал - " + magazine);
    }

    private void showItemByName() {
        String name = readLine(scanner, "Введите название издания, которое хотите найти: ");
        LibraryItem item = library.getItemByName(name);
        System.out.println(item.toString());
    }

    private LibraryItem chooseItem(String action,Function<String, LibraryItem> findByTitle) {

        int option = readInt(scanner,
                "Вы хотите " + action + " издание по id или по названию? \n" +
                        "1 - по id \n" +
                        "2 - по названию \n");
        switch (option) {
            case 1 -> {
                int id = readInt(scanner, "Введите id издания, которое хотите " + action + ": ");
                return library.findItemById(id);
            }
            case 2 -> {
                String name = readLine(scanner, "Введите название издания, которое хотите " + action + ": ");
                return findByTitle.apply(name);
            }
            default -> throw new IllegalStateException("Такого пункта не существует: " + option);
        }
    }



    private void returnItem() {
        int readerId = readInt(scanner, "Введите id читателя, который хочет вернуть издание: ");
        Reader reader = library.findReaderById(readerId);
        LibraryItem libraryItem = chooseItem("вернуть", reader::findItemByName);
        library.returnItem(reader, libraryItem);
        System.out.println("Издание " + libraryItem.getName() + " возвращено читателем: " + reader);

    }
    private void issueItem() {
        int readerId = readInt(scanner, "Введите id читателя, которому хотите выдать издание: ");
        Reader reader = library.findReaderById(readerId);
        LibraryItem libraryItem = chooseItem("выдать", library::getItemByName);
        library.issueItem(reader, libraryItem);
        System.out.println("Издание " + libraryItem.getName() + " выдано читателю: " + reader);
    }

    private void showAllReaders() {
        List<Reader> readers = library.getAllReaders();
        show(readers, "Читателей пока нет.");
    }

    private void registerReader() {
        String name = readLine(scanner, "Введите имя читателя, которого хотите зарегистрировать: ");
        Reader reader = new Reader(name);
        library.registerReader(reader);
        System.out.println("Читатель " + name + " с номером " + reader.getId() + " зарегистрирован! ");
    }


    private int readInt(Scanner scanner, String prompt) {

        while (true) {
            System.out.println(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().strip());
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели не целое число/не число вовсе, введите целое число");
            }
        }
    }

    private String readLine(Scanner scanner, String prompt) {
        while(true) {
            System.out.println(prompt);
            String line = scanner.nextLine();
            if (!line.isBlank()) {
                return line.strip();
            } else {
                System.out.println("Строка не может быть пустой.");
            }
        }
    }

    private void groupItemsByName() {
        Map<String, List<LibraryItem>> map = library.groupByName();
        Set<String> allNames = map.keySet();
        if (map.isEmpty()) {
            System.out.println("В библиотеке нет изданий. ");
        }
        for (String name : allNames) {
            List<LibraryItem> list = map.get(name);
            long countAvailable = list.stream()
                    .filter(LibraryItem::isAvailable).count();

            String allId = list.stream()
                    .filter(LibraryItem::isAvailable)
                    .map(libraryItem -> String.valueOf(libraryItem.getId()))
                    .collect(Collectors.joining(", "));
            String ids = allId.isEmpty() ? "" : " (id: " + allId + ")";
            System.out.println(list.getFirst().getName() + ": всего: " + list.size() +
                    " свободно: " + countAvailable + ids);
        }

    }

    private void showStatistics() {
        System.out.println("===== СТАТИСТИКА =====\n" +
                "Всего изданий: " + library.countItems() + "\n" +
                "Выдано сейчас: " + library.countIssuedItems());
        System.out.println("По типам: ");
        Map<String, Long> types = library.countType();
        types.forEach((key, value) -> System.out.println(key + " - " + value + " шт."));
        Optional<Reader> optional = library.findMostActiveReader();
        optional.ifPresentOrElse(reader -> System.out.println("Самый активный читатель: " + reader
                        + " (изданий: " + reader.getItems().size() + ")"),
                () -> System.out.println("Читателей пока нет."));
        System.out.println("Читателей без изданий:");
        List<Reader> readersWithoutItems = library.findReadersWithoutItems();
        if (!readersWithoutItems.isEmpty()) {
            System.out.println(readersWithoutItems.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n")));
        } else {
            System.out.println("Все читатели что-то взяли");
        }
    }

    private void deleteReader() {
        int id = readInt(scanner, "Введите id читателя, которого хотите удалить: ");
        library.deleteReaderById(id);
        System.out.println("Читатель удален. ");
    }



    private void showReadersItems() {
        int id = readInt(scanner, "Введите id читателя, издания которого хотите увидеть: ");
        Reader reader = library.findReaderById(id);
        List<LibraryItem> libraryItems = reader.getItems();
        show(libraryItems, "У читателя пока нет изданий.");
    }

    private <T> void show(List<T> list, String message) {
        if (list.isEmpty()) {
            System.out.println(message);
        } else {
            for (T item : list) {
                System.out.println(item);
            }
        }
    }


    private void showMenu() {
        System.out.println("===== ГОРОДСКАЯ БИБЛИОТЕКА =====\n" +
                "1 - Показать все издания\n" +
                "2 - Добавить книгу\n" +
                "3 - Добавить журнал\n" +
                "4 - Найти издание по названию\n" +
                "5 - Зарегистрировать читателя\n" +
                "6 - Показать всех читателей\n" +
                "7 - Выдать издание\n" +
                "8 - Вернуть издание\n" +
                "9 - Показать издания читателя\n" +
                "10 - Сводка по экземплярам\n" +
                "11 - отсортировать издания\n" +
                "12 - показать свободные\n" +
                "13 - поиск по части названия\n" +
                "14 - вывести статистику\n" +
                "15 - удалить читателя\n" +
                "0 - Выход");
    }
}
