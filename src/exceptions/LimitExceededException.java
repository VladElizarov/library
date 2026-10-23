package exceptions;

public class LimitExceededException extends LibraryException {
    public LimitExceededException(String message) {
        super(message);
    }
}
