import testdata.TestData;
import service.Library;
import storage.LibraryStorage;
import ui.ConsoleMenu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        Path path = Path.of("data");
        LibraryStorage storage = new LibraryStorage();

        try {
            Library library;
            if (Files.notExists(path)) {
                library = TestData.createLibrary();
                Files.createDirectories(path);
            } else {
                library = storage.load(path);
            }
            List<String> warnings = storage.getWarnings();
            for (String warning : warnings) {
                System.out.println(warning);
            }
            ConsoleMenu menu = new ConsoleMenu(library);
            menu.run();
            storage.save(library, path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}