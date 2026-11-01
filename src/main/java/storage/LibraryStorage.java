package storage;

import exceptions.LibraryException;
import model.Book;
import model.LibraryItem;
import model.Magazine;
import model.Reader;
import service.Library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LibraryStorage {

    private final List<String> warnings = new ArrayList<>();


    public void save(Library library, Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
        saveItems(library, path);
        saveReaders(library, path);
        saveLoans(library, path);
    }


    public Library load(Path path) throws IOException {
        warnings.clear();
        Library library = new Library();
        loadItems(library, path);
        loadReaders(library, path);
        loadLoans(library, path);
        return library;
    }


    private void saveItems(Library library, Path path) throws IOException {
        Path filePath = path.resolve("items.csv");
        List<String> savedItems = library.getLibraryItems().stream()
                .map(LibraryItem::convertToCSV).toList();
        Files.write(filePath, savedItems);
    }


    private void loadItems(Library library, Path path) throws IOException {
        loadFile(path, "items.csv", parts -> {
            String type = parts[0];
            switch (type.toLowerCase()) {
                case "book" -> {
                    if (parts.length != 5) {
                        throw new IllegalArgumentException("В строке неправильное количество полей. Ожидалось 5, получено: " + parts.length);
                    }
                    Book book = new Book(parseNumber(parts[1], "id книги"), parts[2],
                            parts[3], parseNumber(parts[4], "количество страниц"));
                    library.addLibraryItem(book);
                }
                case "magazine" -> {
                    if (parts.length != 4) {
                        throw new IllegalArgumentException("В строке неправильное количество полей. Ожидалось 4, получено: " + parts.length);
                    }
                    Magazine magazine = new Magazine(parseNumber(parts[1], "id журнала"), parts[2],
                            parseNumber(parts[3], "номер журнала"));
                    library.addLibraryItem(magazine);
                }
                default -> throw new IllegalArgumentException("Неизвестный тип издания " + parts[0]);
            }
        });
    }

    private void saveReaders(Library library, Path path) throws IOException {
        Path filePath = path.resolve("readers.csv");
        List<String> savedReaders = library.getAllReaders().stream().map(Reader::convertToCSV).toList();
        Files.write(filePath, savedReaders);

    }


    private void loadReaders(Library library, Path path) throws IOException {
        loadFile(path, "readers.csv", parts -> {
            if (parts.length != 2) {
                throw new IllegalArgumentException("В строке неправильное количество полей. Ожидалось 2, получено: " + parts.length);
            }
            library.registerReader(new Reader(parseNumber(parts[0], "id читателя"), parts[1]));
        });
    }

    private void loadFile(Path folder, String fileName, Consumer<String[]> action) throws IOException {
        Path file = folder.resolve(fileName);
        if (Files.notExists(file)) {
            return;
        }
        List<String> loadedList = Files.readAllLines(file);
        for (int i = 0; i < loadedList.size(); i++) {
            String loaded = loadedList.get(i);
            if (loaded.isBlank()) {
                continue;
            }
            try {
                String[] parts = loaded.split(";");
                action.accept(parts);
            } catch (IllegalArgumentException | LibraryException e) {
                warnings.add("Предупреждение: " + fileName + ", строка " + (i + 1) + ": " + e.getMessage());
            }
        }
    }

    private void saveLoans(Library library, Path path) throws IOException {
        Path filePath = path.resolve("loans.csv");
        List<Reader> readers = library.getAllReaders();

        List<String> savedLoans = readers.stream().flatMap(reader -> reader.getItems()
                        .stream().map(item -> reader.getId() + ";" + item.getId()))
                .toList();
        Files.write(filePath, savedLoans);


    }

    private void loadLoans(Library library, Path path) throws IOException {
        loadFile(path, "loans.csv", parts -> {
            if (parts.length != 2) {
                throw new IllegalArgumentException("В строке неправильное количество полей. Ожидалось - 2, получено: " + parts.length);
            }
            Reader reader = library.findReaderById(parseNumber(parts[0], "id читателя"));
            LibraryItem item = library.findItemById(parseNumber(parts[1], "id издания"));
            library.issueItem(reader, item);
        });
    }

    private int parseNumber(String number, String fieldName) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + fieldName
                    + " должно быть числом, получено: " + number);
        }
    }

    public List<String> getWarnings() {
        return List.copyOf(warnings);
    }
}




