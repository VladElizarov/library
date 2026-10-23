package model;

import exceptions.ItemAvailabilityException;

import java.util.Objects;

public abstract class LibraryItem {

    private final int id;
    private final String name;
    private boolean isAvailable;
    private static int nextId = 0;

    public LibraryItem(String name) {
        this(nextId, name);
    }

    public LibraryItem(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым.");
        }
        this.id = id;
        this.name = name;
        this.isAvailable = true;
        nextId = Math.max(nextId, id + 1);
    }

    public String convertToCSV() {
        return id +";" + name;
    }

    public abstract int getLoanDays();
    public abstract String getTypeName();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return isAvailable ? "id: " + id + " название издания: " + name : "ВЫДАНО id: " + id + " название издания: " + name;
    }

    public void markIssued() {
        if (!isAvailable) {
            throw new ItemAvailabilityException("Издание " + name + " с id " + id + " уже было выдано. ");

        }
        isAvailable = false;
    }

    public void markReturned() {
        if (isAvailable) {
            throw new ItemAvailabilityException("Издание " + name + " с id " + id + " не числится выданным. ");
        }
        isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LibraryItem libraryItem)) return false;
        return id == libraryItem.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
