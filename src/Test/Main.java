package Test;

import model.LibraryItem;

import java.util.Comparator;

import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {

        TreeSet<LibraryItem> set = new TreeSet<>(new Comparator<LibraryItem>() {
            @Override
            public int compare(LibraryItem o1, LibraryItem o2) {
                if (o1.getName().compareTo(o2.getName()) == 0) {
                    return Integer.compare(o1.getId(), o2.getId());
                }
                return o1.getName().compareTo(o2.getName());
            }
        });

        Task4 task4 = new Task4(set);
        task4.addItems();
        for (LibraryItem item : set) {
            System.out.println(item);
        }

        System.out.println(set.size());

    }
    // добавил в компаратор проверку по id, так как раньше проверялись
    // только имена и по ним объекты считались одинаковыми, а потому не попадали в множество
}
