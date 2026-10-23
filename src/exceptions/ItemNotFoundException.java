package exceptions;


public class ItemNotFoundException extends LibraryException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
