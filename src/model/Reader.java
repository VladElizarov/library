package model;

import exceptions.ItemNotFoundException;
import exceptions.LimitExceededException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Reader {

    private static final int PUBLICATIONS_LIMIT = 3;
    private final String name;
    private final int id;
    private static int nextId = 0;
    private final List<LibraryItem> borrowedItems = new ArrayList<>();


    public Reader(String name) {
        this(nextId, name);
    }

    public Reader(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        this.id = id;
        this.name = name;
        nextId = Math.max(nextId, id + 1);
    }

    public int getId() {
        return id;
    }
    public String convertToCSV () {
        return id + ";" + name;
    }


    public void addItem(LibraryItem libraryItem) {
        if (libraryItem == null) {
            throw new IllegalArgumentException("Аргумент не должен быть null");
        }
        if (!canTakeMore()) {
            throw new LimitExceededException("У вас достигнут лимит взятых изданий: " + borrowedItems.size() + "/" + PUBLICATIONS_LIMIT);
        }
        borrowedItems.add(libraryItem);
    }

    public boolean canTakeMore() {
        return borrowedItems.size() < PUBLICATIONS_LIMIT;
    }

    public void removeItem(LibraryItem libraryItem) {
        if (libraryItem == null) {
            throw new IllegalArgumentException("Аргумент не должен быть null");
        }
        if (!borrowedItems.remove(libraryItem)) {
            throw new IllegalArgumentException("Читатель " + name + " не брал это издание");
        }
    }


    public List<LibraryItem> getItems() {
        return List.copyOf(borrowedItems);
    }


    @Override
    public String toString() {
        return "id: " + id + " " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Reader reader)) return false;
        return id == reader.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public LibraryItem findItemByName(String title) {
        return borrowedItems.stream()
                .filter(x -> x.getName().equalsIgnoreCase(title))
                .findFirst().orElseThrow(
                        () -> new ItemNotFoundException("У читателя " + name + " нет издания: " + title));
    }
}
