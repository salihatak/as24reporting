package exception;

public class FileFormatException extends RuntimeException {
    public FileFormatException(String message) {
        super(message);
    }
    public FileFormatException(String message, Throwable e) {
        super(message, e);
    }
}
