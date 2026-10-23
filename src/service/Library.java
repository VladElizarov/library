package service;

import exceptions.ItemNotFoundException;
import exceptions.LimitExceededException;
import exceptions.ReaderNotFoundException;
import model.LibraryItem;
import model.Reader;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

public class Library {


    private final List<LibraryItem> libraryItems = new ArrayList<>();
    private final Map<Integer, Reader> readers = new LinkedHashMap<>();


    public void addLibraryItem(LibraryItem libraryItem) {
        if (libraryItem == null) {
            throw new IllegalArgumentException("Аргумент не должен быть null");
        }
        libraryItems.add(libraryItem);
    }


    public List<LibraryItem> getLibraryItems() {
        return List.copyOf(libraryItems);
    }


    public LibraryItem getItemByName(String name) {
        return libraryItems.stream()
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .min(Comparator.comparing(x -> !x.isAvailable())).orElseThrow(
                        () -> new ItemNotFoundException("Не удалось найти издание с именем " + name));
    }

    public void issueItem(Reader reader, LibraryItem item) {
        if (item == null || reader == null) {
            throw new IllegalArgumentException("Аргумент не должен быть null");
        }
        if (!reader.canTakeMore()) {
            throw new LimitExceededException("Читатель достиг лимита взятых изданий. ");
        }
        item.markIssued();
        reader.addItem(item);
    }

    public void returnItem(Reader reader, LibraryItem item) {
        if (item == null || reader == null) {
            throw new IllegalArgumentException("Аргумент не должен быть null");
        }
        reader.removeItem(item);
        item.markReturned();
    }

    public void registerReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Аргумент не должен быть null");
        }

        readers.put(reader.getId(), reader);

    }

    public Reader findReaderById(int id) {
        Reader reader = readers.get(id);
        if (reader == null) {
            throw new ReaderNotFoundException("Не существует пользователя с id: " + id);
        }
        return reader;

    }

    public LibraryItem findItemById(int id) {
        return libraryItems.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow(() -> new ItemNotFoundException("Не существует издания с id: " + id));
    }

    public List<Reader> getAllReaders() {
        return List.copyOf(readers.values());
    }


    public Map<String, List<LibraryItem>> groupByName() {
        return libraryItems.stream().collect(Collectors.groupingBy(libraryItem -> libraryItem
                .getName()
                .toLowerCase(), TreeMap::new, toList()));

    }

    public List<LibraryItem> getItemsSorted(Comparator<LibraryItem> comparator) {
        return libraryItems.stream()
                .sorted(comparator)
                .toList();
    }

    public int countItems() {
        return libraryItems.size();
    }

    public long countIssuedItems() {
        return libraryItems.stream()
                .filter(libraryItem -> !libraryItem.isAvailable())
                .count();
    }

    public Map<String, Long> countType() {
        return libraryItems.stream()
                .collect(Collectors.groupingBy(
                        LibraryItem::getTypeName, counting()));
    }

    public Optional<Reader> findMostActiveReader() {
        return readers.values().stream()
                .max(Comparator.comparing(reader -> reader.getItems().size()));
    }

    public List<Reader> findReadersWithoutItems() {
        return readers.values().stream()
                .filter(reader -> reader.getItems().isEmpty())
                .toList();
    }

    public List<LibraryItem> findItems(Predicate<LibraryItem> condition) {
        return libraryItems.stream()
                .filter(condition)
                .toList();

    }

}
